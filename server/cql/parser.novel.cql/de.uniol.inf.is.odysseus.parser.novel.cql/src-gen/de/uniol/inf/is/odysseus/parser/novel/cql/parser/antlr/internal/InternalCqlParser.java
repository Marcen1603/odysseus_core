package de.uniol.inf.is.odysseus.parser.novel.cql.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import de.uniol.inf.is.odysseus.parser.novel.cql.services.CqlGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalCqlParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_INT", "RULE_FLOAT_NUMBER", "RULE_STRING", "RULE_ID", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "';'", "'TRUE'", "'FALSE'", "'.'", "'OR'", "'AND'", "'=='", "'!='", "'>='", "'<='", "'<'", "'>'", "'+'", "'-'", "'*'", "'/'", "'('", "')'", "'NOT'", "'SELECT'", "'DISTINCT'", "','", "'FROM'", "'WHERE'", "'CREATE'"
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


        public InternalCqlParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalCqlParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalCqlParser.tokenNames; }
    public String getGrammarFileName() { return "InternalCql.g"; }



     	private CqlGrammarAccess grammarAccess;

        public InternalCqlParser(TokenStream input, CqlGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "Model";
       	}

       	@Override
       	protected CqlGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleModel"
    // InternalCql.g:65:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // InternalCql.g:65:46: (iv_ruleModel= ruleModel EOF )
            // InternalCql.g:66:2: iv_ruleModel= ruleModel EOF
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
    // InternalCql.g:72:1: ruleModel returns [EObject current=null] : ( (lv_statements_0_0= ruleStatement ) )* ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        EObject lv_statements_0_0 = null;



        	enterRule();

        try {
            // InternalCql.g:78:2: ( ( (lv_statements_0_0= ruleStatement ) )* )
            // InternalCql.g:79:2: ( (lv_statements_0_0= ruleStatement ) )*
            {
            // InternalCql.g:79:2: ( (lv_statements_0_0= ruleStatement ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==31||LA1_0==36) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalCql.g:80:3: (lv_statements_0_0= ruleStatement )
            	    {
            	    // InternalCql.g:80:3: (lv_statements_0_0= ruleStatement )
            	    // InternalCql.g:81:4: lv_statements_0_0= ruleStatement
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
            	    					"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.Statement");
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
    // InternalCql.g:101:1: entryRuleStatement returns [EObject current=null] : iv_ruleStatement= ruleStatement EOF ;
    public final EObject entryRuleStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStatement = null;


        try {
            // InternalCql.g:101:50: (iv_ruleStatement= ruleStatement EOF )
            // InternalCql.g:102:2: iv_ruleStatement= ruleStatement EOF
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
    // InternalCql.g:108:1: ruleStatement returns [EObject current=null] : ( ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) ) otherlv_2= ';' ) ;
    public final EObject ruleStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject lv_type_0_0 = null;

        EObject lv_type_1_0 = null;



        	enterRule();

        try {
            // InternalCql.g:114:2: ( ( ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) ) otherlv_2= ';' ) )
            // InternalCql.g:115:2: ( ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) ) otherlv_2= ';' )
            {
            // InternalCql.g:115:2: ( ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) ) otherlv_2= ';' )
            // InternalCql.g:116:3: ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) ) otherlv_2= ';'
            {
            // InternalCql.g:116:3: ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==31) ) {
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
                    // InternalCql.g:117:4: ( (lv_type_0_0= ruleSelect_Statement ) )
                    {
                    // InternalCql.g:117:4: ( (lv_type_0_0= ruleSelect_Statement ) )
                    // InternalCql.g:118:5: (lv_type_0_0= ruleSelect_Statement )
                    {
                    // InternalCql.g:118:5: (lv_type_0_0= ruleSelect_Statement )
                    // InternalCql.g:119:6: lv_type_0_0= ruleSelect_Statement
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
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.Select_Statement");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCql.g:137:4: ( (lv_type_1_0= ruleCreate_Statement ) )
                    {
                    // InternalCql.g:137:4: ( (lv_type_1_0= ruleCreate_Statement ) )
                    // InternalCql.g:138:5: (lv_type_1_0= ruleCreate_Statement )
                    {
                    // InternalCql.g:138:5: (lv_type_1_0= ruleCreate_Statement )
                    // InternalCql.g:139:6: lv_type_1_0= ruleCreate_Statement
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
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.Create_Statement");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_2=(Token)match(input,12,FOLLOW_2); 

            			newLeafNode(otherlv_2, grammarAccess.getStatementAccess().getSemicolonKeyword_1());
            		

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


    // $ANTLR start "entryRuleAtomic"
    // InternalCql.g:165:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCql.g:165:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCql.g:166:2: iv_ruleAtomic= ruleAtomic EOF
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
    // InternalCql.g:172:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( (otherlv_9= RULE_ID ) ) ) ) ;
    public final EObject ruleAtomic() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        Token lv_value_7_1=null;
        Token lv_value_7_2=null;
        Token otherlv_9=null;


        	enterRule();

        try {
            // InternalCql.g:178:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( (otherlv_9= RULE_ID ) ) ) ) )
            // InternalCql.g:179:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( (otherlv_9= RULE_ID ) ) ) )
            {
            // InternalCql.g:179:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( (otherlv_9= RULE_ID ) ) ) )
            int alt4=5;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt4=1;
                }
                break;
            case RULE_FLOAT_NUMBER:
                {
                alt4=2;
                }
                break;
            case RULE_STRING:
                {
                alt4=3;
                }
                break;
            case 13:
            case 14:
                {
                alt4=4;
                }
                break;
            case RULE_ID:
                {
                alt4=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // InternalCql.g:180:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCql.g:180:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCql.g:181:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCql.g:181:4: ()
                    // InternalCql.g:182:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCql.g:188:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCql.g:189:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCql.g:189:5: (lv_value_1_0= RULE_INT )
                    // InternalCql.g:190:6: lv_value_1_0= RULE_INT
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
                    // InternalCql.g:208:3: ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) )
                    {
                    // InternalCql.g:208:3: ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) )
                    // InternalCql.g:209:4: () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) )
                    {
                    // InternalCql.g:209:4: ()
                    // InternalCql.g:210:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getDoubleConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCql.g:216:4: ( (lv_value_3_0= RULE_FLOAT_NUMBER ) )
                    // InternalCql.g:217:5: (lv_value_3_0= RULE_FLOAT_NUMBER )
                    {
                    // InternalCql.g:217:5: (lv_value_3_0= RULE_FLOAT_NUMBER )
                    // InternalCql.g:218:6: lv_value_3_0= RULE_FLOAT_NUMBER
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
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.FLOAT_NUMBER");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCql.g:236:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCql.g:236:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCql.g:237:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCql.g:237:4: ()
                    // InternalCql.g:238:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCql.g:244:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCql.g:245:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCql.g:245:5: (lv_value_5_0= RULE_STRING )
                    // InternalCql.g:246:6: lv_value_5_0= RULE_STRING
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
                    // InternalCql.g:264:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    {
                    // InternalCql.g:264:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    // InternalCql.g:265:4: () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    {
                    // InternalCql.g:265:4: ()
                    // InternalCql.g:266:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCql.g:272:4: ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    // InternalCql.g:273:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    {
                    // InternalCql.g:273:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    // InternalCql.g:274:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    {
                    // InternalCql.g:274:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==13) ) {
                        alt3=1;
                    }
                    else if ( (LA3_0==14) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 3, 0, input);

                        throw nvae;
                    }
                    switch (alt3) {
                        case 1 :
                            // InternalCql.g:275:7: lv_value_7_1= 'TRUE'
                            {
                            lv_value_7_1=(Token)match(input,13,FOLLOW_2); 

                            							newLeafNode(lv_value_7_1, grammarAccess.getAtomicAccess().getValueTRUEKeyword_3_1_0_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getAtomicRule());
                            							}
                            							setWithLastConsumed(current, "value", lv_value_7_1, null);
                            						

                            }
                            break;
                        case 2 :
                            // InternalCql.g:286:7: lv_value_7_2= 'FALSE'
                            {
                            lv_value_7_2=(Token)match(input,14,FOLLOW_2); 

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
                    // InternalCql.g:301:3: ( () ( (otherlv_9= RULE_ID ) ) )
                    {
                    // InternalCql.g:301:3: ( () ( (otherlv_9= RULE_ID ) ) )
                    // InternalCql.g:302:4: () ( (otherlv_9= RULE_ID ) )
                    {
                    // InternalCql.g:302:4: ()
                    // InternalCql.g:303:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCql.g:309:4: ( (otherlv_9= RULE_ID ) )
                    // InternalCql.g:310:5: (otherlv_9= RULE_ID )
                    {
                    // InternalCql.g:310:5: (otherlv_9= RULE_ID )
                    // InternalCql.g:311:6: otherlv_9= RULE_ID
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicRule());
                    						}
                    					
                    otherlv_9=(Token)match(input,RULE_ID,FOLLOW_2); 

                    						newLeafNode(otherlv_9, grammarAccess.getAtomicAccess().getValueAttributeCrossReference_4_1_0());
                    					

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
    // InternalCql.g:327:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalCql.g:327:47: (iv_ruleSource= ruleSource EOF )
            // InternalCql.g:328:2: iv_ruleSource= ruleSource EOF
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
    // InternalCql.g:334:1: ruleSource returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleSource() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCql.g:340:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCql.g:341:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCql.g:341:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCql.g:342:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCql.g:342:3: (lv_name_0_0= RULE_ID )
            // InternalCql.g:343:4: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            				newLeafNode(lv_name_0_0, grammarAccess.getSourceAccess().getNameIDTerminalRuleCall_0());
            			

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


    // $ANTLR start "entryRuleAttribute"
    // InternalCql.g:362:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCql.g:362:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCql.g:363:2: iv_ruleAttribute= ruleAttribute EOF
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
    // InternalCql.g:369:1: ruleAttribute returns [EObject current=null] : ( ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' )? ( (lv_name_2_0= RULE_ID ) ) ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;


        	enterRule();

        try {
            // InternalCql.g:375:2: ( ( ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' )? ( (lv_name_2_0= RULE_ID ) ) ) )
            // InternalCql.g:376:2: ( ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' )? ( (lv_name_2_0= RULE_ID ) ) )
            {
            // InternalCql.g:376:2: ( ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' )? ( (lv_name_2_0= RULE_ID ) ) )
            // InternalCql.g:377:3: ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' )? ( (lv_name_2_0= RULE_ID ) )
            {
            // InternalCql.g:377:3: ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==RULE_ID) ) {
                int LA5_1 = input.LA(2);

                if ( (LA5_1==15) ) {
                    alt5=1;
                }
            }
            switch (alt5) {
                case 1 :
                    // InternalCql.g:378:4: ( (otherlv_0= RULE_ID ) ) otherlv_1= '.'
                    {
                    // InternalCql.g:378:4: ( (otherlv_0= RULE_ID ) )
                    // InternalCql.g:379:5: (otherlv_0= RULE_ID )
                    {
                    // InternalCql.g:379:5: (otherlv_0= RULE_ID )
                    // InternalCql.g:380:6: otherlv_0= RULE_ID
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAttributeRule());
                    						}
                    					
                    otherlv_0=(Token)match(input,RULE_ID,FOLLOW_5); 

                    						newLeafNode(otherlv_0, grammarAccess.getAttributeAccess().getSourceSourceCrossReference_0_0_0());
                    					

                    }


                    }

                    otherlv_1=(Token)match(input,15,FOLLOW_6); 

                    				newLeafNode(otherlv_1, grammarAccess.getAttributeAccess().getFullStopKeyword_0_1());
                    			

                    }
                    break;

            }

            // InternalCql.g:396:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCql.g:397:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCql.g:397:4: (lv_name_2_0= RULE_ID )
            // InternalCql.g:398:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(lv_name_2_0, grammarAccess.getAttributeAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAttributeRule());
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


    // $ANTLR start "entryRuleExpression"
    // InternalCql.g:418:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCql.g:418:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCql.g:419:2: iv_ruleExpression= ruleExpression EOF
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
    // InternalCql.g:425:1: ruleExpression returns [EObject current=null] : this_Or_0= ruleOr ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_Or_0 = null;



        	enterRule();

        try {
            // InternalCql.g:431:2: (this_Or_0= ruleOr )
            // InternalCql.g:432:2: this_Or_0= ruleOr
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
    // InternalCql.g:443:1: entryRuleOr returns [EObject current=null] : iv_ruleOr= ruleOr EOF ;
    public final EObject entryRuleOr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOr = null;


        try {
            // InternalCql.g:443:43: (iv_ruleOr= ruleOr EOF )
            // InternalCql.g:444:2: iv_ruleOr= ruleOr EOF
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
    // InternalCql.g:450:1: ruleOr returns [EObject current=null] : (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) ;
    public final EObject ruleOr() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_And_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCql.g:456:2: ( (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) )
            // InternalCql.g:457:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            {
            // InternalCql.g:457:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            // InternalCql.g:458:3: this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrAccess().getAndParserRuleCall_0());
            		
            pushFollow(FOLLOW_7);
            this_And_0=ruleAnd();

            state._fsp--;


            			current = this_And_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCql.g:466:3: ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==16) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // InternalCql.g:467:4: () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) )
            	    {
            	    // InternalCql.g:467:4: ()
            	    // InternalCql.g:468:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrAccess().getOrLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,16,FOLLOW_8); 

            	    				newLeafNode(otherlv_2, grammarAccess.getOrAccess().getORKeyword_1_1());
            	    			
            	    // InternalCql.g:478:4: ( (lv_right_3_0= ruleAnd ) )
            	    // InternalCql.g:479:5: (lv_right_3_0= ruleAnd )
            	    {
            	    // InternalCql.g:479:5: (lv_right_3_0= ruleAnd )
            	    // InternalCql.g:480:6: lv_right_3_0= ruleAnd
            	    {

            	    						newCompositeNode(grammarAccess.getOrAccess().getRightAndParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_7);
            	    lv_right_3_0=ruleAnd();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getOrRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.And");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop6;
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
    // InternalCql.g:502:1: entryRuleAnd returns [EObject current=null] : iv_ruleAnd= ruleAnd EOF ;
    public final EObject entryRuleAnd() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnd = null;


        try {
            // InternalCql.g:502:44: (iv_ruleAnd= ruleAnd EOF )
            // InternalCql.g:503:2: iv_ruleAnd= ruleAnd EOF
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
    // InternalCql.g:509:1: ruleAnd returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAnd() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCql.g:515:2: ( (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCql.g:516:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCql.g:516:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCql.g:517:3: this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_9);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCql.g:525:3: ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==17) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // InternalCql.g:526:4: () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCql.g:526:4: ()
            	    // InternalCql.g:527:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndAccess().getAndLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,17,FOLLOW_8); 

            	    				newLeafNode(otherlv_2, grammarAccess.getAndAccess().getANDKeyword_1_1());
            	    			
            	    // InternalCql.g:537:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCql.g:538:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCql.g:538:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCql.g:539:6: lv_right_3_0= ruleEqualitiy
            	    {

            	    						newCompositeNode(grammarAccess.getAndAccess().getRightEqualitiyParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_9);
            	    lv_right_3_0=ruleEqualitiy();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getAndRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.Equalitiy");
            	    						afterParserOrEnumRuleCall();
            	    					

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
    // InternalCql.g:561:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCql.g:561:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCql.g:562:2: iv_ruleEqualitiy= ruleEqualitiy EOF
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
    // InternalCql.g:568:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Comparison_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCql.g:574:2: ( (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCql.g:575:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCql.g:575:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCql.g:576:3: this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_10);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCql.g:584:3: ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>=18 && LA9_0<=19)) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // InternalCql.g:585:4: () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCql.g:585:4: ()
            	    // InternalCql.g:586:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCql.g:592:4: ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) )
            	    // InternalCql.g:593:5: ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) )
            	    {
            	    // InternalCql.g:593:5: ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) )
            	    // InternalCql.g:594:6: (lv_op_2_1= '==' | lv_op_2_2= '!=' )
            	    {
            	    // InternalCql.g:594:6: (lv_op_2_1= '==' | lv_op_2_2= '!=' )
            	    int alt8=2;
            	    int LA8_0 = input.LA(1);

            	    if ( (LA8_0==18) ) {
            	        alt8=1;
            	    }
            	    else if ( (LA8_0==19) ) {
            	        alt8=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 8, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt8) {
            	        case 1 :
            	            // InternalCql.g:595:7: lv_op_2_1= '=='
            	            {
            	            lv_op_2_1=(Token)match(input,18,FOLLOW_8); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getEqualitiyAccess().getOpEqualsSignEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getEqualitiyRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCql.g:606:7: lv_op_2_2= '!='
            	            {
            	            lv_op_2_2=(Token)match(input,19,FOLLOW_8); 

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

            	    // InternalCql.g:619:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCql.g:620:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCql.g:620:5: (lv_right_3_0= ruleComparison )
            	    // InternalCql.g:621:6: lv_right_3_0= ruleComparison
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getRightComparisonParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_10);
            	    lv_right_3_0=ruleComparison();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getEqualitiyRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.Comparison");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop9;
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
    // InternalCql.g:643:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCql.g:643:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCql.g:644:2: iv_ruleComparison= ruleComparison EOF
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
    // InternalCql.g:650:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
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
            // InternalCql.g:656:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCql.g:657:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCql.g:657:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCql.g:658:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_11);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCql.g:666:3: ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>=20 && LA11_0<=23)) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // InternalCql.g:667:4: () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCql.g:667:4: ()
            	    // InternalCql.g:668:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCql.g:674:4: ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) )
            	    // InternalCql.g:675:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    {
            	    // InternalCql.g:675:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    // InternalCql.g:676:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    {
            	    // InternalCql.g:676:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    int alt10=4;
            	    switch ( input.LA(1) ) {
            	    case 20:
            	        {
            	        alt10=1;
            	        }
            	        break;
            	    case 21:
            	        {
            	        alt10=2;
            	        }
            	        break;
            	    case 22:
            	        {
            	        alt10=3;
            	        }
            	        break;
            	    case 23:
            	        {
            	        alt10=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 10, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt10) {
            	        case 1 :
            	            // InternalCql.g:677:7: lv_op_2_1= '>='
            	            {
            	            lv_op_2_1=(Token)match(input,20,FOLLOW_8); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCql.g:688:7: lv_op_2_2= '<='
            	            {
            	            lv_op_2_2=(Token)match(input,21,FOLLOW_8); 

            	            							newLeafNode(lv_op_2_2, grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_2, null);
            	            						

            	            }
            	            break;
            	        case 3 :
            	            // InternalCql.g:699:7: lv_op_2_3= '<'
            	            {
            	            lv_op_2_3=(Token)match(input,22,FOLLOW_8); 

            	            							newLeafNode(lv_op_2_3, grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_3, null);
            	            						

            	            }
            	            break;
            	        case 4 :
            	            // InternalCql.g:710:7: lv_op_2_4= '>'
            	            {
            	            lv_op_2_4=(Token)match(input,23,FOLLOW_8); 

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

            	    // InternalCql.g:723:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCql.g:724:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCql.g:724:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCql.g:725:6: lv_right_3_0= rulePlusOrMinus
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getRightPlusOrMinusParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_11);
            	    lv_right_3_0=rulePlusOrMinus();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getComparisonRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.PlusOrMinus");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop11;
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
    // InternalCql.g:747:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCql.g:747:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCql.g:748:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
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
    // InternalCql.g:754:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCql.g:760:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCql.g:761:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCql.g:761:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCql.g:762:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_12);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCql.g:770:3: ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>=24 && LA13_0<=25)) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // InternalCql.g:771:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCql.g:771:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) )
            	    int alt12=2;
            	    int LA12_0 = input.LA(1);

            	    if ( (LA12_0==24) ) {
            	        alt12=1;
            	    }
            	    else if ( (LA12_0==25) ) {
            	        alt12=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 12, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt12) {
            	        case 1 :
            	            // InternalCql.g:772:5: ( () otherlv_2= '+' )
            	            {
            	            // InternalCql.g:772:5: ( () otherlv_2= '+' )
            	            // InternalCql.g:773:6: () otherlv_2= '+'
            	            {
            	            // InternalCql.g:773:6: ()
            	            // InternalCql.g:774:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_2=(Token)match(input,24,FOLLOW_8); 

            	            						newLeafNode(otherlv_2, grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1());
            	            					

            	            }


            	            }
            	            break;
            	        case 2 :
            	            // InternalCql.g:786:5: ( () otherlv_4= '-' )
            	            {
            	            // InternalCql.g:786:5: ( () otherlv_4= '-' )
            	            // InternalCql.g:787:6: () otherlv_4= '-'
            	            {
            	            // InternalCql.g:787:6: ()
            	            // InternalCql.g:788:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_4=(Token)match(input,25,FOLLOW_8); 

            	            						newLeafNode(otherlv_4, grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1());
            	            					

            	            }


            	            }
            	            break;

            	    }

            	    // InternalCql.g:800:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCql.g:801:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCql.g:801:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCql.g:802:6: lv_right_5_0= ruleMulOrDiv
            	    {

            	    						newCompositeNode(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_12);
            	    lv_right_5_0=ruleMulOrDiv();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getPlusOrMinusRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_5_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.MulOrDiv");
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
    // $ANTLR end "rulePlusOrMinus"


    // $ANTLR start "entryRuleMulOrDiv"
    // InternalCql.g:824:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCql.g:824:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCql.g:825:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
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
    // InternalCql.g:831:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCql.g:837:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCql.g:838:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCql.g:838:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCql.g:839:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_13);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCql.g:847:3: ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( ((LA15_0>=26 && LA15_0<=27)) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // InternalCql.g:848:4: () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCql.g:848:4: ()
            	    // InternalCql.g:849:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCql.g:855:4: ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) )
            	    // InternalCql.g:856:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    {
            	    // InternalCql.g:856:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    // InternalCql.g:857:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    {
            	    // InternalCql.g:857:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    int alt14=2;
            	    int LA14_0 = input.LA(1);

            	    if ( (LA14_0==26) ) {
            	        alt14=1;
            	    }
            	    else if ( (LA14_0==27) ) {
            	        alt14=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 14, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt14) {
            	        case 1 :
            	            // InternalCql.g:858:7: lv_op_2_1= '*'
            	            {
            	            lv_op_2_1=(Token)match(input,26,FOLLOW_8); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getMulOrDivRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCql.g:869:7: lv_op_2_2= '/'
            	            {
            	            lv_op_2_2=(Token)match(input,27,FOLLOW_8); 

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

            	    // InternalCql.g:882:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCql.g:883:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCql.g:883:5: (lv_right_3_0= rulePrimary )
            	    // InternalCql.g:884:6: lv_right_3_0= rulePrimary
            	    {

            	    						newCompositeNode(grammarAccess.getMulOrDivAccess().getRightPrimaryParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_13);
            	    lv_right_3_0=rulePrimary();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getMulOrDivRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.Primary");
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
    // $ANTLR end "ruleMulOrDiv"


    // $ANTLR start "entryRulePrimary"
    // InternalCql.g:906:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCql.g:906:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCql.g:907:2: iv_rulePrimary= rulePrimary EOF
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
    // InternalCql.g:913:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
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
            // InternalCql.g:919:2: ( ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCql.g:920:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCql.g:920:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt16=3;
            switch ( input.LA(1) ) {
            case 28:
                {
                alt16=1;
                }
                break;
            case 30:
                {
                alt16=2;
                }
                break;
            case RULE_INT:
            case RULE_FLOAT_NUMBER:
            case RULE_STRING:
            case RULE_ID:
            case 13:
            case 14:
                {
                alt16=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // InternalCql.g:921:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    {
                    // InternalCql.g:921:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    // InternalCql.g:922:4: () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')'
                    {
                    // InternalCql.g:922:4: ()
                    // InternalCql.g:923:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,28,FOLLOW_8); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCql.g:933:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCql.g:934:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCql.g:934:5: (lv_inner_2_0= ruleExpression )
                    // InternalCql.g:935:6: lv_inner_2_0= ruleExpression
                    {

                    						newCompositeNode(grammarAccess.getPrimaryAccess().getInnerExpressionParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_14);
                    lv_inner_2_0=ruleExpression();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getPrimaryRule());
                    						}
                    						set(
                    							current,
                    							"inner",
                    							lv_inner_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.Expression");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    otherlv_3=(Token)match(input,29,FOLLOW_2); 

                    				newLeafNode(otherlv_3, grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_0_3());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCql.g:958:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCql.g:958:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCql.g:959:4: () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCql.g:959:4: ()
                    // InternalCql.g:960:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,30,FOLLOW_8); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCql.g:970:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCql.g:971:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCql.g:971:5: (lv_expression_6_0= rulePrimary )
                    // InternalCql.g:972:6: lv_expression_6_0= rulePrimary
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
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.Primary");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCql.g:991:3: this_Atomic_7= ruleAtomic
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
    // InternalCql.g:1003:1: entryRuleSelect_Statement returns [EObject current=null] : iv_ruleSelect_Statement= ruleSelect_Statement EOF ;
    public final EObject entryRuleSelect_Statement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelect_Statement = null;


        try {
            // InternalCql.g:1003:57: (iv_ruleSelect_Statement= ruleSelect_Statement EOF )
            // InternalCql.g:1004:2: iv_ruleSelect_Statement= ruleSelect_Statement EOF
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
    // InternalCql.g:1010:1: ruleSelect_Statement returns [EObject current=null] : ( ( (lv_name_0_0= 'SELECT' ) ) (otherlv_1= 'DISTINCT' )? ( (lv_attributes_2_0= ruleAttribute ) )+ (otherlv_3= ',' ( (lv_attributes_4_0= ruleAttribute ) ) )* otherlv_5= 'FROM' ( (lv_sources_6_0= ruleSource ) )+ (otherlv_7= ',' ( (lv_sources_8_0= ruleSource ) ) )* (otherlv_9= 'WHERE' ( (lv_conditions_10_0= ruleExpression ) )+ ) ) ;
    public final EObject ruleSelect_Statement() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        EObject lv_attributes_2_0 = null;

        EObject lv_attributes_4_0 = null;

        EObject lv_sources_6_0 = null;

        EObject lv_sources_8_0 = null;

        EObject lv_conditions_10_0 = null;



        	enterRule();

        try {
            // InternalCql.g:1016:2: ( ( ( (lv_name_0_0= 'SELECT' ) ) (otherlv_1= 'DISTINCT' )? ( (lv_attributes_2_0= ruleAttribute ) )+ (otherlv_3= ',' ( (lv_attributes_4_0= ruleAttribute ) ) )* otherlv_5= 'FROM' ( (lv_sources_6_0= ruleSource ) )+ (otherlv_7= ',' ( (lv_sources_8_0= ruleSource ) ) )* (otherlv_9= 'WHERE' ( (lv_conditions_10_0= ruleExpression ) )+ ) ) )
            // InternalCql.g:1017:2: ( ( (lv_name_0_0= 'SELECT' ) ) (otherlv_1= 'DISTINCT' )? ( (lv_attributes_2_0= ruleAttribute ) )+ (otherlv_3= ',' ( (lv_attributes_4_0= ruleAttribute ) ) )* otherlv_5= 'FROM' ( (lv_sources_6_0= ruleSource ) )+ (otherlv_7= ',' ( (lv_sources_8_0= ruleSource ) ) )* (otherlv_9= 'WHERE' ( (lv_conditions_10_0= ruleExpression ) )+ ) )
            {
            // InternalCql.g:1017:2: ( ( (lv_name_0_0= 'SELECT' ) ) (otherlv_1= 'DISTINCT' )? ( (lv_attributes_2_0= ruleAttribute ) )+ (otherlv_3= ',' ( (lv_attributes_4_0= ruleAttribute ) ) )* otherlv_5= 'FROM' ( (lv_sources_6_0= ruleSource ) )+ (otherlv_7= ',' ( (lv_sources_8_0= ruleSource ) ) )* (otherlv_9= 'WHERE' ( (lv_conditions_10_0= ruleExpression ) )+ ) )
            // InternalCql.g:1018:3: ( (lv_name_0_0= 'SELECT' ) ) (otherlv_1= 'DISTINCT' )? ( (lv_attributes_2_0= ruleAttribute ) )+ (otherlv_3= ',' ( (lv_attributes_4_0= ruleAttribute ) ) )* otherlv_5= 'FROM' ( (lv_sources_6_0= ruleSource ) )+ (otherlv_7= ',' ( (lv_sources_8_0= ruleSource ) ) )* (otherlv_9= 'WHERE' ( (lv_conditions_10_0= ruleExpression ) )+ )
            {
            // InternalCql.g:1018:3: ( (lv_name_0_0= 'SELECT' ) )
            // InternalCql.g:1019:4: (lv_name_0_0= 'SELECT' )
            {
            // InternalCql.g:1019:4: (lv_name_0_0= 'SELECT' )
            // InternalCql.g:1020:5: lv_name_0_0= 'SELECT'
            {
            lv_name_0_0=(Token)match(input,31,FOLLOW_15); 

            					newLeafNode(lv_name_0_0, grammarAccess.getSelect_StatementAccess().getNameSELECTKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSelect_StatementRule());
            					}
            					setWithLastConsumed(current, "name", lv_name_0_0, "SELECT");
            				

            }


            }

            // InternalCql.g:1032:3: (otherlv_1= 'DISTINCT' )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==32) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // InternalCql.g:1033:4: otherlv_1= 'DISTINCT'
                    {
                    otherlv_1=(Token)match(input,32,FOLLOW_15); 

                    				newLeafNode(otherlv_1, grammarAccess.getSelect_StatementAccess().getDISTINCTKeyword_1());
                    			

                    }
                    break;

            }

            // InternalCql.g:1038:3: ( (lv_attributes_2_0= ruleAttribute ) )+
            int cnt18=0;
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==RULE_ID) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // InternalCql.g:1039:4: (lv_attributes_2_0= ruleAttribute )
            	    {
            	    // InternalCql.g:1039:4: (lv_attributes_2_0= ruleAttribute )
            	    // InternalCql.g:1040:5: lv_attributes_2_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getSelect_StatementAccess().getAttributesAttributeParserRuleCall_2_0());
            	    				
            	    pushFollow(FOLLOW_16);
            	    lv_attributes_2_0=ruleAttribute();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
            	    					}
            	    					add(
            	    						current,
            	    						"attributes",
            	    						lv_attributes_2_0,
            	    						"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.Attribute");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt18 >= 1 ) break loop18;
                        EarlyExitException eee =
                            new EarlyExitException(18, input);
                        throw eee;
                }
                cnt18++;
            } while (true);

            // InternalCql.g:1057:3: (otherlv_3= ',' ( (lv_attributes_4_0= ruleAttribute ) ) )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==33) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // InternalCql.g:1058:4: otherlv_3= ',' ( (lv_attributes_4_0= ruleAttribute ) )
            	    {
            	    otherlv_3=(Token)match(input,33,FOLLOW_15); 

            	    				newLeafNode(otherlv_3, grammarAccess.getSelect_StatementAccess().getCommaKeyword_3_0());
            	    			
            	    // InternalCql.g:1062:4: ( (lv_attributes_4_0= ruleAttribute ) )
            	    // InternalCql.g:1063:5: (lv_attributes_4_0= ruleAttribute )
            	    {
            	    // InternalCql.g:1063:5: (lv_attributes_4_0= ruleAttribute )
            	    // InternalCql.g:1064:6: lv_attributes_4_0= ruleAttribute
            	    {

            	    						newCompositeNode(grammarAccess.getSelect_StatementAccess().getAttributesAttributeParserRuleCall_3_1_0());
            	    					
            	    pushFollow(FOLLOW_17);
            	    lv_attributes_4_0=ruleAttribute();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
            	    						}
            	    						add(
            	    							current,
            	    							"attributes",
            	    							lv_attributes_4_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.Attribute");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);

            otherlv_5=(Token)match(input,34,FOLLOW_6); 

            			newLeafNode(otherlv_5, grammarAccess.getSelect_StatementAccess().getFROMKeyword_4());
            		
            // InternalCql.g:1086:3: ( (lv_sources_6_0= ruleSource ) )+
            int cnt20=0;
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( (LA20_0==RULE_ID) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // InternalCql.g:1087:4: (lv_sources_6_0= ruleSource )
            	    {
            	    // InternalCql.g:1087:4: (lv_sources_6_0= ruleSource )
            	    // InternalCql.g:1088:5: lv_sources_6_0= ruleSource
            	    {

            	    					newCompositeNode(grammarAccess.getSelect_StatementAccess().getSourcesSourceParserRuleCall_5_0());
            	    				
            	    pushFollow(FOLLOW_18);
            	    lv_sources_6_0=ruleSource();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
            	    					}
            	    					add(
            	    						current,
            	    						"sources",
            	    						lv_sources_6_0,
            	    						"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.Source");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt20 >= 1 ) break loop20;
                        EarlyExitException eee =
                            new EarlyExitException(20, input);
                        throw eee;
                }
                cnt20++;
            } while (true);

            // InternalCql.g:1105:3: (otherlv_7= ',' ( (lv_sources_8_0= ruleSource ) ) )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==33) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // InternalCql.g:1106:4: otherlv_7= ',' ( (lv_sources_8_0= ruleSource ) )
            	    {
            	    otherlv_7=(Token)match(input,33,FOLLOW_6); 

            	    				newLeafNode(otherlv_7, grammarAccess.getSelect_StatementAccess().getCommaKeyword_6_0());
            	    			
            	    // InternalCql.g:1110:4: ( (lv_sources_8_0= ruleSource ) )
            	    // InternalCql.g:1111:5: (lv_sources_8_0= ruleSource )
            	    {
            	    // InternalCql.g:1111:5: (lv_sources_8_0= ruleSource )
            	    // InternalCql.g:1112:6: lv_sources_8_0= ruleSource
            	    {

            	    						newCompositeNode(grammarAccess.getSelect_StatementAccess().getSourcesSourceParserRuleCall_6_1_0());
            	    					
            	    pushFollow(FOLLOW_19);
            	    lv_sources_8_0=ruleSource();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
            	    						}
            	    						add(
            	    							current,
            	    							"sources",
            	    							lv_sources_8_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.Source");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);

            // InternalCql.g:1130:3: (otherlv_9= 'WHERE' ( (lv_conditions_10_0= ruleExpression ) )+ )
            // InternalCql.g:1131:4: otherlv_9= 'WHERE' ( (lv_conditions_10_0= ruleExpression ) )+
            {
            otherlv_9=(Token)match(input,35,FOLLOW_8); 

            				newLeafNode(otherlv_9, grammarAccess.getSelect_StatementAccess().getWHEREKeyword_7_0());
            			
            // InternalCql.g:1135:4: ( (lv_conditions_10_0= ruleExpression ) )+
            int cnt22=0;
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( ((LA22_0>=RULE_INT && LA22_0<=RULE_ID)||(LA22_0>=13 && LA22_0<=14)||LA22_0==28||LA22_0==30) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // InternalCql.g:1136:5: (lv_conditions_10_0= ruleExpression )
            	    {
            	    // InternalCql.g:1136:5: (lv_conditions_10_0= ruleExpression )
            	    // InternalCql.g:1137:6: lv_conditions_10_0= ruleExpression
            	    {

            	    						newCompositeNode(grammarAccess.getSelect_StatementAccess().getConditionsExpressionParserRuleCall_7_1_0());
            	    					
            	    pushFollow(FOLLOW_20);
            	    lv_conditions_10_0=ruleExpression();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
            	    						}
            	    						add(
            	    							current,
            	    							"conditions",
            	    							lv_conditions_10_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.Cql.Expression");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt22 >= 1 ) break loop22;
                        EarlyExitException eee =
                            new EarlyExitException(22, input);
                        throw eee;
                }
                cnt22++;
            } while (true);


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


    // $ANTLR start "entryRuleCreate_Statement"
    // InternalCql.g:1159:1: entryRuleCreate_Statement returns [EObject current=null] : iv_ruleCreate_Statement= ruleCreate_Statement EOF ;
    public final EObject entryRuleCreate_Statement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreate_Statement = null;


        try {
            // InternalCql.g:1159:57: (iv_ruleCreate_Statement= ruleCreate_Statement EOF )
            // InternalCql.g:1160:2: iv_ruleCreate_Statement= ruleCreate_Statement EOF
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
    // InternalCql.g:1166:1: ruleCreate_Statement returns [EObject current=null] : ( (lv_name_0_0= 'CREATE' ) ) ;
    public final EObject ruleCreate_Statement() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCql.g:1172:2: ( ( (lv_name_0_0= 'CREATE' ) ) )
            // InternalCql.g:1173:2: ( (lv_name_0_0= 'CREATE' ) )
            {
            // InternalCql.g:1173:2: ( (lv_name_0_0= 'CREATE' ) )
            // InternalCql.g:1174:3: (lv_name_0_0= 'CREATE' )
            {
            // InternalCql.g:1174:3: (lv_name_0_0= 'CREATE' )
            // InternalCql.g:1175:4: lv_name_0_0= 'CREATE'
            {
            lv_name_0_0=(Token)match(input,36,FOLLOW_2); 

            				newLeafNode(lv_name_0_0, grammarAccess.getCreate_StatementAccess().getNameCREATEKeyword_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getCreate_StatementRule());
            				}
            				setWithLastConsumed(current, "name", lv_name_0_0, "CREATE");
            			

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

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000001080000002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x00000000500060F0L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x00000000000C0002L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000000F00002L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000003000002L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x000000000C000002L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000100000080L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000700000080L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000600000000L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000A00000080L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000A00000000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x00000000500060F2L});

}