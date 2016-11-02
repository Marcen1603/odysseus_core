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
import de.uniol.inf.is.odysseus.parser.novel.cql.services.CQLGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalCQLParser extends AbstractInternalAntlrParser {
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
    // InternalCQL.g:65:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // InternalCQL.g:65:46: (iv_ruleModel= ruleModel EOF )
            // InternalCQL.g:66:2: iv_ruleModel= ruleModel EOF
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
    // InternalCQL.g:72:1: ruleModel returns [EObject current=null] : ( (lv_statements_0_0= ruleStatement ) )* ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        EObject lv_statements_0_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:78:2: ( ( (lv_statements_0_0= ruleStatement ) )* )
            // InternalCQL.g:79:2: ( (lv_statements_0_0= ruleStatement ) )*
            {
            // InternalCQL.g:79:2: ( (lv_statements_0_0= ruleStatement ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==31||LA1_0==36) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalCQL.g:80:3: (lv_statements_0_0= ruleStatement )
            	    {
            	    // InternalCQL.g:80:3: (lv_statements_0_0= ruleStatement )
            	    // InternalCQL.g:81:4: lv_statements_0_0= ruleStatement
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
    // InternalCQL.g:101:1: entryRuleStatement returns [EObject current=null] : iv_ruleStatement= ruleStatement EOF ;
    public final EObject entryRuleStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStatement = null;


        try {
            // InternalCQL.g:101:50: (iv_ruleStatement= ruleStatement EOF )
            // InternalCQL.g:102:2: iv_ruleStatement= ruleStatement EOF
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
    // InternalCQL.g:108:1: ruleStatement returns [EObject current=null] : ( ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) ) (otherlv_2= ';' )? ) ;
    public final EObject ruleStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject lv_type_0_0 = null;

        EObject lv_type_1_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:114:2: ( ( ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) ) (otherlv_2= ';' )? ) )
            // InternalCQL.g:115:2: ( ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) ) (otherlv_2= ';' )? )
            {
            // InternalCQL.g:115:2: ( ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) ) (otherlv_2= ';' )? )
            // InternalCQL.g:116:3: ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) ) (otherlv_2= ';' )?
            {
            // InternalCQL.g:116:3: ( ( (lv_type_0_0= ruleSelect_Statement ) ) | ( (lv_type_1_0= ruleCreate_Statement ) ) )
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
                    // InternalCQL.g:117:4: ( (lv_type_0_0= ruleSelect_Statement ) )
                    {
                    // InternalCQL.g:117:4: ( (lv_type_0_0= ruleSelect_Statement ) )
                    // InternalCQL.g:118:5: (lv_type_0_0= ruleSelect_Statement )
                    {
                    // InternalCQL.g:118:5: (lv_type_0_0= ruleSelect_Statement )
                    // InternalCQL.g:119:6: lv_type_0_0= ruleSelect_Statement
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
                    // InternalCQL.g:137:4: ( (lv_type_1_0= ruleCreate_Statement ) )
                    {
                    // InternalCQL.g:137:4: ( (lv_type_1_0= ruleCreate_Statement ) )
                    // InternalCQL.g:138:5: (lv_type_1_0= ruleCreate_Statement )
                    {
                    // InternalCQL.g:138:5: (lv_type_1_0= ruleCreate_Statement )
                    // InternalCQL.g:139:6: lv_type_1_0= ruleCreate_Statement
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

            }

            // InternalCQL.g:157:3: (otherlv_2= ';' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==12) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalCQL.g:158:4: otherlv_2= ';'
                    {
                    otherlv_2=(Token)match(input,12,FOLLOW_2); 

                    				newLeafNode(otherlv_2, grammarAccess.getStatementAccess().getSemicolonKeyword_1());
                    			

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


    // $ANTLR start "entryRuleAtomic"
    // InternalCQL.g:167:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCQL.g:167:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCQL.g:168:2: iv_ruleAtomic= ruleAtomic EOF
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
    // InternalCQL.g:174:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( (otherlv_9= RULE_ID ) ) ) ) ;
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
            // InternalCQL.g:180:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( (otherlv_9= RULE_ID ) ) ) ) )
            // InternalCQL.g:181:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( (otherlv_9= RULE_ID ) ) ) )
            {
            // InternalCQL.g:181:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( (otherlv_9= RULE_ID ) ) ) )
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
            case 13:
            case 14:
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
                    // InternalCQL.g:182:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQL.g:182:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQL.g:183:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQL.g:183:4: ()
                    // InternalCQL.g:184:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:190:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQL.g:191:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQL.g:191:5: (lv_value_1_0= RULE_INT )
                    // InternalCQL.g:192:6: lv_value_1_0= RULE_INT
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
                    // InternalCQL.g:210:3: ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) )
                    {
                    // InternalCQL.g:210:3: ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) )
                    // InternalCQL.g:211:4: () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) )
                    {
                    // InternalCQL.g:211:4: ()
                    // InternalCQL.g:212:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:218:4: ( (lv_value_3_0= RULE_FLOAT_NUMBER ) )
                    // InternalCQL.g:219:5: (lv_value_3_0= RULE_FLOAT_NUMBER )
                    {
                    // InternalCQL.g:219:5: (lv_value_3_0= RULE_FLOAT_NUMBER )
                    // InternalCQL.g:220:6: lv_value_3_0= RULE_FLOAT_NUMBER
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
                    // InternalCQL.g:238:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQL.g:238:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQL.g:239:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQL.g:239:4: ()
                    // InternalCQL.g:240:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:246:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQL.g:247:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQL.g:247:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQL.g:248:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQL.g:266:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    {
                    // InternalCQL.g:266:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    // InternalCQL.g:267:4: () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    {
                    // InternalCQL.g:267:4: ()
                    // InternalCQL.g:268:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:274:4: ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    // InternalCQL.g:275:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    {
                    // InternalCQL.g:275:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    // InternalCQL.g:276:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    {
                    // InternalCQL.g:276:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
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
                            // InternalCQL.g:277:7: lv_value_7_1= 'TRUE'
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
                            // InternalCQL.g:288:7: lv_value_7_2= 'FALSE'
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
                    // InternalCQL.g:303:3: ( () ( (otherlv_9= RULE_ID ) ) )
                    {
                    // InternalCQL.g:303:3: ( () ( (otherlv_9= RULE_ID ) ) )
                    // InternalCQL.g:304:4: () ( (otherlv_9= RULE_ID ) )
                    {
                    // InternalCQL.g:304:4: ()
                    // InternalCQL.g:305:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:311:4: ( (otherlv_9= RULE_ID ) )
                    // InternalCQL.g:312:5: (otherlv_9= RULE_ID )
                    {
                    // InternalCQL.g:312:5: (otherlv_9= RULE_ID )
                    // InternalCQL.g:313:6: otherlv_9= RULE_ID
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
    // InternalCQL.g:329:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalCQL.g:329:47: (iv_ruleSource= ruleSource EOF )
            // InternalCQL.g:330:2: iv_ruleSource= ruleSource EOF
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
    // InternalCQL.g:336:1: ruleSource returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleSource() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQL.g:342:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQL.g:343:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQL.g:343:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:344:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:344:3: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:345:4: lv_name_0_0= RULE_ID
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
    // InternalCQL.g:364:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCQL.g:364:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCQL.g:365:2: iv_ruleAttribute= ruleAttribute EOF
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
    // InternalCQL.g:371:1: ruleAttribute returns [EObject current=null] : ( ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' )? ( (lv_name_2_0= RULE_ID ) ) ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;


        	enterRule();

        try {
            // InternalCQL.g:377:2: ( ( ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' )? ( (lv_name_2_0= RULE_ID ) ) ) )
            // InternalCQL.g:378:2: ( ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' )? ( (lv_name_2_0= RULE_ID ) ) )
            {
            // InternalCQL.g:378:2: ( ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' )? ( (lv_name_2_0= RULE_ID ) ) )
            // InternalCQL.g:379:3: ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' )? ( (lv_name_2_0= RULE_ID ) )
            {
            // InternalCQL.g:379:3: ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '.' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==RULE_ID) ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1==15) ) {
                    alt6=1;
                }
            }
            switch (alt6) {
                case 1 :
                    // InternalCQL.g:380:4: ( (otherlv_0= RULE_ID ) ) otherlv_1= '.'
                    {
                    // InternalCQL.g:380:4: ( (otherlv_0= RULE_ID ) )
                    // InternalCQL.g:381:5: (otherlv_0= RULE_ID )
                    {
                    // InternalCQL.g:381:5: (otherlv_0= RULE_ID )
                    // InternalCQL.g:382:6: otherlv_0= RULE_ID
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

            // InternalCQL.g:398:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCQL.g:399:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCQL.g:399:4: (lv_name_2_0= RULE_ID )
            // InternalCQL.g:400:5: lv_name_2_0= RULE_ID
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


    // $ANTLR start "entryRuleExpressionsModel"
    // InternalCQL.g:420:1: entryRuleExpressionsModel returns [EObject current=null] : iv_ruleExpressionsModel= ruleExpressionsModel EOF ;
    public final EObject entryRuleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionsModel = null;


        try {
            // InternalCQL.g:420:57: (iv_ruleExpressionsModel= ruleExpressionsModel EOF )
            // InternalCQL.g:421:2: iv_ruleExpressionsModel= ruleExpressionsModel EOF
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
    // InternalCQL.g:427:1: ruleExpressionsModel returns [EObject current=null] : ( () ( (lv_elements_1_0= ruleExpression ) )+ ) ;
    public final EObject ruleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:433:2: ( ( () ( (lv_elements_1_0= ruleExpression ) )+ ) )
            // InternalCQL.g:434:2: ( () ( (lv_elements_1_0= ruleExpression ) )+ )
            {
            // InternalCQL.g:434:2: ( () ( (lv_elements_1_0= ruleExpression ) )+ )
            // InternalCQL.g:435:3: () ( (lv_elements_1_0= ruleExpression ) )+
            {
            // InternalCQL.g:435:3: ()
            // InternalCQL.g:436:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0(),
            					current);
            			

            }

            // InternalCQL.g:442:3: ( (lv_elements_1_0= ruleExpression ) )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>=RULE_INT && LA7_0<=RULE_ID)||(LA7_0>=13 && LA7_0<=14)||LA7_0==28||LA7_0==30) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // InternalCQL.g:443:4: (lv_elements_1_0= ruleExpression )
            	    {
            	    // InternalCQL.g:443:4: (lv_elements_1_0= ruleExpression )
            	    // InternalCQL.g:444:5: lv_elements_1_0= ruleExpression
            	    {

            	    					newCompositeNode(grammarAccess.getExpressionsModelAccess().getElementsExpressionParserRuleCall_1_0());
            	    				
            	    pushFollow(FOLLOW_7);
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
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
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
    // InternalCQL.g:465:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCQL.g:465:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCQL.g:466:2: iv_ruleExpression= ruleExpression EOF
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
    // InternalCQL.g:472:1: ruleExpression returns [EObject current=null] : this_Or_0= ruleOr ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_Or_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:478:2: (this_Or_0= ruleOr )
            // InternalCQL.g:479:2: this_Or_0= ruleOr
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
    // InternalCQL.g:490:1: entryRuleOr returns [EObject current=null] : iv_ruleOr= ruleOr EOF ;
    public final EObject entryRuleOr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOr = null;


        try {
            // InternalCQL.g:490:43: (iv_ruleOr= ruleOr EOF )
            // InternalCQL.g:491:2: iv_ruleOr= ruleOr EOF
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
    // InternalCQL.g:497:1: ruleOr returns [EObject current=null] : (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) ;
    public final EObject ruleOr() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_And_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:503:2: ( (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) )
            // InternalCQL.g:504:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            {
            // InternalCQL.g:504:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            // InternalCQL.g:505:3: this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrAccess().getAndParserRuleCall_0());
            		
            pushFollow(FOLLOW_8);
            this_And_0=ruleAnd();

            state._fsp--;


            			current = this_And_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:513:3: ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==16) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // InternalCQL.g:514:4: () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) )
            	    {
            	    // InternalCQL.g:514:4: ()
            	    // InternalCQL.g:515:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrAccess().getOrLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,16,FOLLOW_9); 

            	    				newLeafNode(otherlv_2, grammarAccess.getOrAccess().getORKeyword_1_1());
            	    			
            	    // InternalCQL.g:525:4: ( (lv_right_3_0= ruleAnd ) )
            	    // InternalCQL.g:526:5: (lv_right_3_0= ruleAnd )
            	    {
            	    // InternalCQL.g:526:5: (lv_right_3_0= ruleAnd )
            	    // InternalCQL.g:527:6: lv_right_3_0= ruleAnd
            	    {

            	    						newCompositeNode(grammarAccess.getOrAccess().getRightAndParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_8);
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
            	    break loop8;
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
    // InternalCQL.g:549:1: entryRuleAnd returns [EObject current=null] : iv_ruleAnd= ruleAnd EOF ;
    public final EObject entryRuleAnd() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnd = null;


        try {
            // InternalCQL.g:549:44: (iv_ruleAnd= ruleAnd EOF )
            // InternalCQL.g:550:2: iv_ruleAnd= ruleAnd EOF
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
    // InternalCQL.g:556:1: ruleAnd returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAnd() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:562:2: ( (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCQL.g:563:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCQL.g:563:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCQL.g:564:3: this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_10);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:572:3: ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==17) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // InternalCQL.g:573:4: () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCQL.g:573:4: ()
            	    // InternalCQL.g:574:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndAccess().getAndLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,17,FOLLOW_11); 

            	    				newLeafNode(otherlv_2, grammarAccess.getAndAccess().getANDKeyword_1_1());
            	    			
            	    // InternalCQL.g:584:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCQL.g:585:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCQL.g:585:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCQL.g:586:6: lv_right_3_0= ruleEqualitiy
            	    {

            	    						newCompositeNode(grammarAccess.getAndAccess().getRightEqualitiyParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_10);
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
    // $ANTLR end "ruleAnd"


    // $ANTLR start "entryRuleEqualitiy"
    // InternalCQL.g:608:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCQL.g:608:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCQL.g:609:2: iv_ruleEqualitiy= ruleEqualitiy EOF
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
    // InternalCQL.g:615:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Comparison_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:621:2: ( (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCQL.g:622:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCQL.g:622:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCQL.g:623:3: this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_12);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:631:3: ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>=18 && LA11_0<=19)) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // InternalCQL.g:632:4: () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCQL.g:632:4: ()
            	    // InternalCQL.g:633:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:639:4: ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) )
            	    // InternalCQL.g:640:5: ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) )
            	    {
            	    // InternalCQL.g:640:5: ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) )
            	    // InternalCQL.g:641:6: (lv_op_2_1= '==' | lv_op_2_2= '!=' )
            	    {
            	    // InternalCQL.g:641:6: (lv_op_2_1= '==' | lv_op_2_2= '!=' )
            	    int alt10=2;
            	    int LA10_0 = input.LA(1);

            	    if ( (LA10_0==18) ) {
            	        alt10=1;
            	    }
            	    else if ( (LA10_0==19) ) {
            	        alt10=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 10, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt10) {
            	        case 1 :
            	            // InternalCQL.g:642:7: lv_op_2_1= '=='
            	            {
            	            lv_op_2_1=(Token)match(input,18,FOLLOW_13); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getEqualitiyAccess().getOpEqualsSignEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getEqualitiyRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:653:7: lv_op_2_2= '!='
            	            {
            	            lv_op_2_2=(Token)match(input,19,FOLLOW_13); 

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

            	    // InternalCQL.g:666:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCQL.g:667:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCQL.g:667:5: (lv_right_3_0= ruleComparison )
            	    // InternalCQL.g:668:6: lv_right_3_0= ruleComparison
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getRightComparisonParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_12);
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
    // $ANTLR end "ruleEqualitiy"


    // $ANTLR start "entryRuleComparison"
    // InternalCQL.g:690:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCQL.g:690:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCQL.g:691:2: iv_ruleComparison= ruleComparison EOF
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
    // InternalCQL.g:697:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
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
            // InternalCQL.g:703:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCQL.g:704:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCQL.g:704:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCQL.g:705:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_14);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:713:3: ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>=20 && LA13_0<=23)) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // InternalCQL.g:714:4: () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCQL.g:714:4: ()
            	    // InternalCQL.g:715:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:721:4: ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) )
            	    // InternalCQL.g:722:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    {
            	    // InternalCQL.g:722:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    // InternalCQL.g:723:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    {
            	    // InternalCQL.g:723:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    int alt12=4;
            	    switch ( input.LA(1) ) {
            	    case 20:
            	        {
            	        alt12=1;
            	        }
            	        break;
            	    case 21:
            	        {
            	        alt12=2;
            	        }
            	        break;
            	    case 22:
            	        {
            	        alt12=3;
            	        }
            	        break;
            	    case 23:
            	        {
            	        alt12=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 12, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt12) {
            	        case 1 :
            	            // InternalCQL.g:724:7: lv_op_2_1= '>='
            	            {
            	            lv_op_2_1=(Token)match(input,20,FOLLOW_15); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:735:7: lv_op_2_2= '<='
            	            {
            	            lv_op_2_2=(Token)match(input,21,FOLLOW_15); 

            	            							newLeafNode(lv_op_2_2, grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_2, null);
            	            						

            	            }
            	            break;
            	        case 3 :
            	            // InternalCQL.g:746:7: lv_op_2_3= '<'
            	            {
            	            lv_op_2_3=(Token)match(input,22,FOLLOW_15); 

            	            							newLeafNode(lv_op_2_3, grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_3, null);
            	            						

            	            }
            	            break;
            	        case 4 :
            	            // InternalCQL.g:757:7: lv_op_2_4= '>'
            	            {
            	            lv_op_2_4=(Token)match(input,23,FOLLOW_15); 

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

            	    // InternalCQL.g:770:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCQL.g:771:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCQL.g:771:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCQL.g:772:6: lv_right_3_0= rulePlusOrMinus
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getRightPlusOrMinusParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_14);
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
    // $ANTLR end "ruleComparison"


    // $ANTLR start "entryRulePlusOrMinus"
    // InternalCQL.g:794:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCQL.g:794:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCQL.g:795:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
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
    // InternalCQL.g:801:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:807:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCQL.g:808:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCQL.g:808:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCQL.g:809:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_16);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:817:3: ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( ((LA15_0>=24 && LA15_0<=25)) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // InternalCQL.g:818:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCQL.g:818:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) )
            	    int alt14=2;
            	    int LA14_0 = input.LA(1);

            	    if ( (LA14_0==24) ) {
            	        alt14=1;
            	    }
            	    else if ( (LA14_0==25) ) {
            	        alt14=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 14, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt14) {
            	        case 1 :
            	            // InternalCQL.g:819:5: ( () otherlv_2= '+' )
            	            {
            	            // InternalCQL.g:819:5: ( () otherlv_2= '+' )
            	            // InternalCQL.g:820:6: () otherlv_2= '+'
            	            {
            	            // InternalCQL.g:820:6: ()
            	            // InternalCQL.g:821:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_2=(Token)match(input,24,FOLLOW_17); 

            	            						newLeafNode(otherlv_2, grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1());
            	            					

            	            }


            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:833:5: ( () otherlv_4= '-' )
            	            {
            	            // InternalCQL.g:833:5: ( () otherlv_4= '-' )
            	            // InternalCQL.g:834:6: () otherlv_4= '-'
            	            {
            	            // InternalCQL.g:834:6: ()
            	            // InternalCQL.g:835:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_4=(Token)match(input,25,FOLLOW_17); 

            	            						newLeafNode(otherlv_4, grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1());
            	            					

            	            }


            	            }
            	            break;

            	    }

            	    // InternalCQL.g:847:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCQL.g:848:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCQL.g:848:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCQL.g:849:6: lv_right_5_0= ruleMulOrDiv
            	    {

            	    						newCompositeNode(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_16);
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
    // $ANTLR end "rulePlusOrMinus"


    // $ANTLR start "entryRuleMulOrDiv"
    // InternalCQL.g:871:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCQL.g:871:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCQL.g:872:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
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
    // InternalCQL.g:878:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:884:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCQL.g:885:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCQL.g:885:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCQL.g:886:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_18);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:894:3: ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( ((LA17_0>=26 && LA17_0<=27)) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // InternalCQL.g:895:4: () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCQL.g:895:4: ()
            	    // InternalCQL.g:896:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:902:4: ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) )
            	    // InternalCQL.g:903:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    {
            	    // InternalCQL.g:903:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    // InternalCQL.g:904:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    {
            	    // InternalCQL.g:904:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    int alt16=2;
            	    int LA16_0 = input.LA(1);

            	    if ( (LA16_0==26) ) {
            	        alt16=1;
            	    }
            	    else if ( (LA16_0==27) ) {
            	        alt16=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 16, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt16) {
            	        case 1 :
            	            // InternalCQL.g:905:7: lv_op_2_1= '*'
            	            {
            	            lv_op_2_1=(Token)match(input,26,FOLLOW_19); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getMulOrDivRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:916:7: lv_op_2_2= '/'
            	            {
            	            lv_op_2_2=(Token)match(input,27,FOLLOW_19); 

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

            	    // InternalCQL.g:929:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCQL.g:930:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCQL.g:930:5: (lv_right_3_0= rulePrimary )
            	    // InternalCQL.g:931:6: lv_right_3_0= rulePrimary
            	    {

            	    						newCompositeNode(grammarAccess.getMulOrDivAccess().getRightPrimaryParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_18);
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
    // $ANTLR end "ruleMulOrDiv"


    // $ANTLR start "entryRulePrimary"
    // InternalCQL.g:953:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCQL.g:953:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCQL.g:954:2: iv_rulePrimary= rulePrimary EOF
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
    // InternalCQL.g:960:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
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
            // InternalCQL.g:966:2: ( ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCQL.g:967:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCQL.g:967:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt18=3;
            switch ( input.LA(1) ) {
            case 28:
                {
                alt18=1;
                }
                break;
            case 30:
                {
                alt18=2;
                }
                break;
            case RULE_INT:
            case RULE_FLOAT_NUMBER:
            case RULE_STRING:
            case RULE_ID:
            case 13:
            case 14:
                {
                alt18=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;
            }

            switch (alt18) {
                case 1 :
                    // InternalCQL.g:968:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    {
                    // InternalCQL.g:968:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    // InternalCQL.g:969:4: () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')'
                    {
                    // InternalCQL.g:969:4: ()
                    // InternalCQL.g:970:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,28,FOLLOW_20); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCQL.g:980:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCQL.g:981:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCQL.g:981:5: (lv_inner_2_0= ruleExpression )
                    // InternalCQL.g:982:6: lv_inner_2_0= ruleExpression
                    {

                    						newCompositeNode(grammarAccess.getPrimaryAccess().getInnerExpressionParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_21);
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

                    otherlv_3=(Token)match(input,29,FOLLOW_2); 

                    				newLeafNode(otherlv_3, grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_0_3());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1005:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCQL.g:1005:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCQL.g:1006:4: () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCQL.g:1006:4: ()
                    // InternalCQL.g:1007:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,30,FOLLOW_22); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCQL.g:1017:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCQL.g:1018:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCQL.g:1018:5: (lv_expression_6_0= rulePrimary )
                    // InternalCQL.g:1019:6: lv_expression_6_0= rulePrimary
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
                    // InternalCQL.g:1038:3: this_Atomic_7= ruleAtomic
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
    // InternalCQL.g:1050:1: entryRuleSelect_Statement returns [EObject current=null] : iv_ruleSelect_Statement= ruleSelect_Statement EOF ;
    public final EObject entryRuleSelect_Statement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelect_Statement = null;


        try {
            // InternalCQL.g:1050:57: (iv_ruleSelect_Statement= ruleSelect_Statement EOF )
            // InternalCQL.g:1051:2: iv_ruleSelect_Statement= ruleSelect_Statement EOF
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
    // InternalCQL.g:1057:1: ruleSelect_Statement returns [EObject current=null] : ( ( (lv_name_0_0= 'SELECT' ) ) (otherlv_1= 'DISTINCT' )? ( (lv_attributes_2_0= ruleAttribute ) )+ (otherlv_3= ',' ( (lv_attributes_4_0= ruleAttribute ) ) )* otherlv_5= 'FROM' ( (lv_sources_6_0= ruleSource ) )+ (otherlv_7= ',' ( (lv_sources_8_0= ruleSource ) ) )* (otherlv_9= 'WHERE' ( (lv_predicates_10_0= ruleExpressionsModel ) ) ) ) ;
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

        EObject lv_predicates_10_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1063:2: ( ( ( (lv_name_0_0= 'SELECT' ) ) (otherlv_1= 'DISTINCT' )? ( (lv_attributes_2_0= ruleAttribute ) )+ (otherlv_3= ',' ( (lv_attributes_4_0= ruleAttribute ) ) )* otherlv_5= 'FROM' ( (lv_sources_6_0= ruleSource ) )+ (otherlv_7= ',' ( (lv_sources_8_0= ruleSource ) ) )* (otherlv_9= 'WHERE' ( (lv_predicates_10_0= ruleExpressionsModel ) ) ) ) )
            // InternalCQL.g:1064:2: ( ( (lv_name_0_0= 'SELECT' ) ) (otherlv_1= 'DISTINCT' )? ( (lv_attributes_2_0= ruleAttribute ) )+ (otherlv_3= ',' ( (lv_attributes_4_0= ruleAttribute ) ) )* otherlv_5= 'FROM' ( (lv_sources_6_0= ruleSource ) )+ (otherlv_7= ',' ( (lv_sources_8_0= ruleSource ) ) )* (otherlv_9= 'WHERE' ( (lv_predicates_10_0= ruleExpressionsModel ) ) ) )
            {
            // InternalCQL.g:1064:2: ( ( (lv_name_0_0= 'SELECT' ) ) (otherlv_1= 'DISTINCT' )? ( (lv_attributes_2_0= ruleAttribute ) )+ (otherlv_3= ',' ( (lv_attributes_4_0= ruleAttribute ) ) )* otherlv_5= 'FROM' ( (lv_sources_6_0= ruleSource ) )+ (otherlv_7= ',' ( (lv_sources_8_0= ruleSource ) ) )* (otherlv_9= 'WHERE' ( (lv_predicates_10_0= ruleExpressionsModel ) ) ) )
            // InternalCQL.g:1065:3: ( (lv_name_0_0= 'SELECT' ) ) (otherlv_1= 'DISTINCT' )? ( (lv_attributes_2_0= ruleAttribute ) )+ (otherlv_3= ',' ( (lv_attributes_4_0= ruleAttribute ) ) )* otherlv_5= 'FROM' ( (lv_sources_6_0= ruleSource ) )+ (otherlv_7= ',' ( (lv_sources_8_0= ruleSource ) ) )* (otherlv_9= 'WHERE' ( (lv_predicates_10_0= ruleExpressionsModel ) ) )
            {
            // InternalCQL.g:1065:3: ( (lv_name_0_0= 'SELECT' ) )
            // InternalCQL.g:1066:4: (lv_name_0_0= 'SELECT' )
            {
            // InternalCQL.g:1066:4: (lv_name_0_0= 'SELECT' )
            // InternalCQL.g:1067:5: lv_name_0_0= 'SELECT'
            {
            lv_name_0_0=(Token)match(input,31,FOLLOW_23); 

            					newLeafNode(lv_name_0_0, grammarAccess.getSelect_StatementAccess().getNameSELECTKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSelect_StatementRule());
            					}
            					setWithLastConsumed(current, "name", lv_name_0_0, "SELECT");
            				

            }


            }

            // InternalCQL.g:1079:3: (otherlv_1= 'DISTINCT' )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==32) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // InternalCQL.g:1080:4: otherlv_1= 'DISTINCT'
                    {
                    otherlv_1=(Token)match(input,32,FOLLOW_23); 

                    				newLeafNode(otherlv_1, grammarAccess.getSelect_StatementAccess().getDISTINCTKeyword_1());
                    			

                    }
                    break;

            }

            // InternalCQL.g:1085:3: ( (lv_attributes_2_0= ruleAttribute ) )+
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
            	    // InternalCQL.g:1086:4: (lv_attributes_2_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:1086:4: (lv_attributes_2_0= ruleAttribute )
            	    // InternalCQL.g:1087:5: lv_attributes_2_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getSelect_StatementAccess().getAttributesAttributeParserRuleCall_2_0());
            	    				
            	    pushFollow(FOLLOW_24);
            	    lv_attributes_2_0=ruleAttribute();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
            	    					}
            	    					add(
            	    						current,
            	    						"attributes",
            	    						lv_attributes_2_0,
            	    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
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

            // InternalCQL.g:1104:3: (otherlv_3= ',' ( (lv_attributes_4_0= ruleAttribute ) ) )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==33) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // InternalCQL.g:1105:4: otherlv_3= ',' ( (lv_attributes_4_0= ruleAttribute ) )
            	    {
            	    otherlv_3=(Token)match(input,33,FOLLOW_23); 

            	    				newLeafNode(otherlv_3, grammarAccess.getSelect_StatementAccess().getCommaKeyword_3_0());
            	    			
            	    // InternalCQL.g:1109:4: ( (lv_attributes_4_0= ruleAttribute ) )
            	    // InternalCQL.g:1110:5: (lv_attributes_4_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:1110:5: (lv_attributes_4_0= ruleAttribute )
            	    // InternalCQL.g:1111:6: lv_attributes_4_0= ruleAttribute
            	    {

            	    						newCompositeNode(grammarAccess.getSelect_StatementAccess().getAttributesAttributeParserRuleCall_3_1_0());
            	    					
            	    pushFollow(FOLLOW_25);
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

            	default :
            	    break loop21;
                }
            } while (true);

            otherlv_5=(Token)match(input,34,FOLLOW_6); 

            			newLeafNode(otherlv_5, grammarAccess.getSelect_StatementAccess().getFROMKeyword_4());
            		
            // InternalCQL.g:1133:3: ( (lv_sources_6_0= ruleSource ) )+
            int cnt22=0;
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==RULE_ID) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // InternalCQL.g:1134:4: (lv_sources_6_0= ruleSource )
            	    {
            	    // InternalCQL.g:1134:4: (lv_sources_6_0= ruleSource )
            	    // InternalCQL.g:1135:5: lv_sources_6_0= ruleSource
            	    {

            	    					newCompositeNode(grammarAccess.getSelect_StatementAccess().getSourcesSourceParserRuleCall_5_0());
            	    				
            	    pushFollow(FOLLOW_26);
            	    lv_sources_6_0=ruleSource();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
            	    					}
            	    					add(
            	    						current,
            	    						"sources",
            	    						lv_sources_6_0,
            	    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Source");
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

            // InternalCQL.g:1152:3: (otherlv_7= ',' ( (lv_sources_8_0= ruleSource ) ) )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==33) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // InternalCQL.g:1153:4: otherlv_7= ',' ( (lv_sources_8_0= ruleSource ) )
            	    {
            	    otherlv_7=(Token)match(input,33,FOLLOW_6); 

            	    				newLeafNode(otherlv_7, grammarAccess.getSelect_StatementAccess().getCommaKeyword_6_0());
            	    			
            	    // InternalCQL.g:1157:4: ( (lv_sources_8_0= ruleSource ) )
            	    // InternalCQL.g:1158:5: (lv_sources_8_0= ruleSource )
            	    {
            	    // InternalCQL.g:1158:5: (lv_sources_8_0= ruleSource )
            	    // InternalCQL.g:1159:6: lv_sources_8_0= ruleSource
            	    {

            	    						newCompositeNode(grammarAccess.getSelect_StatementAccess().getSourcesSourceParserRuleCall_6_1_0());
            	    					
            	    pushFollow(FOLLOW_27);
            	    lv_sources_8_0=ruleSource();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
            	    						}
            	    						add(
            	    							current,
            	    							"sources",
            	    							lv_sources_8_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Source");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop23;
                }
            } while (true);

            // InternalCQL.g:1177:3: (otherlv_9= 'WHERE' ( (lv_predicates_10_0= ruleExpressionsModel ) ) )
            // InternalCQL.g:1178:4: otherlv_9= 'WHERE' ( (lv_predicates_10_0= ruleExpressionsModel ) )
            {
            otherlv_9=(Token)match(input,35,FOLLOW_22); 

            				newLeafNode(otherlv_9, grammarAccess.getSelect_StatementAccess().getWHEREKeyword_7_0());
            			
            // InternalCQL.g:1182:4: ( (lv_predicates_10_0= ruleExpressionsModel ) )
            // InternalCQL.g:1183:5: (lv_predicates_10_0= ruleExpressionsModel )
            {
            // InternalCQL.g:1183:5: (lv_predicates_10_0= ruleExpressionsModel )
            // InternalCQL.g:1184:6: lv_predicates_10_0= ruleExpressionsModel
            {

            						newCompositeNode(grammarAccess.getSelect_StatementAccess().getPredicatesExpressionsModelParserRuleCall_7_1_0());
            					
            pushFollow(FOLLOW_2);
            lv_predicates_10_0=ruleExpressionsModel();

            state._fsp--;


            						if (current==null) {
            							current = createModelElementForParent(grammarAccess.getSelect_StatementRule());
            						}
            						set(
            							current,
            							"predicates",
            							lv_predicates_10_0,
            							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionsModel");
            						afterParserOrEnumRuleCall();
            					

            }


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
    // $ANTLR end "ruleSelect_Statement"


    // $ANTLR start "entryRuleCreate_Statement"
    // InternalCQL.g:1206:1: entryRuleCreate_Statement returns [EObject current=null] : iv_ruleCreate_Statement= ruleCreate_Statement EOF ;
    public final EObject entryRuleCreate_Statement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreate_Statement = null;


        try {
            // InternalCQL.g:1206:57: (iv_ruleCreate_Statement= ruleCreate_Statement EOF )
            // InternalCQL.g:1207:2: iv_ruleCreate_Statement= ruleCreate_Statement EOF
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
    // InternalCQL.g:1213:1: ruleCreate_Statement returns [EObject current=null] : ( (lv_name_0_0= 'CREATE' ) ) ;
    public final EObject ruleCreate_Statement() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQL.g:1219:2: ( ( (lv_name_0_0= 'CREATE' ) ) )
            // InternalCQL.g:1220:2: ( (lv_name_0_0= 'CREATE' ) )
            {
            // InternalCQL.g:1220:2: ( (lv_name_0_0= 'CREATE' ) )
            // InternalCQL.g:1221:3: (lv_name_0_0= 'CREATE' )
            {
            // InternalCQL.g:1221:3: (lv_name_0_0= 'CREATE' )
            // InternalCQL.g:1222:4: lv_name_0_0= 'CREATE'
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
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x00000000500060F2L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x00000000500160F0L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x00000000500260F0L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x00000000000C0002L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x00000000500C60F0L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000000F00002L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000050F060F0L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000003000002L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x00000000530060F0L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x000000000C000002L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x000000005C0060F0L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x00000000700060F0L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x00000000500060F0L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000100000080L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000700000080L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000600000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000A00000080L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000A00000000L});

}