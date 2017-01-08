package de.uniol.inf.is.odysseus.parser.novel.cql.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import de.uniol.inf.is.odysseus.parser.novel.cql.services.CQLGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalCQLParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_INT", "RULE_FLOAT_NUMBER", "RULE_STRING", "RULE_ID", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "';'", "'('", "')'", "'TRUE'", "'FALSE'", "'['", "']'", "'AS'", "'INTEGER'", "'DOUBLE'", "'FLOAT'", "'STRING'", "'BOOLEAN'", "'STARTTIMESTAMP'", "'ENDTIMESTAMP'", "'OR'", "'AND'", "'=='", "'!='", "'>='", "'<='", "'<'", "'>'", "'+'", "'-'", "'*'", "'/'", "'NOT'", "'SELECT'", "'DISTINCT'", "','", "'FROM'", "'WHERE'", "'GROUP'", "'BY'", "'HAVING'", "'CREATE'", "'ATTACH'", "'STREAM'", "'SINK'", "'WRAPPER'", "'PROTOCOL'", "'TRANSPORT'", "'DATAHANDLER'", "'OPTIONS'", "'CHANNEL'", "':'", "'VIEW'", "'TO'", "'DROP'", "'IF'", "'EXISTS'", "'UNBOUNDED'", "'SIZE'", "'ADVANCE'", "'TIME'", "'TUPLE'", "'PARTITION'"
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
    public static final int T__66=66;
    public static final int RULE_ML_COMMENT=8;
    public static final int T__23=23;
    public static final int T__67=67;
    public static final int T__24=24;
    public static final int T__68=68;
    public static final int T__25=25;
    public static final int T__69=69;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T__20=20;
    public static final int T__64=64;
    public static final int T__21=21;
    public static final int T__65=65;
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

        public InternalCQLParser(TokenStream input, CQLGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "Model";
       	}

       	@Override
       	protected CQLGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleModel"
    // InternalCQL.g:64:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // InternalCQL.g:64:46: (iv_ruleModel= ruleModel EOF )
            // InternalCQL.g:65:2: iv_ruleModel= ruleModel EOF
            {
             newCompositeNode(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleModel=ruleModel();

            state._fsp--;

             current =iv_ruleModel; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleModel"


    // $ANTLR start "ruleModel"
    // InternalCQL.g:71:1: ruleModel returns [EObject current=null] : ( (lv_statements_0_0= ruleStatement ) )* ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        EObject lv_statements_0_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:77:2: ( ( (lv_statements_0_0= ruleStatement ) )* )
            // InternalCQL.g:78:2: ( (lv_statements_0_0= ruleStatement ) )*
            {
            // InternalCQL.g:78:2: ( (lv_statements_0_0= ruleStatement ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==40||(LA1_0>=48 && LA1_0<=50)||LA1_0==61) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalCQL.g:79:3: (lv_statements_0_0= ruleStatement )
            	    {
            	    // InternalCQL.g:79:3: (lv_statements_0_0= ruleStatement )
            	    // InternalCQL.g:80:4: lv_statements_0_0= ruleStatement
            	    {

            	    				newCompositeNode(grammarAccess.getModelAccess().getStatementsStatementParserRuleCall_0());
            	    			
            	    pushFollow(FOLLOW_3);
            	    lv_statements_0_0=ruleStatement();

            	    state._fsp--;


            	    				if (current==null) {
            	    					current = createModelElementForParent(grammarAccess.getModelRule());
            	    				}
            	    				add(
            	    					current,
            	    					"statements",
            	    					lv_statements_0_0,
            	    					"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Statement");
            	    				afterParserOrEnumRuleCall();
            	    			

            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModel"


    // $ANTLR start "entryRuleStatement"
    // InternalCQL.g:100:1: entryRuleStatement returns [EObject current=null] : iv_ruleStatement= ruleStatement EOF ;
    public final EObject entryRuleStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStatement = null;


        try {
            // InternalCQL.g:100:50: (iv_ruleStatement= ruleStatement EOF )
            // InternalCQL.g:101:2: iv_ruleStatement= ruleStatement EOF
            {
             newCompositeNode(grammarAccess.getStatementRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleStatement=ruleStatement();

            state._fsp--;

             current =iv_ruleStatement; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleStatement"


    // $ANTLR start "ruleStatement"
    // InternalCQL.g:107:1: ruleStatement returns [EObject current=null] : ( ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) ) (otherlv_4= ';' )? ) ;
    public final EObject ruleStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_4=null;
        EObject lv_type_0_0 = null;

        EObject lv_type_1_0 = null;

        EObject lv_type_2_0 = null;

        EObject lv_type_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:113:2: ( ( ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) ) (otherlv_4= ';' )? ) )
            // InternalCQL.g:114:2: ( ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) ) (otherlv_4= ';' )? )
            {
            // InternalCQL.g:114:2: ( ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) ) (otherlv_4= ';' )? )
            // InternalCQL.g:115:3: ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) ) (otherlv_4= ';' )?
            {
            // InternalCQL.g:115:3: ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) )
            int alt2=4;
            switch ( input.LA(1) ) {
            case 40:
                {
                alt2=1;
                }
                break;
            case 48:
            case 49:
                {
                alt2=2;
                }
                break;
            case 50:
                {
                alt2=3;
                }
                break;
            case 61:
                {
                alt2=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // InternalCQL.g:116:4: ( (lv_type_0_0= ruleSelect_Statement ) )
                    {
                    // InternalCQL.g:116:4: ( (lv_type_0_0= ruleSelect_Statement ) )
                    // InternalCQL.g:117:5: (lv_type_0_0= ruleSelect_Statement )
                    {
                    // InternalCQL.g:117:5: (lv_type_0_0= ruleSelect_Statement )
                    // InternalCQL.g:118:6: lv_type_0_0= ruleSelect_Statement
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeSelect_StatementParserRuleCall_0_0_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_0_0=ruleSelect_Statement();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_0_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Select_Statement");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:136:4: ( (lv_type_1_0= ruleCreate_Statement ) )
                    {
                    // InternalCQL.g:136:4: ( (lv_type_1_0= ruleCreate_Statement ) )
                    // InternalCQL.g:137:5: (lv_type_1_0= ruleCreate_Statement )
                    {
                    // InternalCQL.g:137:5: (lv_type_1_0= ruleCreate_Statement )
                    // InternalCQL.g:138:6: lv_type_1_0= ruleCreate_Statement
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeCreate_StatementParserRuleCall_0_1_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_1_0=ruleCreate_Statement();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_1_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Create_Statement");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:156:4: ( (lv_type_2_0= ruleStreamTo ) )
                    {
                    // InternalCQL.g:156:4: ( (lv_type_2_0= ruleStreamTo ) )
                    // InternalCQL.g:157:5: (lv_type_2_0= ruleStreamTo )
                    {
                    // InternalCQL.g:157:5: (lv_type_2_0= ruleStreamTo )
                    // InternalCQL.g:158:6: lv_type_2_0= ruleStreamTo
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeStreamToParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_2_0=ruleStreamTo();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.StreamTo");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 4 :
                    // InternalCQL.g:176:4: ( (lv_type_3_0= ruleDrop ) )
                    {
                    // InternalCQL.g:176:4: ( (lv_type_3_0= ruleDrop ) )
                    // InternalCQL.g:177:5: (lv_type_3_0= ruleDrop )
                    {
                    // InternalCQL.g:177:5: (lv_type_3_0= ruleDrop )
                    // InternalCQL.g:178:6: lv_type_3_0= ruleDrop
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeDropParserRuleCall_0_3_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_3_0=ruleDrop();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Drop");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:196:3: (otherlv_4= ';' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==12) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalCQL.g:197:4: otherlv_4= ';'
                    {
                    otherlv_4=(Token)match(input,12,FOLLOW_2); 

                    				newLeafNode(otherlv_4, grammarAccess.getStatementAccess().getSemicolonKeyword_1());
                    			

                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleStatement"


    // $ANTLR start "entryRuleNested_Statement"
    // InternalCQL.g:206:1: entryRuleNested_Statement returns [EObject current=null] : iv_ruleNested_Statement= ruleNested_Statement EOF ;
    public final EObject entryRuleNested_Statement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNested_Statement = null;


        try {
            // InternalCQL.g:206:57: (iv_ruleNested_Statement= ruleNested_Statement EOF )
            // InternalCQL.g:207:2: iv_ruleNested_Statement= ruleNested_Statement EOF
            {
             newCompositeNode(grammarAccess.getNested_StatementRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleNested_Statement=ruleNested_Statement();

            state._fsp--;

             current =iv_ruleNested_Statement; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleNested_Statement"


    // $ANTLR start "ruleNested_Statement"
    // InternalCQL.g:213:1: ruleNested_Statement returns [EObject current=null] : (otherlv_0= '(' this_Select_Statement_1= ruleSelect_Statement otherlv_2= ')' ) ;
    public final EObject ruleNested_Statement() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject this_Select_Statement_1 = null;



        	enterRule();

        try {
            // InternalCQL.g:219:2: ( (otherlv_0= '(' this_Select_Statement_1= ruleSelect_Statement otherlv_2= ')' ) )
            // InternalCQL.g:220:2: (otherlv_0= '(' this_Select_Statement_1= ruleSelect_Statement otherlv_2= ')' )
            {
            // InternalCQL.g:220:2: (otherlv_0= '(' this_Select_Statement_1= ruleSelect_Statement otherlv_2= ')' )
            // InternalCQL.g:221:3: otherlv_0= '(' this_Select_Statement_1= ruleSelect_Statement otherlv_2= ')'
            {
            otherlv_0=(Token)match(input,13,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getNested_StatementAccess().getLeftParenthesisKeyword_0());
            		

            			newCompositeNode(grammarAccess.getNested_StatementAccess().getSelect_StatementParserRuleCall_1());
            		
            pushFollow(FOLLOW_6);
            this_Select_Statement_1=ruleSelect_Statement();

            state._fsp--;


            			current = this_Select_Statement_1;
            			afterParserOrEnumRuleCall();
            		
            otherlv_2=(Token)match(input,14,FOLLOW_2); 

            			newLeafNode(otherlv_2, grammarAccess.getNested_StatementAccess().getRightParenthesisKeyword_2());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNested_Statement"


    // $ANTLR start "entryRuleAtomic"
    // InternalCQL.g:241:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCQL.g:241:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCQL.g:242:2: iv_ruleAtomic= ruleAtomic EOF
            {
             newCompositeNode(grammarAccess.getAtomicRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAtomic=ruleAtomic();

            state._fsp--;

             current =iv_ruleAtomic; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAtomic"


    // $ANTLR start "ruleAtomic"
    // InternalCQL.g:248:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( (lv_value_9_0= ruleAttribute ) ) ) ) ;
    public final EObject ruleAtomic() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        Token lv_value_7_1=null;
        Token lv_value_7_2=null;
        EObject lv_value_9_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:254:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( (lv_value_9_0= ruleAttribute ) ) ) ) )
            // InternalCQL.g:255:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( (lv_value_9_0= ruleAttribute ) ) ) )
            {
            // InternalCQL.g:255:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( (lv_value_9_0= ruleAttribute ) ) ) )
            int alt5=5;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt5=1;
                }
                break;
            case RULE_FLOAT_NUMBER:
                {
                alt5=2;
                }
                break;
            case RULE_STRING:
                {
                alt5=3;
                }
                break;
            case 15:
            case 16:
                {
                alt5=4;
                }
                break;
            case RULE_ID:
                {
                alt5=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // InternalCQL.g:256:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQL.g:256:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQL.g:257:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQL.g:257:4: ()
                    // InternalCQL.g:258:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:264:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQL.g:265:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQL.g:265:5: (lv_value_1_0= RULE_INT )
                    // InternalCQL.g:266:6: lv_value_1_0= RULE_INT
                    {
                    lv_value_1_0=(Token)match(input,RULE_INT,FOLLOW_2); 

                    						newLeafNode(lv_value_1_0, grammarAccess.getAtomicAccess().getValueINTTerminalRuleCall_0_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"value",
                    							lv_value_1_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:284:3: ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) )
                    {
                    // InternalCQL.g:284:3: ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) )
                    // InternalCQL.g:285:4: () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) )
                    {
                    // InternalCQL.g:285:4: ()
                    // InternalCQL.g:286:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:292:4: ( (lv_value_3_0= RULE_FLOAT_NUMBER ) )
                    // InternalCQL.g:293:5: (lv_value_3_0= RULE_FLOAT_NUMBER )
                    {
                    // InternalCQL.g:293:5: (lv_value_3_0= RULE_FLOAT_NUMBER )
                    // InternalCQL.g:294:6: lv_value_3_0= RULE_FLOAT_NUMBER
                    {
                    lv_value_3_0=(Token)match(input,RULE_FLOAT_NUMBER,FOLLOW_2); 

                    						newLeafNode(lv_value_3_0, grammarAccess.getAtomicAccess().getValueFLOAT_NUMBERTerminalRuleCall_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"value",
                    							lv_value_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.FLOAT_NUMBER");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:312:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQL.g:312:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQL.g:313:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQL.g:313:4: ()
                    // InternalCQL.g:314:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:320:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQL.g:321:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQL.g:321:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQL.g:322:6: lv_value_5_0= RULE_STRING
                    {
                    lv_value_5_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

                    						newLeafNode(lv_value_5_0, grammarAccess.getAtomicAccess().getValueSTRINGTerminalRuleCall_2_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicRule());
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
                    break;
                case 4 :
                    // InternalCQL.g:340:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    {
                    // InternalCQL.g:340:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    // InternalCQL.g:341:4: () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    {
                    // InternalCQL.g:341:4: ()
                    // InternalCQL.g:342:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:348:4: ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    // InternalCQL.g:349:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    {
                    // InternalCQL.g:349:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    // InternalCQL.g:350:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    {
                    // InternalCQL.g:350:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==15) ) {
                        alt4=1;
                    }
                    else if ( (LA4_0==16) ) {
                        alt4=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 4, 0, input);

                        throw nvae;
                    }
                    switch (alt4) {
                        case 1 :
                            // InternalCQL.g:351:7: lv_value_7_1= 'TRUE'
                            {
                            lv_value_7_1=(Token)match(input,15,FOLLOW_2); 

                            							newLeafNode(lv_value_7_1, grammarAccess.getAtomicAccess().getValueTRUEKeyword_3_1_0_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getAtomicRule());
                            							}
                            							setWithLastConsumed(current, "value", lv_value_7_1, null);
                            						

                            }
                            break;
                        case 2 :
                            // InternalCQL.g:362:7: lv_value_7_2= 'FALSE'
                            {
                            lv_value_7_2=(Token)match(input,16,FOLLOW_2); 

                            							newLeafNode(lv_value_7_2, grammarAccess.getAtomicAccess().getValueFALSEKeyword_3_1_0_1());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getAtomicRule());
                            							}
                            							setWithLastConsumed(current, "value", lv_value_7_2, null);
                            						

                            }
                            break;

                    }


                    }


                    }


                    }


                    }
                    break;
                case 5 :
                    // InternalCQL.g:377:3: ( () ( (lv_value_9_0= ruleAttribute ) ) )
                    {
                    // InternalCQL.g:377:3: ( () ( (lv_value_9_0= ruleAttribute ) ) )
                    // InternalCQL.g:378:4: () ( (lv_value_9_0= ruleAttribute ) )
                    {
                    // InternalCQL.g:378:4: ()
                    // InternalCQL.g:379:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeRefAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:385:4: ( (lv_value_9_0= ruleAttribute ) )
                    // InternalCQL.g:386:5: (lv_value_9_0= ruleAttribute )
                    {
                    // InternalCQL.g:386:5: (lv_value_9_0= ruleAttribute )
                    // InternalCQL.g:387:6: lv_value_9_0= ruleAttribute
                    {

                    						newCompositeNode(grammarAccess.getAtomicAccess().getValueAttributeParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_value_9_0=ruleAttribute();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getAtomicRule());
                    						}
                    						set(
                    							current,
                    							"value",
                    							lv_value_9_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAtomic"


    // $ANTLR start "entryRuleSource"
    // InternalCQL.g:409:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalCQL.g:409:47: (iv_ruleSource= ruleSource EOF )
            // InternalCQL.g:410:2: iv_ruleSource= ruleSource EOF
            {
             newCompositeNode(grammarAccess.getSourceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSource=ruleSource();

            state._fsp--;

             current =iv_ruleSource; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSource"


    // $ANTLR start "ruleSource"
    // InternalCQL.g:416:1: ruleSource returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) ;
    public final EObject ruleSource() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        AntlrDatatypeRuleToken lv_unbounded_2_0 = null;

        EObject lv_time_3_0 = null;

        EObject lv_tuple_4_0 = null;

        EObject lv_alias_7_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:422:2: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:423:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:423:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? )
            // InternalCQL.g:424:3: ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:424:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:425:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:425:4: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:426:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_7); 

            					newLeafNode(lv_name_0_0, grammarAccess.getSourceAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSourceRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalCQL.g:442:3: (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==17) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // InternalCQL.g:443:4: otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']'
                    {
                    otherlv_1=(Token)match(input,17,FOLLOW_8); 

                    				newLeafNode(otherlv_1, grammarAccess.getSourceAccess().getLeftSquareBracketKeyword_1_0());
                    			
                    // InternalCQL.g:447:4: ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) )
                    int alt6=3;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==64) ) {
                        alt6=1;
                    }
                    else if ( (LA6_0==65) ) {
                        int LA6_2 = input.LA(2);

                        if ( (LA6_2==RULE_INT) ) {
                            int LA6_3 = input.LA(3);

                            if ( (LA6_3==66||LA6_3==68) ) {
                                alt6=3;
                            }
                            else if ( (LA6_3==RULE_ID) ) {
                                alt6=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 6, 3, input);

                                throw nvae;
                            }
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 2, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 0, input);

                        throw nvae;
                    }
                    switch (alt6) {
                        case 1 :
                            // InternalCQL.g:448:5: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                            {
                            // InternalCQL.g:448:5: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                            // InternalCQL.g:449:6: (lv_unbounded_2_0= ruleWindow_Unbounded )
                            {
                            // InternalCQL.g:449:6: (lv_unbounded_2_0= ruleWindow_Unbounded )
                            // InternalCQL.g:450:7: lv_unbounded_2_0= ruleWindow_Unbounded
                            {

                            							newCompositeNode(grammarAccess.getSourceAccess().getUnboundedWindow_UnboundedParserRuleCall_1_1_0_0());
                            						
                            pushFollow(FOLLOW_9);
                            lv_unbounded_2_0=ruleWindow_Unbounded();

                            state._fsp--;


                            							if (current==null) {
                            								current = createModelElementForParent(grammarAccess.getSourceRule());
                            							}
                            							set(
                            								current,
                            								"unbounded",
                            								lv_unbounded_2_0,
                            								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Window_Unbounded");
                            							afterParserOrEnumRuleCall();
                            						

                            }


                            }


                            }
                            break;
                        case 2 :
                            // InternalCQL.g:468:5: ( (lv_time_3_0= ruleWindow_Timebased ) )
                            {
                            // InternalCQL.g:468:5: ( (lv_time_3_0= ruleWindow_Timebased ) )
                            // InternalCQL.g:469:6: (lv_time_3_0= ruleWindow_Timebased )
                            {
                            // InternalCQL.g:469:6: (lv_time_3_0= ruleWindow_Timebased )
                            // InternalCQL.g:470:7: lv_time_3_0= ruleWindow_Timebased
                            {

                            							newCompositeNode(grammarAccess.getSourceAccess().getTimeWindow_TimebasedParserRuleCall_1_1_1_0());
                            						
                            pushFollow(FOLLOW_9);
                            lv_time_3_0=ruleWindow_Timebased();

                            state._fsp--;


                            							if (current==null) {
                            								current = createModelElementForParent(grammarAccess.getSourceRule());
                            							}
                            							set(
                            								current,
                            								"time",
                            								lv_time_3_0,
                            								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Window_Timebased");
                            							afterParserOrEnumRuleCall();
                            						

                            }


                            }


                            }
                            break;
                        case 3 :
                            // InternalCQL.g:488:5: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                            {
                            // InternalCQL.g:488:5: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                            // InternalCQL.g:489:6: (lv_tuple_4_0= ruleWindow_Tuplebased )
                            {
                            // InternalCQL.g:489:6: (lv_tuple_4_0= ruleWindow_Tuplebased )
                            // InternalCQL.g:490:7: lv_tuple_4_0= ruleWindow_Tuplebased
                            {

                            							newCompositeNode(grammarAccess.getSourceAccess().getTupleWindow_TuplebasedParserRuleCall_1_1_2_0());
                            						
                            pushFollow(FOLLOW_9);
                            lv_tuple_4_0=ruleWindow_Tuplebased();

                            state._fsp--;


                            							if (current==null) {
                            								current = createModelElementForParent(grammarAccess.getSourceRule());
                            							}
                            							set(
                            								current,
                            								"tuple",
                            								lv_tuple_4_0,
                            								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Window_Tuplebased");
                            							afterParserOrEnumRuleCall();
                            						

                            }


                            }


                            }
                            break;

                    }

                    otherlv_5=(Token)match(input,18,FOLLOW_10); 

                    				newLeafNode(otherlv_5, grammarAccess.getSourceAccess().getRightSquareBracketKeyword_1_2());
                    			

                    }
                    break;

            }

            // InternalCQL.g:513:3: (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==19) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // InternalCQL.g:514:4: otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) )
                    {
                    otherlv_6=(Token)match(input,19,FOLLOW_11); 

                    				newLeafNode(otherlv_6, grammarAccess.getSourceAccess().getASKeyword_2_0());
                    			
                    // InternalCQL.g:518:4: ( (lv_alias_7_0= ruleAlias ) )
                    // InternalCQL.g:519:5: (lv_alias_7_0= ruleAlias )
                    {
                    // InternalCQL.g:519:5: (lv_alias_7_0= ruleAlias )
                    // InternalCQL.g:520:6: lv_alias_7_0= ruleAlias
                    {

                    						newCompositeNode(grammarAccess.getSourceAccess().getAliasAliasParserRuleCall_2_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_alias_7_0=ruleAlias();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSourceRule());
                    						}
                    						set(
                    							current,
                    							"alias",
                    							lv_alias_7_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Alias");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSource"


    // $ANTLR start "entryRuleDataType"
    // InternalCQL.g:542:1: entryRuleDataType returns [EObject current=null] : iv_ruleDataType= ruleDataType EOF ;
    public final EObject entryRuleDataType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataType = null;


        try {
            // InternalCQL.g:542:49: (iv_ruleDataType= ruleDataType EOF )
            // InternalCQL.g:543:2: iv_ruleDataType= ruleDataType EOF
            {
             newCompositeNode(grammarAccess.getDataTypeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDataType=ruleDataType();

            state._fsp--;

             current =iv_ruleDataType; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDataType"


    // $ANTLR start "ruleDataType"
    // InternalCQL.g:549:1: ruleDataType returns [EObject current=null] : ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) ) ) ;
    public final EObject ruleDataType() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_1=null;
        Token lv_value_0_2=null;
        Token lv_value_0_3=null;
        Token lv_value_0_4=null;
        Token lv_value_0_5=null;
        Token lv_value_0_6=null;
        Token lv_value_0_7=null;


        	enterRule();

        try {
            // InternalCQL.g:555:2: ( ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) ) ) )
            // InternalCQL.g:556:2: ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) ) )
            {
            // InternalCQL.g:556:2: ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) ) )
            // InternalCQL.g:557:3: ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) )
            {
            // InternalCQL.g:557:3: ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) )
            // InternalCQL.g:558:4: (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' )
            {
            // InternalCQL.g:558:4: (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' )
            int alt9=7;
            switch ( input.LA(1) ) {
            case 20:
                {
                alt9=1;
                }
                break;
            case 21:
                {
                alt9=2;
                }
                break;
            case 22:
                {
                alt9=3;
                }
                break;
            case 23:
                {
                alt9=4;
                }
                break;
            case 24:
                {
                alt9=5;
                }
                break;
            case 25:
                {
                alt9=6;
                }
                break;
            case 26:
                {
                alt9=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }

            switch (alt9) {
                case 1 :
                    // InternalCQL.g:559:5: lv_value_0_1= 'INTEGER'
                    {
                    lv_value_0_1=(Token)match(input,20,FOLLOW_2); 

                    					newLeafNode(lv_value_0_1, grammarAccess.getDataTypeAccess().getValueINTEGERKeyword_0_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_1, null);
                    				

                    }
                    break;
                case 2 :
                    // InternalCQL.g:570:5: lv_value_0_2= 'DOUBLE'
                    {
                    lv_value_0_2=(Token)match(input,21,FOLLOW_2); 

                    					newLeafNode(lv_value_0_2, grammarAccess.getDataTypeAccess().getValueDOUBLEKeyword_0_1());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_2, null);
                    				

                    }
                    break;
                case 3 :
                    // InternalCQL.g:581:5: lv_value_0_3= 'FLOAT'
                    {
                    lv_value_0_3=(Token)match(input,22,FOLLOW_2); 

                    					newLeafNode(lv_value_0_3, grammarAccess.getDataTypeAccess().getValueFLOATKeyword_0_2());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_3, null);
                    				

                    }
                    break;
                case 4 :
                    // InternalCQL.g:592:5: lv_value_0_4= 'STRING'
                    {
                    lv_value_0_4=(Token)match(input,23,FOLLOW_2); 

                    					newLeafNode(lv_value_0_4, grammarAccess.getDataTypeAccess().getValueSTRINGKeyword_0_3());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_4, null);
                    				

                    }
                    break;
                case 5 :
                    // InternalCQL.g:603:5: lv_value_0_5= 'BOOLEAN'
                    {
                    lv_value_0_5=(Token)match(input,24,FOLLOW_2); 

                    					newLeafNode(lv_value_0_5, grammarAccess.getDataTypeAccess().getValueBOOLEANKeyword_0_4());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_5, null);
                    				

                    }
                    break;
                case 6 :
                    // InternalCQL.g:614:5: lv_value_0_6= 'STARTTIMESTAMP'
                    {
                    lv_value_0_6=(Token)match(input,25,FOLLOW_2); 

                    					newLeafNode(lv_value_0_6, grammarAccess.getDataTypeAccess().getValueSTARTTIMESTAMPKeyword_0_5());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_6, null);
                    				

                    }
                    break;
                case 7 :
                    // InternalCQL.g:625:5: lv_value_0_7= 'ENDTIMESTAMP'
                    {
                    lv_value_0_7=(Token)match(input,26,FOLLOW_2); 

                    					newLeafNode(lv_value_0_7, grammarAccess.getDataTypeAccess().getValueENDTIMESTAMPKeyword_0_6());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_7, null);
                    				

                    }
                    break;

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDataType"


    // $ANTLR start "entryRuleAttribute"
    // InternalCQL.g:641:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCQL.g:641:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCQL.g:642:2: iv_ruleAttribute= ruleAttribute EOF
            {
             newCompositeNode(grammarAccess.getAttributeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAttribute=ruleAttribute();

            state._fsp--;

             current =iv_ruleAttribute; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAttribute"


    // $ANTLR start "ruleAttribute"
    // InternalCQL.g:648:1: ruleAttribute returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        EObject lv_alias_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:654:2: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:655:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:655:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? )
            // InternalCQL.g:656:3: ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:656:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:657:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:657:4: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:658:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_10); 

            					newLeafNode(lv_name_0_0, grammarAccess.getAttributeAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAttributeRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalCQL.g:674:3: (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==19) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // InternalCQL.g:675:4: otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) )
                    {
                    otherlv_1=(Token)match(input,19,FOLLOW_11); 

                    				newLeafNode(otherlv_1, grammarAccess.getAttributeAccess().getASKeyword_1_0());
                    			
                    // InternalCQL.g:679:4: ( (lv_alias_2_0= ruleAlias ) )
                    // InternalCQL.g:680:5: (lv_alias_2_0= ruleAlias )
                    {
                    // InternalCQL.g:680:5: (lv_alias_2_0= ruleAlias )
                    // InternalCQL.g:681:6: lv_alias_2_0= ruleAlias
                    {

                    						newCompositeNode(grammarAccess.getAttributeAccess().getAliasAliasParserRuleCall_1_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_alias_2_0=ruleAlias();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getAttributeRule());
                    						}
                    						set(
                    							current,
                    							"alias",
                    							lv_alias_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Alias");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAttribute"


    // $ANTLR start "entryRuleExpressionsModel"
    // InternalCQL.g:703:1: entryRuleExpressionsModel returns [EObject current=null] : iv_ruleExpressionsModel= ruleExpressionsModel EOF ;
    public final EObject entryRuleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionsModel = null;


        try {
            // InternalCQL.g:703:57: (iv_ruleExpressionsModel= ruleExpressionsModel EOF )
            // InternalCQL.g:704:2: iv_ruleExpressionsModel= ruleExpressionsModel EOF
            {
             newCompositeNode(grammarAccess.getExpressionsModelRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpressionsModel=ruleExpressionsModel();

            state._fsp--;

             current =iv_ruleExpressionsModel; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleExpressionsModel"


    // $ANTLR start "ruleExpressionsModel"
    // InternalCQL.g:710:1: ruleExpressionsModel returns [EObject current=null] : ( () ( (lv_elements_1_0= ruleExpression ) )+ ) ;
    public final EObject ruleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:716:2: ( ( () ( (lv_elements_1_0= ruleExpression ) )+ ) )
            // InternalCQL.g:717:2: ( () ( (lv_elements_1_0= ruleExpression ) )+ )
            {
            // InternalCQL.g:717:2: ( () ( (lv_elements_1_0= ruleExpression ) )+ )
            // InternalCQL.g:718:3: () ( (lv_elements_1_0= ruleExpression ) )+
            {
            // InternalCQL.g:718:3: ()
            // InternalCQL.g:719:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0(),
            					current);
            			

            }

            // InternalCQL.g:725:3: ( (lv_elements_1_0= ruleExpression ) )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>=RULE_INT && LA11_0<=RULE_ID)||LA11_0==13||(LA11_0>=15 && LA11_0<=16)||LA11_0==39) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // InternalCQL.g:726:4: (lv_elements_1_0= ruleExpression )
            	    {
            	    // InternalCQL.g:726:4: (lv_elements_1_0= ruleExpression )
            	    // InternalCQL.g:727:5: lv_elements_1_0= ruleExpression
            	    {

            	    					newCompositeNode(grammarAccess.getExpressionsModelAccess().getElementsExpressionParserRuleCall_1_0());
            	    				
            	    pushFollow(FOLLOW_12);
            	    lv_elements_1_0=ruleExpression();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getExpressionsModelRule());
            	    					}
            	    					add(
            	    						current,
            	    						"elements",
            	    						lv_elements_1_0,
            	    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Expression");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt11 >= 1 ) break loop11;
                        EarlyExitException eee =
                            new EarlyExitException(11, input);
                        throw eee;
                }
                cnt11++;
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleExpressionsModel"


    // $ANTLR start "entryRuleExpression"
    // InternalCQL.g:748:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCQL.g:748:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCQL.g:749:2: iv_ruleExpression= ruleExpression EOF
            {
             newCompositeNode(grammarAccess.getExpressionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpression=ruleExpression();

            state._fsp--;

             current =iv_ruleExpression; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleExpression"


    // $ANTLR start "ruleExpression"
    // InternalCQL.g:755:1: ruleExpression returns [EObject current=null] : this_Or_0= ruleOr ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_Or_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:761:2: (this_Or_0= ruleOr )
            // InternalCQL.g:762:2: this_Or_0= ruleOr
            {

            		newCompositeNode(grammarAccess.getExpressionAccess().getOrParserRuleCall());
            	
            pushFollow(FOLLOW_2);
            this_Or_0=ruleOr();

            state._fsp--;


            		current = this_Or_0;
            		afterParserOrEnumRuleCall();
            	

            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleExpression"


    // $ANTLR start "entryRuleOr"
    // InternalCQL.g:773:1: entryRuleOr returns [EObject current=null] : iv_ruleOr= ruleOr EOF ;
    public final EObject entryRuleOr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOr = null;


        try {
            // InternalCQL.g:773:43: (iv_ruleOr= ruleOr EOF )
            // InternalCQL.g:774:2: iv_ruleOr= ruleOr EOF
            {
             newCompositeNode(grammarAccess.getOrRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleOr=ruleOr();

            state._fsp--;

             current =iv_ruleOr; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleOr"


    // $ANTLR start "ruleOr"
    // InternalCQL.g:780:1: ruleOr returns [EObject current=null] : (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) ;
    public final EObject ruleOr() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_And_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:786:2: ( (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) )
            // InternalCQL.g:787:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            {
            // InternalCQL.g:787:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            // InternalCQL.g:788:3: this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrAccess().getAndParserRuleCall_0());
            		
            pushFollow(FOLLOW_13);
            this_And_0=ruleAnd();

            state._fsp--;


            			current = this_And_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:796:3: ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==27) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // InternalCQL.g:797:4: () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) )
            	    {
            	    // InternalCQL.g:797:4: ()
            	    // InternalCQL.g:798:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrAccess().getOrLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,27,FOLLOW_14); 

            	    				newLeafNode(otherlv_2, grammarAccess.getOrAccess().getORKeyword_1_1());
            	    			
            	    // InternalCQL.g:808:4: ( (lv_right_3_0= ruleAnd ) )
            	    // InternalCQL.g:809:5: (lv_right_3_0= ruleAnd )
            	    {
            	    // InternalCQL.g:809:5: (lv_right_3_0= ruleAnd )
            	    // InternalCQL.g:810:6: lv_right_3_0= ruleAnd
            	    {

            	    						newCompositeNode(grammarAccess.getOrAccess().getRightAndParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_13);
            	    lv_right_3_0=ruleAnd();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getOrRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.And");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleOr"


    // $ANTLR start "entryRuleAnd"
    // InternalCQL.g:832:1: entryRuleAnd returns [EObject current=null] : iv_ruleAnd= ruleAnd EOF ;
    public final EObject entryRuleAnd() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnd = null;


        try {
            // InternalCQL.g:832:44: (iv_ruleAnd= ruleAnd EOF )
            // InternalCQL.g:833:2: iv_ruleAnd= ruleAnd EOF
            {
             newCompositeNode(grammarAccess.getAndRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAnd=ruleAnd();

            state._fsp--;

             current =iv_ruleAnd; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAnd"


    // $ANTLR start "ruleAnd"
    // InternalCQL.g:839:1: ruleAnd returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAnd() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:845:2: ( (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCQL.g:846:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCQL.g:846:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCQL.g:847:3: this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_15);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:855:3: ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==28) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // InternalCQL.g:856:4: () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCQL.g:856:4: ()
            	    // InternalCQL.g:857:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndAccess().getAndLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,28,FOLLOW_16); 

            	    				newLeafNode(otherlv_2, grammarAccess.getAndAccess().getANDKeyword_1_1());
            	    			
            	    // InternalCQL.g:867:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCQL.g:868:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCQL.g:868:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCQL.g:869:6: lv_right_3_0= ruleEqualitiy
            	    {

            	    						newCompositeNode(grammarAccess.getAndAccess().getRightEqualitiyParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_15);
            	    lv_right_3_0=ruleEqualitiy();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getAndRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Equalitiy");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAnd"


    // $ANTLR start "entryRuleEqualitiy"
    // InternalCQL.g:891:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCQL.g:891:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCQL.g:892:2: iv_ruleEqualitiy= ruleEqualitiy EOF
            {
             newCompositeNode(grammarAccess.getEqualitiyRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEqualitiy=ruleEqualitiy();

            state._fsp--;

             current =iv_ruleEqualitiy; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEqualitiy"


    // $ANTLR start "ruleEqualitiy"
    // InternalCQL.g:898:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Comparison_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:904:2: ( (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCQL.g:905:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCQL.g:905:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCQL.g:906:3: this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_17);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:914:3: ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( ((LA15_0>=29 && LA15_0<=30)) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // InternalCQL.g:915:4: () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCQL.g:915:4: ()
            	    // InternalCQL.g:916:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:922:4: ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) )
            	    // InternalCQL.g:923:5: ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) )
            	    {
            	    // InternalCQL.g:923:5: ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) )
            	    // InternalCQL.g:924:6: (lv_op_2_1= '==' | lv_op_2_2= '!=' )
            	    {
            	    // InternalCQL.g:924:6: (lv_op_2_1= '==' | lv_op_2_2= '!=' )
            	    int alt14=2;
            	    int LA14_0 = input.LA(1);

            	    if ( (LA14_0==29) ) {
            	        alt14=1;
            	    }
            	    else if ( (LA14_0==30) ) {
            	        alt14=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 14, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt14) {
            	        case 1 :
            	            // InternalCQL.g:925:7: lv_op_2_1= '=='
            	            {
            	            lv_op_2_1=(Token)match(input,29,FOLLOW_18); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getEqualitiyAccess().getOpEqualsSignEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getEqualitiyRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:936:7: lv_op_2_2= '!='
            	            {
            	            lv_op_2_2=(Token)match(input,30,FOLLOW_18); 

            	            							newLeafNode(lv_op_2_2, grammarAccess.getEqualitiyAccess().getOpExclamationMarkEqualsSignKeyword_1_1_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getEqualitiyRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_2, null);
            	            						

            	            }
            	            break;

            	    }


            	    }


            	    }

            	    // InternalCQL.g:949:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCQL.g:950:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCQL.g:950:5: (lv_right_3_0= ruleComparison )
            	    // InternalCQL.g:951:6: lv_right_3_0= ruleComparison
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getRightComparisonParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_17);
            	    lv_right_3_0=ruleComparison();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getEqualitiyRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Comparison");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEqualitiy"


    // $ANTLR start "entryRuleComparison"
    // InternalCQL.g:973:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCQL.g:973:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCQL.g:974:2: iv_ruleComparison= ruleComparison EOF
            {
             newCompositeNode(grammarAccess.getComparisonRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleComparison=ruleComparison();

            state._fsp--;

             current =iv_ruleComparison; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleComparison"


    // $ANTLR start "ruleComparison"
    // InternalCQL.g:980:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
    public final EObject ruleComparison() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        Token lv_op_2_3=null;
        Token lv_op_2_4=null;
        EObject this_PlusOrMinus_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:986:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCQL.g:987:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCQL.g:987:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCQL.g:988:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_19);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:996:3: ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( ((LA17_0>=31 && LA17_0<=34)) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // InternalCQL.g:997:4: () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCQL.g:997:4: ()
            	    // InternalCQL.g:998:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:1004:4: ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) )
            	    // InternalCQL.g:1005:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    {
            	    // InternalCQL.g:1005:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    // InternalCQL.g:1006:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    {
            	    // InternalCQL.g:1006:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    int alt16=4;
            	    switch ( input.LA(1) ) {
            	    case 31:
            	        {
            	        alt16=1;
            	        }
            	        break;
            	    case 32:
            	        {
            	        alt16=2;
            	        }
            	        break;
            	    case 33:
            	        {
            	        alt16=3;
            	        }
            	        break;
            	    case 34:
            	        {
            	        alt16=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 16, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt16) {
            	        case 1 :
            	            // InternalCQL.g:1007:7: lv_op_2_1= '>='
            	            {
            	            lv_op_2_1=(Token)match(input,31,FOLLOW_20); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:1018:7: lv_op_2_2= '<='
            	            {
            	            lv_op_2_2=(Token)match(input,32,FOLLOW_20); 

            	            							newLeafNode(lv_op_2_2, grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_2, null);
            	            						

            	            }
            	            break;
            	        case 3 :
            	            // InternalCQL.g:1029:7: lv_op_2_3= '<'
            	            {
            	            lv_op_2_3=(Token)match(input,33,FOLLOW_20); 

            	            							newLeafNode(lv_op_2_3, grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_3, null);
            	            						

            	            }
            	            break;
            	        case 4 :
            	            // InternalCQL.g:1040:7: lv_op_2_4= '>'
            	            {
            	            lv_op_2_4=(Token)match(input,34,FOLLOW_20); 

            	            							newLeafNode(lv_op_2_4, grammarAccess.getComparisonAccess().getOpGreaterThanSignKeyword_1_1_0_3());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_4, null);
            	            						

            	            }
            	            break;

            	    }


            	    }


            	    }

            	    // InternalCQL.g:1053:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCQL.g:1054:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCQL.g:1054:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCQL.g:1055:6: lv_right_3_0= rulePlusOrMinus
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getRightPlusOrMinusParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_19);
            	    lv_right_3_0=rulePlusOrMinus();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getComparisonRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.PlusOrMinus");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleComparison"


    // $ANTLR start "entryRulePlusOrMinus"
    // InternalCQL.g:1077:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCQL.g:1077:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCQL.g:1078:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
            {
             newCompositeNode(grammarAccess.getPlusOrMinusRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePlusOrMinus=rulePlusOrMinus();

            state._fsp--;

             current =iv_rulePlusOrMinus; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePlusOrMinus"


    // $ANTLR start "rulePlusOrMinus"
    // InternalCQL.g:1084:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1090:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCQL.g:1091:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCQL.g:1091:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCQL.g:1092:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_21);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:1100:3: ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( ((LA19_0>=35 && LA19_0<=36)) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // InternalCQL.g:1101:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCQL.g:1101:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) )
            	    int alt18=2;
            	    int LA18_0 = input.LA(1);

            	    if ( (LA18_0==35) ) {
            	        alt18=1;
            	    }
            	    else if ( (LA18_0==36) ) {
            	        alt18=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 18, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt18) {
            	        case 1 :
            	            // InternalCQL.g:1102:5: ( () otherlv_2= '+' )
            	            {
            	            // InternalCQL.g:1102:5: ( () otherlv_2= '+' )
            	            // InternalCQL.g:1103:6: () otherlv_2= '+'
            	            {
            	            // InternalCQL.g:1103:6: ()
            	            // InternalCQL.g:1104:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_2=(Token)match(input,35,FOLLOW_22); 

            	            						newLeafNode(otherlv_2, grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1());
            	            					

            	            }


            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:1116:5: ( () otherlv_4= '-' )
            	            {
            	            // InternalCQL.g:1116:5: ( () otherlv_4= '-' )
            	            // InternalCQL.g:1117:6: () otherlv_4= '-'
            	            {
            	            // InternalCQL.g:1117:6: ()
            	            // InternalCQL.g:1118:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_4=(Token)match(input,36,FOLLOW_22); 

            	            						newLeafNode(otherlv_4, grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1());
            	            					

            	            }


            	            }
            	            break;

            	    }

            	    // InternalCQL.g:1130:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCQL.g:1131:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCQL.g:1131:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCQL.g:1132:6: lv_right_5_0= ruleMulOrDiv
            	    {

            	    						newCompositeNode(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_21);
            	    lv_right_5_0=ruleMulOrDiv();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getPlusOrMinusRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_5_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.MulOrDiv");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePlusOrMinus"


    // $ANTLR start "entryRuleMulOrDiv"
    // InternalCQL.g:1154:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCQL.g:1154:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCQL.g:1155:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
            {
             newCompositeNode(grammarAccess.getMulOrDivRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleMulOrDiv=ruleMulOrDiv();

            state._fsp--;

             current =iv_ruleMulOrDiv; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleMulOrDiv"


    // $ANTLR start "ruleMulOrDiv"
    // InternalCQL.g:1161:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1167:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCQL.g:1168:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCQL.g:1168:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCQL.g:1169:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_23);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:1177:3: ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( ((LA21_0>=37 && LA21_0<=38)) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // InternalCQL.g:1178:4: () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCQL.g:1178:4: ()
            	    // InternalCQL.g:1179:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:1185:4: ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) )
            	    // InternalCQL.g:1186:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    {
            	    // InternalCQL.g:1186:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    // InternalCQL.g:1187:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    {
            	    // InternalCQL.g:1187:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    int alt20=2;
            	    int LA20_0 = input.LA(1);

            	    if ( (LA20_0==37) ) {
            	        alt20=1;
            	    }
            	    else if ( (LA20_0==38) ) {
            	        alt20=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 20, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt20) {
            	        case 1 :
            	            // InternalCQL.g:1188:7: lv_op_2_1= '*'
            	            {
            	            lv_op_2_1=(Token)match(input,37,FOLLOW_24); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getMulOrDivRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:1199:7: lv_op_2_2= '/'
            	            {
            	            lv_op_2_2=(Token)match(input,38,FOLLOW_24); 

            	            							newLeafNode(lv_op_2_2, grammarAccess.getMulOrDivAccess().getOpSolidusKeyword_1_1_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getMulOrDivRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_2, null);
            	            						

            	            }
            	            break;

            	    }


            	    }


            	    }

            	    // InternalCQL.g:1212:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCQL.g:1213:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCQL.g:1213:5: (lv_right_3_0= rulePrimary )
            	    // InternalCQL.g:1214:6: lv_right_3_0= rulePrimary
            	    {

            	    						newCompositeNode(grammarAccess.getMulOrDivAccess().getRightPrimaryParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_23);
            	    lv_right_3_0=rulePrimary();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getMulOrDivRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Primary");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleMulOrDiv"


    // $ANTLR start "entryRulePrimary"
    // InternalCQL.g:1236:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCQL.g:1236:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCQL.g:1237:2: iv_rulePrimary= rulePrimary EOF
            {
             newCompositeNode(grammarAccess.getPrimaryRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePrimary=rulePrimary();

            state._fsp--;

             current =iv_rulePrimary; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePrimary"


    // $ANTLR start "rulePrimary"
    // InternalCQL.g:1243:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
    public final EObject rulePrimary() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_inner_2_0 = null;

        EObject lv_expression_6_0 = null;

        EObject this_Atomic_7 = null;



        	enterRule();

        try {
            // InternalCQL.g:1249:2: ( ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCQL.g:1250:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCQL.g:1250:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt22=3;
            switch ( input.LA(1) ) {
            case 13:
                {
                alt22=1;
                }
                break;
            case 39:
                {
                alt22=2;
                }
                break;
            case RULE_INT:
            case RULE_FLOAT_NUMBER:
            case RULE_STRING:
            case RULE_ID:
            case 15:
            case 16:
                {
                alt22=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;
            }

            switch (alt22) {
                case 1 :
                    // InternalCQL.g:1251:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    {
                    // InternalCQL.g:1251:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    // InternalCQL.g:1252:4: () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')'
                    {
                    // InternalCQL.g:1252:4: ()
                    // InternalCQL.g:1253:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,13,FOLLOW_25); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCQL.g:1263:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCQL.g:1264:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCQL.g:1264:5: (lv_inner_2_0= ruleExpression )
                    // InternalCQL.g:1265:6: lv_inner_2_0= ruleExpression
                    {

                    						newCompositeNode(grammarAccess.getPrimaryAccess().getInnerExpressionParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_6);
                    lv_inner_2_0=ruleExpression();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getPrimaryRule());
                    						}
                    						set(
                    							current,
                    							"inner",
                    							lv_inner_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Expression");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    otherlv_3=(Token)match(input,14,FOLLOW_2); 

                    				newLeafNode(otherlv_3, grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_0_3());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1288:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCQL.g:1288:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCQL.g:1289:4: () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCQL.g:1289:4: ()
                    // InternalCQL.g:1290:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,39,FOLLOW_26); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCQL.g:1300:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCQL.g:1301:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCQL.g:1301:5: (lv_expression_6_0= rulePrimary )
                    // InternalCQL.g:1302:6: lv_expression_6_0= rulePrimary
                    {

                    						newCompositeNode(grammarAccess.getPrimaryAccess().getExpressionPrimaryParserRuleCall_1_2_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_expression_6_0=rulePrimary();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getPrimaryRule());
                    						}
                    						set(
                    							current,
                    							"expression",
                    							lv_expression_6_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Primary");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:1321:3: this_Atomic_7= ruleAtomic
                    {

                    			newCompositeNode(grammarAccess.getPrimaryAccess().getAtomicParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_Atomic_7=ruleAtomic();

                    state._fsp--;


                    			current = this_Atomic_7;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePrimary"


    // $ANTLR start "entryRuleSelect_Statement"
    // InternalCQL.g:1333:1: entryRuleSelect_Statement returns [EObject current=null] : iv_ruleSelect_Statement= ruleSelect_Statement EOF ;
    public final EObject entryRuleSelect_Statement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelect_Statement = null;


        try {
            // InternalCQL.g:1333:57: (iv_ruleSelect_Statement= ruleSelect_Statement EOF )
            // InternalCQL.g:1334:2: iv_ruleSelect_Statement= ruleSelect_Statement EOF
            {
             newCompositeNode(grammarAccess.getSelect_StatementRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelect_Statement=ruleSelect_Statement();

            state._fsp--;

             current =iv_ruleSelect_Statement; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSelect_Statement"


    // $ANTLR start "ruleSelect_Statement"
    // InternalCQL.g:1340:1: ruleSelect_Statement returns [EObject current=null] : ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) otherlv_10= 'FROM' ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+ ) ) )+ {...}?) ) ) (otherlv_20= 'WHERE' ( (lv_predicates_21_0= ruleExpressionsModel ) ) )? (otherlv_22= 'GROUP' otherlv_23= 'BY' ( (lv_order_24_0= ruleAttribute ) )+ (otherlv_25= ',' ( (lv_order_26_0= ruleAttribute ) ) )* )? (otherlv_27= 'HAVING' ( (lv_having_28_0= ruleExpressionsModel ) ) )? ) ;
    public final EObject ruleSelect_Statement() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token lv_distinct_1_0=null;
        Token otherlv_2=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        Token otherlv_10=null;
        Token otherlv_14=null;
        Token otherlv_16=null;
        Token otherlv_18=null;
        Token otherlv_20=null;
        Token otherlv_22=null;
        Token otherlv_23=null;
        Token otherlv_25=null;
        Token otherlv_27=null;
        EObject lv_attributes_4_0 = null;

        EObject lv_aggregations_5_0 = null;

        EObject lv_attributes_7_0 = null;

        EObject lv_aggregations_9_0 = null;

        EObject lv_sources_12_0 = null;

        EObject lv_nested_13_0 = null;

        EObject lv_sources_15_0 = null;

        EObject lv_nested_17_0 = null;

        EObject lv_nestedAliases_19_0 = null;

        EObject lv_predicates_21_0 = null;

        EObject lv_order_24_0 = null;

        EObject lv_order_26_0 = null;

        EObject lv_having_28_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1346:2: ( ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) otherlv_10= 'FROM' ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+ ) ) )+ {...}?) ) ) (otherlv_20= 'WHERE' ( (lv_predicates_21_0= ruleExpressionsModel ) ) )? (otherlv_22= 'GROUP' otherlv_23= 'BY' ( (lv_order_24_0= ruleAttribute ) )+ (otherlv_25= ',' ( (lv_order_26_0= ruleAttribute ) ) )* )? (otherlv_27= 'HAVING' ( (lv_having_28_0= ruleExpressionsModel ) ) )? ) )
            // InternalCQL.g:1347:2: ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) otherlv_10= 'FROM' ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+ ) ) )+ {...}?) ) ) (otherlv_20= 'WHERE' ( (lv_predicates_21_0= ruleExpressionsModel ) ) )? (otherlv_22= 'GROUP' otherlv_23= 'BY' ( (lv_order_24_0= ruleAttribute ) )+ (otherlv_25= ',' ( (lv_order_26_0= ruleAttribute ) ) )* )? (otherlv_27= 'HAVING' ( (lv_having_28_0= ruleExpressionsModel ) ) )? )
            {
            // InternalCQL.g:1347:2: ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) otherlv_10= 'FROM' ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+ ) ) )+ {...}?) ) ) (otherlv_20= 'WHERE' ( (lv_predicates_21_0= ruleExpressionsModel ) ) )? (otherlv_22= 'GROUP' otherlv_23= 'BY' ( (lv_order_24_0= ruleAttribute ) )+ (otherlv_25= ',' ( (lv_order_26_0= ruleAttribute ) ) )* )? (otherlv_27= 'HAVING' ( (lv_having_28_0= ruleExpressionsModel ) ) )? )
            // InternalCQL.g:1348:3: ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) otherlv_10= 'FROM' ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+ ) ) )+ {...}?) ) ) (otherlv_20= 'WHERE' ( (lv_predicates_21_0= ruleExpressionsModel ) ) )? (otherlv_22= 'GROUP' otherlv_23= 'BY' ( (lv_order_24_0= ruleAttribute ) )+ (otherlv_25= ',' ( (lv_order_26_0= ruleAttribute ) ) )* )? (otherlv_27= 'HAVING' ( (lv_having_28_0= ruleExpressionsModel ) ) )?
            {
            // InternalCQL.g:1348:3: ( (lv_name_0_0= 'SELECT' ) )
            // InternalCQL.g:1349:4: (lv_name_0_0= 'SELECT' )
            {
            // InternalCQL.g:1349:4: (lv_name_0_0= 'SELECT' )
            // InternalCQL.g:1350:5: lv_name_0_0= 'SELECT'
            {
            lv_name_0_0=(Token)match(input,40,FOLLOW_27); 

            					newLeafNode(lv_name_0_0, grammarAccess.getSelect_StatementAccess().getNameSELECTKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSelect_StatementRule());
            					}
            					setWithLastConsumed(current, "name", lv_name_0_0, "SELECT");
            				

            }


            }

            // InternalCQL.g:1362:3: ( (lv_distinct_1_0= 'DISTINCT' ) )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==41) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // InternalCQL.g:1363:4: (lv_distinct_1_0= 'DISTINCT' )
                    {
                    // InternalCQL.g:1363:4: (lv_distinct_1_0= 'DISTINCT' )
                    // InternalCQL.g:1364:5: lv_distinct_1_0= 'DISTINCT'
                    {
                    lv_distinct_1_0=(Token)match(input,41,FOLLOW_28); 

                    					newLeafNode(lv_distinct_1_0, grammarAccess.getSelect_StatementAccess().getDistinctDISTINCTKeyword_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getSelect_StatementRule());
                    					}
                    					setWithLastConsumed(current, "distinct", lv_distinct_1_0, "DISTINCT");
                    				

                    }


                    }
                    break;

            }

            // InternalCQL.g:1376:3: (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) )
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==37) ) {
                alt29=1;
            }
            else if ( (LA29_0==RULE_ID||LA29_0==42) ) {
                alt29=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;
            }
            switch (alt29) {
                case 1 :
                    // InternalCQL.g:1377:4: otherlv_2= '*'
                    {
                    otherlv_2=(Token)match(input,37,FOLLOW_29); 

                    				newLeafNode(otherlv_2, grammarAccess.getSelect_StatementAccess().getAsteriskKeyword_2_0());
                    			

                    }
                    break;
                case 2 :
                    // InternalCQL.g:1382:4: ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) )
                    {
                    // InternalCQL.g:1382:4: ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) )
                    // InternalCQL.g:1383:5: ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) )
                    {
                    // InternalCQL.g:1383:5: ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) )
                    // InternalCQL.g:1384:6: ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?)
                    {
                     
                    					  getUnorderedGroupHelper().enter(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1());
                    					
                    // InternalCQL.g:1387:6: ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?)
                    // InternalCQL.g:1388:7: ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?
                    {
                    // InternalCQL.g:1388:7: ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+
                    int cnt28=0;
                    loop28:
                    do {
                        int alt28=4;
                        int LA28_0 = input.LA(1);

                        if ( LA28_0 == RULE_ID && getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1(), 0) ) {
                            alt28=1;
                        }
                        else if ( LA28_0 == 42 && ( getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1(), 1) || getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1(), 2) ) ) {
                            int LA28_3 = input.LA(2);

                            if ( LA28_3 == RULE_ID && ( getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1(), 1) || getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1(), 2) ) ) {
                                int LA28_4 = input.LA(3);

                                if ( ( LA28_4 == RULE_ID || LA28_4 == 19 || LA28_4 >= 42 && LA28_4 <= 43 ) && getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1(), 1) ) {
                                    alt28=2;
                                }
                                else if ( LA28_4 == 13 && getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1(), 2) ) {
                                    alt28=3;
                                }


                            }


                        }


                        switch (alt28) {
                    	case 1 :
                    	    // InternalCQL.g:1389:5: ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) )
                    	    {
                    	    // InternalCQL.g:1389:5: ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) )
                    	    // InternalCQL.g:1390:6: {...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ )
                    	    {
                    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1(), 0) ) {
                    	        throw new FailedPredicateException(input, "ruleSelect_Statement", "getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1(), 0)");
                    	    }
                    	    // InternalCQL.g:1390:116: ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ )
                    	    // InternalCQL.g:1391:7: ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+
                    	    {

                    	    							getUnorderedGroupHelper().select(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1(), 0);
                    	    						
                    	    // InternalCQL.g:1394:10: ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+
                    	    int cnt25=0;
                    	    loop25:
                    	    do {
                    	        int alt25=2;
                    	        int LA25_0 = input.LA(1);

                    	        if ( (LA25_0==RULE_ID) ) {
                    	            int LA25_2 = input.LA(2);

                    	            if ( ((true)) ) {
                    	                alt25=1;
                    	            }


                    	        }


                    	        switch (alt25) {
                    	    	case 1 :
                    	    	    // InternalCQL.g:1394:11: {...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) )
                    	    	    {
                    	    	    if ( !((true)) ) {
                    	    	        throw new FailedPredicateException(input, "ruleSelect_Statement", "true");
                    	    	    }
                    	    	    // InternalCQL.g:1394:20: ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) )
                    	    	    int alt24=2;
                    	    	    int LA24_0 = input.LA(1);

                    	    	    if ( (LA24_0==RULE_ID) ) {
                    	    	        int LA24_1 = input.LA(2);

                    	    	        if ( (LA24_1==RULE_ID||LA24_1==19||(LA24_1>=42 && LA24_1<=43)) ) {
                    	    	            alt24=1;
                    	    	        }
                    	    	        else if ( (LA24_1==13) ) {
                    	    	            alt24=2;
                    	    	        }
                    	    	        else {
                    	    	            NoViableAltException nvae =
                    	    	                new NoViableAltException("", 24, 1, input);

                    	    	            throw nvae;
                    	    	        }
                    	    	    }
                    	    	    else {
                    	    	        NoViableAltException nvae =
                    	    	            new NoViableAltException("", 24, 0, input);

                    	    	        throw nvae;
                    	    	    }
                    	    	    switch (alt24) {
                    	    	        case 1 :
                    	    	            // InternalCQL.g:1394:21: ( (lv_attributes_4_0= ruleAttribute ) )
                    	    	            {
                    	    	            // InternalCQL.g:1394:21: ( (lv_attributes_4_0= ruleAttribute ) )
                    	    	            // InternalCQL.g:1395:11: (lv_attributes_4_0= ruleAttribute )
                    	    	            {
                    	    	            // InternalCQL.g:1395:11: (lv_attributes_4_0= ruleAttribute )
                    	    	            // InternalCQL.g:1396:12: lv_attributes_4_0= ruleAttribute
                    	    	            {

                    	    	            												newCompositeNode(grammarAccess.getSelect_StatementAccess().getAttributesAttributeParserRuleCall_2_1_0_0_0());
                    	    	            											
                    	    	            pushFollow(FOLLOW_30);
                    	    	            lv_attributes_4_0=ruleAttribute();

                    	    	            state._fsp--;


                    	    	            												if (current==null) {
                    	    	            													current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
                    	    	            												}
                    	    	            												add(
                    	    	            													current,
                    	    	            													"attributes",
                    	    	            													lv_attributes_4_0,
                    	    	            													"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    	    	            												afterParserOrEnumRuleCall();
                    	    	            											

                    	    	            }


                    	    	            }


                    	    	            }
                    	    	            break;
                    	    	        case 2 :
                    	    	            // InternalCQL.g:1414:10: ( (lv_aggregations_5_0= ruleAggregation ) )
                    	    	            {
                    	    	            // InternalCQL.g:1414:10: ( (lv_aggregations_5_0= ruleAggregation ) )
                    	    	            // InternalCQL.g:1415:11: (lv_aggregations_5_0= ruleAggregation )
                    	    	            {
                    	    	            // InternalCQL.g:1415:11: (lv_aggregations_5_0= ruleAggregation )
                    	    	            // InternalCQL.g:1416:12: lv_aggregations_5_0= ruleAggregation
                    	    	            {

                    	    	            												newCompositeNode(grammarAccess.getSelect_StatementAccess().getAggregationsAggregationParserRuleCall_2_1_0_1_0());
                    	    	            											
                    	    	            pushFollow(FOLLOW_30);
                    	    	            lv_aggregations_5_0=ruleAggregation();

                    	    	            state._fsp--;


                    	    	            												if (current==null) {
                    	    	            													current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
                    	    	            												}
                    	    	            												add(
                    	    	            													current,
                    	    	            													"aggregations",
                    	    	            													lv_aggregations_5_0,
                    	    	            													"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Aggregation");
                    	    	            												afterParserOrEnumRuleCall();
                    	    	            											

                    	    	            }


                    	    	            }


                    	    	            }
                    	    	            break;

                    	    	    }


                    	    	    }
                    	    	    break;

                    	    	default :
                    	    	    if ( cnt25 >= 1 ) break loop25;
                    	                EarlyExitException eee =
                    	                    new EarlyExitException(25, input);
                    	                throw eee;
                    	        }
                    	        cnt25++;
                    	    } while (true);

                    	     
                    	    							getUnorderedGroupHelper().returnFromSelection(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1());
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalCQL.g:1439:5: ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) )
                    	    {
                    	    // InternalCQL.g:1439:5: ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) )
                    	    // InternalCQL.g:1440:6: {...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ )
                    	    {
                    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1(), 1) ) {
                    	        throw new FailedPredicateException(input, "ruleSelect_Statement", "getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1(), 1)");
                    	    }
                    	    // InternalCQL.g:1440:116: ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ )
                    	    // InternalCQL.g:1441:7: ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+
                    	    {

                    	    							getUnorderedGroupHelper().select(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1(), 1);
                    	    						
                    	    // InternalCQL.g:1444:10: ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+
                    	    int cnt26=0;
                    	    loop26:
                    	    do {
                    	        int alt26=2;
                    	        int LA26_0 = input.LA(1);

                    	        if ( (LA26_0==42) ) {
                    	            int LA26_2 = input.LA(2);

                    	            if ( ((true)) ) {
                    	                alt26=1;
                    	            }


                    	        }


                    	        switch (alt26) {
                    	    	case 1 :
                    	    	    // InternalCQL.g:1444:11: {...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) )
                    	    	    {
                    	    	    if ( !((true)) ) {
                    	    	        throw new FailedPredicateException(input, "ruleSelect_Statement", "true");
                    	    	    }
                    	    	    // InternalCQL.g:1444:20: (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) )
                    	    	    // InternalCQL.g:1444:21: otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) )
                    	    	    {
                    	    	    otherlv_6=(Token)match(input,42,FOLLOW_30); 

                    	    	    										newLeafNode(otherlv_6, grammarAccess.getSelect_StatementAccess().getCommaKeyword_2_1_1_0());
                    	    	    									
                    	    	    // InternalCQL.g:1448:10: ( (lv_attributes_7_0= ruleAttribute ) )
                    	    	    // InternalCQL.g:1449:11: (lv_attributes_7_0= ruleAttribute )
                    	    	    {
                    	    	    // InternalCQL.g:1449:11: (lv_attributes_7_0= ruleAttribute )
                    	    	    // InternalCQL.g:1450:12: lv_attributes_7_0= ruleAttribute
                    	    	    {

                    	    	    												newCompositeNode(grammarAccess.getSelect_StatementAccess().getAttributesAttributeParserRuleCall_2_1_1_1_0());
                    	    	    											
                    	    	    pushFollow(FOLLOW_30);
                    	    	    lv_attributes_7_0=ruleAttribute();

                    	    	    state._fsp--;


                    	    	    												if (current==null) {
                    	    	    													current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
                    	    	    												}
                    	    	    												add(
                    	    	    													current,
                    	    	    													"attributes",
                    	    	    													lv_attributes_7_0,
                    	    	    													"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    	    	    												afterParserOrEnumRuleCall();
                    	    	    											

                    	    	    }


                    	    	    }


                    	    	    }


                    	    	    }
                    	    	    break;

                    	    	default :
                    	    	    if ( cnt26 >= 1 ) break loop26;
                    	                EarlyExitException eee =
                    	                    new EarlyExitException(26, input);
                    	                throw eee;
                    	        }
                    	        cnt26++;
                    	    } while (true);

                    	     
                    	    							getUnorderedGroupHelper().returnFromSelection(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1());
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;
                    	case 3 :
                    	    // InternalCQL.g:1473:5: ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) )
                    	    {
                    	    // InternalCQL.g:1473:5: ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) )
                    	    // InternalCQL.g:1474:6: {...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ )
                    	    {
                    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1(), 2) ) {
                    	        throw new FailedPredicateException(input, "ruleSelect_Statement", "getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1(), 2)");
                    	    }
                    	    // InternalCQL.g:1474:116: ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ )
                    	    // InternalCQL.g:1475:7: ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+
                    	    {

                    	    							getUnorderedGroupHelper().select(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1(), 2);
                    	    						
                    	    // InternalCQL.g:1478:10: ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+
                    	    int cnt27=0;
                    	    loop27:
                    	    do {
                    	        int alt27=2;
                    	        int LA27_0 = input.LA(1);

                    	        if ( (LA27_0==42) ) {
                    	            int LA27_2 = input.LA(2);

                    	            if ( ((true)) ) {
                    	                alt27=1;
                    	            }


                    	        }


                    	        switch (alt27) {
                    	    	case 1 :
                    	    	    // InternalCQL.g:1478:11: {...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) )
                    	    	    {
                    	    	    if ( !((true)) ) {
                    	    	        throw new FailedPredicateException(input, "ruleSelect_Statement", "true");
                    	    	    }
                    	    	    // InternalCQL.g:1478:20: (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) )
                    	    	    // InternalCQL.g:1478:21: otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) )
                    	    	    {
                    	    	    otherlv_8=(Token)match(input,42,FOLLOW_31); 

                    	    	    										newLeafNode(otherlv_8, grammarAccess.getSelect_StatementAccess().getCommaKeyword_2_1_2_0());
                    	    	    									
                    	    	    // InternalCQL.g:1482:10: ( (lv_aggregations_9_0= ruleAggregation ) )
                    	    	    // InternalCQL.g:1483:11: (lv_aggregations_9_0= ruleAggregation )
                    	    	    {
                    	    	    // InternalCQL.g:1483:11: (lv_aggregations_9_0= ruleAggregation )
                    	    	    // InternalCQL.g:1484:12: lv_aggregations_9_0= ruleAggregation
                    	    	    {

                    	    	    												newCompositeNode(grammarAccess.getSelect_StatementAccess().getAggregationsAggregationParserRuleCall_2_1_2_1_0());
                    	    	    											
                    	    	    pushFollow(FOLLOW_30);
                    	    	    lv_aggregations_9_0=ruleAggregation();

                    	    	    state._fsp--;


                    	    	    												if (current==null) {
                    	    	    													current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
                    	    	    												}
                    	    	    												add(
                    	    	    													current,
                    	    	    													"aggregations",
                    	    	    													lv_aggregations_9_0,
                    	    	    													"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Aggregation");
                    	    	    												afterParserOrEnumRuleCall();
                    	    	    											

                    	    	    }


                    	    	    }


                    	    	    }


                    	    	    }
                    	    	    break;

                    	    	default :
                    	    	    if ( cnt27 >= 1 ) break loop27;
                    	                EarlyExitException eee =
                    	                    new EarlyExitException(27, input);
                    	                throw eee;
                    	        }
                    	        cnt27++;
                    	    } while (true);

                    	     
                    	    							getUnorderedGroupHelper().returnFromSelection(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1());
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt28 >= 1 ) break loop28;
                                EarlyExitException eee =
                                    new EarlyExitException(28, input);
                                throw eee;
                        }
                        cnt28++;
                    } while (true);

                    if ( ! getUnorderedGroupHelper().canLeave(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1()) ) {
                        throw new FailedPredicateException(input, "ruleSelect_Statement", "getUnorderedGroupHelper().canLeave(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1())");
                    }

                    }


                    }

                     
                    					  getUnorderedGroupHelper().leave(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_2_1());
                    					

                    }


                    }
                    break;

            }

            otherlv_10=(Token)match(input,43,FOLLOW_32); 

            			newLeafNode(otherlv_10, grammarAccess.getSelect_StatementAccess().getFROMKeyword_3());
            		
            // InternalCQL.g:1520:3: ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+ ) ) )+ {...}?) ) )
            // InternalCQL.g:1521:4: ( ( ( ({...}? => ( ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+ ) ) )+ {...}?) )
            {
            // InternalCQL.g:1521:4: ( ( ( ({...}? => ( ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+ ) ) )+ {...}?) )
            // InternalCQL.g:1522:5: ( ( ({...}? => ( ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+ ) ) )+ {...}?)
            {
             
            				  getUnorderedGroupHelper().enter(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4());
            				
            // InternalCQL.g:1525:5: ( ( ({...}? => ( ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+ ) ) )+ {...}?)
            // InternalCQL.g:1526:6: ( ({...}? => ( ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+ ) ) )+ {...}?
            {
            // InternalCQL.g:1526:6: ( ({...}? => ( ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+ ) ) )+
            int cnt35=0;
            loop35:
            do {
                int alt35=4;
                int LA35_0 = input.LA(1);

                if ( ( LA35_0 == RULE_ID || LA35_0 == 13 ) && getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4(), 0) ) {
                    alt35=1;
                }
                else if ( LA35_0 == 42 && ( getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4(), 2) || getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4(), 1) ) ) {
                    int LA35_3 = input.LA(2);

                    if ( LA35_3 == 13 && getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4(), 2) ) {
                        alt35=3;
                    }
                    else if ( LA35_3 == RULE_ID && getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4(), 1) ) {
                        alt35=2;
                    }


                }


                switch (alt35) {
            	case 1 :
            	    // InternalCQL.g:1527:4: ({...}? => ( ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+ ) )
            	    {
            	    // InternalCQL.g:1527:4: ({...}? => ( ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+ ) )
            	    // InternalCQL.g:1528:5: {...}? => ( ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+ )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4(), 0) ) {
            	        throw new FailedPredicateException(input, "ruleSelect_Statement", "getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4(), 0)");
            	    }
            	    // InternalCQL.g:1528:113: ( ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+ )
            	    // InternalCQL.g:1529:6: ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+
            	    {

            	    						getUnorderedGroupHelper().select(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4(), 0);
            	    					
            	    // InternalCQL.g:1532:9: ({...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) ) )+
            	    int cnt31=0;
            	    loop31:
            	    do {
            	        int alt31=2;
            	        int LA31_0 = input.LA(1);

            	        if ( (LA31_0==RULE_ID) ) {
            	            int LA31_2 = input.LA(2);

            	            if ( ((true)) ) {
            	                alt31=1;
            	            }


            	        }
            	        else if ( (LA31_0==13) ) {
            	            int LA31_3 = input.LA(2);

            	            if ( ((true)) ) {
            	                alt31=1;
            	            }


            	        }


            	        switch (alt31) {
            	    	case 1 :
            	    	    // InternalCQL.g:1532:10: {...}? => ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) )
            	    	    {
            	    	    if ( !((true)) ) {
            	    	        throw new FailedPredicateException(input, "ruleSelect_Statement", "true");
            	    	    }
            	    	    // InternalCQL.g:1532:19: ( ( (lv_sources_12_0= ruleSource ) ) | ( (lv_nested_13_0= ruleNested_Statement ) ) )
            	    	    int alt30=2;
            	    	    int LA30_0 = input.LA(1);

            	    	    if ( (LA30_0==RULE_ID) ) {
            	    	        alt30=1;
            	    	    }
            	    	    else if ( (LA30_0==13) ) {
            	    	        alt30=2;
            	    	    }
            	    	    else {
            	    	        NoViableAltException nvae =
            	    	            new NoViableAltException("", 30, 0, input);

            	    	        throw nvae;
            	    	    }
            	    	    switch (alt30) {
            	    	        case 1 :
            	    	            // InternalCQL.g:1532:20: ( (lv_sources_12_0= ruleSource ) )
            	    	            {
            	    	            // InternalCQL.g:1532:20: ( (lv_sources_12_0= ruleSource ) )
            	    	            // InternalCQL.g:1533:10: (lv_sources_12_0= ruleSource )
            	    	            {
            	    	            // InternalCQL.g:1533:10: (lv_sources_12_0= ruleSource )
            	    	            // InternalCQL.g:1534:11: lv_sources_12_0= ruleSource
            	    	            {

            	    	            											newCompositeNode(grammarAccess.getSelect_StatementAccess().getSourcesSourceParserRuleCall_4_0_0_0());
            	    	            										
            	    	            pushFollow(FOLLOW_33);
            	    	            lv_sources_12_0=ruleSource();

            	    	            state._fsp--;


            	    	            											if (current==null) {
            	    	            												current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
            	    	            											}
            	    	            											add(
            	    	            												current,
            	    	            												"sources",
            	    	            												lv_sources_12_0,
            	    	            												"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Source");
            	    	            											afterParserOrEnumRuleCall();
            	    	            										

            	    	            }


            	    	            }


            	    	            }
            	    	            break;
            	    	        case 2 :
            	    	            // InternalCQL.g:1552:9: ( (lv_nested_13_0= ruleNested_Statement ) )
            	    	            {
            	    	            // InternalCQL.g:1552:9: ( (lv_nested_13_0= ruleNested_Statement ) )
            	    	            // InternalCQL.g:1553:10: (lv_nested_13_0= ruleNested_Statement )
            	    	            {
            	    	            // InternalCQL.g:1553:10: (lv_nested_13_0= ruleNested_Statement )
            	    	            // InternalCQL.g:1554:11: lv_nested_13_0= ruleNested_Statement
            	    	            {

            	    	            											newCompositeNode(grammarAccess.getSelect_StatementAccess().getNestedNested_StatementParserRuleCall_4_0_1_0());
            	    	            										
            	    	            pushFollow(FOLLOW_33);
            	    	            lv_nested_13_0=ruleNested_Statement();

            	    	            state._fsp--;


            	    	            											if (current==null) {
            	    	            												current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
            	    	            											}
            	    	            											add(
            	    	            												current,
            	    	            												"nested",
            	    	            												lv_nested_13_0,
            	    	            												"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Nested_Statement");
            	    	            											afterParserOrEnumRuleCall();
            	    	            										

            	    	            }


            	    	            }


            	    	            }
            	    	            break;

            	    	    }


            	    	    }
            	    	    break;

            	    	default :
            	    	    if ( cnt31 >= 1 ) break loop31;
            	                EarlyExitException eee =
            	                    new EarlyExitException(31, input);
            	                throw eee;
            	        }
            	        cnt31++;
            	    } while (true);

            	     
            	    						getUnorderedGroupHelper().returnFromSelection(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4());
            	    					

            	    }


            	    }


            	    }
            	    break;
            	case 2 :
            	    // InternalCQL.g:1577:4: ({...}? => ( ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+ ) )
            	    {
            	    // InternalCQL.g:1577:4: ({...}? => ( ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+ ) )
            	    // InternalCQL.g:1578:5: {...}? => ( ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+ )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4(), 1) ) {
            	        throw new FailedPredicateException(input, "ruleSelect_Statement", "getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4(), 1)");
            	    }
            	    // InternalCQL.g:1578:113: ( ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+ )
            	    // InternalCQL.g:1579:6: ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+
            	    {

            	    						getUnorderedGroupHelper().select(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4(), 1);
            	    					
            	    // InternalCQL.g:1582:9: ({...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) ) )+
            	    int cnt32=0;
            	    loop32:
            	    do {
            	        int alt32=2;
            	        int LA32_0 = input.LA(1);

            	        if ( (LA32_0==42) ) {
            	            int LA32_2 = input.LA(2);

            	            if ( ((true)) ) {
            	                alt32=1;
            	            }


            	        }


            	        switch (alt32) {
            	    	case 1 :
            	    	    // InternalCQL.g:1582:10: {...}? => (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )
            	    	    {
            	    	    if ( !((true)) ) {
            	    	        throw new FailedPredicateException(input, "ruleSelect_Statement", "true");
            	    	    }
            	    	    // InternalCQL.g:1582:19: (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )
            	    	    // InternalCQL.g:1582:20: otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) )
            	    	    {
            	    	    otherlv_14=(Token)match(input,42,FOLLOW_11); 

            	    	    									newLeafNode(otherlv_14, grammarAccess.getSelect_StatementAccess().getCommaKeyword_4_1_0());
            	    	    								
            	    	    // InternalCQL.g:1586:9: ( (lv_sources_15_0= ruleSource ) )
            	    	    // InternalCQL.g:1587:10: (lv_sources_15_0= ruleSource )
            	    	    {
            	    	    // InternalCQL.g:1587:10: (lv_sources_15_0= ruleSource )
            	    	    // InternalCQL.g:1588:11: lv_sources_15_0= ruleSource
            	    	    {

            	    	    											newCompositeNode(grammarAccess.getSelect_StatementAccess().getSourcesSourceParserRuleCall_4_1_1_0());
            	    	    										
            	    	    pushFollow(FOLLOW_33);
            	    	    lv_sources_15_0=ruleSource();

            	    	    state._fsp--;


            	    	    											if (current==null) {
            	    	    												current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
            	    	    											}
            	    	    											add(
            	    	    												current,
            	    	    												"sources",
            	    	    												lv_sources_15_0,
            	    	    												"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Source");
            	    	    											afterParserOrEnumRuleCall();
            	    	    										

            	    	    }


            	    	    }


            	    	    }


            	    	    }
            	    	    break;

            	    	default :
            	    	    if ( cnt32 >= 1 ) break loop32;
            	                EarlyExitException eee =
            	                    new EarlyExitException(32, input);
            	                throw eee;
            	        }
            	        cnt32++;
            	    } while (true);

            	     
            	    						getUnorderedGroupHelper().returnFromSelection(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4());
            	    					

            	    }


            	    }


            	    }
            	    break;
            	case 3 :
            	    // InternalCQL.g:1611:4: ({...}? => ( ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+ ) )
            	    {
            	    // InternalCQL.g:1611:4: ({...}? => ( ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+ ) )
            	    // InternalCQL.g:1612:5: {...}? => ( ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+ )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4(), 2) ) {
            	        throw new FailedPredicateException(input, "ruleSelect_Statement", "getUnorderedGroupHelper().canSelect(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4(), 2)");
            	    }
            	    // InternalCQL.g:1612:113: ( ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+ )
            	    // InternalCQL.g:1613:6: ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+
            	    {

            	    						getUnorderedGroupHelper().select(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4(), 2);
            	    					
            	    // InternalCQL.g:1616:9: ({...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) ) )+
            	    int cnt34=0;
            	    loop34:
            	    do {
            	        int alt34=2;
            	        int LA34_0 = input.LA(1);

            	        if ( (LA34_0==42) ) {
            	            int LA34_2 = input.LA(2);

            	            if ( ((true)) ) {
            	                alt34=1;
            	            }


            	        }


            	        switch (alt34) {
            	    	case 1 :
            	    	    // InternalCQL.g:1616:10: {...}? => (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) )
            	    	    {
            	    	    if ( !((true)) ) {
            	    	        throw new FailedPredicateException(input, "ruleSelect_Statement", "true");
            	    	    }
            	    	    // InternalCQL.g:1616:19: (otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) ) )
            	    	    // InternalCQL.g:1616:20: otherlv_16= ',' ( (lv_nested_17_0= ruleNested_Statement ) ) (otherlv_18= 'AS' )? ( (lv_nestedAliases_19_0= ruleAlias ) )
            	    	    {
            	    	    otherlv_16=(Token)match(input,42,FOLLOW_34); 

            	    	    									newLeafNode(otherlv_16, grammarAccess.getSelect_StatementAccess().getCommaKeyword_4_2_0());
            	    	    								
            	    	    // InternalCQL.g:1620:9: ( (lv_nested_17_0= ruleNested_Statement ) )
            	    	    // InternalCQL.g:1621:10: (lv_nested_17_0= ruleNested_Statement )
            	    	    {
            	    	    // InternalCQL.g:1621:10: (lv_nested_17_0= ruleNested_Statement )
            	    	    // InternalCQL.g:1622:11: lv_nested_17_0= ruleNested_Statement
            	    	    {

            	    	    											newCompositeNode(grammarAccess.getSelect_StatementAccess().getNestedNested_StatementParserRuleCall_4_2_1_0());
            	    	    										
            	    	    pushFollow(FOLLOW_35);
            	    	    lv_nested_17_0=ruleNested_Statement();

            	    	    state._fsp--;


            	    	    											if (current==null) {
            	    	    												current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
            	    	    											}
            	    	    											add(
            	    	    												current,
            	    	    												"nested",
            	    	    												lv_nested_17_0,
            	    	    												"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Nested_Statement");
            	    	    											afterParserOrEnumRuleCall();
            	    	    										

            	    	    }


            	    	    }

            	    	    // InternalCQL.g:1639:9: (otherlv_18= 'AS' )?
            	    	    int alt33=2;
            	    	    int LA33_0 = input.LA(1);

            	    	    if ( (LA33_0==19) ) {
            	    	        alt33=1;
            	    	    }
            	    	    switch (alt33) {
            	    	        case 1 :
            	    	            // InternalCQL.g:1640:10: otherlv_18= 'AS'
            	    	            {
            	    	            otherlv_18=(Token)match(input,19,FOLLOW_11); 

            	    	            										newLeafNode(otherlv_18, grammarAccess.getSelect_StatementAccess().getASKeyword_4_2_2());
            	    	            									

            	    	            }
            	    	            break;

            	    	    }

            	    	    // InternalCQL.g:1645:9: ( (lv_nestedAliases_19_0= ruleAlias ) )
            	    	    // InternalCQL.g:1646:10: (lv_nestedAliases_19_0= ruleAlias )
            	    	    {
            	    	    // InternalCQL.g:1646:10: (lv_nestedAliases_19_0= ruleAlias )
            	    	    // InternalCQL.g:1647:11: lv_nestedAliases_19_0= ruleAlias
            	    	    {

            	    	    											newCompositeNode(grammarAccess.getSelect_StatementAccess().getNestedAliasesAliasParserRuleCall_4_2_3_0());
            	    	    										
            	    	    pushFollow(FOLLOW_33);
            	    	    lv_nestedAliases_19_0=ruleAlias();

            	    	    state._fsp--;


            	    	    											if (current==null) {
            	    	    												current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
            	    	    											}
            	    	    											add(
            	    	    												current,
            	    	    												"nestedAliases",
            	    	    												lv_nestedAliases_19_0,
            	    	    												"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Alias");
            	    	    											afterParserOrEnumRuleCall();
            	    	    										

            	    	    }


            	    	    }


            	    	    }


            	    	    }
            	    	    break;

            	    	default :
            	    	    if ( cnt34 >= 1 ) break loop34;
            	                EarlyExitException eee =
            	                    new EarlyExitException(34, input);
            	                throw eee;
            	        }
            	        cnt34++;
            	    } while (true);

            	     
            	    						getUnorderedGroupHelper().returnFromSelection(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4());
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt35 >= 1 ) break loop35;
                        EarlyExitException eee =
                            new EarlyExitException(35, input);
                        throw eee;
                }
                cnt35++;
            } while (true);

            if ( ! getUnorderedGroupHelper().canLeave(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4()) ) {
                throw new FailedPredicateException(input, "ruleSelect_Statement", "getUnorderedGroupHelper().canLeave(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4())");
            }

            }


            }

             
            				  getUnorderedGroupHelper().leave(grammarAccess.getSelect_StatementAccess().getUnorderedGroup_4());
            				

            }

            // InternalCQL.g:1678:3: (otherlv_20= 'WHERE' ( (lv_predicates_21_0= ruleExpressionsModel ) ) )?
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==44) ) {
                alt36=1;
            }
            switch (alt36) {
                case 1 :
                    // InternalCQL.g:1679:4: otherlv_20= 'WHERE' ( (lv_predicates_21_0= ruleExpressionsModel ) )
                    {
                    otherlv_20=(Token)match(input,44,FOLLOW_26); 

                    				newLeafNode(otherlv_20, grammarAccess.getSelect_StatementAccess().getWHEREKeyword_5_0());
                    			
                    // InternalCQL.g:1683:4: ( (lv_predicates_21_0= ruleExpressionsModel ) )
                    // InternalCQL.g:1684:5: (lv_predicates_21_0= ruleExpressionsModel )
                    {
                    // InternalCQL.g:1684:5: (lv_predicates_21_0= ruleExpressionsModel )
                    // InternalCQL.g:1685:6: lv_predicates_21_0= ruleExpressionsModel
                    {

                    						newCompositeNode(grammarAccess.getSelect_StatementAccess().getPredicatesExpressionsModelParserRuleCall_5_1_0());
                    					
                    pushFollow(FOLLOW_36);
                    lv_predicates_21_0=ruleExpressionsModel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
                    						}
                    						set(
                    							current,
                    							"predicates",
                    							lv_predicates_21_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionsModel");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:1703:3: (otherlv_22= 'GROUP' otherlv_23= 'BY' ( (lv_order_24_0= ruleAttribute ) )+ (otherlv_25= ',' ( (lv_order_26_0= ruleAttribute ) ) )* )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==45) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // InternalCQL.g:1704:4: otherlv_22= 'GROUP' otherlv_23= 'BY' ( (lv_order_24_0= ruleAttribute ) )+ (otherlv_25= ',' ( (lv_order_26_0= ruleAttribute ) ) )*
                    {
                    otherlv_22=(Token)match(input,45,FOLLOW_37); 

                    				newLeafNode(otherlv_22, grammarAccess.getSelect_StatementAccess().getGROUPKeyword_6_0());
                    			
                    otherlv_23=(Token)match(input,46,FOLLOW_38); 

                    				newLeafNode(otherlv_23, grammarAccess.getSelect_StatementAccess().getBYKeyword_6_1());
                    			
                    // InternalCQL.g:1712:4: ( (lv_order_24_0= ruleAttribute ) )+
                    int cnt37=0;
                    loop37:
                    do {
                        int alt37=2;
                        int LA37_0 = input.LA(1);

                        if ( (LA37_0==RULE_ID) ) {
                            alt37=1;
                        }


                        switch (alt37) {
                    	case 1 :
                    	    // InternalCQL.g:1713:5: (lv_order_24_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:1713:5: (lv_order_24_0= ruleAttribute )
                    	    // InternalCQL.g:1714:6: lv_order_24_0= ruleAttribute
                    	    {

                    	    						newCompositeNode(grammarAccess.getSelect_StatementAccess().getOrderAttributeParserRuleCall_6_2_0());
                    	    					
                    	    pushFollow(FOLLOW_39);
                    	    lv_order_24_0=ruleAttribute();

                    	    state._fsp--;


                    	    						if (current==null) {
                    	    							current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
                    	    						}
                    	    						add(
                    	    							current,
                    	    							"order",
                    	    							lv_order_24_0,
                    	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    	    						afterParserOrEnumRuleCall();
                    	    					

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt37 >= 1 ) break loop37;
                                EarlyExitException eee =
                                    new EarlyExitException(37, input);
                                throw eee;
                        }
                        cnt37++;
                    } while (true);

                    // InternalCQL.g:1731:4: (otherlv_25= ',' ( (lv_order_26_0= ruleAttribute ) ) )*
                    loop38:
                    do {
                        int alt38=2;
                        int LA38_0 = input.LA(1);

                        if ( (LA38_0==42) ) {
                            alt38=1;
                        }


                        switch (alt38) {
                    	case 1 :
                    	    // InternalCQL.g:1732:5: otherlv_25= ',' ( (lv_order_26_0= ruleAttribute ) )
                    	    {
                    	    otherlv_25=(Token)match(input,42,FOLLOW_38); 

                    	    					newLeafNode(otherlv_25, grammarAccess.getSelect_StatementAccess().getCommaKeyword_6_3_0());
                    	    				
                    	    // InternalCQL.g:1736:5: ( (lv_order_26_0= ruleAttribute ) )
                    	    // InternalCQL.g:1737:6: (lv_order_26_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:1737:6: (lv_order_26_0= ruleAttribute )
                    	    // InternalCQL.g:1738:7: lv_order_26_0= ruleAttribute
                    	    {

                    	    							newCompositeNode(grammarAccess.getSelect_StatementAccess().getOrderAttributeParserRuleCall_6_3_1_0());
                    	    						
                    	    pushFollow(FOLLOW_40);
                    	    lv_order_26_0=ruleAttribute();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"order",
                    	    								lv_order_26_0,
                    	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    	    							afterParserOrEnumRuleCall();
                    	    						

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

            // InternalCQL.g:1757:3: (otherlv_27= 'HAVING' ( (lv_having_28_0= ruleExpressionsModel ) ) )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==47) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // InternalCQL.g:1758:4: otherlv_27= 'HAVING' ( (lv_having_28_0= ruleExpressionsModel ) )
                    {
                    otherlv_27=(Token)match(input,47,FOLLOW_26); 

                    				newLeafNode(otherlv_27, grammarAccess.getSelect_StatementAccess().getHAVINGKeyword_7_0());
                    			
                    // InternalCQL.g:1762:4: ( (lv_having_28_0= ruleExpressionsModel ) )
                    // InternalCQL.g:1763:5: (lv_having_28_0= ruleExpressionsModel )
                    {
                    // InternalCQL.g:1763:5: (lv_having_28_0= ruleExpressionsModel )
                    // InternalCQL.g:1764:6: lv_having_28_0= ruleExpressionsModel
                    {

                    						newCompositeNode(grammarAccess.getSelect_StatementAccess().getHavingExpressionsModelParserRuleCall_7_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_having_28_0=ruleExpressionsModel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
                    						}
                    						set(
                    							current,
                    							"having",
                    							lv_having_28_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionsModel");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSelect_Statement"


    // $ANTLR start "entryRuleAggregation"
    // InternalCQL.g:1786:1: entryRuleAggregation returns [EObject current=null] : iv_ruleAggregation= ruleAggregation EOF ;
    public final EObject entryRuleAggregation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregation = null;


        try {
            // InternalCQL.g:1786:52: (iv_ruleAggregation= ruleAggregation EOF )
            // InternalCQL.g:1787:2: iv_ruleAggregation= ruleAggregation EOF
            {
             newCompositeNode(grammarAccess.getAggregationRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAggregation=ruleAggregation();

            state._fsp--;

             current =iv_ruleAggregation; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAggregation"


    // $ANTLR start "ruleAggregation"
    // InternalCQL.g:1793:1: ruleAggregation returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttribute ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAggregation() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        EObject lv_attribute_2_0 = null;

        EObject lv_alias_5_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1799:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttribute ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:1800:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttribute ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:1800:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttribute ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? )
            // InternalCQL.g:1801:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttribute ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:1801:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:1802:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:1802:4: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:1803:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_41); 

            					newLeafNode(lv_name_0_0, grammarAccess.getAggregationAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAggregationRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,13,FOLLOW_25); 

            			newLeafNode(otherlv_1, grammarAccess.getAggregationAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQL.g:1823:3: ( (lv_attribute_2_0= ruleAttribute ) )
            // InternalCQL.g:1824:4: (lv_attribute_2_0= ruleAttribute )
            {
            // InternalCQL.g:1824:4: (lv_attribute_2_0= ruleAttribute )
            // InternalCQL.g:1825:5: lv_attribute_2_0= ruleAttribute
            {

            					newCompositeNode(grammarAccess.getAggregationAccess().getAttributeAttributeParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_6);
            lv_attribute_2_0=ruleAttribute();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAggregationRule());
            					}
            					set(
            						current,
            						"attribute",
            						lv_attribute_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_3=(Token)match(input,14,FOLLOW_10); 

            			newLeafNode(otherlv_3, grammarAccess.getAggregationAccess().getRightParenthesisKeyword_3());
            		
            // InternalCQL.g:1846:3: (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )?
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( (LA41_0==19) ) {
                alt41=1;
            }
            switch (alt41) {
                case 1 :
                    // InternalCQL.g:1847:4: otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) )
                    {
                    otherlv_4=(Token)match(input,19,FOLLOW_11); 

                    				newLeafNode(otherlv_4, grammarAccess.getAggregationAccess().getASKeyword_4_0());
                    			
                    // InternalCQL.g:1851:4: ( (lv_alias_5_0= ruleAlias ) )
                    // InternalCQL.g:1852:5: (lv_alias_5_0= ruleAlias )
                    {
                    // InternalCQL.g:1852:5: (lv_alias_5_0= ruleAlias )
                    // InternalCQL.g:1853:6: lv_alias_5_0= ruleAlias
                    {

                    						newCompositeNode(grammarAccess.getAggregationAccess().getAliasAliasParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_alias_5_0=ruleAlias();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getAggregationRule());
                    						}
                    						set(
                    							current,
                    							"alias",
                    							lv_alias_5_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Alias");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAggregation"


    // $ANTLR start "entryRuleAlias"
    // InternalCQL.g:1875:1: entryRuleAlias returns [EObject current=null] : iv_ruleAlias= ruleAlias EOF ;
    public final EObject entryRuleAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAlias = null;


        try {
            // InternalCQL.g:1875:46: (iv_ruleAlias= ruleAlias EOF )
            // InternalCQL.g:1876:2: iv_ruleAlias= ruleAlias EOF
            {
             newCompositeNode(grammarAccess.getAliasRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAlias=ruleAlias();

            state._fsp--;

             current =iv_ruleAlias; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAlias"


    // $ANTLR start "ruleAlias"
    // InternalCQL.g:1882:1: ruleAlias returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQL.g:1888:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQL.g:1889:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQL.g:1889:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:1890:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:1890:3: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:1891:4: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            				newLeafNode(lv_name_0_0, grammarAccess.getAliasAccess().getNameIDTerminalRuleCall_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getAliasRule());
            				}
            				setWithLastConsumed(
            					current,
            					"name",
            					lv_name_0_0,
            					"org.eclipse.xtext.common.Terminals.ID");
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAlias"


    // $ANTLR start "entryRuleCreate_Statement"
    // InternalCQL.g:1910:1: entryRuleCreate_Statement returns [EObject current=null] : iv_ruleCreate_Statement= ruleCreate_Statement EOF ;
    public final EObject entryRuleCreate_Statement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreate_Statement = null;


        try {
            // InternalCQL.g:1910:57: (iv_ruleCreate_Statement= ruleCreate_Statement EOF )
            // InternalCQL.g:1911:2: iv_ruleCreate_Statement= ruleCreate_Statement EOF
            {
             newCompositeNode(grammarAccess.getCreate_StatementRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreate_Statement=ruleCreate_Statement();

            state._fsp--;

             current =iv_ruleCreate_Statement; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCreate_Statement"


    // $ANTLR start "ruleCreate_Statement"
    // InternalCQL.g:1917:1: ruleCreate_Statement returns [EObject current=null] : ( (otherlv_0= 'CREATE' | otherlv_1= 'ATTACH' ) ( ( (lv_channelformat_2_0= ruleChannelFormat ) ) | ( (lv_accessframework_3_0= ruleAccessFramework ) ) ) ) ;
    public final EObject ruleCreate_Statement() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        EObject lv_channelformat_2_0 = null;

        EObject lv_accessframework_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1923:2: ( ( (otherlv_0= 'CREATE' | otherlv_1= 'ATTACH' ) ( ( (lv_channelformat_2_0= ruleChannelFormat ) ) | ( (lv_accessframework_3_0= ruleAccessFramework ) ) ) ) )
            // InternalCQL.g:1924:2: ( (otherlv_0= 'CREATE' | otherlv_1= 'ATTACH' ) ( ( (lv_channelformat_2_0= ruleChannelFormat ) ) | ( (lv_accessframework_3_0= ruleAccessFramework ) ) ) )
            {
            // InternalCQL.g:1924:2: ( (otherlv_0= 'CREATE' | otherlv_1= 'ATTACH' ) ( ( (lv_channelformat_2_0= ruleChannelFormat ) ) | ( (lv_accessframework_3_0= ruleAccessFramework ) ) ) )
            // InternalCQL.g:1925:3: (otherlv_0= 'CREATE' | otherlv_1= 'ATTACH' ) ( ( (lv_channelformat_2_0= ruleChannelFormat ) ) | ( (lv_accessframework_3_0= ruleAccessFramework ) ) )
            {
            // InternalCQL.g:1925:3: (otherlv_0= 'CREATE' | otherlv_1= 'ATTACH' )
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==48) ) {
                alt42=1;
            }
            else if ( (LA42_0==49) ) {
                alt42=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 42, 0, input);

                throw nvae;
            }
            switch (alt42) {
                case 1 :
                    // InternalCQL.g:1926:4: otherlv_0= 'CREATE'
                    {
                    otherlv_0=(Token)match(input,48,FOLLOW_42); 

                    				newLeafNode(otherlv_0, grammarAccess.getCreate_StatementAccess().getCREATEKeyword_0_0());
                    			

                    }
                    break;
                case 2 :
                    // InternalCQL.g:1931:4: otherlv_1= 'ATTACH'
                    {
                    otherlv_1=(Token)match(input,49,FOLLOW_42); 

                    				newLeafNode(otherlv_1, grammarAccess.getCreate_StatementAccess().getATTACHKeyword_0_1());
                    			

                    }
                    break;

            }

            // InternalCQL.g:1936:3: ( ( (lv_channelformat_2_0= ruleChannelFormat ) ) | ( (lv_accessframework_3_0= ruleAccessFramework ) ) )
            int alt43=2;
            alt43 = dfa43.predict(input);
            switch (alt43) {
                case 1 :
                    // InternalCQL.g:1937:4: ( (lv_channelformat_2_0= ruleChannelFormat ) )
                    {
                    // InternalCQL.g:1937:4: ( (lv_channelformat_2_0= ruleChannelFormat ) )
                    // InternalCQL.g:1938:5: (lv_channelformat_2_0= ruleChannelFormat )
                    {
                    // InternalCQL.g:1938:5: (lv_channelformat_2_0= ruleChannelFormat )
                    // InternalCQL.g:1939:6: lv_channelformat_2_0= ruleChannelFormat
                    {

                    						newCompositeNode(grammarAccess.getCreate_StatementAccess().getChannelformatChannelFormatParserRuleCall_1_0_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_channelformat_2_0=ruleChannelFormat();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCreate_StatementRule());
                    						}
                    						set(
                    							current,
                    							"channelformat",
                    							lv_channelformat_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ChannelFormat");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1957:4: ( (lv_accessframework_3_0= ruleAccessFramework ) )
                    {
                    // InternalCQL.g:1957:4: ( (lv_accessframework_3_0= ruleAccessFramework ) )
                    // InternalCQL.g:1958:5: (lv_accessframework_3_0= ruleAccessFramework )
                    {
                    // InternalCQL.g:1958:5: (lv_accessframework_3_0= ruleAccessFramework )
                    // InternalCQL.g:1959:6: lv_accessframework_3_0= ruleAccessFramework
                    {

                    						newCompositeNode(grammarAccess.getCreate_StatementAccess().getAccessframeworkAccessFrameworkParserRuleCall_1_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_accessframework_3_0=ruleAccessFramework();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCreate_StatementRule());
                    						}
                    						set(
                    							current,
                    							"accessframework",
                    							lv_accessframework_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AccessFramework");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCreate_Statement"


    // $ANTLR start "entryRuleAccessFramework"
    // InternalCQL.g:1981:1: entryRuleAccessFramework returns [EObject current=null] : iv_ruleAccessFramework= ruleAccessFramework EOF ;
    public final EObject entryRuleAccessFramework() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAccessFramework = null;


        try {
            // InternalCQL.g:1981:56: (iv_ruleAccessFramework= ruleAccessFramework EOF )
            // InternalCQL.g:1982:2: iv_ruleAccessFramework= ruleAccessFramework EOF
            {
             newCompositeNode(grammarAccess.getAccessFrameworkRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAccessFramework=ruleAccessFramework();

            state._fsp--;

             current =iv_ruleAccessFramework; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAccessFramework"


    // $ANTLR start "ruleAccessFramework"
    // InternalCQL.g:1988:1: ruleAccessFramework returns [EObject current=null] : ( ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) ) ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'WRAPPER' ( (lv_wrapper_10_0= RULE_STRING ) ) otherlv_11= 'PROTOCOL' ( (lv_protocol_12_0= RULE_STRING ) ) otherlv_13= 'TRANSPORT' ( (lv_transport_14_0= RULE_STRING ) ) otherlv_15= 'DATAHANDLER' ( (lv_datahandler_16_0= RULE_STRING ) ) otherlv_17= 'OPTIONS' otherlv_18= '(' ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+ (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )? otherlv_24= ')' ) ;
    public final EObject ruleAccessFramework() throws RecognitionException {
        EObject current = null;

        Token lv_type_0_1=null;
        Token lv_type_0_2=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_5=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token lv_wrapper_10_0=null;
        Token otherlv_11=null;
        Token lv_protocol_12_0=null;
        Token otherlv_13=null;
        Token lv_transport_14_0=null;
        Token otherlv_15=null;
        Token lv_datahandler_16_0=null;
        Token otherlv_17=null;
        Token otherlv_18=null;
        Token lv_keys_19_0=null;
        Token lv_values_20_0=null;
        Token otherlv_21=null;
        Token lv_keys_22_0=null;
        Token lv_values_23_0=null;
        Token otherlv_24=null;
        EObject lv_attributes_3_0 = null;

        EObject lv_datatypes_4_0 = null;

        EObject lv_attributes_6_0 = null;

        EObject lv_datatypes_7_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1994:2: ( ( ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) ) ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'WRAPPER' ( (lv_wrapper_10_0= RULE_STRING ) ) otherlv_11= 'PROTOCOL' ( (lv_protocol_12_0= RULE_STRING ) ) otherlv_13= 'TRANSPORT' ( (lv_transport_14_0= RULE_STRING ) ) otherlv_15= 'DATAHANDLER' ( (lv_datahandler_16_0= RULE_STRING ) ) otherlv_17= 'OPTIONS' otherlv_18= '(' ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+ (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )? otherlv_24= ')' ) )
            // InternalCQL.g:1995:2: ( ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) ) ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'WRAPPER' ( (lv_wrapper_10_0= RULE_STRING ) ) otherlv_11= 'PROTOCOL' ( (lv_protocol_12_0= RULE_STRING ) ) otherlv_13= 'TRANSPORT' ( (lv_transport_14_0= RULE_STRING ) ) otherlv_15= 'DATAHANDLER' ( (lv_datahandler_16_0= RULE_STRING ) ) otherlv_17= 'OPTIONS' otherlv_18= '(' ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+ (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )? otherlv_24= ')' )
            {
            // InternalCQL.g:1995:2: ( ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) ) ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'WRAPPER' ( (lv_wrapper_10_0= RULE_STRING ) ) otherlv_11= 'PROTOCOL' ( (lv_protocol_12_0= RULE_STRING ) ) otherlv_13= 'TRANSPORT' ( (lv_transport_14_0= RULE_STRING ) ) otherlv_15= 'DATAHANDLER' ( (lv_datahandler_16_0= RULE_STRING ) ) otherlv_17= 'OPTIONS' otherlv_18= '(' ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+ (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )? otherlv_24= ')' )
            // InternalCQL.g:1996:3: ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) ) ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'WRAPPER' ( (lv_wrapper_10_0= RULE_STRING ) ) otherlv_11= 'PROTOCOL' ( (lv_protocol_12_0= RULE_STRING ) ) otherlv_13= 'TRANSPORT' ( (lv_transport_14_0= RULE_STRING ) ) otherlv_15= 'DATAHANDLER' ( (lv_datahandler_16_0= RULE_STRING ) ) otherlv_17= 'OPTIONS' otherlv_18= '(' ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+ (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )? otherlv_24= ')'
            {
            // InternalCQL.g:1996:3: ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) )
            // InternalCQL.g:1997:4: ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) )
            {
            // InternalCQL.g:1997:4: ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) )
            // InternalCQL.g:1998:5: (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' )
            {
            // InternalCQL.g:1998:5: (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' )
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==50) ) {
                alt44=1;
            }
            else if ( (LA44_0==51) ) {
                alt44=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 44, 0, input);

                throw nvae;
            }
            switch (alt44) {
                case 1 :
                    // InternalCQL.g:1999:6: lv_type_0_1= 'STREAM'
                    {
                    lv_type_0_1=(Token)match(input,50,FOLLOW_11); 

                    						newLeafNode(lv_type_0_1, grammarAccess.getAccessFrameworkAccess().getTypeSTREAMKeyword_0_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
                    						}
                    						setWithLastConsumed(current, "type", lv_type_0_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:2010:6: lv_type_0_2= 'SINK'
                    {
                    lv_type_0_2=(Token)match(input,51,FOLLOW_11); 

                    						newLeafNode(lv_type_0_2, grammarAccess.getAccessFrameworkAccess().getTypeSINKKeyword_0_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
                    						}
                    						setWithLastConsumed(current, "type", lv_type_0_2, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQL.g:2023:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQL.g:2024:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQL.g:2024:4: (lv_name_1_0= RULE_ID )
            // InternalCQL.g:2025:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_41); 

            					newLeafNode(lv_name_1_0, grammarAccess.getAccessFrameworkAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAccessFrameworkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,13,FOLLOW_43); 

            			newLeafNode(otherlv_2, grammarAccess.getAccessFrameworkAccess().getLeftParenthesisKeyword_2());
            		
            // InternalCQL.g:2045:3: ( (lv_attributes_3_0= ruleAttribute ) )+
            int cnt45=0;
            loop45:
            do {
                int alt45=2;
                int LA45_0 = input.LA(1);

                if ( (LA45_0==RULE_ID) ) {
                    alt45=1;
                }


                switch (alt45) {
            	case 1 :
            	    // InternalCQL.g:2046:4: (lv_attributes_3_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:2046:4: (lv_attributes_3_0= ruleAttribute )
            	    // InternalCQL.g:2047:5: lv_attributes_3_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getAccessFrameworkAccess().getAttributesAttributeParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_43);
            	    lv_attributes_3_0=ruleAttribute();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getAccessFrameworkRule());
            	    					}
            	    					add(
            	    						current,
            	    						"attributes",
            	    						lv_attributes_3_0,
            	    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt45 >= 1 ) break loop45;
                        EarlyExitException eee =
                            new EarlyExitException(45, input);
                        throw eee;
                }
                cnt45++;
            } while (true);

            // InternalCQL.g:2064:3: ( (lv_datatypes_4_0= ruleDataType ) )+
            int cnt46=0;
            loop46:
            do {
                int alt46=2;
                int LA46_0 = input.LA(1);

                if ( ((LA46_0>=20 && LA46_0<=26)) ) {
                    alt46=1;
                }


                switch (alt46) {
            	case 1 :
            	    // InternalCQL.g:2065:4: (lv_datatypes_4_0= ruleDataType )
            	    {
            	    // InternalCQL.g:2065:4: (lv_datatypes_4_0= ruleDataType )
            	    // InternalCQL.g:2066:5: lv_datatypes_4_0= ruleDataType
            	    {

            	    					newCompositeNode(grammarAccess.getAccessFrameworkAccess().getDatatypesDataTypeParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_44);
            	    lv_datatypes_4_0=ruleDataType();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getAccessFrameworkRule());
            	    					}
            	    					add(
            	    						current,
            	    						"datatypes",
            	    						lv_datatypes_4_0,
            	    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.DataType");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt46 >= 1 ) break loop46;
                        EarlyExitException eee =
                            new EarlyExitException(46, input);
                        throw eee;
                }
                cnt46++;
            } while (true);

            // InternalCQL.g:2083:3: (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )*
            loop47:
            do {
                int alt47=2;
                int LA47_0 = input.LA(1);

                if ( (LA47_0==42) ) {
                    alt47=1;
                }


                switch (alt47) {
            	case 1 :
            	    // InternalCQL.g:2084:4: otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) )
            	    {
            	    otherlv_5=(Token)match(input,42,FOLLOW_43); 

            	    				newLeafNode(otherlv_5, grammarAccess.getAccessFrameworkAccess().getCommaKeyword_5_0());
            	    			
            	    // InternalCQL.g:2088:4: ( (lv_attributes_6_0= ruleAttribute ) )
            	    // InternalCQL.g:2089:5: (lv_attributes_6_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:2089:5: (lv_attributes_6_0= ruleAttribute )
            	    // InternalCQL.g:2090:6: lv_attributes_6_0= ruleAttribute
            	    {

            	    						newCompositeNode(grammarAccess.getAccessFrameworkAccess().getAttributesAttributeParserRuleCall_5_1_0());
            	    					
            	    pushFollow(FOLLOW_45);
            	    lv_attributes_6_0=ruleAttribute();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getAccessFrameworkRule());
            	    						}
            	    						add(
            	    							current,
            	    							"attributes",
            	    							lv_attributes_6_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }

            	    // InternalCQL.g:2107:4: ( (lv_datatypes_7_0= ruleDataType ) )
            	    // InternalCQL.g:2108:5: (lv_datatypes_7_0= ruleDataType )
            	    {
            	    // InternalCQL.g:2108:5: (lv_datatypes_7_0= ruleDataType )
            	    // InternalCQL.g:2109:6: lv_datatypes_7_0= ruleDataType
            	    {

            	    						newCompositeNode(grammarAccess.getAccessFrameworkAccess().getDatatypesDataTypeParserRuleCall_5_2_0());
            	    					
            	    pushFollow(FOLLOW_46);
            	    lv_datatypes_7_0=ruleDataType();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getAccessFrameworkRule());
            	    						}
            	    						add(
            	    							current,
            	    							"datatypes",
            	    							lv_datatypes_7_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.DataType");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop47;
                }
            } while (true);

            otherlv_8=(Token)match(input,14,FOLLOW_47); 

            			newLeafNode(otherlv_8, grammarAccess.getAccessFrameworkAccess().getRightParenthesisKeyword_6());
            		
            otherlv_9=(Token)match(input,52,FOLLOW_48); 

            			newLeafNode(otherlv_9, grammarAccess.getAccessFrameworkAccess().getWRAPPERKeyword_7());
            		
            // InternalCQL.g:2135:3: ( (lv_wrapper_10_0= RULE_STRING ) )
            // InternalCQL.g:2136:4: (lv_wrapper_10_0= RULE_STRING )
            {
            // InternalCQL.g:2136:4: (lv_wrapper_10_0= RULE_STRING )
            // InternalCQL.g:2137:5: lv_wrapper_10_0= RULE_STRING
            {
            lv_wrapper_10_0=(Token)match(input,RULE_STRING,FOLLOW_49); 

            					newLeafNode(lv_wrapper_10_0, grammarAccess.getAccessFrameworkAccess().getWrapperSTRINGTerminalRuleCall_8_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAccessFrameworkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"wrapper",
            						lv_wrapper_10_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_11=(Token)match(input,53,FOLLOW_48); 

            			newLeafNode(otherlv_11, grammarAccess.getAccessFrameworkAccess().getPROTOCOLKeyword_9());
            		
            // InternalCQL.g:2157:3: ( (lv_protocol_12_0= RULE_STRING ) )
            // InternalCQL.g:2158:4: (lv_protocol_12_0= RULE_STRING )
            {
            // InternalCQL.g:2158:4: (lv_protocol_12_0= RULE_STRING )
            // InternalCQL.g:2159:5: lv_protocol_12_0= RULE_STRING
            {
            lv_protocol_12_0=(Token)match(input,RULE_STRING,FOLLOW_50); 

            					newLeafNode(lv_protocol_12_0, grammarAccess.getAccessFrameworkAccess().getProtocolSTRINGTerminalRuleCall_10_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAccessFrameworkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"protocol",
            						lv_protocol_12_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_13=(Token)match(input,54,FOLLOW_48); 

            			newLeafNode(otherlv_13, grammarAccess.getAccessFrameworkAccess().getTRANSPORTKeyword_11());
            		
            // InternalCQL.g:2179:3: ( (lv_transport_14_0= RULE_STRING ) )
            // InternalCQL.g:2180:4: (lv_transport_14_0= RULE_STRING )
            {
            // InternalCQL.g:2180:4: (lv_transport_14_0= RULE_STRING )
            // InternalCQL.g:2181:5: lv_transport_14_0= RULE_STRING
            {
            lv_transport_14_0=(Token)match(input,RULE_STRING,FOLLOW_51); 

            					newLeafNode(lv_transport_14_0, grammarAccess.getAccessFrameworkAccess().getTransportSTRINGTerminalRuleCall_12_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAccessFrameworkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"transport",
            						lv_transport_14_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_15=(Token)match(input,55,FOLLOW_48); 

            			newLeafNode(otherlv_15, grammarAccess.getAccessFrameworkAccess().getDATAHANDLERKeyword_13());
            		
            // InternalCQL.g:2201:3: ( (lv_datahandler_16_0= RULE_STRING ) )
            // InternalCQL.g:2202:4: (lv_datahandler_16_0= RULE_STRING )
            {
            // InternalCQL.g:2202:4: (lv_datahandler_16_0= RULE_STRING )
            // InternalCQL.g:2203:5: lv_datahandler_16_0= RULE_STRING
            {
            lv_datahandler_16_0=(Token)match(input,RULE_STRING,FOLLOW_52); 

            					newLeafNode(lv_datahandler_16_0, grammarAccess.getAccessFrameworkAccess().getDatahandlerSTRINGTerminalRuleCall_14_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAccessFrameworkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"datahandler",
            						lv_datahandler_16_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_17=(Token)match(input,56,FOLLOW_41); 

            			newLeafNode(otherlv_17, grammarAccess.getAccessFrameworkAccess().getOPTIONSKeyword_15());
            		
            otherlv_18=(Token)match(input,13,FOLLOW_48); 

            			newLeafNode(otherlv_18, grammarAccess.getAccessFrameworkAccess().getLeftParenthesisKeyword_16());
            		
            // InternalCQL.g:2227:3: ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+
            int cnt48=0;
            loop48:
            do {
                int alt48=2;
                int LA48_0 = input.LA(1);

                if ( (LA48_0==RULE_STRING) ) {
                    alt48=1;
                }


                switch (alt48) {
            	case 1 :
            	    // InternalCQL.g:2228:4: ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) )
            	    {
            	    // InternalCQL.g:2228:4: ( (lv_keys_19_0= RULE_STRING ) )
            	    // InternalCQL.g:2229:5: (lv_keys_19_0= RULE_STRING )
            	    {
            	    // InternalCQL.g:2229:5: (lv_keys_19_0= RULE_STRING )
            	    // InternalCQL.g:2230:6: lv_keys_19_0= RULE_STRING
            	    {
            	    lv_keys_19_0=(Token)match(input,RULE_STRING,FOLLOW_48); 

            	    						newLeafNode(lv_keys_19_0, grammarAccess.getAccessFrameworkAccess().getKeysSTRINGTerminalRuleCall_17_0_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"keys",
            	    							lv_keys_19_0,
            	    							"org.eclipse.xtext.common.Terminals.STRING");
            	    					

            	    }


            	    }

            	    // InternalCQL.g:2246:4: ( (lv_values_20_0= RULE_STRING ) )
            	    // InternalCQL.g:2247:5: (lv_values_20_0= RULE_STRING )
            	    {
            	    // InternalCQL.g:2247:5: (lv_values_20_0= RULE_STRING )
            	    // InternalCQL.g:2248:6: lv_values_20_0= RULE_STRING
            	    {
            	    lv_values_20_0=(Token)match(input,RULE_STRING,FOLLOW_53); 

            	    						newLeafNode(lv_values_20_0, grammarAccess.getAccessFrameworkAccess().getValuesSTRINGTerminalRuleCall_17_1_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"values",
            	    							lv_values_20_0,
            	    							"org.eclipse.xtext.common.Terminals.STRING");
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt48 >= 1 ) break loop48;
                        EarlyExitException eee =
                            new EarlyExitException(48, input);
                        throw eee;
                }
                cnt48++;
            } while (true);

            // InternalCQL.g:2265:3: (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )?
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==42) ) {
                alt49=1;
            }
            switch (alt49) {
                case 1 :
                    // InternalCQL.g:2266:4: otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) )
                    {
                    otherlv_21=(Token)match(input,42,FOLLOW_48); 

                    				newLeafNode(otherlv_21, grammarAccess.getAccessFrameworkAccess().getCommaKeyword_18_0());
                    			
                    // InternalCQL.g:2270:4: ( (lv_keys_22_0= RULE_STRING ) )
                    // InternalCQL.g:2271:5: (lv_keys_22_0= RULE_STRING )
                    {
                    // InternalCQL.g:2271:5: (lv_keys_22_0= RULE_STRING )
                    // InternalCQL.g:2272:6: lv_keys_22_0= RULE_STRING
                    {
                    lv_keys_22_0=(Token)match(input,RULE_STRING,FOLLOW_48); 

                    						newLeafNode(lv_keys_22_0, grammarAccess.getAccessFrameworkAccess().getKeysSTRINGTerminalRuleCall_18_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"keys",
                    							lv_keys_22_0,
                    							"org.eclipse.xtext.common.Terminals.STRING");
                    					

                    }


                    }

                    // InternalCQL.g:2288:4: ( (lv_values_23_0= RULE_STRING ) )
                    // InternalCQL.g:2289:5: (lv_values_23_0= RULE_STRING )
                    {
                    // InternalCQL.g:2289:5: (lv_values_23_0= RULE_STRING )
                    // InternalCQL.g:2290:6: lv_values_23_0= RULE_STRING
                    {
                    lv_values_23_0=(Token)match(input,RULE_STRING,FOLLOW_6); 

                    						newLeafNode(lv_values_23_0, grammarAccess.getAccessFrameworkAccess().getValuesSTRINGTerminalRuleCall_18_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"values",
                    							lv_values_23_0,
                    							"org.eclipse.xtext.common.Terminals.STRING");
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_24=(Token)match(input,14,FOLLOW_2); 

            			newLeafNode(otherlv_24, grammarAccess.getAccessFrameworkAccess().getRightParenthesisKeyword_19());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAccessFramework"


    // $ANTLR start "entryRuleChannelFormat"
    // InternalCQL.g:2315:1: entryRuleChannelFormat returns [EObject current=null] : iv_ruleChannelFormat= ruleChannelFormat EOF ;
    public final EObject entryRuleChannelFormat() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleChannelFormat = null;


        try {
            // InternalCQL.g:2315:54: (iv_ruleChannelFormat= ruleChannelFormat EOF )
            // InternalCQL.g:2316:2: iv_ruleChannelFormat= ruleChannelFormat EOF
            {
             newCompositeNode(grammarAccess.getChannelFormatRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleChannelFormat=ruleChannelFormat();

            state._fsp--;

             current =iv_ruleChannelFormat; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleChannelFormat"


    // $ANTLR start "ruleChannelFormat"
    // InternalCQL.g:2322:1: ruleChannelFormat returns [EObject current=null] : ( ( (lv_stream_0_0= ruleChannelFormatStream ) ) | ( (lv_view_1_0= ruleChannelFormatView ) ) ) ;
    public final EObject ruleChannelFormat() throws RecognitionException {
        EObject current = null;

        EObject lv_stream_0_0 = null;

        EObject lv_view_1_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2328:2: ( ( ( (lv_stream_0_0= ruleChannelFormatStream ) ) | ( (lv_view_1_0= ruleChannelFormatView ) ) ) )
            // InternalCQL.g:2329:2: ( ( (lv_stream_0_0= ruleChannelFormatStream ) ) | ( (lv_view_1_0= ruleChannelFormatView ) ) )
            {
            // InternalCQL.g:2329:2: ( ( (lv_stream_0_0= ruleChannelFormatStream ) ) | ( (lv_view_1_0= ruleChannelFormatView ) ) )
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==50) ) {
                alt50=1;
            }
            else if ( (LA50_0==59) ) {
                alt50=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 50, 0, input);

                throw nvae;
            }
            switch (alt50) {
                case 1 :
                    // InternalCQL.g:2330:3: ( (lv_stream_0_0= ruleChannelFormatStream ) )
                    {
                    // InternalCQL.g:2330:3: ( (lv_stream_0_0= ruleChannelFormatStream ) )
                    // InternalCQL.g:2331:4: (lv_stream_0_0= ruleChannelFormatStream )
                    {
                    // InternalCQL.g:2331:4: (lv_stream_0_0= ruleChannelFormatStream )
                    // InternalCQL.g:2332:5: lv_stream_0_0= ruleChannelFormatStream
                    {

                    					newCompositeNode(grammarAccess.getChannelFormatAccess().getStreamChannelFormatStreamParserRuleCall_0_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_stream_0_0=ruleChannelFormatStream();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getChannelFormatRule());
                    					}
                    					set(
                    						current,
                    						"stream",
                    						lv_stream_0_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ChannelFormatStream");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:2350:3: ( (lv_view_1_0= ruleChannelFormatView ) )
                    {
                    // InternalCQL.g:2350:3: ( (lv_view_1_0= ruleChannelFormatView ) )
                    // InternalCQL.g:2351:4: (lv_view_1_0= ruleChannelFormatView )
                    {
                    // InternalCQL.g:2351:4: (lv_view_1_0= ruleChannelFormatView )
                    // InternalCQL.g:2352:5: lv_view_1_0= ruleChannelFormatView
                    {

                    					newCompositeNode(grammarAccess.getChannelFormatAccess().getViewChannelFormatViewParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_view_1_0=ruleChannelFormatView();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getChannelFormatRule());
                    					}
                    					set(
                    						current,
                    						"view",
                    						lv_view_1_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ChannelFormatView");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleChannelFormat"


    // $ANTLR start "entryRuleChannelFormatStream"
    // InternalCQL.g:2373:1: entryRuleChannelFormatStream returns [EObject current=null] : iv_ruleChannelFormatStream= ruleChannelFormatStream EOF ;
    public final EObject entryRuleChannelFormatStream() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleChannelFormatStream = null;


        try {
            // InternalCQL.g:2373:60: (iv_ruleChannelFormatStream= ruleChannelFormatStream EOF )
            // InternalCQL.g:2374:2: iv_ruleChannelFormatStream= ruleChannelFormatStream EOF
            {
             newCompositeNode(grammarAccess.getChannelFormatStreamRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleChannelFormatStream=ruleChannelFormatStream();

            state._fsp--;

             current =iv_ruleChannelFormatStream; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleChannelFormatStream"


    // $ANTLR start "ruleChannelFormatStream"
    // InternalCQL.g:2380:1: ruleChannelFormatStream returns [EObject current=null] : (otherlv_0= 'STREAM' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'CHANNEL' ( (lv_host_10_0= RULE_ID ) ) otherlv_11= ':' ( (lv_port_12_0= RULE_INT ) ) ) ;
    public final EObject ruleChannelFormatStream() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_5=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token lv_host_10_0=null;
        Token otherlv_11=null;
        Token lv_port_12_0=null;
        EObject lv_attributes_3_0 = null;

        EObject lv_datatypes_4_0 = null;

        EObject lv_attributes_6_0 = null;

        EObject lv_datatypes_7_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2386:2: ( (otherlv_0= 'STREAM' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'CHANNEL' ( (lv_host_10_0= RULE_ID ) ) otherlv_11= ':' ( (lv_port_12_0= RULE_INT ) ) ) )
            // InternalCQL.g:2387:2: (otherlv_0= 'STREAM' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'CHANNEL' ( (lv_host_10_0= RULE_ID ) ) otherlv_11= ':' ( (lv_port_12_0= RULE_INT ) ) )
            {
            // InternalCQL.g:2387:2: (otherlv_0= 'STREAM' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'CHANNEL' ( (lv_host_10_0= RULE_ID ) ) otherlv_11= ':' ( (lv_port_12_0= RULE_INT ) ) )
            // InternalCQL.g:2388:3: otherlv_0= 'STREAM' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'CHANNEL' ( (lv_host_10_0= RULE_ID ) ) otherlv_11= ':' ( (lv_port_12_0= RULE_INT ) )
            {
            otherlv_0=(Token)match(input,50,FOLLOW_11); 

            			newLeafNode(otherlv_0, grammarAccess.getChannelFormatStreamAccess().getSTREAMKeyword_0());
            		
            // InternalCQL.g:2392:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQL.g:2393:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQL.g:2393:4: (lv_name_1_0= RULE_ID )
            // InternalCQL.g:2394:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_41); 

            					newLeafNode(lv_name_1_0, grammarAccess.getChannelFormatStreamAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getChannelFormatStreamRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,13,FOLLOW_43); 

            			newLeafNode(otherlv_2, grammarAccess.getChannelFormatStreamAccess().getLeftParenthesisKeyword_2());
            		
            // InternalCQL.g:2414:3: ( (lv_attributes_3_0= ruleAttribute ) )+
            int cnt51=0;
            loop51:
            do {
                int alt51=2;
                int LA51_0 = input.LA(1);

                if ( (LA51_0==RULE_ID) ) {
                    alt51=1;
                }


                switch (alt51) {
            	case 1 :
            	    // InternalCQL.g:2415:4: (lv_attributes_3_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:2415:4: (lv_attributes_3_0= ruleAttribute )
            	    // InternalCQL.g:2416:5: lv_attributes_3_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getChannelFormatStreamAccess().getAttributesAttributeParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_43);
            	    lv_attributes_3_0=ruleAttribute();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getChannelFormatStreamRule());
            	    					}
            	    					add(
            	    						current,
            	    						"attributes",
            	    						lv_attributes_3_0,
            	    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt51 >= 1 ) break loop51;
                        EarlyExitException eee =
                            new EarlyExitException(51, input);
                        throw eee;
                }
                cnt51++;
            } while (true);

            // InternalCQL.g:2433:3: ( (lv_datatypes_4_0= ruleDataType ) )+
            int cnt52=0;
            loop52:
            do {
                int alt52=2;
                int LA52_0 = input.LA(1);

                if ( ((LA52_0>=20 && LA52_0<=26)) ) {
                    alt52=1;
                }


                switch (alt52) {
            	case 1 :
            	    // InternalCQL.g:2434:4: (lv_datatypes_4_0= ruleDataType )
            	    {
            	    // InternalCQL.g:2434:4: (lv_datatypes_4_0= ruleDataType )
            	    // InternalCQL.g:2435:5: lv_datatypes_4_0= ruleDataType
            	    {

            	    					newCompositeNode(grammarAccess.getChannelFormatStreamAccess().getDatatypesDataTypeParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_44);
            	    lv_datatypes_4_0=ruleDataType();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getChannelFormatStreamRule());
            	    					}
            	    					add(
            	    						current,
            	    						"datatypes",
            	    						lv_datatypes_4_0,
            	    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.DataType");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt52 >= 1 ) break loop52;
                        EarlyExitException eee =
                            new EarlyExitException(52, input);
                        throw eee;
                }
                cnt52++;
            } while (true);

            // InternalCQL.g:2452:3: (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )*
            loop53:
            do {
                int alt53=2;
                int LA53_0 = input.LA(1);

                if ( (LA53_0==42) ) {
                    alt53=1;
                }


                switch (alt53) {
            	case 1 :
            	    // InternalCQL.g:2453:4: otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) )
            	    {
            	    otherlv_5=(Token)match(input,42,FOLLOW_43); 

            	    				newLeafNode(otherlv_5, grammarAccess.getChannelFormatStreamAccess().getCommaKeyword_5_0());
            	    			
            	    // InternalCQL.g:2457:4: ( (lv_attributes_6_0= ruleAttribute ) )
            	    // InternalCQL.g:2458:5: (lv_attributes_6_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:2458:5: (lv_attributes_6_0= ruleAttribute )
            	    // InternalCQL.g:2459:6: lv_attributes_6_0= ruleAttribute
            	    {

            	    						newCompositeNode(grammarAccess.getChannelFormatStreamAccess().getAttributesAttributeParserRuleCall_5_1_0());
            	    					
            	    pushFollow(FOLLOW_45);
            	    lv_attributes_6_0=ruleAttribute();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getChannelFormatStreamRule());
            	    						}
            	    						add(
            	    							current,
            	    							"attributes",
            	    							lv_attributes_6_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }

            	    // InternalCQL.g:2476:4: ( (lv_datatypes_7_0= ruleDataType ) )
            	    // InternalCQL.g:2477:5: (lv_datatypes_7_0= ruleDataType )
            	    {
            	    // InternalCQL.g:2477:5: (lv_datatypes_7_0= ruleDataType )
            	    // InternalCQL.g:2478:6: lv_datatypes_7_0= ruleDataType
            	    {

            	    						newCompositeNode(grammarAccess.getChannelFormatStreamAccess().getDatatypesDataTypeParserRuleCall_5_2_0());
            	    					
            	    pushFollow(FOLLOW_46);
            	    lv_datatypes_7_0=ruleDataType();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getChannelFormatStreamRule());
            	    						}
            	    						add(
            	    							current,
            	    							"datatypes",
            	    							lv_datatypes_7_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.DataType");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop53;
                }
            } while (true);

            otherlv_8=(Token)match(input,14,FOLLOW_54); 

            			newLeafNode(otherlv_8, grammarAccess.getChannelFormatStreamAccess().getRightParenthesisKeyword_6());
            		
            otherlv_9=(Token)match(input,57,FOLLOW_11); 

            			newLeafNode(otherlv_9, grammarAccess.getChannelFormatStreamAccess().getCHANNELKeyword_7());
            		
            // InternalCQL.g:2504:3: ( (lv_host_10_0= RULE_ID ) )
            // InternalCQL.g:2505:4: (lv_host_10_0= RULE_ID )
            {
            // InternalCQL.g:2505:4: (lv_host_10_0= RULE_ID )
            // InternalCQL.g:2506:5: lv_host_10_0= RULE_ID
            {
            lv_host_10_0=(Token)match(input,RULE_ID,FOLLOW_55); 

            					newLeafNode(lv_host_10_0, grammarAccess.getChannelFormatStreamAccess().getHostIDTerminalRuleCall_8_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getChannelFormatStreamRule());
            					}
            					setWithLastConsumed(
            						current,
            						"host",
            						lv_host_10_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_11=(Token)match(input,58,FOLLOW_56); 

            			newLeafNode(otherlv_11, grammarAccess.getChannelFormatStreamAccess().getColonKeyword_9());
            		
            // InternalCQL.g:2526:3: ( (lv_port_12_0= RULE_INT ) )
            // InternalCQL.g:2527:4: (lv_port_12_0= RULE_INT )
            {
            // InternalCQL.g:2527:4: (lv_port_12_0= RULE_INT )
            // InternalCQL.g:2528:5: lv_port_12_0= RULE_INT
            {
            lv_port_12_0=(Token)match(input,RULE_INT,FOLLOW_2); 

            					newLeafNode(lv_port_12_0, grammarAccess.getChannelFormatStreamAccess().getPortINTTerminalRuleCall_10_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getChannelFormatStreamRule());
            					}
            					setWithLastConsumed(
            						current,
            						"port",
            						lv_port_12_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleChannelFormatStream"


    // $ANTLR start "entryRuleChannelFormatView"
    // InternalCQL.g:2548:1: entryRuleChannelFormatView returns [EObject current=null] : iv_ruleChannelFormatView= ruleChannelFormatView EOF ;
    public final EObject entryRuleChannelFormatView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleChannelFormatView = null;


        try {
            // InternalCQL.g:2548:58: (iv_ruleChannelFormatView= ruleChannelFormatView EOF )
            // InternalCQL.g:2549:2: iv_ruleChannelFormatView= ruleChannelFormatView EOF
            {
             newCompositeNode(grammarAccess.getChannelFormatViewRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleChannelFormatView=ruleChannelFormatView();

            state._fsp--;

             current =iv_ruleChannelFormatView; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleChannelFormatView"


    // $ANTLR start "ruleChannelFormatView"
    // InternalCQL.g:2555:1: ruleChannelFormatView returns [EObject current=null] : (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' otherlv_3= '(' ( (lv_select_4_0= ruleSelect_Statement ) ) (otherlv_5= ';' )? otherlv_6= ')' ) ;
    public final EObject ruleChannelFormatView() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        EObject lv_select_4_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2561:2: ( (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' otherlv_3= '(' ( (lv_select_4_0= ruleSelect_Statement ) ) (otherlv_5= ';' )? otherlv_6= ')' ) )
            // InternalCQL.g:2562:2: (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' otherlv_3= '(' ( (lv_select_4_0= ruleSelect_Statement ) ) (otherlv_5= ';' )? otherlv_6= ')' )
            {
            // InternalCQL.g:2562:2: (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' otherlv_3= '(' ( (lv_select_4_0= ruleSelect_Statement ) ) (otherlv_5= ';' )? otherlv_6= ')' )
            // InternalCQL.g:2563:3: otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' otherlv_3= '(' ( (lv_select_4_0= ruleSelect_Statement ) ) (otherlv_5= ';' )? otherlv_6= ')'
            {
            otherlv_0=(Token)match(input,59,FOLLOW_11); 

            			newLeafNode(otherlv_0, grammarAccess.getChannelFormatViewAccess().getVIEWKeyword_0());
            		
            // InternalCQL.g:2567:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQL.g:2568:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQL.g:2568:4: (lv_name_1_0= RULE_ID )
            // InternalCQL.g:2569:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_29); 

            					newLeafNode(lv_name_1_0, grammarAccess.getChannelFormatViewAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getChannelFormatViewRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,43,FOLLOW_41); 

            			newLeafNode(otherlv_2, grammarAccess.getChannelFormatViewAccess().getFROMKeyword_2());
            		
            otherlv_3=(Token)match(input,13,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getChannelFormatViewAccess().getLeftParenthesisKeyword_3());
            		
            // InternalCQL.g:2593:3: ( (lv_select_4_0= ruleSelect_Statement ) )
            // InternalCQL.g:2594:4: (lv_select_4_0= ruleSelect_Statement )
            {
            // InternalCQL.g:2594:4: (lv_select_4_0= ruleSelect_Statement )
            // InternalCQL.g:2595:5: lv_select_4_0= ruleSelect_Statement
            {

            					newCompositeNode(grammarAccess.getChannelFormatViewAccess().getSelectSelect_StatementParserRuleCall_4_0());
            				
            pushFollow(FOLLOW_57);
            lv_select_4_0=ruleSelect_Statement();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getChannelFormatViewRule());
            					}
            					set(
            						current,
            						"select",
            						lv_select_4_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Select_Statement");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQL.g:2612:3: (otherlv_5= ';' )?
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==12) ) {
                alt54=1;
            }
            switch (alt54) {
                case 1 :
                    // InternalCQL.g:2613:4: otherlv_5= ';'
                    {
                    otherlv_5=(Token)match(input,12,FOLLOW_6); 

                    				newLeafNode(otherlv_5, grammarAccess.getChannelFormatViewAccess().getSemicolonKeyword_5());
                    			

                    }
                    break;

            }

            otherlv_6=(Token)match(input,14,FOLLOW_2); 

            			newLeafNode(otherlv_6, grammarAccess.getChannelFormatViewAccess().getRightParenthesisKeyword_6());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleChannelFormatView"


    // $ANTLR start "entryRuleStreamTo"
    // InternalCQL.g:2626:1: entryRuleStreamTo returns [EObject current=null] : iv_ruleStreamTo= ruleStreamTo EOF ;
    public final EObject entryRuleStreamTo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStreamTo = null;


        try {
            // InternalCQL.g:2626:49: (iv_ruleStreamTo= ruleStreamTo EOF )
            // InternalCQL.g:2627:2: iv_ruleStreamTo= ruleStreamTo EOF
            {
             newCompositeNode(grammarAccess.getStreamToRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleStreamTo=ruleStreamTo();

            state._fsp--;

             current =iv_ruleStreamTo; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleStreamTo"


    // $ANTLR start "ruleStreamTo"
    // InternalCQL.g:2633:1: ruleStreamTo returns [EObject current=null] : (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect_Statement ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) ;
    public final EObject ruleStreamTo() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token lv_inputname_4_0=null;
        EObject lv_statement_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2639:2: ( (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect_Statement ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) )
            // InternalCQL.g:2640:2: (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect_Statement ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            {
            // InternalCQL.g:2640:2: (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect_Statement ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            // InternalCQL.g:2641:3: otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect_Statement ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            {
            otherlv_0=(Token)match(input,50,FOLLOW_58); 

            			newLeafNode(otherlv_0, grammarAccess.getStreamToAccess().getSTREAMKeyword_0());
            		
            otherlv_1=(Token)match(input,60,FOLLOW_11); 

            			newLeafNode(otherlv_1, grammarAccess.getStreamToAccess().getTOKeyword_1());
            		
            // InternalCQL.g:2649:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCQL.g:2650:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCQL.g:2650:4: (lv_name_2_0= RULE_ID )
            // InternalCQL.g:2651:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_59); 

            					newLeafNode(lv_name_2_0, grammarAccess.getStreamToAccess().getNameIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getStreamToRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_2_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalCQL.g:2667:3: ( ( (lv_statement_3_0= ruleSelect_Statement ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==40) ) {
                alt55=1;
            }
            else if ( (LA55_0==RULE_ID) ) {
                alt55=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 55, 0, input);

                throw nvae;
            }
            switch (alt55) {
                case 1 :
                    // InternalCQL.g:2668:4: ( (lv_statement_3_0= ruleSelect_Statement ) )
                    {
                    // InternalCQL.g:2668:4: ( (lv_statement_3_0= ruleSelect_Statement ) )
                    // InternalCQL.g:2669:5: (lv_statement_3_0= ruleSelect_Statement )
                    {
                    // InternalCQL.g:2669:5: (lv_statement_3_0= ruleSelect_Statement )
                    // InternalCQL.g:2670:6: lv_statement_3_0= ruleSelect_Statement
                    {

                    						newCompositeNode(grammarAccess.getStreamToAccess().getStatementSelect_StatementParserRuleCall_3_0_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_statement_3_0=ruleSelect_Statement();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStreamToRule());
                    						}
                    						set(
                    							current,
                    							"statement",
                    							lv_statement_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Select_Statement");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:2688:4: ( (lv_inputname_4_0= RULE_ID ) )
                    {
                    // InternalCQL.g:2688:4: ( (lv_inputname_4_0= RULE_ID ) )
                    // InternalCQL.g:2689:5: (lv_inputname_4_0= RULE_ID )
                    {
                    // InternalCQL.g:2689:5: (lv_inputname_4_0= RULE_ID )
                    // InternalCQL.g:2690:6: lv_inputname_4_0= RULE_ID
                    {
                    lv_inputname_4_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    						newLeafNode(lv_inputname_4_0, grammarAccess.getStreamToAccess().getInputnameIDTerminalRuleCall_3_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getStreamToRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"inputname",
                    							lv_inputname_4_0,
                    							"org.eclipse.xtext.common.Terminals.ID");
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleStreamTo"


    // $ANTLR start "entryRuleDrop"
    // InternalCQL.g:2711:1: entryRuleDrop returns [EObject current=null] : iv_ruleDrop= ruleDrop EOF ;
    public final EObject entryRuleDrop() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDrop = null;


        try {
            // InternalCQL.g:2711:45: (iv_ruleDrop= ruleDrop EOF )
            // InternalCQL.g:2712:2: iv_ruleDrop= ruleDrop EOF
            {
             newCompositeNode(grammarAccess.getDropRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDrop=ruleDrop();

            state._fsp--;

             current =iv_ruleDrop; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDrop"


    // $ANTLR start "ruleDrop"
    // InternalCQL.g:2718:1: ruleDrop returns [EObject current=null] : (otherlv_0= 'DROP' ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) ) (otherlv_3= 'IF' otherlv_4= 'EXISTS' )? ) ;
    public final EObject ruleDrop() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_4=null;


        	enterRule();

        try {
            // InternalCQL.g:2724:2: ( (otherlv_0= 'DROP' ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) ) (otherlv_3= 'IF' otherlv_4= 'EXISTS' )? ) )
            // InternalCQL.g:2725:2: (otherlv_0= 'DROP' ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) ) (otherlv_3= 'IF' otherlv_4= 'EXISTS' )? )
            {
            // InternalCQL.g:2725:2: (otherlv_0= 'DROP' ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) ) (otherlv_3= 'IF' otherlv_4= 'EXISTS' )? )
            // InternalCQL.g:2726:3: otherlv_0= 'DROP' ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) ) (otherlv_3= 'IF' otherlv_4= 'EXISTS' )?
            {
            otherlv_0=(Token)match(input,61,FOLLOW_60); 

            			newLeafNode(otherlv_0, grammarAccess.getDropAccess().getDROPKeyword_0());
            		
            // InternalCQL.g:2730:3: ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) )
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==51) ) {
                alt56=1;
            }
            else if ( (LA56_0==50) ) {
                alt56=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;
            }
            switch (alt56) {
                case 1 :
                    // InternalCQL.g:2731:4: ( (lv_name_1_0= 'SINK' ) )
                    {
                    // InternalCQL.g:2731:4: ( (lv_name_1_0= 'SINK' ) )
                    // InternalCQL.g:2732:5: (lv_name_1_0= 'SINK' )
                    {
                    // InternalCQL.g:2732:5: (lv_name_1_0= 'SINK' )
                    // InternalCQL.g:2733:6: lv_name_1_0= 'SINK'
                    {
                    lv_name_1_0=(Token)match(input,51,FOLLOW_61); 

                    						newLeafNode(lv_name_1_0, grammarAccess.getDropAccess().getNameSINKKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_0, "SINK");
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:2746:4: ( (lv_name_2_0= 'STREAM' ) )
                    {
                    // InternalCQL.g:2746:4: ( (lv_name_2_0= 'STREAM' ) )
                    // InternalCQL.g:2747:5: (lv_name_2_0= 'STREAM' )
                    {
                    // InternalCQL.g:2747:5: (lv_name_2_0= 'STREAM' )
                    // InternalCQL.g:2748:6: lv_name_2_0= 'STREAM'
                    {
                    lv_name_2_0=(Token)match(input,50,FOLLOW_61); 

                    						newLeafNode(lv_name_2_0, grammarAccess.getDropAccess().getNameSTREAMKeyword_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_2_0, "STREAM");
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:2761:3: (otherlv_3= 'IF' otherlv_4= 'EXISTS' )?
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( (LA57_0==62) ) {
                alt57=1;
            }
            switch (alt57) {
                case 1 :
                    // InternalCQL.g:2762:4: otherlv_3= 'IF' otherlv_4= 'EXISTS'
                    {
                    otherlv_3=(Token)match(input,62,FOLLOW_62); 

                    				newLeafNode(otherlv_3, grammarAccess.getDropAccess().getIFKeyword_2_0());
                    			
                    otherlv_4=(Token)match(input,63,FOLLOW_2); 

                    				newLeafNode(otherlv_4, grammarAccess.getDropAccess().getEXISTSKeyword_2_1());
                    			

                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDrop"


    // $ANTLR start "entryRuleWindow_Unbounded"
    // InternalCQL.g:2775:1: entryRuleWindow_Unbounded returns [String current=null] : iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF ;
    public final String entryRuleWindow_Unbounded() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleWindow_Unbounded = null;


        try {
            // InternalCQL.g:2775:56: (iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF )
            // InternalCQL.g:2776:2: iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF
            {
             newCompositeNode(grammarAccess.getWindow_UnboundedRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleWindow_Unbounded=ruleWindow_Unbounded();

            state._fsp--;

             current =iv_ruleWindow_Unbounded.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleWindow_Unbounded"


    // $ANTLR start "ruleWindow_Unbounded"
    // InternalCQL.g:2782:1: ruleWindow_Unbounded returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= 'UNBOUNDED' ;
    public final AntlrDatatypeRuleToken ruleWindow_Unbounded() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQL.g:2788:2: (kw= 'UNBOUNDED' )
            // InternalCQL.g:2789:2: kw= 'UNBOUNDED'
            {
            kw=(Token)match(input,64,FOLLOW_2); 

            		current.merge(kw);
            		newLeafNode(kw, grammarAccess.getWindow_UnboundedAccess().getUNBOUNDEDKeyword());
            	

            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleWindow_Unbounded"


    // $ANTLR start "entryRuleWindow_Timebased"
    // InternalCQL.g:2797:1: entryRuleWindow_Timebased returns [EObject current=null] : iv_ruleWindow_Timebased= ruleWindow_Timebased EOF ;
    public final EObject entryRuleWindow_Timebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Timebased = null;


        try {
            // InternalCQL.g:2797:57: (iv_ruleWindow_Timebased= ruleWindow_Timebased EOF )
            // InternalCQL.g:2798:2: iv_ruleWindow_Timebased= ruleWindow_Timebased EOF
            {
             newCompositeNode(grammarAccess.getWindow_TimebasedRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleWindow_Timebased=ruleWindow_Timebased();

            state._fsp--;

             current =iv_ruleWindow_Timebased; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleWindow_Timebased"


    // $ANTLR start "ruleWindow_Timebased"
    // InternalCQL.g:2804:1: ruleWindow_Timebased returns [EObject current=null] : (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' ) ;
    public final EObject ruleWindow_Timebased() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_size_1_0=null;
        Token lv_unit_2_0=null;
        Token otherlv_3=null;
        Token lv_advance_size_4_0=null;
        Token lv_advance_unit_5_0=null;
        Token otherlv_6=null;


        	enterRule();

        try {
            // InternalCQL.g:2810:2: ( (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' ) )
            // InternalCQL.g:2811:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' )
            {
            // InternalCQL.g:2811:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' )
            // InternalCQL.g:2812:3: otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME'
            {
            otherlv_0=(Token)match(input,65,FOLLOW_56); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TimebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQL.g:2816:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQL.g:2817:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQL.g:2817:4: (lv_size_1_0= RULE_INT )
            // InternalCQL.g:2818:5: lv_size_1_0= RULE_INT
            {
            lv_size_1_0=(Token)match(input,RULE_INT,FOLLOW_11); 

            					newLeafNode(lv_size_1_0, grammarAccess.getWindow_TimebasedAccess().getSizeINTTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getWindow_TimebasedRule());
            					}
            					setWithLastConsumed(
            						current,
            						"size",
            						lv_size_1_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }

            // InternalCQL.g:2834:3: ( (lv_unit_2_0= RULE_ID ) )
            // InternalCQL.g:2835:4: (lv_unit_2_0= RULE_ID )
            {
            // InternalCQL.g:2835:4: (lv_unit_2_0= RULE_ID )
            // InternalCQL.g:2836:5: lv_unit_2_0= RULE_ID
            {
            lv_unit_2_0=(Token)match(input,RULE_ID,FOLLOW_63); 

            					newLeafNode(lv_unit_2_0, grammarAccess.getWindow_TimebasedAccess().getUnitIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getWindow_TimebasedRule());
            					}
            					setWithLastConsumed(
            						current,
            						"unit",
            						lv_unit_2_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalCQL.g:2852:3: (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )?
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==66) ) {
                alt58=1;
            }
            switch (alt58) {
                case 1 :
                    // InternalCQL.g:2853:4: otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) )
                    {
                    otherlv_3=(Token)match(input,66,FOLLOW_56); 

                    				newLeafNode(otherlv_3, grammarAccess.getWindow_TimebasedAccess().getADVANCEKeyword_3_0());
                    			
                    // InternalCQL.g:2857:4: ( (lv_advance_size_4_0= RULE_INT ) )
                    // InternalCQL.g:2858:5: (lv_advance_size_4_0= RULE_INT )
                    {
                    // InternalCQL.g:2858:5: (lv_advance_size_4_0= RULE_INT )
                    // InternalCQL.g:2859:6: lv_advance_size_4_0= RULE_INT
                    {
                    lv_advance_size_4_0=(Token)match(input,RULE_INT,FOLLOW_11); 

                    						newLeafNode(lv_advance_size_4_0, grammarAccess.getWindow_TimebasedAccess().getAdvance_sizeINTTerminalRuleCall_3_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getWindow_TimebasedRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"advance_size",
                    							lv_advance_size_4_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }

                    // InternalCQL.g:2875:4: ( (lv_advance_unit_5_0= RULE_ID ) )
                    // InternalCQL.g:2876:5: (lv_advance_unit_5_0= RULE_ID )
                    {
                    // InternalCQL.g:2876:5: (lv_advance_unit_5_0= RULE_ID )
                    // InternalCQL.g:2877:6: lv_advance_unit_5_0= RULE_ID
                    {
                    lv_advance_unit_5_0=(Token)match(input,RULE_ID,FOLLOW_64); 

                    						newLeafNode(lv_advance_unit_5_0, grammarAccess.getWindow_TimebasedAccess().getAdvance_unitIDTerminalRuleCall_3_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getWindow_TimebasedRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"advance_unit",
                    							lv_advance_unit_5_0,
                    							"org.eclipse.xtext.common.Terminals.ID");
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_6=(Token)match(input,67,FOLLOW_2); 

            			newLeafNode(otherlv_6, grammarAccess.getWindow_TimebasedAccess().getTIMEKeyword_4());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleWindow_Timebased"


    // $ANTLR start "entryRuleWindow_Tuplebased"
    // InternalCQL.g:2902:1: entryRuleWindow_Tuplebased returns [EObject current=null] : iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF ;
    public final EObject entryRuleWindow_Tuplebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Tuplebased = null;


        try {
            // InternalCQL.g:2902:58: (iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF )
            // InternalCQL.g:2903:2: iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF
            {
             newCompositeNode(grammarAccess.getWindow_TuplebasedRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleWindow_Tuplebased=ruleWindow_Tuplebased();

            state._fsp--;

             current =iv_ruleWindow_Tuplebased; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleWindow_Tuplebased"


    // $ANTLR start "ruleWindow_Tuplebased"
    // InternalCQL.g:2909:1: ruleWindow_Tuplebased returns [EObject current=null] : (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) ;
    public final EObject ruleWindow_Tuplebased() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_size_1_0=null;
        Token otherlv_2=null;
        Token lv_advance_size_3_0=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        EObject lv_partition_attribute_7_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2915:2: ( (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) )
            // InternalCQL.g:2916:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            {
            // InternalCQL.g:2916:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            // InternalCQL.g:2917:3: otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            {
            otherlv_0=(Token)match(input,65,FOLLOW_56); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TuplebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQL.g:2921:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQL.g:2922:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQL.g:2922:4: (lv_size_1_0= RULE_INT )
            // InternalCQL.g:2923:5: lv_size_1_0= RULE_INT
            {
            lv_size_1_0=(Token)match(input,RULE_INT,FOLLOW_65); 

            					newLeafNode(lv_size_1_0, grammarAccess.getWindow_TuplebasedAccess().getSizeINTTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getWindow_TuplebasedRule());
            					}
            					setWithLastConsumed(
            						current,
            						"size",
            						lv_size_1_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }

            // InternalCQL.g:2939:3: (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )?
            int alt59=2;
            int LA59_0 = input.LA(1);

            if ( (LA59_0==66) ) {
                alt59=1;
            }
            switch (alt59) {
                case 1 :
                    // InternalCQL.g:2940:4: otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) )
                    {
                    otherlv_2=(Token)match(input,66,FOLLOW_56); 

                    				newLeafNode(otherlv_2, grammarAccess.getWindow_TuplebasedAccess().getADVANCEKeyword_2_0());
                    			
                    // InternalCQL.g:2944:4: ( (lv_advance_size_3_0= RULE_INT ) )
                    // InternalCQL.g:2945:5: (lv_advance_size_3_0= RULE_INT )
                    {
                    // InternalCQL.g:2945:5: (lv_advance_size_3_0= RULE_INT )
                    // InternalCQL.g:2946:6: lv_advance_size_3_0= RULE_INT
                    {
                    lv_advance_size_3_0=(Token)match(input,RULE_INT,FOLLOW_66); 

                    						newLeafNode(lv_advance_size_3_0, grammarAccess.getWindow_TuplebasedAccess().getAdvance_sizeINTTerminalRuleCall_2_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getWindow_TuplebasedRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"advance_size",
                    							lv_advance_size_3_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_4=(Token)match(input,68,FOLLOW_67); 

            			newLeafNode(otherlv_4, grammarAccess.getWindow_TuplebasedAccess().getTUPLEKeyword_3());
            		
            // InternalCQL.g:2967:3: (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            int alt60=2;
            int LA60_0 = input.LA(1);

            if ( (LA60_0==69) ) {
                alt60=1;
            }
            switch (alt60) {
                case 1 :
                    // InternalCQL.g:2968:4: otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    {
                    otherlv_5=(Token)match(input,69,FOLLOW_37); 

                    				newLeafNode(otherlv_5, grammarAccess.getWindow_TuplebasedAccess().getPARTITIONKeyword_4_0());
                    			
                    otherlv_6=(Token)match(input,46,FOLLOW_26); 

                    				newLeafNode(otherlv_6, grammarAccess.getWindow_TuplebasedAccess().getBYKeyword_4_1());
                    			
                    // InternalCQL.g:2976:4: ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    // InternalCQL.g:2977:5: (lv_partition_attribute_7_0= ruleAttribute )
                    {
                    // InternalCQL.g:2977:5: (lv_partition_attribute_7_0= ruleAttribute )
                    // InternalCQL.g:2978:6: lv_partition_attribute_7_0= ruleAttribute
                    {

                    						newCompositeNode(grammarAccess.getWindow_TuplebasedAccess().getPartition_attributeAttributeParserRuleCall_4_2_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_partition_attribute_7_0=ruleAttribute();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getWindow_TuplebasedRule());
                    						}
                    						set(
                    							current,
                    							"partition_attribute",
                    							lv_partition_attribute_7_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleWindow_Tuplebased"

    // Delegated rules


    protected DFA43 dfa43 = new DFA43(this);
    static final String dfa_1s = "\34\uffff";
    static final String dfa_2s = "\1\62\1\7\2\uffff\1\15\3\7\7\16\2\7\1\64\1\23\1\7\7\16\1\24";
    static final String dfa_3s = "\1\73\1\7\2\uffff\1\15\1\7\1\32\1\7\7\52\1\32\1\7\1\71\1\32\1\7\7\52\1\32";
    static final String dfa_4s = "\2\uffff\1\1\1\2\30\uffff";
    static final String dfa_5s = "\34\uffff}>";
    static final String[] dfa_6s = {
            "\1\1\1\3\7\uffff\1\2",
            "\1\4",
            "",
            "",
            "\1\5",
            "\1\6",
            "\1\6\13\uffff\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1\16",
            "\1\17",
            "\1\21\5\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16\17\uffff\1\20",
            "\1\21\5\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16\17\uffff\1\20",
            "\1\21\5\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16\17\uffff\1\20",
            "\1\21\5\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16\17\uffff\1\20",
            "\1\21\5\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16\17\uffff\1\20",
            "\1\21\5\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16\17\uffff\1\20",
            "\1\21\5\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16\17\uffff\1\20",
            "\1\6\14\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16",
            "\1\22",
            "\1\3\4\uffff\1\2",
            "\1\23\1\24\1\25\1\26\1\27\1\30\1\31\1\32",
            "\1\33",
            "\1\21\33\uffff\1\20",
            "\1\21\33\uffff\1\20",
            "\1\21\33\uffff\1\20",
            "\1\21\33\uffff\1\20",
            "\1\21\33\uffff\1\20",
            "\1\21\33\uffff\1\20",
            "\1\21\33\uffff\1\20",
            "\1\24\1\25\1\26\1\27\1\30\1\31\1\32"
    };

    static final short[] dfa_1 = DFA.unpackEncodedString(dfa_1s);
    static final char[] dfa_2 = DFA.unpackEncodedStringToUnsignedChars(dfa_2s);
    static final char[] dfa_3 = DFA.unpackEncodedStringToUnsignedChars(dfa_3s);
    static final short[] dfa_4 = DFA.unpackEncodedString(dfa_4s);
    static final short[] dfa_5 = DFA.unpackEncodedString(dfa_5s);
    static final short[][] dfa_6 = unpackEncodedStringArray(dfa_6s);

    class DFA43 extends DFA {

        public DFA43(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 43;
            this.eot = dfa_1;
            this.eof = dfa_1;
            this.min = dfa_2;
            this.max = dfa_3;
            this.accept = dfa_4;
            this.special = dfa_5;
            this.transition = dfa_6;
        }
        public String getDescription() {
            return "1936:3: ( ( (lv_channelformat_2_0= ruleChannelFormat ) ) | ( (lv_accessframework_3_0= ruleAccessFramework ) ) )";
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x2007010000000002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x00000000000A0002L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000003L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x000000800001A0F2L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x000000800801A0F0L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x000000801001A0F0L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000060000002L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x000000806001A0F0L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000780000002L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x000000878001A0F0L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000001800000002L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x000000980001A0F0L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000006000000002L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x000000E00001A0F0L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x000000800001E0F0L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x000000800001A0F0L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x00000EA00001A0F0L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x00000CA00001A0F0L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x00000C800001A0F0L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x000008800001A0F0L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000040000002080L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000B40000002082L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000002080L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000000000080080L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000A00000000002L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x000084800001A0F0L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x000084800001A0F2L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000840000000002L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x080C000000000000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0000008007F1A0F0L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0000040007F04000L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000000007F00000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000040000004000L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0000040000004040L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x0000000000005000L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x0000010000000080L});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x000C000000000000L});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0x4000000000000002L});
    public static final BitSet FOLLOW_62 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_63 = new BitSet(new long[]{0x0000000000000000L,0x000000000000000CL});
    public static final BitSet FOLLOW_64 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_65 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000014L});
    public static final BitSet FOLLOW_66 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_67 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});

}
