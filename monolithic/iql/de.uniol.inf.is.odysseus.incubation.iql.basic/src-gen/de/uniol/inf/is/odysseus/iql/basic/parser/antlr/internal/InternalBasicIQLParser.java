package de.uniol.inf.is.odysseus.iql.basic.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import de.uniol.inf.is.odysseus.iql.basic.services.BasicIQLGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
@SuppressWarnings("all")
public class InternalBasicIQLParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_INT", "RULE_DOUBLE", "RULE_STRING", "RULE_RANGE", "RULE_WS", "RULE_ANY_OTHER", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "'namespace'", "';'", "'use'", "'static'", "'class'", "'extends'", "'implements'", "','", "'{'", "'}'", "'interface'", "'['", "']'", "'override'", "'('", "')'", "':'", "'='", "'null'", "'if'", "'else'", "'while'", "'do'", "'for'", "'switch'", "'default'", "'case'", "'this'", "'super'", "'break'", "'continue'", "'return'", "'+='", "'-='", "'*='", "'/='", "'%='", "'||'", "'&&'", "'=='", "'!='", "'instanceof'", "'>'", "'>='", "'<'", "'<='", "'+'", "'-'", "'*'", "'/'", "'%'", "'!'", "'++'", "'--'", "'.'", "'new'", "'class('", "'::*'", "'::'", "'$*'", "'*$'", "'~'", "'?:'", "'|'", "'|='", "'^'", "'^='", "'&'", "'&='", "'>>'", "'>>='", "'<<'", "'<<='", "'>>>'", "'>>>='", "'package'", "'abstract'", "'assert'", "'catch'", "'const'", "'enum'", "'final'", "'finally'", "'goto'", "'import'", "'native'", "'private'", "'protected'", "'public'", "'synchronized'", "'throw'", "'throws'", "'transient'", "'try'", "'volatile'", "'strictfp'", "'true'", "'false'"
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
    public static final int T__81=81;
    public static final int T__110=110;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int RULE_WS=9;
    public static final int RULE_ANY_OTHER=10;
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
    public static final int RULE_RANGE=8;

    // delegates
    // delegators


        public InternalBasicIQLParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalBasicIQLParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalBasicIQLParser.tokenNames; }
    public String getGrammarFileName() { return "InternalBasicIQL.g"; }



     	private BasicIQLGrammarAccess grammarAccess;

        public InternalBasicIQLParser(TokenStream input, BasicIQLGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "IQLModel";
       	}

       	@Override
       	protected BasicIQLGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleIQLModel"
    // InternalBasicIQL.g:64:1: entryRuleIQLModel returns [EObject current=null] : iv_ruleIQLModel= ruleIQLModel EOF ;
    public final EObject entryRuleIQLModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLModel = null;


        try {
            // InternalBasicIQL.g:64:49: (iv_ruleIQLModel= ruleIQLModel EOF )
            // InternalBasicIQL.g:65:2: iv_ruleIQLModel= ruleIQLModel EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getIQLModelRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleIQLModel=ruleIQLModel();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleIQLModel; 
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
    // $ANTLR end "entryRuleIQLModel"


    // $ANTLR start "ruleIQLModel"
    // InternalBasicIQL.g:71:1: ruleIQLModel returns [EObject current=null] : ( (otherlv_0= 'namespace' ( (lv_name_1_0= ruleQualifiedName ) ) otherlv_2= ';' )? ( (lv_namespaces_3_0= ruleIQLNamespace ) )* ( (lv_elements_4_0= ruleIQLModelElement ) )* ) ;
    public final EObject ruleIQLModel() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        AntlrDatatypeRuleToken lv_name_1_0 = null;

        EObject lv_namespaces_3_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:77:2: ( ( (otherlv_0= 'namespace' ( (lv_name_1_0= ruleQualifiedName ) ) otherlv_2= ';' )? ( (lv_namespaces_3_0= ruleIQLNamespace ) )* ( (lv_elements_4_0= ruleIQLModelElement ) )* ) )
            // InternalBasicIQL.g:78:2: ( (otherlv_0= 'namespace' ( (lv_name_1_0= ruleQualifiedName ) ) otherlv_2= ';' )? ( (lv_namespaces_3_0= ruleIQLNamespace ) )* ( (lv_elements_4_0= ruleIQLModelElement ) )* )
            {
            // InternalBasicIQL.g:78:2: ( (otherlv_0= 'namespace' ( (lv_name_1_0= ruleQualifiedName ) ) otherlv_2= ';' )? ( (lv_namespaces_3_0= ruleIQLNamespace ) )* ( (lv_elements_4_0= ruleIQLModelElement ) )* )
            // InternalBasicIQL.g:79:3: (otherlv_0= 'namespace' ( (lv_name_1_0= ruleQualifiedName ) ) otherlv_2= ';' )? ( (lv_namespaces_3_0= ruleIQLNamespace ) )* ( (lv_elements_4_0= ruleIQLModelElement ) )*
            {
            // InternalBasicIQL.g:79:3: (otherlv_0= 'namespace' ( (lv_name_1_0= ruleQualifiedName ) ) otherlv_2= ';' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==13) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // InternalBasicIQL.g:80:4: otherlv_0= 'namespace' ( (lv_name_1_0= ruleQualifiedName ) ) otherlv_2= ';'
                    {
                    otherlv_0=(Token)match(input,13,FOLLOW_3); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_0, grammarAccess.getIQLModelAccess().getNamespaceKeyword_0_0());
                      			
                    }
                    // InternalBasicIQL.g:84:4: ( (lv_name_1_0= ruleQualifiedName ) )
                    // InternalBasicIQL.g:85:5: (lv_name_1_0= ruleQualifiedName )
                    {
                    // InternalBasicIQL.g:85:5: (lv_name_1_0= ruleQualifiedName )
                    // InternalBasicIQL.g:86:6: lv_name_1_0= ruleQualifiedName
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLModelAccess().getNameQualifiedNameParserRuleCall_0_1_0());
                      					
                    }
                    pushFollow(FOLLOW_4);
                    lv_name_1_0=ruleQualifiedName();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLModelRule());
                      						}
                      						set(
                      							current,
                      							"name",
                      							lv_name_1_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.QualifiedName");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    otherlv_2=(Token)match(input,14,FOLLOW_5); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_2, grammarAccess.getIQLModelAccess().getSemicolonKeyword_0_2());
                      			
                    }

                    }
                    break;

            }

            // InternalBasicIQL.g:108:3: ( (lv_namespaces_3_0= ruleIQLNamespace ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==15) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalBasicIQL.g:109:4: (lv_namespaces_3_0= ruleIQLNamespace )
            	    {
            	    // InternalBasicIQL.g:109:4: (lv_namespaces_3_0= ruleIQLNamespace )
            	    // InternalBasicIQL.g:110:5: lv_namespaces_3_0= ruleIQLNamespace
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLModelAccess().getNamespacesIQLNamespaceParserRuleCall_1_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_5);
            	    lv_namespaces_3_0=ruleIQLNamespace();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getIQLModelRule());
            	      					}
            	      					add(
            	      						current,
            	      						"namespaces",
            	      						lv_namespaces_3_0,
            	      						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLNamespace");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            // InternalBasicIQL.g:127:3: ( (lv_elements_4_0= ruleIQLModelElement ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==17||LA3_0==23||LA3_0==72) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalBasicIQL.g:128:4: (lv_elements_4_0= ruleIQLModelElement )
            	    {
            	    // InternalBasicIQL.g:128:4: (lv_elements_4_0= ruleIQLModelElement )
            	    // InternalBasicIQL.g:129:5: lv_elements_4_0= ruleIQLModelElement
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLModelAccess().getElementsIQLModelElementParserRuleCall_2_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_6);
            	    lv_elements_4_0=ruleIQLModelElement();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getIQLModelRule());
            	      					}
            	      					add(
            	      						current,
            	      						"elements",
            	      						lv_elements_4_0,
            	      						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLModelElement");
            	      					afterParserOrEnumRuleCall();
            	      				
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
    // $ANTLR end "ruleIQLModel"


    // $ANTLR start "entryRuleIQLModelElement"
    // InternalBasicIQL.g:150:1: entryRuleIQLModelElement returns [EObject current=null] : iv_ruleIQLModelElement= ruleIQLModelElement EOF ;
    public final EObject entryRuleIQLModelElement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLModelElement = null;


        try {
            // InternalBasicIQL.g:150:56: (iv_ruleIQLModelElement= ruleIQLModelElement EOF )
            // InternalBasicIQL.g:151:2: iv_ruleIQLModelElement= ruleIQLModelElement EOF
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
    // InternalBasicIQL.g:157:1: ruleIQLModelElement returns [EObject current=null] : ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) ) ) ;
    public final EObject ruleIQLModelElement() throws RecognitionException {
        EObject current = null;

        EObject lv_javametadata_0_0 = null;

        EObject lv_inner_1_1 = null;

        EObject lv_inner_1_2 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:163:2: ( ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) ) ) )
            // InternalBasicIQL.g:164:2: ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) ) )
            {
            // InternalBasicIQL.g:164:2: ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) ) )
            // InternalBasicIQL.g:165:3: ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) )
            {
            // InternalBasicIQL.g:165:3: ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==72) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalBasicIQL.g:166:4: (lv_javametadata_0_0= ruleIQLJavaMetadata )
            	    {
            	    // InternalBasicIQL.g:166:4: (lv_javametadata_0_0= ruleIQLJavaMetadata )
            	    // InternalBasicIQL.g:167:5: lv_javametadata_0_0= ruleIQLJavaMetadata
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLModelElementAccess().getJavametadataIQLJavaMetadataParserRuleCall_0_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_7);
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
            	    break loop4;
                }
            } while (true);

            // InternalBasicIQL.g:184:3: ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) )
            // InternalBasicIQL.g:185:4: ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) )
            {
            // InternalBasicIQL.g:185:4: ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) )
            // InternalBasicIQL.g:186:5: (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface )
            {
            // InternalBasicIQL.g:186:5: (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==17) ) {
                alt5=1;
            }
            else if ( (LA5_0==23) ) {
                alt5=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // InternalBasicIQL.g:187:6: lv_inner_1_1= ruleIQLClass
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
                    // InternalBasicIQL.g:203:6: lv_inner_1_2= ruleIQLInterface
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
    // InternalBasicIQL.g:225:1: entryRuleIQLNamespace returns [EObject current=null] : iv_ruleIQLNamespace= ruleIQLNamespace EOF ;
    public final EObject entryRuleIQLNamespace() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLNamespace = null;


        try {
            // InternalBasicIQL.g:225:53: (iv_ruleIQLNamespace= ruleIQLNamespace EOF )
            // InternalBasicIQL.g:226:2: iv_ruleIQLNamespace= ruleIQLNamespace EOF
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
    // InternalBasicIQL.g:232:1: ruleIQLNamespace returns [EObject current=null] : (otherlv_0= 'use' ( (lv_static_1_0= 'static' ) )? ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) ) otherlv_3= ';' ) ;
    public final EObject ruleIQLNamespace() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_static_1_0=null;
        Token otherlv_3=null;
        AntlrDatatypeRuleToken lv_importedNamespace_2_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:238:2: ( (otherlv_0= 'use' ( (lv_static_1_0= 'static' ) )? ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) ) otherlv_3= ';' ) )
            // InternalBasicIQL.g:239:2: (otherlv_0= 'use' ( (lv_static_1_0= 'static' ) )? ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) ) otherlv_3= ';' )
            {
            // InternalBasicIQL.g:239:2: (otherlv_0= 'use' ( (lv_static_1_0= 'static' ) )? ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) ) otherlv_3= ';' )
            // InternalBasicIQL.g:240:3: otherlv_0= 'use' ( (lv_static_1_0= 'static' ) )? ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) ) otherlv_3= ';'
            {
            otherlv_0=(Token)match(input,15,FOLLOW_8); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_0, grammarAccess.getIQLNamespaceAccess().getUseKeyword_0());
              		
            }
            // InternalBasicIQL.g:244:3: ( (lv_static_1_0= 'static' ) )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==16) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // InternalBasicIQL.g:245:4: (lv_static_1_0= 'static' )
                    {
                    // InternalBasicIQL.g:245:4: (lv_static_1_0= 'static' )
                    // InternalBasicIQL.g:246:5: lv_static_1_0= 'static'
                    {
                    lv_static_1_0=(Token)match(input,16,FOLLOW_8); if (state.failed) return current;
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

            // InternalBasicIQL.g:258:3: ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) )
            // InternalBasicIQL.g:259:4: (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard )
            {
            // InternalBasicIQL.g:259:4: (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard )
            // InternalBasicIQL.g:260:5: lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLNamespaceAccess().getImportedNamespaceQualifiedNameWithWildcardParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_4);
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

            otherlv_3=(Token)match(input,14,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:285:1: entryRuleIQLClass returns [EObject current=null] : iv_ruleIQLClass= ruleIQLClass EOF ;
    public final EObject entryRuleIQLClass() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLClass = null;


        try {
            // InternalBasicIQL.g:285:49: (iv_ruleIQLClass= ruleIQLClass EOF )
            // InternalBasicIQL.g:286:2: iv_ruleIQLClass= ruleIQLClass EOF
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
    // InternalBasicIQL.g:292:1: ruleIQLClass returns [EObject current=null] : ( () otherlv_1= 'class' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )? (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )? otherlv_9= '{' ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )* otherlv_11= '}' ) ;
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
            // InternalBasicIQL.g:298:2: ( ( () otherlv_1= 'class' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )? (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )? otherlv_9= '{' ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )* otherlv_11= '}' ) )
            // InternalBasicIQL.g:299:2: ( () otherlv_1= 'class' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )? (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )? otherlv_9= '{' ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )* otherlv_11= '}' )
            {
            // InternalBasicIQL.g:299:2: ( () otherlv_1= 'class' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )? (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )? otherlv_9= '{' ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )* otherlv_11= '}' )
            // InternalBasicIQL.g:300:3: () otherlv_1= 'class' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )? (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )? otherlv_9= '{' ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )* otherlv_11= '}'
            {
            // InternalBasicIQL.g:300:3: ()
            // InternalBasicIQL.g:301:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLClassAccess().getIQLClassAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,17,FOLLOW_3); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLClassAccess().getClassKeyword_1());
              		
            }
            // InternalBasicIQL.g:311:3: ( (lv_simpleName_2_0= RULE_ID ) )
            // InternalBasicIQL.g:312:4: (lv_simpleName_2_0= RULE_ID )
            {
            // InternalBasicIQL.g:312:4: (lv_simpleName_2_0= RULE_ID )
            // InternalBasicIQL.g:313:5: lv_simpleName_2_0= RULE_ID
            {
            lv_simpleName_2_0=(Token)match(input,RULE_ID,FOLLOW_9); if (state.failed) return current;
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

            // InternalBasicIQL.g:329:3: (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==18) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // InternalBasicIQL.g:330:4: otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) )
                    {
                    otherlv_3=(Token)match(input,18,FOLLOW_3); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getIQLClassAccess().getExtendsKeyword_3_0());
                      			
                    }
                    // InternalBasicIQL.g:334:4: ( (lv_extendedClass_4_0= ruleJvmTypeReference ) )
                    // InternalBasicIQL.g:335:5: (lv_extendedClass_4_0= ruleJvmTypeReference )
                    {
                    // InternalBasicIQL.g:335:5: (lv_extendedClass_4_0= ruleJvmTypeReference )
                    // InternalBasicIQL.g:336:6: lv_extendedClass_4_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLClassAccess().getExtendedClassJvmTypeReferenceParserRuleCall_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_10);
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

            // InternalBasicIQL.g:354:3: (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==19) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // InternalBasicIQL.g:355:4: otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )*
                    {
                    otherlv_5=(Token)match(input,19,FOLLOW_3); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getIQLClassAccess().getImplementsKeyword_4_0());
                      			
                    }
                    // InternalBasicIQL.g:359:4: ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) )
                    // InternalBasicIQL.g:360:5: (lv_extendedInterfaces_6_0= ruleJvmTypeReference )
                    {
                    // InternalBasicIQL.g:360:5: (lv_extendedInterfaces_6_0= ruleJvmTypeReference )
                    // InternalBasicIQL.g:361:6: lv_extendedInterfaces_6_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLClassAccess().getExtendedInterfacesJvmTypeReferenceParserRuleCall_4_1_0());
                      					
                    }
                    pushFollow(FOLLOW_11);
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

                    // InternalBasicIQL.g:378:4: (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )*
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( (LA8_0==20) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // InternalBasicIQL.g:379:5: otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) )
                    	    {
                    	    otherlv_7=(Token)match(input,20,FOLLOW_3); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_7, grammarAccess.getIQLClassAccess().getCommaKeyword_4_2_0());
                    	      				
                    	    }
                    	    // InternalBasicIQL.g:383:5: ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) )
                    	    // InternalBasicIQL.g:384:6: (lv_extendedInterfaces_8_0= ruleJvmTypeReference )
                    	    {
                    	    // InternalBasicIQL.g:384:6: (lv_extendedInterfaces_8_0= ruleJvmTypeReference )
                    	    // InternalBasicIQL.g:385:7: lv_extendedInterfaces_8_0= ruleJvmTypeReference
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLClassAccess().getExtendedInterfacesJvmTypeReferenceParserRuleCall_4_2_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_11);
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
                    	    break loop8;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_9=(Token)match(input,21,FOLLOW_12); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_9, grammarAccess.getIQLClassAccess().getLeftCurlyBracketKeyword_5());
              		
            }
            // InternalBasicIQL.g:408:3: ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==RULE_ID||LA11_0==26||LA11_0==72) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // InternalBasicIQL.g:409:4: ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) )
            	    {
            	    // InternalBasicIQL.g:409:4: ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) )
            	    // InternalBasicIQL.g:410:5: (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember )
            	    {
            	    // InternalBasicIQL.g:410:5: (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember )
            	    int alt10=3;
            	    switch ( input.LA(1) ) {
            	    case RULE_ID:
            	        {
            	        int LA10_1 = input.LA(2);

            	        if ( (LA10_1==21||LA10_1==27||LA10_1==29) ) {
            	            alt10=2;
            	        }
            	        else if ( (LA10_1==RULE_ID||LA10_1==24||LA10_1==71) ) {
            	            alt10=1;
            	        }
            	        else {
            	            if (state.backtracking>0) {state.failed=true; return current;}
            	            NoViableAltException nvae =
            	                new NoViableAltException("", 10, 1, input);

            	            throw nvae;
            	        }
            	        }
            	        break;
            	    case 26:
            	        {
            	        alt10=2;
            	        }
            	        break;
            	    case 72:
            	        {
            	        alt10=3;
            	        }
            	        break;
            	    default:
            	        if (state.backtracking>0) {state.failed=true; return current;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 10, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt10) {
            	        case 1 :
            	            // InternalBasicIQL.g:411:6: lv_members_10_1= ruleIQLAttribute
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getIQLClassAccess().getMembersIQLAttributeParserRuleCall_6_0_0());
            	              					
            	            }
            	            pushFollow(FOLLOW_12);
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
            	            // InternalBasicIQL.g:427:6: lv_members_10_2= ruleIQLMethod
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getIQLClassAccess().getMembersIQLMethodParserRuleCall_6_0_1());
            	              					
            	            }
            	            pushFollow(FOLLOW_12);
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
            	            // InternalBasicIQL.g:443:6: lv_members_10_3= ruleIQLJavaMember
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getIQLClassAccess().getMembersIQLJavaMemberParserRuleCall_6_0_2());
            	              					
            	            }
            	            pushFollow(FOLLOW_12);
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
            	    break loop11;
                }
            } while (true);

            otherlv_11=(Token)match(input,22,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:469:1: entryRuleIQLInterface returns [EObject current=null] : iv_ruleIQLInterface= ruleIQLInterface EOF ;
    public final EObject entryRuleIQLInterface() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLInterface = null;


        try {
            // InternalBasicIQL.g:469:53: (iv_ruleIQLInterface= ruleIQLInterface EOF )
            // InternalBasicIQL.g:470:2: iv_ruleIQLInterface= ruleIQLInterface EOF
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
    // InternalBasicIQL.g:476:1: ruleIQLInterface returns [EObject current=null] : ( () otherlv_1= 'interface' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )? otherlv_7= '{' ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )* otherlv_9= '}' ) ;
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
            // InternalBasicIQL.g:482:2: ( ( () otherlv_1= 'interface' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )? otherlv_7= '{' ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )* otherlv_9= '}' ) )
            // InternalBasicIQL.g:483:2: ( () otherlv_1= 'interface' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )? otherlv_7= '{' ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )* otherlv_9= '}' )
            {
            // InternalBasicIQL.g:483:2: ( () otherlv_1= 'interface' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )? otherlv_7= '{' ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )* otherlv_9= '}' )
            // InternalBasicIQL.g:484:3: () otherlv_1= 'interface' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )? otherlv_7= '{' ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )* otherlv_9= '}'
            {
            // InternalBasicIQL.g:484:3: ()
            // InternalBasicIQL.g:485:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLInterfaceAccess().getIQLInterfaceAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,23,FOLLOW_3); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLInterfaceAccess().getInterfaceKeyword_1());
              		
            }
            // InternalBasicIQL.g:495:3: ( (lv_simpleName_2_0= RULE_ID ) )
            // InternalBasicIQL.g:496:4: (lv_simpleName_2_0= RULE_ID )
            {
            // InternalBasicIQL.g:496:4: (lv_simpleName_2_0= RULE_ID )
            // InternalBasicIQL.g:497:5: lv_simpleName_2_0= RULE_ID
            {
            lv_simpleName_2_0=(Token)match(input,RULE_ID,FOLLOW_13); if (state.failed) return current;
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

            // InternalBasicIQL.g:513:3: (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==18) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // InternalBasicIQL.g:514:4: otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )?
                    {
                    otherlv_3=(Token)match(input,18,FOLLOW_3); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getIQLInterfaceAccess().getExtendsKeyword_3_0());
                      			
                    }
                    // InternalBasicIQL.g:518:4: ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) )
                    // InternalBasicIQL.g:519:5: (lv_extendedInterfaces_4_0= ruleJvmTypeReference )
                    {
                    // InternalBasicIQL.g:519:5: (lv_extendedInterfaces_4_0= ruleJvmTypeReference )
                    // InternalBasicIQL.g:520:6: lv_extendedInterfaces_4_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLInterfaceAccess().getExtendedInterfacesJvmTypeReferenceParserRuleCall_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_11);
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

                    // InternalBasicIQL.g:537:4: (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==20) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // InternalBasicIQL.g:538:5: otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) )
                            {
                            otherlv_5=(Token)match(input,20,FOLLOW_3); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              					newLeafNode(otherlv_5, grammarAccess.getIQLInterfaceAccess().getCommaKeyword_3_2_0());
                              				
                            }
                            // InternalBasicIQL.g:542:5: ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) )
                            // InternalBasicIQL.g:543:6: (lv_extendedInterfaces_6_0= ruleJvmTypeReference )
                            {
                            // InternalBasicIQL.g:543:6: (lv_extendedInterfaces_6_0= ruleJvmTypeReference )
                            // InternalBasicIQL.g:544:7: lv_extendedInterfaces_6_0= ruleJvmTypeReference
                            {
                            if ( state.backtracking==0 ) {

                              							newCompositeNode(grammarAccess.getIQLInterfaceAccess().getExtendedInterfacesJvmTypeReferenceParserRuleCall_3_2_1_0());
                              						
                            }
                            pushFollow(FOLLOW_14);
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

            otherlv_7=(Token)match(input,21,FOLLOW_12); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_7, grammarAccess.getIQLInterfaceAccess().getLeftCurlyBracketKeyword_4());
              		
            }
            // InternalBasicIQL.g:567:3: ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==RULE_ID||LA15_0==72) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // InternalBasicIQL.g:568:4: ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) )
            	    {
            	    // InternalBasicIQL.g:568:4: ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) )
            	    // InternalBasicIQL.g:569:5: (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember )
            	    {
            	    // InternalBasicIQL.g:569:5: (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember )
            	    int alt14=2;
            	    int LA14_0 = input.LA(1);

            	    if ( (LA14_0==RULE_ID) ) {
            	        alt14=1;
            	    }
            	    else if ( (LA14_0==72) ) {
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
            	            // InternalBasicIQL.g:570:6: lv_members_8_1= ruleIQLMethodDeclaration
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getIQLInterfaceAccess().getMembersIQLMethodDeclarationParserRuleCall_5_0_0());
            	              					
            	            }
            	            pushFollow(FOLLOW_12);
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
            	            // InternalBasicIQL.g:586:6: lv_members_8_2= ruleIQLJavaMember
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getIQLInterfaceAccess().getMembersIQLJavaMemberParserRuleCall_5_0_1());
            	              					
            	            }
            	            pushFollow(FOLLOW_12);
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
            	    break loop15;
                }
            } while (true);

            otherlv_9=(Token)match(input,22,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:612:1: entryRuleIQLJavaMetadata returns [EObject current=null] : iv_ruleIQLJavaMetadata= ruleIQLJavaMetadata EOF ;
    public final EObject entryRuleIQLJavaMetadata() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLJavaMetadata = null;


        try {
            // InternalBasicIQL.g:612:56: (iv_ruleIQLJavaMetadata= ruleIQLJavaMetadata EOF )
            // InternalBasicIQL.g:613:2: iv_ruleIQLJavaMetadata= ruleIQLJavaMetadata EOF
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
    // InternalBasicIQL.g:619:1: ruleIQLJavaMetadata returns [EObject current=null] : ( (lv_java_0_0= ruleIQLJava ) ) ;
    public final EObject ruleIQLJavaMetadata() throws RecognitionException {
        EObject current = null;

        EObject lv_java_0_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:625:2: ( ( (lv_java_0_0= ruleIQLJava ) ) )
            // InternalBasicIQL.g:626:2: ( (lv_java_0_0= ruleIQLJava ) )
            {
            // InternalBasicIQL.g:626:2: ( (lv_java_0_0= ruleIQLJava ) )
            // InternalBasicIQL.g:627:3: (lv_java_0_0= ruleIQLJava )
            {
            // InternalBasicIQL.g:627:3: (lv_java_0_0= ruleIQLJava )
            // InternalBasicIQL.g:628:4: lv_java_0_0= ruleIQLJava
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
    // InternalBasicIQL.g:648:1: entryRuleIQLAttribute returns [EObject current=null] : iv_ruleIQLAttribute= ruleIQLAttribute EOF ;
    public final EObject entryRuleIQLAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLAttribute = null;


        try {
            // InternalBasicIQL.g:648:53: (iv_ruleIQLAttribute= ruleIQLAttribute EOF )
            // InternalBasicIQL.g:649:2: iv_ruleIQLAttribute= ruleIQLAttribute EOF
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
    // InternalBasicIQL.g:655:1: ruleIQLAttribute returns [EObject current=null] : ( () ( (lv_type_1_0= ruleJvmTypeReference ) ) ( (lv_simpleName_2_0= RULE_ID ) ) ( (lv_init_3_0= ruleIQLVariableInitialization ) )? otherlv_4= ';' ) ;
    public final EObject ruleIQLAttribute() throws RecognitionException {
        EObject current = null;

        Token lv_simpleName_2_0=null;
        Token otherlv_4=null;
        EObject lv_type_1_0 = null;

        EObject lv_init_3_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:661:2: ( ( () ( (lv_type_1_0= ruleJvmTypeReference ) ) ( (lv_simpleName_2_0= RULE_ID ) ) ( (lv_init_3_0= ruleIQLVariableInitialization ) )? otherlv_4= ';' ) )
            // InternalBasicIQL.g:662:2: ( () ( (lv_type_1_0= ruleJvmTypeReference ) ) ( (lv_simpleName_2_0= RULE_ID ) ) ( (lv_init_3_0= ruleIQLVariableInitialization ) )? otherlv_4= ';' )
            {
            // InternalBasicIQL.g:662:2: ( () ( (lv_type_1_0= ruleJvmTypeReference ) ) ( (lv_simpleName_2_0= RULE_ID ) ) ( (lv_init_3_0= ruleIQLVariableInitialization ) )? otherlv_4= ';' )
            // InternalBasicIQL.g:663:3: () ( (lv_type_1_0= ruleJvmTypeReference ) ) ( (lv_simpleName_2_0= RULE_ID ) ) ( (lv_init_3_0= ruleIQLVariableInitialization ) )? otherlv_4= ';'
            {
            // InternalBasicIQL.g:663:3: ()
            // InternalBasicIQL.g:664:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLAttributeAccess().getIQLAttributeAction_0(),
              					current);
              			
            }

            }

            // InternalBasicIQL.g:670:3: ( (lv_type_1_0= ruleJvmTypeReference ) )
            // InternalBasicIQL.g:671:4: (lv_type_1_0= ruleJvmTypeReference )
            {
            // InternalBasicIQL.g:671:4: (lv_type_1_0= ruleJvmTypeReference )
            // InternalBasicIQL.g:672:5: lv_type_1_0= ruleJvmTypeReference
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLAttributeAccess().getTypeJvmTypeReferenceParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_3);
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

            // InternalBasicIQL.g:689:3: ( (lv_simpleName_2_0= RULE_ID ) )
            // InternalBasicIQL.g:690:4: (lv_simpleName_2_0= RULE_ID )
            {
            // InternalBasicIQL.g:690:4: (lv_simpleName_2_0= RULE_ID )
            // InternalBasicIQL.g:691:5: lv_simpleName_2_0= RULE_ID
            {
            lv_simpleName_2_0=(Token)match(input,RULE_ID,FOLLOW_15); if (state.failed) return current;
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

            // InternalBasicIQL.g:707:3: ( (lv_init_3_0= ruleIQLVariableInitialization ) )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==21||LA16_0==27||LA16_0==30) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // InternalBasicIQL.g:708:4: (lv_init_3_0= ruleIQLVariableInitialization )
                    {
                    // InternalBasicIQL.g:708:4: (lv_init_3_0= ruleIQLVariableInitialization )
                    // InternalBasicIQL.g:709:5: lv_init_3_0= ruleIQLVariableInitialization
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getIQLAttributeAccess().getInitIQLVariableInitializationParserRuleCall_3_0());
                      				
                    }
                    pushFollow(FOLLOW_4);
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

            otherlv_4=(Token)match(input,14,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:734:1: entryRuleJvmTypeReference returns [EObject current=null] : iv_ruleJvmTypeReference= ruleJvmTypeReference EOF ;
    public final EObject entryRuleJvmTypeReference() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJvmTypeReference = null;


        try {
            // InternalBasicIQL.g:734:57: (iv_ruleJvmTypeReference= ruleJvmTypeReference EOF )
            // InternalBasicIQL.g:735:2: iv_ruleJvmTypeReference= ruleJvmTypeReference EOF
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
    // InternalBasicIQL.g:741:1: ruleJvmTypeReference returns [EObject current=null] : (this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef | this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef ) ;
    public final EObject ruleJvmTypeReference() throws RecognitionException {
        EObject current = null;

        EObject this_IQLSimpleTypeRef_0 = null;

        EObject this_IQLArrayTypeRef_1 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:747:2: ( (this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef | this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef ) )
            // InternalBasicIQL.g:748:2: (this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef | this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef )
            {
            // InternalBasicIQL.g:748:2: (this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef | this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef )
            int alt17=2;
            alt17 = dfa17.predict(input);
            switch (alt17) {
                case 1 :
                    // InternalBasicIQL.g:749:3: this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef
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
                    // InternalBasicIQL.g:758:3: this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef
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
    // InternalBasicIQL.g:770:1: entryRuleIQLSimpleTypeRef returns [EObject current=null] : iv_ruleIQLSimpleTypeRef= ruleIQLSimpleTypeRef EOF ;
    public final EObject entryRuleIQLSimpleTypeRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLSimpleTypeRef = null;


        try {
            // InternalBasicIQL.g:770:57: (iv_ruleIQLSimpleTypeRef= ruleIQLSimpleTypeRef EOF )
            // InternalBasicIQL.g:771:2: iv_ruleIQLSimpleTypeRef= ruleIQLSimpleTypeRef EOF
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
    // InternalBasicIQL.g:777:1: ruleIQLSimpleTypeRef returns [EObject current=null] : ( () ( ( ruleQualifiedName ) ) ) ;
    public final EObject ruleIQLSimpleTypeRef() throws RecognitionException {
        EObject current = null;


        	enterRule();

        try {
            // InternalBasicIQL.g:783:2: ( ( () ( ( ruleQualifiedName ) ) ) )
            // InternalBasicIQL.g:784:2: ( () ( ( ruleQualifiedName ) ) )
            {
            // InternalBasicIQL.g:784:2: ( () ( ( ruleQualifiedName ) ) )
            // InternalBasicIQL.g:785:3: () ( ( ruleQualifiedName ) )
            {
            // InternalBasicIQL.g:785:3: ()
            // InternalBasicIQL.g:786:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLSimpleTypeRefAccess().getIQLSimpleTypeRefAction_0(),
              					current);
              			
            }

            }

            // InternalBasicIQL.g:792:3: ( ( ruleQualifiedName ) )
            // InternalBasicIQL.g:793:4: ( ruleQualifiedName )
            {
            // InternalBasicIQL.g:793:4: ( ruleQualifiedName )
            // InternalBasicIQL.g:794:5: ruleQualifiedName
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
    // InternalBasicIQL.g:812:1: entryRuleIQLArrayTypeRef returns [EObject current=null] : iv_ruleIQLArrayTypeRef= ruleIQLArrayTypeRef EOF ;
    public final EObject entryRuleIQLArrayTypeRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLArrayTypeRef = null;


        try {
            // InternalBasicIQL.g:812:56: (iv_ruleIQLArrayTypeRef= ruleIQLArrayTypeRef EOF )
            // InternalBasicIQL.g:813:2: iv_ruleIQLArrayTypeRef= ruleIQLArrayTypeRef EOF
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
    // InternalBasicIQL.g:819:1: ruleIQLArrayTypeRef returns [EObject current=null] : ( () ( (lv_type_1_0= ruleIQLArrayType ) ) ) ;
    public final EObject ruleIQLArrayTypeRef() throws RecognitionException {
        EObject current = null;

        EObject lv_type_1_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:825:2: ( ( () ( (lv_type_1_0= ruleIQLArrayType ) ) ) )
            // InternalBasicIQL.g:826:2: ( () ( (lv_type_1_0= ruleIQLArrayType ) ) )
            {
            // InternalBasicIQL.g:826:2: ( () ( (lv_type_1_0= ruleIQLArrayType ) ) )
            // InternalBasicIQL.g:827:3: () ( (lv_type_1_0= ruleIQLArrayType ) )
            {
            // InternalBasicIQL.g:827:3: ()
            // InternalBasicIQL.g:828:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLArrayTypeRefAccess().getIQLArrayTypeRefAction_0(),
              					current);
              			
            }

            }

            // InternalBasicIQL.g:834:3: ( (lv_type_1_0= ruleIQLArrayType ) )
            // InternalBasicIQL.g:835:4: (lv_type_1_0= ruleIQLArrayType )
            {
            // InternalBasicIQL.g:835:4: (lv_type_1_0= ruleIQLArrayType )
            // InternalBasicIQL.g:836:5: lv_type_1_0= ruleIQLArrayType
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
    // InternalBasicIQL.g:857:1: entryRuleIQLArrayType returns [EObject current=null] : iv_ruleIQLArrayType= ruleIQLArrayType EOF ;
    public final EObject entryRuleIQLArrayType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLArrayType = null;


        try {
            // InternalBasicIQL.g:857:53: (iv_ruleIQLArrayType= ruleIQLArrayType EOF )
            // InternalBasicIQL.g:858:2: iv_ruleIQLArrayType= ruleIQLArrayType EOF
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
    // InternalBasicIQL.g:864:1: ruleIQLArrayType returns [EObject current=null] : ( () ( ( ruleQualifiedName ) ) ( (lv_dimensions_2_0= ruleArrayBrackets ) )+ ) ;
    public final EObject ruleIQLArrayType() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_dimensions_2_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:870:2: ( ( () ( ( ruleQualifiedName ) ) ( (lv_dimensions_2_0= ruleArrayBrackets ) )+ ) )
            // InternalBasicIQL.g:871:2: ( () ( ( ruleQualifiedName ) ) ( (lv_dimensions_2_0= ruleArrayBrackets ) )+ )
            {
            // InternalBasicIQL.g:871:2: ( () ( ( ruleQualifiedName ) ) ( (lv_dimensions_2_0= ruleArrayBrackets ) )+ )
            // InternalBasicIQL.g:872:3: () ( ( ruleQualifiedName ) ) ( (lv_dimensions_2_0= ruleArrayBrackets ) )+
            {
            // InternalBasicIQL.g:872:3: ()
            // InternalBasicIQL.g:873:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLArrayTypeAccess().getIQLArrayTypeAction_0(),
              					current);
              			
            }

            }

            // InternalBasicIQL.g:879:3: ( ( ruleQualifiedName ) )
            // InternalBasicIQL.g:880:4: ( ruleQualifiedName )
            {
            // InternalBasicIQL.g:880:4: ( ruleQualifiedName )
            // InternalBasicIQL.g:881:5: ruleQualifiedName
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLArrayTypeRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLArrayTypeAccess().getComponentTypeJvmTypeCrossReference_1_0());
              				
            }
            pushFollow(FOLLOW_16);
            ruleQualifiedName();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalBasicIQL.g:895:3: ( (lv_dimensions_2_0= ruleArrayBrackets ) )+
            int cnt18=0;
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==24) ) {
                    int LA18_2 = input.LA(2);

                    if ( (LA18_2==25) ) {
                        alt18=1;
                    }


                }


                switch (alt18) {
            	case 1 :
            	    // InternalBasicIQL.g:896:4: (lv_dimensions_2_0= ruleArrayBrackets )
            	    {
            	    // InternalBasicIQL.g:896:4: (lv_dimensions_2_0= ruleArrayBrackets )
            	    // InternalBasicIQL.g:897:5: lv_dimensions_2_0= ruleArrayBrackets
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLArrayTypeAccess().getDimensionsArrayBracketsParserRuleCall_2_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_17);
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
            	    if ( cnt18 >= 1 ) break loop18;
            	    if (state.backtracking>0) {state.failed=true; return current;}
                        EarlyExitException eee =
                            new EarlyExitException(18, input);
                        throw eee;
                }
                cnt18++;
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
    // InternalBasicIQL.g:918:1: entryRuleArrayBrackets returns [String current=null] : iv_ruleArrayBrackets= ruleArrayBrackets EOF ;
    public final String entryRuleArrayBrackets() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleArrayBrackets = null;


        try {
            // InternalBasicIQL.g:918:53: (iv_ruleArrayBrackets= ruleArrayBrackets EOF )
            // InternalBasicIQL.g:919:2: iv_ruleArrayBrackets= ruleArrayBrackets EOF
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
    // InternalBasicIQL.g:925:1: ruleArrayBrackets returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '[' kw= ']' ) ;
    public final AntlrDatatypeRuleToken ruleArrayBrackets() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:931:2: ( (kw= '[' kw= ']' ) )
            // InternalBasicIQL.g:932:2: (kw= '[' kw= ']' )
            {
            // InternalBasicIQL.g:932:2: (kw= '[' kw= ']' )
            // InternalBasicIQL.g:933:3: kw= '[' kw= ']'
            {
            kw=(Token)match(input,24,FOLLOW_18); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current.merge(kw);
              			newLeafNode(kw, grammarAccess.getArrayBracketsAccess().getLeftSquareBracketKeyword_0());
              		
            }
            kw=(Token)match(input,25,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:947:1: entryRuleJvmFormalParameter returns [EObject current=null] : iv_ruleJvmFormalParameter= ruleJvmFormalParameter EOF ;
    public final EObject entryRuleJvmFormalParameter() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJvmFormalParameter = null;


        try {
            // InternalBasicIQL.g:947:59: (iv_ruleJvmFormalParameter= ruleJvmFormalParameter EOF )
            // InternalBasicIQL.g:948:2: iv_ruleJvmFormalParameter= ruleJvmFormalParameter EOF
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
    // InternalBasicIQL.g:954:1: ruleJvmFormalParameter returns [EObject current=null] : ( ( (lv_parameterType_0_0= ruleJvmTypeReference ) ) ( (lv_name_1_0= RULE_ID ) ) ) ;
    public final EObject ruleJvmFormalParameter() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_0=null;
        EObject lv_parameterType_0_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:960:2: ( ( ( (lv_parameterType_0_0= ruleJvmTypeReference ) ) ( (lv_name_1_0= RULE_ID ) ) ) )
            // InternalBasicIQL.g:961:2: ( ( (lv_parameterType_0_0= ruleJvmTypeReference ) ) ( (lv_name_1_0= RULE_ID ) ) )
            {
            // InternalBasicIQL.g:961:2: ( ( (lv_parameterType_0_0= ruleJvmTypeReference ) ) ( (lv_name_1_0= RULE_ID ) ) )
            // InternalBasicIQL.g:962:3: ( (lv_parameterType_0_0= ruleJvmTypeReference ) ) ( (lv_name_1_0= RULE_ID ) )
            {
            // InternalBasicIQL.g:962:3: ( (lv_parameterType_0_0= ruleJvmTypeReference ) )
            // InternalBasicIQL.g:963:4: (lv_parameterType_0_0= ruleJvmTypeReference )
            {
            // InternalBasicIQL.g:963:4: (lv_parameterType_0_0= ruleJvmTypeReference )
            // InternalBasicIQL.g:964:5: lv_parameterType_0_0= ruleJvmTypeReference
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getJvmFormalParameterAccess().getParameterTypeJvmTypeReferenceParserRuleCall_0_0());
              				
            }
            pushFollow(FOLLOW_3);
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

            // InternalBasicIQL.g:981:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalBasicIQL.g:982:4: (lv_name_1_0= RULE_ID )
            {
            // InternalBasicIQL.g:982:4: (lv_name_1_0= RULE_ID )
            // InternalBasicIQL.g:983:5: lv_name_1_0= RULE_ID
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
    // InternalBasicIQL.g:1003:1: entryRuleIQLMethod returns [EObject current=null] : iv_ruleIQLMethod= ruleIQLMethod EOF ;
    public final EObject entryRuleIQLMethod() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMethod = null;


        try {
            // InternalBasicIQL.g:1003:50: (iv_ruleIQLMethod= ruleIQLMethod EOF )
            // InternalBasicIQL.g:1004:2: iv_ruleIQLMethod= ruleIQLMethod EOF
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
    // InternalBasicIQL.g:1010:1: ruleIQLMethod returns [EObject current=null] : ( () ( (lv_override_1_0= 'override' ) )? ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )? (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )? ( (lv_body_10_0= ruleIQLStatementBlock ) ) ) ;
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
            // InternalBasicIQL.g:1016:2: ( ( () ( (lv_override_1_0= 'override' ) )? ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )? (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )? ( (lv_body_10_0= ruleIQLStatementBlock ) ) ) )
            // InternalBasicIQL.g:1017:2: ( () ( (lv_override_1_0= 'override' ) )? ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )? (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )? ( (lv_body_10_0= ruleIQLStatementBlock ) ) )
            {
            // InternalBasicIQL.g:1017:2: ( () ( (lv_override_1_0= 'override' ) )? ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )? (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )? ( (lv_body_10_0= ruleIQLStatementBlock ) ) )
            // InternalBasicIQL.g:1018:3: () ( (lv_override_1_0= 'override' ) )? ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )? (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )? ( (lv_body_10_0= ruleIQLStatementBlock ) )
            {
            // InternalBasicIQL.g:1018:3: ()
            // InternalBasicIQL.g:1019:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLMethodAccess().getIQLMethodAction_0(),
              					current);
              			
            }

            }

            // InternalBasicIQL.g:1025:3: ( (lv_override_1_0= 'override' ) )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==26) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // InternalBasicIQL.g:1026:4: (lv_override_1_0= 'override' )
                    {
                    // InternalBasicIQL.g:1026:4: (lv_override_1_0= 'override' )
                    // InternalBasicIQL.g:1027:5: lv_override_1_0= 'override'
                    {
                    lv_override_1_0=(Token)match(input,26,FOLLOW_3); if (state.failed) return current;
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

            // InternalBasicIQL.g:1039:3: ( (lv_simpleName_2_0= RULE_ID ) )
            // InternalBasicIQL.g:1040:4: (lv_simpleName_2_0= RULE_ID )
            {
            // InternalBasicIQL.g:1040:4: (lv_simpleName_2_0= RULE_ID )
            // InternalBasicIQL.g:1041:5: lv_simpleName_2_0= RULE_ID
            {
            lv_simpleName_2_0=(Token)match(input,RULE_ID,FOLLOW_19); if (state.failed) return current;
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

            // InternalBasicIQL.g:1057:3: (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==27) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // InternalBasicIQL.g:1058:4: otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')'
                    {
                    otherlv_3=(Token)match(input,27,FOLLOW_20); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getIQLMethodAccess().getLeftParenthesisKeyword_3_0());
                      			
                    }
                    // InternalBasicIQL.g:1062:4: ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )?
                    int alt21=2;
                    int LA21_0 = input.LA(1);

                    if ( (LA21_0==RULE_ID) ) {
                        alt21=1;
                    }
                    switch (alt21) {
                        case 1 :
                            // InternalBasicIQL.g:1063:5: ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )*
                            {
                            // InternalBasicIQL.g:1063:5: ( (lv_parameters_4_0= ruleJvmFormalParameter ) )
                            // InternalBasicIQL.g:1064:6: (lv_parameters_4_0= ruleJvmFormalParameter )
                            {
                            // InternalBasicIQL.g:1064:6: (lv_parameters_4_0= ruleJvmFormalParameter )
                            // InternalBasicIQL.g:1065:7: lv_parameters_4_0= ruleJvmFormalParameter
                            {
                            if ( state.backtracking==0 ) {

                              							newCompositeNode(grammarAccess.getIQLMethodAccess().getParametersJvmFormalParameterParserRuleCall_3_1_0_0());
                              						
                            }
                            pushFollow(FOLLOW_21);
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

                            // InternalBasicIQL.g:1082:5: (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )*
                            loop20:
                            do {
                                int alt20=2;
                                int LA20_0 = input.LA(1);

                                if ( (LA20_0==20) ) {
                                    alt20=1;
                                }


                                switch (alt20) {
                            	case 1 :
                            	    // InternalBasicIQL.g:1083:6: otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) )
                            	    {
                            	    otherlv_5=(Token)match(input,20,FOLLOW_3); if (state.failed) return current;
                            	    if ( state.backtracking==0 ) {

                            	      						newLeafNode(otherlv_5, grammarAccess.getIQLMethodAccess().getCommaKeyword_3_1_1_0());
                            	      					
                            	    }
                            	    // InternalBasicIQL.g:1087:6: ( (lv_parameters_6_0= ruleJvmFormalParameter ) )
                            	    // InternalBasicIQL.g:1088:7: (lv_parameters_6_0= ruleJvmFormalParameter )
                            	    {
                            	    // InternalBasicIQL.g:1088:7: (lv_parameters_6_0= ruleJvmFormalParameter )
                            	    // InternalBasicIQL.g:1089:8: lv_parameters_6_0= ruleJvmFormalParameter
                            	    {
                            	    if ( state.backtracking==0 ) {

                            	      								newCompositeNode(grammarAccess.getIQLMethodAccess().getParametersJvmFormalParameterParserRuleCall_3_1_1_1_0());
                            	      							
                            	    }
                            	    pushFollow(FOLLOW_21);
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
                            	    break loop20;
                                }
                            } while (true);


                            }
                            break;

                    }

                    otherlv_7=(Token)match(input,28,FOLLOW_19); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_7, grammarAccess.getIQLMethodAccess().getRightParenthesisKeyword_3_2());
                      			
                    }

                    }
                    break;

            }

            // InternalBasicIQL.g:1113:3: (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==29) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // InternalBasicIQL.g:1114:4: otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) )
                    {
                    otherlv_8=(Token)match(input,29,FOLLOW_3); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_8, grammarAccess.getIQLMethodAccess().getColonKeyword_4_0());
                      			
                    }
                    // InternalBasicIQL.g:1118:4: ( (lv_returnType_9_0= ruleJvmTypeReference ) )
                    // InternalBasicIQL.g:1119:5: (lv_returnType_9_0= ruleJvmTypeReference )
                    {
                    // InternalBasicIQL.g:1119:5: (lv_returnType_9_0= ruleJvmTypeReference )
                    // InternalBasicIQL.g:1120:6: lv_returnType_9_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMethodAccess().getReturnTypeJvmTypeReferenceParserRuleCall_4_1_0());
                      					
                    }
                    pushFollow(FOLLOW_19);
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

            // InternalBasicIQL.g:1138:3: ( (lv_body_10_0= ruleIQLStatementBlock ) )
            // InternalBasicIQL.g:1139:4: (lv_body_10_0= ruleIQLStatementBlock )
            {
            // InternalBasicIQL.g:1139:4: (lv_body_10_0= ruleIQLStatementBlock )
            // InternalBasicIQL.g:1140:5: lv_body_10_0= ruleIQLStatementBlock
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
    // InternalBasicIQL.g:1161:1: entryRuleIQLMethodDeclaration returns [EObject current=null] : iv_ruleIQLMethodDeclaration= ruleIQLMethodDeclaration EOF ;
    public final EObject entryRuleIQLMethodDeclaration() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMethodDeclaration = null;


        try {
            // InternalBasicIQL.g:1161:61: (iv_ruleIQLMethodDeclaration= ruleIQLMethodDeclaration EOF )
            // InternalBasicIQL.g:1162:2: iv_ruleIQLMethodDeclaration= ruleIQLMethodDeclaration EOF
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
    // InternalBasicIQL.g:1168:1: ruleIQLMethodDeclaration returns [EObject current=null] : ( () ( (lv_simpleName_1_0= RULE_ID ) ) otherlv_2= '(' ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )? otherlv_6= ')' (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )? otherlv_9= ';' ) ;
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
            // InternalBasicIQL.g:1174:2: ( ( () ( (lv_simpleName_1_0= RULE_ID ) ) otherlv_2= '(' ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )? otherlv_6= ')' (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )? otherlv_9= ';' ) )
            // InternalBasicIQL.g:1175:2: ( () ( (lv_simpleName_1_0= RULE_ID ) ) otherlv_2= '(' ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )? otherlv_6= ')' (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )? otherlv_9= ';' )
            {
            // InternalBasicIQL.g:1175:2: ( () ( (lv_simpleName_1_0= RULE_ID ) ) otherlv_2= '(' ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )? otherlv_6= ')' (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )? otherlv_9= ';' )
            // InternalBasicIQL.g:1176:3: () ( (lv_simpleName_1_0= RULE_ID ) ) otherlv_2= '(' ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )? otherlv_6= ')' (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )? otherlv_9= ';'
            {
            // InternalBasicIQL.g:1176:3: ()
            // InternalBasicIQL.g:1177:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLMethodDeclarationAccess().getIQLMethodDeclarationAction_0(),
              					current);
              			
            }

            }

            // InternalBasicIQL.g:1183:3: ( (lv_simpleName_1_0= RULE_ID ) )
            // InternalBasicIQL.g:1184:4: (lv_simpleName_1_0= RULE_ID )
            {
            // InternalBasicIQL.g:1184:4: (lv_simpleName_1_0= RULE_ID )
            // InternalBasicIQL.g:1185:5: lv_simpleName_1_0= RULE_ID
            {
            lv_simpleName_1_0=(Token)match(input,RULE_ID,FOLLOW_22); if (state.failed) return current;
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

            otherlv_2=(Token)match(input,27,FOLLOW_20); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLMethodDeclarationAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalBasicIQL.g:1205:3: ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==RULE_ID) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // InternalBasicIQL.g:1206:4: ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )*
                    {
                    // InternalBasicIQL.g:1206:4: ( (lv_parameters_3_0= ruleJvmFormalParameter ) )
                    // InternalBasicIQL.g:1207:5: (lv_parameters_3_0= ruleJvmFormalParameter )
                    {
                    // InternalBasicIQL.g:1207:5: (lv_parameters_3_0= ruleJvmFormalParameter )
                    // InternalBasicIQL.g:1208:6: lv_parameters_3_0= ruleJvmFormalParameter
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMethodDeclarationAccess().getParametersJvmFormalParameterParserRuleCall_3_0_0());
                      					
                    }
                    pushFollow(FOLLOW_21);
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

                    // InternalBasicIQL.g:1225:4: (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )*
                    loop24:
                    do {
                        int alt24=2;
                        int LA24_0 = input.LA(1);

                        if ( (LA24_0==20) ) {
                            alt24=1;
                        }


                        switch (alt24) {
                    	case 1 :
                    	    // InternalBasicIQL.g:1226:5: otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) )
                    	    {
                    	    otherlv_4=(Token)match(input,20,FOLLOW_3); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_4, grammarAccess.getIQLMethodDeclarationAccess().getCommaKeyword_3_1_0());
                    	      				
                    	    }
                    	    // InternalBasicIQL.g:1230:5: ( (lv_parameters_5_0= ruleJvmFormalParameter ) )
                    	    // InternalBasicIQL.g:1231:6: (lv_parameters_5_0= ruleJvmFormalParameter )
                    	    {
                    	    // InternalBasicIQL.g:1231:6: (lv_parameters_5_0= ruleJvmFormalParameter )
                    	    // InternalBasicIQL.g:1232:7: lv_parameters_5_0= ruleJvmFormalParameter
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLMethodDeclarationAccess().getParametersJvmFormalParameterParserRuleCall_3_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_21);
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
                    	    break loop24;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_6=(Token)match(input,28,FOLLOW_23); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_6, grammarAccess.getIQLMethodDeclarationAccess().getRightParenthesisKeyword_4());
              		
            }
            // InternalBasicIQL.g:1255:3: (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==29) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // InternalBasicIQL.g:1256:4: otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) )
                    {
                    otherlv_7=(Token)match(input,29,FOLLOW_3); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_7, grammarAccess.getIQLMethodDeclarationAccess().getColonKeyword_5_0());
                      			
                    }
                    // InternalBasicIQL.g:1260:4: ( (lv_returnType_8_0= ruleJvmTypeReference ) )
                    // InternalBasicIQL.g:1261:5: (lv_returnType_8_0= ruleJvmTypeReference )
                    {
                    // InternalBasicIQL.g:1261:5: (lv_returnType_8_0= ruleJvmTypeReference )
                    // InternalBasicIQL.g:1262:6: lv_returnType_8_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMethodDeclarationAccess().getReturnTypeJvmTypeReferenceParserRuleCall_5_1_0());
                      					
                    }
                    pushFollow(FOLLOW_4);
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

            otherlv_9=(Token)match(input,14,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:1288:1: entryRuleIQLJavaMember returns [EObject current=null] : iv_ruleIQLJavaMember= ruleIQLJavaMember EOF ;
    public final EObject entryRuleIQLJavaMember() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLJavaMember = null;


        try {
            // InternalBasicIQL.g:1288:54: (iv_ruleIQLJavaMember= ruleIQLJavaMember EOF )
            // InternalBasicIQL.g:1289:2: iv_ruleIQLJavaMember= ruleIQLJavaMember EOF
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
    // InternalBasicIQL.g:1295:1: ruleIQLJavaMember returns [EObject current=null] : ( () ( (lv_java_1_0= ruleIQLJava ) ) ) ;
    public final EObject ruleIQLJavaMember() throws RecognitionException {
        EObject current = null;

        EObject lv_java_1_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:1301:2: ( ( () ( (lv_java_1_0= ruleIQLJava ) ) ) )
            // InternalBasicIQL.g:1302:2: ( () ( (lv_java_1_0= ruleIQLJava ) ) )
            {
            // InternalBasicIQL.g:1302:2: ( () ( (lv_java_1_0= ruleIQLJava ) ) )
            // InternalBasicIQL.g:1303:3: () ( (lv_java_1_0= ruleIQLJava ) )
            {
            // InternalBasicIQL.g:1303:3: ()
            // InternalBasicIQL.g:1304:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLJavaMemberAccess().getIQLJavaMemberAction_0(),
              					current);
              			
            }

            }

            // InternalBasicIQL.g:1310:3: ( (lv_java_1_0= ruleIQLJava ) )
            // InternalBasicIQL.g:1311:4: (lv_java_1_0= ruleIQLJava )
            {
            // InternalBasicIQL.g:1311:4: (lv_java_1_0= ruleIQLJava )
            // InternalBasicIQL.g:1312:5: lv_java_1_0= ruleIQLJava
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


    // $ANTLR start "entryRuleIQLMetadata"
    // InternalBasicIQL.g:1333:1: entryRuleIQLMetadata returns [EObject current=null] : iv_ruleIQLMetadata= ruleIQLMetadata EOF ;
    public final EObject entryRuleIQLMetadata() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadata = null;


        try {
            // InternalBasicIQL.g:1333:52: (iv_ruleIQLMetadata= ruleIQLMetadata EOF )
            // InternalBasicIQL.g:1334:2: iv_ruleIQLMetadata= ruleIQLMetadata EOF
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
    // InternalBasicIQL.g:1340:1: ruleIQLMetadata returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )? ) ;
    public final EObject ruleIQLMetadata() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:1346:2: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )? ) )
            // InternalBasicIQL.g:1347:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )? )
            {
            // InternalBasicIQL.g:1347:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )? )
            // InternalBasicIQL.g:1348:3: ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )?
            {
            // InternalBasicIQL.g:1348:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalBasicIQL.g:1349:4: (lv_name_0_0= RULE_ID )
            {
            // InternalBasicIQL.g:1349:4: (lv_name_0_0= RULE_ID )
            // InternalBasicIQL.g:1350:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_24); if (state.failed) return current;
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

            // InternalBasicIQL.g:1366:3: (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )?
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==30) ) {
                alt27=1;
            }
            switch (alt27) {
                case 1 :
                    // InternalBasicIQL.g:1367:4: otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) )
                    {
                    otherlv_1=(Token)match(input,30,FOLLOW_25); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_1, grammarAccess.getIQLMetadataAccess().getEqualsSignKeyword_1_0());
                      			
                    }
                    // InternalBasicIQL.g:1371:4: ( (lv_value_2_0= ruleIQLMetadataValue ) )
                    // InternalBasicIQL.g:1372:5: (lv_value_2_0= ruleIQLMetadataValue )
                    {
                    // InternalBasicIQL.g:1372:5: (lv_value_2_0= ruleIQLMetadataValue )
                    // InternalBasicIQL.g:1373:6: lv_value_2_0= ruleIQLMetadataValue
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
    // InternalBasicIQL.g:1395:1: entryRuleIQLMetadataValue returns [EObject current=null] : iv_ruleIQLMetadataValue= ruleIQLMetadataValue EOF ;
    public final EObject entryRuleIQLMetadataValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataValue = null;


        try {
            // InternalBasicIQL.g:1395:57: (iv_ruleIQLMetadataValue= ruleIQLMetadataValue EOF )
            // InternalBasicIQL.g:1396:2: iv_ruleIQLMetadataValue= ruleIQLMetadataValue EOF
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
    // InternalBasicIQL.g:1402:1: ruleIQLMetadataValue returns [EObject current=null] : (this_IQLMetadataValueSingle_0= ruleIQLMetadataValueSingle | this_IQLMetadataValueList_1= ruleIQLMetadataValueList | this_IQLMetadataValueMap_2= ruleIQLMetadataValueMap ) ;
    public final EObject ruleIQLMetadataValue() throws RecognitionException {
        EObject current = null;

        EObject this_IQLMetadataValueSingle_0 = null;

        EObject this_IQLMetadataValueList_1 = null;

        EObject this_IQLMetadataValueMap_2 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:1408:2: ( (this_IQLMetadataValueSingle_0= ruleIQLMetadataValueSingle | this_IQLMetadataValueList_1= ruleIQLMetadataValueList | this_IQLMetadataValueMap_2= ruleIQLMetadataValueMap ) )
            // InternalBasicIQL.g:1409:2: (this_IQLMetadataValueSingle_0= ruleIQLMetadataValueSingle | this_IQLMetadataValueList_1= ruleIQLMetadataValueList | this_IQLMetadataValueMap_2= ruleIQLMetadataValueMap )
            {
            // InternalBasicIQL.g:1409:2: (this_IQLMetadataValueSingle_0= ruleIQLMetadataValueSingle | this_IQLMetadataValueList_1= ruleIQLMetadataValueList | this_IQLMetadataValueMap_2= ruleIQLMetadataValueMap )
            int alt28=3;
            switch ( input.LA(1) ) {
            case RULE_ID:
            case RULE_INT:
            case RULE_DOUBLE:
            case RULE_STRING:
            case 31:
            case 109:
            case 110:
                {
                alt28=1;
                }
                break;
            case 24:
                {
                alt28=2;
                }
                break;
            case 21:
                {
                alt28=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 28, 0, input);

                throw nvae;
            }

            switch (alt28) {
                case 1 :
                    // InternalBasicIQL.g:1410:3: this_IQLMetadataValueSingle_0= ruleIQLMetadataValueSingle
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
                    // InternalBasicIQL.g:1419:3: this_IQLMetadataValueList_1= ruleIQLMetadataValueList
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
                    // InternalBasicIQL.g:1428:3: this_IQLMetadataValueMap_2= ruleIQLMetadataValueMap
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


    // $ANTLR start "entryRuleIQLMetadataValueSingle"
    // InternalBasicIQL.g:1440:1: entryRuleIQLMetadataValueSingle returns [EObject current=null] : iv_ruleIQLMetadataValueSingle= ruleIQLMetadataValueSingle EOF ;
    public final EObject entryRuleIQLMetadataValueSingle() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataValueSingle = null;


        try {
            // InternalBasicIQL.g:1440:63: (iv_ruleIQLMetadataValueSingle= ruleIQLMetadataValueSingle EOF )
            // InternalBasicIQL.g:1441:2: iv_ruleIQLMetadataValueSingle= ruleIQLMetadataValueSingle EOF
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
    // InternalBasicIQL.g:1447:1: ruleIQLMetadataValueSingle returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleJvmTypeReference ) ) ) | ( () otherlv_11= 'null' ) ) ;
    public final EObject ruleIQLMetadataValueSingle() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        Token otherlv_11=null;
        AntlrDatatypeRuleToken lv_value_7_0 = null;

        EObject lv_value_9_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:1453:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleJvmTypeReference ) ) ) | ( () otherlv_11= 'null' ) ) )
            // InternalBasicIQL.g:1454:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleJvmTypeReference ) ) ) | ( () otherlv_11= 'null' ) )
            {
            // InternalBasicIQL.g:1454:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleJvmTypeReference ) ) ) | ( () otherlv_11= 'null' ) )
            int alt29=6;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt29=1;
                }
                break;
            case RULE_DOUBLE:
                {
                alt29=2;
                }
                break;
            case RULE_STRING:
                {
                alt29=3;
                }
                break;
            case 109:
            case 110:
                {
                alt29=4;
                }
                break;
            case RULE_ID:
                {
                alt29=5;
                }
                break;
            case 31:
                {
                alt29=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;
            }

            switch (alt29) {
                case 1 :
                    // InternalBasicIQL.g:1455:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalBasicIQL.g:1455:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalBasicIQL.g:1456:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalBasicIQL.g:1456:4: ()
                    // InternalBasicIQL.g:1457:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleIntAction_0_0(),
                      						current);
                      				
                    }

                    }

                    // InternalBasicIQL.g:1463:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalBasicIQL.g:1464:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalBasicIQL.g:1464:5: (lv_value_1_0= RULE_INT )
                    // InternalBasicIQL.g:1465:6: lv_value_1_0= RULE_INT
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
                    // InternalBasicIQL.g:1483:3: ( () ( (lv_value_3_0= RULE_DOUBLE ) ) )
                    {
                    // InternalBasicIQL.g:1483:3: ( () ( (lv_value_3_0= RULE_DOUBLE ) ) )
                    // InternalBasicIQL.g:1484:4: () ( (lv_value_3_0= RULE_DOUBLE ) )
                    {
                    // InternalBasicIQL.g:1484:4: ()
                    // InternalBasicIQL.g:1485:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleDoubleAction_1_0(),
                      						current);
                      				
                    }

                    }

                    // InternalBasicIQL.g:1491:4: ( (lv_value_3_0= RULE_DOUBLE ) )
                    // InternalBasicIQL.g:1492:5: (lv_value_3_0= RULE_DOUBLE )
                    {
                    // InternalBasicIQL.g:1492:5: (lv_value_3_0= RULE_DOUBLE )
                    // InternalBasicIQL.g:1493:6: lv_value_3_0= RULE_DOUBLE
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
                    // InternalBasicIQL.g:1511:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalBasicIQL.g:1511:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalBasicIQL.g:1512:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalBasicIQL.g:1512:4: ()
                    // InternalBasicIQL.g:1513:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleStringAction_2_0(),
                      						current);
                      				
                    }

                    }

                    // InternalBasicIQL.g:1519:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalBasicIQL.g:1520:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalBasicIQL.g:1520:5: (lv_value_5_0= RULE_STRING )
                    // InternalBasicIQL.g:1521:6: lv_value_5_0= RULE_STRING
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
                    // InternalBasicIQL.g:1539:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    {
                    // InternalBasicIQL.g:1539:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    // InternalBasicIQL.g:1540:4: () ( (lv_value_7_0= ruleBOOLEAN ) )
                    {
                    // InternalBasicIQL.g:1540:4: ()
                    // InternalBasicIQL.g:1541:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleBooleanAction_3_0(),
                      						current);
                      				
                    }

                    }

                    // InternalBasicIQL.g:1547:4: ( (lv_value_7_0= ruleBOOLEAN ) )
                    // InternalBasicIQL.g:1548:5: (lv_value_7_0= ruleBOOLEAN )
                    {
                    // InternalBasicIQL.g:1548:5: (lv_value_7_0= ruleBOOLEAN )
                    // InternalBasicIQL.g:1549:6: lv_value_7_0= ruleBOOLEAN
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
                    // InternalBasicIQL.g:1568:3: ( () ( (lv_value_9_0= ruleJvmTypeReference ) ) )
                    {
                    // InternalBasicIQL.g:1568:3: ( () ( (lv_value_9_0= ruleJvmTypeReference ) ) )
                    // InternalBasicIQL.g:1569:4: () ( (lv_value_9_0= ruleJvmTypeReference ) )
                    {
                    // InternalBasicIQL.g:1569:4: ()
                    // InternalBasicIQL.g:1570:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleTypeRefAction_4_0(),
                      						current);
                      				
                    }

                    }

                    // InternalBasicIQL.g:1576:4: ( (lv_value_9_0= ruleJvmTypeReference ) )
                    // InternalBasicIQL.g:1577:5: (lv_value_9_0= ruleJvmTypeReference )
                    {
                    // InternalBasicIQL.g:1577:5: (lv_value_9_0= ruleJvmTypeReference )
                    // InternalBasicIQL.g:1578:6: lv_value_9_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMetadataValueSingleAccess().getValueJvmTypeReferenceParserRuleCall_4_1_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_value_9_0=ruleJvmTypeReference();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getIQLMetadataValueSingleRule());
                      						}
                      						set(
                      							current,
                      							"value",
                      							lv_value_9_0,
                      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 6 :
                    // InternalBasicIQL.g:1597:3: ( () otherlv_11= 'null' )
                    {
                    // InternalBasicIQL.g:1597:3: ( () otherlv_11= 'null' )
                    // InternalBasicIQL.g:1598:4: () otherlv_11= 'null'
                    {
                    // InternalBasicIQL.g:1598:4: ()
                    // InternalBasicIQL.g:1599:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleNullAction_5_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_11=(Token)match(input,31,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_11, grammarAccess.getIQLMetadataValueSingleAccess().getNullKeyword_5_1());
                      			
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


    // $ANTLR start "entryRuleIQLMetadataValueList"
    // InternalBasicIQL.g:1614:1: entryRuleIQLMetadataValueList returns [EObject current=null] : iv_ruleIQLMetadataValueList= ruleIQLMetadataValueList EOF ;
    public final EObject entryRuleIQLMetadataValueList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataValueList = null;


        try {
            // InternalBasicIQL.g:1614:61: (iv_ruleIQLMetadataValueList= ruleIQLMetadataValueList EOF )
            // InternalBasicIQL.g:1615:2: iv_ruleIQLMetadataValueList= ruleIQLMetadataValueList EOF
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
    // InternalBasicIQL.g:1621:1: ruleIQLMetadataValueList returns [EObject current=null] : ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )? otherlv_5= ']' ) ;
    public final EObject ruleIQLMetadataValueList() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:1627:2: ( ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )? otherlv_5= ']' ) )
            // InternalBasicIQL.g:1628:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )? otherlv_5= ']' )
            {
            // InternalBasicIQL.g:1628:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )? otherlv_5= ']' )
            // InternalBasicIQL.g:1629:3: () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )? otherlv_5= ']'
            {
            // InternalBasicIQL.g:1629:3: ()
            // InternalBasicIQL.g:1630:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLMetadataValueListAccess().getIQLMetadataValueListAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,24,FOLLOW_26); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLMetadataValueListAccess().getLeftSquareBracketKeyword_1());
              		
            }
            // InternalBasicIQL.g:1640:3: ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( ((LA31_0>=RULE_ID && LA31_0<=RULE_STRING)||LA31_0==21||LA31_0==24||LA31_0==31||(LA31_0>=109 && LA31_0<=110)) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // InternalBasicIQL.g:1641:4: ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )*
                    {
                    // InternalBasicIQL.g:1641:4: ( (lv_elements_2_0= ruleIQLMetadataValue ) )
                    // InternalBasicIQL.g:1642:5: (lv_elements_2_0= ruleIQLMetadataValue )
                    {
                    // InternalBasicIQL.g:1642:5: (lv_elements_2_0= ruleIQLMetadataValue )
                    // InternalBasicIQL.g:1643:6: lv_elements_2_0= ruleIQLMetadataValue
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMetadataValueListAccess().getElementsIQLMetadataValueParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_27);
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

                    // InternalBasicIQL.g:1660:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )*
                    loop30:
                    do {
                        int alt30=2;
                        int LA30_0 = input.LA(1);

                        if ( (LA30_0==20) ) {
                            alt30=1;
                        }


                        switch (alt30) {
                    	case 1 :
                    	    // InternalBasicIQL.g:1661:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) )
                    	    {
                    	    otherlv_3=(Token)match(input,20,FOLLOW_25); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLMetadataValueListAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalBasicIQL.g:1665:5: ( (lv_elements_4_0= ruleIQLMetadataValue ) )
                    	    // InternalBasicIQL.g:1666:6: (lv_elements_4_0= ruleIQLMetadataValue )
                    	    {
                    	    // InternalBasicIQL.g:1666:6: (lv_elements_4_0= ruleIQLMetadataValue )
                    	    // InternalBasicIQL.g:1667:7: lv_elements_4_0= ruleIQLMetadataValue
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLMetadataValueListAccess().getElementsIQLMetadataValueParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_27);
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
                    	    break loop30;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,25,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:1694:1: entryRuleIQLMetadataValueMap returns [EObject current=null] : iv_ruleIQLMetadataValueMap= ruleIQLMetadataValueMap EOF ;
    public final EObject entryRuleIQLMetadataValueMap() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataValueMap = null;


        try {
            // InternalBasicIQL.g:1694:60: (iv_ruleIQLMetadataValueMap= ruleIQLMetadataValueMap EOF )
            // InternalBasicIQL.g:1695:2: iv_ruleIQLMetadataValueMap= ruleIQLMetadataValueMap EOF
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
    // InternalBasicIQL.g:1701:1: ruleIQLMetadataValueMap returns [EObject current=null] : ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )? otherlv_5= '}' ) ;
    public final EObject ruleIQLMetadataValueMap() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:1707:2: ( ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )? otherlv_5= '}' ) )
            // InternalBasicIQL.g:1708:2: ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )? otherlv_5= '}' )
            {
            // InternalBasicIQL.g:1708:2: ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )? otherlv_5= '}' )
            // InternalBasicIQL.g:1709:3: () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )? otherlv_5= '}'
            {
            // InternalBasicIQL.g:1709:3: ()
            // InternalBasicIQL.g:1710:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLMetadataValueMapAccess().getIQLMetadataValueMapAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,21,FOLLOW_28); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLMetadataValueMapAccess().getLeftCurlyBracketKeyword_1());
              		
            }
            // InternalBasicIQL.g:1720:3: ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( ((LA33_0>=RULE_ID && LA33_0<=RULE_STRING)||LA33_0==21||LA33_0==24||LA33_0==31||(LA33_0>=109 && LA33_0<=110)) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // InternalBasicIQL.g:1721:4: ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )*
                    {
                    // InternalBasicIQL.g:1721:4: ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) )
                    // InternalBasicIQL.g:1722:5: (lv_elements_2_0= ruleIQLMetadataValueMapElement )
                    {
                    // InternalBasicIQL.g:1722:5: (lv_elements_2_0= ruleIQLMetadataValueMapElement )
                    // InternalBasicIQL.g:1723:6: lv_elements_2_0= ruleIQLMetadataValueMapElement
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMetadataValueMapAccess().getElementsIQLMetadataValueMapElementParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_29);
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

                    // InternalBasicIQL.g:1740:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )*
                    loop32:
                    do {
                        int alt32=2;
                        int LA32_0 = input.LA(1);

                        if ( (LA32_0==20) ) {
                            alt32=1;
                        }


                        switch (alt32) {
                    	case 1 :
                    	    // InternalBasicIQL.g:1741:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) )
                    	    {
                    	    otherlv_3=(Token)match(input,20,FOLLOW_25); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLMetadataValueMapAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalBasicIQL.g:1745:5: ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) )
                    	    // InternalBasicIQL.g:1746:6: (lv_elements_4_0= ruleIQLMetadataValueMapElement )
                    	    {
                    	    // InternalBasicIQL.g:1746:6: (lv_elements_4_0= ruleIQLMetadataValueMapElement )
                    	    // InternalBasicIQL.g:1747:7: lv_elements_4_0= ruleIQLMetadataValueMapElement
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLMetadataValueMapAccess().getElementsIQLMetadataValueMapElementParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_29);
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
                    	    break loop32;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,22,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:1774:1: entryRuleIQLMetadataValueMapElement returns [EObject current=null] : iv_ruleIQLMetadataValueMapElement= ruleIQLMetadataValueMapElement EOF ;
    public final EObject entryRuleIQLMetadataValueMapElement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataValueMapElement = null;


        try {
            // InternalBasicIQL.g:1774:67: (iv_ruleIQLMetadataValueMapElement= ruleIQLMetadataValueMapElement EOF )
            // InternalBasicIQL.g:1775:2: iv_ruleIQLMetadataValueMapElement= ruleIQLMetadataValueMapElement EOF
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
    // InternalBasicIQL.g:1781:1: ruleIQLMetadataValueMapElement returns [EObject current=null] : ( ( (lv_key_0_0= ruleIQLMetadataValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) ) ;
    public final EObject ruleIQLMetadataValueMapElement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_key_0_0 = null;

        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:1787:2: ( ( ( (lv_key_0_0= ruleIQLMetadataValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) ) )
            // InternalBasicIQL.g:1788:2: ( ( (lv_key_0_0= ruleIQLMetadataValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )
            {
            // InternalBasicIQL.g:1788:2: ( ( (lv_key_0_0= ruleIQLMetadataValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )
            // InternalBasicIQL.g:1789:3: ( (lv_key_0_0= ruleIQLMetadataValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) )
            {
            // InternalBasicIQL.g:1789:3: ( (lv_key_0_0= ruleIQLMetadataValue ) )
            // InternalBasicIQL.g:1790:4: (lv_key_0_0= ruleIQLMetadataValue )
            {
            // InternalBasicIQL.g:1790:4: (lv_key_0_0= ruleIQLMetadataValue )
            // InternalBasicIQL.g:1791:5: lv_key_0_0= ruleIQLMetadataValue
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLMetadataValueMapElementAccess().getKeyIQLMetadataValueParserRuleCall_0_0());
              				
            }
            pushFollow(FOLLOW_30);
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

            otherlv_1=(Token)match(input,30,FOLLOW_25); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLMetadataValueMapElementAccess().getEqualsSignKeyword_1());
              		
            }
            // InternalBasicIQL.g:1812:3: ( (lv_value_2_0= ruleIQLMetadataValue ) )
            // InternalBasicIQL.g:1813:4: (lv_value_2_0= ruleIQLMetadataValue )
            {
            // InternalBasicIQL.g:1813:4: (lv_value_2_0= ruleIQLMetadataValue )
            // InternalBasicIQL.g:1814:5: lv_value_2_0= ruleIQLMetadataValue
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
    // InternalBasicIQL.g:1835:1: entryRuleIQLVariableDeclaration returns [EObject current=null] : iv_ruleIQLVariableDeclaration= ruleIQLVariableDeclaration EOF ;
    public final EObject entryRuleIQLVariableDeclaration() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLVariableDeclaration = null;


        try {
            // InternalBasicIQL.g:1835:63: (iv_ruleIQLVariableDeclaration= ruleIQLVariableDeclaration EOF )
            // InternalBasicIQL.g:1836:2: iv_ruleIQLVariableDeclaration= ruleIQLVariableDeclaration EOF
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
    // InternalBasicIQL.g:1842:1: ruleIQLVariableDeclaration returns [EObject current=null] : ( () ( (lv_ref_1_0= ruleJvmTypeReference ) ) ( (lv_name_2_0= RULE_ID ) ) ) ;
    public final EObject ruleIQLVariableDeclaration() throws RecognitionException {
        EObject current = null;

        Token lv_name_2_0=null;
        EObject lv_ref_1_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:1848:2: ( ( () ( (lv_ref_1_0= ruleJvmTypeReference ) ) ( (lv_name_2_0= RULE_ID ) ) ) )
            // InternalBasicIQL.g:1849:2: ( () ( (lv_ref_1_0= ruleJvmTypeReference ) ) ( (lv_name_2_0= RULE_ID ) ) )
            {
            // InternalBasicIQL.g:1849:2: ( () ( (lv_ref_1_0= ruleJvmTypeReference ) ) ( (lv_name_2_0= RULE_ID ) ) )
            // InternalBasicIQL.g:1850:3: () ( (lv_ref_1_0= ruleJvmTypeReference ) ) ( (lv_name_2_0= RULE_ID ) )
            {
            // InternalBasicIQL.g:1850:3: ()
            // InternalBasicIQL.g:1851:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLVariableDeclarationAccess().getIQLVariableDeclarationAction_0(),
              					current);
              			
            }

            }

            // InternalBasicIQL.g:1857:3: ( (lv_ref_1_0= ruleJvmTypeReference ) )
            // InternalBasicIQL.g:1858:4: (lv_ref_1_0= ruleJvmTypeReference )
            {
            // InternalBasicIQL.g:1858:4: (lv_ref_1_0= ruleJvmTypeReference )
            // InternalBasicIQL.g:1859:5: lv_ref_1_0= ruleJvmTypeReference
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLVariableDeclarationAccess().getRefJvmTypeReferenceParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_3);
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

            // InternalBasicIQL.g:1876:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalBasicIQL.g:1877:4: (lv_name_2_0= RULE_ID )
            {
            // InternalBasicIQL.g:1877:4: (lv_name_2_0= RULE_ID )
            // InternalBasicIQL.g:1878:5: lv_name_2_0= RULE_ID
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
    // InternalBasicIQL.g:1898:1: entryRuleIQLVariableInitialization returns [EObject current=null] : iv_ruleIQLVariableInitialization= ruleIQLVariableInitialization EOF ;
    public final EObject entryRuleIQLVariableInitialization() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLVariableInitialization = null;


        try {
            // InternalBasicIQL.g:1898:66: (iv_ruleIQLVariableInitialization= ruleIQLVariableInitialization EOF )
            // InternalBasicIQL.g:1899:2: iv_ruleIQLVariableInitialization= ruleIQLVariableInitialization EOF
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
    // InternalBasicIQL.g:1905:1: ruleIQLVariableInitialization returns [EObject current=null] : ( ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) ) | ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) ) | (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) ) ) ;
    public final EObject ruleIQLVariableInitialization() throws RecognitionException {
        EObject current = null;

        Token otherlv_4=null;
        EObject lv_argsList_1_0 = null;

        EObject lv_argsMap_2_0 = null;

        EObject lv_argsMap_3_0 = null;

        EObject lv_value_5_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:1911:2: ( ( ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) ) | ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) ) | (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) ) ) )
            // InternalBasicIQL.g:1912:2: ( ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) ) | ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) ) | (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) ) )
            {
            // InternalBasicIQL.g:1912:2: ( ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) ) | ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) ) | (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) ) )
            int alt35=3;
            switch ( input.LA(1) ) {
            case 27:
                {
                alt35=1;
                }
                break;
            case 21:
                {
                alt35=2;
                }
                break;
            case 30:
                {
                alt35=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 35, 0, input);

                throw nvae;
            }

            switch (alt35) {
                case 1 :
                    // InternalBasicIQL.g:1913:3: ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) )
                    {
                    // InternalBasicIQL.g:1913:3: ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) )
                    // InternalBasicIQL.g:1914:4: () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? )
                    {
                    // InternalBasicIQL.g:1914:4: ()
                    // InternalBasicIQL.g:1915:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLVariableInitializationAccess().getIQLVariableInitializationAction_0_0(),
                      						current);
                      				
                    }

                    }

                    // InternalBasicIQL.g:1921:4: ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? )
                    // InternalBasicIQL.g:1922:5: ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )?
                    {
                    // InternalBasicIQL.g:1922:5: ( (lv_argsList_1_0= ruleIQLArgumentsList ) )
                    // InternalBasicIQL.g:1923:6: (lv_argsList_1_0= ruleIQLArgumentsList )
                    {
                    // InternalBasicIQL.g:1923:6: (lv_argsList_1_0= ruleIQLArgumentsList )
                    // InternalBasicIQL.g:1924:7: lv_argsList_1_0= ruleIQLArgumentsList
                    {
                    if ( state.backtracking==0 ) {

                      							newCompositeNode(grammarAccess.getIQLVariableInitializationAccess().getArgsListIQLArgumentsListParserRuleCall_0_1_0_0());
                      						
                    }
                    pushFollow(FOLLOW_31);
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

                    // InternalBasicIQL.g:1941:5: ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )?
                    int alt34=2;
                    int LA34_0 = input.LA(1);

                    if ( (LA34_0==21) ) {
                        alt34=1;
                    }
                    switch (alt34) {
                        case 1 :
                            // InternalBasicIQL.g:1942:6: (lv_argsMap_2_0= ruleIQLArgumentsMap )
                            {
                            // InternalBasicIQL.g:1942:6: (lv_argsMap_2_0= ruleIQLArgumentsMap )
                            // InternalBasicIQL.g:1943:7: lv_argsMap_2_0= ruleIQLArgumentsMap
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
                    // InternalBasicIQL.g:1963:3: ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) )
                    {
                    // InternalBasicIQL.g:1963:3: ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) )
                    // InternalBasicIQL.g:1964:4: (lv_argsMap_3_0= ruleIQLArgumentsMap )
                    {
                    // InternalBasicIQL.g:1964:4: (lv_argsMap_3_0= ruleIQLArgumentsMap )
                    // InternalBasicIQL.g:1965:5: lv_argsMap_3_0= ruleIQLArgumentsMap
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
                    // InternalBasicIQL.g:1983:3: (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) )
                    {
                    // InternalBasicIQL.g:1983:3: (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) )
                    // InternalBasicIQL.g:1984:4: otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) )
                    {
                    otherlv_4=(Token)match(input,30,FOLLOW_32); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_4, grammarAccess.getIQLVariableInitializationAccess().getEqualsSignKeyword_2_0());
                      			
                    }
                    // InternalBasicIQL.g:1988:4: ( (lv_value_5_0= ruleIQLExpression ) )
                    // InternalBasicIQL.g:1989:5: (lv_value_5_0= ruleIQLExpression )
                    {
                    // InternalBasicIQL.g:1989:5: (lv_value_5_0= ruleIQLExpression )
                    // InternalBasicIQL.g:1990:6: lv_value_5_0= ruleIQLExpression
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
    // InternalBasicIQL.g:2012:1: entryRuleIQLArgumentsList returns [EObject current=null] : iv_ruleIQLArgumentsList= ruleIQLArgumentsList EOF ;
    public final EObject entryRuleIQLArgumentsList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLArgumentsList = null;


        try {
            // InternalBasicIQL.g:2012:57: (iv_ruleIQLArgumentsList= ruleIQLArgumentsList EOF )
            // InternalBasicIQL.g:2013:2: iv_ruleIQLArgumentsList= ruleIQLArgumentsList EOF
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
    // InternalBasicIQL.g:2019:1: ruleIQLArgumentsList returns [EObject current=null] : ( () otherlv_1= '(' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ')' ) ;
    public final EObject ruleIQLArgumentsList() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:2025:2: ( ( () otherlv_1= '(' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ')' ) )
            // InternalBasicIQL.g:2026:2: ( () otherlv_1= '(' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ')' )
            {
            // InternalBasicIQL.g:2026:2: ( () otherlv_1= '(' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ')' )
            // InternalBasicIQL.g:2027:3: () otherlv_1= '(' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ')'
            {
            // InternalBasicIQL.g:2027:3: ()
            // InternalBasicIQL.g:2028:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLArgumentsListAccess().getIQLArgumentsListAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,27,FOLLOW_33); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLArgumentsListAccess().getLeftParenthesisKeyword_1());
              		
            }
            // InternalBasicIQL.g:2038:3: ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( ((LA37_0>=RULE_ID && LA37_0<=RULE_RANGE)||LA37_0==24||LA37_0==27||LA37_0==31||(LA37_0>=40 && LA37_0<=41)||(LA37_0>=59 && LA37_0<=60)||(LA37_0>=64 && LA37_0<=66)||(LA37_0>=68 && LA37_0<=69)||(LA37_0>=109 && LA37_0<=110)) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // InternalBasicIQL.g:2039:4: ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )*
                    {
                    // InternalBasicIQL.g:2039:4: ( (lv_elements_2_0= ruleIQLExpression ) )
                    // InternalBasicIQL.g:2040:5: (lv_elements_2_0= ruleIQLExpression )
                    {
                    // InternalBasicIQL.g:2040:5: (lv_elements_2_0= ruleIQLExpression )
                    // InternalBasicIQL.g:2041:6: lv_elements_2_0= ruleIQLExpression
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLArgumentsListAccess().getElementsIQLExpressionParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_21);
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

                    // InternalBasicIQL.g:2058:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )*
                    loop36:
                    do {
                        int alt36=2;
                        int LA36_0 = input.LA(1);

                        if ( (LA36_0==20) ) {
                            alt36=1;
                        }


                        switch (alt36) {
                    	case 1 :
                    	    // InternalBasicIQL.g:2059:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) )
                    	    {
                    	    otherlv_3=(Token)match(input,20,FOLLOW_32); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLArgumentsListAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalBasicIQL.g:2063:5: ( (lv_elements_4_0= ruleIQLExpression ) )
                    	    // InternalBasicIQL.g:2064:6: (lv_elements_4_0= ruleIQLExpression )
                    	    {
                    	    // InternalBasicIQL.g:2064:6: (lv_elements_4_0= ruleIQLExpression )
                    	    // InternalBasicIQL.g:2065:7: lv_elements_4_0= ruleIQLExpression
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLArgumentsListAccess().getElementsIQLExpressionParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_21);
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
                    	    break loop36;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,28,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:2092:1: entryRuleIQLArgumentsMap returns [EObject current=null] : iv_ruleIQLArgumentsMap= ruleIQLArgumentsMap EOF ;
    public final EObject entryRuleIQLArgumentsMap() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLArgumentsMap = null;


        try {
            // InternalBasicIQL.g:2092:56: (iv_ruleIQLArgumentsMap= ruleIQLArgumentsMap EOF )
            // InternalBasicIQL.g:2093:2: iv_ruleIQLArgumentsMap= ruleIQLArgumentsMap EOF
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
    // InternalBasicIQL.g:2099:1: ruleIQLArgumentsMap returns [EObject current=null] : ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )? otherlv_5= '}' ) ;
    public final EObject ruleIQLArgumentsMap() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:2105:2: ( ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )? otherlv_5= '}' ) )
            // InternalBasicIQL.g:2106:2: ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )? otherlv_5= '}' )
            {
            // InternalBasicIQL.g:2106:2: ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )? otherlv_5= '}' )
            // InternalBasicIQL.g:2107:3: () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )? otherlv_5= '}'
            {
            // InternalBasicIQL.g:2107:3: ()
            // InternalBasicIQL.g:2108:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLArgumentsMapAccess().getIQLArgumentsMapAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,21,FOLLOW_34); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLArgumentsMapAccess().getLeftCurlyBracketKeyword_1());
              		
            }
            // InternalBasicIQL.g:2118:3: ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==RULE_ID) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // InternalBasicIQL.g:2119:4: ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )*
                    {
                    // InternalBasicIQL.g:2119:4: ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) )
                    // InternalBasicIQL.g:2120:5: (lv_elements_2_0= ruleIQLArgumentsMapKeyValue )
                    {
                    // InternalBasicIQL.g:2120:5: (lv_elements_2_0= ruleIQLArgumentsMapKeyValue )
                    // InternalBasicIQL.g:2121:6: lv_elements_2_0= ruleIQLArgumentsMapKeyValue
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLArgumentsMapAccess().getElementsIQLArgumentsMapKeyValueParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_29);
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

                    // InternalBasicIQL.g:2138:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )*
                    loop38:
                    do {
                        int alt38=2;
                        int LA38_0 = input.LA(1);

                        if ( (LA38_0==20) ) {
                            alt38=1;
                        }


                        switch (alt38) {
                    	case 1 :
                    	    // InternalBasicIQL.g:2139:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) )
                    	    {
                    	    otherlv_3=(Token)match(input,20,FOLLOW_3); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLArgumentsMapAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalBasicIQL.g:2143:5: ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) )
                    	    // InternalBasicIQL.g:2144:6: (lv_elements_4_0= ruleIQLArgumentsMapKeyValue )
                    	    {
                    	    // InternalBasicIQL.g:2144:6: (lv_elements_4_0= ruleIQLArgumentsMapKeyValue )
                    	    // InternalBasicIQL.g:2145:7: lv_elements_4_0= ruleIQLArgumentsMapKeyValue
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLArgumentsMapAccess().getElementsIQLArgumentsMapKeyValueParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_29);
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
                    	    break loop38;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,22,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:2172:1: entryRuleIQLArgumentsMapKeyValue returns [EObject current=null] : iv_ruleIQLArgumentsMapKeyValue= ruleIQLArgumentsMapKeyValue EOF ;
    public final EObject entryRuleIQLArgumentsMapKeyValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLArgumentsMapKeyValue = null;


        try {
            // InternalBasicIQL.g:2172:64: (iv_ruleIQLArgumentsMapKeyValue= ruleIQLArgumentsMapKeyValue EOF )
            // InternalBasicIQL.g:2173:2: iv_ruleIQLArgumentsMapKeyValue= ruleIQLArgumentsMapKeyValue EOF
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
    // InternalBasicIQL.g:2179:1: ruleIQLArgumentsMapKeyValue returns [EObject current=null] : ( ( ( ruleQualifiedName ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLExpression ) ) ) ;
    public final EObject ruleIQLArgumentsMapKeyValue() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:2185:2: ( ( ( ( ruleQualifiedName ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLExpression ) ) ) )
            // InternalBasicIQL.g:2186:2: ( ( ( ruleQualifiedName ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLExpression ) ) )
            {
            // InternalBasicIQL.g:2186:2: ( ( ( ruleQualifiedName ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLExpression ) ) )
            // InternalBasicIQL.g:2187:3: ( ( ruleQualifiedName ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLExpression ) )
            {
            // InternalBasicIQL.g:2187:3: ( ( ruleQualifiedName ) )
            // InternalBasicIQL.g:2188:4: ( ruleQualifiedName )
            {
            // InternalBasicIQL.g:2188:4: ( ruleQualifiedName )
            // InternalBasicIQL.g:2189:5: ruleQualifiedName
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLArgumentsMapKeyValueRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLArgumentsMapKeyValueAccess().getKeyJvmIdentifiableElementCrossReference_0_0());
              				
            }
            pushFollow(FOLLOW_30);
            ruleQualifiedName();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_1=(Token)match(input,30,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLArgumentsMapKeyValueAccess().getEqualsSignKeyword_1());
              		
            }
            // InternalBasicIQL.g:2207:3: ( (lv_value_2_0= ruleIQLExpression ) )
            // InternalBasicIQL.g:2208:4: (lv_value_2_0= ruleIQLExpression )
            {
            // InternalBasicIQL.g:2208:4: (lv_value_2_0= ruleIQLExpression )
            // InternalBasicIQL.g:2209:5: lv_value_2_0= ruleIQLExpression
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
    // InternalBasicIQL.g:2230:1: entryRuleIQLStatement returns [EObject current=null] : iv_ruleIQLStatement= ruleIQLStatement EOF ;
    public final EObject entryRuleIQLStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLStatement = null;


        try {
            // InternalBasicIQL.g:2230:53: (iv_ruleIQLStatement= ruleIQLStatement EOF )
            // InternalBasicIQL.g:2231:2: iv_ruleIQLStatement= ruleIQLStatement EOF
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
    // InternalBasicIQL.g:2237:1: ruleIQLStatement returns [EObject current=null] : (this_IQLStatementBlock_0= ruleIQLStatementBlock | this_IQLExpressionStatement_1= ruleIQLExpressionStatement | this_IQLIfStatement_2= ruleIQLIfStatement | this_IQLWhileStatement_3= ruleIQLWhileStatement | this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement | this_IQLForStatement_5= ruleIQLForStatement | this_IQLForEachStatement_6= ruleIQLForEachStatement | this_IQLSwitchStatement_7= ruleIQLSwitchStatement | this_IQLVariableStatement_8= ruleIQLVariableStatement | this_IQLBreakStatement_9= ruleIQLBreakStatement | this_IQLContinueStatement_10= ruleIQLContinueStatement | this_IQLReturnStatement_11= ruleIQLReturnStatement | this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement | this_IQLJavaStatement_13= ruleIQLJavaStatement ) ;
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
            // InternalBasicIQL.g:2243:2: ( (this_IQLStatementBlock_0= ruleIQLStatementBlock | this_IQLExpressionStatement_1= ruleIQLExpressionStatement | this_IQLIfStatement_2= ruleIQLIfStatement | this_IQLWhileStatement_3= ruleIQLWhileStatement | this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement | this_IQLForStatement_5= ruleIQLForStatement | this_IQLForEachStatement_6= ruleIQLForEachStatement | this_IQLSwitchStatement_7= ruleIQLSwitchStatement | this_IQLVariableStatement_8= ruleIQLVariableStatement | this_IQLBreakStatement_9= ruleIQLBreakStatement | this_IQLContinueStatement_10= ruleIQLContinueStatement | this_IQLReturnStatement_11= ruleIQLReturnStatement | this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement | this_IQLJavaStatement_13= ruleIQLJavaStatement ) )
            // InternalBasicIQL.g:2244:2: (this_IQLStatementBlock_0= ruleIQLStatementBlock | this_IQLExpressionStatement_1= ruleIQLExpressionStatement | this_IQLIfStatement_2= ruleIQLIfStatement | this_IQLWhileStatement_3= ruleIQLWhileStatement | this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement | this_IQLForStatement_5= ruleIQLForStatement | this_IQLForEachStatement_6= ruleIQLForEachStatement | this_IQLSwitchStatement_7= ruleIQLSwitchStatement | this_IQLVariableStatement_8= ruleIQLVariableStatement | this_IQLBreakStatement_9= ruleIQLBreakStatement | this_IQLContinueStatement_10= ruleIQLContinueStatement | this_IQLReturnStatement_11= ruleIQLReturnStatement | this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement | this_IQLJavaStatement_13= ruleIQLJavaStatement )
            {
            // InternalBasicIQL.g:2244:2: (this_IQLStatementBlock_0= ruleIQLStatementBlock | this_IQLExpressionStatement_1= ruleIQLExpressionStatement | this_IQLIfStatement_2= ruleIQLIfStatement | this_IQLWhileStatement_3= ruleIQLWhileStatement | this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement | this_IQLForStatement_5= ruleIQLForStatement | this_IQLForEachStatement_6= ruleIQLForEachStatement | this_IQLSwitchStatement_7= ruleIQLSwitchStatement | this_IQLVariableStatement_8= ruleIQLVariableStatement | this_IQLBreakStatement_9= ruleIQLBreakStatement | this_IQLContinueStatement_10= ruleIQLContinueStatement | this_IQLReturnStatement_11= ruleIQLReturnStatement | this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement | this_IQLJavaStatement_13= ruleIQLJavaStatement )
            int alt40=14;
            alt40 = dfa40.predict(input);
            switch (alt40) {
                case 1 :
                    // InternalBasicIQL.g:2245:3: this_IQLStatementBlock_0= ruleIQLStatementBlock
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
                    // InternalBasicIQL.g:2254:3: this_IQLExpressionStatement_1= ruleIQLExpressionStatement
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
                    // InternalBasicIQL.g:2263:3: this_IQLIfStatement_2= ruleIQLIfStatement
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
                    // InternalBasicIQL.g:2272:3: this_IQLWhileStatement_3= ruleIQLWhileStatement
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
                    // InternalBasicIQL.g:2281:3: this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement
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
                    // InternalBasicIQL.g:2290:3: this_IQLForStatement_5= ruleIQLForStatement
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
                    // InternalBasicIQL.g:2299:3: this_IQLForEachStatement_6= ruleIQLForEachStatement
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
                    // InternalBasicIQL.g:2308:3: this_IQLSwitchStatement_7= ruleIQLSwitchStatement
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
                    // InternalBasicIQL.g:2317:3: this_IQLVariableStatement_8= ruleIQLVariableStatement
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
                    // InternalBasicIQL.g:2326:3: this_IQLBreakStatement_9= ruleIQLBreakStatement
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
                    // InternalBasicIQL.g:2335:3: this_IQLContinueStatement_10= ruleIQLContinueStatement
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
                    // InternalBasicIQL.g:2344:3: this_IQLReturnStatement_11= ruleIQLReturnStatement
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
                    // InternalBasicIQL.g:2353:3: this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement
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
                    // InternalBasicIQL.g:2362:3: this_IQLJavaStatement_13= ruleIQLJavaStatement
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
    // InternalBasicIQL.g:2374:1: entryRuleIQLStatementBlock returns [EObject current=null] : iv_ruleIQLStatementBlock= ruleIQLStatementBlock EOF ;
    public final EObject entryRuleIQLStatementBlock() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLStatementBlock = null;


        try {
            // InternalBasicIQL.g:2374:58: (iv_ruleIQLStatementBlock= ruleIQLStatementBlock EOF )
            // InternalBasicIQL.g:2375:2: iv_ruleIQLStatementBlock= ruleIQLStatementBlock EOF
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
    // InternalBasicIQL.g:2381:1: ruleIQLStatementBlock returns [EObject current=null] : ( () otherlv_1= '{' ( (lv_statements_2_0= ruleIQLStatement ) )* otherlv_3= '}' ) ;
    public final EObject ruleIQLStatementBlock() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject lv_statements_2_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:2387:2: ( ( () otherlv_1= '{' ( (lv_statements_2_0= ruleIQLStatement ) )* otherlv_3= '}' ) )
            // InternalBasicIQL.g:2388:2: ( () otherlv_1= '{' ( (lv_statements_2_0= ruleIQLStatement ) )* otherlv_3= '}' )
            {
            // InternalBasicIQL.g:2388:2: ( () otherlv_1= '{' ( (lv_statements_2_0= ruleIQLStatement ) )* otherlv_3= '}' )
            // InternalBasicIQL.g:2389:3: () otherlv_1= '{' ( (lv_statements_2_0= ruleIQLStatement ) )* otherlv_3= '}'
            {
            // InternalBasicIQL.g:2389:3: ()
            // InternalBasicIQL.g:2390:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLStatementBlockAccess().getIQLStatementBlockAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,21,FOLLOW_35); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLStatementBlockAccess().getLeftCurlyBracketKeyword_1());
              		
            }
            // InternalBasicIQL.g:2400:3: ( (lv_statements_2_0= ruleIQLStatement ) )*
            loop41:
            do {
                int alt41=2;
                int LA41_0 = input.LA(1);

                if ( ((LA41_0>=RULE_ID && LA41_0<=RULE_RANGE)||LA41_0==21||LA41_0==24||LA41_0==27||(LA41_0>=31 && LA41_0<=32)||(LA41_0>=34 && LA41_0<=37)||(LA41_0>=40 && LA41_0<=44)||(LA41_0>=59 && LA41_0<=60)||(LA41_0>=64 && LA41_0<=66)||(LA41_0>=68 && LA41_0<=69)||LA41_0==72||(LA41_0>=109 && LA41_0<=110)) ) {
                    alt41=1;
                }


                switch (alt41) {
            	case 1 :
            	    // InternalBasicIQL.g:2401:4: (lv_statements_2_0= ruleIQLStatement )
            	    {
            	    // InternalBasicIQL.g:2401:4: (lv_statements_2_0= ruleIQLStatement )
            	    // InternalBasicIQL.g:2402:5: lv_statements_2_0= ruleIQLStatement
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLStatementBlockAccess().getStatementsIQLStatementParserRuleCall_2_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_35);
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
            	    break loop41;
                }
            } while (true);

            otherlv_3=(Token)match(input,22,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:2427:1: entryRuleIQLJavaStatement returns [EObject current=null] : iv_ruleIQLJavaStatement= ruleIQLJavaStatement EOF ;
    public final EObject entryRuleIQLJavaStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLJavaStatement = null;


        try {
            // InternalBasicIQL.g:2427:57: (iv_ruleIQLJavaStatement= ruleIQLJavaStatement EOF )
            // InternalBasicIQL.g:2428:2: iv_ruleIQLJavaStatement= ruleIQLJavaStatement EOF
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
    // InternalBasicIQL.g:2434:1: ruleIQLJavaStatement returns [EObject current=null] : ( () ( (lv_java_1_0= ruleIQLJava ) ) ) ;
    public final EObject ruleIQLJavaStatement() throws RecognitionException {
        EObject current = null;

        EObject lv_java_1_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:2440:2: ( ( () ( (lv_java_1_0= ruleIQLJava ) ) ) )
            // InternalBasicIQL.g:2441:2: ( () ( (lv_java_1_0= ruleIQLJava ) ) )
            {
            // InternalBasicIQL.g:2441:2: ( () ( (lv_java_1_0= ruleIQLJava ) ) )
            // InternalBasicIQL.g:2442:3: () ( (lv_java_1_0= ruleIQLJava ) )
            {
            // InternalBasicIQL.g:2442:3: ()
            // InternalBasicIQL.g:2443:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLJavaStatementAccess().getIQLJavaStatementAction_0(),
              					current);
              			
            }

            }

            // InternalBasicIQL.g:2449:3: ( (lv_java_1_0= ruleIQLJava ) )
            // InternalBasicIQL.g:2450:4: (lv_java_1_0= ruleIQLJava )
            {
            // InternalBasicIQL.g:2450:4: (lv_java_1_0= ruleIQLJava )
            // InternalBasicIQL.g:2451:5: lv_java_1_0= ruleIQLJava
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
    // InternalBasicIQL.g:2472:1: entryRuleIQLIfStatement returns [EObject current=null] : iv_ruleIQLIfStatement= ruleIQLIfStatement EOF ;
    public final EObject entryRuleIQLIfStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLIfStatement = null;


        try {
            // InternalBasicIQL.g:2472:55: (iv_ruleIQLIfStatement= ruleIQLIfStatement EOF )
            // InternalBasicIQL.g:2473:2: iv_ruleIQLIfStatement= ruleIQLIfStatement EOF
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
    // InternalBasicIQL.g:2479:1: ruleIQLIfStatement returns [EObject current=null] : ( () otherlv_1= 'if' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_thenBody_5_0= ruleIQLStatement ) ) ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )? ) ;
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
            // InternalBasicIQL.g:2485:2: ( ( () otherlv_1= 'if' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_thenBody_5_0= ruleIQLStatement ) ) ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )? ) )
            // InternalBasicIQL.g:2486:2: ( () otherlv_1= 'if' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_thenBody_5_0= ruleIQLStatement ) ) ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )? )
            {
            // InternalBasicIQL.g:2486:2: ( () otherlv_1= 'if' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_thenBody_5_0= ruleIQLStatement ) ) ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )? )
            // InternalBasicIQL.g:2487:3: () otherlv_1= 'if' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_thenBody_5_0= ruleIQLStatement ) ) ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )?
            {
            // InternalBasicIQL.g:2487:3: ()
            // InternalBasicIQL.g:2488:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLIfStatementAccess().getIQLIfStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,32,FOLLOW_22); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLIfStatementAccess().getIfKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,27,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLIfStatementAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalBasicIQL.g:2502:3: ( (lv_predicate_3_0= ruleIQLExpression ) )
            // InternalBasicIQL.g:2503:4: (lv_predicate_3_0= ruleIQLExpression )
            {
            // InternalBasicIQL.g:2503:4: (lv_predicate_3_0= ruleIQLExpression )
            // InternalBasicIQL.g:2504:5: lv_predicate_3_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLIfStatementAccess().getPredicateIQLExpressionParserRuleCall_3_0());
              				
            }
            pushFollow(FOLLOW_36);
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

            otherlv_4=(Token)match(input,28,FOLLOW_37); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLIfStatementAccess().getRightParenthesisKeyword_4());
              		
            }
            // InternalBasicIQL.g:2525:3: ( (lv_thenBody_5_0= ruleIQLStatement ) )
            // InternalBasicIQL.g:2526:4: (lv_thenBody_5_0= ruleIQLStatement )
            {
            // InternalBasicIQL.g:2526:4: (lv_thenBody_5_0= ruleIQLStatement )
            // InternalBasicIQL.g:2527:5: lv_thenBody_5_0= ruleIQLStatement
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLIfStatementAccess().getThenBodyIQLStatementParserRuleCall_5_0());
              				
            }
            pushFollow(FOLLOW_38);
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

            // InternalBasicIQL.g:2544:3: ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==33) ) {
                int LA42_1 = input.LA(2);

                if ( (synpred1_InternalBasicIQL()) ) {
                    alt42=1;
                }
            }
            switch (alt42) {
                case 1 :
                    // InternalBasicIQL.g:2545:4: ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) )
                    {
                    // InternalBasicIQL.g:2545:4: ( ( 'else' )=>otherlv_6= 'else' )
                    // InternalBasicIQL.g:2546:5: ( 'else' )=>otherlv_6= 'else'
                    {
                    otherlv_6=(Token)match(input,33,FOLLOW_37); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(otherlv_6, grammarAccess.getIQLIfStatementAccess().getElseKeyword_6_0());
                      				
                    }

                    }

                    // InternalBasicIQL.g:2552:4: ( (lv_elseBody_7_0= ruleIQLStatement ) )
                    // InternalBasicIQL.g:2553:5: (lv_elseBody_7_0= ruleIQLStatement )
                    {
                    // InternalBasicIQL.g:2553:5: (lv_elseBody_7_0= ruleIQLStatement )
                    // InternalBasicIQL.g:2554:6: lv_elseBody_7_0= ruleIQLStatement
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
    // InternalBasicIQL.g:2576:1: entryRuleIQLWhileStatement returns [EObject current=null] : iv_ruleIQLWhileStatement= ruleIQLWhileStatement EOF ;
    public final EObject entryRuleIQLWhileStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLWhileStatement = null;


        try {
            // InternalBasicIQL.g:2576:58: (iv_ruleIQLWhileStatement= ruleIQLWhileStatement EOF )
            // InternalBasicIQL.g:2577:2: iv_ruleIQLWhileStatement= ruleIQLWhileStatement EOF
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
    // InternalBasicIQL.g:2583:1: ruleIQLWhileStatement returns [EObject current=null] : ( () otherlv_1= 'while' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_body_5_0= ruleIQLStatement ) ) ) ;
    public final EObject ruleIQLWhileStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_predicate_3_0 = null;

        EObject lv_body_5_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:2589:2: ( ( () otherlv_1= 'while' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_body_5_0= ruleIQLStatement ) ) ) )
            // InternalBasicIQL.g:2590:2: ( () otherlv_1= 'while' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_body_5_0= ruleIQLStatement ) ) )
            {
            // InternalBasicIQL.g:2590:2: ( () otherlv_1= 'while' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_body_5_0= ruleIQLStatement ) ) )
            // InternalBasicIQL.g:2591:3: () otherlv_1= 'while' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_body_5_0= ruleIQLStatement ) )
            {
            // InternalBasicIQL.g:2591:3: ()
            // InternalBasicIQL.g:2592:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLWhileStatementAccess().getIQLWhileStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,34,FOLLOW_22); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLWhileStatementAccess().getWhileKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,27,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLWhileStatementAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalBasicIQL.g:2606:3: ( (lv_predicate_3_0= ruleIQLExpression ) )
            // InternalBasicIQL.g:2607:4: (lv_predicate_3_0= ruleIQLExpression )
            {
            // InternalBasicIQL.g:2607:4: (lv_predicate_3_0= ruleIQLExpression )
            // InternalBasicIQL.g:2608:5: lv_predicate_3_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLWhileStatementAccess().getPredicateIQLExpressionParserRuleCall_3_0());
              				
            }
            pushFollow(FOLLOW_36);
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

            otherlv_4=(Token)match(input,28,FOLLOW_37); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLWhileStatementAccess().getRightParenthesisKeyword_4());
              		
            }
            // InternalBasicIQL.g:2629:3: ( (lv_body_5_0= ruleIQLStatement ) )
            // InternalBasicIQL.g:2630:4: (lv_body_5_0= ruleIQLStatement )
            {
            // InternalBasicIQL.g:2630:4: (lv_body_5_0= ruleIQLStatement )
            // InternalBasicIQL.g:2631:5: lv_body_5_0= ruleIQLStatement
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
    // InternalBasicIQL.g:2652:1: entryRuleIQLDoWhileStatement returns [EObject current=null] : iv_ruleIQLDoWhileStatement= ruleIQLDoWhileStatement EOF ;
    public final EObject entryRuleIQLDoWhileStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLDoWhileStatement = null;


        try {
            // InternalBasicIQL.g:2652:60: (iv_ruleIQLDoWhileStatement= ruleIQLDoWhileStatement EOF )
            // InternalBasicIQL.g:2653:2: iv_ruleIQLDoWhileStatement= ruleIQLDoWhileStatement EOF
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
    // InternalBasicIQL.g:2659:1: ruleIQLDoWhileStatement returns [EObject current=null] : ( () otherlv_1= 'do' ( (lv_body_2_0= ruleIQLStatement ) ) otherlv_3= 'while' otherlv_4= '(' ( (lv_predicate_5_0= ruleIQLExpression ) ) otherlv_6= ')' otherlv_7= ';' ) ;
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
            // InternalBasicIQL.g:2665:2: ( ( () otherlv_1= 'do' ( (lv_body_2_0= ruleIQLStatement ) ) otherlv_3= 'while' otherlv_4= '(' ( (lv_predicate_5_0= ruleIQLExpression ) ) otherlv_6= ')' otherlv_7= ';' ) )
            // InternalBasicIQL.g:2666:2: ( () otherlv_1= 'do' ( (lv_body_2_0= ruleIQLStatement ) ) otherlv_3= 'while' otherlv_4= '(' ( (lv_predicate_5_0= ruleIQLExpression ) ) otherlv_6= ')' otherlv_7= ';' )
            {
            // InternalBasicIQL.g:2666:2: ( () otherlv_1= 'do' ( (lv_body_2_0= ruleIQLStatement ) ) otherlv_3= 'while' otherlv_4= '(' ( (lv_predicate_5_0= ruleIQLExpression ) ) otherlv_6= ')' otherlv_7= ';' )
            // InternalBasicIQL.g:2667:3: () otherlv_1= 'do' ( (lv_body_2_0= ruleIQLStatement ) ) otherlv_3= 'while' otherlv_4= '(' ( (lv_predicate_5_0= ruleIQLExpression ) ) otherlv_6= ')' otherlv_7= ';'
            {
            // InternalBasicIQL.g:2667:3: ()
            // InternalBasicIQL.g:2668:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLDoWhileStatementAccess().getIQLDoWhileStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,35,FOLLOW_37); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLDoWhileStatementAccess().getDoKeyword_1());
              		
            }
            // InternalBasicIQL.g:2678:3: ( (lv_body_2_0= ruleIQLStatement ) )
            // InternalBasicIQL.g:2679:4: (lv_body_2_0= ruleIQLStatement )
            {
            // InternalBasicIQL.g:2679:4: (lv_body_2_0= ruleIQLStatement )
            // InternalBasicIQL.g:2680:5: lv_body_2_0= ruleIQLStatement
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLDoWhileStatementAccess().getBodyIQLStatementParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_39);
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

            otherlv_3=(Token)match(input,34,FOLLOW_22); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getIQLDoWhileStatementAccess().getWhileKeyword_3());
              		
            }
            otherlv_4=(Token)match(input,27,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLDoWhileStatementAccess().getLeftParenthesisKeyword_4());
              		
            }
            // InternalBasicIQL.g:2705:3: ( (lv_predicate_5_0= ruleIQLExpression ) )
            // InternalBasicIQL.g:2706:4: (lv_predicate_5_0= ruleIQLExpression )
            {
            // InternalBasicIQL.g:2706:4: (lv_predicate_5_0= ruleIQLExpression )
            // InternalBasicIQL.g:2707:5: lv_predicate_5_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLDoWhileStatementAccess().getPredicateIQLExpressionParserRuleCall_5_0());
              				
            }
            pushFollow(FOLLOW_36);
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

            otherlv_6=(Token)match(input,28,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_6, grammarAccess.getIQLDoWhileStatementAccess().getRightParenthesisKeyword_6());
              		
            }
            otherlv_7=(Token)match(input,14,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:2736:1: entryRuleIQLForStatement returns [EObject current=null] : iv_ruleIQLForStatement= ruleIQLForStatement EOF ;
    public final EObject entryRuleIQLForStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLForStatement = null;


        try {
            // InternalBasicIQL.g:2736:56: (iv_ruleIQLForStatement= ruleIQLForStatement EOF )
            // InternalBasicIQL.g:2737:2: iv_ruleIQLForStatement= ruleIQLForStatement EOF
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
    // InternalBasicIQL.g:2743:1: ruleIQLForStatement returns [EObject current=null] : ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) otherlv_6= ';' ( (lv_predicate_7_0= ruleIQLExpression ) ) otherlv_8= ';' ( (lv_updateExpr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ( (lv_body_11_0= ruleIQLStatement ) ) ) ;
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
            // InternalBasicIQL.g:2749:2: ( ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) otherlv_6= ';' ( (lv_predicate_7_0= ruleIQLExpression ) ) otherlv_8= ';' ( (lv_updateExpr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ( (lv_body_11_0= ruleIQLStatement ) ) ) )
            // InternalBasicIQL.g:2750:2: ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) otherlv_6= ';' ( (lv_predicate_7_0= ruleIQLExpression ) ) otherlv_8= ';' ( (lv_updateExpr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ( (lv_body_11_0= ruleIQLStatement ) ) )
            {
            // InternalBasicIQL.g:2750:2: ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) otherlv_6= ';' ( (lv_predicate_7_0= ruleIQLExpression ) ) otherlv_8= ';' ( (lv_updateExpr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ( (lv_body_11_0= ruleIQLStatement ) ) )
            // InternalBasicIQL.g:2751:3: () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) otherlv_6= ';' ( (lv_predicate_7_0= ruleIQLExpression ) ) otherlv_8= ';' ( (lv_updateExpr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ( (lv_body_11_0= ruleIQLStatement ) )
            {
            // InternalBasicIQL.g:2751:3: ()
            // InternalBasicIQL.g:2752:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLForStatementAccess().getIQLForStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,36,FOLLOW_22); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLForStatementAccess().getForKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,27,FOLLOW_3); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLForStatementAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalBasicIQL.g:2766:3: ( (lv_var_3_0= ruleIQLVariableDeclaration ) )
            // InternalBasicIQL.g:2767:4: (lv_var_3_0= ruleIQLVariableDeclaration )
            {
            // InternalBasicIQL.g:2767:4: (lv_var_3_0= ruleIQLVariableDeclaration )
            // InternalBasicIQL.g:2768:5: lv_var_3_0= ruleIQLVariableDeclaration
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForStatementAccess().getVarIQLVariableDeclarationParserRuleCall_3_0());
              				
            }
            pushFollow(FOLLOW_30);
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

            otherlv_4=(Token)match(input,30,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLForStatementAccess().getEqualsSignKeyword_4());
              		
            }
            // InternalBasicIQL.g:2789:3: ( (lv_value_5_0= ruleIQLExpression ) )
            // InternalBasicIQL.g:2790:4: (lv_value_5_0= ruleIQLExpression )
            {
            // InternalBasicIQL.g:2790:4: (lv_value_5_0= ruleIQLExpression )
            // InternalBasicIQL.g:2791:5: lv_value_5_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForStatementAccess().getValueIQLExpressionParserRuleCall_5_0());
              				
            }
            pushFollow(FOLLOW_4);
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

            otherlv_6=(Token)match(input,14,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_6, grammarAccess.getIQLForStatementAccess().getSemicolonKeyword_6());
              		
            }
            // InternalBasicIQL.g:2812:3: ( (lv_predicate_7_0= ruleIQLExpression ) )
            // InternalBasicIQL.g:2813:4: (lv_predicate_7_0= ruleIQLExpression )
            {
            // InternalBasicIQL.g:2813:4: (lv_predicate_7_0= ruleIQLExpression )
            // InternalBasicIQL.g:2814:5: lv_predicate_7_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForStatementAccess().getPredicateIQLExpressionParserRuleCall_7_0());
              				
            }
            pushFollow(FOLLOW_4);
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

            otherlv_8=(Token)match(input,14,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_8, grammarAccess.getIQLForStatementAccess().getSemicolonKeyword_8());
              		
            }
            // InternalBasicIQL.g:2835:3: ( (lv_updateExpr_9_0= ruleIQLExpression ) )
            // InternalBasicIQL.g:2836:4: (lv_updateExpr_9_0= ruleIQLExpression )
            {
            // InternalBasicIQL.g:2836:4: (lv_updateExpr_9_0= ruleIQLExpression )
            // InternalBasicIQL.g:2837:5: lv_updateExpr_9_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForStatementAccess().getUpdateExprIQLExpressionParserRuleCall_9_0());
              				
            }
            pushFollow(FOLLOW_36);
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

            otherlv_10=(Token)match(input,28,FOLLOW_37); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_10, grammarAccess.getIQLForStatementAccess().getRightParenthesisKeyword_10());
              		
            }
            // InternalBasicIQL.g:2858:3: ( (lv_body_11_0= ruleIQLStatement ) )
            // InternalBasicIQL.g:2859:4: (lv_body_11_0= ruleIQLStatement )
            {
            // InternalBasicIQL.g:2859:4: (lv_body_11_0= ruleIQLStatement )
            // InternalBasicIQL.g:2860:5: lv_body_11_0= ruleIQLStatement
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
    // InternalBasicIQL.g:2881:1: entryRuleIQLForEachStatement returns [EObject current=null] : iv_ruleIQLForEachStatement= ruleIQLForEachStatement EOF ;
    public final EObject entryRuleIQLForEachStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLForEachStatement = null;


        try {
            // InternalBasicIQL.g:2881:60: (iv_ruleIQLForEachStatement= ruleIQLForEachStatement EOF )
            // InternalBasicIQL.g:2882:2: iv_ruleIQLForEachStatement= ruleIQLForEachStatement EOF
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
    // InternalBasicIQL.g:2888:1: ruleIQLForEachStatement returns [EObject current=null] : ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= ':' ( (lv_forExpression_5_0= ruleIQLExpression ) ) otherlv_6= ')' ( (lv_body_7_0= ruleIQLStatement ) ) ) ;
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
            // InternalBasicIQL.g:2894:2: ( ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= ':' ( (lv_forExpression_5_0= ruleIQLExpression ) ) otherlv_6= ')' ( (lv_body_7_0= ruleIQLStatement ) ) ) )
            // InternalBasicIQL.g:2895:2: ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= ':' ( (lv_forExpression_5_0= ruleIQLExpression ) ) otherlv_6= ')' ( (lv_body_7_0= ruleIQLStatement ) ) )
            {
            // InternalBasicIQL.g:2895:2: ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= ':' ( (lv_forExpression_5_0= ruleIQLExpression ) ) otherlv_6= ')' ( (lv_body_7_0= ruleIQLStatement ) ) )
            // InternalBasicIQL.g:2896:3: () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= ':' ( (lv_forExpression_5_0= ruleIQLExpression ) ) otherlv_6= ')' ( (lv_body_7_0= ruleIQLStatement ) )
            {
            // InternalBasicIQL.g:2896:3: ()
            // InternalBasicIQL.g:2897:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLForEachStatementAccess().getIQLForEachStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,36,FOLLOW_22); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLForEachStatementAccess().getForKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,27,FOLLOW_3); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLForEachStatementAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalBasicIQL.g:2911:3: ( (lv_var_3_0= ruleIQLVariableDeclaration ) )
            // InternalBasicIQL.g:2912:4: (lv_var_3_0= ruleIQLVariableDeclaration )
            {
            // InternalBasicIQL.g:2912:4: (lv_var_3_0= ruleIQLVariableDeclaration )
            // InternalBasicIQL.g:2913:5: lv_var_3_0= ruleIQLVariableDeclaration
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForEachStatementAccess().getVarIQLVariableDeclarationParserRuleCall_3_0());
              				
            }
            pushFollow(FOLLOW_40);
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

            otherlv_4=(Token)match(input,29,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLForEachStatementAccess().getColonKeyword_4());
              		
            }
            // InternalBasicIQL.g:2934:3: ( (lv_forExpression_5_0= ruleIQLExpression ) )
            // InternalBasicIQL.g:2935:4: (lv_forExpression_5_0= ruleIQLExpression )
            {
            // InternalBasicIQL.g:2935:4: (lv_forExpression_5_0= ruleIQLExpression )
            // InternalBasicIQL.g:2936:5: lv_forExpression_5_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForEachStatementAccess().getForExpressionIQLExpressionParserRuleCall_5_0());
              				
            }
            pushFollow(FOLLOW_36);
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

            otherlv_6=(Token)match(input,28,FOLLOW_37); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_6, grammarAccess.getIQLForEachStatementAccess().getRightParenthesisKeyword_6());
              		
            }
            // InternalBasicIQL.g:2957:3: ( (lv_body_7_0= ruleIQLStatement ) )
            // InternalBasicIQL.g:2958:4: (lv_body_7_0= ruleIQLStatement )
            {
            // InternalBasicIQL.g:2958:4: (lv_body_7_0= ruleIQLStatement )
            // InternalBasicIQL.g:2959:5: lv_body_7_0= ruleIQLStatement
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
    // InternalBasicIQL.g:2980:1: entryRuleIQLSwitchStatement returns [EObject current=null] : iv_ruleIQLSwitchStatement= ruleIQLSwitchStatement EOF ;
    public final EObject entryRuleIQLSwitchStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLSwitchStatement = null;


        try {
            // InternalBasicIQL.g:2980:59: (iv_ruleIQLSwitchStatement= ruleIQLSwitchStatement EOF )
            // InternalBasicIQL.g:2981:2: iv_ruleIQLSwitchStatement= ruleIQLSwitchStatement EOF
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
    // InternalBasicIQL.g:2987:1: ruleIQLSwitchStatement returns [EObject current=null] : ( () otherlv_1= 'switch' otherlv_2= '(' ( (lv_expr_3_0= ruleIQLExpression ) ) otherlv_4= ')' otherlv_5= '{' ( (lv_cases_6_0= ruleIQLCasePart ) )* (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )? otherlv_10= '}' ) ;
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
            // InternalBasicIQL.g:2993:2: ( ( () otherlv_1= 'switch' otherlv_2= '(' ( (lv_expr_3_0= ruleIQLExpression ) ) otherlv_4= ')' otherlv_5= '{' ( (lv_cases_6_0= ruleIQLCasePart ) )* (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )? otherlv_10= '}' ) )
            // InternalBasicIQL.g:2994:2: ( () otherlv_1= 'switch' otherlv_2= '(' ( (lv_expr_3_0= ruleIQLExpression ) ) otherlv_4= ')' otherlv_5= '{' ( (lv_cases_6_0= ruleIQLCasePart ) )* (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )? otherlv_10= '}' )
            {
            // InternalBasicIQL.g:2994:2: ( () otherlv_1= 'switch' otherlv_2= '(' ( (lv_expr_3_0= ruleIQLExpression ) ) otherlv_4= ')' otherlv_5= '{' ( (lv_cases_6_0= ruleIQLCasePart ) )* (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )? otherlv_10= '}' )
            // InternalBasicIQL.g:2995:3: () otherlv_1= 'switch' otherlv_2= '(' ( (lv_expr_3_0= ruleIQLExpression ) ) otherlv_4= ')' otherlv_5= '{' ( (lv_cases_6_0= ruleIQLCasePart ) )* (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )? otherlv_10= '}'
            {
            // InternalBasicIQL.g:2995:3: ()
            // InternalBasicIQL.g:2996:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLSwitchStatementAccess().getIQLSwitchStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,37,FOLLOW_22); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLSwitchStatementAccess().getSwitchKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,27,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLSwitchStatementAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalBasicIQL.g:3010:3: ( (lv_expr_3_0= ruleIQLExpression ) )
            // InternalBasicIQL.g:3011:4: (lv_expr_3_0= ruleIQLExpression )
            {
            // InternalBasicIQL.g:3011:4: (lv_expr_3_0= ruleIQLExpression )
            // InternalBasicIQL.g:3012:5: lv_expr_3_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLSwitchStatementAccess().getExprIQLExpressionParserRuleCall_3_0());
              				
            }
            pushFollow(FOLLOW_36);
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

            otherlv_4=(Token)match(input,28,FOLLOW_14); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLSwitchStatementAccess().getRightParenthesisKeyword_4());
              		
            }
            otherlv_5=(Token)match(input,21,FOLLOW_41); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getIQLSwitchStatementAccess().getLeftCurlyBracketKeyword_5());
              		
            }
            // InternalBasicIQL.g:3037:3: ( (lv_cases_6_0= ruleIQLCasePart ) )*
            loop43:
            do {
                int alt43=2;
                int LA43_0 = input.LA(1);

                if ( (LA43_0==39) ) {
                    alt43=1;
                }


                switch (alt43) {
            	case 1 :
            	    // InternalBasicIQL.g:3038:4: (lv_cases_6_0= ruleIQLCasePart )
            	    {
            	    // InternalBasicIQL.g:3038:4: (lv_cases_6_0= ruleIQLCasePart )
            	    // InternalBasicIQL.g:3039:5: lv_cases_6_0= ruleIQLCasePart
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLSwitchStatementAccess().getCasesIQLCasePartParserRuleCall_6_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_41);
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
            	    break loop43;
                }
            } while (true);

            // InternalBasicIQL.g:3056:3: (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )?
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==38) ) {
                alt45=1;
            }
            switch (alt45) {
                case 1 :
                    // InternalBasicIQL.g:3057:4: otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )*
                    {
                    otherlv_7=(Token)match(input,38,FOLLOW_40); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_7, grammarAccess.getIQLSwitchStatementAccess().getDefaultKeyword_7_0());
                      			
                    }
                    otherlv_8=(Token)match(input,29,FOLLOW_35); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_8, grammarAccess.getIQLSwitchStatementAccess().getColonKeyword_7_1());
                      			
                    }
                    // InternalBasicIQL.g:3065:4: ( (lv_statements_9_0= ruleIQLStatement ) )*
                    loop44:
                    do {
                        int alt44=2;
                        int LA44_0 = input.LA(1);

                        if ( ((LA44_0>=RULE_ID && LA44_0<=RULE_RANGE)||LA44_0==21||LA44_0==24||LA44_0==27||(LA44_0>=31 && LA44_0<=32)||(LA44_0>=34 && LA44_0<=37)||(LA44_0>=40 && LA44_0<=44)||(LA44_0>=59 && LA44_0<=60)||(LA44_0>=64 && LA44_0<=66)||(LA44_0>=68 && LA44_0<=69)||LA44_0==72||(LA44_0>=109 && LA44_0<=110)) ) {
                            alt44=1;
                        }


                        switch (alt44) {
                    	case 1 :
                    	    // InternalBasicIQL.g:3066:5: (lv_statements_9_0= ruleIQLStatement )
                    	    {
                    	    // InternalBasicIQL.g:3066:5: (lv_statements_9_0= ruleIQLStatement )
                    	    // InternalBasicIQL.g:3067:6: lv_statements_9_0= ruleIQLStatement
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      						newCompositeNode(grammarAccess.getIQLSwitchStatementAccess().getStatementsIQLStatementParserRuleCall_7_2_0());
                    	      					
                    	    }
                    	    pushFollow(FOLLOW_35);
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
                    	    break loop44;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_10=(Token)match(input,22,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:3093:1: entryRuleIQLCasePart returns [EObject current=null] : iv_ruleIQLCasePart= ruleIQLCasePart EOF ;
    public final EObject entryRuleIQLCasePart() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLCasePart = null;


        try {
            // InternalBasicIQL.g:3093:52: (iv_ruleIQLCasePart= ruleIQLCasePart EOF )
            // InternalBasicIQL.g:3094:2: iv_ruleIQLCasePart= ruleIQLCasePart EOF
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
    // InternalBasicIQL.g:3100:1: ruleIQLCasePart returns [EObject current=null] : ( () otherlv_1= 'case' ( (lv_expr_2_0= ruleIQLLiteralExpression ) ) otherlv_3= ':' ( (lv_statements_4_0= ruleIQLStatement ) )* ) ;
    public final EObject ruleIQLCasePart() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject lv_expr_2_0 = null;

        EObject lv_statements_4_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:3106:2: ( ( () otherlv_1= 'case' ( (lv_expr_2_0= ruleIQLLiteralExpression ) ) otherlv_3= ':' ( (lv_statements_4_0= ruleIQLStatement ) )* ) )
            // InternalBasicIQL.g:3107:2: ( () otherlv_1= 'case' ( (lv_expr_2_0= ruleIQLLiteralExpression ) ) otherlv_3= ':' ( (lv_statements_4_0= ruleIQLStatement ) )* )
            {
            // InternalBasicIQL.g:3107:2: ( () otherlv_1= 'case' ( (lv_expr_2_0= ruleIQLLiteralExpression ) ) otherlv_3= ':' ( (lv_statements_4_0= ruleIQLStatement ) )* )
            // InternalBasicIQL.g:3108:3: () otherlv_1= 'case' ( (lv_expr_2_0= ruleIQLLiteralExpression ) ) otherlv_3= ':' ( (lv_statements_4_0= ruleIQLStatement ) )*
            {
            // InternalBasicIQL.g:3108:3: ()
            // InternalBasicIQL.g:3109:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLCasePartAccess().getIQLCasePartAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,39,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLCasePartAccess().getCaseKeyword_1());
              		
            }
            // InternalBasicIQL.g:3119:3: ( (lv_expr_2_0= ruleIQLLiteralExpression ) )
            // InternalBasicIQL.g:3120:4: (lv_expr_2_0= ruleIQLLiteralExpression )
            {
            // InternalBasicIQL.g:3120:4: (lv_expr_2_0= ruleIQLLiteralExpression )
            // InternalBasicIQL.g:3121:5: lv_expr_2_0= ruleIQLLiteralExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLCasePartAccess().getExprIQLLiteralExpressionParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_40);
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

            otherlv_3=(Token)match(input,29,FOLLOW_42); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getIQLCasePartAccess().getColonKeyword_3());
              		
            }
            // InternalBasicIQL.g:3142:3: ( (lv_statements_4_0= ruleIQLStatement ) )*
            loop46:
            do {
                int alt46=2;
                int LA46_0 = input.LA(1);

                if ( ((LA46_0>=RULE_ID && LA46_0<=RULE_RANGE)||LA46_0==21||LA46_0==24||LA46_0==27||(LA46_0>=31 && LA46_0<=32)||(LA46_0>=34 && LA46_0<=37)||(LA46_0>=40 && LA46_0<=44)||(LA46_0>=59 && LA46_0<=60)||(LA46_0>=64 && LA46_0<=66)||(LA46_0>=68 && LA46_0<=69)||LA46_0==72||(LA46_0>=109 && LA46_0<=110)) ) {
                    alt46=1;
                }


                switch (alt46) {
            	case 1 :
            	    // InternalBasicIQL.g:3143:4: (lv_statements_4_0= ruleIQLStatement )
            	    {
            	    // InternalBasicIQL.g:3143:4: (lv_statements_4_0= ruleIQLStatement )
            	    // InternalBasicIQL.g:3144:5: lv_statements_4_0= ruleIQLStatement
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLCasePartAccess().getStatementsIQLStatementParserRuleCall_4_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_42);
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
            	    break loop46;
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
    // InternalBasicIQL.g:3165:1: entryRuleIQLExpressionStatement returns [EObject current=null] : iv_ruleIQLExpressionStatement= ruleIQLExpressionStatement EOF ;
    public final EObject entryRuleIQLExpressionStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLExpressionStatement = null;


        try {
            // InternalBasicIQL.g:3165:63: (iv_ruleIQLExpressionStatement= ruleIQLExpressionStatement EOF )
            // InternalBasicIQL.g:3166:2: iv_ruleIQLExpressionStatement= ruleIQLExpressionStatement EOF
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
    // InternalBasicIQL.g:3172:1: ruleIQLExpressionStatement returns [EObject current=null] : ( () ( (lv_expression_1_0= ruleIQLExpression ) ) otherlv_2= ';' ) ;
    public final EObject ruleIQLExpressionStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject lv_expression_1_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:3178:2: ( ( () ( (lv_expression_1_0= ruleIQLExpression ) ) otherlv_2= ';' ) )
            // InternalBasicIQL.g:3179:2: ( () ( (lv_expression_1_0= ruleIQLExpression ) ) otherlv_2= ';' )
            {
            // InternalBasicIQL.g:3179:2: ( () ( (lv_expression_1_0= ruleIQLExpression ) ) otherlv_2= ';' )
            // InternalBasicIQL.g:3180:3: () ( (lv_expression_1_0= ruleIQLExpression ) ) otherlv_2= ';'
            {
            // InternalBasicIQL.g:3180:3: ()
            // InternalBasicIQL.g:3181:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLExpressionStatementAccess().getIQLExpressionStatementAction_0(),
              					current);
              			
            }

            }

            // InternalBasicIQL.g:3187:3: ( (lv_expression_1_0= ruleIQLExpression ) )
            // InternalBasicIQL.g:3188:4: (lv_expression_1_0= ruleIQLExpression )
            {
            // InternalBasicIQL.g:3188:4: (lv_expression_1_0= ruleIQLExpression )
            // InternalBasicIQL.g:3189:5: lv_expression_1_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLExpressionStatementAccess().getExpressionIQLExpressionParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_4);
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

            otherlv_2=(Token)match(input,14,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:3214:1: entryRuleIQLVariableStatement returns [EObject current=null] : iv_ruleIQLVariableStatement= ruleIQLVariableStatement EOF ;
    public final EObject entryRuleIQLVariableStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLVariableStatement = null;


        try {
            // InternalBasicIQL.g:3214:61: (iv_ruleIQLVariableStatement= ruleIQLVariableStatement EOF )
            // InternalBasicIQL.g:3215:2: iv_ruleIQLVariableStatement= ruleIQLVariableStatement EOF
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
    // InternalBasicIQL.g:3221:1: ruleIQLVariableStatement returns [EObject current=null] : ( () ( (lv_var_1_0= ruleIQLVariableDeclaration ) ) ( (lv_init_2_0= ruleIQLVariableInitialization ) ) otherlv_3= ';' ) ;
    public final EObject ruleIQLVariableStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_3=null;
        EObject lv_var_1_0 = null;

        EObject lv_init_2_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:3227:2: ( ( () ( (lv_var_1_0= ruleIQLVariableDeclaration ) ) ( (lv_init_2_0= ruleIQLVariableInitialization ) ) otherlv_3= ';' ) )
            // InternalBasicIQL.g:3228:2: ( () ( (lv_var_1_0= ruleIQLVariableDeclaration ) ) ( (lv_init_2_0= ruleIQLVariableInitialization ) ) otherlv_3= ';' )
            {
            // InternalBasicIQL.g:3228:2: ( () ( (lv_var_1_0= ruleIQLVariableDeclaration ) ) ( (lv_init_2_0= ruleIQLVariableInitialization ) ) otherlv_3= ';' )
            // InternalBasicIQL.g:3229:3: () ( (lv_var_1_0= ruleIQLVariableDeclaration ) ) ( (lv_init_2_0= ruleIQLVariableInitialization ) ) otherlv_3= ';'
            {
            // InternalBasicIQL.g:3229:3: ()
            // InternalBasicIQL.g:3230:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLVariableStatementAccess().getIQLVariableStatementAction_0(),
              					current);
              			
            }

            }

            // InternalBasicIQL.g:3236:3: ( (lv_var_1_0= ruleIQLVariableDeclaration ) )
            // InternalBasicIQL.g:3237:4: (lv_var_1_0= ruleIQLVariableDeclaration )
            {
            // InternalBasicIQL.g:3237:4: (lv_var_1_0= ruleIQLVariableDeclaration )
            // InternalBasicIQL.g:3238:5: lv_var_1_0= ruleIQLVariableDeclaration
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLVariableStatementAccess().getVarIQLVariableDeclarationParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_43);
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

            // InternalBasicIQL.g:3255:3: ( (lv_init_2_0= ruleIQLVariableInitialization ) )
            // InternalBasicIQL.g:3256:4: (lv_init_2_0= ruleIQLVariableInitialization )
            {
            // InternalBasicIQL.g:3256:4: (lv_init_2_0= ruleIQLVariableInitialization )
            // InternalBasicIQL.g:3257:5: lv_init_2_0= ruleIQLVariableInitialization
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLVariableStatementAccess().getInitIQLVariableInitializationParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_4);
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

            otherlv_3=(Token)match(input,14,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:3282:1: entryRuleIQLConstructorCallStatement returns [EObject current=null] : iv_ruleIQLConstructorCallStatement= ruleIQLConstructorCallStatement EOF ;
    public final EObject entryRuleIQLConstructorCallStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLConstructorCallStatement = null;


        try {
            // InternalBasicIQL.g:3282:68: (iv_ruleIQLConstructorCallStatement= ruleIQLConstructorCallStatement EOF )
            // InternalBasicIQL.g:3283:2: iv_ruleIQLConstructorCallStatement= ruleIQLConstructorCallStatement EOF
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
    // InternalBasicIQL.g:3289:1: ruleIQLConstructorCallStatement returns [EObject current=null] : ( () ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) ) ( (lv_args_3_0= ruleIQLArgumentsList ) ) otherlv_4= ';' ) ;
    public final EObject ruleIQLConstructorCallStatement() throws RecognitionException {
        EObject current = null;

        Token lv_this_1_0=null;
        Token lv_super_2_0=null;
        Token otherlv_4=null;
        EObject lv_args_3_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:3295:2: ( ( () ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) ) ( (lv_args_3_0= ruleIQLArgumentsList ) ) otherlv_4= ';' ) )
            // InternalBasicIQL.g:3296:2: ( () ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) ) ( (lv_args_3_0= ruleIQLArgumentsList ) ) otherlv_4= ';' )
            {
            // InternalBasicIQL.g:3296:2: ( () ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) ) ( (lv_args_3_0= ruleIQLArgumentsList ) ) otherlv_4= ';' )
            // InternalBasicIQL.g:3297:3: () ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) ) ( (lv_args_3_0= ruleIQLArgumentsList ) ) otherlv_4= ';'
            {
            // InternalBasicIQL.g:3297:3: ()
            // InternalBasicIQL.g:3298:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLConstructorCallStatementAccess().getIQLConstructorCallStatementAction_0(),
              					current);
              			
            }

            }

            // InternalBasicIQL.g:3304:3: ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) )
            int alt47=2;
            int LA47_0 = input.LA(1);

            if ( (LA47_0==40) ) {
                alt47=1;
            }
            else if ( (LA47_0==41) ) {
                alt47=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 47, 0, input);

                throw nvae;
            }
            switch (alt47) {
                case 1 :
                    // InternalBasicIQL.g:3305:4: ( (lv_this_1_0= 'this' ) )
                    {
                    // InternalBasicIQL.g:3305:4: ( (lv_this_1_0= 'this' ) )
                    // InternalBasicIQL.g:3306:5: (lv_this_1_0= 'this' )
                    {
                    // InternalBasicIQL.g:3306:5: (lv_this_1_0= 'this' )
                    // InternalBasicIQL.g:3307:6: lv_this_1_0= 'this'
                    {
                    lv_this_1_0=(Token)match(input,40,FOLLOW_22); if (state.failed) return current;
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
                    // InternalBasicIQL.g:3320:4: ( (lv_super_2_0= 'super' ) )
                    {
                    // InternalBasicIQL.g:3320:4: ( (lv_super_2_0= 'super' ) )
                    // InternalBasicIQL.g:3321:5: (lv_super_2_0= 'super' )
                    {
                    // InternalBasicIQL.g:3321:5: (lv_super_2_0= 'super' )
                    // InternalBasicIQL.g:3322:6: lv_super_2_0= 'super'
                    {
                    lv_super_2_0=(Token)match(input,41,FOLLOW_22); if (state.failed) return current;
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

            // InternalBasicIQL.g:3335:3: ( (lv_args_3_0= ruleIQLArgumentsList ) )
            // InternalBasicIQL.g:3336:4: (lv_args_3_0= ruleIQLArgumentsList )
            {
            // InternalBasicIQL.g:3336:4: (lv_args_3_0= ruleIQLArgumentsList )
            // InternalBasicIQL.g:3337:5: lv_args_3_0= ruleIQLArgumentsList
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLConstructorCallStatementAccess().getArgsIQLArgumentsListParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_4);
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

            otherlv_4=(Token)match(input,14,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:3362:1: entryRuleIQLBreakStatement returns [EObject current=null] : iv_ruleIQLBreakStatement= ruleIQLBreakStatement EOF ;
    public final EObject entryRuleIQLBreakStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLBreakStatement = null;


        try {
            // InternalBasicIQL.g:3362:58: (iv_ruleIQLBreakStatement= ruleIQLBreakStatement EOF )
            // InternalBasicIQL.g:3363:2: iv_ruleIQLBreakStatement= ruleIQLBreakStatement EOF
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
    // InternalBasicIQL.g:3369:1: ruleIQLBreakStatement returns [EObject current=null] : ( () otherlv_1= 'break' otherlv_2= ';' ) ;
    public final EObject ruleIQLBreakStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:3375:2: ( ( () otherlv_1= 'break' otherlv_2= ';' ) )
            // InternalBasicIQL.g:3376:2: ( () otherlv_1= 'break' otherlv_2= ';' )
            {
            // InternalBasicIQL.g:3376:2: ( () otherlv_1= 'break' otherlv_2= ';' )
            // InternalBasicIQL.g:3377:3: () otherlv_1= 'break' otherlv_2= ';'
            {
            // InternalBasicIQL.g:3377:3: ()
            // InternalBasicIQL.g:3378:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLBreakStatementAccess().getIQLBreakStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,42,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLBreakStatementAccess().getBreakKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,14,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:3396:1: entryRuleIQLContinueStatement returns [EObject current=null] : iv_ruleIQLContinueStatement= ruleIQLContinueStatement EOF ;
    public final EObject entryRuleIQLContinueStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLContinueStatement = null;


        try {
            // InternalBasicIQL.g:3396:61: (iv_ruleIQLContinueStatement= ruleIQLContinueStatement EOF )
            // InternalBasicIQL.g:3397:2: iv_ruleIQLContinueStatement= ruleIQLContinueStatement EOF
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
    // InternalBasicIQL.g:3403:1: ruleIQLContinueStatement returns [EObject current=null] : ( () otherlv_1= 'continue' otherlv_2= ';' ) ;
    public final EObject ruleIQLContinueStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:3409:2: ( ( () otherlv_1= 'continue' otherlv_2= ';' ) )
            // InternalBasicIQL.g:3410:2: ( () otherlv_1= 'continue' otherlv_2= ';' )
            {
            // InternalBasicIQL.g:3410:2: ( () otherlv_1= 'continue' otherlv_2= ';' )
            // InternalBasicIQL.g:3411:3: () otherlv_1= 'continue' otherlv_2= ';'
            {
            // InternalBasicIQL.g:3411:3: ()
            // InternalBasicIQL.g:3412:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLContinueStatementAccess().getIQLContinueStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,43,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLContinueStatementAccess().getContinueKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,14,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:3430:1: entryRuleIQLReturnStatement returns [EObject current=null] : iv_ruleIQLReturnStatement= ruleIQLReturnStatement EOF ;
    public final EObject entryRuleIQLReturnStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLReturnStatement = null;


        try {
            // InternalBasicIQL.g:3430:59: (iv_ruleIQLReturnStatement= ruleIQLReturnStatement EOF )
            // InternalBasicIQL.g:3431:2: iv_ruleIQLReturnStatement= ruleIQLReturnStatement EOF
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
    // InternalBasicIQL.g:3437:1: ruleIQLReturnStatement returns [EObject current=null] : ( () otherlv_1= 'return' ( (lv_expression_2_0= ruleIQLExpression ) )? otherlv_3= ';' ) ;
    public final EObject ruleIQLReturnStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject lv_expression_2_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:3443:2: ( ( () otherlv_1= 'return' ( (lv_expression_2_0= ruleIQLExpression ) )? otherlv_3= ';' ) )
            // InternalBasicIQL.g:3444:2: ( () otherlv_1= 'return' ( (lv_expression_2_0= ruleIQLExpression ) )? otherlv_3= ';' )
            {
            // InternalBasicIQL.g:3444:2: ( () otherlv_1= 'return' ( (lv_expression_2_0= ruleIQLExpression ) )? otherlv_3= ';' )
            // InternalBasicIQL.g:3445:3: () otherlv_1= 'return' ( (lv_expression_2_0= ruleIQLExpression ) )? otherlv_3= ';'
            {
            // InternalBasicIQL.g:3445:3: ()
            // InternalBasicIQL.g:3446:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLReturnStatementAccess().getIQLReturnStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,44,FOLLOW_44); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLReturnStatementAccess().getReturnKeyword_1());
              		
            }
            // InternalBasicIQL.g:3456:3: ( (lv_expression_2_0= ruleIQLExpression ) )?
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( ((LA48_0>=RULE_ID && LA48_0<=RULE_RANGE)||LA48_0==24||LA48_0==27||LA48_0==31||(LA48_0>=40 && LA48_0<=41)||(LA48_0>=59 && LA48_0<=60)||(LA48_0>=64 && LA48_0<=66)||(LA48_0>=68 && LA48_0<=69)||(LA48_0>=109 && LA48_0<=110)) ) {
                alt48=1;
            }
            switch (alt48) {
                case 1 :
                    // InternalBasicIQL.g:3457:4: (lv_expression_2_0= ruleIQLExpression )
                    {
                    // InternalBasicIQL.g:3457:4: (lv_expression_2_0= ruleIQLExpression )
                    // InternalBasicIQL.g:3458:5: lv_expression_2_0= ruleIQLExpression
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getIQLReturnStatementAccess().getExpressionIQLExpressionParserRuleCall_2_0());
                      				
                    }
                    pushFollow(FOLLOW_4);
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

            otherlv_3=(Token)match(input,14,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:3483:1: entryRuleIQLExpression returns [EObject current=null] : iv_ruleIQLExpression= ruleIQLExpression EOF ;
    public final EObject entryRuleIQLExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLExpression = null;


        try {
            // InternalBasicIQL.g:3483:54: (iv_ruleIQLExpression= ruleIQLExpression EOF )
            // InternalBasicIQL.g:3484:2: iv_ruleIQLExpression= ruleIQLExpression EOF
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
    // InternalBasicIQL.g:3490:1: ruleIQLExpression returns [EObject current=null] : this_IQLAssignmentExpression_0= ruleIQLAssignmentExpression ;
    public final EObject ruleIQLExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLAssignmentExpression_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:3496:2: (this_IQLAssignmentExpression_0= ruleIQLAssignmentExpression )
            // InternalBasicIQL.g:3497:2: this_IQLAssignmentExpression_0= ruleIQLAssignmentExpression
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
    // InternalBasicIQL.g:3508:1: entryRuleIQLAssignmentExpression returns [EObject current=null] : iv_ruleIQLAssignmentExpression= ruleIQLAssignmentExpression EOF ;
    public final EObject entryRuleIQLAssignmentExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLAssignmentExpression = null;


        try {
            // InternalBasicIQL.g:3508:64: (iv_ruleIQLAssignmentExpression= ruleIQLAssignmentExpression EOF )
            // InternalBasicIQL.g:3509:2: iv_ruleIQLAssignmentExpression= ruleIQLAssignmentExpression EOF
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
    // InternalBasicIQL.g:3515:1: ruleIQLAssignmentExpression returns [EObject current=null] : (this_IQLLogicalOrExpression_0= ruleIQLLogicalOrExpression ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )? ) ;
    public final EObject ruleIQLAssignmentExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLLogicalOrExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:3521:2: ( (this_IQLLogicalOrExpression_0= ruleIQLLogicalOrExpression ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )? ) )
            // InternalBasicIQL.g:3522:2: (this_IQLLogicalOrExpression_0= ruleIQLLogicalOrExpression ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )? )
            {
            // InternalBasicIQL.g:3522:2: (this_IQLLogicalOrExpression_0= ruleIQLLogicalOrExpression ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )? )
            // InternalBasicIQL.g:3523:3: this_IQLLogicalOrExpression_0= ruleIQLLogicalOrExpression ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )?
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLAssignmentExpressionAccess().getIQLLogicalOrExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_45);
            this_IQLLogicalOrExpression_0=ruleIQLLogicalOrExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLLogicalOrExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalBasicIQL.g:3531:3: ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )?
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==30) && (synpred2_InternalBasicIQL())) {
                alt49=1;
            }
            else if ( (LA49_0==45) && (synpred2_InternalBasicIQL())) {
                alt49=1;
            }
            else if ( (LA49_0==46) && (synpred2_InternalBasicIQL())) {
                alt49=1;
            }
            else if ( (LA49_0==47) && (synpred2_InternalBasicIQL())) {
                alt49=1;
            }
            else if ( (LA49_0==48) && (synpred2_InternalBasicIQL())) {
                alt49=1;
            }
            else if ( (LA49_0==49) && (synpred2_InternalBasicIQL())) {
                alt49=1;
            }
            switch (alt49) {
                case 1 :
                    // InternalBasicIQL.g:3532:4: ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) )
                    {
                    // InternalBasicIQL.g:3532:4: ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) )
                    // InternalBasicIQL.g:3533:5: ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) )
                    {
                    // InternalBasicIQL.g:3543:5: ( () ( (lv_op_2_0= ruleOpAssign ) ) )
                    // InternalBasicIQL.g:3544:6: () ( (lv_op_2_0= ruleOpAssign ) )
                    {
                    // InternalBasicIQL.g:3544:6: ()
                    // InternalBasicIQL.g:3545:7: 
                    {
                    if ( state.backtracking==0 ) {

                      							current = forceCreateModelElementAndSet(
                      								grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0(),
                      								current);
                      						
                    }

                    }

                    // InternalBasicIQL.g:3551:6: ( (lv_op_2_0= ruleOpAssign ) )
                    // InternalBasicIQL.g:3552:7: (lv_op_2_0= ruleOpAssign )
                    {
                    // InternalBasicIQL.g:3552:7: (lv_op_2_0= ruleOpAssign )
                    // InternalBasicIQL.g:3553:8: lv_op_2_0= ruleOpAssign
                    {
                    if ( state.backtracking==0 ) {

                      								newCompositeNode(grammarAccess.getIQLAssignmentExpressionAccess().getOpOpAssignParserRuleCall_1_0_0_1_0());
                      							
                    }
                    pushFollow(FOLLOW_32);
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

                    // InternalBasicIQL.g:3572:4: ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) )
                    // InternalBasicIQL.g:3573:5: (lv_rightOperand_3_0= ruleIQLAssignmentExpression )
                    {
                    // InternalBasicIQL.g:3573:5: (lv_rightOperand_3_0= ruleIQLAssignmentExpression )
                    // InternalBasicIQL.g:3574:6: lv_rightOperand_3_0= ruleIQLAssignmentExpression
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
    // InternalBasicIQL.g:3596:1: entryRuleOpAssign returns [String current=null] : iv_ruleOpAssign= ruleOpAssign EOF ;
    public final String entryRuleOpAssign() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpAssign = null;


        try {
            // InternalBasicIQL.g:3596:48: (iv_ruleOpAssign= ruleOpAssign EOF )
            // InternalBasicIQL.g:3597:2: iv_ruleOpAssign= ruleOpAssign EOF
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
    // InternalBasicIQL.g:3603:1: ruleOpAssign returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '=' | kw= '+=' | kw= '-=' | kw= '*=' | kw= '/=' | kw= '%=' ) ;
    public final AntlrDatatypeRuleToken ruleOpAssign() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:3609:2: ( (kw= '=' | kw= '+=' | kw= '-=' | kw= '*=' | kw= '/=' | kw= '%=' ) )
            // InternalBasicIQL.g:3610:2: (kw= '=' | kw= '+=' | kw= '-=' | kw= '*=' | kw= '/=' | kw= '%=' )
            {
            // InternalBasicIQL.g:3610:2: (kw= '=' | kw= '+=' | kw= '-=' | kw= '*=' | kw= '/=' | kw= '%=' )
            int alt50=6;
            switch ( input.LA(1) ) {
            case 30:
                {
                alt50=1;
                }
                break;
            case 45:
                {
                alt50=2;
                }
                break;
            case 46:
                {
                alt50=3;
                }
                break;
            case 47:
                {
                alt50=4;
                }
                break;
            case 48:
                {
                alt50=5;
                }
                break;
            case 49:
                {
                alt50=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 50, 0, input);

                throw nvae;
            }

            switch (alt50) {
                case 1 :
                    // InternalBasicIQL.g:3611:3: kw= '='
                    {
                    kw=(Token)match(input,30,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAssignAccess().getEqualsSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalBasicIQL.g:3617:3: kw= '+='
                    {
                    kw=(Token)match(input,45,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAssignAccess().getPlusSignEqualsSignKeyword_1());
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalBasicIQL.g:3623:3: kw= '-='
                    {
                    kw=(Token)match(input,46,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAssignAccess().getHyphenMinusEqualsSignKeyword_2());
                      		
                    }

                    }
                    break;
                case 4 :
                    // InternalBasicIQL.g:3629:3: kw= '*='
                    {
                    kw=(Token)match(input,47,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAssignAccess().getAsteriskEqualsSignKeyword_3());
                      		
                    }

                    }
                    break;
                case 5 :
                    // InternalBasicIQL.g:3635:3: kw= '/='
                    {
                    kw=(Token)match(input,48,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAssignAccess().getSolidusEqualsSignKeyword_4());
                      		
                    }

                    }
                    break;
                case 6 :
                    // InternalBasicIQL.g:3641:3: kw= '%='
                    {
                    kw=(Token)match(input,49,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:3650:1: entryRuleIQLLogicalOrExpression returns [EObject current=null] : iv_ruleIQLLogicalOrExpression= ruleIQLLogicalOrExpression EOF ;
    public final EObject entryRuleIQLLogicalOrExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLogicalOrExpression = null;


        try {
            // InternalBasicIQL.g:3650:63: (iv_ruleIQLLogicalOrExpression= ruleIQLLogicalOrExpression EOF )
            // InternalBasicIQL.g:3651:2: iv_ruleIQLLogicalOrExpression= ruleIQLLogicalOrExpression EOF
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
    // InternalBasicIQL.g:3657:1: ruleIQLLogicalOrExpression returns [EObject current=null] : (this_IQLLogicalAndExpression_0= ruleIQLLogicalAndExpression ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )* ) ;
    public final EObject ruleIQLLogicalOrExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLLogicalAndExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:3663:2: ( (this_IQLLogicalAndExpression_0= ruleIQLLogicalAndExpression ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )* ) )
            // InternalBasicIQL.g:3664:2: (this_IQLLogicalAndExpression_0= ruleIQLLogicalAndExpression ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )* )
            {
            // InternalBasicIQL.g:3664:2: (this_IQLLogicalAndExpression_0= ruleIQLLogicalAndExpression ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )* )
            // InternalBasicIQL.g:3665:3: this_IQLLogicalAndExpression_0= ruleIQLLogicalAndExpression ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalAndExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_46);
            this_IQLLogicalAndExpression_0=ruleIQLLogicalAndExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLLogicalAndExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalBasicIQL.g:3673:3: ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )*
            loop51:
            do {
                int alt51=2;
                int LA51_0 = input.LA(1);

                if ( (LA51_0==50) && (synpred3_InternalBasicIQL())) {
                    alt51=1;
                }


                switch (alt51) {
            	case 1 :
            	    // InternalBasicIQL.g:3674:4: ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) )
            	    {
            	    // InternalBasicIQL.g:3674:4: ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) )
            	    // InternalBasicIQL.g:3675:5: ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) )
            	    {
            	    // InternalBasicIQL.g:3685:5: ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) )
            	    // InternalBasicIQL.g:3686:6: () ( (lv_op_2_0= ruleOpLogicalOr ) )
            	    {
            	    // InternalBasicIQL.g:3686:6: ()
            	    // InternalBasicIQL.g:3687:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    // InternalBasicIQL.g:3693:6: ( (lv_op_2_0= ruleOpLogicalOr ) )
            	    // InternalBasicIQL.g:3694:7: (lv_op_2_0= ruleOpLogicalOr )
            	    {
            	    // InternalBasicIQL.g:3694:7: (lv_op_2_0= ruleOpLogicalOr )
            	    // InternalBasicIQL.g:3695:8: lv_op_2_0= ruleOpLogicalOr
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLLogicalOrExpressionAccess().getOpOpLogicalOrParserRuleCall_1_0_0_1_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_32);
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

            	    // InternalBasicIQL.g:3714:4: ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) )
            	    // InternalBasicIQL.g:3715:5: (lv_rightOperand_3_0= ruleIQLLogicalAndExpression )
            	    {
            	    // InternalBasicIQL.g:3715:5: (lv_rightOperand_3_0= ruleIQLLogicalAndExpression )
            	    // InternalBasicIQL.g:3716:6: lv_rightOperand_3_0= ruleIQLLogicalAndExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLLogicalOrExpressionAccess().getRightOperandIQLLogicalAndExpressionParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_46);
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
            	    break loop51;
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
    // InternalBasicIQL.g:3738:1: entryRuleOpLogicalOr returns [String current=null] : iv_ruleOpLogicalOr= ruleOpLogicalOr EOF ;
    public final String entryRuleOpLogicalOr() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpLogicalOr = null;


        try {
            // InternalBasicIQL.g:3738:51: (iv_ruleOpLogicalOr= ruleOpLogicalOr EOF )
            // InternalBasicIQL.g:3739:2: iv_ruleOpLogicalOr= ruleOpLogicalOr EOF
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
    // InternalBasicIQL.g:3745:1: ruleOpLogicalOr returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= '||' ;
    public final AntlrDatatypeRuleToken ruleOpLogicalOr() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:3751:2: (kw= '||' )
            // InternalBasicIQL.g:3752:2: kw= '||'
            {
            kw=(Token)match(input,50,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:3760:1: entryRuleIQLLogicalAndExpression returns [EObject current=null] : iv_ruleIQLLogicalAndExpression= ruleIQLLogicalAndExpression EOF ;
    public final EObject entryRuleIQLLogicalAndExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLogicalAndExpression = null;


        try {
            // InternalBasicIQL.g:3760:64: (iv_ruleIQLLogicalAndExpression= ruleIQLLogicalAndExpression EOF )
            // InternalBasicIQL.g:3761:2: iv_ruleIQLLogicalAndExpression= ruleIQLLogicalAndExpression EOF
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
    // InternalBasicIQL.g:3767:1: ruleIQLLogicalAndExpression returns [EObject current=null] : (this_IQLEqualityExpression_0= ruleIQLEqualityExpression ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )* ) ;
    public final EObject ruleIQLLogicalAndExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLEqualityExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:3773:2: ( (this_IQLEqualityExpression_0= ruleIQLEqualityExpression ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )* ) )
            // InternalBasicIQL.g:3774:2: (this_IQLEqualityExpression_0= ruleIQLEqualityExpression ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )* )
            {
            // InternalBasicIQL.g:3774:2: (this_IQLEqualityExpression_0= ruleIQLEqualityExpression ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )* )
            // InternalBasicIQL.g:3775:3: this_IQLEqualityExpression_0= ruleIQLEqualityExpression ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLLogicalAndExpressionAccess().getIQLEqualityExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_47);
            this_IQLEqualityExpression_0=ruleIQLEqualityExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLEqualityExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalBasicIQL.g:3783:3: ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )*
            loop52:
            do {
                int alt52=2;
                int LA52_0 = input.LA(1);

                if ( (LA52_0==51) && (synpred4_InternalBasicIQL())) {
                    alt52=1;
                }


                switch (alt52) {
            	case 1 :
            	    // InternalBasicIQL.g:3784:4: ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) )
            	    {
            	    // InternalBasicIQL.g:3784:4: ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) )
            	    // InternalBasicIQL.g:3785:5: ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) )
            	    {
            	    // InternalBasicIQL.g:3795:5: ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) )
            	    // InternalBasicIQL.g:3796:6: () ( (lv_op_2_0= ruleOpLogicalAnd ) )
            	    {
            	    // InternalBasicIQL.g:3796:6: ()
            	    // InternalBasicIQL.g:3797:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    // InternalBasicIQL.g:3803:6: ( (lv_op_2_0= ruleOpLogicalAnd ) )
            	    // InternalBasicIQL.g:3804:7: (lv_op_2_0= ruleOpLogicalAnd )
            	    {
            	    // InternalBasicIQL.g:3804:7: (lv_op_2_0= ruleOpLogicalAnd )
            	    // InternalBasicIQL.g:3805:8: lv_op_2_0= ruleOpLogicalAnd
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLLogicalAndExpressionAccess().getOpOpLogicalAndParserRuleCall_1_0_0_1_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_32);
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

            	    // InternalBasicIQL.g:3824:4: ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) )
            	    // InternalBasicIQL.g:3825:5: (lv_rightOperand_3_0= ruleIQLEqualityExpression )
            	    {
            	    // InternalBasicIQL.g:3825:5: (lv_rightOperand_3_0= ruleIQLEqualityExpression )
            	    // InternalBasicIQL.g:3826:6: lv_rightOperand_3_0= ruleIQLEqualityExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLLogicalAndExpressionAccess().getRightOperandIQLEqualityExpressionParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_47);
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
            	    break loop52;
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
    // InternalBasicIQL.g:3848:1: entryRuleOpLogicalAnd returns [String current=null] : iv_ruleOpLogicalAnd= ruleOpLogicalAnd EOF ;
    public final String entryRuleOpLogicalAnd() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpLogicalAnd = null;


        try {
            // InternalBasicIQL.g:3848:52: (iv_ruleOpLogicalAnd= ruleOpLogicalAnd EOF )
            // InternalBasicIQL.g:3849:2: iv_ruleOpLogicalAnd= ruleOpLogicalAnd EOF
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
    // InternalBasicIQL.g:3855:1: ruleOpLogicalAnd returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= '&&' ;
    public final AntlrDatatypeRuleToken ruleOpLogicalAnd() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:3861:2: (kw= '&&' )
            // InternalBasicIQL.g:3862:2: kw= '&&'
            {
            kw=(Token)match(input,51,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:3870:1: entryRuleIQLEqualityExpression returns [EObject current=null] : iv_ruleIQLEqualityExpression= ruleIQLEqualityExpression EOF ;
    public final EObject entryRuleIQLEqualityExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLEqualityExpression = null;


        try {
            // InternalBasicIQL.g:3870:62: (iv_ruleIQLEqualityExpression= ruleIQLEqualityExpression EOF )
            // InternalBasicIQL.g:3871:2: iv_ruleIQLEqualityExpression= ruleIQLEqualityExpression EOF
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
    // InternalBasicIQL.g:3877:1: ruleIQLEqualityExpression returns [EObject current=null] : (this_IQLRelationalExpression_0= ruleIQLRelationalExpression ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )* ) ;
    public final EObject ruleIQLEqualityExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLRelationalExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:3883:2: ( (this_IQLRelationalExpression_0= ruleIQLRelationalExpression ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )* ) )
            // InternalBasicIQL.g:3884:2: (this_IQLRelationalExpression_0= ruleIQLRelationalExpression ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )* )
            {
            // InternalBasicIQL.g:3884:2: (this_IQLRelationalExpression_0= ruleIQLRelationalExpression ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )* )
            // InternalBasicIQL.g:3885:3: this_IQLRelationalExpression_0= ruleIQLRelationalExpression ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLEqualityExpressionAccess().getIQLRelationalExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_48);
            this_IQLRelationalExpression_0=ruleIQLRelationalExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLRelationalExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalBasicIQL.g:3893:3: ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )*
            loop53:
            do {
                int alt53=2;
                int LA53_0 = input.LA(1);

                if ( (LA53_0==52) && (synpred5_InternalBasicIQL())) {
                    alt53=1;
                }
                else if ( (LA53_0==53) && (synpred5_InternalBasicIQL())) {
                    alt53=1;
                }


                switch (alt53) {
            	case 1 :
            	    // InternalBasicIQL.g:3894:4: ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) )
            	    {
            	    // InternalBasicIQL.g:3894:4: ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) )
            	    // InternalBasicIQL.g:3895:5: ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) )
            	    {
            	    // InternalBasicIQL.g:3905:5: ( () ( (lv_op_2_0= ruleOpEquality ) ) )
            	    // InternalBasicIQL.g:3906:6: () ( (lv_op_2_0= ruleOpEquality ) )
            	    {
            	    // InternalBasicIQL.g:3906:6: ()
            	    // InternalBasicIQL.g:3907:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    // InternalBasicIQL.g:3913:6: ( (lv_op_2_0= ruleOpEquality ) )
            	    // InternalBasicIQL.g:3914:7: (lv_op_2_0= ruleOpEquality )
            	    {
            	    // InternalBasicIQL.g:3914:7: (lv_op_2_0= ruleOpEquality )
            	    // InternalBasicIQL.g:3915:8: lv_op_2_0= ruleOpEquality
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLEqualityExpressionAccess().getOpOpEqualityParserRuleCall_1_0_0_1_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_32);
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

            	    // InternalBasicIQL.g:3934:4: ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) )
            	    // InternalBasicIQL.g:3935:5: (lv_rightOperand_3_0= ruleIQLRelationalExpression )
            	    {
            	    // InternalBasicIQL.g:3935:5: (lv_rightOperand_3_0= ruleIQLRelationalExpression )
            	    // InternalBasicIQL.g:3936:6: lv_rightOperand_3_0= ruleIQLRelationalExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLEqualityExpressionAccess().getRightOperandIQLRelationalExpressionParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_48);
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
            	      							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLRelationalExpression");
            	      						afterParserOrEnumRuleCall();
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop53;
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
    // InternalBasicIQL.g:3958:1: entryRuleOpEquality returns [String current=null] : iv_ruleOpEquality= ruleOpEquality EOF ;
    public final String entryRuleOpEquality() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpEquality = null;


        try {
            // InternalBasicIQL.g:3958:50: (iv_ruleOpEquality= ruleOpEquality EOF )
            // InternalBasicIQL.g:3959:2: iv_ruleOpEquality= ruleOpEquality EOF
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
    // InternalBasicIQL.g:3965:1: ruleOpEquality returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '==' | kw= '!=' ) ;
    public final AntlrDatatypeRuleToken ruleOpEquality() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:3971:2: ( (kw= '==' | kw= '!=' ) )
            // InternalBasicIQL.g:3972:2: (kw= '==' | kw= '!=' )
            {
            // InternalBasicIQL.g:3972:2: (kw= '==' | kw= '!=' )
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==52) ) {
                alt54=1;
            }
            else if ( (LA54_0==53) ) {
                alt54=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 54, 0, input);

                throw nvae;
            }
            switch (alt54) {
                case 1 :
                    // InternalBasicIQL.g:3973:3: kw= '=='
                    {
                    kw=(Token)match(input,52,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpEqualityAccess().getEqualsSignEqualsSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalBasicIQL.g:3979:3: kw= '!='
                    {
                    kw=(Token)match(input,53,FOLLOW_2); if (state.failed) return current;
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


    // $ANTLR start "entryRuleIQLRelationalExpression"
    // InternalBasicIQL.g:3988:1: entryRuleIQLRelationalExpression returns [EObject current=null] : iv_ruleIQLRelationalExpression= ruleIQLRelationalExpression EOF ;
    public final EObject entryRuleIQLRelationalExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLRelationalExpression = null;


        try {
            // InternalBasicIQL.g:3988:64: (iv_ruleIQLRelationalExpression= ruleIQLRelationalExpression EOF )
            // InternalBasicIQL.g:3989:2: iv_ruleIQLRelationalExpression= ruleIQLRelationalExpression EOF
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
    // InternalBasicIQL.g:3995:1: ruleIQLRelationalExpression returns [EObject current=null] : (this_IQLAdditiveExpression_0= ruleIQLAdditiveExpression ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) ) ) )* ) ;
    public final EObject ruleIQLRelationalExpression() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_IQLAdditiveExpression_0 = null;

        EObject lv_targetRef_3_0 = null;

        AntlrDatatypeRuleToken lv_op_5_0 = null;

        EObject lv_rightOperand_6_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:4001:2: ( (this_IQLAdditiveExpression_0= ruleIQLAdditiveExpression ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) ) ) )* ) )
            // InternalBasicIQL.g:4002:2: (this_IQLAdditiveExpression_0= ruleIQLAdditiveExpression ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) ) ) )* )
            {
            // InternalBasicIQL.g:4002:2: (this_IQLAdditiveExpression_0= ruleIQLAdditiveExpression ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) ) ) )* )
            // InternalBasicIQL.g:4003:3: this_IQLAdditiveExpression_0= ruleIQLAdditiveExpression ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLRelationalExpressionAccess().getIQLAdditiveExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_49);
            this_IQLAdditiveExpression_0=ruleIQLAdditiveExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLAdditiveExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalBasicIQL.g:4011:3: ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) ) ) )*
            loop55:
            do {
                int alt55=3;
                int LA55_0 = input.LA(1);

                if ( (LA55_0==54) && (synpred6_InternalBasicIQL())) {
                    alt55=1;
                }
                else if ( (LA55_0==55) && (synpred7_InternalBasicIQL())) {
                    alt55=2;
                }
                else if ( (LA55_0==56) && (synpred7_InternalBasicIQL())) {
                    alt55=2;
                }
                else if ( (LA55_0==57) && (synpred7_InternalBasicIQL())) {
                    alt55=2;
                }
                else if ( (LA55_0==58) && (synpred7_InternalBasicIQL())) {
                    alt55=2;
                }


                switch (alt55) {
            	case 1 :
            	    // InternalBasicIQL.g:4012:4: ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) )
            	    {
            	    // InternalBasicIQL.g:4012:4: ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) )
            	    // InternalBasicIQL.g:4013:5: ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) )
            	    {
            	    // InternalBasicIQL.g:4013:5: ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) )
            	    // InternalBasicIQL.g:4014:6: ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' )
            	    {
            	    // InternalBasicIQL.g:4020:6: ( () otherlv_2= 'instanceof' )
            	    // InternalBasicIQL.g:4021:7: () otherlv_2= 'instanceof'
            	    {
            	    // InternalBasicIQL.g:4021:7: ()
            	    // InternalBasicIQL.g:4022:8: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      								current = forceCreateModelElementAndSet(
            	      									grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0(),
            	      									current);
            	      							
            	    }

            	    }

            	    otherlv_2=(Token)match(input,54,FOLLOW_3); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      							newLeafNode(otherlv_2, grammarAccess.getIQLRelationalExpressionAccess().getInstanceofKeyword_1_0_0_0_1());
            	      						
            	    }

            	    }


            	    }

            	    // InternalBasicIQL.g:4034:5: ( (lv_targetRef_3_0= ruleJvmTypeReference ) )
            	    // InternalBasicIQL.g:4035:6: (lv_targetRef_3_0= ruleJvmTypeReference )
            	    {
            	    // InternalBasicIQL.g:4035:6: (lv_targetRef_3_0= ruleJvmTypeReference )
            	    // InternalBasicIQL.g:4036:7: lv_targetRef_3_0= ruleJvmTypeReference
            	    {
            	    if ( state.backtracking==0 ) {

            	      							newCompositeNode(grammarAccess.getIQLRelationalExpressionAccess().getTargetRefJvmTypeReferenceParserRuleCall_1_0_1_0());
            	      						
            	    }
            	    pushFollow(FOLLOW_49);
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
            	    // InternalBasicIQL.g:4055:4: ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) ) )
            	    {
            	    // InternalBasicIQL.g:4055:4: ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) ) )
            	    // InternalBasicIQL.g:4056:5: ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) )
            	    {
            	    // InternalBasicIQL.g:4056:5: ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) )
            	    // InternalBasicIQL.g:4057:6: ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) )
            	    {
            	    // InternalBasicIQL.g:4067:6: ( () ( (lv_op_5_0= ruleOpRelational ) ) )
            	    // InternalBasicIQL.g:4068:7: () ( (lv_op_5_0= ruleOpRelational ) )
            	    {
            	    // InternalBasicIQL.g:4068:7: ()
            	    // InternalBasicIQL.g:4069:8: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      								current = forceCreateModelElementAndSet(
            	      									grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0(),
            	      									current);
            	      							
            	    }

            	    }

            	    // InternalBasicIQL.g:4075:7: ( (lv_op_5_0= ruleOpRelational ) )
            	    // InternalBasicIQL.g:4076:8: (lv_op_5_0= ruleOpRelational )
            	    {
            	    // InternalBasicIQL.g:4076:8: (lv_op_5_0= ruleOpRelational )
            	    // InternalBasicIQL.g:4077:9: lv_op_5_0= ruleOpRelational
            	    {
            	    if ( state.backtracking==0 ) {

            	      									newCompositeNode(grammarAccess.getIQLRelationalExpressionAccess().getOpOpRelationalParserRuleCall_1_1_0_0_1_0());
            	      								
            	    }
            	    pushFollow(FOLLOW_32);
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

            	    // InternalBasicIQL.g:4096:5: ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) )
            	    // InternalBasicIQL.g:4097:6: (lv_rightOperand_6_0= ruleIQLAdditiveExpression )
            	    {
            	    // InternalBasicIQL.g:4097:6: (lv_rightOperand_6_0= ruleIQLAdditiveExpression )
            	    // InternalBasicIQL.g:4098:7: lv_rightOperand_6_0= ruleIQLAdditiveExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      							newCompositeNode(grammarAccess.getIQLRelationalExpressionAccess().getRightOperandIQLAdditiveExpressionParserRuleCall_1_1_1_0());
            	      						
            	    }
            	    pushFollow(FOLLOW_49);
            	    lv_rightOperand_6_0=ruleIQLAdditiveExpression();

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
            	      								"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLAdditiveExpression");
            	      							afterParserOrEnumRuleCall();
            	      						
            	    }

            	    }


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
    // $ANTLR end "ruleIQLRelationalExpression"


    // $ANTLR start "entryRuleOpRelational"
    // InternalBasicIQL.g:4121:1: entryRuleOpRelational returns [String current=null] : iv_ruleOpRelational= ruleOpRelational EOF ;
    public final String entryRuleOpRelational() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpRelational = null;


        try {
            // InternalBasicIQL.g:4121:52: (iv_ruleOpRelational= ruleOpRelational EOF )
            // InternalBasicIQL.g:4122:2: iv_ruleOpRelational= ruleOpRelational EOF
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
    // InternalBasicIQL.g:4128:1: ruleOpRelational returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '>' | kw= '>=' | kw= '<' | kw= '<=' ) ;
    public final AntlrDatatypeRuleToken ruleOpRelational() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:4134:2: ( (kw= '>' | kw= '>=' | kw= '<' | kw= '<=' ) )
            // InternalBasicIQL.g:4135:2: (kw= '>' | kw= '>=' | kw= '<' | kw= '<=' )
            {
            // InternalBasicIQL.g:4135:2: (kw= '>' | kw= '>=' | kw= '<' | kw= '<=' )
            int alt56=4;
            switch ( input.LA(1) ) {
            case 55:
                {
                alt56=1;
                }
                break;
            case 56:
                {
                alt56=2;
                }
                break;
            case 57:
                {
                alt56=3;
                }
                break;
            case 58:
                {
                alt56=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;
            }

            switch (alt56) {
                case 1 :
                    // InternalBasicIQL.g:4136:3: kw= '>'
                    {
                    kw=(Token)match(input,55,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpRelationalAccess().getGreaterThanSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalBasicIQL.g:4142:3: kw= '>='
                    {
                    kw=(Token)match(input,56,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpRelationalAccess().getGreaterThanSignEqualsSignKeyword_1());
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalBasicIQL.g:4148:3: kw= '<'
                    {
                    kw=(Token)match(input,57,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpRelationalAccess().getLessThanSignKeyword_2());
                      		
                    }

                    }
                    break;
                case 4 :
                    // InternalBasicIQL.g:4154:3: kw= '<='
                    {
                    kw=(Token)match(input,58,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:4163:1: entryRuleIQLAdditiveExpression returns [EObject current=null] : iv_ruleIQLAdditiveExpression= ruleIQLAdditiveExpression EOF ;
    public final EObject entryRuleIQLAdditiveExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLAdditiveExpression = null;


        try {
            // InternalBasicIQL.g:4163:62: (iv_ruleIQLAdditiveExpression= ruleIQLAdditiveExpression EOF )
            // InternalBasicIQL.g:4164:2: iv_ruleIQLAdditiveExpression= ruleIQLAdditiveExpression EOF
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
    // InternalBasicIQL.g:4170:1: ruleIQLAdditiveExpression returns [EObject current=null] : (this_IQLMultiplicativeExpression_0= ruleIQLMultiplicativeExpression ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )* ) ;
    public final EObject ruleIQLAdditiveExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLMultiplicativeExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:4176:2: ( (this_IQLMultiplicativeExpression_0= ruleIQLMultiplicativeExpression ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )* ) )
            // InternalBasicIQL.g:4177:2: (this_IQLMultiplicativeExpression_0= ruleIQLMultiplicativeExpression ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )* )
            {
            // InternalBasicIQL.g:4177:2: (this_IQLMultiplicativeExpression_0= ruleIQLMultiplicativeExpression ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )* )
            // InternalBasicIQL.g:4178:3: this_IQLMultiplicativeExpression_0= ruleIQLMultiplicativeExpression ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLAdditiveExpressionAccess().getIQLMultiplicativeExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_50);
            this_IQLMultiplicativeExpression_0=ruleIQLMultiplicativeExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLMultiplicativeExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalBasicIQL.g:4186:3: ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )*
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( (LA57_0==59) && (synpred8_InternalBasicIQL())) {
                    alt57=1;
                }
                else if ( (LA57_0==60) && (synpred8_InternalBasicIQL())) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // InternalBasicIQL.g:4187:4: ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) )
            	    {
            	    // InternalBasicIQL.g:4187:4: ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) )
            	    // InternalBasicIQL.g:4188:5: ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) )
            	    {
            	    // InternalBasicIQL.g:4198:5: ( () ( (lv_op_2_0= ruleOpAdd ) ) )
            	    // InternalBasicIQL.g:4199:6: () ( (lv_op_2_0= ruleOpAdd ) )
            	    {
            	    // InternalBasicIQL.g:4199:6: ()
            	    // InternalBasicIQL.g:4200:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    // InternalBasicIQL.g:4206:6: ( (lv_op_2_0= ruleOpAdd ) )
            	    // InternalBasicIQL.g:4207:7: (lv_op_2_0= ruleOpAdd )
            	    {
            	    // InternalBasicIQL.g:4207:7: (lv_op_2_0= ruleOpAdd )
            	    // InternalBasicIQL.g:4208:8: lv_op_2_0= ruleOpAdd
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLAdditiveExpressionAccess().getOpOpAddParserRuleCall_1_0_0_1_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_32);
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

            	    // InternalBasicIQL.g:4227:4: ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) )
            	    // InternalBasicIQL.g:4228:5: (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression )
            	    {
            	    // InternalBasicIQL.g:4228:5: (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression )
            	    // InternalBasicIQL.g:4229:6: lv_rightOperand_3_0= ruleIQLMultiplicativeExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLAdditiveExpressionAccess().getRightOperandIQLMultiplicativeExpressionParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_50);
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
            	    break loop57;
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
    // InternalBasicIQL.g:4251:1: entryRuleOpAdd returns [String current=null] : iv_ruleOpAdd= ruleOpAdd EOF ;
    public final String entryRuleOpAdd() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpAdd = null;


        try {
            // InternalBasicIQL.g:4251:45: (iv_ruleOpAdd= ruleOpAdd EOF )
            // InternalBasicIQL.g:4252:2: iv_ruleOpAdd= ruleOpAdd EOF
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
    // InternalBasicIQL.g:4258:1: ruleOpAdd returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '+' | kw= '-' ) ;
    public final AntlrDatatypeRuleToken ruleOpAdd() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:4264:2: ( (kw= '+' | kw= '-' ) )
            // InternalBasicIQL.g:4265:2: (kw= '+' | kw= '-' )
            {
            // InternalBasicIQL.g:4265:2: (kw= '+' | kw= '-' )
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==59) ) {
                alt58=1;
            }
            else if ( (LA58_0==60) ) {
                alt58=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 58, 0, input);

                throw nvae;
            }
            switch (alt58) {
                case 1 :
                    // InternalBasicIQL.g:4266:3: kw= '+'
                    {
                    kw=(Token)match(input,59,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAddAccess().getPlusSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalBasicIQL.g:4272:3: kw= '-'
                    {
                    kw=(Token)match(input,60,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:4281:1: entryRuleIQLMultiplicativeExpression returns [EObject current=null] : iv_ruleIQLMultiplicativeExpression= ruleIQLMultiplicativeExpression EOF ;
    public final EObject entryRuleIQLMultiplicativeExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMultiplicativeExpression = null;


        try {
            // InternalBasicIQL.g:4281:68: (iv_ruleIQLMultiplicativeExpression= ruleIQLMultiplicativeExpression EOF )
            // InternalBasicIQL.g:4282:2: iv_ruleIQLMultiplicativeExpression= ruleIQLMultiplicativeExpression EOF
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
    // InternalBasicIQL.g:4288:1: ruleIQLMultiplicativeExpression returns [EObject current=null] : (this_IQLUnaryExpression_0= ruleIQLUnaryExpression ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )* ) ;
    public final EObject ruleIQLMultiplicativeExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLUnaryExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:4294:2: ( (this_IQLUnaryExpression_0= ruleIQLUnaryExpression ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )* ) )
            // InternalBasicIQL.g:4295:2: (this_IQLUnaryExpression_0= ruleIQLUnaryExpression ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )* )
            {
            // InternalBasicIQL.g:4295:2: (this_IQLUnaryExpression_0= ruleIQLUnaryExpression ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )* )
            // InternalBasicIQL.g:4296:3: this_IQLUnaryExpression_0= ruleIQLUnaryExpression ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLUnaryExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_51);
            this_IQLUnaryExpression_0=ruleIQLUnaryExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLUnaryExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalBasicIQL.g:4304:3: ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( (LA59_0==61) && (synpred9_InternalBasicIQL())) {
                    alt59=1;
                }
                else if ( (LA59_0==62) && (synpred9_InternalBasicIQL())) {
                    alt59=1;
                }
                else if ( (LA59_0==63) && (synpred9_InternalBasicIQL())) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // InternalBasicIQL.g:4305:4: ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) )
            	    {
            	    // InternalBasicIQL.g:4305:4: ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) )
            	    // InternalBasicIQL.g:4306:5: ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) )
            	    {
            	    // InternalBasicIQL.g:4316:5: ( () ( (lv_op_2_0= ruleOpMulti ) ) )
            	    // InternalBasicIQL.g:4317:6: () ( (lv_op_2_0= ruleOpMulti ) )
            	    {
            	    // InternalBasicIQL.g:4317:6: ()
            	    // InternalBasicIQL.g:4318:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    // InternalBasicIQL.g:4324:6: ( (lv_op_2_0= ruleOpMulti ) )
            	    // InternalBasicIQL.g:4325:7: (lv_op_2_0= ruleOpMulti )
            	    {
            	    // InternalBasicIQL.g:4325:7: (lv_op_2_0= ruleOpMulti )
            	    // InternalBasicIQL.g:4326:8: lv_op_2_0= ruleOpMulti
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLMultiplicativeExpressionAccess().getOpOpMultiParserRuleCall_1_0_0_1_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_32);
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

            	    // InternalBasicIQL.g:4345:4: ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) )
            	    // InternalBasicIQL.g:4346:5: (lv_rightOperand_3_0= ruleIQLUnaryExpression )
            	    {
            	    // InternalBasicIQL.g:4346:5: (lv_rightOperand_3_0= ruleIQLUnaryExpression )
            	    // InternalBasicIQL.g:4347:6: lv_rightOperand_3_0= ruleIQLUnaryExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLMultiplicativeExpressionAccess().getRightOperandIQLUnaryExpressionParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_51);
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
            	    break loop59;
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
    // InternalBasicIQL.g:4369:1: entryRuleOpMulti returns [String current=null] : iv_ruleOpMulti= ruleOpMulti EOF ;
    public final String entryRuleOpMulti() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpMulti = null;


        try {
            // InternalBasicIQL.g:4369:47: (iv_ruleOpMulti= ruleOpMulti EOF )
            // InternalBasicIQL.g:4370:2: iv_ruleOpMulti= ruleOpMulti EOF
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
    // InternalBasicIQL.g:4376:1: ruleOpMulti returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '*' | kw= '/' | kw= '%' ) ;
    public final AntlrDatatypeRuleToken ruleOpMulti() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:4382:2: ( (kw= '*' | kw= '/' | kw= '%' ) )
            // InternalBasicIQL.g:4383:2: (kw= '*' | kw= '/' | kw= '%' )
            {
            // InternalBasicIQL.g:4383:2: (kw= '*' | kw= '/' | kw= '%' )
            int alt60=3;
            switch ( input.LA(1) ) {
            case 61:
                {
                alt60=1;
                }
                break;
            case 62:
                {
                alt60=2;
                }
                break;
            case 63:
                {
                alt60=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 60, 0, input);

                throw nvae;
            }

            switch (alt60) {
                case 1 :
                    // InternalBasicIQL.g:4384:3: kw= '*'
                    {
                    kw=(Token)match(input,61,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpMultiAccess().getAsteriskKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalBasicIQL.g:4390:3: kw= '/'
                    {
                    kw=(Token)match(input,62,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpMultiAccess().getSolidusKeyword_1());
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalBasicIQL.g:4396:3: kw= '%'
                    {
                    kw=(Token)match(input,63,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:4405:1: entryRuleIQLUnaryExpression returns [EObject current=null] : iv_ruleIQLUnaryExpression= ruleIQLUnaryExpression EOF ;
    public final EObject entryRuleIQLUnaryExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLUnaryExpression = null;


        try {
            // InternalBasicIQL.g:4405:59: (iv_ruleIQLUnaryExpression= ruleIQLUnaryExpression EOF )
            // InternalBasicIQL.g:4406:2: iv_ruleIQLUnaryExpression= ruleIQLUnaryExpression EOF
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
    // InternalBasicIQL.g:4412:1: ruleIQLUnaryExpression returns [EObject current=null] : ( ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) ) | ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) ) | (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? ) ) ;
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
            // InternalBasicIQL.g:4418:2: ( ( ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) ) | ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) ) | (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? ) ) )
            // InternalBasicIQL.g:4419:2: ( ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) ) | ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) ) | (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? ) )
            {
            // InternalBasicIQL.g:4419:2: ( ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) ) | ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) ) | (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? ) )
            int alt62=5;
            alt62 = dfa62.predict(input);
            switch (alt62) {
                case 1 :
                    // InternalBasicIQL.g:4420:3: ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) )
                    {
                    // InternalBasicIQL.g:4420:3: ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) )
                    // InternalBasicIQL.g:4421:4: ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) )
                    {
                    // InternalBasicIQL.g:4421:4: ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) )
                    // InternalBasicIQL.g:4422:5: () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) )
                    {
                    // InternalBasicIQL.g:4422:5: ()
                    // InternalBasicIQL.g:4423:6: 
                    {
                    if ( state.backtracking==0 ) {

                      						current = forceCreateModelElement(
                      							grammarAccess.getIQLUnaryExpressionAccess().getIQLPlusMinusExpressionAction_0_0_0(),
                      							current);
                      					
                    }

                    }

                    // InternalBasicIQL.g:4429:5: ( (lv_op_1_0= ruleOpUnaryPlusMinus ) )
                    // InternalBasicIQL.g:4430:6: (lv_op_1_0= ruleOpUnaryPlusMinus )
                    {
                    // InternalBasicIQL.g:4430:6: (lv_op_1_0= ruleOpUnaryPlusMinus )
                    // InternalBasicIQL.g:4431:7: lv_op_1_0= ruleOpUnaryPlusMinus
                    {
                    if ( state.backtracking==0 ) {

                      							newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getOpOpUnaryPlusMinusParserRuleCall_0_0_1_0());
                      						
                    }
                    pushFollow(FOLLOW_32);
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

                    // InternalBasicIQL.g:4449:4: ( (lv_operand_2_0= ruleIQLMemberCallExpression ) )
                    // InternalBasicIQL.g:4450:5: (lv_operand_2_0= ruleIQLMemberCallExpression )
                    {
                    // InternalBasicIQL.g:4450:5: (lv_operand_2_0= ruleIQLMemberCallExpression )
                    // InternalBasicIQL.g:4451:6: lv_operand_2_0= ruleIQLMemberCallExpression
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
                    // InternalBasicIQL.g:4470:3: ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) )
                    {
                    // InternalBasicIQL.g:4470:3: ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) )
                    // InternalBasicIQL.g:4471:4: ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) )
                    {
                    // InternalBasicIQL.g:4471:4: ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) )
                    // InternalBasicIQL.g:4472:5: () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) )
                    {
                    // InternalBasicIQL.g:4472:5: ()
                    // InternalBasicIQL.g:4473:6: 
                    {
                    if ( state.backtracking==0 ) {

                      						current = forceCreateModelElement(
                      							grammarAccess.getIQLUnaryExpressionAccess().getIQLBooleanNotExpressionAction_1_0_0(),
                      							current);
                      					
                    }

                    }

                    // InternalBasicIQL.g:4479:5: ( (lv_op_4_0= ruleOpUnaryBooleanNot ) )
                    // InternalBasicIQL.g:4480:6: (lv_op_4_0= ruleOpUnaryBooleanNot )
                    {
                    // InternalBasicIQL.g:4480:6: (lv_op_4_0= ruleOpUnaryBooleanNot )
                    // InternalBasicIQL.g:4481:7: lv_op_4_0= ruleOpUnaryBooleanNot
                    {
                    if ( state.backtracking==0 ) {

                      							newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getOpOpUnaryBooleanNotParserRuleCall_1_0_1_0());
                      						
                    }
                    pushFollow(FOLLOW_32);
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

                    // InternalBasicIQL.g:4499:4: ( (lv_operand_5_0= ruleIQLMemberCallExpression ) )
                    // InternalBasicIQL.g:4500:5: (lv_operand_5_0= ruleIQLMemberCallExpression )
                    {
                    // InternalBasicIQL.g:4500:5: (lv_operand_5_0= ruleIQLMemberCallExpression )
                    // InternalBasicIQL.g:4501:6: lv_operand_5_0= ruleIQLMemberCallExpression
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
                    // InternalBasicIQL.g:4520:3: ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) )
                    {
                    // InternalBasicIQL.g:4520:3: ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) )
                    // InternalBasicIQL.g:4521:4: ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) )
                    {
                    // InternalBasicIQL.g:4521:4: ( () ( (lv_op_7_0= ruleOpPrefix ) ) )
                    // InternalBasicIQL.g:4522:5: () ( (lv_op_7_0= ruleOpPrefix ) )
                    {
                    // InternalBasicIQL.g:4522:5: ()
                    // InternalBasicIQL.g:4523:6: 
                    {
                    if ( state.backtracking==0 ) {

                      						current = forceCreateModelElement(
                      							grammarAccess.getIQLUnaryExpressionAccess().getIQLPrefixExpressionAction_2_0_0(),
                      							current);
                      					
                    }

                    }

                    // InternalBasicIQL.g:4529:5: ( (lv_op_7_0= ruleOpPrefix ) )
                    // InternalBasicIQL.g:4530:6: (lv_op_7_0= ruleOpPrefix )
                    {
                    // InternalBasicIQL.g:4530:6: (lv_op_7_0= ruleOpPrefix )
                    // InternalBasicIQL.g:4531:7: lv_op_7_0= ruleOpPrefix
                    {
                    if ( state.backtracking==0 ) {

                      							newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getOpOpPrefixParserRuleCall_2_0_1_0());
                      						
                    }
                    pushFollow(FOLLOW_32);
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

                    // InternalBasicIQL.g:4549:4: ( (lv_operand_8_0= ruleIQLMemberCallExpression ) )
                    // InternalBasicIQL.g:4550:5: (lv_operand_8_0= ruleIQLMemberCallExpression )
                    {
                    // InternalBasicIQL.g:4550:5: (lv_operand_8_0= ruleIQLMemberCallExpression )
                    // InternalBasicIQL.g:4551:6: lv_operand_8_0= ruleIQLMemberCallExpression
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
                    // InternalBasicIQL.g:4570:3: ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) )
                    {
                    // InternalBasicIQL.g:4570:3: ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) )
                    // InternalBasicIQL.g:4571:4: ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) )
                    {
                    // InternalBasicIQL.g:4571:4: ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) )
                    // InternalBasicIQL.g:4572:5: ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' )
                    {
                    // InternalBasicIQL.g:4584:5: ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' )
                    // InternalBasicIQL.g:4585:6: () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')'
                    {
                    // InternalBasicIQL.g:4585:6: ()
                    // InternalBasicIQL.g:4586:7: 
                    {
                    if ( state.backtracking==0 ) {

                      							current = forceCreateModelElement(
                      								grammarAccess.getIQLUnaryExpressionAccess().getIQLTypeCastExpressionAction_3_0_0_0(),
                      								current);
                      						
                    }

                    }

                    otherlv_10=(Token)match(input,27,FOLLOW_3); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(otherlv_10, grammarAccess.getIQLUnaryExpressionAccess().getLeftParenthesisKeyword_3_0_0_1());
                      					
                    }
                    // InternalBasicIQL.g:4596:6: ( (lv_targetRef_11_0= ruleJvmTypeReference ) )
                    // InternalBasicIQL.g:4597:7: (lv_targetRef_11_0= ruleJvmTypeReference )
                    {
                    // InternalBasicIQL.g:4597:7: (lv_targetRef_11_0= ruleJvmTypeReference )
                    // InternalBasicIQL.g:4598:8: lv_targetRef_11_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      								newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getTargetRefJvmTypeReferenceParserRuleCall_3_0_0_2_0());
                      							
                    }
                    pushFollow(FOLLOW_36);
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

                    otherlv_12=(Token)match(input,28,FOLLOW_32); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(otherlv_12, grammarAccess.getIQLUnaryExpressionAccess().getRightParenthesisKeyword_3_0_0_3());
                      					
                    }

                    }


                    }

                    // InternalBasicIQL.g:4621:4: ( (lv_operand_13_0= ruleIQLMemberCallExpression ) )
                    // InternalBasicIQL.g:4622:5: (lv_operand_13_0= ruleIQLMemberCallExpression )
                    {
                    // InternalBasicIQL.g:4622:5: (lv_operand_13_0= ruleIQLMemberCallExpression )
                    // InternalBasicIQL.g:4623:6: lv_operand_13_0= ruleIQLMemberCallExpression
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
                    // InternalBasicIQL.g:4642:3: (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? )
                    {
                    // InternalBasicIQL.g:4642:3: (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? )
                    // InternalBasicIQL.g:4643:4: this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )?
                    {
                    if ( state.backtracking==0 ) {

                      				newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getIQLMemberCallExpressionParserRuleCall_4_0());
                      			
                    }
                    pushFollow(FOLLOW_52);
                    this_IQLMemberCallExpression_14=ruleIQLMemberCallExpression();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = this_IQLMemberCallExpression_14;
                      				afterParserOrEnumRuleCall();
                      			
                    }
                    // InternalBasicIQL.g:4651:4: ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )?
                    int alt61=2;
                    int LA61_0 = input.LA(1);

                    if ( (LA61_0==65) && (synpred11_InternalBasicIQL())) {
                        alt61=1;
                    }
                    else if ( (LA61_0==66) && (synpred11_InternalBasicIQL())) {
                        alt61=1;
                    }
                    switch (alt61) {
                        case 1 :
                            // InternalBasicIQL.g:4652:5: ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) )
                            {
                            // InternalBasicIQL.g:4662:5: ( () ( (lv_op_16_0= ruleOpPostfix ) ) )
                            // InternalBasicIQL.g:4663:6: () ( (lv_op_16_0= ruleOpPostfix ) )
                            {
                            // InternalBasicIQL.g:4663:6: ()
                            // InternalBasicIQL.g:4664:7: 
                            {
                            if ( state.backtracking==0 ) {

                              							current = forceCreateModelElementAndSet(
                              								grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0(),
                              								current);
                              						
                            }

                            }

                            // InternalBasicIQL.g:4670:6: ( (lv_op_16_0= ruleOpPostfix ) )
                            // InternalBasicIQL.g:4671:7: (lv_op_16_0= ruleOpPostfix )
                            {
                            // InternalBasicIQL.g:4671:7: (lv_op_16_0= ruleOpPostfix )
                            // InternalBasicIQL.g:4672:8: lv_op_16_0= ruleOpPostfix
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
    // InternalBasicIQL.g:4696:1: entryRuleOpUnaryPlusMinus returns [String current=null] : iv_ruleOpUnaryPlusMinus= ruleOpUnaryPlusMinus EOF ;
    public final String entryRuleOpUnaryPlusMinus() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpUnaryPlusMinus = null;


        try {
            // InternalBasicIQL.g:4696:56: (iv_ruleOpUnaryPlusMinus= ruleOpUnaryPlusMinus EOF )
            // InternalBasicIQL.g:4697:2: iv_ruleOpUnaryPlusMinus= ruleOpUnaryPlusMinus EOF
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
    // InternalBasicIQL.g:4703:1: ruleOpUnaryPlusMinus returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '-' | kw= '+' ) ;
    public final AntlrDatatypeRuleToken ruleOpUnaryPlusMinus() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:4709:2: ( (kw= '-' | kw= '+' ) )
            // InternalBasicIQL.g:4710:2: (kw= '-' | kw= '+' )
            {
            // InternalBasicIQL.g:4710:2: (kw= '-' | kw= '+' )
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==60) ) {
                alt63=1;
            }
            else if ( (LA63_0==59) ) {
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
                    // InternalBasicIQL.g:4711:3: kw= '-'
                    {
                    kw=(Token)match(input,60,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpUnaryPlusMinusAccess().getHyphenMinusKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalBasicIQL.g:4717:3: kw= '+'
                    {
                    kw=(Token)match(input,59,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:4726:1: entryRuleOpUnaryBooleanNot returns [String current=null] : iv_ruleOpUnaryBooleanNot= ruleOpUnaryBooleanNot EOF ;
    public final String entryRuleOpUnaryBooleanNot() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpUnaryBooleanNot = null;


        try {
            // InternalBasicIQL.g:4726:57: (iv_ruleOpUnaryBooleanNot= ruleOpUnaryBooleanNot EOF )
            // InternalBasicIQL.g:4727:2: iv_ruleOpUnaryBooleanNot= ruleOpUnaryBooleanNot EOF
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
    // InternalBasicIQL.g:4733:1: ruleOpUnaryBooleanNot returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= '!' ;
    public final AntlrDatatypeRuleToken ruleOpUnaryBooleanNot() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:4739:2: (kw= '!' )
            // InternalBasicIQL.g:4740:2: kw= '!'
            {
            kw=(Token)match(input,64,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:4748:1: entryRuleOpPrefix returns [String current=null] : iv_ruleOpPrefix= ruleOpPrefix EOF ;
    public final String entryRuleOpPrefix() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpPrefix = null;


        try {
            // InternalBasicIQL.g:4748:48: (iv_ruleOpPrefix= ruleOpPrefix EOF )
            // InternalBasicIQL.g:4749:2: iv_ruleOpPrefix= ruleOpPrefix EOF
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
    // InternalBasicIQL.g:4755:1: ruleOpPrefix returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '++' | kw= '--' ) ;
    public final AntlrDatatypeRuleToken ruleOpPrefix() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:4761:2: ( (kw= '++' | kw= '--' ) )
            // InternalBasicIQL.g:4762:2: (kw= '++' | kw= '--' )
            {
            // InternalBasicIQL.g:4762:2: (kw= '++' | kw= '--' )
            int alt64=2;
            int LA64_0 = input.LA(1);

            if ( (LA64_0==65) ) {
                alt64=1;
            }
            else if ( (LA64_0==66) ) {
                alt64=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 64, 0, input);

                throw nvae;
            }
            switch (alt64) {
                case 1 :
                    // InternalBasicIQL.g:4763:3: kw= '++'
                    {
                    kw=(Token)match(input,65,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpPrefixAccess().getPlusSignPlusSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalBasicIQL.g:4769:3: kw= '--'
                    {
                    kw=(Token)match(input,66,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:4778:1: entryRuleOpPostfix returns [String current=null] : iv_ruleOpPostfix= ruleOpPostfix EOF ;
    public final String entryRuleOpPostfix() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpPostfix = null;


        try {
            // InternalBasicIQL.g:4778:49: (iv_ruleOpPostfix= ruleOpPostfix EOF )
            // InternalBasicIQL.g:4779:2: iv_ruleOpPostfix= ruleOpPostfix EOF
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
    // InternalBasicIQL.g:4785:1: ruleOpPostfix returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '++' | kw= '--' ) ;
    public final AntlrDatatypeRuleToken ruleOpPostfix() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:4791:2: ( (kw= '++' | kw= '--' ) )
            // InternalBasicIQL.g:4792:2: (kw= '++' | kw= '--' )
            {
            // InternalBasicIQL.g:4792:2: (kw= '++' | kw= '--' )
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( (LA65_0==65) ) {
                alt65=1;
            }
            else if ( (LA65_0==66) ) {
                alt65=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 65, 0, input);

                throw nvae;
            }
            switch (alt65) {
                case 1 :
                    // InternalBasicIQL.g:4793:3: kw= '++'
                    {
                    kw=(Token)match(input,65,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpPostfixAccess().getPlusSignPlusSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalBasicIQL.g:4799:3: kw= '--'
                    {
                    kw=(Token)match(input,66,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:4808:1: entryRuleIQLMemberCallExpression returns [EObject current=null] : iv_ruleIQLMemberCallExpression= ruleIQLMemberCallExpression EOF ;
    public final EObject entryRuleIQLMemberCallExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMemberCallExpression = null;


        try {
            // InternalBasicIQL.g:4808:64: (iv_ruleIQLMemberCallExpression= ruleIQLMemberCallExpression EOF )
            // InternalBasicIQL.g:4809:2: iv_ruleIQLMemberCallExpression= ruleIQLMemberCallExpression EOF
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
    // InternalBasicIQL.g:4815:1: ruleIQLMemberCallExpression returns [EObject current=null] : (this_IQLOtherExpressions_0= ruleIQLOtherExpressions ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )* ) ;
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
            // InternalBasicIQL.g:4821:2: ( (this_IQLOtherExpressions_0= ruleIQLOtherExpressions ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )* ) )
            // InternalBasicIQL.g:4822:2: (this_IQLOtherExpressions_0= ruleIQLOtherExpressions ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )* )
            {
            // InternalBasicIQL.g:4822:2: (this_IQLOtherExpressions_0= ruleIQLOtherExpressions ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )* )
            // InternalBasicIQL.g:4823:3: this_IQLOtherExpressions_0= ruleIQLOtherExpressions ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLMemberCallExpressionAccess().getIQLOtherExpressionsParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_53);
            this_IQLOtherExpressions_0=ruleIQLOtherExpressions();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLOtherExpressions_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalBasicIQL.g:4831:3: ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )*
            loop67:
            do {
                int alt67=3;
                int LA67_0 = input.LA(1);

                if ( (LA67_0==24) && (synpred12_InternalBasicIQL())) {
                    alt67=1;
                }
                else if ( (LA67_0==67) && (synpred13_InternalBasicIQL())) {
                    alt67=2;
                }


                switch (alt67) {
            	case 1 :
            	    // InternalBasicIQL.g:4832:4: ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) )
            	    {
            	    // InternalBasicIQL.g:4832:4: ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) )
            	    // InternalBasicIQL.g:4833:5: ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' )
            	    {
            	    // InternalBasicIQL.g:4853:5: ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' )
            	    // InternalBasicIQL.g:4854:6: () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']'
            	    {
            	    // InternalBasicIQL.g:4854:6: ()
            	    // InternalBasicIQL.g:4855:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    otherlv_2=(Token)match(input,24,FOLLOW_32); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						newLeafNode(otherlv_2, grammarAccess.getIQLMemberCallExpressionAccess().getLeftSquareBracketKeyword_1_0_0_1());
            	      					
            	    }
            	    // InternalBasicIQL.g:4865:6: ( (lv_expressions_3_0= ruleIQLExpression ) )
            	    // InternalBasicIQL.g:4866:7: (lv_expressions_3_0= ruleIQLExpression )
            	    {
            	    // InternalBasicIQL.g:4866:7: (lv_expressions_3_0= ruleIQLExpression )
            	    // InternalBasicIQL.g:4867:8: lv_expressions_3_0= ruleIQLExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLMemberCallExpressionAccess().getExpressionsIQLExpressionParserRuleCall_1_0_0_2_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_27);
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

            	    // InternalBasicIQL.g:4884:6: (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )*
            	    loop66:
            	    do {
            	        int alt66=2;
            	        int LA66_0 = input.LA(1);

            	        if ( (LA66_0==20) ) {
            	            alt66=1;
            	        }


            	        switch (alt66) {
            	    	case 1 :
            	    	    // InternalBasicIQL.g:4885:7: otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) )
            	    	    {
            	    	    otherlv_4=(Token)match(input,20,FOLLOW_32); if (state.failed) return current;
            	    	    if ( state.backtracking==0 ) {

            	    	      							newLeafNode(otherlv_4, grammarAccess.getIQLMemberCallExpressionAccess().getCommaKeyword_1_0_0_3_0());
            	    	      						
            	    	    }
            	    	    // InternalBasicIQL.g:4889:7: ( (lv_expressions_5_0= ruleIQLExpression ) )
            	    	    // InternalBasicIQL.g:4890:8: (lv_expressions_5_0= ruleIQLExpression )
            	    	    {
            	    	    // InternalBasicIQL.g:4890:8: (lv_expressions_5_0= ruleIQLExpression )
            	    	    // InternalBasicIQL.g:4891:9: lv_expressions_5_0= ruleIQLExpression
            	    	    {
            	    	    if ( state.backtracking==0 ) {

            	    	      									newCompositeNode(grammarAccess.getIQLMemberCallExpressionAccess().getExpressionsIQLExpressionParserRuleCall_1_0_0_3_1_0());
            	    	      								
            	    	    }
            	    	    pushFollow(FOLLOW_27);
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
            	    	    break loop66;
            	        }
            	    } while (true);

            	    otherlv_6=(Token)match(input,25,FOLLOW_53); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						newLeafNode(otherlv_6, grammarAccess.getIQLMemberCallExpressionAccess().getRightSquareBracketKeyword_1_0_0_4());
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;
            	case 2 :
            	    // InternalBasicIQL.g:4916:4: ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) )
            	    {
            	    // InternalBasicIQL.g:4916:4: ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) )
            	    // InternalBasicIQL.g:4917:5: ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) )
            	    {
            	    // InternalBasicIQL.g:4917:5: ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) )
            	    // InternalBasicIQL.g:4918:6: ( ( () '.' ) )=> ( () otherlv_8= '.' )
            	    {
            	    // InternalBasicIQL.g:4924:6: ( () otherlv_8= '.' )
            	    // InternalBasicIQL.g:4925:7: () otherlv_8= '.'
            	    {
            	    // InternalBasicIQL.g:4925:7: ()
            	    // InternalBasicIQL.g:4926:8: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      								current = forceCreateModelElementAndSet(
            	      									grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0(),
            	      									current);
            	      							
            	    }

            	    }

            	    otherlv_8=(Token)match(input,67,FOLLOW_3); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      							newLeafNode(otherlv_8, grammarAccess.getIQLMemberCallExpressionAccess().getFullStopKeyword_1_1_0_0_1());
            	      						
            	    }

            	    }


            	    }

            	    // InternalBasicIQL.g:4938:5: ( (lv_sel_9_0= ruleIQLMemberSelection ) )
            	    // InternalBasicIQL.g:4939:6: (lv_sel_9_0= ruleIQLMemberSelection )
            	    {
            	    // InternalBasicIQL.g:4939:6: (lv_sel_9_0= ruleIQLMemberSelection )
            	    // InternalBasicIQL.g:4940:7: lv_sel_9_0= ruleIQLMemberSelection
            	    {
            	    if ( state.backtracking==0 ) {

            	      							newCompositeNode(grammarAccess.getIQLMemberCallExpressionAccess().getSelIQLMemberSelectionParserRuleCall_1_1_1_0());
            	      						
            	    }
            	    pushFollow(FOLLOW_53);
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
    // $ANTLR end "ruleIQLMemberCallExpression"


    // $ANTLR start "entryRuleIQLMemberSelection"
    // InternalBasicIQL.g:4963:1: entryRuleIQLMemberSelection returns [EObject current=null] : iv_ruleIQLMemberSelection= ruleIQLMemberSelection EOF ;
    public final EObject entryRuleIQLMemberSelection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMemberSelection = null;


        try {
            // InternalBasicIQL.g:4963:59: (iv_ruleIQLMemberSelection= ruleIQLMemberSelection EOF )
            // InternalBasicIQL.g:4964:2: iv_ruleIQLMemberSelection= ruleIQLMemberSelection EOF
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
    // InternalBasicIQL.g:4970:1: ruleIQLMemberSelection returns [EObject current=null] : ( ( (otherlv_0= RULE_ID ) ) ( (lv_args_1_0= ruleIQLArgumentsList ) )? ) ;
    public final EObject ruleIQLMemberSelection() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        EObject lv_args_1_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:4976:2: ( ( ( (otherlv_0= RULE_ID ) ) ( (lv_args_1_0= ruleIQLArgumentsList ) )? ) )
            // InternalBasicIQL.g:4977:2: ( ( (otherlv_0= RULE_ID ) ) ( (lv_args_1_0= ruleIQLArgumentsList ) )? )
            {
            // InternalBasicIQL.g:4977:2: ( ( (otherlv_0= RULE_ID ) ) ( (lv_args_1_0= ruleIQLArgumentsList ) )? )
            // InternalBasicIQL.g:4978:3: ( (otherlv_0= RULE_ID ) ) ( (lv_args_1_0= ruleIQLArgumentsList ) )?
            {
            // InternalBasicIQL.g:4978:3: ( (otherlv_0= RULE_ID ) )
            // InternalBasicIQL.g:4979:4: (otherlv_0= RULE_ID )
            {
            // InternalBasicIQL.g:4979:4: (otherlv_0= RULE_ID )
            // InternalBasicIQL.g:4980:5: otherlv_0= RULE_ID
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLMemberSelectionRule());
              					}
              				
            }
            otherlv_0=(Token)match(input,RULE_ID,FOLLOW_54); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(otherlv_0, grammarAccess.getIQLMemberSelectionAccess().getMemberJvmMemberCrossReference_0_0());
              				
            }

            }


            }

            // InternalBasicIQL.g:4991:3: ( (lv_args_1_0= ruleIQLArgumentsList ) )?
            int alt68=2;
            int LA68_0 = input.LA(1);

            if ( (LA68_0==27) ) {
                alt68=1;
            }
            switch (alt68) {
                case 1 :
                    // InternalBasicIQL.g:4992:4: (lv_args_1_0= ruleIQLArgumentsList )
                    {
                    // InternalBasicIQL.g:4992:4: (lv_args_1_0= ruleIQLArgumentsList )
                    // InternalBasicIQL.g:4993:5: lv_args_1_0= ruleIQLArgumentsList
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
    // InternalBasicIQL.g:5014:1: entryRuleIQLOtherExpressions returns [EObject current=null] : iv_ruleIQLOtherExpressions= ruleIQLOtherExpressions EOF ;
    public final EObject entryRuleIQLOtherExpressions() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLOtherExpressions = null;


        try {
            // InternalBasicIQL.g:5014:60: (iv_ruleIQLOtherExpressions= ruleIQLOtherExpressions EOF )
            // InternalBasicIQL.g:5015:2: iv_ruleIQLOtherExpressions= ruleIQLOtherExpressions EOF
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
    // InternalBasicIQL.g:5021:1: ruleIQLOtherExpressions returns [EObject current=null] : ( ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? ) | ( () otherlv_4= 'this' ) | ( () otherlv_6= 'super' ) | ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ) | ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) ) | this_IQLLiteralExpression_18= ruleIQLLiteralExpression ) ;
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
            // InternalBasicIQL.g:5027:2: ( ( ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? ) | ( () otherlv_4= 'this' ) | ( () otherlv_6= 'super' ) | ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ) | ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) ) | this_IQLLiteralExpression_18= ruleIQLLiteralExpression ) )
            // InternalBasicIQL.g:5028:2: ( ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? ) | ( () otherlv_4= 'this' ) | ( () otherlv_6= 'super' ) | ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ) | ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) ) | this_IQLLiteralExpression_18= ruleIQLLiteralExpression )
            {
            // InternalBasicIQL.g:5028:2: ( ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? ) | ( () otherlv_4= 'this' ) | ( () otherlv_6= 'super' ) | ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ) | ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) ) | this_IQLLiteralExpression_18= ruleIQLLiteralExpression )
            int alt73=6;
            switch ( input.LA(1) ) {
            case RULE_ID:
                {
                alt73=1;
                }
                break;
            case 40:
                {
                alt73=2;
                }
                break;
            case 41:
                {
                alt73=3;
                }
                break;
            case 27:
                {
                alt73=4;
                }
                break;
            case 68:
                {
                alt73=5;
                }
                break;
            case RULE_INT:
            case RULE_DOUBLE:
            case RULE_STRING:
            case RULE_RANGE:
            case 24:
            case 31:
            case 69:
            case 109:
            case 110:
                {
                alt73=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 73, 0, input);

                throw nvae;
            }

            switch (alt73) {
                case 1 :
                    // InternalBasicIQL.g:5029:3: ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? )
                    {
                    // InternalBasicIQL.g:5029:3: ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? )
                    // InternalBasicIQL.g:5030:4: () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )?
                    {
                    // InternalBasicIQL.g:5030:4: ()
                    // InternalBasicIQL.g:5031:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLOtherExpressionsAccess().getIQLJvmElementCallExpressionAction_0_0(),
                      						current);
                      				
                    }

                    }

                    // InternalBasicIQL.g:5037:4: ( ( ruleQualifiedName ) )
                    // InternalBasicIQL.g:5038:5: ( ruleQualifiedName )
                    {
                    // InternalBasicIQL.g:5038:5: ( ruleQualifiedName )
                    // InternalBasicIQL.g:5039:6: ruleQualifiedName
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getIQLOtherExpressionsRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLOtherExpressionsAccess().getElementJvmIdentifiableElementCrossReference_0_1_0());
                      					
                    }
                    pushFollow(FOLLOW_54);
                    ruleQualifiedName();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalBasicIQL.g:5053:4: ( (lv_args_2_0= ruleIQLArgumentsList ) )?
                    int alt69=2;
                    int LA69_0 = input.LA(1);

                    if ( (LA69_0==27) ) {
                        alt69=1;
                    }
                    switch (alt69) {
                        case 1 :
                            // InternalBasicIQL.g:5054:5: (lv_args_2_0= ruleIQLArgumentsList )
                            {
                            // InternalBasicIQL.g:5054:5: (lv_args_2_0= ruleIQLArgumentsList )
                            // InternalBasicIQL.g:5055:6: lv_args_2_0= ruleIQLArgumentsList
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
                    // InternalBasicIQL.g:5074:3: ( () otherlv_4= 'this' )
                    {
                    // InternalBasicIQL.g:5074:3: ( () otherlv_4= 'this' )
                    // InternalBasicIQL.g:5075:4: () otherlv_4= 'this'
                    {
                    // InternalBasicIQL.g:5075:4: ()
                    // InternalBasicIQL.g:5076:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLOtherExpressionsAccess().getIQLThisExpressionAction_1_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_4=(Token)match(input,40,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_4, grammarAccess.getIQLOtherExpressionsAccess().getThisKeyword_1_1());
                      			
                    }

                    }


                    }
                    break;
                case 3 :
                    // InternalBasicIQL.g:5088:3: ( () otherlv_6= 'super' )
                    {
                    // InternalBasicIQL.g:5088:3: ( () otherlv_6= 'super' )
                    // InternalBasicIQL.g:5089:4: () otherlv_6= 'super'
                    {
                    // InternalBasicIQL.g:5089:4: ()
                    // InternalBasicIQL.g:5090:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLOtherExpressionsAccess().getIQLSuperExpressionAction_2_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_6=(Token)match(input,41,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_6, grammarAccess.getIQLOtherExpressionsAccess().getSuperKeyword_2_1());
                      			
                    }

                    }


                    }
                    break;
                case 4 :
                    // InternalBasicIQL.g:5102:3: ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' )
                    {
                    // InternalBasicIQL.g:5102:3: ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' )
                    // InternalBasicIQL.g:5103:4: () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')'
                    {
                    // InternalBasicIQL.g:5103:4: ()
                    // InternalBasicIQL.g:5104:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLOtherExpressionsAccess().getIQLParenthesisExpressionAction_3_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_8=(Token)match(input,27,FOLLOW_32); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_8, grammarAccess.getIQLOtherExpressionsAccess().getLeftParenthesisKeyword_3_1());
                      			
                    }
                    // InternalBasicIQL.g:5114:4: ( (lv_expr_9_0= ruleIQLExpression ) )
                    // InternalBasicIQL.g:5115:5: (lv_expr_9_0= ruleIQLExpression )
                    {
                    // InternalBasicIQL.g:5115:5: (lv_expr_9_0= ruleIQLExpression )
                    // InternalBasicIQL.g:5116:6: lv_expr_9_0= ruleIQLExpression
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLOtherExpressionsAccess().getExprIQLExpressionParserRuleCall_3_2_0());
                      					
                    }
                    pushFollow(FOLLOW_36);
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

                    otherlv_10=(Token)match(input,28,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_10, grammarAccess.getIQLOtherExpressionsAccess().getRightParenthesisKeyword_3_3());
                      			
                    }

                    }


                    }
                    break;
                case 5 :
                    // InternalBasicIQL.g:5139:3: ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) )
                    {
                    // InternalBasicIQL.g:5139:3: ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) )
                    // InternalBasicIQL.g:5140:4: () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) )
                    {
                    // InternalBasicIQL.g:5140:4: ()
                    // InternalBasicIQL.g:5141:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLOtherExpressionsAccess().getIQLNewExpressionAction_4_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_12=(Token)match(input,68,FOLLOW_3); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_12, grammarAccess.getIQLOtherExpressionsAccess().getNewKeyword_4_1());
                      			
                    }
                    // InternalBasicIQL.g:5151:4: ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) )
                    int alt72=2;
                    alt72 = dfa72.predict(input);
                    switch (alt72) {
                        case 1 :
                            // InternalBasicIQL.g:5152:5: ( (lv_ref_13_0= ruleIQLArrayTypeRef ) )
                            {
                            // InternalBasicIQL.g:5152:5: ( (lv_ref_13_0= ruleIQLArrayTypeRef ) )
                            // InternalBasicIQL.g:5153:6: (lv_ref_13_0= ruleIQLArrayTypeRef )
                            {
                            // InternalBasicIQL.g:5153:6: (lv_ref_13_0= ruleIQLArrayTypeRef )
                            // InternalBasicIQL.g:5154:7: lv_ref_13_0= ruleIQLArrayTypeRef
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
                            // InternalBasicIQL.g:5172:5: ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) )
                            {
                            // InternalBasicIQL.g:5172:5: ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) )
                            // InternalBasicIQL.g:5173:6: ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) )
                            {
                            // InternalBasicIQL.g:5173:6: ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) )
                            // InternalBasicIQL.g:5174:7: (lv_ref_14_0= ruleIQLSimpleTypeRef )
                            {
                            // InternalBasicIQL.g:5174:7: (lv_ref_14_0= ruleIQLSimpleTypeRef )
                            // InternalBasicIQL.g:5175:8: lv_ref_14_0= ruleIQLSimpleTypeRef
                            {
                            if ( state.backtracking==0 ) {

                              								newCompositeNode(grammarAccess.getIQLOtherExpressionsAccess().getRefIQLSimpleTypeRefParserRuleCall_4_2_1_0_0());
                              							
                            }
                            pushFollow(FOLLOW_55);
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

                            // InternalBasicIQL.g:5192:6: ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) )
                            int alt71=2;
                            int LA71_0 = input.LA(1);

                            if ( (LA71_0==27) ) {
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
                                    // InternalBasicIQL.g:5193:7: ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? )
                                    {
                                    // InternalBasicIQL.g:5193:7: ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? )
                                    // InternalBasicIQL.g:5194:8: ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )?
                                    {
                                    // InternalBasicIQL.g:5194:8: ( (lv_argsList_15_0= ruleIQLArgumentsList ) )
                                    // InternalBasicIQL.g:5195:9: (lv_argsList_15_0= ruleIQLArgumentsList )
                                    {
                                    // InternalBasicIQL.g:5195:9: (lv_argsList_15_0= ruleIQLArgumentsList )
                                    // InternalBasicIQL.g:5196:10: lv_argsList_15_0= ruleIQLArgumentsList
                                    {
                                    if ( state.backtracking==0 ) {

                                      										newCompositeNode(grammarAccess.getIQLOtherExpressionsAccess().getArgsListIQLArgumentsListParserRuleCall_4_2_1_1_0_0_0());
                                      									
                                    }
                                    pushFollow(FOLLOW_31);
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

                                    // InternalBasicIQL.g:5213:8: ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )?
                                    int alt70=2;
                                    int LA70_0 = input.LA(1);

                                    if ( (LA70_0==21) ) {
                                        alt70=1;
                                    }
                                    switch (alt70) {
                                        case 1 :
                                            // InternalBasicIQL.g:5214:9: (lv_argsMap_16_0= ruleIQLArgumentsMap )
                                            {
                                            // InternalBasicIQL.g:5214:9: (lv_argsMap_16_0= ruleIQLArgumentsMap )
                                            // InternalBasicIQL.g:5215:10: lv_argsMap_16_0= ruleIQLArgumentsMap
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
                                    // InternalBasicIQL.g:5234:7: ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) )
                                    {
                                    // InternalBasicIQL.g:5234:7: ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) )
                                    // InternalBasicIQL.g:5235:8: (lv_argsMap_17_0= ruleIQLArgumentsMap )
                                    {
                                    // InternalBasicIQL.g:5235:8: (lv_argsMap_17_0= ruleIQLArgumentsMap )
                                    // InternalBasicIQL.g:5236:9: lv_argsMap_17_0= ruleIQLArgumentsMap
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
                    // InternalBasicIQL.g:5258:3: this_IQLLiteralExpression_18= ruleIQLLiteralExpression
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
    // InternalBasicIQL.g:5270:1: entryRuleIQLLiteralExpression returns [EObject current=null] : iv_ruleIQLLiteralExpression= ruleIQLLiteralExpression EOF ;
    public final EObject entryRuleIQLLiteralExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLiteralExpression = null;


        try {
            // InternalBasicIQL.g:5270:61: (iv_ruleIQLLiteralExpression= ruleIQLLiteralExpression EOF )
            // InternalBasicIQL.g:5271:2: iv_ruleIQLLiteralExpression= ruleIQLLiteralExpression EOF
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
    // InternalBasicIQL.g:5277:1: ruleIQLLiteralExpression returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_RANGE ) ) ) | ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' ) | ( () otherlv_15= 'null' ) | ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList ) | this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap ) ;
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
            // InternalBasicIQL.g:5283:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_RANGE ) ) ) | ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' ) | ( () otherlv_15= 'null' ) | ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList ) | this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap ) )
            // InternalBasicIQL.g:5284:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_RANGE ) ) ) | ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' ) | ( () otherlv_15= 'null' ) | ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList ) | this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap )
            {
            // InternalBasicIQL.g:5284:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_RANGE ) ) ) | ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' ) | ( () otherlv_15= 'null' ) | ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList ) | this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap )
            int alt74=9;
            alt74 = dfa74.predict(input);
            switch (alt74) {
                case 1 :
                    // InternalBasicIQL.g:5285:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalBasicIQL.g:5285:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalBasicIQL.g:5286:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalBasicIQL.g:5286:4: ()
                    // InternalBasicIQL.g:5287:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionIntAction_0_0(),
                      						current);
                      				
                    }

                    }

                    // InternalBasicIQL.g:5293:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalBasicIQL.g:5294:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalBasicIQL.g:5294:5: (lv_value_1_0= RULE_INT )
                    // InternalBasicIQL.g:5295:6: lv_value_1_0= RULE_INT
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
                    // InternalBasicIQL.g:5313:3: ( () ( (lv_value_3_0= RULE_DOUBLE ) ) )
                    {
                    // InternalBasicIQL.g:5313:3: ( () ( (lv_value_3_0= RULE_DOUBLE ) ) )
                    // InternalBasicIQL.g:5314:4: () ( (lv_value_3_0= RULE_DOUBLE ) )
                    {
                    // InternalBasicIQL.g:5314:4: ()
                    // InternalBasicIQL.g:5315:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionDoubleAction_1_0(),
                      						current);
                      				
                    }

                    }

                    // InternalBasicIQL.g:5321:4: ( (lv_value_3_0= RULE_DOUBLE ) )
                    // InternalBasicIQL.g:5322:5: (lv_value_3_0= RULE_DOUBLE )
                    {
                    // InternalBasicIQL.g:5322:5: (lv_value_3_0= RULE_DOUBLE )
                    // InternalBasicIQL.g:5323:6: lv_value_3_0= RULE_DOUBLE
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
                    // InternalBasicIQL.g:5341:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalBasicIQL.g:5341:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalBasicIQL.g:5342:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalBasicIQL.g:5342:4: ()
                    // InternalBasicIQL.g:5343:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionStringAction_2_0(),
                      						current);
                      				
                    }

                    }

                    // InternalBasicIQL.g:5349:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalBasicIQL.g:5350:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalBasicIQL.g:5350:5: (lv_value_5_0= RULE_STRING )
                    // InternalBasicIQL.g:5351:6: lv_value_5_0= RULE_STRING
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
                    // InternalBasicIQL.g:5369:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    {
                    // InternalBasicIQL.g:5369:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    // InternalBasicIQL.g:5370:4: () ( (lv_value_7_0= ruleBOOLEAN ) )
                    {
                    // InternalBasicIQL.g:5370:4: ()
                    // InternalBasicIQL.g:5371:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionBooleanAction_3_0(),
                      						current);
                      				
                    }

                    }

                    // InternalBasicIQL.g:5377:4: ( (lv_value_7_0= ruleBOOLEAN ) )
                    // InternalBasicIQL.g:5378:5: (lv_value_7_0= ruleBOOLEAN )
                    {
                    // InternalBasicIQL.g:5378:5: (lv_value_7_0= ruleBOOLEAN )
                    // InternalBasicIQL.g:5379:6: lv_value_7_0= ruleBOOLEAN
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
                    // InternalBasicIQL.g:5398:3: ( () ( (lv_value_9_0= RULE_RANGE ) ) )
                    {
                    // InternalBasicIQL.g:5398:3: ( () ( (lv_value_9_0= RULE_RANGE ) ) )
                    // InternalBasicIQL.g:5399:4: () ( (lv_value_9_0= RULE_RANGE ) )
                    {
                    // InternalBasicIQL.g:5399:4: ()
                    // InternalBasicIQL.g:5400:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionRangeAction_4_0(),
                      						current);
                      				
                    }

                    }

                    // InternalBasicIQL.g:5406:4: ( (lv_value_9_0= RULE_RANGE ) )
                    // InternalBasicIQL.g:5407:5: (lv_value_9_0= RULE_RANGE )
                    {
                    // InternalBasicIQL.g:5407:5: (lv_value_9_0= RULE_RANGE )
                    // InternalBasicIQL.g:5408:6: lv_value_9_0= RULE_RANGE
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
                    // InternalBasicIQL.g:5426:3: ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' )
                    {
                    // InternalBasicIQL.g:5426:3: ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' )
                    // InternalBasicIQL.g:5427:4: () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')'
                    {
                    // InternalBasicIQL.g:5427:4: ()
                    // InternalBasicIQL.g:5428:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionTypeAction_5_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_11=(Token)match(input,69,FOLLOW_3); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_11, grammarAccess.getIQLLiteralExpressionAccess().getClassKeyword_5_1());
                      			
                    }
                    // InternalBasicIQL.g:5438:4: ( (lv_value_12_0= ruleJvmTypeReference ) )
                    // InternalBasicIQL.g:5439:5: (lv_value_12_0= ruleJvmTypeReference )
                    {
                    // InternalBasicIQL.g:5439:5: (lv_value_12_0= ruleJvmTypeReference )
                    // InternalBasicIQL.g:5440:6: lv_value_12_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLLiteralExpressionAccess().getValueJvmTypeReferenceParserRuleCall_5_2_0());
                      					
                    }
                    pushFollow(FOLLOW_36);
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

                    otherlv_13=(Token)match(input,28,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_13, grammarAccess.getIQLLiteralExpressionAccess().getRightParenthesisKeyword_5_3());
                      			
                    }

                    }


                    }
                    break;
                case 7 :
                    // InternalBasicIQL.g:5463:3: ( () otherlv_15= 'null' )
                    {
                    // InternalBasicIQL.g:5463:3: ( () otherlv_15= 'null' )
                    // InternalBasicIQL.g:5464:4: () otherlv_15= 'null'
                    {
                    // InternalBasicIQL.g:5464:4: ()
                    // InternalBasicIQL.g:5465:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionNullAction_6_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_15=(Token)match(input,31,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_15, grammarAccess.getIQLLiteralExpressionAccess().getNullKeyword_6_1());
                      			
                    }

                    }


                    }
                    break;
                case 8 :
                    // InternalBasicIQL.g:5477:3: ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList )
                    {
                    // InternalBasicIQL.g:5477:3: ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList )
                    // InternalBasicIQL.g:5478:4: ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList
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
                    // InternalBasicIQL.g:5489:3: this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap
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
    // InternalBasicIQL.g:5501:1: entryRuleIQLLiteralExpressionList returns [EObject current=null] : iv_ruleIQLLiteralExpressionList= ruleIQLLiteralExpressionList EOF ;
    public final EObject entryRuleIQLLiteralExpressionList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLiteralExpressionList = null;


        try {
            // InternalBasicIQL.g:5501:65: (iv_ruleIQLLiteralExpressionList= ruleIQLLiteralExpressionList EOF )
            // InternalBasicIQL.g:5502:2: iv_ruleIQLLiteralExpressionList= ruleIQLLiteralExpressionList EOF
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
    // InternalBasicIQL.g:5508:1: ruleIQLLiteralExpressionList returns [EObject current=null] : ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ']' ) ;
    public final EObject ruleIQLLiteralExpressionList() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:5514:2: ( ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ']' ) )
            // InternalBasicIQL.g:5515:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ']' )
            {
            // InternalBasicIQL.g:5515:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ']' )
            // InternalBasicIQL.g:5516:3: () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ']'
            {
            // InternalBasicIQL.g:5516:3: ()
            // InternalBasicIQL.g:5517:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLLiteralExpressionListAccess().getIQLLiteralExpressionListAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,24,FOLLOW_56); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLLiteralExpressionListAccess().getLeftSquareBracketKeyword_1());
              		
            }
            // InternalBasicIQL.g:5527:3: ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )?
            int alt76=2;
            int LA76_0 = input.LA(1);

            if ( ((LA76_0>=RULE_ID && LA76_0<=RULE_RANGE)||LA76_0==24||LA76_0==27||LA76_0==31||(LA76_0>=40 && LA76_0<=41)||(LA76_0>=59 && LA76_0<=60)||(LA76_0>=64 && LA76_0<=66)||(LA76_0>=68 && LA76_0<=69)||(LA76_0>=109 && LA76_0<=110)) ) {
                alt76=1;
            }
            switch (alt76) {
                case 1 :
                    // InternalBasicIQL.g:5528:4: ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )*
                    {
                    // InternalBasicIQL.g:5528:4: ( (lv_elements_2_0= ruleIQLExpression ) )
                    // InternalBasicIQL.g:5529:5: (lv_elements_2_0= ruleIQLExpression )
                    {
                    // InternalBasicIQL.g:5529:5: (lv_elements_2_0= ruleIQLExpression )
                    // InternalBasicIQL.g:5530:6: lv_elements_2_0= ruleIQLExpression
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLLiteralExpressionListAccess().getElementsIQLExpressionParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_27);
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

                    // InternalBasicIQL.g:5547:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )*
                    loop75:
                    do {
                        int alt75=2;
                        int LA75_0 = input.LA(1);

                        if ( (LA75_0==20) ) {
                            alt75=1;
                        }


                        switch (alt75) {
                    	case 1 :
                    	    // InternalBasicIQL.g:5548:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) )
                    	    {
                    	    otherlv_3=(Token)match(input,20,FOLLOW_32); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLLiteralExpressionListAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalBasicIQL.g:5552:5: ( (lv_elements_4_0= ruleIQLExpression ) )
                    	    // InternalBasicIQL.g:5553:6: (lv_elements_4_0= ruleIQLExpression )
                    	    {
                    	    // InternalBasicIQL.g:5553:6: (lv_elements_4_0= ruleIQLExpression )
                    	    // InternalBasicIQL.g:5554:7: lv_elements_4_0= ruleIQLExpression
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLLiteralExpressionListAccess().getElementsIQLExpressionParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_27);
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
                    	    break loop75;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,25,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:5581:1: entryRuleIQLLiteralExpressionMap returns [EObject current=null] : iv_ruleIQLLiteralExpressionMap= ruleIQLLiteralExpressionMap EOF ;
    public final EObject entryRuleIQLLiteralExpressionMap() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLiteralExpressionMap = null;


        try {
            // InternalBasicIQL.g:5581:64: (iv_ruleIQLLiteralExpressionMap= ruleIQLLiteralExpressionMap EOF )
            // InternalBasicIQL.g:5582:2: iv_ruleIQLLiteralExpressionMap= ruleIQLLiteralExpressionMap EOF
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
    // InternalBasicIQL.g:5588:1: ruleIQLLiteralExpressionMap returns [EObject current=null] : ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )? otherlv_5= ']' ) ;
    public final EObject ruleIQLLiteralExpressionMap() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:5594:2: ( ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )? otherlv_5= ']' ) )
            // InternalBasicIQL.g:5595:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )? otherlv_5= ']' )
            {
            // InternalBasicIQL.g:5595:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )? otherlv_5= ']' )
            // InternalBasicIQL.g:5596:3: () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )? otherlv_5= ']'
            {
            // InternalBasicIQL.g:5596:3: ()
            // InternalBasicIQL.g:5597:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLLiteralExpressionMapAccess().getIQLLiteralExpressionMapAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,24,FOLLOW_56); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLLiteralExpressionMapAccess().getLeftSquareBracketKeyword_1());
              		
            }
            // InternalBasicIQL.g:5607:3: ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )?
            int alt78=2;
            int LA78_0 = input.LA(1);

            if ( ((LA78_0>=RULE_ID && LA78_0<=RULE_RANGE)||LA78_0==24||LA78_0==27||LA78_0==31||(LA78_0>=40 && LA78_0<=41)||(LA78_0>=59 && LA78_0<=60)||(LA78_0>=64 && LA78_0<=66)||(LA78_0>=68 && LA78_0<=69)||(LA78_0>=109 && LA78_0<=110)) ) {
                alt78=1;
            }
            switch (alt78) {
                case 1 :
                    // InternalBasicIQL.g:5608:4: ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )*
                    {
                    // InternalBasicIQL.g:5608:4: ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) )
                    // InternalBasicIQL.g:5609:5: (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue )
                    {
                    // InternalBasicIQL.g:5609:5: (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue )
                    // InternalBasicIQL.g:5610:6: lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLLiteralExpressionMapAccess().getElementsIQLLiteralExpressionMapKeyValueParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_27);
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

                    // InternalBasicIQL.g:5627:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )*
                    loop77:
                    do {
                        int alt77=2;
                        int LA77_0 = input.LA(1);

                        if ( (LA77_0==20) ) {
                            alt77=1;
                        }


                        switch (alt77) {
                    	case 1 :
                    	    // InternalBasicIQL.g:5628:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) )
                    	    {
                    	    otherlv_3=(Token)match(input,20,FOLLOW_32); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLLiteralExpressionMapAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalBasicIQL.g:5632:5: ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) )
                    	    // InternalBasicIQL.g:5633:6: (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue )
                    	    {
                    	    // InternalBasicIQL.g:5633:6: (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue )
                    	    // InternalBasicIQL.g:5634:7: lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLLiteralExpressionMapAccess().getElementsIQLLiteralExpressionMapKeyValueParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_27);
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
                    	    break loop77;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,25,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:5661:1: entryRuleIQLLiteralExpressionMapKeyValue returns [EObject current=null] : iv_ruleIQLLiteralExpressionMapKeyValue= ruleIQLLiteralExpressionMapKeyValue EOF ;
    public final EObject entryRuleIQLLiteralExpressionMapKeyValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLiteralExpressionMapKeyValue = null;


        try {
            // InternalBasicIQL.g:5661:72: (iv_ruleIQLLiteralExpressionMapKeyValue= ruleIQLLiteralExpressionMapKeyValue EOF )
            // InternalBasicIQL.g:5662:2: iv_ruleIQLLiteralExpressionMapKeyValue= ruleIQLLiteralExpressionMapKeyValue EOF
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
    // InternalBasicIQL.g:5668:1: ruleIQLLiteralExpressionMapKeyValue returns [EObject current=null] : ( ( (lv_key_0_0= ruleIQLExpression ) ) otherlv_1= ':' ( (lv_value_2_0= ruleIQLExpression ) ) ) ;
    public final EObject ruleIQLLiteralExpressionMapKeyValue() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_key_0_0 = null;

        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:5674:2: ( ( ( (lv_key_0_0= ruleIQLExpression ) ) otherlv_1= ':' ( (lv_value_2_0= ruleIQLExpression ) ) ) )
            // InternalBasicIQL.g:5675:2: ( ( (lv_key_0_0= ruleIQLExpression ) ) otherlv_1= ':' ( (lv_value_2_0= ruleIQLExpression ) ) )
            {
            // InternalBasicIQL.g:5675:2: ( ( (lv_key_0_0= ruleIQLExpression ) ) otherlv_1= ':' ( (lv_value_2_0= ruleIQLExpression ) ) )
            // InternalBasicIQL.g:5676:3: ( (lv_key_0_0= ruleIQLExpression ) ) otherlv_1= ':' ( (lv_value_2_0= ruleIQLExpression ) )
            {
            // InternalBasicIQL.g:5676:3: ( (lv_key_0_0= ruleIQLExpression ) )
            // InternalBasicIQL.g:5677:4: (lv_key_0_0= ruleIQLExpression )
            {
            // InternalBasicIQL.g:5677:4: (lv_key_0_0= ruleIQLExpression )
            // InternalBasicIQL.g:5678:5: lv_key_0_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLLiteralExpressionMapKeyValueAccess().getKeyIQLExpressionParserRuleCall_0_0());
              				
            }
            pushFollow(FOLLOW_40);
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

            otherlv_1=(Token)match(input,29,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLLiteralExpressionMapKeyValueAccess().getColonKeyword_1());
              		
            }
            // InternalBasicIQL.g:5699:3: ( (lv_value_2_0= ruleIQLExpression ) )
            // InternalBasicIQL.g:5700:4: (lv_value_2_0= ruleIQLExpression )
            {
            // InternalBasicIQL.g:5700:4: (lv_value_2_0= ruleIQLExpression )
            // InternalBasicIQL.g:5701:5: lv_value_2_0= ruleIQLExpression
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
    // InternalBasicIQL.g:5722:1: entryRuleQualifiedNameWithWildcard returns [String current=null] : iv_ruleQualifiedNameWithWildcard= ruleQualifiedNameWithWildcard EOF ;
    public final String entryRuleQualifiedNameWithWildcard() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedNameWithWildcard = null;


        try {
            // InternalBasicIQL.g:5722:65: (iv_ruleQualifiedNameWithWildcard= ruleQualifiedNameWithWildcard EOF )
            // InternalBasicIQL.g:5723:2: iv_ruleQualifiedNameWithWildcard= ruleQualifiedNameWithWildcard EOF
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
    // InternalBasicIQL.g:5729:1: ruleQualifiedNameWithWildcard returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_QualifiedName_0= ruleQualifiedName (kw= '::*' )? ) ;
    public final AntlrDatatypeRuleToken ruleQualifiedNameWithWildcard() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        AntlrDatatypeRuleToken this_QualifiedName_0 = null;



        	enterRule();

        try {
            // InternalBasicIQL.g:5735:2: ( (this_QualifiedName_0= ruleQualifiedName (kw= '::*' )? ) )
            // InternalBasicIQL.g:5736:2: (this_QualifiedName_0= ruleQualifiedName (kw= '::*' )? )
            {
            // InternalBasicIQL.g:5736:2: (this_QualifiedName_0= ruleQualifiedName (kw= '::*' )? )
            // InternalBasicIQL.g:5737:3: this_QualifiedName_0= ruleQualifiedName (kw= '::*' )?
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getQualifiedNameWithWildcardAccess().getQualifiedNameParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_57);
            this_QualifiedName_0=ruleQualifiedName();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current.merge(this_QualifiedName_0);
              		
            }
            if ( state.backtracking==0 ) {

              			afterParserOrEnumRuleCall();
              		
            }
            // InternalBasicIQL.g:5747:3: (kw= '::*' )?
            int alt79=2;
            int LA79_0 = input.LA(1);

            if ( (LA79_0==70) ) {
                alt79=1;
            }
            switch (alt79) {
                case 1 :
                    // InternalBasicIQL.g:5748:4: kw= '::*'
                    {
                    kw=(Token)match(input,70,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:5758:1: entryRuleQualifiedName returns [String current=null] : iv_ruleQualifiedName= ruleQualifiedName EOF ;
    public final String entryRuleQualifiedName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedName = null;


        try {
            // InternalBasicIQL.g:5758:53: (iv_ruleQualifiedName= ruleQualifiedName EOF )
            // InternalBasicIQL.g:5759:2: iv_ruleQualifiedName= ruleQualifiedName EOF
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
    // InternalBasicIQL.g:5765:1: ruleQualifiedName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID (kw= '::' this_ID_2= RULE_ID )* ) ;
    public final AntlrDatatypeRuleToken ruleQualifiedName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_2=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:5771:2: ( (this_ID_0= RULE_ID (kw= '::' this_ID_2= RULE_ID )* ) )
            // InternalBasicIQL.g:5772:2: (this_ID_0= RULE_ID (kw= '::' this_ID_2= RULE_ID )* )
            {
            // InternalBasicIQL.g:5772:2: (this_ID_0= RULE_ID (kw= '::' this_ID_2= RULE_ID )* )
            // InternalBasicIQL.g:5773:3: this_ID_0= RULE_ID (kw= '::' this_ID_2= RULE_ID )*
            {
            this_ID_0=(Token)match(input,RULE_ID,FOLLOW_58); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current.merge(this_ID_0);
              		
            }
            if ( state.backtracking==0 ) {

              			newLeafNode(this_ID_0, grammarAccess.getQualifiedNameAccess().getIDTerminalRuleCall_0());
              		
            }
            // InternalBasicIQL.g:5780:3: (kw= '::' this_ID_2= RULE_ID )*
            loop80:
            do {
                int alt80=2;
                int LA80_0 = input.LA(1);

                if ( (LA80_0==71) ) {
                    alt80=1;
                }


                switch (alt80) {
            	case 1 :
            	    // InternalBasicIQL.g:5781:4: kw= '::' this_ID_2= RULE_ID
            	    {
            	    kw=(Token)match(input,71,FOLLOW_3); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				current.merge(kw);
            	      				newLeafNode(kw, grammarAccess.getQualifiedNameAccess().getColonColonKeyword_1_0());
            	      			
            	    }
            	    this_ID_2=(Token)match(input,RULE_ID,FOLLOW_58); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				current.merge(this_ID_2);
            	      			
            	    }
            	    if ( state.backtracking==0 ) {

            	      				newLeafNode(this_ID_2, grammarAccess.getQualifiedNameAccess().getIDTerminalRuleCall_1_1());
            	      			
            	    }

            	    }
            	    break;

            	default :
            	    break loop80;
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
    // InternalBasicIQL.g:5798:1: entryRuleIQLJava returns [EObject current=null] : iv_ruleIQLJava= ruleIQLJava EOF ;
    public final EObject entryRuleIQLJava() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLJava = null;



        	HiddenTokens myHiddenTokenState = ((XtextTokenStream)input).setHiddenTokens("RULE_SL_COMMENT", "RULE_ML_COMMENT");

        try {
            // InternalBasicIQL.g:5800:2: (iv_ruleIQLJava= ruleIQLJava EOF )
            // InternalBasicIQL.g:5801:2: iv_ruleIQLJava= ruleIQLJava EOF
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
    // InternalBasicIQL.g:5810:1: ruleIQLJava returns [EObject current=null] : (otherlv_0= '$*' ( (lv_text_1_0= ruleIQLJavaText ) ) otherlv_2= '*$' ) ;
    public final EObject ruleIQLJava() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        AntlrDatatypeRuleToken lv_text_1_0 = null;



        	enterRule();
        	HiddenTokens myHiddenTokenState = ((XtextTokenStream)input).setHiddenTokens("RULE_SL_COMMENT", "RULE_ML_COMMENT");

        try {
            // InternalBasicIQL.g:5817:2: ( (otherlv_0= '$*' ( (lv_text_1_0= ruleIQLJavaText ) ) otherlv_2= '*$' ) )
            // InternalBasicIQL.g:5818:2: (otherlv_0= '$*' ( (lv_text_1_0= ruleIQLJavaText ) ) otherlv_2= '*$' )
            {
            // InternalBasicIQL.g:5818:2: (otherlv_0= '$*' ( (lv_text_1_0= ruleIQLJavaText ) ) otherlv_2= '*$' )
            // InternalBasicIQL.g:5819:3: otherlv_0= '$*' ( (lv_text_1_0= ruleIQLJavaText ) ) otherlv_2= '*$'
            {
            otherlv_0=(Token)match(input,72,FOLLOW_59); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_0, grammarAccess.getIQLJavaAccess().getDollarSignAsteriskKeyword_0());
              		
            }
            // InternalBasicIQL.g:5823:3: ( (lv_text_1_0= ruleIQLJavaText ) )
            // InternalBasicIQL.g:5824:4: (lv_text_1_0= ruleIQLJavaText )
            {
            // InternalBasicIQL.g:5824:4: (lv_text_1_0= ruleIQLJavaText )
            // InternalBasicIQL.g:5825:5: lv_text_1_0= ruleIQLJavaText
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLJavaAccess().getTextIQLJavaTextParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_60);
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
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLJavaText");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_2=(Token)match(input,73,FOLLOW_2); if (state.failed) return current;
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


    // $ANTLR start "entryRuleIQLJavaText"
    // InternalBasicIQL.g:5853:1: entryRuleIQLJavaText returns [String current=null] : iv_ruleIQLJavaText= ruleIQLJavaText EOF ;
    public final String entryRuleIQLJavaText() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleIQLJavaText = null;


        try {
            // InternalBasicIQL.g:5853:51: (iv_ruleIQLJavaText= ruleIQLJavaText EOF )
            // InternalBasicIQL.g:5854:2: iv_ruleIQLJavaText= ruleIQLJavaText EOF
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
    // InternalBasicIQL.g:5860:1: ruleIQLJavaText returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_IQL_JAVA_KEYWORDS_0= ruleIQL_JAVA_KEYWORDS | this_WS_1= RULE_WS | this_ID_2= RULE_ID | this_BOOLEAN_3= ruleBOOLEAN | this_DOUBLE_4= RULE_DOUBLE | this_STRING_5= RULE_STRING | this_INT_6= RULE_INT | this_ANY_OTHER_7= RULE_ANY_OTHER | kw= '+' | kw= '+=' | kw= '-' | kw= '-=' | kw= '*' | kw= '*=' | kw= '/' | kw= '/=' | kw= '%' | kw= '%=' | kw= '++' | kw= '--' | kw= '>' | kw= '>=' | kw= '<' | kw= '<=' | kw= '!' | kw= '!=' | kw= '&&' | kw= '||' | kw= '==' | kw= '=' | kw= '~' | kw= '?:' | kw= '|' | kw= '|=' | kw= '^' | kw= '^=' | kw= '&' | kw= '&=' | kw= '>>' | kw= '>>=' | kw= '<<' | kw= '<<=' | kw= '>>>' | kw= '>>>=' | kw= '[' | kw= ']' | kw= '{' | kw= '}' | kw= '(' | kw= ')' | kw= '.' | kw= ':' | kw= ';' | kw= ',' | kw= 'null' )* ;
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
            // InternalBasicIQL.g:5866:2: ( (this_IQL_JAVA_KEYWORDS_0= ruleIQL_JAVA_KEYWORDS | this_WS_1= RULE_WS | this_ID_2= RULE_ID | this_BOOLEAN_3= ruleBOOLEAN | this_DOUBLE_4= RULE_DOUBLE | this_STRING_5= RULE_STRING | this_INT_6= RULE_INT | this_ANY_OTHER_7= RULE_ANY_OTHER | kw= '+' | kw= '+=' | kw= '-' | kw= '-=' | kw= '*' | kw= '*=' | kw= '/' | kw= '/=' | kw= '%' | kw= '%=' | kw= '++' | kw= '--' | kw= '>' | kw= '>=' | kw= '<' | kw= '<=' | kw= '!' | kw= '!=' | kw= '&&' | kw= '||' | kw= '==' | kw= '=' | kw= '~' | kw= '?:' | kw= '|' | kw= '|=' | kw= '^' | kw= '^=' | kw= '&' | kw= '&=' | kw= '>>' | kw= '>>=' | kw= '<<' | kw= '<<=' | kw= '>>>' | kw= '>>>=' | kw= '[' | kw= ']' | kw= '{' | kw= '}' | kw= '(' | kw= ')' | kw= '.' | kw= ':' | kw= ';' | kw= ',' | kw= 'null' )* )
            // InternalBasicIQL.g:5867:2: (this_IQL_JAVA_KEYWORDS_0= ruleIQL_JAVA_KEYWORDS | this_WS_1= RULE_WS | this_ID_2= RULE_ID | this_BOOLEAN_3= ruleBOOLEAN | this_DOUBLE_4= RULE_DOUBLE | this_STRING_5= RULE_STRING | this_INT_6= RULE_INT | this_ANY_OTHER_7= RULE_ANY_OTHER | kw= '+' | kw= '+=' | kw= '-' | kw= '-=' | kw= '*' | kw= '*=' | kw= '/' | kw= '/=' | kw= '%' | kw= '%=' | kw= '++' | kw= '--' | kw= '>' | kw= '>=' | kw= '<' | kw= '<=' | kw= '!' | kw= '!=' | kw= '&&' | kw= '||' | kw= '==' | kw= '=' | kw= '~' | kw= '?:' | kw= '|' | kw= '|=' | kw= '^' | kw= '^=' | kw= '&' | kw= '&=' | kw= '>>' | kw= '>>=' | kw= '<<' | kw= '<<=' | kw= '>>>' | kw= '>>>=' | kw= '[' | kw= ']' | kw= '{' | kw= '}' | kw= '(' | kw= ')' | kw= '.' | kw= ':' | kw= ';' | kw= ',' | kw= 'null' )*
            {
            // InternalBasicIQL.g:5867:2: (this_IQL_JAVA_KEYWORDS_0= ruleIQL_JAVA_KEYWORDS | this_WS_1= RULE_WS | this_ID_2= RULE_ID | this_BOOLEAN_3= ruleBOOLEAN | this_DOUBLE_4= RULE_DOUBLE | this_STRING_5= RULE_STRING | this_INT_6= RULE_INT | this_ANY_OTHER_7= RULE_ANY_OTHER | kw= '+' | kw= '+=' | kw= '-' | kw= '-=' | kw= '*' | kw= '*=' | kw= '/' | kw= '/=' | kw= '%' | kw= '%=' | kw= '++' | kw= '--' | kw= '>' | kw= '>=' | kw= '<' | kw= '<=' | kw= '!' | kw= '!=' | kw= '&&' | kw= '||' | kw= '==' | kw= '=' | kw= '~' | kw= '?:' | kw= '|' | kw= '|=' | kw= '^' | kw= '^=' | kw= '&' | kw= '&=' | kw= '>>' | kw= '>>=' | kw= '<<' | kw= '<<=' | kw= '>>>' | kw= '>>>=' | kw= '[' | kw= ']' | kw= '{' | kw= '}' | kw= '(' | kw= ')' | kw= '.' | kw= ':' | kw= ';' | kw= ',' | kw= 'null' )*
            loop81:
            do {
                int alt81=56;
                switch ( input.LA(1) ) {
                case 16:
                case 17:
                case 18:
                case 19:
                case 23:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 54:
                case 68:
                case 88:
                case 89:
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
                    {
                    alt81=1;
                    }
                    break;
                case RULE_WS:
                    {
                    alt81=2;
                    }
                    break;
                case RULE_ID:
                    {
                    alt81=3;
                    }
                    break;
                case 109:
                case 110:
                    {
                    alt81=4;
                    }
                    break;
                case RULE_DOUBLE:
                    {
                    alt81=5;
                    }
                    break;
                case RULE_STRING:
                    {
                    alt81=6;
                    }
                    break;
                case RULE_INT:
                    {
                    alt81=7;
                    }
                    break;
                case RULE_ANY_OTHER:
                    {
                    alt81=8;
                    }
                    break;
                case 59:
                    {
                    alt81=9;
                    }
                    break;
                case 45:
                    {
                    alt81=10;
                    }
                    break;
                case 60:
                    {
                    alt81=11;
                    }
                    break;
                case 46:
                    {
                    alt81=12;
                    }
                    break;
                case 61:
                    {
                    alt81=13;
                    }
                    break;
                case 47:
                    {
                    alt81=14;
                    }
                    break;
                case 62:
                    {
                    alt81=15;
                    }
                    break;
                case 48:
                    {
                    alt81=16;
                    }
                    break;
                case 63:
                    {
                    alt81=17;
                    }
                    break;
                case 49:
                    {
                    alt81=18;
                    }
                    break;
                case 65:
                    {
                    alt81=19;
                    }
                    break;
                case 66:
                    {
                    alt81=20;
                    }
                    break;
                case 55:
                    {
                    alt81=21;
                    }
                    break;
                case 56:
                    {
                    alt81=22;
                    }
                    break;
                case 57:
                    {
                    alt81=23;
                    }
                    break;
                case 58:
                    {
                    alt81=24;
                    }
                    break;
                case 64:
                    {
                    alt81=25;
                    }
                    break;
                case 53:
                    {
                    alt81=26;
                    }
                    break;
                case 51:
                    {
                    alt81=27;
                    }
                    break;
                case 50:
                    {
                    alt81=28;
                    }
                    break;
                case 52:
                    {
                    alt81=29;
                    }
                    break;
                case 30:
                    {
                    alt81=30;
                    }
                    break;
                case 74:
                    {
                    alt81=31;
                    }
                    break;
                case 75:
                    {
                    alt81=32;
                    }
                    break;
                case 76:
                    {
                    alt81=33;
                    }
                    break;
                case 77:
                    {
                    alt81=34;
                    }
                    break;
                case 78:
                    {
                    alt81=35;
                    }
                    break;
                case 79:
                    {
                    alt81=36;
                    }
                    break;
                case 80:
                    {
                    alt81=37;
                    }
                    break;
                case 81:
                    {
                    alt81=38;
                    }
                    break;
                case 82:
                    {
                    alt81=39;
                    }
                    break;
                case 83:
                    {
                    alt81=40;
                    }
                    break;
                case 84:
                    {
                    alt81=41;
                    }
                    break;
                case 85:
                    {
                    alt81=42;
                    }
                    break;
                case 86:
                    {
                    alt81=43;
                    }
                    break;
                case 87:
                    {
                    alt81=44;
                    }
                    break;
                case 24:
                    {
                    alt81=45;
                    }
                    break;
                case 25:
                    {
                    alt81=46;
                    }
                    break;
                case 21:
                    {
                    alt81=47;
                    }
                    break;
                case 22:
                    {
                    alt81=48;
                    }
                    break;
                case 27:
                    {
                    alt81=49;
                    }
                    break;
                case 28:
                    {
                    alt81=50;
                    }
                    break;
                case 67:
                    {
                    alt81=51;
                    }
                    break;
                case 29:
                    {
                    alt81=52;
                    }
                    break;
                case 14:
                    {
                    alt81=53;
                    }
                    break;
                case 20:
                    {
                    alt81=54;
                    }
                    break;
                case 31:
                    {
                    alt81=55;
                    }
                    break;

                }

                switch (alt81) {
            	case 1 :
            	    // InternalBasicIQL.g:5868:3: this_IQL_JAVA_KEYWORDS_0= ruleIQL_JAVA_KEYWORDS
            	    {
            	    if ( state.backtracking==0 ) {

            	      			newCompositeNode(grammarAccess.getIQLJavaTextAccess().getIQL_JAVA_KEYWORDSParserRuleCall_0());
            	      		
            	    }
            	    pushFollow(FOLLOW_61);
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
            	    // InternalBasicIQL.g:5879:3: this_WS_1= RULE_WS
            	    {
            	    this_WS_1=(Token)match(input,RULE_WS,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_WS_1);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_WS_1, grammarAccess.getIQLJavaTextAccess().getWSTerminalRuleCall_1());
            	      		
            	    }

            	    }
            	    break;
            	case 3 :
            	    // InternalBasicIQL.g:5887:3: this_ID_2= RULE_ID
            	    {
            	    this_ID_2=(Token)match(input,RULE_ID,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_ID_2);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_ID_2, grammarAccess.getIQLJavaTextAccess().getIDTerminalRuleCall_2());
            	      		
            	    }

            	    }
            	    break;
            	case 4 :
            	    // InternalBasicIQL.g:5895:3: this_BOOLEAN_3= ruleBOOLEAN
            	    {
            	    if ( state.backtracking==0 ) {

            	      			newCompositeNode(grammarAccess.getIQLJavaTextAccess().getBOOLEANParserRuleCall_3());
            	      		
            	    }
            	    pushFollow(FOLLOW_61);
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
            	    // InternalBasicIQL.g:5906:3: this_DOUBLE_4= RULE_DOUBLE
            	    {
            	    this_DOUBLE_4=(Token)match(input,RULE_DOUBLE,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_DOUBLE_4);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_DOUBLE_4, grammarAccess.getIQLJavaTextAccess().getDOUBLETerminalRuleCall_4());
            	      		
            	    }

            	    }
            	    break;
            	case 6 :
            	    // InternalBasicIQL.g:5914:3: this_STRING_5= RULE_STRING
            	    {
            	    this_STRING_5=(Token)match(input,RULE_STRING,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_STRING_5);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_STRING_5, grammarAccess.getIQLJavaTextAccess().getSTRINGTerminalRuleCall_5());
            	      		
            	    }

            	    }
            	    break;
            	case 7 :
            	    // InternalBasicIQL.g:5922:3: this_INT_6= RULE_INT
            	    {
            	    this_INT_6=(Token)match(input,RULE_INT,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_INT_6);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_INT_6, grammarAccess.getIQLJavaTextAccess().getINTTerminalRuleCall_6());
            	      		
            	    }

            	    }
            	    break;
            	case 8 :
            	    // InternalBasicIQL.g:5930:3: this_ANY_OTHER_7= RULE_ANY_OTHER
            	    {
            	    this_ANY_OTHER_7=(Token)match(input,RULE_ANY_OTHER,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_ANY_OTHER_7);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_ANY_OTHER_7, grammarAccess.getIQLJavaTextAccess().getANY_OTHERTerminalRuleCall_7());
            	      		
            	    }

            	    }
            	    break;
            	case 9 :
            	    // InternalBasicIQL.g:5938:3: kw= '+'
            	    {
            	    kw=(Token)match(input,59,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getPlusSignKeyword_8());
            	      		
            	    }

            	    }
            	    break;
            	case 10 :
            	    // InternalBasicIQL.g:5944:3: kw= '+='
            	    {
            	    kw=(Token)match(input,45,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getPlusSignEqualsSignKeyword_9());
            	      		
            	    }

            	    }
            	    break;
            	case 11 :
            	    // InternalBasicIQL.g:5950:3: kw= '-'
            	    {
            	    kw=(Token)match(input,60,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getHyphenMinusKeyword_10());
            	      		
            	    }

            	    }
            	    break;
            	case 12 :
            	    // InternalBasicIQL.g:5956:3: kw= '-='
            	    {
            	    kw=(Token)match(input,46,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getHyphenMinusEqualsSignKeyword_11());
            	      		
            	    }

            	    }
            	    break;
            	case 13 :
            	    // InternalBasicIQL.g:5962:3: kw= '*'
            	    {
            	    kw=(Token)match(input,61,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getAsteriskKeyword_12());
            	      		
            	    }

            	    }
            	    break;
            	case 14 :
            	    // InternalBasicIQL.g:5968:3: kw= '*='
            	    {
            	    kw=(Token)match(input,47,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getAsteriskEqualsSignKeyword_13());
            	      		
            	    }

            	    }
            	    break;
            	case 15 :
            	    // InternalBasicIQL.g:5974:3: kw= '/'
            	    {
            	    kw=(Token)match(input,62,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getSolidusKeyword_14());
            	      		
            	    }

            	    }
            	    break;
            	case 16 :
            	    // InternalBasicIQL.g:5980:3: kw= '/='
            	    {
            	    kw=(Token)match(input,48,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getSolidusEqualsSignKeyword_15());
            	      		
            	    }

            	    }
            	    break;
            	case 17 :
            	    // InternalBasicIQL.g:5986:3: kw= '%'
            	    {
            	    kw=(Token)match(input,63,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getPercentSignKeyword_16());
            	      		
            	    }

            	    }
            	    break;
            	case 18 :
            	    // InternalBasicIQL.g:5992:3: kw= '%='
            	    {
            	    kw=(Token)match(input,49,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getPercentSignEqualsSignKeyword_17());
            	      		
            	    }

            	    }
            	    break;
            	case 19 :
            	    // InternalBasicIQL.g:5998:3: kw= '++'
            	    {
            	    kw=(Token)match(input,65,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getPlusSignPlusSignKeyword_18());
            	      		
            	    }

            	    }
            	    break;
            	case 20 :
            	    // InternalBasicIQL.g:6004:3: kw= '--'
            	    {
            	    kw=(Token)match(input,66,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getHyphenMinusHyphenMinusKeyword_19());
            	      		
            	    }

            	    }
            	    break;
            	case 21 :
            	    // InternalBasicIQL.g:6010:3: kw= '>'
            	    {
            	    kw=(Token)match(input,55,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignKeyword_20());
            	      		
            	    }

            	    }
            	    break;
            	case 22 :
            	    // InternalBasicIQL.g:6016:3: kw= '>='
            	    {
            	    kw=(Token)match(input,56,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignEqualsSignKeyword_21());
            	      		
            	    }

            	    }
            	    break;
            	case 23 :
            	    // InternalBasicIQL.g:6022:3: kw= '<'
            	    {
            	    kw=(Token)match(input,57,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLessThanSignKeyword_22());
            	      		
            	    }

            	    }
            	    break;
            	case 24 :
            	    // InternalBasicIQL.g:6028:3: kw= '<='
            	    {
            	    kw=(Token)match(input,58,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLessThanSignEqualsSignKeyword_23());
            	      		
            	    }

            	    }
            	    break;
            	case 25 :
            	    // InternalBasicIQL.g:6034:3: kw= '!'
            	    {
            	    kw=(Token)match(input,64,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getExclamationMarkKeyword_24());
            	      		
            	    }

            	    }
            	    break;
            	case 26 :
            	    // InternalBasicIQL.g:6040:3: kw= '!='
            	    {
            	    kw=(Token)match(input,53,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getExclamationMarkEqualsSignKeyword_25());
            	      		
            	    }

            	    }
            	    break;
            	case 27 :
            	    // InternalBasicIQL.g:6046:3: kw= '&&'
            	    {
            	    kw=(Token)match(input,51,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getAmpersandAmpersandKeyword_26());
            	      		
            	    }

            	    }
            	    break;
            	case 28 :
            	    // InternalBasicIQL.g:6052:3: kw= '||'
            	    {
            	    kw=(Token)match(input,50,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getVerticalLineVerticalLineKeyword_27());
            	      		
            	    }

            	    }
            	    break;
            	case 29 :
            	    // InternalBasicIQL.g:6058:3: kw= '=='
            	    {
            	    kw=(Token)match(input,52,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getEqualsSignEqualsSignKeyword_28());
            	      		
            	    }

            	    }
            	    break;
            	case 30 :
            	    // InternalBasicIQL.g:6064:3: kw= '='
            	    {
            	    kw=(Token)match(input,30,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getEqualsSignKeyword_29());
            	      		
            	    }

            	    }
            	    break;
            	case 31 :
            	    // InternalBasicIQL.g:6070:3: kw= '~'
            	    {
            	    kw=(Token)match(input,74,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getTildeKeyword_30());
            	      		
            	    }

            	    }
            	    break;
            	case 32 :
            	    // InternalBasicIQL.g:6076:3: kw= '?:'
            	    {
            	    kw=(Token)match(input,75,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getQuestionMarkColonKeyword_31());
            	      		
            	    }

            	    }
            	    break;
            	case 33 :
            	    // InternalBasicIQL.g:6082:3: kw= '|'
            	    {
            	    kw=(Token)match(input,76,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getVerticalLineKeyword_32());
            	      		
            	    }

            	    }
            	    break;
            	case 34 :
            	    // InternalBasicIQL.g:6088:3: kw= '|='
            	    {
            	    kw=(Token)match(input,77,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getVerticalLineEqualsSignKeyword_33());
            	      		
            	    }

            	    }
            	    break;
            	case 35 :
            	    // InternalBasicIQL.g:6094:3: kw= '^'
            	    {
            	    kw=(Token)match(input,78,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getCircumflexAccentKeyword_34());
            	      		
            	    }

            	    }
            	    break;
            	case 36 :
            	    // InternalBasicIQL.g:6100:3: kw= '^='
            	    {
            	    kw=(Token)match(input,79,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getCircumflexAccentEqualsSignKeyword_35());
            	      		
            	    }

            	    }
            	    break;
            	case 37 :
            	    // InternalBasicIQL.g:6106:3: kw= '&'
            	    {
            	    kw=(Token)match(input,80,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getAmpersandKeyword_36());
            	      		
            	    }

            	    }
            	    break;
            	case 38 :
            	    // InternalBasicIQL.g:6112:3: kw= '&='
            	    {
            	    kw=(Token)match(input,81,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getAmpersandEqualsSignKeyword_37());
            	      		
            	    }

            	    }
            	    break;
            	case 39 :
            	    // InternalBasicIQL.g:6118:3: kw= '>>'
            	    {
            	    kw=(Token)match(input,82,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignGreaterThanSignKeyword_38());
            	      		
            	    }

            	    }
            	    break;
            	case 40 :
            	    // InternalBasicIQL.g:6124:3: kw= '>>='
            	    {
            	    kw=(Token)match(input,83,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignGreaterThanSignEqualsSignKeyword_39());
            	      		
            	    }

            	    }
            	    break;
            	case 41 :
            	    // InternalBasicIQL.g:6130:3: kw= '<<'
            	    {
            	    kw=(Token)match(input,84,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLessThanSignLessThanSignKeyword_40());
            	      		
            	    }

            	    }
            	    break;
            	case 42 :
            	    // InternalBasicIQL.g:6136:3: kw= '<<='
            	    {
            	    kw=(Token)match(input,85,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLessThanSignLessThanSignEqualsSignKeyword_41());
            	      		
            	    }

            	    }
            	    break;
            	case 43 :
            	    // InternalBasicIQL.g:6142:3: kw= '>>>'
            	    {
            	    kw=(Token)match(input,86,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignGreaterThanSignGreaterThanSignKeyword_42());
            	      		
            	    }

            	    }
            	    break;
            	case 44 :
            	    // InternalBasicIQL.g:6148:3: kw= '>>>='
            	    {
            	    kw=(Token)match(input,87,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignGreaterThanSignGreaterThanSignEqualsSignKeyword_43());
            	      		
            	    }

            	    }
            	    break;
            	case 45 :
            	    // InternalBasicIQL.g:6154:3: kw= '['
            	    {
            	    kw=(Token)match(input,24,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLeftSquareBracketKeyword_44());
            	      		
            	    }

            	    }
            	    break;
            	case 46 :
            	    // InternalBasicIQL.g:6160:3: kw= ']'
            	    {
            	    kw=(Token)match(input,25,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getRightSquareBracketKeyword_45());
            	      		
            	    }

            	    }
            	    break;
            	case 47 :
            	    // InternalBasicIQL.g:6166:3: kw= '{'
            	    {
            	    kw=(Token)match(input,21,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLeftCurlyBracketKeyword_46());
            	      		
            	    }

            	    }
            	    break;
            	case 48 :
            	    // InternalBasicIQL.g:6172:3: kw= '}'
            	    {
            	    kw=(Token)match(input,22,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getRightCurlyBracketKeyword_47());
            	      		
            	    }

            	    }
            	    break;
            	case 49 :
            	    // InternalBasicIQL.g:6178:3: kw= '('
            	    {
            	    kw=(Token)match(input,27,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLeftParenthesisKeyword_48());
            	      		
            	    }

            	    }
            	    break;
            	case 50 :
            	    // InternalBasicIQL.g:6184:3: kw= ')'
            	    {
            	    kw=(Token)match(input,28,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getRightParenthesisKeyword_49());
            	      		
            	    }

            	    }
            	    break;
            	case 51 :
            	    // InternalBasicIQL.g:6190:3: kw= '.'
            	    {
            	    kw=(Token)match(input,67,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getFullStopKeyword_50());
            	      		
            	    }

            	    }
            	    break;
            	case 52 :
            	    // InternalBasicIQL.g:6196:3: kw= ':'
            	    {
            	    kw=(Token)match(input,29,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getColonKeyword_51());
            	      		
            	    }

            	    }
            	    break;
            	case 53 :
            	    // InternalBasicIQL.g:6202:3: kw= ';'
            	    {
            	    kw=(Token)match(input,14,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getSemicolonKeyword_52());
            	      		
            	    }

            	    }
            	    break;
            	case 54 :
            	    // InternalBasicIQL.g:6208:3: kw= ','
            	    {
            	    kw=(Token)match(input,20,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getCommaKeyword_53());
            	      		
            	    }

            	    }
            	    break;
            	case 55 :
            	    // InternalBasicIQL.g:6214:3: kw= 'null'
            	    {
            	    kw=(Token)match(input,31,FOLLOW_61); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getNullKeyword_54());
            	      		
            	    }

            	    }
            	    break;

            	default :
            	    break loop81;
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


    // $ANTLR start "entryRuleIQL_JAVA_KEYWORDS"
    // InternalBasicIQL.g:6223:1: entryRuleIQL_JAVA_KEYWORDS returns [String current=null] : iv_ruleIQL_JAVA_KEYWORDS= ruleIQL_JAVA_KEYWORDS EOF ;
    public final String entryRuleIQL_JAVA_KEYWORDS() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleIQL_JAVA_KEYWORDS = null;


        try {
            // InternalBasicIQL.g:6223:57: (iv_ruleIQL_JAVA_KEYWORDS= ruleIQL_JAVA_KEYWORDS EOF )
            // InternalBasicIQL.g:6224:2: iv_ruleIQL_JAVA_KEYWORDS= ruleIQL_JAVA_KEYWORDS EOF
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
    // InternalBasicIQL.g:6230:1: ruleIQL_JAVA_KEYWORDS returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'break' | kw= 'case' | kw= 'class' | kw= 'continue' | kw= 'default' | kw= 'do' | kw= 'else' | kw= 'extends' | kw= 'for' | kw= 'if' | kw= 'implements' | kw= 'instanceof' | kw= 'interface' | kw= 'new' | kw= 'package' | kw= 'return' | kw= 'super' | kw= 'switch' | kw= 'this' | kw= 'while' | kw= 'abstract' | kw= 'assert' | kw= 'catch' | kw= 'const' | kw= 'enum' | kw= 'final' | kw= 'finally' | kw= 'goto' | kw= 'import' | kw= 'native' | kw= 'private' | kw= 'protected' | kw= 'public' | kw= 'static' | kw= 'synchronized' | kw= 'throw' | kw= 'throws' | kw= 'transient' | kw= 'try' | kw= 'volatile' | kw= 'strictfp' ) ;
    public final AntlrDatatypeRuleToken ruleIQL_JAVA_KEYWORDS() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:6236:2: ( (kw= 'break' | kw= 'case' | kw= 'class' | kw= 'continue' | kw= 'default' | kw= 'do' | kw= 'else' | kw= 'extends' | kw= 'for' | kw= 'if' | kw= 'implements' | kw= 'instanceof' | kw= 'interface' | kw= 'new' | kw= 'package' | kw= 'return' | kw= 'super' | kw= 'switch' | kw= 'this' | kw= 'while' | kw= 'abstract' | kw= 'assert' | kw= 'catch' | kw= 'const' | kw= 'enum' | kw= 'final' | kw= 'finally' | kw= 'goto' | kw= 'import' | kw= 'native' | kw= 'private' | kw= 'protected' | kw= 'public' | kw= 'static' | kw= 'synchronized' | kw= 'throw' | kw= 'throws' | kw= 'transient' | kw= 'try' | kw= 'volatile' | kw= 'strictfp' ) )
            // InternalBasicIQL.g:6237:2: (kw= 'break' | kw= 'case' | kw= 'class' | kw= 'continue' | kw= 'default' | kw= 'do' | kw= 'else' | kw= 'extends' | kw= 'for' | kw= 'if' | kw= 'implements' | kw= 'instanceof' | kw= 'interface' | kw= 'new' | kw= 'package' | kw= 'return' | kw= 'super' | kw= 'switch' | kw= 'this' | kw= 'while' | kw= 'abstract' | kw= 'assert' | kw= 'catch' | kw= 'const' | kw= 'enum' | kw= 'final' | kw= 'finally' | kw= 'goto' | kw= 'import' | kw= 'native' | kw= 'private' | kw= 'protected' | kw= 'public' | kw= 'static' | kw= 'synchronized' | kw= 'throw' | kw= 'throws' | kw= 'transient' | kw= 'try' | kw= 'volatile' | kw= 'strictfp' )
            {
            // InternalBasicIQL.g:6237:2: (kw= 'break' | kw= 'case' | kw= 'class' | kw= 'continue' | kw= 'default' | kw= 'do' | kw= 'else' | kw= 'extends' | kw= 'for' | kw= 'if' | kw= 'implements' | kw= 'instanceof' | kw= 'interface' | kw= 'new' | kw= 'package' | kw= 'return' | kw= 'super' | kw= 'switch' | kw= 'this' | kw= 'while' | kw= 'abstract' | kw= 'assert' | kw= 'catch' | kw= 'const' | kw= 'enum' | kw= 'final' | kw= 'finally' | kw= 'goto' | kw= 'import' | kw= 'native' | kw= 'private' | kw= 'protected' | kw= 'public' | kw= 'static' | kw= 'synchronized' | kw= 'throw' | kw= 'throws' | kw= 'transient' | kw= 'try' | kw= 'volatile' | kw= 'strictfp' )
            int alt82=41;
            switch ( input.LA(1) ) {
            case 42:
                {
                alt82=1;
                }
                break;
            case 39:
                {
                alt82=2;
                }
                break;
            case 17:
                {
                alt82=3;
                }
                break;
            case 43:
                {
                alt82=4;
                }
                break;
            case 38:
                {
                alt82=5;
                }
                break;
            case 35:
                {
                alt82=6;
                }
                break;
            case 33:
                {
                alt82=7;
                }
                break;
            case 18:
                {
                alt82=8;
                }
                break;
            case 36:
                {
                alt82=9;
                }
                break;
            case 32:
                {
                alt82=10;
                }
                break;
            case 19:
                {
                alt82=11;
                }
                break;
            case 54:
                {
                alt82=12;
                }
                break;
            case 23:
                {
                alt82=13;
                }
                break;
            case 68:
                {
                alt82=14;
                }
                break;
            case 88:
                {
                alt82=15;
                }
                break;
            case 44:
                {
                alt82=16;
                }
                break;
            case 41:
                {
                alt82=17;
                }
                break;
            case 37:
                {
                alt82=18;
                }
                break;
            case 40:
                {
                alt82=19;
                }
                break;
            case 34:
                {
                alt82=20;
                }
                break;
            case 89:
                {
                alt82=21;
                }
                break;
            case 90:
                {
                alt82=22;
                }
                break;
            case 91:
                {
                alt82=23;
                }
                break;
            case 92:
                {
                alt82=24;
                }
                break;
            case 93:
                {
                alt82=25;
                }
                break;
            case 94:
                {
                alt82=26;
                }
                break;
            case 95:
                {
                alt82=27;
                }
                break;
            case 96:
                {
                alt82=28;
                }
                break;
            case 97:
                {
                alt82=29;
                }
                break;
            case 98:
                {
                alt82=30;
                }
                break;
            case 99:
                {
                alt82=31;
                }
                break;
            case 100:
                {
                alt82=32;
                }
                break;
            case 101:
                {
                alt82=33;
                }
                break;
            case 16:
                {
                alt82=34;
                }
                break;
            case 102:
                {
                alt82=35;
                }
                break;
            case 103:
                {
                alt82=36;
                }
                break;
            case 104:
                {
                alt82=37;
                }
                break;
            case 105:
                {
                alt82=38;
                }
                break;
            case 106:
                {
                alt82=39;
                }
                break;
            case 107:
                {
                alt82=40;
                }
                break;
            case 108:
                {
                alt82=41;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 82, 0, input);

                throw nvae;
            }

            switch (alt82) {
                case 1 :
                    // InternalBasicIQL.g:6238:3: kw= 'break'
                    {
                    kw=(Token)match(input,42,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getBreakKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalBasicIQL.g:6244:3: kw= 'case'
                    {
                    kw=(Token)match(input,39,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getCaseKeyword_1());
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalBasicIQL.g:6250:3: kw= 'class'
                    {
                    kw=(Token)match(input,17,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getClassKeyword_2());
                      		
                    }

                    }
                    break;
                case 4 :
                    // InternalBasicIQL.g:6256:3: kw= 'continue'
                    {
                    kw=(Token)match(input,43,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getContinueKeyword_3());
                      		
                    }

                    }
                    break;
                case 5 :
                    // InternalBasicIQL.g:6262:3: kw= 'default'
                    {
                    kw=(Token)match(input,38,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getDefaultKeyword_4());
                      		
                    }

                    }
                    break;
                case 6 :
                    // InternalBasicIQL.g:6268:3: kw= 'do'
                    {
                    kw=(Token)match(input,35,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getDoKeyword_5());
                      		
                    }

                    }
                    break;
                case 7 :
                    // InternalBasicIQL.g:6274:3: kw= 'else'
                    {
                    kw=(Token)match(input,33,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getElseKeyword_6());
                      		
                    }

                    }
                    break;
                case 8 :
                    // InternalBasicIQL.g:6280:3: kw= 'extends'
                    {
                    kw=(Token)match(input,18,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getExtendsKeyword_7());
                      		
                    }

                    }
                    break;
                case 9 :
                    // InternalBasicIQL.g:6286:3: kw= 'for'
                    {
                    kw=(Token)match(input,36,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getForKeyword_8());
                      		
                    }

                    }
                    break;
                case 10 :
                    // InternalBasicIQL.g:6292:3: kw= 'if'
                    {
                    kw=(Token)match(input,32,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getIfKeyword_9());
                      		
                    }

                    }
                    break;
                case 11 :
                    // InternalBasicIQL.g:6298:3: kw= 'implements'
                    {
                    kw=(Token)match(input,19,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getImplementsKeyword_10());
                      		
                    }

                    }
                    break;
                case 12 :
                    // InternalBasicIQL.g:6304:3: kw= 'instanceof'
                    {
                    kw=(Token)match(input,54,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getInstanceofKeyword_11());
                      		
                    }

                    }
                    break;
                case 13 :
                    // InternalBasicIQL.g:6310:3: kw= 'interface'
                    {
                    kw=(Token)match(input,23,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getInterfaceKeyword_12());
                      		
                    }

                    }
                    break;
                case 14 :
                    // InternalBasicIQL.g:6316:3: kw= 'new'
                    {
                    kw=(Token)match(input,68,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getNewKeyword_13());
                      		
                    }

                    }
                    break;
                case 15 :
                    // InternalBasicIQL.g:6322:3: kw= 'package'
                    {
                    kw=(Token)match(input,88,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getPackageKeyword_14());
                      		
                    }

                    }
                    break;
                case 16 :
                    // InternalBasicIQL.g:6328:3: kw= 'return'
                    {
                    kw=(Token)match(input,44,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getReturnKeyword_15());
                      		
                    }

                    }
                    break;
                case 17 :
                    // InternalBasicIQL.g:6334:3: kw= 'super'
                    {
                    kw=(Token)match(input,41,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getSuperKeyword_16());
                      		
                    }

                    }
                    break;
                case 18 :
                    // InternalBasicIQL.g:6340:3: kw= 'switch'
                    {
                    kw=(Token)match(input,37,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getSwitchKeyword_17());
                      		
                    }

                    }
                    break;
                case 19 :
                    // InternalBasicIQL.g:6346:3: kw= 'this'
                    {
                    kw=(Token)match(input,40,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getThisKeyword_18());
                      		
                    }

                    }
                    break;
                case 20 :
                    // InternalBasicIQL.g:6352:3: kw= 'while'
                    {
                    kw=(Token)match(input,34,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getWhileKeyword_19());
                      		
                    }

                    }
                    break;
                case 21 :
                    // InternalBasicIQL.g:6358:3: kw= 'abstract'
                    {
                    kw=(Token)match(input,89,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getAbstractKeyword_20());
                      		
                    }

                    }
                    break;
                case 22 :
                    // InternalBasicIQL.g:6364:3: kw= 'assert'
                    {
                    kw=(Token)match(input,90,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getAssertKeyword_21());
                      		
                    }

                    }
                    break;
                case 23 :
                    // InternalBasicIQL.g:6370:3: kw= 'catch'
                    {
                    kw=(Token)match(input,91,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getCatchKeyword_22());
                      		
                    }

                    }
                    break;
                case 24 :
                    // InternalBasicIQL.g:6376:3: kw= 'const'
                    {
                    kw=(Token)match(input,92,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getConstKeyword_23());
                      		
                    }

                    }
                    break;
                case 25 :
                    // InternalBasicIQL.g:6382:3: kw= 'enum'
                    {
                    kw=(Token)match(input,93,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getEnumKeyword_24());
                      		
                    }

                    }
                    break;
                case 26 :
                    // InternalBasicIQL.g:6388:3: kw= 'final'
                    {
                    kw=(Token)match(input,94,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getFinalKeyword_25());
                      		
                    }

                    }
                    break;
                case 27 :
                    // InternalBasicIQL.g:6394:3: kw= 'finally'
                    {
                    kw=(Token)match(input,95,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getFinallyKeyword_26());
                      		
                    }

                    }
                    break;
                case 28 :
                    // InternalBasicIQL.g:6400:3: kw= 'goto'
                    {
                    kw=(Token)match(input,96,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getGotoKeyword_27());
                      		
                    }

                    }
                    break;
                case 29 :
                    // InternalBasicIQL.g:6406:3: kw= 'import'
                    {
                    kw=(Token)match(input,97,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getImportKeyword_28());
                      		
                    }

                    }
                    break;
                case 30 :
                    // InternalBasicIQL.g:6412:3: kw= 'native'
                    {
                    kw=(Token)match(input,98,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getNativeKeyword_29());
                      		
                    }

                    }
                    break;
                case 31 :
                    // InternalBasicIQL.g:6418:3: kw= 'private'
                    {
                    kw=(Token)match(input,99,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getPrivateKeyword_30());
                      		
                    }

                    }
                    break;
                case 32 :
                    // InternalBasicIQL.g:6424:3: kw= 'protected'
                    {
                    kw=(Token)match(input,100,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getProtectedKeyword_31());
                      		
                    }

                    }
                    break;
                case 33 :
                    // InternalBasicIQL.g:6430:3: kw= 'public'
                    {
                    kw=(Token)match(input,101,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getPublicKeyword_32());
                      		
                    }

                    }
                    break;
                case 34 :
                    // InternalBasicIQL.g:6436:3: kw= 'static'
                    {
                    kw=(Token)match(input,16,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getStaticKeyword_33());
                      		
                    }

                    }
                    break;
                case 35 :
                    // InternalBasicIQL.g:6442:3: kw= 'synchronized'
                    {
                    kw=(Token)match(input,102,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getSynchronizedKeyword_34());
                      		
                    }

                    }
                    break;
                case 36 :
                    // InternalBasicIQL.g:6448:3: kw= 'throw'
                    {
                    kw=(Token)match(input,103,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getThrowKeyword_35());
                      		
                    }

                    }
                    break;
                case 37 :
                    // InternalBasicIQL.g:6454:3: kw= 'throws'
                    {
                    kw=(Token)match(input,104,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getThrowsKeyword_36());
                      		
                    }

                    }
                    break;
                case 38 :
                    // InternalBasicIQL.g:6460:3: kw= 'transient'
                    {
                    kw=(Token)match(input,105,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getTransientKeyword_37());
                      		
                    }

                    }
                    break;
                case 39 :
                    // InternalBasicIQL.g:6466:3: kw= 'try'
                    {
                    kw=(Token)match(input,106,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getTryKeyword_38());
                      		
                    }

                    }
                    break;
                case 40 :
                    // InternalBasicIQL.g:6472:3: kw= 'volatile'
                    {
                    kw=(Token)match(input,107,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getVolatileKeyword_39());
                      		
                    }

                    }
                    break;
                case 41 :
                    // InternalBasicIQL.g:6478:3: kw= 'strictfp'
                    {
                    kw=(Token)match(input,108,FOLLOW_2); if (state.failed) return current;
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
    // InternalBasicIQL.g:6487:1: entryRuleBOOLEAN returns [String current=null] : iv_ruleBOOLEAN= ruleBOOLEAN EOF ;
    public final String entryRuleBOOLEAN() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleBOOLEAN = null;


        try {
            // InternalBasicIQL.g:6487:47: (iv_ruleBOOLEAN= ruleBOOLEAN EOF )
            // InternalBasicIQL.g:6488:2: iv_ruleBOOLEAN= ruleBOOLEAN EOF
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
    // InternalBasicIQL.g:6494:1: ruleBOOLEAN returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'true' | kw= 'false' ) ;
    public final AntlrDatatypeRuleToken ruleBOOLEAN() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalBasicIQL.g:6500:2: ( (kw= 'true' | kw= 'false' ) )
            // InternalBasicIQL.g:6501:2: (kw= 'true' | kw= 'false' )
            {
            // InternalBasicIQL.g:6501:2: (kw= 'true' | kw= 'false' )
            int alt83=2;
            int LA83_0 = input.LA(1);

            if ( (LA83_0==109) ) {
                alt83=1;
            }
            else if ( (LA83_0==110) ) {
                alt83=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 83, 0, input);

                throw nvae;
            }
            switch (alt83) {
                case 1 :
                    // InternalBasicIQL.g:6502:3: kw= 'true'
                    {
                    kw=(Token)match(input,109,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getBOOLEANAccess().getTrueKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalBasicIQL.g:6508:3: kw= 'false'
                    {
                    kw=(Token)match(input,110,FOLLOW_2); if (state.failed) return current;
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

    // $ANTLR start synpred1_InternalBasicIQL
    public final void synpred1_InternalBasicIQL_fragment() throws RecognitionException {   
        // InternalBasicIQL.g:2546:5: ( 'else' )
        // InternalBasicIQL.g:2546:6: 'else'
        {
        match(input,33,FOLLOW_2); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred1_InternalBasicIQL

    // $ANTLR start synpred2_InternalBasicIQL
    public final void synpred2_InternalBasicIQL_fragment() throws RecognitionException {   
        // InternalBasicIQL.g:3533:5: ( ( () ( ( ruleOpAssign ) ) ) )
        // InternalBasicIQL.g:3533:6: ( () ( ( ruleOpAssign ) ) )
        {
        // InternalBasicIQL.g:3533:6: ( () ( ( ruleOpAssign ) ) )
        // InternalBasicIQL.g:3534:6: () ( ( ruleOpAssign ) )
        {
        // InternalBasicIQL.g:3534:6: ()
        // InternalBasicIQL.g:3535:6: 
        {
        }

        // InternalBasicIQL.g:3536:6: ( ( ruleOpAssign ) )
        // InternalBasicIQL.g:3537:7: ( ruleOpAssign )
        {
        // InternalBasicIQL.g:3537:7: ( ruleOpAssign )
        // InternalBasicIQL.g:3538:8: ruleOpAssign
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
    // $ANTLR end synpred2_InternalBasicIQL

    // $ANTLR start synpred3_InternalBasicIQL
    public final void synpred3_InternalBasicIQL_fragment() throws RecognitionException {   
        // InternalBasicIQL.g:3675:5: ( ( () ( ( ruleOpLogicalOr ) ) ) )
        // InternalBasicIQL.g:3675:6: ( () ( ( ruleOpLogicalOr ) ) )
        {
        // InternalBasicIQL.g:3675:6: ( () ( ( ruleOpLogicalOr ) ) )
        // InternalBasicIQL.g:3676:6: () ( ( ruleOpLogicalOr ) )
        {
        // InternalBasicIQL.g:3676:6: ()
        // InternalBasicIQL.g:3677:6: 
        {
        }

        // InternalBasicIQL.g:3678:6: ( ( ruleOpLogicalOr ) )
        // InternalBasicIQL.g:3679:7: ( ruleOpLogicalOr )
        {
        // InternalBasicIQL.g:3679:7: ( ruleOpLogicalOr )
        // InternalBasicIQL.g:3680:8: ruleOpLogicalOr
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
    // $ANTLR end synpred3_InternalBasicIQL

    // $ANTLR start synpred4_InternalBasicIQL
    public final void synpred4_InternalBasicIQL_fragment() throws RecognitionException {   
        // InternalBasicIQL.g:3785:5: ( ( () ( ( ruleOpLogicalAnd ) ) ) )
        // InternalBasicIQL.g:3785:6: ( () ( ( ruleOpLogicalAnd ) ) )
        {
        // InternalBasicIQL.g:3785:6: ( () ( ( ruleOpLogicalAnd ) ) )
        // InternalBasicIQL.g:3786:6: () ( ( ruleOpLogicalAnd ) )
        {
        // InternalBasicIQL.g:3786:6: ()
        // InternalBasicIQL.g:3787:6: 
        {
        }

        // InternalBasicIQL.g:3788:6: ( ( ruleOpLogicalAnd ) )
        // InternalBasicIQL.g:3789:7: ( ruleOpLogicalAnd )
        {
        // InternalBasicIQL.g:3789:7: ( ruleOpLogicalAnd )
        // InternalBasicIQL.g:3790:8: ruleOpLogicalAnd
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
    // $ANTLR end synpred4_InternalBasicIQL

    // $ANTLR start synpred5_InternalBasicIQL
    public final void synpred5_InternalBasicIQL_fragment() throws RecognitionException {   
        // InternalBasicIQL.g:3895:5: ( ( () ( ( ruleOpEquality ) ) ) )
        // InternalBasicIQL.g:3895:6: ( () ( ( ruleOpEquality ) ) )
        {
        // InternalBasicIQL.g:3895:6: ( () ( ( ruleOpEquality ) ) )
        // InternalBasicIQL.g:3896:6: () ( ( ruleOpEquality ) )
        {
        // InternalBasicIQL.g:3896:6: ()
        // InternalBasicIQL.g:3897:6: 
        {
        }

        // InternalBasicIQL.g:3898:6: ( ( ruleOpEquality ) )
        // InternalBasicIQL.g:3899:7: ( ruleOpEquality )
        {
        // InternalBasicIQL.g:3899:7: ( ruleOpEquality )
        // InternalBasicIQL.g:3900:8: ruleOpEquality
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
    // $ANTLR end synpred5_InternalBasicIQL

    // $ANTLR start synpred6_InternalBasicIQL
    public final void synpred6_InternalBasicIQL_fragment() throws RecognitionException {   
        // InternalBasicIQL.g:4014:6: ( ( () 'instanceof' ) )
        // InternalBasicIQL.g:4014:7: ( () 'instanceof' )
        {
        // InternalBasicIQL.g:4014:7: ( () 'instanceof' )
        // InternalBasicIQL.g:4015:7: () 'instanceof'
        {
        // InternalBasicIQL.g:4015:7: ()
        // InternalBasicIQL.g:4016:7: 
        {
        }

        match(input,54,FOLLOW_2); if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred6_InternalBasicIQL

    // $ANTLR start synpred7_InternalBasicIQL
    public final void synpred7_InternalBasicIQL_fragment() throws RecognitionException {   
        // InternalBasicIQL.g:4057:6: ( ( () ( ( ruleOpRelational ) ) ) )
        // InternalBasicIQL.g:4057:7: ( () ( ( ruleOpRelational ) ) )
        {
        // InternalBasicIQL.g:4057:7: ( () ( ( ruleOpRelational ) ) )
        // InternalBasicIQL.g:4058:7: () ( ( ruleOpRelational ) )
        {
        // InternalBasicIQL.g:4058:7: ()
        // InternalBasicIQL.g:4059:7: 
        {
        }

        // InternalBasicIQL.g:4060:7: ( ( ruleOpRelational ) )
        // InternalBasicIQL.g:4061:8: ( ruleOpRelational )
        {
        // InternalBasicIQL.g:4061:8: ( ruleOpRelational )
        // InternalBasicIQL.g:4062:9: ruleOpRelational
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
    // $ANTLR end synpred7_InternalBasicIQL

    // $ANTLR start synpred8_InternalBasicIQL
    public final void synpred8_InternalBasicIQL_fragment() throws RecognitionException {   
        // InternalBasicIQL.g:4188:5: ( ( () ( ( ruleOpAdd ) ) ) )
        // InternalBasicIQL.g:4188:6: ( () ( ( ruleOpAdd ) ) )
        {
        // InternalBasicIQL.g:4188:6: ( () ( ( ruleOpAdd ) ) )
        // InternalBasicIQL.g:4189:6: () ( ( ruleOpAdd ) )
        {
        // InternalBasicIQL.g:4189:6: ()
        // InternalBasicIQL.g:4190:6: 
        {
        }

        // InternalBasicIQL.g:4191:6: ( ( ruleOpAdd ) )
        // InternalBasicIQL.g:4192:7: ( ruleOpAdd )
        {
        // InternalBasicIQL.g:4192:7: ( ruleOpAdd )
        // InternalBasicIQL.g:4193:8: ruleOpAdd
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
    // $ANTLR end synpred8_InternalBasicIQL

    // $ANTLR start synpred9_InternalBasicIQL
    public final void synpred9_InternalBasicIQL_fragment() throws RecognitionException {   
        // InternalBasicIQL.g:4306:5: ( ( () ( ( ruleOpMulti ) ) ) )
        // InternalBasicIQL.g:4306:6: ( () ( ( ruleOpMulti ) ) )
        {
        // InternalBasicIQL.g:4306:6: ( () ( ( ruleOpMulti ) ) )
        // InternalBasicIQL.g:4307:6: () ( ( ruleOpMulti ) )
        {
        // InternalBasicIQL.g:4307:6: ()
        // InternalBasicIQL.g:4308:6: 
        {
        }

        // InternalBasicIQL.g:4309:6: ( ( ruleOpMulti ) )
        // InternalBasicIQL.g:4310:7: ( ruleOpMulti )
        {
        // InternalBasicIQL.g:4310:7: ( ruleOpMulti )
        // InternalBasicIQL.g:4311:8: ruleOpMulti
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
    // $ANTLR end synpred9_InternalBasicIQL

    // $ANTLR start synpred10_InternalBasicIQL
    public final void synpred10_InternalBasicIQL_fragment() throws RecognitionException {   
        // InternalBasicIQL.g:4572:5: ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )
        // InternalBasicIQL.g:4572:6: ( () '(' ( ( ruleJvmTypeReference ) ) ')' )
        {
        // InternalBasicIQL.g:4572:6: ( () '(' ( ( ruleJvmTypeReference ) ) ')' )
        // InternalBasicIQL.g:4573:6: () '(' ( ( ruleJvmTypeReference ) ) ')'
        {
        // InternalBasicIQL.g:4573:6: ()
        // InternalBasicIQL.g:4574:6: 
        {
        }

        match(input,27,FOLLOW_3); if (state.failed) return ;
        // InternalBasicIQL.g:4576:6: ( ( ruleJvmTypeReference ) )
        // InternalBasicIQL.g:4577:7: ( ruleJvmTypeReference )
        {
        // InternalBasicIQL.g:4577:7: ( ruleJvmTypeReference )
        // InternalBasicIQL.g:4578:8: ruleJvmTypeReference
        {
        pushFollow(FOLLOW_36);
        ruleJvmTypeReference();

        state._fsp--;
        if (state.failed) return ;

        }


        }

        match(input,28,FOLLOW_2); if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred10_InternalBasicIQL

    // $ANTLR start synpred11_InternalBasicIQL
    public final void synpred11_InternalBasicIQL_fragment() throws RecognitionException {   
        // InternalBasicIQL.g:4652:5: ( ( () ( ( ruleOpPostfix ) ) ) )
        // InternalBasicIQL.g:4652:6: ( () ( ( ruleOpPostfix ) ) )
        {
        // InternalBasicIQL.g:4652:6: ( () ( ( ruleOpPostfix ) ) )
        // InternalBasicIQL.g:4653:6: () ( ( ruleOpPostfix ) )
        {
        // InternalBasicIQL.g:4653:6: ()
        // InternalBasicIQL.g:4654:6: 
        {
        }

        // InternalBasicIQL.g:4655:6: ( ( ruleOpPostfix ) )
        // InternalBasicIQL.g:4656:7: ( ruleOpPostfix )
        {
        // InternalBasicIQL.g:4656:7: ( ruleOpPostfix )
        // InternalBasicIQL.g:4657:8: ruleOpPostfix
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
    // $ANTLR end synpred11_InternalBasicIQL

    // $ANTLR start synpred12_InternalBasicIQL
    public final void synpred12_InternalBasicIQL_fragment() throws RecognitionException {   
        // InternalBasicIQL.g:4833:5: ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )
        // InternalBasicIQL.g:4833:6: ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' )
        {
        // InternalBasicIQL.g:4833:6: ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' )
        // InternalBasicIQL.g:4834:6: () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']'
        {
        // InternalBasicIQL.g:4834:6: ()
        // InternalBasicIQL.g:4835:6: 
        {
        }

        match(input,24,FOLLOW_32); if (state.failed) return ;
        // InternalBasicIQL.g:4837:6: ( ( ruleIQLExpression ) )
        // InternalBasicIQL.g:4838:7: ( ruleIQLExpression )
        {
        // InternalBasicIQL.g:4838:7: ( ruleIQLExpression )
        // InternalBasicIQL.g:4839:8: ruleIQLExpression
        {
        pushFollow(FOLLOW_27);
        ruleIQLExpression();

        state._fsp--;
        if (state.failed) return ;

        }


        }

        // InternalBasicIQL.g:4842:6: ( ',' ( ( ruleIQLExpression ) ) )*
        loop84:
        do {
            int alt84=2;
            int LA84_0 = input.LA(1);

            if ( (LA84_0==20) ) {
                alt84=1;
            }


            switch (alt84) {
        	case 1 :
        	    // InternalBasicIQL.g:4843:7: ',' ( ( ruleIQLExpression ) )
        	    {
        	    match(input,20,FOLLOW_32); if (state.failed) return ;
        	    // InternalBasicIQL.g:4844:7: ( ( ruleIQLExpression ) )
        	    // InternalBasicIQL.g:4845:8: ( ruleIQLExpression )
        	    {
        	    // InternalBasicIQL.g:4845:8: ( ruleIQLExpression )
        	    // InternalBasicIQL.g:4846:9: ruleIQLExpression
        	    {
        	    pushFollow(FOLLOW_27);
        	    ruleIQLExpression();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }


        	    }


        	    }
        	    break;

        	default :
        	    break loop84;
            }
        } while (true);

        match(input,25,FOLLOW_2); if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred12_InternalBasicIQL

    // $ANTLR start synpred13_InternalBasicIQL
    public final void synpred13_InternalBasicIQL_fragment() throws RecognitionException {   
        // InternalBasicIQL.g:4918:6: ( ( () '.' ) )
        // InternalBasicIQL.g:4918:7: ( () '.' )
        {
        // InternalBasicIQL.g:4918:7: ( () '.' )
        // InternalBasicIQL.g:4919:7: () '.'
        {
        // InternalBasicIQL.g:4919:7: ()
        // InternalBasicIQL.g:4920:7: 
        {
        }

        match(input,67,FOLLOW_2); if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred13_InternalBasicIQL

    // $ANTLR start synpred14_InternalBasicIQL
    public final void synpred14_InternalBasicIQL_fragment() throws RecognitionException {   
        // InternalBasicIQL.g:5478:4: ( ruleIQLLiteralExpressionList )
        // InternalBasicIQL.g:5478:5: ruleIQLLiteralExpressionList
        {
        pushFollow(FOLLOW_2);
        ruleIQLLiteralExpressionList();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred14_InternalBasicIQL

    // Delegated rules

    public final boolean synpred7_InternalBasicIQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred7_InternalBasicIQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred5_InternalBasicIQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_InternalBasicIQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_InternalBasicIQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_InternalBasicIQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_InternalBasicIQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_InternalBasicIQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred9_InternalBasicIQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred9_InternalBasicIQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred11_InternalBasicIQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred11_InternalBasicIQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred13_InternalBasicIQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred13_InternalBasicIQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred6_InternalBasicIQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred6_InternalBasicIQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred4_InternalBasicIQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_InternalBasicIQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_InternalBasicIQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_InternalBasicIQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred10_InternalBasicIQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred10_InternalBasicIQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred8_InternalBasicIQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred8_InternalBasicIQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred12_InternalBasicIQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred12_InternalBasicIQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred14_InternalBasicIQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred14_InternalBasicIQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA17 dfa17 = new DFA17(this);
    protected DFA40 dfa40 = new DFA40(this);
    protected DFA62 dfa62 = new DFA62(this);
    protected DFA72 dfa72 = new DFA72(this);
    protected DFA74 dfa74 = new DFA74(this);
    static final String dfa_1s = "\6\uffff";
    static final String dfa_2s = "\1\uffff\1\4\3\uffff\1\4";
    static final String dfa_3s = "\3\4\2\uffff\1\4";
    static final String dfa_4s = "\1\4\1\107\1\4\2\uffff\1\107";
    static final String dfa_5s = "\3\uffff\1\2\1\1\1\uffff";
    static final String dfa_6s = "\6\uffff}>";
    static final String[] dfa_7s = {
            "\1\1",
            "\1\4\11\uffff\1\4\4\uffff\4\4\1\uffff\1\3\1\4\2\uffff\3\4\16\uffff\16\4\14\uffff\1\2",
            "\1\5",
            "",
            "",
            "\1\4\11\uffff\1\4\4\uffff\4\4\1\uffff\1\3\1\4\2\uffff\3\4\16\uffff\16\4\14\uffff\1\2"
    };

    static final short[] dfa_1 = DFA.unpackEncodedString(dfa_1s);
    static final short[] dfa_2 = DFA.unpackEncodedString(dfa_2s);
    static final char[] dfa_3 = DFA.unpackEncodedStringToUnsignedChars(dfa_3s);
    static final char[] dfa_4 = DFA.unpackEncodedStringToUnsignedChars(dfa_4s);
    static final short[] dfa_5 = DFA.unpackEncodedString(dfa_5s);
    static final short[] dfa_6 = DFA.unpackEncodedString(dfa_6s);
    static final short[][] dfa_7 = unpackEncodedStringArray(dfa_7s);

    class DFA17 extends DFA {

        public DFA17(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 17;
            this.eot = dfa_1;
            this.eof = dfa_2;
            this.min = dfa_3;
            this.max = dfa_4;
            this.accept = dfa_5;
            this.special = dfa_6;
            this.transition = dfa_7;
        }
        public String getDescription() {
            return "748:2: (this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef | this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef )";
        }
    }
    static final String dfa_8s = "\35\uffff";
    static final String dfa_9s = "\1\4\2\uffff\1\4\2\16\3\uffff\1\33\5\uffff\1\4\1\uffff\1\4\1\uffff\4\4\1\35\1\31\1\4\2\uffff\1\4";
    static final String dfa_10s = "\1\156\2\uffff\1\107\2\103\3\uffff\1\33\5\uffff\1\4\1\uffff\1\156\1\uffff\1\4\2\107\1\4\1\36\1\31\1\107\2\uffff\1\30";
    static final String dfa_11s = "\1\uffff\1\1\1\2\3\uffff\1\3\1\4\1\5\1\uffff\1\10\1\12\1\13\1\14\1\16\1\uffff\1\11\1\uffff\1\15\7\uffff\1\6\1\7\1\uffff";
    static final String dfa_12s = "\35\uffff}>";
    static final String[] dfa_13s = {
            "\1\3\4\2\14\uffff\1\1\2\uffff\1\2\2\uffff\1\2\3\uffff\1\2\1\6\1\uffff\1\7\1\10\1\11\1\12\2\uffff\1\4\1\5\1\13\1\14\1\15\16\uffff\2\2\3\uffff\3\2\1\uffff\2\2\2\uffff\1\16\44\uffff\2\2",
            "",
            "",
            "\1\20\11\uffff\1\2\11\uffff\1\21\2\uffff\1\2\2\uffff\1\2\16\uffff\23\2\1\uffff\3\2\3\uffff\1\17",
            "\1\2\11\uffff\1\2\2\uffff\1\22\2\uffff\1\2\16\uffff\23\2\1\uffff\3\2",
            "\1\2\11\uffff\1\2\2\uffff\1\22\2\uffff\1\2\16\uffff\23\2\1\uffff\3\2",
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
            "",
            "\5\2\17\uffff\1\2\1\20\1\uffff\1\2\3\uffff\1\2\10\uffff\2\2\21\uffff\2\2\3\uffff\3\2\1\uffff\2\2\47\uffff\2\2",
            "",
            "\1\25",
            "\1\20\11\uffff\1\2\11\uffff\1\21\2\uffff\1\2\2\uffff\1\2\16\uffff\23\2\1\uffff\3\2\3\uffff\1\17",
            "\1\27\23\uffff\1\30\56\uffff\1\26",
            "\1\31",
            "\1\33\1\32",
            "\1\34",
            "\1\27\23\uffff\1\30\56\uffff\1\26",
            "",
            "",
            "\1\27\23\uffff\1\30"
    };

    static final short[] dfa_8 = DFA.unpackEncodedString(dfa_8s);
    static final char[] dfa_9 = DFA.unpackEncodedStringToUnsignedChars(dfa_9s);
    static final char[] dfa_10 = DFA.unpackEncodedStringToUnsignedChars(dfa_10s);
    static final short[] dfa_11 = DFA.unpackEncodedString(dfa_11s);
    static final short[] dfa_12 = DFA.unpackEncodedString(dfa_12s);
    static final short[][] dfa_13 = unpackEncodedStringArray(dfa_13s);

    class DFA40 extends DFA {

        public DFA40(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 40;
            this.eot = dfa_8;
            this.eof = dfa_8;
            this.min = dfa_9;
            this.max = dfa_10;
            this.accept = dfa_11;
            this.special = dfa_12;
            this.transition = dfa_13;
        }
        public String getDescription() {
            return "2244:2: (this_IQLStatementBlock_0= ruleIQLStatementBlock | this_IQLExpressionStatement_1= ruleIQLExpressionStatement | this_IQLIfStatement_2= ruleIQLIfStatement | this_IQLWhileStatement_3= ruleIQLWhileStatement | this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement | this_IQLForStatement_5= ruleIQLForStatement | this_IQLForEachStatement_6= ruleIQLForEachStatement | this_IQLSwitchStatement_7= ruleIQLSwitchStatement | this_IQLVariableStatement_8= ruleIQLVariableStatement | this_IQLBreakStatement_9= ruleIQLBreakStatement | this_IQLContinueStatement_10= ruleIQLContinueStatement | this_IQLReturnStatement_11= ruleIQLReturnStatement | this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement | this_IQLJavaStatement_13= ruleIQLJavaStatement )";
        }
    }
    static final String dfa_14s = "\25\uffff";
    static final String dfa_15s = "\1\4\5\uffff\1\0\16\uffff";
    static final String dfa_16s = "\1\156\5\uffff\1\0\16\uffff";
    static final String dfa_17s = "\1\uffff\1\1\1\uffff\1\2\1\3\2\uffff\1\5\14\uffff\1\4";
    static final String dfa_18s = "\6\uffff\1\0\16\uffff}>";
    static final String[] dfa_19s = {
            "\5\7\17\uffff\1\7\2\uffff\1\6\3\uffff\1\7\10\uffff\2\7\21\uffff\2\1\3\uffff\1\3\2\4\1\uffff\2\7\47\uffff\2\7",
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

    static final short[] dfa_14 = DFA.unpackEncodedString(dfa_14s);
    static final char[] dfa_15 = DFA.unpackEncodedStringToUnsignedChars(dfa_15s);
    static final char[] dfa_16 = DFA.unpackEncodedStringToUnsignedChars(dfa_16s);
    static final short[] dfa_17 = DFA.unpackEncodedString(dfa_17s);
    static final short[] dfa_18 = DFA.unpackEncodedString(dfa_18s);
    static final short[][] dfa_19 = unpackEncodedStringArray(dfa_19s);

    class DFA62 extends DFA {

        public DFA62(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 62;
            this.eot = dfa_14;
            this.eof = dfa_14;
            this.min = dfa_15;
            this.max = dfa_16;
            this.accept = dfa_17;
            this.special = dfa_18;
            this.transition = dfa_19;
        }
        public String getDescription() {
            return "4419:2: ( ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) ) | ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) ) | (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? ) )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA62_6 = input.LA(1);

                         
                        int index62_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_InternalBasicIQL()) ) {s = 20;}

                        else if ( (true) ) {s = 7;}

                         
                        input.seek(index62_6);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 62, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String dfa_20s = "\1\4\1\25\1\4\2\uffff\1\25";
    static final String dfa_21s = "\3\uffff\1\1\1\2\1\uffff";
    static final String[] dfa_22s = {
            "\1\1",
            "\1\4\2\uffff\1\3\2\uffff\1\4\53\uffff\1\2",
            "\1\5",
            "",
            "",
            "\1\4\2\uffff\1\3\2\uffff\1\4\53\uffff\1\2"
    };
    static final char[] dfa_20 = DFA.unpackEncodedStringToUnsignedChars(dfa_20s);
    static final short[] dfa_21 = DFA.unpackEncodedString(dfa_21s);
    static final short[][] dfa_22 = unpackEncodedStringArray(dfa_22s);

    class DFA72 extends DFA {

        public DFA72(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 72;
            this.eot = dfa_1;
            this.eof = dfa_1;
            this.min = dfa_20;
            this.max = dfa_4;
            this.accept = dfa_21;
            this.special = dfa_6;
            this.transition = dfa_22;
        }
        public String getDescription() {
            return "5151:4: ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) )";
        }
    }
    static final String dfa_23s = "\14\uffff";
    static final String dfa_24s = "\1\5\10\uffff\1\0\2\uffff";
    static final String dfa_25s = "\1\156\10\uffff\1\0\2\uffff";
    static final String dfa_26s = "\1\uffff\1\1\1\2\1\3\1\4\1\uffff\1\5\1\6\1\7\1\uffff\1\10\1\11";
    static final String dfa_27s = "\11\uffff\1\0\2\uffff}>";
    static final String[] dfa_28s = {
            "\1\1\1\2\1\3\1\6\17\uffff\1\11\6\uffff\1\10\45\uffff\1\7\47\uffff\2\4",
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

    static final short[] dfa_23 = DFA.unpackEncodedString(dfa_23s);
    static final char[] dfa_24 = DFA.unpackEncodedStringToUnsignedChars(dfa_24s);
    static final char[] dfa_25 = DFA.unpackEncodedStringToUnsignedChars(dfa_25s);
    static final short[] dfa_26 = DFA.unpackEncodedString(dfa_26s);
    static final short[] dfa_27 = DFA.unpackEncodedString(dfa_27s);
    static final short[][] dfa_28 = unpackEncodedStringArray(dfa_28s);

    class DFA74 extends DFA {

        public DFA74(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 74;
            this.eot = dfa_23;
            this.eof = dfa_23;
            this.min = dfa_24;
            this.max = dfa_25;
            this.accept = dfa_26;
            this.special = dfa_27;
            this.transition = dfa_28;
        }
        public String getDescription() {
            return "5284:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_RANGE ) ) ) | ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' ) | ( () otherlv_15= 'null' ) | ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList ) | this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA74_9 = input.LA(1);

                         
                        int index74_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_InternalBasicIQL()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index74_9);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 74, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000828002L,0x0000000000000100L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000820002L,0x0000000000000100L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000820000L,0x0000000000000100L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000010010L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x00000000002C0000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000280000L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000000300000L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000004400010L,0x0000000000000100L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000000240000L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000048204000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000028200000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000010000010L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000010100000L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000020004000L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x00000000812000F0L,0x0000600000000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x00000000832000F0L,0x0000600000000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000002100000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x00000000816000F0L,0x0000600000000000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000000500000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x18000300890001F0L,0x0000600000000037L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x18000300990001F0L,0x0000600000000037L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000400010L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x18001F3DA96001F0L,0x0000600000000137L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x18001F3DA92001F0L,0x0000600000000137L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x000000C000400000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x18001F3DA92001F2L,0x0000600000000137L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0000000048200000L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x18000300890041F0L,0x0000600000000037L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0003E00040000002L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0030000000000002L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x07C0000000000002L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x1800000000000002L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0xE000000000000002L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000006L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0000000001000002L,0x0000000000000008L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0000000008200000L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x180003008B0001F0L,0x0000600000000037L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000040L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0xFFFFFFFFFBFF46F0L,0x00007FFFFFFFFE1FL});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0xFFFFFFFFFBFF46F2L,0x00007FFFFFFFFC1FL});

}