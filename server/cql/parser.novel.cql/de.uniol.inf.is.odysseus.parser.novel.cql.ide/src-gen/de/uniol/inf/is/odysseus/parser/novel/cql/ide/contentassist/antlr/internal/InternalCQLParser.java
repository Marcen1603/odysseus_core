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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_INT", "RULE_FLOAT_NUMBER", "RULE_STRING", "RULE_ID", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'UNBOUNDED'", "'TRUE'", "'FALSE'", "'INTEGER'", "'DOUBLE'", "'FLOAT'", "'STRING'", "'BOOLEAN'", "'STARTTIMESTAMP'", "'ENDTIMESTAMP'", "'=='", "'!='", "'>='", "'<='", "'<'", "'>'", "'*'", "'/'", "'STREAM'", "'VIEW'", "';'", "'['", "']'", "'OR'", "'AND'", "'+'", "'-'", "'('", "')'", "'NOT'", "'DISTINCT'", "'FROM'", "','", "'WHERE'", "'SIZE'", "'TIME'", "'ADVANCE'", "'TUPLE'", "'PARTITION'", "'BY'", "'CREATE'", "'SINK'", "'WRAPPER'", "'PROTOCOL'", "'TRANSPORT'", "'DATAHANDLER'", "'OPTIONS'", "'CHANNEL'", "':'", "'SELECT'"
    };
    public static final int T__50=50;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__59=59;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__55=55;
    public static final int T__12=12;
    public static final int T__56=56;
    public static final int T__13=13;
    public static final int T__57=57;
    public static final int T__14=14;
    public static final int T__58=58;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__60=60;
    public static final int T__61=61;
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

                if ( (LA1_0==52||LA1_0==61) ) {
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
    // InternalCQL.g:137:1: ruleSource : ( ( rule__Source__Group__0 ) ) ;
    public final void ruleSource() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:141:2: ( ( ( rule__Source__Group__0 ) ) )
            // InternalCQL.g:142:2: ( ( rule__Source__Group__0 ) )
            {
            // InternalCQL.g:142:2: ( ( rule__Source__Group__0 ) )
            // InternalCQL.g:143:3: ( rule__Source__Group__0 )
            {
             before(grammarAccess.getSourceAccess().getGroup()); 
            // InternalCQL.g:144:3: ( rule__Source__Group__0 )
            // InternalCQL.g:144:4: rule__Source__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Source__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getSourceAccess().getGroup()); 

            }


            }

        }
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


    // $ANTLR start "entryRuleWindow_Unbounded"
    // InternalCQL.g:453:1: entryRuleWindow_Unbounded : ruleWindow_Unbounded EOF ;
    public final void entryRuleWindow_Unbounded() throws RecognitionException {
        try {
            // InternalCQL.g:454:1: ( ruleWindow_Unbounded EOF )
            // InternalCQL.g:455:1: ruleWindow_Unbounded EOF
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
    // InternalCQL.g:462:1: ruleWindow_Unbounded : ( 'UNBOUNDED' ) ;
    public final void ruleWindow_Unbounded() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:466:2: ( ( 'UNBOUNDED' ) )
            // InternalCQL.g:467:2: ( 'UNBOUNDED' )
            {
            // InternalCQL.g:467:2: ( 'UNBOUNDED' )
            // InternalCQL.g:468:3: 'UNBOUNDED'
            {
             before(grammarAccess.getWindow_UnboundedAccess().getUNBOUNDEDKeyword()); 
            match(input,12,FOLLOW_2); 
             after(grammarAccess.getWindow_UnboundedAccess().getUNBOUNDEDKeyword()); 

            }


            }

        }
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
    // InternalCQL.g:478:1: entryRuleWindow_Timebased : ruleWindow_Timebased EOF ;
    public final void entryRuleWindow_Timebased() throws RecognitionException {
        try {
            // InternalCQL.g:479:1: ( ruleWindow_Timebased EOF )
            // InternalCQL.g:480:1: ruleWindow_Timebased EOF
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
    // InternalCQL.g:487:1: ruleWindow_Timebased : ( ( rule__Window_Timebased__Group__0 ) ) ;
    public final void ruleWindow_Timebased() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:491:2: ( ( ( rule__Window_Timebased__Group__0 ) ) )
            // InternalCQL.g:492:2: ( ( rule__Window_Timebased__Group__0 ) )
            {
            // InternalCQL.g:492:2: ( ( rule__Window_Timebased__Group__0 ) )
            // InternalCQL.g:493:3: ( rule__Window_Timebased__Group__0 )
            {
             before(grammarAccess.getWindow_TimebasedAccess().getGroup()); 
            // InternalCQL.g:494:3: ( rule__Window_Timebased__Group__0 )
            // InternalCQL.g:494:4: rule__Window_Timebased__Group__0
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
    // InternalCQL.g:503:1: entryRuleWindow_Tuplebased : ruleWindow_Tuplebased EOF ;
    public final void entryRuleWindow_Tuplebased() throws RecognitionException {
        try {
            // InternalCQL.g:504:1: ( ruleWindow_Tuplebased EOF )
            // InternalCQL.g:505:1: ruleWindow_Tuplebased EOF
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
    // InternalCQL.g:512:1: ruleWindow_Tuplebased : ( ( rule__Window_Tuplebased__Group__0 ) ) ;
    public final void ruleWindow_Tuplebased() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:516:2: ( ( ( rule__Window_Tuplebased__Group__0 ) ) )
            // InternalCQL.g:517:2: ( ( rule__Window_Tuplebased__Group__0 ) )
            {
            // InternalCQL.g:517:2: ( ( rule__Window_Tuplebased__Group__0 ) )
            // InternalCQL.g:518:3: ( rule__Window_Tuplebased__Group__0 )
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getGroup()); 
            // InternalCQL.g:519:3: ( rule__Window_Tuplebased__Group__0 )
            // InternalCQL.g:519:4: rule__Window_Tuplebased__Group__0
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
    // InternalCQL.g:528:1: entryRuleCreate_Statement : ruleCreate_Statement EOF ;
    public final void entryRuleCreate_Statement() throws RecognitionException {
        try {
            // InternalCQL.g:529:1: ( ruleCreate_Statement EOF )
            // InternalCQL.g:530:1: ruleCreate_Statement EOF
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
    // InternalCQL.g:537:1: ruleCreate_Statement : ( ( rule__Create_Statement__Group__0 ) ) ;
    public final void ruleCreate_Statement() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:541:2: ( ( ( rule__Create_Statement__Group__0 ) ) )
            // InternalCQL.g:542:2: ( ( rule__Create_Statement__Group__0 ) )
            {
            // InternalCQL.g:542:2: ( ( rule__Create_Statement__Group__0 ) )
            // InternalCQL.g:543:3: ( rule__Create_Statement__Group__0 )
            {
             before(grammarAccess.getCreate_StatementAccess().getGroup()); 
            // InternalCQL.g:544:3: ( rule__Create_Statement__Group__0 )
            // InternalCQL.g:544:4: rule__Create_Statement__Group__0
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


    // $ANTLR start "entryRuleCreate_Sink"
    // InternalCQL.g:553:1: entryRuleCreate_Sink : ruleCreate_Sink EOF ;
    public final void entryRuleCreate_Sink() throws RecognitionException {
        try {
            // InternalCQL.g:554:1: ( ruleCreate_Sink EOF )
            // InternalCQL.g:555:1: ruleCreate_Sink EOF
            {
             before(grammarAccess.getCreate_SinkRule()); 
            pushFollow(FOLLOW_1);
            ruleCreate_Sink();

            state._fsp--;

             after(grammarAccess.getCreate_SinkRule()); 
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
    // $ANTLR end "entryRuleCreate_Sink"


    // $ANTLR start "ruleCreate_Sink"
    // InternalCQL.g:562:1: ruleCreate_Sink : ( ( rule__Create_Sink__Group__0 ) ) ;
    public final void ruleCreate_Sink() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:566:2: ( ( ( rule__Create_Sink__Group__0 ) ) )
            // InternalCQL.g:567:2: ( ( rule__Create_Sink__Group__0 ) )
            {
            // InternalCQL.g:567:2: ( ( rule__Create_Sink__Group__0 ) )
            // InternalCQL.g:568:3: ( rule__Create_Sink__Group__0 )
            {
             before(grammarAccess.getCreate_SinkAccess().getGroup()); 
            // InternalCQL.g:569:3: ( rule__Create_Sink__Group__0 )
            // InternalCQL.g:569:4: rule__Create_Sink__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getCreate_SinkAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleCreate_Sink"


    // $ANTLR start "entryRuleCreate_Stream"
    // InternalCQL.g:578:1: entryRuleCreate_Stream : ruleCreate_Stream EOF ;
    public final void entryRuleCreate_Stream() throws RecognitionException {
        try {
            // InternalCQL.g:579:1: ( ruleCreate_Stream EOF )
            // InternalCQL.g:580:1: ruleCreate_Stream EOF
            {
             before(grammarAccess.getCreate_StreamRule()); 
            pushFollow(FOLLOW_1);
            ruleCreate_Stream();

            state._fsp--;

             after(grammarAccess.getCreate_StreamRule()); 
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
    // $ANTLR end "entryRuleCreate_Stream"


    // $ANTLR start "ruleCreate_Stream"
    // InternalCQL.g:587:1: ruleCreate_Stream : ( ( rule__Create_Stream__Group__0 ) ) ;
    public final void ruleCreate_Stream() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:591:2: ( ( ( rule__Create_Stream__Group__0 ) ) )
            // InternalCQL.g:592:2: ( ( rule__Create_Stream__Group__0 ) )
            {
            // InternalCQL.g:592:2: ( ( rule__Create_Stream__Group__0 ) )
            // InternalCQL.g:593:3: ( rule__Create_Stream__Group__0 )
            {
             before(grammarAccess.getCreate_StreamAccess().getGroup()); 
            // InternalCQL.g:594:3: ( rule__Create_Stream__Group__0 )
            // InternalCQL.g:594:4: rule__Create_Stream__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Create_Stream__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StreamAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleCreate_Stream"


    // $ANTLR start "rule__Statement__Alternatives_0"
    // InternalCQL.g:602:1: rule__Statement__Alternatives_0 : ( ( ( rule__Statement__TypeAssignment_0_0 ) ) | ( ( rule__Statement__TypeAssignment_0_1 ) ) );
    public final void rule__Statement__Alternatives_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:606:1: ( ( ( rule__Statement__TypeAssignment_0_0 ) ) | ( ( rule__Statement__TypeAssignment_0_1 ) ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==61) ) {
                alt2=1;
            }
            else if ( (LA2_0==52) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // InternalCQL.g:607:2: ( ( rule__Statement__TypeAssignment_0_0 ) )
                    {
                    // InternalCQL.g:607:2: ( ( rule__Statement__TypeAssignment_0_0 ) )
                    // InternalCQL.g:608:3: ( rule__Statement__TypeAssignment_0_0 )
                    {
                     before(grammarAccess.getStatementAccess().getTypeAssignment_0_0()); 
                    // InternalCQL.g:609:3: ( rule__Statement__TypeAssignment_0_0 )
                    // InternalCQL.g:609:4: rule__Statement__TypeAssignment_0_0
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
                    // InternalCQL.g:613:2: ( ( rule__Statement__TypeAssignment_0_1 ) )
                    {
                    // InternalCQL.g:613:2: ( ( rule__Statement__TypeAssignment_0_1 ) )
                    // InternalCQL.g:614:3: ( rule__Statement__TypeAssignment_0_1 )
                    {
                     before(grammarAccess.getStatementAccess().getTypeAssignment_0_1()); 
                    // InternalCQL.g:615:3: ( rule__Statement__TypeAssignment_0_1 )
                    // InternalCQL.g:615:4: rule__Statement__TypeAssignment_0_1
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
    // InternalCQL.g:623:1: rule__Atomic__Alternatives : ( ( ( rule__Atomic__Group_0__0 ) ) | ( ( rule__Atomic__Group_1__0 ) ) | ( ( rule__Atomic__Group_2__0 ) ) | ( ( rule__Atomic__Group_3__0 ) ) | ( ( rule__Atomic__Group_4__0 ) ) );
    public final void rule__Atomic__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:627:1: ( ( ( rule__Atomic__Group_0__0 ) ) | ( ( rule__Atomic__Group_1__0 ) ) | ( ( rule__Atomic__Group_2__0 ) ) | ( ( rule__Atomic__Group_3__0 ) ) | ( ( rule__Atomic__Group_4__0 ) ) )
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
            case 13:
            case 14:
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
                    // InternalCQL.g:628:2: ( ( rule__Atomic__Group_0__0 ) )
                    {
                    // InternalCQL.g:628:2: ( ( rule__Atomic__Group_0__0 ) )
                    // InternalCQL.g:629:3: ( rule__Atomic__Group_0__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_0()); 
                    // InternalCQL.g:630:3: ( rule__Atomic__Group_0__0 )
                    // InternalCQL.g:630:4: rule__Atomic__Group_0__0
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
                    // InternalCQL.g:634:2: ( ( rule__Atomic__Group_1__0 ) )
                    {
                    // InternalCQL.g:634:2: ( ( rule__Atomic__Group_1__0 ) )
                    // InternalCQL.g:635:3: ( rule__Atomic__Group_1__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_1()); 
                    // InternalCQL.g:636:3: ( rule__Atomic__Group_1__0 )
                    // InternalCQL.g:636:4: rule__Atomic__Group_1__0
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
                    // InternalCQL.g:640:2: ( ( rule__Atomic__Group_2__0 ) )
                    {
                    // InternalCQL.g:640:2: ( ( rule__Atomic__Group_2__0 ) )
                    // InternalCQL.g:641:3: ( rule__Atomic__Group_2__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_2()); 
                    // InternalCQL.g:642:3: ( rule__Atomic__Group_2__0 )
                    // InternalCQL.g:642:4: rule__Atomic__Group_2__0
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
                    // InternalCQL.g:646:2: ( ( rule__Atomic__Group_3__0 ) )
                    {
                    // InternalCQL.g:646:2: ( ( rule__Atomic__Group_3__0 ) )
                    // InternalCQL.g:647:3: ( rule__Atomic__Group_3__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_3()); 
                    // InternalCQL.g:648:3: ( rule__Atomic__Group_3__0 )
                    // InternalCQL.g:648:4: rule__Atomic__Group_3__0
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
                    // InternalCQL.g:652:2: ( ( rule__Atomic__Group_4__0 ) )
                    {
                    // InternalCQL.g:652:2: ( ( rule__Atomic__Group_4__0 ) )
                    // InternalCQL.g:653:3: ( rule__Atomic__Group_4__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_4()); 
                    // InternalCQL.g:654:3: ( rule__Atomic__Group_4__0 )
                    // InternalCQL.g:654:4: rule__Atomic__Group_4__0
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
    // InternalCQL.g:662:1: rule__Atomic__ValueAlternatives_3_1_0 : ( ( 'TRUE' ) | ( 'FALSE' ) );
    public final void rule__Atomic__ValueAlternatives_3_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:666:1: ( ( 'TRUE' ) | ( 'FALSE' ) )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==13) ) {
                alt4=1;
            }
            else if ( (LA4_0==14) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // InternalCQL.g:667:2: ( 'TRUE' )
                    {
                    // InternalCQL.g:667:2: ( 'TRUE' )
                    // InternalCQL.g:668:3: 'TRUE'
                    {
                     before(grammarAccess.getAtomicAccess().getValueTRUEKeyword_3_1_0_0()); 
                    match(input,13,FOLLOW_2); 
                     after(grammarAccess.getAtomicAccess().getValueTRUEKeyword_3_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:673:2: ( 'FALSE' )
                    {
                    // InternalCQL.g:673:2: ( 'FALSE' )
                    // InternalCQL.g:674:3: 'FALSE'
                    {
                     before(grammarAccess.getAtomicAccess().getValueFALSEKeyword_3_1_0_1()); 
                    match(input,14,FOLLOW_2); 
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


    // $ANTLR start "rule__Source__Alternatives_1_1"
    // InternalCQL.g:683:1: rule__Source__Alternatives_1_1 : ( ( ( rule__Source__UnboundedAssignment_1_1_0 ) ) | ( ( rule__Source__TimeAssignment_1_1_1 ) ) | ( ( rule__Source__TupleAssignment_1_1_2 ) ) );
    public final void rule__Source__Alternatives_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:687:1: ( ( ( rule__Source__UnboundedAssignment_1_1_0 ) ) | ( ( rule__Source__TimeAssignment_1_1_1 ) ) | ( ( rule__Source__TupleAssignment_1_1_2 ) ) )
            int alt5=3;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==12) ) {
                alt5=1;
            }
            else if ( (LA5_0==46) ) {
                int LA5_2 = input.LA(2);

                if ( (LA5_2==RULE_INT) ) {
                    int LA5_3 = input.LA(3);

                    if ( ((LA5_3>=48 && LA5_3<=49)) ) {
                        alt5=3;
                    }
                    else if ( (LA5_3==RULE_ID) ) {
                        alt5=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 5, 3, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 2, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // InternalCQL.g:688:2: ( ( rule__Source__UnboundedAssignment_1_1_0 ) )
                    {
                    // InternalCQL.g:688:2: ( ( rule__Source__UnboundedAssignment_1_1_0 ) )
                    // InternalCQL.g:689:3: ( rule__Source__UnboundedAssignment_1_1_0 )
                    {
                     before(grammarAccess.getSourceAccess().getUnboundedAssignment_1_1_0()); 
                    // InternalCQL.g:690:3: ( rule__Source__UnboundedAssignment_1_1_0 )
                    // InternalCQL.g:690:4: rule__Source__UnboundedAssignment_1_1_0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Source__UnboundedAssignment_1_1_0();

                    state._fsp--;


                    }

                     after(grammarAccess.getSourceAccess().getUnboundedAssignment_1_1_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:694:2: ( ( rule__Source__TimeAssignment_1_1_1 ) )
                    {
                    // InternalCQL.g:694:2: ( ( rule__Source__TimeAssignment_1_1_1 ) )
                    // InternalCQL.g:695:3: ( rule__Source__TimeAssignment_1_1_1 )
                    {
                     before(grammarAccess.getSourceAccess().getTimeAssignment_1_1_1()); 
                    // InternalCQL.g:696:3: ( rule__Source__TimeAssignment_1_1_1 )
                    // InternalCQL.g:696:4: rule__Source__TimeAssignment_1_1_1
                    {
                    pushFollow(FOLLOW_2);
                    rule__Source__TimeAssignment_1_1_1();

                    state._fsp--;


                    }

                     after(grammarAccess.getSourceAccess().getTimeAssignment_1_1_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:700:2: ( ( rule__Source__TupleAssignment_1_1_2 ) )
                    {
                    // InternalCQL.g:700:2: ( ( rule__Source__TupleAssignment_1_1_2 ) )
                    // InternalCQL.g:701:3: ( rule__Source__TupleAssignment_1_1_2 )
                    {
                     before(grammarAccess.getSourceAccess().getTupleAssignment_1_1_2()); 
                    // InternalCQL.g:702:3: ( rule__Source__TupleAssignment_1_1_2 )
                    // InternalCQL.g:702:4: rule__Source__TupleAssignment_1_1_2
                    {
                    pushFollow(FOLLOW_2);
                    rule__Source__TupleAssignment_1_1_2();

                    state._fsp--;


                    }

                     after(grammarAccess.getSourceAccess().getTupleAssignment_1_1_2()); 

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
    // $ANTLR end "rule__Source__Alternatives_1_1"


    // $ANTLR start "rule__DataType__Alternatives"
    // InternalCQL.g:710:1: rule__DataType__Alternatives : ( ( 'INTEGER' ) | ( 'DOUBLE' ) | ( 'FLOAT' ) | ( 'STRING' ) | ( 'BOOLEAN' ) | ( 'STARTTIMESTAMP' ) | ( 'ENDTIMESTAMP' ) );
    public final void rule__DataType__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:714:1: ( ( 'INTEGER' ) | ( 'DOUBLE' ) | ( 'FLOAT' ) | ( 'STRING' ) | ( 'BOOLEAN' ) | ( 'STARTTIMESTAMP' ) | ( 'ENDTIMESTAMP' ) )
            int alt6=7;
            switch ( input.LA(1) ) {
            case 15:
                {
                alt6=1;
                }
                break;
            case 16:
                {
                alt6=2;
                }
                break;
            case 17:
                {
                alt6=3;
                }
                break;
            case 18:
                {
                alt6=4;
                }
                break;
            case 19:
                {
                alt6=5;
                }
                break;
            case 20:
                {
                alt6=6;
                }
                break;
            case 21:
                {
                alt6=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // InternalCQL.g:715:2: ( 'INTEGER' )
                    {
                    // InternalCQL.g:715:2: ( 'INTEGER' )
                    // InternalCQL.g:716:3: 'INTEGER'
                    {
                     before(grammarAccess.getDataTypeAccess().getINTEGERKeyword_0()); 
                    match(input,15,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getINTEGERKeyword_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:721:2: ( 'DOUBLE' )
                    {
                    // InternalCQL.g:721:2: ( 'DOUBLE' )
                    // InternalCQL.g:722:3: 'DOUBLE'
                    {
                     before(grammarAccess.getDataTypeAccess().getDOUBLEKeyword_1()); 
                    match(input,16,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getDOUBLEKeyword_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:727:2: ( 'FLOAT' )
                    {
                    // InternalCQL.g:727:2: ( 'FLOAT' )
                    // InternalCQL.g:728:3: 'FLOAT'
                    {
                     before(grammarAccess.getDataTypeAccess().getFLOATKeyword_2()); 
                    match(input,17,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getFLOATKeyword_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalCQL.g:733:2: ( 'STRING' )
                    {
                    // InternalCQL.g:733:2: ( 'STRING' )
                    // InternalCQL.g:734:3: 'STRING'
                    {
                     before(grammarAccess.getDataTypeAccess().getSTRINGKeyword_3()); 
                    match(input,18,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getSTRINGKeyword_3()); 

                    }


                    }
                    break;
                case 5 :
                    // InternalCQL.g:739:2: ( 'BOOLEAN' )
                    {
                    // InternalCQL.g:739:2: ( 'BOOLEAN' )
                    // InternalCQL.g:740:3: 'BOOLEAN'
                    {
                     before(grammarAccess.getDataTypeAccess().getBOOLEANKeyword_4()); 
                    match(input,19,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getBOOLEANKeyword_4()); 

                    }


                    }
                    break;
                case 6 :
                    // InternalCQL.g:745:2: ( 'STARTTIMESTAMP' )
                    {
                    // InternalCQL.g:745:2: ( 'STARTTIMESTAMP' )
                    // InternalCQL.g:746:3: 'STARTTIMESTAMP'
                    {
                     before(grammarAccess.getDataTypeAccess().getSTARTTIMESTAMPKeyword_5()); 
                    match(input,20,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getSTARTTIMESTAMPKeyword_5()); 

                    }


                    }
                    break;
                case 7 :
                    // InternalCQL.g:751:2: ( 'ENDTIMESTAMP' )
                    {
                    // InternalCQL.g:751:2: ( 'ENDTIMESTAMP' )
                    // InternalCQL.g:752:3: 'ENDTIMESTAMP'
                    {
                     before(grammarAccess.getDataTypeAccess().getENDTIMESTAMPKeyword_6()); 
                    match(input,21,FOLLOW_2); 
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
    // InternalCQL.g:761:1: rule__Equalitiy__OpAlternatives_1_1_0 : ( ( '==' ) | ( '!=' ) );
    public final void rule__Equalitiy__OpAlternatives_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:765:1: ( ( '==' ) | ( '!=' ) )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==22) ) {
                alt7=1;
            }
            else if ( (LA7_0==23) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // InternalCQL.g:766:2: ( '==' )
                    {
                    // InternalCQL.g:766:2: ( '==' )
                    // InternalCQL.g:767:3: '=='
                    {
                     before(grammarAccess.getEqualitiyAccess().getOpEqualsSignEqualsSignKeyword_1_1_0_0()); 
                    match(input,22,FOLLOW_2); 
                     after(grammarAccess.getEqualitiyAccess().getOpEqualsSignEqualsSignKeyword_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:772:2: ( '!=' )
                    {
                    // InternalCQL.g:772:2: ( '!=' )
                    // InternalCQL.g:773:3: '!='
                    {
                     before(grammarAccess.getEqualitiyAccess().getOpExclamationMarkEqualsSignKeyword_1_1_0_1()); 
                    match(input,23,FOLLOW_2); 
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
    // InternalCQL.g:782:1: rule__Comparison__OpAlternatives_1_1_0 : ( ( '>=' ) | ( '<=' ) | ( '<' ) | ( '>' ) );
    public final void rule__Comparison__OpAlternatives_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:786:1: ( ( '>=' ) | ( '<=' ) | ( '<' ) | ( '>' ) )
            int alt8=4;
            switch ( input.LA(1) ) {
            case 24:
                {
                alt8=1;
                }
                break;
            case 25:
                {
                alt8=2;
                }
                break;
            case 26:
                {
                alt8=3;
                }
                break;
            case 27:
                {
                alt8=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // InternalCQL.g:787:2: ( '>=' )
                    {
                    // InternalCQL.g:787:2: ( '>=' )
                    // InternalCQL.g:788:3: '>='
                    {
                     before(grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0()); 
                    match(input,24,FOLLOW_2); 
                     after(grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:793:2: ( '<=' )
                    {
                    // InternalCQL.g:793:2: ( '<=' )
                    // InternalCQL.g:794:3: '<='
                    {
                     before(grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1()); 
                    match(input,25,FOLLOW_2); 
                     after(grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:799:2: ( '<' )
                    {
                    // InternalCQL.g:799:2: ( '<' )
                    // InternalCQL.g:800:3: '<'
                    {
                     before(grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2()); 
                    match(input,26,FOLLOW_2); 
                     after(grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalCQL.g:805:2: ( '>' )
                    {
                    // InternalCQL.g:805:2: ( '>' )
                    // InternalCQL.g:806:3: '>'
                    {
                     before(grammarAccess.getComparisonAccess().getOpGreaterThanSignKeyword_1_1_0_3()); 
                    match(input,27,FOLLOW_2); 
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
    // InternalCQL.g:815:1: rule__PlusOrMinus__Alternatives_1_0 : ( ( ( rule__PlusOrMinus__Group_1_0_0__0 ) ) | ( ( rule__PlusOrMinus__Group_1_0_1__0 ) ) );
    public final void rule__PlusOrMinus__Alternatives_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:819:1: ( ( ( rule__PlusOrMinus__Group_1_0_0__0 ) ) | ( ( rule__PlusOrMinus__Group_1_0_1__0 ) ) )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==37) ) {
                alt9=1;
            }
            else if ( (LA9_0==38) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // InternalCQL.g:820:2: ( ( rule__PlusOrMinus__Group_1_0_0__0 ) )
                    {
                    // InternalCQL.g:820:2: ( ( rule__PlusOrMinus__Group_1_0_0__0 ) )
                    // InternalCQL.g:821:3: ( rule__PlusOrMinus__Group_1_0_0__0 )
                    {
                     before(grammarAccess.getPlusOrMinusAccess().getGroup_1_0_0()); 
                    // InternalCQL.g:822:3: ( rule__PlusOrMinus__Group_1_0_0__0 )
                    // InternalCQL.g:822:4: rule__PlusOrMinus__Group_1_0_0__0
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
                    // InternalCQL.g:826:2: ( ( rule__PlusOrMinus__Group_1_0_1__0 ) )
                    {
                    // InternalCQL.g:826:2: ( ( rule__PlusOrMinus__Group_1_0_1__0 ) )
                    // InternalCQL.g:827:3: ( rule__PlusOrMinus__Group_1_0_1__0 )
                    {
                     before(grammarAccess.getPlusOrMinusAccess().getGroup_1_0_1()); 
                    // InternalCQL.g:828:3: ( rule__PlusOrMinus__Group_1_0_1__0 )
                    // InternalCQL.g:828:4: rule__PlusOrMinus__Group_1_0_1__0
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
    // InternalCQL.g:836:1: rule__MulOrDiv__OpAlternatives_1_1_0 : ( ( '*' ) | ( '/' ) );
    public final void rule__MulOrDiv__OpAlternatives_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:840:1: ( ( '*' ) | ( '/' ) )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==28) ) {
                alt10=1;
            }
            else if ( (LA10_0==29) ) {
                alt10=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // InternalCQL.g:841:2: ( '*' )
                    {
                    // InternalCQL.g:841:2: ( '*' )
                    // InternalCQL.g:842:3: '*'
                    {
                     before(grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0()); 
                    match(input,28,FOLLOW_2); 
                     after(grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:847:2: ( '/' )
                    {
                    // InternalCQL.g:847:2: ( '/' )
                    // InternalCQL.g:848:3: '/'
                    {
                     before(grammarAccess.getMulOrDivAccess().getOpSolidusKeyword_1_1_0_1()); 
                    match(input,29,FOLLOW_2); 
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
    // InternalCQL.g:857:1: rule__Primary__Alternatives : ( ( ( rule__Primary__Group_0__0 ) ) | ( ( rule__Primary__Group_1__0 ) ) | ( ruleAtomic ) );
    public final void rule__Primary__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:861:1: ( ( ( rule__Primary__Group_0__0 ) ) | ( ( rule__Primary__Group_1__0 ) ) | ( ruleAtomic ) )
            int alt11=3;
            switch ( input.LA(1) ) {
            case 39:
                {
                alt11=1;
                }
                break;
            case 41:
                {
                alt11=2;
                }
                break;
            case RULE_INT:
            case RULE_FLOAT_NUMBER:
            case RULE_STRING:
            case RULE_ID:
            case 13:
            case 14:
                {
                alt11=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }

            switch (alt11) {
                case 1 :
                    // InternalCQL.g:862:2: ( ( rule__Primary__Group_0__0 ) )
                    {
                    // InternalCQL.g:862:2: ( ( rule__Primary__Group_0__0 ) )
                    // InternalCQL.g:863:3: ( rule__Primary__Group_0__0 )
                    {
                     before(grammarAccess.getPrimaryAccess().getGroup_0()); 
                    // InternalCQL.g:864:3: ( rule__Primary__Group_0__0 )
                    // InternalCQL.g:864:4: rule__Primary__Group_0__0
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
                    // InternalCQL.g:868:2: ( ( rule__Primary__Group_1__0 ) )
                    {
                    // InternalCQL.g:868:2: ( ( rule__Primary__Group_1__0 ) )
                    // InternalCQL.g:869:3: ( rule__Primary__Group_1__0 )
                    {
                     before(grammarAccess.getPrimaryAccess().getGroup_1()); 
                    // InternalCQL.g:870:3: ( rule__Primary__Group_1__0 )
                    // InternalCQL.g:870:4: rule__Primary__Group_1__0
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
                    // InternalCQL.g:874:2: ( ruleAtomic )
                    {
                    // InternalCQL.g:874:2: ( ruleAtomic )
                    // InternalCQL.g:875:3: ruleAtomic
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
    // InternalCQL.g:884:1: rule__Select_Statement__Alternatives_2 : ( ( '*' ) | ( ( rule__Select_Statement__Group_2_1__0 ) ) );
    public final void rule__Select_Statement__Alternatives_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:888:1: ( ( '*' ) | ( ( rule__Select_Statement__Group_2_1__0 ) ) )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==28) ) {
                alt12=1;
            }
            else if ( (LA12_0==RULE_ID) ) {
                alt12=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // InternalCQL.g:889:2: ( '*' )
                    {
                    // InternalCQL.g:889:2: ( '*' )
                    // InternalCQL.g:890:3: '*'
                    {
                     before(grammarAccess.getSelect_StatementAccess().getAsteriskKeyword_2_0()); 
                    match(input,28,FOLLOW_2); 
                     after(grammarAccess.getSelect_StatementAccess().getAsteriskKeyword_2_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:895:2: ( ( rule__Select_Statement__Group_2_1__0 ) )
                    {
                    // InternalCQL.g:895:2: ( ( rule__Select_Statement__Group_2_1__0 ) )
                    // InternalCQL.g:896:3: ( rule__Select_Statement__Group_2_1__0 )
                    {
                     before(grammarAccess.getSelect_StatementAccess().getGroup_2_1()); 
                    // InternalCQL.g:897:3: ( rule__Select_Statement__Group_2_1__0 )
                    // InternalCQL.g:897:4: rule__Select_Statement__Group_2_1__0
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


    // $ANTLR start "rule__Create_Statement__Alternatives_1"
    // InternalCQL.g:905:1: rule__Create_Statement__Alternatives_1 : ( ( ( rule__Create_Statement__StreamAssignment_1_0 ) ) | ( ( rule__Create_Statement__SinkAssignment_1_1 ) ) );
    public final void rule__Create_Statement__Alternatives_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:909:1: ( ( ( rule__Create_Statement__StreamAssignment_1_0 ) ) | ( ( rule__Create_Statement__SinkAssignment_1_1 ) ) )
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( ((LA13_0>=30 && LA13_0<=31)) ) {
                alt13=1;
            }
            else if ( (LA13_0==53) ) {
                alt13=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }
            switch (alt13) {
                case 1 :
                    // InternalCQL.g:910:2: ( ( rule__Create_Statement__StreamAssignment_1_0 ) )
                    {
                    // InternalCQL.g:910:2: ( ( rule__Create_Statement__StreamAssignment_1_0 ) )
                    // InternalCQL.g:911:3: ( rule__Create_Statement__StreamAssignment_1_0 )
                    {
                     before(grammarAccess.getCreate_StatementAccess().getStreamAssignment_1_0()); 
                    // InternalCQL.g:912:3: ( rule__Create_Statement__StreamAssignment_1_0 )
                    // InternalCQL.g:912:4: rule__Create_Statement__StreamAssignment_1_0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Create_Statement__StreamAssignment_1_0();

                    state._fsp--;


                    }

                     after(grammarAccess.getCreate_StatementAccess().getStreamAssignment_1_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:916:2: ( ( rule__Create_Statement__SinkAssignment_1_1 ) )
                    {
                    // InternalCQL.g:916:2: ( ( rule__Create_Statement__SinkAssignment_1_1 ) )
                    // InternalCQL.g:917:3: ( rule__Create_Statement__SinkAssignment_1_1 )
                    {
                     before(grammarAccess.getCreate_StatementAccess().getSinkAssignment_1_1()); 
                    // InternalCQL.g:918:3: ( rule__Create_Statement__SinkAssignment_1_1 )
                    // InternalCQL.g:918:4: rule__Create_Statement__SinkAssignment_1_1
                    {
                    pushFollow(FOLLOW_2);
                    rule__Create_Statement__SinkAssignment_1_1();

                    state._fsp--;


                    }

                     after(grammarAccess.getCreate_StatementAccess().getSinkAssignment_1_1()); 

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
    // $ANTLR end "rule__Create_Statement__Alternatives_1"


    // $ANTLR start "rule__Create_Stream__Alternatives_0"
    // InternalCQL.g:926:1: rule__Create_Stream__Alternatives_0 : ( ( 'STREAM' ) | ( 'VIEW' ) );
    public final void rule__Create_Stream__Alternatives_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:930:1: ( ( 'STREAM' ) | ( 'VIEW' ) )
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==30) ) {
                alt14=1;
            }
            else if ( (LA14_0==31) ) {
                alt14=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // InternalCQL.g:931:2: ( 'STREAM' )
                    {
                    // InternalCQL.g:931:2: ( 'STREAM' )
                    // InternalCQL.g:932:3: 'STREAM'
                    {
                     before(grammarAccess.getCreate_StreamAccess().getSTREAMKeyword_0_0()); 
                    match(input,30,FOLLOW_2); 
                     after(grammarAccess.getCreate_StreamAccess().getSTREAMKeyword_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:937:2: ( 'VIEW' )
                    {
                    // InternalCQL.g:937:2: ( 'VIEW' )
                    // InternalCQL.g:938:3: 'VIEW'
                    {
                     before(grammarAccess.getCreate_StreamAccess().getVIEWKeyword_0_1()); 
                    match(input,31,FOLLOW_2); 
                     after(grammarAccess.getCreate_StreamAccess().getVIEWKeyword_0_1()); 

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
    // $ANTLR end "rule__Create_Stream__Alternatives_0"


    // $ANTLR start "rule__Statement__Group__0"
    // InternalCQL.g:947:1: rule__Statement__Group__0 : rule__Statement__Group__0__Impl rule__Statement__Group__1 ;
    public final void rule__Statement__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:951:1: ( rule__Statement__Group__0__Impl rule__Statement__Group__1 )
            // InternalCQL.g:952:2: rule__Statement__Group__0__Impl rule__Statement__Group__1
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
    // InternalCQL.g:959:1: rule__Statement__Group__0__Impl : ( ( rule__Statement__Alternatives_0 ) ) ;
    public final void rule__Statement__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:963:1: ( ( ( rule__Statement__Alternatives_0 ) ) )
            // InternalCQL.g:964:1: ( ( rule__Statement__Alternatives_0 ) )
            {
            // InternalCQL.g:964:1: ( ( rule__Statement__Alternatives_0 ) )
            // InternalCQL.g:965:2: ( rule__Statement__Alternatives_0 )
            {
             before(grammarAccess.getStatementAccess().getAlternatives_0()); 
            // InternalCQL.g:966:2: ( rule__Statement__Alternatives_0 )
            // InternalCQL.g:966:3: rule__Statement__Alternatives_0
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
    // InternalCQL.g:974:1: rule__Statement__Group__1 : rule__Statement__Group__1__Impl ;
    public final void rule__Statement__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:978:1: ( rule__Statement__Group__1__Impl )
            // InternalCQL.g:979:2: rule__Statement__Group__1__Impl
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
    // InternalCQL.g:985:1: rule__Statement__Group__1__Impl : ( ( ';' )? ) ;
    public final void rule__Statement__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:989:1: ( ( ( ';' )? ) )
            // InternalCQL.g:990:1: ( ( ';' )? )
            {
            // InternalCQL.g:990:1: ( ( ';' )? )
            // InternalCQL.g:991:2: ( ';' )?
            {
             before(grammarAccess.getStatementAccess().getSemicolonKeyword_1()); 
            // InternalCQL.g:992:2: ( ';' )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==32) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // InternalCQL.g:992:3: ';'
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
    // InternalCQL.g:1001:1: rule__Atomic__Group_0__0 : rule__Atomic__Group_0__0__Impl rule__Atomic__Group_0__1 ;
    public final void rule__Atomic__Group_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1005:1: ( rule__Atomic__Group_0__0__Impl rule__Atomic__Group_0__1 )
            // InternalCQL.g:1006:2: rule__Atomic__Group_0__0__Impl rule__Atomic__Group_0__1
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
    // InternalCQL.g:1013:1: rule__Atomic__Group_0__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1017:1: ( ( () ) )
            // InternalCQL.g:1018:1: ( () )
            {
            // InternalCQL.g:1018:1: ( () )
            // InternalCQL.g:1019:2: ()
            {
             before(grammarAccess.getAtomicAccess().getIntConstantAction_0_0()); 
            // InternalCQL.g:1020:2: ()
            // InternalCQL.g:1020:3: 
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
    // InternalCQL.g:1028:1: rule__Atomic__Group_0__1 : rule__Atomic__Group_0__1__Impl ;
    public final void rule__Atomic__Group_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1032:1: ( rule__Atomic__Group_0__1__Impl )
            // InternalCQL.g:1033:2: rule__Atomic__Group_0__1__Impl
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
    // InternalCQL.g:1039:1: rule__Atomic__Group_0__1__Impl : ( ( rule__Atomic__ValueAssignment_0_1 ) ) ;
    public final void rule__Atomic__Group_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1043:1: ( ( ( rule__Atomic__ValueAssignment_0_1 ) ) )
            // InternalCQL.g:1044:1: ( ( rule__Atomic__ValueAssignment_0_1 ) )
            {
            // InternalCQL.g:1044:1: ( ( rule__Atomic__ValueAssignment_0_1 ) )
            // InternalCQL.g:1045:2: ( rule__Atomic__ValueAssignment_0_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_0_1()); 
            // InternalCQL.g:1046:2: ( rule__Atomic__ValueAssignment_0_1 )
            // InternalCQL.g:1046:3: rule__Atomic__ValueAssignment_0_1
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
    // InternalCQL.g:1055:1: rule__Atomic__Group_1__0 : rule__Atomic__Group_1__0__Impl rule__Atomic__Group_1__1 ;
    public final void rule__Atomic__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1059:1: ( rule__Atomic__Group_1__0__Impl rule__Atomic__Group_1__1 )
            // InternalCQL.g:1060:2: rule__Atomic__Group_1__0__Impl rule__Atomic__Group_1__1
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
    // InternalCQL.g:1067:1: rule__Atomic__Group_1__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1071:1: ( ( () ) )
            // InternalCQL.g:1072:1: ( () )
            {
            // InternalCQL.g:1072:1: ( () )
            // InternalCQL.g:1073:2: ()
            {
             before(grammarAccess.getAtomicAccess().getFloatConstantAction_1_0()); 
            // InternalCQL.g:1074:2: ()
            // InternalCQL.g:1074:3: 
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
    // InternalCQL.g:1082:1: rule__Atomic__Group_1__1 : rule__Atomic__Group_1__1__Impl ;
    public final void rule__Atomic__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1086:1: ( rule__Atomic__Group_1__1__Impl )
            // InternalCQL.g:1087:2: rule__Atomic__Group_1__1__Impl
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
    // InternalCQL.g:1093:1: rule__Atomic__Group_1__1__Impl : ( ( rule__Atomic__ValueAssignment_1_1 ) ) ;
    public final void rule__Atomic__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1097:1: ( ( ( rule__Atomic__ValueAssignment_1_1 ) ) )
            // InternalCQL.g:1098:1: ( ( rule__Atomic__ValueAssignment_1_1 ) )
            {
            // InternalCQL.g:1098:1: ( ( rule__Atomic__ValueAssignment_1_1 ) )
            // InternalCQL.g:1099:2: ( rule__Atomic__ValueAssignment_1_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_1_1()); 
            // InternalCQL.g:1100:2: ( rule__Atomic__ValueAssignment_1_1 )
            // InternalCQL.g:1100:3: rule__Atomic__ValueAssignment_1_1
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
    // InternalCQL.g:1109:1: rule__Atomic__Group_2__0 : rule__Atomic__Group_2__0__Impl rule__Atomic__Group_2__1 ;
    public final void rule__Atomic__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1113:1: ( rule__Atomic__Group_2__0__Impl rule__Atomic__Group_2__1 )
            // InternalCQL.g:1114:2: rule__Atomic__Group_2__0__Impl rule__Atomic__Group_2__1
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
    // InternalCQL.g:1121:1: rule__Atomic__Group_2__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1125:1: ( ( () ) )
            // InternalCQL.g:1126:1: ( () )
            {
            // InternalCQL.g:1126:1: ( () )
            // InternalCQL.g:1127:2: ()
            {
             before(grammarAccess.getAtomicAccess().getStringConstantAction_2_0()); 
            // InternalCQL.g:1128:2: ()
            // InternalCQL.g:1128:3: 
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
    // InternalCQL.g:1136:1: rule__Atomic__Group_2__1 : rule__Atomic__Group_2__1__Impl ;
    public final void rule__Atomic__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1140:1: ( rule__Atomic__Group_2__1__Impl )
            // InternalCQL.g:1141:2: rule__Atomic__Group_2__1__Impl
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
    // InternalCQL.g:1147:1: rule__Atomic__Group_2__1__Impl : ( ( rule__Atomic__ValueAssignment_2_1 ) ) ;
    public final void rule__Atomic__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1151:1: ( ( ( rule__Atomic__ValueAssignment_2_1 ) ) )
            // InternalCQL.g:1152:1: ( ( rule__Atomic__ValueAssignment_2_1 ) )
            {
            // InternalCQL.g:1152:1: ( ( rule__Atomic__ValueAssignment_2_1 ) )
            // InternalCQL.g:1153:2: ( rule__Atomic__ValueAssignment_2_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_2_1()); 
            // InternalCQL.g:1154:2: ( rule__Atomic__ValueAssignment_2_1 )
            // InternalCQL.g:1154:3: rule__Atomic__ValueAssignment_2_1
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
    // InternalCQL.g:1163:1: rule__Atomic__Group_3__0 : rule__Atomic__Group_3__0__Impl rule__Atomic__Group_3__1 ;
    public final void rule__Atomic__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1167:1: ( rule__Atomic__Group_3__0__Impl rule__Atomic__Group_3__1 )
            // InternalCQL.g:1168:2: rule__Atomic__Group_3__0__Impl rule__Atomic__Group_3__1
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
    // InternalCQL.g:1175:1: rule__Atomic__Group_3__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1179:1: ( ( () ) )
            // InternalCQL.g:1180:1: ( () )
            {
            // InternalCQL.g:1180:1: ( () )
            // InternalCQL.g:1181:2: ()
            {
             before(grammarAccess.getAtomicAccess().getBoolConstantAction_3_0()); 
            // InternalCQL.g:1182:2: ()
            // InternalCQL.g:1182:3: 
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
    // InternalCQL.g:1190:1: rule__Atomic__Group_3__1 : rule__Atomic__Group_3__1__Impl ;
    public final void rule__Atomic__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1194:1: ( rule__Atomic__Group_3__1__Impl )
            // InternalCQL.g:1195:2: rule__Atomic__Group_3__1__Impl
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
    // InternalCQL.g:1201:1: rule__Atomic__Group_3__1__Impl : ( ( rule__Atomic__ValueAssignment_3_1 ) ) ;
    public final void rule__Atomic__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1205:1: ( ( ( rule__Atomic__ValueAssignment_3_1 ) ) )
            // InternalCQL.g:1206:1: ( ( rule__Atomic__ValueAssignment_3_1 ) )
            {
            // InternalCQL.g:1206:1: ( ( rule__Atomic__ValueAssignment_3_1 ) )
            // InternalCQL.g:1207:2: ( rule__Atomic__ValueAssignment_3_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_3_1()); 
            // InternalCQL.g:1208:2: ( rule__Atomic__ValueAssignment_3_1 )
            // InternalCQL.g:1208:3: rule__Atomic__ValueAssignment_3_1
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
    // InternalCQL.g:1217:1: rule__Atomic__Group_4__0 : rule__Atomic__Group_4__0__Impl rule__Atomic__Group_4__1 ;
    public final void rule__Atomic__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1221:1: ( rule__Atomic__Group_4__0__Impl rule__Atomic__Group_4__1 )
            // InternalCQL.g:1222:2: rule__Atomic__Group_4__0__Impl rule__Atomic__Group_4__1
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
    // InternalCQL.g:1229:1: rule__Atomic__Group_4__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1233:1: ( ( () ) )
            // InternalCQL.g:1234:1: ( () )
            {
            // InternalCQL.g:1234:1: ( () )
            // InternalCQL.g:1235:2: ()
            {
             before(grammarAccess.getAtomicAccess().getAttributeRefAction_4_0()); 
            // InternalCQL.g:1236:2: ()
            // InternalCQL.g:1236:3: 
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
    // InternalCQL.g:1244:1: rule__Atomic__Group_4__1 : rule__Atomic__Group_4__1__Impl ;
    public final void rule__Atomic__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1248:1: ( rule__Atomic__Group_4__1__Impl )
            // InternalCQL.g:1249:2: rule__Atomic__Group_4__1__Impl
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
    // InternalCQL.g:1255:1: rule__Atomic__Group_4__1__Impl : ( ( rule__Atomic__ValueAssignment_4_1 ) ) ;
    public final void rule__Atomic__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1259:1: ( ( ( rule__Atomic__ValueAssignment_4_1 ) ) )
            // InternalCQL.g:1260:1: ( ( rule__Atomic__ValueAssignment_4_1 ) )
            {
            // InternalCQL.g:1260:1: ( ( rule__Atomic__ValueAssignment_4_1 ) )
            // InternalCQL.g:1261:2: ( rule__Atomic__ValueAssignment_4_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_4_1()); 
            // InternalCQL.g:1262:2: ( rule__Atomic__ValueAssignment_4_1 )
            // InternalCQL.g:1262:3: rule__Atomic__ValueAssignment_4_1
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


    // $ANTLR start "rule__Source__Group__0"
    // InternalCQL.g:1271:1: rule__Source__Group__0 : rule__Source__Group__0__Impl rule__Source__Group__1 ;
    public final void rule__Source__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1275:1: ( rule__Source__Group__0__Impl rule__Source__Group__1 )
            // InternalCQL.g:1276:2: rule__Source__Group__0__Impl rule__Source__Group__1
            {
            pushFollow(FOLLOW_10);
            rule__Source__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Source__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Source__Group__0"


    // $ANTLR start "rule__Source__Group__0__Impl"
    // InternalCQL.g:1283:1: rule__Source__Group__0__Impl : ( ( rule__Source__NameAssignment_0 ) ) ;
    public final void rule__Source__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1287:1: ( ( ( rule__Source__NameAssignment_0 ) ) )
            // InternalCQL.g:1288:1: ( ( rule__Source__NameAssignment_0 ) )
            {
            // InternalCQL.g:1288:1: ( ( rule__Source__NameAssignment_0 ) )
            // InternalCQL.g:1289:2: ( rule__Source__NameAssignment_0 )
            {
             before(grammarAccess.getSourceAccess().getNameAssignment_0()); 
            // InternalCQL.g:1290:2: ( rule__Source__NameAssignment_0 )
            // InternalCQL.g:1290:3: rule__Source__NameAssignment_0
            {
            pushFollow(FOLLOW_2);
            rule__Source__NameAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getSourceAccess().getNameAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Source__Group__0__Impl"


    // $ANTLR start "rule__Source__Group__1"
    // InternalCQL.g:1298:1: rule__Source__Group__1 : rule__Source__Group__1__Impl ;
    public final void rule__Source__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1302:1: ( rule__Source__Group__1__Impl )
            // InternalCQL.g:1303:2: rule__Source__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Source__Group__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Source__Group__1"


    // $ANTLR start "rule__Source__Group__1__Impl"
    // InternalCQL.g:1309:1: rule__Source__Group__1__Impl : ( ( rule__Source__Group_1__0 )? ) ;
    public final void rule__Source__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1313:1: ( ( ( rule__Source__Group_1__0 )? ) )
            // InternalCQL.g:1314:1: ( ( rule__Source__Group_1__0 )? )
            {
            // InternalCQL.g:1314:1: ( ( rule__Source__Group_1__0 )? )
            // InternalCQL.g:1315:2: ( rule__Source__Group_1__0 )?
            {
             before(grammarAccess.getSourceAccess().getGroup_1()); 
            // InternalCQL.g:1316:2: ( rule__Source__Group_1__0 )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==33) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // InternalCQL.g:1316:3: rule__Source__Group_1__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Source__Group_1__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSourceAccess().getGroup_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Source__Group__1__Impl"


    // $ANTLR start "rule__Source__Group_1__0"
    // InternalCQL.g:1325:1: rule__Source__Group_1__0 : rule__Source__Group_1__0__Impl rule__Source__Group_1__1 ;
    public final void rule__Source__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1329:1: ( rule__Source__Group_1__0__Impl rule__Source__Group_1__1 )
            // InternalCQL.g:1330:2: rule__Source__Group_1__0__Impl rule__Source__Group_1__1
            {
            pushFollow(FOLLOW_11);
            rule__Source__Group_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Source__Group_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Source__Group_1__0"


    // $ANTLR start "rule__Source__Group_1__0__Impl"
    // InternalCQL.g:1337:1: rule__Source__Group_1__0__Impl : ( '[' ) ;
    public final void rule__Source__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1341:1: ( ( '[' ) )
            // InternalCQL.g:1342:1: ( '[' )
            {
            // InternalCQL.g:1342:1: ( '[' )
            // InternalCQL.g:1343:2: '['
            {
             before(grammarAccess.getSourceAccess().getLeftSquareBracketKeyword_1_0()); 
            match(input,33,FOLLOW_2); 
             after(grammarAccess.getSourceAccess().getLeftSquareBracketKeyword_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Source__Group_1__0__Impl"


    // $ANTLR start "rule__Source__Group_1__1"
    // InternalCQL.g:1352:1: rule__Source__Group_1__1 : rule__Source__Group_1__1__Impl rule__Source__Group_1__2 ;
    public final void rule__Source__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1356:1: ( rule__Source__Group_1__1__Impl rule__Source__Group_1__2 )
            // InternalCQL.g:1357:2: rule__Source__Group_1__1__Impl rule__Source__Group_1__2
            {
            pushFollow(FOLLOW_12);
            rule__Source__Group_1__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Source__Group_1__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Source__Group_1__1"


    // $ANTLR start "rule__Source__Group_1__1__Impl"
    // InternalCQL.g:1364:1: rule__Source__Group_1__1__Impl : ( ( rule__Source__Alternatives_1_1 ) ) ;
    public final void rule__Source__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1368:1: ( ( ( rule__Source__Alternatives_1_1 ) ) )
            // InternalCQL.g:1369:1: ( ( rule__Source__Alternatives_1_1 ) )
            {
            // InternalCQL.g:1369:1: ( ( rule__Source__Alternatives_1_1 ) )
            // InternalCQL.g:1370:2: ( rule__Source__Alternatives_1_1 )
            {
             before(grammarAccess.getSourceAccess().getAlternatives_1_1()); 
            // InternalCQL.g:1371:2: ( rule__Source__Alternatives_1_1 )
            // InternalCQL.g:1371:3: rule__Source__Alternatives_1_1
            {
            pushFollow(FOLLOW_2);
            rule__Source__Alternatives_1_1();

            state._fsp--;


            }

             after(grammarAccess.getSourceAccess().getAlternatives_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Source__Group_1__1__Impl"


    // $ANTLR start "rule__Source__Group_1__2"
    // InternalCQL.g:1379:1: rule__Source__Group_1__2 : rule__Source__Group_1__2__Impl ;
    public final void rule__Source__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1383:1: ( rule__Source__Group_1__2__Impl )
            // InternalCQL.g:1384:2: rule__Source__Group_1__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Source__Group_1__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Source__Group_1__2"


    // $ANTLR start "rule__Source__Group_1__2__Impl"
    // InternalCQL.g:1390:1: rule__Source__Group_1__2__Impl : ( ']' ) ;
    public final void rule__Source__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1394:1: ( ( ']' ) )
            // InternalCQL.g:1395:1: ( ']' )
            {
            // InternalCQL.g:1395:1: ( ']' )
            // InternalCQL.g:1396:2: ']'
            {
             before(grammarAccess.getSourceAccess().getRightSquareBracketKeyword_1_2()); 
            match(input,34,FOLLOW_2); 
             after(grammarAccess.getSourceAccess().getRightSquareBracketKeyword_1_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Source__Group_1__2__Impl"


    // $ANTLR start "rule__ExpressionsModel__Group__0"
    // InternalCQL.g:1406:1: rule__ExpressionsModel__Group__0 : rule__ExpressionsModel__Group__0__Impl rule__ExpressionsModel__Group__1 ;
    public final void rule__ExpressionsModel__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1410:1: ( rule__ExpressionsModel__Group__0__Impl rule__ExpressionsModel__Group__1 )
            // InternalCQL.g:1411:2: rule__ExpressionsModel__Group__0__Impl rule__ExpressionsModel__Group__1
            {
            pushFollow(FOLLOW_13);
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
    // InternalCQL.g:1418:1: rule__ExpressionsModel__Group__0__Impl : ( () ) ;
    public final void rule__ExpressionsModel__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1422:1: ( ( () ) )
            // InternalCQL.g:1423:1: ( () )
            {
            // InternalCQL.g:1423:1: ( () )
            // InternalCQL.g:1424:2: ()
            {
             before(grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0()); 
            // InternalCQL.g:1425:2: ()
            // InternalCQL.g:1425:3: 
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
    // InternalCQL.g:1433:1: rule__ExpressionsModel__Group__1 : rule__ExpressionsModel__Group__1__Impl ;
    public final void rule__ExpressionsModel__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1437:1: ( rule__ExpressionsModel__Group__1__Impl )
            // InternalCQL.g:1438:2: rule__ExpressionsModel__Group__1__Impl
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
    // InternalCQL.g:1444:1: rule__ExpressionsModel__Group__1__Impl : ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) ) ;
    public final void rule__ExpressionsModel__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1448:1: ( ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) ) )
            // InternalCQL.g:1449:1: ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) )
            {
            // InternalCQL.g:1449:1: ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) )
            // InternalCQL.g:1450:2: ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* )
            {
            // InternalCQL.g:1450:2: ( ( rule__ExpressionsModel__ElementsAssignment_1 ) )
            // InternalCQL.g:1451:3: ( rule__ExpressionsModel__ElementsAssignment_1 )
            {
             before(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1()); 
            // InternalCQL.g:1452:3: ( rule__ExpressionsModel__ElementsAssignment_1 )
            // InternalCQL.g:1452:4: rule__ExpressionsModel__ElementsAssignment_1
            {
            pushFollow(FOLLOW_14);
            rule__ExpressionsModel__ElementsAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1()); 

            }

            // InternalCQL.g:1455:2: ( ( rule__ExpressionsModel__ElementsAssignment_1 )* )
            // InternalCQL.g:1456:3: ( rule__ExpressionsModel__ElementsAssignment_1 )*
            {
             before(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1()); 
            // InternalCQL.g:1457:3: ( rule__ExpressionsModel__ElementsAssignment_1 )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( ((LA17_0>=RULE_INT && LA17_0<=RULE_ID)||(LA17_0>=13 && LA17_0<=14)||LA17_0==39||LA17_0==41) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // InternalCQL.g:1457:4: rule__ExpressionsModel__ElementsAssignment_1
            	    {
            	    pushFollow(FOLLOW_14);
            	    rule__ExpressionsModel__ElementsAssignment_1();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop17;
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
    // InternalCQL.g:1467:1: rule__Or__Group__0 : rule__Or__Group__0__Impl rule__Or__Group__1 ;
    public final void rule__Or__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1471:1: ( rule__Or__Group__0__Impl rule__Or__Group__1 )
            // InternalCQL.g:1472:2: rule__Or__Group__0__Impl rule__Or__Group__1
            {
            pushFollow(FOLLOW_15);
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
    // InternalCQL.g:1479:1: rule__Or__Group__0__Impl : ( ruleAnd ) ;
    public final void rule__Or__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1483:1: ( ( ruleAnd ) )
            // InternalCQL.g:1484:1: ( ruleAnd )
            {
            // InternalCQL.g:1484:1: ( ruleAnd )
            // InternalCQL.g:1485:2: ruleAnd
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
    // InternalCQL.g:1494:1: rule__Or__Group__1 : rule__Or__Group__1__Impl ;
    public final void rule__Or__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1498:1: ( rule__Or__Group__1__Impl )
            // InternalCQL.g:1499:2: rule__Or__Group__1__Impl
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
    // InternalCQL.g:1505:1: rule__Or__Group__1__Impl : ( ( rule__Or__Group_1__0 )* ) ;
    public final void rule__Or__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1509:1: ( ( ( rule__Or__Group_1__0 )* ) )
            // InternalCQL.g:1510:1: ( ( rule__Or__Group_1__0 )* )
            {
            // InternalCQL.g:1510:1: ( ( rule__Or__Group_1__0 )* )
            // InternalCQL.g:1511:2: ( rule__Or__Group_1__0 )*
            {
             before(grammarAccess.getOrAccess().getGroup_1()); 
            // InternalCQL.g:1512:2: ( rule__Or__Group_1__0 )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==35) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // InternalCQL.g:1512:3: rule__Or__Group_1__0
            	    {
            	    pushFollow(FOLLOW_16);
            	    rule__Or__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop18;
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
    // InternalCQL.g:1521:1: rule__Or__Group_1__0 : rule__Or__Group_1__0__Impl rule__Or__Group_1__1 ;
    public final void rule__Or__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1525:1: ( rule__Or__Group_1__0__Impl rule__Or__Group_1__1 )
            // InternalCQL.g:1526:2: rule__Or__Group_1__0__Impl rule__Or__Group_1__1
            {
            pushFollow(FOLLOW_15);
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
    // InternalCQL.g:1533:1: rule__Or__Group_1__0__Impl : ( () ) ;
    public final void rule__Or__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1537:1: ( ( () ) )
            // InternalCQL.g:1538:1: ( () )
            {
            // InternalCQL.g:1538:1: ( () )
            // InternalCQL.g:1539:2: ()
            {
             before(grammarAccess.getOrAccess().getOrLeftAction_1_0()); 
            // InternalCQL.g:1540:2: ()
            // InternalCQL.g:1540:3: 
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
    // InternalCQL.g:1548:1: rule__Or__Group_1__1 : rule__Or__Group_1__1__Impl rule__Or__Group_1__2 ;
    public final void rule__Or__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1552:1: ( rule__Or__Group_1__1__Impl rule__Or__Group_1__2 )
            // InternalCQL.g:1553:2: rule__Or__Group_1__1__Impl rule__Or__Group_1__2
            {
            pushFollow(FOLLOW_13);
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
    // InternalCQL.g:1560:1: rule__Or__Group_1__1__Impl : ( 'OR' ) ;
    public final void rule__Or__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1564:1: ( ( 'OR' ) )
            // InternalCQL.g:1565:1: ( 'OR' )
            {
            // InternalCQL.g:1565:1: ( 'OR' )
            // InternalCQL.g:1566:2: 'OR'
            {
             before(grammarAccess.getOrAccess().getORKeyword_1_1()); 
            match(input,35,FOLLOW_2); 
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
    // InternalCQL.g:1575:1: rule__Or__Group_1__2 : rule__Or__Group_1__2__Impl ;
    public final void rule__Or__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1579:1: ( rule__Or__Group_1__2__Impl )
            // InternalCQL.g:1580:2: rule__Or__Group_1__2__Impl
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
    // InternalCQL.g:1586:1: rule__Or__Group_1__2__Impl : ( ( rule__Or__RightAssignment_1_2 ) ) ;
    public final void rule__Or__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1590:1: ( ( ( rule__Or__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1591:1: ( ( rule__Or__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1591:1: ( ( rule__Or__RightAssignment_1_2 ) )
            // InternalCQL.g:1592:2: ( rule__Or__RightAssignment_1_2 )
            {
             before(grammarAccess.getOrAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1593:2: ( rule__Or__RightAssignment_1_2 )
            // InternalCQL.g:1593:3: rule__Or__RightAssignment_1_2
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
    // InternalCQL.g:1602:1: rule__And__Group__0 : rule__And__Group__0__Impl rule__And__Group__1 ;
    public final void rule__And__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1606:1: ( rule__And__Group__0__Impl rule__And__Group__1 )
            // InternalCQL.g:1607:2: rule__And__Group__0__Impl rule__And__Group__1
            {
            pushFollow(FOLLOW_17);
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
    // InternalCQL.g:1614:1: rule__And__Group__0__Impl : ( ruleEqualitiy ) ;
    public final void rule__And__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1618:1: ( ( ruleEqualitiy ) )
            // InternalCQL.g:1619:1: ( ruleEqualitiy )
            {
            // InternalCQL.g:1619:1: ( ruleEqualitiy )
            // InternalCQL.g:1620:2: ruleEqualitiy
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
    // InternalCQL.g:1629:1: rule__And__Group__1 : rule__And__Group__1__Impl ;
    public final void rule__And__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1633:1: ( rule__And__Group__1__Impl )
            // InternalCQL.g:1634:2: rule__And__Group__1__Impl
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
    // InternalCQL.g:1640:1: rule__And__Group__1__Impl : ( ( rule__And__Group_1__0 )* ) ;
    public final void rule__And__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1644:1: ( ( ( rule__And__Group_1__0 )* ) )
            // InternalCQL.g:1645:1: ( ( rule__And__Group_1__0 )* )
            {
            // InternalCQL.g:1645:1: ( ( rule__And__Group_1__0 )* )
            // InternalCQL.g:1646:2: ( rule__And__Group_1__0 )*
            {
             before(grammarAccess.getAndAccess().getGroup_1()); 
            // InternalCQL.g:1647:2: ( rule__And__Group_1__0 )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==36) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // InternalCQL.g:1647:3: rule__And__Group_1__0
            	    {
            	    pushFollow(FOLLOW_18);
            	    rule__And__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop19;
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
    // InternalCQL.g:1656:1: rule__And__Group_1__0 : rule__And__Group_1__0__Impl rule__And__Group_1__1 ;
    public final void rule__And__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1660:1: ( rule__And__Group_1__0__Impl rule__And__Group_1__1 )
            // InternalCQL.g:1661:2: rule__And__Group_1__0__Impl rule__And__Group_1__1
            {
            pushFollow(FOLLOW_17);
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
    // InternalCQL.g:1668:1: rule__And__Group_1__0__Impl : ( () ) ;
    public final void rule__And__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1672:1: ( ( () ) )
            // InternalCQL.g:1673:1: ( () )
            {
            // InternalCQL.g:1673:1: ( () )
            // InternalCQL.g:1674:2: ()
            {
             before(grammarAccess.getAndAccess().getAndLeftAction_1_0()); 
            // InternalCQL.g:1675:2: ()
            // InternalCQL.g:1675:3: 
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
    // InternalCQL.g:1683:1: rule__And__Group_1__1 : rule__And__Group_1__1__Impl rule__And__Group_1__2 ;
    public final void rule__And__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1687:1: ( rule__And__Group_1__1__Impl rule__And__Group_1__2 )
            // InternalCQL.g:1688:2: rule__And__Group_1__1__Impl rule__And__Group_1__2
            {
            pushFollow(FOLLOW_13);
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
    // InternalCQL.g:1695:1: rule__And__Group_1__1__Impl : ( 'AND' ) ;
    public final void rule__And__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1699:1: ( ( 'AND' ) )
            // InternalCQL.g:1700:1: ( 'AND' )
            {
            // InternalCQL.g:1700:1: ( 'AND' )
            // InternalCQL.g:1701:2: 'AND'
            {
             before(grammarAccess.getAndAccess().getANDKeyword_1_1()); 
            match(input,36,FOLLOW_2); 
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
    // InternalCQL.g:1710:1: rule__And__Group_1__2 : rule__And__Group_1__2__Impl ;
    public final void rule__And__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1714:1: ( rule__And__Group_1__2__Impl )
            // InternalCQL.g:1715:2: rule__And__Group_1__2__Impl
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
    // InternalCQL.g:1721:1: rule__And__Group_1__2__Impl : ( ( rule__And__RightAssignment_1_2 ) ) ;
    public final void rule__And__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1725:1: ( ( ( rule__And__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1726:1: ( ( rule__And__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1726:1: ( ( rule__And__RightAssignment_1_2 ) )
            // InternalCQL.g:1727:2: ( rule__And__RightAssignment_1_2 )
            {
             before(grammarAccess.getAndAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1728:2: ( rule__And__RightAssignment_1_2 )
            // InternalCQL.g:1728:3: rule__And__RightAssignment_1_2
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
    // InternalCQL.g:1737:1: rule__Equalitiy__Group__0 : rule__Equalitiy__Group__0__Impl rule__Equalitiy__Group__1 ;
    public final void rule__Equalitiy__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1741:1: ( rule__Equalitiy__Group__0__Impl rule__Equalitiy__Group__1 )
            // InternalCQL.g:1742:2: rule__Equalitiy__Group__0__Impl rule__Equalitiy__Group__1
            {
            pushFollow(FOLLOW_19);
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
    // InternalCQL.g:1749:1: rule__Equalitiy__Group__0__Impl : ( ruleComparison ) ;
    public final void rule__Equalitiy__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1753:1: ( ( ruleComparison ) )
            // InternalCQL.g:1754:1: ( ruleComparison )
            {
            // InternalCQL.g:1754:1: ( ruleComparison )
            // InternalCQL.g:1755:2: ruleComparison
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
    // InternalCQL.g:1764:1: rule__Equalitiy__Group__1 : rule__Equalitiy__Group__1__Impl ;
    public final void rule__Equalitiy__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1768:1: ( rule__Equalitiy__Group__1__Impl )
            // InternalCQL.g:1769:2: rule__Equalitiy__Group__1__Impl
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
    // InternalCQL.g:1775:1: rule__Equalitiy__Group__1__Impl : ( ( rule__Equalitiy__Group_1__0 )* ) ;
    public final void rule__Equalitiy__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1779:1: ( ( ( rule__Equalitiy__Group_1__0 )* ) )
            // InternalCQL.g:1780:1: ( ( rule__Equalitiy__Group_1__0 )* )
            {
            // InternalCQL.g:1780:1: ( ( rule__Equalitiy__Group_1__0 )* )
            // InternalCQL.g:1781:2: ( rule__Equalitiy__Group_1__0 )*
            {
             before(grammarAccess.getEqualitiyAccess().getGroup_1()); 
            // InternalCQL.g:1782:2: ( rule__Equalitiy__Group_1__0 )*
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( ((LA20_0>=22 && LA20_0<=23)) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // InternalCQL.g:1782:3: rule__Equalitiy__Group_1__0
            	    {
            	    pushFollow(FOLLOW_20);
            	    rule__Equalitiy__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop20;
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
    // InternalCQL.g:1791:1: rule__Equalitiy__Group_1__0 : rule__Equalitiy__Group_1__0__Impl rule__Equalitiy__Group_1__1 ;
    public final void rule__Equalitiy__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1795:1: ( rule__Equalitiy__Group_1__0__Impl rule__Equalitiy__Group_1__1 )
            // InternalCQL.g:1796:2: rule__Equalitiy__Group_1__0__Impl rule__Equalitiy__Group_1__1
            {
            pushFollow(FOLLOW_19);
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
    // InternalCQL.g:1803:1: rule__Equalitiy__Group_1__0__Impl : ( () ) ;
    public final void rule__Equalitiy__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1807:1: ( ( () ) )
            // InternalCQL.g:1808:1: ( () )
            {
            // InternalCQL.g:1808:1: ( () )
            // InternalCQL.g:1809:2: ()
            {
             before(grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0()); 
            // InternalCQL.g:1810:2: ()
            // InternalCQL.g:1810:3: 
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
    // InternalCQL.g:1818:1: rule__Equalitiy__Group_1__1 : rule__Equalitiy__Group_1__1__Impl rule__Equalitiy__Group_1__2 ;
    public final void rule__Equalitiy__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1822:1: ( rule__Equalitiy__Group_1__1__Impl rule__Equalitiy__Group_1__2 )
            // InternalCQL.g:1823:2: rule__Equalitiy__Group_1__1__Impl rule__Equalitiy__Group_1__2
            {
            pushFollow(FOLLOW_13);
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
    // InternalCQL.g:1830:1: rule__Equalitiy__Group_1__1__Impl : ( ( rule__Equalitiy__OpAssignment_1_1 ) ) ;
    public final void rule__Equalitiy__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1834:1: ( ( ( rule__Equalitiy__OpAssignment_1_1 ) ) )
            // InternalCQL.g:1835:1: ( ( rule__Equalitiy__OpAssignment_1_1 ) )
            {
            // InternalCQL.g:1835:1: ( ( rule__Equalitiy__OpAssignment_1_1 ) )
            // InternalCQL.g:1836:2: ( rule__Equalitiy__OpAssignment_1_1 )
            {
             before(grammarAccess.getEqualitiyAccess().getOpAssignment_1_1()); 
            // InternalCQL.g:1837:2: ( rule__Equalitiy__OpAssignment_1_1 )
            // InternalCQL.g:1837:3: rule__Equalitiy__OpAssignment_1_1
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
    // InternalCQL.g:1845:1: rule__Equalitiy__Group_1__2 : rule__Equalitiy__Group_1__2__Impl ;
    public final void rule__Equalitiy__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1849:1: ( rule__Equalitiy__Group_1__2__Impl )
            // InternalCQL.g:1850:2: rule__Equalitiy__Group_1__2__Impl
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
    // InternalCQL.g:1856:1: rule__Equalitiy__Group_1__2__Impl : ( ( rule__Equalitiy__RightAssignment_1_2 ) ) ;
    public final void rule__Equalitiy__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1860:1: ( ( ( rule__Equalitiy__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1861:1: ( ( rule__Equalitiy__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1861:1: ( ( rule__Equalitiy__RightAssignment_1_2 ) )
            // InternalCQL.g:1862:2: ( rule__Equalitiy__RightAssignment_1_2 )
            {
             before(grammarAccess.getEqualitiyAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1863:2: ( rule__Equalitiy__RightAssignment_1_2 )
            // InternalCQL.g:1863:3: rule__Equalitiy__RightAssignment_1_2
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
    // InternalCQL.g:1872:1: rule__Comparison__Group__0 : rule__Comparison__Group__0__Impl rule__Comparison__Group__1 ;
    public final void rule__Comparison__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1876:1: ( rule__Comparison__Group__0__Impl rule__Comparison__Group__1 )
            // InternalCQL.g:1877:2: rule__Comparison__Group__0__Impl rule__Comparison__Group__1
            {
            pushFollow(FOLLOW_21);
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
    // InternalCQL.g:1884:1: rule__Comparison__Group__0__Impl : ( rulePlusOrMinus ) ;
    public final void rule__Comparison__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1888:1: ( ( rulePlusOrMinus ) )
            // InternalCQL.g:1889:1: ( rulePlusOrMinus )
            {
            // InternalCQL.g:1889:1: ( rulePlusOrMinus )
            // InternalCQL.g:1890:2: rulePlusOrMinus
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
    // InternalCQL.g:1899:1: rule__Comparison__Group__1 : rule__Comparison__Group__1__Impl ;
    public final void rule__Comparison__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1903:1: ( rule__Comparison__Group__1__Impl )
            // InternalCQL.g:1904:2: rule__Comparison__Group__1__Impl
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
    // InternalCQL.g:1910:1: rule__Comparison__Group__1__Impl : ( ( rule__Comparison__Group_1__0 )* ) ;
    public final void rule__Comparison__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1914:1: ( ( ( rule__Comparison__Group_1__0 )* ) )
            // InternalCQL.g:1915:1: ( ( rule__Comparison__Group_1__0 )* )
            {
            // InternalCQL.g:1915:1: ( ( rule__Comparison__Group_1__0 )* )
            // InternalCQL.g:1916:2: ( rule__Comparison__Group_1__0 )*
            {
             before(grammarAccess.getComparisonAccess().getGroup_1()); 
            // InternalCQL.g:1917:2: ( rule__Comparison__Group_1__0 )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( ((LA21_0>=24 && LA21_0<=27)) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // InternalCQL.g:1917:3: rule__Comparison__Group_1__0
            	    {
            	    pushFollow(FOLLOW_22);
            	    rule__Comparison__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop21;
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
    // InternalCQL.g:1926:1: rule__Comparison__Group_1__0 : rule__Comparison__Group_1__0__Impl rule__Comparison__Group_1__1 ;
    public final void rule__Comparison__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1930:1: ( rule__Comparison__Group_1__0__Impl rule__Comparison__Group_1__1 )
            // InternalCQL.g:1931:2: rule__Comparison__Group_1__0__Impl rule__Comparison__Group_1__1
            {
            pushFollow(FOLLOW_21);
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
    // InternalCQL.g:1938:1: rule__Comparison__Group_1__0__Impl : ( () ) ;
    public final void rule__Comparison__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1942:1: ( ( () ) )
            // InternalCQL.g:1943:1: ( () )
            {
            // InternalCQL.g:1943:1: ( () )
            // InternalCQL.g:1944:2: ()
            {
             before(grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0()); 
            // InternalCQL.g:1945:2: ()
            // InternalCQL.g:1945:3: 
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
    // InternalCQL.g:1953:1: rule__Comparison__Group_1__1 : rule__Comparison__Group_1__1__Impl rule__Comparison__Group_1__2 ;
    public final void rule__Comparison__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1957:1: ( rule__Comparison__Group_1__1__Impl rule__Comparison__Group_1__2 )
            // InternalCQL.g:1958:2: rule__Comparison__Group_1__1__Impl rule__Comparison__Group_1__2
            {
            pushFollow(FOLLOW_13);
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
    // InternalCQL.g:1965:1: rule__Comparison__Group_1__1__Impl : ( ( rule__Comparison__OpAssignment_1_1 ) ) ;
    public final void rule__Comparison__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1969:1: ( ( ( rule__Comparison__OpAssignment_1_1 ) ) )
            // InternalCQL.g:1970:1: ( ( rule__Comparison__OpAssignment_1_1 ) )
            {
            // InternalCQL.g:1970:1: ( ( rule__Comparison__OpAssignment_1_1 ) )
            // InternalCQL.g:1971:2: ( rule__Comparison__OpAssignment_1_1 )
            {
             before(grammarAccess.getComparisonAccess().getOpAssignment_1_1()); 
            // InternalCQL.g:1972:2: ( rule__Comparison__OpAssignment_1_1 )
            // InternalCQL.g:1972:3: rule__Comparison__OpAssignment_1_1
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
    // InternalCQL.g:1980:1: rule__Comparison__Group_1__2 : rule__Comparison__Group_1__2__Impl ;
    public final void rule__Comparison__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1984:1: ( rule__Comparison__Group_1__2__Impl )
            // InternalCQL.g:1985:2: rule__Comparison__Group_1__2__Impl
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
    // InternalCQL.g:1991:1: rule__Comparison__Group_1__2__Impl : ( ( rule__Comparison__RightAssignment_1_2 ) ) ;
    public final void rule__Comparison__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1995:1: ( ( ( rule__Comparison__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1996:1: ( ( rule__Comparison__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1996:1: ( ( rule__Comparison__RightAssignment_1_2 ) )
            // InternalCQL.g:1997:2: ( rule__Comparison__RightAssignment_1_2 )
            {
             before(grammarAccess.getComparisonAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1998:2: ( rule__Comparison__RightAssignment_1_2 )
            // InternalCQL.g:1998:3: rule__Comparison__RightAssignment_1_2
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
    // InternalCQL.g:2007:1: rule__PlusOrMinus__Group__0 : rule__PlusOrMinus__Group__0__Impl rule__PlusOrMinus__Group__1 ;
    public final void rule__PlusOrMinus__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2011:1: ( rule__PlusOrMinus__Group__0__Impl rule__PlusOrMinus__Group__1 )
            // InternalCQL.g:2012:2: rule__PlusOrMinus__Group__0__Impl rule__PlusOrMinus__Group__1
            {
            pushFollow(FOLLOW_23);
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
    // InternalCQL.g:2019:1: rule__PlusOrMinus__Group__0__Impl : ( ruleMulOrDiv ) ;
    public final void rule__PlusOrMinus__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2023:1: ( ( ruleMulOrDiv ) )
            // InternalCQL.g:2024:1: ( ruleMulOrDiv )
            {
            // InternalCQL.g:2024:1: ( ruleMulOrDiv )
            // InternalCQL.g:2025:2: ruleMulOrDiv
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
    // InternalCQL.g:2034:1: rule__PlusOrMinus__Group__1 : rule__PlusOrMinus__Group__1__Impl ;
    public final void rule__PlusOrMinus__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2038:1: ( rule__PlusOrMinus__Group__1__Impl )
            // InternalCQL.g:2039:2: rule__PlusOrMinus__Group__1__Impl
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
    // InternalCQL.g:2045:1: rule__PlusOrMinus__Group__1__Impl : ( ( rule__PlusOrMinus__Group_1__0 )* ) ;
    public final void rule__PlusOrMinus__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2049:1: ( ( ( rule__PlusOrMinus__Group_1__0 )* ) )
            // InternalCQL.g:2050:1: ( ( rule__PlusOrMinus__Group_1__0 )* )
            {
            // InternalCQL.g:2050:1: ( ( rule__PlusOrMinus__Group_1__0 )* )
            // InternalCQL.g:2051:2: ( rule__PlusOrMinus__Group_1__0 )*
            {
             before(grammarAccess.getPlusOrMinusAccess().getGroup_1()); 
            // InternalCQL.g:2052:2: ( rule__PlusOrMinus__Group_1__0 )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( ((LA22_0>=37 && LA22_0<=38)) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // InternalCQL.g:2052:3: rule__PlusOrMinus__Group_1__0
            	    {
            	    pushFollow(FOLLOW_24);
            	    rule__PlusOrMinus__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop22;
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
    // InternalCQL.g:2061:1: rule__PlusOrMinus__Group_1__0 : rule__PlusOrMinus__Group_1__0__Impl rule__PlusOrMinus__Group_1__1 ;
    public final void rule__PlusOrMinus__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2065:1: ( rule__PlusOrMinus__Group_1__0__Impl rule__PlusOrMinus__Group_1__1 )
            // InternalCQL.g:2066:2: rule__PlusOrMinus__Group_1__0__Impl rule__PlusOrMinus__Group_1__1
            {
            pushFollow(FOLLOW_13);
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
    // InternalCQL.g:2073:1: rule__PlusOrMinus__Group_1__0__Impl : ( ( rule__PlusOrMinus__Alternatives_1_0 ) ) ;
    public final void rule__PlusOrMinus__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2077:1: ( ( ( rule__PlusOrMinus__Alternatives_1_0 ) ) )
            // InternalCQL.g:2078:1: ( ( rule__PlusOrMinus__Alternatives_1_0 ) )
            {
            // InternalCQL.g:2078:1: ( ( rule__PlusOrMinus__Alternatives_1_0 ) )
            // InternalCQL.g:2079:2: ( rule__PlusOrMinus__Alternatives_1_0 )
            {
             before(grammarAccess.getPlusOrMinusAccess().getAlternatives_1_0()); 
            // InternalCQL.g:2080:2: ( rule__PlusOrMinus__Alternatives_1_0 )
            // InternalCQL.g:2080:3: rule__PlusOrMinus__Alternatives_1_0
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
    // InternalCQL.g:2088:1: rule__PlusOrMinus__Group_1__1 : rule__PlusOrMinus__Group_1__1__Impl ;
    public final void rule__PlusOrMinus__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2092:1: ( rule__PlusOrMinus__Group_1__1__Impl )
            // InternalCQL.g:2093:2: rule__PlusOrMinus__Group_1__1__Impl
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
    // InternalCQL.g:2099:1: rule__PlusOrMinus__Group_1__1__Impl : ( ( rule__PlusOrMinus__RightAssignment_1_1 ) ) ;
    public final void rule__PlusOrMinus__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2103:1: ( ( ( rule__PlusOrMinus__RightAssignment_1_1 ) ) )
            // InternalCQL.g:2104:1: ( ( rule__PlusOrMinus__RightAssignment_1_1 ) )
            {
            // InternalCQL.g:2104:1: ( ( rule__PlusOrMinus__RightAssignment_1_1 ) )
            // InternalCQL.g:2105:2: ( rule__PlusOrMinus__RightAssignment_1_1 )
            {
             before(grammarAccess.getPlusOrMinusAccess().getRightAssignment_1_1()); 
            // InternalCQL.g:2106:2: ( rule__PlusOrMinus__RightAssignment_1_1 )
            // InternalCQL.g:2106:3: rule__PlusOrMinus__RightAssignment_1_1
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
    // InternalCQL.g:2115:1: rule__PlusOrMinus__Group_1_0_0__0 : rule__PlusOrMinus__Group_1_0_0__0__Impl rule__PlusOrMinus__Group_1_0_0__1 ;
    public final void rule__PlusOrMinus__Group_1_0_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2119:1: ( rule__PlusOrMinus__Group_1_0_0__0__Impl rule__PlusOrMinus__Group_1_0_0__1 )
            // InternalCQL.g:2120:2: rule__PlusOrMinus__Group_1_0_0__0__Impl rule__PlusOrMinus__Group_1_0_0__1
            {
            pushFollow(FOLLOW_25);
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
    // InternalCQL.g:2127:1: rule__PlusOrMinus__Group_1_0_0__0__Impl : ( () ) ;
    public final void rule__PlusOrMinus__Group_1_0_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2131:1: ( ( () ) )
            // InternalCQL.g:2132:1: ( () )
            {
            // InternalCQL.g:2132:1: ( () )
            // InternalCQL.g:2133:2: ()
            {
             before(grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0()); 
            // InternalCQL.g:2134:2: ()
            // InternalCQL.g:2134:3: 
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
    // InternalCQL.g:2142:1: rule__PlusOrMinus__Group_1_0_0__1 : rule__PlusOrMinus__Group_1_0_0__1__Impl ;
    public final void rule__PlusOrMinus__Group_1_0_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2146:1: ( rule__PlusOrMinus__Group_1_0_0__1__Impl )
            // InternalCQL.g:2147:2: rule__PlusOrMinus__Group_1_0_0__1__Impl
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
    // InternalCQL.g:2153:1: rule__PlusOrMinus__Group_1_0_0__1__Impl : ( '+' ) ;
    public final void rule__PlusOrMinus__Group_1_0_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2157:1: ( ( '+' ) )
            // InternalCQL.g:2158:1: ( '+' )
            {
            // InternalCQL.g:2158:1: ( '+' )
            // InternalCQL.g:2159:2: '+'
            {
             before(grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1()); 
            match(input,37,FOLLOW_2); 
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
    // InternalCQL.g:2169:1: rule__PlusOrMinus__Group_1_0_1__0 : rule__PlusOrMinus__Group_1_0_1__0__Impl rule__PlusOrMinus__Group_1_0_1__1 ;
    public final void rule__PlusOrMinus__Group_1_0_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2173:1: ( rule__PlusOrMinus__Group_1_0_1__0__Impl rule__PlusOrMinus__Group_1_0_1__1 )
            // InternalCQL.g:2174:2: rule__PlusOrMinus__Group_1_0_1__0__Impl rule__PlusOrMinus__Group_1_0_1__1
            {
            pushFollow(FOLLOW_23);
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
    // InternalCQL.g:2181:1: rule__PlusOrMinus__Group_1_0_1__0__Impl : ( () ) ;
    public final void rule__PlusOrMinus__Group_1_0_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2185:1: ( ( () ) )
            // InternalCQL.g:2186:1: ( () )
            {
            // InternalCQL.g:2186:1: ( () )
            // InternalCQL.g:2187:2: ()
            {
             before(grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0()); 
            // InternalCQL.g:2188:2: ()
            // InternalCQL.g:2188:3: 
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
    // InternalCQL.g:2196:1: rule__PlusOrMinus__Group_1_0_1__1 : rule__PlusOrMinus__Group_1_0_1__1__Impl ;
    public final void rule__PlusOrMinus__Group_1_0_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2200:1: ( rule__PlusOrMinus__Group_1_0_1__1__Impl )
            // InternalCQL.g:2201:2: rule__PlusOrMinus__Group_1_0_1__1__Impl
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
    // InternalCQL.g:2207:1: rule__PlusOrMinus__Group_1_0_1__1__Impl : ( '-' ) ;
    public final void rule__PlusOrMinus__Group_1_0_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2211:1: ( ( '-' ) )
            // InternalCQL.g:2212:1: ( '-' )
            {
            // InternalCQL.g:2212:1: ( '-' )
            // InternalCQL.g:2213:2: '-'
            {
             before(grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1()); 
            match(input,38,FOLLOW_2); 
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
    // InternalCQL.g:2223:1: rule__MulOrDiv__Group__0 : rule__MulOrDiv__Group__0__Impl rule__MulOrDiv__Group__1 ;
    public final void rule__MulOrDiv__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2227:1: ( rule__MulOrDiv__Group__0__Impl rule__MulOrDiv__Group__1 )
            // InternalCQL.g:2228:2: rule__MulOrDiv__Group__0__Impl rule__MulOrDiv__Group__1
            {
            pushFollow(FOLLOW_26);
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
    // InternalCQL.g:2235:1: rule__MulOrDiv__Group__0__Impl : ( rulePrimary ) ;
    public final void rule__MulOrDiv__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2239:1: ( ( rulePrimary ) )
            // InternalCQL.g:2240:1: ( rulePrimary )
            {
            // InternalCQL.g:2240:1: ( rulePrimary )
            // InternalCQL.g:2241:2: rulePrimary
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
    // InternalCQL.g:2250:1: rule__MulOrDiv__Group__1 : rule__MulOrDiv__Group__1__Impl ;
    public final void rule__MulOrDiv__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2254:1: ( rule__MulOrDiv__Group__1__Impl )
            // InternalCQL.g:2255:2: rule__MulOrDiv__Group__1__Impl
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
    // InternalCQL.g:2261:1: rule__MulOrDiv__Group__1__Impl : ( ( rule__MulOrDiv__Group_1__0 )* ) ;
    public final void rule__MulOrDiv__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2265:1: ( ( ( rule__MulOrDiv__Group_1__0 )* ) )
            // InternalCQL.g:2266:1: ( ( rule__MulOrDiv__Group_1__0 )* )
            {
            // InternalCQL.g:2266:1: ( ( rule__MulOrDiv__Group_1__0 )* )
            // InternalCQL.g:2267:2: ( rule__MulOrDiv__Group_1__0 )*
            {
             before(grammarAccess.getMulOrDivAccess().getGroup_1()); 
            // InternalCQL.g:2268:2: ( rule__MulOrDiv__Group_1__0 )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( ((LA23_0>=28 && LA23_0<=29)) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // InternalCQL.g:2268:3: rule__MulOrDiv__Group_1__0
            	    {
            	    pushFollow(FOLLOW_27);
            	    rule__MulOrDiv__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop23;
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
    // InternalCQL.g:2277:1: rule__MulOrDiv__Group_1__0 : rule__MulOrDiv__Group_1__0__Impl rule__MulOrDiv__Group_1__1 ;
    public final void rule__MulOrDiv__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2281:1: ( rule__MulOrDiv__Group_1__0__Impl rule__MulOrDiv__Group_1__1 )
            // InternalCQL.g:2282:2: rule__MulOrDiv__Group_1__0__Impl rule__MulOrDiv__Group_1__1
            {
            pushFollow(FOLLOW_26);
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
    // InternalCQL.g:2289:1: rule__MulOrDiv__Group_1__0__Impl : ( () ) ;
    public final void rule__MulOrDiv__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2293:1: ( ( () ) )
            // InternalCQL.g:2294:1: ( () )
            {
            // InternalCQL.g:2294:1: ( () )
            // InternalCQL.g:2295:2: ()
            {
             before(grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0()); 
            // InternalCQL.g:2296:2: ()
            // InternalCQL.g:2296:3: 
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
    // InternalCQL.g:2304:1: rule__MulOrDiv__Group_1__1 : rule__MulOrDiv__Group_1__1__Impl rule__MulOrDiv__Group_1__2 ;
    public final void rule__MulOrDiv__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2308:1: ( rule__MulOrDiv__Group_1__1__Impl rule__MulOrDiv__Group_1__2 )
            // InternalCQL.g:2309:2: rule__MulOrDiv__Group_1__1__Impl rule__MulOrDiv__Group_1__2
            {
            pushFollow(FOLLOW_13);
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
    // InternalCQL.g:2316:1: rule__MulOrDiv__Group_1__1__Impl : ( ( rule__MulOrDiv__OpAssignment_1_1 ) ) ;
    public final void rule__MulOrDiv__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2320:1: ( ( ( rule__MulOrDiv__OpAssignment_1_1 ) ) )
            // InternalCQL.g:2321:1: ( ( rule__MulOrDiv__OpAssignment_1_1 ) )
            {
            // InternalCQL.g:2321:1: ( ( rule__MulOrDiv__OpAssignment_1_1 ) )
            // InternalCQL.g:2322:2: ( rule__MulOrDiv__OpAssignment_1_1 )
            {
             before(grammarAccess.getMulOrDivAccess().getOpAssignment_1_1()); 
            // InternalCQL.g:2323:2: ( rule__MulOrDiv__OpAssignment_1_1 )
            // InternalCQL.g:2323:3: rule__MulOrDiv__OpAssignment_1_1
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
    // InternalCQL.g:2331:1: rule__MulOrDiv__Group_1__2 : rule__MulOrDiv__Group_1__2__Impl ;
    public final void rule__MulOrDiv__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2335:1: ( rule__MulOrDiv__Group_1__2__Impl )
            // InternalCQL.g:2336:2: rule__MulOrDiv__Group_1__2__Impl
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
    // InternalCQL.g:2342:1: rule__MulOrDiv__Group_1__2__Impl : ( ( rule__MulOrDiv__RightAssignment_1_2 ) ) ;
    public final void rule__MulOrDiv__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2346:1: ( ( ( rule__MulOrDiv__RightAssignment_1_2 ) ) )
            // InternalCQL.g:2347:1: ( ( rule__MulOrDiv__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:2347:1: ( ( rule__MulOrDiv__RightAssignment_1_2 ) )
            // InternalCQL.g:2348:2: ( rule__MulOrDiv__RightAssignment_1_2 )
            {
             before(grammarAccess.getMulOrDivAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:2349:2: ( rule__MulOrDiv__RightAssignment_1_2 )
            // InternalCQL.g:2349:3: rule__MulOrDiv__RightAssignment_1_2
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
    // InternalCQL.g:2358:1: rule__Primary__Group_0__0 : rule__Primary__Group_0__0__Impl rule__Primary__Group_0__1 ;
    public final void rule__Primary__Group_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2362:1: ( rule__Primary__Group_0__0__Impl rule__Primary__Group_0__1 )
            // InternalCQL.g:2363:2: rule__Primary__Group_0__0__Impl rule__Primary__Group_0__1
            {
            pushFollow(FOLLOW_28);
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
    // InternalCQL.g:2370:1: rule__Primary__Group_0__0__Impl : ( () ) ;
    public final void rule__Primary__Group_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2374:1: ( ( () ) )
            // InternalCQL.g:2375:1: ( () )
            {
            // InternalCQL.g:2375:1: ( () )
            // InternalCQL.g:2376:2: ()
            {
             before(grammarAccess.getPrimaryAccess().getBracketAction_0_0()); 
            // InternalCQL.g:2377:2: ()
            // InternalCQL.g:2377:3: 
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
    // InternalCQL.g:2385:1: rule__Primary__Group_0__1 : rule__Primary__Group_0__1__Impl rule__Primary__Group_0__2 ;
    public final void rule__Primary__Group_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2389:1: ( rule__Primary__Group_0__1__Impl rule__Primary__Group_0__2 )
            // InternalCQL.g:2390:2: rule__Primary__Group_0__1__Impl rule__Primary__Group_0__2
            {
            pushFollow(FOLLOW_13);
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
    // InternalCQL.g:2397:1: rule__Primary__Group_0__1__Impl : ( '(' ) ;
    public final void rule__Primary__Group_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2401:1: ( ( '(' ) )
            // InternalCQL.g:2402:1: ( '(' )
            {
            // InternalCQL.g:2402:1: ( '(' )
            // InternalCQL.g:2403:2: '('
            {
             before(grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1()); 
            match(input,39,FOLLOW_2); 
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
    // InternalCQL.g:2412:1: rule__Primary__Group_0__2 : rule__Primary__Group_0__2__Impl rule__Primary__Group_0__3 ;
    public final void rule__Primary__Group_0__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2416:1: ( rule__Primary__Group_0__2__Impl rule__Primary__Group_0__3 )
            // InternalCQL.g:2417:2: rule__Primary__Group_0__2__Impl rule__Primary__Group_0__3
            {
            pushFollow(FOLLOW_29);
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
    // InternalCQL.g:2424:1: rule__Primary__Group_0__2__Impl : ( ( rule__Primary__InnerAssignment_0_2 ) ) ;
    public final void rule__Primary__Group_0__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2428:1: ( ( ( rule__Primary__InnerAssignment_0_2 ) ) )
            // InternalCQL.g:2429:1: ( ( rule__Primary__InnerAssignment_0_2 ) )
            {
            // InternalCQL.g:2429:1: ( ( rule__Primary__InnerAssignment_0_2 ) )
            // InternalCQL.g:2430:2: ( rule__Primary__InnerAssignment_0_2 )
            {
             before(grammarAccess.getPrimaryAccess().getInnerAssignment_0_2()); 
            // InternalCQL.g:2431:2: ( rule__Primary__InnerAssignment_0_2 )
            // InternalCQL.g:2431:3: rule__Primary__InnerAssignment_0_2
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
    // InternalCQL.g:2439:1: rule__Primary__Group_0__3 : rule__Primary__Group_0__3__Impl ;
    public final void rule__Primary__Group_0__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2443:1: ( rule__Primary__Group_0__3__Impl )
            // InternalCQL.g:2444:2: rule__Primary__Group_0__3__Impl
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
    // InternalCQL.g:2450:1: rule__Primary__Group_0__3__Impl : ( ')' ) ;
    public final void rule__Primary__Group_0__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2454:1: ( ( ')' ) )
            // InternalCQL.g:2455:1: ( ')' )
            {
            // InternalCQL.g:2455:1: ( ')' )
            // InternalCQL.g:2456:2: ')'
            {
             before(grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_0_3()); 
            match(input,40,FOLLOW_2); 
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
    // InternalCQL.g:2466:1: rule__Primary__Group_1__0 : rule__Primary__Group_1__0__Impl rule__Primary__Group_1__1 ;
    public final void rule__Primary__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2470:1: ( rule__Primary__Group_1__0__Impl rule__Primary__Group_1__1 )
            // InternalCQL.g:2471:2: rule__Primary__Group_1__0__Impl rule__Primary__Group_1__1
            {
            pushFollow(FOLLOW_30);
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
    // InternalCQL.g:2478:1: rule__Primary__Group_1__0__Impl : ( () ) ;
    public final void rule__Primary__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2482:1: ( ( () ) )
            // InternalCQL.g:2483:1: ( () )
            {
            // InternalCQL.g:2483:1: ( () )
            // InternalCQL.g:2484:2: ()
            {
             before(grammarAccess.getPrimaryAccess().getNOTAction_1_0()); 
            // InternalCQL.g:2485:2: ()
            // InternalCQL.g:2485:3: 
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
    // InternalCQL.g:2493:1: rule__Primary__Group_1__1 : rule__Primary__Group_1__1__Impl rule__Primary__Group_1__2 ;
    public final void rule__Primary__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2497:1: ( rule__Primary__Group_1__1__Impl rule__Primary__Group_1__2 )
            // InternalCQL.g:2498:2: rule__Primary__Group_1__1__Impl rule__Primary__Group_1__2
            {
            pushFollow(FOLLOW_13);
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
    // InternalCQL.g:2505:1: rule__Primary__Group_1__1__Impl : ( 'NOT' ) ;
    public final void rule__Primary__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2509:1: ( ( 'NOT' ) )
            // InternalCQL.g:2510:1: ( 'NOT' )
            {
            // InternalCQL.g:2510:1: ( 'NOT' )
            // InternalCQL.g:2511:2: 'NOT'
            {
             before(grammarAccess.getPrimaryAccess().getNOTKeyword_1_1()); 
            match(input,41,FOLLOW_2); 
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
    // InternalCQL.g:2520:1: rule__Primary__Group_1__2 : rule__Primary__Group_1__2__Impl ;
    public final void rule__Primary__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2524:1: ( rule__Primary__Group_1__2__Impl )
            // InternalCQL.g:2525:2: rule__Primary__Group_1__2__Impl
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
    // InternalCQL.g:2531:1: rule__Primary__Group_1__2__Impl : ( ( rule__Primary__ExpressionAssignment_1_2 ) ) ;
    public final void rule__Primary__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2535:1: ( ( ( rule__Primary__ExpressionAssignment_1_2 ) ) )
            // InternalCQL.g:2536:1: ( ( rule__Primary__ExpressionAssignment_1_2 ) )
            {
            // InternalCQL.g:2536:1: ( ( rule__Primary__ExpressionAssignment_1_2 ) )
            // InternalCQL.g:2537:2: ( rule__Primary__ExpressionAssignment_1_2 )
            {
             before(grammarAccess.getPrimaryAccess().getExpressionAssignment_1_2()); 
            // InternalCQL.g:2538:2: ( rule__Primary__ExpressionAssignment_1_2 )
            // InternalCQL.g:2538:3: rule__Primary__ExpressionAssignment_1_2
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
    // InternalCQL.g:2547:1: rule__Select_Statement__Group__0 : rule__Select_Statement__Group__0__Impl rule__Select_Statement__Group__1 ;
    public final void rule__Select_Statement__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2551:1: ( rule__Select_Statement__Group__0__Impl rule__Select_Statement__Group__1 )
            // InternalCQL.g:2552:2: rule__Select_Statement__Group__0__Impl rule__Select_Statement__Group__1
            {
            pushFollow(FOLLOW_31);
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
    // InternalCQL.g:2559:1: rule__Select_Statement__Group__0__Impl : ( ( rule__Select_Statement__NameAssignment_0 ) ) ;
    public final void rule__Select_Statement__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2563:1: ( ( ( rule__Select_Statement__NameAssignment_0 ) ) )
            // InternalCQL.g:2564:1: ( ( rule__Select_Statement__NameAssignment_0 ) )
            {
            // InternalCQL.g:2564:1: ( ( rule__Select_Statement__NameAssignment_0 ) )
            // InternalCQL.g:2565:2: ( rule__Select_Statement__NameAssignment_0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getNameAssignment_0()); 
            // InternalCQL.g:2566:2: ( rule__Select_Statement__NameAssignment_0 )
            // InternalCQL.g:2566:3: rule__Select_Statement__NameAssignment_0
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
    // InternalCQL.g:2574:1: rule__Select_Statement__Group__1 : rule__Select_Statement__Group__1__Impl rule__Select_Statement__Group__2 ;
    public final void rule__Select_Statement__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2578:1: ( rule__Select_Statement__Group__1__Impl rule__Select_Statement__Group__2 )
            // InternalCQL.g:2579:2: rule__Select_Statement__Group__1__Impl rule__Select_Statement__Group__2
            {
            pushFollow(FOLLOW_31);
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
    // InternalCQL.g:2586:1: rule__Select_Statement__Group__1__Impl : ( ( 'DISTINCT' )? ) ;
    public final void rule__Select_Statement__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2590:1: ( ( ( 'DISTINCT' )? ) )
            // InternalCQL.g:2591:1: ( ( 'DISTINCT' )? )
            {
            // InternalCQL.g:2591:1: ( ( 'DISTINCT' )? )
            // InternalCQL.g:2592:2: ( 'DISTINCT' )?
            {
             before(grammarAccess.getSelect_StatementAccess().getDISTINCTKeyword_1()); 
            // InternalCQL.g:2593:2: ( 'DISTINCT' )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==42) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // InternalCQL.g:2593:3: 'DISTINCT'
                    {
                    match(input,42,FOLLOW_2); 

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
    // InternalCQL.g:2601:1: rule__Select_Statement__Group__2 : rule__Select_Statement__Group__2__Impl rule__Select_Statement__Group__3 ;
    public final void rule__Select_Statement__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2605:1: ( rule__Select_Statement__Group__2__Impl rule__Select_Statement__Group__3 )
            // InternalCQL.g:2606:2: rule__Select_Statement__Group__2__Impl rule__Select_Statement__Group__3
            {
            pushFollow(FOLLOW_32);
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
    // InternalCQL.g:2613:1: rule__Select_Statement__Group__2__Impl : ( ( rule__Select_Statement__Alternatives_2 ) ) ;
    public final void rule__Select_Statement__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2617:1: ( ( ( rule__Select_Statement__Alternatives_2 ) ) )
            // InternalCQL.g:2618:1: ( ( rule__Select_Statement__Alternatives_2 ) )
            {
            // InternalCQL.g:2618:1: ( ( rule__Select_Statement__Alternatives_2 ) )
            // InternalCQL.g:2619:2: ( rule__Select_Statement__Alternatives_2 )
            {
             before(grammarAccess.getSelect_StatementAccess().getAlternatives_2()); 
            // InternalCQL.g:2620:2: ( rule__Select_Statement__Alternatives_2 )
            // InternalCQL.g:2620:3: rule__Select_Statement__Alternatives_2
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
    // InternalCQL.g:2628:1: rule__Select_Statement__Group__3 : rule__Select_Statement__Group__3__Impl rule__Select_Statement__Group__4 ;
    public final void rule__Select_Statement__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2632:1: ( rule__Select_Statement__Group__3__Impl rule__Select_Statement__Group__4 )
            // InternalCQL.g:2633:2: rule__Select_Statement__Group__3__Impl rule__Select_Statement__Group__4
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
    // InternalCQL.g:2640:1: rule__Select_Statement__Group__3__Impl : ( 'FROM' ) ;
    public final void rule__Select_Statement__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2644:1: ( ( 'FROM' ) )
            // InternalCQL.g:2645:1: ( 'FROM' )
            {
            // InternalCQL.g:2645:1: ( 'FROM' )
            // InternalCQL.g:2646:2: 'FROM'
            {
             before(grammarAccess.getSelect_StatementAccess().getFROMKeyword_3()); 
            match(input,43,FOLLOW_2); 
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
    // InternalCQL.g:2655:1: rule__Select_Statement__Group__4 : rule__Select_Statement__Group__4__Impl rule__Select_Statement__Group__5 ;
    public final void rule__Select_Statement__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2659:1: ( rule__Select_Statement__Group__4__Impl rule__Select_Statement__Group__5 )
            // InternalCQL.g:2660:2: rule__Select_Statement__Group__4__Impl rule__Select_Statement__Group__5
            {
            pushFollow(FOLLOW_33);
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
    // InternalCQL.g:2667:1: rule__Select_Statement__Group__4__Impl : ( ( rule__Select_Statement__Group_4__0 ) ) ;
    public final void rule__Select_Statement__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2671:1: ( ( ( rule__Select_Statement__Group_4__0 ) ) )
            // InternalCQL.g:2672:1: ( ( rule__Select_Statement__Group_4__0 ) )
            {
            // InternalCQL.g:2672:1: ( ( rule__Select_Statement__Group_4__0 ) )
            // InternalCQL.g:2673:2: ( rule__Select_Statement__Group_4__0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_4()); 
            // InternalCQL.g:2674:2: ( rule__Select_Statement__Group_4__0 )
            // InternalCQL.g:2674:3: rule__Select_Statement__Group_4__0
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
    // InternalCQL.g:2682:1: rule__Select_Statement__Group__5 : rule__Select_Statement__Group__5__Impl ;
    public final void rule__Select_Statement__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2686:1: ( rule__Select_Statement__Group__5__Impl )
            // InternalCQL.g:2687:2: rule__Select_Statement__Group__5__Impl
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
    // InternalCQL.g:2693:1: rule__Select_Statement__Group__5__Impl : ( ( rule__Select_Statement__Group_5__0 )? ) ;
    public final void rule__Select_Statement__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2697:1: ( ( ( rule__Select_Statement__Group_5__0 )? ) )
            // InternalCQL.g:2698:1: ( ( rule__Select_Statement__Group_5__0 )? )
            {
            // InternalCQL.g:2698:1: ( ( rule__Select_Statement__Group_5__0 )? )
            // InternalCQL.g:2699:2: ( rule__Select_Statement__Group_5__0 )?
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_5()); 
            // InternalCQL.g:2700:2: ( rule__Select_Statement__Group_5__0 )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==45) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // InternalCQL.g:2700:3: rule__Select_Statement__Group_5__0
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
    // InternalCQL.g:2709:1: rule__Select_Statement__Group_2_1__0 : rule__Select_Statement__Group_2_1__0__Impl rule__Select_Statement__Group_2_1__1 ;
    public final void rule__Select_Statement__Group_2_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2713:1: ( rule__Select_Statement__Group_2_1__0__Impl rule__Select_Statement__Group_2_1__1 )
            // InternalCQL.g:2714:2: rule__Select_Statement__Group_2_1__0__Impl rule__Select_Statement__Group_2_1__1
            {
            pushFollow(FOLLOW_34);
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
    // InternalCQL.g:2721:1: rule__Select_Statement__Group_2_1__0__Impl : ( ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) ) ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* ) ) ;
    public final void rule__Select_Statement__Group_2_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2725:1: ( ( ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) ) ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* ) ) )
            // InternalCQL.g:2726:1: ( ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) ) ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* ) )
            {
            // InternalCQL.g:2726:1: ( ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) ) ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* ) )
            // InternalCQL.g:2727:2: ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) ) ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* )
            {
            // InternalCQL.g:2727:2: ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) )
            // InternalCQL.g:2728:3: ( rule__Select_Statement__AttributesAssignment_2_1_0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2_1_0()); 
            // InternalCQL.g:2729:3: ( rule__Select_Statement__AttributesAssignment_2_1_0 )
            // InternalCQL.g:2729:4: rule__Select_Statement__AttributesAssignment_2_1_0
            {
            pushFollow(FOLLOW_35);
            rule__Select_Statement__AttributesAssignment_2_1_0();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2_1_0()); 

            }

            // InternalCQL.g:2732:2: ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* )
            // InternalCQL.g:2733:3: ( rule__Select_Statement__AttributesAssignment_2_1_0 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2_1_0()); 
            // InternalCQL.g:2734:3: ( rule__Select_Statement__AttributesAssignment_2_1_0 )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( (LA26_0==RULE_ID) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // InternalCQL.g:2734:4: rule__Select_Statement__AttributesAssignment_2_1_0
            	    {
            	    pushFollow(FOLLOW_35);
            	    rule__Select_Statement__AttributesAssignment_2_1_0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop26;
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
    // InternalCQL.g:2743:1: rule__Select_Statement__Group_2_1__1 : rule__Select_Statement__Group_2_1__1__Impl ;
    public final void rule__Select_Statement__Group_2_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2747:1: ( rule__Select_Statement__Group_2_1__1__Impl )
            // InternalCQL.g:2748:2: rule__Select_Statement__Group_2_1__1__Impl
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
    // InternalCQL.g:2754:1: rule__Select_Statement__Group_2_1__1__Impl : ( ( rule__Select_Statement__Group_2_1_1__0 )* ) ;
    public final void rule__Select_Statement__Group_2_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2758:1: ( ( ( rule__Select_Statement__Group_2_1_1__0 )* ) )
            // InternalCQL.g:2759:1: ( ( rule__Select_Statement__Group_2_1_1__0 )* )
            {
            // InternalCQL.g:2759:1: ( ( rule__Select_Statement__Group_2_1_1__0 )* )
            // InternalCQL.g:2760:2: ( rule__Select_Statement__Group_2_1_1__0 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_2_1_1()); 
            // InternalCQL.g:2761:2: ( rule__Select_Statement__Group_2_1_1__0 )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0==44) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // InternalCQL.g:2761:3: rule__Select_Statement__Group_2_1_1__0
            	    {
            	    pushFollow(FOLLOW_36);
            	    rule__Select_Statement__Group_2_1_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop27;
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
    // InternalCQL.g:2770:1: rule__Select_Statement__Group_2_1_1__0 : rule__Select_Statement__Group_2_1_1__0__Impl rule__Select_Statement__Group_2_1_1__1 ;
    public final void rule__Select_Statement__Group_2_1_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2774:1: ( rule__Select_Statement__Group_2_1_1__0__Impl rule__Select_Statement__Group_2_1_1__1 )
            // InternalCQL.g:2775:2: rule__Select_Statement__Group_2_1_1__0__Impl rule__Select_Statement__Group_2_1_1__1
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
    // InternalCQL.g:2782:1: rule__Select_Statement__Group_2_1_1__0__Impl : ( ',' ) ;
    public final void rule__Select_Statement__Group_2_1_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2786:1: ( ( ',' ) )
            // InternalCQL.g:2787:1: ( ',' )
            {
            // InternalCQL.g:2787:1: ( ',' )
            // InternalCQL.g:2788:2: ','
            {
             before(grammarAccess.getSelect_StatementAccess().getCommaKeyword_2_1_1_0()); 
            match(input,44,FOLLOW_2); 
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
    // InternalCQL.g:2797:1: rule__Select_Statement__Group_2_1_1__1 : rule__Select_Statement__Group_2_1_1__1__Impl ;
    public final void rule__Select_Statement__Group_2_1_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2801:1: ( rule__Select_Statement__Group_2_1_1__1__Impl )
            // InternalCQL.g:2802:2: rule__Select_Statement__Group_2_1_1__1__Impl
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
    // InternalCQL.g:2808:1: rule__Select_Statement__Group_2_1_1__1__Impl : ( ( rule__Select_Statement__AttributesAssignment_2_1_1_1 ) ) ;
    public final void rule__Select_Statement__Group_2_1_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2812:1: ( ( ( rule__Select_Statement__AttributesAssignment_2_1_1_1 ) ) )
            // InternalCQL.g:2813:1: ( ( rule__Select_Statement__AttributesAssignment_2_1_1_1 ) )
            {
            // InternalCQL.g:2813:1: ( ( rule__Select_Statement__AttributesAssignment_2_1_1_1 ) )
            // InternalCQL.g:2814:2: ( rule__Select_Statement__AttributesAssignment_2_1_1_1 )
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2_1_1_1()); 
            // InternalCQL.g:2815:2: ( rule__Select_Statement__AttributesAssignment_2_1_1_1 )
            // InternalCQL.g:2815:3: rule__Select_Statement__AttributesAssignment_2_1_1_1
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
    // InternalCQL.g:2824:1: rule__Select_Statement__Group_4__0 : rule__Select_Statement__Group_4__0__Impl rule__Select_Statement__Group_4__1 ;
    public final void rule__Select_Statement__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2828:1: ( rule__Select_Statement__Group_4__0__Impl rule__Select_Statement__Group_4__1 )
            // InternalCQL.g:2829:2: rule__Select_Statement__Group_4__0__Impl rule__Select_Statement__Group_4__1
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
    // InternalCQL.g:2836:1: rule__Select_Statement__Group_4__0__Impl : ( ( ( rule__Select_Statement__SourcesAssignment_4_0 ) ) ( ( rule__Select_Statement__SourcesAssignment_4_0 )* ) ) ;
    public final void rule__Select_Statement__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2840:1: ( ( ( ( rule__Select_Statement__SourcesAssignment_4_0 ) ) ( ( rule__Select_Statement__SourcesAssignment_4_0 )* ) ) )
            // InternalCQL.g:2841:1: ( ( ( rule__Select_Statement__SourcesAssignment_4_0 ) ) ( ( rule__Select_Statement__SourcesAssignment_4_0 )* ) )
            {
            // InternalCQL.g:2841:1: ( ( ( rule__Select_Statement__SourcesAssignment_4_0 ) ) ( ( rule__Select_Statement__SourcesAssignment_4_0 )* ) )
            // InternalCQL.g:2842:2: ( ( rule__Select_Statement__SourcesAssignment_4_0 ) ) ( ( rule__Select_Statement__SourcesAssignment_4_0 )* )
            {
            // InternalCQL.g:2842:2: ( ( rule__Select_Statement__SourcesAssignment_4_0 ) )
            // InternalCQL.g:2843:3: ( rule__Select_Statement__SourcesAssignment_4_0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_0()); 
            // InternalCQL.g:2844:3: ( rule__Select_Statement__SourcesAssignment_4_0 )
            // InternalCQL.g:2844:4: rule__Select_Statement__SourcesAssignment_4_0
            {
            pushFollow(FOLLOW_37);
            rule__Select_Statement__SourcesAssignment_4_0();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_0()); 

            }

            // InternalCQL.g:2847:2: ( ( rule__Select_Statement__SourcesAssignment_4_0 )* )
            // InternalCQL.g:2848:3: ( rule__Select_Statement__SourcesAssignment_4_0 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_0()); 
            // InternalCQL.g:2849:3: ( rule__Select_Statement__SourcesAssignment_4_0 )*
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( (LA28_0==RULE_ID) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // InternalCQL.g:2849:4: rule__Select_Statement__SourcesAssignment_4_0
            	    {
            	    pushFollow(FOLLOW_37);
            	    rule__Select_Statement__SourcesAssignment_4_0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop28;
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
    // InternalCQL.g:2858:1: rule__Select_Statement__Group_4__1 : rule__Select_Statement__Group_4__1__Impl ;
    public final void rule__Select_Statement__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2862:1: ( rule__Select_Statement__Group_4__1__Impl )
            // InternalCQL.g:2863:2: rule__Select_Statement__Group_4__1__Impl
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
    // InternalCQL.g:2869:1: rule__Select_Statement__Group_4__1__Impl : ( ( rule__Select_Statement__Group_4_1__0 )* ) ;
    public final void rule__Select_Statement__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2873:1: ( ( ( rule__Select_Statement__Group_4_1__0 )* ) )
            // InternalCQL.g:2874:1: ( ( rule__Select_Statement__Group_4_1__0 )* )
            {
            // InternalCQL.g:2874:1: ( ( rule__Select_Statement__Group_4_1__0 )* )
            // InternalCQL.g:2875:2: ( rule__Select_Statement__Group_4_1__0 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_4_1()); 
            // InternalCQL.g:2876:2: ( rule__Select_Statement__Group_4_1__0 )*
            loop29:
            do {
                int alt29=2;
                int LA29_0 = input.LA(1);

                if ( (LA29_0==44) ) {
                    alt29=1;
                }


                switch (alt29) {
            	case 1 :
            	    // InternalCQL.g:2876:3: rule__Select_Statement__Group_4_1__0
            	    {
            	    pushFollow(FOLLOW_36);
            	    rule__Select_Statement__Group_4_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop29;
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
    // InternalCQL.g:2885:1: rule__Select_Statement__Group_4_1__0 : rule__Select_Statement__Group_4_1__0__Impl rule__Select_Statement__Group_4_1__1 ;
    public final void rule__Select_Statement__Group_4_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2889:1: ( rule__Select_Statement__Group_4_1__0__Impl rule__Select_Statement__Group_4_1__1 )
            // InternalCQL.g:2890:2: rule__Select_Statement__Group_4_1__0__Impl rule__Select_Statement__Group_4_1__1
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
    // InternalCQL.g:2897:1: rule__Select_Statement__Group_4_1__0__Impl : ( ',' ) ;
    public final void rule__Select_Statement__Group_4_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2901:1: ( ( ',' ) )
            // InternalCQL.g:2902:1: ( ',' )
            {
            // InternalCQL.g:2902:1: ( ',' )
            // InternalCQL.g:2903:2: ','
            {
             before(grammarAccess.getSelect_StatementAccess().getCommaKeyword_4_1_0()); 
            match(input,44,FOLLOW_2); 
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
    // InternalCQL.g:2912:1: rule__Select_Statement__Group_4_1__1 : rule__Select_Statement__Group_4_1__1__Impl ;
    public final void rule__Select_Statement__Group_4_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2916:1: ( rule__Select_Statement__Group_4_1__1__Impl )
            // InternalCQL.g:2917:2: rule__Select_Statement__Group_4_1__1__Impl
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
    // InternalCQL.g:2923:1: rule__Select_Statement__Group_4_1__1__Impl : ( ( rule__Select_Statement__SourcesAssignment_4_1_1 ) ) ;
    public final void rule__Select_Statement__Group_4_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2927:1: ( ( ( rule__Select_Statement__SourcesAssignment_4_1_1 ) ) )
            // InternalCQL.g:2928:1: ( ( rule__Select_Statement__SourcesAssignment_4_1_1 ) )
            {
            // InternalCQL.g:2928:1: ( ( rule__Select_Statement__SourcesAssignment_4_1_1 ) )
            // InternalCQL.g:2929:2: ( rule__Select_Statement__SourcesAssignment_4_1_1 )
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_1_1()); 
            // InternalCQL.g:2930:2: ( rule__Select_Statement__SourcesAssignment_4_1_1 )
            // InternalCQL.g:2930:3: rule__Select_Statement__SourcesAssignment_4_1_1
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
    // InternalCQL.g:2939:1: rule__Select_Statement__Group_5__0 : rule__Select_Statement__Group_5__0__Impl rule__Select_Statement__Group_5__1 ;
    public final void rule__Select_Statement__Group_5__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2943:1: ( rule__Select_Statement__Group_5__0__Impl rule__Select_Statement__Group_5__1 )
            // InternalCQL.g:2944:2: rule__Select_Statement__Group_5__0__Impl rule__Select_Statement__Group_5__1
            {
            pushFollow(FOLLOW_13);
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
    // InternalCQL.g:2951:1: rule__Select_Statement__Group_5__0__Impl : ( 'WHERE' ) ;
    public final void rule__Select_Statement__Group_5__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2955:1: ( ( 'WHERE' ) )
            // InternalCQL.g:2956:1: ( 'WHERE' )
            {
            // InternalCQL.g:2956:1: ( 'WHERE' )
            // InternalCQL.g:2957:2: 'WHERE'
            {
             before(grammarAccess.getSelect_StatementAccess().getWHEREKeyword_5_0()); 
            match(input,45,FOLLOW_2); 
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
    // InternalCQL.g:2966:1: rule__Select_Statement__Group_5__1 : rule__Select_Statement__Group_5__1__Impl ;
    public final void rule__Select_Statement__Group_5__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2970:1: ( rule__Select_Statement__Group_5__1__Impl )
            // InternalCQL.g:2971:2: rule__Select_Statement__Group_5__1__Impl
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
    // InternalCQL.g:2977:1: rule__Select_Statement__Group_5__1__Impl : ( ( rule__Select_Statement__PredicatesAssignment_5_1 ) ) ;
    public final void rule__Select_Statement__Group_5__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2981:1: ( ( ( rule__Select_Statement__PredicatesAssignment_5_1 ) ) )
            // InternalCQL.g:2982:1: ( ( rule__Select_Statement__PredicatesAssignment_5_1 ) )
            {
            // InternalCQL.g:2982:1: ( ( rule__Select_Statement__PredicatesAssignment_5_1 ) )
            // InternalCQL.g:2983:2: ( rule__Select_Statement__PredicatesAssignment_5_1 )
            {
             before(grammarAccess.getSelect_StatementAccess().getPredicatesAssignment_5_1()); 
            // InternalCQL.g:2984:2: ( rule__Select_Statement__PredicatesAssignment_5_1 )
            // InternalCQL.g:2984:3: rule__Select_Statement__PredicatesAssignment_5_1
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


    // $ANTLR start "rule__Window_Timebased__Group__0"
    // InternalCQL.g:2993:1: rule__Window_Timebased__Group__0 : rule__Window_Timebased__Group__0__Impl rule__Window_Timebased__Group__1 ;
    public final void rule__Window_Timebased__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2997:1: ( rule__Window_Timebased__Group__0__Impl rule__Window_Timebased__Group__1 )
            // InternalCQL.g:2998:2: rule__Window_Timebased__Group__0__Impl rule__Window_Timebased__Group__1
            {
            pushFollow(FOLLOW_5);
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
    // InternalCQL.g:3005:1: rule__Window_Timebased__Group__0__Impl : ( 'SIZE' ) ;
    public final void rule__Window_Timebased__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3009:1: ( ( 'SIZE' ) )
            // InternalCQL.g:3010:1: ( 'SIZE' )
            {
            // InternalCQL.g:3010:1: ( 'SIZE' )
            // InternalCQL.g:3011:2: 'SIZE'
            {
             before(grammarAccess.getWindow_TimebasedAccess().getSIZEKeyword_0()); 
            match(input,46,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getSIZEKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group__0__Impl"


    // $ANTLR start "rule__Window_Timebased__Group__1"
    // InternalCQL.g:3020:1: rule__Window_Timebased__Group__1 : rule__Window_Timebased__Group__1__Impl rule__Window_Timebased__Group__2 ;
    public final void rule__Window_Timebased__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3024:1: ( rule__Window_Timebased__Group__1__Impl rule__Window_Timebased__Group__2 )
            // InternalCQL.g:3025:2: rule__Window_Timebased__Group__1__Impl rule__Window_Timebased__Group__2
            {
            pushFollow(FOLLOW_9);
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
    // InternalCQL.g:3032:1: rule__Window_Timebased__Group__1__Impl : ( ( rule__Window_Timebased__SizeAssignment_1 ) ) ;
    public final void rule__Window_Timebased__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3036:1: ( ( ( rule__Window_Timebased__SizeAssignment_1 ) ) )
            // InternalCQL.g:3037:1: ( ( rule__Window_Timebased__SizeAssignment_1 ) )
            {
            // InternalCQL.g:3037:1: ( ( rule__Window_Timebased__SizeAssignment_1 ) )
            // InternalCQL.g:3038:2: ( rule__Window_Timebased__SizeAssignment_1 )
            {
             before(grammarAccess.getWindow_TimebasedAccess().getSizeAssignment_1()); 
            // InternalCQL.g:3039:2: ( rule__Window_Timebased__SizeAssignment_1 )
            // InternalCQL.g:3039:3: rule__Window_Timebased__SizeAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__Window_Timebased__SizeAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TimebasedAccess().getSizeAssignment_1()); 

            }


            }

        }
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
    // InternalCQL.g:3047:1: rule__Window_Timebased__Group__2 : rule__Window_Timebased__Group__2__Impl rule__Window_Timebased__Group__3 ;
    public final void rule__Window_Timebased__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3051:1: ( rule__Window_Timebased__Group__2__Impl rule__Window_Timebased__Group__3 )
            // InternalCQL.g:3052:2: rule__Window_Timebased__Group__2__Impl rule__Window_Timebased__Group__3
            {
            pushFollow(FOLLOW_38);
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
    // InternalCQL.g:3059:1: rule__Window_Timebased__Group__2__Impl : ( ( rule__Window_Timebased__UnitAssignment_2 ) ) ;
    public final void rule__Window_Timebased__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3063:1: ( ( ( rule__Window_Timebased__UnitAssignment_2 ) ) )
            // InternalCQL.g:3064:1: ( ( rule__Window_Timebased__UnitAssignment_2 ) )
            {
            // InternalCQL.g:3064:1: ( ( rule__Window_Timebased__UnitAssignment_2 ) )
            // InternalCQL.g:3065:2: ( rule__Window_Timebased__UnitAssignment_2 )
            {
             before(grammarAccess.getWindow_TimebasedAccess().getUnitAssignment_2()); 
            // InternalCQL.g:3066:2: ( rule__Window_Timebased__UnitAssignment_2 )
            // InternalCQL.g:3066:3: rule__Window_Timebased__UnitAssignment_2
            {
            pushFollow(FOLLOW_2);
            rule__Window_Timebased__UnitAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TimebasedAccess().getUnitAssignment_2()); 

            }


            }

        }
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
    // InternalCQL.g:3074:1: rule__Window_Timebased__Group__3 : rule__Window_Timebased__Group__3__Impl rule__Window_Timebased__Group__4 ;
    public final void rule__Window_Timebased__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3078:1: ( rule__Window_Timebased__Group__3__Impl rule__Window_Timebased__Group__4 )
            // InternalCQL.g:3079:2: rule__Window_Timebased__Group__3__Impl rule__Window_Timebased__Group__4
            {
            pushFollow(FOLLOW_38);
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
    // InternalCQL.g:3086:1: rule__Window_Timebased__Group__3__Impl : ( ( rule__Window_Timebased__Group_3__0 )? ) ;
    public final void rule__Window_Timebased__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3090:1: ( ( ( rule__Window_Timebased__Group_3__0 )? ) )
            // InternalCQL.g:3091:1: ( ( rule__Window_Timebased__Group_3__0 )? )
            {
            // InternalCQL.g:3091:1: ( ( rule__Window_Timebased__Group_3__0 )? )
            // InternalCQL.g:3092:2: ( rule__Window_Timebased__Group_3__0 )?
            {
             before(grammarAccess.getWindow_TimebasedAccess().getGroup_3()); 
            // InternalCQL.g:3093:2: ( rule__Window_Timebased__Group_3__0 )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==48) ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // InternalCQL.g:3093:3: rule__Window_Timebased__Group_3__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Window_Timebased__Group_3__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getWindow_TimebasedAccess().getGroup_3()); 

            }


            }

        }
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
    // InternalCQL.g:3101:1: rule__Window_Timebased__Group__4 : rule__Window_Timebased__Group__4__Impl ;
    public final void rule__Window_Timebased__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3105:1: ( rule__Window_Timebased__Group__4__Impl )
            // InternalCQL.g:3106:2: rule__Window_Timebased__Group__4__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group__4__Impl();

            state._fsp--;


            }

        }
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
    // InternalCQL.g:3112:1: rule__Window_Timebased__Group__4__Impl : ( 'TIME' ) ;
    public final void rule__Window_Timebased__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3116:1: ( ( 'TIME' ) )
            // InternalCQL.g:3117:1: ( 'TIME' )
            {
            // InternalCQL.g:3117:1: ( 'TIME' )
            // InternalCQL.g:3118:2: 'TIME'
            {
             before(grammarAccess.getWindow_TimebasedAccess().getTIMEKeyword_4()); 
            match(input,47,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getTIMEKeyword_4()); 

            }


            }

        }
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


    // $ANTLR start "rule__Window_Timebased__Group_3__0"
    // InternalCQL.g:3128:1: rule__Window_Timebased__Group_3__0 : rule__Window_Timebased__Group_3__0__Impl rule__Window_Timebased__Group_3__1 ;
    public final void rule__Window_Timebased__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3132:1: ( rule__Window_Timebased__Group_3__0__Impl rule__Window_Timebased__Group_3__1 )
            // InternalCQL.g:3133:2: rule__Window_Timebased__Group_3__0__Impl rule__Window_Timebased__Group_3__1
            {
            pushFollow(FOLLOW_5);
            rule__Window_Timebased__Group_3__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group_3__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_3__0"


    // $ANTLR start "rule__Window_Timebased__Group_3__0__Impl"
    // InternalCQL.g:3140:1: rule__Window_Timebased__Group_3__0__Impl : ( 'ADVANCE' ) ;
    public final void rule__Window_Timebased__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3144:1: ( ( 'ADVANCE' ) )
            // InternalCQL.g:3145:1: ( 'ADVANCE' )
            {
            // InternalCQL.g:3145:1: ( 'ADVANCE' )
            // InternalCQL.g:3146:2: 'ADVANCE'
            {
             before(grammarAccess.getWindow_TimebasedAccess().getADVANCEKeyword_3_0()); 
            match(input,48,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getADVANCEKeyword_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_3__0__Impl"


    // $ANTLR start "rule__Window_Timebased__Group_3__1"
    // InternalCQL.g:3155:1: rule__Window_Timebased__Group_3__1 : rule__Window_Timebased__Group_3__1__Impl rule__Window_Timebased__Group_3__2 ;
    public final void rule__Window_Timebased__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3159:1: ( rule__Window_Timebased__Group_3__1__Impl rule__Window_Timebased__Group_3__2 )
            // InternalCQL.g:3160:2: rule__Window_Timebased__Group_3__1__Impl rule__Window_Timebased__Group_3__2
            {
            pushFollow(FOLLOW_9);
            rule__Window_Timebased__Group_3__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group_3__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_3__1"


    // $ANTLR start "rule__Window_Timebased__Group_3__1__Impl"
    // InternalCQL.g:3167:1: rule__Window_Timebased__Group_3__1__Impl : ( ( rule__Window_Timebased__Advance_sizeAssignment_3_1 ) ) ;
    public final void rule__Window_Timebased__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3171:1: ( ( ( rule__Window_Timebased__Advance_sizeAssignment_3_1 ) ) )
            // InternalCQL.g:3172:1: ( ( rule__Window_Timebased__Advance_sizeAssignment_3_1 ) )
            {
            // InternalCQL.g:3172:1: ( ( rule__Window_Timebased__Advance_sizeAssignment_3_1 ) )
            // InternalCQL.g:3173:2: ( rule__Window_Timebased__Advance_sizeAssignment_3_1 )
            {
             before(grammarAccess.getWindow_TimebasedAccess().getAdvance_sizeAssignment_3_1()); 
            // InternalCQL.g:3174:2: ( rule__Window_Timebased__Advance_sizeAssignment_3_1 )
            // InternalCQL.g:3174:3: rule__Window_Timebased__Advance_sizeAssignment_3_1
            {
            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Advance_sizeAssignment_3_1();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TimebasedAccess().getAdvance_sizeAssignment_3_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_3__1__Impl"


    // $ANTLR start "rule__Window_Timebased__Group_3__2"
    // InternalCQL.g:3182:1: rule__Window_Timebased__Group_3__2 : rule__Window_Timebased__Group_3__2__Impl ;
    public final void rule__Window_Timebased__Group_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3186:1: ( rule__Window_Timebased__Group_3__2__Impl )
            // InternalCQL.g:3187:2: rule__Window_Timebased__Group_3__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Group_3__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_3__2"


    // $ANTLR start "rule__Window_Timebased__Group_3__2__Impl"
    // InternalCQL.g:3193:1: rule__Window_Timebased__Group_3__2__Impl : ( ( rule__Window_Timebased__Advance_unitAssignment_3_2 ) ) ;
    public final void rule__Window_Timebased__Group_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3197:1: ( ( ( rule__Window_Timebased__Advance_unitAssignment_3_2 ) ) )
            // InternalCQL.g:3198:1: ( ( rule__Window_Timebased__Advance_unitAssignment_3_2 ) )
            {
            // InternalCQL.g:3198:1: ( ( rule__Window_Timebased__Advance_unitAssignment_3_2 ) )
            // InternalCQL.g:3199:2: ( rule__Window_Timebased__Advance_unitAssignment_3_2 )
            {
             before(grammarAccess.getWindow_TimebasedAccess().getAdvance_unitAssignment_3_2()); 
            // InternalCQL.g:3200:2: ( rule__Window_Timebased__Advance_unitAssignment_3_2 )
            // InternalCQL.g:3200:3: rule__Window_Timebased__Advance_unitAssignment_3_2
            {
            pushFollow(FOLLOW_2);
            rule__Window_Timebased__Advance_unitAssignment_3_2();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TimebasedAccess().getAdvance_unitAssignment_3_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Group_3__2__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group__0"
    // InternalCQL.g:3209:1: rule__Window_Tuplebased__Group__0 : rule__Window_Tuplebased__Group__0__Impl rule__Window_Tuplebased__Group__1 ;
    public final void rule__Window_Tuplebased__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3213:1: ( rule__Window_Tuplebased__Group__0__Impl rule__Window_Tuplebased__Group__1 )
            // InternalCQL.g:3214:2: rule__Window_Tuplebased__Group__0__Impl rule__Window_Tuplebased__Group__1
            {
            pushFollow(FOLLOW_5);
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
    // InternalCQL.g:3221:1: rule__Window_Tuplebased__Group__0__Impl : ( 'SIZE' ) ;
    public final void rule__Window_Tuplebased__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3225:1: ( ( 'SIZE' ) )
            // InternalCQL.g:3226:1: ( 'SIZE' )
            {
            // InternalCQL.g:3226:1: ( 'SIZE' )
            // InternalCQL.g:3227:2: 'SIZE'
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getSIZEKeyword_0()); 
            match(input,46,FOLLOW_2); 
             after(grammarAccess.getWindow_TuplebasedAccess().getSIZEKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group__0__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group__1"
    // InternalCQL.g:3236:1: rule__Window_Tuplebased__Group__1 : rule__Window_Tuplebased__Group__1__Impl rule__Window_Tuplebased__Group__2 ;
    public final void rule__Window_Tuplebased__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3240:1: ( rule__Window_Tuplebased__Group__1__Impl rule__Window_Tuplebased__Group__2 )
            // InternalCQL.g:3241:2: rule__Window_Tuplebased__Group__1__Impl rule__Window_Tuplebased__Group__2
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
    // InternalCQL.g:3248:1: rule__Window_Tuplebased__Group__1__Impl : ( ( rule__Window_Tuplebased__SizeAssignment_1 ) ) ;
    public final void rule__Window_Tuplebased__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3252:1: ( ( ( rule__Window_Tuplebased__SizeAssignment_1 ) ) )
            // InternalCQL.g:3253:1: ( ( rule__Window_Tuplebased__SizeAssignment_1 ) )
            {
            // InternalCQL.g:3253:1: ( ( rule__Window_Tuplebased__SizeAssignment_1 ) )
            // InternalCQL.g:3254:2: ( rule__Window_Tuplebased__SizeAssignment_1 )
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getSizeAssignment_1()); 
            // InternalCQL.g:3255:2: ( rule__Window_Tuplebased__SizeAssignment_1 )
            // InternalCQL.g:3255:3: rule__Window_Tuplebased__SizeAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__SizeAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TuplebasedAccess().getSizeAssignment_1()); 

            }


            }

        }
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
    // InternalCQL.g:3263:1: rule__Window_Tuplebased__Group__2 : rule__Window_Tuplebased__Group__2__Impl rule__Window_Tuplebased__Group__3 ;
    public final void rule__Window_Tuplebased__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3267:1: ( rule__Window_Tuplebased__Group__2__Impl rule__Window_Tuplebased__Group__3 )
            // InternalCQL.g:3268:2: rule__Window_Tuplebased__Group__2__Impl rule__Window_Tuplebased__Group__3
            {
            pushFollow(FOLLOW_39);
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
    // InternalCQL.g:3275:1: rule__Window_Tuplebased__Group__2__Impl : ( ( rule__Window_Tuplebased__Group_2__0 )? ) ;
    public final void rule__Window_Tuplebased__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3279:1: ( ( ( rule__Window_Tuplebased__Group_2__0 )? ) )
            // InternalCQL.g:3280:1: ( ( rule__Window_Tuplebased__Group_2__0 )? )
            {
            // InternalCQL.g:3280:1: ( ( rule__Window_Tuplebased__Group_2__0 )? )
            // InternalCQL.g:3281:2: ( rule__Window_Tuplebased__Group_2__0 )?
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getGroup_2()); 
            // InternalCQL.g:3282:2: ( rule__Window_Tuplebased__Group_2__0 )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==48) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // InternalCQL.g:3282:3: rule__Window_Tuplebased__Group_2__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Window_Tuplebased__Group_2__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getWindow_TuplebasedAccess().getGroup_2()); 

            }


            }

        }
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
    // InternalCQL.g:3290:1: rule__Window_Tuplebased__Group__3 : rule__Window_Tuplebased__Group__3__Impl rule__Window_Tuplebased__Group__4 ;
    public final void rule__Window_Tuplebased__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3294:1: ( rule__Window_Tuplebased__Group__3__Impl rule__Window_Tuplebased__Group__4 )
            // InternalCQL.g:3295:2: rule__Window_Tuplebased__Group__3__Impl rule__Window_Tuplebased__Group__4
            {
            pushFollow(FOLLOW_40);
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
    // InternalCQL.g:3302:1: rule__Window_Tuplebased__Group__3__Impl : ( 'TUPLE' ) ;
    public final void rule__Window_Tuplebased__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3306:1: ( ( 'TUPLE' ) )
            // InternalCQL.g:3307:1: ( 'TUPLE' )
            {
            // InternalCQL.g:3307:1: ( 'TUPLE' )
            // InternalCQL.g:3308:2: 'TUPLE'
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getTUPLEKeyword_3()); 
            match(input,49,FOLLOW_2); 
             after(grammarAccess.getWindow_TuplebasedAccess().getTUPLEKeyword_3()); 

            }


            }

        }
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
    // InternalCQL.g:3317:1: rule__Window_Tuplebased__Group__4 : rule__Window_Tuplebased__Group__4__Impl ;
    public final void rule__Window_Tuplebased__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3321:1: ( rule__Window_Tuplebased__Group__4__Impl )
            // InternalCQL.g:3322:2: rule__Window_Tuplebased__Group__4__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group__4__Impl();

            state._fsp--;


            }

        }
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
    // InternalCQL.g:3328:1: rule__Window_Tuplebased__Group__4__Impl : ( ( rule__Window_Tuplebased__Group_4__0 )? ) ;
    public final void rule__Window_Tuplebased__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3332:1: ( ( ( rule__Window_Tuplebased__Group_4__0 )? ) )
            // InternalCQL.g:3333:1: ( ( rule__Window_Tuplebased__Group_4__0 )? )
            {
            // InternalCQL.g:3333:1: ( ( rule__Window_Tuplebased__Group_4__0 )? )
            // InternalCQL.g:3334:2: ( rule__Window_Tuplebased__Group_4__0 )?
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getGroup_4()); 
            // InternalCQL.g:3335:2: ( rule__Window_Tuplebased__Group_4__0 )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==50) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // InternalCQL.g:3335:3: rule__Window_Tuplebased__Group_4__0
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


    // $ANTLR start "rule__Window_Tuplebased__Group_2__0"
    // InternalCQL.g:3344:1: rule__Window_Tuplebased__Group_2__0 : rule__Window_Tuplebased__Group_2__0__Impl rule__Window_Tuplebased__Group_2__1 ;
    public final void rule__Window_Tuplebased__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3348:1: ( rule__Window_Tuplebased__Group_2__0__Impl rule__Window_Tuplebased__Group_2__1 )
            // InternalCQL.g:3349:2: rule__Window_Tuplebased__Group_2__0__Impl rule__Window_Tuplebased__Group_2__1
            {
            pushFollow(FOLLOW_5);
            rule__Window_Tuplebased__Group_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group_2__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group_2__0"


    // $ANTLR start "rule__Window_Tuplebased__Group_2__0__Impl"
    // InternalCQL.g:3356:1: rule__Window_Tuplebased__Group_2__0__Impl : ( 'ADVANCE' ) ;
    public final void rule__Window_Tuplebased__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3360:1: ( ( 'ADVANCE' ) )
            // InternalCQL.g:3361:1: ( 'ADVANCE' )
            {
            // InternalCQL.g:3361:1: ( 'ADVANCE' )
            // InternalCQL.g:3362:2: 'ADVANCE'
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getADVANCEKeyword_2_0()); 
            match(input,48,FOLLOW_2); 
             after(grammarAccess.getWindow_TuplebasedAccess().getADVANCEKeyword_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group_2__0__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group_2__1"
    // InternalCQL.g:3371:1: rule__Window_Tuplebased__Group_2__1 : rule__Window_Tuplebased__Group_2__1__Impl ;
    public final void rule__Window_Tuplebased__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3375:1: ( rule__Window_Tuplebased__Group_2__1__Impl )
            // InternalCQL.g:3376:2: rule__Window_Tuplebased__Group_2__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group_2__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group_2__1"


    // $ANTLR start "rule__Window_Tuplebased__Group_2__1__Impl"
    // InternalCQL.g:3382:1: rule__Window_Tuplebased__Group_2__1__Impl : ( ( rule__Window_Tuplebased__Advance_sizeAssignment_2_1 ) ) ;
    public final void rule__Window_Tuplebased__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3386:1: ( ( ( rule__Window_Tuplebased__Advance_sizeAssignment_2_1 ) ) )
            // InternalCQL.g:3387:1: ( ( rule__Window_Tuplebased__Advance_sizeAssignment_2_1 ) )
            {
            // InternalCQL.g:3387:1: ( ( rule__Window_Tuplebased__Advance_sizeAssignment_2_1 ) )
            // InternalCQL.g:3388:2: ( rule__Window_Tuplebased__Advance_sizeAssignment_2_1 )
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getAdvance_sizeAssignment_2_1()); 
            // InternalCQL.g:3389:2: ( rule__Window_Tuplebased__Advance_sizeAssignment_2_1 )
            // InternalCQL.g:3389:3: rule__Window_Tuplebased__Advance_sizeAssignment_2_1
            {
            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Advance_sizeAssignment_2_1();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TuplebasedAccess().getAdvance_sizeAssignment_2_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group_2__1__Impl"


    // $ANTLR start "rule__Window_Tuplebased__Group_4__0"
    // InternalCQL.g:3398:1: rule__Window_Tuplebased__Group_4__0 : rule__Window_Tuplebased__Group_4__0__Impl rule__Window_Tuplebased__Group_4__1 ;
    public final void rule__Window_Tuplebased__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3402:1: ( rule__Window_Tuplebased__Group_4__0__Impl rule__Window_Tuplebased__Group_4__1 )
            // InternalCQL.g:3403:2: rule__Window_Tuplebased__Group_4__0__Impl rule__Window_Tuplebased__Group_4__1
            {
            pushFollow(FOLLOW_41);
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
    // InternalCQL.g:3410:1: rule__Window_Tuplebased__Group_4__0__Impl : ( 'PARTITION' ) ;
    public final void rule__Window_Tuplebased__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3414:1: ( ( 'PARTITION' ) )
            // InternalCQL.g:3415:1: ( 'PARTITION' )
            {
            // InternalCQL.g:3415:1: ( 'PARTITION' )
            // InternalCQL.g:3416:2: 'PARTITION'
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getPARTITIONKeyword_4_0()); 
            match(input,50,FOLLOW_2); 
             after(grammarAccess.getWindow_TuplebasedAccess().getPARTITIONKeyword_4_0()); 

            }


            }

        }
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
    // InternalCQL.g:3425:1: rule__Window_Tuplebased__Group_4__1 : rule__Window_Tuplebased__Group_4__1__Impl rule__Window_Tuplebased__Group_4__2 ;
    public final void rule__Window_Tuplebased__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3429:1: ( rule__Window_Tuplebased__Group_4__1__Impl rule__Window_Tuplebased__Group_4__2 )
            // InternalCQL.g:3430:2: rule__Window_Tuplebased__Group_4__1__Impl rule__Window_Tuplebased__Group_4__2
            {
            pushFollow(FOLLOW_9);
            rule__Window_Tuplebased__Group_4__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group_4__2();

            state._fsp--;


            }

        }
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
    // InternalCQL.g:3437:1: rule__Window_Tuplebased__Group_4__1__Impl : ( 'BY' ) ;
    public final void rule__Window_Tuplebased__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3441:1: ( ( 'BY' ) )
            // InternalCQL.g:3442:1: ( 'BY' )
            {
            // InternalCQL.g:3442:1: ( 'BY' )
            // InternalCQL.g:3443:2: 'BY'
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getBYKeyword_4_1()); 
            match(input,51,FOLLOW_2); 
             after(grammarAccess.getWindow_TuplebasedAccess().getBYKeyword_4_1()); 

            }


            }

        }
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


    // $ANTLR start "rule__Window_Tuplebased__Group_4__2"
    // InternalCQL.g:3452:1: rule__Window_Tuplebased__Group_4__2 : rule__Window_Tuplebased__Group_4__2__Impl ;
    public final void rule__Window_Tuplebased__Group_4__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3456:1: ( rule__Window_Tuplebased__Group_4__2__Impl )
            // InternalCQL.g:3457:2: rule__Window_Tuplebased__Group_4__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Group_4__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group_4__2"


    // $ANTLR start "rule__Window_Tuplebased__Group_4__2__Impl"
    // InternalCQL.g:3463:1: rule__Window_Tuplebased__Group_4__2__Impl : ( ( rule__Window_Tuplebased__Partition_attributeAssignment_4_2 ) ) ;
    public final void rule__Window_Tuplebased__Group_4__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3467:1: ( ( ( rule__Window_Tuplebased__Partition_attributeAssignment_4_2 ) ) )
            // InternalCQL.g:3468:1: ( ( rule__Window_Tuplebased__Partition_attributeAssignment_4_2 ) )
            {
            // InternalCQL.g:3468:1: ( ( rule__Window_Tuplebased__Partition_attributeAssignment_4_2 ) )
            // InternalCQL.g:3469:2: ( rule__Window_Tuplebased__Partition_attributeAssignment_4_2 )
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getPartition_attributeAssignment_4_2()); 
            // InternalCQL.g:3470:2: ( rule__Window_Tuplebased__Partition_attributeAssignment_4_2 )
            // InternalCQL.g:3470:3: rule__Window_Tuplebased__Partition_attributeAssignment_4_2
            {
            pushFollow(FOLLOW_2);
            rule__Window_Tuplebased__Partition_attributeAssignment_4_2();

            state._fsp--;


            }

             after(grammarAccess.getWindow_TuplebasedAccess().getPartition_attributeAssignment_4_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Group_4__2__Impl"


    // $ANTLR start "rule__Create_Statement__Group__0"
    // InternalCQL.g:3479:1: rule__Create_Statement__Group__0 : rule__Create_Statement__Group__0__Impl rule__Create_Statement__Group__1 ;
    public final void rule__Create_Statement__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3483:1: ( rule__Create_Statement__Group__0__Impl rule__Create_Statement__Group__1 )
            // InternalCQL.g:3484:2: rule__Create_Statement__Group__0__Impl rule__Create_Statement__Group__1
            {
            pushFollow(FOLLOW_42);
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
    // InternalCQL.g:3491:1: rule__Create_Statement__Group__0__Impl : ( 'CREATE' ) ;
    public final void rule__Create_Statement__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3495:1: ( ( 'CREATE' ) )
            // InternalCQL.g:3496:1: ( 'CREATE' )
            {
            // InternalCQL.g:3496:1: ( 'CREATE' )
            // InternalCQL.g:3497:2: 'CREATE'
            {
             before(grammarAccess.getCreate_StatementAccess().getCREATEKeyword_0()); 
            match(input,52,FOLLOW_2); 
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
    // InternalCQL.g:3506:1: rule__Create_Statement__Group__1 : rule__Create_Statement__Group__1__Impl ;
    public final void rule__Create_Statement__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3510:1: ( rule__Create_Statement__Group__1__Impl )
            // InternalCQL.g:3511:2: rule__Create_Statement__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group__1__Impl();

            state._fsp--;


            }

        }
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
    // InternalCQL.g:3517:1: rule__Create_Statement__Group__1__Impl : ( ( rule__Create_Statement__Alternatives_1 ) ) ;
    public final void rule__Create_Statement__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3521:1: ( ( ( rule__Create_Statement__Alternatives_1 ) ) )
            // InternalCQL.g:3522:1: ( ( rule__Create_Statement__Alternatives_1 ) )
            {
            // InternalCQL.g:3522:1: ( ( rule__Create_Statement__Alternatives_1 ) )
            // InternalCQL.g:3523:2: ( rule__Create_Statement__Alternatives_1 )
            {
             before(grammarAccess.getCreate_StatementAccess().getAlternatives_1()); 
            // InternalCQL.g:3524:2: ( rule__Create_Statement__Alternatives_1 )
            // InternalCQL.g:3524:3: rule__Create_Statement__Alternatives_1
            {
            pushFollow(FOLLOW_2);
            rule__Create_Statement__Alternatives_1();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StatementAccess().getAlternatives_1()); 

            }


            }

        }
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


    // $ANTLR start "rule__Create_Sink__Group__0"
    // InternalCQL.g:3533:1: rule__Create_Sink__Group__0 : rule__Create_Sink__Group__0__Impl rule__Create_Sink__Group__1 ;
    public final void rule__Create_Sink__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3537:1: ( rule__Create_Sink__Group__0__Impl rule__Create_Sink__Group__1 )
            // InternalCQL.g:3538:2: rule__Create_Sink__Group__0__Impl rule__Create_Sink__Group__1
            {
            pushFollow(FOLLOW_9);
            rule__Create_Sink__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__0"


    // $ANTLR start "rule__Create_Sink__Group__0__Impl"
    // InternalCQL.g:3545:1: rule__Create_Sink__Group__0__Impl : ( 'SINK' ) ;
    public final void rule__Create_Sink__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3549:1: ( ( 'SINK' ) )
            // InternalCQL.g:3550:1: ( 'SINK' )
            {
            // InternalCQL.g:3550:1: ( 'SINK' )
            // InternalCQL.g:3551:2: 'SINK'
            {
             before(grammarAccess.getCreate_SinkAccess().getSINKKeyword_0()); 
            match(input,53,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getSINKKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__0__Impl"


    // $ANTLR start "rule__Create_Sink__Group__1"
    // InternalCQL.g:3560:1: rule__Create_Sink__Group__1 : rule__Create_Sink__Group__1__Impl rule__Create_Sink__Group__2 ;
    public final void rule__Create_Sink__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3564:1: ( rule__Create_Sink__Group__1__Impl rule__Create_Sink__Group__2 )
            // InternalCQL.g:3565:2: rule__Create_Sink__Group__1__Impl rule__Create_Sink__Group__2
            {
            pushFollow(FOLLOW_28);
            rule__Create_Sink__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__1"


    // $ANTLR start "rule__Create_Sink__Group__1__Impl"
    // InternalCQL.g:3572:1: rule__Create_Sink__Group__1__Impl : ( ( rule__Create_Sink__NameAssignment_1 ) ) ;
    public final void rule__Create_Sink__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3576:1: ( ( ( rule__Create_Sink__NameAssignment_1 ) ) )
            // InternalCQL.g:3577:1: ( ( rule__Create_Sink__NameAssignment_1 ) )
            {
            // InternalCQL.g:3577:1: ( ( rule__Create_Sink__NameAssignment_1 ) )
            // InternalCQL.g:3578:2: ( rule__Create_Sink__NameAssignment_1 )
            {
             before(grammarAccess.getCreate_SinkAccess().getNameAssignment_1()); 
            // InternalCQL.g:3579:2: ( rule__Create_Sink__NameAssignment_1 )
            // InternalCQL.g:3579:3: rule__Create_Sink__NameAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__Create_Sink__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getCreate_SinkAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__1__Impl"


    // $ANTLR start "rule__Create_Sink__Group__2"
    // InternalCQL.g:3587:1: rule__Create_Sink__Group__2 : rule__Create_Sink__Group__2__Impl rule__Create_Sink__Group__3 ;
    public final void rule__Create_Sink__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3591:1: ( rule__Create_Sink__Group__2__Impl rule__Create_Sink__Group__3 )
            // InternalCQL.g:3592:2: rule__Create_Sink__Group__2__Impl rule__Create_Sink__Group__3
            {
            pushFollow(FOLLOW_9);
            rule__Create_Sink__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__2"


    // $ANTLR start "rule__Create_Sink__Group__2__Impl"
    // InternalCQL.g:3599:1: rule__Create_Sink__Group__2__Impl : ( '(' ) ;
    public final void rule__Create_Sink__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3603:1: ( ( '(' ) )
            // InternalCQL.g:3604:1: ( '(' )
            {
            // InternalCQL.g:3604:1: ( '(' )
            // InternalCQL.g:3605:2: '('
            {
             before(grammarAccess.getCreate_SinkAccess().getLeftParenthesisKeyword_2()); 
            match(input,39,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getLeftParenthesisKeyword_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__2__Impl"


    // $ANTLR start "rule__Create_Sink__Group__3"
    // InternalCQL.g:3614:1: rule__Create_Sink__Group__3 : rule__Create_Sink__Group__3__Impl rule__Create_Sink__Group__4 ;
    public final void rule__Create_Sink__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3618:1: ( rule__Create_Sink__Group__3__Impl rule__Create_Sink__Group__4 )
            // InternalCQL.g:3619:2: rule__Create_Sink__Group__3__Impl rule__Create_Sink__Group__4
            {
            pushFollow(FOLLOW_43);
            rule__Create_Sink__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__3"


    // $ANTLR start "rule__Create_Sink__Group__3__Impl"
    // InternalCQL.g:3626:1: rule__Create_Sink__Group__3__Impl : ( ( ( rule__Create_Sink__AttributesAssignment_3 ) ) ( ( rule__Create_Sink__AttributesAssignment_3 )* ) ) ;
    public final void rule__Create_Sink__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3630:1: ( ( ( ( rule__Create_Sink__AttributesAssignment_3 ) ) ( ( rule__Create_Sink__AttributesAssignment_3 )* ) ) )
            // InternalCQL.g:3631:1: ( ( ( rule__Create_Sink__AttributesAssignment_3 ) ) ( ( rule__Create_Sink__AttributesAssignment_3 )* ) )
            {
            // InternalCQL.g:3631:1: ( ( ( rule__Create_Sink__AttributesAssignment_3 ) ) ( ( rule__Create_Sink__AttributesAssignment_3 )* ) )
            // InternalCQL.g:3632:2: ( ( rule__Create_Sink__AttributesAssignment_3 ) ) ( ( rule__Create_Sink__AttributesAssignment_3 )* )
            {
            // InternalCQL.g:3632:2: ( ( rule__Create_Sink__AttributesAssignment_3 ) )
            // InternalCQL.g:3633:3: ( rule__Create_Sink__AttributesAssignment_3 )
            {
             before(grammarAccess.getCreate_SinkAccess().getAttributesAssignment_3()); 
            // InternalCQL.g:3634:3: ( rule__Create_Sink__AttributesAssignment_3 )
            // InternalCQL.g:3634:4: rule__Create_Sink__AttributesAssignment_3
            {
            pushFollow(FOLLOW_37);
            rule__Create_Sink__AttributesAssignment_3();

            state._fsp--;


            }

             after(grammarAccess.getCreate_SinkAccess().getAttributesAssignment_3()); 

            }

            // InternalCQL.g:3637:2: ( ( rule__Create_Sink__AttributesAssignment_3 )* )
            // InternalCQL.g:3638:3: ( rule__Create_Sink__AttributesAssignment_3 )*
            {
             before(grammarAccess.getCreate_SinkAccess().getAttributesAssignment_3()); 
            // InternalCQL.g:3639:3: ( rule__Create_Sink__AttributesAssignment_3 )*
            loop33:
            do {
                int alt33=2;
                int LA33_0 = input.LA(1);

                if ( (LA33_0==RULE_ID) ) {
                    alt33=1;
                }


                switch (alt33) {
            	case 1 :
            	    // InternalCQL.g:3639:4: rule__Create_Sink__AttributesAssignment_3
            	    {
            	    pushFollow(FOLLOW_37);
            	    rule__Create_Sink__AttributesAssignment_3();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop33;
                }
            } while (true);

             after(grammarAccess.getCreate_SinkAccess().getAttributesAssignment_3()); 

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
    // $ANTLR end "rule__Create_Sink__Group__3__Impl"


    // $ANTLR start "rule__Create_Sink__Group__4"
    // InternalCQL.g:3648:1: rule__Create_Sink__Group__4 : rule__Create_Sink__Group__4__Impl rule__Create_Sink__Group__5 ;
    public final void rule__Create_Sink__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3652:1: ( rule__Create_Sink__Group__4__Impl rule__Create_Sink__Group__5 )
            // InternalCQL.g:3653:2: rule__Create_Sink__Group__4__Impl rule__Create_Sink__Group__5
            {
            pushFollow(FOLLOW_44);
            rule__Create_Sink__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__4"


    // $ANTLR start "rule__Create_Sink__Group__4__Impl"
    // InternalCQL.g:3660:1: rule__Create_Sink__Group__4__Impl : ( ( ( rule__Create_Sink__DatatypesAssignment_4 ) ) ( ( rule__Create_Sink__DatatypesAssignment_4 )* ) ) ;
    public final void rule__Create_Sink__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3664:1: ( ( ( ( rule__Create_Sink__DatatypesAssignment_4 ) ) ( ( rule__Create_Sink__DatatypesAssignment_4 )* ) ) )
            // InternalCQL.g:3665:1: ( ( ( rule__Create_Sink__DatatypesAssignment_4 ) ) ( ( rule__Create_Sink__DatatypesAssignment_4 )* ) )
            {
            // InternalCQL.g:3665:1: ( ( ( rule__Create_Sink__DatatypesAssignment_4 ) ) ( ( rule__Create_Sink__DatatypesAssignment_4 )* ) )
            // InternalCQL.g:3666:2: ( ( rule__Create_Sink__DatatypesAssignment_4 ) ) ( ( rule__Create_Sink__DatatypesAssignment_4 )* )
            {
            // InternalCQL.g:3666:2: ( ( rule__Create_Sink__DatatypesAssignment_4 ) )
            // InternalCQL.g:3667:3: ( rule__Create_Sink__DatatypesAssignment_4 )
            {
             before(grammarAccess.getCreate_SinkAccess().getDatatypesAssignment_4()); 
            // InternalCQL.g:3668:3: ( rule__Create_Sink__DatatypesAssignment_4 )
            // InternalCQL.g:3668:4: rule__Create_Sink__DatatypesAssignment_4
            {
            pushFollow(FOLLOW_45);
            rule__Create_Sink__DatatypesAssignment_4();

            state._fsp--;


            }

             after(grammarAccess.getCreate_SinkAccess().getDatatypesAssignment_4()); 

            }

            // InternalCQL.g:3671:2: ( ( rule__Create_Sink__DatatypesAssignment_4 )* )
            // InternalCQL.g:3672:3: ( rule__Create_Sink__DatatypesAssignment_4 )*
            {
             before(grammarAccess.getCreate_SinkAccess().getDatatypesAssignment_4()); 
            // InternalCQL.g:3673:3: ( rule__Create_Sink__DatatypesAssignment_4 )*
            loop34:
            do {
                int alt34=2;
                int LA34_0 = input.LA(1);

                if ( ((LA34_0>=15 && LA34_0<=21)) ) {
                    alt34=1;
                }


                switch (alt34) {
            	case 1 :
            	    // InternalCQL.g:3673:4: rule__Create_Sink__DatatypesAssignment_4
            	    {
            	    pushFollow(FOLLOW_45);
            	    rule__Create_Sink__DatatypesAssignment_4();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop34;
                }
            } while (true);

             after(grammarAccess.getCreate_SinkAccess().getDatatypesAssignment_4()); 

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
    // $ANTLR end "rule__Create_Sink__Group__4__Impl"


    // $ANTLR start "rule__Create_Sink__Group__5"
    // InternalCQL.g:3682:1: rule__Create_Sink__Group__5 : rule__Create_Sink__Group__5__Impl rule__Create_Sink__Group__6 ;
    public final void rule__Create_Sink__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3686:1: ( rule__Create_Sink__Group__5__Impl rule__Create_Sink__Group__6 )
            // InternalCQL.g:3687:2: rule__Create_Sink__Group__5__Impl rule__Create_Sink__Group__6
            {
            pushFollow(FOLLOW_44);
            rule__Create_Sink__Group__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__6();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__5"


    // $ANTLR start "rule__Create_Sink__Group__5__Impl"
    // InternalCQL.g:3694:1: rule__Create_Sink__Group__5__Impl : ( ( rule__Create_Sink__Group_5__0 )* ) ;
    public final void rule__Create_Sink__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3698:1: ( ( ( rule__Create_Sink__Group_5__0 )* ) )
            // InternalCQL.g:3699:1: ( ( rule__Create_Sink__Group_5__0 )* )
            {
            // InternalCQL.g:3699:1: ( ( rule__Create_Sink__Group_5__0 )* )
            // InternalCQL.g:3700:2: ( rule__Create_Sink__Group_5__0 )*
            {
             before(grammarAccess.getCreate_SinkAccess().getGroup_5()); 
            // InternalCQL.g:3701:2: ( rule__Create_Sink__Group_5__0 )*
            loop35:
            do {
                int alt35=2;
                int LA35_0 = input.LA(1);

                if ( (LA35_0==44) ) {
                    alt35=1;
                }


                switch (alt35) {
            	case 1 :
            	    // InternalCQL.g:3701:3: rule__Create_Sink__Group_5__0
            	    {
            	    pushFollow(FOLLOW_36);
            	    rule__Create_Sink__Group_5__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop35;
                }
            } while (true);

             after(grammarAccess.getCreate_SinkAccess().getGroup_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__5__Impl"


    // $ANTLR start "rule__Create_Sink__Group__6"
    // InternalCQL.g:3709:1: rule__Create_Sink__Group__6 : rule__Create_Sink__Group__6__Impl rule__Create_Sink__Group__7 ;
    public final void rule__Create_Sink__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3713:1: ( rule__Create_Sink__Group__6__Impl rule__Create_Sink__Group__7 )
            // InternalCQL.g:3714:2: rule__Create_Sink__Group__6__Impl rule__Create_Sink__Group__7
            {
            pushFollow(FOLLOW_46);
            rule__Create_Sink__Group__6__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__7();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__6"


    // $ANTLR start "rule__Create_Sink__Group__6__Impl"
    // InternalCQL.g:3721:1: rule__Create_Sink__Group__6__Impl : ( ')' ) ;
    public final void rule__Create_Sink__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3725:1: ( ( ')' ) )
            // InternalCQL.g:3726:1: ( ')' )
            {
            // InternalCQL.g:3726:1: ( ')' )
            // InternalCQL.g:3727:2: ')'
            {
             before(grammarAccess.getCreate_SinkAccess().getRightParenthesisKeyword_6()); 
            match(input,40,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getRightParenthesisKeyword_6()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__6__Impl"


    // $ANTLR start "rule__Create_Sink__Group__7"
    // InternalCQL.g:3736:1: rule__Create_Sink__Group__7 : rule__Create_Sink__Group__7__Impl rule__Create_Sink__Group__8 ;
    public final void rule__Create_Sink__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3740:1: ( rule__Create_Sink__Group__7__Impl rule__Create_Sink__Group__8 )
            // InternalCQL.g:3741:2: rule__Create_Sink__Group__7__Impl rule__Create_Sink__Group__8
            {
            pushFollow(FOLLOW_7);
            rule__Create_Sink__Group__7__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__8();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__7"


    // $ANTLR start "rule__Create_Sink__Group__7__Impl"
    // InternalCQL.g:3748:1: rule__Create_Sink__Group__7__Impl : ( 'WRAPPER' ) ;
    public final void rule__Create_Sink__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3752:1: ( ( 'WRAPPER' ) )
            // InternalCQL.g:3753:1: ( 'WRAPPER' )
            {
            // InternalCQL.g:3753:1: ( 'WRAPPER' )
            // InternalCQL.g:3754:2: 'WRAPPER'
            {
             before(grammarAccess.getCreate_SinkAccess().getWRAPPERKeyword_7()); 
            match(input,54,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getWRAPPERKeyword_7()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__7__Impl"


    // $ANTLR start "rule__Create_Sink__Group__8"
    // InternalCQL.g:3763:1: rule__Create_Sink__Group__8 : rule__Create_Sink__Group__8__Impl rule__Create_Sink__Group__9 ;
    public final void rule__Create_Sink__Group__8() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3767:1: ( rule__Create_Sink__Group__8__Impl rule__Create_Sink__Group__9 )
            // InternalCQL.g:3768:2: rule__Create_Sink__Group__8__Impl rule__Create_Sink__Group__9
            {
            pushFollow(FOLLOW_47);
            rule__Create_Sink__Group__8__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__9();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__8"


    // $ANTLR start "rule__Create_Sink__Group__8__Impl"
    // InternalCQL.g:3775:1: rule__Create_Sink__Group__8__Impl : ( ( rule__Create_Sink__WrapperAssignment_8 ) ) ;
    public final void rule__Create_Sink__Group__8__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3779:1: ( ( ( rule__Create_Sink__WrapperAssignment_8 ) ) )
            // InternalCQL.g:3780:1: ( ( rule__Create_Sink__WrapperAssignment_8 ) )
            {
            // InternalCQL.g:3780:1: ( ( rule__Create_Sink__WrapperAssignment_8 ) )
            // InternalCQL.g:3781:2: ( rule__Create_Sink__WrapperAssignment_8 )
            {
             before(grammarAccess.getCreate_SinkAccess().getWrapperAssignment_8()); 
            // InternalCQL.g:3782:2: ( rule__Create_Sink__WrapperAssignment_8 )
            // InternalCQL.g:3782:3: rule__Create_Sink__WrapperAssignment_8
            {
            pushFollow(FOLLOW_2);
            rule__Create_Sink__WrapperAssignment_8();

            state._fsp--;


            }

             after(grammarAccess.getCreate_SinkAccess().getWrapperAssignment_8()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__8__Impl"


    // $ANTLR start "rule__Create_Sink__Group__9"
    // InternalCQL.g:3790:1: rule__Create_Sink__Group__9 : rule__Create_Sink__Group__9__Impl rule__Create_Sink__Group__10 ;
    public final void rule__Create_Sink__Group__9() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3794:1: ( rule__Create_Sink__Group__9__Impl rule__Create_Sink__Group__10 )
            // InternalCQL.g:3795:2: rule__Create_Sink__Group__9__Impl rule__Create_Sink__Group__10
            {
            pushFollow(FOLLOW_7);
            rule__Create_Sink__Group__9__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__10();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__9"


    // $ANTLR start "rule__Create_Sink__Group__9__Impl"
    // InternalCQL.g:3802:1: rule__Create_Sink__Group__9__Impl : ( 'PROTOCOL' ) ;
    public final void rule__Create_Sink__Group__9__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3806:1: ( ( 'PROTOCOL' ) )
            // InternalCQL.g:3807:1: ( 'PROTOCOL' )
            {
            // InternalCQL.g:3807:1: ( 'PROTOCOL' )
            // InternalCQL.g:3808:2: 'PROTOCOL'
            {
             before(grammarAccess.getCreate_SinkAccess().getPROTOCOLKeyword_9()); 
            match(input,55,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getPROTOCOLKeyword_9()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__9__Impl"


    // $ANTLR start "rule__Create_Sink__Group__10"
    // InternalCQL.g:3817:1: rule__Create_Sink__Group__10 : rule__Create_Sink__Group__10__Impl rule__Create_Sink__Group__11 ;
    public final void rule__Create_Sink__Group__10() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3821:1: ( rule__Create_Sink__Group__10__Impl rule__Create_Sink__Group__11 )
            // InternalCQL.g:3822:2: rule__Create_Sink__Group__10__Impl rule__Create_Sink__Group__11
            {
            pushFollow(FOLLOW_48);
            rule__Create_Sink__Group__10__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__11();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__10"


    // $ANTLR start "rule__Create_Sink__Group__10__Impl"
    // InternalCQL.g:3829:1: rule__Create_Sink__Group__10__Impl : ( ( rule__Create_Sink__ProtocolAssignment_10 ) ) ;
    public final void rule__Create_Sink__Group__10__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3833:1: ( ( ( rule__Create_Sink__ProtocolAssignment_10 ) ) )
            // InternalCQL.g:3834:1: ( ( rule__Create_Sink__ProtocolAssignment_10 ) )
            {
            // InternalCQL.g:3834:1: ( ( rule__Create_Sink__ProtocolAssignment_10 ) )
            // InternalCQL.g:3835:2: ( rule__Create_Sink__ProtocolAssignment_10 )
            {
             before(grammarAccess.getCreate_SinkAccess().getProtocolAssignment_10()); 
            // InternalCQL.g:3836:2: ( rule__Create_Sink__ProtocolAssignment_10 )
            // InternalCQL.g:3836:3: rule__Create_Sink__ProtocolAssignment_10
            {
            pushFollow(FOLLOW_2);
            rule__Create_Sink__ProtocolAssignment_10();

            state._fsp--;


            }

             after(grammarAccess.getCreate_SinkAccess().getProtocolAssignment_10()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__10__Impl"


    // $ANTLR start "rule__Create_Sink__Group__11"
    // InternalCQL.g:3844:1: rule__Create_Sink__Group__11 : rule__Create_Sink__Group__11__Impl rule__Create_Sink__Group__12 ;
    public final void rule__Create_Sink__Group__11() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3848:1: ( rule__Create_Sink__Group__11__Impl rule__Create_Sink__Group__12 )
            // InternalCQL.g:3849:2: rule__Create_Sink__Group__11__Impl rule__Create_Sink__Group__12
            {
            pushFollow(FOLLOW_7);
            rule__Create_Sink__Group__11__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__12();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__11"


    // $ANTLR start "rule__Create_Sink__Group__11__Impl"
    // InternalCQL.g:3856:1: rule__Create_Sink__Group__11__Impl : ( 'TRANSPORT' ) ;
    public final void rule__Create_Sink__Group__11__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3860:1: ( ( 'TRANSPORT' ) )
            // InternalCQL.g:3861:1: ( 'TRANSPORT' )
            {
            // InternalCQL.g:3861:1: ( 'TRANSPORT' )
            // InternalCQL.g:3862:2: 'TRANSPORT'
            {
             before(grammarAccess.getCreate_SinkAccess().getTRANSPORTKeyword_11()); 
            match(input,56,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getTRANSPORTKeyword_11()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__11__Impl"


    // $ANTLR start "rule__Create_Sink__Group__12"
    // InternalCQL.g:3871:1: rule__Create_Sink__Group__12 : rule__Create_Sink__Group__12__Impl rule__Create_Sink__Group__13 ;
    public final void rule__Create_Sink__Group__12() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3875:1: ( rule__Create_Sink__Group__12__Impl rule__Create_Sink__Group__13 )
            // InternalCQL.g:3876:2: rule__Create_Sink__Group__12__Impl rule__Create_Sink__Group__13
            {
            pushFollow(FOLLOW_49);
            rule__Create_Sink__Group__12__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__13();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__12"


    // $ANTLR start "rule__Create_Sink__Group__12__Impl"
    // InternalCQL.g:3883:1: rule__Create_Sink__Group__12__Impl : ( ( rule__Create_Sink__TransportAssignment_12 ) ) ;
    public final void rule__Create_Sink__Group__12__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3887:1: ( ( ( rule__Create_Sink__TransportAssignment_12 ) ) )
            // InternalCQL.g:3888:1: ( ( rule__Create_Sink__TransportAssignment_12 ) )
            {
            // InternalCQL.g:3888:1: ( ( rule__Create_Sink__TransportAssignment_12 ) )
            // InternalCQL.g:3889:2: ( rule__Create_Sink__TransportAssignment_12 )
            {
             before(grammarAccess.getCreate_SinkAccess().getTransportAssignment_12()); 
            // InternalCQL.g:3890:2: ( rule__Create_Sink__TransportAssignment_12 )
            // InternalCQL.g:3890:3: rule__Create_Sink__TransportAssignment_12
            {
            pushFollow(FOLLOW_2);
            rule__Create_Sink__TransportAssignment_12();

            state._fsp--;


            }

             after(grammarAccess.getCreate_SinkAccess().getTransportAssignment_12()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__12__Impl"


    // $ANTLR start "rule__Create_Sink__Group__13"
    // InternalCQL.g:3898:1: rule__Create_Sink__Group__13 : rule__Create_Sink__Group__13__Impl rule__Create_Sink__Group__14 ;
    public final void rule__Create_Sink__Group__13() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3902:1: ( rule__Create_Sink__Group__13__Impl rule__Create_Sink__Group__14 )
            // InternalCQL.g:3903:2: rule__Create_Sink__Group__13__Impl rule__Create_Sink__Group__14
            {
            pushFollow(FOLLOW_7);
            rule__Create_Sink__Group__13__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__14();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__13"


    // $ANTLR start "rule__Create_Sink__Group__13__Impl"
    // InternalCQL.g:3910:1: rule__Create_Sink__Group__13__Impl : ( 'DATAHANDLER' ) ;
    public final void rule__Create_Sink__Group__13__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3914:1: ( ( 'DATAHANDLER' ) )
            // InternalCQL.g:3915:1: ( 'DATAHANDLER' )
            {
            // InternalCQL.g:3915:1: ( 'DATAHANDLER' )
            // InternalCQL.g:3916:2: 'DATAHANDLER'
            {
             before(grammarAccess.getCreate_SinkAccess().getDATAHANDLERKeyword_13()); 
            match(input,57,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getDATAHANDLERKeyword_13()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__13__Impl"


    // $ANTLR start "rule__Create_Sink__Group__14"
    // InternalCQL.g:3925:1: rule__Create_Sink__Group__14 : rule__Create_Sink__Group__14__Impl rule__Create_Sink__Group__15 ;
    public final void rule__Create_Sink__Group__14() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3929:1: ( rule__Create_Sink__Group__14__Impl rule__Create_Sink__Group__15 )
            // InternalCQL.g:3930:2: rule__Create_Sink__Group__14__Impl rule__Create_Sink__Group__15
            {
            pushFollow(FOLLOW_50);
            rule__Create_Sink__Group__14__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__15();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__14"


    // $ANTLR start "rule__Create_Sink__Group__14__Impl"
    // InternalCQL.g:3937:1: rule__Create_Sink__Group__14__Impl : ( ( rule__Create_Sink__DatahandlerAssignment_14 ) ) ;
    public final void rule__Create_Sink__Group__14__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3941:1: ( ( ( rule__Create_Sink__DatahandlerAssignment_14 ) ) )
            // InternalCQL.g:3942:1: ( ( rule__Create_Sink__DatahandlerAssignment_14 ) )
            {
            // InternalCQL.g:3942:1: ( ( rule__Create_Sink__DatahandlerAssignment_14 ) )
            // InternalCQL.g:3943:2: ( rule__Create_Sink__DatahandlerAssignment_14 )
            {
             before(grammarAccess.getCreate_SinkAccess().getDatahandlerAssignment_14()); 
            // InternalCQL.g:3944:2: ( rule__Create_Sink__DatahandlerAssignment_14 )
            // InternalCQL.g:3944:3: rule__Create_Sink__DatahandlerAssignment_14
            {
            pushFollow(FOLLOW_2);
            rule__Create_Sink__DatahandlerAssignment_14();

            state._fsp--;


            }

             after(grammarAccess.getCreate_SinkAccess().getDatahandlerAssignment_14()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__14__Impl"


    // $ANTLR start "rule__Create_Sink__Group__15"
    // InternalCQL.g:3952:1: rule__Create_Sink__Group__15 : rule__Create_Sink__Group__15__Impl rule__Create_Sink__Group__16 ;
    public final void rule__Create_Sink__Group__15() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3956:1: ( rule__Create_Sink__Group__15__Impl rule__Create_Sink__Group__16 )
            // InternalCQL.g:3957:2: rule__Create_Sink__Group__15__Impl rule__Create_Sink__Group__16
            {
            pushFollow(FOLLOW_28);
            rule__Create_Sink__Group__15__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__16();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__15"


    // $ANTLR start "rule__Create_Sink__Group__15__Impl"
    // InternalCQL.g:3964:1: rule__Create_Sink__Group__15__Impl : ( 'OPTIONS' ) ;
    public final void rule__Create_Sink__Group__15__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3968:1: ( ( 'OPTIONS' ) )
            // InternalCQL.g:3969:1: ( 'OPTIONS' )
            {
            // InternalCQL.g:3969:1: ( 'OPTIONS' )
            // InternalCQL.g:3970:2: 'OPTIONS'
            {
             before(grammarAccess.getCreate_SinkAccess().getOPTIONSKeyword_15()); 
            match(input,58,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getOPTIONSKeyword_15()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__15__Impl"


    // $ANTLR start "rule__Create_Sink__Group__16"
    // InternalCQL.g:3979:1: rule__Create_Sink__Group__16 : rule__Create_Sink__Group__16__Impl rule__Create_Sink__Group__17 ;
    public final void rule__Create_Sink__Group__16() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3983:1: ( rule__Create_Sink__Group__16__Impl rule__Create_Sink__Group__17 )
            // InternalCQL.g:3984:2: rule__Create_Sink__Group__16__Impl rule__Create_Sink__Group__17
            {
            pushFollow(FOLLOW_7);
            rule__Create_Sink__Group__16__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__17();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__16"


    // $ANTLR start "rule__Create_Sink__Group__16__Impl"
    // InternalCQL.g:3991:1: rule__Create_Sink__Group__16__Impl : ( '(' ) ;
    public final void rule__Create_Sink__Group__16__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3995:1: ( ( '(' ) )
            // InternalCQL.g:3996:1: ( '(' )
            {
            // InternalCQL.g:3996:1: ( '(' )
            // InternalCQL.g:3997:2: '('
            {
             before(grammarAccess.getCreate_SinkAccess().getLeftParenthesisKeyword_16()); 
            match(input,39,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getLeftParenthesisKeyword_16()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__16__Impl"


    // $ANTLR start "rule__Create_Sink__Group__17"
    // InternalCQL.g:4006:1: rule__Create_Sink__Group__17 : rule__Create_Sink__Group__17__Impl rule__Create_Sink__Group__18 ;
    public final void rule__Create_Sink__Group__17() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4010:1: ( rule__Create_Sink__Group__17__Impl rule__Create_Sink__Group__18 )
            // InternalCQL.g:4011:2: rule__Create_Sink__Group__17__Impl rule__Create_Sink__Group__18
            {
            pushFollow(FOLLOW_44);
            rule__Create_Sink__Group__17__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__18();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__17"


    // $ANTLR start "rule__Create_Sink__Group__17__Impl"
    // InternalCQL.g:4018:1: rule__Create_Sink__Group__17__Impl : ( ( ( rule__Create_Sink__Group_17__0 ) ) ( ( rule__Create_Sink__Group_17__0 )* ) ) ;
    public final void rule__Create_Sink__Group__17__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4022:1: ( ( ( ( rule__Create_Sink__Group_17__0 ) ) ( ( rule__Create_Sink__Group_17__0 )* ) ) )
            // InternalCQL.g:4023:1: ( ( ( rule__Create_Sink__Group_17__0 ) ) ( ( rule__Create_Sink__Group_17__0 )* ) )
            {
            // InternalCQL.g:4023:1: ( ( ( rule__Create_Sink__Group_17__0 ) ) ( ( rule__Create_Sink__Group_17__0 )* ) )
            // InternalCQL.g:4024:2: ( ( rule__Create_Sink__Group_17__0 ) ) ( ( rule__Create_Sink__Group_17__0 )* )
            {
            // InternalCQL.g:4024:2: ( ( rule__Create_Sink__Group_17__0 ) )
            // InternalCQL.g:4025:3: ( rule__Create_Sink__Group_17__0 )
            {
             before(grammarAccess.getCreate_SinkAccess().getGroup_17()); 
            // InternalCQL.g:4026:3: ( rule__Create_Sink__Group_17__0 )
            // InternalCQL.g:4026:4: rule__Create_Sink__Group_17__0
            {
            pushFollow(FOLLOW_51);
            rule__Create_Sink__Group_17__0();

            state._fsp--;


            }

             after(grammarAccess.getCreate_SinkAccess().getGroup_17()); 

            }

            // InternalCQL.g:4029:2: ( ( rule__Create_Sink__Group_17__0 )* )
            // InternalCQL.g:4030:3: ( rule__Create_Sink__Group_17__0 )*
            {
             before(grammarAccess.getCreate_SinkAccess().getGroup_17()); 
            // InternalCQL.g:4031:3: ( rule__Create_Sink__Group_17__0 )*
            loop36:
            do {
                int alt36=2;
                int LA36_0 = input.LA(1);

                if ( (LA36_0==RULE_STRING) ) {
                    alt36=1;
                }


                switch (alt36) {
            	case 1 :
            	    // InternalCQL.g:4031:4: rule__Create_Sink__Group_17__0
            	    {
            	    pushFollow(FOLLOW_51);
            	    rule__Create_Sink__Group_17__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop36;
                }
            } while (true);

             after(grammarAccess.getCreate_SinkAccess().getGroup_17()); 

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
    // $ANTLR end "rule__Create_Sink__Group__17__Impl"


    // $ANTLR start "rule__Create_Sink__Group__18"
    // InternalCQL.g:4040:1: rule__Create_Sink__Group__18 : rule__Create_Sink__Group__18__Impl rule__Create_Sink__Group__19 ;
    public final void rule__Create_Sink__Group__18() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4044:1: ( rule__Create_Sink__Group__18__Impl rule__Create_Sink__Group__19 )
            // InternalCQL.g:4045:2: rule__Create_Sink__Group__18__Impl rule__Create_Sink__Group__19
            {
            pushFollow(FOLLOW_44);
            rule__Create_Sink__Group__18__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__19();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__18"


    // $ANTLR start "rule__Create_Sink__Group__18__Impl"
    // InternalCQL.g:4052:1: rule__Create_Sink__Group__18__Impl : ( ( rule__Create_Sink__Group_18__0 )? ) ;
    public final void rule__Create_Sink__Group__18__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4056:1: ( ( ( rule__Create_Sink__Group_18__0 )? ) )
            // InternalCQL.g:4057:1: ( ( rule__Create_Sink__Group_18__0 )? )
            {
            // InternalCQL.g:4057:1: ( ( rule__Create_Sink__Group_18__0 )? )
            // InternalCQL.g:4058:2: ( rule__Create_Sink__Group_18__0 )?
            {
             before(grammarAccess.getCreate_SinkAccess().getGroup_18()); 
            // InternalCQL.g:4059:2: ( rule__Create_Sink__Group_18__0 )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==44) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // InternalCQL.g:4059:3: rule__Create_Sink__Group_18__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Create_Sink__Group_18__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getCreate_SinkAccess().getGroup_18()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__18__Impl"


    // $ANTLR start "rule__Create_Sink__Group__19"
    // InternalCQL.g:4067:1: rule__Create_Sink__Group__19 : rule__Create_Sink__Group__19__Impl ;
    public final void rule__Create_Sink__Group__19() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4071:1: ( rule__Create_Sink__Group__19__Impl )
            // InternalCQL.g:4072:2: rule__Create_Sink__Group__19__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group__19__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__19"


    // $ANTLR start "rule__Create_Sink__Group__19__Impl"
    // InternalCQL.g:4078:1: rule__Create_Sink__Group__19__Impl : ( ')' ) ;
    public final void rule__Create_Sink__Group__19__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4082:1: ( ( ')' ) )
            // InternalCQL.g:4083:1: ( ')' )
            {
            // InternalCQL.g:4083:1: ( ')' )
            // InternalCQL.g:4084:2: ')'
            {
             before(grammarAccess.getCreate_SinkAccess().getRightParenthesisKeyword_19()); 
            match(input,40,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getRightParenthesisKeyword_19()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group__19__Impl"


    // $ANTLR start "rule__Create_Sink__Group_5__0"
    // InternalCQL.g:4094:1: rule__Create_Sink__Group_5__0 : rule__Create_Sink__Group_5__0__Impl rule__Create_Sink__Group_5__1 ;
    public final void rule__Create_Sink__Group_5__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4098:1: ( rule__Create_Sink__Group_5__0__Impl rule__Create_Sink__Group_5__1 )
            // InternalCQL.g:4099:2: rule__Create_Sink__Group_5__0__Impl rule__Create_Sink__Group_5__1
            {
            pushFollow(FOLLOW_9);
            rule__Create_Sink__Group_5__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group_5__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group_5__0"


    // $ANTLR start "rule__Create_Sink__Group_5__0__Impl"
    // InternalCQL.g:4106:1: rule__Create_Sink__Group_5__0__Impl : ( ',' ) ;
    public final void rule__Create_Sink__Group_5__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4110:1: ( ( ',' ) )
            // InternalCQL.g:4111:1: ( ',' )
            {
            // InternalCQL.g:4111:1: ( ',' )
            // InternalCQL.g:4112:2: ','
            {
             before(grammarAccess.getCreate_SinkAccess().getCommaKeyword_5_0()); 
            match(input,44,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getCommaKeyword_5_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group_5__0__Impl"


    // $ANTLR start "rule__Create_Sink__Group_5__1"
    // InternalCQL.g:4121:1: rule__Create_Sink__Group_5__1 : rule__Create_Sink__Group_5__1__Impl rule__Create_Sink__Group_5__2 ;
    public final void rule__Create_Sink__Group_5__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4125:1: ( rule__Create_Sink__Group_5__1__Impl rule__Create_Sink__Group_5__2 )
            // InternalCQL.g:4126:2: rule__Create_Sink__Group_5__1__Impl rule__Create_Sink__Group_5__2
            {
            pushFollow(FOLLOW_43);
            rule__Create_Sink__Group_5__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group_5__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group_5__1"


    // $ANTLR start "rule__Create_Sink__Group_5__1__Impl"
    // InternalCQL.g:4133:1: rule__Create_Sink__Group_5__1__Impl : ( ( rule__Create_Sink__AttributesAssignment_5_1 ) ) ;
    public final void rule__Create_Sink__Group_5__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4137:1: ( ( ( rule__Create_Sink__AttributesAssignment_5_1 ) ) )
            // InternalCQL.g:4138:1: ( ( rule__Create_Sink__AttributesAssignment_5_1 ) )
            {
            // InternalCQL.g:4138:1: ( ( rule__Create_Sink__AttributesAssignment_5_1 ) )
            // InternalCQL.g:4139:2: ( rule__Create_Sink__AttributesAssignment_5_1 )
            {
             before(grammarAccess.getCreate_SinkAccess().getAttributesAssignment_5_1()); 
            // InternalCQL.g:4140:2: ( rule__Create_Sink__AttributesAssignment_5_1 )
            // InternalCQL.g:4140:3: rule__Create_Sink__AttributesAssignment_5_1
            {
            pushFollow(FOLLOW_2);
            rule__Create_Sink__AttributesAssignment_5_1();

            state._fsp--;


            }

             after(grammarAccess.getCreate_SinkAccess().getAttributesAssignment_5_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group_5__1__Impl"


    // $ANTLR start "rule__Create_Sink__Group_5__2"
    // InternalCQL.g:4148:1: rule__Create_Sink__Group_5__2 : rule__Create_Sink__Group_5__2__Impl ;
    public final void rule__Create_Sink__Group_5__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4152:1: ( rule__Create_Sink__Group_5__2__Impl )
            // InternalCQL.g:4153:2: rule__Create_Sink__Group_5__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group_5__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group_5__2"


    // $ANTLR start "rule__Create_Sink__Group_5__2__Impl"
    // InternalCQL.g:4159:1: rule__Create_Sink__Group_5__2__Impl : ( ( rule__Create_Sink__DatatypesAssignment_5_2 ) ) ;
    public final void rule__Create_Sink__Group_5__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4163:1: ( ( ( rule__Create_Sink__DatatypesAssignment_5_2 ) ) )
            // InternalCQL.g:4164:1: ( ( rule__Create_Sink__DatatypesAssignment_5_2 ) )
            {
            // InternalCQL.g:4164:1: ( ( rule__Create_Sink__DatatypesAssignment_5_2 ) )
            // InternalCQL.g:4165:2: ( rule__Create_Sink__DatatypesAssignment_5_2 )
            {
             before(grammarAccess.getCreate_SinkAccess().getDatatypesAssignment_5_2()); 
            // InternalCQL.g:4166:2: ( rule__Create_Sink__DatatypesAssignment_5_2 )
            // InternalCQL.g:4166:3: rule__Create_Sink__DatatypesAssignment_5_2
            {
            pushFollow(FOLLOW_2);
            rule__Create_Sink__DatatypesAssignment_5_2();

            state._fsp--;


            }

             after(grammarAccess.getCreate_SinkAccess().getDatatypesAssignment_5_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group_5__2__Impl"


    // $ANTLR start "rule__Create_Sink__Group_17__0"
    // InternalCQL.g:4175:1: rule__Create_Sink__Group_17__0 : rule__Create_Sink__Group_17__0__Impl rule__Create_Sink__Group_17__1 ;
    public final void rule__Create_Sink__Group_17__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4179:1: ( rule__Create_Sink__Group_17__0__Impl rule__Create_Sink__Group_17__1 )
            // InternalCQL.g:4180:2: rule__Create_Sink__Group_17__0__Impl rule__Create_Sink__Group_17__1
            {
            pushFollow(FOLLOW_7);
            rule__Create_Sink__Group_17__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group_17__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group_17__0"


    // $ANTLR start "rule__Create_Sink__Group_17__0__Impl"
    // InternalCQL.g:4187:1: rule__Create_Sink__Group_17__0__Impl : ( ( rule__Create_Sink__KeysAssignment_17_0 ) ) ;
    public final void rule__Create_Sink__Group_17__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4191:1: ( ( ( rule__Create_Sink__KeysAssignment_17_0 ) ) )
            // InternalCQL.g:4192:1: ( ( rule__Create_Sink__KeysAssignment_17_0 ) )
            {
            // InternalCQL.g:4192:1: ( ( rule__Create_Sink__KeysAssignment_17_0 ) )
            // InternalCQL.g:4193:2: ( rule__Create_Sink__KeysAssignment_17_0 )
            {
             before(grammarAccess.getCreate_SinkAccess().getKeysAssignment_17_0()); 
            // InternalCQL.g:4194:2: ( rule__Create_Sink__KeysAssignment_17_0 )
            // InternalCQL.g:4194:3: rule__Create_Sink__KeysAssignment_17_0
            {
            pushFollow(FOLLOW_2);
            rule__Create_Sink__KeysAssignment_17_0();

            state._fsp--;


            }

             after(grammarAccess.getCreate_SinkAccess().getKeysAssignment_17_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group_17__0__Impl"


    // $ANTLR start "rule__Create_Sink__Group_17__1"
    // InternalCQL.g:4202:1: rule__Create_Sink__Group_17__1 : rule__Create_Sink__Group_17__1__Impl ;
    public final void rule__Create_Sink__Group_17__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4206:1: ( rule__Create_Sink__Group_17__1__Impl )
            // InternalCQL.g:4207:2: rule__Create_Sink__Group_17__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group_17__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group_17__1"


    // $ANTLR start "rule__Create_Sink__Group_17__1__Impl"
    // InternalCQL.g:4213:1: rule__Create_Sink__Group_17__1__Impl : ( ( rule__Create_Sink__ValuesAssignment_17_1 ) ) ;
    public final void rule__Create_Sink__Group_17__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4217:1: ( ( ( rule__Create_Sink__ValuesAssignment_17_1 ) ) )
            // InternalCQL.g:4218:1: ( ( rule__Create_Sink__ValuesAssignment_17_1 ) )
            {
            // InternalCQL.g:4218:1: ( ( rule__Create_Sink__ValuesAssignment_17_1 ) )
            // InternalCQL.g:4219:2: ( rule__Create_Sink__ValuesAssignment_17_1 )
            {
             before(grammarAccess.getCreate_SinkAccess().getValuesAssignment_17_1()); 
            // InternalCQL.g:4220:2: ( rule__Create_Sink__ValuesAssignment_17_1 )
            // InternalCQL.g:4220:3: rule__Create_Sink__ValuesAssignment_17_1
            {
            pushFollow(FOLLOW_2);
            rule__Create_Sink__ValuesAssignment_17_1();

            state._fsp--;


            }

             after(grammarAccess.getCreate_SinkAccess().getValuesAssignment_17_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group_17__1__Impl"


    // $ANTLR start "rule__Create_Sink__Group_18__0"
    // InternalCQL.g:4229:1: rule__Create_Sink__Group_18__0 : rule__Create_Sink__Group_18__0__Impl rule__Create_Sink__Group_18__1 ;
    public final void rule__Create_Sink__Group_18__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4233:1: ( rule__Create_Sink__Group_18__0__Impl rule__Create_Sink__Group_18__1 )
            // InternalCQL.g:4234:2: rule__Create_Sink__Group_18__0__Impl rule__Create_Sink__Group_18__1
            {
            pushFollow(FOLLOW_7);
            rule__Create_Sink__Group_18__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group_18__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group_18__0"


    // $ANTLR start "rule__Create_Sink__Group_18__0__Impl"
    // InternalCQL.g:4241:1: rule__Create_Sink__Group_18__0__Impl : ( ',' ) ;
    public final void rule__Create_Sink__Group_18__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4245:1: ( ( ',' ) )
            // InternalCQL.g:4246:1: ( ',' )
            {
            // InternalCQL.g:4246:1: ( ',' )
            // InternalCQL.g:4247:2: ','
            {
             before(grammarAccess.getCreate_SinkAccess().getCommaKeyword_18_0()); 
            match(input,44,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getCommaKeyword_18_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group_18__0__Impl"


    // $ANTLR start "rule__Create_Sink__Group_18__1"
    // InternalCQL.g:4256:1: rule__Create_Sink__Group_18__1 : rule__Create_Sink__Group_18__1__Impl rule__Create_Sink__Group_18__2 ;
    public final void rule__Create_Sink__Group_18__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4260:1: ( rule__Create_Sink__Group_18__1__Impl rule__Create_Sink__Group_18__2 )
            // InternalCQL.g:4261:2: rule__Create_Sink__Group_18__1__Impl rule__Create_Sink__Group_18__2
            {
            pushFollow(FOLLOW_7);
            rule__Create_Sink__Group_18__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group_18__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group_18__1"


    // $ANTLR start "rule__Create_Sink__Group_18__1__Impl"
    // InternalCQL.g:4268:1: rule__Create_Sink__Group_18__1__Impl : ( ( rule__Create_Sink__KeysAssignment_18_1 ) ) ;
    public final void rule__Create_Sink__Group_18__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4272:1: ( ( ( rule__Create_Sink__KeysAssignment_18_1 ) ) )
            // InternalCQL.g:4273:1: ( ( rule__Create_Sink__KeysAssignment_18_1 ) )
            {
            // InternalCQL.g:4273:1: ( ( rule__Create_Sink__KeysAssignment_18_1 ) )
            // InternalCQL.g:4274:2: ( rule__Create_Sink__KeysAssignment_18_1 )
            {
             before(grammarAccess.getCreate_SinkAccess().getKeysAssignment_18_1()); 
            // InternalCQL.g:4275:2: ( rule__Create_Sink__KeysAssignment_18_1 )
            // InternalCQL.g:4275:3: rule__Create_Sink__KeysAssignment_18_1
            {
            pushFollow(FOLLOW_2);
            rule__Create_Sink__KeysAssignment_18_1();

            state._fsp--;


            }

             after(grammarAccess.getCreate_SinkAccess().getKeysAssignment_18_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group_18__1__Impl"


    // $ANTLR start "rule__Create_Sink__Group_18__2"
    // InternalCQL.g:4283:1: rule__Create_Sink__Group_18__2 : rule__Create_Sink__Group_18__2__Impl ;
    public final void rule__Create_Sink__Group_18__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4287:1: ( rule__Create_Sink__Group_18__2__Impl )
            // InternalCQL.g:4288:2: rule__Create_Sink__Group_18__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Create_Sink__Group_18__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group_18__2"


    // $ANTLR start "rule__Create_Sink__Group_18__2__Impl"
    // InternalCQL.g:4294:1: rule__Create_Sink__Group_18__2__Impl : ( ( rule__Create_Sink__ValuesAssignment_18_2 ) ) ;
    public final void rule__Create_Sink__Group_18__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4298:1: ( ( ( rule__Create_Sink__ValuesAssignment_18_2 ) ) )
            // InternalCQL.g:4299:1: ( ( rule__Create_Sink__ValuesAssignment_18_2 ) )
            {
            // InternalCQL.g:4299:1: ( ( rule__Create_Sink__ValuesAssignment_18_2 ) )
            // InternalCQL.g:4300:2: ( rule__Create_Sink__ValuesAssignment_18_2 )
            {
             before(grammarAccess.getCreate_SinkAccess().getValuesAssignment_18_2()); 
            // InternalCQL.g:4301:2: ( rule__Create_Sink__ValuesAssignment_18_2 )
            // InternalCQL.g:4301:3: rule__Create_Sink__ValuesAssignment_18_2
            {
            pushFollow(FOLLOW_2);
            rule__Create_Sink__ValuesAssignment_18_2();

            state._fsp--;


            }

             after(grammarAccess.getCreate_SinkAccess().getValuesAssignment_18_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__Group_18__2__Impl"


    // $ANTLR start "rule__Create_Stream__Group__0"
    // InternalCQL.g:4310:1: rule__Create_Stream__Group__0 : rule__Create_Stream__Group__0__Impl rule__Create_Stream__Group__1 ;
    public final void rule__Create_Stream__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4314:1: ( rule__Create_Stream__Group__0__Impl rule__Create_Stream__Group__1 )
            // InternalCQL.g:4315:2: rule__Create_Stream__Group__0__Impl rule__Create_Stream__Group__1
            {
            pushFollow(FOLLOW_9);
            rule__Create_Stream__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Stream__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__0"


    // $ANTLR start "rule__Create_Stream__Group__0__Impl"
    // InternalCQL.g:4322:1: rule__Create_Stream__Group__0__Impl : ( ( rule__Create_Stream__Alternatives_0 ) ) ;
    public final void rule__Create_Stream__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4326:1: ( ( ( rule__Create_Stream__Alternatives_0 ) ) )
            // InternalCQL.g:4327:1: ( ( rule__Create_Stream__Alternatives_0 ) )
            {
            // InternalCQL.g:4327:1: ( ( rule__Create_Stream__Alternatives_0 ) )
            // InternalCQL.g:4328:2: ( rule__Create_Stream__Alternatives_0 )
            {
             before(grammarAccess.getCreate_StreamAccess().getAlternatives_0()); 
            // InternalCQL.g:4329:2: ( rule__Create_Stream__Alternatives_0 )
            // InternalCQL.g:4329:3: rule__Create_Stream__Alternatives_0
            {
            pushFollow(FOLLOW_2);
            rule__Create_Stream__Alternatives_0();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StreamAccess().getAlternatives_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__0__Impl"


    // $ANTLR start "rule__Create_Stream__Group__1"
    // InternalCQL.g:4337:1: rule__Create_Stream__Group__1 : rule__Create_Stream__Group__1__Impl rule__Create_Stream__Group__2 ;
    public final void rule__Create_Stream__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4341:1: ( rule__Create_Stream__Group__1__Impl rule__Create_Stream__Group__2 )
            // InternalCQL.g:4342:2: rule__Create_Stream__Group__1__Impl rule__Create_Stream__Group__2
            {
            pushFollow(FOLLOW_28);
            rule__Create_Stream__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Stream__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__1"


    // $ANTLR start "rule__Create_Stream__Group__1__Impl"
    // InternalCQL.g:4349:1: rule__Create_Stream__Group__1__Impl : ( ( rule__Create_Stream__NameAssignment_1 ) ) ;
    public final void rule__Create_Stream__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4353:1: ( ( ( rule__Create_Stream__NameAssignment_1 ) ) )
            // InternalCQL.g:4354:1: ( ( rule__Create_Stream__NameAssignment_1 ) )
            {
            // InternalCQL.g:4354:1: ( ( rule__Create_Stream__NameAssignment_1 ) )
            // InternalCQL.g:4355:2: ( rule__Create_Stream__NameAssignment_1 )
            {
             before(grammarAccess.getCreate_StreamAccess().getNameAssignment_1()); 
            // InternalCQL.g:4356:2: ( rule__Create_Stream__NameAssignment_1 )
            // InternalCQL.g:4356:3: rule__Create_Stream__NameAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__Create_Stream__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StreamAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__1__Impl"


    // $ANTLR start "rule__Create_Stream__Group__2"
    // InternalCQL.g:4364:1: rule__Create_Stream__Group__2 : rule__Create_Stream__Group__2__Impl rule__Create_Stream__Group__3 ;
    public final void rule__Create_Stream__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4368:1: ( rule__Create_Stream__Group__2__Impl rule__Create_Stream__Group__3 )
            // InternalCQL.g:4369:2: rule__Create_Stream__Group__2__Impl rule__Create_Stream__Group__3
            {
            pushFollow(FOLLOW_9);
            rule__Create_Stream__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Stream__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__2"


    // $ANTLR start "rule__Create_Stream__Group__2__Impl"
    // InternalCQL.g:4376:1: rule__Create_Stream__Group__2__Impl : ( '(' ) ;
    public final void rule__Create_Stream__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4380:1: ( ( '(' ) )
            // InternalCQL.g:4381:1: ( '(' )
            {
            // InternalCQL.g:4381:1: ( '(' )
            // InternalCQL.g:4382:2: '('
            {
             before(grammarAccess.getCreate_StreamAccess().getLeftParenthesisKeyword_2()); 
            match(input,39,FOLLOW_2); 
             after(grammarAccess.getCreate_StreamAccess().getLeftParenthesisKeyword_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__2__Impl"


    // $ANTLR start "rule__Create_Stream__Group__3"
    // InternalCQL.g:4391:1: rule__Create_Stream__Group__3 : rule__Create_Stream__Group__3__Impl rule__Create_Stream__Group__4 ;
    public final void rule__Create_Stream__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4395:1: ( rule__Create_Stream__Group__3__Impl rule__Create_Stream__Group__4 )
            // InternalCQL.g:4396:2: rule__Create_Stream__Group__3__Impl rule__Create_Stream__Group__4
            {
            pushFollow(FOLLOW_43);
            rule__Create_Stream__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Stream__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__3"


    // $ANTLR start "rule__Create_Stream__Group__3__Impl"
    // InternalCQL.g:4403:1: rule__Create_Stream__Group__3__Impl : ( ( ( rule__Create_Stream__AttributesAssignment_3 ) ) ( ( rule__Create_Stream__AttributesAssignment_3 )* ) ) ;
    public final void rule__Create_Stream__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4407:1: ( ( ( ( rule__Create_Stream__AttributesAssignment_3 ) ) ( ( rule__Create_Stream__AttributesAssignment_3 )* ) ) )
            // InternalCQL.g:4408:1: ( ( ( rule__Create_Stream__AttributesAssignment_3 ) ) ( ( rule__Create_Stream__AttributesAssignment_3 )* ) )
            {
            // InternalCQL.g:4408:1: ( ( ( rule__Create_Stream__AttributesAssignment_3 ) ) ( ( rule__Create_Stream__AttributesAssignment_3 )* ) )
            // InternalCQL.g:4409:2: ( ( rule__Create_Stream__AttributesAssignment_3 ) ) ( ( rule__Create_Stream__AttributesAssignment_3 )* )
            {
            // InternalCQL.g:4409:2: ( ( rule__Create_Stream__AttributesAssignment_3 ) )
            // InternalCQL.g:4410:3: ( rule__Create_Stream__AttributesAssignment_3 )
            {
             before(grammarAccess.getCreate_StreamAccess().getAttributesAssignment_3()); 
            // InternalCQL.g:4411:3: ( rule__Create_Stream__AttributesAssignment_3 )
            // InternalCQL.g:4411:4: rule__Create_Stream__AttributesAssignment_3
            {
            pushFollow(FOLLOW_37);
            rule__Create_Stream__AttributesAssignment_3();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StreamAccess().getAttributesAssignment_3()); 

            }

            // InternalCQL.g:4414:2: ( ( rule__Create_Stream__AttributesAssignment_3 )* )
            // InternalCQL.g:4415:3: ( rule__Create_Stream__AttributesAssignment_3 )*
            {
             before(grammarAccess.getCreate_StreamAccess().getAttributesAssignment_3()); 
            // InternalCQL.g:4416:3: ( rule__Create_Stream__AttributesAssignment_3 )*
            loop38:
            do {
                int alt38=2;
                int LA38_0 = input.LA(1);

                if ( (LA38_0==RULE_ID) ) {
                    alt38=1;
                }


                switch (alt38) {
            	case 1 :
            	    // InternalCQL.g:4416:4: rule__Create_Stream__AttributesAssignment_3
            	    {
            	    pushFollow(FOLLOW_37);
            	    rule__Create_Stream__AttributesAssignment_3();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop38;
                }
            } while (true);

             after(grammarAccess.getCreate_StreamAccess().getAttributesAssignment_3()); 

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
    // $ANTLR end "rule__Create_Stream__Group__3__Impl"


    // $ANTLR start "rule__Create_Stream__Group__4"
    // InternalCQL.g:4425:1: rule__Create_Stream__Group__4 : rule__Create_Stream__Group__4__Impl rule__Create_Stream__Group__5 ;
    public final void rule__Create_Stream__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4429:1: ( rule__Create_Stream__Group__4__Impl rule__Create_Stream__Group__5 )
            // InternalCQL.g:4430:2: rule__Create_Stream__Group__4__Impl rule__Create_Stream__Group__5
            {
            pushFollow(FOLLOW_44);
            rule__Create_Stream__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Stream__Group__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__4"


    // $ANTLR start "rule__Create_Stream__Group__4__Impl"
    // InternalCQL.g:4437:1: rule__Create_Stream__Group__4__Impl : ( ( ( rule__Create_Stream__DatatypesAssignment_4 ) ) ( ( rule__Create_Stream__DatatypesAssignment_4 )* ) ) ;
    public final void rule__Create_Stream__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4441:1: ( ( ( ( rule__Create_Stream__DatatypesAssignment_4 ) ) ( ( rule__Create_Stream__DatatypesAssignment_4 )* ) ) )
            // InternalCQL.g:4442:1: ( ( ( rule__Create_Stream__DatatypesAssignment_4 ) ) ( ( rule__Create_Stream__DatatypesAssignment_4 )* ) )
            {
            // InternalCQL.g:4442:1: ( ( ( rule__Create_Stream__DatatypesAssignment_4 ) ) ( ( rule__Create_Stream__DatatypesAssignment_4 )* ) )
            // InternalCQL.g:4443:2: ( ( rule__Create_Stream__DatatypesAssignment_4 ) ) ( ( rule__Create_Stream__DatatypesAssignment_4 )* )
            {
            // InternalCQL.g:4443:2: ( ( rule__Create_Stream__DatatypesAssignment_4 ) )
            // InternalCQL.g:4444:3: ( rule__Create_Stream__DatatypesAssignment_4 )
            {
             before(grammarAccess.getCreate_StreamAccess().getDatatypesAssignment_4()); 
            // InternalCQL.g:4445:3: ( rule__Create_Stream__DatatypesAssignment_4 )
            // InternalCQL.g:4445:4: rule__Create_Stream__DatatypesAssignment_4
            {
            pushFollow(FOLLOW_45);
            rule__Create_Stream__DatatypesAssignment_4();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StreamAccess().getDatatypesAssignment_4()); 

            }

            // InternalCQL.g:4448:2: ( ( rule__Create_Stream__DatatypesAssignment_4 )* )
            // InternalCQL.g:4449:3: ( rule__Create_Stream__DatatypesAssignment_4 )*
            {
             before(grammarAccess.getCreate_StreamAccess().getDatatypesAssignment_4()); 
            // InternalCQL.g:4450:3: ( rule__Create_Stream__DatatypesAssignment_4 )*
            loop39:
            do {
                int alt39=2;
                int LA39_0 = input.LA(1);

                if ( ((LA39_0>=15 && LA39_0<=21)) ) {
                    alt39=1;
                }


                switch (alt39) {
            	case 1 :
            	    // InternalCQL.g:4450:4: rule__Create_Stream__DatatypesAssignment_4
            	    {
            	    pushFollow(FOLLOW_45);
            	    rule__Create_Stream__DatatypesAssignment_4();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop39;
                }
            } while (true);

             after(grammarAccess.getCreate_StreamAccess().getDatatypesAssignment_4()); 

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
    // $ANTLR end "rule__Create_Stream__Group__4__Impl"


    // $ANTLR start "rule__Create_Stream__Group__5"
    // InternalCQL.g:4459:1: rule__Create_Stream__Group__5 : rule__Create_Stream__Group__5__Impl rule__Create_Stream__Group__6 ;
    public final void rule__Create_Stream__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4463:1: ( rule__Create_Stream__Group__5__Impl rule__Create_Stream__Group__6 )
            // InternalCQL.g:4464:2: rule__Create_Stream__Group__5__Impl rule__Create_Stream__Group__6
            {
            pushFollow(FOLLOW_44);
            rule__Create_Stream__Group__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Stream__Group__6();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__5"


    // $ANTLR start "rule__Create_Stream__Group__5__Impl"
    // InternalCQL.g:4471:1: rule__Create_Stream__Group__5__Impl : ( ( rule__Create_Stream__Group_5__0 )* ) ;
    public final void rule__Create_Stream__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4475:1: ( ( ( rule__Create_Stream__Group_5__0 )* ) )
            // InternalCQL.g:4476:1: ( ( rule__Create_Stream__Group_5__0 )* )
            {
            // InternalCQL.g:4476:1: ( ( rule__Create_Stream__Group_5__0 )* )
            // InternalCQL.g:4477:2: ( rule__Create_Stream__Group_5__0 )*
            {
             before(grammarAccess.getCreate_StreamAccess().getGroup_5()); 
            // InternalCQL.g:4478:2: ( rule__Create_Stream__Group_5__0 )*
            loop40:
            do {
                int alt40=2;
                int LA40_0 = input.LA(1);

                if ( (LA40_0==44) ) {
                    alt40=1;
                }


                switch (alt40) {
            	case 1 :
            	    // InternalCQL.g:4478:3: rule__Create_Stream__Group_5__0
            	    {
            	    pushFollow(FOLLOW_36);
            	    rule__Create_Stream__Group_5__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop40;
                }
            } while (true);

             after(grammarAccess.getCreate_StreamAccess().getGroup_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__5__Impl"


    // $ANTLR start "rule__Create_Stream__Group__6"
    // InternalCQL.g:4486:1: rule__Create_Stream__Group__6 : rule__Create_Stream__Group__6__Impl rule__Create_Stream__Group__7 ;
    public final void rule__Create_Stream__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4490:1: ( rule__Create_Stream__Group__6__Impl rule__Create_Stream__Group__7 )
            // InternalCQL.g:4491:2: rule__Create_Stream__Group__6__Impl rule__Create_Stream__Group__7
            {
            pushFollow(FOLLOW_52);
            rule__Create_Stream__Group__6__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Stream__Group__7();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__6"


    // $ANTLR start "rule__Create_Stream__Group__6__Impl"
    // InternalCQL.g:4498:1: rule__Create_Stream__Group__6__Impl : ( ')' ) ;
    public final void rule__Create_Stream__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4502:1: ( ( ')' ) )
            // InternalCQL.g:4503:1: ( ')' )
            {
            // InternalCQL.g:4503:1: ( ')' )
            // InternalCQL.g:4504:2: ')'
            {
             before(grammarAccess.getCreate_StreamAccess().getRightParenthesisKeyword_6()); 
            match(input,40,FOLLOW_2); 
             after(grammarAccess.getCreate_StreamAccess().getRightParenthesisKeyword_6()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__6__Impl"


    // $ANTLR start "rule__Create_Stream__Group__7"
    // InternalCQL.g:4513:1: rule__Create_Stream__Group__7 : rule__Create_Stream__Group__7__Impl rule__Create_Stream__Group__8 ;
    public final void rule__Create_Stream__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4517:1: ( rule__Create_Stream__Group__7__Impl rule__Create_Stream__Group__8 )
            // InternalCQL.g:4518:2: rule__Create_Stream__Group__7__Impl rule__Create_Stream__Group__8
            {
            pushFollow(FOLLOW_9);
            rule__Create_Stream__Group__7__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Stream__Group__8();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__7"


    // $ANTLR start "rule__Create_Stream__Group__7__Impl"
    // InternalCQL.g:4525:1: rule__Create_Stream__Group__7__Impl : ( 'CHANNEL' ) ;
    public final void rule__Create_Stream__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4529:1: ( ( 'CHANNEL' ) )
            // InternalCQL.g:4530:1: ( 'CHANNEL' )
            {
            // InternalCQL.g:4530:1: ( 'CHANNEL' )
            // InternalCQL.g:4531:2: 'CHANNEL'
            {
             before(grammarAccess.getCreate_StreamAccess().getCHANNELKeyword_7()); 
            match(input,59,FOLLOW_2); 
             after(grammarAccess.getCreate_StreamAccess().getCHANNELKeyword_7()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__7__Impl"


    // $ANTLR start "rule__Create_Stream__Group__8"
    // InternalCQL.g:4540:1: rule__Create_Stream__Group__8 : rule__Create_Stream__Group__8__Impl rule__Create_Stream__Group__9 ;
    public final void rule__Create_Stream__Group__8() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4544:1: ( rule__Create_Stream__Group__8__Impl rule__Create_Stream__Group__9 )
            // InternalCQL.g:4545:2: rule__Create_Stream__Group__8__Impl rule__Create_Stream__Group__9
            {
            pushFollow(FOLLOW_53);
            rule__Create_Stream__Group__8__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Stream__Group__9();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__8"


    // $ANTLR start "rule__Create_Stream__Group__8__Impl"
    // InternalCQL.g:4552:1: rule__Create_Stream__Group__8__Impl : ( ( rule__Create_Stream__HostAssignment_8 ) ) ;
    public final void rule__Create_Stream__Group__8__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4556:1: ( ( ( rule__Create_Stream__HostAssignment_8 ) ) )
            // InternalCQL.g:4557:1: ( ( rule__Create_Stream__HostAssignment_8 ) )
            {
            // InternalCQL.g:4557:1: ( ( rule__Create_Stream__HostAssignment_8 ) )
            // InternalCQL.g:4558:2: ( rule__Create_Stream__HostAssignment_8 )
            {
             before(grammarAccess.getCreate_StreamAccess().getHostAssignment_8()); 
            // InternalCQL.g:4559:2: ( rule__Create_Stream__HostAssignment_8 )
            // InternalCQL.g:4559:3: rule__Create_Stream__HostAssignment_8
            {
            pushFollow(FOLLOW_2);
            rule__Create_Stream__HostAssignment_8();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StreamAccess().getHostAssignment_8()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__8__Impl"


    // $ANTLR start "rule__Create_Stream__Group__9"
    // InternalCQL.g:4567:1: rule__Create_Stream__Group__9 : rule__Create_Stream__Group__9__Impl rule__Create_Stream__Group__10 ;
    public final void rule__Create_Stream__Group__9() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4571:1: ( rule__Create_Stream__Group__9__Impl rule__Create_Stream__Group__10 )
            // InternalCQL.g:4572:2: rule__Create_Stream__Group__9__Impl rule__Create_Stream__Group__10
            {
            pushFollow(FOLLOW_5);
            rule__Create_Stream__Group__9__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Stream__Group__10();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__9"


    // $ANTLR start "rule__Create_Stream__Group__9__Impl"
    // InternalCQL.g:4579:1: rule__Create_Stream__Group__9__Impl : ( ':' ) ;
    public final void rule__Create_Stream__Group__9__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4583:1: ( ( ':' ) )
            // InternalCQL.g:4584:1: ( ':' )
            {
            // InternalCQL.g:4584:1: ( ':' )
            // InternalCQL.g:4585:2: ':'
            {
             before(grammarAccess.getCreate_StreamAccess().getColonKeyword_9()); 
            match(input,60,FOLLOW_2); 
             after(grammarAccess.getCreate_StreamAccess().getColonKeyword_9()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__9__Impl"


    // $ANTLR start "rule__Create_Stream__Group__10"
    // InternalCQL.g:4594:1: rule__Create_Stream__Group__10 : rule__Create_Stream__Group__10__Impl ;
    public final void rule__Create_Stream__Group__10() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4598:1: ( rule__Create_Stream__Group__10__Impl )
            // InternalCQL.g:4599:2: rule__Create_Stream__Group__10__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Create_Stream__Group__10__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__10"


    // $ANTLR start "rule__Create_Stream__Group__10__Impl"
    // InternalCQL.g:4605:1: rule__Create_Stream__Group__10__Impl : ( ( rule__Create_Stream__PortAssignment_10 ) ) ;
    public final void rule__Create_Stream__Group__10__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4609:1: ( ( ( rule__Create_Stream__PortAssignment_10 ) ) )
            // InternalCQL.g:4610:1: ( ( rule__Create_Stream__PortAssignment_10 ) )
            {
            // InternalCQL.g:4610:1: ( ( rule__Create_Stream__PortAssignment_10 ) )
            // InternalCQL.g:4611:2: ( rule__Create_Stream__PortAssignment_10 )
            {
             before(grammarAccess.getCreate_StreamAccess().getPortAssignment_10()); 
            // InternalCQL.g:4612:2: ( rule__Create_Stream__PortAssignment_10 )
            // InternalCQL.g:4612:3: rule__Create_Stream__PortAssignment_10
            {
            pushFollow(FOLLOW_2);
            rule__Create_Stream__PortAssignment_10();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StreamAccess().getPortAssignment_10()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group__10__Impl"


    // $ANTLR start "rule__Create_Stream__Group_5__0"
    // InternalCQL.g:4621:1: rule__Create_Stream__Group_5__0 : rule__Create_Stream__Group_5__0__Impl rule__Create_Stream__Group_5__1 ;
    public final void rule__Create_Stream__Group_5__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4625:1: ( rule__Create_Stream__Group_5__0__Impl rule__Create_Stream__Group_5__1 )
            // InternalCQL.g:4626:2: rule__Create_Stream__Group_5__0__Impl rule__Create_Stream__Group_5__1
            {
            pushFollow(FOLLOW_9);
            rule__Create_Stream__Group_5__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Stream__Group_5__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group_5__0"


    // $ANTLR start "rule__Create_Stream__Group_5__0__Impl"
    // InternalCQL.g:4633:1: rule__Create_Stream__Group_5__0__Impl : ( ',' ) ;
    public final void rule__Create_Stream__Group_5__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4637:1: ( ( ',' ) )
            // InternalCQL.g:4638:1: ( ',' )
            {
            // InternalCQL.g:4638:1: ( ',' )
            // InternalCQL.g:4639:2: ','
            {
             before(grammarAccess.getCreate_StreamAccess().getCommaKeyword_5_0()); 
            match(input,44,FOLLOW_2); 
             after(grammarAccess.getCreate_StreamAccess().getCommaKeyword_5_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group_5__0__Impl"


    // $ANTLR start "rule__Create_Stream__Group_5__1"
    // InternalCQL.g:4648:1: rule__Create_Stream__Group_5__1 : rule__Create_Stream__Group_5__1__Impl rule__Create_Stream__Group_5__2 ;
    public final void rule__Create_Stream__Group_5__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4652:1: ( rule__Create_Stream__Group_5__1__Impl rule__Create_Stream__Group_5__2 )
            // InternalCQL.g:4653:2: rule__Create_Stream__Group_5__1__Impl rule__Create_Stream__Group_5__2
            {
            pushFollow(FOLLOW_43);
            rule__Create_Stream__Group_5__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Stream__Group_5__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group_5__1"


    // $ANTLR start "rule__Create_Stream__Group_5__1__Impl"
    // InternalCQL.g:4660:1: rule__Create_Stream__Group_5__1__Impl : ( ( rule__Create_Stream__AttributesAssignment_5_1 ) ) ;
    public final void rule__Create_Stream__Group_5__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4664:1: ( ( ( rule__Create_Stream__AttributesAssignment_5_1 ) ) )
            // InternalCQL.g:4665:1: ( ( rule__Create_Stream__AttributesAssignment_5_1 ) )
            {
            // InternalCQL.g:4665:1: ( ( rule__Create_Stream__AttributesAssignment_5_1 ) )
            // InternalCQL.g:4666:2: ( rule__Create_Stream__AttributesAssignment_5_1 )
            {
             before(grammarAccess.getCreate_StreamAccess().getAttributesAssignment_5_1()); 
            // InternalCQL.g:4667:2: ( rule__Create_Stream__AttributesAssignment_5_1 )
            // InternalCQL.g:4667:3: rule__Create_Stream__AttributesAssignment_5_1
            {
            pushFollow(FOLLOW_2);
            rule__Create_Stream__AttributesAssignment_5_1();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StreamAccess().getAttributesAssignment_5_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group_5__1__Impl"


    // $ANTLR start "rule__Create_Stream__Group_5__2"
    // InternalCQL.g:4675:1: rule__Create_Stream__Group_5__2 : rule__Create_Stream__Group_5__2__Impl ;
    public final void rule__Create_Stream__Group_5__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4679:1: ( rule__Create_Stream__Group_5__2__Impl )
            // InternalCQL.g:4680:2: rule__Create_Stream__Group_5__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Create_Stream__Group_5__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group_5__2"


    // $ANTLR start "rule__Create_Stream__Group_5__2__Impl"
    // InternalCQL.g:4686:1: rule__Create_Stream__Group_5__2__Impl : ( ( rule__Create_Stream__DatatypesAssignment_5_2 ) ) ;
    public final void rule__Create_Stream__Group_5__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4690:1: ( ( ( rule__Create_Stream__DatatypesAssignment_5_2 ) ) )
            // InternalCQL.g:4691:1: ( ( rule__Create_Stream__DatatypesAssignment_5_2 ) )
            {
            // InternalCQL.g:4691:1: ( ( rule__Create_Stream__DatatypesAssignment_5_2 ) )
            // InternalCQL.g:4692:2: ( rule__Create_Stream__DatatypesAssignment_5_2 )
            {
             before(grammarAccess.getCreate_StreamAccess().getDatatypesAssignment_5_2()); 
            // InternalCQL.g:4693:2: ( rule__Create_Stream__DatatypesAssignment_5_2 )
            // InternalCQL.g:4693:3: rule__Create_Stream__DatatypesAssignment_5_2
            {
            pushFollow(FOLLOW_2);
            rule__Create_Stream__DatatypesAssignment_5_2();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StreamAccess().getDatatypesAssignment_5_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__Group_5__2__Impl"


    // $ANTLR start "rule__Model__StatementsAssignment"
    // InternalCQL.g:4702:1: rule__Model__StatementsAssignment : ( ruleStatement ) ;
    public final void rule__Model__StatementsAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4706:1: ( ( ruleStatement ) )
            // InternalCQL.g:4707:2: ( ruleStatement )
            {
            // InternalCQL.g:4707:2: ( ruleStatement )
            // InternalCQL.g:4708:3: ruleStatement
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
    // InternalCQL.g:4717:1: rule__Statement__TypeAssignment_0_0 : ( ruleSelect_Statement ) ;
    public final void rule__Statement__TypeAssignment_0_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4721:1: ( ( ruleSelect_Statement ) )
            // InternalCQL.g:4722:2: ( ruleSelect_Statement )
            {
            // InternalCQL.g:4722:2: ( ruleSelect_Statement )
            // InternalCQL.g:4723:3: ruleSelect_Statement
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
    // InternalCQL.g:4732:1: rule__Statement__TypeAssignment_0_1 : ( ruleCreate_Statement ) ;
    public final void rule__Statement__TypeAssignment_0_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4736:1: ( ( ruleCreate_Statement ) )
            // InternalCQL.g:4737:2: ( ruleCreate_Statement )
            {
            // InternalCQL.g:4737:2: ( ruleCreate_Statement )
            // InternalCQL.g:4738:3: ruleCreate_Statement
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
    // InternalCQL.g:4747:1: rule__Atomic__ValueAssignment_0_1 : ( RULE_INT ) ;
    public final void rule__Atomic__ValueAssignment_0_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4751:1: ( ( RULE_INT ) )
            // InternalCQL.g:4752:2: ( RULE_INT )
            {
            // InternalCQL.g:4752:2: ( RULE_INT )
            // InternalCQL.g:4753:3: RULE_INT
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
    // InternalCQL.g:4762:1: rule__Atomic__ValueAssignment_1_1 : ( RULE_FLOAT_NUMBER ) ;
    public final void rule__Atomic__ValueAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4766:1: ( ( RULE_FLOAT_NUMBER ) )
            // InternalCQL.g:4767:2: ( RULE_FLOAT_NUMBER )
            {
            // InternalCQL.g:4767:2: ( RULE_FLOAT_NUMBER )
            // InternalCQL.g:4768:3: RULE_FLOAT_NUMBER
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
    // InternalCQL.g:4777:1: rule__Atomic__ValueAssignment_2_1 : ( RULE_STRING ) ;
    public final void rule__Atomic__ValueAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4781:1: ( ( RULE_STRING ) )
            // InternalCQL.g:4782:2: ( RULE_STRING )
            {
            // InternalCQL.g:4782:2: ( RULE_STRING )
            // InternalCQL.g:4783:3: RULE_STRING
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
    // InternalCQL.g:4792:1: rule__Atomic__ValueAssignment_3_1 : ( ( rule__Atomic__ValueAlternatives_3_1_0 ) ) ;
    public final void rule__Atomic__ValueAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4796:1: ( ( ( rule__Atomic__ValueAlternatives_3_1_0 ) ) )
            // InternalCQL.g:4797:2: ( ( rule__Atomic__ValueAlternatives_3_1_0 ) )
            {
            // InternalCQL.g:4797:2: ( ( rule__Atomic__ValueAlternatives_3_1_0 ) )
            // InternalCQL.g:4798:3: ( rule__Atomic__ValueAlternatives_3_1_0 )
            {
             before(grammarAccess.getAtomicAccess().getValueAlternatives_3_1_0()); 
            // InternalCQL.g:4799:3: ( rule__Atomic__ValueAlternatives_3_1_0 )
            // InternalCQL.g:4799:4: rule__Atomic__ValueAlternatives_3_1_0
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
    // InternalCQL.g:4807:1: rule__Atomic__ValueAssignment_4_1 : ( ruleAttribute ) ;
    public final void rule__Atomic__ValueAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4811:1: ( ( ruleAttribute ) )
            // InternalCQL.g:4812:2: ( ruleAttribute )
            {
            // InternalCQL.g:4812:2: ( ruleAttribute )
            // InternalCQL.g:4813:3: ruleAttribute
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


    // $ANTLR start "rule__Source__NameAssignment_0"
    // InternalCQL.g:4822:1: rule__Source__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__Source__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4826:1: ( ( RULE_ID ) )
            // InternalCQL.g:4827:2: ( RULE_ID )
            {
            // InternalCQL.g:4827:2: ( RULE_ID )
            // InternalCQL.g:4828:3: RULE_ID
            {
             before(grammarAccess.getSourceAccess().getNameIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getSourceAccess().getNameIDTerminalRuleCall_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Source__NameAssignment_0"


    // $ANTLR start "rule__Source__UnboundedAssignment_1_1_0"
    // InternalCQL.g:4837:1: rule__Source__UnboundedAssignment_1_1_0 : ( ruleWindow_Unbounded ) ;
    public final void rule__Source__UnboundedAssignment_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4841:1: ( ( ruleWindow_Unbounded ) )
            // InternalCQL.g:4842:2: ( ruleWindow_Unbounded )
            {
            // InternalCQL.g:4842:2: ( ruleWindow_Unbounded )
            // InternalCQL.g:4843:3: ruleWindow_Unbounded
            {
             before(grammarAccess.getSourceAccess().getUnboundedWindow_UnboundedParserRuleCall_1_1_0_0()); 
            pushFollow(FOLLOW_2);
            ruleWindow_Unbounded();

            state._fsp--;

             after(grammarAccess.getSourceAccess().getUnboundedWindow_UnboundedParserRuleCall_1_1_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Source__UnboundedAssignment_1_1_0"


    // $ANTLR start "rule__Source__TimeAssignment_1_1_1"
    // InternalCQL.g:4852:1: rule__Source__TimeAssignment_1_1_1 : ( ruleWindow_Timebased ) ;
    public final void rule__Source__TimeAssignment_1_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4856:1: ( ( ruleWindow_Timebased ) )
            // InternalCQL.g:4857:2: ( ruleWindow_Timebased )
            {
            // InternalCQL.g:4857:2: ( ruleWindow_Timebased )
            // InternalCQL.g:4858:3: ruleWindow_Timebased
            {
             before(grammarAccess.getSourceAccess().getTimeWindow_TimebasedParserRuleCall_1_1_1_0()); 
            pushFollow(FOLLOW_2);
            ruleWindow_Timebased();

            state._fsp--;

             after(grammarAccess.getSourceAccess().getTimeWindow_TimebasedParserRuleCall_1_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Source__TimeAssignment_1_1_1"


    // $ANTLR start "rule__Source__TupleAssignment_1_1_2"
    // InternalCQL.g:4867:1: rule__Source__TupleAssignment_1_1_2 : ( ruleWindow_Tuplebased ) ;
    public final void rule__Source__TupleAssignment_1_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4871:1: ( ( ruleWindow_Tuplebased ) )
            // InternalCQL.g:4872:2: ( ruleWindow_Tuplebased )
            {
            // InternalCQL.g:4872:2: ( ruleWindow_Tuplebased )
            // InternalCQL.g:4873:3: ruleWindow_Tuplebased
            {
             before(grammarAccess.getSourceAccess().getTupleWindow_TuplebasedParserRuleCall_1_1_2_0()); 
            pushFollow(FOLLOW_2);
            ruleWindow_Tuplebased();

            state._fsp--;

             after(grammarAccess.getSourceAccess().getTupleWindow_TuplebasedParserRuleCall_1_1_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Source__TupleAssignment_1_1_2"


    // $ANTLR start "rule__Attribute__NameAssignment"
    // InternalCQL.g:4882:1: rule__Attribute__NameAssignment : ( RULE_ID ) ;
    public final void rule__Attribute__NameAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4886:1: ( ( RULE_ID ) )
            // InternalCQL.g:4887:2: ( RULE_ID )
            {
            // InternalCQL.g:4887:2: ( RULE_ID )
            // InternalCQL.g:4888:3: RULE_ID
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
    // InternalCQL.g:4897:1: rule__ExpressionsModel__ElementsAssignment_1 : ( ruleExpression ) ;
    public final void rule__ExpressionsModel__ElementsAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4901:1: ( ( ruleExpression ) )
            // InternalCQL.g:4902:2: ( ruleExpression )
            {
            // InternalCQL.g:4902:2: ( ruleExpression )
            // InternalCQL.g:4903:3: ruleExpression
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
    // InternalCQL.g:4912:1: rule__Or__RightAssignment_1_2 : ( ruleAnd ) ;
    public final void rule__Or__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4916:1: ( ( ruleAnd ) )
            // InternalCQL.g:4917:2: ( ruleAnd )
            {
            // InternalCQL.g:4917:2: ( ruleAnd )
            // InternalCQL.g:4918:3: ruleAnd
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
    // InternalCQL.g:4927:1: rule__And__RightAssignment_1_2 : ( ruleEqualitiy ) ;
    public final void rule__And__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4931:1: ( ( ruleEqualitiy ) )
            // InternalCQL.g:4932:2: ( ruleEqualitiy )
            {
            // InternalCQL.g:4932:2: ( ruleEqualitiy )
            // InternalCQL.g:4933:3: ruleEqualitiy
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
    // InternalCQL.g:4942:1: rule__Equalitiy__OpAssignment_1_1 : ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) ) ;
    public final void rule__Equalitiy__OpAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4946:1: ( ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) ) )
            // InternalCQL.g:4947:2: ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) )
            {
            // InternalCQL.g:4947:2: ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) )
            // InternalCQL.g:4948:3: ( rule__Equalitiy__OpAlternatives_1_1_0 )
            {
             before(grammarAccess.getEqualitiyAccess().getOpAlternatives_1_1_0()); 
            // InternalCQL.g:4949:3: ( rule__Equalitiy__OpAlternatives_1_1_0 )
            // InternalCQL.g:4949:4: rule__Equalitiy__OpAlternatives_1_1_0
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
    // InternalCQL.g:4957:1: rule__Equalitiy__RightAssignment_1_2 : ( ruleComparison ) ;
    public final void rule__Equalitiy__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4961:1: ( ( ruleComparison ) )
            // InternalCQL.g:4962:2: ( ruleComparison )
            {
            // InternalCQL.g:4962:2: ( ruleComparison )
            // InternalCQL.g:4963:3: ruleComparison
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
    // InternalCQL.g:4972:1: rule__Comparison__OpAssignment_1_1 : ( ( rule__Comparison__OpAlternatives_1_1_0 ) ) ;
    public final void rule__Comparison__OpAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4976:1: ( ( ( rule__Comparison__OpAlternatives_1_1_0 ) ) )
            // InternalCQL.g:4977:2: ( ( rule__Comparison__OpAlternatives_1_1_0 ) )
            {
            // InternalCQL.g:4977:2: ( ( rule__Comparison__OpAlternatives_1_1_0 ) )
            // InternalCQL.g:4978:3: ( rule__Comparison__OpAlternatives_1_1_0 )
            {
             before(grammarAccess.getComparisonAccess().getOpAlternatives_1_1_0()); 
            // InternalCQL.g:4979:3: ( rule__Comparison__OpAlternatives_1_1_0 )
            // InternalCQL.g:4979:4: rule__Comparison__OpAlternatives_1_1_0
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
    // InternalCQL.g:4987:1: rule__Comparison__RightAssignment_1_2 : ( rulePlusOrMinus ) ;
    public final void rule__Comparison__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:4991:1: ( ( rulePlusOrMinus ) )
            // InternalCQL.g:4992:2: ( rulePlusOrMinus )
            {
            // InternalCQL.g:4992:2: ( rulePlusOrMinus )
            // InternalCQL.g:4993:3: rulePlusOrMinus
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
    // InternalCQL.g:5002:1: rule__PlusOrMinus__RightAssignment_1_1 : ( ruleMulOrDiv ) ;
    public final void rule__PlusOrMinus__RightAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5006:1: ( ( ruleMulOrDiv ) )
            // InternalCQL.g:5007:2: ( ruleMulOrDiv )
            {
            // InternalCQL.g:5007:2: ( ruleMulOrDiv )
            // InternalCQL.g:5008:3: ruleMulOrDiv
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
    // InternalCQL.g:5017:1: rule__MulOrDiv__OpAssignment_1_1 : ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) ) ;
    public final void rule__MulOrDiv__OpAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5021:1: ( ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) ) )
            // InternalCQL.g:5022:2: ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) )
            {
            // InternalCQL.g:5022:2: ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) )
            // InternalCQL.g:5023:3: ( rule__MulOrDiv__OpAlternatives_1_1_0 )
            {
             before(grammarAccess.getMulOrDivAccess().getOpAlternatives_1_1_0()); 
            // InternalCQL.g:5024:3: ( rule__MulOrDiv__OpAlternatives_1_1_0 )
            // InternalCQL.g:5024:4: rule__MulOrDiv__OpAlternatives_1_1_0
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
    // InternalCQL.g:5032:1: rule__MulOrDiv__RightAssignment_1_2 : ( rulePrimary ) ;
    public final void rule__MulOrDiv__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5036:1: ( ( rulePrimary ) )
            // InternalCQL.g:5037:2: ( rulePrimary )
            {
            // InternalCQL.g:5037:2: ( rulePrimary )
            // InternalCQL.g:5038:3: rulePrimary
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
    // InternalCQL.g:5047:1: rule__Primary__InnerAssignment_0_2 : ( ruleExpression ) ;
    public final void rule__Primary__InnerAssignment_0_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5051:1: ( ( ruleExpression ) )
            // InternalCQL.g:5052:2: ( ruleExpression )
            {
            // InternalCQL.g:5052:2: ( ruleExpression )
            // InternalCQL.g:5053:3: ruleExpression
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
    // InternalCQL.g:5062:1: rule__Primary__ExpressionAssignment_1_2 : ( rulePrimary ) ;
    public final void rule__Primary__ExpressionAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5066:1: ( ( rulePrimary ) )
            // InternalCQL.g:5067:2: ( rulePrimary )
            {
            // InternalCQL.g:5067:2: ( rulePrimary )
            // InternalCQL.g:5068:3: rulePrimary
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
    // InternalCQL.g:5077:1: rule__Select_Statement__NameAssignment_0 : ( ( 'SELECT' ) ) ;
    public final void rule__Select_Statement__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5081:1: ( ( ( 'SELECT' ) ) )
            // InternalCQL.g:5082:2: ( ( 'SELECT' ) )
            {
            // InternalCQL.g:5082:2: ( ( 'SELECT' ) )
            // InternalCQL.g:5083:3: ( 'SELECT' )
            {
             before(grammarAccess.getSelect_StatementAccess().getNameSELECTKeyword_0_0()); 
            // InternalCQL.g:5084:3: ( 'SELECT' )
            // InternalCQL.g:5085:4: 'SELECT'
            {
             before(grammarAccess.getSelect_StatementAccess().getNameSELECTKeyword_0_0()); 
            match(input,61,FOLLOW_2); 
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
    // InternalCQL.g:5096:1: rule__Select_Statement__AttributesAssignment_2_1_0 : ( ruleAttribute ) ;
    public final void rule__Select_Statement__AttributesAssignment_2_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5100:1: ( ( ruleAttribute ) )
            // InternalCQL.g:5101:2: ( ruleAttribute )
            {
            // InternalCQL.g:5101:2: ( ruleAttribute )
            // InternalCQL.g:5102:3: ruleAttribute
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
    // InternalCQL.g:5111:1: rule__Select_Statement__AttributesAssignment_2_1_1_1 : ( ruleAttribute ) ;
    public final void rule__Select_Statement__AttributesAssignment_2_1_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5115:1: ( ( ruleAttribute ) )
            // InternalCQL.g:5116:2: ( ruleAttribute )
            {
            // InternalCQL.g:5116:2: ( ruleAttribute )
            // InternalCQL.g:5117:3: ruleAttribute
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
    // InternalCQL.g:5126:1: rule__Select_Statement__SourcesAssignment_4_0 : ( ruleSource ) ;
    public final void rule__Select_Statement__SourcesAssignment_4_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5130:1: ( ( ruleSource ) )
            // InternalCQL.g:5131:2: ( ruleSource )
            {
            // InternalCQL.g:5131:2: ( ruleSource )
            // InternalCQL.g:5132:3: ruleSource
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
    // InternalCQL.g:5141:1: rule__Select_Statement__SourcesAssignment_4_1_1 : ( ruleSource ) ;
    public final void rule__Select_Statement__SourcesAssignment_4_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5145:1: ( ( ruleSource ) )
            // InternalCQL.g:5146:2: ( ruleSource )
            {
            // InternalCQL.g:5146:2: ( ruleSource )
            // InternalCQL.g:5147:3: ruleSource
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
    // InternalCQL.g:5156:1: rule__Select_Statement__PredicatesAssignment_5_1 : ( ruleExpressionsModel ) ;
    public final void rule__Select_Statement__PredicatesAssignment_5_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5160:1: ( ( ruleExpressionsModel ) )
            // InternalCQL.g:5161:2: ( ruleExpressionsModel )
            {
            // InternalCQL.g:5161:2: ( ruleExpressionsModel )
            // InternalCQL.g:5162:3: ruleExpressionsModel
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


    // $ANTLR start "rule__Window_Timebased__SizeAssignment_1"
    // InternalCQL.g:5171:1: rule__Window_Timebased__SizeAssignment_1 : ( RULE_INT ) ;
    public final void rule__Window_Timebased__SizeAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5175:1: ( ( RULE_INT ) )
            // InternalCQL.g:5176:2: ( RULE_INT )
            {
            // InternalCQL.g:5176:2: ( RULE_INT )
            // InternalCQL.g:5177:3: RULE_INT
            {
             before(grammarAccess.getWindow_TimebasedAccess().getSizeINTTerminalRuleCall_1_0()); 
            match(input,RULE_INT,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getSizeINTTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__SizeAssignment_1"


    // $ANTLR start "rule__Window_Timebased__UnitAssignment_2"
    // InternalCQL.g:5186:1: rule__Window_Timebased__UnitAssignment_2 : ( RULE_ID ) ;
    public final void rule__Window_Timebased__UnitAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5190:1: ( ( RULE_ID ) )
            // InternalCQL.g:5191:2: ( RULE_ID )
            {
            // InternalCQL.g:5191:2: ( RULE_ID )
            // InternalCQL.g:5192:3: RULE_ID
            {
             before(grammarAccess.getWindow_TimebasedAccess().getUnitIDTerminalRuleCall_2_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getUnitIDTerminalRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__UnitAssignment_2"


    // $ANTLR start "rule__Window_Timebased__Advance_sizeAssignment_3_1"
    // InternalCQL.g:5201:1: rule__Window_Timebased__Advance_sizeAssignment_3_1 : ( RULE_INT ) ;
    public final void rule__Window_Timebased__Advance_sizeAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5205:1: ( ( RULE_INT ) )
            // InternalCQL.g:5206:2: ( RULE_INT )
            {
            // InternalCQL.g:5206:2: ( RULE_INT )
            // InternalCQL.g:5207:3: RULE_INT
            {
             before(grammarAccess.getWindow_TimebasedAccess().getAdvance_sizeINTTerminalRuleCall_3_1_0()); 
            match(input,RULE_INT,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getAdvance_sizeINTTerminalRuleCall_3_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Advance_sizeAssignment_3_1"


    // $ANTLR start "rule__Window_Timebased__Advance_unitAssignment_3_2"
    // InternalCQL.g:5216:1: rule__Window_Timebased__Advance_unitAssignment_3_2 : ( RULE_ID ) ;
    public final void rule__Window_Timebased__Advance_unitAssignment_3_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5220:1: ( ( RULE_ID ) )
            // InternalCQL.g:5221:2: ( RULE_ID )
            {
            // InternalCQL.g:5221:2: ( RULE_ID )
            // InternalCQL.g:5222:3: RULE_ID
            {
             before(grammarAccess.getWindow_TimebasedAccess().getAdvance_unitIDTerminalRuleCall_3_2_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getWindow_TimebasedAccess().getAdvance_unitIDTerminalRuleCall_3_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Timebased__Advance_unitAssignment_3_2"


    // $ANTLR start "rule__Window_Tuplebased__SizeAssignment_1"
    // InternalCQL.g:5231:1: rule__Window_Tuplebased__SizeAssignment_1 : ( RULE_INT ) ;
    public final void rule__Window_Tuplebased__SizeAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5235:1: ( ( RULE_INT ) )
            // InternalCQL.g:5236:2: ( RULE_INT )
            {
            // InternalCQL.g:5236:2: ( RULE_INT )
            // InternalCQL.g:5237:3: RULE_INT
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getSizeINTTerminalRuleCall_1_0()); 
            match(input,RULE_INT,FOLLOW_2); 
             after(grammarAccess.getWindow_TuplebasedAccess().getSizeINTTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__SizeAssignment_1"


    // $ANTLR start "rule__Window_Tuplebased__Advance_sizeAssignment_2_1"
    // InternalCQL.g:5246:1: rule__Window_Tuplebased__Advance_sizeAssignment_2_1 : ( RULE_INT ) ;
    public final void rule__Window_Tuplebased__Advance_sizeAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5250:1: ( ( RULE_INT ) )
            // InternalCQL.g:5251:2: ( RULE_INT )
            {
            // InternalCQL.g:5251:2: ( RULE_INT )
            // InternalCQL.g:5252:3: RULE_INT
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getAdvance_sizeINTTerminalRuleCall_2_1_0()); 
            match(input,RULE_INT,FOLLOW_2); 
             after(grammarAccess.getWindow_TuplebasedAccess().getAdvance_sizeINTTerminalRuleCall_2_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Advance_sizeAssignment_2_1"


    // $ANTLR start "rule__Window_Tuplebased__Partition_attributeAssignment_4_2"
    // InternalCQL.g:5261:1: rule__Window_Tuplebased__Partition_attributeAssignment_4_2 : ( ruleAttribute ) ;
    public final void rule__Window_Tuplebased__Partition_attributeAssignment_4_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5265:1: ( ( ruleAttribute ) )
            // InternalCQL.g:5266:2: ( ruleAttribute )
            {
            // InternalCQL.g:5266:2: ( ruleAttribute )
            // InternalCQL.g:5267:3: ruleAttribute
            {
             before(grammarAccess.getWindow_TuplebasedAccess().getPartition_attributeAttributeParserRuleCall_4_2_0()); 
            pushFollow(FOLLOW_2);
            ruleAttribute();

            state._fsp--;

             after(grammarAccess.getWindow_TuplebasedAccess().getPartition_attributeAttributeParserRuleCall_4_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Window_Tuplebased__Partition_attributeAssignment_4_2"


    // $ANTLR start "rule__Create_Statement__StreamAssignment_1_0"
    // InternalCQL.g:5276:1: rule__Create_Statement__StreamAssignment_1_0 : ( ruleCreate_Stream ) ;
    public final void rule__Create_Statement__StreamAssignment_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5280:1: ( ( ruleCreate_Stream ) )
            // InternalCQL.g:5281:2: ( ruleCreate_Stream )
            {
            // InternalCQL.g:5281:2: ( ruleCreate_Stream )
            // InternalCQL.g:5282:3: ruleCreate_Stream
            {
             before(grammarAccess.getCreate_StatementAccess().getStreamCreate_StreamParserRuleCall_1_0_0()); 
            pushFollow(FOLLOW_2);
            ruleCreate_Stream();

            state._fsp--;

             after(grammarAccess.getCreate_StatementAccess().getStreamCreate_StreamParserRuleCall_1_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__StreamAssignment_1_0"


    // $ANTLR start "rule__Create_Statement__SinkAssignment_1_1"
    // InternalCQL.g:5291:1: rule__Create_Statement__SinkAssignment_1_1 : ( ruleCreate_Sink ) ;
    public final void rule__Create_Statement__SinkAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5295:1: ( ( ruleCreate_Sink ) )
            // InternalCQL.g:5296:2: ( ruleCreate_Sink )
            {
            // InternalCQL.g:5296:2: ( ruleCreate_Sink )
            // InternalCQL.g:5297:3: ruleCreate_Sink
            {
             before(grammarAccess.getCreate_StatementAccess().getSinkCreate_SinkParserRuleCall_1_1_0()); 
            pushFollow(FOLLOW_2);
            ruleCreate_Sink();

            state._fsp--;

             after(grammarAccess.getCreate_StatementAccess().getSinkCreate_SinkParserRuleCall_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__SinkAssignment_1_1"


    // $ANTLR start "rule__Create_Sink__NameAssignment_1"
    // InternalCQL.g:5306:1: rule__Create_Sink__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__Create_Sink__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5310:1: ( ( RULE_ID ) )
            // InternalCQL.g:5311:2: ( RULE_ID )
            {
            // InternalCQL.g:5311:2: ( RULE_ID )
            // InternalCQL.g:5312:3: RULE_ID
            {
             before(grammarAccess.getCreate_SinkAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__NameAssignment_1"


    // $ANTLR start "rule__Create_Sink__AttributesAssignment_3"
    // InternalCQL.g:5321:1: rule__Create_Sink__AttributesAssignment_3 : ( ruleAttribute ) ;
    public final void rule__Create_Sink__AttributesAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5325:1: ( ( ruleAttribute ) )
            // InternalCQL.g:5326:2: ( ruleAttribute )
            {
            // InternalCQL.g:5326:2: ( ruleAttribute )
            // InternalCQL.g:5327:3: ruleAttribute
            {
             before(grammarAccess.getCreate_SinkAccess().getAttributesAttributeParserRuleCall_3_0()); 
            pushFollow(FOLLOW_2);
            ruleAttribute();

            state._fsp--;

             after(grammarAccess.getCreate_SinkAccess().getAttributesAttributeParserRuleCall_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__AttributesAssignment_3"


    // $ANTLR start "rule__Create_Sink__DatatypesAssignment_4"
    // InternalCQL.g:5336:1: rule__Create_Sink__DatatypesAssignment_4 : ( ruleDataType ) ;
    public final void rule__Create_Sink__DatatypesAssignment_4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5340:1: ( ( ruleDataType ) )
            // InternalCQL.g:5341:2: ( ruleDataType )
            {
            // InternalCQL.g:5341:2: ( ruleDataType )
            // InternalCQL.g:5342:3: ruleDataType
            {
             before(grammarAccess.getCreate_SinkAccess().getDatatypesDataTypeParserRuleCall_4_0()); 
            pushFollow(FOLLOW_2);
            ruleDataType();

            state._fsp--;

             after(grammarAccess.getCreate_SinkAccess().getDatatypesDataTypeParserRuleCall_4_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__DatatypesAssignment_4"


    // $ANTLR start "rule__Create_Sink__AttributesAssignment_5_1"
    // InternalCQL.g:5351:1: rule__Create_Sink__AttributesAssignment_5_1 : ( ruleAttribute ) ;
    public final void rule__Create_Sink__AttributesAssignment_5_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5355:1: ( ( ruleAttribute ) )
            // InternalCQL.g:5356:2: ( ruleAttribute )
            {
            // InternalCQL.g:5356:2: ( ruleAttribute )
            // InternalCQL.g:5357:3: ruleAttribute
            {
             before(grammarAccess.getCreate_SinkAccess().getAttributesAttributeParserRuleCall_5_1_0()); 
            pushFollow(FOLLOW_2);
            ruleAttribute();

            state._fsp--;

             after(grammarAccess.getCreate_SinkAccess().getAttributesAttributeParserRuleCall_5_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__AttributesAssignment_5_1"


    // $ANTLR start "rule__Create_Sink__DatatypesAssignment_5_2"
    // InternalCQL.g:5366:1: rule__Create_Sink__DatatypesAssignment_5_2 : ( ruleDataType ) ;
    public final void rule__Create_Sink__DatatypesAssignment_5_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5370:1: ( ( ruleDataType ) )
            // InternalCQL.g:5371:2: ( ruleDataType )
            {
            // InternalCQL.g:5371:2: ( ruleDataType )
            // InternalCQL.g:5372:3: ruleDataType
            {
             before(grammarAccess.getCreate_SinkAccess().getDatatypesDataTypeParserRuleCall_5_2_0()); 
            pushFollow(FOLLOW_2);
            ruleDataType();

            state._fsp--;

             after(grammarAccess.getCreate_SinkAccess().getDatatypesDataTypeParserRuleCall_5_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__DatatypesAssignment_5_2"


    // $ANTLR start "rule__Create_Sink__WrapperAssignment_8"
    // InternalCQL.g:5381:1: rule__Create_Sink__WrapperAssignment_8 : ( RULE_STRING ) ;
    public final void rule__Create_Sink__WrapperAssignment_8() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5385:1: ( ( RULE_STRING ) )
            // InternalCQL.g:5386:2: ( RULE_STRING )
            {
            // InternalCQL.g:5386:2: ( RULE_STRING )
            // InternalCQL.g:5387:3: RULE_STRING
            {
             before(grammarAccess.getCreate_SinkAccess().getWrapperSTRINGTerminalRuleCall_8_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getWrapperSTRINGTerminalRuleCall_8_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__WrapperAssignment_8"


    // $ANTLR start "rule__Create_Sink__ProtocolAssignment_10"
    // InternalCQL.g:5396:1: rule__Create_Sink__ProtocolAssignment_10 : ( RULE_STRING ) ;
    public final void rule__Create_Sink__ProtocolAssignment_10() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5400:1: ( ( RULE_STRING ) )
            // InternalCQL.g:5401:2: ( RULE_STRING )
            {
            // InternalCQL.g:5401:2: ( RULE_STRING )
            // InternalCQL.g:5402:3: RULE_STRING
            {
             before(grammarAccess.getCreate_SinkAccess().getProtocolSTRINGTerminalRuleCall_10_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getProtocolSTRINGTerminalRuleCall_10_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__ProtocolAssignment_10"


    // $ANTLR start "rule__Create_Sink__TransportAssignment_12"
    // InternalCQL.g:5411:1: rule__Create_Sink__TransportAssignment_12 : ( RULE_STRING ) ;
    public final void rule__Create_Sink__TransportAssignment_12() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5415:1: ( ( RULE_STRING ) )
            // InternalCQL.g:5416:2: ( RULE_STRING )
            {
            // InternalCQL.g:5416:2: ( RULE_STRING )
            // InternalCQL.g:5417:3: RULE_STRING
            {
             before(grammarAccess.getCreate_SinkAccess().getTransportSTRINGTerminalRuleCall_12_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getTransportSTRINGTerminalRuleCall_12_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__TransportAssignment_12"


    // $ANTLR start "rule__Create_Sink__DatahandlerAssignment_14"
    // InternalCQL.g:5426:1: rule__Create_Sink__DatahandlerAssignment_14 : ( RULE_STRING ) ;
    public final void rule__Create_Sink__DatahandlerAssignment_14() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5430:1: ( ( RULE_STRING ) )
            // InternalCQL.g:5431:2: ( RULE_STRING )
            {
            // InternalCQL.g:5431:2: ( RULE_STRING )
            // InternalCQL.g:5432:3: RULE_STRING
            {
             before(grammarAccess.getCreate_SinkAccess().getDatahandlerSTRINGTerminalRuleCall_14_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getDatahandlerSTRINGTerminalRuleCall_14_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__DatahandlerAssignment_14"


    // $ANTLR start "rule__Create_Sink__KeysAssignment_17_0"
    // InternalCQL.g:5441:1: rule__Create_Sink__KeysAssignment_17_0 : ( RULE_STRING ) ;
    public final void rule__Create_Sink__KeysAssignment_17_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5445:1: ( ( RULE_STRING ) )
            // InternalCQL.g:5446:2: ( RULE_STRING )
            {
            // InternalCQL.g:5446:2: ( RULE_STRING )
            // InternalCQL.g:5447:3: RULE_STRING
            {
             before(grammarAccess.getCreate_SinkAccess().getKeysSTRINGTerminalRuleCall_17_0_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getKeysSTRINGTerminalRuleCall_17_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__KeysAssignment_17_0"


    // $ANTLR start "rule__Create_Sink__ValuesAssignment_17_1"
    // InternalCQL.g:5456:1: rule__Create_Sink__ValuesAssignment_17_1 : ( RULE_STRING ) ;
    public final void rule__Create_Sink__ValuesAssignment_17_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5460:1: ( ( RULE_STRING ) )
            // InternalCQL.g:5461:2: ( RULE_STRING )
            {
            // InternalCQL.g:5461:2: ( RULE_STRING )
            // InternalCQL.g:5462:3: RULE_STRING
            {
             before(grammarAccess.getCreate_SinkAccess().getValuesSTRINGTerminalRuleCall_17_1_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getValuesSTRINGTerminalRuleCall_17_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__ValuesAssignment_17_1"


    // $ANTLR start "rule__Create_Sink__KeysAssignment_18_1"
    // InternalCQL.g:5471:1: rule__Create_Sink__KeysAssignment_18_1 : ( RULE_STRING ) ;
    public final void rule__Create_Sink__KeysAssignment_18_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5475:1: ( ( RULE_STRING ) )
            // InternalCQL.g:5476:2: ( RULE_STRING )
            {
            // InternalCQL.g:5476:2: ( RULE_STRING )
            // InternalCQL.g:5477:3: RULE_STRING
            {
             before(grammarAccess.getCreate_SinkAccess().getKeysSTRINGTerminalRuleCall_18_1_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getKeysSTRINGTerminalRuleCall_18_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__KeysAssignment_18_1"


    // $ANTLR start "rule__Create_Sink__ValuesAssignment_18_2"
    // InternalCQL.g:5486:1: rule__Create_Sink__ValuesAssignment_18_2 : ( RULE_STRING ) ;
    public final void rule__Create_Sink__ValuesAssignment_18_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5490:1: ( ( RULE_STRING ) )
            // InternalCQL.g:5491:2: ( RULE_STRING )
            {
            // InternalCQL.g:5491:2: ( RULE_STRING )
            // InternalCQL.g:5492:3: RULE_STRING
            {
             before(grammarAccess.getCreate_SinkAccess().getValuesSTRINGTerminalRuleCall_18_2_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getCreate_SinkAccess().getValuesSTRINGTerminalRuleCall_18_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Sink__ValuesAssignment_18_2"


    // $ANTLR start "rule__Create_Stream__NameAssignment_1"
    // InternalCQL.g:5501:1: rule__Create_Stream__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__Create_Stream__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5505:1: ( ( RULE_ID ) )
            // InternalCQL.g:5506:2: ( RULE_ID )
            {
            // InternalCQL.g:5506:2: ( RULE_ID )
            // InternalCQL.g:5507:3: RULE_ID
            {
             before(grammarAccess.getCreate_StreamAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getCreate_StreamAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__NameAssignment_1"


    // $ANTLR start "rule__Create_Stream__AttributesAssignment_3"
    // InternalCQL.g:5516:1: rule__Create_Stream__AttributesAssignment_3 : ( ruleAttribute ) ;
    public final void rule__Create_Stream__AttributesAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5520:1: ( ( ruleAttribute ) )
            // InternalCQL.g:5521:2: ( ruleAttribute )
            {
            // InternalCQL.g:5521:2: ( ruleAttribute )
            // InternalCQL.g:5522:3: ruleAttribute
            {
             before(grammarAccess.getCreate_StreamAccess().getAttributesAttributeParserRuleCall_3_0()); 
            pushFollow(FOLLOW_2);
            ruleAttribute();

            state._fsp--;

             after(grammarAccess.getCreate_StreamAccess().getAttributesAttributeParserRuleCall_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__AttributesAssignment_3"


    // $ANTLR start "rule__Create_Stream__DatatypesAssignment_4"
    // InternalCQL.g:5531:1: rule__Create_Stream__DatatypesAssignment_4 : ( ruleDataType ) ;
    public final void rule__Create_Stream__DatatypesAssignment_4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5535:1: ( ( ruleDataType ) )
            // InternalCQL.g:5536:2: ( ruleDataType )
            {
            // InternalCQL.g:5536:2: ( ruleDataType )
            // InternalCQL.g:5537:3: ruleDataType
            {
             before(grammarAccess.getCreate_StreamAccess().getDatatypesDataTypeParserRuleCall_4_0()); 
            pushFollow(FOLLOW_2);
            ruleDataType();

            state._fsp--;

             after(grammarAccess.getCreate_StreamAccess().getDatatypesDataTypeParserRuleCall_4_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__DatatypesAssignment_4"


    // $ANTLR start "rule__Create_Stream__AttributesAssignment_5_1"
    // InternalCQL.g:5546:1: rule__Create_Stream__AttributesAssignment_5_1 : ( ruleAttribute ) ;
    public final void rule__Create_Stream__AttributesAssignment_5_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5550:1: ( ( ruleAttribute ) )
            // InternalCQL.g:5551:2: ( ruleAttribute )
            {
            // InternalCQL.g:5551:2: ( ruleAttribute )
            // InternalCQL.g:5552:3: ruleAttribute
            {
             before(grammarAccess.getCreate_StreamAccess().getAttributesAttributeParserRuleCall_5_1_0()); 
            pushFollow(FOLLOW_2);
            ruleAttribute();

            state._fsp--;

             after(grammarAccess.getCreate_StreamAccess().getAttributesAttributeParserRuleCall_5_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__AttributesAssignment_5_1"


    // $ANTLR start "rule__Create_Stream__DatatypesAssignment_5_2"
    // InternalCQL.g:5561:1: rule__Create_Stream__DatatypesAssignment_5_2 : ( ruleDataType ) ;
    public final void rule__Create_Stream__DatatypesAssignment_5_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5565:1: ( ( ruleDataType ) )
            // InternalCQL.g:5566:2: ( ruleDataType )
            {
            // InternalCQL.g:5566:2: ( ruleDataType )
            // InternalCQL.g:5567:3: ruleDataType
            {
             before(grammarAccess.getCreate_StreamAccess().getDatatypesDataTypeParserRuleCall_5_2_0()); 
            pushFollow(FOLLOW_2);
            ruleDataType();

            state._fsp--;

             after(grammarAccess.getCreate_StreamAccess().getDatatypesDataTypeParserRuleCall_5_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__DatatypesAssignment_5_2"


    // $ANTLR start "rule__Create_Stream__HostAssignment_8"
    // InternalCQL.g:5576:1: rule__Create_Stream__HostAssignment_8 : ( RULE_ID ) ;
    public final void rule__Create_Stream__HostAssignment_8() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5580:1: ( ( RULE_ID ) )
            // InternalCQL.g:5581:2: ( RULE_ID )
            {
            // InternalCQL.g:5581:2: ( RULE_ID )
            // InternalCQL.g:5582:3: RULE_ID
            {
             before(grammarAccess.getCreate_StreamAccess().getHostIDTerminalRuleCall_8_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getCreate_StreamAccess().getHostIDTerminalRuleCall_8_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__HostAssignment_8"


    // $ANTLR start "rule__Create_Stream__PortAssignment_10"
    // InternalCQL.g:5591:1: rule__Create_Stream__PortAssignment_10 : ( RULE_INT ) ;
    public final void rule__Create_Stream__PortAssignment_10() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:5595:1: ( ( RULE_INT ) )
            // InternalCQL.g:5596:2: ( RULE_INT )
            {
            // InternalCQL.g:5596:2: ( RULE_INT )
            // InternalCQL.g:5597:3: RULE_INT
            {
             before(grammarAccess.getCreate_StreamAccess().getPortINTTerminalRuleCall_10_0()); 
            match(input,RULE_INT,FOLLOW_2); 
             after(grammarAccess.getCreate_StreamAccess().getPortINTTerminalRuleCall_10_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Stream__PortAssignment_10"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x2010000000000002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000006000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000400000001000L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x00000280000060F0L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x00000280000060F2L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000800000002L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000001000000002L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000000C00000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000000C00002L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x000000000F000000L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x000000000F000002L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000006000000000L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000006000000002L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000030000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000030000002L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000040010000080L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000040010000082L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000100000000002L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0001800000000000L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0003000000000000L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x00200000C0000000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x00000000003F8000L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0000110000000000L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x00000000003F8002L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x1000000000000000L});

}