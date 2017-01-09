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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_FLOAT_NUMBER", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "';'", "'('", "')'", "'INTEGER'", "'DOUBLE'", "'FLOAT'", "'STRING'", "'BOOLEAN'", "'STARTTIMESTAMP'", "'ENDTIMESTAMP'", "'['", "']'", "'AS'", "'SELECT'", "'DISTINCT'", "'*'", "','", "'FROM'", "'WHERE'", "'GROUP'", "'BY'", "'HAVING'", "'CREATE'", "'ATTACH'", "'STREAM'", "'SINK'", "'WRAPPER'", "'PROTOCOL'", "'TRANSPORT'", "'DATAHANDLER'", "'OPTIONS'", "'CHANNEL'", "':'", "'VIEW'", "'TO'", "'DROP'", "'IF'", "'EXISTS'", "'UNBOUNDED'", "'SIZE'", "'ADVANCE'", "'TIME'", "'TUPLE'", "'PARTITION'", "'OR'", "'AND'", "'=='", "'!='", "'>='", "'<='", "'<'", "'>'", "'+'", "'-'", "'/'", "'NOT'", "'TRUE'", "'FALSE'"
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
    public static final int RULE_ID=4;
    public static final int RULE_FLOAT_NUMBER=7;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=6;
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
    public static final int RULE_STRING=5;
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

                if ( (LA1_0==25||(LA1_0>=34 && LA1_0<=36)||LA1_0==47) ) {
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
    // InternalCQL.g:107:1: ruleStatement returns [EObject current=null] : ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleCreate ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) ) (otherlv_4= ';' )? ) ;
    public final EObject ruleStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_4=null;
        EObject lv_type_0_0 = null;

        EObject lv_type_1_0 = null;

        EObject lv_type_2_0 = null;

        EObject lv_type_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:113:2: ( ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleCreate ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) ) (otherlv_4= ';' )? ) )
            // InternalCQL.g:114:2: ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleCreate ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) ) (otherlv_4= ';' )? )
            {
            // InternalCQL.g:114:2: ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleCreate ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) ) (otherlv_4= ';' )? )
            // InternalCQL.g:115:3: ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleCreate ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) ) (otherlv_4= ';' )?
            {
            // InternalCQL.g:115:3: ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleCreate ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) )
            int alt2=4;
            switch ( input.LA(1) ) {
            case 25:
                {
                alt2=1;
                }
                break;
            case 34:
            case 35:
                {
                alt2=2;
                }
                break;
            case 36:
                {
                alt2=3;
                }
                break;
            case 47:
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
                    // InternalCQL.g:116:4: ( (lv_type_0_0= ruleSelect ) )
                    {
                    // InternalCQL.g:116:4: ( (lv_type_0_0= ruleSelect ) )
                    // InternalCQL.g:117:5: (lv_type_0_0= ruleSelect )
                    {
                    // InternalCQL.g:117:5: (lv_type_0_0= ruleSelect )
                    // InternalCQL.g:118:6: lv_type_0_0= ruleSelect
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeSelectParserRuleCall_0_0_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_0_0=ruleSelect();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_0_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Select");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:136:4: ( (lv_type_1_0= ruleCreate ) )
                    {
                    // InternalCQL.g:136:4: ( (lv_type_1_0= ruleCreate ) )
                    // InternalCQL.g:137:5: (lv_type_1_0= ruleCreate )
                    {
                    // InternalCQL.g:137:5: (lv_type_1_0= ruleCreate )
                    // InternalCQL.g:138:6: lv_type_1_0= ruleCreate
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeCreateParserRuleCall_0_1_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_1_0=ruleCreate();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_1_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Create");
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


    // $ANTLR start "entryRuleNestedStatement"
    // InternalCQL.g:206:1: entryRuleNestedStatement returns [EObject current=null] : iv_ruleNestedStatement= ruleNestedStatement EOF ;
    public final EObject entryRuleNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNestedStatement = null;


        try {
            // InternalCQL.g:206:56: (iv_ruleNestedStatement= ruleNestedStatement EOF )
            // InternalCQL.g:207:2: iv_ruleNestedStatement= ruleNestedStatement EOF
            {
             newCompositeNode(grammarAccess.getNestedStatementRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleNestedStatement=ruleNestedStatement();

            state._fsp--;

             current =iv_ruleNestedStatement; 
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
    // $ANTLR end "entryRuleNestedStatement"


    // $ANTLR start "ruleNestedStatement"
    // InternalCQL.g:213:1: ruleNestedStatement returns [EObject current=null] : (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' ) ;
    public final EObject ruleNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject this_Select_1 = null;



        	enterRule();

        try {
            // InternalCQL.g:219:2: ( (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' ) )
            // InternalCQL.g:220:2: (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' )
            {
            // InternalCQL.g:220:2: (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' )
            // InternalCQL.g:221:3: otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')'
            {
            otherlv_0=(Token)match(input,13,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getNestedStatementAccess().getLeftParenthesisKeyword_0());
            		

            			newCompositeNode(grammarAccess.getNestedStatementAccess().getSelectParserRuleCall_1());
            		
            pushFollow(FOLLOW_6);
            this_Select_1=ruleSelect();

            state._fsp--;


            			current = this_Select_1;
            			afterParserOrEnumRuleCall();
            		
            otherlv_2=(Token)match(input,14,FOLLOW_2); 

            			newLeafNode(otherlv_2, grammarAccess.getNestedStatementAccess().getRightParenthesisKeyword_2());
            		

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
    // $ANTLR end "ruleNestedStatement"


    // $ANTLR start "entryRuleDataType"
    // InternalCQL.g:241:1: entryRuleDataType returns [EObject current=null] : iv_ruleDataType= ruleDataType EOF ;
    public final EObject entryRuleDataType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataType = null;


        try {
            // InternalCQL.g:241:49: (iv_ruleDataType= ruleDataType EOF )
            // InternalCQL.g:242:2: iv_ruleDataType= ruleDataType EOF
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
    // InternalCQL.g:248:1: ruleDataType returns [EObject current=null] : ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) ) ) ;
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
            // InternalCQL.g:254:2: ( ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) ) ) )
            // InternalCQL.g:255:2: ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) ) )
            {
            // InternalCQL.g:255:2: ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) ) )
            // InternalCQL.g:256:3: ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) )
            {
            // InternalCQL.g:256:3: ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) )
            // InternalCQL.g:257:4: (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' )
            {
            // InternalCQL.g:257:4: (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' )
            int alt4=7;
            switch ( input.LA(1) ) {
            case 15:
                {
                alt4=1;
                }
                break;
            case 16:
                {
                alt4=2;
                }
                break;
            case 17:
                {
                alt4=3;
                }
                break;
            case 18:
                {
                alt4=4;
                }
                break;
            case 19:
                {
                alt4=5;
                }
                break;
            case 20:
                {
                alt4=6;
                }
                break;
            case 21:
                {
                alt4=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // InternalCQL.g:258:5: lv_value_0_1= 'INTEGER'
                    {
                    lv_value_0_1=(Token)match(input,15,FOLLOW_2); 

                    					newLeafNode(lv_value_0_1, grammarAccess.getDataTypeAccess().getValueINTEGERKeyword_0_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_1, null);
                    				

                    }
                    break;
                case 2 :
                    // InternalCQL.g:269:5: lv_value_0_2= 'DOUBLE'
                    {
                    lv_value_0_2=(Token)match(input,16,FOLLOW_2); 

                    					newLeafNode(lv_value_0_2, grammarAccess.getDataTypeAccess().getValueDOUBLEKeyword_0_1());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_2, null);
                    				

                    }
                    break;
                case 3 :
                    // InternalCQL.g:280:5: lv_value_0_3= 'FLOAT'
                    {
                    lv_value_0_3=(Token)match(input,17,FOLLOW_2); 

                    					newLeafNode(lv_value_0_3, grammarAccess.getDataTypeAccess().getValueFLOATKeyword_0_2());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_3, null);
                    				

                    }
                    break;
                case 4 :
                    // InternalCQL.g:291:5: lv_value_0_4= 'STRING'
                    {
                    lv_value_0_4=(Token)match(input,18,FOLLOW_2); 

                    					newLeafNode(lv_value_0_4, grammarAccess.getDataTypeAccess().getValueSTRINGKeyword_0_3());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_4, null);
                    				

                    }
                    break;
                case 5 :
                    // InternalCQL.g:302:5: lv_value_0_5= 'BOOLEAN'
                    {
                    lv_value_0_5=(Token)match(input,19,FOLLOW_2); 

                    					newLeafNode(lv_value_0_5, grammarAccess.getDataTypeAccess().getValueBOOLEANKeyword_0_4());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_5, null);
                    				

                    }
                    break;
                case 6 :
                    // InternalCQL.g:313:5: lv_value_0_6= 'STARTTIMESTAMP'
                    {
                    lv_value_0_6=(Token)match(input,20,FOLLOW_2); 

                    					newLeafNode(lv_value_0_6, grammarAccess.getDataTypeAccess().getValueSTARTTIMESTAMPKeyword_0_5());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_6, null);
                    				

                    }
                    break;
                case 7 :
                    // InternalCQL.g:324:5: lv_value_0_7= 'ENDTIMESTAMP'
                    {
                    lv_value_0_7=(Token)match(input,21,FOLLOW_2); 

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


    // $ANTLR start "entryRuleSource"
    // InternalCQL.g:340:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalCQL.g:340:47: (iv_ruleSource= ruleSource EOF )
            // InternalCQL.g:341:2: iv_ruleSource= ruleSource EOF
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
    // InternalCQL.g:347:1: ruleSource returns [EObject current=null] : ( ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? ) | ( (lv_nested_6_0= ruleNestedStatement ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )? ) ;
    public final EObject ruleSource() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        AntlrDatatypeRuleToken lv_unbounded_2_0 = null;

        EObject lv_time_3_0 = null;

        EObject lv_tuple_4_0 = null;

        EObject lv_nested_6_0 = null;

        EObject lv_alias_8_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:353:2: ( ( ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? ) | ( (lv_nested_6_0= ruleNestedStatement ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:354:2: ( ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? ) | ( (lv_nested_6_0= ruleNestedStatement ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:354:2: ( ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? ) | ( (lv_nested_6_0= ruleNestedStatement ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )? )
            // InternalCQL.g:355:3: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? ) | ( (lv_nested_6_0= ruleNestedStatement ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:355:3: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? ) | ( (lv_nested_6_0= ruleNestedStatement ) ) )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==RULE_ID) ) {
                alt7=1;
            }
            else if ( (LA7_0==13) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // InternalCQL.g:356:4: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? )
                    {
                    // InternalCQL.g:356:4: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? )
                    // InternalCQL.g:357:5: ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )?
                    {
                    // InternalCQL.g:357:5: ( (lv_name_0_0= RULE_ID ) )
                    // InternalCQL.g:358:6: (lv_name_0_0= RULE_ID )
                    {
                    // InternalCQL.g:358:6: (lv_name_0_0= RULE_ID )
                    // InternalCQL.g:359:7: lv_name_0_0= RULE_ID
                    {
                    lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_7); 

                    							newLeafNode(lv_name_0_0, grammarAccess.getSourceAccess().getNameIDTerminalRuleCall_0_0_0_0());
                    						

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

                    // InternalCQL.g:375:5: (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==22) ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // InternalCQL.g:376:6: otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']'
                            {
                            otherlv_1=(Token)match(input,22,FOLLOW_8); 

                            						newLeafNode(otherlv_1, grammarAccess.getSourceAccess().getLeftSquareBracketKeyword_0_0_1_0());
                            					
                            // InternalCQL.g:380:6: ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) )
                            int alt5=3;
                            int LA5_0 = input.LA(1);

                            if ( (LA5_0==50) ) {
                                alt5=1;
                            }
                            else if ( (LA5_0==51) ) {
                                int LA5_2 = input.LA(2);

                                if ( (LA5_2==RULE_INT) ) {
                                    int LA5_3 = input.LA(3);

                                    if ( (LA5_3==RULE_ID) ) {
                                        alt5=2;
                                    }
                                    else if ( (LA5_3==52||LA5_3==54) ) {
                                        alt5=3;
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
                                    // InternalCQL.g:381:7: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    {
                                    // InternalCQL.g:381:7: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    // InternalCQL.g:382:8: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    {
                                    // InternalCQL.g:382:8: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    // InternalCQL.g:383:9: lv_unbounded_2_0= ruleWindow_Unbounded
                                    {

                                    									newCompositeNode(grammarAccess.getSourceAccess().getUnboundedWindow_UnboundedParserRuleCall_0_0_1_1_0_0());
                                    								
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
                                    // InternalCQL.g:401:7: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    {
                                    // InternalCQL.g:401:7: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    // InternalCQL.g:402:8: (lv_time_3_0= ruleWindow_Timebased )
                                    {
                                    // InternalCQL.g:402:8: (lv_time_3_0= ruleWindow_Timebased )
                                    // InternalCQL.g:403:9: lv_time_3_0= ruleWindow_Timebased
                                    {

                                    									newCompositeNode(grammarAccess.getSourceAccess().getTimeWindow_TimebasedParserRuleCall_0_0_1_1_1_0());
                                    								
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
                                    // InternalCQL.g:421:7: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    {
                                    // InternalCQL.g:421:7: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    // InternalCQL.g:422:8: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    {
                                    // InternalCQL.g:422:8: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    // InternalCQL.g:423:9: lv_tuple_4_0= ruleWindow_Tuplebased
                                    {

                                    									newCompositeNode(grammarAccess.getSourceAccess().getTupleWindow_TuplebasedParserRuleCall_0_0_1_1_2_0());
                                    								
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

                            otherlv_5=(Token)match(input,23,FOLLOW_10); 

                            						newLeafNode(otherlv_5, grammarAccess.getSourceAccess().getRightSquareBracketKeyword_0_0_1_2());
                            					

                            }
                            break;

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:448:4: ( (lv_nested_6_0= ruleNestedStatement ) )
                    {
                    // InternalCQL.g:448:4: ( (lv_nested_6_0= ruleNestedStatement ) )
                    // InternalCQL.g:449:5: (lv_nested_6_0= ruleNestedStatement )
                    {
                    // InternalCQL.g:449:5: (lv_nested_6_0= ruleNestedStatement )
                    // InternalCQL.g:450:6: lv_nested_6_0= ruleNestedStatement
                    {

                    						newCompositeNode(grammarAccess.getSourceAccess().getNestedNestedStatementParserRuleCall_0_1_0());
                    					
                    pushFollow(FOLLOW_10);
                    lv_nested_6_0=ruleNestedStatement();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSourceRule());
                    						}
                    						set(
                    							current,
                    							"nested",
                    							lv_nested_6_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.NestedStatement");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:468:3: (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==24) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // InternalCQL.g:469:4: otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) )
                    {
                    otherlv_7=(Token)match(input,24,FOLLOW_11); 

                    				newLeafNode(otherlv_7, grammarAccess.getSourceAccess().getASKeyword_1_0());
                    			
                    // InternalCQL.g:473:4: ( (lv_alias_8_0= ruleAlias ) )
                    // InternalCQL.g:474:5: (lv_alias_8_0= ruleAlias )
                    {
                    // InternalCQL.g:474:5: (lv_alias_8_0= ruleAlias )
                    // InternalCQL.g:475:6: lv_alias_8_0= ruleAlias
                    {

                    						newCompositeNode(grammarAccess.getSourceAccess().getAliasAliasParserRuleCall_1_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_alias_8_0=ruleAlias();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSourceRule());
                    						}
                    						set(
                    							current,
                    							"alias",
                    							lv_alias_8_0,
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


    // $ANTLR start "entryRuleAttribute"
    // InternalCQL.g:497:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCQL.g:497:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCQL.g:498:2: iv_ruleAttribute= ruleAttribute EOF
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
    // InternalCQL.g:504:1: ruleAttribute returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        EObject lv_alias_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:510:2: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:511:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:511:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? )
            // InternalCQL.g:512:3: ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:512:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:513:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:513:4: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:514:5: lv_name_0_0= RULE_ID
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

            // InternalCQL.g:530:3: (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==24) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // InternalCQL.g:531:4: otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) )
                    {
                    otherlv_1=(Token)match(input,24,FOLLOW_11); 

                    				newLeafNode(otherlv_1, grammarAccess.getAttributeAccess().getASKeyword_1_0());
                    			
                    // InternalCQL.g:535:4: ( (lv_alias_2_0= ruleAlias ) )
                    // InternalCQL.g:536:5: (lv_alias_2_0= ruleAlias )
                    {
                    // InternalCQL.g:536:5: (lv_alias_2_0= ruleAlias )
                    // InternalCQL.g:537:6: lv_alias_2_0= ruleAlias
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


    // $ANTLR start "entryRuleAttributeWithoutAlias"
    // InternalCQL.g:559:1: entryRuleAttributeWithoutAlias returns [EObject current=null] : iv_ruleAttributeWithoutAlias= ruleAttributeWithoutAlias EOF ;
    public final EObject entryRuleAttributeWithoutAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithoutAlias = null;


        try {
            // InternalCQL.g:559:62: (iv_ruleAttributeWithoutAlias= ruleAttributeWithoutAlias EOF )
            // InternalCQL.g:560:2: iv_ruleAttributeWithoutAlias= ruleAttributeWithoutAlias EOF
            {
             newCompositeNode(grammarAccess.getAttributeWithoutAliasRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAttributeWithoutAlias=ruleAttributeWithoutAlias();

            state._fsp--;

             current =iv_ruleAttributeWithoutAlias; 
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
    // $ANTLR end "entryRuleAttributeWithoutAlias"


    // $ANTLR start "ruleAttributeWithoutAlias"
    // InternalCQL.g:566:1: ruleAttributeWithoutAlias returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAttributeWithoutAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQL.g:572:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQL.g:573:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQL.g:573:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:574:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:574:3: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:575:4: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            				newLeafNode(lv_name_0_0, grammarAccess.getAttributeWithoutAliasAccess().getNameIDTerminalRuleCall_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getAttributeWithoutAliasRule());
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
    // $ANTLR end "ruleAttributeWithoutAlias"


    // $ANTLR start "entryRuleAggregation"
    // InternalCQL.g:594:1: entryRuleAggregation returns [EObject current=null] : iv_ruleAggregation= ruleAggregation EOF ;
    public final EObject entryRuleAggregation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregation = null;


        try {
            // InternalCQL.g:594:52: (iv_ruleAggregation= ruleAggregation EOF )
            // InternalCQL.g:595:2: iv_ruleAggregation= ruleAggregation EOF
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
    // InternalCQL.g:601:1: ruleAggregation returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? ) ;
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
            // InternalCQL.g:607:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:608:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:608:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? )
            // InternalCQL.g:609:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:609:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:610:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:610:4: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:611:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_12); 

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

            otherlv_1=(Token)match(input,13,FOLLOW_11); 

            			newLeafNode(otherlv_1, grammarAccess.getAggregationAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQL.g:631:3: ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) )
            // InternalCQL.g:632:4: (lv_attribute_2_0= ruleAttributeWithoutAlias )
            {
            // InternalCQL.g:632:4: (lv_attribute_2_0= ruleAttributeWithoutAlias )
            // InternalCQL.g:633:5: lv_attribute_2_0= ruleAttributeWithoutAlias
            {

            					newCompositeNode(grammarAccess.getAggregationAccess().getAttributeAttributeWithoutAliasParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_6);
            lv_attribute_2_0=ruleAttributeWithoutAlias();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAggregationRule());
            					}
            					set(
            						current,
            						"attribute",
            						lv_attribute_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAlias");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_3=(Token)match(input,14,FOLLOW_10); 

            			newLeafNode(otherlv_3, grammarAccess.getAggregationAccess().getRightParenthesisKeyword_3());
            		
            // InternalCQL.g:654:3: (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==24) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // InternalCQL.g:655:4: otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) )
                    {
                    otherlv_4=(Token)match(input,24,FOLLOW_11); 

                    				newLeafNode(otherlv_4, grammarAccess.getAggregationAccess().getASKeyword_4_0());
                    			
                    // InternalCQL.g:659:4: ( (lv_alias_5_0= ruleAlias ) )
                    // InternalCQL.g:660:5: (lv_alias_5_0= ruleAlias )
                    {
                    // InternalCQL.g:660:5: (lv_alias_5_0= ruleAlias )
                    // InternalCQL.g:661:6: lv_alias_5_0= ruleAlias
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
    // InternalCQL.g:683:1: entryRuleAlias returns [EObject current=null] : iv_ruleAlias= ruleAlias EOF ;
    public final EObject entryRuleAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAlias = null;


        try {
            // InternalCQL.g:683:46: (iv_ruleAlias= ruleAlias EOF )
            // InternalCQL.g:684:2: iv_ruleAlias= ruleAlias EOF
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
    // InternalCQL.g:690:1: ruleAlias returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQL.g:696:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQL.g:697:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQL.g:697:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:698:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:698:3: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:699:4: lv_name_0_0= RULE_ID
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


    // $ANTLR start "entryRuleSelect"
    // InternalCQL.g:718:1: entryRuleSelect returns [EObject current=null] : iv_ruleSelect= ruleSelect EOF ;
    public final EObject entryRuleSelect() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelect = null;


        try {
            // InternalCQL.g:718:47: (iv_ruleSelect= ruleSelect EOF )
            // InternalCQL.g:719:2: iv_ruleSelect= ruleSelect EOF
            {
             newCompositeNode(grammarAccess.getSelectRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelect=ruleSelect();

            state._fsp--;

             current =iv_ruleSelect; 
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
    // $ANTLR end "entryRuleSelect"


    // $ANTLR start "ruleSelect"
    // InternalCQL.g:725:1: ruleSelect returns [EObject current=null] : ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* ) (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )? (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )? (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )? ) ;
    public final EObject ruleSelect() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token lv_distinct_1_0=null;
        Token otherlv_2=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        Token otherlv_10=null;
        Token otherlv_12=null;
        Token otherlv_14=null;
        Token otherlv_16=null;
        Token otherlv_17=null;
        Token otherlv_19=null;
        Token otherlv_21=null;
        EObject lv_attributes_4_0 = null;

        EObject lv_aggregations_5_0 = null;

        EObject lv_attributes_7_0 = null;

        EObject lv_aggregations_9_0 = null;

        EObject lv_sources_11_0 = null;

        EObject lv_sources_13_0 = null;

        EObject lv_predicates_15_0 = null;

        EObject lv_order_18_0 = null;

        EObject lv_order_20_0 = null;

        EObject lv_having_22_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:731:2: ( ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* ) (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )? (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )? (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )? ) )
            // InternalCQL.g:732:2: ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* ) (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )? (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )? (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )? )
            {
            // InternalCQL.g:732:2: ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* ) (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )? (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )? (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )? )
            // InternalCQL.g:733:3: ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* ) (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )? (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )? (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )?
            {
            // InternalCQL.g:733:3: ( (lv_name_0_0= 'SELECT' ) )
            // InternalCQL.g:734:4: (lv_name_0_0= 'SELECT' )
            {
            // InternalCQL.g:734:4: (lv_name_0_0= 'SELECT' )
            // InternalCQL.g:735:5: lv_name_0_0= 'SELECT'
            {
            lv_name_0_0=(Token)match(input,25,FOLLOW_13); 

            					newLeafNode(lv_name_0_0, grammarAccess.getSelectAccess().getNameSELECTKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSelectRule());
            					}
            					setWithLastConsumed(current, "name", lv_name_0_0, "SELECT");
            				

            }


            }

            // InternalCQL.g:747:3: ( (lv_distinct_1_0= 'DISTINCT' ) )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==26) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // InternalCQL.g:748:4: (lv_distinct_1_0= 'DISTINCT' )
                    {
                    // InternalCQL.g:748:4: (lv_distinct_1_0= 'DISTINCT' )
                    // InternalCQL.g:749:5: lv_distinct_1_0= 'DISTINCT'
                    {
                    lv_distinct_1_0=(Token)match(input,26,FOLLOW_14); 

                    					newLeafNode(lv_distinct_1_0, grammarAccess.getSelectAccess().getDistinctDISTINCTKeyword_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getSelectRule());
                    					}
                    					setWithLastConsumed(current, "distinct", lv_distinct_1_0, "DISTINCT");
                    				

                    }


                    }
                    break;

            }

            // InternalCQL.g:761:3: (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==27) ) {
                alt17=1;
            }
            else if ( (LA17_0==RULE_ID||LA17_0==28) ) {
                alt17=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // InternalCQL.g:762:4: otherlv_2= '*'
                    {
                    otherlv_2=(Token)match(input,27,FOLLOW_15); 

                    				newLeafNode(otherlv_2, grammarAccess.getSelectAccess().getAsteriskKeyword_2_0());
                    			

                    }
                    break;
                case 2 :
                    // InternalCQL.g:767:4: ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) )
                    {
                    // InternalCQL.g:767:4: ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) )
                    // InternalCQL.g:768:5: ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) )
                    {
                    // InternalCQL.g:768:5: ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) )
                    // InternalCQL.g:769:6: ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?)
                    {
                     
                    					  getUnorderedGroupHelper().enter(grammarAccess.getSelectAccess().getUnorderedGroup_2_1());
                    					
                    // InternalCQL.g:772:6: ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?)
                    // InternalCQL.g:773:7: ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?
                    {
                    // InternalCQL.g:773:7: ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+
                    int cnt16=0;
                    loop16:
                    do {
                        int alt16=4;
                        int LA16_0 = input.LA(1);

                        if ( LA16_0 == RULE_ID && getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 0) ) {
                            alt16=1;
                        }
                        else if ( LA16_0 == 28 && ( getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 2) || getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 1) ) ) {
                            int LA16_3 = input.LA(2);

                            if ( LA16_3 == RULE_ID && ( getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 2) || getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 1) ) ) {
                                int LA16_4 = input.LA(3);

                                if ( ( LA16_4 == RULE_ID || LA16_4 == 24 || LA16_4 >= 28 && LA16_4 <= 29 ) && getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 1) ) {
                                    alt16=2;
                                }
                                else if ( LA16_4 == 13 && getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 2) ) {
                                    alt16=3;
                                }


                            }


                        }


                        switch (alt16) {
                    	case 1 :
                    	    // InternalCQL.g:774:5: ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) )
                    	    {
                    	    // InternalCQL.g:774:5: ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) )
                    	    // InternalCQL.g:775:6: {...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ )
                    	    {
                    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 0) ) {
                    	        throw new FailedPredicateException(input, "ruleSelect", "getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 0)");
                    	    }
                    	    // InternalCQL.g:775:106: ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ )
                    	    // InternalCQL.g:776:7: ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+
                    	    {

                    	    							getUnorderedGroupHelper().select(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 0);
                    	    						
                    	    // InternalCQL.g:779:10: ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+
                    	    int cnt13=0;
                    	    loop13:
                    	    do {
                    	        int alt13=2;
                    	        int LA13_0 = input.LA(1);

                    	        if ( (LA13_0==RULE_ID) ) {
                    	            int LA13_2 = input.LA(2);

                    	            if ( ((true)) ) {
                    	                alt13=1;
                    	            }


                    	        }


                    	        switch (alt13) {
                    	    	case 1 :
                    	    	    // InternalCQL.g:779:11: {...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) )
                    	    	    {
                    	    	    if ( !((true)) ) {
                    	    	        throw new FailedPredicateException(input, "ruleSelect", "true");
                    	    	    }
                    	    	    // InternalCQL.g:779:20: ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) )
                    	    	    int alt12=2;
                    	    	    int LA12_0 = input.LA(1);

                    	    	    if ( (LA12_0==RULE_ID) ) {
                    	    	        int LA12_1 = input.LA(2);

                    	    	        if ( (LA12_1==13) ) {
                    	    	            alt12=2;
                    	    	        }
                    	    	        else if ( (LA12_1==RULE_ID||LA12_1==24||(LA12_1>=28 && LA12_1<=29)) ) {
                    	    	            alt12=1;
                    	    	        }
                    	    	        else {
                    	    	            NoViableAltException nvae =
                    	    	                new NoViableAltException("", 12, 1, input);

                    	    	            throw nvae;
                    	    	        }
                    	    	    }
                    	    	    else {
                    	    	        NoViableAltException nvae =
                    	    	            new NoViableAltException("", 12, 0, input);

                    	    	        throw nvae;
                    	    	    }
                    	    	    switch (alt12) {
                    	    	        case 1 :
                    	    	            // InternalCQL.g:779:21: ( (lv_attributes_4_0= ruleAttribute ) )
                    	    	            {
                    	    	            // InternalCQL.g:779:21: ( (lv_attributes_4_0= ruleAttribute ) )
                    	    	            // InternalCQL.g:780:11: (lv_attributes_4_0= ruleAttribute )
                    	    	            {
                    	    	            // InternalCQL.g:780:11: (lv_attributes_4_0= ruleAttribute )
                    	    	            // InternalCQL.g:781:12: lv_attributes_4_0= ruleAttribute
                    	    	            {

                    	    	            												newCompositeNode(grammarAccess.getSelectAccess().getAttributesAttributeParserRuleCall_2_1_0_0_0());
                    	    	            											
                    	    	            pushFollow(FOLLOW_16);
                    	    	            lv_attributes_4_0=ruleAttribute();

                    	    	            state._fsp--;


                    	    	            												if (current==null) {
                    	    	            													current = createModelElementForParent(grammarAccess.getSelectRule());
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
                    	    	            // InternalCQL.g:799:10: ( (lv_aggregations_5_0= ruleAggregation ) )
                    	    	            {
                    	    	            // InternalCQL.g:799:10: ( (lv_aggregations_5_0= ruleAggregation ) )
                    	    	            // InternalCQL.g:800:11: (lv_aggregations_5_0= ruleAggregation )
                    	    	            {
                    	    	            // InternalCQL.g:800:11: (lv_aggregations_5_0= ruleAggregation )
                    	    	            // InternalCQL.g:801:12: lv_aggregations_5_0= ruleAggregation
                    	    	            {

                    	    	            												newCompositeNode(grammarAccess.getSelectAccess().getAggregationsAggregationParserRuleCall_2_1_0_1_0());
                    	    	            											
                    	    	            pushFollow(FOLLOW_16);
                    	    	            lv_aggregations_5_0=ruleAggregation();

                    	    	            state._fsp--;


                    	    	            												if (current==null) {
                    	    	            													current = createModelElementForParent(grammarAccess.getSelectRule());
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
                    	    	    if ( cnt13 >= 1 ) break loop13;
                    	                EarlyExitException eee =
                    	                    new EarlyExitException(13, input);
                    	                throw eee;
                    	        }
                    	        cnt13++;
                    	    } while (true);

                    	     
                    	    							getUnorderedGroupHelper().returnFromSelection(grammarAccess.getSelectAccess().getUnorderedGroup_2_1());
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalCQL.g:824:5: ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) )
                    	    {
                    	    // InternalCQL.g:824:5: ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) )
                    	    // InternalCQL.g:825:6: {...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ )
                    	    {
                    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 1) ) {
                    	        throw new FailedPredicateException(input, "ruleSelect", "getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 1)");
                    	    }
                    	    // InternalCQL.g:825:106: ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ )
                    	    // InternalCQL.g:826:7: ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+
                    	    {

                    	    							getUnorderedGroupHelper().select(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 1);
                    	    						
                    	    // InternalCQL.g:829:10: ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+
                    	    int cnt14=0;
                    	    loop14:
                    	    do {
                    	        int alt14=2;
                    	        int LA14_0 = input.LA(1);

                    	        if ( (LA14_0==28) ) {
                    	            int LA14_2 = input.LA(2);

                    	            if ( ((true)) ) {
                    	                alt14=1;
                    	            }


                    	        }


                    	        switch (alt14) {
                    	    	case 1 :
                    	    	    // InternalCQL.g:829:11: {...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) )
                    	    	    {
                    	    	    if ( !((true)) ) {
                    	    	        throw new FailedPredicateException(input, "ruleSelect", "true");
                    	    	    }
                    	    	    // InternalCQL.g:829:20: (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) )
                    	    	    // InternalCQL.g:829:21: otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) )
                    	    	    {
                    	    	    otherlv_6=(Token)match(input,28,FOLLOW_11); 

                    	    	    										newLeafNode(otherlv_6, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_0());
                    	    	    									
                    	    	    // InternalCQL.g:833:10: ( (lv_attributes_7_0= ruleAttribute ) )
                    	    	    // InternalCQL.g:834:11: (lv_attributes_7_0= ruleAttribute )
                    	    	    {
                    	    	    // InternalCQL.g:834:11: (lv_attributes_7_0= ruleAttribute )
                    	    	    // InternalCQL.g:835:12: lv_attributes_7_0= ruleAttribute
                    	    	    {

                    	    	    												newCompositeNode(grammarAccess.getSelectAccess().getAttributesAttributeParserRuleCall_2_1_1_1_0());
                    	    	    											
                    	    	    pushFollow(FOLLOW_16);
                    	    	    lv_attributes_7_0=ruleAttribute();

                    	    	    state._fsp--;


                    	    	    												if (current==null) {
                    	    	    													current = createModelElementForParent(grammarAccess.getSelectRule());
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
                    	    	    if ( cnt14 >= 1 ) break loop14;
                    	                EarlyExitException eee =
                    	                    new EarlyExitException(14, input);
                    	                throw eee;
                    	        }
                    	        cnt14++;
                    	    } while (true);

                    	     
                    	    							getUnorderedGroupHelper().returnFromSelection(grammarAccess.getSelectAccess().getUnorderedGroup_2_1());
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;
                    	case 3 :
                    	    // InternalCQL.g:858:5: ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) )
                    	    {
                    	    // InternalCQL.g:858:5: ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) )
                    	    // InternalCQL.g:859:6: {...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ )
                    	    {
                    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 2) ) {
                    	        throw new FailedPredicateException(input, "ruleSelect", "getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 2)");
                    	    }
                    	    // InternalCQL.g:859:106: ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ )
                    	    // InternalCQL.g:860:7: ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+
                    	    {

                    	    							getUnorderedGroupHelper().select(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 2);
                    	    						
                    	    // InternalCQL.g:863:10: ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+
                    	    int cnt15=0;
                    	    loop15:
                    	    do {
                    	        int alt15=2;
                    	        int LA15_0 = input.LA(1);

                    	        if ( (LA15_0==28) ) {
                    	            int LA15_2 = input.LA(2);

                    	            if ( ((true)) ) {
                    	                alt15=1;
                    	            }


                    	        }


                    	        switch (alt15) {
                    	    	case 1 :
                    	    	    // InternalCQL.g:863:11: {...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) )
                    	    	    {
                    	    	    if ( !((true)) ) {
                    	    	        throw new FailedPredicateException(input, "ruleSelect", "true");
                    	    	    }
                    	    	    // InternalCQL.g:863:20: (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) )
                    	    	    // InternalCQL.g:863:21: otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) )
                    	    	    {
                    	    	    otherlv_8=(Token)match(input,28,FOLLOW_11); 

                    	    	    										newLeafNode(otherlv_8, grammarAccess.getSelectAccess().getCommaKeyword_2_1_2_0());
                    	    	    									
                    	    	    // InternalCQL.g:867:10: ( (lv_aggregations_9_0= ruleAggregation ) )
                    	    	    // InternalCQL.g:868:11: (lv_aggregations_9_0= ruleAggregation )
                    	    	    {
                    	    	    // InternalCQL.g:868:11: (lv_aggregations_9_0= ruleAggregation )
                    	    	    // InternalCQL.g:869:12: lv_aggregations_9_0= ruleAggregation
                    	    	    {

                    	    	    												newCompositeNode(grammarAccess.getSelectAccess().getAggregationsAggregationParserRuleCall_2_1_2_1_0());
                    	    	    											
                    	    	    pushFollow(FOLLOW_16);
                    	    	    lv_aggregations_9_0=ruleAggregation();

                    	    	    state._fsp--;


                    	    	    												if (current==null) {
                    	    	    													current = createModelElementForParent(grammarAccess.getSelectRule());
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
                    	    	    if ( cnt15 >= 1 ) break loop15;
                    	                EarlyExitException eee =
                    	                    new EarlyExitException(15, input);
                    	                throw eee;
                    	        }
                    	        cnt15++;
                    	    } while (true);

                    	     
                    	    							getUnorderedGroupHelper().returnFromSelection(grammarAccess.getSelectAccess().getUnorderedGroup_2_1());
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt16 >= 1 ) break loop16;
                                EarlyExitException eee =
                                    new EarlyExitException(16, input);
                                throw eee;
                        }
                        cnt16++;
                    } while (true);

                    if ( ! getUnorderedGroupHelper().canLeave(grammarAccess.getSelectAccess().getUnorderedGroup_2_1()) ) {
                        throw new FailedPredicateException(input, "ruleSelect", "getUnorderedGroupHelper().canLeave(grammarAccess.getSelectAccess().getUnorderedGroup_2_1())");
                    }

                    }


                    }

                     
                    					  getUnorderedGroupHelper().leave(grammarAccess.getSelectAccess().getUnorderedGroup_2_1());
                    					

                    }


                    }
                    break;

            }

            // InternalCQL.g:901:3: (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* )
            // InternalCQL.g:902:4: otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )*
            {
            otherlv_10=(Token)match(input,29,FOLLOW_17); 

            				newLeafNode(otherlv_10, grammarAccess.getSelectAccess().getFROMKeyword_3_0());
            			
            // InternalCQL.g:906:4: ( (lv_sources_11_0= ruleSource ) )+
            int cnt18=0;
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==RULE_ID||LA18_0==13) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // InternalCQL.g:907:5: (lv_sources_11_0= ruleSource )
            	    {
            	    // InternalCQL.g:907:5: (lv_sources_11_0= ruleSource )
            	    // InternalCQL.g:908:6: lv_sources_11_0= ruleSource
            	    {

            	    						newCompositeNode(grammarAccess.getSelectAccess().getSourcesSourceParserRuleCall_3_1_0());
            	    					
            	    pushFollow(FOLLOW_18);
            	    lv_sources_11_0=ruleSource();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getSelectRule());
            	    						}
            	    						add(
            	    							current,
            	    							"sources",
            	    							lv_sources_11_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Source");
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

            // InternalCQL.g:925:4: (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==28) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // InternalCQL.g:926:5: otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) )
            	    {
            	    otherlv_12=(Token)match(input,28,FOLLOW_17); 

            	    					newLeafNode(otherlv_12, grammarAccess.getSelectAccess().getCommaKeyword_3_2_0());
            	    				
            	    // InternalCQL.g:930:5: ( (lv_sources_13_0= ruleSource ) )
            	    // InternalCQL.g:931:6: (lv_sources_13_0= ruleSource )
            	    {
            	    // InternalCQL.g:931:6: (lv_sources_13_0= ruleSource )
            	    // InternalCQL.g:932:7: lv_sources_13_0= ruleSource
            	    {

            	    							newCompositeNode(grammarAccess.getSelectAccess().getSourcesSourceParserRuleCall_3_2_1_0());
            	    						
            	    pushFollow(FOLLOW_19);
            	    lv_sources_13_0=ruleSource();

            	    state._fsp--;


            	    							if (current==null) {
            	    								current = createModelElementForParent(grammarAccess.getSelectRule());
            	    							}
            	    							add(
            	    								current,
            	    								"sources",
            	    								lv_sources_13_0,
            	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Source");
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

            // InternalCQL.g:951:3: (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==30) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // InternalCQL.g:952:4: otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) )
                    {
                    otherlv_14=(Token)match(input,30,FOLLOW_20); 

                    				newLeafNode(otherlv_14, grammarAccess.getSelectAccess().getWHEREKeyword_4_0());
                    			
                    // InternalCQL.g:956:4: ( (lv_predicates_15_0= ruleExpressionsModel ) )
                    // InternalCQL.g:957:5: (lv_predicates_15_0= ruleExpressionsModel )
                    {
                    // InternalCQL.g:957:5: (lv_predicates_15_0= ruleExpressionsModel )
                    // InternalCQL.g:958:6: lv_predicates_15_0= ruleExpressionsModel
                    {

                    						newCompositeNode(grammarAccess.getSelectAccess().getPredicatesExpressionsModelParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_21);
                    lv_predicates_15_0=ruleExpressionsModel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    						}
                    						set(
                    							current,
                    							"predicates",
                    							lv_predicates_15_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionsModel");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:976:3: (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==31) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // InternalCQL.g:977:4: otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )*
                    {
                    otherlv_16=(Token)match(input,31,FOLLOW_22); 

                    				newLeafNode(otherlv_16, grammarAccess.getSelectAccess().getGROUPKeyword_5_0());
                    			
                    otherlv_17=(Token)match(input,32,FOLLOW_11); 

                    				newLeafNode(otherlv_17, grammarAccess.getSelectAccess().getBYKeyword_5_1());
                    			
                    // InternalCQL.g:985:4: ( (lv_order_18_0= ruleAttribute ) )+
                    int cnt21=0;
                    loop21:
                    do {
                        int alt21=2;
                        int LA21_0 = input.LA(1);

                        if ( (LA21_0==RULE_ID) ) {
                            alt21=1;
                        }


                        switch (alt21) {
                    	case 1 :
                    	    // InternalCQL.g:986:5: (lv_order_18_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:986:5: (lv_order_18_0= ruleAttribute )
                    	    // InternalCQL.g:987:6: lv_order_18_0= ruleAttribute
                    	    {

                    	    						newCompositeNode(grammarAccess.getSelectAccess().getOrderAttributeParserRuleCall_5_2_0());
                    	    					
                    	    pushFollow(FOLLOW_23);
                    	    lv_order_18_0=ruleAttribute();

                    	    state._fsp--;


                    	    						if (current==null) {
                    	    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    						}
                    	    						add(
                    	    							current,
                    	    							"order",
                    	    							lv_order_18_0,
                    	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    	    						afterParserOrEnumRuleCall();
                    	    					

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt21 >= 1 ) break loop21;
                                EarlyExitException eee =
                                    new EarlyExitException(21, input);
                                throw eee;
                        }
                        cnt21++;
                    } while (true);

                    // InternalCQL.g:1004:4: (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )*
                    loop22:
                    do {
                        int alt22=2;
                        int LA22_0 = input.LA(1);

                        if ( (LA22_0==28) ) {
                            alt22=1;
                        }


                        switch (alt22) {
                    	case 1 :
                    	    // InternalCQL.g:1005:5: otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) )
                    	    {
                    	    otherlv_19=(Token)match(input,28,FOLLOW_11); 

                    	    					newLeafNode(otherlv_19, grammarAccess.getSelectAccess().getCommaKeyword_5_3_0());
                    	    				
                    	    // InternalCQL.g:1009:5: ( (lv_order_20_0= ruleAttribute ) )
                    	    // InternalCQL.g:1010:6: (lv_order_20_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:1010:6: (lv_order_20_0= ruleAttribute )
                    	    // InternalCQL.g:1011:7: lv_order_20_0= ruleAttribute
                    	    {

                    	    							newCompositeNode(grammarAccess.getSelectAccess().getOrderAttributeParserRuleCall_5_3_1_0());
                    	    						
                    	    pushFollow(FOLLOW_24);
                    	    lv_order_20_0=ruleAttribute();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"order",
                    	    								lv_order_20_0,
                    	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop22;
                        }
                    } while (true);


                    }
                    break;

            }

            // InternalCQL.g:1030:3: (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==33) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // InternalCQL.g:1031:4: otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) )
                    {
                    otherlv_21=(Token)match(input,33,FOLLOW_20); 

                    				newLeafNode(otherlv_21, grammarAccess.getSelectAccess().getHAVINGKeyword_6_0());
                    			
                    // InternalCQL.g:1035:4: ( (lv_having_22_0= ruleExpressionsModel ) )
                    // InternalCQL.g:1036:5: (lv_having_22_0= ruleExpressionsModel )
                    {
                    // InternalCQL.g:1036:5: (lv_having_22_0= ruleExpressionsModel )
                    // InternalCQL.g:1037:6: lv_having_22_0= ruleExpressionsModel
                    {

                    						newCompositeNode(grammarAccess.getSelectAccess().getHavingExpressionsModelParserRuleCall_6_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_having_22_0=ruleExpressionsModel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    						}
                    						set(
                    							current,
                    							"having",
                    							lv_having_22_0,
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
    // $ANTLR end "ruleSelect"


    // $ANTLR start "entryRuleCreate"
    // InternalCQL.g:1059:1: entryRuleCreate returns [EObject current=null] : iv_ruleCreate= ruleCreate EOF ;
    public final EObject entryRuleCreate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreate = null;


        try {
            // InternalCQL.g:1059:47: (iv_ruleCreate= ruleCreate EOF )
            // InternalCQL.g:1060:2: iv_ruleCreate= ruleCreate EOF
            {
             newCompositeNode(grammarAccess.getCreateRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreate=ruleCreate();

            state._fsp--;

             current =iv_ruleCreate; 
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
    // $ANTLR end "entryRuleCreate"


    // $ANTLR start "ruleCreate"
    // InternalCQL.g:1066:1: ruleCreate returns [EObject current=null] : ( ( ( (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' ) ) ) ( ( (lv_channelformat_1_0= ruleChannelFormat ) ) | ( (lv_accessframework_2_0= ruleAccessFramework ) ) ) ) ;
    public final EObject ruleCreate() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_1=null;
        Token lv_name_0_2=null;
        EObject lv_channelformat_1_0 = null;

        EObject lv_accessframework_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1072:2: ( ( ( ( (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' ) ) ) ( ( (lv_channelformat_1_0= ruleChannelFormat ) ) | ( (lv_accessframework_2_0= ruleAccessFramework ) ) ) ) )
            // InternalCQL.g:1073:2: ( ( ( (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' ) ) ) ( ( (lv_channelformat_1_0= ruleChannelFormat ) ) | ( (lv_accessframework_2_0= ruleAccessFramework ) ) ) )
            {
            // InternalCQL.g:1073:2: ( ( ( (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' ) ) ) ( ( (lv_channelformat_1_0= ruleChannelFormat ) ) | ( (lv_accessframework_2_0= ruleAccessFramework ) ) ) )
            // InternalCQL.g:1074:3: ( ( (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' ) ) ) ( ( (lv_channelformat_1_0= ruleChannelFormat ) ) | ( (lv_accessframework_2_0= ruleAccessFramework ) ) )
            {
            // InternalCQL.g:1074:3: ( ( (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' ) ) )
            // InternalCQL.g:1075:4: ( (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' ) )
            {
            // InternalCQL.g:1075:4: ( (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' ) )
            // InternalCQL.g:1076:5: (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' )
            {
            // InternalCQL.g:1076:5: (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' )
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==34) ) {
                alt25=1;
            }
            else if ( (LA25_0==35) ) {
                alt25=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;
            }
            switch (alt25) {
                case 1 :
                    // InternalCQL.g:1077:6: lv_name_0_1= 'CREATE'
                    {
                    lv_name_0_1=(Token)match(input,34,FOLLOW_25); 

                    						newLeafNode(lv_name_0_1, grammarAccess.getCreateAccess().getNameCREATEKeyword_0_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:1088:6: lv_name_0_2= 'ATTACH'
                    {
                    lv_name_0_2=(Token)match(input,35,FOLLOW_25); 

                    						newLeafNode(lv_name_0_2, grammarAccess.getCreateAccess().getNameATTACHKeyword_0_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_2, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQL.g:1101:3: ( ( (lv_channelformat_1_0= ruleChannelFormat ) ) | ( (lv_accessframework_2_0= ruleAccessFramework ) ) )
            int alt26=2;
            alt26 = dfa26.predict(input);
            switch (alt26) {
                case 1 :
                    // InternalCQL.g:1102:4: ( (lv_channelformat_1_0= ruleChannelFormat ) )
                    {
                    // InternalCQL.g:1102:4: ( (lv_channelformat_1_0= ruleChannelFormat ) )
                    // InternalCQL.g:1103:5: (lv_channelformat_1_0= ruleChannelFormat )
                    {
                    // InternalCQL.g:1103:5: (lv_channelformat_1_0= ruleChannelFormat )
                    // InternalCQL.g:1104:6: lv_channelformat_1_0= ruleChannelFormat
                    {

                    						newCompositeNode(grammarAccess.getCreateAccess().getChannelformatChannelFormatParserRuleCall_1_0_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_channelformat_1_0=ruleChannelFormat();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCreateRule());
                    						}
                    						set(
                    							current,
                    							"channelformat",
                    							lv_channelformat_1_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ChannelFormat");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1122:4: ( (lv_accessframework_2_0= ruleAccessFramework ) )
                    {
                    // InternalCQL.g:1122:4: ( (lv_accessframework_2_0= ruleAccessFramework ) )
                    // InternalCQL.g:1123:5: (lv_accessframework_2_0= ruleAccessFramework )
                    {
                    // InternalCQL.g:1123:5: (lv_accessframework_2_0= ruleAccessFramework )
                    // InternalCQL.g:1124:6: lv_accessframework_2_0= ruleAccessFramework
                    {

                    						newCompositeNode(grammarAccess.getCreateAccess().getAccessframeworkAccessFrameworkParserRuleCall_1_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_accessframework_2_0=ruleAccessFramework();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCreateRule());
                    						}
                    						set(
                    							current,
                    							"accessframework",
                    							lv_accessframework_2_0,
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
    // $ANTLR end "ruleCreate"


    // $ANTLR start "entryRuleAccessFramework"
    // InternalCQL.g:1146:1: entryRuleAccessFramework returns [EObject current=null] : iv_ruleAccessFramework= ruleAccessFramework EOF ;
    public final EObject entryRuleAccessFramework() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAccessFramework = null;


        try {
            // InternalCQL.g:1146:56: (iv_ruleAccessFramework= ruleAccessFramework EOF )
            // InternalCQL.g:1147:2: iv_ruleAccessFramework= ruleAccessFramework EOF
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
    // InternalCQL.g:1153:1: ruleAccessFramework returns [EObject current=null] : ( ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) ) ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'WRAPPER' ( (lv_wrapper_10_0= RULE_STRING ) ) otherlv_11= 'PROTOCOL' ( (lv_protocol_12_0= RULE_STRING ) ) otherlv_13= 'TRANSPORT' ( (lv_transport_14_0= RULE_STRING ) ) otherlv_15= 'DATAHANDLER' ( (lv_datahandler_16_0= RULE_STRING ) ) otherlv_17= 'OPTIONS' otherlv_18= '(' ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+ (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )? otherlv_24= ')' ) ;
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
            // InternalCQL.g:1159:2: ( ( ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) ) ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'WRAPPER' ( (lv_wrapper_10_0= RULE_STRING ) ) otherlv_11= 'PROTOCOL' ( (lv_protocol_12_0= RULE_STRING ) ) otherlv_13= 'TRANSPORT' ( (lv_transport_14_0= RULE_STRING ) ) otherlv_15= 'DATAHANDLER' ( (lv_datahandler_16_0= RULE_STRING ) ) otherlv_17= 'OPTIONS' otherlv_18= '(' ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+ (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )? otherlv_24= ')' ) )
            // InternalCQL.g:1160:2: ( ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) ) ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'WRAPPER' ( (lv_wrapper_10_0= RULE_STRING ) ) otherlv_11= 'PROTOCOL' ( (lv_protocol_12_0= RULE_STRING ) ) otherlv_13= 'TRANSPORT' ( (lv_transport_14_0= RULE_STRING ) ) otherlv_15= 'DATAHANDLER' ( (lv_datahandler_16_0= RULE_STRING ) ) otherlv_17= 'OPTIONS' otherlv_18= '(' ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+ (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )? otherlv_24= ')' )
            {
            // InternalCQL.g:1160:2: ( ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) ) ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'WRAPPER' ( (lv_wrapper_10_0= RULE_STRING ) ) otherlv_11= 'PROTOCOL' ( (lv_protocol_12_0= RULE_STRING ) ) otherlv_13= 'TRANSPORT' ( (lv_transport_14_0= RULE_STRING ) ) otherlv_15= 'DATAHANDLER' ( (lv_datahandler_16_0= RULE_STRING ) ) otherlv_17= 'OPTIONS' otherlv_18= '(' ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+ (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )? otherlv_24= ')' )
            // InternalCQL.g:1161:3: ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) ) ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'WRAPPER' ( (lv_wrapper_10_0= RULE_STRING ) ) otherlv_11= 'PROTOCOL' ( (lv_protocol_12_0= RULE_STRING ) ) otherlv_13= 'TRANSPORT' ( (lv_transport_14_0= RULE_STRING ) ) otherlv_15= 'DATAHANDLER' ( (lv_datahandler_16_0= RULE_STRING ) ) otherlv_17= 'OPTIONS' otherlv_18= '(' ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+ (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )? otherlv_24= ')'
            {
            // InternalCQL.g:1161:3: ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) )
            // InternalCQL.g:1162:4: ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) )
            {
            // InternalCQL.g:1162:4: ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) )
            // InternalCQL.g:1163:5: (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' )
            {
            // InternalCQL.g:1163:5: (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' )
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==36) ) {
                alt27=1;
            }
            else if ( (LA27_0==37) ) {
                alt27=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }
            switch (alt27) {
                case 1 :
                    // InternalCQL.g:1164:6: lv_type_0_1= 'STREAM'
                    {
                    lv_type_0_1=(Token)match(input,36,FOLLOW_11); 

                    						newLeafNode(lv_type_0_1, grammarAccess.getAccessFrameworkAccess().getTypeSTREAMKeyword_0_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
                    						}
                    						setWithLastConsumed(current, "type", lv_type_0_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:1175:6: lv_type_0_2= 'SINK'
                    {
                    lv_type_0_2=(Token)match(input,37,FOLLOW_11); 

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

            // InternalCQL.g:1188:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQL.g:1189:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQL.g:1189:4: (lv_name_1_0= RULE_ID )
            // InternalCQL.g:1190:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_12); 

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

            otherlv_2=(Token)match(input,13,FOLLOW_11); 

            			newLeafNode(otherlv_2, grammarAccess.getAccessFrameworkAccess().getLeftParenthesisKeyword_2());
            		
            // InternalCQL.g:1210:3: ( (lv_attributes_3_0= ruleAttribute ) )+
            int cnt28=0;
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( (LA28_0==RULE_ID) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // InternalCQL.g:1211:4: (lv_attributes_3_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:1211:4: (lv_attributes_3_0= ruleAttribute )
            	    // InternalCQL.g:1212:5: lv_attributes_3_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getAccessFrameworkAccess().getAttributesAttributeParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_26);
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
            	    if ( cnt28 >= 1 ) break loop28;
                        EarlyExitException eee =
                            new EarlyExitException(28, input);
                        throw eee;
                }
                cnt28++;
            } while (true);

            // InternalCQL.g:1229:3: ( (lv_datatypes_4_0= ruleDataType ) )+
            int cnt29=0;
            loop29:
            do {
                int alt29=2;
                int LA29_0 = input.LA(1);

                if ( ((LA29_0>=15 && LA29_0<=21)) ) {
                    alt29=1;
                }


                switch (alt29) {
            	case 1 :
            	    // InternalCQL.g:1230:4: (lv_datatypes_4_0= ruleDataType )
            	    {
            	    // InternalCQL.g:1230:4: (lv_datatypes_4_0= ruleDataType )
            	    // InternalCQL.g:1231:5: lv_datatypes_4_0= ruleDataType
            	    {

            	    					newCompositeNode(grammarAccess.getAccessFrameworkAccess().getDatatypesDataTypeParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_27);
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
            	    if ( cnt29 >= 1 ) break loop29;
                        EarlyExitException eee =
                            new EarlyExitException(29, input);
                        throw eee;
                }
                cnt29++;
            } while (true);

            // InternalCQL.g:1248:3: (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )*
            loop30:
            do {
                int alt30=2;
                int LA30_0 = input.LA(1);

                if ( (LA30_0==28) ) {
                    alt30=1;
                }


                switch (alt30) {
            	case 1 :
            	    // InternalCQL.g:1249:4: otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) )
            	    {
            	    otherlv_5=(Token)match(input,28,FOLLOW_11); 

            	    				newLeafNode(otherlv_5, grammarAccess.getAccessFrameworkAccess().getCommaKeyword_5_0());
            	    			
            	    // InternalCQL.g:1253:4: ( (lv_attributes_6_0= ruleAttribute ) )
            	    // InternalCQL.g:1254:5: (lv_attributes_6_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:1254:5: (lv_attributes_6_0= ruleAttribute )
            	    // InternalCQL.g:1255:6: lv_attributes_6_0= ruleAttribute
            	    {

            	    						newCompositeNode(grammarAccess.getAccessFrameworkAccess().getAttributesAttributeParserRuleCall_5_1_0());
            	    					
            	    pushFollow(FOLLOW_28);
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

            	    // InternalCQL.g:1272:4: ( (lv_datatypes_7_0= ruleDataType ) )
            	    // InternalCQL.g:1273:5: (lv_datatypes_7_0= ruleDataType )
            	    {
            	    // InternalCQL.g:1273:5: (lv_datatypes_7_0= ruleDataType )
            	    // InternalCQL.g:1274:6: lv_datatypes_7_0= ruleDataType
            	    {

            	    						newCompositeNode(grammarAccess.getAccessFrameworkAccess().getDatatypesDataTypeParserRuleCall_5_2_0());
            	    					
            	    pushFollow(FOLLOW_29);
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
            	    break loop30;
                }
            } while (true);

            otherlv_8=(Token)match(input,14,FOLLOW_30); 

            			newLeafNode(otherlv_8, grammarAccess.getAccessFrameworkAccess().getRightParenthesisKeyword_6());
            		
            otherlv_9=(Token)match(input,38,FOLLOW_31); 

            			newLeafNode(otherlv_9, grammarAccess.getAccessFrameworkAccess().getWRAPPERKeyword_7());
            		
            // InternalCQL.g:1300:3: ( (lv_wrapper_10_0= RULE_STRING ) )
            // InternalCQL.g:1301:4: (lv_wrapper_10_0= RULE_STRING )
            {
            // InternalCQL.g:1301:4: (lv_wrapper_10_0= RULE_STRING )
            // InternalCQL.g:1302:5: lv_wrapper_10_0= RULE_STRING
            {
            lv_wrapper_10_0=(Token)match(input,RULE_STRING,FOLLOW_32); 

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

            otherlv_11=(Token)match(input,39,FOLLOW_31); 

            			newLeafNode(otherlv_11, grammarAccess.getAccessFrameworkAccess().getPROTOCOLKeyword_9());
            		
            // InternalCQL.g:1322:3: ( (lv_protocol_12_0= RULE_STRING ) )
            // InternalCQL.g:1323:4: (lv_protocol_12_0= RULE_STRING )
            {
            // InternalCQL.g:1323:4: (lv_protocol_12_0= RULE_STRING )
            // InternalCQL.g:1324:5: lv_protocol_12_0= RULE_STRING
            {
            lv_protocol_12_0=(Token)match(input,RULE_STRING,FOLLOW_33); 

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

            otherlv_13=(Token)match(input,40,FOLLOW_31); 

            			newLeafNode(otherlv_13, grammarAccess.getAccessFrameworkAccess().getTRANSPORTKeyword_11());
            		
            // InternalCQL.g:1344:3: ( (lv_transport_14_0= RULE_STRING ) )
            // InternalCQL.g:1345:4: (lv_transport_14_0= RULE_STRING )
            {
            // InternalCQL.g:1345:4: (lv_transport_14_0= RULE_STRING )
            // InternalCQL.g:1346:5: lv_transport_14_0= RULE_STRING
            {
            lv_transport_14_0=(Token)match(input,RULE_STRING,FOLLOW_34); 

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

            otherlv_15=(Token)match(input,41,FOLLOW_31); 

            			newLeafNode(otherlv_15, grammarAccess.getAccessFrameworkAccess().getDATAHANDLERKeyword_13());
            		
            // InternalCQL.g:1366:3: ( (lv_datahandler_16_0= RULE_STRING ) )
            // InternalCQL.g:1367:4: (lv_datahandler_16_0= RULE_STRING )
            {
            // InternalCQL.g:1367:4: (lv_datahandler_16_0= RULE_STRING )
            // InternalCQL.g:1368:5: lv_datahandler_16_0= RULE_STRING
            {
            lv_datahandler_16_0=(Token)match(input,RULE_STRING,FOLLOW_35); 

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

            otherlv_17=(Token)match(input,42,FOLLOW_12); 

            			newLeafNode(otherlv_17, grammarAccess.getAccessFrameworkAccess().getOPTIONSKeyword_15());
            		
            otherlv_18=(Token)match(input,13,FOLLOW_31); 

            			newLeafNode(otherlv_18, grammarAccess.getAccessFrameworkAccess().getLeftParenthesisKeyword_16());
            		
            // InternalCQL.g:1392:3: ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+
            int cnt31=0;
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( (LA31_0==RULE_STRING) ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // InternalCQL.g:1393:4: ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) )
            	    {
            	    // InternalCQL.g:1393:4: ( (lv_keys_19_0= RULE_STRING ) )
            	    // InternalCQL.g:1394:5: (lv_keys_19_0= RULE_STRING )
            	    {
            	    // InternalCQL.g:1394:5: (lv_keys_19_0= RULE_STRING )
            	    // InternalCQL.g:1395:6: lv_keys_19_0= RULE_STRING
            	    {
            	    lv_keys_19_0=(Token)match(input,RULE_STRING,FOLLOW_31); 

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

            	    // InternalCQL.g:1411:4: ( (lv_values_20_0= RULE_STRING ) )
            	    // InternalCQL.g:1412:5: (lv_values_20_0= RULE_STRING )
            	    {
            	    // InternalCQL.g:1412:5: (lv_values_20_0= RULE_STRING )
            	    // InternalCQL.g:1413:6: lv_values_20_0= RULE_STRING
            	    {
            	    lv_values_20_0=(Token)match(input,RULE_STRING,FOLLOW_36); 

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
            	    if ( cnt31 >= 1 ) break loop31;
                        EarlyExitException eee =
                            new EarlyExitException(31, input);
                        throw eee;
                }
                cnt31++;
            } while (true);

            // InternalCQL.g:1430:3: (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==28) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // InternalCQL.g:1431:4: otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) )
                    {
                    otherlv_21=(Token)match(input,28,FOLLOW_31); 

                    				newLeafNode(otherlv_21, grammarAccess.getAccessFrameworkAccess().getCommaKeyword_18_0());
                    			
                    // InternalCQL.g:1435:4: ( (lv_keys_22_0= RULE_STRING ) )
                    // InternalCQL.g:1436:5: (lv_keys_22_0= RULE_STRING )
                    {
                    // InternalCQL.g:1436:5: (lv_keys_22_0= RULE_STRING )
                    // InternalCQL.g:1437:6: lv_keys_22_0= RULE_STRING
                    {
                    lv_keys_22_0=(Token)match(input,RULE_STRING,FOLLOW_31); 

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

                    // InternalCQL.g:1453:4: ( (lv_values_23_0= RULE_STRING ) )
                    // InternalCQL.g:1454:5: (lv_values_23_0= RULE_STRING )
                    {
                    // InternalCQL.g:1454:5: (lv_values_23_0= RULE_STRING )
                    // InternalCQL.g:1455:6: lv_values_23_0= RULE_STRING
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
    // InternalCQL.g:1480:1: entryRuleChannelFormat returns [EObject current=null] : iv_ruleChannelFormat= ruleChannelFormat EOF ;
    public final EObject entryRuleChannelFormat() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleChannelFormat = null;


        try {
            // InternalCQL.g:1480:54: (iv_ruleChannelFormat= ruleChannelFormat EOF )
            // InternalCQL.g:1481:2: iv_ruleChannelFormat= ruleChannelFormat EOF
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
    // InternalCQL.g:1487:1: ruleChannelFormat returns [EObject current=null] : ( ( (lv_stream_0_0= ruleChannelFormatStream ) ) | ( (lv_view_1_0= ruleChannelFormatView ) ) ) ;
    public final EObject ruleChannelFormat() throws RecognitionException {
        EObject current = null;

        EObject lv_stream_0_0 = null;

        EObject lv_view_1_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1493:2: ( ( ( (lv_stream_0_0= ruleChannelFormatStream ) ) | ( (lv_view_1_0= ruleChannelFormatView ) ) ) )
            // InternalCQL.g:1494:2: ( ( (lv_stream_0_0= ruleChannelFormatStream ) ) | ( (lv_view_1_0= ruleChannelFormatView ) ) )
            {
            // InternalCQL.g:1494:2: ( ( (lv_stream_0_0= ruleChannelFormatStream ) ) | ( (lv_view_1_0= ruleChannelFormatView ) ) )
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==36) ) {
                alt33=1;
            }
            else if ( (LA33_0==45) ) {
                alt33=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 33, 0, input);

                throw nvae;
            }
            switch (alt33) {
                case 1 :
                    // InternalCQL.g:1495:3: ( (lv_stream_0_0= ruleChannelFormatStream ) )
                    {
                    // InternalCQL.g:1495:3: ( (lv_stream_0_0= ruleChannelFormatStream ) )
                    // InternalCQL.g:1496:4: (lv_stream_0_0= ruleChannelFormatStream )
                    {
                    // InternalCQL.g:1496:4: (lv_stream_0_0= ruleChannelFormatStream )
                    // InternalCQL.g:1497:5: lv_stream_0_0= ruleChannelFormatStream
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
                    // InternalCQL.g:1515:3: ( (lv_view_1_0= ruleChannelFormatView ) )
                    {
                    // InternalCQL.g:1515:3: ( (lv_view_1_0= ruleChannelFormatView ) )
                    // InternalCQL.g:1516:4: (lv_view_1_0= ruleChannelFormatView )
                    {
                    // InternalCQL.g:1516:4: (lv_view_1_0= ruleChannelFormatView )
                    // InternalCQL.g:1517:5: lv_view_1_0= ruleChannelFormatView
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
    // InternalCQL.g:1538:1: entryRuleChannelFormatStream returns [EObject current=null] : iv_ruleChannelFormatStream= ruleChannelFormatStream EOF ;
    public final EObject entryRuleChannelFormatStream() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleChannelFormatStream = null;


        try {
            // InternalCQL.g:1538:60: (iv_ruleChannelFormatStream= ruleChannelFormatStream EOF )
            // InternalCQL.g:1539:2: iv_ruleChannelFormatStream= ruleChannelFormatStream EOF
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
    // InternalCQL.g:1545:1: ruleChannelFormatStream returns [EObject current=null] : (otherlv_0= 'STREAM' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'CHANNEL' ( (lv_host_10_0= RULE_ID ) ) otherlv_11= ':' ( (lv_port_12_0= RULE_INT ) ) ) ;
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
            // InternalCQL.g:1551:2: ( (otherlv_0= 'STREAM' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'CHANNEL' ( (lv_host_10_0= RULE_ID ) ) otherlv_11= ':' ( (lv_port_12_0= RULE_INT ) ) ) )
            // InternalCQL.g:1552:2: (otherlv_0= 'STREAM' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'CHANNEL' ( (lv_host_10_0= RULE_ID ) ) otherlv_11= ':' ( (lv_port_12_0= RULE_INT ) ) )
            {
            // InternalCQL.g:1552:2: (otherlv_0= 'STREAM' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'CHANNEL' ( (lv_host_10_0= RULE_ID ) ) otherlv_11= ':' ( (lv_port_12_0= RULE_INT ) ) )
            // InternalCQL.g:1553:3: otherlv_0= 'STREAM' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'CHANNEL' ( (lv_host_10_0= RULE_ID ) ) otherlv_11= ':' ( (lv_port_12_0= RULE_INT ) )
            {
            otherlv_0=(Token)match(input,36,FOLLOW_11); 

            			newLeafNode(otherlv_0, grammarAccess.getChannelFormatStreamAccess().getSTREAMKeyword_0());
            		
            // InternalCQL.g:1557:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQL.g:1558:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQL.g:1558:4: (lv_name_1_0= RULE_ID )
            // InternalCQL.g:1559:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_12); 

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

            otherlv_2=(Token)match(input,13,FOLLOW_11); 

            			newLeafNode(otherlv_2, grammarAccess.getChannelFormatStreamAccess().getLeftParenthesisKeyword_2());
            		
            // InternalCQL.g:1579:3: ( (lv_attributes_3_0= ruleAttribute ) )+
            int cnt34=0;
            loop34:
            do {
                int alt34=2;
                int LA34_0 = input.LA(1);

                if ( (LA34_0==RULE_ID) ) {
                    alt34=1;
                }


                switch (alt34) {
            	case 1 :
            	    // InternalCQL.g:1580:4: (lv_attributes_3_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:1580:4: (lv_attributes_3_0= ruleAttribute )
            	    // InternalCQL.g:1581:5: lv_attributes_3_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getChannelFormatStreamAccess().getAttributesAttributeParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_26);
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
            	    if ( cnt34 >= 1 ) break loop34;
                        EarlyExitException eee =
                            new EarlyExitException(34, input);
                        throw eee;
                }
                cnt34++;
            } while (true);

            // InternalCQL.g:1598:3: ( (lv_datatypes_4_0= ruleDataType ) )+
            int cnt35=0;
            loop35:
            do {
                int alt35=2;
                int LA35_0 = input.LA(1);

                if ( ((LA35_0>=15 && LA35_0<=21)) ) {
                    alt35=1;
                }


                switch (alt35) {
            	case 1 :
            	    // InternalCQL.g:1599:4: (lv_datatypes_4_0= ruleDataType )
            	    {
            	    // InternalCQL.g:1599:4: (lv_datatypes_4_0= ruleDataType )
            	    // InternalCQL.g:1600:5: lv_datatypes_4_0= ruleDataType
            	    {

            	    					newCompositeNode(grammarAccess.getChannelFormatStreamAccess().getDatatypesDataTypeParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_27);
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
            	    if ( cnt35 >= 1 ) break loop35;
                        EarlyExitException eee =
                            new EarlyExitException(35, input);
                        throw eee;
                }
                cnt35++;
            } while (true);

            // InternalCQL.g:1617:3: (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )*
            loop36:
            do {
                int alt36=2;
                int LA36_0 = input.LA(1);

                if ( (LA36_0==28) ) {
                    alt36=1;
                }


                switch (alt36) {
            	case 1 :
            	    // InternalCQL.g:1618:4: otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) )
            	    {
            	    otherlv_5=(Token)match(input,28,FOLLOW_11); 

            	    				newLeafNode(otherlv_5, grammarAccess.getChannelFormatStreamAccess().getCommaKeyword_5_0());
            	    			
            	    // InternalCQL.g:1622:4: ( (lv_attributes_6_0= ruleAttribute ) )
            	    // InternalCQL.g:1623:5: (lv_attributes_6_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:1623:5: (lv_attributes_6_0= ruleAttribute )
            	    // InternalCQL.g:1624:6: lv_attributes_6_0= ruleAttribute
            	    {

            	    						newCompositeNode(grammarAccess.getChannelFormatStreamAccess().getAttributesAttributeParserRuleCall_5_1_0());
            	    					
            	    pushFollow(FOLLOW_28);
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

            	    // InternalCQL.g:1641:4: ( (lv_datatypes_7_0= ruleDataType ) )
            	    // InternalCQL.g:1642:5: (lv_datatypes_7_0= ruleDataType )
            	    {
            	    // InternalCQL.g:1642:5: (lv_datatypes_7_0= ruleDataType )
            	    // InternalCQL.g:1643:6: lv_datatypes_7_0= ruleDataType
            	    {

            	    						newCompositeNode(grammarAccess.getChannelFormatStreamAccess().getDatatypesDataTypeParserRuleCall_5_2_0());
            	    					
            	    pushFollow(FOLLOW_29);
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
            	    break loop36;
                }
            } while (true);

            otherlv_8=(Token)match(input,14,FOLLOW_37); 

            			newLeafNode(otherlv_8, grammarAccess.getChannelFormatStreamAccess().getRightParenthesisKeyword_6());
            		
            otherlv_9=(Token)match(input,43,FOLLOW_11); 

            			newLeafNode(otherlv_9, grammarAccess.getChannelFormatStreamAccess().getCHANNELKeyword_7());
            		
            // InternalCQL.g:1669:3: ( (lv_host_10_0= RULE_ID ) )
            // InternalCQL.g:1670:4: (lv_host_10_0= RULE_ID )
            {
            // InternalCQL.g:1670:4: (lv_host_10_0= RULE_ID )
            // InternalCQL.g:1671:5: lv_host_10_0= RULE_ID
            {
            lv_host_10_0=(Token)match(input,RULE_ID,FOLLOW_38); 

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

            otherlv_11=(Token)match(input,44,FOLLOW_39); 

            			newLeafNode(otherlv_11, grammarAccess.getChannelFormatStreamAccess().getColonKeyword_9());
            		
            // InternalCQL.g:1691:3: ( (lv_port_12_0= RULE_INT ) )
            // InternalCQL.g:1692:4: (lv_port_12_0= RULE_INT )
            {
            // InternalCQL.g:1692:4: (lv_port_12_0= RULE_INT )
            // InternalCQL.g:1693:5: lv_port_12_0= RULE_INT
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
    // InternalCQL.g:1713:1: entryRuleChannelFormatView returns [EObject current=null] : iv_ruleChannelFormatView= ruleChannelFormatView EOF ;
    public final EObject entryRuleChannelFormatView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleChannelFormatView = null;


        try {
            // InternalCQL.g:1713:58: (iv_ruleChannelFormatView= ruleChannelFormatView EOF )
            // InternalCQL.g:1714:2: iv_ruleChannelFormatView= ruleChannelFormatView EOF
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
    // InternalCQL.g:1720:1: ruleChannelFormatView returns [EObject current=null] : (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' otherlv_3= '(' ( (lv_select_4_0= ruleSelect ) ) (otherlv_5= ';' )? otherlv_6= ')' ) ;
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
            // InternalCQL.g:1726:2: ( (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' otherlv_3= '(' ( (lv_select_4_0= ruleSelect ) ) (otherlv_5= ';' )? otherlv_6= ')' ) )
            // InternalCQL.g:1727:2: (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' otherlv_3= '(' ( (lv_select_4_0= ruleSelect ) ) (otherlv_5= ';' )? otherlv_6= ')' )
            {
            // InternalCQL.g:1727:2: (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' otherlv_3= '(' ( (lv_select_4_0= ruleSelect ) ) (otherlv_5= ';' )? otherlv_6= ')' )
            // InternalCQL.g:1728:3: otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' otherlv_3= '(' ( (lv_select_4_0= ruleSelect ) ) (otherlv_5= ';' )? otherlv_6= ')'
            {
            otherlv_0=(Token)match(input,45,FOLLOW_11); 

            			newLeafNode(otherlv_0, grammarAccess.getChannelFormatViewAccess().getVIEWKeyword_0());
            		
            // InternalCQL.g:1732:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQL.g:1733:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQL.g:1733:4: (lv_name_1_0= RULE_ID )
            // InternalCQL.g:1734:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_15); 

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

            otherlv_2=(Token)match(input,29,FOLLOW_12); 

            			newLeafNode(otherlv_2, grammarAccess.getChannelFormatViewAccess().getFROMKeyword_2());
            		
            otherlv_3=(Token)match(input,13,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getChannelFormatViewAccess().getLeftParenthesisKeyword_3());
            		
            // InternalCQL.g:1758:3: ( (lv_select_4_0= ruleSelect ) )
            // InternalCQL.g:1759:4: (lv_select_4_0= ruleSelect )
            {
            // InternalCQL.g:1759:4: (lv_select_4_0= ruleSelect )
            // InternalCQL.g:1760:5: lv_select_4_0= ruleSelect
            {

            					newCompositeNode(grammarAccess.getChannelFormatViewAccess().getSelectSelectParserRuleCall_4_0());
            				
            pushFollow(FOLLOW_40);
            lv_select_4_0=ruleSelect();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getChannelFormatViewRule());
            					}
            					set(
            						current,
            						"select",
            						lv_select_4_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Select");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQL.g:1777:3: (otherlv_5= ';' )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==12) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // InternalCQL.g:1778:4: otherlv_5= ';'
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
    // InternalCQL.g:1791:1: entryRuleStreamTo returns [EObject current=null] : iv_ruleStreamTo= ruleStreamTo EOF ;
    public final EObject entryRuleStreamTo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStreamTo = null;


        try {
            // InternalCQL.g:1791:49: (iv_ruleStreamTo= ruleStreamTo EOF )
            // InternalCQL.g:1792:2: iv_ruleStreamTo= ruleStreamTo EOF
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
    // InternalCQL.g:1798:1: ruleStreamTo returns [EObject current=null] : (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) ;
    public final EObject ruleStreamTo() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token lv_inputname_4_0=null;
        EObject lv_statement_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1804:2: ( (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) )
            // InternalCQL.g:1805:2: (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            {
            // InternalCQL.g:1805:2: (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            // InternalCQL.g:1806:3: otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            {
            otherlv_0=(Token)match(input,36,FOLLOW_41); 

            			newLeafNode(otherlv_0, grammarAccess.getStreamToAccess().getSTREAMKeyword_0());
            		
            otherlv_1=(Token)match(input,46,FOLLOW_11); 

            			newLeafNode(otherlv_1, grammarAccess.getStreamToAccess().getTOKeyword_1());
            		
            // InternalCQL.g:1814:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCQL.g:1815:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCQL.g:1815:4: (lv_name_2_0= RULE_ID )
            // InternalCQL.g:1816:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_42); 

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

            // InternalCQL.g:1832:3: ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==25) ) {
                alt38=1;
            }
            else if ( (LA38_0==RULE_ID) ) {
                alt38=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 0, input);

                throw nvae;
            }
            switch (alt38) {
                case 1 :
                    // InternalCQL.g:1833:4: ( (lv_statement_3_0= ruleSelect ) )
                    {
                    // InternalCQL.g:1833:4: ( (lv_statement_3_0= ruleSelect ) )
                    // InternalCQL.g:1834:5: (lv_statement_3_0= ruleSelect )
                    {
                    // InternalCQL.g:1834:5: (lv_statement_3_0= ruleSelect )
                    // InternalCQL.g:1835:6: lv_statement_3_0= ruleSelect
                    {

                    						newCompositeNode(grammarAccess.getStreamToAccess().getStatementSelectParserRuleCall_3_0_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_statement_3_0=ruleSelect();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStreamToRule());
                    						}
                    						set(
                    							current,
                    							"statement",
                    							lv_statement_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Select");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1853:4: ( (lv_inputname_4_0= RULE_ID ) )
                    {
                    // InternalCQL.g:1853:4: ( (lv_inputname_4_0= RULE_ID ) )
                    // InternalCQL.g:1854:5: (lv_inputname_4_0= RULE_ID )
                    {
                    // InternalCQL.g:1854:5: (lv_inputname_4_0= RULE_ID )
                    // InternalCQL.g:1855:6: lv_inputname_4_0= RULE_ID
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
    // InternalCQL.g:1876:1: entryRuleDrop returns [EObject current=null] : iv_ruleDrop= ruleDrop EOF ;
    public final EObject entryRuleDrop() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDrop = null;


        try {
            // InternalCQL.g:1876:45: (iv_ruleDrop= ruleDrop EOF )
            // InternalCQL.g:1877:2: iv_ruleDrop= ruleDrop EOF
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
    // InternalCQL.g:1883:1: ruleDrop returns [EObject current=null] : (otherlv_0= 'DROP' ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) ) (otherlv_3= 'IF' otherlv_4= 'EXISTS' )? ) ;
    public final EObject ruleDrop() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_4=null;


        	enterRule();

        try {
            // InternalCQL.g:1889:2: ( (otherlv_0= 'DROP' ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) ) (otherlv_3= 'IF' otherlv_4= 'EXISTS' )? ) )
            // InternalCQL.g:1890:2: (otherlv_0= 'DROP' ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) ) (otherlv_3= 'IF' otherlv_4= 'EXISTS' )? )
            {
            // InternalCQL.g:1890:2: (otherlv_0= 'DROP' ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) ) (otherlv_3= 'IF' otherlv_4= 'EXISTS' )? )
            // InternalCQL.g:1891:3: otherlv_0= 'DROP' ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) ) (otherlv_3= 'IF' otherlv_4= 'EXISTS' )?
            {
            otherlv_0=(Token)match(input,47,FOLLOW_43); 

            			newLeafNode(otherlv_0, grammarAccess.getDropAccess().getDROPKeyword_0());
            		
            // InternalCQL.g:1895:3: ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) )
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==37) ) {
                alt39=1;
            }
            else if ( (LA39_0==36) ) {
                alt39=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;
            }
            switch (alt39) {
                case 1 :
                    // InternalCQL.g:1896:4: ( (lv_name_1_0= 'SINK' ) )
                    {
                    // InternalCQL.g:1896:4: ( (lv_name_1_0= 'SINK' ) )
                    // InternalCQL.g:1897:5: (lv_name_1_0= 'SINK' )
                    {
                    // InternalCQL.g:1897:5: (lv_name_1_0= 'SINK' )
                    // InternalCQL.g:1898:6: lv_name_1_0= 'SINK'
                    {
                    lv_name_1_0=(Token)match(input,37,FOLLOW_44); 

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
                    // InternalCQL.g:1911:4: ( (lv_name_2_0= 'STREAM' ) )
                    {
                    // InternalCQL.g:1911:4: ( (lv_name_2_0= 'STREAM' ) )
                    // InternalCQL.g:1912:5: (lv_name_2_0= 'STREAM' )
                    {
                    // InternalCQL.g:1912:5: (lv_name_2_0= 'STREAM' )
                    // InternalCQL.g:1913:6: lv_name_2_0= 'STREAM'
                    {
                    lv_name_2_0=(Token)match(input,36,FOLLOW_44); 

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

            // InternalCQL.g:1926:3: (otherlv_3= 'IF' otherlv_4= 'EXISTS' )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==48) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // InternalCQL.g:1927:4: otherlv_3= 'IF' otherlv_4= 'EXISTS'
                    {
                    otherlv_3=(Token)match(input,48,FOLLOW_45); 

                    				newLeafNode(otherlv_3, grammarAccess.getDropAccess().getIFKeyword_2_0());
                    			
                    otherlv_4=(Token)match(input,49,FOLLOW_2); 

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
    // InternalCQL.g:1940:1: entryRuleWindow_Unbounded returns [String current=null] : iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF ;
    public final String entryRuleWindow_Unbounded() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleWindow_Unbounded = null;


        try {
            // InternalCQL.g:1940:56: (iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF )
            // InternalCQL.g:1941:2: iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF
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
    // InternalCQL.g:1947:1: ruleWindow_Unbounded returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= 'UNBOUNDED' ;
    public final AntlrDatatypeRuleToken ruleWindow_Unbounded() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQL.g:1953:2: (kw= 'UNBOUNDED' )
            // InternalCQL.g:1954:2: kw= 'UNBOUNDED'
            {
            kw=(Token)match(input,50,FOLLOW_2); 

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
    // InternalCQL.g:1962:1: entryRuleWindow_Timebased returns [EObject current=null] : iv_ruleWindow_Timebased= ruleWindow_Timebased EOF ;
    public final EObject entryRuleWindow_Timebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Timebased = null;


        try {
            // InternalCQL.g:1962:57: (iv_ruleWindow_Timebased= ruleWindow_Timebased EOF )
            // InternalCQL.g:1963:2: iv_ruleWindow_Timebased= ruleWindow_Timebased EOF
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
    // InternalCQL.g:1969:1: ruleWindow_Timebased returns [EObject current=null] : (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' ) ;
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
            // InternalCQL.g:1975:2: ( (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' ) )
            // InternalCQL.g:1976:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' )
            {
            // InternalCQL.g:1976:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' )
            // InternalCQL.g:1977:3: otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME'
            {
            otherlv_0=(Token)match(input,51,FOLLOW_39); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TimebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQL.g:1981:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQL.g:1982:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQL.g:1982:4: (lv_size_1_0= RULE_INT )
            // InternalCQL.g:1983:5: lv_size_1_0= RULE_INT
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

            // InternalCQL.g:1999:3: ( (lv_unit_2_0= RULE_ID ) )
            // InternalCQL.g:2000:4: (lv_unit_2_0= RULE_ID )
            {
            // InternalCQL.g:2000:4: (lv_unit_2_0= RULE_ID )
            // InternalCQL.g:2001:5: lv_unit_2_0= RULE_ID
            {
            lv_unit_2_0=(Token)match(input,RULE_ID,FOLLOW_46); 

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

            // InternalCQL.g:2017:3: (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )?
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( (LA41_0==52) ) {
                alt41=1;
            }
            switch (alt41) {
                case 1 :
                    // InternalCQL.g:2018:4: otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) )
                    {
                    otherlv_3=(Token)match(input,52,FOLLOW_39); 

                    				newLeafNode(otherlv_3, grammarAccess.getWindow_TimebasedAccess().getADVANCEKeyword_3_0());
                    			
                    // InternalCQL.g:2022:4: ( (lv_advance_size_4_0= RULE_INT ) )
                    // InternalCQL.g:2023:5: (lv_advance_size_4_0= RULE_INT )
                    {
                    // InternalCQL.g:2023:5: (lv_advance_size_4_0= RULE_INT )
                    // InternalCQL.g:2024:6: lv_advance_size_4_0= RULE_INT
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

                    // InternalCQL.g:2040:4: ( (lv_advance_unit_5_0= RULE_ID ) )
                    // InternalCQL.g:2041:5: (lv_advance_unit_5_0= RULE_ID )
                    {
                    // InternalCQL.g:2041:5: (lv_advance_unit_5_0= RULE_ID )
                    // InternalCQL.g:2042:6: lv_advance_unit_5_0= RULE_ID
                    {
                    lv_advance_unit_5_0=(Token)match(input,RULE_ID,FOLLOW_47); 

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

            otherlv_6=(Token)match(input,53,FOLLOW_2); 

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
    // InternalCQL.g:2067:1: entryRuleWindow_Tuplebased returns [EObject current=null] : iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF ;
    public final EObject entryRuleWindow_Tuplebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Tuplebased = null;


        try {
            // InternalCQL.g:2067:58: (iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF )
            // InternalCQL.g:2068:2: iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF
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
    // InternalCQL.g:2074:1: ruleWindow_Tuplebased returns [EObject current=null] : (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) ;
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
            // InternalCQL.g:2080:2: ( (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) )
            // InternalCQL.g:2081:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            {
            // InternalCQL.g:2081:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            // InternalCQL.g:2082:3: otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            {
            otherlv_0=(Token)match(input,51,FOLLOW_39); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TuplebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQL.g:2086:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQL.g:2087:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQL.g:2087:4: (lv_size_1_0= RULE_INT )
            // InternalCQL.g:2088:5: lv_size_1_0= RULE_INT
            {
            lv_size_1_0=(Token)match(input,RULE_INT,FOLLOW_48); 

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

            // InternalCQL.g:2104:3: (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==52) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // InternalCQL.g:2105:4: otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) )
                    {
                    otherlv_2=(Token)match(input,52,FOLLOW_39); 

                    				newLeafNode(otherlv_2, grammarAccess.getWindow_TuplebasedAccess().getADVANCEKeyword_2_0());
                    			
                    // InternalCQL.g:2109:4: ( (lv_advance_size_3_0= RULE_INT ) )
                    // InternalCQL.g:2110:5: (lv_advance_size_3_0= RULE_INT )
                    {
                    // InternalCQL.g:2110:5: (lv_advance_size_3_0= RULE_INT )
                    // InternalCQL.g:2111:6: lv_advance_size_3_0= RULE_INT
                    {
                    lv_advance_size_3_0=(Token)match(input,RULE_INT,FOLLOW_49); 

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

            otherlv_4=(Token)match(input,54,FOLLOW_50); 

            			newLeafNode(otherlv_4, grammarAccess.getWindow_TuplebasedAccess().getTUPLEKeyword_3());
            		
            // InternalCQL.g:2132:3: (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==55) ) {
                alt43=1;
            }
            switch (alt43) {
                case 1 :
                    // InternalCQL.g:2133:4: otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    {
                    otherlv_5=(Token)match(input,55,FOLLOW_22); 

                    				newLeafNode(otherlv_5, grammarAccess.getWindow_TuplebasedAccess().getPARTITIONKeyword_4_0());
                    			
                    otherlv_6=(Token)match(input,32,FOLLOW_11); 

                    				newLeafNode(otherlv_6, grammarAccess.getWindow_TuplebasedAccess().getBYKeyword_4_1());
                    			
                    // InternalCQL.g:2141:4: ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    // InternalCQL.g:2142:5: (lv_partition_attribute_7_0= ruleAttribute )
                    {
                    // InternalCQL.g:2142:5: (lv_partition_attribute_7_0= ruleAttribute )
                    // InternalCQL.g:2143:6: lv_partition_attribute_7_0= ruleAttribute
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


    // $ANTLR start "entryRuleExpressionsModel"
    // InternalCQL.g:2165:1: entryRuleExpressionsModel returns [EObject current=null] : iv_ruleExpressionsModel= ruleExpressionsModel EOF ;
    public final EObject entryRuleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionsModel = null;


        try {
            // InternalCQL.g:2165:57: (iv_ruleExpressionsModel= ruleExpressionsModel EOF )
            // InternalCQL.g:2166:2: iv_ruleExpressionsModel= ruleExpressionsModel EOF
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
    // InternalCQL.g:2172:1: ruleExpressionsModel returns [EObject current=null] : ( () ( (lv_elements_1_0= ruleExpression ) ) ) ;
    public final EObject ruleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2178:2: ( ( () ( (lv_elements_1_0= ruleExpression ) ) ) )
            // InternalCQL.g:2179:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            {
            // InternalCQL.g:2179:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            // InternalCQL.g:2180:3: () ( (lv_elements_1_0= ruleExpression ) )
            {
            // InternalCQL.g:2180:3: ()
            // InternalCQL.g:2181:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0(),
            					current);
            			

            }

            // InternalCQL.g:2187:3: ( (lv_elements_1_0= ruleExpression ) )
            // InternalCQL.g:2188:4: (lv_elements_1_0= ruleExpression )
            {
            // InternalCQL.g:2188:4: (lv_elements_1_0= ruleExpression )
            // InternalCQL.g:2189:5: lv_elements_1_0= ruleExpression
            {

            					newCompositeNode(grammarAccess.getExpressionsModelAccess().getElementsExpressionParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
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
    // InternalCQL.g:2210:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCQL.g:2210:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCQL.g:2211:2: iv_ruleExpression= ruleExpression EOF
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
    // InternalCQL.g:2217:1: ruleExpression returns [EObject current=null] : this_Or_0= ruleOr ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_Or_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2223:2: (this_Or_0= ruleOr )
            // InternalCQL.g:2224:2: this_Or_0= ruleOr
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
    // InternalCQL.g:2235:1: entryRuleOr returns [EObject current=null] : iv_ruleOr= ruleOr EOF ;
    public final EObject entryRuleOr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOr = null;


        try {
            // InternalCQL.g:2235:43: (iv_ruleOr= ruleOr EOF )
            // InternalCQL.g:2236:2: iv_ruleOr= ruleOr EOF
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
    // InternalCQL.g:2242:1: ruleOr returns [EObject current=null] : (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) ;
    public final EObject ruleOr() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_And_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2248:2: ( (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) )
            // InternalCQL.g:2249:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            {
            // InternalCQL.g:2249:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            // InternalCQL.g:2250:3: this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrAccess().getAndParserRuleCall_0());
            		
            pushFollow(FOLLOW_51);
            this_And_0=ruleAnd();

            state._fsp--;


            			current = this_And_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2258:3: ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            loop44:
            do {
                int alt44=2;
                int LA44_0 = input.LA(1);

                if ( (LA44_0==56) ) {
                    alt44=1;
                }


                switch (alt44) {
            	case 1 :
            	    // InternalCQL.g:2259:4: () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) )
            	    {
            	    // InternalCQL.g:2259:4: ()
            	    // InternalCQL.g:2260:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrAccess().getOrLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,56,FOLLOW_20); 

            	    				newLeafNode(otherlv_2, grammarAccess.getOrAccess().getORKeyword_1_1());
            	    			
            	    // InternalCQL.g:2270:4: ( (lv_right_3_0= ruleAnd ) )
            	    // InternalCQL.g:2271:5: (lv_right_3_0= ruleAnd )
            	    {
            	    // InternalCQL.g:2271:5: (lv_right_3_0= ruleAnd )
            	    // InternalCQL.g:2272:6: lv_right_3_0= ruleAnd
            	    {

            	    						newCompositeNode(grammarAccess.getOrAccess().getRightAndParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_51);
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
            	    break loop44;
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
    // InternalCQL.g:2294:1: entryRuleAnd returns [EObject current=null] : iv_ruleAnd= ruleAnd EOF ;
    public final EObject entryRuleAnd() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnd = null;


        try {
            // InternalCQL.g:2294:44: (iv_ruleAnd= ruleAnd EOF )
            // InternalCQL.g:2295:2: iv_ruleAnd= ruleAnd EOF
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
    // InternalCQL.g:2301:1: ruleAnd returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAnd() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2307:2: ( (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCQL.g:2308:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCQL.g:2308:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCQL.g:2309:3: this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_52);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2317:3: ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop45:
            do {
                int alt45=2;
                int LA45_0 = input.LA(1);

                if ( (LA45_0==57) ) {
                    alt45=1;
                }


                switch (alt45) {
            	case 1 :
            	    // InternalCQL.g:2318:4: () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCQL.g:2318:4: ()
            	    // InternalCQL.g:2319:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndAccess().getAndLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,57,FOLLOW_20); 

            	    				newLeafNode(otherlv_2, grammarAccess.getAndAccess().getANDKeyword_1_1());
            	    			
            	    // InternalCQL.g:2329:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCQL.g:2330:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCQL.g:2330:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCQL.g:2331:6: lv_right_3_0= ruleEqualitiy
            	    {

            	    						newCompositeNode(grammarAccess.getAndAccess().getRightEqualitiyParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_52);
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
            	    break loop45;
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
    // InternalCQL.g:2353:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCQL.g:2353:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCQL.g:2354:2: iv_ruleEqualitiy= ruleEqualitiy EOF
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
    // InternalCQL.g:2360:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Comparison_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2366:2: ( (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCQL.g:2367:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCQL.g:2367:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCQL.g:2368:3: this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_53);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2376:3: ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop47:
            do {
                int alt47=2;
                int LA47_0 = input.LA(1);

                if ( ((LA47_0>=58 && LA47_0<=59)) ) {
                    alt47=1;
                }


                switch (alt47) {
            	case 1 :
            	    // InternalCQL.g:2377:4: () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCQL.g:2377:4: ()
            	    // InternalCQL.g:2378:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:2384:4: ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) )
            	    // InternalCQL.g:2385:5: ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) )
            	    {
            	    // InternalCQL.g:2385:5: ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) )
            	    // InternalCQL.g:2386:6: (lv_op_2_1= '==' | lv_op_2_2= '!=' )
            	    {
            	    // InternalCQL.g:2386:6: (lv_op_2_1= '==' | lv_op_2_2= '!=' )
            	    int alt46=2;
            	    int LA46_0 = input.LA(1);

            	    if ( (LA46_0==58) ) {
            	        alt46=1;
            	    }
            	    else if ( (LA46_0==59) ) {
            	        alt46=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 46, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt46) {
            	        case 1 :
            	            // InternalCQL.g:2387:7: lv_op_2_1= '=='
            	            {
            	            lv_op_2_1=(Token)match(input,58,FOLLOW_20); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getEqualitiyAccess().getOpEqualsSignEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getEqualitiyRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:2398:7: lv_op_2_2= '!='
            	            {
            	            lv_op_2_2=(Token)match(input,59,FOLLOW_20); 

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

            	    // InternalCQL.g:2411:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCQL.g:2412:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCQL.g:2412:5: (lv_right_3_0= ruleComparison )
            	    // InternalCQL.g:2413:6: lv_right_3_0= ruleComparison
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getRightComparisonParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_53);
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
            	    break loop47;
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
    // InternalCQL.g:2435:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCQL.g:2435:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCQL.g:2436:2: iv_ruleComparison= ruleComparison EOF
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
    // InternalCQL.g:2442:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
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
            // InternalCQL.g:2448:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCQL.g:2449:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCQL.g:2449:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCQL.g:2450:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_54);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2458:3: ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop49:
            do {
                int alt49=2;
                int LA49_0 = input.LA(1);

                if ( ((LA49_0>=60 && LA49_0<=63)) ) {
                    alt49=1;
                }


                switch (alt49) {
            	case 1 :
            	    // InternalCQL.g:2459:4: () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCQL.g:2459:4: ()
            	    // InternalCQL.g:2460:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:2466:4: ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) )
            	    // InternalCQL.g:2467:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    {
            	    // InternalCQL.g:2467:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    // InternalCQL.g:2468:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    {
            	    // InternalCQL.g:2468:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    int alt48=4;
            	    switch ( input.LA(1) ) {
            	    case 60:
            	        {
            	        alt48=1;
            	        }
            	        break;
            	    case 61:
            	        {
            	        alt48=2;
            	        }
            	        break;
            	    case 62:
            	        {
            	        alt48=3;
            	        }
            	        break;
            	    case 63:
            	        {
            	        alt48=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 48, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt48) {
            	        case 1 :
            	            // InternalCQL.g:2469:7: lv_op_2_1= '>='
            	            {
            	            lv_op_2_1=(Token)match(input,60,FOLLOW_20); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:2480:7: lv_op_2_2= '<='
            	            {
            	            lv_op_2_2=(Token)match(input,61,FOLLOW_20); 

            	            							newLeafNode(lv_op_2_2, grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_2, null);
            	            						

            	            }
            	            break;
            	        case 3 :
            	            // InternalCQL.g:2491:7: lv_op_2_3= '<'
            	            {
            	            lv_op_2_3=(Token)match(input,62,FOLLOW_20); 

            	            							newLeafNode(lv_op_2_3, grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_3, null);
            	            						

            	            }
            	            break;
            	        case 4 :
            	            // InternalCQL.g:2502:7: lv_op_2_4= '>'
            	            {
            	            lv_op_2_4=(Token)match(input,63,FOLLOW_20); 

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

            	    // InternalCQL.g:2515:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCQL.g:2516:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCQL.g:2516:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCQL.g:2517:6: lv_right_3_0= rulePlusOrMinus
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getRightPlusOrMinusParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_54);
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
            	    break loop49;
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
    // InternalCQL.g:2539:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCQL.g:2539:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCQL.g:2540:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
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
    // InternalCQL.g:2546:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2552:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCQL.g:2553:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCQL.g:2553:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCQL.g:2554:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_55);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2562:3: ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop51:
            do {
                int alt51=2;
                int LA51_0 = input.LA(1);

                if ( ((LA51_0>=64 && LA51_0<=65)) ) {
                    alt51=1;
                }


                switch (alt51) {
            	case 1 :
            	    // InternalCQL.g:2563:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCQL.g:2563:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) )
            	    int alt50=2;
            	    int LA50_0 = input.LA(1);

            	    if ( (LA50_0==64) ) {
            	        alt50=1;
            	    }
            	    else if ( (LA50_0==65) ) {
            	        alt50=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 50, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt50) {
            	        case 1 :
            	            // InternalCQL.g:2564:5: ( () otherlv_2= '+' )
            	            {
            	            // InternalCQL.g:2564:5: ( () otherlv_2= '+' )
            	            // InternalCQL.g:2565:6: () otherlv_2= '+'
            	            {
            	            // InternalCQL.g:2565:6: ()
            	            // InternalCQL.g:2566:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_2=(Token)match(input,64,FOLLOW_20); 

            	            						newLeafNode(otherlv_2, grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1());
            	            					

            	            }


            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:2578:5: ( () otherlv_4= '-' )
            	            {
            	            // InternalCQL.g:2578:5: ( () otherlv_4= '-' )
            	            // InternalCQL.g:2579:6: () otherlv_4= '-'
            	            {
            	            // InternalCQL.g:2579:6: ()
            	            // InternalCQL.g:2580:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_4=(Token)match(input,65,FOLLOW_20); 

            	            						newLeafNode(otherlv_4, grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1());
            	            					

            	            }


            	            }
            	            break;

            	    }

            	    // InternalCQL.g:2592:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCQL.g:2593:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCQL.g:2593:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCQL.g:2594:6: lv_right_5_0= ruleMulOrDiv
            	    {

            	    						newCompositeNode(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_55);
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
            	    break loop51;
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
    // InternalCQL.g:2616:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCQL.g:2616:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCQL.g:2617:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
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
    // InternalCQL.g:2623:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2629:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCQL.g:2630:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCQL.g:2630:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCQL.g:2631:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_56);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2639:3: ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop53:
            do {
                int alt53=2;
                int LA53_0 = input.LA(1);

                if ( (LA53_0==27||LA53_0==66) ) {
                    alt53=1;
                }


                switch (alt53) {
            	case 1 :
            	    // InternalCQL.g:2640:4: () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCQL.g:2640:4: ()
            	    // InternalCQL.g:2641:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:2647:4: ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) )
            	    // InternalCQL.g:2648:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    {
            	    // InternalCQL.g:2648:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    // InternalCQL.g:2649:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    {
            	    // InternalCQL.g:2649:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    int alt52=2;
            	    int LA52_0 = input.LA(1);

            	    if ( (LA52_0==27) ) {
            	        alt52=1;
            	    }
            	    else if ( (LA52_0==66) ) {
            	        alt52=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 52, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt52) {
            	        case 1 :
            	            // InternalCQL.g:2650:7: lv_op_2_1= '*'
            	            {
            	            lv_op_2_1=(Token)match(input,27,FOLLOW_20); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getMulOrDivRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:2661:7: lv_op_2_2= '/'
            	            {
            	            lv_op_2_2=(Token)match(input,66,FOLLOW_20); 

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

            	    // InternalCQL.g:2674:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCQL.g:2675:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCQL.g:2675:5: (lv_right_3_0= rulePrimary )
            	    // InternalCQL.g:2676:6: lv_right_3_0= rulePrimary
            	    {

            	    						newCompositeNode(grammarAccess.getMulOrDivAccess().getRightPrimaryParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_56);
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
            	    break loop53;
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
    // InternalCQL.g:2698:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCQL.g:2698:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCQL.g:2699:2: iv_rulePrimary= rulePrimary EOF
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
    // InternalCQL.g:2705:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
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
            // InternalCQL.g:2711:2: ( ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCQL.g:2712:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCQL.g:2712:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt54=3;
            switch ( input.LA(1) ) {
            case 13:
                {
                alt54=1;
                }
                break;
            case 67:
                {
                alt54=2;
                }
                break;
            case RULE_ID:
            case RULE_STRING:
            case RULE_INT:
            case RULE_FLOAT_NUMBER:
            case 68:
            case 69:
                {
                alt54=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 54, 0, input);

                throw nvae;
            }

            switch (alt54) {
                case 1 :
                    // InternalCQL.g:2713:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    {
                    // InternalCQL.g:2713:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    // InternalCQL.g:2714:4: () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')'
                    {
                    // InternalCQL.g:2714:4: ()
                    // InternalCQL.g:2715:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,13,FOLLOW_20); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCQL.g:2725:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCQL.g:2726:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCQL.g:2726:5: (lv_inner_2_0= ruleExpression )
                    // InternalCQL.g:2727:6: lv_inner_2_0= ruleExpression
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
                    // InternalCQL.g:2750:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCQL.g:2750:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCQL.g:2751:4: () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCQL.g:2751:4: ()
                    // InternalCQL.g:2752:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,67,FOLLOW_20); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCQL.g:2762:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCQL.g:2763:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCQL.g:2763:5: (lv_expression_6_0= rulePrimary )
                    // InternalCQL.g:2764:6: lv_expression_6_0= rulePrimary
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
                    // InternalCQL.g:2783:3: this_Atomic_7= ruleAtomic
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


    // $ANTLR start "entryRuleAtomic"
    // InternalCQL.g:2795:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCQL.g:2795:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCQL.g:2796:2: iv_ruleAtomic= ruleAtomic EOF
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
    // InternalCQL.g:2802:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) ) ) ;
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
            // InternalCQL.g:2808:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) ) ) )
            // InternalCQL.g:2809:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) ) )
            {
            // InternalCQL.g:2809:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) ) )
            int alt56=5;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt56=1;
                }
                break;
            case RULE_FLOAT_NUMBER:
                {
                alt56=2;
                }
                break;
            case RULE_STRING:
                {
                alt56=3;
                }
                break;
            case 68:
            case 69:
                {
                alt56=4;
                }
                break;
            case RULE_ID:
                {
                alt56=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;
            }

            switch (alt56) {
                case 1 :
                    // InternalCQL.g:2810:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQL.g:2810:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQL.g:2811:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQL.g:2811:4: ()
                    // InternalCQL.g:2812:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:2818:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQL.g:2819:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQL.g:2819:5: (lv_value_1_0= RULE_INT )
                    // InternalCQL.g:2820:6: lv_value_1_0= RULE_INT
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
                    // InternalCQL.g:2838:3: ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) )
                    {
                    // InternalCQL.g:2838:3: ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) )
                    // InternalCQL.g:2839:4: () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) )
                    {
                    // InternalCQL.g:2839:4: ()
                    // InternalCQL.g:2840:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:2846:4: ( (lv_value_3_0= RULE_FLOAT_NUMBER ) )
                    // InternalCQL.g:2847:5: (lv_value_3_0= RULE_FLOAT_NUMBER )
                    {
                    // InternalCQL.g:2847:5: (lv_value_3_0= RULE_FLOAT_NUMBER )
                    // InternalCQL.g:2848:6: lv_value_3_0= RULE_FLOAT_NUMBER
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
                    // InternalCQL.g:2866:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQL.g:2866:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQL.g:2867:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQL.g:2867:4: ()
                    // InternalCQL.g:2868:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:2874:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQL.g:2875:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQL.g:2875:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQL.g:2876:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQL.g:2894:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    {
                    // InternalCQL.g:2894:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    // InternalCQL.g:2895:4: () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    {
                    // InternalCQL.g:2895:4: ()
                    // InternalCQL.g:2896:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:2902:4: ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    // InternalCQL.g:2903:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    {
                    // InternalCQL.g:2903:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    // InternalCQL.g:2904:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    {
                    // InternalCQL.g:2904:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    int alt55=2;
                    int LA55_0 = input.LA(1);

                    if ( (LA55_0==68) ) {
                        alt55=1;
                    }
                    else if ( (LA55_0==69) ) {
                        alt55=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 55, 0, input);

                        throw nvae;
                    }
                    switch (alt55) {
                        case 1 :
                            // InternalCQL.g:2905:7: lv_value_7_1= 'TRUE'
                            {
                            lv_value_7_1=(Token)match(input,68,FOLLOW_2); 

                            							newLeafNode(lv_value_7_1, grammarAccess.getAtomicAccess().getValueTRUEKeyword_3_1_0_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getAtomicRule());
                            							}
                            							setWithLastConsumed(current, "value", lv_value_7_1, null);
                            						

                            }
                            break;
                        case 2 :
                            // InternalCQL.g:2916:7: lv_value_7_2= 'FALSE'
                            {
                            lv_value_7_2=(Token)match(input,69,FOLLOW_2); 

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
                    // InternalCQL.g:2931:3: ( () ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) )
                    {
                    // InternalCQL.g:2931:3: ( () ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) )
                    // InternalCQL.g:2932:4: () ( (lv_value_9_0= ruleAttributeWithoutAlias ) )
                    {
                    // InternalCQL.g:2932:4: ()
                    // InternalCQL.g:2933:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeRefAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:2939:4: ( (lv_value_9_0= ruleAttributeWithoutAlias ) )
                    // InternalCQL.g:2940:5: (lv_value_9_0= ruleAttributeWithoutAlias )
                    {
                    // InternalCQL.g:2940:5: (lv_value_9_0= ruleAttributeWithoutAlias )
                    // InternalCQL.g:2941:6: lv_value_9_0= ruleAttributeWithoutAlias
                    {

                    						newCompositeNode(grammarAccess.getAtomicAccess().getValueAttributeWithoutAliasParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_value_9_0=ruleAttributeWithoutAlias();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getAtomicRule());
                    						}
                    						set(
                    							current,
                    							"value",
                    							lv_value_9_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAlias");
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

    // Delegated rules


    protected DFA26 dfa26 = new DFA26(this);
    static final String dfa_1s = "\34\uffff";
    static final String dfa_2s = "\1\44\1\4\2\uffff\1\15\3\4\7\16\2\4\1\46\1\17\1\4\7\16\1\17";
    static final String dfa_3s = "\1\55\1\4\2\uffff\1\15\1\4\1\30\1\4\7\34\1\25\1\4\1\53\1\30\1\4\7\34\1\25";
    static final String dfa_4s = "\2\uffff\1\1\1\2\30\uffff";
    static final String dfa_5s = "\34\uffff}>";
    static final String[] dfa_6s = {
            "\1\1\1\3\7\uffff\1\2",
            "\1\4",
            "",
            "",
            "\1\5",
            "\1\6",
            "\1\6\12\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16\2\uffff\1\7",
            "\1\17",
            "\1\21\1\10\1\11\1\12\1\13\1\14\1\15\1\16\6\uffff\1\20",
            "\1\21\1\10\1\11\1\12\1\13\1\14\1\15\1\16\6\uffff\1\20",
            "\1\21\1\10\1\11\1\12\1\13\1\14\1\15\1\16\6\uffff\1\20",
            "\1\21\1\10\1\11\1\12\1\13\1\14\1\15\1\16\6\uffff\1\20",
            "\1\21\1\10\1\11\1\12\1\13\1\14\1\15\1\16\6\uffff\1\20",
            "\1\21\1\10\1\11\1\12\1\13\1\14\1\15\1\16\6\uffff\1\20",
            "\1\21\1\10\1\11\1\12\1\13\1\14\1\15\1\16\6\uffff\1\20",
            "\1\6\12\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16",
            "\1\22",
            "\1\3\4\uffff\1\2",
            "\1\24\1\25\1\26\1\27\1\30\1\31\1\32\2\uffff\1\23",
            "\1\33",
            "\1\21\15\uffff\1\20",
            "\1\21\15\uffff\1\20",
            "\1\21\15\uffff\1\20",
            "\1\21\15\uffff\1\20",
            "\1\21\15\uffff\1\20",
            "\1\21\15\uffff\1\20",
            "\1\21\15\uffff\1\20",
            "\1\24\1\25\1\26\1\27\1\30\1\31\1\32"
    };

    static final short[] dfa_1 = DFA.unpackEncodedString(dfa_1s);
    static final char[] dfa_2 = DFA.unpackEncodedStringToUnsignedChars(dfa_2s);
    static final char[] dfa_3 = DFA.unpackEncodedStringToUnsignedChars(dfa_3s);
    static final short[] dfa_4 = DFA.unpackEncodedString(dfa_4s);
    static final short[] dfa_5 = DFA.unpackEncodedString(dfa_5s);
    static final short[][] dfa_6 = unpackEncodedStringArray(dfa_6s);

    class DFA26 extends DFA {

        public DFA26(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 26;
            this.eot = dfa_1;
            this.eof = dfa_1;
            this.min = dfa_2;
            this.max = dfa_3;
            this.accept = dfa_4;
            this.special = dfa_5;
            this.transition = dfa_6;
        }
        public String getDescription() {
            return "1101:3: ( ( (lv_channelformat_1_0= ruleChannelFormat ) ) | ( (lv_accessframework_2_0= ruleAccessFramework ) ) )";
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000801C02000002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000001400002L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x000C000000000000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x000000001C000010L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000018000010L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000030000010L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000000002010L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x00000002D0002012L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x00000002D0000002L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x00000000000020F0L,0x0000000000000038L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000280000002L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000210000012L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000210000002L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000203000000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x00000000003F8010L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x00000000103FC000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x00000000003F8000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000010004000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000000010004020L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000000000005000L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000000002000010L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0000003000000000L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0030000000000000L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0050000000000000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0C00000000000002L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0xF000000000000002L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000003L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0000000008000002L,0x0000000000000004L});

}
