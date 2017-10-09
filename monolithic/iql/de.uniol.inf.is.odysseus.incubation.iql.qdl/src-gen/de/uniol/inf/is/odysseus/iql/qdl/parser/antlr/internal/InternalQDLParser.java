package de.uniol.inf.is.odysseus.iql.qdl.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import de.uniol.inf.is.odysseus.iql.qdl.services.QDLGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
@SuppressWarnings("all")
public class InternalQDLParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_INT", "RULE_DOUBLE", "RULE_STRING", "RULE_WS", "RULE_ANY_OTHER", "RULE_RANGE", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "'query'", "'('", "')'", "'instanceof'", "':'", "'->'", "'<-'", "'null'", "'+'", "'+='", "'-'", "'-='", "'*'", "'*='", "'/'", "'/='", "'%'", "'%='", "'++'", "'--'", "'>'", "'>='", "'<'", "'<='", "'!'", "'!='", "'&&'", "'||'", "'=='", "'='", "'~'", "'?:'", "'|'", "'|='", "'^'", "'^='", "'&'", "'&='", "'>>'", "'>>='", "'<<'", "'<<='", "'>>>'", "'>>>='", "'['", "']'", "'{'", "'}'", "'.'", "';'", "','", "'use'", "'static'", "'class'", "'extends'", "'implements'", "'interface'", "'override'", "'if'", "'else'", "'while'", "'do'", "'for'", "'switch'", "'default'", "'case'", "'this'", "'super'", "'break'", "'continue'", "'return'", "'new'", "'class('", "'::*'", "'::'", "'$*'", "'*$'", "'package'", "'abstract'", "'assert'", "'catch'", "'const'", "'enum'", "'final'", "'finally'", "'goto'", "'import'", "'native'", "'private'", "'protected'", "'public'", "'synchronized'", "'throw'", "'throws'", "'transient'", "'try'", "'volatile'", "'strictfp'", "'true'", "'false'"
    };
    public static final int T__50=50;
    public static final int T__59=59;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__60=60;
    public static final int T__61=61;
    public static final int RULE_ID=4;
    public static final int RULE_INT=5;
    public static final int T__66=66;
    public static final int RULE_ML_COMMENT=11;
    public static final int T__67=67;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
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
    public static final int T__91=91;
    public static final int T__100=100;
    public static final int T__92=92;
    public static final int T__93=93;
    public static final int T__102=102;
    public static final int T__94=94;
    public static final int T__101=101;
    public static final int T__90=90;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__99=99;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int T__95=95;
    public static final int T__96=96;
    public static final int T__97=97;
    public static final int T__98=98;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__20=20;
    public static final int T__21=21;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int RULE_STRING=7;
    public static final int RULE_SL_COMMENT=12;
    public static final int RULE_DOUBLE=6;
    public static final int T__77=77;
    public static final int T__78=78;
    public static final int T__79=79;
    public static final int T__73=73;
    public static final int EOF=-1;
    public static final int T__74=74;
    public static final int T__75=75;
    public static final int T__76=76;
    public static final int T__80=80;
    public static final int T__111=111;
    public static final int T__81=81;
    public static final int T__110=110;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int T__112=112;
    public static final int RULE_WS=8;
    public static final int RULE_ANY_OTHER=9;
    public static final int T__88=88;
    public static final int T__108=108;
    public static final int T__89=89;
    public static final int T__107=107;
    public static final int T__109=109;
    public static final int T__84=84;
    public static final int T__104=104;
    public static final int T__85=85;
    public static final int T__103=103;
    public static final int T__86=86;
    public static final int T__106=106;
    public static final int T__87=87;
    public static final int T__105=105;
    public static final int RULE_RANGE=10;

    // delegates
    // delegators


        public InternalQDLParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalQDLParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalQDLParser.tokenNames; }
    public String getGrammarFileName() { return "InternalQDL.g"; }



     	private QDLGrammarAccess grammarAccess;

        public InternalQDLParser(TokenStream input, QDLGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "QDLModel";
       	}

       	@Override
       	protected QDLGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleQDLModel"
    // InternalQDL.g:64:1: entryRuleQDLModel returns [EObject current=null] : iv_ruleQDLModel= ruleQDLModel EOF ;
    public final EObject entryRuleQDLModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleQDLModel = null;


        try {
            // InternalQDL.g:64:49: (iv_ruleQDLModel= ruleQDLModel EOF )
            // InternalQDL.g:65:2: iv_ruleQDLModel= ruleQDLModel EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getQDLModelRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleQDLModel=ruleQDLModel();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleQDLModel; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleQDLModel"


    // $ANTLR start "ruleQDLModel"
    // InternalQDL.g:71:1: ruleQDLModel returns [EObject current=null] : ( ( (lv_namespaces_0_0= ruleIQLNamespace ) )* ( (lv_elements_1_0= ruleQDLModelElement ) )* ) ;
    public final EObject ruleQDLModel() throws RecognitionException {
        EObject current = null;

        EObject lv_namespaces_0_0 = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:77:2: ( ( ( (lv_namespaces_0_0= ruleIQLNamespace ) )* ( (lv_elements_1_0= ruleQDLModelElement ) )* ) )
            // InternalQDL.g:78:2: ( ( (lv_namespaces_0_0= ruleIQLNamespace ) )* ( (lv_elements_1_0= ruleQDLModelElement ) )* )
            {
            // InternalQDL.g:78:2: ( ( (lv_namespaces_0_0= ruleIQLNamespace ) )* ( (lv_elements_1_0= ruleQDLModelElement ) )* )
            // InternalQDL.g:79:3: ( (lv_namespaces_0_0= ruleIQLNamespace ) )* ( (lv_elements_1_0= ruleQDLModelElement ) )*
            {
            // InternalQDL.g:79:3: ( (lv_namespaces_0_0= ruleIQLNamespace ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==64) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalQDL.g:80:4: (lv_namespaces_0_0= ruleIQLNamespace )
            	    {
            	    // InternalQDL.g:80:4: (lv_namespaces_0_0= ruleIQLNamespace )
            	    // InternalQDL.g:81:5: lv_namespaces_0_0= ruleIQLNamespace
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getQDLModelAccess().getNamespacesIQLNamespaceParserRuleCall_0_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_3);
            	    lv_namespaces_0_0=ruleIQLNamespace();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getQDLModelRule());
            	      					}
            	      					add(
            	      						current,
            	      						"namespaces",
            	      						lv_namespaces_0_0,
            	      						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLNamespace");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            // InternalQDL.g:98:3: ( (lv_elements_1_0= ruleQDLModelElement ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==13||LA2_0==66||LA2_0==69||LA2_0==88) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalQDL.g:99:4: (lv_elements_1_0= ruleQDLModelElement )
            	    {
            	    // InternalQDL.g:99:4: (lv_elements_1_0= ruleQDLModelElement )
            	    // InternalQDL.g:100:5: lv_elements_1_0= ruleQDLModelElement
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getQDLModelAccess().getElementsQDLModelElementParserRuleCall_1_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_4);
            	    lv_elements_1_0=ruleQDLModelElement();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getQDLModelRule());
            	      					}
            	      					add(
            	      						current,
            	      						"elements",
            	      						lv_elements_1_0,
            	      						"de.uniol.inf.is.odysseus.iql.qdl.QDL.QDLModelElement");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleQDLModel"


    // $ANTLR start "entryRuleQDLModelElement"
    // InternalQDL.g:121:1: entryRuleQDLModelElement returns [EObject current=null] : iv_ruleQDLModelElement= ruleQDLModelElement EOF ;
    public final EObject entryRuleQDLModelElement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleQDLModelElement = null;


        try {
            // InternalQDL.g:121:56: (iv_ruleQDLModelElement= ruleQDLModelElement EOF )
            // InternalQDL.g:122:2: iv_ruleQDLModelElement= ruleQDLModelElement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getQDLModelElementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleQDLModelElement=ruleQDLModelElement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleQDLModelElement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleQDLModelElement"


    // $ANTLR start "ruleQDLModelElement"
    // InternalQDL.g:128:1: ruleQDLModelElement returns [EObject current=null] : ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleQDLQuery ) ) ) ) ;
    public final EObject ruleQDLModelElement() throws RecognitionException {
        EObject current = null;

        EObject lv_javametadata_0_0 = null;

        EObject lv_inner_1_1 = null;

        EObject lv_inner_1_2 = null;

        EObject lv_inner_1_3 = null;



        	enterRule();

        try {
            // InternalQDL.g:134:2: ( ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleQDLQuery ) ) ) ) )
            // InternalQDL.g:135:2: ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleQDLQuery ) ) ) )
            {
            // InternalQDL.g:135:2: ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleQDLQuery ) ) ) )
            // InternalQDL.g:136:3: ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleQDLQuery ) ) )
            {
            // InternalQDL.g:136:3: ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==88) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalQDL.g:137:4: (lv_javametadata_0_0= ruleIQLJavaMetadata )
            	    {
            	    // InternalQDL.g:137:4: (lv_javametadata_0_0= ruleIQLJavaMetadata )
            	    // InternalQDL.g:138:5: lv_javametadata_0_0= ruleIQLJavaMetadata
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getQDLModelElementAccess().getJavametadataIQLJavaMetadataParserRuleCall_0_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_5);
            	    lv_javametadata_0_0=ruleIQLJavaMetadata();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getQDLModelElementRule());
            	      					}
            	      					add(
            	      						current,
            	      						"javametadata",
            	      						lv_javametadata_0_0,
            	      						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLJavaMetadata");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            // InternalQDL.g:155:3: ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleQDLQuery ) ) )
            // InternalQDL.g:156:4: ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleQDLQuery ) )
            {
            // InternalQDL.g:156:4: ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleQDLQuery ) )
            // InternalQDL.g:157:5: (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleQDLQuery )
            {
            // InternalQDL.g:157:5: (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleQDLQuery )
            int alt4=3;
            switch ( input.LA(1) ) {
            case 66:
                {
                alt4=1;
                }
                break;
            case 69:
                {
                alt4=2;
                }
                break;
            case 13:
                {
                alt4=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // InternalQDL.g:158:6: lv_inner_1_1= ruleIQLClass
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getQDLModelElementAccess().getInnerIQLClassParserRuleCall_1_0_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_inner_1_1=ruleIQLClass();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getQDLModelElementRule());
                      						}
                      						set(
                      							current,
                      							"inner",
                      							lv_inner_1_1,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLClass");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }
                    break;
                case 2 :
                    // InternalQDL.g:174:6: lv_inner_1_2= ruleIQLInterface
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getQDLModelElementAccess().getInnerIQLInterfaceParserRuleCall_1_0_1());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_inner_1_2=ruleIQLInterface();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getQDLModelElementRule());
                      						}
                      						set(
                      							current,
                      							"inner",
                      							lv_inner_1_2,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLInterface");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }
                    break;
                case 3 :
                    // InternalQDL.g:190:6: lv_inner_1_3= ruleQDLQuery
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getQDLModelElementAccess().getInnerQDLQueryParserRuleCall_1_0_2());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_inner_1_3=ruleQDLQuery();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getQDLModelElementRule());
                      						}
                      						set(
                      							current,
                      							"inner",
                      							lv_inner_1_3,
                      							"de.uniol.inf.is.odysseus.iql.qdl.QDL.QDLQuery");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }
                    break;

            }


            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleQDLModelElement"


    // $ANTLR start "entryRuleQDLQuery"
    // InternalQDL.g:212:1: entryRuleQDLQuery returns [EObject current=null] : iv_ruleQDLQuery= ruleQDLQuery EOF ;
    public final EObject entryRuleQDLQuery() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleQDLQuery = null;


        try {
            // InternalQDL.g:212:49: (iv_ruleQDLQuery= ruleQDLQuery EOF )
            // InternalQDL.g:213:2: iv_ruleQDLQuery= ruleQDLQuery EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getQDLQueryRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleQDLQuery=ruleQDLQuery();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleQDLQuery; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleQDLQuery"


    // $ANTLR start "ruleQDLQuery"
    // InternalQDL.g:219:1: ruleQDLQuery returns [EObject current=null] : ( () otherlv_1= 'query' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )? ( (lv_statements_6_0= ruleIQLStatementBlock ) ) ) ;
    public final EObject ruleQDLQuery() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_simpleName_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_metadataList_4_0 = null;

        EObject lv_statements_6_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:225:2: ( ( () otherlv_1= 'query' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )? ( (lv_statements_6_0= ruleIQLStatementBlock ) ) ) )
            // InternalQDL.g:226:2: ( () otherlv_1= 'query' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )? ( (lv_statements_6_0= ruleIQLStatementBlock ) ) )
            {
            // InternalQDL.g:226:2: ( () otherlv_1= 'query' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )? ( (lv_statements_6_0= ruleIQLStatementBlock ) ) )
            // InternalQDL.g:227:3: () otherlv_1= 'query' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )? ( (lv_statements_6_0= ruleIQLStatementBlock ) )
            {
            // InternalQDL.g:227:3: ()
            // InternalQDL.g:228:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getQDLQueryAccess().getQDLQueryAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,13,FOLLOW_6); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getQDLQueryAccess().getQueryKeyword_1());
              		
            }
            // InternalQDL.g:238:3: ( (lv_simpleName_2_0= RULE_ID ) )
            // InternalQDL.g:239:4: (lv_simpleName_2_0= RULE_ID )
            {
            // InternalQDL.g:239:4: (lv_simpleName_2_0= RULE_ID )
            // InternalQDL.g:240:5: lv_simpleName_2_0= RULE_ID
            {
            lv_simpleName_2_0=(Token)match(input,RULE_ID,FOLLOW_7); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_simpleName_2_0, grammarAccess.getQDLQueryAccess().getSimpleNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getQDLQueryRule());
              					}
              					setWithLastConsumed(
              						current,
              						"simpleName",
              						lv_simpleName_2_0,
              						"org.eclipse.xtext.common.Terminals.ID");
              				
            }

            }


            }

            // InternalQDL.g:256:3: (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==14) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // InternalQDL.g:257:4: otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')'
                    {
                    otherlv_3=(Token)match(input,14,FOLLOW_8); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getQDLQueryAccess().getLeftParenthesisKeyword_3_0());
                      			
                    }
                    // InternalQDL.g:261:4: ( (lv_metadataList_4_0= ruleIQLMetadataList ) )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==RULE_ID) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // InternalQDL.g:262:5: (lv_metadataList_4_0= ruleIQLMetadataList )
                            {
                            // InternalQDL.g:262:5: (lv_metadataList_4_0= ruleIQLMetadataList )
                            // InternalQDL.g:263:6: lv_metadataList_4_0= ruleIQLMetadataList
                            {
                            if ( state.backtracking==0 ) {

                              						newCompositeNode(grammarAccess.getQDLQueryAccess().getMetadataListIQLMetadataListParserRuleCall_3_1_0());
                              					
                            }
                            pushFollow(FOLLOW_9);
                            lv_metadataList_4_0=ruleIQLMetadataList();

                            state._fsp--;
                            if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              						if (current==null) {
                              							current = createModelElementForParent(grammarAccess.getQDLQueryRule());
                              						}
                              						set(
                              							current,
                              							"metadataList",
                              							lv_metadataList_4_0,
                              							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMetadataList");
                              						afterParserOrEnumRuleCall();
                              					
                            }

                            }


                            }
                            break;

                    }

                    otherlv_5=(Token)match(input,15,FOLLOW_7); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getQDLQueryAccess().getRightParenthesisKeyword_3_2());
                      			
                    }

                    }
                    break;

            }

            // InternalQDL.g:285:3: ( (lv_statements_6_0= ruleIQLStatementBlock ) )
            // InternalQDL.g:286:4: (lv_statements_6_0= ruleIQLStatementBlock )
            {
            // InternalQDL.g:286:4: (lv_statements_6_0= ruleIQLStatementBlock )
            // InternalQDL.g:287:5: lv_statements_6_0= ruleIQLStatementBlock
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getQDLQueryAccess().getStatementsIQLStatementBlockParserRuleCall_4_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_statements_6_0=ruleIQLStatementBlock();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getQDLQueryRule());
              					}
              					set(
              						current,
              						"statements",
              						lv_statements_6_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLStatementBlock");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleQDLQuery"


    // $ANTLR start "entryRuleIQLRelationalExpression"
    // InternalQDL.g:308:1: entryRuleIQLRelationalExpression returns [EObject current=null] : iv_ruleIQLRelationalExpression= ruleIQLRelationalExpression EOF ;
    public final EObject entryRuleIQLRelationalExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLRelationalExpression = null;


        try {
            // InternalQDL.g:308:64: (iv_ruleIQLRelationalExpression= ruleIQLRelationalExpression EOF )
            // InternalQDL.g:309:2: iv_ruleIQLRelationalExpression= ruleIQLRelationalExpression EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLRelationalExpressionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLRelationalExpression=ruleIQLRelationalExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLRelationalExpression; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLRelationalExpression"


    // $ANTLR start "ruleIQLRelationalExpression"
    // InternalQDL.g:315:1: ruleIQLRelationalExpression returns [EObject current=null] : (this_IQLSubscribeExpression_0= ruleIQLSubscribeExpression ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLSubscribeExpression ) ) ) )* ) ;
    public final EObject ruleIQLRelationalExpression() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_IQLSubscribeExpression_0 = null;

        EObject lv_targetRef_3_0 = null;

        AntlrDatatypeRuleToken lv_op_5_0 = null;

        EObject lv_rightOperand_6_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:321:2: ( (this_IQLSubscribeExpression_0= ruleIQLSubscribeExpression ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLSubscribeExpression ) ) ) )* ) )
            // InternalQDL.g:322:2: (this_IQLSubscribeExpression_0= ruleIQLSubscribeExpression ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLSubscribeExpression ) ) ) )* )
            {
            // InternalQDL.g:322:2: (this_IQLSubscribeExpression_0= ruleIQLSubscribeExpression ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLSubscribeExpression ) ) ) )* )
            // InternalQDL.g:323:3: this_IQLSubscribeExpression_0= ruleIQLSubscribeExpression ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLSubscribeExpression ) ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLRelationalExpressionAccess().getIQLSubscribeExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_10);
            this_IQLSubscribeExpression_0=ruleIQLSubscribeExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLSubscribeExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalQDL.g:331:3: ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLSubscribeExpression ) ) ) )*
            loop7:
            do {
                int alt7=3;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==16) && (synpred1_InternalQDL())) {
                    alt7=1;
                }
                else if ( (LA7_0==33) && (synpred2_InternalQDL())) {
                    alt7=2;
                }
                else if ( (LA7_0==34) && (synpred2_InternalQDL())) {
                    alt7=2;
                }
                else if ( (LA7_0==35) && (synpred2_InternalQDL())) {
                    alt7=2;
                }
                else if ( (LA7_0==36) && (synpred2_InternalQDL())) {
                    alt7=2;
                }


                switch (alt7) {
            	case 1 :
            	    // InternalQDL.g:332:4: ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) )
            	    {
            	    // InternalQDL.g:332:4: ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) )
            	    // InternalQDL.g:333:5: ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) )
            	    {
            	    // InternalQDL.g:333:5: ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) )
            	    // InternalQDL.g:334:6: ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' )
            	    {
            	    // InternalQDL.g:340:6: ( () otherlv_2= 'instanceof' )
            	    // InternalQDL.g:341:7: () otherlv_2= 'instanceof'
            	    {
            	    // InternalQDL.g:341:7: ()
            	    // InternalQDL.g:342:8: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      								current = forceCreateModelElementAndSet(
            	      									grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0(),
            	      									current);
            	      							
            	    }

            	    }

            	    otherlv_2=(Token)match(input,16,FOLLOW_6); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      							newLeafNode(otherlv_2, grammarAccess.getIQLRelationalExpressionAccess().getInstanceofKeyword_1_0_0_0_1());
            	      						
            	    }

            	    }


            	    }

            	    // InternalQDL.g:354:5: ( (lv_targetRef_3_0= ruleJvmTypeReference ) )
            	    // InternalQDL.g:355:6: (lv_targetRef_3_0= ruleJvmTypeReference )
            	    {
            	    // InternalQDL.g:355:6: (lv_targetRef_3_0= ruleJvmTypeReference )
            	    // InternalQDL.g:356:7: lv_targetRef_3_0= ruleJvmTypeReference
            	    {
            	    if ( state.backtracking==0 ) {

            	      							newCompositeNode(grammarAccess.getIQLRelationalExpressionAccess().getTargetRefJvmTypeReferenceParserRuleCall_1_0_1_0());
            	      						
            	    }
            	    pushFollow(FOLLOW_10);
            	    lv_targetRef_3_0=ruleJvmTypeReference();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      							if (current==null) {
            	      								current = createModelElementForParent(grammarAccess.getIQLRelationalExpressionRule());
            	      							}
            	      							set(
            	      								current,
            	      								"targetRef",
            	      								lv_targetRef_3_0,
            	      								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference");
            	      							afterParserOrEnumRuleCall();
            	      						
            	    }

            	    }


            	    }


            	    }


            	    }
            	    break;
            	case 2 :
            	    // InternalQDL.g:375:4: ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLSubscribeExpression ) ) )
            	    {
            	    // InternalQDL.g:375:4: ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLSubscribeExpression ) ) )
            	    // InternalQDL.g:376:5: ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLSubscribeExpression ) )
            	    {
            	    // InternalQDL.g:376:5: ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) )
            	    // InternalQDL.g:377:6: ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) )
            	    {
            	    // InternalQDL.g:387:6: ( () ( (lv_op_5_0= ruleOpRelational ) ) )
            	    // InternalQDL.g:388:7: () ( (lv_op_5_0= ruleOpRelational ) )
            	    {
            	    // InternalQDL.g:388:7: ()
            	    // InternalQDL.g:389:8: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      								current = forceCreateModelElementAndSet(
            	      									grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0(),
            	      									current);
            	      							
            	    }

            	    }

            	    // InternalQDL.g:395:7: ( (lv_op_5_0= ruleOpRelational ) )
            	    // InternalQDL.g:396:8: (lv_op_5_0= ruleOpRelational )
            	    {
            	    // InternalQDL.g:396:8: (lv_op_5_0= ruleOpRelational )
            	    // InternalQDL.g:397:9: lv_op_5_0= ruleOpRelational
            	    {
            	    if ( state.backtracking==0 ) {

            	      									newCompositeNode(grammarAccess.getIQLRelationalExpressionAccess().getOpOpRelationalParserRuleCall_1_1_0_0_1_0());
            	      								
            	    }
            	    pushFollow(FOLLOW_11);
            	    lv_op_5_0=ruleOpRelational();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      									if (current==null) {
            	      										current = createModelElementForParent(grammarAccess.getIQLRelationalExpressionRule());
            	      									}
            	      									set(
            	      										current,
            	      										"op",
            	      										lv_op_5_0,
            	      										"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.OpRelational");
            	      									afterParserOrEnumRuleCall();
            	      								
            	    }

            	    }


            	    }


            	    }


            	    }

            	    // InternalQDL.g:416:5: ( (lv_rightOperand_6_0= ruleIQLSubscribeExpression ) )
            	    // InternalQDL.g:417:6: (lv_rightOperand_6_0= ruleIQLSubscribeExpression )
            	    {
            	    // InternalQDL.g:417:6: (lv_rightOperand_6_0= ruleIQLSubscribeExpression )
            	    // InternalQDL.g:418:7: lv_rightOperand_6_0= ruleIQLSubscribeExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      							newCompositeNode(grammarAccess.getIQLRelationalExpressionAccess().getRightOperandIQLSubscribeExpressionParserRuleCall_1_1_1_0());
            	      						
            	    }
            	    pushFollow(FOLLOW_10);
            	    lv_rightOperand_6_0=ruleIQLSubscribeExpression();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      							if (current==null) {
            	      								current = createModelElementForParent(grammarAccess.getIQLRelationalExpressionRule());
            	      							}
            	      							set(
            	      								current,
            	      								"rightOperand",
            	      								lv_rightOperand_6_0,
            	      								"de.uniol.inf.is.odysseus.iql.qdl.QDL.IQLSubscribeExpression");
            	      							afterParserOrEnumRuleCall();
            	      						
            	    }

            	    }


            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLRelationalExpression"


    // $ANTLR start "entryRuleIQLSubscribeExpression"
    // InternalQDL.g:441:1: entryRuleIQLSubscribeExpression returns [EObject current=null] : iv_ruleIQLSubscribeExpression= ruleIQLSubscribeExpression EOF ;
    public final EObject entryRuleIQLSubscribeExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLSubscribeExpression = null;


        try {
            // InternalQDL.g:441:63: (iv_ruleIQLSubscribeExpression= ruleIQLSubscribeExpression EOF )
            // InternalQDL.g:442:2: iv_ruleIQLSubscribeExpression= ruleIQLSubscribeExpression EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLSubscribeExpressionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLSubscribeExpression=ruleIQLSubscribeExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLSubscribeExpression; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLSubscribeExpression"


    // $ANTLR start "ruleIQLSubscribeExpression"
    // InternalQDL.g:448:1: ruleIQLSubscribeExpression returns [EObject current=null] : (this_IQLPortExpression_0= ruleIQLPortExpression ( ( ( ( () ( ( ruleIQLSubscribe ) ) ) )=> ( () ( (lv_op_2_0= ruleIQLSubscribe ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLPortExpression ) ) )* ) ;
    public final EObject ruleIQLSubscribeExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLPortExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:454:2: ( (this_IQLPortExpression_0= ruleIQLPortExpression ( ( ( ( () ( ( ruleIQLSubscribe ) ) ) )=> ( () ( (lv_op_2_0= ruleIQLSubscribe ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLPortExpression ) ) )* ) )
            // InternalQDL.g:455:2: (this_IQLPortExpression_0= ruleIQLPortExpression ( ( ( ( () ( ( ruleIQLSubscribe ) ) ) )=> ( () ( (lv_op_2_0= ruleIQLSubscribe ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLPortExpression ) ) )* )
            {
            // InternalQDL.g:455:2: (this_IQLPortExpression_0= ruleIQLPortExpression ( ( ( ( () ( ( ruleIQLSubscribe ) ) ) )=> ( () ( (lv_op_2_0= ruleIQLSubscribe ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLPortExpression ) ) )* )
            // InternalQDL.g:456:3: this_IQLPortExpression_0= ruleIQLPortExpression ( ( ( ( () ( ( ruleIQLSubscribe ) ) ) )=> ( () ( (lv_op_2_0= ruleIQLSubscribe ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLPortExpression ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLSubscribeExpressionAccess().getIQLPortExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_12);
            this_IQLPortExpression_0=ruleIQLPortExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLPortExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalQDL.g:464:3: ( ( ( ( () ( ( ruleIQLSubscribe ) ) ) )=> ( () ( (lv_op_2_0= ruleIQLSubscribe ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLPortExpression ) ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==18) && (synpred3_InternalQDL())) {
                    alt8=1;
                }
                else if ( (LA8_0==19) && (synpred3_InternalQDL())) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // InternalQDL.g:465:4: ( ( ( () ( ( ruleIQLSubscribe ) ) ) )=> ( () ( (lv_op_2_0= ruleIQLSubscribe ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLPortExpression ) )
            	    {
            	    // InternalQDL.g:465:4: ( ( ( () ( ( ruleIQLSubscribe ) ) ) )=> ( () ( (lv_op_2_0= ruleIQLSubscribe ) ) ) )
            	    // InternalQDL.g:466:5: ( ( () ( ( ruleIQLSubscribe ) ) ) )=> ( () ( (lv_op_2_0= ruleIQLSubscribe ) ) )
            	    {
            	    // InternalQDL.g:476:5: ( () ( (lv_op_2_0= ruleIQLSubscribe ) ) )
            	    // InternalQDL.g:477:6: () ( (lv_op_2_0= ruleIQLSubscribe ) )
            	    {
            	    // InternalQDL.g:477:6: ()
            	    // InternalQDL.g:478:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    // InternalQDL.g:484:6: ( (lv_op_2_0= ruleIQLSubscribe ) )
            	    // InternalQDL.g:485:7: (lv_op_2_0= ruleIQLSubscribe )
            	    {
            	    // InternalQDL.g:485:7: (lv_op_2_0= ruleIQLSubscribe )
            	    // InternalQDL.g:486:8: lv_op_2_0= ruleIQLSubscribe
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLSubscribeExpressionAccess().getOpIQLSubscribeParserRuleCall_1_0_0_1_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_11);
            	    lv_op_2_0=ruleIQLSubscribe();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      								if (current==null) {
            	      									current = createModelElementForParent(grammarAccess.getIQLSubscribeExpressionRule());
            	      								}
            	      								set(
            	      									current,
            	      									"op",
            	      									lv_op_2_0,
            	      									"de.uniol.inf.is.odysseus.iql.qdl.QDL.IQLSubscribe");
            	      								afterParserOrEnumRuleCall();
            	      							
            	    }

            	    }


            	    }


            	    }


            	    }

            	    // InternalQDL.g:505:4: ( (lv_rightOperand_3_0= ruleIQLPortExpression ) )
            	    // InternalQDL.g:506:5: (lv_rightOperand_3_0= ruleIQLPortExpression )
            	    {
            	    // InternalQDL.g:506:5: (lv_rightOperand_3_0= ruleIQLPortExpression )
            	    // InternalQDL.g:507:6: lv_rightOperand_3_0= ruleIQLPortExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLSubscribeExpressionAccess().getRightOperandIQLPortExpressionParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_12);
            	    lv_rightOperand_3_0=ruleIQLPortExpression();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						if (current==null) {
            	      							current = createModelElementForParent(grammarAccess.getIQLSubscribeExpressionRule());
            	      						}
            	      						set(
            	      							current,
            	      							"rightOperand",
            	      							lv_rightOperand_3_0,
            	      							"de.uniol.inf.is.odysseus.iql.qdl.QDL.IQLPortExpression");
            	      						afterParserOrEnumRuleCall();
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLSubscribeExpression"


    // $ANTLR start "entryRuleIQLPortExpression"
    // InternalQDL.g:529:1: entryRuleIQLPortExpression returns [EObject current=null] : iv_ruleIQLPortExpression= ruleIQLPortExpression EOF ;
    public final EObject entryRuleIQLPortExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLPortExpression = null;


        try {
            // InternalQDL.g:529:58: (iv_ruleIQLPortExpression= ruleIQLPortExpression EOF )
            // InternalQDL.g:530:2: iv_ruleIQLPortExpression= ruleIQLPortExpression EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLPortExpressionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLPortExpression=ruleIQLPortExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLPortExpression; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLPortExpression"


    // $ANTLR start "ruleIQLPortExpression"
    // InternalQDL.g:536:1: ruleIQLPortExpression returns [EObject current=null] : (this_IQLAdditiveExpression_0= ruleIQLAdditiveExpression ( ( ( ( () ( ( ':' ) ) ) )=> ( () ( (lv_op_2_0= ':' ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAdditiveExpression ) ) )? ) ;
    public final EObject ruleIQLPortExpression() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_0=null;
        EObject this_IQLAdditiveExpression_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:542:2: ( (this_IQLAdditiveExpression_0= ruleIQLAdditiveExpression ( ( ( ( () ( ( ':' ) ) ) )=> ( () ( (lv_op_2_0= ':' ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAdditiveExpression ) ) )? ) )
            // InternalQDL.g:543:2: (this_IQLAdditiveExpression_0= ruleIQLAdditiveExpression ( ( ( ( () ( ( ':' ) ) ) )=> ( () ( (lv_op_2_0= ':' ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAdditiveExpression ) ) )? )
            {
            // InternalQDL.g:543:2: (this_IQLAdditiveExpression_0= ruleIQLAdditiveExpression ( ( ( ( () ( ( ':' ) ) ) )=> ( () ( (lv_op_2_0= ':' ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAdditiveExpression ) ) )? )
            // InternalQDL.g:544:3: this_IQLAdditiveExpression_0= ruleIQLAdditiveExpression ( ( ( ( () ( ( ':' ) ) ) )=> ( () ( (lv_op_2_0= ':' ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAdditiveExpression ) ) )?
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLPortExpressionAccess().getIQLAdditiveExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_13);
            this_IQLAdditiveExpression_0=ruleIQLAdditiveExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLAdditiveExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalQDL.g:552:3: ( ( ( ( () ( ( ':' ) ) ) )=> ( () ( (lv_op_2_0= ':' ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAdditiveExpression ) ) )?
            int alt9=2;
            alt9 = dfa9.predict(input);
            switch (alt9) {
                case 1 :
                    // InternalQDL.g:553:4: ( ( ( () ( ( ':' ) ) ) )=> ( () ( (lv_op_2_0= ':' ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAdditiveExpression ) )
                    {
                    // InternalQDL.g:553:4: ( ( ( () ( ( ':' ) ) ) )=> ( () ( (lv_op_2_0= ':' ) ) ) )
                    // InternalQDL.g:554:5: ( ( () ( ( ':' ) ) ) )=> ( () ( (lv_op_2_0= ':' ) ) )
                    {
                    // InternalQDL.g:564:5: ( () ( (lv_op_2_0= ':' ) ) )
                    // InternalQDL.g:565:6: () ( (lv_op_2_0= ':' ) )
                    {
                    // InternalQDL.g:565:6: ()
                    // InternalQDL.g:566:7: 
                    {
                    if ( state.backtracking==0 ) {

                      							current = forceCreateModelElementAndSet(
                      								grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0(),
                      								current);
                      						
                    }

                    }

                    // InternalQDL.g:572:6: ( (lv_op_2_0= ':' ) )
                    // InternalQDL.g:573:7: (lv_op_2_0= ':' )
                    {
                    // InternalQDL.g:573:7: (lv_op_2_0= ':' )
                    // InternalQDL.g:574:8: lv_op_2_0= ':'
                    {
                    lv_op_2_0=(Token)match(input,17,FOLLOW_11); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      								newLeafNode(lv_op_2_0, grammarAccess.getIQLPortExpressionAccess().getOpColonKeyword_1_0_0_1_0());
                      							
                    }
                    if ( state.backtracking==0 ) {

                      								if (current==null) {
                      									current = createModelElement(grammarAccess.getIQLPortExpressionRule());
                      								}
                      								setWithLastConsumed(current, "op", lv_op_2_0, ":");
                      							
                    }

                    }


                    }


                    }


                    }

                    // InternalQDL.g:588:4: ( (lv_rightOperand_3_0= ruleIQLAdditiveExpression ) )
                    // InternalQDL.g:589:5: (lv_rightOperand_3_0= ruleIQLAdditiveExpression )
                    {
                    // InternalQDL.g:589:5: (lv_rightOperand_3_0= ruleIQLAdditiveExpression )
                    // InternalQDL.g:590:6: lv_rightOperand_3_0= ruleIQLAdditiveExpression
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLPortExpressionAccess().getRightOperandIQLAdditiveExpressionParserRuleCall_1_1_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_rightOperand_3_0=ruleIQLAdditiveExpression();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLPortExpressionRule());
                      						}
                      						set(
                      							current,
                      							"rightOperand",
                      							lv_rightOperand_3_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLAdditiveExpression");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLPortExpression"


    // $ANTLR start "entryRuleIQLSubscribe"
    // InternalQDL.g:612:1: entryRuleIQLSubscribe returns [String current=null] : iv_ruleIQLSubscribe= ruleIQLSubscribe EOF ;
    public final String entryRuleIQLSubscribe() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleIQLSubscribe = null;


        try {
            // InternalQDL.g:612:52: (iv_ruleIQLSubscribe= ruleIQLSubscribe EOF )
            // InternalQDL.g:613:2: iv_ruleIQLSubscribe= ruleIQLSubscribe EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLSubscribeRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLSubscribe=ruleIQLSubscribe();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLSubscribe.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLSubscribe"


    // $ANTLR start "ruleIQLSubscribe"
    // InternalQDL.g:619:1: ruleIQLSubscribe returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '->' | kw= '<-' ) ;
    public final AntlrDatatypeRuleToken ruleIQLSubscribe() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalQDL.g:625:2: ( (kw= '->' | kw= '<-' ) )
            // InternalQDL.g:626:2: (kw= '->' | kw= '<-' )
            {
            // InternalQDL.g:626:2: (kw= '->' | kw= '<-' )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==18) ) {
                alt10=1;
            }
            else if ( (LA10_0==19) ) {
                alt10=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // InternalQDL.g:627:3: kw= '->'
                    {
                    kw=(Token)match(input,18,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQLSubscribeAccess().getHyphenMinusGreaterThanSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalQDL.g:633:3: kw= '<-'
                    {
                    kw=(Token)match(input,19,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQLSubscribeAccess().getLessThanSignHyphenMinusKeyword_1());
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLSubscribe"


    // $ANTLR start "entryRuleIQLMetadataValueSingle"
    // InternalQDL.g:642:1: entryRuleIQLMetadataValueSingle returns [EObject current=null] : iv_ruleIQLMetadataValueSingle= ruleIQLMetadataValueSingle EOF ;
    public final EObject entryRuleIQLMetadataValueSingle() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataValueSingle = null;


        try {
            // InternalQDL.g:642:63: (iv_ruleIQLMetadataValueSingle= ruleIQLMetadataValueSingle EOF )
            // InternalQDL.g:643:2: iv_ruleIQLMetadataValueSingle= ruleIQLMetadataValueSingle EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLMetadataValueSingleRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLMetadataValueSingle=ruleIQLMetadataValueSingle();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLMetadataValueSingle; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLMetadataValueSingle"


    // $ANTLR start "ruleIQLMetadataValueSingle"
    // InternalQDL.g:649:1: ruleIQLMetadataValueSingle returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( ( ( () ( ( RULE_ID ) ) ) )=> ( () ( (lv_value_9_0= RULE_ID ) ) ) ) | ( () ( (lv_value_11_0= ruleJvmTypeReference ) ) ) | ( () otherlv_13= 'null' ) ) ;
    public final EObject ruleIQLMetadataValueSingle() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        Token lv_value_9_0=null;
        Token otherlv_13=null;
        AntlrDatatypeRuleToken lv_value_7_0 = null;

        EObject lv_value_11_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:655:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( ( ( () ( ( RULE_ID ) ) ) )=> ( () ( (lv_value_9_0= RULE_ID ) ) ) ) | ( () ( (lv_value_11_0= ruleJvmTypeReference ) ) ) | ( () otherlv_13= 'null' ) ) )
            // InternalQDL.g:656:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( ( ( () ( ( RULE_ID ) ) ) )=> ( () ( (lv_value_9_0= RULE_ID ) ) ) ) | ( () ( (lv_value_11_0= ruleJvmTypeReference ) ) ) | ( () otherlv_13= 'null' ) )
            {
            // InternalQDL.g:656:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( ( ( () ( ( RULE_ID ) ) ) )=> ( () ( (lv_value_9_0= RULE_ID ) ) ) ) | ( () ( (lv_value_11_0= ruleJvmTypeReference ) ) ) | ( () otherlv_13= 'null' ) )
            int alt11=7;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt11=1;
                }
                break;
            case RULE_DOUBLE:
                {
                alt11=2;
                }
                break;
            case RULE_STRING:
                {
                alt11=3;
                }
                break;
            case 111:
            case 112:
                {
                alt11=4;
                }
                break;
            case RULE_ID:
                {
                int LA11_5 = input.LA(2);

                if ( (synpred5_InternalQDL()) ) {
                    alt11=5;
                }
                else if ( (true) ) {
                    alt11=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return current;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 11, 5, input);

                    throw nvae;
                }
                }
                break;
            case 20:
                {
                alt11=7;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }

            switch (alt11) {
                case 1 :
                    // InternalQDL.g:657:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalQDL.g:657:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalQDL.g:658:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalQDL.g:658:4: ()
                    // InternalQDL.g:659:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleIntAction_0_0(),
                      						current);
                      				
                    }

                    }

                    // InternalQDL.g:665:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalQDL.g:666:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalQDL.g:666:5: (lv_value_1_0= RULE_INT )
                    // InternalQDL.g:667:6: lv_value_1_0= RULE_INT
                    {
                    lv_value_1_0=(Token)match(input,RULE_INT,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_value_1_0, grammarAccess.getIQLMetadataValueSingleAccess().getValueINTTerminalRuleCall_0_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getIQLMetadataValueSingleRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"value",
                      							lv_value_1_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.INT");
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalQDL.g:685:3: ( () ( (lv_value_3_0= RULE_DOUBLE ) ) )
                    {
                    // InternalQDL.g:685:3: ( () ( (lv_value_3_0= RULE_DOUBLE ) ) )
                    // InternalQDL.g:686:4: () ( (lv_value_3_0= RULE_DOUBLE ) )
                    {
                    // InternalQDL.g:686:4: ()
                    // InternalQDL.g:687:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleDoubleAction_1_0(),
                      						current);
                      				
                    }

                    }

                    // InternalQDL.g:693:4: ( (lv_value_3_0= RULE_DOUBLE ) )
                    // InternalQDL.g:694:5: (lv_value_3_0= RULE_DOUBLE )
                    {
                    // InternalQDL.g:694:5: (lv_value_3_0= RULE_DOUBLE )
                    // InternalQDL.g:695:6: lv_value_3_0= RULE_DOUBLE
                    {
                    lv_value_3_0=(Token)match(input,RULE_DOUBLE,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_value_3_0, grammarAccess.getIQLMetadataValueSingleAccess().getValueDOUBLETerminalRuleCall_1_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getIQLMetadataValueSingleRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"value",
                      							lv_value_3_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.DOUBLE");
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalQDL.g:713:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalQDL.g:713:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalQDL.g:714:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalQDL.g:714:4: ()
                    // InternalQDL.g:715:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleStringAction_2_0(),
                      						current);
                      				
                    }

                    }

                    // InternalQDL.g:721:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalQDL.g:722:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalQDL.g:722:5: (lv_value_5_0= RULE_STRING )
                    // InternalQDL.g:723:6: lv_value_5_0= RULE_STRING
                    {
                    lv_value_5_0=(Token)match(input,RULE_STRING,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_value_5_0, grammarAccess.getIQLMetadataValueSingleAccess().getValueSTRINGTerminalRuleCall_2_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getIQLMetadataValueSingleRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"value",
                      							lv_value_5_0,
                      							"org.eclipse.xtext.common.Terminals.STRING");
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 4 :
                    // InternalQDL.g:741:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    {
                    // InternalQDL.g:741:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    // InternalQDL.g:742:4: () ( (lv_value_7_0= ruleBOOLEAN ) )
                    {
                    // InternalQDL.g:742:4: ()
                    // InternalQDL.g:743:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleBooleanAction_3_0(),
                      						current);
                      				
                    }

                    }

                    // InternalQDL.g:749:4: ( (lv_value_7_0= ruleBOOLEAN ) )
                    // InternalQDL.g:750:5: (lv_value_7_0= ruleBOOLEAN )
                    {
                    // InternalQDL.g:750:5: (lv_value_7_0= ruleBOOLEAN )
                    // InternalQDL.g:751:6: lv_value_7_0= ruleBOOLEAN
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMetadataValueSingleAccess().getValueBOOLEANParserRuleCall_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_value_7_0=ruleBOOLEAN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLMetadataValueSingleRule());
                      						}
                      						set(
                      							current,
                      							"value",
                      							lv_value_7_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.BOOLEAN");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 5 :
                    // InternalQDL.g:770:3: ( ( ( () ( ( RULE_ID ) ) ) )=> ( () ( (lv_value_9_0= RULE_ID ) ) ) )
                    {
                    // InternalQDL.g:770:3: ( ( ( () ( ( RULE_ID ) ) ) )=> ( () ( (lv_value_9_0= RULE_ID ) ) ) )
                    // InternalQDL.g:771:4: ( ( () ( ( RULE_ID ) ) ) )=> ( () ( (lv_value_9_0= RULE_ID ) ) )
                    {
                    // InternalQDL.g:781:4: ( () ( (lv_value_9_0= RULE_ID ) ) )
                    // InternalQDL.g:782:5: () ( (lv_value_9_0= RULE_ID ) )
                    {
                    // InternalQDL.g:782:5: ()
                    // InternalQDL.g:783:6: 
                    {
                    if ( state.backtracking==0 ) {

                      						current = forceCreateModelElement(
                      							grammarAccess.getIQLMetadataValueSingleAccess().getQDLMetadataValueSingleIDAction_4_0_0(),
                      							current);
                      					
                    }

                    }

                    // InternalQDL.g:789:5: ( (lv_value_9_0= RULE_ID ) )
                    // InternalQDL.g:790:6: (lv_value_9_0= RULE_ID )
                    {
                    // InternalQDL.g:790:6: (lv_value_9_0= RULE_ID )
                    // InternalQDL.g:791:7: lv_value_9_0= RULE_ID
                    {
                    lv_value_9_0=(Token)match(input,RULE_ID,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      							newLeafNode(lv_value_9_0, grammarAccess.getIQLMetadataValueSingleAccess().getValueIDTerminalRuleCall_4_0_1_0());
                      						
                    }
                    if ( state.backtracking==0 ) {

                      							if (current==null) {
                      								current = createModelElement(grammarAccess.getIQLMetadataValueSingleRule());
                      							}
                      							setWithLastConsumed(
                      								current,
                      								"value",
                      								lv_value_9_0,
                      								"org.eclipse.xtext.common.Terminals.ID");
                      						
                    }

                    }


                    }


                    }


                    }


                    }
                    break;
                case 6 :
                    // InternalQDL.g:810:3: ( () ( (lv_value_11_0= ruleJvmTypeReference ) ) )
                    {
                    // InternalQDL.g:810:3: ( () ( (lv_value_11_0= ruleJvmTypeReference ) ) )
                    // InternalQDL.g:811:4: () ( (lv_value_11_0= ruleJvmTypeReference ) )
                    {
                    // InternalQDL.g:811:4: ()
                    // InternalQDL.g:812:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleTypeRefAction_5_0(),
                      						current);
                      				
                    }

                    }

                    // InternalQDL.g:818:4: ( (lv_value_11_0= ruleJvmTypeReference ) )
                    // InternalQDL.g:819:5: (lv_value_11_0= ruleJvmTypeReference )
                    {
                    // InternalQDL.g:819:5: (lv_value_11_0= ruleJvmTypeReference )
                    // InternalQDL.g:820:6: lv_value_11_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMetadataValueSingleAccess().getValueJvmTypeReferenceParserRuleCall_5_1_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_value_11_0=ruleJvmTypeReference();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLMetadataValueSingleRule());
                      						}
                      						set(
                      							current,
                      							"value",
                      							lv_value_11_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 7 :
                    // InternalQDL.g:839:3: ( () otherlv_13= 'null' )
                    {
                    // InternalQDL.g:839:3: ( () otherlv_13= 'null' )
                    // InternalQDL.g:840:4: () otherlv_13= 'null'
                    {
                    // InternalQDL.g:840:4: ()
                    // InternalQDL.g:841:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleNullAction_6_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_13=(Token)match(input,20,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_13, grammarAccess.getIQLMetadataValueSingleAccess().getNullKeyword_6_1());
                      			
                    }

                    }


                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLMetadataValueSingle"


    // $ANTLR start "entryRuleIQLJavaText"
    // InternalQDL.g:856:1: entryRuleIQLJavaText returns [String current=null] : iv_ruleIQLJavaText= ruleIQLJavaText EOF ;
    public final String entryRuleIQLJavaText() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleIQLJavaText = null;


        try {
            // InternalQDL.g:856:51: (iv_ruleIQLJavaText= ruleIQLJavaText EOF )
            // InternalQDL.g:857:2: iv_ruleIQLJavaText= ruleIQLJavaText EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLJavaTextRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLJavaText=ruleIQLJavaText();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLJavaText.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLJavaText"


    // $ANTLR start "ruleIQLJavaText"
    // InternalQDL.g:863:1: ruleIQLJavaText returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_IQL_JAVA_KEYWORDS_0= ruleIQL_JAVA_KEYWORDS | this_WS_1= RULE_WS | this_ID_2= RULE_ID | this_BOOLEAN_3= ruleBOOLEAN | this_DOUBLE_4= RULE_DOUBLE | this_STRING_5= RULE_STRING | this_INT_6= RULE_INT | this_ANY_OTHER_7= RULE_ANY_OTHER | kw= '+' | kw= '+=' | kw= '-' | kw= '-=' | kw= '*' | kw= '*=' | kw= '/' | kw= '/=' | kw= '%' | kw= '%=' | kw= '++' | kw= '--' | kw= '>' | kw= '>=' | kw= '<' | kw= '<=' | kw= '!' | kw= '!=' | kw= '&&' | kw= '||' | kw= '==' | kw= '=' | kw= '~' | kw= '?:' | kw= '|' | kw= '|=' | kw= '^' | kw= '^=' | kw= '&' | kw= '&=' | kw= '>>' | kw= '>>=' | kw= '<<' | kw= '<<=' | kw= '>>>' | kw= '>>>=' | kw= '[' | kw= ']' | kw= '{' | kw= '}' | kw= '(' | kw= ')' | kw= '.' | kw= ':' | kw= ';' | kw= ',' | kw= 'null' | kw= 'query' )* ;
    public final AntlrDatatypeRuleToken ruleIQLJavaText() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_WS_1=null;
        Token this_ID_2=null;
        Token this_DOUBLE_4=null;
        Token this_STRING_5=null;
        Token this_INT_6=null;
        Token this_ANY_OTHER_7=null;
        Token kw=null;
        AntlrDatatypeRuleToken this_IQL_JAVA_KEYWORDS_0 = null;

        AntlrDatatypeRuleToken this_BOOLEAN_3 = null;



        	enterRule();

        try {
            // InternalQDL.g:869:2: ( (this_IQL_JAVA_KEYWORDS_0= ruleIQL_JAVA_KEYWORDS | this_WS_1= RULE_WS | this_ID_2= RULE_ID | this_BOOLEAN_3= ruleBOOLEAN | this_DOUBLE_4= RULE_DOUBLE | this_STRING_5= RULE_STRING | this_INT_6= RULE_INT | this_ANY_OTHER_7= RULE_ANY_OTHER | kw= '+' | kw= '+=' | kw= '-' | kw= '-=' | kw= '*' | kw= '*=' | kw= '/' | kw= '/=' | kw= '%' | kw= '%=' | kw= '++' | kw= '--' | kw= '>' | kw= '>=' | kw= '<' | kw= '<=' | kw= '!' | kw= '!=' | kw= '&&' | kw= '||' | kw= '==' | kw= '=' | kw= '~' | kw= '?:' | kw= '|' | kw= '|=' | kw= '^' | kw= '^=' | kw= '&' | kw= '&=' | kw= '>>' | kw= '>>=' | kw= '<<' | kw= '<<=' | kw= '>>>' | kw= '>>>=' | kw= '[' | kw= ']' | kw= '{' | kw= '}' | kw= '(' | kw= ')' | kw= '.' | kw= ':' | kw= ';' | kw= ',' | kw= 'null' | kw= 'query' )* )
            // InternalQDL.g:870:2: (this_IQL_JAVA_KEYWORDS_0= ruleIQL_JAVA_KEYWORDS | this_WS_1= RULE_WS | this_ID_2= RULE_ID | this_BOOLEAN_3= ruleBOOLEAN | this_DOUBLE_4= RULE_DOUBLE | this_STRING_5= RULE_STRING | this_INT_6= RULE_INT | this_ANY_OTHER_7= RULE_ANY_OTHER | kw= '+' | kw= '+=' | kw= '-' | kw= '-=' | kw= '*' | kw= '*=' | kw= '/' | kw= '/=' | kw= '%' | kw= '%=' | kw= '++' | kw= '--' | kw= '>' | kw= '>=' | kw= '<' | kw= '<=' | kw= '!' | kw= '!=' | kw= '&&' | kw= '||' | kw= '==' | kw= '=' | kw= '~' | kw= '?:' | kw= '|' | kw= '|=' | kw= '^' | kw= '^=' | kw= '&' | kw= '&=' | kw= '>>' | kw= '>>=' | kw= '<<' | kw= '<<=' | kw= '>>>' | kw= '>>>=' | kw= '[' | kw= ']' | kw= '{' | kw= '}' | kw= '(' | kw= ')' | kw= '.' | kw= ':' | kw= ';' | kw= ',' | kw= 'null' | kw= 'query' )*
            {
            // InternalQDL.g:870:2: (this_IQL_JAVA_KEYWORDS_0= ruleIQL_JAVA_KEYWORDS | this_WS_1= RULE_WS | this_ID_2= RULE_ID | this_BOOLEAN_3= ruleBOOLEAN | this_DOUBLE_4= RULE_DOUBLE | this_STRING_5= RULE_STRING | this_INT_6= RULE_INT | this_ANY_OTHER_7= RULE_ANY_OTHER | kw= '+' | kw= '+=' | kw= '-' | kw= '-=' | kw= '*' | kw= '*=' | kw= '/' | kw= '/=' | kw= '%' | kw= '%=' | kw= '++' | kw= '--' | kw= '>' | kw= '>=' | kw= '<' | kw= '<=' | kw= '!' | kw= '!=' | kw= '&&' | kw= '||' | kw= '==' | kw= '=' | kw= '~' | kw= '?:' | kw= '|' | kw= '|=' | kw= '^' | kw= '^=' | kw= '&' | kw= '&=' | kw= '>>' | kw= '>>=' | kw= '<<' | kw= '<<=' | kw= '>>>' | kw= '>>>=' | kw= '[' | kw= ']' | kw= '{' | kw= '}' | kw= '(' | kw= ')' | kw= '.' | kw= ':' | kw= ';' | kw= ',' | kw= 'null' | kw= 'query' )*
            loop12:
            do {
                int alt12=57;
                switch ( input.LA(1) ) {
                case 16:
                case 65:
                case 66:
                case 67:
                case 68:
                case 69:
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                case 76:
                case 77:
                case 78:
                case 79:
                case 80:
                case 81:
                case 82:
                case 83:
                case 84:
                case 90:
                case 91:
                case 92:
                case 93:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                    {
                    alt12=1;
                    }
                    break;
                case RULE_WS:
                    {
                    alt12=2;
                    }
                    break;
                case RULE_ID:
                    {
                    alt12=3;
                    }
                    break;
                case 111:
                case 112:
                    {
                    alt12=4;
                    }
                    break;
                case RULE_DOUBLE:
                    {
                    alt12=5;
                    }
                    break;
                case RULE_STRING:
                    {
                    alt12=6;
                    }
                    break;
                case RULE_INT:
                    {
                    alt12=7;
                    }
                    break;
                case RULE_ANY_OTHER:
                    {
                    alt12=8;
                    }
                    break;
                case 21:
                    {
                    alt12=9;
                    }
                    break;
                case 22:
                    {
                    alt12=10;
                    }
                    break;
                case 23:
                    {
                    alt12=11;
                    }
                    break;
                case 24:
                    {
                    alt12=12;
                    }
                    break;
                case 25:
                    {
                    alt12=13;
                    }
                    break;
                case 26:
                    {
                    alt12=14;
                    }
                    break;
                case 27:
                    {
                    alt12=15;
                    }
                    break;
                case 28:
                    {
                    alt12=16;
                    }
                    break;
                case 29:
                    {
                    alt12=17;
                    }
                    break;
                case 30:
                    {
                    alt12=18;
                    }
                    break;
                case 31:
                    {
                    alt12=19;
                    }
                    break;
                case 32:
                    {
                    alt12=20;
                    }
                    break;
                case 33:
                    {
                    alt12=21;
                    }
                    break;
                case 34:
                    {
                    alt12=22;
                    }
                    break;
                case 35:
                    {
                    alt12=23;
                    }
                    break;
                case 36:
                    {
                    alt12=24;
                    }
                    break;
                case 37:
                    {
                    alt12=25;
                    }
                    break;
                case 38:
                    {
                    alt12=26;
                    }
                    break;
                case 39:
                    {
                    alt12=27;
                    }
                    break;
                case 40:
                    {
                    alt12=28;
                    }
                    break;
                case 41:
                    {
                    alt12=29;
                    }
                    break;
                case 42:
                    {
                    alt12=30;
                    }
                    break;
                case 43:
                    {
                    alt12=31;
                    }
                    break;
                case 44:
                    {
                    alt12=32;
                    }
                    break;
                case 45:
                    {
                    alt12=33;
                    }
                    break;
                case 46:
                    {
                    alt12=34;
                    }
                    break;
                case 47:
                    {
                    alt12=35;
                    }
                    break;
                case 48:
                    {
                    alt12=36;
                    }
                    break;
                case 49:
                    {
                    alt12=37;
                    }
                    break;
                case 50:
                    {
                    alt12=38;
                    }
                    break;
                case 51:
                    {
                    alt12=39;
                    }
                    break;
                case 52:
                    {
                    alt12=40;
                    }
                    break;
                case 53:
                    {
                    alt12=41;
                    }
                    break;
                case 54:
                    {
                    alt12=42;
                    }
                    break;
                case 55:
                    {
                    alt12=43;
                    }
                    break;
                case 56:
                    {
                    alt12=44;
                    }
                    break;
                case 57:
                    {
                    alt12=45;
                    }
                    break;
                case 58:
                    {
                    alt12=46;
                    }
                    break;
                case 59:
                    {
                    alt12=47;
                    }
                    break;
                case 60:
                    {
                    alt12=48;
                    }
                    break;
                case 14:
                    {
                    alt12=49;
                    }
                    break;
                case 15:
                    {
                    alt12=50;
                    }
                    break;
                case 61:
                    {
                    alt12=51;
                    }
                    break;
                case 17:
                    {
                    alt12=52;
                    }
                    break;
                case 62:
                    {
                    alt12=53;
                    }
                    break;
                case 63:
                    {
                    alt12=54;
                    }
                    break;
                case 20:
                    {
                    alt12=55;
                    }
                    break;
                case 13:
                    {
                    alt12=56;
                    }
                    break;

                }

                switch (alt12) {
            	case 1 :
            	    // InternalQDL.g:871:3: this_IQL_JAVA_KEYWORDS_0= ruleIQL_JAVA_KEYWORDS
            	    {
            	    if ( state.backtracking==0 ) {

            	      			newCompositeNode(grammarAccess.getIQLJavaTextAccess().getIQL_JAVA_KEYWORDSParserRuleCall_0());
            	      		
            	    }
            	    pushFollow(FOLLOW_14);
            	    this_IQL_JAVA_KEYWORDS_0=ruleIQL_JAVA_KEYWORDS();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_IQL_JAVA_KEYWORDS_0);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			afterParserOrEnumRuleCall();
            	      		
            	    }

            	    }
            	    break;
            	case 2 :
            	    // InternalQDL.g:882:3: this_WS_1= RULE_WS
            	    {
            	    this_WS_1=(Token)match(input,RULE_WS,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_WS_1);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_WS_1, grammarAccess.getIQLJavaTextAccess().getWSTerminalRuleCall_1());
            	      		
            	    }

            	    }
            	    break;
            	case 3 :
            	    // InternalQDL.g:890:3: this_ID_2= RULE_ID
            	    {
            	    this_ID_2=(Token)match(input,RULE_ID,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_ID_2);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_ID_2, grammarAccess.getIQLJavaTextAccess().getIDTerminalRuleCall_2());
            	      		
            	    }

            	    }
            	    break;
            	case 4 :
            	    // InternalQDL.g:898:3: this_BOOLEAN_3= ruleBOOLEAN
            	    {
            	    if ( state.backtracking==0 ) {

            	      			newCompositeNode(grammarAccess.getIQLJavaTextAccess().getBOOLEANParserRuleCall_3());
            	      		
            	    }
            	    pushFollow(FOLLOW_14);
            	    this_BOOLEAN_3=ruleBOOLEAN();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_BOOLEAN_3);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			afterParserOrEnumRuleCall();
            	      		
            	    }

            	    }
            	    break;
            	case 5 :
            	    // InternalQDL.g:909:3: this_DOUBLE_4= RULE_DOUBLE
            	    {
            	    this_DOUBLE_4=(Token)match(input,RULE_DOUBLE,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_DOUBLE_4);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_DOUBLE_4, grammarAccess.getIQLJavaTextAccess().getDOUBLETerminalRuleCall_4());
            	      		
            	    }

            	    }
            	    break;
            	case 6 :
            	    // InternalQDL.g:917:3: this_STRING_5= RULE_STRING
            	    {
            	    this_STRING_5=(Token)match(input,RULE_STRING,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_STRING_5);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_STRING_5, grammarAccess.getIQLJavaTextAccess().getSTRINGTerminalRuleCall_5());
            	      		
            	    }

            	    }
            	    break;
            	case 7 :
            	    // InternalQDL.g:925:3: this_INT_6= RULE_INT
            	    {
            	    this_INT_6=(Token)match(input,RULE_INT,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_INT_6);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_INT_6, grammarAccess.getIQLJavaTextAccess().getINTTerminalRuleCall_6());
            	      		
            	    }

            	    }
            	    break;
            	case 8 :
            	    // InternalQDL.g:933:3: this_ANY_OTHER_7= RULE_ANY_OTHER
            	    {
            	    this_ANY_OTHER_7=(Token)match(input,RULE_ANY_OTHER,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_ANY_OTHER_7);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_ANY_OTHER_7, grammarAccess.getIQLJavaTextAccess().getANY_OTHERTerminalRuleCall_7());
            	      		
            	    }

            	    }
            	    break;
            	case 9 :
            	    // InternalQDL.g:941:3: kw= '+'
            	    {
            	    kw=(Token)match(input,21,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getPlusSignKeyword_8());
            	      		
            	    }

            	    }
            	    break;
            	case 10 :
            	    // InternalQDL.g:947:3: kw= '+='
            	    {
            	    kw=(Token)match(input,22,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getPlusSignEqualsSignKeyword_9());
            	      		
            	    }

            	    }
            	    break;
            	case 11 :
            	    // InternalQDL.g:953:3: kw= '-'
            	    {
            	    kw=(Token)match(input,23,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getHyphenMinusKeyword_10());
            	      		
            	    }

            	    }
            	    break;
            	case 12 :
            	    // InternalQDL.g:959:3: kw= '-='
            	    {
            	    kw=(Token)match(input,24,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getHyphenMinusEqualsSignKeyword_11());
            	      		
            	    }

            	    }
            	    break;
            	case 13 :
            	    // InternalQDL.g:965:3: kw= '*'
            	    {
            	    kw=(Token)match(input,25,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getAsteriskKeyword_12());
            	      		
            	    }

            	    }
            	    break;
            	case 14 :
            	    // InternalQDL.g:971:3: kw= '*='
            	    {
            	    kw=(Token)match(input,26,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getAsteriskEqualsSignKeyword_13());
            	      		
            	    }

            	    }
            	    break;
            	case 15 :
            	    // InternalQDL.g:977:3: kw= '/'
            	    {
            	    kw=(Token)match(input,27,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getSolidusKeyword_14());
            	      		
            	    }

            	    }
            	    break;
            	case 16 :
            	    // InternalQDL.g:983:3: kw= '/='
            	    {
            	    kw=(Token)match(input,28,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getSolidusEqualsSignKeyword_15());
            	      		
            	    }

            	    }
            	    break;
            	case 17 :
            	    // InternalQDL.g:989:3: kw= '%'
            	    {
            	    kw=(Token)match(input,29,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getPercentSignKeyword_16());
            	      		
            	    }

            	    }
            	    break;
            	case 18 :
            	    // InternalQDL.g:995:3: kw= '%='
            	    {
            	    kw=(Token)match(input,30,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getPercentSignEqualsSignKeyword_17());
            	      		
            	    }

            	    }
            	    break;
            	case 19 :
            	    // InternalQDL.g:1001:3: kw= '++'
            	    {
            	    kw=(Token)match(input,31,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getPlusSignPlusSignKeyword_18());
            	      		
            	    }

            	    }
            	    break;
            	case 20 :
            	    // InternalQDL.g:1007:3: kw= '--'
            	    {
            	    kw=(Token)match(input,32,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getHyphenMinusHyphenMinusKeyword_19());
            	      		
            	    }

            	    }
            	    break;
            	case 21 :
            	    // InternalQDL.g:1013:3: kw= '>'
            	    {
            	    kw=(Token)match(input,33,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignKeyword_20());
            	      		
            	    }

            	    }
            	    break;
            	case 22 :
            	    // InternalQDL.g:1019:3: kw= '>='
            	    {
            	    kw=(Token)match(input,34,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignEqualsSignKeyword_21());
            	      		
            	    }

            	    }
            	    break;
            	case 23 :
            	    // InternalQDL.g:1025:3: kw= '<'
            	    {
            	    kw=(Token)match(input,35,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLessThanSignKeyword_22());
            	      		
            	    }

            	    }
            	    break;
            	case 24 :
            	    // InternalQDL.g:1031:3: kw= '<='
            	    {
            	    kw=(Token)match(input,36,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLessThanSignEqualsSignKeyword_23());
            	      		
            	    }

            	    }
            	    break;
            	case 25 :
            	    // InternalQDL.g:1037:3: kw= '!'
            	    {
            	    kw=(Token)match(input,37,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getExclamationMarkKeyword_24());
            	      		
            	    }

            	    }
            	    break;
            	case 26 :
            	    // InternalQDL.g:1043:3: kw= '!='
            	    {
            	    kw=(Token)match(input,38,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getExclamationMarkEqualsSignKeyword_25());
            	      		
            	    }

            	    }
            	    break;
            	case 27 :
            	    // InternalQDL.g:1049:3: kw= '&&'
            	    {
            	    kw=(Token)match(input,39,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getAmpersandAmpersandKeyword_26());
            	      		
            	    }

            	    }
            	    break;
            	case 28 :
            	    // InternalQDL.g:1055:3: kw= '||'
            	    {
            	    kw=(Token)match(input,40,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getVerticalLineVerticalLineKeyword_27());
            	      		
            	    }

            	    }
            	    break;
            	case 29 :
            	    // InternalQDL.g:1061:3: kw= '=='
            	    {
            	    kw=(Token)match(input,41,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getEqualsSignEqualsSignKeyword_28());
            	      		
            	    }

            	    }
            	    break;
            	case 30 :
            	    // InternalQDL.g:1067:3: kw= '='
            	    {
            	    kw=(Token)match(input,42,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getEqualsSignKeyword_29());
            	      		
            	    }

            	    }
            	    break;
            	case 31 :
            	    // InternalQDL.g:1073:3: kw= '~'
            	    {
            	    kw=(Token)match(input,43,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getTildeKeyword_30());
            	      		
            	    }

            	    }
            	    break;
            	case 32 :
            	    // InternalQDL.g:1079:3: kw= '?:'
            	    {
            	    kw=(Token)match(input,44,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getQuestionMarkColonKeyword_31());
            	      		
            	    }

            	    }
            	    break;
            	case 33 :
            	    // InternalQDL.g:1085:3: kw= '|'
            	    {
            	    kw=(Token)match(input,45,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getVerticalLineKeyword_32());
            	      		
            	    }

            	    }
            	    break;
            	case 34 :
            	    // InternalQDL.g:1091:3: kw= '|='
            	    {
            	    kw=(Token)match(input,46,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getVerticalLineEqualsSignKeyword_33());
            	      		
            	    }

            	    }
            	    break;
            	case 35 :
            	    // InternalQDL.g:1097:3: kw= '^'
            	    {
            	    kw=(Token)match(input,47,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getCircumflexAccentKeyword_34());
            	      		
            	    }

            	    }
            	    break;
            	case 36 :
            	    // InternalQDL.g:1103:3: kw= '^='
            	    {
            	    kw=(Token)match(input,48,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getCircumflexAccentEqualsSignKeyword_35());
            	      		
            	    }

            	    }
            	    break;
            	case 37 :
            	    // InternalQDL.g:1109:3: kw= '&'
            	    {
            	    kw=(Token)match(input,49,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getAmpersandKeyword_36());
            	      		
            	    }

            	    }
            	    break;
            	case 38 :
            	    // InternalQDL.g:1115:3: kw= '&='
            	    {
            	    kw=(Token)match(input,50,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getAmpersandEqualsSignKeyword_37());
            	      		
            	    }

            	    }
            	    break;
            	case 39 :
            	    // InternalQDL.g:1121:3: kw= '>>'
            	    {
            	    kw=(Token)match(input,51,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignGreaterThanSignKeyword_38());
            	      		
            	    }

            	    }
            	    break;
            	case 40 :
            	    // InternalQDL.g:1127:3: kw= '>>='
            	    {
            	    kw=(Token)match(input,52,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignGreaterThanSignEqualsSignKeyword_39());
            	      		
            	    }

            	    }
            	    break;
            	case 41 :
            	    // InternalQDL.g:1133:3: kw= '<<'
            	    {
            	    kw=(Token)match(input,53,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLessThanSignLessThanSignKeyword_40());
            	      		
            	    }

            	    }
            	    break;
            	case 42 :
            	    // InternalQDL.g:1139:3: kw= '<<='
            	    {
            	    kw=(Token)match(input,54,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLessThanSignLessThanSignEqualsSignKeyword_41());
            	      		
            	    }

            	    }
            	    break;
            	case 43 :
            	    // InternalQDL.g:1145:3: kw= '>>>'
            	    {
            	    kw=(Token)match(input,55,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignGreaterThanSignGreaterThanSignKeyword_42());
            	      		
            	    }

            	    }
            	    break;
            	case 44 :
            	    // InternalQDL.g:1151:3: kw= '>>>='
            	    {
            	    kw=(Token)match(input,56,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignGreaterThanSignGreaterThanSignEqualsSignKeyword_43());
            	      		
            	    }

            	    }
            	    break;
            	case 45 :
            	    // InternalQDL.g:1157:3: kw= '['
            	    {
            	    kw=(Token)match(input,57,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLeftSquareBracketKeyword_44());
            	      		
            	    }

            	    }
            	    break;
            	case 46 :
            	    // InternalQDL.g:1163:3: kw= ']'
            	    {
            	    kw=(Token)match(input,58,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getRightSquareBracketKeyword_45());
            	      		
            	    }

            	    }
            	    break;
            	case 47 :
            	    // InternalQDL.g:1169:3: kw= '{'
            	    {
            	    kw=(Token)match(input,59,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLeftCurlyBracketKeyword_46());
            	      		
            	    }

            	    }
            	    break;
            	case 48 :
            	    // InternalQDL.g:1175:3: kw= '}'
            	    {
            	    kw=(Token)match(input,60,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getRightCurlyBracketKeyword_47());
            	      		
            	    }

            	    }
            	    break;
            	case 49 :
            	    // InternalQDL.g:1181:3: kw= '('
            	    {
            	    kw=(Token)match(input,14,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLeftParenthesisKeyword_48());
            	      		
            	    }

            	    }
            	    break;
            	case 50 :
            	    // InternalQDL.g:1187:3: kw= ')'
            	    {
            	    kw=(Token)match(input,15,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getRightParenthesisKeyword_49());
            	      		
            	    }

            	    }
            	    break;
            	case 51 :
            	    // InternalQDL.g:1193:3: kw= '.'
            	    {
            	    kw=(Token)match(input,61,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getFullStopKeyword_50());
            	      		
            	    }

            	    }
            	    break;
            	case 52 :
            	    // InternalQDL.g:1199:3: kw= ':'
            	    {
            	    kw=(Token)match(input,17,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getColonKeyword_51());
            	      		
            	    }

            	    }
            	    break;
            	case 53 :
            	    // InternalQDL.g:1205:3: kw= ';'
            	    {
            	    kw=(Token)match(input,62,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getSemicolonKeyword_52());
            	      		
            	    }

            	    }
            	    break;
            	case 54 :
            	    // InternalQDL.g:1211:3: kw= ','
            	    {
            	    kw=(Token)match(input,63,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getCommaKeyword_53());
            	      		
            	    }

            	    }
            	    break;
            	case 55 :
            	    // InternalQDL.g:1217:3: kw= 'null'
            	    {
            	    kw=(Token)match(input,20,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getNullKeyword_54());
            	      		
            	    }

            	    }
            	    break;
            	case 56 :
            	    // InternalQDL.g:1223:3: kw= 'query'
            	    {
            	    kw=(Token)match(input,13,FOLLOW_14); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getQueryKeyword_55());
            	      		
            	    }

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLJavaText"


    // $ANTLR start "entryRuleIQLModelElement"
    // InternalQDL.g:1232:1: entryRuleIQLModelElement returns [EObject current=null] : iv_ruleIQLModelElement= ruleIQLModelElement EOF ;
    public final EObject entryRuleIQLModelElement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLModelElement = null;


        try {
            // InternalQDL.g:1232:56: (iv_ruleIQLModelElement= ruleIQLModelElement EOF )
            // InternalQDL.g:1233:2: iv_ruleIQLModelElement= ruleIQLModelElement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLModelElementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLModelElement=ruleIQLModelElement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLModelElement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLModelElement"


    // $ANTLR start "ruleIQLModelElement"
    // InternalQDL.g:1239:1: ruleIQLModelElement returns [EObject current=null] : ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) ) ) ;
    public final EObject ruleIQLModelElement() throws RecognitionException {
        EObject current = null;

        EObject lv_javametadata_0_0 = null;

        EObject lv_inner_1_1 = null;

        EObject lv_inner_1_2 = null;



        	enterRule();

        try {
            // InternalQDL.g:1245:2: ( ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) ) ) )
            // InternalQDL.g:1246:2: ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) ) )
            {
            // InternalQDL.g:1246:2: ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) ) )
            // InternalQDL.g:1247:3: ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) )
            {
            // InternalQDL.g:1247:3: ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==88) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // InternalQDL.g:1248:4: (lv_javametadata_0_0= ruleIQLJavaMetadata )
            	    {
            	    // InternalQDL.g:1248:4: (lv_javametadata_0_0= ruleIQLJavaMetadata )
            	    // InternalQDL.g:1249:5: lv_javametadata_0_0= ruleIQLJavaMetadata
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLModelElementAccess().getJavametadataIQLJavaMetadataParserRuleCall_0_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_15);
            	    lv_javametadata_0_0=ruleIQLJavaMetadata();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getIQLModelElementRule());
            	      					}
            	      					add(
            	      						current,
            	      						"javametadata",
            	      						lv_javametadata_0_0,
            	      						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLJavaMetadata");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);

            // InternalQDL.g:1266:3: ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) )
            // InternalQDL.g:1267:4: ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) )
            {
            // InternalQDL.g:1267:4: ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) )
            // InternalQDL.g:1268:5: (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface )
            {
            // InternalQDL.g:1268:5: (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface )
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==66) ) {
                alt14=1;
            }
            else if ( (LA14_0==69) ) {
                alt14=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // InternalQDL.g:1269:6: lv_inner_1_1= ruleIQLClass
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLModelElementAccess().getInnerIQLClassParserRuleCall_1_0_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_inner_1_1=ruleIQLClass();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLModelElementRule());
                      						}
                      						set(
                      							current,
                      							"inner",
                      							lv_inner_1_1,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLClass");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }
                    break;
                case 2 :
                    // InternalQDL.g:1285:6: lv_inner_1_2= ruleIQLInterface
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLModelElementAccess().getInnerIQLInterfaceParserRuleCall_1_0_1());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_inner_1_2=ruleIQLInterface();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLModelElementRule());
                      						}
                      						set(
                      							current,
                      							"inner",
                      							lv_inner_1_2,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLInterface");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }
                    break;

            }


            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLModelElement"


    // $ANTLR start "entryRuleIQLNamespace"
    // InternalQDL.g:1307:1: entryRuleIQLNamespace returns [EObject current=null] : iv_ruleIQLNamespace= ruleIQLNamespace EOF ;
    public final EObject entryRuleIQLNamespace() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLNamespace = null;


        try {
            // InternalQDL.g:1307:53: (iv_ruleIQLNamespace= ruleIQLNamespace EOF )
            // InternalQDL.g:1308:2: iv_ruleIQLNamespace= ruleIQLNamespace EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLNamespaceRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLNamespace=ruleIQLNamespace();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLNamespace; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLNamespace"


    // $ANTLR start "ruleIQLNamespace"
    // InternalQDL.g:1314:1: ruleIQLNamespace returns [EObject current=null] : (otherlv_0= 'use' ( (lv_static_1_0= 'static' ) )? ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) ) otherlv_3= ';' ) ;
    public final EObject ruleIQLNamespace() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_static_1_0=null;
        Token otherlv_3=null;
        AntlrDatatypeRuleToken lv_importedNamespace_2_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:1320:2: ( (otherlv_0= 'use' ( (lv_static_1_0= 'static' ) )? ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) ) otherlv_3= ';' ) )
            // InternalQDL.g:1321:2: (otherlv_0= 'use' ( (lv_static_1_0= 'static' ) )? ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) ) otherlv_3= ';' )
            {
            // InternalQDL.g:1321:2: (otherlv_0= 'use' ( (lv_static_1_0= 'static' ) )? ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) ) otherlv_3= ';' )
            // InternalQDL.g:1322:3: otherlv_0= 'use' ( (lv_static_1_0= 'static' ) )? ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) ) otherlv_3= ';'
            {
            otherlv_0=(Token)match(input,64,FOLLOW_16); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_0, grammarAccess.getIQLNamespaceAccess().getUseKeyword_0());
              		
            }
            // InternalQDL.g:1326:3: ( (lv_static_1_0= 'static' ) )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==65) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // InternalQDL.g:1327:4: (lv_static_1_0= 'static' )
                    {
                    // InternalQDL.g:1327:4: (lv_static_1_0= 'static' )
                    // InternalQDL.g:1328:5: lv_static_1_0= 'static'
                    {
                    lv_static_1_0=(Token)match(input,65,FOLLOW_16); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_static_1_0, grammarAccess.getIQLNamespaceAccess().getStaticStaticKeyword_1_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getIQLNamespaceRule());
                      					}
                      					setWithLastConsumed(current, "static", true, "static");
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalQDL.g:1340:3: ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) )
            // InternalQDL.g:1341:4: (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard )
            {
            // InternalQDL.g:1341:4: (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard )
            // InternalQDL.g:1342:5: lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLNamespaceAccess().getImportedNamespaceQualifiedNameWithWildcardParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_17);
            lv_importedNamespace_2_0=ruleQualifiedNameWithWildcard();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLNamespaceRule());
              					}
              					set(
              						current,
              						"importedNamespace",
              						lv_importedNamespace_2_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.QualifiedNameWithWildcard");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_3=(Token)match(input,62,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getIQLNamespaceAccess().getSemicolonKeyword_3());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLNamespace"


    // $ANTLR start "entryRuleIQLClass"
    // InternalQDL.g:1367:1: entryRuleIQLClass returns [EObject current=null] : iv_ruleIQLClass= ruleIQLClass EOF ;
    public final EObject entryRuleIQLClass() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLClass = null;


        try {
            // InternalQDL.g:1367:49: (iv_ruleIQLClass= ruleIQLClass EOF )
            // InternalQDL.g:1368:2: iv_ruleIQLClass= ruleIQLClass EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLClassRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLClass=ruleIQLClass();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLClass; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLClass"


    // $ANTLR start "ruleIQLClass"
    // InternalQDL.g:1374:1: ruleIQLClass returns [EObject current=null] : ( () otherlv_1= 'class' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )? (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )? otherlv_9= '{' ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )* otherlv_11= '}' ) ;
    public final EObject ruleIQLClass() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_simpleName_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        Token otherlv_11=null;
        EObject lv_extendedClass_4_0 = null;

        EObject lv_extendedInterfaces_6_0 = null;

        EObject lv_extendedInterfaces_8_0 = null;

        EObject lv_members_10_1 = null;

        EObject lv_members_10_2 = null;

        EObject lv_members_10_3 = null;



        	enterRule();

        try {
            // InternalQDL.g:1380:2: ( ( () otherlv_1= 'class' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )? (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )? otherlv_9= '{' ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )* otherlv_11= '}' ) )
            // InternalQDL.g:1381:2: ( () otherlv_1= 'class' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )? (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )? otherlv_9= '{' ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )* otherlv_11= '}' )
            {
            // InternalQDL.g:1381:2: ( () otherlv_1= 'class' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )? (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )? otherlv_9= '{' ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )* otherlv_11= '}' )
            // InternalQDL.g:1382:3: () otherlv_1= 'class' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )? (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )? otherlv_9= '{' ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )* otherlv_11= '}'
            {
            // InternalQDL.g:1382:3: ()
            // InternalQDL.g:1383:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLClassAccess().getIQLClassAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,66,FOLLOW_6); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLClassAccess().getClassKeyword_1());
              		
            }
            // InternalQDL.g:1393:3: ( (lv_simpleName_2_0= RULE_ID ) )
            // InternalQDL.g:1394:4: (lv_simpleName_2_0= RULE_ID )
            {
            // InternalQDL.g:1394:4: (lv_simpleName_2_0= RULE_ID )
            // InternalQDL.g:1395:5: lv_simpleName_2_0= RULE_ID
            {
            lv_simpleName_2_0=(Token)match(input,RULE_ID,FOLLOW_18); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_simpleName_2_0, grammarAccess.getIQLClassAccess().getSimpleNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLClassRule());
              					}
              					setWithLastConsumed(
              						current,
              						"simpleName",
              						lv_simpleName_2_0,
              						"org.eclipse.xtext.common.Terminals.ID");
              				
            }

            }


            }

            // InternalQDL.g:1411:3: (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==67) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // InternalQDL.g:1412:4: otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) )
                    {
                    otherlv_3=(Token)match(input,67,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getIQLClassAccess().getExtendsKeyword_3_0());
                      			
                    }
                    // InternalQDL.g:1416:4: ( (lv_extendedClass_4_0= ruleJvmTypeReference ) )
                    // InternalQDL.g:1417:5: (lv_extendedClass_4_0= ruleJvmTypeReference )
                    {
                    // InternalQDL.g:1417:5: (lv_extendedClass_4_0= ruleJvmTypeReference )
                    // InternalQDL.g:1418:6: lv_extendedClass_4_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLClassAccess().getExtendedClassJvmTypeReferenceParserRuleCall_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_19);
                    lv_extendedClass_4_0=ruleJvmTypeReference();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLClassRule());
                      						}
                      						set(
                      							current,
                      							"extendedClass",
                      							lv_extendedClass_4_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalQDL.g:1436:3: (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==68) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // InternalQDL.g:1437:4: otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )*
                    {
                    otherlv_5=(Token)match(input,68,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getIQLClassAccess().getImplementsKeyword_4_0());
                      			
                    }
                    // InternalQDL.g:1441:4: ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) )
                    // InternalQDL.g:1442:5: (lv_extendedInterfaces_6_0= ruleJvmTypeReference )
                    {
                    // InternalQDL.g:1442:5: (lv_extendedInterfaces_6_0= ruleJvmTypeReference )
                    // InternalQDL.g:1443:6: lv_extendedInterfaces_6_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLClassAccess().getExtendedInterfacesJvmTypeReferenceParserRuleCall_4_1_0());
                      					
                    }
                    pushFollow(FOLLOW_20);
                    lv_extendedInterfaces_6_0=ruleJvmTypeReference();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLClassRule());
                      						}
                      						add(
                      							current,
                      							"extendedInterfaces",
                      							lv_extendedInterfaces_6_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalQDL.g:1460:4: (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )*
                    loop17:
                    do {
                        int alt17=2;
                        int LA17_0 = input.LA(1);

                        if ( (LA17_0==63) ) {
                            alt17=1;
                        }


                        switch (alt17) {
                    	case 1 :
                    	    // InternalQDL.g:1461:5: otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) )
                    	    {
                    	    otherlv_7=(Token)match(input,63,FOLLOW_6); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_7, grammarAccess.getIQLClassAccess().getCommaKeyword_4_2_0());
                    	      				
                    	    }
                    	    // InternalQDL.g:1465:5: ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) )
                    	    // InternalQDL.g:1466:6: (lv_extendedInterfaces_8_0= ruleJvmTypeReference )
                    	    {
                    	    // InternalQDL.g:1466:6: (lv_extendedInterfaces_8_0= ruleJvmTypeReference )
                    	    // InternalQDL.g:1467:7: lv_extendedInterfaces_8_0= ruleJvmTypeReference
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLClassAccess().getExtendedInterfacesJvmTypeReferenceParserRuleCall_4_2_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_20);
                    	    lv_extendedInterfaces_8_0=ruleJvmTypeReference();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElementForParent(grammarAccess.getIQLClassRule());
                    	      							}
                    	      							add(
                    	      								current,
                    	      								"extendedInterfaces",
                    	      								lv_extendedInterfaces_8_0,
                    	      								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference");
                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop17;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_9=(Token)match(input,59,FOLLOW_21); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_9, grammarAccess.getIQLClassAccess().getLeftCurlyBracketKeyword_5());
              		
            }
            // InternalQDL.g:1490:3: ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )*
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( (LA20_0==RULE_ID||LA20_0==70||LA20_0==88) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // InternalQDL.g:1491:4: ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) )
            	    {
            	    // InternalQDL.g:1491:4: ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) )
            	    // InternalQDL.g:1492:5: (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember )
            	    {
            	    // InternalQDL.g:1492:5: (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember )
            	    int alt19=3;
            	    switch ( input.LA(1) ) {
            	    case RULE_ID:
            	        {
            	        int LA19_1 = input.LA(2);

            	        if ( (LA19_1==RULE_ID||LA19_1==57||LA19_1==87) ) {
            	            alt19=1;
            	        }
            	        else if ( (LA19_1==14||LA19_1==17||LA19_1==59) ) {
            	            alt19=2;
            	        }
            	        else {
            	            if (state.backtracking>0) {state.failed=true; return current;}
            	            NoViableAltException nvae =
            	                new NoViableAltException("", 19, 1, input);

            	            throw nvae;
            	        }
            	        }
            	        break;
            	    case 70:
            	        {
            	        alt19=2;
            	        }
            	        break;
            	    case 88:
            	        {
            	        alt19=3;
            	        }
            	        break;
            	    default:
            	        if (state.backtracking>0) {state.failed=true; return current;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 19, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt19) {
            	        case 1 :
            	            // InternalQDL.g:1493:6: lv_members_10_1= ruleIQLAttribute
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getIQLClassAccess().getMembersIQLAttributeParserRuleCall_6_0_0());
            	              					
            	            }
            	            pushFollow(FOLLOW_21);
            	            lv_members_10_1=ruleIQLAttribute();

            	            state._fsp--;
            	            if (state.failed) return current;
            	            if ( state.backtracking==0 ) {

            	              						if (current==null) {
            	              							current = createModelElementForParent(grammarAccess.getIQLClassRule());
            	              						}
            	              						add(
            	              							current,
            	              							"members",
            	              							lv_members_10_1,
            	              							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLAttribute");
            	              						afterParserOrEnumRuleCall();
            	              					
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // InternalQDL.g:1509:6: lv_members_10_2= ruleIQLMethod
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getIQLClassAccess().getMembersIQLMethodParserRuleCall_6_0_1());
            	              					
            	            }
            	            pushFollow(FOLLOW_21);
            	            lv_members_10_2=ruleIQLMethod();

            	            state._fsp--;
            	            if (state.failed) return current;
            	            if ( state.backtracking==0 ) {

            	              						if (current==null) {
            	              							current = createModelElementForParent(grammarAccess.getIQLClassRule());
            	              						}
            	              						add(
            	              							current,
            	              							"members",
            	              							lv_members_10_2,
            	              							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMethod");
            	              						afterParserOrEnumRuleCall();
            	              					
            	            }

            	            }
            	            break;
            	        case 3 :
            	            // InternalQDL.g:1525:6: lv_members_10_3= ruleIQLJavaMember
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getIQLClassAccess().getMembersIQLJavaMemberParserRuleCall_6_0_2());
            	              					
            	            }
            	            pushFollow(FOLLOW_21);
            	            lv_members_10_3=ruleIQLJavaMember();

            	            state._fsp--;
            	            if (state.failed) return current;
            	            if ( state.backtracking==0 ) {

            	              						if (current==null) {
            	              							current = createModelElementForParent(grammarAccess.getIQLClassRule());
            	              						}
            	              						add(
            	              							current,
            	              							"members",
            	              							lv_members_10_3,
            	              							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLJavaMember");
            	              						afterParserOrEnumRuleCall();
            	              					
            	            }

            	            }
            	            break;

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);

            otherlv_11=(Token)match(input,60,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_11, grammarAccess.getIQLClassAccess().getRightCurlyBracketKeyword_7());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLClass"


    // $ANTLR start "entryRuleIQLInterface"
    // InternalQDL.g:1551:1: entryRuleIQLInterface returns [EObject current=null] : iv_ruleIQLInterface= ruleIQLInterface EOF ;
    public final EObject entryRuleIQLInterface() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLInterface = null;


        try {
            // InternalQDL.g:1551:53: (iv_ruleIQLInterface= ruleIQLInterface EOF )
            // InternalQDL.g:1552:2: iv_ruleIQLInterface= ruleIQLInterface EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLInterfaceRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLInterface=ruleIQLInterface();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLInterface; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLInterface"


    // $ANTLR start "ruleIQLInterface"
    // InternalQDL.g:1558:1: ruleIQLInterface returns [EObject current=null] : ( () otherlv_1= 'interface' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )? otherlv_7= '{' ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )* otherlv_9= '}' ) ;
    public final EObject ruleIQLInterface() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_simpleName_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        EObject lv_extendedInterfaces_4_0 = null;

        EObject lv_extendedInterfaces_6_0 = null;

        EObject lv_members_8_1 = null;

        EObject lv_members_8_2 = null;



        	enterRule();

        try {
            // InternalQDL.g:1564:2: ( ( () otherlv_1= 'interface' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )? otherlv_7= '{' ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )* otherlv_9= '}' ) )
            // InternalQDL.g:1565:2: ( () otherlv_1= 'interface' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )? otherlv_7= '{' ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )* otherlv_9= '}' )
            {
            // InternalQDL.g:1565:2: ( () otherlv_1= 'interface' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )? otherlv_7= '{' ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )* otherlv_9= '}' )
            // InternalQDL.g:1566:3: () otherlv_1= 'interface' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )? otherlv_7= '{' ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )* otherlv_9= '}'
            {
            // InternalQDL.g:1566:3: ()
            // InternalQDL.g:1567:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLInterfaceAccess().getIQLInterfaceAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,69,FOLLOW_6); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLInterfaceAccess().getInterfaceKeyword_1());
              		
            }
            // InternalQDL.g:1577:3: ( (lv_simpleName_2_0= RULE_ID ) )
            // InternalQDL.g:1578:4: (lv_simpleName_2_0= RULE_ID )
            {
            // InternalQDL.g:1578:4: (lv_simpleName_2_0= RULE_ID )
            // InternalQDL.g:1579:5: lv_simpleName_2_0= RULE_ID
            {
            lv_simpleName_2_0=(Token)match(input,RULE_ID,FOLLOW_22); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_simpleName_2_0, grammarAccess.getIQLInterfaceAccess().getSimpleNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLInterfaceRule());
              					}
              					setWithLastConsumed(
              						current,
              						"simpleName",
              						lv_simpleName_2_0,
              						"org.eclipse.xtext.common.Terminals.ID");
              				
            }

            }


            }

            // InternalQDL.g:1595:3: (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==67) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // InternalQDL.g:1596:4: otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )?
                    {
                    otherlv_3=(Token)match(input,67,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getIQLInterfaceAccess().getExtendsKeyword_3_0());
                      			
                    }
                    // InternalQDL.g:1600:4: ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) )
                    // InternalQDL.g:1601:5: (lv_extendedInterfaces_4_0= ruleJvmTypeReference )
                    {
                    // InternalQDL.g:1601:5: (lv_extendedInterfaces_4_0= ruleJvmTypeReference )
                    // InternalQDL.g:1602:6: lv_extendedInterfaces_4_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLInterfaceAccess().getExtendedInterfacesJvmTypeReferenceParserRuleCall_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_20);
                    lv_extendedInterfaces_4_0=ruleJvmTypeReference();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLInterfaceRule());
                      						}
                      						add(
                      							current,
                      							"extendedInterfaces",
                      							lv_extendedInterfaces_4_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalQDL.g:1619:4: (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )?
                    int alt21=2;
                    int LA21_0 = input.LA(1);

                    if ( (LA21_0==63) ) {
                        alt21=1;
                    }
                    switch (alt21) {
                        case 1 :
                            // InternalQDL.g:1620:5: otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) )
                            {
                            otherlv_5=(Token)match(input,63,FOLLOW_6); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              					newLeafNode(otherlv_5, grammarAccess.getIQLInterfaceAccess().getCommaKeyword_3_2_0());
                              				
                            }
                            // InternalQDL.g:1624:5: ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) )
                            // InternalQDL.g:1625:6: (lv_extendedInterfaces_6_0= ruleJvmTypeReference )
                            {
                            // InternalQDL.g:1625:6: (lv_extendedInterfaces_6_0= ruleJvmTypeReference )
                            // InternalQDL.g:1626:7: lv_extendedInterfaces_6_0= ruleJvmTypeReference
                            {
                            if ( state.backtracking==0 ) {

                              							newCompositeNode(grammarAccess.getIQLInterfaceAccess().getExtendedInterfacesJvmTypeReferenceParserRuleCall_3_2_1_0());
                              						
                            }
                            pushFollow(FOLLOW_23);
                            lv_extendedInterfaces_6_0=ruleJvmTypeReference();

                            state._fsp--;
                            if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              							if (current==null) {
                              								current = createModelElementForParent(grammarAccess.getIQLInterfaceRule());
                              							}
                              							add(
                              								current,
                              								"extendedInterfaces",
                              								lv_extendedInterfaces_6_0,
                              								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference");
                              							afterParserOrEnumRuleCall();
                              						
                            }

                            }


                            }


                            }
                            break;

                    }


                    }
                    break;

            }

            otherlv_7=(Token)match(input,59,FOLLOW_21); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_7, grammarAccess.getIQLInterfaceAccess().getLeftCurlyBracketKeyword_4());
              		
            }
            // InternalQDL.g:1649:3: ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==RULE_ID||LA24_0==88) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // InternalQDL.g:1650:4: ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) )
            	    {
            	    // InternalQDL.g:1650:4: ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) )
            	    // InternalQDL.g:1651:5: (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember )
            	    {
            	    // InternalQDL.g:1651:5: (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember )
            	    int alt23=2;
            	    int LA23_0 = input.LA(1);

            	    if ( (LA23_0==RULE_ID) ) {
            	        alt23=1;
            	    }
            	    else if ( (LA23_0==88) ) {
            	        alt23=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return current;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 23, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt23) {
            	        case 1 :
            	            // InternalQDL.g:1652:6: lv_members_8_1= ruleIQLMethodDeclaration
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getIQLInterfaceAccess().getMembersIQLMethodDeclarationParserRuleCall_5_0_0());
            	              					
            	            }
            	            pushFollow(FOLLOW_21);
            	            lv_members_8_1=ruleIQLMethodDeclaration();

            	            state._fsp--;
            	            if (state.failed) return current;
            	            if ( state.backtracking==0 ) {

            	              						if (current==null) {
            	              							current = createModelElementForParent(grammarAccess.getIQLInterfaceRule());
            	              						}
            	              						add(
            	              							current,
            	              							"members",
            	              							lv_members_8_1,
            	              							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMethodDeclaration");
            	              						afterParserOrEnumRuleCall();
            	              					
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // InternalQDL.g:1668:6: lv_members_8_2= ruleIQLJavaMember
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getIQLInterfaceAccess().getMembersIQLJavaMemberParserRuleCall_5_0_1());
            	              					
            	            }
            	            pushFollow(FOLLOW_21);
            	            lv_members_8_2=ruleIQLJavaMember();

            	            state._fsp--;
            	            if (state.failed) return current;
            	            if ( state.backtracking==0 ) {

            	              						if (current==null) {
            	              							current = createModelElementForParent(grammarAccess.getIQLInterfaceRule());
            	              						}
            	              						add(
            	              							current,
            	              							"members",
            	              							lv_members_8_2,
            	              							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLJavaMember");
            	              						afterParserOrEnumRuleCall();
            	              					
            	            }

            	            }
            	            break;

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop24;
                }
            } while (true);

            otherlv_9=(Token)match(input,60,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_9, grammarAccess.getIQLInterfaceAccess().getRightCurlyBracketKeyword_6());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLInterface"


    // $ANTLR start "entryRuleIQLJavaMetadata"
    // InternalQDL.g:1694:1: entryRuleIQLJavaMetadata returns [EObject current=null] : iv_ruleIQLJavaMetadata= ruleIQLJavaMetadata EOF ;
    public final EObject entryRuleIQLJavaMetadata() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLJavaMetadata = null;


        try {
            // InternalQDL.g:1694:56: (iv_ruleIQLJavaMetadata= ruleIQLJavaMetadata EOF )
            // InternalQDL.g:1695:2: iv_ruleIQLJavaMetadata= ruleIQLJavaMetadata EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLJavaMetadataRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLJavaMetadata=ruleIQLJavaMetadata();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLJavaMetadata; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLJavaMetadata"


    // $ANTLR start "ruleIQLJavaMetadata"
    // InternalQDL.g:1701:1: ruleIQLJavaMetadata returns [EObject current=null] : ( (lv_java_0_0= ruleIQLJava ) ) ;
    public final EObject ruleIQLJavaMetadata() throws RecognitionException {
        EObject current = null;

        EObject lv_java_0_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:1707:2: ( ( (lv_java_0_0= ruleIQLJava ) ) )
            // InternalQDL.g:1708:2: ( (lv_java_0_0= ruleIQLJava ) )
            {
            // InternalQDL.g:1708:2: ( (lv_java_0_0= ruleIQLJava ) )
            // InternalQDL.g:1709:3: (lv_java_0_0= ruleIQLJava )
            {
            // InternalQDL.g:1709:3: (lv_java_0_0= ruleIQLJava )
            // InternalQDL.g:1710:4: lv_java_0_0= ruleIQLJava
            {
            if ( state.backtracking==0 ) {

              				newCompositeNode(grammarAccess.getIQLJavaMetadataAccess().getJavaIQLJavaParserRuleCall_0());
              			
            }
            pushFollow(FOLLOW_2);
            lv_java_0_0=ruleIQLJava();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              				if (current==null) {
              					current = createModelElementForParent(grammarAccess.getIQLJavaMetadataRule());
              				}
              				set(
              					current,
              					"java",
              					lv_java_0_0,
              					"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLJava");
              				afterParserOrEnumRuleCall();
              			
            }

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLJavaMetadata"


    // $ANTLR start "entryRuleIQLAttribute"
    // InternalQDL.g:1730:1: entryRuleIQLAttribute returns [EObject current=null] : iv_ruleIQLAttribute= ruleIQLAttribute EOF ;
    public final EObject entryRuleIQLAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLAttribute = null;


        try {
            // InternalQDL.g:1730:53: (iv_ruleIQLAttribute= ruleIQLAttribute EOF )
            // InternalQDL.g:1731:2: iv_ruleIQLAttribute= ruleIQLAttribute EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLAttributeRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLAttribute=ruleIQLAttribute();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLAttribute; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLAttribute"


    // $ANTLR start "ruleIQLAttribute"
    // InternalQDL.g:1737:1: ruleIQLAttribute returns [EObject current=null] : ( () ( (lv_type_1_0= ruleJvmTypeReference ) ) ( (lv_simpleName_2_0= RULE_ID ) ) ( (lv_init_3_0= ruleIQLVariableInitialization ) )? otherlv_4= ';' ) ;
    public final EObject ruleIQLAttribute() throws RecognitionException {
        EObject current = null;

        Token lv_simpleName_2_0=null;
        Token otherlv_4=null;
        EObject lv_type_1_0 = null;

        EObject lv_init_3_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:1743:2: ( ( () ( (lv_type_1_0= ruleJvmTypeReference ) ) ( (lv_simpleName_2_0= RULE_ID ) ) ( (lv_init_3_0= ruleIQLVariableInitialization ) )? otherlv_4= ';' ) )
            // InternalQDL.g:1744:2: ( () ( (lv_type_1_0= ruleJvmTypeReference ) ) ( (lv_simpleName_2_0= RULE_ID ) ) ( (lv_init_3_0= ruleIQLVariableInitialization ) )? otherlv_4= ';' )
            {
            // InternalQDL.g:1744:2: ( () ( (lv_type_1_0= ruleJvmTypeReference ) ) ( (lv_simpleName_2_0= RULE_ID ) ) ( (lv_init_3_0= ruleIQLVariableInitialization ) )? otherlv_4= ';' )
            // InternalQDL.g:1745:3: () ( (lv_type_1_0= ruleJvmTypeReference ) ) ( (lv_simpleName_2_0= RULE_ID ) ) ( (lv_init_3_0= ruleIQLVariableInitialization ) )? otherlv_4= ';'
            {
            // InternalQDL.g:1745:3: ()
            // InternalQDL.g:1746:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLAttributeAccess().getIQLAttributeAction_0(),
              					current);
              			
            }

            }

            // InternalQDL.g:1752:3: ( (lv_type_1_0= ruleJvmTypeReference ) )
            // InternalQDL.g:1753:4: (lv_type_1_0= ruleJvmTypeReference )
            {
            // InternalQDL.g:1753:4: (lv_type_1_0= ruleJvmTypeReference )
            // InternalQDL.g:1754:5: lv_type_1_0= ruleJvmTypeReference
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLAttributeAccess().getTypeJvmTypeReferenceParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_6);
            lv_type_1_0=ruleJvmTypeReference();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLAttributeRule());
              					}
              					set(
              						current,
              						"type",
              						lv_type_1_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalQDL.g:1771:3: ( (lv_simpleName_2_0= RULE_ID ) )
            // InternalQDL.g:1772:4: (lv_simpleName_2_0= RULE_ID )
            {
            // InternalQDL.g:1772:4: (lv_simpleName_2_0= RULE_ID )
            // InternalQDL.g:1773:5: lv_simpleName_2_0= RULE_ID
            {
            lv_simpleName_2_0=(Token)match(input,RULE_ID,FOLLOW_24); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_simpleName_2_0, grammarAccess.getIQLAttributeAccess().getSimpleNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLAttributeRule());
              					}
              					setWithLastConsumed(
              						current,
              						"simpleName",
              						lv_simpleName_2_0,
              						"org.eclipse.xtext.common.Terminals.ID");
              				
            }

            }


            }

            // InternalQDL.g:1789:3: ( (lv_init_3_0= ruleIQLVariableInitialization ) )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==14||LA25_0==42||LA25_0==59) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // InternalQDL.g:1790:4: (lv_init_3_0= ruleIQLVariableInitialization )
                    {
                    // InternalQDL.g:1790:4: (lv_init_3_0= ruleIQLVariableInitialization )
                    // InternalQDL.g:1791:5: lv_init_3_0= ruleIQLVariableInitialization
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getIQLAttributeAccess().getInitIQLVariableInitializationParserRuleCall_3_0());
                      				
                    }
                    pushFollow(FOLLOW_17);
                    lv_init_3_0=ruleIQLVariableInitialization();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getIQLAttributeRule());
                      					}
                      					set(
                      						current,
                      						"init",
                      						lv_init_3_0,
                      						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLVariableInitialization");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_4=(Token)match(input,62,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLAttributeAccess().getSemicolonKeyword_4());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLAttribute"


    // $ANTLR start "entryRuleJvmTypeReference"
    // InternalQDL.g:1816:1: entryRuleJvmTypeReference returns [EObject current=null] : iv_ruleJvmTypeReference= ruleJvmTypeReference EOF ;
    public final EObject entryRuleJvmTypeReference() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJvmTypeReference = null;


        try {
            // InternalQDL.g:1816:57: (iv_ruleJvmTypeReference= ruleJvmTypeReference EOF )
            // InternalQDL.g:1817:2: iv_ruleJvmTypeReference= ruleJvmTypeReference EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getJvmTypeReferenceRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleJvmTypeReference=ruleJvmTypeReference();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleJvmTypeReference; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleJvmTypeReference"


    // $ANTLR start "ruleJvmTypeReference"
    // InternalQDL.g:1823:1: ruleJvmTypeReference returns [EObject current=null] : (this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef | this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef ) ;
    public final EObject ruleJvmTypeReference() throws RecognitionException {
        EObject current = null;

        EObject this_IQLSimpleTypeRef_0 = null;

        EObject this_IQLArrayTypeRef_1 = null;



        	enterRule();

        try {
            // InternalQDL.g:1829:2: ( (this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef | this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef ) )
            // InternalQDL.g:1830:2: (this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef | this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef )
            {
            // InternalQDL.g:1830:2: (this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef | this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef )
            int alt26=2;
            alt26 = dfa26.predict(input);
            switch (alt26) {
                case 1 :
                    // InternalQDL.g:1831:3: this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getJvmTypeReferenceAccess().getIQLSimpleTypeRefParserRuleCall_0());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLSimpleTypeRef_0=ruleIQLSimpleTypeRef();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLSimpleTypeRef_0;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalQDL.g:1840:3: this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getJvmTypeReferenceAccess().getIQLArrayTypeRefParserRuleCall_1());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLArrayTypeRef_1=ruleIQLArrayTypeRef();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLArrayTypeRef_1;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleJvmTypeReference"


    // $ANTLR start "entryRuleIQLSimpleTypeRef"
    // InternalQDL.g:1852:1: entryRuleIQLSimpleTypeRef returns [EObject current=null] : iv_ruleIQLSimpleTypeRef= ruleIQLSimpleTypeRef EOF ;
    public final EObject entryRuleIQLSimpleTypeRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLSimpleTypeRef = null;


        try {
            // InternalQDL.g:1852:57: (iv_ruleIQLSimpleTypeRef= ruleIQLSimpleTypeRef EOF )
            // InternalQDL.g:1853:2: iv_ruleIQLSimpleTypeRef= ruleIQLSimpleTypeRef EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLSimpleTypeRefRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLSimpleTypeRef=ruleIQLSimpleTypeRef();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLSimpleTypeRef; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLSimpleTypeRef"


    // $ANTLR start "ruleIQLSimpleTypeRef"
    // InternalQDL.g:1859:1: ruleIQLSimpleTypeRef returns [EObject current=null] : ( () ( ( ruleQualifiedName ) ) ) ;
    public final EObject ruleIQLSimpleTypeRef() throws RecognitionException {
        EObject current = null;


        	enterRule();

        try {
            // InternalQDL.g:1865:2: ( ( () ( ( ruleQualifiedName ) ) ) )
            // InternalQDL.g:1866:2: ( () ( ( ruleQualifiedName ) ) )
            {
            // InternalQDL.g:1866:2: ( () ( ( ruleQualifiedName ) ) )
            // InternalQDL.g:1867:3: () ( ( ruleQualifiedName ) )
            {
            // InternalQDL.g:1867:3: ()
            // InternalQDL.g:1868:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLSimpleTypeRefAccess().getIQLSimpleTypeRefAction_0(),
              					current);
              			
            }

            }

            // InternalQDL.g:1874:3: ( ( ruleQualifiedName ) )
            // InternalQDL.g:1875:4: ( ruleQualifiedName )
            {
            // InternalQDL.g:1875:4: ( ruleQualifiedName )
            // InternalQDL.g:1876:5: ruleQualifiedName
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLSimpleTypeRefRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLSimpleTypeRefAccess().getTypeJvmTypeCrossReference_1_0());
              				
            }
            pushFollow(FOLLOW_2);
            ruleQualifiedName();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLSimpleTypeRef"


    // $ANTLR start "entryRuleIQLArrayTypeRef"
    // InternalQDL.g:1894:1: entryRuleIQLArrayTypeRef returns [EObject current=null] : iv_ruleIQLArrayTypeRef= ruleIQLArrayTypeRef EOF ;
    public final EObject entryRuleIQLArrayTypeRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLArrayTypeRef = null;


        try {
            // InternalQDL.g:1894:56: (iv_ruleIQLArrayTypeRef= ruleIQLArrayTypeRef EOF )
            // InternalQDL.g:1895:2: iv_ruleIQLArrayTypeRef= ruleIQLArrayTypeRef EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLArrayTypeRefRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLArrayTypeRef=ruleIQLArrayTypeRef();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLArrayTypeRef; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLArrayTypeRef"


    // $ANTLR start "ruleIQLArrayTypeRef"
    // InternalQDL.g:1901:1: ruleIQLArrayTypeRef returns [EObject current=null] : ( () ( (lv_type_1_0= ruleIQLArrayType ) ) ) ;
    public final EObject ruleIQLArrayTypeRef() throws RecognitionException {
        EObject current = null;

        EObject lv_type_1_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:1907:2: ( ( () ( (lv_type_1_0= ruleIQLArrayType ) ) ) )
            // InternalQDL.g:1908:2: ( () ( (lv_type_1_0= ruleIQLArrayType ) ) )
            {
            // InternalQDL.g:1908:2: ( () ( (lv_type_1_0= ruleIQLArrayType ) ) )
            // InternalQDL.g:1909:3: () ( (lv_type_1_0= ruleIQLArrayType ) )
            {
            // InternalQDL.g:1909:3: ()
            // InternalQDL.g:1910:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLArrayTypeRefAccess().getIQLArrayTypeRefAction_0(),
              					current);
              			
            }

            }

            // InternalQDL.g:1916:3: ( (lv_type_1_0= ruleIQLArrayType ) )
            // InternalQDL.g:1917:4: (lv_type_1_0= ruleIQLArrayType )
            {
            // InternalQDL.g:1917:4: (lv_type_1_0= ruleIQLArrayType )
            // InternalQDL.g:1918:5: lv_type_1_0= ruleIQLArrayType
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLArrayTypeRefAccess().getTypeIQLArrayTypeParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_type_1_0=ruleIQLArrayType();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLArrayTypeRefRule());
              					}
              					set(
              						current,
              						"type",
              						lv_type_1_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLArrayType");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLArrayTypeRef"


    // $ANTLR start "entryRuleIQLArrayType"
    // InternalQDL.g:1939:1: entryRuleIQLArrayType returns [EObject current=null] : iv_ruleIQLArrayType= ruleIQLArrayType EOF ;
    public final EObject entryRuleIQLArrayType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLArrayType = null;


        try {
            // InternalQDL.g:1939:53: (iv_ruleIQLArrayType= ruleIQLArrayType EOF )
            // InternalQDL.g:1940:2: iv_ruleIQLArrayType= ruleIQLArrayType EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLArrayTypeRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLArrayType=ruleIQLArrayType();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLArrayType; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLArrayType"


    // $ANTLR start "ruleIQLArrayType"
    // InternalQDL.g:1946:1: ruleIQLArrayType returns [EObject current=null] : ( () ( ( ruleQualifiedName ) ) ( (lv_dimensions_2_0= ruleArrayBrackets ) )+ ) ;
    public final EObject ruleIQLArrayType() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_dimensions_2_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:1952:2: ( ( () ( ( ruleQualifiedName ) ) ( (lv_dimensions_2_0= ruleArrayBrackets ) )+ ) )
            // InternalQDL.g:1953:2: ( () ( ( ruleQualifiedName ) ) ( (lv_dimensions_2_0= ruleArrayBrackets ) )+ )
            {
            // InternalQDL.g:1953:2: ( () ( ( ruleQualifiedName ) ) ( (lv_dimensions_2_0= ruleArrayBrackets ) )+ )
            // InternalQDL.g:1954:3: () ( ( ruleQualifiedName ) ) ( (lv_dimensions_2_0= ruleArrayBrackets ) )+
            {
            // InternalQDL.g:1954:3: ()
            // InternalQDL.g:1955:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLArrayTypeAccess().getIQLArrayTypeAction_0(),
              					current);
              			
            }

            }

            // InternalQDL.g:1961:3: ( ( ruleQualifiedName ) )
            // InternalQDL.g:1962:4: ( ruleQualifiedName )
            {
            // InternalQDL.g:1962:4: ( ruleQualifiedName )
            // InternalQDL.g:1963:5: ruleQualifiedName
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLArrayTypeRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLArrayTypeAccess().getComponentTypeJvmTypeCrossReference_1_0());
              				
            }
            pushFollow(FOLLOW_25);
            ruleQualifiedName();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalQDL.g:1977:3: ( (lv_dimensions_2_0= ruleArrayBrackets ) )+
            int cnt27=0;
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0==57) ) {
                    int LA27_2 = input.LA(2);

                    if ( (LA27_2==58) ) {
                        alt27=1;
                    }


                }


                switch (alt27) {
            	case 1 :
            	    // InternalQDL.g:1978:4: (lv_dimensions_2_0= ruleArrayBrackets )
            	    {
            	    // InternalQDL.g:1978:4: (lv_dimensions_2_0= ruleArrayBrackets )
            	    // InternalQDL.g:1979:5: lv_dimensions_2_0= ruleArrayBrackets
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLArrayTypeAccess().getDimensionsArrayBracketsParserRuleCall_2_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_26);
            	    lv_dimensions_2_0=ruleArrayBrackets();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getIQLArrayTypeRule());
            	      					}
            	      					add(
            	      						current,
            	      						"dimensions",
            	      						lv_dimensions_2_0,
            	      						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.ArrayBrackets");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt27 >= 1 ) break loop27;
            	    if (state.backtracking>0) {state.failed=true; return current;}
                        EarlyExitException eee =
                            new EarlyExitException(27, input);
                        throw eee;
                }
                cnt27++;
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLArrayType"


    // $ANTLR start "entryRuleArrayBrackets"
    // InternalQDL.g:2000:1: entryRuleArrayBrackets returns [String current=null] : iv_ruleArrayBrackets= ruleArrayBrackets EOF ;
    public final String entryRuleArrayBrackets() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleArrayBrackets = null;


        try {
            // InternalQDL.g:2000:53: (iv_ruleArrayBrackets= ruleArrayBrackets EOF )
            // InternalQDL.g:2001:2: iv_ruleArrayBrackets= ruleArrayBrackets EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getArrayBracketsRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleArrayBrackets=ruleArrayBrackets();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleArrayBrackets.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleArrayBrackets"


    // $ANTLR start "ruleArrayBrackets"
    // InternalQDL.g:2007:1: ruleArrayBrackets returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '[' kw= ']' ) ;
    public final AntlrDatatypeRuleToken ruleArrayBrackets() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalQDL.g:2013:2: ( (kw= '[' kw= ']' ) )
            // InternalQDL.g:2014:2: (kw= '[' kw= ']' )
            {
            // InternalQDL.g:2014:2: (kw= '[' kw= ']' )
            // InternalQDL.g:2015:3: kw= '[' kw= ']'
            {
            kw=(Token)match(input,57,FOLLOW_27); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current.merge(kw);
              			newLeafNode(kw, grammarAccess.getArrayBracketsAccess().getLeftSquareBracketKeyword_0());
              		
            }
            kw=(Token)match(input,58,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current.merge(kw);
              			newLeafNode(kw, grammarAccess.getArrayBracketsAccess().getRightSquareBracketKeyword_1());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleArrayBrackets"


    // $ANTLR start "entryRuleJvmFormalParameter"
    // InternalQDL.g:2029:1: entryRuleJvmFormalParameter returns [EObject current=null] : iv_ruleJvmFormalParameter= ruleJvmFormalParameter EOF ;
    public final EObject entryRuleJvmFormalParameter() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJvmFormalParameter = null;


        try {
            // InternalQDL.g:2029:59: (iv_ruleJvmFormalParameter= ruleJvmFormalParameter EOF )
            // InternalQDL.g:2030:2: iv_ruleJvmFormalParameter= ruleJvmFormalParameter EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getJvmFormalParameterRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleJvmFormalParameter=ruleJvmFormalParameter();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleJvmFormalParameter; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleJvmFormalParameter"


    // $ANTLR start "ruleJvmFormalParameter"
    // InternalQDL.g:2036:1: ruleJvmFormalParameter returns [EObject current=null] : ( ( (lv_parameterType_0_0= ruleJvmTypeReference ) ) ( (lv_name_1_0= RULE_ID ) ) ) ;
    public final EObject ruleJvmFormalParameter() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_0=null;
        EObject lv_parameterType_0_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:2042:2: ( ( ( (lv_parameterType_0_0= ruleJvmTypeReference ) ) ( (lv_name_1_0= RULE_ID ) ) ) )
            // InternalQDL.g:2043:2: ( ( (lv_parameterType_0_0= ruleJvmTypeReference ) ) ( (lv_name_1_0= RULE_ID ) ) )
            {
            // InternalQDL.g:2043:2: ( ( (lv_parameterType_0_0= ruleJvmTypeReference ) ) ( (lv_name_1_0= RULE_ID ) ) )
            // InternalQDL.g:2044:3: ( (lv_parameterType_0_0= ruleJvmTypeReference ) ) ( (lv_name_1_0= RULE_ID ) )
            {
            // InternalQDL.g:2044:3: ( (lv_parameterType_0_0= ruleJvmTypeReference ) )
            // InternalQDL.g:2045:4: (lv_parameterType_0_0= ruleJvmTypeReference )
            {
            // InternalQDL.g:2045:4: (lv_parameterType_0_0= ruleJvmTypeReference )
            // InternalQDL.g:2046:5: lv_parameterType_0_0= ruleJvmTypeReference
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getJvmFormalParameterAccess().getParameterTypeJvmTypeReferenceParserRuleCall_0_0());
              				
            }
            pushFollow(FOLLOW_6);
            lv_parameterType_0_0=ruleJvmTypeReference();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getJvmFormalParameterRule());
              					}
              					set(
              						current,
              						"parameterType",
              						lv_parameterType_0_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalQDL.g:2063:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalQDL.g:2064:4: (lv_name_1_0= RULE_ID )
            {
            // InternalQDL.g:2064:4: (lv_name_1_0= RULE_ID )
            // InternalQDL.g:2065:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_1_0, grammarAccess.getJvmFormalParameterAccess().getNameIDTerminalRuleCall_1_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getJvmFormalParameterRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_1_0,
              						"org.eclipse.xtext.common.Terminals.ID");
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleJvmFormalParameter"


    // $ANTLR start "entryRuleIQLMethod"
    // InternalQDL.g:2085:1: entryRuleIQLMethod returns [EObject current=null] : iv_ruleIQLMethod= ruleIQLMethod EOF ;
    public final EObject entryRuleIQLMethod() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMethod = null;


        try {
            // InternalQDL.g:2085:50: (iv_ruleIQLMethod= ruleIQLMethod EOF )
            // InternalQDL.g:2086:2: iv_ruleIQLMethod= ruleIQLMethod EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLMethodRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLMethod=ruleIQLMethod();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLMethod; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLMethod"


    // $ANTLR start "ruleIQLMethod"
    // InternalQDL.g:2092:1: ruleIQLMethod returns [EObject current=null] : ( () ( (lv_override_1_0= 'override' ) )? ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )? (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )? ( (lv_body_10_0= ruleIQLStatementBlock ) ) ) ;
    public final EObject ruleIQLMethod() throws RecognitionException {
        EObject current = null;

        Token lv_override_1_0=null;
        Token lv_simpleName_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        Token otherlv_8=null;
        EObject lv_parameters_4_0 = null;

        EObject lv_parameters_6_0 = null;

        EObject lv_returnType_9_0 = null;

        EObject lv_body_10_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:2098:2: ( ( () ( (lv_override_1_0= 'override' ) )? ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )? (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )? ( (lv_body_10_0= ruleIQLStatementBlock ) ) ) )
            // InternalQDL.g:2099:2: ( () ( (lv_override_1_0= 'override' ) )? ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )? (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )? ( (lv_body_10_0= ruleIQLStatementBlock ) ) )
            {
            // InternalQDL.g:2099:2: ( () ( (lv_override_1_0= 'override' ) )? ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )? (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )? ( (lv_body_10_0= ruleIQLStatementBlock ) ) )
            // InternalQDL.g:2100:3: () ( (lv_override_1_0= 'override' ) )? ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )? (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )? ( (lv_body_10_0= ruleIQLStatementBlock ) )
            {
            // InternalQDL.g:2100:3: ()
            // InternalQDL.g:2101:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLMethodAccess().getIQLMethodAction_0(),
              					current);
              			
            }

            }

            // InternalQDL.g:2107:3: ( (lv_override_1_0= 'override' ) )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==70) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // InternalQDL.g:2108:4: (lv_override_1_0= 'override' )
                    {
                    // InternalQDL.g:2108:4: (lv_override_1_0= 'override' )
                    // InternalQDL.g:2109:5: lv_override_1_0= 'override'
                    {
                    lv_override_1_0=(Token)match(input,70,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_override_1_0, grammarAccess.getIQLMethodAccess().getOverrideOverrideKeyword_1_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getIQLMethodRule());
                      					}
                      					setWithLastConsumed(current, "override", true, "override");
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalQDL.g:2121:3: ( (lv_simpleName_2_0= RULE_ID ) )
            // InternalQDL.g:2122:4: (lv_simpleName_2_0= RULE_ID )
            {
            // InternalQDL.g:2122:4: (lv_simpleName_2_0= RULE_ID )
            // InternalQDL.g:2123:5: lv_simpleName_2_0= RULE_ID
            {
            lv_simpleName_2_0=(Token)match(input,RULE_ID,FOLLOW_28); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_simpleName_2_0, grammarAccess.getIQLMethodAccess().getSimpleNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLMethodRule());
              					}
              					setWithLastConsumed(
              						current,
              						"simpleName",
              						lv_simpleName_2_0,
              						"org.eclipse.xtext.common.Terminals.ID");
              				
            }

            }


            }

            // InternalQDL.g:2139:3: (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==14) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // InternalQDL.g:2140:4: otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')'
                    {
                    otherlv_3=(Token)match(input,14,FOLLOW_8); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getIQLMethodAccess().getLeftParenthesisKeyword_3_0());
                      			
                    }
                    // InternalQDL.g:2144:4: ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )?
                    int alt30=2;
                    int LA30_0 = input.LA(1);

                    if ( (LA30_0==RULE_ID) ) {
                        alt30=1;
                    }
                    switch (alt30) {
                        case 1 :
                            // InternalQDL.g:2145:5: ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )*
                            {
                            // InternalQDL.g:2145:5: ( (lv_parameters_4_0= ruleJvmFormalParameter ) )
                            // InternalQDL.g:2146:6: (lv_parameters_4_0= ruleJvmFormalParameter )
                            {
                            // InternalQDL.g:2146:6: (lv_parameters_4_0= ruleJvmFormalParameter )
                            // InternalQDL.g:2147:7: lv_parameters_4_0= ruleJvmFormalParameter
                            {
                            if ( state.backtracking==0 ) {

                              							newCompositeNode(grammarAccess.getIQLMethodAccess().getParametersJvmFormalParameterParserRuleCall_3_1_0_0());
                              						
                            }
                            pushFollow(FOLLOW_29);
                            lv_parameters_4_0=ruleJvmFormalParameter();

                            state._fsp--;
                            if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              							if (current==null) {
                              								current = createModelElementForParent(grammarAccess.getIQLMethodRule());
                              							}
                              							add(
                              								current,
                              								"parameters",
                              								lv_parameters_4_0,
                              								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmFormalParameter");
                              							afterParserOrEnumRuleCall();
                              						
                            }

                            }


                            }

                            // InternalQDL.g:2164:5: (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )*
                            loop29:
                            do {
                                int alt29=2;
                                int LA29_0 = input.LA(1);

                                if ( (LA29_0==63) ) {
                                    alt29=1;
                                }


                                switch (alt29) {
                            	case 1 :
                            	    // InternalQDL.g:2165:6: otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) )
                            	    {
                            	    otherlv_5=(Token)match(input,63,FOLLOW_6); if (state.failed) return current;
                            	    if ( state.backtracking==0 ) {

                            	      						newLeafNode(otherlv_5, grammarAccess.getIQLMethodAccess().getCommaKeyword_3_1_1_0());
                            	      					
                            	    }
                            	    // InternalQDL.g:2169:6: ( (lv_parameters_6_0= ruleJvmFormalParameter ) )
                            	    // InternalQDL.g:2170:7: (lv_parameters_6_0= ruleJvmFormalParameter )
                            	    {
                            	    // InternalQDL.g:2170:7: (lv_parameters_6_0= ruleJvmFormalParameter )
                            	    // InternalQDL.g:2171:8: lv_parameters_6_0= ruleJvmFormalParameter
                            	    {
                            	    if ( state.backtracking==0 ) {

                            	      								newCompositeNode(grammarAccess.getIQLMethodAccess().getParametersJvmFormalParameterParserRuleCall_3_1_1_1_0());
                            	      							
                            	    }
                            	    pushFollow(FOLLOW_29);
                            	    lv_parameters_6_0=ruleJvmFormalParameter();

                            	    state._fsp--;
                            	    if (state.failed) return current;
                            	    if ( state.backtracking==0 ) {

                            	      								if (current==null) {
                            	      									current = createModelElementForParent(grammarAccess.getIQLMethodRule());
                            	      								}
                            	      								add(
                            	      									current,
                            	      									"parameters",
                            	      									lv_parameters_6_0,
                            	      									"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmFormalParameter");
                            	      								afterParserOrEnumRuleCall();
                            	      							
                            	    }

                            	    }


                            	    }


                            	    }
                            	    break;

                            	default :
                            	    break loop29;
                                }
                            } while (true);


                            }
                            break;

                    }

                    otherlv_7=(Token)match(input,15,FOLLOW_28); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_7, grammarAccess.getIQLMethodAccess().getRightParenthesisKeyword_3_2());
                      			
                    }

                    }
                    break;

            }

            // InternalQDL.g:2195:3: (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==17) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // InternalQDL.g:2196:4: otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) )
                    {
                    otherlv_8=(Token)match(input,17,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_8, grammarAccess.getIQLMethodAccess().getColonKeyword_4_0());
                      			
                    }
                    // InternalQDL.g:2200:4: ( (lv_returnType_9_0= ruleJvmTypeReference ) )
                    // InternalQDL.g:2201:5: (lv_returnType_9_0= ruleJvmTypeReference )
                    {
                    // InternalQDL.g:2201:5: (lv_returnType_9_0= ruleJvmTypeReference )
                    // InternalQDL.g:2202:6: lv_returnType_9_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMethodAccess().getReturnTypeJvmTypeReferenceParserRuleCall_4_1_0());
                      					
                    }
                    pushFollow(FOLLOW_7);
                    lv_returnType_9_0=ruleJvmTypeReference();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLMethodRule());
                      						}
                      						set(
                      							current,
                      							"returnType",
                      							lv_returnType_9_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalQDL.g:2220:3: ( (lv_body_10_0= ruleIQLStatementBlock ) )
            // InternalQDL.g:2221:4: (lv_body_10_0= ruleIQLStatementBlock )
            {
            // InternalQDL.g:2221:4: (lv_body_10_0= ruleIQLStatementBlock )
            // InternalQDL.g:2222:5: lv_body_10_0= ruleIQLStatementBlock
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLMethodAccess().getBodyIQLStatementBlockParserRuleCall_5_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_body_10_0=ruleIQLStatementBlock();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLMethodRule());
              					}
              					set(
              						current,
              						"body",
              						lv_body_10_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLStatementBlock");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLMethod"


    // $ANTLR start "entryRuleIQLMethodDeclaration"
    // InternalQDL.g:2243:1: entryRuleIQLMethodDeclaration returns [EObject current=null] : iv_ruleIQLMethodDeclaration= ruleIQLMethodDeclaration EOF ;
    public final EObject entryRuleIQLMethodDeclaration() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMethodDeclaration = null;


        try {
            // InternalQDL.g:2243:61: (iv_ruleIQLMethodDeclaration= ruleIQLMethodDeclaration EOF )
            // InternalQDL.g:2244:2: iv_ruleIQLMethodDeclaration= ruleIQLMethodDeclaration EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLMethodDeclarationRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLMethodDeclaration=ruleIQLMethodDeclaration();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLMethodDeclaration; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLMethodDeclaration"


    // $ANTLR start "ruleIQLMethodDeclaration"
    // InternalQDL.g:2250:1: ruleIQLMethodDeclaration returns [EObject current=null] : ( () ( (lv_simpleName_1_0= RULE_ID ) ) otherlv_2= '(' ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )? otherlv_6= ')' (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )? otherlv_9= ';' ) ;
    public final EObject ruleIQLMethodDeclaration() throws RecognitionException {
        EObject current = null;

        Token lv_simpleName_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        EObject lv_parameters_3_0 = null;

        EObject lv_parameters_5_0 = null;

        EObject lv_returnType_8_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:2256:2: ( ( () ( (lv_simpleName_1_0= RULE_ID ) ) otherlv_2= '(' ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )? otherlv_6= ')' (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )? otherlv_9= ';' ) )
            // InternalQDL.g:2257:2: ( () ( (lv_simpleName_1_0= RULE_ID ) ) otherlv_2= '(' ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )? otherlv_6= ')' (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )? otherlv_9= ';' )
            {
            // InternalQDL.g:2257:2: ( () ( (lv_simpleName_1_0= RULE_ID ) ) otherlv_2= '(' ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )? otherlv_6= ')' (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )? otherlv_9= ';' )
            // InternalQDL.g:2258:3: () ( (lv_simpleName_1_0= RULE_ID ) ) otherlv_2= '(' ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )? otherlv_6= ')' (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )? otherlv_9= ';'
            {
            // InternalQDL.g:2258:3: ()
            // InternalQDL.g:2259:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLMethodDeclarationAccess().getIQLMethodDeclarationAction_0(),
              					current);
              			
            }

            }

            // InternalQDL.g:2265:3: ( (lv_simpleName_1_0= RULE_ID ) )
            // InternalQDL.g:2266:4: (lv_simpleName_1_0= RULE_ID )
            {
            // InternalQDL.g:2266:4: (lv_simpleName_1_0= RULE_ID )
            // InternalQDL.g:2267:5: lv_simpleName_1_0= RULE_ID
            {
            lv_simpleName_1_0=(Token)match(input,RULE_ID,FOLLOW_30); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_simpleName_1_0, grammarAccess.getIQLMethodDeclarationAccess().getSimpleNameIDTerminalRuleCall_1_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLMethodDeclarationRule());
              					}
              					setWithLastConsumed(
              						current,
              						"simpleName",
              						lv_simpleName_1_0,
              						"org.eclipse.xtext.common.Terminals.ID");
              				
            }

            }


            }

            otherlv_2=(Token)match(input,14,FOLLOW_8); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLMethodDeclarationAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalQDL.g:2287:3: ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==RULE_ID) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // InternalQDL.g:2288:4: ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )*
                    {
                    // InternalQDL.g:2288:4: ( (lv_parameters_3_0= ruleJvmFormalParameter ) )
                    // InternalQDL.g:2289:5: (lv_parameters_3_0= ruleJvmFormalParameter )
                    {
                    // InternalQDL.g:2289:5: (lv_parameters_3_0= ruleJvmFormalParameter )
                    // InternalQDL.g:2290:6: lv_parameters_3_0= ruleJvmFormalParameter
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMethodDeclarationAccess().getParametersJvmFormalParameterParserRuleCall_3_0_0());
                      					
                    }
                    pushFollow(FOLLOW_29);
                    lv_parameters_3_0=ruleJvmFormalParameter();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLMethodDeclarationRule());
                      						}
                      						add(
                      							current,
                      							"parameters",
                      							lv_parameters_3_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmFormalParameter");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalQDL.g:2307:4: (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )*
                    loop33:
                    do {
                        int alt33=2;
                        int LA33_0 = input.LA(1);

                        if ( (LA33_0==63) ) {
                            alt33=1;
                        }


                        switch (alt33) {
                    	case 1 :
                    	    // InternalQDL.g:2308:5: otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) )
                    	    {
                    	    otherlv_4=(Token)match(input,63,FOLLOW_6); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_4, grammarAccess.getIQLMethodDeclarationAccess().getCommaKeyword_3_1_0());
                    	      				
                    	    }
                    	    // InternalQDL.g:2312:5: ( (lv_parameters_5_0= ruleJvmFormalParameter ) )
                    	    // InternalQDL.g:2313:6: (lv_parameters_5_0= ruleJvmFormalParameter )
                    	    {
                    	    // InternalQDL.g:2313:6: (lv_parameters_5_0= ruleJvmFormalParameter )
                    	    // InternalQDL.g:2314:7: lv_parameters_5_0= ruleJvmFormalParameter
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLMethodDeclarationAccess().getParametersJvmFormalParameterParserRuleCall_3_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_29);
                    	    lv_parameters_5_0=ruleJvmFormalParameter();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElementForParent(grammarAccess.getIQLMethodDeclarationRule());
                    	      							}
                    	      							add(
                    	      								current,
                    	      								"parameters",
                    	      								lv_parameters_5_0,
                    	      								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmFormalParameter");
                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop33;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_6=(Token)match(input,15,FOLLOW_31); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_6, grammarAccess.getIQLMethodDeclarationAccess().getRightParenthesisKeyword_4());
              		
            }
            // InternalQDL.g:2337:3: (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==17) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // InternalQDL.g:2338:4: otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) )
                    {
                    otherlv_7=(Token)match(input,17,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_7, grammarAccess.getIQLMethodDeclarationAccess().getColonKeyword_5_0());
                      			
                    }
                    // InternalQDL.g:2342:4: ( (lv_returnType_8_0= ruleJvmTypeReference ) )
                    // InternalQDL.g:2343:5: (lv_returnType_8_0= ruleJvmTypeReference )
                    {
                    // InternalQDL.g:2343:5: (lv_returnType_8_0= ruleJvmTypeReference )
                    // InternalQDL.g:2344:6: lv_returnType_8_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMethodDeclarationAccess().getReturnTypeJvmTypeReferenceParserRuleCall_5_1_0());
                      					
                    }
                    pushFollow(FOLLOW_17);
                    lv_returnType_8_0=ruleJvmTypeReference();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLMethodDeclarationRule());
                      						}
                      						set(
                      							current,
                      							"returnType",
                      							lv_returnType_8_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            otherlv_9=(Token)match(input,62,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_9, grammarAccess.getIQLMethodDeclarationAccess().getSemicolonKeyword_6());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLMethodDeclaration"


    // $ANTLR start "entryRuleIQLJavaMember"
    // InternalQDL.g:2370:1: entryRuleIQLJavaMember returns [EObject current=null] : iv_ruleIQLJavaMember= ruleIQLJavaMember EOF ;
    public final EObject entryRuleIQLJavaMember() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLJavaMember = null;


        try {
            // InternalQDL.g:2370:54: (iv_ruleIQLJavaMember= ruleIQLJavaMember EOF )
            // InternalQDL.g:2371:2: iv_ruleIQLJavaMember= ruleIQLJavaMember EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLJavaMemberRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLJavaMember=ruleIQLJavaMember();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLJavaMember; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLJavaMember"


    // $ANTLR start "ruleIQLJavaMember"
    // InternalQDL.g:2377:1: ruleIQLJavaMember returns [EObject current=null] : ( () ( (lv_java_1_0= ruleIQLJava ) ) ) ;
    public final EObject ruleIQLJavaMember() throws RecognitionException {
        EObject current = null;

        EObject lv_java_1_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:2383:2: ( ( () ( (lv_java_1_0= ruleIQLJava ) ) ) )
            // InternalQDL.g:2384:2: ( () ( (lv_java_1_0= ruleIQLJava ) ) )
            {
            // InternalQDL.g:2384:2: ( () ( (lv_java_1_0= ruleIQLJava ) ) )
            // InternalQDL.g:2385:3: () ( (lv_java_1_0= ruleIQLJava ) )
            {
            // InternalQDL.g:2385:3: ()
            // InternalQDL.g:2386:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLJavaMemberAccess().getIQLJavaMemberAction_0(),
              					current);
              			
            }

            }

            // InternalQDL.g:2392:3: ( (lv_java_1_0= ruleIQLJava ) )
            // InternalQDL.g:2393:4: (lv_java_1_0= ruleIQLJava )
            {
            // InternalQDL.g:2393:4: (lv_java_1_0= ruleIQLJava )
            // InternalQDL.g:2394:5: lv_java_1_0= ruleIQLJava
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLJavaMemberAccess().getJavaIQLJavaParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_java_1_0=ruleIQLJava();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLJavaMemberRule());
              					}
              					set(
              						current,
              						"java",
              						lv_java_1_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLJava");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLJavaMember"


    // $ANTLR start "entryRuleIQLMetadataList"
    // InternalQDL.g:2415:1: entryRuleIQLMetadataList returns [EObject current=null] : iv_ruleIQLMetadataList= ruleIQLMetadataList EOF ;
    public final EObject entryRuleIQLMetadataList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataList = null;


        try {
            // InternalQDL.g:2415:56: (iv_ruleIQLMetadataList= ruleIQLMetadataList EOF )
            // InternalQDL.g:2416:2: iv_ruleIQLMetadataList= ruleIQLMetadataList EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLMetadataListRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLMetadataList=ruleIQLMetadataList();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLMetadataList; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLMetadataList"


    // $ANTLR start "ruleIQLMetadataList"
    // InternalQDL.g:2422:1: ruleIQLMetadataList returns [EObject current=null] : ( ( (lv_elements_0_0= ruleIQLMetadata ) ) (otherlv_1= ',' ( (lv_elements_2_0= ruleIQLMetadata ) ) )* ) ;
    public final EObject ruleIQLMetadataList() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_elements_0_0 = null;

        EObject lv_elements_2_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:2428:2: ( ( ( (lv_elements_0_0= ruleIQLMetadata ) ) (otherlv_1= ',' ( (lv_elements_2_0= ruleIQLMetadata ) ) )* ) )
            // InternalQDL.g:2429:2: ( ( (lv_elements_0_0= ruleIQLMetadata ) ) (otherlv_1= ',' ( (lv_elements_2_0= ruleIQLMetadata ) ) )* )
            {
            // InternalQDL.g:2429:2: ( ( (lv_elements_0_0= ruleIQLMetadata ) ) (otherlv_1= ',' ( (lv_elements_2_0= ruleIQLMetadata ) ) )* )
            // InternalQDL.g:2430:3: ( (lv_elements_0_0= ruleIQLMetadata ) ) (otherlv_1= ',' ( (lv_elements_2_0= ruleIQLMetadata ) ) )*
            {
            // InternalQDL.g:2430:3: ( (lv_elements_0_0= ruleIQLMetadata ) )
            // InternalQDL.g:2431:4: (lv_elements_0_0= ruleIQLMetadata )
            {
            // InternalQDL.g:2431:4: (lv_elements_0_0= ruleIQLMetadata )
            // InternalQDL.g:2432:5: lv_elements_0_0= ruleIQLMetadata
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLMetadataListAccess().getElementsIQLMetadataParserRuleCall_0_0());
              				
            }
            pushFollow(FOLLOW_32);
            lv_elements_0_0=ruleIQLMetadata();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLMetadataListRule());
              					}
              					add(
              						current,
              						"elements",
              						lv_elements_0_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMetadata");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalQDL.g:2449:3: (otherlv_1= ',' ( (lv_elements_2_0= ruleIQLMetadata ) ) )*
            loop36:
            do {
                int alt36=2;
                int LA36_0 = input.LA(1);

                if ( (LA36_0==63) ) {
                    alt36=1;
                }


                switch (alt36) {
            	case 1 :
            	    // InternalQDL.g:2450:4: otherlv_1= ',' ( (lv_elements_2_0= ruleIQLMetadata ) )
            	    {
            	    otherlv_1=(Token)match(input,63,FOLLOW_6); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				newLeafNode(otherlv_1, grammarAccess.getIQLMetadataListAccess().getCommaKeyword_1_0());
            	      			
            	    }
            	    // InternalQDL.g:2454:4: ( (lv_elements_2_0= ruleIQLMetadata ) )
            	    // InternalQDL.g:2455:5: (lv_elements_2_0= ruleIQLMetadata )
            	    {
            	    // InternalQDL.g:2455:5: (lv_elements_2_0= ruleIQLMetadata )
            	    // InternalQDL.g:2456:6: lv_elements_2_0= ruleIQLMetadata
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLMetadataListAccess().getElementsIQLMetadataParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_32);
            	    lv_elements_2_0=ruleIQLMetadata();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						if (current==null) {
            	      							current = createModelElementForParent(grammarAccess.getIQLMetadataListRule());
            	      						}
            	      						add(
            	      							current,
            	      							"elements",
            	      							lv_elements_2_0,
            	      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMetadata");
            	      						afterParserOrEnumRuleCall();
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop36;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLMetadataList"


    // $ANTLR start "entryRuleIQLMetadata"
    // InternalQDL.g:2478:1: entryRuleIQLMetadata returns [EObject current=null] : iv_ruleIQLMetadata= ruleIQLMetadata EOF ;
    public final EObject entryRuleIQLMetadata() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadata = null;


        try {
            // InternalQDL.g:2478:52: (iv_ruleIQLMetadata= ruleIQLMetadata EOF )
            // InternalQDL.g:2479:2: iv_ruleIQLMetadata= ruleIQLMetadata EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLMetadataRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLMetadata=ruleIQLMetadata();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLMetadata; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLMetadata"


    // $ANTLR start "ruleIQLMetadata"
    // InternalQDL.g:2485:1: ruleIQLMetadata returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )? ) ;
    public final EObject ruleIQLMetadata() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:2491:2: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )? ) )
            // InternalQDL.g:2492:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )? )
            {
            // InternalQDL.g:2492:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )? )
            // InternalQDL.g:2493:3: ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )?
            {
            // InternalQDL.g:2493:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalQDL.g:2494:4: (lv_name_0_0= RULE_ID )
            {
            // InternalQDL.g:2494:4: (lv_name_0_0= RULE_ID )
            // InternalQDL.g:2495:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_33); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_0_0, grammarAccess.getIQLMetadataAccess().getNameIDTerminalRuleCall_0_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLMetadataRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_0_0,
              						"org.eclipse.xtext.common.Terminals.ID");
              				
            }

            }


            }

            // InternalQDL.g:2511:3: (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==42) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // InternalQDL.g:2512:4: otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) )
                    {
                    otherlv_1=(Token)match(input,42,FOLLOW_34); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_1, grammarAccess.getIQLMetadataAccess().getEqualsSignKeyword_1_0());
                      			
                    }
                    // InternalQDL.g:2516:4: ( (lv_value_2_0= ruleIQLMetadataValue ) )
                    // InternalQDL.g:2517:5: (lv_value_2_0= ruleIQLMetadataValue )
                    {
                    // InternalQDL.g:2517:5: (lv_value_2_0= ruleIQLMetadataValue )
                    // InternalQDL.g:2518:6: lv_value_2_0= ruleIQLMetadataValue
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMetadataAccess().getValueIQLMetadataValueParserRuleCall_1_1_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_value_2_0=ruleIQLMetadataValue();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLMetadataRule());
                      						}
                      						set(
                      							current,
                      							"value",
                      							lv_value_2_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMetadataValue");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLMetadata"


    // $ANTLR start "entryRuleIQLMetadataValue"
    // InternalQDL.g:2540:1: entryRuleIQLMetadataValue returns [EObject current=null] : iv_ruleIQLMetadataValue= ruleIQLMetadataValue EOF ;
    public final EObject entryRuleIQLMetadataValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataValue = null;


        try {
            // InternalQDL.g:2540:57: (iv_ruleIQLMetadataValue= ruleIQLMetadataValue EOF )
            // InternalQDL.g:2541:2: iv_ruleIQLMetadataValue= ruleIQLMetadataValue EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLMetadataValueRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLMetadataValue=ruleIQLMetadataValue();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLMetadataValue; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLMetadataValue"


    // $ANTLR start "ruleIQLMetadataValue"
    // InternalQDL.g:2547:1: ruleIQLMetadataValue returns [EObject current=null] : (this_IQLMetadataValueSingle_0= ruleIQLMetadataValueSingle | this_IQLMetadataValueList_1= ruleIQLMetadataValueList | this_IQLMetadataValueMap_2= ruleIQLMetadataValueMap ) ;
    public final EObject ruleIQLMetadataValue() throws RecognitionException {
        EObject current = null;

        EObject this_IQLMetadataValueSingle_0 = null;

        EObject this_IQLMetadataValueList_1 = null;

        EObject this_IQLMetadataValueMap_2 = null;



        	enterRule();

        try {
            // InternalQDL.g:2553:2: ( (this_IQLMetadataValueSingle_0= ruleIQLMetadataValueSingle | this_IQLMetadataValueList_1= ruleIQLMetadataValueList | this_IQLMetadataValueMap_2= ruleIQLMetadataValueMap ) )
            // InternalQDL.g:2554:2: (this_IQLMetadataValueSingle_0= ruleIQLMetadataValueSingle | this_IQLMetadataValueList_1= ruleIQLMetadataValueList | this_IQLMetadataValueMap_2= ruleIQLMetadataValueMap )
            {
            // InternalQDL.g:2554:2: (this_IQLMetadataValueSingle_0= ruleIQLMetadataValueSingle | this_IQLMetadataValueList_1= ruleIQLMetadataValueList | this_IQLMetadataValueMap_2= ruleIQLMetadataValueMap )
            int alt38=3;
            switch ( input.LA(1) ) {
            case RULE_ID:
            case RULE_INT:
            case RULE_DOUBLE:
            case RULE_STRING:
            case 20:
            case 111:
            case 112:
                {
                alt38=1;
                }
                break;
            case 57:
                {
                alt38=2;
                }
                break;
            case 59:
                {
                alt38=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 0, input);

                throw nvae;
            }

            switch (alt38) {
                case 1 :
                    // InternalQDL.g:2555:3: this_IQLMetadataValueSingle_0= ruleIQLMetadataValueSingle
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLMetadataValueAccess().getIQLMetadataValueSingleParserRuleCall_0());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLMetadataValueSingle_0=ruleIQLMetadataValueSingle();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLMetadataValueSingle_0;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalQDL.g:2564:3: this_IQLMetadataValueList_1= ruleIQLMetadataValueList
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLMetadataValueAccess().getIQLMetadataValueListParserRuleCall_1());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLMetadataValueList_1=ruleIQLMetadataValueList();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLMetadataValueList_1;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalQDL.g:2573:3: this_IQLMetadataValueMap_2= ruleIQLMetadataValueMap
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLMetadataValueAccess().getIQLMetadataValueMapParserRuleCall_2());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLMetadataValueMap_2=ruleIQLMetadataValueMap();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLMetadataValueMap_2;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLMetadataValue"


    // $ANTLR start "entryRuleIQLMetadataValueList"
    // InternalQDL.g:2585:1: entryRuleIQLMetadataValueList returns [EObject current=null] : iv_ruleIQLMetadataValueList= ruleIQLMetadataValueList EOF ;
    public final EObject entryRuleIQLMetadataValueList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataValueList = null;


        try {
            // InternalQDL.g:2585:61: (iv_ruleIQLMetadataValueList= ruleIQLMetadataValueList EOF )
            // InternalQDL.g:2586:2: iv_ruleIQLMetadataValueList= ruleIQLMetadataValueList EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLMetadataValueListRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLMetadataValueList=ruleIQLMetadataValueList();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLMetadataValueList; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLMetadataValueList"


    // $ANTLR start "ruleIQLMetadataValueList"
    // InternalQDL.g:2592:1: ruleIQLMetadataValueList returns [EObject current=null] : ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )? otherlv_5= ']' ) ;
    public final EObject ruleIQLMetadataValueList() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:2598:2: ( ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )? otherlv_5= ']' ) )
            // InternalQDL.g:2599:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )? otherlv_5= ']' )
            {
            // InternalQDL.g:2599:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )? otherlv_5= ']' )
            // InternalQDL.g:2600:3: () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )? otherlv_5= ']'
            {
            // InternalQDL.g:2600:3: ()
            // InternalQDL.g:2601:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLMetadataValueListAccess().getIQLMetadataValueListAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,57,FOLLOW_35); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLMetadataValueListAccess().getLeftSquareBracketKeyword_1());
              		
            }
            // InternalQDL.g:2611:3: ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( ((LA40_0>=RULE_ID && LA40_0<=RULE_STRING)||LA40_0==20||LA40_0==57||LA40_0==59||(LA40_0>=111 && LA40_0<=112)) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // InternalQDL.g:2612:4: ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )*
                    {
                    // InternalQDL.g:2612:4: ( (lv_elements_2_0= ruleIQLMetadataValue ) )
                    // InternalQDL.g:2613:5: (lv_elements_2_0= ruleIQLMetadataValue )
                    {
                    // InternalQDL.g:2613:5: (lv_elements_2_0= ruleIQLMetadataValue )
                    // InternalQDL.g:2614:6: lv_elements_2_0= ruleIQLMetadataValue
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMetadataValueListAccess().getElementsIQLMetadataValueParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_36);
                    lv_elements_2_0=ruleIQLMetadataValue();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLMetadataValueListRule());
                      						}
                      						add(
                      							current,
                      							"elements",
                      							lv_elements_2_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMetadataValue");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalQDL.g:2631:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )*
                    loop39:
                    do {
                        int alt39=2;
                        int LA39_0 = input.LA(1);

                        if ( (LA39_0==63) ) {
                            alt39=1;
                        }


                        switch (alt39) {
                    	case 1 :
                    	    // InternalQDL.g:2632:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) )
                    	    {
                    	    otherlv_3=(Token)match(input,63,FOLLOW_34); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLMetadataValueListAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalQDL.g:2636:5: ( (lv_elements_4_0= ruleIQLMetadataValue ) )
                    	    // InternalQDL.g:2637:6: (lv_elements_4_0= ruleIQLMetadataValue )
                    	    {
                    	    // InternalQDL.g:2637:6: (lv_elements_4_0= ruleIQLMetadataValue )
                    	    // InternalQDL.g:2638:7: lv_elements_4_0= ruleIQLMetadataValue
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLMetadataValueListAccess().getElementsIQLMetadataValueParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_36);
                    	    lv_elements_4_0=ruleIQLMetadataValue();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElementForParent(grammarAccess.getIQLMetadataValueListRule());
                    	      							}
                    	      							add(
                    	      								current,
                    	      								"elements",
                    	      								lv_elements_4_0,
                    	      								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMetadataValue");
                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop39;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,58,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getIQLMetadataValueListAccess().getRightSquareBracketKeyword_3());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLMetadataValueList"


    // $ANTLR start "entryRuleIQLMetadataValueMap"
    // InternalQDL.g:2665:1: entryRuleIQLMetadataValueMap returns [EObject current=null] : iv_ruleIQLMetadataValueMap= ruleIQLMetadataValueMap EOF ;
    public final EObject entryRuleIQLMetadataValueMap() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataValueMap = null;


        try {
            // InternalQDL.g:2665:60: (iv_ruleIQLMetadataValueMap= ruleIQLMetadataValueMap EOF )
            // InternalQDL.g:2666:2: iv_ruleIQLMetadataValueMap= ruleIQLMetadataValueMap EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLMetadataValueMapRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLMetadataValueMap=ruleIQLMetadataValueMap();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLMetadataValueMap; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLMetadataValueMap"


    // $ANTLR start "ruleIQLMetadataValueMap"
    // InternalQDL.g:2672:1: ruleIQLMetadataValueMap returns [EObject current=null] : ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )? otherlv_5= '}' ) ;
    public final EObject ruleIQLMetadataValueMap() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:2678:2: ( ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )? otherlv_5= '}' ) )
            // InternalQDL.g:2679:2: ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )? otherlv_5= '}' )
            {
            // InternalQDL.g:2679:2: ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )? otherlv_5= '}' )
            // InternalQDL.g:2680:3: () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )? otherlv_5= '}'
            {
            // InternalQDL.g:2680:3: ()
            // InternalQDL.g:2681:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLMetadataValueMapAccess().getIQLMetadataValueMapAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,59,FOLLOW_37); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLMetadataValueMapAccess().getLeftCurlyBracketKeyword_1());
              		
            }
            // InternalQDL.g:2691:3: ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( ((LA42_0>=RULE_ID && LA42_0<=RULE_STRING)||LA42_0==20||LA42_0==57||LA42_0==59||(LA42_0>=111 && LA42_0<=112)) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // InternalQDL.g:2692:4: ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )*
                    {
                    // InternalQDL.g:2692:4: ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) )
                    // InternalQDL.g:2693:5: (lv_elements_2_0= ruleIQLMetadataValueMapElement )
                    {
                    // InternalQDL.g:2693:5: (lv_elements_2_0= ruleIQLMetadataValueMapElement )
                    // InternalQDL.g:2694:6: lv_elements_2_0= ruleIQLMetadataValueMapElement
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMetadataValueMapAccess().getElementsIQLMetadataValueMapElementParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_38);
                    lv_elements_2_0=ruleIQLMetadataValueMapElement();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLMetadataValueMapRule());
                      						}
                      						add(
                      							current,
                      							"elements",
                      							lv_elements_2_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMetadataValueMapElement");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalQDL.g:2711:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )*
                    loop41:
                    do {
                        int alt41=2;
                        int LA41_0 = input.LA(1);

                        if ( (LA41_0==63) ) {
                            alt41=1;
                        }


                        switch (alt41) {
                    	case 1 :
                    	    // InternalQDL.g:2712:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) )
                    	    {
                    	    otherlv_3=(Token)match(input,63,FOLLOW_34); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLMetadataValueMapAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalQDL.g:2716:5: ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) )
                    	    // InternalQDL.g:2717:6: (lv_elements_4_0= ruleIQLMetadataValueMapElement )
                    	    {
                    	    // InternalQDL.g:2717:6: (lv_elements_4_0= ruleIQLMetadataValueMapElement )
                    	    // InternalQDL.g:2718:7: lv_elements_4_0= ruleIQLMetadataValueMapElement
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLMetadataValueMapAccess().getElementsIQLMetadataValueMapElementParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_38);
                    	    lv_elements_4_0=ruleIQLMetadataValueMapElement();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElementForParent(grammarAccess.getIQLMetadataValueMapRule());
                    	      							}
                    	      							add(
                    	      								current,
                    	      								"elements",
                    	      								lv_elements_4_0,
                    	      								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMetadataValueMapElement");
                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop41;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,60,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getIQLMetadataValueMapAccess().getRightCurlyBracketKeyword_3());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLMetadataValueMap"


    // $ANTLR start "entryRuleIQLMetadataValueMapElement"
    // InternalQDL.g:2745:1: entryRuleIQLMetadataValueMapElement returns [EObject current=null] : iv_ruleIQLMetadataValueMapElement= ruleIQLMetadataValueMapElement EOF ;
    public final EObject entryRuleIQLMetadataValueMapElement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataValueMapElement = null;


        try {
            // InternalQDL.g:2745:67: (iv_ruleIQLMetadataValueMapElement= ruleIQLMetadataValueMapElement EOF )
            // InternalQDL.g:2746:2: iv_ruleIQLMetadataValueMapElement= ruleIQLMetadataValueMapElement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLMetadataValueMapElementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLMetadataValueMapElement=ruleIQLMetadataValueMapElement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLMetadataValueMapElement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLMetadataValueMapElement"


    // $ANTLR start "ruleIQLMetadataValueMapElement"
    // InternalQDL.g:2752:1: ruleIQLMetadataValueMapElement returns [EObject current=null] : ( ( (lv_key_0_0= ruleIQLMetadataValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) ) ;
    public final EObject ruleIQLMetadataValueMapElement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_key_0_0 = null;

        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:2758:2: ( ( ( (lv_key_0_0= ruleIQLMetadataValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) ) )
            // InternalQDL.g:2759:2: ( ( (lv_key_0_0= ruleIQLMetadataValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )
            {
            // InternalQDL.g:2759:2: ( ( (lv_key_0_0= ruleIQLMetadataValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )
            // InternalQDL.g:2760:3: ( (lv_key_0_0= ruleIQLMetadataValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) )
            {
            // InternalQDL.g:2760:3: ( (lv_key_0_0= ruleIQLMetadataValue ) )
            // InternalQDL.g:2761:4: (lv_key_0_0= ruleIQLMetadataValue )
            {
            // InternalQDL.g:2761:4: (lv_key_0_0= ruleIQLMetadataValue )
            // InternalQDL.g:2762:5: lv_key_0_0= ruleIQLMetadataValue
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLMetadataValueMapElementAccess().getKeyIQLMetadataValueParserRuleCall_0_0());
              				
            }
            pushFollow(FOLLOW_39);
            lv_key_0_0=ruleIQLMetadataValue();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLMetadataValueMapElementRule());
              					}
              					set(
              						current,
              						"key",
              						lv_key_0_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMetadataValue");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_1=(Token)match(input,42,FOLLOW_34); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLMetadataValueMapElementAccess().getEqualsSignKeyword_1());
              		
            }
            // InternalQDL.g:2783:3: ( (lv_value_2_0= ruleIQLMetadataValue ) )
            // InternalQDL.g:2784:4: (lv_value_2_0= ruleIQLMetadataValue )
            {
            // InternalQDL.g:2784:4: (lv_value_2_0= ruleIQLMetadataValue )
            // InternalQDL.g:2785:5: lv_value_2_0= ruleIQLMetadataValue
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLMetadataValueMapElementAccess().getValueIQLMetadataValueParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_value_2_0=ruleIQLMetadataValue();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLMetadataValueMapElementRule());
              					}
              					set(
              						current,
              						"value",
              						lv_value_2_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMetadataValue");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLMetadataValueMapElement"


    // $ANTLR start "entryRuleIQLVariableDeclaration"
    // InternalQDL.g:2806:1: entryRuleIQLVariableDeclaration returns [EObject current=null] : iv_ruleIQLVariableDeclaration= ruleIQLVariableDeclaration EOF ;
    public final EObject entryRuleIQLVariableDeclaration() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLVariableDeclaration = null;


        try {
            // InternalQDL.g:2806:63: (iv_ruleIQLVariableDeclaration= ruleIQLVariableDeclaration EOF )
            // InternalQDL.g:2807:2: iv_ruleIQLVariableDeclaration= ruleIQLVariableDeclaration EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLVariableDeclarationRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLVariableDeclaration=ruleIQLVariableDeclaration();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLVariableDeclaration; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLVariableDeclaration"


    // $ANTLR start "ruleIQLVariableDeclaration"
    // InternalQDL.g:2813:1: ruleIQLVariableDeclaration returns [EObject current=null] : ( () ( (lv_ref_1_0= ruleJvmTypeReference ) ) ( (lv_name_2_0= RULE_ID ) ) ) ;
    public final EObject ruleIQLVariableDeclaration() throws RecognitionException {
        EObject current = null;

        Token lv_name_2_0=null;
        EObject lv_ref_1_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:2819:2: ( ( () ( (lv_ref_1_0= ruleJvmTypeReference ) ) ( (lv_name_2_0= RULE_ID ) ) ) )
            // InternalQDL.g:2820:2: ( () ( (lv_ref_1_0= ruleJvmTypeReference ) ) ( (lv_name_2_0= RULE_ID ) ) )
            {
            // InternalQDL.g:2820:2: ( () ( (lv_ref_1_0= ruleJvmTypeReference ) ) ( (lv_name_2_0= RULE_ID ) ) )
            // InternalQDL.g:2821:3: () ( (lv_ref_1_0= ruleJvmTypeReference ) ) ( (lv_name_2_0= RULE_ID ) )
            {
            // InternalQDL.g:2821:3: ()
            // InternalQDL.g:2822:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLVariableDeclarationAccess().getIQLVariableDeclarationAction_0(),
              					current);
              			
            }

            }

            // InternalQDL.g:2828:3: ( (lv_ref_1_0= ruleJvmTypeReference ) )
            // InternalQDL.g:2829:4: (lv_ref_1_0= ruleJvmTypeReference )
            {
            // InternalQDL.g:2829:4: (lv_ref_1_0= ruleJvmTypeReference )
            // InternalQDL.g:2830:5: lv_ref_1_0= ruleJvmTypeReference
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLVariableDeclarationAccess().getRefJvmTypeReferenceParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_6);
            lv_ref_1_0=ruleJvmTypeReference();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLVariableDeclarationRule());
              					}
              					set(
              						current,
              						"ref",
              						lv_ref_1_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalQDL.g:2847:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalQDL.g:2848:4: (lv_name_2_0= RULE_ID )
            {
            // InternalQDL.g:2848:4: (lv_name_2_0= RULE_ID )
            // InternalQDL.g:2849:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getIQLVariableDeclarationAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLVariableDeclarationRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.eclipse.xtext.common.Terminals.ID");
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLVariableDeclaration"


    // $ANTLR start "entryRuleIQLVariableInitialization"
    // InternalQDL.g:2869:1: entryRuleIQLVariableInitialization returns [EObject current=null] : iv_ruleIQLVariableInitialization= ruleIQLVariableInitialization EOF ;
    public final EObject entryRuleIQLVariableInitialization() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLVariableInitialization = null;


        try {
            // InternalQDL.g:2869:66: (iv_ruleIQLVariableInitialization= ruleIQLVariableInitialization EOF )
            // InternalQDL.g:2870:2: iv_ruleIQLVariableInitialization= ruleIQLVariableInitialization EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLVariableInitializationRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLVariableInitialization=ruleIQLVariableInitialization();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLVariableInitialization; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLVariableInitialization"


    // $ANTLR start "ruleIQLVariableInitialization"
    // InternalQDL.g:2876:1: ruleIQLVariableInitialization returns [EObject current=null] : ( ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) ) | ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) ) | (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) ) ) ;
    public final EObject ruleIQLVariableInitialization() throws RecognitionException {
        EObject current = null;

        Token otherlv_4=null;
        EObject lv_argsList_1_0 = null;

        EObject lv_argsMap_2_0 = null;

        EObject lv_argsMap_3_0 = null;

        EObject lv_value_5_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:2882:2: ( ( ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) ) | ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) ) | (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) ) ) )
            // InternalQDL.g:2883:2: ( ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) ) | ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) ) | (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) ) )
            {
            // InternalQDL.g:2883:2: ( ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) ) | ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) ) | (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) ) )
            int alt44=3;
            switch ( input.LA(1) ) {
            case 14:
                {
                alt44=1;
                }
                break;
            case 59:
                {
                alt44=2;
                }
                break;
            case 42:
                {
                alt44=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 44, 0, input);

                throw nvae;
            }

            switch (alt44) {
                case 1 :
                    // InternalQDL.g:2884:3: ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) )
                    {
                    // InternalQDL.g:2884:3: ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) )
                    // InternalQDL.g:2885:4: () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? )
                    {
                    // InternalQDL.g:2885:4: ()
                    // InternalQDL.g:2886:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLVariableInitializationAccess().getIQLVariableInitializationAction_0_0(),
                      						current);
                      				
                    }

                    }

                    // InternalQDL.g:2892:4: ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? )
                    // InternalQDL.g:2893:5: ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )?
                    {
                    // InternalQDL.g:2893:5: ( (lv_argsList_1_0= ruleIQLArgumentsList ) )
                    // InternalQDL.g:2894:6: (lv_argsList_1_0= ruleIQLArgumentsList )
                    {
                    // InternalQDL.g:2894:6: (lv_argsList_1_0= ruleIQLArgumentsList )
                    // InternalQDL.g:2895:7: lv_argsList_1_0= ruleIQLArgumentsList
                    {
                    if ( state.backtracking==0 ) {

                      							newCompositeNode(grammarAccess.getIQLVariableInitializationAccess().getArgsListIQLArgumentsListParserRuleCall_0_1_0_0());
                      						
                    }
                    pushFollow(FOLLOW_40);
                    lv_argsList_1_0=ruleIQLArgumentsList();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      							if (current==null) {
                      								current = createModelElementForParent(grammarAccess.getIQLVariableInitializationRule());
                      							}
                      							set(
                      								current,
                      								"argsList",
                      								lv_argsList_1_0,
                      								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLArgumentsList");
                      							afterParserOrEnumRuleCall();
                      						
                    }

                    }


                    }

                    // InternalQDL.g:2912:5: ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )?
                    int alt43=2;
                    int LA43_0 = input.LA(1);

                    if ( (LA43_0==59) ) {
                        alt43=1;
                    }
                    switch (alt43) {
                        case 1 :
                            // InternalQDL.g:2913:6: (lv_argsMap_2_0= ruleIQLArgumentsMap )
                            {
                            // InternalQDL.g:2913:6: (lv_argsMap_2_0= ruleIQLArgumentsMap )
                            // InternalQDL.g:2914:7: lv_argsMap_2_0= ruleIQLArgumentsMap
                            {
                            if ( state.backtracking==0 ) {

                              							newCompositeNode(grammarAccess.getIQLVariableInitializationAccess().getArgsMapIQLArgumentsMapParserRuleCall_0_1_1_0());
                              						
                            }
                            pushFollow(FOLLOW_2);
                            lv_argsMap_2_0=ruleIQLArgumentsMap();

                            state._fsp--;
                            if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              							if (current==null) {
                              								current = createModelElementForParent(grammarAccess.getIQLVariableInitializationRule());
                              							}
                              							set(
                              								current,
                              								"argsMap",
                              								lv_argsMap_2_0,
                              								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLArgumentsMap");
                              							afterParserOrEnumRuleCall();
                              						
                            }

                            }


                            }
                            break;

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalQDL.g:2934:3: ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) )
                    {
                    // InternalQDL.g:2934:3: ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) )
                    // InternalQDL.g:2935:4: (lv_argsMap_3_0= ruleIQLArgumentsMap )
                    {
                    // InternalQDL.g:2935:4: (lv_argsMap_3_0= ruleIQLArgumentsMap )
                    // InternalQDL.g:2936:5: lv_argsMap_3_0= ruleIQLArgumentsMap
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getIQLVariableInitializationAccess().getArgsMapIQLArgumentsMapParserRuleCall_1_0());
                      				
                    }
                    pushFollow(FOLLOW_2);
                    lv_argsMap_3_0=ruleIQLArgumentsMap();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getIQLVariableInitializationRule());
                      					}
                      					set(
                      						current,
                      						"argsMap",
                      						lv_argsMap_3_0,
                      						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLArgumentsMap");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalQDL.g:2954:3: (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) )
                    {
                    // InternalQDL.g:2954:3: (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) )
                    // InternalQDL.g:2955:4: otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) )
                    {
                    otherlv_4=(Token)match(input,42,FOLLOW_11); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_4, grammarAccess.getIQLVariableInitializationAccess().getEqualsSignKeyword_2_0());
                      			
                    }
                    // InternalQDL.g:2959:4: ( (lv_value_5_0= ruleIQLExpression ) )
                    // InternalQDL.g:2960:5: (lv_value_5_0= ruleIQLExpression )
                    {
                    // InternalQDL.g:2960:5: (lv_value_5_0= ruleIQLExpression )
                    // InternalQDL.g:2961:6: lv_value_5_0= ruleIQLExpression
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLVariableInitializationAccess().getValueIQLExpressionParserRuleCall_2_1_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_value_5_0=ruleIQLExpression();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLVariableInitializationRule());
                      						}
                      						set(
                      							current,
                      							"value",
                      							lv_value_5_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }


                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLVariableInitialization"


    // $ANTLR start "entryRuleIQLArgumentsList"
    // InternalQDL.g:2983:1: entryRuleIQLArgumentsList returns [EObject current=null] : iv_ruleIQLArgumentsList= ruleIQLArgumentsList EOF ;
    public final EObject entryRuleIQLArgumentsList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLArgumentsList = null;


        try {
            // InternalQDL.g:2983:57: (iv_ruleIQLArgumentsList= ruleIQLArgumentsList EOF )
            // InternalQDL.g:2984:2: iv_ruleIQLArgumentsList= ruleIQLArgumentsList EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLArgumentsListRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLArgumentsList=ruleIQLArgumentsList();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLArgumentsList; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLArgumentsList"


    // $ANTLR start "ruleIQLArgumentsList"
    // InternalQDL.g:2990:1: ruleIQLArgumentsList returns [EObject current=null] : ( () otherlv_1= '(' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ')' ) ;
    public final EObject ruleIQLArgumentsList() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:2996:2: ( ( () otherlv_1= '(' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ')' ) )
            // InternalQDL.g:2997:2: ( () otherlv_1= '(' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ')' )
            {
            // InternalQDL.g:2997:2: ( () otherlv_1= '(' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ')' )
            // InternalQDL.g:2998:3: () otherlv_1= '(' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ')'
            {
            // InternalQDL.g:2998:3: ()
            // InternalQDL.g:2999:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLArgumentsListAccess().getIQLArgumentsListAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,14,FOLLOW_41); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLArgumentsListAccess().getLeftParenthesisKeyword_1());
              		
            }
            // InternalQDL.g:3009:3: ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )?
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( ((LA46_0>=RULE_ID && LA46_0<=RULE_STRING)||LA46_0==RULE_RANGE||LA46_0==14||(LA46_0>=20 && LA46_0<=21)||LA46_0==23||(LA46_0>=31 && LA46_0<=32)||LA46_0==37||LA46_0==57||(LA46_0>=79 && LA46_0<=80)||(LA46_0>=84 && LA46_0<=85)||(LA46_0>=111 && LA46_0<=112)) ) {
                alt46=1;
            }
            switch (alt46) {
                case 1 :
                    // InternalQDL.g:3010:4: ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )*
                    {
                    // InternalQDL.g:3010:4: ( (lv_elements_2_0= ruleIQLExpression ) )
                    // InternalQDL.g:3011:5: (lv_elements_2_0= ruleIQLExpression )
                    {
                    // InternalQDL.g:3011:5: (lv_elements_2_0= ruleIQLExpression )
                    // InternalQDL.g:3012:6: lv_elements_2_0= ruleIQLExpression
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLArgumentsListAccess().getElementsIQLExpressionParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_29);
                    lv_elements_2_0=ruleIQLExpression();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLArgumentsListRule());
                      						}
                      						add(
                      							current,
                      							"elements",
                      							lv_elements_2_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalQDL.g:3029:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )*
                    loop45:
                    do {
                        int alt45=2;
                        int LA45_0 = input.LA(1);

                        if ( (LA45_0==63) ) {
                            alt45=1;
                        }


                        switch (alt45) {
                    	case 1 :
                    	    // InternalQDL.g:3030:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) )
                    	    {
                    	    otherlv_3=(Token)match(input,63,FOLLOW_11); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLArgumentsListAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalQDL.g:3034:5: ( (lv_elements_4_0= ruleIQLExpression ) )
                    	    // InternalQDL.g:3035:6: (lv_elements_4_0= ruleIQLExpression )
                    	    {
                    	    // InternalQDL.g:3035:6: (lv_elements_4_0= ruleIQLExpression )
                    	    // InternalQDL.g:3036:7: lv_elements_4_0= ruleIQLExpression
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLArgumentsListAccess().getElementsIQLExpressionParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_29);
                    	    lv_elements_4_0=ruleIQLExpression();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElementForParent(grammarAccess.getIQLArgumentsListRule());
                    	      							}
                    	      							add(
                    	      								current,
                    	      								"elements",
                    	      								lv_elements_4_0,
                    	      								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop45;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getIQLArgumentsListAccess().getRightParenthesisKeyword_3());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLArgumentsList"


    // $ANTLR start "entryRuleIQLArgumentsMap"
    // InternalQDL.g:3063:1: entryRuleIQLArgumentsMap returns [EObject current=null] : iv_ruleIQLArgumentsMap= ruleIQLArgumentsMap EOF ;
    public final EObject entryRuleIQLArgumentsMap() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLArgumentsMap = null;


        try {
            // InternalQDL.g:3063:56: (iv_ruleIQLArgumentsMap= ruleIQLArgumentsMap EOF )
            // InternalQDL.g:3064:2: iv_ruleIQLArgumentsMap= ruleIQLArgumentsMap EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLArgumentsMapRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLArgumentsMap=ruleIQLArgumentsMap();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLArgumentsMap; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLArgumentsMap"


    // $ANTLR start "ruleIQLArgumentsMap"
    // InternalQDL.g:3070:1: ruleIQLArgumentsMap returns [EObject current=null] : ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )? otherlv_5= '}' ) ;
    public final EObject ruleIQLArgumentsMap() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:3076:2: ( ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )? otherlv_5= '}' ) )
            // InternalQDL.g:3077:2: ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )? otherlv_5= '}' )
            {
            // InternalQDL.g:3077:2: ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )? otherlv_5= '}' )
            // InternalQDL.g:3078:3: () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )? otherlv_5= '}'
            {
            // InternalQDL.g:3078:3: ()
            // InternalQDL.g:3079:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLArgumentsMapAccess().getIQLArgumentsMapAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,59,FOLLOW_42); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLArgumentsMapAccess().getLeftCurlyBracketKeyword_1());
              		
            }
            // InternalQDL.g:3089:3: ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )?
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==RULE_ID) ) {
                alt48=1;
            }
            switch (alt48) {
                case 1 :
                    // InternalQDL.g:3090:4: ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )*
                    {
                    // InternalQDL.g:3090:4: ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) )
                    // InternalQDL.g:3091:5: (lv_elements_2_0= ruleIQLArgumentsMapKeyValue )
                    {
                    // InternalQDL.g:3091:5: (lv_elements_2_0= ruleIQLArgumentsMapKeyValue )
                    // InternalQDL.g:3092:6: lv_elements_2_0= ruleIQLArgumentsMapKeyValue
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLArgumentsMapAccess().getElementsIQLArgumentsMapKeyValueParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_38);
                    lv_elements_2_0=ruleIQLArgumentsMapKeyValue();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLArgumentsMapRule());
                      						}
                      						add(
                      							current,
                      							"elements",
                      							lv_elements_2_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLArgumentsMapKeyValue");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalQDL.g:3109:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )*
                    loop47:
                    do {
                        int alt47=2;
                        int LA47_0 = input.LA(1);

                        if ( (LA47_0==63) ) {
                            alt47=1;
                        }


                        switch (alt47) {
                    	case 1 :
                    	    // InternalQDL.g:3110:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) )
                    	    {
                    	    otherlv_3=(Token)match(input,63,FOLLOW_6); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLArgumentsMapAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalQDL.g:3114:5: ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) )
                    	    // InternalQDL.g:3115:6: (lv_elements_4_0= ruleIQLArgumentsMapKeyValue )
                    	    {
                    	    // InternalQDL.g:3115:6: (lv_elements_4_0= ruleIQLArgumentsMapKeyValue )
                    	    // InternalQDL.g:3116:7: lv_elements_4_0= ruleIQLArgumentsMapKeyValue
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLArgumentsMapAccess().getElementsIQLArgumentsMapKeyValueParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_38);
                    	    lv_elements_4_0=ruleIQLArgumentsMapKeyValue();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElementForParent(grammarAccess.getIQLArgumentsMapRule());
                    	      							}
                    	      							add(
                    	      								current,
                    	      								"elements",
                    	      								lv_elements_4_0,
                    	      								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLArgumentsMapKeyValue");
                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop47;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,60,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getIQLArgumentsMapAccess().getRightCurlyBracketKeyword_3());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLArgumentsMap"


    // $ANTLR start "entryRuleIQLArgumentsMapKeyValue"
    // InternalQDL.g:3143:1: entryRuleIQLArgumentsMapKeyValue returns [EObject current=null] : iv_ruleIQLArgumentsMapKeyValue= ruleIQLArgumentsMapKeyValue EOF ;
    public final EObject entryRuleIQLArgumentsMapKeyValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLArgumentsMapKeyValue = null;


        try {
            // InternalQDL.g:3143:64: (iv_ruleIQLArgumentsMapKeyValue= ruleIQLArgumentsMapKeyValue EOF )
            // InternalQDL.g:3144:2: iv_ruleIQLArgumentsMapKeyValue= ruleIQLArgumentsMapKeyValue EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLArgumentsMapKeyValueRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLArgumentsMapKeyValue=ruleIQLArgumentsMapKeyValue();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLArgumentsMapKeyValue; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLArgumentsMapKeyValue"


    // $ANTLR start "ruleIQLArgumentsMapKeyValue"
    // InternalQDL.g:3150:1: ruleIQLArgumentsMapKeyValue returns [EObject current=null] : ( ( ( ruleQualifiedName ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLExpression ) ) ) ;
    public final EObject ruleIQLArgumentsMapKeyValue() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:3156:2: ( ( ( ( ruleQualifiedName ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLExpression ) ) ) )
            // InternalQDL.g:3157:2: ( ( ( ruleQualifiedName ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLExpression ) ) )
            {
            // InternalQDL.g:3157:2: ( ( ( ruleQualifiedName ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLExpression ) ) )
            // InternalQDL.g:3158:3: ( ( ruleQualifiedName ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLExpression ) )
            {
            // InternalQDL.g:3158:3: ( ( ruleQualifiedName ) )
            // InternalQDL.g:3159:4: ( ruleQualifiedName )
            {
            // InternalQDL.g:3159:4: ( ruleQualifiedName )
            // InternalQDL.g:3160:5: ruleQualifiedName
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLArgumentsMapKeyValueRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLArgumentsMapKeyValueAccess().getKeyJvmIdentifiableElementCrossReference_0_0());
              				
            }
            pushFollow(FOLLOW_39);
            ruleQualifiedName();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_1=(Token)match(input,42,FOLLOW_11); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLArgumentsMapKeyValueAccess().getEqualsSignKeyword_1());
              		
            }
            // InternalQDL.g:3178:3: ( (lv_value_2_0= ruleIQLExpression ) )
            // InternalQDL.g:3179:4: (lv_value_2_0= ruleIQLExpression )
            {
            // InternalQDL.g:3179:4: (lv_value_2_0= ruleIQLExpression )
            // InternalQDL.g:3180:5: lv_value_2_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLArgumentsMapKeyValueAccess().getValueIQLExpressionParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_value_2_0=ruleIQLExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLArgumentsMapKeyValueRule());
              					}
              					set(
              						current,
              						"value",
              						lv_value_2_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLArgumentsMapKeyValue"


    // $ANTLR start "entryRuleIQLStatement"
    // InternalQDL.g:3201:1: entryRuleIQLStatement returns [EObject current=null] : iv_ruleIQLStatement= ruleIQLStatement EOF ;
    public final EObject entryRuleIQLStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLStatement = null;


        try {
            // InternalQDL.g:3201:53: (iv_ruleIQLStatement= ruleIQLStatement EOF )
            // InternalQDL.g:3202:2: iv_ruleIQLStatement= ruleIQLStatement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLStatementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLStatement=ruleIQLStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLStatement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLStatement"


    // $ANTLR start "ruleIQLStatement"
    // InternalQDL.g:3208:1: ruleIQLStatement returns [EObject current=null] : (this_IQLStatementBlock_0= ruleIQLStatementBlock | this_IQLExpressionStatement_1= ruleIQLExpressionStatement | this_IQLIfStatement_2= ruleIQLIfStatement | this_IQLWhileStatement_3= ruleIQLWhileStatement | this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement | this_IQLForStatement_5= ruleIQLForStatement | this_IQLForEachStatement_6= ruleIQLForEachStatement | this_IQLSwitchStatement_7= ruleIQLSwitchStatement | this_IQLVariableStatement_8= ruleIQLVariableStatement | this_IQLBreakStatement_9= ruleIQLBreakStatement | this_IQLContinueStatement_10= ruleIQLContinueStatement | this_IQLReturnStatement_11= ruleIQLReturnStatement | this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement | this_IQLJavaStatement_13= ruleIQLJavaStatement ) ;
    public final EObject ruleIQLStatement() throws RecognitionException {
        EObject current = null;

        EObject this_IQLStatementBlock_0 = null;

        EObject this_IQLExpressionStatement_1 = null;

        EObject this_IQLIfStatement_2 = null;

        EObject this_IQLWhileStatement_3 = null;

        EObject this_IQLDoWhileStatement_4 = null;

        EObject this_IQLForStatement_5 = null;

        EObject this_IQLForEachStatement_6 = null;

        EObject this_IQLSwitchStatement_7 = null;

        EObject this_IQLVariableStatement_8 = null;

        EObject this_IQLBreakStatement_9 = null;

        EObject this_IQLContinueStatement_10 = null;

        EObject this_IQLReturnStatement_11 = null;

        EObject this_IQLConstructorCallStatement_12 = null;

        EObject this_IQLJavaStatement_13 = null;



        	enterRule();

        try {
            // InternalQDL.g:3214:2: ( (this_IQLStatementBlock_0= ruleIQLStatementBlock | this_IQLExpressionStatement_1= ruleIQLExpressionStatement | this_IQLIfStatement_2= ruleIQLIfStatement | this_IQLWhileStatement_3= ruleIQLWhileStatement | this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement | this_IQLForStatement_5= ruleIQLForStatement | this_IQLForEachStatement_6= ruleIQLForEachStatement | this_IQLSwitchStatement_7= ruleIQLSwitchStatement | this_IQLVariableStatement_8= ruleIQLVariableStatement | this_IQLBreakStatement_9= ruleIQLBreakStatement | this_IQLContinueStatement_10= ruleIQLContinueStatement | this_IQLReturnStatement_11= ruleIQLReturnStatement | this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement | this_IQLJavaStatement_13= ruleIQLJavaStatement ) )
            // InternalQDL.g:3215:2: (this_IQLStatementBlock_0= ruleIQLStatementBlock | this_IQLExpressionStatement_1= ruleIQLExpressionStatement | this_IQLIfStatement_2= ruleIQLIfStatement | this_IQLWhileStatement_3= ruleIQLWhileStatement | this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement | this_IQLForStatement_5= ruleIQLForStatement | this_IQLForEachStatement_6= ruleIQLForEachStatement | this_IQLSwitchStatement_7= ruleIQLSwitchStatement | this_IQLVariableStatement_8= ruleIQLVariableStatement | this_IQLBreakStatement_9= ruleIQLBreakStatement | this_IQLContinueStatement_10= ruleIQLContinueStatement | this_IQLReturnStatement_11= ruleIQLReturnStatement | this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement | this_IQLJavaStatement_13= ruleIQLJavaStatement )
            {
            // InternalQDL.g:3215:2: (this_IQLStatementBlock_0= ruleIQLStatementBlock | this_IQLExpressionStatement_1= ruleIQLExpressionStatement | this_IQLIfStatement_2= ruleIQLIfStatement | this_IQLWhileStatement_3= ruleIQLWhileStatement | this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement | this_IQLForStatement_5= ruleIQLForStatement | this_IQLForEachStatement_6= ruleIQLForEachStatement | this_IQLSwitchStatement_7= ruleIQLSwitchStatement | this_IQLVariableStatement_8= ruleIQLVariableStatement | this_IQLBreakStatement_9= ruleIQLBreakStatement | this_IQLContinueStatement_10= ruleIQLContinueStatement | this_IQLReturnStatement_11= ruleIQLReturnStatement | this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement | this_IQLJavaStatement_13= ruleIQLJavaStatement )
            int alt49=14;
            alt49 = dfa49.predict(input);
            switch (alt49) {
                case 1 :
                    // InternalQDL.g:3216:3: this_IQLStatementBlock_0= ruleIQLStatementBlock
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLStatementAccess().getIQLStatementBlockParserRuleCall_0());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLStatementBlock_0=ruleIQLStatementBlock();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLStatementBlock_0;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalQDL.g:3225:3: this_IQLExpressionStatement_1= ruleIQLExpressionStatement
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLStatementAccess().getIQLExpressionStatementParserRuleCall_1());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLExpressionStatement_1=ruleIQLExpressionStatement();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLExpressionStatement_1;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalQDL.g:3234:3: this_IQLIfStatement_2= ruleIQLIfStatement
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLStatementAccess().getIQLIfStatementParserRuleCall_2());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLIfStatement_2=ruleIQLIfStatement();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLIfStatement_2;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 4 :
                    // InternalQDL.g:3243:3: this_IQLWhileStatement_3= ruleIQLWhileStatement
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLStatementAccess().getIQLWhileStatementParserRuleCall_3());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLWhileStatement_3=ruleIQLWhileStatement();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLWhileStatement_3;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 5 :
                    // InternalQDL.g:3252:3: this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLStatementAccess().getIQLDoWhileStatementParserRuleCall_4());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLDoWhileStatement_4=ruleIQLDoWhileStatement();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLDoWhileStatement_4;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 6 :
                    // InternalQDL.g:3261:3: this_IQLForStatement_5= ruleIQLForStatement
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLStatementAccess().getIQLForStatementParserRuleCall_5());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLForStatement_5=ruleIQLForStatement();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLForStatement_5;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 7 :
                    // InternalQDL.g:3270:3: this_IQLForEachStatement_6= ruleIQLForEachStatement
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLStatementAccess().getIQLForEachStatementParserRuleCall_6());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLForEachStatement_6=ruleIQLForEachStatement();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLForEachStatement_6;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 8 :
                    // InternalQDL.g:3279:3: this_IQLSwitchStatement_7= ruleIQLSwitchStatement
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLStatementAccess().getIQLSwitchStatementParserRuleCall_7());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLSwitchStatement_7=ruleIQLSwitchStatement();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLSwitchStatement_7;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 9 :
                    // InternalQDL.g:3288:3: this_IQLVariableStatement_8= ruleIQLVariableStatement
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLStatementAccess().getIQLVariableStatementParserRuleCall_8());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLVariableStatement_8=ruleIQLVariableStatement();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLVariableStatement_8;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 10 :
                    // InternalQDL.g:3297:3: this_IQLBreakStatement_9= ruleIQLBreakStatement
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLStatementAccess().getIQLBreakStatementParserRuleCall_9());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLBreakStatement_9=ruleIQLBreakStatement();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLBreakStatement_9;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 11 :
                    // InternalQDL.g:3306:3: this_IQLContinueStatement_10= ruleIQLContinueStatement
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLStatementAccess().getIQLContinueStatementParserRuleCall_10());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLContinueStatement_10=ruleIQLContinueStatement();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLContinueStatement_10;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 12 :
                    // InternalQDL.g:3315:3: this_IQLReturnStatement_11= ruleIQLReturnStatement
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLStatementAccess().getIQLReturnStatementParserRuleCall_11());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLReturnStatement_11=ruleIQLReturnStatement();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLReturnStatement_11;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 13 :
                    // InternalQDL.g:3324:3: this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLStatementAccess().getIQLConstructorCallStatementParserRuleCall_12());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLConstructorCallStatement_12=ruleIQLConstructorCallStatement();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLConstructorCallStatement_12;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 14 :
                    // InternalQDL.g:3333:3: this_IQLJavaStatement_13= ruleIQLJavaStatement
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLStatementAccess().getIQLJavaStatementParserRuleCall_13());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLJavaStatement_13=ruleIQLJavaStatement();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLJavaStatement_13;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLStatement"


    // $ANTLR start "entryRuleIQLStatementBlock"
    // InternalQDL.g:3345:1: entryRuleIQLStatementBlock returns [EObject current=null] : iv_ruleIQLStatementBlock= ruleIQLStatementBlock EOF ;
    public final EObject entryRuleIQLStatementBlock() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLStatementBlock = null;


        try {
            // InternalQDL.g:3345:58: (iv_ruleIQLStatementBlock= ruleIQLStatementBlock EOF )
            // InternalQDL.g:3346:2: iv_ruleIQLStatementBlock= ruleIQLStatementBlock EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLStatementBlockRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLStatementBlock=ruleIQLStatementBlock();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLStatementBlock; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLStatementBlock"


    // $ANTLR start "ruleIQLStatementBlock"
    // InternalQDL.g:3352:1: ruleIQLStatementBlock returns [EObject current=null] : ( () otherlv_1= '{' ( (lv_statements_2_0= ruleIQLStatement ) )* otherlv_3= '}' ) ;
    public final EObject ruleIQLStatementBlock() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject lv_statements_2_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:3358:2: ( ( () otherlv_1= '{' ( (lv_statements_2_0= ruleIQLStatement ) )* otherlv_3= '}' ) )
            // InternalQDL.g:3359:2: ( () otherlv_1= '{' ( (lv_statements_2_0= ruleIQLStatement ) )* otherlv_3= '}' )
            {
            // InternalQDL.g:3359:2: ( () otherlv_1= '{' ( (lv_statements_2_0= ruleIQLStatement ) )* otherlv_3= '}' )
            // InternalQDL.g:3360:3: () otherlv_1= '{' ( (lv_statements_2_0= ruleIQLStatement ) )* otherlv_3= '}'
            {
            // InternalQDL.g:3360:3: ()
            // InternalQDL.g:3361:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLStatementBlockAccess().getIQLStatementBlockAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,59,FOLLOW_43); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLStatementBlockAccess().getLeftCurlyBracketKeyword_1());
              		
            }
            // InternalQDL.g:3371:3: ( (lv_statements_2_0= ruleIQLStatement ) )*
            loop50:
            do {
                int alt50=2;
                int LA50_0 = input.LA(1);

                if ( ((LA50_0>=RULE_ID && LA50_0<=RULE_STRING)||LA50_0==RULE_RANGE||LA50_0==14||(LA50_0>=20 && LA50_0<=21)||LA50_0==23||(LA50_0>=31 && LA50_0<=32)||LA50_0==37||LA50_0==57||LA50_0==59||LA50_0==71||(LA50_0>=73 && LA50_0<=76)||(LA50_0>=79 && LA50_0<=85)||LA50_0==88||(LA50_0>=111 && LA50_0<=112)) ) {
                    alt50=1;
                }


                switch (alt50) {
            	case 1 :
            	    // InternalQDL.g:3372:4: (lv_statements_2_0= ruleIQLStatement )
            	    {
            	    // InternalQDL.g:3372:4: (lv_statements_2_0= ruleIQLStatement )
            	    // InternalQDL.g:3373:5: lv_statements_2_0= ruleIQLStatement
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLStatementBlockAccess().getStatementsIQLStatementParserRuleCall_2_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_43);
            	    lv_statements_2_0=ruleIQLStatement();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getIQLStatementBlockRule());
            	      					}
            	      					add(
            	      						current,
            	      						"statements",
            	      						lv_statements_2_0,
            	      						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLStatement");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop50;
                }
            } while (true);

            otherlv_3=(Token)match(input,60,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getIQLStatementBlockAccess().getRightCurlyBracketKeyword_3());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLStatementBlock"


    // $ANTLR start "entryRuleIQLJavaStatement"
    // InternalQDL.g:3398:1: entryRuleIQLJavaStatement returns [EObject current=null] : iv_ruleIQLJavaStatement= ruleIQLJavaStatement EOF ;
    public final EObject entryRuleIQLJavaStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLJavaStatement = null;


        try {
            // InternalQDL.g:3398:57: (iv_ruleIQLJavaStatement= ruleIQLJavaStatement EOF )
            // InternalQDL.g:3399:2: iv_ruleIQLJavaStatement= ruleIQLJavaStatement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLJavaStatementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLJavaStatement=ruleIQLJavaStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLJavaStatement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLJavaStatement"


    // $ANTLR start "ruleIQLJavaStatement"
    // InternalQDL.g:3405:1: ruleIQLJavaStatement returns [EObject current=null] : ( () ( (lv_java_1_0= ruleIQLJava ) ) ) ;
    public final EObject ruleIQLJavaStatement() throws RecognitionException {
        EObject current = null;

        EObject lv_java_1_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:3411:2: ( ( () ( (lv_java_1_0= ruleIQLJava ) ) ) )
            // InternalQDL.g:3412:2: ( () ( (lv_java_1_0= ruleIQLJava ) ) )
            {
            // InternalQDL.g:3412:2: ( () ( (lv_java_1_0= ruleIQLJava ) ) )
            // InternalQDL.g:3413:3: () ( (lv_java_1_0= ruleIQLJava ) )
            {
            // InternalQDL.g:3413:3: ()
            // InternalQDL.g:3414:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLJavaStatementAccess().getIQLJavaStatementAction_0(),
              					current);
              			
            }

            }

            // InternalQDL.g:3420:3: ( (lv_java_1_0= ruleIQLJava ) )
            // InternalQDL.g:3421:4: (lv_java_1_0= ruleIQLJava )
            {
            // InternalQDL.g:3421:4: (lv_java_1_0= ruleIQLJava )
            // InternalQDL.g:3422:5: lv_java_1_0= ruleIQLJava
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLJavaStatementAccess().getJavaIQLJavaParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_java_1_0=ruleIQLJava();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLJavaStatementRule());
              					}
              					set(
              						current,
              						"java",
              						lv_java_1_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLJava");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLJavaStatement"


    // $ANTLR start "entryRuleIQLIfStatement"
    // InternalQDL.g:3443:1: entryRuleIQLIfStatement returns [EObject current=null] : iv_ruleIQLIfStatement= ruleIQLIfStatement EOF ;
    public final EObject entryRuleIQLIfStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLIfStatement = null;


        try {
            // InternalQDL.g:3443:55: (iv_ruleIQLIfStatement= ruleIQLIfStatement EOF )
            // InternalQDL.g:3444:2: iv_ruleIQLIfStatement= ruleIQLIfStatement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLIfStatementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLIfStatement=ruleIQLIfStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLIfStatement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLIfStatement"


    // $ANTLR start "ruleIQLIfStatement"
    // InternalQDL.g:3450:1: ruleIQLIfStatement returns [EObject current=null] : ( () otherlv_1= 'if' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_thenBody_5_0= ruleIQLStatement ) ) ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )? ) ;
    public final EObject ruleIQLIfStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        EObject lv_predicate_3_0 = null;

        EObject lv_thenBody_5_0 = null;

        EObject lv_elseBody_7_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:3456:2: ( ( () otherlv_1= 'if' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_thenBody_5_0= ruleIQLStatement ) ) ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )? ) )
            // InternalQDL.g:3457:2: ( () otherlv_1= 'if' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_thenBody_5_0= ruleIQLStatement ) ) ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )? )
            {
            // InternalQDL.g:3457:2: ( () otherlv_1= 'if' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_thenBody_5_0= ruleIQLStatement ) ) ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )? )
            // InternalQDL.g:3458:3: () otherlv_1= 'if' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_thenBody_5_0= ruleIQLStatement ) ) ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )?
            {
            // InternalQDL.g:3458:3: ()
            // InternalQDL.g:3459:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLIfStatementAccess().getIQLIfStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,71,FOLLOW_30); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLIfStatementAccess().getIfKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,14,FOLLOW_11); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLIfStatementAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalQDL.g:3473:3: ( (lv_predicate_3_0= ruleIQLExpression ) )
            // InternalQDL.g:3474:4: (lv_predicate_3_0= ruleIQLExpression )
            {
            // InternalQDL.g:3474:4: (lv_predicate_3_0= ruleIQLExpression )
            // InternalQDL.g:3475:5: lv_predicate_3_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLIfStatementAccess().getPredicateIQLExpressionParserRuleCall_3_0());
              				
            }
            pushFollow(FOLLOW_9);
            lv_predicate_3_0=ruleIQLExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLIfStatementRule());
              					}
              					set(
              						current,
              						"predicate",
              						lv_predicate_3_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_4=(Token)match(input,15,FOLLOW_44); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLIfStatementAccess().getRightParenthesisKeyword_4());
              		
            }
            // InternalQDL.g:3496:3: ( (lv_thenBody_5_0= ruleIQLStatement ) )
            // InternalQDL.g:3497:4: (lv_thenBody_5_0= ruleIQLStatement )
            {
            // InternalQDL.g:3497:4: (lv_thenBody_5_0= ruleIQLStatement )
            // InternalQDL.g:3498:5: lv_thenBody_5_0= ruleIQLStatement
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLIfStatementAccess().getThenBodyIQLStatementParserRuleCall_5_0());
              				
            }
            pushFollow(FOLLOW_45);
            lv_thenBody_5_0=ruleIQLStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLIfStatementRule());
              					}
              					set(
              						current,
              						"thenBody",
              						lv_thenBody_5_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLStatement");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalQDL.g:3515:3: ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )?
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==72) ) {
                int LA51_1 = input.LA(2);

                if ( (synpred6_InternalQDL()) ) {
                    alt51=1;
                }
            }
            switch (alt51) {
                case 1 :
                    // InternalQDL.g:3516:4: ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) )
                    {
                    // InternalQDL.g:3516:4: ( ( 'else' )=>otherlv_6= 'else' )
                    // InternalQDL.g:3517:5: ( 'else' )=>otherlv_6= 'else'
                    {
                    otherlv_6=(Token)match(input,72,FOLLOW_44); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(otherlv_6, grammarAccess.getIQLIfStatementAccess().getElseKeyword_6_0());
                      				
                    }

                    }

                    // InternalQDL.g:3523:4: ( (lv_elseBody_7_0= ruleIQLStatement ) )
                    // InternalQDL.g:3524:5: (lv_elseBody_7_0= ruleIQLStatement )
                    {
                    // InternalQDL.g:3524:5: (lv_elseBody_7_0= ruleIQLStatement )
                    // InternalQDL.g:3525:6: lv_elseBody_7_0= ruleIQLStatement
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLIfStatementAccess().getElseBodyIQLStatementParserRuleCall_6_1_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_elseBody_7_0=ruleIQLStatement();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLIfStatementRule());
                      						}
                      						set(
                      							current,
                      							"elseBody",
                      							lv_elseBody_7_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLStatement");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLIfStatement"


    // $ANTLR start "entryRuleIQLWhileStatement"
    // InternalQDL.g:3547:1: entryRuleIQLWhileStatement returns [EObject current=null] : iv_ruleIQLWhileStatement= ruleIQLWhileStatement EOF ;
    public final EObject entryRuleIQLWhileStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLWhileStatement = null;


        try {
            // InternalQDL.g:3547:58: (iv_ruleIQLWhileStatement= ruleIQLWhileStatement EOF )
            // InternalQDL.g:3548:2: iv_ruleIQLWhileStatement= ruleIQLWhileStatement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLWhileStatementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLWhileStatement=ruleIQLWhileStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLWhileStatement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLWhileStatement"


    // $ANTLR start "ruleIQLWhileStatement"
    // InternalQDL.g:3554:1: ruleIQLWhileStatement returns [EObject current=null] : ( () otherlv_1= 'while' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_body_5_0= ruleIQLStatement ) ) ) ;
    public final EObject ruleIQLWhileStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_predicate_3_0 = null;

        EObject lv_body_5_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:3560:2: ( ( () otherlv_1= 'while' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_body_5_0= ruleIQLStatement ) ) ) )
            // InternalQDL.g:3561:2: ( () otherlv_1= 'while' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_body_5_0= ruleIQLStatement ) ) )
            {
            // InternalQDL.g:3561:2: ( () otherlv_1= 'while' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_body_5_0= ruleIQLStatement ) ) )
            // InternalQDL.g:3562:3: () otherlv_1= 'while' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_body_5_0= ruleIQLStatement ) )
            {
            // InternalQDL.g:3562:3: ()
            // InternalQDL.g:3563:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLWhileStatementAccess().getIQLWhileStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,73,FOLLOW_30); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLWhileStatementAccess().getWhileKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,14,FOLLOW_11); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLWhileStatementAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalQDL.g:3577:3: ( (lv_predicate_3_0= ruleIQLExpression ) )
            // InternalQDL.g:3578:4: (lv_predicate_3_0= ruleIQLExpression )
            {
            // InternalQDL.g:3578:4: (lv_predicate_3_0= ruleIQLExpression )
            // InternalQDL.g:3579:5: lv_predicate_3_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLWhileStatementAccess().getPredicateIQLExpressionParserRuleCall_3_0());
              				
            }
            pushFollow(FOLLOW_9);
            lv_predicate_3_0=ruleIQLExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLWhileStatementRule());
              					}
              					set(
              						current,
              						"predicate",
              						lv_predicate_3_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_4=(Token)match(input,15,FOLLOW_44); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLWhileStatementAccess().getRightParenthesisKeyword_4());
              		
            }
            // InternalQDL.g:3600:3: ( (lv_body_5_0= ruleIQLStatement ) )
            // InternalQDL.g:3601:4: (lv_body_5_0= ruleIQLStatement )
            {
            // InternalQDL.g:3601:4: (lv_body_5_0= ruleIQLStatement )
            // InternalQDL.g:3602:5: lv_body_5_0= ruleIQLStatement
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLWhileStatementAccess().getBodyIQLStatementParserRuleCall_5_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_body_5_0=ruleIQLStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLWhileStatementRule());
              					}
              					set(
              						current,
              						"body",
              						lv_body_5_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLStatement");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLWhileStatement"


    // $ANTLR start "entryRuleIQLDoWhileStatement"
    // InternalQDL.g:3623:1: entryRuleIQLDoWhileStatement returns [EObject current=null] : iv_ruleIQLDoWhileStatement= ruleIQLDoWhileStatement EOF ;
    public final EObject entryRuleIQLDoWhileStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLDoWhileStatement = null;


        try {
            // InternalQDL.g:3623:60: (iv_ruleIQLDoWhileStatement= ruleIQLDoWhileStatement EOF )
            // InternalQDL.g:3624:2: iv_ruleIQLDoWhileStatement= ruleIQLDoWhileStatement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLDoWhileStatementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLDoWhileStatement=ruleIQLDoWhileStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLDoWhileStatement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLDoWhileStatement"


    // $ANTLR start "ruleIQLDoWhileStatement"
    // InternalQDL.g:3630:1: ruleIQLDoWhileStatement returns [EObject current=null] : ( () otherlv_1= 'do' ( (lv_body_2_0= ruleIQLStatement ) ) otherlv_3= 'while' otherlv_4= '(' ( (lv_predicate_5_0= ruleIQLExpression ) ) otherlv_6= ')' otherlv_7= ';' ) ;
    public final EObject ruleIQLDoWhileStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        EObject lv_body_2_0 = null;

        EObject lv_predicate_5_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:3636:2: ( ( () otherlv_1= 'do' ( (lv_body_2_0= ruleIQLStatement ) ) otherlv_3= 'while' otherlv_4= '(' ( (lv_predicate_5_0= ruleIQLExpression ) ) otherlv_6= ')' otherlv_7= ';' ) )
            // InternalQDL.g:3637:2: ( () otherlv_1= 'do' ( (lv_body_2_0= ruleIQLStatement ) ) otherlv_3= 'while' otherlv_4= '(' ( (lv_predicate_5_0= ruleIQLExpression ) ) otherlv_6= ')' otherlv_7= ';' )
            {
            // InternalQDL.g:3637:2: ( () otherlv_1= 'do' ( (lv_body_2_0= ruleIQLStatement ) ) otherlv_3= 'while' otherlv_4= '(' ( (lv_predicate_5_0= ruleIQLExpression ) ) otherlv_6= ')' otherlv_7= ';' )
            // InternalQDL.g:3638:3: () otherlv_1= 'do' ( (lv_body_2_0= ruleIQLStatement ) ) otherlv_3= 'while' otherlv_4= '(' ( (lv_predicate_5_0= ruleIQLExpression ) ) otherlv_6= ')' otherlv_7= ';'
            {
            // InternalQDL.g:3638:3: ()
            // InternalQDL.g:3639:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLDoWhileStatementAccess().getIQLDoWhileStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,74,FOLLOW_44); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLDoWhileStatementAccess().getDoKeyword_1());
              		
            }
            // InternalQDL.g:3649:3: ( (lv_body_2_0= ruleIQLStatement ) )
            // InternalQDL.g:3650:4: (lv_body_2_0= ruleIQLStatement )
            {
            // InternalQDL.g:3650:4: (lv_body_2_0= ruleIQLStatement )
            // InternalQDL.g:3651:5: lv_body_2_0= ruleIQLStatement
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLDoWhileStatementAccess().getBodyIQLStatementParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_46);
            lv_body_2_0=ruleIQLStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLDoWhileStatementRule());
              					}
              					set(
              						current,
              						"body",
              						lv_body_2_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLStatement");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_3=(Token)match(input,73,FOLLOW_30); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getIQLDoWhileStatementAccess().getWhileKeyword_3());
              		
            }
            otherlv_4=(Token)match(input,14,FOLLOW_11); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLDoWhileStatementAccess().getLeftParenthesisKeyword_4());
              		
            }
            // InternalQDL.g:3676:3: ( (lv_predicate_5_0= ruleIQLExpression ) )
            // InternalQDL.g:3677:4: (lv_predicate_5_0= ruleIQLExpression )
            {
            // InternalQDL.g:3677:4: (lv_predicate_5_0= ruleIQLExpression )
            // InternalQDL.g:3678:5: lv_predicate_5_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLDoWhileStatementAccess().getPredicateIQLExpressionParserRuleCall_5_0());
              				
            }
            pushFollow(FOLLOW_9);
            lv_predicate_5_0=ruleIQLExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLDoWhileStatementRule());
              					}
              					set(
              						current,
              						"predicate",
              						lv_predicate_5_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_6=(Token)match(input,15,FOLLOW_17); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_6, grammarAccess.getIQLDoWhileStatementAccess().getRightParenthesisKeyword_6());
              		
            }
            otherlv_7=(Token)match(input,62,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_7, grammarAccess.getIQLDoWhileStatementAccess().getSemicolonKeyword_7());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLDoWhileStatement"


    // $ANTLR start "entryRuleIQLForStatement"
    // InternalQDL.g:3707:1: entryRuleIQLForStatement returns [EObject current=null] : iv_ruleIQLForStatement= ruleIQLForStatement EOF ;
    public final EObject entryRuleIQLForStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLForStatement = null;


        try {
            // InternalQDL.g:3707:56: (iv_ruleIQLForStatement= ruleIQLForStatement EOF )
            // InternalQDL.g:3708:2: iv_ruleIQLForStatement= ruleIQLForStatement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLForStatementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLForStatement=ruleIQLForStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLForStatement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLForStatement"


    // $ANTLR start "ruleIQLForStatement"
    // InternalQDL.g:3714:1: ruleIQLForStatement returns [EObject current=null] : ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) otherlv_6= ';' ( (lv_predicate_7_0= ruleIQLExpression ) ) otherlv_8= ';' ( (lv_updateExpr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ( (lv_body_11_0= ruleIQLStatement ) ) ) ;
    public final EObject ruleIQLForStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        Token otherlv_10=null;
        EObject lv_var_3_0 = null;

        EObject lv_value_5_0 = null;

        EObject lv_predicate_7_0 = null;

        EObject lv_updateExpr_9_0 = null;

        EObject lv_body_11_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:3720:2: ( ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) otherlv_6= ';' ( (lv_predicate_7_0= ruleIQLExpression ) ) otherlv_8= ';' ( (lv_updateExpr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ( (lv_body_11_0= ruleIQLStatement ) ) ) )
            // InternalQDL.g:3721:2: ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) otherlv_6= ';' ( (lv_predicate_7_0= ruleIQLExpression ) ) otherlv_8= ';' ( (lv_updateExpr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ( (lv_body_11_0= ruleIQLStatement ) ) )
            {
            // InternalQDL.g:3721:2: ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) otherlv_6= ';' ( (lv_predicate_7_0= ruleIQLExpression ) ) otherlv_8= ';' ( (lv_updateExpr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ( (lv_body_11_0= ruleIQLStatement ) ) )
            // InternalQDL.g:3722:3: () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) otherlv_6= ';' ( (lv_predicate_7_0= ruleIQLExpression ) ) otherlv_8= ';' ( (lv_updateExpr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ( (lv_body_11_0= ruleIQLStatement ) )
            {
            // InternalQDL.g:3722:3: ()
            // InternalQDL.g:3723:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLForStatementAccess().getIQLForStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,75,FOLLOW_30); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLForStatementAccess().getForKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,14,FOLLOW_6); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLForStatementAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalQDL.g:3737:3: ( (lv_var_3_0= ruleIQLVariableDeclaration ) )
            // InternalQDL.g:3738:4: (lv_var_3_0= ruleIQLVariableDeclaration )
            {
            // InternalQDL.g:3738:4: (lv_var_3_0= ruleIQLVariableDeclaration )
            // InternalQDL.g:3739:5: lv_var_3_0= ruleIQLVariableDeclaration
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForStatementAccess().getVarIQLVariableDeclarationParserRuleCall_3_0());
              				
            }
            pushFollow(FOLLOW_39);
            lv_var_3_0=ruleIQLVariableDeclaration();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLForStatementRule());
              					}
              					set(
              						current,
              						"var",
              						lv_var_3_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLVariableDeclaration");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_4=(Token)match(input,42,FOLLOW_11); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLForStatementAccess().getEqualsSignKeyword_4());
              		
            }
            // InternalQDL.g:3760:3: ( (lv_value_5_0= ruleIQLExpression ) )
            // InternalQDL.g:3761:4: (lv_value_5_0= ruleIQLExpression )
            {
            // InternalQDL.g:3761:4: (lv_value_5_0= ruleIQLExpression )
            // InternalQDL.g:3762:5: lv_value_5_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForStatementAccess().getValueIQLExpressionParserRuleCall_5_0());
              				
            }
            pushFollow(FOLLOW_17);
            lv_value_5_0=ruleIQLExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLForStatementRule());
              					}
              					set(
              						current,
              						"value",
              						lv_value_5_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_6=(Token)match(input,62,FOLLOW_11); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_6, grammarAccess.getIQLForStatementAccess().getSemicolonKeyword_6());
              		
            }
            // InternalQDL.g:3783:3: ( (lv_predicate_7_0= ruleIQLExpression ) )
            // InternalQDL.g:3784:4: (lv_predicate_7_0= ruleIQLExpression )
            {
            // InternalQDL.g:3784:4: (lv_predicate_7_0= ruleIQLExpression )
            // InternalQDL.g:3785:5: lv_predicate_7_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForStatementAccess().getPredicateIQLExpressionParserRuleCall_7_0());
              				
            }
            pushFollow(FOLLOW_17);
            lv_predicate_7_0=ruleIQLExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLForStatementRule());
              					}
              					set(
              						current,
              						"predicate",
              						lv_predicate_7_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_8=(Token)match(input,62,FOLLOW_11); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_8, grammarAccess.getIQLForStatementAccess().getSemicolonKeyword_8());
              		
            }
            // InternalQDL.g:3806:3: ( (lv_updateExpr_9_0= ruleIQLExpression ) )
            // InternalQDL.g:3807:4: (lv_updateExpr_9_0= ruleIQLExpression )
            {
            // InternalQDL.g:3807:4: (lv_updateExpr_9_0= ruleIQLExpression )
            // InternalQDL.g:3808:5: lv_updateExpr_9_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForStatementAccess().getUpdateExprIQLExpressionParserRuleCall_9_0());
              				
            }
            pushFollow(FOLLOW_9);
            lv_updateExpr_9_0=ruleIQLExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLForStatementRule());
              					}
              					set(
              						current,
              						"updateExpr",
              						lv_updateExpr_9_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_10=(Token)match(input,15,FOLLOW_44); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_10, grammarAccess.getIQLForStatementAccess().getRightParenthesisKeyword_10());
              		
            }
            // InternalQDL.g:3829:3: ( (lv_body_11_0= ruleIQLStatement ) )
            // InternalQDL.g:3830:4: (lv_body_11_0= ruleIQLStatement )
            {
            // InternalQDL.g:3830:4: (lv_body_11_0= ruleIQLStatement )
            // InternalQDL.g:3831:5: lv_body_11_0= ruleIQLStatement
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForStatementAccess().getBodyIQLStatementParserRuleCall_11_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_body_11_0=ruleIQLStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLForStatementRule());
              					}
              					set(
              						current,
              						"body",
              						lv_body_11_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLStatement");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLForStatement"


    // $ANTLR start "entryRuleIQLForEachStatement"
    // InternalQDL.g:3852:1: entryRuleIQLForEachStatement returns [EObject current=null] : iv_ruleIQLForEachStatement= ruleIQLForEachStatement EOF ;
    public final EObject entryRuleIQLForEachStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLForEachStatement = null;


        try {
            // InternalQDL.g:3852:60: (iv_ruleIQLForEachStatement= ruleIQLForEachStatement EOF )
            // InternalQDL.g:3853:2: iv_ruleIQLForEachStatement= ruleIQLForEachStatement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLForEachStatementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLForEachStatement=ruleIQLForEachStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLForEachStatement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLForEachStatement"


    // $ANTLR start "ruleIQLForEachStatement"
    // InternalQDL.g:3859:1: ruleIQLForEachStatement returns [EObject current=null] : ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= ':' ( (lv_forExpression_5_0= ruleIQLExpression ) ) otherlv_6= ')' ( (lv_body_7_0= ruleIQLStatement ) ) ) ;
    public final EObject ruleIQLForEachStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        EObject lv_var_3_0 = null;

        EObject lv_forExpression_5_0 = null;

        EObject lv_body_7_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:3865:2: ( ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= ':' ( (lv_forExpression_5_0= ruleIQLExpression ) ) otherlv_6= ')' ( (lv_body_7_0= ruleIQLStatement ) ) ) )
            // InternalQDL.g:3866:2: ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= ':' ( (lv_forExpression_5_0= ruleIQLExpression ) ) otherlv_6= ')' ( (lv_body_7_0= ruleIQLStatement ) ) )
            {
            // InternalQDL.g:3866:2: ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= ':' ( (lv_forExpression_5_0= ruleIQLExpression ) ) otherlv_6= ')' ( (lv_body_7_0= ruleIQLStatement ) ) )
            // InternalQDL.g:3867:3: () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= ':' ( (lv_forExpression_5_0= ruleIQLExpression ) ) otherlv_6= ')' ( (lv_body_7_0= ruleIQLStatement ) )
            {
            // InternalQDL.g:3867:3: ()
            // InternalQDL.g:3868:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLForEachStatementAccess().getIQLForEachStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,75,FOLLOW_30); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLForEachStatementAccess().getForKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,14,FOLLOW_6); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLForEachStatementAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalQDL.g:3882:3: ( (lv_var_3_0= ruleIQLVariableDeclaration ) )
            // InternalQDL.g:3883:4: (lv_var_3_0= ruleIQLVariableDeclaration )
            {
            // InternalQDL.g:3883:4: (lv_var_3_0= ruleIQLVariableDeclaration )
            // InternalQDL.g:3884:5: lv_var_3_0= ruleIQLVariableDeclaration
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForEachStatementAccess().getVarIQLVariableDeclarationParserRuleCall_3_0());
              				
            }
            pushFollow(FOLLOW_47);
            lv_var_3_0=ruleIQLVariableDeclaration();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLForEachStatementRule());
              					}
              					set(
              						current,
              						"var",
              						lv_var_3_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLVariableDeclaration");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_4=(Token)match(input,17,FOLLOW_11); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLForEachStatementAccess().getColonKeyword_4());
              		
            }
            // InternalQDL.g:3905:3: ( (lv_forExpression_5_0= ruleIQLExpression ) )
            // InternalQDL.g:3906:4: (lv_forExpression_5_0= ruleIQLExpression )
            {
            // InternalQDL.g:3906:4: (lv_forExpression_5_0= ruleIQLExpression )
            // InternalQDL.g:3907:5: lv_forExpression_5_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForEachStatementAccess().getForExpressionIQLExpressionParserRuleCall_5_0());
              				
            }
            pushFollow(FOLLOW_9);
            lv_forExpression_5_0=ruleIQLExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLForEachStatementRule());
              					}
              					set(
              						current,
              						"forExpression",
              						lv_forExpression_5_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_6=(Token)match(input,15,FOLLOW_44); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_6, grammarAccess.getIQLForEachStatementAccess().getRightParenthesisKeyword_6());
              		
            }
            // InternalQDL.g:3928:3: ( (lv_body_7_0= ruleIQLStatement ) )
            // InternalQDL.g:3929:4: (lv_body_7_0= ruleIQLStatement )
            {
            // InternalQDL.g:3929:4: (lv_body_7_0= ruleIQLStatement )
            // InternalQDL.g:3930:5: lv_body_7_0= ruleIQLStatement
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForEachStatementAccess().getBodyIQLStatementParserRuleCall_7_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_body_7_0=ruleIQLStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLForEachStatementRule());
              					}
              					set(
              						current,
              						"body",
              						lv_body_7_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLStatement");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLForEachStatement"


    // $ANTLR start "entryRuleIQLSwitchStatement"
    // InternalQDL.g:3951:1: entryRuleIQLSwitchStatement returns [EObject current=null] : iv_ruleIQLSwitchStatement= ruleIQLSwitchStatement EOF ;
    public final EObject entryRuleIQLSwitchStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLSwitchStatement = null;


        try {
            // InternalQDL.g:3951:59: (iv_ruleIQLSwitchStatement= ruleIQLSwitchStatement EOF )
            // InternalQDL.g:3952:2: iv_ruleIQLSwitchStatement= ruleIQLSwitchStatement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLSwitchStatementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLSwitchStatement=ruleIQLSwitchStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLSwitchStatement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLSwitchStatement"


    // $ANTLR start "ruleIQLSwitchStatement"
    // InternalQDL.g:3958:1: ruleIQLSwitchStatement returns [EObject current=null] : ( () otherlv_1= 'switch' otherlv_2= '(' ( (lv_expr_3_0= ruleIQLExpression ) ) otherlv_4= ')' otherlv_5= '{' ( (lv_cases_6_0= ruleIQLCasePart ) )* (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )? otherlv_10= '}' ) ;
    public final EObject ruleIQLSwitchStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        Token otherlv_8=null;
        Token otherlv_10=null;
        EObject lv_expr_3_0 = null;

        EObject lv_cases_6_0 = null;

        EObject lv_statements_9_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:3964:2: ( ( () otherlv_1= 'switch' otherlv_2= '(' ( (lv_expr_3_0= ruleIQLExpression ) ) otherlv_4= ')' otherlv_5= '{' ( (lv_cases_6_0= ruleIQLCasePart ) )* (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )? otherlv_10= '}' ) )
            // InternalQDL.g:3965:2: ( () otherlv_1= 'switch' otherlv_2= '(' ( (lv_expr_3_0= ruleIQLExpression ) ) otherlv_4= ')' otherlv_5= '{' ( (lv_cases_6_0= ruleIQLCasePart ) )* (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )? otherlv_10= '}' )
            {
            // InternalQDL.g:3965:2: ( () otherlv_1= 'switch' otherlv_2= '(' ( (lv_expr_3_0= ruleIQLExpression ) ) otherlv_4= ')' otherlv_5= '{' ( (lv_cases_6_0= ruleIQLCasePart ) )* (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )? otherlv_10= '}' )
            // InternalQDL.g:3966:3: () otherlv_1= 'switch' otherlv_2= '(' ( (lv_expr_3_0= ruleIQLExpression ) ) otherlv_4= ')' otherlv_5= '{' ( (lv_cases_6_0= ruleIQLCasePart ) )* (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )? otherlv_10= '}'
            {
            // InternalQDL.g:3966:3: ()
            // InternalQDL.g:3967:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLSwitchStatementAccess().getIQLSwitchStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,76,FOLLOW_30); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLSwitchStatementAccess().getSwitchKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,14,FOLLOW_11); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLSwitchStatementAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalQDL.g:3981:3: ( (lv_expr_3_0= ruleIQLExpression ) )
            // InternalQDL.g:3982:4: (lv_expr_3_0= ruleIQLExpression )
            {
            // InternalQDL.g:3982:4: (lv_expr_3_0= ruleIQLExpression )
            // InternalQDL.g:3983:5: lv_expr_3_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLSwitchStatementAccess().getExprIQLExpressionParserRuleCall_3_0());
              				
            }
            pushFollow(FOLLOW_9);
            lv_expr_3_0=ruleIQLExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLSwitchStatementRule());
              					}
              					set(
              						current,
              						"expr",
              						lv_expr_3_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_4=(Token)match(input,15,FOLLOW_23); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLSwitchStatementAccess().getRightParenthesisKeyword_4());
              		
            }
            otherlv_5=(Token)match(input,59,FOLLOW_48); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getIQLSwitchStatementAccess().getLeftCurlyBracketKeyword_5());
              		
            }
            // InternalQDL.g:4008:3: ( (lv_cases_6_0= ruleIQLCasePart ) )*
            loop52:
            do {
                int alt52=2;
                int LA52_0 = input.LA(1);

                if ( (LA52_0==78) ) {
                    alt52=1;
                }


                switch (alt52) {
            	case 1 :
            	    // InternalQDL.g:4009:4: (lv_cases_6_0= ruleIQLCasePart )
            	    {
            	    // InternalQDL.g:4009:4: (lv_cases_6_0= ruleIQLCasePart )
            	    // InternalQDL.g:4010:5: lv_cases_6_0= ruleIQLCasePart
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLSwitchStatementAccess().getCasesIQLCasePartParserRuleCall_6_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_48);
            	    lv_cases_6_0=ruleIQLCasePart();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getIQLSwitchStatementRule());
            	      					}
            	      					add(
            	      						current,
            	      						"cases",
            	      						lv_cases_6_0,
            	      						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLCasePart");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop52;
                }
            } while (true);

            // InternalQDL.g:4027:3: (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )?
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==77) ) {
                alt54=1;
            }
            switch (alt54) {
                case 1 :
                    // InternalQDL.g:4028:4: otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )*
                    {
                    otherlv_7=(Token)match(input,77,FOLLOW_47); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_7, grammarAccess.getIQLSwitchStatementAccess().getDefaultKeyword_7_0());
                      			
                    }
                    otherlv_8=(Token)match(input,17,FOLLOW_43); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_8, grammarAccess.getIQLSwitchStatementAccess().getColonKeyword_7_1());
                      			
                    }
                    // InternalQDL.g:4036:4: ( (lv_statements_9_0= ruleIQLStatement ) )*
                    loop53:
                    do {
                        int alt53=2;
                        int LA53_0 = input.LA(1);

                        if ( ((LA53_0>=RULE_ID && LA53_0<=RULE_STRING)||LA53_0==RULE_RANGE||LA53_0==14||(LA53_0>=20 && LA53_0<=21)||LA53_0==23||(LA53_0>=31 && LA53_0<=32)||LA53_0==37||LA53_0==57||LA53_0==59||LA53_0==71||(LA53_0>=73 && LA53_0<=76)||(LA53_0>=79 && LA53_0<=85)||LA53_0==88||(LA53_0>=111 && LA53_0<=112)) ) {
                            alt53=1;
                        }


                        switch (alt53) {
                    	case 1 :
                    	    // InternalQDL.g:4037:5: (lv_statements_9_0= ruleIQLStatement )
                    	    {
                    	    // InternalQDL.g:4037:5: (lv_statements_9_0= ruleIQLStatement )
                    	    // InternalQDL.g:4038:6: lv_statements_9_0= ruleIQLStatement
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      						newCompositeNode(grammarAccess.getIQLSwitchStatementAccess().getStatementsIQLStatementParserRuleCall_7_2_0());
                    	      					
                    	    }
                    	    pushFollow(FOLLOW_43);
                    	    lv_statements_9_0=ruleIQLStatement();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      						if (current==null) {
                    	      							current = createModelElementForParent(grammarAccess.getIQLSwitchStatementRule());
                    	      						}
                    	      						add(
                    	      							current,
                    	      							"statements",
                    	      							lv_statements_9_0,
                    	      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLStatement");
                    	      						afterParserOrEnumRuleCall();
                    	      					
                    	    }

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop53;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_10=(Token)match(input,60,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_10, grammarAccess.getIQLSwitchStatementAccess().getRightCurlyBracketKeyword_8());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLSwitchStatement"


    // $ANTLR start "entryRuleIQLCasePart"
    // InternalQDL.g:4064:1: entryRuleIQLCasePart returns [EObject current=null] : iv_ruleIQLCasePart= ruleIQLCasePart EOF ;
    public final EObject entryRuleIQLCasePart() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLCasePart = null;


        try {
            // InternalQDL.g:4064:52: (iv_ruleIQLCasePart= ruleIQLCasePart EOF )
            // InternalQDL.g:4065:2: iv_ruleIQLCasePart= ruleIQLCasePart EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLCasePartRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLCasePart=ruleIQLCasePart();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLCasePart; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLCasePart"


    // $ANTLR start "ruleIQLCasePart"
    // InternalQDL.g:4071:1: ruleIQLCasePart returns [EObject current=null] : ( () otherlv_1= 'case' ( (lv_expr_2_0= ruleIQLLiteralExpression ) ) otherlv_3= ':' ( (lv_statements_4_0= ruleIQLStatement ) )* ) ;
    public final EObject ruleIQLCasePart() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject lv_expr_2_0 = null;

        EObject lv_statements_4_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:4077:2: ( ( () otherlv_1= 'case' ( (lv_expr_2_0= ruleIQLLiteralExpression ) ) otherlv_3= ':' ( (lv_statements_4_0= ruleIQLStatement ) )* ) )
            // InternalQDL.g:4078:2: ( () otherlv_1= 'case' ( (lv_expr_2_0= ruleIQLLiteralExpression ) ) otherlv_3= ':' ( (lv_statements_4_0= ruleIQLStatement ) )* )
            {
            // InternalQDL.g:4078:2: ( () otherlv_1= 'case' ( (lv_expr_2_0= ruleIQLLiteralExpression ) ) otherlv_3= ':' ( (lv_statements_4_0= ruleIQLStatement ) )* )
            // InternalQDL.g:4079:3: () otherlv_1= 'case' ( (lv_expr_2_0= ruleIQLLiteralExpression ) ) otherlv_3= ':' ( (lv_statements_4_0= ruleIQLStatement ) )*
            {
            // InternalQDL.g:4079:3: ()
            // InternalQDL.g:4080:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLCasePartAccess().getIQLCasePartAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,78,FOLLOW_11); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLCasePartAccess().getCaseKeyword_1());
              		
            }
            // InternalQDL.g:4090:3: ( (lv_expr_2_0= ruleIQLLiteralExpression ) )
            // InternalQDL.g:4091:4: (lv_expr_2_0= ruleIQLLiteralExpression )
            {
            // InternalQDL.g:4091:4: (lv_expr_2_0= ruleIQLLiteralExpression )
            // InternalQDL.g:4092:5: lv_expr_2_0= ruleIQLLiteralExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLCasePartAccess().getExprIQLLiteralExpressionParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_47);
            lv_expr_2_0=ruleIQLLiteralExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLCasePartRule());
              					}
              					set(
              						current,
              						"expr",
              						lv_expr_2_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLLiteralExpression");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_3=(Token)match(input,17,FOLLOW_49); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getIQLCasePartAccess().getColonKeyword_3());
              		
            }
            // InternalQDL.g:4113:3: ( (lv_statements_4_0= ruleIQLStatement ) )*
            loop55:
            do {
                int alt55=2;
                int LA55_0 = input.LA(1);

                if ( ((LA55_0>=RULE_ID && LA55_0<=RULE_STRING)||LA55_0==RULE_RANGE||LA55_0==14||(LA55_0>=20 && LA55_0<=21)||LA55_0==23||(LA55_0>=31 && LA55_0<=32)||LA55_0==37||LA55_0==57||LA55_0==59||LA55_0==71||(LA55_0>=73 && LA55_0<=76)||(LA55_0>=79 && LA55_0<=85)||LA55_0==88||(LA55_0>=111 && LA55_0<=112)) ) {
                    alt55=1;
                }


                switch (alt55) {
            	case 1 :
            	    // InternalQDL.g:4114:4: (lv_statements_4_0= ruleIQLStatement )
            	    {
            	    // InternalQDL.g:4114:4: (lv_statements_4_0= ruleIQLStatement )
            	    // InternalQDL.g:4115:5: lv_statements_4_0= ruleIQLStatement
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLCasePartAccess().getStatementsIQLStatementParserRuleCall_4_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_49);
            	    lv_statements_4_0=ruleIQLStatement();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getIQLCasePartRule());
            	      					}
            	      					add(
            	      						current,
            	      						"statements",
            	      						lv_statements_4_0,
            	      						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLStatement");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop55;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLCasePart"


    // $ANTLR start "entryRuleIQLExpressionStatement"
    // InternalQDL.g:4136:1: entryRuleIQLExpressionStatement returns [EObject current=null] : iv_ruleIQLExpressionStatement= ruleIQLExpressionStatement EOF ;
    public final EObject entryRuleIQLExpressionStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLExpressionStatement = null;


        try {
            // InternalQDL.g:4136:63: (iv_ruleIQLExpressionStatement= ruleIQLExpressionStatement EOF )
            // InternalQDL.g:4137:2: iv_ruleIQLExpressionStatement= ruleIQLExpressionStatement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLExpressionStatementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLExpressionStatement=ruleIQLExpressionStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLExpressionStatement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLExpressionStatement"


    // $ANTLR start "ruleIQLExpressionStatement"
    // InternalQDL.g:4143:1: ruleIQLExpressionStatement returns [EObject current=null] : ( () ( (lv_expression_1_0= ruleIQLExpression ) ) otherlv_2= ';' ) ;
    public final EObject ruleIQLExpressionStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject lv_expression_1_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:4149:2: ( ( () ( (lv_expression_1_0= ruleIQLExpression ) ) otherlv_2= ';' ) )
            // InternalQDL.g:4150:2: ( () ( (lv_expression_1_0= ruleIQLExpression ) ) otherlv_2= ';' )
            {
            // InternalQDL.g:4150:2: ( () ( (lv_expression_1_0= ruleIQLExpression ) ) otherlv_2= ';' )
            // InternalQDL.g:4151:3: () ( (lv_expression_1_0= ruleIQLExpression ) ) otherlv_2= ';'
            {
            // InternalQDL.g:4151:3: ()
            // InternalQDL.g:4152:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLExpressionStatementAccess().getIQLExpressionStatementAction_0(),
              					current);
              			
            }

            }

            // InternalQDL.g:4158:3: ( (lv_expression_1_0= ruleIQLExpression ) )
            // InternalQDL.g:4159:4: (lv_expression_1_0= ruleIQLExpression )
            {
            // InternalQDL.g:4159:4: (lv_expression_1_0= ruleIQLExpression )
            // InternalQDL.g:4160:5: lv_expression_1_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLExpressionStatementAccess().getExpressionIQLExpressionParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_17);
            lv_expression_1_0=ruleIQLExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLExpressionStatementRule());
              					}
              					set(
              						current,
              						"expression",
              						lv_expression_1_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_2=(Token)match(input,62,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLExpressionStatementAccess().getSemicolonKeyword_2());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLExpressionStatement"


    // $ANTLR start "entryRuleIQLVariableStatement"
    // InternalQDL.g:4185:1: entryRuleIQLVariableStatement returns [EObject current=null] : iv_ruleIQLVariableStatement= ruleIQLVariableStatement EOF ;
    public final EObject entryRuleIQLVariableStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLVariableStatement = null;


        try {
            // InternalQDL.g:4185:61: (iv_ruleIQLVariableStatement= ruleIQLVariableStatement EOF )
            // InternalQDL.g:4186:2: iv_ruleIQLVariableStatement= ruleIQLVariableStatement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLVariableStatementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLVariableStatement=ruleIQLVariableStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLVariableStatement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLVariableStatement"


    // $ANTLR start "ruleIQLVariableStatement"
    // InternalQDL.g:4192:1: ruleIQLVariableStatement returns [EObject current=null] : ( () ( (lv_var_1_0= ruleIQLVariableDeclaration ) ) ( (lv_init_2_0= ruleIQLVariableInitialization ) ) otherlv_3= ';' ) ;
    public final EObject ruleIQLVariableStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_3=null;
        EObject lv_var_1_0 = null;

        EObject lv_init_2_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:4198:2: ( ( () ( (lv_var_1_0= ruleIQLVariableDeclaration ) ) ( (lv_init_2_0= ruleIQLVariableInitialization ) ) otherlv_3= ';' ) )
            // InternalQDL.g:4199:2: ( () ( (lv_var_1_0= ruleIQLVariableDeclaration ) ) ( (lv_init_2_0= ruleIQLVariableInitialization ) ) otherlv_3= ';' )
            {
            // InternalQDL.g:4199:2: ( () ( (lv_var_1_0= ruleIQLVariableDeclaration ) ) ( (lv_init_2_0= ruleIQLVariableInitialization ) ) otherlv_3= ';' )
            // InternalQDL.g:4200:3: () ( (lv_var_1_0= ruleIQLVariableDeclaration ) ) ( (lv_init_2_0= ruleIQLVariableInitialization ) ) otherlv_3= ';'
            {
            // InternalQDL.g:4200:3: ()
            // InternalQDL.g:4201:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLVariableStatementAccess().getIQLVariableStatementAction_0(),
              					current);
              			
            }

            }

            // InternalQDL.g:4207:3: ( (lv_var_1_0= ruleIQLVariableDeclaration ) )
            // InternalQDL.g:4208:4: (lv_var_1_0= ruleIQLVariableDeclaration )
            {
            // InternalQDL.g:4208:4: (lv_var_1_0= ruleIQLVariableDeclaration )
            // InternalQDL.g:4209:5: lv_var_1_0= ruleIQLVariableDeclaration
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLVariableStatementAccess().getVarIQLVariableDeclarationParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_50);
            lv_var_1_0=ruleIQLVariableDeclaration();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLVariableStatementRule());
              					}
              					set(
              						current,
              						"var",
              						lv_var_1_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLVariableDeclaration");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalQDL.g:4226:3: ( (lv_init_2_0= ruleIQLVariableInitialization ) )
            // InternalQDL.g:4227:4: (lv_init_2_0= ruleIQLVariableInitialization )
            {
            // InternalQDL.g:4227:4: (lv_init_2_0= ruleIQLVariableInitialization )
            // InternalQDL.g:4228:5: lv_init_2_0= ruleIQLVariableInitialization
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLVariableStatementAccess().getInitIQLVariableInitializationParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_17);
            lv_init_2_0=ruleIQLVariableInitialization();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLVariableStatementRule());
              					}
              					set(
              						current,
              						"init",
              						lv_init_2_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLVariableInitialization");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_3=(Token)match(input,62,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getIQLVariableStatementAccess().getSemicolonKeyword_3());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLVariableStatement"


    // $ANTLR start "entryRuleIQLConstructorCallStatement"
    // InternalQDL.g:4253:1: entryRuleIQLConstructorCallStatement returns [EObject current=null] : iv_ruleIQLConstructorCallStatement= ruleIQLConstructorCallStatement EOF ;
    public final EObject entryRuleIQLConstructorCallStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLConstructorCallStatement = null;


        try {
            // InternalQDL.g:4253:68: (iv_ruleIQLConstructorCallStatement= ruleIQLConstructorCallStatement EOF )
            // InternalQDL.g:4254:2: iv_ruleIQLConstructorCallStatement= ruleIQLConstructorCallStatement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLConstructorCallStatementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLConstructorCallStatement=ruleIQLConstructorCallStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLConstructorCallStatement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLConstructorCallStatement"


    // $ANTLR start "ruleIQLConstructorCallStatement"
    // InternalQDL.g:4260:1: ruleIQLConstructorCallStatement returns [EObject current=null] : ( () ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) ) ( (lv_args_3_0= ruleIQLArgumentsList ) ) otherlv_4= ';' ) ;
    public final EObject ruleIQLConstructorCallStatement() throws RecognitionException {
        EObject current = null;

        Token lv_this_1_0=null;
        Token lv_super_2_0=null;
        Token otherlv_4=null;
        EObject lv_args_3_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:4266:2: ( ( () ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) ) ( (lv_args_3_0= ruleIQLArgumentsList ) ) otherlv_4= ';' ) )
            // InternalQDL.g:4267:2: ( () ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) ) ( (lv_args_3_0= ruleIQLArgumentsList ) ) otherlv_4= ';' )
            {
            // InternalQDL.g:4267:2: ( () ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) ) ( (lv_args_3_0= ruleIQLArgumentsList ) ) otherlv_4= ';' )
            // InternalQDL.g:4268:3: () ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) ) ( (lv_args_3_0= ruleIQLArgumentsList ) ) otherlv_4= ';'
            {
            // InternalQDL.g:4268:3: ()
            // InternalQDL.g:4269:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLConstructorCallStatementAccess().getIQLConstructorCallStatementAction_0(),
              					current);
              			
            }

            }

            // InternalQDL.g:4275:3: ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) )
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==79) ) {
                alt56=1;
            }
            else if ( (LA56_0==80) ) {
                alt56=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;
            }
            switch (alt56) {
                case 1 :
                    // InternalQDL.g:4276:4: ( (lv_this_1_0= 'this' ) )
                    {
                    // InternalQDL.g:4276:4: ( (lv_this_1_0= 'this' ) )
                    // InternalQDL.g:4277:5: (lv_this_1_0= 'this' )
                    {
                    // InternalQDL.g:4277:5: (lv_this_1_0= 'this' )
                    // InternalQDL.g:4278:6: lv_this_1_0= 'this'
                    {
                    lv_this_1_0=(Token)match(input,79,FOLLOW_30); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_this_1_0, grammarAccess.getIQLConstructorCallStatementAccess().getThisThisKeyword_1_0_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getIQLConstructorCallStatementRule());
                      						}
                      						setWithLastConsumed(current, "this", true, "this");
                      					
                    }

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalQDL.g:4291:4: ( (lv_super_2_0= 'super' ) )
                    {
                    // InternalQDL.g:4291:4: ( (lv_super_2_0= 'super' ) )
                    // InternalQDL.g:4292:5: (lv_super_2_0= 'super' )
                    {
                    // InternalQDL.g:4292:5: (lv_super_2_0= 'super' )
                    // InternalQDL.g:4293:6: lv_super_2_0= 'super'
                    {
                    lv_super_2_0=(Token)match(input,80,FOLLOW_30); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_super_2_0, grammarAccess.getIQLConstructorCallStatementAccess().getSuperSuperKeyword_1_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getIQLConstructorCallStatementRule());
                      						}
                      						setWithLastConsumed(current, "super", true, "super");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalQDL.g:4306:3: ( (lv_args_3_0= ruleIQLArgumentsList ) )
            // InternalQDL.g:4307:4: (lv_args_3_0= ruleIQLArgumentsList )
            {
            // InternalQDL.g:4307:4: (lv_args_3_0= ruleIQLArgumentsList )
            // InternalQDL.g:4308:5: lv_args_3_0= ruleIQLArgumentsList
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLConstructorCallStatementAccess().getArgsIQLArgumentsListParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_17);
            lv_args_3_0=ruleIQLArgumentsList();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLConstructorCallStatementRule());
              					}
              					set(
              						current,
              						"args",
              						lv_args_3_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLArgumentsList");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_4=(Token)match(input,62,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLConstructorCallStatementAccess().getSemicolonKeyword_3());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLConstructorCallStatement"


    // $ANTLR start "entryRuleIQLBreakStatement"
    // InternalQDL.g:4333:1: entryRuleIQLBreakStatement returns [EObject current=null] : iv_ruleIQLBreakStatement= ruleIQLBreakStatement EOF ;
    public final EObject entryRuleIQLBreakStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLBreakStatement = null;


        try {
            // InternalQDL.g:4333:58: (iv_ruleIQLBreakStatement= ruleIQLBreakStatement EOF )
            // InternalQDL.g:4334:2: iv_ruleIQLBreakStatement= ruleIQLBreakStatement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLBreakStatementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLBreakStatement=ruleIQLBreakStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLBreakStatement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLBreakStatement"


    // $ANTLR start "ruleIQLBreakStatement"
    // InternalQDL.g:4340:1: ruleIQLBreakStatement returns [EObject current=null] : ( () otherlv_1= 'break' otherlv_2= ';' ) ;
    public final EObject ruleIQLBreakStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;


        	enterRule();

        try {
            // InternalQDL.g:4346:2: ( ( () otherlv_1= 'break' otherlv_2= ';' ) )
            // InternalQDL.g:4347:2: ( () otherlv_1= 'break' otherlv_2= ';' )
            {
            // InternalQDL.g:4347:2: ( () otherlv_1= 'break' otherlv_2= ';' )
            // InternalQDL.g:4348:3: () otherlv_1= 'break' otherlv_2= ';'
            {
            // InternalQDL.g:4348:3: ()
            // InternalQDL.g:4349:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLBreakStatementAccess().getIQLBreakStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,81,FOLLOW_17); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLBreakStatementAccess().getBreakKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,62,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLBreakStatementAccess().getSemicolonKeyword_2());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLBreakStatement"


    // $ANTLR start "entryRuleIQLContinueStatement"
    // InternalQDL.g:4367:1: entryRuleIQLContinueStatement returns [EObject current=null] : iv_ruleIQLContinueStatement= ruleIQLContinueStatement EOF ;
    public final EObject entryRuleIQLContinueStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLContinueStatement = null;


        try {
            // InternalQDL.g:4367:61: (iv_ruleIQLContinueStatement= ruleIQLContinueStatement EOF )
            // InternalQDL.g:4368:2: iv_ruleIQLContinueStatement= ruleIQLContinueStatement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLContinueStatementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLContinueStatement=ruleIQLContinueStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLContinueStatement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLContinueStatement"


    // $ANTLR start "ruleIQLContinueStatement"
    // InternalQDL.g:4374:1: ruleIQLContinueStatement returns [EObject current=null] : ( () otherlv_1= 'continue' otherlv_2= ';' ) ;
    public final EObject ruleIQLContinueStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;


        	enterRule();

        try {
            // InternalQDL.g:4380:2: ( ( () otherlv_1= 'continue' otherlv_2= ';' ) )
            // InternalQDL.g:4381:2: ( () otherlv_1= 'continue' otherlv_2= ';' )
            {
            // InternalQDL.g:4381:2: ( () otherlv_1= 'continue' otherlv_2= ';' )
            // InternalQDL.g:4382:3: () otherlv_1= 'continue' otherlv_2= ';'
            {
            // InternalQDL.g:4382:3: ()
            // InternalQDL.g:4383:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLContinueStatementAccess().getIQLContinueStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,82,FOLLOW_17); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLContinueStatementAccess().getContinueKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,62,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLContinueStatementAccess().getSemicolonKeyword_2());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLContinueStatement"


    // $ANTLR start "entryRuleIQLReturnStatement"
    // InternalQDL.g:4401:1: entryRuleIQLReturnStatement returns [EObject current=null] : iv_ruleIQLReturnStatement= ruleIQLReturnStatement EOF ;
    public final EObject entryRuleIQLReturnStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLReturnStatement = null;


        try {
            // InternalQDL.g:4401:59: (iv_ruleIQLReturnStatement= ruleIQLReturnStatement EOF )
            // InternalQDL.g:4402:2: iv_ruleIQLReturnStatement= ruleIQLReturnStatement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLReturnStatementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLReturnStatement=ruleIQLReturnStatement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLReturnStatement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLReturnStatement"


    // $ANTLR start "ruleIQLReturnStatement"
    // InternalQDL.g:4408:1: ruleIQLReturnStatement returns [EObject current=null] : ( () otherlv_1= 'return' ( (lv_expression_2_0= ruleIQLExpression ) )? otherlv_3= ';' ) ;
    public final EObject ruleIQLReturnStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject lv_expression_2_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:4414:2: ( ( () otherlv_1= 'return' ( (lv_expression_2_0= ruleIQLExpression ) )? otherlv_3= ';' ) )
            // InternalQDL.g:4415:2: ( () otherlv_1= 'return' ( (lv_expression_2_0= ruleIQLExpression ) )? otherlv_3= ';' )
            {
            // InternalQDL.g:4415:2: ( () otherlv_1= 'return' ( (lv_expression_2_0= ruleIQLExpression ) )? otherlv_3= ';' )
            // InternalQDL.g:4416:3: () otherlv_1= 'return' ( (lv_expression_2_0= ruleIQLExpression ) )? otherlv_3= ';'
            {
            // InternalQDL.g:4416:3: ()
            // InternalQDL.g:4417:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLReturnStatementAccess().getIQLReturnStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,83,FOLLOW_51); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLReturnStatementAccess().getReturnKeyword_1());
              		
            }
            // InternalQDL.g:4427:3: ( (lv_expression_2_0= ruleIQLExpression ) )?
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( ((LA57_0>=RULE_ID && LA57_0<=RULE_STRING)||LA57_0==RULE_RANGE||LA57_0==14||(LA57_0>=20 && LA57_0<=21)||LA57_0==23||(LA57_0>=31 && LA57_0<=32)||LA57_0==37||LA57_0==57||(LA57_0>=79 && LA57_0<=80)||(LA57_0>=84 && LA57_0<=85)||(LA57_0>=111 && LA57_0<=112)) ) {
                alt57=1;
            }
            switch (alt57) {
                case 1 :
                    // InternalQDL.g:4428:4: (lv_expression_2_0= ruleIQLExpression )
                    {
                    // InternalQDL.g:4428:4: (lv_expression_2_0= ruleIQLExpression )
                    // InternalQDL.g:4429:5: lv_expression_2_0= ruleIQLExpression
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getIQLReturnStatementAccess().getExpressionIQLExpressionParserRuleCall_2_0());
                      				
                    }
                    pushFollow(FOLLOW_17);
                    lv_expression_2_0=ruleIQLExpression();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getIQLReturnStatementRule());
                      					}
                      					set(
                      						current,
                      						"expression",
                      						lv_expression_2_0,
                      						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_3=(Token)match(input,62,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getIQLReturnStatementAccess().getSemicolonKeyword_3());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLReturnStatement"


    // $ANTLR start "entryRuleIQLExpression"
    // InternalQDL.g:4454:1: entryRuleIQLExpression returns [EObject current=null] : iv_ruleIQLExpression= ruleIQLExpression EOF ;
    public final EObject entryRuleIQLExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLExpression = null;


        try {
            // InternalQDL.g:4454:54: (iv_ruleIQLExpression= ruleIQLExpression EOF )
            // InternalQDL.g:4455:2: iv_ruleIQLExpression= ruleIQLExpression EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLExpressionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLExpression=ruleIQLExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLExpression; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLExpression"


    // $ANTLR start "ruleIQLExpression"
    // InternalQDL.g:4461:1: ruleIQLExpression returns [EObject current=null] : this_IQLAssignmentExpression_0= ruleIQLAssignmentExpression ;
    public final EObject ruleIQLExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLAssignmentExpression_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:4467:2: (this_IQLAssignmentExpression_0= ruleIQLAssignmentExpression )
            // InternalQDL.g:4468:2: this_IQLAssignmentExpression_0= ruleIQLAssignmentExpression
            {
            if ( state.backtracking==0 ) {

              		newCompositeNode(grammarAccess.getIQLExpressionAccess().getIQLAssignmentExpressionParserRuleCall());
              	
            }
            pushFollow(FOLLOW_2);
            this_IQLAssignmentExpression_0=ruleIQLAssignmentExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              		current = this_IQLAssignmentExpression_0;
              		afterParserOrEnumRuleCall();
              	
            }

            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLExpression"


    // $ANTLR start "entryRuleIQLAssignmentExpression"
    // InternalQDL.g:4479:1: entryRuleIQLAssignmentExpression returns [EObject current=null] : iv_ruleIQLAssignmentExpression= ruleIQLAssignmentExpression EOF ;
    public final EObject entryRuleIQLAssignmentExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLAssignmentExpression = null;


        try {
            // InternalQDL.g:4479:64: (iv_ruleIQLAssignmentExpression= ruleIQLAssignmentExpression EOF )
            // InternalQDL.g:4480:2: iv_ruleIQLAssignmentExpression= ruleIQLAssignmentExpression EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLAssignmentExpressionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLAssignmentExpression=ruleIQLAssignmentExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLAssignmentExpression; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLAssignmentExpression"


    // $ANTLR start "ruleIQLAssignmentExpression"
    // InternalQDL.g:4486:1: ruleIQLAssignmentExpression returns [EObject current=null] : (this_IQLLogicalOrExpression_0= ruleIQLLogicalOrExpression ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )? ) ;
    public final EObject ruleIQLAssignmentExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLLogicalOrExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:4492:2: ( (this_IQLLogicalOrExpression_0= ruleIQLLogicalOrExpression ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )? ) )
            // InternalQDL.g:4493:2: (this_IQLLogicalOrExpression_0= ruleIQLLogicalOrExpression ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )? )
            {
            // InternalQDL.g:4493:2: (this_IQLLogicalOrExpression_0= ruleIQLLogicalOrExpression ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )? )
            // InternalQDL.g:4494:3: this_IQLLogicalOrExpression_0= ruleIQLLogicalOrExpression ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )?
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLAssignmentExpressionAccess().getIQLLogicalOrExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_52);
            this_IQLLogicalOrExpression_0=ruleIQLLogicalOrExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLLogicalOrExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalQDL.g:4502:3: ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )?
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==42) && (synpred7_InternalQDL())) {
                alt58=1;
            }
            else if ( (LA58_0==22) && (synpred7_InternalQDL())) {
                alt58=1;
            }
            else if ( (LA58_0==24) && (synpred7_InternalQDL())) {
                alt58=1;
            }
            else if ( (LA58_0==26) && (synpred7_InternalQDL())) {
                alt58=1;
            }
            else if ( (LA58_0==28) && (synpred7_InternalQDL())) {
                alt58=1;
            }
            else if ( (LA58_0==30) && (synpred7_InternalQDL())) {
                alt58=1;
            }
            switch (alt58) {
                case 1 :
                    // InternalQDL.g:4503:4: ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) )
                    {
                    // InternalQDL.g:4503:4: ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) )
                    // InternalQDL.g:4504:5: ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) )
                    {
                    // InternalQDL.g:4514:5: ( () ( (lv_op_2_0= ruleOpAssign ) ) )
                    // InternalQDL.g:4515:6: () ( (lv_op_2_0= ruleOpAssign ) )
                    {
                    // InternalQDL.g:4515:6: ()
                    // InternalQDL.g:4516:7: 
                    {
                    if ( state.backtracking==0 ) {

                      							current = forceCreateModelElementAndSet(
                      								grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0(),
                      								current);
                      						
                    }

                    }

                    // InternalQDL.g:4522:6: ( (lv_op_2_0= ruleOpAssign ) )
                    // InternalQDL.g:4523:7: (lv_op_2_0= ruleOpAssign )
                    {
                    // InternalQDL.g:4523:7: (lv_op_2_0= ruleOpAssign )
                    // InternalQDL.g:4524:8: lv_op_2_0= ruleOpAssign
                    {
                    if ( state.backtracking==0 ) {

                      								newCompositeNode(grammarAccess.getIQLAssignmentExpressionAccess().getOpOpAssignParserRuleCall_1_0_0_1_0());
                      							
                    }
                    pushFollow(FOLLOW_11);
                    lv_op_2_0=ruleOpAssign();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      								if (current==null) {
                      									current = createModelElementForParent(grammarAccess.getIQLAssignmentExpressionRule());
                      								}
                      								set(
                      									current,
                      									"op",
                      									lv_op_2_0,
                      									"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.OpAssign");
                      								afterParserOrEnumRuleCall();
                      							
                    }

                    }


                    }


                    }


                    }

                    // InternalQDL.g:4543:4: ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) )
                    // InternalQDL.g:4544:5: (lv_rightOperand_3_0= ruleIQLAssignmentExpression )
                    {
                    // InternalQDL.g:4544:5: (lv_rightOperand_3_0= ruleIQLAssignmentExpression )
                    // InternalQDL.g:4545:6: lv_rightOperand_3_0= ruleIQLAssignmentExpression
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLAssignmentExpressionAccess().getRightOperandIQLAssignmentExpressionParserRuleCall_1_1_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_rightOperand_3_0=ruleIQLAssignmentExpression();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLAssignmentExpressionRule());
                      						}
                      						set(
                      							current,
                      							"rightOperand",
                      							lv_rightOperand_3_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLAssignmentExpression");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLAssignmentExpression"


    // $ANTLR start "entryRuleOpAssign"
    // InternalQDL.g:4567:1: entryRuleOpAssign returns [String current=null] : iv_ruleOpAssign= ruleOpAssign EOF ;
    public final String entryRuleOpAssign() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpAssign = null;


        try {
            // InternalQDL.g:4567:48: (iv_ruleOpAssign= ruleOpAssign EOF )
            // InternalQDL.g:4568:2: iv_ruleOpAssign= ruleOpAssign EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getOpAssignRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleOpAssign=ruleOpAssign();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleOpAssign.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleOpAssign"


    // $ANTLR start "ruleOpAssign"
    // InternalQDL.g:4574:1: ruleOpAssign returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '=' | kw= '+=' | kw= '-=' | kw= '*=' | kw= '/=' | kw= '%=' ) ;
    public final AntlrDatatypeRuleToken ruleOpAssign() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalQDL.g:4580:2: ( (kw= '=' | kw= '+=' | kw= '-=' | kw= '*=' | kw= '/=' | kw= '%=' ) )
            // InternalQDL.g:4581:2: (kw= '=' | kw= '+=' | kw= '-=' | kw= '*=' | kw= '/=' | kw= '%=' )
            {
            // InternalQDL.g:4581:2: (kw= '=' | kw= '+=' | kw= '-=' | kw= '*=' | kw= '/=' | kw= '%=' )
            int alt59=6;
            switch ( input.LA(1) ) {
            case 42:
                {
                alt59=1;
                }
                break;
            case 22:
                {
                alt59=2;
                }
                break;
            case 24:
                {
                alt59=3;
                }
                break;
            case 26:
                {
                alt59=4;
                }
                break;
            case 28:
                {
                alt59=5;
                }
                break;
            case 30:
                {
                alt59=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 59, 0, input);

                throw nvae;
            }

            switch (alt59) {
                case 1 :
                    // InternalQDL.g:4582:3: kw= '='
                    {
                    kw=(Token)match(input,42,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAssignAccess().getEqualsSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalQDL.g:4588:3: kw= '+='
                    {
                    kw=(Token)match(input,22,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAssignAccess().getPlusSignEqualsSignKeyword_1());
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalQDL.g:4594:3: kw= '-='
                    {
                    kw=(Token)match(input,24,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAssignAccess().getHyphenMinusEqualsSignKeyword_2());
                      		
                    }

                    }
                    break;
                case 4 :
                    // InternalQDL.g:4600:3: kw= '*='
                    {
                    kw=(Token)match(input,26,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAssignAccess().getAsteriskEqualsSignKeyword_3());
                      		
                    }

                    }
                    break;
                case 5 :
                    // InternalQDL.g:4606:3: kw= '/='
                    {
                    kw=(Token)match(input,28,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAssignAccess().getSolidusEqualsSignKeyword_4());
                      		
                    }

                    }
                    break;
                case 6 :
                    // InternalQDL.g:4612:3: kw= '%='
                    {
                    kw=(Token)match(input,30,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAssignAccess().getPercentSignEqualsSignKeyword_5());
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleOpAssign"


    // $ANTLR start "entryRuleIQLLogicalOrExpression"
    // InternalQDL.g:4621:1: entryRuleIQLLogicalOrExpression returns [EObject current=null] : iv_ruleIQLLogicalOrExpression= ruleIQLLogicalOrExpression EOF ;
    public final EObject entryRuleIQLLogicalOrExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLogicalOrExpression = null;


        try {
            // InternalQDL.g:4621:63: (iv_ruleIQLLogicalOrExpression= ruleIQLLogicalOrExpression EOF )
            // InternalQDL.g:4622:2: iv_ruleIQLLogicalOrExpression= ruleIQLLogicalOrExpression EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLLogicalOrExpressionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLLogicalOrExpression=ruleIQLLogicalOrExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLLogicalOrExpression; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLLogicalOrExpression"


    // $ANTLR start "ruleIQLLogicalOrExpression"
    // InternalQDL.g:4628:1: ruleIQLLogicalOrExpression returns [EObject current=null] : (this_IQLLogicalAndExpression_0= ruleIQLLogicalAndExpression ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )* ) ;
    public final EObject ruleIQLLogicalOrExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLLogicalAndExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:4634:2: ( (this_IQLLogicalAndExpression_0= ruleIQLLogicalAndExpression ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )* ) )
            // InternalQDL.g:4635:2: (this_IQLLogicalAndExpression_0= ruleIQLLogicalAndExpression ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )* )
            {
            // InternalQDL.g:4635:2: (this_IQLLogicalAndExpression_0= ruleIQLLogicalAndExpression ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )* )
            // InternalQDL.g:4636:3: this_IQLLogicalAndExpression_0= ruleIQLLogicalAndExpression ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalAndExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_53);
            this_IQLLogicalAndExpression_0=ruleIQLLogicalAndExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLLogicalAndExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalQDL.g:4644:3: ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )*
            loop60:
            do {
                int alt60=2;
                int LA60_0 = input.LA(1);

                if ( (LA60_0==40) && (synpred8_InternalQDL())) {
                    alt60=1;
                }


                switch (alt60) {
            	case 1 :
            	    // InternalQDL.g:4645:4: ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) )
            	    {
            	    // InternalQDL.g:4645:4: ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) )
            	    // InternalQDL.g:4646:5: ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) )
            	    {
            	    // InternalQDL.g:4656:5: ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) )
            	    // InternalQDL.g:4657:6: () ( (lv_op_2_0= ruleOpLogicalOr ) )
            	    {
            	    // InternalQDL.g:4657:6: ()
            	    // InternalQDL.g:4658:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    // InternalQDL.g:4664:6: ( (lv_op_2_0= ruleOpLogicalOr ) )
            	    // InternalQDL.g:4665:7: (lv_op_2_0= ruleOpLogicalOr )
            	    {
            	    // InternalQDL.g:4665:7: (lv_op_2_0= ruleOpLogicalOr )
            	    // InternalQDL.g:4666:8: lv_op_2_0= ruleOpLogicalOr
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLLogicalOrExpressionAccess().getOpOpLogicalOrParserRuleCall_1_0_0_1_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_11);
            	    lv_op_2_0=ruleOpLogicalOr();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      								if (current==null) {
            	      									current = createModelElementForParent(grammarAccess.getIQLLogicalOrExpressionRule());
            	      								}
            	      								set(
            	      									current,
            	      									"op",
            	      									lv_op_2_0,
            	      									"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.OpLogicalOr");
            	      								afterParserOrEnumRuleCall();
            	      							
            	    }

            	    }


            	    }


            	    }


            	    }

            	    // InternalQDL.g:4685:4: ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) )
            	    // InternalQDL.g:4686:5: (lv_rightOperand_3_0= ruleIQLLogicalAndExpression )
            	    {
            	    // InternalQDL.g:4686:5: (lv_rightOperand_3_0= ruleIQLLogicalAndExpression )
            	    // InternalQDL.g:4687:6: lv_rightOperand_3_0= ruleIQLLogicalAndExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLLogicalOrExpressionAccess().getRightOperandIQLLogicalAndExpressionParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_53);
            	    lv_rightOperand_3_0=ruleIQLLogicalAndExpression();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						if (current==null) {
            	      							current = createModelElementForParent(grammarAccess.getIQLLogicalOrExpressionRule());
            	      						}
            	      						set(
            	      							current,
            	      							"rightOperand",
            	      							lv_rightOperand_3_0,
            	      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLLogicalAndExpression");
            	      						afterParserOrEnumRuleCall();
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop60;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLLogicalOrExpression"


    // $ANTLR start "entryRuleOpLogicalOr"
    // InternalQDL.g:4709:1: entryRuleOpLogicalOr returns [String current=null] : iv_ruleOpLogicalOr= ruleOpLogicalOr EOF ;
    public final String entryRuleOpLogicalOr() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpLogicalOr = null;


        try {
            // InternalQDL.g:4709:51: (iv_ruleOpLogicalOr= ruleOpLogicalOr EOF )
            // InternalQDL.g:4710:2: iv_ruleOpLogicalOr= ruleOpLogicalOr EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getOpLogicalOrRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleOpLogicalOr=ruleOpLogicalOr();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleOpLogicalOr.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleOpLogicalOr"


    // $ANTLR start "ruleOpLogicalOr"
    // InternalQDL.g:4716:1: ruleOpLogicalOr returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= '||' ;
    public final AntlrDatatypeRuleToken ruleOpLogicalOr() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalQDL.g:4722:2: (kw= '||' )
            // InternalQDL.g:4723:2: kw= '||'
            {
            kw=(Token)match(input,40,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              		current.merge(kw);
              		newLeafNode(kw, grammarAccess.getOpLogicalOrAccess().getVerticalLineVerticalLineKeyword());
              	
            }

            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleOpLogicalOr"


    // $ANTLR start "entryRuleIQLLogicalAndExpression"
    // InternalQDL.g:4731:1: entryRuleIQLLogicalAndExpression returns [EObject current=null] : iv_ruleIQLLogicalAndExpression= ruleIQLLogicalAndExpression EOF ;
    public final EObject entryRuleIQLLogicalAndExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLogicalAndExpression = null;


        try {
            // InternalQDL.g:4731:64: (iv_ruleIQLLogicalAndExpression= ruleIQLLogicalAndExpression EOF )
            // InternalQDL.g:4732:2: iv_ruleIQLLogicalAndExpression= ruleIQLLogicalAndExpression EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLLogicalAndExpressionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLLogicalAndExpression=ruleIQLLogicalAndExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLLogicalAndExpression; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLLogicalAndExpression"


    // $ANTLR start "ruleIQLLogicalAndExpression"
    // InternalQDL.g:4738:1: ruleIQLLogicalAndExpression returns [EObject current=null] : (this_IQLEqualityExpression_0= ruleIQLEqualityExpression ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )* ) ;
    public final EObject ruleIQLLogicalAndExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLEqualityExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:4744:2: ( (this_IQLEqualityExpression_0= ruleIQLEqualityExpression ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )* ) )
            // InternalQDL.g:4745:2: (this_IQLEqualityExpression_0= ruleIQLEqualityExpression ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )* )
            {
            // InternalQDL.g:4745:2: (this_IQLEqualityExpression_0= ruleIQLEqualityExpression ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )* )
            // InternalQDL.g:4746:3: this_IQLEqualityExpression_0= ruleIQLEqualityExpression ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLLogicalAndExpressionAccess().getIQLEqualityExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_54);
            this_IQLEqualityExpression_0=ruleIQLEqualityExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLEqualityExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalQDL.g:4754:3: ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )*
            loop61:
            do {
                int alt61=2;
                int LA61_0 = input.LA(1);

                if ( (LA61_0==39) && (synpred9_InternalQDL())) {
                    alt61=1;
                }


                switch (alt61) {
            	case 1 :
            	    // InternalQDL.g:4755:4: ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) )
            	    {
            	    // InternalQDL.g:4755:4: ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) )
            	    // InternalQDL.g:4756:5: ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) )
            	    {
            	    // InternalQDL.g:4766:5: ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) )
            	    // InternalQDL.g:4767:6: () ( (lv_op_2_0= ruleOpLogicalAnd ) )
            	    {
            	    // InternalQDL.g:4767:6: ()
            	    // InternalQDL.g:4768:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    // InternalQDL.g:4774:6: ( (lv_op_2_0= ruleOpLogicalAnd ) )
            	    // InternalQDL.g:4775:7: (lv_op_2_0= ruleOpLogicalAnd )
            	    {
            	    // InternalQDL.g:4775:7: (lv_op_2_0= ruleOpLogicalAnd )
            	    // InternalQDL.g:4776:8: lv_op_2_0= ruleOpLogicalAnd
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLLogicalAndExpressionAccess().getOpOpLogicalAndParserRuleCall_1_0_0_1_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_11);
            	    lv_op_2_0=ruleOpLogicalAnd();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      								if (current==null) {
            	      									current = createModelElementForParent(grammarAccess.getIQLLogicalAndExpressionRule());
            	      								}
            	      								set(
            	      									current,
            	      									"op",
            	      									lv_op_2_0,
            	      									"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.OpLogicalAnd");
            	      								afterParserOrEnumRuleCall();
            	      							
            	    }

            	    }


            	    }


            	    }


            	    }

            	    // InternalQDL.g:4795:4: ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) )
            	    // InternalQDL.g:4796:5: (lv_rightOperand_3_0= ruleIQLEqualityExpression )
            	    {
            	    // InternalQDL.g:4796:5: (lv_rightOperand_3_0= ruleIQLEqualityExpression )
            	    // InternalQDL.g:4797:6: lv_rightOperand_3_0= ruleIQLEqualityExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLLogicalAndExpressionAccess().getRightOperandIQLEqualityExpressionParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_54);
            	    lv_rightOperand_3_0=ruleIQLEqualityExpression();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						if (current==null) {
            	      							current = createModelElementForParent(grammarAccess.getIQLLogicalAndExpressionRule());
            	      						}
            	      						set(
            	      							current,
            	      							"rightOperand",
            	      							lv_rightOperand_3_0,
            	      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLEqualityExpression");
            	      						afterParserOrEnumRuleCall();
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop61;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLLogicalAndExpression"


    // $ANTLR start "entryRuleOpLogicalAnd"
    // InternalQDL.g:4819:1: entryRuleOpLogicalAnd returns [String current=null] : iv_ruleOpLogicalAnd= ruleOpLogicalAnd EOF ;
    public final String entryRuleOpLogicalAnd() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpLogicalAnd = null;


        try {
            // InternalQDL.g:4819:52: (iv_ruleOpLogicalAnd= ruleOpLogicalAnd EOF )
            // InternalQDL.g:4820:2: iv_ruleOpLogicalAnd= ruleOpLogicalAnd EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getOpLogicalAndRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleOpLogicalAnd=ruleOpLogicalAnd();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleOpLogicalAnd.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleOpLogicalAnd"


    // $ANTLR start "ruleOpLogicalAnd"
    // InternalQDL.g:4826:1: ruleOpLogicalAnd returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= '&&' ;
    public final AntlrDatatypeRuleToken ruleOpLogicalAnd() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalQDL.g:4832:2: (kw= '&&' )
            // InternalQDL.g:4833:2: kw= '&&'
            {
            kw=(Token)match(input,39,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              		current.merge(kw);
              		newLeafNode(kw, grammarAccess.getOpLogicalAndAccess().getAmpersandAmpersandKeyword());
              	
            }

            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleOpLogicalAnd"


    // $ANTLR start "entryRuleIQLEqualityExpression"
    // InternalQDL.g:4841:1: entryRuleIQLEqualityExpression returns [EObject current=null] : iv_ruleIQLEqualityExpression= ruleIQLEqualityExpression EOF ;
    public final EObject entryRuleIQLEqualityExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLEqualityExpression = null;


        try {
            // InternalQDL.g:4841:62: (iv_ruleIQLEqualityExpression= ruleIQLEqualityExpression EOF )
            // InternalQDL.g:4842:2: iv_ruleIQLEqualityExpression= ruleIQLEqualityExpression EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLEqualityExpressionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLEqualityExpression=ruleIQLEqualityExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLEqualityExpression; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLEqualityExpression"


    // $ANTLR start "ruleIQLEqualityExpression"
    // InternalQDL.g:4848:1: ruleIQLEqualityExpression returns [EObject current=null] : (this_IQLRelationalExpression_0= ruleIQLRelationalExpression ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )* ) ;
    public final EObject ruleIQLEqualityExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLRelationalExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:4854:2: ( (this_IQLRelationalExpression_0= ruleIQLRelationalExpression ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )* ) )
            // InternalQDL.g:4855:2: (this_IQLRelationalExpression_0= ruleIQLRelationalExpression ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )* )
            {
            // InternalQDL.g:4855:2: (this_IQLRelationalExpression_0= ruleIQLRelationalExpression ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )* )
            // InternalQDL.g:4856:3: this_IQLRelationalExpression_0= ruleIQLRelationalExpression ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLEqualityExpressionAccess().getIQLRelationalExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_55);
            this_IQLRelationalExpression_0=ruleIQLRelationalExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLRelationalExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalQDL.g:4864:3: ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )*
            loop62:
            do {
                int alt62=2;
                int LA62_0 = input.LA(1);

                if ( (LA62_0==41) && (synpred10_InternalQDL())) {
                    alt62=1;
                }
                else if ( (LA62_0==38) && (synpred10_InternalQDL())) {
                    alt62=1;
                }


                switch (alt62) {
            	case 1 :
            	    // InternalQDL.g:4865:4: ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) )
            	    {
            	    // InternalQDL.g:4865:4: ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) )
            	    // InternalQDL.g:4866:5: ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) )
            	    {
            	    // InternalQDL.g:4876:5: ( () ( (lv_op_2_0= ruleOpEquality ) ) )
            	    // InternalQDL.g:4877:6: () ( (lv_op_2_0= ruleOpEquality ) )
            	    {
            	    // InternalQDL.g:4877:6: ()
            	    // InternalQDL.g:4878:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    // InternalQDL.g:4884:6: ( (lv_op_2_0= ruleOpEquality ) )
            	    // InternalQDL.g:4885:7: (lv_op_2_0= ruleOpEquality )
            	    {
            	    // InternalQDL.g:4885:7: (lv_op_2_0= ruleOpEquality )
            	    // InternalQDL.g:4886:8: lv_op_2_0= ruleOpEquality
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLEqualityExpressionAccess().getOpOpEqualityParserRuleCall_1_0_0_1_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_11);
            	    lv_op_2_0=ruleOpEquality();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      								if (current==null) {
            	      									current = createModelElementForParent(grammarAccess.getIQLEqualityExpressionRule());
            	      								}
            	      								set(
            	      									current,
            	      									"op",
            	      									lv_op_2_0,
            	      									"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.OpEquality");
            	      								afterParserOrEnumRuleCall();
            	      							
            	    }

            	    }


            	    }


            	    }


            	    }

            	    // InternalQDL.g:4905:4: ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) )
            	    // InternalQDL.g:4906:5: (lv_rightOperand_3_0= ruleIQLRelationalExpression )
            	    {
            	    // InternalQDL.g:4906:5: (lv_rightOperand_3_0= ruleIQLRelationalExpression )
            	    // InternalQDL.g:4907:6: lv_rightOperand_3_0= ruleIQLRelationalExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLEqualityExpressionAccess().getRightOperandIQLRelationalExpressionParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_55);
            	    lv_rightOperand_3_0=ruleIQLRelationalExpression();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						if (current==null) {
            	      							current = createModelElementForParent(grammarAccess.getIQLEqualityExpressionRule());
            	      						}
            	      						set(
            	      							current,
            	      							"rightOperand",
            	      							lv_rightOperand_3_0,
            	      							"de.uniol.inf.is.odysseus.iql.qdl.QDL.IQLRelationalExpression");
            	      						afterParserOrEnumRuleCall();
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop62;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLEqualityExpression"


    // $ANTLR start "entryRuleOpEquality"
    // InternalQDL.g:4929:1: entryRuleOpEquality returns [String current=null] : iv_ruleOpEquality= ruleOpEquality EOF ;
    public final String entryRuleOpEquality() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpEquality = null;


        try {
            // InternalQDL.g:4929:50: (iv_ruleOpEquality= ruleOpEquality EOF )
            // InternalQDL.g:4930:2: iv_ruleOpEquality= ruleOpEquality EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getOpEqualityRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleOpEquality=ruleOpEquality();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleOpEquality.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleOpEquality"


    // $ANTLR start "ruleOpEquality"
    // InternalQDL.g:4936:1: ruleOpEquality returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '==' | kw= '!=' ) ;
    public final AntlrDatatypeRuleToken ruleOpEquality() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalQDL.g:4942:2: ( (kw= '==' | kw= '!=' ) )
            // InternalQDL.g:4943:2: (kw= '==' | kw= '!=' )
            {
            // InternalQDL.g:4943:2: (kw= '==' | kw= '!=' )
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==41) ) {
                alt63=1;
            }
            else if ( (LA63_0==38) ) {
                alt63=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 63, 0, input);

                throw nvae;
            }
            switch (alt63) {
                case 1 :
                    // InternalQDL.g:4944:3: kw= '=='
                    {
                    kw=(Token)match(input,41,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpEqualityAccess().getEqualsSignEqualsSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalQDL.g:4950:3: kw= '!='
                    {
                    kw=(Token)match(input,38,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpEqualityAccess().getExclamationMarkEqualsSignKeyword_1());
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleOpEquality"


    // $ANTLR start "entryRuleOpRelational"
    // InternalQDL.g:4959:1: entryRuleOpRelational returns [String current=null] : iv_ruleOpRelational= ruleOpRelational EOF ;
    public final String entryRuleOpRelational() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpRelational = null;


        try {
            // InternalQDL.g:4959:52: (iv_ruleOpRelational= ruleOpRelational EOF )
            // InternalQDL.g:4960:2: iv_ruleOpRelational= ruleOpRelational EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getOpRelationalRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleOpRelational=ruleOpRelational();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleOpRelational.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleOpRelational"


    // $ANTLR start "ruleOpRelational"
    // InternalQDL.g:4966:1: ruleOpRelational returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '>' | kw= '>=' | kw= '<' | kw= '<=' ) ;
    public final AntlrDatatypeRuleToken ruleOpRelational() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalQDL.g:4972:2: ( (kw= '>' | kw= '>=' | kw= '<' | kw= '<=' ) )
            // InternalQDL.g:4973:2: (kw= '>' | kw= '>=' | kw= '<' | kw= '<=' )
            {
            // InternalQDL.g:4973:2: (kw= '>' | kw= '>=' | kw= '<' | kw= '<=' )
            int alt64=4;
            switch ( input.LA(1) ) {
            case 33:
                {
                alt64=1;
                }
                break;
            case 34:
                {
                alt64=2;
                }
                break;
            case 35:
                {
                alt64=3;
                }
                break;
            case 36:
                {
                alt64=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 64, 0, input);

                throw nvae;
            }

            switch (alt64) {
                case 1 :
                    // InternalQDL.g:4974:3: kw= '>'
                    {
                    kw=(Token)match(input,33,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpRelationalAccess().getGreaterThanSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalQDL.g:4980:3: kw= '>='
                    {
                    kw=(Token)match(input,34,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpRelationalAccess().getGreaterThanSignEqualsSignKeyword_1());
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalQDL.g:4986:3: kw= '<'
                    {
                    kw=(Token)match(input,35,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpRelationalAccess().getLessThanSignKeyword_2());
                      		
                    }

                    }
                    break;
                case 4 :
                    // InternalQDL.g:4992:3: kw= '<='
                    {
                    kw=(Token)match(input,36,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpRelationalAccess().getLessThanSignEqualsSignKeyword_3());
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleOpRelational"


    // $ANTLR start "entryRuleIQLAdditiveExpression"
    // InternalQDL.g:5001:1: entryRuleIQLAdditiveExpression returns [EObject current=null] : iv_ruleIQLAdditiveExpression= ruleIQLAdditiveExpression EOF ;
    public final EObject entryRuleIQLAdditiveExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLAdditiveExpression = null;


        try {
            // InternalQDL.g:5001:62: (iv_ruleIQLAdditiveExpression= ruleIQLAdditiveExpression EOF )
            // InternalQDL.g:5002:2: iv_ruleIQLAdditiveExpression= ruleIQLAdditiveExpression EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLAdditiveExpressionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLAdditiveExpression=ruleIQLAdditiveExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLAdditiveExpression; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLAdditiveExpression"


    // $ANTLR start "ruleIQLAdditiveExpression"
    // InternalQDL.g:5008:1: ruleIQLAdditiveExpression returns [EObject current=null] : (this_IQLMultiplicativeExpression_0= ruleIQLMultiplicativeExpression ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )* ) ;
    public final EObject ruleIQLAdditiveExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLMultiplicativeExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:5014:2: ( (this_IQLMultiplicativeExpression_0= ruleIQLMultiplicativeExpression ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )* ) )
            // InternalQDL.g:5015:2: (this_IQLMultiplicativeExpression_0= ruleIQLMultiplicativeExpression ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )* )
            {
            // InternalQDL.g:5015:2: (this_IQLMultiplicativeExpression_0= ruleIQLMultiplicativeExpression ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )* )
            // InternalQDL.g:5016:3: this_IQLMultiplicativeExpression_0= ruleIQLMultiplicativeExpression ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLAdditiveExpressionAccess().getIQLMultiplicativeExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_56);
            this_IQLMultiplicativeExpression_0=ruleIQLMultiplicativeExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLMultiplicativeExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalQDL.g:5024:3: ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )*
            loop65:
            do {
                int alt65=2;
                int LA65_0 = input.LA(1);

                if ( (LA65_0==21) && (synpred11_InternalQDL())) {
                    alt65=1;
                }
                else if ( (LA65_0==23) && (synpred11_InternalQDL())) {
                    alt65=1;
                }


                switch (alt65) {
            	case 1 :
            	    // InternalQDL.g:5025:4: ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) )
            	    {
            	    // InternalQDL.g:5025:4: ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) )
            	    // InternalQDL.g:5026:5: ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) )
            	    {
            	    // InternalQDL.g:5036:5: ( () ( (lv_op_2_0= ruleOpAdd ) ) )
            	    // InternalQDL.g:5037:6: () ( (lv_op_2_0= ruleOpAdd ) )
            	    {
            	    // InternalQDL.g:5037:6: ()
            	    // InternalQDL.g:5038:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    // InternalQDL.g:5044:6: ( (lv_op_2_0= ruleOpAdd ) )
            	    // InternalQDL.g:5045:7: (lv_op_2_0= ruleOpAdd )
            	    {
            	    // InternalQDL.g:5045:7: (lv_op_2_0= ruleOpAdd )
            	    // InternalQDL.g:5046:8: lv_op_2_0= ruleOpAdd
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLAdditiveExpressionAccess().getOpOpAddParserRuleCall_1_0_0_1_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_11);
            	    lv_op_2_0=ruleOpAdd();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      								if (current==null) {
            	      									current = createModelElementForParent(grammarAccess.getIQLAdditiveExpressionRule());
            	      								}
            	      								set(
            	      									current,
            	      									"op",
            	      									lv_op_2_0,
            	      									"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.OpAdd");
            	      								afterParserOrEnumRuleCall();
            	      							
            	    }

            	    }


            	    }


            	    }


            	    }

            	    // InternalQDL.g:5065:4: ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) )
            	    // InternalQDL.g:5066:5: (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression )
            	    {
            	    // InternalQDL.g:5066:5: (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression )
            	    // InternalQDL.g:5067:6: lv_rightOperand_3_0= ruleIQLMultiplicativeExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLAdditiveExpressionAccess().getRightOperandIQLMultiplicativeExpressionParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_56);
            	    lv_rightOperand_3_0=ruleIQLMultiplicativeExpression();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						if (current==null) {
            	      							current = createModelElementForParent(grammarAccess.getIQLAdditiveExpressionRule());
            	      						}
            	      						set(
            	      							current,
            	      							"rightOperand",
            	      							lv_rightOperand_3_0,
            	      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMultiplicativeExpression");
            	      						afterParserOrEnumRuleCall();
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop65;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLAdditiveExpression"


    // $ANTLR start "entryRuleOpAdd"
    // InternalQDL.g:5089:1: entryRuleOpAdd returns [String current=null] : iv_ruleOpAdd= ruleOpAdd EOF ;
    public final String entryRuleOpAdd() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpAdd = null;


        try {
            // InternalQDL.g:5089:45: (iv_ruleOpAdd= ruleOpAdd EOF )
            // InternalQDL.g:5090:2: iv_ruleOpAdd= ruleOpAdd EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getOpAddRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleOpAdd=ruleOpAdd();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleOpAdd.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleOpAdd"


    // $ANTLR start "ruleOpAdd"
    // InternalQDL.g:5096:1: ruleOpAdd returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '+' | kw= '-' ) ;
    public final AntlrDatatypeRuleToken ruleOpAdd() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalQDL.g:5102:2: ( (kw= '+' | kw= '-' ) )
            // InternalQDL.g:5103:2: (kw= '+' | kw= '-' )
            {
            // InternalQDL.g:5103:2: (kw= '+' | kw= '-' )
            int alt66=2;
            int LA66_0 = input.LA(1);

            if ( (LA66_0==21) ) {
                alt66=1;
            }
            else if ( (LA66_0==23) ) {
                alt66=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 66, 0, input);

                throw nvae;
            }
            switch (alt66) {
                case 1 :
                    // InternalQDL.g:5104:3: kw= '+'
                    {
                    kw=(Token)match(input,21,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAddAccess().getPlusSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalQDL.g:5110:3: kw= '-'
                    {
                    kw=(Token)match(input,23,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAddAccess().getHyphenMinusKeyword_1());
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleOpAdd"


    // $ANTLR start "entryRuleIQLMultiplicativeExpression"
    // InternalQDL.g:5119:1: entryRuleIQLMultiplicativeExpression returns [EObject current=null] : iv_ruleIQLMultiplicativeExpression= ruleIQLMultiplicativeExpression EOF ;
    public final EObject entryRuleIQLMultiplicativeExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMultiplicativeExpression = null;


        try {
            // InternalQDL.g:5119:68: (iv_ruleIQLMultiplicativeExpression= ruleIQLMultiplicativeExpression EOF )
            // InternalQDL.g:5120:2: iv_ruleIQLMultiplicativeExpression= ruleIQLMultiplicativeExpression EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLMultiplicativeExpressionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLMultiplicativeExpression=ruleIQLMultiplicativeExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLMultiplicativeExpression; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLMultiplicativeExpression"


    // $ANTLR start "ruleIQLMultiplicativeExpression"
    // InternalQDL.g:5126:1: ruleIQLMultiplicativeExpression returns [EObject current=null] : (this_IQLUnaryExpression_0= ruleIQLUnaryExpression ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )* ) ;
    public final EObject ruleIQLMultiplicativeExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLUnaryExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:5132:2: ( (this_IQLUnaryExpression_0= ruleIQLUnaryExpression ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )* ) )
            // InternalQDL.g:5133:2: (this_IQLUnaryExpression_0= ruleIQLUnaryExpression ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )* )
            {
            // InternalQDL.g:5133:2: (this_IQLUnaryExpression_0= ruleIQLUnaryExpression ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )* )
            // InternalQDL.g:5134:3: this_IQLUnaryExpression_0= ruleIQLUnaryExpression ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLUnaryExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_57);
            this_IQLUnaryExpression_0=ruleIQLUnaryExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLUnaryExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalQDL.g:5142:3: ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )*
            loop67:
            do {
                int alt67=2;
                int LA67_0 = input.LA(1);

                if ( (LA67_0==25) && (synpred12_InternalQDL())) {
                    alt67=1;
                }
                else if ( (LA67_0==27) && (synpred12_InternalQDL())) {
                    alt67=1;
                }
                else if ( (LA67_0==29) && (synpred12_InternalQDL())) {
                    alt67=1;
                }


                switch (alt67) {
            	case 1 :
            	    // InternalQDL.g:5143:4: ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) )
            	    {
            	    // InternalQDL.g:5143:4: ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) )
            	    // InternalQDL.g:5144:5: ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) )
            	    {
            	    // InternalQDL.g:5154:5: ( () ( (lv_op_2_0= ruleOpMulti ) ) )
            	    // InternalQDL.g:5155:6: () ( (lv_op_2_0= ruleOpMulti ) )
            	    {
            	    // InternalQDL.g:5155:6: ()
            	    // InternalQDL.g:5156:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    // InternalQDL.g:5162:6: ( (lv_op_2_0= ruleOpMulti ) )
            	    // InternalQDL.g:5163:7: (lv_op_2_0= ruleOpMulti )
            	    {
            	    // InternalQDL.g:5163:7: (lv_op_2_0= ruleOpMulti )
            	    // InternalQDL.g:5164:8: lv_op_2_0= ruleOpMulti
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLMultiplicativeExpressionAccess().getOpOpMultiParserRuleCall_1_0_0_1_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_11);
            	    lv_op_2_0=ruleOpMulti();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      								if (current==null) {
            	      									current = createModelElementForParent(grammarAccess.getIQLMultiplicativeExpressionRule());
            	      								}
            	      								set(
            	      									current,
            	      									"op",
            	      									lv_op_2_0,
            	      									"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.OpMulti");
            	      								afterParserOrEnumRuleCall();
            	      							
            	    }

            	    }


            	    }


            	    }


            	    }

            	    // InternalQDL.g:5183:4: ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) )
            	    // InternalQDL.g:5184:5: (lv_rightOperand_3_0= ruleIQLUnaryExpression )
            	    {
            	    // InternalQDL.g:5184:5: (lv_rightOperand_3_0= ruleIQLUnaryExpression )
            	    // InternalQDL.g:5185:6: lv_rightOperand_3_0= ruleIQLUnaryExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLMultiplicativeExpressionAccess().getRightOperandIQLUnaryExpressionParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_57);
            	    lv_rightOperand_3_0=ruleIQLUnaryExpression();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						if (current==null) {
            	      							current = createModelElementForParent(grammarAccess.getIQLMultiplicativeExpressionRule());
            	      						}
            	      						set(
            	      							current,
            	      							"rightOperand",
            	      							lv_rightOperand_3_0,
            	      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLUnaryExpression");
            	      						afterParserOrEnumRuleCall();
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop67;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLMultiplicativeExpression"


    // $ANTLR start "entryRuleOpMulti"
    // InternalQDL.g:5207:1: entryRuleOpMulti returns [String current=null] : iv_ruleOpMulti= ruleOpMulti EOF ;
    public final String entryRuleOpMulti() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpMulti = null;


        try {
            // InternalQDL.g:5207:47: (iv_ruleOpMulti= ruleOpMulti EOF )
            // InternalQDL.g:5208:2: iv_ruleOpMulti= ruleOpMulti EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getOpMultiRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleOpMulti=ruleOpMulti();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleOpMulti.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleOpMulti"


    // $ANTLR start "ruleOpMulti"
    // InternalQDL.g:5214:1: ruleOpMulti returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '*' | kw= '/' | kw= '%' ) ;
    public final AntlrDatatypeRuleToken ruleOpMulti() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalQDL.g:5220:2: ( (kw= '*' | kw= '/' | kw= '%' ) )
            // InternalQDL.g:5221:2: (kw= '*' | kw= '/' | kw= '%' )
            {
            // InternalQDL.g:5221:2: (kw= '*' | kw= '/' | kw= '%' )
            int alt68=3;
            switch ( input.LA(1) ) {
            case 25:
                {
                alt68=1;
                }
                break;
            case 27:
                {
                alt68=2;
                }
                break;
            case 29:
                {
                alt68=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 68, 0, input);

                throw nvae;
            }

            switch (alt68) {
                case 1 :
                    // InternalQDL.g:5222:3: kw= '*'
                    {
                    kw=(Token)match(input,25,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpMultiAccess().getAsteriskKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalQDL.g:5228:3: kw= '/'
                    {
                    kw=(Token)match(input,27,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpMultiAccess().getSolidusKeyword_1());
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalQDL.g:5234:3: kw= '%'
                    {
                    kw=(Token)match(input,29,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpMultiAccess().getPercentSignKeyword_2());
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleOpMulti"


    // $ANTLR start "entryRuleIQLUnaryExpression"
    // InternalQDL.g:5243:1: entryRuleIQLUnaryExpression returns [EObject current=null] : iv_ruleIQLUnaryExpression= ruleIQLUnaryExpression EOF ;
    public final EObject entryRuleIQLUnaryExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLUnaryExpression = null;


        try {
            // InternalQDL.g:5243:59: (iv_ruleIQLUnaryExpression= ruleIQLUnaryExpression EOF )
            // InternalQDL.g:5244:2: iv_ruleIQLUnaryExpression= ruleIQLUnaryExpression EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLUnaryExpressionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLUnaryExpression=ruleIQLUnaryExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLUnaryExpression; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLUnaryExpression"


    // $ANTLR start "ruleIQLUnaryExpression"
    // InternalQDL.g:5250:1: ruleIQLUnaryExpression returns [EObject current=null] : ( ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) ) | ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) ) | (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? ) ) ;
    public final EObject ruleIQLUnaryExpression() throws RecognitionException {
        EObject current = null;

        Token otherlv_10=null;
        Token otherlv_12=null;
        AntlrDatatypeRuleToken lv_op_1_0 = null;

        EObject lv_operand_2_0 = null;

        AntlrDatatypeRuleToken lv_op_4_0 = null;

        EObject lv_operand_5_0 = null;

        AntlrDatatypeRuleToken lv_op_7_0 = null;

        EObject lv_operand_8_0 = null;

        EObject lv_targetRef_11_0 = null;

        EObject lv_operand_13_0 = null;

        EObject this_IQLMemberCallExpression_14 = null;

        AntlrDatatypeRuleToken lv_op_16_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:5256:2: ( ( ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) ) | ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) ) | (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? ) ) )
            // InternalQDL.g:5257:2: ( ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) ) | ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) ) | (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? ) )
            {
            // InternalQDL.g:5257:2: ( ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) ) | ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) ) | (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? ) )
            int alt70=5;
            alt70 = dfa70.predict(input);
            switch (alt70) {
                case 1 :
                    // InternalQDL.g:5258:3: ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) )
                    {
                    // InternalQDL.g:5258:3: ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) )
                    // InternalQDL.g:5259:4: ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) )
                    {
                    // InternalQDL.g:5259:4: ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) )
                    // InternalQDL.g:5260:5: () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) )
                    {
                    // InternalQDL.g:5260:5: ()
                    // InternalQDL.g:5261:6: 
                    {
                    if ( state.backtracking==0 ) {

                      						current = forceCreateModelElement(
                      							grammarAccess.getIQLUnaryExpressionAccess().getIQLPlusMinusExpressionAction_0_0_0(),
                      							current);
                      					
                    }

                    }

                    // InternalQDL.g:5267:5: ( (lv_op_1_0= ruleOpUnaryPlusMinus ) )
                    // InternalQDL.g:5268:6: (lv_op_1_0= ruleOpUnaryPlusMinus )
                    {
                    // InternalQDL.g:5268:6: (lv_op_1_0= ruleOpUnaryPlusMinus )
                    // InternalQDL.g:5269:7: lv_op_1_0= ruleOpUnaryPlusMinus
                    {
                    if ( state.backtracking==0 ) {

                      							newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getOpOpUnaryPlusMinusParserRuleCall_0_0_1_0());
                      						
                    }
                    pushFollow(FOLLOW_11);
                    lv_op_1_0=ruleOpUnaryPlusMinus();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      							if (current==null) {
                      								current = createModelElementForParent(grammarAccess.getIQLUnaryExpressionRule());
                      							}
                      							set(
                      								current,
                      								"op",
                      								lv_op_1_0,
                      								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.OpUnaryPlusMinus");
                      							afterParserOrEnumRuleCall();
                      						
                    }

                    }


                    }


                    }

                    // InternalQDL.g:5287:4: ( (lv_operand_2_0= ruleIQLMemberCallExpression ) )
                    // InternalQDL.g:5288:5: (lv_operand_2_0= ruleIQLMemberCallExpression )
                    {
                    // InternalQDL.g:5288:5: (lv_operand_2_0= ruleIQLMemberCallExpression )
                    // InternalQDL.g:5289:6: lv_operand_2_0= ruleIQLMemberCallExpression
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getOperandIQLMemberCallExpressionParserRuleCall_0_1_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_operand_2_0=ruleIQLMemberCallExpression();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLUnaryExpressionRule());
                      						}
                      						set(
                      							current,
                      							"operand",
                      							lv_operand_2_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMemberCallExpression");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalQDL.g:5308:3: ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) )
                    {
                    // InternalQDL.g:5308:3: ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) )
                    // InternalQDL.g:5309:4: ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) )
                    {
                    // InternalQDL.g:5309:4: ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) )
                    // InternalQDL.g:5310:5: () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) )
                    {
                    // InternalQDL.g:5310:5: ()
                    // InternalQDL.g:5311:6: 
                    {
                    if ( state.backtracking==0 ) {

                      						current = forceCreateModelElement(
                      							grammarAccess.getIQLUnaryExpressionAccess().getIQLBooleanNotExpressionAction_1_0_0(),
                      							current);
                      					
                    }

                    }

                    // InternalQDL.g:5317:5: ( (lv_op_4_0= ruleOpUnaryBooleanNot ) )
                    // InternalQDL.g:5318:6: (lv_op_4_0= ruleOpUnaryBooleanNot )
                    {
                    // InternalQDL.g:5318:6: (lv_op_4_0= ruleOpUnaryBooleanNot )
                    // InternalQDL.g:5319:7: lv_op_4_0= ruleOpUnaryBooleanNot
                    {
                    if ( state.backtracking==0 ) {

                      							newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getOpOpUnaryBooleanNotParserRuleCall_1_0_1_0());
                      						
                    }
                    pushFollow(FOLLOW_11);
                    lv_op_4_0=ruleOpUnaryBooleanNot();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      							if (current==null) {
                      								current = createModelElementForParent(grammarAccess.getIQLUnaryExpressionRule());
                      							}
                      							set(
                      								current,
                      								"op",
                      								lv_op_4_0,
                      								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.OpUnaryBooleanNot");
                      							afterParserOrEnumRuleCall();
                      						
                    }

                    }


                    }


                    }

                    // InternalQDL.g:5337:4: ( (lv_operand_5_0= ruleIQLMemberCallExpression ) )
                    // InternalQDL.g:5338:5: (lv_operand_5_0= ruleIQLMemberCallExpression )
                    {
                    // InternalQDL.g:5338:5: (lv_operand_5_0= ruleIQLMemberCallExpression )
                    // InternalQDL.g:5339:6: lv_operand_5_0= ruleIQLMemberCallExpression
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getOperandIQLMemberCallExpressionParserRuleCall_1_1_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_operand_5_0=ruleIQLMemberCallExpression();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLUnaryExpressionRule());
                      						}
                      						set(
                      							current,
                      							"operand",
                      							lv_operand_5_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMemberCallExpression");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalQDL.g:5358:3: ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) )
                    {
                    // InternalQDL.g:5358:3: ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) )
                    // InternalQDL.g:5359:4: ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) )
                    {
                    // InternalQDL.g:5359:4: ( () ( (lv_op_7_0= ruleOpPrefix ) ) )
                    // InternalQDL.g:5360:5: () ( (lv_op_7_0= ruleOpPrefix ) )
                    {
                    // InternalQDL.g:5360:5: ()
                    // InternalQDL.g:5361:6: 
                    {
                    if ( state.backtracking==0 ) {

                      						current = forceCreateModelElement(
                      							grammarAccess.getIQLUnaryExpressionAccess().getIQLPrefixExpressionAction_2_0_0(),
                      							current);
                      					
                    }

                    }

                    // InternalQDL.g:5367:5: ( (lv_op_7_0= ruleOpPrefix ) )
                    // InternalQDL.g:5368:6: (lv_op_7_0= ruleOpPrefix )
                    {
                    // InternalQDL.g:5368:6: (lv_op_7_0= ruleOpPrefix )
                    // InternalQDL.g:5369:7: lv_op_7_0= ruleOpPrefix
                    {
                    if ( state.backtracking==0 ) {

                      							newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getOpOpPrefixParserRuleCall_2_0_1_0());
                      						
                    }
                    pushFollow(FOLLOW_11);
                    lv_op_7_0=ruleOpPrefix();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      							if (current==null) {
                      								current = createModelElementForParent(grammarAccess.getIQLUnaryExpressionRule());
                      							}
                      							set(
                      								current,
                      								"op",
                      								lv_op_7_0,
                      								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.OpPrefix");
                      							afterParserOrEnumRuleCall();
                      						
                    }

                    }


                    }


                    }

                    // InternalQDL.g:5387:4: ( (lv_operand_8_0= ruleIQLMemberCallExpression ) )
                    // InternalQDL.g:5388:5: (lv_operand_8_0= ruleIQLMemberCallExpression )
                    {
                    // InternalQDL.g:5388:5: (lv_operand_8_0= ruleIQLMemberCallExpression )
                    // InternalQDL.g:5389:6: lv_operand_8_0= ruleIQLMemberCallExpression
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getOperandIQLMemberCallExpressionParserRuleCall_2_1_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_operand_8_0=ruleIQLMemberCallExpression();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLUnaryExpressionRule());
                      						}
                      						set(
                      							current,
                      							"operand",
                      							lv_operand_8_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMemberCallExpression");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 4 :
                    // InternalQDL.g:5408:3: ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) )
                    {
                    // InternalQDL.g:5408:3: ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) )
                    // InternalQDL.g:5409:4: ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) )
                    {
                    // InternalQDL.g:5409:4: ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) )
                    // InternalQDL.g:5410:5: ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' )
                    {
                    // InternalQDL.g:5422:5: ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' )
                    // InternalQDL.g:5423:6: () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')'
                    {
                    // InternalQDL.g:5423:6: ()
                    // InternalQDL.g:5424:7: 
                    {
                    if ( state.backtracking==0 ) {

                      							current = forceCreateModelElement(
                      								grammarAccess.getIQLUnaryExpressionAccess().getIQLTypeCastExpressionAction_3_0_0_0(),
                      								current);
                      						
                    }

                    }

                    otherlv_10=(Token)match(input,14,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(otherlv_10, grammarAccess.getIQLUnaryExpressionAccess().getLeftParenthesisKeyword_3_0_0_1());
                      					
                    }
                    // InternalQDL.g:5434:6: ( (lv_targetRef_11_0= ruleJvmTypeReference ) )
                    // InternalQDL.g:5435:7: (lv_targetRef_11_0= ruleJvmTypeReference )
                    {
                    // InternalQDL.g:5435:7: (lv_targetRef_11_0= ruleJvmTypeReference )
                    // InternalQDL.g:5436:8: lv_targetRef_11_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      								newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getTargetRefJvmTypeReferenceParserRuleCall_3_0_0_2_0());
                      							
                    }
                    pushFollow(FOLLOW_9);
                    lv_targetRef_11_0=ruleJvmTypeReference();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      								if (current==null) {
                      									current = createModelElementForParent(grammarAccess.getIQLUnaryExpressionRule());
                      								}
                      								set(
                      									current,
                      									"targetRef",
                      									lv_targetRef_11_0,
                      									"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference");
                      								afterParserOrEnumRuleCall();
                      							
                    }

                    }


                    }

                    otherlv_12=(Token)match(input,15,FOLLOW_11); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(otherlv_12, grammarAccess.getIQLUnaryExpressionAccess().getRightParenthesisKeyword_3_0_0_3());
                      					
                    }

                    }


                    }

                    // InternalQDL.g:5459:4: ( (lv_operand_13_0= ruleIQLMemberCallExpression ) )
                    // InternalQDL.g:5460:5: (lv_operand_13_0= ruleIQLMemberCallExpression )
                    {
                    // InternalQDL.g:5460:5: (lv_operand_13_0= ruleIQLMemberCallExpression )
                    // InternalQDL.g:5461:6: lv_operand_13_0= ruleIQLMemberCallExpression
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getOperandIQLMemberCallExpressionParserRuleCall_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_operand_13_0=ruleIQLMemberCallExpression();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLUnaryExpressionRule());
                      						}
                      						set(
                      							current,
                      							"operand",
                      							lv_operand_13_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMemberCallExpression");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 5 :
                    // InternalQDL.g:5480:3: (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? )
                    {
                    // InternalQDL.g:5480:3: (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? )
                    // InternalQDL.g:5481:4: this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )?
                    {
                    if ( state.backtracking==0 ) {

                      				newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getIQLMemberCallExpressionParserRuleCall_4_0());
                      			
                    }
                    pushFollow(FOLLOW_58);
                    this_IQLMemberCallExpression_14=ruleIQLMemberCallExpression();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = this_IQLMemberCallExpression_14;
                      				afterParserOrEnumRuleCall();
                      			
                    }
                    // InternalQDL.g:5489:4: ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )?
                    int alt69=2;
                    int LA69_0 = input.LA(1);

                    if ( (LA69_0==31) && (synpred14_InternalQDL())) {
                        alt69=1;
                    }
                    else if ( (LA69_0==32) && (synpred14_InternalQDL())) {
                        alt69=1;
                    }
                    switch (alt69) {
                        case 1 :
                            // InternalQDL.g:5490:5: ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) )
                            {
                            // InternalQDL.g:5500:5: ( () ( (lv_op_16_0= ruleOpPostfix ) ) )
                            // InternalQDL.g:5501:6: () ( (lv_op_16_0= ruleOpPostfix ) )
                            {
                            // InternalQDL.g:5501:6: ()
                            // InternalQDL.g:5502:7: 
                            {
                            if ( state.backtracking==0 ) {

                              							current = forceCreateModelElementAndSet(
                              								grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0(),
                              								current);
                              						
                            }

                            }

                            // InternalQDL.g:5508:6: ( (lv_op_16_0= ruleOpPostfix ) )
                            // InternalQDL.g:5509:7: (lv_op_16_0= ruleOpPostfix )
                            {
                            // InternalQDL.g:5509:7: (lv_op_16_0= ruleOpPostfix )
                            // InternalQDL.g:5510:8: lv_op_16_0= ruleOpPostfix
                            {
                            if ( state.backtracking==0 ) {

                              								newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getOpOpPostfixParserRuleCall_4_1_0_1_0());
                              							
                            }
                            pushFollow(FOLLOW_2);
                            lv_op_16_0=ruleOpPostfix();

                            state._fsp--;
                            if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              								if (current==null) {
                              									current = createModelElementForParent(grammarAccess.getIQLUnaryExpressionRule());
                              								}
                              								set(
                              									current,
                              									"op",
                              									lv_op_16_0,
                              									"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.OpPostfix");
                              								afterParserOrEnumRuleCall();
                              							
                            }

                            }


                            }


                            }


                            }
                            break;

                    }


                    }


                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLUnaryExpression"


    // $ANTLR start "entryRuleOpUnaryPlusMinus"
    // InternalQDL.g:5534:1: entryRuleOpUnaryPlusMinus returns [String current=null] : iv_ruleOpUnaryPlusMinus= ruleOpUnaryPlusMinus EOF ;
    public final String entryRuleOpUnaryPlusMinus() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpUnaryPlusMinus = null;


        try {
            // InternalQDL.g:5534:56: (iv_ruleOpUnaryPlusMinus= ruleOpUnaryPlusMinus EOF )
            // InternalQDL.g:5535:2: iv_ruleOpUnaryPlusMinus= ruleOpUnaryPlusMinus EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getOpUnaryPlusMinusRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleOpUnaryPlusMinus=ruleOpUnaryPlusMinus();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleOpUnaryPlusMinus.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleOpUnaryPlusMinus"


    // $ANTLR start "ruleOpUnaryPlusMinus"
    // InternalQDL.g:5541:1: ruleOpUnaryPlusMinus returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '-' | kw= '+' ) ;
    public final AntlrDatatypeRuleToken ruleOpUnaryPlusMinus() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalQDL.g:5547:2: ( (kw= '-' | kw= '+' ) )
            // InternalQDL.g:5548:2: (kw= '-' | kw= '+' )
            {
            // InternalQDL.g:5548:2: (kw= '-' | kw= '+' )
            int alt71=2;
            int LA71_0 = input.LA(1);

            if ( (LA71_0==23) ) {
                alt71=1;
            }
            else if ( (LA71_0==21) ) {
                alt71=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 71, 0, input);

                throw nvae;
            }
            switch (alt71) {
                case 1 :
                    // InternalQDL.g:5549:3: kw= '-'
                    {
                    kw=(Token)match(input,23,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpUnaryPlusMinusAccess().getHyphenMinusKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalQDL.g:5555:3: kw= '+'
                    {
                    kw=(Token)match(input,21,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpUnaryPlusMinusAccess().getPlusSignKeyword_1());
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleOpUnaryPlusMinus"


    // $ANTLR start "entryRuleOpUnaryBooleanNot"
    // InternalQDL.g:5564:1: entryRuleOpUnaryBooleanNot returns [String current=null] : iv_ruleOpUnaryBooleanNot= ruleOpUnaryBooleanNot EOF ;
    public final String entryRuleOpUnaryBooleanNot() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpUnaryBooleanNot = null;


        try {
            // InternalQDL.g:5564:57: (iv_ruleOpUnaryBooleanNot= ruleOpUnaryBooleanNot EOF )
            // InternalQDL.g:5565:2: iv_ruleOpUnaryBooleanNot= ruleOpUnaryBooleanNot EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getOpUnaryBooleanNotRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleOpUnaryBooleanNot=ruleOpUnaryBooleanNot();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleOpUnaryBooleanNot.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleOpUnaryBooleanNot"


    // $ANTLR start "ruleOpUnaryBooleanNot"
    // InternalQDL.g:5571:1: ruleOpUnaryBooleanNot returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= '!' ;
    public final AntlrDatatypeRuleToken ruleOpUnaryBooleanNot() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalQDL.g:5577:2: (kw= '!' )
            // InternalQDL.g:5578:2: kw= '!'
            {
            kw=(Token)match(input,37,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              		current.merge(kw);
              		newLeafNode(kw, grammarAccess.getOpUnaryBooleanNotAccess().getExclamationMarkKeyword());
              	
            }

            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleOpUnaryBooleanNot"


    // $ANTLR start "entryRuleOpPrefix"
    // InternalQDL.g:5586:1: entryRuleOpPrefix returns [String current=null] : iv_ruleOpPrefix= ruleOpPrefix EOF ;
    public final String entryRuleOpPrefix() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpPrefix = null;


        try {
            // InternalQDL.g:5586:48: (iv_ruleOpPrefix= ruleOpPrefix EOF )
            // InternalQDL.g:5587:2: iv_ruleOpPrefix= ruleOpPrefix EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getOpPrefixRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleOpPrefix=ruleOpPrefix();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleOpPrefix.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleOpPrefix"


    // $ANTLR start "ruleOpPrefix"
    // InternalQDL.g:5593:1: ruleOpPrefix returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '++' | kw= '--' ) ;
    public final AntlrDatatypeRuleToken ruleOpPrefix() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalQDL.g:5599:2: ( (kw= '++' | kw= '--' ) )
            // InternalQDL.g:5600:2: (kw= '++' | kw= '--' )
            {
            // InternalQDL.g:5600:2: (kw= '++' | kw= '--' )
            int alt72=2;
            int LA72_0 = input.LA(1);

            if ( (LA72_0==31) ) {
                alt72=1;
            }
            else if ( (LA72_0==32) ) {
                alt72=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 72, 0, input);

                throw nvae;
            }
            switch (alt72) {
                case 1 :
                    // InternalQDL.g:5601:3: kw= '++'
                    {
                    kw=(Token)match(input,31,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpPrefixAccess().getPlusSignPlusSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalQDL.g:5607:3: kw= '--'
                    {
                    kw=(Token)match(input,32,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpPrefixAccess().getHyphenMinusHyphenMinusKeyword_1());
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleOpPrefix"


    // $ANTLR start "entryRuleOpPostfix"
    // InternalQDL.g:5616:1: entryRuleOpPostfix returns [String current=null] : iv_ruleOpPostfix= ruleOpPostfix EOF ;
    public final String entryRuleOpPostfix() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpPostfix = null;


        try {
            // InternalQDL.g:5616:49: (iv_ruleOpPostfix= ruleOpPostfix EOF )
            // InternalQDL.g:5617:2: iv_ruleOpPostfix= ruleOpPostfix EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getOpPostfixRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleOpPostfix=ruleOpPostfix();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleOpPostfix.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleOpPostfix"


    // $ANTLR start "ruleOpPostfix"
    // InternalQDL.g:5623:1: ruleOpPostfix returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '++' | kw= '--' ) ;
    public final AntlrDatatypeRuleToken ruleOpPostfix() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalQDL.g:5629:2: ( (kw= '++' | kw= '--' ) )
            // InternalQDL.g:5630:2: (kw= '++' | kw= '--' )
            {
            // InternalQDL.g:5630:2: (kw= '++' | kw= '--' )
            int alt73=2;
            int LA73_0 = input.LA(1);

            if ( (LA73_0==31) ) {
                alt73=1;
            }
            else if ( (LA73_0==32) ) {
                alt73=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 73, 0, input);

                throw nvae;
            }
            switch (alt73) {
                case 1 :
                    // InternalQDL.g:5631:3: kw= '++'
                    {
                    kw=(Token)match(input,31,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpPostfixAccess().getPlusSignPlusSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalQDL.g:5637:3: kw= '--'
                    {
                    kw=(Token)match(input,32,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpPostfixAccess().getHyphenMinusHyphenMinusKeyword_1());
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleOpPostfix"


    // $ANTLR start "entryRuleIQLMemberCallExpression"
    // InternalQDL.g:5646:1: entryRuleIQLMemberCallExpression returns [EObject current=null] : iv_ruleIQLMemberCallExpression= ruleIQLMemberCallExpression EOF ;
    public final EObject entryRuleIQLMemberCallExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMemberCallExpression = null;


        try {
            // InternalQDL.g:5646:64: (iv_ruleIQLMemberCallExpression= ruleIQLMemberCallExpression EOF )
            // InternalQDL.g:5647:2: iv_ruleIQLMemberCallExpression= ruleIQLMemberCallExpression EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLMemberCallExpressionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLMemberCallExpression=ruleIQLMemberCallExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLMemberCallExpression; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLMemberCallExpression"


    // $ANTLR start "ruleIQLMemberCallExpression"
    // InternalQDL.g:5653:1: ruleIQLMemberCallExpression returns [EObject current=null] : (this_IQLOtherExpressions_0= ruleIQLOtherExpressions ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )* ) ;
    public final EObject ruleIQLMemberCallExpression() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        EObject this_IQLOtherExpressions_0 = null;

        EObject lv_expressions_3_0 = null;

        EObject lv_expressions_5_0 = null;

        EObject lv_sel_9_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:5659:2: ( (this_IQLOtherExpressions_0= ruleIQLOtherExpressions ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )* ) )
            // InternalQDL.g:5660:2: (this_IQLOtherExpressions_0= ruleIQLOtherExpressions ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )* )
            {
            // InternalQDL.g:5660:2: (this_IQLOtherExpressions_0= ruleIQLOtherExpressions ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )* )
            // InternalQDL.g:5661:3: this_IQLOtherExpressions_0= ruleIQLOtherExpressions ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLMemberCallExpressionAccess().getIQLOtherExpressionsParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_59);
            this_IQLOtherExpressions_0=ruleIQLOtherExpressions();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLOtherExpressions_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalQDL.g:5669:3: ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )*
            loop75:
            do {
                int alt75=3;
                int LA75_0 = input.LA(1);

                if ( (LA75_0==57) && (synpred15_InternalQDL())) {
                    alt75=1;
                }
                else if ( (LA75_0==61) && (synpred16_InternalQDL())) {
                    alt75=2;
                }


                switch (alt75) {
            	case 1 :
            	    // InternalQDL.g:5670:4: ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) )
            	    {
            	    // InternalQDL.g:5670:4: ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) )
            	    // InternalQDL.g:5671:5: ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' )
            	    {
            	    // InternalQDL.g:5691:5: ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' )
            	    // InternalQDL.g:5692:6: () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']'
            	    {
            	    // InternalQDL.g:5692:6: ()
            	    // InternalQDL.g:5693:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    otherlv_2=(Token)match(input,57,FOLLOW_11); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						newLeafNode(otherlv_2, grammarAccess.getIQLMemberCallExpressionAccess().getLeftSquareBracketKeyword_1_0_0_1());
            	      					
            	    }
            	    // InternalQDL.g:5703:6: ( (lv_expressions_3_0= ruleIQLExpression ) )
            	    // InternalQDL.g:5704:7: (lv_expressions_3_0= ruleIQLExpression )
            	    {
            	    // InternalQDL.g:5704:7: (lv_expressions_3_0= ruleIQLExpression )
            	    // InternalQDL.g:5705:8: lv_expressions_3_0= ruleIQLExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLMemberCallExpressionAccess().getExpressionsIQLExpressionParserRuleCall_1_0_0_2_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_36);
            	    lv_expressions_3_0=ruleIQLExpression();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      								if (current==null) {
            	      									current = createModelElementForParent(grammarAccess.getIQLMemberCallExpressionRule());
            	      								}
            	      								add(
            	      									current,
            	      									"expressions",
            	      									lv_expressions_3_0,
            	      									"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
            	      								afterParserOrEnumRuleCall();
            	      							
            	    }

            	    }


            	    }

            	    // InternalQDL.g:5722:6: (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )*
            	    loop74:
            	    do {
            	        int alt74=2;
            	        int LA74_0 = input.LA(1);

            	        if ( (LA74_0==63) ) {
            	            alt74=1;
            	        }


            	        switch (alt74) {
            	    	case 1 :
            	    	    // InternalQDL.g:5723:7: otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) )
            	    	    {
            	    	    otherlv_4=(Token)match(input,63,FOLLOW_11); if (state.failed) return current;
            	    	    if ( state.backtracking==0 ) {

            	    	      							newLeafNode(otherlv_4, grammarAccess.getIQLMemberCallExpressionAccess().getCommaKeyword_1_0_0_3_0());
            	    	      						
            	    	    }
            	    	    // InternalQDL.g:5727:7: ( (lv_expressions_5_0= ruleIQLExpression ) )
            	    	    // InternalQDL.g:5728:8: (lv_expressions_5_0= ruleIQLExpression )
            	    	    {
            	    	    // InternalQDL.g:5728:8: (lv_expressions_5_0= ruleIQLExpression )
            	    	    // InternalQDL.g:5729:9: lv_expressions_5_0= ruleIQLExpression
            	    	    {
            	    	    if ( state.backtracking==0 ) {

            	    	      									newCompositeNode(grammarAccess.getIQLMemberCallExpressionAccess().getExpressionsIQLExpressionParserRuleCall_1_0_0_3_1_0());
            	    	      								
            	    	    }
            	    	    pushFollow(FOLLOW_36);
            	    	    lv_expressions_5_0=ruleIQLExpression();

            	    	    state._fsp--;
            	    	    if (state.failed) return current;
            	    	    if ( state.backtracking==0 ) {

            	    	      									if (current==null) {
            	    	      										current = createModelElementForParent(grammarAccess.getIQLMemberCallExpressionRule());
            	    	      									}
            	    	      									add(
            	    	      										current,
            	    	      										"expressions",
            	    	      										lv_expressions_5_0,
            	    	      										"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
            	    	      									afterParserOrEnumRuleCall();
            	    	      								
            	    	    }

            	    	    }


            	    	    }


            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop74;
            	        }
            	    } while (true);

            	    otherlv_6=(Token)match(input,58,FOLLOW_59); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						newLeafNode(otherlv_6, grammarAccess.getIQLMemberCallExpressionAccess().getRightSquareBracketKeyword_1_0_0_4());
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;
            	case 2 :
            	    // InternalQDL.g:5754:4: ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) )
            	    {
            	    // InternalQDL.g:5754:4: ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) )
            	    // InternalQDL.g:5755:5: ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) )
            	    {
            	    // InternalQDL.g:5755:5: ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) )
            	    // InternalQDL.g:5756:6: ( ( () '.' ) )=> ( () otherlv_8= '.' )
            	    {
            	    // InternalQDL.g:5762:6: ( () otherlv_8= '.' )
            	    // InternalQDL.g:5763:7: () otherlv_8= '.'
            	    {
            	    // InternalQDL.g:5763:7: ()
            	    // InternalQDL.g:5764:8: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      								current = forceCreateModelElementAndSet(
            	      									grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0(),
            	      									current);
            	      							
            	    }

            	    }

            	    otherlv_8=(Token)match(input,61,FOLLOW_6); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      							newLeafNode(otherlv_8, grammarAccess.getIQLMemberCallExpressionAccess().getFullStopKeyword_1_1_0_0_1());
            	      						
            	    }

            	    }


            	    }

            	    // InternalQDL.g:5776:5: ( (lv_sel_9_0= ruleIQLMemberSelection ) )
            	    // InternalQDL.g:5777:6: (lv_sel_9_0= ruleIQLMemberSelection )
            	    {
            	    // InternalQDL.g:5777:6: (lv_sel_9_0= ruleIQLMemberSelection )
            	    // InternalQDL.g:5778:7: lv_sel_9_0= ruleIQLMemberSelection
            	    {
            	    if ( state.backtracking==0 ) {

            	      							newCompositeNode(grammarAccess.getIQLMemberCallExpressionAccess().getSelIQLMemberSelectionParserRuleCall_1_1_1_0());
            	      						
            	    }
            	    pushFollow(FOLLOW_59);
            	    lv_sel_9_0=ruleIQLMemberSelection();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      							if (current==null) {
            	      								current = createModelElementForParent(grammarAccess.getIQLMemberCallExpressionRule());
            	      							}
            	      							set(
            	      								current,
            	      								"sel",
            	      								lv_sel_9_0,
            	      								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMemberSelection");
            	      							afterParserOrEnumRuleCall();
            	      						
            	    }

            	    }


            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop75;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLMemberCallExpression"


    // $ANTLR start "entryRuleIQLMemberSelection"
    // InternalQDL.g:5801:1: entryRuleIQLMemberSelection returns [EObject current=null] : iv_ruleIQLMemberSelection= ruleIQLMemberSelection EOF ;
    public final EObject entryRuleIQLMemberSelection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMemberSelection = null;


        try {
            // InternalQDL.g:5801:59: (iv_ruleIQLMemberSelection= ruleIQLMemberSelection EOF )
            // InternalQDL.g:5802:2: iv_ruleIQLMemberSelection= ruleIQLMemberSelection EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLMemberSelectionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLMemberSelection=ruleIQLMemberSelection();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLMemberSelection; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLMemberSelection"


    // $ANTLR start "ruleIQLMemberSelection"
    // InternalQDL.g:5808:1: ruleIQLMemberSelection returns [EObject current=null] : ( ( (otherlv_0= RULE_ID ) ) ( (lv_args_1_0= ruleIQLArgumentsList ) )? ) ;
    public final EObject ruleIQLMemberSelection() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        EObject lv_args_1_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:5814:2: ( ( ( (otherlv_0= RULE_ID ) ) ( (lv_args_1_0= ruleIQLArgumentsList ) )? ) )
            // InternalQDL.g:5815:2: ( ( (otherlv_0= RULE_ID ) ) ( (lv_args_1_0= ruleIQLArgumentsList ) )? )
            {
            // InternalQDL.g:5815:2: ( ( (otherlv_0= RULE_ID ) ) ( (lv_args_1_0= ruleIQLArgumentsList ) )? )
            // InternalQDL.g:5816:3: ( (otherlv_0= RULE_ID ) ) ( (lv_args_1_0= ruleIQLArgumentsList ) )?
            {
            // InternalQDL.g:5816:3: ( (otherlv_0= RULE_ID ) )
            // InternalQDL.g:5817:4: (otherlv_0= RULE_ID )
            {
            // InternalQDL.g:5817:4: (otherlv_0= RULE_ID )
            // InternalQDL.g:5818:5: otherlv_0= RULE_ID
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLMemberSelectionRule());
              					}
              				
            }
            otherlv_0=(Token)match(input,RULE_ID,FOLLOW_60); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(otherlv_0, grammarAccess.getIQLMemberSelectionAccess().getMemberJvmMemberCrossReference_0_0());
              				
            }

            }


            }

            // InternalQDL.g:5829:3: ( (lv_args_1_0= ruleIQLArgumentsList ) )?
            int alt76=2;
            int LA76_0 = input.LA(1);

            if ( (LA76_0==14) ) {
                alt76=1;
            }
            switch (alt76) {
                case 1 :
                    // InternalQDL.g:5830:4: (lv_args_1_0= ruleIQLArgumentsList )
                    {
                    // InternalQDL.g:5830:4: (lv_args_1_0= ruleIQLArgumentsList )
                    // InternalQDL.g:5831:5: lv_args_1_0= ruleIQLArgumentsList
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getIQLMemberSelectionAccess().getArgsIQLArgumentsListParserRuleCall_1_0());
                      				
                    }
                    pushFollow(FOLLOW_2);
                    lv_args_1_0=ruleIQLArgumentsList();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getIQLMemberSelectionRule());
                      					}
                      					set(
                      						current,
                      						"args",
                      						lv_args_1_0,
                      						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLArgumentsList");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLMemberSelection"


    // $ANTLR start "entryRuleIQLOtherExpressions"
    // InternalQDL.g:5852:1: entryRuleIQLOtherExpressions returns [EObject current=null] : iv_ruleIQLOtherExpressions= ruleIQLOtherExpressions EOF ;
    public final EObject entryRuleIQLOtherExpressions() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLOtherExpressions = null;


        try {
            // InternalQDL.g:5852:60: (iv_ruleIQLOtherExpressions= ruleIQLOtherExpressions EOF )
            // InternalQDL.g:5853:2: iv_ruleIQLOtherExpressions= ruleIQLOtherExpressions EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLOtherExpressionsRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLOtherExpressions=ruleIQLOtherExpressions();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLOtherExpressions; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLOtherExpressions"


    // $ANTLR start "ruleIQLOtherExpressions"
    // InternalQDL.g:5859:1: ruleIQLOtherExpressions returns [EObject current=null] : ( ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? ) | ( () otherlv_4= 'this' ) | ( () otherlv_6= 'super' ) | ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ) | ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) ) | this_IQLLiteralExpression_18= ruleIQLLiteralExpression ) ;
    public final EObject ruleIQLOtherExpressions() throws RecognitionException {
        EObject current = null;

        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        Token otherlv_10=null;
        Token otherlv_12=null;
        EObject lv_args_2_0 = null;

        EObject lv_expr_9_0 = null;

        EObject lv_ref_13_0 = null;

        EObject lv_ref_14_0 = null;

        EObject lv_argsList_15_0 = null;

        EObject lv_argsMap_16_0 = null;

        EObject lv_argsMap_17_0 = null;

        EObject this_IQLLiteralExpression_18 = null;



        	enterRule();

        try {
            // InternalQDL.g:5865:2: ( ( ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? ) | ( () otherlv_4= 'this' ) | ( () otherlv_6= 'super' ) | ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ) | ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) ) | this_IQLLiteralExpression_18= ruleIQLLiteralExpression ) )
            // InternalQDL.g:5866:2: ( ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? ) | ( () otherlv_4= 'this' ) | ( () otherlv_6= 'super' ) | ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ) | ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) ) | this_IQLLiteralExpression_18= ruleIQLLiteralExpression )
            {
            // InternalQDL.g:5866:2: ( ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? ) | ( () otherlv_4= 'this' ) | ( () otherlv_6= 'super' ) | ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ) | ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) ) | this_IQLLiteralExpression_18= ruleIQLLiteralExpression )
            int alt81=6;
            switch ( input.LA(1) ) {
            case RULE_ID:
                {
                alt81=1;
                }
                break;
            case 79:
                {
                alt81=2;
                }
                break;
            case 80:
                {
                alt81=3;
                }
                break;
            case 14:
                {
                alt81=4;
                }
                break;
            case 84:
                {
                alt81=5;
                }
                break;
            case RULE_INT:
            case RULE_DOUBLE:
            case RULE_STRING:
            case RULE_RANGE:
            case 20:
            case 57:
            case 85:
            case 111:
            case 112:
                {
                alt81=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 81, 0, input);

                throw nvae;
            }

            switch (alt81) {
                case 1 :
                    // InternalQDL.g:5867:3: ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? )
                    {
                    // InternalQDL.g:5867:3: ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? )
                    // InternalQDL.g:5868:4: () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )?
                    {
                    // InternalQDL.g:5868:4: ()
                    // InternalQDL.g:5869:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLOtherExpressionsAccess().getIQLJvmElementCallExpressionAction_0_0(),
                      						current);
                      				
                    }

                    }

                    // InternalQDL.g:5875:4: ( ( ruleQualifiedName ) )
                    // InternalQDL.g:5876:5: ( ruleQualifiedName )
                    {
                    // InternalQDL.g:5876:5: ( ruleQualifiedName )
                    // InternalQDL.g:5877:6: ruleQualifiedName
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getIQLOtherExpressionsRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLOtherExpressionsAccess().getElementJvmIdentifiableElementCrossReference_0_1_0());
                      					
                    }
                    pushFollow(FOLLOW_60);
                    ruleQualifiedName();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalQDL.g:5891:4: ( (lv_args_2_0= ruleIQLArgumentsList ) )?
                    int alt77=2;
                    int LA77_0 = input.LA(1);

                    if ( (LA77_0==14) ) {
                        alt77=1;
                    }
                    switch (alt77) {
                        case 1 :
                            // InternalQDL.g:5892:5: (lv_args_2_0= ruleIQLArgumentsList )
                            {
                            // InternalQDL.g:5892:5: (lv_args_2_0= ruleIQLArgumentsList )
                            // InternalQDL.g:5893:6: lv_args_2_0= ruleIQLArgumentsList
                            {
                            if ( state.backtracking==0 ) {

                              						newCompositeNode(grammarAccess.getIQLOtherExpressionsAccess().getArgsIQLArgumentsListParserRuleCall_0_2_0());
                              					
                            }
                            pushFollow(FOLLOW_2);
                            lv_args_2_0=ruleIQLArgumentsList();

                            state._fsp--;
                            if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              						if (current==null) {
                              							current = createModelElementForParent(grammarAccess.getIQLOtherExpressionsRule());
                              						}
                              						set(
                              							current,
                              							"args",
                              							lv_args_2_0,
                              							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLArgumentsList");
                              						afterParserOrEnumRuleCall();
                              					
                            }

                            }


                            }
                            break;

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalQDL.g:5912:3: ( () otherlv_4= 'this' )
                    {
                    // InternalQDL.g:5912:3: ( () otherlv_4= 'this' )
                    // InternalQDL.g:5913:4: () otherlv_4= 'this'
                    {
                    // InternalQDL.g:5913:4: ()
                    // InternalQDL.g:5914:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLOtherExpressionsAccess().getIQLThisExpressionAction_1_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_4=(Token)match(input,79,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_4, grammarAccess.getIQLOtherExpressionsAccess().getThisKeyword_1_1());
                      			
                    }

                    }


                    }
                    break;
                case 3 :
                    // InternalQDL.g:5926:3: ( () otherlv_6= 'super' )
                    {
                    // InternalQDL.g:5926:3: ( () otherlv_6= 'super' )
                    // InternalQDL.g:5927:4: () otherlv_6= 'super'
                    {
                    // InternalQDL.g:5927:4: ()
                    // InternalQDL.g:5928:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLOtherExpressionsAccess().getIQLSuperExpressionAction_2_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_6=(Token)match(input,80,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_6, grammarAccess.getIQLOtherExpressionsAccess().getSuperKeyword_2_1());
                      			
                    }

                    }


                    }
                    break;
                case 4 :
                    // InternalQDL.g:5940:3: ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' )
                    {
                    // InternalQDL.g:5940:3: ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' )
                    // InternalQDL.g:5941:4: () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')'
                    {
                    // InternalQDL.g:5941:4: ()
                    // InternalQDL.g:5942:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLOtherExpressionsAccess().getIQLParenthesisExpressionAction_3_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_8=(Token)match(input,14,FOLLOW_11); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_8, grammarAccess.getIQLOtherExpressionsAccess().getLeftParenthesisKeyword_3_1());
                      			
                    }
                    // InternalQDL.g:5952:4: ( (lv_expr_9_0= ruleIQLExpression ) )
                    // InternalQDL.g:5953:5: (lv_expr_9_0= ruleIQLExpression )
                    {
                    // InternalQDL.g:5953:5: (lv_expr_9_0= ruleIQLExpression )
                    // InternalQDL.g:5954:6: lv_expr_9_0= ruleIQLExpression
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLOtherExpressionsAccess().getExprIQLExpressionParserRuleCall_3_2_0());
                      					
                    }
                    pushFollow(FOLLOW_9);
                    lv_expr_9_0=ruleIQLExpression();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLOtherExpressionsRule());
                      						}
                      						set(
                      							current,
                      							"expr",
                      							lv_expr_9_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    otherlv_10=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_10, grammarAccess.getIQLOtherExpressionsAccess().getRightParenthesisKeyword_3_3());
                      			
                    }

                    }


                    }
                    break;
                case 5 :
                    // InternalQDL.g:5977:3: ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) )
                    {
                    // InternalQDL.g:5977:3: ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) )
                    // InternalQDL.g:5978:4: () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) )
                    {
                    // InternalQDL.g:5978:4: ()
                    // InternalQDL.g:5979:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLOtherExpressionsAccess().getIQLNewExpressionAction_4_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_12=(Token)match(input,84,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_12, grammarAccess.getIQLOtherExpressionsAccess().getNewKeyword_4_1());
                      			
                    }
                    // InternalQDL.g:5989:4: ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) )
                    int alt80=2;
                    alt80 = dfa80.predict(input);
                    switch (alt80) {
                        case 1 :
                            // InternalQDL.g:5990:5: ( (lv_ref_13_0= ruleIQLArrayTypeRef ) )
                            {
                            // InternalQDL.g:5990:5: ( (lv_ref_13_0= ruleIQLArrayTypeRef ) )
                            // InternalQDL.g:5991:6: (lv_ref_13_0= ruleIQLArrayTypeRef )
                            {
                            // InternalQDL.g:5991:6: (lv_ref_13_0= ruleIQLArrayTypeRef )
                            // InternalQDL.g:5992:7: lv_ref_13_0= ruleIQLArrayTypeRef
                            {
                            if ( state.backtracking==0 ) {

                              							newCompositeNode(grammarAccess.getIQLOtherExpressionsAccess().getRefIQLArrayTypeRefParserRuleCall_4_2_0_0());
                              						
                            }
                            pushFollow(FOLLOW_2);
                            lv_ref_13_0=ruleIQLArrayTypeRef();

                            state._fsp--;
                            if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              							if (current==null) {
                              								current = createModelElementForParent(grammarAccess.getIQLOtherExpressionsRule());
                              							}
                              							set(
                              								current,
                              								"ref",
                              								lv_ref_13_0,
                              								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLArrayTypeRef");
                              							afterParserOrEnumRuleCall();
                              						
                            }

                            }


                            }


                            }
                            break;
                        case 2 :
                            // InternalQDL.g:6010:5: ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) )
                            {
                            // InternalQDL.g:6010:5: ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) )
                            // InternalQDL.g:6011:6: ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) )
                            {
                            // InternalQDL.g:6011:6: ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) )
                            // InternalQDL.g:6012:7: (lv_ref_14_0= ruleIQLSimpleTypeRef )
                            {
                            // InternalQDL.g:6012:7: (lv_ref_14_0= ruleIQLSimpleTypeRef )
                            // InternalQDL.g:6013:8: lv_ref_14_0= ruleIQLSimpleTypeRef
                            {
                            if ( state.backtracking==0 ) {

                              								newCompositeNode(grammarAccess.getIQLOtherExpressionsAccess().getRefIQLSimpleTypeRefParserRuleCall_4_2_1_0_0());
                              							
                            }
                            pushFollow(FOLLOW_7);
                            lv_ref_14_0=ruleIQLSimpleTypeRef();

                            state._fsp--;
                            if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              								if (current==null) {
                              									current = createModelElementForParent(grammarAccess.getIQLOtherExpressionsRule());
                              								}
                              								set(
                              									current,
                              									"ref",
                              									lv_ref_14_0,
                              									"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLSimpleTypeRef");
                              								afterParserOrEnumRuleCall();
                              							
                            }

                            }


                            }

                            // InternalQDL.g:6030:6: ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) )
                            int alt79=2;
                            int LA79_0 = input.LA(1);

                            if ( (LA79_0==14) ) {
                                alt79=1;
                            }
                            else if ( (LA79_0==59) ) {
                                alt79=2;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return current;}
                                NoViableAltException nvae =
                                    new NoViableAltException("", 79, 0, input);

                                throw nvae;
                            }
                            switch (alt79) {
                                case 1 :
                                    // InternalQDL.g:6031:7: ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? )
                                    {
                                    // InternalQDL.g:6031:7: ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? )
                                    // InternalQDL.g:6032:8: ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )?
                                    {
                                    // InternalQDL.g:6032:8: ( (lv_argsList_15_0= ruleIQLArgumentsList ) )
                                    // InternalQDL.g:6033:9: (lv_argsList_15_0= ruleIQLArgumentsList )
                                    {
                                    // InternalQDL.g:6033:9: (lv_argsList_15_0= ruleIQLArgumentsList )
                                    // InternalQDL.g:6034:10: lv_argsList_15_0= ruleIQLArgumentsList
                                    {
                                    if ( state.backtracking==0 ) {

                                      										newCompositeNode(grammarAccess.getIQLOtherExpressionsAccess().getArgsListIQLArgumentsListParserRuleCall_4_2_1_1_0_0_0());
                                      									
                                    }
                                    pushFollow(FOLLOW_40);
                                    lv_argsList_15_0=ruleIQLArgumentsList();

                                    state._fsp--;
                                    if (state.failed) return current;
                                    if ( state.backtracking==0 ) {

                                      										if (current==null) {
                                      											current = createModelElementForParent(grammarAccess.getIQLOtherExpressionsRule());
                                      										}
                                      										set(
                                      											current,
                                      											"argsList",
                                      											lv_argsList_15_0,
                                      											"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLArgumentsList");
                                      										afterParserOrEnumRuleCall();
                                      									
                                    }

                                    }


                                    }

                                    // InternalQDL.g:6051:8: ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )?
                                    int alt78=2;
                                    int LA78_0 = input.LA(1);

                                    if ( (LA78_0==59) ) {
                                        alt78=1;
                                    }
                                    switch (alt78) {
                                        case 1 :
                                            // InternalQDL.g:6052:9: (lv_argsMap_16_0= ruleIQLArgumentsMap )
                                            {
                                            // InternalQDL.g:6052:9: (lv_argsMap_16_0= ruleIQLArgumentsMap )
                                            // InternalQDL.g:6053:10: lv_argsMap_16_0= ruleIQLArgumentsMap
                                            {
                                            if ( state.backtracking==0 ) {

                                              										newCompositeNode(grammarAccess.getIQLOtherExpressionsAccess().getArgsMapIQLArgumentsMapParserRuleCall_4_2_1_1_0_1_0());
                                              									
                                            }
                                            pushFollow(FOLLOW_2);
                                            lv_argsMap_16_0=ruleIQLArgumentsMap();

                                            state._fsp--;
                                            if (state.failed) return current;
                                            if ( state.backtracking==0 ) {

                                              										if (current==null) {
                                              											current = createModelElementForParent(grammarAccess.getIQLOtherExpressionsRule());
                                              										}
                                              										set(
                                              											current,
                                              											"argsMap",
                                              											lv_argsMap_16_0,
                                              											"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLArgumentsMap");
                                              										afterParserOrEnumRuleCall();
                                              									
                                            }

                                            }


                                            }
                                            break;

                                    }


                                    }


                                    }
                                    break;
                                case 2 :
                                    // InternalQDL.g:6072:7: ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) )
                                    {
                                    // InternalQDL.g:6072:7: ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) )
                                    // InternalQDL.g:6073:8: (lv_argsMap_17_0= ruleIQLArgumentsMap )
                                    {
                                    // InternalQDL.g:6073:8: (lv_argsMap_17_0= ruleIQLArgumentsMap )
                                    // InternalQDL.g:6074:9: lv_argsMap_17_0= ruleIQLArgumentsMap
                                    {
                                    if ( state.backtracking==0 ) {

                                      									newCompositeNode(grammarAccess.getIQLOtherExpressionsAccess().getArgsMapIQLArgumentsMapParserRuleCall_4_2_1_1_1_0());
                                      								
                                    }
                                    pushFollow(FOLLOW_2);
                                    lv_argsMap_17_0=ruleIQLArgumentsMap();

                                    state._fsp--;
                                    if (state.failed) return current;
                                    if ( state.backtracking==0 ) {

                                      									if (current==null) {
                                      										current = createModelElementForParent(grammarAccess.getIQLOtherExpressionsRule());
                                      									}
                                      									set(
                                      										current,
                                      										"argsMap",
                                      										lv_argsMap_17_0,
                                      										"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLArgumentsMap");
                                      									afterParserOrEnumRuleCall();
                                      								
                                    }

                                    }


                                    }


                                    }
                                    break;

                            }


                            }


                            }
                            break;

                    }


                    }


                    }
                    break;
                case 6 :
                    // InternalQDL.g:6096:3: this_IQLLiteralExpression_18= ruleIQLLiteralExpression
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLOtherExpressionsAccess().getIQLLiteralExpressionParserRuleCall_5());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLLiteralExpression_18=ruleIQLLiteralExpression();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLLiteralExpression_18;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLOtherExpressions"


    // $ANTLR start "entryRuleIQLLiteralExpression"
    // InternalQDL.g:6108:1: entryRuleIQLLiteralExpression returns [EObject current=null] : iv_ruleIQLLiteralExpression= ruleIQLLiteralExpression EOF ;
    public final EObject entryRuleIQLLiteralExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLiteralExpression = null;


        try {
            // InternalQDL.g:6108:61: (iv_ruleIQLLiteralExpression= ruleIQLLiteralExpression EOF )
            // InternalQDL.g:6109:2: iv_ruleIQLLiteralExpression= ruleIQLLiteralExpression EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLLiteralExpressionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLLiteralExpression=ruleIQLLiteralExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLLiteralExpression; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLLiteralExpression"


    // $ANTLR start "ruleIQLLiteralExpression"
    // InternalQDL.g:6115:1: ruleIQLLiteralExpression returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_RANGE ) ) ) | ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' ) | ( () otherlv_15= 'null' ) | ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList ) | this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap ) ;
    public final EObject ruleIQLLiteralExpression() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        Token lv_value_9_0=null;
        Token otherlv_11=null;
        Token otherlv_13=null;
        Token otherlv_15=null;
        AntlrDatatypeRuleToken lv_value_7_0 = null;

        EObject lv_value_12_0 = null;

        EObject this_IQLLiteralExpressionList_16 = null;

        EObject this_IQLLiteralExpressionMap_17 = null;



        	enterRule();

        try {
            // InternalQDL.g:6121:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_RANGE ) ) ) | ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' ) | ( () otherlv_15= 'null' ) | ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList ) | this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap ) )
            // InternalQDL.g:6122:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_RANGE ) ) ) | ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' ) | ( () otherlv_15= 'null' ) | ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList ) | this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap )
            {
            // InternalQDL.g:6122:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_RANGE ) ) ) | ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' ) | ( () otherlv_15= 'null' ) | ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList ) | this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap )
            int alt82=9;
            alt82 = dfa82.predict(input);
            switch (alt82) {
                case 1 :
                    // InternalQDL.g:6123:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalQDL.g:6123:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalQDL.g:6124:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalQDL.g:6124:4: ()
                    // InternalQDL.g:6125:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionIntAction_0_0(),
                      						current);
                      				
                    }

                    }

                    // InternalQDL.g:6131:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalQDL.g:6132:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalQDL.g:6132:5: (lv_value_1_0= RULE_INT )
                    // InternalQDL.g:6133:6: lv_value_1_0= RULE_INT
                    {
                    lv_value_1_0=(Token)match(input,RULE_INT,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_value_1_0, grammarAccess.getIQLLiteralExpressionAccess().getValueINTTerminalRuleCall_0_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getIQLLiteralExpressionRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"value",
                      							lv_value_1_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.INT");
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalQDL.g:6151:3: ( () ( (lv_value_3_0= RULE_DOUBLE ) ) )
                    {
                    // InternalQDL.g:6151:3: ( () ( (lv_value_3_0= RULE_DOUBLE ) ) )
                    // InternalQDL.g:6152:4: () ( (lv_value_3_0= RULE_DOUBLE ) )
                    {
                    // InternalQDL.g:6152:4: ()
                    // InternalQDL.g:6153:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionDoubleAction_1_0(),
                      						current);
                      				
                    }

                    }

                    // InternalQDL.g:6159:4: ( (lv_value_3_0= RULE_DOUBLE ) )
                    // InternalQDL.g:6160:5: (lv_value_3_0= RULE_DOUBLE )
                    {
                    // InternalQDL.g:6160:5: (lv_value_3_0= RULE_DOUBLE )
                    // InternalQDL.g:6161:6: lv_value_3_0= RULE_DOUBLE
                    {
                    lv_value_3_0=(Token)match(input,RULE_DOUBLE,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_value_3_0, grammarAccess.getIQLLiteralExpressionAccess().getValueDOUBLETerminalRuleCall_1_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getIQLLiteralExpressionRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"value",
                      							lv_value_3_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.DOUBLE");
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalQDL.g:6179:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalQDL.g:6179:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalQDL.g:6180:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalQDL.g:6180:4: ()
                    // InternalQDL.g:6181:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionStringAction_2_0(),
                      						current);
                      				
                    }

                    }

                    // InternalQDL.g:6187:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalQDL.g:6188:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalQDL.g:6188:5: (lv_value_5_0= RULE_STRING )
                    // InternalQDL.g:6189:6: lv_value_5_0= RULE_STRING
                    {
                    lv_value_5_0=(Token)match(input,RULE_STRING,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_value_5_0, grammarAccess.getIQLLiteralExpressionAccess().getValueSTRINGTerminalRuleCall_2_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getIQLLiteralExpressionRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"value",
                      							lv_value_5_0,
                      							"org.eclipse.xtext.common.Terminals.STRING");
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 4 :
                    // InternalQDL.g:6207:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    {
                    // InternalQDL.g:6207:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    // InternalQDL.g:6208:4: () ( (lv_value_7_0= ruleBOOLEAN ) )
                    {
                    // InternalQDL.g:6208:4: ()
                    // InternalQDL.g:6209:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionBooleanAction_3_0(),
                      						current);
                      				
                    }

                    }

                    // InternalQDL.g:6215:4: ( (lv_value_7_0= ruleBOOLEAN ) )
                    // InternalQDL.g:6216:5: (lv_value_7_0= ruleBOOLEAN )
                    {
                    // InternalQDL.g:6216:5: (lv_value_7_0= ruleBOOLEAN )
                    // InternalQDL.g:6217:6: lv_value_7_0= ruleBOOLEAN
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLLiteralExpressionAccess().getValueBOOLEANParserRuleCall_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_value_7_0=ruleBOOLEAN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLLiteralExpressionRule());
                      						}
                      						set(
                      							current,
                      							"value",
                      							lv_value_7_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.BOOLEAN");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 5 :
                    // InternalQDL.g:6236:3: ( () ( (lv_value_9_0= RULE_RANGE ) ) )
                    {
                    // InternalQDL.g:6236:3: ( () ( (lv_value_9_0= RULE_RANGE ) ) )
                    // InternalQDL.g:6237:4: () ( (lv_value_9_0= RULE_RANGE ) )
                    {
                    // InternalQDL.g:6237:4: ()
                    // InternalQDL.g:6238:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionRangeAction_4_0(),
                      						current);
                      				
                    }

                    }

                    // InternalQDL.g:6244:4: ( (lv_value_9_0= RULE_RANGE ) )
                    // InternalQDL.g:6245:5: (lv_value_9_0= RULE_RANGE )
                    {
                    // InternalQDL.g:6245:5: (lv_value_9_0= RULE_RANGE )
                    // InternalQDL.g:6246:6: lv_value_9_0= RULE_RANGE
                    {
                    lv_value_9_0=(Token)match(input,RULE_RANGE,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_value_9_0, grammarAccess.getIQLLiteralExpressionAccess().getValueRANGETerminalRuleCall_4_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getIQLLiteralExpressionRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"value",
                      							lv_value_9_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.RANGE");
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 6 :
                    // InternalQDL.g:6264:3: ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' )
                    {
                    // InternalQDL.g:6264:3: ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' )
                    // InternalQDL.g:6265:4: () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')'
                    {
                    // InternalQDL.g:6265:4: ()
                    // InternalQDL.g:6266:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionTypeAction_5_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_11=(Token)match(input,85,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_11, grammarAccess.getIQLLiteralExpressionAccess().getClassKeyword_5_1());
                      			
                    }
                    // InternalQDL.g:6276:4: ( (lv_value_12_0= ruleJvmTypeReference ) )
                    // InternalQDL.g:6277:5: (lv_value_12_0= ruleJvmTypeReference )
                    {
                    // InternalQDL.g:6277:5: (lv_value_12_0= ruleJvmTypeReference )
                    // InternalQDL.g:6278:6: lv_value_12_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLLiteralExpressionAccess().getValueJvmTypeReferenceParserRuleCall_5_2_0());
                      					
                    }
                    pushFollow(FOLLOW_9);
                    lv_value_12_0=ruleJvmTypeReference();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLLiteralExpressionRule());
                      						}
                      						set(
                      							current,
                      							"value",
                      							lv_value_12_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    otherlv_13=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_13, grammarAccess.getIQLLiteralExpressionAccess().getRightParenthesisKeyword_5_3());
                      			
                    }

                    }


                    }
                    break;
                case 7 :
                    // InternalQDL.g:6301:3: ( () otherlv_15= 'null' )
                    {
                    // InternalQDL.g:6301:3: ( () otherlv_15= 'null' )
                    // InternalQDL.g:6302:4: () otherlv_15= 'null'
                    {
                    // InternalQDL.g:6302:4: ()
                    // InternalQDL.g:6303:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionNullAction_6_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_15=(Token)match(input,20,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_15, grammarAccess.getIQLLiteralExpressionAccess().getNullKeyword_6_1());
                      			
                    }

                    }


                    }
                    break;
                case 8 :
                    // InternalQDL.g:6315:3: ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList )
                    {
                    // InternalQDL.g:6315:3: ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList )
                    // InternalQDL.g:6316:4: ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList
                    {
                    if ( state.backtracking==0 ) {

                      				newCompositeNode(grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionListParserRuleCall_7());
                      			
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLLiteralExpressionList_16=ruleIQLLiteralExpressionList();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = this_IQLLiteralExpressionList_16;
                      				afterParserOrEnumRuleCall();
                      			
                    }

                    }


                    }
                    break;
                case 9 :
                    // InternalQDL.g:6327:3: this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionMapParserRuleCall_8());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_IQLLiteralExpressionMap_17=ruleIQLLiteralExpressionMap();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_IQLLiteralExpressionMap_17;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLLiteralExpression"


    // $ANTLR start "entryRuleIQLLiteralExpressionList"
    // InternalQDL.g:6339:1: entryRuleIQLLiteralExpressionList returns [EObject current=null] : iv_ruleIQLLiteralExpressionList= ruleIQLLiteralExpressionList EOF ;
    public final EObject entryRuleIQLLiteralExpressionList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLiteralExpressionList = null;


        try {
            // InternalQDL.g:6339:65: (iv_ruleIQLLiteralExpressionList= ruleIQLLiteralExpressionList EOF )
            // InternalQDL.g:6340:2: iv_ruleIQLLiteralExpressionList= ruleIQLLiteralExpressionList EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLLiteralExpressionListRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLLiteralExpressionList=ruleIQLLiteralExpressionList();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLLiteralExpressionList; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLLiteralExpressionList"


    // $ANTLR start "ruleIQLLiteralExpressionList"
    // InternalQDL.g:6346:1: ruleIQLLiteralExpressionList returns [EObject current=null] : ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ']' ) ;
    public final EObject ruleIQLLiteralExpressionList() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:6352:2: ( ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ']' ) )
            // InternalQDL.g:6353:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ']' )
            {
            // InternalQDL.g:6353:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ']' )
            // InternalQDL.g:6354:3: () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ']'
            {
            // InternalQDL.g:6354:3: ()
            // InternalQDL.g:6355:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLLiteralExpressionListAccess().getIQLLiteralExpressionListAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,57,FOLLOW_61); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLLiteralExpressionListAccess().getLeftSquareBracketKeyword_1());
              		
            }
            // InternalQDL.g:6365:3: ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )?
            int alt84=2;
            int LA84_0 = input.LA(1);

            if ( ((LA84_0>=RULE_ID && LA84_0<=RULE_STRING)||LA84_0==RULE_RANGE||LA84_0==14||(LA84_0>=20 && LA84_0<=21)||LA84_0==23||(LA84_0>=31 && LA84_0<=32)||LA84_0==37||LA84_0==57||(LA84_0>=79 && LA84_0<=80)||(LA84_0>=84 && LA84_0<=85)||(LA84_0>=111 && LA84_0<=112)) ) {
                alt84=1;
            }
            switch (alt84) {
                case 1 :
                    // InternalQDL.g:6366:4: ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )*
                    {
                    // InternalQDL.g:6366:4: ( (lv_elements_2_0= ruleIQLExpression ) )
                    // InternalQDL.g:6367:5: (lv_elements_2_0= ruleIQLExpression )
                    {
                    // InternalQDL.g:6367:5: (lv_elements_2_0= ruleIQLExpression )
                    // InternalQDL.g:6368:6: lv_elements_2_0= ruleIQLExpression
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLLiteralExpressionListAccess().getElementsIQLExpressionParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_36);
                    lv_elements_2_0=ruleIQLExpression();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLLiteralExpressionListRule());
                      						}
                      						add(
                      							current,
                      							"elements",
                      							lv_elements_2_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalQDL.g:6385:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )*
                    loop83:
                    do {
                        int alt83=2;
                        int LA83_0 = input.LA(1);

                        if ( (LA83_0==63) ) {
                            alt83=1;
                        }


                        switch (alt83) {
                    	case 1 :
                    	    // InternalQDL.g:6386:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) )
                    	    {
                    	    otherlv_3=(Token)match(input,63,FOLLOW_11); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLLiteralExpressionListAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalQDL.g:6390:5: ( (lv_elements_4_0= ruleIQLExpression ) )
                    	    // InternalQDL.g:6391:6: (lv_elements_4_0= ruleIQLExpression )
                    	    {
                    	    // InternalQDL.g:6391:6: (lv_elements_4_0= ruleIQLExpression )
                    	    // InternalQDL.g:6392:7: lv_elements_4_0= ruleIQLExpression
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLLiteralExpressionListAccess().getElementsIQLExpressionParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_36);
                    	    lv_elements_4_0=ruleIQLExpression();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElementForParent(grammarAccess.getIQLLiteralExpressionListRule());
                    	      							}
                    	      							add(
                    	      								current,
                    	      								"elements",
                    	      								lv_elements_4_0,
                    	      								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop83;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,58,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getIQLLiteralExpressionListAccess().getRightSquareBracketKeyword_3());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLLiteralExpressionList"


    // $ANTLR start "entryRuleIQLLiteralExpressionMap"
    // InternalQDL.g:6419:1: entryRuleIQLLiteralExpressionMap returns [EObject current=null] : iv_ruleIQLLiteralExpressionMap= ruleIQLLiteralExpressionMap EOF ;
    public final EObject entryRuleIQLLiteralExpressionMap() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLiteralExpressionMap = null;


        try {
            // InternalQDL.g:6419:64: (iv_ruleIQLLiteralExpressionMap= ruleIQLLiteralExpressionMap EOF )
            // InternalQDL.g:6420:2: iv_ruleIQLLiteralExpressionMap= ruleIQLLiteralExpressionMap EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLLiteralExpressionMapRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLLiteralExpressionMap=ruleIQLLiteralExpressionMap();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLLiteralExpressionMap; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLLiteralExpressionMap"


    // $ANTLR start "ruleIQLLiteralExpressionMap"
    // InternalQDL.g:6426:1: ruleIQLLiteralExpressionMap returns [EObject current=null] : ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )? otherlv_5= ']' ) ;
    public final EObject ruleIQLLiteralExpressionMap() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:6432:2: ( ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )? otherlv_5= ']' ) )
            // InternalQDL.g:6433:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )? otherlv_5= ']' )
            {
            // InternalQDL.g:6433:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )? otherlv_5= ']' )
            // InternalQDL.g:6434:3: () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )? otherlv_5= ']'
            {
            // InternalQDL.g:6434:3: ()
            // InternalQDL.g:6435:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLLiteralExpressionMapAccess().getIQLLiteralExpressionMapAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,57,FOLLOW_61); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLLiteralExpressionMapAccess().getLeftSquareBracketKeyword_1());
              		
            }
            // InternalQDL.g:6445:3: ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )?
            int alt86=2;
            int LA86_0 = input.LA(1);

            if ( ((LA86_0>=RULE_ID && LA86_0<=RULE_STRING)||LA86_0==RULE_RANGE||LA86_0==14||(LA86_0>=20 && LA86_0<=21)||LA86_0==23||(LA86_0>=31 && LA86_0<=32)||LA86_0==37||LA86_0==57||(LA86_0>=79 && LA86_0<=80)||(LA86_0>=84 && LA86_0<=85)||(LA86_0>=111 && LA86_0<=112)) ) {
                alt86=1;
            }
            switch (alt86) {
                case 1 :
                    // InternalQDL.g:6446:4: ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )*
                    {
                    // InternalQDL.g:6446:4: ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) )
                    // InternalQDL.g:6447:5: (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue )
                    {
                    // InternalQDL.g:6447:5: (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue )
                    // InternalQDL.g:6448:6: lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLLiteralExpressionMapAccess().getElementsIQLLiteralExpressionMapKeyValueParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_36);
                    lv_elements_2_0=ruleIQLLiteralExpressionMapKeyValue();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLLiteralExpressionMapRule());
                      						}
                      						add(
                      							current,
                      							"elements",
                      							lv_elements_2_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLLiteralExpressionMapKeyValue");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalQDL.g:6465:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )*
                    loop85:
                    do {
                        int alt85=2;
                        int LA85_0 = input.LA(1);

                        if ( (LA85_0==63) ) {
                            alt85=1;
                        }


                        switch (alt85) {
                    	case 1 :
                    	    // InternalQDL.g:6466:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) )
                    	    {
                    	    otherlv_3=(Token)match(input,63,FOLLOW_11); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLLiteralExpressionMapAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalQDL.g:6470:5: ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) )
                    	    // InternalQDL.g:6471:6: (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue )
                    	    {
                    	    // InternalQDL.g:6471:6: (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue )
                    	    // InternalQDL.g:6472:7: lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLLiteralExpressionMapAccess().getElementsIQLLiteralExpressionMapKeyValueParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_36);
                    	    lv_elements_4_0=ruleIQLLiteralExpressionMapKeyValue();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElementForParent(grammarAccess.getIQLLiteralExpressionMapRule());
                    	      							}
                    	      							add(
                    	      								current,
                    	      								"elements",
                    	      								lv_elements_4_0,
                    	      								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLLiteralExpressionMapKeyValue");
                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop85;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,58,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getIQLLiteralExpressionMapAccess().getRightSquareBracketKeyword_3());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLLiteralExpressionMap"


    // $ANTLR start "entryRuleIQLLiteralExpressionMapKeyValue"
    // InternalQDL.g:6499:1: entryRuleIQLLiteralExpressionMapKeyValue returns [EObject current=null] : iv_ruleIQLLiteralExpressionMapKeyValue= ruleIQLLiteralExpressionMapKeyValue EOF ;
    public final EObject entryRuleIQLLiteralExpressionMapKeyValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLiteralExpressionMapKeyValue = null;


        try {
            // InternalQDL.g:6499:72: (iv_ruleIQLLiteralExpressionMapKeyValue= ruleIQLLiteralExpressionMapKeyValue EOF )
            // InternalQDL.g:6500:2: iv_ruleIQLLiteralExpressionMapKeyValue= ruleIQLLiteralExpressionMapKeyValue EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLLiteralExpressionMapKeyValueRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLLiteralExpressionMapKeyValue=ruleIQLLiteralExpressionMapKeyValue();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLLiteralExpressionMapKeyValue; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQLLiteralExpressionMapKeyValue"


    // $ANTLR start "ruleIQLLiteralExpressionMapKeyValue"
    // InternalQDL.g:6506:1: ruleIQLLiteralExpressionMapKeyValue returns [EObject current=null] : ( ( (lv_key_0_0= ruleIQLExpression ) ) otherlv_1= ':' ( (lv_value_2_0= ruleIQLExpression ) ) ) ;
    public final EObject ruleIQLLiteralExpressionMapKeyValue() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_key_0_0 = null;

        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:6512:2: ( ( ( (lv_key_0_0= ruleIQLExpression ) ) otherlv_1= ':' ( (lv_value_2_0= ruleIQLExpression ) ) ) )
            // InternalQDL.g:6513:2: ( ( (lv_key_0_0= ruleIQLExpression ) ) otherlv_1= ':' ( (lv_value_2_0= ruleIQLExpression ) ) )
            {
            // InternalQDL.g:6513:2: ( ( (lv_key_0_0= ruleIQLExpression ) ) otherlv_1= ':' ( (lv_value_2_0= ruleIQLExpression ) ) )
            // InternalQDL.g:6514:3: ( (lv_key_0_0= ruleIQLExpression ) ) otherlv_1= ':' ( (lv_value_2_0= ruleIQLExpression ) )
            {
            // InternalQDL.g:6514:3: ( (lv_key_0_0= ruleIQLExpression ) )
            // InternalQDL.g:6515:4: (lv_key_0_0= ruleIQLExpression )
            {
            // InternalQDL.g:6515:4: (lv_key_0_0= ruleIQLExpression )
            // InternalQDL.g:6516:5: lv_key_0_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLLiteralExpressionMapKeyValueAccess().getKeyIQLExpressionParserRuleCall_0_0());
              				
            }
            pushFollow(FOLLOW_47);
            lv_key_0_0=ruleIQLExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLLiteralExpressionMapKeyValueRule());
              					}
              					set(
              						current,
              						"key",
              						lv_key_0_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_1=(Token)match(input,17,FOLLOW_11); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLLiteralExpressionMapKeyValueAccess().getColonKeyword_1());
              		
            }
            // InternalQDL.g:6537:3: ( (lv_value_2_0= ruleIQLExpression ) )
            // InternalQDL.g:6538:4: (lv_value_2_0= ruleIQLExpression )
            {
            // InternalQDL.g:6538:4: (lv_value_2_0= ruleIQLExpression )
            // InternalQDL.g:6539:5: lv_value_2_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLLiteralExpressionMapKeyValueAccess().getValueIQLExpressionParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_value_2_0=ruleIQLExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLLiteralExpressionMapKeyValueRule());
              					}
              					set(
              						current,
              						"value",
              						lv_value_2_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQLLiteralExpressionMapKeyValue"


    // $ANTLR start "entryRuleQualifiedNameWithWildcard"
    // InternalQDL.g:6560:1: entryRuleQualifiedNameWithWildcard returns [String current=null] : iv_ruleQualifiedNameWithWildcard= ruleQualifiedNameWithWildcard EOF ;
    public final String entryRuleQualifiedNameWithWildcard() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedNameWithWildcard = null;


        try {
            // InternalQDL.g:6560:65: (iv_ruleQualifiedNameWithWildcard= ruleQualifiedNameWithWildcard EOF )
            // InternalQDL.g:6561:2: iv_ruleQualifiedNameWithWildcard= ruleQualifiedNameWithWildcard EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getQualifiedNameWithWildcardRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleQualifiedNameWithWildcard=ruleQualifiedNameWithWildcard();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleQualifiedNameWithWildcard.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleQualifiedNameWithWildcard"


    // $ANTLR start "ruleQualifiedNameWithWildcard"
    // InternalQDL.g:6567:1: ruleQualifiedNameWithWildcard returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_QualifiedName_0= ruleQualifiedName (kw= '::*' )? ) ;
    public final AntlrDatatypeRuleToken ruleQualifiedNameWithWildcard() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        AntlrDatatypeRuleToken this_QualifiedName_0 = null;



        	enterRule();

        try {
            // InternalQDL.g:6573:2: ( (this_QualifiedName_0= ruleQualifiedName (kw= '::*' )? ) )
            // InternalQDL.g:6574:2: (this_QualifiedName_0= ruleQualifiedName (kw= '::*' )? )
            {
            // InternalQDL.g:6574:2: (this_QualifiedName_0= ruleQualifiedName (kw= '::*' )? )
            // InternalQDL.g:6575:3: this_QualifiedName_0= ruleQualifiedName (kw= '::*' )?
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getQualifiedNameWithWildcardAccess().getQualifiedNameParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_62);
            this_QualifiedName_0=ruleQualifiedName();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current.merge(this_QualifiedName_0);
              		
            }
            if ( state.backtracking==0 ) {

              			afterParserOrEnumRuleCall();
              		
            }
            // InternalQDL.g:6585:3: (kw= '::*' )?
            int alt87=2;
            int LA87_0 = input.LA(1);

            if ( (LA87_0==86) ) {
                alt87=1;
            }
            switch (alt87) {
                case 1 :
                    // InternalQDL.g:6586:4: kw= '::*'
                    {
                    kw=(Token)match(input,86,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current.merge(kw);
                      				newLeafNode(kw, grammarAccess.getQualifiedNameWithWildcardAccess().getColonColonAsteriskKeyword_1());
                      			
                    }

                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleQualifiedNameWithWildcard"


    // $ANTLR start "entryRuleQualifiedName"
    // InternalQDL.g:6596:1: entryRuleQualifiedName returns [String current=null] : iv_ruleQualifiedName= ruleQualifiedName EOF ;
    public final String entryRuleQualifiedName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedName = null;


        try {
            // InternalQDL.g:6596:53: (iv_ruleQualifiedName= ruleQualifiedName EOF )
            // InternalQDL.g:6597:2: iv_ruleQualifiedName= ruleQualifiedName EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getQualifiedNameRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleQualifiedName=ruleQualifiedName();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleQualifiedName.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleQualifiedName"


    // $ANTLR start "ruleQualifiedName"
    // InternalQDL.g:6603:1: ruleQualifiedName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID (kw= '::' this_ID_2= RULE_ID )* ) ;
    public final AntlrDatatypeRuleToken ruleQualifiedName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_2=null;


        	enterRule();

        try {
            // InternalQDL.g:6609:2: ( (this_ID_0= RULE_ID (kw= '::' this_ID_2= RULE_ID )* ) )
            // InternalQDL.g:6610:2: (this_ID_0= RULE_ID (kw= '::' this_ID_2= RULE_ID )* )
            {
            // InternalQDL.g:6610:2: (this_ID_0= RULE_ID (kw= '::' this_ID_2= RULE_ID )* )
            // InternalQDL.g:6611:3: this_ID_0= RULE_ID (kw= '::' this_ID_2= RULE_ID )*
            {
            this_ID_0=(Token)match(input,RULE_ID,FOLLOW_63); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current.merge(this_ID_0);
              		
            }
            if ( state.backtracking==0 ) {

              			newLeafNode(this_ID_0, grammarAccess.getQualifiedNameAccess().getIDTerminalRuleCall_0());
              		
            }
            // InternalQDL.g:6618:3: (kw= '::' this_ID_2= RULE_ID )*
            loop88:
            do {
                int alt88=2;
                int LA88_0 = input.LA(1);

                if ( (LA88_0==87) ) {
                    alt88=1;
                }


                switch (alt88) {
            	case 1 :
            	    // InternalQDL.g:6619:4: kw= '::' this_ID_2= RULE_ID
            	    {
            	    kw=(Token)match(input,87,FOLLOW_6); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				current.merge(kw);
            	      				newLeafNode(kw, grammarAccess.getQualifiedNameAccess().getColonColonKeyword_1_0());
            	      			
            	    }
            	    this_ID_2=(Token)match(input,RULE_ID,FOLLOW_63); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				current.merge(this_ID_2);
            	      			
            	    }
            	    if ( state.backtracking==0 ) {

            	      				newLeafNode(this_ID_2, grammarAccess.getQualifiedNameAccess().getIDTerminalRuleCall_1_1());
            	      			
            	    }

            	    }
            	    break;

            	default :
            	    break loop88;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleQualifiedName"


    // $ANTLR start "entryRuleIQLJava"
    // InternalQDL.g:6636:1: entryRuleIQLJava returns [EObject current=null] : iv_ruleIQLJava= ruleIQLJava EOF ;
    public final EObject entryRuleIQLJava() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLJava = null;



        	HiddenTokens myHiddenTokenState = ((XtextTokenStream)input).setHiddenTokens("RULE_SL_COMMENT", "RULE_ML_COMMENT");

        try {
            // InternalQDL.g:6638:2: (iv_ruleIQLJava= ruleIQLJava EOF )
            // InternalQDL.g:6639:2: iv_ruleIQLJava= ruleIQLJava EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLJavaRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLJava=ruleIQLJava();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLJava; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {

            	myHiddenTokenState.restore();

        }
        return current;
    }
    // $ANTLR end "entryRuleIQLJava"


    // $ANTLR start "ruleIQLJava"
    // InternalQDL.g:6648:1: ruleIQLJava returns [EObject current=null] : (otherlv_0= '$*' ( (lv_text_1_0= ruleIQLJavaText ) ) otherlv_2= '*$' ) ;
    public final EObject ruleIQLJava() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        AntlrDatatypeRuleToken lv_text_1_0 = null;



        	enterRule();
        	HiddenTokens myHiddenTokenState = ((XtextTokenStream)input).setHiddenTokens("RULE_SL_COMMENT", "RULE_ML_COMMENT");

        try {
            // InternalQDL.g:6655:2: ( (otherlv_0= '$*' ( (lv_text_1_0= ruleIQLJavaText ) ) otherlv_2= '*$' ) )
            // InternalQDL.g:6656:2: (otherlv_0= '$*' ( (lv_text_1_0= ruleIQLJavaText ) ) otherlv_2= '*$' )
            {
            // InternalQDL.g:6656:2: (otherlv_0= '$*' ( (lv_text_1_0= ruleIQLJavaText ) ) otherlv_2= '*$' )
            // InternalQDL.g:6657:3: otherlv_0= '$*' ( (lv_text_1_0= ruleIQLJavaText ) ) otherlv_2= '*$'
            {
            otherlv_0=(Token)match(input,88,FOLLOW_64); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_0, grammarAccess.getIQLJavaAccess().getDollarSignAsteriskKeyword_0());
              		
            }
            // InternalQDL.g:6661:3: ( (lv_text_1_0= ruleIQLJavaText ) )
            // InternalQDL.g:6662:4: (lv_text_1_0= ruleIQLJavaText )
            {
            // InternalQDL.g:6662:4: (lv_text_1_0= ruleIQLJavaText )
            // InternalQDL.g:6663:5: lv_text_1_0= ruleIQLJavaText
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLJavaAccess().getTextIQLJavaTextParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_65);
            lv_text_1_0=ruleIQLJavaText();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getIQLJavaRule());
              					}
              					set(
              						current,
              						"text",
              						lv_text_1_0,
              						"de.uniol.inf.is.odysseus.iql.qdl.QDL.IQLJavaText");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_2=(Token)match(input,89,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLJavaAccess().getAsteriskDollarSignKeyword_2());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {

            	myHiddenTokenState.restore();

        }
        return current;
    }
    // $ANTLR end "ruleIQLJava"


    // $ANTLR start "entryRuleIQL_JAVA_KEYWORDS"
    // InternalQDL.g:6691:1: entryRuleIQL_JAVA_KEYWORDS returns [String current=null] : iv_ruleIQL_JAVA_KEYWORDS= ruleIQL_JAVA_KEYWORDS EOF ;
    public final String entryRuleIQL_JAVA_KEYWORDS() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleIQL_JAVA_KEYWORDS = null;


        try {
            // InternalQDL.g:6691:57: (iv_ruleIQL_JAVA_KEYWORDS= ruleIQL_JAVA_KEYWORDS EOF )
            // InternalQDL.g:6692:2: iv_ruleIQL_JAVA_KEYWORDS= ruleIQL_JAVA_KEYWORDS EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQL_JAVA_KEYWORDSRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQL_JAVA_KEYWORDS=ruleIQL_JAVA_KEYWORDS();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQL_JAVA_KEYWORDS.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleIQL_JAVA_KEYWORDS"


    // $ANTLR start "ruleIQL_JAVA_KEYWORDS"
    // InternalQDL.g:6698:1: ruleIQL_JAVA_KEYWORDS returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'break' | kw= 'case' | kw= 'class' | kw= 'continue' | kw= 'default' | kw= 'do' | kw= 'else' | kw= 'extends' | kw= 'for' | kw= 'if' | kw= 'implements' | kw= 'instanceof' | kw= 'interface' | kw= 'new' | kw= 'package' | kw= 'return' | kw= 'super' | kw= 'switch' | kw= 'this' | kw= 'while' | kw= 'abstract' | kw= 'assert' | kw= 'catch' | kw= 'const' | kw= 'enum' | kw= 'final' | kw= 'finally' | kw= 'goto' | kw= 'import' | kw= 'native' | kw= 'private' | kw= 'protected' | kw= 'public' | kw= 'static' | kw= 'synchronized' | kw= 'throw' | kw= 'throws' | kw= 'transient' | kw= 'try' | kw= 'volatile' | kw= 'strictfp' ) ;
    public final AntlrDatatypeRuleToken ruleIQL_JAVA_KEYWORDS() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalQDL.g:6704:2: ( (kw= 'break' | kw= 'case' | kw= 'class' | kw= 'continue' | kw= 'default' | kw= 'do' | kw= 'else' | kw= 'extends' | kw= 'for' | kw= 'if' | kw= 'implements' | kw= 'instanceof' | kw= 'interface' | kw= 'new' | kw= 'package' | kw= 'return' | kw= 'super' | kw= 'switch' | kw= 'this' | kw= 'while' | kw= 'abstract' | kw= 'assert' | kw= 'catch' | kw= 'const' | kw= 'enum' | kw= 'final' | kw= 'finally' | kw= 'goto' | kw= 'import' | kw= 'native' | kw= 'private' | kw= 'protected' | kw= 'public' | kw= 'static' | kw= 'synchronized' | kw= 'throw' | kw= 'throws' | kw= 'transient' | kw= 'try' | kw= 'volatile' | kw= 'strictfp' ) )
            // InternalQDL.g:6705:2: (kw= 'break' | kw= 'case' | kw= 'class' | kw= 'continue' | kw= 'default' | kw= 'do' | kw= 'else' | kw= 'extends' | kw= 'for' | kw= 'if' | kw= 'implements' | kw= 'instanceof' | kw= 'interface' | kw= 'new' | kw= 'package' | kw= 'return' | kw= 'super' | kw= 'switch' | kw= 'this' | kw= 'while' | kw= 'abstract' | kw= 'assert' | kw= 'catch' | kw= 'const' | kw= 'enum' | kw= 'final' | kw= 'finally' | kw= 'goto' | kw= 'import' | kw= 'native' | kw= 'private' | kw= 'protected' | kw= 'public' | kw= 'static' | kw= 'synchronized' | kw= 'throw' | kw= 'throws' | kw= 'transient' | kw= 'try' | kw= 'volatile' | kw= 'strictfp' )
            {
            // InternalQDL.g:6705:2: (kw= 'break' | kw= 'case' | kw= 'class' | kw= 'continue' | kw= 'default' | kw= 'do' | kw= 'else' | kw= 'extends' | kw= 'for' | kw= 'if' | kw= 'implements' | kw= 'instanceof' | kw= 'interface' | kw= 'new' | kw= 'package' | kw= 'return' | kw= 'super' | kw= 'switch' | kw= 'this' | kw= 'while' | kw= 'abstract' | kw= 'assert' | kw= 'catch' | kw= 'const' | kw= 'enum' | kw= 'final' | kw= 'finally' | kw= 'goto' | kw= 'import' | kw= 'native' | kw= 'private' | kw= 'protected' | kw= 'public' | kw= 'static' | kw= 'synchronized' | kw= 'throw' | kw= 'throws' | kw= 'transient' | kw= 'try' | kw= 'volatile' | kw= 'strictfp' )
            int alt89=41;
            switch ( input.LA(1) ) {
            case 81:
                {
                alt89=1;
                }
                break;
            case 78:
                {
                alt89=2;
                }
                break;
            case 66:
                {
                alt89=3;
                }
                break;
            case 82:
                {
                alt89=4;
                }
                break;
            case 77:
                {
                alt89=5;
                }
                break;
            case 74:
                {
                alt89=6;
                }
                break;
            case 72:
                {
                alt89=7;
                }
                break;
            case 67:
                {
                alt89=8;
                }
                break;
            case 75:
                {
                alt89=9;
                }
                break;
            case 71:
                {
                alt89=10;
                }
                break;
            case 68:
                {
                alt89=11;
                }
                break;
            case 16:
                {
                alt89=12;
                }
                break;
            case 69:
                {
                alt89=13;
                }
                break;
            case 84:
                {
                alt89=14;
                }
                break;
            case 90:
                {
                alt89=15;
                }
                break;
            case 83:
                {
                alt89=16;
                }
                break;
            case 80:
                {
                alt89=17;
                }
                break;
            case 76:
                {
                alt89=18;
                }
                break;
            case 79:
                {
                alt89=19;
                }
                break;
            case 73:
                {
                alt89=20;
                }
                break;
            case 91:
                {
                alt89=21;
                }
                break;
            case 92:
                {
                alt89=22;
                }
                break;
            case 93:
                {
                alt89=23;
                }
                break;
            case 94:
                {
                alt89=24;
                }
                break;
            case 95:
                {
                alt89=25;
                }
                break;
            case 96:
                {
                alt89=26;
                }
                break;
            case 97:
                {
                alt89=27;
                }
                break;
            case 98:
                {
                alt89=28;
                }
                break;
            case 99:
                {
                alt89=29;
                }
                break;
            case 100:
                {
                alt89=30;
                }
                break;
            case 101:
                {
                alt89=31;
                }
                break;
            case 102:
                {
                alt89=32;
                }
                break;
            case 103:
                {
                alt89=33;
                }
                break;
            case 65:
                {
                alt89=34;
                }
                break;
            case 104:
                {
                alt89=35;
                }
                break;
            case 105:
                {
                alt89=36;
                }
                break;
            case 106:
                {
                alt89=37;
                }
                break;
            case 107:
                {
                alt89=38;
                }
                break;
            case 108:
                {
                alt89=39;
                }
                break;
            case 109:
                {
                alt89=40;
                }
                break;
            case 110:
                {
                alt89=41;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 89, 0, input);

                throw nvae;
            }

            switch (alt89) {
                case 1 :
                    // InternalQDL.g:6706:3: kw= 'break'
                    {
                    kw=(Token)match(input,81,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getBreakKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalQDL.g:6712:3: kw= 'case'
                    {
                    kw=(Token)match(input,78,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getCaseKeyword_1());
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalQDL.g:6718:3: kw= 'class'
                    {
                    kw=(Token)match(input,66,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getClassKeyword_2());
                      		
                    }

                    }
                    break;
                case 4 :
                    // InternalQDL.g:6724:3: kw= 'continue'
                    {
                    kw=(Token)match(input,82,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getContinueKeyword_3());
                      		
                    }

                    }
                    break;
                case 5 :
                    // InternalQDL.g:6730:3: kw= 'default'
                    {
                    kw=(Token)match(input,77,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getDefaultKeyword_4());
                      		
                    }

                    }
                    break;
                case 6 :
                    // InternalQDL.g:6736:3: kw= 'do'
                    {
                    kw=(Token)match(input,74,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getDoKeyword_5());
                      		
                    }

                    }
                    break;
                case 7 :
                    // InternalQDL.g:6742:3: kw= 'else'
                    {
                    kw=(Token)match(input,72,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getElseKeyword_6());
                      		
                    }

                    }
                    break;
                case 8 :
                    // InternalQDL.g:6748:3: kw= 'extends'
                    {
                    kw=(Token)match(input,67,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getExtendsKeyword_7());
                      		
                    }

                    }
                    break;
                case 9 :
                    // InternalQDL.g:6754:3: kw= 'for'
                    {
                    kw=(Token)match(input,75,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getForKeyword_8());
                      		
                    }

                    }
                    break;
                case 10 :
                    // InternalQDL.g:6760:3: kw= 'if'
                    {
                    kw=(Token)match(input,71,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getIfKeyword_9());
                      		
                    }

                    }
                    break;
                case 11 :
                    // InternalQDL.g:6766:3: kw= 'implements'
                    {
                    kw=(Token)match(input,68,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getImplementsKeyword_10());
                      		
                    }

                    }
                    break;
                case 12 :
                    // InternalQDL.g:6772:3: kw= 'instanceof'
                    {
                    kw=(Token)match(input,16,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getInstanceofKeyword_11());
                      		
                    }

                    }
                    break;
                case 13 :
                    // InternalQDL.g:6778:3: kw= 'interface'
                    {
                    kw=(Token)match(input,69,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getInterfaceKeyword_12());
                      		
                    }

                    }
                    break;
                case 14 :
                    // InternalQDL.g:6784:3: kw= 'new'
                    {
                    kw=(Token)match(input,84,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getNewKeyword_13());
                      		
                    }

                    }
                    break;
                case 15 :
                    // InternalQDL.g:6790:3: kw= 'package'
                    {
                    kw=(Token)match(input,90,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getPackageKeyword_14());
                      		
                    }

                    }
                    break;
                case 16 :
                    // InternalQDL.g:6796:3: kw= 'return'
                    {
                    kw=(Token)match(input,83,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getReturnKeyword_15());
                      		
                    }

                    }
                    break;
                case 17 :
                    // InternalQDL.g:6802:3: kw= 'super'
                    {
                    kw=(Token)match(input,80,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getSuperKeyword_16());
                      		
                    }

                    }
                    break;
                case 18 :
                    // InternalQDL.g:6808:3: kw= 'switch'
                    {
                    kw=(Token)match(input,76,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getSwitchKeyword_17());
                      		
                    }

                    }
                    break;
                case 19 :
                    // InternalQDL.g:6814:3: kw= 'this'
                    {
                    kw=(Token)match(input,79,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getThisKeyword_18());
                      		
                    }

                    }
                    break;
                case 20 :
                    // InternalQDL.g:6820:3: kw= 'while'
                    {
                    kw=(Token)match(input,73,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getWhileKeyword_19());
                      		
                    }

                    }
                    break;
                case 21 :
                    // InternalQDL.g:6826:3: kw= 'abstract'
                    {
                    kw=(Token)match(input,91,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getAbstractKeyword_20());
                      		
                    }

                    }
                    break;
                case 22 :
                    // InternalQDL.g:6832:3: kw= 'assert'
                    {
                    kw=(Token)match(input,92,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getAssertKeyword_21());
                      		
                    }

                    }
                    break;
                case 23 :
                    // InternalQDL.g:6838:3: kw= 'catch'
                    {
                    kw=(Token)match(input,93,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getCatchKeyword_22());
                      		
                    }

                    }
                    break;
                case 24 :
                    // InternalQDL.g:6844:3: kw= 'const'
                    {
                    kw=(Token)match(input,94,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getConstKeyword_23());
                      		
                    }

                    }
                    break;
                case 25 :
                    // InternalQDL.g:6850:3: kw= 'enum'
                    {
                    kw=(Token)match(input,95,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getEnumKeyword_24());
                      		
                    }

                    }
                    break;
                case 26 :
                    // InternalQDL.g:6856:3: kw= 'final'
                    {
                    kw=(Token)match(input,96,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getFinalKeyword_25());
                      		
                    }

                    }
                    break;
                case 27 :
                    // InternalQDL.g:6862:3: kw= 'finally'
                    {
                    kw=(Token)match(input,97,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getFinallyKeyword_26());
                      		
                    }

                    }
                    break;
                case 28 :
                    // InternalQDL.g:6868:3: kw= 'goto'
                    {
                    kw=(Token)match(input,98,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getGotoKeyword_27());
                      		
                    }

                    }
                    break;
                case 29 :
                    // InternalQDL.g:6874:3: kw= 'import'
                    {
                    kw=(Token)match(input,99,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getImportKeyword_28());
                      		
                    }

                    }
                    break;
                case 30 :
                    // InternalQDL.g:6880:3: kw= 'native'
                    {
                    kw=(Token)match(input,100,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getNativeKeyword_29());
                      		
                    }

                    }
                    break;
                case 31 :
                    // InternalQDL.g:6886:3: kw= 'private'
                    {
                    kw=(Token)match(input,101,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getPrivateKeyword_30());
                      		
                    }

                    }
                    break;
                case 32 :
                    // InternalQDL.g:6892:3: kw= 'protected'
                    {
                    kw=(Token)match(input,102,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getProtectedKeyword_31());
                      		
                    }

                    }
                    break;
                case 33 :
                    // InternalQDL.g:6898:3: kw= 'public'
                    {
                    kw=(Token)match(input,103,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getPublicKeyword_32());
                      		
                    }

                    }
                    break;
                case 34 :
                    // InternalQDL.g:6904:3: kw= 'static'
                    {
                    kw=(Token)match(input,65,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getStaticKeyword_33());
                      		
                    }

                    }
                    break;
                case 35 :
                    // InternalQDL.g:6910:3: kw= 'synchronized'
                    {
                    kw=(Token)match(input,104,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getSynchronizedKeyword_34());
                      		
                    }

                    }
                    break;
                case 36 :
                    // InternalQDL.g:6916:3: kw= 'throw'
                    {
                    kw=(Token)match(input,105,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getThrowKeyword_35());
                      		
                    }

                    }
                    break;
                case 37 :
                    // InternalQDL.g:6922:3: kw= 'throws'
                    {
                    kw=(Token)match(input,106,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getThrowsKeyword_36());
                      		
                    }

                    }
                    break;
                case 38 :
                    // InternalQDL.g:6928:3: kw= 'transient'
                    {
                    kw=(Token)match(input,107,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getTransientKeyword_37());
                      		
                    }

                    }
                    break;
                case 39 :
                    // InternalQDL.g:6934:3: kw= 'try'
                    {
                    kw=(Token)match(input,108,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getTryKeyword_38());
                      		
                    }

                    }
                    break;
                case 40 :
                    // InternalQDL.g:6940:3: kw= 'volatile'
                    {
                    kw=(Token)match(input,109,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getVolatileKeyword_39());
                      		
                    }

                    }
                    break;
                case 41 :
                    // InternalQDL.g:6946:3: kw= 'strictfp'
                    {
                    kw=(Token)match(input,110,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getStrictfpKeyword_40());
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleIQL_JAVA_KEYWORDS"


    // $ANTLR start "entryRuleBOOLEAN"
    // InternalQDL.g:6955:1: entryRuleBOOLEAN returns [String current=null] : iv_ruleBOOLEAN= ruleBOOLEAN EOF ;
    public final String entryRuleBOOLEAN() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleBOOLEAN = null;


        try {
            // InternalQDL.g:6955:47: (iv_ruleBOOLEAN= ruleBOOLEAN EOF )
            // InternalQDL.g:6956:2: iv_ruleBOOLEAN= ruleBOOLEAN EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getBOOLEANRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleBOOLEAN=ruleBOOLEAN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleBOOLEAN.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

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
    // $ANTLR end "entryRuleBOOLEAN"


    // $ANTLR start "ruleBOOLEAN"
    // InternalQDL.g:6962:1: ruleBOOLEAN returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'true' | kw= 'false' ) ;
    public final AntlrDatatypeRuleToken ruleBOOLEAN() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalQDL.g:6968:2: ( (kw= 'true' | kw= 'false' ) )
            // InternalQDL.g:6969:2: (kw= 'true' | kw= 'false' )
            {
            // InternalQDL.g:6969:2: (kw= 'true' | kw= 'false' )
            int alt90=2;
            int LA90_0 = input.LA(1);

            if ( (LA90_0==111) ) {
                alt90=1;
            }
            else if ( (LA90_0==112) ) {
                alt90=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 90, 0, input);

                throw nvae;
            }
            switch (alt90) {
                case 1 :
                    // InternalQDL.g:6970:3: kw= 'true'
                    {
                    kw=(Token)match(input,111,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getBOOLEANAccess().getTrueKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalQDL.g:6976:3: kw= 'false'
                    {
                    kw=(Token)match(input,112,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getBOOLEANAccess().getFalseKeyword_1());
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

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
    // $ANTLR end "ruleBOOLEAN"

    // $ANTLR start synpred1_InternalQDL
    public final void synpred1_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:334:6: ( ( () 'instanceof' ) )
        // InternalQDL.g:334:7: ( () 'instanceof' )
        {
        // InternalQDL.g:334:7: ( () 'instanceof' )
        // InternalQDL.g:335:7: () 'instanceof'
        {
        // InternalQDL.g:335:7: ()
        // InternalQDL.g:336:7: 
        {
        }

        match(input,16,FOLLOW_2); if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred1_InternalQDL

    // $ANTLR start synpred2_InternalQDL
    public final void synpred2_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:377:6: ( ( () ( ( ruleOpRelational ) ) ) )
        // InternalQDL.g:377:7: ( () ( ( ruleOpRelational ) ) )
        {
        // InternalQDL.g:377:7: ( () ( ( ruleOpRelational ) ) )
        // InternalQDL.g:378:7: () ( ( ruleOpRelational ) )
        {
        // InternalQDL.g:378:7: ()
        // InternalQDL.g:379:7: 
        {
        }

        // InternalQDL.g:380:7: ( ( ruleOpRelational ) )
        // InternalQDL.g:381:8: ( ruleOpRelational )
        {
        // InternalQDL.g:381:8: ( ruleOpRelational )
        // InternalQDL.g:382:9: ruleOpRelational
        {
        pushFollow(FOLLOW_2);
        ruleOpRelational();

        state._fsp--;
        if (state.failed) return ;

        }


        }


        }


        }
    }
    // $ANTLR end synpred2_InternalQDL

    // $ANTLR start synpred3_InternalQDL
    public final void synpred3_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:466:5: ( ( () ( ( ruleIQLSubscribe ) ) ) )
        // InternalQDL.g:466:6: ( () ( ( ruleIQLSubscribe ) ) )
        {
        // InternalQDL.g:466:6: ( () ( ( ruleIQLSubscribe ) ) )
        // InternalQDL.g:467:6: () ( ( ruleIQLSubscribe ) )
        {
        // InternalQDL.g:467:6: ()
        // InternalQDL.g:468:6: 
        {
        }

        // InternalQDL.g:469:6: ( ( ruleIQLSubscribe ) )
        // InternalQDL.g:470:7: ( ruleIQLSubscribe )
        {
        // InternalQDL.g:470:7: ( ruleIQLSubscribe )
        // InternalQDL.g:471:8: ruleIQLSubscribe
        {
        pushFollow(FOLLOW_2);
        ruleIQLSubscribe();

        state._fsp--;
        if (state.failed) return ;

        }


        }


        }


        }
    }
    // $ANTLR end synpred3_InternalQDL

    // $ANTLR start synpred4_InternalQDL
    public final void synpred4_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:554:5: ( ( () ( ( ':' ) ) ) )
        // InternalQDL.g:554:6: ( () ( ( ':' ) ) )
        {
        // InternalQDL.g:554:6: ( () ( ( ':' ) ) )
        // InternalQDL.g:555:6: () ( ( ':' ) )
        {
        // InternalQDL.g:555:6: ()
        // InternalQDL.g:556:6: 
        {
        }

        // InternalQDL.g:557:6: ( ( ':' ) )
        // InternalQDL.g:558:7: ( ':' )
        {
        // InternalQDL.g:558:7: ( ':' )
        // InternalQDL.g:559:8: ':'
        {
        match(input,17,FOLLOW_2); if (state.failed) return ;

        }


        }


        }


        }
    }
    // $ANTLR end synpred4_InternalQDL

    // $ANTLR start synpred5_InternalQDL
    public final void synpred5_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:771:4: ( ( () ( ( RULE_ID ) ) ) )
        // InternalQDL.g:771:5: ( () ( ( RULE_ID ) ) )
        {
        // InternalQDL.g:771:5: ( () ( ( RULE_ID ) ) )
        // InternalQDL.g:772:5: () ( ( RULE_ID ) )
        {
        // InternalQDL.g:772:5: ()
        // InternalQDL.g:773:5: 
        {
        }

        // InternalQDL.g:774:5: ( ( RULE_ID ) )
        // InternalQDL.g:775:6: ( RULE_ID )
        {
        // InternalQDL.g:775:6: ( RULE_ID )
        // InternalQDL.g:776:7: RULE_ID
        {
        match(input,RULE_ID,FOLLOW_2); if (state.failed) return ;

        }


        }


        }


        }
    }
    // $ANTLR end synpred5_InternalQDL

    // $ANTLR start synpred6_InternalQDL
    public final void synpred6_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:3517:5: ( 'else' )
        // InternalQDL.g:3517:6: 'else'
        {
        match(input,72,FOLLOW_2); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred6_InternalQDL

    // $ANTLR start synpred7_InternalQDL
    public final void synpred7_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:4504:5: ( ( () ( ( ruleOpAssign ) ) ) )
        // InternalQDL.g:4504:6: ( () ( ( ruleOpAssign ) ) )
        {
        // InternalQDL.g:4504:6: ( () ( ( ruleOpAssign ) ) )
        // InternalQDL.g:4505:6: () ( ( ruleOpAssign ) )
        {
        // InternalQDL.g:4505:6: ()
        // InternalQDL.g:4506:6: 
        {
        }

        // InternalQDL.g:4507:6: ( ( ruleOpAssign ) )
        // InternalQDL.g:4508:7: ( ruleOpAssign )
        {
        // InternalQDL.g:4508:7: ( ruleOpAssign )
        // InternalQDL.g:4509:8: ruleOpAssign
        {
        pushFollow(FOLLOW_2);
        ruleOpAssign();

        state._fsp--;
        if (state.failed) return ;

        }


        }


        }


        }
    }
    // $ANTLR end synpred7_InternalQDL

    // $ANTLR start synpred8_InternalQDL
    public final void synpred8_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:4646:5: ( ( () ( ( ruleOpLogicalOr ) ) ) )
        // InternalQDL.g:4646:6: ( () ( ( ruleOpLogicalOr ) ) )
        {
        // InternalQDL.g:4646:6: ( () ( ( ruleOpLogicalOr ) ) )
        // InternalQDL.g:4647:6: () ( ( ruleOpLogicalOr ) )
        {
        // InternalQDL.g:4647:6: ()
        // InternalQDL.g:4648:6: 
        {
        }

        // InternalQDL.g:4649:6: ( ( ruleOpLogicalOr ) )
        // InternalQDL.g:4650:7: ( ruleOpLogicalOr )
        {
        // InternalQDL.g:4650:7: ( ruleOpLogicalOr )
        // InternalQDL.g:4651:8: ruleOpLogicalOr
        {
        pushFollow(FOLLOW_2);
        ruleOpLogicalOr();

        state._fsp--;
        if (state.failed) return ;

        }


        }


        }


        }
    }
    // $ANTLR end synpred8_InternalQDL

    // $ANTLR start synpred9_InternalQDL
    public final void synpred9_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:4756:5: ( ( () ( ( ruleOpLogicalAnd ) ) ) )
        // InternalQDL.g:4756:6: ( () ( ( ruleOpLogicalAnd ) ) )
        {
        // InternalQDL.g:4756:6: ( () ( ( ruleOpLogicalAnd ) ) )
        // InternalQDL.g:4757:6: () ( ( ruleOpLogicalAnd ) )
        {
        // InternalQDL.g:4757:6: ()
        // InternalQDL.g:4758:6: 
        {
        }

        // InternalQDL.g:4759:6: ( ( ruleOpLogicalAnd ) )
        // InternalQDL.g:4760:7: ( ruleOpLogicalAnd )
        {
        // InternalQDL.g:4760:7: ( ruleOpLogicalAnd )
        // InternalQDL.g:4761:8: ruleOpLogicalAnd
        {
        pushFollow(FOLLOW_2);
        ruleOpLogicalAnd();

        state._fsp--;
        if (state.failed) return ;

        }


        }


        }


        }
    }
    // $ANTLR end synpred9_InternalQDL

    // $ANTLR start synpred10_InternalQDL
    public final void synpred10_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:4866:5: ( ( () ( ( ruleOpEquality ) ) ) )
        // InternalQDL.g:4866:6: ( () ( ( ruleOpEquality ) ) )
        {
        // InternalQDL.g:4866:6: ( () ( ( ruleOpEquality ) ) )
        // InternalQDL.g:4867:6: () ( ( ruleOpEquality ) )
        {
        // InternalQDL.g:4867:6: ()
        // InternalQDL.g:4868:6: 
        {
        }

        // InternalQDL.g:4869:6: ( ( ruleOpEquality ) )
        // InternalQDL.g:4870:7: ( ruleOpEquality )
        {
        // InternalQDL.g:4870:7: ( ruleOpEquality )
        // InternalQDL.g:4871:8: ruleOpEquality
        {
        pushFollow(FOLLOW_2);
        ruleOpEquality();

        state._fsp--;
        if (state.failed) return ;

        }


        }


        }


        }
    }
    // $ANTLR end synpred10_InternalQDL

    // $ANTLR start synpred11_InternalQDL
    public final void synpred11_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:5026:5: ( ( () ( ( ruleOpAdd ) ) ) )
        // InternalQDL.g:5026:6: ( () ( ( ruleOpAdd ) ) )
        {
        // InternalQDL.g:5026:6: ( () ( ( ruleOpAdd ) ) )
        // InternalQDL.g:5027:6: () ( ( ruleOpAdd ) )
        {
        // InternalQDL.g:5027:6: ()
        // InternalQDL.g:5028:6: 
        {
        }

        // InternalQDL.g:5029:6: ( ( ruleOpAdd ) )
        // InternalQDL.g:5030:7: ( ruleOpAdd )
        {
        // InternalQDL.g:5030:7: ( ruleOpAdd )
        // InternalQDL.g:5031:8: ruleOpAdd
        {
        pushFollow(FOLLOW_2);
        ruleOpAdd();

        state._fsp--;
        if (state.failed) return ;

        }


        }


        }


        }
    }
    // $ANTLR end synpred11_InternalQDL

    // $ANTLR start synpred12_InternalQDL
    public final void synpred12_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:5144:5: ( ( () ( ( ruleOpMulti ) ) ) )
        // InternalQDL.g:5144:6: ( () ( ( ruleOpMulti ) ) )
        {
        // InternalQDL.g:5144:6: ( () ( ( ruleOpMulti ) ) )
        // InternalQDL.g:5145:6: () ( ( ruleOpMulti ) )
        {
        // InternalQDL.g:5145:6: ()
        // InternalQDL.g:5146:6: 
        {
        }

        // InternalQDL.g:5147:6: ( ( ruleOpMulti ) )
        // InternalQDL.g:5148:7: ( ruleOpMulti )
        {
        // InternalQDL.g:5148:7: ( ruleOpMulti )
        // InternalQDL.g:5149:8: ruleOpMulti
        {
        pushFollow(FOLLOW_2);
        ruleOpMulti();

        state._fsp--;
        if (state.failed) return ;

        }


        }


        }


        }
    }
    // $ANTLR end synpred12_InternalQDL

    // $ANTLR start synpred13_InternalQDL
    public final void synpred13_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:5410:5: ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )
        // InternalQDL.g:5410:6: ( () '(' ( ( ruleJvmTypeReference ) ) ')' )
        {
        // InternalQDL.g:5410:6: ( () '(' ( ( ruleJvmTypeReference ) ) ')' )
        // InternalQDL.g:5411:6: () '(' ( ( ruleJvmTypeReference ) ) ')'
        {
        // InternalQDL.g:5411:6: ()
        // InternalQDL.g:5412:6: 
        {
        }

        match(input,14,FOLLOW_6); if (state.failed) return ;
        // InternalQDL.g:5414:6: ( ( ruleJvmTypeReference ) )
        // InternalQDL.g:5415:7: ( ruleJvmTypeReference )
        {
        // InternalQDL.g:5415:7: ( ruleJvmTypeReference )
        // InternalQDL.g:5416:8: ruleJvmTypeReference
        {
        pushFollow(FOLLOW_9);
        ruleJvmTypeReference();

        state._fsp--;
        if (state.failed) return ;

        }


        }

        match(input,15,FOLLOW_2); if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred13_InternalQDL

    // $ANTLR start synpred14_InternalQDL
    public final void synpred14_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:5490:5: ( ( () ( ( ruleOpPostfix ) ) ) )
        // InternalQDL.g:5490:6: ( () ( ( ruleOpPostfix ) ) )
        {
        // InternalQDL.g:5490:6: ( () ( ( ruleOpPostfix ) ) )
        // InternalQDL.g:5491:6: () ( ( ruleOpPostfix ) )
        {
        // InternalQDL.g:5491:6: ()
        // InternalQDL.g:5492:6: 
        {
        }

        // InternalQDL.g:5493:6: ( ( ruleOpPostfix ) )
        // InternalQDL.g:5494:7: ( ruleOpPostfix )
        {
        // InternalQDL.g:5494:7: ( ruleOpPostfix )
        // InternalQDL.g:5495:8: ruleOpPostfix
        {
        pushFollow(FOLLOW_2);
        ruleOpPostfix();

        state._fsp--;
        if (state.failed) return ;

        }


        }


        }


        }
    }
    // $ANTLR end synpred14_InternalQDL

    // $ANTLR start synpred15_InternalQDL
    public final void synpred15_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:5671:5: ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )
        // InternalQDL.g:5671:6: ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' )
        {
        // InternalQDL.g:5671:6: ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' )
        // InternalQDL.g:5672:6: () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']'
        {
        // InternalQDL.g:5672:6: ()
        // InternalQDL.g:5673:6: 
        {
        }

        match(input,57,FOLLOW_11); if (state.failed) return ;
        // InternalQDL.g:5675:6: ( ( ruleIQLExpression ) )
        // InternalQDL.g:5676:7: ( ruleIQLExpression )
        {
        // InternalQDL.g:5676:7: ( ruleIQLExpression )
        // InternalQDL.g:5677:8: ruleIQLExpression
        {
        pushFollow(FOLLOW_36);
        ruleIQLExpression();

        state._fsp--;
        if (state.failed) return ;

        }


        }

        // InternalQDL.g:5680:6: ( ',' ( ( ruleIQLExpression ) ) )*
        loop91:
        do {
            int alt91=2;
            int LA91_0 = input.LA(1);

            if ( (LA91_0==63) ) {
                alt91=1;
            }


            switch (alt91) {
        	case 1 :
        	    // InternalQDL.g:5681:7: ',' ( ( ruleIQLExpression ) )
        	    {
        	    match(input,63,FOLLOW_11); if (state.failed) return ;
        	    // InternalQDL.g:5682:7: ( ( ruleIQLExpression ) )
        	    // InternalQDL.g:5683:8: ( ruleIQLExpression )
        	    {
        	    // InternalQDL.g:5683:8: ( ruleIQLExpression )
        	    // InternalQDL.g:5684:9: ruleIQLExpression
        	    {
        	    pushFollow(FOLLOW_36);
        	    ruleIQLExpression();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }


        	    }


        	    }
        	    break;

        	default :
        	    break loop91;
            }
        } while (true);

        match(input,58,FOLLOW_2); if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred15_InternalQDL

    // $ANTLR start synpred16_InternalQDL
    public final void synpred16_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:5756:6: ( ( () '.' ) )
        // InternalQDL.g:5756:7: ( () '.' )
        {
        // InternalQDL.g:5756:7: ( () '.' )
        // InternalQDL.g:5757:7: () '.'
        {
        // InternalQDL.g:5757:7: ()
        // InternalQDL.g:5758:7: 
        {
        }

        match(input,61,FOLLOW_2); if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred16_InternalQDL

    // $ANTLR start synpred17_InternalQDL
    public final void synpred17_InternalQDL_fragment() throws RecognitionException {   
        // InternalQDL.g:6316:4: ( ruleIQLLiteralExpressionList )
        // InternalQDL.g:6316:5: ruleIQLLiteralExpressionList
        {
        pushFollow(FOLLOW_2);
        ruleIQLLiteralExpressionList();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred17_InternalQDL

    // Delegated rules

    public final boolean synpred16_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred16_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred15_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred15_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred17_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred17_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred13_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred13_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred9_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred9_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred14_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred14_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred7_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred7_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred8_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred8_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred4_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred12_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred12_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred5_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred6_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred6_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred10_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred10_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred11_InternalQDL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred11_InternalQDL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA9 dfa9 = new DFA9(this);
    protected DFA26 dfa26 = new DFA26(this);
    protected DFA49 dfa49 = new DFA49(this);
    protected DFA70 dfa70 = new DFA70(this);
    protected DFA80 dfa80 = new DFA80(this);
    protected DFA82 dfa82 = new DFA82(this);
    static final String dfa_1s = "\32\uffff";
    static final String dfa_2s = "\1\2\31\uffff";
    static final String dfa_3s = "\1\17\1\0\30\uffff";
    static final String dfa_4s = "\1\77\1\0\30\uffff";
    static final String dfa_5s = "\2\uffff\1\2\26\uffff\1\1";
    static final String dfa_6s = "\1\uffff\1\0\30\uffff}>";
    static final String[] dfa_7s = {
            "\2\2\1\1\2\2\2\uffff\1\2\1\uffff\1\2\1\uffff\1\2\1\uffff\1\2\1\uffff\1\2\2\uffff\4\2\1\uffff\5\2\17\uffff\1\2\1\uffff\1\2\1\uffff\2\2",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] dfa_1 = DFA.unpackEncodedString(dfa_1s);
    static final short[] dfa_2 = DFA.unpackEncodedString(dfa_2s);
    static final char[] dfa_3 = DFA.unpackEncodedStringToUnsignedChars(dfa_3s);
    static final char[] dfa_4 = DFA.unpackEncodedStringToUnsignedChars(dfa_4s);
    static final short[] dfa_5 = DFA.unpackEncodedString(dfa_5s);
    static final short[] dfa_6 = DFA.unpackEncodedString(dfa_6s);
    static final short[][] dfa_7 = unpackEncodedStringArray(dfa_7s);

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = dfa_1;
            this.eof = dfa_2;
            this.min = dfa_3;
            this.max = dfa_4;
            this.accept = dfa_5;
            this.special = dfa_6;
            this.transition = dfa_7;
        }
        public String getDescription() {
            return "552:3: ( ( ( ( () ( ( ':' ) ) ) )=> ( () ( (lv_op_2_0= ':' ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAdditiveExpression ) ) )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA9_1 = input.LA(1);

                         
                        int index9_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_InternalQDL()) ) {s = 25;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index9_1);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 9, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String dfa_8s = "\6\uffff";
    static final String dfa_9s = "\1\uffff\1\3\3\uffff\1\3";
    static final String dfa_10s = "\3\4\2\uffff\1\4";
    static final String dfa_11s = "\1\4\1\127\1\4\2\uffff\1\127";
    static final String dfa_12s = "\3\uffff\1\1\1\2\1\uffff";
    static final String dfa_13s = "\6\uffff}>";
    static final String[] dfa_14s = {
            "\1\1",
            "\1\3\12\uffff\3\3\4\uffff\1\3\1\uffff\1\3\1\uffff\1\3\1\uffff\1\3\1\uffff\1\3\2\uffff\4\3\1\uffff\5\3\16\uffff\1\4\3\3\1\uffff\2\3\4\uffff\1\3\22\uffff\1\2",
            "\1\5",
            "",
            "",
            "\1\3\12\uffff\3\3\4\uffff\1\3\1\uffff\1\3\1\uffff\1\3\1\uffff\1\3\1\uffff\1\3\2\uffff\4\3\1\uffff\5\3\16\uffff\1\4\3\3\1\uffff\2\3\4\uffff\1\3\22\uffff\1\2"
    };

    static final short[] dfa_8 = DFA.unpackEncodedString(dfa_8s);
    static final short[] dfa_9 = DFA.unpackEncodedString(dfa_9s);
    static final char[] dfa_10 = DFA.unpackEncodedStringToUnsignedChars(dfa_10s);
    static final char[] dfa_11 = DFA.unpackEncodedStringToUnsignedChars(dfa_11s);
    static final short[] dfa_12 = DFA.unpackEncodedString(dfa_12s);
    static final short[] dfa_13 = DFA.unpackEncodedString(dfa_13s);
    static final short[][] dfa_14 = unpackEncodedStringArray(dfa_14s);

    class DFA26 extends DFA {

        public DFA26(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 26;
            this.eot = dfa_8;
            this.eof = dfa_9;
            this.min = dfa_10;
            this.max = dfa_11;
            this.accept = dfa_12;
            this.special = dfa_13;
            this.transition = dfa_14;
        }
        public String getDescription() {
            return "1830:2: (this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef | this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef )";
        }
    }
    static final String dfa_15s = "\35\uffff";
    static final String dfa_16s = "\1\4\2\uffff\1\4\2\16\3\uffff\1\16\5\uffff\2\4\2\uffff\4\4\1\72\1\21\2\4\2\uffff";
    static final String dfa_17s = "\1\160\2\uffff\1\127\2\76\3\uffff\1\16\5\uffff\1\4\1\160\2\uffff\1\4\2\127\1\4\1\72\1\52\1\127\1\71\2\uffff";
    static final String dfa_18s = "\1\uffff\1\1\1\2\3\uffff\1\3\1\4\1\5\1\uffff\1\10\1\12\1\13\1\14\1\16\2\uffff\1\11\1\15\10\uffff\1\6\1\7";
    static final String dfa_19s = "\35\uffff}>";
    static final String[] dfa_20s = {
            "\1\3\3\2\2\uffff\1\2\3\uffff\1\2\5\uffff\2\2\1\uffff\1\2\7\uffff\2\2\4\uffff\1\2\23\uffff\1\2\1\uffff\1\1\13\uffff\1\6\1\uffff\1\7\1\10\1\11\1\12\2\uffff\1\4\1\5\1\13\1\14\1\15\2\2\2\uffff\1\16\26\uffff\2\2",
            "",
            "",
            "\1\21\11\uffff\1\2\1\uffff\4\2\1\uffff\20\2\1\uffff\5\2\16\uffff\1\20\3\uffff\2\2\30\uffff\1\17",
            "\1\22\1\uffff\4\2\1\uffff\20\2\1\uffff\5\2\16\uffff\1\2\3\uffff\2\2",
            "\1\22\1\uffff\4\2\1\uffff\20\2\1\uffff\5\2\16\uffff\1\2\3\uffff\2\2",
            "",
            "",
            "",
            "\1\23",
            "",
            "",
            "",
            "",
            "",
            "\1\24",
            "\4\2\2\uffff\1\2\3\uffff\1\2\5\uffff\2\2\1\uffff\1\2\7\uffff\2\2\4\uffff\1\2\23\uffff\1\2\1\21\24\uffff\2\2\3\uffff\2\2\31\uffff\2\2",
            "",
            "",
            "\1\25",
            "\1\21\11\uffff\1\2\1\uffff\4\2\1\uffff\20\2\1\uffff\5\2\16\uffff\1\20\3\uffff\2\2\30\uffff\1\17",
            "\1\30\64\uffff\1\27\35\uffff\1\26",
            "\1\31",
            "\1\32",
            "\1\34\30\uffff\1\33",
            "\1\30\64\uffff\1\27\35\uffff\1\26",
            "\1\30\64\uffff\1\27",
            "",
            ""
    };

    static final short[] dfa_15 = DFA.unpackEncodedString(dfa_15s);
    static final char[] dfa_16 = DFA.unpackEncodedStringToUnsignedChars(dfa_16s);
    static final char[] dfa_17 = DFA.unpackEncodedStringToUnsignedChars(dfa_17s);
    static final short[] dfa_18 = DFA.unpackEncodedString(dfa_18s);
    static final short[] dfa_19 = DFA.unpackEncodedString(dfa_19s);
    static final short[][] dfa_20 = unpackEncodedStringArray(dfa_20s);

    class DFA49 extends DFA {

        public DFA49(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 49;
            this.eot = dfa_15;
            this.eof = dfa_15;
            this.min = dfa_16;
            this.max = dfa_17;
            this.accept = dfa_18;
            this.special = dfa_19;
            this.transition = dfa_20;
        }
        public String getDescription() {
            return "3215:2: (this_IQLStatementBlock_0= ruleIQLStatementBlock | this_IQLExpressionStatement_1= ruleIQLExpressionStatement | this_IQLIfStatement_2= ruleIQLIfStatement | this_IQLWhileStatement_3= ruleIQLWhileStatement | this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement | this_IQLForStatement_5= ruleIQLForStatement | this_IQLForEachStatement_6= ruleIQLForEachStatement | this_IQLSwitchStatement_7= ruleIQLSwitchStatement | this_IQLVariableStatement_8= ruleIQLVariableStatement | this_IQLBreakStatement_9= ruleIQLBreakStatement | this_IQLContinueStatement_10= ruleIQLContinueStatement | this_IQLReturnStatement_11= ruleIQLReturnStatement | this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement | this_IQLJavaStatement_13= ruleIQLJavaStatement )";
        }
    }
    static final String dfa_21s = "\25\uffff";
    static final String dfa_22s = "\1\4\5\uffff\1\0\16\uffff";
    static final String dfa_23s = "\1\160\5\uffff\1\0\16\uffff";
    static final String dfa_24s = "\1\uffff\1\1\1\uffff\1\2\1\3\2\uffff\1\5\14\uffff\1\4";
    static final String dfa_25s = "\6\uffff\1\0\16\uffff}>";
    static final String[] dfa_26s = {
            "\4\7\2\uffff\1\7\3\uffff\1\6\5\uffff\1\7\1\1\1\uffff\1\1\7\uffff\2\4\4\uffff\1\3\23\uffff\1\7\25\uffff\2\7\3\uffff\2\7\31\uffff\2\7",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] dfa_21 = DFA.unpackEncodedString(dfa_21s);
    static final char[] dfa_22 = DFA.unpackEncodedStringToUnsignedChars(dfa_22s);
    static final char[] dfa_23 = DFA.unpackEncodedStringToUnsignedChars(dfa_23s);
    static final short[] dfa_24 = DFA.unpackEncodedString(dfa_24s);
    static final short[] dfa_25 = DFA.unpackEncodedString(dfa_25s);
    static final short[][] dfa_26 = unpackEncodedStringArray(dfa_26s);

    class DFA70 extends DFA {

        public DFA70(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 70;
            this.eot = dfa_21;
            this.eof = dfa_21;
            this.min = dfa_22;
            this.max = dfa_23;
            this.accept = dfa_24;
            this.special = dfa_25;
            this.transition = dfa_26;
        }
        public String getDescription() {
            return "5257:2: ( ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) ) | ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) ) | (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? ) )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA70_6 = input.LA(1);

                         
                        int index70_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred13_InternalQDL()) ) {s = 20;}

                        else if ( (true) ) {s = 7;}

                         
                        input.seek(index70_6);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 70, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String dfa_27s = "\1\4\1\16\1\4\2\uffff\1\16";
    static final String[] dfa_28s = {
            "\1\1",
            "\1\4\52\uffff\1\3\1\uffff\1\4\33\uffff\1\2",
            "\1\5",
            "",
            "",
            "\1\4\52\uffff\1\3\1\uffff\1\4\33\uffff\1\2"
    };
    static final char[] dfa_27 = DFA.unpackEncodedStringToUnsignedChars(dfa_27s);
    static final short[][] dfa_28 = unpackEncodedStringArray(dfa_28s);

    class DFA80 extends DFA {

        public DFA80(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 80;
            this.eot = dfa_8;
            this.eof = dfa_8;
            this.min = dfa_27;
            this.max = dfa_11;
            this.accept = dfa_12;
            this.special = dfa_13;
            this.transition = dfa_28;
        }
        public String getDescription() {
            return "5989:4: ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) )";
        }
    }
    static final String dfa_29s = "\14\uffff";
    static final String dfa_30s = "\1\5\10\uffff\1\0\2\uffff";
    static final String dfa_31s = "\1\160\10\uffff\1\0\2\uffff";
    static final String dfa_32s = "\1\uffff\1\1\1\2\1\3\1\4\1\uffff\1\5\1\6\1\7\1\uffff\1\10\1\11";
    static final String dfa_33s = "\11\uffff\1\0\2\uffff}>";
    static final String[] dfa_34s = {
            "\1\1\1\2\1\3\2\uffff\1\6\11\uffff\1\10\44\uffff\1\11\33\uffff\1\7\31\uffff\2\4",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            ""
    };

    static final short[] dfa_29 = DFA.unpackEncodedString(dfa_29s);
    static final char[] dfa_30 = DFA.unpackEncodedStringToUnsignedChars(dfa_30s);
    static final char[] dfa_31 = DFA.unpackEncodedStringToUnsignedChars(dfa_31s);
    static final short[] dfa_32 = DFA.unpackEncodedString(dfa_32s);
    static final short[] dfa_33 = DFA.unpackEncodedString(dfa_33s);
    static final short[][] dfa_34 = unpackEncodedStringArray(dfa_34s);

    class DFA82 extends DFA {

        public DFA82(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 82;
            this.eot = dfa_29;
            this.eof = dfa_29;
            this.min = dfa_30;
            this.max = dfa_31;
            this.accept = dfa_32;
            this.special = dfa_33;
            this.transition = dfa_34;
        }
        public String getDescription() {
            return "6122:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_RANGE ) ) ) | ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' ) | ( () otherlv_15= 'null' ) | ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList ) | this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA82_9 = input.LA(1);

                         
                        int index82_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_InternalQDL()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index82_9);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 82, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000002002L,0x0000000001000025L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000002002L,0x0000000001000024L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000002000L,0x0000000001000024L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0800000000004000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000008010L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000001E00010002L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0200002180B044F0L,0x0001800000318000L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x00000000000C0002L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0xFFFFFFFFFFF3E3F2L,0x0001FFFFFC1FFFBEL});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000024L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000000000010L,0x0000000000000002L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0800000000000000L,0x0000000000000018L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0800000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x8800000000000000L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x1000000000000010L,0x0000000001000040L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0800000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x4800040000004000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0800000000024000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x8000000000008000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x4000000000020000L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x8000000000000002L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000040000000002L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0A000000001000F0L,0x0001800000000000L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0E000000001000F0L,0x0001800000000000L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x8400000000000000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x1A000000001000F0L,0x0001800000000000L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x9000000000000000L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0800000000000002L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0200002180B0C4F0L,0x0001800000318000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x1000000000000010L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x1A00002180B044F0L,0x00018000013F9E80L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0A00002180B044F0L,0x00018000013F9E80L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x1000000000000000L,0x0000000000006000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0A00002180B044F2L,0x00018000013F9E80L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0800040000004000L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x4200002180B044F0L,0x0001800000318000L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0000040055400002L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0000010000000002L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0000024000000002L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0000000000A00002L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x000000002A000002L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x0000000180000002L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x2200000000000002L});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0x0600002180B044F0L,0x0001800000318000L});
    public static final BitSet FOLLOW_62 = new BitSet(new long[]{0x0000000000000002L,0x0000000000400000L});
    public static final BitSet FOLLOW_63 = new BitSet(new long[]{0x0000000000000002L,0x0000000000800000L});
    public static final BitSet FOLLOW_64 = new BitSet(new long[]{0xFFFFFFFFFFF3E3F0L,0x0001FFFFFE1FFFBEL});
    public static final BitSet FOLLOW_65 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});

}