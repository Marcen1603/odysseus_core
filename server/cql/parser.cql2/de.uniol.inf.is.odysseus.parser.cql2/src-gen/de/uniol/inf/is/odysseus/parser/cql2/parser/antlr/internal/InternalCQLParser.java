package de.uniol.inf.is.odysseus.parser.cql2.parser.antlr.internal;

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
import de.uniol.inf.is.odysseus.parser.cql2.services.CQLGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalCQLParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "NO_LAZY_CONNECTION_CHECK", "INTERSECTION", "MILLISECONDS", "DATAHANDLER", "MILLISECOND", "CONNECTION", "DIFFERENCE", "IDENTIFIED", "PARTITION", "TRANSPORT", "UNBOUNDED", "DATABASE", "DISTINCT", "PASSWORD", "PROTOCOL", "TRUNCATE", "ADVANCE", "CHANNEL", "CONTEXT", "MINUTES", "OPTIONS", "SECONDS", "WRAPPER", "ATTACH", "CREATE", "EXISTS", "HAVING", "MINUTE", "REVOKE", "SECOND", "SELECT", "SINGLE", "STREAM", "TENANT", "ALTER", "FALSE", "GRANT", "GROUP", "HOURS", "MULTI", "STORE", "TABLE", "TUPLE", "UNION", "WEEKS", "WHERE", "DROP", "EACH", "FILE", "FROM", "HOUR", "JDBC", "ROLE", "SINK", "SIZE", "SOME", "TIME", "TRUE", "USER", "VIEW", "WEEK", "WITH", "ALL", "AND", "ANY", "NOT", "ExclamationMarkEqualsSign", "LessThanSignEqualsSign", "GreaterThanSignEqualsSign", "AS", "AT", "BY", "IF", "IN", "ON", "OR", "TO", "DollarSign", "LeftParenthesis", "RightParenthesis", "Asterisk", "PlusSign", "Comma", "HyphenMinus", "FullStop", "Solidus", "Colon", "Semicolon", "LessThanSign", "EqualsSign", "GreaterThanSign", "LeftSquareBracket", "RightSquareBracket", "CircumflexAccent", "LeftCurlyBracket", "RightCurlyBracket", "RULE_LETTER", "RULE_SPECIAL_CHARS", "RULE_INT", "RULE_ID", "RULE_FLOAT", "RULE_BIT", "RULE_BYTE", "RULE_VECTOR_FLOAT", "RULE_MATRIX_FLOAT", "RULE_PATH", "RULE_STRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER"
    };
    public static final int CREATE=28;
    public static final int CONTEXT=22;
    public static final int LessThanSign=92;
    public static final int TABLE=45;
    public static final int RULE_BIT=105;
    public static final int LeftParenthesis=82;
    public static final int RULE_VECTOR_FLOAT=107;
    public static final int PARTITION=12;
    public static final int IF=76;
    public static final int MILLISECONDS=6;
    public static final int EACH=51;
    public static final int GreaterThanSign=94;
    public static final int RULE_ID=103;
    public static final int IN=77;
    public static final int DISTINCT=16;
    public static final int SIZE=58;
    public static final int RULE_SPECIAL_CHARS=101;
    public static final int PROTOCOL=18;
    public static final int OPTIONS=24;
    public static final int WHERE=49;
    public static final int GreaterThanSignEqualsSign=72;
    public static final int AS=73;
    public static final int AT=74;
    public static final int DATABASE=15;
    public static final int CHANNEL=21;
    public static final int WEEKS=48;
    public static final int PlusSign=85;
    public static final int RULE_INT=102;
    public static final int RULE_ML_COMMENT=111;
    public static final int LeftSquareBracket=95;
    public static final int ADVANCE=20;
    public static final int ALTER=38;
    public static final int RULE_BYTE=106;
    public static final int ROLE=56;
    public static final int GROUP=41;
    public static final int Comma=86;
    public static final int HyphenMinus=87;
    public static final int BY=75;
    public static final int LessThanSignEqualsSign=71;
    public static final int Solidus=89;
    public static final int RightCurlyBracket=99;
    public static final int DATAHANDLER=7;
    public static final int FILE=52;
    public static final int FullStop=88;
    public static final int REVOKE=32;
    public static final int SECONDS=25;
    public static final int NO_LAZY_CONNECTION_CHECK=4;
    public static final int SELECT=34;
    public static final int TUPLE=46;
    public static final int CONNECTION=9;
    public static final int Semicolon=91;
    public static final int RULE_LETTER=100;
    public static final int STORE=44;
    public static final int RULE_FLOAT=104;
    public static final int MILLISECOND=8;
    public static final int ExclamationMarkEqualsSign=70;
    public static final int TO=80;
    public static final int MULTI=43;
    public static final int UNION=47;
    public static final int TRUNCATE=19;
    public static final int ALL=66;
    public static final int SINGLE=35;
    public static final int FROM=53;
    public static final int VIEW=63;
    public static final int UNBOUNDED=14;
    public static final int WRAPPER=26;
    public static final int MINUTE=31;
    public static final int RightSquareBracket=96;
    public static final int RightParenthesis=83;
    public static final int TRUE=61;
    public static final int RULE_PATH=109;
    public static final int NOT=69;
    public static final int INTERSECTION=5;
    public static final int PASSWORD=17;
    public static final int SINK=57;
    public static final int AND=67;
    public static final int HAVING=30;
    public static final int HOUR=54;
    public static final int RULE_STRING=110;
    public static final int ANY=68;
    public static final int DROP=50;
    public static final int RULE_SL_COMMENT=112;
    public static final int EqualsSign=93;
    public static final int TRANSPORT=13;
    public static final int SOME=59;
    public static final int DIFFERENCE=10;
    public static final int JDBC=55;
    public static final int Colon=90;
    public static final int WEEK=64;
    public static final int EOF=-1;
    public static final int Asterisk=84;
    public static final int ON=78;
    public static final int OR=79;
    public static final int EXISTS=29;
    public static final int RULE_WS=113;
    public static final int STREAM=36;
    public static final int LeftCurlyBracket=98;
    public static final int IDENTIFIED=11;
    public static final int TIME=60;
    public static final int RULE_ANY_OTHER=114;
    public static final int USER=62;
    public static final int TENANT=37;
    public static final int WITH=65;
    public static final int CircumflexAccent=97;
    public static final int GRANT=40;
    public static final int ATTACH=27;
    public static final int HOURS=42;
    public static final int DollarSign=81;
    public static final int SECOND=33;
    public static final int FALSE=39;
    public static final int RULE_MATRIX_FLOAT=108;
    public static final int MINUTES=23;

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
    // InternalCQLParser.g:65:1: ruleModel returns [EObject current=null] : ( ( ( (lv_components_0_1= ruleQuery | lv_components_0_2= ruleCommand ) ) ) (otherlv_1= Semicolon )? )* ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_components_0_1 = null;

        EObject lv_components_0_2 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:71:2: ( ( ( ( (lv_components_0_1= ruleQuery | lv_components_0_2= ruleCommand ) ) ) (otherlv_1= Semicolon )? )* )
            // InternalCQLParser.g:72:2: ( ( ( (lv_components_0_1= ruleQuery | lv_components_0_2= ruleCommand ) ) ) (otherlv_1= Semicolon )? )*
            {
            // InternalCQLParser.g:72:2: ( ( ( (lv_components_0_1= ruleQuery | lv_components_0_2= ruleCommand ) ) ) (otherlv_1= Semicolon )? )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>=ATTACH && LA3_0<=CREATE)||LA3_0==REVOKE||LA3_0==SELECT||LA3_0==STREAM||LA3_0==ALTER||LA3_0==GRANT||LA3_0==DROP) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalCQLParser.g:73:3: ( ( (lv_components_0_1= ruleQuery | lv_components_0_2= ruleCommand ) ) ) (otherlv_1= Semicolon )?
            	    {
            	    // InternalCQLParser.g:73:3: ( ( (lv_components_0_1= ruleQuery | lv_components_0_2= ruleCommand ) ) )
            	    // InternalCQLParser.g:74:4: ( (lv_components_0_1= ruleQuery | lv_components_0_2= ruleCommand ) )
            	    {
            	    // InternalCQLParser.g:74:4: ( (lv_components_0_1= ruleQuery | lv_components_0_2= ruleCommand ) )
            	    // InternalCQLParser.g:75:5: (lv_components_0_1= ruleQuery | lv_components_0_2= ruleCommand )
            	    {
            	    // InternalCQLParser.g:75:5: (lv_components_0_1= ruleQuery | lv_components_0_2= ruleCommand )
            	    int alt1=2;
            	    switch ( input.LA(1) ) {
            	    case CREATE:
            	        {
            	        int LA1_1 = input.LA(2);

            	        if ( (LA1_1==DATABASE||LA1_1==CONTEXT||LA1_1==TENANT||LA1_1==ROLE||LA1_1==USER) ) {
            	            alt1=2;
            	        }
            	        else if ( (LA1_1==STREAM||LA1_1==SINK||LA1_1==VIEW) ) {
            	            alt1=1;
            	        }
            	        else {
            	            NoViableAltException nvae =
            	                new NoViableAltException("", 1, 1, input);

            	            throw nvae;
            	        }
            	        }
            	        break;
            	    case ATTACH:
            	    case SELECT:
            	    case STREAM:
            	        {
            	        alt1=1;
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
            	            // InternalCQLParser.g:76:6: lv_components_0_1= ruleQuery
            	            {

            	            						newCompositeNode(grammarAccess.getModelAccess().getComponentsQueryParserRuleCall_0_0_0());
            	            					
            	            pushFollow(FOLLOW_3);
            	            lv_components_0_1=ruleQuery();

            	            state._fsp--;


            	            						if (current==null) {
            	            							current = createModelElementForParent(grammarAccess.getModelRule());
            	            						}
            	            						add(
            	            							current,
            	            							"components",
            	            							lv_components_0_1,
            	            							"de.uniol.inf.is.odysseus.parser.cql2.CQL.Query");
            	            						afterParserOrEnumRuleCall();
            	            					

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:92:6: lv_components_0_2= ruleCommand
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
            	            							"de.uniol.inf.is.odysseus.parser.cql2.CQL.Command");
            	            						afterParserOrEnumRuleCall();
            	            					

            	            }
            	            break;

            	    }


            	    }


            	    }

            	    // InternalCQLParser.g:110:3: (otherlv_1= Semicolon )?
            	    int alt2=2;
            	    int LA2_0 = input.LA(1);

            	    if ( (LA2_0==Semicolon) ) {
            	        alt2=1;
            	    }
            	    switch (alt2) {
            	        case 1 :
            	            // InternalCQLParser.g:111:4: otherlv_1= Semicolon
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
    // InternalCQLParser.g:120:1: entryRuleBOOLEAN returns [String current=null] : iv_ruleBOOLEAN= ruleBOOLEAN EOF ;
    public final String entryRuleBOOLEAN() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleBOOLEAN = null;


        try {
            // InternalCQLParser.g:120:47: (iv_ruleBOOLEAN= ruleBOOLEAN EOF )
            // InternalCQLParser.g:121:2: iv_ruleBOOLEAN= ruleBOOLEAN EOF
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
    // InternalCQLParser.g:127:1: ruleBOOLEAN returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= FALSE | kw= TRUE ) ;
    public final AntlrDatatypeRuleToken ruleBOOLEAN() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:133:2: ( (kw= FALSE | kw= TRUE ) )
            // InternalCQLParser.g:134:2: (kw= FALSE | kw= TRUE )
            {
            // InternalCQLParser.g:134:2: (kw= FALSE | kw= TRUE )
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
                    // InternalCQLParser.g:135:3: kw= FALSE
                    {
                    kw=(Token)match(input,FALSE,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getBOOLEANAccess().getFALSEKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:141:3: kw= TRUE
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


    // $ANTLR start "entryRuleSystemVariable"
    // InternalCQLParser.g:150:1: entryRuleSystemVariable returns [String current=null] : iv_ruleSystemVariable= ruleSystemVariable EOF ;
    public final String entryRuleSystemVariable() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleSystemVariable = null;


        try {
            // InternalCQLParser.g:150:54: (iv_ruleSystemVariable= ruleSystemVariable EOF )
            // InternalCQLParser.g:151:2: iv_ruleSystemVariable= ruleSystemVariable EOF
            {
             newCompositeNode(grammarAccess.getSystemVariableRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSystemVariable=ruleSystemVariable();

            state._fsp--;

             current =iv_ruleSystemVariable.getText(); 
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
    // $ANTLR end "entryRuleSystemVariable"


    // $ANTLR start "ruleSystemVariable"
    // InternalCQLParser.g:157:1: ruleSystemVariable returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= DollarSign kw= LeftCurlyBracket this_ID_2= RULE_ID kw= RightCurlyBracket ) ;
    public final AntlrDatatypeRuleToken ruleSystemVariable() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        Token this_ID_2=null;


        	enterRule();

        try {
            // InternalCQLParser.g:163:2: ( (kw= DollarSign kw= LeftCurlyBracket this_ID_2= RULE_ID kw= RightCurlyBracket ) )
            // InternalCQLParser.g:164:2: (kw= DollarSign kw= LeftCurlyBracket this_ID_2= RULE_ID kw= RightCurlyBracket )
            {
            // InternalCQLParser.g:164:2: (kw= DollarSign kw= LeftCurlyBracket this_ID_2= RULE_ID kw= RightCurlyBracket )
            // InternalCQLParser.g:165:3: kw= DollarSign kw= LeftCurlyBracket this_ID_2= RULE_ID kw= RightCurlyBracket
            {
            kw=(Token)match(input,DollarSign,FOLLOW_4); 

            			current.merge(kw);
            			newLeafNode(kw, grammarAccess.getSystemVariableAccess().getDollarSignKeyword_0());
            		
            kw=(Token)match(input,LeftCurlyBracket,FOLLOW_5); 

            			current.merge(kw);
            			newLeafNode(kw, grammarAccess.getSystemVariableAccess().getLeftCurlyBracketKeyword_1());
            		
            this_ID_2=(Token)match(input,RULE_ID,FOLLOW_6); 

            			current.merge(this_ID_2);
            		

            			newLeafNode(this_ID_2, grammarAccess.getSystemVariableAccess().getIDTerminalRuleCall_2());
            		
            kw=(Token)match(input,RightCurlyBracket,FOLLOW_2); 

            			current.merge(kw);
            			newLeafNode(kw, grammarAccess.getSystemVariableAccess().getRightCurlyBracketKeyword_3());
            		

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
    // $ANTLR end "ruleSystemVariable"


    // $ANTLR start "entryRuleQualifiedAttributename"
    // InternalCQLParser.g:191:1: entryRuleQualifiedAttributename returns [String current=null] : iv_ruleQualifiedAttributename= ruleQualifiedAttributename EOF ;
    public final String entryRuleQualifiedAttributename() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedAttributename = null;


        try {
            // InternalCQLParser.g:191:62: (iv_ruleQualifiedAttributename= ruleQualifiedAttributename EOF )
            // InternalCQLParser.g:192:2: iv_ruleQualifiedAttributename= ruleQualifiedAttributename EOF
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
    // InternalCQLParser.g:198:1: ruleQualifiedAttributename returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) | (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk ) | this_SystemVariable_7= ruleSystemVariable ) ;
    public final AntlrDatatypeRuleToken ruleQualifiedAttributename() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_3=null;
        AntlrDatatypeRuleToken this_QualifiedSourcename_1 = null;

        AntlrDatatypeRuleToken this_QualifiedSourcename_4 = null;

        AntlrDatatypeRuleToken this_SystemVariable_7 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:204:2: ( (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) | (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk ) | this_SystemVariable_7= ruleSystemVariable ) )
            // InternalCQLParser.g:205:2: (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) | (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk ) | this_SystemVariable_7= ruleSystemVariable )
            {
            // InternalCQLParser.g:205:2: (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) | (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk ) | this_SystemVariable_7= ruleSystemVariable )
            int alt5=4;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==RULE_ID) ) {
                int LA5_1 = input.LA(2);

                if ( (LA5_1==FullStop) ) {
                    int LA5_3 = input.LA(3);

                    if ( (LA5_3==RULE_ID) ) {
                        alt5=2;
                    }
                    else if ( (LA5_3==Asterisk) ) {
                        alt5=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 5, 3, input);

                        throw nvae;
                    }
                }
                else if ( (LA5_1==EOF||LA5_1==INTERSECTION||LA5_1==DIFFERENCE||(LA5_1>=ATTACH && LA5_1<=CREATE)||LA5_1==HAVING||LA5_1==REVOKE||LA5_1==SELECT||LA5_1==STREAM||(LA5_1>=ALTER && LA5_1<=GROUP)||LA5_1==UNION||LA5_1==DROP||LA5_1==FROM||LA5_1==TRUE||LA5_1==AND||(LA5_1>=ExclamationMarkEqualsSign && LA5_1<=AS)||LA5_1==IN||LA5_1==OR||LA5_1==DollarSign||(LA5_1>=RightParenthesis && LA5_1<=HyphenMinus)||LA5_1==Solidus||(LA5_1>=Semicolon && LA5_1<=GreaterThanSign)||LA5_1==RightSquareBracket||(LA5_1>=RULE_INT && LA5_1<=RULE_FLOAT)||(LA5_1>=RULE_VECTOR_FLOAT && LA5_1<=RULE_MATRIX_FLOAT)||LA5_1==RULE_STRING) ) {
                    alt5=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA5_0==DollarSign) ) {
                alt5=4;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // InternalCQLParser.g:206:3: this_ID_0= RULE_ID
                    {
                    this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    			current.merge(this_ID_0);
                    		

                    			newLeafNode(this_ID_0, grammarAccess.getQualifiedAttributenameAccess().getIDTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:214:3: (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID )
                    {
                    // InternalCQLParser.g:214:3: (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID )
                    // InternalCQLParser.g:215:4: this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID
                    {

                    				newCompositeNode(grammarAccess.getQualifiedAttributenameAccess().getQualifiedSourcenameParserRuleCall_1_0());
                    			
                    pushFollow(FOLLOW_7);
                    this_QualifiedSourcename_1=ruleQualifiedSourcename();

                    state._fsp--;


                    				current.merge(this_QualifiedSourcename_1);
                    			

                    				afterParserOrEnumRuleCall();
                    			
                    kw=(Token)match(input,FullStop,FOLLOW_5); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getQualifiedAttributenameAccess().getFullStopKeyword_1_1());
                    			
                    this_ID_3=(Token)match(input,RULE_ID,FOLLOW_2); 

                    				current.merge(this_ID_3);
                    			

                    				newLeafNode(this_ID_3, grammarAccess.getQualifiedAttributenameAccess().getIDTerminalRuleCall_1_2());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:239:3: (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk )
                    {
                    // InternalCQLParser.g:239:3: (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk )
                    // InternalCQLParser.g:240:4: this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk
                    {

                    				newCompositeNode(grammarAccess.getQualifiedAttributenameAccess().getQualifiedSourcenameParserRuleCall_2_0());
                    			
                    pushFollow(FOLLOW_7);
                    this_QualifiedSourcename_4=ruleQualifiedSourcename();

                    state._fsp--;


                    				current.merge(this_QualifiedSourcename_4);
                    			

                    				afterParserOrEnumRuleCall();
                    			
                    kw=(Token)match(input,FullStop,FOLLOW_8); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getQualifiedAttributenameAccess().getFullStopKeyword_2_1());
                    			
                    kw=(Token)match(input,Asterisk,FOLLOW_2); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getQualifiedAttributenameAccess().getAsteriskKeyword_2_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:262:3: this_SystemVariable_7= ruleSystemVariable
                    {

                    			newCompositeNode(grammarAccess.getQualifiedAttributenameAccess().getSystemVariableParserRuleCall_3());
                    		
                    pushFollow(FOLLOW_2);
                    this_SystemVariable_7=ruleSystemVariable();

                    state._fsp--;


                    			current.merge(this_SystemVariable_7);
                    		

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
    // $ANTLR end "ruleQualifiedAttributename"


    // $ANTLR start "entryRuleQualifiedAttributenameWithoutSpecialChars"
    // InternalCQLParser.g:276:1: entryRuleQualifiedAttributenameWithoutSpecialChars returns [String current=null] : iv_ruleQualifiedAttributenameWithoutSpecialChars= ruleQualifiedAttributenameWithoutSpecialChars EOF ;
    public final String entryRuleQualifiedAttributenameWithoutSpecialChars() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedAttributenameWithoutSpecialChars = null;


        try {
            // InternalCQLParser.g:276:81: (iv_ruleQualifiedAttributenameWithoutSpecialChars= ruleQualifiedAttributenameWithoutSpecialChars EOF )
            // InternalCQLParser.g:277:2: iv_ruleQualifiedAttributenameWithoutSpecialChars= ruleQualifiedAttributenameWithoutSpecialChars EOF
            {
             newCompositeNode(grammarAccess.getQualifiedAttributenameWithoutSpecialCharsRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleQualifiedAttributenameWithoutSpecialChars=ruleQualifiedAttributenameWithoutSpecialChars();

            state._fsp--;

             current =iv_ruleQualifiedAttributenameWithoutSpecialChars.getText(); 
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
    // $ANTLR end "entryRuleQualifiedAttributenameWithoutSpecialChars"


    // $ANTLR start "ruleQualifiedAttributenameWithoutSpecialChars"
    // InternalCQLParser.g:283:1: ruleQualifiedAttributenameWithoutSpecialChars returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) ) ;
    public final AntlrDatatypeRuleToken ruleQualifiedAttributenameWithoutSpecialChars() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_3=null;
        AntlrDatatypeRuleToken this_QualifiedSourcename_1 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:289:2: ( (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) ) )
            // InternalCQLParser.g:290:2: (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) )
            {
            // InternalCQLParser.g:290:2: (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==RULE_ID) ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1==FullStop) ) {
                    alt6=2;
                }
                else if ( (LA6_1==EOF||LA6_1==FALSE||LA6_1==FROM||LA6_1==TRUE||LA6_1==AS||LA6_1==DollarSign||(LA6_1>=RightParenthesis && LA6_1<=HyphenMinus)||LA6_1==Solidus||LA6_1==CircumflexAccent||(LA6_1>=RULE_INT && LA6_1<=RULE_FLOAT)||(LA6_1>=RULE_VECTOR_FLOAT && LA6_1<=RULE_MATRIX_FLOAT)||LA6_1==RULE_STRING) ) {
                    alt6=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

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
                    // InternalCQLParser.g:291:3: this_ID_0= RULE_ID
                    {
                    this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    			current.merge(this_ID_0);
                    		

                    			newLeafNode(this_ID_0, grammarAccess.getQualifiedAttributenameWithoutSpecialCharsAccess().getIDTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:299:3: (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID )
                    {
                    // InternalCQLParser.g:299:3: (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID )
                    // InternalCQLParser.g:300:4: this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID
                    {

                    				newCompositeNode(grammarAccess.getQualifiedAttributenameWithoutSpecialCharsAccess().getQualifiedSourcenameParserRuleCall_1_0());
                    			
                    pushFollow(FOLLOW_7);
                    this_QualifiedSourcename_1=ruleQualifiedSourcename();

                    state._fsp--;


                    				current.merge(this_QualifiedSourcename_1);
                    			

                    				afterParserOrEnumRuleCall();
                    			
                    kw=(Token)match(input,FullStop,FOLLOW_5); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getQualifiedAttributenameWithoutSpecialCharsAccess().getFullStopKeyword_1_1());
                    			
                    this_ID_3=(Token)match(input,RULE_ID,FOLLOW_2); 

                    				current.merge(this_ID_3);
                    			

                    				newLeafNode(this_ID_3, grammarAccess.getQualifiedAttributenameWithoutSpecialCharsAccess().getIDTerminalRuleCall_1_2());
                    			

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
    // $ANTLR end "ruleQualifiedAttributenameWithoutSpecialChars"


    // $ANTLR start "entryRuleQualifiedSourcename"
    // InternalCQLParser.g:327:1: entryRuleQualifiedSourcename returns [String current=null] : iv_ruleQualifiedSourcename= ruleQualifiedSourcename EOF ;
    public final String entryRuleQualifiedSourcename() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedSourcename = null;


        try {
            // InternalCQLParser.g:327:59: (iv_ruleQualifiedSourcename= ruleQualifiedSourcename EOF )
            // InternalCQLParser.g:328:2: iv_ruleQualifiedSourcename= ruleQualifiedSourcename EOF
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
    // InternalCQLParser.g:334:1: ruleQualifiedSourcename returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : this_ID_0= RULE_ID ;
    public final AntlrDatatypeRuleToken ruleQualifiedSourcename() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:340:2: (this_ID_0= RULE_ID )
            // InternalCQLParser.g:341:2: this_ID_0= RULE_ID
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


    // $ANTLR start "entryRuleQuery"
    // InternalCQLParser.g:351:1: entryRuleQuery returns [EObject current=null] : iv_ruleQuery= ruleQuery EOF ;
    public final EObject entryRuleQuery() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleQuery = null;


        try {
            // InternalCQLParser.g:351:46: (iv_ruleQuery= ruleQuery EOF )
            // InternalCQLParser.g:352:2: iv_ruleQuery= ruleQuery EOF
            {
             newCompositeNode(grammarAccess.getQueryRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleQuery=ruleQuery();

            state._fsp--;

             current =iv_ruleQuery; 
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
    // $ANTLR end "entryRuleQuery"


    // $ANTLR start "ruleQuery"
    // InternalCQLParser.g:358:1: ruleQuery returns [EObject current=null] : ( ( (lv_type_0_1= ruleCreate | lv_type_0_2= ruleStreamTo | lv_type_0_3= ruleComplexSelect ) ) ) ;
    public final EObject ruleQuery() throws RecognitionException {
        EObject current = null;

        EObject lv_type_0_1 = null;

        EObject lv_type_0_2 = null;

        EObject lv_type_0_3 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:364:2: ( ( ( (lv_type_0_1= ruleCreate | lv_type_0_2= ruleStreamTo | lv_type_0_3= ruleComplexSelect ) ) ) )
            // InternalCQLParser.g:365:2: ( ( (lv_type_0_1= ruleCreate | lv_type_0_2= ruleStreamTo | lv_type_0_3= ruleComplexSelect ) ) )
            {
            // InternalCQLParser.g:365:2: ( ( (lv_type_0_1= ruleCreate | lv_type_0_2= ruleStreamTo | lv_type_0_3= ruleComplexSelect ) ) )
            // InternalCQLParser.g:366:3: ( (lv_type_0_1= ruleCreate | lv_type_0_2= ruleStreamTo | lv_type_0_3= ruleComplexSelect ) )
            {
            // InternalCQLParser.g:366:3: ( (lv_type_0_1= ruleCreate | lv_type_0_2= ruleStreamTo | lv_type_0_3= ruleComplexSelect ) )
            // InternalCQLParser.g:367:4: (lv_type_0_1= ruleCreate | lv_type_0_2= ruleStreamTo | lv_type_0_3= ruleComplexSelect )
            {
            // InternalCQLParser.g:367:4: (lv_type_0_1= ruleCreate | lv_type_0_2= ruleStreamTo | lv_type_0_3= ruleComplexSelect )
            int alt7=3;
            switch ( input.LA(1) ) {
            case ATTACH:
            case CREATE:
                {
                alt7=1;
                }
                break;
            case STREAM:
                {
                alt7=2;
                }
                break;
            case SELECT:
                {
                alt7=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }

            switch (alt7) {
                case 1 :
                    // InternalCQLParser.g:368:5: lv_type_0_1= ruleCreate
                    {

                    					newCompositeNode(grammarAccess.getQueryAccess().getTypeCreateParserRuleCall_0_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_type_0_1=ruleCreate();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getQueryRule());
                    					}
                    					set(
                    						current,
                    						"type",
                    						lv_type_0_1,
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.Create");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:384:5: lv_type_0_2= ruleStreamTo
                    {

                    					newCompositeNode(grammarAccess.getQueryAccess().getTypeStreamToParserRuleCall_0_1());
                    				
                    pushFollow(FOLLOW_2);
                    lv_type_0_2=ruleStreamTo();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getQueryRule());
                    					}
                    					set(
                    						current,
                    						"type",
                    						lv_type_0_2,
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.StreamTo");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:400:5: lv_type_0_3= ruleComplexSelect
                    {

                    					newCompositeNode(grammarAccess.getQueryAccess().getTypeComplexSelectParserRuleCall_0_2());
                    				
                    pushFollow(FOLLOW_2);
                    lv_type_0_3=ruleComplexSelect();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getQueryRule());
                    					}
                    					set(
                    						current,
                    						"type",
                    						lv_type_0_3,
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ComplexSelect");
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
    // $ANTLR end "ruleQuery"


    // $ANTLR start "entryRuleCommand"
    // InternalCQLParser.g:421:1: entryRuleCommand returns [EObject current=null] : iv_ruleCommand= ruleCommand EOF ;
    public final EObject entryRuleCommand() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCommand = null;


        try {
            // InternalCQLParser.g:421:48: (iv_ruleCommand= ruleCommand EOF )
            // InternalCQLParser.g:422:2: iv_ruleCommand= ruleCommand EOF
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
    // InternalCQLParser.g:428:1: ruleCommand returns [EObject current=null] : ( ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) ) ) ;
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
            // InternalCQLParser.g:434:2: ( ( ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) ) ) )
            // InternalCQLParser.g:435:2: ( ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) ) )
            {
            // InternalCQLParser.g:435:2: ( ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) ) )
            // InternalCQLParser.g:436:3: ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) )
            {
            // InternalCQLParser.g:436:3: ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) )
            // InternalCQLParser.g:437:4: (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore )
            {
            // InternalCQLParser.g:437:4: (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore )
            int alt8=9;
            alt8 = dfa8.predict(input);
            switch (alt8) {
                case 1 :
                    // InternalCQLParser.g:438:5: lv_type_0_1= ruleDropStream
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
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.DropStream");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:454:5: lv_type_0_2= ruleUserManagement
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
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.UserManagement");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:470:5: lv_type_0_3= ruleRightsManagement
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
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.RightsManagement");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:486:5: lv_type_0_4= ruleRoleManagement
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
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.RoleManagement");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 5 :
                    // InternalCQLParser.g:502:5: lv_type_0_5= ruleCreateDataBaseGenericConnection
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
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.CreateDataBaseGenericConnection");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 6 :
                    // InternalCQLParser.g:518:5: lv_type_0_6= ruleCreateDataBaseJDBCConnection
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
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.CreateDataBaseJDBCConnection");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 7 :
                    // InternalCQLParser.g:534:5: lv_type_0_7= ruleDropDatabaseConnection
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
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.DropDatabaseConnection");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 8 :
                    // InternalCQLParser.g:550:5: lv_type_0_8= ruleCreateContextStore
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
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.CreateContextStore");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 9 :
                    // InternalCQLParser.g:566:5: lv_type_0_9= ruleDropContextStore
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
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.DropContextStore");
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


    // $ANTLR start "entryRuleSimpleSelect"
    // InternalCQLParser.g:587:1: entryRuleSimpleSelect returns [EObject current=null] : iv_ruleSimpleSelect= ruleSimpleSelect EOF ;
    public final EObject entryRuleSimpleSelect() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSimpleSelect = null;


        try {
            // InternalCQLParser.g:587:53: (iv_ruleSimpleSelect= ruleSimpleSelect EOF )
            // InternalCQLParser.g:588:2: iv_ruleSimpleSelect= ruleSimpleSelect EOF
            {
             newCompositeNode(grammarAccess.getSimpleSelectRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSimpleSelect=ruleSimpleSelect();

            state._fsp--;

             current =iv_ruleSimpleSelect; 
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
    // $ANTLR end "entryRuleSimpleSelect"


    // $ANTLR start "ruleSimpleSelect"
    // InternalCQLParser.g:594:1: ruleSimpleSelect returns [EObject current=null] : ( () (otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )? ) ) ;
    public final EObject ruleSimpleSelect() throws RecognitionException {
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
            // InternalCQLParser.g:600:2: ( ( () (otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )? ) ) )
            // InternalCQLParser.g:601:2: ( () (otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )? ) )
            {
            // InternalCQLParser.g:601:2: ( () (otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )? ) )
            // InternalCQLParser.g:602:3: () (otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )? )
            {
            // InternalCQLParser.g:602:3: ()
            // InternalCQLParser.g:603:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getSimpleSelectAccess().getSimpleSelectAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:609:3: (otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )? )
            // InternalCQLParser.g:610:4: otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )?
            {
            otherlv_1=(Token)match(input,SELECT,FOLLOW_9); 

            				newLeafNode(otherlv_1, grammarAccess.getSimpleSelectAccess().getSELECTKeyword_1_0());
            			
            // InternalCQLParser.g:614:4: ( (lv_distinct_2_0= DISTINCT ) )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==DISTINCT) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // InternalCQLParser.g:615:5: (lv_distinct_2_0= DISTINCT )
                    {
                    // InternalCQLParser.g:615:5: (lv_distinct_2_0= DISTINCT )
                    // InternalCQLParser.g:616:6: lv_distinct_2_0= DISTINCT
                    {
                    lv_distinct_2_0=(Token)match(input,DISTINCT,FOLLOW_9); 

                    						newLeafNode(lv_distinct_2_0, grammarAccess.getSimpleSelectAccess().getDistinctDISTINCTKeyword_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSimpleSelectRule());
                    						}
                    						setWithLastConsumed(current, "distinct", lv_distinct_2_0, "DISTINCT");
                    					

                    }


                    }
                    break;

            }

            // InternalCQLParser.g:628:4: (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==Asterisk) ) {
                alt12=1;
            }
            else if ( (LA12_0==FALSE||LA12_0==TRUE||LA12_0==DollarSign||(LA12_0>=RULE_INT && LA12_0<=RULE_FLOAT)||(LA12_0>=RULE_VECTOR_FLOAT && LA12_0<=RULE_MATRIX_FLOAT)||LA12_0==RULE_STRING) ) {
                alt12=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // InternalCQLParser.g:629:5: otherlv_3= Asterisk
                    {
                    otherlv_3=(Token)match(input,Asterisk,FOLLOW_10); 

                    					newLeafNode(otherlv_3, grammarAccess.getSimpleSelectAccess().getAsteriskKeyword_1_2_0());
                    				

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:634:5: ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* )
                    {
                    // InternalCQLParser.g:634:5: ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* )
                    // InternalCQLParser.g:635:6: ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )*
                    {
                    // InternalCQLParser.g:635:6: ( (lv_arguments_4_0= ruleSelectArgument ) )+
                    int cnt10=0;
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( (LA10_0==FALSE||LA10_0==TRUE||LA10_0==DollarSign||(LA10_0>=RULE_INT && LA10_0<=RULE_FLOAT)||(LA10_0>=RULE_VECTOR_FLOAT && LA10_0<=RULE_MATRIX_FLOAT)||LA10_0==RULE_STRING) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // InternalCQLParser.g:636:7: (lv_arguments_4_0= ruleSelectArgument )
                    	    {
                    	    // InternalCQLParser.g:636:7: (lv_arguments_4_0= ruleSelectArgument )
                    	    // InternalCQLParser.g:637:8: lv_arguments_4_0= ruleSelectArgument
                    	    {

                    	    								newCompositeNode(grammarAccess.getSimpleSelectAccess().getArgumentsSelectArgumentParserRuleCall_1_2_1_0_0());
                    	    							
                    	    pushFollow(FOLLOW_11);
                    	    lv_arguments_4_0=ruleSelectArgument();

                    	    state._fsp--;


                    	    								if (current==null) {
                    	    									current = createModelElementForParent(grammarAccess.getSimpleSelectRule());
                    	    								}
                    	    								add(
                    	    									current,
                    	    									"arguments",
                    	    									lv_arguments_4_0,
                    	    									"de.uniol.inf.is.odysseus.parser.cql2.CQL.SelectArgument");
                    	    								afterParserOrEnumRuleCall();
                    	    							

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt10 >= 1 ) break loop10;
                                EarlyExitException eee =
                                    new EarlyExitException(10, input);
                                throw eee;
                        }
                        cnt10++;
                    } while (true);

                    // InternalCQLParser.g:654:6: (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )*
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0==Comma) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // InternalCQLParser.g:655:7: otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) )
                    	    {
                    	    otherlv_5=(Token)match(input,Comma,FOLLOW_9); 

                    	    							newLeafNode(otherlv_5, grammarAccess.getSimpleSelectAccess().getCommaKeyword_1_2_1_1_0());
                    	    						
                    	    // InternalCQLParser.g:659:7: ( (lv_arguments_6_0= ruleSelectArgument ) )
                    	    // InternalCQLParser.g:660:8: (lv_arguments_6_0= ruleSelectArgument )
                    	    {
                    	    // InternalCQLParser.g:660:8: (lv_arguments_6_0= ruleSelectArgument )
                    	    // InternalCQLParser.g:661:9: lv_arguments_6_0= ruleSelectArgument
                    	    {

                    	    									newCompositeNode(grammarAccess.getSimpleSelectAccess().getArgumentsSelectArgumentParserRuleCall_1_2_1_1_1_0());
                    	    								
                    	    pushFollow(FOLLOW_12);
                    	    lv_arguments_6_0=ruleSelectArgument();

                    	    state._fsp--;


                    	    									if (current==null) {
                    	    										current = createModelElementForParent(grammarAccess.getSimpleSelectRule());
                    	    									}
                    	    									add(
                    	    										current,
                    	    										"arguments",
                    	    										lv_arguments_6_0,
                    	    										"de.uniol.inf.is.odysseus.parser.cql2.CQL.SelectArgument");
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
                    break;

            }

            // InternalCQLParser.g:681:4: (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* )
            // InternalCQLParser.g:682:5: otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )*
            {
            otherlv_7=(Token)match(input,FROM,FOLLOW_13); 

            					newLeafNode(otherlv_7, grammarAccess.getSimpleSelectAccess().getFROMKeyword_1_3_0());
            				
            // InternalCQLParser.g:686:5: ( (lv_sources_8_0= ruleSource ) )+
            int cnt13=0;
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==LeftParenthesis||LA13_0==RULE_ID) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // InternalCQLParser.g:687:6: (lv_sources_8_0= ruleSource )
            	    {
            	    // InternalCQLParser.g:687:6: (lv_sources_8_0= ruleSource )
            	    // InternalCQLParser.g:688:7: lv_sources_8_0= ruleSource
            	    {

            	    							newCompositeNode(grammarAccess.getSimpleSelectAccess().getSourcesSourceParserRuleCall_1_3_1_0());
            	    						
            	    pushFollow(FOLLOW_14);
            	    lv_sources_8_0=ruleSource();

            	    state._fsp--;


            	    							if (current==null) {
            	    								current = createModelElementForParent(grammarAccess.getSimpleSelectRule());
            	    							}
            	    							add(
            	    								current,
            	    								"sources",
            	    								lv_sources_8_0,
            	    								"de.uniol.inf.is.odysseus.parser.cql2.CQL.Source");
            	    							afterParserOrEnumRuleCall();
            	    						

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

            // InternalCQLParser.g:705:5: (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==Comma) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // InternalCQLParser.g:706:6: otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) )
            	    {
            	    otherlv_9=(Token)match(input,Comma,FOLLOW_13); 

            	    						newLeafNode(otherlv_9, grammarAccess.getSimpleSelectAccess().getCommaKeyword_1_3_2_0());
            	    					
            	    // InternalCQLParser.g:710:6: ( (lv_sources_10_0= ruleSource ) )
            	    // InternalCQLParser.g:711:7: (lv_sources_10_0= ruleSource )
            	    {
            	    // InternalCQLParser.g:711:7: (lv_sources_10_0= ruleSource )
            	    // InternalCQLParser.g:712:8: lv_sources_10_0= ruleSource
            	    {

            	    								newCompositeNode(grammarAccess.getSimpleSelectAccess().getSourcesSourceParserRuleCall_1_3_2_1_0());
            	    							
            	    pushFollow(FOLLOW_15);
            	    lv_sources_10_0=ruleSource();

            	    state._fsp--;


            	    								if (current==null) {
            	    									current = createModelElementForParent(grammarAccess.getSimpleSelectRule());
            	    								}
            	    								add(
            	    									current,
            	    									"sources",
            	    									lv_sources_10_0,
            	    									"de.uniol.inf.is.odysseus.parser.cql2.CQL.Source");
            	    								afterParserOrEnumRuleCall();
            	    							

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);


            }

            // InternalCQLParser.g:731:4: (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==WHERE) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // InternalCQLParser.g:732:5: otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) )
                    {
                    otherlv_11=(Token)match(input,WHERE,FOLLOW_16); 

                    					newLeafNode(otherlv_11, grammarAccess.getSimpleSelectAccess().getWHEREKeyword_1_4_0());
                    				
                    // InternalCQLParser.g:736:5: ( (lv_predicates_12_0= ruleExpressionsModel ) )
                    // InternalCQLParser.g:737:6: (lv_predicates_12_0= ruleExpressionsModel )
                    {
                    // InternalCQLParser.g:737:6: (lv_predicates_12_0= ruleExpressionsModel )
                    // InternalCQLParser.g:738:7: lv_predicates_12_0= ruleExpressionsModel
                    {

                    							newCompositeNode(grammarAccess.getSimpleSelectAccess().getPredicatesExpressionsModelParserRuleCall_1_4_1_0());
                    						
                    pushFollow(FOLLOW_17);
                    lv_predicates_12_0=ruleExpressionsModel();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSimpleSelectRule());
                    							}
                    							set(
                    								current,
                    								"predicates",
                    								lv_predicates_12_0,
                    								"de.uniol.inf.is.odysseus.parser.cql2.CQL.ExpressionsModel");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:756:4: (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==GROUP) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // InternalCQLParser.g:757:5: otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )*
                    {
                    otherlv_13=(Token)match(input,GROUP,FOLLOW_18); 

                    					newLeafNode(otherlv_13, grammarAccess.getSimpleSelectAccess().getGROUPKeyword_1_5_0());
                    				
                    otherlv_14=(Token)match(input,BY,FOLLOW_19); 

                    					newLeafNode(otherlv_14, grammarAccess.getSimpleSelectAccess().getBYKeyword_1_5_1());
                    				
                    // InternalCQLParser.g:765:5: ( (lv_order_15_0= ruleAttribute ) )+
                    int cnt16=0;
                    loop16:
                    do {
                        int alt16=2;
                        int LA16_0 = input.LA(1);

                        if ( (LA16_0==DollarSign||LA16_0==RULE_ID) ) {
                            alt16=1;
                        }


                        switch (alt16) {
                    	case 1 :
                    	    // InternalCQLParser.g:766:6: (lv_order_15_0= ruleAttribute )
                    	    {
                    	    // InternalCQLParser.g:766:6: (lv_order_15_0= ruleAttribute )
                    	    // InternalCQLParser.g:767:7: lv_order_15_0= ruleAttribute
                    	    {

                    	    							newCompositeNode(grammarAccess.getSimpleSelectAccess().getOrderAttributeParserRuleCall_1_5_2_0());
                    	    						
                    	    pushFollow(FOLLOW_20);
                    	    lv_order_15_0=ruleAttribute();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getSimpleSelectRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"order",
                    	    								lv_order_15_0,
                    	    								"de.uniol.inf.is.odysseus.parser.cql2.CQL.Attribute");
                    	    							afterParserOrEnumRuleCall();
                    	    						

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

                    // InternalCQLParser.g:784:5: (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )*
                    loop17:
                    do {
                        int alt17=2;
                        int LA17_0 = input.LA(1);

                        if ( (LA17_0==Comma) ) {
                            alt17=1;
                        }


                        switch (alt17) {
                    	case 1 :
                    	    // InternalCQLParser.g:785:6: otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) )
                    	    {
                    	    otherlv_16=(Token)match(input,Comma,FOLLOW_19); 

                    	    						newLeafNode(otherlv_16, grammarAccess.getSimpleSelectAccess().getCommaKeyword_1_5_3_0());
                    	    					
                    	    // InternalCQLParser.g:789:6: ( (lv_order_17_0= ruleAttribute ) )
                    	    // InternalCQLParser.g:790:7: (lv_order_17_0= ruleAttribute )
                    	    {
                    	    // InternalCQLParser.g:790:7: (lv_order_17_0= ruleAttribute )
                    	    // InternalCQLParser.g:791:8: lv_order_17_0= ruleAttribute
                    	    {

                    	    								newCompositeNode(grammarAccess.getSimpleSelectAccess().getOrderAttributeParserRuleCall_1_5_3_1_0());
                    	    							
                    	    pushFollow(FOLLOW_21);
                    	    lv_order_17_0=ruleAttribute();

                    	    state._fsp--;


                    	    								if (current==null) {
                    	    									current = createModelElementForParent(grammarAccess.getSimpleSelectRule());
                    	    								}
                    	    								add(
                    	    									current,
                    	    									"order",
                    	    									lv_order_17_0,
                    	    									"de.uniol.inf.is.odysseus.parser.cql2.CQL.Attribute");
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
                    break;

            }

            // InternalCQLParser.g:810:4: (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==HAVING) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // InternalCQLParser.g:811:5: otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) )
                    {
                    otherlv_18=(Token)match(input,HAVING,FOLLOW_16); 

                    					newLeafNode(otherlv_18, grammarAccess.getSimpleSelectAccess().getHAVINGKeyword_1_6_0());
                    				
                    // InternalCQLParser.g:815:5: ( (lv_having_19_0= ruleExpressionsModel ) )
                    // InternalCQLParser.g:816:6: (lv_having_19_0= ruleExpressionsModel )
                    {
                    // InternalCQLParser.g:816:6: (lv_having_19_0= ruleExpressionsModel )
                    // InternalCQLParser.g:817:7: lv_having_19_0= ruleExpressionsModel
                    {

                    							newCompositeNode(grammarAccess.getSimpleSelectAccess().getHavingExpressionsModelParserRuleCall_1_6_1_0());
                    						
                    pushFollow(FOLLOW_2);
                    lv_having_19_0=ruleExpressionsModel();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSimpleSelectRule());
                    							}
                    							set(
                    								current,
                    								"having",
                    								lv_having_19_0,
                    								"de.uniol.inf.is.odysseus.parser.cql2.CQL.ExpressionsModel");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }


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
    // $ANTLR end "ruleSimpleSelect"


    // $ANTLR start "entryRuleComplexSelect"
    // InternalCQLParser.g:840:1: entryRuleComplexSelect returns [EObject current=null] : iv_ruleComplexSelect= ruleComplexSelect EOF ;
    public final EObject entryRuleComplexSelect() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComplexSelect = null;


        try {
            // InternalCQLParser.g:840:54: (iv_ruleComplexSelect= ruleComplexSelect EOF )
            // InternalCQLParser.g:841:2: iv_ruleComplexSelect= ruleComplexSelect EOF
            {
             newCompositeNode(grammarAccess.getComplexSelectRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleComplexSelect=ruleComplexSelect();

            state._fsp--;

             current =iv_ruleComplexSelect; 
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
    // $ANTLR end "entryRuleComplexSelect"


    // $ANTLR start "ruleComplexSelect"
    // InternalCQLParser.g:847:1: ruleComplexSelect returns [EObject current=null] : ( () ( (lv_left_1_0= ruleSimpleSelect ) ) ( ( ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) ) ) ( (lv_right_3_0= ruleSimpleSelect ) ) )? ) ;
    public final EObject ruleComplexSelect() throws RecognitionException {
        EObject current = null;

        Token lv_operation_2_1=null;
        Token lv_operation_2_2=null;
        Token lv_operation_2_3=null;
        EObject lv_left_1_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:853:2: ( ( () ( (lv_left_1_0= ruleSimpleSelect ) ) ( ( ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) ) ) ( (lv_right_3_0= ruleSimpleSelect ) ) )? ) )
            // InternalCQLParser.g:854:2: ( () ( (lv_left_1_0= ruleSimpleSelect ) ) ( ( ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) ) ) ( (lv_right_3_0= ruleSimpleSelect ) ) )? )
            {
            // InternalCQLParser.g:854:2: ( () ( (lv_left_1_0= ruleSimpleSelect ) ) ( ( ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) ) ) ( (lv_right_3_0= ruleSimpleSelect ) ) )? )
            // InternalCQLParser.g:855:3: () ( (lv_left_1_0= ruleSimpleSelect ) ) ( ( ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) ) ) ( (lv_right_3_0= ruleSimpleSelect ) ) )?
            {
            // InternalCQLParser.g:855:3: ()
            // InternalCQLParser.g:856:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getComplexSelectAccess().getComplexSelectAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:862:3: ( (lv_left_1_0= ruleSimpleSelect ) )
            // InternalCQLParser.g:863:4: (lv_left_1_0= ruleSimpleSelect )
            {
            // InternalCQLParser.g:863:4: (lv_left_1_0= ruleSimpleSelect )
            // InternalCQLParser.g:864:5: lv_left_1_0= ruleSimpleSelect
            {

            					newCompositeNode(grammarAccess.getComplexSelectAccess().getLeftSimpleSelectParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_22);
            lv_left_1_0=ruleSimpleSelect();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getComplexSelectRule());
            					}
            					set(
            						current,
            						"left",
            						lv_left_1_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.SimpleSelect");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:881:3: ( ( ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) ) ) ( (lv_right_3_0= ruleSimpleSelect ) ) )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==INTERSECTION||LA21_0==DIFFERENCE||LA21_0==UNION) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // InternalCQLParser.g:882:4: ( ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) ) ) ( (lv_right_3_0= ruleSimpleSelect ) )
                    {
                    // InternalCQLParser.g:882:4: ( ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) ) )
                    // InternalCQLParser.g:883:5: ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) )
                    {
                    // InternalCQLParser.g:883:5: ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) )
                    // InternalCQLParser.g:884:6: (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION )
                    {
                    // InternalCQLParser.g:884:6: (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION )
                    int alt20=3;
                    switch ( input.LA(1) ) {
                    case UNION:
                        {
                        alt20=1;
                        }
                        break;
                    case DIFFERENCE:
                        {
                        alt20=2;
                        }
                        break;
                    case INTERSECTION:
                        {
                        alt20=3;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 20, 0, input);

                        throw nvae;
                    }

                    switch (alt20) {
                        case 1 :
                            // InternalCQLParser.g:885:7: lv_operation_2_1= UNION
                            {
                            lv_operation_2_1=(Token)match(input,UNION,FOLLOW_23); 

                            							newLeafNode(lv_operation_2_1, grammarAccess.getComplexSelectAccess().getOperationUNIONKeyword_2_0_0_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getComplexSelectRule());
                            							}
                            							setWithLastConsumed(current, "operation", lv_operation_2_1, null);
                            						

                            }
                            break;
                        case 2 :
                            // InternalCQLParser.g:896:7: lv_operation_2_2= DIFFERENCE
                            {
                            lv_operation_2_2=(Token)match(input,DIFFERENCE,FOLLOW_23); 

                            							newLeafNode(lv_operation_2_2, grammarAccess.getComplexSelectAccess().getOperationDIFFERENCEKeyword_2_0_0_1());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getComplexSelectRule());
                            							}
                            							setWithLastConsumed(current, "operation", lv_operation_2_2, null);
                            						

                            }
                            break;
                        case 3 :
                            // InternalCQLParser.g:907:7: lv_operation_2_3= INTERSECTION
                            {
                            lv_operation_2_3=(Token)match(input,INTERSECTION,FOLLOW_23); 

                            							newLeafNode(lv_operation_2_3, grammarAccess.getComplexSelectAccess().getOperationINTERSECTIONKeyword_2_0_0_2());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getComplexSelectRule());
                            							}
                            							setWithLastConsumed(current, "operation", lv_operation_2_3, null);
                            						

                            }
                            break;

                    }


                    }


                    }

                    // InternalCQLParser.g:920:4: ( (lv_right_3_0= ruleSimpleSelect ) )
                    // InternalCQLParser.g:921:5: (lv_right_3_0= ruleSimpleSelect )
                    {
                    // InternalCQLParser.g:921:5: (lv_right_3_0= ruleSimpleSelect )
                    // InternalCQLParser.g:922:6: lv_right_3_0= ruleSimpleSelect
                    {

                    						newCompositeNode(grammarAccess.getComplexSelectAccess().getRightSimpleSelectParserRuleCall_2_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_right_3_0=ruleSimpleSelect();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getComplexSelectRule());
                    						}
                    						set(
                    							current,
                    							"right",
                    							lv_right_3_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.SimpleSelect");
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
    // $ANTLR end "ruleComplexSelect"


    // $ANTLR start "entryRuleInnerSelect"
    // InternalCQLParser.g:944:1: entryRuleInnerSelect returns [EObject current=null] : iv_ruleInnerSelect= ruleInnerSelect EOF ;
    public final EObject entryRuleInnerSelect() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInnerSelect = null;


        try {
            // InternalCQLParser.g:944:52: (iv_ruleInnerSelect= ruleInnerSelect EOF )
            // InternalCQLParser.g:945:2: iv_ruleInnerSelect= ruleInnerSelect EOF
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
    // InternalCQLParser.g:951:1: ruleInnerSelect returns [EObject current=null] : (otherlv_0= LeftParenthesis ( (lv_select_1_0= ruleSimpleSelect ) ) otherlv_2= RightParenthesis ) ;
    public final EObject ruleInnerSelect() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject lv_select_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:957:2: ( (otherlv_0= LeftParenthesis ( (lv_select_1_0= ruleSimpleSelect ) ) otherlv_2= RightParenthesis ) )
            // InternalCQLParser.g:958:2: (otherlv_0= LeftParenthesis ( (lv_select_1_0= ruleSimpleSelect ) ) otherlv_2= RightParenthesis )
            {
            // InternalCQLParser.g:958:2: (otherlv_0= LeftParenthesis ( (lv_select_1_0= ruleSimpleSelect ) ) otherlv_2= RightParenthesis )
            // InternalCQLParser.g:959:3: otherlv_0= LeftParenthesis ( (lv_select_1_0= ruleSimpleSelect ) ) otherlv_2= RightParenthesis
            {
            otherlv_0=(Token)match(input,LeftParenthesis,FOLLOW_23); 

            			newLeafNode(otherlv_0, grammarAccess.getInnerSelectAccess().getLeftParenthesisKeyword_0());
            		
            // InternalCQLParser.g:963:3: ( (lv_select_1_0= ruleSimpleSelect ) )
            // InternalCQLParser.g:964:4: (lv_select_1_0= ruleSimpleSelect )
            {
            // InternalCQLParser.g:964:4: (lv_select_1_0= ruleSimpleSelect )
            // InternalCQLParser.g:965:5: lv_select_1_0= ruleSimpleSelect
            {

            					newCompositeNode(grammarAccess.getInnerSelectAccess().getSelectSimpleSelectParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_24);
            lv_select_1_0=ruleSimpleSelect();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getInnerSelectRule());
            					}
            					set(
            						current,
            						"select",
            						lv_select_1_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.SimpleSelect");
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
    // InternalCQLParser.g:990:1: entryRuleInnerSelect2 returns [EObject current=null] : iv_ruleInnerSelect2= ruleInnerSelect2 EOF ;
    public final EObject entryRuleInnerSelect2() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInnerSelect2 = null;


        try {
            // InternalCQLParser.g:990:53: (iv_ruleInnerSelect2= ruleInnerSelect2 EOF )
            // InternalCQLParser.g:991:2: iv_ruleInnerSelect2= ruleInnerSelect2 EOF
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
    // InternalCQLParser.g:997:1: ruleInnerSelect2 returns [EObject current=null] : ( (lv_select_0_0= ruleSimpleSelect ) ) ;
    public final EObject ruleInnerSelect2() throws RecognitionException {
        EObject current = null;

        EObject lv_select_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1003:2: ( ( (lv_select_0_0= ruleSimpleSelect ) ) )
            // InternalCQLParser.g:1004:2: ( (lv_select_0_0= ruleSimpleSelect ) )
            {
            // InternalCQLParser.g:1004:2: ( (lv_select_0_0= ruleSimpleSelect ) )
            // InternalCQLParser.g:1005:3: (lv_select_0_0= ruleSimpleSelect )
            {
            // InternalCQLParser.g:1005:3: (lv_select_0_0= ruleSimpleSelect )
            // InternalCQLParser.g:1006:4: lv_select_0_0= ruleSimpleSelect
            {

            				newCompositeNode(grammarAccess.getInnerSelect2Access().getSelectSimpleSelectParserRuleCall_0());
            			
            pushFollow(FOLLOW_2);
            lv_select_0_0=ruleSimpleSelect();

            state._fsp--;


            				if (current==null) {
            					current = createModelElementForParent(grammarAccess.getInnerSelect2Rule());
            				}
            				set(
            					current,
            					"select",
            					lv_select_0_0,
            					"de.uniol.inf.is.odysseus.parser.cql2.CQL.SimpleSelect");
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
    // InternalCQLParser.g:1026:1: entryRuleSelectArgument returns [EObject current=null] : iv_ruleSelectArgument= ruleSelectArgument EOF ;
    public final EObject entryRuleSelectArgument() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectArgument = null;


        try {
            // InternalCQLParser.g:1026:55: (iv_ruleSelectArgument= ruleSelectArgument EOF )
            // InternalCQLParser.g:1027:2: iv_ruleSelectArgument= ruleSelectArgument EOF
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
    // InternalCQLParser.g:1033:1: ruleSelectArgument returns [EObject current=null] : ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) ) ;
    public final EObject ruleSelectArgument() throws RecognitionException {
        EObject current = null;

        EObject lv_attribute_0_0 = null;

        EObject lv_expression_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1039:2: ( ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) ) )
            // InternalCQLParser.g:1040:2: ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) )
            {
            // InternalCQLParser.g:1040:2: ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) )
            int alt22=2;
            switch ( input.LA(1) ) {
            case RULE_ID:
                {
                switch ( input.LA(2) ) {
                case FullStop:
                    {
                    int LA22_4 = input.LA(3);

                    if ( (LA22_4==RULE_ID) ) {
                        int LA22_5 = input.LA(4);

                        if ( ((LA22_5>=Asterisk && LA22_5<=PlusSign)||LA22_5==HyphenMinus||LA22_5==Solidus||LA22_5==CircumflexAccent) ) {
                            alt22=2;
                        }
                        else if ( (LA22_5==EOF||LA22_5==FALSE||LA22_5==FROM||LA22_5==TRUE||LA22_5==AS||LA22_5==DollarSign||LA22_5==Comma||(LA22_5>=RULE_INT && LA22_5<=RULE_FLOAT)||(LA22_5>=RULE_VECTOR_FLOAT && LA22_5<=RULE_MATRIX_FLOAT)||LA22_5==RULE_STRING) ) {
                            alt22=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 22, 5, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA22_4==Asterisk) ) {
                        alt22=1;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 22, 4, input);

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
                    alt22=2;
                    }
                    break;
                case EOF:
                case FALSE:
                case FROM:
                case TRUE:
                case AS:
                case DollarSign:
                case Comma:
                case RULE_INT:
                case RULE_ID:
                case RULE_FLOAT:
                case RULE_VECTOR_FLOAT:
                case RULE_MATRIX_FLOAT:
                case RULE_STRING:
                    {
                    alt22=1;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 22, 1, input);

                    throw nvae;
                }

                }
                break;
            case DollarSign:
                {
                alt22=1;
                }
                break;
            case FALSE:
            case TRUE:
            case RULE_INT:
            case RULE_FLOAT:
            case RULE_VECTOR_FLOAT:
            case RULE_MATRIX_FLOAT:
            case RULE_STRING:
                {
                alt22=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;
            }

            switch (alt22) {
                case 1 :
                    // InternalCQLParser.g:1041:3: ( (lv_attribute_0_0= ruleAttribute ) )
                    {
                    // InternalCQLParser.g:1041:3: ( (lv_attribute_0_0= ruleAttribute ) )
                    // InternalCQLParser.g:1042:4: (lv_attribute_0_0= ruleAttribute )
                    {
                    // InternalCQLParser.g:1042:4: (lv_attribute_0_0= ruleAttribute )
                    // InternalCQLParser.g:1043:5: lv_attribute_0_0= ruleAttribute
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
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.Attribute");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1061:3: ( (lv_expression_1_0= ruleSelectExpression ) )
                    {
                    // InternalCQLParser.g:1061:3: ( (lv_expression_1_0= ruleSelectExpression ) )
                    // InternalCQLParser.g:1062:4: (lv_expression_1_0= ruleSelectExpression )
                    {
                    // InternalCQLParser.g:1062:4: (lv_expression_1_0= ruleSelectExpression )
                    // InternalCQLParser.g:1063:5: lv_expression_1_0= ruleSelectExpression
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
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.SelectExpression");
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
    // InternalCQLParser.g:1084:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalCQLParser.g:1084:47: (iv_ruleSource= ruleSource EOF )
            // InternalCQLParser.g:1085:2: iv_ruleSource= ruleSource EOF
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
    // InternalCQLParser.g:1091:1: ruleSource returns [EObject current=null] : (this_SimpleSource_0= ruleSimpleSource | this_NestedSource_1= ruleNestedSource ) ;
    public final EObject ruleSource() throws RecognitionException {
        EObject current = null;

        EObject this_SimpleSource_0 = null;

        EObject this_NestedSource_1 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1097:2: ( (this_SimpleSource_0= ruleSimpleSource | this_NestedSource_1= ruleNestedSource ) )
            // InternalCQLParser.g:1098:2: (this_SimpleSource_0= ruleSimpleSource | this_NestedSource_1= ruleNestedSource )
            {
            // InternalCQLParser.g:1098:2: (this_SimpleSource_0= ruleSimpleSource | this_NestedSource_1= ruleNestedSource )
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==RULE_ID) ) {
                alt23=1;
            }
            else if ( (LA23_0==LeftParenthesis) ) {
                alt23=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;
            }
            switch (alt23) {
                case 1 :
                    // InternalCQLParser.g:1099:3: this_SimpleSource_0= ruleSimpleSource
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
                    // InternalCQLParser.g:1108:3: this_NestedSource_1= ruleNestedSource
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
    // InternalCQLParser.g:1120:1: entryRuleSimpleSource returns [EObject current=null] : iv_ruleSimpleSource= ruleSimpleSource EOF ;
    public final EObject entryRuleSimpleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSimpleSource = null;


        try {
            // InternalCQLParser.g:1120:53: (iv_ruleSimpleSource= ruleSimpleSource EOF )
            // InternalCQLParser.g:1121:2: iv_ruleSimpleSource= ruleSimpleSource EOF
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
    // InternalCQLParser.g:1127:1: ruleSimpleSource returns [EObject current=null] : ( () ( (lv_name_1_0= ruleQualifiedSourcename ) ) ( (lv_window_2_0= ruleWindowOperator ) )? (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )? ) ;
    public final EObject ruleSimpleSource() throws RecognitionException {
        EObject current = null;

        Token otherlv_3=null;
        AntlrDatatypeRuleToken lv_name_1_0 = null;

        EObject lv_window_2_0 = null;

        EObject lv_alias_4_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1133:2: ( ( () ( (lv_name_1_0= ruleQualifiedSourcename ) ) ( (lv_window_2_0= ruleWindowOperator ) )? (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:1134:2: ( () ( (lv_name_1_0= ruleQualifiedSourcename ) ) ( (lv_window_2_0= ruleWindowOperator ) )? (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:1134:2: ( () ( (lv_name_1_0= ruleQualifiedSourcename ) ) ( (lv_window_2_0= ruleWindowOperator ) )? (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:1135:3: () ( (lv_name_1_0= ruleQualifiedSourcename ) ) ( (lv_window_2_0= ruleWindowOperator ) )? (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:1135:3: ()
            // InternalCQLParser.g:1136:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getSimpleSourceAccess().getSimpleSourceAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:1142:3: ( (lv_name_1_0= ruleQualifiedSourcename ) )
            // InternalCQLParser.g:1143:4: (lv_name_1_0= ruleQualifiedSourcename )
            {
            // InternalCQLParser.g:1143:4: (lv_name_1_0= ruleQualifiedSourcename )
            // InternalCQLParser.g:1144:5: lv_name_1_0= ruleQualifiedSourcename
            {

            					newCompositeNode(grammarAccess.getSimpleSourceAccess().getNameQualifiedSourcenameParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_25);
            lv_name_1_0=ruleQualifiedSourcename();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSimpleSourceRule());
            					}
            					set(
            						current,
            						"name",
            						lv_name_1_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.QualifiedSourcename");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:1161:3: ( (lv_window_2_0= ruleWindowOperator ) )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==LeftSquareBracket) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // InternalCQLParser.g:1162:4: (lv_window_2_0= ruleWindowOperator )
                    {
                    // InternalCQLParser.g:1162:4: (lv_window_2_0= ruleWindowOperator )
                    // InternalCQLParser.g:1163:5: lv_window_2_0= ruleWindowOperator
                    {

                    					newCompositeNode(grammarAccess.getSimpleSourceAccess().getWindowWindowOperatorParserRuleCall_2_0());
                    				
                    pushFollow(FOLLOW_26);
                    lv_window_2_0=ruleWindowOperator();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSimpleSourceRule());
                    					}
                    					set(
                    						current,
                    						"window",
                    						lv_window_2_0,
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.WindowOperator");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalCQLParser.g:1180:3: (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==AS) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // InternalCQLParser.g:1181:4: otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) )
                    {
                    otherlv_3=(Token)match(input,AS,FOLLOW_5); 

                    				newLeafNode(otherlv_3, grammarAccess.getSimpleSourceAccess().getASKeyword_3_0());
                    			
                    // InternalCQLParser.g:1185:4: ( (lv_alias_4_0= ruleAlias ) )
                    // InternalCQLParser.g:1186:5: (lv_alias_4_0= ruleAlias )
                    {
                    // InternalCQLParser.g:1186:5: (lv_alias_4_0= ruleAlias )
                    // InternalCQLParser.g:1187:6: lv_alias_4_0= ruleAlias
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.Alias");
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
    // InternalCQLParser.g:1209:1: entryRuleNestedSource returns [EObject current=null] : iv_ruleNestedSource= ruleNestedSource EOF ;
    public final EObject entryRuleNestedSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNestedSource = null;


        try {
            // InternalCQLParser.g:1209:53: (iv_ruleNestedSource= ruleNestedSource EOF )
            // InternalCQLParser.g:1210:2: iv_ruleNestedSource= ruleNestedSource EOF
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
    // InternalCQLParser.g:1216:1: ruleNestedSource returns [EObject current=null] : ( () ( (lv_statement_1_0= ruleInnerSelect ) ) otherlv_2= AS ( (lv_alias_3_0= ruleAlias ) ) ) ;
    public final EObject ruleNestedSource() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject lv_statement_1_0 = null;

        EObject lv_alias_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1222:2: ( ( () ( (lv_statement_1_0= ruleInnerSelect ) ) otherlv_2= AS ( (lv_alias_3_0= ruleAlias ) ) ) )
            // InternalCQLParser.g:1223:2: ( () ( (lv_statement_1_0= ruleInnerSelect ) ) otherlv_2= AS ( (lv_alias_3_0= ruleAlias ) ) )
            {
            // InternalCQLParser.g:1223:2: ( () ( (lv_statement_1_0= ruleInnerSelect ) ) otherlv_2= AS ( (lv_alias_3_0= ruleAlias ) ) )
            // InternalCQLParser.g:1224:3: () ( (lv_statement_1_0= ruleInnerSelect ) ) otherlv_2= AS ( (lv_alias_3_0= ruleAlias ) )
            {
            // InternalCQLParser.g:1224:3: ()
            // InternalCQLParser.g:1225:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getNestedSourceAccess().getNestedSourceAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:1231:3: ( (lv_statement_1_0= ruleInnerSelect ) )
            // InternalCQLParser.g:1232:4: (lv_statement_1_0= ruleInnerSelect )
            {
            // InternalCQLParser.g:1232:4: (lv_statement_1_0= ruleInnerSelect )
            // InternalCQLParser.g:1233:5: lv_statement_1_0= ruleInnerSelect
            {

            					newCompositeNode(grammarAccess.getNestedSourceAccess().getStatementInnerSelectParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_27);
            lv_statement_1_0=ruleInnerSelect();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getNestedSourceRule());
            					}
            					set(
            						current,
            						"statement",
            						lv_statement_1_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.InnerSelect");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_2=(Token)match(input,AS,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getNestedSourceAccess().getASKeyword_2());
            		
            // InternalCQLParser.g:1254:3: ( (lv_alias_3_0= ruleAlias ) )
            // InternalCQLParser.g:1255:4: (lv_alias_3_0= ruleAlias )
            {
            // InternalCQLParser.g:1255:4: (lv_alias_3_0= ruleAlias )
            // InternalCQLParser.g:1256:5: lv_alias_3_0= ruleAlias
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
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.Alias");
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


    // $ANTLR start "entryRuleAttribute"
    // InternalCQLParser.g:1277:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCQLParser.g:1277:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCQLParser.g:1278:2: iv_ruleAttribute= ruleAttribute EOF
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
    // InternalCQLParser.g:1284:1: ruleAttribute returns [EObject current=null] : ( ( (lv_name_0_0= ruleQualifiedAttributename ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_name_0_0 = null;

        EObject lv_alias_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1290:2: ( ( ( (lv_name_0_0= ruleQualifiedAttributename ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:1291:2: ( ( (lv_name_0_0= ruleQualifiedAttributename ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:1291:2: ( ( (lv_name_0_0= ruleQualifiedAttributename ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:1292:3: ( (lv_name_0_0= ruleQualifiedAttributename ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:1292:3: ( (lv_name_0_0= ruleQualifiedAttributename ) )
            // InternalCQLParser.g:1293:4: (lv_name_0_0= ruleQualifiedAttributename )
            {
            // InternalCQLParser.g:1293:4: (lv_name_0_0= ruleQualifiedAttributename )
            // InternalCQLParser.g:1294:5: lv_name_0_0= ruleQualifiedAttributename
            {

            					newCompositeNode(grammarAccess.getAttributeAccess().getNameQualifiedAttributenameParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_26);
            lv_name_0_0=ruleQualifiedAttributename();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAttributeRule());
            					}
            					set(
            						current,
            						"name",
            						lv_name_0_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.QualifiedAttributename");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:1311:3: (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==AS) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // InternalCQLParser.g:1312:4: otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) )
                    {
                    otherlv_1=(Token)match(input,AS,FOLLOW_5); 

                    				newLeafNode(otherlv_1, grammarAccess.getAttributeAccess().getASKeyword_1_0());
                    			
                    // InternalCQLParser.g:1316:4: ( (lv_alias_2_0= ruleAlias ) )
                    // InternalCQLParser.g:1317:5: (lv_alias_2_0= ruleAlias )
                    {
                    // InternalCQLParser.g:1317:5: (lv_alias_2_0= ruleAlias )
                    // InternalCQLParser.g:1318:6: lv_alias_2_0= ruleAlias
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.Alias");
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
    // InternalCQLParser.g:1340:1: entryRuleAttributeWithoutAliasDefinition returns [EObject current=null] : iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF ;
    public final EObject entryRuleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithoutAliasDefinition = null;


        try {
            // InternalCQLParser.g:1340:72: (iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF )
            // InternalCQLParser.g:1341:2: iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF
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
    // InternalCQLParser.g:1347:1: ruleAttributeWithoutAliasDefinition returns [EObject current=null] : ( (lv_name_0_0= ruleQualifiedAttributename ) ) ;
    public final EObject ruleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_name_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1353:2: ( ( (lv_name_0_0= ruleQualifiedAttributename ) ) )
            // InternalCQLParser.g:1354:2: ( (lv_name_0_0= ruleQualifiedAttributename ) )
            {
            // InternalCQLParser.g:1354:2: ( (lv_name_0_0= ruleQualifiedAttributename ) )
            // InternalCQLParser.g:1355:3: (lv_name_0_0= ruleQualifiedAttributename )
            {
            // InternalCQLParser.g:1355:3: (lv_name_0_0= ruleQualifiedAttributename )
            // InternalCQLParser.g:1356:4: lv_name_0_0= ruleQualifiedAttributename
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
            					"de.uniol.inf.is.odysseus.parser.cql2.CQL.QualifiedAttributename");
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


    // $ANTLR start "entryRuleAttributeForSelectExpression"
    // InternalCQLParser.g:1376:1: entryRuleAttributeForSelectExpression returns [EObject current=null] : iv_ruleAttributeForSelectExpression= ruleAttributeForSelectExpression EOF ;
    public final EObject entryRuleAttributeForSelectExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeForSelectExpression = null;


        try {
            // InternalCQLParser.g:1376:69: (iv_ruleAttributeForSelectExpression= ruleAttributeForSelectExpression EOF )
            // InternalCQLParser.g:1377:2: iv_ruleAttributeForSelectExpression= ruleAttributeForSelectExpression EOF
            {
             newCompositeNode(grammarAccess.getAttributeForSelectExpressionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAttributeForSelectExpression=ruleAttributeForSelectExpression();

            state._fsp--;

             current =iv_ruleAttributeForSelectExpression; 
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
    // $ANTLR end "entryRuleAttributeForSelectExpression"


    // $ANTLR start "ruleAttributeForSelectExpression"
    // InternalCQLParser.g:1383:1: ruleAttributeForSelectExpression returns [EObject current=null] : ( (lv_name_0_0= ruleQualifiedAttributenameWithoutSpecialChars ) ) ;
    public final EObject ruleAttributeForSelectExpression() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_name_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1389:2: ( ( (lv_name_0_0= ruleQualifiedAttributenameWithoutSpecialChars ) ) )
            // InternalCQLParser.g:1390:2: ( (lv_name_0_0= ruleQualifiedAttributenameWithoutSpecialChars ) )
            {
            // InternalCQLParser.g:1390:2: ( (lv_name_0_0= ruleQualifiedAttributenameWithoutSpecialChars ) )
            // InternalCQLParser.g:1391:3: (lv_name_0_0= ruleQualifiedAttributenameWithoutSpecialChars )
            {
            // InternalCQLParser.g:1391:3: (lv_name_0_0= ruleQualifiedAttributenameWithoutSpecialChars )
            // InternalCQLParser.g:1392:4: lv_name_0_0= ruleQualifiedAttributenameWithoutSpecialChars
            {

            				newCompositeNode(grammarAccess.getAttributeForSelectExpressionAccess().getNameQualifiedAttributenameWithoutSpecialCharsParserRuleCall_0());
            			
            pushFollow(FOLLOW_2);
            lv_name_0_0=ruleQualifiedAttributenameWithoutSpecialChars();

            state._fsp--;


            				if (current==null) {
            					current = createModelElementForParent(grammarAccess.getAttributeForSelectExpressionRule());
            				}
            				set(
            					current,
            					"name",
            					lv_name_0_0,
            					"de.uniol.inf.is.odysseus.parser.cql2.CQL.QualifiedAttributenameWithoutSpecialChars");
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
    // $ANTLR end "ruleAttributeForSelectExpression"


    // $ANTLR start "entryRuleComplexPredicate"
    // InternalCQLParser.g:1412:1: entryRuleComplexPredicate returns [EObject current=null] : iv_ruleComplexPredicate= ruleComplexPredicate EOF ;
    public final EObject entryRuleComplexPredicate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComplexPredicate = null;


        try {
            // InternalCQLParser.g:1412:57: (iv_ruleComplexPredicate= ruleComplexPredicate EOF )
            // InternalCQLParser.g:1413:2: iv_ruleComplexPredicate= ruleComplexPredicate EOF
            {
             newCompositeNode(grammarAccess.getComplexPredicateRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleComplexPredicate=ruleComplexPredicate();

            state._fsp--;

             current =iv_ruleComplexPredicate; 
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
    // $ANTLR end "entryRuleComplexPredicate"


    // $ANTLR start "ruleComplexPredicate"
    // InternalCQLParser.g:1419:1: ruleComplexPredicate returns [EObject current=null] : ( ( ( (lv_quantification_0_0= ruleQuantificationPredicate ) ) | ( (lv_exists_1_0= ruleExistPredicate ) ) | ( (lv_in_2_0= ruleInPredicate ) ) ) ( (lv_select_3_0= ruleInnerSelect ) ) ) ;
    public final EObject ruleComplexPredicate() throws RecognitionException {
        EObject current = null;

        EObject lv_quantification_0_0 = null;

        EObject lv_exists_1_0 = null;

        EObject lv_in_2_0 = null;

        EObject lv_select_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1425:2: ( ( ( ( (lv_quantification_0_0= ruleQuantificationPredicate ) ) | ( (lv_exists_1_0= ruleExistPredicate ) ) | ( (lv_in_2_0= ruleInPredicate ) ) ) ( (lv_select_3_0= ruleInnerSelect ) ) ) )
            // InternalCQLParser.g:1426:2: ( ( ( (lv_quantification_0_0= ruleQuantificationPredicate ) ) | ( (lv_exists_1_0= ruleExistPredicate ) ) | ( (lv_in_2_0= ruleInPredicate ) ) ) ( (lv_select_3_0= ruleInnerSelect ) ) )
            {
            // InternalCQLParser.g:1426:2: ( ( ( (lv_quantification_0_0= ruleQuantificationPredicate ) ) | ( (lv_exists_1_0= ruleExistPredicate ) ) | ( (lv_in_2_0= ruleInPredicate ) ) ) ( (lv_select_3_0= ruleInnerSelect ) ) )
            // InternalCQLParser.g:1427:3: ( ( (lv_quantification_0_0= ruleQuantificationPredicate ) ) | ( (lv_exists_1_0= ruleExistPredicate ) ) | ( (lv_in_2_0= ruleInPredicate ) ) ) ( (lv_select_3_0= ruleInnerSelect ) )
            {
            // InternalCQLParser.g:1427:3: ( ( (lv_quantification_0_0= ruleQuantificationPredicate ) ) | ( (lv_exists_1_0= ruleExistPredicate ) ) | ( (lv_in_2_0= ruleInPredicate ) ) )
            int alt27=3;
            alt27 = dfa27.predict(input);
            switch (alt27) {
                case 1 :
                    // InternalCQLParser.g:1428:4: ( (lv_quantification_0_0= ruleQuantificationPredicate ) )
                    {
                    // InternalCQLParser.g:1428:4: ( (lv_quantification_0_0= ruleQuantificationPredicate ) )
                    // InternalCQLParser.g:1429:5: (lv_quantification_0_0= ruleQuantificationPredicate )
                    {
                    // InternalCQLParser.g:1429:5: (lv_quantification_0_0= ruleQuantificationPredicate )
                    // InternalCQLParser.g:1430:6: lv_quantification_0_0= ruleQuantificationPredicate
                    {

                    						newCompositeNode(grammarAccess.getComplexPredicateAccess().getQuantificationQuantificationPredicateParserRuleCall_0_0_0());
                    					
                    pushFollow(FOLLOW_13);
                    lv_quantification_0_0=ruleQuantificationPredicate();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getComplexPredicateRule());
                    						}
                    						set(
                    							current,
                    							"quantification",
                    							lv_quantification_0_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.QuantificationPredicate");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1448:4: ( (lv_exists_1_0= ruleExistPredicate ) )
                    {
                    // InternalCQLParser.g:1448:4: ( (lv_exists_1_0= ruleExistPredicate ) )
                    // InternalCQLParser.g:1449:5: (lv_exists_1_0= ruleExistPredicate )
                    {
                    // InternalCQLParser.g:1449:5: (lv_exists_1_0= ruleExistPredicate )
                    // InternalCQLParser.g:1450:6: lv_exists_1_0= ruleExistPredicate
                    {

                    						newCompositeNode(grammarAccess.getComplexPredicateAccess().getExistsExistPredicateParserRuleCall_0_1_0());
                    					
                    pushFollow(FOLLOW_13);
                    lv_exists_1_0=ruleExistPredicate();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getComplexPredicateRule());
                    						}
                    						set(
                    							current,
                    							"exists",
                    							lv_exists_1_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ExistPredicate");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:1468:4: ( (lv_in_2_0= ruleInPredicate ) )
                    {
                    // InternalCQLParser.g:1468:4: ( (lv_in_2_0= ruleInPredicate ) )
                    // InternalCQLParser.g:1469:5: (lv_in_2_0= ruleInPredicate )
                    {
                    // InternalCQLParser.g:1469:5: (lv_in_2_0= ruleInPredicate )
                    // InternalCQLParser.g:1470:6: lv_in_2_0= ruleInPredicate
                    {

                    						newCompositeNode(grammarAccess.getComplexPredicateAccess().getInInPredicateParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_13);
                    lv_in_2_0=ruleInPredicate();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getComplexPredicateRule());
                    						}
                    						set(
                    							current,
                    							"in",
                    							lv_in_2_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.InPredicate");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:1488:3: ( (lv_select_3_0= ruleInnerSelect ) )
            // InternalCQLParser.g:1489:4: (lv_select_3_0= ruleInnerSelect )
            {
            // InternalCQLParser.g:1489:4: (lv_select_3_0= ruleInnerSelect )
            // InternalCQLParser.g:1490:5: lv_select_3_0= ruleInnerSelect
            {

            					newCompositeNode(grammarAccess.getComplexPredicateAccess().getSelectInnerSelectParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_select_3_0=ruleInnerSelect();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getComplexPredicateRule());
            					}
            					set(
            						current,
            						"select",
            						lv_select_3_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.InnerSelect");
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
    // $ANTLR end "ruleComplexPredicate"


    // $ANTLR start "entryRuleQuantificationPredicate"
    // InternalCQLParser.g:1511:1: entryRuleQuantificationPredicate returns [EObject current=null] : iv_ruleQuantificationPredicate= ruleQuantificationPredicate EOF ;
    public final EObject entryRuleQuantificationPredicate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleQuantificationPredicate = null;


        try {
            // InternalCQLParser.g:1511:64: (iv_ruleQuantificationPredicate= ruleQuantificationPredicate EOF )
            // InternalCQLParser.g:1512:2: iv_ruleQuantificationPredicate= ruleQuantificationPredicate EOF
            {
             newCompositeNode(grammarAccess.getQuantificationPredicateRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleQuantificationPredicate=ruleQuantificationPredicate();

            state._fsp--;

             current =iv_ruleQuantificationPredicate; 
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
    // $ANTLR end "entryRuleQuantificationPredicate"


    // $ANTLR start "ruleQuantificationPredicate"
    // InternalCQLParser.g:1518:1: ruleQuantificationPredicate returns [EObject current=null] : ( ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_operator_1_0= ruleCOMPARE_OPERATOR ) ) ( ( (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME ) ) ) ) ;
    public final EObject ruleQuantificationPredicate() throws RecognitionException {
        EObject current = null;

        Token lv_predicate_2_1=null;
        Token lv_predicate_2_2=null;
        Token lv_predicate_2_3=null;
        EObject lv_attribute_0_0 = null;

        AntlrDatatypeRuleToken lv_operator_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1524:2: ( ( ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_operator_1_0= ruleCOMPARE_OPERATOR ) ) ( ( (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME ) ) ) ) )
            // InternalCQLParser.g:1525:2: ( ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_operator_1_0= ruleCOMPARE_OPERATOR ) ) ( ( (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME ) ) ) )
            {
            // InternalCQLParser.g:1525:2: ( ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_operator_1_0= ruleCOMPARE_OPERATOR ) ) ( ( (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME ) ) ) )
            // InternalCQLParser.g:1526:3: ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_operator_1_0= ruleCOMPARE_OPERATOR ) ) ( ( (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME ) ) )
            {
            // InternalCQLParser.g:1526:3: ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQLParser.g:1527:4: (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1527:4: (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQLParser.g:1528:5: lv_attribute_0_0= ruleAttributeWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getQuantificationPredicateAccess().getAttributeAttributeWithoutAliasDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_28);
            lv_attribute_0_0=ruleAttributeWithoutAliasDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getQuantificationPredicateRule());
            					}
            					set(
            						current,
            						"attribute",
            						lv_attribute_0_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.AttributeWithoutAliasDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:1545:3: ( (lv_operator_1_0= ruleCOMPARE_OPERATOR ) )
            // InternalCQLParser.g:1546:4: (lv_operator_1_0= ruleCOMPARE_OPERATOR )
            {
            // InternalCQLParser.g:1546:4: (lv_operator_1_0= ruleCOMPARE_OPERATOR )
            // InternalCQLParser.g:1547:5: lv_operator_1_0= ruleCOMPARE_OPERATOR
            {

            					newCompositeNode(grammarAccess.getQuantificationPredicateAccess().getOperatorCOMPARE_OPERATORParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_29);
            lv_operator_1_0=ruleCOMPARE_OPERATOR();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getQuantificationPredicateRule());
            					}
            					set(
            						current,
            						"operator",
            						lv_operator_1_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.COMPARE_OPERATOR");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:1564:3: ( ( (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME ) ) )
            // InternalCQLParser.g:1565:4: ( (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME ) )
            {
            // InternalCQLParser.g:1565:4: ( (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME ) )
            // InternalCQLParser.g:1566:5: (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME )
            {
            // InternalCQLParser.g:1566:5: (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME )
            int alt28=3;
            switch ( input.LA(1) ) {
            case ALL:
                {
                alt28=1;
                }
                break;
            case ANY:
                {
                alt28=2;
                }
                break;
            case SOME:
                {
                alt28=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 28, 0, input);

                throw nvae;
            }

            switch (alt28) {
                case 1 :
                    // InternalCQLParser.g:1567:6: lv_predicate_2_1= ALL
                    {
                    lv_predicate_2_1=(Token)match(input,ALL,FOLLOW_2); 

                    						newLeafNode(lv_predicate_2_1, grammarAccess.getQuantificationPredicateAccess().getPredicateALLKeyword_2_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getQuantificationPredicateRule());
                    						}
                    						setWithLastConsumed(current, "predicate", lv_predicate_2_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1578:6: lv_predicate_2_2= ANY
                    {
                    lv_predicate_2_2=(Token)match(input,ANY,FOLLOW_2); 

                    						newLeafNode(lv_predicate_2_2, grammarAccess.getQuantificationPredicateAccess().getPredicateANYKeyword_2_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getQuantificationPredicateRule());
                    						}
                    						setWithLastConsumed(current, "predicate", lv_predicate_2_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:1589:6: lv_predicate_2_3= SOME
                    {
                    lv_predicate_2_3=(Token)match(input,SOME,FOLLOW_2); 

                    						newLeafNode(lv_predicate_2_3, grammarAccess.getQuantificationPredicateAccess().getPredicateSOMEKeyword_2_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getQuantificationPredicateRule());
                    						}
                    						setWithLastConsumed(current, "predicate", lv_predicate_2_3, null);
                    					

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
    // $ANTLR end "ruleQuantificationPredicate"


    // $ANTLR start "entryRuleExistPredicate"
    // InternalCQLParser.g:1606:1: entryRuleExistPredicate returns [EObject current=null] : iv_ruleExistPredicate= ruleExistPredicate EOF ;
    public final EObject entryRuleExistPredicate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExistPredicate = null;


        try {
            // InternalCQLParser.g:1606:55: (iv_ruleExistPredicate= ruleExistPredicate EOF )
            // InternalCQLParser.g:1607:2: iv_ruleExistPredicate= ruleExistPredicate EOF
            {
             newCompositeNode(grammarAccess.getExistPredicateRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExistPredicate=ruleExistPredicate();

            state._fsp--;

             current =iv_ruleExistPredicate; 
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
    // $ANTLR end "entryRuleExistPredicate"


    // $ANTLR start "ruleExistPredicate"
    // InternalCQLParser.g:1613:1: ruleExistPredicate returns [EObject current=null] : ( (lv_predicate_0_0= EXISTS ) ) ;
    public final EObject ruleExistPredicate() throws RecognitionException {
        EObject current = null;

        Token lv_predicate_0_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1619:2: ( ( (lv_predicate_0_0= EXISTS ) ) )
            // InternalCQLParser.g:1620:2: ( (lv_predicate_0_0= EXISTS ) )
            {
            // InternalCQLParser.g:1620:2: ( (lv_predicate_0_0= EXISTS ) )
            // InternalCQLParser.g:1621:3: (lv_predicate_0_0= EXISTS )
            {
            // InternalCQLParser.g:1621:3: (lv_predicate_0_0= EXISTS )
            // InternalCQLParser.g:1622:4: lv_predicate_0_0= EXISTS
            {
            lv_predicate_0_0=(Token)match(input,EXISTS,FOLLOW_2); 

            				newLeafNode(lv_predicate_0_0, grammarAccess.getExistPredicateAccess().getPredicateEXISTSKeyword_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getExistPredicateRule());
            				}
            				setWithLastConsumed(current, "predicate", lv_predicate_0_0, "EXISTS");
            			

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
    // $ANTLR end "ruleExistPredicate"


    // $ANTLR start "entryRuleInPredicate"
    // InternalCQLParser.g:1637:1: entryRuleInPredicate returns [EObject current=null] : iv_ruleInPredicate= ruleInPredicate EOF ;
    public final EObject entryRuleInPredicate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInPredicate = null;


        try {
            // InternalCQLParser.g:1637:52: (iv_ruleInPredicate= ruleInPredicate EOF )
            // InternalCQLParser.g:1638:2: iv_ruleInPredicate= ruleInPredicate EOF
            {
             newCompositeNode(grammarAccess.getInPredicateRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleInPredicate=ruleInPredicate();

            state._fsp--;

             current =iv_ruleInPredicate; 
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
    // $ANTLR end "entryRuleInPredicate"


    // $ANTLR start "ruleInPredicate"
    // InternalCQLParser.g:1644:1: ruleInPredicate returns [EObject current=null] : ( ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_predicate_1_0= IN ) ) ) ;
    public final EObject ruleInPredicate() throws RecognitionException {
        EObject current = null;

        Token lv_predicate_1_0=null;
        EObject lv_attribute_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1650:2: ( ( ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_predicate_1_0= IN ) ) ) )
            // InternalCQLParser.g:1651:2: ( ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_predicate_1_0= IN ) ) )
            {
            // InternalCQLParser.g:1651:2: ( ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_predicate_1_0= IN ) ) )
            // InternalCQLParser.g:1652:3: ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_predicate_1_0= IN ) )
            {
            // InternalCQLParser.g:1652:3: ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQLParser.g:1653:4: (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1653:4: (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQLParser.g:1654:5: lv_attribute_0_0= ruleAttributeWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getInPredicateAccess().getAttributeAttributeWithoutAliasDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_30);
            lv_attribute_0_0=ruleAttributeWithoutAliasDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getInPredicateRule());
            					}
            					set(
            						current,
            						"attribute",
            						lv_attribute_0_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.AttributeWithoutAliasDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:1671:3: ( (lv_predicate_1_0= IN ) )
            // InternalCQLParser.g:1672:4: (lv_predicate_1_0= IN )
            {
            // InternalCQLParser.g:1672:4: (lv_predicate_1_0= IN )
            // InternalCQLParser.g:1673:5: lv_predicate_1_0= IN
            {
            lv_predicate_1_0=(Token)match(input,IN,FOLLOW_2); 

            					newLeafNode(lv_predicate_1_0, grammarAccess.getInPredicateAccess().getPredicateINKeyword_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getInPredicateRule());
            					}
            					setWithLastConsumed(current, "predicate", lv_predicate_1_0, "IN");
            				

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
    // $ANTLR end "ruleInPredicate"


    // $ANTLR start "entryRuleAndOperator"
    // InternalCQLParser.g:1689:1: entryRuleAndOperator returns [String current=null] : iv_ruleAndOperator= ruleAndOperator EOF ;
    public final String entryRuleAndOperator() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleAndOperator = null;


        try {
            // InternalCQLParser.g:1689:51: (iv_ruleAndOperator= ruleAndOperator EOF )
            // InternalCQLParser.g:1690:2: iv_ruleAndOperator= ruleAndOperator EOF
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
    // InternalCQLParser.g:1696:1: ruleAndOperator returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= AND ;
    public final AntlrDatatypeRuleToken ruleAndOperator() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1702:2: (kw= AND )
            // InternalCQLParser.g:1703:2: kw= AND
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
    // InternalCQLParser.g:1711:1: entryRuleOrOperator returns [String current=null] : iv_ruleOrOperator= ruleOrOperator EOF ;
    public final String entryRuleOrOperator() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOrOperator = null;


        try {
            // InternalCQLParser.g:1711:50: (iv_ruleOrOperator= ruleOrOperator EOF )
            // InternalCQLParser.g:1712:2: iv_ruleOrOperator= ruleOrOperator EOF
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
    // InternalCQLParser.g:1718:1: ruleOrOperator returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= OR ;
    public final AntlrDatatypeRuleToken ruleOrOperator() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1724:2: (kw= OR )
            // InternalCQLParser.g:1725:2: kw= OR
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
    // InternalCQLParser.g:1733:1: entryRuleEQUALITIY_OPERATOR returns [String current=null] : iv_ruleEQUALITIY_OPERATOR= ruleEQUALITIY_OPERATOR EOF ;
    public final String entryRuleEQUALITIY_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleEQUALITIY_OPERATOR = null;


        try {
            // InternalCQLParser.g:1733:58: (iv_ruleEQUALITIY_OPERATOR= ruleEQUALITIY_OPERATOR EOF )
            // InternalCQLParser.g:1734:2: iv_ruleEQUALITIY_OPERATOR= ruleEQUALITIY_OPERATOR EOF
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
    // InternalCQLParser.g:1740:1: ruleEQUALITIY_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= EqualsSign | kw= ExclamationMarkEqualsSign ) ;
    public final AntlrDatatypeRuleToken ruleEQUALITIY_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1746:2: ( (kw= EqualsSign | kw= ExclamationMarkEqualsSign ) )
            // InternalCQLParser.g:1747:2: (kw= EqualsSign | kw= ExclamationMarkEqualsSign )
            {
            // InternalCQLParser.g:1747:2: (kw= EqualsSign | kw= ExclamationMarkEqualsSign )
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==EqualsSign) ) {
                alt29=1;
            }
            else if ( (LA29_0==ExclamationMarkEqualsSign) ) {
                alt29=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;
            }
            switch (alt29) {
                case 1 :
                    // InternalCQLParser.g:1748:3: kw= EqualsSign
                    {
                    kw=(Token)match(input,EqualsSign,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getEQUALITIY_OPERATORAccess().getEqualsSignKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1754:3: kw= ExclamationMarkEqualsSign
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
    // InternalCQLParser.g:1763:1: entryRuleCOMPARE_OPERATOR returns [String current=null] : iv_ruleCOMPARE_OPERATOR= ruleCOMPARE_OPERATOR EOF ;
    public final String entryRuleCOMPARE_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleCOMPARE_OPERATOR = null;


        try {
            // InternalCQLParser.g:1763:56: (iv_ruleCOMPARE_OPERATOR= ruleCOMPARE_OPERATOR EOF )
            // InternalCQLParser.g:1764:2: iv_ruleCOMPARE_OPERATOR= ruleCOMPARE_OPERATOR EOF
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
    // InternalCQLParser.g:1770:1: ruleCOMPARE_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= GreaterThanSignEqualsSign | kw= LessThanSignEqualsSign | kw= LessThanSign | kw= GreaterThanSign ) ;
    public final AntlrDatatypeRuleToken ruleCOMPARE_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1776:2: ( (kw= GreaterThanSignEqualsSign | kw= LessThanSignEqualsSign | kw= LessThanSign | kw= GreaterThanSign ) )
            // InternalCQLParser.g:1777:2: (kw= GreaterThanSignEqualsSign | kw= LessThanSignEqualsSign | kw= LessThanSign | kw= GreaterThanSign )
            {
            // InternalCQLParser.g:1777:2: (kw= GreaterThanSignEqualsSign | kw= LessThanSignEqualsSign | kw= LessThanSign | kw= GreaterThanSign )
            int alt30=4;
            switch ( input.LA(1) ) {
            case GreaterThanSignEqualsSign:
                {
                alt30=1;
                }
                break;
            case LessThanSignEqualsSign:
                {
                alt30=2;
                }
                break;
            case LessThanSign:
                {
                alt30=3;
                }
                break;
            case GreaterThanSign:
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
                    // InternalCQLParser.g:1778:3: kw= GreaterThanSignEqualsSign
                    {
                    kw=(Token)match(input,GreaterThanSignEqualsSign,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getCOMPARE_OPERATORAccess().getGreaterThanSignEqualsSignKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1784:3: kw= LessThanSignEqualsSign
                    {
                    kw=(Token)match(input,LessThanSignEqualsSign,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getCOMPARE_OPERATORAccess().getLessThanSignEqualsSignKeyword_1());
                    		

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:1790:3: kw= LessThanSign
                    {
                    kw=(Token)match(input,LessThanSign,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getCOMPARE_OPERATORAccess().getLessThanSignKeyword_2());
                    		

                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:1796:3: kw= GreaterThanSign
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
    // InternalCQLParser.g:1805:1: entryRuleARITHMETIC_OPERATOR returns [String current=null] : iv_ruleARITHMETIC_OPERATOR= ruleARITHMETIC_OPERATOR EOF ;
    public final String entryRuleARITHMETIC_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleARITHMETIC_OPERATOR = null;


        try {
            // InternalCQLParser.g:1805:59: (iv_ruleARITHMETIC_OPERATOR= ruleARITHMETIC_OPERATOR EOF )
            // InternalCQLParser.g:1806:2: iv_ruleARITHMETIC_OPERATOR= ruleARITHMETIC_OPERATOR EOF
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
    // InternalCQLParser.g:1812:1: ruleARITHMETIC_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ADD_OPERATOR_0= ruleADD_OPERATOR | this_MINUS_OPERATOR_1= ruleMINUS_OPERATOR | this_MUL_OR_DIV_OPERATOR_2= ruleMUL_OR_DIV_OPERATOR | this_EXPONENT_OPERATOR_3= ruleEXPONENT_OPERATOR ) ;
    public final AntlrDatatypeRuleToken ruleARITHMETIC_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        AntlrDatatypeRuleToken this_ADD_OPERATOR_0 = null;

        AntlrDatatypeRuleToken this_MINUS_OPERATOR_1 = null;

        AntlrDatatypeRuleToken this_MUL_OR_DIV_OPERATOR_2 = null;

        AntlrDatatypeRuleToken this_EXPONENT_OPERATOR_3 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1818:2: ( (this_ADD_OPERATOR_0= ruleADD_OPERATOR | this_MINUS_OPERATOR_1= ruleMINUS_OPERATOR | this_MUL_OR_DIV_OPERATOR_2= ruleMUL_OR_DIV_OPERATOR | this_EXPONENT_OPERATOR_3= ruleEXPONENT_OPERATOR ) )
            // InternalCQLParser.g:1819:2: (this_ADD_OPERATOR_0= ruleADD_OPERATOR | this_MINUS_OPERATOR_1= ruleMINUS_OPERATOR | this_MUL_OR_DIV_OPERATOR_2= ruleMUL_OR_DIV_OPERATOR | this_EXPONENT_OPERATOR_3= ruleEXPONENT_OPERATOR )
            {
            // InternalCQLParser.g:1819:2: (this_ADD_OPERATOR_0= ruleADD_OPERATOR | this_MINUS_OPERATOR_1= ruleMINUS_OPERATOR | this_MUL_OR_DIV_OPERATOR_2= ruleMUL_OR_DIV_OPERATOR | this_EXPONENT_OPERATOR_3= ruleEXPONENT_OPERATOR )
            int alt31=4;
            switch ( input.LA(1) ) {
            case PlusSign:
                {
                alt31=1;
                }
                break;
            case HyphenMinus:
                {
                alt31=2;
                }
                break;
            case Asterisk:
            case Solidus:
                {
                alt31=3;
                }
                break;
            case CircumflexAccent:
                {
                alt31=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 31, 0, input);

                throw nvae;
            }

            switch (alt31) {
                case 1 :
                    // InternalCQLParser.g:1820:3: this_ADD_OPERATOR_0= ruleADD_OPERATOR
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
                    // InternalCQLParser.g:1831:3: this_MINUS_OPERATOR_1= ruleMINUS_OPERATOR
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
                    // InternalCQLParser.g:1842:3: this_MUL_OR_DIV_OPERATOR_2= ruleMUL_OR_DIV_OPERATOR
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
                    // InternalCQLParser.g:1853:3: this_EXPONENT_OPERATOR_3= ruleEXPONENT_OPERATOR
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
    // InternalCQLParser.g:1867:1: entryRuleEXPONENT_OPERATOR returns [String current=null] : iv_ruleEXPONENT_OPERATOR= ruleEXPONENT_OPERATOR EOF ;
    public final String entryRuleEXPONENT_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleEXPONENT_OPERATOR = null;


        try {
            // InternalCQLParser.g:1867:57: (iv_ruleEXPONENT_OPERATOR= ruleEXPONENT_OPERATOR EOF )
            // InternalCQLParser.g:1868:2: iv_ruleEXPONENT_OPERATOR= ruleEXPONENT_OPERATOR EOF
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
    // InternalCQLParser.g:1874:1: ruleEXPONENT_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= CircumflexAccent ;
    public final AntlrDatatypeRuleToken ruleEXPONENT_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1880:2: (kw= CircumflexAccent )
            // InternalCQLParser.g:1881:2: kw= CircumflexAccent
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
    // InternalCQLParser.g:1889:1: entryRuleMUL_OR_DIV_OPERATOR returns [String current=null] : iv_ruleMUL_OR_DIV_OPERATOR= ruleMUL_OR_DIV_OPERATOR EOF ;
    public final String entryRuleMUL_OR_DIV_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleMUL_OR_DIV_OPERATOR = null;


        try {
            // InternalCQLParser.g:1889:59: (iv_ruleMUL_OR_DIV_OPERATOR= ruleMUL_OR_DIV_OPERATOR EOF )
            // InternalCQLParser.g:1890:2: iv_ruleMUL_OR_DIV_OPERATOR= ruleMUL_OR_DIV_OPERATOR EOF
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
    // InternalCQLParser.g:1896:1: ruleMUL_OR_DIV_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= Solidus | kw= Asterisk ) ;
    public final AntlrDatatypeRuleToken ruleMUL_OR_DIV_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1902:2: ( (kw= Solidus | kw= Asterisk ) )
            // InternalCQLParser.g:1903:2: (kw= Solidus | kw= Asterisk )
            {
            // InternalCQLParser.g:1903:2: (kw= Solidus | kw= Asterisk )
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==Solidus) ) {
                alt32=1;
            }
            else if ( (LA32_0==Asterisk) ) {
                alt32=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }
            switch (alt32) {
                case 1 :
                    // InternalCQLParser.g:1904:3: kw= Solidus
                    {
                    kw=(Token)match(input,Solidus,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getMUL_OR_DIV_OPERATORAccess().getSolidusKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1910:3: kw= Asterisk
                    {
                    kw=(Token)match(input,Asterisk,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getMUL_OR_DIV_OPERATORAccess().getAsteriskKeyword_1());
                    		

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
    // InternalCQLParser.g:1919:1: entryRuleADD_OPERATOR returns [String current=null] : iv_ruleADD_OPERATOR= ruleADD_OPERATOR EOF ;
    public final String entryRuleADD_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleADD_OPERATOR = null;


        try {
            // InternalCQLParser.g:1919:52: (iv_ruleADD_OPERATOR= ruleADD_OPERATOR EOF )
            // InternalCQLParser.g:1920:2: iv_ruleADD_OPERATOR= ruleADD_OPERATOR EOF
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
    // InternalCQLParser.g:1926:1: ruleADD_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= PlusSign ;
    public final AntlrDatatypeRuleToken ruleADD_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1932:2: (kw= PlusSign )
            // InternalCQLParser.g:1933:2: kw= PlusSign
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
    // InternalCQLParser.g:1941:1: entryRuleMINUS_OPERATOR returns [String current=null] : iv_ruleMINUS_OPERATOR= ruleMINUS_OPERATOR EOF ;
    public final String entryRuleMINUS_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleMINUS_OPERATOR = null;


        try {
            // InternalCQLParser.g:1941:54: (iv_ruleMINUS_OPERATOR= ruleMINUS_OPERATOR EOF )
            // InternalCQLParser.g:1942:2: iv_ruleMINUS_OPERATOR= ruleMINUS_OPERATOR EOF
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
    // InternalCQLParser.g:1948:1: ruleMINUS_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= HyphenMinus ;
    public final AntlrDatatypeRuleToken ruleMINUS_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1954:2: (kw= HyphenMinus )
            // InternalCQLParser.g:1955:2: kw= HyphenMinus
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
    // InternalCQLParser.g:1963:1: entryRuleSelectExpression returns [EObject current=null] : iv_ruleSelectExpression= ruleSelectExpression EOF ;
    public final EObject entryRuleSelectExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpression = null;


        try {
            // InternalCQLParser.g:1963:57: (iv_ruleSelectExpression= ruleSelectExpression EOF )
            // InternalCQLParser.g:1964:2: iv_ruleSelectExpression= ruleSelectExpression EOF
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
    // InternalCQLParser.g:1970:1: ruleSelectExpression returns [EObject current=null] : ( ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) ) ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) ;
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
            // InternalCQLParser.g:1976:2: ( ( ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) ) ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:1977:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) ) ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:1977:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) ) ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:1978:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) ) ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:1978:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) ) ) )
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==RULE_ID) ) {
                int LA36_1 = input.LA(2);

                if ( (LA36_1==LeftParenthesis) ) {
                    alt36=1;
                }
                else if ( ((LA36_1>=Asterisk && LA36_1<=PlusSign)||(LA36_1>=HyphenMinus && LA36_1<=Solidus)||LA36_1==CircumflexAccent) ) {
                    alt36=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 36, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA36_0==FALSE||LA36_0==TRUE||LA36_0==RULE_INT||LA36_0==RULE_FLOAT||(LA36_0>=RULE_VECTOR_FLOAT && LA36_0<=RULE_MATRIX_FLOAT)||LA36_0==RULE_STRING) ) {
                alt36=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 36, 0, input);

                throw nvae;
            }
            switch (alt36) {
                case 1 :
                    // InternalCQLParser.g:1979:4: ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* )
                    {
                    // InternalCQLParser.g:1979:4: ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* )
                    // InternalCQLParser.g:1980:5: ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )*
                    {
                    // InternalCQLParser.g:1980:5: ( (lv_expressions_0_0= ruleExpressionComponent ) )
                    // InternalCQLParser.g:1981:6: (lv_expressions_0_0= ruleExpressionComponent )
                    {
                    // InternalCQLParser.g:1981:6: (lv_expressions_0_0= ruleExpressionComponent )
                    // InternalCQLParser.g:1982:7: lv_expressions_0_0= ruleExpressionComponent
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentParserRuleCall_0_0_0_0());
                    						
                    pushFollow(FOLLOW_31);
                    lv_expressions_0_0=ruleExpressionComponent();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    							}
                    							add(
                    								current,
                    								"expressions",
                    								lv_expressions_0_0,
                    								"de.uniol.inf.is.odysseus.parser.cql2.CQL.ExpressionComponent");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCQLParser.g:1999:5: ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )*
                    loop34:
                    do {
                        int alt34=2;
                        int LA34_0 = input.LA(1);

                        if ( ((LA34_0>=Asterisk && LA34_0<=PlusSign)||LA34_0==HyphenMinus||LA34_0==Solidus||LA34_0==CircumflexAccent) ) {
                            alt34=1;
                        }


                        switch (alt34) {
                    	case 1 :
                    	    // InternalCQLParser.g:2000:6: ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) )
                    	    {
                    	    // InternalCQLParser.g:2000:6: ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) )
                    	    // InternalCQLParser.g:2001:7: (lv_operators_1_0= ruleARITHMETIC_OPERATOR )
                    	    {
                    	    // InternalCQLParser.g:2001:7: (lv_operators_1_0= ruleARITHMETIC_OPERATOR )
                    	    // InternalCQLParser.g:2002:8: lv_operators_1_0= ruleARITHMETIC_OPERATOR
                    	    {

                    	    								newCompositeNode(grammarAccess.getSelectExpressionAccess().getOperatorsARITHMETIC_OPERATORParserRuleCall_0_0_1_0_0());
                    	    							
                    	    pushFollow(FOLLOW_9);
                    	    lv_operators_1_0=ruleARITHMETIC_OPERATOR();

                    	    state._fsp--;


                    	    								if (current==null) {
                    	    									current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	    								}
                    	    								add(
                    	    									current,
                    	    									"operators",
                    	    									lv_operators_1_0,
                    	    									"de.uniol.inf.is.odysseus.parser.cql2.CQL.ARITHMETIC_OPERATOR");
                    	    								afterParserOrEnumRuleCall();
                    	    							

                    	    }


                    	    }

                    	    // InternalCQLParser.g:2019:6: ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) )
                    	    // InternalCQLParser.g:2020:7: ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) )
                    	    {
                    	    // InternalCQLParser.g:2020:7: ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) )
                    	    // InternalCQLParser.g:2021:8: (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute )
                    	    {
                    	    // InternalCQLParser.g:2021:8: (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute )
                    	    int alt33=2;
                    	    int LA33_0 = input.LA(1);

                    	    if ( (LA33_0==RULE_ID) ) {
                    	        int LA33_1 = input.LA(2);

                    	        if ( (LA33_1==LeftParenthesis) ) {
                    	            alt33=1;
                    	        }
                    	        else if ( (LA33_1==EOF||LA33_1==FALSE||LA33_1==FROM||LA33_1==TRUE||LA33_1==AS||LA33_1==DollarSign||(LA33_1>=RightParenthesis && LA33_1<=Solidus)||LA33_1==CircumflexAccent||(LA33_1>=RULE_INT && LA33_1<=RULE_FLOAT)||(LA33_1>=RULE_VECTOR_FLOAT && LA33_1<=RULE_MATRIX_FLOAT)||LA33_1==RULE_STRING) ) {
                    	            alt33=2;
                    	        }
                    	        else {
                    	            NoViableAltException nvae =
                    	                new NoViableAltException("", 33, 1, input);

                    	            throw nvae;
                    	        }
                    	    }
                    	    else if ( (LA33_0==FALSE||LA33_0==TRUE||LA33_0==RULE_INT||LA33_0==RULE_FLOAT||(LA33_0>=RULE_VECTOR_FLOAT && LA33_0<=RULE_MATRIX_FLOAT)||LA33_0==RULE_STRING) ) {
                    	        alt33=1;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 33, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt33) {
                    	        case 1 :
                    	            // InternalCQLParser.g:2022:9: lv_expressions_2_1= ruleExpressionComponent
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentParserRuleCall_0_0_1_1_0_0());
                    	            								
                    	            pushFollow(FOLLOW_31);
                    	            lv_expressions_2_1=ruleExpressionComponent();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_2_1,
                    	            										"de.uniol.inf.is.odysseus.parser.cql2.CQL.ExpressionComponent");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:2038:9: lv_expressions_2_2= ruleExpressionComponentAsAttribute
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentAsAttributeParserRuleCall_0_0_1_1_0_1());
                    	            								
                    	            pushFollow(FOLLOW_31);
                    	            lv_expressions_2_2=ruleExpressionComponentAsAttribute();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_2_2,
                    	            										"de.uniol.inf.is.odysseus.parser.cql2.CQL.ExpressionComponentAsAttribute");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop34;
                        }
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:2059:4: ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) ) )
                    {
                    // InternalCQLParser.g:2059:4: ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) ) )
                    // InternalCQLParser.g:2060:5: ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) )
                    {
                    // InternalCQLParser.g:2060:5: ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) )
                    // InternalCQLParser.g:2061:6: (lv_expressions_3_0= ruleExpressionComponentAsAttribute )
                    {
                    // InternalCQLParser.g:2061:6: (lv_expressions_3_0= ruleExpressionComponentAsAttribute )
                    // InternalCQLParser.g:2062:7: lv_expressions_3_0= ruleExpressionComponentAsAttribute
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentAsAttributeParserRuleCall_0_1_0_0());
                    						
                    pushFollow(FOLLOW_32);
                    lv_expressions_3_0=ruleExpressionComponentAsAttribute();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    							}
                    							add(
                    								current,
                    								"expressions",
                    								lv_expressions_3_0,
                    								"de.uniol.inf.is.odysseus.parser.cql2.CQL.ExpressionComponentAsAttribute");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCQLParser.g:2079:5: ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) )
                    // InternalCQLParser.g:2080:6: ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) )
                    {
                    // InternalCQLParser.g:2080:6: ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) )
                    // InternalCQLParser.g:2081:7: (lv_operators_4_0= ruleARITHMETIC_OPERATOR )
                    {
                    // InternalCQLParser.g:2081:7: (lv_operators_4_0= ruleARITHMETIC_OPERATOR )
                    // InternalCQLParser.g:2082:8: lv_operators_4_0= ruleARITHMETIC_OPERATOR
                    {

                    								newCompositeNode(grammarAccess.getSelectExpressionAccess().getOperatorsARITHMETIC_OPERATORParserRuleCall_0_1_1_0_0());
                    							
                    pushFollow(FOLLOW_9);
                    lv_operators_4_0=ruleARITHMETIC_OPERATOR();

                    state._fsp--;


                    								if (current==null) {
                    									current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    								}
                    								add(
                    									current,
                    									"operators",
                    									lv_operators_4_0,
                    									"de.uniol.inf.is.odysseus.parser.cql2.CQL.ARITHMETIC_OPERATOR");
                    								afterParserOrEnumRuleCall();
                    							

                    }


                    }

                    // InternalCQLParser.g:2099:6: ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) )
                    // InternalCQLParser.g:2100:7: ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) )
                    {
                    // InternalCQLParser.g:2100:7: ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) )
                    // InternalCQLParser.g:2101:8: (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute )
                    {
                    // InternalCQLParser.g:2101:8: (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute )
                    int alt35=2;
                    int LA35_0 = input.LA(1);

                    if ( (LA35_0==RULE_ID) ) {
                        int LA35_1 = input.LA(2);

                        if ( (LA35_1==LeftParenthesis) ) {
                            alt35=1;
                        }
                        else if ( (LA35_1==EOF||LA35_1==FALSE||LA35_1==FROM||LA35_1==TRUE||LA35_1==AS||LA35_1==DollarSign||LA35_1==RightParenthesis||LA35_1==Comma||LA35_1==FullStop||(LA35_1>=RULE_INT && LA35_1<=RULE_FLOAT)||(LA35_1>=RULE_VECTOR_FLOAT && LA35_1<=RULE_MATRIX_FLOAT)||LA35_1==RULE_STRING) ) {
                            alt35=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 35, 1, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA35_0==FALSE||LA35_0==TRUE||LA35_0==RULE_INT||LA35_0==RULE_FLOAT||(LA35_0>=RULE_VECTOR_FLOAT && LA35_0<=RULE_MATRIX_FLOAT)||LA35_0==RULE_STRING) ) {
                        alt35=1;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 35, 0, input);

                        throw nvae;
                    }
                    switch (alt35) {
                        case 1 :
                            // InternalCQLParser.g:2102:9: lv_expressions_5_1= ruleExpressionComponent
                            {

                            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentParserRuleCall_0_1_1_1_0_0());
                            								
                            pushFollow(FOLLOW_26);
                            lv_expressions_5_1=ruleExpressionComponent();

                            state._fsp--;


                            									if (current==null) {
                            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                            									}
                            									add(
                            										current,
                            										"expressions",
                            										lv_expressions_5_1,
                            										"de.uniol.inf.is.odysseus.parser.cql2.CQL.ExpressionComponent");
                            									afterParserOrEnumRuleCall();
                            								

                            }
                            break;
                        case 2 :
                            // InternalCQLParser.g:2118:9: lv_expressions_5_2= ruleExpressionComponentAsAttribute
                            {

                            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentAsAttributeParserRuleCall_0_1_1_1_0_1());
                            								
                            pushFollow(FOLLOW_26);
                            lv_expressions_5_2=ruleExpressionComponentAsAttribute();

                            state._fsp--;


                            									if (current==null) {
                            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                            									}
                            									add(
                            										current,
                            										"expressions",
                            										lv_expressions_5_2,
                            										"de.uniol.inf.is.odysseus.parser.cql2.CQL.ExpressionComponentAsAttribute");
                            									afterParserOrEnumRuleCall();
                            								

                            }
                            break;

                    }


                    }


                    }


                    }


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:2139:3: (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==AS) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // InternalCQLParser.g:2140:4: otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) )
                    {
                    otherlv_6=(Token)match(input,AS,FOLLOW_5); 

                    				newLeafNode(otherlv_6, grammarAccess.getSelectExpressionAccess().getASKeyword_1_0());
                    			
                    // InternalCQLParser.g:2144:4: ( (lv_alias_7_0= ruleAlias ) )
                    // InternalCQLParser.g:2145:5: (lv_alias_7_0= ruleAlias )
                    {
                    // InternalCQLParser.g:2145:5: (lv_alias_7_0= ruleAlias )
                    // InternalCQLParser.g:2146:6: lv_alias_7_0= ruleAlias
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.Alias");
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
    // InternalCQLParser.g:2168:1: entryRuleSelectExpressionOnlyWithAttribute returns [EObject current=null] : iv_ruleSelectExpressionOnlyWithAttribute= ruleSelectExpressionOnlyWithAttribute EOF ;
    public final EObject entryRuleSelectExpressionOnlyWithAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionOnlyWithAttribute = null;


        try {
            // InternalCQLParser.g:2168:74: (iv_ruleSelectExpressionOnlyWithAttribute= ruleSelectExpressionOnlyWithAttribute EOF )
            // InternalCQLParser.g:2169:2: iv_ruleSelectExpressionOnlyWithAttribute= ruleSelectExpressionOnlyWithAttribute EOF
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
    // InternalCQLParser.g:2175:1: ruleSelectExpressionOnlyWithAttribute returns [EObject current=null] : ( (lv_expressions_0_0= ruleExpressionComponentAsAttribute ) ) ;
    public final EObject ruleSelectExpressionOnlyWithAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_expressions_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2181:2: ( ( (lv_expressions_0_0= ruleExpressionComponentAsAttribute ) ) )
            // InternalCQLParser.g:2182:2: ( (lv_expressions_0_0= ruleExpressionComponentAsAttribute ) )
            {
            // InternalCQLParser.g:2182:2: ( (lv_expressions_0_0= ruleExpressionComponentAsAttribute ) )
            // InternalCQLParser.g:2183:3: (lv_expressions_0_0= ruleExpressionComponentAsAttribute )
            {
            // InternalCQLParser.g:2183:3: (lv_expressions_0_0= ruleExpressionComponentAsAttribute )
            // InternalCQLParser.g:2184:4: lv_expressions_0_0= ruleExpressionComponentAsAttribute
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
            					"de.uniol.inf.is.odysseus.parser.cql2.CQL.ExpressionComponentAsAttribute");
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
    // InternalCQLParser.g:2204:1: entryRuleFunction returns [EObject current=null] : iv_ruleFunction= ruleFunction EOF ;
    public final EObject entryRuleFunction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFunction = null;


        try {
            // InternalCQLParser.g:2204:49: (iv_ruleFunction= ruleFunction EOF )
            // InternalCQLParser.g:2205:2: iv_ruleFunction= ruleFunction EOF
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
    // InternalCQLParser.g:2211:1: ruleFunction returns [EObject current=null] : ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression ) ) ) otherlv_4= RightParenthesis ) ;
    public final EObject ruleFunction() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_value_3_1 = null;

        EObject lv_value_3_2 = null;

        EObject lv_value_3_3 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2217:2: ( ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression ) ) ) otherlv_4= RightParenthesis ) )
            // InternalCQLParser.g:2218:2: ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression ) ) ) otherlv_4= RightParenthesis )
            {
            // InternalCQLParser.g:2218:2: ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression ) ) ) otherlv_4= RightParenthesis )
            // InternalCQLParser.g:2219:3: () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression ) ) ) otherlv_4= RightParenthesis
            {
            // InternalCQLParser.g:2219:3: ()
            // InternalCQLParser.g:2220:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getFunctionAccess().getFunctionAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:2226:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQLParser.g:2227:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQLParser.g:2227:4: (lv_name_1_0= RULE_ID )
            // InternalCQLParser.g:2228:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_33); 

            					newLeafNode(lv_name_1_0, grammarAccess.getFunctionAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getFunctionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,LeftParenthesis,FOLLOW_9); 

            			newLeafNode(otherlv_2, grammarAccess.getFunctionAccess().getLeftParenthesisKeyword_2());
            		
            // InternalCQLParser.g:2248:3: ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression ) ) )
            // InternalCQLParser.g:2249:4: ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression ) )
            {
            // InternalCQLParser.g:2249:4: ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression ) )
            // InternalCQLParser.g:2250:5: (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression )
            {
            // InternalCQLParser.g:2250:5: (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression )
            int alt38=3;
            switch ( input.LA(1) ) {
            case RULE_ID:
                {
                switch ( input.LA(2) ) {
                case FullStop:
                    {
                    int LA38_4 = input.LA(3);

                    if ( (LA38_4==RULE_ID) ) {
                        int LA38_6 = input.LA(4);

                        if ( (LA38_6==RightParenthesis) ) {
                            alt38=2;
                        }
                        else if ( ((LA38_6>=Asterisk && LA38_6<=PlusSign)||LA38_6==HyphenMinus||LA38_6==Solidus||LA38_6==CircumflexAccent) ) {
                            alt38=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 38, 6, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 38, 4, input);

                        throw nvae;
                    }
                    }
                    break;
                case RightParenthesis:
                    {
                    alt38=2;
                    }
                    break;
                case LeftParenthesis:
                case Asterisk:
                case PlusSign:
                case HyphenMinus:
                case Solidus:
                case CircumflexAccent:
                    {
                    alt38=1;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 38, 1, input);

                    throw nvae;
                }

                }
                break;
            case FALSE:
            case TRUE:
            case RULE_INT:
            case RULE_FLOAT:
            case RULE_VECTOR_FLOAT:
            case RULE_MATRIX_FLOAT:
            case RULE_STRING:
                {
                alt38=1;
                }
                break;
            case Asterisk:
                {
                alt38=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 0, input);

                throw nvae;
            }

            switch (alt38) {
                case 1 :
                    // InternalCQLParser.g:2251:6: lv_value_3_1= ruleSelectExpression
                    {

                    						newCompositeNode(grammarAccess.getFunctionAccess().getValueSelectExpressionParserRuleCall_3_0_0());
                    					
                    pushFollow(FOLLOW_24);
                    lv_value_3_1=ruleSelectExpression();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getFunctionRule());
                    						}
                    						set(
                    							current,
                    							"value",
                    							lv_value_3_1,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.SelectExpression");
                    						afterParserOrEnumRuleCall();
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:2267:6: lv_value_3_2= ruleSelectExpressionOnlyWithAttribute
                    {

                    						newCompositeNode(grammarAccess.getFunctionAccess().getValueSelectExpressionOnlyWithAttributeParserRuleCall_3_0_1());
                    					
                    pushFollow(FOLLOW_24);
                    lv_value_3_2=ruleSelectExpressionOnlyWithAttribute();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getFunctionRule());
                    						}
                    						set(
                    							current,
                    							"value",
                    							lv_value_3_2,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.SelectExpressionOnlyWithAttribute");
                    						afterParserOrEnumRuleCall();
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:2283:6: lv_value_3_3= ruleStarExpression
                    {

                    						newCompositeNode(grammarAccess.getFunctionAccess().getValueStarExpressionParserRuleCall_3_0_2());
                    					
                    pushFollow(FOLLOW_24);
                    lv_value_3_3=ruleStarExpression();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getFunctionRule());
                    						}
                    						set(
                    							current,
                    							"value",
                    							lv_value_3_3,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.StarExpression");
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
    // InternalCQLParser.g:2309:1: entryRuleExpressionComponent returns [EObject current=null] : iv_ruleExpressionComponent= ruleExpressionComponent EOF ;
    public final EObject entryRuleExpressionComponent() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponent = null;


        try {
            // InternalCQLParser.g:2309:60: (iv_ruleExpressionComponent= ruleExpressionComponent EOF )
            // InternalCQLParser.g:2310:2: iv_ruleExpressionComponent= ruleExpressionComponent EOF
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
    // InternalCQLParser.g:2316:1: ruleExpressionComponent returns [EObject current=null] : ( ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) ) ) ;
    public final EObject ruleExpressionComponent() throws RecognitionException {
        EObject current = null;

        EObject lv_value_0_1 = null;

        EObject lv_value_0_2 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2322:2: ( ( ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) ) ) )
            // InternalCQLParser.g:2323:2: ( ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) ) )
            {
            // InternalCQLParser.g:2323:2: ( ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) ) )
            // InternalCQLParser.g:2324:3: ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) )
            {
            // InternalCQLParser.g:2324:3: ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) )
            // InternalCQLParser.g:2325:4: (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef )
            {
            // InternalCQLParser.g:2325:4: (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef )
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==RULE_ID) ) {
                alt39=1;
            }
            else if ( (LA39_0==FALSE||LA39_0==TRUE||LA39_0==RULE_INT||LA39_0==RULE_FLOAT||(LA39_0>=RULE_VECTOR_FLOAT && LA39_0<=RULE_MATRIX_FLOAT)||LA39_0==RULE_STRING) ) {
                alt39=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;
            }
            switch (alt39) {
                case 1 :
                    // InternalCQLParser.g:2326:5: lv_value_0_1= ruleFunction
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
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.Function");
                    					afterParserOrEnumRuleCall();
                    				

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:2342:5: lv_value_0_2= ruleAtomicWithoutAttributeRef
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
                    						"de.uniol.inf.is.odysseus.parser.cql2.CQL.AtomicWithoutAttributeRef");
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


    // $ANTLR start "entryRuleStarExpression"
    // InternalCQLParser.g:2363:1: entryRuleStarExpression returns [EObject current=null] : iv_ruleStarExpression= ruleStarExpression EOF ;
    public final EObject entryRuleStarExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStarExpression = null;


        try {
            // InternalCQLParser.g:2363:55: (iv_ruleStarExpression= ruleStarExpression EOF )
            // InternalCQLParser.g:2364:2: iv_ruleStarExpression= ruleStarExpression EOF
            {
             newCompositeNode(grammarAccess.getStarExpressionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleStarExpression=ruleStarExpression();

            state._fsp--;

             current =iv_ruleStarExpression; 
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
    // $ANTLR end "entryRuleStarExpression"


    // $ANTLR start "ruleStarExpression"
    // InternalCQLParser.g:2370:1: ruleStarExpression returns [EObject current=null] : ( () ( (lv_expressions_1_0= ruleStar ) ) ) ;
    public final EObject ruleStarExpression() throws RecognitionException {
        EObject current = null;

        EObject lv_expressions_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2376:2: ( ( () ( (lv_expressions_1_0= ruleStar ) ) ) )
            // InternalCQLParser.g:2377:2: ( () ( (lv_expressions_1_0= ruleStar ) ) )
            {
            // InternalCQLParser.g:2377:2: ( () ( (lv_expressions_1_0= ruleStar ) ) )
            // InternalCQLParser.g:2378:3: () ( (lv_expressions_1_0= ruleStar ) )
            {
            // InternalCQLParser.g:2378:3: ()
            // InternalCQLParser.g:2379:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getStarExpressionAccess().getStarExpressionAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:2385:3: ( (lv_expressions_1_0= ruleStar ) )
            // InternalCQLParser.g:2386:4: (lv_expressions_1_0= ruleStar )
            {
            // InternalCQLParser.g:2386:4: (lv_expressions_1_0= ruleStar )
            // InternalCQLParser.g:2387:5: lv_expressions_1_0= ruleStar
            {

            					newCompositeNode(grammarAccess.getStarExpressionAccess().getExpressionsStarParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_expressions_1_0=ruleStar();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getStarExpressionRule());
            					}
            					add(
            						current,
            						"expressions",
            						lv_expressions_1_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.Star");
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
    // $ANTLR end "ruleStarExpression"


    // $ANTLR start "entryRuleStar"
    // InternalCQLParser.g:2408:1: entryRuleStar returns [EObject current=null] : iv_ruleStar= ruleStar EOF ;
    public final EObject entryRuleStar() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStar = null;


        try {
            // InternalCQLParser.g:2408:45: (iv_ruleStar= ruleStar EOF )
            // InternalCQLParser.g:2409:2: iv_ruleStar= ruleStar EOF
            {
             newCompositeNode(grammarAccess.getStarRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleStar=ruleStar();

            state._fsp--;

             current =iv_ruleStar; 
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
    // $ANTLR end "entryRuleStar"


    // $ANTLR start "ruleStar"
    // InternalCQLParser.g:2415:1: ruleStar returns [EObject current=null] : ( () ( (lv_value_1_0= ruleStarthing ) ) ) ;
    public final EObject ruleStar() throws RecognitionException {
        EObject current = null;

        EObject lv_value_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2421:2: ( ( () ( (lv_value_1_0= ruleStarthing ) ) ) )
            // InternalCQLParser.g:2422:2: ( () ( (lv_value_1_0= ruleStarthing ) ) )
            {
            // InternalCQLParser.g:2422:2: ( () ( (lv_value_1_0= ruleStarthing ) ) )
            // InternalCQLParser.g:2423:3: () ( (lv_value_1_0= ruleStarthing ) )
            {
            // InternalCQLParser.g:2423:3: ()
            // InternalCQLParser.g:2424:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getStarAccess().getStarAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:2430:3: ( (lv_value_1_0= ruleStarthing ) )
            // InternalCQLParser.g:2431:4: (lv_value_1_0= ruleStarthing )
            {
            // InternalCQLParser.g:2431:4: (lv_value_1_0= ruleStarthing )
            // InternalCQLParser.g:2432:5: lv_value_1_0= ruleStarthing
            {

            					newCompositeNode(grammarAccess.getStarAccess().getValueStarthingParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_value_1_0=ruleStarthing();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getStarRule());
            					}
            					set(
            						current,
            						"value",
            						lv_value_1_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.Starthing");
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
    // $ANTLR end "ruleStar"


    // $ANTLR start "entryRuleStarthing"
    // InternalCQLParser.g:2453:1: entryRuleStarthing returns [EObject current=null] : iv_ruleStarthing= ruleStarthing EOF ;
    public final EObject entryRuleStarthing() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStarthing = null;


        try {
            // InternalCQLParser.g:2453:50: (iv_ruleStarthing= ruleStarthing EOF )
            // InternalCQLParser.g:2454:2: iv_ruleStarthing= ruleStarthing EOF
            {
             newCompositeNode(grammarAccess.getStarthingRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleStarthing=ruleStarthing();

            state._fsp--;

             current =iv_ruleStarthing; 
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
    // $ANTLR end "entryRuleStarthing"


    // $ANTLR start "ruleStarthing"
    // InternalCQLParser.g:2460:1: ruleStarthing returns [EObject current=null] : ( () otherlv_1= Asterisk ) ;
    public final EObject ruleStarthing() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2466:2: ( ( () otherlv_1= Asterisk ) )
            // InternalCQLParser.g:2467:2: ( () otherlv_1= Asterisk )
            {
            // InternalCQLParser.g:2467:2: ( () otherlv_1= Asterisk )
            // InternalCQLParser.g:2468:3: () otherlv_1= Asterisk
            {
            // InternalCQLParser.g:2468:3: ()
            // InternalCQLParser.g:2469:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getStarthingAccess().getStarthingAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,Asterisk,FOLLOW_2); 

            			newLeafNode(otherlv_1, grammarAccess.getStarthingAccess().getAsteriskKeyword_1());
            		

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
    // $ANTLR end "ruleStarthing"


    // $ANTLR start "entryRuleExpressionComponentAsAttribute"
    // InternalCQLParser.g:2483:1: entryRuleExpressionComponentAsAttribute returns [EObject current=null] : iv_ruleExpressionComponentAsAttribute= ruleExpressionComponentAsAttribute EOF ;
    public final EObject entryRuleExpressionComponentAsAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentAsAttribute = null;


        try {
            // InternalCQLParser.g:2483:71: (iv_ruleExpressionComponentAsAttribute= ruleExpressionComponentAsAttribute EOF )
            // InternalCQLParser.g:2484:2: iv_ruleExpressionComponentAsAttribute= ruleExpressionComponentAsAttribute EOF
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
    // InternalCQLParser.g:2490:1: ruleExpressionComponentAsAttribute returns [EObject current=null] : ( () ( (lv_value_1_0= ruleAttributeForSelectExpression ) ) ) ;
    public final EObject ruleExpressionComponentAsAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_value_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2496:2: ( ( () ( (lv_value_1_0= ruleAttributeForSelectExpression ) ) ) )
            // InternalCQLParser.g:2497:2: ( () ( (lv_value_1_0= ruleAttributeForSelectExpression ) ) )
            {
            // InternalCQLParser.g:2497:2: ( () ( (lv_value_1_0= ruleAttributeForSelectExpression ) ) )
            // InternalCQLParser.g:2498:3: () ( (lv_value_1_0= ruleAttributeForSelectExpression ) )
            {
            // InternalCQLParser.g:2498:3: ()
            // InternalCQLParser.g:2499:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionComponentAsAttributeAccess().getExpressionComponentAsAttributeAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:2505:3: ( (lv_value_1_0= ruleAttributeForSelectExpression ) )
            // InternalCQLParser.g:2506:4: (lv_value_1_0= ruleAttributeForSelectExpression )
            {
            // InternalCQLParser.g:2506:4: (lv_value_1_0= ruleAttributeForSelectExpression )
            // InternalCQLParser.g:2507:5: lv_value_1_0= ruleAttributeForSelectExpression
            {

            					newCompositeNode(grammarAccess.getExpressionComponentAsAttributeAccess().getValueAttributeForSelectExpressionParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_value_1_0=ruleAttributeForSelectExpression();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getExpressionComponentAsAttributeRule());
            					}
            					set(
            						current,
            						"value",
            						lv_value_1_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.AttributeForSelectExpression");
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
    // InternalCQLParser.g:2528:1: entryRuleAlias returns [EObject current=null] : iv_ruleAlias= ruleAlias EOF ;
    public final EObject entryRuleAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAlias = null;


        try {
            // InternalCQLParser.g:2528:46: (iv_ruleAlias= ruleAlias EOF )
            // InternalCQLParser.g:2529:2: iv_ruleAlias= ruleAlias EOF
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
    // InternalCQLParser.g:2535:1: ruleAlias returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2541:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQLParser.g:2542:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQLParser.g:2542:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:2543:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:2543:3: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:2544:4: lv_name_0_0= RULE_ID
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
            					"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            			

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
    // InternalCQLParser.g:2563:1: entryRuleAccessFramework returns [EObject current=null] : iv_ruleAccessFramework= ruleAccessFramework EOF ;
    public final EObject entryRuleAccessFramework() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAccessFramework = null;


        try {
            // InternalCQLParser.g:2563:56: (iv_ruleAccessFramework= ruleAccessFramework EOF )
            // InternalCQLParser.g:2564:2: iv_ruleAccessFramework= ruleAccessFramework EOF
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
    // InternalCQLParser.g:2570:1: ruleAccessFramework returns [EObject current=null] : (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) ) ) )* otherlv_15= RightParenthesis ) ;
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
        Token lv_values_11_1=null;
        Token lv_values_11_2=null;
        Token otherlv_12=null;
        Token lv_keys_13_0=null;
        Token lv_values_14_1=null;
        Token lv_values_14_2=null;
        Token otherlv_15=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2576:2: ( (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) ) ) )* otherlv_15= RightParenthesis ) )
            // InternalCQLParser.g:2577:2: (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) ) ) )* otherlv_15= RightParenthesis )
            {
            // InternalCQLParser.g:2577:2: (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) ) ) )* otherlv_15= RightParenthesis )
            // InternalCQLParser.g:2578:3: otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) ) ) )* otherlv_15= RightParenthesis
            {
            otherlv_0=(Token)match(input,WRAPPER,FOLLOW_34); 

            			newLeafNode(otherlv_0, grammarAccess.getAccessFrameworkAccess().getWRAPPERKeyword_0());
            		
            // InternalCQLParser.g:2582:3: ( (lv_wrapper_1_0= RULE_STRING ) )
            // InternalCQLParser.g:2583:4: (lv_wrapper_1_0= RULE_STRING )
            {
            // InternalCQLParser.g:2583:4: (lv_wrapper_1_0= RULE_STRING )
            // InternalCQLParser.g:2584:5: lv_wrapper_1_0= RULE_STRING
            {
            lv_wrapper_1_0=(Token)match(input,RULE_STRING,FOLLOW_35); 

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

            otherlv_2=(Token)match(input,PROTOCOL,FOLLOW_34); 

            			newLeafNode(otherlv_2, grammarAccess.getAccessFrameworkAccess().getPROTOCOLKeyword_2());
            		
            // InternalCQLParser.g:2604:3: ( (lv_protocol_3_0= RULE_STRING ) )
            // InternalCQLParser.g:2605:4: (lv_protocol_3_0= RULE_STRING )
            {
            // InternalCQLParser.g:2605:4: (lv_protocol_3_0= RULE_STRING )
            // InternalCQLParser.g:2606:5: lv_protocol_3_0= RULE_STRING
            {
            lv_protocol_3_0=(Token)match(input,RULE_STRING,FOLLOW_36); 

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

            otherlv_4=(Token)match(input,TRANSPORT,FOLLOW_34); 

            			newLeafNode(otherlv_4, grammarAccess.getAccessFrameworkAccess().getTRANSPORTKeyword_4());
            		
            // InternalCQLParser.g:2626:3: ( (lv_transport_5_0= RULE_STRING ) )
            // InternalCQLParser.g:2627:4: (lv_transport_5_0= RULE_STRING )
            {
            // InternalCQLParser.g:2627:4: (lv_transport_5_0= RULE_STRING )
            // InternalCQLParser.g:2628:5: lv_transport_5_0= RULE_STRING
            {
            lv_transport_5_0=(Token)match(input,RULE_STRING,FOLLOW_37); 

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

            otherlv_6=(Token)match(input,DATAHANDLER,FOLLOW_34); 

            			newLeafNode(otherlv_6, grammarAccess.getAccessFrameworkAccess().getDATAHANDLERKeyword_6());
            		
            // InternalCQLParser.g:2648:3: ( (lv_datahandler_7_0= RULE_STRING ) )
            // InternalCQLParser.g:2649:4: (lv_datahandler_7_0= RULE_STRING )
            {
            // InternalCQLParser.g:2649:4: (lv_datahandler_7_0= RULE_STRING )
            // InternalCQLParser.g:2650:5: lv_datahandler_7_0= RULE_STRING
            {
            lv_datahandler_7_0=(Token)match(input,RULE_STRING,FOLLOW_38); 

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

            otherlv_8=(Token)match(input,OPTIONS,FOLLOW_33); 

            			newLeafNode(otherlv_8, grammarAccess.getAccessFrameworkAccess().getOPTIONSKeyword_8());
            		
            otherlv_9=(Token)match(input,LeftParenthesis,FOLLOW_34); 

            			newLeafNode(otherlv_9, grammarAccess.getAccessFrameworkAccess().getLeftParenthesisKeyword_9());
            		
            // InternalCQLParser.g:2674:3: ( ( (lv_keys_10_0= RULE_STRING ) ) ( ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) ) ) )+
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
            	    // InternalCQLParser.g:2675:4: ( (lv_keys_10_0= RULE_STRING ) ) ( ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) ) )
            	    {
            	    // InternalCQLParser.g:2675:4: ( (lv_keys_10_0= RULE_STRING ) )
            	    // InternalCQLParser.g:2676:5: (lv_keys_10_0= RULE_STRING )
            	    {
            	    // InternalCQLParser.g:2676:5: (lv_keys_10_0= RULE_STRING )
            	    // InternalCQLParser.g:2677:6: lv_keys_10_0= RULE_STRING
            	    {
            	    lv_keys_10_0=(Token)match(input,RULE_STRING,FOLLOW_39); 

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

            	    // InternalCQLParser.g:2693:4: ( ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) ) )
            	    // InternalCQLParser.g:2694:5: ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) )
            	    {
            	    // InternalCQLParser.g:2694:5: ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) )
            	    // InternalCQLParser.g:2695:6: (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH )
            	    {
            	    // InternalCQLParser.g:2695:6: (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH )
            	    int alt40=2;
            	    int LA40_0 = input.LA(1);

            	    if ( (LA40_0==RULE_STRING) ) {
            	        alt40=1;
            	    }
            	    else if ( (LA40_0==RULE_PATH) ) {
            	        alt40=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 40, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt40) {
            	        case 1 :
            	            // InternalCQLParser.g:2696:7: lv_values_11_1= RULE_STRING
            	            {
            	            lv_values_11_1=(Token)match(input,RULE_STRING,FOLLOW_40); 

            	            							newLeafNode(lv_values_11_1, grammarAccess.getAccessFrameworkAccess().getValuesSTRINGTerminalRuleCall_10_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getAccessFrameworkRule());
            	            							}
            	            							addWithLastConsumed(
            	            								current,
            	            								"values",
            	            								lv_values_11_1,
            	            								"org.eclipse.xtext.common.Terminals.STRING");
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:2711:7: lv_values_11_2= RULE_PATH
            	            {
            	            lv_values_11_2=(Token)match(input,RULE_PATH,FOLLOW_40); 

            	            							newLeafNode(lv_values_11_2, grammarAccess.getAccessFrameworkAccess().getValuesPATHTerminalRuleCall_10_1_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getAccessFrameworkRule());
            	            							}
            	            							addWithLastConsumed(
            	            								current,
            	            								"values",
            	            								lv_values_11_2,
            	            								"de.uniol.inf.is.odysseus.parser.cql2.CQL.PATH");
            	            						

            	            }
            	            break;

            	    }


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

            // InternalCQLParser.g:2729:3: (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) ) ) )*
            loop43:
            do {
                int alt43=2;
                int LA43_0 = input.LA(1);

                if ( (LA43_0==Comma) ) {
                    alt43=1;
                }


                switch (alt43) {
            	case 1 :
            	    // InternalCQLParser.g:2730:4: otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) ) )
            	    {
            	    otherlv_12=(Token)match(input,Comma,FOLLOW_34); 

            	    				newLeafNode(otherlv_12, grammarAccess.getAccessFrameworkAccess().getCommaKeyword_11_0());
            	    			
            	    // InternalCQLParser.g:2734:4: ( (lv_keys_13_0= RULE_STRING ) )
            	    // InternalCQLParser.g:2735:5: (lv_keys_13_0= RULE_STRING )
            	    {
            	    // InternalCQLParser.g:2735:5: (lv_keys_13_0= RULE_STRING )
            	    // InternalCQLParser.g:2736:6: lv_keys_13_0= RULE_STRING
            	    {
            	    lv_keys_13_0=(Token)match(input,RULE_STRING,FOLLOW_39); 

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

            	    // InternalCQLParser.g:2752:4: ( ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) ) )
            	    // InternalCQLParser.g:2753:5: ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) )
            	    {
            	    // InternalCQLParser.g:2753:5: ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) )
            	    // InternalCQLParser.g:2754:6: (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH )
            	    {
            	    // InternalCQLParser.g:2754:6: (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH )
            	    int alt42=2;
            	    int LA42_0 = input.LA(1);

            	    if ( (LA42_0==RULE_STRING) ) {
            	        alt42=1;
            	    }
            	    else if ( (LA42_0==RULE_PATH) ) {
            	        alt42=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 42, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt42) {
            	        case 1 :
            	            // InternalCQLParser.g:2755:7: lv_values_14_1= RULE_STRING
            	            {
            	            lv_values_14_1=(Token)match(input,RULE_STRING,FOLLOW_41); 

            	            							newLeafNode(lv_values_14_1, grammarAccess.getAccessFrameworkAccess().getValuesSTRINGTerminalRuleCall_11_2_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getAccessFrameworkRule());
            	            							}
            	            							addWithLastConsumed(
            	            								current,
            	            								"values",
            	            								lv_values_14_1,
            	            								"org.eclipse.xtext.common.Terminals.STRING");
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:2770:7: lv_values_14_2= RULE_PATH
            	            {
            	            lv_values_14_2=(Token)match(input,RULE_PATH,FOLLOW_41); 

            	            							newLeafNode(lv_values_14_2, grammarAccess.getAccessFrameworkAccess().getValuesPATHTerminalRuleCall_11_2_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getAccessFrameworkRule());
            	            							}
            	            							addWithLastConsumed(
            	            								current,
            	            								"values",
            	            								lv_values_14_2,
            	            								"de.uniol.inf.is.odysseus.parser.cql2.CQL.PATH");
            	            						

            	            }
            	            break;

            	    }


            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop43;
                }
            } while (true);

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
    // InternalCQLParser.g:2796:1: entryRuleSchemaDefinition returns [EObject current=null] : iv_ruleSchemaDefinition= ruleSchemaDefinition EOF ;
    public final EObject entryRuleSchemaDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSchemaDefinition = null;


        try {
            // InternalCQLParser.g:2796:57: (iv_ruleSchemaDefinition= ruleSchemaDefinition EOF )
            // InternalCQLParser.g:2797:2: iv_ruleSchemaDefinition= ruleSchemaDefinition EOF
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
    // InternalCQLParser.g:2803:1: ruleSchemaDefinition returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis ) ;
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
            // InternalCQLParser.g:2809:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis ) )
            // InternalCQLParser.g:2810:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis )
            {
            // InternalCQLParser.g:2810:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis )
            // InternalCQLParser.g:2811:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis
            {
            // InternalCQLParser.g:2811:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:2812:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:2812:4: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:2813:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_33); 

            					newLeafNode(lv_name_0_0, grammarAccess.getSchemaDefinitionAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSchemaDefinitionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,LeftParenthesis,FOLLOW_5); 

            			newLeafNode(otherlv_1, grammarAccess.getSchemaDefinitionAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQLParser.g:2833:3: ( (lv_arguments_2_0= RULE_ID ) )
            // InternalCQLParser.g:2834:4: (lv_arguments_2_0= RULE_ID )
            {
            // InternalCQLParser.g:2834:4: (lv_arguments_2_0= RULE_ID )
            // InternalCQLParser.g:2835:5: lv_arguments_2_0= RULE_ID
            {
            lv_arguments_2_0=(Token)match(input,RULE_ID,FOLLOW_5); 

            					newLeafNode(lv_arguments_2_0, grammarAccess.getSchemaDefinitionAccess().getArgumentsIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSchemaDefinitionRule());
            					}
            					addWithLastConsumed(
            						current,
            						"arguments",
            						lv_arguments_2_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:2851:3: ( (lv_arguments_3_0= RULE_ID ) )
            // InternalCQLParser.g:2852:4: (lv_arguments_3_0= RULE_ID )
            {
            // InternalCQLParser.g:2852:4: (lv_arguments_3_0= RULE_ID )
            // InternalCQLParser.g:2853:5: lv_arguments_3_0= RULE_ID
            {
            lv_arguments_3_0=(Token)match(input,RULE_ID,FOLLOW_41); 

            					newLeafNode(lv_arguments_3_0, grammarAccess.getSchemaDefinitionAccess().getArgumentsIDTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSchemaDefinitionRule());
            					}
            					addWithLastConsumed(
            						current,
            						"arguments",
            						lv_arguments_3_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:2869:3: (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )*
            loop44:
            do {
                int alt44=2;
                int LA44_0 = input.LA(1);

                if ( (LA44_0==Comma) ) {
                    alt44=1;
                }


                switch (alt44) {
            	case 1 :
            	    // InternalCQLParser.g:2870:4: otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) )
            	    {
            	    otherlv_4=(Token)match(input,Comma,FOLLOW_5); 

            	    				newLeafNode(otherlv_4, grammarAccess.getSchemaDefinitionAccess().getCommaKeyword_4_0());
            	    			
            	    // InternalCQLParser.g:2874:4: ( (lv_arguments_5_0= RULE_ID ) )
            	    // InternalCQLParser.g:2875:5: (lv_arguments_5_0= RULE_ID )
            	    {
            	    // InternalCQLParser.g:2875:5: (lv_arguments_5_0= RULE_ID )
            	    // InternalCQLParser.g:2876:6: lv_arguments_5_0= RULE_ID
            	    {
            	    lv_arguments_5_0=(Token)match(input,RULE_ID,FOLLOW_5); 

            	    						newLeafNode(lv_arguments_5_0, grammarAccess.getSchemaDefinitionAccess().getArgumentsIDTerminalRuleCall_4_1_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getSchemaDefinitionRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"arguments",
            	    							lv_arguments_5_0,
            	    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            	    					

            	    }


            	    }

            	    // InternalCQLParser.g:2892:4: ( (lv_arguments_6_0= RULE_ID ) )
            	    // InternalCQLParser.g:2893:5: (lv_arguments_6_0= RULE_ID )
            	    {
            	    // InternalCQLParser.g:2893:5: (lv_arguments_6_0= RULE_ID )
            	    // InternalCQLParser.g:2894:6: lv_arguments_6_0= RULE_ID
            	    {
            	    lv_arguments_6_0=(Token)match(input,RULE_ID,FOLLOW_41); 

            	    						newLeafNode(lv_arguments_6_0, grammarAccess.getSchemaDefinitionAccess().getArgumentsIDTerminalRuleCall_4_2_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getSchemaDefinitionRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"arguments",
            	    							lv_arguments_6_0,
            	    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop44;
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
    // InternalCQLParser.g:2919:1: entryRuleCreate returns [EObject current=null] : iv_ruleCreate= ruleCreate EOF ;
    public final EObject entryRuleCreate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreate = null;


        try {
            // InternalCQLParser.g:2919:47: (iv_ruleCreate= ruleCreate EOF )
            // InternalCQLParser.g:2920:2: iv_ruleCreate= ruleCreate EOF
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
    // InternalCQLParser.g:2926:1: ruleCreate returns [EObject current=null] : ( () (otherlv_1= CREATE | otherlv_2= ATTACH ) ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) ) ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) ) ) ;
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
            // InternalCQLParser.g:2932:2: ( ( () (otherlv_1= CREATE | otherlv_2= ATTACH ) ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) ) ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) ) ) )
            // InternalCQLParser.g:2933:2: ( () (otherlv_1= CREATE | otherlv_2= ATTACH ) ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) ) ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) ) )
            {
            // InternalCQLParser.g:2933:2: ( () (otherlv_1= CREATE | otherlv_2= ATTACH ) ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) ) ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) ) )
            // InternalCQLParser.g:2934:3: () (otherlv_1= CREATE | otherlv_2= ATTACH ) ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) ) ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) )
            {
            // InternalCQLParser.g:2934:3: ()
            // InternalCQLParser.g:2935:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getCreateAccess().getCreateAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:2941:3: (otherlv_1= CREATE | otherlv_2= ATTACH )
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==CREATE) ) {
                alt45=1;
            }
            else if ( (LA45_0==ATTACH) ) {
                alt45=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 45, 0, input);

                throw nvae;
            }
            switch (alt45) {
                case 1 :
                    // InternalCQLParser.g:2942:4: otherlv_1= CREATE
                    {
                    otherlv_1=(Token)match(input,CREATE,FOLLOW_42); 

                    				newLeafNode(otherlv_1, grammarAccess.getCreateAccess().getCREATEKeyword_1_0());
                    			

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:2947:4: otherlv_2= ATTACH
                    {
                    otherlv_2=(Token)match(input,ATTACH,FOLLOW_42); 

                    				newLeafNode(otherlv_2, grammarAccess.getCreateAccess().getATTACHKeyword_1_1());
                    			

                    }
                    break;

            }

            // InternalCQLParser.g:2952:3: ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) )
            // InternalCQLParser.g:2953:4: ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) )
            {
            // InternalCQLParser.g:2953:4: ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) )
            // InternalCQLParser.g:2954:5: (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW )
            {
            // InternalCQLParser.g:2954:5: (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW )
            int alt46=3;
            switch ( input.LA(1) ) {
            case STREAM:
                {
                alt46=1;
                }
                break;
            case SINK:
                {
                alt46=2;
                }
                break;
            case VIEW:
                {
                alt46=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 46, 0, input);

                throw nvae;
            }

            switch (alt46) {
                case 1 :
                    // InternalCQLParser.g:2955:6: lv_type_3_1= STREAM
                    {
                    lv_type_3_1=(Token)match(input,STREAM,FOLLOW_5); 

                    						newLeafNode(lv_type_3_1, grammarAccess.getCreateAccess().getTypeSTREAMKeyword_2_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateRule());
                    						}
                    						setWithLastConsumed(current, "type", lv_type_3_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:2966:6: lv_type_3_2= SINK
                    {
                    lv_type_3_2=(Token)match(input,SINK,FOLLOW_5); 

                    						newLeafNode(lv_type_3_2, grammarAccess.getCreateAccess().getTypeSINKKeyword_2_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateRule());
                    						}
                    						setWithLastConsumed(current, "type", lv_type_3_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:2977:6: lv_type_3_3= VIEW
                    {
                    lv_type_3_3=(Token)match(input,VIEW,FOLLOW_5); 

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

            // InternalCQLParser.g:2990:3: ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) )
            // InternalCQLParser.g:2991:4: ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) )
            {
            // InternalCQLParser.g:2991:4: ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) )
            // InternalCQLParser.g:2992:5: (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView )
            {
            // InternalCQLParser.g:2992:5: (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView )
            int alt47=6;
            alt47 = dfa47.predict(input);
            switch (alt47) {
                case 1 :
                    // InternalCQLParser.g:2993:6: lv_create_4_1= ruleCreateAccessFramework
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.CreateAccessFramework");
                    						afterParserOrEnumRuleCall();
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:3009:6: lv_create_4_2= ruleCreateChannelFrameworkViaPort
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.CreateChannelFrameworkViaPort");
                    						afterParserOrEnumRuleCall();
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:3025:6: lv_create_4_3= ruleCreateChannelFormatViaFile
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.CreateChannelFormatViaFile");
                    						afterParserOrEnumRuleCall();
                    					

                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:3041:6: lv_create_4_4= ruleCreateDatabaseStream
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.CreateDatabaseStream");
                    						afterParserOrEnumRuleCall();
                    					

                    }
                    break;
                case 5 :
                    // InternalCQLParser.g:3057:6: lv_create_4_5= ruleCreateDatabaseSink
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.CreateDatabaseSink");
                    						afterParserOrEnumRuleCall();
                    					

                    }
                    break;
                case 6 :
                    // InternalCQLParser.g:3073:6: lv_create_4_6= ruleCreateView
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.CreateView");
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
    // InternalCQLParser.g:3095:1: entryRuleCreateAccessFramework returns [EObject current=null] : iv_ruleCreateAccessFramework= ruleCreateAccessFramework EOF ;
    public final EObject entryRuleCreateAccessFramework() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateAccessFramework = null;


        try {
            // InternalCQLParser.g:3095:62: (iv_ruleCreateAccessFramework= ruleCreateAccessFramework EOF )
            // InternalCQLParser.g:3096:2: iv_ruleCreateAccessFramework= ruleCreateAccessFramework EOF
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
    // InternalCQLParser.g:3102:1: ruleCreateAccessFramework returns [EObject current=null] : ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) ( (lv_pars_1_0= ruleAccessFramework ) ) ) ;
    public final EObject ruleCreateAccessFramework() throws RecognitionException {
        EObject current = null;

        EObject lv_attributes_0_0 = null;

        EObject lv_pars_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3108:2: ( ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) ( (lv_pars_1_0= ruleAccessFramework ) ) ) )
            // InternalCQLParser.g:3109:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) ( (lv_pars_1_0= ruleAccessFramework ) ) )
            {
            // InternalCQLParser.g:3109:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) ( (lv_pars_1_0= ruleAccessFramework ) ) )
            // InternalCQLParser.g:3110:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) ) ( (lv_pars_1_0= ruleAccessFramework ) )
            {
            // InternalCQLParser.g:3110:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) )
            // InternalCQLParser.g:3111:4: (lv_attributes_0_0= ruleSchemaDefinition )
            {
            // InternalCQLParser.g:3111:4: (lv_attributes_0_0= ruleSchemaDefinition )
            // InternalCQLParser.g:3112:5: lv_attributes_0_0= ruleSchemaDefinition
            {

            					newCompositeNode(grammarAccess.getCreateAccessFrameworkAccess().getAttributesSchemaDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_43);
            lv_attributes_0_0=ruleSchemaDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateAccessFrameworkRule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_0_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.SchemaDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:3129:3: ( (lv_pars_1_0= ruleAccessFramework ) )
            // InternalCQLParser.g:3130:4: (lv_pars_1_0= ruleAccessFramework )
            {
            // InternalCQLParser.g:3130:4: (lv_pars_1_0= ruleAccessFramework )
            // InternalCQLParser.g:3131:5: lv_pars_1_0= ruleAccessFramework
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
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.AccessFramework");
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
    // InternalCQLParser.g:3152:1: entryRuleCreateChannelFrameworkViaPort returns [EObject current=null] : iv_ruleCreateChannelFrameworkViaPort= ruleCreateChannelFrameworkViaPort EOF ;
    public final EObject entryRuleCreateChannelFrameworkViaPort() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateChannelFrameworkViaPort = null;


        try {
            // InternalCQLParser.g:3152:70: (iv_ruleCreateChannelFrameworkViaPort= ruleCreateChannelFrameworkViaPort EOF )
            // InternalCQLParser.g:3153:2: iv_ruleCreateChannelFrameworkViaPort= ruleCreateChannelFrameworkViaPort EOF
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
    // InternalCQLParser.g:3159:1: ruleCreateChannelFrameworkViaPort returns [EObject current=null] : ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= CHANNEL ( (lv_host_2_0= RULE_ID ) ) otherlv_3= Colon ( (lv_port_4_0= RULE_INT ) ) ) ;
    public final EObject ruleCreateChannelFrameworkViaPort() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_host_2_0=null;
        Token otherlv_3=null;
        Token lv_port_4_0=null;
        EObject lv_attributes_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3165:2: ( ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= CHANNEL ( (lv_host_2_0= RULE_ID ) ) otherlv_3= Colon ( (lv_port_4_0= RULE_INT ) ) ) )
            // InternalCQLParser.g:3166:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= CHANNEL ( (lv_host_2_0= RULE_ID ) ) otherlv_3= Colon ( (lv_port_4_0= RULE_INT ) ) )
            {
            // InternalCQLParser.g:3166:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= CHANNEL ( (lv_host_2_0= RULE_ID ) ) otherlv_3= Colon ( (lv_port_4_0= RULE_INT ) ) )
            // InternalCQLParser.g:3167:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= CHANNEL ( (lv_host_2_0= RULE_ID ) ) otherlv_3= Colon ( (lv_port_4_0= RULE_INT ) )
            {
            // InternalCQLParser.g:3167:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) )
            // InternalCQLParser.g:3168:4: (lv_attributes_0_0= ruleSchemaDefinition )
            {
            // InternalCQLParser.g:3168:4: (lv_attributes_0_0= ruleSchemaDefinition )
            // InternalCQLParser.g:3169:5: lv_attributes_0_0= ruleSchemaDefinition
            {

            					newCompositeNode(grammarAccess.getCreateChannelFrameworkViaPortAccess().getAttributesSchemaDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_44);
            lv_attributes_0_0=ruleSchemaDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateChannelFrameworkViaPortRule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_0_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.SchemaDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,CHANNEL,FOLLOW_5); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateChannelFrameworkViaPortAccess().getCHANNELKeyword_1());
            		
            // InternalCQLParser.g:3190:3: ( (lv_host_2_0= RULE_ID ) )
            // InternalCQLParser.g:3191:4: (lv_host_2_0= RULE_ID )
            {
            // InternalCQLParser.g:3191:4: (lv_host_2_0= RULE_ID )
            // InternalCQLParser.g:3192:5: lv_host_2_0= RULE_ID
            {
            lv_host_2_0=(Token)match(input,RULE_ID,FOLLOW_45); 

            					newLeafNode(lv_host_2_0, grammarAccess.getCreateChannelFrameworkViaPortAccess().getHostIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateChannelFrameworkViaPortRule());
            					}
            					setWithLastConsumed(
            						current,
            						"host",
            						lv_host_2_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            otherlv_3=(Token)match(input,Colon,FOLLOW_46); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateChannelFrameworkViaPortAccess().getColonKeyword_3());
            		
            // InternalCQLParser.g:3212:3: ( (lv_port_4_0= RULE_INT ) )
            // InternalCQLParser.g:3213:4: (lv_port_4_0= RULE_INT )
            {
            // InternalCQLParser.g:3213:4: (lv_port_4_0= RULE_INT )
            // InternalCQLParser.g:3214:5: lv_port_4_0= RULE_INT
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
    // InternalCQLParser.g:3234:1: entryRuleCreateChannelFormatViaFile returns [EObject current=null] : iv_ruleCreateChannelFormatViaFile= ruleCreateChannelFormatViaFile EOF ;
    public final EObject entryRuleCreateChannelFormatViaFile() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateChannelFormatViaFile = null;


        try {
            // InternalCQLParser.g:3234:67: (iv_ruleCreateChannelFormatViaFile= ruleCreateChannelFormatViaFile EOF )
            // InternalCQLParser.g:3235:2: iv_ruleCreateChannelFormatViaFile= ruleCreateChannelFormatViaFile EOF
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
    // InternalCQLParser.g:3241:1: ruleCreateChannelFormatViaFile returns [EObject current=null] : ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= FILE ( (lv_filename_2_0= RULE_STRING ) ) otherlv_3= AS ( (lv_type_4_0= RULE_ID ) ) ) ;
    public final EObject ruleCreateChannelFormatViaFile() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_filename_2_0=null;
        Token otherlv_3=null;
        Token lv_type_4_0=null;
        EObject lv_attributes_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3247:2: ( ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= FILE ( (lv_filename_2_0= RULE_STRING ) ) otherlv_3= AS ( (lv_type_4_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:3248:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= FILE ( (lv_filename_2_0= RULE_STRING ) ) otherlv_3= AS ( (lv_type_4_0= RULE_ID ) ) )
            {
            // InternalCQLParser.g:3248:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= FILE ( (lv_filename_2_0= RULE_STRING ) ) otherlv_3= AS ( (lv_type_4_0= RULE_ID ) ) )
            // InternalCQLParser.g:3249:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= FILE ( (lv_filename_2_0= RULE_STRING ) ) otherlv_3= AS ( (lv_type_4_0= RULE_ID ) )
            {
            // InternalCQLParser.g:3249:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) )
            // InternalCQLParser.g:3250:4: (lv_attributes_0_0= ruleSchemaDefinition )
            {
            // InternalCQLParser.g:3250:4: (lv_attributes_0_0= ruleSchemaDefinition )
            // InternalCQLParser.g:3251:5: lv_attributes_0_0= ruleSchemaDefinition
            {

            					newCompositeNode(grammarAccess.getCreateChannelFormatViaFileAccess().getAttributesSchemaDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_47);
            lv_attributes_0_0=ruleSchemaDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateChannelFormatViaFileRule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_0_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.SchemaDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,FILE,FOLLOW_34); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateChannelFormatViaFileAccess().getFILEKeyword_1());
            		
            // InternalCQLParser.g:3272:3: ( (lv_filename_2_0= RULE_STRING ) )
            // InternalCQLParser.g:3273:4: (lv_filename_2_0= RULE_STRING )
            {
            // InternalCQLParser.g:3273:4: (lv_filename_2_0= RULE_STRING )
            // InternalCQLParser.g:3274:5: lv_filename_2_0= RULE_STRING
            {
            lv_filename_2_0=(Token)match(input,RULE_STRING,FOLLOW_27); 

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

            otherlv_3=(Token)match(input,AS,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateChannelFormatViaFileAccess().getASKeyword_3());
            		
            // InternalCQLParser.g:3294:3: ( (lv_type_4_0= RULE_ID ) )
            // InternalCQLParser.g:3295:4: (lv_type_4_0= RULE_ID )
            {
            // InternalCQLParser.g:3295:4: (lv_type_4_0= RULE_ID )
            // InternalCQLParser.g:3296:5: lv_type_4_0= RULE_ID
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
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

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
    // InternalCQLParser.g:3316:1: entryRuleCreateDatabaseStream returns [EObject current=null] : iv_ruleCreateDatabaseStream= ruleCreateDatabaseStream EOF ;
    public final EObject entryRuleCreateDatabaseStream() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateDatabaseStream = null;


        try {
            // InternalCQLParser.g:3316:61: (iv_ruleCreateDatabaseStream= ruleCreateDatabaseStream EOF )
            // InternalCQLParser.g:3317:2: iv_ruleCreateDatabaseStream= ruleCreateDatabaseStream EOF
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
    // InternalCQLParser.g:3323:1: ruleCreateDatabaseStream returns [EObject current=null] : ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= DATABASE ( (lv_database_2_0= RULE_ID ) ) otherlv_3= TABLE ( (lv_table_4_0= RULE_ID ) ) (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= ruleTime ) ) )? ) ;
    public final EObject ruleCreateDatabaseStream() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_database_2_0=null;
        Token otherlv_3=null;
        Token lv_table_4_0=null;
        Token otherlv_5=null;
        Token lv_size_6_0=null;
        EObject lv_attributes_0_0 = null;

        Enumerator lv_unit_7_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3329:2: ( ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= DATABASE ( (lv_database_2_0= RULE_ID ) ) otherlv_3= TABLE ( (lv_table_4_0= RULE_ID ) ) (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= ruleTime ) ) )? ) )
            // InternalCQLParser.g:3330:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= DATABASE ( (lv_database_2_0= RULE_ID ) ) otherlv_3= TABLE ( (lv_table_4_0= RULE_ID ) ) (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= ruleTime ) ) )? )
            {
            // InternalCQLParser.g:3330:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= DATABASE ( (lv_database_2_0= RULE_ID ) ) otherlv_3= TABLE ( (lv_table_4_0= RULE_ID ) ) (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= ruleTime ) ) )? )
            // InternalCQLParser.g:3331:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= DATABASE ( (lv_database_2_0= RULE_ID ) ) otherlv_3= TABLE ( (lv_table_4_0= RULE_ID ) ) (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= ruleTime ) ) )?
            {
            // InternalCQLParser.g:3331:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) )
            // InternalCQLParser.g:3332:4: (lv_attributes_0_0= ruleSchemaDefinition )
            {
            // InternalCQLParser.g:3332:4: (lv_attributes_0_0= ruleSchemaDefinition )
            // InternalCQLParser.g:3333:5: lv_attributes_0_0= ruleSchemaDefinition
            {

            					newCompositeNode(grammarAccess.getCreateDatabaseStreamAccess().getAttributesSchemaDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_48);
            lv_attributes_0_0=ruleSchemaDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateDatabaseStreamRule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_0_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.SchemaDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,DATABASE,FOLLOW_5); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateDatabaseStreamAccess().getDATABASEKeyword_1());
            		
            // InternalCQLParser.g:3354:3: ( (lv_database_2_0= RULE_ID ) )
            // InternalCQLParser.g:3355:4: (lv_database_2_0= RULE_ID )
            {
            // InternalCQLParser.g:3355:4: (lv_database_2_0= RULE_ID )
            // InternalCQLParser.g:3356:5: lv_database_2_0= RULE_ID
            {
            lv_database_2_0=(Token)match(input,RULE_ID,FOLLOW_49); 

            					newLeafNode(lv_database_2_0, grammarAccess.getCreateDatabaseStreamAccess().getDatabaseIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDatabaseStreamRule());
            					}
            					setWithLastConsumed(
            						current,
            						"database",
            						lv_database_2_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            otherlv_3=(Token)match(input,TABLE,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateDatabaseStreamAccess().getTABLEKeyword_3());
            		
            // InternalCQLParser.g:3376:3: ( (lv_table_4_0= RULE_ID ) )
            // InternalCQLParser.g:3377:4: (lv_table_4_0= RULE_ID )
            {
            // InternalCQLParser.g:3377:4: (lv_table_4_0= RULE_ID )
            // InternalCQLParser.g:3378:5: lv_table_4_0= RULE_ID
            {
            lv_table_4_0=(Token)match(input,RULE_ID,FOLLOW_50); 

            					newLeafNode(lv_table_4_0, grammarAccess.getCreateDatabaseStreamAccess().getTableIDTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDatabaseStreamRule());
            					}
            					setWithLastConsumed(
            						current,
            						"table",
            						lv_table_4_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:3394:3: (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= ruleTime ) ) )?
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==EACH) ) {
                alt48=1;
            }
            switch (alt48) {
                case 1 :
                    // InternalCQLParser.g:3395:4: otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= ruleTime ) )
                    {
                    otherlv_5=(Token)match(input,EACH,FOLLOW_46); 

                    				newLeafNode(otherlv_5, grammarAccess.getCreateDatabaseStreamAccess().getEACHKeyword_5_0());
                    			
                    // InternalCQLParser.g:3399:4: ( (lv_size_6_0= RULE_INT ) )
                    // InternalCQLParser.g:3400:5: (lv_size_6_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3400:5: (lv_size_6_0= RULE_INT )
                    // InternalCQLParser.g:3401:6: lv_size_6_0= RULE_INT
                    {
                    lv_size_6_0=(Token)match(input,RULE_INT,FOLLOW_51); 

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

                    // InternalCQLParser.g:3417:4: ( (lv_unit_7_0= ruleTime ) )
                    // InternalCQLParser.g:3418:5: (lv_unit_7_0= ruleTime )
                    {
                    // InternalCQLParser.g:3418:5: (lv_unit_7_0= ruleTime )
                    // InternalCQLParser.g:3419:6: lv_unit_7_0= ruleTime
                    {

                    						newCompositeNode(grammarAccess.getCreateDatabaseStreamAccess().getUnitTimeEnumRuleCall_5_2_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_unit_7_0=ruleTime();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCreateDatabaseStreamRule());
                    						}
                    						set(
                    							current,
                    							"unit",
                    							lv_unit_7_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.Time");
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
    // $ANTLR end "ruleCreateDatabaseStream"


    // $ANTLR start "entryRuleCreateDatabaseSink"
    // InternalCQLParser.g:3441:1: entryRuleCreateDatabaseSink returns [EObject current=null] : iv_ruleCreateDatabaseSink= ruleCreateDatabaseSink EOF ;
    public final EObject entryRuleCreateDatabaseSink() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateDatabaseSink = null;


        try {
            // InternalCQLParser.g:3441:59: (iv_ruleCreateDatabaseSink= ruleCreateDatabaseSink EOF )
            // InternalCQLParser.g:3442:2: iv_ruleCreateDatabaseSink= ruleCreateDatabaseSink EOF
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
    // InternalCQLParser.g:3448:1: ruleCreateDatabaseSink returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= AS otherlv_2= DATABASE ( (lv_database_3_0= RULE_ID ) ) otherlv_4= TABLE ( (lv_table_5_0= RULE_ID ) ) (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )? ) ;
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
            // InternalCQLParser.g:3454:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= AS otherlv_2= DATABASE ( (lv_database_3_0= RULE_ID ) ) otherlv_4= TABLE ( (lv_table_5_0= RULE_ID ) ) (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )? ) )
            // InternalCQLParser.g:3455:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= AS otherlv_2= DATABASE ( (lv_database_3_0= RULE_ID ) ) otherlv_4= TABLE ( (lv_table_5_0= RULE_ID ) ) (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )? )
            {
            // InternalCQLParser.g:3455:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= AS otherlv_2= DATABASE ( (lv_database_3_0= RULE_ID ) ) otherlv_4= TABLE ( (lv_table_5_0= RULE_ID ) ) (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )? )
            // InternalCQLParser.g:3456:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= AS otherlv_2= DATABASE ( (lv_database_3_0= RULE_ID ) ) otherlv_4= TABLE ( (lv_table_5_0= RULE_ID ) ) (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )?
            {
            // InternalCQLParser.g:3456:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:3457:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:3457:4: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:3458:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_27); 

            					newLeafNode(lv_name_0_0, grammarAccess.getCreateDatabaseSinkAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDatabaseSinkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,AS,FOLLOW_48); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateDatabaseSinkAccess().getASKeyword_1());
            		
            otherlv_2=(Token)match(input,DATABASE,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateDatabaseSinkAccess().getDATABASEKeyword_2());
            		
            // InternalCQLParser.g:3482:3: ( (lv_database_3_0= RULE_ID ) )
            // InternalCQLParser.g:3483:4: (lv_database_3_0= RULE_ID )
            {
            // InternalCQLParser.g:3483:4: (lv_database_3_0= RULE_ID )
            // InternalCQLParser.g:3484:5: lv_database_3_0= RULE_ID
            {
            lv_database_3_0=(Token)match(input,RULE_ID,FOLLOW_49); 

            					newLeafNode(lv_database_3_0, grammarAccess.getCreateDatabaseSinkAccess().getDatabaseIDTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDatabaseSinkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"database",
            						lv_database_3_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            otherlv_4=(Token)match(input,TABLE,FOLLOW_5); 

            			newLeafNode(otherlv_4, grammarAccess.getCreateDatabaseSinkAccess().getTABLEKeyword_4());
            		
            // InternalCQLParser.g:3504:3: ( (lv_table_5_0= RULE_ID ) )
            // InternalCQLParser.g:3505:4: (lv_table_5_0= RULE_ID )
            {
            // InternalCQLParser.g:3505:4: (lv_table_5_0= RULE_ID )
            // InternalCQLParser.g:3506:5: lv_table_5_0= RULE_ID
            {
            lv_table_5_0=(Token)match(input,RULE_ID,FOLLOW_52); 

            					newLeafNode(lv_table_5_0, grammarAccess.getCreateDatabaseSinkAccess().getTableIDTerminalRuleCall_5_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDatabaseSinkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"table",
            						lv_table_5_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:3522:3: (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )?
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==AND) ) {
                alt50=1;
            }
            switch (alt50) {
                case 1 :
                    // InternalCQLParser.g:3523:4: otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) )
                    {
                    otherlv_6=(Token)match(input,AND,FOLLOW_53); 

                    				newLeafNode(otherlv_6, grammarAccess.getCreateDatabaseSinkAccess().getANDKeyword_6_0());
                    			
                    // InternalCQLParser.g:3527:4: ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) )
                    // InternalCQLParser.g:3528:5: ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) )
                    {
                    // InternalCQLParser.g:3528:5: ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) )
                    // InternalCQLParser.g:3529:6: (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE )
                    {
                    // InternalCQLParser.g:3529:6: (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE )
                    int alt49=2;
                    int LA49_0 = input.LA(1);

                    if ( (LA49_0==DROP) ) {
                        alt49=1;
                    }
                    else if ( (LA49_0==TRUNCATE) ) {
                        alt49=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 49, 0, input);

                        throw nvae;
                    }
                    switch (alt49) {
                        case 1 :
                            // InternalCQLParser.g:3530:7: lv_option_7_1= DROP
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
                            // InternalCQLParser.g:3541:7: lv_option_7_2= TRUNCATE
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
    // InternalCQLParser.g:3559:1: entryRuleCreateView returns [EObject current=null] : iv_ruleCreateView= ruleCreateView EOF ;
    public final EObject entryRuleCreateView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateView = null;


        try {
            // InternalCQLParser.g:3559:51: (iv_ruleCreateView= ruleCreateView EOF )
            // InternalCQLParser.g:3560:2: iv_ruleCreateView= ruleCreateView EOF
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
    // InternalCQLParser.g:3566:1: ruleCreateView returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= FROM ( (lv_select_2_0= ruleInnerSelect ) ) ) ;
    public final EObject ruleCreateView() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        EObject lv_select_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3572:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= FROM ( (lv_select_2_0= ruleInnerSelect ) ) ) )
            // InternalCQLParser.g:3573:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= FROM ( (lv_select_2_0= ruleInnerSelect ) ) )
            {
            // InternalCQLParser.g:3573:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= FROM ( (lv_select_2_0= ruleInnerSelect ) ) )
            // InternalCQLParser.g:3574:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= FROM ( (lv_select_2_0= ruleInnerSelect ) )
            {
            // InternalCQLParser.g:3574:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:3575:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:3575:4: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:3576:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_10); 

            					newLeafNode(lv_name_0_0, grammarAccess.getCreateViewAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateViewRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,FROM,FOLLOW_13); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateViewAccess().getFROMKeyword_1());
            		
            // InternalCQLParser.g:3596:3: ( (lv_select_2_0= ruleInnerSelect ) )
            // InternalCQLParser.g:3597:4: (lv_select_2_0= ruleInnerSelect )
            {
            // InternalCQLParser.g:3597:4: (lv_select_2_0= ruleInnerSelect )
            // InternalCQLParser.g:3598:5: lv_select_2_0= ruleInnerSelect
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
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.InnerSelect");
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
    // InternalCQLParser.g:3619:1: entryRuleCreateDataBaseJDBCConnection returns [EObject current=null] : iv_ruleCreateDataBaseJDBCConnection= ruleCreateDataBaseJDBCConnection EOF ;
    public final EObject entryRuleCreateDataBaseJDBCConnection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateDataBaseJDBCConnection = null;


        try {
            // InternalCQLParser.g:3619:69: (iv_ruleCreateDataBaseJDBCConnection= ruleCreateDataBaseJDBCConnection EOF )
            // InternalCQLParser.g:3620:2: iv_ruleCreateDataBaseJDBCConnection= ruleCreateDataBaseJDBCConnection EOF
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
    // InternalCQLParser.g:3626:1: ruleCreateDataBaseJDBCConnection returns [EObject current=null] : ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= JDBC ( (lv_server_6_0= RULE_ID ) ) (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )? ) ;
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
            // InternalCQLParser.g:3632:2: ( ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= JDBC ( (lv_server_6_0= RULE_ID ) ) (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )? ) )
            // InternalCQLParser.g:3633:2: ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= JDBC ( (lv_server_6_0= RULE_ID ) ) (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )? )
            {
            // InternalCQLParser.g:3633:2: ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= JDBC ( (lv_server_6_0= RULE_ID ) ) (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )? )
            // InternalCQLParser.g:3634:3: () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= JDBC ( (lv_server_6_0= RULE_ID ) ) (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )?
            {
            // InternalCQLParser.g:3634:3: ()
            // InternalCQLParser.g:3635:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getCreateDataBaseJDBCConnectionAccess().getCreateDataBaseConnectionJDBCAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,CREATE,FOLLOW_48); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getCREATEKeyword_1());
            		
            otherlv_2=(Token)match(input,DATABASE,FOLLOW_54); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getDATABASEKeyword_2());
            		
            otherlv_3=(Token)match(input,CONNECTION,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getCONNECTIONKeyword_3());
            		
            // InternalCQLParser.g:3653:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCQLParser.g:3654:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCQLParser.g:3654:4: (lv_name_4_0= RULE_ID )
            // InternalCQLParser.g:3655:5: lv_name_4_0= RULE_ID
            {
            lv_name_4_0=(Token)match(input,RULE_ID,FOLLOW_55); 

            					newLeafNode(lv_name_4_0, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getNameIDTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDataBaseJDBCConnectionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_4_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            otherlv_5=(Token)match(input,JDBC,FOLLOW_5); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getJDBCKeyword_5());
            		
            // InternalCQLParser.g:3675:3: ( (lv_server_6_0= RULE_ID ) )
            // InternalCQLParser.g:3676:4: (lv_server_6_0= RULE_ID )
            {
            // InternalCQLParser.g:3676:4: (lv_server_6_0= RULE_ID )
            // InternalCQLParser.g:3677:5: lv_server_6_0= RULE_ID
            {
            lv_server_6_0=(Token)match(input,RULE_ID,FOLLOW_56); 

            					newLeafNode(lv_server_6_0, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getServerIDTerminalRuleCall_6_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDataBaseJDBCConnectionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"server",
            						lv_server_6_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:3693:3: (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )?
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( (LA52_0==WITH) ) {
                alt52=1;
            }
            switch (alt52) {
                case 1 :
                    // InternalCQLParser.g:3694:4: otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )?
                    {
                    otherlv_7=(Token)match(input,WITH,FOLLOW_57); 

                    				newLeafNode(otherlv_7, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getWITHKeyword_7_0());
                    			
                    otherlv_8=(Token)match(input,USER,FOLLOW_5); 

                    				newLeafNode(otherlv_8, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getUSERKeyword_7_1());
                    			
                    // InternalCQLParser.g:3702:4: ( (lv_user_9_0= RULE_ID ) )
                    // InternalCQLParser.g:3703:5: (lv_user_9_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3703:5: (lv_user_9_0= RULE_ID )
                    // InternalCQLParser.g:3704:6: lv_user_9_0= RULE_ID
                    {
                    lv_user_9_0=(Token)match(input,RULE_ID,FOLLOW_58); 

                    						newLeafNode(lv_user_9_0, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getUserIDTerminalRuleCall_7_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateDataBaseJDBCConnectionRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"user",
                    							lv_user_9_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    					

                    }


                    }

                    otherlv_10=(Token)match(input,PASSWORD,FOLLOW_5); 

                    				newLeafNode(otherlv_10, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getPASSWORDKeyword_7_3());
                    			
                    // InternalCQLParser.g:3724:4: ( (lv_password_11_0= RULE_ID ) )
                    // InternalCQLParser.g:3725:5: (lv_password_11_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3725:5: (lv_password_11_0= RULE_ID )
                    // InternalCQLParser.g:3726:6: lv_password_11_0= RULE_ID
                    {
                    lv_password_11_0=(Token)match(input,RULE_ID,FOLLOW_59); 

                    						newLeafNode(lv_password_11_0, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getPasswordIDTerminalRuleCall_7_4_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateDataBaseJDBCConnectionRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"password",
                    							lv_password_11_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    					

                    }


                    }

                    // InternalCQLParser.g:3742:4: ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )?
                    int alt51=2;
                    int LA51_0 = input.LA(1);

                    if ( (LA51_0==NO_LAZY_CONNECTION_CHECK) ) {
                        alt51=1;
                    }
                    switch (alt51) {
                        case 1 :
                            // InternalCQLParser.g:3743:5: (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK )
                            {
                            // InternalCQLParser.g:3743:5: (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK )
                            // InternalCQLParser.g:3744:6: lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK
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
    // InternalCQLParser.g:3761:1: entryRuleCreateDataBaseGenericConnection returns [EObject current=null] : iv_ruleCreateDataBaseGenericConnection= ruleCreateDataBaseGenericConnection EOF ;
    public final EObject entryRuleCreateDataBaseGenericConnection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateDataBaseGenericConnection = null;


        try {
            // InternalCQLParser.g:3761:72: (iv_ruleCreateDataBaseGenericConnection= ruleCreateDataBaseGenericConnection EOF )
            // InternalCQLParser.g:3762:2: iv_ruleCreateDataBaseGenericConnection= ruleCreateDataBaseGenericConnection EOF
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
    // InternalCQLParser.g:3768:1: ruleCreateDataBaseGenericConnection returns [EObject current=null] : ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= AS ( (lv_driver_6_0= RULE_ID ) ) otherlv_7= TO ( (lv_source_8_0= RULE_ID ) ) (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )? (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )? ) ;
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
            // InternalCQLParser.g:3774:2: ( ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= AS ( (lv_driver_6_0= RULE_ID ) ) otherlv_7= TO ( (lv_source_8_0= RULE_ID ) ) (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )? (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )? ) )
            // InternalCQLParser.g:3775:2: ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= AS ( (lv_driver_6_0= RULE_ID ) ) otherlv_7= TO ( (lv_source_8_0= RULE_ID ) ) (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )? (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )? )
            {
            // InternalCQLParser.g:3775:2: ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= AS ( (lv_driver_6_0= RULE_ID ) ) otherlv_7= TO ( (lv_source_8_0= RULE_ID ) ) (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )? (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )? )
            // InternalCQLParser.g:3776:3: () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= AS ( (lv_driver_6_0= RULE_ID ) ) otherlv_7= TO ( (lv_source_8_0= RULE_ID ) ) (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )? (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )?
            {
            // InternalCQLParser.g:3776:3: ()
            // InternalCQLParser.g:3777:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getCreateDataBaseGenericConnectionAccess().getCreateDataBaseConnectionGenericAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,CREATE,FOLLOW_48); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateDataBaseGenericConnectionAccess().getCREATEKeyword_1());
            		
            otherlv_2=(Token)match(input,DATABASE,FOLLOW_54); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateDataBaseGenericConnectionAccess().getDATABASEKeyword_2());
            		
            otherlv_3=(Token)match(input,CONNECTION,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateDataBaseGenericConnectionAccess().getCONNECTIONKeyword_3());
            		
            // InternalCQLParser.g:3795:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCQLParser.g:3796:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCQLParser.g:3796:4: (lv_name_4_0= RULE_ID )
            // InternalCQLParser.g:3797:5: lv_name_4_0= RULE_ID
            {
            lv_name_4_0=(Token)match(input,RULE_ID,FOLLOW_27); 

            					newLeafNode(lv_name_4_0, grammarAccess.getCreateDataBaseGenericConnectionAccess().getNameIDTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDataBaseGenericConnectionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_4_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            otherlv_5=(Token)match(input,AS,FOLLOW_5); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateDataBaseGenericConnectionAccess().getASKeyword_5());
            		
            // InternalCQLParser.g:3817:3: ( (lv_driver_6_0= RULE_ID ) )
            // InternalCQLParser.g:3818:4: (lv_driver_6_0= RULE_ID )
            {
            // InternalCQLParser.g:3818:4: (lv_driver_6_0= RULE_ID )
            // InternalCQLParser.g:3819:5: lv_driver_6_0= RULE_ID
            {
            lv_driver_6_0=(Token)match(input,RULE_ID,FOLLOW_60); 

            					newLeafNode(lv_driver_6_0, grammarAccess.getCreateDataBaseGenericConnectionAccess().getDriverIDTerminalRuleCall_6_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDataBaseGenericConnectionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"driver",
            						lv_driver_6_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            otherlv_7=(Token)match(input,TO,FOLLOW_5); 

            			newLeafNode(otherlv_7, grammarAccess.getCreateDataBaseGenericConnectionAccess().getTOKeyword_7());
            		
            // InternalCQLParser.g:3839:3: ( (lv_source_8_0= RULE_ID ) )
            // InternalCQLParser.g:3840:4: (lv_source_8_0= RULE_ID )
            {
            // InternalCQLParser.g:3840:4: (lv_source_8_0= RULE_ID )
            // InternalCQLParser.g:3841:5: lv_source_8_0= RULE_ID
            {
            lv_source_8_0=(Token)match(input,RULE_ID,FOLLOW_61); 

            					newLeafNode(lv_source_8_0, grammarAccess.getCreateDataBaseGenericConnectionAccess().getSourceIDTerminalRuleCall_8_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateDataBaseGenericConnectionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"source",
            						lv_source_8_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:3857:3: (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==AT) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // InternalCQLParser.g:3858:4: otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) )
                    {
                    otherlv_9=(Token)match(input,AT,FOLLOW_5); 

                    				newLeafNode(otherlv_9, grammarAccess.getCreateDataBaseGenericConnectionAccess().getATKeyword_9_0());
                    			
                    // InternalCQLParser.g:3862:4: ( (lv_host_10_0= RULE_ID ) )
                    // InternalCQLParser.g:3863:5: (lv_host_10_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3863:5: (lv_host_10_0= RULE_ID )
                    // InternalCQLParser.g:3864:6: lv_host_10_0= RULE_ID
                    {
                    lv_host_10_0=(Token)match(input,RULE_ID,FOLLOW_45); 

                    						newLeafNode(lv_host_10_0, grammarAccess.getCreateDataBaseGenericConnectionAccess().getHostIDTerminalRuleCall_9_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateDataBaseGenericConnectionRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"host",
                    							lv_host_10_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    					

                    }


                    }

                    otherlv_11=(Token)match(input,Colon,FOLLOW_46); 

                    				newLeafNode(otherlv_11, grammarAccess.getCreateDataBaseGenericConnectionAccess().getColonKeyword_9_2());
                    			
                    // InternalCQLParser.g:3884:4: ( (lv_port_12_0= RULE_INT ) )
                    // InternalCQLParser.g:3885:5: (lv_port_12_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3885:5: (lv_port_12_0= RULE_INT )
                    // InternalCQLParser.g:3886:6: lv_port_12_0= RULE_INT
                    {
                    lv_port_12_0=(Token)match(input,RULE_INT,FOLLOW_56); 

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

            // InternalCQLParser.g:3903:3: (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )?
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==WITH) ) {
                alt55=1;
            }
            switch (alt55) {
                case 1 :
                    // InternalCQLParser.g:3904:4: otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )?
                    {
                    otherlv_13=(Token)match(input,WITH,FOLLOW_57); 

                    				newLeafNode(otherlv_13, grammarAccess.getCreateDataBaseGenericConnectionAccess().getWITHKeyword_10_0());
                    			
                    otherlv_14=(Token)match(input,USER,FOLLOW_5); 

                    				newLeafNode(otherlv_14, grammarAccess.getCreateDataBaseGenericConnectionAccess().getUSERKeyword_10_1());
                    			
                    // InternalCQLParser.g:3912:4: ( (lv_user_15_0= RULE_ID ) )
                    // InternalCQLParser.g:3913:5: (lv_user_15_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3913:5: (lv_user_15_0= RULE_ID )
                    // InternalCQLParser.g:3914:6: lv_user_15_0= RULE_ID
                    {
                    lv_user_15_0=(Token)match(input,RULE_ID,FOLLOW_58); 

                    						newLeafNode(lv_user_15_0, grammarAccess.getCreateDataBaseGenericConnectionAccess().getUserIDTerminalRuleCall_10_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateDataBaseGenericConnectionRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"user",
                    							lv_user_15_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    					

                    }


                    }

                    otherlv_16=(Token)match(input,PASSWORD,FOLLOW_5); 

                    				newLeafNode(otherlv_16, grammarAccess.getCreateDataBaseGenericConnectionAccess().getPASSWORDKeyword_10_3());
                    			
                    // InternalCQLParser.g:3934:4: ( (lv_password_17_0= RULE_ID ) )
                    // InternalCQLParser.g:3935:5: (lv_password_17_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3935:5: (lv_password_17_0= RULE_ID )
                    // InternalCQLParser.g:3936:6: lv_password_17_0= RULE_ID
                    {
                    lv_password_17_0=(Token)match(input,RULE_ID,FOLLOW_59); 

                    						newLeafNode(lv_password_17_0, grammarAccess.getCreateDataBaseGenericConnectionAccess().getPasswordIDTerminalRuleCall_10_4_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateDataBaseGenericConnectionRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"password",
                    							lv_password_17_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    					

                    }


                    }

                    // InternalCQLParser.g:3952:4: ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )?
                    int alt54=2;
                    int LA54_0 = input.LA(1);

                    if ( (LA54_0==NO_LAZY_CONNECTION_CHECK) ) {
                        alt54=1;
                    }
                    switch (alt54) {
                        case 1 :
                            // InternalCQLParser.g:3953:5: (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK )
                            {
                            // InternalCQLParser.g:3953:5: (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK )
                            // InternalCQLParser.g:3954:6: lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK
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
    // InternalCQLParser.g:3971:1: entryRuleDropDatabaseConnection returns [EObject current=null] : iv_ruleDropDatabaseConnection= ruleDropDatabaseConnection EOF ;
    public final EObject entryRuleDropDatabaseConnection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDropDatabaseConnection = null;


        try {
            // InternalCQLParser.g:3971:63: (iv_ruleDropDatabaseConnection= ruleDropDatabaseConnection EOF )
            // InternalCQLParser.g:3972:2: iv_ruleDropDatabaseConnection= ruleDropDatabaseConnection EOF
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
    // InternalCQLParser.g:3978:1: ruleDropDatabaseConnection returns [EObject current=null] : ( () otherlv_1= DROP otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) ) ;
    public final EObject ruleDropDatabaseConnection() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token lv_name_4_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:3984:2: ( ( () otherlv_1= DROP otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:3985:2: ( () otherlv_1= DROP otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) )
            {
            // InternalCQLParser.g:3985:2: ( () otherlv_1= DROP otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) )
            // InternalCQLParser.g:3986:3: () otherlv_1= DROP otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) )
            {
            // InternalCQLParser.g:3986:3: ()
            // InternalCQLParser.g:3987:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getDropDatabaseConnectionAccess().getDropDatabaseConnectionAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,DROP,FOLLOW_48); 

            			newLeafNode(otherlv_1, grammarAccess.getDropDatabaseConnectionAccess().getDROPKeyword_1());
            		
            otherlv_2=(Token)match(input,DATABASE,FOLLOW_54); 

            			newLeafNode(otherlv_2, grammarAccess.getDropDatabaseConnectionAccess().getDATABASEKeyword_2());
            		
            otherlv_3=(Token)match(input,CONNECTION,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getDropDatabaseConnectionAccess().getCONNECTIONKeyword_3());
            		
            // InternalCQLParser.g:4005:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCQLParser.g:4006:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCQLParser.g:4006:4: (lv_name_4_0= RULE_ID )
            // InternalCQLParser.g:4007:5: lv_name_4_0= RULE_ID
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
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

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
    // InternalCQLParser.g:4027:1: entryRuleContextStoreType returns [EObject current=null] : iv_ruleContextStoreType= ruleContextStoreType EOF ;
    public final EObject entryRuleContextStoreType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleContextStoreType = null;


        try {
            // InternalCQLParser.g:4027:57: (iv_ruleContextStoreType= ruleContextStoreType EOF )
            // InternalCQLParser.g:4028:2: iv_ruleContextStoreType= ruleContextStoreType EOF
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
    // InternalCQLParser.g:4034:1: ruleContextStoreType returns [EObject current=null] : ( ( (lv_type_0_0= SINGLE ) ) | ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? ) ) ;
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
            // InternalCQLParser.g:4040:2: ( ( ( (lv_type_0_0= SINGLE ) ) | ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? ) ) )
            // InternalCQLParser.g:4041:2: ( ( (lv_type_0_0= SINGLE ) ) | ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? ) )
            {
            // InternalCQLParser.g:4041:2: ( ( (lv_type_0_0= SINGLE ) ) | ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? ) )
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( (LA57_0==SINGLE) ) {
                alt57=1;
            }
            else if ( (LA57_0==MULTI) ) {
                alt57=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 57, 0, input);

                throw nvae;
            }
            switch (alt57) {
                case 1 :
                    // InternalCQLParser.g:4042:3: ( (lv_type_0_0= SINGLE ) )
                    {
                    // InternalCQLParser.g:4042:3: ( (lv_type_0_0= SINGLE ) )
                    // InternalCQLParser.g:4043:4: (lv_type_0_0= SINGLE )
                    {
                    // InternalCQLParser.g:4043:4: (lv_type_0_0= SINGLE )
                    // InternalCQLParser.g:4044:5: lv_type_0_0= SINGLE
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
                    // InternalCQLParser.g:4057:3: ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? )
                    {
                    // InternalCQLParser.g:4057:3: ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? )
                    // InternalCQLParser.g:4058:4: ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )?
                    {
                    // InternalCQLParser.g:4058:4: ( (lv_type_1_0= MULTI ) )
                    // InternalCQLParser.g:4059:5: (lv_type_1_0= MULTI )
                    {
                    // InternalCQLParser.g:4059:5: (lv_type_1_0= MULTI )
                    // InternalCQLParser.g:4060:6: lv_type_1_0= MULTI
                    {
                    lv_type_1_0=(Token)match(input,MULTI,FOLLOW_46); 

                    						newLeafNode(lv_type_1_0, grammarAccess.getContextStoreTypeAccess().getTypeMULTIKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getContextStoreTypeRule());
                    						}
                    						setWithLastConsumed(current, "type", lv_type_1_0, "MULTI");
                    					

                    }


                    }

                    // InternalCQLParser.g:4072:4: ( (lv_size_2_0= RULE_INT ) )
                    // InternalCQLParser.g:4073:5: (lv_size_2_0= RULE_INT )
                    {
                    // InternalCQLParser.g:4073:5: (lv_size_2_0= RULE_INT )
                    // InternalCQLParser.g:4074:6: lv_size_2_0= RULE_INT
                    {
                    lv_size_2_0=(Token)match(input,RULE_INT,FOLLOW_62); 

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

                    // InternalCQLParser.g:4090:4: (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )?
                    int alt56=2;
                    int LA56_0 = input.LA(1);

                    if ( (LA56_0==PARTITION) ) {
                        alt56=1;
                    }
                    switch (alt56) {
                        case 1 :
                            // InternalCQLParser.g:4091:5: otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) )
                            {
                            otherlv_3=(Token)match(input,PARTITION,FOLLOW_18); 

                            					newLeafNode(otherlv_3, grammarAccess.getContextStoreTypeAccess().getPARTITIONKeyword_1_2_0());
                            				
                            otherlv_4=(Token)match(input,BY,FOLLOW_46); 

                            					newLeafNode(otherlv_4, grammarAccess.getContextStoreTypeAccess().getBYKeyword_1_2_1());
                            				
                            // InternalCQLParser.g:4099:5: ( (lv_partition_5_0= RULE_INT ) )
                            // InternalCQLParser.g:4100:6: (lv_partition_5_0= RULE_INT )
                            {
                            // InternalCQLParser.g:4100:6: (lv_partition_5_0= RULE_INT )
                            // InternalCQLParser.g:4101:7: lv_partition_5_0= RULE_INT
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
    // InternalCQLParser.g:4123:1: entryRuleCreateContextStore returns [EObject current=null] : iv_ruleCreateContextStore= ruleCreateContextStore EOF ;
    public final EObject entryRuleCreateContextStore() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateContextStore = null;


        try {
            // InternalCQLParser.g:4123:59: (iv_ruleCreateContextStore= ruleCreateContextStore EOF )
            // InternalCQLParser.g:4124:2: iv_ruleCreateContextStore= ruleCreateContextStore EOF
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
    // InternalCQLParser.g:4130:1: ruleCreateContextStore returns [EObject current=null] : ( () otherlv_1= CREATE otherlv_2= CONTEXT otherlv_3= STORE ( (lv_attributes_4_0= ruleSchemaDefinition ) ) otherlv_5= AS ( (lv_contextType_6_0= ruleContextStoreType ) ) ) ;
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
            // InternalCQLParser.g:4136:2: ( ( () otherlv_1= CREATE otherlv_2= CONTEXT otherlv_3= STORE ( (lv_attributes_4_0= ruleSchemaDefinition ) ) otherlv_5= AS ( (lv_contextType_6_0= ruleContextStoreType ) ) ) )
            // InternalCQLParser.g:4137:2: ( () otherlv_1= CREATE otherlv_2= CONTEXT otherlv_3= STORE ( (lv_attributes_4_0= ruleSchemaDefinition ) ) otherlv_5= AS ( (lv_contextType_6_0= ruleContextStoreType ) ) )
            {
            // InternalCQLParser.g:4137:2: ( () otherlv_1= CREATE otherlv_2= CONTEXT otherlv_3= STORE ( (lv_attributes_4_0= ruleSchemaDefinition ) ) otherlv_5= AS ( (lv_contextType_6_0= ruleContextStoreType ) ) )
            // InternalCQLParser.g:4138:3: () otherlv_1= CREATE otherlv_2= CONTEXT otherlv_3= STORE ( (lv_attributes_4_0= ruleSchemaDefinition ) ) otherlv_5= AS ( (lv_contextType_6_0= ruleContextStoreType ) )
            {
            // InternalCQLParser.g:4138:3: ()
            // InternalCQLParser.g:4139:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getCreateContextStoreAccess().getCreateContextStoreAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,CREATE,FOLLOW_63); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateContextStoreAccess().getCREATEKeyword_1());
            		
            otherlv_2=(Token)match(input,CONTEXT,FOLLOW_64); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateContextStoreAccess().getCONTEXTKeyword_2());
            		
            otherlv_3=(Token)match(input,STORE,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateContextStoreAccess().getSTOREKeyword_3());
            		
            // InternalCQLParser.g:4157:3: ( (lv_attributes_4_0= ruleSchemaDefinition ) )
            // InternalCQLParser.g:4158:4: (lv_attributes_4_0= ruleSchemaDefinition )
            {
            // InternalCQLParser.g:4158:4: (lv_attributes_4_0= ruleSchemaDefinition )
            // InternalCQLParser.g:4159:5: lv_attributes_4_0= ruleSchemaDefinition
            {

            					newCompositeNode(grammarAccess.getCreateContextStoreAccess().getAttributesSchemaDefinitionParserRuleCall_4_0());
            				
            pushFollow(FOLLOW_27);
            lv_attributes_4_0=ruleSchemaDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateContextStoreRule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_4_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.SchemaDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_5=(Token)match(input,AS,FOLLOW_65); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateContextStoreAccess().getASKeyword_5());
            		
            // InternalCQLParser.g:4180:3: ( (lv_contextType_6_0= ruleContextStoreType ) )
            // InternalCQLParser.g:4181:4: (lv_contextType_6_0= ruleContextStoreType )
            {
            // InternalCQLParser.g:4181:4: (lv_contextType_6_0= ruleContextStoreType )
            // InternalCQLParser.g:4182:5: lv_contextType_6_0= ruleContextStoreType
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
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ContextStoreType");
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
    // InternalCQLParser.g:4203:1: entryRuleDropContextStore returns [EObject current=null] : iv_ruleDropContextStore= ruleDropContextStore EOF ;
    public final EObject entryRuleDropContextStore() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDropContextStore = null;


        try {
            // InternalCQLParser.g:4203:57: (iv_ruleDropContextStore= ruleDropContextStore EOF )
            // InternalCQLParser.g:4204:2: iv_ruleDropContextStore= ruleDropContextStore EOF
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
    // InternalCQLParser.g:4210:1: ruleDropContextStore returns [EObject current=null] : ( () otherlv_1= DROP otherlv_2= CONTEXT otherlv_3= STORE ( (lv_name_4_0= RULE_ID ) ) (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )? ) ;
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
            // InternalCQLParser.g:4216:2: ( ( () otherlv_1= DROP otherlv_2= CONTEXT otherlv_3= STORE ( (lv_name_4_0= RULE_ID ) ) (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )? ) )
            // InternalCQLParser.g:4217:2: ( () otherlv_1= DROP otherlv_2= CONTEXT otherlv_3= STORE ( (lv_name_4_0= RULE_ID ) ) (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )? )
            {
            // InternalCQLParser.g:4217:2: ( () otherlv_1= DROP otherlv_2= CONTEXT otherlv_3= STORE ( (lv_name_4_0= RULE_ID ) ) (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )? )
            // InternalCQLParser.g:4218:3: () otherlv_1= DROP otherlv_2= CONTEXT otherlv_3= STORE ( (lv_name_4_0= RULE_ID ) ) (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )?
            {
            // InternalCQLParser.g:4218:3: ()
            // InternalCQLParser.g:4219:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getDropContextStoreAccess().getDropContextStoreAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,DROP,FOLLOW_63); 

            			newLeafNode(otherlv_1, grammarAccess.getDropContextStoreAccess().getDROPKeyword_1());
            		
            otherlv_2=(Token)match(input,CONTEXT,FOLLOW_64); 

            			newLeafNode(otherlv_2, grammarAccess.getDropContextStoreAccess().getCONTEXTKeyword_2());
            		
            otherlv_3=(Token)match(input,STORE,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getDropContextStoreAccess().getSTOREKeyword_3());
            		
            // InternalCQLParser.g:4237:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCQLParser.g:4238:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCQLParser.g:4238:4: (lv_name_4_0= RULE_ID )
            // InternalCQLParser.g:4239:5: lv_name_4_0= RULE_ID
            {
            lv_name_4_0=(Token)match(input,RULE_ID,FOLLOW_66); 

            					newLeafNode(lv_name_4_0, grammarAccess.getDropContextStoreAccess().getNameIDTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDropContextStoreRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_4_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:4255:3: (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )?
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==IF) ) {
                alt58=1;
            }
            switch (alt58) {
                case 1 :
                    // InternalCQLParser.g:4256:4: otherlv_5= IF ( (lv_exists_6_0= EXISTS ) )
                    {
                    otherlv_5=(Token)match(input,IF,FOLLOW_67); 

                    				newLeafNode(otherlv_5, grammarAccess.getDropContextStoreAccess().getIFKeyword_5_0());
                    			
                    // InternalCQLParser.g:4260:4: ( (lv_exists_6_0= EXISTS ) )
                    // InternalCQLParser.g:4261:5: (lv_exists_6_0= EXISTS )
                    {
                    // InternalCQLParser.g:4261:5: (lv_exists_6_0= EXISTS )
                    // InternalCQLParser.g:4262:6: lv_exists_6_0= EXISTS
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
    // InternalCQLParser.g:4279:1: entryRuleStreamTo returns [EObject current=null] : iv_ruleStreamTo= ruleStreamTo EOF ;
    public final EObject entryRuleStreamTo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStreamTo = null;


        try {
            // InternalCQLParser.g:4279:49: (iv_ruleStreamTo= ruleStreamTo EOF )
            // InternalCQLParser.g:4280:2: iv_ruleStreamTo= ruleStreamTo EOF
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
    // InternalCQLParser.g:4286:1: ruleStreamTo returns [EObject current=null] : ( () otherlv_1= STREAM otherlv_2= TO ( (lv_name_3_0= RULE_ID ) ) ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) ) ) ;
    public final EObject ruleStreamTo() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token lv_name_3_0=null;
        Token lv_inputname_5_0=null;
        EObject lv_statement_4_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:4292:2: ( ( () otherlv_1= STREAM otherlv_2= TO ( (lv_name_3_0= RULE_ID ) ) ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) ) ) )
            // InternalCQLParser.g:4293:2: ( () otherlv_1= STREAM otherlv_2= TO ( (lv_name_3_0= RULE_ID ) ) ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) ) )
            {
            // InternalCQLParser.g:4293:2: ( () otherlv_1= STREAM otherlv_2= TO ( (lv_name_3_0= RULE_ID ) ) ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:4294:3: () otherlv_1= STREAM otherlv_2= TO ( (lv_name_3_0= RULE_ID ) ) ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) )
            {
            // InternalCQLParser.g:4294:3: ()
            // InternalCQLParser.g:4295:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getStreamToAccess().getStreamToAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,STREAM,FOLLOW_60); 

            			newLeafNode(otherlv_1, grammarAccess.getStreamToAccess().getSTREAMKeyword_1());
            		
            otherlv_2=(Token)match(input,TO,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getStreamToAccess().getTOKeyword_2());
            		
            // InternalCQLParser.g:4309:3: ( (lv_name_3_0= RULE_ID ) )
            // InternalCQLParser.g:4310:4: (lv_name_3_0= RULE_ID )
            {
            // InternalCQLParser.g:4310:4: (lv_name_3_0= RULE_ID )
            // InternalCQLParser.g:4311:5: lv_name_3_0= RULE_ID
            {
            lv_name_3_0=(Token)match(input,RULE_ID,FOLLOW_68); 

            					newLeafNode(lv_name_3_0, grammarAccess.getStreamToAccess().getNameIDTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getStreamToRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_3_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:4327:3: ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) )
            int alt59=2;
            int LA59_0 = input.LA(1);

            if ( (LA59_0==SELECT) ) {
                alt59=1;
            }
            else if ( (LA59_0==RULE_ID) ) {
                alt59=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 59, 0, input);

                throw nvae;
            }
            switch (alt59) {
                case 1 :
                    // InternalCQLParser.g:4328:4: ( (lv_statement_4_0= ruleInnerSelect2 ) )
                    {
                    // InternalCQLParser.g:4328:4: ( (lv_statement_4_0= ruleInnerSelect2 ) )
                    // InternalCQLParser.g:4329:5: (lv_statement_4_0= ruleInnerSelect2 )
                    {
                    // InternalCQLParser.g:4329:5: (lv_statement_4_0= ruleInnerSelect2 )
                    // InternalCQLParser.g:4330:6: lv_statement_4_0= ruleInnerSelect2
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.InnerSelect2");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:4348:4: ( (lv_inputname_5_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:4348:4: ( (lv_inputname_5_0= RULE_ID ) )
                    // InternalCQLParser.g:4349:5: (lv_inputname_5_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4349:5: (lv_inputname_5_0= RULE_ID )
                    // InternalCQLParser.g:4350:6: lv_inputname_5_0= RULE_ID
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    					

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
    // InternalCQLParser.g:4371:1: entryRuleDropStream returns [EObject current=null] : iv_ruleDropStream= ruleDropStream EOF ;
    public final EObject entryRuleDropStream() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDropStream = null;


        try {
            // InternalCQLParser.g:4371:51: (iv_ruleDropStream= ruleDropStream EOF )
            // InternalCQLParser.g:4372:2: iv_ruleDropStream= ruleDropStream EOF
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
    // InternalCQLParser.g:4378:1: ruleDropStream returns [EObject current=null] : ( () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )? ) ;
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
            // InternalCQLParser.g:4384:2: ( ( () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )? ) )
            // InternalCQLParser.g:4385:2: ( () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )? )
            {
            // InternalCQLParser.g:4385:2: ( () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )? )
            // InternalCQLParser.g:4386:3: () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )?
            {
            // InternalCQLParser.g:4386:3: ()
            // InternalCQLParser.g:4387:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getDropStreamAccess().getDropStreamAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,DROP,FOLLOW_42); 

            			newLeafNode(otherlv_1, grammarAccess.getDropStreamAccess().getDROPKeyword_1());
            		
            // InternalCQLParser.g:4397:3: ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) )
            // InternalCQLParser.g:4398:4: ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) )
            {
            // InternalCQLParser.g:4398:4: ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) )
            // InternalCQLParser.g:4399:5: (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW )
            {
            // InternalCQLParser.g:4399:5: (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW )
            int alt60=3;
            switch ( input.LA(1) ) {
            case SINK:
                {
                alt60=1;
                }
                break;
            case STREAM:
                {
                alt60=2;
                }
                break;
            case VIEW:
                {
                alt60=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 60, 0, input);

                throw nvae;
            }

            switch (alt60) {
                case 1 :
                    // InternalCQLParser.g:4400:6: lv_name_2_1= SINK
                    {
                    lv_name_2_1=(Token)match(input,SINK,FOLLOW_5); 

                    						newLeafNode(lv_name_2_1, grammarAccess.getDropStreamAccess().getNameSINKKeyword_2_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropStreamRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_2_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:4411:6: lv_name_2_2= STREAM
                    {
                    lv_name_2_2=(Token)match(input,STREAM,FOLLOW_5); 

                    						newLeafNode(lv_name_2_2, grammarAccess.getDropStreamAccess().getNameSTREAMKeyword_2_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropStreamRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_2_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:4422:6: lv_name_2_3= VIEW
                    {
                    lv_name_2_3=(Token)match(input,VIEW,FOLLOW_5); 

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

            // InternalCQLParser.g:4435:3: ( (lv_stream_3_0= RULE_ID ) )
            // InternalCQLParser.g:4436:4: (lv_stream_3_0= RULE_ID )
            {
            // InternalCQLParser.g:4436:4: (lv_stream_3_0= RULE_ID )
            // InternalCQLParser.g:4437:5: lv_stream_3_0= RULE_ID
            {
            lv_stream_3_0=(Token)match(input,RULE_ID,FOLLOW_66); 

            					newLeafNode(lv_stream_3_0, grammarAccess.getDropStreamAccess().getStreamIDTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDropStreamRule());
            					}
            					setWithLastConsumed(
            						current,
            						"stream",
            						lv_stream_3_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:4453:3: ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )?
            int alt61=2;
            int LA61_0 = input.LA(1);

            if ( (LA61_0==IF) ) {
                alt61=1;
            }
            switch (alt61) {
                case 1 :
                    // InternalCQLParser.g:4454:4: ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS
                    {
                    // InternalCQLParser.g:4454:4: ( (lv_exists_4_0= IF ) )
                    // InternalCQLParser.g:4455:5: (lv_exists_4_0= IF )
                    {
                    // InternalCQLParser.g:4455:5: (lv_exists_4_0= IF )
                    // InternalCQLParser.g:4456:6: lv_exists_4_0= IF
                    {
                    lv_exists_4_0=(Token)match(input,IF,FOLLOW_67); 

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
    // InternalCQLParser.g:4477:1: entryRuleUserManagement returns [EObject current=null] : iv_ruleUserManagement= ruleUserManagement EOF ;
    public final EObject entryRuleUserManagement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleUserManagement = null;


        try {
            // InternalCQLParser.g:4477:55: (iv_ruleUserManagement= ruleUserManagement EOF )
            // InternalCQLParser.g:4478:2: iv_ruleUserManagement= ruleUserManagement EOF
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
    // InternalCQLParser.g:4484:1: ruleUserManagement returns [EObject current=null] : ( () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )? ) ;
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
            // InternalCQLParser.g:4490:2: ( ( () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )? ) )
            // InternalCQLParser.g:4491:2: ( () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )? )
            {
            // InternalCQLParser.g:4491:2: ( () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )? )
            // InternalCQLParser.g:4492:3: () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )?
            {
            // InternalCQLParser.g:4492:3: ()
            // InternalCQLParser.g:4493:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getUserManagementAccess().getUserManagementAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:4499:3: ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) )
            // InternalCQLParser.g:4500:4: ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) )
            {
            // InternalCQLParser.g:4500:4: ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) )
            // InternalCQLParser.g:4501:5: (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP )
            {
            // InternalCQLParser.g:4501:5: (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP )
            int alt62=3;
            switch ( input.LA(1) ) {
            case CREATE:
                {
                alt62=1;
                }
                break;
            case ALTER:
                {
                alt62=2;
                }
                break;
            case DROP:
                {
                alt62=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 62, 0, input);

                throw nvae;
            }

            switch (alt62) {
                case 1 :
                    // InternalCQLParser.g:4502:6: lv_name_1_1= CREATE
                    {
                    lv_name_1_1=(Token)match(input,CREATE,FOLLOW_69); 

                    						newLeafNode(lv_name_1_1, grammarAccess.getUserManagementAccess().getNameCREATEKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:4513:6: lv_name_1_2= ALTER
                    {
                    lv_name_1_2=(Token)match(input,ALTER,FOLLOW_69); 

                    						newLeafNode(lv_name_1_2, grammarAccess.getUserManagementAccess().getNameALTERKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:4524:6: lv_name_1_3= DROP
                    {
                    lv_name_1_3=(Token)match(input,DROP,FOLLOW_69); 

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

            // InternalCQLParser.g:4537:3: ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) )
            // InternalCQLParser.g:4538:4: ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) )
            {
            // InternalCQLParser.g:4538:4: ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) )
            // InternalCQLParser.g:4539:5: (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT )
            {
            // InternalCQLParser.g:4539:5: (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT )
            int alt63=3;
            switch ( input.LA(1) ) {
            case USER:
                {
                alt63=1;
                }
                break;
            case ROLE:
                {
                alt63=2;
                }
                break;
            case TENANT:
                {
                alt63=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 63, 0, input);

                throw nvae;
            }

            switch (alt63) {
                case 1 :
                    // InternalCQLParser.g:4540:6: lv_subject_2_1= USER
                    {
                    lv_subject_2_1=(Token)match(input,USER,FOLLOW_5); 

                    						newLeafNode(lv_subject_2_1, grammarAccess.getUserManagementAccess().getSubjectUSERKeyword_2_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserManagementRule());
                    						}
                    						setWithLastConsumed(current, "subject", lv_subject_2_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:4551:6: lv_subject_2_2= ROLE
                    {
                    lv_subject_2_2=(Token)match(input,ROLE,FOLLOW_5); 

                    						newLeafNode(lv_subject_2_2, grammarAccess.getUserManagementAccess().getSubjectROLEKeyword_2_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserManagementRule());
                    						}
                    						setWithLastConsumed(current, "subject", lv_subject_2_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:4562:6: lv_subject_2_3= TENANT
                    {
                    lv_subject_2_3=(Token)match(input,TENANT,FOLLOW_5); 

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

            // InternalCQLParser.g:4575:3: ( (lv_subjectName_3_0= RULE_ID ) )
            // InternalCQLParser.g:4576:4: (lv_subjectName_3_0= RULE_ID )
            {
            // InternalCQLParser.g:4576:4: (lv_subjectName_3_0= RULE_ID )
            // InternalCQLParser.g:4577:5: lv_subjectName_3_0= RULE_ID
            {
            lv_subjectName_3_0=(Token)match(input,RULE_ID,FOLLOW_70); 

            					newLeafNode(lv_subjectName_3_0, grammarAccess.getUserManagementAccess().getSubjectNameIDTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getUserManagementRule());
            					}
            					setWithLastConsumed(
            						current,
            						"subjectName",
            						lv_subjectName_3_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:4593:3: (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )?
            int alt64=2;
            int LA64_0 = input.LA(1);

            if ( (LA64_0==IDENTIFIED) ) {
                alt64=1;
            }
            switch (alt64) {
                case 1 :
                    // InternalCQLParser.g:4594:4: otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) )
                    {
                    otherlv_4=(Token)match(input,IDENTIFIED,FOLLOW_18); 

                    				newLeafNode(otherlv_4, grammarAccess.getUserManagementAccess().getIDENTIFIEDKeyword_4_0());
                    			
                    otherlv_5=(Token)match(input,BY,FOLLOW_34); 

                    				newLeafNode(otherlv_5, grammarAccess.getUserManagementAccess().getBYKeyword_4_1());
                    			
                    // InternalCQLParser.g:4602:4: ( (lv_password_6_0= RULE_STRING ) )
                    // InternalCQLParser.g:4603:5: (lv_password_6_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:4603:5: (lv_password_6_0= RULE_STRING )
                    // InternalCQLParser.g:4604:6: lv_password_6_0= RULE_STRING
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
    // InternalCQLParser.g:4625:1: entryRuleRightsManagement returns [EObject current=null] : iv_ruleRightsManagement= ruleRightsManagement EOF ;
    public final EObject entryRuleRightsManagement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRightsManagement = null;


        try {
            // InternalCQLParser.g:4625:57: (iv_ruleRightsManagement= ruleRightsManagement EOF )
            // InternalCQLParser.g:4626:2: iv_ruleRightsManagement= ruleRightsManagement EOF
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
    // InternalCQLParser.g:4632:1: ruleRightsManagement returns [EObject current=null] : ( ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) ) | ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) ) ) ;
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
            // InternalCQLParser.g:4638:2: ( ( ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) ) | ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) ) ) )
            // InternalCQLParser.g:4639:2: ( ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) ) | ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) ) )
            {
            // InternalCQLParser.g:4639:2: ( ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) ) | ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) ) )
            int alt71=2;
            int LA71_0 = input.LA(1);

            if ( (LA71_0==GRANT) ) {
                alt71=1;
            }
            else if ( (LA71_0==REVOKE) ) {
                alt71=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 71, 0, input);

                throw nvae;
            }
            switch (alt71) {
                case 1 :
                    // InternalCQLParser.g:4640:3: ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) )
                    {
                    // InternalCQLParser.g:4640:3: ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) )
                    // InternalCQLParser.g:4641:4: () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:4641:4: ()
                    // InternalCQLParser.g:4642:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getRightsManagementAccess().getRightsManagementAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4648:4: ( (lv_name_1_0= GRANT ) )
                    // InternalCQLParser.g:4649:5: (lv_name_1_0= GRANT )
                    {
                    // InternalCQLParser.g:4649:5: (lv_name_1_0= GRANT )
                    // InternalCQLParser.g:4650:6: lv_name_1_0= GRANT
                    {
                    lv_name_1_0=(Token)match(input,GRANT,FOLLOW_5); 

                    						newLeafNode(lv_name_1_0, grammarAccess.getRightsManagementAccess().getNameGRANTKeyword_0_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_0, "GRANT");
                    					

                    }


                    }

                    // InternalCQLParser.g:4662:4: ( (lv_operations_2_0= RULE_ID ) )
                    // InternalCQLParser.g:4663:5: (lv_operations_2_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4663:5: (lv_operations_2_0= RULE_ID )
                    // InternalCQLParser.g:4664:6: lv_operations_2_0= RULE_ID
                    {
                    lv_operations_2_0=(Token)match(input,RULE_ID,FOLLOW_71); 

                    						newLeafNode(lv_operations_2_0, grammarAccess.getRightsManagementAccess().getOperationsIDTerminalRuleCall_0_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsManagementRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"operations",
                    							lv_operations_2_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    					

                    }


                    }

                    // InternalCQLParser.g:4680:4: (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )*
                    loop65:
                    do {
                        int alt65=2;
                        int LA65_0 = input.LA(1);

                        if ( (LA65_0==Comma) ) {
                            alt65=1;
                        }


                        switch (alt65) {
                    	case 1 :
                    	    // InternalCQLParser.g:4681:5: otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) )
                    	    {
                    	    otherlv_3=(Token)match(input,Comma,FOLLOW_5); 

                    	    					newLeafNode(otherlv_3, grammarAccess.getRightsManagementAccess().getCommaKeyword_0_3_0());
                    	    				
                    	    // InternalCQLParser.g:4685:5: ( (lv_operations_4_0= RULE_ID ) )
                    	    // InternalCQLParser.g:4686:6: (lv_operations_4_0= RULE_ID )
                    	    {
                    	    // InternalCQLParser.g:4686:6: (lv_operations_4_0= RULE_ID )
                    	    // InternalCQLParser.g:4687:7: lv_operations_4_0= RULE_ID
                    	    {
                    	    lv_operations_4_0=(Token)match(input,RULE_ID,FOLLOW_71); 

                    	    							newLeafNode(lv_operations_4_0, grammarAccess.getRightsManagementAccess().getOperationsIDTerminalRuleCall_0_3_1_0());
                    	    						

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getRightsManagementRule());
                    	    							}
                    	    							addWithLastConsumed(
                    	    								current,
                    	    								"operations",
                    	    								lv_operations_4_0,
                    	    								"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop65;
                        }
                    } while (true);

                    // InternalCQLParser.g:4704:4: (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )?
                    int alt67=2;
                    int LA67_0 = input.LA(1);

                    if ( (LA67_0==ON) ) {
                        alt67=1;
                    }
                    switch (alt67) {
                        case 1 :
                            // InternalCQLParser.g:4705:5: otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )*
                            {
                            otherlv_5=(Token)match(input,ON,FOLLOW_5); 

                            					newLeafNode(otherlv_5, grammarAccess.getRightsManagementAccess().getONKeyword_0_4_0());
                            				
                            // InternalCQLParser.g:4709:5: ( (lv_operations2_6_0= RULE_ID ) )
                            // InternalCQLParser.g:4710:6: (lv_operations2_6_0= RULE_ID )
                            {
                            // InternalCQLParser.g:4710:6: (lv_operations2_6_0= RULE_ID )
                            // InternalCQLParser.g:4711:7: lv_operations2_6_0= RULE_ID
                            {
                            lv_operations2_6_0=(Token)match(input,RULE_ID,FOLLOW_72); 

                            							newLeafNode(lv_operations2_6_0, grammarAccess.getRightsManagementAccess().getOperations2IDTerminalRuleCall_0_4_1_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getRightsManagementRule());
                            							}
                            							addWithLastConsumed(
                            								current,
                            								"operations2",
                            								lv_operations2_6_0,
                            								"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                            						

                            }


                            }

                            // InternalCQLParser.g:4727:5: (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )*
                            loop66:
                            do {
                                int alt66=2;
                                int LA66_0 = input.LA(1);

                                if ( (LA66_0==Comma) ) {
                                    alt66=1;
                                }


                                switch (alt66) {
                            	case 1 :
                            	    // InternalCQLParser.g:4728:6: otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) )
                            	    {
                            	    otherlv_7=(Token)match(input,Comma,FOLLOW_5); 

                            	    						newLeafNode(otherlv_7, grammarAccess.getRightsManagementAccess().getCommaKeyword_0_4_2_0());
                            	    					
                            	    // InternalCQLParser.g:4732:6: ( (lv_operations2_8_0= RULE_ID ) )
                            	    // InternalCQLParser.g:4733:7: (lv_operations2_8_0= RULE_ID )
                            	    {
                            	    // InternalCQLParser.g:4733:7: (lv_operations2_8_0= RULE_ID )
                            	    // InternalCQLParser.g:4734:8: lv_operations2_8_0= RULE_ID
                            	    {
                            	    lv_operations2_8_0=(Token)match(input,RULE_ID,FOLLOW_72); 

                            	    								newLeafNode(lv_operations2_8_0, grammarAccess.getRightsManagementAccess().getOperations2IDTerminalRuleCall_0_4_2_1_0());
                            	    							

                            	    								if (current==null) {
                            	    									current = createModelElement(grammarAccess.getRightsManagementRule());
                            	    								}
                            	    								addWithLastConsumed(
                            	    									current,
                            	    									"operations2",
                            	    									lv_operations2_8_0,
                            	    									"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                            	    							

                            	    }


                            	    }


                            	    }
                            	    break;

                            	default :
                            	    break loop66;
                                }
                            } while (true);


                            }
                            break;

                    }

                    otherlv_9=(Token)match(input,TO,FOLLOW_5); 

                    				newLeafNode(otherlv_9, grammarAccess.getRightsManagementAccess().getTOKeyword_0_5());
                    			
                    // InternalCQLParser.g:4756:4: ( (lv_user_10_0= RULE_ID ) )
                    // InternalCQLParser.g:4757:5: (lv_user_10_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4757:5: (lv_user_10_0= RULE_ID )
                    // InternalCQLParser.g:4758:6: lv_user_10_0= RULE_ID
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:4776:3: ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) )
                    {
                    // InternalCQLParser.g:4776:3: ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) )
                    // InternalCQLParser.g:4777:4: () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:4777:4: ()
                    // InternalCQLParser.g:4778:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getRightsManagementAccess().getRightsManagementAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4784:4: ( (lv_name_12_0= REVOKE ) )
                    // InternalCQLParser.g:4785:5: (lv_name_12_0= REVOKE )
                    {
                    // InternalCQLParser.g:4785:5: (lv_name_12_0= REVOKE )
                    // InternalCQLParser.g:4786:6: lv_name_12_0= REVOKE
                    {
                    lv_name_12_0=(Token)match(input,REVOKE,FOLLOW_5); 

                    						newLeafNode(lv_name_12_0, grammarAccess.getRightsManagementAccess().getNameREVOKEKeyword_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_12_0, "REVOKE");
                    					

                    }


                    }

                    // InternalCQLParser.g:4798:4: ( (lv_operations_13_0= RULE_ID ) )
                    // InternalCQLParser.g:4799:5: (lv_operations_13_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4799:5: (lv_operations_13_0= RULE_ID )
                    // InternalCQLParser.g:4800:6: lv_operations_13_0= RULE_ID
                    {
                    lv_operations_13_0=(Token)match(input,RULE_ID,FOLLOW_73); 

                    						newLeafNode(lv_operations_13_0, grammarAccess.getRightsManagementAccess().getOperationsIDTerminalRuleCall_1_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsManagementRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"operations",
                    							lv_operations_13_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    					

                    }


                    }

                    // InternalCQLParser.g:4816:4: (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )*
                    loop68:
                    do {
                        int alt68=2;
                        int LA68_0 = input.LA(1);

                        if ( (LA68_0==Comma) ) {
                            alt68=1;
                        }


                        switch (alt68) {
                    	case 1 :
                    	    // InternalCQLParser.g:4817:5: otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) )
                    	    {
                    	    otherlv_14=(Token)match(input,Comma,FOLLOW_5); 

                    	    					newLeafNode(otherlv_14, grammarAccess.getRightsManagementAccess().getCommaKeyword_1_3_0());
                    	    				
                    	    // InternalCQLParser.g:4821:5: ( (lv_operations_15_0= RULE_ID ) )
                    	    // InternalCQLParser.g:4822:6: (lv_operations_15_0= RULE_ID )
                    	    {
                    	    // InternalCQLParser.g:4822:6: (lv_operations_15_0= RULE_ID )
                    	    // InternalCQLParser.g:4823:7: lv_operations_15_0= RULE_ID
                    	    {
                    	    lv_operations_15_0=(Token)match(input,RULE_ID,FOLLOW_73); 

                    	    							newLeafNode(lv_operations_15_0, grammarAccess.getRightsManagementAccess().getOperationsIDTerminalRuleCall_1_3_1_0());
                    	    						

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getRightsManagementRule());
                    	    							}
                    	    							addWithLastConsumed(
                    	    								current,
                    	    								"operations",
                    	    								lv_operations_15_0,
                    	    								"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop68;
                        }
                    } while (true);

                    // InternalCQLParser.g:4840:4: (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )?
                    int alt70=2;
                    int LA70_0 = input.LA(1);

                    if ( (LA70_0==ON) ) {
                        alt70=1;
                    }
                    switch (alt70) {
                        case 1 :
                            // InternalCQLParser.g:4841:5: otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )*
                            {
                            otherlv_16=(Token)match(input,ON,FOLLOW_5); 

                            					newLeafNode(otherlv_16, grammarAccess.getRightsManagementAccess().getONKeyword_1_4_0());
                            				
                            // InternalCQLParser.g:4845:5: ( (lv_operations2_17_0= RULE_ID ) )
                            // InternalCQLParser.g:4846:6: (lv_operations2_17_0= RULE_ID )
                            {
                            // InternalCQLParser.g:4846:6: (lv_operations2_17_0= RULE_ID )
                            // InternalCQLParser.g:4847:7: lv_operations2_17_0= RULE_ID
                            {
                            lv_operations2_17_0=(Token)match(input,RULE_ID,FOLLOW_12); 

                            							newLeafNode(lv_operations2_17_0, grammarAccess.getRightsManagementAccess().getOperations2IDTerminalRuleCall_1_4_1_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getRightsManagementRule());
                            							}
                            							addWithLastConsumed(
                            								current,
                            								"operations2",
                            								lv_operations2_17_0,
                            								"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                            						

                            }


                            }

                            // InternalCQLParser.g:4863:5: (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )*
                            loop69:
                            do {
                                int alt69=2;
                                int LA69_0 = input.LA(1);

                                if ( (LA69_0==Comma) ) {
                                    alt69=1;
                                }


                                switch (alt69) {
                            	case 1 :
                            	    // InternalCQLParser.g:4864:6: otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) )
                            	    {
                            	    otherlv_18=(Token)match(input,Comma,FOLLOW_5); 

                            	    						newLeafNode(otherlv_18, grammarAccess.getRightsManagementAccess().getCommaKeyword_1_4_2_0());
                            	    					
                            	    // InternalCQLParser.g:4868:6: ( (lv_operations2_19_0= RULE_ID ) )
                            	    // InternalCQLParser.g:4869:7: (lv_operations2_19_0= RULE_ID )
                            	    {
                            	    // InternalCQLParser.g:4869:7: (lv_operations2_19_0= RULE_ID )
                            	    // InternalCQLParser.g:4870:8: lv_operations2_19_0= RULE_ID
                            	    {
                            	    lv_operations2_19_0=(Token)match(input,RULE_ID,FOLLOW_12); 

                            	    								newLeafNode(lv_operations2_19_0, grammarAccess.getRightsManagementAccess().getOperations2IDTerminalRuleCall_1_4_2_1_0());
                            	    							

                            	    								if (current==null) {
                            	    									current = createModelElement(grammarAccess.getRightsManagementRule());
                            	    								}
                            	    								addWithLastConsumed(
                            	    									current,
                            	    									"operations2",
                            	    									lv_operations2_19_0,
                            	    									"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                            	    							

                            	    }


                            	    }


                            	    }
                            	    break;

                            	default :
                            	    break loop69;
                                }
                            } while (true);


                            }
                            break;

                    }

                    otherlv_20=(Token)match(input,FROM,FOLLOW_5); 

                    				newLeafNode(otherlv_20, grammarAccess.getRightsManagementAccess().getFROMKeyword_1_5());
                    			
                    // InternalCQLParser.g:4892:4: ( (lv_user_21_0= RULE_ID ) )
                    // InternalCQLParser.g:4893:5: (lv_user_21_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4893:5: (lv_user_21_0= RULE_ID )
                    // InternalCQLParser.g:4894:6: lv_user_21_0= RULE_ID
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    					

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
    // InternalCQLParser.g:4915:1: entryRuleRoleManagement returns [EObject current=null] : iv_ruleRoleManagement= ruleRoleManagement EOF ;
    public final EObject entryRuleRoleManagement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRoleManagement = null;


        try {
            // InternalCQLParser.g:4915:55: (iv_ruleRoleManagement= ruleRoleManagement EOF )
            // InternalCQLParser.g:4916:2: iv_ruleRoleManagement= ruleRoleManagement EOF
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
    // InternalCQLParser.g:4922:1: ruleRoleManagement returns [EObject current=null] : ( ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) ) | ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) ) ) ;
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
            // InternalCQLParser.g:4928:2: ( ( ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) ) | ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) ) ) )
            // InternalCQLParser.g:4929:2: ( ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) ) | ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) ) )
            {
            // InternalCQLParser.g:4929:2: ( ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) ) | ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) ) )
            int alt74=2;
            int LA74_0 = input.LA(1);

            if ( (LA74_0==GRANT) ) {
                alt74=1;
            }
            else if ( (LA74_0==REVOKE) ) {
                alt74=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 74, 0, input);

                throw nvae;
            }
            switch (alt74) {
                case 1 :
                    // InternalCQLParser.g:4930:3: ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) )
                    {
                    // InternalCQLParser.g:4930:3: ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) )
                    // InternalCQLParser.g:4931:4: () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:4931:4: ()
                    // InternalCQLParser.g:4932:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getRoleManagementAccess().getRoleManagementAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4938:4: ( (lv_name_1_0= GRANT ) )
                    // InternalCQLParser.g:4939:5: (lv_name_1_0= GRANT )
                    {
                    // InternalCQLParser.g:4939:5: (lv_name_1_0= GRANT )
                    // InternalCQLParser.g:4940:6: lv_name_1_0= GRANT
                    {
                    lv_name_1_0=(Token)match(input,GRANT,FOLLOW_74); 

                    						newLeafNode(lv_name_1_0, grammarAccess.getRoleManagementAccess().getNameGRANTKeyword_0_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRoleManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_0, "GRANT");
                    					

                    }


                    }

                    otherlv_2=(Token)match(input,ROLE,FOLLOW_5); 

                    				newLeafNode(otherlv_2, grammarAccess.getRoleManagementAccess().getROLEKeyword_0_2());
                    			
                    // InternalCQLParser.g:4956:4: ( (lv_operations_3_0= RULE_ID ) )
                    // InternalCQLParser.g:4957:5: (lv_operations_3_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4957:5: (lv_operations_3_0= RULE_ID )
                    // InternalCQLParser.g:4958:6: lv_operations_3_0= RULE_ID
                    {
                    lv_operations_3_0=(Token)match(input,RULE_ID,FOLLOW_72); 

                    						newLeafNode(lv_operations_3_0, grammarAccess.getRoleManagementAccess().getOperationsIDTerminalRuleCall_0_3_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRoleManagementRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"operations",
                    							lv_operations_3_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    					

                    }


                    }

                    // InternalCQLParser.g:4974:4: (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )*
                    loop72:
                    do {
                        int alt72=2;
                        int LA72_0 = input.LA(1);

                        if ( (LA72_0==Comma) ) {
                            alt72=1;
                        }


                        switch (alt72) {
                    	case 1 :
                    	    // InternalCQLParser.g:4975:5: otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) )
                    	    {
                    	    otherlv_4=(Token)match(input,Comma,FOLLOW_5); 

                    	    					newLeafNode(otherlv_4, grammarAccess.getRoleManagementAccess().getCommaKeyword_0_4_0());
                    	    				
                    	    // InternalCQLParser.g:4979:5: ( (lv_operations_5_0= RULE_ID ) )
                    	    // InternalCQLParser.g:4980:6: (lv_operations_5_0= RULE_ID )
                    	    {
                    	    // InternalCQLParser.g:4980:6: (lv_operations_5_0= RULE_ID )
                    	    // InternalCQLParser.g:4981:7: lv_operations_5_0= RULE_ID
                    	    {
                    	    lv_operations_5_0=(Token)match(input,RULE_ID,FOLLOW_72); 

                    	    							newLeafNode(lv_operations_5_0, grammarAccess.getRoleManagementAccess().getOperationsIDTerminalRuleCall_0_4_1_0());
                    	    						

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getRoleManagementRule());
                    	    							}
                    	    							addWithLastConsumed(
                    	    								current,
                    	    								"operations",
                    	    								lv_operations_5_0,
                    	    								"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop72;
                        }
                    } while (true);

                    otherlv_6=(Token)match(input,TO,FOLLOW_5); 

                    				newLeafNode(otherlv_6, grammarAccess.getRoleManagementAccess().getTOKeyword_0_5());
                    			
                    // InternalCQLParser.g:5002:4: ( (lv_user_7_0= RULE_ID ) )
                    // InternalCQLParser.g:5003:5: (lv_user_7_0= RULE_ID )
                    {
                    // InternalCQLParser.g:5003:5: (lv_user_7_0= RULE_ID )
                    // InternalCQLParser.g:5004:6: lv_user_7_0= RULE_ID
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:5022:3: ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) )
                    {
                    // InternalCQLParser.g:5022:3: ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) )
                    // InternalCQLParser.g:5023:4: () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:5023:4: ()
                    // InternalCQLParser.g:5024:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getRoleManagementAccess().getRoleManagementAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:5030:4: ( (lv_name_9_0= REVOKE ) )
                    // InternalCQLParser.g:5031:5: (lv_name_9_0= REVOKE )
                    {
                    // InternalCQLParser.g:5031:5: (lv_name_9_0= REVOKE )
                    // InternalCQLParser.g:5032:6: lv_name_9_0= REVOKE
                    {
                    lv_name_9_0=(Token)match(input,REVOKE,FOLLOW_74); 

                    						newLeafNode(lv_name_9_0, grammarAccess.getRoleManagementAccess().getNameREVOKEKeyword_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRoleManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_9_0, "REVOKE");
                    					

                    }


                    }

                    otherlv_10=(Token)match(input,ROLE,FOLLOW_5); 

                    				newLeafNode(otherlv_10, grammarAccess.getRoleManagementAccess().getROLEKeyword_1_2());
                    			
                    // InternalCQLParser.g:5048:4: ( (lv_operations_11_0= RULE_ID ) )
                    // InternalCQLParser.g:5049:5: (lv_operations_11_0= RULE_ID )
                    {
                    // InternalCQLParser.g:5049:5: (lv_operations_11_0= RULE_ID )
                    // InternalCQLParser.g:5050:6: lv_operations_11_0= RULE_ID
                    {
                    lv_operations_11_0=(Token)match(input,RULE_ID,FOLLOW_12); 

                    						newLeafNode(lv_operations_11_0, grammarAccess.getRoleManagementAccess().getOperationsIDTerminalRuleCall_1_3_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRoleManagementRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"operations",
                    							lv_operations_11_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    					

                    }


                    }

                    // InternalCQLParser.g:5066:4: (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )*
                    loop73:
                    do {
                        int alt73=2;
                        int LA73_0 = input.LA(1);

                        if ( (LA73_0==Comma) ) {
                            alt73=1;
                        }


                        switch (alt73) {
                    	case 1 :
                    	    // InternalCQLParser.g:5067:5: otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) )
                    	    {
                    	    otherlv_12=(Token)match(input,Comma,FOLLOW_5); 

                    	    					newLeafNode(otherlv_12, grammarAccess.getRoleManagementAccess().getCommaKeyword_1_4_0());
                    	    				
                    	    // InternalCQLParser.g:5071:5: ( (lv_operations_13_0= RULE_ID ) )
                    	    // InternalCQLParser.g:5072:6: (lv_operations_13_0= RULE_ID )
                    	    {
                    	    // InternalCQLParser.g:5072:6: (lv_operations_13_0= RULE_ID )
                    	    // InternalCQLParser.g:5073:7: lv_operations_13_0= RULE_ID
                    	    {
                    	    lv_operations_13_0=(Token)match(input,RULE_ID,FOLLOW_12); 

                    	    							newLeafNode(lv_operations_13_0, grammarAccess.getRoleManagementAccess().getOperationsIDTerminalRuleCall_1_4_1_0());
                    	    						

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getRoleManagementRule());
                    	    							}
                    	    							addWithLastConsumed(
                    	    								current,
                    	    								"operations",
                    	    								lv_operations_13_0,
                    	    								"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop73;
                        }
                    } while (true);

                    otherlv_14=(Token)match(input,FROM,FOLLOW_5); 

                    				newLeafNode(otherlv_14, grammarAccess.getRoleManagementAccess().getFROMKeyword_1_5());
                    			
                    // InternalCQLParser.g:5094:4: ( (lv_user_15_0= RULE_ID ) )
                    // InternalCQLParser.g:5095:5: (lv_user_15_0= RULE_ID )
                    {
                    // InternalCQLParser.g:5095:5: (lv_user_15_0= RULE_ID )
                    // InternalCQLParser.g:5096:6: lv_user_15_0= RULE_ID
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ID");
                    					

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
    // InternalCQLParser.g:5117:1: entryRuleWindowOperator returns [EObject current=null] : iv_ruleWindowOperator= ruleWindowOperator EOF ;
    public final EObject entryRuleWindowOperator() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindowOperator = null;


        try {
            // InternalCQLParser.g:5117:55: (iv_ruleWindowOperator= ruleWindowOperator EOF )
            // InternalCQLParser.g:5118:2: iv_ruleWindowOperator= ruleWindowOperator EOF
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
    // InternalCQLParser.g:5124:1: ruleWindowOperator returns [EObject current=null] : (otherlv_0= LeftSquareBracket (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow ) otherlv_4= RightSquareBracket ) ;
    public final EObject ruleWindowOperator() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_4=null;
        EObject this_UnboundedWindow_1 = null;

        EObject this_TimebasedWindow_2 = null;

        EObject this_TuplebasedWindow_3 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5130:2: ( (otherlv_0= LeftSquareBracket (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow ) otherlv_4= RightSquareBracket ) )
            // InternalCQLParser.g:5131:2: (otherlv_0= LeftSquareBracket (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow ) otherlv_4= RightSquareBracket )
            {
            // InternalCQLParser.g:5131:2: (otherlv_0= LeftSquareBracket (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow ) otherlv_4= RightSquareBracket )
            // InternalCQLParser.g:5132:3: otherlv_0= LeftSquareBracket (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow ) otherlv_4= RightSquareBracket
            {
            otherlv_0=(Token)match(input,LeftSquareBracket,FOLLOW_75); 

            			newLeafNode(otherlv_0, grammarAccess.getWindowOperatorAccess().getLeftSquareBracketKeyword_0());
            		
            // InternalCQLParser.g:5136:3: (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow )
            int alt75=3;
            int LA75_0 = input.LA(1);

            if ( (LA75_0==UNBOUNDED) ) {
                alt75=1;
            }
            else if ( (LA75_0==SIZE) ) {
                int LA75_2 = input.LA(2);

                if ( (LA75_2==RULE_INT) ) {
                    int LA75_3 = input.LA(3);

                    if ( (LA75_3==ADVANCE||LA75_3==TUPLE) ) {
                        alt75=3;
                    }
                    else if ( (LA75_3==MILLISECONDS||LA75_3==MILLISECOND||LA75_3==MINUTES||LA75_3==SECONDS||LA75_3==MINUTE||LA75_3==SECOND||LA75_3==HOURS||LA75_3==WEEKS||LA75_3==HOUR||LA75_3==WEEK) ) {
                        alt75=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 75, 3, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 75, 2, input);

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
                    // InternalCQLParser.g:5137:4: this_UnboundedWindow_1= ruleUnboundedWindow
                    {

                    				newCompositeNode(grammarAccess.getWindowOperatorAccess().getUnboundedWindowParserRuleCall_1_0());
                    			
                    pushFollow(FOLLOW_76);
                    this_UnboundedWindow_1=ruleUnboundedWindow();

                    state._fsp--;


                    				current = this_UnboundedWindow_1;
                    				afterParserOrEnumRuleCall();
                    			

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:5146:4: this_TimebasedWindow_2= ruleTimebasedWindow
                    {

                    				newCompositeNode(grammarAccess.getWindowOperatorAccess().getTimebasedWindowParserRuleCall_1_1());
                    			
                    pushFollow(FOLLOW_76);
                    this_TimebasedWindow_2=ruleTimebasedWindow();

                    state._fsp--;


                    				current = this_TimebasedWindow_2;
                    				afterParserOrEnumRuleCall();
                    			

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:5155:4: this_TuplebasedWindow_3= ruleTuplebasedWindow
                    {

                    				newCompositeNode(grammarAccess.getWindowOperatorAccess().getTuplebasedWindowParserRuleCall_1_2());
                    			
                    pushFollow(FOLLOW_76);
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
    // InternalCQLParser.g:5172:1: entryRuleUnboundedWindow returns [EObject current=null] : iv_ruleUnboundedWindow= ruleUnboundedWindow EOF ;
    public final EObject entryRuleUnboundedWindow() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleUnboundedWindow = null;


        try {
            // InternalCQLParser.g:5172:56: (iv_ruleUnboundedWindow= ruleUnboundedWindow EOF )
            // InternalCQLParser.g:5173:2: iv_ruleUnboundedWindow= ruleUnboundedWindow EOF
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
    // InternalCQLParser.g:5179:1: ruleUnboundedWindow returns [EObject current=null] : ( () otherlv_1= UNBOUNDED ) ;
    public final EObject ruleUnboundedWindow() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;


        	enterRule();

        try {
            // InternalCQLParser.g:5185:2: ( ( () otherlv_1= UNBOUNDED ) )
            // InternalCQLParser.g:5186:2: ( () otherlv_1= UNBOUNDED )
            {
            // InternalCQLParser.g:5186:2: ( () otherlv_1= UNBOUNDED )
            // InternalCQLParser.g:5187:3: () otherlv_1= UNBOUNDED
            {
            // InternalCQLParser.g:5187:3: ()
            // InternalCQLParser.g:5188:4: 
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
    // InternalCQLParser.g:5202:1: entryRuleTimebasedWindow returns [EObject current=null] : iv_ruleTimebasedWindow= ruleTimebasedWindow EOF ;
    public final EObject entryRuleTimebasedWindow() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTimebasedWindow = null;


        try {
            // InternalCQLParser.g:5202:56: (iv_ruleTimebasedWindow= ruleTimebasedWindow EOF )
            // InternalCQLParser.g:5203:2: iv_ruleTimebasedWindow= ruleTimebasedWindow EOF
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
    // InternalCQLParser.g:5209:1: ruleTimebasedWindow returns [EObject current=null] : ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) ( (lv_unit_3_0= ruleTime ) ) (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= ruleTime ) ) )? otherlv_7= TIME ) ;
    public final EObject ruleTimebasedWindow() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_size_2_0=null;
        Token otherlv_4=null;
        Token lv_advance_size_5_0=null;
        Token otherlv_7=null;
        Enumerator lv_unit_3_0 = null;

        Enumerator lv_advance_unit_6_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5215:2: ( ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) ( (lv_unit_3_0= ruleTime ) ) (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= ruleTime ) ) )? otherlv_7= TIME ) )
            // InternalCQLParser.g:5216:2: ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) ( (lv_unit_3_0= ruleTime ) ) (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= ruleTime ) ) )? otherlv_7= TIME )
            {
            // InternalCQLParser.g:5216:2: ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) ( (lv_unit_3_0= ruleTime ) ) (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= ruleTime ) ) )? otherlv_7= TIME )
            // InternalCQLParser.g:5217:3: () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) ( (lv_unit_3_0= ruleTime ) ) (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= ruleTime ) ) )? otherlv_7= TIME
            {
            // InternalCQLParser.g:5217:3: ()
            // InternalCQLParser.g:5218:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getTimebasedWindowAccess().getTimebasedWindowAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,SIZE,FOLLOW_46); 

            			newLeafNode(otherlv_1, grammarAccess.getTimebasedWindowAccess().getSIZEKeyword_1());
            		
            // InternalCQLParser.g:5228:3: ( (lv_size_2_0= RULE_INT ) )
            // InternalCQLParser.g:5229:4: (lv_size_2_0= RULE_INT )
            {
            // InternalCQLParser.g:5229:4: (lv_size_2_0= RULE_INT )
            // InternalCQLParser.g:5230:5: lv_size_2_0= RULE_INT
            {
            lv_size_2_0=(Token)match(input,RULE_INT,FOLLOW_51); 

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

            // InternalCQLParser.g:5246:3: ( (lv_unit_3_0= ruleTime ) )
            // InternalCQLParser.g:5247:4: (lv_unit_3_0= ruleTime )
            {
            // InternalCQLParser.g:5247:4: (lv_unit_3_0= ruleTime )
            // InternalCQLParser.g:5248:5: lv_unit_3_0= ruleTime
            {

            					newCompositeNode(grammarAccess.getTimebasedWindowAccess().getUnitTimeEnumRuleCall_3_0());
            				
            pushFollow(FOLLOW_77);
            lv_unit_3_0=ruleTime();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getTimebasedWindowRule());
            					}
            					set(
            						current,
            						"unit",
            						lv_unit_3_0,
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.Time");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:5265:3: (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= ruleTime ) ) )?
            int alt76=2;
            int LA76_0 = input.LA(1);

            if ( (LA76_0==ADVANCE) ) {
                alt76=1;
            }
            switch (alt76) {
                case 1 :
                    // InternalCQLParser.g:5266:4: otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= ruleTime ) )
                    {
                    otherlv_4=(Token)match(input,ADVANCE,FOLLOW_46); 

                    				newLeafNode(otherlv_4, grammarAccess.getTimebasedWindowAccess().getADVANCEKeyword_4_0());
                    			
                    // InternalCQLParser.g:5270:4: ( (lv_advance_size_5_0= RULE_INT ) )
                    // InternalCQLParser.g:5271:5: (lv_advance_size_5_0= RULE_INT )
                    {
                    // InternalCQLParser.g:5271:5: (lv_advance_size_5_0= RULE_INT )
                    // InternalCQLParser.g:5272:6: lv_advance_size_5_0= RULE_INT
                    {
                    lv_advance_size_5_0=(Token)match(input,RULE_INT,FOLLOW_51); 

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

                    // InternalCQLParser.g:5288:4: ( (lv_advance_unit_6_0= ruleTime ) )
                    // InternalCQLParser.g:5289:5: (lv_advance_unit_6_0= ruleTime )
                    {
                    // InternalCQLParser.g:5289:5: (lv_advance_unit_6_0= ruleTime )
                    // InternalCQLParser.g:5290:6: lv_advance_unit_6_0= ruleTime
                    {

                    						newCompositeNode(grammarAccess.getTimebasedWindowAccess().getAdvance_unitTimeEnumRuleCall_4_2_0());
                    					
                    pushFollow(FOLLOW_78);
                    lv_advance_unit_6_0=ruleTime();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getTimebasedWindowRule());
                    						}
                    						set(
                    							current,
                    							"advance_unit",
                    							lv_advance_unit_6_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.Time");
                    						afterParserOrEnumRuleCall();
                    					

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
    // InternalCQLParser.g:5316:1: entryRuleTuplebasedWindow returns [EObject current=null] : iv_ruleTuplebasedWindow= ruleTuplebasedWindow EOF ;
    public final EObject entryRuleTuplebasedWindow() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTuplebasedWindow = null;


        try {
            // InternalCQLParser.g:5316:57: (iv_ruleTuplebasedWindow= ruleTuplebasedWindow EOF )
            // InternalCQLParser.g:5317:2: iv_ruleTuplebasedWindow= ruleTuplebasedWindow EOF
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
    // InternalCQLParser.g:5323:1: ruleTuplebasedWindow returns [EObject current=null] : ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )? otherlv_5= TUPLE (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )? ) ;
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
            // InternalCQLParser.g:5329:2: ( ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )? otherlv_5= TUPLE (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )? ) )
            // InternalCQLParser.g:5330:2: ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )? otherlv_5= TUPLE (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )? )
            {
            // InternalCQLParser.g:5330:2: ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )? otherlv_5= TUPLE (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )? )
            // InternalCQLParser.g:5331:3: () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )? otherlv_5= TUPLE (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )?
            {
            // InternalCQLParser.g:5331:3: ()
            // InternalCQLParser.g:5332:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getTuplebasedWindowAccess().getTuplebasedWindowAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,SIZE,FOLLOW_46); 

            			newLeafNode(otherlv_1, grammarAccess.getTuplebasedWindowAccess().getSIZEKeyword_1());
            		
            // InternalCQLParser.g:5342:3: ( (lv_size_2_0= RULE_INT ) )
            // InternalCQLParser.g:5343:4: (lv_size_2_0= RULE_INT )
            {
            // InternalCQLParser.g:5343:4: (lv_size_2_0= RULE_INT )
            // InternalCQLParser.g:5344:5: lv_size_2_0= RULE_INT
            {
            lv_size_2_0=(Token)match(input,RULE_INT,FOLLOW_79); 

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

            // InternalCQLParser.g:5360:3: (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )?
            int alt77=2;
            int LA77_0 = input.LA(1);

            if ( (LA77_0==ADVANCE) ) {
                alt77=1;
            }
            switch (alt77) {
                case 1 :
                    // InternalCQLParser.g:5361:4: otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) )
                    {
                    otherlv_3=(Token)match(input,ADVANCE,FOLLOW_46); 

                    				newLeafNode(otherlv_3, grammarAccess.getTuplebasedWindowAccess().getADVANCEKeyword_3_0());
                    			
                    // InternalCQLParser.g:5365:4: ( (lv_advance_size_4_0= RULE_INT ) )
                    // InternalCQLParser.g:5366:5: (lv_advance_size_4_0= RULE_INT )
                    {
                    // InternalCQLParser.g:5366:5: (lv_advance_size_4_0= RULE_INT )
                    // InternalCQLParser.g:5367:6: lv_advance_size_4_0= RULE_INT
                    {
                    lv_advance_size_4_0=(Token)match(input,RULE_INT,FOLLOW_80); 

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

            otherlv_5=(Token)match(input,TUPLE,FOLLOW_62); 

            			newLeafNode(otherlv_5, grammarAccess.getTuplebasedWindowAccess().getTUPLEKeyword_4());
            		
            // InternalCQLParser.g:5388:3: (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )?
            int alt78=2;
            int LA78_0 = input.LA(1);

            if ( (LA78_0==PARTITION) ) {
                alt78=1;
            }
            switch (alt78) {
                case 1 :
                    // InternalCQLParser.g:5389:4: otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) )
                    {
                    otherlv_6=(Token)match(input,PARTITION,FOLLOW_18); 

                    				newLeafNode(otherlv_6, grammarAccess.getTuplebasedWindowAccess().getPARTITIONKeyword_5_0());
                    			
                    otherlv_7=(Token)match(input,BY,FOLLOW_19); 

                    				newLeafNode(otherlv_7, grammarAccess.getTuplebasedWindowAccess().getBYKeyword_5_1());
                    			
                    // InternalCQLParser.g:5397:4: ( (lv_partition_attribute_8_0= ruleAttribute ) )
                    // InternalCQLParser.g:5398:5: (lv_partition_attribute_8_0= ruleAttribute )
                    {
                    // InternalCQLParser.g:5398:5: (lv_partition_attribute_8_0= ruleAttribute )
                    // InternalCQLParser.g:5399:6: lv_partition_attribute_8_0= ruleAttribute
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.Attribute");
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
    // InternalCQLParser.g:5421:1: entryRuleExpressionsModel returns [EObject current=null] : iv_ruleExpressionsModel= ruleExpressionsModel EOF ;
    public final EObject entryRuleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionsModel = null;


        try {
            // InternalCQLParser.g:5421:57: (iv_ruleExpressionsModel= ruleExpressionsModel EOF )
            // InternalCQLParser.g:5422:2: iv_ruleExpressionsModel= ruleExpressionsModel EOF
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
    // InternalCQLParser.g:5428:1: ruleExpressionsModel returns [EObject current=null] : ( () ( (lv_elements_1_0= ruleExpression ) ) ) ;
    public final EObject ruleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5434:2: ( ( () ( (lv_elements_1_0= ruleExpression ) ) ) )
            // InternalCQLParser.g:5435:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            {
            // InternalCQLParser.g:5435:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            // InternalCQLParser.g:5436:3: () ( (lv_elements_1_0= ruleExpression ) )
            {
            // InternalCQLParser.g:5436:3: ()
            // InternalCQLParser.g:5437:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:5443:3: ( (lv_elements_1_0= ruleExpression ) )
            // InternalCQLParser.g:5444:4: (lv_elements_1_0= ruleExpression )
            {
            // InternalCQLParser.g:5444:4: (lv_elements_1_0= ruleExpression )
            // InternalCQLParser.g:5445:5: lv_elements_1_0= ruleExpression
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
            						"de.uniol.inf.is.odysseus.parser.cql2.CQL.Expression");
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
    // InternalCQLParser.g:5466:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCQLParser.g:5466:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCQLParser.g:5467:2: iv_ruleExpression= ruleExpression EOF
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
    // InternalCQLParser.g:5473:1: ruleExpression returns [EObject current=null] : this_OrPredicate_0= ruleOrPredicate ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_OrPredicate_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5479:2: (this_OrPredicate_0= ruleOrPredicate )
            // InternalCQLParser.g:5480:2: this_OrPredicate_0= ruleOrPredicate
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
    // InternalCQLParser.g:5491:1: entryRuleOrPredicate returns [EObject current=null] : iv_ruleOrPredicate= ruleOrPredicate EOF ;
    public final EObject entryRuleOrPredicate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOrPredicate = null;


        try {
            // InternalCQLParser.g:5491:52: (iv_ruleOrPredicate= ruleOrPredicate EOF )
            // InternalCQLParser.g:5492:2: iv_ruleOrPredicate= ruleOrPredicate EOF
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
    // InternalCQLParser.g:5498:1: ruleOrPredicate returns [EObject current=null] : (this_AndPredicate_0= ruleAndPredicate ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )* ) ;
    public final EObject ruleOrPredicate() throws RecognitionException {
        EObject current = null;

        EObject this_AndPredicate_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5504:2: ( (this_AndPredicate_0= ruleAndPredicate ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )* ) )
            // InternalCQLParser.g:5505:2: (this_AndPredicate_0= ruleAndPredicate ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )* )
            {
            // InternalCQLParser.g:5505:2: (this_AndPredicate_0= ruleAndPredicate ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )* )
            // InternalCQLParser.g:5506:3: this_AndPredicate_0= ruleAndPredicate ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrPredicateAccess().getAndPredicateParserRuleCall_0());
            		
            pushFollow(FOLLOW_81);
            this_AndPredicate_0=ruleAndPredicate();

            state._fsp--;


            			current = this_AndPredicate_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:5514:3: ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )*
            loop79:
            do {
                int alt79=2;
                int LA79_0 = input.LA(1);

                if ( (LA79_0==OR) ) {
                    alt79=1;
                }


                switch (alt79) {
            	case 1 :
            	    // InternalCQLParser.g:5515:4: () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) )
            	    {
            	    // InternalCQLParser.g:5515:4: ()
            	    // InternalCQLParser.g:5516:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrPredicateAccess().getOrPredicateLeftAction_1_0(),
            	    						current);
            	    				

            	    }


            	    				newCompositeNode(grammarAccess.getOrPredicateAccess().getOrOperatorParserRuleCall_1_1());
            	    			
            	    pushFollow(FOLLOW_16);
            	    ruleOrOperator();

            	    state._fsp--;


            	    				afterParserOrEnumRuleCall();
            	    			
            	    // InternalCQLParser.g:5529:4: ( (lv_right_3_0= ruleAndPredicate ) )
            	    // InternalCQLParser.g:5530:5: (lv_right_3_0= ruleAndPredicate )
            	    {
            	    // InternalCQLParser.g:5530:5: (lv_right_3_0= ruleAndPredicate )
            	    // InternalCQLParser.g:5531:6: lv_right_3_0= ruleAndPredicate
            	    {

            	    						newCompositeNode(grammarAccess.getOrPredicateAccess().getRightAndPredicateParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_81);
            	    lv_right_3_0=ruleAndPredicate();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getOrPredicateRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.AndPredicate");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop79;
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
    // InternalCQLParser.g:5553:1: entryRuleAndPredicate returns [EObject current=null] : iv_ruleAndPredicate= ruleAndPredicate EOF ;
    public final EObject entryRuleAndPredicate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAndPredicate = null;


        try {
            // InternalCQLParser.g:5553:53: (iv_ruleAndPredicate= ruleAndPredicate EOF )
            // InternalCQLParser.g:5554:2: iv_ruleAndPredicate= ruleAndPredicate EOF
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
    // InternalCQLParser.g:5560:1: ruleAndPredicate returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAndPredicate() throws RecognitionException {
        EObject current = null;

        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5566:2: ( (this_Equalitiy_0= ruleEqualitiy ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCQLParser.g:5567:2: (this_Equalitiy_0= ruleEqualitiy ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCQLParser.g:5567:2: (this_Equalitiy_0= ruleEqualitiy ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCQLParser.g:5568:3: this_Equalitiy_0= ruleEqualitiy ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndPredicateAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_52);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:5576:3: ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop80:
            do {
                int alt80=2;
                int LA80_0 = input.LA(1);

                if ( (LA80_0==AND) ) {
                    alt80=1;
                }


                switch (alt80) {
            	case 1 :
            	    // InternalCQLParser.g:5577:4: () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCQLParser.g:5577:4: ()
            	    // InternalCQLParser.g:5578:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndPredicateAccess().getAndPredicateLeftAction_1_0(),
            	    						current);
            	    				

            	    }


            	    				newCompositeNode(grammarAccess.getAndPredicateAccess().getAndOperatorParserRuleCall_1_1());
            	    			
            	    pushFollow(FOLLOW_16);
            	    ruleAndOperator();

            	    state._fsp--;


            	    				afterParserOrEnumRuleCall();
            	    			
            	    // InternalCQLParser.g:5591:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCQLParser.g:5592:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCQLParser.g:5592:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCQLParser.g:5593:6: lv_right_3_0= ruleEqualitiy
            	    {

            	    						newCompositeNode(grammarAccess.getAndPredicateAccess().getRightEqualitiyParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_52);
            	    lv_right_3_0=ruleEqualitiy();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getAndPredicateRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.Equalitiy");
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
    // $ANTLR end "ruleAndPredicate"


    // $ANTLR start "entryRuleEqualitiy"
    // InternalCQLParser.g:5615:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCQLParser.g:5615:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCQLParser.g:5616:2: iv_ruleEqualitiy= ruleEqualitiy EOF
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
    // InternalCQLParser.g:5622:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject this_Comparison_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5628:2: ( (this_Comparison_0= ruleComparison ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCQLParser.g:5629:2: (this_Comparison_0= ruleComparison ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCQLParser.g:5629:2: (this_Comparison_0= ruleComparison ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCQLParser.g:5630:3: this_Comparison_0= ruleComparison ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_82);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:5638:3: ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop81:
            do {
                int alt81=2;
                int LA81_0 = input.LA(1);

                if ( (LA81_0==ExclamationMarkEqualsSign||LA81_0==EqualsSign) ) {
                    alt81=1;
                }


                switch (alt81) {
            	case 1 :
            	    // InternalCQLParser.g:5639:4: () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCQLParser.g:5639:4: ()
            	    // InternalCQLParser.g:5640:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:5646:4: ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) )
            	    // InternalCQLParser.g:5647:5: (lv_op_2_0= ruleEQUALITIY_OPERATOR )
            	    {
            	    // InternalCQLParser.g:5647:5: (lv_op_2_0= ruleEQUALITIY_OPERATOR )
            	    // InternalCQLParser.g:5648:6: lv_op_2_0= ruleEQUALITIY_OPERATOR
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getOpEQUALITIY_OPERATORParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_16);
            	    lv_op_2_0=ruleEQUALITIY_OPERATOR();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getEqualitiyRule());
            	    						}
            	    						set(
            	    							current,
            	    							"op",
            	    							lv_op_2_0,
            	    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.EQUALITIY_OPERATOR");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }

            	    // InternalCQLParser.g:5665:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCQLParser.g:5666:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCQLParser.g:5666:5: (lv_right_3_0= ruleComparison )
            	    // InternalCQLParser.g:5667:6: lv_right_3_0= ruleComparison
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getRightComparisonParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_82);
            	    lv_right_3_0=ruleComparison();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getEqualitiyRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.Comparison");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop81;
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
    // InternalCQLParser.g:5689:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCQLParser.g:5689:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCQLParser.g:5690:2: iv_ruleComparison= ruleComparison EOF
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
    // InternalCQLParser.g:5696:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
    public final EObject ruleComparison() throws RecognitionException {
        EObject current = null;

        EObject this_PlusOrMinus_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5702:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCQLParser.g:5703:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCQLParser.g:5703:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCQLParser.g:5704:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_83);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:5712:3: ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop82:
            do {
                int alt82=2;
                int LA82_0 = input.LA(1);

                if ( ((LA82_0>=LessThanSignEqualsSign && LA82_0<=GreaterThanSignEqualsSign)||LA82_0==LessThanSign||LA82_0==GreaterThanSign) ) {
                    alt82=1;
                }


                switch (alt82) {
            	case 1 :
            	    // InternalCQLParser.g:5713:4: () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCQLParser.g:5713:4: ()
            	    // InternalCQLParser.g:5714:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:5720:4: ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) )
            	    // InternalCQLParser.g:5721:5: (lv_op_2_0= ruleCOMPARE_OPERATOR )
            	    {
            	    // InternalCQLParser.g:5721:5: (lv_op_2_0= ruleCOMPARE_OPERATOR )
            	    // InternalCQLParser.g:5722:6: lv_op_2_0= ruleCOMPARE_OPERATOR
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getOpCOMPARE_OPERATORParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_16);
            	    lv_op_2_0=ruleCOMPARE_OPERATOR();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getComparisonRule());
            	    						}
            	    						set(
            	    							current,
            	    							"op",
            	    							lv_op_2_0,
            	    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.COMPARE_OPERATOR");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }

            	    // InternalCQLParser.g:5739:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCQLParser.g:5740:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCQLParser.g:5740:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCQLParser.g:5741:6: lv_right_3_0= rulePlusOrMinus
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getRightPlusOrMinusParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_83);
            	    lv_right_3_0=rulePlusOrMinus();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getComparisonRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.PlusOrMinus");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop82;
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
    // InternalCQLParser.g:5763:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCQLParser.g:5763:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCQLParser.g:5764:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
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
    // InternalCQLParser.g:5770:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5776:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCQLParser.g:5777:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCQLParser.g:5777:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCQLParser.g:5778:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_84);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:5786:3: ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop84:
            do {
                int alt84=2;
                int LA84_0 = input.LA(1);

                if ( (LA84_0==PlusSign||LA84_0==HyphenMinus) ) {
                    alt84=1;
                }


                switch (alt84) {
            	case 1 :
            	    // InternalCQLParser.g:5787:4: ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCQLParser.g:5787:4: ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) )
            	    int alt83=2;
            	    int LA83_0 = input.LA(1);

            	    if ( (LA83_0==PlusSign) ) {
            	        alt83=1;
            	    }
            	    else if ( (LA83_0==HyphenMinus) ) {
            	        alt83=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 83, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt83) {
            	        case 1 :
            	            // InternalCQLParser.g:5788:5: ( () otherlv_2= PlusSign )
            	            {
            	            // InternalCQLParser.g:5788:5: ( () otherlv_2= PlusSign )
            	            // InternalCQLParser.g:5789:6: () otherlv_2= PlusSign
            	            {
            	            // InternalCQLParser.g:5789:6: ()
            	            // InternalCQLParser.g:5790:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_2=(Token)match(input,PlusSign,FOLLOW_16); 

            	            						newLeafNode(otherlv_2, grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1());
            	            					

            	            }


            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:5802:5: ( () otherlv_4= HyphenMinus )
            	            {
            	            // InternalCQLParser.g:5802:5: ( () otherlv_4= HyphenMinus )
            	            // InternalCQLParser.g:5803:6: () otherlv_4= HyphenMinus
            	            {
            	            // InternalCQLParser.g:5803:6: ()
            	            // InternalCQLParser.g:5804:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_4=(Token)match(input,HyphenMinus,FOLLOW_16); 

            	            						newLeafNode(otherlv_4, grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1());
            	            					

            	            }


            	            }
            	            break;

            	    }

            	    // InternalCQLParser.g:5816:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCQLParser.g:5817:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCQLParser.g:5817:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCQLParser.g:5818:6: lv_right_5_0= ruleMulOrDiv
            	    {

            	    						newCompositeNode(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_84);
            	    lv_right_5_0=ruleMulOrDiv();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getPlusOrMinusRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_5_0,
            	    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.MulOrDiv");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop84;
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
    // InternalCQLParser.g:5840:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCQLParser.g:5840:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCQLParser.g:5841:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
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
    // InternalCQLParser.g:5847:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5853:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCQLParser.g:5854:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCQLParser.g:5854:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCQLParser.g:5855:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_85);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:5863:3: ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop86:
            do {
                int alt86=2;
                int LA86_0 = input.LA(1);

                if ( (LA86_0==Asterisk||LA86_0==Solidus) ) {
                    alt86=1;
                }


                switch (alt86) {
            	case 1 :
            	    // InternalCQLParser.g:5864:4: () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCQLParser.g:5864:4: ()
            	    // InternalCQLParser.g:5865:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:5871:4: ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) )
            	    // InternalCQLParser.g:5872:5: ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) )
            	    {
            	    // InternalCQLParser.g:5872:5: ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) )
            	    // InternalCQLParser.g:5873:6: (lv_op_2_1= Solidus | lv_op_2_2= Asterisk )
            	    {
            	    // InternalCQLParser.g:5873:6: (lv_op_2_1= Solidus | lv_op_2_2= Asterisk )
            	    int alt85=2;
            	    int LA85_0 = input.LA(1);

            	    if ( (LA85_0==Solidus) ) {
            	        alt85=1;
            	    }
            	    else if ( (LA85_0==Asterisk) ) {
            	        alt85=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 85, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt85) {
            	        case 1 :
            	            // InternalCQLParser.g:5874:7: lv_op_2_1= Solidus
            	            {
            	            lv_op_2_1=(Token)match(input,Solidus,FOLLOW_16); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getMulOrDivAccess().getOpSolidusKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getMulOrDivRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:5885:7: lv_op_2_2= Asterisk
            	            {
            	            lv_op_2_2=(Token)match(input,Asterisk,FOLLOW_16); 

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

            	    // InternalCQLParser.g:5898:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCQLParser.g:5899:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCQLParser.g:5899:5: (lv_right_3_0= rulePrimary )
            	    // InternalCQLParser.g:5900:6: lv_right_3_0= rulePrimary
            	    {

            	    						newCompositeNode(grammarAccess.getMulOrDivAccess().getRightPrimaryParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_85);
            	    lv_right_3_0=rulePrimary();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getMulOrDivRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.Primary");
            	    						afterParserOrEnumRuleCall();
            	    					

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
    // InternalCQLParser.g:5922:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCQLParser.g:5922:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCQLParser.g:5923:2: iv_rulePrimary= rulePrimary EOF
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
    // InternalCQLParser.g:5929:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
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
            // InternalCQLParser.g:5935:2: ( ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCQLParser.g:5936:2: ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCQLParser.g:5936:2: ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt87=3;
            switch ( input.LA(1) ) {
            case LeftParenthesis:
                {
                alt87=1;
                }
                break;
            case NOT:
                {
                alt87=2;
                }
                break;
            case EXISTS:
            case FALSE:
            case TRUE:
            case DollarSign:
            case RULE_INT:
            case RULE_ID:
            case RULE_FLOAT:
            case RULE_STRING:
                {
                alt87=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 87, 0, input);

                throw nvae;
            }

            switch (alt87) {
                case 1 :
                    // InternalCQLParser.g:5937:3: ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis )
                    {
                    // InternalCQLParser.g:5937:3: ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis )
                    // InternalCQLParser.g:5938:4: () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis
                    {
                    // InternalCQLParser.g:5938:4: ()
                    // InternalCQLParser.g:5939:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,LeftParenthesis,FOLLOW_16); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCQLParser.g:5949:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCQLParser.g:5950:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCQLParser.g:5950:5: (lv_inner_2_0= ruleExpression )
                    // InternalCQLParser.g:5951:6: lv_inner_2_0= ruleExpression
                    {

                    						newCompositeNode(grammarAccess.getPrimaryAccess().getInnerExpressionParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_24);
                    lv_inner_2_0=ruleExpression();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getPrimaryRule());
                    						}
                    						set(
                    							current,
                    							"inner",
                    							lv_inner_2_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.Expression");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    otherlv_3=(Token)match(input,RightParenthesis,FOLLOW_2); 

                    				newLeafNode(otherlv_3, grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_0_3());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:5974:3: ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCQLParser.g:5974:3: ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCQLParser.g:5975:4: () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCQLParser.g:5975:4: ()
                    // InternalCQLParser.g:5976:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,NOT,FOLLOW_16); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCQLParser.g:5986:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCQLParser.g:5987:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCQLParser.g:5987:5: (lv_expression_6_0= rulePrimary )
                    // InternalCQLParser.g:5988:6: lv_expression_6_0= rulePrimary
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.Primary");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:6007:3: this_Atomic_7= ruleAtomic
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
    // InternalCQLParser.g:6019:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCQLParser.g:6019:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCQLParser.g:6020:2: iv_ruleAtomic= ruleAtomic EOF
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
    // InternalCQLParser.g:6026:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) ) | ( () ( (lv_value_11_0= ruleComplexPredicate ) ) ) ) ;
    public final EObject ruleAtomic() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        AntlrDatatypeRuleToken lv_value_7_0 = null;

        EObject lv_value_9_0 = null;

        EObject lv_value_11_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:6032:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) ) | ( () ( (lv_value_11_0= ruleComplexPredicate ) ) ) ) )
            // InternalCQLParser.g:6033:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) ) | ( () ( (lv_value_11_0= ruleComplexPredicate ) ) ) )
            {
            // InternalCQLParser.g:6033:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) ) | ( () ( (lv_value_11_0= ruleComplexPredicate ) ) ) )
            int alt88=6;
            alt88 = dfa88.predict(input);
            switch (alt88) {
                case 1 :
                    // InternalCQLParser.g:6034:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQLParser.g:6034:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQLParser.g:6035:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQLParser.g:6035:4: ()
                    // InternalCQLParser.g:6036:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6042:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQLParser.g:6043:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQLParser.g:6043:5: (lv_value_1_0= RULE_INT )
                    // InternalCQLParser.g:6044:6: lv_value_1_0= RULE_INT
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
                    // InternalCQLParser.g:6062:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:6062:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQLParser.g:6063:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQLParser.g:6063:4: ()
                    // InternalCQLParser.g:6064:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6070:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQLParser.g:6071:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQLParser.g:6071:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQLParser.g:6072:6: lv_value_3_0= RULE_FLOAT
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.FLOAT");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:6090:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQLParser.g:6090:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQLParser.g:6091:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQLParser.g:6091:4: ()
                    // InternalCQLParser.g:6092:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6098:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQLParser.g:6099:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:6099:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQLParser.g:6100:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQLParser.g:6118:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    {
                    // InternalCQLParser.g:6118:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    // InternalCQLParser.g:6119:4: () ( (lv_value_7_0= ruleBOOLEAN ) )
                    {
                    // InternalCQLParser.g:6119:4: ()
                    // InternalCQLParser.g:6120:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6126:4: ( (lv_value_7_0= ruleBOOLEAN ) )
                    // InternalCQLParser.g:6127:5: (lv_value_7_0= ruleBOOLEAN )
                    {
                    // InternalCQLParser.g:6127:5: (lv_value_7_0= ruleBOOLEAN )
                    // InternalCQLParser.g:6128:6: lv_value_7_0= ruleBOOLEAN
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.BOOLEAN");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;
                case 5 :
                    // InternalCQLParser.g:6147:3: ( () ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) )
                    {
                    // InternalCQLParser.g:6147:3: ( () ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) )
                    // InternalCQLParser.g:6148:4: () ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQLParser.g:6148:4: ()
                    // InternalCQLParser.g:6149:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeRefAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6155:4: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQLParser.g:6156:5: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQLParser.g:6156:5: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQLParser.g:6157:6: lv_value_9_0= ruleAttributeWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getAtomicAccess().getValueAttributeWithoutAliasDefinitionParserRuleCall_4_1_0());
                    					
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.AttributeWithoutAliasDefinition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;
                case 6 :
                    // InternalCQLParser.g:6176:3: ( () ( (lv_value_11_0= ruleComplexPredicate ) ) )
                    {
                    // InternalCQLParser.g:6176:3: ( () ( (lv_value_11_0= ruleComplexPredicate ) ) )
                    // InternalCQLParser.g:6177:4: () ( (lv_value_11_0= ruleComplexPredicate ) )
                    {
                    // InternalCQLParser.g:6177:4: ()
                    // InternalCQLParser.g:6178:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getComplexPredicateRefAction_5_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6184:4: ( (lv_value_11_0= ruleComplexPredicate ) )
                    // InternalCQLParser.g:6185:5: (lv_value_11_0= ruleComplexPredicate )
                    {
                    // InternalCQLParser.g:6185:5: (lv_value_11_0= ruleComplexPredicate )
                    // InternalCQLParser.g:6186:6: lv_value_11_0= ruleComplexPredicate
                    {

                    						newCompositeNode(grammarAccess.getAtomicAccess().getValueComplexPredicateParserRuleCall_5_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_value_11_0=ruleComplexPredicate();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getAtomicRule());
                    						}
                    						set(
                    							current,
                    							"value",
                    							lv_value_11_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.ComplexPredicate");
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


    // $ANTLR start "entryRuleAtomicWithoutAttributeRef"
    // InternalCQLParser.g:6208:1: entryRuleAtomicWithoutAttributeRef returns [EObject current=null] : iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF ;
    public final EObject entryRuleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomicWithoutAttributeRef = null;


        try {
            // InternalCQLParser.g:6208:66: (iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF )
            // InternalCQLParser.g:6209:2: iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF
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
    // InternalCQLParser.g:6215:1: ruleAtomicWithoutAttributeRef returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_MATRIX_FLOAT ) ) ) | ( () ( (lv_value_11_0= RULE_VECTOR_FLOAT ) ) ) ) ;
    public final EObject ruleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        Token lv_value_9_0=null;
        Token lv_value_11_0=null;
        AntlrDatatypeRuleToken lv_value_7_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:6221:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_MATRIX_FLOAT ) ) ) | ( () ( (lv_value_11_0= RULE_VECTOR_FLOAT ) ) ) ) )
            // InternalCQLParser.g:6222:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_MATRIX_FLOAT ) ) ) | ( () ( (lv_value_11_0= RULE_VECTOR_FLOAT ) ) ) )
            {
            // InternalCQLParser.g:6222:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_MATRIX_FLOAT ) ) ) | ( () ( (lv_value_11_0= RULE_VECTOR_FLOAT ) ) ) )
            int alt89=6;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt89=1;
                }
                break;
            case RULE_FLOAT:
                {
                alt89=2;
                }
                break;
            case RULE_STRING:
                {
                alt89=3;
                }
                break;
            case FALSE:
            case TRUE:
                {
                alt89=4;
                }
                break;
            case RULE_MATRIX_FLOAT:
                {
                alt89=5;
                }
                break;
            case RULE_VECTOR_FLOAT:
                {
                alt89=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 89, 0, input);

                throw nvae;
            }

            switch (alt89) {
                case 1 :
                    // InternalCQLParser.g:6223:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQLParser.g:6223:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQLParser.g:6224:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQLParser.g:6224:4: ()
                    // InternalCQLParser.g:6225:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6231:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQLParser.g:6232:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQLParser.g:6232:5: (lv_value_1_0= RULE_INT )
                    // InternalCQLParser.g:6233:6: lv_value_1_0= RULE_INT
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
                    // InternalCQLParser.g:6251:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:6251:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQLParser.g:6252:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQLParser.g:6252:4: ()
                    // InternalCQLParser.g:6253:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6259:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQLParser.g:6260:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQLParser.g:6260:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQLParser.g:6261:6: lv_value_3_0= RULE_FLOAT
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.FLOAT");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:6279:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQLParser.g:6279:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQLParser.g:6280:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQLParser.g:6280:4: ()
                    // InternalCQLParser.g:6281:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6287:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQLParser.g:6288:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:6288:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQLParser.g:6289:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQLParser.g:6307:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    {
                    // InternalCQLParser.g:6307:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    // InternalCQLParser.g:6308:4: () ( (lv_value_7_0= ruleBOOLEAN ) )
                    {
                    // InternalCQLParser.g:6308:4: ()
                    // InternalCQLParser.g:6309:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6315:4: ( (lv_value_7_0= ruleBOOLEAN ) )
                    // InternalCQLParser.g:6316:5: (lv_value_7_0= ruleBOOLEAN )
                    {
                    // InternalCQLParser.g:6316:5: (lv_value_7_0= ruleBOOLEAN )
                    // InternalCQLParser.g:6317:6: lv_value_7_0= ruleBOOLEAN
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
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.BOOLEAN");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;
                case 5 :
                    // InternalCQLParser.g:6336:3: ( () ( (lv_value_9_0= RULE_MATRIX_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:6336:3: ( () ( (lv_value_9_0= RULE_MATRIX_FLOAT ) ) )
                    // InternalCQLParser.g:6337:4: () ( (lv_value_9_0= RULE_MATRIX_FLOAT ) )
                    {
                    // InternalCQLParser.g:6337:4: ()
                    // InternalCQLParser.g:6338:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getMatrixAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6344:4: ( (lv_value_9_0= RULE_MATRIX_FLOAT ) )
                    // InternalCQLParser.g:6345:5: (lv_value_9_0= RULE_MATRIX_FLOAT )
                    {
                    // InternalCQLParser.g:6345:5: (lv_value_9_0= RULE_MATRIX_FLOAT )
                    // InternalCQLParser.g:6346:6: lv_value_9_0= RULE_MATRIX_FLOAT
                    {
                    lv_value_9_0=(Token)match(input,RULE_MATRIX_FLOAT,FOLLOW_2); 

                    						newLeafNode(lv_value_9_0, grammarAccess.getAtomicWithoutAttributeRefAccess().getValueMATRIX_FLOATTerminalRuleCall_4_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicWithoutAttributeRefRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"value",
                    							lv_value_9_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.MATRIX_FLOAT");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 6 :
                    // InternalCQLParser.g:6364:3: ( () ( (lv_value_11_0= RULE_VECTOR_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:6364:3: ( () ( (lv_value_11_0= RULE_VECTOR_FLOAT ) ) )
                    // InternalCQLParser.g:6365:4: () ( (lv_value_11_0= RULE_VECTOR_FLOAT ) )
                    {
                    // InternalCQLParser.g:6365:4: ()
                    // InternalCQLParser.g:6366:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getVectorAction_5_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6372:4: ( (lv_value_11_0= RULE_VECTOR_FLOAT ) )
                    // InternalCQLParser.g:6373:5: (lv_value_11_0= RULE_VECTOR_FLOAT )
                    {
                    // InternalCQLParser.g:6373:5: (lv_value_11_0= RULE_VECTOR_FLOAT )
                    // InternalCQLParser.g:6374:6: lv_value_11_0= RULE_VECTOR_FLOAT
                    {
                    lv_value_11_0=(Token)match(input,RULE_VECTOR_FLOAT,FOLLOW_2); 

                    						newLeafNode(lv_value_11_0, grammarAccess.getAtomicWithoutAttributeRefAccess().getValueVECTOR_FLOATTerminalRuleCall_5_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicWithoutAttributeRefRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"value",
                    							lv_value_11_0,
                    							"de.uniol.inf.is.odysseus.parser.cql2.CQL.VECTOR_FLOAT");
                    					

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


    // $ANTLR start "ruleTime"
    // InternalCQLParser.g:6395:1: ruleTime returns [Enumerator current=null] : ( (enumLiteral_0= MILLISECOND ) | (enumLiteral_1= SECOND ) | (enumLiteral_2= MINUTE ) | (enumLiteral_3= HOUR ) | (enumLiteral_4= WEEK ) | (enumLiteral_5= MILLISECONDS ) | (enumLiteral_6= SECONDS ) | (enumLiteral_7= MINUTES ) | (enumLiteral_8= HOURS ) | (enumLiteral_9= WEEKS ) ) ;
    public final Enumerator ruleTime() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;
        Token enumLiteral_4=null;
        Token enumLiteral_5=null;
        Token enumLiteral_6=null;
        Token enumLiteral_7=null;
        Token enumLiteral_8=null;
        Token enumLiteral_9=null;


        	enterRule();

        try {
            // InternalCQLParser.g:6401:2: ( ( (enumLiteral_0= MILLISECOND ) | (enumLiteral_1= SECOND ) | (enumLiteral_2= MINUTE ) | (enumLiteral_3= HOUR ) | (enumLiteral_4= WEEK ) | (enumLiteral_5= MILLISECONDS ) | (enumLiteral_6= SECONDS ) | (enumLiteral_7= MINUTES ) | (enumLiteral_8= HOURS ) | (enumLiteral_9= WEEKS ) ) )
            // InternalCQLParser.g:6402:2: ( (enumLiteral_0= MILLISECOND ) | (enumLiteral_1= SECOND ) | (enumLiteral_2= MINUTE ) | (enumLiteral_3= HOUR ) | (enumLiteral_4= WEEK ) | (enumLiteral_5= MILLISECONDS ) | (enumLiteral_6= SECONDS ) | (enumLiteral_7= MINUTES ) | (enumLiteral_8= HOURS ) | (enumLiteral_9= WEEKS ) )
            {
            // InternalCQLParser.g:6402:2: ( (enumLiteral_0= MILLISECOND ) | (enumLiteral_1= SECOND ) | (enumLiteral_2= MINUTE ) | (enumLiteral_3= HOUR ) | (enumLiteral_4= WEEK ) | (enumLiteral_5= MILLISECONDS ) | (enumLiteral_6= SECONDS ) | (enumLiteral_7= MINUTES ) | (enumLiteral_8= HOURS ) | (enumLiteral_9= WEEKS ) )
            int alt90=10;
            switch ( input.LA(1) ) {
            case MILLISECOND:
                {
                alt90=1;
                }
                break;
            case SECOND:
                {
                alt90=2;
                }
                break;
            case MINUTE:
                {
                alt90=3;
                }
                break;
            case HOUR:
                {
                alt90=4;
                }
                break;
            case WEEK:
                {
                alt90=5;
                }
                break;
            case MILLISECONDS:
                {
                alt90=6;
                }
                break;
            case SECONDS:
                {
                alt90=7;
                }
                break;
            case MINUTES:
                {
                alt90=8;
                }
                break;
            case HOURS:
                {
                alt90=9;
                }
                break;
            case WEEKS:
                {
                alt90=10;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 90, 0, input);

                throw nvae;
            }

            switch (alt90) {
                case 1 :
                    // InternalCQLParser.g:6403:3: (enumLiteral_0= MILLISECOND )
                    {
                    // InternalCQLParser.g:6403:3: (enumLiteral_0= MILLISECOND )
                    // InternalCQLParser.g:6404:4: enumLiteral_0= MILLISECOND
                    {
                    enumLiteral_0=(Token)match(input,MILLISECOND,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getMILLISECONDEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getTimeAccess().getMILLISECONDEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:6411:3: (enumLiteral_1= SECOND )
                    {
                    // InternalCQLParser.g:6411:3: (enumLiteral_1= SECOND )
                    // InternalCQLParser.g:6412:4: enumLiteral_1= SECOND
                    {
                    enumLiteral_1=(Token)match(input,SECOND,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getSECONDEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getTimeAccess().getSECONDEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:6419:3: (enumLiteral_2= MINUTE )
                    {
                    // InternalCQLParser.g:6419:3: (enumLiteral_2= MINUTE )
                    // InternalCQLParser.g:6420:4: enumLiteral_2= MINUTE
                    {
                    enumLiteral_2=(Token)match(input,MINUTE,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getMINUTEEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getTimeAccess().getMINUTEEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:6427:3: (enumLiteral_3= HOUR )
                    {
                    // InternalCQLParser.g:6427:3: (enumLiteral_3= HOUR )
                    // InternalCQLParser.g:6428:4: enumLiteral_3= HOUR
                    {
                    enumLiteral_3=(Token)match(input,HOUR,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getHOUREnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_3, grammarAccess.getTimeAccess().getHOUREnumLiteralDeclaration_3());
                    			

                    }


                    }
                    break;
                case 5 :
                    // InternalCQLParser.g:6435:3: (enumLiteral_4= WEEK )
                    {
                    // InternalCQLParser.g:6435:3: (enumLiteral_4= WEEK )
                    // InternalCQLParser.g:6436:4: enumLiteral_4= WEEK
                    {
                    enumLiteral_4=(Token)match(input,WEEK,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getWEEKEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_4, grammarAccess.getTimeAccess().getWEEKEnumLiteralDeclaration_4());
                    			

                    }


                    }
                    break;
                case 6 :
                    // InternalCQLParser.g:6443:3: (enumLiteral_5= MILLISECONDS )
                    {
                    // InternalCQLParser.g:6443:3: (enumLiteral_5= MILLISECONDS )
                    // InternalCQLParser.g:6444:4: enumLiteral_5= MILLISECONDS
                    {
                    enumLiteral_5=(Token)match(input,MILLISECONDS,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getMILLISECONDSEnumLiteralDeclaration_5().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_5, grammarAccess.getTimeAccess().getMILLISECONDSEnumLiteralDeclaration_5());
                    			

                    }


                    }
                    break;
                case 7 :
                    // InternalCQLParser.g:6451:3: (enumLiteral_6= SECONDS )
                    {
                    // InternalCQLParser.g:6451:3: (enumLiteral_6= SECONDS )
                    // InternalCQLParser.g:6452:4: enumLiteral_6= SECONDS
                    {
                    enumLiteral_6=(Token)match(input,SECONDS,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getSECONDSEnumLiteralDeclaration_6().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_6, grammarAccess.getTimeAccess().getSECONDSEnumLiteralDeclaration_6());
                    			

                    }


                    }
                    break;
                case 8 :
                    // InternalCQLParser.g:6459:3: (enumLiteral_7= MINUTES )
                    {
                    // InternalCQLParser.g:6459:3: (enumLiteral_7= MINUTES )
                    // InternalCQLParser.g:6460:4: enumLiteral_7= MINUTES
                    {
                    enumLiteral_7=(Token)match(input,MINUTES,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getMINUTESEnumLiteralDeclaration_7().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_7, grammarAccess.getTimeAccess().getMINUTESEnumLiteralDeclaration_7());
                    			

                    }


                    }
                    break;
                case 9 :
                    // InternalCQLParser.g:6467:3: (enumLiteral_8= HOURS )
                    {
                    // InternalCQLParser.g:6467:3: (enumLiteral_8= HOURS )
                    // InternalCQLParser.g:6468:4: enumLiteral_8= HOURS
                    {
                    enumLiteral_8=(Token)match(input,HOURS,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getHOURSEnumLiteralDeclaration_8().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_8, grammarAccess.getTimeAccess().getHOURSEnumLiteralDeclaration_8());
                    			

                    }


                    }
                    break;
                case 10 :
                    // InternalCQLParser.g:6475:3: (enumLiteral_9= WEEKS )
                    {
                    // InternalCQLParser.g:6475:3: (enumLiteral_9= WEEKS )
                    // InternalCQLParser.g:6476:4: enumLiteral_9= WEEKS
                    {
                    enumLiteral_9=(Token)match(input,WEEKS,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getWEEKSEnumLiteralDeclaration_9().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_9, grammarAccess.getTimeAccess().getWEEKSEnumLiteralDeclaration_9());
                    			

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
    // $ANTLR end "ruleTime"

    // Delegated rules


    protected DFA8 dfa8 = new DFA8(this);
    protected DFA27 dfa27 = new DFA27(this);
    protected DFA47 dfa47 = new DFA47(this);
    protected DFA88 dfa88 = new DFA88(this);
    static final String dfa_1s = "\21\uffff";
    static final String dfa_2s = "\1\34\2\17\1\uffff\2\70\3\uffff\1\11\3\uffff\1\147\1\67\2\uffff";
    static final String dfa_3s = "\1\62\1\77\1\76\1\uffff\2\147\3\uffff\1\11\3\uffff\1\147\1\111\2\uffff";
    static final String dfa_4s = "\3\uffff\1\2\2\uffff\1\1\1\11\1\7\1\uffff\1\10\1\3\1\4\2\uffff\1\6\1\5";
    static final String dfa_5s = "\21\uffff}>";
    static final String[] dfa_6s = {
            "\1\2\3\uffff\1\5\5\uffff\1\3\1\uffff\1\4\11\uffff\1\1",
            "\1\10\6\uffff\1\7\15\uffff\1\6\1\3\22\uffff\1\3\1\6\4\uffff\1\3\1\6",
            "\1\11\6\uffff\1\12\16\uffff\1\3\22\uffff\1\3\5\uffff\1\3",
            "",
            "\1\14\56\uffff\1\13",
            "\1\14\56\uffff\1\13",
            "",
            "",
            "",
            "\1\15",
            "",
            "",
            "",
            "\1\16",
            "\1\17\21\uffff\1\20",
            "",
            ""
    };

    static final short[] dfa_1 = DFA.unpackEncodedString(dfa_1s);
    static final char[] dfa_2 = DFA.unpackEncodedStringToUnsignedChars(dfa_2s);
    static final char[] dfa_3 = DFA.unpackEncodedStringToUnsignedChars(dfa_3s);
    static final short[] dfa_4 = DFA.unpackEncodedString(dfa_4s);
    static final short[] dfa_5 = DFA.unpackEncodedString(dfa_5s);
    static final short[][] dfa_6 = unpackEncodedStringArray(dfa_6s);

    class DFA8 extends DFA {

        public DFA8(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 8;
            this.eot = dfa_1;
            this.eof = dfa_1;
            this.min = dfa_2;
            this.max = dfa_3;
            this.accept = dfa_4;
            this.special = dfa_5;
            this.transition = dfa_6;
        }
        public String getDescription() {
            return "437:4: (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore )";
        }
    }
    static final String dfa_7s = "\14\uffff";
    static final String dfa_8s = "\1\35\1\107\1\142\1\uffff\1\124\2\uffff\1\147\2\107\1\143\1\107";
    static final String dfa_9s = "\1\147\1\136\1\142\1\uffff\1\147\2\uffff\1\147\2\136\1\143\1\136";
    static final String dfa_10s = "\3\uffff\1\2\1\uffff\1\3\1\1\5\uffff";
    static final String dfa_11s = "\14\uffff}>";
    static final String[] dfa_12s = {
            "\1\3\63\uffff\1\2\25\uffff\1\1",
            "\2\6\4\uffff\1\5\12\uffff\1\4\3\uffff\1\6\1\uffff\1\6",
            "\1\7",
            "",
            "\1\10\22\uffff\1\11",
            "",
            "",
            "\1\12",
            "\2\6\4\uffff\1\5\16\uffff\1\6\1\uffff\1\6",
            "\2\6\4\uffff\1\5\16\uffff\1\6\1\uffff\1\6",
            "\1\13",
            "\2\6\4\uffff\1\5\16\uffff\1\6\1\uffff\1\6"
    };

    static final short[] dfa_7 = DFA.unpackEncodedString(dfa_7s);
    static final char[] dfa_8 = DFA.unpackEncodedStringToUnsignedChars(dfa_8s);
    static final char[] dfa_9 = DFA.unpackEncodedStringToUnsignedChars(dfa_9s);
    static final short[] dfa_10 = DFA.unpackEncodedString(dfa_10s);
    static final short[] dfa_11 = DFA.unpackEncodedString(dfa_11s);
    static final short[][] dfa_12 = unpackEncodedStringArray(dfa_12s);

    class DFA27 extends DFA {

        public DFA27(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 27;
            this.eot = dfa_7;
            this.eof = dfa_7;
            this.min = dfa_8;
            this.max = dfa_9;
            this.accept = dfa_10;
            this.special = dfa_11;
            this.transition = dfa_12;
        }
        public String getDescription() {
            return "1427:3: ( ( (lv_quantification_0_0= ruleQuantificationPredicate ) ) | ( (lv_exists_1_0= ruleExistPredicate ) ) | ( (lv_in_2_0= ruleInPredicate ) ) )";
        }
    }
    static final String dfa_13s = "\17\uffff";
    static final String dfa_14s = "\1\147\1\65\1\147\2\uffff\1\147\1\123\1\147\1\17\1\147\4\uffff\1\123";
    static final String dfa_15s = "\1\147\1\122\1\147\2\uffff\1\147\1\126\1\147\1\64\1\147\4\uffff\1\126";
    static final String dfa_16s = "\3\uffff\1\6\1\5\5\uffff\1\3\1\4\1\1\1\2\1\uffff";
    static final String dfa_17s = "\17\uffff}>";
    static final String[] dfa_18s = {
            "\1\1",
            "\1\3\23\uffff\1\4\10\uffff\1\2",
            "\1\5",
            "",
            "",
            "\1\6",
            "\1\10\2\uffff\1\7",
            "\1\11",
            "\1\13\5\uffff\1\15\4\uffff\1\14\31\uffff\1\12",
            "\1\16",
            "",
            "",
            "",
            "",
            "\1\10\2\uffff\1\7"
    };

    static final short[] dfa_13 = DFA.unpackEncodedString(dfa_13s);
    static final char[] dfa_14 = DFA.unpackEncodedStringToUnsignedChars(dfa_14s);
    static final char[] dfa_15 = DFA.unpackEncodedStringToUnsignedChars(dfa_15s);
    static final short[] dfa_16 = DFA.unpackEncodedString(dfa_16s);
    static final short[] dfa_17 = DFA.unpackEncodedString(dfa_17s);
    static final short[][] dfa_18 = unpackEncodedStringArray(dfa_18s);

    class DFA47 extends DFA {

        public DFA47(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 47;
            this.eot = dfa_13;
            this.eof = dfa_13;
            this.min = dfa_14;
            this.max = dfa_15;
            this.accept = dfa_16;
            this.special = dfa_17;
            this.transition = dfa_18;
        }
        public String getDescription() {
            return "2992:5: (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView )";
        }
    }
    static final String dfa_19s = "\23\uffff";
    static final String dfa_20s = "\5\uffff\1\11\11\uffff\2\11\1\uffff\1\11";
    static final String dfa_21s = "\1\35\4\uffff\1\5\1\142\1\uffff\1\124\1\uffff\4\35\1\147\2\5\1\143\1\5";
    static final String dfa_22s = "\1\156\4\uffff\1\136\1\142\1\uffff\1\147\1\uffff\4\156\1\147\2\136\1\143\1\136";
    static final String dfa_23s = "\1\uffff\1\1\1\2\1\3\1\4\2\uffff\1\6\1\uffff\1\5\11\uffff";
    static final String dfa_24s = "\23\uffff}>";
    static final String[] dfa_25s = {
            "\1\7\11\uffff\1\4\25\uffff\1\4\23\uffff\1\6\24\uffff\1\1\1\5\1\2\5\uffff\1\3",
            "",
            "",
            "",
            "",
            "\1\11\4\uffff\1\11\20\uffff\2\11\1\uffff\1\11\1\uffff\1\11\1\uffff\1\11\1\uffff\1\11\1\uffff\1\11\1\uffff\2\11\5\uffff\1\11\2\uffff\1\11\20\uffff\1\11\2\uffff\1\11\1\13\1\12\4\uffff\1\7\1\uffff\1\11\3\uffff\3\11\1\uffff\1\11\1\10\1\11\1\uffff\1\11\1\14\1\11\1\15",
            "\1\16",
            "",
            "\1\20\22\uffff\1\17",
            "",
            "\1\11\11\uffff\1\11\23\uffff\1\7\1\uffff\1\11\4\uffff\1\7\1\uffff\1\7\1\11\13\uffff\2\11\23\uffff\3\11\5\uffff\1\11",
            "\1\11\11\uffff\1\11\23\uffff\1\7\1\uffff\1\11\4\uffff\1\7\1\uffff\1\7\1\11\13\uffff\2\11\23\uffff\3\11\5\uffff\1\11",
            "\1\11\11\uffff\1\11\23\uffff\1\7\1\uffff\1\11\4\uffff\1\7\1\uffff\1\7\1\11\13\uffff\2\11\23\uffff\3\11\5\uffff\1\11",
            "\1\11\11\uffff\1\11\23\uffff\1\7\1\uffff\1\11\4\uffff\1\7\1\uffff\1\7\1\11\13\uffff\2\11\23\uffff\3\11\5\uffff\1\11",
            "\1\21",
            "\1\11\4\uffff\1\11\20\uffff\2\11\1\uffff\1\11\1\uffff\1\11\1\uffff\1\11\1\uffff\1\11\1\uffff\1\11\1\uffff\2\11\5\uffff\1\11\2\uffff\1\11\20\uffff\1\11\2\uffff\1\11\1\13\1\12\4\uffff\1\7\1\uffff\1\11\3\uffff\3\11\1\uffff\1\11\1\uffff\1\11\1\uffff\1\11\1\14\1\11\1\15",
            "\1\11\4\uffff\1\11\20\uffff\2\11\1\uffff\1\11\1\uffff\1\11\1\uffff\1\11\1\uffff\1\11\1\uffff\1\11\1\uffff\2\11\5\uffff\1\11\2\uffff\1\11\20\uffff\1\11\2\uffff\1\11\1\13\1\12\4\uffff\1\7\1\uffff\1\11\3\uffff\3\11\1\uffff\1\11\1\uffff\1\11\1\uffff\1\11\1\14\1\11\1\15",
            "\1\22",
            "\1\11\4\uffff\1\11\20\uffff\2\11\1\uffff\1\11\1\uffff\1\11\1\uffff\1\11\1\uffff\1\11\1\uffff\1\11\1\uffff\2\11\5\uffff\1\11\2\uffff\1\11\20\uffff\1\11\2\uffff\1\11\1\13\1\12\4\uffff\1\7\1\uffff\1\11\3\uffff\3\11\1\uffff\1\11\1\uffff\1\11\1\uffff\1\11\1\14\1\11\1\15"
    };

    static final short[] dfa_19 = DFA.unpackEncodedString(dfa_19s);
    static final short[] dfa_20 = DFA.unpackEncodedString(dfa_20s);
    static final char[] dfa_21 = DFA.unpackEncodedStringToUnsignedChars(dfa_21s);
    static final char[] dfa_22 = DFA.unpackEncodedStringToUnsignedChars(dfa_22s);
    static final short[] dfa_23 = DFA.unpackEncodedString(dfa_23s);
    static final short[] dfa_24 = DFA.unpackEncodedString(dfa_24s);
    static final short[][] dfa_25 = unpackEncodedStringArray(dfa_25s);

    class DFA88 extends DFA {

        public DFA88(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 88;
            this.eot = dfa_19;
            this.eof = dfa_20;
            this.min = dfa_21;
            this.max = dfa_22;
            this.accept = dfa_23;
            this.special = dfa_24;
            this.transition = dfa_25;
        }
        public String getDescription() {
            return "6033:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) ) | ( () ( (lv_value_11_0= ruleComplexPredicate ) ) ) )";
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0004015518000002L,0x0000000008000000L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x2000008000010000L,0x000059C000120000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x2020008000010000L,0x000059C000520000L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0020000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000000000000L,0x0000008000040000L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0002020040000002L,0x0000008000440000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0002020040000002L,0x0000000000400000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x2000008020000000L,0x000041C000060020L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000020040000002L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000000000000L,0x0000008000020000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000040000002L,0x0000008000420000L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000040000002L,0x0000000000400000L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000800000000422L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000001418000000L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000000000002L,0x0000000080000200L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000200L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000000000000000L,0x0000000050000180L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0800000000000000L,0x0000000000000014L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000000000000002L,0x0000000202B00200L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000000000000L,0x0000000202B00000L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000000000000000L,0x0000600000000000L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000000000000000L,0x0000400000480000L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000000000000000L,0x0000000000480000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x8200001000000000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0041040282800140L,0x0000000000000001L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000008L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0004000000080000L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000402L});
    public static final BitSet FOLLOW_62 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_63 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_64 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_65 = new BitSet(new long[]{0x0000080800000000L});
    public static final BitSet FOLLOW_66 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001000L});
    public static final BitSet FOLLOW_67 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_68 = new BitSet(new long[]{0x0000001418000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_69 = new BitSet(new long[]{0x4100002000000000L});
    public static final BitSet FOLLOW_70 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_71 = new BitSet(new long[]{0x0000000000000000L,0x0000000000414000L});
    public static final BitSet FOLLOW_72 = new BitSet(new long[]{0x0000000000000000L,0x0000000000410000L});
    public static final BitSet FOLLOW_73 = new BitSet(new long[]{0x0020000000000000L,0x0000000000404000L});
    public static final BitSet FOLLOW_74 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_75 = new BitSet(new long[]{0x0400000000004000L});
    public static final BitSet FOLLOW_76 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000000L});
    public static final BitSet FOLLOW_77 = new BitSet(new long[]{0x1000000000100000L});
    public static final BitSet FOLLOW_78 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_79 = new BitSet(new long[]{0x0000400000100000L});
    public static final BitSet FOLLOW_80 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_81 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_82 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000040L});
    public static final BitSet FOLLOW_83 = new BitSet(new long[]{0x0000000000000002L,0x0000000050000180L});
    public static final BitSet FOLLOW_84 = new BitSet(new long[]{0x0000000000000002L,0x0000000000A00000L});
    public static final BitSet FOLLOW_85 = new BitSet(new long[]{0x0000000000000002L,0x0000000002100000L});

}