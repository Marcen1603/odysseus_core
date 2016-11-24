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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_INT", "RULE_FLOAT_NUMBER", "RULE_STRING", "RULE_ID", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'TRUE'", "'FALSE'", "'INTEGER'", "'DOUBLE'", "'FLOAT'", "'STRING'", "'BOOLEAN'", "'STARTTIMESTAMP'", "'ENDTIMESTAMP'", "'=='", "'!='", "'>='", "'<='", "'<'", "'>'", "'*'", "'/'", "'STREAM'", "'VIEW'", "'SINK'", "';'", "'OR'", "'AND'", "'+'", "'-'", "'('", "')'", "'NOT'", "'DISTINCT'", "'FROM'", "','", "'WHERE'", "'['", "'UNBOUNDED'", "']'", "'SIZE'", "'TIME'", "'ADVANCE'", "'PARTITION'", "'BY'", "'TUPLE'", "'CREATE'", "'CHANNEL'", "':'", "'SELECT'"
    };
    public static final int T__50=50;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__55=55;
    public static final int T__12=12;
    public static final int T__56=56;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
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
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__46=46;
    public static final int T__47=47;
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

                if ( (LA1_0==53||LA1_0==56) ) {
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


    // $ANTLR start "entryRuleWindow"
    // InternalCQL.g:453:1: entryRuleWindow : ruleWindow EOF ;
    public final void entryRuleWindow() throws RecognitionException {
        try {
            // InternalCQL.g:454:1: ( ruleWindow EOF )
            // InternalCQL.g:455:1: ruleWindow EOF
            {
             before(grammarAccess.getWindowRule()); 
            pushFollow(FOLLOW_1);
            ruleWindow();

            state._fsp--;

             after(grammarAccess.getWindowRule()); 
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
    // $ANTLR end "entryRuleWindow"


    // $ANTLR start "ruleWindow"
    // InternalCQL.g:462:1: ruleWindow : ( ( rule__Window__Group__0 ) ) ;
    public final void ruleWindow() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:466:2: ( ( ( rule__Window__Group__0 ) ) )
            // InternalCQL.g:467:2: ( ( rule__Window__Group__0 ) )
            {
            // InternalCQL.g:467:2: ( ( rule__Window__Group__0 ) )
            // InternalCQL.g:468:3: ( rule__Window__Group__0 )
            {
             before(grammarAccess.getWindowAccess().getGroup()); 
            // InternalCQL.g:469:3: ( rule__Window__Group__0 )
            // InternalCQL.g:469:4: rule__Window__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Window__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getWindowAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleWindow"


    // $ANTLR start "entryRuleWindow_Unbounded"
    // InternalCQL.g:478:1: entryRuleWindow_Unbounded : ruleWindow_Unbounded EOF ;
    public final void entryRuleWindow_Unbounded() throws RecognitionException {
        try {
            // InternalCQL.g:479:1: ( ruleWindow_Unbounded EOF )
            // InternalCQL.g:480:1: ruleWindow_Unbounded EOF
            {
             before(grammarAccess.getWindow_UnboundedRule()); 
            pushFollow(FOLLOW_1);
            ruleWindow_Unbounded();

            state._fsp--;

             after(grammarAccess.getWindow_UnboundedRule()); 
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
    // $ANTLR end "entryRuleWindow_Unbounded"


    // $ANTLR start "ruleWindow_Unbounded"
    // InternalCQL.g:487:1: ruleWindow_Unbounded : ( ( rule__Window_Unbounded__Group__0 ) ) ;
    public final void ruleWindow_Unbounded() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:491:2: ( ( ( rule__Window_Unbounded__Group__0 ) ) )
            // InternalCQL.g:492:2: ( ( rule__Window_Unbounded__Group__0 ) )
            {
            // InternalCQL.g:492:2: ( ( rule__Window_Unbounded__Group__0 ) )
            // InternalCQL.g:493:3: ( rule__Window_Unbounded__Group__0 )
            {
             before(grammarAccess.getWindow_UnboundedAccess().getGroup()); 
            // InternalCQL.g:494:3: ( rule__Window_Unbounded__Group__0 )
            // InternalCQL.g:494:4: rule__Window_Unbounded__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Window_Unbounded__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getWindow_UnboundedAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleWindow_Unbounded"


    // $ANTLR start "entryRuleWindow_Timebased"
    // InternalCQL.g:503:1: entryRuleWindow_Timebased : ruleWindow_Timebased EOF ;
    public final void entryRuleWindow_Timebased() throws RecognitionException {
        try {
            // InternalCQL.g:504:1: ( ruleWindow_Timebased EOF )
            // InternalCQL.g:505:1: ruleWindow_Timebased EOF
            {
             before(grammarAccess.getWindow_TimebasedRule()); 
            pushFollow(FOLLOW_1);
            ruleWindow_Timebased();

            state._fsp--;

             after(grammarAccess.getWindow_TimebasedRule()); 
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
    // $ANTLR end "entryRuleWindow_Timebased"


    // $ANTLR start "ruleWindow_Timebased"
    // InternalCQL.g:512:1: ruleWindow_Timebased : ( ( rule__Window_Timebased__Group__0 ) ) ;
    public final void ruleWindow_Timebased() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:516:2: ( ( ( rule__Window_Timebased__Group__0 ) ) )
            // InternalCQL.g:517:2: ( ( rule__Window_Timebased__Group__0 ) )
            {
            // InternalCQL.g:517:2: ( ( rule__Window_Timebased__Group__0 ) )
            // InternalCQL.g:518:3: ( rule__Window_Timebased__Group__0 )
            {
             before(grammarAccess.getWindow_TimebasedAccess().getGroup()); 
            // InternalCQL.g:519:3: ( rule__Window_Timebased__Group__0 )
            // InternalCQL.g:519:4: rule__Window_Timebased__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TimebasedAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleWindow_Timebased"


    // $ANTLR start "entryRuleWindow_Tuplebased"
    // InternalCQL.g:528:1: entryRuleWindow_Tuplebased : ruleWindow_Tuplebased EOF ;
    public final void entryRuleWindow_Tuplebased() throws RecognitionException {
        try {
            // InternalCQL.g:529:1: ( ruleWindow_Tuplebased EOF )
            // InternalCQL.g:530:1: ruleWindow_Tuplebased EOF
            {
             before(grammarAccess.getWindow_TuplebasedRule()); 
            pushFollow(FOLLOW_1);
            ruleWindow_Tuplebased();

            state._fsp--;

             after(grammarAccess.getWindow_TuplebasedRule()); 
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
    // $ANTLR end "entryRuleWindow_Tuplebased"


    // $ANTLR start "ruleWindow_Tuplebased"
    // InternalCQL.g:537:1: ruleWindow_Tuplebased : ( ( rule__Window_Tuplebased__Group__0 ) ) ;
    public final void ruleWindow_Tuplebased() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:541:2: ( ( ( rule__Window_Tuplebased__Group__0 ) ) )
            // InternalCQL.g:542:2: ( ( rule__Window_Tuplebased__Group__0 ) )
            {
            // InternalCQL.g:542:2: ( ( rule__Window_Tuplebased__Group__0 ) )
            // InternalCQL.g:543:3: ( rule__Window_Tuplebased__Group__0 )
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getGroup()); 
            // InternalCQL.g:544:3: ( rule__Window_Tuplebased__Group__0 )
            // InternalCQL.g:544:4: rule__Window_Tuplebased__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TuplebasedAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleWindow_Tuplebased"


    // $ANTLR start "entryRuleCreate_Statement"
    // InternalCQL.g:553:1: entryRuleCreate_Statement : ruleCreate_Statement EOF ;
    public final void entryRuleCreate_Statement() throws RecognitionException {
        try {
            // InternalCQL.g:554:1: ( ruleCreate_Statement EOF )
            // InternalCQL.g:555:1: ruleCreate_Statement EOF
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
    // InternalCQL.g:562:1: ruleCreate_Statement : ( ( rule__Create_Statement__Group__0 ) ) ;
    public final void ruleCreate_Statement() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:566:2: ( ( ( rule__Create_Statement__Group__0 ) ) )
            // InternalCQL.g:567:2: ( ( rule__Create_Statement__Group__0 ) )
            {
            // InternalCQL.g:567:2: ( ( rule__Create_Statement__Group__0 ) )
            // InternalCQL.g:568:3: ( rule__Create_Statement__Group__0 )
            {
             before(grammarAccess.getCreate_StatementAccess().getGroup()); 
            // InternalCQL.g:569:3: ( rule__Create_Statement__Group__0 )
            // InternalCQL.g:569:4: rule__Create_Statement__Group__0
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
    // InternalCQL.g:577:1: rule__Statement__Alternatives_0 : ( ( ( rule__Statement__TypeAssignment_0_0 ) ) | ( ( rule__Statement__TypeAssignment_0_1 ) ) );
    public final void rule__Statement__Alternatives_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:581:1: ( ( ( rule__Statement__TypeAssignment_0_0 ) ) | ( ( rule__Statement__TypeAssignment_0_1 ) ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==56) ) {
                alt2=1;
            }
            else if ( (LA2_0==53) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // InternalCQL.g:582:2: ( ( rule__Statement__TypeAssignment_0_0 ) )
                    {
                    // InternalCQL.g:582:2: ( ( rule__Statement__TypeAssignment_0_0 ) )
                    // InternalCQL.g:583:3: ( rule__Statement__TypeAssignment_0_0 )
                    {
                     before(grammarAccess.getStatementAccess().getTypeAssignment_0_0()); 
                    // InternalCQL.g:584:3: ( rule__Statement__TypeAssignment_0_0 )
                    // InternalCQL.g:584:4: rule__Statement__TypeAssignment_0_0
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
                    // InternalCQL.g:588:2: ( ( rule__Statement__TypeAssignment_0_1 ) )
                    {
                    // InternalCQL.g:588:2: ( ( rule__Statement__TypeAssignment_0_1 ) )
                    // InternalCQL.g:589:3: ( rule__Statement__TypeAssignment_0_1 )
                    {
                     before(grammarAccess.getStatementAccess().getTypeAssignment_0_1()); 
                    // InternalCQL.g:590:3: ( rule__Statement__TypeAssignment_0_1 )
                    // InternalCQL.g:590:4: rule__Statement__TypeAssignment_0_1
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
    // InternalCQL.g:598:1: rule__Atomic__Alternatives : ( ( ( rule__Atomic__Group_0__0 ) ) | ( ( rule__Atomic__Group_1__0 ) ) | ( ( rule__Atomic__Group_2__0 ) ) | ( ( rule__Atomic__Group_3__0 ) ) | ( ( rule__Atomic__Group_4__0 ) ) );
    public final void rule__Atomic__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:602:1: ( ( ( rule__Atomic__Group_0__0 ) ) | ( ( rule__Atomic__Group_1__0 ) ) | ( ( rule__Atomic__Group_2__0 ) ) | ( ( rule__Atomic__Group_3__0 ) ) | ( ( rule__Atomic__Group_4__0 ) ) )
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
                    // InternalCQL.g:603:2: ( ( rule__Atomic__Group_0__0 ) )
                    {
                    // InternalCQL.g:603:2: ( ( rule__Atomic__Group_0__0 ) )
                    // InternalCQL.g:604:3: ( rule__Atomic__Group_0__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_0()); 
                    // InternalCQL.g:605:3: ( rule__Atomic__Group_0__0 )
                    // InternalCQL.g:605:4: rule__Atomic__Group_0__0
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
                    // InternalCQL.g:609:2: ( ( rule__Atomic__Group_1__0 ) )
                    {
                    // InternalCQL.g:609:2: ( ( rule__Atomic__Group_1__0 ) )
                    // InternalCQL.g:610:3: ( rule__Atomic__Group_1__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_1()); 
                    // InternalCQL.g:611:3: ( rule__Atomic__Group_1__0 )
                    // InternalCQL.g:611:4: rule__Atomic__Group_1__0
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
                    // InternalCQL.g:615:2: ( ( rule__Atomic__Group_2__0 ) )
                    {
                    // InternalCQL.g:615:2: ( ( rule__Atomic__Group_2__0 ) )
                    // InternalCQL.g:616:3: ( rule__Atomic__Group_2__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_2()); 
                    // InternalCQL.g:617:3: ( rule__Atomic__Group_2__0 )
                    // InternalCQL.g:617:4: rule__Atomic__Group_2__0
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
                    // InternalCQL.g:621:2: ( ( rule__Atomic__Group_3__0 ) )
                    {
                    // InternalCQL.g:621:2: ( ( rule__Atomic__Group_3__0 ) )
                    // InternalCQL.g:622:3: ( rule__Atomic__Group_3__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_3()); 
                    // InternalCQL.g:623:3: ( rule__Atomic__Group_3__0 )
                    // InternalCQL.g:623:4: rule__Atomic__Group_3__0
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
                    // InternalCQL.g:627:2: ( ( rule__Atomic__Group_4__0 ) )
                    {
                    // InternalCQL.g:627:2: ( ( rule__Atomic__Group_4__0 ) )
                    // InternalCQL.g:628:3: ( rule__Atomic__Group_4__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_4()); 
                    // InternalCQL.g:629:3: ( rule__Atomic__Group_4__0 )
                    // InternalCQL.g:629:4: rule__Atomic__Group_4__0
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
    // InternalCQL.g:637:1: rule__Atomic__ValueAlternatives_3_1_0 : ( ( 'TRUE' ) | ( 'FALSE' ) );
    public final void rule__Atomic__ValueAlternatives_3_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:641:1: ( ( 'TRUE' ) | ( 'FALSE' ) )
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
                    // InternalCQL.g:642:2: ( 'TRUE' )
                    {
                    // InternalCQL.g:642:2: ( 'TRUE' )
                    // InternalCQL.g:643:3: 'TRUE'
                    {
                     before(grammarAccess.getAtomicAccess().getValueTRUEKeyword_3_1_0_0()); 
                    match(input,12,FOLLOW_2); 
                     after(grammarAccess.getAtomicAccess().getValueTRUEKeyword_3_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:648:2: ( 'FALSE' )
                    {
                    // InternalCQL.g:648:2: ( 'FALSE' )
                    // InternalCQL.g:649:3: 'FALSE'
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
    // InternalCQL.g:658:1: rule__DataType__Alternatives : ( ( 'INTEGER' ) | ( 'DOUBLE' ) | ( 'FLOAT' ) | ( 'STRING' ) | ( 'BOOLEAN' ) | ( 'STARTTIMESTAMP' ) | ( 'ENDTIMESTAMP' ) );
    public final void rule__DataType__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:662:1: ( ( 'INTEGER' ) | ( 'DOUBLE' ) | ( 'FLOAT' ) | ( 'STRING' ) | ( 'BOOLEAN' ) | ( 'STARTTIMESTAMP' ) | ( 'ENDTIMESTAMP' ) )
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
                    // InternalCQL.g:663:2: ( 'INTEGER' )
                    {
                    // InternalCQL.g:663:2: ( 'INTEGER' )
                    // InternalCQL.g:664:3: 'INTEGER'
                    {
                     before(grammarAccess.getDataTypeAccess().getINTEGERKeyword_0()); 
                    match(input,14,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getINTEGERKeyword_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:669:2: ( 'DOUBLE' )
                    {
                    // InternalCQL.g:669:2: ( 'DOUBLE' )
                    // InternalCQL.g:670:3: 'DOUBLE'
                    {
                     before(grammarAccess.getDataTypeAccess().getDOUBLEKeyword_1()); 
                    match(input,15,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getDOUBLEKeyword_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:675:2: ( 'FLOAT' )
                    {
                    // InternalCQL.g:675:2: ( 'FLOAT' )
                    // InternalCQL.g:676:3: 'FLOAT'
                    {
                     before(grammarAccess.getDataTypeAccess().getFLOATKeyword_2()); 
                    match(input,16,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getFLOATKeyword_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalCQL.g:681:2: ( 'STRING' )
                    {
                    // InternalCQL.g:681:2: ( 'STRING' )
                    // InternalCQL.g:682:3: 'STRING'
                    {
                     before(grammarAccess.getDataTypeAccess().getSTRINGKeyword_3()); 
                    match(input,17,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getSTRINGKeyword_3()); 

                    }


                    }
                    break;
                case 5 :
                    // InternalCQL.g:687:2: ( 'BOOLEAN' )
                    {
                    // InternalCQL.g:687:2: ( 'BOOLEAN' )
                    // InternalCQL.g:688:3: 'BOOLEAN'
                    {
                     before(grammarAccess.getDataTypeAccess().getBOOLEANKeyword_4()); 
                    match(input,18,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getBOOLEANKeyword_4()); 

                    }


                    }
                    break;
                case 6 :
                    // InternalCQL.g:693:2: ( 'STARTTIMESTAMP' )
                    {
                    // InternalCQL.g:693:2: ( 'STARTTIMESTAMP' )
                    // InternalCQL.g:694:3: 'STARTTIMESTAMP'
                    {
                     before(grammarAccess.getDataTypeAccess().getSTARTTIMESTAMPKeyword_5()); 
                    match(input,19,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getSTARTTIMESTAMPKeyword_5()); 

                    }


                    }
                    break;
                case 7 :
                    // InternalCQL.g:699:2: ( 'ENDTIMESTAMP' )
                    {
                    // InternalCQL.g:699:2: ( 'ENDTIMESTAMP' )
                    // InternalCQL.g:700:3: 'ENDTIMESTAMP'
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
    // InternalCQL.g:709:1: rule__Equalitiy__OpAlternatives_1_1_0 : ( ( '==' ) | ( '!=' ) );
    public final void rule__Equalitiy__OpAlternatives_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:713:1: ( ( '==' ) | ( '!=' ) )
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
                    // InternalCQL.g:714:2: ( '==' )
                    {
                    // InternalCQL.g:714:2: ( '==' )
                    // InternalCQL.g:715:3: '=='
                    {
                     before(grammarAccess.getEqualitiyAccess().getOpEqualsSignEqualsSignKeyword_1_1_0_0()); 
                    match(input,21,FOLLOW_2); 
                     after(grammarAccess.getEqualitiyAccess().getOpEqualsSignEqualsSignKeyword_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:720:2: ( '!=' )
                    {
                    // InternalCQL.g:720:2: ( '!=' )
                    // InternalCQL.g:721:3: '!='
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
    // InternalCQL.g:730:1: rule__Comparison__OpAlternatives_1_1_0 : ( ( '>=' ) | ( '<=' ) | ( '<' ) | ( '>' ) );
    public final void rule__Comparison__OpAlternatives_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:734:1: ( ( '>=' ) | ( '<=' ) | ( '<' ) | ( '>' ) )
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
                    // InternalCQL.g:735:2: ( '>=' )
                    {
                    // InternalCQL.g:735:2: ( '>=' )
                    // InternalCQL.g:736:3: '>='
                    {
                     before(grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0()); 
                    match(input,23,FOLLOW_2); 
                     after(grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:741:2: ( '<=' )
                    {
                    // InternalCQL.g:741:2: ( '<=' )
                    // InternalCQL.g:742:3: '<='
                    {
                     before(grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1()); 
                    match(input,24,FOLLOW_2); 
                     after(grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:747:2: ( '<' )
                    {
                    // InternalCQL.g:747:2: ( '<' )
                    // InternalCQL.g:748:3: '<'
                    {
                     before(grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2()); 
                    match(input,25,FOLLOW_2); 
                     after(grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalCQL.g:753:2: ( '>' )
                    {
                    // InternalCQL.g:753:2: ( '>' )
                    // InternalCQL.g:754:3: '>'
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
    // InternalCQL.g:763:1: rule__PlusOrMinus__Alternatives_1_0 : ( ( ( rule__PlusOrMinus__Group_1_0_0__0 ) ) | ( ( rule__PlusOrMinus__Group_1_0_1__0 ) ) );
    public final void rule__PlusOrMinus__Alternatives_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:767:1: ( ( ( rule__PlusOrMinus__Group_1_0_0__0 ) ) | ( ( rule__PlusOrMinus__Group_1_0_1__0 ) ) )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==35) ) {
                alt8=1;
            }
            else if ( (LA8_0==36) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // InternalCQL.g:768:2: ( ( rule__PlusOrMinus__Group_1_0_0__0 ) )
                    {
                    // InternalCQL.g:768:2: ( ( rule__PlusOrMinus__Group_1_0_0__0 ) )
                    // InternalCQL.g:769:3: ( rule__PlusOrMinus__Group_1_0_0__0 )
                    {
                     before(grammarAccess.getPlusOrMinusAccess().getGroup_1_0_0()); 
                    // InternalCQL.g:770:3: ( rule__PlusOrMinus__Group_1_0_0__0 )
                    // InternalCQL.g:770:4: rule__PlusOrMinus__Group_1_0_0__0
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
                    // InternalCQL.g:774:2: ( ( rule__PlusOrMinus__Group_1_0_1__0 ) )
                    {
                    // InternalCQL.g:774:2: ( ( rule__PlusOrMinus__Group_1_0_1__0 ) )
                    // InternalCQL.g:775:3: ( rule__PlusOrMinus__Group_1_0_1__0 )
                    {
                     before(grammarAccess.getPlusOrMinusAccess().getGroup_1_0_1()); 
                    // InternalCQL.g:776:3: ( rule__PlusOrMinus__Group_1_0_1__0 )
                    // InternalCQL.g:776:4: rule__PlusOrMinus__Group_1_0_1__0
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
    // InternalCQL.g:784:1: rule__MulOrDiv__OpAlternatives_1_1_0 : ( ( '*' ) | ( '/' ) );
    public final void rule__MulOrDiv__OpAlternatives_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:788:1: ( ( '*' ) | ( '/' ) )
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
                    // InternalCQL.g:789:2: ( '*' )
                    {
                    // InternalCQL.g:789:2: ( '*' )
                    // InternalCQL.g:790:3: '*'
                    {
                     before(grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0()); 
                    match(input,27,FOLLOW_2); 
                     after(grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:795:2: ( '/' )
                    {
                    // InternalCQL.g:795:2: ( '/' )
                    // InternalCQL.g:796:3: '/'
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
    // InternalCQL.g:805:1: rule__Primary__Alternatives : ( ( ( rule__Primary__Group_0__0 ) ) | ( ( rule__Primary__Group_1__0 ) ) | ( ruleAtomic ) );
    public final void rule__Primary__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:809:1: ( ( ( rule__Primary__Group_0__0 ) ) | ( ( rule__Primary__Group_1__0 ) ) | ( ruleAtomic ) )
            int alt10=3;
            switch ( input.LA(1) ) {
            case 37:
                {
                alt10=1;
                }
                break;
            case 39:
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
                    // InternalCQL.g:810:2: ( ( rule__Primary__Group_0__0 ) )
                    {
                    // InternalCQL.g:810:2: ( ( rule__Primary__Group_0__0 ) )
                    // InternalCQL.g:811:3: ( rule__Primary__Group_0__0 )
                    {
                     before(grammarAccess.getPrimaryAccess().getGroup_0()); 
                    // InternalCQL.g:812:3: ( rule__Primary__Group_0__0 )
                    // InternalCQL.g:812:4: rule__Primary__Group_0__0
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
                    // InternalCQL.g:816:2: ( ( rule__Primary__Group_1__0 ) )
                    {
                    // InternalCQL.g:816:2: ( ( rule__Primary__Group_1__0 ) )
                    // InternalCQL.g:817:3: ( rule__Primary__Group_1__0 )
                    {
                     before(grammarAccess.getPrimaryAccess().getGroup_1()); 
                    // InternalCQL.g:818:3: ( rule__Primary__Group_1__0 )
                    // InternalCQL.g:818:4: rule__Primary__Group_1__0
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
                    // InternalCQL.g:822:2: ( ruleAtomic )
                    {
                    // InternalCQL.g:822:2: ( ruleAtomic )
                    // InternalCQL.g:823:3: ruleAtomic
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
    // InternalCQL.g:832:1: rule__Select_Statement__Alternatives_2 : ( ( '*' ) | ( ( rule__Select_Statement__Group_2_1__0 ) ) );
    public final void rule__Select_Statement__Alternatives_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:836:1: ( ( '*' ) | ( ( rule__Select_Statement__Group_2_1__0 ) ) )
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
                    // InternalCQL.g:837:2: ( '*' )
                    {
                    // InternalCQL.g:837:2: ( '*' )
                    // InternalCQL.g:838:3: '*'
                    {
                     before(grammarAccess.getSelect_StatementAccess().getAsteriskKeyword_2_0()); 
                    match(input,27,FOLLOW_2); 
                     after(grammarAccess.getSelect_StatementAccess().getAsteriskKeyword_2_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:843:2: ( ( rule__Select_Statement__Group_2_1__0 ) )
                    {
                    // InternalCQL.g:843:2: ( ( rule__Select_Statement__Group_2_1__0 ) )
                    // InternalCQL.g:844:3: ( rule__Select_Statement__Group_2_1__0 )
                    {
                     before(grammarAccess.getSelect_StatementAccess().getGroup_2_1()); 
                    // InternalCQL.g:845:3: ( rule__Select_Statement__Group_2_1__0 )
                    // InternalCQL.g:845:4: rule__Select_Statement__Group_2_1__0
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


    // $ANTLR start "rule__Window__Alternatives_1"
    // InternalCQL.g:853:1: rule__Window__Alternatives_1 : ( ( ( rule__Window__TypeAssignment_1_0 ) ) | ( ( rule__Window__TypeAssignment_1_1 ) ) | ( ( rule__Window__TypeAssignment_1_2 ) ) );
    public final void rule__Window__Alternatives_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:857:1: ( ( ( rule__Window__TypeAssignment_1_0 ) ) | ( ( rule__Window__TypeAssignment_1_1 ) ) | ( ( rule__Window__TypeAssignment_1_2 ) ) )
            int alt12=3;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==44) ) {
                int LA12_1 = input.LA(2);

                if ( (LA12_1==45) ) {
                    alt12=1;
                }
                else if ( (LA12_1==47) ) {
                    int LA12_3 = input.LA(3);

                    if ( (LA12_3==RULE_INT) ) {
                        int LA12_4 = input.LA(4);

                        if ( (LA12_4==RULE_STRING) ) {
                            alt12=2;
                        }
                        else if ( (LA12_4==49||LA12_4==52) ) {
                            alt12=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 12, 4, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 12, 3, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA12_0==EOF||LA12_0==32||(LA12_0>=42 && LA12_0<=43)||LA12_0==53||LA12_0==56) ) {
                alt12=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // InternalCQL.g:858:2: ( ( rule__Window__TypeAssignment_1_0 ) )
                    {
                    // InternalCQL.g:858:2: ( ( rule__Window__TypeAssignment_1_0 ) )
                    // InternalCQL.g:859:3: ( rule__Window__TypeAssignment_1_0 )
                    {
                     before(grammarAccess.getWindowAccess().getTypeAssignment_1_0()); 
                    // InternalCQL.g:860:3: ( rule__Window__TypeAssignment_1_0 )
                    // InternalCQL.g:860:4: rule__Window__TypeAssignment_1_0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Window__TypeAssignment_1_0();

                    state._fsp--;


                    }

                     after(grammarAccess.getWindowAccess().getTypeAssignment_1_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:864:2: ( ( rule__Window__TypeAssignment_1_1 ) )
                    {
                    // InternalCQL.g:864:2: ( ( rule__Window__TypeAssignment_1_1 ) )
                    // InternalCQL.g:865:3: ( rule__Window__TypeAssignment_1_1 )
                    {
                     before(grammarAccess.getWindowAccess().getTypeAssignment_1_1()); 
                    // InternalCQL.g:866:3: ( rule__Window__TypeAssignment_1_1 )
                    // InternalCQL.g:866:4: rule__Window__TypeAssignment_1_1
                    {
                    pushFollow(FOLLOW_2);
                    rule__Window__TypeAssignment_1_1();

                    state._fsp--;


                    }

                     after(grammarAccess.getWindowAccess().getTypeAssignment_1_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:870:2: ( ( rule__Window__TypeAssignment_1_2 ) )
                    {
                    // InternalCQL.g:870:2: ( ( rule__Window__TypeAssignment_1_2 ) )
                    // InternalCQL.g:871:3: ( rule__Window__TypeAssignment_1_2 )
                    {
                     before(grammarAccess.getWindowAccess().getTypeAssignment_1_2()); 
                    // InternalCQL.g:872:3: ( rule__Window__TypeAssignment_1_2 )
                    // InternalCQL.g:872:4: rule__Window__TypeAssignment_1_2
                    {
                    pushFollow(FOLLOW_2);
                    rule__Window__TypeAssignment_1_2();

                    state._fsp--;


                    }

                     after(grammarAccess.getWindowAccess().getTypeAssignment_1_2()); 

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
    // $ANTLR end "rule__Window__Alternatives_1"


    // $ANTLR start "rule__Create_Statement__TypeAlternatives_1_0"
    // InternalCQL.g:880:1: rule__Create_Statement__TypeAlternatives_1_0 : ( ( 'STREAM' ) | ( 'VIEW' ) | ( 'SINK' ) );
    public final void rule__Create_Statement__TypeAlternatives_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:884:1: ( ( 'STREAM' ) | ( 'VIEW' ) | ( 'SINK' ) )
            int alt13=3;
            switch ( input.LA(1) ) {
            case 29:
                {
                alt13=1;
                }
                break;
            case 30:
                {
                alt13=2;
                }
                break;
            case 31:
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
                    // InternalCQL.g:885:2: ( 'STREAM' )
                    {
                    // InternalCQL.g:885:2: ( 'STREAM' )
                    // InternalCQL.g:886:3: 'STREAM'
                    {
                     before(grammarAccess.getCreate_StatementAccess().getTypeSTREAMKeyword_1_0_0()); 
                    match(input,29,FOLLOW_2); 
                     after(grammarAccess.getCreate_StatementAccess().getTypeSTREAMKeyword_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:891:2: ( 'VIEW' )
                    {
                    // InternalCQL.g:891:2: ( 'VIEW' )
                    // InternalCQL.g:892:3: 'VIEW'
                    {
                     before(grammarAccess.getCreate_StatementAccess().getTypeVIEWKeyword_1_0_1()); 
                    match(input,30,FOLLOW_2); 
                     after(grammarAccess.getCreate_StatementAccess().getTypeVIEWKeyword_1_0_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:897:2: ( 'SINK' )
                    {
                    // InternalCQL.g:897:2: ( 'SINK' )
                    // InternalCQL.g:898:3: 'SINK'
                    {
                     before(grammarAccess.getCreate_StatementAccess().getTypeSINKKeyword_1_0_2()); 
                    match(input,31,FOLLOW_2); 
                     after(grammarAccess.getCreate_StatementAccess().getTypeSINKKeyword_1_0_2()); 

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
    // $ANTLR end "rule__Create_Statement__TypeAlternatives_1_0"


    // $ANTLR start "rule__Statement__Group__0"
    // InternalCQL.g:907:1: rule__Statement__Group__0 : rule__Statement__Group__0__Impl rule__Statement__Group__1 ;
    public final void rule__Statement__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:911:1: ( rule__Statement__Group__0__Impl rule__Statement__Group__1 )
            // InternalCQL.g:912:2: rule__Statement__Group__0__Impl rule__Statement__Group__1
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
    // InternalCQL.g:919:1: rule__Statement__Group__0__Impl : ( ( rule__Statement__Alternatives_0 ) ) ;
    public final void rule__Statement__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:923:1: ( ( ( rule__Statement__Alternatives_0 ) ) )
            // InternalCQL.g:924:1: ( ( rule__Statement__Alternatives_0 ) )
            {
            // InternalCQL.g:924:1: ( ( rule__Statement__Alternatives_0 ) )
            // InternalCQL.g:925:2: ( rule__Statement__Alternatives_0 )
            {
             before(grammarAccess.getStatementAccess().getAlternatives_0()); 
            // InternalCQL.g:926:2: ( rule__Statement__Alternatives_0 )
            // InternalCQL.g:926:3: rule__Statement__Alternatives_0
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
    // InternalCQL.g:934:1: rule__Statement__Group__1 : rule__Statement__Group__1__Impl ;
    public final void rule__Statement__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:938:1: ( rule__Statement__Group__1__Impl )
            // InternalCQL.g:939:2: rule__Statement__Group__1__Impl
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
    // InternalCQL.g:945:1: rule__Statement__Group__1__Impl : ( ( ';' )? ) ;
    public final void rule__Statement__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:949:1: ( ( ( ';' )? ) )
            // InternalCQL.g:950:1: ( ( ';' )? )
            {
            // InternalCQL.g:950:1: ( ( ';' )? )
            // InternalCQL.g:951:2: ( ';' )?
            {
             before(grammarAccess.getStatementAccess().getSemicolonKeyword_1()); 
            // InternalCQL.g:952:2: ( ';' )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==32) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // InternalCQL.g:952:3: ';'
                    {
                    match(input,32,FOLLOW_2); 

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
    // InternalCQL.g:961:1: rule__Atomic__Group_0__0 : rule__Atomic__Group_0__0__Impl rule__Atomic__Group_0__1 ;
    public final void rule__Atomic__Group_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:965:1: ( rule__Atomic__Group_0__0__Impl rule__Atomic__Group_0__1 )
            // InternalCQL.g:966:2: rule__Atomic__Group_0__0__Impl rule__Atomic__Group_0__1
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
    // InternalCQL.g:973:1: rule__Atomic__Group_0__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:977:1: ( ( () ) )
            // InternalCQL.g:978:1: ( () )
            {
            // InternalCQL.g:978:1: ( () )
            // InternalCQL.g:979:2: ()
            {
             before(grammarAccess.getAtomicAccess().getIntConstantAction_0_0()); 
            // InternalCQL.g:980:2: ()
            // InternalCQL.g:980:3: 
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
    // InternalCQL.g:988:1: rule__Atomic__Group_0__1 : rule__Atomic__Group_0__1__Impl ;
    public final void rule__Atomic__Group_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:992:1: ( rule__Atomic__Group_0__1__Impl )
            // InternalCQL.g:993:2: rule__Atomic__Group_0__1__Impl
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
    // InternalCQL.g:999:1: rule__Atomic__Group_0__1__Impl : ( ( rule__Atomic__ValueAssignment_0_1 ) ) ;
    public final void rule__Atomic__Group_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1003:1: ( ( ( rule__Atomic__ValueAssignment_0_1 ) ) )
            // InternalCQL.g:1004:1: ( ( rule__Atomic__ValueAssignment_0_1 ) )
            {
            // InternalCQL.g:1004:1: ( ( rule__Atomic__ValueAssignment_0_1 ) )
            // InternalCQL.g:1005:2: ( rule__Atomic__ValueAssignment_0_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_0_1()); 
            // InternalCQL.g:1006:2: ( rule__Atomic__ValueAssignment_0_1 )
            // InternalCQL.g:1006:3: rule__Atomic__ValueAssignment_0_1
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
    // InternalCQL.g:1015:1: rule__Atomic__Group_1__0 : rule__Atomic__Group_1__0__Impl rule__Atomic__Group_1__1 ;
    public final void rule__Atomic__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1019:1: ( rule__Atomic__Group_1__0__Impl rule__Atomic__Group_1__1 )
            // InternalCQL.g:1020:2: rule__Atomic__Group_1__0__Impl rule__Atomic__Group_1__1
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
    // InternalCQL.g:1027:1: rule__Atomic__Group_1__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1031:1: ( ( () ) )
            // InternalCQL.g:1032:1: ( () )
            {
            // InternalCQL.g:1032:1: ( () )
            // InternalCQL.g:1033:2: ()
            {
             before(grammarAccess.getAtomicAccess().getFloatConstantAction_1_0()); 
            // InternalCQL.g:1034:2: ()
            // InternalCQL.g:1034:3: 
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
    // InternalCQL.g:1042:1: rule__Atomic__Group_1__1 : rule__Atomic__Group_1__1__Impl ;
    public final void rule__Atomic__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1046:1: ( rule__Atomic__Group_1__1__Impl )
            // InternalCQL.g:1047:2: rule__Atomic__Group_1__1__Impl
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
    // InternalCQL.g:1053:1: rule__Atomic__Group_1__1__Impl : ( ( rule__Atomic__ValueAssignment_1_1 ) ) ;
    public final void rule__Atomic__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1057:1: ( ( ( rule__Atomic__ValueAssignment_1_1 ) ) )
            // InternalCQL.g:1058:1: ( ( rule__Atomic__ValueAssignment_1_1 ) )
            {
            // InternalCQL.g:1058:1: ( ( rule__Atomic__ValueAssignment_1_1 ) )
            // InternalCQL.g:1059:2: ( rule__Atomic__ValueAssignment_1_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_1_1()); 
            // InternalCQL.g:1060:2: ( rule__Atomic__ValueAssignment_1_1 )
            // InternalCQL.g:1060:3: rule__Atomic__ValueAssignment_1_1
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
    // InternalCQL.g:1069:1: rule__Atomic__Group_2__0 : rule__Atomic__Group_2__0__Impl rule__Atomic__Group_2__1 ;
    public final void rule__Atomic__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1073:1: ( rule__Atomic__Group_2__0__Impl rule__Atomic__Group_2__1 )
            // InternalCQL.g:1074:2: rule__Atomic__Group_2__0__Impl rule__Atomic__Group_2__1
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
    // InternalCQL.g:1081:1: rule__Atomic__Group_2__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1085:1: ( ( () ) )
            // InternalCQL.g:1086:1: ( () )
            {
            // InternalCQL.g:1086:1: ( () )
            // InternalCQL.g:1087:2: ()
            {
             before(grammarAccess.getAtomicAccess().getStringConstantAction_2_0()); 
            // InternalCQL.g:1088:2: ()
            // InternalCQL.g:1088:3: 
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
    // InternalCQL.g:1096:1: rule__Atomic__Group_2__1 : rule__Atomic__Group_2__1__Impl ;
    public final void rule__Atomic__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1100:1: ( rule__Atomic__Group_2__1__Impl )
            // InternalCQL.g:1101:2: rule__Atomic__Group_2__1__Impl
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
    // InternalCQL.g:1107:1: rule__Atomic__Group_2__1__Impl : ( ( rule__Atomic__ValueAssignment_2_1 ) ) ;
    public final void rule__Atomic__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1111:1: ( ( ( rule__Atomic__ValueAssignment_2_1 ) ) )
            // InternalCQL.g:1112:1: ( ( rule__Atomic__ValueAssignment_2_1 ) )
            {
            // InternalCQL.g:1112:1: ( ( rule__Atomic__ValueAssignment_2_1 ) )
            // InternalCQL.g:1113:2: ( rule__Atomic__ValueAssignment_2_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_2_1()); 
            // InternalCQL.g:1114:2: ( rule__Atomic__ValueAssignment_2_1 )
            // InternalCQL.g:1114:3: rule__Atomic__ValueAssignment_2_1
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
    // InternalCQL.g:1123:1: rule__Atomic__Group_3__0 : rule__Atomic__Group_3__0__Impl rule__Atomic__Group_3__1 ;
    public final void rule__Atomic__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1127:1: ( rule__Atomic__Group_3__0__Impl rule__Atomic__Group_3__1 )
            // InternalCQL.g:1128:2: rule__Atomic__Group_3__0__Impl rule__Atomic__Group_3__1
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
    // InternalCQL.g:1135:1: rule__Atomic__Group_3__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1139:1: ( ( () ) )
            // InternalCQL.g:1140:1: ( () )
            {
            // InternalCQL.g:1140:1: ( () )
            // InternalCQL.g:1141:2: ()
            {
             before(grammarAccess.getAtomicAccess().getBoolConstantAction_3_0()); 
            // InternalCQL.g:1142:2: ()
            // InternalCQL.g:1142:3: 
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
    // InternalCQL.g:1150:1: rule__Atomic__Group_3__1 : rule__Atomic__Group_3__1__Impl ;
    public final void rule__Atomic__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1154:1: ( rule__Atomic__Group_3__1__Impl )
            // InternalCQL.g:1155:2: rule__Atomic__Group_3__1__Impl
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
    // InternalCQL.g:1161:1: rule__Atomic__Group_3__1__Impl : ( ( rule__Atomic__ValueAssignment_3_1 ) ) ;
    public final void rule__Atomic__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1165:1: ( ( ( rule__Atomic__ValueAssignment_3_1 ) ) )
            // InternalCQL.g:1166:1: ( ( rule__Atomic__ValueAssignment_3_1 ) )
            {
            // InternalCQL.g:1166:1: ( ( rule__Atomic__ValueAssignment_3_1 ) )
            // InternalCQL.g:1167:2: ( rule__Atomic__ValueAssignment_3_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_3_1()); 
            // InternalCQL.g:1168:2: ( rule__Atomic__ValueAssignment_3_1 )
            // InternalCQL.g:1168:3: rule__Atomic__ValueAssignment_3_1
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
    // InternalCQL.g:1177:1: rule__Atomic__Group_4__0 : rule__Atomic__Group_4__0__Impl rule__Atomic__Group_4__1 ;
    public final void rule__Atomic__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1181:1: ( rule__Atomic__Group_4__0__Impl rule__Atomic__Group_4__1 )
            // InternalCQL.g:1182:2: rule__Atomic__Group_4__0__Impl rule__Atomic__Group_4__1
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
    // InternalCQL.g:1189:1: rule__Atomic__Group_4__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1193:1: ( ( () ) )
            // InternalCQL.g:1194:1: ( () )
            {
            // InternalCQL.g:1194:1: ( () )
            // InternalCQL.g:1195:2: ()
            {
             before(grammarAccess.getAtomicAccess().getAttributeRefAction_4_0()); 
            // InternalCQL.g:1196:2: ()
            // InternalCQL.g:1196:3: 
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
    // InternalCQL.g:1204:1: rule__Atomic__Group_4__1 : rule__Atomic__Group_4__1__Impl ;
    public final void rule__Atomic__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1208:1: ( rule__Atomic__Group_4__1__Impl )
            // InternalCQL.g:1209:2: rule__Atomic__Group_4__1__Impl
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
    // InternalCQL.g:1215:1: rule__Atomic__Group_4__1__Impl : ( ( rule__Atomic__ValueAssignment_4_1 ) ) ;
    public final void rule__Atomic__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1219:1: ( ( ( rule__Atomic__ValueAssignment_4_1 ) ) )
            // InternalCQL.g:1220:1: ( ( rule__Atomic__ValueAssignment_4_1 ) )
            {
            // InternalCQL.g:1220:1: ( ( rule__Atomic__ValueAssignment_4_1 ) )
            // InternalCQL.g:1221:2: ( rule__Atomic__ValueAssignment_4_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_4_1()); 
            // InternalCQL.g:1222:2: ( rule__Atomic__ValueAssignment_4_1 )
            // InternalCQL.g:1222:3: rule__Atomic__ValueAssignment_4_1
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
    // InternalCQL.g:1231:1: rule__ExpressionsModel__Group__0 : rule__ExpressionsModel__Group__0__Impl rule__ExpressionsModel__Group__1 ;
    public final void rule__ExpressionsModel__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1235:1: ( rule__ExpressionsModel__Group__0__Impl rule__ExpressionsModel__Group__1 )
            // InternalCQL.g:1236:2: rule__ExpressionsModel__Group__0__Impl rule__ExpressionsModel__Group__1
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
    // InternalCQL.g:1243:1: rule__ExpressionsModel__Group__0__Impl : ( () ) ;
    public final void rule__ExpressionsModel__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1247:1: ( ( () ) )
            // InternalCQL.g:1248:1: ( () )
            {
            // InternalCQL.g:1248:1: ( () )
            // InternalCQL.g:1249:2: ()
            {
             before(grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0()); 
            // InternalCQL.g:1250:2: ()
            // InternalCQL.g:1250:3: 
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
    // InternalCQL.g:1258:1: rule__ExpressionsModel__Group__1 : rule__ExpressionsModel__Group__1__Impl ;
    public final void rule__ExpressionsModel__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1262:1: ( rule__ExpressionsModel__Group__1__Impl )
            // InternalCQL.g:1263:2: rule__ExpressionsModel__Group__1__Impl
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
    // InternalCQL.g:1269:1: rule__ExpressionsModel__Group__1__Impl : ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) ) ;
    public final void rule__ExpressionsModel__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1273:1: ( ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) ) )
            // InternalCQL.g:1274:1: ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) )
            {
            // InternalCQL.g:1274:1: ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) )
            // InternalCQL.g:1275:2: ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* )
            {
            // InternalCQL.g:1275:2: ( ( rule__ExpressionsModel__ElementsAssignment_1 ) )
            // InternalCQL.g:1276:3: ( rule__ExpressionsModel__ElementsAssignment_1 )
            {
             before(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1()); 
            // InternalCQL.g:1277:3: ( rule__ExpressionsModel__ElementsAssignment_1 )
            // InternalCQL.g:1277:4: rule__ExpressionsModel__ElementsAssignment_1
            {
            pushFollow(FOLLOW_11);
            rule__ExpressionsModel__ElementsAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1()); 

            }

            // InternalCQL.g:1280:2: ( ( rule__ExpressionsModel__ElementsAssignment_1 )* )
            // InternalCQL.g:1281:3: ( rule__ExpressionsModel__ElementsAssignment_1 )*
            {
             before(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1()); 
            // InternalCQL.g:1282:3: ( rule__ExpressionsModel__ElementsAssignment_1 )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( ((LA15_0>=RULE_INT && LA15_0<=RULE_ID)||(LA15_0>=12 && LA15_0<=13)||LA15_0==37||LA15_0==39) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // InternalCQL.g:1282:4: rule__ExpressionsModel__ElementsAssignment_1
            	    {
            	    pushFollow(FOLLOW_11);
            	    rule__ExpressionsModel__ElementsAssignment_1();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop15;
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
    // InternalCQL.g:1292:1: rule__Or__Group__0 : rule__Or__Group__0__Impl rule__Or__Group__1 ;
    public final void rule__Or__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1296:1: ( rule__Or__Group__0__Impl rule__Or__Group__1 )
            // InternalCQL.g:1297:2: rule__Or__Group__0__Impl rule__Or__Group__1
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
    // InternalCQL.g:1304:1: rule__Or__Group__0__Impl : ( ruleAnd ) ;
    public final void rule__Or__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1308:1: ( ( ruleAnd ) )
            // InternalCQL.g:1309:1: ( ruleAnd )
            {
            // InternalCQL.g:1309:1: ( ruleAnd )
            // InternalCQL.g:1310:2: ruleAnd
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
    // InternalCQL.g:1319:1: rule__Or__Group__1 : rule__Or__Group__1__Impl ;
    public final void rule__Or__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1323:1: ( rule__Or__Group__1__Impl )
            // InternalCQL.g:1324:2: rule__Or__Group__1__Impl
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
    // InternalCQL.g:1330:1: rule__Or__Group__1__Impl : ( ( rule__Or__Group_1__0 )* ) ;
    public final void rule__Or__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1334:1: ( ( ( rule__Or__Group_1__0 )* ) )
            // InternalCQL.g:1335:1: ( ( rule__Or__Group_1__0 )* )
            {
            // InternalCQL.g:1335:1: ( ( rule__Or__Group_1__0 )* )
            // InternalCQL.g:1336:2: ( rule__Or__Group_1__0 )*
            {
             before(grammarAccess.getOrAccess().getGroup_1()); 
            // InternalCQL.g:1337:2: ( rule__Or__Group_1__0 )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==33) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // InternalCQL.g:1337:3: rule__Or__Group_1__0
            	    {
            	    pushFollow(FOLLOW_13);
            	    rule__Or__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop16;
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
    // InternalCQL.g:1346:1: rule__Or__Group_1__0 : rule__Or__Group_1__0__Impl rule__Or__Group_1__1 ;
    public final void rule__Or__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1350:1: ( rule__Or__Group_1__0__Impl rule__Or__Group_1__1 )
            // InternalCQL.g:1351:2: rule__Or__Group_1__0__Impl rule__Or__Group_1__1
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
    // InternalCQL.g:1358:1: rule__Or__Group_1__0__Impl : ( () ) ;
    public final void rule__Or__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1362:1: ( ( () ) )
            // InternalCQL.g:1363:1: ( () )
            {
            // InternalCQL.g:1363:1: ( () )
            // InternalCQL.g:1364:2: ()
            {
             before(grammarAccess.getOrAccess().getOrLeftAction_1_0()); 
            // InternalCQL.g:1365:2: ()
            // InternalCQL.g:1365:3: 
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
    // InternalCQL.g:1373:1: rule__Or__Group_1__1 : rule__Or__Group_1__1__Impl rule__Or__Group_1__2 ;
    public final void rule__Or__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1377:1: ( rule__Or__Group_1__1__Impl rule__Or__Group_1__2 )
            // InternalCQL.g:1378:2: rule__Or__Group_1__1__Impl rule__Or__Group_1__2
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
    // InternalCQL.g:1385:1: rule__Or__Group_1__1__Impl : ( 'OR' ) ;
    public final void rule__Or__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1389:1: ( ( 'OR' ) )
            // InternalCQL.g:1390:1: ( 'OR' )
            {
            // InternalCQL.g:1390:1: ( 'OR' )
            // InternalCQL.g:1391:2: 'OR'
            {
             before(grammarAccess.getOrAccess().getORKeyword_1_1()); 
            match(input,33,FOLLOW_2); 
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
    // InternalCQL.g:1400:1: rule__Or__Group_1__2 : rule__Or__Group_1__2__Impl ;
    public final void rule__Or__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1404:1: ( rule__Or__Group_1__2__Impl )
            // InternalCQL.g:1405:2: rule__Or__Group_1__2__Impl
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
    // InternalCQL.g:1411:1: rule__Or__Group_1__2__Impl : ( ( rule__Or__RightAssignment_1_2 ) ) ;
    public final void rule__Or__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1415:1: ( ( ( rule__Or__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1416:1: ( ( rule__Or__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1416:1: ( ( rule__Or__RightAssignment_1_2 ) )
            // InternalCQL.g:1417:2: ( rule__Or__RightAssignment_1_2 )
            {
             before(grammarAccess.getOrAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1418:2: ( rule__Or__RightAssignment_1_2 )
            // InternalCQL.g:1418:3: rule__Or__RightAssignment_1_2
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
    // InternalCQL.g:1427:1: rule__And__Group__0 : rule__And__Group__0__Impl rule__And__Group__1 ;
    public final void rule__And__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1431:1: ( rule__And__Group__0__Impl rule__And__Group__1 )
            // InternalCQL.g:1432:2: rule__And__Group__0__Impl rule__And__Group__1
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
    // InternalCQL.g:1439:1: rule__And__Group__0__Impl : ( ruleEqualitiy ) ;
    public final void rule__And__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1443:1: ( ( ruleEqualitiy ) )
            // InternalCQL.g:1444:1: ( ruleEqualitiy )
            {
            // InternalCQL.g:1444:1: ( ruleEqualitiy )
            // InternalCQL.g:1445:2: ruleEqualitiy
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
    // InternalCQL.g:1454:1: rule__And__Group__1 : rule__And__Group__1__Impl ;
    public final void rule__And__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1458:1: ( rule__And__Group__1__Impl )
            // InternalCQL.g:1459:2: rule__And__Group__1__Impl
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
    // InternalCQL.g:1465:1: rule__And__Group__1__Impl : ( ( rule__And__Group_1__0 )* ) ;
    public final void rule__And__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1469:1: ( ( ( rule__And__Group_1__0 )* ) )
            // InternalCQL.g:1470:1: ( ( rule__And__Group_1__0 )* )
            {
            // InternalCQL.g:1470:1: ( ( rule__And__Group_1__0 )* )
            // InternalCQL.g:1471:2: ( rule__And__Group_1__0 )*
            {
             before(grammarAccess.getAndAccess().getGroup_1()); 
            // InternalCQL.g:1472:2: ( rule__And__Group_1__0 )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==34) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // InternalCQL.g:1472:3: rule__And__Group_1__0
            	    {
            	    pushFollow(FOLLOW_15);
            	    rule__And__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop17;
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
    // InternalCQL.g:1481:1: rule__And__Group_1__0 : rule__And__Group_1__0__Impl rule__And__Group_1__1 ;
    public final void rule__And__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1485:1: ( rule__And__Group_1__0__Impl rule__And__Group_1__1 )
            // InternalCQL.g:1486:2: rule__And__Group_1__0__Impl rule__And__Group_1__1
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
    // InternalCQL.g:1493:1: rule__And__Group_1__0__Impl : ( () ) ;
    public final void rule__And__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1497:1: ( ( () ) )
            // InternalCQL.g:1498:1: ( () )
            {
            // InternalCQL.g:1498:1: ( () )
            // InternalCQL.g:1499:2: ()
            {
             before(grammarAccess.getAndAccess().getAndLeftAction_1_0()); 
            // InternalCQL.g:1500:2: ()
            // InternalCQL.g:1500:3: 
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
    // InternalCQL.g:1508:1: rule__And__Group_1__1 : rule__And__Group_1__1__Impl rule__And__Group_1__2 ;
    public final void rule__And__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1512:1: ( rule__And__Group_1__1__Impl rule__And__Group_1__2 )
            // InternalCQL.g:1513:2: rule__And__Group_1__1__Impl rule__And__Group_1__2
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
    // InternalCQL.g:1520:1: rule__And__Group_1__1__Impl : ( 'AND' ) ;
    public final void rule__And__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1524:1: ( ( 'AND' ) )
            // InternalCQL.g:1525:1: ( 'AND' )
            {
            // InternalCQL.g:1525:1: ( 'AND' )
            // InternalCQL.g:1526:2: 'AND'
            {
             before(grammarAccess.getAndAccess().getANDKeyword_1_1()); 
            match(input,34,FOLLOW_2); 
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
    // InternalCQL.g:1535:1: rule__And__Group_1__2 : rule__And__Group_1__2__Impl ;
    public final void rule__And__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1539:1: ( rule__And__Group_1__2__Impl )
            // InternalCQL.g:1540:2: rule__And__Group_1__2__Impl
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
    // InternalCQL.g:1546:1: rule__And__Group_1__2__Impl : ( ( rule__And__RightAssignment_1_2 ) ) ;
    public final void rule__And__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1550:1: ( ( ( rule__And__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1551:1: ( ( rule__And__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1551:1: ( ( rule__And__RightAssignment_1_2 ) )
            // InternalCQL.g:1552:2: ( rule__And__RightAssignment_1_2 )
            {
             before(grammarAccess.getAndAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1553:2: ( rule__And__RightAssignment_1_2 )
            // InternalCQL.g:1553:3: rule__And__RightAssignment_1_2
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
    // InternalCQL.g:1562:1: rule__Equalitiy__Group__0 : rule__Equalitiy__Group__0__Impl rule__Equalitiy__Group__1 ;
    public final void rule__Equalitiy__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1566:1: ( rule__Equalitiy__Group__0__Impl rule__Equalitiy__Group__1 )
            // InternalCQL.g:1567:2: rule__Equalitiy__Group__0__Impl rule__Equalitiy__Group__1
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
    // InternalCQL.g:1574:1: rule__Equalitiy__Group__0__Impl : ( ruleComparison ) ;
    public final void rule__Equalitiy__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1578:1: ( ( ruleComparison ) )
            // InternalCQL.g:1579:1: ( ruleComparison )
            {
            // InternalCQL.g:1579:1: ( ruleComparison )
            // InternalCQL.g:1580:2: ruleComparison
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
    // InternalCQL.g:1589:1: rule__Equalitiy__Group__1 : rule__Equalitiy__Group__1__Impl ;
    public final void rule__Equalitiy__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1593:1: ( rule__Equalitiy__Group__1__Impl )
            // InternalCQL.g:1594:2: rule__Equalitiy__Group__1__Impl
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
    // InternalCQL.g:1600:1: rule__Equalitiy__Group__1__Impl : ( ( rule__Equalitiy__Group_1__0 )* ) ;
    public final void rule__Equalitiy__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1604:1: ( ( ( rule__Equalitiy__Group_1__0 )* ) )
            // InternalCQL.g:1605:1: ( ( rule__Equalitiy__Group_1__0 )* )
            {
            // InternalCQL.g:1605:1: ( ( rule__Equalitiy__Group_1__0 )* )
            // InternalCQL.g:1606:2: ( rule__Equalitiy__Group_1__0 )*
            {
             before(grammarAccess.getEqualitiyAccess().getGroup_1()); 
            // InternalCQL.g:1607:2: ( rule__Equalitiy__Group_1__0 )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( ((LA18_0>=21 && LA18_0<=22)) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // InternalCQL.g:1607:3: rule__Equalitiy__Group_1__0
            	    {
            	    pushFollow(FOLLOW_17);
            	    rule__Equalitiy__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop18;
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
    // InternalCQL.g:1616:1: rule__Equalitiy__Group_1__0 : rule__Equalitiy__Group_1__0__Impl rule__Equalitiy__Group_1__1 ;
    public final void rule__Equalitiy__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1620:1: ( rule__Equalitiy__Group_1__0__Impl rule__Equalitiy__Group_1__1 )
            // InternalCQL.g:1621:2: rule__Equalitiy__Group_1__0__Impl rule__Equalitiy__Group_1__1
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
    // InternalCQL.g:1628:1: rule__Equalitiy__Group_1__0__Impl : ( () ) ;
    public final void rule__Equalitiy__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1632:1: ( ( () ) )
            // InternalCQL.g:1633:1: ( () )
            {
            // InternalCQL.g:1633:1: ( () )
            // InternalCQL.g:1634:2: ()
            {
             before(grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0()); 
            // InternalCQL.g:1635:2: ()
            // InternalCQL.g:1635:3: 
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
    // InternalCQL.g:1643:1: rule__Equalitiy__Group_1__1 : rule__Equalitiy__Group_1__1__Impl rule__Equalitiy__Group_1__2 ;
    public final void rule__Equalitiy__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1647:1: ( rule__Equalitiy__Group_1__1__Impl rule__Equalitiy__Group_1__2 )
            // InternalCQL.g:1648:2: rule__Equalitiy__Group_1__1__Impl rule__Equalitiy__Group_1__2
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
    // InternalCQL.g:1655:1: rule__Equalitiy__Group_1__1__Impl : ( ( rule__Equalitiy__OpAssignment_1_1 ) ) ;
    public final void rule__Equalitiy__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1659:1: ( ( ( rule__Equalitiy__OpAssignment_1_1 ) ) )
            // InternalCQL.g:1660:1: ( ( rule__Equalitiy__OpAssignment_1_1 ) )
            {
            // InternalCQL.g:1660:1: ( ( rule__Equalitiy__OpAssignment_1_1 ) )
            // InternalCQL.g:1661:2: ( rule__Equalitiy__OpAssignment_1_1 )
            {
             before(grammarAccess.getEqualitiyAccess().getOpAssignment_1_1()); 
            // InternalCQL.g:1662:2: ( rule__Equalitiy__OpAssignment_1_1 )
            // InternalCQL.g:1662:3: rule__Equalitiy__OpAssignment_1_1
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
    // InternalCQL.g:1670:1: rule__Equalitiy__Group_1__2 : rule__Equalitiy__Group_1__2__Impl ;
    public final void rule__Equalitiy__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1674:1: ( rule__Equalitiy__Group_1__2__Impl )
            // InternalCQL.g:1675:2: rule__Equalitiy__Group_1__2__Impl
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
    // InternalCQL.g:1681:1: rule__Equalitiy__Group_1__2__Impl : ( ( rule__Equalitiy__RightAssignment_1_2 ) ) ;
    public final void rule__Equalitiy__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1685:1: ( ( ( rule__Equalitiy__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1686:1: ( ( rule__Equalitiy__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1686:1: ( ( rule__Equalitiy__RightAssignment_1_2 ) )
            // InternalCQL.g:1687:2: ( rule__Equalitiy__RightAssignment_1_2 )
            {
             before(grammarAccess.getEqualitiyAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1688:2: ( rule__Equalitiy__RightAssignment_1_2 )
            // InternalCQL.g:1688:3: rule__Equalitiy__RightAssignment_1_2
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
    // InternalCQL.g:1697:1: rule__Comparison__Group__0 : rule__Comparison__Group__0__Impl rule__Comparison__Group__1 ;
    public final void rule__Comparison__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1701:1: ( rule__Comparison__Group__0__Impl rule__Comparison__Group__1 )
            // InternalCQL.g:1702:2: rule__Comparison__Group__0__Impl rule__Comparison__Group__1
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
    // InternalCQL.g:1709:1: rule__Comparison__Group__0__Impl : ( rulePlusOrMinus ) ;
    public final void rule__Comparison__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1713:1: ( ( rulePlusOrMinus ) )
            // InternalCQL.g:1714:1: ( rulePlusOrMinus )
            {
            // InternalCQL.g:1714:1: ( rulePlusOrMinus )
            // InternalCQL.g:1715:2: rulePlusOrMinus
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
    // InternalCQL.g:1724:1: rule__Comparison__Group__1 : rule__Comparison__Group__1__Impl ;
    public final void rule__Comparison__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1728:1: ( rule__Comparison__Group__1__Impl )
            // InternalCQL.g:1729:2: rule__Comparison__Group__1__Impl
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
    // InternalCQL.g:1735:1: rule__Comparison__Group__1__Impl : ( ( rule__Comparison__Group_1__0 )* ) ;
    public final void rule__Comparison__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1739:1: ( ( ( rule__Comparison__Group_1__0 )* ) )
            // InternalCQL.g:1740:1: ( ( rule__Comparison__Group_1__0 )* )
            {
            // InternalCQL.g:1740:1: ( ( rule__Comparison__Group_1__0 )* )
            // InternalCQL.g:1741:2: ( rule__Comparison__Group_1__0 )*
            {
             before(grammarAccess.getComparisonAccess().getGroup_1()); 
            // InternalCQL.g:1742:2: ( rule__Comparison__Group_1__0 )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( ((LA19_0>=23 && LA19_0<=26)) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // InternalCQL.g:1742:3: rule__Comparison__Group_1__0
            	    {
            	    pushFollow(FOLLOW_19);
            	    rule__Comparison__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop19;
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
    // InternalCQL.g:1751:1: rule__Comparison__Group_1__0 : rule__Comparison__Group_1__0__Impl rule__Comparison__Group_1__1 ;
    public final void rule__Comparison__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1755:1: ( rule__Comparison__Group_1__0__Impl rule__Comparison__Group_1__1 )
            // InternalCQL.g:1756:2: rule__Comparison__Group_1__0__Impl rule__Comparison__Group_1__1
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
    // InternalCQL.g:1763:1: rule__Comparison__Group_1__0__Impl : ( () ) ;
    public final void rule__Comparison__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1767:1: ( ( () ) )
            // InternalCQL.g:1768:1: ( () )
            {
            // InternalCQL.g:1768:1: ( () )
            // InternalCQL.g:1769:2: ()
            {
             before(grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0()); 
            // InternalCQL.g:1770:2: ()
            // InternalCQL.g:1770:3: 
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
    // InternalCQL.g:1778:1: rule__Comparison__Group_1__1 : rule__Comparison__Group_1__1__Impl rule__Comparison__Group_1__2 ;
    public final void rule__Comparison__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1782:1: ( rule__Comparison__Group_1__1__Impl rule__Comparison__Group_1__2 )
            // InternalCQL.g:1783:2: rule__Comparison__Group_1__1__Impl rule__Comparison__Group_1__2
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
    // InternalCQL.g:1790:1: rule__Comparison__Group_1__1__Impl : ( ( rule__Comparison__OpAssignment_1_1 ) ) ;
    public final void rule__Comparison__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1794:1: ( ( ( rule__Comparison__OpAssignment_1_1 ) ) )
            // InternalCQL.g:1795:1: ( ( rule__Comparison__OpAssignment_1_1 ) )
            {
            // InternalCQL.g:1795:1: ( ( rule__Comparison__OpAssignment_1_1 ) )
            // InternalCQL.g:1796:2: ( rule__Comparison__OpAssignment_1_1 )
            {
             before(grammarAccess.getComparisonAccess().getOpAssignment_1_1()); 
            // InternalCQL.g:1797:2: ( rule__Comparison__OpAssignment_1_1 )
            // InternalCQL.g:1797:3: rule__Comparison__OpAssignment_1_1
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
    // InternalCQL.g:1805:1: rule__Comparison__Group_1__2 : rule__Comparison__Group_1__2__Impl ;
    public final void rule__Comparison__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1809:1: ( rule__Comparison__Group_1__2__Impl )
            // InternalCQL.g:1810:2: rule__Comparison__Group_1__2__Impl
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
    // InternalCQL.g:1816:1: rule__Comparison__Group_1__2__Impl : ( ( rule__Comparison__RightAssignment_1_2 ) ) ;
    public final void rule__Comparison__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1820:1: ( ( ( rule__Comparison__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1821:1: ( ( rule__Comparison__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1821:1: ( ( rule__Comparison__RightAssignment_1_2 ) )
            // InternalCQL.g:1822:2: ( rule__Comparison__RightAssignment_1_2 )
            {
             before(grammarAccess.getComparisonAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1823:2: ( rule__Comparison__RightAssignment_1_2 )
            // InternalCQL.g:1823:3: rule__Comparison__RightAssignment_1_2
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
    // InternalCQL.g:1832:1: rule__PlusOrMinus__Group__0 : rule__PlusOrMinus__Group__0__Impl rule__PlusOrMinus__Group__1 ;
    public final void rule__PlusOrMinus__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1836:1: ( rule__PlusOrMinus__Group__0__Impl rule__PlusOrMinus__Group__1 )
            // InternalCQL.g:1837:2: rule__PlusOrMinus__Group__0__Impl rule__PlusOrMinus__Group__1
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
    // InternalCQL.g:1844:1: rule__PlusOrMinus__Group__0__Impl : ( ruleMulOrDiv ) ;
    public final void rule__PlusOrMinus__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1848:1: ( ( ruleMulOrDiv ) )
            // InternalCQL.g:1849:1: ( ruleMulOrDiv )
            {
            // InternalCQL.g:1849:1: ( ruleMulOrDiv )
            // InternalCQL.g:1850:2: ruleMulOrDiv
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
    // InternalCQL.g:1859:1: rule__PlusOrMinus__Group__1 : rule__PlusOrMinus__Group__1__Impl ;
    public final void rule__PlusOrMinus__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1863:1: ( rule__PlusOrMinus__Group__1__Impl )
            // InternalCQL.g:1864:2: rule__PlusOrMinus__Group__1__Impl
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
    // InternalCQL.g:1870:1: rule__PlusOrMinus__Group__1__Impl : ( ( rule__PlusOrMinus__Group_1__0 )* ) ;
    public final void rule__PlusOrMinus__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1874:1: ( ( ( rule__PlusOrMinus__Group_1__0 )* ) )
            // InternalCQL.g:1875:1: ( ( rule__PlusOrMinus__Group_1__0 )* )
            {
            // InternalCQL.g:1875:1: ( ( rule__PlusOrMinus__Group_1__0 )* )
            // InternalCQL.g:1876:2: ( rule__PlusOrMinus__Group_1__0 )*
            {
             before(grammarAccess.getPlusOrMinusAccess().getGroup_1()); 
            // InternalCQL.g:1877:2: ( rule__PlusOrMinus__Group_1__0 )*
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( ((LA20_0>=35 && LA20_0<=36)) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // InternalCQL.g:1877:3: rule__PlusOrMinus__Group_1__0
            	    {
            	    pushFollow(FOLLOW_21);
            	    rule__PlusOrMinus__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop20;
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
    // InternalCQL.g:1886:1: rule__PlusOrMinus__Group_1__0 : rule__PlusOrMinus__Group_1__0__Impl rule__PlusOrMinus__Group_1__1 ;
    public final void rule__PlusOrMinus__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1890:1: ( rule__PlusOrMinus__Group_1__0__Impl rule__PlusOrMinus__Group_1__1 )
            // InternalCQL.g:1891:2: rule__PlusOrMinus__Group_1__0__Impl rule__PlusOrMinus__Group_1__1
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
    // InternalCQL.g:1898:1: rule__PlusOrMinus__Group_1__0__Impl : ( ( rule__PlusOrMinus__Alternatives_1_0 ) ) ;
    public final void rule__PlusOrMinus__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1902:1: ( ( ( rule__PlusOrMinus__Alternatives_1_0 ) ) )
            // InternalCQL.g:1903:1: ( ( rule__PlusOrMinus__Alternatives_1_0 ) )
            {
            // InternalCQL.g:1903:1: ( ( rule__PlusOrMinus__Alternatives_1_0 ) )
            // InternalCQL.g:1904:2: ( rule__PlusOrMinus__Alternatives_1_0 )
            {
             before(grammarAccess.getPlusOrMinusAccess().getAlternatives_1_0()); 
            // InternalCQL.g:1905:2: ( rule__PlusOrMinus__Alternatives_1_0 )
            // InternalCQL.g:1905:3: rule__PlusOrMinus__Alternatives_1_0
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
    // InternalCQL.g:1913:1: rule__PlusOrMinus__Group_1__1 : rule__PlusOrMinus__Group_1__1__Impl ;
    public final void rule__PlusOrMinus__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1917:1: ( rule__PlusOrMinus__Group_1__1__Impl )
            // InternalCQL.g:1918:2: rule__PlusOrMinus__Group_1__1__Impl
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
    // InternalCQL.g:1924:1: rule__PlusOrMinus__Group_1__1__Impl : ( ( rule__PlusOrMinus__RightAssignment_1_1 ) ) ;
    public final void rule__PlusOrMinus__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1928:1: ( ( ( rule__PlusOrMinus__RightAssignment_1_1 ) ) )
            // InternalCQL.g:1929:1: ( ( rule__PlusOrMinus__RightAssignment_1_1 ) )
            {
            // InternalCQL.g:1929:1: ( ( rule__PlusOrMinus__RightAssignment_1_1 ) )
            // InternalCQL.g:1930:2: ( rule__PlusOrMinus__RightAssignment_1_1 )
            {
             before(grammarAccess.getPlusOrMinusAccess().getRightAssignment_1_1()); 
            // InternalCQL.g:1931:2: ( rule__PlusOrMinus__RightAssignment_1_1 )
            // InternalCQL.g:1931:3: rule__PlusOrMinus__RightAssignment_1_1
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
    // InternalCQL.g:1940:1: rule__PlusOrMinus__Group_1_0_0__0 : rule__PlusOrMinus__Group_1_0_0__0__Impl rule__PlusOrMinus__Group_1_0_0__1 ;
    public final void rule__PlusOrMinus__Group_1_0_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1944:1: ( rule__PlusOrMinus__Group_1_0_0__0__Impl rule__PlusOrMinus__Group_1_0_0__1 )
            // InternalCQL.g:1945:2: rule__PlusOrMinus__Group_1_0_0__0__Impl rule__PlusOrMinus__Group_1_0_0__1
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
    // InternalCQL.g:1952:1: rule__PlusOrMinus__Group_1_0_0__0__Impl : ( () ) ;
    public final void rule__PlusOrMinus__Group_1_0_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1956:1: ( ( () ) )
            // InternalCQL.g:1957:1: ( () )
            {
            // InternalCQL.g:1957:1: ( () )
            // InternalCQL.g:1958:2: ()
            {
             before(grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0()); 
            // InternalCQL.g:1959:2: ()
            // InternalCQL.g:1959:3: 
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
    // InternalCQL.g:1967:1: rule__PlusOrMinus__Group_1_0_0__1 : rule__PlusOrMinus__Group_1_0_0__1__Impl ;
    public final void rule__PlusOrMinus__Group_1_0_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1971:1: ( rule__PlusOrMinus__Group_1_0_0__1__Impl )
            // InternalCQL.g:1972:2: rule__PlusOrMinus__Group_1_0_0__1__Impl
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
    // InternalCQL.g:1978:1: rule__PlusOrMinus__Group_1_0_0__1__Impl : ( '+' ) ;
    public final void rule__PlusOrMinus__Group_1_0_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1982:1: ( ( '+' ) )
            // InternalCQL.g:1983:1: ( '+' )
            {
            // InternalCQL.g:1983:1: ( '+' )
            // InternalCQL.g:1984:2: '+'
            {
             before(grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1()); 
            match(input,35,FOLLOW_2); 
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
    // InternalCQL.g:1994:1: rule__PlusOrMinus__Group_1_0_1__0 : rule__PlusOrMinus__Group_1_0_1__0__Impl rule__PlusOrMinus__Group_1_0_1__1 ;
    public final void rule__PlusOrMinus__Group_1_0_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1998:1: ( rule__PlusOrMinus__Group_1_0_1__0__Impl rule__PlusOrMinus__Group_1_0_1__1 )
            // InternalCQL.g:1999:2: rule__PlusOrMinus__Group_1_0_1__0__Impl rule__PlusOrMinus__Group_1_0_1__1
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
    // InternalCQL.g:2006:1: rule__PlusOrMinus__Group_1_0_1__0__Impl : ( () ) ;
    public final void rule__PlusOrMinus__Group_1_0_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2010:1: ( ( () ) )
            // InternalCQL.g:2011:1: ( () )
            {
            // InternalCQL.g:2011:1: ( () )
            // InternalCQL.g:2012:2: ()
            {
             before(grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0()); 
            // InternalCQL.g:2013:2: ()
            // InternalCQL.g:2013:3: 
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
    // InternalCQL.g:2021:1: rule__PlusOrMinus__Group_1_0_1__1 : rule__PlusOrMinus__Group_1_0_1__1__Impl ;
    public final void rule__PlusOrMinus__Group_1_0_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2025:1: ( rule__PlusOrMinus__Group_1_0_1__1__Impl )
            // InternalCQL.g:2026:2: rule__PlusOrMinus__Group_1_0_1__1__Impl
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
    // InternalCQL.g:2032:1: rule__PlusOrMinus__Group_1_0_1__1__Impl : ( '-' ) ;
    public final void rule__PlusOrMinus__Group_1_0_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2036:1: ( ( '-' ) )
            // InternalCQL.g:2037:1: ( '-' )
            {
            // InternalCQL.g:2037:1: ( '-' )
            // InternalCQL.g:2038:2: '-'
            {
             before(grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1()); 
            match(input,36,FOLLOW_2); 
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
    // InternalCQL.g:2048:1: rule__MulOrDiv__Group__0 : rule__MulOrDiv__Group__0__Impl rule__MulOrDiv__Group__1 ;
    public final void rule__MulOrDiv__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2052:1: ( rule__MulOrDiv__Group__0__Impl rule__MulOrDiv__Group__1 )
            // InternalCQL.g:2053:2: rule__MulOrDiv__Group__0__Impl rule__MulOrDiv__Group__1
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
    // InternalCQL.g:2060:1: rule__MulOrDiv__Group__0__Impl : ( rulePrimary ) ;
    public final void rule__MulOrDiv__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2064:1: ( ( rulePrimary ) )
            // InternalCQL.g:2065:1: ( rulePrimary )
            {
            // InternalCQL.g:2065:1: ( rulePrimary )
            // InternalCQL.g:2066:2: rulePrimary
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
    // InternalCQL.g:2075:1: rule__MulOrDiv__Group__1 : rule__MulOrDiv__Group__1__Impl ;
    public final void rule__MulOrDiv__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2079:1: ( rule__MulOrDiv__Group__1__Impl )
            // InternalCQL.g:2080:2: rule__MulOrDiv__Group__1__Impl
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
    // InternalCQL.g:2086:1: rule__MulOrDiv__Group__1__Impl : ( ( rule__MulOrDiv__Group_1__0 )* ) ;
    public final void rule__MulOrDiv__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2090:1: ( ( ( rule__MulOrDiv__Group_1__0 )* ) )
            // InternalCQL.g:2091:1: ( ( rule__MulOrDiv__Group_1__0 )* )
            {
            // InternalCQL.g:2091:1: ( ( rule__MulOrDiv__Group_1__0 )* )
            // InternalCQL.g:2092:2: ( rule__MulOrDiv__Group_1__0 )*
            {
             before(grammarAccess.getMulOrDivAccess().getGroup_1()); 
            // InternalCQL.g:2093:2: ( rule__MulOrDiv__Group_1__0 )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( ((LA21_0>=27 && LA21_0<=28)) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // InternalCQL.g:2093:3: rule__MulOrDiv__Group_1__0
            	    {
            	    pushFollow(FOLLOW_24);
            	    rule__MulOrDiv__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop21;
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
    // InternalCQL.g:2102:1: rule__MulOrDiv__Group_1__0 : rule__MulOrDiv__Group_1__0__Impl rule__MulOrDiv__Group_1__1 ;
    public final void rule__MulOrDiv__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2106:1: ( rule__MulOrDiv__Group_1__0__Impl rule__MulOrDiv__Group_1__1 )
            // InternalCQL.g:2107:2: rule__MulOrDiv__Group_1__0__Impl rule__MulOrDiv__Group_1__1
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
    // InternalCQL.g:2114:1: rule__MulOrDiv__Group_1__0__Impl : ( () ) ;
    public final void rule__MulOrDiv__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2118:1: ( ( () ) )
            // InternalCQL.g:2119:1: ( () )
            {
            // InternalCQL.g:2119:1: ( () )
            // InternalCQL.g:2120:2: ()
            {
             before(grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0()); 
            // InternalCQL.g:2121:2: ()
            // InternalCQL.g:2121:3: 
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
    // InternalCQL.g:2129:1: rule__MulOrDiv__Group_1__1 : rule__MulOrDiv__Group_1__1__Impl rule__MulOrDiv__Group_1__2 ;
    public final void rule__MulOrDiv__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2133:1: ( rule__MulOrDiv__Group_1__1__Impl rule__MulOrDiv__Group_1__2 )
            // InternalCQL.g:2134:2: rule__MulOrDiv__Group_1__1__Impl rule__MulOrDiv__Group_1__2
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
    // InternalCQL.g:2141:1: rule__MulOrDiv__Group_1__1__Impl : ( ( rule__MulOrDiv__OpAssignment_1_1 ) ) ;
    public final void rule__MulOrDiv__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2145:1: ( ( ( rule__MulOrDiv__OpAssignment_1_1 ) ) )
            // InternalCQL.g:2146:1: ( ( rule__MulOrDiv__OpAssignment_1_1 ) )
            {
            // InternalCQL.g:2146:1: ( ( rule__MulOrDiv__OpAssignment_1_1 ) )
            // InternalCQL.g:2147:2: ( rule__MulOrDiv__OpAssignment_1_1 )
            {
             before(grammarAccess.getMulOrDivAccess().getOpAssignment_1_1()); 
            // InternalCQL.g:2148:2: ( rule__MulOrDiv__OpAssignment_1_1 )
            // InternalCQL.g:2148:3: rule__MulOrDiv__OpAssignment_1_1
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
    // InternalCQL.g:2156:1: rule__MulOrDiv__Group_1__2 : rule__MulOrDiv__Group_1__2__Impl ;
    public final void rule__MulOrDiv__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2160:1: ( rule__MulOrDiv__Group_1__2__Impl )
            // InternalCQL.g:2161:2: rule__MulOrDiv__Group_1__2__Impl
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
    // InternalCQL.g:2167:1: rule__MulOrDiv__Group_1__2__Impl : ( ( rule__MulOrDiv__RightAssignment_1_2 ) ) ;
    public final void rule__MulOrDiv__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2171:1: ( ( ( rule__MulOrDiv__RightAssignment_1_2 ) ) )
            // InternalCQL.g:2172:1: ( ( rule__MulOrDiv__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:2172:1: ( ( rule__MulOrDiv__RightAssignment_1_2 ) )
            // InternalCQL.g:2173:2: ( rule__MulOrDiv__RightAssignment_1_2 )
            {
             before(grammarAccess.getMulOrDivAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:2174:2: ( rule__MulOrDiv__RightAssignment_1_2 )
            // InternalCQL.g:2174:3: rule__MulOrDiv__RightAssignment_1_2
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
    // InternalCQL.g:2183:1: rule__Primary__Group_0__0 : rule__Primary__Group_0__0__Impl rule__Primary__Group_0__1 ;
    public final void rule__Primary__Group_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2187:1: ( rule__Primary__Group_0__0__Impl rule__Primary__Group_0__1 )
            // InternalCQL.g:2188:2: rule__Primary__Group_0__0__Impl rule__Primary__Group_0__1
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
    // InternalCQL.g:2195:1: rule__Primary__Group_0__0__Impl : ( () ) ;
    public final void rule__Primary__Group_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2199:1: ( ( () ) )
            // InternalCQL.g:2200:1: ( () )
            {
            // InternalCQL.g:2200:1: ( () )
            // InternalCQL.g:2201:2: ()
            {
             before(grammarAccess.getPrimaryAccess().getBracketAction_0_0()); 
            // InternalCQL.g:2202:2: ()
            // InternalCQL.g:2202:3: 
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
    // InternalCQL.g:2210:1: rule__Primary__Group_0__1 : rule__Primary__Group_0__1__Impl rule__Primary__Group_0__2 ;
    public final void rule__Primary__Group_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2214:1: ( rule__Primary__Group_0__1__Impl rule__Primary__Group_0__2 )
            // InternalCQL.g:2215:2: rule__Primary__Group_0__1__Impl rule__Primary__Group_0__2
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
    // InternalCQL.g:2222:1: rule__Primary__Group_0__1__Impl : ( '(' ) ;
    public final void rule__Primary__Group_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2226:1: ( ( '(' ) )
            // InternalCQL.g:2227:1: ( '(' )
            {
            // InternalCQL.g:2227:1: ( '(' )
            // InternalCQL.g:2228:2: '('
            {
             before(grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1()); 
            match(input,37,FOLLOW_2); 
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
    // InternalCQL.g:2237:1: rule__Primary__Group_0__2 : rule__Primary__Group_0__2__Impl rule__Primary__Group_0__3 ;
    public final void rule__Primary__Group_0__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2241:1: ( rule__Primary__Group_0__2__Impl rule__Primary__Group_0__3 )
            // InternalCQL.g:2242:2: rule__Primary__Group_0__2__Impl rule__Primary__Group_0__3
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
    // InternalCQL.g:2249:1: rule__Primary__Group_0__2__Impl : ( ( rule__Primary__InnerAssignment_0_2 ) ) ;
    public final void rule__Primary__Group_0__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2253:1: ( ( ( rule__Primary__InnerAssignment_0_2 ) ) )
            // InternalCQL.g:2254:1: ( ( rule__Primary__InnerAssignment_0_2 ) )
            {
            // InternalCQL.g:2254:1: ( ( rule__Primary__InnerAssignment_0_2 ) )
            // InternalCQL.g:2255:2: ( rule__Primary__InnerAssignment_0_2 )
            {
             before(grammarAccess.getPrimaryAccess().getInnerAssignment_0_2()); 
            // InternalCQL.g:2256:2: ( rule__Primary__InnerAssignment_0_2 )
            // InternalCQL.g:2256:3: rule__Primary__InnerAssignment_0_2
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
    // InternalCQL.g:2264:1: rule__Primary__Group_0__3 : rule__Primary__Group_0__3__Impl ;
    public final void rule__Primary__Group_0__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2268:1: ( rule__Primary__Group_0__3__Impl )
            // InternalCQL.g:2269:2: rule__Primary__Group_0__3__Impl
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
    // InternalCQL.g:2275:1: rule__Primary__Group_0__3__Impl : ( ')' ) ;
    public final void rule__Primary__Group_0__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2279:1: ( ( ')' ) )
            // InternalCQL.g:2280:1: ( ')' )
            {
            // InternalCQL.g:2280:1: ( ')' )
            // InternalCQL.g:2281:2: ')'
            {
             before(grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_0_3()); 
            match(input,38,FOLLOW_2); 
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
    // InternalCQL.g:2291:1: rule__Primary__Group_1__0 : rule__Primary__Group_1__0__Impl rule__Primary__Group_1__1 ;
    public final void rule__Primary__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2295:1: ( rule__Primary__Group_1__0__Impl rule__Primary__Group_1__1 )
            // InternalCQL.g:2296:2: rule__Primary__Group_1__0__Impl rule__Primary__Group_1__1
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
    // InternalCQL.g:2303:1: rule__Primary__Group_1__0__Impl : ( () ) ;
    public final void rule__Primary__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2307:1: ( ( () ) )
            // InternalCQL.g:2308:1: ( () )
            {
            // InternalCQL.g:2308:1: ( () )
            // InternalCQL.g:2309:2: ()
            {
             before(grammarAccess.getPrimaryAccess().getNOTAction_1_0()); 
            // InternalCQL.g:2310:2: ()
            // InternalCQL.g:2310:3: 
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
    // InternalCQL.g:2318:1: rule__Primary__Group_1__1 : rule__Primary__Group_1__1__Impl rule__Primary__Group_1__2 ;
    public final void rule__Primary__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2322:1: ( rule__Primary__Group_1__1__Impl rule__Primary__Group_1__2 )
            // InternalCQL.g:2323:2: rule__Primary__Group_1__1__Impl rule__Primary__Group_1__2
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
    // InternalCQL.g:2330:1: rule__Primary__Group_1__1__Impl : ( 'NOT' ) ;
    public final void rule__Primary__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2334:1: ( ( 'NOT' ) )
            // InternalCQL.g:2335:1: ( 'NOT' )
            {
            // InternalCQL.g:2335:1: ( 'NOT' )
            // InternalCQL.g:2336:2: 'NOT'
            {
             before(grammarAccess.getPrimaryAccess().getNOTKeyword_1_1()); 
            match(input,39,FOLLOW_2); 
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
    // InternalCQL.g:2345:1: rule__Primary__Group_1__2 : rule__Primary__Group_1__2__Impl ;
    public final void rule__Primary__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2349:1: ( rule__Primary__Group_1__2__Impl )
            // InternalCQL.g:2350:2: rule__Primary__Group_1__2__Impl
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
    // InternalCQL.g:2356:1: rule__Primary__Group_1__2__Impl : ( ( rule__Primary__ExpressionAssignment_1_2 ) ) ;
    public final void rule__Primary__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2360:1: ( ( ( rule__Primary__ExpressionAssignment_1_2 ) ) )
            // InternalCQL.g:2361:1: ( ( rule__Primary__ExpressionAssignment_1_2 ) )
            {
            // InternalCQL.g:2361:1: ( ( rule__Primary__ExpressionAssignment_1_2 ) )
            // InternalCQL.g:2362:2: ( rule__Primary__ExpressionAssignment_1_2 )
            {
             before(grammarAccess.getPrimaryAccess().getExpressionAssignment_1_2()); 
            // InternalCQL.g:2363:2: ( rule__Primary__ExpressionAssignment_1_2 )
            // InternalCQL.g:2363:3: rule__Primary__ExpressionAssignment_1_2
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
    // InternalCQL.g:2372:1: rule__Select_Statement__Group__0 : rule__Select_Statement__Group__0__Impl rule__Select_Statement__Group__1 ;
    public final void rule__Select_Statement__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2376:1: ( rule__Select_Statement__Group__0__Impl rule__Select_Statement__Group__1 )
            // InternalCQL.g:2377:2: rule__Select_Statement__Group__0__Impl rule__Select_Statement__Group__1
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
    // InternalCQL.g:2384:1: rule__Select_Statement__Group__0__Impl : ( ( rule__Select_Statement__NameAssignment_0 ) ) ;
    public final void rule__Select_Statement__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2388:1: ( ( ( rule__Select_Statement__NameAssignment_0 ) ) )
            // InternalCQL.g:2389:1: ( ( rule__Select_Statement__NameAssignment_0 ) )
            {
            // InternalCQL.g:2389:1: ( ( rule__Select_Statement__NameAssignment_0 ) )
            // InternalCQL.g:2390:2: ( rule__Select_Statement__NameAssignment_0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getNameAssignment_0()); 
            // InternalCQL.g:2391:2: ( rule__Select_Statement__NameAssignment_0 )
            // InternalCQL.g:2391:3: rule__Select_Statement__NameAssignment_0
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
    // InternalCQL.g:2399:1: rule__Select_Statement__Group__1 : rule__Select_Statement__Group__1__Impl rule__Select_Statement__Group__2 ;
    public final void rule__Select_Statement__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2403:1: ( rule__Select_Statement__Group__1__Impl rule__Select_Statement__Group__2 )
            // InternalCQL.g:2404:2: rule__Select_Statement__Group__1__Impl rule__Select_Statement__Group__2
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
    // InternalCQL.g:2411:1: rule__Select_Statement__Group__1__Impl : ( ( 'DISTINCT' )? ) ;
    public final void rule__Select_Statement__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2415:1: ( ( ( 'DISTINCT' )? ) )
            // InternalCQL.g:2416:1: ( ( 'DISTINCT' )? )
            {
            // InternalCQL.g:2416:1: ( ( 'DISTINCT' )? )
            // InternalCQL.g:2417:2: ( 'DISTINCT' )?
            {
             before(grammarAccess.getSelect_StatementAccess().getDISTINCTKeyword_1()); 
            // InternalCQL.g:2418:2: ( 'DISTINCT' )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==40) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // InternalCQL.g:2418:3: 'DISTINCT'
                    {
                    match(input,40,FOLLOW_2); 

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
    // InternalCQL.g:2426:1: rule__Select_Statement__Group__2 : rule__Select_Statement__Group__2__Impl rule__Select_Statement__Group__3 ;
    public final void rule__Select_Statement__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2430:1: ( rule__Select_Statement__Group__2__Impl rule__Select_Statement__Group__3 )
            // InternalCQL.g:2431:2: rule__Select_Statement__Group__2__Impl rule__Select_Statement__Group__3
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
    // InternalCQL.g:2438:1: rule__Select_Statement__Group__2__Impl : ( ( rule__Select_Statement__Alternatives_2 ) ) ;
    public final void rule__Select_Statement__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2442:1: ( ( ( rule__Select_Statement__Alternatives_2 ) ) )
            // InternalCQL.g:2443:1: ( ( rule__Select_Statement__Alternatives_2 ) )
            {
            // InternalCQL.g:2443:1: ( ( rule__Select_Statement__Alternatives_2 ) )
            // InternalCQL.g:2444:2: ( rule__Select_Statement__Alternatives_2 )
            {
             before(grammarAccess.getSelect_StatementAccess().getAlternatives_2()); 
            // InternalCQL.g:2445:2: ( rule__Select_Statement__Alternatives_2 )
            // InternalCQL.g:2445:3: rule__Select_Statement__Alternatives_2
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
    // InternalCQL.g:2453:1: rule__Select_Statement__Group__3 : rule__Select_Statement__Group__3__Impl rule__Select_Statement__Group__4 ;
    public final void rule__Select_Statement__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2457:1: ( rule__Select_Statement__Group__3__Impl rule__Select_Statement__Group__4 )
            // InternalCQL.g:2458:2: rule__Select_Statement__Group__3__Impl rule__Select_Statement__Group__4
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
    // InternalCQL.g:2465:1: rule__Select_Statement__Group__3__Impl : ( 'FROM' ) ;
    public final void rule__Select_Statement__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2469:1: ( ( 'FROM' ) )
            // InternalCQL.g:2470:1: ( 'FROM' )
            {
            // InternalCQL.g:2470:1: ( 'FROM' )
            // InternalCQL.g:2471:2: 'FROM'
            {
             before(grammarAccess.getSelect_StatementAccess().getFROMKeyword_3()); 
            match(input,41,FOLLOW_2); 
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
    // InternalCQL.g:2480:1: rule__Select_Statement__Group__4 : rule__Select_Statement__Group__4__Impl rule__Select_Statement__Group__5 ;
    public final void rule__Select_Statement__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2484:1: ( rule__Select_Statement__Group__4__Impl rule__Select_Statement__Group__5 )
            // InternalCQL.g:2485:2: rule__Select_Statement__Group__4__Impl rule__Select_Statement__Group__5
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
    // InternalCQL.g:2492:1: rule__Select_Statement__Group__4__Impl : ( ( rule__Select_Statement__Group_4__0 ) ) ;
    public final void rule__Select_Statement__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2496:1: ( ( ( rule__Select_Statement__Group_4__0 ) ) )
            // InternalCQL.g:2497:1: ( ( rule__Select_Statement__Group_4__0 ) )
            {
            // InternalCQL.g:2497:1: ( ( rule__Select_Statement__Group_4__0 ) )
            // InternalCQL.g:2498:2: ( rule__Select_Statement__Group_4__0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_4()); 
            // InternalCQL.g:2499:2: ( rule__Select_Statement__Group_4__0 )
            // InternalCQL.g:2499:3: rule__Select_Statement__Group_4__0
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
    // InternalCQL.g:2507:1: rule__Select_Statement__Group__5 : rule__Select_Statement__Group__5__Impl ;
    public final void rule__Select_Statement__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2511:1: ( rule__Select_Statement__Group__5__Impl )
            // InternalCQL.g:2512:2: rule__Select_Statement__Group__5__Impl
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
    // InternalCQL.g:2518:1: rule__Select_Statement__Group__5__Impl : ( ( rule__Select_Statement__Group_5__0 )? ) ;
    public final void rule__Select_Statement__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2522:1: ( ( ( rule__Select_Statement__Group_5__0 )? ) )
            // InternalCQL.g:2523:1: ( ( rule__Select_Statement__Group_5__0 )? )
            {
            // InternalCQL.g:2523:1: ( ( rule__Select_Statement__Group_5__0 )? )
            // InternalCQL.g:2524:2: ( rule__Select_Statement__Group_5__0 )?
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_5()); 
            // InternalCQL.g:2525:2: ( rule__Select_Statement__Group_5__0 )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==43) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // InternalCQL.g:2525:3: rule__Select_Statement__Group_5__0
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
    // InternalCQL.g:2534:1: rule__Select_Statement__Group_2_1__0 : rule__Select_Statement__Group_2_1__0__Impl rule__Select_Statement__Group_2_1__1 ;
    public final void rule__Select_Statement__Group_2_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2538:1: ( rule__Select_Statement__Group_2_1__0__Impl rule__Select_Statement__Group_2_1__1 )
            // InternalCQL.g:2539:2: rule__Select_Statement__Group_2_1__0__Impl rule__Select_Statement__Group_2_1__1
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
    // InternalCQL.g:2546:1: rule__Select_Statement__Group_2_1__0__Impl : ( ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) ) ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* ) ) ;
    public final void rule__Select_Statement__Group_2_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2550:1: ( ( ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) ) ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* ) ) )
            // InternalCQL.g:2551:1: ( ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) ) ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* ) )
            {
            // InternalCQL.g:2551:1: ( ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) ) ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* ) )
            // InternalCQL.g:2552:2: ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) ) ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* )
            {
            // InternalCQL.g:2552:2: ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) )
            // InternalCQL.g:2553:3: ( rule__Select_Statement__AttributesAssignment_2_1_0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2_1_0()); 
            // InternalCQL.g:2554:3: ( rule__Select_Statement__AttributesAssignment_2_1_0 )
            // InternalCQL.g:2554:4: rule__Select_Statement__AttributesAssignment_2_1_0
            {
            pushFollow(FOLLOW_32);
            rule__Select_Statement__AttributesAssignment_2_1_0();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2_1_0()); 

            }

            // InternalCQL.g:2557:2: ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* )
            // InternalCQL.g:2558:3: ( rule__Select_Statement__AttributesAssignment_2_1_0 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2_1_0()); 
            // InternalCQL.g:2559:3: ( rule__Select_Statement__AttributesAssignment_2_1_0 )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==RULE_ID) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // InternalCQL.g:2559:4: rule__Select_Statement__AttributesAssignment_2_1_0
            	    {
            	    pushFollow(FOLLOW_32);
            	    rule__Select_Statement__AttributesAssignment_2_1_0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop24;
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
    // InternalCQL.g:2568:1: rule__Select_Statement__Group_2_1__1 : rule__Select_Statement__Group_2_1__1__Impl ;
    public final void rule__Select_Statement__Group_2_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2572:1: ( rule__Select_Statement__Group_2_1__1__Impl )
            // InternalCQL.g:2573:2: rule__Select_Statement__Group_2_1__1__Impl
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
    // InternalCQL.g:2579:1: rule__Select_Statement__Group_2_1__1__Impl : ( ( rule__Select_Statement__Group_2_1_1__0 )* ) ;
    public final void rule__Select_Statement__Group_2_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2583:1: ( ( ( rule__Select_Statement__Group_2_1_1__0 )* ) )
            // InternalCQL.g:2584:1: ( ( rule__Select_Statement__Group_2_1_1__0 )* )
            {
            // InternalCQL.g:2584:1: ( ( rule__Select_Statement__Group_2_1_1__0 )* )
            // InternalCQL.g:2585:2: ( rule__Select_Statement__Group_2_1_1__0 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_2_1_1()); 
            // InternalCQL.g:2586:2: ( rule__Select_Statement__Group_2_1_1__0 )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( (LA25_0==42) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // InternalCQL.g:2586:3: rule__Select_Statement__Group_2_1_1__0
            	    {
            	    pushFollow(FOLLOW_33);
            	    rule__Select_Statement__Group_2_1_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop25;
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
    // InternalCQL.g:2595:1: rule__Select_Statement__Group_2_1_1__0 : rule__Select_Statement__Group_2_1_1__0__Impl rule__Select_Statement__Group_2_1_1__1 ;
    public final void rule__Select_Statement__Group_2_1_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2599:1: ( rule__Select_Statement__Group_2_1_1__0__Impl rule__Select_Statement__Group_2_1_1__1 )
            // InternalCQL.g:2600:2: rule__Select_Statement__Group_2_1_1__0__Impl rule__Select_Statement__Group_2_1_1__1
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
    // InternalCQL.g:2607:1: rule__Select_Statement__Group_2_1_1__0__Impl : ( ',' ) ;
    public final void rule__Select_Statement__Group_2_1_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2611:1: ( ( ',' ) )
            // InternalCQL.g:2612:1: ( ',' )
            {
            // InternalCQL.g:2612:1: ( ',' )
            // InternalCQL.g:2613:2: ','
            {
             before(grammarAccess.getSelect_StatementAccess().getCommaKeyword_2_1_1_0()); 
            match(input,42,FOLLOW_2); 
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
    // InternalCQL.g:2622:1: rule__Select_Statement__Group_2_1_1__1 : rule__Select_Statement__Group_2_1_1__1__Impl ;
    public final void rule__Select_Statement__Group_2_1_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2626:1: ( rule__Select_Statement__Group_2_1_1__1__Impl )
            // InternalCQL.g:2627:2: rule__Select_Statement__Group_2_1_1__1__Impl
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
    // InternalCQL.g:2633:1: rule__Select_Statement__Group_2_1_1__1__Impl : ( ( rule__Select_Statement__AttributesAssignment_2_1_1_1 ) ) ;
    public final void rule__Select_Statement__Group_2_1_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2637:1: ( ( ( rule__Select_Statement__AttributesAssignment_2_1_1_1 ) ) )
            // InternalCQL.g:2638:1: ( ( rule__Select_Statement__AttributesAssignment_2_1_1_1 ) )
            {
            // InternalCQL.g:2638:1: ( ( rule__Select_Statement__AttributesAssignment_2_1_1_1 ) )
            // InternalCQL.g:2639:2: ( rule__Select_Statement__AttributesAssignment_2_1_1_1 )
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2_1_1_1()); 
            // InternalCQL.g:2640:2: ( rule__Select_Statement__AttributesAssignment_2_1_1_1 )
            // InternalCQL.g:2640:3: rule__Select_Statement__AttributesAssignment_2_1_1_1
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
    // InternalCQL.g:2649:1: rule__Select_Statement__Group_4__0 : rule__Select_Statement__Group_4__0__Impl rule__Select_Statement__Group_4__1 ;
    public final void rule__Select_Statement__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2653:1: ( rule__Select_Statement__Group_4__0__Impl rule__Select_Statement__Group_4__1 )
            // InternalCQL.g:2654:2: rule__Select_Statement__Group_4__0__Impl rule__Select_Statement__Group_4__1
            {
            pushFollow(FOLLOW_34);
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
    // InternalCQL.g:2661:1: rule__Select_Statement__Group_4__0__Impl : ( ( ( rule__Select_Statement__SourcesAssignment_4_0 ) ) ( ( rule__Select_Statement__SourcesAssignment_4_0 )* ) ) ;
    public final void rule__Select_Statement__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2665:1: ( ( ( ( rule__Select_Statement__SourcesAssignment_4_0 ) ) ( ( rule__Select_Statement__SourcesAssignment_4_0 )* ) ) )
            // InternalCQL.g:2666:1: ( ( ( rule__Select_Statement__SourcesAssignment_4_0 ) ) ( ( rule__Select_Statement__SourcesAssignment_4_0 )* ) )
            {
            // InternalCQL.g:2666:1: ( ( ( rule__Select_Statement__SourcesAssignment_4_0 ) ) ( ( rule__Select_Statement__SourcesAssignment_4_0 )* ) )
            // InternalCQL.g:2667:2: ( ( rule__Select_Statement__SourcesAssignment_4_0 ) ) ( ( rule__Select_Statement__SourcesAssignment_4_0 )* )
            {
            // InternalCQL.g:2667:2: ( ( rule__Select_Statement__SourcesAssignment_4_0 ) )
            // InternalCQL.g:2668:3: ( rule__Select_Statement__SourcesAssignment_4_0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_0()); 
            // InternalCQL.g:2669:3: ( rule__Select_Statement__SourcesAssignment_4_0 )
            // InternalCQL.g:2669:4: rule__Select_Statement__SourcesAssignment_4_0
            {
            pushFollow(FOLLOW_35);
            rule__Select_Statement__SourcesAssignment_4_0();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_0()); 

            }

            // InternalCQL.g:2672:2: ( ( rule__Select_Statement__SourcesAssignment_4_0 )* )
            // InternalCQL.g:2673:3: ( rule__Select_Statement__SourcesAssignment_4_0 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_0()); 
            // InternalCQL.g:2674:3: ( rule__Select_Statement__SourcesAssignment_4_0 )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( (LA26_0==RULE_ID) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // InternalCQL.g:2674:4: rule__Select_Statement__SourcesAssignment_4_0
            	    {
            	    pushFollow(FOLLOW_35);
            	    rule__Select_Statement__SourcesAssignment_4_0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop26;
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
    // InternalCQL.g:2683:1: rule__Select_Statement__Group_4__1 : rule__Select_Statement__Group_4__1__Impl rule__Select_Statement__Group_4__2 ;
    public final void rule__Select_Statement__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2687:1: ( rule__Select_Statement__Group_4__1__Impl rule__Select_Statement__Group_4__2 )
            // InternalCQL.g:2688:2: rule__Select_Statement__Group_4__1__Impl rule__Select_Statement__Group_4__2
            {
            pushFollow(FOLLOW_34);
            rule__Select_Statement__Group_4__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_4__2();

            state._fsp--;


            }

        }
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
    // InternalCQL.g:2695:1: rule__Select_Statement__Group_4__1__Impl : ( ( rule__Select_Statement__WindowsAssignment_4_1 )? ) ;
    public final void rule__Select_Statement__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2699:1: ( ( ( rule__Select_Statement__WindowsAssignment_4_1 )? ) )
            // InternalCQL.g:2700:1: ( ( rule__Select_Statement__WindowsAssignment_4_1 )? )
            {
            // InternalCQL.g:2700:1: ( ( rule__Select_Statement__WindowsAssignment_4_1 )? )
            // InternalCQL.g:2701:2: ( rule__Select_Statement__WindowsAssignment_4_1 )?
            {
             before(grammarAccess.getSelect_StatementAccess().getWindowsAssignment_4_1()); 
            // InternalCQL.g:2702:2: ( rule__Select_Statement__WindowsAssignment_4_1 )?
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==44) ) {
                alt27=1;
            }
            switch (alt27) {
                case 1 :
                    // InternalCQL.g:2702:3: rule__Select_Statement__WindowsAssignment_4_1
                    {
                    pushFollow(FOLLOW_2);
                    rule__Select_Statement__WindowsAssignment_4_1();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSelect_StatementAccess().getWindowsAssignment_4_1()); 

            }


            }

        }
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


    // $ANTLR start "rule__Select_Statement__Group_4__2"
    // InternalCQL.g:2710:1: rule__Select_Statement__Group_4__2 : rule__Select_Statement__Group_4__2__Impl ;
    public final void rule__Select_Statement__Group_4__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2714:1: ( rule__Select_Statement__Group_4__2__Impl )
            // InternalCQL.g:2715:2: rule__Select_Statement__Group_4__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_4__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_4__2"


    // $ANTLR start "rule__Select_Statement__Group_4__2__Impl"
    // InternalCQL.g:2721:1: rule__Select_Statement__Group_4__2__Impl : ( ( rule__Select_Statement__Group_4_2__0 )* ) ;
    public final void rule__Select_Statement__Group_4__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2725:1: ( ( ( rule__Select_Statement__Group_4_2__0 )* ) )
            // InternalCQL.g:2726:1: ( ( rule__Select_Statement__Group_4_2__0 )* )
            {
            // InternalCQL.g:2726:1: ( ( rule__Select_Statement__Group_4_2__0 )* )
            // InternalCQL.g:2727:2: ( rule__Select_Statement__Group_4_2__0 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_4_2()); 
            // InternalCQL.g:2728:2: ( rule__Select_Statement__Group_4_2__0 )*
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( (LA28_0==42) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // InternalCQL.g:2728:3: rule__Select_Statement__Group_4_2__0
            	    {
            	    pushFollow(FOLLOW_33);
            	    rule__Select_Statement__Group_4_2__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop28;
                }
            } while (true);

             after(grammarAccess.getSelect_StatementAccess().getGroup_4_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_4__2__Impl"


    // $ANTLR start "rule__Select_Statement__Group_4_2__0"
    // InternalCQL.g:2737:1: rule__Select_Statement__Group_4_2__0 : rule__Select_Statement__Group_4_2__0__Impl rule__Select_Statement__Group_4_2__1 ;
    public final void rule__Select_Statement__Group_4_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2741:1: ( rule__Select_Statement__Group_4_2__0__Impl rule__Select_Statement__Group_4_2__1 )
            // InternalCQL.g:2742:2: rule__Select_Statement__Group_4_2__0__Impl rule__Select_Statement__Group_4_2__1
            {
            pushFollow(FOLLOW_9);
            rule__Select_Statement__Group_4_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_4_2__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_4_2__0"


    // $ANTLR start "rule__Select_Statement__Group_4_2__0__Impl"
    // InternalCQL.g:2749:1: rule__Select_Statement__Group_4_2__0__Impl : ( ',' ) ;
    public final void rule__Select_Statement__Group_4_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2753:1: ( ( ',' ) )
            // InternalCQL.g:2754:1: ( ',' )
            {
            // InternalCQL.g:2754:1: ( ',' )
            // InternalCQL.g:2755:2: ','
            {
             before(grammarAccess.getSelect_StatementAccess().getCommaKeyword_4_2_0()); 
            match(input,42,FOLLOW_2); 
             after(grammarAccess.getSelect_StatementAccess().getCommaKeyword_4_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_4_2__0__Impl"


    // $ANTLR start "rule__Select_Statement__Group_4_2__1"
    // InternalCQL.g:2764:1: rule__Select_Statement__Group_4_2__1 : rule__Select_Statement__Group_4_2__1__Impl rule__Select_Statement__Group_4_2__2 ;
    public final void rule__Select_Statement__Group_4_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2768:1: ( rule__Select_Statement__Group_4_2__1__Impl rule__Select_Statement__Group_4_2__2 )
            // InternalCQL.g:2769:2: rule__Select_Statement__Group_4_2__1__Impl rule__Select_Statement__Group_4_2__2
            {
            pushFollow(FOLLOW_36);
            rule__Select_Statement__Group_4_2__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_4_2__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_4_2__1"


    // $ANTLR start "rule__Select_Statement__Group_4_2__1__Impl"
    // InternalCQL.g:2776:1: rule__Select_Statement__Group_4_2__1__Impl : ( ( rule__Select_Statement__SourcesAssignment_4_2_1 ) ) ;
    public final void rule__Select_Statement__Group_4_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2780:1: ( ( ( rule__Select_Statement__SourcesAssignment_4_2_1 ) ) )
            // InternalCQL.g:2781:1: ( ( rule__Select_Statement__SourcesAssignment_4_2_1 ) )
            {
            // InternalCQL.g:2781:1: ( ( rule__Select_Statement__SourcesAssignment_4_2_1 ) )
            // InternalCQL.g:2782:2: ( rule__Select_Statement__SourcesAssignment_4_2_1 )
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_2_1()); 
            // InternalCQL.g:2783:2: ( rule__Select_Statement__SourcesAssignment_4_2_1 )
            // InternalCQL.g:2783:3: rule__Select_Statement__SourcesAssignment_4_2_1
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__SourcesAssignment_4_2_1();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_2_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_4_2__1__Impl"


    // $ANTLR start "rule__Select_Statement__Group_4_2__2"
    // InternalCQL.g:2791:1: rule__Select_Statement__Group_4_2__2 : rule__Select_Statement__Group_4_2__2__Impl ;
    public final void rule__Select_Statement__Group_4_2__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2795:1: ( rule__Select_Statement__Group_4_2__2__Impl )
            // InternalCQL.g:2796:2: rule__Select_Statement__Group_4_2__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_4_2__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_4_2__2"


    // $ANTLR start "rule__Select_Statement__Group_4_2__2__Impl"
    // InternalCQL.g:2802:1: rule__Select_Statement__Group_4_2__2__Impl : ( ( rule__Select_Statement__WindowsAssignment_4_2_2 )? ) ;
    public final void rule__Select_Statement__Group_4_2__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2806:1: ( ( ( rule__Select_Statement__WindowsAssignment_4_2_2 )? ) )
            // InternalCQL.g:2807:1: ( ( rule__Select_Statement__WindowsAssignment_4_2_2 )? )
            {
            // InternalCQL.g:2807:1: ( ( rule__Select_Statement__WindowsAssignment_4_2_2 )? )
            // InternalCQL.g:2808:2: ( rule__Select_Statement__WindowsAssignment_4_2_2 )?
            {
             before(grammarAccess.getSelect_StatementAccess().getWindowsAssignment_4_2_2()); 
            // InternalCQL.g:2809:2: ( rule__Select_Statement__WindowsAssignment_4_2_2 )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==44) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // InternalCQL.g:2809:3: rule__Select_Statement__WindowsAssignment_4_2_2
                    {
                    pushFollow(FOLLOW_2);
                    rule__Select_Statement__WindowsAssignment_4_2_2();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSelect_StatementAccess().getWindowsAssignment_4_2_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_4_2__2__Impl"


    // $ANTLR start "rule__Select_Statement__Group_5__0"
    // InternalCQL.g:2818:1: rule__Select_Statement__Group_5__0 : rule__Select_Statement__Group_5__0__Impl rule__Select_Statement__Group_5__1 ;
    public final void rule__Select_Statement__Group_5__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2822:1: ( rule__Select_Statement__Group_5__0__Impl rule__Select_Statement__Group_5__1 )
            // InternalCQL.g:2823:2: rule__Select_Statement__Group_5__0__Impl rule__Select_Statement__Group_5__1
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
    // InternalCQL.g:2830:1: rule__Select_Statement__Group_5__0__Impl : ( 'WHERE' ) ;
    public final void rule__Select_Statement__Group_5__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2834:1: ( ( 'WHERE' ) )
            // InternalCQL.g:2835:1: ( 'WHERE' )
            {
            // InternalCQL.g:2835:1: ( 'WHERE' )
            // InternalCQL.g:2836:2: 'WHERE'
            {
             before(grammarAccess.getSelect_StatementAccess().getWHEREKeyword_5_0()); 
            match(input,43,FOLLOW_2); 
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
    // InternalCQL.g:2845:1: rule__Select_Statement__Group_5__1 : rule__Select_Statement__Group_5__1__Impl ;
    public final void rule__Select_Statement__Group_5__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2849:1: ( rule__Select_Statement__Group_5__1__Impl )
            // InternalCQL.g:2850:2: rule__Select_Statement__Group_5__1__Impl
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
    // InternalCQL.g:2856:1: rule__Select_Statement__Group_5__1__Impl : ( ( rule__Select_Statement__PredicatesAssignment_5_1 ) ) ;
    public final void rule__Select_Statement__Group_5__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2860:1: ( ( ( rule__Select_Statement__PredicatesAssignment_5_1 ) ) )
            // InternalCQL.g:2861:1: ( ( rule__Select_Statement__PredicatesAssignment_5_1 ) )
            {
            // InternalCQL.g:2861:1: ( ( rule__Select_Statement__PredicatesAssignment_5_1 ) )
            // InternalCQL.g:2862:2: ( rule__Select_Statement__PredicatesAssignment_5_1 )
            {
             before(grammarAccess.getSelect_StatementAccess().getPredicatesAssignment_5_1()); 
            // InternalCQL.g:2863:2: ( rule__Select_Statement__PredicatesAssignment_5_1 )
            // InternalCQL.g:2863:3: rule__Select_Statement__PredicatesAssignment_5_1
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


    // $ANTLR start "rule__Window__Group__0"
    // InternalCQL.g:2872:1: rule__Window__Group__0 : rule__Window__Group__0__Impl rule__Window__Group__1 ;
    public final void rule__Window__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2876:1: ( rule__Window__Group__0__Impl rule__Window__Group__1 )
            // InternalCQL.g:2877:2: rule__Window__Group__0__Impl rule__Window__Group__1
            {
            pushFollow(FOLLOW_36);
            rule__Window__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window__Group__0"


    // $ANTLR start "rule__Window__Group__0__Impl"
    // InternalCQL.g:2884:1: rule__Window__Group__0__Impl : ( '[' ) ;
    public final void rule__Window__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2888:1: ( ( '[' ) )
            // InternalCQL.g:2889:1: ( '[' )
            {
            // InternalCQL.g:2889:1: ( '[' )
            // InternalCQL.g:2890:2: '['
            {
             before(grammarAccess.getWindowAccess().getLeftSquareBracketKeyword_0()); 
            match(input,44,FOLLOW_2); 
             after(grammarAccess.getWindowAccess().getLeftSquareBracketKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window__Group__0__Impl"


    // $ANTLR start "rule__Window__Group__1"
    // InternalCQL.g:2899:1: rule__Window__Group__1 : rule__Window__Group__1__Impl ;
    public final void rule__Window__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2903:1: ( rule__Window__Group__1__Impl )
            // InternalCQL.g:2904:2: rule__Window__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Window__Group__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window__Group__1"


    // $ANTLR start "rule__Window__Group__1__Impl"
    // InternalCQL.g:2910:1: rule__Window__Group__1__Impl : ( ( rule__Window__Alternatives_1 ) ) ;
    public final void rule__Window__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2914:1: ( ( ( rule__Window__Alternatives_1 ) ) )
            // InternalCQL.g:2915:1: ( ( rule__Window__Alternatives_1 ) )
            {
            // InternalCQL.g:2915:1: ( ( rule__Window__Alternatives_1 ) )
            // InternalCQL.g:2916:2: ( rule__Window__Alternatives_1 )
            {
             before(grammarAccess.getWindowAccess().getAlternatives_1()); 
            // InternalCQL.g:2917:2: ( rule__Window__Alternatives_1 )
            // InternalCQL.g:2917:3: rule__Window__Alternatives_1
            {
            pushFollow(FOLLOW_2);
            rule__Window__Alternatives_1();

            state._fsp--;


            }

             after(grammarAccess.getWindowAccess().getAlternatives_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window__Group__1__Impl"


    // $ANTLR start "rule__Window_Unbounded__Group__0"
    // InternalCQL.g:2926:1: rule__Window_Unbounded__Group__0 : rule__Window_Unbounded__Group__0__Impl rule__Window_Unbounded__Group__1 ;
    public final void rule__Window_Unbounded__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2930:1: ( rule__Window_Unbounded__Group__0__Impl rule__Window_Unbounded__Group__1 )
            // InternalCQL.g:2931:2: rule__Window_Unbounded__Group__0__Impl rule__Window_Unbounded__Group__1
            {
            pushFollow(FOLLOW_36);
            rule__Window_Unbounded__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Unbounded__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Unbounded__Group__0"


    // $ANTLR start "rule__Window_Unbounded__Group__0__Impl"
    // InternalCQL.g:2938:1: rule__Window_Unbounded__Group__0__Impl : ( () ) ;
    public final void rule__Window_Unbounded__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2942:1: ( ( () ) )
            // InternalCQL.g:2943:1: ( () )
            {
            // InternalCQL.g:2943:1: ( () )
            // InternalCQL.g:2944:2: ()
            {
             before(grammarAccess.getWindow_UnboundedAccess().getWindowTypeAction_0()); 
            // InternalCQL.g:2945:2: ()
            // InternalCQL.g:2945:3: 
            {
            }

             after(grammarAccess.getWindow_UnboundedAccess().getWindowTypeAction_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Unbounded__Group__0__Impl"


    // $ANTLR start "rule__Window_Unbounded__Group__1"
    // InternalCQL.g:2953:1: rule__Window_Unbounded__Group__1 : rule__Window_Unbounded__Group__1__Impl ;
    public final void rule__Window_Unbounded__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2957:1: ( rule__Window_Unbounded__Group__1__Impl )
            // InternalCQL.g:2958:2: rule__Window_Unbounded__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Window_Unbounded__Group__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Unbounded__Group__1"


    // $ANTLR start "rule__Window_Unbounded__Group__1__Impl"
    // InternalCQL.g:2964:1: rule__Window_Unbounded__Group__1__Impl : ( ( rule__Window_Unbounded__Group_1__0 )? ) ;
    public final void rule__Window_Unbounded__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2968:1: ( ( ( rule__Window_Unbounded__Group_1__0 )? ) )
            // InternalCQL.g:2969:1: ( ( rule__Window_Unbounded__Group_1__0 )? )
            {
            // InternalCQL.g:2969:1: ( ( rule__Window_Unbounded__Group_1__0 )? )
            // InternalCQL.g:2970:2: ( rule__Window_Unbounded__Group_1__0 )?
            {
             before(grammarAccess.getWindow_UnboundedAccess().getGroup_1()); 
            // InternalCQL.g:2971:2: ( rule__Window_Unbounded__Group_1__0 )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==44) ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // InternalCQL.g:2971:3: rule__Window_Unbounded__Group_1__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Window_Unbounded__Group_1__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getWindow_UnboundedAccess().getGroup_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Unbounded__Group__1__Impl"


    // $ANTLR start "rule__Window_Unbounded__Group_1__0"
    // InternalCQL.g:2980:1: rule__Window_Unbounded__Group_1__0 : rule__Window_Unbounded__Group_1__0__Impl rule__Window_Unbounded__Group_1__1 ;
    public final void rule__Window_Unbounded__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2984:1: ( rule__Window_Unbounded__Group_1__0__Impl rule__Window_Unbounded__Group_1__1 )
            // InternalCQL.g:2985:2: rule__Window_Unbounded__Group_1__0__Impl rule__Window_Unbounded__Group_1__1
            {
            pushFollow(FOLLOW_37);
            rule__Window_Unbounded__Group_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Unbounded__Group_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Unbounded__Group_1__0"


    // $ANTLR start "rule__Window_Unbounded__Group_1__0__Impl"
    // InternalCQL.g:2992:1: rule__Window_Unbounded__Group_1__0__Impl : ( '[' ) ;
    public final void rule__Window_Unbounded__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2996:1: ( ( '[' ) )
            // InternalCQL.g:2997:1: ( '[' )
            {
            // InternalCQL.g:2997:1: ( '[' )
            // InternalCQL.g:2998:2: '['
            {
             before(grammarAccess.getWindow_UnboundedAccess().getLeftSquareBracketKeyword_1_0()); 
            match(input,44,FOLLOW_2); 
             after(grammarAccess.getWindow_UnboundedAccess().getLeftSquareBracketKeyword_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Unbounded__Group_1__0__Impl"


    // $ANTLR start "rule__Window_Unbounded__Group_1__1"
    // InternalCQL.g:3007:1: rule__Window_Unbounded__Group_1__1 : rule__Window_Unbounded__Group_1__1__Impl rule__Window_Unbounded__Group_1__2 ;
    public final void rule__Window_Unbounded__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3011:1: ( rule__Window_Unbounded__Group_1__1__Impl rule__Window_Unbounded__Group_1__2 )
            // InternalCQL.g:3012:2: rule__Window_Unbounded__Group_1__1__Impl rule__Window_Unbounded__Group_1__2
            {
            pushFollow(FOLLOW_38);
            rule__Window_Unbounded__Group_1__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Unbounded__Group_1__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Unbounded__Group_1__1"


    // $ANTLR start "rule__Window_Unbounded__Group_1__1__Impl"
    // InternalCQL.g:3019:1: rule__Window_Unbounded__Group_1__1__Impl : ( 'UNBOUNDED' ) ;
    public final void rule__Window_Unbounded__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3023:1: ( ( 'UNBOUNDED' ) )
            // InternalCQL.g:3024:1: ( 'UNBOUNDED' )
            {
            // InternalCQL.g:3024:1: ( 'UNBOUNDED' )
            // InternalCQL.g:3025:2: 'UNBOUNDED'
            {
             before(grammarAccess.getWindow_UnboundedAccess().getUNBOUNDEDKeyword_1_1()); 
            match(input,45,FOLLOW_2); 
             after(grammarAccess.getWindow_UnboundedAccess().getUNBOUNDEDKeyword_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Unbounded__Group_1__1__Impl"


    // $ANTLR start "rule__Window_Unbounded__Group_1__2"
    // InternalCQL.g:3034:1: rule__Window_Unbounded__Group_1__2 : rule__Window_Unbounded__Group_1__2__Impl ;
    public final void rule__Window_Unbounded__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3038:1: ( rule__Window_Unbounded__Group_1__2__Impl )
            // InternalCQL.g:3039:2: rule__Window_Unbounded__Group_1__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Window_Unbounded__Group_1__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Unbounded__Group_1__2"


    // $ANTLR start "rule__Window_Unbounded__Group_1__2__Impl"
    // InternalCQL.g:3045:1: rule__Window_Unbounded__Group_1__2__Impl : ( ']' ) ;
    public final void rule__Window_Unbounded__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3049:1: ( ( ']' ) )
            // InternalCQL.g:3050:1: ( ']' )
            {
            // InternalCQL.g:3050:1: ( ']' )
            // InternalCQL.g:3051:2: ']'
            {
             before(grammarAccess.getWindow_UnboundedAccess().getRightSquareBracketKeyword_1_2()); 
            match(input,46,FOLLOW_2); 
             after(grammarAccess.getWindow_UnboundedAccess().getRightSquareBracketKeyword_1_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Unbounded__Group_1__2__Impl"


    // $ANTLR start "rule__Window_Timebased__Group__0"
    // InternalCQL.g:3061:1: rule__Window_Timebased__Group__0 : rule__Window_Timebased__Group__0__Impl rule__Window_Timebased__Group__1 ;
    public final void rule__Window_Timebased__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3065:1: ( rule__Window_Timebased__Group__0__Impl rule__Window_Timebased__Group__1 )
            // InternalCQL.g:3066:2: rule__Window_Timebased__Group__0__Impl rule__Window_Timebased__Group__1
            {
            pushFollow(FOLLOW_36);
            rule__Window_Timebased__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__0"


    // $ANTLR start "rule__Window_Timebased__Group__0__Impl"
    // InternalCQL.g:3073:1: rule__Window_Timebased__Group__0__Impl : ( () ) ;
    public final void rule__Window_Timebased__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3077:1: ( ( () ) )
            // InternalCQL.g:3078:1: ( () )
            {
            // InternalCQL.g:3078:1: ( () )
            // InternalCQL.g:3079:2: ()
            {
             before(grammarAccess.getWindow_TimebasedAccess().getWindowTypeAction_0()); 
            // InternalCQL.g:3080:2: ()
            // InternalCQL.g:3080:3: 
            {
            }

             after(grammarAccess.getWindow_TimebasedAccess().getWindowTypeAction_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__0__Impl"


    // $ANTLR start "rule__Window_Timebased__Group__1"
    // InternalCQL.g:3088:1: rule__Window_Timebased__Group__1 : rule__Window_Timebased__Group__1__Impl rule__Window_Timebased__Group__2 ;
    public final void rule__Window_Timebased__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3092:1: ( rule__Window_Timebased__Group__1__Impl rule__Window_Timebased__Group__2 )
            // InternalCQL.g:3093:2: rule__Window_Timebased__Group__1__Impl rule__Window_Timebased__Group__2
            {
            pushFollow(FOLLOW_39);
            rule__Window_Timebased__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__1"


    // $ANTLR start "rule__Window_Timebased__Group__1__Impl"
    // InternalCQL.g:3100:1: rule__Window_Timebased__Group__1__Impl : ( '[' ) ;
    public final void rule__Window_Timebased__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3104:1: ( ( '[' ) )
            // InternalCQL.g:3105:1: ( '[' )
            {
            // InternalCQL.g:3105:1: ( '[' )
            // InternalCQL.g:3106:2: '['
            {
             before(grammarAccess.getWindow_TimebasedAccess().getLeftSquareBracketKeyword_1()); 
            match(input,44,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getLeftSquareBracketKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__1__Impl"


    // $ANTLR start "rule__Window_Timebased__Group__2"
    // InternalCQL.g:3115:1: rule__Window_Timebased__Group__2 : rule__Window_Timebased__Group__2__Impl rule__Window_Timebased__Group__3 ;
    public final void rule__Window_Timebased__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3119:1: ( rule__Window_Timebased__Group__2__Impl rule__Window_Timebased__Group__3 )
            // InternalCQL.g:3120:2: rule__Window_Timebased__Group__2__Impl rule__Window_Timebased__Group__3
            {
            pushFollow(FOLLOW_5);
            rule__Window_Timebased__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__2"


    // $ANTLR start "rule__Window_Timebased__Group__2__Impl"
    // InternalCQL.g:3127:1: rule__Window_Timebased__Group__2__Impl : ( 'SIZE' ) ;
    public final void rule__Window_Timebased__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3131:1: ( ( 'SIZE' ) )
            // InternalCQL.g:3132:1: ( 'SIZE' )
            {
            // InternalCQL.g:3132:1: ( 'SIZE' )
            // InternalCQL.g:3133:2: 'SIZE'
            {
             before(grammarAccess.getWindow_TimebasedAccess().getSIZEKeyword_2()); 
            match(input,47,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getSIZEKeyword_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__2__Impl"


    // $ANTLR start "rule__Window_Timebased__Group__3"
    // InternalCQL.g:3142:1: rule__Window_Timebased__Group__3 : rule__Window_Timebased__Group__3__Impl rule__Window_Timebased__Group__4 ;
    public final void rule__Window_Timebased__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3146:1: ( rule__Window_Timebased__Group__3__Impl rule__Window_Timebased__Group__4 )
            // InternalCQL.g:3147:2: rule__Window_Timebased__Group__3__Impl rule__Window_Timebased__Group__4
            {
            pushFollow(FOLLOW_7);
            rule__Window_Timebased__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__3"


    // $ANTLR start "rule__Window_Timebased__Group__3__Impl"
    // InternalCQL.g:3154:1: rule__Window_Timebased__Group__3__Impl : ( ( rule__Window_Timebased__SizeAssignment_3 ) ) ;
    public final void rule__Window_Timebased__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3158:1: ( ( ( rule__Window_Timebased__SizeAssignment_3 ) ) )
            // InternalCQL.g:3159:1: ( ( rule__Window_Timebased__SizeAssignment_3 ) )
            {
            // InternalCQL.g:3159:1: ( ( rule__Window_Timebased__SizeAssignment_3 ) )
            // InternalCQL.g:3160:2: ( rule__Window_Timebased__SizeAssignment_3 )
            {
             before(grammarAccess.getWindow_TimebasedAccess().getSizeAssignment_3()); 
            // InternalCQL.g:3161:2: ( rule__Window_Timebased__SizeAssignment_3 )
            // InternalCQL.g:3161:3: rule__Window_Timebased__SizeAssignment_3
            {
            pushFollow(FOLLOW_2);
            rule__Window_Timebased__SizeAssignment_3();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TimebasedAccess().getSizeAssignment_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__3__Impl"


    // $ANTLR start "rule__Window_Timebased__Group__4"
    // InternalCQL.g:3169:1: rule__Window_Timebased__Group__4 : rule__Window_Timebased__Group__4__Impl rule__Window_Timebased__Group__5 ;
    public final void rule__Window_Timebased__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3173:1: ( rule__Window_Timebased__Group__4__Impl rule__Window_Timebased__Group__5 )
            // InternalCQL.g:3174:2: rule__Window_Timebased__Group__4__Impl rule__Window_Timebased__Group__5
            {
            pushFollow(FOLLOW_40);
            rule__Window_Timebased__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__4"


    // $ANTLR start "rule__Window_Timebased__Group__4__Impl"
    // InternalCQL.g:3181:1: rule__Window_Timebased__Group__4__Impl : ( ( rule__Window_Timebased__UnitAssignment_4 ) ) ;
    public final void rule__Window_Timebased__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3185:1: ( ( ( rule__Window_Timebased__UnitAssignment_4 ) ) )
            // InternalCQL.g:3186:1: ( ( rule__Window_Timebased__UnitAssignment_4 ) )
            {
            // InternalCQL.g:3186:1: ( ( rule__Window_Timebased__UnitAssignment_4 ) )
            // InternalCQL.g:3187:2: ( rule__Window_Timebased__UnitAssignment_4 )
            {
             before(grammarAccess.getWindow_TimebasedAccess().getUnitAssignment_4()); 
            // InternalCQL.g:3188:2: ( rule__Window_Timebased__UnitAssignment_4 )
            // InternalCQL.g:3188:3: rule__Window_Timebased__UnitAssignment_4
            {
            pushFollow(FOLLOW_2);
            rule__Window_Timebased__UnitAssignment_4();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TimebasedAccess().getUnitAssignment_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__4__Impl"


    // $ANTLR start "rule__Window_Timebased__Group__5"
    // InternalCQL.g:3196:1: rule__Window_Timebased__Group__5 : rule__Window_Timebased__Group__5__Impl rule__Window_Timebased__Group__6 ;
    public final void rule__Window_Timebased__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3200:1: ( rule__Window_Timebased__Group__5__Impl rule__Window_Timebased__Group__6 )
            // InternalCQL.g:3201:2: rule__Window_Timebased__Group__5__Impl rule__Window_Timebased__Group__6
            {
            pushFollow(FOLLOW_40);
            rule__Window_Timebased__Group__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group__6();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__5"


    // $ANTLR start "rule__Window_Timebased__Group__5__Impl"
    // InternalCQL.g:3208:1: rule__Window_Timebased__Group__5__Impl : ( ( rule__Window_Timebased__Group_5__0 )? ) ;
    public final void rule__Window_Timebased__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3212:1: ( ( ( rule__Window_Timebased__Group_5__0 )? ) )
            // InternalCQL.g:3213:1: ( ( rule__Window_Timebased__Group_5__0 )? )
            {
            // InternalCQL.g:3213:1: ( ( rule__Window_Timebased__Group_5__0 )? )
            // InternalCQL.g:3214:2: ( rule__Window_Timebased__Group_5__0 )?
            {
             before(grammarAccess.getWindow_TimebasedAccess().getGroup_5()); 
            // InternalCQL.g:3215:2: ( rule__Window_Timebased__Group_5__0 )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==49) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // InternalCQL.g:3215:3: rule__Window_Timebased__Group_5__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Window_Timebased__Group_5__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getWindow_TimebasedAccess().getGroup_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__5__Impl"


    // $ANTLR start "rule__Window_Timebased__Group__6"
    // InternalCQL.g:3223:1: rule__Window_Timebased__Group__6 : rule__Window_Timebased__Group__6__Impl rule__Window_Timebased__Group__7 ;
    public final void rule__Window_Timebased__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3227:1: ( rule__Window_Timebased__Group__6__Impl rule__Window_Timebased__Group__7 )
            // InternalCQL.g:3228:2: rule__Window_Timebased__Group__6__Impl rule__Window_Timebased__Group__7
            {
            pushFollow(FOLLOW_41);
            rule__Window_Timebased__Group__6__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group__7();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__6"


    // $ANTLR start "rule__Window_Timebased__Group__6__Impl"
    // InternalCQL.g:3235:1: rule__Window_Timebased__Group__6__Impl : ( 'TIME' ) ;
    public final void rule__Window_Timebased__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3239:1: ( ( 'TIME' ) )
            // InternalCQL.g:3240:1: ( 'TIME' )
            {
            // InternalCQL.g:3240:1: ( 'TIME' )
            // InternalCQL.g:3241:2: 'TIME'
            {
             before(grammarAccess.getWindow_TimebasedAccess().getTIMEKeyword_6()); 
            match(input,48,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getTIMEKeyword_6()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__6__Impl"


    // $ANTLR start "rule__Window_Timebased__Group__7"
    // InternalCQL.g:3250:1: rule__Window_Timebased__Group__7 : rule__Window_Timebased__Group__7__Impl rule__Window_Timebased__Group__8 ;
    public final void rule__Window_Timebased__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3254:1: ( rule__Window_Timebased__Group__7__Impl rule__Window_Timebased__Group__8 )
            // InternalCQL.g:3255:2: rule__Window_Timebased__Group__7__Impl rule__Window_Timebased__Group__8
            {
            pushFollow(FOLLOW_41);
            rule__Window_Timebased__Group__7__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group__8();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__7"


    // $ANTLR start "rule__Window_Timebased__Group__7__Impl"
    // InternalCQL.g:3262:1: rule__Window_Timebased__Group__7__Impl : ( ( rule__Window_Timebased__Group_7__0 )? ) ;
    public final void rule__Window_Timebased__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3266:1: ( ( ( rule__Window_Timebased__Group_7__0 )? ) )
            // InternalCQL.g:3267:1: ( ( rule__Window_Timebased__Group_7__0 )? )
            {
            // InternalCQL.g:3267:1: ( ( rule__Window_Timebased__Group_7__0 )? )
            // InternalCQL.g:3268:2: ( rule__Window_Timebased__Group_7__0 )?
            {
             before(grammarAccess.getWindow_TimebasedAccess().getGroup_7()); 
            // InternalCQL.g:3269:2: ( rule__Window_Timebased__Group_7__0 )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==50) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // InternalCQL.g:3269:3: rule__Window_Timebased__Group_7__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Window_Timebased__Group_7__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getWindow_TimebasedAccess().getGroup_7()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__7__Impl"


    // $ANTLR start "rule__Window_Timebased__Group__8"
    // InternalCQL.g:3277:1: rule__Window_Timebased__Group__8 : rule__Window_Timebased__Group__8__Impl ;
    public final void rule__Window_Timebased__Group__8() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3281:1: ( rule__Window_Timebased__Group__8__Impl )
            // InternalCQL.g:3282:2: rule__Window_Timebased__Group__8__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group__8__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__8"


    // $ANTLR start "rule__Window_Timebased__Group__8__Impl"
    // InternalCQL.g:3288:1: rule__Window_Timebased__Group__8__Impl : ( ']' ) ;
    public final void rule__Window_Timebased__Group__8__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3292:1: ( ( ']' ) )
            // InternalCQL.g:3293:1: ( ']' )
            {
            // InternalCQL.g:3293:1: ( ']' )
            // InternalCQL.g:3294:2: ']'
            {
             before(grammarAccess.getWindow_TimebasedAccess().getRightSquareBracketKeyword_8()); 
            match(input,46,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getRightSquareBracketKeyword_8()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__8__Impl"


    // $ANTLR start "rule__Window_Timebased__Group_5__0"
    // InternalCQL.g:3304:1: rule__Window_Timebased__Group_5__0 : rule__Window_Timebased__Group_5__0__Impl rule__Window_Timebased__Group_5__1 ;
    public final void rule__Window_Timebased__Group_5__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3308:1: ( rule__Window_Timebased__Group_5__0__Impl rule__Window_Timebased__Group_5__1 )
            // InternalCQL.g:3309:2: rule__Window_Timebased__Group_5__0__Impl rule__Window_Timebased__Group_5__1
            {
            pushFollow(FOLLOW_5);
            rule__Window_Timebased__Group_5__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group_5__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_5__0"


    // $ANTLR start "rule__Window_Timebased__Group_5__0__Impl"
    // InternalCQL.g:3316:1: rule__Window_Timebased__Group_5__0__Impl : ( 'ADVANCE' ) ;
    public final void rule__Window_Timebased__Group_5__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3320:1: ( ( 'ADVANCE' ) )
            // InternalCQL.g:3321:1: ( 'ADVANCE' )
            {
            // InternalCQL.g:3321:1: ( 'ADVANCE' )
            // InternalCQL.g:3322:2: 'ADVANCE'
            {
             before(grammarAccess.getWindow_TimebasedAccess().getADVANCEKeyword_5_0()); 
            match(input,49,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getADVANCEKeyword_5_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_5__0__Impl"


    // $ANTLR start "rule__Window_Timebased__Group_5__1"
    // InternalCQL.g:3331:1: rule__Window_Timebased__Group_5__1 : rule__Window_Timebased__Group_5__1__Impl rule__Window_Timebased__Group_5__2 ;
    public final void rule__Window_Timebased__Group_5__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3335:1: ( rule__Window_Timebased__Group_5__1__Impl rule__Window_Timebased__Group_5__2 )
            // InternalCQL.g:3336:2: rule__Window_Timebased__Group_5__1__Impl rule__Window_Timebased__Group_5__2
            {
            pushFollow(FOLLOW_7);
            rule__Window_Timebased__Group_5__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group_5__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_5__1"


    // $ANTLR start "rule__Window_Timebased__Group_5__1__Impl"
    // InternalCQL.g:3343:1: rule__Window_Timebased__Group_5__1__Impl : ( ( rule__Window_Timebased__Advance_sizeAssignment_5_1 ) ) ;
    public final void rule__Window_Timebased__Group_5__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3347:1: ( ( ( rule__Window_Timebased__Advance_sizeAssignment_5_1 ) ) )
            // InternalCQL.g:3348:1: ( ( rule__Window_Timebased__Advance_sizeAssignment_5_1 ) )
            {
            // InternalCQL.g:3348:1: ( ( rule__Window_Timebased__Advance_sizeAssignment_5_1 ) )
            // InternalCQL.g:3349:2: ( rule__Window_Timebased__Advance_sizeAssignment_5_1 )
            {
             before(grammarAccess.getWindow_TimebasedAccess().getAdvance_sizeAssignment_5_1()); 
            // InternalCQL.g:3350:2: ( rule__Window_Timebased__Advance_sizeAssignment_5_1 )
            // InternalCQL.g:3350:3: rule__Window_Timebased__Advance_sizeAssignment_5_1
            {
            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Advance_sizeAssignment_5_1();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TimebasedAccess().getAdvance_sizeAssignment_5_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_5__1__Impl"


    // $ANTLR start "rule__Window_Timebased__Group_5__2"
    // InternalCQL.g:3358:1: rule__Window_Timebased__Group_5__2 : rule__Window_Timebased__Group_5__2__Impl ;
    public final void rule__Window_Timebased__Group_5__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3362:1: ( rule__Window_Timebased__Group_5__2__Impl )
            // InternalCQL.g:3363:2: rule__Window_Timebased__Group_5__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group_5__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_5__2"


    // $ANTLR start "rule__Window_Timebased__Group_5__2__Impl"
    // InternalCQL.g:3369:1: rule__Window_Timebased__Group_5__2__Impl : ( ( rule__Window_Timebased__Advance_unitAssignment_5_2 ) ) ;
    public final void rule__Window_Timebased__Group_5__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3373:1: ( ( ( rule__Window_Timebased__Advance_unitAssignment_5_2 ) ) )
            // InternalCQL.g:3374:1: ( ( rule__Window_Timebased__Advance_unitAssignment_5_2 ) )
            {
            // InternalCQL.g:3374:1: ( ( rule__Window_Timebased__Advance_unitAssignment_5_2 ) )
            // InternalCQL.g:3375:2: ( rule__Window_Timebased__Advance_unitAssignment_5_2 )
            {
             before(grammarAccess.getWindow_TimebasedAccess().getAdvance_unitAssignment_5_2()); 
            // InternalCQL.g:3376:2: ( rule__Window_Timebased__Advance_unitAssignment_5_2 )
            // InternalCQL.g:3376:3: rule__Window_Timebased__Advance_unitAssignment_5_2
            {
            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Advance_unitAssignment_5_2();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TimebasedAccess().getAdvance_unitAssignment_5_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_5__2__Impl"


    // $ANTLR start "rule__Window_Timebased__Group_7__0"
    // InternalCQL.g:3385:1: rule__Window_Timebased__Group_7__0 : rule__Window_Timebased__Group_7__0__Impl rule__Window_Timebased__Group_7__1 ;
    public final void rule__Window_Timebased__Group_7__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3389:1: ( rule__Window_Timebased__Group_7__0__Impl rule__Window_Timebased__Group_7__1 )
            // InternalCQL.g:3390:2: rule__Window_Timebased__Group_7__0__Impl rule__Window_Timebased__Group_7__1
            {
            pushFollow(FOLLOW_42);
            rule__Window_Timebased__Group_7__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group_7__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_7__0"


    // $ANTLR start "rule__Window_Timebased__Group_7__0__Impl"
    // InternalCQL.g:3397:1: rule__Window_Timebased__Group_7__0__Impl : ( 'PARTITION' ) ;
    public final void rule__Window_Timebased__Group_7__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3401:1: ( ( 'PARTITION' ) )
            // InternalCQL.g:3402:1: ( 'PARTITION' )
            {
            // InternalCQL.g:3402:1: ( 'PARTITION' )
            // InternalCQL.g:3403:2: 'PARTITION'
            {
             before(grammarAccess.getWindow_TimebasedAccess().getPARTITIONKeyword_7_0()); 
            match(input,50,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getPARTITIONKeyword_7_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_7__0__Impl"


    // $ANTLR start "rule__Window_Timebased__Group_7__1"
    // InternalCQL.g:3412:1: rule__Window_Timebased__Group_7__1 : rule__Window_Timebased__Group_7__1__Impl rule__Window_Timebased__Group_7__2 ;
    public final void rule__Window_Timebased__Group_7__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3416:1: ( rule__Window_Timebased__Group_7__1__Impl rule__Window_Timebased__Group_7__2 )
            // InternalCQL.g:3417:2: rule__Window_Timebased__Group_7__1__Impl rule__Window_Timebased__Group_7__2
            {
            pushFollow(FOLLOW_9);
            rule__Window_Timebased__Group_7__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group_7__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_7__1"


    // $ANTLR start "rule__Window_Timebased__Group_7__1__Impl"
    // InternalCQL.g:3424:1: rule__Window_Timebased__Group_7__1__Impl : ( 'BY' ) ;
    public final void rule__Window_Timebased__Group_7__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3428:1: ( ( 'BY' ) )
            // InternalCQL.g:3429:1: ( 'BY' )
            {
            // InternalCQL.g:3429:1: ( 'BY' )
            // InternalCQL.g:3430:2: 'BY'
            {
             before(grammarAccess.getWindow_TimebasedAccess().getBYKeyword_7_1()); 
            match(input,51,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getBYKeyword_7_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_7__1__Impl"


    // $ANTLR start "rule__Window_Timebased__Group_7__2"
    // InternalCQL.g:3439:1: rule__Window_Timebased__Group_7__2 : rule__Window_Timebased__Group_7__2__Impl ;
    public final void rule__Window_Timebased__Group_7__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3443:1: ( rule__Window_Timebased__Group_7__2__Impl )
            // InternalCQL.g:3444:2: rule__Window_Timebased__Group_7__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group_7__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_7__2"


    // $ANTLR start "rule__Window_Timebased__Group_7__2__Impl"
    // InternalCQL.g:3450:1: rule__Window_Timebased__Group_7__2__Impl : ( ( rule__Window_Timebased__Partition_attributeAssignment_7_2 ) ) ;
    public final void rule__Window_Timebased__Group_7__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3454:1: ( ( ( rule__Window_Timebased__Partition_attributeAssignment_7_2 ) ) )
            // InternalCQL.g:3455:1: ( ( rule__Window_Timebased__Partition_attributeAssignment_7_2 ) )
            {
            // InternalCQL.g:3455:1: ( ( rule__Window_Timebased__Partition_attributeAssignment_7_2 ) )
            // InternalCQL.g:3456:2: ( rule__Window_Timebased__Partition_attributeAssignment_7_2 )
            {
             before(grammarAccess.getWindow_TimebasedAccess().getPartition_attributeAssignment_7_2()); 
            // InternalCQL.g:3457:2: ( rule__Window_Timebased__Partition_attributeAssignment_7_2 )
            // InternalCQL.g:3457:3: rule__Window_Timebased__Partition_attributeAssignment_7_2
            {
            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Partition_attributeAssignment_7_2();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TimebasedAccess().getPartition_attributeAssignment_7_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_7__2__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group__0"
    // InternalCQL.g:3466:1: rule__Window_Tuplebased__Group__0 : rule__Window_Tuplebased__Group__0__Impl rule__Window_Tuplebased__Group__1 ;
    public final void rule__Window_Tuplebased__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3470:1: ( rule__Window_Tuplebased__Group__0__Impl rule__Window_Tuplebased__Group__1 )
            // InternalCQL.g:3471:2: rule__Window_Tuplebased__Group__0__Impl rule__Window_Tuplebased__Group__1
            {
            pushFollow(FOLLOW_36);
            rule__Window_Tuplebased__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__0"


    // $ANTLR start "rule__Window_Tuplebased__Group__0__Impl"
    // InternalCQL.g:3478:1: rule__Window_Tuplebased__Group__0__Impl : ( () ) ;
    public final void rule__Window_Tuplebased__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3482:1: ( ( () ) )
            // InternalCQL.g:3483:1: ( () )
            {
            // InternalCQL.g:3483:1: ( () )
            // InternalCQL.g:3484:2: ()
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getWindowTypeAction_0()); 
            // InternalCQL.g:3485:2: ()
            // InternalCQL.g:3485:3: 
            {
            }

             after(grammarAccess.getWindow_TuplebasedAccess().getWindowTypeAction_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__0__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group__1"
    // InternalCQL.g:3493:1: rule__Window_Tuplebased__Group__1 : rule__Window_Tuplebased__Group__1__Impl rule__Window_Tuplebased__Group__2 ;
    public final void rule__Window_Tuplebased__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3497:1: ( rule__Window_Tuplebased__Group__1__Impl rule__Window_Tuplebased__Group__2 )
            // InternalCQL.g:3498:2: rule__Window_Tuplebased__Group__1__Impl rule__Window_Tuplebased__Group__2
            {
            pushFollow(FOLLOW_39);
            rule__Window_Tuplebased__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__1"


    // $ANTLR start "rule__Window_Tuplebased__Group__1__Impl"
    // InternalCQL.g:3505:1: rule__Window_Tuplebased__Group__1__Impl : ( '[' ) ;
    public final void rule__Window_Tuplebased__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3509:1: ( ( '[' ) )
            // InternalCQL.g:3510:1: ( '[' )
            {
            // InternalCQL.g:3510:1: ( '[' )
            // InternalCQL.g:3511:2: '['
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getLeftSquareBracketKeyword_1()); 
            match(input,44,FOLLOW_2); 
             after(grammarAccess.getWindow_TuplebasedAccess().getLeftSquareBracketKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__1__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group__2"
    // InternalCQL.g:3520:1: rule__Window_Tuplebased__Group__2 : rule__Window_Tuplebased__Group__2__Impl rule__Window_Tuplebased__Group__3 ;
    public final void rule__Window_Tuplebased__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3524:1: ( rule__Window_Tuplebased__Group__2__Impl rule__Window_Tuplebased__Group__3 )
            // InternalCQL.g:3525:2: rule__Window_Tuplebased__Group__2__Impl rule__Window_Tuplebased__Group__3
            {
            pushFollow(FOLLOW_5);
            rule__Window_Tuplebased__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__2"


    // $ANTLR start "rule__Window_Tuplebased__Group__2__Impl"
    // InternalCQL.g:3532:1: rule__Window_Tuplebased__Group__2__Impl : ( 'SIZE' ) ;
    public final void rule__Window_Tuplebased__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3536:1: ( ( 'SIZE' ) )
            // InternalCQL.g:3537:1: ( 'SIZE' )
            {
            // InternalCQL.g:3537:1: ( 'SIZE' )
            // InternalCQL.g:3538:2: 'SIZE'
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getSIZEKeyword_2()); 
            match(input,47,FOLLOW_2); 
             after(grammarAccess.getWindow_TuplebasedAccess().getSIZEKeyword_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__2__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group__3"
    // InternalCQL.g:3547:1: rule__Window_Tuplebased__Group__3 : rule__Window_Tuplebased__Group__3__Impl rule__Window_Tuplebased__Group__4 ;
    public final void rule__Window_Tuplebased__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3551:1: ( rule__Window_Tuplebased__Group__3__Impl rule__Window_Tuplebased__Group__4 )
            // InternalCQL.g:3552:2: rule__Window_Tuplebased__Group__3__Impl rule__Window_Tuplebased__Group__4
            {
            pushFollow(FOLLOW_43);
            rule__Window_Tuplebased__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__3"


    // $ANTLR start "rule__Window_Tuplebased__Group__3__Impl"
    // InternalCQL.g:3559:1: rule__Window_Tuplebased__Group__3__Impl : ( ( rule__Window_Tuplebased__SizeAssignment_3 ) ) ;
    public final void rule__Window_Tuplebased__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3563:1: ( ( ( rule__Window_Tuplebased__SizeAssignment_3 ) ) )
            // InternalCQL.g:3564:1: ( ( rule__Window_Tuplebased__SizeAssignment_3 ) )
            {
            // InternalCQL.g:3564:1: ( ( rule__Window_Tuplebased__SizeAssignment_3 ) )
            // InternalCQL.g:3565:2: ( rule__Window_Tuplebased__SizeAssignment_3 )
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getSizeAssignment_3()); 
            // InternalCQL.g:3566:2: ( rule__Window_Tuplebased__SizeAssignment_3 )
            // InternalCQL.g:3566:3: rule__Window_Tuplebased__SizeAssignment_3
            {
            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__SizeAssignment_3();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TuplebasedAccess().getSizeAssignment_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__3__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group__4"
    // InternalCQL.g:3574:1: rule__Window_Tuplebased__Group__4 : rule__Window_Tuplebased__Group__4__Impl rule__Window_Tuplebased__Group__5 ;
    public final void rule__Window_Tuplebased__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3578:1: ( rule__Window_Tuplebased__Group__4__Impl rule__Window_Tuplebased__Group__5 )
            // InternalCQL.g:3579:2: rule__Window_Tuplebased__Group__4__Impl rule__Window_Tuplebased__Group__5
            {
            pushFollow(FOLLOW_43);
            rule__Window_Tuplebased__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__4"


    // $ANTLR start "rule__Window_Tuplebased__Group__4__Impl"
    // InternalCQL.g:3586:1: rule__Window_Tuplebased__Group__4__Impl : ( ( rule__Window_Tuplebased__Group_4__0 )? ) ;
    public final void rule__Window_Tuplebased__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3590:1: ( ( ( rule__Window_Tuplebased__Group_4__0 )? ) )
            // InternalCQL.g:3591:1: ( ( rule__Window_Tuplebased__Group_4__0 )? )
            {
            // InternalCQL.g:3591:1: ( ( rule__Window_Tuplebased__Group_4__0 )? )
            // InternalCQL.g:3592:2: ( rule__Window_Tuplebased__Group_4__0 )?
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getGroup_4()); 
            // InternalCQL.g:3593:2: ( rule__Window_Tuplebased__Group_4__0 )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==49) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // InternalCQL.g:3593:3: rule__Window_Tuplebased__Group_4__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Window_Tuplebased__Group_4__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getWindow_TuplebasedAccess().getGroup_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__4__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group__5"
    // InternalCQL.g:3601:1: rule__Window_Tuplebased__Group__5 : rule__Window_Tuplebased__Group__5__Impl rule__Window_Tuplebased__Group__6 ;
    public final void rule__Window_Tuplebased__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3605:1: ( rule__Window_Tuplebased__Group__5__Impl rule__Window_Tuplebased__Group__6 )
            // InternalCQL.g:3606:2: rule__Window_Tuplebased__Group__5__Impl rule__Window_Tuplebased__Group__6
            {
            pushFollow(FOLLOW_41);
            rule__Window_Tuplebased__Group__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group__6();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__5"


    // $ANTLR start "rule__Window_Tuplebased__Group__5__Impl"
    // InternalCQL.g:3613:1: rule__Window_Tuplebased__Group__5__Impl : ( 'TUPLE' ) ;
    public final void rule__Window_Tuplebased__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3617:1: ( ( 'TUPLE' ) )
            // InternalCQL.g:3618:1: ( 'TUPLE' )
            {
            // InternalCQL.g:3618:1: ( 'TUPLE' )
            // InternalCQL.g:3619:2: 'TUPLE'
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getTUPLEKeyword_5()); 
            match(input,52,FOLLOW_2); 
             after(grammarAccess.getWindow_TuplebasedAccess().getTUPLEKeyword_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__5__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group__6"
    // InternalCQL.g:3628:1: rule__Window_Tuplebased__Group__6 : rule__Window_Tuplebased__Group__6__Impl rule__Window_Tuplebased__Group__7 ;
    public final void rule__Window_Tuplebased__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3632:1: ( rule__Window_Tuplebased__Group__6__Impl rule__Window_Tuplebased__Group__7 )
            // InternalCQL.g:3633:2: rule__Window_Tuplebased__Group__6__Impl rule__Window_Tuplebased__Group__7
            {
            pushFollow(FOLLOW_41);
            rule__Window_Tuplebased__Group__6__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group__7();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__6"


    // $ANTLR start "rule__Window_Tuplebased__Group__6__Impl"
    // InternalCQL.g:3640:1: rule__Window_Tuplebased__Group__6__Impl : ( ( rule__Window_Tuplebased__Group_6__0 )? ) ;
    public final void rule__Window_Tuplebased__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3644:1: ( ( ( rule__Window_Tuplebased__Group_6__0 )? ) )
            // InternalCQL.g:3645:1: ( ( rule__Window_Tuplebased__Group_6__0 )? )
            {
            // InternalCQL.g:3645:1: ( ( rule__Window_Tuplebased__Group_6__0 )? )
            // InternalCQL.g:3646:2: ( rule__Window_Tuplebased__Group_6__0 )?
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getGroup_6()); 
            // InternalCQL.g:3647:2: ( rule__Window_Tuplebased__Group_6__0 )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==50) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // InternalCQL.g:3647:3: rule__Window_Tuplebased__Group_6__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Window_Tuplebased__Group_6__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getWindow_TuplebasedAccess().getGroup_6()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__6__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group__7"
    // InternalCQL.g:3655:1: rule__Window_Tuplebased__Group__7 : rule__Window_Tuplebased__Group__7__Impl ;
    public final void rule__Window_Tuplebased__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3659:1: ( rule__Window_Tuplebased__Group__7__Impl )
            // InternalCQL.g:3660:2: rule__Window_Tuplebased__Group__7__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group__7__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__7"


    // $ANTLR start "rule__Window_Tuplebased__Group__7__Impl"
    // InternalCQL.g:3666:1: rule__Window_Tuplebased__Group__7__Impl : ( ']' ) ;
    public final void rule__Window_Tuplebased__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3670:1: ( ( ']' ) )
            // InternalCQL.g:3671:1: ( ']' )
            {
            // InternalCQL.g:3671:1: ( ']' )
            // InternalCQL.g:3672:2: ']'
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getRightSquareBracketKeyword_7()); 
            match(input,46,FOLLOW_2); 
             after(grammarAccess.getWindow_TuplebasedAccess().getRightSquareBracketKeyword_7()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__7__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group_4__0"
    // InternalCQL.g:3682:1: rule__Window_Tuplebased__Group_4__0 : rule__Window_Tuplebased__Group_4__0__Impl rule__Window_Tuplebased__Group_4__1 ;
    public final void rule__Window_Tuplebased__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3686:1: ( rule__Window_Tuplebased__Group_4__0__Impl rule__Window_Tuplebased__Group_4__1 )
            // InternalCQL.g:3687:2: rule__Window_Tuplebased__Group_4__0__Impl rule__Window_Tuplebased__Group_4__1
            {
            pushFollow(FOLLOW_5);
            rule__Window_Tuplebased__Group_4__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group_4__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group_4__0"


    // $ANTLR start "rule__Window_Tuplebased__Group_4__0__Impl"
    // InternalCQL.g:3694:1: rule__Window_Tuplebased__Group_4__0__Impl : ( 'ADVANCE' ) ;
    public final void rule__Window_Tuplebased__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3698:1: ( ( 'ADVANCE' ) )
            // InternalCQL.g:3699:1: ( 'ADVANCE' )
            {
            // InternalCQL.g:3699:1: ( 'ADVANCE' )
            // InternalCQL.g:3700:2: 'ADVANCE'
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getADVANCEKeyword_4_0()); 
            match(input,49,FOLLOW_2); 
             after(grammarAccess.getWindow_TuplebasedAccess().getADVANCEKeyword_4_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group_4__0__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group_4__1"
    // InternalCQL.g:3709:1: rule__Window_Tuplebased__Group_4__1 : rule__Window_Tuplebased__Group_4__1__Impl ;
    public final void rule__Window_Tuplebased__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3713:1: ( rule__Window_Tuplebased__Group_4__1__Impl )
            // InternalCQL.g:3714:2: rule__Window_Tuplebased__Group_4__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group_4__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group_4__1"


    // $ANTLR start "rule__Window_Tuplebased__Group_4__1__Impl"
    // InternalCQL.g:3720:1: rule__Window_Tuplebased__Group_4__1__Impl : ( ( rule__Window_Tuplebased__Advance_sizeAssignment_4_1 ) ) ;
    public final void rule__Window_Tuplebased__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3724:1: ( ( ( rule__Window_Tuplebased__Advance_sizeAssignment_4_1 ) ) )
            // InternalCQL.g:3725:1: ( ( rule__Window_Tuplebased__Advance_sizeAssignment_4_1 ) )
            {
            // InternalCQL.g:3725:1: ( ( rule__Window_Tuplebased__Advance_sizeAssignment_4_1 ) )
            // InternalCQL.g:3726:2: ( rule__Window_Tuplebased__Advance_sizeAssignment_4_1 )
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getAdvance_sizeAssignment_4_1()); 
            // InternalCQL.g:3727:2: ( rule__Window_Tuplebased__Advance_sizeAssignment_4_1 )
            // InternalCQL.g:3727:3: rule__Window_Tuplebased__Advance_sizeAssignment_4_1
            {
            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Advance_sizeAssignment_4_1();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TuplebasedAccess().getAdvance_sizeAssignment_4_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group_4__1__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group_6__0"
    // InternalCQL.g:3736:1: rule__Window_Tuplebased__Group_6__0 : rule__Window_Tuplebased__Group_6__0__Impl rule__Window_Tuplebased__Group_6__1 ;
    public final void rule__Window_Tuplebased__Group_6__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3740:1: ( rule__Window_Tuplebased__Group_6__0__Impl rule__Window_Tuplebased__Group_6__1 )
            // InternalCQL.g:3741:2: rule__Window_Tuplebased__Group_6__0__Impl rule__Window_Tuplebased__Group_6__1
            {
            pushFollow(FOLLOW_42);
            rule__Window_Tuplebased__Group_6__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group_6__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group_6__0"


    // $ANTLR start "rule__Window_Tuplebased__Group_6__0__Impl"
    // InternalCQL.g:3748:1: rule__Window_Tuplebased__Group_6__0__Impl : ( 'PARTITION' ) ;
    public final void rule__Window_Tuplebased__Group_6__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3752:1: ( ( 'PARTITION' ) )
            // InternalCQL.g:3753:1: ( 'PARTITION' )
            {
            // InternalCQL.g:3753:1: ( 'PARTITION' )
            // InternalCQL.g:3754:2: 'PARTITION'
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getPARTITIONKeyword_6_0()); 
            match(input,50,FOLLOW_2); 
             after(grammarAccess.getWindow_TuplebasedAccess().getPARTITIONKeyword_6_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group_6__0__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group_6__1"
    // InternalCQL.g:3763:1: rule__Window_Tuplebased__Group_6__1 : rule__Window_Tuplebased__Group_6__1__Impl rule__Window_Tuplebased__Group_6__2 ;
    public final void rule__Window_Tuplebased__Group_6__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3767:1: ( rule__Window_Tuplebased__Group_6__1__Impl rule__Window_Tuplebased__Group_6__2 )
            // InternalCQL.g:3768:2: rule__Window_Tuplebased__Group_6__1__Impl rule__Window_Tuplebased__Group_6__2
            {
            pushFollow(FOLLOW_9);
            rule__Window_Tuplebased__Group_6__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group_6__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group_6__1"


    // $ANTLR start "rule__Window_Tuplebased__Group_6__1__Impl"
    // InternalCQL.g:3775:1: rule__Window_Tuplebased__Group_6__1__Impl : ( 'BY' ) ;
    public final void rule__Window_Tuplebased__Group_6__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3779:1: ( ( 'BY' ) )
            // InternalCQL.g:3780:1: ( 'BY' )
            {
            // InternalCQL.g:3780:1: ( 'BY' )
            // InternalCQL.g:3781:2: 'BY'
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getBYKeyword_6_1()); 
            match(input,51,FOLLOW_2); 
             after(grammarAccess.getWindow_TuplebasedAccess().getBYKeyword_6_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group_6__1__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group_6__2"
    // InternalCQL.g:3790:1: rule__Window_Tuplebased__Group_6__2 : rule__Window_Tuplebased__Group_6__2__Impl ;
    public final void rule__Window_Tuplebased__Group_6__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3794:1: ( rule__Window_Tuplebased__Group_6__2__Impl )
            // InternalCQL.g:3795:2: rule__Window_Tuplebased__Group_6__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group_6__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group_6__2"


    // $ANTLR start "rule__Window_Tuplebased__Group_6__2__Impl"
    // InternalCQL.g:3801:1: rule__Window_Tuplebased__Group_6__2__Impl : ( ( rule__Window_Tuplebased__Partition_attributeAssignment_6_2 ) ) ;
    public final void rule__Window_Tuplebased__Group_6__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3805:1: ( ( ( rule__Window_Tuplebased__Partition_attributeAssignment_6_2 ) ) )
            // InternalCQL.g:3806:1: ( ( rule__Window_Tuplebased__Partition_attributeAssignment_6_2 ) )
            {
            // InternalCQL.g:3806:1: ( ( rule__Window_Tuplebased__Partition_attributeAssignment_6_2 ) )
            // InternalCQL.g:3807:2: ( rule__Window_Tuplebased__Partition_attributeAssignment_6_2 )
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getPartition_attributeAssignment_6_2()); 
            // InternalCQL.g:3808:2: ( rule__Window_Tuplebased__Partition_attributeAssignment_6_2 )
            // InternalCQL.g:3808:3: rule__Window_Tuplebased__Partition_attributeAssignment_6_2
            {
            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Partition_attributeAssignment_6_2();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TuplebasedAccess().getPartition_attributeAssignment_6_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group_6__2__Impl"


    // $ANTLR start "rule__Create_Statement__Group__0"
    // InternalCQL.g:3817:1: rule__Create_Statement__Group__0 : rule__Create_Statement__Group__0__Impl rule__Create_Statement__Group__1 ;
    public final void rule__Create_Statement__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3821:1: ( rule__Create_Statement__Group__0__Impl rule__Create_Statement__Group__1 )
            // InternalCQL.g:3822:2: rule__Create_Statement__Group__0__Impl rule__Create_Statement__Group__1
            {
            pushFollow(FOLLOW_44);
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
    // InternalCQL.g:3829:1: rule__Create_Statement__Group__0__Impl : ( 'CREATE' ) ;
    public final void rule__Create_Statement__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3833:1: ( ( 'CREATE' ) )
            // InternalCQL.g:3834:1: ( 'CREATE' )
            {
            // InternalCQL.g:3834:1: ( 'CREATE' )
            // InternalCQL.g:3835:2: 'CREATE'
            {
             before(grammarAccess.getCreate_StatementAccess().getCREATEKeyword_0()); 
            match(input,53,FOLLOW_2); 
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
    // InternalCQL.g:3844:1: rule__Create_Statement__Group__1 : rule__Create_Statement__Group__1__Impl rule__Create_Statement__Group__2 ;
    public final void rule__Create_Statement__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3848:1: ( rule__Create_Statement__Group__1__Impl rule__Create_Statement__Group__2 )
            // InternalCQL.g:3849:2: rule__Create_Statement__Group__1__Impl rule__Create_Statement__Group__2
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
    // InternalCQL.g:3856:1: rule__Create_Statement__Group__1__Impl : ( ( rule__Create_Statement__TypeAssignment_1 ) ) ;
    public final void rule__Create_Statement__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3860:1: ( ( ( rule__Create_Statement__TypeAssignment_1 ) ) )
            // InternalCQL.g:3861:1: ( ( rule__Create_Statement__TypeAssignment_1 ) )
            {
            // InternalCQL.g:3861:1: ( ( rule__Create_Statement__TypeAssignment_1 ) )
            // InternalCQL.g:3862:2: ( rule__Create_Statement__TypeAssignment_1 )
            {
             before(grammarAccess.getCreate_StatementAccess().getTypeAssignment_1()); 
            // InternalCQL.g:3863:2: ( rule__Create_Statement__TypeAssignment_1 )
            // InternalCQL.g:3863:3: rule__Create_Statement__TypeAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__Create_Statement__TypeAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StatementAccess().getTypeAssignment_1()); 

            }


            }

        }
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
    // InternalCQL.g:3871:1: rule__Create_Statement__Group__2 : rule__Create_Statement__Group__2__Impl rule__Create_Statement__Group__3 ;
    public final void rule__Create_Statement__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3875:1: ( rule__Create_Statement__Group__2__Impl rule__Create_Statement__Group__3 )
            // InternalCQL.g:3876:2: rule__Create_Statement__Group__2__Impl rule__Create_Statement__Group__3
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
    // InternalCQL.g:3883:1: rule__Create_Statement__Group__2__Impl : ( ( rule__Create_Statement__NameAssignment_2 ) ) ;
    public final void rule__Create_Statement__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3887:1: ( ( ( rule__Create_Statement__NameAssignment_2 ) ) )
            // InternalCQL.g:3888:1: ( ( rule__Create_Statement__NameAssignment_2 ) )
            {
            // InternalCQL.g:3888:1: ( ( rule__Create_Statement__NameAssignment_2 ) )
            // InternalCQL.g:3889:2: ( rule__Create_Statement__NameAssignment_2 )
            {
             before(grammarAccess.getCreate_StatementAccess().getNameAssignment_2()); 
            // InternalCQL.g:3890:2: ( rule__Create_Statement__NameAssignment_2 )
            // InternalCQL.g:3890:3: rule__Create_Statement__NameAssignment_2
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
    // InternalCQL.g:3898:1: rule__Create_Statement__Group__3 : rule__Create_Statement__Group__3__Impl rule__Create_Statement__Group__4 ;
    public final void rule__Create_Statement__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3902:1: ( rule__Create_Statement__Group__3__Impl rule__Create_Statement__Group__4 )
            // InternalCQL.g:3903:2: rule__Create_Statement__Group__3__Impl rule__Create_Statement__Group__4
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
    // InternalCQL.g:3910:1: rule__Create_Statement__Group__3__Impl : ( '(' ) ;
    public final void rule__Create_Statement__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3914:1: ( ( '(' ) )
            // InternalCQL.g:3915:1: ( '(' )
            {
            // InternalCQL.g:3915:1: ( '(' )
            // InternalCQL.g:3916:2: '('
            {
             before(grammarAccess.getCreate_StatementAccess().getLeftParenthesisKeyword_3()); 
            match(input,37,FOLLOW_2); 
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
    // InternalCQL.g:3925:1: rule__Create_Statement__Group__4 : rule__Create_Statement__Group__4__Impl rule__Create_Statement__Group__5 ;
    public final void rule__Create_Statement__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3929:1: ( rule__Create_Statement__Group__4__Impl rule__Create_Statement__Group__5 )
            // InternalCQL.g:3930:2: rule__Create_Statement__Group__4__Impl rule__Create_Statement__Group__5
            {
            pushFollow(FOLLOW_45);
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
    // InternalCQL.g:3937:1: rule__Create_Statement__Group__4__Impl : ( ( ( rule__Create_Statement__AttributesAssignment_4 ) ) ( ( rule__Create_Statement__AttributesAssignment_4 )* ) ) ;
    public final void rule__Create_Statement__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3941:1: ( ( ( ( rule__Create_Statement__AttributesAssignment_4 ) ) ( ( rule__Create_Statement__AttributesAssignment_4 )* ) ) )
            // InternalCQL.g:3942:1: ( ( ( rule__Create_Statement__AttributesAssignment_4 ) ) ( ( rule__Create_Statement__AttributesAssignment_4 )* ) )
            {
            // InternalCQL.g:3942:1: ( ( ( rule__Create_Statement__AttributesAssignment_4 ) ) ( ( rule__Create_Statement__AttributesAssignment_4 )* ) )
            // InternalCQL.g:3943:2: ( ( rule__Create_Statement__AttributesAssignment_4 ) ) ( ( rule__Create_Statement__AttributesAssignment_4 )* )
            {
            // InternalCQL.g:3943:2: ( ( rule__Create_Statement__AttributesAssignment_4 ) )
            // InternalCQL.g:3944:3: ( rule__Create_Statement__AttributesAssignment_4 )
            {
             before(grammarAccess.getCreate_StatementAccess().getAttributesAssignment_4()); 
            // InternalCQL.g:3945:3: ( rule__Create_Statement__AttributesAssignment_4 )
            // InternalCQL.g:3945:4: rule__Create_Statement__AttributesAssignment_4
            {
            pushFollow(FOLLOW_35);
            rule__Create_Statement__AttributesAssignment_4();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StatementAccess().getAttributesAssignment_4()); 

            }

            // InternalCQL.g:3948:2: ( ( rule__Create_Statement__AttributesAssignment_4 )* )
            // InternalCQL.g:3949:3: ( rule__Create_Statement__AttributesAssignment_4 )*
            {
             before(grammarAccess.getCreate_StatementAccess().getAttributesAssignment_4()); 
            // InternalCQL.g:3950:3: ( rule__Create_Statement__AttributesAssignment_4 )*
            loop35:
            do {
                int alt35=2;
                int LA35_0 = input.LA(1);

                if ( (LA35_0==RULE_ID) ) {
                    alt35=1;
                }


                switch (alt35) {
            	case 1 :
            	    // InternalCQL.g:3950:4: rule__Create_Statement__AttributesAssignment_4
            	    {
            	    pushFollow(FOLLOW_35);
            	    rule__Create_Statement__AttributesAssignment_4();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop35;
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
    // InternalCQL.g:3959:1: rule__Create_Statement__Group__5 : rule__Create_Statement__Group__5__Impl rule__Create_Statement__Group__6 ;
    public final void rule__Create_Statement__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3963:1: ( rule__Create_Statement__Group__5__Impl rule__Create_Statement__Group__6 )
            // InternalCQL.g:3964:2: rule__Create_Statement__Group__5__Impl rule__Create_Statement__Group__6
            {
            pushFollow(FOLLOW_46);
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
    // InternalCQL.g:3971:1: rule__Create_Statement__Group__5__Impl : ( ( ( rule__Create_Statement__DatatypesAssignment_5 ) ) ( ( rule__Create_Statement__DatatypesAssignment_5 )* ) ) ;
    public final void rule__Create_Statement__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3975:1: ( ( ( ( rule__Create_Statement__DatatypesAssignment_5 ) ) ( ( rule__Create_Statement__DatatypesAssignment_5 )* ) ) )
            // InternalCQL.g:3976:1: ( ( ( rule__Create_Statement__DatatypesAssignment_5 ) ) ( ( rule__Create_Statement__DatatypesAssignment_5 )* ) )
            {
            // InternalCQL.g:3976:1: ( ( ( rule__Create_Statement__DatatypesAssignment_5 ) ) ( ( rule__Create_Statement__DatatypesAssignment_5 )* ) )
            // InternalCQL.g:3977:2: ( ( rule__Create_Statement__DatatypesAssignment_5 ) ) ( ( rule__Create_Statement__DatatypesAssignment_5 )* )
            {
            // InternalCQL.g:3977:2: ( ( rule__Create_Statement__DatatypesAssignment_5 ) )
            // InternalCQL.g:3978:3: ( rule__Create_Statement__DatatypesAssignment_5 )
            {
             before(grammarAccess.getCreate_StatementAccess().getDatatypesAssignment_5()); 
            // InternalCQL.g:3979:3: ( rule__Create_Statement__DatatypesAssignment_5 )
            // InternalCQL.g:3979:4: rule__Create_Statement__DatatypesAssignment_5
            {
            pushFollow(FOLLOW_47);
            rule__Create_Statement__DatatypesAssignment_5();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StatementAccess().getDatatypesAssignment_5()); 

            }

            // InternalCQL.g:3982:2: ( ( rule__Create_Statement__DatatypesAssignment_5 )* )
            // InternalCQL.g:3983:3: ( rule__Create_Statement__DatatypesAssignment_5 )*
            {
             before(grammarAccess.getCreate_StatementAccess().getDatatypesAssignment_5()); 
            // InternalCQL.g:3984:3: ( rule__Create_Statement__DatatypesAssignment_5 )*
            loop36:
            do {
                int alt36=2;
                int LA36_0 = input.LA(1);

                if ( ((LA36_0>=14 && LA36_0<=20)) ) {
                    alt36=1;
                }


                switch (alt36) {
            	case 1 :
            	    // InternalCQL.g:3984:4: rule__Create_Statement__DatatypesAssignment_5
            	    {
            	    pushFollow(FOLLOW_47);
            	    rule__Create_Statement__DatatypesAssignment_5();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop36;
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
    // InternalCQL.g:3993:1: rule__Create_Statement__Group__6 : rule__Create_Statement__Group__6__Impl rule__Create_Statement__Group__7 ;
    public final void rule__Create_Statement__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3997:1: ( rule__Create_Statement__Group__6__Impl rule__Create_Statement__Group__7 )
            // InternalCQL.g:3998:2: rule__Create_Statement__Group__6__Impl rule__Create_Statement__Group__7
            {
            pushFollow(FOLLOW_46);
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
    // InternalCQL.g:4005:1: rule__Create_Statement__Group__6__Impl : ( ( rule__Create_Statement__Group_6__0 )* ) ;
    public final void rule__Create_Statement__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4009:1: ( ( ( rule__Create_Statement__Group_6__0 )* ) )
            // InternalCQL.g:4010:1: ( ( rule__Create_Statement__Group_6__0 )* )
            {
            // InternalCQL.g:4010:1: ( ( rule__Create_Statement__Group_6__0 )* )
            // InternalCQL.g:4011:2: ( rule__Create_Statement__Group_6__0 )*
            {
             before(grammarAccess.getCreate_StatementAccess().getGroup_6()); 
            // InternalCQL.g:4012:2: ( rule__Create_Statement__Group_6__0 )*
            loop37:
            do {
                int alt37=2;
                int LA37_0 = input.LA(1);

                if ( (LA37_0==42) ) {
                    alt37=1;
                }


                switch (alt37) {
            	case 1 :
            	    // InternalCQL.g:4012:3: rule__Create_Statement__Group_6__0
            	    {
            	    pushFollow(FOLLOW_33);
            	    rule__Create_Statement__Group_6__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop37;
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
    // InternalCQL.g:4020:1: rule__Create_Statement__Group__7 : rule__Create_Statement__Group__7__Impl rule__Create_Statement__Group__8 ;
    public final void rule__Create_Statement__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4024:1: ( rule__Create_Statement__Group__7__Impl rule__Create_Statement__Group__8 )
            // InternalCQL.g:4025:2: rule__Create_Statement__Group__7__Impl rule__Create_Statement__Group__8
            {
            pushFollow(FOLLOW_48);
            rule__Create_Statement__Group__7__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group__8();

            state._fsp--;


            }

        }
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
    // InternalCQL.g:4032:1: rule__Create_Statement__Group__7__Impl : ( ')' ) ;
    public final void rule__Create_Statement__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4036:1: ( ( ')' ) )
            // InternalCQL.g:4037:1: ( ')' )
            {
            // InternalCQL.g:4037:1: ( ')' )
            // InternalCQL.g:4038:2: ')'
            {
             before(grammarAccess.getCreate_StatementAccess().getRightParenthesisKeyword_7()); 
            match(input,38,FOLLOW_2); 
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


    // $ANTLR start "rule__Create_Statement__Group__8"
    // InternalCQL.g:4047:1: rule__Create_Statement__Group__8 : rule__Create_Statement__Group__8__Impl rule__Create_Statement__Group__9 ;
    public final void rule__Create_Statement__Group__8() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4051:1: ( rule__Create_Statement__Group__8__Impl rule__Create_Statement__Group__9 )
            // InternalCQL.g:4052:2: rule__Create_Statement__Group__8__Impl rule__Create_Statement__Group__9
            {
            pushFollow(FOLLOW_9);
            rule__Create_Statement__Group__8__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group__9();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__8"


    // $ANTLR start "rule__Create_Statement__Group__8__Impl"
    // InternalCQL.g:4059:1: rule__Create_Statement__Group__8__Impl : ( 'CHANNEL' ) ;
    public final void rule__Create_Statement__Group__8__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4063:1: ( ( 'CHANNEL' ) )
            // InternalCQL.g:4064:1: ( 'CHANNEL' )
            {
            // InternalCQL.g:4064:1: ( 'CHANNEL' )
            // InternalCQL.g:4065:2: 'CHANNEL'
            {
             before(grammarAccess.getCreate_StatementAccess().getCHANNELKeyword_8()); 
            match(input,54,FOLLOW_2); 
             after(grammarAccess.getCreate_StatementAccess().getCHANNELKeyword_8()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__8__Impl"


    // $ANTLR start "rule__Create_Statement__Group__9"
    // InternalCQL.g:4074:1: rule__Create_Statement__Group__9 : rule__Create_Statement__Group__9__Impl rule__Create_Statement__Group__10 ;
    public final void rule__Create_Statement__Group__9() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4078:1: ( rule__Create_Statement__Group__9__Impl rule__Create_Statement__Group__10 )
            // InternalCQL.g:4079:2: rule__Create_Statement__Group__9__Impl rule__Create_Statement__Group__10
            {
            pushFollow(FOLLOW_49);
            rule__Create_Statement__Group__9__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group__10();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__9"


    // $ANTLR start "rule__Create_Statement__Group__9__Impl"
    // InternalCQL.g:4086:1: rule__Create_Statement__Group__9__Impl : ( ( rule__Create_Statement__HostAssignment_9 ) ) ;
    public final void rule__Create_Statement__Group__9__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4090:1: ( ( ( rule__Create_Statement__HostAssignment_9 ) ) )
            // InternalCQL.g:4091:1: ( ( rule__Create_Statement__HostAssignment_9 ) )
            {
            // InternalCQL.g:4091:1: ( ( rule__Create_Statement__HostAssignment_9 ) )
            // InternalCQL.g:4092:2: ( rule__Create_Statement__HostAssignment_9 )
            {
             before(grammarAccess.getCreate_StatementAccess().getHostAssignment_9()); 
            // InternalCQL.g:4093:2: ( rule__Create_Statement__HostAssignment_9 )
            // InternalCQL.g:4093:3: rule__Create_Statement__HostAssignment_9
            {
            pushFollow(FOLLOW_2);
            rule__Create_Statement__HostAssignment_9();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StatementAccess().getHostAssignment_9()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__9__Impl"


    // $ANTLR start "rule__Create_Statement__Group__10"
    // InternalCQL.g:4101:1: rule__Create_Statement__Group__10 : rule__Create_Statement__Group__10__Impl rule__Create_Statement__Group__11 ;
    public final void rule__Create_Statement__Group__10() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4105:1: ( rule__Create_Statement__Group__10__Impl rule__Create_Statement__Group__11 )
            // InternalCQL.g:4106:2: rule__Create_Statement__Group__10__Impl rule__Create_Statement__Group__11
            {
            pushFollow(FOLLOW_5);
            rule__Create_Statement__Group__10__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group__11();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__10"


    // $ANTLR start "rule__Create_Statement__Group__10__Impl"
    // InternalCQL.g:4113:1: rule__Create_Statement__Group__10__Impl : ( ':' ) ;
    public final void rule__Create_Statement__Group__10__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4117:1: ( ( ':' ) )
            // InternalCQL.g:4118:1: ( ':' )
            {
            // InternalCQL.g:4118:1: ( ':' )
            // InternalCQL.g:4119:2: ':'
            {
             before(grammarAccess.getCreate_StatementAccess().getColonKeyword_10()); 
            match(input,55,FOLLOW_2); 
             after(grammarAccess.getCreate_StatementAccess().getColonKeyword_10()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__10__Impl"


    // $ANTLR start "rule__Create_Statement__Group__11"
    // InternalCQL.g:4128:1: rule__Create_Statement__Group__11 : rule__Create_Statement__Group__11__Impl ;
    public final void rule__Create_Statement__Group__11() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4132:1: ( rule__Create_Statement__Group__11__Impl )
            // InternalCQL.g:4133:2: rule__Create_Statement__Group__11__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group__11__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__11"


    // $ANTLR start "rule__Create_Statement__Group__11__Impl"
    // InternalCQL.g:4139:1: rule__Create_Statement__Group__11__Impl : ( ( rule__Create_Statement__PortAssignment_11 ) ) ;
    public final void rule__Create_Statement__Group__11__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4143:1: ( ( ( rule__Create_Statement__PortAssignment_11 ) ) )
            // InternalCQL.g:4144:1: ( ( rule__Create_Statement__PortAssignment_11 ) )
            {
            // InternalCQL.g:4144:1: ( ( rule__Create_Statement__PortAssignment_11 ) )
            // InternalCQL.g:4145:2: ( rule__Create_Statement__PortAssignment_11 )
            {
             before(grammarAccess.getCreate_StatementAccess().getPortAssignment_11()); 
            // InternalCQL.g:4146:2: ( rule__Create_Statement__PortAssignment_11 )
            // InternalCQL.g:4146:3: rule__Create_Statement__PortAssignment_11
            {
            pushFollow(FOLLOW_2);
            rule__Create_Statement__PortAssignment_11();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StatementAccess().getPortAssignment_11()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__11__Impl"


    // $ANTLR start "rule__Create_Statement__Group_6__0"
    // InternalCQL.g:4155:1: rule__Create_Statement__Group_6__0 : rule__Create_Statement__Group_6__0__Impl rule__Create_Statement__Group_6__1 ;
    public final void rule__Create_Statement__Group_6__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4159:1: ( rule__Create_Statement__Group_6__0__Impl rule__Create_Statement__Group_6__1 )
            // InternalCQL.g:4160:2: rule__Create_Statement__Group_6__0__Impl rule__Create_Statement__Group_6__1
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
    // InternalCQL.g:4167:1: rule__Create_Statement__Group_6__0__Impl : ( ',' ) ;
    public final void rule__Create_Statement__Group_6__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4171:1: ( ( ',' ) )
            // InternalCQL.g:4172:1: ( ',' )
            {
            // InternalCQL.g:4172:1: ( ',' )
            // InternalCQL.g:4173:2: ','
            {
             before(grammarAccess.getCreate_StatementAccess().getCommaKeyword_6_0()); 
            match(input,42,FOLLOW_2); 
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
    // InternalCQL.g:4182:1: rule__Create_Statement__Group_6__1 : rule__Create_Statement__Group_6__1__Impl rule__Create_Statement__Group_6__2 ;
    public final void rule__Create_Statement__Group_6__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4186:1: ( rule__Create_Statement__Group_6__1__Impl rule__Create_Statement__Group_6__2 )
            // InternalCQL.g:4187:2: rule__Create_Statement__Group_6__1__Impl rule__Create_Statement__Group_6__2
            {
            pushFollow(FOLLOW_45);
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
    // InternalCQL.g:4194:1: rule__Create_Statement__Group_6__1__Impl : ( ( rule__Create_Statement__AttributesAssignment_6_1 ) ) ;
    public final void rule__Create_Statement__Group_6__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4198:1: ( ( ( rule__Create_Statement__AttributesAssignment_6_1 ) ) )
            // InternalCQL.g:4199:1: ( ( rule__Create_Statement__AttributesAssignment_6_1 ) )
            {
            // InternalCQL.g:4199:1: ( ( rule__Create_Statement__AttributesAssignment_6_1 ) )
            // InternalCQL.g:4200:2: ( rule__Create_Statement__AttributesAssignment_6_1 )
            {
             before(grammarAccess.getCreate_StatementAccess().getAttributesAssignment_6_1()); 
            // InternalCQL.g:4201:2: ( rule__Create_Statement__AttributesAssignment_6_1 )
            // InternalCQL.g:4201:3: rule__Create_Statement__AttributesAssignment_6_1
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
    // InternalCQL.g:4209:1: rule__Create_Statement__Group_6__2 : rule__Create_Statement__Group_6__2__Impl ;
    public final void rule__Create_Statement__Group_6__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4213:1: ( rule__Create_Statement__Group_6__2__Impl )
            // InternalCQL.g:4214:2: rule__Create_Statement__Group_6__2__Impl
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
    // InternalCQL.g:4220:1: rule__Create_Statement__Group_6__2__Impl : ( ( rule__Create_Statement__DatatypesAssignment_6_2 ) ) ;
    public final void rule__Create_Statement__Group_6__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4224:1: ( ( ( rule__Create_Statement__DatatypesAssignment_6_2 ) ) )
            // InternalCQL.g:4225:1: ( ( rule__Create_Statement__DatatypesAssignment_6_2 ) )
            {
            // InternalCQL.g:4225:1: ( ( rule__Create_Statement__DatatypesAssignment_6_2 ) )
            // InternalCQL.g:4226:2: ( rule__Create_Statement__DatatypesAssignment_6_2 )
            {
             before(grammarAccess.getCreate_StatementAccess().getDatatypesAssignment_6_2()); 
            // InternalCQL.g:4227:2: ( rule__Create_Statement__DatatypesAssignment_6_2 )
            // InternalCQL.g:4227:3: rule__Create_Statement__DatatypesAssignment_6_2
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
    // InternalCQL.g:4236:1: rule__Model__StatementsAssignment : ( ruleStatement ) ;
    public final void rule__Model__StatementsAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4240:1: ( ( ruleStatement ) )
            // InternalCQL.g:4241:2: ( ruleStatement )
            {
            // InternalCQL.g:4241:2: ( ruleStatement )
            // InternalCQL.g:4242:3: ruleStatement
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
    // InternalCQL.g:4251:1: rule__Statement__TypeAssignment_0_0 : ( ruleSelect_Statement ) ;
    public final void rule__Statement__TypeAssignment_0_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4255:1: ( ( ruleSelect_Statement ) )
            // InternalCQL.g:4256:2: ( ruleSelect_Statement )
            {
            // InternalCQL.g:4256:2: ( ruleSelect_Statement )
            // InternalCQL.g:4257:3: ruleSelect_Statement
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
    // InternalCQL.g:4266:1: rule__Statement__TypeAssignment_0_1 : ( ruleCreate_Statement ) ;
    public final void rule__Statement__TypeAssignment_0_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4270:1: ( ( ruleCreate_Statement ) )
            // InternalCQL.g:4271:2: ( ruleCreate_Statement )
            {
            // InternalCQL.g:4271:2: ( ruleCreate_Statement )
            // InternalCQL.g:4272:3: ruleCreate_Statement
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
    // InternalCQL.g:4281:1: rule__Atomic__ValueAssignment_0_1 : ( RULE_INT ) ;
    public final void rule__Atomic__ValueAssignment_0_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4285:1: ( ( RULE_INT ) )
            // InternalCQL.g:4286:2: ( RULE_INT )
            {
            // InternalCQL.g:4286:2: ( RULE_INT )
            // InternalCQL.g:4287:3: RULE_INT
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
    // InternalCQL.g:4296:1: rule__Atomic__ValueAssignment_1_1 : ( RULE_FLOAT_NUMBER ) ;
    public final void rule__Atomic__ValueAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4300:1: ( ( RULE_FLOAT_NUMBER ) )
            // InternalCQL.g:4301:2: ( RULE_FLOAT_NUMBER )
            {
            // InternalCQL.g:4301:2: ( RULE_FLOAT_NUMBER )
            // InternalCQL.g:4302:3: RULE_FLOAT_NUMBER
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
    // InternalCQL.g:4311:1: rule__Atomic__ValueAssignment_2_1 : ( RULE_STRING ) ;
    public final void rule__Atomic__ValueAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4315:1: ( ( RULE_STRING ) )
            // InternalCQL.g:4316:2: ( RULE_STRING )
            {
            // InternalCQL.g:4316:2: ( RULE_STRING )
            // InternalCQL.g:4317:3: RULE_STRING
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
    // InternalCQL.g:4326:1: rule__Atomic__ValueAssignment_3_1 : ( ( rule__Atomic__ValueAlternatives_3_1_0 ) ) ;
    public final void rule__Atomic__ValueAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4330:1: ( ( ( rule__Atomic__ValueAlternatives_3_1_0 ) ) )
            // InternalCQL.g:4331:2: ( ( rule__Atomic__ValueAlternatives_3_1_0 ) )
            {
            // InternalCQL.g:4331:2: ( ( rule__Atomic__ValueAlternatives_3_1_0 ) )
            // InternalCQL.g:4332:3: ( rule__Atomic__ValueAlternatives_3_1_0 )
            {
             before(grammarAccess.getAtomicAccess().getValueAlternatives_3_1_0()); 
            // InternalCQL.g:4333:3: ( rule__Atomic__ValueAlternatives_3_1_0 )
            // InternalCQL.g:4333:4: rule__Atomic__ValueAlternatives_3_1_0
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
    // InternalCQL.g:4341:1: rule__Atomic__ValueAssignment_4_1 : ( ruleAttribute ) ;
    public final void rule__Atomic__ValueAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4345:1: ( ( ruleAttribute ) )
            // InternalCQL.g:4346:2: ( ruleAttribute )
            {
            // InternalCQL.g:4346:2: ( ruleAttribute )
            // InternalCQL.g:4347:3: ruleAttribute
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
    // InternalCQL.g:4356:1: rule__Source__NameAssignment : ( RULE_ID ) ;
    public final void rule__Source__NameAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4360:1: ( ( RULE_ID ) )
            // InternalCQL.g:4361:2: ( RULE_ID )
            {
            // InternalCQL.g:4361:2: ( RULE_ID )
            // InternalCQL.g:4362:3: RULE_ID
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
    // InternalCQL.g:4371:1: rule__Attribute__NameAssignment : ( RULE_ID ) ;
    public final void rule__Attribute__NameAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4375:1: ( ( RULE_ID ) )
            // InternalCQL.g:4376:2: ( RULE_ID )
            {
            // InternalCQL.g:4376:2: ( RULE_ID )
            // InternalCQL.g:4377:3: RULE_ID
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
    // InternalCQL.g:4386:1: rule__ExpressionsModel__ElementsAssignment_1 : ( ruleExpression ) ;
    public final void rule__ExpressionsModel__ElementsAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4390:1: ( ( ruleExpression ) )
            // InternalCQL.g:4391:2: ( ruleExpression )
            {
            // InternalCQL.g:4391:2: ( ruleExpression )
            // InternalCQL.g:4392:3: ruleExpression
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
    // InternalCQL.g:4401:1: rule__Or__RightAssignment_1_2 : ( ruleAnd ) ;
    public final void rule__Or__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4405:1: ( ( ruleAnd ) )
            // InternalCQL.g:4406:2: ( ruleAnd )
            {
            // InternalCQL.g:4406:2: ( ruleAnd )
            // InternalCQL.g:4407:3: ruleAnd
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
    // InternalCQL.g:4416:1: rule__And__RightAssignment_1_2 : ( ruleEqualitiy ) ;
    public final void rule__And__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4420:1: ( ( ruleEqualitiy ) )
            // InternalCQL.g:4421:2: ( ruleEqualitiy )
            {
            // InternalCQL.g:4421:2: ( ruleEqualitiy )
            // InternalCQL.g:4422:3: ruleEqualitiy
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
    // InternalCQL.g:4431:1: rule__Equalitiy__OpAssignment_1_1 : ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) ) ;
    public final void rule__Equalitiy__OpAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4435:1: ( ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) ) )
            // InternalCQL.g:4436:2: ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) )
            {
            // InternalCQL.g:4436:2: ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) )
            // InternalCQL.g:4437:3: ( rule__Equalitiy__OpAlternatives_1_1_0 )
            {
             before(grammarAccess.getEqualitiyAccess().getOpAlternatives_1_1_0()); 
            // InternalCQL.g:4438:3: ( rule__Equalitiy__OpAlternatives_1_1_0 )
            // InternalCQL.g:4438:4: rule__Equalitiy__OpAlternatives_1_1_0
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
    // InternalCQL.g:4446:1: rule__Equalitiy__RightAssignment_1_2 : ( ruleComparison ) ;
    public final void rule__Equalitiy__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4450:1: ( ( ruleComparison ) )
            // InternalCQL.g:4451:2: ( ruleComparison )
            {
            // InternalCQL.g:4451:2: ( ruleComparison )
            // InternalCQL.g:4452:3: ruleComparison
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
    // InternalCQL.g:4461:1: rule__Comparison__OpAssignment_1_1 : ( ( rule__Comparison__OpAlternatives_1_1_0 ) ) ;
    public final void rule__Comparison__OpAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4465:1: ( ( ( rule__Comparison__OpAlternatives_1_1_0 ) ) )
            // InternalCQL.g:4466:2: ( ( rule__Comparison__OpAlternatives_1_1_0 ) )
            {
            // InternalCQL.g:4466:2: ( ( rule__Comparison__OpAlternatives_1_1_0 ) )
            // InternalCQL.g:4467:3: ( rule__Comparison__OpAlternatives_1_1_0 )
            {
             before(grammarAccess.getComparisonAccess().getOpAlternatives_1_1_0()); 
            // InternalCQL.g:4468:3: ( rule__Comparison__OpAlternatives_1_1_0 )
            // InternalCQL.g:4468:4: rule__Comparison__OpAlternatives_1_1_0
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
    // InternalCQL.g:4476:1: rule__Comparison__RightAssignment_1_2 : ( rulePlusOrMinus ) ;
    public final void rule__Comparison__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4480:1: ( ( rulePlusOrMinus ) )
            // InternalCQL.g:4481:2: ( rulePlusOrMinus )
            {
            // InternalCQL.g:4481:2: ( rulePlusOrMinus )
            // InternalCQL.g:4482:3: rulePlusOrMinus
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
    // InternalCQL.g:4491:1: rule__PlusOrMinus__RightAssignment_1_1 : ( ruleMulOrDiv ) ;
    public final void rule__PlusOrMinus__RightAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4495:1: ( ( ruleMulOrDiv ) )
            // InternalCQL.g:4496:2: ( ruleMulOrDiv )
            {
            // InternalCQL.g:4496:2: ( ruleMulOrDiv )
            // InternalCQL.g:4497:3: ruleMulOrDiv
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
    // InternalCQL.g:4506:1: rule__MulOrDiv__OpAssignment_1_1 : ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) ) ;
    public final void rule__MulOrDiv__OpAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4510:1: ( ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) ) )
            // InternalCQL.g:4511:2: ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) )
            {
            // InternalCQL.g:4511:2: ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) )
            // InternalCQL.g:4512:3: ( rule__MulOrDiv__OpAlternatives_1_1_0 )
            {
             before(grammarAccess.getMulOrDivAccess().getOpAlternatives_1_1_0()); 
            // InternalCQL.g:4513:3: ( rule__MulOrDiv__OpAlternatives_1_1_0 )
            // InternalCQL.g:4513:4: rule__MulOrDiv__OpAlternatives_1_1_0
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
    // InternalCQL.g:4521:1: rule__MulOrDiv__RightAssignment_1_2 : ( rulePrimary ) ;
    public final void rule__MulOrDiv__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4525:1: ( ( rulePrimary ) )
            // InternalCQL.g:4526:2: ( rulePrimary )
            {
            // InternalCQL.g:4526:2: ( rulePrimary )
            // InternalCQL.g:4527:3: rulePrimary
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
    // InternalCQL.g:4536:1: rule__Primary__InnerAssignment_0_2 : ( ruleExpression ) ;
    public final void rule__Primary__InnerAssignment_0_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4540:1: ( ( ruleExpression ) )
            // InternalCQL.g:4541:2: ( ruleExpression )
            {
            // InternalCQL.g:4541:2: ( ruleExpression )
            // InternalCQL.g:4542:3: ruleExpression
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
    // InternalCQL.g:4551:1: rule__Primary__ExpressionAssignment_1_2 : ( rulePrimary ) ;
    public final void rule__Primary__ExpressionAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4555:1: ( ( rulePrimary ) )
            // InternalCQL.g:4556:2: ( rulePrimary )
            {
            // InternalCQL.g:4556:2: ( rulePrimary )
            // InternalCQL.g:4557:3: rulePrimary
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
    // InternalCQL.g:4566:1: rule__Select_Statement__NameAssignment_0 : ( ( 'SELECT' ) ) ;
    public final void rule__Select_Statement__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4570:1: ( ( ( 'SELECT' ) ) )
            // InternalCQL.g:4571:2: ( ( 'SELECT' ) )
            {
            // InternalCQL.g:4571:2: ( ( 'SELECT' ) )
            // InternalCQL.g:4572:3: ( 'SELECT' )
            {
             before(grammarAccess.getSelect_StatementAccess().getNameSELECTKeyword_0_0()); 
            // InternalCQL.g:4573:3: ( 'SELECT' )
            // InternalCQL.g:4574:4: 'SELECT'
            {
             before(grammarAccess.getSelect_StatementAccess().getNameSELECTKeyword_0_0()); 
            match(input,56,FOLLOW_2); 
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
    // InternalCQL.g:4585:1: rule__Select_Statement__AttributesAssignment_2_1_0 : ( ruleAttribute ) ;
    public final void rule__Select_Statement__AttributesAssignment_2_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4589:1: ( ( ruleAttribute ) )
            // InternalCQL.g:4590:2: ( ruleAttribute )
            {
            // InternalCQL.g:4590:2: ( ruleAttribute )
            // InternalCQL.g:4591:3: ruleAttribute
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
    // InternalCQL.g:4600:1: rule__Select_Statement__AttributesAssignment_2_1_1_1 : ( ruleAttribute ) ;
    public final void rule__Select_Statement__AttributesAssignment_2_1_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4604:1: ( ( ruleAttribute ) )
            // InternalCQL.g:4605:2: ( ruleAttribute )
            {
            // InternalCQL.g:4605:2: ( ruleAttribute )
            // InternalCQL.g:4606:3: ruleAttribute
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
    // InternalCQL.g:4615:1: rule__Select_Statement__SourcesAssignment_4_0 : ( ruleSource ) ;
    public final void rule__Select_Statement__SourcesAssignment_4_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4619:1: ( ( ruleSource ) )
            // InternalCQL.g:4620:2: ( ruleSource )
            {
            // InternalCQL.g:4620:2: ( ruleSource )
            // InternalCQL.g:4621:3: ruleSource
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


    // $ANTLR start "rule__Select_Statement__WindowsAssignment_4_1"
    // InternalCQL.g:4630:1: rule__Select_Statement__WindowsAssignment_4_1 : ( ruleWindow ) ;
    public final void rule__Select_Statement__WindowsAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4634:1: ( ( ruleWindow ) )
            // InternalCQL.g:4635:2: ( ruleWindow )
            {
            // InternalCQL.g:4635:2: ( ruleWindow )
            // InternalCQL.g:4636:3: ruleWindow
            {
             before(grammarAccess.getSelect_StatementAccess().getWindowsWindowParserRuleCall_4_1_0()); 
            pushFollow(FOLLOW_2);
            ruleWindow();

            state._fsp--;

             after(grammarAccess.getSelect_StatementAccess().getWindowsWindowParserRuleCall_4_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__WindowsAssignment_4_1"


    // $ANTLR start "rule__Select_Statement__SourcesAssignment_4_2_1"
    // InternalCQL.g:4645:1: rule__Select_Statement__SourcesAssignment_4_2_1 : ( ruleSource ) ;
    public final void rule__Select_Statement__SourcesAssignment_4_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4649:1: ( ( ruleSource ) )
            // InternalCQL.g:4650:2: ( ruleSource )
            {
            // InternalCQL.g:4650:2: ( ruleSource )
            // InternalCQL.g:4651:3: ruleSource
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesSourceParserRuleCall_4_2_1_0()); 
            pushFollow(FOLLOW_2);
            ruleSource();

            state._fsp--;

             after(grammarAccess.getSelect_StatementAccess().getSourcesSourceParserRuleCall_4_2_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__SourcesAssignment_4_2_1"


    // $ANTLR start "rule__Select_Statement__WindowsAssignment_4_2_2"
    // InternalCQL.g:4660:1: rule__Select_Statement__WindowsAssignment_4_2_2 : ( ruleWindow ) ;
    public final void rule__Select_Statement__WindowsAssignment_4_2_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4664:1: ( ( ruleWindow ) )
            // InternalCQL.g:4665:2: ( ruleWindow )
            {
            // InternalCQL.g:4665:2: ( ruleWindow )
            // InternalCQL.g:4666:3: ruleWindow
            {
             before(grammarAccess.getSelect_StatementAccess().getWindowsWindowParserRuleCall_4_2_2_0()); 
            pushFollow(FOLLOW_2);
            ruleWindow();

            state._fsp--;

             after(grammarAccess.getSelect_StatementAccess().getWindowsWindowParserRuleCall_4_2_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__WindowsAssignment_4_2_2"


    // $ANTLR start "rule__Select_Statement__PredicatesAssignment_5_1"
    // InternalCQL.g:4675:1: rule__Select_Statement__PredicatesAssignment_5_1 : ( ruleExpressionsModel ) ;
    public final void rule__Select_Statement__PredicatesAssignment_5_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4679:1: ( ( ruleExpressionsModel ) )
            // InternalCQL.g:4680:2: ( ruleExpressionsModel )
            {
            // InternalCQL.g:4680:2: ( ruleExpressionsModel )
            // InternalCQL.g:4681:3: ruleExpressionsModel
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


    // $ANTLR start "rule__Window__TypeAssignment_1_0"
    // InternalCQL.g:4690:1: rule__Window__TypeAssignment_1_0 : ( ruleWindow_Unbounded ) ;
    public final void rule__Window__TypeAssignment_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4694:1: ( ( ruleWindow_Unbounded ) )
            // InternalCQL.g:4695:2: ( ruleWindow_Unbounded )
            {
            // InternalCQL.g:4695:2: ( ruleWindow_Unbounded )
            // InternalCQL.g:4696:3: ruleWindow_Unbounded
            {
             before(grammarAccess.getWindowAccess().getTypeWindow_UnboundedParserRuleCall_1_0_0()); 
            pushFollow(FOLLOW_2);
            ruleWindow_Unbounded();

            state._fsp--;

             after(grammarAccess.getWindowAccess().getTypeWindow_UnboundedParserRuleCall_1_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window__TypeAssignment_1_0"


    // $ANTLR start "rule__Window__TypeAssignment_1_1"
    // InternalCQL.g:4705:1: rule__Window__TypeAssignment_1_1 : ( ruleWindow_Timebased ) ;
    public final void rule__Window__TypeAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4709:1: ( ( ruleWindow_Timebased ) )
            // InternalCQL.g:4710:2: ( ruleWindow_Timebased )
            {
            // InternalCQL.g:4710:2: ( ruleWindow_Timebased )
            // InternalCQL.g:4711:3: ruleWindow_Timebased
            {
             before(grammarAccess.getWindowAccess().getTypeWindow_TimebasedParserRuleCall_1_1_0()); 
            pushFollow(FOLLOW_2);
            ruleWindow_Timebased();

            state._fsp--;

             after(grammarAccess.getWindowAccess().getTypeWindow_TimebasedParserRuleCall_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window__TypeAssignment_1_1"


    // $ANTLR start "rule__Window__TypeAssignment_1_2"
    // InternalCQL.g:4720:1: rule__Window__TypeAssignment_1_2 : ( ruleWindow_Tuplebased ) ;
    public final void rule__Window__TypeAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4724:1: ( ( ruleWindow_Tuplebased ) )
            // InternalCQL.g:4725:2: ( ruleWindow_Tuplebased )
            {
            // InternalCQL.g:4725:2: ( ruleWindow_Tuplebased )
            // InternalCQL.g:4726:3: ruleWindow_Tuplebased
            {
             before(grammarAccess.getWindowAccess().getTypeWindow_TuplebasedParserRuleCall_1_2_0()); 
            pushFollow(FOLLOW_2);
            ruleWindow_Tuplebased();

            state._fsp--;

             after(grammarAccess.getWindowAccess().getTypeWindow_TuplebasedParserRuleCall_1_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window__TypeAssignment_1_2"


    // $ANTLR start "rule__Window_Timebased__SizeAssignment_3"
    // InternalCQL.g:4735:1: rule__Window_Timebased__SizeAssignment_3 : ( RULE_INT ) ;
    public final void rule__Window_Timebased__SizeAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4739:1: ( ( RULE_INT ) )
            // InternalCQL.g:4740:2: ( RULE_INT )
            {
            // InternalCQL.g:4740:2: ( RULE_INT )
            // InternalCQL.g:4741:3: RULE_INT
            {
             before(grammarAccess.getWindow_TimebasedAccess().getSizeINTTerminalRuleCall_3_0()); 
            match(input,RULE_INT,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getSizeINTTerminalRuleCall_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__SizeAssignment_3"


    // $ANTLR start "rule__Window_Timebased__UnitAssignment_4"
    // InternalCQL.g:4750:1: rule__Window_Timebased__UnitAssignment_4 : ( RULE_STRING ) ;
    public final void rule__Window_Timebased__UnitAssignment_4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4754:1: ( ( RULE_STRING ) )
            // InternalCQL.g:4755:2: ( RULE_STRING )
            {
            // InternalCQL.g:4755:2: ( RULE_STRING )
            // InternalCQL.g:4756:3: RULE_STRING
            {
             before(grammarAccess.getWindow_TimebasedAccess().getUnitSTRINGTerminalRuleCall_4_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getUnitSTRINGTerminalRuleCall_4_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__UnitAssignment_4"


    // $ANTLR start "rule__Window_Timebased__Advance_sizeAssignment_5_1"
    // InternalCQL.g:4765:1: rule__Window_Timebased__Advance_sizeAssignment_5_1 : ( RULE_INT ) ;
    public final void rule__Window_Timebased__Advance_sizeAssignment_5_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4769:1: ( ( RULE_INT ) )
            // InternalCQL.g:4770:2: ( RULE_INT )
            {
            // InternalCQL.g:4770:2: ( RULE_INT )
            // InternalCQL.g:4771:3: RULE_INT
            {
             before(grammarAccess.getWindow_TimebasedAccess().getAdvance_sizeINTTerminalRuleCall_5_1_0()); 
            match(input,RULE_INT,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getAdvance_sizeINTTerminalRuleCall_5_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Advance_sizeAssignment_5_1"


    // $ANTLR start "rule__Window_Timebased__Advance_unitAssignment_5_2"
    // InternalCQL.g:4780:1: rule__Window_Timebased__Advance_unitAssignment_5_2 : ( RULE_STRING ) ;
    public final void rule__Window_Timebased__Advance_unitAssignment_5_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4784:1: ( ( RULE_STRING ) )
            // InternalCQL.g:4785:2: ( RULE_STRING )
            {
            // InternalCQL.g:4785:2: ( RULE_STRING )
            // InternalCQL.g:4786:3: RULE_STRING
            {
             before(grammarAccess.getWindow_TimebasedAccess().getAdvance_unitSTRINGTerminalRuleCall_5_2_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getAdvance_unitSTRINGTerminalRuleCall_5_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Advance_unitAssignment_5_2"


    // $ANTLR start "rule__Window_Timebased__Partition_attributeAssignment_7_2"
    // InternalCQL.g:4795:1: rule__Window_Timebased__Partition_attributeAssignment_7_2 : ( ruleAttribute ) ;
    public final void rule__Window_Timebased__Partition_attributeAssignment_7_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4799:1: ( ( ruleAttribute ) )
            // InternalCQL.g:4800:2: ( ruleAttribute )
            {
            // InternalCQL.g:4800:2: ( ruleAttribute )
            // InternalCQL.g:4801:3: ruleAttribute
            {
             before(grammarAccess.getWindow_TimebasedAccess().getPartition_attributeAttributeParserRuleCall_7_2_0()); 
            pushFollow(FOLLOW_2);
            ruleAttribute();

            state._fsp--;

             after(grammarAccess.getWindow_TimebasedAccess().getPartition_attributeAttributeParserRuleCall_7_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Partition_attributeAssignment_7_2"


    // $ANTLR start "rule__Window_Tuplebased__SizeAssignment_3"
    // InternalCQL.g:4810:1: rule__Window_Tuplebased__SizeAssignment_3 : ( RULE_INT ) ;
    public final void rule__Window_Tuplebased__SizeAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4814:1: ( ( RULE_INT ) )
            // InternalCQL.g:4815:2: ( RULE_INT )
            {
            // InternalCQL.g:4815:2: ( RULE_INT )
            // InternalCQL.g:4816:3: RULE_INT
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getSizeINTTerminalRuleCall_3_0()); 
            match(input,RULE_INT,FOLLOW_2); 
             after(grammarAccess.getWindow_TuplebasedAccess().getSizeINTTerminalRuleCall_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__SizeAssignment_3"


    // $ANTLR start "rule__Window_Tuplebased__Advance_sizeAssignment_4_1"
    // InternalCQL.g:4825:1: rule__Window_Tuplebased__Advance_sizeAssignment_4_1 : ( RULE_INT ) ;
    public final void rule__Window_Tuplebased__Advance_sizeAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4829:1: ( ( RULE_INT ) )
            // InternalCQL.g:4830:2: ( RULE_INT )
            {
            // InternalCQL.g:4830:2: ( RULE_INT )
            // InternalCQL.g:4831:3: RULE_INT
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getAdvance_sizeINTTerminalRuleCall_4_1_0()); 
            match(input,RULE_INT,FOLLOW_2); 
             after(grammarAccess.getWindow_TuplebasedAccess().getAdvance_sizeINTTerminalRuleCall_4_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Advance_sizeAssignment_4_1"


    // $ANTLR start "rule__Window_Tuplebased__Partition_attributeAssignment_6_2"
    // InternalCQL.g:4840:1: rule__Window_Tuplebased__Partition_attributeAssignment_6_2 : ( ruleAttribute ) ;
    public final void rule__Window_Tuplebased__Partition_attributeAssignment_6_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4844:1: ( ( ruleAttribute ) )
            // InternalCQL.g:4845:2: ( ruleAttribute )
            {
            // InternalCQL.g:4845:2: ( ruleAttribute )
            // InternalCQL.g:4846:3: ruleAttribute
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getPartition_attributeAttributeParserRuleCall_6_2_0()); 
            pushFollow(FOLLOW_2);
            ruleAttribute();

            state._fsp--;

             after(grammarAccess.getWindow_TuplebasedAccess().getPartition_attributeAttributeParserRuleCall_6_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Partition_attributeAssignment_6_2"


    // $ANTLR start "rule__Create_Statement__TypeAssignment_1"
    // InternalCQL.g:4855:1: rule__Create_Statement__TypeAssignment_1 : ( ( rule__Create_Statement__TypeAlternatives_1_0 ) ) ;
    public final void rule__Create_Statement__TypeAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4859:1: ( ( ( rule__Create_Statement__TypeAlternatives_1_0 ) ) )
            // InternalCQL.g:4860:2: ( ( rule__Create_Statement__TypeAlternatives_1_0 ) )
            {
            // InternalCQL.g:4860:2: ( ( rule__Create_Statement__TypeAlternatives_1_0 ) )
            // InternalCQL.g:4861:3: ( rule__Create_Statement__TypeAlternatives_1_0 )
            {
             before(grammarAccess.getCreate_StatementAccess().getTypeAlternatives_1_0()); 
            // InternalCQL.g:4862:3: ( rule__Create_Statement__TypeAlternatives_1_0 )
            // InternalCQL.g:4862:4: rule__Create_Statement__TypeAlternatives_1_0
            {
            pushFollow(FOLLOW_2);
            rule__Create_Statement__TypeAlternatives_1_0();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StatementAccess().getTypeAlternatives_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__TypeAssignment_1"


    // $ANTLR start "rule__Create_Statement__NameAssignment_2"
    // InternalCQL.g:4870:1: rule__Create_Statement__NameAssignment_2 : ( RULE_ID ) ;
    public final void rule__Create_Statement__NameAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4874:1: ( ( RULE_ID ) )
            // InternalCQL.g:4875:2: ( RULE_ID )
            {
            // InternalCQL.g:4875:2: ( RULE_ID )
            // InternalCQL.g:4876:3: RULE_ID
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
    // InternalCQL.g:4885:1: rule__Create_Statement__AttributesAssignment_4 : ( ruleAttribute ) ;
    public final void rule__Create_Statement__AttributesAssignment_4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4889:1: ( ( ruleAttribute ) )
            // InternalCQL.g:4890:2: ( ruleAttribute )
            {
            // InternalCQL.g:4890:2: ( ruleAttribute )
            // InternalCQL.g:4891:3: ruleAttribute
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
    // InternalCQL.g:4900:1: rule__Create_Statement__DatatypesAssignment_5 : ( ruleDataType ) ;
    public final void rule__Create_Statement__DatatypesAssignment_5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4904:1: ( ( ruleDataType ) )
            // InternalCQL.g:4905:2: ( ruleDataType )
            {
            // InternalCQL.g:4905:2: ( ruleDataType )
            // InternalCQL.g:4906:3: ruleDataType
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
    // InternalCQL.g:4915:1: rule__Create_Statement__AttributesAssignment_6_1 : ( ruleAttribute ) ;
    public final void rule__Create_Statement__AttributesAssignment_6_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4919:1: ( ( ruleAttribute ) )
            // InternalCQL.g:4920:2: ( ruleAttribute )
            {
            // InternalCQL.g:4920:2: ( ruleAttribute )
            // InternalCQL.g:4921:3: ruleAttribute
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
    // InternalCQL.g:4930:1: rule__Create_Statement__DatatypesAssignment_6_2 : ( ruleDataType ) ;
    public final void rule__Create_Statement__DatatypesAssignment_6_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4934:1: ( ( ruleDataType ) )
            // InternalCQL.g:4935:2: ( ruleDataType )
            {
            // InternalCQL.g:4935:2: ( ruleDataType )
            // InternalCQL.g:4936:3: ruleDataType
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


    // $ANTLR start "rule__Create_Statement__HostAssignment_9"
    // InternalCQL.g:4945:1: rule__Create_Statement__HostAssignment_9 : ( RULE_ID ) ;
    public final void rule__Create_Statement__HostAssignment_9() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4949:1: ( ( RULE_ID ) )
            // InternalCQL.g:4950:2: ( RULE_ID )
            {
            // InternalCQL.g:4950:2: ( RULE_ID )
            // InternalCQL.g:4951:3: RULE_ID
            {
             before(grammarAccess.getCreate_StatementAccess().getHostIDTerminalRuleCall_9_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getCreate_StatementAccess().getHostIDTerminalRuleCall_9_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__HostAssignment_9"


    // $ANTLR start "rule__Create_Statement__PortAssignment_11"
    // InternalCQL.g:4960:1: rule__Create_Statement__PortAssignment_11 : ( RULE_INT ) ;
    public final void rule__Create_Statement__PortAssignment_11() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4964:1: ( ( RULE_INT ) )
            // InternalCQL.g:4965:2: ( RULE_INT )
            {
            // InternalCQL.g:4965:2: ( RULE_INT )
            // InternalCQL.g:4966:3: RULE_INT
            {
             before(grammarAccess.getCreate_StatementAccess().getPortINTTerminalRuleCall_11_0()); 
            match(input,RULE_INT,FOLLOW_2); 
             after(grammarAccess.getCreate_StatementAccess().getPortINTTerminalRuleCall_11_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__PortAssignment_11"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0120000000000002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000003000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x000000A0000030F0L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x000000A0000030F2L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000000600000L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000000600002L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000007800000L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000007800002L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000001800000000L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000001800000002L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000018000000L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000018000002L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000010008000080L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000010008000082L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000040000000002L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000140000000000L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0003000000000000L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0004400000000000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0012000000000000L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x00000000E0000000L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x00000000001FC000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000044000000000L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x00000000001FC002L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0080000000000000L});

}