package de.uniol.inf.is.odysseus.iql.odl.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import de.uniol.inf.is.odysseus.iql.odl.services.ODLGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
@SuppressWarnings("all")
public class InternalODLParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_WS", "RULE_DOUBLE", "RULE_STRING", "RULE_INT", "RULE_ANY_OTHER", "RULE_RANGE", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "'operator'", "'('", "')'", "'{'", "'}'", "'optional'", "'parameter'", "';'", "'on'", "'validate'", "'override'", "'ao'", "'po'", "','", "':'", "'+'", "'+='", "'-'", "'-='", "'*'", "'*='", "'/'", "'/='", "'%'", "'%='", "'++'", "'--'", "'>'", "'>='", "'<'", "'<='", "'!'", "'!='", "'&&'", "'||'", "'=='", "'='", "'~'", "'?:'", "'|'", "'|='", "'^'", "'^='", "'&'", "'&='", "'>>'", "'>>='", "'<<'", "'<<='", "'>>>'", "'>>>='", "'['", "']'", "'.'", "'null'", "'use'", "'static'", "'class'", "'extends'", "'implements'", "'interface'", "'if'", "'else'", "'while'", "'do'", "'for'", "'switch'", "'default'", "'case'", "'this'", "'super'", "'break'", "'continue'", "'return'", "'instanceof'", "'new'", "'class('", "'::*'", "'::'", "'$*'", "'*$'", "'package'", "'abstract'", "'assert'", "'catch'", "'const'", "'enum'", "'final'", "'finally'", "'goto'", "'import'", "'native'", "'private'", "'protected'", "'public'", "'synchronized'", "'throw'", "'throws'", "'transient'", "'try'", "'volatile'", "'strictfp'", "'true'", "'false'"
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
    public static final int RULE_INT=8;
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
    public static final int T__115=115;
    public static final int EOF=-1;
    public static final int T__74=74;
    public static final int T__114=114;
    public static final int T__75=75;
    public static final int T__76=76;
    public static final int T__116=116;
    public static final int T__80=80;
    public static final int T__111=111;
    public static final int T__81=81;
    public static final int T__110=110;
    public static final int T__82=82;
    public static final int T__113=113;
    public static final int T__83=83;
    public static final int T__112=112;
    public static final int RULE_WS=5;
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


        public InternalODLParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalODLParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalODLParser.tokenNames; }
    public String getGrammarFileName() { return "InternalODL.g"; }



     	private ODLGrammarAccess grammarAccess;

        public InternalODLParser(TokenStream input, ODLGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "ODLModel";
       	}

       	@Override
       	protected ODLGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleODLModel"
    // InternalODL.g:64:1: entryRuleODLModel returns [EObject current=null] : iv_ruleODLModel= ruleODLModel EOF ;
    public final EObject entryRuleODLModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleODLModel = null;


        try {
            // InternalODL.g:64:49: (iv_ruleODLModel= ruleODLModel EOF )
            // InternalODL.g:65:2: iv_ruleODLModel= ruleODLModel EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getODLModelRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleODLModel=ruleODLModel();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleODLModel; 
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
    // $ANTLR end "entryRuleODLModel"


    // $ANTLR start "ruleODLModel"
    // InternalODL.g:71:1: ruleODLModel returns [EObject current=null] : ( ( (lv_namespaces_0_0= ruleIQLNamespace ) )* ( (lv_elements_1_0= ruleODLModelElement ) )* ) ;
    public final EObject ruleODLModel() throws RecognitionException {
        EObject current = null;

        EObject lv_namespaces_0_0 = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalODL.g:77:2: ( ( ( (lv_namespaces_0_0= ruleIQLNamespace ) )* ( (lv_elements_1_0= ruleODLModelElement ) )* ) )
            // InternalODL.g:78:2: ( ( (lv_namespaces_0_0= ruleIQLNamespace ) )* ( (lv_elements_1_0= ruleODLModelElement ) )* )
            {
            // InternalODL.g:78:2: ( ( (lv_namespaces_0_0= ruleIQLNamespace ) )* ( (lv_elements_1_0= ruleODLModelElement ) )* )
            // InternalODL.g:79:3: ( (lv_namespaces_0_0= ruleIQLNamespace ) )* ( (lv_elements_1_0= ruleODLModelElement ) )*
            {
            // InternalODL.g:79:3: ( (lv_namespaces_0_0= ruleIQLNamespace ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==68) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalODL.g:80:4: (lv_namespaces_0_0= ruleIQLNamespace )
            	    {
            	    // InternalODL.g:80:4: (lv_namespaces_0_0= ruleIQLNamespace )
            	    // InternalODL.g:81:5: lv_namespaces_0_0= ruleIQLNamespace
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getODLModelAccess().getNamespacesIQLNamespaceParserRuleCall_0_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_3);
            	    lv_namespaces_0_0=ruleIQLNamespace();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getODLModelRule());
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

            // InternalODL.g:98:3: ( (lv_elements_1_0= ruleODLModelElement ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==13||LA2_0==70||LA2_0==73||LA2_0==92) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalODL.g:99:4: (lv_elements_1_0= ruleODLModelElement )
            	    {
            	    // InternalODL.g:99:4: (lv_elements_1_0= ruleODLModelElement )
            	    // InternalODL.g:100:5: lv_elements_1_0= ruleODLModelElement
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getODLModelAccess().getElementsODLModelElementParserRuleCall_1_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_4);
            	    lv_elements_1_0=ruleODLModelElement();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getODLModelRule());
            	      					}
            	      					add(
            	      						current,
            	      						"elements",
            	      						lv_elements_1_0,
            	      						"de.uniol.inf.is.odysseus.iql.odl.ODL.ODLModelElement");
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
    // $ANTLR end "ruleODLModel"


    // $ANTLR start "entryRuleODLModelElement"
    // InternalODL.g:121:1: entryRuleODLModelElement returns [EObject current=null] : iv_ruleODLModelElement= ruleODLModelElement EOF ;
    public final EObject entryRuleODLModelElement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleODLModelElement = null;


        try {
            // InternalODL.g:121:56: (iv_ruleODLModelElement= ruleODLModelElement EOF )
            // InternalODL.g:122:2: iv_ruleODLModelElement= ruleODLModelElement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getODLModelElementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleODLModelElement=ruleODLModelElement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleODLModelElement; 
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
    // $ANTLR end "entryRuleODLModelElement"


    // $ANTLR start "ruleODLModelElement"
    // InternalODL.g:128:1: ruleODLModelElement returns [EObject current=null] : ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleODLOperator ) ) ) ) ;
    public final EObject ruleODLModelElement() throws RecognitionException {
        EObject current = null;

        EObject lv_javametadata_0_0 = null;

        EObject lv_inner_1_1 = null;

        EObject lv_inner_1_2 = null;

        EObject lv_inner_1_3 = null;



        	enterRule();

        try {
            // InternalODL.g:134:2: ( ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleODLOperator ) ) ) ) )
            // InternalODL.g:135:2: ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleODLOperator ) ) ) )
            {
            // InternalODL.g:135:2: ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleODLOperator ) ) ) )
            // InternalODL.g:136:3: ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleODLOperator ) ) )
            {
            // InternalODL.g:136:3: ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==92) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalODL.g:137:4: (lv_javametadata_0_0= ruleIQLJavaMetadata )
            	    {
            	    // InternalODL.g:137:4: (lv_javametadata_0_0= ruleIQLJavaMetadata )
            	    // InternalODL.g:138:5: lv_javametadata_0_0= ruleIQLJavaMetadata
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getODLModelElementAccess().getJavametadataIQLJavaMetadataParserRuleCall_0_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_5);
            	    lv_javametadata_0_0=ruleIQLJavaMetadata();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getODLModelElementRule());
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

            // InternalODL.g:155:3: ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleODLOperator ) ) )
            // InternalODL.g:156:4: ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleODLOperator ) )
            {
            // InternalODL.g:156:4: ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleODLOperator ) )
            // InternalODL.g:157:5: (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleODLOperator )
            {
            // InternalODL.g:157:5: (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface | lv_inner_1_3= ruleODLOperator )
            int alt4=3;
            switch ( input.LA(1) ) {
            case 70:
                {
                alt4=1;
                }
                break;
            case 73:
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
                    // InternalODL.g:158:6: lv_inner_1_1= ruleIQLClass
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getODLModelElementAccess().getInnerIQLClassParserRuleCall_1_0_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_inner_1_1=ruleIQLClass();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getODLModelElementRule());
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
                    // InternalODL.g:174:6: lv_inner_1_2= ruleIQLInterface
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getODLModelElementAccess().getInnerIQLInterfaceParserRuleCall_1_0_1());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_inner_1_2=ruleIQLInterface();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getODLModelElementRule());
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
                    // InternalODL.g:190:6: lv_inner_1_3= ruleODLOperator
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getODLModelElementAccess().getInnerODLOperatorParserRuleCall_1_0_2());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_inner_1_3=ruleODLOperator();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getODLModelElementRule());
                      						}
                      						set(
                      							current,
                      							"inner",
                      							lv_inner_1_3,
                      							"de.uniol.inf.is.odysseus.iql.odl.ODL.ODLOperator");
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
    // $ANTLR end "ruleODLModelElement"


    // $ANTLR start "entryRuleODLOperator"
    // InternalODL.g:212:1: entryRuleODLOperator returns [EObject current=null] : iv_ruleODLOperator= ruleODLOperator EOF ;
    public final EObject entryRuleODLOperator() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleODLOperator = null;


        try {
            // InternalODL.g:212:52: (iv_ruleODLOperator= ruleODLOperator EOF )
            // InternalODL.g:213:2: iv_ruleODLOperator= ruleODLOperator EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getODLOperatorRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleODLOperator=ruleODLOperator();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleODLOperator; 
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
    // $ANTLR end "entryRuleODLOperator"


    // $ANTLR start "ruleODLOperator"
    // InternalODL.g:219:1: ruleODLOperator returns [EObject current=null] : ( () otherlv_1= 'operator' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )? otherlv_6= '{' ( ( (lv_members_7_1= ruleIQLAttribute | lv_members_7_2= ruleIQLMethod | lv_members_7_3= ruleODLParameter | lv_members_7_4= ruleODLMethod | lv_members_7_5= ruleIQLJavaMember ) ) )* otherlv_8= '}' ) ;
    public final EObject ruleODLOperator() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_simpleName_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        EObject lv_metadataList_4_0 = null;

        EObject lv_members_7_1 = null;

        EObject lv_members_7_2 = null;

        EObject lv_members_7_3 = null;

        EObject lv_members_7_4 = null;

        EObject lv_members_7_5 = null;



        	enterRule();

        try {
            // InternalODL.g:225:2: ( ( () otherlv_1= 'operator' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )? otherlv_6= '{' ( ( (lv_members_7_1= ruleIQLAttribute | lv_members_7_2= ruleIQLMethod | lv_members_7_3= ruleODLParameter | lv_members_7_4= ruleODLMethod | lv_members_7_5= ruleIQLJavaMember ) ) )* otherlv_8= '}' ) )
            // InternalODL.g:226:2: ( () otherlv_1= 'operator' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )? otherlv_6= '{' ( ( (lv_members_7_1= ruleIQLAttribute | lv_members_7_2= ruleIQLMethod | lv_members_7_3= ruleODLParameter | lv_members_7_4= ruleODLMethod | lv_members_7_5= ruleIQLJavaMember ) ) )* otherlv_8= '}' )
            {
            // InternalODL.g:226:2: ( () otherlv_1= 'operator' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )? otherlv_6= '{' ( ( (lv_members_7_1= ruleIQLAttribute | lv_members_7_2= ruleIQLMethod | lv_members_7_3= ruleODLParameter | lv_members_7_4= ruleODLMethod | lv_members_7_5= ruleIQLJavaMember ) ) )* otherlv_8= '}' )
            // InternalODL.g:227:3: () otherlv_1= 'operator' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )? otherlv_6= '{' ( ( (lv_members_7_1= ruleIQLAttribute | lv_members_7_2= ruleIQLMethod | lv_members_7_3= ruleODLParameter | lv_members_7_4= ruleODLMethod | lv_members_7_5= ruleIQLJavaMember ) ) )* otherlv_8= '}'
            {
            // InternalODL.g:227:3: ()
            // InternalODL.g:228:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getODLOperatorAccess().getODLOperatorAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,13,FOLLOW_6); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getODLOperatorAccess().getOperatorKeyword_1());
              		
            }
            // InternalODL.g:238:3: ( (lv_simpleName_2_0= RULE_ID ) )
            // InternalODL.g:239:4: (lv_simpleName_2_0= RULE_ID )
            {
            // InternalODL.g:239:4: (lv_simpleName_2_0= RULE_ID )
            // InternalODL.g:240:5: lv_simpleName_2_0= RULE_ID
            {
            lv_simpleName_2_0=(Token)match(input,RULE_ID,FOLLOW_7); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_simpleName_2_0, grammarAccess.getODLOperatorAccess().getSimpleNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getODLOperatorRule());
              					}
              					setWithLastConsumed(
              						current,
              						"simpleName",
              						lv_simpleName_2_0,
              						"org.eclipse.xtext.common.Terminals.ID");
              				
            }

            }


            }

            // InternalODL.g:256:3: (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==14) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // InternalODL.g:257:4: otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')'
                    {
                    otherlv_3=(Token)match(input,14,FOLLOW_8); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getODLOperatorAccess().getLeftParenthesisKeyword_3_0());
                      			
                    }
                    // InternalODL.g:261:4: ( (lv_metadataList_4_0= ruleIQLMetadataList ) )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==RULE_ID) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // InternalODL.g:262:5: (lv_metadataList_4_0= ruleIQLMetadataList )
                            {
                            // InternalODL.g:262:5: (lv_metadataList_4_0= ruleIQLMetadataList )
                            // InternalODL.g:263:6: lv_metadataList_4_0= ruleIQLMetadataList
                            {
                            if ( state.backtracking==0 ) {

                              						newCompositeNode(grammarAccess.getODLOperatorAccess().getMetadataListIQLMetadataListParserRuleCall_3_1_0());
                              					
                            }
                            pushFollow(FOLLOW_9);
                            lv_metadataList_4_0=ruleIQLMetadataList();

                            state._fsp--;
                            if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              						if (current==null) {
                              							current = createModelElementForParent(grammarAccess.getODLOperatorRule());
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

                    otherlv_5=(Token)match(input,15,FOLLOW_10); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getODLOperatorAccess().getRightParenthesisKeyword_3_2());
                      			
                    }

                    }
                    break;

            }

            otherlv_6=(Token)match(input,16,FOLLOW_11); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_6, grammarAccess.getODLOperatorAccess().getLeftCurlyBracketKeyword_4());
              		
            }
            // InternalODL.g:289:3: ( ( (lv_members_7_1= ruleIQLAttribute | lv_members_7_2= ruleIQLMethod | lv_members_7_3= ruleODLParameter | lv_members_7_4= ruleODLMethod | lv_members_7_5= ruleIQLJavaMember ) ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==RULE_ID||(LA8_0>=18 && LA8_0<=19)||(LA8_0>=21 && LA8_0<=25)||LA8_0==92) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // InternalODL.g:290:4: ( (lv_members_7_1= ruleIQLAttribute | lv_members_7_2= ruleIQLMethod | lv_members_7_3= ruleODLParameter | lv_members_7_4= ruleODLMethod | lv_members_7_5= ruleIQLJavaMember ) )
            	    {
            	    // InternalODL.g:290:4: ( (lv_members_7_1= ruleIQLAttribute | lv_members_7_2= ruleIQLMethod | lv_members_7_3= ruleODLParameter | lv_members_7_4= ruleODLMethod | lv_members_7_5= ruleIQLJavaMember ) )
            	    // InternalODL.g:291:5: (lv_members_7_1= ruleIQLAttribute | lv_members_7_2= ruleIQLMethod | lv_members_7_3= ruleODLParameter | lv_members_7_4= ruleODLMethod | lv_members_7_5= ruleIQLJavaMember )
            	    {
            	    // InternalODL.g:291:5: (lv_members_7_1= ruleIQLAttribute | lv_members_7_2= ruleIQLMethod | lv_members_7_3= ruleODLParameter | lv_members_7_4= ruleODLMethod | lv_members_7_5= ruleIQLJavaMember )
            	    int alt7=5;
            	    switch ( input.LA(1) ) {
            	    case RULE_ID:
            	        {
            	        int LA7_1 = input.LA(2);

            	        if ( (LA7_1==RULE_ID||LA7_1==64||LA7_1==91) ) {
            	            alt7=1;
            	        }
            	        else if ( (LA7_1==14||LA7_1==16||LA7_1==27) ) {
            	            alt7=2;
            	        }
            	        else {
            	            if (state.backtracking>0) {state.failed=true; return current;}
            	            NoViableAltException nvae =
            	                new NoViableAltException("", 7, 1, input);

            	            throw nvae;
            	        }
            	        }
            	        break;
            	    case 23:
            	        {
            	        int LA7_2 = input.LA(2);

            	        if ( ((LA7_2>=24 && LA7_2<=25)) ) {
            	            alt7=4;
            	        }
            	        else if ( (LA7_2==RULE_ID) ) {
            	            alt7=2;
            	        }
            	        else {
            	            if (state.backtracking>0) {state.failed=true; return current;}
            	            NoViableAltException nvae =
            	                new NoViableAltException("", 7, 2, input);

            	            throw nvae;
            	        }
            	        }
            	        break;
            	    case 18:
            	    case 19:
            	        {
            	        alt7=3;
            	        }
            	        break;
            	    case 21:
            	    case 22:
            	    case 24:
            	    case 25:
            	        {
            	        alt7=4;
            	        }
            	        break;
            	    case 92:
            	        {
            	        alt7=5;
            	        }
            	        break;
            	    default:
            	        if (state.backtracking>0) {state.failed=true; return current;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 7, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt7) {
            	        case 1 :
            	            // InternalODL.g:292:6: lv_members_7_1= ruleIQLAttribute
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getODLOperatorAccess().getMembersIQLAttributeParserRuleCall_5_0_0());
            	              					
            	            }
            	            pushFollow(FOLLOW_11);
            	            lv_members_7_1=ruleIQLAttribute();

            	            state._fsp--;
            	            if (state.failed) return current;
            	            if ( state.backtracking==0 ) {

            	              						if (current==null) {
            	              							current = createModelElementForParent(grammarAccess.getODLOperatorRule());
            	              						}
            	              						add(
            	              							current,
            	              							"members",
            	              							lv_members_7_1,
            	              							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLAttribute");
            	              						afterParserOrEnumRuleCall();
            	              					
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // InternalODL.g:308:6: lv_members_7_2= ruleIQLMethod
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getODLOperatorAccess().getMembersIQLMethodParserRuleCall_5_0_1());
            	              					
            	            }
            	            pushFollow(FOLLOW_11);
            	            lv_members_7_2=ruleIQLMethod();

            	            state._fsp--;
            	            if (state.failed) return current;
            	            if ( state.backtracking==0 ) {

            	              						if (current==null) {
            	              							current = createModelElementForParent(grammarAccess.getODLOperatorRule());
            	              						}
            	              						add(
            	              							current,
            	              							"members",
            	              							lv_members_7_2,
            	              							"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMethod");
            	              						afterParserOrEnumRuleCall();
            	              					
            	            }

            	            }
            	            break;
            	        case 3 :
            	            // InternalODL.g:324:6: lv_members_7_3= ruleODLParameter
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getODLOperatorAccess().getMembersODLParameterParserRuleCall_5_0_2());
            	              					
            	            }
            	            pushFollow(FOLLOW_11);
            	            lv_members_7_3=ruleODLParameter();

            	            state._fsp--;
            	            if (state.failed) return current;
            	            if ( state.backtracking==0 ) {

            	              						if (current==null) {
            	              							current = createModelElementForParent(grammarAccess.getODLOperatorRule());
            	              						}
            	              						add(
            	              							current,
            	              							"members",
            	              							lv_members_7_3,
            	              							"de.uniol.inf.is.odysseus.iql.odl.ODL.ODLParameter");
            	              						afterParserOrEnumRuleCall();
            	              					
            	            }

            	            }
            	            break;
            	        case 4 :
            	            // InternalODL.g:340:6: lv_members_7_4= ruleODLMethod
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getODLOperatorAccess().getMembersODLMethodParserRuleCall_5_0_3());
            	              					
            	            }
            	            pushFollow(FOLLOW_11);
            	            lv_members_7_4=ruleODLMethod();

            	            state._fsp--;
            	            if (state.failed) return current;
            	            if ( state.backtracking==0 ) {

            	              						if (current==null) {
            	              							current = createModelElementForParent(grammarAccess.getODLOperatorRule());
            	              						}
            	              						add(
            	              							current,
            	              							"members",
            	              							lv_members_7_4,
            	              							"de.uniol.inf.is.odysseus.iql.odl.ODL.ODLMethod");
            	              						afterParserOrEnumRuleCall();
            	              					
            	            }

            	            }
            	            break;
            	        case 5 :
            	            // InternalODL.g:356:6: lv_members_7_5= ruleIQLJavaMember
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getODLOperatorAccess().getMembersIQLJavaMemberParserRuleCall_5_0_4());
            	              					
            	            }
            	            pushFollow(FOLLOW_11);
            	            lv_members_7_5=ruleIQLJavaMember();

            	            state._fsp--;
            	            if (state.failed) return current;
            	            if ( state.backtracking==0 ) {

            	              						if (current==null) {
            	              							current = createModelElementForParent(grammarAccess.getODLOperatorRule());
            	              						}
            	              						add(
            	              							current,
            	              							"members",
            	              							lv_members_7_5,
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
            	    break loop8;
                }
            } while (true);

            otherlv_8=(Token)match(input,17,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_8, grammarAccess.getODLOperatorAccess().getRightCurlyBracketKeyword_6());
              		
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
    // $ANTLR end "ruleODLOperator"


    // $ANTLR start "entryRuleODLParameter"
    // InternalODL.g:382:1: entryRuleODLParameter returns [EObject current=null] : iv_ruleODLParameter= ruleODLParameter EOF ;
    public final EObject entryRuleODLParameter() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleODLParameter = null;


        try {
            // InternalODL.g:382:53: (iv_ruleODLParameter= ruleODLParameter EOF )
            // InternalODL.g:383:2: iv_ruleODLParameter= ruleODLParameter EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getODLParameterRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleODLParameter=ruleODLParameter();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleODLParameter; 
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
    // $ANTLR end "entryRuleODLParameter"


    // $ANTLR start "ruleODLParameter"
    // InternalODL.g:389:1: ruleODLParameter returns [EObject current=null] : ( () ( (lv_optional_1_0= 'optional' ) )? ( (lv_parameter_2_0= 'parameter' ) ) (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )? ( (lv_type_6_0= ruleJvmTypeReference ) ) ( (lv_simpleName_7_0= RULE_ID ) ) ( (lv_init_8_0= ruleIQLVariableInitialization ) )? otherlv_9= ';' ) ;
    public final EObject ruleODLParameter() throws RecognitionException {
        EObject current = null;

        Token lv_optional_1_0=null;
        Token lv_parameter_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token lv_simpleName_7_0=null;
        Token otherlv_9=null;
        EObject lv_metadataList_4_0 = null;

        EObject lv_type_6_0 = null;

        EObject lv_init_8_0 = null;



        	enterRule();

        try {
            // InternalODL.g:395:2: ( ( () ( (lv_optional_1_0= 'optional' ) )? ( (lv_parameter_2_0= 'parameter' ) ) (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )? ( (lv_type_6_0= ruleJvmTypeReference ) ) ( (lv_simpleName_7_0= RULE_ID ) ) ( (lv_init_8_0= ruleIQLVariableInitialization ) )? otherlv_9= ';' ) )
            // InternalODL.g:396:2: ( () ( (lv_optional_1_0= 'optional' ) )? ( (lv_parameter_2_0= 'parameter' ) ) (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )? ( (lv_type_6_0= ruleJvmTypeReference ) ) ( (lv_simpleName_7_0= RULE_ID ) ) ( (lv_init_8_0= ruleIQLVariableInitialization ) )? otherlv_9= ';' )
            {
            // InternalODL.g:396:2: ( () ( (lv_optional_1_0= 'optional' ) )? ( (lv_parameter_2_0= 'parameter' ) ) (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )? ( (lv_type_6_0= ruleJvmTypeReference ) ) ( (lv_simpleName_7_0= RULE_ID ) ) ( (lv_init_8_0= ruleIQLVariableInitialization ) )? otherlv_9= ';' )
            // InternalODL.g:397:3: () ( (lv_optional_1_0= 'optional' ) )? ( (lv_parameter_2_0= 'parameter' ) ) (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )? ( (lv_type_6_0= ruleJvmTypeReference ) ) ( (lv_simpleName_7_0= RULE_ID ) ) ( (lv_init_8_0= ruleIQLVariableInitialization ) )? otherlv_9= ';'
            {
            // InternalODL.g:397:3: ()
            // InternalODL.g:398:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getODLParameterAccess().getODLParameterAction_0(),
              					current);
              			
            }

            }

            // InternalODL.g:404:3: ( (lv_optional_1_0= 'optional' ) )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==18) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // InternalODL.g:405:4: (lv_optional_1_0= 'optional' )
                    {
                    // InternalODL.g:405:4: (lv_optional_1_0= 'optional' )
                    // InternalODL.g:406:5: lv_optional_1_0= 'optional'
                    {
                    lv_optional_1_0=(Token)match(input,18,FOLLOW_12); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_optional_1_0, grammarAccess.getODLParameterAccess().getOptionalOptionalKeyword_1_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getODLParameterRule());
                      					}
                      					setWithLastConsumed(current, "optional", true, "optional");
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalODL.g:418:3: ( (lv_parameter_2_0= 'parameter' ) )
            // InternalODL.g:419:4: (lv_parameter_2_0= 'parameter' )
            {
            // InternalODL.g:419:4: (lv_parameter_2_0= 'parameter' )
            // InternalODL.g:420:5: lv_parameter_2_0= 'parameter'
            {
            lv_parameter_2_0=(Token)match(input,19,FOLLOW_13); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_parameter_2_0, grammarAccess.getODLParameterAccess().getParameterParameterKeyword_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getODLParameterRule());
              					}
              					setWithLastConsumed(current, "parameter", true, "parameter");
              				
            }

            }


            }

            // InternalODL.g:432:3: (otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')' )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==14) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // InternalODL.g:433:4: otherlv_3= '(' ( (lv_metadataList_4_0= ruleIQLMetadataList ) )? otherlv_5= ')'
                    {
                    otherlv_3=(Token)match(input,14,FOLLOW_8); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getODLParameterAccess().getLeftParenthesisKeyword_3_0());
                      			
                    }
                    // InternalODL.g:437:4: ( (lv_metadataList_4_0= ruleIQLMetadataList ) )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==RULE_ID) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // InternalODL.g:438:5: (lv_metadataList_4_0= ruleIQLMetadataList )
                            {
                            // InternalODL.g:438:5: (lv_metadataList_4_0= ruleIQLMetadataList )
                            // InternalODL.g:439:6: lv_metadataList_4_0= ruleIQLMetadataList
                            {
                            if ( state.backtracking==0 ) {

                              						newCompositeNode(grammarAccess.getODLParameterAccess().getMetadataListIQLMetadataListParserRuleCall_3_1_0());
                              					
                            }
                            pushFollow(FOLLOW_9);
                            lv_metadataList_4_0=ruleIQLMetadataList();

                            state._fsp--;
                            if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              						if (current==null) {
                              							current = createModelElementForParent(grammarAccess.getODLParameterRule());
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

                    otherlv_5=(Token)match(input,15,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getODLParameterAccess().getRightParenthesisKeyword_3_2());
                      			
                    }

                    }
                    break;

            }

            // InternalODL.g:461:3: ( (lv_type_6_0= ruleJvmTypeReference ) )
            // InternalODL.g:462:4: (lv_type_6_0= ruleJvmTypeReference )
            {
            // InternalODL.g:462:4: (lv_type_6_0= ruleJvmTypeReference )
            // InternalODL.g:463:5: lv_type_6_0= ruleJvmTypeReference
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getODLParameterAccess().getTypeJvmTypeReferenceParserRuleCall_4_0());
              				
            }
            pushFollow(FOLLOW_6);
            lv_type_6_0=ruleJvmTypeReference();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getODLParameterRule());
              					}
              					set(
              						current,
              						"type",
              						lv_type_6_0,
              						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalODL.g:480:3: ( (lv_simpleName_7_0= RULE_ID ) )
            // InternalODL.g:481:4: (lv_simpleName_7_0= RULE_ID )
            {
            // InternalODL.g:481:4: (lv_simpleName_7_0= RULE_ID )
            // InternalODL.g:482:5: lv_simpleName_7_0= RULE_ID
            {
            lv_simpleName_7_0=(Token)match(input,RULE_ID,FOLLOW_14); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_simpleName_7_0, grammarAccess.getODLParameterAccess().getSimpleNameIDTerminalRuleCall_5_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getODLParameterRule());
              					}
              					setWithLastConsumed(
              						current,
              						"simpleName",
              						lv_simpleName_7_0,
              						"org.eclipse.xtext.common.Terminals.ID");
              				
            }

            }


            }

            // InternalODL.g:498:3: ( (lv_init_8_0= ruleIQLVariableInitialization ) )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==14||LA12_0==16||LA12_0==49) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // InternalODL.g:499:4: (lv_init_8_0= ruleIQLVariableInitialization )
                    {
                    // InternalODL.g:499:4: (lv_init_8_0= ruleIQLVariableInitialization )
                    // InternalODL.g:500:5: lv_init_8_0= ruleIQLVariableInitialization
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getODLParameterAccess().getInitIQLVariableInitializationParserRuleCall_6_0());
                      				
                    }
                    pushFollow(FOLLOW_15);
                    lv_init_8_0=ruleIQLVariableInitialization();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getODLParameterRule());
                      					}
                      					set(
                      						current,
                      						"init",
                      						lv_init_8_0,
                      						"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLVariableInitialization");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_9=(Token)match(input,20,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_9, grammarAccess.getODLParameterAccess().getSemicolonKeyword_7());
              		
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
    // $ANTLR end "ruleODLParameter"


    // $ANTLR start "entryRuleODLMethod"
    // InternalODL.g:525:1: entryRuleODLMethod returns [EObject current=null] : iv_ruleODLMethod= ruleODLMethod EOF ;
    public final EObject entryRuleODLMethod() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleODLMethod = null;


        try {
            // InternalODL.g:525:50: (iv_ruleODLMethod= ruleODLMethod EOF )
            // InternalODL.g:526:2: iv_ruleODLMethod= ruleODLMethod EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getODLMethodRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleODLMethod=ruleODLMethod();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleODLMethod; 
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
    // $ANTLR end "entryRuleODLMethod"


    // $ANTLR start "ruleODLMethod"
    // InternalODL.g:532:1: ruleODLMethod returns [EObject current=null] : ( () ( ( (lv_on_1_0= 'on' ) ) | ( (lv_validate_2_0= 'validate' ) ) | ( ( (lv_override_3_0= 'override' ) )? ( (lv_ao_4_0= 'ao' ) ) ) | ( ( (lv_override_5_0= 'override' ) )? ( (lv_po_6_0= 'po' ) ) ) ) ( ( (lv_simpleName_7_0= RULE_ID ) ) (otherlv_8= '(' ( ( (lv_parameters_9_0= ruleJvmFormalParameter ) ) (otherlv_10= ',' ( (lv_parameters_11_0= ruleJvmFormalParameter ) ) )* )? otherlv_12= ')' )? (otherlv_13= ':' ( (lv_returnType_14_0= ruleJvmTypeReference ) ) )? )? ( (lv_body_15_0= ruleIQLStatementBlock ) ) ) ;
    public final EObject ruleODLMethod() throws RecognitionException {
        EObject current = null;

        Token lv_on_1_0=null;
        Token lv_validate_2_0=null;
        Token lv_override_3_0=null;
        Token lv_ao_4_0=null;
        Token lv_override_5_0=null;
        Token lv_po_6_0=null;
        Token lv_simpleName_7_0=null;
        Token otherlv_8=null;
        Token otherlv_10=null;
        Token otherlv_12=null;
        Token otherlv_13=null;
        EObject lv_parameters_9_0 = null;

        EObject lv_parameters_11_0 = null;

        EObject lv_returnType_14_0 = null;

        EObject lv_body_15_0 = null;



        	enterRule();

        try {
            // InternalODL.g:538:2: ( ( () ( ( (lv_on_1_0= 'on' ) ) | ( (lv_validate_2_0= 'validate' ) ) | ( ( (lv_override_3_0= 'override' ) )? ( (lv_ao_4_0= 'ao' ) ) ) | ( ( (lv_override_5_0= 'override' ) )? ( (lv_po_6_0= 'po' ) ) ) ) ( ( (lv_simpleName_7_0= RULE_ID ) ) (otherlv_8= '(' ( ( (lv_parameters_9_0= ruleJvmFormalParameter ) ) (otherlv_10= ',' ( (lv_parameters_11_0= ruleJvmFormalParameter ) ) )* )? otherlv_12= ')' )? (otherlv_13= ':' ( (lv_returnType_14_0= ruleJvmTypeReference ) ) )? )? ( (lv_body_15_0= ruleIQLStatementBlock ) ) ) )
            // InternalODL.g:539:2: ( () ( ( (lv_on_1_0= 'on' ) ) | ( (lv_validate_2_0= 'validate' ) ) | ( ( (lv_override_3_0= 'override' ) )? ( (lv_ao_4_0= 'ao' ) ) ) | ( ( (lv_override_5_0= 'override' ) )? ( (lv_po_6_0= 'po' ) ) ) ) ( ( (lv_simpleName_7_0= RULE_ID ) ) (otherlv_8= '(' ( ( (lv_parameters_9_0= ruleJvmFormalParameter ) ) (otherlv_10= ',' ( (lv_parameters_11_0= ruleJvmFormalParameter ) ) )* )? otherlv_12= ')' )? (otherlv_13= ':' ( (lv_returnType_14_0= ruleJvmTypeReference ) ) )? )? ( (lv_body_15_0= ruleIQLStatementBlock ) ) )
            {
            // InternalODL.g:539:2: ( () ( ( (lv_on_1_0= 'on' ) ) | ( (lv_validate_2_0= 'validate' ) ) | ( ( (lv_override_3_0= 'override' ) )? ( (lv_ao_4_0= 'ao' ) ) ) | ( ( (lv_override_5_0= 'override' ) )? ( (lv_po_6_0= 'po' ) ) ) ) ( ( (lv_simpleName_7_0= RULE_ID ) ) (otherlv_8= '(' ( ( (lv_parameters_9_0= ruleJvmFormalParameter ) ) (otherlv_10= ',' ( (lv_parameters_11_0= ruleJvmFormalParameter ) ) )* )? otherlv_12= ')' )? (otherlv_13= ':' ( (lv_returnType_14_0= ruleJvmTypeReference ) ) )? )? ( (lv_body_15_0= ruleIQLStatementBlock ) ) )
            // InternalODL.g:540:3: () ( ( (lv_on_1_0= 'on' ) ) | ( (lv_validate_2_0= 'validate' ) ) | ( ( (lv_override_3_0= 'override' ) )? ( (lv_ao_4_0= 'ao' ) ) ) | ( ( (lv_override_5_0= 'override' ) )? ( (lv_po_6_0= 'po' ) ) ) ) ( ( (lv_simpleName_7_0= RULE_ID ) ) (otherlv_8= '(' ( ( (lv_parameters_9_0= ruleJvmFormalParameter ) ) (otherlv_10= ',' ( (lv_parameters_11_0= ruleJvmFormalParameter ) ) )* )? otherlv_12= ')' )? (otherlv_13= ':' ( (lv_returnType_14_0= ruleJvmTypeReference ) ) )? )? ( (lv_body_15_0= ruleIQLStatementBlock ) )
            {
            // InternalODL.g:540:3: ()
            // InternalODL.g:541:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getODLMethodAccess().getODLMethodAction_0(),
              					current);
              			
            }

            }

            // InternalODL.g:547:3: ( ( (lv_on_1_0= 'on' ) ) | ( (lv_validate_2_0= 'validate' ) ) | ( ( (lv_override_3_0= 'override' ) )? ( (lv_ao_4_0= 'ao' ) ) ) | ( ( (lv_override_5_0= 'override' ) )? ( (lv_po_6_0= 'po' ) ) ) )
            int alt15=4;
            switch ( input.LA(1) ) {
            case 21:
                {
                alt15=1;
                }
                break;
            case 22:
                {
                alt15=2;
                }
                break;
            case 23:
                {
                int LA15_3 = input.LA(2);

                if ( (LA15_3==24) ) {
                    alt15=3;
                }
                else if ( (LA15_3==25) ) {
                    alt15=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return current;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 3, input);

                    throw nvae;
                }
                }
                break;
            case 24:
                {
                alt15=3;
                }
                break;
            case 25:
                {
                alt15=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }

            switch (alt15) {
                case 1 :
                    // InternalODL.g:548:4: ( (lv_on_1_0= 'on' ) )
                    {
                    // InternalODL.g:548:4: ( (lv_on_1_0= 'on' ) )
                    // InternalODL.g:549:5: (lv_on_1_0= 'on' )
                    {
                    // InternalODL.g:549:5: (lv_on_1_0= 'on' )
                    // InternalODL.g:550:6: lv_on_1_0= 'on'
                    {
                    lv_on_1_0=(Token)match(input,21,FOLLOW_16); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_on_1_0, grammarAccess.getODLMethodAccess().getOnOnKeyword_1_0_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getODLMethodRule());
                      						}
                      						setWithLastConsumed(current, "on", true, "on");
                      					
                    }

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalODL.g:563:4: ( (lv_validate_2_0= 'validate' ) )
                    {
                    // InternalODL.g:563:4: ( (lv_validate_2_0= 'validate' ) )
                    // InternalODL.g:564:5: (lv_validate_2_0= 'validate' )
                    {
                    // InternalODL.g:564:5: (lv_validate_2_0= 'validate' )
                    // InternalODL.g:565:6: lv_validate_2_0= 'validate'
                    {
                    lv_validate_2_0=(Token)match(input,22,FOLLOW_16); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_validate_2_0, grammarAccess.getODLMethodAccess().getValidateValidateKeyword_1_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getODLMethodRule());
                      						}
                      						setWithLastConsumed(current, "validate", true, "validate");
                      					
                    }

                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalODL.g:578:4: ( ( (lv_override_3_0= 'override' ) )? ( (lv_ao_4_0= 'ao' ) ) )
                    {
                    // InternalODL.g:578:4: ( ( (lv_override_3_0= 'override' ) )? ( (lv_ao_4_0= 'ao' ) ) )
                    // InternalODL.g:579:5: ( (lv_override_3_0= 'override' ) )? ( (lv_ao_4_0= 'ao' ) )
                    {
                    // InternalODL.g:579:5: ( (lv_override_3_0= 'override' ) )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==23) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // InternalODL.g:580:6: (lv_override_3_0= 'override' )
                            {
                            // InternalODL.g:580:6: (lv_override_3_0= 'override' )
                            // InternalODL.g:581:7: lv_override_3_0= 'override'
                            {
                            lv_override_3_0=(Token)match(input,23,FOLLOW_17); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              							newLeafNode(lv_override_3_0, grammarAccess.getODLMethodAccess().getOverrideOverrideKeyword_1_2_0_0());
                              						
                            }
                            if ( state.backtracking==0 ) {

                              							if (current==null) {
                              								current = createModelElement(grammarAccess.getODLMethodRule());
                              							}
                              							setWithLastConsumed(current, "override", true, "override");
                              						
                            }

                            }


                            }
                            break;

                    }

                    // InternalODL.g:593:5: ( (lv_ao_4_0= 'ao' ) )
                    // InternalODL.g:594:6: (lv_ao_4_0= 'ao' )
                    {
                    // InternalODL.g:594:6: (lv_ao_4_0= 'ao' )
                    // InternalODL.g:595:7: lv_ao_4_0= 'ao'
                    {
                    lv_ao_4_0=(Token)match(input,24,FOLLOW_16); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      							newLeafNode(lv_ao_4_0, grammarAccess.getODLMethodAccess().getAoAoKeyword_1_2_1_0());
                      						
                    }
                    if ( state.backtracking==0 ) {

                      							if (current==null) {
                      								current = createModelElement(grammarAccess.getODLMethodRule());
                      							}
                      							setWithLastConsumed(current, "ao", true, "ao");
                      						
                    }

                    }


                    }


                    }


                    }
                    break;
                case 4 :
                    // InternalODL.g:609:4: ( ( (lv_override_5_0= 'override' ) )? ( (lv_po_6_0= 'po' ) ) )
                    {
                    // InternalODL.g:609:4: ( ( (lv_override_5_0= 'override' ) )? ( (lv_po_6_0= 'po' ) ) )
                    // InternalODL.g:610:5: ( (lv_override_5_0= 'override' ) )? ( (lv_po_6_0= 'po' ) )
                    {
                    // InternalODL.g:610:5: ( (lv_override_5_0= 'override' ) )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==23) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // InternalODL.g:611:6: (lv_override_5_0= 'override' )
                            {
                            // InternalODL.g:611:6: (lv_override_5_0= 'override' )
                            // InternalODL.g:612:7: lv_override_5_0= 'override'
                            {
                            lv_override_5_0=(Token)match(input,23,FOLLOW_18); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              							newLeafNode(lv_override_5_0, grammarAccess.getODLMethodAccess().getOverrideOverrideKeyword_1_3_0_0());
                              						
                            }
                            if ( state.backtracking==0 ) {

                              							if (current==null) {
                              								current = createModelElement(grammarAccess.getODLMethodRule());
                              							}
                              							setWithLastConsumed(current, "override", true, "override");
                              						
                            }

                            }


                            }
                            break;

                    }

                    // InternalODL.g:624:5: ( (lv_po_6_0= 'po' ) )
                    // InternalODL.g:625:6: (lv_po_6_0= 'po' )
                    {
                    // InternalODL.g:625:6: (lv_po_6_0= 'po' )
                    // InternalODL.g:626:7: lv_po_6_0= 'po'
                    {
                    lv_po_6_0=(Token)match(input,25,FOLLOW_16); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      							newLeafNode(lv_po_6_0, grammarAccess.getODLMethodAccess().getPoPoKeyword_1_3_1_0());
                      						
                    }
                    if ( state.backtracking==0 ) {

                      							if (current==null) {
                      								current = createModelElement(grammarAccess.getODLMethodRule());
                      							}
                      							setWithLastConsumed(current, "po", true, "po");
                      						
                    }

                    }


                    }


                    }


                    }
                    break;

            }

            // InternalODL.g:640:3: ( ( (lv_simpleName_7_0= RULE_ID ) ) (otherlv_8= '(' ( ( (lv_parameters_9_0= ruleJvmFormalParameter ) ) (otherlv_10= ',' ( (lv_parameters_11_0= ruleJvmFormalParameter ) ) )* )? otherlv_12= ')' )? (otherlv_13= ':' ( (lv_returnType_14_0= ruleJvmTypeReference ) ) )? )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==RULE_ID) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // InternalODL.g:641:4: ( (lv_simpleName_7_0= RULE_ID ) ) (otherlv_8= '(' ( ( (lv_parameters_9_0= ruleJvmFormalParameter ) ) (otherlv_10= ',' ( (lv_parameters_11_0= ruleJvmFormalParameter ) ) )* )? otherlv_12= ')' )? (otherlv_13= ':' ( (lv_returnType_14_0= ruleJvmTypeReference ) ) )?
                    {
                    // InternalODL.g:641:4: ( (lv_simpleName_7_0= RULE_ID ) )
                    // InternalODL.g:642:5: (lv_simpleName_7_0= RULE_ID )
                    {
                    // InternalODL.g:642:5: (lv_simpleName_7_0= RULE_ID )
                    // InternalODL.g:643:6: lv_simpleName_7_0= RULE_ID
                    {
                    lv_simpleName_7_0=(Token)match(input,RULE_ID,FOLLOW_19); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_simpleName_7_0, grammarAccess.getODLMethodAccess().getSimpleNameIDTerminalRuleCall_2_0_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getODLMethodRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"simpleName",
                      							lv_simpleName_7_0,
                      							"org.eclipse.xtext.common.Terminals.ID");
                      					
                    }

                    }


                    }

                    // InternalODL.g:659:4: (otherlv_8= '(' ( ( (lv_parameters_9_0= ruleJvmFormalParameter ) ) (otherlv_10= ',' ( (lv_parameters_11_0= ruleJvmFormalParameter ) ) )* )? otherlv_12= ')' )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==14) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // InternalODL.g:660:5: otherlv_8= '(' ( ( (lv_parameters_9_0= ruleJvmFormalParameter ) ) (otherlv_10= ',' ( (lv_parameters_11_0= ruleJvmFormalParameter ) ) )* )? otherlv_12= ')'
                            {
                            otherlv_8=(Token)match(input,14,FOLLOW_8); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              					newLeafNode(otherlv_8, grammarAccess.getODLMethodAccess().getLeftParenthesisKeyword_2_1_0());
                              				
                            }
                            // InternalODL.g:664:5: ( ( (lv_parameters_9_0= ruleJvmFormalParameter ) ) (otherlv_10= ',' ( (lv_parameters_11_0= ruleJvmFormalParameter ) ) )* )?
                            int alt17=2;
                            int LA17_0 = input.LA(1);

                            if ( (LA17_0==RULE_ID) ) {
                                alt17=1;
                            }
                            switch (alt17) {
                                case 1 :
                                    // InternalODL.g:665:6: ( (lv_parameters_9_0= ruleJvmFormalParameter ) ) (otherlv_10= ',' ( (lv_parameters_11_0= ruleJvmFormalParameter ) ) )*
                                    {
                                    // InternalODL.g:665:6: ( (lv_parameters_9_0= ruleJvmFormalParameter ) )
                                    // InternalODL.g:666:7: (lv_parameters_9_0= ruleJvmFormalParameter )
                                    {
                                    // InternalODL.g:666:7: (lv_parameters_9_0= ruleJvmFormalParameter )
                                    // InternalODL.g:667:8: lv_parameters_9_0= ruleJvmFormalParameter
                                    {
                                    if ( state.backtracking==0 ) {

                                      								newCompositeNode(grammarAccess.getODLMethodAccess().getParametersJvmFormalParameterParserRuleCall_2_1_1_0_0());
                                      							
                                    }
                                    pushFollow(FOLLOW_20);
                                    lv_parameters_9_0=ruleJvmFormalParameter();

                                    state._fsp--;
                                    if (state.failed) return current;
                                    if ( state.backtracking==0 ) {

                                      								if (current==null) {
                                      									current = createModelElementForParent(grammarAccess.getODLMethodRule());
                                      								}
                                      								add(
                                      									current,
                                      									"parameters",
                                      									lv_parameters_9_0,
                                      									"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmFormalParameter");
                                      								afterParserOrEnumRuleCall();
                                      							
                                    }

                                    }


                                    }

                                    // InternalODL.g:684:6: (otherlv_10= ',' ( (lv_parameters_11_0= ruleJvmFormalParameter ) ) )*
                                    loop16:
                                    do {
                                        int alt16=2;
                                        int LA16_0 = input.LA(1);

                                        if ( (LA16_0==26) ) {
                                            alt16=1;
                                        }


                                        switch (alt16) {
                                    	case 1 :
                                    	    // InternalODL.g:685:7: otherlv_10= ',' ( (lv_parameters_11_0= ruleJvmFormalParameter ) )
                                    	    {
                                    	    otherlv_10=(Token)match(input,26,FOLLOW_6); if (state.failed) return current;
                                    	    if ( state.backtracking==0 ) {

                                    	      							newLeafNode(otherlv_10, grammarAccess.getODLMethodAccess().getCommaKeyword_2_1_1_1_0());
                                    	      						
                                    	    }
                                    	    // InternalODL.g:689:7: ( (lv_parameters_11_0= ruleJvmFormalParameter ) )
                                    	    // InternalODL.g:690:8: (lv_parameters_11_0= ruleJvmFormalParameter )
                                    	    {
                                    	    // InternalODL.g:690:8: (lv_parameters_11_0= ruleJvmFormalParameter )
                                    	    // InternalODL.g:691:9: lv_parameters_11_0= ruleJvmFormalParameter
                                    	    {
                                    	    if ( state.backtracking==0 ) {

                                    	      									newCompositeNode(grammarAccess.getODLMethodAccess().getParametersJvmFormalParameterParserRuleCall_2_1_1_1_1_0());
                                    	      								
                                    	    }
                                    	    pushFollow(FOLLOW_20);
                                    	    lv_parameters_11_0=ruleJvmFormalParameter();

                                    	    state._fsp--;
                                    	    if (state.failed) return current;
                                    	    if ( state.backtracking==0 ) {

                                    	      									if (current==null) {
                                    	      										current = createModelElementForParent(grammarAccess.getODLMethodRule());
                                    	      									}
                                    	      									add(
                                    	      										current,
                                    	      										"parameters",
                                    	      										lv_parameters_11_0,
                                    	      										"de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmFormalParameter");
                                    	      									afterParserOrEnumRuleCall();
                                    	      								
                                    	    }

                                    	    }


                                    	    }


                                    	    }
                                    	    break;

                                    	default :
                                    	    break loop16;
                                        }
                                    } while (true);


                                    }
                                    break;

                            }

                            otherlv_12=(Token)match(input,15,FOLLOW_21); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              					newLeafNode(otherlv_12, grammarAccess.getODLMethodAccess().getRightParenthesisKeyword_2_1_2());
                              				
                            }

                            }
                            break;

                    }

                    // InternalODL.g:715:4: (otherlv_13= ':' ( (lv_returnType_14_0= ruleJvmTypeReference ) ) )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==27) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // InternalODL.g:716:5: otherlv_13= ':' ( (lv_returnType_14_0= ruleJvmTypeReference ) )
                            {
                            otherlv_13=(Token)match(input,27,FOLLOW_6); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              					newLeafNode(otherlv_13, grammarAccess.getODLMethodAccess().getColonKeyword_2_2_0());
                              				
                            }
                            // InternalODL.g:720:5: ( (lv_returnType_14_0= ruleJvmTypeReference ) )
                            // InternalODL.g:721:6: (lv_returnType_14_0= ruleJvmTypeReference )
                            {
                            // InternalODL.g:721:6: (lv_returnType_14_0= ruleJvmTypeReference )
                            // InternalODL.g:722:7: lv_returnType_14_0= ruleJvmTypeReference
                            {
                            if ( state.backtracking==0 ) {

                              							newCompositeNode(grammarAccess.getODLMethodAccess().getReturnTypeJvmTypeReferenceParserRuleCall_2_2_1_0());
                              						
                            }
                            pushFollow(FOLLOW_16);
                            lv_returnType_14_0=ruleJvmTypeReference();

                            state._fsp--;
                            if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              							if (current==null) {
                              								current = createModelElementForParent(grammarAccess.getODLMethodRule());
                              							}
                              							set(
                              								current,
                              								"returnType",
                              								lv_returnType_14_0,
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

            // InternalODL.g:741:3: ( (lv_body_15_0= ruleIQLStatementBlock ) )
            // InternalODL.g:742:4: (lv_body_15_0= ruleIQLStatementBlock )
            {
            // InternalODL.g:742:4: (lv_body_15_0= ruleIQLStatementBlock )
            // InternalODL.g:743:5: lv_body_15_0= ruleIQLStatementBlock
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getODLMethodAccess().getBodyIQLStatementBlockParserRuleCall_3_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_body_15_0=ruleIQLStatementBlock();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getODLMethodRule());
              					}
              					set(
              						current,
              						"body",
              						lv_body_15_0,
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
    // $ANTLR end "ruleODLMethod"


    // $ANTLR start "entryRuleIQLJavaText"
    // InternalODL.g:764:1: entryRuleIQLJavaText returns [String current=null] : iv_ruleIQLJavaText= ruleIQLJavaText EOF ;
    public final String entryRuleIQLJavaText() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleIQLJavaText = null;


        try {
            // InternalODL.g:764:51: (iv_ruleIQLJavaText= ruleIQLJavaText EOF )
            // InternalODL.g:765:2: iv_ruleIQLJavaText= ruleIQLJavaText EOF
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
    // InternalODL.g:771:1: ruleIQLJavaText returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_IQL_JAVA_KEYWORDS_0= ruleIQL_JAVA_KEYWORDS | this_WS_1= RULE_WS | this_ID_2= RULE_ID | this_BOOLEAN_3= ruleBOOLEAN | this_DOUBLE_4= RULE_DOUBLE | this_STRING_5= RULE_STRING | this_INT_6= RULE_INT | this_ANY_OTHER_7= RULE_ANY_OTHER | kw= '+' | kw= '+=' | kw= '-' | kw= '-=' | kw= '*' | kw= '*=' | kw= '/' | kw= '/=' | kw= '%' | kw= '%=' | kw= '++' | kw= '--' | kw= '>' | kw= '>=' | kw= '<' | kw= '<=' | kw= '!' | kw= '!=' | kw= '&&' | kw= '||' | kw= '==' | kw= '=' | kw= '~' | kw= '?:' | kw= '|' | kw= '|=' | kw= '^' | kw= '^=' | kw= '&' | kw= '&=' | kw= '>>' | kw= '>>=' | kw= '<<' | kw= '<<=' | kw= '>>>' | kw= '>>>=' | kw= '[' | kw= ']' | kw= '{' | kw= '}' | kw= '(' | kw= ')' | kw= '.' | kw= ':' | kw= ';' | kw= ',' | kw= 'null' | kw= 'on' | kw= 'validate' | kw= 'parameter' | kw= 'optional' | kw= 'operator' )* ;
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
            // InternalODL.g:777:2: ( (this_IQL_JAVA_KEYWORDS_0= ruleIQL_JAVA_KEYWORDS | this_WS_1= RULE_WS | this_ID_2= RULE_ID | this_BOOLEAN_3= ruleBOOLEAN | this_DOUBLE_4= RULE_DOUBLE | this_STRING_5= RULE_STRING | this_INT_6= RULE_INT | this_ANY_OTHER_7= RULE_ANY_OTHER | kw= '+' | kw= '+=' | kw= '-' | kw= '-=' | kw= '*' | kw= '*=' | kw= '/' | kw= '/=' | kw= '%' | kw= '%=' | kw= '++' | kw= '--' | kw= '>' | kw= '>=' | kw= '<' | kw= '<=' | kw= '!' | kw= '!=' | kw= '&&' | kw= '||' | kw= '==' | kw= '=' | kw= '~' | kw= '?:' | kw= '|' | kw= '|=' | kw= '^' | kw= '^=' | kw= '&' | kw= '&=' | kw= '>>' | kw= '>>=' | kw= '<<' | kw= '<<=' | kw= '>>>' | kw= '>>>=' | kw= '[' | kw= ']' | kw= '{' | kw= '}' | kw= '(' | kw= ')' | kw= '.' | kw= ':' | kw= ';' | kw= ',' | kw= 'null' | kw= 'on' | kw= 'validate' | kw= 'parameter' | kw= 'optional' | kw= 'operator' )* )
            // InternalODL.g:778:2: (this_IQL_JAVA_KEYWORDS_0= ruleIQL_JAVA_KEYWORDS | this_WS_1= RULE_WS | this_ID_2= RULE_ID | this_BOOLEAN_3= ruleBOOLEAN | this_DOUBLE_4= RULE_DOUBLE | this_STRING_5= RULE_STRING | this_INT_6= RULE_INT | this_ANY_OTHER_7= RULE_ANY_OTHER | kw= '+' | kw= '+=' | kw= '-' | kw= '-=' | kw= '*' | kw= '*=' | kw= '/' | kw= '/=' | kw= '%' | kw= '%=' | kw= '++' | kw= '--' | kw= '>' | kw= '>=' | kw= '<' | kw= '<=' | kw= '!' | kw= '!=' | kw= '&&' | kw= '||' | kw= '==' | kw= '=' | kw= '~' | kw= '?:' | kw= '|' | kw= '|=' | kw= '^' | kw= '^=' | kw= '&' | kw= '&=' | kw= '>>' | kw= '>>=' | kw= '<<' | kw= '<<=' | kw= '>>>' | kw= '>>>=' | kw= '[' | kw= ']' | kw= '{' | kw= '}' | kw= '(' | kw= ')' | kw= '.' | kw= ':' | kw= ';' | kw= ',' | kw= 'null' | kw= 'on' | kw= 'validate' | kw= 'parameter' | kw= 'optional' | kw= 'operator' )*
            {
            // InternalODL.g:778:2: (this_IQL_JAVA_KEYWORDS_0= ruleIQL_JAVA_KEYWORDS | this_WS_1= RULE_WS | this_ID_2= RULE_ID | this_BOOLEAN_3= ruleBOOLEAN | this_DOUBLE_4= RULE_DOUBLE | this_STRING_5= RULE_STRING | this_INT_6= RULE_INT | this_ANY_OTHER_7= RULE_ANY_OTHER | kw= '+' | kw= '+=' | kw= '-' | kw= '-=' | kw= '*' | kw= '*=' | kw= '/' | kw= '/=' | kw= '%' | kw= '%=' | kw= '++' | kw= '--' | kw= '>' | kw= '>=' | kw= '<' | kw= '<=' | kw= '!' | kw= '!=' | kw= '&&' | kw= '||' | kw= '==' | kw= '=' | kw= '~' | kw= '?:' | kw= '|' | kw= '|=' | kw= '^' | kw= '^=' | kw= '&' | kw= '&=' | kw= '>>' | kw= '>>=' | kw= '<<' | kw= '<<=' | kw= '>>>' | kw= '>>>=' | kw= '[' | kw= ']' | kw= '{' | kw= '}' | kw= '(' | kw= ')' | kw= '.' | kw= ':' | kw= ';' | kw= ',' | kw= 'null' | kw= 'on' | kw= 'validate' | kw= 'parameter' | kw= 'optional' | kw= 'operator' )*
            loop21:
            do {
                int alt21=61;
                switch ( input.LA(1) ) {
                case 69:
                case 70:
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
                case 85:
                case 86:
                case 87:
                case 88:
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
                case 111:
                case 112:
                case 113:
                case 114:
                    {
                    alt21=1;
                    }
                    break;
                case RULE_WS:
                    {
                    alt21=2;
                    }
                    break;
                case RULE_ID:
                    {
                    alt21=3;
                    }
                    break;
                case 115:
                case 116:
                    {
                    alt21=4;
                    }
                    break;
                case RULE_DOUBLE:
                    {
                    alt21=5;
                    }
                    break;
                case RULE_STRING:
                    {
                    alt21=6;
                    }
                    break;
                case RULE_INT:
                    {
                    alt21=7;
                    }
                    break;
                case RULE_ANY_OTHER:
                    {
                    alt21=8;
                    }
                    break;
                case 28:
                    {
                    alt21=9;
                    }
                    break;
                case 29:
                    {
                    alt21=10;
                    }
                    break;
                case 30:
                    {
                    alt21=11;
                    }
                    break;
                case 31:
                    {
                    alt21=12;
                    }
                    break;
                case 32:
                    {
                    alt21=13;
                    }
                    break;
                case 33:
                    {
                    alt21=14;
                    }
                    break;
                case 34:
                    {
                    alt21=15;
                    }
                    break;
                case 35:
                    {
                    alt21=16;
                    }
                    break;
                case 36:
                    {
                    alt21=17;
                    }
                    break;
                case 37:
                    {
                    alt21=18;
                    }
                    break;
                case 38:
                    {
                    alt21=19;
                    }
                    break;
                case 39:
                    {
                    alt21=20;
                    }
                    break;
                case 40:
                    {
                    alt21=21;
                    }
                    break;
                case 41:
                    {
                    alt21=22;
                    }
                    break;
                case 42:
                    {
                    alt21=23;
                    }
                    break;
                case 43:
                    {
                    alt21=24;
                    }
                    break;
                case 44:
                    {
                    alt21=25;
                    }
                    break;
                case 45:
                    {
                    alt21=26;
                    }
                    break;
                case 46:
                    {
                    alt21=27;
                    }
                    break;
                case 47:
                    {
                    alt21=28;
                    }
                    break;
                case 48:
                    {
                    alt21=29;
                    }
                    break;
                case 49:
                    {
                    alt21=30;
                    }
                    break;
                case 50:
                    {
                    alt21=31;
                    }
                    break;
                case 51:
                    {
                    alt21=32;
                    }
                    break;
                case 52:
                    {
                    alt21=33;
                    }
                    break;
                case 53:
                    {
                    alt21=34;
                    }
                    break;
                case 54:
                    {
                    alt21=35;
                    }
                    break;
                case 55:
                    {
                    alt21=36;
                    }
                    break;
                case 56:
                    {
                    alt21=37;
                    }
                    break;
                case 57:
                    {
                    alt21=38;
                    }
                    break;
                case 58:
                    {
                    alt21=39;
                    }
                    break;
                case 59:
                    {
                    alt21=40;
                    }
                    break;
                case 60:
                    {
                    alt21=41;
                    }
                    break;
                case 61:
                    {
                    alt21=42;
                    }
                    break;
                case 62:
                    {
                    alt21=43;
                    }
                    break;
                case 63:
                    {
                    alt21=44;
                    }
                    break;
                case 64:
                    {
                    alt21=45;
                    }
                    break;
                case 65:
                    {
                    alt21=46;
                    }
                    break;
                case 16:
                    {
                    alt21=47;
                    }
                    break;
                case 17:
                    {
                    alt21=48;
                    }
                    break;
                case 14:
                    {
                    alt21=49;
                    }
                    break;
                case 15:
                    {
                    alt21=50;
                    }
                    break;
                case 66:
                    {
                    alt21=51;
                    }
                    break;
                case 27:
                    {
                    alt21=52;
                    }
                    break;
                case 20:
                    {
                    alt21=53;
                    }
                    break;
                case 26:
                    {
                    alt21=54;
                    }
                    break;
                case 67:
                    {
                    alt21=55;
                    }
                    break;
                case 21:
                    {
                    alt21=56;
                    }
                    break;
                case 22:
                    {
                    alt21=57;
                    }
                    break;
                case 19:
                    {
                    alt21=58;
                    }
                    break;
                case 18:
                    {
                    alt21=59;
                    }
                    break;
                case 13:
                    {
                    alt21=60;
                    }
                    break;

                }

                switch (alt21) {
            	case 1 :
            	    // InternalODL.g:779:3: this_IQL_JAVA_KEYWORDS_0= ruleIQL_JAVA_KEYWORDS
            	    {
            	    if ( state.backtracking==0 ) {

            	      			newCompositeNode(grammarAccess.getIQLJavaTextAccess().getIQL_JAVA_KEYWORDSParserRuleCall_0());
            	      		
            	    }
            	    pushFollow(FOLLOW_22);
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
            	    // InternalODL.g:790:3: this_WS_1= RULE_WS
            	    {
            	    this_WS_1=(Token)match(input,RULE_WS,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_WS_1);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_WS_1, grammarAccess.getIQLJavaTextAccess().getWSTerminalRuleCall_1());
            	      		
            	    }

            	    }
            	    break;
            	case 3 :
            	    // InternalODL.g:798:3: this_ID_2= RULE_ID
            	    {
            	    this_ID_2=(Token)match(input,RULE_ID,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_ID_2);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_ID_2, grammarAccess.getIQLJavaTextAccess().getIDTerminalRuleCall_2());
            	      		
            	    }

            	    }
            	    break;
            	case 4 :
            	    // InternalODL.g:806:3: this_BOOLEAN_3= ruleBOOLEAN
            	    {
            	    if ( state.backtracking==0 ) {

            	      			newCompositeNode(grammarAccess.getIQLJavaTextAccess().getBOOLEANParserRuleCall_3());
            	      		
            	    }
            	    pushFollow(FOLLOW_22);
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
            	    // InternalODL.g:817:3: this_DOUBLE_4= RULE_DOUBLE
            	    {
            	    this_DOUBLE_4=(Token)match(input,RULE_DOUBLE,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_DOUBLE_4);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_DOUBLE_4, grammarAccess.getIQLJavaTextAccess().getDOUBLETerminalRuleCall_4());
            	      		
            	    }

            	    }
            	    break;
            	case 6 :
            	    // InternalODL.g:825:3: this_STRING_5= RULE_STRING
            	    {
            	    this_STRING_5=(Token)match(input,RULE_STRING,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_STRING_5);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_STRING_5, grammarAccess.getIQLJavaTextAccess().getSTRINGTerminalRuleCall_5());
            	      		
            	    }

            	    }
            	    break;
            	case 7 :
            	    // InternalODL.g:833:3: this_INT_6= RULE_INT
            	    {
            	    this_INT_6=(Token)match(input,RULE_INT,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_INT_6);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_INT_6, grammarAccess.getIQLJavaTextAccess().getINTTerminalRuleCall_6());
            	      		
            	    }

            	    }
            	    break;
            	case 8 :
            	    // InternalODL.g:841:3: this_ANY_OTHER_7= RULE_ANY_OTHER
            	    {
            	    this_ANY_OTHER_7=(Token)match(input,RULE_ANY_OTHER,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(this_ANY_OTHER_7);
            	      		
            	    }
            	    if ( state.backtracking==0 ) {

            	      			newLeafNode(this_ANY_OTHER_7, grammarAccess.getIQLJavaTextAccess().getANY_OTHERTerminalRuleCall_7());
            	      		
            	    }

            	    }
            	    break;
            	case 9 :
            	    // InternalODL.g:849:3: kw= '+'
            	    {
            	    kw=(Token)match(input,28,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getPlusSignKeyword_8());
            	      		
            	    }

            	    }
            	    break;
            	case 10 :
            	    // InternalODL.g:855:3: kw= '+='
            	    {
            	    kw=(Token)match(input,29,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getPlusSignEqualsSignKeyword_9());
            	      		
            	    }

            	    }
            	    break;
            	case 11 :
            	    // InternalODL.g:861:3: kw= '-'
            	    {
            	    kw=(Token)match(input,30,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getHyphenMinusKeyword_10());
            	      		
            	    }

            	    }
            	    break;
            	case 12 :
            	    // InternalODL.g:867:3: kw= '-='
            	    {
            	    kw=(Token)match(input,31,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getHyphenMinusEqualsSignKeyword_11());
            	      		
            	    }

            	    }
            	    break;
            	case 13 :
            	    // InternalODL.g:873:3: kw= '*'
            	    {
            	    kw=(Token)match(input,32,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getAsteriskKeyword_12());
            	      		
            	    }

            	    }
            	    break;
            	case 14 :
            	    // InternalODL.g:879:3: kw= '*='
            	    {
            	    kw=(Token)match(input,33,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getAsteriskEqualsSignKeyword_13());
            	      		
            	    }

            	    }
            	    break;
            	case 15 :
            	    // InternalODL.g:885:3: kw= '/'
            	    {
            	    kw=(Token)match(input,34,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getSolidusKeyword_14());
            	      		
            	    }

            	    }
            	    break;
            	case 16 :
            	    // InternalODL.g:891:3: kw= '/='
            	    {
            	    kw=(Token)match(input,35,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getSolidusEqualsSignKeyword_15());
            	      		
            	    }

            	    }
            	    break;
            	case 17 :
            	    // InternalODL.g:897:3: kw= '%'
            	    {
            	    kw=(Token)match(input,36,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getPercentSignKeyword_16());
            	      		
            	    }

            	    }
            	    break;
            	case 18 :
            	    // InternalODL.g:903:3: kw= '%='
            	    {
            	    kw=(Token)match(input,37,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getPercentSignEqualsSignKeyword_17());
            	      		
            	    }

            	    }
            	    break;
            	case 19 :
            	    // InternalODL.g:909:3: kw= '++'
            	    {
            	    kw=(Token)match(input,38,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getPlusSignPlusSignKeyword_18());
            	      		
            	    }

            	    }
            	    break;
            	case 20 :
            	    // InternalODL.g:915:3: kw= '--'
            	    {
            	    kw=(Token)match(input,39,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getHyphenMinusHyphenMinusKeyword_19());
            	      		
            	    }

            	    }
            	    break;
            	case 21 :
            	    // InternalODL.g:921:3: kw= '>'
            	    {
            	    kw=(Token)match(input,40,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignKeyword_20());
            	      		
            	    }

            	    }
            	    break;
            	case 22 :
            	    // InternalODL.g:927:3: kw= '>='
            	    {
            	    kw=(Token)match(input,41,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignEqualsSignKeyword_21());
            	      		
            	    }

            	    }
            	    break;
            	case 23 :
            	    // InternalODL.g:933:3: kw= '<'
            	    {
            	    kw=(Token)match(input,42,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLessThanSignKeyword_22());
            	      		
            	    }

            	    }
            	    break;
            	case 24 :
            	    // InternalODL.g:939:3: kw= '<='
            	    {
            	    kw=(Token)match(input,43,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLessThanSignEqualsSignKeyword_23());
            	      		
            	    }

            	    }
            	    break;
            	case 25 :
            	    // InternalODL.g:945:3: kw= '!'
            	    {
            	    kw=(Token)match(input,44,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getExclamationMarkKeyword_24());
            	      		
            	    }

            	    }
            	    break;
            	case 26 :
            	    // InternalODL.g:951:3: kw= '!='
            	    {
            	    kw=(Token)match(input,45,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getExclamationMarkEqualsSignKeyword_25());
            	      		
            	    }

            	    }
            	    break;
            	case 27 :
            	    // InternalODL.g:957:3: kw= '&&'
            	    {
            	    kw=(Token)match(input,46,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getAmpersandAmpersandKeyword_26());
            	      		
            	    }

            	    }
            	    break;
            	case 28 :
            	    // InternalODL.g:963:3: kw= '||'
            	    {
            	    kw=(Token)match(input,47,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getVerticalLineVerticalLineKeyword_27());
            	      		
            	    }

            	    }
            	    break;
            	case 29 :
            	    // InternalODL.g:969:3: kw= '=='
            	    {
            	    kw=(Token)match(input,48,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getEqualsSignEqualsSignKeyword_28());
            	      		
            	    }

            	    }
            	    break;
            	case 30 :
            	    // InternalODL.g:975:3: kw= '='
            	    {
            	    kw=(Token)match(input,49,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getEqualsSignKeyword_29());
            	      		
            	    }

            	    }
            	    break;
            	case 31 :
            	    // InternalODL.g:981:3: kw= '~'
            	    {
            	    kw=(Token)match(input,50,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getTildeKeyword_30());
            	      		
            	    }

            	    }
            	    break;
            	case 32 :
            	    // InternalODL.g:987:3: kw= '?:'
            	    {
            	    kw=(Token)match(input,51,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getQuestionMarkColonKeyword_31());
            	      		
            	    }

            	    }
            	    break;
            	case 33 :
            	    // InternalODL.g:993:3: kw= '|'
            	    {
            	    kw=(Token)match(input,52,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getVerticalLineKeyword_32());
            	      		
            	    }

            	    }
            	    break;
            	case 34 :
            	    // InternalODL.g:999:3: kw= '|='
            	    {
            	    kw=(Token)match(input,53,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getVerticalLineEqualsSignKeyword_33());
            	      		
            	    }

            	    }
            	    break;
            	case 35 :
            	    // InternalODL.g:1005:3: kw= '^'
            	    {
            	    kw=(Token)match(input,54,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getCircumflexAccentKeyword_34());
            	      		
            	    }

            	    }
            	    break;
            	case 36 :
            	    // InternalODL.g:1011:3: kw= '^='
            	    {
            	    kw=(Token)match(input,55,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getCircumflexAccentEqualsSignKeyword_35());
            	      		
            	    }

            	    }
            	    break;
            	case 37 :
            	    // InternalODL.g:1017:3: kw= '&'
            	    {
            	    kw=(Token)match(input,56,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getAmpersandKeyword_36());
            	      		
            	    }

            	    }
            	    break;
            	case 38 :
            	    // InternalODL.g:1023:3: kw= '&='
            	    {
            	    kw=(Token)match(input,57,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getAmpersandEqualsSignKeyword_37());
            	      		
            	    }

            	    }
            	    break;
            	case 39 :
            	    // InternalODL.g:1029:3: kw= '>>'
            	    {
            	    kw=(Token)match(input,58,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignGreaterThanSignKeyword_38());
            	      		
            	    }

            	    }
            	    break;
            	case 40 :
            	    // InternalODL.g:1035:3: kw= '>>='
            	    {
            	    kw=(Token)match(input,59,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignGreaterThanSignEqualsSignKeyword_39());
            	      		
            	    }

            	    }
            	    break;
            	case 41 :
            	    // InternalODL.g:1041:3: kw= '<<'
            	    {
            	    kw=(Token)match(input,60,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLessThanSignLessThanSignKeyword_40());
            	      		
            	    }

            	    }
            	    break;
            	case 42 :
            	    // InternalODL.g:1047:3: kw= '<<='
            	    {
            	    kw=(Token)match(input,61,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLessThanSignLessThanSignEqualsSignKeyword_41());
            	      		
            	    }

            	    }
            	    break;
            	case 43 :
            	    // InternalODL.g:1053:3: kw= '>>>'
            	    {
            	    kw=(Token)match(input,62,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignGreaterThanSignGreaterThanSignKeyword_42());
            	      		
            	    }

            	    }
            	    break;
            	case 44 :
            	    // InternalODL.g:1059:3: kw= '>>>='
            	    {
            	    kw=(Token)match(input,63,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getGreaterThanSignGreaterThanSignGreaterThanSignEqualsSignKeyword_43());
            	      		
            	    }

            	    }
            	    break;
            	case 45 :
            	    // InternalODL.g:1065:3: kw= '['
            	    {
            	    kw=(Token)match(input,64,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLeftSquareBracketKeyword_44());
            	      		
            	    }

            	    }
            	    break;
            	case 46 :
            	    // InternalODL.g:1071:3: kw= ']'
            	    {
            	    kw=(Token)match(input,65,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getRightSquareBracketKeyword_45());
            	      		
            	    }

            	    }
            	    break;
            	case 47 :
            	    // InternalODL.g:1077:3: kw= '{'
            	    {
            	    kw=(Token)match(input,16,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLeftCurlyBracketKeyword_46());
            	      		
            	    }

            	    }
            	    break;
            	case 48 :
            	    // InternalODL.g:1083:3: kw= '}'
            	    {
            	    kw=(Token)match(input,17,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getRightCurlyBracketKeyword_47());
            	      		
            	    }

            	    }
            	    break;
            	case 49 :
            	    // InternalODL.g:1089:3: kw= '('
            	    {
            	    kw=(Token)match(input,14,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getLeftParenthesisKeyword_48());
            	      		
            	    }

            	    }
            	    break;
            	case 50 :
            	    // InternalODL.g:1095:3: kw= ')'
            	    {
            	    kw=(Token)match(input,15,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getRightParenthesisKeyword_49());
            	      		
            	    }

            	    }
            	    break;
            	case 51 :
            	    // InternalODL.g:1101:3: kw= '.'
            	    {
            	    kw=(Token)match(input,66,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getFullStopKeyword_50());
            	      		
            	    }

            	    }
            	    break;
            	case 52 :
            	    // InternalODL.g:1107:3: kw= ':'
            	    {
            	    kw=(Token)match(input,27,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getColonKeyword_51());
            	      		
            	    }

            	    }
            	    break;
            	case 53 :
            	    // InternalODL.g:1113:3: kw= ';'
            	    {
            	    kw=(Token)match(input,20,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getSemicolonKeyword_52());
            	      		
            	    }

            	    }
            	    break;
            	case 54 :
            	    // InternalODL.g:1119:3: kw= ','
            	    {
            	    kw=(Token)match(input,26,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getCommaKeyword_53());
            	      		
            	    }

            	    }
            	    break;
            	case 55 :
            	    // InternalODL.g:1125:3: kw= 'null'
            	    {
            	    kw=(Token)match(input,67,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getNullKeyword_54());
            	      		
            	    }

            	    }
            	    break;
            	case 56 :
            	    // InternalODL.g:1131:3: kw= 'on'
            	    {
            	    kw=(Token)match(input,21,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getOnKeyword_55());
            	      		
            	    }

            	    }
            	    break;
            	case 57 :
            	    // InternalODL.g:1137:3: kw= 'validate'
            	    {
            	    kw=(Token)match(input,22,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getValidateKeyword_56());
            	      		
            	    }

            	    }
            	    break;
            	case 58 :
            	    // InternalODL.g:1143:3: kw= 'parameter'
            	    {
            	    kw=(Token)match(input,19,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getParameterKeyword_57());
            	      		
            	    }

            	    }
            	    break;
            	case 59 :
            	    // InternalODL.g:1149:3: kw= 'optional'
            	    {
            	    kw=(Token)match(input,18,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getOptionalKeyword_58());
            	      		
            	    }

            	    }
            	    break;
            	case 60 :
            	    // InternalODL.g:1155:3: kw= 'operator'
            	    {
            	    kw=(Token)match(input,13,FOLLOW_22); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      			current.merge(kw);
            	      			newLeafNode(kw, grammarAccess.getIQLJavaTextAccess().getOperatorKeyword_59());
            	      		
            	    }

            	    }
            	    break;

            	default :
            	    break loop21;
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
    // InternalODL.g:1164:1: entryRuleIQLModelElement returns [EObject current=null] : iv_ruleIQLModelElement= ruleIQLModelElement EOF ;
    public final EObject entryRuleIQLModelElement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLModelElement = null;


        try {
            // InternalODL.g:1164:56: (iv_ruleIQLModelElement= ruleIQLModelElement EOF )
            // InternalODL.g:1165:2: iv_ruleIQLModelElement= ruleIQLModelElement EOF
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
    // InternalODL.g:1171:1: ruleIQLModelElement returns [EObject current=null] : ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) ) ) ;
    public final EObject ruleIQLModelElement() throws RecognitionException {
        EObject current = null;

        EObject lv_javametadata_0_0 = null;

        EObject lv_inner_1_1 = null;

        EObject lv_inner_1_2 = null;



        	enterRule();

        try {
            // InternalODL.g:1177:2: ( ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) ) ) )
            // InternalODL.g:1178:2: ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) ) )
            {
            // InternalODL.g:1178:2: ( ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) ) )
            // InternalODL.g:1179:3: ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )* ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) )
            {
            // InternalODL.g:1179:3: ( (lv_javametadata_0_0= ruleIQLJavaMetadata ) )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==92) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // InternalODL.g:1180:4: (lv_javametadata_0_0= ruleIQLJavaMetadata )
            	    {
            	    // InternalODL.g:1180:4: (lv_javametadata_0_0= ruleIQLJavaMetadata )
            	    // InternalODL.g:1181:5: lv_javametadata_0_0= ruleIQLJavaMetadata
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLModelElementAccess().getJavametadataIQLJavaMetadataParserRuleCall_0_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_23);
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
            	    break loop22;
                }
            } while (true);

            // InternalODL.g:1198:3: ( ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) ) )
            // InternalODL.g:1199:4: ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) )
            {
            // InternalODL.g:1199:4: ( (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface ) )
            // InternalODL.g:1200:5: (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface )
            {
            // InternalODL.g:1200:5: (lv_inner_1_1= ruleIQLClass | lv_inner_1_2= ruleIQLInterface )
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==70) ) {
                alt23=1;
            }
            else if ( (LA23_0==73) ) {
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
                    // InternalODL.g:1201:6: lv_inner_1_1= ruleIQLClass
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
                    // InternalODL.g:1217:6: lv_inner_1_2= ruleIQLInterface
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
    // InternalODL.g:1239:1: entryRuleIQLNamespace returns [EObject current=null] : iv_ruleIQLNamespace= ruleIQLNamespace EOF ;
    public final EObject entryRuleIQLNamespace() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLNamespace = null;


        try {
            // InternalODL.g:1239:53: (iv_ruleIQLNamespace= ruleIQLNamespace EOF )
            // InternalODL.g:1240:2: iv_ruleIQLNamespace= ruleIQLNamespace EOF
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
    // InternalODL.g:1246:1: ruleIQLNamespace returns [EObject current=null] : (otherlv_0= 'use' ( (lv_static_1_0= 'static' ) )? ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) ) otherlv_3= ';' ) ;
    public final EObject ruleIQLNamespace() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_static_1_0=null;
        Token otherlv_3=null;
        AntlrDatatypeRuleToken lv_importedNamespace_2_0 = null;



        	enterRule();

        try {
            // InternalODL.g:1252:2: ( (otherlv_0= 'use' ( (lv_static_1_0= 'static' ) )? ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) ) otherlv_3= ';' ) )
            // InternalODL.g:1253:2: (otherlv_0= 'use' ( (lv_static_1_0= 'static' ) )? ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) ) otherlv_3= ';' )
            {
            // InternalODL.g:1253:2: (otherlv_0= 'use' ( (lv_static_1_0= 'static' ) )? ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) ) otherlv_3= ';' )
            // InternalODL.g:1254:3: otherlv_0= 'use' ( (lv_static_1_0= 'static' ) )? ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) ) otherlv_3= ';'
            {
            otherlv_0=(Token)match(input,68,FOLLOW_24); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_0, grammarAccess.getIQLNamespaceAccess().getUseKeyword_0());
              		
            }
            // InternalODL.g:1258:3: ( (lv_static_1_0= 'static' ) )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==69) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // InternalODL.g:1259:4: (lv_static_1_0= 'static' )
                    {
                    // InternalODL.g:1259:4: (lv_static_1_0= 'static' )
                    // InternalODL.g:1260:5: lv_static_1_0= 'static'
                    {
                    lv_static_1_0=(Token)match(input,69,FOLLOW_24); if (state.failed) return current;
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

            // InternalODL.g:1272:3: ( (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard ) )
            // InternalODL.g:1273:4: (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard )
            {
            // InternalODL.g:1273:4: (lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard )
            // InternalODL.g:1274:5: lv_importedNamespace_2_0= ruleQualifiedNameWithWildcard
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLNamespaceAccess().getImportedNamespaceQualifiedNameWithWildcardParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_15);
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

            otherlv_3=(Token)match(input,20,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:1299:1: entryRuleIQLClass returns [EObject current=null] : iv_ruleIQLClass= ruleIQLClass EOF ;
    public final EObject entryRuleIQLClass() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLClass = null;


        try {
            // InternalODL.g:1299:49: (iv_ruleIQLClass= ruleIQLClass EOF )
            // InternalODL.g:1300:2: iv_ruleIQLClass= ruleIQLClass EOF
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
    // InternalODL.g:1306:1: ruleIQLClass returns [EObject current=null] : ( () otherlv_1= 'class' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )? (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )? otherlv_9= '{' ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )* otherlv_11= '}' ) ;
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
            // InternalODL.g:1312:2: ( ( () otherlv_1= 'class' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )? (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )? otherlv_9= '{' ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )* otherlv_11= '}' ) )
            // InternalODL.g:1313:2: ( () otherlv_1= 'class' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )? (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )? otherlv_9= '{' ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )* otherlv_11= '}' )
            {
            // InternalODL.g:1313:2: ( () otherlv_1= 'class' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )? (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )? otherlv_9= '{' ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )* otherlv_11= '}' )
            // InternalODL.g:1314:3: () otherlv_1= 'class' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )? (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )? otherlv_9= '{' ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )* otherlv_11= '}'
            {
            // InternalODL.g:1314:3: ()
            // InternalODL.g:1315:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLClassAccess().getIQLClassAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,70,FOLLOW_6); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLClassAccess().getClassKeyword_1());
              		
            }
            // InternalODL.g:1325:3: ( (lv_simpleName_2_0= RULE_ID ) )
            // InternalODL.g:1326:4: (lv_simpleName_2_0= RULE_ID )
            {
            // InternalODL.g:1326:4: (lv_simpleName_2_0= RULE_ID )
            // InternalODL.g:1327:5: lv_simpleName_2_0= RULE_ID
            {
            lv_simpleName_2_0=(Token)match(input,RULE_ID,FOLLOW_25); if (state.failed) return current;
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

            // InternalODL.g:1343:3: (otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) ) )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==71) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // InternalODL.g:1344:4: otherlv_3= 'extends' ( (lv_extendedClass_4_0= ruleJvmTypeReference ) )
                    {
                    otherlv_3=(Token)match(input,71,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getIQLClassAccess().getExtendsKeyword_3_0());
                      			
                    }
                    // InternalODL.g:1348:4: ( (lv_extendedClass_4_0= ruleJvmTypeReference ) )
                    // InternalODL.g:1349:5: (lv_extendedClass_4_0= ruleJvmTypeReference )
                    {
                    // InternalODL.g:1349:5: (lv_extendedClass_4_0= ruleJvmTypeReference )
                    // InternalODL.g:1350:6: lv_extendedClass_4_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLClassAccess().getExtendedClassJvmTypeReferenceParserRuleCall_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_26);
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

            // InternalODL.g:1368:3: (otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )* )?
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==72) ) {
                alt27=1;
            }
            switch (alt27) {
                case 1 :
                    // InternalODL.g:1369:4: otherlv_5= 'implements' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )*
                    {
                    otherlv_5=(Token)match(input,72,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getIQLClassAccess().getImplementsKeyword_4_0());
                      			
                    }
                    // InternalODL.g:1373:4: ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) )
                    // InternalODL.g:1374:5: (lv_extendedInterfaces_6_0= ruleJvmTypeReference )
                    {
                    // InternalODL.g:1374:5: (lv_extendedInterfaces_6_0= ruleJvmTypeReference )
                    // InternalODL.g:1375:6: lv_extendedInterfaces_6_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLClassAccess().getExtendedInterfacesJvmTypeReferenceParserRuleCall_4_1_0());
                      					
                    }
                    pushFollow(FOLLOW_27);
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

                    // InternalODL.g:1392:4: (otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) ) )*
                    loop26:
                    do {
                        int alt26=2;
                        int LA26_0 = input.LA(1);

                        if ( (LA26_0==26) ) {
                            alt26=1;
                        }


                        switch (alt26) {
                    	case 1 :
                    	    // InternalODL.g:1393:5: otherlv_7= ',' ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) )
                    	    {
                    	    otherlv_7=(Token)match(input,26,FOLLOW_6); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_7, grammarAccess.getIQLClassAccess().getCommaKeyword_4_2_0());
                    	      				
                    	    }
                    	    // InternalODL.g:1397:5: ( (lv_extendedInterfaces_8_0= ruleJvmTypeReference ) )
                    	    // InternalODL.g:1398:6: (lv_extendedInterfaces_8_0= ruleJvmTypeReference )
                    	    {
                    	    // InternalODL.g:1398:6: (lv_extendedInterfaces_8_0= ruleJvmTypeReference )
                    	    // InternalODL.g:1399:7: lv_extendedInterfaces_8_0= ruleJvmTypeReference
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLClassAccess().getExtendedInterfacesJvmTypeReferenceParserRuleCall_4_2_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_27);
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
                    	    break loop26;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_9=(Token)match(input,16,FOLLOW_11); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_9, grammarAccess.getIQLClassAccess().getLeftCurlyBracketKeyword_5());
              		
            }
            // InternalODL.g:1422:3: ( ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) ) )*
            loop29:
            do {
                int alt29=2;
                int LA29_0 = input.LA(1);

                if ( (LA29_0==RULE_ID||LA29_0==23||LA29_0==92) ) {
                    alt29=1;
                }


                switch (alt29) {
            	case 1 :
            	    // InternalODL.g:1423:4: ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) )
            	    {
            	    // InternalODL.g:1423:4: ( (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember ) )
            	    // InternalODL.g:1424:5: (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember )
            	    {
            	    // InternalODL.g:1424:5: (lv_members_10_1= ruleIQLAttribute | lv_members_10_2= ruleIQLMethod | lv_members_10_3= ruleIQLJavaMember )
            	    int alt28=3;
            	    switch ( input.LA(1) ) {
            	    case RULE_ID:
            	        {
            	        int LA28_1 = input.LA(2);

            	        if ( (LA28_1==RULE_ID||LA28_1==64||LA28_1==91) ) {
            	            alt28=1;
            	        }
            	        else if ( (LA28_1==14||LA28_1==16||LA28_1==27) ) {
            	            alt28=2;
            	        }
            	        else {
            	            if (state.backtracking>0) {state.failed=true; return current;}
            	            NoViableAltException nvae =
            	                new NoViableAltException("", 28, 1, input);

            	            throw nvae;
            	        }
            	        }
            	        break;
            	    case 23:
            	        {
            	        alt28=2;
            	        }
            	        break;
            	    case 92:
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
            	            // InternalODL.g:1425:6: lv_members_10_1= ruleIQLAttribute
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getIQLClassAccess().getMembersIQLAttributeParserRuleCall_6_0_0());
            	              					
            	            }
            	            pushFollow(FOLLOW_11);
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
            	            // InternalODL.g:1441:6: lv_members_10_2= ruleIQLMethod
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getIQLClassAccess().getMembersIQLMethodParserRuleCall_6_0_1());
            	              					
            	            }
            	            pushFollow(FOLLOW_11);
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
            	            // InternalODL.g:1457:6: lv_members_10_3= ruleIQLJavaMember
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getIQLClassAccess().getMembersIQLJavaMemberParserRuleCall_6_0_2());
            	              					
            	            }
            	            pushFollow(FOLLOW_11);
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
            	    break loop29;
                }
            } while (true);

            otherlv_11=(Token)match(input,17,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:1483:1: entryRuleIQLInterface returns [EObject current=null] : iv_ruleIQLInterface= ruleIQLInterface EOF ;
    public final EObject entryRuleIQLInterface() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLInterface = null;


        try {
            // InternalODL.g:1483:53: (iv_ruleIQLInterface= ruleIQLInterface EOF )
            // InternalODL.g:1484:2: iv_ruleIQLInterface= ruleIQLInterface EOF
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
    // InternalODL.g:1490:1: ruleIQLInterface returns [EObject current=null] : ( () otherlv_1= 'interface' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )? otherlv_7= '{' ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )* otherlv_9= '}' ) ;
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
            // InternalODL.g:1496:2: ( ( () otherlv_1= 'interface' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )? otherlv_7= '{' ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )* otherlv_9= '}' ) )
            // InternalODL.g:1497:2: ( () otherlv_1= 'interface' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )? otherlv_7= '{' ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )* otherlv_9= '}' )
            {
            // InternalODL.g:1497:2: ( () otherlv_1= 'interface' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )? otherlv_7= '{' ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )* otherlv_9= '}' )
            // InternalODL.g:1498:3: () otherlv_1= 'interface' ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )? otherlv_7= '{' ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )* otherlv_9= '}'
            {
            // InternalODL.g:1498:3: ()
            // InternalODL.g:1499:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLInterfaceAccess().getIQLInterfaceAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,73,FOLLOW_6); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLInterfaceAccess().getInterfaceKeyword_1());
              		
            }
            // InternalODL.g:1509:3: ( (lv_simpleName_2_0= RULE_ID ) )
            // InternalODL.g:1510:4: (lv_simpleName_2_0= RULE_ID )
            {
            // InternalODL.g:1510:4: (lv_simpleName_2_0= RULE_ID )
            // InternalODL.g:1511:5: lv_simpleName_2_0= RULE_ID
            {
            lv_simpleName_2_0=(Token)match(input,RULE_ID,FOLLOW_28); if (state.failed) return current;
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

            // InternalODL.g:1527:3: (otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )? )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==71) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // InternalODL.g:1528:4: otherlv_3= 'extends' ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) ) (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )?
                    {
                    otherlv_3=(Token)match(input,71,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getIQLInterfaceAccess().getExtendsKeyword_3_0());
                      			
                    }
                    // InternalODL.g:1532:4: ( (lv_extendedInterfaces_4_0= ruleJvmTypeReference ) )
                    // InternalODL.g:1533:5: (lv_extendedInterfaces_4_0= ruleJvmTypeReference )
                    {
                    // InternalODL.g:1533:5: (lv_extendedInterfaces_4_0= ruleJvmTypeReference )
                    // InternalODL.g:1534:6: lv_extendedInterfaces_4_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLInterfaceAccess().getExtendedInterfacesJvmTypeReferenceParserRuleCall_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_27);
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

                    // InternalODL.g:1551:4: (otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) ) )?
                    int alt30=2;
                    int LA30_0 = input.LA(1);

                    if ( (LA30_0==26) ) {
                        alt30=1;
                    }
                    switch (alt30) {
                        case 1 :
                            // InternalODL.g:1552:5: otherlv_5= ',' ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) )
                            {
                            otherlv_5=(Token)match(input,26,FOLLOW_6); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              					newLeafNode(otherlv_5, grammarAccess.getIQLInterfaceAccess().getCommaKeyword_3_2_0());
                              				
                            }
                            // InternalODL.g:1556:5: ( (lv_extendedInterfaces_6_0= ruleJvmTypeReference ) )
                            // InternalODL.g:1557:6: (lv_extendedInterfaces_6_0= ruleJvmTypeReference )
                            {
                            // InternalODL.g:1557:6: (lv_extendedInterfaces_6_0= ruleJvmTypeReference )
                            // InternalODL.g:1558:7: lv_extendedInterfaces_6_0= ruleJvmTypeReference
                            {
                            if ( state.backtracking==0 ) {

                              							newCompositeNode(grammarAccess.getIQLInterfaceAccess().getExtendedInterfacesJvmTypeReferenceParserRuleCall_3_2_1_0());
                              						
                            }
                            pushFollow(FOLLOW_10);
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

            otherlv_7=(Token)match(input,16,FOLLOW_11); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_7, grammarAccess.getIQLInterfaceAccess().getLeftCurlyBracketKeyword_4());
              		
            }
            // InternalODL.g:1581:3: ( ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) ) )*
            loop33:
            do {
                int alt33=2;
                int LA33_0 = input.LA(1);

                if ( (LA33_0==RULE_ID||LA33_0==92) ) {
                    alt33=1;
                }


                switch (alt33) {
            	case 1 :
            	    // InternalODL.g:1582:4: ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) )
            	    {
            	    // InternalODL.g:1582:4: ( (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember ) )
            	    // InternalODL.g:1583:5: (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember )
            	    {
            	    // InternalODL.g:1583:5: (lv_members_8_1= ruleIQLMethodDeclaration | lv_members_8_2= ruleIQLJavaMember )
            	    int alt32=2;
            	    int LA32_0 = input.LA(1);

            	    if ( (LA32_0==RULE_ID) ) {
            	        alt32=1;
            	    }
            	    else if ( (LA32_0==92) ) {
            	        alt32=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return current;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 32, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt32) {
            	        case 1 :
            	            // InternalODL.g:1584:6: lv_members_8_1= ruleIQLMethodDeclaration
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getIQLInterfaceAccess().getMembersIQLMethodDeclarationParserRuleCall_5_0_0());
            	              					
            	            }
            	            pushFollow(FOLLOW_11);
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
            	            // InternalODL.g:1600:6: lv_members_8_2= ruleIQLJavaMember
            	            {
            	            if ( state.backtracking==0 ) {

            	              						newCompositeNode(grammarAccess.getIQLInterfaceAccess().getMembersIQLJavaMemberParserRuleCall_5_0_1());
            	              					
            	            }
            	            pushFollow(FOLLOW_11);
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
            	    break loop33;
                }
            } while (true);

            otherlv_9=(Token)match(input,17,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:1626:1: entryRuleIQLJavaMetadata returns [EObject current=null] : iv_ruleIQLJavaMetadata= ruleIQLJavaMetadata EOF ;
    public final EObject entryRuleIQLJavaMetadata() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLJavaMetadata = null;


        try {
            // InternalODL.g:1626:56: (iv_ruleIQLJavaMetadata= ruleIQLJavaMetadata EOF )
            // InternalODL.g:1627:2: iv_ruleIQLJavaMetadata= ruleIQLJavaMetadata EOF
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
    // InternalODL.g:1633:1: ruleIQLJavaMetadata returns [EObject current=null] : ( (lv_java_0_0= ruleIQLJava ) ) ;
    public final EObject ruleIQLJavaMetadata() throws RecognitionException {
        EObject current = null;

        EObject lv_java_0_0 = null;



        	enterRule();

        try {
            // InternalODL.g:1639:2: ( ( (lv_java_0_0= ruleIQLJava ) ) )
            // InternalODL.g:1640:2: ( (lv_java_0_0= ruleIQLJava ) )
            {
            // InternalODL.g:1640:2: ( (lv_java_0_0= ruleIQLJava ) )
            // InternalODL.g:1641:3: (lv_java_0_0= ruleIQLJava )
            {
            // InternalODL.g:1641:3: (lv_java_0_0= ruleIQLJava )
            // InternalODL.g:1642:4: lv_java_0_0= ruleIQLJava
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
    // InternalODL.g:1662:1: entryRuleIQLAttribute returns [EObject current=null] : iv_ruleIQLAttribute= ruleIQLAttribute EOF ;
    public final EObject entryRuleIQLAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLAttribute = null;


        try {
            // InternalODL.g:1662:53: (iv_ruleIQLAttribute= ruleIQLAttribute EOF )
            // InternalODL.g:1663:2: iv_ruleIQLAttribute= ruleIQLAttribute EOF
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
    // InternalODL.g:1669:1: ruleIQLAttribute returns [EObject current=null] : ( () ( (lv_type_1_0= ruleJvmTypeReference ) ) ( (lv_simpleName_2_0= RULE_ID ) ) ( (lv_init_3_0= ruleIQLVariableInitialization ) )? otherlv_4= ';' ) ;
    public final EObject ruleIQLAttribute() throws RecognitionException {
        EObject current = null;

        Token lv_simpleName_2_0=null;
        Token otherlv_4=null;
        EObject lv_type_1_0 = null;

        EObject lv_init_3_0 = null;



        	enterRule();

        try {
            // InternalODL.g:1675:2: ( ( () ( (lv_type_1_0= ruleJvmTypeReference ) ) ( (lv_simpleName_2_0= RULE_ID ) ) ( (lv_init_3_0= ruleIQLVariableInitialization ) )? otherlv_4= ';' ) )
            // InternalODL.g:1676:2: ( () ( (lv_type_1_0= ruleJvmTypeReference ) ) ( (lv_simpleName_2_0= RULE_ID ) ) ( (lv_init_3_0= ruleIQLVariableInitialization ) )? otherlv_4= ';' )
            {
            // InternalODL.g:1676:2: ( () ( (lv_type_1_0= ruleJvmTypeReference ) ) ( (lv_simpleName_2_0= RULE_ID ) ) ( (lv_init_3_0= ruleIQLVariableInitialization ) )? otherlv_4= ';' )
            // InternalODL.g:1677:3: () ( (lv_type_1_0= ruleJvmTypeReference ) ) ( (lv_simpleName_2_0= RULE_ID ) ) ( (lv_init_3_0= ruleIQLVariableInitialization ) )? otherlv_4= ';'
            {
            // InternalODL.g:1677:3: ()
            // InternalODL.g:1678:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLAttributeAccess().getIQLAttributeAction_0(),
              					current);
              			
            }

            }

            // InternalODL.g:1684:3: ( (lv_type_1_0= ruleJvmTypeReference ) )
            // InternalODL.g:1685:4: (lv_type_1_0= ruleJvmTypeReference )
            {
            // InternalODL.g:1685:4: (lv_type_1_0= ruleJvmTypeReference )
            // InternalODL.g:1686:5: lv_type_1_0= ruleJvmTypeReference
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

            // InternalODL.g:1703:3: ( (lv_simpleName_2_0= RULE_ID ) )
            // InternalODL.g:1704:4: (lv_simpleName_2_0= RULE_ID )
            {
            // InternalODL.g:1704:4: (lv_simpleName_2_0= RULE_ID )
            // InternalODL.g:1705:5: lv_simpleName_2_0= RULE_ID
            {
            lv_simpleName_2_0=(Token)match(input,RULE_ID,FOLLOW_14); if (state.failed) return current;
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

            // InternalODL.g:1721:3: ( (lv_init_3_0= ruleIQLVariableInitialization ) )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==14||LA34_0==16||LA34_0==49) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // InternalODL.g:1722:4: (lv_init_3_0= ruleIQLVariableInitialization )
                    {
                    // InternalODL.g:1722:4: (lv_init_3_0= ruleIQLVariableInitialization )
                    // InternalODL.g:1723:5: lv_init_3_0= ruleIQLVariableInitialization
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getIQLAttributeAccess().getInitIQLVariableInitializationParserRuleCall_3_0());
                      				
                    }
                    pushFollow(FOLLOW_15);
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

            otherlv_4=(Token)match(input,20,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:1748:1: entryRuleJvmTypeReference returns [EObject current=null] : iv_ruleJvmTypeReference= ruleJvmTypeReference EOF ;
    public final EObject entryRuleJvmTypeReference() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJvmTypeReference = null;


        try {
            // InternalODL.g:1748:57: (iv_ruleJvmTypeReference= ruleJvmTypeReference EOF )
            // InternalODL.g:1749:2: iv_ruleJvmTypeReference= ruleJvmTypeReference EOF
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
    // InternalODL.g:1755:1: ruleJvmTypeReference returns [EObject current=null] : (this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef | this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef ) ;
    public final EObject ruleJvmTypeReference() throws RecognitionException {
        EObject current = null;

        EObject this_IQLSimpleTypeRef_0 = null;

        EObject this_IQLArrayTypeRef_1 = null;



        	enterRule();

        try {
            // InternalODL.g:1761:2: ( (this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef | this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef ) )
            // InternalODL.g:1762:2: (this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef | this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef )
            {
            // InternalODL.g:1762:2: (this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef | this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef )
            int alt35=2;
            alt35 = dfa35.predict(input);
            switch (alt35) {
                case 1 :
                    // InternalODL.g:1763:3: this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef
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
                    // InternalODL.g:1772:3: this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef
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
    // InternalODL.g:1784:1: entryRuleIQLSimpleTypeRef returns [EObject current=null] : iv_ruleIQLSimpleTypeRef= ruleIQLSimpleTypeRef EOF ;
    public final EObject entryRuleIQLSimpleTypeRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLSimpleTypeRef = null;


        try {
            // InternalODL.g:1784:57: (iv_ruleIQLSimpleTypeRef= ruleIQLSimpleTypeRef EOF )
            // InternalODL.g:1785:2: iv_ruleIQLSimpleTypeRef= ruleIQLSimpleTypeRef EOF
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
    // InternalODL.g:1791:1: ruleIQLSimpleTypeRef returns [EObject current=null] : ( () ( ( ruleQualifiedName ) ) ) ;
    public final EObject ruleIQLSimpleTypeRef() throws RecognitionException {
        EObject current = null;


        	enterRule();

        try {
            // InternalODL.g:1797:2: ( ( () ( ( ruleQualifiedName ) ) ) )
            // InternalODL.g:1798:2: ( () ( ( ruleQualifiedName ) ) )
            {
            // InternalODL.g:1798:2: ( () ( ( ruleQualifiedName ) ) )
            // InternalODL.g:1799:3: () ( ( ruleQualifiedName ) )
            {
            // InternalODL.g:1799:3: ()
            // InternalODL.g:1800:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLSimpleTypeRefAccess().getIQLSimpleTypeRefAction_0(),
              					current);
              			
            }

            }

            // InternalODL.g:1806:3: ( ( ruleQualifiedName ) )
            // InternalODL.g:1807:4: ( ruleQualifiedName )
            {
            // InternalODL.g:1807:4: ( ruleQualifiedName )
            // InternalODL.g:1808:5: ruleQualifiedName
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
    // InternalODL.g:1826:1: entryRuleIQLArrayTypeRef returns [EObject current=null] : iv_ruleIQLArrayTypeRef= ruleIQLArrayTypeRef EOF ;
    public final EObject entryRuleIQLArrayTypeRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLArrayTypeRef = null;


        try {
            // InternalODL.g:1826:56: (iv_ruleIQLArrayTypeRef= ruleIQLArrayTypeRef EOF )
            // InternalODL.g:1827:2: iv_ruleIQLArrayTypeRef= ruleIQLArrayTypeRef EOF
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
    // InternalODL.g:1833:1: ruleIQLArrayTypeRef returns [EObject current=null] : ( () ( (lv_type_1_0= ruleIQLArrayType ) ) ) ;
    public final EObject ruleIQLArrayTypeRef() throws RecognitionException {
        EObject current = null;

        EObject lv_type_1_0 = null;



        	enterRule();

        try {
            // InternalODL.g:1839:2: ( ( () ( (lv_type_1_0= ruleIQLArrayType ) ) ) )
            // InternalODL.g:1840:2: ( () ( (lv_type_1_0= ruleIQLArrayType ) ) )
            {
            // InternalODL.g:1840:2: ( () ( (lv_type_1_0= ruleIQLArrayType ) ) )
            // InternalODL.g:1841:3: () ( (lv_type_1_0= ruleIQLArrayType ) )
            {
            // InternalODL.g:1841:3: ()
            // InternalODL.g:1842:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLArrayTypeRefAccess().getIQLArrayTypeRefAction_0(),
              					current);
              			
            }

            }

            // InternalODL.g:1848:3: ( (lv_type_1_0= ruleIQLArrayType ) )
            // InternalODL.g:1849:4: (lv_type_1_0= ruleIQLArrayType )
            {
            // InternalODL.g:1849:4: (lv_type_1_0= ruleIQLArrayType )
            // InternalODL.g:1850:5: lv_type_1_0= ruleIQLArrayType
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
    // InternalODL.g:1871:1: entryRuleIQLArrayType returns [EObject current=null] : iv_ruleIQLArrayType= ruleIQLArrayType EOF ;
    public final EObject entryRuleIQLArrayType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLArrayType = null;


        try {
            // InternalODL.g:1871:53: (iv_ruleIQLArrayType= ruleIQLArrayType EOF )
            // InternalODL.g:1872:2: iv_ruleIQLArrayType= ruleIQLArrayType EOF
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
    // InternalODL.g:1878:1: ruleIQLArrayType returns [EObject current=null] : ( () ( ( ruleQualifiedName ) ) ( (lv_dimensions_2_0= ruleArrayBrackets ) )+ ) ;
    public final EObject ruleIQLArrayType() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_dimensions_2_0 = null;



        	enterRule();

        try {
            // InternalODL.g:1884:2: ( ( () ( ( ruleQualifiedName ) ) ( (lv_dimensions_2_0= ruleArrayBrackets ) )+ ) )
            // InternalODL.g:1885:2: ( () ( ( ruleQualifiedName ) ) ( (lv_dimensions_2_0= ruleArrayBrackets ) )+ )
            {
            // InternalODL.g:1885:2: ( () ( ( ruleQualifiedName ) ) ( (lv_dimensions_2_0= ruleArrayBrackets ) )+ )
            // InternalODL.g:1886:3: () ( ( ruleQualifiedName ) ) ( (lv_dimensions_2_0= ruleArrayBrackets ) )+
            {
            // InternalODL.g:1886:3: ()
            // InternalODL.g:1887:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLArrayTypeAccess().getIQLArrayTypeAction_0(),
              					current);
              			
            }

            }

            // InternalODL.g:1893:3: ( ( ruleQualifiedName ) )
            // InternalODL.g:1894:4: ( ruleQualifiedName )
            {
            // InternalODL.g:1894:4: ( ruleQualifiedName )
            // InternalODL.g:1895:5: ruleQualifiedName
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLArrayTypeRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLArrayTypeAccess().getComponentTypeJvmTypeCrossReference_1_0());
              				
            }
            pushFollow(FOLLOW_29);
            ruleQualifiedName();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalODL.g:1909:3: ( (lv_dimensions_2_0= ruleArrayBrackets ) )+
            int cnt36=0;
            loop36:
            do {
                int alt36=2;
                int LA36_0 = input.LA(1);

                if ( (LA36_0==64) ) {
                    int LA36_2 = input.LA(2);

                    if ( (LA36_2==65) ) {
                        alt36=1;
                    }


                }


                switch (alt36) {
            	case 1 :
            	    // InternalODL.g:1910:4: (lv_dimensions_2_0= ruleArrayBrackets )
            	    {
            	    // InternalODL.g:1910:4: (lv_dimensions_2_0= ruleArrayBrackets )
            	    // InternalODL.g:1911:5: lv_dimensions_2_0= ruleArrayBrackets
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLArrayTypeAccess().getDimensionsArrayBracketsParserRuleCall_2_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_30);
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
            	    if ( cnt36 >= 1 ) break loop36;
            	    if (state.backtracking>0) {state.failed=true; return current;}
                        EarlyExitException eee =
                            new EarlyExitException(36, input);
                        throw eee;
                }
                cnt36++;
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
    // InternalODL.g:1932:1: entryRuleArrayBrackets returns [String current=null] : iv_ruleArrayBrackets= ruleArrayBrackets EOF ;
    public final String entryRuleArrayBrackets() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleArrayBrackets = null;


        try {
            // InternalODL.g:1932:53: (iv_ruleArrayBrackets= ruleArrayBrackets EOF )
            // InternalODL.g:1933:2: iv_ruleArrayBrackets= ruleArrayBrackets EOF
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
    // InternalODL.g:1939:1: ruleArrayBrackets returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '[' kw= ']' ) ;
    public final AntlrDatatypeRuleToken ruleArrayBrackets() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalODL.g:1945:2: ( (kw= '[' kw= ']' ) )
            // InternalODL.g:1946:2: (kw= '[' kw= ']' )
            {
            // InternalODL.g:1946:2: (kw= '[' kw= ']' )
            // InternalODL.g:1947:3: kw= '[' kw= ']'
            {
            kw=(Token)match(input,64,FOLLOW_31); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current.merge(kw);
              			newLeafNode(kw, grammarAccess.getArrayBracketsAccess().getLeftSquareBracketKeyword_0());
              		
            }
            kw=(Token)match(input,65,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:1961:1: entryRuleJvmFormalParameter returns [EObject current=null] : iv_ruleJvmFormalParameter= ruleJvmFormalParameter EOF ;
    public final EObject entryRuleJvmFormalParameter() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJvmFormalParameter = null;


        try {
            // InternalODL.g:1961:59: (iv_ruleJvmFormalParameter= ruleJvmFormalParameter EOF )
            // InternalODL.g:1962:2: iv_ruleJvmFormalParameter= ruleJvmFormalParameter EOF
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
    // InternalODL.g:1968:1: ruleJvmFormalParameter returns [EObject current=null] : ( ( (lv_parameterType_0_0= ruleJvmTypeReference ) ) ( (lv_name_1_0= RULE_ID ) ) ) ;
    public final EObject ruleJvmFormalParameter() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_0=null;
        EObject lv_parameterType_0_0 = null;



        	enterRule();

        try {
            // InternalODL.g:1974:2: ( ( ( (lv_parameterType_0_0= ruleJvmTypeReference ) ) ( (lv_name_1_0= RULE_ID ) ) ) )
            // InternalODL.g:1975:2: ( ( (lv_parameterType_0_0= ruleJvmTypeReference ) ) ( (lv_name_1_0= RULE_ID ) ) )
            {
            // InternalODL.g:1975:2: ( ( (lv_parameterType_0_0= ruleJvmTypeReference ) ) ( (lv_name_1_0= RULE_ID ) ) )
            // InternalODL.g:1976:3: ( (lv_parameterType_0_0= ruleJvmTypeReference ) ) ( (lv_name_1_0= RULE_ID ) )
            {
            // InternalODL.g:1976:3: ( (lv_parameterType_0_0= ruleJvmTypeReference ) )
            // InternalODL.g:1977:4: (lv_parameterType_0_0= ruleJvmTypeReference )
            {
            // InternalODL.g:1977:4: (lv_parameterType_0_0= ruleJvmTypeReference )
            // InternalODL.g:1978:5: lv_parameterType_0_0= ruleJvmTypeReference
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

            // InternalODL.g:1995:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalODL.g:1996:4: (lv_name_1_0= RULE_ID )
            {
            // InternalODL.g:1996:4: (lv_name_1_0= RULE_ID )
            // InternalODL.g:1997:5: lv_name_1_0= RULE_ID
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
    // InternalODL.g:2017:1: entryRuleIQLMethod returns [EObject current=null] : iv_ruleIQLMethod= ruleIQLMethod EOF ;
    public final EObject entryRuleIQLMethod() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMethod = null;


        try {
            // InternalODL.g:2017:50: (iv_ruleIQLMethod= ruleIQLMethod EOF )
            // InternalODL.g:2018:2: iv_ruleIQLMethod= ruleIQLMethod EOF
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
    // InternalODL.g:2024:1: ruleIQLMethod returns [EObject current=null] : ( () ( (lv_override_1_0= 'override' ) )? ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )? (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )? ( (lv_body_10_0= ruleIQLStatementBlock ) ) ) ;
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
            // InternalODL.g:2030:2: ( ( () ( (lv_override_1_0= 'override' ) )? ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )? (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )? ( (lv_body_10_0= ruleIQLStatementBlock ) ) ) )
            // InternalODL.g:2031:2: ( () ( (lv_override_1_0= 'override' ) )? ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )? (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )? ( (lv_body_10_0= ruleIQLStatementBlock ) ) )
            {
            // InternalODL.g:2031:2: ( () ( (lv_override_1_0= 'override' ) )? ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )? (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )? ( (lv_body_10_0= ruleIQLStatementBlock ) ) )
            // InternalODL.g:2032:3: () ( (lv_override_1_0= 'override' ) )? ( (lv_simpleName_2_0= RULE_ID ) ) (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )? (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )? ( (lv_body_10_0= ruleIQLStatementBlock ) )
            {
            // InternalODL.g:2032:3: ()
            // InternalODL.g:2033:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLMethodAccess().getIQLMethodAction_0(),
              					current);
              			
            }

            }

            // InternalODL.g:2039:3: ( (lv_override_1_0= 'override' ) )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==23) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // InternalODL.g:2040:4: (lv_override_1_0= 'override' )
                    {
                    // InternalODL.g:2040:4: (lv_override_1_0= 'override' )
                    // InternalODL.g:2041:5: lv_override_1_0= 'override'
                    {
                    lv_override_1_0=(Token)match(input,23,FOLLOW_6); if (state.failed) return current;
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

            // InternalODL.g:2053:3: ( (lv_simpleName_2_0= RULE_ID ) )
            // InternalODL.g:2054:4: (lv_simpleName_2_0= RULE_ID )
            {
            // InternalODL.g:2054:4: (lv_simpleName_2_0= RULE_ID )
            // InternalODL.g:2055:5: lv_simpleName_2_0= RULE_ID
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

            // InternalODL.g:2071:3: (otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')' )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==14) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // InternalODL.g:2072:4: otherlv_3= '(' ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )? otherlv_7= ')'
                    {
                    otherlv_3=(Token)match(input,14,FOLLOW_8); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getIQLMethodAccess().getLeftParenthesisKeyword_3_0());
                      			
                    }
                    // InternalODL.g:2076:4: ( ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )* )?
                    int alt39=2;
                    int LA39_0 = input.LA(1);

                    if ( (LA39_0==RULE_ID) ) {
                        alt39=1;
                    }
                    switch (alt39) {
                        case 1 :
                            // InternalODL.g:2077:5: ( (lv_parameters_4_0= ruleJvmFormalParameter ) ) (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )*
                            {
                            // InternalODL.g:2077:5: ( (lv_parameters_4_0= ruleJvmFormalParameter ) )
                            // InternalODL.g:2078:6: (lv_parameters_4_0= ruleJvmFormalParameter )
                            {
                            // InternalODL.g:2078:6: (lv_parameters_4_0= ruleJvmFormalParameter )
                            // InternalODL.g:2079:7: lv_parameters_4_0= ruleJvmFormalParameter
                            {
                            if ( state.backtracking==0 ) {

                              							newCompositeNode(grammarAccess.getIQLMethodAccess().getParametersJvmFormalParameterParserRuleCall_3_1_0_0());
                              						
                            }
                            pushFollow(FOLLOW_20);
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

                            // InternalODL.g:2096:5: (otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) ) )*
                            loop38:
                            do {
                                int alt38=2;
                                int LA38_0 = input.LA(1);

                                if ( (LA38_0==26) ) {
                                    alt38=1;
                                }


                                switch (alt38) {
                            	case 1 :
                            	    // InternalODL.g:2097:6: otherlv_5= ',' ( (lv_parameters_6_0= ruleJvmFormalParameter ) )
                            	    {
                            	    otherlv_5=(Token)match(input,26,FOLLOW_6); if (state.failed) return current;
                            	    if ( state.backtracking==0 ) {

                            	      						newLeafNode(otherlv_5, grammarAccess.getIQLMethodAccess().getCommaKeyword_3_1_1_0());
                            	      					
                            	    }
                            	    // InternalODL.g:2101:6: ( (lv_parameters_6_0= ruleJvmFormalParameter ) )
                            	    // InternalODL.g:2102:7: (lv_parameters_6_0= ruleJvmFormalParameter )
                            	    {
                            	    // InternalODL.g:2102:7: (lv_parameters_6_0= ruleJvmFormalParameter )
                            	    // InternalODL.g:2103:8: lv_parameters_6_0= ruleJvmFormalParameter
                            	    {
                            	    if ( state.backtracking==0 ) {

                            	      								newCompositeNode(grammarAccess.getIQLMethodAccess().getParametersJvmFormalParameterParserRuleCall_3_1_1_1_0());
                            	      							
                            	    }
                            	    pushFollow(FOLLOW_20);
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
                            	    break loop38;
                                }
                            } while (true);


                            }
                            break;

                    }

                    otherlv_7=(Token)match(input,15,FOLLOW_21); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_7, grammarAccess.getIQLMethodAccess().getRightParenthesisKeyword_3_2());
                      			
                    }

                    }
                    break;

            }

            // InternalODL.g:2127:3: (otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) ) )?
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( (LA41_0==27) ) {
                alt41=1;
            }
            switch (alt41) {
                case 1 :
                    // InternalODL.g:2128:4: otherlv_8= ':' ( (lv_returnType_9_0= ruleJvmTypeReference ) )
                    {
                    otherlv_8=(Token)match(input,27,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_8, grammarAccess.getIQLMethodAccess().getColonKeyword_4_0());
                      			
                    }
                    // InternalODL.g:2132:4: ( (lv_returnType_9_0= ruleJvmTypeReference ) )
                    // InternalODL.g:2133:5: (lv_returnType_9_0= ruleJvmTypeReference )
                    {
                    // InternalODL.g:2133:5: (lv_returnType_9_0= ruleJvmTypeReference )
                    // InternalODL.g:2134:6: lv_returnType_9_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMethodAccess().getReturnTypeJvmTypeReferenceParserRuleCall_4_1_0());
                      					
                    }
                    pushFollow(FOLLOW_16);
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

            // InternalODL.g:2152:3: ( (lv_body_10_0= ruleIQLStatementBlock ) )
            // InternalODL.g:2153:4: (lv_body_10_0= ruleIQLStatementBlock )
            {
            // InternalODL.g:2153:4: (lv_body_10_0= ruleIQLStatementBlock )
            // InternalODL.g:2154:5: lv_body_10_0= ruleIQLStatementBlock
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
    // InternalODL.g:2175:1: entryRuleIQLMethodDeclaration returns [EObject current=null] : iv_ruleIQLMethodDeclaration= ruleIQLMethodDeclaration EOF ;
    public final EObject entryRuleIQLMethodDeclaration() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMethodDeclaration = null;


        try {
            // InternalODL.g:2175:61: (iv_ruleIQLMethodDeclaration= ruleIQLMethodDeclaration EOF )
            // InternalODL.g:2176:2: iv_ruleIQLMethodDeclaration= ruleIQLMethodDeclaration EOF
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
    // InternalODL.g:2182:1: ruleIQLMethodDeclaration returns [EObject current=null] : ( () ( (lv_simpleName_1_0= RULE_ID ) ) otherlv_2= '(' ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )? otherlv_6= ')' (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )? otherlv_9= ';' ) ;
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
            // InternalODL.g:2188:2: ( ( () ( (lv_simpleName_1_0= RULE_ID ) ) otherlv_2= '(' ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )? otherlv_6= ')' (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )? otherlv_9= ';' ) )
            // InternalODL.g:2189:2: ( () ( (lv_simpleName_1_0= RULE_ID ) ) otherlv_2= '(' ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )? otherlv_6= ')' (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )? otherlv_9= ';' )
            {
            // InternalODL.g:2189:2: ( () ( (lv_simpleName_1_0= RULE_ID ) ) otherlv_2= '(' ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )? otherlv_6= ')' (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )? otherlv_9= ';' )
            // InternalODL.g:2190:3: () ( (lv_simpleName_1_0= RULE_ID ) ) otherlv_2= '(' ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )? otherlv_6= ')' (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )? otherlv_9= ';'
            {
            // InternalODL.g:2190:3: ()
            // InternalODL.g:2191:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLMethodDeclarationAccess().getIQLMethodDeclarationAction_0(),
              					current);
              			
            }

            }

            // InternalODL.g:2197:3: ( (lv_simpleName_1_0= RULE_ID ) )
            // InternalODL.g:2198:4: (lv_simpleName_1_0= RULE_ID )
            {
            // InternalODL.g:2198:4: (lv_simpleName_1_0= RULE_ID )
            // InternalODL.g:2199:5: lv_simpleName_1_0= RULE_ID
            {
            lv_simpleName_1_0=(Token)match(input,RULE_ID,FOLLOW_32); if (state.failed) return current;
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
            // InternalODL.g:2219:3: ( ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )* )?
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==RULE_ID) ) {
                alt43=1;
            }
            switch (alt43) {
                case 1 :
                    // InternalODL.g:2220:4: ( (lv_parameters_3_0= ruleJvmFormalParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )*
                    {
                    // InternalODL.g:2220:4: ( (lv_parameters_3_0= ruleJvmFormalParameter ) )
                    // InternalODL.g:2221:5: (lv_parameters_3_0= ruleJvmFormalParameter )
                    {
                    // InternalODL.g:2221:5: (lv_parameters_3_0= ruleJvmFormalParameter )
                    // InternalODL.g:2222:6: lv_parameters_3_0= ruleJvmFormalParameter
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMethodDeclarationAccess().getParametersJvmFormalParameterParserRuleCall_3_0_0());
                      					
                    }
                    pushFollow(FOLLOW_20);
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

                    // InternalODL.g:2239:4: (otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) ) )*
                    loop42:
                    do {
                        int alt42=2;
                        int LA42_0 = input.LA(1);

                        if ( (LA42_0==26) ) {
                            alt42=1;
                        }


                        switch (alt42) {
                    	case 1 :
                    	    // InternalODL.g:2240:5: otherlv_4= ',' ( (lv_parameters_5_0= ruleJvmFormalParameter ) )
                    	    {
                    	    otherlv_4=(Token)match(input,26,FOLLOW_6); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_4, grammarAccess.getIQLMethodDeclarationAccess().getCommaKeyword_3_1_0());
                    	      				
                    	    }
                    	    // InternalODL.g:2244:5: ( (lv_parameters_5_0= ruleJvmFormalParameter ) )
                    	    // InternalODL.g:2245:6: (lv_parameters_5_0= ruleJvmFormalParameter )
                    	    {
                    	    // InternalODL.g:2245:6: (lv_parameters_5_0= ruleJvmFormalParameter )
                    	    // InternalODL.g:2246:7: lv_parameters_5_0= ruleJvmFormalParameter
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLMethodDeclarationAccess().getParametersJvmFormalParameterParserRuleCall_3_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_20);
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
                    	    break loop42;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_6=(Token)match(input,15,FOLLOW_33); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_6, grammarAccess.getIQLMethodDeclarationAccess().getRightParenthesisKeyword_4());
              		
            }
            // InternalODL.g:2269:3: (otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) ) )?
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==27) ) {
                alt44=1;
            }
            switch (alt44) {
                case 1 :
                    // InternalODL.g:2270:4: otherlv_7= ':' ( (lv_returnType_8_0= ruleJvmTypeReference ) )
                    {
                    otherlv_7=(Token)match(input,27,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_7, grammarAccess.getIQLMethodDeclarationAccess().getColonKeyword_5_0());
                      			
                    }
                    // InternalODL.g:2274:4: ( (lv_returnType_8_0= ruleJvmTypeReference ) )
                    // InternalODL.g:2275:5: (lv_returnType_8_0= ruleJvmTypeReference )
                    {
                    // InternalODL.g:2275:5: (lv_returnType_8_0= ruleJvmTypeReference )
                    // InternalODL.g:2276:6: lv_returnType_8_0= ruleJvmTypeReference
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMethodDeclarationAccess().getReturnTypeJvmTypeReferenceParserRuleCall_5_1_0());
                      					
                    }
                    pushFollow(FOLLOW_15);
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

            otherlv_9=(Token)match(input,20,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:2302:1: entryRuleIQLJavaMember returns [EObject current=null] : iv_ruleIQLJavaMember= ruleIQLJavaMember EOF ;
    public final EObject entryRuleIQLJavaMember() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLJavaMember = null;


        try {
            // InternalODL.g:2302:54: (iv_ruleIQLJavaMember= ruleIQLJavaMember EOF )
            // InternalODL.g:2303:2: iv_ruleIQLJavaMember= ruleIQLJavaMember EOF
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
    // InternalODL.g:2309:1: ruleIQLJavaMember returns [EObject current=null] : ( () ( (lv_java_1_0= ruleIQLJava ) ) ) ;
    public final EObject ruleIQLJavaMember() throws RecognitionException {
        EObject current = null;

        EObject lv_java_1_0 = null;



        	enterRule();

        try {
            // InternalODL.g:2315:2: ( ( () ( (lv_java_1_0= ruleIQLJava ) ) ) )
            // InternalODL.g:2316:2: ( () ( (lv_java_1_0= ruleIQLJava ) ) )
            {
            // InternalODL.g:2316:2: ( () ( (lv_java_1_0= ruleIQLJava ) ) )
            // InternalODL.g:2317:3: () ( (lv_java_1_0= ruleIQLJava ) )
            {
            // InternalODL.g:2317:3: ()
            // InternalODL.g:2318:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLJavaMemberAccess().getIQLJavaMemberAction_0(),
              					current);
              			
            }

            }

            // InternalODL.g:2324:3: ( (lv_java_1_0= ruleIQLJava ) )
            // InternalODL.g:2325:4: (lv_java_1_0= ruleIQLJava )
            {
            // InternalODL.g:2325:4: (lv_java_1_0= ruleIQLJava )
            // InternalODL.g:2326:5: lv_java_1_0= ruleIQLJava
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
    // InternalODL.g:2347:1: entryRuleIQLMetadataList returns [EObject current=null] : iv_ruleIQLMetadataList= ruleIQLMetadataList EOF ;
    public final EObject entryRuleIQLMetadataList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataList = null;


        try {
            // InternalODL.g:2347:56: (iv_ruleIQLMetadataList= ruleIQLMetadataList EOF )
            // InternalODL.g:2348:2: iv_ruleIQLMetadataList= ruleIQLMetadataList EOF
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
    // InternalODL.g:2354:1: ruleIQLMetadataList returns [EObject current=null] : ( ( (lv_elements_0_0= ruleIQLMetadata ) ) (otherlv_1= ',' ( (lv_elements_2_0= ruleIQLMetadata ) ) )* ) ;
    public final EObject ruleIQLMetadataList() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_elements_0_0 = null;

        EObject lv_elements_2_0 = null;



        	enterRule();

        try {
            // InternalODL.g:2360:2: ( ( ( (lv_elements_0_0= ruleIQLMetadata ) ) (otherlv_1= ',' ( (lv_elements_2_0= ruleIQLMetadata ) ) )* ) )
            // InternalODL.g:2361:2: ( ( (lv_elements_0_0= ruleIQLMetadata ) ) (otherlv_1= ',' ( (lv_elements_2_0= ruleIQLMetadata ) ) )* )
            {
            // InternalODL.g:2361:2: ( ( (lv_elements_0_0= ruleIQLMetadata ) ) (otherlv_1= ',' ( (lv_elements_2_0= ruleIQLMetadata ) ) )* )
            // InternalODL.g:2362:3: ( (lv_elements_0_0= ruleIQLMetadata ) ) (otherlv_1= ',' ( (lv_elements_2_0= ruleIQLMetadata ) ) )*
            {
            // InternalODL.g:2362:3: ( (lv_elements_0_0= ruleIQLMetadata ) )
            // InternalODL.g:2363:4: (lv_elements_0_0= ruleIQLMetadata )
            {
            // InternalODL.g:2363:4: (lv_elements_0_0= ruleIQLMetadata )
            // InternalODL.g:2364:5: lv_elements_0_0= ruleIQLMetadata
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLMetadataListAccess().getElementsIQLMetadataParserRuleCall_0_0());
              				
            }
            pushFollow(FOLLOW_34);
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

            // InternalODL.g:2381:3: (otherlv_1= ',' ( (lv_elements_2_0= ruleIQLMetadata ) ) )*
            loop45:
            do {
                int alt45=2;
                int LA45_0 = input.LA(1);

                if ( (LA45_0==26) ) {
                    alt45=1;
                }


                switch (alt45) {
            	case 1 :
            	    // InternalODL.g:2382:4: otherlv_1= ',' ( (lv_elements_2_0= ruleIQLMetadata ) )
            	    {
            	    otherlv_1=(Token)match(input,26,FOLLOW_6); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				newLeafNode(otherlv_1, grammarAccess.getIQLMetadataListAccess().getCommaKeyword_1_0());
            	      			
            	    }
            	    // InternalODL.g:2386:4: ( (lv_elements_2_0= ruleIQLMetadata ) )
            	    // InternalODL.g:2387:5: (lv_elements_2_0= ruleIQLMetadata )
            	    {
            	    // InternalODL.g:2387:5: (lv_elements_2_0= ruleIQLMetadata )
            	    // InternalODL.g:2388:6: lv_elements_2_0= ruleIQLMetadata
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLMetadataListAccess().getElementsIQLMetadataParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_34);
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
            	    break loop45;
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
    // InternalODL.g:2410:1: entryRuleIQLMetadata returns [EObject current=null] : iv_ruleIQLMetadata= ruleIQLMetadata EOF ;
    public final EObject entryRuleIQLMetadata() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadata = null;


        try {
            // InternalODL.g:2410:52: (iv_ruleIQLMetadata= ruleIQLMetadata EOF )
            // InternalODL.g:2411:2: iv_ruleIQLMetadata= ruleIQLMetadata EOF
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
    // InternalODL.g:2417:1: ruleIQLMetadata returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )? ) ;
    public final EObject ruleIQLMetadata() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalODL.g:2423:2: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )? ) )
            // InternalODL.g:2424:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )? )
            {
            // InternalODL.g:2424:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )? )
            // InternalODL.g:2425:3: ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )?
            {
            // InternalODL.g:2425:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalODL.g:2426:4: (lv_name_0_0= RULE_ID )
            {
            // InternalODL.g:2426:4: (lv_name_0_0= RULE_ID )
            // InternalODL.g:2427:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_35); if (state.failed) return current;
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

            // InternalODL.g:2443:3: (otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )?
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==49) ) {
                alt46=1;
            }
            switch (alt46) {
                case 1 :
                    // InternalODL.g:2444:4: otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) )
                    {
                    otherlv_1=(Token)match(input,49,FOLLOW_36); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_1, grammarAccess.getIQLMetadataAccess().getEqualsSignKeyword_1_0());
                      			
                    }
                    // InternalODL.g:2448:4: ( (lv_value_2_0= ruleIQLMetadataValue ) )
                    // InternalODL.g:2449:5: (lv_value_2_0= ruleIQLMetadataValue )
                    {
                    // InternalODL.g:2449:5: (lv_value_2_0= ruleIQLMetadataValue )
                    // InternalODL.g:2450:6: lv_value_2_0= ruleIQLMetadataValue
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
    // InternalODL.g:2472:1: entryRuleIQLMetadataValue returns [EObject current=null] : iv_ruleIQLMetadataValue= ruleIQLMetadataValue EOF ;
    public final EObject entryRuleIQLMetadataValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataValue = null;


        try {
            // InternalODL.g:2472:57: (iv_ruleIQLMetadataValue= ruleIQLMetadataValue EOF )
            // InternalODL.g:2473:2: iv_ruleIQLMetadataValue= ruleIQLMetadataValue EOF
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
    // InternalODL.g:2479:1: ruleIQLMetadataValue returns [EObject current=null] : (this_IQLMetadataValueSingle_0= ruleIQLMetadataValueSingle | this_IQLMetadataValueList_1= ruleIQLMetadataValueList | this_IQLMetadataValueMap_2= ruleIQLMetadataValueMap ) ;
    public final EObject ruleIQLMetadataValue() throws RecognitionException {
        EObject current = null;

        EObject this_IQLMetadataValueSingle_0 = null;

        EObject this_IQLMetadataValueList_1 = null;

        EObject this_IQLMetadataValueMap_2 = null;



        	enterRule();

        try {
            // InternalODL.g:2485:2: ( (this_IQLMetadataValueSingle_0= ruleIQLMetadataValueSingle | this_IQLMetadataValueList_1= ruleIQLMetadataValueList | this_IQLMetadataValueMap_2= ruleIQLMetadataValueMap ) )
            // InternalODL.g:2486:2: (this_IQLMetadataValueSingle_0= ruleIQLMetadataValueSingle | this_IQLMetadataValueList_1= ruleIQLMetadataValueList | this_IQLMetadataValueMap_2= ruleIQLMetadataValueMap )
            {
            // InternalODL.g:2486:2: (this_IQLMetadataValueSingle_0= ruleIQLMetadataValueSingle | this_IQLMetadataValueList_1= ruleIQLMetadataValueList | this_IQLMetadataValueMap_2= ruleIQLMetadataValueMap )
            int alt47=3;
            switch ( input.LA(1) ) {
            case RULE_ID:
            case RULE_DOUBLE:
            case RULE_STRING:
            case RULE_INT:
            case 67:
            case 115:
            case 116:
                {
                alt47=1;
                }
                break;
            case 64:
                {
                alt47=2;
                }
                break;
            case 16:
                {
                alt47=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 47, 0, input);

                throw nvae;
            }

            switch (alt47) {
                case 1 :
                    // InternalODL.g:2487:3: this_IQLMetadataValueSingle_0= ruleIQLMetadataValueSingle
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
                    // InternalODL.g:2496:3: this_IQLMetadataValueList_1= ruleIQLMetadataValueList
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
                    // InternalODL.g:2505:3: this_IQLMetadataValueMap_2= ruleIQLMetadataValueMap
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
    // InternalODL.g:2517:1: entryRuleIQLMetadataValueSingle returns [EObject current=null] : iv_ruleIQLMetadataValueSingle= ruleIQLMetadataValueSingle EOF ;
    public final EObject entryRuleIQLMetadataValueSingle() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataValueSingle = null;


        try {
            // InternalODL.g:2517:63: (iv_ruleIQLMetadataValueSingle= ruleIQLMetadataValueSingle EOF )
            // InternalODL.g:2518:2: iv_ruleIQLMetadataValueSingle= ruleIQLMetadataValueSingle EOF
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
    // InternalODL.g:2524:1: ruleIQLMetadataValueSingle returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleJvmTypeReference ) ) ) | ( () otherlv_11= 'null' ) ) ;
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
            // InternalODL.g:2530:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleJvmTypeReference ) ) ) | ( () otherlv_11= 'null' ) ) )
            // InternalODL.g:2531:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleJvmTypeReference ) ) ) | ( () otherlv_11= 'null' ) )
            {
            // InternalODL.g:2531:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleJvmTypeReference ) ) ) | ( () otherlv_11= 'null' ) )
            int alt48=6;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt48=1;
                }
                break;
            case RULE_DOUBLE:
                {
                alt48=2;
                }
                break;
            case RULE_STRING:
                {
                alt48=3;
                }
                break;
            case 115:
            case 116:
                {
                alt48=4;
                }
                break;
            case RULE_ID:
                {
                alt48=5;
                }
                break;
            case 67:
                {
                alt48=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 48, 0, input);

                throw nvae;
            }

            switch (alt48) {
                case 1 :
                    // InternalODL.g:2532:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalODL.g:2532:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalODL.g:2533:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalODL.g:2533:4: ()
                    // InternalODL.g:2534:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleIntAction_0_0(),
                      						current);
                      				
                    }

                    }

                    // InternalODL.g:2540:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalODL.g:2541:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalODL.g:2541:5: (lv_value_1_0= RULE_INT )
                    // InternalODL.g:2542:6: lv_value_1_0= RULE_INT
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
                    // InternalODL.g:2560:3: ( () ( (lv_value_3_0= RULE_DOUBLE ) ) )
                    {
                    // InternalODL.g:2560:3: ( () ( (lv_value_3_0= RULE_DOUBLE ) ) )
                    // InternalODL.g:2561:4: () ( (lv_value_3_0= RULE_DOUBLE ) )
                    {
                    // InternalODL.g:2561:4: ()
                    // InternalODL.g:2562:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleDoubleAction_1_0(),
                      						current);
                      				
                    }

                    }

                    // InternalODL.g:2568:4: ( (lv_value_3_0= RULE_DOUBLE ) )
                    // InternalODL.g:2569:5: (lv_value_3_0= RULE_DOUBLE )
                    {
                    // InternalODL.g:2569:5: (lv_value_3_0= RULE_DOUBLE )
                    // InternalODL.g:2570:6: lv_value_3_0= RULE_DOUBLE
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
                    // InternalODL.g:2588:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalODL.g:2588:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalODL.g:2589:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalODL.g:2589:4: ()
                    // InternalODL.g:2590:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleStringAction_2_0(),
                      						current);
                      				
                    }

                    }

                    // InternalODL.g:2596:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalODL.g:2597:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalODL.g:2597:5: (lv_value_5_0= RULE_STRING )
                    // InternalODL.g:2598:6: lv_value_5_0= RULE_STRING
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
                    // InternalODL.g:2616:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    {
                    // InternalODL.g:2616:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    // InternalODL.g:2617:4: () ( (lv_value_7_0= ruleBOOLEAN ) )
                    {
                    // InternalODL.g:2617:4: ()
                    // InternalODL.g:2618:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleBooleanAction_3_0(),
                      						current);
                      				
                    }

                    }

                    // InternalODL.g:2624:4: ( (lv_value_7_0= ruleBOOLEAN ) )
                    // InternalODL.g:2625:5: (lv_value_7_0= ruleBOOLEAN )
                    {
                    // InternalODL.g:2625:5: (lv_value_7_0= ruleBOOLEAN )
                    // InternalODL.g:2626:6: lv_value_7_0= ruleBOOLEAN
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
                    // InternalODL.g:2645:3: ( () ( (lv_value_9_0= ruleJvmTypeReference ) ) )
                    {
                    // InternalODL.g:2645:3: ( () ( (lv_value_9_0= ruleJvmTypeReference ) ) )
                    // InternalODL.g:2646:4: () ( (lv_value_9_0= ruleJvmTypeReference ) )
                    {
                    // InternalODL.g:2646:4: ()
                    // InternalODL.g:2647:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleTypeRefAction_4_0(),
                      						current);
                      				
                    }

                    }

                    // InternalODL.g:2653:4: ( (lv_value_9_0= ruleJvmTypeReference ) )
                    // InternalODL.g:2654:5: (lv_value_9_0= ruleJvmTypeReference )
                    {
                    // InternalODL.g:2654:5: (lv_value_9_0= ruleJvmTypeReference )
                    // InternalODL.g:2655:6: lv_value_9_0= ruleJvmTypeReference
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
                    // InternalODL.g:2674:3: ( () otherlv_11= 'null' )
                    {
                    // InternalODL.g:2674:3: ( () otherlv_11= 'null' )
                    // InternalODL.g:2675:4: () otherlv_11= 'null'
                    {
                    // InternalODL.g:2675:4: ()
                    // InternalODL.g:2676:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLMetadataValueSingleAccess().getIQLMetadataValueSingleNullAction_5_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_11=(Token)match(input,67,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:2691:1: entryRuleIQLMetadataValueList returns [EObject current=null] : iv_ruleIQLMetadataValueList= ruleIQLMetadataValueList EOF ;
    public final EObject entryRuleIQLMetadataValueList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataValueList = null;


        try {
            // InternalODL.g:2691:61: (iv_ruleIQLMetadataValueList= ruleIQLMetadataValueList EOF )
            // InternalODL.g:2692:2: iv_ruleIQLMetadataValueList= ruleIQLMetadataValueList EOF
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
    // InternalODL.g:2698:1: ruleIQLMetadataValueList returns [EObject current=null] : ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )? otherlv_5= ']' ) ;
    public final EObject ruleIQLMetadataValueList() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalODL.g:2704:2: ( ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )? otherlv_5= ']' ) )
            // InternalODL.g:2705:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )? otherlv_5= ']' )
            {
            // InternalODL.g:2705:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )? otherlv_5= ']' )
            // InternalODL.g:2706:3: () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )? otherlv_5= ']'
            {
            // InternalODL.g:2706:3: ()
            // InternalODL.g:2707:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLMetadataValueListAccess().getIQLMetadataValueListAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,64,FOLLOW_37); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLMetadataValueListAccess().getLeftSquareBracketKeyword_1());
              		
            }
            // InternalODL.g:2717:3: ( ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )* )?
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==RULE_ID||(LA50_0>=RULE_DOUBLE && LA50_0<=RULE_INT)||LA50_0==16||LA50_0==64||LA50_0==67||(LA50_0>=115 && LA50_0<=116)) ) {
                alt50=1;
            }
            switch (alt50) {
                case 1 :
                    // InternalODL.g:2718:4: ( (lv_elements_2_0= ruleIQLMetadataValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )*
                    {
                    // InternalODL.g:2718:4: ( (lv_elements_2_0= ruleIQLMetadataValue ) )
                    // InternalODL.g:2719:5: (lv_elements_2_0= ruleIQLMetadataValue )
                    {
                    // InternalODL.g:2719:5: (lv_elements_2_0= ruleIQLMetadataValue )
                    // InternalODL.g:2720:6: lv_elements_2_0= ruleIQLMetadataValue
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMetadataValueListAccess().getElementsIQLMetadataValueParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_38);
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

                    // InternalODL.g:2737:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) ) )*
                    loop49:
                    do {
                        int alt49=2;
                        int LA49_0 = input.LA(1);

                        if ( (LA49_0==26) ) {
                            alt49=1;
                        }


                        switch (alt49) {
                    	case 1 :
                    	    // InternalODL.g:2738:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValue ) )
                    	    {
                    	    otherlv_3=(Token)match(input,26,FOLLOW_36); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLMetadataValueListAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalODL.g:2742:5: ( (lv_elements_4_0= ruleIQLMetadataValue ) )
                    	    // InternalODL.g:2743:6: (lv_elements_4_0= ruleIQLMetadataValue )
                    	    {
                    	    // InternalODL.g:2743:6: (lv_elements_4_0= ruleIQLMetadataValue )
                    	    // InternalODL.g:2744:7: lv_elements_4_0= ruleIQLMetadataValue
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLMetadataValueListAccess().getElementsIQLMetadataValueParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_38);
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
                    	    break loop49;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,65,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:2771:1: entryRuleIQLMetadataValueMap returns [EObject current=null] : iv_ruleIQLMetadataValueMap= ruleIQLMetadataValueMap EOF ;
    public final EObject entryRuleIQLMetadataValueMap() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataValueMap = null;


        try {
            // InternalODL.g:2771:60: (iv_ruleIQLMetadataValueMap= ruleIQLMetadataValueMap EOF )
            // InternalODL.g:2772:2: iv_ruleIQLMetadataValueMap= ruleIQLMetadataValueMap EOF
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
    // InternalODL.g:2778:1: ruleIQLMetadataValueMap returns [EObject current=null] : ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )? otherlv_5= '}' ) ;
    public final EObject ruleIQLMetadataValueMap() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalODL.g:2784:2: ( ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )? otherlv_5= '}' ) )
            // InternalODL.g:2785:2: ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )? otherlv_5= '}' )
            {
            // InternalODL.g:2785:2: ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )? otherlv_5= '}' )
            // InternalODL.g:2786:3: () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )? otherlv_5= '}'
            {
            // InternalODL.g:2786:3: ()
            // InternalODL.g:2787:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLMetadataValueMapAccess().getIQLMetadataValueMapAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,16,FOLLOW_39); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLMetadataValueMapAccess().getLeftCurlyBracketKeyword_1());
              		
            }
            // InternalODL.g:2797:3: ( ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )* )?
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( (LA52_0==RULE_ID||(LA52_0>=RULE_DOUBLE && LA52_0<=RULE_INT)||LA52_0==16||LA52_0==64||LA52_0==67||(LA52_0>=115 && LA52_0<=116)) ) {
                alt52=1;
            }
            switch (alt52) {
                case 1 :
                    // InternalODL.g:2798:4: ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )*
                    {
                    // InternalODL.g:2798:4: ( (lv_elements_2_0= ruleIQLMetadataValueMapElement ) )
                    // InternalODL.g:2799:5: (lv_elements_2_0= ruleIQLMetadataValueMapElement )
                    {
                    // InternalODL.g:2799:5: (lv_elements_2_0= ruleIQLMetadataValueMapElement )
                    // InternalODL.g:2800:6: lv_elements_2_0= ruleIQLMetadataValueMapElement
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLMetadataValueMapAccess().getElementsIQLMetadataValueMapElementParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_40);
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

                    // InternalODL.g:2817:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) ) )*
                    loop51:
                    do {
                        int alt51=2;
                        int LA51_0 = input.LA(1);

                        if ( (LA51_0==26) ) {
                            alt51=1;
                        }


                        switch (alt51) {
                    	case 1 :
                    	    // InternalODL.g:2818:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) )
                    	    {
                    	    otherlv_3=(Token)match(input,26,FOLLOW_36); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLMetadataValueMapAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalODL.g:2822:5: ( (lv_elements_4_0= ruleIQLMetadataValueMapElement ) )
                    	    // InternalODL.g:2823:6: (lv_elements_4_0= ruleIQLMetadataValueMapElement )
                    	    {
                    	    // InternalODL.g:2823:6: (lv_elements_4_0= ruleIQLMetadataValueMapElement )
                    	    // InternalODL.g:2824:7: lv_elements_4_0= ruleIQLMetadataValueMapElement
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLMetadataValueMapAccess().getElementsIQLMetadataValueMapElementParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_40);
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
                    	    break loop51;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,17,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:2851:1: entryRuleIQLMetadataValueMapElement returns [EObject current=null] : iv_ruleIQLMetadataValueMapElement= ruleIQLMetadataValueMapElement EOF ;
    public final EObject entryRuleIQLMetadataValueMapElement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMetadataValueMapElement = null;


        try {
            // InternalODL.g:2851:67: (iv_ruleIQLMetadataValueMapElement= ruleIQLMetadataValueMapElement EOF )
            // InternalODL.g:2852:2: iv_ruleIQLMetadataValueMapElement= ruleIQLMetadataValueMapElement EOF
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
    // InternalODL.g:2858:1: ruleIQLMetadataValueMapElement returns [EObject current=null] : ( ( (lv_key_0_0= ruleIQLMetadataValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) ) ;
    public final EObject ruleIQLMetadataValueMapElement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_key_0_0 = null;

        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalODL.g:2864:2: ( ( ( (lv_key_0_0= ruleIQLMetadataValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) ) )
            // InternalODL.g:2865:2: ( ( (lv_key_0_0= ruleIQLMetadataValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )
            {
            // InternalODL.g:2865:2: ( ( (lv_key_0_0= ruleIQLMetadataValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) ) )
            // InternalODL.g:2866:3: ( (lv_key_0_0= ruleIQLMetadataValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLMetadataValue ) )
            {
            // InternalODL.g:2866:3: ( (lv_key_0_0= ruleIQLMetadataValue ) )
            // InternalODL.g:2867:4: (lv_key_0_0= ruleIQLMetadataValue )
            {
            // InternalODL.g:2867:4: (lv_key_0_0= ruleIQLMetadataValue )
            // InternalODL.g:2868:5: lv_key_0_0= ruleIQLMetadataValue
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLMetadataValueMapElementAccess().getKeyIQLMetadataValueParserRuleCall_0_0());
              				
            }
            pushFollow(FOLLOW_41);
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

            otherlv_1=(Token)match(input,49,FOLLOW_36); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLMetadataValueMapElementAccess().getEqualsSignKeyword_1());
              		
            }
            // InternalODL.g:2889:3: ( (lv_value_2_0= ruleIQLMetadataValue ) )
            // InternalODL.g:2890:4: (lv_value_2_0= ruleIQLMetadataValue )
            {
            // InternalODL.g:2890:4: (lv_value_2_0= ruleIQLMetadataValue )
            // InternalODL.g:2891:5: lv_value_2_0= ruleIQLMetadataValue
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
    // InternalODL.g:2912:1: entryRuleIQLVariableDeclaration returns [EObject current=null] : iv_ruleIQLVariableDeclaration= ruleIQLVariableDeclaration EOF ;
    public final EObject entryRuleIQLVariableDeclaration() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLVariableDeclaration = null;


        try {
            // InternalODL.g:2912:63: (iv_ruleIQLVariableDeclaration= ruleIQLVariableDeclaration EOF )
            // InternalODL.g:2913:2: iv_ruleIQLVariableDeclaration= ruleIQLVariableDeclaration EOF
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
    // InternalODL.g:2919:1: ruleIQLVariableDeclaration returns [EObject current=null] : ( () ( (lv_ref_1_0= ruleJvmTypeReference ) ) ( (lv_name_2_0= RULE_ID ) ) ) ;
    public final EObject ruleIQLVariableDeclaration() throws RecognitionException {
        EObject current = null;

        Token lv_name_2_0=null;
        EObject lv_ref_1_0 = null;



        	enterRule();

        try {
            // InternalODL.g:2925:2: ( ( () ( (lv_ref_1_0= ruleJvmTypeReference ) ) ( (lv_name_2_0= RULE_ID ) ) ) )
            // InternalODL.g:2926:2: ( () ( (lv_ref_1_0= ruleJvmTypeReference ) ) ( (lv_name_2_0= RULE_ID ) ) )
            {
            // InternalODL.g:2926:2: ( () ( (lv_ref_1_0= ruleJvmTypeReference ) ) ( (lv_name_2_0= RULE_ID ) ) )
            // InternalODL.g:2927:3: () ( (lv_ref_1_0= ruleJvmTypeReference ) ) ( (lv_name_2_0= RULE_ID ) )
            {
            // InternalODL.g:2927:3: ()
            // InternalODL.g:2928:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLVariableDeclarationAccess().getIQLVariableDeclarationAction_0(),
              					current);
              			
            }

            }

            // InternalODL.g:2934:3: ( (lv_ref_1_0= ruleJvmTypeReference ) )
            // InternalODL.g:2935:4: (lv_ref_1_0= ruleJvmTypeReference )
            {
            // InternalODL.g:2935:4: (lv_ref_1_0= ruleJvmTypeReference )
            // InternalODL.g:2936:5: lv_ref_1_0= ruleJvmTypeReference
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

            // InternalODL.g:2953:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalODL.g:2954:4: (lv_name_2_0= RULE_ID )
            {
            // InternalODL.g:2954:4: (lv_name_2_0= RULE_ID )
            // InternalODL.g:2955:5: lv_name_2_0= RULE_ID
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
    // InternalODL.g:2975:1: entryRuleIQLVariableInitialization returns [EObject current=null] : iv_ruleIQLVariableInitialization= ruleIQLVariableInitialization EOF ;
    public final EObject entryRuleIQLVariableInitialization() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLVariableInitialization = null;


        try {
            // InternalODL.g:2975:66: (iv_ruleIQLVariableInitialization= ruleIQLVariableInitialization EOF )
            // InternalODL.g:2976:2: iv_ruleIQLVariableInitialization= ruleIQLVariableInitialization EOF
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
    // InternalODL.g:2982:1: ruleIQLVariableInitialization returns [EObject current=null] : ( ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) ) | ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) ) | (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) ) ) ;
    public final EObject ruleIQLVariableInitialization() throws RecognitionException {
        EObject current = null;

        Token otherlv_4=null;
        EObject lv_argsList_1_0 = null;

        EObject lv_argsMap_2_0 = null;

        EObject lv_argsMap_3_0 = null;

        EObject lv_value_5_0 = null;



        	enterRule();

        try {
            // InternalODL.g:2988:2: ( ( ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) ) | ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) ) | (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) ) ) )
            // InternalODL.g:2989:2: ( ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) ) | ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) ) | (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) ) )
            {
            // InternalODL.g:2989:2: ( ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) ) | ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) ) | (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) ) )
            int alt54=3;
            switch ( input.LA(1) ) {
            case 14:
                {
                alt54=1;
                }
                break;
            case 16:
                {
                alt54=2;
                }
                break;
            case 49:
                {
                alt54=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 54, 0, input);

                throw nvae;
            }

            switch (alt54) {
                case 1 :
                    // InternalODL.g:2990:3: ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) )
                    {
                    // InternalODL.g:2990:3: ( () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? ) )
                    // InternalODL.g:2991:4: () ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? )
                    {
                    // InternalODL.g:2991:4: ()
                    // InternalODL.g:2992:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLVariableInitializationAccess().getIQLVariableInitializationAction_0_0(),
                      						current);
                      				
                    }

                    }

                    // InternalODL.g:2998:4: ( ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )? )
                    // InternalODL.g:2999:5: ( (lv_argsList_1_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )?
                    {
                    // InternalODL.g:2999:5: ( (lv_argsList_1_0= ruleIQLArgumentsList ) )
                    // InternalODL.g:3000:6: (lv_argsList_1_0= ruleIQLArgumentsList )
                    {
                    // InternalODL.g:3000:6: (lv_argsList_1_0= ruleIQLArgumentsList )
                    // InternalODL.g:3001:7: lv_argsList_1_0= ruleIQLArgumentsList
                    {
                    if ( state.backtracking==0 ) {

                      							newCompositeNode(grammarAccess.getIQLVariableInitializationAccess().getArgsListIQLArgumentsListParserRuleCall_0_1_0_0());
                      						
                    }
                    pushFollow(FOLLOW_42);
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

                    // InternalODL.g:3018:5: ( (lv_argsMap_2_0= ruleIQLArgumentsMap ) )?
                    int alt53=2;
                    int LA53_0 = input.LA(1);

                    if ( (LA53_0==16) ) {
                        alt53=1;
                    }
                    switch (alt53) {
                        case 1 :
                            // InternalODL.g:3019:6: (lv_argsMap_2_0= ruleIQLArgumentsMap )
                            {
                            // InternalODL.g:3019:6: (lv_argsMap_2_0= ruleIQLArgumentsMap )
                            // InternalODL.g:3020:7: lv_argsMap_2_0= ruleIQLArgumentsMap
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
                    // InternalODL.g:3040:3: ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) )
                    {
                    // InternalODL.g:3040:3: ( (lv_argsMap_3_0= ruleIQLArgumentsMap ) )
                    // InternalODL.g:3041:4: (lv_argsMap_3_0= ruleIQLArgumentsMap )
                    {
                    // InternalODL.g:3041:4: (lv_argsMap_3_0= ruleIQLArgumentsMap )
                    // InternalODL.g:3042:5: lv_argsMap_3_0= ruleIQLArgumentsMap
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
                    // InternalODL.g:3060:3: (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) )
                    {
                    // InternalODL.g:3060:3: (otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) )
                    // InternalODL.g:3061:4: otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) )
                    {
                    otherlv_4=(Token)match(input,49,FOLLOW_43); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_4, grammarAccess.getIQLVariableInitializationAccess().getEqualsSignKeyword_2_0());
                      			
                    }
                    // InternalODL.g:3065:4: ( (lv_value_5_0= ruleIQLExpression ) )
                    // InternalODL.g:3066:5: (lv_value_5_0= ruleIQLExpression )
                    {
                    // InternalODL.g:3066:5: (lv_value_5_0= ruleIQLExpression )
                    // InternalODL.g:3067:6: lv_value_5_0= ruleIQLExpression
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
    // InternalODL.g:3089:1: entryRuleIQLArgumentsList returns [EObject current=null] : iv_ruleIQLArgumentsList= ruleIQLArgumentsList EOF ;
    public final EObject entryRuleIQLArgumentsList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLArgumentsList = null;


        try {
            // InternalODL.g:3089:57: (iv_ruleIQLArgumentsList= ruleIQLArgumentsList EOF )
            // InternalODL.g:3090:2: iv_ruleIQLArgumentsList= ruleIQLArgumentsList EOF
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
    // InternalODL.g:3096:1: ruleIQLArgumentsList returns [EObject current=null] : ( () otherlv_1= '(' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ')' ) ;
    public final EObject ruleIQLArgumentsList() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalODL.g:3102:2: ( ( () otherlv_1= '(' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ')' ) )
            // InternalODL.g:3103:2: ( () otherlv_1= '(' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ')' )
            {
            // InternalODL.g:3103:2: ( () otherlv_1= '(' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ')' )
            // InternalODL.g:3104:3: () otherlv_1= '(' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ')'
            {
            // InternalODL.g:3104:3: ()
            // InternalODL.g:3105:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLArgumentsListAccess().getIQLArgumentsListAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,14,FOLLOW_44); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLArgumentsListAccess().getLeftParenthesisKeyword_1());
              		
            }
            // InternalODL.g:3115:3: ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )?
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==RULE_ID||(LA56_0>=RULE_DOUBLE && LA56_0<=RULE_INT)||LA56_0==RULE_RANGE||LA56_0==14||LA56_0==28||LA56_0==30||(LA56_0>=38 && LA56_0<=39)||LA56_0==44||LA56_0==64||LA56_0==67||(LA56_0>=82 && LA56_0<=83)||(LA56_0>=88 && LA56_0<=89)||(LA56_0>=115 && LA56_0<=116)) ) {
                alt56=1;
            }
            switch (alt56) {
                case 1 :
                    // InternalODL.g:3116:4: ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )*
                    {
                    // InternalODL.g:3116:4: ( (lv_elements_2_0= ruleIQLExpression ) )
                    // InternalODL.g:3117:5: (lv_elements_2_0= ruleIQLExpression )
                    {
                    // InternalODL.g:3117:5: (lv_elements_2_0= ruleIQLExpression )
                    // InternalODL.g:3118:6: lv_elements_2_0= ruleIQLExpression
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLArgumentsListAccess().getElementsIQLExpressionParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_20);
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

                    // InternalODL.g:3135:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )*
                    loop55:
                    do {
                        int alt55=2;
                        int LA55_0 = input.LA(1);

                        if ( (LA55_0==26) ) {
                            alt55=1;
                        }


                        switch (alt55) {
                    	case 1 :
                    	    // InternalODL.g:3136:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) )
                    	    {
                    	    otherlv_3=(Token)match(input,26,FOLLOW_43); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLArgumentsListAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalODL.g:3140:5: ( (lv_elements_4_0= ruleIQLExpression ) )
                    	    // InternalODL.g:3141:6: (lv_elements_4_0= ruleIQLExpression )
                    	    {
                    	    // InternalODL.g:3141:6: (lv_elements_4_0= ruleIQLExpression )
                    	    // InternalODL.g:3142:7: lv_elements_4_0= ruleIQLExpression
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLArgumentsListAccess().getElementsIQLExpressionParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_20);
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
                    	    break loop55;
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
    // InternalODL.g:3169:1: entryRuleIQLArgumentsMap returns [EObject current=null] : iv_ruleIQLArgumentsMap= ruleIQLArgumentsMap EOF ;
    public final EObject entryRuleIQLArgumentsMap() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLArgumentsMap = null;


        try {
            // InternalODL.g:3169:56: (iv_ruleIQLArgumentsMap= ruleIQLArgumentsMap EOF )
            // InternalODL.g:3170:2: iv_ruleIQLArgumentsMap= ruleIQLArgumentsMap EOF
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
    // InternalODL.g:3176:1: ruleIQLArgumentsMap returns [EObject current=null] : ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )? otherlv_5= '}' ) ;
    public final EObject ruleIQLArgumentsMap() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalODL.g:3182:2: ( ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )? otherlv_5= '}' ) )
            // InternalODL.g:3183:2: ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )? otherlv_5= '}' )
            {
            // InternalODL.g:3183:2: ( () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )? otherlv_5= '}' )
            // InternalODL.g:3184:3: () otherlv_1= '{' ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )? otherlv_5= '}'
            {
            // InternalODL.g:3184:3: ()
            // InternalODL.g:3185:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLArgumentsMapAccess().getIQLArgumentsMapAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,16,FOLLOW_45); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLArgumentsMapAccess().getLeftCurlyBracketKeyword_1());
              		
            }
            // InternalODL.g:3195:3: ( ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )* )?
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==RULE_ID) ) {
                alt58=1;
            }
            switch (alt58) {
                case 1 :
                    // InternalODL.g:3196:4: ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )*
                    {
                    // InternalODL.g:3196:4: ( (lv_elements_2_0= ruleIQLArgumentsMapKeyValue ) )
                    // InternalODL.g:3197:5: (lv_elements_2_0= ruleIQLArgumentsMapKeyValue )
                    {
                    // InternalODL.g:3197:5: (lv_elements_2_0= ruleIQLArgumentsMapKeyValue )
                    // InternalODL.g:3198:6: lv_elements_2_0= ruleIQLArgumentsMapKeyValue
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLArgumentsMapAccess().getElementsIQLArgumentsMapKeyValueParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_40);
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

                    // InternalODL.g:3215:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) ) )*
                    loop57:
                    do {
                        int alt57=2;
                        int LA57_0 = input.LA(1);

                        if ( (LA57_0==26) ) {
                            alt57=1;
                        }


                        switch (alt57) {
                    	case 1 :
                    	    // InternalODL.g:3216:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) )
                    	    {
                    	    otherlv_3=(Token)match(input,26,FOLLOW_6); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLArgumentsMapAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalODL.g:3220:5: ( (lv_elements_4_0= ruleIQLArgumentsMapKeyValue ) )
                    	    // InternalODL.g:3221:6: (lv_elements_4_0= ruleIQLArgumentsMapKeyValue )
                    	    {
                    	    // InternalODL.g:3221:6: (lv_elements_4_0= ruleIQLArgumentsMapKeyValue )
                    	    // InternalODL.g:3222:7: lv_elements_4_0= ruleIQLArgumentsMapKeyValue
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLArgumentsMapAccess().getElementsIQLArgumentsMapKeyValueParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_40);
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
                    	    break loop57;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,17,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:3249:1: entryRuleIQLArgumentsMapKeyValue returns [EObject current=null] : iv_ruleIQLArgumentsMapKeyValue= ruleIQLArgumentsMapKeyValue EOF ;
    public final EObject entryRuleIQLArgumentsMapKeyValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLArgumentsMapKeyValue = null;


        try {
            // InternalODL.g:3249:64: (iv_ruleIQLArgumentsMapKeyValue= ruleIQLArgumentsMapKeyValue EOF )
            // InternalODL.g:3250:2: iv_ruleIQLArgumentsMapKeyValue= ruleIQLArgumentsMapKeyValue EOF
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
    // InternalODL.g:3256:1: ruleIQLArgumentsMapKeyValue returns [EObject current=null] : ( ( ( ruleQualifiedName ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLExpression ) ) ) ;
    public final EObject ruleIQLArgumentsMapKeyValue() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalODL.g:3262:2: ( ( ( ( ruleQualifiedName ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLExpression ) ) ) )
            // InternalODL.g:3263:2: ( ( ( ruleQualifiedName ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLExpression ) ) )
            {
            // InternalODL.g:3263:2: ( ( ( ruleQualifiedName ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLExpression ) ) )
            // InternalODL.g:3264:3: ( ( ruleQualifiedName ) ) otherlv_1= '=' ( (lv_value_2_0= ruleIQLExpression ) )
            {
            // InternalODL.g:3264:3: ( ( ruleQualifiedName ) )
            // InternalODL.g:3265:4: ( ruleQualifiedName )
            {
            // InternalODL.g:3265:4: ( ruleQualifiedName )
            // InternalODL.g:3266:5: ruleQualifiedName
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLArgumentsMapKeyValueRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLArgumentsMapKeyValueAccess().getKeyJvmIdentifiableElementCrossReference_0_0());
              				
            }
            pushFollow(FOLLOW_41);
            ruleQualifiedName();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_1=(Token)match(input,49,FOLLOW_43); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLArgumentsMapKeyValueAccess().getEqualsSignKeyword_1());
              		
            }
            // InternalODL.g:3284:3: ( (lv_value_2_0= ruleIQLExpression ) )
            // InternalODL.g:3285:4: (lv_value_2_0= ruleIQLExpression )
            {
            // InternalODL.g:3285:4: (lv_value_2_0= ruleIQLExpression )
            // InternalODL.g:3286:5: lv_value_2_0= ruleIQLExpression
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
    // InternalODL.g:3307:1: entryRuleIQLStatement returns [EObject current=null] : iv_ruleIQLStatement= ruleIQLStatement EOF ;
    public final EObject entryRuleIQLStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLStatement = null;


        try {
            // InternalODL.g:3307:53: (iv_ruleIQLStatement= ruleIQLStatement EOF )
            // InternalODL.g:3308:2: iv_ruleIQLStatement= ruleIQLStatement EOF
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
    // InternalODL.g:3314:1: ruleIQLStatement returns [EObject current=null] : (this_IQLStatementBlock_0= ruleIQLStatementBlock | this_IQLExpressionStatement_1= ruleIQLExpressionStatement | this_IQLIfStatement_2= ruleIQLIfStatement | this_IQLWhileStatement_3= ruleIQLWhileStatement | this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement | this_IQLForStatement_5= ruleIQLForStatement | this_IQLForEachStatement_6= ruleIQLForEachStatement | this_IQLSwitchStatement_7= ruleIQLSwitchStatement | this_IQLVariableStatement_8= ruleIQLVariableStatement | this_IQLBreakStatement_9= ruleIQLBreakStatement | this_IQLContinueStatement_10= ruleIQLContinueStatement | this_IQLReturnStatement_11= ruleIQLReturnStatement | this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement | this_IQLJavaStatement_13= ruleIQLJavaStatement ) ;
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
            // InternalODL.g:3320:2: ( (this_IQLStatementBlock_0= ruleIQLStatementBlock | this_IQLExpressionStatement_1= ruleIQLExpressionStatement | this_IQLIfStatement_2= ruleIQLIfStatement | this_IQLWhileStatement_3= ruleIQLWhileStatement | this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement | this_IQLForStatement_5= ruleIQLForStatement | this_IQLForEachStatement_6= ruleIQLForEachStatement | this_IQLSwitchStatement_7= ruleIQLSwitchStatement | this_IQLVariableStatement_8= ruleIQLVariableStatement | this_IQLBreakStatement_9= ruleIQLBreakStatement | this_IQLContinueStatement_10= ruleIQLContinueStatement | this_IQLReturnStatement_11= ruleIQLReturnStatement | this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement | this_IQLJavaStatement_13= ruleIQLJavaStatement ) )
            // InternalODL.g:3321:2: (this_IQLStatementBlock_0= ruleIQLStatementBlock | this_IQLExpressionStatement_1= ruleIQLExpressionStatement | this_IQLIfStatement_2= ruleIQLIfStatement | this_IQLWhileStatement_3= ruleIQLWhileStatement | this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement | this_IQLForStatement_5= ruleIQLForStatement | this_IQLForEachStatement_6= ruleIQLForEachStatement | this_IQLSwitchStatement_7= ruleIQLSwitchStatement | this_IQLVariableStatement_8= ruleIQLVariableStatement | this_IQLBreakStatement_9= ruleIQLBreakStatement | this_IQLContinueStatement_10= ruleIQLContinueStatement | this_IQLReturnStatement_11= ruleIQLReturnStatement | this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement | this_IQLJavaStatement_13= ruleIQLJavaStatement )
            {
            // InternalODL.g:3321:2: (this_IQLStatementBlock_0= ruleIQLStatementBlock | this_IQLExpressionStatement_1= ruleIQLExpressionStatement | this_IQLIfStatement_2= ruleIQLIfStatement | this_IQLWhileStatement_3= ruleIQLWhileStatement | this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement | this_IQLForStatement_5= ruleIQLForStatement | this_IQLForEachStatement_6= ruleIQLForEachStatement | this_IQLSwitchStatement_7= ruleIQLSwitchStatement | this_IQLVariableStatement_8= ruleIQLVariableStatement | this_IQLBreakStatement_9= ruleIQLBreakStatement | this_IQLContinueStatement_10= ruleIQLContinueStatement | this_IQLReturnStatement_11= ruleIQLReturnStatement | this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement | this_IQLJavaStatement_13= ruleIQLJavaStatement )
            int alt59=14;
            alt59 = dfa59.predict(input);
            switch (alt59) {
                case 1 :
                    // InternalODL.g:3322:3: this_IQLStatementBlock_0= ruleIQLStatementBlock
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
                    // InternalODL.g:3331:3: this_IQLExpressionStatement_1= ruleIQLExpressionStatement
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
                    // InternalODL.g:3340:3: this_IQLIfStatement_2= ruleIQLIfStatement
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
                    // InternalODL.g:3349:3: this_IQLWhileStatement_3= ruleIQLWhileStatement
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
                    // InternalODL.g:3358:3: this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement
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
                    // InternalODL.g:3367:3: this_IQLForStatement_5= ruleIQLForStatement
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
                    // InternalODL.g:3376:3: this_IQLForEachStatement_6= ruleIQLForEachStatement
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
                    // InternalODL.g:3385:3: this_IQLSwitchStatement_7= ruleIQLSwitchStatement
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
                    // InternalODL.g:3394:3: this_IQLVariableStatement_8= ruleIQLVariableStatement
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
                    // InternalODL.g:3403:3: this_IQLBreakStatement_9= ruleIQLBreakStatement
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
                    // InternalODL.g:3412:3: this_IQLContinueStatement_10= ruleIQLContinueStatement
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
                    // InternalODL.g:3421:3: this_IQLReturnStatement_11= ruleIQLReturnStatement
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
                    // InternalODL.g:3430:3: this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement
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
                    // InternalODL.g:3439:3: this_IQLJavaStatement_13= ruleIQLJavaStatement
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
    // InternalODL.g:3451:1: entryRuleIQLStatementBlock returns [EObject current=null] : iv_ruleIQLStatementBlock= ruleIQLStatementBlock EOF ;
    public final EObject entryRuleIQLStatementBlock() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLStatementBlock = null;


        try {
            // InternalODL.g:3451:58: (iv_ruleIQLStatementBlock= ruleIQLStatementBlock EOF )
            // InternalODL.g:3452:2: iv_ruleIQLStatementBlock= ruleIQLStatementBlock EOF
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
    // InternalODL.g:3458:1: ruleIQLStatementBlock returns [EObject current=null] : ( () otherlv_1= '{' ( (lv_statements_2_0= ruleIQLStatement ) )* otherlv_3= '}' ) ;
    public final EObject ruleIQLStatementBlock() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject lv_statements_2_0 = null;



        	enterRule();

        try {
            // InternalODL.g:3464:2: ( ( () otherlv_1= '{' ( (lv_statements_2_0= ruleIQLStatement ) )* otherlv_3= '}' ) )
            // InternalODL.g:3465:2: ( () otherlv_1= '{' ( (lv_statements_2_0= ruleIQLStatement ) )* otherlv_3= '}' )
            {
            // InternalODL.g:3465:2: ( () otherlv_1= '{' ( (lv_statements_2_0= ruleIQLStatement ) )* otherlv_3= '}' )
            // InternalODL.g:3466:3: () otherlv_1= '{' ( (lv_statements_2_0= ruleIQLStatement ) )* otherlv_3= '}'
            {
            // InternalODL.g:3466:3: ()
            // InternalODL.g:3467:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLStatementBlockAccess().getIQLStatementBlockAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,16,FOLLOW_46); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLStatementBlockAccess().getLeftCurlyBracketKeyword_1());
              		
            }
            // InternalODL.g:3477:3: ( (lv_statements_2_0= ruleIQLStatement ) )*
            loop60:
            do {
                int alt60=2;
                int LA60_0 = input.LA(1);

                if ( (LA60_0==RULE_ID||(LA60_0>=RULE_DOUBLE && LA60_0<=RULE_INT)||LA60_0==RULE_RANGE||LA60_0==14||LA60_0==16||LA60_0==28||LA60_0==30||(LA60_0>=38 && LA60_0<=39)||LA60_0==44||LA60_0==64||LA60_0==67||LA60_0==74||(LA60_0>=76 && LA60_0<=79)||(LA60_0>=82 && LA60_0<=86)||(LA60_0>=88 && LA60_0<=89)||LA60_0==92||(LA60_0>=115 && LA60_0<=116)) ) {
                    alt60=1;
                }


                switch (alt60) {
            	case 1 :
            	    // InternalODL.g:3478:4: (lv_statements_2_0= ruleIQLStatement )
            	    {
            	    // InternalODL.g:3478:4: (lv_statements_2_0= ruleIQLStatement )
            	    // InternalODL.g:3479:5: lv_statements_2_0= ruleIQLStatement
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLStatementBlockAccess().getStatementsIQLStatementParserRuleCall_2_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_46);
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
            	    break loop60;
                }
            } while (true);

            otherlv_3=(Token)match(input,17,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:3504:1: entryRuleIQLJavaStatement returns [EObject current=null] : iv_ruleIQLJavaStatement= ruleIQLJavaStatement EOF ;
    public final EObject entryRuleIQLJavaStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLJavaStatement = null;


        try {
            // InternalODL.g:3504:57: (iv_ruleIQLJavaStatement= ruleIQLJavaStatement EOF )
            // InternalODL.g:3505:2: iv_ruleIQLJavaStatement= ruleIQLJavaStatement EOF
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
    // InternalODL.g:3511:1: ruleIQLJavaStatement returns [EObject current=null] : ( () ( (lv_java_1_0= ruleIQLJava ) ) ) ;
    public final EObject ruleIQLJavaStatement() throws RecognitionException {
        EObject current = null;

        EObject lv_java_1_0 = null;



        	enterRule();

        try {
            // InternalODL.g:3517:2: ( ( () ( (lv_java_1_0= ruleIQLJava ) ) ) )
            // InternalODL.g:3518:2: ( () ( (lv_java_1_0= ruleIQLJava ) ) )
            {
            // InternalODL.g:3518:2: ( () ( (lv_java_1_0= ruleIQLJava ) ) )
            // InternalODL.g:3519:3: () ( (lv_java_1_0= ruleIQLJava ) )
            {
            // InternalODL.g:3519:3: ()
            // InternalODL.g:3520:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLJavaStatementAccess().getIQLJavaStatementAction_0(),
              					current);
              			
            }

            }

            // InternalODL.g:3526:3: ( (lv_java_1_0= ruleIQLJava ) )
            // InternalODL.g:3527:4: (lv_java_1_0= ruleIQLJava )
            {
            // InternalODL.g:3527:4: (lv_java_1_0= ruleIQLJava )
            // InternalODL.g:3528:5: lv_java_1_0= ruleIQLJava
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
    // InternalODL.g:3549:1: entryRuleIQLIfStatement returns [EObject current=null] : iv_ruleIQLIfStatement= ruleIQLIfStatement EOF ;
    public final EObject entryRuleIQLIfStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLIfStatement = null;


        try {
            // InternalODL.g:3549:55: (iv_ruleIQLIfStatement= ruleIQLIfStatement EOF )
            // InternalODL.g:3550:2: iv_ruleIQLIfStatement= ruleIQLIfStatement EOF
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
    // InternalODL.g:3556:1: ruleIQLIfStatement returns [EObject current=null] : ( () otherlv_1= 'if' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_thenBody_5_0= ruleIQLStatement ) ) ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )? ) ;
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
            // InternalODL.g:3562:2: ( ( () otherlv_1= 'if' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_thenBody_5_0= ruleIQLStatement ) ) ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )? ) )
            // InternalODL.g:3563:2: ( () otherlv_1= 'if' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_thenBody_5_0= ruleIQLStatement ) ) ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )? )
            {
            // InternalODL.g:3563:2: ( () otherlv_1= 'if' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_thenBody_5_0= ruleIQLStatement ) ) ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )? )
            // InternalODL.g:3564:3: () otherlv_1= 'if' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_thenBody_5_0= ruleIQLStatement ) ) ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )?
            {
            // InternalODL.g:3564:3: ()
            // InternalODL.g:3565:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLIfStatementAccess().getIQLIfStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,74,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLIfStatementAccess().getIfKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,14,FOLLOW_43); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLIfStatementAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalODL.g:3579:3: ( (lv_predicate_3_0= ruleIQLExpression ) )
            // InternalODL.g:3580:4: (lv_predicate_3_0= ruleIQLExpression )
            {
            // InternalODL.g:3580:4: (lv_predicate_3_0= ruleIQLExpression )
            // InternalODL.g:3581:5: lv_predicate_3_0= ruleIQLExpression
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

            otherlv_4=(Token)match(input,15,FOLLOW_47); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLIfStatementAccess().getRightParenthesisKeyword_4());
              		
            }
            // InternalODL.g:3602:3: ( (lv_thenBody_5_0= ruleIQLStatement ) )
            // InternalODL.g:3603:4: (lv_thenBody_5_0= ruleIQLStatement )
            {
            // InternalODL.g:3603:4: (lv_thenBody_5_0= ruleIQLStatement )
            // InternalODL.g:3604:5: lv_thenBody_5_0= ruleIQLStatement
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLIfStatementAccess().getThenBodyIQLStatementParserRuleCall_5_0());
              				
            }
            pushFollow(FOLLOW_48);
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

            // InternalODL.g:3621:3: ( ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) ) )?
            int alt61=2;
            int LA61_0 = input.LA(1);

            if ( (LA61_0==75) ) {
                int LA61_1 = input.LA(2);

                if ( (synpred1_InternalODL()) ) {
                    alt61=1;
                }
            }
            switch (alt61) {
                case 1 :
                    // InternalODL.g:3622:4: ( ( 'else' )=>otherlv_6= 'else' ) ( (lv_elseBody_7_0= ruleIQLStatement ) )
                    {
                    // InternalODL.g:3622:4: ( ( 'else' )=>otherlv_6= 'else' )
                    // InternalODL.g:3623:5: ( 'else' )=>otherlv_6= 'else'
                    {
                    otherlv_6=(Token)match(input,75,FOLLOW_47); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(otherlv_6, grammarAccess.getIQLIfStatementAccess().getElseKeyword_6_0());
                      				
                    }

                    }

                    // InternalODL.g:3629:4: ( (lv_elseBody_7_0= ruleIQLStatement ) )
                    // InternalODL.g:3630:5: (lv_elseBody_7_0= ruleIQLStatement )
                    {
                    // InternalODL.g:3630:5: (lv_elseBody_7_0= ruleIQLStatement )
                    // InternalODL.g:3631:6: lv_elseBody_7_0= ruleIQLStatement
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
    // InternalODL.g:3653:1: entryRuleIQLWhileStatement returns [EObject current=null] : iv_ruleIQLWhileStatement= ruleIQLWhileStatement EOF ;
    public final EObject entryRuleIQLWhileStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLWhileStatement = null;


        try {
            // InternalODL.g:3653:58: (iv_ruleIQLWhileStatement= ruleIQLWhileStatement EOF )
            // InternalODL.g:3654:2: iv_ruleIQLWhileStatement= ruleIQLWhileStatement EOF
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
    // InternalODL.g:3660:1: ruleIQLWhileStatement returns [EObject current=null] : ( () otherlv_1= 'while' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_body_5_0= ruleIQLStatement ) ) ) ;
    public final EObject ruleIQLWhileStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_predicate_3_0 = null;

        EObject lv_body_5_0 = null;



        	enterRule();

        try {
            // InternalODL.g:3666:2: ( ( () otherlv_1= 'while' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_body_5_0= ruleIQLStatement ) ) ) )
            // InternalODL.g:3667:2: ( () otherlv_1= 'while' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_body_5_0= ruleIQLStatement ) ) )
            {
            // InternalODL.g:3667:2: ( () otherlv_1= 'while' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_body_5_0= ruleIQLStatement ) ) )
            // InternalODL.g:3668:3: () otherlv_1= 'while' otherlv_2= '(' ( (lv_predicate_3_0= ruleIQLExpression ) ) otherlv_4= ')' ( (lv_body_5_0= ruleIQLStatement ) )
            {
            // InternalODL.g:3668:3: ()
            // InternalODL.g:3669:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLWhileStatementAccess().getIQLWhileStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,76,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLWhileStatementAccess().getWhileKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,14,FOLLOW_43); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLWhileStatementAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalODL.g:3683:3: ( (lv_predicate_3_0= ruleIQLExpression ) )
            // InternalODL.g:3684:4: (lv_predicate_3_0= ruleIQLExpression )
            {
            // InternalODL.g:3684:4: (lv_predicate_3_0= ruleIQLExpression )
            // InternalODL.g:3685:5: lv_predicate_3_0= ruleIQLExpression
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

            otherlv_4=(Token)match(input,15,FOLLOW_47); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLWhileStatementAccess().getRightParenthesisKeyword_4());
              		
            }
            // InternalODL.g:3706:3: ( (lv_body_5_0= ruleIQLStatement ) )
            // InternalODL.g:3707:4: (lv_body_5_0= ruleIQLStatement )
            {
            // InternalODL.g:3707:4: (lv_body_5_0= ruleIQLStatement )
            // InternalODL.g:3708:5: lv_body_5_0= ruleIQLStatement
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
    // InternalODL.g:3729:1: entryRuleIQLDoWhileStatement returns [EObject current=null] : iv_ruleIQLDoWhileStatement= ruleIQLDoWhileStatement EOF ;
    public final EObject entryRuleIQLDoWhileStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLDoWhileStatement = null;


        try {
            // InternalODL.g:3729:60: (iv_ruleIQLDoWhileStatement= ruleIQLDoWhileStatement EOF )
            // InternalODL.g:3730:2: iv_ruleIQLDoWhileStatement= ruleIQLDoWhileStatement EOF
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
    // InternalODL.g:3736:1: ruleIQLDoWhileStatement returns [EObject current=null] : ( () otherlv_1= 'do' ( (lv_body_2_0= ruleIQLStatement ) ) otherlv_3= 'while' otherlv_4= '(' ( (lv_predicate_5_0= ruleIQLExpression ) ) otherlv_6= ')' otherlv_7= ';' ) ;
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
            // InternalODL.g:3742:2: ( ( () otherlv_1= 'do' ( (lv_body_2_0= ruleIQLStatement ) ) otherlv_3= 'while' otherlv_4= '(' ( (lv_predicate_5_0= ruleIQLExpression ) ) otherlv_6= ')' otherlv_7= ';' ) )
            // InternalODL.g:3743:2: ( () otherlv_1= 'do' ( (lv_body_2_0= ruleIQLStatement ) ) otherlv_3= 'while' otherlv_4= '(' ( (lv_predicate_5_0= ruleIQLExpression ) ) otherlv_6= ')' otherlv_7= ';' )
            {
            // InternalODL.g:3743:2: ( () otherlv_1= 'do' ( (lv_body_2_0= ruleIQLStatement ) ) otherlv_3= 'while' otherlv_4= '(' ( (lv_predicate_5_0= ruleIQLExpression ) ) otherlv_6= ')' otherlv_7= ';' )
            // InternalODL.g:3744:3: () otherlv_1= 'do' ( (lv_body_2_0= ruleIQLStatement ) ) otherlv_3= 'while' otherlv_4= '(' ( (lv_predicate_5_0= ruleIQLExpression ) ) otherlv_6= ')' otherlv_7= ';'
            {
            // InternalODL.g:3744:3: ()
            // InternalODL.g:3745:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLDoWhileStatementAccess().getIQLDoWhileStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,77,FOLLOW_47); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLDoWhileStatementAccess().getDoKeyword_1());
              		
            }
            // InternalODL.g:3755:3: ( (lv_body_2_0= ruleIQLStatement ) )
            // InternalODL.g:3756:4: (lv_body_2_0= ruleIQLStatement )
            {
            // InternalODL.g:3756:4: (lv_body_2_0= ruleIQLStatement )
            // InternalODL.g:3757:5: lv_body_2_0= ruleIQLStatement
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLDoWhileStatementAccess().getBodyIQLStatementParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_49);
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

            otherlv_3=(Token)match(input,76,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getIQLDoWhileStatementAccess().getWhileKeyword_3());
              		
            }
            otherlv_4=(Token)match(input,14,FOLLOW_43); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLDoWhileStatementAccess().getLeftParenthesisKeyword_4());
              		
            }
            // InternalODL.g:3782:3: ( (lv_predicate_5_0= ruleIQLExpression ) )
            // InternalODL.g:3783:4: (lv_predicate_5_0= ruleIQLExpression )
            {
            // InternalODL.g:3783:4: (lv_predicate_5_0= ruleIQLExpression )
            // InternalODL.g:3784:5: lv_predicate_5_0= ruleIQLExpression
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

            otherlv_6=(Token)match(input,15,FOLLOW_15); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_6, grammarAccess.getIQLDoWhileStatementAccess().getRightParenthesisKeyword_6());
              		
            }
            otherlv_7=(Token)match(input,20,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:3813:1: entryRuleIQLForStatement returns [EObject current=null] : iv_ruleIQLForStatement= ruleIQLForStatement EOF ;
    public final EObject entryRuleIQLForStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLForStatement = null;


        try {
            // InternalODL.g:3813:56: (iv_ruleIQLForStatement= ruleIQLForStatement EOF )
            // InternalODL.g:3814:2: iv_ruleIQLForStatement= ruleIQLForStatement EOF
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
    // InternalODL.g:3820:1: ruleIQLForStatement returns [EObject current=null] : ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) otherlv_6= ';' ( (lv_predicate_7_0= ruleIQLExpression ) ) otherlv_8= ';' ( (lv_updateExpr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ( (lv_body_11_0= ruleIQLStatement ) ) ) ;
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
            // InternalODL.g:3826:2: ( ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) otherlv_6= ';' ( (lv_predicate_7_0= ruleIQLExpression ) ) otherlv_8= ';' ( (lv_updateExpr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ( (lv_body_11_0= ruleIQLStatement ) ) ) )
            // InternalODL.g:3827:2: ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) otherlv_6= ';' ( (lv_predicate_7_0= ruleIQLExpression ) ) otherlv_8= ';' ( (lv_updateExpr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ( (lv_body_11_0= ruleIQLStatement ) ) )
            {
            // InternalODL.g:3827:2: ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) otherlv_6= ';' ( (lv_predicate_7_0= ruleIQLExpression ) ) otherlv_8= ';' ( (lv_updateExpr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ( (lv_body_11_0= ruleIQLStatement ) ) )
            // InternalODL.g:3828:3: () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= '=' ( (lv_value_5_0= ruleIQLExpression ) ) otherlv_6= ';' ( (lv_predicate_7_0= ruleIQLExpression ) ) otherlv_8= ';' ( (lv_updateExpr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ( (lv_body_11_0= ruleIQLStatement ) )
            {
            // InternalODL.g:3828:3: ()
            // InternalODL.g:3829:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLForStatementAccess().getIQLForStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,78,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLForStatementAccess().getForKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,14,FOLLOW_6); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLForStatementAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalODL.g:3843:3: ( (lv_var_3_0= ruleIQLVariableDeclaration ) )
            // InternalODL.g:3844:4: (lv_var_3_0= ruleIQLVariableDeclaration )
            {
            // InternalODL.g:3844:4: (lv_var_3_0= ruleIQLVariableDeclaration )
            // InternalODL.g:3845:5: lv_var_3_0= ruleIQLVariableDeclaration
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForStatementAccess().getVarIQLVariableDeclarationParserRuleCall_3_0());
              				
            }
            pushFollow(FOLLOW_41);
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

            otherlv_4=(Token)match(input,49,FOLLOW_43); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLForStatementAccess().getEqualsSignKeyword_4());
              		
            }
            // InternalODL.g:3866:3: ( (lv_value_5_0= ruleIQLExpression ) )
            // InternalODL.g:3867:4: (lv_value_5_0= ruleIQLExpression )
            {
            // InternalODL.g:3867:4: (lv_value_5_0= ruleIQLExpression )
            // InternalODL.g:3868:5: lv_value_5_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForStatementAccess().getValueIQLExpressionParserRuleCall_5_0());
              				
            }
            pushFollow(FOLLOW_15);
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

            otherlv_6=(Token)match(input,20,FOLLOW_43); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_6, grammarAccess.getIQLForStatementAccess().getSemicolonKeyword_6());
              		
            }
            // InternalODL.g:3889:3: ( (lv_predicate_7_0= ruleIQLExpression ) )
            // InternalODL.g:3890:4: (lv_predicate_7_0= ruleIQLExpression )
            {
            // InternalODL.g:3890:4: (lv_predicate_7_0= ruleIQLExpression )
            // InternalODL.g:3891:5: lv_predicate_7_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForStatementAccess().getPredicateIQLExpressionParserRuleCall_7_0());
              				
            }
            pushFollow(FOLLOW_15);
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

            otherlv_8=(Token)match(input,20,FOLLOW_43); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_8, grammarAccess.getIQLForStatementAccess().getSemicolonKeyword_8());
              		
            }
            // InternalODL.g:3912:3: ( (lv_updateExpr_9_0= ruleIQLExpression ) )
            // InternalODL.g:3913:4: (lv_updateExpr_9_0= ruleIQLExpression )
            {
            // InternalODL.g:3913:4: (lv_updateExpr_9_0= ruleIQLExpression )
            // InternalODL.g:3914:5: lv_updateExpr_9_0= ruleIQLExpression
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

            otherlv_10=(Token)match(input,15,FOLLOW_47); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_10, grammarAccess.getIQLForStatementAccess().getRightParenthesisKeyword_10());
              		
            }
            // InternalODL.g:3935:3: ( (lv_body_11_0= ruleIQLStatement ) )
            // InternalODL.g:3936:4: (lv_body_11_0= ruleIQLStatement )
            {
            // InternalODL.g:3936:4: (lv_body_11_0= ruleIQLStatement )
            // InternalODL.g:3937:5: lv_body_11_0= ruleIQLStatement
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
    // InternalODL.g:3958:1: entryRuleIQLForEachStatement returns [EObject current=null] : iv_ruleIQLForEachStatement= ruleIQLForEachStatement EOF ;
    public final EObject entryRuleIQLForEachStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLForEachStatement = null;


        try {
            // InternalODL.g:3958:60: (iv_ruleIQLForEachStatement= ruleIQLForEachStatement EOF )
            // InternalODL.g:3959:2: iv_ruleIQLForEachStatement= ruleIQLForEachStatement EOF
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
    // InternalODL.g:3965:1: ruleIQLForEachStatement returns [EObject current=null] : ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= ':' ( (lv_forExpression_5_0= ruleIQLExpression ) ) otherlv_6= ')' ( (lv_body_7_0= ruleIQLStatement ) ) ) ;
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
            // InternalODL.g:3971:2: ( ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= ':' ( (lv_forExpression_5_0= ruleIQLExpression ) ) otherlv_6= ')' ( (lv_body_7_0= ruleIQLStatement ) ) ) )
            // InternalODL.g:3972:2: ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= ':' ( (lv_forExpression_5_0= ruleIQLExpression ) ) otherlv_6= ')' ( (lv_body_7_0= ruleIQLStatement ) ) )
            {
            // InternalODL.g:3972:2: ( () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= ':' ( (lv_forExpression_5_0= ruleIQLExpression ) ) otherlv_6= ')' ( (lv_body_7_0= ruleIQLStatement ) ) )
            // InternalODL.g:3973:3: () otherlv_1= 'for' otherlv_2= '(' ( (lv_var_3_0= ruleIQLVariableDeclaration ) ) otherlv_4= ':' ( (lv_forExpression_5_0= ruleIQLExpression ) ) otherlv_6= ')' ( (lv_body_7_0= ruleIQLStatement ) )
            {
            // InternalODL.g:3973:3: ()
            // InternalODL.g:3974:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLForEachStatementAccess().getIQLForEachStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,78,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLForEachStatementAccess().getForKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,14,FOLLOW_6); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLForEachStatementAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalODL.g:3988:3: ( (lv_var_3_0= ruleIQLVariableDeclaration ) )
            // InternalODL.g:3989:4: (lv_var_3_0= ruleIQLVariableDeclaration )
            {
            // InternalODL.g:3989:4: (lv_var_3_0= ruleIQLVariableDeclaration )
            // InternalODL.g:3990:5: lv_var_3_0= ruleIQLVariableDeclaration
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLForEachStatementAccess().getVarIQLVariableDeclarationParserRuleCall_3_0());
              				
            }
            pushFollow(FOLLOW_50);
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

            otherlv_4=(Token)match(input,27,FOLLOW_43); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLForEachStatementAccess().getColonKeyword_4());
              		
            }
            // InternalODL.g:4011:3: ( (lv_forExpression_5_0= ruleIQLExpression ) )
            // InternalODL.g:4012:4: (lv_forExpression_5_0= ruleIQLExpression )
            {
            // InternalODL.g:4012:4: (lv_forExpression_5_0= ruleIQLExpression )
            // InternalODL.g:4013:5: lv_forExpression_5_0= ruleIQLExpression
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

            otherlv_6=(Token)match(input,15,FOLLOW_47); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_6, grammarAccess.getIQLForEachStatementAccess().getRightParenthesisKeyword_6());
              		
            }
            // InternalODL.g:4034:3: ( (lv_body_7_0= ruleIQLStatement ) )
            // InternalODL.g:4035:4: (lv_body_7_0= ruleIQLStatement )
            {
            // InternalODL.g:4035:4: (lv_body_7_0= ruleIQLStatement )
            // InternalODL.g:4036:5: lv_body_7_0= ruleIQLStatement
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
    // InternalODL.g:4057:1: entryRuleIQLSwitchStatement returns [EObject current=null] : iv_ruleIQLSwitchStatement= ruleIQLSwitchStatement EOF ;
    public final EObject entryRuleIQLSwitchStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLSwitchStatement = null;


        try {
            // InternalODL.g:4057:59: (iv_ruleIQLSwitchStatement= ruleIQLSwitchStatement EOF )
            // InternalODL.g:4058:2: iv_ruleIQLSwitchStatement= ruleIQLSwitchStatement EOF
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
    // InternalODL.g:4064:1: ruleIQLSwitchStatement returns [EObject current=null] : ( () otherlv_1= 'switch' otherlv_2= '(' ( (lv_expr_3_0= ruleIQLExpression ) ) otherlv_4= ')' otherlv_5= '{' ( (lv_cases_6_0= ruleIQLCasePart ) )* (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )? otherlv_10= '}' ) ;
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
            // InternalODL.g:4070:2: ( ( () otherlv_1= 'switch' otherlv_2= '(' ( (lv_expr_3_0= ruleIQLExpression ) ) otherlv_4= ')' otherlv_5= '{' ( (lv_cases_6_0= ruleIQLCasePart ) )* (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )? otherlv_10= '}' ) )
            // InternalODL.g:4071:2: ( () otherlv_1= 'switch' otherlv_2= '(' ( (lv_expr_3_0= ruleIQLExpression ) ) otherlv_4= ')' otherlv_5= '{' ( (lv_cases_6_0= ruleIQLCasePart ) )* (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )? otherlv_10= '}' )
            {
            // InternalODL.g:4071:2: ( () otherlv_1= 'switch' otherlv_2= '(' ( (lv_expr_3_0= ruleIQLExpression ) ) otherlv_4= ')' otherlv_5= '{' ( (lv_cases_6_0= ruleIQLCasePart ) )* (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )? otherlv_10= '}' )
            // InternalODL.g:4072:3: () otherlv_1= 'switch' otherlv_2= '(' ( (lv_expr_3_0= ruleIQLExpression ) ) otherlv_4= ')' otherlv_5= '{' ( (lv_cases_6_0= ruleIQLCasePart ) )* (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )? otherlv_10= '}'
            {
            // InternalODL.g:4072:3: ()
            // InternalODL.g:4073:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLSwitchStatementAccess().getIQLSwitchStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,79,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLSwitchStatementAccess().getSwitchKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,14,FOLLOW_43); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getIQLSwitchStatementAccess().getLeftParenthesisKeyword_2());
              		
            }
            // InternalODL.g:4087:3: ( (lv_expr_3_0= ruleIQLExpression ) )
            // InternalODL.g:4088:4: (lv_expr_3_0= ruleIQLExpression )
            {
            // InternalODL.g:4088:4: (lv_expr_3_0= ruleIQLExpression )
            // InternalODL.g:4089:5: lv_expr_3_0= ruleIQLExpression
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

            otherlv_4=(Token)match(input,15,FOLLOW_10); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getIQLSwitchStatementAccess().getRightParenthesisKeyword_4());
              		
            }
            otherlv_5=(Token)match(input,16,FOLLOW_51); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getIQLSwitchStatementAccess().getLeftCurlyBracketKeyword_5());
              		
            }
            // InternalODL.g:4114:3: ( (lv_cases_6_0= ruleIQLCasePart ) )*
            loop62:
            do {
                int alt62=2;
                int LA62_0 = input.LA(1);

                if ( (LA62_0==81) ) {
                    alt62=1;
                }


                switch (alt62) {
            	case 1 :
            	    // InternalODL.g:4115:4: (lv_cases_6_0= ruleIQLCasePart )
            	    {
            	    // InternalODL.g:4115:4: (lv_cases_6_0= ruleIQLCasePart )
            	    // InternalODL.g:4116:5: lv_cases_6_0= ruleIQLCasePart
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLSwitchStatementAccess().getCasesIQLCasePartParserRuleCall_6_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_51);
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
            	    break loop62;
                }
            } while (true);

            // InternalODL.g:4133:3: (otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )* )?
            int alt64=2;
            int LA64_0 = input.LA(1);

            if ( (LA64_0==80) ) {
                alt64=1;
            }
            switch (alt64) {
                case 1 :
                    // InternalODL.g:4134:4: otherlv_7= 'default' otherlv_8= ':' ( (lv_statements_9_0= ruleIQLStatement ) )*
                    {
                    otherlv_7=(Token)match(input,80,FOLLOW_50); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_7, grammarAccess.getIQLSwitchStatementAccess().getDefaultKeyword_7_0());
                      			
                    }
                    otherlv_8=(Token)match(input,27,FOLLOW_46); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_8, grammarAccess.getIQLSwitchStatementAccess().getColonKeyword_7_1());
                      			
                    }
                    // InternalODL.g:4142:4: ( (lv_statements_9_0= ruleIQLStatement ) )*
                    loop63:
                    do {
                        int alt63=2;
                        int LA63_0 = input.LA(1);

                        if ( (LA63_0==RULE_ID||(LA63_0>=RULE_DOUBLE && LA63_0<=RULE_INT)||LA63_0==RULE_RANGE||LA63_0==14||LA63_0==16||LA63_0==28||LA63_0==30||(LA63_0>=38 && LA63_0<=39)||LA63_0==44||LA63_0==64||LA63_0==67||LA63_0==74||(LA63_0>=76 && LA63_0<=79)||(LA63_0>=82 && LA63_0<=86)||(LA63_0>=88 && LA63_0<=89)||LA63_0==92||(LA63_0>=115 && LA63_0<=116)) ) {
                            alt63=1;
                        }


                        switch (alt63) {
                    	case 1 :
                    	    // InternalODL.g:4143:5: (lv_statements_9_0= ruleIQLStatement )
                    	    {
                    	    // InternalODL.g:4143:5: (lv_statements_9_0= ruleIQLStatement )
                    	    // InternalODL.g:4144:6: lv_statements_9_0= ruleIQLStatement
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      						newCompositeNode(grammarAccess.getIQLSwitchStatementAccess().getStatementsIQLStatementParserRuleCall_7_2_0());
                    	      					
                    	    }
                    	    pushFollow(FOLLOW_46);
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
                    	    break loop63;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_10=(Token)match(input,17,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:4170:1: entryRuleIQLCasePart returns [EObject current=null] : iv_ruleIQLCasePart= ruleIQLCasePart EOF ;
    public final EObject entryRuleIQLCasePart() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLCasePart = null;


        try {
            // InternalODL.g:4170:52: (iv_ruleIQLCasePart= ruleIQLCasePart EOF )
            // InternalODL.g:4171:2: iv_ruleIQLCasePart= ruleIQLCasePart EOF
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
    // InternalODL.g:4177:1: ruleIQLCasePart returns [EObject current=null] : ( () otherlv_1= 'case' ( (lv_expr_2_0= ruleIQLLiteralExpression ) ) otherlv_3= ':' ( (lv_statements_4_0= ruleIQLStatement ) )* ) ;
    public final EObject ruleIQLCasePart() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject lv_expr_2_0 = null;

        EObject lv_statements_4_0 = null;



        	enterRule();

        try {
            // InternalODL.g:4183:2: ( ( () otherlv_1= 'case' ( (lv_expr_2_0= ruleIQLLiteralExpression ) ) otherlv_3= ':' ( (lv_statements_4_0= ruleIQLStatement ) )* ) )
            // InternalODL.g:4184:2: ( () otherlv_1= 'case' ( (lv_expr_2_0= ruleIQLLiteralExpression ) ) otherlv_3= ':' ( (lv_statements_4_0= ruleIQLStatement ) )* )
            {
            // InternalODL.g:4184:2: ( () otherlv_1= 'case' ( (lv_expr_2_0= ruleIQLLiteralExpression ) ) otherlv_3= ':' ( (lv_statements_4_0= ruleIQLStatement ) )* )
            // InternalODL.g:4185:3: () otherlv_1= 'case' ( (lv_expr_2_0= ruleIQLLiteralExpression ) ) otherlv_3= ':' ( (lv_statements_4_0= ruleIQLStatement ) )*
            {
            // InternalODL.g:4185:3: ()
            // InternalODL.g:4186:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLCasePartAccess().getIQLCasePartAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,81,FOLLOW_43); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLCasePartAccess().getCaseKeyword_1());
              		
            }
            // InternalODL.g:4196:3: ( (lv_expr_2_0= ruleIQLLiteralExpression ) )
            // InternalODL.g:4197:4: (lv_expr_2_0= ruleIQLLiteralExpression )
            {
            // InternalODL.g:4197:4: (lv_expr_2_0= ruleIQLLiteralExpression )
            // InternalODL.g:4198:5: lv_expr_2_0= ruleIQLLiteralExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLCasePartAccess().getExprIQLLiteralExpressionParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_50);
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

            otherlv_3=(Token)match(input,27,FOLLOW_52); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getIQLCasePartAccess().getColonKeyword_3());
              		
            }
            // InternalODL.g:4219:3: ( (lv_statements_4_0= ruleIQLStatement ) )*
            loop65:
            do {
                int alt65=2;
                int LA65_0 = input.LA(1);

                if ( (LA65_0==RULE_ID||(LA65_0>=RULE_DOUBLE && LA65_0<=RULE_INT)||LA65_0==RULE_RANGE||LA65_0==14||LA65_0==16||LA65_0==28||LA65_0==30||(LA65_0>=38 && LA65_0<=39)||LA65_0==44||LA65_0==64||LA65_0==67||LA65_0==74||(LA65_0>=76 && LA65_0<=79)||(LA65_0>=82 && LA65_0<=86)||(LA65_0>=88 && LA65_0<=89)||LA65_0==92||(LA65_0>=115 && LA65_0<=116)) ) {
                    alt65=1;
                }


                switch (alt65) {
            	case 1 :
            	    // InternalODL.g:4220:4: (lv_statements_4_0= ruleIQLStatement )
            	    {
            	    // InternalODL.g:4220:4: (lv_statements_4_0= ruleIQLStatement )
            	    // InternalODL.g:4221:5: lv_statements_4_0= ruleIQLStatement
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getIQLCasePartAccess().getStatementsIQLStatementParserRuleCall_4_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_52);
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
    // $ANTLR end "ruleIQLCasePart"


    // $ANTLR start "entryRuleIQLExpressionStatement"
    // InternalODL.g:4242:1: entryRuleIQLExpressionStatement returns [EObject current=null] : iv_ruleIQLExpressionStatement= ruleIQLExpressionStatement EOF ;
    public final EObject entryRuleIQLExpressionStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLExpressionStatement = null;


        try {
            // InternalODL.g:4242:63: (iv_ruleIQLExpressionStatement= ruleIQLExpressionStatement EOF )
            // InternalODL.g:4243:2: iv_ruleIQLExpressionStatement= ruleIQLExpressionStatement EOF
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
    // InternalODL.g:4249:1: ruleIQLExpressionStatement returns [EObject current=null] : ( () ( (lv_expression_1_0= ruleIQLExpression ) ) otherlv_2= ';' ) ;
    public final EObject ruleIQLExpressionStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject lv_expression_1_0 = null;



        	enterRule();

        try {
            // InternalODL.g:4255:2: ( ( () ( (lv_expression_1_0= ruleIQLExpression ) ) otherlv_2= ';' ) )
            // InternalODL.g:4256:2: ( () ( (lv_expression_1_0= ruleIQLExpression ) ) otherlv_2= ';' )
            {
            // InternalODL.g:4256:2: ( () ( (lv_expression_1_0= ruleIQLExpression ) ) otherlv_2= ';' )
            // InternalODL.g:4257:3: () ( (lv_expression_1_0= ruleIQLExpression ) ) otherlv_2= ';'
            {
            // InternalODL.g:4257:3: ()
            // InternalODL.g:4258:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLExpressionStatementAccess().getIQLExpressionStatementAction_0(),
              					current);
              			
            }

            }

            // InternalODL.g:4264:3: ( (lv_expression_1_0= ruleIQLExpression ) )
            // InternalODL.g:4265:4: (lv_expression_1_0= ruleIQLExpression )
            {
            // InternalODL.g:4265:4: (lv_expression_1_0= ruleIQLExpression )
            // InternalODL.g:4266:5: lv_expression_1_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLExpressionStatementAccess().getExpressionIQLExpressionParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_15);
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

            otherlv_2=(Token)match(input,20,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:4291:1: entryRuleIQLVariableStatement returns [EObject current=null] : iv_ruleIQLVariableStatement= ruleIQLVariableStatement EOF ;
    public final EObject entryRuleIQLVariableStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLVariableStatement = null;


        try {
            // InternalODL.g:4291:61: (iv_ruleIQLVariableStatement= ruleIQLVariableStatement EOF )
            // InternalODL.g:4292:2: iv_ruleIQLVariableStatement= ruleIQLVariableStatement EOF
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
    // InternalODL.g:4298:1: ruleIQLVariableStatement returns [EObject current=null] : ( () ( (lv_var_1_0= ruleIQLVariableDeclaration ) ) ( (lv_init_2_0= ruleIQLVariableInitialization ) ) otherlv_3= ';' ) ;
    public final EObject ruleIQLVariableStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_3=null;
        EObject lv_var_1_0 = null;

        EObject lv_init_2_0 = null;



        	enterRule();

        try {
            // InternalODL.g:4304:2: ( ( () ( (lv_var_1_0= ruleIQLVariableDeclaration ) ) ( (lv_init_2_0= ruleIQLVariableInitialization ) ) otherlv_3= ';' ) )
            // InternalODL.g:4305:2: ( () ( (lv_var_1_0= ruleIQLVariableDeclaration ) ) ( (lv_init_2_0= ruleIQLVariableInitialization ) ) otherlv_3= ';' )
            {
            // InternalODL.g:4305:2: ( () ( (lv_var_1_0= ruleIQLVariableDeclaration ) ) ( (lv_init_2_0= ruleIQLVariableInitialization ) ) otherlv_3= ';' )
            // InternalODL.g:4306:3: () ( (lv_var_1_0= ruleIQLVariableDeclaration ) ) ( (lv_init_2_0= ruleIQLVariableInitialization ) ) otherlv_3= ';'
            {
            // InternalODL.g:4306:3: ()
            // InternalODL.g:4307:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLVariableStatementAccess().getIQLVariableStatementAction_0(),
              					current);
              			
            }

            }

            // InternalODL.g:4313:3: ( (lv_var_1_0= ruleIQLVariableDeclaration ) )
            // InternalODL.g:4314:4: (lv_var_1_0= ruleIQLVariableDeclaration )
            {
            // InternalODL.g:4314:4: (lv_var_1_0= ruleIQLVariableDeclaration )
            // InternalODL.g:4315:5: lv_var_1_0= ruleIQLVariableDeclaration
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLVariableStatementAccess().getVarIQLVariableDeclarationParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_53);
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

            // InternalODL.g:4332:3: ( (lv_init_2_0= ruleIQLVariableInitialization ) )
            // InternalODL.g:4333:4: (lv_init_2_0= ruleIQLVariableInitialization )
            {
            // InternalODL.g:4333:4: (lv_init_2_0= ruleIQLVariableInitialization )
            // InternalODL.g:4334:5: lv_init_2_0= ruleIQLVariableInitialization
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLVariableStatementAccess().getInitIQLVariableInitializationParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_15);
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

            otherlv_3=(Token)match(input,20,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:4359:1: entryRuleIQLConstructorCallStatement returns [EObject current=null] : iv_ruleIQLConstructorCallStatement= ruleIQLConstructorCallStatement EOF ;
    public final EObject entryRuleIQLConstructorCallStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLConstructorCallStatement = null;


        try {
            // InternalODL.g:4359:68: (iv_ruleIQLConstructorCallStatement= ruleIQLConstructorCallStatement EOF )
            // InternalODL.g:4360:2: iv_ruleIQLConstructorCallStatement= ruleIQLConstructorCallStatement EOF
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
    // InternalODL.g:4366:1: ruleIQLConstructorCallStatement returns [EObject current=null] : ( () ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) ) ( (lv_args_3_0= ruleIQLArgumentsList ) ) otherlv_4= ';' ) ;
    public final EObject ruleIQLConstructorCallStatement() throws RecognitionException {
        EObject current = null;

        Token lv_this_1_0=null;
        Token lv_super_2_0=null;
        Token otherlv_4=null;
        EObject lv_args_3_0 = null;



        	enterRule();

        try {
            // InternalODL.g:4372:2: ( ( () ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) ) ( (lv_args_3_0= ruleIQLArgumentsList ) ) otherlv_4= ';' ) )
            // InternalODL.g:4373:2: ( () ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) ) ( (lv_args_3_0= ruleIQLArgumentsList ) ) otherlv_4= ';' )
            {
            // InternalODL.g:4373:2: ( () ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) ) ( (lv_args_3_0= ruleIQLArgumentsList ) ) otherlv_4= ';' )
            // InternalODL.g:4374:3: () ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) ) ( (lv_args_3_0= ruleIQLArgumentsList ) ) otherlv_4= ';'
            {
            // InternalODL.g:4374:3: ()
            // InternalODL.g:4375:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLConstructorCallStatementAccess().getIQLConstructorCallStatementAction_0(),
              					current);
              			
            }

            }

            // InternalODL.g:4381:3: ( ( (lv_this_1_0= 'this' ) ) | ( (lv_super_2_0= 'super' ) ) )
            int alt66=2;
            int LA66_0 = input.LA(1);

            if ( (LA66_0==82) ) {
                alt66=1;
            }
            else if ( (LA66_0==83) ) {
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
                    // InternalODL.g:4382:4: ( (lv_this_1_0= 'this' ) )
                    {
                    // InternalODL.g:4382:4: ( (lv_this_1_0= 'this' ) )
                    // InternalODL.g:4383:5: (lv_this_1_0= 'this' )
                    {
                    // InternalODL.g:4383:5: (lv_this_1_0= 'this' )
                    // InternalODL.g:4384:6: lv_this_1_0= 'this'
                    {
                    lv_this_1_0=(Token)match(input,82,FOLLOW_32); if (state.failed) return current;
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
                    // InternalODL.g:4397:4: ( (lv_super_2_0= 'super' ) )
                    {
                    // InternalODL.g:4397:4: ( (lv_super_2_0= 'super' ) )
                    // InternalODL.g:4398:5: (lv_super_2_0= 'super' )
                    {
                    // InternalODL.g:4398:5: (lv_super_2_0= 'super' )
                    // InternalODL.g:4399:6: lv_super_2_0= 'super'
                    {
                    lv_super_2_0=(Token)match(input,83,FOLLOW_32); if (state.failed) return current;
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

            // InternalODL.g:4412:3: ( (lv_args_3_0= ruleIQLArgumentsList ) )
            // InternalODL.g:4413:4: (lv_args_3_0= ruleIQLArgumentsList )
            {
            // InternalODL.g:4413:4: (lv_args_3_0= ruleIQLArgumentsList )
            // InternalODL.g:4414:5: lv_args_3_0= ruleIQLArgumentsList
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLConstructorCallStatementAccess().getArgsIQLArgumentsListParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_15);
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

            otherlv_4=(Token)match(input,20,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:4439:1: entryRuleIQLBreakStatement returns [EObject current=null] : iv_ruleIQLBreakStatement= ruleIQLBreakStatement EOF ;
    public final EObject entryRuleIQLBreakStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLBreakStatement = null;


        try {
            // InternalODL.g:4439:58: (iv_ruleIQLBreakStatement= ruleIQLBreakStatement EOF )
            // InternalODL.g:4440:2: iv_ruleIQLBreakStatement= ruleIQLBreakStatement EOF
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
    // InternalODL.g:4446:1: ruleIQLBreakStatement returns [EObject current=null] : ( () otherlv_1= 'break' otherlv_2= ';' ) ;
    public final EObject ruleIQLBreakStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;


        	enterRule();

        try {
            // InternalODL.g:4452:2: ( ( () otherlv_1= 'break' otherlv_2= ';' ) )
            // InternalODL.g:4453:2: ( () otherlv_1= 'break' otherlv_2= ';' )
            {
            // InternalODL.g:4453:2: ( () otherlv_1= 'break' otherlv_2= ';' )
            // InternalODL.g:4454:3: () otherlv_1= 'break' otherlv_2= ';'
            {
            // InternalODL.g:4454:3: ()
            // InternalODL.g:4455:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLBreakStatementAccess().getIQLBreakStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,84,FOLLOW_15); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLBreakStatementAccess().getBreakKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,20,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:4473:1: entryRuleIQLContinueStatement returns [EObject current=null] : iv_ruleIQLContinueStatement= ruleIQLContinueStatement EOF ;
    public final EObject entryRuleIQLContinueStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLContinueStatement = null;


        try {
            // InternalODL.g:4473:61: (iv_ruleIQLContinueStatement= ruleIQLContinueStatement EOF )
            // InternalODL.g:4474:2: iv_ruleIQLContinueStatement= ruleIQLContinueStatement EOF
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
    // InternalODL.g:4480:1: ruleIQLContinueStatement returns [EObject current=null] : ( () otherlv_1= 'continue' otherlv_2= ';' ) ;
    public final EObject ruleIQLContinueStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;


        	enterRule();

        try {
            // InternalODL.g:4486:2: ( ( () otherlv_1= 'continue' otherlv_2= ';' ) )
            // InternalODL.g:4487:2: ( () otherlv_1= 'continue' otherlv_2= ';' )
            {
            // InternalODL.g:4487:2: ( () otherlv_1= 'continue' otherlv_2= ';' )
            // InternalODL.g:4488:3: () otherlv_1= 'continue' otherlv_2= ';'
            {
            // InternalODL.g:4488:3: ()
            // InternalODL.g:4489:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLContinueStatementAccess().getIQLContinueStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,85,FOLLOW_15); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLContinueStatementAccess().getContinueKeyword_1());
              		
            }
            otherlv_2=(Token)match(input,20,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:4507:1: entryRuleIQLReturnStatement returns [EObject current=null] : iv_ruleIQLReturnStatement= ruleIQLReturnStatement EOF ;
    public final EObject entryRuleIQLReturnStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLReturnStatement = null;


        try {
            // InternalODL.g:4507:59: (iv_ruleIQLReturnStatement= ruleIQLReturnStatement EOF )
            // InternalODL.g:4508:2: iv_ruleIQLReturnStatement= ruleIQLReturnStatement EOF
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
    // InternalODL.g:4514:1: ruleIQLReturnStatement returns [EObject current=null] : ( () otherlv_1= 'return' ( (lv_expression_2_0= ruleIQLExpression ) )? otherlv_3= ';' ) ;
    public final EObject ruleIQLReturnStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject lv_expression_2_0 = null;



        	enterRule();

        try {
            // InternalODL.g:4520:2: ( ( () otherlv_1= 'return' ( (lv_expression_2_0= ruleIQLExpression ) )? otherlv_3= ';' ) )
            // InternalODL.g:4521:2: ( () otherlv_1= 'return' ( (lv_expression_2_0= ruleIQLExpression ) )? otherlv_3= ';' )
            {
            // InternalODL.g:4521:2: ( () otherlv_1= 'return' ( (lv_expression_2_0= ruleIQLExpression ) )? otherlv_3= ';' )
            // InternalODL.g:4522:3: () otherlv_1= 'return' ( (lv_expression_2_0= ruleIQLExpression ) )? otherlv_3= ';'
            {
            // InternalODL.g:4522:3: ()
            // InternalODL.g:4523:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLReturnStatementAccess().getIQLReturnStatementAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,86,FOLLOW_54); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLReturnStatementAccess().getReturnKeyword_1());
              		
            }
            // InternalODL.g:4533:3: ( (lv_expression_2_0= ruleIQLExpression ) )?
            int alt67=2;
            int LA67_0 = input.LA(1);

            if ( (LA67_0==RULE_ID||(LA67_0>=RULE_DOUBLE && LA67_0<=RULE_INT)||LA67_0==RULE_RANGE||LA67_0==14||LA67_0==28||LA67_0==30||(LA67_0>=38 && LA67_0<=39)||LA67_0==44||LA67_0==64||LA67_0==67||(LA67_0>=82 && LA67_0<=83)||(LA67_0>=88 && LA67_0<=89)||(LA67_0>=115 && LA67_0<=116)) ) {
                alt67=1;
            }
            switch (alt67) {
                case 1 :
                    // InternalODL.g:4534:4: (lv_expression_2_0= ruleIQLExpression )
                    {
                    // InternalODL.g:4534:4: (lv_expression_2_0= ruleIQLExpression )
                    // InternalODL.g:4535:5: lv_expression_2_0= ruleIQLExpression
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getIQLReturnStatementAccess().getExpressionIQLExpressionParserRuleCall_2_0());
                      				
                    }
                    pushFollow(FOLLOW_15);
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

            otherlv_3=(Token)match(input,20,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:4560:1: entryRuleIQLExpression returns [EObject current=null] : iv_ruleIQLExpression= ruleIQLExpression EOF ;
    public final EObject entryRuleIQLExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLExpression = null;


        try {
            // InternalODL.g:4560:54: (iv_ruleIQLExpression= ruleIQLExpression EOF )
            // InternalODL.g:4561:2: iv_ruleIQLExpression= ruleIQLExpression EOF
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
    // InternalODL.g:4567:1: ruleIQLExpression returns [EObject current=null] : this_IQLAssignmentExpression_0= ruleIQLAssignmentExpression ;
    public final EObject ruleIQLExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLAssignmentExpression_0 = null;



        	enterRule();

        try {
            // InternalODL.g:4573:2: (this_IQLAssignmentExpression_0= ruleIQLAssignmentExpression )
            // InternalODL.g:4574:2: this_IQLAssignmentExpression_0= ruleIQLAssignmentExpression
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
    // InternalODL.g:4585:1: entryRuleIQLAssignmentExpression returns [EObject current=null] : iv_ruleIQLAssignmentExpression= ruleIQLAssignmentExpression EOF ;
    public final EObject entryRuleIQLAssignmentExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLAssignmentExpression = null;


        try {
            // InternalODL.g:4585:64: (iv_ruleIQLAssignmentExpression= ruleIQLAssignmentExpression EOF )
            // InternalODL.g:4586:2: iv_ruleIQLAssignmentExpression= ruleIQLAssignmentExpression EOF
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
    // InternalODL.g:4592:1: ruleIQLAssignmentExpression returns [EObject current=null] : (this_IQLLogicalOrExpression_0= ruleIQLLogicalOrExpression ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )? ) ;
    public final EObject ruleIQLAssignmentExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLLogicalOrExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalODL.g:4598:2: ( (this_IQLLogicalOrExpression_0= ruleIQLLogicalOrExpression ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )? ) )
            // InternalODL.g:4599:2: (this_IQLLogicalOrExpression_0= ruleIQLLogicalOrExpression ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )? )
            {
            // InternalODL.g:4599:2: (this_IQLLogicalOrExpression_0= ruleIQLLogicalOrExpression ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )? )
            // InternalODL.g:4600:3: this_IQLLogicalOrExpression_0= ruleIQLLogicalOrExpression ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )?
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLAssignmentExpressionAccess().getIQLLogicalOrExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_55);
            this_IQLLogicalOrExpression_0=ruleIQLLogicalOrExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLLogicalOrExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalODL.g:4608:3: ( ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) ) )?
            int alt68=2;
            int LA68_0 = input.LA(1);

            if ( (LA68_0==49) && (synpred2_InternalODL())) {
                alt68=1;
            }
            else if ( (LA68_0==29) && (synpred2_InternalODL())) {
                alt68=1;
            }
            else if ( (LA68_0==31) && (synpred2_InternalODL())) {
                alt68=1;
            }
            else if ( (LA68_0==33) && (synpred2_InternalODL())) {
                alt68=1;
            }
            else if ( (LA68_0==35) && (synpred2_InternalODL())) {
                alt68=1;
            }
            else if ( (LA68_0==37) && (synpred2_InternalODL())) {
                alt68=1;
            }
            switch (alt68) {
                case 1 :
                    // InternalODL.g:4609:4: ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) )
                    {
                    // InternalODL.g:4609:4: ( ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) ) )
                    // InternalODL.g:4610:5: ( ( () ( ( ruleOpAssign ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAssign ) ) )
                    {
                    // InternalODL.g:4620:5: ( () ( (lv_op_2_0= ruleOpAssign ) ) )
                    // InternalODL.g:4621:6: () ( (lv_op_2_0= ruleOpAssign ) )
                    {
                    // InternalODL.g:4621:6: ()
                    // InternalODL.g:4622:7: 
                    {
                    if ( state.backtracking==0 ) {

                      							current = forceCreateModelElementAndSet(
                      								grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0(),
                      								current);
                      						
                    }

                    }

                    // InternalODL.g:4628:6: ( (lv_op_2_0= ruleOpAssign ) )
                    // InternalODL.g:4629:7: (lv_op_2_0= ruleOpAssign )
                    {
                    // InternalODL.g:4629:7: (lv_op_2_0= ruleOpAssign )
                    // InternalODL.g:4630:8: lv_op_2_0= ruleOpAssign
                    {
                    if ( state.backtracking==0 ) {

                      								newCompositeNode(grammarAccess.getIQLAssignmentExpressionAccess().getOpOpAssignParserRuleCall_1_0_0_1_0());
                      							
                    }
                    pushFollow(FOLLOW_43);
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

                    // InternalODL.g:4649:4: ( (lv_rightOperand_3_0= ruleIQLAssignmentExpression ) )
                    // InternalODL.g:4650:5: (lv_rightOperand_3_0= ruleIQLAssignmentExpression )
                    {
                    // InternalODL.g:4650:5: (lv_rightOperand_3_0= ruleIQLAssignmentExpression )
                    // InternalODL.g:4651:6: lv_rightOperand_3_0= ruleIQLAssignmentExpression
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
    // InternalODL.g:4673:1: entryRuleOpAssign returns [String current=null] : iv_ruleOpAssign= ruleOpAssign EOF ;
    public final String entryRuleOpAssign() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpAssign = null;


        try {
            // InternalODL.g:4673:48: (iv_ruleOpAssign= ruleOpAssign EOF )
            // InternalODL.g:4674:2: iv_ruleOpAssign= ruleOpAssign EOF
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
    // InternalODL.g:4680:1: ruleOpAssign returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '=' | kw= '+=' | kw= '-=' | kw= '*=' | kw= '/=' | kw= '%=' ) ;
    public final AntlrDatatypeRuleToken ruleOpAssign() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalODL.g:4686:2: ( (kw= '=' | kw= '+=' | kw= '-=' | kw= '*=' | kw= '/=' | kw= '%=' ) )
            // InternalODL.g:4687:2: (kw= '=' | kw= '+=' | kw= '-=' | kw= '*=' | kw= '/=' | kw= '%=' )
            {
            // InternalODL.g:4687:2: (kw= '=' | kw= '+=' | kw= '-=' | kw= '*=' | kw= '/=' | kw= '%=' )
            int alt69=6;
            switch ( input.LA(1) ) {
            case 49:
                {
                alt69=1;
                }
                break;
            case 29:
                {
                alt69=2;
                }
                break;
            case 31:
                {
                alt69=3;
                }
                break;
            case 33:
                {
                alt69=4;
                }
                break;
            case 35:
                {
                alt69=5;
                }
                break;
            case 37:
                {
                alt69=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 69, 0, input);

                throw nvae;
            }

            switch (alt69) {
                case 1 :
                    // InternalODL.g:4688:3: kw= '='
                    {
                    kw=(Token)match(input,49,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAssignAccess().getEqualsSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalODL.g:4694:3: kw= '+='
                    {
                    kw=(Token)match(input,29,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAssignAccess().getPlusSignEqualsSignKeyword_1());
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalODL.g:4700:3: kw= '-='
                    {
                    kw=(Token)match(input,31,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAssignAccess().getHyphenMinusEqualsSignKeyword_2());
                      		
                    }

                    }
                    break;
                case 4 :
                    // InternalODL.g:4706:3: kw= '*='
                    {
                    kw=(Token)match(input,33,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAssignAccess().getAsteriskEqualsSignKeyword_3());
                      		
                    }

                    }
                    break;
                case 5 :
                    // InternalODL.g:4712:3: kw= '/='
                    {
                    kw=(Token)match(input,35,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAssignAccess().getSolidusEqualsSignKeyword_4());
                      		
                    }

                    }
                    break;
                case 6 :
                    // InternalODL.g:4718:3: kw= '%='
                    {
                    kw=(Token)match(input,37,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:4727:1: entryRuleIQLLogicalOrExpression returns [EObject current=null] : iv_ruleIQLLogicalOrExpression= ruleIQLLogicalOrExpression EOF ;
    public final EObject entryRuleIQLLogicalOrExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLogicalOrExpression = null;


        try {
            // InternalODL.g:4727:63: (iv_ruleIQLLogicalOrExpression= ruleIQLLogicalOrExpression EOF )
            // InternalODL.g:4728:2: iv_ruleIQLLogicalOrExpression= ruleIQLLogicalOrExpression EOF
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
    // InternalODL.g:4734:1: ruleIQLLogicalOrExpression returns [EObject current=null] : (this_IQLLogicalAndExpression_0= ruleIQLLogicalAndExpression ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )* ) ;
    public final EObject ruleIQLLogicalOrExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLLogicalAndExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalODL.g:4740:2: ( (this_IQLLogicalAndExpression_0= ruleIQLLogicalAndExpression ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )* ) )
            // InternalODL.g:4741:2: (this_IQLLogicalAndExpression_0= ruleIQLLogicalAndExpression ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )* )
            {
            // InternalODL.g:4741:2: (this_IQLLogicalAndExpression_0= ruleIQLLogicalAndExpression ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )* )
            // InternalODL.g:4742:3: this_IQLLogicalAndExpression_0= ruleIQLLogicalAndExpression ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalAndExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_56);
            this_IQLLogicalAndExpression_0=ruleIQLLogicalAndExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLLogicalAndExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalODL.g:4750:3: ( ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) ) )*
            loop70:
            do {
                int alt70=2;
                int LA70_0 = input.LA(1);

                if ( (LA70_0==47) && (synpred3_InternalODL())) {
                    alt70=1;
                }


                switch (alt70) {
            	case 1 :
            	    // InternalODL.g:4751:4: ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) )
            	    {
            	    // InternalODL.g:4751:4: ( ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) ) )
            	    // InternalODL.g:4752:5: ( ( () ( ( ruleOpLogicalOr ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) )
            	    {
            	    // InternalODL.g:4762:5: ( () ( (lv_op_2_0= ruleOpLogicalOr ) ) )
            	    // InternalODL.g:4763:6: () ( (lv_op_2_0= ruleOpLogicalOr ) )
            	    {
            	    // InternalODL.g:4763:6: ()
            	    // InternalODL.g:4764:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    // InternalODL.g:4770:6: ( (lv_op_2_0= ruleOpLogicalOr ) )
            	    // InternalODL.g:4771:7: (lv_op_2_0= ruleOpLogicalOr )
            	    {
            	    // InternalODL.g:4771:7: (lv_op_2_0= ruleOpLogicalOr )
            	    // InternalODL.g:4772:8: lv_op_2_0= ruleOpLogicalOr
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLLogicalOrExpressionAccess().getOpOpLogicalOrParserRuleCall_1_0_0_1_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_43);
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

            	    // InternalODL.g:4791:4: ( (lv_rightOperand_3_0= ruleIQLLogicalAndExpression ) )
            	    // InternalODL.g:4792:5: (lv_rightOperand_3_0= ruleIQLLogicalAndExpression )
            	    {
            	    // InternalODL.g:4792:5: (lv_rightOperand_3_0= ruleIQLLogicalAndExpression )
            	    // InternalODL.g:4793:6: lv_rightOperand_3_0= ruleIQLLogicalAndExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLLogicalOrExpressionAccess().getRightOperandIQLLogicalAndExpressionParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_56);
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
            	    break loop70;
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
    // InternalODL.g:4815:1: entryRuleOpLogicalOr returns [String current=null] : iv_ruleOpLogicalOr= ruleOpLogicalOr EOF ;
    public final String entryRuleOpLogicalOr() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpLogicalOr = null;


        try {
            // InternalODL.g:4815:51: (iv_ruleOpLogicalOr= ruleOpLogicalOr EOF )
            // InternalODL.g:4816:2: iv_ruleOpLogicalOr= ruleOpLogicalOr EOF
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
    // InternalODL.g:4822:1: ruleOpLogicalOr returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= '||' ;
    public final AntlrDatatypeRuleToken ruleOpLogicalOr() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalODL.g:4828:2: (kw= '||' )
            // InternalODL.g:4829:2: kw= '||'
            {
            kw=(Token)match(input,47,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:4837:1: entryRuleIQLLogicalAndExpression returns [EObject current=null] : iv_ruleIQLLogicalAndExpression= ruleIQLLogicalAndExpression EOF ;
    public final EObject entryRuleIQLLogicalAndExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLogicalAndExpression = null;


        try {
            // InternalODL.g:4837:64: (iv_ruleIQLLogicalAndExpression= ruleIQLLogicalAndExpression EOF )
            // InternalODL.g:4838:2: iv_ruleIQLLogicalAndExpression= ruleIQLLogicalAndExpression EOF
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
    // InternalODL.g:4844:1: ruleIQLLogicalAndExpression returns [EObject current=null] : (this_IQLEqualityExpression_0= ruleIQLEqualityExpression ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )* ) ;
    public final EObject ruleIQLLogicalAndExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLEqualityExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalODL.g:4850:2: ( (this_IQLEqualityExpression_0= ruleIQLEqualityExpression ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )* ) )
            // InternalODL.g:4851:2: (this_IQLEqualityExpression_0= ruleIQLEqualityExpression ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )* )
            {
            // InternalODL.g:4851:2: (this_IQLEqualityExpression_0= ruleIQLEqualityExpression ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )* )
            // InternalODL.g:4852:3: this_IQLEqualityExpression_0= ruleIQLEqualityExpression ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLLogicalAndExpressionAccess().getIQLEqualityExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_57);
            this_IQLEqualityExpression_0=ruleIQLEqualityExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLEqualityExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalODL.g:4860:3: ( ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) ) )*
            loop71:
            do {
                int alt71=2;
                int LA71_0 = input.LA(1);

                if ( (LA71_0==46) && (synpred4_InternalODL())) {
                    alt71=1;
                }


                switch (alt71) {
            	case 1 :
            	    // InternalODL.g:4861:4: ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) )
            	    {
            	    // InternalODL.g:4861:4: ( ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) ) )
            	    // InternalODL.g:4862:5: ( ( () ( ( ruleOpLogicalAnd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) )
            	    {
            	    // InternalODL.g:4872:5: ( () ( (lv_op_2_0= ruleOpLogicalAnd ) ) )
            	    // InternalODL.g:4873:6: () ( (lv_op_2_0= ruleOpLogicalAnd ) )
            	    {
            	    // InternalODL.g:4873:6: ()
            	    // InternalODL.g:4874:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    // InternalODL.g:4880:6: ( (lv_op_2_0= ruleOpLogicalAnd ) )
            	    // InternalODL.g:4881:7: (lv_op_2_0= ruleOpLogicalAnd )
            	    {
            	    // InternalODL.g:4881:7: (lv_op_2_0= ruleOpLogicalAnd )
            	    // InternalODL.g:4882:8: lv_op_2_0= ruleOpLogicalAnd
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLLogicalAndExpressionAccess().getOpOpLogicalAndParserRuleCall_1_0_0_1_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_43);
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

            	    // InternalODL.g:4901:4: ( (lv_rightOperand_3_0= ruleIQLEqualityExpression ) )
            	    // InternalODL.g:4902:5: (lv_rightOperand_3_0= ruleIQLEqualityExpression )
            	    {
            	    // InternalODL.g:4902:5: (lv_rightOperand_3_0= ruleIQLEqualityExpression )
            	    // InternalODL.g:4903:6: lv_rightOperand_3_0= ruleIQLEqualityExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLLogicalAndExpressionAccess().getRightOperandIQLEqualityExpressionParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_57);
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
            	    break loop71;
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
    // InternalODL.g:4925:1: entryRuleOpLogicalAnd returns [String current=null] : iv_ruleOpLogicalAnd= ruleOpLogicalAnd EOF ;
    public final String entryRuleOpLogicalAnd() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpLogicalAnd = null;


        try {
            // InternalODL.g:4925:52: (iv_ruleOpLogicalAnd= ruleOpLogicalAnd EOF )
            // InternalODL.g:4926:2: iv_ruleOpLogicalAnd= ruleOpLogicalAnd EOF
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
    // InternalODL.g:4932:1: ruleOpLogicalAnd returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= '&&' ;
    public final AntlrDatatypeRuleToken ruleOpLogicalAnd() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalODL.g:4938:2: (kw= '&&' )
            // InternalODL.g:4939:2: kw= '&&'
            {
            kw=(Token)match(input,46,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:4947:1: entryRuleIQLEqualityExpression returns [EObject current=null] : iv_ruleIQLEqualityExpression= ruleIQLEqualityExpression EOF ;
    public final EObject entryRuleIQLEqualityExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLEqualityExpression = null;


        try {
            // InternalODL.g:4947:62: (iv_ruleIQLEqualityExpression= ruleIQLEqualityExpression EOF )
            // InternalODL.g:4948:2: iv_ruleIQLEqualityExpression= ruleIQLEqualityExpression EOF
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
    // InternalODL.g:4954:1: ruleIQLEqualityExpression returns [EObject current=null] : (this_IQLRelationalExpression_0= ruleIQLRelationalExpression ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )* ) ;
    public final EObject ruleIQLEqualityExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLRelationalExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalODL.g:4960:2: ( (this_IQLRelationalExpression_0= ruleIQLRelationalExpression ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )* ) )
            // InternalODL.g:4961:2: (this_IQLRelationalExpression_0= ruleIQLRelationalExpression ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )* )
            {
            // InternalODL.g:4961:2: (this_IQLRelationalExpression_0= ruleIQLRelationalExpression ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )* )
            // InternalODL.g:4962:3: this_IQLRelationalExpression_0= ruleIQLRelationalExpression ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLEqualityExpressionAccess().getIQLRelationalExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_58);
            this_IQLRelationalExpression_0=ruleIQLRelationalExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLRelationalExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalODL.g:4970:3: ( ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) ) )*
            loop72:
            do {
                int alt72=2;
                int LA72_0 = input.LA(1);

                if ( (LA72_0==48) && (synpred5_InternalODL())) {
                    alt72=1;
                }
                else if ( (LA72_0==45) && (synpred5_InternalODL())) {
                    alt72=1;
                }


                switch (alt72) {
            	case 1 :
            	    // InternalODL.g:4971:4: ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) )
            	    {
            	    // InternalODL.g:4971:4: ( ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) ) )
            	    // InternalODL.g:4972:5: ( ( () ( ( ruleOpEquality ) ) ) )=> ( () ( (lv_op_2_0= ruleOpEquality ) ) )
            	    {
            	    // InternalODL.g:4982:5: ( () ( (lv_op_2_0= ruleOpEquality ) ) )
            	    // InternalODL.g:4983:6: () ( (lv_op_2_0= ruleOpEquality ) )
            	    {
            	    // InternalODL.g:4983:6: ()
            	    // InternalODL.g:4984:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    // InternalODL.g:4990:6: ( (lv_op_2_0= ruleOpEquality ) )
            	    // InternalODL.g:4991:7: (lv_op_2_0= ruleOpEquality )
            	    {
            	    // InternalODL.g:4991:7: (lv_op_2_0= ruleOpEquality )
            	    // InternalODL.g:4992:8: lv_op_2_0= ruleOpEquality
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLEqualityExpressionAccess().getOpOpEqualityParserRuleCall_1_0_0_1_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_43);
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

            	    // InternalODL.g:5011:4: ( (lv_rightOperand_3_0= ruleIQLRelationalExpression ) )
            	    // InternalODL.g:5012:5: (lv_rightOperand_3_0= ruleIQLRelationalExpression )
            	    {
            	    // InternalODL.g:5012:5: (lv_rightOperand_3_0= ruleIQLRelationalExpression )
            	    // InternalODL.g:5013:6: lv_rightOperand_3_0= ruleIQLRelationalExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLEqualityExpressionAccess().getRightOperandIQLRelationalExpressionParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_58);
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
            	    break loop72;
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
    // InternalODL.g:5035:1: entryRuleOpEquality returns [String current=null] : iv_ruleOpEquality= ruleOpEquality EOF ;
    public final String entryRuleOpEquality() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpEquality = null;


        try {
            // InternalODL.g:5035:50: (iv_ruleOpEquality= ruleOpEquality EOF )
            // InternalODL.g:5036:2: iv_ruleOpEquality= ruleOpEquality EOF
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
    // InternalODL.g:5042:1: ruleOpEquality returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '==' | kw= '!=' ) ;
    public final AntlrDatatypeRuleToken ruleOpEquality() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalODL.g:5048:2: ( (kw= '==' | kw= '!=' ) )
            // InternalODL.g:5049:2: (kw= '==' | kw= '!=' )
            {
            // InternalODL.g:5049:2: (kw= '==' | kw= '!=' )
            int alt73=2;
            int LA73_0 = input.LA(1);

            if ( (LA73_0==48) ) {
                alt73=1;
            }
            else if ( (LA73_0==45) ) {
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
                    // InternalODL.g:5050:3: kw= '=='
                    {
                    kw=(Token)match(input,48,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpEqualityAccess().getEqualsSignEqualsSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalODL.g:5056:3: kw= '!='
                    {
                    kw=(Token)match(input,45,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:5065:1: entryRuleIQLRelationalExpression returns [EObject current=null] : iv_ruleIQLRelationalExpression= ruleIQLRelationalExpression EOF ;
    public final EObject entryRuleIQLRelationalExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLRelationalExpression = null;


        try {
            // InternalODL.g:5065:64: (iv_ruleIQLRelationalExpression= ruleIQLRelationalExpression EOF )
            // InternalODL.g:5066:2: iv_ruleIQLRelationalExpression= ruleIQLRelationalExpression EOF
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
    // InternalODL.g:5072:1: ruleIQLRelationalExpression returns [EObject current=null] : (this_IQLAdditiveExpression_0= ruleIQLAdditiveExpression ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) ) ) )* ) ;
    public final EObject ruleIQLRelationalExpression() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_IQLAdditiveExpression_0 = null;

        EObject lv_targetRef_3_0 = null;

        AntlrDatatypeRuleToken lv_op_5_0 = null;

        EObject lv_rightOperand_6_0 = null;



        	enterRule();

        try {
            // InternalODL.g:5078:2: ( (this_IQLAdditiveExpression_0= ruleIQLAdditiveExpression ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) ) ) )* ) )
            // InternalODL.g:5079:2: (this_IQLAdditiveExpression_0= ruleIQLAdditiveExpression ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) ) ) )* )
            {
            // InternalODL.g:5079:2: (this_IQLAdditiveExpression_0= ruleIQLAdditiveExpression ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) ) ) )* )
            // InternalODL.g:5080:3: this_IQLAdditiveExpression_0= ruleIQLAdditiveExpression ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLRelationalExpressionAccess().getIQLAdditiveExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_59);
            this_IQLAdditiveExpression_0=ruleIQLAdditiveExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLAdditiveExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalODL.g:5088:3: ( ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) ) | ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) ) ) )*
            loop74:
            do {
                int alt74=3;
                int LA74_0 = input.LA(1);

                if ( (LA74_0==87) && (synpred6_InternalODL())) {
                    alt74=1;
                }
                else if ( (LA74_0==40) && (synpred7_InternalODL())) {
                    alt74=2;
                }
                else if ( (LA74_0==41) && (synpred7_InternalODL())) {
                    alt74=2;
                }
                else if ( (LA74_0==42) && (synpred7_InternalODL())) {
                    alt74=2;
                }
                else if ( (LA74_0==43) && (synpred7_InternalODL())) {
                    alt74=2;
                }


                switch (alt74) {
            	case 1 :
            	    // InternalODL.g:5089:4: ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) )
            	    {
            	    // InternalODL.g:5089:4: ( ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) ) )
            	    // InternalODL.g:5090:5: ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) ) ( (lv_targetRef_3_0= ruleJvmTypeReference ) )
            	    {
            	    // InternalODL.g:5090:5: ( ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' ) )
            	    // InternalODL.g:5091:6: ( ( () 'instanceof' ) )=> ( () otherlv_2= 'instanceof' )
            	    {
            	    // InternalODL.g:5097:6: ( () otherlv_2= 'instanceof' )
            	    // InternalODL.g:5098:7: () otherlv_2= 'instanceof'
            	    {
            	    // InternalODL.g:5098:7: ()
            	    // InternalODL.g:5099:8: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      								current = forceCreateModelElementAndSet(
            	      									grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0(),
            	      									current);
            	      							
            	    }

            	    }

            	    otherlv_2=(Token)match(input,87,FOLLOW_6); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      							newLeafNode(otherlv_2, grammarAccess.getIQLRelationalExpressionAccess().getInstanceofKeyword_1_0_0_0_1());
            	      						
            	    }

            	    }


            	    }

            	    // InternalODL.g:5111:5: ( (lv_targetRef_3_0= ruleJvmTypeReference ) )
            	    // InternalODL.g:5112:6: (lv_targetRef_3_0= ruleJvmTypeReference )
            	    {
            	    // InternalODL.g:5112:6: (lv_targetRef_3_0= ruleJvmTypeReference )
            	    // InternalODL.g:5113:7: lv_targetRef_3_0= ruleJvmTypeReference
            	    {
            	    if ( state.backtracking==0 ) {

            	      							newCompositeNode(grammarAccess.getIQLRelationalExpressionAccess().getTargetRefJvmTypeReferenceParserRuleCall_1_0_1_0());
            	      						
            	    }
            	    pushFollow(FOLLOW_59);
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
            	    // InternalODL.g:5132:4: ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) ) )
            	    {
            	    // InternalODL.g:5132:4: ( ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) ) )
            	    // InternalODL.g:5133:5: ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) ) ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) )
            	    {
            	    // InternalODL.g:5133:5: ( ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) ) )
            	    // InternalODL.g:5134:6: ( ( () ( ( ruleOpRelational ) ) ) )=> ( () ( (lv_op_5_0= ruleOpRelational ) ) )
            	    {
            	    // InternalODL.g:5144:6: ( () ( (lv_op_5_0= ruleOpRelational ) ) )
            	    // InternalODL.g:5145:7: () ( (lv_op_5_0= ruleOpRelational ) )
            	    {
            	    // InternalODL.g:5145:7: ()
            	    // InternalODL.g:5146:8: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      								current = forceCreateModelElementAndSet(
            	      									grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0(),
            	      									current);
            	      							
            	    }

            	    }

            	    // InternalODL.g:5152:7: ( (lv_op_5_0= ruleOpRelational ) )
            	    // InternalODL.g:5153:8: (lv_op_5_0= ruleOpRelational )
            	    {
            	    // InternalODL.g:5153:8: (lv_op_5_0= ruleOpRelational )
            	    // InternalODL.g:5154:9: lv_op_5_0= ruleOpRelational
            	    {
            	    if ( state.backtracking==0 ) {

            	      									newCompositeNode(grammarAccess.getIQLRelationalExpressionAccess().getOpOpRelationalParserRuleCall_1_1_0_0_1_0());
            	      								
            	    }
            	    pushFollow(FOLLOW_43);
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

            	    // InternalODL.g:5173:5: ( (lv_rightOperand_6_0= ruleIQLAdditiveExpression ) )
            	    // InternalODL.g:5174:6: (lv_rightOperand_6_0= ruleIQLAdditiveExpression )
            	    {
            	    // InternalODL.g:5174:6: (lv_rightOperand_6_0= ruleIQLAdditiveExpression )
            	    // InternalODL.g:5175:7: lv_rightOperand_6_0= ruleIQLAdditiveExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      							newCompositeNode(grammarAccess.getIQLRelationalExpressionAccess().getRightOperandIQLAdditiveExpressionParserRuleCall_1_1_1_0());
            	      						
            	    }
            	    pushFollow(FOLLOW_59);
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
            	    break loop74;
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
    // InternalODL.g:5198:1: entryRuleOpRelational returns [String current=null] : iv_ruleOpRelational= ruleOpRelational EOF ;
    public final String entryRuleOpRelational() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpRelational = null;


        try {
            // InternalODL.g:5198:52: (iv_ruleOpRelational= ruleOpRelational EOF )
            // InternalODL.g:5199:2: iv_ruleOpRelational= ruleOpRelational EOF
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
    // InternalODL.g:5205:1: ruleOpRelational returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '>' | kw= '>=' | kw= '<' | kw= '<=' ) ;
    public final AntlrDatatypeRuleToken ruleOpRelational() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalODL.g:5211:2: ( (kw= '>' | kw= '>=' | kw= '<' | kw= '<=' ) )
            // InternalODL.g:5212:2: (kw= '>' | kw= '>=' | kw= '<' | kw= '<=' )
            {
            // InternalODL.g:5212:2: (kw= '>' | kw= '>=' | kw= '<' | kw= '<=' )
            int alt75=4;
            switch ( input.LA(1) ) {
            case 40:
                {
                alt75=1;
                }
                break;
            case 41:
                {
                alt75=2;
                }
                break;
            case 42:
                {
                alt75=3;
                }
                break;
            case 43:
                {
                alt75=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 75, 0, input);

                throw nvae;
            }

            switch (alt75) {
                case 1 :
                    // InternalODL.g:5213:3: kw= '>'
                    {
                    kw=(Token)match(input,40,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpRelationalAccess().getGreaterThanSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalODL.g:5219:3: kw= '>='
                    {
                    kw=(Token)match(input,41,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpRelationalAccess().getGreaterThanSignEqualsSignKeyword_1());
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalODL.g:5225:3: kw= '<'
                    {
                    kw=(Token)match(input,42,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpRelationalAccess().getLessThanSignKeyword_2());
                      		
                    }

                    }
                    break;
                case 4 :
                    // InternalODL.g:5231:3: kw= '<='
                    {
                    kw=(Token)match(input,43,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:5240:1: entryRuleIQLAdditiveExpression returns [EObject current=null] : iv_ruleIQLAdditiveExpression= ruleIQLAdditiveExpression EOF ;
    public final EObject entryRuleIQLAdditiveExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLAdditiveExpression = null;


        try {
            // InternalODL.g:5240:62: (iv_ruleIQLAdditiveExpression= ruleIQLAdditiveExpression EOF )
            // InternalODL.g:5241:2: iv_ruleIQLAdditiveExpression= ruleIQLAdditiveExpression EOF
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
    // InternalODL.g:5247:1: ruleIQLAdditiveExpression returns [EObject current=null] : (this_IQLMultiplicativeExpression_0= ruleIQLMultiplicativeExpression ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )* ) ;
    public final EObject ruleIQLAdditiveExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLMultiplicativeExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalODL.g:5253:2: ( (this_IQLMultiplicativeExpression_0= ruleIQLMultiplicativeExpression ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )* ) )
            // InternalODL.g:5254:2: (this_IQLMultiplicativeExpression_0= ruleIQLMultiplicativeExpression ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )* )
            {
            // InternalODL.g:5254:2: (this_IQLMultiplicativeExpression_0= ruleIQLMultiplicativeExpression ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )* )
            // InternalODL.g:5255:3: this_IQLMultiplicativeExpression_0= ruleIQLMultiplicativeExpression ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLAdditiveExpressionAccess().getIQLMultiplicativeExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_60);
            this_IQLMultiplicativeExpression_0=ruleIQLMultiplicativeExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLMultiplicativeExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalODL.g:5263:3: ( ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) ) )*
            loop76:
            do {
                int alt76=2;
                int LA76_0 = input.LA(1);

                if ( (LA76_0==28) && (synpred8_InternalODL())) {
                    alt76=1;
                }
                else if ( (LA76_0==30) && (synpred8_InternalODL())) {
                    alt76=1;
                }


                switch (alt76) {
            	case 1 :
            	    // InternalODL.g:5264:4: ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) )
            	    {
            	    // InternalODL.g:5264:4: ( ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) ) )
            	    // InternalODL.g:5265:5: ( ( () ( ( ruleOpAdd ) ) ) )=> ( () ( (lv_op_2_0= ruleOpAdd ) ) )
            	    {
            	    // InternalODL.g:5275:5: ( () ( (lv_op_2_0= ruleOpAdd ) ) )
            	    // InternalODL.g:5276:6: () ( (lv_op_2_0= ruleOpAdd ) )
            	    {
            	    // InternalODL.g:5276:6: ()
            	    // InternalODL.g:5277:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    // InternalODL.g:5283:6: ( (lv_op_2_0= ruleOpAdd ) )
            	    // InternalODL.g:5284:7: (lv_op_2_0= ruleOpAdd )
            	    {
            	    // InternalODL.g:5284:7: (lv_op_2_0= ruleOpAdd )
            	    // InternalODL.g:5285:8: lv_op_2_0= ruleOpAdd
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLAdditiveExpressionAccess().getOpOpAddParserRuleCall_1_0_0_1_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_43);
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

            	    // InternalODL.g:5304:4: ( (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression ) )
            	    // InternalODL.g:5305:5: (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression )
            	    {
            	    // InternalODL.g:5305:5: (lv_rightOperand_3_0= ruleIQLMultiplicativeExpression )
            	    // InternalODL.g:5306:6: lv_rightOperand_3_0= ruleIQLMultiplicativeExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLAdditiveExpressionAccess().getRightOperandIQLMultiplicativeExpressionParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_60);
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
            	    break loop76;
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
    // InternalODL.g:5328:1: entryRuleOpAdd returns [String current=null] : iv_ruleOpAdd= ruleOpAdd EOF ;
    public final String entryRuleOpAdd() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpAdd = null;


        try {
            // InternalODL.g:5328:45: (iv_ruleOpAdd= ruleOpAdd EOF )
            // InternalODL.g:5329:2: iv_ruleOpAdd= ruleOpAdd EOF
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
    // InternalODL.g:5335:1: ruleOpAdd returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '+' | kw= '-' ) ;
    public final AntlrDatatypeRuleToken ruleOpAdd() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalODL.g:5341:2: ( (kw= '+' | kw= '-' ) )
            // InternalODL.g:5342:2: (kw= '+' | kw= '-' )
            {
            // InternalODL.g:5342:2: (kw= '+' | kw= '-' )
            int alt77=2;
            int LA77_0 = input.LA(1);

            if ( (LA77_0==28) ) {
                alt77=1;
            }
            else if ( (LA77_0==30) ) {
                alt77=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 77, 0, input);

                throw nvae;
            }
            switch (alt77) {
                case 1 :
                    // InternalODL.g:5343:3: kw= '+'
                    {
                    kw=(Token)match(input,28,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpAddAccess().getPlusSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalODL.g:5349:3: kw= '-'
                    {
                    kw=(Token)match(input,30,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:5358:1: entryRuleIQLMultiplicativeExpression returns [EObject current=null] : iv_ruleIQLMultiplicativeExpression= ruleIQLMultiplicativeExpression EOF ;
    public final EObject entryRuleIQLMultiplicativeExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMultiplicativeExpression = null;


        try {
            // InternalODL.g:5358:68: (iv_ruleIQLMultiplicativeExpression= ruleIQLMultiplicativeExpression EOF )
            // InternalODL.g:5359:2: iv_ruleIQLMultiplicativeExpression= ruleIQLMultiplicativeExpression EOF
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
    // InternalODL.g:5365:1: ruleIQLMultiplicativeExpression returns [EObject current=null] : (this_IQLUnaryExpression_0= ruleIQLUnaryExpression ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )* ) ;
    public final EObject ruleIQLMultiplicativeExpression() throws RecognitionException {
        EObject current = null;

        EObject this_IQLUnaryExpression_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_rightOperand_3_0 = null;



        	enterRule();

        try {
            // InternalODL.g:5371:2: ( (this_IQLUnaryExpression_0= ruleIQLUnaryExpression ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )* ) )
            // InternalODL.g:5372:2: (this_IQLUnaryExpression_0= ruleIQLUnaryExpression ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )* )
            {
            // InternalODL.g:5372:2: (this_IQLUnaryExpression_0= ruleIQLUnaryExpression ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )* )
            // InternalODL.g:5373:3: this_IQLUnaryExpression_0= ruleIQLUnaryExpression ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLUnaryExpressionParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_61);
            this_IQLUnaryExpression_0=ruleIQLUnaryExpression();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLUnaryExpression_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalODL.g:5381:3: ( ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) ) )*
            loop78:
            do {
                int alt78=2;
                int LA78_0 = input.LA(1);

                if ( (LA78_0==32) && (synpred9_InternalODL())) {
                    alt78=1;
                }
                else if ( (LA78_0==34) && (synpred9_InternalODL())) {
                    alt78=1;
                }
                else if ( (LA78_0==36) && (synpred9_InternalODL())) {
                    alt78=1;
                }


                switch (alt78) {
            	case 1 :
            	    // InternalODL.g:5382:4: ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) ) ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) )
            	    {
            	    // InternalODL.g:5382:4: ( ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) ) )
            	    // InternalODL.g:5383:5: ( ( () ( ( ruleOpMulti ) ) ) )=> ( () ( (lv_op_2_0= ruleOpMulti ) ) )
            	    {
            	    // InternalODL.g:5393:5: ( () ( (lv_op_2_0= ruleOpMulti ) ) )
            	    // InternalODL.g:5394:6: () ( (lv_op_2_0= ruleOpMulti ) )
            	    {
            	    // InternalODL.g:5394:6: ()
            	    // InternalODL.g:5395:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    // InternalODL.g:5401:6: ( (lv_op_2_0= ruleOpMulti ) )
            	    // InternalODL.g:5402:7: (lv_op_2_0= ruleOpMulti )
            	    {
            	    // InternalODL.g:5402:7: (lv_op_2_0= ruleOpMulti )
            	    // InternalODL.g:5403:8: lv_op_2_0= ruleOpMulti
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLMultiplicativeExpressionAccess().getOpOpMultiParserRuleCall_1_0_0_1_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_43);
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

            	    // InternalODL.g:5422:4: ( (lv_rightOperand_3_0= ruleIQLUnaryExpression ) )
            	    // InternalODL.g:5423:5: (lv_rightOperand_3_0= ruleIQLUnaryExpression )
            	    {
            	    // InternalODL.g:5423:5: (lv_rightOperand_3_0= ruleIQLUnaryExpression )
            	    // InternalODL.g:5424:6: lv_rightOperand_3_0= ruleIQLUnaryExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getIQLMultiplicativeExpressionAccess().getRightOperandIQLUnaryExpressionParserRuleCall_1_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_61);
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
            	    break loop78;
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
    // InternalODL.g:5446:1: entryRuleOpMulti returns [String current=null] : iv_ruleOpMulti= ruleOpMulti EOF ;
    public final String entryRuleOpMulti() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpMulti = null;


        try {
            // InternalODL.g:5446:47: (iv_ruleOpMulti= ruleOpMulti EOF )
            // InternalODL.g:5447:2: iv_ruleOpMulti= ruleOpMulti EOF
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
    // InternalODL.g:5453:1: ruleOpMulti returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '*' | kw= '/' | kw= '%' ) ;
    public final AntlrDatatypeRuleToken ruleOpMulti() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalODL.g:5459:2: ( (kw= '*' | kw= '/' | kw= '%' ) )
            // InternalODL.g:5460:2: (kw= '*' | kw= '/' | kw= '%' )
            {
            // InternalODL.g:5460:2: (kw= '*' | kw= '/' | kw= '%' )
            int alt79=3;
            switch ( input.LA(1) ) {
            case 32:
                {
                alt79=1;
                }
                break;
            case 34:
                {
                alt79=2;
                }
                break;
            case 36:
                {
                alt79=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 79, 0, input);

                throw nvae;
            }

            switch (alt79) {
                case 1 :
                    // InternalODL.g:5461:3: kw= '*'
                    {
                    kw=(Token)match(input,32,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpMultiAccess().getAsteriskKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalODL.g:5467:3: kw= '/'
                    {
                    kw=(Token)match(input,34,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpMultiAccess().getSolidusKeyword_1());
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalODL.g:5473:3: kw= '%'
                    {
                    kw=(Token)match(input,36,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:5482:1: entryRuleIQLUnaryExpression returns [EObject current=null] : iv_ruleIQLUnaryExpression= ruleIQLUnaryExpression EOF ;
    public final EObject entryRuleIQLUnaryExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLUnaryExpression = null;


        try {
            // InternalODL.g:5482:59: (iv_ruleIQLUnaryExpression= ruleIQLUnaryExpression EOF )
            // InternalODL.g:5483:2: iv_ruleIQLUnaryExpression= ruleIQLUnaryExpression EOF
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
    // InternalODL.g:5489:1: ruleIQLUnaryExpression returns [EObject current=null] : ( ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) ) | ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) ) | (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? ) ) ;
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
            // InternalODL.g:5495:2: ( ( ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) ) | ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) ) | (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? ) ) )
            // InternalODL.g:5496:2: ( ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) ) | ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) ) | (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? ) )
            {
            // InternalODL.g:5496:2: ( ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) ) | ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) ) | (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? ) )
            int alt81=5;
            alt81 = dfa81.predict(input);
            switch (alt81) {
                case 1 :
                    // InternalODL.g:5497:3: ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) )
                    {
                    // InternalODL.g:5497:3: ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) )
                    // InternalODL.g:5498:4: ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) )
                    {
                    // InternalODL.g:5498:4: ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) )
                    // InternalODL.g:5499:5: () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) )
                    {
                    // InternalODL.g:5499:5: ()
                    // InternalODL.g:5500:6: 
                    {
                    if ( state.backtracking==0 ) {

                      						current = forceCreateModelElement(
                      							grammarAccess.getIQLUnaryExpressionAccess().getIQLPlusMinusExpressionAction_0_0_0(),
                      							current);
                      					
                    }

                    }

                    // InternalODL.g:5506:5: ( (lv_op_1_0= ruleOpUnaryPlusMinus ) )
                    // InternalODL.g:5507:6: (lv_op_1_0= ruleOpUnaryPlusMinus )
                    {
                    // InternalODL.g:5507:6: (lv_op_1_0= ruleOpUnaryPlusMinus )
                    // InternalODL.g:5508:7: lv_op_1_0= ruleOpUnaryPlusMinus
                    {
                    if ( state.backtracking==0 ) {

                      							newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getOpOpUnaryPlusMinusParserRuleCall_0_0_1_0());
                      						
                    }
                    pushFollow(FOLLOW_43);
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

                    // InternalODL.g:5526:4: ( (lv_operand_2_0= ruleIQLMemberCallExpression ) )
                    // InternalODL.g:5527:5: (lv_operand_2_0= ruleIQLMemberCallExpression )
                    {
                    // InternalODL.g:5527:5: (lv_operand_2_0= ruleIQLMemberCallExpression )
                    // InternalODL.g:5528:6: lv_operand_2_0= ruleIQLMemberCallExpression
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
                    // InternalODL.g:5547:3: ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) )
                    {
                    // InternalODL.g:5547:3: ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) )
                    // InternalODL.g:5548:4: ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) )
                    {
                    // InternalODL.g:5548:4: ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) )
                    // InternalODL.g:5549:5: () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) )
                    {
                    // InternalODL.g:5549:5: ()
                    // InternalODL.g:5550:6: 
                    {
                    if ( state.backtracking==0 ) {

                      						current = forceCreateModelElement(
                      							grammarAccess.getIQLUnaryExpressionAccess().getIQLBooleanNotExpressionAction_1_0_0(),
                      							current);
                      					
                    }

                    }

                    // InternalODL.g:5556:5: ( (lv_op_4_0= ruleOpUnaryBooleanNot ) )
                    // InternalODL.g:5557:6: (lv_op_4_0= ruleOpUnaryBooleanNot )
                    {
                    // InternalODL.g:5557:6: (lv_op_4_0= ruleOpUnaryBooleanNot )
                    // InternalODL.g:5558:7: lv_op_4_0= ruleOpUnaryBooleanNot
                    {
                    if ( state.backtracking==0 ) {

                      							newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getOpOpUnaryBooleanNotParserRuleCall_1_0_1_0());
                      						
                    }
                    pushFollow(FOLLOW_43);
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

                    // InternalODL.g:5576:4: ( (lv_operand_5_0= ruleIQLMemberCallExpression ) )
                    // InternalODL.g:5577:5: (lv_operand_5_0= ruleIQLMemberCallExpression )
                    {
                    // InternalODL.g:5577:5: (lv_operand_5_0= ruleIQLMemberCallExpression )
                    // InternalODL.g:5578:6: lv_operand_5_0= ruleIQLMemberCallExpression
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
                    // InternalODL.g:5597:3: ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) )
                    {
                    // InternalODL.g:5597:3: ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) )
                    // InternalODL.g:5598:4: ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) )
                    {
                    // InternalODL.g:5598:4: ( () ( (lv_op_7_0= ruleOpPrefix ) ) )
                    // InternalODL.g:5599:5: () ( (lv_op_7_0= ruleOpPrefix ) )
                    {
                    // InternalODL.g:5599:5: ()
                    // InternalODL.g:5600:6: 
                    {
                    if ( state.backtracking==0 ) {

                      						current = forceCreateModelElement(
                      							grammarAccess.getIQLUnaryExpressionAccess().getIQLPrefixExpressionAction_2_0_0(),
                      							current);
                      					
                    }

                    }

                    // InternalODL.g:5606:5: ( (lv_op_7_0= ruleOpPrefix ) )
                    // InternalODL.g:5607:6: (lv_op_7_0= ruleOpPrefix )
                    {
                    // InternalODL.g:5607:6: (lv_op_7_0= ruleOpPrefix )
                    // InternalODL.g:5608:7: lv_op_7_0= ruleOpPrefix
                    {
                    if ( state.backtracking==0 ) {

                      							newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getOpOpPrefixParserRuleCall_2_0_1_0());
                      						
                    }
                    pushFollow(FOLLOW_43);
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

                    // InternalODL.g:5626:4: ( (lv_operand_8_0= ruleIQLMemberCallExpression ) )
                    // InternalODL.g:5627:5: (lv_operand_8_0= ruleIQLMemberCallExpression )
                    {
                    // InternalODL.g:5627:5: (lv_operand_8_0= ruleIQLMemberCallExpression )
                    // InternalODL.g:5628:6: lv_operand_8_0= ruleIQLMemberCallExpression
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
                    // InternalODL.g:5647:3: ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) )
                    {
                    // InternalODL.g:5647:3: ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) )
                    // InternalODL.g:5648:4: ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) )
                    {
                    // InternalODL.g:5648:4: ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) )
                    // InternalODL.g:5649:5: ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' )
                    {
                    // InternalODL.g:5661:5: ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' )
                    // InternalODL.g:5662:6: () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')'
                    {
                    // InternalODL.g:5662:6: ()
                    // InternalODL.g:5663:7: 
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
                    // InternalODL.g:5673:6: ( (lv_targetRef_11_0= ruleJvmTypeReference ) )
                    // InternalODL.g:5674:7: (lv_targetRef_11_0= ruleJvmTypeReference )
                    {
                    // InternalODL.g:5674:7: (lv_targetRef_11_0= ruleJvmTypeReference )
                    // InternalODL.g:5675:8: lv_targetRef_11_0= ruleJvmTypeReference
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

                    otherlv_12=(Token)match(input,15,FOLLOW_43); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(otherlv_12, grammarAccess.getIQLUnaryExpressionAccess().getRightParenthesisKeyword_3_0_0_3());
                      					
                    }

                    }


                    }

                    // InternalODL.g:5698:4: ( (lv_operand_13_0= ruleIQLMemberCallExpression ) )
                    // InternalODL.g:5699:5: (lv_operand_13_0= ruleIQLMemberCallExpression )
                    {
                    // InternalODL.g:5699:5: (lv_operand_13_0= ruleIQLMemberCallExpression )
                    // InternalODL.g:5700:6: lv_operand_13_0= ruleIQLMemberCallExpression
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
                    // InternalODL.g:5719:3: (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? )
                    {
                    // InternalODL.g:5719:3: (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? )
                    // InternalODL.g:5720:4: this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )?
                    {
                    if ( state.backtracking==0 ) {

                      				newCompositeNode(grammarAccess.getIQLUnaryExpressionAccess().getIQLMemberCallExpressionParserRuleCall_4_0());
                      			
                    }
                    pushFollow(FOLLOW_62);
                    this_IQLMemberCallExpression_14=ruleIQLMemberCallExpression();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = this_IQLMemberCallExpression_14;
                      				afterParserOrEnumRuleCall();
                      			
                    }
                    // InternalODL.g:5728:4: ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )?
                    int alt80=2;
                    int LA80_0 = input.LA(1);

                    if ( (LA80_0==38) && (synpred11_InternalODL())) {
                        alt80=1;
                    }
                    else if ( (LA80_0==39) && (synpred11_InternalODL())) {
                        alt80=1;
                    }
                    switch (alt80) {
                        case 1 :
                            // InternalODL.g:5729:5: ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) )
                            {
                            // InternalODL.g:5739:5: ( () ( (lv_op_16_0= ruleOpPostfix ) ) )
                            // InternalODL.g:5740:6: () ( (lv_op_16_0= ruleOpPostfix ) )
                            {
                            // InternalODL.g:5740:6: ()
                            // InternalODL.g:5741:7: 
                            {
                            if ( state.backtracking==0 ) {

                              							current = forceCreateModelElementAndSet(
                              								grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0(),
                              								current);
                              						
                            }

                            }

                            // InternalODL.g:5747:6: ( (lv_op_16_0= ruleOpPostfix ) )
                            // InternalODL.g:5748:7: (lv_op_16_0= ruleOpPostfix )
                            {
                            // InternalODL.g:5748:7: (lv_op_16_0= ruleOpPostfix )
                            // InternalODL.g:5749:8: lv_op_16_0= ruleOpPostfix
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
    // InternalODL.g:5773:1: entryRuleOpUnaryPlusMinus returns [String current=null] : iv_ruleOpUnaryPlusMinus= ruleOpUnaryPlusMinus EOF ;
    public final String entryRuleOpUnaryPlusMinus() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpUnaryPlusMinus = null;


        try {
            // InternalODL.g:5773:56: (iv_ruleOpUnaryPlusMinus= ruleOpUnaryPlusMinus EOF )
            // InternalODL.g:5774:2: iv_ruleOpUnaryPlusMinus= ruleOpUnaryPlusMinus EOF
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
    // InternalODL.g:5780:1: ruleOpUnaryPlusMinus returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '-' | kw= '+' ) ;
    public final AntlrDatatypeRuleToken ruleOpUnaryPlusMinus() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalODL.g:5786:2: ( (kw= '-' | kw= '+' ) )
            // InternalODL.g:5787:2: (kw= '-' | kw= '+' )
            {
            // InternalODL.g:5787:2: (kw= '-' | kw= '+' )
            int alt82=2;
            int LA82_0 = input.LA(1);

            if ( (LA82_0==30) ) {
                alt82=1;
            }
            else if ( (LA82_0==28) ) {
                alt82=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 82, 0, input);

                throw nvae;
            }
            switch (alt82) {
                case 1 :
                    // InternalODL.g:5788:3: kw= '-'
                    {
                    kw=(Token)match(input,30,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpUnaryPlusMinusAccess().getHyphenMinusKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalODL.g:5794:3: kw= '+'
                    {
                    kw=(Token)match(input,28,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:5803:1: entryRuleOpUnaryBooleanNot returns [String current=null] : iv_ruleOpUnaryBooleanNot= ruleOpUnaryBooleanNot EOF ;
    public final String entryRuleOpUnaryBooleanNot() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpUnaryBooleanNot = null;


        try {
            // InternalODL.g:5803:57: (iv_ruleOpUnaryBooleanNot= ruleOpUnaryBooleanNot EOF )
            // InternalODL.g:5804:2: iv_ruleOpUnaryBooleanNot= ruleOpUnaryBooleanNot EOF
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
    // InternalODL.g:5810:1: ruleOpUnaryBooleanNot returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= '!' ;
    public final AntlrDatatypeRuleToken ruleOpUnaryBooleanNot() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalODL.g:5816:2: (kw= '!' )
            // InternalODL.g:5817:2: kw= '!'
            {
            kw=(Token)match(input,44,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:5825:1: entryRuleOpPrefix returns [String current=null] : iv_ruleOpPrefix= ruleOpPrefix EOF ;
    public final String entryRuleOpPrefix() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpPrefix = null;


        try {
            // InternalODL.g:5825:48: (iv_ruleOpPrefix= ruleOpPrefix EOF )
            // InternalODL.g:5826:2: iv_ruleOpPrefix= ruleOpPrefix EOF
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
    // InternalODL.g:5832:1: ruleOpPrefix returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '++' | kw= '--' ) ;
    public final AntlrDatatypeRuleToken ruleOpPrefix() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalODL.g:5838:2: ( (kw= '++' | kw= '--' ) )
            // InternalODL.g:5839:2: (kw= '++' | kw= '--' )
            {
            // InternalODL.g:5839:2: (kw= '++' | kw= '--' )
            int alt83=2;
            int LA83_0 = input.LA(1);

            if ( (LA83_0==38) ) {
                alt83=1;
            }
            else if ( (LA83_0==39) ) {
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
                    // InternalODL.g:5840:3: kw= '++'
                    {
                    kw=(Token)match(input,38,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpPrefixAccess().getPlusSignPlusSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalODL.g:5846:3: kw= '--'
                    {
                    kw=(Token)match(input,39,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:5855:1: entryRuleOpPostfix returns [String current=null] : iv_ruleOpPostfix= ruleOpPostfix EOF ;
    public final String entryRuleOpPostfix() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOpPostfix = null;


        try {
            // InternalODL.g:5855:49: (iv_ruleOpPostfix= ruleOpPostfix EOF )
            // InternalODL.g:5856:2: iv_ruleOpPostfix= ruleOpPostfix EOF
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
    // InternalODL.g:5862:1: ruleOpPostfix returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '++' | kw= '--' ) ;
    public final AntlrDatatypeRuleToken ruleOpPostfix() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalODL.g:5868:2: ( (kw= '++' | kw= '--' ) )
            // InternalODL.g:5869:2: (kw= '++' | kw= '--' )
            {
            // InternalODL.g:5869:2: (kw= '++' | kw= '--' )
            int alt84=2;
            int LA84_0 = input.LA(1);

            if ( (LA84_0==38) ) {
                alt84=1;
            }
            else if ( (LA84_0==39) ) {
                alt84=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 84, 0, input);

                throw nvae;
            }
            switch (alt84) {
                case 1 :
                    // InternalODL.g:5870:3: kw= '++'
                    {
                    kw=(Token)match(input,38,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getOpPostfixAccess().getPlusSignPlusSignKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalODL.g:5876:3: kw= '--'
                    {
                    kw=(Token)match(input,39,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:5885:1: entryRuleIQLMemberCallExpression returns [EObject current=null] : iv_ruleIQLMemberCallExpression= ruleIQLMemberCallExpression EOF ;
    public final EObject entryRuleIQLMemberCallExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMemberCallExpression = null;


        try {
            // InternalODL.g:5885:64: (iv_ruleIQLMemberCallExpression= ruleIQLMemberCallExpression EOF )
            // InternalODL.g:5886:2: iv_ruleIQLMemberCallExpression= ruleIQLMemberCallExpression EOF
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
    // InternalODL.g:5892:1: ruleIQLMemberCallExpression returns [EObject current=null] : (this_IQLOtherExpressions_0= ruleIQLOtherExpressions ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )* ) ;
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
            // InternalODL.g:5898:2: ( (this_IQLOtherExpressions_0= ruleIQLOtherExpressions ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )* ) )
            // InternalODL.g:5899:2: (this_IQLOtherExpressions_0= ruleIQLOtherExpressions ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )* )
            {
            // InternalODL.g:5899:2: (this_IQLOtherExpressions_0= ruleIQLOtherExpressions ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )* )
            // InternalODL.g:5900:3: this_IQLOtherExpressions_0= ruleIQLOtherExpressions ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getIQLMemberCallExpressionAccess().getIQLOtherExpressionsParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_63);
            this_IQLOtherExpressions_0=ruleIQLOtherExpressions();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_IQLOtherExpressions_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalODL.g:5908:3: ( ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) ) | ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) ) )*
            loop86:
            do {
                int alt86=3;
                int LA86_0 = input.LA(1);

                if ( (LA86_0==64) && (synpred12_InternalODL())) {
                    alt86=1;
                }
                else if ( (LA86_0==66) && (synpred13_InternalODL())) {
                    alt86=2;
                }


                switch (alt86) {
            	case 1 :
            	    // InternalODL.g:5909:4: ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) )
            	    {
            	    // InternalODL.g:5909:4: ( ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' ) )
            	    // InternalODL.g:5910:5: ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )=> ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' )
            	    {
            	    // InternalODL.g:5930:5: ( () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']' )
            	    // InternalODL.g:5931:6: () otherlv_2= '[' ( (lv_expressions_3_0= ruleIQLExpression ) ) (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )* otherlv_6= ']'
            	    {
            	    // InternalODL.g:5931:6: ()
            	    // InternalODL.g:5932:7: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      							current = forceCreateModelElementAndSet(
            	      								grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0(),
            	      								current);
            	      						
            	    }

            	    }

            	    otherlv_2=(Token)match(input,64,FOLLOW_43); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						newLeafNode(otherlv_2, grammarAccess.getIQLMemberCallExpressionAccess().getLeftSquareBracketKeyword_1_0_0_1());
            	      					
            	    }
            	    // InternalODL.g:5942:6: ( (lv_expressions_3_0= ruleIQLExpression ) )
            	    // InternalODL.g:5943:7: (lv_expressions_3_0= ruleIQLExpression )
            	    {
            	    // InternalODL.g:5943:7: (lv_expressions_3_0= ruleIQLExpression )
            	    // InternalODL.g:5944:8: lv_expressions_3_0= ruleIQLExpression
            	    {
            	    if ( state.backtracking==0 ) {

            	      								newCompositeNode(grammarAccess.getIQLMemberCallExpressionAccess().getExpressionsIQLExpressionParserRuleCall_1_0_0_2_0());
            	      							
            	    }
            	    pushFollow(FOLLOW_38);
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

            	    // InternalODL.g:5961:6: (otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) ) )*
            	    loop85:
            	    do {
            	        int alt85=2;
            	        int LA85_0 = input.LA(1);

            	        if ( (LA85_0==26) ) {
            	            alt85=1;
            	        }


            	        switch (alt85) {
            	    	case 1 :
            	    	    // InternalODL.g:5962:7: otherlv_4= ',' ( (lv_expressions_5_0= ruleIQLExpression ) )
            	    	    {
            	    	    otherlv_4=(Token)match(input,26,FOLLOW_43); if (state.failed) return current;
            	    	    if ( state.backtracking==0 ) {

            	    	      							newLeafNode(otherlv_4, grammarAccess.getIQLMemberCallExpressionAccess().getCommaKeyword_1_0_0_3_0());
            	    	      						
            	    	    }
            	    	    // InternalODL.g:5966:7: ( (lv_expressions_5_0= ruleIQLExpression ) )
            	    	    // InternalODL.g:5967:8: (lv_expressions_5_0= ruleIQLExpression )
            	    	    {
            	    	    // InternalODL.g:5967:8: (lv_expressions_5_0= ruleIQLExpression )
            	    	    // InternalODL.g:5968:9: lv_expressions_5_0= ruleIQLExpression
            	    	    {
            	    	    if ( state.backtracking==0 ) {

            	    	      									newCompositeNode(grammarAccess.getIQLMemberCallExpressionAccess().getExpressionsIQLExpressionParserRuleCall_1_0_0_3_1_0());
            	    	      								
            	    	    }
            	    	    pushFollow(FOLLOW_38);
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
            	    	    break loop85;
            	        }
            	    } while (true);

            	    otherlv_6=(Token)match(input,65,FOLLOW_63); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						newLeafNode(otherlv_6, grammarAccess.getIQLMemberCallExpressionAccess().getRightSquareBracketKeyword_1_0_0_4());
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;
            	case 2 :
            	    // InternalODL.g:5993:4: ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) )
            	    {
            	    // InternalODL.g:5993:4: ( ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) ) )
            	    // InternalODL.g:5994:5: ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) ) ( (lv_sel_9_0= ruleIQLMemberSelection ) )
            	    {
            	    // InternalODL.g:5994:5: ( ( ( () '.' ) )=> ( () otherlv_8= '.' ) )
            	    // InternalODL.g:5995:6: ( ( () '.' ) )=> ( () otherlv_8= '.' )
            	    {
            	    // InternalODL.g:6001:6: ( () otherlv_8= '.' )
            	    // InternalODL.g:6002:7: () otherlv_8= '.'
            	    {
            	    // InternalODL.g:6002:7: ()
            	    // InternalODL.g:6003:8: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      								current = forceCreateModelElementAndSet(
            	      									grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0(),
            	      									current);
            	      							
            	    }

            	    }

            	    otherlv_8=(Token)match(input,66,FOLLOW_6); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      							newLeafNode(otherlv_8, grammarAccess.getIQLMemberCallExpressionAccess().getFullStopKeyword_1_1_0_0_1());
            	      						
            	    }

            	    }


            	    }

            	    // InternalODL.g:6015:5: ( (lv_sel_9_0= ruleIQLMemberSelection ) )
            	    // InternalODL.g:6016:6: (lv_sel_9_0= ruleIQLMemberSelection )
            	    {
            	    // InternalODL.g:6016:6: (lv_sel_9_0= ruleIQLMemberSelection )
            	    // InternalODL.g:6017:7: lv_sel_9_0= ruleIQLMemberSelection
            	    {
            	    if ( state.backtracking==0 ) {

            	      							newCompositeNode(grammarAccess.getIQLMemberCallExpressionAccess().getSelIQLMemberSelectionParserRuleCall_1_1_1_0());
            	      						
            	    }
            	    pushFollow(FOLLOW_63);
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
            	    break loop86;
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
    // InternalODL.g:6040:1: entryRuleIQLMemberSelection returns [EObject current=null] : iv_ruleIQLMemberSelection= ruleIQLMemberSelection EOF ;
    public final EObject entryRuleIQLMemberSelection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLMemberSelection = null;


        try {
            // InternalODL.g:6040:59: (iv_ruleIQLMemberSelection= ruleIQLMemberSelection EOF )
            // InternalODL.g:6041:2: iv_ruleIQLMemberSelection= ruleIQLMemberSelection EOF
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
    // InternalODL.g:6047:1: ruleIQLMemberSelection returns [EObject current=null] : ( ( (otherlv_0= RULE_ID ) ) ( (lv_args_1_0= ruleIQLArgumentsList ) )? ) ;
    public final EObject ruleIQLMemberSelection() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        EObject lv_args_1_0 = null;



        	enterRule();

        try {
            // InternalODL.g:6053:2: ( ( ( (otherlv_0= RULE_ID ) ) ( (lv_args_1_0= ruleIQLArgumentsList ) )? ) )
            // InternalODL.g:6054:2: ( ( (otherlv_0= RULE_ID ) ) ( (lv_args_1_0= ruleIQLArgumentsList ) )? )
            {
            // InternalODL.g:6054:2: ( ( (otherlv_0= RULE_ID ) ) ( (lv_args_1_0= ruleIQLArgumentsList ) )? )
            // InternalODL.g:6055:3: ( (otherlv_0= RULE_ID ) ) ( (lv_args_1_0= ruleIQLArgumentsList ) )?
            {
            // InternalODL.g:6055:3: ( (otherlv_0= RULE_ID ) )
            // InternalODL.g:6056:4: (otherlv_0= RULE_ID )
            {
            // InternalODL.g:6056:4: (otherlv_0= RULE_ID )
            // InternalODL.g:6057:5: otherlv_0= RULE_ID
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getIQLMemberSelectionRule());
              					}
              				
            }
            otherlv_0=(Token)match(input,RULE_ID,FOLLOW_64); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(otherlv_0, grammarAccess.getIQLMemberSelectionAccess().getMemberJvmMemberCrossReference_0_0());
              				
            }

            }


            }

            // InternalODL.g:6068:3: ( (lv_args_1_0= ruleIQLArgumentsList ) )?
            int alt87=2;
            int LA87_0 = input.LA(1);

            if ( (LA87_0==14) ) {
                alt87=1;
            }
            switch (alt87) {
                case 1 :
                    // InternalODL.g:6069:4: (lv_args_1_0= ruleIQLArgumentsList )
                    {
                    // InternalODL.g:6069:4: (lv_args_1_0= ruleIQLArgumentsList )
                    // InternalODL.g:6070:5: lv_args_1_0= ruleIQLArgumentsList
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
    // InternalODL.g:6091:1: entryRuleIQLOtherExpressions returns [EObject current=null] : iv_ruleIQLOtherExpressions= ruleIQLOtherExpressions EOF ;
    public final EObject entryRuleIQLOtherExpressions() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLOtherExpressions = null;


        try {
            // InternalODL.g:6091:60: (iv_ruleIQLOtherExpressions= ruleIQLOtherExpressions EOF )
            // InternalODL.g:6092:2: iv_ruleIQLOtherExpressions= ruleIQLOtherExpressions EOF
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
    // InternalODL.g:6098:1: ruleIQLOtherExpressions returns [EObject current=null] : ( ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? ) | ( () otherlv_4= 'this' ) | ( () otherlv_6= 'super' ) | ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ) | ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) ) | this_IQLLiteralExpression_18= ruleIQLLiteralExpression ) ;
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
            // InternalODL.g:6104:2: ( ( ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? ) | ( () otherlv_4= 'this' ) | ( () otherlv_6= 'super' ) | ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ) | ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) ) | this_IQLLiteralExpression_18= ruleIQLLiteralExpression ) )
            // InternalODL.g:6105:2: ( ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? ) | ( () otherlv_4= 'this' ) | ( () otherlv_6= 'super' ) | ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ) | ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) ) | this_IQLLiteralExpression_18= ruleIQLLiteralExpression )
            {
            // InternalODL.g:6105:2: ( ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? ) | ( () otherlv_4= 'this' ) | ( () otherlv_6= 'super' ) | ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' ) | ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) ) | this_IQLLiteralExpression_18= ruleIQLLiteralExpression )
            int alt92=6;
            switch ( input.LA(1) ) {
            case RULE_ID:
                {
                alt92=1;
                }
                break;
            case 82:
                {
                alt92=2;
                }
                break;
            case 83:
                {
                alt92=3;
                }
                break;
            case 14:
                {
                alt92=4;
                }
                break;
            case 88:
                {
                alt92=5;
                }
                break;
            case RULE_DOUBLE:
            case RULE_STRING:
            case RULE_INT:
            case RULE_RANGE:
            case 64:
            case 67:
            case 89:
            case 115:
            case 116:
                {
                alt92=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 92, 0, input);

                throw nvae;
            }

            switch (alt92) {
                case 1 :
                    // InternalODL.g:6106:3: ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? )
                    {
                    // InternalODL.g:6106:3: ( () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )? )
                    // InternalODL.g:6107:4: () ( ( ruleQualifiedName ) ) ( (lv_args_2_0= ruleIQLArgumentsList ) )?
                    {
                    // InternalODL.g:6107:4: ()
                    // InternalODL.g:6108:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLOtherExpressionsAccess().getIQLJvmElementCallExpressionAction_0_0(),
                      						current);
                      				
                    }

                    }

                    // InternalODL.g:6114:4: ( ( ruleQualifiedName ) )
                    // InternalODL.g:6115:5: ( ruleQualifiedName )
                    {
                    // InternalODL.g:6115:5: ( ruleQualifiedName )
                    // InternalODL.g:6116:6: ruleQualifiedName
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getIQLOtherExpressionsRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLOtherExpressionsAccess().getElementJvmIdentifiableElementCrossReference_0_1_0());
                      					
                    }
                    pushFollow(FOLLOW_64);
                    ruleQualifiedName();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalODL.g:6130:4: ( (lv_args_2_0= ruleIQLArgumentsList ) )?
                    int alt88=2;
                    int LA88_0 = input.LA(1);

                    if ( (LA88_0==14) ) {
                        alt88=1;
                    }
                    switch (alt88) {
                        case 1 :
                            // InternalODL.g:6131:5: (lv_args_2_0= ruleIQLArgumentsList )
                            {
                            // InternalODL.g:6131:5: (lv_args_2_0= ruleIQLArgumentsList )
                            // InternalODL.g:6132:6: lv_args_2_0= ruleIQLArgumentsList
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
                    // InternalODL.g:6151:3: ( () otherlv_4= 'this' )
                    {
                    // InternalODL.g:6151:3: ( () otherlv_4= 'this' )
                    // InternalODL.g:6152:4: () otherlv_4= 'this'
                    {
                    // InternalODL.g:6152:4: ()
                    // InternalODL.g:6153:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLOtherExpressionsAccess().getIQLThisExpressionAction_1_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_4=(Token)match(input,82,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_4, grammarAccess.getIQLOtherExpressionsAccess().getThisKeyword_1_1());
                      			
                    }

                    }


                    }
                    break;
                case 3 :
                    // InternalODL.g:6165:3: ( () otherlv_6= 'super' )
                    {
                    // InternalODL.g:6165:3: ( () otherlv_6= 'super' )
                    // InternalODL.g:6166:4: () otherlv_6= 'super'
                    {
                    // InternalODL.g:6166:4: ()
                    // InternalODL.g:6167:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLOtherExpressionsAccess().getIQLSuperExpressionAction_2_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_6=(Token)match(input,83,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_6, grammarAccess.getIQLOtherExpressionsAccess().getSuperKeyword_2_1());
                      			
                    }

                    }


                    }
                    break;
                case 4 :
                    // InternalODL.g:6179:3: ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' )
                    {
                    // InternalODL.g:6179:3: ( () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')' )
                    // InternalODL.g:6180:4: () otherlv_8= '(' ( (lv_expr_9_0= ruleIQLExpression ) ) otherlv_10= ')'
                    {
                    // InternalODL.g:6180:4: ()
                    // InternalODL.g:6181:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLOtherExpressionsAccess().getIQLParenthesisExpressionAction_3_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_8=(Token)match(input,14,FOLLOW_43); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_8, grammarAccess.getIQLOtherExpressionsAccess().getLeftParenthesisKeyword_3_1());
                      			
                    }
                    // InternalODL.g:6191:4: ( (lv_expr_9_0= ruleIQLExpression ) )
                    // InternalODL.g:6192:5: (lv_expr_9_0= ruleIQLExpression )
                    {
                    // InternalODL.g:6192:5: (lv_expr_9_0= ruleIQLExpression )
                    // InternalODL.g:6193:6: lv_expr_9_0= ruleIQLExpression
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
                    // InternalODL.g:6216:3: ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) )
                    {
                    // InternalODL.g:6216:3: ( () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) ) )
                    // InternalODL.g:6217:4: () otherlv_12= 'new' ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) )
                    {
                    // InternalODL.g:6217:4: ()
                    // InternalODL.g:6218:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLOtherExpressionsAccess().getIQLNewExpressionAction_4_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_12=(Token)match(input,88,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_12, grammarAccess.getIQLOtherExpressionsAccess().getNewKeyword_4_1());
                      			
                    }
                    // InternalODL.g:6228:4: ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) )
                    int alt91=2;
                    alt91 = dfa91.predict(input);
                    switch (alt91) {
                        case 1 :
                            // InternalODL.g:6229:5: ( (lv_ref_13_0= ruleIQLArrayTypeRef ) )
                            {
                            // InternalODL.g:6229:5: ( (lv_ref_13_0= ruleIQLArrayTypeRef ) )
                            // InternalODL.g:6230:6: (lv_ref_13_0= ruleIQLArrayTypeRef )
                            {
                            // InternalODL.g:6230:6: (lv_ref_13_0= ruleIQLArrayTypeRef )
                            // InternalODL.g:6231:7: lv_ref_13_0= ruleIQLArrayTypeRef
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
                            // InternalODL.g:6249:5: ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) )
                            {
                            // InternalODL.g:6249:5: ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) )
                            // InternalODL.g:6250:6: ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) )
                            {
                            // InternalODL.g:6250:6: ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) )
                            // InternalODL.g:6251:7: (lv_ref_14_0= ruleIQLSimpleTypeRef )
                            {
                            // InternalODL.g:6251:7: (lv_ref_14_0= ruleIQLSimpleTypeRef )
                            // InternalODL.g:6252:8: lv_ref_14_0= ruleIQLSimpleTypeRef
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

                            // InternalODL.g:6269:6: ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) )
                            int alt90=2;
                            int LA90_0 = input.LA(1);

                            if ( (LA90_0==14) ) {
                                alt90=1;
                            }
                            else if ( (LA90_0==16) ) {
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
                                    // InternalODL.g:6270:7: ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? )
                                    {
                                    // InternalODL.g:6270:7: ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? )
                                    // InternalODL.g:6271:8: ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )?
                                    {
                                    // InternalODL.g:6271:8: ( (lv_argsList_15_0= ruleIQLArgumentsList ) )
                                    // InternalODL.g:6272:9: (lv_argsList_15_0= ruleIQLArgumentsList )
                                    {
                                    // InternalODL.g:6272:9: (lv_argsList_15_0= ruleIQLArgumentsList )
                                    // InternalODL.g:6273:10: lv_argsList_15_0= ruleIQLArgumentsList
                                    {
                                    if ( state.backtracking==0 ) {

                                      										newCompositeNode(grammarAccess.getIQLOtherExpressionsAccess().getArgsListIQLArgumentsListParserRuleCall_4_2_1_1_0_0_0());
                                      									
                                    }
                                    pushFollow(FOLLOW_42);
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

                                    // InternalODL.g:6290:8: ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )?
                                    int alt89=2;
                                    int LA89_0 = input.LA(1);

                                    if ( (LA89_0==16) ) {
                                        alt89=1;
                                    }
                                    switch (alt89) {
                                        case 1 :
                                            // InternalODL.g:6291:9: (lv_argsMap_16_0= ruleIQLArgumentsMap )
                                            {
                                            // InternalODL.g:6291:9: (lv_argsMap_16_0= ruleIQLArgumentsMap )
                                            // InternalODL.g:6292:10: lv_argsMap_16_0= ruleIQLArgumentsMap
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
                                    // InternalODL.g:6311:7: ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) )
                                    {
                                    // InternalODL.g:6311:7: ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) )
                                    // InternalODL.g:6312:8: (lv_argsMap_17_0= ruleIQLArgumentsMap )
                                    {
                                    // InternalODL.g:6312:8: (lv_argsMap_17_0= ruleIQLArgumentsMap )
                                    // InternalODL.g:6313:9: lv_argsMap_17_0= ruleIQLArgumentsMap
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
                    // InternalODL.g:6335:3: this_IQLLiteralExpression_18= ruleIQLLiteralExpression
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
    // InternalODL.g:6347:1: entryRuleIQLLiteralExpression returns [EObject current=null] : iv_ruleIQLLiteralExpression= ruleIQLLiteralExpression EOF ;
    public final EObject entryRuleIQLLiteralExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLiteralExpression = null;


        try {
            // InternalODL.g:6347:61: (iv_ruleIQLLiteralExpression= ruleIQLLiteralExpression EOF )
            // InternalODL.g:6348:2: iv_ruleIQLLiteralExpression= ruleIQLLiteralExpression EOF
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
    // InternalODL.g:6354:1: ruleIQLLiteralExpression returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_RANGE ) ) ) | ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' ) | ( () otherlv_15= 'null' ) | ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList ) | this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap ) ;
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
            // InternalODL.g:6360:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_RANGE ) ) ) | ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' ) | ( () otherlv_15= 'null' ) | ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList ) | this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap ) )
            // InternalODL.g:6361:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_RANGE ) ) ) | ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' ) | ( () otherlv_15= 'null' ) | ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList ) | this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap )
            {
            // InternalODL.g:6361:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_RANGE ) ) ) | ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' ) | ( () otherlv_15= 'null' ) | ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList ) | this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap )
            int alt93=9;
            alt93 = dfa93.predict(input);
            switch (alt93) {
                case 1 :
                    // InternalODL.g:6362:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalODL.g:6362:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalODL.g:6363:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalODL.g:6363:4: ()
                    // InternalODL.g:6364:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionIntAction_0_0(),
                      						current);
                      				
                    }

                    }

                    // InternalODL.g:6370:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalODL.g:6371:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalODL.g:6371:5: (lv_value_1_0= RULE_INT )
                    // InternalODL.g:6372:6: lv_value_1_0= RULE_INT
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
                    // InternalODL.g:6390:3: ( () ( (lv_value_3_0= RULE_DOUBLE ) ) )
                    {
                    // InternalODL.g:6390:3: ( () ( (lv_value_3_0= RULE_DOUBLE ) ) )
                    // InternalODL.g:6391:4: () ( (lv_value_3_0= RULE_DOUBLE ) )
                    {
                    // InternalODL.g:6391:4: ()
                    // InternalODL.g:6392:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionDoubleAction_1_0(),
                      						current);
                      				
                    }

                    }

                    // InternalODL.g:6398:4: ( (lv_value_3_0= RULE_DOUBLE ) )
                    // InternalODL.g:6399:5: (lv_value_3_0= RULE_DOUBLE )
                    {
                    // InternalODL.g:6399:5: (lv_value_3_0= RULE_DOUBLE )
                    // InternalODL.g:6400:6: lv_value_3_0= RULE_DOUBLE
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
                    // InternalODL.g:6418:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalODL.g:6418:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalODL.g:6419:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalODL.g:6419:4: ()
                    // InternalODL.g:6420:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionStringAction_2_0(),
                      						current);
                      				
                    }

                    }

                    // InternalODL.g:6426:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalODL.g:6427:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalODL.g:6427:5: (lv_value_5_0= RULE_STRING )
                    // InternalODL.g:6428:6: lv_value_5_0= RULE_STRING
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
                    // InternalODL.g:6446:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    {
                    // InternalODL.g:6446:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    // InternalODL.g:6447:4: () ( (lv_value_7_0= ruleBOOLEAN ) )
                    {
                    // InternalODL.g:6447:4: ()
                    // InternalODL.g:6448:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionBooleanAction_3_0(),
                      						current);
                      				
                    }

                    }

                    // InternalODL.g:6454:4: ( (lv_value_7_0= ruleBOOLEAN ) )
                    // InternalODL.g:6455:5: (lv_value_7_0= ruleBOOLEAN )
                    {
                    // InternalODL.g:6455:5: (lv_value_7_0= ruleBOOLEAN )
                    // InternalODL.g:6456:6: lv_value_7_0= ruleBOOLEAN
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
                    // InternalODL.g:6475:3: ( () ( (lv_value_9_0= RULE_RANGE ) ) )
                    {
                    // InternalODL.g:6475:3: ( () ( (lv_value_9_0= RULE_RANGE ) ) )
                    // InternalODL.g:6476:4: () ( (lv_value_9_0= RULE_RANGE ) )
                    {
                    // InternalODL.g:6476:4: ()
                    // InternalODL.g:6477:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionRangeAction_4_0(),
                      						current);
                      				
                    }

                    }

                    // InternalODL.g:6483:4: ( (lv_value_9_0= RULE_RANGE ) )
                    // InternalODL.g:6484:5: (lv_value_9_0= RULE_RANGE )
                    {
                    // InternalODL.g:6484:5: (lv_value_9_0= RULE_RANGE )
                    // InternalODL.g:6485:6: lv_value_9_0= RULE_RANGE
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
                    // InternalODL.g:6503:3: ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' )
                    {
                    // InternalODL.g:6503:3: ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' )
                    // InternalODL.g:6504:4: () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')'
                    {
                    // InternalODL.g:6504:4: ()
                    // InternalODL.g:6505:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionTypeAction_5_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_11=(Token)match(input,89,FOLLOW_6); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_11, grammarAccess.getIQLLiteralExpressionAccess().getClassKeyword_5_1());
                      			
                    }
                    // InternalODL.g:6515:4: ( (lv_value_12_0= ruleJvmTypeReference ) )
                    // InternalODL.g:6516:5: (lv_value_12_0= ruleJvmTypeReference )
                    {
                    // InternalODL.g:6516:5: (lv_value_12_0= ruleJvmTypeReference )
                    // InternalODL.g:6517:6: lv_value_12_0= ruleJvmTypeReference
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
                    // InternalODL.g:6540:3: ( () otherlv_15= 'null' )
                    {
                    // InternalODL.g:6540:3: ( () otherlv_15= 'null' )
                    // InternalODL.g:6541:4: () otherlv_15= 'null'
                    {
                    // InternalODL.g:6541:4: ()
                    // InternalODL.g:6542:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getIQLLiteralExpressionAccess().getIQLLiteralExpressionNullAction_6_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_15=(Token)match(input,67,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_15, grammarAccess.getIQLLiteralExpressionAccess().getNullKeyword_6_1());
                      			
                    }

                    }


                    }
                    break;
                case 8 :
                    // InternalODL.g:6554:3: ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList )
                    {
                    // InternalODL.g:6554:3: ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList )
                    // InternalODL.g:6555:4: ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList
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
                    // InternalODL.g:6566:3: this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap
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
    // InternalODL.g:6578:1: entryRuleIQLLiteralExpressionList returns [EObject current=null] : iv_ruleIQLLiteralExpressionList= ruleIQLLiteralExpressionList EOF ;
    public final EObject entryRuleIQLLiteralExpressionList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLiteralExpressionList = null;


        try {
            // InternalODL.g:6578:65: (iv_ruleIQLLiteralExpressionList= ruleIQLLiteralExpressionList EOF )
            // InternalODL.g:6579:2: iv_ruleIQLLiteralExpressionList= ruleIQLLiteralExpressionList EOF
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
    // InternalODL.g:6585:1: ruleIQLLiteralExpressionList returns [EObject current=null] : ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ']' ) ;
    public final EObject ruleIQLLiteralExpressionList() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalODL.g:6591:2: ( ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ']' ) )
            // InternalODL.g:6592:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ']' )
            {
            // InternalODL.g:6592:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ']' )
            // InternalODL.g:6593:3: () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )? otherlv_5= ']'
            {
            // InternalODL.g:6593:3: ()
            // InternalODL.g:6594:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLLiteralExpressionListAccess().getIQLLiteralExpressionListAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,64,FOLLOW_65); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLLiteralExpressionListAccess().getLeftSquareBracketKeyword_1());
              		
            }
            // InternalODL.g:6604:3: ( ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )* )?
            int alt95=2;
            int LA95_0 = input.LA(1);

            if ( (LA95_0==RULE_ID||(LA95_0>=RULE_DOUBLE && LA95_0<=RULE_INT)||LA95_0==RULE_RANGE||LA95_0==14||LA95_0==28||LA95_0==30||(LA95_0>=38 && LA95_0<=39)||LA95_0==44||LA95_0==64||LA95_0==67||(LA95_0>=82 && LA95_0<=83)||(LA95_0>=88 && LA95_0<=89)||(LA95_0>=115 && LA95_0<=116)) ) {
                alt95=1;
            }
            switch (alt95) {
                case 1 :
                    // InternalODL.g:6605:4: ( (lv_elements_2_0= ruleIQLExpression ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )*
                    {
                    // InternalODL.g:6605:4: ( (lv_elements_2_0= ruleIQLExpression ) )
                    // InternalODL.g:6606:5: (lv_elements_2_0= ruleIQLExpression )
                    {
                    // InternalODL.g:6606:5: (lv_elements_2_0= ruleIQLExpression )
                    // InternalODL.g:6607:6: lv_elements_2_0= ruleIQLExpression
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLLiteralExpressionListAccess().getElementsIQLExpressionParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_38);
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

                    // InternalODL.g:6624:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) ) )*
                    loop94:
                    do {
                        int alt94=2;
                        int LA94_0 = input.LA(1);

                        if ( (LA94_0==26) ) {
                            alt94=1;
                        }


                        switch (alt94) {
                    	case 1 :
                    	    // InternalODL.g:6625:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLExpression ) )
                    	    {
                    	    otherlv_3=(Token)match(input,26,FOLLOW_43); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLLiteralExpressionListAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalODL.g:6629:5: ( (lv_elements_4_0= ruleIQLExpression ) )
                    	    // InternalODL.g:6630:6: (lv_elements_4_0= ruleIQLExpression )
                    	    {
                    	    // InternalODL.g:6630:6: (lv_elements_4_0= ruleIQLExpression )
                    	    // InternalODL.g:6631:7: lv_elements_4_0= ruleIQLExpression
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLLiteralExpressionListAccess().getElementsIQLExpressionParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_38);
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
                    	    break loop94;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,65,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:6658:1: entryRuleIQLLiteralExpressionMap returns [EObject current=null] : iv_ruleIQLLiteralExpressionMap= ruleIQLLiteralExpressionMap EOF ;
    public final EObject entryRuleIQLLiteralExpressionMap() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLiteralExpressionMap = null;


        try {
            // InternalODL.g:6658:64: (iv_ruleIQLLiteralExpressionMap= ruleIQLLiteralExpressionMap EOF )
            // InternalODL.g:6659:2: iv_ruleIQLLiteralExpressionMap= ruleIQLLiteralExpressionMap EOF
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
    // InternalODL.g:6665:1: ruleIQLLiteralExpressionMap returns [EObject current=null] : ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )? otherlv_5= ']' ) ;
    public final EObject ruleIQLLiteralExpressionMap() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalODL.g:6671:2: ( ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )? otherlv_5= ']' ) )
            // InternalODL.g:6672:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )? otherlv_5= ']' )
            {
            // InternalODL.g:6672:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )? otherlv_5= ']' )
            // InternalODL.g:6673:3: () otherlv_1= '[' ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )? otherlv_5= ']'
            {
            // InternalODL.g:6673:3: ()
            // InternalODL.g:6674:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getIQLLiteralExpressionMapAccess().getIQLLiteralExpressionMapAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,64,FOLLOW_65); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLLiteralExpressionMapAccess().getLeftSquareBracketKeyword_1());
              		
            }
            // InternalODL.g:6684:3: ( ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )* )?
            int alt97=2;
            int LA97_0 = input.LA(1);

            if ( (LA97_0==RULE_ID||(LA97_0>=RULE_DOUBLE && LA97_0<=RULE_INT)||LA97_0==RULE_RANGE||LA97_0==14||LA97_0==28||LA97_0==30||(LA97_0>=38 && LA97_0<=39)||LA97_0==44||LA97_0==64||LA97_0==67||(LA97_0>=82 && LA97_0<=83)||(LA97_0>=88 && LA97_0<=89)||(LA97_0>=115 && LA97_0<=116)) ) {
                alt97=1;
            }
            switch (alt97) {
                case 1 :
                    // InternalODL.g:6685:4: ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )*
                    {
                    // InternalODL.g:6685:4: ( (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue ) )
                    // InternalODL.g:6686:5: (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue )
                    {
                    // InternalODL.g:6686:5: (lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue )
                    // InternalODL.g:6687:6: lv_elements_2_0= ruleIQLLiteralExpressionMapKeyValue
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getIQLLiteralExpressionMapAccess().getElementsIQLLiteralExpressionMapKeyValueParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_38);
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

                    // InternalODL.g:6704:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) ) )*
                    loop96:
                    do {
                        int alt96=2;
                        int LA96_0 = input.LA(1);

                        if ( (LA96_0==26) ) {
                            alt96=1;
                        }


                        switch (alt96) {
                    	case 1 :
                    	    // InternalODL.g:6705:5: otherlv_3= ',' ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) )
                    	    {
                    	    otherlv_3=(Token)match(input,26,FOLLOW_43); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getIQLLiteralExpressionMapAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalODL.g:6709:5: ( (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue ) )
                    	    // InternalODL.g:6710:6: (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue )
                    	    {
                    	    // InternalODL.g:6710:6: (lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue )
                    	    // InternalODL.g:6711:7: lv_elements_4_0= ruleIQLLiteralExpressionMapKeyValue
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getIQLLiteralExpressionMapAccess().getElementsIQLLiteralExpressionMapKeyValueParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_38);
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
                    	    break loop96;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,65,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:6738:1: entryRuleIQLLiteralExpressionMapKeyValue returns [EObject current=null] : iv_ruleIQLLiteralExpressionMapKeyValue= ruleIQLLiteralExpressionMapKeyValue EOF ;
    public final EObject entryRuleIQLLiteralExpressionMapKeyValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLLiteralExpressionMapKeyValue = null;


        try {
            // InternalODL.g:6738:72: (iv_ruleIQLLiteralExpressionMapKeyValue= ruleIQLLiteralExpressionMapKeyValue EOF )
            // InternalODL.g:6739:2: iv_ruleIQLLiteralExpressionMapKeyValue= ruleIQLLiteralExpressionMapKeyValue EOF
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
    // InternalODL.g:6745:1: ruleIQLLiteralExpressionMapKeyValue returns [EObject current=null] : ( ( (lv_key_0_0= ruleIQLExpression ) ) otherlv_1= ':' ( (lv_value_2_0= ruleIQLExpression ) ) ) ;
    public final EObject ruleIQLLiteralExpressionMapKeyValue() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_key_0_0 = null;

        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalODL.g:6751:2: ( ( ( (lv_key_0_0= ruleIQLExpression ) ) otherlv_1= ':' ( (lv_value_2_0= ruleIQLExpression ) ) ) )
            // InternalODL.g:6752:2: ( ( (lv_key_0_0= ruleIQLExpression ) ) otherlv_1= ':' ( (lv_value_2_0= ruleIQLExpression ) ) )
            {
            // InternalODL.g:6752:2: ( ( (lv_key_0_0= ruleIQLExpression ) ) otherlv_1= ':' ( (lv_value_2_0= ruleIQLExpression ) ) )
            // InternalODL.g:6753:3: ( (lv_key_0_0= ruleIQLExpression ) ) otherlv_1= ':' ( (lv_value_2_0= ruleIQLExpression ) )
            {
            // InternalODL.g:6753:3: ( (lv_key_0_0= ruleIQLExpression ) )
            // InternalODL.g:6754:4: (lv_key_0_0= ruleIQLExpression )
            {
            // InternalODL.g:6754:4: (lv_key_0_0= ruleIQLExpression )
            // InternalODL.g:6755:5: lv_key_0_0= ruleIQLExpression
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLLiteralExpressionMapKeyValueAccess().getKeyIQLExpressionParserRuleCall_0_0());
              				
            }
            pushFollow(FOLLOW_50);
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

            otherlv_1=(Token)match(input,27,FOLLOW_43); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getIQLLiteralExpressionMapKeyValueAccess().getColonKeyword_1());
              		
            }
            // InternalODL.g:6776:3: ( (lv_value_2_0= ruleIQLExpression ) )
            // InternalODL.g:6777:4: (lv_value_2_0= ruleIQLExpression )
            {
            // InternalODL.g:6777:4: (lv_value_2_0= ruleIQLExpression )
            // InternalODL.g:6778:5: lv_value_2_0= ruleIQLExpression
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
    // InternalODL.g:6799:1: entryRuleQualifiedNameWithWildcard returns [String current=null] : iv_ruleQualifiedNameWithWildcard= ruleQualifiedNameWithWildcard EOF ;
    public final String entryRuleQualifiedNameWithWildcard() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedNameWithWildcard = null;


        try {
            // InternalODL.g:6799:65: (iv_ruleQualifiedNameWithWildcard= ruleQualifiedNameWithWildcard EOF )
            // InternalODL.g:6800:2: iv_ruleQualifiedNameWithWildcard= ruleQualifiedNameWithWildcard EOF
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
    // InternalODL.g:6806:1: ruleQualifiedNameWithWildcard returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_QualifiedName_0= ruleQualifiedName (kw= '::*' )? ) ;
    public final AntlrDatatypeRuleToken ruleQualifiedNameWithWildcard() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        AntlrDatatypeRuleToken this_QualifiedName_0 = null;



        	enterRule();

        try {
            // InternalODL.g:6812:2: ( (this_QualifiedName_0= ruleQualifiedName (kw= '::*' )? ) )
            // InternalODL.g:6813:2: (this_QualifiedName_0= ruleQualifiedName (kw= '::*' )? )
            {
            // InternalODL.g:6813:2: (this_QualifiedName_0= ruleQualifiedName (kw= '::*' )? )
            // InternalODL.g:6814:3: this_QualifiedName_0= ruleQualifiedName (kw= '::*' )?
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getQualifiedNameWithWildcardAccess().getQualifiedNameParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_66);
            this_QualifiedName_0=ruleQualifiedName();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current.merge(this_QualifiedName_0);
              		
            }
            if ( state.backtracking==0 ) {

              			afterParserOrEnumRuleCall();
              		
            }
            // InternalODL.g:6824:3: (kw= '::*' )?
            int alt98=2;
            int LA98_0 = input.LA(1);

            if ( (LA98_0==90) ) {
                alt98=1;
            }
            switch (alt98) {
                case 1 :
                    // InternalODL.g:6825:4: kw= '::*'
                    {
                    kw=(Token)match(input,90,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:6835:1: entryRuleQualifiedName returns [String current=null] : iv_ruleQualifiedName= ruleQualifiedName EOF ;
    public final String entryRuleQualifiedName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedName = null;


        try {
            // InternalODL.g:6835:53: (iv_ruleQualifiedName= ruleQualifiedName EOF )
            // InternalODL.g:6836:2: iv_ruleQualifiedName= ruleQualifiedName EOF
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
    // InternalODL.g:6842:1: ruleQualifiedName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID (kw= '::' this_ID_2= RULE_ID )* ) ;
    public final AntlrDatatypeRuleToken ruleQualifiedName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_2=null;


        	enterRule();

        try {
            // InternalODL.g:6848:2: ( (this_ID_0= RULE_ID (kw= '::' this_ID_2= RULE_ID )* ) )
            // InternalODL.g:6849:2: (this_ID_0= RULE_ID (kw= '::' this_ID_2= RULE_ID )* )
            {
            // InternalODL.g:6849:2: (this_ID_0= RULE_ID (kw= '::' this_ID_2= RULE_ID )* )
            // InternalODL.g:6850:3: this_ID_0= RULE_ID (kw= '::' this_ID_2= RULE_ID )*
            {
            this_ID_0=(Token)match(input,RULE_ID,FOLLOW_67); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current.merge(this_ID_0);
              		
            }
            if ( state.backtracking==0 ) {

              			newLeafNode(this_ID_0, grammarAccess.getQualifiedNameAccess().getIDTerminalRuleCall_0());
              		
            }
            // InternalODL.g:6857:3: (kw= '::' this_ID_2= RULE_ID )*
            loop99:
            do {
                int alt99=2;
                int LA99_0 = input.LA(1);

                if ( (LA99_0==91) ) {
                    alt99=1;
                }


                switch (alt99) {
            	case 1 :
            	    // InternalODL.g:6858:4: kw= '::' this_ID_2= RULE_ID
            	    {
            	    kw=(Token)match(input,91,FOLLOW_6); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				current.merge(kw);
            	      				newLeafNode(kw, grammarAccess.getQualifiedNameAccess().getColonColonKeyword_1_0());
            	      			
            	    }
            	    this_ID_2=(Token)match(input,RULE_ID,FOLLOW_67); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				current.merge(this_ID_2);
            	      			
            	    }
            	    if ( state.backtracking==0 ) {

            	      				newLeafNode(this_ID_2, grammarAccess.getQualifiedNameAccess().getIDTerminalRuleCall_1_1());
            	      			
            	    }

            	    }
            	    break;

            	default :
            	    break loop99;
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
    // InternalODL.g:6875:1: entryRuleIQLJava returns [EObject current=null] : iv_ruleIQLJava= ruleIQLJava EOF ;
    public final EObject entryRuleIQLJava() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIQLJava = null;



        	HiddenTokens myHiddenTokenState = ((XtextTokenStream)input).setHiddenTokens("RULE_SL_COMMENT", "RULE_ML_COMMENT");

        try {
            // InternalODL.g:6877:2: (iv_ruleIQLJava= ruleIQLJava EOF )
            // InternalODL.g:6878:2: iv_ruleIQLJava= ruleIQLJava EOF
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
    // InternalODL.g:6887:1: ruleIQLJava returns [EObject current=null] : (otherlv_0= '$*' ( (lv_text_1_0= ruleIQLJavaText ) ) otherlv_2= '*$' ) ;
    public final EObject ruleIQLJava() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        AntlrDatatypeRuleToken lv_text_1_0 = null;



        	enterRule();
        	HiddenTokens myHiddenTokenState = ((XtextTokenStream)input).setHiddenTokens("RULE_SL_COMMENT", "RULE_ML_COMMENT");

        try {
            // InternalODL.g:6894:2: ( (otherlv_0= '$*' ( (lv_text_1_0= ruleIQLJavaText ) ) otherlv_2= '*$' ) )
            // InternalODL.g:6895:2: (otherlv_0= '$*' ( (lv_text_1_0= ruleIQLJavaText ) ) otherlv_2= '*$' )
            {
            // InternalODL.g:6895:2: (otherlv_0= '$*' ( (lv_text_1_0= ruleIQLJavaText ) ) otherlv_2= '*$' )
            // InternalODL.g:6896:3: otherlv_0= '$*' ( (lv_text_1_0= ruleIQLJavaText ) ) otherlv_2= '*$'
            {
            otherlv_0=(Token)match(input,92,FOLLOW_68); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_0, grammarAccess.getIQLJavaAccess().getDollarSignAsteriskKeyword_0());
              		
            }
            // InternalODL.g:6900:3: ( (lv_text_1_0= ruleIQLJavaText ) )
            // InternalODL.g:6901:4: (lv_text_1_0= ruleIQLJavaText )
            {
            // InternalODL.g:6901:4: (lv_text_1_0= ruleIQLJavaText )
            // InternalODL.g:6902:5: lv_text_1_0= ruleIQLJavaText
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getIQLJavaAccess().getTextIQLJavaTextParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_69);
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
              						"de.uniol.inf.is.odysseus.iql.odl.ODL.IQLJavaText");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_2=(Token)match(input,93,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:6930:1: entryRuleIQL_JAVA_KEYWORDS returns [String current=null] : iv_ruleIQL_JAVA_KEYWORDS= ruleIQL_JAVA_KEYWORDS EOF ;
    public final String entryRuleIQL_JAVA_KEYWORDS() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleIQL_JAVA_KEYWORDS = null;


        try {
            // InternalODL.g:6930:57: (iv_ruleIQL_JAVA_KEYWORDS= ruleIQL_JAVA_KEYWORDS EOF )
            // InternalODL.g:6931:2: iv_ruleIQL_JAVA_KEYWORDS= ruleIQL_JAVA_KEYWORDS EOF
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
    // InternalODL.g:6937:1: ruleIQL_JAVA_KEYWORDS returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'break' | kw= 'case' | kw= 'class' | kw= 'continue' | kw= 'default' | kw= 'do' | kw= 'else' | kw= 'extends' | kw= 'for' | kw= 'if' | kw= 'implements' | kw= 'instanceof' | kw= 'interface' | kw= 'new' | kw= 'package' | kw= 'return' | kw= 'super' | kw= 'switch' | kw= 'this' | kw= 'while' | kw= 'abstract' | kw= 'assert' | kw= 'catch' | kw= 'const' | kw= 'enum' | kw= 'final' | kw= 'finally' | kw= 'goto' | kw= 'import' | kw= 'native' | kw= 'private' | kw= 'protected' | kw= 'public' | kw= 'static' | kw= 'synchronized' | kw= 'throw' | kw= 'throws' | kw= 'transient' | kw= 'try' | kw= 'volatile' | kw= 'strictfp' ) ;
    public final AntlrDatatypeRuleToken ruleIQL_JAVA_KEYWORDS() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalODL.g:6943:2: ( (kw= 'break' | kw= 'case' | kw= 'class' | kw= 'continue' | kw= 'default' | kw= 'do' | kw= 'else' | kw= 'extends' | kw= 'for' | kw= 'if' | kw= 'implements' | kw= 'instanceof' | kw= 'interface' | kw= 'new' | kw= 'package' | kw= 'return' | kw= 'super' | kw= 'switch' | kw= 'this' | kw= 'while' | kw= 'abstract' | kw= 'assert' | kw= 'catch' | kw= 'const' | kw= 'enum' | kw= 'final' | kw= 'finally' | kw= 'goto' | kw= 'import' | kw= 'native' | kw= 'private' | kw= 'protected' | kw= 'public' | kw= 'static' | kw= 'synchronized' | kw= 'throw' | kw= 'throws' | kw= 'transient' | kw= 'try' | kw= 'volatile' | kw= 'strictfp' ) )
            // InternalODL.g:6944:2: (kw= 'break' | kw= 'case' | kw= 'class' | kw= 'continue' | kw= 'default' | kw= 'do' | kw= 'else' | kw= 'extends' | kw= 'for' | kw= 'if' | kw= 'implements' | kw= 'instanceof' | kw= 'interface' | kw= 'new' | kw= 'package' | kw= 'return' | kw= 'super' | kw= 'switch' | kw= 'this' | kw= 'while' | kw= 'abstract' | kw= 'assert' | kw= 'catch' | kw= 'const' | kw= 'enum' | kw= 'final' | kw= 'finally' | kw= 'goto' | kw= 'import' | kw= 'native' | kw= 'private' | kw= 'protected' | kw= 'public' | kw= 'static' | kw= 'synchronized' | kw= 'throw' | kw= 'throws' | kw= 'transient' | kw= 'try' | kw= 'volatile' | kw= 'strictfp' )
            {
            // InternalODL.g:6944:2: (kw= 'break' | kw= 'case' | kw= 'class' | kw= 'continue' | kw= 'default' | kw= 'do' | kw= 'else' | kw= 'extends' | kw= 'for' | kw= 'if' | kw= 'implements' | kw= 'instanceof' | kw= 'interface' | kw= 'new' | kw= 'package' | kw= 'return' | kw= 'super' | kw= 'switch' | kw= 'this' | kw= 'while' | kw= 'abstract' | kw= 'assert' | kw= 'catch' | kw= 'const' | kw= 'enum' | kw= 'final' | kw= 'finally' | kw= 'goto' | kw= 'import' | kw= 'native' | kw= 'private' | kw= 'protected' | kw= 'public' | kw= 'static' | kw= 'synchronized' | kw= 'throw' | kw= 'throws' | kw= 'transient' | kw= 'try' | kw= 'volatile' | kw= 'strictfp' )
            int alt100=41;
            switch ( input.LA(1) ) {
            case 84:
                {
                alt100=1;
                }
                break;
            case 81:
                {
                alt100=2;
                }
                break;
            case 70:
                {
                alt100=3;
                }
                break;
            case 85:
                {
                alt100=4;
                }
                break;
            case 80:
                {
                alt100=5;
                }
                break;
            case 77:
                {
                alt100=6;
                }
                break;
            case 75:
                {
                alt100=7;
                }
                break;
            case 71:
                {
                alt100=8;
                }
                break;
            case 78:
                {
                alt100=9;
                }
                break;
            case 74:
                {
                alt100=10;
                }
                break;
            case 72:
                {
                alt100=11;
                }
                break;
            case 87:
                {
                alt100=12;
                }
                break;
            case 73:
                {
                alt100=13;
                }
                break;
            case 88:
                {
                alt100=14;
                }
                break;
            case 94:
                {
                alt100=15;
                }
                break;
            case 86:
                {
                alt100=16;
                }
                break;
            case 83:
                {
                alt100=17;
                }
                break;
            case 79:
                {
                alt100=18;
                }
                break;
            case 82:
                {
                alt100=19;
                }
                break;
            case 76:
                {
                alt100=20;
                }
                break;
            case 95:
                {
                alt100=21;
                }
                break;
            case 96:
                {
                alt100=22;
                }
                break;
            case 97:
                {
                alt100=23;
                }
                break;
            case 98:
                {
                alt100=24;
                }
                break;
            case 99:
                {
                alt100=25;
                }
                break;
            case 100:
                {
                alt100=26;
                }
                break;
            case 101:
                {
                alt100=27;
                }
                break;
            case 102:
                {
                alt100=28;
                }
                break;
            case 103:
                {
                alt100=29;
                }
                break;
            case 104:
                {
                alt100=30;
                }
                break;
            case 105:
                {
                alt100=31;
                }
                break;
            case 106:
                {
                alt100=32;
                }
                break;
            case 107:
                {
                alt100=33;
                }
                break;
            case 69:
                {
                alt100=34;
                }
                break;
            case 108:
                {
                alt100=35;
                }
                break;
            case 109:
                {
                alt100=36;
                }
                break;
            case 110:
                {
                alt100=37;
                }
                break;
            case 111:
                {
                alt100=38;
                }
                break;
            case 112:
                {
                alt100=39;
                }
                break;
            case 113:
                {
                alt100=40;
                }
                break;
            case 114:
                {
                alt100=41;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 100, 0, input);

                throw nvae;
            }

            switch (alt100) {
                case 1 :
                    // InternalODL.g:6945:3: kw= 'break'
                    {
                    kw=(Token)match(input,84,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getBreakKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalODL.g:6951:3: kw= 'case'
                    {
                    kw=(Token)match(input,81,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getCaseKeyword_1());
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalODL.g:6957:3: kw= 'class'
                    {
                    kw=(Token)match(input,70,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getClassKeyword_2());
                      		
                    }

                    }
                    break;
                case 4 :
                    // InternalODL.g:6963:3: kw= 'continue'
                    {
                    kw=(Token)match(input,85,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getContinueKeyword_3());
                      		
                    }

                    }
                    break;
                case 5 :
                    // InternalODL.g:6969:3: kw= 'default'
                    {
                    kw=(Token)match(input,80,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getDefaultKeyword_4());
                      		
                    }

                    }
                    break;
                case 6 :
                    // InternalODL.g:6975:3: kw= 'do'
                    {
                    kw=(Token)match(input,77,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getDoKeyword_5());
                      		
                    }

                    }
                    break;
                case 7 :
                    // InternalODL.g:6981:3: kw= 'else'
                    {
                    kw=(Token)match(input,75,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getElseKeyword_6());
                      		
                    }

                    }
                    break;
                case 8 :
                    // InternalODL.g:6987:3: kw= 'extends'
                    {
                    kw=(Token)match(input,71,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getExtendsKeyword_7());
                      		
                    }

                    }
                    break;
                case 9 :
                    // InternalODL.g:6993:3: kw= 'for'
                    {
                    kw=(Token)match(input,78,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getForKeyword_8());
                      		
                    }

                    }
                    break;
                case 10 :
                    // InternalODL.g:6999:3: kw= 'if'
                    {
                    kw=(Token)match(input,74,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getIfKeyword_9());
                      		
                    }

                    }
                    break;
                case 11 :
                    // InternalODL.g:7005:3: kw= 'implements'
                    {
                    kw=(Token)match(input,72,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getImplementsKeyword_10());
                      		
                    }

                    }
                    break;
                case 12 :
                    // InternalODL.g:7011:3: kw= 'instanceof'
                    {
                    kw=(Token)match(input,87,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getInstanceofKeyword_11());
                      		
                    }

                    }
                    break;
                case 13 :
                    // InternalODL.g:7017:3: kw= 'interface'
                    {
                    kw=(Token)match(input,73,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getInterfaceKeyword_12());
                      		
                    }

                    }
                    break;
                case 14 :
                    // InternalODL.g:7023:3: kw= 'new'
                    {
                    kw=(Token)match(input,88,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getNewKeyword_13());
                      		
                    }

                    }
                    break;
                case 15 :
                    // InternalODL.g:7029:3: kw= 'package'
                    {
                    kw=(Token)match(input,94,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getPackageKeyword_14());
                      		
                    }

                    }
                    break;
                case 16 :
                    // InternalODL.g:7035:3: kw= 'return'
                    {
                    kw=(Token)match(input,86,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getReturnKeyword_15());
                      		
                    }

                    }
                    break;
                case 17 :
                    // InternalODL.g:7041:3: kw= 'super'
                    {
                    kw=(Token)match(input,83,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getSuperKeyword_16());
                      		
                    }

                    }
                    break;
                case 18 :
                    // InternalODL.g:7047:3: kw= 'switch'
                    {
                    kw=(Token)match(input,79,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getSwitchKeyword_17());
                      		
                    }

                    }
                    break;
                case 19 :
                    // InternalODL.g:7053:3: kw= 'this'
                    {
                    kw=(Token)match(input,82,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getThisKeyword_18());
                      		
                    }

                    }
                    break;
                case 20 :
                    // InternalODL.g:7059:3: kw= 'while'
                    {
                    kw=(Token)match(input,76,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getWhileKeyword_19());
                      		
                    }

                    }
                    break;
                case 21 :
                    // InternalODL.g:7065:3: kw= 'abstract'
                    {
                    kw=(Token)match(input,95,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getAbstractKeyword_20());
                      		
                    }

                    }
                    break;
                case 22 :
                    // InternalODL.g:7071:3: kw= 'assert'
                    {
                    kw=(Token)match(input,96,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getAssertKeyword_21());
                      		
                    }

                    }
                    break;
                case 23 :
                    // InternalODL.g:7077:3: kw= 'catch'
                    {
                    kw=(Token)match(input,97,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getCatchKeyword_22());
                      		
                    }

                    }
                    break;
                case 24 :
                    // InternalODL.g:7083:3: kw= 'const'
                    {
                    kw=(Token)match(input,98,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getConstKeyword_23());
                      		
                    }

                    }
                    break;
                case 25 :
                    // InternalODL.g:7089:3: kw= 'enum'
                    {
                    kw=(Token)match(input,99,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getEnumKeyword_24());
                      		
                    }

                    }
                    break;
                case 26 :
                    // InternalODL.g:7095:3: kw= 'final'
                    {
                    kw=(Token)match(input,100,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getFinalKeyword_25());
                      		
                    }

                    }
                    break;
                case 27 :
                    // InternalODL.g:7101:3: kw= 'finally'
                    {
                    kw=(Token)match(input,101,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getFinallyKeyword_26());
                      		
                    }

                    }
                    break;
                case 28 :
                    // InternalODL.g:7107:3: kw= 'goto'
                    {
                    kw=(Token)match(input,102,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getGotoKeyword_27());
                      		
                    }

                    }
                    break;
                case 29 :
                    // InternalODL.g:7113:3: kw= 'import'
                    {
                    kw=(Token)match(input,103,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getImportKeyword_28());
                      		
                    }

                    }
                    break;
                case 30 :
                    // InternalODL.g:7119:3: kw= 'native'
                    {
                    kw=(Token)match(input,104,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getNativeKeyword_29());
                      		
                    }

                    }
                    break;
                case 31 :
                    // InternalODL.g:7125:3: kw= 'private'
                    {
                    kw=(Token)match(input,105,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getPrivateKeyword_30());
                      		
                    }

                    }
                    break;
                case 32 :
                    // InternalODL.g:7131:3: kw= 'protected'
                    {
                    kw=(Token)match(input,106,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getProtectedKeyword_31());
                      		
                    }

                    }
                    break;
                case 33 :
                    // InternalODL.g:7137:3: kw= 'public'
                    {
                    kw=(Token)match(input,107,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getPublicKeyword_32());
                      		
                    }

                    }
                    break;
                case 34 :
                    // InternalODL.g:7143:3: kw= 'static'
                    {
                    kw=(Token)match(input,69,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getStaticKeyword_33());
                      		
                    }

                    }
                    break;
                case 35 :
                    // InternalODL.g:7149:3: kw= 'synchronized'
                    {
                    kw=(Token)match(input,108,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getSynchronizedKeyword_34());
                      		
                    }

                    }
                    break;
                case 36 :
                    // InternalODL.g:7155:3: kw= 'throw'
                    {
                    kw=(Token)match(input,109,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getThrowKeyword_35());
                      		
                    }

                    }
                    break;
                case 37 :
                    // InternalODL.g:7161:3: kw= 'throws'
                    {
                    kw=(Token)match(input,110,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getThrowsKeyword_36());
                      		
                    }

                    }
                    break;
                case 38 :
                    // InternalODL.g:7167:3: kw= 'transient'
                    {
                    kw=(Token)match(input,111,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getTransientKeyword_37());
                      		
                    }

                    }
                    break;
                case 39 :
                    // InternalODL.g:7173:3: kw= 'try'
                    {
                    kw=(Token)match(input,112,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getTryKeyword_38());
                      		
                    }

                    }
                    break;
                case 40 :
                    // InternalODL.g:7179:3: kw= 'volatile'
                    {
                    kw=(Token)match(input,113,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getIQL_JAVA_KEYWORDSAccess().getVolatileKeyword_39());
                      		
                    }

                    }
                    break;
                case 41 :
                    // InternalODL.g:7185:3: kw= 'strictfp'
                    {
                    kw=(Token)match(input,114,FOLLOW_2); if (state.failed) return current;
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
    // InternalODL.g:7194:1: entryRuleBOOLEAN returns [String current=null] : iv_ruleBOOLEAN= ruleBOOLEAN EOF ;
    public final String entryRuleBOOLEAN() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleBOOLEAN = null;


        try {
            // InternalODL.g:7194:47: (iv_ruleBOOLEAN= ruleBOOLEAN EOF )
            // InternalODL.g:7195:2: iv_ruleBOOLEAN= ruleBOOLEAN EOF
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
    // InternalODL.g:7201:1: ruleBOOLEAN returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'true' | kw= 'false' ) ;
    public final AntlrDatatypeRuleToken ruleBOOLEAN() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalODL.g:7207:2: ( (kw= 'true' | kw= 'false' ) )
            // InternalODL.g:7208:2: (kw= 'true' | kw= 'false' )
            {
            // InternalODL.g:7208:2: (kw= 'true' | kw= 'false' )
            int alt101=2;
            int LA101_0 = input.LA(1);

            if ( (LA101_0==115) ) {
                alt101=1;
            }
            else if ( (LA101_0==116) ) {
                alt101=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 101, 0, input);

                throw nvae;
            }
            switch (alt101) {
                case 1 :
                    // InternalODL.g:7209:3: kw= 'true'
                    {
                    kw=(Token)match(input,115,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(kw);
                      			newLeafNode(kw, grammarAccess.getBOOLEANAccess().getTrueKeyword_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalODL.g:7215:3: kw= 'false'
                    {
                    kw=(Token)match(input,116,FOLLOW_2); if (state.failed) return current;
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

    // $ANTLR start synpred1_InternalODL
    public final void synpred1_InternalODL_fragment() throws RecognitionException {   
        // InternalODL.g:3623:5: ( 'else' )
        // InternalODL.g:3623:6: 'else'
        {
        match(input,75,FOLLOW_2); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred1_InternalODL

    // $ANTLR start synpred2_InternalODL
    public final void synpred2_InternalODL_fragment() throws RecognitionException {   
        // InternalODL.g:4610:5: ( ( () ( ( ruleOpAssign ) ) ) )
        // InternalODL.g:4610:6: ( () ( ( ruleOpAssign ) ) )
        {
        // InternalODL.g:4610:6: ( () ( ( ruleOpAssign ) ) )
        // InternalODL.g:4611:6: () ( ( ruleOpAssign ) )
        {
        // InternalODL.g:4611:6: ()
        // InternalODL.g:4612:6: 
        {
        }

        // InternalODL.g:4613:6: ( ( ruleOpAssign ) )
        // InternalODL.g:4614:7: ( ruleOpAssign )
        {
        // InternalODL.g:4614:7: ( ruleOpAssign )
        // InternalODL.g:4615:8: ruleOpAssign
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
    // $ANTLR end synpred2_InternalODL

    // $ANTLR start synpred3_InternalODL
    public final void synpred3_InternalODL_fragment() throws RecognitionException {   
        // InternalODL.g:4752:5: ( ( () ( ( ruleOpLogicalOr ) ) ) )
        // InternalODL.g:4752:6: ( () ( ( ruleOpLogicalOr ) ) )
        {
        // InternalODL.g:4752:6: ( () ( ( ruleOpLogicalOr ) ) )
        // InternalODL.g:4753:6: () ( ( ruleOpLogicalOr ) )
        {
        // InternalODL.g:4753:6: ()
        // InternalODL.g:4754:6: 
        {
        }

        // InternalODL.g:4755:6: ( ( ruleOpLogicalOr ) )
        // InternalODL.g:4756:7: ( ruleOpLogicalOr )
        {
        // InternalODL.g:4756:7: ( ruleOpLogicalOr )
        // InternalODL.g:4757:8: ruleOpLogicalOr
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
    // $ANTLR end synpred3_InternalODL

    // $ANTLR start synpred4_InternalODL
    public final void synpred4_InternalODL_fragment() throws RecognitionException {   
        // InternalODL.g:4862:5: ( ( () ( ( ruleOpLogicalAnd ) ) ) )
        // InternalODL.g:4862:6: ( () ( ( ruleOpLogicalAnd ) ) )
        {
        // InternalODL.g:4862:6: ( () ( ( ruleOpLogicalAnd ) ) )
        // InternalODL.g:4863:6: () ( ( ruleOpLogicalAnd ) )
        {
        // InternalODL.g:4863:6: ()
        // InternalODL.g:4864:6: 
        {
        }

        // InternalODL.g:4865:6: ( ( ruleOpLogicalAnd ) )
        // InternalODL.g:4866:7: ( ruleOpLogicalAnd )
        {
        // InternalODL.g:4866:7: ( ruleOpLogicalAnd )
        // InternalODL.g:4867:8: ruleOpLogicalAnd
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
    // $ANTLR end synpred4_InternalODL

    // $ANTLR start synpred5_InternalODL
    public final void synpred5_InternalODL_fragment() throws RecognitionException {   
        // InternalODL.g:4972:5: ( ( () ( ( ruleOpEquality ) ) ) )
        // InternalODL.g:4972:6: ( () ( ( ruleOpEquality ) ) )
        {
        // InternalODL.g:4972:6: ( () ( ( ruleOpEquality ) ) )
        // InternalODL.g:4973:6: () ( ( ruleOpEquality ) )
        {
        // InternalODL.g:4973:6: ()
        // InternalODL.g:4974:6: 
        {
        }

        // InternalODL.g:4975:6: ( ( ruleOpEquality ) )
        // InternalODL.g:4976:7: ( ruleOpEquality )
        {
        // InternalODL.g:4976:7: ( ruleOpEquality )
        // InternalODL.g:4977:8: ruleOpEquality
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
    // $ANTLR end synpred5_InternalODL

    // $ANTLR start synpred6_InternalODL
    public final void synpred6_InternalODL_fragment() throws RecognitionException {   
        // InternalODL.g:5091:6: ( ( () 'instanceof' ) )
        // InternalODL.g:5091:7: ( () 'instanceof' )
        {
        // InternalODL.g:5091:7: ( () 'instanceof' )
        // InternalODL.g:5092:7: () 'instanceof'
        {
        // InternalODL.g:5092:7: ()
        // InternalODL.g:5093:7: 
        {
        }

        match(input,87,FOLLOW_2); if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred6_InternalODL

    // $ANTLR start synpred7_InternalODL
    public final void synpred7_InternalODL_fragment() throws RecognitionException {   
        // InternalODL.g:5134:6: ( ( () ( ( ruleOpRelational ) ) ) )
        // InternalODL.g:5134:7: ( () ( ( ruleOpRelational ) ) )
        {
        // InternalODL.g:5134:7: ( () ( ( ruleOpRelational ) ) )
        // InternalODL.g:5135:7: () ( ( ruleOpRelational ) )
        {
        // InternalODL.g:5135:7: ()
        // InternalODL.g:5136:7: 
        {
        }

        // InternalODL.g:5137:7: ( ( ruleOpRelational ) )
        // InternalODL.g:5138:8: ( ruleOpRelational )
        {
        // InternalODL.g:5138:8: ( ruleOpRelational )
        // InternalODL.g:5139:9: ruleOpRelational
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
    // $ANTLR end synpred7_InternalODL

    // $ANTLR start synpred8_InternalODL
    public final void synpred8_InternalODL_fragment() throws RecognitionException {   
        // InternalODL.g:5265:5: ( ( () ( ( ruleOpAdd ) ) ) )
        // InternalODL.g:5265:6: ( () ( ( ruleOpAdd ) ) )
        {
        // InternalODL.g:5265:6: ( () ( ( ruleOpAdd ) ) )
        // InternalODL.g:5266:6: () ( ( ruleOpAdd ) )
        {
        // InternalODL.g:5266:6: ()
        // InternalODL.g:5267:6: 
        {
        }

        // InternalODL.g:5268:6: ( ( ruleOpAdd ) )
        // InternalODL.g:5269:7: ( ruleOpAdd )
        {
        // InternalODL.g:5269:7: ( ruleOpAdd )
        // InternalODL.g:5270:8: ruleOpAdd
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
    // $ANTLR end synpred8_InternalODL

    // $ANTLR start synpred9_InternalODL
    public final void synpred9_InternalODL_fragment() throws RecognitionException {   
        // InternalODL.g:5383:5: ( ( () ( ( ruleOpMulti ) ) ) )
        // InternalODL.g:5383:6: ( () ( ( ruleOpMulti ) ) )
        {
        // InternalODL.g:5383:6: ( () ( ( ruleOpMulti ) ) )
        // InternalODL.g:5384:6: () ( ( ruleOpMulti ) )
        {
        // InternalODL.g:5384:6: ()
        // InternalODL.g:5385:6: 
        {
        }

        // InternalODL.g:5386:6: ( ( ruleOpMulti ) )
        // InternalODL.g:5387:7: ( ruleOpMulti )
        {
        // InternalODL.g:5387:7: ( ruleOpMulti )
        // InternalODL.g:5388:8: ruleOpMulti
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
    // $ANTLR end synpred9_InternalODL

    // $ANTLR start synpred10_InternalODL
    public final void synpred10_InternalODL_fragment() throws RecognitionException {   
        // InternalODL.g:5649:5: ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )
        // InternalODL.g:5649:6: ( () '(' ( ( ruleJvmTypeReference ) ) ')' )
        {
        // InternalODL.g:5649:6: ( () '(' ( ( ruleJvmTypeReference ) ) ')' )
        // InternalODL.g:5650:6: () '(' ( ( ruleJvmTypeReference ) ) ')'
        {
        // InternalODL.g:5650:6: ()
        // InternalODL.g:5651:6: 
        {
        }

        match(input,14,FOLLOW_6); if (state.failed) return ;
        // InternalODL.g:5653:6: ( ( ruleJvmTypeReference ) )
        // InternalODL.g:5654:7: ( ruleJvmTypeReference )
        {
        // InternalODL.g:5654:7: ( ruleJvmTypeReference )
        // InternalODL.g:5655:8: ruleJvmTypeReference
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
    // $ANTLR end synpred10_InternalODL

    // $ANTLR start synpred11_InternalODL
    public final void synpred11_InternalODL_fragment() throws RecognitionException {   
        // InternalODL.g:5729:5: ( ( () ( ( ruleOpPostfix ) ) ) )
        // InternalODL.g:5729:6: ( () ( ( ruleOpPostfix ) ) )
        {
        // InternalODL.g:5729:6: ( () ( ( ruleOpPostfix ) ) )
        // InternalODL.g:5730:6: () ( ( ruleOpPostfix ) )
        {
        // InternalODL.g:5730:6: ()
        // InternalODL.g:5731:6: 
        {
        }

        // InternalODL.g:5732:6: ( ( ruleOpPostfix ) )
        // InternalODL.g:5733:7: ( ruleOpPostfix )
        {
        // InternalODL.g:5733:7: ( ruleOpPostfix )
        // InternalODL.g:5734:8: ruleOpPostfix
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
    // $ANTLR end synpred11_InternalODL

    // $ANTLR start synpred12_InternalODL
    public final void synpred12_InternalODL_fragment() throws RecognitionException {   
        // InternalODL.g:5910:5: ( ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' ) )
        // InternalODL.g:5910:6: ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' )
        {
        // InternalODL.g:5910:6: ( () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']' )
        // InternalODL.g:5911:6: () '[' ( ( ruleIQLExpression ) ) ( ',' ( ( ruleIQLExpression ) ) )* ']'
        {
        // InternalODL.g:5911:6: ()
        // InternalODL.g:5912:6: 
        {
        }

        match(input,64,FOLLOW_43); if (state.failed) return ;
        // InternalODL.g:5914:6: ( ( ruleIQLExpression ) )
        // InternalODL.g:5915:7: ( ruleIQLExpression )
        {
        // InternalODL.g:5915:7: ( ruleIQLExpression )
        // InternalODL.g:5916:8: ruleIQLExpression
        {
        pushFollow(FOLLOW_38);
        ruleIQLExpression();

        state._fsp--;
        if (state.failed) return ;

        }


        }

        // InternalODL.g:5919:6: ( ',' ( ( ruleIQLExpression ) ) )*
        loop102:
        do {
            int alt102=2;
            int LA102_0 = input.LA(1);

            if ( (LA102_0==26) ) {
                alt102=1;
            }


            switch (alt102) {
        	case 1 :
        	    // InternalODL.g:5920:7: ',' ( ( ruleIQLExpression ) )
        	    {
        	    match(input,26,FOLLOW_43); if (state.failed) return ;
        	    // InternalODL.g:5921:7: ( ( ruleIQLExpression ) )
        	    // InternalODL.g:5922:8: ( ruleIQLExpression )
        	    {
        	    // InternalODL.g:5922:8: ( ruleIQLExpression )
        	    // InternalODL.g:5923:9: ruleIQLExpression
        	    {
        	    pushFollow(FOLLOW_38);
        	    ruleIQLExpression();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }


        	    }


        	    }
        	    break;

        	default :
        	    break loop102;
            }
        } while (true);

        match(input,65,FOLLOW_2); if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred12_InternalODL

    // $ANTLR start synpred13_InternalODL
    public final void synpred13_InternalODL_fragment() throws RecognitionException {   
        // InternalODL.g:5995:6: ( ( () '.' ) )
        // InternalODL.g:5995:7: ( () '.' )
        {
        // InternalODL.g:5995:7: ( () '.' )
        // InternalODL.g:5996:7: () '.'
        {
        // InternalODL.g:5996:7: ()
        // InternalODL.g:5997:7: 
        {
        }

        match(input,66,FOLLOW_2); if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred13_InternalODL

    // $ANTLR start synpred14_InternalODL
    public final void synpred14_InternalODL_fragment() throws RecognitionException {   
        // InternalODL.g:6555:4: ( ruleIQLLiteralExpressionList )
        // InternalODL.g:6555:5: ruleIQLLiteralExpressionList
        {
        pushFollow(FOLLOW_2);
        ruleIQLLiteralExpressionList();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred14_InternalODL

    // Delegated rules

    public final boolean synpred14_InternalODL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred14_InternalODL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred13_InternalODL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred13_InternalODL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_InternalODL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_InternalODL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_InternalODL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_InternalODL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred9_InternalODL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred9_InternalODL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_InternalODL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_InternalODL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred5_InternalODL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_InternalODL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred7_InternalODL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred7_InternalODL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred4_InternalODL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_InternalODL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred8_InternalODL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred8_InternalODL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred10_InternalODL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred10_InternalODL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred12_InternalODL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred12_InternalODL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred6_InternalODL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred6_InternalODL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred11_InternalODL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred11_InternalODL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA35 dfa35 = new DFA35(this);
    protected DFA59 dfa59 = new DFA59(this);
    protected DFA81 dfa81 = new DFA81(this);
    protected DFA91 dfa91 = new DFA91(this);
    protected DFA93 dfa93 = new DFA93(this);
    static final String dfa_1s = "\6\uffff";
    static final String dfa_2s = "\1\uffff\1\4\3\uffff\1\4";
    static final String dfa_3s = "\3\4\2\uffff\1\4";
    static final String dfa_4s = "\1\4\1\133\1\4\2\uffff\1\133";
    static final String dfa_5s = "\3\uffff\1\2\1\1\1\uffff";
    static final String dfa_6s = "\6\uffff}>";
    static final String[] dfa_7s = {
            "\1\1",
            "\1\4\12\uffff\3\4\2\uffff\1\4\5\uffff\2\4\1\uffff\1\4\1\uffff\1\4\1\uffff\1\4\1\uffff\1\4\1\uffff\1\4\2\uffff\4\4\1\uffff\5\4\16\uffff\1\3\1\4\6\uffff\1\4\16\uffff\1\4\3\uffff\1\2",
            "\1\5",
            "",
            "",
            "\1\4\12\uffff\3\4\2\uffff\1\4\5\uffff\2\4\1\uffff\1\4\1\uffff\1\4\1\uffff\1\4\1\uffff\1\4\1\uffff\1\4\2\uffff\4\4\1\uffff\5\4\16\uffff\1\3\1\4\6\uffff\1\4\16\uffff\1\4\3\uffff\1\2"
    };

    static final short[] dfa_1 = DFA.unpackEncodedString(dfa_1s);
    static final short[] dfa_2 = DFA.unpackEncodedString(dfa_2s);
    static final char[] dfa_3 = DFA.unpackEncodedStringToUnsignedChars(dfa_3s);
    static final char[] dfa_4 = DFA.unpackEncodedStringToUnsignedChars(dfa_4s);
    static final short[] dfa_5 = DFA.unpackEncodedString(dfa_5s);
    static final short[] dfa_6 = DFA.unpackEncodedString(dfa_6s);
    static final short[][] dfa_7 = unpackEncodedStringArray(dfa_7s);

    class DFA35 extends DFA {

        public DFA35(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 35;
            this.eot = dfa_1;
            this.eof = dfa_2;
            this.min = dfa_3;
            this.max = dfa_4;
            this.accept = dfa_5;
            this.special = dfa_6;
            this.transition = dfa_7;
        }
        public String getDescription() {
            return "1762:2: (this_IQLSimpleTypeRef_0= ruleIQLSimpleTypeRef | this_IQLArrayTypeRef_1= ruleIQLArrayTypeRef )";
        }
    }
    static final String dfa_8s = "\35\uffff";
    static final String dfa_9s = "\1\4\2\uffff\1\4\2\16\3\uffff\1\16\5\uffff\2\4\2\uffff\4\4\1\101\1\33\2\4\2\uffff";
    static final String dfa_10s = "\1\164\2\uffff\1\133\2\127\3\uffff\1\16\5\uffff\1\4\1\164\2\uffff\1\4\2\133\1\4\1\101\1\61\1\133\1\100\2\uffff";
    static final String dfa_11s = "\1\uffff\1\1\1\2\3\uffff\1\3\1\4\1\5\1\uffff\1\10\1\12\1\13\1\14\1\16\2\uffff\1\11\1\15\10\uffff\1\7\1\6";
    static final String dfa_12s = "\35\uffff}>";
    static final String[] dfa_13s = {
            "\1\3\1\uffff\3\2\1\uffff\1\2\3\uffff\1\2\1\uffff\1\1\13\uffff\1\2\1\uffff\1\2\7\uffff\2\2\4\uffff\1\2\23\uffff\1\2\2\uffff\1\2\6\uffff\1\6\1\uffff\1\7\1\10\1\11\1\12\2\uffff\1\4\1\5\1\13\1\14\1\15\1\uffff\2\2\2\uffff\1\16\26\uffff\2\2",
            "",
            "",
            "\1\21\11\uffff\1\2\5\uffff\1\2\7\uffff\20\2\1\uffff\5\2\16\uffff\1\20\1\uffff\1\2\24\uffff\1\2\3\uffff\1\17",
            "\1\22\5\uffff\1\2\7\uffff\20\2\1\uffff\5\2\16\uffff\1\2\1\uffff\1\2\24\uffff\1\2",
            "\1\22\5\uffff\1\2\7\uffff\20\2\1\uffff\5\2\16\uffff\1\2\1\uffff\1\2\24\uffff\1\2",
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
            "\1\2\1\uffff\3\2\1\uffff\1\2\3\uffff\1\2\15\uffff\1\2\1\uffff\1\2\7\uffff\2\2\4\uffff\1\2\23\uffff\1\2\1\21\1\uffff\1\2\16\uffff\2\2\4\uffff\2\2\31\uffff\2\2",
            "",
            "",
            "\1\25",
            "\1\21\11\uffff\1\2\5\uffff\1\2\7\uffff\20\2\1\uffff\5\2\16\uffff\1\20\1\uffff\1\2\24\uffff\1\2\3\uffff\1\17",
            "\1\30\73\uffff\1\27\32\uffff\1\26",
            "\1\31",
            "\1\32",
            "\1\33\25\uffff\1\34",
            "\1\30\73\uffff\1\27\32\uffff\1\26",
            "\1\30\73\uffff\1\27",
            "",
            ""
    };

    static final short[] dfa_8 = DFA.unpackEncodedString(dfa_8s);
    static final char[] dfa_9 = DFA.unpackEncodedStringToUnsignedChars(dfa_9s);
    static final char[] dfa_10 = DFA.unpackEncodedStringToUnsignedChars(dfa_10s);
    static final short[] dfa_11 = DFA.unpackEncodedString(dfa_11s);
    static final short[] dfa_12 = DFA.unpackEncodedString(dfa_12s);
    static final short[][] dfa_13 = unpackEncodedStringArray(dfa_13s);

    class DFA59 extends DFA {

        public DFA59(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 59;
            this.eot = dfa_8;
            this.eof = dfa_8;
            this.min = dfa_9;
            this.max = dfa_10;
            this.accept = dfa_11;
            this.special = dfa_12;
            this.transition = dfa_13;
        }
        public String getDescription() {
            return "3321:2: (this_IQLStatementBlock_0= ruleIQLStatementBlock | this_IQLExpressionStatement_1= ruleIQLExpressionStatement | this_IQLIfStatement_2= ruleIQLIfStatement | this_IQLWhileStatement_3= ruleIQLWhileStatement | this_IQLDoWhileStatement_4= ruleIQLDoWhileStatement | this_IQLForStatement_5= ruleIQLForStatement | this_IQLForEachStatement_6= ruleIQLForEachStatement | this_IQLSwitchStatement_7= ruleIQLSwitchStatement | this_IQLVariableStatement_8= ruleIQLVariableStatement | this_IQLBreakStatement_9= ruleIQLBreakStatement | this_IQLContinueStatement_10= ruleIQLContinueStatement | this_IQLReturnStatement_11= ruleIQLReturnStatement | this_IQLConstructorCallStatement_12= ruleIQLConstructorCallStatement | this_IQLJavaStatement_13= ruleIQLJavaStatement )";
        }
    }
    static final String dfa_14s = "\25\uffff";
    static final String dfa_15s = "\1\4\5\uffff\1\0\16\uffff";
    static final String dfa_16s = "\1\164\5\uffff\1\0\16\uffff";
    static final String dfa_17s = "\1\uffff\1\1\1\uffff\1\2\1\3\2\uffff\1\5\14\uffff\1\4";
    static final String dfa_18s = "\6\uffff\1\0\16\uffff}>";
    static final String[] dfa_19s = {
            "\1\7\1\uffff\3\7\1\uffff\1\7\3\uffff\1\6\15\uffff\1\1\1\uffff\1\1\7\uffff\2\4\4\uffff\1\3\23\uffff\1\7\2\uffff\1\7\16\uffff\2\7\4\uffff\2\7\31\uffff\2\7",
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

    class DFA81 extends DFA {

        public DFA81(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 81;
            this.eot = dfa_14;
            this.eof = dfa_14;
            this.min = dfa_15;
            this.max = dfa_16;
            this.accept = dfa_17;
            this.special = dfa_18;
            this.transition = dfa_19;
        }
        public String getDescription() {
            return "5496:2: ( ( ( () ( (lv_op_1_0= ruleOpUnaryPlusMinus ) ) ) ( (lv_operand_2_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_4_0= ruleOpUnaryBooleanNot ) ) ) ( (lv_operand_5_0= ruleIQLMemberCallExpression ) ) ) | ( ( () ( (lv_op_7_0= ruleOpPrefix ) ) ) ( (lv_operand_8_0= ruleIQLMemberCallExpression ) ) ) | ( ( ( ( () '(' ( ( ruleJvmTypeReference ) ) ')' ) )=> ( () otherlv_10= '(' ( (lv_targetRef_11_0= ruleJvmTypeReference ) ) otherlv_12= ')' ) ) ( (lv_operand_13_0= ruleIQLMemberCallExpression ) ) ) | (this_IQLMemberCallExpression_14= ruleIQLMemberCallExpression ( ( ( () ( ( ruleOpPostfix ) ) ) )=> ( () ( (lv_op_16_0= ruleOpPostfix ) ) ) )? ) )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA81_6 = input.LA(1);

                         
                        int index81_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_InternalODL()) ) {s = 20;}

                        else if ( (true) ) {s = 7;}

                         
                        input.seek(index81_6);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 81, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String dfa_20s = "\1\4\1\16\1\4\2\uffff\1\16";
    static final String[] dfa_21s = {
            "\1\1",
            "\1\3\1\uffff\1\3\57\uffff\1\4\32\uffff\1\2",
            "\1\5",
            "",
            "",
            "\1\3\1\uffff\1\3\57\uffff\1\4\32\uffff\1\2"
    };
    static final char[] dfa_20 = DFA.unpackEncodedStringToUnsignedChars(dfa_20s);
    static final short[][] dfa_21 = unpackEncodedStringArray(dfa_21s);

    class DFA91 extends DFA {

        public DFA91(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 91;
            this.eot = dfa_1;
            this.eof = dfa_1;
            this.min = dfa_20;
            this.max = dfa_4;
            this.accept = dfa_5;
            this.special = dfa_6;
            this.transition = dfa_21;
        }
        public String getDescription() {
            return "6228:4: ( ( (lv_ref_13_0= ruleIQLArrayTypeRef ) ) | ( ( (lv_ref_14_0= ruleIQLSimpleTypeRef ) ) ( ( ( (lv_argsList_15_0= ruleIQLArgumentsList ) ) ( (lv_argsMap_16_0= ruleIQLArgumentsMap ) )? ) | ( (lv_argsMap_17_0= ruleIQLArgumentsMap ) ) ) ) )";
        }
    }
    static final String dfa_22s = "\14\uffff";
    static final String dfa_23s = "\1\6\10\uffff\1\0\2\uffff";
    static final String dfa_24s = "\1\164\10\uffff\1\0\2\uffff";
    static final String dfa_25s = "\1\uffff\1\1\1\2\1\3\1\4\1\uffff\1\5\1\6\1\7\1\uffff\1\10\1\11";
    static final String dfa_26s = "\11\uffff\1\0\2\uffff}>";
    static final String[] dfa_27s = {
            "\1\2\1\3\1\1\1\uffff\1\6\65\uffff\1\11\2\uffff\1\10\25\uffff\1\7\31\uffff\2\4",
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

    static final short[] dfa_22 = DFA.unpackEncodedString(dfa_22s);
    static final char[] dfa_23 = DFA.unpackEncodedStringToUnsignedChars(dfa_23s);
    static final char[] dfa_24 = DFA.unpackEncodedStringToUnsignedChars(dfa_24s);
    static final short[] dfa_25 = DFA.unpackEncodedString(dfa_25s);
    static final short[] dfa_26 = DFA.unpackEncodedString(dfa_26s);
    static final short[][] dfa_27 = unpackEncodedStringArray(dfa_27s);

    class DFA93 extends DFA {

        public DFA93(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 93;
            this.eot = dfa_22;
            this.eof = dfa_22;
            this.min = dfa_23;
            this.max = dfa_24;
            this.accept = dfa_25;
            this.special = dfa_26;
            this.transition = dfa_27;
        }
        public String getDescription() {
            return "6361:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_DOUBLE ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_RANGE ) ) ) | ( () otherlv_11= 'class(' ( (lv_value_12_0= ruleJvmTypeReference ) ) otherlv_13= ')' ) | ( () otherlv_15= 'null' ) | ( ( ruleIQLLiteralExpressionList )=>this_IQLLiteralExpressionList_16= ruleIQLLiteralExpressionList ) | this_IQLLiteralExpressionMap_17= ruleIQLLiteralExpressionMap )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA93_9 = input.LA(1);

                         
                        int index93_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_InternalODL()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index93_9);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 93, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000002002L,0x0000000010000250L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000002002L,0x0000000010000240L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000002000L,0x0000000010000240L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000014000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000008010L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000003EE0010L,0x0000000010000000L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0002000000114000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000000010010L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000008014010L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000004008000L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000008010010L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0xFFFFFFFFFC7FE3F2L,0x001FFFFFC1FFFFEFL});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000240L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000000000010L,0x0000000000000020L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000000010000L,0x0000000000000180L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000000010000L,0x0000000000000100L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000004010000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000000000010000L,0x0000000000000080L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000001L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000008100000L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x00000000000101D0L,0x0018000000000009L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x00000000000101D0L,0x001800000000000BL});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000000004000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x00000000000301D0L,0x0018000000000009L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000000004020000L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x000010C0500045D0L,0x00180000030C0009L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x000010C05000C5D0L,0x00180000030C0009L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000000000020010L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x000010C0500345D0L,0x00180000137CF409L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x000010C0500145D0L,0x00180000137CF409L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000800L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0000000000020000L,0x0000000000030000L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x000010C0500145D2L,0x00180000137CF409L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0002000000014000L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x000010C0501045D0L,0x00180000030C0009L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0002002AA0000002L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x0000400000000002L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x0001200000000002L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x00000F0000000002L,0x0000000000800000L});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x0000000050000002L});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0x0000001500000002L});
    public static final BitSet FOLLOW_62 = new BitSet(new long[]{0x000000C000000002L});
    public static final BitSet FOLLOW_63 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000005L});
    public static final BitSet FOLLOW_64 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_65 = new BitSet(new long[]{0x000010C0500045D0L,0x00180000030C000BL});
    public static final BitSet FOLLOW_66 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_67 = new BitSet(new long[]{0x0000000000000002L,0x0000000008000000L});
    public static final BitSet FOLLOW_68 = new BitSet(new long[]{0xFFFFFFFFFC7FE3F0L,0x001FFFFFE1FFFFEFL});
    public static final BitSet FOLLOW_69 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});

}