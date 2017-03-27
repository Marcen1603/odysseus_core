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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "INTERSECTION", "DATAHANDLER", "DIFFERENCE", "IDENTIFIED", "PARTITION", "TRANSPORT", "UNBOUNDED", "DISTINCT", "PROTOCOL", "ADVANCE", "CHANNEL", "OPTIONS", "WRAPPER", "ATTACH", "CREATE", "EXISTS", "HAVING", "REVOKE", "SELECT", "STREAM", "TENANT", "ALTER", "FALSE", "GRANT", "GROUP", "TUPLE", "UNION", "WHERE", "DROP", "FILE", "FROM", "ROLE", "SINK", "SIZE", "TIME", "TRUE", "USER", "VIEW", "AND", "NOT", "ExclamationMarkEqualsSign", "LessThanSignEqualsSign", "GreaterThanSignEqualsSign", "AS", "BY", "IF", "IN", "ON", "OR", "TO", "LeftParenthesis", "RightParenthesis", "Asterisk", "PlusSign", "Comma", "HyphenMinus", "FullStop", "Solidus", "Colon", "Semicolon", "LessThanSign", "EqualsSign", "GreaterThanSign", "LeftSquareBracket", "RightSquareBracket", "RULE_ID", "RULE_INT", "RULE_FLOAT", "RULE_STRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER"
    };
    public static final int CREATE=18;
    public static final int FROM=34;
    public static final int VIEW=41;
    public static final int LessThanSign=64;
    public static final int LeftParenthesis=54;
    public static final int UNBOUNDED=10;
    public static final int WRAPPER=16;
    public static final int PARTITION=8;
    public static final int IF=49;
    public static final int RightSquareBracket=68;
    public static final int GreaterThanSign=66;
    public static final int RULE_ID=69;
    public static final int IN=50;
    public static final int DISTINCT=11;
    public static final int SIZE=37;
    public static final int PROTOCOL=12;
    public static final int RightParenthesis=55;
    public static final int TRUE=39;
    public static final int OPTIONS=15;
    public static final int WHERE=31;
    public static final int GreaterThanSignEqualsSign=46;
    public static final int NOT=43;
    public static final int AS=47;
    public static final int INTERSECTION=4;
    public static final int CHANNEL=14;
    public static final int SINK=36;
    public static final int PlusSign=57;
    public static final int RULE_INT=70;
    public static final int AND=42;
    public static final int RULE_ML_COMMENT=73;
    public static final int LeftSquareBracket=67;
    public static final int ADVANCE=13;
    public static final int HAVING=20;
    public static final int ALTER=25;
    public static final int RULE_STRING=72;
    public static final int DROP=32;
    public static final int RULE_SL_COMMENT=74;
    public static final int ROLE=35;
    public static final int GROUP=28;
    public static final int Comma=58;
    public static final int EqualsSign=65;
    public static final int TRANSPORT=9;
    public static final int HyphenMinus=59;
    public static final int DIFFERENCE=6;
    public static final int BY=48;
    public static final int LessThanSignEqualsSign=45;
    public static final int Solidus=61;
    public static final int Colon=62;
    public static final int DATAHANDLER=5;
    public static final int FILE=33;
    public static final int EOF=-1;
    public static final int Asterisk=56;
    public static final int ON=51;
    public static final int FullStop=60;
    public static final int OR=52;
    public static final int EXISTS=19;
    public static final int REVOKE=21;
    public static final int RULE_WS=75;
    public static final int STREAM=23;
    public static final int IDENTIFIED=7;
    public static final int TIME=38;
    public static final int RULE_ANY_OTHER=76;
    public static final int USER=40;
    public static final int TENANT=24;
    public static final int SELECT=22;
    public static final int TUPLE=29;
    public static final int Semicolon=63;
    public static final int GRANT=27;
    public static final int ATTACH=17;
    public static final int RULE_FLOAT=71;
    public static final int FALSE=26;
    public static final int ExclamationMarkEqualsSign=44;
    public static final int TO=53;
    public static final int UNION=30;

    // delegates
    // delegators


        public InternalCQLParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalCQLParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalCQLParser.tokenNames; }
    public String getGrammarFileName() { return "InternalCQLParser.g"; }



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
    // InternalCQLParser.g:58:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // InternalCQLParser.g:58:46: (iv_ruleModel= ruleModel EOF )
            // InternalCQLParser.g:59:2: iv_ruleModel= ruleModel EOF
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
    // InternalCQLParser.g:65:1: ruleModel returns [EObject current=null] : ( (lv_statements_0_0= ruleStatement ) )* ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        EObject lv_statements_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:71:2: ( ( (lv_statements_0_0= ruleStatement ) )* )
            // InternalCQLParser.g:72:2: ( (lv_statements_0_0= ruleStatement ) )*
            {
            // InternalCQLParser.g:72:2: ( (lv_statements_0_0= ruleStatement ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=ATTACH && LA1_0<=CREATE)||(LA1_0>=REVOKE && LA1_0<=STREAM)||LA1_0==ALTER||LA1_0==GRANT||LA1_0==DROP||LA1_0==VIEW) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalCQLParser.g:73:3: (lv_statements_0_0= ruleStatement )
            	    {
            	    // InternalCQLParser.g:73:3: (lv_statements_0_0= ruleStatement )
            	    // InternalCQLParser.g:74:4: lv_statements_0_0= ruleStatement
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
    // InternalCQLParser.g:94:1: entryRuleStatement returns [EObject current=null] : iv_ruleStatement= ruleStatement EOF ;
    public final EObject entryRuleStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStatement = null;


        try {
            // InternalCQLParser.g:94:50: (iv_ruleStatement= ruleStatement EOF )
            // InternalCQLParser.g:95:2: iv_ruleStatement= ruleStatement EOF
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
    // InternalCQLParser.g:101:1: ruleStatement returns [EObject current=null] : ( () ( ( (lv_type_1_0= ruleSelect ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDropCommand ) ) | ( (lv_type_4_0= ruleUserCommand ) ) | ( (lv_type_5_0= ruleRightsCommand ) ) | ( (lv_type_6_0= ruleRightsRoleCommand ) ) | ( (lv_type_7_0= ruleCreateStream1 ) ) | ( (lv_type_8_0= ruleCreateSink1 ) ) | ( (lv_type_9_0= ruleCreateStreamChannel ) ) | ( (lv_type_10_0= ruleCreateStreamFile ) ) | ( (lv_type_11_0= ruleCreateView ) ) ) (otherlv_12= Semicolon )? ) ;
    public final EObject ruleStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_12=null;
        EObject lv_type_1_0 = null;

        EObject lv_type_2_0 = null;

        EObject lv_type_3_0 = null;

        EObject lv_type_4_0 = null;

        EObject lv_type_5_0 = null;

        EObject lv_type_6_0 = null;

        EObject lv_type_7_0 = null;

        EObject lv_type_8_0 = null;

        EObject lv_type_9_0 = null;

        EObject lv_type_10_0 = null;

        EObject lv_type_11_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:107:2: ( ( () ( ( (lv_type_1_0= ruleSelect ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDropCommand ) ) | ( (lv_type_4_0= ruleUserCommand ) ) | ( (lv_type_5_0= ruleRightsCommand ) ) | ( (lv_type_6_0= ruleRightsRoleCommand ) ) | ( (lv_type_7_0= ruleCreateStream1 ) ) | ( (lv_type_8_0= ruleCreateSink1 ) ) | ( (lv_type_9_0= ruleCreateStreamChannel ) ) | ( (lv_type_10_0= ruleCreateStreamFile ) ) | ( (lv_type_11_0= ruleCreateView ) ) ) (otherlv_12= Semicolon )? ) )
            // InternalCQLParser.g:108:2: ( () ( ( (lv_type_1_0= ruleSelect ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDropCommand ) ) | ( (lv_type_4_0= ruleUserCommand ) ) | ( (lv_type_5_0= ruleRightsCommand ) ) | ( (lv_type_6_0= ruleRightsRoleCommand ) ) | ( (lv_type_7_0= ruleCreateStream1 ) ) | ( (lv_type_8_0= ruleCreateSink1 ) ) | ( (lv_type_9_0= ruleCreateStreamChannel ) ) | ( (lv_type_10_0= ruleCreateStreamFile ) ) | ( (lv_type_11_0= ruleCreateView ) ) ) (otherlv_12= Semicolon )? )
            {
            // InternalCQLParser.g:108:2: ( () ( ( (lv_type_1_0= ruleSelect ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDropCommand ) ) | ( (lv_type_4_0= ruleUserCommand ) ) | ( (lv_type_5_0= ruleRightsCommand ) ) | ( (lv_type_6_0= ruleRightsRoleCommand ) ) | ( (lv_type_7_0= ruleCreateStream1 ) ) | ( (lv_type_8_0= ruleCreateSink1 ) ) | ( (lv_type_9_0= ruleCreateStreamChannel ) ) | ( (lv_type_10_0= ruleCreateStreamFile ) ) | ( (lv_type_11_0= ruleCreateView ) ) ) (otherlv_12= Semicolon )? )
            // InternalCQLParser.g:109:3: () ( ( (lv_type_1_0= ruleSelect ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDropCommand ) ) | ( (lv_type_4_0= ruleUserCommand ) ) | ( (lv_type_5_0= ruleRightsCommand ) ) | ( (lv_type_6_0= ruleRightsRoleCommand ) ) | ( (lv_type_7_0= ruleCreateStream1 ) ) | ( (lv_type_8_0= ruleCreateSink1 ) ) | ( (lv_type_9_0= ruleCreateStreamChannel ) ) | ( (lv_type_10_0= ruleCreateStreamFile ) ) | ( (lv_type_11_0= ruleCreateView ) ) ) (otherlv_12= Semicolon )?
            {
            // InternalCQLParser.g:109:3: ()
            // InternalCQLParser.g:110:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getStatementAccess().getStatementAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:116:3: ( ( (lv_type_1_0= ruleSelect ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDropCommand ) ) | ( (lv_type_4_0= ruleUserCommand ) ) | ( (lv_type_5_0= ruleRightsCommand ) ) | ( (lv_type_6_0= ruleRightsRoleCommand ) ) | ( (lv_type_7_0= ruleCreateStream1 ) ) | ( (lv_type_8_0= ruleCreateSink1 ) ) | ( (lv_type_9_0= ruleCreateStreamChannel ) ) | ( (lv_type_10_0= ruleCreateStreamFile ) ) | ( (lv_type_11_0= ruleCreateView ) ) )
            int alt2=11;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // InternalCQLParser.g:117:4: ( (lv_type_1_0= ruleSelect ) )
                    {
                    // InternalCQLParser.g:117:4: ( (lv_type_1_0= ruleSelect ) )
                    // InternalCQLParser.g:118:5: (lv_type_1_0= ruleSelect )
                    {
                    // InternalCQLParser.g:118:5: (lv_type_1_0= ruleSelect )
                    // InternalCQLParser.g:119:6: lv_type_1_0= ruleSelect
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeSelectParserRuleCall_1_0_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_1_0=ruleSelect();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_1_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Select");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:137:4: ( (lv_type_2_0= ruleStreamTo ) )
                    {
                    // InternalCQLParser.g:137:4: ( (lv_type_2_0= ruleStreamTo ) )
                    // InternalCQLParser.g:138:5: (lv_type_2_0= ruleStreamTo )
                    {
                    // InternalCQLParser.g:138:5: (lv_type_2_0= ruleStreamTo )
                    // InternalCQLParser.g:139:6: lv_type_2_0= ruleStreamTo
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeStreamToParserRuleCall_1_1_0());
                    					
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
                case 3 :
                    // InternalCQLParser.g:157:4: ( (lv_type_3_0= ruleDropCommand ) )
                    {
                    // InternalCQLParser.g:157:4: ( (lv_type_3_0= ruleDropCommand ) )
                    // InternalCQLParser.g:158:5: (lv_type_3_0= ruleDropCommand )
                    {
                    // InternalCQLParser.g:158:5: (lv_type_3_0= ruleDropCommand )
                    // InternalCQLParser.g:159:6: lv_type_3_0= ruleDropCommand
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeDropCommandParserRuleCall_1_2_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_3_0=ruleDropCommand();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.DropCommand");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:177:4: ( (lv_type_4_0= ruleUserCommand ) )
                    {
                    // InternalCQLParser.g:177:4: ( (lv_type_4_0= ruleUserCommand ) )
                    // InternalCQLParser.g:178:5: (lv_type_4_0= ruleUserCommand )
                    {
                    // InternalCQLParser.g:178:5: (lv_type_4_0= ruleUserCommand )
                    // InternalCQLParser.g:179:6: lv_type_4_0= ruleUserCommand
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeUserCommandParserRuleCall_1_3_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_4_0=ruleUserCommand();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_4_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.UserCommand");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 5 :
                    // InternalCQLParser.g:197:4: ( (lv_type_5_0= ruleRightsCommand ) )
                    {
                    // InternalCQLParser.g:197:4: ( (lv_type_5_0= ruleRightsCommand ) )
                    // InternalCQLParser.g:198:5: (lv_type_5_0= ruleRightsCommand )
                    {
                    // InternalCQLParser.g:198:5: (lv_type_5_0= ruleRightsCommand )
                    // InternalCQLParser.g:199:6: lv_type_5_0= ruleRightsCommand
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeRightsCommandParserRuleCall_1_4_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_5_0=ruleRightsCommand();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_5_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.RightsCommand");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 6 :
                    // InternalCQLParser.g:217:4: ( (lv_type_6_0= ruleRightsRoleCommand ) )
                    {
                    // InternalCQLParser.g:217:4: ( (lv_type_6_0= ruleRightsRoleCommand ) )
                    // InternalCQLParser.g:218:5: (lv_type_6_0= ruleRightsRoleCommand )
                    {
                    // InternalCQLParser.g:218:5: (lv_type_6_0= ruleRightsRoleCommand )
                    // InternalCQLParser.g:219:6: lv_type_6_0= ruleRightsRoleCommand
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeRightsRoleCommandParserRuleCall_1_5_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_6_0=ruleRightsRoleCommand();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_6_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.RightsRoleCommand");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 7 :
                    // InternalCQLParser.g:237:4: ( (lv_type_7_0= ruleCreateStream1 ) )
                    {
                    // InternalCQLParser.g:237:4: ( (lv_type_7_0= ruleCreateStream1 ) )
                    // InternalCQLParser.g:238:5: (lv_type_7_0= ruleCreateStream1 )
                    {
                    // InternalCQLParser.g:238:5: (lv_type_7_0= ruleCreateStream1 )
                    // InternalCQLParser.g:239:6: lv_type_7_0= ruleCreateStream1
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeCreateStream1ParserRuleCall_1_6_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_7_0=ruleCreateStream1();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_7_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateStream1");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 8 :
                    // InternalCQLParser.g:257:4: ( (lv_type_8_0= ruleCreateSink1 ) )
                    {
                    // InternalCQLParser.g:257:4: ( (lv_type_8_0= ruleCreateSink1 ) )
                    // InternalCQLParser.g:258:5: (lv_type_8_0= ruleCreateSink1 )
                    {
                    // InternalCQLParser.g:258:5: (lv_type_8_0= ruleCreateSink1 )
                    // InternalCQLParser.g:259:6: lv_type_8_0= ruleCreateSink1
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeCreateSink1ParserRuleCall_1_7_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_8_0=ruleCreateSink1();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_8_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateSink1");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 9 :
                    // InternalCQLParser.g:277:4: ( (lv_type_9_0= ruleCreateStreamChannel ) )
                    {
                    // InternalCQLParser.g:277:4: ( (lv_type_9_0= ruleCreateStreamChannel ) )
                    // InternalCQLParser.g:278:5: (lv_type_9_0= ruleCreateStreamChannel )
                    {
                    // InternalCQLParser.g:278:5: (lv_type_9_0= ruleCreateStreamChannel )
                    // InternalCQLParser.g:279:6: lv_type_9_0= ruleCreateStreamChannel
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeCreateStreamChannelParserRuleCall_1_8_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_9_0=ruleCreateStreamChannel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_9_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateStreamChannel");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 10 :
                    // InternalCQLParser.g:297:4: ( (lv_type_10_0= ruleCreateStreamFile ) )
                    {
                    // InternalCQLParser.g:297:4: ( (lv_type_10_0= ruleCreateStreamFile ) )
                    // InternalCQLParser.g:298:5: (lv_type_10_0= ruleCreateStreamFile )
                    {
                    // InternalCQLParser.g:298:5: (lv_type_10_0= ruleCreateStreamFile )
                    // InternalCQLParser.g:299:6: lv_type_10_0= ruleCreateStreamFile
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeCreateStreamFileParserRuleCall_1_9_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_10_0=ruleCreateStreamFile();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_10_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateStreamFile");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 11 :
                    // InternalCQLParser.g:317:4: ( (lv_type_11_0= ruleCreateView ) )
                    {
                    // InternalCQLParser.g:317:4: ( (lv_type_11_0= ruleCreateView ) )
                    // InternalCQLParser.g:318:5: (lv_type_11_0= ruleCreateView )
                    {
                    // InternalCQLParser.g:318:5: (lv_type_11_0= ruleCreateView )
                    // InternalCQLParser.g:319:6: lv_type_11_0= ruleCreateView
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeCreateViewParserRuleCall_1_10_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_11_0=ruleCreateView();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_11_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateView");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:337:3: (otherlv_12= Semicolon )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==Semicolon) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalCQLParser.g:338:4: otherlv_12= Semicolon
                    {
                    otherlv_12=(Token)match(input,Semicolon,FOLLOW_2); 

                    				newLeafNode(otherlv_12, grammarAccess.getStatementAccess().getSemicolonKeyword_2());
                    			

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


    // $ANTLR start "entryRuleSelect"
    // InternalCQLParser.g:347:1: entryRuleSelect returns [EObject current=null] : iv_ruleSelect= ruleSelect EOF ;
    public final EObject entryRuleSelect() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelect = null;


        try {
            // InternalCQLParser.g:347:47: (iv_ruleSelect= ruleSelect EOF )
            // InternalCQLParser.g:348:2: iv_ruleSelect= ruleSelect EOF
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
    // InternalCQLParser.g:354:1: ruleSelect returns [EObject current=null] : ( ( (lv_name_0_0= SELECT ) ) ( (lv_distinct_1_0= DISTINCT ) )? (otherlv_2= Asterisk | ( ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )* ) ) (otherlv_6= FROM ( (lv_sources_7_0= ruleSource ) )+ (otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) ) )* ) (otherlv_10= WHERE ( (lv_predicates_11_0= ruleExpressionsModel ) ) )? (otherlv_12= GROUP otherlv_13= BY ( (lv_order_14_0= ruleAttribute ) )+ (otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) ) )* )? (otherlv_17= HAVING ( (lv_having_18_0= ruleExpressionsModel ) ) )? ) ;
    public final EObject ruleSelect() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token lv_distinct_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        Token otherlv_10=null;
        Token otherlv_12=null;
        Token otherlv_13=null;
        Token otherlv_15=null;
        Token otherlv_17=null;
        EObject lv_arguments_3_0 = null;

        EObject lv_arguments_5_0 = null;

        EObject lv_sources_7_0 = null;

        EObject lv_sources_9_0 = null;

        EObject lv_predicates_11_0 = null;

        EObject lv_order_14_0 = null;

        EObject lv_order_16_0 = null;

        EObject lv_having_18_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:360:2: ( ( ( (lv_name_0_0= SELECT ) ) ( (lv_distinct_1_0= DISTINCT ) )? (otherlv_2= Asterisk | ( ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )* ) ) (otherlv_6= FROM ( (lv_sources_7_0= ruleSource ) )+ (otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) ) )* ) (otherlv_10= WHERE ( (lv_predicates_11_0= ruleExpressionsModel ) ) )? (otherlv_12= GROUP otherlv_13= BY ( (lv_order_14_0= ruleAttribute ) )+ (otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) ) )* )? (otherlv_17= HAVING ( (lv_having_18_0= ruleExpressionsModel ) ) )? ) )
            // InternalCQLParser.g:361:2: ( ( (lv_name_0_0= SELECT ) ) ( (lv_distinct_1_0= DISTINCT ) )? (otherlv_2= Asterisk | ( ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )* ) ) (otherlv_6= FROM ( (lv_sources_7_0= ruleSource ) )+ (otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) ) )* ) (otherlv_10= WHERE ( (lv_predicates_11_0= ruleExpressionsModel ) ) )? (otherlv_12= GROUP otherlv_13= BY ( (lv_order_14_0= ruleAttribute ) )+ (otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) ) )* )? (otherlv_17= HAVING ( (lv_having_18_0= ruleExpressionsModel ) ) )? )
            {
            // InternalCQLParser.g:361:2: ( ( (lv_name_0_0= SELECT ) ) ( (lv_distinct_1_0= DISTINCT ) )? (otherlv_2= Asterisk | ( ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )* ) ) (otherlv_6= FROM ( (lv_sources_7_0= ruleSource ) )+ (otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) ) )* ) (otherlv_10= WHERE ( (lv_predicates_11_0= ruleExpressionsModel ) ) )? (otherlv_12= GROUP otherlv_13= BY ( (lv_order_14_0= ruleAttribute ) )+ (otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) ) )* )? (otherlv_17= HAVING ( (lv_having_18_0= ruleExpressionsModel ) ) )? )
            // InternalCQLParser.g:362:3: ( (lv_name_0_0= SELECT ) ) ( (lv_distinct_1_0= DISTINCT ) )? (otherlv_2= Asterisk | ( ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )* ) ) (otherlv_6= FROM ( (lv_sources_7_0= ruleSource ) )+ (otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) ) )* ) (otherlv_10= WHERE ( (lv_predicates_11_0= ruleExpressionsModel ) ) )? (otherlv_12= GROUP otherlv_13= BY ( (lv_order_14_0= ruleAttribute ) )+ (otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) ) )* )? (otherlv_17= HAVING ( (lv_having_18_0= ruleExpressionsModel ) ) )?
            {
            // InternalCQLParser.g:362:3: ( (lv_name_0_0= SELECT ) )
            // InternalCQLParser.g:363:4: (lv_name_0_0= SELECT )
            {
            // InternalCQLParser.g:363:4: (lv_name_0_0= SELECT )
            // InternalCQLParser.g:364:5: lv_name_0_0= SELECT
            {
            lv_name_0_0=(Token)match(input,SELECT,FOLLOW_5); 

            					newLeafNode(lv_name_0_0, grammarAccess.getSelectAccess().getNameSELECTKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSelectRule());
            					}
            					setWithLastConsumed(current, "name", lv_name_0_0, "SELECT");
            				

            }


            }

            // InternalCQLParser.g:376:3: ( (lv_distinct_1_0= DISTINCT ) )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==DISTINCT) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // InternalCQLParser.g:377:4: (lv_distinct_1_0= DISTINCT )
                    {
                    // InternalCQLParser.g:377:4: (lv_distinct_1_0= DISTINCT )
                    // InternalCQLParser.g:378:5: lv_distinct_1_0= DISTINCT
                    {
                    lv_distinct_1_0=(Token)match(input,DISTINCT,FOLLOW_5); 

                    					newLeafNode(lv_distinct_1_0, grammarAccess.getSelectAccess().getDistinctDISTINCTKeyword_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getSelectRule());
                    					}
                    					setWithLastConsumed(current, "distinct", lv_distinct_1_0, "DISTINCT");
                    				

                    }


                    }
                    break;

            }

            // InternalCQLParser.g:390:3: (otherlv_2= Asterisk | ( ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )* ) )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==Asterisk) ) {
                alt7=1;
            }
            else if ( (LA7_0==FALSE||LA7_0==TRUE||(LA7_0>=RULE_ID && LA7_0<=RULE_STRING)) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // InternalCQLParser.g:391:4: otherlv_2= Asterisk
                    {
                    otherlv_2=(Token)match(input,Asterisk,FOLLOW_6); 

                    				newLeafNode(otherlv_2, grammarAccess.getSelectAccess().getAsteriskKeyword_2_0());
                    			

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:396:4: ( ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )* )
                    {
                    // InternalCQLParser.g:396:4: ( ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )* )
                    // InternalCQLParser.g:397:5: ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )*
                    {
                    // InternalCQLParser.g:397:5: ( (lv_arguments_3_0= ruleArgument ) )+
                    int cnt5=0;
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==FALSE||LA5_0==TRUE||(LA5_0>=RULE_ID && LA5_0<=RULE_STRING)) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // InternalCQLParser.g:398:6: (lv_arguments_3_0= ruleArgument )
                    	    {
                    	    // InternalCQLParser.g:398:6: (lv_arguments_3_0= ruleArgument )
                    	    // InternalCQLParser.g:399:7: lv_arguments_3_0= ruleArgument
                    	    {

                    	    							newCompositeNode(grammarAccess.getSelectAccess().getArgumentsArgumentParserRuleCall_2_1_0_0());
                    	    						
                    	    pushFollow(FOLLOW_7);
                    	    lv_arguments_3_0=ruleArgument();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"arguments",
                    	    								lv_arguments_3_0,
                    	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Argument");
                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt5 >= 1 ) break loop5;
                                EarlyExitException eee =
                                    new EarlyExitException(5, input);
                                throw eee;
                        }
                        cnt5++;
                    } while (true);

                    // InternalCQLParser.g:416:5: (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==Comma) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // InternalCQLParser.g:417:6: otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) )
                    	    {
                    	    otherlv_4=(Token)match(input,Comma,FOLLOW_5); 

                    	    						newLeafNode(otherlv_4, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_0());
                    	    					
                    	    // InternalCQLParser.g:421:6: ( (lv_arguments_5_0= ruleArgument ) )
                    	    // InternalCQLParser.g:422:7: (lv_arguments_5_0= ruleArgument )
                    	    {
                    	    // InternalCQLParser.g:422:7: (lv_arguments_5_0= ruleArgument )
                    	    // InternalCQLParser.g:423:8: lv_arguments_5_0= ruleArgument
                    	    {

                    	    								newCompositeNode(grammarAccess.getSelectAccess().getArgumentsArgumentParserRuleCall_2_1_1_1_0());
                    	    							
                    	    pushFollow(FOLLOW_8);
                    	    lv_arguments_5_0=ruleArgument();

                    	    state._fsp--;


                    	    								if (current==null) {
                    	    									current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    								}
                    	    								add(
                    	    									current,
                    	    									"arguments",
                    	    									lv_arguments_5_0,
                    	    									"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Argument");
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
                    break;

            }

            // InternalCQLParser.g:443:3: (otherlv_6= FROM ( (lv_sources_7_0= ruleSource ) )+ (otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) ) )* )
            // InternalCQLParser.g:444:4: otherlv_6= FROM ( (lv_sources_7_0= ruleSource ) )+ (otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) ) )*
            {
            otherlv_6=(Token)match(input,FROM,FOLLOW_9); 

            				newLeafNode(otherlv_6, grammarAccess.getSelectAccess().getFROMKeyword_3_0());
            			
            // InternalCQLParser.g:448:4: ( (lv_sources_7_0= ruleSource ) )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==LeftParenthesis||LA8_0==RULE_ID) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // InternalCQLParser.g:449:5: (lv_sources_7_0= ruleSource )
            	    {
            	    // InternalCQLParser.g:449:5: (lv_sources_7_0= ruleSource )
            	    // InternalCQLParser.g:450:6: lv_sources_7_0= ruleSource
            	    {

            	    						newCompositeNode(grammarAccess.getSelectAccess().getSourcesSourceParserRuleCall_3_1_0());
            	    					
            	    pushFollow(FOLLOW_10);
            	    lv_sources_7_0=ruleSource();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getSelectRule());
            	    						}
            	    						add(
            	    							current,
            	    							"sources",
            	    							lv_sources_7_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Source");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);

            // InternalCQLParser.g:467:4: (otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) ) )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==Comma) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // InternalCQLParser.g:468:5: otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) )
            	    {
            	    otherlv_8=(Token)match(input,Comma,FOLLOW_9); 

            	    					newLeafNode(otherlv_8, grammarAccess.getSelectAccess().getCommaKeyword_3_2_0());
            	    				
            	    // InternalCQLParser.g:472:5: ( (lv_sources_9_0= ruleSource ) )
            	    // InternalCQLParser.g:473:6: (lv_sources_9_0= ruleSource )
            	    {
            	    // InternalCQLParser.g:473:6: (lv_sources_9_0= ruleSource )
            	    // InternalCQLParser.g:474:7: lv_sources_9_0= ruleSource
            	    {

            	    							newCompositeNode(grammarAccess.getSelectAccess().getSourcesSourceParserRuleCall_3_2_1_0());
            	    						
            	    pushFollow(FOLLOW_11);
            	    lv_sources_9_0=ruleSource();

            	    state._fsp--;


            	    							if (current==null) {
            	    								current = createModelElementForParent(grammarAccess.getSelectRule());
            	    							}
            	    							add(
            	    								current,
            	    								"sources",
            	    								lv_sources_9_0,
            	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Source");
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

            // InternalCQLParser.g:493:3: (otherlv_10= WHERE ( (lv_predicates_11_0= ruleExpressionsModel ) ) )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==WHERE) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // InternalCQLParser.g:494:4: otherlv_10= WHERE ( (lv_predicates_11_0= ruleExpressionsModel ) )
                    {
                    otherlv_10=(Token)match(input,WHERE,FOLLOW_12); 

                    				newLeafNode(otherlv_10, grammarAccess.getSelectAccess().getWHEREKeyword_4_0());
                    			
                    // InternalCQLParser.g:498:4: ( (lv_predicates_11_0= ruleExpressionsModel ) )
                    // InternalCQLParser.g:499:5: (lv_predicates_11_0= ruleExpressionsModel )
                    {
                    // InternalCQLParser.g:499:5: (lv_predicates_11_0= ruleExpressionsModel )
                    // InternalCQLParser.g:500:6: lv_predicates_11_0= ruleExpressionsModel
                    {

                    						newCompositeNode(grammarAccess.getSelectAccess().getPredicatesExpressionsModelParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_13);
                    lv_predicates_11_0=ruleExpressionsModel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    						}
                    						set(
                    							current,
                    							"predicates",
                    							lv_predicates_11_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionsModel");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:518:3: (otherlv_12= GROUP otherlv_13= BY ( (lv_order_14_0= ruleAttribute ) )+ (otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) ) )* )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==GROUP) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // InternalCQLParser.g:519:4: otherlv_12= GROUP otherlv_13= BY ( (lv_order_14_0= ruleAttribute ) )+ (otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) ) )*
                    {
                    otherlv_12=(Token)match(input,GROUP,FOLLOW_14); 

                    				newLeafNode(otherlv_12, grammarAccess.getSelectAccess().getGROUPKeyword_5_0());
                    			
                    otherlv_13=(Token)match(input,BY,FOLLOW_15); 

                    				newLeafNode(otherlv_13, grammarAccess.getSelectAccess().getBYKeyword_5_1());
                    			
                    // InternalCQLParser.g:527:4: ( (lv_order_14_0= ruleAttribute ) )+
                    int cnt11=0;
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0==RULE_ID) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // InternalCQLParser.g:528:5: (lv_order_14_0= ruleAttribute )
                    	    {
                    	    // InternalCQLParser.g:528:5: (lv_order_14_0= ruleAttribute )
                    	    // InternalCQLParser.g:529:6: lv_order_14_0= ruleAttribute
                    	    {

                    	    						newCompositeNode(grammarAccess.getSelectAccess().getOrderAttributeParserRuleCall_5_2_0());
                    	    					
                    	    pushFollow(FOLLOW_16);
                    	    lv_order_14_0=ruleAttribute();

                    	    state._fsp--;


                    	    						if (current==null) {
                    	    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    						}
                    	    						add(
                    	    							current,
                    	    							"order",
                    	    							lv_order_14_0,
                    	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
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

                    // InternalCQLParser.g:546:4: (otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) ) )*
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0==Comma) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // InternalCQLParser.g:547:5: otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) )
                    	    {
                    	    otherlv_15=(Token)match(input,Comma,FOLLOW_15); 

                    	    					newLeafNode(otherlv_15, grammarAccess.getSelectAccess().getCommaKeyword_5_3_0());
                    	    				
                    	    // InternalCQLParser.g:551:5: ( (lv_order_16_0= ruleAttribute ) )
                    	    // InternalCQLParser.g:552:6: (lv_order_16_0= ruleAttribute )
                    	    {
                    	    // InternalCQLParser.g:552:6: (lv_order_16_0= ruleAttribute )
                    	    // InternalCQLParser.g:553:7: lv_order_16_0= ruleAttribute
                    	    {

                    	    							newCompositeNode(grammarAccess.getSelectAccess().getOrderAttributeParserRuleCall_5_3_1_0());
                    	    						
                    	    pushFollow(FOLLOW_17);
                    	    lv_order_16_0=ruleAttribute();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"order",
                    	    								lv_order_16_0,
                    	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
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
                    break;

            }

            // InternalCQLParser.g:572:3: (otherlv_17= HAVING ( (lv_having_18_0= ruleExpressionsModel ) ) )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==HAVING) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // InternalCQLParser.g:573:4: otherlv_17= HAVING ( (lv_having_18_0= ruleExpressionsModel ) )
                    {
                    otherlv_17=(Token)match(input,HAVING,FOLLOW_12); 

                    				newLeafNode(otherlv_17, grammarAccess.getSelectAccess().getHAVINGKeyword_6_0());
                    			
                    // InternalCQLParser.g:577:4: ( (lv_having_18_0= ruleExpressionsModel ) )
                    // InternalCQLParser.g:578:5: (lv_having_18_0= ruleExpressionsModel )
                    {
                    // InternalCQLParser.g:578:5: (lv_having_18_0= ruleExpressionsModel )
                    // InternalCQLParser.g:579:6: lv_having_18_0= ruleExpressionsModel
                    {

                    						newCompositeNode(grammarAccess.getSelectAccess().getHavingExpressionsModelParserRuleCall_6_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_having_18_0=ruleExpressionsModel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    						}
                    						set(
                    							current,
                    							"having",
                    							lv_having_18_0,
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


    // $ANTLR start "entryRuleNestedStatement"
    // InternalCQLParser.g:601:1: entryRuleNestedStatement returns [EObject current=null] : iv_ruleNestedStatement= ruleNestedStatement EOF ;
    public final EObject entryRuleNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNestedStatement = null;


        try {
            // InternalCQLParser.g:601:56: (iv_ruleNestedStatement= ruleNestedStatement EOF )
            // InternalCQLParser.g:602:2: iv_ruleNestedStatement= ruleNestedStatement EOF
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
    // InternalCQLParser.g:608:1: ruleNestedStatement returns [EObject current=null] : (otherlv_0= LeftParenthesis this_Select_1= ruleSelect otherlv_2= RightParenthesis ) ;
    public final EObject ruleNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject this_Select_1 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:614:2: ( (otherlv_0= LeftParenthesis this_Select_1= ruleSelect otherlv_2= RightParenthesis ) )
            // InternalCQLParser.g:615:2: (otherlv_0= LeftParenthesis this_Select_1= ruleSelect otherlv_2= RightParenthesis )
            {
            // InternalCQLParser.g:615:2: (otherlv_0= LeftParenthesis this_Select_1= ruleSelect otherlv_2= RightParenthesis )
            // InternalCQLParser.g:616:3: otherlv_0= LeftParenthesis this_Select_1= ruleSelect otherlv_2= RightParenthesis
            {
            otherlv_0=(Token)match(input,LeftParenthesis,FOLLOW_18); 

            			newLeafNode(otherlv_0, grammarAccess.getNestedStatementAccess().getLeftParenthesisKeyword_0());
            		

            			newCompositeNode(grammarAccess.getNestedStatementAccess().getSelectParserRuleCall_1());
            		
            pushFollow(FOLLOW_19);
            this_Select_1=ruleSelect();

            state._fsp--;


            			current = this_Select_1;
            			afterParserOrEnumRuleCall();
            		
            otherlv_2=(Token)match(input,RightParenthesis,FOLLOW_2); 

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


    // $ANTLR start "entryRuleArgument"
    // InternalCQLParser.g:636:1: entryRuleArgument returns [EObject current=null] : iv_ruleArgument= ruleArgument EOF ;
    public final EObject entryRuleArgument() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleArgument = null;


        try {
            // InternalCQLParser.g:636:49: (iv_ruleArgument= ruleArgument EOF )
            // InternalCQLParser.g:637:2: iv_ruleArgument= ruleArgument EOF
            {
             newCompositeNode(grammarAccess.getArgumentRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleArgument=ruleArgument();

            state._fsp--;

             current =iv_ruleArgument; 
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
    // $ANTLR end "entryRuleArgument"


    // $ANTLR start "ruleArgument"
    // InternalCQLParser.g:643:1: ruleArgument returns [EObject current=null] : ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) ) ;
    public final EObject ruleArgument() throws RecognitionException {
        EObject current = null;

        EObject lv_attribute_0_0 = null;

        EObject lv_expression_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:649:2: ( ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) ) )
            // InternalCQLParser.g:650:2: ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) )
            {
            // InternalCQLParser.g:650:2: ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) )
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==RULE_ID) ) {
                switch ( input.LA(2) ) {
                case LeftParenthesis:
                case Asterisk:
                case PlusSign:
                case HyphenMinus:
                case Solidus:
                    {
                    alt15=2;
                    }
                    break;
                case FullStop:
                    {
                    int LA15_3 = input.LA(3);

                    if ( (LA15_3==RULE_ID) ) {
                        int LA15_5 = input.LA(4);

                        if ( ((LA15_5>=Asterisk && LA15_5<=PlusSign)||LA15_5==HyphenMinus||LA15_5==Solidus) ) {
                            alt15=2;
                        }
                        else if ( (LA15_5==EOF||LA15_5==FALSE||LA15_5==FROM||LA15_5==TRUE||LA15_5==AS||LA15_5==Comma||(LA15_5>=RULE_ID && LA15_5<=RULE_STRING)) ) {
                            alt15=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 15, 5, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA15_3==Asterisk) ) {
                        int LA15_6 = input.LA(4);

                        if ( (LA15_6==EOF||LA15_6==FALSE||LA15_6==FROM||LA15_6==TRUE||LA15_6==AS||LA15_6==Comma||(LA15_6>=RULE_ID && LA15_6<=RULE_STRING)) ) {
                            alt15=1;
                        }
                        else if ( ((LA15_6>=Asterisk && LA15_6<=PlusSign)||LA15_6==HyphenMinus||LA15_6==Solidus) ) {
                            alt15=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 15, 6, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 15, 3, input);

                        throw nvae;
                    }
                    }
                    break;
                case EOF:
                case FALSE:
                case FROM:
                case TRUE:
                case AS:
                case Comma:
                case RULE_ID:
                case RULE_INT:
                case RULE_FLOAT:
                case RULE_STRING:
                    {
                    alt15=1;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA15_0==FALSE||LA15_0==TRUE||(LA15_0>=RULE_INT && LA15_0<=RULE_STRING)) ) {
                alt15=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }
            switch (alt15) {
                case 1 :
                    // InternalCQLParser.g:651:3: ( (lv_attribute_0_0= ruleAttribute ) )
                    {
                    // InternalCQLParser.g:651:3: ( (lv_attribute_0_0= ruleAttribute ) )
                    // InternalCQLParser.g:652:4: (lv_attribute_0_0= ruleAttribute )
                    {
                    // InternalCQLParser.g:652:4: (lv_attribute_0_0= ruleAttribute )
                    // InternalCQLParser.g:653:5: lv_attribute_0_0= ruleAttribute
                    {

                    					newCompositeNode(grammarAccess.getArgumentAccess().getAttributeAttributeParserRuleCall_0_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_attribute_0_0=ruleAttribute();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getArgumentRule());
                    					}
                    					set(
                    						current,
                    						"attribute",
                    						lv_attribute_0_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:671:3: ( (lv_expression_1_0= ruleSelectExpression ) )
                    {
                    // InternalCQLParser.g:671:3: ( (lv_expression_1_0= ruleSelectExpression ) )
                    // InternalCQLParser.g:672:4: (lv_expression_1_0= ruleSelectExpression )
                    {
                    // InternalCQLParser.g:672:4: (lv_expression_1_0= ruleSelectExpression )
                    // InternalCQLParser.g:673:5: lv_expression_1_0= ruleSelectExpression
                    {

                    					newCompositeNode(grammarAccess.getArgumentAccess().getExpressionSelectExpressionParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_expression_1_0=ruleSelectExpression();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getArgumentRule());
                    					}
                    					set(
                    						current,
                    						"expression",
                    						lv_expression_1_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpression");
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
    // $ANTLR end "ruleArgument"


    // $ANTLR start "entryRuleSource"
    // InternalCQLParser.g:694:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalCQLParser.g:694:47: (iv_ruleSource= ruleSource EOF )
            // InternalCQLParser.g:695:2: iv_ruleSource= ruleSource EOF
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
    // InternalCQLParser.g:701:1: ruleSource returns [EObject current=null] : ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) ) ) ;
    public final EObject ruleSource() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token otherlv_9=null;
        AntlrDatatypeRuleToken lv_name_0_0 = null;

        AntlrDatatypeRuleToken lv_unbounded_2_0 = null;

        EObject lv_time_3_0 = null;

        EObject lv_tuple_4_0 = null;

        EObject lv_alias_7_0 = null;

        EObject lv_nested_8_0 = null;

        EObject lv_alias_10_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:707:2: ( ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) ) ) )
            // InternalCQLParser.g:708:2: ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) ) )
            {
            // InternalCQLParser.g:708:2: ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) ) )
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==RULE_ID) ) {
                alt19=1;
            }
            else if ( (LA19_0==LeftParenthesis) ) {
                alt19=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }
            switch (alt19) {
                case 1 :
                    // InternalCQLParser.g:709:3: ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
                    {
                    // InternalCQLParser.g:709:3: ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
                    // InternalCQLParser.g:710:4: ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
                    {
                    // InternalCQLParser.g:710:4: ( (lv_name_0_0= ruleSourceName ) )
                    // InternalCQLParser.g:711:5: (lv_name_0_0= ruleSourceName )
                    {
                    // InternalCQLParser.g:711:5: (lv_name_0_0= ruleSourceName )
                    // InternalCQLParser.g:712:6: lv_name_0_0= ruleSourceName
                    {

                    						newCompositeNode(grammarAccess.getSourceAccess().getNameSourceNameParserRuleCall_0_0_0());
                    					
                    pushFollow(FOLLOW_20);
                    lv_name_0_0=ruleSourceName();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSourceRule());
                    						}
                    						set(
                    							current,
                    							"name",
                    							lv_name_0_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SourceName");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalCQLParser.g:729:4: (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0==LeftSquareBracket) ) {
                        alt17=1;
                    }
                    switch (alt17) {
                        case 1 :
                            // InternalCQLParser.g:730:5: otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket
                            {
                            otherlv_1=(Token)match(input,LeftSquareBracket,FOLLOW_21); 

                            					newLeafNode(otherlv_1, grammarAccess.getSourceAccess().getLeftSquareBracketKeyword_0_1_0());
                            				
                            // InternalCQLParser.g:734:5: ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) )
                            int alt16=3;
                            int LA16_0 = input.LA(1);

                            if ( (LA16_0==UNBOUNDED) ) {
                                alt16=1;
                            }
                            else if ( (LA16_0==SIZE) ) {
                                int LA16_2 = input.LA(2);

                                if ( (LA16_2==RULE_INT) ) {
                                    int LA16_3 = input.LA(3);

                                    if ( (LA16_3==RULE_ID) ) {
                                        alt16=2;
                                    }
                                    else if ( (LA16_3==ADVANCE||LA16_3==TUPLE) ) {
                                        alt16=3;
                                    }
                                    else {
                                        NoViableAltException nvae =
                                            new NoViableAltException("", 16, 3, input);

                                        throw nvae;
                                    }
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 16, 2, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 16, 0, input);

                                throw nvae;
                            }
                            switch (alt16) {
                                case 1 :
                                    // InternalCQLParser.g:735:6: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    {
                                    // InternalCQLParser.g:735:6: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    // InternalCQLParser.g:736:7: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    {
                                    // InternalCQLParser.g:736:7: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    // InternalCQLParser.g:737:8: lv_unbounded_2_0= ruleWindow_Unbounded
                                    {

                                    								newCompositeNode(grammarAccess.getSourceAccess().getUnboundedWindow_UnboundedParserRuleCall_0_1_1_0_0());
                                    							
                                    pushFollow(FOLLOW_22);
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
                                    // InternalCQLParser.g:755:6: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    {
                                    // InternalCQLParser.g:755:6: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    // InternalCQLParser.g:756:7: (lv_time_3_0= ruleWindow_Timebased )
                                    {
                                    // InternalCQLParser.g:756:7: (lv_time_3_0= ruleWindow_Timebased )
                                    // InternalCQLParser.g:757:8: lv_time_3_0= ruleWindow_Timebased
                                    {

                                    								newCompositeNode(grammarAccess.getSourceAccess().getTimeWindow_TimebasedParserRuleCall_0_1_1_1_0());
                                    							
                                    pushFollow(FOLLOW_22);
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
                                    // InternalCQLParser.g:775:6: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    {
                                    // InternalCQLParser.g:775:6: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    // InternalCQLParser.g:776:7: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    {
                                    // InternalCQLParser.g:776:7: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    // InternalCQLParser.g:777:8: lv_tuple_4_0= ruleWindow_Tuplebased
                                    {

                                    								newCompositeNode(grammarAccess.getSourceAccess().getTupleWindow_TuplebasedParserRuleCall_0_1_1_2_0());
                                    							
                                    pushFollow(FOLLOW_22);
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

                            otherlv_5=(Token)match(input,RightSquareBracket,FOLLOW_23); 

                            					newLeafNode(otherlv_5, grammarAccess.getSourceAccess().getRightSquareBracketKeyword_0_1_2());
                            				

                            }
                            break;

                    }

                    // InternalCQLParser.g:800:4: (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==AS) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // InternalCQLParser.g:801:5: otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) )
                            {
                            otherlv_6=(Token)match(input,AS,FOLLOW_15); 

                            					newLeafNode(otherlv_6, grammarAccess.getSourceAccess().getASKeyword_0_2_0());
                            				
                            // InternalCQLParser.g:805:5: ( (lv_alias_7_0= ruleAlias ) )
                            // InternalCQLParser.g:806:6: (lv_alias_7_0= ruleAlias )
                            {
                            // InternalCQLParser.g:806:6: (lv_alias_7_0= ruleAlias )
                            // InternalCQLParser.g:807:7: lv_alias_7_0= ruleAlias
                            {

                            							newCompositeNode(grammarAccess.getSourceAccess().getAliasAliasParserRuleCall_0_2_1_0());
                            						
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
                    break;
                case 2 :
                    // InternalCQLParser.g:827:3: ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) )
                    {
                    // InternalCQLParser.g:827:3: ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) )
                    // InternalCQLParser.g:828:4: ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) )
                    {
                    // InternalCQLParser.g:828:4: ( (lv_nested_8_0= ruleNestedStatement ) )
                    // InternalCQLParser.g:829:5: (lv_nested_8_0= ruleNestedStatement )
                    {
                    // InternalCQLParser.g:829:5: (lv_nested_8_0= ruleNestedStatement )
                    // InternalCQLParser.g:830:6: lv_nested_8_0= ruleNestedStatement
                    {

                    						newCompositeNode(grammarAccess.getSourceAccess().getNestedNestedStatementParserRuleCall_1_0_0());
                    					
                    pushFollow(FOLLOW_24);
                    lv_nested_8_0=ruleNestedStatement();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSourceRule());
                    						}
                    						set(
                    							current,
                    							"nested",
                    							lv_nested_8_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.NestedStatement");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    otherlv_9=(Token)match(input,AS,FOLLOW_15); 

                    				newLeafNode(otherlv_9, grammarAccess.getSourceAccess().getASKeyword_1_1());
                    			
                    // InternalCQLParser.g:851:4: ( (lv_alias_10_0= ruleAlias ) )
                    // InternalCQLParser.g:852:5: (lv_alias_10_0= ruleAlias )
                    {
                    // InternalCQLParser.g:852:5: (lv_alias_10_0= ruleAlias )
                    // InternalCQLParser.g:853:6: lv_alias_10_0= ruleAlias
                    {

                    						newCompositeNode(grammarAccess.getSourceAccess().getAliasAliasParserRuleCall_1_2_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_alias_10_0=ruleAlias();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSourceRule());
                    						}
                    						set(
                    							current,
                    							"alias",
                    							lv_alias_10_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Alias");
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
    // $ANTLR end "ruleSource"


    // $ANTLR start "entryRuleSourceName"
    // InternalCQLParser.g:875:1: entryRuleSourceName returns [String current=null] : iv_ruleSourceName= ruleSourceName EOF ;
    public final String entryRuleSourceName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleSourceName = null;


        try {
            // InternalCQLParser.g:875:50: (iv_ruleSourceName= ruleSourceName EOF )
            // InternalCQLParser.g:876:2: iv_ruleSourceName= ruleSourceName EOF
            {
             newCompositeNode(grammarAccess.getSourceNameRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSourceName=ruleSourceName();

            state._fsp--;

             current =iv_ruleSourceName.getText(); 
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
    // $ANTLR end "entryRuleSourceName"


    // $ANTLR start "ruleSourceName"
    // InternalCQLParser.g:882:1: ruleSourceName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : this_ID_0= RULE_ID ;
    public final AntlrDatatypeRuleToken ruleSourceName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:888:2: (this_ID_0= RULE_ID )
            // InternalCQLParser.g:889:2: this_ID_0= RULE_ID
            {
            this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            		current.merge(this_ID_0);
            	

            		newLeafNode(this_ID_0, grammarAccess.getSourceNameAccess().getIDTerminalRuleCall());
            	

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
    // $ANTLR end "ruleSourceName"


    // $ANTLR start "entryRuleAttribute"
    // InternalCQLParser.g:899:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCQLParser.g:899:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCQLParser.g:900:2: iv_ruleAttribute= ruleAttribute EOF
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
    // InternalCQLParser.g:906:1: ruleAttribute returns [EObject current=null] : ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_name_0_0 = null;

        EObject lv_alias_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:912:2: ( ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:913:2: ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:913:2: ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:914:3: ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:914:3: ( (lv_name_0_0= ruleAttributeName ) )
            // InternalCQLParser.g:915:4: (lv_name_0_0= ruleAttributeName )
            {
            // InternalCQLParser.g:915:4: (lv_name_0_0= ruleAttributeName )
            // InternalCQLParser.g:916:5: lv_name_0_0= ruleAttributeName
            {

            					newCompositeNode(grammarAccess.getAttributeAccess().getNameAttributeNameParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_23);
            lv_name_0_0=ruleAttributeName();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAttributeRule());
            					}
            					set(
            						current,
            						"name",
            						lv_name_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeName");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:933:3: (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==AS) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // InternalCQLParser.g:934:4: otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) )
                    {
                    otherlv_1=(Token)match(input,AS,FOLLOW_15); 

                    				newLeafNode(otherlv_1, grammarAccess.getAttributeAccess().getASKeyword_1_0());
                    			
                    // InternalCQLParser.g:938:4: ( (lv_alias_2_0= ruleAlias ) )
                    // InternalCQLParser.g:939:5: (lv_alias_2_0= ruleAlias )
                    {
                    // InternalCQLParser.g:939:5: (lv_alias_2_0= ruleAlias )
                    // InternalCQLParser.g:940:6: lv_alias_2_0= ruleAlias
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


    // $ANTLR start "entryRuleAttributeWithoutAliasDefinition"
    // InternalCQLParser.g:962:1: entryRuleAttributeWithoutAliasDefinition returns [EObject current=null] : iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF ;
    public final EObject entryRuleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithoutAliasDefinition = null;


        try {
            // InternalCQLParser.g:962:72: (iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF )
            // InternalCQLParser.g:963:2: iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF
            {
             newCompositeNode(grammarAccess.getAttributeWithoutAliasDefinitionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAttributeWithoutAliasDefinition=ruleAttributeWithoutAliasDefinition();

            state._fsp--;

             current =iv_ruleAttributeWithoutAliasDefinition; 
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
    // $ANTLR end "entryRuleAttributeWithoutAliasDefinition"


    // $ANTLR start "ruleAttributeWithoutAliasDefinition"
    // InternalCQLParser.g:969:1: ruleAttributeWithoutAliasDefinition returns [EObject current=null] : ( (lv_name_0_0= ruleAttributeName ) ) ;
    public final EObject ruleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_name_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:975:2: ( ( (lv_name_0_0= ruleAttributeName ) ) )
            // InternalCQLParser.g:976:2: ( (lv_name_0_0= ruleAttributeName ) )
            {
            // InternalCQLParser.g:976:2: ( (lv_name_0_0= ruleAttributeName ) )
            // InternalCQLParser.g:977:3: (lv_name_0_0= ruleAttributeName )
            {
            // InternalCQLParser.g:977:3: (lv_name_0_0= ruleAttributeName )
            // InternalCQLParser.g:978:4: lv_name_0_0= ruleAttributeName
            {

            				newCompositeNode(grammarAccess.getAttributeWithoutAliasDefinitionAccess().getNameAttributeNameParserRuleCall_0());
            			
            pushFollow(FOLLOW_2);
            lv_name_0_0=ruleAttributeName();

            state._fsp--;


            				if (current==null) {
            					current = createModelElementForParent(grammarAccess.getAttributeWithoutAliasDefinitionRule());
            				}
            				set(
            					current,
            					"name",
            					lv_name_0_0,
            					"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeName");
            				afterParserOrEnumRuleCall();
            			

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
    // $ANTLR end "ruleAttributeWithoutAliasDefinition"


    // $ANTLR start "entryRuleAttributeName"
    // InternalCQLParser.g:998:1: entryRuleAttributeName returns [String current=null] : iv_ruleAttributeName= ruleAttributeName EOF ;
    public final String entryRuleAttributeName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleAttributeName = null;


        try {
            // InternalCQLParser.g:998:53: (iv_ruleAttributeName= ruleAttributeName EOF )
            // InternalCQLParser.g:999:2: iv_ruleAttributeName= ruleAttributeName EOF
            {
             newCompositeNode(grammarAccess.getAttributeNameRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAttributeName=ruleAttributeName();

            state._fsp--;

             current =iv_ruleAttributeName.getText(); 
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
    // $ANTLR end "entryRuleAttributeName"


    // $ANTLR start "ruleAttributeName"
    // InternalCQLParser.g:1005:1: ruleAttributeName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID ) | (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk ) ) ;
    public final AntlrDatatypeRuleToken ruleAttributeName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_3=null;
        AntlrDatatypeRuleToken this_SourceName_1 = null;

        AntlrDatatypeRuleToken this_SourceName_4 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1011:2: ( (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID ) | (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk ) ) )
            // InternalCQLParser.g:1012:2: (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID ) | (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk ) )
            {
            // InternalCQLParser.g:1012:2: (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID ) | (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk ) )
            int alt21=3;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==RULE_ID) ) {
                int LA21_1 = input.LA(2);

                if ( (LA21_1==EOF||(LA21_1>=ATTACH && LA21_1<=CREATE)||(LA21_1>=HAVING && LA21_1<=STREAM)||(LA21_1>=ALTER && LA21_1<=GROUP)||LA21_1==DROP||LA21_1==FROM||LA21_1==TRUE||(LA21_1>=VIEW && LA21_1<=AND)||(LA21_1>=ExclamationMarkEqualsSign && LA21_1<=AS)||LA21_1==IN||LA21_1==OR||(LA21_1>=RightParenthesis && LA21_1<=HyphenMinus)||LA21_1==Solidus||(LA21_1>=Semicolon && LA21_1<=GreaterThanSign)||(LA21_1>=RightSquareBracket && LA21_1<=RULE_STRING)) ) {
                    alt21=1;
                }
                else if ( (LA21_1==FullStop) ) {
                    int LA21_3 = input.LA(3);

                    if ( (LA21_3==RULE_ID) ) {
                        alt21=2;
                    }
                    else if ( (LA21_3==Asterisk) ) {
                        alt21=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 21, 3, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }
            switch (alt21) {
                case 1 :
                    // InternalCQLParser.g:1013:3: this_ID_0= RULE_ID
                    {
                    this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    			current.merge(this_ID_0);
                    		

                    			newLeafNode(this_ID_0, grammarAccess.getAttributeNameAccess().getIDTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1021:3: (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID )
                    {
                    // InternalCQLParser.g:1021:3: (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID )
                    // InternalCQLParser.g:1022:4: this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID
                    {

                    				newCompositeNode(grammarAccess.getAttributeNameAccess().getSourceNameParserRuleCall_1_0());
                    			
                    pushFollow(FOLLOW_25);
                    this_SourceName_1=ruleSourceName();

                    state._fsp--;


                    				current.merge(this_SourceName_1);
                    			

                    				afterParserOrEnumRuleCall();
                    			
                    kw=(Token)match(input,FullStop,FOLLOW_15); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getAttributeNameAccess().getFullStopKeyword_1_1());
                    			
                    this_ID_3=(Token)match(input,RULE_ID,FOLLOW_2); 

                    				current.merge(this_ID_3);
                    			

                    				newLeafNode(this_ID_3, grammarAccess.getAttributeNameAccess().getIDTerminalRuleCall_1_2());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:1046:3: (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk )
                    {
                    // InternalCQLParser.g:1046:3: (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk )
                    // InternalCQLParser.g:1047:4: this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk
                    {

                    				newCompositeNode(grammarAccess.getAttributeNameAccess().getSourceNameParserRuleCall_2_0());
                    			
                    pushFollow(FOLLOW_25);
                    this_SourceName_4=ruleSourceName();

                    state._fsp--;


                    				current.merge(this_SourceName_4);
                    			

                    				afterParserOrEnumRuleCall();
                    			
                    kw=(Token)match(input,FullStop,FOLLOW_26); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getAttributeNameAccess().getFullStopKeyword_2_1());
                    			
                    kw=(Token)match(input,Asterisk,FOLLOW_2); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getAttributeNameAccess().getAsteriskKeyword_2_2());
                    			

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
    // $ANTLR end "ruleAttributeName"


    // $ANTLR start "entryRuleAttributeWithNestedStatement"
    // InternalCQLParser.g:1072:1: entryRuleAttributeWithNestedStatement returns [EObject current=null] : iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF ;
    public final EObject entryRuleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithNestedStatement = null;


        try {
            // InternalCQLParser.g:1072:69: (iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF )
            // InternalCQLParser.g:1073:2: iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF
            {
             newCompositeNode(grammarAccess.getAttributeWithNestedStatementRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAttributeWithNestedStatement=ruleAttributeWithNestedStatement();

            state._fsp--;

             current =iv_ruleAttributeWithNestedStatement; 
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
    // $ANTLR end "entryRuleAttributeWithNestedStatement"


    // $ANTLR start "ruleAttributeWithNestedStatement"
    // InternalCQLParser.g:1079:1: ruleAttributeWithNestedStatement returns [EObject current=null] : ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_value_0_0 = null;

        EObject lv_nested_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1085:2: ( ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) ) ) )
            // InternalCQLParser.g:1086:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) ) )
            {
            // InternalCQLParser.g:1086:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) ) )
            // InternalCQLParser.g:1087:3: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) )
            {
            // InternalCQLParser.g:1087:3: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQLParser.g:1088:4: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1088:4: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQLParser.g:1089:5: lv_value_0_0= ruleAttributeWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getAttributeWithNestedStatementAccess().getValueAttributeWithoutAliasDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_27);
            lv_value_0_0=ruleAttributeWithoutAliasDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAttributeWithNestedStatementRule());
            					}
            					set(
            						current,
            						"value",
            						lv_value_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,IN,FOLLOW_9); 

            			newLeafNode(otherlv_1, grammarAccess.getAttributeWithNestedStatementAccess().getINKeyword_1());
            		
            // InternalCQLParser.g:1110:3: ( (lv_nested_2_0= ruleNestedStatement ) )
            // InternalCQLParser.g:1111:4: (lv_nested_2_0= ruleNestedStatement )
            {
            // InternalCQLParser.g:1111:4: (lv_nested_2_0= ruleNestedStatement )
            // InternalCQLParser.g:1112:5: lv_nested_2_0= ruleNestedStatement
            {

            					newCompositeNode(grammarAccess.getAttributeWithNestedStatementAccess().getNestedNestedStatementParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_2);
            lv_nested_2_0=ruleNestedStatement();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAttributeWithNestedStatementRule());
            					}
            					set(
            						current,
            						"nested",
            						lv_nested_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.NestedStatement");
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
    // $ANTLR end "ruleAttributeWithNestedStatement"


    // $ANTLR start "entryRuleSelectExpression"
    // InternalCQLParser.g:1133:1: entryRuleSelectExpression returns [EObject current=null] : iv_ruleSelectExpression= ruleSelectExpression EOF ;
    public final EObject entryRuleSelectExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpression = null;


        try {
            // InternalCQLParser.g:1133:57: (iv_ruleSelectExpression= ruleSelectExpression EOF )
            // InternalCQLParser.g:1134:2: iv_ruleSelectExpression= ruleSelectExpression EOF
            {
             newCompositeNode(grammarAccess.getSelectExpressionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelectExpression=ruleSelectExpression();

            state._fsp--;

             current =iv_ruleSelectExpression; 
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
    // $ANTLR end "entryRuleSelectExpression"


    // $ANTLR start "ruleSelectExpression"
    // InternalCQLParser.g:1140:1: ruleSelectExpression returns [EObject current=null] : ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) ;
    public final EObject ruleSelectExpression() throws RecognitionException {
        EObject current = null;

        Token lv_operators_1_1=null;
        Token lv_operators_1_2=null;
        Token lv_operators_1_3=null;
        Token lv_operators_1_4=null;
        Token lv_operators_4_1=null;
        Token lv_operators_4_2=null;
        Token lv_operators_4_3=null;
        Token lv_operators_4_4=null;
        Token otherlv_6=null;
        EObject lv_expressions_0_0 = null;

        EObject lv_expressions_2_1 = null;

        EObject lv_expressions_2_2 = null;

        EObject lv_expressions_3_0 = null;

        EObject lv_expressions_5_1 = null;

        EObject lv_expressions_5_2 = null;

        EObject lv_alias_7_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1146:2: ( ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:1147:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:1147:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:1148:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:1148:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) )
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==RULE_ID) ) {
                int LA28_1 = input.LA(2);

                if ( ((LA28_1>=Asterisk && LA28_1<=PlusSign)||(LA28_1>=HyphenMinus && LA28_1<=Solidus)) ) {
                    alt28=2;
                }
                else if ( (LA28_1==LeftParenthesis) ) {
                    alt28=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 28, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA28_0==FALSE||LA28_0==TRUE||(LA28_0>=RULE_INT && LA28_0<=RULE_STRING)) ) {
                alt28=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 28, 0, input);

                throw nvae;
            }
            switch (alt28) {
                case 1 :
                    // InternalCQLParser.g:1149:4: ( ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* )
                    {
                    // InternalCQLParser.g:1149:4: ( ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* )
                    // InternalCQLParser.g:1150:5: ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )*
                    {
                    // InternalCQLParser.g:1150:5: ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) )
                    // InternalCQLParser.g:1151:6: (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant )
                    {
                    // InternalCQLParser.g:1151:6: (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant )
                    // InternalCQLParser.g:1152:7: lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentFunctionOrConstantParserRuleCall_0_0_0_0());
                    						
                    pushFollow(FOLLOW_28);
                    lv_expressions_0_0=ruleExpressionComponentFunctionOrConstant();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    							}
                    							add(
                    								current,
                    								"expressions",
                    								lv_expressions_0_0,
                    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentFunctionOrConstant");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCQLParser.g:1169:5: ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )*
                    loop24:
                    do {
                        int alt24=2;
                        int LA24_0 = input.LA(1);

                        if ( ((LA24_0>=Asterisk && LA24_0<=PlusSign)||LA24_0==HyphenMinus||LA24_0==Solidus) ) {
                            alt24=1;
                        }


                        switch (alt24) {
                    	case 1 :
                    	    // InternalCQLParser.g:1170:6: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) )
                    	    {
                    	    // InternalCQLParser.g:1170:6: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1171:7: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1171:7: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
                    	    // InternalCQLParser.g:1172:8: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1172:8: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
                    	    int alt22=4;
                    	    switch ( input.LA(1) ) {
                    	    case PlusSign:
                    	        {
                    	        alt22=1;
                    	        }
                    	        break;
                    	    case HyphenMinus:
                    	        {
                    	        alt22=2;
                    	        }
                    	        break;
                    	    case Asterisk:
                    	        {
                    	        alt22=3;
                    	        }
                    	        break;
                    	    case Solidus:
                    	        {
                    	        alt22=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 22, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt22) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1173:9: lv_operators_1_1= PlusSign
                    	            {
                    	            lv_operators_1_1=(Token)match(input,PlusSign,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_1_1, grammarAccess.getSelectExpressionAccess().getOperatorsPlusSignKeyword_0_0_1_0_0_0());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_1_1, null);
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1184:9: lv_operators_1_2= HyphenMinus
                    	            {
                    	            lv_operators_1_2=(Token)match(input,HyphenMinus,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_1_2, grammarAccess.getSelectExpressionAccess().getOperatorsHyphenMinusKeyword_0_0_1_0_0_1());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_1_2, null);
                    	            								

                    	            }
                    	            break;
                    	        case 3 :
                    	            // InternalCQLParser.g:1195:9: lv_operators_1_3= Asterisk
                    	            {
                    	            lv_operators_1_3=(Token)match(input,Asterisk,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_1_3, grammarAccess.getSelectExpressionAccess().getOperatorsAsteriskKeyword_0_0_1_0_0_2());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_1_3, null);
                    	            								

                    	            }
                    	            break;
                    	        case 4 :
                    	            // InternalCQLParser.g:1206:9: lv_operators_1_4= Solidus
                    	            {
                    	            lv_operators_1_4=(Token)match(input,Solidus,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_1_4, grammarAccess.getSelectExpressionAccess().getOperatorsSolidusKeyword_0_0_1_0_0_3());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_1_4, null);
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }

                    	    // InternalCQLParser.g:1219:6: ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) )
                    	    // InternalCQLParser.g:1220:7: ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) )
                    	    {
                    	    // InternalCQLParser.g:1220:7: ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) )
                    	    // InternalCQLParser.g:1221:8: (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction )
                    	    {
                    	    // InternalCQLParser.g:1221:8: (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction )
                    	    int alt23=2;
                    	    int LA23_0 = input.LA(1);

                    	    if ( (LA23_0==FALSE||LA23_0==TRUE||(LA23_0>=RULE_INT && LA23_0<=RULE_STRING)) ) {
                    	        alt23=1;
                    	    }
                    	    else if ( (LA23_0==RULE_ID) ) {
                    	        int LA23_2 = input.LA(2);

                    	        if ( (LA23_2==EOF||LA23_2==FALSE||LA23_2==FROM||LA23_2==TRUE||LA23_2==AS||(LA23_2>=Asterisk && LA23_2<=Solidus)||(LA23_2>=RULE_ID && LA23_2<=RULE_STRING)) ) {
                    	            alt23=1;
                    	        }
                    	        else if ( (LA23_2==LeftParenthesis) ) {
                    	            alt23=2;
                    	        }
                    	        else {
                    	            NoViableAltException nvae =
                    	                new NoViableAltException("", 23, 2, input);

                    	            throw nvae;
                    	        }
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 23, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt23) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1222:9: lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_0_0_1_1_0_0());
                    	            								
                    	            pushFollow(FOLLOW_28);
                    	            lv_expressions_2_1=ruleExpressionComponentConstantOrAttribute();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_2_1,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentConstantOrAttribute");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1238:9: lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentOnlyWithFunctionParserRuleCall_0_0_1_1_0_1());
                    	            								
                    	            pushFollow(FOLLOW_28);
                    	            lv_expressions_2_2=ruleExpressionComponentOnlyWithFunction();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_2_2,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlyWithFunction");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;

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


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1259:4: ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ )
                    {
                    // InternalCQLParser.g:1259:4: ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ )
                    // InternalCQLParser.g:1260:5: ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+
                    {
                    // InternalCQLParser.g:1260:5: ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) )
                    // InternalCQLParser.g:1261:6: (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute )
                    {
                    // InternalCQLParser.g:1261:6: (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute )
                    // InternalCQLParser.g:1262:7: lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentOnlyWithAttributeParserRuleCall_0_1_0_0());
                    						
                    pushFollow(FOLLOW_29);
                    lv_expressions_3_0=ruleExpressionComponentOnlyWithAttribute();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    							}
                    							add(
                    								current,
                    								"expressions",
                    								lv_expressions_3_0,
                    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlyWithAttribute");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCQLParser.g:1279:5: ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+
                    int cnt27=0;
                    loop27:
                    do {
                        int alt27=2;
                        int LA27_0 = input.LA(1);

                        if ( ((LA27_0>=Asterisk && LA27_0<=PlusSign)||LA27_0==HyphenMinus||LA27_0==Solidus) ) {
                            alt27=1;
                        }


                        switch (alt27) {
                    	case 1 :
                    	    // InternalCQLParser.g:1280:6: ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) )
                    	    {
                    	    // InternalCQLParser.g:1280:6: ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1281:7: ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1281:7: ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) )
                    	    // InternalCQLParser.g:1282:8: (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1282:8: (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus )
                    	    int alt25=4;
                    	    switch ( input.LA(1) ) {
                    	    case PlusSign:
                    	        {
                    	        alt25=1;
                    	        }
                    	        break;
                    	    case HyphenMinus:
                    	        {
                    	        alt25=2;
                    	        }
                    	        break;
                    	    case Asterisk:
                    	        {
                    	        alt25=3;
                    	        }
                    	        break;
                    	    case Solidus:
                    	        {
                    	        alt25=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 25, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt25) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1283:9: lv_operators_4_1= PlusSign
                    	            {
                    	            lv_operators_4_1=(Token)match(input,PlusSign,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_4_1, grammarAccess.getSelectExpressionAccess().getOperatorsPlusSignKeyword_0_1_1_0_0_0());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_4_1, null);
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1294:9: lv_operators_4_2= HyphenMinus
                    	            {
                    	            lv_operators_4_2=(Token)match(input,HyphenMinus,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_4_2, grammarAccess.getSelectExpressionAccess().getOperatorsHyphenMinusKeyword_0_1_1_0_0_1());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_4_2, null);
                    	            								

                    	            }
                    	            break;
                    	        case 3 :
                    	            // InternalCQLParser.g:1305:9: lv_operators_4_3= Asterisk
                    	            {
                    	            lv_operators_4_3=(Token)match(input,Asterisk,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_4_3, grammarAccess.getSelectExpressionAccess().getOperatorsAsteriskKeyword_0_1_1_0_0_2());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_4_3, null);
                    	            								

                    	            }
                    	            break;
                    	        case 4 :
                    	            // InternalCQLParser.g:1316:9: lv_operators_4_4= Solidus
                    	            {
                    	            lv_operators_4_4=(Token)match(input,Solidus,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_4_4, grammarAccess.getSelectExpressionAccess().getOperatorsSolidusKeyword_0_1_1_0_0_3());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_4_4, null);
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }

                    	    // InternalCQLParser.g:1329:6: ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) )
                    	    // InternalCQLParser.g:1330:7: ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) )
                    	    {
                    	    // InternalCQLParser.g:1330:7: ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) )
                    	    // InternalCQLParser.g:1331:8: (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction )
                    	    {
                    	    // InternalCQLParser.g:1331:8: (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction )
                    	    int alt26=2;
                    	    int LA26_0 = input.LA(1);

                    	    if ( (LA26_0==FALSE||LA26_0==TRUE||(LA26_0>=RULE_INT && LA26_0<=RULE_STRING)) ) {
                    	        alt26=1;
                    	    }
                    	    else if ( (LA26_0==RULE_ID) ) {
                    	        int LA26_2 = input.LA(2);

                    	        if ( (LA26_2==EOF||LA26_2==FALSE||LA26_2==FROM||LA26_2==TRUE||LA26_2==AS||(LA26_2>=Asterisk && LA26_2<=Solidus)||(LA26_2>=RULE_ID && LA26_2<=RULE_STRING)) ) {
                    	            alt26=1;
                    	        }
                    	        else if ( (LA26_2==LeftParenthesis) ) {
                    	            alt26=2;
                    	        }
                    	        else {
                    	            NoViableAltException nvae =
                    	                new NoViableAltException("", 26, 2, input);

                    	            throw nvae;
                    	        }
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 26, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt26) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1332:9: lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_0_1_1_1_0_0());
                    	            								
                    	            pushFollow(FOLLOW_28);
                    	            lv_expressions_5_1=ruleExpressionComponentConstantOrAttribute();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_5_1,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentConstantOrAttribute");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1348:9: lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentOnlyWithFunctionParserRuleCall_0_1_1_1_0_1());
                    	            								
                    	            pushFollow(FOLLOW_28);
                    	            lv_expressions_5_2=ruleExpressionComponentOnlyWithFunction();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_5_2,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlyWithFunction");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;

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


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:1369:3: (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==AS) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // InternalCQLParser.g:1370:4: otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) )
                    {
                    otherlv_6=(Token)match(input,AS,FOLLOW_15); 

                    				newLeafNode(otherlv_6, grammarAccess.getSelectExpressionAccess().getASKeyword_1_0());
                    			
                    // InternalCQLParser.g:1374:4: ( (lv_alias_7_0= ruleAlias ) )
                    // InternalCQLParser.g:1375:5: (lv_alias_7_0= ruleAlias )
                    {
                    // InternalCQLParser.g:1375:5: (lv_alias_7_0= ruleAlias )
                    // InternalCQLParser.g:1376:6: lv_alias_7_0= ruleAlias
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionAccess().getAliasAliasParserRuleCall_1_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_alias_7_0=ruleAlias();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
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
    // $ANTLR end "ruleSelectExpression"


    // $ANTLR start "entryRuleSelectExpressionWithoutAliasDefinition"
    // InternalCQLParser.g:1398:1: entryRuleSelectExpressionWithoutAliasDefinition returns [EObject current=null] : iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF ;
    public final EObject entryRuleSelectExpressionWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionWithoutAliasDefinition = null;


        try {
            // InternalCQLParser.g:1398:79: (iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF )
            // InternalCQLParser.g:1399:2: iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF
            {
             newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelectExpressionWithoutAliasDefinition=ruleSelectExpressionWithoutAliasDefinition();

            state._fsp--;

             current =iv_ruleSelectExpressionWithoutAliasDefinition; 
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
    // $ANTLR end "entryRuleSelectExpressionWithoutAliasDefinition"


    // $ANTLR start "ruleSelectExpressionWithoutAliasDefinition"
    // InternalCQLParser.g:1405:1: ruleSelectExpressionWithoutAliasDefinition returns [EObject current=null] : (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) ) ;
    public final EObject ruleSelectExpressionWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        Token lv_operators_2_1=null;
        Token lv_operators_2_2=null;
        Token lv_operators_2_3=null;
        Token lv_operators_2_4=null;
        Token lv_operators_5_1=null;
        Token lv_operators_5_2=null;
        Token lv_operators_5_3=null;
        Token lv_operators_5_4=null;
        EObject this_SelectExpressionWithOnlyAttributeOrConstant_0 = null;

        EObject lv_expressions_1_0 = null;

        EObject lv_expressions_3_1 = null;

        EObject lv_expressions_3_2 = null;

        EObject lv_expressions_4_0 = null;

        EObject lv_expressions_6_1 = null;

        EObject lv_expressions_6_2 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1411:2: ( (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) ) )
            // InternalCQLParser.g:1412:2: (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) )
            {
            // InternalCQLParser.g:1412:2: (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) )
            // InternalCQLParser.g:1413:3: this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) )
            {

            			newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getSelectExpressionWithOnlyAttributeOrConstantParserRuleCall_0());
            		
            pushFollow(FOLLOW_5);
            this_SelectExpressionWithOnlyAttributeOrConstant_0=ruleSelectExpressionWithOnlyAttributeOrConstant();

            state._fsp--;


            			current = this_SelectExpressionWithOnlyAttributeOrConstant_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:1421:3: ( ( ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) )
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==RULE_ID) ) {
                int LA36_1 = input.LA(2);

                if ( ((LA36_1>=Asterisk && LA36_1<=PlusSign)||(LA36_1>=HyphenMinus && LA36_1<=Solidus)) ) {
                    alt36=2;
                }
                else if ( (LA36_1==LeftParenthesis) ) {
                    alt36=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 36, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA36_0==FALSE||LA36_0==TRUE||(LA36_0>=RULE_INT && LA36_0<=RULE_STRING)) ) {
                alt36=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 36, 0, input);

                throw nvae;
            }
            switch (alt36) {
                case 1 :
                    // InternalCQLParser.g:1422:4: ( ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* )
                    {
                    // InternalCQLParser.g:1422:4: ( ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* )
                    // InternalCQLParser.g:1423:5: ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )*
                    {
                    // InternalCQLParser.g:1423:5: ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) )
                    // InternalCQLParser.g:1424:6: (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant )
                    {
                    // InternalCQLParser.g:1424:6: (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant )
                    // InternalCQLParser.g:1425:7: lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentFunctionOrConstantParserRuleCall_1_0_0_0());
                    						
                    pushFollow(FOLLOW_30);
                    lv_expressions_1_0=ruleExpressionComponentFunctionOrConstant();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    							}
                    							add(
                    								current,
                    								"expressions",
                    								lv_expressions_1_0,
                    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentFunctionOrConstant");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCQLParser.g:1442:5: ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )*
                    loop32:
                    do {
                        int alt32=2;
                        int LA32_0 = input.LA(1);

                        if ( ((LA32_0>=Asterisk && LA32_0<=PlusSign)||LA32_0==HyphenMinus||LA32_0==Solidus) ) {
                            alt32=1;
                        }


                        switch (alt32) {
                    	case 1 :
                    	    // InternalCQLParser.g:1443:6: ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) )
                    	    {
                    	    // InternalCQLParser.g:1443:6: ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1444:7: ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1444:7: ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) )
                    	    // InternalCQLParser.g:1445:8: (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1445:8: (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus )
                    	    int alt30=4;
                    	    switch ( input.LA(1) ) {
                    	    case PlusSign:
                    	        {
                    	        alt30=1;
                    	        }
                    	        break;
                    	    case HyphenMinus:
                    	        {
                    	        alt30=2;
                    	        }
                    	        break;
                    	    case Asterisk:
                    	        {
                    	        alt30=3;
                    	        }
                    	        break;
                    	    case Solidus:
                    	        {
                    	        alt30=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 30, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt30) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1446:9: lv_operators_2_1= PlusSign
                    	            {
                    	            lv_operators_2_1=(Token)match(input,PlusSign,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_2_1, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsPlusSignKeyword_1_0_1_0_0_0());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_2_1, null);
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1457:9: lv_operators_2_2= HyphenMinus
                    	            {
                    	            lv_operators_2_2=(Token)match(input,HyphenMinus,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_2_2, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsHyphenMinusKeyword_1_0_1_0_0_1());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_2_2, null);
                    	            								

                    	            }
                    	            break;
                    	        case 3 :
                    	            // InternalCQLParser.g:1468:9: lv_operators_2_3= Asterisk
                    	            {
                    	            lv_operators_2_3=(Token)match(input,Asterisk,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_2_3, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsAsteriskKeyword_1_0_1_0_0_2());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_2_3, null);
                    	            								

                    	            }
                    	            break;
                    	        case 4 :
                    	            // InternalCQLParser.g:1479:9: lv_operators_2_4= Solidus
                    	            {
                    	            lv_operators_2_4=(Token)match(input,Solidus,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_2_4, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsSolidusKeyword_1_0_1_0_0_3());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_2_4, null);
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }

                    	    // InternalCQLParser.g:1492:6: ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) )
                    	    // InternalCQLParser.g:1493:7: ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) )
                    	    {
                    	    // InternalCQLParser.g:1493:7: ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) )
                    	    // InternalCQLParser.g:1494:8: (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction )
                    	    {
                    	    // InternalCQLParser.g:1494:8: (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction )
                    	    int alt31=2;
                    	    int LA31_0 = input.LA(1);

                    	    if ( (LA31_0==FALSE||LA31_0==TRUE||(LA31_0>=RULE_INT && LA31_0<=RULE_STRING)) ) {
                    	        alt31=1;
                    	    }
                    	    else if ( (LA31_0==RULE_ID) ) {
                    	        int LA31_2 = input.LA(2);

                    	        if ( (LA31_2==EOF||(LA31_2>=RightParenthesis && LA31_2<=PlusSign)||(LA31_2>=HyphenMinus && LA31_2<=Solidus)) ) {
                    	            alt31=1;
                    	        }
                    	        else if ( (LA31_2==LeftParenthesis) ) {
                    	            alt31=2;
                    	        }
                    	        else {
                    	            NoViableAltException nvae =
                    	                new NoViableAltException("", 31, 2, input);

                    	            throw nvae;
                    	        }
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 31, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt31) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1495:9: lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_1_0_1_1_0_0());
                    	            								
                    	            pushFollow(FOLLOW_30);
                    	            lv_expressions_3_1=ruleExpressionComponentConstantOrAttribute();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_3_1,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentConstantOrAttribute");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1511:9: lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentOnlyWithFunctionParserRuleCall_1_0_1_1_0_1());
                    	            								
                    	            pushFollow(FOLLOW_30);
                    	            lv_expressions_3_2=ruleExpressionComponentOnlyWithFunction();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_3_2,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlyWithFunction");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;

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


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1532:4: ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ )
                    {
                    // InternalCQLParser.g:1532:4: ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ )
                    // InternalCQLParser.g:1533:5: ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+
                    {
                    // InternalCQLParser.g:1533:5: ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) )
                    // InternalCQLParser.g:1534:6: (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute )
                    {
                    // InternalCQLParser.g:1534:6: (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute )
                    // InternalCQLParser.g:1535:7: lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentOnlyWithAttributeParserRuleCall_1_1_0_0());
                    						
                    pushFollow(FOLLOW_29);
                    lv_expressions_4_0=ruleExpressionComponentOnlyWithAttribute();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    							}
                    							add(
                    								current,
                    								"expressions",
                    								lv_expressions_4_0,
                    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlyWithAttribute");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCQLParser.g:1552:5: ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+
                    int cnt35=0;
                    loop35:
                    do {
                        int alt35=2;
                        int LA35_0 = input.LA(1);

                        if ( ((LA35_0>=Asterisk && LA35_0<=PlusSign)||LA35_0==HyphenMinus||LA35_0==Solidus) ) {
                            alt35=1;
                        }


                        switch (alt35) {
                    	case 1 :
                    	    // InternalCQLParser.g:1553:6: ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) )
                    	    {
                    	    // InternalCQLParser.g:1553:6: ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1554:7: ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1554:7: ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) )
                    	    // InternalCQLParser.g:1555:8: (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1555:8: (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus )
                    	    int alt33=4;
                    	    switch ( input.LA(1) ) {
                    	    case PlusSign:
                    	        {
                    	        alt33=1;
                    	        }
                    	        break;
                    	    case HyphenMinus:
                    	        {
                    	        alt33=2;
                    	        }
                    	        break;
                    	    case Asterisk:
                    	        {
                    	        alt33=3;
                    	        }
                    	        break;
                    	    case Solidus:
                    	        {
                    	        alt33=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 33, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt33) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1556:9: lv_operators_5_1= PlusSign
                    	            {
                    	            lv_operators_5_1=(Token)match(input,PlusSign,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_5_1, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsPlusSignKeyword_1_1_1_0_0_0());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_5_1, null);
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1567:9: lv_operators_5_2= HyphenMinus
                    	            {
                    	            lv_operators_5_2=(Token)match(input,HyphenMinus,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_5_2, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsHyphenMinusKeyword_1_1_1_0_0_1());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_5_2, null);
                    	            								

                    	            }
                    	            break;
                    	        case 3 :
                    	            // InternalCQLParser.g:1578:9: lv_operators_5_3= Asterisk
                    	            {
                    	            lv_operators_5_3=(Token)match(input,Asterisk,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_5_3, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsAsteriskKeyword_1_1_1_0_0_2());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_5_3, null);
                    	            								

                    	            }
                    	            break;
                    	        case 4 :
                    	            // InternalCQLParser.g:1589:9: lv_operators_5_4= Solidus
                    	            {
                    	            lv_operators_5_4=(Token)match(input,Solidus,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_5_4, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsSolidusKeyword_1_1_1_0_0_3());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_5_4, null);
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }

                    	    // InternalCQLParser.g:1602:6: ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) )
                    	    // InternalCQLParser.g:1603:7: ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) )
                    	    {
                    	    // InternalCQLParser.g:1603:7: ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) )
                    	    // InternalCQLParser.g:1604:8: (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction )
                    	    {
                    	    // InternalCQLParser.g:1604:8: (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction )
                    	    int alt34=2;
                    	    int LA34_0 = input.LA(1);

                    	    if ( (LA34_0==FALSE||LA34_0==TRUE||(LA34_0>=RULE_INT && LA34_0<=RULE_STRING)) ) {
                    	        alt34=1;
                    	    }
                    	    else if ( (LA34_0==RULE_ID) ) {
                    	        int LA34_2 = input.LA(2);

                    	        if ( (LA34_2==EOF||(LA34_2>=RightParenthesis && LA34_2<=PlusSign)||(LA34_2>=HyphenMinus && LA34_2<=Solidus)) ) {
                    	            alt34=1;
                    	        }
                    	        else if ( (LA34_2==LeftParenthesis) ) {
                    	            alt34=2;
                    	        }
                    	        else {
                    	            NoViableAltException nvae =
                    	                new NoViableAltException("", 34, 2, input);

                    	            throw nvae;
                    	        }
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 34, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt34) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1605:9: lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_1_1_1_1_0_0());
                    	            								
                    	            pushFollow(FOLLOW_30);
                    	            lv_expressions_6_1=ruleExpressionComponentConstantOrAttribute();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_6_1,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentConstantOrAttribute");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1621:9: lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentOnlyWithFunctionParserRuleCall_1_1_1_1_0_1());
                    	            								
                    	            pushFollow(FOLLOW_30);
                    	            lv_expressions_6_2=ruleExpressionComponentOnlyWithFunction();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_6_2,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlyWithFunction");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;

                    	    }


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
    // $ANTLR end "ruleSelectExpressionWithoutAliasDefinition"


    // $ANTLR start "entryRuleSelectExpressionWithOnlyAttributeOrConstant"
    // InternalCQLParser.g:1646:1: entryRuleSelectExpressionWithOnlyAttributeOrConstant returns [EObject current=null] : iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF ;
    public final EObject entryRuleSelectExpressionWithOnlyAttributeOrConstant() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionWithOnlyAttributeOrConstant = null;


        try {
            // InternalCQLParser.g:1646:84: (iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF )
            // InternalCQLParser.g:1647:2: iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF
            {
             newCompositeNode(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelectExpressionWithOnlyAttributeOrConstant=ruleSelectExpressionWithOnlyAttributeOrConstant();

            state._fsp--;

             current =iv_ruleSelectExpressionWithOnlyAttributeOrConstant; 
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
    // $ANTLR end "entryRuleSelectExpressionWithOnlyAttributeOrConstant"


    // $ANTLR start "ruleSelectExpressionWithOnlyAttributeOrConstant"
    // InternalCQLParser.g:1653:1: ruleSelectExpressionWithOnlyAttributeOrConstant returns [EObject current=null] : ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* ) ;
    public final EObject ruleSelectExpressionWithOnlyAttributeOrConstant() throws RecognitionException {
        EObject current = null;

        Token lv_operators_1_1=null;
        Token lv_operators_1_2=null;
        Token lv_operators_1_3=null;
        Token lv_operators_1_4=null;
        EObject lv_expressions_0_0 = null;

        EObject lv_expressions_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1659:2: ( ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* ) )
            // InternalCQLParser.g:1660:2: ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* )
            {
            // InternalCQLParser.g:1660:2: ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* )
            // InternalCQLParser.g:1661:3: ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )*
            {
            // InternalCQLParser.g:1661:3: ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) )
            // InternalCQLParser.g:1662:4: (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute )
            {
            // InternalCQLParser.g:1662:4: (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute )
            // InternalCQLParser.g:1663:5: lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute
            {

            					newCompositeNode(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_30);
            lv_expressions_0_0=ruleExpressionComponentConstantOrAttribute();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule());
            					}
            					add(
            						current,
            						"expressions",
            						lv_expressions_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentConstantOrAttribute");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:1680:3: ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )*
            loop38:
            do {
                int alt38=2;
                int LA38_0 = input.LA(1);

                if ( ((LA38_0>=Asterisk && LA38_0<=PlusSign)||LA38_0==HyphenMinus||LA38_0==Solidus) ) {
                    alt38=1;
                }


                switch (alt38) {
            	case 1 :
            	    // InternalCQLParser.g:1681:4: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) )
            	    {
            	    // InternalCQLParser.g:1681:4: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) )
            	    // InternalCQLParser.g:1682:5: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
            	    {
            	    // InternalCQLParser.g:1682:5: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
            	    // InternalCQLParser.g:1683:6: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
            	    {
            	    // InternalCQLParser.g:1683:6: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
            	    int alt37=4;
            	    switch ( input.LA(1) ) {
            	    case PlusSign:
            	        {
            	        alt37=1;
            	        }
            	        break;
            	    case HyphenMinus:
            	        {
            	        alt37=2;
            	        }
            	        break;
            	    case Asterisk:
            	        {
            	        alt37=3;
            	        }
            	        break;
            	    case Solidus:
            	        {
            	        alt37=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 37, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt37) {
            	        case 1 :
            	            // InternalCQLParser.g:1684:7: lv_operators_1_1= PlusSign
            	            {
            	            lv_operators_1_1=(Token)match(input,PlusSign,FOLLOW_5); 

            	            							newLeafNode(lv_operators_1_1, grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getOperatorsPlusSignKeyword_1_0_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule());
            	            							}
            	            							addWithLastConsumed(current, "operators", lv_operators_1_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:1695:7: lv_operators_1_2= HyphenMinus
            	            {
            	            lv_operators_1_2=(Token)match(input,HyphenMinus,FOLLOW_5); 

            	            							newLeafNode(lv_operators_1_2, grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getOperatorsHyphenMinusKeyword_1_0_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule());
            	            							}
            	            							addWithLastConsumed(current, "operators", lv_operators_1_2, null);
            	            						

            	            }
            	            break;
            	        case 3 :
            	            // InternalCQLParser.g:1706:7: lv_operators_1_3= Asterisk
            	            {
            	            lv_operators_1_3=(Token)match(input,Asterisk,FOLLOW_5); 

            	            							newLeafNode(lv_operators_1_3, grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getOperatorsAsteriskKeyword_1_0_0_2());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule());
            	            							}
            	            							addWithLastConsumed(current, "operators", lv_operators_1_3, null);
            	            						

            	            }
            	            break;
            	        case 4 :
            	            // InternalCQLParser.g:1717:7: lv_operators_1_4= Solidus
            	            {
            	            lv_operators_1_4=(Token)match(input,Solidus,FOLLOW_5); 

            	            							newLeafNode(lv_operators_1_4, grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getOperatorsSolidusKeyword_1_0_0_3());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule());
            	            							}
            	            							addWithLastConsumed(current, "operators", lv_operators_1_4, null);
            	            						

            	            }
            	            break;

            	    }


            	    }


            	    }

            	    // InternalCQLParser.g:1730:4: ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) )
            	    // InternalCQLParser.g:1731:5: (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute )
            	    {
            	    // InternalCQLParser.g:1731:5: (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute )
            	    // InternalCQLParser.g:1732:6: lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute
            	    {

            	    						newCompositeNode(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_30);
            	    lv_expressions_2_0=ruleExpressionComponentConstantOrAttribute();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule());
            	    						}
            	    						add(
            	    							current,
            	    							"expressions",
            	    							lv_expressions_2_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentConstantOrAttribute");
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
    // $ANTLR end "ruleSelectExpressionWithOnlyAttributeOrConstant"


    // $ANTLR start "entryRuleFunction"
    // InternalCQLParser.g:1754:1: entryRuleFunction returns [EObject current=null] : iv_ruleFunction= ruleFunction EOF ;
    public final EObject entryRuleFunction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFunction = null;


        try {
            // InternalCQLParser.g:1754:49: (iv_ruleFunction= ruleFunction EOF )
            // InternalCQLParser.g:1755:2: iv_ruleFunction= ruleFunction EOF
            {
             newCompositeNode(grammarAccess.getFunctionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleFunction=ruleFunction();

            state._fsp--;

             current =iv_ruleFunction; 
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
    // $ANTLR end "entryRuleFunction"


    // $ANTLR start "ruleFunction"
    // InternalCQLParser.g:1761:1: ruleFunction returns [EObject current=null] : ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis ) ;
    public final EObject ruleFunction() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_value_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1767:2: ( ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis ) )
            // InternalCQLParser.g:1768:2: ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis )
            {
            // InternalCQLParser.g:1768:2: ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis )
            // InternalCQLParser.g:1769:3: () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis
            {
            // InternalCQLParser.g:1769:3: ()
            // InternalCQLParser.g:1770:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getFunctionAccess().getFunctionAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:1776:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQLParser.g:1777:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQLParser.g:1777:4: (lv_name_1_0= RULE_ID )
            // InternalCQLParser.g:1778:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_31); 

            					newLeafNode(lv_name_1_0, grammarAccess.getFunctionAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getFunctionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,LeftParenthesis,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getFunctionAccess().getLeftParenthesisKeyword_2());
            		
            // InternalCQLParser.g:1798:3: ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) )
            // InternalCQLParser.g:1799:4: (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1799:4: (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition )
            // InternalCQLParser.g:1800:5: lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getFunctionAccess().getValueSelectExpressionWithoutAliasDefinitionParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_19);
            lv_value_3_0=ruleSelectExpressionWithoutAliasDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getFunctionRule());
            					}
            					set(
            						current,
            						"value",
            						lv_value_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpressionWithoutAliasDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_4=(Token)match(input,RightParenthesis,FOLLOW_2); 

            			newLeafNode(otherlv_4, grammarAccess.getFunctionAccess().getRightParenthesisKeyword_4());
            		

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
    // $ANTLR end "ruleFunction"


    // $ANTLR start "entryRuleExpressionComponentConstantOrAttribute"
    // InternalCQLParser.g:1825:1: entryRuleExpressionComponentConstantOrAttribute returns [EObject current=null] : iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF ;
    public final EObject entryRuleExpressionComponentConstantOrAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentConstantOrAttribute = null;


        try {
            // InternalCQLParser.g:1825:79: (iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF )
            // InternalCQLParser.g:1826:2: iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF
            {
             newCompositeNode(grammarAccess.getExpressionComponentConstantOrAttributeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpressionComponentConstantOrAttribute=ruleExpressionComponentConstantOrAttribute();

            state._fsp--;

             current =iv_ruleExpressionComponentConstantOrAttribute; 
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
    // $ANTLR end "entryRuleExpressionComponentConstantOrAttribute"


    // $ANTLR start "ruleExpressionComponentConstantOrAttribute"
    // InternalCQLParser.g:1832:1: ruleExpressionComponentConstantOrAttribute returns [EObject current=null] : ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) ) ;
    public final EObject ruleExpressionComponentConstantOrAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_value_0_0 = null;

        EObject lv_value_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1838:2: ( ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) ) )
            // InternalCQLParser.g:1839:2: ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) )
            {
            // InternalCQLParser.g:1839:2: ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) )
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==FALSE||LA39_0==TRUE||(LA39_0>=RULE_INT && LA39_0<=RULE_STRING)) ) {
                alt39=1;
            }
            else if ( (LA39_0==RULE_ID) ) {
                alt39=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;
            }
            switch (alt39) {
                case 1 :
                    // InternalCQLParser.g:1840:3: ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQLParser.g:1840:3: ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQLParser.g:1841:4: (lv_value_0_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQLParser.g:1841:4: (lv_value_0_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQLParser.g:1842:5: lv_value_0_0= ruleAtomicWithoutAttributeRef
                    {

                    					newCompositeNode(grammarAccess.getExpressionComponentConstantOrAttributeAccess().getValueAtomicWithoutAttributeRefParserRuleCall_0_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_value_0_0=ruleAtomicWithoutAttributeRef();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getExpressionComponentConstantOrAttributeRule());
                    					}
                    					set(
                    						current,
                    						"value",
                    						lv_value_0_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AtomicWithoutAttributeRef");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1860:3: ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQLParser.g:1860:3: ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQLParser.g:1861:4: (lv_value_1_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQLParser.g:1861:4: (lv_value_1_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQLParser.g:1862:5: lv_value_1_0= ruleAttributeWithoutAliasDefinition
                    {

                    					newCompositeNode(grammarAccess.getExpressionComponentConstantOrAttributeAccess().getValueAttributeWithoutAliasDefinitionParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_value_1_0=ruleAttributeWithoutAliasDefinition();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getExpressionComponentConstantOrAttributeRule());
                    					}
                    					set(
                    						current,
                    						"value",
                    						lv_value_1_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
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
    // $ANTLR end "ruleExpressionComponentConstantOrAttribute"


    // $ANTLR start "entryRuleExpressionComponentFunctionOrConstant"
    // InternalCQLParser.g:1883:1: entryRuleExpressionComponentFunctionOrConstant returns [EObject current=null] : iv_ruleExpressionComponentFunctionOrConstant= ruleExpressionComponentFunctionOrConstant EOF ;
    public final EObject entryRuleExpressionComponentFunctionOrConstant() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentFunctionOrConstant = null;


        try {
            // InternalCQLParser.g:1883:78: (iv_ruleExpressionComponentFunctionOrConstant= ruleExpressionComponentFunctionOrConstant EOF )
            // InternalCQLParser.g:1884:2: iv_ruleExpressionComponentFunctionOrConstant= ruleExpressionComponentFunctionOrConstant EOF
            {
             newCompositeNode(grammarAccess.getExpressionComponentFunctionOrConstantRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpressionComponentFunctionOrConstant=ruleExpressionComponentFunctionOrConstant();

            state._fsp--;

             current =iv_ruleExpressionComponentFunctionOrConstant; 
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
    // $ANTLR end "entryRuleExpressionComponentFunctionOrConstant"


    // $ANTLR start "ruleExpressionComponentFunctionOrConstant"
    // InternalCQLParser.g:1890:1: ruleExpressionComponentFunctionOrConstant returns [EObject current=null] : ( (this_Function_0= ruleFunction () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) ) ;
    public final EObject ruleExpressionComponentFunctionOrConstant() throws RecognitionException {
        EObject current = null;

        EObject this_Function_0 = null;

        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1896:2: ( ( (this_Function_0= ruleFunction () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) ) )
            // InternalCQLParser.g:1897:2: ( (this_Function_0= ruleFunction () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) )
            {
            // InternalCQLParser.g:1897:2: ( (this_Function_0= ruleFunction () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) )
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==RULE_ID) ) {
                alt40=1;
            }
            else if ( (LA40_0==FALSE||LA40_0==TRUE||(LA40_0>=RULE_INT && LA40_0<=RULE_STRING)) ) {
                alt40=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 40, 0, input);

                throw nvae;
            }
            switch (alt40) {
                case 1 :
                    // InternalCQLParser.g:1898:3: (this_Function_0= ruleFunction () )
                    {
                    // InternalCQLParser.g:1898:3: (this_Function_0= ruleFunction () )
                    // InternalCQLParser.g:1899:4: this_Function_0= ruleFunction ()
                    {

                    				newCompositeNode(grammarAccess.getExpressionComponentFunctionOrConstantAccess().getFunctionParserRuleCall_0_0());
                    			
                    pushFollow(FOLLOW_2);
                    this_Function_0=ruleFunction();

                    state._fsp--;


                    				current = this_Function_0;
                    				afterParserOrEnumRuleCall();
                    			
                    // InternalCQLParser.g:1907:4: ()
                    // InternalCQLParser.g:1908:5: 
                    {

                    					current = forceCreateModelElementAndSet(
                    						grammarAccess.getExpressionComponentFunctionOrConstantAccess().getExpressionComponentValueAction_0_1(),
                    						current);
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1916:3: ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQLParser.g:1916:3: ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQLParser.g:1917:4: (lv_value_2_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQLParser.g:1917:4: (lv_value_2_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQLParser.g:1918:5: lv_value_2_0= ruleAtomicWithoutAttributeRef
                    {

                    					newCompositeNode(grammarAccess.getExpressionComponentFunctionOrConstantAccess().getValueAtomicWithoutAttributeRefParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_value_2_0=ruleAtomicWithoutAttributeRef();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getExpressionComponentFunctionOrConstantRule());
                    					}
                    					set(
                    						current,
                    						"value",
                    						lv_value_2_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AtomicWithoutAttributeRef");
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
    // $ANTLR end "ruleExpressionComponentFunctionOrConstant"


    // $ANTLR start "entryRuleExpressionComponentOnlyWithAttribute"
    // InternalCQLParser.g:1939:1: entryRuleExpressionComponentOnlyWithAttribute returns [EObject current=null] : iv_ruleExpressionComponentOnlyWithAttribute= ruleExpressionComponentOnlyWithAttribute EOF ;
    public final EObject entryRuleExpressionComponentOnlyWithAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentOnlyWithAttribute = null;


        try {
            // InternalCQLParser.g:1939:77: (iv_ruleExpressionComponentOnlyWithAttribute= ruleExpressionComponentOnlyWithAttribute EOF )
            // InternalCQLParser.g:1940:2: iv_ruleExpressionComponentOnlyWithAttribute= ruleExpressionComponentOnlyWithAttribute EOF
            {
             newCompositeNode(grammarAccess.getExpressionComponentOnlyWithAttributeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpressionComponentOnlyWithAttribute=ruleExpressionComponentOnlyWithAttribute();

            state._fsp--;

             current =iv_ruleExpressionComponentOnlyWithAttribute; 
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
    // $ANTLR end "entryRuleExpressionComponentOnlyWithAttribute"


    // $ANTLR start "ruleExpressionComponentOnlyWithAttribute"
    // InternalCQLParser.g:1946:1: ruleExpressionComponentOnlyWithAttribute returns [EObject current=null] : ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) ;
    public final EObject ruleExpressionComponentOnlyWithAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_value_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1952:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) )
            // InternalCQLParser.g:1953:2: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            {
            // InternalCQLParser.g:1953:2: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQLParser.g:1954:3: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1954:3: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQLParser.g:1955:4: lv_value_0_0= ruleAttributeWithoutAliasDefinition
            {

            				newCompositeNode(grammarAccess.getExpressionComponentOnlyWithAttributeAccess().getValueAttributeWithoutAliasDefinitionParserRuleCall_0());
            			
            pushFollow(FOLLOW_2);
            lv_value_0_0=ruleAttributeWithoutAliasDefinition();

            state._fsp--;


            				if (current==null) {
            					current = createModelElementForParent(grammarAccess.getExpressionComponentOnlyWithAttributeRule());
            				}
            				set(
            					current,
            					"value",
            					lv_value_0_0,
            					"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
            				afterParserOrEnumRuleCall();
            			

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
    // $ANTLR end "ruleExpressionComponentOnlyWithAttribute"


    // $ANTLR start "entryRuleExpressionComponentOnlyWithFunction"
    // InternalCQLParser.g:1975:1: entryRuleExpressionComponentOnlyWithFunction returns [EObject current=null] : iv_ruleExpressionComponentOnlyWithFunction= ruleExpressionComponentOnlyWithFunction EOF ;
    public final EObject entryRuleExpressionComponentOnlyWithFunction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentOnlyWithFunction = null;


        try {
            // InternalCQLParser.g:1975:76: (iv_ruleExpressionComponentOnlyWithFunction= ruleExpressionComponentOnlyWithFunction EOF )
            // InternalCQLParser.g:1976:2: iv_ruleExpressionComponentOnlyWithFunction= ruleExpressionComponentOnlyWithFunction EOF
            {
             newCompositeNode(grammarAccess.getExpressionComponentOnlyWithFunctionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpressionComponentOnlyWithFunction=ruleExpressionComponentOnlyWithFunction();

            state._fsp--;

             current =iv_ruleExpressionComponentOnlyWithFunction; 
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
    // $ANTLR end "entryRuleExpressionComponentOnlyWithFunction"


    // $ANTLR start "ruleExpressionComponentOnlyWithFunction"
    // InternalCQLParser.g:1982:1: ruleExpressionComponentOnlyWithFunction returns [EObject current=null] : (this_Function_0= ruleFunction () ) ;
    public final EObject ruleExpressionComponentOnlyWithFunction() throws RecognitionException {
        EObject current = null;

        EObject this_Function_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1988:2: ( (this_Function_0= ruleFunction () ) )
            // InternalCQLParser.g:1989:2: (this_Function_0= ruleFunction () )
            {
            // InternalCQLParser.g:1989:2: (this_Function_0= ruleFunction () )
            // InternalCQLParser.g:1990:3: this_Function_0= ruleFunction ()
            {

            			newCompositeNode(grammarAccess.getExpressionComponentOnlyWithFunctionAccess().getFunctionParserRuleCall_0());
            		
            pushFollow(FOLLOW_2);
            this_Function_0=ruleFunction();

            state._fsp--;


            			current = this_Function_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:1998:3: ()
            // InternalCQLParser.g:1999:4: 
            {

            				current = forceCreateModelElementAndSet(
            					grammarAccess.getExpressionComponentOnlyWithFunctionAccess().getExpressionComponentValueAction_1(),
            					current);
            			

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
    // $ANTLR end "ruleExpressionComponentOnlyWithFunction"


    // $ANTLR start "entryRuleAlias"
    // InternalCQLParser.g:2009:1: entryRuleAlias returns [EObject current=null] : iv_ruleAlias= ruleAlias EOF ;
    public final EObject entryRuleAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAlias = null;


        try {
            // InternalCQLParser.g:2009:46: (iv_ruleAlias= ruleAlias EOF )
            // InternalCQLParser.g:2010:2: iv_ruleAlias= ruleAlias EOF
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
    // InternalCQLParser.g:2016:1: ruleAlias returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2022:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQLParser.g:2023:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQLParser.g:2023:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:2024:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:2024:3: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:2025:4: lv_name_0_0= RULE_ID
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
            					"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            			

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


    // $ANTLR start "entryRuleCreateParameters"
    // InternalCQLParser.g:2044:1: entryRuleCreateParameters returns [EObject current=null] : iv_ruleCreateParameters= ruleCreateParameters EOF ;
    public final EObject entryRuleCreateParameters() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateParameters = null;


        try {
            // InternalCQLParser.g:2044:57: (iv_ruleCreateParameters= ruleCreateParameters EOF )
            // InternalCQLParser.g:2045:2: iv_ruleCreateParameters= ruleCreateParameters EOF
            {
             newCompositeNode(grammarAccess.getCreateParametersRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateParameters=ruleCreateParameters();

            state._fsp--;

             current =iv_ruleCreateParameters; 
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
    // $ANTLR end "entryRuleCreateParameters"


    // $ANTLR start "ruleCreateParameters"
    // InternalCQLParser.g:2051:1: ruleCreateParameters returns [EObject current=null] : (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis ) ;
    public final EObject ruleCreateParameters() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_wrapper_1_0=null;
        Token otherlv_2=null;
        Token lv_protocol_3_0=null;
        Token otherlv_4=null;
        Token lv_transport_5_0=null;
        Token otherlv_6=null;
        Token lv_datahandler_7_0=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token lv_keys_10_0=null;
        Token lv_values_11_0=null;
        Token otherlv_12=null;
        Token lv_keys_13_0=null;
        Token lv_values_14_0=null;
        Token otherlv_15=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2057:2: ( (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis ) )
            // InternalCQLParser.g:2058:2: (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis )
            {
            // InternalCQLParser.g:2058:2: (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis )
            // InternalCQLParser.g:2059:3: otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis
            {
            otherlv_0=(Token)match(input,WRAPPER,FOLLOW_32); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateParametersAccess().getWRAPPERKeyword_0());
            		
            // InternalCQLParser.g:2063:3: ( (lv_wrapper_1_0= RULE_STRING ) )
            // InternalCQLParser.g:2064:4: (lv_wrapper_1_0= RULE_STRING )
            {
            // InternalCQLParser.g:2064:4: (lv_wrapper_1_0= RULE_STRING )
            // InternalCQLParser.g:2065:5: lv_wrapper_1_0= RULE_STRING
            {
            lv_wrapper_1_0=(Token)match(input,RULE_STRING,FOLLOW_33); 

            					newLeafNode(lv_wrapper_1_0, grammarAccess.getCreateParametersAccess().getWrapperSTRINGTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateParametersRule());
            					}
            					setWithLastConsumed(
            						current,
            						"wrapper",
            						lv_wrapper_1_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_2=(Token)match(input,PROTOCOL,FOLLOW_32); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateParametersAccess().getPROTOCOLKeyword_2());
            		
            // InternalCQLParser.g:2085:3: ( (lv_protocol_3_0= RULE_STRING ) )
            // InternalCQLParser.g:2086:4: (lv_protocol_3_0= RULE_STRING )
            {
            // InternalCQLParser.g:2086:4: (lv_protocol_3_0= RULE_STRING )
            // InternalCQLParser.g:2087:5: lv_protocol_3_0= RULE_STRING
            {
            lv_protocol_3_0=(Token)match(input,RULE_STRING,FOLLOW_34); 

            					newLeafNode(lv_protocol_3_0, grammarAccess.getCreateParametersAccess().getProtocolSTRINGTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateParametersRule());
            					}
            					setWithLastConsumed(
            						current,
            						"protocol",
            						lv_protocol_3_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_4=(Token)match(input,TRANSPORT,FOLLOW_32); 

            			newLeafNode(otherlv_4, grammarAccess.getCreateParametersAccess().getTRANSPORTKeyword_4());
            		
            // InternalCQLParser.g:2107:3: ( (lv_transport_5_0= RULE_STRING ) )
            // InternalCQLParser.g:2108:4: (lv_transport_5_0= RULE_STRING )
            {
            // InternalCQLParser.g:2108:4: (lv_transport_5_0= RULE_STRING )
            // InternalCQLParser.g:2109:5: lv_transport_5_0= RULE_STRING
            {
            lv_transport_5_0=(Token)match(input,RULE_STRING,FOLLOW_35); 

            					newLeafNode(lv_transport_5_0, grammarAccess.getCreateParametersAccess().getTransportSTRINGTerminalRuleCall_5_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateParametersRule());
            					}
            					setWithLastConsumed(
            						current,
            						"transport",
            						lv_transport_5_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_6=(Token)match(input,DATAHANDLER,FOLLOW_32); 

            			newLeafNode(otherlv_6, grammarAccess.getCreateParametersAccess().getDATAHANDLERKeyword_6());
            		
            // InternalCQLParser.g:2129:3: ( (lv_datahandler_7_0= RULE_STRING ) )
            // InternalCQLParser.g:2130:4: (lv_datahandler_7_0= RULE_STRING )
            {
            // InternalCQLParser.g:2130:4: (lv_datahandler_7_0= RULE_STRING )
            // InternalCQLParser.g:2131:5: lv_datahandler_7_0= RULE_STRING
            {
            lv_datahandler_7_0=(Token)match(input,RULE_STRING,FOLLOW_36); 

            					newLeafNode(lv_datahandler_7_0, grammarAccess.getCreateParametersAccess().getDatahandlerSTRINGTerminalRuleCall_7_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateParametersRule());
            					}
            					setWithLastConsumed(
            						current,
            						"datahandler",
            						lv_datahandler_7_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_8=(Token)match(input,OPTIONS,FOLLOW_31); 

            			newLeafNode(otherlv_8, grammarAccess.getCreateParametersAccess().getOPTIONSKeyword_8());
            		
            otherlv_9=(Token)match(input,LeftParenthesis,FOLLOW_32); 

            			newLeafNode(otherlv_9, grammarAccess.getCreateParametersAccess().getLeftParenthesisKeyword_9());
            		
            // InternalCQLParser.g:2155:3: ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+
            int cnt41=0;
            loop41:
            do {
                int alt41=2;
                int LA41_0 = input.LA(1);

                if ( (LA41_0==RULE_STRING) ) {
                    alt41=1;
                }


                switch (alt41) {
            	case 1 :
            	    // InternalCQLParser.g:2156:4: ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) )
            	    {
            	    // InternalCQLParser.g:2156:4: ( (lv_keys_10_0= RULE_STRING ) )
            	    // InternalCQLParser.g:2157:5: (lv_keys_10_0= RULE_STRING )
            	    {
            	    // InternalCQLParser.g:2157:5: (lv_keys_10_0= RULE_STRING )
            	    // InternalCQLParser.g:2158:6: lv_keys_10_0= RULE_STRING
            	    {
            	    lv_keys_10_0=(Token)match(input,RULE_STRING,FOLLOW_32); 

            	    						newLeafNode(lv_keys_10_0, grammarAccess.getCreateParametersAccess().getKeysSTRINGTerminalRuleCall_10_0_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getCreateParametersRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"keys",
            	    							lv_keys_10_0,
            	    							"org.eclipse.xtext.common.Terminals.STRING");
            	    					

            	    }


            	    }

            	    // InternalCQLParser.g:2174:4: ( (lv_values_11_0= RULE_STRING ) )
            	    // InternalCQLParser.g:2175:5: (lv_values_11_0= RULE_STRING )
            	    {
            	    // InternalCQLParser.g:2175:5: (lv_values_11_0= RULE_STRING )
            	    // InternalCQLParser.g:2176:6: lv_values_11_0= RULE_STRING
            	    {
            	    lv_values_11_0=(Token)match(input,RULE_STRING,FOLLOW_37); 

            	    						newLeafNode(lv_values_11_0, grammarAccess.getCreateParametersAccess().getValuesSTRINGTerminalRuleCall_10_1_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getCreateParametersRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"values",
            	    							lv_values_11_0,
            	    							"org.eclipse.xtext.common.Terminals.STRING");
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt41 >= 1 ) break loop41;
                        EarlyExitException eee =
                            new EarlyExitException(41, input);
                        throw eee;
                }
                cnt41++;
            } while (true);

            // InternalCQLParser.g:2193:3: (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==Comma) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // InternalCQLParser.g:2194:4: otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) )
                    {
                    otherlv_12=(Token)match(input,Comma,FOLLOW_32); 

                    				newLeafNode(otherlv_12, grammarAccess.getCreateParametersAccess().getCommaKeyword_11_0());
                    			
                    // InternalCQLParser.g:2198:4: ( (lv_keys_13_0= RULE_STRING ) )
                    // InternalCQLParser.g:2199:5: (lv_keys_13_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:2199:5: (lv_keys_13_0= RULE_STRING )
                    // InternalCQLParser.g:2200:6: lv_keys_13_0= RULE_STRING
                    {
                    lv_keys_13_0=(Token)match(input,RULE_STRING,FOLLOW_32); 

                    						newLeafNode(lv_keys_13_0, grammarAccess.getCreateParametersAccess().getKeysSTRINGTerminalRuleCall_11_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateParametersRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"keys",
                    							lv_keys_13_0,
                    							"org.eclipse.xtext.common.Terminals.STRING");
                    					

                    }


                    }

                    // InternalCQLParser.g:2216:4: ( (lv_values_14_0= RULE_STRING ) )
                    // InternalCQLParser.g:2217:5: (lv_values_14_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:2217:5: (lv_values_14_0= RULE_STRING )
                    // InternalCQLParser.g:2218:6: lv_values_14_0= RULE_STRING
                    {
                    lv_values_14_0=(Token)match(input,RULE_STRING,FOLLOW_19); 

                    						newLeafNode(lv_values_14_0, grammarAccess.getCreateParametersAccess().getValuesSTRINGTerminalRuleCall_11_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateParametersRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"values",
                    							lv_values_14_0,
                    							"org.eclipse.xtext.common.Terminals.STRING");
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_15=(Token)match(input,RightParenthesis,FOLLOW_2); 

            			newLeafNode(otherlv_15, grammarAccess.getCreateParametersAccess().getRightParenthesisKeyword_12());
            		

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
    // $ANTLR end "ruleCreateParameters"


    // $ANTLR start "entryRuleAttributeDefinition"
    // InternalCQLParser.g:2243:1: entryRuleAttributeDefinition returns [EObject current=null] : iv_ruleAttributeDefinition= ruleAttributeDefinition EOF ;
    public final EObject entryRuleAttributeDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeDefinition = null;


        try {
            // InternalCQLParser.g:2243:60: (iv_ruleAttributeDefinition= ruleAttributeDefinition EOF )
            // InternalCQLParser.g:2244:2: iv_ruleAttributeDefinition= ruleAttributeDefinition EOF
            {
             newCompositeNode(grammarAccess.getAttributeDefinitionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAttributeDefinition=ruleAttributeDefinition();

            state._fsp--;

             current =iv_ruleAttributeDefinition; 
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
    // $ANTLR end "entryRuleAttributeDefinition"


    // $ANTLR start "ruleAttributeDefinition"
    // InternalCQLParser.g:2250:1: ruleAttributeDefinition returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis ) ;
    public final EObject ruleAttributeDefinition() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token lv_arguments_2_0=null;
        Token lv_arguments_3_0=null;
        Token otherlv_4=null;
        Token lv_arguments_5_0=null;
        Token lv_arguments_6_0=null;
        Token otherlv_7=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2256:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis ) )
            // InternalCQLParser.g:2257:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis )
            {
            // InternalCQLParser.g:2257:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis )
            // InternalCQLParser.g:2258:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis
            {
            // InternalCQLParser.g:2258:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:2259:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:2259:4: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:2260:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_31); 

            					newLeafNode(lv_name_0_0, grammarAccess.getAttributeDefinitionAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAttributeDefinitionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,LeftParenthesis,FOLLOW_15); 

            			newLeafNode(otherlv_1, grammarAccess.getAttributeDefinitionAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQLParser.g:2280:3: ( (lv_arguments_2_0= RULE_ID ) )
            // InternalCQLParser.g:2281:4: (lv_arguments_2_0= RULE_ID )
            {
            // InternalCQLParser.g:2281:4: (lv_arguments_2_0= RULE_ID )
            // InternalCQLParser.g:2282:5: lv_arguments_2_0= RULE_ID
            {
            lv_arguments_2_0=(Token)match(input,RULE_ID,FOLLOW_15); 

            					newLeafNode(lv_arguments_2_0, grammarAccess.getAttributeDefinitionAccess().getArgumentsIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAttributeDefinitionRule());
            					}
            					addWithLastConsumed(
            						current,
            						"arguments",
            						lv_arguments_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:2298:3: ( (lv_arguments_3_0= RULE_ID ) )
            // InternalCQLParser.g:2299:4: (lv_arguments_3_0= RULE_ID )
            {
            // InternalCQLParser.g:2299:4: (lv_arguments_3_0= RULE_ID )
            // InternalCQLParser.g:2300:5: lv_arguments_3_0= RULE_ID
            {
            lv_arguments_3_0=(Token)match(input,RULE_ID,FOLLOW_38); 

            					newLeafNode(lv_arguments_3_0, grammarAccess.getAttributeDefinitionAccess().getArgumentsIDTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAttributeDefinitionRule());
            					}
            					addWithLastConsumed(
            						current,
            						"arguments",
            						lv_arguments_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:2316:3: (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )*
            loop43:
            do {
                int alt43=2;
                int LA43_0 = input.LA(1);

                if ( (LA43_0==Comma) ) {
                    alt43=1;
                }


                switch (alt43) {
            	case 1 :
            	    // InternalCQLParser.g:2317:4: otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) )
            	    {
            	    otherlv_4=(Token)match(input,Comma,FOLLOW_15); 

            	    				newLeafNode(otherlv_4, grammarAccess.getAttributeDefinitionAccess().getCommaKeyword_4_0());
            	    			
            	    // InternalCQLParser.g:2321:4: ( (lv_arguments_5_0= RULE_ID ) )
            	    // InternalCQLParser.g:2322:5: (lv_arguments_5_0= RULE_ID )
            	    {
            	    // InternalCQLParser.g:2322:5: (lv_arguments_5_0= RULE_ID )
            	    // InternalCQLParser.g:2323:6: lv_arguments_5_0= RULE_ID
            	    {
            	    lv_arguments_5_0=(Token)match(input,RULE_ID,FOLLOW_15); 

            	    						newLeafNode(lv_arguments_5_0, grammarAccess.getAttributeDefinitionAccess().getArgumentsIDTerminalRuleCall_4_1_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getAttributeDefinitionRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"arguments",
            	    							lv_arguments_5_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            	    					

            	    }


            	    }

            	    // InternalCQLParser.g:2339:4: ( (lv_arguments_6_0= RULE_ID ) )
            	    // InternalCQLParser.g:2340:5: (lv_arguments_6_0= RULE_ID )
            	    {
            	    // InternalCQLParser.g:2340:5: (lv_arguments_6_0= RULE_ID )
            	    // InternalCQLParser.g:2341:6: lv_arguments_6_0= RULE_ID
            	    {
            	    lv_arguments_6_0=(Token)match(input,RULE_ID,FOLLOW_38); 

            	    						newLeafNode(lv_arguments_6_0, grammarAccess.getAttributeDefinitionAccess().getArgumentsIDTerminalRuleCall_4_2_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getAttributeDefinitionRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"arguments",
            	    							lv_arguments_6_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop43;
                }
            } while (true);

            otherlv_7=(Token)match(input,RightParenthesis,FOLLOW_2); 

            			newLeafNode(otherlv_7, grammarAccess.getAttributeDefinitionAccess().getRightParenthesisKeyword_5());
            		

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
    // $ANTLR end "ruleAttributeDefinition"


    // $ANTLR start "entryRuleCreateStream1"
    // InternalCQLParser.g:2366:1: entryRuleCreateStream1 returns [EObject current=null] : iv_ruleCreateStream1= ruleCreateStream1 EOF ;
    public final EObject entryRuleCreateStream1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStream1 = null;


        try {
            // InternalCQLParser.g:2366:54: (iv_ruleCreateStream1= ruleCreateStream1 EOF )
            // InternalCQLParser.g:2367:2: iv_ruleCreateStream1= ruleCreateStream1 EOF
            {
             newCompositeNode(grammarAccess.getCreateStream1Rule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateStream1=ruleCreateStream1();

            state._fsp--;

             current =iv_ruleCreateStream1; 
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
    // $ANTLR end "entryRuleCreateStream1"


    // $ANTLR start "ruleCreateStream1"
    // InternalCQLParser.g:2373:1: ruleCreateStream1 returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateStream1() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2379:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQLParser.g:2380:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQLParser.g:2380:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQLParser.g:2381:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            // InternalCQLParser.g:2381:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2382:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2382:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2383:5: lv_keyword_0_0= ruleCreateKeyword
            {

            					newCompositeNode(grammarAccess.getCreateStream1Access().getKeywordCreateKeywordEnumRuleCall_0_0());
            				
            pushFollow(FOLLOW_39);
            lv_keyword_0_0=ruleCreateKeyword();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateStream1Rule());
            					}
            					set(
            						current,
            						"keyword",
            						lv_keyword_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateKeyword");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,STREAM,FOLLOW_15); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStream1Access().getSTREAMKeyword_1());
            		
            // InternalCQLParser.g:2404:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2405:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2405:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2406:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateStream1Access().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_40);
            lv_attributes_2_0=ruleAttributeDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateStream1Rule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:2423:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQLParser.g:2424:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQLParser.g:2424:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQLParser.g:2425:5: lv_pars_3_0= ruleCreateParameters
            {

            					newCompositeNode(grammarAccess.getCreateStream1Access().getParsCreateParametersParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_pars_3_0=ruleCreateParameters();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateStream1Rule());
            					}
            					set(
            						current,
            						"pars",
            						lv_pars_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateParameters");
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
    // $ANTLR end "ruleCreateStream1"


    // $ANTLR start "entryRuleCreateSink1"
    // InternalCQLParser.g:2446:1: entryRuleCreateSink1 returns [EObject current=null] : iv_ruleCreateSink1= ruleCreateSink1 EOF ;
    public final EObject entryRuleCreateSink1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateSink1 = null;


        try {
            // InternalCQLParser.g:2446:52: (iv_ruleCreateSink1= ruleCreateSink1 EOF )
            // InternalCQLParser.g:2447:2: iv_ruleCreateSink1= ruleCreateSink1 EOF
            {
             newCompositeNode(grammarAccess.getCreateSink1Rule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateSink1=ruleCreateSink1();

            state._fsp--;

             current =iv_ruleCreateSink1; 
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
    // $ANTLR end "entryRuleCreateSink1"


    // $ANTLR start "ruleCreateSink1"
    // InternalCQLParser.g:2453:1: ruleCreateSink1 returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateSink1() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2459:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQLParser.g:2460:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQLParser.g:2460:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQLParser.g:2461:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            // InternalCQLParser.g:2461:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2462:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2462:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2463:5: lv_keyword_0_0= ruleCreateKeyword
            {

            					newCompositeNode(grammarAccess.getCreateSink1Access().getKeywordCreateKeywordEnumRuleCall_0_0());
            				
            pushFollow(FOLLOW_41);
            lv_keyword_0_0=ruleCreateKeyword();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateSink1Rule());
            					}
            					set(
            						current,
            						"keyword",
            						lv_keyword_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateKeyword");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,SINK,FOLLOW_15); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateSink1Access().getSINKKeyword_1());
            		
            // InternalCQLParser.g:2484:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2485:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2485:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2486:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateSink1Access().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_40);
            lv_attributes_2_0=ruleAttributeDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateSink1Rule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:2503:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQLParser.g:2504:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQLParser.g:2504:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQLParser.g:2505:5: lv_pars_3_0= ruleCreateParameters
            {

            					newCompositeNode(grammarAccess.getCreateSink1Access().getParsCreateParametersParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_pars_3_0=ruleCreateParameters();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateSink1Rule());
            					}
            					set(
            						current,
            						"pars",
            						lv_pars_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateParameters");
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
    // $ANTLR end "ruleCreateSink1"


    // $ANTLR start "entryRuleCreateStreamChannel"
    // InternalCQLParser.g:2526:1: entryRuleCreateStreamChannel returns [EObject current=null] : iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF ;
    public final EObject entryRuleCreateStreamChannel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamChannel = null;


        try {
            // InternalCQLParser.g:2526:60: (iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF )
            // InternalCQLParser.g:2527:2: iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF
            {
             newCompositeNode(grammarAccess.getCreateStreamChannelRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateStreamChannel=ruleCreateStreamChannel();

            state._fsp--;

             current =iv_ruleCreateStreamChannel; 
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
    // $ANTLR end "entryRuleCreateStreamChannel"


    // $ANTLR start "ruleCreateStreamChannel"
    // InternalCQLParser.g:2533:1: ruleCreateStreamChannel returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) ) ;
    public final EObject ruleCreateStreamChannel() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token lv_host_4_0=null;
        Token otherlv_5=null;
        Token lv_port_6_0=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2539:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) ) )
            // InternalCQLParser.g:2540:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) )
            {
            // InternalCQLParser.g:2540:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) )
            // InternalCQLParser.g:2541:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) )
            {
            // InternalCQLParser.g:2541:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2542:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2542:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2543:5: lv_keyword_0_0= ruleCreateKeyword
            {

            					newCompositeNode(grammarAccess.getCreateStreamChannelAccess().getKeywordCreateKeywordEnumRuleCall_0_0());
            				
            pushFollow(FOLLOW_39);
            lv_keyword_0_0=ruleCreateKeyword();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateStreamChannelRule());
            					}
            					set(
            						current,
            						"keyword",
            						lv_keyword_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateKeyword");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,STREAM,FOLLOW_15); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStreamChannelAccess().getSTREAMKeyword_1());
            		
            // InternalCQLParser.g:2564:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2565:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2565:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2566:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateStreamChannelAccess().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_42);
            lv_attributes_2_0=ruleAttributeDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateStreamChannelRule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_3=(Token)match(input,CHANNEL,FOLLOW_15); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateStreamChannelAccess().getCHANNELKeyword_3());
            		
            // InternalCQLParser.g:2587:3: ( (lv_host_4_0= RULE_ID ) )
            // InternalCQLParser.g:2588:4: (lv_host_4_0= RULE_ID )
            {
            // InternalCQLParser.g:2588:4: (lv_host_4_0= RULE_ID )
            // InternalCQLParser.g:2589:5: lv_host_4_0= RULE_ID
            {
            lv_host_4_0=(Token)match(input,RULE_ID,FOLLOW_43); 

            					newLeafNode(lv_host_4_0, grammarAccess.getCreateStreamChannelAccess().getHostIDTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateStreamChannelRule());
            					}
            					setWithLastConsumed(
            						current,
            						"host",
            						lv_host_4_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_5=(Token)match(input,Colon,FOLLOW_44); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateStreamChannelAccess().getColonKeyword_5());
            		
            // InternalCQLParser.g:2609:3: ( (lv_port_6_0= RULE_INT ) )
            // InternalCQLParser.g:2610:4: (lv_port_6_0= RULE_INT )
            {
            // InternalCQLParser.g:2610:4: (lv_port_6_0= RULE_INT )
            // InternalCQLParser.g:2611:5: lv_port_6_0= RULE_INT
            {
            lv_port_6_0=(Token)match(input,RULE_INT,FOLLOW_2); 

            					newLeafNode(lv_port_6_0, grammarAccess.getCreateStreamChannelAccess().getPortINTTerminalRuleCall_6_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateStreamChannelRule());
            					}
            					setWithLastConsumed(
            						current,
            						"port",
            						lv_port_6_0,
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
    // $ANTLR end "ruleCreateStreamChannel"


    // $ANTLR start "entryRuleCreateStreamFile"
    // InternalCQLParser.g:2631:1: entryRuleCreateStreamFile returns [EObject current=null] : iv_ruleCreateStreamFile= ruleCreateStreamFile EOF ;
    public final EObject entryRuleCreateStreamFile() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamFile = null;


        try {
            // InternalCQLParser.g:2631:57: (iv_ruleCreateStreamFile= ruleCreateStreamFile EOF )
            // InternalCQLParser.g:2632:2: iv_ruleCreateStreamFile= ruleCreateStreamFile EOF
            {
             newCompositeNode(grammarAccess.getCreateStreamFileRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateStreamFile=ruleCreateStreamFile();

            state._fsp--;

             current =iv_ruleCreateStreamFile; 
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
    // $ANTLR end "entryRuleCreateStreamFile"


    // $ANTLR start "ruleCreateStreamFile"
    // InternalCQLParser.g:2638:1: ruleCreateStreamFile returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) ) ;
    public final EObject ruleCreateStreamFile() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token lv_filename_4_0=null;
        Token otherlv_5=null;
        Token lv_type_6_0=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2644:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:2645:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) )
            {
            // InternalCQLParser.g:2645:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) )
            // InternalCQLParser.g:2646:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) )
            {
            // InternalCQLParser.g:2646:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2647:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2647:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2648:5: lv_keyword_0_0= ruleCreateKeyword
            {

            					newCompositeNode(grammarAccess.getCreateStreamFileAccess().getKeywordCreateKeywordEnumRuleCall_0_0());
            				
            pushFollow(FOLLOW_39);
            lv_keyword_0_0=ruleCreateKeyword();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateStreamFileRule());
            					}
            					set(
            						current,
            						"keyword",
            						lv_keyword_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateKeyword");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,STREAM,FOLLOW_15); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStreamFileAccess().getSTREAMKeyword_1());
            		
            // InternalCQLParser.g:2669:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2670:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2670:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2671:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateStreamFileAccess().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_45);
            lv_attributes_2_0=ruleAttributeDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateStreamFileRule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_3=(Token)match(input,FILE,FOLLOW_32); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateStreamFileAccess().getFILEKeyword_3());
            		
            // InternalCQLParser.g:2692:3: ( (lv_filename_4_0= RULE_STRING ) )
            // InternalCQLParser.g:2693:4: (lv_filename_4_0= RULE_STRING )
            {
            // InternalCQLParser.g:2693:4: (lv_filename_4_0= RULE_STRING )
            // InternalCQLParser.g:2694:5: lv_filename_4_0= RULE_STRING
            {
            lv_filename_4_0=(Token)match(input,RULE_STRING,FOLLOW_24); 

            					newLeafNode(lv_filename_4_0, grammarAccess.getCreateStreamFileAccess().getFilenameSTRINGTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateStreamFileRule());
            					}
            					setWithLastConsumed(
            						current,
            						"filename",
            						lv_filename_4_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_5=(Token)match(input,AS,FOLLOW_15); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateStreamFileAccess().getASKeyword_5());
            		
            // InternalCQLParser.g:2714:3: ( (lv_type_6_0= RULE_ID ) )
            // InternalCQLParser.g:2715:4: (lv_type_6_0= RULE_ID )
            {
            // InternalCQLParser.g:2715:4: (lv_type_6_0= RULE_ID )
            // InternalCQLParser.g:2716:5: lv_type_6_0= RULE_ID
            {
            lv_type_6_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(lv_type_6_0, grammarAccess.getCreateStreamFileAccess().getTypeIDTerminalRuleCall_6_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateStreamFileRule());
            					}
            					setWithLastConsumed(
            						current,
            						"type",
            						lv_type_6_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

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
    // $ANTLR end "ruleCreateStreamFile"


    // $ANTLR start "entryRuleCreateView"
    // InternalCQLParser.g:2736:1: entryRuleCreateView returns [EObject current=null] : iv_ruleCreateView= ruleCreateView EOF ;
    public final EObject entryRuleCreateView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateView = null;


        try {
            // InternalCQLParser.g:2736:51: (iv_ruleCreateView= ruleCreateView EOF )
            // InternalCQLParser.g:2737:2: iv_ruleCreateView= ruleCreateView EOF
            {
             newCompositeNode(grammarAccess.getCreateViewRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateView=ruleCreateView();

            state._fsp--;

             current =iv_ruleCreateView; 
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
    // $ANTLR end "entryRuleCreateView"


    // $ANTLR start "ruleCreateView"
    // InternalCQLParser.g:2743:1: ruleCreateView returns [EObject current=null] : (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleCreateView() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        EObject lv_select_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2749:2: ( (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) ) )
            // InternalCQLParser.g:2750:2: (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) )
            {
            // InternalCQLParser.g:2750:2: (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) )
            // InternalCQLParser.g:2751:3: otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) )
            {
            otherlv_0=(Token)match(input,VIEW,FOLLOW_15); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateViewAccess().getVIEWKeyword_0());
            		
            // InternalCQLParser.g:2755:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQLParser.g:2756:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQLParser.g:2756:4: (lv_name_1_0= RULE_ID )
            // InternalCQLParser.g:2757:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_6); 

            					newLeafNode(lv_name_1_0, grammarAccess.getCreateViewAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateViewRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,FROM,FOLLOW_9); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateViewAccess().getFROMKeyword_2());
            		
            // InternalCQLParser.g:2777:3: ( (lv_select_3_0= ruleNestedStatement ) )
            // InternalCQLParser.g:2778:4: (lv_select_3_0= ruleNestedStatement )
            {
            // InternalCQLParser.g:2778:4: (lv_select_3_0= ruleNestedStatement )
            // InternalCQLParser.g:2779:5: lv_select_3_0= ruleNestedStatement
            {

            					newCompositeNode(grammarAccess.getCreateViewAccess().getSelectNestedStatementParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_select_3_0=ruleNestedStatement();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateViewRule());
            					}
            					set(
            						current,
            						"select",
            						lv_select_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.NestedStatement");
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
    // $ANTLR end "ruleCreateView"


    // $ANTLR start "entryRuleStreamTo"
    // InternalCQLParser.g:2800:1: entryRuleStreamTo returns [EObject current=null] : iv_ruleStreamTo= ruleStreamTo EOF ;
    public final EObject entryRuleStreamTo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStreamTo = null;


        try {
            // InternalCQLParser.g:2800:49: (iv_ruleStreamTo= ruleStreamTo EOF )
            // InternalCQLParser.g:2801:2: iv_ruleStreamTo= ruleStreamTo EOF
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
    // InternalCQLParser.g:2807:1: ruleStreamTo returns [EObject current=null] : (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) ;
    public final EObject ruleStreamTo() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token lv_inputname_4_0=null;
        EObject lv_statement_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2813:2: ( (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) )
            // InternalCQLParser.g:2814:2: (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            {
            // InternalCQLParser.g:2814:2: (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:2815:3: otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            {
            otherlv_0=(Token)match(input,STREAM,FOLLOW_46); 

            			newLeafNode(otherlv_0, grammarAccess.getStreamToAccess().getSTREAMKeyword_0());
            		
            otherlv_1=(Token)match(input,TO,FOLLOW_15); 

            			newLeafNode(otherlv_1, grammarAccess.getStreamToAccess().getTOKeyword_1());
            		
            // InternalCQLParser.g:2823:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCQLParser.g:2824:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCQLParser.g:2824:4: (lv_name_2_0= RULE_ID )
            // InternalCQLParser.g:2825:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_47); 

            					newLeafNode(lv_name_2_0, grammarAccess.getStreamToAccess().getNameIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getStreamToRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:2841:3: ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==SELECT) ) {
                alt44=1;
            }
            else if ( (LA44_0==RULE_ID) ) {
                alt44=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 44, 0, input);

                throw nvae;
            }
            switch (alt44) {
                case 1 :
                    // InternalCQLParser.g:2842:4: ( (lv_statement_3_0= ruleSelect ) )
                    {
                    // InternalCQLParser.g:2842:4: ( (lv_statement_3_0= ruleSelect ) )
                    // InternalCQLParser.g:2843:5: (lv_statement_3_0= ruleSelect )
                    {
                    // InternalCQLParser.g:2843:5: (lv_statement_3_0= ruleSelect )
                    // InternalCQLParser.g:2844:6: lv_statement_3_0= ruleSelect
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
                    // InternalCQLParser.g:2862:4: ( (lv_inputname_4_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:2862:4: ( (lv_inputname_4_0= RULE_ID ) )
                    // InternalCQLParser.g:2863:5: (lv_inputname_4_0= RULE_ID )
                    {
                    // InternalCQLParser.g:2863:5: (lv_inputname_4_0= RULE_ID )
                    // InternalCQLParser.g:2864:6: lv_inputname_4_0= RULE_ID
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
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

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


    // $ANTLR start "entryRuleDropCommand"
    // InternalCQLParser.g:2885:1: entryRuleDropCommand returns [EObject current=null] : iv_ruleDropCommand= ruleDropCommand EOF ;
    public final EObject entryRuleDropCommand() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDropCommand = null;


        try {
            // InternalCQLParser.g:2885:52: (iv_ruleDropCommand= ruleDropCommand EOF )
            // InternalCQLParser.g:2886:2: iv_ruleDropCommand= ruleDropCommand EOF
            {
             newCompositeNode(grammarAccess.getDropCommandRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDropCommand=ruleDropCommand();

            state._fsp--;

             current =iv_ruleDropCommand; 
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
    // $ANTLR end "entryRuleDropCommand"


    // $ANTLR start "ruleDropCommand"
    // InternalCQLParser.g:2892:1: ruleDropCommand returns [EObject current=null] : ( () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )? ) ;
    public final EObject ruleDropCommand() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_name_2_1=null;
        Token lv_name_2_2=null;
        Token lv_name_2_3=null;
        Token lv_stream_3_0=null;
        Token lv_exists_4_0=null;
        Token otherlv_5=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2898:2: ( ( () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )? ) )
            // InternalCQLParser.g:2899:2: ( () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )? )
            {
            // InternalCQLParser.g:2899:2: ( () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )? )
            // InternalCQLParser.g:2900:3: () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )?
            {
            // InternalCQLParser.g:2900:3: ()
            // InternalCQLParser.g:2901:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getDropCommandAccess().getDropCommandAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,DROP,FOLLOW_48); 

            			newLeafNode(otherlv_1, grammarAccess.getDropCommandAccess().getDROPKeyword_1());
            		
            // InternalCQLParser.g:2911:3: ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) )
            // InternalCQLParser.g:2912:4: ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) )
            {
            // InternalCQLParser.g:2912:4: ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) )
            // InternalCQLParser.g:2913:5: (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW )
            {
            // InternalCQLParser.g:2913:5: (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW )
            int alt45=3;
            switch ( input.LA(1) ) {
            case SINK:
                {
                alt45=1;
                }
                break;
            case STREAM:
                {
                alt45=2;
                }
                break;
            case VIEW:
                {
                alt45=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 45, 0, input);

                throw nvae;
            }

            switch (alt45) {
                case 1 :
                    // InternalCQLParser.g:2914:6: lv_name_2_1= SINK
                    {
                    lv_name_2_1=(Token)match(input,SINK,FOLLOW_15); 

                    						newLeafNode(lv_name_2_1, grammarAccess.getDropCommandAccess().getNameSINKKeyword_2_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropCommandRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_2_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:2925:6: lv_name_2_2= STREAM
                    {
                    lv_name_2_2=(Token)match(input,STREAM,FOLLOW_15); 

                    						newLeafNode(lv_name_2_2, grammarAccess.getDropCommandAccess().getNameSTREAMKeyword_2_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropCommandRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_2_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:2936:6: lv_name_2_3= VIEW
                    {
                    lv_name_2_3=(Token)match(input,VIEW,FOLLOW_15); 

                    						newLeafNode(lv_name_2_3, grammarAccess.getDropCommandAccess().getNameVIEWKeyword_2_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropCommandRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_2_3, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQLParser.g:2949:3: ( (lv_stream_3_0= RULE_ID ) )
            // InternalCQLParser.g:2950:4: (lv_stream_3_0= RULE_ID )
            {
            // InternalCQLParser.g:2950:4: (lv_stream_3_0= RULE_ID )
            // InternalCQLParser.g:2951:5: lv_stream_3_0= RULE_ID
            {
            lv_stream_3_0=(Token)match(input,RULE_ID,FOLLOW_49); 

            					newLeafNode(lv_stream_3_0, grammarAccess.getDropCommandAccess().getStreamIDTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDropCommandRule());
            					}
            					setWithLastConsumed(
            						current,
            						"stream",
            						lv_stream_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:2967:3: ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )?
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==IF) ) {
                alt46=1;
            }
            switch (alt46) {
                case 1 :
                    // InternalCQLParser.g:2968:4: ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS
                    {
                    // InternalCQLParser.g:2968:4: ( (lv_exists_4_0= IF ) )
                    // InternalCQLParser.g:2969:5: (lv_exists_4_0= IF )
                    {
                    // InternalCQLParser.g:2969:5: (lv_exists_4_0= IF )
                    // InternalCQLParser.g:2970:6: lv_exists_4_0= IF
                    {
                    lv_exists_4_0=(Token)match(input,IF,FOLLOW_50); 

                    						newLeafNode(lv_exists_4_0, grammarAccess.getDropCommandAccess().getExistsIFKeyword_4_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropCommandRule());
                    						}
                    						setWithLastConsumed(current, "exists", lv_exists_4_0, "IF");
                    					

                    }


                    }

                    otherlv_5=(Token)match(input,EXISTS,FOLLOW_2); 

                    				newLeafNode(otherlv_5, grammarAccess.getDropCommandAccess().getEXISTSKeyword_4_1());
                    			

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
    // $ANTLR end "ruleDropCommand"


    // $ANTLR start "entryRuleUserCommand"
    // InternalCQLParser.g:2991:1: entryRuleUserCommand returns [EObject current=null] : iv_ruleUserCommand= ruleUserCommand EOF ;
    public final EObject entryRuleUserCommand() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleUserCommand = null;


        try {
            // InternalCQLParser.g:2991:52: (iv_ruleUserCommand= ruleUserCommand EOF )
            // InternalCQLParser.g:2992:2: iv_ruleUserCommand= ruleUserCommand EOF
            {
             newCompositeNode(grammarAccess.getUserCommandRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleUserCommand=ruleUserCommand();

            state._fsp--;

             current =iv_ruleUserCommand; 
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
    // $ANTLR end "entryRuleUserCommand"


    // $ANTLR start "ruleUserCommand"
    // InternalCQLParser.g:2998:1: ruleUserCommand returns [EObject current=null] : ( () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )? ) ;
    public final EObject ruleUserCommand() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_1=null;
        Token lv_name_1_2=null;
        Token lv_name_1_3=null;
        Token lv_subject_2_1=null;
        Token lv_subject_2_2=null;
        Token lv_subject_2_3=null;
        Token lv_subjectName_3_0=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        Token lv_password_6_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:3004:2: ( ( () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )? ) )
            // InternalCQLParser.g:3005:2: ( () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )? )
            {
            // InternalCQLParser.g:3005:2: ( () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )? )
            // InternalCQLParser.g:3006:3: () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )?
            {
            // InternalCQLParser.g:3006:3: ()
            // InternalCQLParser.g:3007:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getUserCommandAccess().getUserCommandAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:3013:3: ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) )
            // InternalCQLParser.g:3014:4: ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) )
            {
            // InternalCQLParser.g:3014:4: ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) )
            // InternalCQLParser.g:3015:5: (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP )
            {
            // InternalCQLParser.g:3015:5: (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP )
            int alt47=3;
            switch ( input.LA(1) ) {
            case CREATE:
                {
                alt47=1;
                }
                break;
            case ALTER:
                {
                alt47=2;
                }
                break;
            case DROP:
                {
                alt47=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 47, 0, input);

                throw nvae;
            }

            switch (alt47) {
                case 1 :
                    // InternalCQLParser.g:3016:6: lv_name_1_1= CREATE
                    {
                    lv_name_1_1=(Token)match(input,CREATE,FOLLOW_51); 

                    						newLeafNode(lv_name_1_1, grammarAccess.getUserCommandAccess().getNameCREATEKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserCommandRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:3027:6: lv_name_1_2= ALTER
                    {
                    lv_name_1_2=(Token)match(input,ALTER,FOLLOW_51); 

                    						newLeafNode(lv_name_1_2, grammarAccess.getUserCommandAccess().getNameALTERKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserCommandRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:3038:6: lv_name_1_3= DROP
                    {
                    lv_name_1_3=(Token)match(input,DROP,FOLLOW_51); 

                    						newLeafNode(lv_name_1_3, grammarAccess.getUserCommandAccess().getNameDROPKeyword_1_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserCommandRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_3, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQLParser.g:3051:3: ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) )
            // InternalCQLParser.g:3052:4: ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) )
            {
            // InternalCQLParser.g:3052:4: ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) )
            // InternalCQLParser.g:3053:5: (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT )
            {
            // InternalCQLParser.g:3053:5: (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT )
            int alt48=3;
            switch ( input.LA(1) ) {
            case USER:
                {
                alt48=1;
                }
                break;
            case ROLE:
                {
                alt48=2;
                }
                break;
            case TENANT:
                {
                alt48=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 48, 0, input);

                throw nvae;
            }

            switch (alt48) {
                case 1 :
                    // InternalCQLParser.g:3054:6: lv_subject_2_1= USER
                    {
                    lv_subject_2_1=(Token)match(input,USER,FOLLOW_15); 

                    						newLeafNode(lv_subject_2_1, grammarAccess.getUserCommandAccess().getSubjectUSERKeyword_2_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserCommandRule());
                    						}
                    						setWithLastConsumed(current, "subject", lv_subject_2_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:3065:6: lv_subject_2_2= ROLE
                    {
                    lv_subject_2_2=(Token)match(input,ROLE,FOLLOW_15); 

                    						newLeafNode(lv_subject_2_2, grammarAccess.getUserCommandAccess().getSubjectROLEKeyword_2_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserCommandRule());
                    						}
                    						setWithLastConsumed(current, "subject", lv_subject_2_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:3076:6: lv_subject_2_3= TENANT
                    {
                    lv_subject_2_3=(Token)match(input,TENANT,FOLLOW_15); 

                    						newLeafNode(lv_subject_2_3, grammarAccess.getUserCommandAccess().getSubjectTENANTKeyword_2_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserCommandRule());
                    						}
                    						setWithLastConsumed(current, "subject", lv_subject_2_3, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQLParser.g:3089:3: ( (lv_subjectName_3_0= RULE_ID ) )
            // InternalCQLParser.g:3090:4: (lv_subjectName_3_0= RULE_ID )
            {
            // InternalCQLParser.g:3090:4: (lv_subjectName_3_0= RULE_ID )
            // InternalCQLParser.g:3091:5: lv_subjectName_3_0= RULE_ID
            {
            lv_subjectName_3_0=(Token)match(input,RULE_ID,FOLLOW_52); 

            					newLeafNode(lv_subjectName_3_0, grammarAccess.getUserCommandAccess().getSubjectNameIDTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getUserCommandRule());
            					}
            					setWithLastConsumed(
            						current,
            						"subjectName",
            						lv_subjectName_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:3107:3: (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )?
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==IDENTIFIED) ) {
                alt49=1;
            }
            switch (alt49) {
                case 1 :
                    // InternalCQLParser.g:3108:4: otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) )
                    {
                    otherlv_4=(Token)match(input,IDENTIFIED,FOLLOW_14); 

                    				newLeafNode(otherlv_4, grammarAccess.getUserCommandAccess().getIDENTIFIEDKeyword_4_0());
                    			
                    otherlv_5=(Token)match(input,BY,FOLLOW_32); 

                    				newLeafNode(otherlv_5, grammarAccess.getUserCommandAccess().getBYKeyword_4_1());
                    			
                    // InternalCQLParser.g:3116:4: ( (lv_password_6_0= RULE_STRING ) )
                    // InternalCQLParser.g:3117:5: (lv_password_6_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:3117:5: (lv_password_6_0= RULE_STRING )
                    // InternalCQLParser.g:3118:6: lv_password_6_0= RULE_STRING
                    {
                    lv_password_6_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

                    						newLeafNode(lv_password_6_0, grammarAccess.getUserCommandAccess().getPasswordSTRINGTerminalRuleCall_4_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserCommandRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"password",
                    							lv_password_6_0,
                    							"org.eclipse.xtext.common.Terminals.STRING");
                    					

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
    // $ANTLR end "ruleUserCommand"


    // $ANTLR start "entryRuleRightsCommand"
    // InternalCQLParser.g:3139:1: entryRuleRightsCommand returns [EObject current=null] : iv_ruleRightsCommand= ruleRightsCommand EOF ;
    public final EObject entryRuleRightsCommand() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRightsCommand = null;


        try {
            // InternalCQLParser.g:3139:54: (iv_ruleRightsCommand= ruleRightsCommand EOF )
            // InternalCQLParser.g:3140:2: iv_ruleRightsCommand= ruleRightsCommand EOF
            {
             newCompositeNode(grammarAccess.getRightsCommandRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRightsCommand=ruleRightsCommand();

            state._fsp--;

             current =iv_ruleRightsCommand; 
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
    // $ANTLR end "entryRuleRightsCommand"


    // $ANTLR start "ruleRightsCommand"
    // InternalCQLParser.g:3146:1: ruleRightsCommand returns [EObject current=null] : ( ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) ) | ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) ) ) ;
    public final EObject ruleRightsCommand() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_0=null;
        Token lv_operations_2_0=null;
        Token otherlv_3=null;
        Token lv_operations_4_0=null;
        Token otherlv_5=null;
        Token lv_operations2_6_0=null;
        Token otherlv_7=null;
        Token lv_operations2_8_0=null;
        Token otherlv_9=null;
        Token lv_user_10_0=null;
        Token lv_name_12_0=null;
        Token lv_operations_13_0=null;
        Token otherlv_14=null;
        Token lv_operations_15_0=null;
        Token otherlv_16=null;
        Token lv_operations2_17_0=null;
        Token otherlv_18=null;
        Token lv_operations2_19_0=null;
        Token otherlv_20=null;
        Token lv_user_21_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:3152:2: ( ( ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) ) | ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) ) ) )
            // InternalCQLParser.g:3153:2: ( ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) ) | ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) ) )
            {
            // InternalCQLParser.g:3153:2: ( ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) ) | ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) ) )
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==GRANT) ) {
                alt56=1;
            }
            else if ( (LA56_0==REVOKE) ) {
                alt56=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;
            }
            switch (alt56) {
                case 1 :
                    // InternalCQLParser.g:3154:3: ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) )
                    {
                    // InternalCQLParser.g:3154:3: ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) )
                    // InternalCQLParser.g:3155:4: () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:3155:4: ()
                    // InternalCQLParser.g:3156:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getRightsCommandAccess().getRightsCommandAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:3162:4: ( (lv_name_1_0= GRANT ) )
                    // InternalCQLParser.g:3163:5: (lv_name_1_0= GRANT )
                    {
                    // InternalCQLParser.g:3163:5: (lv_name_1_0= GRANT )
                    // InternalCQLParser.g:3164:6: lv_name_1_0= GRANT
                    {
                    lv_name_1_0=(Token)match(input,GRANT,FOLLOW_15); 

                    						newLeafNode(lv_name_1_0, grammarAccess.getRightsCommandAccess().getNameGRANTKeyword_0_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsCommandRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_0, "GRANT");
                    					

                    }


                    }

                    // InternalCQLParser.g:3176:4: ( (lv_operations_2_0= RULE_ID ) )
                    // InternalCQLParser.g:3177:5: (lv_operations_2_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3177:5: (lv_operations_2_0= RULE_ID )
                    // InternalCQLParser.g:3178:6: lv_operations_2_0= RULE_ID
                    {
                    lv_operations_2_0=(Token)match(input,RULE_ID,FOLLOW_53); 

                    						newLeafNode(lv_operations_2_0, grammarAccess.getRightsCommandAccess().getOperationsIDTerminalRuleCall_0_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsCommandRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"operations",
                    							lv_operations_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }

                    // InternalCQLParser.g:3194:4: (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )*
                    loop50:
                    do {
                        int alt50=2;
                        int LA50_0 = input.LA(1);

                        if ( (LA50_0==Comma) ) {
                            alt50=1;
                        }


                        switch (alt50) {
                    	case 1 :
                    	    // InternalCQLParser.g:3195:5: otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) )
                    	    {
                    	    otherlv_3=(Token)match(input,Comma,FOLLOW_15); 

                    	    					newLeafNode(otherlv_3, grammarAccess.getRightsCommandAccess().getCommaKeyword_0_3_0());
                    	    				
                    	    // InternalCQLParser.g:3199:5: ( (lv_operations_4_0= RULE_ID ) )
                    	    // InternalCQLParser.g:3200:6: (lv_operations_4_0= RULE_ID )
                    	    {
                    	    // InternalCQLParser.g:3200:6: (lv_operations_4_0= RULE_ID )
                    	    // InternalCQLParser.g:3201:7: lv_operations_4_0= RULE_ID
                    	    {
                    	    lv_operations_4_0=(Token)match(input,RULE_ID,FOLLOW_53); 

                    	    							newLeafNode(lv_operations_4_0, grammarAccess.getRightsCommandAccess().getOperationsIDTerminalRuleCall_0_3_1_0());
                    	    						

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getRightsCommandRule());
                    	    							}
                    	    							addWithLastConsumed(
                    	    								current,
                    	    								"operations",
                    	    								lv_operations_4_0,
                    	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop50;
                        }
                    } while (true);

                    // InternalCQLParser.g:3218:4: (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )?
                    int alt52=2;
                    int LA52_0 = input.LA(1);

                    if ( (LA52_0==ON) ) {
                        alt52=1;
                    }
                    switch (alt52) {
                        case 1 :
                            // InternalCQLParser.g:3219:5: otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )*
                            {
                            otherlv_5=(Token)match(input,ON,FOLLOW_15); 

                            					newLeafNode(otherlv_5, grammarAccess.getRightsCommandAccess().getONKeyword_0_4_0());
                            				
                            // InternalCQLParser.g:3223:5: ( (lv_operations2_6_0= RULE_ID ) )
                            // InternalCQLParser.g:3224:6: (lv_operations2_6_0= RULE_ID )
                            {
                            // InternalCQLParser.g:3224:6: (lv_operations2_6_0= RULE_ID )
                            // InternalCQLParser.g:3225:7: lv_operations2_6_0= RULE_ID
                            {
                            lv_operations2_6_0=(Token)match(input,RULE_ID,FOLLOW_54); 

                            							newLeafNode(lv_operations2_6_0, grammarAccess.getRightsCommandAccess().getOperations2IDTerminalRuleCall_0_4_1_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getRightsCommandRule());
                            							}
                            							addWithLastConsumed(
                            								current,
                            								"operations2",
                            								lv_operations2_6_0,
                            								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                            						

                            }


                            }

                            // InternalCQLParser.g:3241:5: (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )*
                            loop51:
                            do {
                                int alt51=2;
                                int LA51_0 = input.LA(1);

                                if ( (LA51_0==Comma) ) {
                                    alt51=1;
                                }


                                switch (alt51) {
                            	case 1 :
                            	    // InternalCQLParser.g:3242:6: otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) )
                            	    {
                            	    otherlv_7=(Token)match(input,Comma,FOLLOW_15); 

                            	    						newLeafNode(otherlv_7, grammarAccess.getRightsCommandAccess().getCommaKeyword_0_4_2_0());
                            	    					
                            	    // InternalCQLParser.g:3246:6: ( (lv_operations2_8_0= RULE_ID ) )
                            	    // InternalCQLParser.g:3247:7: (lv_operations2_8_0= RULE_ID )
                            	    {
                            	    // InternalCQLParser.g:3247:7: (lv_operations2_8_0= RULE_ID )
                            	    // InternalCQLParser.g:3248:8: lv_operations2_8_0= RULE_ID
                            	    {
                            	    lv_operations2_8_0=(Token)match(input,RULE_ID,FOLLOW_54); 

                            	    								newLeafNode(lv_operations2_8_0, grammarAccess.getRightsCommandAccess().getOperations2IDTerminalRuleCall_0_4_2_1_0());
                            	    							

                            	    								if (current==null) {
                            	    									current = createModelElement(grammarAccess.getRightsCommandRule());
                            	    								}
                            	    								addWithLastConsumed(
                            	    									current,
                            	    									"operations2",
                            	    									lv_operations2_8_0,
                            	    									"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                            	    							

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

                    otherlv_9=(Token)match(input,TO,FOLLOW_15); 

                    				newLeafNode(otherlv_9, grammarAccess.getRightsCommandAccess().getTOKeyword_0_5());
                    			
                    // InternalCQLParser.g:3270:4: ( (lv_user_10_0= RULE_ID ) )
                    // InternalCQLParser.g:3271:5: (lv_user_10_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3271:5: (lv_user_10_0= RULE_ID )
                    // InternalCQLParser.g:3272:6: lv_user_10_0= RULE_ID
                    {
                    lv_user_10_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    						newLeafNode(lv_user_10_0, grammarAccess.getRightsCommandAccess().getUserIDTerminalRuleCall_0_6_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsCommandRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"user",
                    							lv_user_10_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:3290:3: ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) )
                    {
                    // InternalCQLParser.g:3290:3: ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) )
                    // InternalCQLParser.g:3291:4: () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:3291:4: ()
                    // InternalCQLParser.g:3292:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getRightsCommandAccess().getRightsCommandAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:3298:4: ( (lv_name_12_0= REVOKE ) )
                    // InternalCQLParser.g:3299:5: (lv_name_12_0= REVOKE )
                    {
                    // InternalCQLParser.g:3299:5: (lv_name_12_0= REVOKE )
                    // InternalCQLParser.g:3300:6: lv_name_12_0= REVOKE
                    {
                    lv_name_12_0=(Token)match(input,REVOKE,FOLLOW_15); 

                    						newLeafNode(lv_name_12_0, grammarAccess.getRightsCommandAccess().getNameREVOKEKeyword_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsCommandRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_12_0, "REVOKE");
                    					

                    }


                    }

                    // InternalCQLParser.g:3312:4: ( (lv_operations_13_0= RULE_ID ) )
                    // InternalCQLParser.g:3313:5: (lv_operations_13_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3313:5: (lv_operations_13_0= RULE_ID )
                    // InternalCQLParser.g:3314:6: lv_operations_13_0= RULE_ID
                    {
                    lv_operations_13_0=(Token)match(input,RULE_ID,FOLLOW_55); 

                    						newLeafNode(lv_operations_13_0, grammarAccess.getRightsCommandAccess().getOperationsIDTerminalRuleCall_1_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsCommandRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"operations",
                    							lv_operations_13_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }

                    // InternalCQLParser.g:3330:4: (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )*
                    loop53:
                    do {
                        int alt53=2;
                        int LA53_0 = input.LA(1);

                        if ( (LA53_0==Comma) ) {
                            alt53=1;
                        }


                        switch (alt53) {
                    	case 1 :
                    	    // InternalCQLParser.g:3331:5: otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) )
                    	    {
                    	    otherlv_14=(Token)match(input,Comma,FOLLOW_15); 

                    	    					newLeafNode(otherlv_14, grammarAccess.getRightsCommandAccess().getCommaKeyword_1_3_0());
                    	    				
                    	    // InternalCQLParser.g:3335:5: ( (lv_operations_15_0= RULE_ID ) )
                    	    // InternalCQLParser.g:3336:6: (lv_operations_15_0= RULE_ID )
                    	    {
                    	    // InternalCQLParser.g:3336:6: (lv_operations_15_0= RULE_ID )
                    	    // InternalCQLParser.g:3337:7: lv_operations_15_0= RULE_ID
                    	    {
                    	    lv_operations_15_0=(Token)match(input,RULE_ID,FOLLOW_55); 

                    	    							newLeafNode(lv_operations_15_0, grammarAccess.getRightsCommandAccess().getOperationsIDTerminalRuleCall_1_3_1_0());
                    	    						

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getRightsCommandRule());
                    	    							}
                    	    							addWithLastConsumed(
                    	    								current,
                    	    								"operations",
                    	    								lv_operations_15_0,
                    	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop53;
                        }
                    } while (true);

                    // InternalCQLParser.g:3354:4: (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )?
                    int alt55=2;
                    int LA55_0 = input.LA(1);

                    if ( (LA55_0==ON) ) {
                        alt55=1;
                    }
                    switch (alt55) {
                        case 1 :
                            // InternalCQLParser.g:3355:5: otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )*
                            {
                            otherlv_16=(Token)match(input,ON,FOLLOW_15); 

                            					newLeafNode(otherlv_16, grammarAccess.getRightsCommandAccess().getONKeyword_1_4_0());
                            				
                            // InternalCQLParser.g:3359:5: ( (lv_operations2_17_0= RULE_ID ) )
                            // InternalCQLParser.g:3360:6: (lv_operations2_17_0= RULE_ID )
                            {
                            // InternalCQLParser.g:3360:6: (lv_operations2_17_0= RULE_ID )
                            // InternalCQLParser.g:3361:7: lv_operations2_17_0= RULE_ID
                            {
                            lv_operations2_17_0=(Token)match(input,RULE_ID,FOLLOW_8); 

                            							newLeafNode(lv_operations2_17_0, grammarAccess.getRightsCommandAccess().getOperations2IDTerminalRuleCall_1_4_1_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getRightsCommandRule());
                            							}
                            							addWithLastConsumed(
                            								current,
                            								"operations2",
                            								lv_operations2_17_0,
                            								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                            						

                            }


                            }

                            // InternalCQLParser.g:3377:5: (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )*
                            loop54:
                            do {
                                int alt54=2;
                                int LA54_0 = input.LA(1);

                                if ( (LA54_0==Comma) ) {
                                    alt54=1;
                                }


                                switch (alt54) {
                            	case 1 :
                            	    // InternalCQLParser.g:3378:6: otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) )
                            	    {
                            	    otherlv_18=(Token)match(input,Comma,FOLLOW_15); 

                            	    						newLeafNode(otherlv_18, grammarAccess.getRightsCommandAccess().getCommaKeyword_1_4_2_0());
                            	    					
                            	    // InternalCQLParser.g:3382:6: ( (lv_operations2_19_0= RULE_ID ) )
                            	    // InternalCQLParser.g:3383:7: (lv_operations2_19_0= RULE_ID )
                            	    {
                            	    // InternalCQLParser.g:3383:7: (lv_operations2_19_0= RULE_ID )
                            	    // InternalCQLParser.g:3384:8: lv_operations2_19_0= RULE_ID
                            	    {
                            	    lv_operations2_19_0=(Token)match(input,RULE_ID,FOLLOW_8); 

                            	    								newLeafNode(lv_operations2_19_0, grammarAccess.getRightsCommandAccess().getOperations2IDTerminalRuleCall_1_4_2_1_0());
                            	    							

                            	    								if (current==null) {
                            	    									current = createModelElement(grammarAccess.getRightsCommandRule());
                            	    								}
                            	    								addWithLastConsumed(
                            	    									current,
                            	    									"operations2",
                            	    									lv_operations2_19_0,
                            	    									"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                            	    							

                            	    }


                            	    }


                            	    }
                            	    break;

                            	default :
                            	    break loop54;
                                }
                            } while (true);


                            }
                            break;

                    }

                    otherlv_20=(Token)match(input,FROM,FOLLOW_15); 

                    				newLeafNode(otherlv_20, grammarAccess.getRightsCommandAccess().getFROMKeyword_1_5());
                    			
                    // InternalCQLParser.g:3406:4: ( (lv_user_21_0= RULE_ID ) )
                    // InternalCQLParser.g:3407:5: (lv_user_21_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3407:5: (lv_user_21_0= RULE_ID )
                    // InternalCQLParser.g:3408:6: lv_user_21_0= RULE_ID
                    {
                    lv_user_21_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    						newLeafNode(lv_user_21_0, grammarAccess.getRightsCommandAccess().getUserIDTerminalRuleCall_1_6_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsCommandRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"user",
                    							lv_user_21_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

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
    // $ANTLR end "ruleRightsCommand"


    // $ANTLR start "entryRuleRightsRoleCommand"
    // InternalCQLParser.g:3429:1: entryRuleRightsRoleCommand returns [EObject current=null] : iv_ruleRightsRoleCommand= ruleRightsRoleCommand EOF ;
    public final EObject entryRuleRightsRoleCommand() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRightsRoleCommand = null;


        try {
            // InternalCQLParser.g:3429:58: (iv_ruleRightsRoleCommand= ruleRightsRoleCommand EOF )
            // InternalCQLParser.g:3430:2: iv_ruleRightsRoleCommand= ruleRightsRoleCommand EOF
            {
             newCompositeNode(grammarAccess.getRightsRoleCommandRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRightsRoleCommand=ruleRightsRoleCommand();

            state._fsp--;

             current =iv_ruleRightsRoleCommand; 
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
    // $ANTLR end "entryRuleRightsRoleCommand"


    // $ANTLR start "ruleRightsRoleCommand"
    // InternalCQLParser.g:3436:1: ruleRightsRoleCommand returns [EObject current=null] : ( ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) ) | ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) ) ) ;
    public final EObject ruleRightsRoleCommand() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token lv_operations_3_0=null;
        Token otherlv_4=null;
        Token lv_operations_5_0=null;
        Token otherlv_6=null;
        Token lv_user_7_0=null;
        Token lv_name_9_0=null;
        Token otherlv_10=null;
        Token lv_operations_11_0=null;
        Token otherlv_12=null;
        Token lv_operations_13_0=null;
        Token otherlv_14=null;
        Token lv_user_15_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:3442:2: ( ( ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) ) | ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) ) ) )
            // InternalCQLParser.g:3443:2: ( ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) ) | ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) ) )
            {
            // InternalCQLParser.g:3443:2: ( ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) ) | ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) ) )
            int alt59=2;
            int LA59_0 = input.LA(1);

            if ( (LA59_0==GRANT) ) {
                alt59=1;
            }
            else if ( (LA59_0==REVOKE) ) {
                alt59=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 59, 0, input);

                throw nvae;
            }
            switch (alt59) {
                case 1 :
                    // InternalCQLParser.g:3444:3: ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) )
                    {
                    // InternalCQLParser.g:3444:3: ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) )
                    // InternalCQLParser.g:3445:4: () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:3445:4: ()
                    // InternalCQLParser.g:3446:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getRightsRoleCommandAccess().getRightsRoleCommandAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:3452:4: ( (lv_name_1_0= GRANT ) )
                    // InternalCQLParser.g:3453:5: (lv_name_1_0= GRANT )
                    {
                    // InternalCQLParser.g:3453:5: (lv_name_1_0= GRANT )
                    // InternalCQLParser.g:3454:6: lv_name_1_0= GRANT
                    {
                    lv_name_1_0=(Token)match(input,GRANT,FOLLOW_56); 

                    						newLeafNode(lv_name_1_0, grammarAccess.getRightsRoleCommandAccess().getNameGRANTKeyword_0_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsRoleCommandRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_0, "GRANT");
                    					

                    }


                    }

                    otherlv_2=(Token)match(input,ROLE,FOLLOW_15); 

                    				newLeafNode(otherlv_2, grammarAccess.getRightsRoleCommandAccess().getROLEKeyword_0_2());
                    			
                    // InternalCQLParser.g:3470:4: ( (lv_operations_3_0= RULE_ID ) )
                    // InternalCQLParser.g:3471:5: (lv_operations_3_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3471:5: (lv_operations_3_0= RULE_ID )
                    // InternalCQLParser.g:3472:6: lv_operations_3_0= RULE_ID
                    {
                    lv_operations_3_0=(Token)match(input,RULE_ID,FOLLOW_54); 

                    						newLeafNode(lv_operations_3_0, grammarAccess.getRightsRoleCommandAccess().getOperationsIDTerminalRuleCall_0_3_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsRoleCommandRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"operations",
                    							lv_operations_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }

                    // InternalCQLParser.g:3488:4: (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )*
                    loop57:
                    do {
                        int alt57=2;
                        int LA57_0 = input.LA(1);

                        if ( (LA57_0==Comma) ) {
                            alt57=1;
                        }


                        switch (alt57) {
                    	case 1 :
                    	    // InternalCQLParser.g:3489:5: otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) )
                    	    {
                    	    otherlv_4=(Token)match(input,Comma,FOLLOW_15); 

                    	    					newLeafNode(otherlv_4, grammarAccess.getRightsRoleCommandAccess().getCommaKeyword_0_4_0());
                    	    				
                    	    // InternalCQLParser.g:3493:5: ( (lv_operations_5_0= RULE_ID ) )
                    	    // InternalCQLParser.g:3494:6: (lv_operations_5_0= RULE_ID )
                    	    {
                    	    // InternalCQLParser.g:3494:6: (lv_operations_5_0= RULE_ID )
                    	    // InternalCQLParser.g:3495:7: lv_operations_5_0= RULE_ID
                    	    {
                    	    lv_operations_5_0=(Token)match(input,RULE_ID,FOLLOW_54); 

                    	    							newLeafNode(lv_operations_5_0, grammarAccess.getRightsRoleCommandAccess().getOperationsIDTerminalRuleCall_0_4_1_0());
                    	    						

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getRightsRoleCommandRule());
                    	    							}
                    	    							addWithLastConsumed(
                    	    								current,
                    	    								"operations",
                    	    								lv_operations_5_0,
                    	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop57;
                        }
                    } while (true);

                    otherlv_6=(Token)match(input,TO,FOLLOW_15); 

                    				newLeafNode(otherlv_6, grammarAccess.getRightsRoleCommandAccess().getTOKeyword_0_5());
                    			
                    // InternalCQLParser.g:3516:4: ( (lv_user_7_0= RULE_ID ) )
                    // InternalCQLParser.g:3517:5: (lv_user_7_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3517:5: (lv_user_7_0= RULE_ID )
                    // InternalCQLParser.g:3518:6: lv_user_7_0= RULE_ID
                    {
                    lv_user_7_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    						newLeafNode(lv_user_7_0, grammarAccess.getRightsRoleCommandAccess().getUserIDTerminalRuleCall_0_6_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsRoleCommandRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"user",
                    							lv_user_7_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:3536:3: ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) )
                    {
                    // InternalCQLParser.g:3536:3: ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) )
                    // InternalCQLParser.g:3537:4: () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:3537:4: ()
                    // InternalCQLParser.g:3538:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getRightsRoleCommandAccess().getRightsRoleCommandAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:3544:4: ( (lv_name_9_0= REVOKE ) )
                    // InternalCQLParser.g:3545:5: (lv_name_9_0= REVOKE )
                    {
                    // InternalCQLParser.g:3545:5: (lv_name_9_0= REVOKE )
                    // InternalCQLParser.g:3546:6: lv_name_9_0= REVOKE
                    {
                    lv_name_9_0=(Token)match(input,REVOKE,FOLLOW_56); 

                    						newLeafNode(lv_name_9_0, grammarAccess.getRightsRoleCommandAccess().getNameREVOKEKeyword_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsRoleCommandRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_9_0, "REVOKE");
                    					

                    }


                    }

                    otherlv_10=(Token)match(input,ROLE,FOLLOW_15); 

                    				newLeafNode(otherlv_10, grammarAccess.getRightsRoleCommandAccess().getROLEKeyword_1_2());
                    			
                    // InternalCQLParser.g:3562:4: ( (lv_operations_11_0= RULE_ID ) )
                    // InternalCQLParser.g:3563:5: (lv_operations_11_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3563:5: (lv_operations_11_0= RULE_ID )
                    // InternalCQLParser.g:3564:6: lv_operations_11_0= RULE_ID
                    {
                    lv_operations_11_0=(Token)match(input,RULE_ID,FOLLOW_8); 

                    						newLeafNode(lv_operations_11_0, grammarAccess.getRightsRoleCommandAccess().getOperationsIDTerminalRuleCall_1_3_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsRoleCommandRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"operations",
                    							lv_operations_11_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }

                    // InternalCQLParser.g:3580:4: (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )*
                    loop58:
                    do {
                        int alt58=2;
                        int LA58_0 = input.LA(1);

                        if ( (LA58_0==Comma) ) {
                            alt58=1;
                        }


                        switch (alt58) {
                    	case 1 :
                    	    // InternalCQLParser.g:3581:5: otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) )
                    	    {
                    	    otherlv_12=(Token)match(input,Comma,FOLLOW_15); 

                    	    					newLeafNode(otherlv_12, grammarAccess.getRightsRoleCommandAccess().getCommaKeyword_1_4_0());
                    	    				
                    	    // InternalCQLParser.g:3585:5: ( (lv_operations_13_0= RULE_ID ) )
                    	    // InternalCQLParser.g:3586:6: (lv_operations_13_0= RULE_ID )
                    	    {
                    	    // InternalCQLParser.g:3586:6: (lv_operations_13_0= RULE_ID )
                    	    // InternalCQLParser.g:3587:7: lv_operations_13_0= RULE_ID
                    	    {
                    	    lv_operations_13_0=(Token)match(input,RULE_ID,FOLLOW_8); 

                    	    							newLeafNode(lv_operations_13_0, grammarAccess.getRightsRoleCommandAccess().getOperationsIDTerminalRuleCall_1_4_1_0());
                    	    						

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getRightsRoleCommandRule());
                    	    							}
                    	    							addWithLastConsumed(
                    	    								current,
                    	    								"operations",
                    	    								lv_operations_13_0,
                    	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop58;
                        }
                    } while (true);

                    otherlv_14=(Token)match(input,FROM,FOLLOW_15); 

                    				newLeafNode(otherlv_14, grammarAccess.getRightsRoleCommandAccess().getFROMKeyword_1_5());
                    			
                    // InternalCQLParser.g:3608:4: ( (lv_user_15_0= RULE_ID ) )
                    // InternalCQLParser.g:3609:5: (lv_user_15_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3609:5: (lv_user_15_0= RULE_ID )
                    // InternalCQLParser.g:3610:6: lv_user_15_0= RULE_ID
                    {
                    lv_user_15_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    						newLeafNode(lv_user_15_0, grammarAccess.getRightsRoleCommandAccess().getUserIDTerminalRuleCall_1_6_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsRoleCommandRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"user",
                    							lv_user_15_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

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
    // $ANTLR end "ruleRightsRoleCommand"


    // $ANTLR start "entryRuleWindow_Unbounded"
    // InternalCQLParser.g:3631:1: entryRuleWindow_Unbounded returns [String current=null] : iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF ;
    public final String entryRuleWindow_Unbounded() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleWindow_Unbounded = null;


        try {
            // InternalCQLParser.g:3631:56: (iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF )
            // InternalCQLParser.g:3632:2: iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF
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
    // InternalCQLParser.g:3638:1: ruleWindow_Unbounded returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= UNBOUNDED ;
    public final AntlrDatatypeRuleToken ruleWindow_Unbounded() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:3644:2: (kw= UNBOUNDED )
            // InternalCQLParser.g:3645:2: kw= UNBOUNDED
            {
            kw=(Token)match(input,UNBOUNDED,FOLLOW_2); 

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
    // InternalCQLParser.g:3653:1: entryRuleWindow_Timebased returns [EObject current=null] : iv_ruleWindow_Timebased= ruleWindow_Timebased EOF ;
    public final EObject entryRuleWindow_Timebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Timebased = null;


        try {
            // InternalCQLParser.g:3653:57: (iv_ruleWindow_Timebased= ruleWindow_Timebased EOF )
            // InternalCQLParser.g:3654:2: iv_ruleWindow_Timebased= ruleWindow_Timebased EOF
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
    // InternalCQLParser.g:3660:1: ruleWindow_Timebased returns [EObject current=null] : (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME ) ;
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
            // InternalCQLParser.g:3666:2: ( (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME ) )
            // InternalCQLParser.g:3667:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME )
            {
            // InternalCQLParser.g:3667:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME )
            // InternalCQLParser.g:3668:3: otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME
            {
            otherlv_0=(Token)match(input,SIZE,FOLLOW_44); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TimebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQLParser.g:3672:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQLParser.g:3673:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQLParser.g:3673:4: (lv_size_1_0= RULE_INT )
            // InternalCQLParser.g:3674:5: lv_size_1_0= RULE_INT
            {
            lv_size_1_0=(Token)match(input,RULE_INT,FOLLOW_15); 

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

            // InternalCQLParser.g:3690:3: ( (lv_unit_2_0= RULE_ID ) )
            // InternalCQLParser.g:3691:4: (lv_unit_2_0= RULE_ID )
            {
            // InternalCQLParser.g:3691:4: (lv_unit_2_0= RULE_ID )
            // InternalCQLParser.g:3692:5: lv_unit_2_0= RULE_ID
            {
            lv_unit_2_0=(Token)match(input,RULE_ID,FOLLOW_57); 

            					newLeafNode(lv_unit_2_0, grammarAccess.getWindow_TimebasedAccess().getUnitIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getWindow_TimebasedRule());
            					}
            					setWithLastConsumed(
            						current,
            						"unit",
            						lv_unit_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:3708:3: (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )?
            int alt60=2;
            int LA60_0 = input.LA(1);

            if ( (LA60_0==ADVANCE) ) {
                alt60=1;
            }
            switch (alt60) {
                case 1 :
                    // InternalCQLParser.g:3709:4: otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) )
                    {
                    otherlv_3=(Token)match(input,ADVANCE,FOLLOW_44); 

                    				newLeafNode(otherlv_3, grammarAccess.getWindow_TimebasedAccess().getADVANCEKeyword_3_0());
                    			
                    // InternalCQLParser.g:3713:4: ( (lv_advance_size_4_0= RULE_INT ) )
                    // InternalCQLParser.g:3714:5: (lv_advance_size_4_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3714:5: (lv_advance_size_4_0= RULE_INT )
                    // InternalCQLParser.g:3715:6: lv_advance_size_4_0= RULE_INT
                    {
                    lv_advance_size_4_0=(Token)match(input,RULE_INT,FOLLOW_15); 

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

                    // InternalCQLParser.g:3731:4: ( (lv_advance_unit_5_0= RULE_ID ) )
                    // InternalCQLParser.g:3732:5: (lv_advance_unit_5_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3732:5: (lv_advance_unit_5_0= RULE_ID )
                    // InternalCQLParser.g:3733:6: lv_advance_unit_5_0= RULE_ID
                    {
                    lv_advance_unit_5_0=(Token)match(input,RULE_ID,FOLLOW_58); 

                    						newLeafNode(lv_advance_unit_5_0, grammarAccess.getWindow_TimebasedAccess().getAdvance_unitIDTerminalRuleCall_3_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getWindow_TimebasedRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"advance_unit",
                    							lv_advance_unit_5_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_6=(Token)match(input,TIME,FOLLOW_2); 

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
    // InternalCQLParser.g:3758:1: entryRuleWindow_Tuplebased returns [EObject current=null] : iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF ;
    public final EObject entryRuleWindow_Tuplebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Tuplebased = null;


        try {
            // InternalCQLParser.g:3758:58: (iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF )
            // InternalCQLParser.g:3759:2: iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF
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
    // InternalCQLParser.g:3765:1: ruleWindow_Tuplebased returns [EObject current=null] : (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) ;
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
            // InternalCQLParser.g:3771:2: ( (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) )
            // InternalCQLParser.g:3772:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            {
            // InternalCQLParser.g:3772:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            // InternalCQLParser.g:3773:3: otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            {
            otherlv_0=(Token)match(input,SIZE,FOLLOW_44); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TuplebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQLParser.g:3777:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQLParser.g:3778:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQLParser.g:3778:4: (lv_size_1_0= RULE_INT )
            // InternalCQLParser.g:3779:5: lv_size_1_0= RULE_INT
            {
            lv_size_1_0=(Token)match(input,RULE_INT,FOLLOW_59); 

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

            // InternalCQLParser.g:3795:3: (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )?
            int alt61=2;
            int LA61_0 = input.LA(1);

            if ( (LA61_0==ADVANCE) ) {
                alt61=1;
            }
            switch (alt61) {
                case 1 :
                    // InternalCQLParser.g:3796:4: otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) )
                    {
                    otherlv_2=(Token)match(input,ADVANCE,FOLLOW_44); 

                    				newLeafNode(otherlv_2, grammarAccess.getWindow_TuplebasedAccess().getADVANCEKeyword_2_0());
                    			
                    // InternalCQLParser.g:3800:4: ( (lv_advance_size_3_0= RULE_INT ) )
                    // InternalCQLParser.g:3801:5: (lv_advance_size_3_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3801:5: (lv_advance_size_3_0= RULE_INT )
                    // InternalCQLParser.g:3802:6: lv_advance_size_3_0= RULE_INT
                    {
                    lv_advance_size_3_0=(Token)match(input,RULE_INT,FOLLOW_60); 

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

            otherlv_4=(Token)match(input,TUPLE,FOLLOW_61); 

            			newLeafNode(otherlv_4, grammarAccess.getWindow_TuplebasedAccess().getTUPLEKeyword_3());
            		
            // InternalCQLParser.g:3823:3: (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==PARTITION) ) {
                alt62=1;
            }
            switch (alt62) {
                case 1 :
                    // InternalCQLParser.g:3824:4: otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    {
                    otherlv_5=(Token)match(input,PARTITION,FOLLOW_14); 

                    				newLeafNode(otherlv_5, grammarAccess.getWindow_TuplebasedAccess().getPARTITIONKeyword_4_0());
                    			
                    otherlv_6=(Token)match(input,BY,FOLLOW_15); 

                    				newLeafNode(otherlv_6, grammarAccess.getWindow_TuplebasedAccess().getBYKeyword_4_1());
                    			
                    // InternalCQLParser.g:3832:4: ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    // InternalCQLParser.g:3833:5: (lv_partition_attribute_7_0= ruleAttribute )
                    {
                    // InternalCQLParser.g:3833:5: (lv_partition_attribute_7_0= ruleAttribute )
                    // InternalCQLParser.g:3834:6: lv_partition_attribute_7_0= ruleAttribute
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
    // InternalCQLParser.g:3856:1: entryRuleExpressionsModel returns [EObject current=null] : iv_ruleExpressionsModel= ruleExpressionsModel EOF ;
    public final EObject entryRuleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionsModel = null;


        try {
            // InternalCQLParser.g:3856:57: (iv_ruleExpressionsModel= ruleExpressionsModel EOF )
            // InternalCQLParser.g:3857:2: iv_ruleExpressionsModel= ruleExpressionsModel EOF
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
    // InternalCQLParser.g:3863:1: ruleExpressionsModel returns [EObject current=null] : ( () ( (lv_elements_1_0= ruleExpression ) ) ) ;
    public final EObject ruleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3869:2: ( ( () ( (lv_elements_1_0= ruleExpression ) ) ) )
            // InternalCQLParser.g:3870:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            {
            // InternalCQLParser.g:3870:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            // InternalCQLParser.g:3871:3: () ( (lv_elements_1_0= ruleExpression ) )
            {
            // InternalCQLParser.g:3871:3: ()
            // InternalCQLParser.g:3872:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:3878:3: ( (lv_elements_1_0= ruleExpression ) )
            // InternalCQLParser.g:3879:4: (lv_elements_1_0= ruleExpression )
            {
            // InternalCQLParser.g:3879:4: (lv_elements_1_0= ruleExpression )
            // InternalCQLParser.g:3880:5: lv_elements_1_0= ruleExpression
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
    // InternalCQLParser.g:3901:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCQLParser.g:3901:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCQLParser.g:3902:2: iv_ruleExpression= ruleExpression EOF
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
    // InternalCQLParser.g:3908:1: ruleExpression returns [EObject current=null] : this_Or_0= ruleOr ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_Or_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3914:2: (this_Or_0= ruleOr )
            // InternalCQLParser.g:3915:2: this_Or_0= ruleOr
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
    // InternalCQLParser.g:3926:1: entryRuleOr returns [EObject current=null] : iv_ruleOr= ruleOr EOF ;
    public final EObject entryRuleOr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOr = null;


        try {
            // InternalCQLParser.g:3926:43: (iv_ruleOr= ruleOr EOF )
            // InternalCQLParser.g:3927:2: iv_ruleOr= ruleOr EOF
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
    // InternalCQLParser.g:3933:1: ruleOr returns [EObject current=null] : (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* ) ;
    public final EObject ruleOr() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_And_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3939:2: ( (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* ) )
            // InternalCQLParser.g:3940:2: (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* )
            {
            // InternalCQLParser.g:3940:2: (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* )
            // InternalCQLParser.g:3941:3: this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrAccess().getAndParserRuleCall_0());
            		
            pushFollow(FOLLOW_62);
            this_And_0=ruleAnd();

            state._fsp--;


            			current = this_And_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3949:3: ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )*
            loop63:
            do {
                int alt63=2;
                int LA63_0 = input.LA(1);

                if ( (LA63_0==OR) ) {
                    alt63=1;
                }


                switch (alt63) {
            	case 1 :
            	    // InternalCQLParser.g:3950:4: () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) )
            	    {
            	    // InternalCQLParser.g:3950:4: ()
            	    // InternalCQLParser.g:3951:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrAccess().getOrLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,OR,FOLLOW_12); 

            	    				newLeafNode(otherlv_2, grammarAccess.getOrAccess().getORKeyword_1_1());
            	    			
            	    // InternalCQLParser.g:3961:4: ( (lv_right_3_0= ruleAnd ) )
            	    // InternalCQLParser.g:3962:5: (lv_right_3_0= ruleAnd )
            	    {
            	    // InternalCQLParser.g:3962:5: (lv_right_3_0= ruleAnd )
            	    // InternalCQLParser.g:3963:6: lv_right_3_0= ruleAnd
            	    {

            	    						newCompositeNode(grammarAccess.getOrAccess().getRightAndParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_62);
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
            	    break loop63;
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
    // InternalCQLParser.g:3985:1: entryRuleAnd returns [EObject current=null] : iv_ruleAnd= ruleAnd EOF ;
    public final EObject entryRuleAnd() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnd = null;


        try {
            // InternalCQLParser.g:3985:44: (iv_ruleAnd= ruleAnd EOF )
            // InternalCQLParser.g:3986:2: iv_ruleAnd= ruleAnd EOF
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
    // InternalCQLParser.g:3992:1: ruleAnd returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAnd() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3998:2: ( (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCQLParser.g:3999:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCQLParser.g:3999:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCQLParser.g:4000:3: this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_63);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:4008:3: ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop64:
            do {
                int alt64=2;
                int LA64_0 = input.LA(1);

                if ( (LA64_0==AND) ) {
                    alt64=1;
                }


                switch (alt64) {
            	case 1 :
            	    // InternalCQLParser.g:4009:4: () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCQLParser.g:4009:4: ()
            	    // InternalCQLParser.g:4010:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndAccess().getAndLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,AND,FOLLOW_12); 

            	    				newLeafNode(otherlv_2, grammarAccess.getAndAccess().getANDKeyword_1_1());
            	    			
            	    // InternalCQLParser.g:4020:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCQLParser.g:4021:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCQLParser.g:4021:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCQLParser.g:4022:6: lv_right_3_0= ruleEqualitiy
            	    {

            	    						newCompositeNode(grammarAccess.getAndAccess().getRightEqualitiyParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_63);
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
            	    break loop64;
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
    // InternalCQLParser.g:4044:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCQLParser.g:4044:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCQLParser.g:4045:2: iv_ruleEqualitiy= ruleEqualitiy EOF
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
    // InternalCQLParser.g:4051:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Comparison_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:4057:2: ( (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCQLParser.g:4058:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCQLParser.g:4058:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCQLParser.g:4059:3: this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_64);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:4067:3: ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop66:
            do {
                int alt66=2;
                int LA66_0 = input.LA(1);

                if ( (LA66_0==ExclamationMarkEqualsSign||LA66_0==EqualsSign) ) {
                    alt66=1;
                }


                switch (alt66) {
            	case 1 :
            	    // InternalCQLParser.g:4068:4: () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCQLParser.g:4068:4: ()
            	    // InternalCQLParser.g:4069:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:4075:4: ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) )
            	    // InternalCQLParser.g:4076:5: ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) )
            	    {
            	    // InternalCQLParser.g:4076:5: ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) )
            	    // InternalCQLParser.g:4077:6: (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign )
            	    {
            	    // InternalCQLParser.g:4077:6: (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign )
            	    int alt65=2;
            	    int LA65_0 = input.LA(1);

            	    if ( (LA65_0==EqualsSign) ) {
            	        alt65=1;
            	    }
            	    else if ( (LA65_0==ExclamationMarkEqualsSign) ) {
            	        alt65=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 65, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt65) {
            	        case 1 :
            	            // InternalCQLParser.g:4078:7: lv_op_2_1= EqualsSign
            	            {
            	            lv_op_2_1=(Token)match(input,EqualsSign,FOLLOW_12); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getEqualitiyAccess().getOpEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getEqualitiyRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:4089:7: lv_op_2_2= ExclamationMarkEqualsSign
            	            {
            	            lv_op_2_2=(Token)match(input,ExclamationMarkEqualsSign,FOLLOW_12); 

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

            	    // InternalCQLParser.g:4102:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCQLParser.g:4103:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCQLParser.g:4103:5: (lv_right_3_0= ruleComparison )
            	    // InternalCQLParser.g:4104:6: lv_right_3_0= ruleComparison
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getRightComparisonParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_64);
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
            	    break loop66;
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
    // InternalCQLParser.g:4126:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCQLParser.g:4126:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCQLParser.g:4127:2: iv_ruleComparison= ruleComparison EOF
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
    // InternalCQLParser.g:4133:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
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
            // InternalCQLParser.g:4139:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCQLParser.g:4140:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCQLParser.g:4140:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCQLParser.g:4141:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_65);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:4149:3: ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop68:
            do {
                int alt68=2;
                int LA68_0 = input.LA(1);

                if ( ((LA68_0>=LessThanSignEqualsSign && LA68_0<=GreaterThanSignEqualsSign)||LA68_0==LessThanSign||LA68_0==GreaterThanSign) ) {
                    alt68=1;
                }


                switch (alt68) {
            	case 1 :
            	    // InternalCQLParser.g:4150:4: () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCQLParser.g:4150:4: ()
            	    // InternalCQLParser.g:4151:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:4157:4: ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) )
            	    // InternalCQLParser.g:4158:5: ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) )
            	    {
            	    // InternalCQLParser.g:4158:5: ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) )
            	    // InternalCQLParser.g:4159:6: (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign )
            	    {
            	    // InternalCQLParser.g:4159:6: (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign )
            	    int alt67=4;
            	    switch ( input.LA(1) ) {
            	    case GreaterThanSignEqualsSign:
            	        {
            	        alt67=1;
            	        }
            	        break;
            	    case LessThanSignEqualsSign:
            	        {
            	        alt67=2;
            	        }
            	        break;
            	    case LessThanSign:
            	        {
            	        alt67=3;
            	        }
            	        break;
            	    case GreaterThanSign:
            	        {
            	        alt67=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 67, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt67) {
            	        case 1 :
            	            // InternalCQLParser.g:4160:7: lv_op_2_1= GreaterThanSignEqualsSign
            	            {
            	            lv_op_2_1=(Token)match(input,GreaterThanSignEqualsSign,FOLLOW_12); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:4171:7: lv_op_2_2= LessThanSignEqualsSign
            	            {
            	            lv_op_2_2=(Token)match(input,LessThanSignEqualsSign,FOLLOW_12); 

            	            							newLeafNode(lv_op_2_2, grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_2, null);
            	            						

            	            }
            	            break;
            	        case 3 :
            	            // InternalCQLParser.g:4182:7: lv_op_2_3= LessThanSign
            	            {
            	            lv_op_2_3=(Token)match(input,LessThanSign,FOLLOW_12); 

            	            							newLeafNode(lv_op_2_3, grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_3, null);
            	            						

            	            }
            	            break;
            	        case 4 :
            	            // InternalCQLParser.g:4193:7: lv_op_2_4= GreaterThanSign
            	            {
            	            lv_op_2_4=(Token)match(input,GreaterThanSign,FOLLOW_12); 

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

            	    // InternalCQLParser.g:4206:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCQLParser.g:4207:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCQLParser.g:4207:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCQLParser.g:4208:6: lv_right_3_0= rulePlusOrMinus
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getRightPlusOrMinusParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_65);
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
            	    break loop68;
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
    // InternalCQLParser.g:4230:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCQLParser.g:4230:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCQLParser.g:4231:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
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
    // InternalCQLParser.g:4237:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:4243:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCQLParser.g:4244:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCQLParser.g:4244:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCQLParser.g:4245:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_66);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:4253:3: ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop70:
            do {
                int alt70=2;
                int LA70_0 = input.LA(1);

                if ( (LA70_0==PlusSign||LA70_0==HyphenMinus) ) {
                    alt70=1;
                }


                switch (alt70) {
            	case 1 :
            	    // InternalCQLParser.g:4254:4: ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCQLParser.g:4254:4: ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) )
            	    int alt69=2;
            	    int LA69_0 = input.LA(1);

            	    if ( (LA69_0==PlusSign) ) {
            	        alt69=1;
            	    }
            	    else if ( (LA69_0==HyphenMinus) ) {
            	        alt69=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 69, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt69) {
            	        case 1 :
            	            // InternalCQLParser.g:4255:5: ( () otherlv_2= PlusSign )
            	            {
            	            // InternalCQLParser.g:4255:5: ( () otherlv_2= PlusSign )
            	            // InternalCQLParser.g:4256:6: () otherlv_2= PlusSign
            	            {
            	            // InternalCQLParser.g:4256:6: ()
            	            // InternalCQLParser.g:4257:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_2=(Token)match(input,PlusSign,FOLLOW_12); 

            	            						newLeafNode(otherlv_2, grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1());
            	            					

            	            }


            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:4269:5: ( () otherlv_4= HyphenMinus )
            	            {
            	            // InternalCQLParser.g:4269:5: ( () otherlv_4= HyphenMinus )
            	            // InternalCQLParser.g:4270:6: () otherlv_4= HyphenMinus
            	            {
            	            // InternalCQLParser.g:4270:6: ()
            	            // InternalCQLParser.g:4271:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_4=(Token)match(input,HyphenMinus,FOLLOW_12); 

            	            						newLeafNode(otherlv_4, grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1());
            	            					

            	            }


            	            }
            	            break;

            	    }

            	    // InternalCQLParser.g:4283:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCQLParser.g:4284:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCQLParser.g:4284:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCQLParser.g:4285:6: lv_right_5_0= ruleMulOrDiv
            	    {

            	    						newCompositeNode(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_66);
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
            	    break loop70;
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
    // InternalCQLParser.g:4307:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCQLParser.g:4307:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCQLParser.g:4308:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
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
    // InternalCQLParser.g:4314:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:4320:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCQLParser.g:4321:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCQLParser.g:4321:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCQLParser.g:4322:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_67);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:4330:3: ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop72:
            do {
                int alt72=2;
                int LA72_0 = input.LA(1);

                if ( (LA72_0==Asterisk||LA72_0==Solidus) ) {
                    alt72=1;
                }


                switch (alt72) {
            	case 1 :
            	    // InternalCQLParser.g:4331:4: () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCQLParser.g:4331:4: ()
            	    // InternalCQLParser.g:4332:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:4338:4: ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) )
            	    // InternalCQLParser.g:4339:5: ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) )
            	    {
            	    // InternalCQLParser.g:4339:5: ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) )
            	    // InternalCQLParser.g:4340:6: (lv_op_2_1= Asterisk | lv_op_2_2= Solidus )
            	    {
            	    // InternalCQLParser.g:4340:6: (lv_op_2_1= Asterisk | lv_op_2_2= Solidus )
            	    int alt71=2;
            	    int LA71_0 = input.LA(1);

            	    if ( (LA71_0==Asterisk) ) {
            	        alt71=1;
            	    }
            	    else if ( (LA71_0==Solidus) ) {
            	        alt71=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 71, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt71) {
            	        case 1 :
            	            // InternalCQLParser.g:4341:7: lv_op_2_1= Asterisk
            	            {
            	            lv_op_2_1=(Token)match(input,Asterisk,FOLLOW_12); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getMulOrDivRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:4352:7: lv_op_2_2= Solidus
            	            {
            	            lv_op_2_2=(Token)match(input,Solidus,FOLLOW_12); 

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

            	    // InternalCQLParser.g:4365:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCQLParser.g:4366:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCQLParser.g:4366:5: (lv_right_3_0= rulePrimary )
            	    // InternalCQLParser.g:4367:6: lv_right_3_0= rulePrimary
            	    {

            	    						newCompositeNode(grammarAccess.getMulOrDivAccess().getRightPrimaryParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_67);
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
            	    break loop72;
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
    // InternalCQLParser.g:4389:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCQLParser.g:4389:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCQLParser.g:4390:2: iv_rulePrimary= rulePrimary EOF
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
    // InternalCQLParser.g:4396:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
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
            // InternalCQLParser.g:4402:2: ( ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCQLParser.g:4403:2: ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCQLParser.g:4403:2: ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt73=3;
            switch ( input.LA(1) ) {
            case LeftParenthesis:
                {
                alt73=1;
                }
                break;
            case NOT:
                {
                alt73=2;
                }
                break;
            case FALSE:
            case TRUE:
            case RULE_ID:
            case RULE_INT:
            case RULE_FLOAT:
            case RULE_STRING:
                {
                alt73=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 73, 0, input);

                throw nvae;
            }

            switch (alt73) {
                case 1 :
                    // InternalCQLParser.g:4404:3: ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis )
                    {
                    // InternalCQLParser.g:4404:3: ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis )
                    // InternalCQLParser.g:4405:4: () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis
                    {
                    // InternalCQLParser.g:4405:4: ()
                    // InternalCQLParser.g:4406:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,LeftParenthesis,FOLLOW_12); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCQLParser.g:4416:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCQLParser.g:4417:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCQLParser.g:4417:5: (lv_inner_2_0= ruleExpression )
                    // InternalCQLParser.g:4418:6: lv_inner_2_0= ruleExpression
                    {

                    						newCompositeNode(grammarAccess.getPrimaryAccess().getInnerExpressionParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_19);
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

                    otherlv_3=(Token)match(input,RightParenthesis,FOLLOW_2); 

                    				newLeafNode(otherlv_3, grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_0_3());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:4441:3: ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCQLParser.g:4441:3: ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCQLParser.g:4442:4: () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCQLParser.g:4442:4: ()
                    // InternalCQLParser.g:4443:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,NOT,FOLLOW_12); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCQLParser.g:4453:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCQLParser.g:4454:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCQLParser.g:4454:5: (lv_expression_6_0= rulePrimary )
                    // InternalCQLParser.g:4455:6: lv_expression_6_0= rulePrimary
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
                    // InternalCQLParser.g:4474:3: this_Atomic_7= ruleAtomic
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
    // InternalCQLParser.g:4486:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCQLParser.g:4486:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCQLParser.g:4487:2: iv_ruleAtomic= ruleAtomic EOF
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
    // InternalCQLParser.g:4493:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) ;
    public final EObject ruleAtomic() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        Token lv_value_7_1=null;
        Token lv_value_7_2=null;
        EObject lv_value_9_0 = null;

        EObject lv_value_10_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:4499:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) )
            // InternalCQLParser.g:4500:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            {
            // InternalCQLParser.g:4500:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            int alt76=5;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt76=1;
                }
                break;
            case RULE_FLOAT:
                {
                alt76=2;
                }
                break;
            case RULE_STRING:
                {
                alt76=3;
                }
                break;
            case FALSE:
            case TRUE:
                {
                alt76=4;
                }
                break;
            case RULE_ID:
                {
                alt76=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 76, 0, input);

                throw nvae;
            }

            switch (alt76) {
                case 1 :
                    // InternalCQLParser.g:4501:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQLParser.g:4501:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQLParser.g:4502:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQLParser.g:4502:4: ()
                    // InternalCQLParser.g:4503:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4509:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQLParser.g:4510:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQLParser.g:4510:5: (lv_value_1_0= RULE_INT )
                    // InternalCQLParser.g:4511:6: lv_value_1_0= RULE_INT
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
                    // InternalCQLParser.g:4529:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:4529:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQLParser.g:4530:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQLParser.g:4530:4: ()
                    // InternalCQLParser.g:4531:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4537:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQLParser.g:4538:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQLParser.g:4538:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQLParser.g:4539:6: lv_value_3_0= RULE_FLOAT
                    {
                    lv_value_3_0=(Token)match(input,RULE_FLOAT,FOLLOW_2); 

                    						newLeafNode(lv_value_3_0, grammarAccess.getAtomicAccess().getValueFLOATTerminalRuleCall_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"value",
                    							lv_value_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.FLOAT");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:4557:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQLParser.g:4557:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQLParser.g:4558:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQLParser.g:4558:4: ()
                    // InternalCQLParser.g:4559:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4565:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQLParser.g:4566:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:4566:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQLParser.g:4567:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQLParser.g:4585:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    {
                    // InternalCQLParser.g:4585:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    // InternalCQLParser.g:4586:4: () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    {
                    // InternalCQLParser.g:4586:4: ()
                    // InternalCQLParser.g:4587:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4593:4: ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    // InternalCQLParser.g:4594:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    {
                    // InternalCQLParser.g:4594:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    // InternalCQLParser.g:4595:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    {
                    // InternalCQLParser.g:4595:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    int alt74=2;
                    int LA74_0 = input.LA(1);

                    if ( (LA74_0==TRUE) ) {
                        alt74=1;
                    }
                    else if ( (LA74_0==FALSE) ) {
                        alt74=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 74, 0, input);

                        throw nvae;
                    }
                    switch (alt74) {
                        case 1 :
                            // InternalCQLParser.g:4596:7: lv_value_7_1= TRUE
                            {
                            lv_value_7_1=(Token)match(input,TRUE,FOLLOW_2); 

                            							newLeafNode(lv_value_7_1, grammarAccess.getAtomicAccess().getValueTRUEKeyword_3_1_0_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getAtomicRule());
                            							}
                            							setWithLastConsumed(current, "value", lv_value_7_1, null);
                            						

                            }
                            break;
                        case 2 :
                            // InternalCQLParser.g:4607:7: lv_value_7_2= FALSE
                            {
                            lv_value_7_2=(Token)match(input,FALSE,FOLLOW_2); 

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
                    // InternalCQLParser.g:4622:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    {
                    // InternalCQLParser.g:4622:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    // InternalCQLParser.g:4623:4: () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    {
                    // InternalCQLParser.g:4623:4: ()
                    // InternalCQLParser.g:4624:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeRefAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4630:4: ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    int alt75=2;
                    int LA75_0 = input.LA(1);

                    if ( (LA75_0==RULE_ID) ) {
                        switch ( input.LA(2) ) {
                        case FullStop:
                            {
                            int LA75_2 = input.LA(3);

                            if ( (LA75_2==RULE_ID) ) {
                                int LA75_5 = input.LA(4);

                                if ( (LA75_5==EOF||(LA75_5>=ATTACH && LA75_5<=CREATE)||(LA75_5>=HAVING && LA75_5<=STREAM)||LA75_5==ALTER||(LA75_5>=GRANT && LA75_5<=GROUP)||LA75_5==DROP||(LA75_5>=VIEW && LA75_5<=AND)||(LA75_5>=ExclamationMarkEqualsSign && LA75_5<=GreaterThanSignEqualsSign)||LA75_5==OR||(LA75_5>=RightParenthesis && LA75_5<=PlusSign)||LA75_5==HyphenMinus||LA75_5==Solidus||(LA75_5>=Semicolon && LA75_5<=GreaterThanSign)) ) {
                                    alt75=1;
                                }
                                else if ( (LA75_5==IN) ) {
                                    alt75=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 75, 5, input);

                                    throw nvae;
                                }
                            }
                            else if ( (LA75_2==Asterisk) ) {
                                int LA75_6 = input.LA(4);

                                if ( (LA75_6==IN) ) {
                                    alt75=2;
                                }
                                else if ( (LA75_6==EOF||(LA75_6>=ATTACH && LA75_6<=CREATE)||(LA75_6>=HAVING && LA75_6<=STREAM)||LA75_6==ALTER||(LA75_6>=GRANT && LA75_6<=GROUP)||LA75_6==DROP||(LA75_6>=VIEW && LA75_6<=AND)||(LA75_6>=ExclamationMarkEqualsSign && LA75_6<=GreaterThanSignEqualsSign)||LA75_6==OR||(LA75_6>=RightParenthesis && LA75_6<=PlusSign)||LA75_6==HyphenMinus||LA75_6==Solidus||(LA75_6>=Semicolon && LA75_6<=GreaterThanSign)) ) {
                                    alt75=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 75, 6, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 75, 2, input);

                                throw nvae;
                            }
                            }
                            break;
                        case EOF:
                        case ATTACH:
                        case CREATE:
                        case HAVING:
                        case REVOKE:
                        case SELECT:
                        case STREAM:
                        case ALTER:
                        case GRANT:
                        case GROUP:
                        case DROP:
                        case VIEW:
                        case AND:
                        case ExclamationMarkEqualsSign:
                        case LessThanSignEqualsSign:
                        case GreaterThanSignEqualsSign:
                        case OR:
                        case RightParenthesis:
                        case Asterisk:
                        case PlusSign:
                        case HyphenMinus:
                        case Solidus:
                        case Semicolon:
                        case LessThanSign:
                        case EqualsSign:
                        case GreaterThanSign:
                            {
                            alt75=1;
                            }
                            break;
                        case IN:
                            {
                            alt75=2;
                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 75, 1, input);

                            throw nvae;
                        }

                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 75, 0, input);

                        throw nvae;
                    }
                    switch (alt75) {
                        case 1 :
                            // InternalCQLParser.g:4631:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            {
                            // InternalCQLParser.g:4631:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            // InternalCQLParser.g:4632:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            {
                            // InternalCQLParser.g:4632:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            // InternalCQLParser.g:4633:7: lv_value_9_0= ruleAttributeWithoutAliasDefinition
                            {

                            							newCompositeNode(grammarAccess.getAtomicAccess().getValueAttributeWithoutAliasDefinitionParserRuleCall_4_1_0_0());
                            						
                            pushFollow(FOLLOW_2);
                            lv_value_9_0=ruleAttributeWithoutAliasDefinition();

                            state._fsp--;


                            							if (current==null) {
                            								current = createModelElementForParent(grammarAccess.getAtomicRule());
                            							}
                            							set(
                            								current,
                            								"value",
                            								lv_value_9_0,
                            								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
                            							afterParserOrEnumRuleCall();
                            						

                            }


                            }


                            }
                            break;
                        case 2 :
                            // InternalCQLParser.g:4651:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            {
                            // InternalCQLParser.g:4651:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            // InternalCQLParser.g:4652:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            {
                            // InternalCQLParser.g:4652:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            // InternalCQLParser.g:4653:7: lv_value_10_0= ruleAttributeWithNestedStatement
                            {

                            							newCompositeNode(grammarAccess.getAtomicAccess().getValueAttributeWithNestedStatementParserRuleCall_4_1_1_0());
                            						
                            pushFollow(FOLLOW_2);
                            lv_value_10_0=ruleAttributeWithNestedStatement();

                            state._fsp--;


                            							if (current==null) {
                            								current = createModelElementForParent(grammarAccess.getAtomicRule());
                            							}
                            							set(
                            								current,
                            								"value",
                            								lv_value_10_0,
                            								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithNestedStatement");
                            							afterParserOrEnumRuleCall();
                            						

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


    // $ANTLR start "entryRuleAtomicWithoutAttributeRef"
    // InternalCQLParser.g:4676:1: entryRuleAtomicWithoutAttributeRef returns [EObject current=null] : iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF ;
    public final EObject entryRuleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomicWithoutAttributeRef = null;


        try {
            // InternalCQLParser.g:4676:66: (iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF )
            // InternalCQLParser.g:4677:2: iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF
            {
             newCompositeNode(grammarAccess.getAtomicWithoutAttributeRefRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAtomicWithoutAttributeRef=ruleAtomicWithoutAttributeRef();

            state._fsp--;

             current =iv_ruleAtomicWithoutAttributeRef; 
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
    // $ANTLR end "entryRuleAtomicWithoutAttributeRef"


    // $ANTLR start "ruleAtomicWithoutAttributeRef"
    // InternalCQLParser.g:4683:1: ruleAtomicWithoutAttributeRef returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) ) ;
    public final EObject ruleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        Token lv_value_7_1=null;
        Token lv_value_7_2=null;


        	enterRule();

        try {
            // InternalCQLParser.g:4689:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) ) )
            // InternalCQLParser.g:4690:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) )
            {
            // InternalCQLParser.g:4690:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) )
            int alt78=4;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt78=1;
                }
                break;
            case RULE_FLOAT:
                {
                alt78=2;
                }
                break;
            case RULE_STRING:
                {
                alt78=3;
                }
                break;
            case FALSE:
            case TRUE:
                {
                alt78=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 78, 0, input);

                throw nvae;
            }

            switch (alt78) {
                case 1 :
                    // InternalCQLParser.g:4691:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQLParser.g:4691:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQLParser.g:4692:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQLParser.g:4692:4: ()
                    // InternalCQLParser.g:4693:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4699:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQLParser.g:4700:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQLParser.g:4700:5: (lv_value_1_0= RULE_INT )
                    // InternalCQLParser.g:4701:6: lv_value_1_0= RULE_INT
                    {
                    lv_value_1_0=(Token)match(input,RULE_INT,FOLLOW_2); 

                    						newLeafNode(lv_value_1_0, grammarAccess.getAtomicWithoutAttributeRefAccess().getValueINTTerminalRuleCall_0_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicWithoutAttributeRefRule());
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
                    // InternalCQLParser.g:4719:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:4719:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQLParser.g:4720:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQLParser.g:4720:4: ()
                    // InternalCQLParser.g:4721:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4727:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQLParser.g:4728:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQLParser.g:4728:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQLParser.g:4729:6: lv_value_3_0= RULE_FLOAT
                    {
                    lv_value_3_0=(Token)match(input,RULE_FLOAT,FOLLOW_2); 

                    						newLeafNode(lv_value_3_0, grammarAccess.getAtomicWithoutAttributeRefAccess().getValueFLOATTerminalRuleCall_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicWithoutAttributeRefRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"value",
                    							lv_value_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.FLOAT");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:4747:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQLParser.g:4747:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQLParser.g:4748:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQLParser.g:4748:4: ()
                    // InternalCQLParser.g:4749:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4755:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQLParser.g:4756:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:4756:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQLParser.g:4757:6: lv_value_5_0= RULE_STRING
                    {
                    lv_value_5_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

                    						newLeafNode(lv_value_5_0, grammarAccess.getAtomicWithoutAttributeRefAccess().getValueSTRINGTerminalRuleCall_2_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicWithoutAttributeRefRule());
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
                    // InternalCQLParser.g:4775:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    {
                    // InternalCQLParser.g:4775:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    // InternalCQLParser.g:4776:4: () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    {
                    // InternalCQLParser.g:4776:4: ()
                    // InternalCQLParser.g:4777:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4783:4: ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    // InternalCQLParser.g:4784:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    {
                    // InternalCQLParser.g:4784:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    // InternalCQLParser.g:4785:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    {
                    // InternalCQLParser.g:4785:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    int alt77=2;
                    int LA77_0 = input.LA(1);

                    if ( (LA77_0==TRUE) ) {
                        alt77=1;
                    }
                    else if ( (LA77_0==FALSE) ) {
                        alt77=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 77, 0, input);

                        throw nvae;
                    }
                    switch (alt77) {
                        case 1 :
                            // InternalCQLParser.g:4786:7: lv_value_7_1= TRUE
                            {
                            lv_value_7_1=(Token)match(input,TRUE,FOLLOW_2); 

                            							newLeafNode(lv_value_7_1, grammarAccess.getAtomicWithoutAttributeRefAccess().getValueTRUEKeyword_3_1_0_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getAtomicWithoutAttributeRefRule());
                            							}
                            							setWithLastConsumed(current, "value", lv_value_7_1, null);
                            						

                            }
                            break;
                        case 2 :
                            // InternalCQLParser.g:4797:7: lv_value_7_2= FALSE
                            {
                            lv_value_7_2=(Token)match(input,FALSE,FOLLOW_2); 

                            							newLeafNode(lv_value_7_2, grammarAccess.getAtomicWithoutAttributeRefAccess().getValueFALSEKeyword_3_1_0_1());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getAtomicWithoutAttributeRefRule());
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
    // $ANTLR end "ruleAtomicWithoutAttributeRef"


    // $ANTLR start "ruleCreateKeyword"
    // InternalCQLParser.g:4815:1: ruleCreateKeyword returns [Enumerator current=null] : ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) ) ;
    public final Enumerator ruleCreateKeyword() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;


        	enterRule();

        try {
            // InternalCQLParser.g:4821:2: ( ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) ) )
            // InternalCQLParser.g:4822:2: ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) )
            {
            // InternalCQLParser.g:4822:2: ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) )
            int alt79=2;
            int LA79_0 = input.LA(1);

            if ( (LA79_0==CREATE) ) {
                alt79=1;
            }
            else if ( (LA79_0==ATTACH) ) {
                alt79=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 79, 0, input);

                throw nvae;
            }
            switch (alt79) {
                case 1 :
                    // InternalCQLParser.g:4823:3: (enumLiteral_0= CREATE )
                    {
                    // InternalCQLParser.g:4823:3: (enumLiteral_0= CREATE )
                    // InternalCQLParser.g:4824:4: enumLiteral_0= CREATE
                    {
                    enumLiteral_0=(Token)match(input,CREATE,FOLLOW_2); 

                    				current = grammarAccess.getCreateKeywordAccess().getCREATEEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getCreateKeywordAccess().getCREATEEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:4831:3: (enumLiteral_1= ATTACH )
                    {
                    // InternalCQLParser.g:4831:3: (enumLiteral_1= ATTACH )
                    // InternalCQLParser.g:4832:4: enumLiteral_1= ATTACH
                    {
                    enumLiteral_1=(Token)match(input,ATTACH,FOLLOW_2); 

                    				current = grammarAccess.getCreateKeywordAccess().getATTACHEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getCreateKeywordAccess().getATTACHEnumLiteralDeclaration_1());
                    			

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
    // $ANTLR end "ruleCreateKeyword"

    // Delegated rules


    protected DFA2 dfa2 = new DFA2(this);
    static final String dfa_1s = "\32\uffff";
    static final String dfa_2s = "\1\21\2\uffff\2\27\1\uffff\2\43\1\27\2\uffff\1\105\3\uffff\1\66\2\105\1\67\1\105\1\16\1\105\3\uffff\1\67";
    static final String dfa_3s = "\1\51\2\uffff\1\51\1\50\1\uffff\2\105\1\44\2\uffff\1\105\3\uffff\1\66\2\105\1\72\1\105\1\41\1\105\3\uffff\1\72";
    static final String dfa_4s = "\1\uffff\1\1\1\2\2\uffff\1\4\3\uffff\1\13\1\3\1\uffff\1\10\1\5\1\6\7\uffff\1\7\1\11\1\12\1\uffff";
    static final String dfa_5s = "\32\uffff}>";
    static final String[] dfa_6s = {
            "\1\10\1\4\2\uffff\1\7\1\1\1\2\1\uffff\1\5\1\uffff\1\6\4\uffff\1\3\10\uffff\1\11",
            "",
            "",
            "\1\12\1\5\12\uffff\1\5\1\12\3\uffff\1\5\1\12",
            "\1\13\1\5\12\uffff\1\5\1\14\3\uffff\1\5",
            "",
            "\1\16\41\uffff\1\15",
            "\1\16\41\uffff\1\15",
            "\1\13\14\uffff\1\14",
            "",
            "",
            "\1\17",
            "",
            "",
            "",
            "\1\20",
            "\1\21",
            "\1\22",
            "\1\24\2\uffff\1\23",
            "\1\25",
            "\1\27\1\uffff\1\26\20\uffff\1\30",
            "\1\31",
            "",
            "",
            "",
            "\1\24\2\uffff\1\23"
    };

    static final short[] dfa_1 = DFA.unpackEncodedString(dfa_1s);
    static final char[] dfa_2 = DFA.unpackEncodedStringToUnsignedChars(dfa_2s);
    static final char[] dfa_3 = DFA.unpackEncodedStringToUnsignedChars(dfa_3s);
    static final short[] dfa_4 = DFA.unpackEncodedString(dfa_4s);
    static final short[] dfa_5 = DFA.unpackEncodedString(dfa_5s);
    static final short[][] dfa_6 = unpackEncodedStringArray(dfa_6s);

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = dfa_1;
            this.eof = dfa_1;
            this.min = dfa_2;
            this.max = dfa_3;
            this.accept = dfa_4;
            this.special = dfa_5;
            this.transition = dfa_6;
        }
        public String getDescription() {
            return "116:3: ( ( (lv_type_1_0= ruleSelect ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDropCommand ) ) | ( (lv_type_4_0= ruleUserCommand ) ) | ( (lv_type_5_0= ruleRightsCommand ) ) | ( (lv_type_6_0= ruleRightsRoleCommand ) ) | ( (lv_type_7_0= ruleCreateStream1 ) ) | ( (lv_type_8_0= ruleCreateSink1 ) ) | ( (lv_type_9_0= ruleCreateStreamChannel ) ) | ( (lv_type_10_0= ruleCreateStreamFile ) ) | ( (lv_type_11_0= ruleCreateView ) ) )";
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x000002010AE60002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x8000000000000002L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0100008004000800L,0x00000000000001E0L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0500008404000800L,0x00000000000001E0L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0400000400000000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0440000090100002L,0x0000000000000020L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0400000090100002L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0140088004000800L,0x00000000000001E0L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000010100002L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0400000000100002L,0x0000000000000020L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0400000000100002L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000800000000002L,0x0000000000000008L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000002000000400L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x2B00800000000002L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x2B00000000000000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x2B00000000000002L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0480000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0480000000000000L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0000000000400000L,0x0000000000000020L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0000021000800000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0000010801000000L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0428000000000000L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0420000000000000L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0408000400000000L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x0000004000002000L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x0000000020002000L});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_62 = new BitSet(new long[]{0x0010000000000002L});
    public static final BitSet FOLLOW_63 = new BitSet(new long[]{0x0000040000000002L});
    public static final BitSet FOLLOW_64 = new BitSet(new long[]{0x0000100000000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_65 = new BitSet(new long[]{0x0000600000000002L,0x0000000000000005L});
    public static final BitSet FOLLOW_66 = new BitSet(new long[]{0x0A00000000000002L});
    public static final BitSet FOLLOW_67 = new BitSet(new long[]{0x2100000000000002L});

}