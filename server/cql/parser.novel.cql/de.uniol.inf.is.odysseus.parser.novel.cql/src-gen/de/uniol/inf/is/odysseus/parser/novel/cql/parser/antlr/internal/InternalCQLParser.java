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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "NO_LAZY_CONNECTION_CHECK", "INTERSECTION", "DATAHANDLER", "CONNECTION", "DIFFERENCE", "IDENTIFIED", "PARTITION", "TRANSPORT", "UNBOUNDED", "DATABASE", "DISTINCT", "PASSWORD", "PROTOCOL", "TRUNCATE", "ADVANCE", "CHANNEL", "CONTEXT", "OPTIONS", "WRAPPER", "ATTACH", "CREATE", "EXISTS", "HAVING", "REVOKE", "SELECT", "SINGLE", "STREAM", "TENANT", "ALTER", "FALSE", "GRANT", "GROUP", "MULTI", "STORE", "TABLE", "TUPLE", "UNION", "WHERE", "DROP", "EACH", "FILE", "FROM", "JDBC", "ROLE", "SINK", "SIZE", "TIME", "TRUE", "USER", "VIEW", "WITH", "AND", "NOT", "ExclamationMarkEqualsSign", "LessThanSignEqualsSign", "GreaterThanSignEqualsSign", "AS", "AT", "BY", "IF", "IN", "ON", "OR", "TO", "LeftParenthesis", "RightParenthesis", "Asterisk", "PlusSign", "Comma", "HyphenMinus", "FullStop", "Solidus", "Colon", "Semicolon", "LessThanSign", "EqualsSign", "GreaterThanSign", "LeftSquareBracket", "RightSquareBracket", "CircumflexAccent", "RULE_ID", "RULE_INT", "RULE_FLOAT", "RULE_STRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER"
    };
    public static final int CREATE=24;
    public static final int CONTEXT=20;
    public static final int LessThanSign=78;
    public static final int TABLE=38;
    public static final int LeftParenthesis=68;
    public static final int PARTITION=10;
    public static final int IF=63;
    public static final int EACH=43;
    public static final int GreaterThanSign=80;
    public static final int RULE_ID=84;
    public static final int IN=64;
    public static final int DISTINCT=14;
    public static final int SIZE=49;
    public static final int PROTOCOL=16;
    public static final int OPTIONS=21;
    public static final int WHERE=41;
    public static final int GreaterThanSignEqualsSign=59;
    public static final int AS=60;
    public static final int AT=61;
    public static final int DATABASE=13;
    public static final int CHANNEL=19;
    public static final int PlusSign=71;
    public static final int RULE_INT=85;
    public static final int RULE_ML_COMMENT=88;
    public static final int LeftSquareBracket=81;
    public static final int ADVANCE=18;
    public static final int ALTER=32;
    public static final int ROLE=47;
    public static final int GROUP=35;
    public static final int Comma=72;
    public static final int HyphenMinus=73;
    public static final int BY=62;
    public static final int LessThanSignEqualsSign=58;
    public static final int Solidus=75;
    public static final int DATAHANDLER=6;
    public static final int FILE=44;
    public static final int FullStop=74;
    public static final int REVOKE=27;
    public static final int NO_LAZY_CONNECTION_CHECK=4;
    public static final int SELECT=28;
    public static final int TUPLE=39;
    public static final int CONNECTION=7;
    public static final int Semicolon=77;
    public static final int STORE=37;
    public static final int RULE_FLOAT=86;
    public static final int ExclamationMarkEqualsSign=57;
    public static final int TO=67;
    public static final int MULTI=36;
    public static final int UNION=40;
    public static final int TRUNCATE=17;
    public static final int SINGLE=29;
    public static final int FROM=45;
    public static final int VIEW=53;
    public static final int UNBOUNDED=12;
    public static final int WRAPPER=22;
    public static final int RightSquareBracket=82;
    public static final int RightParenthesis=69;
    public static final int TRUE=51;
    public static final int NOT=56;
    public static final int INTERSECTION=5;
    public static final int PASSWORD=15;
    public static final int SINK=48;
    public static final int AND=55;
    public static final int HAVING=26;
    public static final int RULE_STRING=87;
    public static final int DROP=42;
    public static final int RULE_SL_COMMENT=89;
    public static final int EqualsSign=79;
    public static final int TRANSPORT=11;
    public static final int DIFFERENCE=8;
    public static final int JDBC=46;
    public static final int Colon=76;
    public static final int EOF=-1;
    public static final int Asterisk=70;
    public static final int ON=65;
    public static final int OR=66;
    public static final int EXISTS=25;
    public static final int RULE_WS=90;
    public static final int STREAM=30;
    public static final int IDENTIFIED=9;
    public static final int TIME=50;
    public static final int RULE_ANY_OTHER=91;
    public static final int USER=52;
    public static final int TENANT=31;
    public static final int WITH=54;
    public static final int CircumflexAccent=83;
    public static final int GRANT=34;
    public static final int ATTACH=23;
    public static final int FALSE=33;

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
    // InternalCQLParser.g:57:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // InternalCQLParser.g:57:46: (iv_ruleModel= ruleModel EOF )
            // InternalCQLParser.g:58:2: iv_ruleModel= ruleModel EOF
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
    // InternalCQLParser.g:64:1: ruleModel returns [EObject current=null] : ( ( ( (lv_components_0_1= ruleStatement | lv_components_0_2= ruleCommand ) ) ) (otherlv_1= Semicolon )? )* ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_components_0_1 = null;

        EObject lv_components_0_2 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:70:2: ( ( ( ( (lv_components_0_1= ruleStatement | lv_components_0_2= ruleCommand ) ) ) (otherlv_1= Semicolon )? )* )
            // InternalCQLParser.g:71:2: ( ( ( (lv_components_0_1= ruleStatement | lv_components_0_2= ruleCommand ) ) ) (otherlv_1= Semicolon )? )*
            {
            // InternalCQLParser.g:71:2: ( ( ( (lv_components_0_1= ruleStatement | lv_components_0_2= ruleCommand ) ) ) (otherlv_1= Semicolon )? )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>=ATTACH && LA3_0<=CREATE)||(LA3_0>=REVOKE && LA3_0<=SELECT)||LA3_0==STREAM||LA3_0==ALTER||LA3_0==GRANT||LA3_0==DROP) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalCQLParser.g:72:3: ( ( (lv_components_0_1= ruleStatement | lv_components_0_2= ruleCommand ) ) ) (otherlv_1= Semicolon )?
            	    {
            	    // InternalCQLParser.g:72:3: ( ( (lv_components_0_1= ruleStatement | lv_components_0_2= ruleCommand ) ) )
            	    // InternalCQLParser.g:73:4: ( (lv_components_0_1= ruleStatement | lv_components_0_2= ruleCommand ) )
            	    {
            	    // InternalCQLParser.g:73:4: ( (lv_components_0_1= ruleStatement | lv_components_0_2= ruleCommand ) )
            	    // InternalCQLParser.g:74:5: (lv_components_0_1= ruleStatement | lv_components_0_2= ruleCommand )
            	    {
            	    // InternalCQLParser.g:74:5: (lv_components_0_1= ruleStatement | lv_components_0_2= ruleCommand )
            	    int alt1=2;
            	    switch ( input.LA(1) ) {
            	    case ATTACH:
            	    case SELECT:
            	    case STREAM:
            	        {
            	        alt1=1;
            	        }
            	        break;
            	    case CREATE:
            	        {
            	        int LA1_2 = input.LA(2);

            	        if ( (LA1_2==STREAM||LA1_2==SINK||LA1_2==VIEW) ) {
            	            alt1=1;
            	        }
            	        else if ( (LA1_2==DATABASE||LA1_2==CONTEXT||LA1_2==TENANT||LA1_2==ROLE||LA1_2==USER) ) {
            	            alt1=2;
            	        }
            	        else {
            	            NoViableAltException nvae =
            	                new NoViableAltException("", 1, 2, input);

            	            throw nvae;
            	        }
            	        }
            	        break;
            	    case REVOKE:
            	    case ALTER:
            	    case GRANT:
            	    case DROP:
            	        {
            	        alt1=2;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 1, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt1) {
            	        case 1 :
            	            // InternalCQLParser.g:75:6: lv_components_0_1= ruleStatement
            	            {

            	            						newCompositeNode(grammarAccess.getModelAccess().getComponentsStatementParserRuleCall_0_0_0());
            	            					
            	            pushFollow(FOLLOW_3);
            	            lv_components_0_1=ruleStatement();

            	            state._fsp--;


            	            						if (current==null) {
            	            							current = createModelElementForParent(grammarAccess.getModelRule());
            	            						}
            	            						add(
            	            							current,
            	            							"components",
            	            							lv_components_0_1,
            	            							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Statement");
            	            						afterParserOrEnumRuleCall();
            	            					

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:91:6: lv_components_0_2= ruleCommand
            	            {

            	            						newCompositeNode(grammarAccess.getModelAccess().getComponentsCommandParserRuleCall_0_0_1());
            	            					
            	            pushFollow(FOLLOW_3);
            	            lv_components_0_2=ruleCommand();

            	            state._fsp--;


            	            						if (current==null) {
            	            							current = createModelElementForParent(grammarAccess.getModelRule());
            	            						}
            	            						add(
            	            							current,
            	            							"components",
            	            							lv_components_0_2,
            	            							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Command");
            	            						afterParserOrEnumRuleCall();
            	            					

            	            }
            	            break;

            	    }


            	    }


            	    }

            	    // InternalCQLParser.g:109:3: (otherlv_1= Semicolon )?
            	    int alt2=2;
            	    int LA2_0 = input.LA(1);

            	    if ( (LA2_0==Semicolon) ) {
            	        alt2=1;
            	    }
            	    switch (alt2) {
            	        case 1 :
            	            // InternalCQLParser.g:110:4: otherlv_1= Semicolon
            	            {
            	            otherlv_1=(Token)match(input,Semicolon,FOLLOW_3); 

            	            				newLeafNode(otherlv_1, grammarAccess.getModelAccess().getSemicolonKeyword_1());
            	            			

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop3;
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


    // $ANTLR start "entryRuleBOOLEAN"
    // InternalCQLParser.g:119:1: entryRuleBOOLEAN returns [String current=null] : iv_ruleBOOLEAN= ruleBOOLEAN EOF ;
    public final String entryRuleBOOLEAN() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleBOOLEAN = null;


        try {
            // InternalCQLParser.g:119:47: (iv_ruleBOOLEAN= ruleBOOLEAN EOF )
            // InternalCQLParser.g:120:2: iv_ruleBOOLEAN= ruleBOOLEAN EOF
            {
             newCompositeNode(grammarAccess.getBOOLEANRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleBOOLEAN=ruleBOOLEAN();

            state._fsp--;

             current =iv_ruleBOOLEAN.getText(); 
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
    // $ANTLR end "entryRuleBOOLEAN"


    // $ANTLR start "ruleBOOLEAN"
    // InternalCQLParser.g:126:1: ruleBOOLEAN returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= FALSE | kw= TRUE ) ;
    public final AntlrDatatypeRuleToken ruleBOOLEAN() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:132:2: ( (kw= FALSE | kw= TRUE ) )
            // InternalCQLParser.g:133:2: (kw= FALSE | kw= TRUE )
            {
            // InternalCQLParser.g:133:2: (kw= FALSE | kw= TRUE )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==FALSE) ) {
                alt4=1;
            }
            else if ( (LA4_0==TRUE) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // InternalCQLParser.g:134:3: kw= FALSE
                    {
                    kw=(Token)match(input,FALSE,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getBOOLEANAccess().getFALSEKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:140:3: kw= TRUE
                    {
                    kw=(Token)match(input,TRUE,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getBOOLEANAccess().getTRUEKeyword_1());
                    		

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
    // $ANTLR end "ruleBOOLEAN"


    // $ANTLR start "entryRuleStatement"
    // InternalCQLParser.g:149:1: entryRuleStatement returns [EObject current=null] : iv_ruleStatement= ruleStatement EOF ;
    public final EObject entryRuleStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStatement = null;


        try {
            // InternalCQLParser.g:149:50: (iv_ruleStatement= ruleStatement EOF )
            // InternalCQLParser.g:150:2: iv_ruleStatement= ruleStatement EOF
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
    // InternalCQLParser.g:156:1: ruleStatement returns [EObject current=null] : ( ( (lv_type_0_1= ruleSelect | lv_type_0_2= ruleCreate | lv_type_0_3= ruleStreamTo ) ) ) ;
    public final EObject ruleStatement() throws RecognitionException {
        EObject current = null;

        EObject lv_type_0_1 = null;

        EObject lv_type_0_2 = null;

        EObject lv_type_0_3 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:162:2: ( ( ( (lv_type_0_1= ruleSelect | lv_type_0_2= ruleCreate | lv_type_0_3= ruleStreamTo ) ) ) )
            // InternalCQLParser.g:163:2: ( ( (lv_type_0_1= ruleSelect | lv_type_0_2= ruleCreate | lv_type_0_3= ruleStreamTo ) ) )
            {
            // InternalCQLParser.g:163:2: ( ( (lv_type_0_1= ruleSelect | lv_type_0_2= ruleCreate | lv_type_0_3= ruleStreamTo ) ) )
            // InternalCQLParser.g:164:3: ( (lv_type_0_1= ruleSelect | lv_type_0_2= ruleCreate | lv_type_0_3= ruleStreamTo ) )
            {
            // InternalCQLParser.g:164:3: ( (lv_type_0_1= ruleSelect | lv_type_0_2= ruleCreate | lv_type_0_3= ruleStreamTo ) )
            // InternalCQLParser.g:165:4: (lv_type_0_1= ruleSelect | lv_type_0_2= ruleCreate | lv_type_0_3= ruleStreamTo )
            {
            // InternalCQLParser.g:165:4: (lv_type_0_1= ruleSelect | lv_type_0_2= ruleCreate | lv_type_0_3= ruleStreamTo )
            int alt5=3;
            switch ( input.LA(1) ) {
            case SELECT:
                {
                alt5=1;
                }
                break;
            case ATTACH:
            case CREATE:
                {
                alt5=2;
                }
                break;
            case STREAM:
                {
                alt5=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // InternalCQLParser.g:166:5: lv_type_0_1= ruleSelect
                    {

                    					newCompositeNode(grammarAccess.getStatementAccess().getTypeSelectParserRuleCall_0_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_type_0_1=ruleSelect();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getStatementRule());
                    					}
                    					set(
                    						current,
                    						"type",
                    						lv_type_0_1,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Select");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:182:5: lv_type_0_2= ruleCreate
                    {

                    					newCompositeNode(grammarAccess.getStatementAccess().getTypeCreateParserRuleCall_0_1());
                    				
                    pushFollow(FOLLOW_2);
                    lv_type_0_2=ruleCreate();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getStatementRule());
                    					}
                    					set(
                    						current,
                    						"type",
                    						lv_type_0_2,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Create");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:198:5: lv_type_0_3= ruleStreamTo
                    {

                    					newCompositeNode(grammarAccess.getStatementAccess().getTypeStreamToParserRuleCall_0_2());
                    				
                    pushFollow(FOLLOW_2);
                    lv_type_0_3=ruleStreamTo();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getStatementRule());
                    					}
                    					set(
                    						current,
                    						"type",
                    						lv_type_0_3,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.StreamTo");
                    					afterParserOrEnumRuleCall();
                    				

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
    // $ANTLR end "ruleStatement"


    // $ANTLR start "entryRuleCommand"
    // InternalCQLParser.g:219:1: entryRuleCommand returns [EObject current=null] : iv_ruleCommand= ruleCommand EOF ;
    public final EObject entryRuleCommand() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCommand = null;


        try {
            // InternalCQLParser.g:219:48: (iv_ruleCommand= ruleCommand EOF )
            // InternalCQLParser.g:220:2: iv_ruleCommand= ruleCommand EOF
            {
             newCompositeNode(grammarAccess.getCommandRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCommand=ruleCommand();

            state._fsp--;

             current =iv_ruleCommand; 
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
    // $ANTLR end "entryRuleCommand"


    // $ANTLR start "ruleCommand"
    // InternalCQLParser.g:226:1: ruleCommand returns [EObject current=null] : ( ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) ) ) ;
    public final EObject ruleCommand() throws RecognitionException {
        EObject current = null;

        EObject lv_type_0_1 = null;

        EObject lv_type_0_2 = null;

        EObject lv_type_0_3 = null;

        EObject lv_type_0_4 = null;

        EObject lv_type_0_5 = null;

        EObject lv_type_0_6 = null;

        EObject lv_type_0_7 = null;

        EObject lv_type_0_8 = null;

        EObject lv_type_0_9 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:232:2: ( ( ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) ) ) )
            // InternalCQLParser.g:233:2: ( ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) ) )
            {
            // InternalCQLParser.g:233:2: ( ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) ) )
            // InternalCQLParser.g:234:3: ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) )
            {
            // InternalCQLParser.g:234:3: ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) )
            // InternalCQLParser.g:235:4: (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore )
            {
            // InternalCQLParser.g:235:4: (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore )
            int alt6=9;
            alt6 = dfa6.predict(input);
            switch (alt6) {
                case 1 :
                    // InternalCQLParser.g:236:5: lv_type_0_1= ruleDropStream
                    {

                    					newCompositeNode(grammarAccess.getCommandAccess().getTypeDropStreamParserRuleCall_0_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_type_0_1=ruleDropStream();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getCommandRule());
                    					}
                    					set(
                    						current,
                    						"type",
                    						lv_type_0_1,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.DropStream");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:252:5: lv_type_0_2= ruleUserManagement
                    {

                    					newCompositeNode(grammarAccess.getCommandAccess().getTypeUserManagementParserRuleCall_0_1());
                    				
                    pushFollow(FOLLOW_2);
                    lv_type_0_2=ruleUserManagement();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getCommandRule());
                    					}
                    					set(
                    						current,
                    						"type",
                    						lv_type_0_2,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.UserManagement");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:268:5: lv_type_0_3= ruleRightsManagement
                    {

                    					newCompositeNode(grammarAccess.getCommandAccess().getTypeRightsManagementParserRuleCall_0_2());
                    				
                    pushFollow(FOLLOW_2);
                    lv_type_0_3=ruleRightsManagement();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getCommandRule());
                    					}
                    					set(
                    						current,
                    						"type",
                    						lv_type_0_3,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.RightsManagement");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:284:5: lv_type_0_4= ruleRoleManagement
                    {

                    					newCompositeNode(grammarAccess.getCommandAccess().getTypeRoleManagementParserRuleCall_0_3());
                    				
                    pushFollow(FOLLOW_2);
                    lv_type_0_4=ruleRoleManagement();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getCommandRule());
                    					}
                    					set(
                    						current,
                    						"type",
                    						lv_type_0_4,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.RoleManagement");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 5 :
                    // InternalCQLParser.g:300:5: lv_type_0_5= ruleCreateDataBaseGenericConnection
                    {

                    					newCompositeNode(grammarAccess.getCommandAccess().getTypeCreateDataBaseGenericConnectionParserRuleCall_0_4());
                    				
                    pushFollow(FOLLOW_2);
                    lv_type_0_5=ruleCreateDataBaseGenericConnection();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getCommandRule());
                    					}
                    					set(
                    						current,
                    						"type",
                    						lv_type_0_5,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateDataBaseGenericConnection");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 6 :
                    // InternalCQLParser.g:316:5: lv_type_0_6= ruleCreateDataBaseJDBCConnection
                    {

                    					newCompositeNode(grammarAccess.getCommandAccess().getTypeCreateDataBaseJDBCConnectionParserRuleCall_0_5());
                    				
                    pushFollow(FOLLOW_2);
                    lv_type_0_6=ruleCreateDataBaseJDBCConnection();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getCommandRule());
                    					}
                    					set(
                    						current,
                    						"type",
                    						lv_type_0_6,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateDataBaseJDBCConnection");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 7 :
                    // InternalCQLParser.g:332:5: lv_type_0_7= ruleDropDatabaseConnection
                    {

                    					newCompositeNode(grammarAccess.getCommandAccess().getTypeDropDatabaseConnectionParserRuleCall_0_6());
                    				
                    pushFollow(FOLLOW_2);
                    lv_type_0_7=ruleDropDatabaseConnection();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getCommandRule());
                    					}
                    					set(
                    						current,
                    						"type",
                    						lv_type_0_7,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.DropDatabaseConnection");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 8 :
                    // InternalCQLParser.g:348:5: lv_type_0_8= ruleCreateContextStore
                    {

                    					newCompositeNode(grammarAccess.getCommandAccess().getTypeCreateContextStoreParserRuleCall_0_7());
                    				
                    pushFollow(FOLLOW_2);
                    lv_type_0_8=ruleCreateContextStore();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getCommandRule());
                    					}
                    					set(
                    						current,
                    						"type",
                    						lv_type_0_8,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateContextStore");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 9 :
                    // InternalCQLParser.g:364:5: lv_type_0_9= ruleDropContextStore
                    {

                    					newCompositeNode(grammarAccess.getCommandAccess().getTypeDropContextStoreParserRuleCall_0_8());
                    				
                    pushFollow(FOLLOW_2);
                    lv_type_0_9=ruleDropContextStore();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getCommandRule());
                    					}
                    					set(
                    						current,
                    						"type",
                    						lv_type_0_9,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.DropContextStore");
                    					afterParserOrEnumRuleCall();
                    				

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
    // $ANTLR end "ruleCommand"


    // $ANTLR start "entryRuleSelect"
    // InternalCQLParser.g:385:1: entryRuleSelect returns [EObject current=null] : iv_ruleSelect= ruleSelect EOF ;
    public final EObject entryRuleSelect() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelect = null;


        try {
            // InternalCQLParser.g:385:47: (iv_ruleSelect= ruleSelect EOF )
            // InternalCQLParser.g:386:2: iv_ruleSelect= ruleSelect EOF
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
    // InternalCQLParser.g:392:1: ruleSelect returns [EObject current=null] : ( () otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )? ) ;
    public final EObject ruleSelect() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_distinct_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        Token otherlv_11=null;
        Token otherlv_13=null;
        Token otherlv_14=null;
        Token otherlv_16=null;
        Token otherlv_18=null;
        EObject lv_arguments_4_0 = null;

        EObject lv_arguments_6_0 = null;

        EObject lv_sources_8_0 = null;

        EObject lv_sources_10_0 = null;

        EObject lv_predicates_12_0 = null;

        EObject lv_order_15_0 = null;

        EObject lv_order_17_0 = null;

        EObject lv_having_19_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:398:2: ( ( () otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )? ) )
            // InternalCQLParser.g:399:2: ( () otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )? )
            {
            // InternalCQLParser.g:399:2: ( () otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )? )
            // InternalCQLParser.g:400:3: () otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )?
            {
            // InternalCQLParser.g:400:3: ()
            // InternalCQLParser.g:401:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getSelectAccess().getSelectAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,SELECT,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getSelectAccess().getSELECTKeyword_1());
            		
            // InternalCQLParser.g:411:3: ( (lv_distinct_2_0= DISTINCT ) )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==DISTINCT) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // InternalCQLParser.g:412:4: (lv_distinct_2_0= DISTINCT )
                    {
                    // InternalCQLParser.g:412:4: (lv_distinct_2_0= DISTINCT )
                    // InternalCQLParser.g:413:5: lv_distinct_2_0= DISTINCT
                    {
                    lv_distinct_2_0=(Token)match(input,DISTINCT,FOLLOW_4); 

                    					newLeafNode(lv_distinct_2_0, grammarAccess.getSelectAccess().getDistinctDISTINCTKeyword_2_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getSelectRule());
                    					}
                    					setWithLastConsumed(current, "distinct", lv_distinct_2_0, "DISTINCT");
                    				

                    }


                    }
                    break;

            }

            // InternalCQLParser.g:425:3: (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==Asterisk) ) {
                alt10=1;
            }
            else if ( (LA10_0==FALSE||LA10_0==TRUE||(LA10_0>=RULE_ID && LA10_0<=RULE_STRING)) ) {
                alt10=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // InternalCQLParser.g:426:4: otherlv_3= Asterisk
                    {
                    otherlv_3=(Token)match(input,Asterisk,FOLLOW_5); 

                    				newLeafNode(otherlv_3, grammarAccess.getSelectAccess().getAsteriskKeyword_3_0());
                    			

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:431:4: ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* )
                    {
                    // InternalCQLParser.g:431:4: ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* )
                    // InternalCQLParser.g:432:5: ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )*
                    {
                    // InternalCQLParser.g:432:5: ( (lv_arguments_4_0= ruleSelectArgument ) )+
                    int cnt8=0;
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( (LA8_0==FALSE||LA8_0==TRUE||(LA8_0>=RULE_ID && LA8_0<=RULE_STRING)) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // InternalCQLParser.g:433:6: (lv_arguments_4_0= ruleSelectArgument )
                    	    {
                    	    // InternalCQLParser.g:433:6: (lv_arguments_4_0= ruleSelectArgument )
                    	    // InternalCQLParser.g:434:7: lv_arguments_4_0= ruleSelectArgument
                    	    {

                    	    							newCompositeNode(grammarAccess.getSelectAccess().getArgumentsSelectArgumentParserRuleCall_3_1_0_0());
                    	    						
                    	    pushFollow(FOLLOW_6);
                    	    lv_arguments_4_0=ruleSelectArgument();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"arguments",
                    	    								lv_arguments_4_0,
                    	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectArgument");
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

                    // InternalCQLParser.g:451:5: (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )*
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( (LA9_0==Comma) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // InternalCQLParser.g:452:6: otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) )
                    	    {
                    	    otherlv_5=(Token)match(input,Comma,FOLLOW_4); 

                    	    						newLeafNode(otherlv_5, grammarAccess.getSelectAccess().getCommaKeyword_3_1_1_0());
                    	    					
                    	    // InternalCQLParser.g:456:6: ( (lv_arguments_6_0= ruleSelectArgument ) )
                    	    // InternalCQLParser.g:457:7: (lv_arguments_6_0= ruleSelectArgument )
                    	    {
                    	    // InternalCQLParser.g:457:7: (lv_arguments_6_0= ruleSelectArgument )
                    	    // InternalCQLParser.g:458:8: lv_arguments_6_0= ruleSelectArgument
                    	    {

                    	    								newCompositeNode(grammarAccess.getSelectAccess().getArgumentsSelectArgumentParserRuleCall_3_1_1_1_0());
                    	    							
                    	    pushFollow(FOLLOW_7);
                    	    lv_arguments_6_0=ruleSelectArgument();

                    	    state._fsp--;


                    	    								if (current==null) {
                    	    									current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    								}
                    	    								add(
                    	    									current,
                    	    									"arguments",
                    	    									lv_arguments_6_0,
                    	    									"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectArgument");
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
                    break;

            }

            // InternalCQLParser.g:478:3: (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* )
            // InternalCQLParser.g:479:4: otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )*
            {
            otherlv_7=(Token)match(input,FROM,FOLLOW_8); 

            				newLeafNode(otherlv_7, grammarAccess.getSelectAccess().getFROMKeyword_4_0());
            			
            // InternalCQLParser.g:483:4: ( (lv_sources_8_0= ruleSource ) )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==LeftParenthesis||LA11_0==RULE_ID) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // InternalCQLParser.g:484:5: (lv_sources_8_0= ruleSource )
            	    {
            	    // InternalCQLParser.g:484:5: (lv_sources_8_0= ruleSource )
            	    // InternalCQLParser.g:485:6: lv_sources_8_0= ruleSource
            	    {

            	    						newCompositeNode(grammarAccess.getSelectAccess().getSourcesSourceParserRuleCall_4_1_0());
            	    					
            	    pushFollow(FOLLOW_9);
            	    lv_sources_8_0=ruleSource();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getSelectRule());
            	    						}
            	    						add(
            	    							current,
            	    							"sources",
            	    							lv_sources_8_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Source");
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

            // InternalCQLParser.g:502:4: (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==Comma) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // InternalCQLParser.g:503:5: otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) )
            	    {
            	    otherlv_9=(Token)match(input,Comma,FOLLOW_8); 

            	    					newLeafNode(otherlv_9, grammarAccess.getSelectAccess().getCommaKeyword_4_2_0());
            	    				
            	    // InternalCQLParser.g:507:5: ( (lv_sources_10_0= ruleSource ) )
            	    // InternalCQLParser.g:508:6: (lv_sources_10_0= ruleSource )
            	    {
            	    // InternalCQLParser.g:508:6: (lv_sources_10_0= ruleSource )
            	    // InternalCQLParser.g:509:7: lv_sources_10_0= ruleSource
            	    {

            	    							newCompositeNode(grammarAccess.getSelectAccess().getSourcesSourceParserRuleCall_4_2_1_0());
            	    						
            	    pushFollow(FOLLOW_10);
            	    lv_sources_10_0=ruleSource();

            	    state._fsp--;


            	    							if (current==null) {
            	    								current = createModelElementForParent(grammarAccess.getSelectRule());
            	    							}
            	    							add(
            	    								current,
            	    								"sources",
            	    								lv_sources_10_0,
            	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Source");
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

            // InternalCQLParser.g:528:3: (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==WHERE) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // InternalCQLParser.g:529:4: otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) )
                    {
                    otherlv_11=(Token)match(input,WHERE,FOLLOW_11); 

                    				newLeafNode(otherlv_11, grammarAccess.getSelectAccess().getWHEREKeyword_5_0());
                    			
                    // InternalCQLParser.g:533:4: ( (lv_predicates_12_0= ruleExpressionsModel ) )
                    // InternalCQLParser.g:534:5: (lv_predicates_12_0= ruleExpressionsModel )
                    {
                    // InternalCQLParser.g:534:5: (lv_predicates_12_0= ruleExpressionsModel )
                    // InternalCQLParser.g:535:6: lv_predicates_12_0= ruleExpressionsModel
                    {

                    						newCompositeNode(grammarAccess.getSelectAccess().getPredicatesExpressionsModelParserRuleCall_5_1_0());
                    					
                    pushFollow(FOLLOW_12);
                    lv_predicates_12_0=ruleExpressionsModel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    						}
                    						set(
                    							current,
                    							"predicates",
                    							lv_predicates_12_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionsModel");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:553:3: (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==GROUP) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // InternalCQLParser.g:554:4: otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )*
                    {
                    otherlv_13=(Token)match(input,GROUP,FOLLOW_13); 

                    				newLeafNode(otherlv_13, grammarAccess.getSelectAccess().getGROUPKeyword_6_0());
                    			
                    otherlv_14=(Token)match(input,BY,FOLLOW_14); 

                    				newLeafNode(otherlv_14, grammarAccess.getSelectAccess().getBYKeyword_6_1());
                    			
                    // InternalCQLParser.g:562:4: ( (lv_order_15_0= ruleAttribute ) )+
                    int cnt14=0;
                    loop14:
                    do {
                        int alt14=2;
                        int LA14_0 = input.LA(1);

                        if ( (LA14_0==RULE_ID) ) {
                            alt14=1;
                        }


                        switch (alt14) {
                    	case 1 :
                    	    // InternalCQLParser.g:563:5: (lv_order_15_0= ruleAttribute )
                    	    {
                    	    // InternalCQLParser.g:563:5: (lv_order_15_0= ruleAttribute )
                    	    // InternalCQLParser.g:564:6: lv_order_15_0= ruleAttribute
                    	    {

                    	    						newCompositeNode(grammarAccess.getSelectAccess().getOrderAttributeParserRuleCall_6_2_0());
                    	    					
                    	    pushFollow(FOLLOW_15);
                    	    lv_order_15_0=ruleAttribute();

                    	    state._fsp--;


                    	    						if (current==null) {
                    	    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    						}
                    	    						add(
                    	    							current,
                    	    							"order",
                    	    							lv_order_15_0,
                    	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    	    						afterParserOrEnumRuleCall();
                    	    					

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

                    // InternalCQLParser.g:581:4: (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )*
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( (LA15_0==Comma) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // InternalCQLParser.g:582:5: otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) )
                    	    {
                    	    otherlv_16=(Token)match(input,Comma,FOLLOW_14); 

                    	    					newLeafNode(otherlv_16, grammarAccess.getSelectAccess().getCommaKeyword_6_3_0());
                    	    				
                    	    // InternalCQLParser.g:586:5: ( (lv_order_17_0= ruleAttribute ) )
                    	    // InternalCQLParser.g:587:6: (lv_order_17_0= ruleAttribute )
                    	    {
                    	    // InternalCQLParser.g:587:6: (lv_order_17_0= ruleAttribute )
                    	    // InternalCQLParser.g:588:7: lv_order_17_0= ruleAttribute
                    	    {

                    	    							newCompositeNode(grammarAccess.getSelectAccess().getOrderAttributeParserRuleCall_6_3_1_0());
                    	    						
                    	    pushFollow(FOLLOW_16);
                    	    lv_order_17_0=ruleAttribute();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"order",
                    	    								lv_order_17_0,
                    	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
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
                    break;

            }

            // InternalCQLParser.g:607:3: (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==HAVING) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // InternalCQLParser.g:608:4: otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) )
                    {
                    otherlv_18=(Token)match(input,HAVING,FOLLOW_11); 

                    				newLeafNode(otherlv_18, grammarAccess.getSelectAccess().getHAVINGKeyword_7_0());
                    			
                    // InternalCQLParser.g:612:4: ( (lv_having_19_0= ruleExpressionsModel ) )
                    // InternalCQLParser.g:613:5: (lv_having_19_0= ruleExpressionsModel )
                    {
                    // InternalCQLParser.g:613:5: (lv_having_19_0= ruleExpressionsModel )
                    // InternalCQLParser.g:614:6: lv_having_19_0= ruleExpressionsModel
                    {

                    						newCompositeNode(grammarAccess.getSelectAccess().getHavingExpressionsModelParserRuleCall_7_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_having_19_0=ruleExpressionsModel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    						}
                    						set(
                    							current,
                    							"having",
                    							lv_having_19_0,
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


    // $ANTLR start "entryRuleInnerSelect"
    // InternalCQLParser.g:636:1: entryRuleInnerSelect returns [EObject current=null] : iv_ruleInnerSelect= ruleInnerSelect EOF ;
    public final EObject entryRuleInnerSelect() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInnerSelect = null;


        try {
            // InternalCQLParser.g:636:52: (iv_ruleInnerSelect= ruleInnerSelect EOF )
            // InternalCQLParser.g:637:2: iv_ruleInnerSelect= ruleInnerSelect EOF
            {
             newCompositeNode(grammarAccess.getInnerSelectRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleInnerSelect=ruleInnerSelect();

            state._fsp--;

             current =iv_ruleInnerSelect; 
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
    // $ANTLR end "entryRuleInnerSelect"


    // $ANTLR start "ruleInnerSelect"
    // InternalCQLParser.g:643:1: ruleInnerSelect returns [EObject current=null] : (otherlv_0= LeftParenthesis ( (lv_select_1_0= ruleSelect ) ) otherlv_2= RightParenthesis ) ;
    public final EObject ruleInnerSelect() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject lv_select_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:649:2: ( (otherlv_0= LeftParenthesis ( (lv_select_1_0= ruleSelect ) ) otherlv_2= RightParenthesis ) )
            // InternalCQLParser.g:650:2: (otherlv_0= LeftParenthesis ( (lv_select_1_0= ruleSelect ) ) otherlv_2= RightParenthesis )
            {
            // InternalCQLParser.g:650:2: (otherlv_0= LeftParenthesis ( (lv_select_1_0= ruleSelect ) ) otherlv_2= RightParenthesis )
            // InternalCQLParser.g:651:3: otherlv_0= LeftParenthesis ( (lv_select_1_0= ruleSelect ) ) otherlv_2= RightParenthesis
            {
            otherlv_0=(Token)match(input,LeftParenthesis,FOLLOW_17); 

            			newLeafNode(otherlv_0, grammarAccess.getInnerSelectAccess().getLeftParenthesisKeyword_0());
            		
            // InternalCQLParser.g:655:3: ( (lv_select_1_0= ruleSelect ) )
            // InternalCQLParser.g:656:4: (lv_select_1_0= ruleSelect )
            {
            // InternalCQLParser.g:656:4: (lv_select_1_0= ruleSelect )
            // InternalCQLParser.g:657:5: lv_select_1_0= ruleSelect
            {

            					newCompositeNode(grammarAccess.getInnerSelectAccess().getSelectSelectParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_18);
            lv_select_1_0=ruleSelect();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getInnerSelectRule());
            					}
            					set(
            						current,
            						"select",
            						lv_select_1_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Select");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_2=(Token)match(input,RightParenthesis,FOLLOW_2); 

            			newLeafNode(otherlv_2, grammarAccess.getInnerSelectAccess().getRightParenthesisKeyword_2());
            		

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
    // $ANTLR end "ruleInnerSelect"


    // $ANTLR start "entryRuleInnerSelect2"
    // InternalCQLParser.g:682:1: entryRuleInnerSelect2 returns [EObject current=null] : iv_ruleInnerSelect2= ruleInnerSelect2 EOF ;
    public final EObject entryRuleInnerSelect2() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInnerSelect2 = null;


        try {
            // InternalCQLParser.g:682:53: (iv_ruleInnerSelect2= ruleInnerSelect2 EOF )
            // InternalCQLParser.g:683:2: iv_ruleInnerSelect2= ruleInnerSelect2 EOF
            {
             newCompositeNode(grammarAccess.getInnerSelect2Rule()); 
            pushFollow(FOLLOW_1);
            iv_ruleInnerSelect2=ruleInnerSelect2();

            state._fsp--;

             current =iv_ruleInnerSelect2; 
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
    // $ANTLR end "entryRuleInnerSelect2"


    // $ANTLR start "ruleInnerSelect2"
    // InternalCQLParser.g:689:1: ruleInnerSelect2 returns [EObject current=null] : ( (lv_select_0_0= ruleSelect ) ) ;
    public final EObject ruleInnerSelect2() throws RecognitionException {
        EObject current = null;

        EObject lv_select_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:695:2: ( ( (lv_select_0_0= ruleSelect ) ) )
            // InternalCQLParser.g:696:2: ( (lv_select_0_0= ruleSelect ) )
            {
            // InternalCQLParser.g:696:2: ( (lv_select_0_0= ruleSelect ) )
            // InternalCQLParser.g:697:3: (lv_select_0_0= ruleSelect )
            {
            // InternalCQLParser.g:697:3: (lv_select_0_0= ruleSelect )
            // InternalCQLParser.g:698:4: lv_select_0_0= ruleSelect
            {

            				newCompositeNode(grammarAccess.getInnerSelect2Access().getSelectSelectParserRuleCall_0());
            			
            pushFollow(FOLLOW_2);
            lv_select_0_0=ruleSelect();

            state._fsp--;


            				if (current==null) {
            					current = createModelElementForParent(grammarAccess.getInnerSelect2Rule());
            				}
            				set(
            					current,
            					"select",
            					lv_select_0_0,
            					"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Select");
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
    // $ANTLR end "ruleInnerSelect2"


    // $ANTLR start "entryRuleSelectArgument"
    // InternalCQLParser.g:718:1: entryRuleSelectArgument returns [EObject current=null] : iv_ruleSelectArgument= ruleSelectArgument EOF ;
    public final EObject entryRuleSelectArgument() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectArgument = null;


        try {
            // InternalCQLParser.g:718:55: (iv_ruleSelectArgument= ruleSelectArgument EOF )
            // InternalCQLParser.g:719:2: iv_ruleSelectArgument= ruleSelectArgument EOF
            {
             newCompositeNode(grammarAccess.getSelectArgumentRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelectArgument=ruleSelectArgument();

            state._fsp--;

             current =iv_ruleSelectArgument; 
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
    // $ANTLR end "entryRuleSelectArgument"


    // $ANTLR start "ruleSelectArgument"
    // InternalCQLParser.g:725:1: ruleSelectArgument returns [EObject current=null] : ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) ) ;
    public final EObject ruleSelectArgument() throws RecognitionException {
        EObject current = null;

        EObject lv_attribute_0_0 = null;

        EObject lv_expression_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:731:2: ( ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) ) )
            // InternalCQLParser.g:732:2: ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) )
            {
            // InternalCQLParser.g:732:2: ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) )
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==RULE_ID) ) {
                switch ( input.LA(2) ) {
                case FullStop:
                    {
                    int LA18_3 = input.LA(3);

                    if ( (LA18_3==Asterisk) ) {
                        int LA18_5 = input.LA(4);

                        if ( (LA18_5==EOF||LA18_5==FALSE||LA18_5==FROM||LA18_5==TRUE||LA18_5==AS||LA18_5==Comma||(LA18_5>=RULE_ID && LA18_5<=RULE_STRING)) ) {
                            alt18=1;
                        }
                        else if ( ((LA18_5>=Asterisk && LA18_5<=PlusSign)||LA18_5==HyphenMinus||LA18_5==Solidus||LA18_5==CircumflexAccent) ) {
                            alt18=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 18, 5, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA18_3==RULE_ID) ) {
                        int LA18_6 = input.LA(4);

                        if ( (LA18_6==EOF||LA18_6==FALSE||LA18_6==FROM||LA18_6==TRUE||LA18_6==AS||LA18_6==Comma||(LA18_6>=RULE_ID && LA18_6<=RULE_STRING)) ) {
                            alt18=1;
                        }
                        else if ( ((LA18_6>=Asterisk && LA18_6<=PlusSign)||LA18_6==HyphenMinus||LA18_6==Solidus||LA18_6==CircumflexAccent) ) {
                            alt18=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 18, 6, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 18, 3, input);

                        throw nvae;
                    }
                    }
                    break;
                case LeftParenthesis:
                case Asterisk:
                case PlusSign:
                case HyphenMinus:
                case Solidus:
                case CircumflexAccent:
                    {
                    alt18=2;
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
                    alt18=1;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA18_0==FALSE||LA18_0==TRUE||(LA18_0>=RULE_INT && LA18_0<=RULE_STRING)) ) {
                alt18=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;
            }
            switch (alt18) {
                case 1 :
                    // InternalCQLParser.g:733:3: ( (lv_attribute_0_0= ruleAttribute ) )
                    {
                    // InternalCQLParser.g:733:3: ( (lv_attribute_0_0= ruleAttribute ) )
                    // InternalCQLParser.g:734:4: (lv_attribute_0_0= ruleAttribute )
                    {
                    // InternalCQLParser.g:734:4: (lv_attribute_0_0= ruleAttribute )
                    // InternalCQLParser.g:735:5: lv_attribute_0_0= ruleAttribute
                    {

                    					newCompositeNode(grammarAccess.getSelectArgumentAccess().getAttributeAttributeParserRuleCall_0_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_attribute_0_0=ruleAttribute();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSelectArgumentRule());
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
                    // InternalCQLParser.g:753:3: ( (lv_expression_1_0= ruleSelectExpression ) )
                    {
                    // InternalCQLParser.g:753:3: ( (lv_expression_1_0= ruleSelectExpression ) )
                    // InternalCQLParser.g:754:4: (lv_expression_1_0= ruleSelectExpression )
                    {
                    // InternalCQLParser.g:754:4: (lv_expression_1_0= ruleSelectExpression )
                    // InternalCQLParser.g:755:5: lv_expression_1_0= ruleSelectExpression
                    {

                    					newCompositeNode(grammarAccess.getSelectArgumentAccess().getExpressionSelectExpressionParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_expression_1_0=ruleSelectExpression();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSelectArgumentRule());
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
    // $ANTLR end "ruleSelectArgument"


    // $ANTLR start "entryRuleSource"
    // InternalCQLParser.g:776:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalCQLParser.g:776:47: (iv_ruleSource= ruleSource EOF )
            // InternalCQLParser.g:777:2: iv_ruleSource= ruleSource EOF
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
    // InternalCQLParser.g:783:1: ruleSource returns [EObject current=null] : (this_SimpleSource_0= ruleSimpleSource | this_NestedSource_1= ruleNestedSource ) ;
    public final EObject ruleSource() throws RecognitionException {
        EObject current = null;

        EObject this_SimpleSource_0 = null;

        EObject this_NestedSource_1 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:789:2: ( (this_SimpleSource_0= ruleSimpleSource | this_NestedSource_1= ruleNestedSource ) )
            // InternalCQLParser.g:790:2: (this_SimpleSource_0= ruleSimpleSource | this_NestedSource_1= ruleNestedSource )
            {
            // InternalCQLParser.g:790:2: (this_SimpleSource_0= ruleSimpleSource | this_NestedSource_1= ruleNestedSource )
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
                    // InternalCQLParser.g:791:3: this_SimpleSource_0= ruleSimpleSource
                    {

                    			newCompositeNode(grammarAccess.getSourceAccess().getSimpleSourceParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_SimpleSource_0=ruleSimpleSource();

                    state._fsp--;


                    			current = this_SimpleSource_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:800:3: this_NestedSource_1= ruleNestedSource
                    {

                    			newCompositeNode(grammarAccess.getSourceAccess().getNestedSourceParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_NestedSource_1=ruleNestedSource();

                    state._fsp--;


                    			current = this_NestedSource_1;
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
    // $ANTLR end "ruleSource"


    // $ANTLR start "entryRuleSimpleSource"
    // InternalCQLParser.g:812:1: entryRuleSimpleSource returns [EObject current=null] : iv_ruleSimpleSource= ruleSimpleSource EOF ;
    public final EObject entryRuleSimpleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSimpleSource = null;


        try {
            // InternalCQLParser.g:812:53: (iv_ruleSimpleSource= ruleSimpleSource EOF )
            // InternalCQLParser.g:813:2: iv_ruleSimpleSource= ruleSimpleSource EOF
            {
             newCompositeNode(grammarAccess.getSimpleSourceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSimpleSource=ruleSimpleSource();

            state._fsp--;

             current =iv_ruleSimpleSource; 
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
    // $ANTLR end "entryRuleSimpleSource"


    // $ANTLR start "ruleSimpleSource"
    // InternalCQLParser.g:819:1: ruleSimpleSource returns [EObject current=null] : ( () ( (lv_name_1_0= ruleQualifiedSourcename ) ) ( (lv_window_2_0= ruleWindowOperator ) )? (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )? ) ;
    public final EObject ruleSimpleSource() throws RecognitionException {
        EObject current = null;

        Token otherlv_3=null;
        AntlrDatatypeRuleToken lv_name_1_0 = null;

        EObject lv_window_2_0 = null;

        EObject lv_alias_4_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:825:2: ( ( () ( (lv_name_1_0= ruleQualifiedSourcename ) ) ( (lv_window_2_0= ruleWindowOperator ) )? (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:826:2: ( () ( (lv_name_1_0= ruleQualifiedSourcename ) ) ( (lv_window_2_0= ruleWindowOperator ) )? (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:826:2: ( () ( (lv_name_1_0= ruleQualifiedSourcename ) ) ( (lv_window_2_0= ruleWindowOperator ) )? (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:827:3: () ( (lv_name_1_0= ruleQualifiedSourcename ) ) ( (lv_window_2_0= ruleWindowOperator ) )? (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:827:3: ()
            // InternalCQLParser.g:828:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getSimpleSourceAccess().getSimpleSourceAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:834:3: ( (lv_name_1_0= ruleQualifiedSourcename ) )
            // InternalCQLParser.g:835:4: (lv_name_1_0= ruleQualifiedSourcename )
            {
            // InternalCQLParser.g:835:4: (lv_name_1_0= ruleQualifiedSourcename )
            // InternalCQLParser.g:836:5: lv_name_1_0= ruleQualifiedSourcename
            {

            					newCompositeNode(grammarAccess.getSimpleSourceAccess().getNameQualifiedSourcenameParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_19);
            lv_name_1_0=ruleQualifiedSourcename();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSimpleSourceRule());
            					}
            					set(
            						current,
            						"name",
            						lv_name_1_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.QualifiedSourcename");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:853:3: ( (lv_window_2_0= ruleWindowOperator ) )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==LeftSquareBracket) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // InternalCQLParser.g:854:4: (lv_window_2_0= ruleWindowOperator )
                    {
                    // InternalCQLParser.g:854:4: (lv_window_2_0= ruleWindowOperator )
                    // InternalCQLParser.g:855:5: lv_window_2_0= ruleWindowOperator
                    {

                    					newCompositeNode(grammarAccess.getSimpleSourceAccess().getWindowWindowOperatorParserRuleCall_2_0());
                    				
                    pushFollow(FOLLOW_20);
                    lv_window_2_0=ruleWindowOperator();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSimpleSourceRule());
                    					}
                    					set(
                    						current,
                    						"window",
                    						lv_window_2_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.WindowOperator");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalCQLParser.g:872:3: (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==AS) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // InternalCQLParser.g:873:4: otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) )
                    {
                    otherlv_3=(Token)match(input,AS,FOLLOW_14); 

                    				newLeafNode(otherlv_3, grammarAccess.getSimpleSourceAccess().getASKeyword_3_0());
                    			
                    // InternalCQLParser.g:877:4: ( (lv_alias_4_0= ruleAlias ) )
                    // InternalCQLParser.g:878:5: (lv_alias_4_0= ruleAlias )
                    {
                    // InternalCQLParser.g:878:5: (lv_alias_4_0= ruleAlias )
                    // InternalCQLParser.g:879:6: lv_alias_4_0= ruleAlias
                    {

                    						newCompositeNode(grammarAccess.getSimpleSourceAccess().getAliasAliasParserRuleCall_3_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_alias_4_0=ruleAlias();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSimpleSourceRule());
                    						}
                    						set(
                    							current,
                    							"alias",
                    							lv_alias_4_0,
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
    // $ANTLR end "ruleSimpleSource"


    // $ANTLR start "entryRuleNestedSource"
    // InternalCQLParser.g:901:1: entryRuleNestedSource returns [EObject current=null] : iv_ruleNestedSource= ruleNestedSource EOF ;
    public final EObject entryRuleNestedSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNestedSource = null;


        try {
            // InternalCQLParser.g:901:53: (iv_ruleNestedSource= ruleNestedSource EOF )
            // InternalCQLParser.g:902:2: iv_ruleNestedSource= ruleNestedSource EOF
            {
             newCompositeNode(grammarAccess.getNestedSourceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleNestedSource=ruleNestedSource();

            state._fsp--;

             current =iv_ruleNestedSource; 
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
    // $ANTLR end "entryRuleNestedSource"


    // $ANTLR start "ruleNestedSource"
    // InternalCQLParser.g:908:1: ruleNestedSource returns [EObject current=null] : ( () ( (lv_statement_1_0= ruleInnerSelect ) ) otherlv_2= AS ( (lv_alias_3_0= ruleAlias ) ) ) ;
    public final EObject ruleNestedSource() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject lv_statement_1_0 = null;

        EObject lv_alias_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:914:2: ( ( () ( (lv_statement_1_0= ruleInnerSelect ) ) otherlv_2= AS ( (lv_alias_3_0= ruleAlias ) ) ) )
            // InternalCQLParser.g:915:2: ( () ( (lv_statement_1_0= ruleInnerSelect ) ) otherlv_2= AS ( (lv_alias_3_0= ruleAlias ) ) )
            {
            // InternalCQLParser.g:915:2: ( () ( (lv_statement_1_0= ruleInnerSelect ) ) otherlv_2= AS ( (lv_alias_3_0= ruleAlias ) ) )
            // InternalCQLParser.g:916:3: () ( (lv_statement_1_0= ruleInnerSelect ) ) otherlv_2= AS ( (lv_alias_3_0= ruleAlias ) )
            {
            // InternalCQLParser.g:916:3: ()
            // InternalCQLParser.g:917:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getNestedSourceAccess().getNestedSourceAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:923:3: ( (lv_statement_1_0= ruleInnerSelect ) )
            // InternalCQLParser.g:924:4: (lv_statement_1_0= ruleInnerSelect )
            {
            // InternalCQLParser.g:924:4: (lv_statement_1_0= ruleInnerSelect )
            // InternalCQLParser.g:925:5: lv_statement_1_0= ruleInnerSelect
            {

            					newCompositeNode(grammarAccess.getNestedSourceAccess().getStatementInnerSelectParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_21);
            lv_statement_1_0=ruleInnerSelect();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getNestedSourceRule());
            					}
            					set(
            						current,
            						"statement",
            						lv_statement_1_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.InnerSelect");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_2=(Token)match(input,AS,FOLLOW_14); 

            			newLeafNode(otherlv_2, grammarAccess.getNestedSourceAccess().getASKeyword_2());
            		
            // InternalCQLParser.g:946:3: ( (lv_alias_3_0= ruleAlias ) )
            // InternalCQLParser.g:947:4: (lv_alias_3_0= ruleAlias )
            {
            // InternalCQLParser.g:947:4: (lv_alias_3_0= ruleAlias )
            // InternalCQLParser.g:948:5: lv_alias_3_0= ruleAlias
            {

            					newCompositeNode(grammarAccess.getNestedSourceAccess().getAliasAliasParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_alias_3_0=ruleAlias();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getNestedSourceRule());
            					}
            					set(
            						current,
            						"alias",
            						lv_alias_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Alias");
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
    // $ANTLR end "ruleNestedSource"


    // $ANTLR start "entryRuleQualifiedSourcename"
    // InternalCQLParser.g:969:1: entryRuleQualifiedSourcename returns [String current=null] : iv_ruleQualifiedSourcename= ruleQualifiedSourcename EOF ;
    public final String entryRuleQualifiedSourcename() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedSourcename = null;


        try {
            // InternalCQLParser.g:969:59: (iv_ruleQualifiedSourcename= ruleQualifiedSourcename EOF )
            // InternalCQLParser.g:970:2: iv_ruleQualifiedSourcename= ruleQualifiedSourcename EOF
            {
             newCompositeNode(grammarAccess.getQualifiedSourcenameRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleQualifiedSourcename=ruleQualifiedSourcename();

            state._fsp--;

             current =iv_ruleQualifiedSourcename.getText(); 
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
    // $ANTLR end "entryRuleQualifiedSourcename"


    // $ANTLR start "ruleQualifiedSourcename"
    // InternalCQLParser.g:976:1: ruleQualifiedSourcename returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : this_ID_0= RULE_ID ;
    public final AntlrDatatypeRuleToken ruleQualifiedSourcename() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:982:2: (this_ID_0= RULE_ID )
            // InternalCQLParser.g:983:2: this_ID_0= RULE_ID
            {
            this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            		current.merge(this_ID_0);
            	

            		newLeafNode(this_ID_0, grammarAccess.getQualifiedSourcenameAccess().getIDTerminalRuleCall());
            	

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
    // $ANTLR end "ruleQualifiedSourcename"


    // $ANTLR start "entryRuleAttribute"
    // InternalCQLParser.g:993:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCQLParser.g:993:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCQLParser.g:994:2: iv_ruleAttribute= ruleAttribute EOF
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
    // InternalCQLParser.g:1000:1: ruleAttribute returns [EObject current=null] : ( ( (lv_name_0_0= ruleQualifiedAttributename ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_name_0_0 = null;

        EObject lv_alias_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1006:2: ( ( ( (lv_name_0_0= ruleQualifiedAttributename ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:1007:2: ( ( (lv_name_0_0= ruleQualifiedAttributename ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:1007:2: ( ( (lv_name_0_0= ruleQualifiedAttributename ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:1008:3: ( (lv_name_0_0= ruleQualifiedAttributename ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:1008:3: ( (lv_name_0_0= ruleQualifiedAttributename ) )
            // InternalCQLParser.g:1009:4: (lv_name_0_0= ruleQualifiedAttributename )
            {
            // InternalCQLParser.g:1009:4: (lv_name_0_0= ruleQualifiedAttributename )
            // InternalCQLParser.g:1010:5: lv_name_0_0= ruleQualifiedAttributename
            {

            					newCompositeNode(grammarAccess.getAttributeAccess().getNameQualifiedAttributenameParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_20);
            lv_name_0_0=ruleQualifiedAttributename();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAttributeRule());
            					}
            					set(
            						current,
            						"name",
            						lv_name_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.QualifiedAttributename");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:1027:3: (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==AS) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // InternalCQLParser.g:1028:4: otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) )
                    {
                    otherlv_1=(Token)match(input,AS,FOLLOW_14); 

                    				newLeafNode(otherlv_1, grammarAccess.getAttributeAccess().getASKeyword_1_0());
                    			
                    // InternalCQLParser.g:1032:4: ( (lv_alias_2_0= ruleAlias ) )
                    // InternalCQLParser.g:1033:5: (lv_alias_2_0= ruleAlias )
                    {
                    // InternalCQLParser.g:1033:5: (lv_alias_2_0= ruleAlias )
                    // InternalCQLParser.g:1034:6: lv_alias_2_0= ruleAlias
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
    // InternalCQLParser.g:1056:1: entryRuleAttributeWithoutAliasDefinition returns [EObject current=null] : iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF ;
    public final EObject entryRuleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithoutAliasDefinition = null;


        try {
            // InternalCQLParser.g:1056:72: (iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF )
            // InternalCQLParser.g:1057:2: iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF
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
    // InternalCQLParser.g:1063:1: ruleAttributeWithoutAliasDefinition returns [EObject current=null] : ( (lv_name_0_0= ruleQualifiedAttributename ) ) ;
    public final EObject ruleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_name_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1069:2: ( ( (lv_name_0_0= ruleQualifiedAttributename ) ) )
            // InternalCQLParser.g:1070:2: ( (lv_name_0_0= ruleQualifiedAttributename ) )
            {
            // InternalCQLParser.g:1070:2: ( (lv_name_0_0= ruleQualifiedAttributename ) )
            // InternalCQLParser.g:1071:3: (lv_name_0_0= ruleQualifiedAttributename )
            {
            // InternalCQLParser.g:1071:3: (lv_name_0_0= ruleQualifiedAttributename )
            // InternalCQLParser.g:1072:4: lv_name_0_0= ruleQualifiedAttributename
            {

            				newCompositeNode(grammarAccess.getAttributeWithoutAliasDefinitionAccess().getNameQualifiedAttributenameParserRuleCall_0());
            			
            pushFollow(FOLLOW_2);
            lv_name_0_0=ruleQualifiedAttributename();

            state._fsp--;


            				if (current==null) {
            					current = createModelElementForParent(grammarAccess.getAttributeWithoutAliasDefinitionRule());
            				}
            				set(
            					current,
            					"name",
            					lv_name_0_0,
            					"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.QualifiedAttributename");
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


    // $ANTLR start "entryRuleQualifiedAttributename"
    // InternalCQLParser.g:1092:1: entryRuleQualifiedAttributename returns [String current=null] : iv_ruleQualifiedAttributename= ruleQualifiedAttributename EOF ;
    public final String entryRuleQualifiedAttributename() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedAttributename = null;


        try {
            // InternalCQLParser.g:1092:62: (iv_ruleQualifiedAttributename= ruleQualifiedAttributename EOF )
            // InternalCQLParser.g:1093:2: iv_ruleQualifiedAttributename= ruleQualifiedAttributename EOF
            {
             newCompositeNode(grammarAccess.getQualifiedAttributenameRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleQualifiedAttributename=ruleQualifiedAttributename();

            state._fsp--;

             current =iv_ruleQualifiedAttributename.getText(); 
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
    // $ANTLR end "entryRuleQualifiedAttributename"


    // $ANTLR start "ruleQualifiedAttributename"
    // InternalCQLParser.g:1099:1: ruleQualifiedAttributename returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) | (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk ) ) ;
    public final AntlrDatatypeRuleToken ruleQualifiedAttributename() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_3=null;
        AntlrDatatypeRuleToken this_QualifiedSourcename_1 = null;

        AntlrDatatypeRuleToken this_QualifiedSourcename_4 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1105:2: ( (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) | (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk ) ) )
            // InternalCQLParser.g:1106:2: (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) | (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk ) )
            {
            // InternalCQLParser.g:1106:2: (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) | (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk ) )
            int alt23=3;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==RULE_ID) ) {
                int LA23_1 = input.LA(2);

                if ( (LA23_1==FullStop) ) {
                    int LA23_2 = input.LA(3);

                    if ( (LA23_2==RULE_ID) ) {
                        alt23=2;
                    }
                    else if ( (LA23_2==Asterisk) ) {
                        alt23=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 23, 2, input);

                        throw nvae;
                    }
                }
                else if ( (LA23_1==EOF||(LA23_1>=ATTACH && LA23_1<=CREATE)||(LA23_1>=HAVING && LA23_1<=SELECT)||LA23_1==STREAM||(LA23_1>=ALTER && LA23_1<=GROUP)||LA23_1==DROP||LA23_1==FROM||LA23_1==TRUE||LA23_1==AND||(LA23_1>=ExclamationMarkEqualsSign && LA23_1<=AS)||LA23_1==IN||LA23_1==OR||(LA23_1>=RightParenthesis && LA23_1<=HyphenMinus)||LA23_1==Solidus||(LA23_1>=Semicolon && LA23_1<=GreaterThanSign)||(LA23_1>=RightSquareBracket && LA23_1<=RULE_STRING)) ) {
                    alt23=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 23, 1, input);

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
                    // InternalCQLParser.g:1107:3: this_ID_0= RULE_ID
                    {
                    this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    			current.merge(this_ID_0);
                    		

                    			newLeafNode(this_ID_0, grammarAccess.getQualifiedAttributenameAccess().getIDTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1115:3: (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID )
                    {
                    // InternalCQLParser.g:1115:3: (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID )
                    // InternalCQLParser.g:1116:4: this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID
                    {

                    				newCompositeNode(grammarAccess.getQualifiedAttributenameAccess().getQualifiedSourcenameParserRuleCall_1_0());
                    			
                    pushFollow(FOLLOW_22);
                    this_QualifiedSourcename_1=ruleQualifiedSourcename();

                    state._fsp--;


                    				current.merge(this_QualifiedSourcename_1);
                    			

                    				afterParserOrEnumRuleCall();
                    			
                    kw=(Token)match(input,FullStop,FOLLOW_14); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getQualifiedAttributenameAccess().getFullStopKeyword_1_1());
                    			
                    this_ID_3=(Token)match(input,RULE_ID,FOLLOW_2); 

                    				current.merge(this_ID_3);
                    			

                    				newLeafNode(this_ID_3, grammarAccess.getQualifiedAttributenameAccess().getIDTerminalRuleCall_1_2());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:1140:3: (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk )
                    {
                    // InternalCQLParser.g:1140:3: (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk )
                    // InternalCQLParser.g:1141:4: this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk
                    {

                    				newCompositeNode(grammarAccess.getQualifiedAttributenameAccess().getQualifiedSourcenameParserRuleCall_2_0());
                    			
                    pushFollow(FOLLOW_22);
                    this_QualifiedSourcename_4=ruleQualifiedSourcename();

                    state._fsp--;


                    				current.merge(this_QualifiedSourcename_4);
                    			

                    				afterParserOrEnumRuleCall();
                    			
                    kw=(Token)match(input,FullStop,FOLLOW_23); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getQualifiedAttributenameAccess().getFullStopKeyword_2_1());
                    			
                    kw=(Token)match(input,Asterisk,FOLLOW_2); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getQualifiedAttributenameAccess().getAsteriskKeyword_2_2());
                    			

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
    // $ANTLR end "ruleQualifiedAttributename"


    // $ANTLR start "entryRuleAttributeWithNestedStatement"
    // InternalCQLParser.g:1166:1: entryRuleAttributeWithNestedStatement returns [EObject current=null] : iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF ;
    public final EObject entryRuleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithNestedStatement = null;


        try {
            // InternalCQLParser.g:1166:69: (iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF )
            // InternalCQLParser.g:1167:2: iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF
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
    // InternalCQLParser.g:1173:1: ruleAttributeWithNestedStatement returns [EObject current=null] : ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleInnerSelect ) ) ) ;
    public final EObject ruleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_value_0_0 = null;

        EObject lv_nested_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1179:2: ( ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleInnerSelect ) ) ) )
            // InternalCQLParser.g:1180:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleInnerSelect ) ) )
            {
            // InternalCQLParser.g:1180:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleInnerSelect ) ) )
            // InternalCQLParser.g:1181:3: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleInnerSelect ) )
            {
            // InternalCQLParser.g:1181:3: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQLParser.g:1182:4: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1182:4: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQLParser.g:1183:5: lv_value_0_0= ruleAttributeWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getAttributeWithNestedStatementAccess().getValueAttributeWithoutAliasDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_24);
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

            otherlv_1=(Token)match(input,IN,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getAttributeWithNestedStatementAccess().getINKeyword_1());
            		
            // InternalCQLParser.g:1204:3: ( (lv_nested_2_0= ruleInnerSelect ) )
            // InternalCQLParser.g:1205:4: (lv_nested_2_0= ruleInnerSelect )
            {
            // InternalCQLParser.g:1205:4: (lv_nested_2_0= ruleInnerSelect )
            // InternalCQLParser.g:1206:5: lv_nested_2_0= ruleInnerSelect
            {

            					newCompositeNode(grammarAccess.getAttributeWithNestedStatementAccess().getNestedInnerSelectParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_2);
            lv_nested_2_0=ruleInnerSelect();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAttributeWithNestedStatementRule());
            					}
            					set(
            						current,
            						"nested",
            						lv_nested_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.InnerSelect");
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


    // $ANTLR start "entryRuleAndOperator"
    // InternalCQLParser.g:1227:1: entryRuleAndOperator returns [String current=null] : iv_ruleAndOperator= ruleAndOperator EOF ;
    public final String entryRuleAndOperator() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleAndOperator = null;


        try {
            // InternalCQLParser.g:1227:51: (iv_ruleAndOperator= ruleAndOperator EOF )
            // InternalCQLParser.g:1228:2: iv_ruleAndOperator= ruleAndOperator EOF
            {
             newCompositeNode(grammarAccess.getAndOperatorRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAndOperator=ruleAndOperator();

            state._fsp--;

             current =iv_ruleAndOperator.getText(); 
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
    // $ANTLR end "entryRuleAndOperator"


    // $ANTLR start "ruleAndOperator"
    // InternalCQLParser.g:1234:1: ruleAndOperator returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= AND ;
    public final AntlrDatatypeRuleToken ruleAndOperator() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1240:2: (kw= AND )
            // InternalCQLParser.g:1241:2: kw= AND
            {
            kw=(Token)match(input,AND,FOLLOW_2); 

            		current.merge(kw);
            		newLeafNode(kw, grammarAccess.getAndOperatorAccess().getANDKeyword());
            	

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
    // $ANTLR end "ruleAndOperator"


    // $ANTLR start "entryRuleOrOperator"
    // InternalCQLParser.g:1249:1: entryRuleOrOperator returns [String current=null] : iv_ruleOrOperator= ruleOrOperator EOF ;
    public final String entryRuleOrOperator() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOrOperator = null;


        try {
            // InternalCQLParser.g:1249:50: (iv_ruleOrOperator= ruleOrOperator EOF )
            // InternalCQLParser.g:1250:2: iv_ruleOrOperator= ruleOrOperator EOF
            {
             newCompositeNode(grammarAccess.getOrOperatorRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleOrOperator=ruleOrOperator();

            state._fsp--;

             current =iv_ruleOrOperator.getText(); 
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
    // $ANTLR end "entryRuleOrOperator"


    // $ANTLR start "ruleOrOperator"
    // InternalCQLParser.g:1256:1: ruleOrOperator returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= OR ;
    public final AntlrDatatypeRuleToken ruleOrOperator() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1262:2: (kw= OR )
            // InternalCQLParser.g:1263:2: kw= OR
            {
            kw=(Token)match(input,OR,FOLLOW_2); 

            		current.merge(kw);
            		newLeafNode(kw, grammarAccess.getOrOperatorAccess().getORKeyword());
            	

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
    // $ANTLR end "ruleOrOperator"


    // $ANTLR start "entryRuleEQUALITIY_OPERATOR"
    // InternalCQLParser.g:1271:1: entryRuleEQUALITIY_OPERATOR returns [String current=null] : iv_ruleEQUALITIY_OPERATOR= ruleEQUALITIY_OPERATOR EOF ;
    public final String entryRuleEQUALITIY_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleEQUALITIY_OPERATOR = null;


        try {
            // InternalCQLParser.g:1271:58: (iv_ruleEQUALITIY_OPERATOR= ruleEQUALITIY_OPERATOR EOF )
            // InternalCQLParser.g:1272:2: iv_ruleEQUALITIY_OPERATOR= ruleEQUALITIY_OPERATOR EOF
            {
             newCompositeNode(grammarAccess.getEQUALITIY_OPERATORRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEQUALITIY_OPERATOR=ruleEQUALITIY_OPERATOR();

            state._fsp--;

             current =iv_ruleEQUALITIY_OPERATOR.getText(); 
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
    // $ANTLR end "entryRuleEQUALITIY_OPERATOR"


    // $ANTLR start "ruleEQUALITIY_OPERATOR"
    // InternalCQLParser.g:1278:1: ruleEQUALITIY_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= EqualsSign | kw= ExclamationMarkEqualsSign ) ;
    public final AntlrDatatypeRuleToken ruleEQUALITIY_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1284:2: ( (kw= EqualsSign | kw= ExclamationMarkEqualsSign ) )
            // InternalCQLParser.g:1285:2: (kw= EqualsSign | kw= ExclamationMarkEqualsSign )
            {
            // InternalCQLParser.g:1285:2: (kw= EqualsSign | kw= ExclamationMarkEqualsSign )
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==EqualsSign) ) {
                alt24=1;
            }
            else if ( (LA24_0==ExclamationMarkEqualsSign) ) {
                alt24=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;
            }
            switch (alt24) {
                case 1 :
                    // InternalCQLParser.g:1286:3: kw= EqualsSign
                    {
                    kw=(Token)match(input,EqualsSign,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getEQUALITIY_OPERATORAccess().getEqualsSignKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1292:3: kw= ExclamationMarkEqualsSign
                    {
                    kw=(Token)match(input,ExclamationMarkEqualsSign,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getEQUALITIY_OPERATORAccess().getExclamationMarkEqualsSignKeyword_1());
                    		

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
    // $ANTLR end "ruleEQUALITIY_OPERATOR"


    // $ANTLR start "entryRuleCOMPARE_OPERATOR"
    // InternalCQLParser.g:1301:1: entryRuleCOMPARE_OPERATOR returns [String current=null] : iv_ruleCOMPARE_OPERATOR= ruleCOMPARE_OPERATOR EOF ;
    public final String entryRuleCOMPARE_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleCOMPARE_OPERATOR = null;


        try {
            // InternalCQLParser.g:1301:56: (iv_ruleCOMPARE_OPERATOR= ruleCOMPARE_OPERATOR EOF )
            // InternalCQLParser.g:1302:2: iv_ruleCOMPARE_OPERATOR= ruleCOMPARE_OPERATOR EOF
            {
             newCompositeNode(grammarAccess.getCOMPARE_OPERATORRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCOMPARE_OPERATOR=ruleCOMPARE_OPERATOR();

            state._fsp--;

             current =iv_ruleCOMPARE_OPERATOR.getText(); 
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
    // $ANTLR end "entryRuleCOMPARE_OPERATOR"


    // $ANTLR start "ruleCOMPARE_OPERATOR"
    // InternalCQLParser.g:1308:1: ruleCOMPARE_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= GreaterThanSignEqualsSign | kw= LessThanSignEqualsSign | kw= LessThanSign | kw= GreaterThanSign ) ;
    public final AntlrDatatypeRuleToken ruleCOMPARE_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1314:2: ( (kw= GreaterThanSignEqualsSign | kw= LessThanSignEqualsSign | kw= LessThanSign | kw= GreaterThanSign ) )
            // InternalCQLParser.g:1315:2: (kw= GreaterThanSignEqualsSign | kw= LessThanSignEqualsSign | kw= LessThanSign | kw= GreaterThanSign )
            {
            // InternalCQLParser.g:1315:2: (kw= GreaterThanSignEqualsSign | kw= LessThanSignEqualsSign | kw= LessThanSign | kw= GreaterThanSign )
            int alt25=4;
            switch ( input.LA(1) ) {
            case GreaterThanSignEqualsSign:
                {
                alt25=1;
                }
                break;
            case LessThanSignEqualsSign:
                {
                alt25=2;
                }
                break;
            case LessThanSign:
                {
                alt25=3;
                }
                break;
            case GreaterThanSign:
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
                    // InternalCQLParser.g:1316:3: kw= GreaterThanSignEqualsSign
                    {
                    kw=(Token)match(input,GreaterThanSignEqualsSign,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getCOMPARE_OPERATORAccess().getGreaterThanSignEqualsSignKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1322:3: kw= LessThanSignEqualsSign
                    {
                    kw=(Token)match(input,LessThanSignEqualsSign,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getCOMPARE_OPERATORAccess().getLessThanSignEqualsSignKeyword_1());
                    		

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:1328:3: kw= LessThanSign
                    {
                    kw=(Token)match(input,LessThanSign,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getCOMPARE_OPERATORAccess().getLessThanSignKeyword_2());
                    		

                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:1334:3: kw= GreaterThanSign
                    {
                    kw=(Token)match(input,GreaterThanSign,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getCOMPARE_OPERATORAccess().getGreaterThanSignKeyword_3());
                    		

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
    // $ANTLR end "ruleCOMPARE_OPERATOR"


    // $ANTLR start "entryRuleARITHMETIC_OPERATOR"
    // InternalCQLParser.g:1343:1: entryRuleARITHMETIC_OPERATOR returns [String current=null] : iv_ruleARITHMETIC_OPERATOR= ruleARITHMETIC_OPERATOR EOF ;
    public final String entryRuleARITHMETIC_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleARITHMETIC_OPERATOR = null;


        try {
            // InternalCQLParser.g:1343:59: (iv_ruleARITHMETIC_OPERATOR= ruleARITHMETIC_OPERATOR EOF )
            // InternalCQLParser.g:1344:2: iv_ruleARITHMETIC_OPERATOR= ruleARITHMETIC_OPERATOR EOF
            {
             newCompositeNode(grammarAccess.getARITHMETIC_OPERATORRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleARITHMETIC_OPERATOR=ruleARITHMETIC_OPERATOR();

            state._fsp--;

             current =iv_ruleARITHMETIC_OPERATOR.getText(); 
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
    // $ANTLR end "entryRuleARITHMETIC_OPERATOR"


    // $ANTLR start "ruleARITHMETIC_OPERATOR"
    // InternalCQLParser.g:1350:1: ruleARITHMETIC_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ADD_OPERATOR_0= ruleADD_OPERATOR | this_MINUS_OPERATOR_1= ruleMINUS_OPERATOR | this_MUL_OR_DIV_OPERATOR_2= ruleMUL_OR_DIV_OPERATOR | this_EXPONENT_OPERATOR_3= ruleEXPONENT_OPERATOR ) ;
    public final AntlrDatatypeRuleToken ruleARITHMETIC_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        AntlrDatatypeRuleToken this_ADD_OPERATOR_0 = null;

        AntlrDatatypeRuleToken this_MINUS_OPERATOR_1 = null;

        AntlrDatatypeRuleToken this_MUL_OR_DIV_OPERATOR_2 = null;

        AntlrDatatypeRuleToken this_EXPONENT_OPERATOR_3 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1356:2: ( (this_ADD_OPERATOR_0= ruleADD_OPERATOR | this_MINUS_OPERATOR_1= ruleMINUS_OPERATOR | this_MUL_OR_DIV_OPERATOR_2= ruleMUL_OR_DIV_OPERATOR | this_EXPONENT_OPERATOR_3= ruleEXPONENT_OPERATOR ) )
            // InternalCQLParser.g:1357:2: (this_ADD_OPERATOR_0= ruleADD_OPERATOR | this_MINUS_OPERATOR_1= ruleMINUS_OPERATOR | this_MUL_OR_DIV_OPERATOR_2= ruleMUL_OR_DIV_OPERATOR | this_EXPONENT_OPERATOR_3= ruleEXPONENT_OPERATOR )
            {
            // InternalCQLParser.g:1357:2: (this_ADD_OPERATOR_0= ruleADD_OPERATOR | this_MINUS_OPERATOR_1= ruleMINUS_OPERATOR | this_MUL_OR_DIV_OPERATOR_2= ruleMUL_OR_DIV_OPERATOR | this_EXPONENT_OPERATOR_3= ruleEXPONENT_OPERATOR )
            int alt26=4;
            switch ( input.LA(1) ) {
            case PlusSign:
                {
                alt26=1;
                }
                break;
            case HyphenMinus:
                {
                alt26=2;
                }
                break;
            case Asterisk:
            case Solidus:
                {
                alt26=3;
                }
                break;
            case CircumflexAccent:
                {
                alt26=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;
            }

            switch (alt26) {
                case 1 :
                    // InternalCQLParser.g:1358:3: this_ADD_OPERATOR_0= ruleADD_OPERATOR
                    {

                    			newCompositeNode(grammarAccess.getARITHMETIC_OPERATORAccess().getADD_OPERATORParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_ADD_OPERATOR_0=ruleADD_OPERATOR();

                    state._fsp--;


                    			current.merge(this_ADD_OPERATOR_0);
                    		

                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1369:3: this_MINUS_OPERATOR_1= ruleMINUS_OPERATOR
                    {

                    			newCompositeNode(grammarAccess.getARITHMETIC_OPERATORAccess().getMINUS_OPERATORParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_MINUS_OPERATOR_1=ruleMINUS_OPERATOR();

                    state._fsp--;


                    			current.merge(this_MINUS_OPERATOR_1);
                    		

                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:1380:3: this_MUL_OR_DIV_OPERATOR_2= ruleMUL_OR_DIV_OPERATOR
                    {

                    			newCompositeNode(grammarAccess.getARITHMETIC_OPERATORAccess().getMUL_OR_DIV_OPERATORParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_MUL_OR_DIV_OPERATOR_2=ruleMUL_OR_DIV_OPERATOR();

                    state._fsp--;


                    			current.merge(this_MUL_OR_DIV_OPERATOR_2);
                    		

                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:1391:3: this_EXPONENT_OPERATOR_3= ruleEXPONENT_OPERATOR
                    {

                    			newCompositeNode(grammarAccess.getARITHMETIC_OPERATORAccess().getEXPONENT_OPERATORParserRuleCall_3());
                    		
                    pushFollow(FOLLOW_2);
                    this_EXPONENT_OPERATOR_3=ruleEXPONENT_OPERATOR();

                    state._fsp--;


                    			current.merge(this_EXPONENT_OPERATOR_3);
                    		

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
    // $ANTLR end "ruleARITHMETIC_OPERATOR"


    // $ANTLR start "entryRuleEXPONENT_OPERATOR"
    // InternalCQLParser.g:1405:1: entryRuleEXPONENT_OPERATOR returns [String current=null] : iv_ruleEXPONENT_OPERATOR= ruleEXPONENT_OPERATOR EOF ;
    public final String entryRuleEXPONENT_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleEXPONENT_OPERATOR = null;


        try {
            // InternalCQLParser.g:1405:57: (iv_ruleEXPONENT_OPERATOR= ruleEXPONENT_OPERATOR EOF )
            // InternalCQLParser.g:1406:2: iv_ruleEXPONENT_OPERATOR= ruleEXPONENT_OPERATOR EOF
            {
             newCompositeNode(grammarAccess.getEXPONENT_OPERATORRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEXPONENT_OPERATOR=ruleEXPONENT_OPERATOR();

            state._fsp--;

             current =iv_ruleEXPONENT_OPERATOR.getText(); 
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
    // $ANTLR end "entryRuleEXPONENT_OPERATOR"


    // $ANTLR start "ruleEXPONENT_OPERATOR"
    // InternalCQLParser.g:1412:1: ruleEXPONENT_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= CircumflexAccent ;
    public final AntlrDatatypeRuleToken ruleEXPONENT_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1418:2: (kw= CircumflexAccent )
            // InternalCQLParser.g:1419:2: kw= CircumflexAccent
            {
            kw=(Token)match(input,CircumflexAccent,FOLLOW_2); 

            		current.merge(kw);
            		newLeafNode(kw, grammarAccess.getEXPONENT_OPERATORAccess().getCircumflexAccentKeyword());
            	

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
    // $ANTLR end "ruleEXPONENT_OPERATOR"


    // $ANTLR start "entryRuleMUL_OR_DIV_OPERATOR"
    // InternalCQLParser.g:1427:1: entryRuleMUL_OR_DIV_OPERATOR returns [String current=null] : iv_ruleMUL_OR_DIV_OPERATOR= ruleMUL_OR_DIV_OPERATOR EOF ;
    public final String entryRuleMUL_OR_DIV_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleMUL_OR_DIV_OPERATOR = null;


        try {
            // InternalCQLParser.g:1427:59: (iv_ruleMUL_OR_DIV_OPERATOR= ruleMUL_OR_DIV_OPERATOR EOF )
            // InternalCQLParser.g:1428:2: iv_ruleMUL_OR_DIV_OPERATOR= ruleMUL_OR_DIV_OPERATOR EOF
            {
             newCompositeNode(grammarAccess.getMUL_OR_DIV_OPERATORRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleMUL_OR_DIV_OPERATOR=ruleMUL_OR_DIV_OPERATOR();

            state._fsp--;

             current =iv_ruleMUL_OR_DIV_OPERATOR.getText(); 
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
    // $ANTLR end "entryRuleMUL_OR_DIV_OPERATOR"


    // $ANTLR start "ruleMUL_OR_DIV_OPERATOR"
    // InternalCQLParser.g:1434:1: ruleMUL_OR_DIV_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= Asterisk | kw= Solidus ) ;
    public final AntlrDatatypeRuleToken ruleMUL_OR_DIV_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1440:2: ( (kw= Asterisk | kw= Solidus ) )
            // InternalCQLParser.g:1441:2: (kw= Asterisk | kw= Solidus )
            {
            // InternalCQLParser.g:1441:2: (kw= Asterisk | kw= Solidus )
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==Asterisk) ) {
                alt27=1;
            }
            else if ( (LA27_0==Solidus) ) {
                alt27=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }
            switch (alt27) {
                case 1 :
                    // InternalCQLParser.g:1442:3: kw= Asterisk
                    {
                    kw=(Token)match(input,Asterisk,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getMUL_OR_DIV_OPERATORAccess().getAsteriskKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1448:3: kw= Solidus
                    {
                    kw=(Token)match(input,Solidus,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getMUL_OR_DIV_OPERATORAccess().getSolidusKeyword_1());
                    		

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
    // $ANTLR end "ruleMUL_OR_DIV_OPERATOR"


    // $ANTLR start "entryRuleADD_OPERATOR"
    // InternalCQLParser.g:1457:1: entryRuleADD_OPERATOR returns [String current=null] : iv_ruleADD_OPERATOR= ruleADD_OPERATOR EOF ;
    public final String entryRuleADD_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleADD_OPERATOR = null;


        try {
            // InternalCQLParser.g:1457:52: (iv_ruleADD_OPERATOR= ruleADD_OPERATOR EOF )
            // InternalCQLParser.g:1458:2: iv_ruleADD_OPERATOR= ruleADD_OPERATOR EOF
            {
             newCompositeNode(grammarAccess.getADD_OPERATORRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleADD_OPERATOR=ruleADD_OPERATOR();

            state._fsp--;

             current =iv_ruleADD_OPERATOR.getText(); 
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
    // $ANTLR end "entryRuleADD_OPERATOR"


    // $ANTLR start "ruleADD_OPERATOR"
    // InternalCQLParser.g:1464:1: ruleADD_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= PlusSign ;
    public final AntlrDatatypeRuleToken ruleADD_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1470:2: (kw= PlusSign )
            // InternalCQLParser.g:1471:2: kw= PlusSign
            {
            kw=(Token)match(input,PlusSign,FOLLOW_2); 

            		current.merge(kw);
            		newLeafNode(kw, grammarAccess.getADD_OPERATORAccess().getPlusSignKeyword());
            	

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
    // $ANTLR end "ruleADD_OPERATOR"


    // $ANTLR start "entryRuleMINUS_OPERATOR"
    // InternalCQLParser.g:1479:1: entryRuleMINUS_OPERATOR returns [String current=null] : iv_ruleMINUS_OPERATOR= ruleMINUS_OPERATOR EOF ;
    public final String entryRuleMINUS_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleMINUS_OPERATOR = null;


        try {
            // InternalCQLParser.g:1479:54: (iv_ruleMINUS_OPERATOR= ruleMINUS_OPERATOR EOF )
            // InternalCQLParser.g:1480:2: iv_ruleMINUS_OPERATOR= ruleMINUS_OPERATOR EOF
            {
             newCompositeNode(grammarAccess.getMINUS_OPERATORRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleMINUS_OPERATOR=ruleMINUS_OPERATOR();

            state._fsp--;

             current =iv_ruleMINUS_OPERATOR.getText(); 
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
    // $ANTLR end "entryRuleMINUS_OPERATOR"


    // $ANTLR start "ruleMINUS_OPERATOR"
    // InternalCQLParser.g:1486:1: ruleMINUS_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= HyphenMinus ;
    public final AntlrDatatypeRuleToken ruleMINUS_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1492:2: (kw= HyphenMinus )
            // InternalCQLParser.g:1493:2: kw= HyphenMinus
            {
            kw=(Token)match(input,HyphenMinus,FOLLOW_2); 

            		current.merge(kw);
            		newLeafNode(kw, grammarAccess.getMINUS_OPERATORAccess().getHyphenMinusKeyword());
            	

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
    // $ANTLR end "ruleMINUS_OPERATOR"


    // $ANTLR start "entryRuleSelectExpression"
    // InternalCQLParser.g:1501:1: entryRuleSelectExpression returns [EObject current=null] : iv_ruleSelectExpression= ruleSelectExpression EOF ;
    public final EObject entryRuleSelectExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpression = null;


        try {
            // InternalCQLParser.g:1501:57: (iv_ruleSelectExpression= ruleSelectExpression EOF )
            // InternalCQLParser.g:1502:2: iv_ruleSelectExpression= ruleSelectExpression EOF
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
    // InternalCQLParser.g:1508:1: ruleSelectExpression returns [EObject current=null] : ( ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) ;
    public final EObject ruleSelectExpression() throws RecognitionException {
        EObject current = null;

        Token otherlv_6=null;
        EObject lv_expressions_0_0 = null;

        AntlrDatatypeRuleToken lv_operators_1_0 = null;

        EObject lv_expressions_2_1 = null;

        EObject lv_expressions_2_2 = null;

        EObject lv_expressions_3_0 = null;

        AntlrDatatypeRuleToken lv_operators_4_0 = null;

        EObject lv_expressions_5_1 = null;

        EObject lv_expressions_5_2 = null;

        EObject lv_alias_7_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1514:2: ( ( ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:1515:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:1515:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:1516:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:1516:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) )+ ) )
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==RULE_ID) ) {
                int LA32_1 = input.LA(2);

                if ( ((LA32_1>=Asterisk && LA32_1<=PlusSign)||(LA32_1>=HyphenMinus && LA32_1<=Solidus)||LA32_1==CircumflexAccent) ) {
                    alt32=2;
                }
                else if ( (LA32_1==LeftParenthesis) ) {
                    alt32=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA32_0==FALSE||LA32_0==TRUE||(LA32_0>=RULE_INT && LA32_0<=RULE_STRING)) ) {
                alt32=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }
            switch (alt32) {
                case 1 :
                    // InternalCQLParser.g:1517:4: ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* )
                    {
                    // InternalCQLParser.g:1517:4: ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* )
                    // InternalCQLParser.g:1518:5: ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )*
                    {
                    // InternalCQLParser.g:1518:5: ( (lv_expressions_0_0= ruleExpressionComponent ) )
                    // InternalCQLParser.g:1519:6: (lv_expressions_0_0= ruleExpressionComponent )
                    {
                    // InternalCQLParser.g:1519:6: (lv_expressions_0_0= ruleExpressionComponent )
                    // InternalCQLParser.g:1520:7: lv_expressions_0_0= ruleExpressionComponent
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentParserRuleCall_0_0_0_0());
                    						
                    pushFollow(FOLLOW_25);
                    lv_expressions_0_0=ruleExpressionComponent();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    							}
                    							add(
                    								current,
                    								"expressions",
                    								lv_expressions_0_0,
                    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponent");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCQLParser.g:1537:5: ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )*
                    loop29:
                    do {
                        int alt29=2;
                        int LA29_0 = input.LA(1);

                        if ( ((LA29_0>=Asterisk && LA29_0<=PlusSign)||LA29_0==HyphenMinus||LA29_0==Solidus||LA29_0==CircumflexAccent) ) {
                            alt29=1;
                        }


                        switch (alt29) {
                    	case 1 :
                    	    // InternalCQLParser.g:1538:6: ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) )
                    	    {
                    	    // InternalCQLParser.g:1538:6: ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) )
                    	    // InternalCQLParser.g:1539:7: (lv_operators_1_0= ruleARITHMETIC_OPERATOR )
                    	    {
                    	    // InternalCQLParser.g:1539:7: (lv_operators_1_0= ruleARITHMETIC_OPERATOR )
                    	    // InternalCQLParser.g:1540:8: lv_operators_1_0= ruleARITHMETIC_OPERATOR
                    	    {

                    	    								newCompositeNode(grammarAccess.getSelectExpressionAccess().getOperatorsARITHMETIC_OPERATORParserRuleCall_0_0_1_0_0());
                    	    							
                    	    pushFollow(FOLLOW_4);
                    	    lv_operators_1_0=ruleARITHMETIC_OPERATOR();

                    	    state._fsp--;


                    	    								if (current==null) {
                    	    									current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	    								}
                    	    								add(
                    	    									current,
                    	    									"operators",
                    	    									lv_operators_1_0,
                    	    									"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ARITHMETIC_OPERATOR");
                    	    								afterParserOrEnumRuleCall();
                    	    							

                    	    }


                    	    }

                    	    // InternalCQLParser.g:1557:6: ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) )
                    	    // InternalCQLParser.g:1558:7: ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) )
                    	    {
                    	    // InternalCQLParser.g:1558:7: ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) )
                    	    // InternalCQLParser.g:1559:8: (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute )
                    	    {
                    	    // InternalCQLParser.g:1559:8: (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute )
                    	    int alt28=2;
                    	    int LA28_0 = input.LA(1);

                    	    if ( (LA28_0==RULE_ID) ) {
                    	        int LA28_1 = input.LA(2);

                    	        if ( (LA28_1==LeftParenthesis) ) {
                    	            alt28=1;
                    	        }
                    	        else if ( (LA28_1==EOF||LA28_1==FALSE||LA28_1==FROM||LA28_1==TRUE||LA28_1==AS||(LA28_1>=RightParenthesis && LA28_1<=Solidus)||(LA28_1>=CircumflexAccent && LA28_1<=RULE_STRING)) ) {
                    	            alt28=2;
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
                    	            // InternalCQLParser.g:1560:9: lv_expressions_2_1= ruleExpressionComponent
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentParserRuleCall_0_0_1_1_0_0());
                    	            								
                    	            pushFollow(FOLLOW_25);
                    	            lv_expressions_2_1=ruleExpressionComponent();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_2_1,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponent");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1576:9: lv_expressions_2_2= ruleExpressionComponentAsAttribute
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentAsAttributeParserRuleCall_0_0_1_1_0_1());
                    	            								
                    	            pushFollow(FOLLOW_25);
                    	            lv_expressions_2_2=ruleExpressionComponentAsAttribute();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_2_2,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentAsAttribute");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;

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


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1597:4: ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) )+ )
                    {
                    // InternalCQLParser.g:1597:4: ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) )+ )
                    // InternalCQLParser.g:1598:5: ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) )+
                    {
                    // InternalCQLParser.g:1598:5: ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) )
                    // InternalCQLParser.g:1599:6: (lv_expressions_3_0= ruleExpressionComponentAsAttribute )
                    {
                    // InternalCQLParser.g:1599:6: (lv_expressions_3_0= ruleExpressionComponentAsAttribute )
                    // InternalCQLParser.g:1600:7: lv_expressions_3_0= ruleExpressionComponentAsAttribute
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentAsAttributeParserRuleCall_0_1_0_0());
                    						
                    pushFollow(FOLLOW_26);
                    lv_expressions_3_0=ruleExpressionComponentAsAttribute();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    							}
                    							add(
                    								current,
                    								"expressions",
                    								lv_expressions_3_0,
                    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentAsAttribute");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCQLParser.g:1617:5: ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) )+
                    int cnt31=0;
                    loop31:
                    do {
                        int alt31=2;
                        int LA31_0 = input.LA(1);

                        if ( ((LA31_0>=Asterisk && LA31_0<=PlusSign)||LA31_0==HyphenMinus||LA31_0==Solidus||LA31_0==CircumflexAccent) ) {
                            alt31=1;
                        }


                        switch (alt31) {
                    	case 1 :
                    	    // InternalCQLParser.g:1618:6: ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) )
                    	    {
                    	    // InternalCQLParser.g:1618:6: ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) )
                    	    // InternalCQLParser.g:1619:7: (lv_operators_4_0= ruleARITHMETIC_OPERATOR )
                    	    {
                    	    // InternalCQLParser.g:1619:7: (lv_operators_4_0= ruleARITHMETIC_OPERATOR )
                    	    // InternalCQLParser.g:1620:8: lv_operators_4_0= ruleARITHMETIC_OPERATOR
                    	    {

                    	    								newCompositeNode(grammarAccess.getSelectExpressionAccess().getOperatorsARITHMETIC_OPERATORParserRuleCall_0_1_1_0_0());
                    	    							
                    	    pushFollow(FOLLOW_4);
                    	    lv_operators_4_0=ruleARITHMETIC_OPERATOR();

                    	    state._fsp--;


                    	    								if (current==null) {
                    	    									current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	    								}
                    	    								add(
                    	    									current,
                    	    									"operators",
                    	    									lv_operators_4_0,
                    	    									"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ARITHMETIC_OPERATOR");
                    	    								afterParserOrEnumRuleCall();
                    	    							

                    	    }


                    	    }

                    	    // InternalCQLParser.g:1637:6: ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) )
                    	    // InternalCQLParser.g:1638:7: ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) )
                    	    {
                    	    // InternalCQLParser.g:1638:7: ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) )
                    	    // InternalCQLParser.g:1639:8: (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute )
                    	    {
                    	    // InternalCQLParser.g:1639:8: (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute )
                    	    int alt30=2;
                    	    int LA30_0 = input.LA(1);

                    	    if ( (LA30_0==RULE_ID) ) {
                    	        int LA30_1 = input.LA(2);

                    	        if ( (LA30_1==EOF||LA30_1==FALSE||LA30_1==FROM||LA30_1==TRUE||LA30_1==AS||(LA30_1>=RightParenthesis && LA30_1<=Solidus)||(LA30_1>=CircumflexAccent && LA30_1<=RULE_STRING)) ) {
                    	            alt30=2;
                    	        }
                    	        else if ( (LA30_1==LeftParenthesis) ) {
                    	            alt30=1;
                    	        }
                    	        else {
                    	            NoViableAltException nvae =
                    	                new NoViableAltException("", 30, 1, input);

                    	            throw nvae;
                    	        }
                    	    }
                    	    else if ( (LA30_0==FALSE||LA30_0==TRUE||(LA30_0>=RULE_INT && LA30_0<=RULE_STRING)) ) {
                    	        alt30=1;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 30, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt30) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1640:9: lv_expressions_5_1= ruleExpressionComponent
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentParserRuleCall_0_1_1_1_0_0());
                    	            								
                    	            pushFollow(FOLLOW_25);
                    	            lv_expressions_5_1=ruleExpressionComponent();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_5_1,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponent");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1656:9: lv_expressions_5_2= ruleExpressionComponentAsAttribute
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentAsAttributeParserRuleCall_0_1_1_1_0_1());
                    	            								
                    	            pushFollow(FOLLOW_25);
                    	            lv_expressions_5_2=ruleExpressionComponentAsAttribute();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_5_2,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentAsAttribute");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;

                    	    }


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


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:1677:3: (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==AS) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // InternalCQLParser.g:1678:4: otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) )
                    {
                    otherlv_6=(Token)match(input,AS,FOLLOW_14); 

                    				newLeafNode(otherlv_6, grammarAccess.getSelectExpressionAccess().getASKeyword_1_0());
                    			
                    // InternalCQLParser.g:1682:4: ( (lv_alias_7_0= ruleAlias ) )
                    // InternalCQLParser.g:1683:5: (lv_alias_7_0= ruleAlias )
                    {
                    // InternalCQLParser.g:1683:5: (lv_alias_7_0= ruleAlias )
                    // InternalCQLParser.g:1684:6: lv_alias_7_0= ruleAlias
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


    // $ANTLR start "entryRuleSelectExpressionOnlyWithAttribute"
    // InternalCQLParser.g:1706:1: entryRuleSelectExpressionOnlyWithAttribute returns [EObject current=null] : iv_ruleSelectExpressionOnlyWithAttribute= ruleSelectExpressionOnlyWithAttribute EOF ;
    public final EObject entryRuleSelectExpressionOnlyWithAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionOnlyWithAttribute = null;


        try {
            // InternalCQLParser.g:1706:74: (iv_ruleSelectExpressionOnlyWithAttribute= ruleSelectExpressionOnlyWithAttribute EOF )
            // InternalCQLParser.g:1707:2: iv_ruleSelectExpressionOnlyWithAttribute= ruleSelectExpressionOnlyWithAttribute EOF
            {
             newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttributeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelectExpressionOnlyWithAttribute=ruleSelectExpressionOnlyWithAttribute();

            state._fsp--;

             current =iv_ruleSelectExpressionOnlyWithAttribute; 
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
    // $ANTLR end "entryRuleSelectExpressionOnlyWithAttribute"


    // $ANTLR start "ruleSelectExpressionOnlyWithAttribute"
    // InternalCQLParser.g:1713:1: ruleSelectExpressionOnlyWithAttribute returns [EObject current=null] : ( (lv_expressions_0_0= ruleExpressionComponentAsAttribute ) ) ;
    public final EObject ruleSelectExpressionOnlyWithAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_expressions_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1719:2: ( ( (lv_expressions_0_0= ruleExpressionComponentAsAttribute ) ) )
            // InternalCQLParser.g:1720:2: ( (lv_expressions_0_0= ruleExpressionComponentAsAttribute ) )
            {
            // InternalCQLParser.g:1720:2: ( (lv_expressions_0_0= ruleExpressionComponentAsAttribute ) )
            // InternalCQLParser.g:1721:3: (lv_expressions_0_0= ruleExpressionComponentAsAttribute )
            {
            // InternalCQLParser.g:1721:3: (lv_expressions_0_0= ruleExpressionComponentAsAttribute )
            // InternalCQLParser.g:1722:4: lv_expressions_0_0= ruleExpressionComponentAsAttribute
            {

            				newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttributeAccess().getExpressionsExpressionComponentAsAttributeParserRuleCall_0());
            			
            pushFollow(FOLLOW_2);
            lv_expressions_0_0=ruleExpressionComponentAsAttribute();

            state._fsp--;


            				if (current==null) {
            					current = createModelElementForParent(grammarAccess.getSelectExpressionOnlyWithAttributeRule());
            				}
            				add(
            					current,
            					"expressions",
            					lv_expressions_0_0,
            					"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentAsAttribute");
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
    // $ANTLR end "ruleSelectExpressionOnlyWithAttribute"


    // $ANTLR start "entryRuleFunction"
    // InternalCQLParser.g:1742:1: entryRuleFunction returns [EObject current=null] : iv_ruleFunction= ruleFunction EOF ;
    public final EObject entryRuleFunction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFunction = null;


        try {
            // InternalCQLParser.g:1742:49: (iv_ruleFunction= ruleFunction EOF )
            // InternalCQLParser.g:1743:2: iv_ruleFunction= ruleFunction EOF
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
    // InternalCQLParser.g:1749:1: ruleFunction returns [EObject current=null] : ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute ) ) ) otherlv_4= RightParenthesis ) ;
    public final EObject ruleFunction() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_value_3_1 = null;

        EObject lv_value_3_2 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1755:2: ( ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute ) ) ) otherlv_4= RightParenthesis ) )
            // InternalCQLParser.g:1756:2: ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute ) ) ) otherlv_4= RightParenthesis )
            {
            // InternalCQLParser.g:1756:2: ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute ) ) ) otherlv_4= RightParenthesis )
            // InternalCQLParser.g:1757:3: () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute ) ) ) otherlv_4= RightParenthesis
            {
            // InternalCQLParser.g:1757:3: ()
            // InternalCQLParser.g:1758:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getFunctionAccess().getFunctionAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:1764:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQLParser.g:1765:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQLParser.g:1765:4: (lv_name_1_0= RULE_ID )
            // InternalCQLParser.g:1766:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_27); 

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

            otherlv_2=(Token)match(input,LeftParenthesis,FOLLOW_4); 

            			newLeafNode(otherlv_2, grammarAccess.getFunctionAccess().getLeftParenthesisKeyword_2());
            		
            // InternalCQLParser.g:1786:3: ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute ) ) )
            // InternalCQLParser.g:1787:4: ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute ) )
            {
            // InternalCQLParser.g:1787:4: ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute ) )
            // InternalCQLParser.g:1788:5: (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute )
            {
            // InternalCQLParser.g:1788:5: (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute )
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==RULE_ID) ) {
                switch ( input.LA(2) ) {
                case RightParenthesis:
                    {
                    alt34=2;
                    }
                    break;
                case LeftParenthesis:
                case Asterisk:
                case PlusSign:
                case HyphenMinus:
                case Solidus:
                case CircumflexAccent:
                    {
                    alt34=1;
                    }
                    break;
                case FullStop:
                    {
                    int LA34_4 = input.LA(3);

                    if ( (LA34_4==RULE_ID) ) {
                        int LA34_5 = input.LA(4);

                        if ( (LA34_5==RightParenthesis) ) {
                            alt34=2;
                        }
                        else if ( ((LA34_5>=Asterisk && LA34_5<=PlusSign)||LA34_5==HyphenMinus||LA34_5==Solidus||LA34_5==CircumflexAccent) ) {
                            alt34=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 34, 5, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA34_4==Asterisk) ) {
                        int LA34_6 = input.LA(4);

                        if ( (LA34_6==RightParenthesis) ) {
                            alt34=2;
                        }
                        else if ( ((LA34_6>=Asterisk && LA34_6<=PlusSign)||LA34_6==HyphenMinus||LA34_6==Solidus||LA34_6==CircumflexAccent) ) {
                            alt34=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 34, 6, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 34, 4, input);

                        throw nvae;
                    }
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 34, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA34_0==FALSE||LA34_0==TRUE||(LA34_0>=RULE_INT && LA34_0<=RULE_STRING)) ) {
                alt34=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 34, 0, input);

                throw nvae;
            }
            switch (alt34) {
                case 1 :
                    // InternalCQLParser.g:1789:6: lv_value_3_1= ruleSelectExpression
                    {

                    						newCompositeNode(grammarAccess.getFunctionAccess().getValueSelectExpressionParserRuleCall_3_0_0());
                    					
                    pushFollow(FOLLOW_18);
                    lv_value_3_1=ruleSelectExpression();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getFunctionRule());
                    						}
                    						set(
                    							current,
                    							"value",
                    							lv_value_3_1,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpression");
                    						afterParserOrEnumRuleCall();
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1805:6: lv_value_3_2= ruleSelectExpressionOnlyWithAttribute
                    {

                    						newCompositeNode(grammarAccess.getFunctionAccess().getValueSelectExpressionOnlyWithAttributeParserRuleCall_3_0_1());
                    					
                    pushFollow(FOLLOW_18);
                    lv_value_3_2=ruleSelectExpressionOnlyWithAttribute();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getFunctionRule());
                    						}
                    						set(
                    							current,
                    							"value",
                    							lv_value_3_2,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpressionOnlyWithAttribute");
                    						afterParserOrEnumRuleCall();
                    					

                    }
                    break;

            }


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


    // $ANTLR start "entryRuleExpressionComponent"
    // InternalCQLParser.g:1831:1: entryRuleExpressionComponent returns [EObject current=null] : iv_ruleExpressionComponent= ruleExpressionComponent EOF ;
    public final EObject entryRuleExpressionComponent() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponent = null;


        try {
            // InternalCQLParser.g:1831:60: (iv_ruleExpressionComponent= ruleExpressionComponent EOF )
            // InternalCQLParser.g:1832:2: iv_ruleExpressionComponent= ruleExpressionComponent EOF
            {
             newCompositeNode(grammarAccess.getExpressionComponentRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpressionComponent=ruleExpressionComponent();

            state._fsp--;

             current =iv_ruleExpressionComponent; 
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
    // $ANTLR end "entryRuleExpressionComponent"


    // $ANTLR start "ruleExpressionComponent"
    // InternalCQLParser.g:1838:1: ruleExpressionComponent returns [EObject current=null] : ( ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) ) ) ;
    public final EObject ruleExpressionComponent() throws RecognitionException {
        EObject current = null;

        EObject lv_value_0_1 = null;

        EObject lv_value_0_2 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1844:2: ( ( ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) ) ) )
            // InternalCQLParser.g:1845:2: ( ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) ) )
            {
            // InternalCQLParser.g:1845:2: ( ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) ) )
            // InternalCQLParser.g:1846:3: ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) )
            {
            // InternalCQLParser.g:1846:3: ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) )
            // InternalCQLParser.g:1847:4: (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef )
            {
            // InternalCQLParser.g:1847:4: (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef )
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==RULE_ID) ) {
                alt35=1;
            }
            else if ( (LA35_0==FALSE||LA35_0==TRUE||(LA35_0>=RULE_INT && LA35_0<=RULE_STRING)) ) {
                alt35=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 35, 0, input);

                throw nvae;
            }
            switch (alt35) {
                case 1 :
                    // InternalCQLParser.g:1848:5: lv_value_0_1= ruleFunction
                    {

                    					newCompositeNode(grammarAccess.getExpressionComponentAccess().getValueFunctionParserRuleCall_0_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_value_0_1=ruleFunction();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getExpressionComponentRule());
                    					}
                    					set(
                    						current,
                    						"value",
                    						lv_value_0_1,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Function");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1864:5: lv_value_0_2= ruleAtomicWithoutAttributeRef
                    {

                    					newCompositeNode(grammarAccess.getExpressionComponentAccess().getValueAtomicWithoutAttributeRefParserRuleCall_0_1());
                    				
                    pushFollow(FOLLOW_2);
                    lv_value_0_2=ruleAtomicWithoutAttributeRef();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getExpressionComponentRule());
                    					}
                    					set(
                    						current,
                    						"value",
                    						lv_value_0_2,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AtomicWithoutAttributeRef");
                    					afterParserOrEnumRuleCall();
                    				

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
    // $ANTLR end "ruleExpressionComponent"


    // $ANTLR start "entryRuleExpressionComponentAsAttribute"
    // InternalCQLParser.g:1885:1: entryRuleExpressionComponentAsAttribute returns [EObject current=null] : iv_ruleExpressionComponentAsAttribute= ruleExpressionComponentAsAttribute EOF ;
    public final EObject entryRuleExpressionComponentAsAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentAsAttribute = null;


        try {
            // InternalCQLParser.g:1885:71: (iv_ruleExpressionComponentAsAttribute= ruleExpressionComponentAsAttribute EOF )
            // InternalCQLParser.g:1886:2: iv_ruleExpressionComponentAsAttribute= ruleExpressionComponentAsAttribute EOF
            {
             newCompositeNode(grammarAccess.getExpressionComponentAsAttributeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpressionComponentAsAttribute=ruleExpressionComponentAsAttribute();

            state._fsp--;

             current =iv_ruleExpressionComponentAsAttribute; 
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
    // $ANTLR end "entryRuleExpressionComponentAsAttribute"


    // $ANTLR start "ruleExpressionComponentAsAttribute"
    // InternalCQLParser.g:1892:1: ruleExpressionComponentAsAttribute returns [EObject current=null] : ( () ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) ) ;
    public final EObject ruleExpressionComponentAsAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_value_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1898:2: ( ( () ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) ) )
            // InternalCQLParser.g:1899:2: ( () ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) )
            {
            // InternalCQLParser.g:1899:2: ( () ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) )
            // InternalCQLParser.g:1900:3: () ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) )
            {
            // InternalCQLParser.g:1900:3: ()
            // InternalCQLParser.g:1901:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionComponentAsAttributeAccess().getExpressionComponentAsAttributeAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:1907:3: ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQLParser.g:1908:4: (lv_value_1_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1908:4: (lv_value_1_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQLParser.g:1909:5: lv_value_1_0= ruleAttributeWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getExpressionComponentAsAttributeAccess().getValueAttributeWithoutAliasDefinitionParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_value_1_0=ruleAttributeWithoutAliasDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getExpressionComponentAsAttributeRule());
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
    // $ANTLR end "ruleExpressionComponentAsAttribute"


    // $ANTLR start "entryRuleAlias"
    // InternalCQLParser.g:1930:1: entryRuleAlias returns [EObject current=null] : iv_ruleAlias= ruleAlias EOF ;
    public final EObject entryRuleAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAlias = null;


        try {
            // InternalCQLParser.g:1930:46: (iv_ruleAlias= ruleAlias EOF )
            // InternalCQLParser.g:1931:2: iv_ruleAlias= ruleAlias EOF
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
    // InternalCQLParser.g:1937:1: ruleAlias returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1943:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQLParser.g:1944:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQLParser.g:1944:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:1945:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:1945:3: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:1946:4: lv_name_0_0= RULE_ID
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


    // $ANTLR start "entryRuleAccessFramework"
    // InternalCQLParser.g:1965:1: entryRuleAccessFramework returns [EObject current=null] : iv_ruleAccessFramework= ruleAccessFramework EOF ;
    public final EObject entryRuleAccessFramework() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAccessFramework = null;


        try {
            // InternalCQLParser.g:1965:56: (iv_ruleAccessFramework= ruleAccessFramework EOF )
            // InternalCQLParser.g:1966:2: iv_ruleAccessFramework= ruleAccessFramework EOF
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
    // InternalCQLParser.g:1972:1: ruleAccessFramework returns [EObject current=null] : (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis ) ;
    public final EObject ruleAccessFramework() throws RecognitionException {
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
            // InternalCQLParser.g:1978:2: ( (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis ) )
            // InternalCQLParser.g:1979:2: (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis )
            {
            // InternalCQLParser.g:1979:2: (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis )
            // InternalCQLParser.g:1980:3: otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis
            {
            otherlv_0=(Token)match(input,WRAPPER,FOLLOW_28); 

            			newLeafNode(otherlv_0, grammarAccess.getAccessFrameworkAccess().getWRAPPERKeyword_0());
            		
            // InternalCQLParser.g:1984:3: ( (lv_wrapper_1_0= RULE_STRING ) )
            // InternalCQLParser.g:1985:4: (lv_wrapper_1_0= RULE_STRING )
            {
            // InternalCQLParser.g:1985:4: (lv_wrapper_1_0= RULE_STRING )
            // InternalCQLParser.g:1986:5: lv_wrapper_1_0= RULE_STRING
            {
            lv_wrapper_1_0=(Token)match(input,RULE_STRING,FOLLOW_29); 

            					newLeafNode(lv_wrapper_1_0, grammarAccess.getAccessFrameworkAccess().getWrapperSTRINGTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAccessFrameworkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"wrapper",
            						lv_wrapper_1_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_2=(Token)match(input,PROTOCOL,FOLLOW_28); 

            			newLeafNode(otherlv_2, grammarAccess.getAccessFrameworkAccess().getPROTOCOLKeyword_2());
            		
            // InternalCQLParser.g:2006:3: ( (lv_protocol_3_0= RULE_STRING ) )
            // InternalCQLParser.g:2007:4: (lv_protocol_3_0= RULE_STRING )
            {
            // InternalCQLParser.g:2007:4: (lv_protocol_3_0= RULE_STRING )
            // InternalCQLParser.g:2008:5: lv_protocol_3_0= RULE_STRING
            {
            lv_protocol_3_0=(Token)match(input,RULE_STRING,FOLLOW_30); 

            					newLeafNode(lv_protocol_3_0, grammarAccess.getAccessFrameworkAccess().getProtocolSTRINGTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAccessFrameworkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"protocol",
            						lv_protocol_3_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_4=(Token)match(input,TRANSPORT,FOLLOW_28); 

            			newLeafNode(otherlv_4, grammarAccess.getAccessFrameworkAccess().getTRANSPORTKeyword_4());
            		
            // InternalCQLParser.g:2028:3: ( (lv_transport_5_0= RULE_STRING ) )
            // InternalCQLParser.g:2029:4: (lv_transport_5_0= RULE_STRING )
            {
            // InternalCQLParser.g:2029:4: (lv_transport_5_0= RULE_STRING )
            // InternalCQLParser.g:2030:5: lv_transport_5_0= RULE_STRING
            {
            lv_transport_5_0=(Token)match(input,RULE_STRING,FOLLOW_31); 

            					newLeafNode(lv_transport_5_0, grammarAccess.getAccessFrameworkAccess().getTransportSTRINGTerminalRuleCall_5_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAccessFrameworkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"transport",
            						lv_transport_5_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_6=(Token)match(input,DATAHANDLER,FOLLOW_28); 

            			newLeafNode(otherlv_6, grammarAccess.getAccessFrameworkAccess().getDATAHANDLERKeyword_6());
            		
            // InternalCQLParser.g:2050:3: ( (lv_datahandler_7_0= RULE_STRING ) )
            // InternalCQLParser.g:2051:4: (lv_datahandler_7_0= RULE_STRING )
            {
            // InternalCQLParser.g:2051:4: (lv_datahandler_7_0= RULE_STRING )
            // InternalCQLParser.g:2052:5: lv_datahandler_7_0= RULE_STRING
            {
            lv_datahandler_7_0=(Token)match(input,RULE_STRING,FOLLOW_32); 

            					newLeafNode(lv_datahandler_7_0, grammarAccess.getAccessFrameworkAccess().getDatahandlerSTRINGTerminalRuleCall_7_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAccessFrameworkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"datahandler",
            						lv_datahandler_7_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_8=(Token)match(input,OPTIONS,FOLLOW_27); 

            			newLeafNode(otherlv_8, grammarAccess.getAccessFrameworkAccess().getOPTIONSKeyword_8());
            		
            otherlv_9=(Token)match(input,LeftParenthesis,FOLLOW_28); 

            			newLeafNode(otherlv_9, grammarAccess.getAccessFrameworkAccess().getLeftParenthesisKeyword_9());
            		
            // InternalCQLParser.g:2076:3: ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+
            int cnt36=0;
            loop36:
            do {
                int alt36=2;
                int LA36_0 = input.LA(1);

                if ( (LA36_0==RULE_STRING) ) {
                    alt36=1;
                }


                switch (alt36) {
            	case 1 :
            	    // InternalCQLParser.g:2077:4: ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) )
            	    {
            	    // InternalCQLParser.g:2077:4: ( (lv_keys_10_0= RULE_STRING ) )
            	    // InternalCQLParser.g:2078:5: (lv_keys_10_0= RULE_STRING )
            	    {
            	    // InternalCQLParser.g:2078:5: (lv_keys_10_0= RULE_STRING )
            	    // InternalCQLParser.g:2079:6: lv_keys_10_0= RULE_STRING
            	    {
            	    lv_keys_10_0=(Token)match(input,RULE_STRING,FOLLOW_28); 

            	    						newLeafNode(lv_keys_10_0, grammarAccess.getAccessFrameworkAccess().getKeysSTRINGTerminalRuleCall_10_0_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"keys",
            	    							lv_keys_10_0,
            	    							"org.eclipse.xtext.common.Terminals.STRING");
            	    					

            	    }


            	    }

            	    // InternalCQLParser.g:2095:4: ( (lv_values_11_0= RULE_STRING ) )
            	    // InternalCQLParser.g:2096:5: (lv_values_11_0= RULE_STRING )
            	    {
            	    // InternalCQLParser.g:2096:5: (lv_values_11_0= RULE_STRING )
            	    // InternalCQLParser.g:2097:6: lv_values_11_0= RULE_STRING
            	    {
            	    lv_values_11_0=(Token)match(input,RULE_STRING,FOLLOW_33); 

            	    						newLeafNode(lv_values_11_0, grammarAccess.getAccessFrameworkAccess().getValuesSTRINGTerminalRuleCall_10_1_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
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
            	    if ( cnt36 >= 1 ) break loop36;
                        EarlyExitException eee =
                            new EarlyExitException(36, input);
                        throw eee;
                }
                cnt36++;
            } while (true);

            // InternalCQLParser.g:2114:3: (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==Comma) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // InternalCQLParser.g:2115:4: otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) )
                    {
                    otherlv_12=(Token)match(input,Comma,FOLLOW_28); 

                    				newLeafNode(otherlv_12, grammarAccess.getAccessFrameworkAccess().getCommaKeyword_11_0());
                    			
                    // InternalCQLParser.g:2119:4: ( (lv_keys_13_0= RULE_STRING ) )
                    // InternalCQLParser.g:2120:5: (lv_keys_13_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:2120:5: (lv_keys_13_0= RULE_STRING )
                    // InternalCQLParser.g:2121:6: lv_keys_13_0= RULE_STRING
                    {
                    lv_keys_13_0=(Token)match(input,RULE_STRING,FOLLOW_28); 

                    						newLeafNode(lv_keys_13_0, grammarAccess.getAccessFrameworkAccess().getKeysSTRINGTerminalRuleCall_11_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"keys",
                    							lv_keys_13_0,
                    							"org.eclipse.xtext.common.Terminals.STRING");
                    					

                    }


                    }

                    // InternalCQLParser.g:2137:4: ( (lv_values_14_0= RULE_STRING ) )
                    // InternalCQLParser.g:2138:5: (lv_values_14_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:2138:5: (lv_values_14_0= RULE_STRING )
                    // InternalCQLParser.g:2139:6: lv_values_14_0= RULE_STRING
                    {
                    lv_values_14_0=(Token)match(input,RULE_STRING,FOLLOW_18); 

                    						newLeafNode(lv_values_14_0, grammarAccess.getAccessFrameworkAccess().getValuesSTRINGTerminalRuleCall_11_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
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

            			newLeafNode(otherlv_15, grammarAccess.getAccessFrameworkAccess().getRightParenthesisKeyword_12());
            		

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


    // $ANTLR start "entryRuleSchemaDefinition"
    // InternalCQLParser.g:2164:1: entryRuleSchemaDefinition returns [EObject current=null] : iv_ruleSchemaDefinition= ruleSchemaDefinition EOF ;
    public final EObject entryRuleSchemaDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSchemaDefinition = null;


        try {
            // InternalCQLParser.g:2164:57: (iv_ruleSchemaDefinition= ruleSchemaDefinition EOF )
            // InternalCQLParser.g:2165:2: iv_ruleSchemaDefinition= ruleSchemaDefinition EOF
            {
             newCompositeNode(grammarAccess.getSchemaDefinitionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSchemaDefinition=ruleSchemaDefinition();

            state._fsp--;

             current =iv_ruleSchemaDefinition; 
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
    // $ANTLR end "entryRuleSchemaDefinition"


    // $ANTLR start "ruleSchemaDefinition"
    // InternalCQLParser.g:2171:1: ruleSchemaDefinition returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis ) ;
    public final EObject ruleSchemaDefinition() throws RecognitionException {
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
            // InternalCQLParser.g:2177:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis ) )
            // InternalCQLParser.g:2178:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis )
            {
            // InternalCQLParser.g:2178:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis )
            // InternalCQLParser.g:2179:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis
            {
            // InternalCQLParser.g:2179:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:2180:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:2180:4: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:2181:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_27); 

            					newLeafNode(lv_name_0_0, grammarAccess.getSchemaDefinitionAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSchemaDefinitionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,LeftParenthesis,FOLLOW_14); 

            			newLeafNode(otherlv_1, grammarAccess.getSchemaDefinitionAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQLParser.g:2201:3: ( (lv_arguments_2_0= RULE_ID ) )
            // InternalCQLParser.g:2202:4: (lv_arguments_2_0= RULE_ID )
            {
            // InternalCQLParser.g:2202:4: (lv_arguments_2_0= RULE_ID )
            // InternalCQLParser.g:2203:5: lv_arguments_2_0= RULE_ID
            {
            lv_arguments_2_0=(Token)match(input,RULE_ID,FOLLOW_14); 

            					newLeafNode(lv_arguments_2_0, grammarAccess.getSchemaDefinitionAccess().getArgumentsIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSchemaDefinitionRule());
            					}
            					addWithLastConsumed(
            						current,
            						"arguments",
            						lv_arguments_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:2219:3: ( (lv_arguments_3_0= RULE_ID ) )
            // InternalCQLParser.g:2220:4: (lv_arguments_3_0= RULE_ID )
            {
            // InternalCQLParser.g:2220:4: (lv_arguments_3_0= RULE_ID )
            // InternalCQLParser.g:2221:5: lv_arguments_3_0= RULE_ID
            {
            lv_arguments_3_0=(Token)match(input,RULE_ID,FOLLOW_34); 

            					newLeafNode(lv_arguments_3_0, grammarAccess.getSchemaDefinitionAccess().getArgumentsIDTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSchemaDefinitionRule());
            					}
            					addWithLastConsumed(
            						current,
            						"arguments",
            						lv_arguments_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:2237:3: (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )*
            loop38:
            do {
                int alt38=2;
                int LA38_0 = input.LA(1);

                if ( (LA38_0==Comma) ) {
                    alt38=1;
                }


                switch (alt38) {
            	case 1 :
            	    // InternalCQLParser.g:2238:4: otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) )
            	    {
            	    otherlv_4=(Token)match(input,Comma,FOLLOW_14); 

            	    				newLeafNode(otherlv_4, grammarAccess.getSchemaDefinitionAccess().getCommaKeyword_4_0());
            	    			
            	    // InternalCQLParser.g:2242:4: ( (lv_arguments_5_0= RULE_ID ) )
            	    // InternalCQLParser.g:2243:5: (lv_arguments_5_0= RULE_ID )
            	    {
            	    // InternalCQLParser.g:2243:5: (lv_arguments_5_0= RULE_ID )
            	    // InternalCQLParser.g:2244:6: lv_arguments_5_0= RULE_ID
            	    {
            	    lv_arguments_5_0=(Token)match(input,RULE_ID,FOLLOW_14); 

            	    						newLeafNode(lv_arguments_5_0, grammarAccess.getSchemaDefinitionAccess().getArgumentsIDTerminalRuleCall_4_1_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getSchemaDefinitionRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"arguments",
            	    							lv_arguments_5_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            	    					

            	    }


            	    }

            	    // InternalCQLParser.g:2260:4: ( (lv_arguments_6_0= RULE_ID ) )
            	    // InternalCQLParser.g:2261:5: (lv_arguments_6_0= RULE_ID )
            	    {
            	    // InternalCQLParser.g:2261:5: (lv_arguments_6_0= RULE_ID )
            	    // InternalCQLParser.g:2262:6: lv_arguments_6_0= RULE_ID
            	    {
            	    lv_arguments_6_0=(Token)match(input,RULE_ID,FOLLOW_34); 

            	    						newLeafNode(lv_arguments_6_0, grammarAccess.getSchemaDefinitionAccess().getArgumentsIDTerminalRuleCall_4_2_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getSchemaDefinitionRule());
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
            	    break loop38;
                }
            } while (true);

            otherlv_7=(Token)match(input,RightParenthesis,FOLLOW_2); 

            			newLeafNode(otherlv_7, grammarAccess.getSchemaDefinitionAccess().getRightParenthesisKeyword_5());
            		

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
    // $ANTLR end "ruleSchemaDefinition"


    // $ANTLR start "entryRuleCreate"
    // InternalCQLParser.g:2287:1: entryRuleCreate returns [EObject current=null] : iv_ruleCreate= ruleCreate EOF ;
    public final EObject entryRuleCreate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreate = null;


        try {
            // InternalCQLParser.g:2287:47: (iv_ruleCreate= ruleCreate EOF )
            // InternalCQLParser.g:2288:2: iv_ruleCreate= ruleCreate EOF
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
    // InternalCQLParser.g:2294:1: ruleCreate returns [EObject current=null] : ( () (otherlv_1= CREATE | otherlv_2= ATTACH ) ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) ) ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) ) ) ;
    public final EObject ruleCreate() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token lv_type_3_1=null;
        Token lv_type_3_2=null;
        Token lv_type_3_3=null;
        EObject lv_create_4_1 = null;

        EObject lv_create_4_2 = null;

        EObject lv_create_4_3 = null;

        EObject lv_create_4_4 = null;

        EObject lv_create_4_5 = null;

        EObject lv_create_4_6 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2300:2: ( ( () (otherlv_1= CREATE | otherlv_2= ATTACH ) ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) ) ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) ) ) )
            // InternalCQLParser.g:2301:2: ( () (otherlv_1= CREATE | otherlv_2= ATTACH ) ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) ) ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) ) )
            {
            // InternalCQLParser.g:2301:2: ( () (otherlv_1= CREATE | otherlv_2= ATTACH ) ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) ) ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) ) )
            // InternalCQLParser.g:2302:3: () (otherlv_1= CREATE | otherlv_2= ATTACH ) ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) ) ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) )
            {
            // InternalCQLParser.g:2302:3: ()
            // InternalCQLParser.g:2303:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getCreateAccess().getCreateAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:2309:3: (otherlv_1= CREATE | otherlv_2= ATTACH )
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==CREATE) ) {
                alt39=1;
            }
            else if ( (LA39_0==ATTACH) ) {
                alt39=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;
            }
            switch (alt39) {
                case 1 :
                    // InternalCQLParser.g:2310:4: otherlv_1= CREATE
                    {
                    otherlv_1=(Token)match(input,CREATE,FOLLOW_35); 

                    				newLeafNode(otherlv_1, grammarAccess.getCreateAccess().getCREATEKeyword_1_0());
                    			

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:2315:4: otherlv_2= ATTACH
                    {
                    otherlv_2=(Token)match(input,ATTACH,FOLLOW_35); 

                    				newLeafNode(otherlv_2, grammarAccess.getCreateAccess().getATTACHKeyword_1_1());
                    			

                    }
                    break;

            }

            // InternalCQLParser.g:2320:3: ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) )
            // InternalCQLParser.g:2321:4: ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) )
            {
            // InternalCQLParser.g:2321:4: ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) )
            // InternalCQLParser.g:2322:5: (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW )
            {
            // InternalCQLParser.g:2322:5: (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW )
            int alt40=3;
            switch ( input.LA(1) ) {
            case STREAM:
                {
                alt40=1;
                }
                break;
            case SINK:
                {
                alt40=2;
                }
                break;
            case VIEW:
                {
                alt40=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 40, 0, input);

                throw nvae;
            }

            switch (alt40) {
                case 1 :
                    // InternalCQLParser.g:2323:6: lv_type_3_1= STREAM
                    {
                    lv_type_3_1=(Token)match(input,STREAM,FOLLOW_14); 

                    						newLeafNode(lv_type_3_1, grammarAccess.getCreateAccess().getTypeSTREAMKeyword_2_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateRule());
                    						}
                    						setWithLastConsumed(current, "type", lv_type_3_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:2334:6: lv_type_3_2= SINK
                    {
                    lv_type_3_2=(Token)match(input,SINK,FOLLOW_14); 

                    						newLeafNode(lv_type_3_2, grammarAccess.getCreateAccess().getTypeSINKKeyword_2_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateRule());
                    						}
                    						setWithLastConsumed(current, "type", lv_type_3_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:2345:6: lv_type_3_3= VIEW
                    {
                    lv_type_3_3=(Token)match(input,VIEW,FOLLOW_14); 

                    						newLeafNode(lv_type_3_3, grammarAccess.getCreateAccess().getTypeVIEWKeyword_2_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateRule());
                    						}
                    						setWithLastConsumed(current, "type", lv_type_3_3, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQLParser.g:2358:3: ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) )
            // InternalCQLParser.g:2359:4: ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) )
            {
            // InternalCQLParser.g:2359:4: ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) )
            // InternalCQLParser.g:2360:5: (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView )
            {
            // InternalCQLParser.g:2360:5: (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView )
            int alt41=6;
            alt41 = dfa41.predict(input);
            switch (alt41) {
                case 1 :
                    // InternalCQLParser.g:2361:6: lv_create_4_1= ruleCreateAccessFramework
                    {

                    						newCompositeNode(grammarAccess.getCreateAccess().getCreateCreateAccessFrameworkParserRuleCall_3_0_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_create_4_1=ruleCreateAccessFramework();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCreateRule());
                    						}
                    						set(
                    							current,
                    							"create",
                    							lv_create_4_1,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateAccessFramework");
                    						afterParserOrEnumRuleCall();
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:2377:6: lv_create_4_2= ruleCreateChannelFrameworkViaPort
                    {

                    						newCompositeNode(grammarAccess.getCreateAccess().getCreateCreateChannelFrameworkViaPortParserRuleCall_3_0_1());
                    					
                    pushFollow(FOLLOW_2);
                    lv_create_4_2=ruleCreateChannelFrameworkViaPort();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCreateRule());
                    						}
                    						set(
                    							current,
                    							"create",
                    							lv_create_4_2,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateChannelFrameworkViaPort");
                    						afterParserOrEnumRuleCall();
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:2393:6: lv_create_4_3= ruleCreateChannelFormatViaFile
                    {

                    						newCompositeNode(grammarAccess.getCreateAccess().getCreateCreateChannelFormatViaFileParserRuleCall_3_0_2());
                    					
                    pushFollow(FOLLOW_2);
                    lv_create_4_3=ruleCreateChannelFormatViaFile();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCreateRule());
                    						}
                    						set(
                    							current,
                    							"create",
                    							lv_create_4_3,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateChannelFormatViaFile");
                    						afterParserOrEnumRuleCall();
                    					

                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:2409:6: lv_create_4_4= ruleCreateDatabaseStream
                    {

                    						newCompositeNode(grammarAccess.getCreateAccess().getCreateCreateDatabaseStreamParserRuleCall_3_0_3());
                    					
                    pushFollow(FOLLOW_2);
                    lv_create_4_4=ruleCreateDatabaseStream();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCreateRule());
                    						}
                    						set(
                    							current,
                    							"create",
                    							lv_create_4_4,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateDatabaseStream");
                    						afterParserOrEnumRuleCall();
                    					

                    }
                    break;
                case 5 :
                    // InternalCQLParser.g:2425:6: lv_create_4_5= ruleCreateDatabaseSink
                    {

                    						newCompositeNode(grammarAccess.getCreateAccess().getCreateCreateDatabaseSinkParserRuleCall_3_0_4());
                    					
                    pushFollow(FOLLOW_2);
                    lv_create_4_5=ruleCreateDatabaseSink();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCreateRule());
                    						}
                    						set(
                    							current,
                    							"create",
                    							lv_create_4_5,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateDatabaseSink");
                    						afterParserOrEnumRuleCall();
                    					

                    }
                    break;
                case 6 :
                    // InternalCQLParser.g:2441:6: lv_create_4_6= ruleCreateView
                    {

                    						newCompositeNode(grammarAccess.getCreateAccess().getCreateCreateViewParserRuleCall_3_0_5());
                    					
                    pushFollow(FOLLOW_2);
                    lv_create_4_6=ruleCreateView();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCreateRule());
                    						}
                    						set(
                    							current,
                    							"create",
                    							lv_create_4_6,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateView");
                    						afterParserOrEnumRuleCall();
                    					

                    }
                    break;

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
    // $ANTLR end "ruleCreate"


    // $ANTLR start "entryRuleCreateAccessFramework"
    // InternalCQLParser.g:2463:1: entryRuleCreateAccessFramework returns [EObject current=null] : iv_ruleCreateAccessFramework= ruleCreateAccessFramework EOF ;
    public final EObject entryRuleCreateAccessFramework() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateAccessFramework = null;


        try {
            // InternalCQLParser.g:2463:62: (iv_ruleCreateAccessFramework= ruleCreateAccessFramework EOF )
            // InternalCQLParser.g:2464:2: iv_ruleCreateAccessFramework= ruleCreateAccessFramework EOF
            {
             newCompositeNode(grammarAccess.getCreateAccessFrameworkRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateAccessFramework=ruleCreateAccessFramework();

            state._fsp--;

             current =iv_ruleCreateAccessFramework; 
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
    // $ANTLR end "entryRuleCreateAccessFramework"


    // $ANTLR start "ruleCreateAccessFramework"
    // InternalCQLParser.g:2470:1: ruleCreateAccessFramework returns [EObject current=null] : ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) ( (lv_pars_1_0= ruleAccessFramework ) ) ) ;
    public final EObject ruleCreateAccessFramework() throws RecognitionException {
        EObject current = null;

        EObject lv_attributes_0_0 = null;

        EObject lv_pars_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2476:2: ( ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) ( (lv_pars_1_0= ruleAccessFramework ) ) ) )
            // InternalCQLParser.g:2477:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) ( (lv_pars_1_0= ruleAccessFramework ) ) )
            {
            // InternalCQLParser.g:2477:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) ( (lv_pars_1_0= ruleAccessFramework ) ) )
            // InternalCQLParser.g:2478:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) ) ( (lv_pars_1_0= ruleAccessFramework ) )
            {
            // InternalCQLParser.g:2478:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) )
            // InternalCQLParser.g:2479:4: (lv_attributes_0_0= ruleSchemaDefinition )
            {
            // InternalCQLParser.g:2479:4: (lv_attributes_0_0= ruleSchemaDefinition )
            // InternalCQLParser.g:2480:5: lv_attributes_0_0= ruleSchemaDefinition
            {

            					newCompositeNode(grammarAccess.getCreateAccessFrameworkAccess().getAttributesSchemaDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_36);
            lv_attributes_0_0=ruleSchemaDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateAccessFrameworkRule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SchemaDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:2497:3: ( (lv_pars_1_0= ruleAccessFramework ) )
            // InternalCQLParser.g:2498:4: (lv_pars_1_0= ruleAccessFramework )
            {
            // InternalCQLParser.g:2498:4: (lv_pars_1_0= ruleAccessFramework )
            // InternalCQLParser.g:2499:5: lv_pars_1_0= ruleAccessFramework
            {

            					newCompositeNode(grammarAccess.getCreateAccessFrameworkAccess().getParsAccessFrameworkParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_pars_1_0=ruleAccessFramework();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateAccessFrameworkRule());
            					}
            					set(
            						current,
            						"pars",
            						lv_pars_1_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AccessFramework");
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
    // $ANTLR end "ruleCreateAccessFramework"


    // $ANTLR start "entryRuleCreateChannelFrameworkViaPort"
    // InternalCQLParser.g:2520:1: entryRuleCreateChannelFrameworkViaPort returns [EObject current=null] : iv_ruleCreateChannelFrameworkViaPort= ruleCreateChannelFrameworkViaPort EOF ;
    public final EObject entryRuleCreateChannelFrameworkViaPort() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateChannelFrameworkViaPort = null;


        try {
            // InternalCQLParser.g:2520:70: (iv_ruleCreateChannelFrameworkViaPort= ruleCreateChannelFrameworkViaPort EOF )
            // InternalCQLParser.g:2521:2: iv_ruleCreateChannelFrameworkViaPort= ruleCreateChannelFrameworkViaPort EOF
            {
             newCompositeNode(grammarAccess.getCreateChannelFrameworkViaPortRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateChannelFrameworkViaPort=ruleCreateChannelFrameworkViaPort();

            state._fsp--;

             current =iv_ruleCreateChannelFrameworkViaPort; 
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
    // $ANTLR end "entryRuleCreateChannelFrameworkViaPort"


    // $ANTLR start "ruleCreateChannelFrameworkViaPort"
    // InternalCQLParser.g:2527:1: ruleCreateChannelFrameworkViaPort returns [EObject current=null] : ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= CHANNEL ( (lv_host_2_0= RULE_ID ) ) otherlv_3= Colon ( (lv_port_4_0= RULE_INT ) ) ) ;
    public final EObject ruleCreateChannelFrameworkViaPort() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_host_2_0=null;
        Token otherlv_3=null;
        Token lv_port_4_0=null;
        EObject lv_attributes_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2533:2: ( ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= CHANNEL ( (lv_host_2_0= RULE_ID ) ) otherlv_3= Colon ( (lv_port_4_0= RULE_INT ) ) ) )
            // InternalCQLParser.g:2534:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= CHANNEL ( (lv_host_2_0= RULE_ID ) ) otherlv_3= Colon ( (lv_port_4_0= RULE_INT ) ) )
            {
            // InternalCQLParser.g:2534:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= CHANNEL ( (lv_host_2_0= RULE_ID ) ) otherlv_3= Colon ( (lv_port_4_0= RULE_INT ) ) )
            // InternalCQLParser.g:2535:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= CHANNEL ( (lv_host_2_0= RULE_ID ) ) otherlv_3= Colon ( (lv_port_4_0= RULE_INT ) )
            {
            // InternalCQLParser.g:2535:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) )
            // InternalCQLParser.g:2536:4: (lv_attributes_0_0= ruleSchemaDefinition )
            {
            // InternalCQLParser.g:2536:4: (lv_attributes_0_0= ruleSchemaDefinition )
            // InternalCQLParser.g:2537:5: lv_attributes_0_0= ruleSchemaDefinition
            {

            					newCompositeNode(grammarAccess.getCreateChannelFrameworkViaPortAccess().getAttributesSchemaDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_37);
            lv_attributes_0_0=ruleSchemaDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateChannelFrameworkViaPortRule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SchemaDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,CHANNEL,FOLLOW_14); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateChannelFrameworkViaPortAccess().getCHANNELKeyword_1());
            		
            // InternalCQLParser.g:2558:3: ( (lv_host_2_0= RULE_ID ) )
            // InternalCQLParser.g:2559:4: (lv_host_2_0= RULE_ID )
            {
            // InternalCQLParser.g:2559:4: (lv_host_2_0= RULE_ID )
            // InternalCQLParser.g:2560:5: lv_host_2_0= RULE_ID
            {
            lv_host_2_0=(Token)match(input,RULE_ID,FOLLOW_38); 

            					newLeafNode(lv_host_2_0, grammarAccess.getCreateChannelFrameworkViaPortAccess().getHostIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateChannelFrameworkViaPortRule());
            					}
            					setWithLastConsumed(
            						current,
            						"host",
            						lv_host_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_3=(Token)match(input,Colon,FOLLOW_39); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateChannelFrameworkViaPortAccess().getColonKeyword_3());
            		
            // InternalCQLParser.g:2580:3: ( (lv_port_4_0= RULE_INT ) )
            // InternalCQLParser.g:2581:4: (lv_port_4_0= RULE_INT )
            {
            // InternalCQLParser.g:2581:4: (lv_port_4_0= RULE_INT )
            // InternalCQLParser.g:2582:5: lv_port_4_0= RULE_INT
            {
            lv_port_4_0=(Token)match(input,RULE_INT,FOLLOW_2); 

            					newLeafNode(lv_port_4_0, grammarAccess.getCreateChannelFrameworkViaPortAccess().getPortINTTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateChannelFrameworkViaPortRule());
            					}
            					setWithLastConsumed(
            						current,
            						"port",
            						lv_port_4_0,
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
    // $ANTLR end "ruleCreateChannelFrameworkViaPort"


    // $ANTLR start "entryRuleCreateChannelFormatViaFile"
    // InternalCQLParser.g:2602:1: entryRuleCreateChannelFormatViaFile returns [EObject current=null] : iv_ruleCreateChannelFormatViaFile= ruleCreateChannelFormatViaFile EOF ;
    public final EObject entryRuleCreateChannelFormatViaFile() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateChannelFormatViaFile = null;


        try {
            // InternalCQLParser.g:2602:67: (iv_ruleCreateChannelFormatViaFile= ruleCreateChannelFormatViaFile EOF )
            // InternalCQLParser.g:2603:2: iv_ruleCreateChannelFormatViaFile= ruleCreateChannelFormatViaFile EOF
            {
             newCompositeNode(grammarAccess.getCreateChannelFormatViaFileRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateChannelFormatViaFile=ruleCreateChannelFormatViaFile();

            state._fsp--;

             current =iv_ruleCreateChannelFormatViaFile; 
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
    // $ANTLR end "entryRuleCreateChannelFormatViaFile"


    // $ANTLR start "ruleCreateChannelFormatViaFile"
    // InternalCQLParser.g:2609:1: ruleCreateChannelFormatViaFile returns [EObject current=null] : ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= FILE ( (lv_filename_2_0= RULE_STRING ) ) otherlv_3= AS ( (lv_type_4_0= RULE_ID ) ) ) ;
    public final EObject ruleCreateChannelFormatViaFile() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_filename_2_0=null;
        Token otherlv_3=null;
        Token lv_type_4_0=null;
        EObject lv_attributes_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2615:2: ( ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= FILE ( (lv_filename_2_0= RULE_STRING ) ) otherlv_3= AS ( (lv_type_4_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:2616:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= FILE ( (lv_filename_2_0= RULE_STRING ) ) otherlv_3= AS ( (lv_type_4_0= RULE_ID ) ) )
            {
            // InternalCQLParser.g:2616:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= FILE ( (lv_filename_2_0= RULE_STRING ) ) otherlv_3= AS ( (lv_type_4_0= RULE_ID ) ) )
            // InternalCQLParser.g:2617:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= FILE ( (lv_filename_2_0= RULE_STRING ) ) otherlv_3= AS ( (lv_type_4_0= RULE_ID ) )
            {
            // InternalCQLParser.g:2617:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) )
            // InternalCQLParser.g:2618:4: (lv_attributes_0_0= ruleSchemaDefinition )
            {
            // InternalCQLParser.g:2618:4: (lv_attributes_0_0= ruleSchemaDefinition )
            // InternalCQLParser.g:2619:5: lv_attributes_0_0= ruleSchemaDefinition
            {

            					newCompositeNode(grammarAccess.getCreateChannelFormatViaFileAccess().getAttributesSchemaDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_40);
            lv_attributes_0_0=ruleSchemaDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateChannelFormatViaFileRule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SchemaDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,FILE,FOLLOW_28); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateChannelFormatViaFileAccess().getFILEKeyword_1());
            		
            // InternalCQLParser.g:2640:3: ( (lv_filename_2_0= RULE_STRING ) )
            // InternalCQLParser.g:2641:4: (lv_filename_2_0= RULE_STRING )
            {
            // InternalCQLParser.g:2641:4: (lv_filename_2_0= RULE_STRING )
            // InternalCQLParser.g:2642:5: lv_filename_2_0= RULE_STRING
            {
            lv_filename_2_0=(Token)match(input,RULE_STRING,FOLLOW_21); 

            					newLeafNode(lv_filename_2_0, grammarAccess.getCreateChannelFormatViaFileAccess().getFilenameSTRINGTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateChannelFormatViaFileRule());
            					}
            					setWithLastConsumed(
            						current,
            						"filename",
            						lv_filename_2_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_3=(Token)match(input,AS,FOLLOW_14); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateChannelFormatViaFileAccess().getASKeyword_3());
            		
            // InternalCQLParser.g:2662:3: ( (lv_type_4_0= RULE_ID ) )
            // InternalCQLParser.g:2663:4: (lv_type_4_0= RULE_ID )
            {
            // InternalCQLParser.g:2663:4: (lv_type_4_0= RULE_ID )
            // InternalCQLParser.g:2664:5: lv_type_4_0= RULE_ID
            {
            lv_type_4_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(lv_type_4_0, grammarAccess.getCreateChannelFormatViaFileAccess().getTypeIDTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateChannelFormatViaFileRule());
            					}
            					setWithLastConsumed(
            						current,
            						"type",
            						lv_type_4_0,
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
    // $ANTLR end "ruleCreateChannelFormatViaFile"


    // $ANTLR start "entryRuleCreateDatabaseStream"
    // InternalCQLParser.g:2684:1: entryRuleCreateDatabaseStream returns [EObject current=null] : iv_ruleCreateDatabaseStream= ruleCreateDatabaseStream EOF ;
    public final EObject entryRuleCreateDatabaseStream() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateDatabaseStream = null;


        try {
            // InternalCQLParser.g:2684:61: (iv_ruleCreateDatabaseStream= ruleCreateDatabaseStream EOF )
            // InternalCQLParser.g:2685:2: iv_ruleCreateDatabaseStream= ruleCreateDatabaseStream EOF
            {
             newCompositeNode(grammarAccess.getCreateDatabaseStreamRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateDatabaseStream=ruleCreateDatabaseStream();

            state._fsp--;

             current =iv_ruleCreateDatabaseStream; 
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
    // $ANTLR end "entryRuleCreateDatabaseStream"


    // $ANTLR start "ruleCreateDatabaseStream"
    // InternalCQLParser.g:2691:1: ruleCreateDatabaseStream returns [EObject current=null] : ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= DATABASE ( (lv_database_2_0= RULE_ID ) ) otherlv_3= TABLE ( (lv_table_4_0= RULE_ID ) ) (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= RULE_ID ) ) )? ) ;
    public final EObject ruleCreateDatabaseStream() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_database_2_0=null;
        Token otherlv_3=null;
        Token lv_table_4_0=null;
        Token otherlv_5=null;
        Token lv_size_6_0=null;
        Token lv_unit_7_0=null;
        EObject lv_attributes_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2697:2: ( ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= DATABASE ( (lv_database_2_0= RULE_ID ) ) otherlv_3= TABLE ( (lv_table_4_0= RULE_ID ) ) (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= RULE_ID ) ) )? ) )
            // InternalCQLParser.g:2698:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= DATABASE ( (lv_database_2_0= RULE_ID ) ) otherlv_3= TABLE ( (lv_table_4_0= RULE_ID ) ) (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= RULE_ID ) ) )? )
            {
            // InternalCQLParser.g:2698:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= DATABASE ( (lv_database_2_0= RULE_ID ) ) otherlv_3= TABLE ( (lv_table_4_0= RULE_ID ) ) (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= RULE_ID ) ) )? )
            // InternalCQLParser.g:2699:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= DATABASE ( (lv_database_2_0= RULE_ID ) ) otherlv_3= TABLE ( (lv_table_4_0= RULE_ID ) ) (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= RULE_ID ) ) )?
            {
            // InternalCQLParser.g:2699:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) )
            // InternalCQLParser.g:2700:4: (lv_attributes_0_0= ruleSchemaDefinition )
            {
            // InternalCQLParser.g:2700:4: (lv_attributes_0_0= ruleSchemaDefinition )
            // InternalCQLParser.g:2701:5: lv_attributes_0_0= ruleSchemaDefinition
            {

            					newCompositeNode(grammarAccess.getCreateDatabaseStreamAccess().getAttributesSchemaDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_41);
            lv_attributes_0_0=ruleSchemaDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateDatabaseStreamRule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SchemaDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,DATABASE,FOLLOW_14); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateDatabaseStreamAccess().getDATABASEKeyword_1());
            		
            // InternalCQLParser.g:2722:3: ( (lv_database_2_0= RULE_ID ) )
            // InternalCQLParser.g:2723:4: (lv_database_2_0= RULE_ID )
            {
            // InternalCQLParser.g:2723:4: (lv_database_2_0= RULE_ID )
            // InternalCQLParser.g:2724:5: lv_database_2_0= RULE_ID
            {
            lv_database_2_0=(Token)match(input,RULE_ID,FOLLOW_42); 

            					newLeafNode(lv_database_2_0, grammarAccess.getCreateDatabaseStreamAccess().getDatabaseIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDatabaseStreamRule());
            					}
            					setWithLastConsumed(
            						current,
            						"database",
            						lv_database_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_3=(Token)match(input,TABLE,FOLLOW_14); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateDatabaseStreamAccess().getTABLEKeyword_3());
            		
            // InternalCQLParser.g:2744:3: ( (lv_table_4_0= RULE_ID ) )
            // InternalCQLParser.g:2745:4: (lv_table_4_0= RULE_ID )
            {
            // InternalCQLParser.g:2745:4: (lv_table_4_0= RULE_ID )
            // InternalCQLParser.g:2746:5: lv_table_4_0= RULE_ID
            {
            lv_table_4_0=(Token)match(input,RULE_ID,FOLLOW_43); 

            					newLeafNode(lv_table_4_0, grammarAccess.getCreateDatabaseStreamAccess().getTableIDTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDatabaseStreamRule());
            					}
            					setWithLastConsumed(
            						current,
            						"table",
            						lv_table_4_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:2762:3: (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= RULE_ID ) ) )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==EACH) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // InternalCQLParser.g:2763:4: otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= RULE_ID ) )
                    {
                    otherlv_5=(Token)match(input,EACH,FOLLOW_39); 

                    				newLeafNode(otherlv_5, grammarAccess.getCreateDatabaseStreamAccess().getEACHKeyword_5_0());
                    			
                    // InternalCQLParser.g:2767:4: ( (lv_size_6_0= RULE_INT ) )
                    // InternalCQLParser.g:2768:5: (lv_size_6_0= RULE_INT )
                    {
                    // InternalCQLParser.g:2768:5: (lv_size_6_0= RULE_INT )
                    // InternalCQLParser.g:2769:6: lv_size_6_0= RULE_INT
                    {
                    lv_size_6_0=(Token)match(input,RULE_INT,FOLLOW_14); 

                    						newLeafNode(lv_size_6_0, grammarAccess.getCreateDatabaseStreamAccess().getSizeINTTerminalRuleCall_5_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateDatabaseStreamRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"size",
                    							lv_size_6_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }

                    // InternalCQLParser.g:2785:4: ( (lv_unit_7_0= RULE_ID ) )
                    // InternalCQLParser.g:2786:5: (lv_unit_7_0= RULE_ID )
                    {
                    // InternalCQLParser.g:2786:5: (lv_unit_7_0= RULE_ID )
                    // InternalCQLParser.g:2787:6: lv_unit_7_0= RULE_ID
                    {
                    lv_unit_7_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    						newLeafNode(lv_unit_7_0, grammarAccess.getCreateDatabaseStreamAccess().getUnitIDTerminalRuleCall_5_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateDatabaseStreamRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"unit",
                    							lv_unit_7_0,
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
    // $ANTLR end "ruleCreateDatabaseStream"


    // $ANTLR start "entryRuleCreateDatabaseSink"
    // InternalCQLParser.g:2808:1: entryRuleCreateDatabaseSink returns [EObject current=null] : iv_ruleCreateDatabaseSink= ruleCreateDatabaseSink EOF ;
    public final EObject entryRuleCreateDatabaseSink() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateDatabaseSink = null;


        try {
            // InternalCQLParser.g:2808:59: (iv_ruleCreateDatabaseSink= ruleCreateDatabaseSink EOF )
            // InternalCQLParser.g:2809:2: iv_ruleCreateDatabaseSink= ruleCreateDatabaseSink EOF
            {
             newCompositeNode(grammarAccess.getCreateDatabaseSinkRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateDatabaseSink=ruleCreateDatabaseSink();

            state._fsp--;

             current =iv_ruleCreateDatabaseSink; 
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
    // $ANTLR end "entryRuleCreateDatabaseSink"


    // $ANTLR start "ruleCreateDatabaseSink"
    // InternalCQLParser.g:2815:1: ruleCreateDatabaseSink returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= AS otherlv_2= DATABASE ( (lv_database_3_0= RULE_ID ) ) otherlv_4= TABLE ( (lv_table_5_0= RULE_ID ) ) (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )? ) ;
    public final EObject ruleCreateDatabaseSink() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token lv_database_3_0=null;
        Token otherlv_4=null;
        Token lv_table_5_0=null;
        Token otherlv_6=null;
        Token lv_option_7_1=null;
        Token lv_option_7_2=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2821:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= AS otherlv_2= DATABASE ( (lv_database_3_0= RULE_ID ) ) otherlv_4= TABLE ( (lv_table_5_0= RULE_ID ) ) (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )? ) )
            // InternalCQLParser.g:2822:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= AS otherlv_2= DATABASE ( (lv_database_3_0= RULE_ID ) ) otherlv_4= TABLE ( (lv_table_5_0= RULE_ID ) ) (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )? )
            {
            // InternalCQLParser.g:2822:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= AS otherlv_2= DATABASE ( (lv_database_3_0= RULE_ID ) ) otherlv_4= TABLE ( (lv_table_5_0= RULE_ID ) ) (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )? )
            // InternalCQLParser.g:2823:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= AS otherlv_2= DATABASE ( (lv_database_3_0= RULE_ID ) ) otherlv_4= TABLE ( (lv_table_5_0= RULE_ID ) ) (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )?
            {
            // InternalCQLParser.g:2823:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:2824:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:2824:4: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:2825:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_21); 

            					newLeafNode(lv_name_0_0, grammarAccess.getCreateDatabaseSinkAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDatabaseSinkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,AS,FOLLOW_41); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateDatabaseSinkAccess().getASKeyword_1());
            		
            otherlv_2=(Token)match(input,DATABASE,FOLLOW_14); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateDatabaseSinkAccess().getDATABASEKeyword_2());
            		
            // InternalCQLParser.g:2849:3: ( (lv_database_3_0= RULE_ID ) )
            // InternalCQLParser.g:2850:4: (lv_database_3_0= RULE_ID )
            {
            // InternalCQLParser.g:2850:4: (lv_database_3_0= RULE_ID )
            // InternalCQLParser.g:2851:5: lv_database_3_0= RULE_ID
            {
            lv_database_3_0=(Token)match(input,RULE_ID,FOLLOW_42); 

            					newLeafNode(lv_database_3_0, grammarAccess.getCreateDatabaseSinkAccess().getDatabaseIDTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDatabaseSinkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"database",
            						lv_database_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_4=(Token)match(input,TABLE,FOLLOW_14); 

            			newLeafNode(otherlv_4, grammarAccess.getCreateDatabaseSinkAccess().getTABLEKeyword_4());
            		
            // InternalCQLParser.g:2871:3: ( (lv_table_5_0= RULE_ID ) )
            // InternalCQLParser.g:2872:4: (lv_table_5_0= RULE_ID )
            {
            // InternalCQLParser.g:2872:4: (lv_table_5_0= RULE_ID )
            // InternalCQLParser.g:2873:5: lv_table_5_0= RULE_ID
            {
            lv_table_5_0=(Token)match(input,RULE_ID,FOLLOW_44); 

            					newLeafNode(lv_table_5_0, grammarAccess.getCreateDatabaseSinkAccess().getTableIDTerminalRuleCall_5_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDatabaseSinkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"table",
            						lv_table_5_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:2889:3: (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )?
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==AND) ) {
                alt44=1;
            }
            switch (alt44) {
                case 1 :
                    // InternalCQLParser.g:2890:4: otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) )
                    {
                    otherlv_6=(Token)match(input,AND,FOLLOW_45); 

                    				newLeafNode(otherlv_6, grammarAccess.getCreateDatabaseSinkAccess().getANDKeyword_6_0());
                    			
                    // InternalCQLParser.g:2894:4: ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) )
                    // InternalCQLParser.g:2895:5: ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) )
                    {
                    // InternalCQLParser.g:2895:5: ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) )
                    // InternalCQLParser.g:2896:6: (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE )
                    {
                    // InternalCQLParser.g:2896:6: (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE )
                    int alt43=2;
                    int LA43_0 = input.LA(1);

                    if ( (LA43_0==DROP) ) {
                        alt43=1;
                    }
                    else if ( (LA43_0==TRUNCATE) ) {
                        alt43=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 43, 0, input);

                        throw nvae;
                    }
                    switch (alt43) {
                        case 1 :
                            // InternalCQLParser.g:2897:7: lv_option_7_1= DROP
                            {
                            lv_option_7_1=(Token)match(input,DROP,FOLLOW_2); 

                            							newLeafNode(lv_option_7_1, grammarAccess.getCreateDatabaseSinkAccess().getOptionDROPKeyword_6_1_0_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getCreateDatabaseSinkRule());
                            							}
                            							setWithLastConsumed(current, "option", lv_option_7_1, null);
                            						

                            }
                            break;
                        case 2 :
                            // InternalCQLParser.g:2908:7: lv_option_7_2= TRUNCATE
                            {
                            lv_option_7_2=(Token)match(input,TRUNCATE,FOLLOW_2); 

                            							newLeafNode(lv_option_7_2, grammarAccess.getCreateDatabaseSinkAccess().getOptionTRUNCATEKeyword_6_1_0_1());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getCreateDatabaseSinkRule());
                            							}
                            							setWithLastConsumed(current, "option", lv_option_7_2, null);
                            						

                            }
                            break;

                    }


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
    // $ANTLR end "ruleCreateDatabaseSink"


    // $ANTLR start "entryRuleCreateView"
    // InternalCQLParser.g:2926:1: entryRuleCreateView returns [EObject current=null] : iv_ruleCreateView= ruleCreateView EOF ;
    public final EObject entryRuleCreateView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateView = null;


        try {
            // InternalCQLParser.g:2926:51: (iv_ruleCreateView= ruleCreateView EOF )
            // InternalCQLParser.g:2927:2: iv_ruleCreateView= ruleCreateView EOF
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
    // InternalCQLParser.g:2933:1: ruleCreateView returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= FROM ( (lv_select_2_0= ruleInnerSelect ) ) ) ;
    public final EObject ruleCreateView() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        EObject lv_select_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2939:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= FROM ( (lv_select_2_0= ruleInnerSelect ) ) ) )
            // InternalCQLParser.g:2940:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= FROM ( (lv_select_2_0= ruleInnerSelect ) ) )
            {
            // InternalCQLParser.g:2940:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= FROM ( (lv_select_2_0= ruleInnerSelect ) ) )
            // InternalCQLParser.g:2941:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= FROM ( (lv_select_2_0= ruleInnerSelect ) )
            {
            // InternalCQLParser.g:2941:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:2942:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:2942:4: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:2943:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_5); 

            					newLeafNode(lv_name_0_0, grammarAccess.getCreateViewAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateViewRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,FROM,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateViewAccess().getFROMKeyword_1());
            		
            // InternalCQLParser.g:2963:3: ( (lv_select_2_0= ruleInnerSelect ) )
            // InternalCQLParser.g:2964:4: (lv_select_2_0= ruleInnerSelect )
            {
            // InternalCQLParser.g:2964:4: (lv_select_2_0= ruleInnerSelect )
            // InternalCQLParser.g:2965:5: lv_select_2_0= ruleInnerSelect
            {

            					newCompositeNode(grammarAccess.getCreateViewAccess().getSelectInnerSelectParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_2);
            lv_select_2_0=ruleInnerSelect();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateViewRule());
            					}
            					set(
            						current,
            						"select",
            						lv_select_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.InnerSelect");
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


    // $ANTLR start "entryRuleCreateDataBaseJDBCConnection"
    // InternalCQLParser.g:2986:1: entryRuleCreateDataBaseJDBCConnection returns [EObject current=null] : iv_ruleCreateDataBaseJDBCConnection= ruleCreateDataBaseJDBCConnection EOF ;
    public final EObject entryRuleCreateDataBaseJDBCConnection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateDataBaseJDBCConnection = null;


        try {
            // InternalCQLParser.g:2986:69: (iv_ruleCreateDataBaseJDBCConnection= ruleCreateDataBaseJDBCConnection EOF )
            // InternalCQLParser.g:2987:2: iv_ruleCreateDataBaseJDBCConnection= ruleCreateDataBaseJDBCConnection EOF
            {
             newCompositeNode(grammarAccess.getCreateDataBaseJDBCConnectionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateDataBaseJDBCConnection=ruleCreateDataBaseJDBCConnection();

            state._fsp--;

             current =iv_ruleCreateDataBaseJDBCConnection; 
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
    // $ANTLR end "entryRuleCreateDataBaseJDBCConnection"


    // $ANTLR start "ruleCreateDataBaseJDBCConnection"
    // InternalCQLParser.g:2993:1: ruleCreateDataBaseJDBCConnection returns [EObject current=null] : ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= JDBC ( (lv_server_6_0= RULE_ID ) ) (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )? ) ;
    public final EObject ruleCreateDataBaseJDBCConnection() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token lv_name_4_0=null;
        Token otherlv_5=null;
        Token lv_server_6_0=null;
        Token otherlv_7=null;
        Token otherlv_8=null;
        Token lv_user_9_0=null;
        Token otherlv_10=null;
        Token lv_password_11_0=null;
        Token lv_lazy_12_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2999:2: ( ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= JDBC ( (lv_server_6_0= RULE_ID ) ) (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )? ) )
            // InternalCQLParser.g:3000:2: ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= JDBC ( (lv_server_6_0= RULE_ID ) ) (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )? )
            {
            // InternalCQLParser.g:3000:2: ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= JDBC ( (lv_server_6_0= RULE_ID ) ) (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )? )
            // InternalCQLParser.g:3001:3: () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= JDBC ( (lv_server_6_0= RULE_ID ) ) (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )?
            {
            // InternalCQLParser.g:3001:3: ()
            // InternalCQLParser.g:3002:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getCreateDataBaseJDBCConnectionAccess().getCreateDataBaseConnectionJDBCAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,CREATE,FOLLOW_41); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getCREATEKeyword_1());
            		
            otherlv_2=(Token)match(input,DATABASE,FOLLOW_46); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getDATABASEKeyword_2());
            		
            otherlv_3=(Token)match(input,CONNECTION,FOLLOW_14); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getCONNECTIONKeyword_3());
            		
            // InternalCQLParser.g:3020:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCQLParser.g:3021:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCQLParser.g:3021:4: (lv_name_4_0= RULE_ID )
            // InternalCQLParser.g:3022:5: lv_name_4_0= RULE_ID
            {
            lv_name_4_0=(Token)match(input,RULE_ID,FOLLOW_47); 

            					newLeafNode(lv_name_4_0, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getNameIDTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDataBaseJDBCConnectionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_4_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_5=(Token)match(input,JDBC,FOLLOW_14); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getJDBCKeyword_5());
            		
            // InternalCQLParser.g:3042:3: ( (lv_server_6_0= RULE_ID ) )
            // InternalCQLParser.g:3043:4: (lv_server_6_0= RULE_ID )
            {
            // InternalCQLParser.g:3043:4: (lv_server_6_0= RULE_ID )
            // InternalCQLParser.g:3044:5: lv_server_6_0= RULE_ID
            {
            lv_server_6_0=(Token)match(input,RULE_ID,FOLLOW_48); 

            					newLeafNode(lv_server_6_0, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getServerIDTerminalRuleCall_6_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDataBaseJDBCConnectionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"server",
            						lv_server_6_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:3060:3: (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )?
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==WITH) ) {
                alt46=1;
            }
            switch (alt46) {
                case 1 :
                    // InternalCQLParser.g:3061:4: otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )?
                    {
                    otherlv_7=(Token)match(input,WITH,FOLLOW_49); 

                    				newLeafNode(otherlv_7, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getWITHKeyword_7_0());
                    			
                    otherlv_8=(Token)match(input,USER,FOLLOW_14); 

                    				newLeafNode(otherlv_8, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getUSERKeyword_7_1());
                    			
                    // InternalCQLParser.g:3069:4: ( (lv_user_9_0= RULE_ID ) )
                    // InternalCQLParser.g:3070:5: (lv_user_9_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3070:5: (lv_user_9_0= RULE_ID )
                    // InternalCQLParser.g:3071:6: lv_user_9_0= RULE_ID
                    {
                    lv_user_9_0=(Token)match(input,RULE_ID,FOLLOW_50); 

                    						newLeafNode(lv_user_9_0, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getUserIDTerminalRuleCall_7_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateDataBaseJDBCConnectionRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"user",
                    							lv_user_9_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }

                    otherlv_10=(Token)match(input,PASSWORD,FOLLOW_14); 

                    				newLeafNode(otherlv_10, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getPASSWORDKeyword_7_3());
                    			
                    // InternalCQLParser.g:3091:4: ( (lv_password_11_0= RULE_ID ) )
                    // InternalCQLParser.g:3092:5: (lv_password_11_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3092:5: (lv_password_11_0= RULE_ID )
                    // InternalCQLParser.g:3093:6: lv_password_11_0= RULE_ID
                    {
                    lv_password_11_0=(Token)match(input,RULE_ID,FOLLOW_51); 

                    						newLeafNode(lv_password_11_0, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getPasswordIDTerminalRuleCall_7_4_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateDataBaseJDBCConnectionRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"password",
                    							lv_password_11_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }

                    // InternalCQLParser.g:3109:4: ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )?
                    int alt45=2;
                    int LA45_0 = input.LA(1);

                    if ( (LA45_0==NO_LAZY_CONNECTION_CHECK) ) {
                        alt45=1;
                    }
                    switch (alt45) {
                        case 1 :
                            // InternalCQLParser.g:3110:5: (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK )
                            {
                            // InternalCQLParser.g:3110:5: (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK )
                            // InternalCQLParser.g:3111:6: lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK
                            {
                            lv_lazy_12_0=(Token)match(input,NO_LAZY_CONNECTION_CHECK,FOLLOW_2); 

                            						newLeafNode(lv_lazy_12_0, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getLazyNO_LAZY_CONNECTION_CHECKKeyword_7_5_0());
                            					

                            						if (current==null) {
                            							current = createModelElement(grammarAccess.getCreateDataBaseJDBCConnectionRule());
                            						}
                            						setWithLastConsumed(current, "lazy", lv_lazy_12_0, "NO_LAZY_CONNECTION_CHECK");
                            					

                            }


                            }
                            break;

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
    // $ANTLR end "ruleCreateDataBaseJDBCConnection"


    // $ANTLR start "entryRuleCreateDataBaseGenericConnection"
    // InternalCQLParser.g:3128:1: entryRuleCreateDataBaseGenericConnection returns [EObject current=null] : iv_ruleCreateDataBaseGenericConnection= ruleCreateDataBaseGenericConnection EOF ;
    public final EObject entryRuleCreateDataBaseGenericConnection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateDataBaseGenericConnection = null;


        try {
            // InternalCQLParser.g:3128:72: (iv_ruleCreateDataBaseGenericConnection= ruleCreateDataBaseGenericConnection EOF )
            // InternalCQLParser.g:3129:2: iv_ruleCreateDataBaseGenericConnection= ruleCreateDataBaseGenericConnection EOF
            {
             newCompositeNode(grammarAccess.getCreateDataBaseGenericConnectionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateDataBaseGenericConnection=ruleCreateDataBaseGenericConnection();

            state._fsp--;

             current =iv_ruleCreateDataBaseGenericConnection; 
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
    // $ANTLR end "entryRuleCreateDataBaseGenericConnection"


    // $ANTLR start "ruleCreateDataBaseGenericConnection"
    // InternalCQLParser.g:3135:1: ruleCreateDataBaseGenericConnection returns [EObject current=null] : ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= AS ( (lv_driver_6_0= RULE_ID ) ) otherlv_7= TO ( (lv_source_8_0= RULE_ID ) ) (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )? (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )? ) ;
    public final EObject ruleCreateDataBaseGenericConnection() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token lv_name_4_0=null;
        Token otherlv_5=null;
        Token lv_driver_6_0=null;
        Token otherlv_7=null;
        Token lv_source_8_0=null;
        Token otherlv_9=null;
        Token lv_host_10_0=null;
        Token otherlv_11=null;
        Token lv_port_12_0=null;
        Token otherlv_13=null;
        Token otherlv_14=null;
        Token lv_user_15_0=null;
        Token otherlv_16=null;
        Token lv_password_17_0=null;
        Token lv_lazy_18_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:3141:2: ( ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= AS ( (lv_driver_6_0= RULE_ID ) ) otherlv_7= TO ( (lv_source_8_0= RULE_ID ) ) (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )? (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )? ) )
            // InternalCQLParser.g:3142:2: ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= AS ( (lv_driver_6_0= RULE_ID ) ) otherlv_7= TO ( (lv_source_8_0= RULE_ID ) ) (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )? (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )? )
            {
            // InternalCQLParser.g:3142:2: ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= AS ( (lv_driver_6_0= RULE_ID ) ) otherlv_7= TO ( (lv_source_8_0= RULE_ID ) ) (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )? (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )? )
            // InternalCQLParser.g:3143:3: () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= AS ( (lv_driver_6_0= RULE_ID ) ) otherlv_7= TO ( (lv_source_8_0= RULE_ID ) ) (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )? (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )?
            {
            // InternalCQLParser.g:3143:3: ()
            // InternalCQLParser.g:3144:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getCreateDataBaseGenericConnectionAccess().getCreateDataBaseConnectionGenericAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,CREATE,FOLLOW_41); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateDataBaseGenericConnectionAccess().getCREATEKeyword_1());
            		
            otherlv_2=(Token)match(input,DATABASE,FOLLOW_46); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateDataBaseGenericConnectionAccess().getDATABASEKeyword_2());
            		
            otherlv_3=(Token)match(input,CONNECTION,FOLLOW_14); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateDataBaseGenericConnectionAccess().getCONNECTIONKeyword_3());
            		
            // InternalCQLParser.g:3162:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCQLParser.g:3163:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCQLParser.g:3163:4: (lv_name_4_0= RULE_ID )
            // InternalCQLParser.g:3164:5: lv_name_4_0= RULE_ID
            {
            lv_name_4_0=(Token)match(input,RULE_ID,FOLLOW_21); 

            					newLeafNode(lv_name_4_0, grammarAccess.getCreateDataBaseGenericConnectionAccess().getNameIDTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDataBaseGenericConnectionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_4_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_5=(Token)match(input,AS,FOLLOW_14); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateDataBaseGenericConnectionAccess().getASKeyword_5());
            		
            // InternalCQLParser.g:3184:3: ( (lv_driver_6_0= RULE_ID ) )
            // InternalCQLParser.g:3185:4: (lv_driver_6_0= RULE_ID )
            {
            // InternalCQLParser.g:3185:4: (lv_driver_6_0= RULE_ID )
            // InternalCQLParser.g:3186:5: lv_driver_6_0= RULE_ID
            {
            lv_driver_6_0=(Token)match(input,RULE_ID,FOLLOW_52); 

            					newLeafNode(lv_driver_6_0, grammarAccess.getCreateDataBaseGenericConnectionAccess().getDriverIDTerminalRuleCall_6_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDataBaseGenericConnectionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"driver",
            						lv_driver_6_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_7=(Token)match(input,TO,FOLLOW_14); 

            			newLeafNode(otherlv_7, grammarAccess.getCreateDataBaseGenericConnectionAccess().getTOKeyword_7());
            		
            // InternalCQLParser.g:3206:3: ( (lv_source_8_0= RULE_ID ) )
            // InternalCQLParser.g:3207:4: (lv_source_8_0= RULE_ID )
            {
            // InternalCQLParser.g:3207:4: (lv_source_8_0= RULE_ID )
            // InternalCQLParser.g:3208:5: lv_source_8_0= RULE_ID
            {
            lv_source_8_0=(Token)match(input,RULE_ID,FOLLOW_53); 

            					newLeafNode(lv_source_8_0, grammarAccess.getCreateDataBaseGenericConnectionAccess().getSourceIDTerminalRuleCall_8_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDataBaseGenericConnectionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"source",
            						lv_source_8_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:3224:3: (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )?
            int alt47=2;
            int LA47_0 = input.LA(1);

            if ( (LA47_0==AT) ) {
                alt47=1;
            }
            switch (alt47) {
                case 1 :
                    // InternalCQLParser.g:3225:4: otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) )
                    {
                    otherlv_9=(Token)match(input,AT,FOLLOW_14); 

                    				newLeafNode(otherlv_9, grammarAccess.getCreateDataBaseGenericConnectionAccess().getATKeyword_9_0());
                    			
                    // InternalCQLParser.g:3229:4: ( (lv_host_10_0= RULE_ID ) )
                    // InternalCQLParser.g:3230:5: (lv_host_10_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3230:5: (lv_host_10_0= RULE_ID )
                    // InternalCQLParser.g:3231:6: lv_host_10_0= RULE_ID
                    {
                    lv_host_10_0=(Token)match(input,RULE_ID,FOLLOW_38); 

                    						newLeafNode(lv_host_10_0, grammarAccess.getCreateDataBaseGenericConnectionAccess().getHostIDTerminalRuleCall_9_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateDataBaseGenericConnectionRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"host",
                    							lv_host_10_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }

                    otherlv_11=(Token)match(input,Colon,FOLLOW_39); 

                    				newLeafNode(otherlv_11, grammarAccess.getCreateDataBaseGenericConnectionAccess().getColonKeyword_9_2());
                    			
                    // InternalCQLParser.g:3251:4: ( (lv_port_12_0= RULE_INT ) )
                    // InternalCQLParser.g:3252:5: (lv_port_12_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3252:5: (lv_port_12_0= RULE_INT )
                    // InternalCQLParser.g:3253:6: lv_port_12_0= RULE_INT
                    {
                    lv_port_12_0=(Token)match(input,RULE_INT,FOLLOW_48); 

                    						newLeafNode(lv_port_12_0, grammarAccess.getCreateDataBaseGenericConnectionAccess().getPortINTTerminalRuleCall_9_3_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateDataBaseGenericConnectionRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"port",
                    							lv_port_12_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:3270:3: (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )?
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==WITH) ) {
                alt49=1;
            }
            switch (alt49) {
                case 1 :
                    // InternalCQLParser.g:3271:4: otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )?
                    {
                    otherlv_13=(Token)match(input,WITH,FOLLOW_49); 

                    				newLeafNode(otherlv_13, grammarAccess.getCreateDataBaseGenericConnectionAccess().getWITHKeyword_10_0());
                    			
                    otherlv_14=(Token)match(input,USER,FOLLOW_14); 

                    				newLeafNode(otherlv_14, grammarAccess.getCreateDataBaseGenericConnectionAccess().getUSERKeyword_10_1());
                    			
                    // InternalCQLParser.g:3279:4: ( (lv_user_15_0= RULE_ID ) )
                    // InternalCQLParser.g:3280:5: (lv_user_15_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3280:5: (lv_user_15_0= RULE_ID )
                    // InternalCQLParser.g:3281:6: lv_user_15_0= RULE_ID
                    {
                    lv_user_15_0=(Token)match(input,RULE_ID,FOLLOW_50); 

                    						newLeafNode(lv_user_15_0, grammarAccess.getCreateDataBaseGenericConnectionAccess().getUserIDTerminalRuleCall_10_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateDataBaseGenericConnectionRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"user",
                    							lv_user_15_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }

                    otherlv_16=(Token)match(input,PASSWORD,FOLLOW_14); 

                    				newLeafNode(otherlv_16, grammarAccess.getCreateDataBaseGenericConnectionAccess().getPASSWORDKeyword_10_3());
                    			
                    // InternalCQLParser.g:3301:4: ( (lv_password_17_0= RULE_ID ) )
                    // InternalCQLParser.g:3302:5: (lv_password_17_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3302:5: (lv_password_17_0= RULE_ID )
                    // InternalCQLParser.g:3303:6: lv_password_17_0= RULE_ID
                    {
                    lv_password_17_0=(Token)match(input,RULE_ID,FOLLOW_51); 

                    						newLeafNode(lv_password_17_0, grammarAccess.getCreateDataBaseGenericConnectionAccess().getPasswordIDTerminalRuleCall_10_4_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateDataBaseGenericConnectionRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"password",
                    							lv_password_17_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }

                    // InternalCQLParser.g:3319:4: ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )?
                    int alt48=2;
                    int LA48_0 = input.LA(1);

                    if ( (LA48_0==NO_LAZY_CONNECTION_CHECK) ) {
                        alt48=1;
                    }
                    switch (alt48) {
                        case 1 :
                            // InternalCQLParser.g:3320:5: (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK )
                            {
                            // InternalCQLParser.g:3320:5: (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK )
                            // InternalCQLParser.g:3321:6: lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK
                            {
                            lv_lazy_18_0=(Token)match(input,NO_LAZY_CONNECTION_CHECK,FOLLOW_2); 

                            						newLeafNode(lv_lazy_18_0, grammarAccess.getCreateDataBaseGenericConnectionAccess().getLazyNO_LAZY_CONNECTION_CHECKKeyword_10_5_0());
                            					

                            						if (current==null) {
                            							current = createModelElement(grammarAccess.getCreateDataBaseGenericConnectionRule());
                            						}
                            						setWithLastConsumed(current, "lazy", lv_lazy_18_0, "NO_LAZY_CONNECTION_CHECK");
                            					

                            }


                            }
                            break;

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
    // $ANTLR end "ruleCreateDataBaseGenericConnection"


    // $ANTLR start "entryRuleDropDatabaseConnection"
    // InternalCQLParser.g:3338:1: entryRuleDropDatabaseConnection returns [EObject current=null] : iv_ruleDropDatabaseConnection= ruleDropDatabaseConnection EOF ;
    public final EObject entryRuleDropDatabaseConnection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDropDatabaseConnection = null;


        try {
            // InternalCQLParser.g:3338:63: (iv_ruleDropDatabaseConnection= ruleDropDatabaseConnection EOF )
            // InternalCQLParser.g:3339:2: iv_ruleDropDatabaseConnection= ruleDropDatabaseConnection EOF
            {
             newCompositeNode(grammarAccess.getDropDatabaseConnectionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDropDatabaseConnection=ruleDropDatabaseConnection();

            state._fsp--;

             current =iv_ruleDropDatabaseConnection; 
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
    // $ANTLR end "entryRuleDropDatabaseConnection"


    // $ANTLR start "ruleDropDatabaseConnection"
    // InternalCQLParser.g:3345:1: ruleDropDatabaseConnection returns [EObject current=null] : ( () otherlv_1= DROP otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) ) ;
    public final EObject ruleDropDatabaseConnection() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token lv_name_4_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:3351:2: ( ( () otherlv_1= DROP otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:3352:2: ( () otherlv_1= DROP otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) )
            {
            // InternalCQLParser.g:3352:2: ( () otherlv_1= DROP otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) )
            // InternalCQLParser.g:3353:3: () otherlv_1= DROP otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) )
            {
            // InternalCQLParser.g:3353:3: ()
            // InternalCQLParser.g:3354:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getDropDatabaseConnectionAccess().getDropDatabaseConnectionAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,DROP,FOLLOW_41); 

            			newLeafNode(otherlv_1, grammarAccess.getDropDatabaseConnectionAccess().getDROPKeyword_1());
            		
            otherlv_2=(Token)match(input,DATABASE,FOLLOW_46); 

            			newLeafNode(otherlv_2, grammarAccess.getDropDatabaseConnectionAccess().getDATABASEKeyword_2());
            		
            otherlv_3=(Token)match(input,CONNECTION,FOLLOW_14); 

            			newLeafNode(otherlv_3, grammarAccess.getDropDatabaseConnectionAccess().getCONNECTIONKeyword_3());
            		
            // InternalCQLParser.g:3372:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCQLParser.g:3373:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCQLParser.g:3373:4: (lv_name_4_0= RULE_ID )
            // InternalCQLParser.g:3374:5: lv_name_4_0= RULE_ID
            {
            lv_name_4_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(lv_name_4_0, grammarAccess.getDropDatabaseConnectionAccess().getNameIDTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDropDatabaseConnectionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_4_0,
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
    // $ANTLR end "ruleDropDatabaseConnection"


    // $ANTLR start "entryRuleContextStoreType"
    // InternalCQLParser.g:3394:1: entryRuleContextStoreType returns [EObject current=null] : iv_ruleContextStoreType= ruleContextStoreType EOF ;
    public final EObject entryRuleContextStoreType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleContextStoreType = null;


        try {
            // InternalCQLParser.g:3394:57: (iv_ruleContextStoreType= ruleContextStoreType EOF )
            // InternalCQLParser.g:3395:2: iv_ruleContextStoreType= ruleContextStoreType EOF
            {
             newCompositeNode(grammarAccess.getContextStoreTypeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleContextStoreType=ruleContextStoreType();

            state._fsp--;

             current =iv_ruleContextStoreType; 
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
    // $ANTLR end "entryRuleContextStoreType"


    // $ANTLR start "ruleContextStoreType"
    // InternalCQLParser.g:3401:1: ruleContextStoreType returns [EObject current=null] : ( ( (lv_type_0_0= SINGLE ) ) | ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? ) ) ;
    public final EObject ruleContextStoreType() throws RecognitionException {
        EObject current = null;

        Token lv_type_0_0=null;
        Token lv_type_1_0=null;
        Token lv_size_2_0=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token lv_partition_5_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:3407:2: ( ( ( (lv_type_0_0= SINGLE ) ) | ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? ) ) )
            // InternalCQLParser.g:3408:2: ( ( (lv_type_0_0= SINGLE ) ) | ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? ) )
            {
            // InternalCQLParser.g:3408:2: ( ( (lv_type_0_0= SINGLE ) ) | ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? ) )
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==SINGLE) ) {
                alt51=1;
            }
            else if ( (LA51_0==MULTI) ) {
                alt51=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 51, 0, input);

                throw nvae;
            }
            switch (alt51) {
                case 1 :
                    // InternalCQLParser.g:3409:3: ( (lv_type_0_0= SINGLE ) )
                    {
                    // InternalCQLParser.g:3409:3: ( (lv_type_0_0= SINGLE ) )
                    // InternalCQLParser.g:3410:4: (lv_type_0_0= SINGLE )
                    {
                    // InternalCQLParser.g:3410:4: (lv_type_0_0= SINGLE )
                    // InternalCQLParser.g:3411:5: lv_type_0_0= SINGLE
                    {
                    lv_type_0_0=(Token)match(input,SINGLE,FOLLOW_2); 

                    					newLeafNode(lv_type_0_0, grammarAccess.getContextStoreTypeAccess().getTypeSINGLEKeyword_0_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getContextStoreTypeRule());
                    					}
                    					setWithLastConsumed(current, "type", lv_type_0_0, "SINGLE");
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:3424:3: ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? )
                    {
                    // InternalCQLParser.g:3424:3: ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? )
                    // InternalCQLParser.g:3425:4: ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )?
                    {
                    // InternalCQLParser.g:3425:4: ( (lv_type_1_0= MULTI ) )
                    // InternalCQLParser.g:3426:5: (lv_type_1_0= MULTI )
                    {
                    // InternalCQLParser.g:3426:5: (lv_type_1_0= MULTI )
                    // InternalCQLParser.g:3427:6: lv_type_1_0= MULTI
                    {
                    lv_type_1_0=(Token)match(input,MULTI,FOLLOW_39); 

                    						newLeafNode(lv_type_1_0, grammarAccess.getContextStoreTypeAccess().getTypeMULTIKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getContextStoreTypeRule());
                    						}
                    						setWithLastConsumed(current, "type", lv_type_1_0, "MULTI");
                    					

                    }


                    }

                    // InternalCQLParser.g:3439:4: ( (lv_size_2_0= RULE_INT ) )
                    // InternalCQLParser.g:3440:5: (lv_size_2_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3440:5: (lv_size_2_0= RULE_INT )
                    // InternalCQLParser.g:3441:6: lv_size_2_0= RULE_INT
                    {
                    lv_size_2_0=(Token)match(input,RULE_INT,FOLLOW_54); 

                    						newLeafNode(lv_size_2_0, grammarAccess.getContextStoreTypeAccess().getSizeINTTerminalRuleCall_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getContextStoreTypeRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"size",
                    							lv_size_2_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }

                    // InternalCQLParser.g:3457:4: (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )?
                    int alt50=2;
                    int LA50_0 = input.LA(1);

                    if ( (LA50_0==PARTITION) ) {
                        alt50=1;
                    }
                    switch (alt50) {
                        case 1 :
                            // InternalCQLParser.g:3458:5: otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) )
                            {
                            otherlv_3=(Token)match(input,PARTITION,FOLLOW_13); 

                            					newLeafNode(otherlv_3, grammarAccess.getContextStoreTypeAccess().getPARTITIONKeyword_1_2_0());
                            				
                            otherlv_4=(Token)match(input,BY,FOLLOW_39); 

                            					newLeafNode(otherlv_4, grammarAccess.getContextStoreTypeAccess().getBYKeyword_1_2_1());
                            				
                            // InternalCQLParser.g:3466:5: ( (lv_partition_5_0= RULE_INT ) )
                            // InternalCQLParser.g:3467:6: (lv_partition_5_0= RULE_INT )
                            {
                            // InternalCQLParser.g:3467:6: (lv_partition_5_0= RULE_INT )
                            // InternalCQLParser.g:3468:7: lv_partition_5_0= RULE_INT
                            {
                            lv_partition_5_0=(Token)match(input,RULE_INT,FOLLOW_2); 

                            							newLeafNode(lv_partition_5_0, grammarAccess.getContextStoreTypeAccess().getPartitionINTTerminalRuleCall_1_2_2_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getContextStoreTypeRule());
                            							}
                            							setWithLastConsumed(
                            								current,
                            								"partition",
                            								lv_partition_5_0,
                            								"org.eclipse.xtext.common.Terminals.INT");
                            						

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
    // $ANTLR end "ruleContextStoreType"


    // $ANTLR start "entryRuleCreateContextStore"
    // InternalCQLParser.g:3490:1: entryRuleCreateContextStore returns [EObject current=null] : iv_ruleCreateContextStore= ruleCreateContextStore EOF ;
    public final EObject entryRuleCreateContextStore() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateContextStore = null;


        try {
            // InternalCQLParser.g:3490:59: (iv_ruleCreateContextStore= ruleCreateContextStore EOF )
            // InternalCQLParser.g:3491:2: iv_ruleCreateContextStore= ruleCreateContextStore EOF
            {
             newCompositeNode(grammarAccess.getCreateContextStoreRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateContextStore=ruleCreateContextStore();

            state._fsp--;

             current =iv_ruleCreateContextStore; 
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
    // $ANTLR end "entryRuleCreateContextStore"


    // $ANTLR start "ruleCreateContextStore"
    // InternalCQLParser.g:3497:1: ruleCreateContextStore returns [EObject current=null] : ( () otherlv_1= CREATE otherlv_2= CONTEXT otherlv_3= STORE ( (lv_attributes_4_0= ruleSchemaDefinition ) ) otherlv_5= AS ( (lv_contextType_6_0= ruleContextStoreType ) ) ) ;
    public final EObject ruleCreateContextStore() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_attributes_4_0 = null;

        EObject lv_contextType_6_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3503:2: ( ( () otherlv_1= CREATE otherlv_2= CONTEXT otherlv_3= STORE ( (lv_attributes_4_0= ruleSchemaDefinition ) ) otherlv_5= AS ( (lv_contextType_6_0= ruleContextStoreType ) ) ) )
            // InternalCQLParser.g:3504:2: ( () otherlv_1= CREATE otherlv_2= CONTEXT otherlv_3= STORE ( (lv_attributes_4_0= ruleSchemaDefinition ) ) otherlv_5= AS ( (lv_contextType_6_0= ruleContextStoreType ) ) )
            {
            // InternalCQLParser.g:3504:2: ( () otherlv_1= CREATE otherlv_2= CONTEXT otherlv_3= STORE ( (lv_attributes_4_0= ruleSchemaDefinition ) ) otherlv_5= AS ( (lv_contextType_6_0= ruleContextStoreType ) ) )
            // InternalCQLParser.g:3505:3: () otherlv_1= CREATE otherlv_2= CONTEXT otherlv_3= STORE ( (lv_attributes_4_0= ruleSchemaDefinition ) ) otherlv_5= AS ( (lv_contextType_6_0= ruleContextStoreType ) )
            {
            // InternalCQLParser.g:3505:3: ()
            // InternalCQLParser.g:3506:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getCreateContextStoreAccess().getCreateContextStoreAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,CREATE,FOLLOW_55); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateContextStoreAccess().getCREATEKeyword_1());
            		
            otherlv_2=(Token)match(input,CONTEXT,FOLLOW_56); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateContextStoreAccess().getCONTEXTKeyword_2());
            		
            otherlv_3=(Token)match(input,STORE,FOLLOW_14); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateContextStoreAccess().getSTOREKeyword_3());
            		
            // InternalCQLParser.g:3524:3: ( (lv_attributes_4_0= ruleSchemaDefinition ) )
            // InternalCQLParser.g:3525:4: (lv_attributes_4_0= ruleSchemaDefinition )
            {
            // InternalCQLParser.g:3525:4: (lv_attributes_4_0= ruleSchemaDefinition )
            // InternalCQLParser.g:3526:5: lv_attributes_4_0= ruleSchemaDefinition
            {

            					newCompositeNode(grammarAccess.getCreateContextStoreAccess().getAttributesSchemaDefinitionParserRuleCall_4_0());
            				
            pushFollow(FOLLOW_21);
            lv_attributes_4_0=ruleSchemaDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateContextStoreRule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_4_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SchemaDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_5=(Token)match(input,AS,FOLLOW_57); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateContextStoreAccess().getASKeyword_5());
            		
            // InternalCQLParser.g:3547:3: ( (lv_contextType_6_0= ruleContextStoreType ) )
            // InternalCQLParser.g:3548:4: (lv_contextType_6_0= ruleContextStoreType )
            {
            // InternalCQLParser.g:3548:4: (lv_contextType_6_0= ruleContextStoreType )
            // InternalCQLParser.g:3549:5: lv_contextType_6_0= ruleContextStoreType
            {

            					newCompositeNode(grammarAccess.getCreateContextStoreAccess().getContextTypeContextStoreTypeParserRuleCall_6_0());
            				
            pushFollow(FOLLOW_2);
            lv_contextType_6_0=ruleContextStoreType();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateContextStoreRule());
            					}
            					set(
            						current,
            						"contextType",
            						lv_contextType_6_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ContextStoreType");
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
    // $ANTLR end "ruleCreateContextStore"


    // $ANTLR start "entryRuleDropContextStore"
    // InternalCQLParser.g:3570:1: entryRuleDropContextStore returns [EObject current=null] : iv_ruleDropContextStore= ruleDropContextStore EOF ;
    public final EObject entryRuleDropContextStore() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDropContextStore = null;


        try {
            // InternalCQLParser.g:3570:57: (iv_ruleDropContextStore= ruleDropContextStore EOF )
            // InternalCQLParser.g:3571:2: iv_ruleDropContextStore= ruleDropContextStore EOF
            {
             newCompositeNode(grammarAccess.getDropContextStoreRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDropContextStore=ruleDropContextStore();

            state._fsp--;

             current =iv_ruleDropContextStore; 
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
    // $ANTLR end "entryRuleDropContextStore"


    // $ANTLR start "ruleDropContextStore"
    // InternalCQLParser.g:3577:1: ruleDropContextStore returns [EObject current=null] : ( () otherlv_1= DROP otherlv_2= CONTEXT otherlv_3= STORE ( (lv_name_4_0= RULE_ID ) ) (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )? ) ;
    public final EObject ruleDropContextStore() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token lv_name_4_0=null;
        Token otherlv_5=null;
        Token lv_exists_6_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:3583:2: ( ( () otherlv_1= DROP otherlv_2= CONTEXT otherlv_3= STORE ( (lv_name_4_0= RULE_ID ) ) (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )? ) )
            // InternalCQLParser.g:3584:2: ( () otherlv_1= DROP otherlv_2= CONTEXT otherlv_3= STORE ( (lv_name_4_0= RULE_ID ) ) (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )? )
            {
            // InternalCQLParser.g:3584:2: ( () otherlv_1= DROP otherlv_2= CONTEXT otherlv_3= STORE ( (lv_name_4_0= RULE_ID ) ) (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )? )
            // InternalCQLParser.g:3585:3: () otherlv_1= DROP otherlv_2= CONTEXT otherlv_3= STORE ( (lv_name_4_0= RULE_ID ) ) (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )?
            {
            // InternalCQLParser.g:3585:3: ()
            // InternalCQLParser.g:3586:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getDropContextStoreAccess().getDropContextStoreAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,DROP,FOLLOW_55); 

            			newLeafNode(otherlv_1, grammarAccess.getDropContextStoreAccess().getDROPKeyword_1());
            		
            otherlv_2=(Token)match(input,CONTEXT,FOLLOW_56); 

            			newLeafNode(otherlv_2, grammarAccess.getDropContextStoreAccess().getCONTEXTKeyword_2());
            		
            otherlv_3=(Token)match(input,STORE,FOLLOW_14); 

            			newLeafNode(otherlv_3, grammarAccess.getDropContextStoreAccess().getSTOREKeyword_3());
            		
            // InternalCQLParser.g:3604:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCQLParser.g:3605:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCQLParser.g:3605:4: (lv_name_4_0= RULE_ID )
            // InternalCQLParser.g:3606:5: lv_name_4_0= RULE_ID
            {
            lv_name_4_0=(Token)match(input,RULE_ID,FOLLOW_58); 

            					newLeafNode(lv_name_4_0, grammarAccess.getDropContextStoreAccess().getNameIDTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDropContextStoreRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_4_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:3622:3: (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )?
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( (LA52_0==IF) ) {
                alt52=1;
            }
            switch (alt52) {
                case 1 :
                    // InternalCQLParser.g:3623:4: otherlv_5= IF ( (lv_exists_6_0= EXISTS ) )
                    {
                    otherlv_5=(Token)match(input,IF,FOLLOW_59); 

                    				newLeafNode(otherlv_5, grammarAccess.getDropContextStoreAccess().getIFKeyword_5_0());
                    			
                    // InternalCQLParser.g:3627:4: ( (lv_exists_6_0= EXISTS ) )
                    // InternalCQLParser.g:3628:5: (lv_exists_6_0= EXISTS )
                    {
                    // InternalCQLParser.g:3628:5: (lv_exists_6_0= EXISTS )
                    // InternalCQLParser.g:3629:6: lv_exists_6_0= EXISTS
                    {
                    lv_exists_6_0=(Token)match(input,EXISTS,FOLLOW_2); 

                    						newLeafNode(lv_exists_6_0, grammarAccess.getDropContextStoreAccess().getExistsEXISTSKeyword_5_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropContextStoreRule());
                    						}
                    						setWithLastConsumed(current, "exists", lv_exists_6_0, "EXISTS");
                    					

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
    // $ANTLR end "ruleDropContextStore"


    // $ANTLR start "entryRuleStreamTo"
    // InternalCQLParser.g:3646:1: entryRuleStreamTo returns [EObject current=null] : iv_ruleStreamTo= ruleStreamTo EOF ;
    public final EObject entryRuleStreamTo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStreamTo = null;


        try {
            // InternalCQLParser.g:3646:49: (iv_ruleStreamTo= ruleStreamTo EOF )
            // InternalCQLParser.g:3647:2: iv_ruleStreamTo= ruleStreamTo EOF
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
    // InternalCQLParser.g:3653:1: ruleStreamTo returns [EObject current=null] : ( () otherlv_1= STREAM otherlv_2= TO ( (lv_name_3_0= RULE_ID ) ) ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) ) ) ;
    public final EObject ruleStreamTo() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token lv_name_3_0=null;
        Token lv_inputname_5_0=null;
        EObject lv_statement_4_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3659:2: ( ( () otherlv_1= STREAM otherlv_2= TO ( (lv_name_3_0= RULE_ID ) ) ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) ) ) )
            // InternalCQLParser.g:3660:2: ( () otherlv_1= STREAM otherlv_2= TO ( (lv_name_3_0= RULE_ID ) ) ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) ) )
            {
            // InternalCQLParser.g:3660:2: ( () otherlv_1= STREAM otherlv_2= TO ( (lv_name_3_0= RULE_ID ) ) ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:3661:3: () otherlv_1= STREAM otherlv_2= TO ( (lv_name_3_0= RULE_ID ) ) ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) )
            {
            // InternalCQLParser.g:3661:3: ()
            // InternalCQLParser.g:3662:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getStreamToAccess().getStreamToAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,STREAM,FOLLOW_52); 

            			newLeafNode(otherlv_1, grammarAccess.getStreamToAccess().getSTREAMKeyword_1());
            		
            otherlv_2=(Token)match(input,TO,FOLLOW_14); 

            			newLeafNode(otherlv_2, grammarAccess.getStreamToAccess().getTOKeyword_2());
            		
            // InternalCQLParser.g:3676:3: ( (lv_name_3_0= RULE_ID ) )
            // InternalCQLParser.g:3677:4: (lv_name_3_0= RULE_ID )
            {
            // InternalCQLParser.g:3677:4: (lv_name_3_0= RULE_ID )
            // InternalCQLParser.g:3678:5: lv_name_3_0= RULE_ID
            {
            lv_name_3_0=(Token)match(input,RULE_ID,FOLLOW_60); 

            					newLeafNode(lv_name_3_0, grammarAccess.getStreamToAccess().getNameIDTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getStreamToRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:3694:3: ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) )
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==SELECT) ) {
                alt53=1;
            }
            else if ( (LA53_0==RULE_ID) ) {
                alt53=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 53, 0, input);

                throw nvae;
            }
            switch (alt53) {
                case 1 :
                    // InternalCQLParser.g:3695:4: ( (lv_statement_4_0= ruleInnerSelect2 ) )
                    {
                    // InternalCQLParser.g:3695:4: ( (lv_statement_4_0= ruleInnerSelect2 ) )
                    // InternalCQLParser.g:3696:5: (lv_statement_4_0= ruleInnerSelect2 )
                    {
                    // InternalCQLParser.g:3696:5: (lv_statement_4_0= ruleInnerSelect2 )
                    // InternalCQLParser.g:3697:6: lv_statement_4_0= ruleInnerSelect2
                    {

                    						newCompositeNode(grammarAccess.getStreamToAccess().getStatementInnerSelect2ParserRuleCall_4_0_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_statement_4_0=ruleInnerSelect2();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStreamToRule());
                    						}
                    						set(
                    							current,
                    							"statement",
                    							lv_statement_4_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.InnerSelect2");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:3715:4: ( (lv_inputname_5_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:3715:4: ( (lv_inputname_5_0= RULE_ID ) )
                    // InternalCQLParser.g:3716:5: (lv_inputname_5_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3716:5: (lv_inputname_5_0= RULE_ID )
                    // InternalCQLParser.g:3717:6: lv_inputname_5_0= RULE_ID
                    {
                    lv_inputname_5_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    						newLeafNode(lv_inputname_5_0, grammarAccess.getStreamToAccess().getInputnameIDTerminalRuleCall_4_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getStreamToRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"inputname",
                    							lv_inputname_5_0,
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


    // $ANTLR start "entryRuleDropStream"
    // InternalCQLParser.g:3738:1: entryRuleDropStream returns [EObject current=null] : iv_ruleDropStream= ruleDropStream EOF ;
    public final EObject entryRuleDropStream() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDropStream = null;


        try {
            // InternalCQLParser.g:3738:51: (iv_ruleDropStream= ruleDropStream EOF )
            // InternalCQLParser.g:3739:2: iv_ruleDropStream= ruleDropStream EOF
            {
             newCompositeNode(grammarAccess.getDropStreamRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDropStream=ruleDropStream();

            state._fsp--;

             current =iv_ruleDropStream; 
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
    // $ANTLR end "entryRuleDropStream"


    // $ANTLR start "ruleDropStream"
    // InternalCQLParser.g:3745:1: ruleDropStream returns [EObject current=null] : ( () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )? ) ;
    public final EObject ruleDropStream() throws RecognitionException {
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
            // InternalCQLParser.g:3751:2: ( ( () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )? ) )
            // InternalCQLParser.g:3752:2: ( () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )? )
            {
            // InternalCQLParser.g:3752:2: ( () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )? )
            // InternalCQLParser.g:3753:3: () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )?
            {
            // InternalCQLParser.g:3753:3: ()
            // InternalCQLParser.g:3754:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getDropStreamAccess().getDropStreamAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,DROP,FOLLOW_35); 

            			newLeafNode(otherlv_1, grammarAccess.getDropStreamAccess().getDROPKeyword_1());
            		
            // InternalCQLParser.g:3764:3: ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) )
            // InternalCQLParser.g:3765:4: ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) )
            {
            // InternalCQLParser.g:3765:4: ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) )
            // InternalCQLParser.g:3766:5: (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW )
            {
            // InternalCQLParser.g:3766:5: (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW )
            int alt54=3;
            switch ( input.LA(1) ) {
            case SINK:
                {
                alt54=1;
                }
                break;
            case STREAM:
                {
                alt54=2;
                }
                break;
            case VIEW:
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
                    // InternalCQLParser.g:3767:6: lv_name_2_1= SINK
                    {
                    lv_name_2_1=(Token)match(input,SINK,FOLLOW_14); 

                    						newLeafNode(lv_name_2_1, grammarAccess.getDropStreamAccess().getNameSINKKeyword_2_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropStreamRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_2_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:3778:6: lv_name_2_2= STREAM
                    {
                    lv_name_2_2=(Token)match(input,STREAM,FOLLOW_14); 

                    						newLeafNode(lv_name_2_2, grammarAccess.getDropStreamAccess().getNameSTREAMKeyword_2_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropStreamRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_2_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:3789:6: lv_name_2_3= VIEW
                    {
                    lv_name_2_3=(Token)match(input,VIEW,FOLLOW_14); 

                    						newLeafNode(lv_name_2_3, grammarAccess.getDropStreamAccess().getNameVIEWKeyword_2_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropStreamRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_2_3, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQLParser.g:3802:3: ( (lv_stream_3_0= RULE_ID ) )
            // InternalCQLParser.g:3803:4: (lv_stream_3_0= RULE_ID )
            {
            // InternalCQLParser.g:3803:4: (lv_stream_3_0= RULE_ID )
            // InternalCQLParser.g:3804:5: lv_stream_3_0= RULE_ID
            {
            lv_stream_3_0=(Token)match(input,RULE_ID,FOLLOW_58); 

            					newLeafNode(lv_stream_3_0, grammarAccess.getDropStreamAccess().getStreamIDTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDropStreamRule());
            					}
            					setWithLastConsumed(
            						current,
            						"stream",
            						lv_stream_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:3820:3: ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )?
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==IF) ) {
                alt55=1;
            }
            switch (alt55) {
                case 1 :
                    // InternalCQLParser.g:3821:4: ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS
                    {
                    // InternalCQLParser.g:3821:4: ( (lv_exists_4_0= IF ) )
                    // InternalCQLParser.g:3822:5: (lv_exists_4_0= IF )
                    {
                    // InternalCQLParser.g:3822:5: (lv_exists_4_0= IF )
                    // InternalCQLParser.g:3823:6: lv_exists_4_0= IF
                    {
                    lv_exists_4_0=(Token)match(input,IF,FOLLOW_59); 

                    						newLeafNode(lv_exists_4_0, grammarAccess.getDropStreamAccess().getExistsIFKeyword_4_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropStreamRule());
                    						}
                    						setWithLastConsumed(current, "exists", lv_exists_4_0, "IF");
                    					

                    }


                    }

                    otherlv_5=(Token)match(input,EXISTS,FOLLOW_2); 

                    				newLeafNode(otherlv_5, grammarAccess.getDropStreamAccess().getEXISTSKeyword_4_1());
                    			

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
    // $ANTLR end "ruleDropStream"


    // $ANTLR start "entryRuleUserManagement"
    // InternalCQLParser.g:3844:1: entryRuleUserManagement returns [EObject current=null] : iv_ruleUserManagement= ruleUserManagement EOF ;
    public final EObject entryRuleUserManagement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleUserManagement = null;


        try {
            // InternalCQLParser.g:3844:55: (iv_ruleUserManagement= ruleUserManagement EOF )
            // InternalCQLParser.g:3845:2: iv_ruleUserManagement= ruleUserManagement EOF
            {
             newCompositeNode(grammarAccess.getUserManagementRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleUserManagement=ruleUserManagement();

            state._fsp--;

             current =iv_ruleUserManagement; 
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
    // $ANTLR end "entryRuleUserManagement"


    // $ANTLR start "ruleUserManagement"
    // InternalCQLParser.g:3851:1: ruleUserManagement returns [EObject current=null] : ( () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )? ) ;
    public final EObject ruleUserManagement() throws RecognitionException {
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
            // InternalCQLParser.g:3857:2: ( ( () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )? ) )
            // InternalCQLParser.g:3858:2: ( () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )? )
            {
            // InternalCQLParser.g:3858:2: ( () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )? )
            // InternalCQLParser.g:3859:3: () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )?
            {
            // InternalCQLParser.g:3859:3: ()
            // InternalCQLParser.g:3860:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getUserManagementAccess().getUserManagementAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:3866:3: ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) )
            // InternalCQLParser.g:3867:4: ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) )
            {
            // InternalCQLParser.g:3867:4: ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) )
            // InternalCQLParser.g:3868:5: (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP )
            {
            // InternalCQLParser.g:3868:5: (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP )
            int alt56=3;
            switch ( input.LA(1) ) {
            case CREATE:
                {
                alt56=1;
                }
                break;
            case ALTER:
                {
                alt56=2;
                }
                break;
            case DROP:
                {
                alt56=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;
            }

            switch (alt56) {
                case 1 :
                    // InternalCQLParser.g:3869:6: lv_name_1_1= CREATE
                    {
                    lv_name_1_1=(Token)match(input,CREATE,FOLLOW_61); 

                    						newLeafNode(lv_name_1_1, grammarAccess.getUserManagementAccess().getNameCREATEKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:3880:6: lv_name_1_2= ALTER
                    {
                    lv_name_1_2=(Token)match(input,ALTER,FOLLOW_61); 

                    						newLeafNode(lv_name_1_2, grammarAccess.getUserManagementAccess().getNameALTERKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:3891:6: lv_name_1_3= DROP
                    {
                    lv_name_1_3=(Token)match(input,DROP,FOLLOW_61); 

                    						newLeafNode(lv_name_1_3, grammarAccess.getUserManagementAccess().getNameDROPKeyword_1_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_3, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQLParser.g:3904:3: ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) )
            // InternalCQLParser.g:3905:4: ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) )
            {
            // InternalCQLParser.g:3905:4: ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) )
            // InternalCQLParser.g:3906:5: (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT )
            {
            // InternalCQLParser.g:3906:5: (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT )
            int alt57=3;
            switch ( input.LA(1) ) {
            case USER:
                {
                alt57=1;
                }
                break;
            case ROLE:
                {
                alt57=2;
                }
                break;
            case TENANT:
                {
                alt57=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 57, 0, input);

                throw nvae;
            }

            switch (alt57) {
                case 1 :
                    // InternalCQLParser.g:3907:6: lv_subject_2_1= USER
                    {
                    lv_subject_2_1=(Token)match(input,USER,FOLLOW_14); 

                    						newLeafNode(lv_subject_2_1, grammarAccess.getUserManagementAccess().getSubjectUSERKeyword_2_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserManagementRule());
                    						}
                    						setWithLastConsumed(current, "subject", lv_subject_2_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:3918:6: lv_subject_2_2= ROLE
                    {
                    lv_subject_2_2=(Token)match(input,ROLE,FOLLOW_14); 

                    						newLeafNode(lv_subject_2_2, grammarAccess.getUserManagementAccess().getSubjectROLEKeyword_2_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserManagementRule());
                    						}
                    						setWithLastConsumed(current, "subject", lv_subject_2_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:3929:6: lv_subject_2_3= TENANT
                    {
                    lv_subject_2_3=(Token)match(input,TENANT,FOLLOW_14); 

                    						newLeafNode(lv_subject_2_3, grammarAccess.getUserManagementAccess().getSubjectTENANTKeyword_2_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserManagementRule());
                    						}
                    						setWithLastConsumed(current, "subject", lv_subject_2_3, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQLParser.g:3942:3: ( (lv_subjectName_3_0= RULE_ID ) )
            // InternalCQLParser.g:3943:4: (lv_subjectName_3_0= RULE_ID )
            {
            // InternalCQLParser.g:3943:4: (lv_subjectName_3_0= RULE_ID )
            // InternalCQLParser.g:3944:5: lv_subjectName_3_0= RULE_ID
            {
            lv_subjectName_3_0=(Token)match(input,RULE_ID,FOLLOW_62); 

            					newLeafNode(lv_subjectName_3_0, grammarAccess.getUserManagementAccess().getSubjectNameIDTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getUserManagementRule());
            					}
            					setWithLastConsumed(
            						current,
            						"subjectName",
            						lv_subjectName_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:3960:3: (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )?
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==IDENTIFIED) ) {
                alt58=1;
            }
            switch (alt58) {
                case 1 :
                    // InternalCQLParser.g:3961:4: otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) )
                    {
                    otherlv_4=(Token)match(input,IDENTIFIED,FOLLOW_13); 

                    				newLeafNode(otherlv_4, grammarAccess.getUserManagementAccess().getIDENTIFIEDKeyword_4_0());
                    			
                    otherlv_5=(Token)match(input,BY,FOLLOW_28); 

                    				newLeafNode(otherlv_5, grammarAccess.getUserManagementAccess().getBYKeyword_4_1());
                    			
                    // InternalCQLParser.g:3969:4: ( (lv_password_6_0= RULE_STRING ) )
                    // InternalCQLParser.g:3970:5: (lv_password_6_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:3970:5: (lv_password_6_0= RULE_STRING )
                    // InternalCQLParser.g:3971:6: lv_password_6_0= RULE_STRING
                    {
                    lv_password_6_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

                    						newLeafNode(lv_password_6_0, grammarAccess.getUserManagementAccess().getPasswordSTRINGTerminalRuleCall_4_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserManagementRule());
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
    // $ANTLR end "ruleUserManagement"


    // $ANTLR start "entryRuleRightsManagement"
    // InternalCQLParser.g:3992:1: entryRuleRightsManagement returns [EObject current=null] : iv_ruleRightsManagement= ruleRightsManagement EOF ;
    public final EObject entryRuleRightsManagement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRightsManagement = null;


        try {
            // InternalCQLParser.g:3992:57: (iv_ruleRightsManagement= ruleRightsManagement EOF )
            // InternalCQLParser.g:3993:2: iv_ruleRightsManagement= ruleRightsManagement EOF
            {
             newCompositeNode(grammarAccess.getRightsManagementRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRightsManagement=ruleRightsManagement();

            state._fsp--;

             current =iv_ruleRightsManagement; 
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
    // $ANTLR end "entryRuleRightsManagement"


    // $ANTLR start "ruleRightsManagement"
    // InternalCQLParser.g:3999:1: ruleRightsManagement returns [EObject current=null] : ( ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) ) | ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) ) ) ;
    public final EObject ruleRightsManagement() throws RecognitionException {
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
            // InternalCQLParser.g:4005:2: ( ( ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) ) | ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) ) ) )
            // InternalCQLParser.g:4006:2: ( ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) ) | ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) ) )
            {
            // InternalCQLParser.g:4006:2: ( ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) ) | ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) ) )
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( (LA65_0==GRANT) ) {
                alt65=1;
            }
            else if ( (LA65_0==REVOKE) ) {
                alt65=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 65, 0, input);

                throw nvae;
            }
            switch (alt65) {
                case 1 :
                    // InternalCQLParser.g:4007:3: ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) )
                    {
                    // InternalCQLParser.g:4007:3: ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) )
                    // InternalCQLParser.g:4008:4: () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:4008:4: ()
                    // InternalCQLParser.g:4009:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getRightsManagementAccess().getRightsManagementAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4015:4: ( (lv_name_1_0= GRANT ) )
                    // InternalCQLParser.g:4016:5: (lv_name_1_0= GRANT )
                    {
                    // InternalCQLParser.g:4016:5: (lv_name_1_0= GRANT )
                    // InternalCQLParser.g:4017:6: lv_name_1_0= GRANT
                    {
                    lv_name_1_0=(Token)match(input,GRANT,FOLLOW_14); 

                    						newLeafNode(lv_name_1_0, grammarAccess.getRightsManagementAccess().getNameGRANTKeyword_0_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_0, "GRANT");
                    					

                    }


                    }

                    // InternalCQLParser.g:4029:4: ( (lv_operations_2_0= RULE_ID ) )
                    // InternalCQLParser.g:4030:5: (lv_operations_2_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4030:5: (lv_operations_2_0= RULE_ID )
                    // InternalCQLParser.g:4031:6: lv_operations_2_0= RULE_ID
                    {
                    lv_operations_2_0=(Token)match(input,RULE_ID,FOLLOW_63); 

                    						newLeafNode(lv_operations_2_0, grammarAccess.getRightsManagementAccess().getOperationsIDTerminalRuleCall_0_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsManagementRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"operations",
                    							lv_operations_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }

                    // InternalCQLParser.g:4047:4: (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )*
                    loop59:
                    do {
                        int alt59=2;
                        int LA59_0 = input.LA(1);

                        if ( (LA59_0==Comma) ) {
                            alt59=1;
                        }


                        switch (alt59) {
                    	case 1 :
                    	    // InternalCQLParser.g:4048:5: otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) )
                    	    {
                    	    otherlv_3=(Token)match(input,Comma,FOLLOW_14); 

                    	    					newLeafNode(otherlv_3, grammarAccess.getRightsManagementAccess().getCommaKeyword_0_3_0());
                    	    				
                    	    // InternalCQLParser.g:4052:5: ( (lv_operations_4_0= RULE_ID ) )
                    	    // InternalCQLParser.g:4053:6: (lv_operations_4_0= RULE_ID )
                    	    {
                    	    // InternalCQLParser.g:4053:6: (lv_operations_4_0= RULE_ID )
                    	    // InternalCQLParser.g:4054:7: lv_operations_4_0= RULE_ID
                    	    {
                    	    lv_operations_4_0=(Token)match(input,RULE_ID,FOLLOW_63); 

                    	    							newLeafNode(lv_operations_4_0, grammarAccess.getRightsManagementAccess().getOperationsIDTerminalRuleCall_0_3_1_0());
                    	    						

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getRightsManagementRule());
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
                    	    break loop59;
                        }
                    } while (true);

                    // InternalCQLParser.g:4071:4: (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )?
                    int alt61=2;
                    int LA61_0 = input.LA(1);

                    if ( (LA61_0==ON) ) {
                        alt61=1;
                    }
                    switch (alt61) {
                        case 1 :
                            // InternalCQLParser.g:4072:5: otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )*
                            {
                            otherlv_5=(Token)match(input,ON,FOLLOW_14); 

                            					newLeafNode(otherlv_5, grammarAccess.getRightsManagementAccess().getONKeyword_0_4_0());
                            				
                            // InternalCQLParser.g:4076:5: ( (lv_operations2_6_0= RULE_ID ) )
                            // InternalCQLParser.g:4077:6: (lv_operations2_6_0= RULE_ID )
                            {
                            // InternalCQLParser.g:4077:6: (lv_operations2_6_0= RULE_ID )
                            // InternalCQLParser.g:4078:7: lv_operations2_6_0= RULE_ID
                            {
                            lv_operations2_6_0=(Token)match(input,RULE_ID,FOLLOW_64); 

                            							newLeafNode(lv_operations2_6_0, grammarAccess.getRightsManagementAccess().getOperations2IDTerminalRuleCall_0_4_1_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getRightsManagementRule());
                            							}
                            							addWithLastConsumed(
                            								current,
                            								"operations2",
                            								lv_operations2_6_0,
                            								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                            						

                            }


                            }

                            // InternalCQLParser.g:4094:5: (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )*
                            loop60:
                            do {
                                int alt60=2;
                                int LA60_0 = input.LA(1);

                                if ( (LA60_0==Comma) ) {
                                    alt60=1;
                                }


                                switch (alt60) {
                            	case 1 :
                            	    // InternalCQLParser.g:4095:6: otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) )
                            	    {
                            	    otherlv_7=(Token)match(input,Comma,FOLLOW_14); 

                            	    						newLeafNode(otherlv_7, grammarAccess.getRightsManagementAccess().getCommaKeyword_0_4_2_0());
                            	    					
                            	    // InternalCQLParser.g:4099:6: ( (lv_operations2_8_0= RULE_ID ) )
                            	    // InternalCQLParser.g:4100:7: (lv_operations2_8_0= RULE_ID )
                            	    {
                            	    // InternalCQLParser.g:4100:7: (lv_operations2_8_0= RULE_ID )
                            	    // InternalCQLParser.g:4101:8: lv_operations2_8_0= RULE_ID
                            	    {
                            	    lv_operations2_8_0=(Token)match(input,RULE_ID,FOLLOW_64); 

                            	    								newLeafNode(lv_operations2_8_0, grammarAccess.getRightsManagementAccess().getOperations2IDTerminalRuleCall_0_4_2_1_0());
                            	    							

                            	    								if (current==null) {
                            	    									current = createModelElement(grammarAccess.getRightsManagementRule());
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
                            	    break loop60;
                                }
                            } while (true);


                            }
                            break;

                    }

                    otherlv_9=(Token)match(input,TO,FOLLOW_14); 

                    				newLeafNode(otherlv_9, grammarAccess.getRightsManagementAccess().getTOKeyword_0_5());
                    			
                    // InternalCQLParser.g:4123:4: ( (lv_user_10_0= RULE_ID ) )
                    // InternalCQLParser.g:4124:5: (lv_user_10_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4124:5: (lv_user_10_0= RULE_ID )
                    // InternalCQLParser.g:4125:6: lv_user_10_0= RULE_ID
                    {
                    lv_user_10_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    						newLeafNode(lv_user_10_0, grammarAccess.getRightsManagementAccess().getUserIDTerminalRuleCall_0_6_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsManagementRule());
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
                    // InternalCQLParser.g:4143:3: ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) )
                    {
                    // InternalCQLParser.g:4143:3: ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) )
                    // InternalCQLParser.g:4144:4: () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:4144:4: ()
                    // InternalCQLParser.g:4145:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getRightsManagementAccess().getRightsManagementAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4151:4: ( (lv_name_12_0= REVOKE ) )
                    // InternalCQLParser.g:4152:5: (lv_name_12_0= REVOKE )
                    {
                    // InternalCQLParser.g:4152:5: (lv_name_12_0= REVOKE )
                    // InternalCQLParser.g:4153:6: lv_name_12_0= REVOKE
                    {
                    lv_name_12_0=(Token)match(input,REVOKE,FOLLOW_14); 

                    						newLeafNode(lv_name_12_0, grammarAccess.getRightsManagementAccess().getNameREVOKEKeyword_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_12_0, "REVOKE");
                    					

                    }


                    }

                    // InternalCQLParser.g:4165:4: ( (lv_operations_13_0= RULE_ID ) )
                    // InternalCQLParser.g:4166:5: (lv_operations_13_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4166:5: (lv_operations_13_0= RULE_ID )
                    // InternalCQLParser.g:4167:6: lv_operations_13_0= RULE_ID
                    {
                    lv_operations_13_0=(Token)match(input,RULE_ID,FOLLOW_65); 

                    						newLeafNode(lv_operations_13_0, grammarAccess.getRightsManagementAccess().getOperationsIDTerminalRuleCall_1_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsManagementRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"operations",
                    							lv_operations_13_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }

                    // InternalCQLParser.g:4183:4: (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )*
                    loop62:
                    do {
                        int alt62=2;
                        int LA62_0 = input.LA(1);

                        if ( (LA62_0==Comma) ) {
                            alt62=1;
                        }


                        switch (alt62) {
                    	case 1 :
                    	    // InternalCQLParser.g:4184:5: otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) )
                    	    {
                    	    otherlv_14=(Token)match(input,Comma,FOLLOW_14); 

                    	    					newLeafNode(otherlv_14, grammarAccess.getRightsManagementAccess().getCommaKeyword_1_3_0());
                    	    				
                    	    // InternalCQLParser.g:4188:5: ( (lv_operations_15_0= RULE_ID ) )
                    	    // InternalCQLParser.g:4189:6: (lv_operations_15_0= RULE_ID )
                    	    {
                    	    // InternalCQLParser.g:4189:6: (lv_operations_15_0= RULE_ID )
                    	    // InternalCQLParser.g:4190:7: lv_operations_15_0= RULE_ID
                    	    {
                    	    lv_operations_15_0=(Token)match(input,RULE_ID,FOLLOW_65); 

                    	    							newLeafNode(lv_operations_15_0, grammarAccess.getRightsManagementAccess().getOperationsIDTerminalRuleCall_1_3_1_0());
                    	    						

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getRightsManagementRule());
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
                    	    break loop62;
                        }
                    } while (true);

                    // InternalCQLParser.g:4207:4: (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )?
                    int alt64=2;
                    int LA64_0 = input.LA(1);

                    if ( (LA64_0==ON) ) {
                        alt64=1;
                    }
                    switch (alt64) {
                        case 1 :
                            // InternalCQLParser.g:4208:5: otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )*
                            {
                            otherlv_16=(Token)match(input,ON,FOLLOW_14); 

                            					newLeafNode(otherlv_16, grammarAccess.getRightsManagementAccess().getONKeyword_1_4_0());
                            				
                            // InternalCQLParser.g:4212:5: ( (lv_operations2_17_0= RULE_ID ) )
                            // InternalCQLParser.g:4213:6: (lv_operations2_17_0= RULE_ID )
                            {
                            // InternalCQLParser.g:4213:6: (lv_operations2_17_0= RULE_ID )
                            // InternalCQLParser.g:4214:7: lv_operations2_17_0= RULE_ID
                            {
                            lv_operations2_17_0=(Token)match(input,RULE_ID,FOLLOW_7); 

                            							newLeafNode(lv_operations2_17_0, grammarAccess.getRightsManagementAccess().getOperations2IDTerminalRuleCall_1_4_1_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getRightsManagementRule());
                            							}
                            							addWithLastConsumed(
                            								current,
                            								"operations2",
                            								lv_operations2_17_0,
                            								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                            						

                            }


                            }

                            // InternalCQLParser.g:4230:5: (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )*
                            loop63:
                            do {
                                int alt63=2;
                                int LA63_0 = input.LA(1);

                                if ( (LA63_0==Comma) ) {
                                    alt63=1;
                                }


                                switch (alt63) {
                            	case 1 :
                            	    // InternalCQLParser.g:4231:6: otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) )
                            	    {
                            	    otherlv_18=(Token)match(input,Comma,FOLLOW_14); 

                            	    						newLeafNode(otherlv_18, grammarAccess.getRightsManagementAccess().getCommaKeyword_1_4_2_0());
                            	    					
                            	    // InternalCQLParser.g:4235:6: ( (lv_operations2_19_0= RULE_ID ) )
                            	    // InternalCQLParser.g:4236:7: (lv_operations2_19_0= RULE_ID )
                            	    {
                            	    // InternalCQLParser.g:4236:7: (lv_operations2_19_0= RULE_ID )
                            	    // InternalCQLParser.g:4237:8: lv_operations2_19_0= RULE_ID
                            	    {
                            	    lv_operations2_19_0=(Token)match(input,RULE_ID,FOLLOW_7); 

                            	    								newLeafNode(lv_operations2_19_0, grammarAccess.getRightsManagementAccess().getOperations2IDTerminalRuleCall_1_4_2_1_0());
                            	    							

                            	    								if (current==null) {
                            	    									current = createModelElement(grammarAccess.getRightsManagementRule());
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
                            	    break loop63;
                                }
                            } while (true);


                            }
                            break;

                    }

                    otherlv_20=(Token)match(input,FROM,FOLLOW_14); 

                    				newLeafNode(otherlv_20, grammarAccess.getRightsManagementAccess().getFROMKeyword_1_5());
                    			
                    // InternalCQLParser.g:4259:4: ( (lv_user_21_0= RULE_ID ) )
                    // InternalCQLParser.g:4260:5: (lv_user_21_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4260:5: (lv_user_21_0= RULE_ID )
                    // InternalCQLParser.g:4261:6: lv_user_21_0= RULE_ID
                    {
                    lv_user_21_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    						newLeafNode(lv_user_21_0, grammarAccess.getRightsManagementAccess().getUserIDTerminalRuleCall_1_6_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsManagementRule());
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
    // $ANTLR end "ruleRightsManagement"


    // $ANTLR start "entryRuleRoleManagement"
    // InternalCQLParser.g:4282:1: entryRuleRoleManagement returns [EObject current=null] : iv_ruleRoleManagement= ruleRoleManagement EOF ;
    public final EObject entryRuleRoleManagement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRoleManagement = null;


        try {
            // InternalCQLParser.g:4282:55: (iv_ruleRoleManagement= ruleRoleManagement EOF )
            // InternalCQLParser.g:4283:2: iv_ruleRoleManagement= ruleRoleManagement EOF
            {
             newCompositeNode(grammarAccess.getRoleManagementRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRoleManagement=ruleRoleManagement();

            state._fsp--;

             current =iv_ruleRoleManagement; 
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
    // $ANTLR end "entryRuleRoleManagement"


    // $ANTLR start "ruleRoleManagement"
    // InternalCQLParser.g:4289:1: ruleRoleManagement returns [EObject current=null] : ( ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) ) | ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) ) ) ;
    public final EObject ruleRoleManagement() throws RecognitionException {
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
            // InternalCQLParser.g:4295:2: ( ( ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) ) | ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) ) ) )
            // InternalCQLParser.g:4296:2: ( ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) ) | ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) ) )
            {
            // InternalCQLParser.g:4296:2: ( ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) ) | ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) ) )
            int alt68=2;
            int LA68_0 = input.LA(1);

            if ( (LA68_0==GRANT) ) {
                alt68=1;
            }
            else if ( (LA68_0==REVOKE) ) {
                alt68=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 68, 0, input);

                throw nvae;
            }
            switch (alt68) {
                case 1 :
                    // InternalCQLParser.g:4297:3: ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) )
                    {
                    // InternalCQLParser.g:4297:3: ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) )
                    // InternalCQLParser.g:4298:4: () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:4298:4: ()
                    // InternalCQLParser.g:4299:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getRoleManagementAccess().getRoleManagementAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4305:4: ( (lv_name_1_0= GRANT ) )
                    // InternalCQLParser.g:4306:5: (lv_name_1_0= GRANT )
                    {
                    // InternalCQLParser.g:4306:5: (lv_name_1_0= GRANT )
                    // InternalCQLParser.g:4307:6: lv_name_1_0= GRANT
                    {
                    lv_name_1_0=(Token)match(input,GRANT,FOLLOW_66); 

                    						newLeafNode(lv_name_1_0, grammarAccess.getRoleManagementAccess().getNameGRANTKeyword_0_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRoleManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_0, "GRANT");
                    					

                    }


                    }

                    otherlv_2=(Token)match(input,ROLE,FOLLOW_14); 

                    				newLeafNode(otherlv_2, grammarAccess.getRoleManagementAccess().getROLEKeyword_0_2());
                    			
                    // InternalCQLParser.g:4323:4: ( (lv_operations_3_0= RULE_ID ) )
                    // InternalCQLParser.g:4324:5: (lv_operations_3_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4324:5: (lv_operations_3_0= RULE_ID )
                    // InternalCQLParser.g:4325:6: lv_operations_3_0= RULE_ID
                    {
                    lv_operations_3_0=(Token)match(input,RULE_ID,FOLLOW_64); 

                    						newLeafNode(lv_operations_3_0, grammarAccess.getRoleManagementAccess().getOperationsIDTerminalRuleCall_0_3_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRoleManagementRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"operations",
                    							lv_operations_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }

                    // InternalCQLParser.g:4341:4: (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )*
                    loop66:
                    do {
                        int alt66=2;
                        int LA66_0 = input.LA(1);

                        if ( (LA66_0==Comma) ) {
                            alt66=1;
                        }


                        switch (alt66) {
                    	case 1 :
                    	    // InternalCQLParser.g:4342:5: otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) )
                    	    {
                    	    otherlv_4=(Token)match(input,Comma,FOLLOW_14); 

                    	    					newLeafNode(otherlv_4, grammarAccess.getRoleManagementAccess().getCommaKeyword_0_4_0());
                    	    				
                    	    // InternalCQLParser.g:4346:5: ( (lv_operations_5_0= RULE_ID ) )
                    	    // InternalCQLParser.g:4347:6: (lv_operations_5_0= RULE_ID )
                    	    {
                    	    // InternalCQLParser.g:4347:6: (lv_operations_5_0= RULE_ID )
                    	    // InternalCQLParser.g:4348:7: lv_operations_5_0= RULE_ID
                    	    {
                    	    lv_operations_5_0=(Token)match(input,RULE_ID,FOLLOW_64); 

                    	    							newLeafNode(lv_operations_5_0, grammarAccess.getRoleManagementAccess().getOperationsIDTerminalRuleCall_0_4_1_0());
                    	    						

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getRoleManagementRule());
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
                    	    break loop66;
                        }
                    } while (true);

                    otherlv_6=(Token)match(input,TO,FOLLOW_14); 

                    				newLeafNode(otherlv_6, grammarAccess.getRoleManagementAccess().getTOKeyword_0_5());
                    			
                    // InternalCQLParser.g:4369:4: ( (lv_user_7_0= RULE_ID ) )
                    // InternalCQLParser.g:4370:5: (lv_user_7_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4370:5: (lv_user_7_0= RULE_ID )
                    // InternalCQLParser.g:4371:6: lv_user_7_0= RULE_ID
                    {
                    lv_user_7_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    						newLeafNode(lv_user_7_0, grammarAccess.getRoleManagementAccess().getUserIDTerminalRuleCall_0_6_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRoleManagementRule());
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
                    // InternalCQLParser.g:4389:3: ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) )
                    {
                    // InternalCQLParser.g:4389:3: ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) )
                    // InternalCQLParser.g:4390:4: () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:4390:4: ()
                    // InternalCQLParser.g:4391:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getRoleManagementAccess().getRoleManagementAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4397:4: ( (lv_name_9_0= REVOKE ) )
                    // InternalCQLParser.g:4398:5: (lv_name_9_0= REVOKE )
                    {
                    // InternalCQLParser.g:4398:5: (lv_name_9_0= REVOKE )
                    // InternalCQLParser.g:4399:6: lv_name_9_0= REVOKE
                    {
                    lv_name_9_0=(Token)match(input,REVOKE,FOLLOW_66); 

                    						newLeafNode(lv_name_9_0, grammarAccess.getRoleManagementAccess().getNameREVOKEKeyword_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRoleManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_9_0, "REVOKE");
                    					

                    }


                    }

                    otherlv_10=(Token)match(input,ROLE,FOLLOW_14); 

                    				newLeafNode(otherlv_10, grammarAccess.getRoleManagementAccess().getROLEKeyword_1_2());
                    			
                    // InternalCQLParser.g:4415:4: ( (lv_operations_11_0= RULE_ID ) )
                    // InternalCQLParser.g:4416:5: (lv_operations_11_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4416:5: (lv_operations_11_0= RULE_ID )
                    // InternalCQLParser.g:4417:6: lv_operations_11_0= RULE_ID
                    {
                    lv_operations_11_0=(Token)match(input,RULE_ID,FOLLOW_7); 

                    						newLeafNode(lv_operations_11_0, grammarAccess.getRoleManagementAccess().getOperationsIDTerminalRuleCall_1_3_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRoleManagementRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"operations",
                    							lv_operations_11_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }

                    // InternalCQLParser.g:4433:4: (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )*
                    loop67:
                    do {
                        int alt67=2;
                        int LA67_0 = input.LA(1);

                        if ( (LA67_0==Comma) ) {
                            alt67=1;
                        }


                        switch (alt67) {
                    	case 1 :
                    	    // InternalCQLParser.g:4434:5: otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) )
                    	    {
                    	    otherlv_12=(Token)match(input,Comma,FOLLOW_14); 

                    	    					newLeafNode(otherlv_12, grammarAccess.getRoleManagementAccess().getCommaKeyword_1_4_0());
                    	    				
                    	    // InternalCQLParser.g:4438:5: ( (lv_operations_13_0= RULE_ID ) )
                    	    // InternalCQLParser.g:4439:6: (lv_operations_13_0= RULE_ID )
                    	    {
                    	    // InternalCQLParser.g:4439:6: (lv_operations_13_0= RULE_ID )
                    	    // InternalCQLParser.g:4440:7: lv_operations_13_0= RULE_ID
                    	    {
                    	    lv_operations_13_0=(Token)match(input,RULE_ID,FOLLOW_7); 

                    	    							newLeafNode(lv_operations_13_0, grammarAccess.getRoleManagementAccess().getOperationsIDTerminalRuleCall_1_4_1_0());
                    	    						

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getRoleManagementRule());
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
                    	    break loop67;
                        }
                    } while (true);

                    otherlv_14=(Token)match(input,FROM,FOLLOW_14); 

                    				newLeafNode(otherlv_14, grammarAccess.getRoleManagementAccess().getFROMKeyword_1_5());
                    			
                    // InternalCQLParser.g:4461:4: ( (lv_user_15_0= RULE_ID ) )
                    // InternalCQLParser.g:4462:5: (lv_user_15_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4462:5: (lv_user_15_0= RULE_ID )
                    // InternalCQLParser.g:4463:6: lv_user_15_0= RULE_ID
                    {
                    lv_user_15_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    						newLeafNode(lv_user_15_0, grammarAccess.getRoleManagementAccess().getUserIDTerminalRuleCall_1_6_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRoleManagementRule());
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
    // $ANTLR end "ruleRoleManagement"


    // $ANTLR start "entryRuleWindowOperator"
    // InternalCQLParser.g:4484:1: entryRuleWindowOperator returns [EObject current=null] : iv_ruleWindowOperator= ruleWindowOperator EOF ;
    public final EObject entryRuleWindowOperator() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindowOperator = null;


        try {
            // InternalCQLParser.g:4484:55: (iv_ruleWindowOperator= ruleWindowOperator EOF )
            // InternalCQLParser.g:4485:2: iv_ruleWindowOperator= ruleWindowOperator EOF
            {
             newCompositeNode(grammarAccess.getWindowOperatorRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleWindowOperator=ruleWindowOperator();

            state._fsp--;

             current =iv_ruleWindowOperator; 
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
    // $ANTLR end "entryRuleWindowOperator"


    // $ANTLR start "ruleWindowOperator"
    // InternalCQLParser.g:4491:1: ruleWindowOperator returns [EObject current=null] : (otherlv_0= LeftSquareBracket (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow ) otherlv_4= RightSquareBracket ) ;
    public final EObject ruleWindowOperator() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_4=null;
        EObject this_UnboundedWindow_1 = null;

        EObject this_TimebasedWindow_2 = null;

        EObject this_TuplebasedWindow_3 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:4497:2: ( (otherlv_0= LeftSquareBracket (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow ) otherlv_4= RightSquareBracket ) )
            // InternalCQLParser.g:4498:2: (otherlv_0= LeftSquareBracket (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow ) otherlv_4= RightSquareBracket )
            {
            // InternalCQLParser.g:4498:2: (otherlv_0= LeftSquareBracket (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow ) otherlv_4= RightSquareBracket )
            // InternalCQLParser.g:4499:3: otherlv_0= LeftSquareBracket (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow ) otherlv_4= RightSquareBracket
            {
            otherlv_0=(Token)match(input,LeftSquareBracket,FOLLOW_67); 

            			newLeafNode(otherlv_0, grammarAccess.getWindowOperatorAccess().getLeftSquareBracketKeyword_0());
            		
            // InternalCQLParser.g:4503:3: (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow )
            int alt69=3;
            int LA69_0 = input.LA(1);

            if ( (LA69_0==UNBOUNDED) ) {
                alt69=1;
            }
            else if ( (LA69_0==SIZE) ) {
                int LA69_2 = input.LA(2);

                if ( (LA69_2==RULE_INT) ) {
                    int LA69_3 = input.LA(3);

                    if ( (LA69_3==RULE_ID) ) {
                        alt69=2;
                    }
                    else if ( (LA69_3==ADVANCE||LA69_3==TUPLE) ) {
                        alt69=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 69, 3, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 69, 2, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 69, 0, input);

                throw nvae;
            }
            switch (alt69) {
                case 1 :
                    // InternalCQLParser.g:4504:4: this_UnboundedWindow_1= ruleUnboundedWindow
                    {

                    				newCompositeNode(grammarAccess.getWindowOperatorAccess().getUnboundedWindowParserRuleCall_1_0());
                    			
                    pushFollow(FOLLOW_68);
                    this_UnboundedWindow_1=ruleUnboundedWindow();

                    state._fsp--;


                    				current = this_UnboundedWindow_1;
                    				afterParserOrEnumRuleCall();
                    			

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:4513:4: this_TimebasedWindow_2= ruleTimebasedWindow
                    {

                    				newCompositeNode(grammarAccess.getWindowOperatorAccess().getTimebasedWindowParserRuleCall_1_1());
                    			
                    pushFollow(FOLLOW_68);
                    this_TimebasedWindow_2=ruleTimebasedWindow();

                    state._fsp--;


                    				current = this_TimebasedWindow_2;
                    				afterParserOrEnumRuleCall();
                    			

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:4522:4: this_TuplebasedWindow_3= ruleTuplebasedWindow
                    {

                    				newCompositeNode(grammarAccess.getWindowOperatorAccess().getTuplebasedWindowParserRuleCall_1_2());
                    			
                    pushFollow(FOLLOW_68);
                    this_TuplebasedWindow_3=ruleTuplebasedWindow();

                    state._fsp--;


                    				current = this_TuplebasedWindow_3;
                    				afterParserOrEnumRuleCall();
                    			

                    }
                    break;

            }

            otherlv_4=(Token)match(input,RightSquareBracket,FOLLOW_2); 

            			newLeafNode(otherlv_4, grammarAccess.getWindowOperatorAccess().getRightSquareBracketKeyword_2());
            		

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
    // $ANTLR end "ruleWindowOperator"


    // $ANTLR start "entryRuleUnboundedWindow"
    // InternalCQLParser.g:4539:1: entryRuleUnboundedWindow returns [EObject current=null] : iv_ruleUnboundedWindow= ruleUnboundedWindow EOF ;
    public final EObject entryRuleUnboundedWindow() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleUnboundedWindow = null;


        try {
            // InternalCQLParser.g:4539:56: (iv_ruleUnboundedWindow= ruleUnboundedWindow EOF )
            // InternalCQLParser.g:4540:2: iv_ruleUnboundedWindow= ruleUnboundedWindow EOF
            {
             newCompositeNode(grammarAccess.getUnboundedWindowRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleUnboundedWindow=ruleUnboundedWindow();

            state._fsp--;

             current =iv_ruleUnboundedWindow; 
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
    // $ANTLR end "entryRuleUnboundedWindow"


    // $ANTLR start "ruleUnboundedWindow"
    // InternalCQLParser.g:4546:1: ruleUnboundedWindow returns [EObject current=null] : ( () otherlv_1= UNBOUNDED ) ;
    public final EObject ruleUnboundedWindow() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;


        	enterRule();

        try {
            // InternalCQLParser.g:4552:2: ( ( () otherlv_1= UNBOUNDED ) )
            // InternalCQLParser.g:4553:2: ( () otherlv_1= UNBOUNDED )
            {
            // InternalCQLParser.g:4553:2: ( () otherlv_1= UNBOUNDED )
            // InternalCQLParser.g:4554:3: () otherlv_1= UNBOUNDED
            {
            // InternalCQLParser.g:4554:3: ()
            // InternalCQLParser.g:4555:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getUnboundedWindowAccess().getUndboundedWindowAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,UNBOUNDED,FOLLOW_2); 

            			newLeafNode(otherlv_1, grammarAccess.getUnboundedWindowAccess().getUNBOUNDEDKeyword_1());
            		

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
    // $ANTLR end "ruleUnboundedWindow"


    // $ANTLR start "entryRuleTimebasedWindow"
    // InternalCQLParser.g:4569:1: entryRuleTimebasedWindow returns [EObject current=null] : iv_ruleTimebasedWindow= ruleTimebasedWindow EOF ;
    public final EObject entryRuleTimebasedWindow() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTimebasedWindow = null;


        try {
            // InternalCQLParser.g:4569:56: (iv_ruleTimebasedWindow= ruleTimebasedWindow EOF )
            // InternalCQLParser.g:4570:2: iv_ruleTimebasedWindow= ruleTimebasedWindow EOF
            {
             newCompositeNode(grammarAccess.getTimebasedWindowRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTimebasedWindow=ruleTimebasedWindow();

            state._fsp--;

             current =iv_ruleTimebasedWindow; 
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
    // $ANTLR end "entryRuleTimebasedWindow"


    // $ANTLR start "ruleTimebasedWindow"
    // InternalCQLParser.g:4576:1: ruleTimebasedWindow returns [EObject current=null] : ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) ( (lv_unit_3_0= RULE_ID ) ) (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= RULE_ID ) ) )? otherlv_7= TIME ) ;
    public final EObject ruleTimebasedWindow() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_size_2_0=null;
        Token lv_unit_3_0=null;
        Token otherlv_4=null;
        Token lv_advance_size_5_0=null;
        Token lv_advance_unit_6_0=null;
        Token otherlv_7=null;


        	enterRule();

        try {
            // InternalCQLParser.g:4582:2: ( ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) ( (lv_unit_3_0= RULE_ID ) ) (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= RULE_ID ) ) )? otherlv_7= TIME ) )
            // InternalCQLParser.g:4583:2: ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) ( (lv_unit_3_0= RULE_ID ) ) (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= RULE_ID ) ) )? otherlv_7= TIME )
            {
            // InternalCQLParser.g:4583:2: ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) ( (lv_unit_3_0= RULE_ID ) ) (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= RULE_ID ) ) )? otherlv_7= TIME )
            // InternalCQLParser.g:4584:3: () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) ( (lv_unit_3_0= RULE_ID ) ) (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= RULE_ID ) ) )? otherlv_7= TIME
            {
            // InternalCQLParser.g:4584:3: ()
            // InternalCQLParser.g:4585:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getTimebasedWindowAccess().getTimebasedWindowAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,SIZE,FOLLOW_39); 

            			newLeafNode(otherlv_1, grammarAccess.getTimebasedWindowAccess().getSIZEKeyword_1());
            		
            // InternalCQLParser.g:4595:3: ( (lv_size_2_0= RULE_INT ) )
            // InternalCQLParser.g:4596:4: (lv_size_2_0= RULE_INT )
            {
            // InternalCQLParser.g:4596:4: (lv_size_2_0= RULE_INT )
            // InternalCQLParser.g:4597:5: lv_size_2_0= RULE_INT
            {
            lv_size_2_0=(Token)match(input,RULE_INT,FOLLOW_14); 

            					newLeafNode(lv_size_2_0, grammarAccess.getTimebasedWindowAccess().getSizeINTTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getTimebasedWindowRule());
            					}
            					setWithLastConsumed(
            						current,
            						"size",
            						lv_size_2_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }

            // InternalCQLParser.g:4613:3: ( (lv_unit_3_0= RULE_ID ) )
            // InternalCQLParser.g:4614:4: (lv_unit_3_0= RULE_ID )
            {
            // InternalCQLParser.g:4614:4: (lv_unit_3_0= RULE_ID )
            // InternalCQLParser.g:4615:5: lv_unit_3_0= RULE_ID
            {
            lv_unit_3_0=(Token)match(input,RULE_ID,FOLLOW_69); 

            					newLeafNode(lv_unit_3_0, grammarAccess.getTimebasedWindowAccess().getUnitIDTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getTimebasedWindowRule());
            					}
            					setWithLastConsumed(
            						current,
            						"unit",
            						lv_unit_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:4631:3: (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= RULE_ID ) ) )?
            int alt70=2;
            int LA70_0 = input.LA(1);

            if ( (LA70_0==ADVANCE) ) {
                alt70=1;
            }
            switch (alt70) {
                case 1 :
                    // InternalCQLParser.g:4632:4: otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= RULE_ID ) )
                    {
                    otherlv_4=(Token)match(input,ADVANCE,FOLLOW_39); 

                    				newLeafNode(otherlv_4, grammarAccess.getTimebasedWindowAccess().getADVANCEKeyword_4_0());
                    			
                    // InternalCQLParser.g:4636:4: ( (lv_advance_size_5_0= RULE_INT ) )
                    // InternalCQLParser.g:4637:5: (lv_advance_size_5_0= RULE_INT )
                    {
                    // InternalCQLParser.g:4637:5: (lv_advance_size_5_0= RULE_INT )
                    // InternalCQLParser.g:4638:6: lv_advance_size_5_0= RULE_INT
                    {
                    lv_advance_size_5_0=(Token)match(input,RULE_INT,FOLLOW_14); 

                    						newLeafNode(lv_advance_size_5_0, grammarAccess.getTimebasedWindowAccess().getAdvance_sizeINTTerminalRuleCall_4_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getTimebasedWindowRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"advance_size",
                    							lv_advance_size_5_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }

                    // InternalCQLParser.g:4654:4: ( (lv_advance_unit_6_0= RULE_ID ) )
                    // InternalCQLParser.g:4655:5: (lv_advance_unit_6_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4655:5: (lv_advance_unit_6_0= RULE_ID )
                    // InternalCQLParser.g:4656:6: lv_advance_unit_6_0= RULE_ID
                    {
                    lv_advance_unit_6_0=(Token)match(input,RULE_ID,FOLLOW_70); 

                    						newLeafNode(lv_advance_unit_6_0, grammarAccess.getTimebasedWindowAccess().getAdvance_unitIDTerminalRuleCall_4_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getTimebasedWindowRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"advance_unit",
                    							lv_advance_unit_6_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_7=(Token)match(input,TIME,FOLLOW_2); 

            			newLeafNode(otherlv_7, grammarAccess.getTimebasedWindowAccess().getTIMEKeyword_5());
            		

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
    // $ANTLR end "ruleTimebasedWindow"


    // $ANTLR start "entryRuleTuplebasedWindow"
    // InternalCQLParser.g:4681:1: entryRuleTuplebasedWindow returns [EObject current=null] : iv_ruleTuplebasedWindow= ruleTuplebasedWindow EOF ;
    public final EObject entryRuleTuplebasedWindow() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTuplebasedWindow = null;


        try {
            // InternalCQLParser.g:4681:57: (iv_ruleTuplebasedWindow= ruleTuplebasedWindow EOF )
            // InternalCQLParser.g:4682:2: iv_ruleTuplebasedWindow= ruleTuplebasedWindow EOF
            {
             newCompositeNode(grammarAccess.getTuplebasedWindowRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTuplebasedWindow=ruleTuplebasedWindow();

            state._fsp--;

             current =iv_ruleTuplebasedWindow; 
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
    // $ANTLR end "entryRuleTuplebasedWindow"


    // $ANTLR start "ruleTuplebasedWindow"
    // InternalCQLParser.g:4688:1: ruleTuplebasedWindow returns [EObject current=null] : ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )? otherlv_5= TUPLE (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )? ) ;
    public final EObject ruleTuplebasedWindow() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_size_2_0=null;
        Token otherlv_3=null;
        Token lv_advance_size_4_0=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        EObject lv_partition_attribute_8_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:4694:2: ( ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )? otherlv_5= TUPLE (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )? ) )
            // InternalCQLParser.g:4695:2: ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )? otherlv_5= TUPLE (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )? )
            {
            // InternalCQLParser.g:4695:2: ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )? otherlv_5= TUPLE (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )? )
            // InternalCQLParser.g:4696:3: () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )? otherlv_5= TUPLE (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )?
            {
            // InternalCQLParser.g:4696:3: ()
            // InternalCQLParser.g:4697:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getTuplebasedWindowAccess().getTuplebasedWindowAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,SIZE,FOLLOW_39); 

            			newLeafNode(otherlv_1, grammarAccess.getTuplebasedWindowAccess().getSIZEKeyword_1());
            		
            // InternalCQLParser.g:4707:3: ( (lv_size_2_0= RULE_INT ) )
            // InternalCQLParser.g:4708:4: (lv_size_2_0= RULE_INT )
            {
            // InternalCQLParser.g:4708:4: (lv_size_2_0= RULE_INT )
            // InternalCQLParser.g:4709:5: lv_size_2_0= RULE_INT
            {
            lv_size_2_0=(Token)match(input,RULE_INT,FOLLOW_71); 

            					newLeafNode(lv_size_2_0, grammarAccess.getTuplebasedWindowAccess().getSizeINTTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getTuplebasedWindowRule());
            					}
            					setWithLastConsumed(
            						current,
            						"size",
            						lv_size_2_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }

            // InternalCQLParser.g:4725:3: (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )?
            int alt71=2;
            int LA71_0 = input.LA(1);

            if ( (LA71_0==ADVANCE) ) {
                alt71=1;
            }
            switch (alt71) {
                case 1 :
                    // InternalCQLParser.g:4726:4: otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) )
                    {
                    otherlv_3=(Token)match(input,ADVANCE,FOLLOW_39); 

                    				newLeafNode(otherlv_3, grammarAccess.getTuplebasedWindowAccess().getADVANCEKeyword_3_0());
                    			
                    // InternalCQLParser.g:4730:4: ( (lv_advance_size_4_0= RULE_INT ) )
                    // InternalCQLParser.g:4731:5: (lv_advance_size_4_0= RULE_INT )
                    {
                    // InternalCQLParser.g:4731:5: (lv_advance_size_4_0= RULE_INT )
                    // InternalCQLParser.g:4732:6: lv_advance_size_4_0= RULE_INT
                    {
                    lv_advance_size_4_0=(Token)match(input,RULE_INT,FOLLOW_72); 

                    						newLeafNode(lv_advance_size_4_0, grammarAccess.getTuplebasedWindowAccess().getAdvance_sizeINTTerminalRuleCall_3_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getTuplebasedWindowRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"advance_size",
                    							lv_advance_size_4_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_5=(Token)match(input,TUPLE,FOLLOW_54); 

            			newLeafNode(otherlv_5, grammarAccess.getTuplebasedWindowAccess().getTUPLEKeyword_4());
            		
            // InternalCQLParser.g:4753:3: (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )?
            int alt72=2;
            int LA72_0 = input.LA(1);

            if ( (LA72_0==PARTITION) ) {
                alt72=1;
            }
            switch (alt72) {
                case 1 :
                    // InternalCQLParser.g:4754:4: otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) )
                    {
                    otherlv_6=(Token)match(input,PARTITION,FOLLOW_13); 

                    				newLeafNode(otherlv_6, grammarAccess.getTuplebasedWindowAccess().getPARTITIONKeyword_5_0());
                    			
                    otherlv_7=(Token)match(input,BY,FOLLOW_14); 

                    				newLeafNode(otherlv_7, grammarAccess.getTuplebasedWindowAccess().getBYKeyword_5_1());
                    			
                    // InternalCQLParser.g:4762:4: ( (lv_partition_attribute_8_0= ruleAttribute ) )
                    // InternalCQLParser.g:4763:5: (lv_partition_attribute_8_0= ruleAttribute )
                    {
                    // InternalCQLParser.g:4763:5: (lv_partition_attribute_8_0= ruleAttribute )
                    // InternalCQLParser.g:4764:6: lv_partition_attribute_8_0= ruleAttribute
                    {

                    						newCompositeNode(grammarAccess.getTuplebasedWindowAccess().getPartition_attributeAttributeParserRuleCall_5_2_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_partition_attribute_8_0=ruleAttribute();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getTuplebasedWindowRule());
                    						}
                    						set(
                    							current,
                    							"partition_attribute",
                    							lv_partition_attribute_8_0,
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
    // $ANTLR end "ruleTuplebasedWindow"


    // $ANTLR start "entryRuleExpressionsModel"
    // InternalCQLParser.g:4786:1: entryRuleExpressionsModel returns [EObject current=null] : iv_ruleExpressionsModel= ruleExpressionsModel EOF ;
    public final EObject entryRuleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionsModel = null;


        try {
            // InternalCQLParser.g:4786:57: (iv_ruleExpressionsModel= ruleExpressionsModel EOF )
            // InternalCQLParser.g:4787:2: iv_ruleExpressionsModel= ruleExpressionsModel EOF
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
    // InternalCQLParser.g:4793:1: ruleExpressionsModel returns [EObject current=null] : ( () ( (lv_elements_1_0= ruleExpression ) ) ) ;
    public final EObject ruleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:4799:2: ( ( () ( (lv_elements_1_0= ruleExpression ) ) ) )
            // InternalCQLParser.g:4800:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            {
            // InternalCQLParser.g:4800:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            // InternalCQLParser.g:4801:3: () ( (lv_elements_1_0= ruleExpression ) )
            {
            // InternalCQLParser.g:4801:3: ()
            // InternalCQLParser.g:4802:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:4808:3: ( (lv_elements_1_0= ruleExpression ) )
            // InternalCQLParser.g:4809:4: (lv_elements_1_0= ruleExpression )
            {
            // InternalCQLParser.g:4809:4: (lv_elements_1_0= ruleExpression )
            // InternalCQLParser.g:4810:5: lv_elements_1_0= ruleExpression
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
    // InternalCQLParser.g:4831:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCQLParser.g:4831:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCQLParser.g:4832:2: iv_ruleExpression= ruleExpression EOF
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
    // InternalCQLParser.g:4838:1: ruleExpression returns [EObject current=null] : this_OrPredicate_0= ruleOrPredicate ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_OrPredicate_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:4844:2: (this_OrPredicate_0= ruleOrPredicate )
            // InternalCQLParser.g:4845:2: this_OrPredicate_0= ruleOrPredicate
            {

            		newCompositeNode(grammarAccess.getExpressionAccess().getOrPredicateParserRuleCall());
            	
            pushFollow(FOLLOW_2);
            this_OrPredicate_0=ruleOrPredicate();

            state._fsp--;


            		current = this_OrPredicate_0;
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


    // $ANTLR start "entryRuleOrPredicate"
    // InternalCQLParser.g:4856:1: entryRuleOrPredicate returns [EObject current=null] : iv_ruleOrPredicate= ruleOrPredicate EOF ;
    public final EObject entryRuleOrPredicate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOrPredicate = null;


        try {
            // InternalCQLParser.g:4856:52: (iv_ruleOrPredicate= ruleOrPredicate EOF )
            // InternalCQLParser.g:4857:2: iv_ruleOrPredicate= ruleOrPredicate EOF
            {
             newCompositeNode(grammarAccess.getOrPredicateRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleOrPredicate=ruleOrPredicate();

            state._fsp--;

             current =iv_ruleOrPredicate; 
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
    // $ANTLR end "entryRuleOrPredicate"


    // $ANTLR start "ruleOrPredicate"
    // InternalCQLParser.g:4863:1: ruleOrPredicate returns [EObject current=null] : (this_AndPredicate_0= ruleAndPredicate ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )* ) ;
    public final EObject ruleOrPredicate() throws RecognitionException {
        EObject current = null;

        EObject this_AndPredicate_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:4869:2: ( (this_AndPredicate_0= ruleAndPredicate ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )* ) )
            // InternalCQLParser.g:4870:2: (this_AndPredicate_0= ruleAndPredicate ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )* )
            {
            // InternalCQLParser.g:4870:2: (this_AndPredicate_0= ruleAndPredicate ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )* )
            // InternalCQLParser.g:4871:3: this_AndPredicate_0= ruleAndPredicate ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrPredicateAccess().getAndPredicateParserRuleCall_0());
            		
            pushFollow(FOLLOW_73);
            this_AndPredicate_0=ruleAndPredicate();

            state._fsp--;


            			current = this_AndPredicate_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:4879:3: ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )*
            loop73:
            do {
                int alt73=2;
                int LA73_0 = input.LA(1);

                if ( (LA73_0==OR) ) {
                    alt73=1;
                }


                switch (alt73) {
            	case 1 :
            	    // InternalCQLParser.g:4880:4: () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) )
            	    {
            	    // InternalCQLParser.g:4880:4: ()
            	    // InternalCQLParser.g:4881:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrPredicateAccess().getOrPredicateLeftAction_1_0(),
            	    						current);
            	    				

            	    }


            	    				newCompositeNode(grammarAccess.getOrPredicateAccess().getOrOperatorParserRuleCall_1_1());
            	    			
            	    pushFollow(FOLLOW_11);
            	    ruleOrOperator();

            	    state._fsp--;


            	    				afterParserOrEnumRuleCall();
            	    			
            	    // InternalCQLParser.g:4894:4: ( (lv_right_3_0= ruleAndPredicate ) )
            	    // InternalCQLParser.g:4895:5: (lv_right_3_0= ruleAndPredicate )
            	    {
            	    // InternalCQLParser.g:4895:5: (lv_right_3_0= ruleAndPredicate )
            	    // InternalCQLParser.g:4896:6: lv_right_3_0= ruleAndPredicate
            	    {

            	    						newCompositeNode(grammarAccess.getOrPredicateAccess().getRightAndPredicateParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_73);
            	    lv_right_3_0=ruleAndPredicate();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getOrPredicateRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AndPredicate");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop73;
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
    // $ANTLR end "ruleOrPredicate"


    // $ANTLR start "entryRuleAndPredicate"
    // InternalCQLParser.g:4918:1: entryRuleAndPredicate returns [EObject current=null] : iv_ruleAndPredicate= ruleAndPredicate EOF ;
    public final EObject entryRuleAndPredicate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAndPredicate = null;


        try {
            // InternalCQLParser.g:4918:53: (iv_ruleAndPredicate= ruleAndPredicate EOF )
            // InternalCQLParser.g:4919:2: iv_ruleAndPredicate= ruleAndPredicate EOF
            {
             newCompositeNode(grammarAccess.getAndPredicateRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAndPredicate=ruleAndPredicate();

            state._fsp--;

             current =iv_ruleAndPredicate; 
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
    // $ANTLR end "entryRuleAndPredicate"


    // $ANTLR start "ruleAndPredicate"
    // InternalCQLParser.g:4925:1: ruleAndPredicate returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAndPredicate() throws RecognitionException {
        EObject current = null;

        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:4931:2: ( (this_Equalitiy_0= ruleEqualitiy ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCQLParser.g:4932:2: (this_Equalitiy_0= ruleEqualitiy ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCQLParser.g:4932:2: (this_Equalitiy_0= ruleEqualitiy ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCQLParser.g:4933:3: this_Equalitiy_0= ruleEqualitiy ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndPredicateAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_44);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:4941:3: ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop74:
            do {
                int alt74=2;
                int LA74_0 = input.LA(1);

                if ( (LA74_0==AND) ) {
                    alt74=1;
                }


                switch (alt74) {
            	case 1 :
            	    // InternalCQLParser.g:4942:4: () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCQLParser.g:4942:4: ()
            	    // InternalCQLParser.g:4943:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndPredicateAccess().getAndPredicateLeftAction_1_0(),
            	    						current);
            	    				

            	    }


            	    				newCompositeNode(grammarAccess.getAndPredicateAccess().getAndOperatorParserRuleCall_1_1());
            	    			
            	    pushFollow(FOLLOW_11);
            	    ruleAndOperator();

            	    state._fsp--;


            	    				afterParserOrEnumRuleCall();
            	    			
            	    // InternalCQLParser.g:4956:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCQLParser.g:4957:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCQLParser.g:4957:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCQLParser.g:4958:6: lv_right_3_0= ruleEqualitiy
            	    {

            	    						newCompositeNode(grammarAccess.getAndPredicateAccess().getRightEqualitiyParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_44);
            	    lv_right_3_0=ruleEqualitiy();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getAndPredicateRule());
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
            	    break loop74;
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
    // $ANTLR end "ruleAndPredicate"


    // $ANTLR start "entryRuleEqualitiy"
    // InternalCQLParser.g:4980:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCQLParser.g:4980:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCQLParser.g:4981:2: iv_ruleEqualitiy= ruleEqualitiy EOF
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
    // InternalCQLParser.g:4987:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject this_Comparison_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:4993:2: ( (this_Comparison_0= ruleComparison ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCQLParser.g:4994:2: (this_Comparison_0= ruleComparison ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCQLParser.g:4994:2: (this_Comparison_0= ruleComparison ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCQLParser.g:4995:3: this_Comparison_0= ruleComparison ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_74);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:5003:3: ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop75:
            do {
                int alt75=2;
                int LA75_0 = input.LA(1);

                if ( (LA75_0==ExclamationMarkEqualsSign||LA75_0==EqualsSign) ) {
                    alt75=1;
                }


                switch (alt75) {
            	case 1 :
            	    // InternalCQLParser.g:5004:4: () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCQLParser.g:5004:4: ()
            	    // InternalCQLParser.g:5005:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:5011:4: ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) )
            	    // InternalCQLParser.g:5012:5: (lv_op_2_0= ruleEQUALITIY_OPERATOR )
            	    {
            	    // InternalCQLParser.g:5012:5: (lv_op_2_0= ruleEQUALITIY_OPERATOR )
            	    // InternalCQLParser.g:5013:6: lv_op_2_0= ruleEQUALITIY_OPERATOR
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getOpEQUALITIY_OPERATORParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_11);
            	    lv_op_2_0=ruleEQUALITIY_OPERATOR();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getEqualitiyRule());
            	    						}
            	    						set(
            	    							current,
            	    							"op",
            	    							lv_op_2_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.EQUALITIY_OPERATOR");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }

            	    // InternalCQLParser.g:5030:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCQLParser.g:5031:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCQLParser.g:5031:5: (lv_right_3_0= ruleComparison )
            	    // InternalCQLParser.g:5032:6: lv_right_3_0= ruleComparison
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getRightComparisonParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_74);
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
            	    break loop75;
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
    // InternalCQLParser.g:5054:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCQLParser.g:5054:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCQLParser.g:5055:2: iv_ruleComparison= ruleComparison EOF
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
    // InternalCQLParser.g:5061:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
    public final EObject ruleComparison() throws RecognitionException {
        EObject current = null;

        EObject this_PlusOrMinus_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5067:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCQLParser.g:5068:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCQLParser.g:5068:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCQLParser.g:5069:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_75);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:5077:3: ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop76:
            do {
                int alt76=2;
                int LA76_0 = input.LA(1);

                if ( ((LA76_0>=LessThanSignEqualsSign && LA76_0<=GreaterThanSignEqualsSign)||LA76_0==LessThanSign||LA76_0==GreaterThanSign) ) {
                    alt76=1;
                }


                switch (alt76) {
            	case 1 :
            	    // InternalCQLParser.g:5078:4: () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCQLParser.g:5078:4: ()
            	    // InternalCQLParser.g:5079:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:5085:4: ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) )
            	    // InternalCQLParser.g:5086:5: (lv_op_2_0= ruleCOMPARE_OPERATOR )
            	    {
            	    // InternalCQLParser.g:5086:5: (lv_op_2_0= ruleCOMPARE_OPERATOR )
            	    // InternalCQLParser.g:5087:6: lv_op_2_0= ruleCOMPARE_OPERATOR
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getOpCOMPARE_OPERATORParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_11);
            	    lv_op_2_0=ruleCOMPARE_OPERATOR();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getComparisonRule());
            	    						}
            	    						set(
            	    							current,
            	    							"op",
            	    							lv_op_2_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.COMPARE_OPERATOR");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }

            	    // InternalCQLParser.g:5104:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCQLParser.g:5105:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCQLParser.g:5105:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCQLParser.g:5106:6: lv_right_3_0= rulePlusOrMinus
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getRightPlusOrMinusParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_75);
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
            	    break loop76;
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
    // InternalCQLParser.g:5128:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCQLParser.g:5128:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCQLParser.g:5129:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
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
    // InternalCQLParser.g:5135:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5141:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCQLParser.g:5142:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCQLParser.g:5142:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCQLParser.g:5143:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_76);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:5151:3: ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop78:
            do {
                int alt78=2;
                int LA78_0 = input.LA(1);

                if ( (LA78_0==PlusSign||LA78_0==HyphenMinus) ) {
                    alt78=1;
                }


                switch (alt78) {
            	case 1 :
            	    // InternalCQLParser.g:5152:4: ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCQLParser.g:5152:4: ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) )
            	    int alt77=2;
            	    int LA77_0 = input.LA(1);

            	    if ( (LA77_0==PlusSign) ) {
            	        alt77=1;
            	    }
            	    else if ( (LA77_0==HyphenMinus) ) {
            	        alt77=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 77, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt77) {
            	        case 1 :
            	            // InternalCQLParser.g:5153:5: ( () otherlv_2= PlusSign )
            	            {
            	            // InternalCQLParser.g:5153:5: ( () otherlv_2= PlusSign )
            	            // InternalCQLParser.g:5154:6: () otherlv_2= PlusSign
            	            {
            	            // InternalCQLParser.g:5154:6: ()
            	            // InternalCQLParser.g:5155:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_2=(Token)match(input,PlusSign,FOLLOW_11); 

            	            						newLeafNode(otherlv_2, grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1());
            	            					

            	            }


            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:5167:5: ( () otherlv_4= HyphenMinus )
            	            {
            	            // InternalCQLParser.g:5167:5: ( () otherlv_4= HyphenMinus )
            	            // InternalCQLParser.g:5168:6: () otherlv_4= HyphenMinus
            	            {
            	            // InternalCQLParser.g:5168:6: ()
            	            // InternalCQLParser.g:5169:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_4=(Token)match(input,HyphenMinus,FOLLOW_11); 

            	            						newLeafNode(otherlv_4, grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1());
            	            					

            	            }


            	            }
            	            break;

            	    }

            	    // InternalCQLParser.g:5181:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCQLParser.g:5182:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCQLParser.g:5182:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCQLParser.g:5183:6: lv_right_5_0= ruleMulOrDiv
            	    {

            	    						newCompositeNode(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_76);
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
            	    break loop78;
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
    // InternalCQLParser.g:5205:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCQLParser.g:5205:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCQLParser.g:5206:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
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
    // InternalCQLParser.g:5212:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5218:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCQLParser.g:5219:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCQLParser.g:5219:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCQLParser.g:5220:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_77);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:5228:3: ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop80:
            do {
                int alt80=2;
                int LA80_0 = input.LA(1);

                if ( (LA80_0==Asterisk||LA80_0==Solidus) ) {
                    alt80=1;
                }


                switch (alt80) {
            	case 1 :
            	    // InternalCQLParser.g:5229:4: () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCQLParser.g:5229:4: ()
            	    // InternalCQLParser.g:5230:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:5236:4: ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) )
            	    // InternalCQLParser.g:5237:5: ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) )
            	    {
            	    // InternalCQLParser.g:5237:5: ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) )
            	    // InternalCQLParser.g:5238:6: (lv_op_2_1= Solidus | lv_op_2_2= Asterisk )
            	    {
            	    // InternalCQLParser.g:5238:6: (lv_op_2_1= Solidus | lv_op_2_2= Asterisk )
            	    int alt79=2;
            	    int LA79_0 = input.LA(1);

            	    if ( (LA79_0==Solidus) ) {
            	        alt79=1;
            	    }
            	    else if ( (LA79_0==Asterisk) ) {
            	        alt79=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 79, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt79) {
            	        case 1 :
            	            // InternalCQLParser.g:5239:7: lv_op_2_1= Solidus
            	            {
            	            lv_op_2_1=(Token)match(input,Solidus,FOLLOW_11); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getMulOrDivAccess().getOpSolidusKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getMulOrDivRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:5250:7: lv_op_2_2= Asterisk
            	            {
            	            lv_op_2_2=(Token)match(input,Asterisk,FOLLOW_11); 

            	            							newLeafNode(lv_op_2_2, grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getMulOrDivRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_2, null);
            	            						

            	            }
            	            break;

            	    }


            	    }


            	    }

            	    // InternalCQLParser.g:5263:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCQLParser.g:5264:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCQLParser.g:5264:5: (lv_right_3_0= rulePrimary )
            	    // InternalCQLParser.g:5265:6: lv_right_3_0= rulePrimary
            	    {

            	    						newCompositeNode(grammarAccess.getMulOrDivAccess().getRightPrimaryParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_77);
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
            	    break loop80;
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
    // InternalCQLParser.g:5287:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCQLParser.g:5287:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCQLParser.g:5288:2: iv_rulePrimary= rulePrimary EOF
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
    // InternalCQLParser.g:5294:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
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
            // InternalCQLParser.g:5300:2: ( ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCQLParser.g:5301:2: ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCQLParser.g:5301:2: ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt81=3;
            switch ( input.LA(1) ) {
            case LeftParenthesis:
                {
                alt81=1;
                }
                break;
            case NOT:
                {
                alt81=2;
                }
                break;
            case FALSE:
            case TRUE:
            case RULE_ID:
            case RULE_INT:
            case RULE_FLOAT:
            case RULE_STRING:
                {
                alt81=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 81, 0, input);

                throw nvae;
            }

            switch (alt81) {
                case 1 :
                    // InternalCQLParser.g:5302:3: ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis )
                    {
                    // InternalCQLParser.g:5302:3: ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis )
                    // InternalCQLParser.g:5303:4: () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis
                    {
                    // InternalCQLParser.g:5303:4: ()
                    // InternalCQLParser.g:5304:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,LeftParenthesis,FOLLOW_11); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCQLParser.g:5314:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCQLParser.g:5315:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCQLParser.g:5315:5: (lv_inner_2_0= ruleExpression )
                    // InternalCQLParser.g:5316:6: lv_inner_2_0= ruleExpression
                    {

                    						newCompositeNode(grammarAccess.getPrimaryAccess().getInnerExpressionParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_18);
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
                    // InternalCQLParser.g:5339:3: ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCQLParser.g:5339:3: ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCQLParser.g:5340:4: () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCQLParser.g:5340:4: ()
                    // InternalCQLParser.g:5341:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,NOT,FOLLOW_11); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCQLParser.g:5351:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCQLParser.g:5352:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCQLParser.g:5352:5: (lv_expression_6_0= rulePrimary )
                    // InternalCQLParser.g:5353:6: lv_expression_6_0= rulePrimary
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
                    // InternalCQLParser.g:5372:3: this_Atomic_7= ruleAtomic
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
    // InternalCQLParser.g:5384:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCQLParser.g:5384:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCQLParser.g:5385:2: iv_ruleAtomic= ruleAtomic EOF
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
    // InternalCQLParser.g:5391:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) ;
    public final EObject ruleAtomic() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        AntlrDatatypeRuleToken lv_value_7_0 = null;

        EObject lv_value_9_0 = null;

        EObject lv_value_10_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5397:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) )
            // InternalCQLParser.g:5398:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            {
            // InternalCQLParser.g:5398:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            int alt83=5;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt83=1;
                }
                break;
            case RULE_FLOAT:
                {
                alt83=2;
                }
                break;
            case RULE_STRING:
                {
                alt83=3;
                }
                break;
            case FALSE:
            case TRUE:
                {
                alt83=4;
                }
                break;
            case RULE_ID:
                {
                alt83=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 83, 0, input);

                throw nvae;
            }

            switch (alt83) {
                case 1 :
                    // InternalCQLParser.g:5399:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQLParser.g:5399:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQLParser.g:5400:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQLParser.g:5400:4: ()
                    // InternalCQLParser.g:5401:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:5407:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQLParser.g:5408:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQLParser.g:5408:5: (lv_value_1_0= RULE_INT )
                    // InternalCQLParser.g:5409:6: lv_value_1_0= RULE_INT
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
                    // InternalCQLParser.g:5427:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:5427:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQLParser.g:5428:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQLParser.g:5428:4: ()
                    // InternalCQLParser.g:5429:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:5435:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQLParser.g:5436:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQLParser.g:5436:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQLParser.g:5437:6: lv_value_3_0= RULE_FLOAT
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
                    // InternalCQLParser.g:5455:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQLParser.g:5455:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQLParser.g:5456:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQLParser.g:5456:4: ()
                    // InternalCQLParser.g:5457:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:5463:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQLParser.g:5464:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:5464:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQLParser.g:5465:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQLParser.g:5483:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    {
                    // InternalCQLParser.g:5483:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    // InternalCQLParser.g:5484:4: () ( (lv_value_7_0= ruleBOOLEAN ) )
                    {
                    // InternalCQLParser.g:5484:4: ()
                    // InternalCQLParser.g:5485:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:5491:4: ( (lv_value_7_0= ruleBOOLEAN ) )
                    // InternalCQLParser.g:5492:5: (lv_value_7_0= ruleBOOLEAN )
                    {
                    // InternalCQLParser.g:5492:5: (lv_value_7_0= ruleBOOLEAN )
                    // InternalCQLParser.g:5493:6: lv_value_7_0= ruleBOOLEAN
                    {

                    						newCompositeNode(grammarAccess.getAtomicAccess().getValueBOOLEANParserRuleCall_3_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_value_7_0=ruleBOOLEAN();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getAtomicRule());
                    						}
                    						set(
                    							current,
                    							"value",
                    							lv_value_7_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.BOOLEAN");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;
                case 5 :
                    // InternalCQLParser.g:5512:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    {
                    // InternalCQLParser.g:5512:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    // InternalCQLParser.g:5513:4: () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    {
                    // InternalCQLParser.g:5513:4: ()
                    // InternalCQLParser.g:5514:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeRefAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:5520:4: ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    int alt82=2;
                    int LA82_0 = input.LA(1);

                    if ( (LA82_0==RULE_ID) ) {
                        switch ( input.LA(2) ) {
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
                            alt82=1;
                            }
                            break;
                        case FullStop:
                            {
                            int LA82_3 = input.LA(3);

                            if ( (LA82_3==RULE_ID) ) {
                                int LA82_5 = input.LA(4);

                                if ( (LA82_5==IN) ) {
                                    alt82=2;
                                }
                                else if ( (LA82_5==EOF||(LA82_5>=ATTACH && LA82_5<=CREATE)||(LA82_5>=HAVING && LA82_5<=SELECT)||LA82_5==STREAM||LA82_5==ALTER||(LA82_5>=GRANT && LA82_5<=GROUP)||LA82_5==DROP||LA82_5==AND||(LA82_5>=ExclamationMarkEqualsSign && LA82_5<=GreaterThanSignEqualsSign)||LA82_5==OR||(LA82_5>=RightParenthesis && LA82_5<=PlusSign)||LA82_5==HyphenMinus||LA82_5==Solidus||(LA82_5>=Semicolon && LA82_5<=GreaterThanSign)) ) {
                                    alt82=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 82, 5, input);

                                    throw nvae;
                                }
                            }
                            else if ( (LA82_3==Asterisk) ) {
                                int LA82_6 = input.LA(4);

                                if ( (LA82_6==IN) ) {
                                    alt82=2;
                                }
                                else if ( (LA82_6==EOF||(LA82_6>=ATTACH && LA82_6<=CREATE)||(LA82_6>=HAVING && LA82_6<=SELECT)||LA82_6==STREAM||LA82_6==ALTER||(LA82_6>=GRANT && LA82_6<=GROUP)||LA82_6==DROP||LA82_6==AND||(LA82_6>=ExclamationMarkEqualsSign && LA82_6<=GreaterThanSignEqualsSign)||LA82_6==OR||(LA82_6>=RightParenthesis && LA82_6<=PlusSign)||LA82_6==HyphenMinus||LA82_6==Solidus||(LA82_6>=Semicolon && LA82_6<=GreaterThanSign)) ) {
                                    alt82=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 82, 6, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 82, 3, input);

                                throw nvae;
                            }
                            }
                            break;
                        case IN:
                            {
                            alt82=2;
                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 82, 1, input);

                            throw nvae;
                        }

                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 82, 0, input);

                        throw nvae;
                    }
                    switch (alt82) {
                        case 1 :
                            // InternalCQLParser.g:5521:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            {
                            // InternalCQLParser.g:5521:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            // InternalCQLParser.g:5522:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            {
                            // InternalCQLParser.g:5522:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            // InternalCQLParser.g:5523:7: lv_value_9_0= ruleAttributeWithoutAliasDefinition
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
                            // InternalCQLParser.g:5541:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            {
                            // InternalCQLParser.g:5541:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            // InternalCQLParser.g:5542:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            {
                            // InternalCQLParser.g:5542:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            // InternalCQLParser.g:5543:7: lv_value_10_0= ruleAttributeWithNestedStatement
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
    // InternalCQLParser.g:5566:1: entryRuleAtomicWithoutAttributeRef returns [EObject current=null] : iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF ;
    public final EObject entryRuleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomicWithoutAttributeRef = null;


        try {
            // InternalCQLParser.g:5566:66: (iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF )
            // InternalCQLParser.g:5567:2: iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF
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
    // InternalCQLParser.g:5573:1: ruleAtomicWithoutAttributeRef returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) ) ;
    public final EObject ruleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        AntlrDatatypeRuleToken lv_value_7_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5579:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) ) )
            // InternalCQLParser.g:5580:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) )
            {
            // InternalCQLParser.g:5580:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) )
            int alt84=4;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt84=1;
                }
                break;
            case RULE_FLOAT:
                {
                alt84=2;
                }
                break;
            case RULE_STRING:
                {
                alt84=3;
                }
                break;
            case FALSE:
            case TRUE:
                {
                alt84=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 84, 0, input);

                throw nvae;
            }

            switch (alt84) {
                case 1 :
                    // InternalCQLParser.g:5581:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQLParser.g:5581:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQLParser.g:5582:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQLParser.g:5582:4: ()
                    // InternalCQLParser.g:5583:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:5589:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQLParser.g:5590:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQLParser.g:5590:5: (lv_value_1_0= RULE_INT )
                    // InternalCQLParser.g:5591:6: lv_value_1_0= RULE_INT
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
                    // InternalCQLParser.g:5609:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:5609:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQLParser.g:5610:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQLParser.g:5610:4: ()
                    // InternalCQLParser.g:5611:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:5617:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQLParser.g:5618:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQLParser.g:5618:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQLParser.g:5619:6: lv_value_3_0= RULE_FLOAT
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
                    // InternalCQLParser.g:5637:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQLParser.g:5637:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQLParser.g:5638:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQLParser.g:5638:4: ()
                    // InternalCQLParser.g:5639:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:5645:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQLParser.g:5646:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:5646:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQLParser.g:5647:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQLParser.g:5665:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    {
                    // InternalCQLParser.g:5665:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    // InternalCQLParser.g:5666:4: () ( (lv_value_7_0= ruleBOOLEAN ) )
                    {
                    // InternalCQLParser.g:5666:4: ()
                    // InternalCQLParser.g:5667:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:5673:4: ( (lv_value_7_0= ruleBOOLEAN ) )
                    // InternalCQLParser.g:5674:5: (lv_value_7_0= ruleBOOLEAN )
                    {
                    // InternalCQLParser.g:5674:5: (lv_value_7_0= ruleBOOLEAN )
                    // InternalCQLParser.g:5675:6: lv_value_7_0= ruleBOOLEAN
                    {

                    						newCompositeNode(grammarAccess.getAtomicWithoutAttributeRefAccess().getValueBOOLEANParserRuleCall_3_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_value_7_0=ruleBOOLEAN();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getAtomicWithoutAttributeRefRule());
                    						}
                    						set(
                    							current,
                    							"value",
                    							lv_value_7_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.BOOLEAN");
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
    // $ANTLR end "ruleAtomicWithoutAttributeRef"

    // Delegated rules


    protected DFA6 dfa6 = new DFA6(this);
    protected DFA41 dfa41 = new DFA41(this);
    static final String dfa_1s = "\21\uffff";
    static final String dfa_2s = "\1\30\2\15\1\uffff\2\57\3\uffff\1\7\3\uffff\1\124\1\56\2\uffff";
    static final String dfa_3s = "\1\52\1\65\1\64\1\uffff\2\124\3\uffff\1\7\3\uffff\1\124\1\74\2\uffff";
    static final String dfa_4s = "\3\uffff\1\2\2\uffff\1\7\1\1\1\11\1\uffff\1\10\1\3\1\4\2\uffff\1\6\1\5";
    static final String dfa_5s = "\21\uffff}>";
    static final String[] dfa_6s = {
            "\1\2\2\uffff\1\5\4\uffff\1\3\1\uffff\1\4\7\uffff\1\1",
            "\1\6\6\uffff\1\10\11\uffff\1\7\1\3\17\uffff\1\3\1\7\3\uffff\1\3\1\7",
            "\1\11\6\uffff\1\12\12\uffff\1\3\17\uffff\1\3\4\uffff\1\3",
            "",
            "\1\14\44\uffff\1\13",
            "\1\14\44\uffff\1\13",
            "",
            "",
            "",
            "\1\15",
            "",
            "",
            "",
            "\1\16",
            "\1\17\15\uffff\1\20",
            "",
            ""
    };

    static final short[] dfa_1 = DFA.unpackEncodedString(dfa_1s);
    static final char[] dfa_2 = DFA.unpackEncodedStringToUnsignedChars(dfa_2s);
    static final char[] dfa_3 = DFA.unpackEncodedStringToUnsignedChars(dfa_3s);
    static final short[] dfa_4 = DFA.unpackEncodedString(dfa_4s);
    static final short[] dfa_5 = DFA.unpackEncodedString(dfa_5s);
    static final short[][] dfa_6 = unpackEncodedStringArray(dfa_6s);

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = dfa_1;
            this.eof = dfa_1;
            this.min = dfa_2;
            this.max = dfa_3;
            this.accept = dfa_4;
            this.special = dfa_5;
            this.transition = dfa_6;
        }
        public String getDescription() {
            return "235:4: (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore )";
        }
    }
    static final String dfa_7s = "\17\uffff";
    static final String dfa_8s = "\1\124\1\55\1\124\2\uffff\1\124\1\105\1\124\1\15\1\124\4\uffff\1\105";
    static final String dfa_9s = "\1\124\1\104\1\124\2\uffff\1\124\1\110\1\124\1\54\1\124\4\uffff\1\110";
    static final String dfa_10s = "\3\uffff\1\6\1\5\5\uffff\1\2\1\3\1\4\1\1\1\uffff";
    static final String dfa_11s = "\17\uffff}>";
    static final String[] dfa_12s = {
            "\1\1",
            "\1\3\16\uffff\1\4\7\uffff\1\2",
            "\1\5",
            "",
            "",
            "\1\6",
            "\1\10\2\uffff\1\7",
            "\1\11",
            "\1\14\5\uffff\1\12\2\uffff\1\15\25\uffff\1\13",
            "\1\16",
            "",
            "",
            "",
            "",
            "\1\10\2\uffff\1\7"
    };

    static final short[] dfa_7 = DFA.unpackEncodedString(dfa_7s);
    static final char[] dfa_8 = DFA.unpackEncodedStringToUnsignedChars(dfa_8s);
    static final char[] dfa_9 = DFA.unpackEncodedStringToUnsignedChars(dfa_9s);
    static final short[] dfa_10 = DFA.unpackEncodedString(dfa_10s);
    static final short[] dfa_11 = DFA.unpackEncodedString(dfa_11s);
    static final short[][] dfa_12 = unpackEncodedStringArray(dfa_12s);

    class DFA41 extends DFA {

        public DFA41(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 41;
            this.eot = dfa_7;
            this.eof = dfa_7;
            this.min = dfa_8;
            this.max = dfa_9;
            this.accept = dfa_10;
            this.special = dfa_11;
            this.transition = dfa_12;
        }
        public String getDescription() {
            return "2360:5: (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView )";
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000040559800002L,0x0000000000002000L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0008000200004000L,0x0000000000F00040L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0008200200004000L,0x0000000000F00140L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100010L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000020804000002L,0x0000000000100110L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000020804000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0108000200004000L,0x0000000000F00050L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000804000002L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000004000002L,0x0000000000100100L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000004000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x1000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x1000000000000002L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x1000000000000002L,0x0000000000080AC0L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080AC0L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800120L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000120L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0021000040000000L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000040000020000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x2040000000000002L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x0000001020000000L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x8000000000000002L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x0000000010000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0x0010800080000000L});
    public static final BitSet FOLLOW_62 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_63 = new BitSet(new long[]{0x0000000000000000L,0x000000000000010AL});
    public static final BitSet FOLLOW_64 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000108L});
    public static final BitSet FOLLOW_65 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000102L});
    public static final BitSet FOLLOW_66 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_67 = new BitSet(new long[]{0x0002000000001000L});
    public static final BitSet FOLLOW_68 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_69 = new BitSet(new long[]{0x0004000000040000L});
    public static final BitSet FOLLOW_70 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_71 = new BitSet(new long[]{0x0000008000040000L});
    public static final BitSet FOLLOW_72 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_73 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_74 = new BitSet(new long[]{0x0200000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_75 = new BitSet(new long[]{0x0C00000000000002L,0x0000000000014000L});
    public static final BitSet FOLLOW_76 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000280L});
    public static final BitSet FOLLOW_77 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000840L});

}