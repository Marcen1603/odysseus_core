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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "NO_LAZY_CONNECTION_CHECK", "INTERSECTION", "MILLISECONDS", "DATAHANDLER", "MILLISECOND", "NANOSECONDS", "CONNECTION", "DIFFERENCE", "IDENTIFIED", "NANOSECOND", "PARTITION", "TRANSPORT", "UNBOUNDED", "DATABASE", "DISTINCT", "PASSWORD", "PROTOCOL", "TRUNCATE", "ADVANCE", "CHANNEL", "CONTEXT", "MINUTES", "OPTIONS", "SECONDS", "WRAPPER", "ATTACH", "CREATE", "EXISTS", "HAVING", "MINUTE", "REVOKE", "SECOND", "SELECT", "SINGLE", "STREAM", "TENANT", "ALTER", "FALSE", "GRANT", "GROUP", "HOURS", "MULTI", "STORE", "TABLE", "TUPLE", "UNION", "WEEKS", "WHERE", "DROP", "EACH", "FILE", "FROM", "HOUR", "JDBC", "NULL", "ROLE", "SINK", "SIZE", "SOME", "TIME", "TRUE", "USER", "VIEW", "WEEK", "WITH", "ALL", "AND", "ANY", "NOT", "ExclamationMarkEqualsSign", "LeftParenthesisRightParenthesis", "LessThanSignEqualsSign", "GreaterThanSignEqualsSign", "AS", "AT", "BY", "IF", "IN", "ON", "OR", "TO", "DollarSign", "LeftParenthesis", "RightParenthesis", "Asterisk", "PlusSign", "Comma", "HyphenMinus", "FullStop", "Solidus", "Colon", "Semicolon", "LessThanSign", "EqualsSign", "GreaterThanSign", "LeftSquareBracket", "RightSquareBracket", "CircumflexAccent", "LeftCurlyBracket", "RightCurlyBracket", "RULE_LETTER", "RULE_SPECIAL_CHARS", "RULE_INT", "RULE_ID", "RULE_FLOAT", "RULE_BIT", "RULE_BYTE", "RULE_VECTOR_FLOAT", "RULE_MATRIX_FLOAT", "RULE_PATH", "RULE_STRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER"
    };
    public static final int CREATE=30;
    public static final int CONTEXT=24;
    public static final int LessThanSign=96;
    public static final int TABLE=47;
    public static final int RULE_BIT=109;
    public static final int LeftParenthesis=86;
    public static final int RULE_VECTOR_FLOAT=111;
    public static final int PARTITION=14;
    public static final int IF=80;
    public static final int MILLISECONDS=6;
    public static final int EACH=53;
    public static final int GreaterThanSign=98;
    public static final int RULE_ID=107;
    public static final int IN=81;
    public static final int DISTINCT=18;
    public static final int SIZE=61;
    public static final int RULE_SPECIAL_CHARS=105;
    public static final int PROTOCOL=20;
    public static final int OPTIONS=26;
    public static final int WHERE=51;
    public static final int GreaterThanSignEqualsSign=76;
    public static final int AS=77;
    public static final int AT=78;
    public static final int DATABASE=17;
    public static final int CHANNEL=23;
    public static final int WEEKS=50;
    public static final int PlusSign=89;
    public static final int RULE_INT=106;
    public static final int RULE_ML_COMMENT=115;
    public static final int LeftSquareBracket=99;
    public static final int ADVANCE=22;
    public static final int ALTER=40;
    public static final int RULE_BYTE=110;
    public static final int ROLE=59;
    public static final int GROUP=43;
    public static final int Comma=90;
    public static final int LeftParenthesisRightParenthesis=74;
    public static final int HyphenMinus=91;
    public static final int BY=79;
    public static final int LessThanSignEqualsSign=75;
    public static final int Solidus=93;
    public static final int RightCurlyBracket=103;
    public static final int DATAHANDLER=7;
    public static final int FILE=54;
    public static final int FullStop=92;
    public static final int REVOKE=34;
    public static final int SECONDS=27;
    public static final int NO_LAZY_CONNECTION_CHECK=4;
    public static final int SELECT=36;
    public static final int TUPLE=48;
    public static final int CONNECTION=10;
    public static final int Semicolon=95;
    public static final int RULE_LETTER=104;
    public static final int STORE=46;
    public static final int RULE_FLOAT=108;
    public static final int MILLISECOND=8;
    public static final int ExclamationMarkEqualsSign=73;
    public static final int TO=84;
    public static final int MULTI=45;
    public static final int UNION=49;
    public static final int TRUNCATE=21;
    public static final int ALL=69;
    public static final int SINGLE=37;
    public static final int FROM=55;
    public static final int VIEW=66;
    public static final int UNBOUNDED=16;
    public static final int WRAPPER=28;
    public static final int MINUTE=33;
    public static final int RightSquareBracket=100;
    public static final int NULL=58;
    public static final int RightParenthesis=87;
    public static final int TRUE=64;
    public static final int RULE_PATH=113;
    public static final int NOT=72;
    public static final int INTERSECTION=5;
    public static final int PASSWORD=19;
    public static final int SINK=60;
    public static final int AND=70;
    public static final int HAVING=32;
    public static final int HOUR=56;
    public static final int NANOSECONDS=9;
    public static final int RULE_STRING=114;
    public static final int ANY=71;
    public static final int DROP=52;
    public static final int RULE_SL_COMMENT=116;
    public static final int EqualsSign=97;
    public static final int TRANSPORT=15;
    public static final int SOME=62;
    public static final int DIFFERENCE=11;
    public static final int JDBC=57;
    public static final int Colon=94;
    public static final int WEEK=67;
    public static final int EOF=-1;
    public static final int Asterisk=88;
    public static final int NANOSECOND=13;
    public static final int ON=82;
    public static final int OR=83;
    public static final int EXISTS=31;
    public static final int RULE_WS=117;
    public static final int STREAM=38;
    public static final int LeftCurlyBracket=102;
    public static final int IDENTIFIED=12;
    public static final int TIME=63;
    public static final int RULE_ANY_OTHER=118;
    public static final int USER=65;
    public static final int TENANT=39;
    public static final int WITH=68;
    public static final int CircumflexAccent=101;
    public static final int GRANT=42;
    public static final int ATTACH=29;
    public static final int HOURS=44;
    public static final int DollarSign=85;
    public static final int SECOND=35;
    public static final int FALSE=41;
    public static final int RULE_MATRIX_FLOAT=112;
    public static final int MINUTES=25;

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
    // InternalCQLParser.g:157:1: ruleSystemVariable returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : ( (kw= DollarSign kw= LeftCurlyBracket this_ID_2= RULE_ID kw= RightCurlyBracket ) | (this_ID_4= RULE_ID kw= LeftParenthesisRightParenthesis ) ) ;
    public final AntlrDatatypeRuleToken ruleSystemVariable() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        Token this_ID_2=null;
        Token this_ID_4=null;


        	enterRule();

        try {
            // InternalCQLParser.g:163:2: ( ( (kw= DollarSign kw= LeftCurlyBracket this_ID_2= RULE_ID kw= RightCurlyBracket ) | (this_ID_4= RULE_ID kw= LeftParenthesisRightParenthesis ) ) )
            // InternalCQLParser.g:164:2: ( (kw= DollarSign kw= LeftCurlyBracket this_ID_2= RULE_ID kw= RightCurlyBracket ) | (this_ID_4= RULE_ID kw= LeftParenthesisRightParenthesis ) )
            {
            // InternalCQLParser.g:164:2: ( (kw= DollarSign kw= LeftCurlyBracket this_ID_2= RULE_ID kw= RightCurlyBracket ) | (this_ID_4= RULE_ID kw= LeftParenthesisRightParenthesis ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==DollarSign) ) {
                alt5=1;
            }
            else if ( (LA5_0==RULE_ID) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // InternalCQLParser.g:165:3: (kw= DollarSign kw= LeftCurlyBracket this_ID_2= RULE_ID kw= RightCurlyBracket )
                    {
                    // InternalCQLParser.g:165:3: (kw= DollarSign kw= LeftCurlyBracket this_ID_2= RULE_ID kw= RightCurlyBracket )
                    // InternalCQLParser.g:166:4: kw= DollarSign kw= LeftCurlyBracket this_ID_2= RULE_ID kw= RightCurlyBracket
                    {
                    kw=(Token)match(input,DollarSign,FOLLOW_4); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getSystemVariableAccess().getDollarSignKeyword_0_0());
                    			
                    kw=(Token)match(input,LeftCurlyBracket,FOLLOW_5); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getSystemVariableAccess().getLeftCurlyBracketKeyword_0_1());
                    			
                    this_ID_2=(Token)match(input,RULE_ID,FOLLOW_6); 

                    				current.merge(this_ID_2);
                    			

                    				newLeafNode(this_ID_2, grammarAccess.getSystemVariableAccess().getIDTerminalRuleCall_0_2());
                    			
                    kw=(Token)match(input,RightCurlyBracket,FOLLOW_2); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getSystemVariableAccess().getRightCurlyBracketKeyword_0_3());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:190:3: (this_ID_4= RULE_ID kw= LeftParenthesisRightParenthesis )
                    {
                    // InternalCQLParser.g:190:3: (this_ID_4= RULE_ID kw= LeftParenthesisRightParenthesis )
                    // InternalCQLParser.g:191:4: this_ID_4= RULE_ID kw= LeftParenthesisRightParenthesis
                    {
                    this_ID_4=(Token)match(input,RULE_ID,FOLLOW_7); 

                    				current.merge(this_ID_4);
                    			

                    				newLeafNode(this_ID_4, grammarAccess.getSystemVariableAccess().getIDTerminalRuleCall_1_0());
                    			
                    kw=(Token)match(input,LeftParenthesisRightParenthesis,FOLLOW_2); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getSystemVariableAccess().getLeftParenthesisRightParenthesisKeyword_1_1());
                    			

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
    // $ANTLR end "ruleSystemVariable"


    // $ANTLR start "entryRuleQualifiedAttributename"
    // InternalCQLParser.g:208:1: entryRuleQualifiedAttributename returns [String current=null] : iv_ruleQualifiedAttributename= ruleQualifiedAttributename EOF ;
    public final String entryRuleQualifiedAttributename() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedAttributename = null;


        try {
            // InternalCQLParser.g:208:62: (iv_ruleQualifiedAttributename= ruleQualifiedAttributename EOF )
            // InternalCQLParser.g:209:2: iv_ruleQualifiedAttributename= ruleQualifiedAttributename EOF
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
    // InternalCQLParser.g:215:1: ruleQualifiedAttributename returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) | (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk ) | this_SystemVariable_7= ruleSystemVariable ) ;
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
            // InternalCQLParser.g:221:2: ( (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) | (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk ) | this_SystemVariable_7= ruleSystemVariable ) )
            // InternalCQLParser.g:222:2: (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) | (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk ) | this_SystemVariable_7= ruleSystemVariable )
            {
            // InternalCQLParser.g:222:2: (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) | (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk ) | this_SystemVariable_7= ruleSystemVariable )
            int alt6=4;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==RULE_ID) ) {
                switch ( input.LA(2) ) {
                case EOF:
                case INTERSECTION:
                case DIFFERENCE:
                case ATTACH:
                case CREATE:
                case HAVING:
                case REVOKE:
                case SELECT:
                case STREAM:
                case ALTER:
                case FALSE:
                case GRANT:
                case GROUP:
                case UNION:
                case DROP:
                case FROM:
                case TRUE:
                case AND:
                case ExclamationMarkEqualsSign:
                case LessThanSignEqualsSign:
                case GreaterThanSignEqualsSign:
                case AS:
                case IN:
                case OR:
                case DollarSign:
                case RightParenthesis:
                case Asterisk:
                case PlusSign:
                case Comma:
                case HyphenMinus:
                case Solidus:
                case Semicolon:
                case LessThanSign:
                case EqualsSign:
                case GreaterThanSign:
                case RightSquareBracket:
                case RULE_INT:
                case RULE_ID:
                case RULE_FLOAT:
                case RULE_VECTOR_FLOAT:
                case RULE_MATRIX_FLOAT:
                case RULE_STRING:
                    {
                    alt6=1;
                    }
                    break;
                case FullStop:
                    {
                    int LA6_4 = input.LA(3);

                    if ( (LA6_4==RULE_ID) ) {
                        alt6=2;
                    }
                    else if ( (LA6_4==Asterisk) ) {
                        alt6=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 4, input);

                        throw nvae;
                    }
                    }
                    break;
                case LeftParenthesisRightParenthesis:
                    {
                    alt6=4;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA6_0==DollarSign) ) {
                alt6=4;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // InternalCQLParser.g:223:3: this_ID_0= RULE_ID
                    {
                    this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    			current.merge(this_ID_0);
                    		

                    			newLeafNode(this_ID_0, grammarAccess.getQualifiedAttributenameAccess().getIDTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:231:3: (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID )
                    {
                    // InternalCQLParser.g:231:3: (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID )
                    // InternalCQLParser.g:232:4: this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID
                    {

                    				newCompositeNode(grammarAccess.getQualifiedAttributenameAccess().getQualifiedSourcenameParserRuleCall_1_0());
                    			
                    pushFollow(FOLLOW_8);
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
                    // InternalCQLParser.g:256:3: (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk )
                    {
                    // InternalCQLParser.g:256:3: (this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk )
                    // InternalCQLParser.g:257:4: this_QualifiedSourcename_4= ruleQualifiedSourcename kw= FullStop kw= Asterisk
                    {

                    				newCompositeNode(grammarAccess.getQualifiedAttributenameAccess().getQualifiedSourcenameParserRuleCall_2_0());
                    			
                    pushFollow(FOLLOW_8);
                    this_QualifiedSourcename_4=ruleQualifiedSourcename();

                    state._fsp--;


                    				current.merge(this_QualifiedSourcename_4);
                    			

                    				afterParserOrEnumRuleCall();
                    			
                    kw=(Token)match(input,FullStop,FOLLOW_9); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getQualifiedAttributenameAccess().getFullStopKeyword_2_1());
                    			
                    kw=(Token)match(input,Asterisk,FOLLOW_2); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getQualifiedAttributenameAccess().getAsteriskKeyword_2_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:279:3: this_SystemVariable_7= ruleSystemVariable
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
    // InternalCQLParser.g:293:1: entryRuleQualifiedAttributenameWithoutSpecialChars returns [String current=null] : iv_ruleQualifiedAttributenameWithoutSpecialChars= ruleQualifiedAttributenameWithoutSpecialChars EOF ;
    public final String entryRuleQualifiedAttributenameWithoutSpecialChars() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedAttributenameWithoutSpecialChars = null;


        try {
            // InternalCQLParser.g:293:81: (iv_ruleQualifiedAttributenameWithoutSpecialChars= ruleQualifiedAttributenameWithoutSpecialChars EOF )
            // InternalCQLParser.g:294:2: iv_ruleQualifiedAttributenameWithoutSpecialChars= ruleQualifiedAttributenameWithoutSpecialChars EOF
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
    // InternalCQLParser.g:300:1: ruleQualifiedAttributenameWithoutSpecialChars returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) ) ;
    public final AntlrDatatypeRuleToken ruleQualifiedAttributenameWithoutSpecialChars() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_3=null;
        AntlrDatatypeRuleToken this_QualifiedSourcename_1 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:306:2: ( (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) ) )
            // InternalCQLParser.g:307:2: (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) )
            {
            // InternalCQLParser.g:307:2: (this_ID_0= RULE_ID | (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID ) )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==RULE_ID) ) {
                int LA7_1 = input.LA(2);

                if ( (LA7_1==EOF||LA7_1==FALSE||LA7_1==FROM||LA7_1==TRUE||LA7_1==AS||LA7_1==DollarSign||(LA7_1>=RightParenthesis && LA7_1<=HyphenMinus)||LA7_1==Solidus||LA7_1==CircumflexAccent||(LA7_1>=RULE_INT && LA7_1<=RULE_FLOAT)||(LA7_1>=RULE_VECTOR_FLOAT && LA7_1<=RULE_MATRIX_FLOAT)||LA7_1==RULE_STRING) ) {
                    alt7=1;
                }
                else if ( (LA7_1==FullStop) ) {
                    alt7=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // InternalCQLParser.g:308:3: this_ID_0= RULE_ID
                    {
                    this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    			current.merge(this_ID_0);
                    		

                    			newLeafNode(this_ID_0, grammarAccess.getQualifiedAttributenameWithoutSpecialCharsAccess().getIDTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:316:3: (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID )
                    {
                    // InternalCQLParser.g:316:3: (this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID )
                    // InternalCQLParser.g:317:4: this_QualifiedSourcename_1= ruleQualifiedSourcename kw= FullStop this_ID_3= RULE_ID
                    {

                    				newCompositeNode(grammarAccess.getQualifiedAttributenameWithoutSpecialCharsAccess().getQualifiedSourcenameParserRuleCall_1_0());
                    			
                    pushFollow(FOLLOW_8);
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
    // InternalCQLParser.g:344:1: entryRuleQualifiedSourcename returns [String current=null] : iv_ruleQualifiedSourcename= ruleQualifiedSourcename EOF ;
    public final String entryRuleQualifiedSourcename() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedSourcename = null;


        try {
            // InternalCQLParser.g:344:59: (iv_ruleQualifiedSourcename= ruleQualifiedSourcename EOF )
            // InternalCQLParser.g:345:2: iv_ruleQualifiedSourcename= ruleQualifiedSourcename EOF
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
    // InternalCQLParser.g:351:1: ruleQualifiedSourcename returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : this_ID_0= RULE_ID ;
    public final AntlrDatatypeRuleToken ruleQualifiedSourcename() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:357:2: (this_ID_0= RULE_ID )
            // InternalCQLParser.g:358:2: this_ID_0= RULE_ID
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
    // InternalCQLParser.g:368:1: entryRuleQuery returns [EObject current=null] : iv_ruleQuery= ruleQuery EOF ;
    public final EObject entryRuleQuery() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleQuery = null;


        try {
            // InternalCQLParser.g:368:46: (iv_ruleQuery= ruleQuery EOF )
            // InternalCQLParser.g:369:2: iv_ruleQuery= ruleQuery EOF
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
    // InternalCQLParser.g:375:1: ruleQuery returns [EObject current=null] : ( ( (lv_type_0_1= ruleCreate | lv_type_0_2= ruleStreamTo | lv_type_0_3= ruleComplexSelect ) ) ) ;
    public final EObject ruleQuery() throws RecognitionException {
        EObject current = null;

        EObject lv_type_0_1 = null;

        EObject lv_type_0_2 = null;

        EObject lv_type_0_3 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:381:2: ( ( ( (lv_type_0_1= ruleCreate | lv_type_0_2= ruleStreamTo | lv_type_0_3= ruleComplexSelect ) ) ) )
            // InternalCQLParser.g:382:2: ( ( (lv_type_0_1= ruleCreate | lv_type_0_2= ruleStreamTo | lv_type_0_3= ruleComplexSelect ) ) )
            {
            // InternalCQLParser.g:382:2: ( ( (lv_type_0_1= ruleCreate | lv_type_0_2= ruleStreamTo | lv_type_0_3= ruleComplexSelect ) ) )
            // InternalCQLParser.g:383:3: ( (lv_type_0_1= ruleCreate | lv_type_0_2= ruleStreamTo | lv_type_0_3= ruleComplexSelect ) )
            {
            // InternalCQLParser.g:383:3: ( (lv_type_0_1= ruleCreate | lv_type_0_2= ruleStreamTo | lv_type_0_3= ruleComplexSelect ) )
            // InternalCQLParser.g:384:4: (lv_type_0_1= ruleCreate | lv_type_0_2= ruleStreamTo | lv_type_0_3= ruleComplexSelect )
            {
            // InternalCQLParser.g:384:4: (lv_type_0_1= ruleCreate | lv_type_0_2= ruleStreamTo | lv_type_0_3= ruleComplexSelect )
            int alt8=3;
            switch ( input.LA(1) ) {
            case ATTACH:
            case CREATE:
                {
                alt8=1;
                }
                break;
            case STREAM:
                {
                alt8=2;
                }
                break;
            case SELECT:
                {
                alt8=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // InternalCQLParser.g:385:5: lv_type_0_1= ruleCreate
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
                    // InternalCQLParser.g:401:5: lv_type_0_2= ruleStreamTo
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
                    // InternalCQLParser.g:417:5: lv_type_0_3= ruleComplexSelect
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
    // InternalCQLParser.g:438:1: entryRuleCommand returns [EObject current=null] : iv_ruleCommand= ruleCommand EOF ;
    public final EObject entryRuleCommand() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCommand = null;


        try {
            // InternalCQLParser.g:438:48: (iv_ruleCommand= ruleCommand EOF )
            // InternalCQLParser.g:439:2: iv_ruleCommand= ruleCommand EOF
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
    // InternalCQLParser.g:445:1: ruleCommand returns [EObject current=null] : ( ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) ) ) ;
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
            // InternalCQLParser.g:451:2: ( ( ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) ) ) )
            // InternalCQLParser.g:452:2: ( ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) ) )
            {
            // InternalCQLParser.g:452:2: ( ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) ) )
            // InternalCQLParser.g:453:3: ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) )
            {
            // InternalCQLParser.g:453:3: ( (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore ) )
            // InternalCQLParser.g:454:4: (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore )
            {
            // InternalCQLParser.g:454:4: (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore )
            int alt9=9;
            alt9 = dfa9.predict(input);
            switch (alt9) {
                case 1 :
                    // InternalCQLParser.g:455:5: lv_type_0_1= ruleDropStream
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
                    // InternalCQLParser.g:471:5: lv_type_0_2= ruleUserManagement
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
                    // InternalCQLParser.g:487:5: lv_type_0_3= ruleRightsManagement
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
                    // InternalCQLParser.g:503:5: lv_type_0_4= ruleRoleManagement
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
                    // InternalCQLParser.g:519:5: lv_type_0_5= ruleCreateDataBaseGenericConnection
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
                    // InternalCQLParser.g:535:5: lv_type_0_6= ruleCreateDataBaseJDBCConnection
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
                    // InternalCQLParser.g:551:5: lv_type_0_7= ruleDropDatabaseConnection
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
                    // InternalCQLParser.g:567:5: lv_type_0_8= ruleCreateContextStore
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
                    // InternalCQLParser.g:583:5: lv_type_0_9= ruleDropContextStore
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
    // InternalCQLParser.g:604:1: entryRuleSimpleSelect returns [EObject current=null] : iv_ruleSimpleSelect= ruleSimpleSelect EOF ;
    public final EObject entryRuleSimpleSelect() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSimpleSelect = null;


        try {
            // InternalCQLParser.g:604:53: (iv_ruleSimpleSelect= ruleSimpleSelect EOF )
            // InternalCQLParser.g:605:2: iv_ruleSimpleSelect= ruleSimpleSelect EOF
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
    // InternalCQLParser.g:611:1: ruleSimpleSelect returns [EObject current=null] : ( () (otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )? ) ) ;
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
            // InternalCQLParser.g:617:2: ( ( () (otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )? ) ) )
            // InternalCQLParser.g:618:2: ( () (otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )? ) )
            {
            // InternalCQLParser.g:618:2: ( () (otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )? ) )
            // InternalCQLParser.g:619:3: () (otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )? )
            {
            // InternalCQLParser.g:619:3: ()
            // InternalCQLParser.g:620:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getSimpleSelectAccess().getSimpleSelectAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:626:3: (otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )? )
            // InternalCQLParser.g:627:4: otherlv_1= SELECT ( (lv_distinct_2_0= DISTINCT ) )? (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) ) (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* ) (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )? (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )? (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )?
            {
            otherlv_1=(Token)match(input,SELECT,FOLLOW_10); 

            				newLeafNode(otherlv_1, grammarAccess.getSimpleSelectAccess().getSELECTKeyword_1_0());
            			
            // InternalCQLParser.g:631:4: ( (lv_distinct_2_0= DISTINCT ) )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==DISTINCT) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // InternalCQLParser.g:632:5: (lv_distinct_2_0= DISTINCT )
                    {
                    // InternalCQLParser.g:632:5: (lv_distinct_2_0= DISTINCT )
                    // InternalCQLParser.g:633:6: lv_distinct_2_0= DISTINCT
                    {
                    lv_distinct_2_0=(Token)match(input,DISTINCT,FOLLOW_10); 

                    						newLeafNode(lv_distinct_2_0, grammarAccess.getSimpleSelectAccess().getDistinctDISTINCTKeyword_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSimpleSelectRule());
                    						}
                    						setWithLastConsumed(current, "distinct", lv_distinct_2_0, "DISTINCT");
                    					

                    }


                    }
                    break;

            }

            // InternalCQLParser.g:645:4: (otherlv_3= Asterisk | ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* ) )
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==Asterisk) ) {
                alt13=1;
            }
            else if ( (LA13_0==FALSE||LA13_0==TRUE||LA13_0==DollarSign||(LA13_0>=RULE_INT && LA13_0<=RULE_FLOAT)||(LA13_0>=RULE_VECTOR_FLOAT && LA13_0<=RULE_MATRIX_FLOAT)||LA13_0==RULE_STRING) ) {
                alt13=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }
            switch (alt13) {
                case 1 :
                    // InternalCQLParser.g:646:5: otherlv_3= Asterisk
                    {
                    otherlv_3=(Token)match(input,Asterisk,FOLLOW_11); 

                    					newLeafNode(otherlv_3, grammarAccess.getSimpleSelectAccess().getAsteriskKeyword_1_2_0());
                    				

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:651:5: ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* )
                    {
                    // InternalCQLParser.g:651:5: ( ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )* )
                    // InternalCQLParser.g:652:6: ( (lv_arguments_4_0= ruleSelectArgument ) )+ (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )*
                    {
                    // InternalCQLParser.g:652:6: ( (lv_arguments_4_0= ruleSelectArgument ) )+
                    int cnt11=0;
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0==FALSE||LA11_0==TRUE||LA11_0==DollarSign||(LA11_0>=RULE_INT && LA11_0<=RULE_FLOAT)||(LA11_0>=RULE_VECTOR_FLOAT && LA11_0<=RULE_MATRIX_FLOAT)||LA11_0==RULE_STRING) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // InternalCQLParser.g:653:7: (lv_arguments_4_0= ruleSelectArgument )
                    	    {
                    	    // InternalCQLParser.g:653:7: (lv_arguments_4_0= ruleSelectArgument )
                    	    // InternalCQLParser.g:654:8: lv_arguments_4_0= ruleSelectArgument
                    	    {

                    	    								newCompositeNode(grammarAccess.getSimpleSelectAccess().getArgumentsSelectArgumentParserRuleCall_1_2_1_0_0());
                    	    							
                    	    pushFollow(FOLLOW_12);
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
                    	    if ( cnt11 >= 1 ) break loop11;
                                EarlyExitException eee =
                                    new EarlyExitException(11, input);
                                throw eee;
                        }
                        cnt11++;
                    } while (true);

                    // InternalCQLParser.g:671:6: (otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) ) )*
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0==Comma) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // InternalCQLParser.g:672:7: otherlv_5= Comma ( (lv_arguments_6_0= ruleSelectArgument ) )
                    	    {
                    	    otherlv_5=(Token)match(input,Comma,FOLLOW_10); 

                    	    							newLeafNode(otherlv_5, grammarAccess.getSimpleSelectAccess().getCommaKeyword_1_2_1_1_0());
                    	    						
                    	    // InternalCQLParser.g:676:7: ( (lv_arguments_6_0= ruleSelectArgument ) )
                    	    // InternalCQLParser.g:677:8: (lv_arguments_6_0= ruleSelectArgument )
                    	    {
                    	    // InternalCQLParser.g:677:8: (lv_arguments_6_0= ruleSelectArgument )
                    	    // InternalCQLParser.g:678:9: lv_arguments_6_0= ruleSelectArgument
                    	    {

                    	    									newCompositeNode(grammarAccess.getSimpleSelectAccess().getArgumentsSelectArgumentParserRuleCall_1_2_1_1_1_0());
                    	    								
                    	    pushFollow(FOLLOW_13);
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
                    	    break loop12;
                        }
                    } while (true);


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:698:4: (otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )* )
            // InternalCQLParser.g:699:5: otherlv_7= FROM ( (lv_sources_8_0= ruleSource ) )+ (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )*
            {
            otherlv_7=(Token)match(input,FROM,FOLLOW_14); 

            					newLeafNode(otherlv_7, grammarAccess.getSimpleSelectAccess().getFROMKeyword_1_3_0());
            				
            // InternalCQLParser.g:703:5: ( (lv_sources_8_0= ruleSource ) )+
            int cnt14=0;
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==LeftParenthesis||LA14_0==RULE_ID) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // InternalCQLParser.g:704:6: (lv_sources_8_0= ruleSource )
            	    {
            	    // InternalCQLParser.g:704:6: (lv_sources_8_0= ruleSource )
            	    // InternalCQLParser.g:705:7: lv_sources_8_0= ruleSource
            	    {

            	    							newCompositeNode(grammarAccess.getSimpleSelectAccess().getSourcesSourceParserRuleCall_1_3_1_0());
            	    						
            	    pushFollow(FOLLOW_15);
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
            	    if ( cnt14 >= 1 ) break loop14;
                        EarlyExitException eee =
                            new EarlyExitException(14, input);
                        throw eee;
                }
                cnt14++;
            } while (true);

            // InternalCQLParser.g:722:5: (otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) ) )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==Comma) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // InternalCQLParser.g:723:6: otherlv_9= Comma ( (lv_sources_10_0= ruleSource ) )
            	    {
            	    otherlv_9=(Token)match(input,Comma,FOLLOW_14); 

            	    						newLeafNode(otherlv_9, grammarAccess.getSimpleSelectAccess().getCommaKeyword_1_3_2_0());
            	    					
            	    // InternalCQLParser.g:727:6: ( (lv_sources_10_0= ruleSource ) )
            	    // InternalCQLParser.g:728:7: (lv_sources_10_0= ruleSource )
            	    {
            	    // InternalCQLParser.g:728:7: (lv_sources_10_0= ruleSource )
            	    // InternalCQLParser.g:729:8: lv_sources_10_0= ruleSource
            	    {

            	    								newCompositeNode(grammarAccess.getSimpleSelectAccess().getSourcesSourceParserRuleCall_1_3_2_1_0());
            	    							
            	    pushFollow(FOLLOW_16);
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
            	    break loop15;
                }
            } while (true);


            }

            // InternalCQLParser.g:748:4: (otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) ) )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==WHERE) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // InternalCQLParser.g:749:5: otherlv_11= WHERE ( (lv_predicates_12_0= ruleExpressionsModel ) )
                    {
                    otherlv_11=(Token)match(input,WHERE,FOLLOW_17); 

                    					newLeafNode(otherlv_11, grammarAccess.getSimpleSelectAccess().getWHEREKeyword_1_4_0());
                    				
                    // InternalCQLParser.g:753:5: ( (lv_predicates_12_0= ruleExpressionsModel ) )
                    // InternalCQLParser.g:754:6: (lv_predicates_12_0= ruleExpressionsModel )
                    {
                    // InternalCQLParser.g:754:6: (lv_predicates_12_0= ruleExpressionsModel )
                    // InternalCQLParser.g:755:7: lv_predicates_12_0= ruleExpressionsModel
                    {

                    							newCompositeNode(grammarAccess.getSimpleSelectAccess().getPredicatesExpressionsModelParserRuleCall_1_4_1_0());
                    						
                    pushFollow(FOLLOW_18);
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

            // InternalCQLParser.g:773:4: (otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )* )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==GROUP) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // InternalCQLParser.g:774:5: otherlv_13= GROUP otherlv_14= BY ( (lv_order_15_0= ruleAttribute ) )+ (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )*
                    {
                    otherlv_13=(Token)match(input,GROUP,FOLLOW_19); 

                    					newLeafNode(otherlv_13, grammarAccess.getSimpleSelectAccess().getGROUPKeyword_1_5_0());
                    				
                    otherlv_14=(Token)match(input,BY,FOLLOW_20); 

                    					newLeafNode(otherlv_14, grammarAccess.getSimpleSelectAccess().getBYKeyword_1_5_1());
                    				
                    // InternalCQLParser.g:782:5: ( (lv_order_15_0= ruleAttribute ) )+
                    int cnt17=0;
                    loop17:
                    do {
                        int alt17=2;
                        int LA17_0 = input.LA(1);

                        if ( (LA17_0==DollarSign||LA17_0==RULE_ID) ) {
                            alt17=1;
                        }


                        switch (alt17) {
                    	case 1 :
                    	    // InternalCQLParser.g:783:6: (lv_order_15_0= ruleAttribute )
                    	    {
                    	    // InternalCQLParser.g:783:6: (lv_order_15_0= ruleAttribute )
                    	    // InternalCQLParser.g:784:7: lv_order_15_0= ruleAttribute
                    	    {

                    	    							newCompositeNode(grammarAccess.getSimpleSelectAccess().getOrderAttributeParserRuleCall_1_5_2_0());
                    	    						
                    	    pushFollow(FOLLOW_21);
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
                    	    if ( cnt17 >= 1 ) break loop17;
                                EarlyExitException eee =
                                    new EarlyExitException(17, input);
                                throw eee;
                        }
                        cnt17++;
                    } while (true);

                    // InternalCQLParser.g:801:5: (otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) ) )*
                    loop18:
                    do {
                        int alt18=2;
                        int LA18_0 = input.LA(1);

                        if ( (LA18_0==Comma) ) {
                            alt18=1;
                        }


                        switch (alt18) {
                    	case 1 :
                    	    // InternalCQLParser.g:802:6: otherlv_16= Comma ( (lv_order_17_0= ruleAttribute ) )
                    	    {
                    	    otherlv_16=(Token)match(input,Comma,FOLLOW_20); 

                    	    						newLeafNode(otherlv_16, grammarAccess.getSimpleSelectAccess().getCommaKeyword_1_5_3_0());
                    	    					
                    	    // InternalCQLParser.g:806:6: ( (lv_order_17_0= ruleAttribute ) )
                    	    // InternalCQLParser.g:807:7: (lv_order_17_0= ruleAttribute )
                    	    {
                    	    // InternalCQLParser.g:807:7: (lv_order_17_0= ruleAttribute )
                    	    // InternalCQLParser.g:808:8: lv_order_17_0= ruleAttribute
                    	    {

                    	    								newCompositeNode(grammarAccess.getSimpleSelectAccess().getOrderAttributeParserRuleCall_1_5_3_1_0());
                    	    							
                    	    pushFollow(FOLLOW_22);
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
                    	    break loop18;
                        }
                    } while (true);


                    }
                    break;

            }

            // InternalCQLParser.g:827:4: (otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) ) )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==HAVING) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // InternalCQLParser.g:828:5: otherlv_18= HAVING ( (lv_having_19_0= ruleExpressionsModel ) )
                    {
                    otherlv_18=(Token)match(input,HAVING,FOLLOW_17); 

                    					newLeafNode(otherlv_18, grammarAccess.getSimpleSelectAccess().getHAVINGKeyword_1_6_0());
                    				
                    // InternalCQLParser.g:832:5: ( (lv_having_19_0= ruleExpressionsModel ) )
                    // InternalCQLParser.g:833:6: (lv_having_19_0= ruleExpressionsModel )
                    {
                    // InternalCQLParser.g:833:6: (lv_having_19_0= ruleExpressionsModel )
                    // InternalCQLParser.g:834:7: lv_having_19_0= ruleExpressionsModel
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
    // InternalCQLParser.g:857:1: entryRuleComplexSelect returns [EObject current=null] : iv_ruleComplexSelect= ruleComplexSelect EOF ;
    public final EObject entryRuleComplexSelect() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComplexSelect = null;


        try {
            // InternalCQLParser.g:857:54: (iv_ruleComplexSelect= ruleComplexSelect EOF )
            // InternalCQLParser.g:858:2: iv_ruleComplexSelect= ruleComplexSelect EOF
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
    // InternalCQLParser.g:864:1: ruleComplexSelect returns [EObject current=null] : ( () ( (lv_left_1_0= ruleSimpleSelect ) ) ( ( ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) ) ) ( (lv_right_3_0= ruleSimpleSelect ) ) )? ) ;
    public final EObject ruleComplexSelect() throws RecognitionException {
        EObject current = null;

        Token lv_operation_2_1=null;
        Token lv_operation_2_2=null;
        Token lv_operation_2_3=null;
        EObject lv_left_1_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:870:2: ( ( () ( (lv_left_1_0= ruleSimpleSelect ) ) ( ( ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) ) ) ( (lv_right_3_0= ruleSimpleSelect ) ) )? ) )
            // InternalCQLParser.g:871:2: ( () ( (lv_left_1_0= ruleSimpleSelect ) ) ( ( ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) ) ) ( (lv_right_3_0= ruleSimpleSelect ) ) )? )
            {
            // InternalCQLParser.g:871:2: ( () ( (lv_left_1_0= ruleSimpleSelect ) ) ( ( ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) ) ) ( (lv_right_3_0= ruleSimpleSelect ) ) )? )
            // InternalCQLParser.g:872:3: () ( (lv_left_1_0= ruleSimpleSelect ) ) ( ( ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) ) ) ( (lv_right_3_0= ruleSimpleSelect ) ) )?
            {
            // InternalCQLParser.g:872:3: ()
            // InternalCQLParser.g:873:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getComplexSelectAccess().getComplexSelectAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:879:3: ( (lv_left_1_0= ruleSimpleSelect ) )
            // InternalCQLParser.g:880:4: (lv_left_1_0= ruleSimpleSelect )
            {
            // InternalCQLParser.g:880:4: (lv_left_1_0= ruleSimpleSelect )
            // InternalCQLParser.g:881:5: lv_left_1_0= ruleSimpleSelect
            {

            					newCompositeNode(grammarAccess.getComplexSelectAccess().getLeftSimpleSelectParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_23);
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

            // InternalCQLParser.g:898:3: ( ( ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) ) ) ( (lv_right_3_0= ruleSimpleSelect ) ) )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==INTERSECTION||LA22_0==DIFFERENCE||LA22_0==UNION) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // InternalCQLParser.g:899:4: ( ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) ) ) ( (lv_right_3_0= ruleSimpleSelect ) )
                    {
                    // InternalCQLParser.g:899:4: ( ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) ) )
                    // InternalCQLParser.g:900:5: ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) )
                    {
                    // InternalCQLParser.g:900:5: ( (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION ) )
                    // InternalCQLParser.g:901:6: (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION )
                    {
                    // InternalCQLParser.g:901:6: (lv_operation_2_1= UNION | lv_operation_2_2= DIFFERENCE | lv_operation_2_3= INTERSECTION )
                    int alt21=3;
                    switch ( input.LA(1) ) {
                    case UNION:
                        {
                        alt21=1;
                        }
                        break;
                    case DIFFERENCE:
                        {
                        alt21=2;
                        }
                        break;
                    case INTERSECTION:
                        {
                        alt21=3;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 21, 0, input);

                        throw nvae;
                    }

                    switch (alt21) {
                        case 1 :
                            // InternalCQLParser.g:902:7: lv_operation_2_1= UNION
                            {
                            lv_operation_2_1=(Token)match(input,UNION,FOLLOW_24); 

                            							newLeafNode(lv_operation_2_1, grammarAccess.getComplexSelectAccess().getOperationUNIONKeyword_2_0_0_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getComplexSelectRule());
                            							}
                            							setWithLastConsumed(current, "operation", lv_operation_2_1, null);
                            						

                            }
                            break;
                        case 2 :
                            // InternalCQLParser.g:913:7: lv_operation_2_2= DIFFERENCE
                            {
                            lv_operation_2_2=(Token)match(input,DIFFERENCE,FOLLOW_24); 

                            							newLeafNode(lv_operation_2_2, grammarAccess.getComplexSelectAccess().getOperationDIFFERENCEKeyword_2_0_0_1());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getComplexSelectRule());
                            							}
                            							setWithLastConsumed(current, "operation", lv_operation_2_2, null);
                            						

                            }
                            break;
                        case 3 :
                            // InternalCQLParser.g:924:7: lv_operation_2_3= INTERSECTION
                            {
                            lv_operation_2_3=(Token)match(input,INTERSECTION,FOLLOW_24); 

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

                    // InternalCQLParser.g:937:4: ( (lv_right_3_0= ruleSimpleSelect ) )
                    // InternalCQLParser.g:938:5: (lv_right_3_0= ruleSimpleSelect )
                    {
                    // InternalCQLParser.g:938:5: (lv_right_3_0= ruleSimpleSelect )
                    // InternalCQLParser.g:939:6: lv_right_3_0= ruleSimpleSelect
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
    // InternalCQLParser.g:961:1: entryRuleInnerSelect returns [EObject current=null] : iv_ruleInnerSelect= ruleInnerSelect EOF ;
    public final EObject entryRuleInnerSelect() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInnerSelect = null;


        try {
            // InternalCQLParser.g:961:52: (iv_ruleInnerSelect= ruleInnerSelect EOF )
            // InternalCQLParser.g:962:2: iv_ruleInnerSelect= ruleInnerSelect EOF
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
    // InternalCQLParser.g:968:1: ruleInnerSelect returns [EObject current=null] : (otherlv_0= LeftParenthesis ( (lv_select_1_0= ruleSimpleSelect ) ) otherlv_2= RightParenthesis ) ;
    public final EObject ruleInnerSelect() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject lv_select_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:974:2: ( (otherlv_0= LeftParenthesis ( (lv_select_1_0= ruleSimpleSelect ) ) otherlv_2= RightParenthesis ) )
            // InternalCQLParser.g:975:2: (otherlv_0= LeftParenthesis ( (lv_select_1_0= ruleSimpleSelect ) ) otherlv_2= RightParenthesis )
            {
            // InternalCQLParser.g:975:2: (otherlv_0= LeftParenthesis ( (lv_select_1_0= ruleSimpleSelect ) ) otherlv_2= RightParenthesis )
            // InternalCQLParser.g:976:3: otherlv_0= LeftParenthesis ( (lv_select_1_0= ruleSimpleSelect ) ) otherlv_2= RightParenthesis
            {
            otherlv_0=(Token)match(input,LeftParenthesis,FOLLOW_24); 

            			newLeafNode(otherlv_0, grammarAccess.getInnerSelectAccess().getLeftParenthesisKeyword_0());
            		
            // InternalCQLParser.g:980:3: ( (lv_select_1_0= ruleSimpleSelect ) )
            // InternalCQLParser.g:981:4: (lv_select_1_0= ruleSimpleSelect )
            {
            // InternalCQLParser.g:981:4: (lv_select_1_0= ruleSimpleSelect )
            // InternalCQLParser.g:982:5: lv_select_1_0= ruleSimpleSelect
            {

            					newCompositeNode(grammarAccess.getInnerSelectAccess().getSelectSimpleSelectParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_25);
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
    // InternalCQLParser.g:1007:1: entryRuleInnerSelect2 returns [EObject current=null] : iv_ruleInnerSelect2= ruleInnerSelect2 EOF ;
    public final EObject entryRuleInnerSelect2() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInnerSelect2 = null;


        try {
            // InternalCQLParser.g:1007:53: (iv_ruleInnerSelect2= ruleInnerSelect2 EOF )
            // InternalCQLParser.g:1008:2: iv_ruleInnerSelect2= ruleInnerSelect2 EOF
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
    // InternalCQLParser.g:1014:1: ruleInnerSelect2 returns [EObject current=null] : ( (lv_select_0_0= ruleSimpleSelect ) ) ;
    public final EObject ruleInnerSelect2() throws RecognitionException {
        EObject current = null;

        EObject lv_select_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1020:2: ( ( (lv_select_0_0= ruleSimpleSelect ) ) )
            // InternalCQLParser.g:1021:2: ( (lv_select_0_0= ruleSimpleSelect ) )
            {
            // InternalCQLParser.g:1021:2: ( (lv_select_0_0= ruleSimpleSelect ) )
            // InternalCQLParser.g:1022:3: (lv_select_0_0= ruleSimpleSelect )
            {
            // InternalCQLParser.g:1022:3: (lv_select_0_0= ruleSimpleSelect )
            // InternalCQLParser.g:1023:4: lv_select_0_0= ruleSimpleSelect
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
    // InternalCQLParser.g:1043:1: entryRuleSelectArgument returns [EObject current=null] : iv_ruleSelectArgument= ruleSelectArgument EOF ;
    public final EObject entryRuleSelectArgument() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectArgument = null;


        try {
            // InternalCQLParser.g:1043:55: (iv_ruleSelectArgument= ruleSelectArgument EOF )
            // InternalCQLParser.g:1044:2: iv_ruleSelectArgument= ruleSelectArgument EOF
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
    // InternalCQLParser.g:1050:1: ruleSelectArgument returns [EObject current=null] : ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) ) ;
    public final EObject ruleSelectArgument() throws RecognitionException {
        EObject current = null;

        EObject lv_attribute_0_0 = null;

        EObject lv_expression_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1056:2: ( ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) ) )
            // InternalCQLParser.g:1057:2: ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) )
            {
            // InternalCQLParser.g:1057:2: ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) )
            int alt23=2;
            switch ( input.LA(1) ) {
            case RULE_ID:
                {
                switch ( input.LA(2) ) {
                case EOF:
                case FALSE:
                case FROM:
                case TRUE:
                case LeftParenthesisRightParenthesis:
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
                    alt23=1;
                    }
                    break;
                case FullStop:
                    {
                    int LA23_4 = input.LA(3);

                    if ( (LA23_4==Asterisk) ) {
                        alt23=1;
                    }
                    else if ( (LA23_4==RULE_ID) ) {
                        int LA23_5 = input.LA(4);

                        if ( (LA23_5==EOF||LA23_5==FALSE||LA23_5==FROM||LA23_5==TRUE||LA23_5==AS||LA23_5==DollarSign||LA23_5==Comma||(LA23_5>=RULE_INT && LA23_5<=RULE_FLOAT)||(LA23_5>=RULE_VECTOR_FLOAT && LA23_5<=RULE_MATRIX_FLOAT)||LA23_5==RULE_STRING) ) {
                            alt23=1;
                        }
                        else if ( ((LA23_5>=Asterisk && LA23_5<=PlusSign)||LA23_5==HyphenMinus||LA23_5==Solidus||LA23_5==CircumflexAccent) ) {
                            alt23=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 23, 5, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 23, 4, input);

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
                    alt23=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 23, 1, input);

                    throw nvae;
                }

                }
                break;
            case DollarSign:
                {
                alt23=1;
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
                alt23=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;
            }

            switch (alt23) {
                case 1 :
                    // InternalCQLParser.g:1058:3: ( (lv_attribute_0_0= ruleAttribute ) )
                    {
                    // InternalCQLParser.g:1058:3: ( (lv_attribute_0_0= ruleAttribute ) )
                    // InternalCQLParser.g:1059:4: (lv_attribute_0_0= ruleAttribute )
                    {
                    // InternalCQLParser.g:1059:4: (lv_attribute_0_0= ruleAttribute )
                    // InternalCQLParser.g:1060:5: lv_attribute_0_0= ruleAttribute
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
                    // InternalCQLParser.g:1078:3: ( (lv_expression_1_0= ruleSelectExpression ) )
                    {
                    // InternalCQLParser.g:1078:3: ( (lv_expression_1_0= ruleSelectExpression ) )
                    // InternalCQLParser.g:1079:4: (lv_expression_1_0= ruleSelectExpression )
                    {
                    // InternalCQLParser.g:1079:4: (lv_expression_1_0= ruleSelectExpression )
                    // InternalCQLParser.g:1080:5: lv_expression_1_0= ruleSelectExpression
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
    // InternalCQLParser.g:1101:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalCQLParser.g:1101:47: (iv_ruleSource= ruleSource EOF )
            // InternalCQLParser.g:1102:2: iv_ruleSource= ruleSource EOF
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
    // InternalCQLParser.g:1108:1: ruleSource returns [EObject current=null] : (this_SimpleSource_0= ruleSimpleSource | this_NestedSource_1= ruleNestedSource ) ;
    public final EObject ruleSource() throws RecognitionException {
        EObject current = null;

        EObject this_SimpleSource_0 = null;

        EObject this_NestedSource_1 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1114:2: ( (this_SimpleSource_0= ruleSimpleSource | this_NestedSource_1= ruleNestedSource ) )
            // InternalCQLParser.g:1115:2: (this_SimpleSource_0= ruleSimpleSource | this_NestedSource_1= ruleNestedSource )
            {
            // InternalCQLParser.g:1115:2: (this_SimpleSource_0= ruleSimpleSource | this_NestedSource_1= ruleNestedSource )
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==RULE_ID) ) {
                alt24=1;
            }
            else if ( (LA24_0==LeftParenthesis) ) {
                alt24=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;
            }
            switch (alt24) {
                case 1 :
                    // InternalCQLParser.g:1116:3: this_SimpleSource_0= ruleSimpleSource
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
                    // InternalCQLParser.g:1125:3: this_NestedSource_1= ruleNestedSource
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
    // InternalCQLParser.g:1137:1: entryRuleSimpleSource returns [EObject current=null] : iv_ruleSimpleSource= ruleSimpleSource EOF ;
    public final EObject entryRuleSimpleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSimpleSource = null;


        try {
            // InternalCQLParser.g:1137:53: (iv_ruleSimpleSource= ruleSimpleSource EOF )
            // InternalCQLParser.g:1138:2: iv_ruleSimpleSource= ruleSimpleSource EOF
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
    // InternalCQLParser.g:1144:1: ruleSimpleSource returns [EObject current=null] : ( () ( (lv_name_1_0= ruleQualifiedSourcename ) ) ( (lv_window_2_0= ruleWindowOperator ) )? (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )? ) ;
    public final EObject ruleSimpleSource() throws RecognitionException {
        EObject current = null;

        Token otherlv_3=null;
        AntlrDatatypeRuleToken lv_name_1_0 = null;

        EObject lv_window_2_0 = null;

        EObject lv_alias_4_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1150:2: ( ( () ( (lv_name_1_0= ruleQualifiedSourcename ) ) ( (lv_window_2_0= ruleWindowOperator ) )? (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:1151:2: ( () ( (lv_name_1_0= ruleQualifiedSourcename ) ) ( (lv_window_2_0= ruleWindowOperator ) )? (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:1151:2: ( () ( (lv_name_1_0= ruleQualifiedSourcename ) ) ( (lv_window_2_0= ruleWindowOperator ) )? (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:1152:3: () ( (lv_name_1_0= ruleQualifiedSourcename ) ) ( (lv_window_2_0= ruleWindowOperator ) )? (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:1152:3: ()
            // InternalCQLParser.g:1153:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getSimpleSourceAccess().getSimpleSourceAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:1159:3: ( (lv_name_1_0= ruleQualifiedSourcename ) )
            // InternalCQLParser.g:1160:4: (lv_name_1_0= ruleQualifiedSourcename )
            {
            // InternalCQLParser.g:1160:4: (lv_name_1_0= ruleQualifiedSourcename )
            // InternalCQLParser.g:1161:5: lv_name_1_0= ruleQualifiedSourcename
            {

            					newCompositeNode(grammarAccess.getSimpleSourceAccess().getNameQualifiedSourcenameParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_26);
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

            // InternalCQLParser.g:1178:3: ( (lv_window_2_0= ruleWindowOperator ) )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==LeftSquareBracket) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // InternalCQLParser.g:1179:4: (lv_window_2_0= ruleWindowOperator )
                    {
                    // InternalCQLParser.g:1179:4: (lv_window_2_0= ruleWindowOperator )
                    // InternalCQLParser.g:1180:5: lv_window_2_0= ruleWindowOperator
                    {

                    					newCompositeNode(grammarAccess.getSimpleSourceAccess().getWindowWindowOperatorParserRuleCall_2_0());
                    				
                    pushFollow(FOLLOW_27);
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

            // InternalCQLParser.g:1197:3: (otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) ) )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==AS) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // InternalCQLParser.g:1198:4: otherlv_3= AS ( (lv_alias_4_0= ruleAlias ) )
                    {
                    otherlv_3=(Token)match(input,AS,FOLLOW_5); 

                    				newLeafNode(otherlv_3, grammarAccess.getSimpleSourceAccess().getASKeyword_3_0());
                    			
                    // InternalCQLParser.g:1202:4: ( (lv_alias_4_0= ruleAlias ) )
                    // InternalCQLParser.g:1203:5: (lv_alias_4_0= ruleAlias )
                    {
                    // InternalCQLParser.g:1203:5: (lv_alias_4_0= ruleAlias )
                    // InternalCQLParser.g:1204:6: lv_alias_4_0= ruleAlias
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
    // InternalCQLParser.g:1226:1: entryRuleNestedSource returns [EObject current=null] : iv_ruleNestedSource= ruleNestedSource EOF ;
    public final EObject entryRuleNestedSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNestedSource = null;


        try {
            // InternalCQLParser.g:1226:53: (iv_ruleNestedSource= ruleNestedSource EOF )
            // InternalCQLParser.g:1227:2: iv_ruleNestedSource= ruleNestedSource EOF
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
    // InternalCQLParser.g:1233:1: ruleNestedSource returns [EObject current=null] : ( () ( (lv_statement_1_0= ruleInnerSelect ) ) otherlv_2= AS ( (lv_alias_3_0= ruleAlias ) ) ) ;
    public final EObject ruleNestedSource() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject lv_statement_1_0 = null;

        EObject lv_alias_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1239:2: ( ( () ( (lv_statement_1_0= ruleInnerSelect ) ) otherlv_2= AS ( (lv_alias_3_0= ruleAlias ) ) ) )
            // InternalCQLParser.g:1240:2: ( () ( (lv_statement_1_0= ruleInnerSelect ) ) otherlv_2= AS ( (lv_alias_3_0= ruleAlias ) ) )
            {
            // InternalCQLParser.g:1240:2: ( () ( (lv_statement_1_0= ruleInnerSelect ) ) otherlv_2= AS ( (lv_alias_3_0= ruleAlias ) ) )
            // InternalCQLParser.g:1241:3: () ( (lv_statement_1_0= ruleInnerSelect ) ) otherlv_2= AS ( (lv_alias_3_0= ruleAlias ) )
            {
            // InternalCQLParser.g:1241:3: ()
            // InternalCQLParser.g:1242:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getNestedSourceAccess().getNestedSourceAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:1248:3: ( (lv_statement_1_0= ruleInnerSelect ) )
            // InternalCQLParser.g:1249:4: (lv_statement_1_0= ruleInnerSelect )
            {
            // InternalCQLParser.g:1249:4: (lv_statement_1_0= ruleInnerSelect )
            // InternalCQLParser.g:1250:5: lv_statement_1_0= ruleInnerSelect
            {

            					newCompositeNode(grammarAccess.getNestedSourceAccess().getStatementInnerSelectParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_28);
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
            		
            // InternalCQLParser.g:1271:3: ( (lv_alias_3_0= ruleAlias ) )
            // InternalCQLParser.g:1272:4: (lv_alias_3_0= ruleAlias )
            {
            // InternalCQLParser.g:1272:4: (lv_alias_3_0= ruleAlias )
            // InternalCQLParser.g:1273:5: lv_alias_3_0= ruleAlias
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
    // InternalCQLParser.g:1294:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCQLParser.g:1294:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCQLParser.g:1295:2: iv_ruleAttribute= ruleAttribute EOF
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
    // InternalCQLParser.g:1301:1: ruleAttribute returns [EObject current=null] : ( ( (lv_name_0_0= ruleQualifiedAttributename ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_name_0_0 = null;

        EObject lv_alias_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1307:2: ( ( ( (lv_name_0_0= ruleQualifiedAttributename ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:1308:2: ( ( (lv_name_0_0= ruleQualifiedAttributename ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:1308:2: ( ( (lv_name_0_0= ruleQualifiedAttributename ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:1309:3: ( (lv_name_0_0= ruleQualifiedAttributename ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:1309:3: ( (lv_name_0_0= ruleQualifiedAttributename ) )
            // InternalCQLParser.g:1310:4: (lv_name_0_0= ruleQualifiedAttributename )
            {
            // InternalCQLParser.g:1310:4: (lv_name_0_0= ruleQualifiedAttributename )
            // InternalCQLParser.g:1311:5: lv_name_0_0= ruleQualifiedAttributename
            {

            					newCompositeNode(grammarAccess.getAttributeAccess().getNameQualifiedAttributenameParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_27);
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

            // InternalCQLParser.g:1328:3: (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )?
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==AS) ) {
                alt27=1;
            }
            switch (alt27) {
                case 1 :
                    // InternalCQLParser.g:1329:4: otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) )
                    {
                    otherlv_1=(Token)match(input,AS,FOLLOW_5); 

                    				newLeafNode(otherlv_1, grammarAccess.getAttributeAccess().getASKeyword_1_0());
                    			
                    // InternalCQLParser.g:1333:4: ( (lv_alias_2_0= ruleAlias ) )
                    // InternalCQLParser.g:1334:5: (lv_alias_2_0= ruleAlias )
                    {
                    // InternalCQLParser.g:1334:5: (lv_alias_2_0= ruleAlias )
                    // InternalCQLParser.g:1335:6: lv_alias_2_0= ruleAlias
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
    // InternalCQLParser.g:1357:1: entryRuleAttributeWithoutAliasDefinition returns [EObject current=null] : iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF ;
    public final EObject entryRuleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithoutAliasDefinition = null;


        try {
            // InternalCQLParser.g:1357:72: (iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF )
            // InternalCQLParser.g:1358:2: iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF
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
    // InternalCQLParser.g:1364:1: ruleAttributeWithoutAliasDefinition returns [EObject current=null] : ( (lv_name_0_0= ruleQualifiedAttributename ) ) ;
    public final EObject ruleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_name_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1370:2: ( ( (lv_name_0_0= ruleQualifiedAttributename ) ) )
            // InternalCQLParser.g:1371:2: ( (lv_name_0_0= ruleQualifiedAttributename ) )
            {
            // InternalCQLParser.g:1371:2: ( (lv_name_0_0= ruleQualifiedAttributename ) )
            // InternalCQLParser.g:1372:3: (lv_name_0_0= ruleQualifiedAttributename )
            {
            // InternalCQLParser.g:1372:3: (lv_name_0_0= ruleQualifiedAttributename )
            // InternalCQLParser.g:1373:4: lv_name_0_0= ruleQualifiedAttributename
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
    // InternalCQLParser.g:1393:1: entryRuleAttributeForSelectExpression returns [EObject current=null] : iv_ruleAttributeForSelectExpression= ruleAttributeForSelectExpression EOF ;
    public final EObject entryRuleAttributeForSelectExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeForSelectExpression = null;


        try {
            // InternalCQLParser.g:1393:69: (iv_ruleAttributeForSelectExpression= ruleAttributeForSelectExpression EOF )
            // InternalCQLParser.g:1394:2: iv_ruleAttributeForSelectExpression= ruleAttributeForSelectExpression EOF
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
    // InternalCQLParser.g:1400:1: ruleAttributeForSelectExpression returns [EObject current=null] : ( (lv_name_0_0= ruleQualifiedAttributenameWithoutSpecialChars ) ) ;
    public final EObject ruleAttributeForSelectExpression() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_name_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1406:2: ( ( (lv_name_0_0= ruleQualifiedAttributenameWithoutSpecialChars ) ) )
            // InternalCQLParser.g:1407:2: ( (lv_name_0_0= ruleQualifiedAttributenameWithoutSpecialChars ) )
            {
            // InternalCQLParser.g:1407:2: ( (lv_name_0_0= ruleQualifiedAttributenameWithoutSpecialChars ) )
            // InternalCQLParser.g:1408:3: (lv_name_0_0= ruleQualifiedAttributenameWithoutSpecialChars )
            {
            // InternalCQLParser.g:1408:3: (lv_name_0_0= ruleQualifiedAttributenameWithoutSpecialChars )
            // InternalCQLParser.g:1409:4: lv_name_0_0= ruleQualifiedAttributenameWithoutSpecialChars
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
    // InternalCQLParser.g:1429:1: entryRuleComplexPredicate returns [EObject current=null] : iv_ruleComplexPredicate= ruleComplexPredicate EOF ;
    public final EObject entryRuleComplexPredicate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComplexPredicate = null;


        try {
            // InternalCQLParser.g:1429:57: (iv_ruleComplexPredicate= ruleComplexPredicate EOF )
            // InternalCQLParser.g:1430:2: iv_ruleComplexPredicate= ruleComplexPredicate EOF
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
    // InternalCQLParser.g:1436:1: ruleComplexPredicate returns [EObject current=null] : ( ( ( (lv_quantification_0_0= ruleQuantificationPredicate ) ) | ( (lv_exists_1_0= ruleExistPredicate ) ) | ( (lv_in_2_0= ruleInPredicate ) ) ) ( (lv_select_3_0= ruleInnerSelect ) ) ) ;
    public final EObject ruleComplexPredicate() throws RecognitionException {
        EObject current = null;

        EObject lv_quantification_0_0 = null;

        EObject lv_exists_1_0 = null;

        EObject lv_in_2_0 = null;

        EObject lv_select_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1442:2: ( ( ( ( (lv_quantification_0_0= ruleQuantificationPredicate ) ) | ( (lv_exists_1_0= ruleExistPredicate ) ) | ( (lv_in_2_0= ruleInPredicate ) ) ) ( (lv_select_3_0= ruleInnerSelect ) ) ) )
            // InternalCQLParser.g:1443:2: ( ( ( (lv_quantification_0_0= ruleQuantificationPredicate ) ) | ( (lv_exists_1_0= ruleExistPredicate ) ) | ( (lv_in_2_0= ruleInPredicate ) ) ) ( (lv_select_3_0= ruleInnerSelect ) ) )
            {
            // InternalCQLParser.g:1443:2: ( ( ( (lv_quantification_0_0= ruleQuantificationPredicate ) ) | ( (lv_exists_1_0= ruleExistPredicate ) ) | ( (lv_in_2_0= ruleInPredicate ) ) ) ( (lv_select_3_0= ruleInnerSelect ) ) )
            // InternalCQLParser.g:1444:3: ( ( (lv_quantification_0_0= ruleQuantificationPredicate ) ) | ( (lv_exists_1_0= ruleExistPredicate ) ) | ( (lv_in_2_0= ruleInPredicate ) ) ) ( (lv_select_3_0= ruleInnerSelect ) )
            {
            // InternalCQLParser.g:1444:3: ( ( (lv_quantification_0_0= ruleQuantificationPredicate ) ) | ( (lv_exists_1_0= ruleExistPredicate ) ) | ( (lv_in_2_0= ruleInPredicate ) ) )
            int alt28=3;
            alt28 = dfa28.predict(input);
            switch (alt28) {
                case 1 :
                    // InternalCQLParser.g:1445:4: ( (lv_quantification_0_0= ruleQuantificationPredicate ) )
                    {
                    // InternalCQLParser.g:1445:4: ( (lv_quantification_0_0= ruleQuantificationPredicate ) )
                    // InternalCQLParser.g:1446:5: (lv_quantification_0_0= ruleQuantificationPredicate )
                    {
                    // InternalCQLParser.g:1446:5: (lv_quantification_0_0= ruleQuantificationPredicate )
                    // InternalCQLParser.g:1447:6: lv_quantification_0_0= ruleQuantificationPredicate
                    {

                    						newCompositeNode(grammarAccess.getComplexPredicateAccess().getQuantificationQuantificationPredicateParserRuleCall_0_0_0());
                    					
                    pushFollow(FOLLOW_14);
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
                    // InternalCQLParser.g:1465:4: ( (lv_exists_1_0= ruleExistPredicate ) )
                    {
                    // InternalCQLParser.g:1465:4: ( (lv_exists_1_0= ruleExistPredicate ) )
                    // InternalCQLParser.g:1466:5: (lv_exists_1_0= ruleExistPredicate )
                    {
                    // InternalCQLParser.g:1466:5: (lv_exists_1_0= ruleExistPredicate )
                    // InternalCQLParser.g:1467:6: lv_exists_1_0= ruleExistPredicate
                    {

                    						newCompositeNode(grammarAccess.getComplexPredicateAccess().getExistsExistPredicateParserRuleCall_0_1_0());
                    					
                    pushFollow(FOLLOW_14);
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
                    // InternalCQLParser.g:1485:4: ( (lv_in_2_0= ruleInPredicate ) )
                    {
                    // InternalCQLParser.g:1485:4: ( (lv_in_2_0= ruleInPredicate ) )
                    // InternalCQLParser.g:1486:5: (lv_in_2_0= ruleInPredicate )
                    {
                    // InternalCQLParser.g:1486:5: (lv_in_2_0= ruleInPredicate )
                    // InternalCQLParser.g:1487:6: lv_in_2_0= ruleInPredicate
                    {

                    						newCompositeNode(grammarAccess.getComplexPredicateAccess().getInInPredicateParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_14);
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

            // InternalCQLParser.g:1505:3: ( (lv_select_3_0= ruleInnerSelect ) )
            // InternalCQLParser.g:1506:4: (lv_select_3_0= ruleInnerSelect )
            {
            // InternalCQLParser.g:1506:4: (lv_select_3_0= ruleInnerSelect )
            // InternalCQLParser.g:1507:5: lv_select_3_0= ruleInnerSelect
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
    // InternalCQLParser.g:1528:1: entryRuleQuantificationPredicate returns [EObject current=null] : iv_ruleQuantificationPredicate= ruleQuantificationPredicate EOF ;
    public final EObject entryRuleQuantificationPredicate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleQuantificationPredicate = null;


        try {
            // InternalCQLParser.g:1528:64: (iv_ruleQuantificationPredicate= ruleQuantificationPredicate EOF )
            // InternalCQLParser.g:1529:2: iv_ruleQuantificationPredicate= ruleQuantificationPredicate EOF
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
    // InternalCQLParser.g:1535:1: ruleQuantificationPredicate returns [EObject current=null] : ( ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_operator_1_0= ruleCOMPARE_OPERATOR ) ) ( ( (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME ) ) ) ) ;
    public final EObject ruleQuantificationPredicate() throws RecognitionException {
        EObject current = null;

        Token lv_predicate_2_1=null;
        Token lv_predicate_2_2=null;
        Token lv_predicate_2_3=null;
        EObject lv_attribute_0_0 = null;

        AntlrDatatypeRuleToken lv_operator_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1541:2: ( ( ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_operator_1_0= ruleCOMPARE_OPERATOR ) ) ( ( (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME ) ) ) ) )
            // InternalCQLParser.g:1542:2: ( ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_operator_1_0= ruleCOMPARE_OPERATOR ) ) ( ( (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME ) ) ) )
            {
            // InternalCQLParser.g:1542:2: ( ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_operator_1_0= ruleCOMPARE_OPERATOR ) ) ( ( (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME ) ) ) )
            // InternalCQLParser.g:1543:3: ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_operator_1_0= ruleCOMPARE_OPERATOR ) ) ( ( (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME ) ) )
            {
            // InternalCQLParser.g:1543:3: ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQLParser.g:1544:4: (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1544:4: (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQLParser.g:1545:5: lv_attribute_0_0= ruleAttributeWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getQuantificationPredicateAccess().getAttributeAttributeWithoutAliasDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_29);
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

            // InternalCQLParser.g:1562:3: ( (lv_operator_1_0= ruleCOMPARE_OPERATOR ) )
            // InternalCQLParser.g:1563:4: (lv_operator_1_0= ruleCOMPARE_OPERATOR )
            {
            // InternalCQLParser.g:1563:4: (lv_operator_1_0= ruleCOMPARE_OPERATOR )
            // InternalCQLParser.g:1564:5: lv_operator_1_0= ruleCOMPARE_OPERATOR
            {

            					newCompositeNode(grammarAccess.getQuantificationPredicateAccess().getOperatorCOMPARE_OPERATORParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_30);
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

            // InternalCQLParser.g:1581:3: ( ( (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME ) ) )
            // InternalCQLParser.g:1582:4: ( (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME ) )
            {
            // InternalCQLParser.g:1582:4: ( (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME ) )
            // InternalCQLParser.g:1583:5: (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME )
            {
            // InternalCQLParser.g:1583:5: (lv_predicate_2_1= ALL | lv_predicate_2_2= ANY | lv_predicate_2_3= SOME )
            int alt29=3;
            switch ( input.LA(1) ) {
            case ALL:
                {
                alt29=1;
                }
                break;
            case ANY:
                {
                alt29=2;
                }
                break;
            case SOME:
                {
                alt29=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;
            }

            switch (alt29) {
                case 1 :
                    // InternalCQLParser.g:1584:6: lv_predicate_2_1= ALL
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
                    // InternalCQLParser.g:1595:6: lv_predicate_2_2= ANY
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
                    // InternalCQLParser.g:1606:6: lv_predicate_2_3= SOME
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
    // InternalCQLParser.g:1623:1: entryRuleExistPredicate returns [EObject current=null] : iv_ruleExistPredicate= ruleExistPredicate EOF ;
    public final EObject entryRuleExistPredicate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExistPredicate = null;


        try {
            // InternalCQLParser.g:1623:55: (iv_ruleExistPredicate= ruleExistPredicate EOF )
            // InternalCQLParser.g:1624:2: iv_ruleExistPredicate= ruleExistPredicate EOF
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
    // InternalCQLParser.g:1630:1: ruleExistPredicate returns [EObject current=null] : ( (lv_predicate_0_0= EXISTS ) ) ;
    public final EObject ruleExistPredicate() throws RecognitionException {
        EObject current = null;

        Token lv_predicate_0_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1636:2: ( ( (lv_predicate_0_0= EXISTS ) ) )
            // InternalCQLParser.g:1637:2: ( (lv_predicate_0_0= EXISTS ) )
            {
            // InternalCQLParser.g:1637:2: ( (lv_predicate_0_0= EXISTS ) )
            // InternalCQLParser.g:1638:3: (lv_predicate_0_0= EXISTS )
            {
            // InternalCQLParser.g:1638:3: (lv_predicate_0_0= EXISTS )
            // InternalCQLParser.g:1639:4: lv_predicate_0_0= EXISTS
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
    // InternalCQLParser.g:1654:1: entryRuleInPredicate returns [EObject current=null] : iv_ruleInPredicate= ruleInPredicate EOF ;
    public final EObject entryRuleInPredicate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInPredicate = null;


        try {
            // InternalCQLParser.g:1654:52: (iv_ruleInPredicate= ruleInPredicate EOF )
            // InternalCQLParser.g:1655:2: iv_ruleInPredicate= ruleInPredicate EOF
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
    // InternalCQLParser.g:1661:1: ruleInPredicate returns [EObject current=null] : ( ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_predicate_1_0= IN ) ) ) ;
    public final EObject ruleInPredicate() throws RecognitionException {
        EObject current = null;

        Token lv_predicate_1_0=null;
        EObject lv_attribute_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1667:2: ( ( ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_predicate_1_0= IN ) ) ) )
            // InternalCQLParser.g:1668:2: ( ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_predicate_1_0= IN ) ) )
            {
            // InternalCQLParser.g:1668:2: ( ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_predicate_1_0= IN ) ) )
            // InternalCQLParser.g:1669:3: ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( (lv_predicate_1_0= IN ) )
            {
            // InternalCQLParser.g:1669:3: ( (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQLParser.g:1670:4: (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1670:4: (lv_attribute_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQLParser.g:1671:5: lv_attribute_0_0= ruleAttributeWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getInPredicateAccess().getAttributeAttributeWithoutAliasDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_31);
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

            // InternalCQLParser.g:1688:3: ( (lv_predicate_1_0= IN ) )
            // InternalCQLParser.g:1689:4: (lv_predicate_1_0= IN )
            {
            // InternalCQLParser.g:1689:4: (lv_predicate_1_0= IN )
            // InternalCQLParser.g:1690:5: lv_predicate_1_0= IN
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
    // InternalCQLParser.g:1706:1: entryRuleAndOperator returns [String current=null] : iv_ruleAndOperator= ruleAndOperator EOF ;
    public final String entryRuleAndOperator() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleAndOperator = null;


        try {
            // InternalCQLParser.g:1706:51: (iv_ruleAndOperator= ruleAndOperator EOF )
            // InternalCQLParser.g:1707:2: iv_ruleAndOperator= ruleAndOperator EOF
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
    // InternalCQLParser.g:1713:1: ruleAndOperator returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= AND ;
    public final AntlrDatatypeRuleToken ruleAndOperator() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1719:2: (kw= AND )
            // InternalCQLParser.g:1720:2: kw= AND
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
    // InternalCQLParser.g:1728:1: entryRuleOrOperator returns [String current=null] : iv_ruleOrOperator= ruleOrOperator EOF ;
    public final String entryRuleOrOperator() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOrOperator = null;


        try {
            // InternalCQLParser.g:1728:50: (iv_ruleOrOperator= ruleOrOperator EOF )
            // InternalCQLParser.g:1729:2: iv_ruleOrOperator= ruleOrOperator EOF
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
    // InternalCQLParser.g:1735:1: ruleOrOperator returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= OR ;
    public final AntlrDatatypeRuleToken ruleOrOperator() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1741:2: (kw= OR )
            // InternalCQLParser.g:1742:2: kw= OR
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
    // InternalCQLParser.g:1750:1: entryRuleEQUALITIY_OPERATOR returns [String current=null] : iv_ruleEQUALITIY_OPERATOR= ruleEQUALITIY_OPERATOR EOF ;
    public final String entryRuleEQUALITIY_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleEQUALITIY_OPERATOR = null;


        try {
            // InternalCQLParser.g:1750:58: (iv_ruleEQUALITIY_OPERATOR= ruleEQUALITIY_OPERATOR EOF )
            // InternalCQLParser.g:1751:2: iv_ruleEQUALITIY_OPERATOR= ruleEQUALITIY_OPERATOR EOF
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
    // InternalCQLParser.g:1757:1: ruleEQUALITIY_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= EqualsSign | kw= ExclamationMarkEqualsSign ) ;
    public final AntlrDatatypeRuleToken ruleEQUALITIY_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1763:2: ( (kw= EqualsSign | kw= ExclamationMarkEqualsSign ) )
            // InternalCQLParser.g:1764:2: (kw= EqualsSign | kw= ExclamationMarkEqualsSign )
            {
            // InternalCQLParser.g:1764:2: (kw= EqualsSign | kw= ExclamationMarkEqualsSign )
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==EqualsSign) ) {
                alt30=1;
            }
            else if ( (LA30_0==ExclamationMarkEqualsSign) ) {
                alt30=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 30, 0, input);

                throw nvae;
            }
            switch (alt30) {
                case 1 :
                    // InternalCQLParser.g:1765:3: kw= EqualsSign
                    {
                    kw=(Token)match(input,EqualsSign,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getEQUALITIY_OPERATORAccess().getEqualsSignKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1771:3: kw= ExclamationMarkEqualsSign
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
    // InternalCQLParser.g:1780:1: entryRuleCOMPARE_OPERATOR returns [String current=null] : iv_ruleCOMPARE_OPERATOR= ruleCOMPARE_OPERATOR EOF ;
    public final String entryRuleCOMPARE_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleCOMPARE_OPERATOR = null;


        try {
            // InternalCQLParser.g:1780:56: (iv_ruleCOMPARE_OPERATOR= ruleCOMPARE_OPERATOR EOF )
            // InternalCQLParser.g:1781:2: iv_ruleCOMPARE_OPERATOR= ruleCOMPARE_OPERATOR EOF
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
    // InternalCQLParser.g:1787:1: ruleCOMPARE_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= GreaterThanSignEqualsSign | kw= LessThanSignEqualsSign | kw= LessThanSign | kw= GreaterThanSign ) ;
    public final AntlrDatatypeRuleToken ruleCOMPARE_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1793:2: ( (kw= GreaterThanSignEqualsSign | kw= LessThanSignEqualsSign | kw= LessThanSign | kw= GreaterThanSign ) )
            // InternalCQLParser.g:1794:2: (kw= GreaterThanSignEqualsSign | kw= LessThanSignEqualsSign | kw= LessThanSign | kw= GreaterThanSign )
            {
            // InternalCQLParser.g:1794:2: (kw= GreaterThanSignEqualsSign | kw= LessThanSignEqualsSign | kw= LessThanSign | kw= GreaterThanSign )
            int alt31=4;
            switch ( input.LA(1) ) {
            case GreaterThanSignEqualsSign:
                {
                alt31=1;
                }
                break;
            case LessThanSignEqualsSign:
                {
                alt31=2;
                }
                break;
            case LessThanSign:
                {
                alt31=3;
                }
                break;
            case GreaterThanSign:
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
                    // InternalCQLParser.g:1795:3: kw= GreaterThanSignEqualsSign
                    {
                    kw=(Token)match(input,GreaterThanSignEqualsSign,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getCOMPARE_OPERATORAccess().getGreaterThanSignEqualsSignKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1801:3: kw= LessThanSignEqualsSign
                    {
                    kw=(Token)match(input,LessThanSignEqualsSign,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getCOMPARE_OPERATORAccess().getLessThanSignEqualsSignKeyword_1());
                    		

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:1807:3: kw= LessThanSign
                    {
                    kw=(Token)match(input,LessThanSign,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getCOMPARE_OPERATORAccess().getLessThanSignKeyword_2());
                    		

                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:1813:3: kw= GreaterThanSign
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
    // InternalCQLParser.g:1822:1: entryRuleARITHMETIC_OPERATOR returns [String current=null] : iv_ruleARITHMETIC_OPERATOR= ruleARITHMETIC_OPERATOR EOF ;
    public final String entryRuleARITHMETIC_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleARITHMETIC_OPERATOR = null;


        try {
            // InternalCQLParser.g:1822:59: (iv_ruleARITHMETIC_OPERATOR= ruleARITHMETIC_OPERATOR EOF )
            // InternalCQLParser.g:1823:2: iv_ruleARITHMETIC_OPERATOR= ruleARITHMETIC_OPERATOR EOF
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
    // InternalCQLParser.g:1829:1: ruleARITHMETIC_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ADD_OPERATOR_0= ruleADD_OPERATOR | this_MINUS_OPERATOR_1= ruleMINUS_OPERATOR | this_MUL_OR_DIV_OPERATOR_2= ruleMUL_OR_DIV_OPERATOR | this_EXPONENT_OPERATOR_3= ruleEXPONENT_OPERATOR ) ;
    public final AntlrDatatypeRuleToken ruleARITHMETIC_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        AntlrDatatypeRuleToken this_ADD_OPERATOR_0 = null;

        AntlrDatatypeRuleToken this_MINUS_OPERATOR_1 = null;

        AntlrDatatypeRuleToken this_MUL_OR_DIV_OPERATOR_2 = null;

        AntlrDatatypeRuleToken this_EXPONENT_OPERATOR_3 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1835:2: ( (this_ADD_OPERATOR_0= ruleADD_OPERATOR | this_MINUS_OPERATOR_1= ruleMINUS_OPERATOR | this_MUL_OR_DIV_OPERATOR_2= ruleMUL_OR_DIV_OPERATOR | this_EXPONENT_OPERATOR_3= ruleEXPONENT_OPERATOR ) )
            // InternalCQLParser.g:1836:2: (this_ADD_OPERATOR_0= ruleADD_OPERATOR | this_MINUS_OPERATOR_1= ruleMINUS_OPERATOR | this_MUL_OR_DIV_OPERATOR_2= ruleMUL_OR_DIV_OPERATOR | this_EXPONENT_OPERATOR_3= ruleEXPONENT_OPERATOR )
            {
            // InternalCQLParser.g:1836:2: (this_ADD_OPERATOR_0= ruleADD_OPERATOR | this_MINUS_OPERATOR_1= ruleMINUS_OPERATOR | this_MUL_OR_DIV_OPERATOR_2= ruleMUL_OR_DIV_OPERATOR | this_EXPONENT_OPERATOR_3= ruleEXPONENT_OPERATOR )
            int alt32=4;
            switch ( input.LA(1) ) {
            case PlusSign:
                {
                alt32=1;
                }
                break;
            case HyphenMinus:
                {
                alt32=2;
                }
                break;
            case Asterisk:
            case Solidus:
                {
                alt32=3;
                }
                break;
            case CircumflexAccent:
                {
                alt32=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }

            switch (alt32) {
                case 1 :
                    // InternalCQLParser.g:1837:3: this_ADD_OPERATOR_0= ruleADD_OPERATOR
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
                    // InternalCQLParser.g:1848:3: this_MINUS_OPERATOR_1= ruleMINUS_OPERATOR
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
                    // InternalCQLParser.g:1859:3: this_MUL_OR_DIV_OPERATOR_2= ruleMUL_OR_DIV_OPERATOR
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
                    // InternalCQLParser.g:1870:3: this_EXPONENT_OPERATOR_3= ruleEXPONENT_OPERATOR
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
    // InternalCQLParser.g:1884:1: entryRuleEXPONENT_OPERATOR returns [String current=null] : iv_ruleEXPONENT_OPERATOR= ruleEXPONENT_OPERATOR EOF ;
    public final String entryRuleEXPONENT_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleEXPONENT_OPERATOR = null;


        try {
            // InternalCQLParser.g:1884:57: (iv_ruleEXPONENT_OPERATOR= ruleEXPONENT_OPERATOR EOF )
            // InternalCQLParser.g:1885:2: iv_ruleEXPONENT_OPERATOR= ruleEXPONENT_OPERATOR EOF
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
    // InternalCQLParser.g:1891:1: ruleEXPONENT_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= CircumflexAccent ;
    public final AntlrDatatypeRuleToken ruleEXPONENT_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1897:2: (kw= CircumflexAccent )
            // InternalCQLParser.g:1898:2: kw= CircumflexAccent
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
    // InternalCQLParser.g:1906:1: entryRuleMUL_OR_DIV_OPERATOR returns [String current=null] : iv_ruleMUL_OR_DIV_OPERATOR= ruleMUL_OR_DIV_OPERATOR EOF ;
    public final String entryRuleMUL_OR_DIV_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleMUL_OR_DIV_OPERATOR = null;


        try {
            // InternalCQLParser.g:1906:59: (iv_ruleMUL_OR_DIV_OPERATOR= ruleMUL_OR_DIV_OPERATOR EOF )
            // InternalCQLParser.g:1907:2: iv_ruleMUL_OR_DIV_OPERATOR= ruleMUL_OR_DIV_OPERATOR EOF
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
    // InternalCQLParser.g:1913:1: ruleMUL_OR_DIV_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= Solidus | kw= Asterisk ) ;
    public final AntlrDatatypeRuleToken ruleMUL_OR_DIV_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1919:2: ( (kw= Solidus | kw= Asterisk ) )
            // InternalCQLParser.g:1920:2: (kw= Solidus | kw= Asterisk )
            {
            // InternalCQLParser.g:1920:2: (kw= Solidus | kw= Asterisk )
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==Solidus) ) {
                alt33=1;
            }
            else if ( (LA33_0==Asterisk) ) {
                alt33=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 33, 0, input);

                throw nvae;
            }
            switch (alt33) {
                case 1 :
                    // InternalCQLParser.g:1921:3: kw= Solidus
                    {
                    kw=(Token)match(input,Solidus,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getMUL_OR_DIV_OPERATORAccess().getSolidusKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1927:3: kw= Asterisk
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
    // InternalCQLParser.g:1936:1: entryRuleADD_OPERATOR returns [String current=null] : iv_ruleADD_OPERATOR= ruleADD_OPERATOR EOF ;
    public final String entryRuleADD_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleADD_OPERATOR = null;


        try {
            // InternalCQLParser.g:1936:52: (iv_ruleADD_OPERATOR= ruleADD_OPERATOR EOF )
            // InternalCQLParser.g:1937:2: iv_ruleADD_OPERATOR= ruleADD_OPERATOR EOF
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
    // InternalCQLParser.g:1943:1: ruleADD_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= PlusSign ;
    public final AntlrDatatypeRuleToken ruleADD_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1949:2: (kw= PlusSign )
            // InternalCQLParser.g:1950:2: kw= PlusSign
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
    // InternalCQLParser.g:1958:1: entryRuleMINUS_OPERATOR returns [String current=null] : iv_ruleMINUS_OPERATOR= ruleMINUS_OPERATOR EOF ;
    public final String entryRuleMINUS_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleMINUS_OPERATOR = null;


        try {
            // InternalCQLParser.g:1958:54: (iv_ruleMINUS_OPERATOR= ruleMINUS_OPERATOR EOF )
            // InternalCQLParser.g:1959:2: iv_ruleMINUS_OPERATOR= ruleMINUS_OPERATOR EOF
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
    // InternalCQLParser.g:1965:1: ruleMINUS_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= HyphenMinus ;
    public final AntlrDatatypeRuleToken ruleMINUS_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1971:2: (kw= HyphenMinus )
            // InternalCQLParser.g:1972:2: kw= HyphenMinus
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
    // InternalCQLParser.g:1980:1: entryRuleSelectExpression returns [EObject current=null] : iv_ruleSelectExpression= ruleSelectExpression EOF ;
    public final EObject entryRuleSelectExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpression = null;


        try {
            // InternalCQLParser.g:1980:57: (iv_ruleSelectExpression= ruleSelectExpression EOF )
            // InternalCQLParser.g:1981:2: iv_ruleSelectExpression= ruleSelectExpression EOF
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
    // InternalCQLParser.g:1987:1: ruleSelectExpression returns [EObject current=null] : ( ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) ) ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) ;
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
            // InternalCQLParser.g:1993:2: ( ( ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) ) ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:1994:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) ) ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:1994:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) ) ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:1995:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) ) ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:1995:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) ) ) )
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==RULE_ID) ) {
                int LA37_1 = input.LA(2);

                if ( (LA37_1==LeftParenthesis) ) {
                    alt37=1;
                }
                else if ( ((LA37_1>=Asterisk && LA37_1<=PlusSign)||(LA37_1>=HyphenMinus && LA37_1<=Solidus)||LA37_1==CircumflexAccent) ) {
                    alt37=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 37, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA37_0==FALSE||LA37_0==TRUE||LA37_0==RULE_INT||LA37_0==RULE_FLOAT||(LA37_0>=RULE_VECTOR_FLOAT && LA37_0<=RULE_MATRIX_FLOAT)||LA37_0==RULE_STRING) ) {
                alt37=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 37, 0, input);

                throw nvae;
            }
            switch (alt37) {
                case 1 :
                    // InternalCQLParser.g:1996:4: ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* )
                    {
                    // InternalCQLParser.g:1996:4: ( ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )* )
                    // InternalCQLParser.g:1997:5: ( (lv_expressions_0_0= ruleExpressionComponent ) ) ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )*
                    {
                    // InternalCQLParser.g:1997:5: ( (lv_expressions_0_0= ruleExpressionComponent ) )
                    // InternalCQLParser.g:1998:6: (lv_expressions_0_0= ruleExpressionComponent )
                    {
                    // InternalCQLParser.g:1998:6: (lv_expressions_0_0= ruleExpressionComponent )
                    // InternalCQLParser.g:1999:7: lv_expressions_0_0= ruleExpressionComponent
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentParserRuleCall_0_0_0_0());
                    						
                    pushFollow(FOLLOW_32);
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

                    // InternalCQLParser.g:2016:5: ( ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) ) )*
                    loop35:
                    do {
                        int alt35=2;
                        int LA35_0 = input.LA(1);

                        if ( ((LA35_0>=Asterisk && LA35_0<=PlusSign)||LA35_0==HyphenMinus||LA35_0==Solidus||LA35_0==CircumflexAccent) ) {
                            alt35=1;
                        }


                        switch (alt35) {
                    	case 1 :
                    	    // InternalCQLParser.g:2017:6: ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) )
                    	    {
                    	    // InternalCQLParser.g:2017:6: ( (lv_operators_1_0= ruleARITHMETIC_OPERATOR ) )
                    	    // InternalCQLParser.g:2018:7: (lv_operators_1_0= ruleARITHMETIC_OPERATOR )
                    	    {
                    	    // InternalCQLParser.g:2018:7: (lv_operators_1_0= ruleARITHMETIC_OPERATOR )
                    	    // InternalCQLParser.g:2019:8: lv_operators_1_0= ruleARITHMETIC_OPERATOR
                    	    {

                    	    								newCompositeNode(grammarAccess.getSelectExpressionAccess().getOperatorsARITHMETIC_OPERATORParserRuleCall_0_0_1_0_0());
                    	    							
                    	    pushFollow(FOLLOW_10);
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

                    	    // InternalCQLParser.g:2036:6: ( ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) ) )
                    	    // InternalCQLParser.g:2037:7: ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) )
                    	    {
                    	    // InternalCQLParser.g:2037:7: ( (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute ) )
                    	    // InternalCQLParser.g:2038:8: (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute )
                    	    {
                    	    // InternalCQLParser.g:2038:8: (lv_expressions_2_1= ruleExpressionComponent | lv_expressions_2_2= ruleExpressionComponentAsAttribute )
                    	    int alt34=2;
                    	    int LA34_0 = input.LA(1);

                    	    if ( (LA34_0==RULE_ID) ) {
                    	        int LA34_1 = input.LA(2);

                    	        if ( (LA34_1==LeftParenthesis) ) {
                    	            alt34=1;
                    	        }
                    	        else if ( (LA34_1==EOF||LA34_1==FALSE||LA34_1==FROM||LA34_1==TRUE||LA34_1==AS||LA34_1==DollarSign||(LA34_1>=RightParenthesis && LA34_1<=Solidus)||LA34_1==CircumflexAccent||(LA34_1>=RULE_INT && LA34_1<=RULE_FLOAT)||(LA34_1>=RULE_VECTOR_FLOAT && LA34_1<=RULE_MATRIX_FLOAT)||LA34_1==RULE_STRING) ) {
                    	            alt34=2;
                    	        }
                    	        else {
                    	            NoViableAltException nvae =
                    	                new NoViableAltException("", 34, 1, input);

                    	            throw nvae;
                    	        }
                    	    }
                    	    else if ( (LA34_0==FALSE||LA34_0==TRUE||LA34_0==RULE_INT||LA34_0==RULE_FLOAT||(LA34_0>=RULE_VECTOR_FLOAT && LA34_0<=RULE_MATRIX_FLOAT)||LA34_0==RULE_STRING) ) {
                    	        alt34=1;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 34, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt34) {
                    	        case 1 :
                    	            // InternalCQLParser.g:2039:9: lv_expressions_2_1= ruleExpressionComponent
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentParserRuleCall_0_0_1_1_0_0());
                    	            								
                    	            pushFollow(FOLLOW_32);
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
                    	            // InternalCQLParser.g:2055:9: lv_expressions_2_2= ruleExpressionComponentAsAttribute
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentAsAttributeParserRuleCall_0_0_1_1_0_1());
                    	            								
                    	            pushFollow(FOLLOW_32);
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
                    	    break loop35;
                        }
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:2076:4: ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) ) )
                    {
                    // InternalCQLParser.g:2076:4: ( ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) ) )
                    // InternalCQLParser.g:2077:5: ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) ) ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) )
                    {
                    // InternalCQLParser.g:2077:5: ( (lv_expressions_3_0= ruleExpressionComponentAsAttribute ) )
                    // InternalCQLParser.g:2078:6: (lv_expressions_3_0= ruleExpressionComponentAsAttribute )
                    {
                    // InternalCQLParser.g:2078:6: (lv_expressions_3_0= ruleExpressionComponentAsAttribute )
                    // InternalCQLParser.g:2079:7: lv_expressions_3_0= ruleExpressionComponentAsAttribute
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentAsAttributeParserRuleCall_0_1_0_0());
                    						
                    pushFollow(FOLLOW_33);
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

                    // InternalCQLParser.g:2096:5: ( ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) ) )
                    // InternalCQLParser.g:2097:6: ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) ) ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) )
                    {
                    // InternalCQLParser.g:2097:6: ( (lv_operators_4_0= ruleARITHMETIC_OPERATOR ) )
                    // InternalCQLParser.g:2098:7: (lv_operators_4_0= ruleARITHMETIC_OPERATOR )
                    {
                    // InternalCQLParser.g:2098:7: (lv_operators_4_0= ruleARITHMETIC_OPERATOR )
                    // InternalCQLParser.g:2099:8: lv_operators_4_0= ruleARITHMETIC_OPERATOR
                    {

                    								newCompositeNode(grammarAccess.getSelectExpressionAccess().getOperatorsARITHMETIC_OPERATORParserRuleCall_0_1_1_0_0());
                    							
                    pushFollow(FOLLOW_10);
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

                    // InternalCQLParser.g:2116:6: ( ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) ) )
                    // InternalCQLParser.g:2117:7: ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) )
                    {
                    // InternalCQLParser.g:2117:7: ( (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute ) )
                    // InternalCQLParser.g:2118:8: (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute )
                    {
                    // InternalCQLParser.g:2118:8: (lv_expressions_5_1= ruleExpressionComponent | lv_expressions_5_2= ruleExpressionComponentAsAttribute )
                    int alt36=2;
                    int LA36_0 = input.LA(1);

                    if ( (LA36_0==RULE_ID) ) {
                        int LA36_1 = input.LA(2);

                        if ( (LA36_1==LeftParenthesis) ) {
                            alt36=1;
                        }
                        else if ( (LA36_1==EOF||LA36_1==FALSE||LA36_1==FROM||LA36_1==TRUE||LA36_1==AS||LA36_1==DollarSign||LA36_1==RightParenthesis||LA36_1==Comma||LA36_1==FullStop||(LA36_1>=RULE_INT && LA36_1<=RULE_FLOAT)||(LA36_1>=RULE_VECTOR_FLOAT && LA36_1<=RULE_MATRIX_FLOAT)||LA36_1==RULE_STRING) ) {
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
                            // InternalCQLParser.g:2119:9: lv_expressions_5_1= ruleExpressionComponent
                            {

                            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentParserRuleCall_0_1_1_1_0_0());
                            								
                            pushFollow(FOLLOW_27);
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
                            // InternalCQLParser.g:2135:9: lv_expressions_5_2= ruleExpressionComponentAsAttribute
                            {

                            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentAsAttributeParserRuleCall_0_1_1_1_0_1());
                            								
                            pushFollow(FOLLOW_27);
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

            // InternalCQLParser.g:2156:3: (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==AS) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // InternalCQLParser.g:2157:4: otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) )
                    {
                    otherlv_6=(Token)match(input,AS,FOLLOW_5); 

                    				newLeafNode(otherlv_6, grammarAccess.getSelectExpressionAccess().getASKeyword_1_0());
                    			
                    // InternalCQLParser.g:2161:4: ( (lv_alias_7_0= ruleAlias ) )
                    // InternalCQLParser.g:2162:5: (lv_alias_7_0= ruleAlias )
                    {
                    // InternalCQLParser.g:2162:5: (lv_alias_7_0= ruleAlias )
                    // InternalCQLParser.g:2163:6: lv_alias_7_0= ruleAlias
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
    // InternalCQLParser.g:2185:1: entryRuleSelectExpressionOnlyWithAttribute returns [EObject current=null] : iv_ruleSelectExpressionOnlyWithAttribute= ruleSelectExpressionOnlyWithAttribute EOF ;
    public final EObject entryRuleSelectExpressionOnlyWithAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionOnlyWithAttribute = null;


        try {
            // InternalCQLParser.g:2185:74: (iv_ruleSelectExpressionOnlyWithAttribute= ruleSelectExpressionOnlyWithAttribute EOF )
            // InternalCQLParser.g:2186:2: iv_ruleSelectExpressionOnlyWithAttribute= ruleSelectExpressionOnlyWithAttribute EOF
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
    // InternalCQLParser.g:2192:1: ruleSelectExpressionOnlyWithAttribute returns [EObject current=null] : ( (lv_expressions_0_0= ruleExpressionComponentAsAttribute ) ) ;
    public final EObject ruleSelectExpressionOnlyWithAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_expressions_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2198:2: ( ( (lv_expressions_0_0= ruleExpressionComponentAsAttribute ) ) )
            // InternalCQLParser.g:2199:2: ( (lv_expressions_0_0= ruleExpressionComponentAsAttribute ) )
            {
            // InternalCQLParser.g:2199:2: ( (lv_expressions_0_0= ruleExpressionComponentAsAttribute ) )
            // InternalCQLParser.g:2200:3: (lv_expressions_0_0= ruleExpressionComponentAsAttribute )
            {
            // InternalCQLParser.g:2200:3: (lv_expressions_0_0= ruleExpressionComponentAsAttribute )
            // InternalCQLParser.g:2201:4: lv_expressions_0_0= ruleExpressionComponentAsAttribute
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
    // InternalCQLParser.g:2221:1: entryRuleFunction returns [EObject current=null] : iv_ruleFunction= ruleFunction EOF ;
    public final EObject entryRuleFunction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFunction = null;


        try {
            // InternalCQLParser.g:2221:49: (iv_ruleFunction= ruleFunction EOF )
            // InternalCQLParser.g:2222:2: iv_ruleFunction= ruleFunction EOF
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
    // InternalCQLParser.g:2228:1: ruleFunction returns [EObject current=null] : ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression ) ) ) otherlv_4= RightParenthesis ) ;
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
            // InternalCQLParser.g:2234:2: ( ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression ) ) ) otherlv_4= RightParenthesis ) )
            // InternalCQLParser.g:2235:2: ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression ) ) ) otherlv_4= RightParenthesis )
            {
            // InternalCQLParser.g:2235:2: ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression ) ) ) otherlv_4= RightParenthesis )
            // InternalCQLParser.g:2236:3: () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression ) ) ) otherlv_4= RightParenthesis
            {
            // InternalCQLParser.g:2236:3: ()
            // InternalCQLParser.g:2237:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getFunctionAccess().getFunctionAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:2243:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQLParser.g:2244:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQLParser.g:2244:4: (lv_name_1_0= RULE_ID )
            // InternalCQLParser.g:2245:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_34); 

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

            otherlv_2=(Token)match(input,LeftParenthesis,FOLLOW_10); 

            			newLeafNode(otherlv_2, grammarAccess.getFunctionAccess().getLeftParenthesisKeyword_2());
            		
            // InternalCQLParser.g:2265:3: ( ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression ) ) )
            // InternalCQLParser.g:2266:4: ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression ) )
            {
            // InternalCQLParser.g:2266:4: ( (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression ) )
            // InternalCQLParser.g:2267:5: (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression )
            {
            // InternalCQLParser.g:2267:5: (lv_value_3_1= ruleSelectExpression | lv_value_3_2= ruleSelectExpressionOnlyWithAttribute | lv_value_3_3= ruleStarExpression )
            int alt39=3;
            switch ( input.LA(1) ) {
            case RULE_ID:
                {
                switch ( input.LA(2) ) {
                case LeftParenthesis:
                case Asterisk:
                case PlusSign:
                case HyphenMinus:
                case Solidus:
                case CircumflexAccent:
                    {
                    alt39=1;
                    }
                    break;
                case FullStop:
                    {
                    int LA39_4 = input.LA(3);

                    if ( (LA39_4==RULE_ID) ) {
                        int LA39_6 = input.LA(4);

                        if ( (LA39_6==RightParenthesis) ) {
                            alt39=2;
                        }
                        else if ( ((LA39_6>=Asterisk && LA39_6<=PlusSign)||LA39_6==HyphenMinus||LA39_6==Solidus||LA39_6==CircumflexAccent) ) {
                            alt39=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 39, 6, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 39, 4, input);

                        throw nvae;
                    }
                    }
                    break;
                case RightParenthesis:
                    {
                    alt39=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 1, input);

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
                alt39=1;
                }
                break;
            case Asterisk:
                {
                alt39=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;
            }

            switch (alt39) {
                case 1 :
                    // InternalCQLParser.g:2268:6: lv_value_3_1= ruleSelectExpression
                    {

                    						newCompositeNode(grammarAccess.getFunctionAccess().getValueSelectExpressionParserRuleCall_3_0_0());
                    					
                    pushFollow(FOLLOW_25);
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
                    // InternalCQLParser.g:2284:6: lv_value_3_2= ruleSelectExpressionOnlyWithAttribute
                    {

                    						newCompositeNode(grammarAccess.getFunctionAccess().getValueSelectExpressionOnlyWithAttributeParserRuleCall_3_0_1());
                    					
                    pushFollow(FOLLOW_25);
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
                    // InternalCQLParser.g:2300:6: lv_value_3_3= ruleStarExpression
                    {

                    						newCompositeNode(grammarAccess.getFunctionAccess().getValueStarExpressionParserRuleCall_3_0_2());
                    					
                    pushFollow(FOLLOW_25);
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
    // InternalCQLParser.g:2326:1: entryRuleExpressionComponent returns [EObject current=null] : iv_ruleExpressionComponent= ruleExpressionComponent EOF ;
    public final EObject entryRuleExpressionComponent() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponent = null;


        try {
            // InternalCQLParser.g:2326:60: (iv_ruleExpressionComponent= ruleExpressionComponent EOF )
            // InternalCQLParser.g:2327:2: iv_ruleExpressionComponent= ruleExpressionComponent EOF
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
    // InternalCQLParser.g:2333:1: ruleExpressionComponent returns [EObject current=null] : ( ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) ) ) ;
    public final EObject ruleExpressionComponent() throws RecognitionException {
        EObject current = null;

        EObject lv_value_0_1 = null;

        EObject lv_value_0_2 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2339:2: ( ( ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) ) ) )
            // InternalCQLParser.g:2340:2: ( ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) ) )
            {
            // InternalCQLParser.g:2340:2: ( ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) ) )
            // InternalCQLParser.g:2341:3: ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) )
            {
            // InternalCQLParser.g:2341:3: ( (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef ) )
            // InternalCQLParser.g:2342:4: (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef )
            {
            // InternalCQLParser.g:2342:4: (lv_value_0_1= ruleFunction | lv_value_0_2= ruleAtomicWithoutAttributeRef )
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==RULE_ID) ) {
                alt40=1;
            }
            else if ( (LA40_0==FALSE||LA40_0==TRUE||LA40_0==RULE_INT||LA40_0==RULE_FLOAT||(LA40_0>=RULE_VECTOR_FLOAT && LA40_0<=RULE_MATRIX_FLOAT)||LA40_0==RULE_STRING) ) {
                alt40=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 40, 0, input);

                throw nvae;
            }
            switch (alt40) {
                case 1 :
                    // InternalCQLParser.g:2343:5: lv_value_0_1= ruleFunction
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
                    // InternalCQLParser.g:2359:5: lv_value_0_2= ruleAtomicWithoutAttributeRef
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
    // InternalCQLParser.g:2380:1: entryRuleStarExpression returns [EObject current=null] : iv_ruleStarExpression= ruleStarExpression EOF ;
    public final EObject entryRuleStarExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStarExpression = null;


        try {
            // InternalCQLParser.g:2380:55: (iv_ruleStarExpression= ruleStarExpression EOF )
            // InternalCQLParser.g:2381:2: iv_ruleStarExpression= ruleStarExpression EOF
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
    // InternalCQLParser.g:2387:1: ruleStarExpression returns [EObject current=null] : ( () ( (lv_expressions_1_0= ruleStar ) ) ) ;
    public final EObject ruleStarExpression() throws RecognitionException {
        EObject current = null;

        EObject lv_expressions_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2393:2: ( ( () ( (lv_expressions_1_0= ruleStar ) ) ) )
            // InternalCQLParser.g:2394:2: ( () ( (lv_expressions_1_0= ruleStar ) ) )
            {
            // InternalCQLParser.g:2394:2: ( () ( (lv_expressions_1_0= ruleStar ) ) )
            // InternalCQLParser.g:2395:3: () ( (lv_expressions_1_0= ruleStar ) )
            {
            // InternalCQLParser.g:2395:3: ()
            // InternalCQLParser.g:2396:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getStarExpressionAccess().getStarExpressionAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:2402:3: ( (lv_expressions_1_0= ruleStar ) )
            // InternalCQLParser.g:2403:4: (lv_expressions_1_0= ruleStar )
            {
            // InternalCQLParser.g:2403:4: (lv_expressions_1_0= ruleStar )
            // InternalCQLParser.g:2404:5: lv_expressions_1_0= ruleStar
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
    // InternalCQLParser.g:2425:1: entryRuleStar returns [EObject current=null] : iv_ruleStar= ruleStar EOF ;
    public final EObject entryRuleStar() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStar = null;


        try {
            // InternalCQLParser.g:2425:45: (iv_ruleStar= ruleStar EOF )
            // InternalCQLParser.g:2426:2: iv_ruleStar= ruleStar EOF
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
    // InternalCQLParser.g:2432:1: ruleStar returns [EObject current=null] : ( () ( (lv_value_1_0= ruleStarthing ) ) ) ;
    public final EObject ruleStar() throws RecognitionException {
        EObject current = null;

        EObject lv_value_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2438:2: ( ( () ( (lv_value_1_0= ruleStarthing ) ) ) )
            // InternalCQLParser.g:2439:2: ( () ( (lv_value_1_0= ruleStarthing ) ) )
            {
            // InternalCQLParser.g:2439:2: ( () ( (lv_value_1_0= ruleStarthing ) ) )
            // InternalCQLParser.g:2440:3: () ( (lv_value_1_0= ruleStarthing ) )
            {
            // InternalCQLParser.g:2440:3: ()
            // InternalCQLParser.g:2441:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getStarAccess().getStarAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:2447:3: ( (lv_value_1_0= ruleStarthing ) )
            // InternalCQLParser.g:2448:4: (lv_value_1_0= ruleStarthing )
            {
            // InternalCQLParser.g:2448:4: (lv_value_1_0= ruleStarthing )
            // InternalCQLParser.g:2449:5: lv_value_1_0= ruleStarthing
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
    // InternalCQLParser.g:2470:1: entryRuleStarthing returns [EObject current=null] : iv_ruleStarthing= ruleStarthing EOF ;
    public final EObject entryRuleStarthing() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStarthing = null;


        try {
            // InternalCQLParser.g:2470:50: (iv_ruleStarthing= ruleStarthing EOF )
            // InternalCQLParser.g:2471:2: iv_ruleStarthing= ruleStarthing EOF
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
    // InternalCQLParser.g:2477:1: ruleStarthing returns [EObject current=null] : ( () otherlv_1= Asterisk ) ;
    public final EObject ruleStarthing() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2483:2: ( ( () otherlv_1= Asterisk ) )
            // InternalCQLParser.g:2484:2: ( () otherlv_1= Asterisk )
            {
            // InternalCQLParser.g:2484:2: ( () otherlv_1= Asterisk )
            // InternalCQLParser.g:2485:3: () otherlv_1= Asterisk
            {
            // InternalCQLParser.g:2485:3: ()
            // InternalCQLParser.g:2486:4: 
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
    // InternalCQLParser.g:2500:1: entryRuleExpressionComponentAsAttribute returns [EObject current=null] : iv_ruleExpressionComponentAsAttribute= ruleExpressionComponentAsAttribute EOF ;
    public final EObject entryRuleExpressionComponentAsAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentAsAttribute = null;


        try {
            // InternalCQLParser.g:2500:71: (iv_ruleExpressionComponentAsAttribute= ruleExpressionComponentAsAttribute EOF )
            // InternalCQLParser.g:2501:2: iv_ruleExpressionComponentAsAttribute= ruleExpressionComponentAsAttribute EOF
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
    // InternalCQLParser.g:2507:1: ruleExpressionComponentAsAttribute returns [EObject current=null] : ( () ( (lv_value_1_0= ruleAttributeForSelectExpression ) ) ) ;
    public final EObject ruleExpressionComponentAsAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_value_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2513:2: ( ( () ( (lv_value_1_0= ruleAttributeForSelectExpression ) ) ) )
            // InternalCQLParser.g:2514:2: ( () ( (lv_value_1_0= ruleAttributeForSelectExpression ) ) )
            {
            // InternalCQLParser.g:2514:2: ( () ( (lv_value_1_0= ruleAttributeForSelectExpression ) ) )
            // InternalCQLParser.g:2515:3: () ( (lv_value_1_0= ruleAttributeForSelectExpression ) )
            {
            // InternalCQLParser.g:2515:3: ()
            // InternalCQLParser.g:2516:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionComponentAsAttributeAccess().getExpressionComponentAsAttributeAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:2522:3: ( (lv_value_1_0= ruleAttributeForSelectExpression ) )
            // InternalCQLParser.g:2523:4: (lv_value_1_0= ruleAttributeForSelectExpression )
            {
            // InternalCQLParser.g:2523:4: (lv_value_1_0= ruleAttributeForSelectExpression )
            // InternalCQLParser.g:2524:5: lv_value_1_0= ruleAttributeForSelectExpression
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
    // InternalCQLParser.g:2545:1: entryRuleAlias returns [EObject current=null] : iv_ruleAlias= ruleAlias EOF ;
    public final EObject entryRuleAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAlias = null;


        try {
            // InternalCQLParser.g:2545:46: (iv_ruleAlias= ruleAlias EOF )
            // InternalCQLParser.g:2546:2: iv_ruleAlias= ruleAlias EOF
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
    // InternalCQLParser.g:2552:1: ruleAlias returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2558:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQLParser.g:2559:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQLParser.g:2559:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:2560:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:2560:3: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:2561:4: lv_name_0_0= RULE_ID
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
    // InternalCQLParser.g:2580:1: entryRuleAccessFramework returns [EObject current=null] : iv_ruleAccessFramework= ruleAccessFramework EOF ;
    public final EObject entryRuleAccessFramework() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAccessFramework = null;


        try {
            // InternalCQLParser.g:2580:56: (iv_ruleAccessFramework= ruleAccessFramework EOF )
            // InternalCQLParser.g:2581:2: iv_ruleAccessFramework= ruleAccessFramework EOF
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
    // InternalCQLParser.g:2587:1: ruleAccessFramework returns [EObject current=null] : (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) ) ) )* otherlv_15= RightParenthesis ) ;
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
            // InternalCQLParser.g:2593:2: ( (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) ) ) )* otherlv_15= RightParenthesis ) )
            // InternalCQLParser.g:2594:2: (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) ) ) )* otherlv_15= RightParenthesis )
            {
            // InternalCQLParser.g:2594:2: (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) ) ) )* otherlv_15= RightParenthesis )
            // InternalCQLParser.g:2595:3: otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) ) ) )* otherlv_15= RightParenthesis
            {
            otherlv_0=(Token)match(input,WRAPPER,FOLLOW_35); 

            			newLeafNode(otherlv_0, grammarAccess.getAccessFrameworkAccess().getWRAPPERKeyword_0());
            		
            // InternalCQLParser.g:2599:3: ( (lv_wrapper_1_0= RULE_STRING ) )
            // InternalCQLParser.g:2600:4: (lv_wrapper_1_0= RULE_STRING )
            {
            // InternalCQLParser.g:2600:4: (lv_wrapper_1_0= RULE_STRING )
            // InternalCQLParser.g:2601:5: lv_wrapper_1_0= RULE_STRING
            {
            lv_wrapper_1_0=(Token)match(input,RULE_STRING,FOLLOW_36); 

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

            otherlv_2=(Token)match(input,PROTOCOL,FOLLOW_35); 

            			newLeafNode(otherlv_2, grammarAccess.getAccessFrameworkAccess().getPROTOCOLKeyword_2());
            		
            // InternalCQLParser.g:2621:3: ( (lv_protocol_3_0= RULE_STRING ) )
            // InternalCQLParser.g:2622:4: (lv_protocol_3_0= RULE_STRING )
            {
            // InternalCQLParser.g:2622:4: (lv_protocol_3_0= RULE_STRING )
            // InternalCQLParser.g:2623:5: lv_protocol_3_0= RULE_STRING
            {
            lv_protocol_3_0=(Token)match(input,RULE_STRING,FOLLOW_37); 

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

            otherlv_4=(Token)match(input,TRANSPORT,FOLLOW_35); 

            			newLeafNode(otherlv_4, grammarAccess.getAccessFrameworkAccess().getTRANSPORTKeyword_4());
            		
            // InternalCQLParser.g:2643:3: ( (lv_transport_5_0= RULE_STRING ) )
            // InternalCQLParser.g:2644:4: (lv_transport_5_0= RULE_STRING )
            {
            // InternalCQLParser.g:2644:4: (lv_transport_5_0= RULE_STRING )
            // InternalCQLParser.g:2645:5: lv_transport_5_0= RULE_STRING
            {
            lv_transport_5_0=(Token)match(input,RULE_STRING,FOLLOW_38); 

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

            otherlv_6=(Token)match(input,DATAHANDLER,FOLLOW_35); 

            			newLeafNode(otherlv_6, grammarAccess.getAccessFrameworkAccess().getDATAHANDLERKeyword_6());
            		
            // InternalCQLParser.g:2665:3: ( (lv_datahandler_7_0= RULE_STRING ) )
            // InternalCQLParser.g:2666:4: (lv_datahandler_7_0= RULE_STRING )
            {
            // InternalCQLParser.g:2666:4: (lv_datahandler_7_0= RULE_STRING )
            // InternalCQLParser.g:2667:5: lv_datahandler_7_0= RULE_STRING
            {
            lv_datahandler_7_0=(Token)match(input,RULE_STRING,FOLLOW_39); 

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

            otherlv_8=(Token)match(input,OPTIONS,FOLLOW_34); 

            			newLeafNode(otherlv_8, grammarAccess.getAccessFrameworkAccess().getOPTIONSKeyword_8());
            		
            otherlv_9=(Token)match(input,LeftParenthesis,FOLLOW_35); 

            			newLeafNode(otherlv_9, grammarAccess.getAccessFrameworkAccess().getLeftParenthesisKeyword_9());
            		
            // InternalCQLParser.g:2691:3: ( ( (lv_keys_10_0= RULE_STRING ) ) ( ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) ) ) )+
            int cnt42=0;
            loop42:
            do {
                int alt42=2;
                int LA42_0 = input.LA(1);

                if ( (LA42_0==RULE_STRING) ) {
                    alt42=1;
                }


                switch (alt42) {
            	case 1 :
            	    // InternalCQLParser.g:2692:4: ( (lv_keys_10_0= RULE_STRING ) ) ( ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) ) )
            	    {
            	    // InternalCQLParser.g:2692:4: ( (lv_keys_10_0= RULE_STRING ) )
            	    // InternalCQLParser.g:2693:5: (lv_keys_10_0= RULE_STRING )
            	    {
            	    // InternalCQLParser.g:2693:5: (lv_keys_10_0= RULE_STRING )
            	    // InternalCQLParser.g:2694:6: lv_keys_10_0= RULE_STRING
            	    {
            	    lv_keys_10_0=(Token)match(input,RULE_STRING,FOLLOW_40); 

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

            	    // InternalCQLParser.g:2710:4: ( ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) ) )
            	    // InternalCQLParser.g:2711:5: ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) )
            	    {
            	    // InternalCQLParser.g:2711:5: ( (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH ) )
            	    // InternalCQLParser.g:2712:6: (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH )
            	    {
            	    // InternalCQLParser.g:2712:6: (lv_values_11_1= RULE_STRING | lv_values_11_2= RULE_PATH )
            	    int alt41=2;
            	    int LA41_0 = input.LA(1);

            	    if ( (LA41_0==RULE_STRING) ) {
            	        alt41=1;
            	    }
            	    else if ( (LA41_0==RULE_PATH) ) {
            	        alt41=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 41, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt41) {
            	        case 1 :
            	            // InternalCQLParser.g:2713:7: lv_values_11_1= RULE_STRING
            	            {
            	            lv_values_11_1=(Token)match(input,RULE_STRING,FOLLOW_41); 

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
            	            // InternalCQLParser.g:2728:7: lv_values_11_2= RULE_PATH
            	            {
            	            lv_values_11_2=(Token)match(input,RULE_PATH,FOLLOW_41); 

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
            	    if ( cnt42 >= 1 ) break loop42;
                        EarlyExitException eee =
                            new EarlyExitException(42, input);
                        throw eee;
                }
                cnt42++;
            } while (true);

            // InternalCQLParser.g:2746:3: (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) ) ) )*
            loop44:
            do {
                int alt44=2;
                int LA44_0 = input.LA(1);

                if ( (LA44_0==Comma) ) {
                    alt44=1;
                }


                switch (alt44) {
            	case 1 :
            	    // InternalCQLParser.g:2747:4: otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) ) )
            	    {
            	    otherlv_12=(Token)match(input,Comma,FOLLOW_35); 

            	    				newLeafNode(otherlv_12, grammarAccess.getAccessFrameworkAccess().getCommaKeyword_11_0());
            	    			
            	    // InternalCQLParser.g:2751:4: ( (lv_keys_13_0= RULE_STRING ) )
            	    // InternalCQLParser.g:2752:5: (lv_keys_13_0= RULE_STRING )
            	    {
            	    // InternalCQLParser.g:2752:5: (lv_keys_13_0= RULE_STRING )
            	    // InternalCQLParser.g:2753:6: lv_keys_13_0= RULE_STRING
            	    {
            	    lv_keys_13_0=(Token)match(input,RULE_STRING,FOLLOW_40); 

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

            	    // InternalCQLParser.g:2769:4: ( ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) ) )
            	    // InternalCQLParser.g:2770:5: ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) )
            	    {
            	    // InternalCQLParser.g:2770:5: ( (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH ) )
            	    // InternalCQLParser.g:2771:6: (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH )
            	    {
            	    // InternalCQLParser.g:2771:6: (lv_values_14_1= RULE_STRING | lv_values_14_2= RULE_PATH )
            	    int alt43=2;
            	    int LA43_0 = input.LA(1);

            	    if ( (LA43_0==RULE_STRING) ) {
            	        alt43=1;
            	    }
            	    else if ( (LA43_0==RULE_PATH) ) {
            	        alt43=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 43, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt43) {
            	        case 1 :
            	            // InternalCQLParser.g:2772:7: lv_values_14_1= RULE_STRING
            	            {
            	            lv_values_14_1=(Token)match(input,RULE_STRING,FOLLOW_42); 

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
            	            // InternalCQLParser.g:2787:7: lv_values_14_2= RULE_PATH
            	            {
            	            lv_values_14_2=(Token)match(input,RULE_PATH,FOLLOW_42); 

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
            	    break loop44;
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
    // InternalCQLParser.g:2813:1: entryRuleSchemaDefinition returns [EObject current=null] : iv_ruleSchemaDefinition= ruleSchemaDefinition EOF ;
    public final EObject entryRuleSchemaDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSchemaDefinition = null;


        try {
            // InternalCQLParser.g:2813:57: (iv_ruleSchemaDefinition= ruleSchemaDefinition EOF )
            // InternalCQLParser.g:2814:2: iv_ruleSchemaDefinition= ruleSchemaDefinition EOF
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
    // InternalCQLParser.g:2820:1: ruleSchemaDefinition returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis ) ;
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
            // InternalCQLParser.g:2826:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis ) )
            // InternalCQLParser.g:2827:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis )
            {
            // InternalCQLParser.g:2827:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis )
            // InternalCQLParser.g:2828:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis
            {
            // InternalCQLParser.g:2828:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:2829:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:2829:4: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:2830:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_34); 

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
            		
            // InternalCQLParser.g:2850:3: ( (lv_arguments_2_0= RULE_ID ) )
            // InternalCQLParser.g:2851:4: (lv_arguments_2_0= RULE_ID )
            {
            // InternalCQLParser.g:2851:4: (lv_arguments_2_0= RULE_ID )
            // InternalCQLParser.g:2852:5: lv_arguments_2_0= RULE_ID
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

            // InternalCQLParser.g:2868:3: ( (lv_arguments_3_0= RULE_ID ) )
            // InternalCQLParser.g:2869:4: (lv_arguments_3_0= RULE_ID )
            {
            // InternalCQLParser.g:2869:4: (lv_arguments_3_0= RULE_ID )
            // InternalCQLParser.g:2870:5: lv_arguments_3_0= RULE_ID
            {
            lv_arguments_3_0=(Token)match(input,RULE_ID,FOLLOW_42); 

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

            // InternalCQLParser.g:2886:3: (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )*
            loop45:
            do {
                int alt45=2;
                int LA45_0 = input.LA(1);

                if ( (LA45_0==Comma) ) {
                    alt45=1;
                }


                switch (alt45) {
            	case 1 :
            	    // InternalCQLParser.g:2887:4: otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) )
            	    {
            	    otherlv_4=(Token)match(input,Comma,FOLLOW_5); 

            	    				newLeafNode(otherlv_4, grammarAccess.getSchemaDefinitionAccess().getCommaKeyword_4_0());
            	    			
            	    // InternalCQLParser.g:2891:4: ( (lv_arguments_5_0= RULE_ID ) )
            	    // InternalCQLParser.g:2892:5: (lv_arguments_5_0= RULE_ID )
            	    {
            	    // InternalCQLParser.g:2892:5: (lv_arguments_5_0= RULE_ID )
            	    // InternalCQLParser.g:2893:6: lv_arguments_5_0= RULE_ID
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

            	    // InternalCQLParser.g:2909:4: ( (lv_arguments_6_0= RULE_ID ) )
            	    // InternalCQLParser.g:2910:5: (lv_arguments_6_0= RULE_ID )
            	    {
            	    // InternalCQLParser.g:2910:5: (lv_arguments_6_0= RULE_ID )
            	    // InternalCQLParser.g:2911:6: lv_arguments_6_0= RULE_ID
            	    {
            	    lv_arguments_6_0=(Token)match(input,RULE_ID,FOLLOW_42); 

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
            	    break loop45;
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
    // InternalCQLParser.g:2936:1: entryRuleCreate returns [EObject current=null] : iv_ruleCreate= ruleCreate EOF ;
    public final EObject entryRuleCreate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreate = null;


        try {
            // InternalCQLParser.g:2936:47: (iv_ruleCreate= ruleCreate EOF )
            // InternalCQLParser.g:2937:2: iv_ruleCreate= ruleCreate EOF
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
    // InternalCQLParser.g:2943:1: ruleCreate returns [EObject current=null] : ( () (otherlv_1= CREATE | otherlv_2= ATTACH ) ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) ) ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) ) ) ;
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
            // InternalCQLParser.g:2949:2: ( ( () (otherlv_1= CREATE | otherlv_2= ATTACH ) ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) ) ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) ) ) )
            // InternalCQLParser.g:2950:2: ( () (otherlv_1= CREATE | otherlv_2= ATTACH ) ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) ) ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) ) )
            {
            // InternalCQLParser.g:2950:2: ( () (otherlv_1= CREATE | otherlv_2= ATTACH ) ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) ) ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) ) )
            // InternalCQLParser.g:2951:3: () (otherlv_1= CREATE | otherlv_2= ATTACH ) ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) ) ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) )
            {
            // InternalCQLParser.g:2951:3: ()
            // InternalCQLParser.g:2952:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getCreateAccess().getCreateAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:2958:3: (otherlv_1= CREATE | otherlv_2= ATTACH )
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==CREATE) ) {
                alt46=1;
            }
            else if ( (LA46_0==ATTACH) ) {
                alt46=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 46, 0, input);

                throw nvae;
            }
            switch (alt46) {
                case 1 :
                    // InternalCQLParser.g:2959:4: otherlv_1= CREATE
                    {
                    otherlv_1=(Token)match(input,CREATE,FOLLOW_43); 

                    				newLeafNode(otherlv_1, grammarAccess.getCreateAccess().getCREATEKeyword_1_0());
                    			

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:2964:4: otherlv_2= ATTACH
                    {
                    otherlv_2=(Token)match(input,ATTACH,FOLLOW_43); 

                    				newLeafNode(otherlv_2, grammarAccess.getCreateAccess().getATTACHKeyword_1_1());
                    			

                    }
                    break;

            }

            // InternalCQLParser.g:2969:3: ( ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) ) )
            // InternalCQLParser.g:2970:4: ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) )
            {
            // InternalCQLParser.g:2970:4: ( (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW ) )
            // InternalCQLParser.g:2971:5: (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW )
            {
            // InternalCQLParser.g:2971:5: (lv_type_3_1= STREAM | lv_type_3_2= SINK | lv_type_3_3= VIEW )
            int alt47=3;
            switch ( input.LA(1) ) {
            case STREAM:
                {
                alt47=1;
                }
                break;
            case SINK:
                {
                alt47=2;
                }
                break;
            case VIEW:
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
                    // InternalCQLParser.g:2972:6: lv_type_3_1= STREAM
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
                    // InternalCQLParser.g:2983:6: lv_type_3_2= SINK
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
                    // InternalCQLParser.g:2994:6: lv_type_3_3= VIEW
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

            // InternalCQLParser.g:3007:3: ( ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) ) )
            // InternalCQLParser.g:3008:4: ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) )
            {
            // InternalCQLParser.g:3008:4: ( (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView ) )
            // InternalCQLParser.g:3009:5: (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView )
            {
            // InternalCQLParser.g:3009:5: (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView )
            int alt48=6;
            alt48 = dfa48.predict(input);
            switch (alt48) {
                case 1 :
                    // InternalCQLParser.g:3010:6: lv_create_4_1= ruleCreateAccessFramework
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
                    // InternalCQLParser.g:3026:6: lv_create_4_2= ruleCreateChannelFrameworkViaPort
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
                    // InternalCQLParser.g:3042:6: lv_create_4_3= ruleCreateChannelFormatViaFile
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
                    // InternalCQLParser.g:3058:6: lv_create_4_4= ruleCreateDatabaseStream
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
                    // InternalCQLParser.g:3074:6: lv_create_4_5= ruleCreateDatabaseSink
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
                    // InternalCQLParser.g:3090:6: lv_create_4_6= ruleCreateView
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
    // InternalCQLParser.g:3112:1: entryRuleCreateAccessFramework returns [EObject current=null] : iv_ruleCreateAccessFramework= ruleCreateAccessFramework EOF ;
    public final EObject entryRuleCreateAccessFramework() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateAccessFramework = null;


        try {
            // InternalCQLParser.g:3112:62: (iv_ruleCreateAccessFramework= ruleCreateAccessFramework EOF )
            // InternalCQLParser.g:3113:2: iv_ruleCreateAccessFramework= ruleCreateAccessFramework EOF
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
    // InternalCQLParser.g:3119:1: ruleCreateAccessFramework returns [EObject current=null] : ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) ( (lv_pars_1_0= ruleAccessFramework ) ) ) ;
    public final EObject ruleCreateAccessFramework() throws RecognitionException {
        EObject current = null;

        EObject lv_attributes_0_0 = null;

        EObject lv_pars_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3125:2: ( ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) ( (lv_pars_1_0= ruleAccessFramework ) ) ) )
            // InternalCQLParser.g:3126:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) ( (lv_pars_1_0= ruleAccessFramework ) ) )
            {
            // InternalCQLParser.g:3126:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) ( (lv_pars_1_0= ruleAccessFramework ) ) )
            // InternalCQLParser.g:3127:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) ) ( (lv_pars_1_0= ruleAccessFramework ) )
            {
            // InternalCQLParser.g:3127:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) )
            // InternalCQLParser.g:3128:4: (lv_attributes_0_0= ruleSchemaDefinition )
            {
            // InternalCQLParser.g:3128:4: (lv_attributes_0_0= ruleSchemaDefinition )
            // InternalCQLParser.g:3129:5: lv_attributes_0_0= ruleSchemaDefinition
            {

            					newCompositeNode(grammarAccess.getCreateAccessFrameworkAccess().getAttributesSchemaDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_44);
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

            // InternalCQLParser.g:3146:3: ( (lv_pars_1_0= ruleAccessFramework ) )
            // InternalCQLParser.g:3147:4: (lv_pars_1_0= ruleAccessFramework )
            {
            // InternalCQLParser.g:3147:4: (lv_pars_1_0= ruleAccessFramework )
            // InternalCQLParser.g:3148:5: lv_pars_1_0= ruleAccessFramework
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
    // InternalCQLParser.g:3169:1: entryRuleCreateChannelFrameworkViaPort returns [EObject current=null] : iv_ruleCreateChannelFrameworkViaPort= ruleCreateChannelFrameworkViaPort EOF ;
    public final EObject entryRuleCreateChannelFrameworkViaPort() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateChannelFrameworkViaPort = null;


        try {
            // InternalCQLParser.g:3169:70: (iv_ruleCreateChannelFrameworkViaPort= ruleCreateChannelFrameworkViaPort EOF )
            // InternalCQLParser.g:3170:2: iv_ruleCreateChannelFrameworkViaPort= ruleCreateChannelFrameworkViaPort EOF
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
    // InternalCQLParser.g:3176:1: ruleCreateChannelFrameworkViaPort returns [EObject current=null] : ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= CHANNEL ( (lv_host_2_0= RULE_ID ) ) otherlv_3= Colon ( (lv_port_4_0= RULE_INT ) ) ) ;
    public final EObject ruleCreateChannelFrameworkViaPort() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_host_2_0=null;
        Token otherlv_3=null;
        Token lv_port_4_0=null;
        EObject lv_attributes_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3182:2: ( ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= CHANNEL ( (lv_host_2_0= RULE_ID ) ) otherlv_3= Colon ( (lv_port_4_0= RULE_INT ) ) ) )
            // InternalCQLParser.g:3183:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= CHANNEL ( (lv_host_2_0= RULE_ID ) ) otherlv_3= Colon ( (lv_port_4_0= RULE_INT ) ) )
            {
            // InternalCQLParser.g:3183:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= CHANNEL ( (lv_host_2_0= RULE_ID ) ) otherlv_3= Colon ( (lv_port_4_0= RULE_INT ) ) )
            // InternalCQLParser.g:3184:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= CHANNEL ( (lv_host_2_0= RULE_ID ) ) otherlv_3= Colon ( (lv_port_4_0= RULE_INT ) )
            {
            // InternalCQLParser.g:3184:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) )
            // InternalCQLParser.g:3185:4: (lv_attributes_0_0= ruleSchemaDefinition )
            {
            // InternalCQLParser.g:3185:4: (lv_attributes_0_0= ruleSchemaDefinition )
            // InternalCQLParser.g:3186:5: lv_attributes_0_0= ruleSchemaDefinition
            {

            					newCompositeNode(grammarAccess.getCreateChannelFrameworkViaPortAccess().getAttributesSchemaDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_45);
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
            		
            // InternalCQLParser.g:3207:3: ( (lv_host_2_0= RULE_ID ) )
            // InternalCQLParser.g:3208:4: (lv_host_2_0= RULE_ID )
            {
            // InternalCQLParser.g:3208:4: (lv_host_2_0= RULE_ID )
            // InternalCQLParser.g:3209:5: lv_host_2_0= RULE_ID
            {
            lv_host_2_0=(Token)match(input,RULE_ID,FOLLOW_46); 

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

            otherlv_3=(Token)match(input,Colon,FOLLOW_47); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateChannelFrameworkViaPortAccess().getColonKeyword_3());
            		
            // InternalCQLParser.g:3229:3: ( (lv_port_4_0= RULE_INT ) )
            // InternalCQLParser.g:3230:4: (lv_port_4_0= RULE_INT )
            {
            // InternalCQLParser.g:3230:4: (lv_port_4_0= RULE_INT )
            // InternalCQLParser.g:3231:5: lv_port_4_0= RULE_INT
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
    // InternalCQLParser.g:3251:1: entryRuleCreateChannelFormatViaFile returns [EObject current=null] : iv_ruleCreateChannelFormatViaFile= ruleCreateChannelFormatViaFile EOF ;
    public final EObject entryRuleCreateChannelFormatViaFile() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateChannelFormatViaFile = null;


        try {
            // InternalCQLParser.g:3251:67: (iv_ruleCreateChannelFormatViaFile= ruleCreateChannelFormatViaFile EOF )
            // InternalCQLParser.g:3252:2: iv_ruleCreateChannelFormatViaFile= ruleCreateChannelFormatViaFile EOF
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
    // InternalCQLParser.g:3258:1: ruleCreateChannelFormatViaFile returns [EObject current=null] : ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= FILE ( (lv_filename_2_0= RULE_STRING ) ) otherlv_3= AS ( (lv_type_4_0= RULE_ID ) ) ) ;
    public final EObject ruleCreateChannelFormatViaFile() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_filename_2_0=null;
        Token otherlv_3=null;
        Token lv_type_4_0=null;
        EObject lv_attributes_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3264:2: ( ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= FILE ( (lv_filename_2_0= RULE_STRING ) ) otherlv_3= AS ( (lv_type_4_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:3265:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= FILE ( (lv_filename_2_0= RULE_STRING ) ) otherlv_3= AS ( (lv_type_4_0= RULE_ID ) ) )
            {
            // InternalCQLParser.g:3265:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= FILE ( (lv_filename_2_0= RULE_STRING ) ) otherlv_3= AS ( (lv_type_4_0= RULE_ID ) ) )
            // InternalCQLParser.g:3266:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= FILE ( (lv_filename_2_0= RULE_STRING ) ) otherlv_3= AS ( (lv_type_4_0= RULE_ID ) )
            {
            // InternalCQLParser.g:3266:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) )
            // InternalCQLParser.g:3267:4: (lv_attributes_0_0= ruleSchemaDefinition )
            {
            // InternalCQLParser.g:3267:4: (lv_attributes_0_0= ruleSchemaDefinition )
            // InternalCQLParser.g:3268:5: lv_attributes_0_0= ruleSchemaDefinition
            {

            					newCompositeNode(grammarAccess.getCreateChannelFormatViaFileAccess().getAttributesSchemaDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_48);
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

            otherlv_1=(Token)match(input,FILE,FOLLOW_35); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateChannelFormatViaFileAccess().getFILEKeyword_1());
            		
            // InternalCQLParser.g:3289:3: ( (lv_filename_2_0= RULE_STRING ) )
            // InternalCQLParser.g:3290:4: (lv_filename_2_0= RULE_STRING )
            {
            // InternalCQLParser.g:3290:4: (lv_filename_2_0= RULE_STRING )
            // InternalCQLParser.g:3291:5: lv_filename_2_0= RULE_STRING
            {
            lv_filename_2_0=(Token)match(input,RULE_STRING,FOLLOW_28); 

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
            		
            // InternalCQLParser.g:3311:3: ( (lv_type_4_0= RULE_ID ) )
            // InternalCQLParser.g:3312:4: (lv_type_4_0= RULE_ID )
            {
            // InternalCQLParser.g:3312:4: (lv_type_4_0= RULE_ID )
            // InternalCQLParser.g:3313:5: lv_type_4_0= RULE_ID
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
    // InternalCQLParser.g:3333:1: entryRuleCreateDatabaseStream returns [EObject current=null] : iv_ruleCreateDatabaseStream= ruleCreateDatabaseStream EOF ;
    public final EObject entryRuleCreateDatabaseStream() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateDatabaseStream = null;


        try {
            // InternalCQLParser.g:3333:61: (iv_ruleCreateDatabaseStream= ruleCreateDatabaseStream EOF )
            // InternalCQLParser.g:3334:2: iv_ruleCreateDatabaseStream= ruleCreateDatabaseStream EOF
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
    // InternalCQLParser.g:3340:1: ruleCreateDatabaseStream returns [EObject current=null] : ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= DATABASE ( (lv_database_2_0= RULE_ID ) ) otherlv_3= TABLE ( (lv_table_4_0= RULE_ID ) ) (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= ruleTime ) ) )? ) ;
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
            // InternalCQLParser.g:3346:2: ( ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= DATABASE ( (lv_database_2_0= RULE_ID ) ) otherlv_3= TABLE ( (lv_table_4_0= RULE_ID ) ) (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= ruleTime ) ) )? ) )
            // InternalCQLParser.g:3347:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= DATABASE ( (lv_database_2_0= RULE_ID ) ) otherlv_3= TABLE ( (lv_table_4_0= RULE_ID ) ) (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= ruleTime ) ) )? )
            {
            // InternalCQLParser.g:3347:2: ( ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= DATABASE ( (lv_database_2_0= RULE_ID ) ) otherlv_3= TABLE ( (lv_table_4_0= RULE_ID ) ) (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= ruleTime ) ) )? )
            // InternalCQLParser.g:3348:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) ) otherlv_1= DATABASE ( (lv_database_2_0= RULE_ID ) ) otherlv_3= TABLE ( (lv_table_4_0= RULE_ID ) ) (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= ruleTime ) ) )?
            {
            // InternalCQLParser.g:3348:3: ( (lv_attributes_0_0= ruleSchemaDefinition ) )
            // InternalCQLParser.g:3349:4: (lv_attributes_0_0= ruleSchemaDefinition )
            {
            // InternalCQLParser.g:3349:4: (lv_attributes_0_0= ruleSchemaDefinition )
            // InternalCQLParser.g:3350:5: lv_attributes_0_0= ruleSchemaDefinition
            {

            					newCompositeNode(grammarAccess.getCreateDatabaseStreamAccess().getAttributesSchemaDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_49);
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
            		
            // InternalCQLParser.g:3371:3: ( (lv_database_2_0= RULE_ID ) )
            // InternalCQLParser.g:3372:4: (lv_database_2_0= RULE_ID )
            {
            // InternalCQLParser.g:3372:4: (lv_database_2_0= RULE_ID )
            // InternalCQLParser.g:3373:5: lv_database_2_0= RULE_ID
            {
            lv_database_2_0=(Token)match(input,RULE_ID,FOLLOW_50); 

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
            		
            // InternalCQLParser.g:3393:3: ( (lv_table_4_0= RULE_ID ) )
            // InternalCQLParser.g:3394:4: (lv_table_4_0= RULE_ID )
            {
            // InternalCQLParser.g:3394:4: (lv_table_4_0= RULE_ID )
            // InternalCQLParser.g:3395:5: lv_table_4_0= RULE_ID
            {
            lv_table_4_0=(Token)match(input,RULE_ID,FOLLOW_51); 

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

            // InternalCQLParser.g:3411:3: (otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= ruleTime ) ) )?
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==EACH) ) {
                alt49=1;
            }
            switch (alt49) {
                case 1 :
                    // InternalCQLParser.g:3412:4: otherlv_5= EACH ( (lv_size_6_0= RULE_INT ) ) ( (lv_unit_7_0= ruleTime ) )
                    {
                    otherlv_5=(Token)match(input,EACH,FOLLOW_47); 

                    				newLeafNode(otherlv_5, grammarAccess.getCreateDatabaseStreamAccess().getEACHKeyword_5_0());
                    			
                    // InternalCQLParser.g:3416:4: ( (lv_size_6_0= RULE_INT ) )
                    // InternalCQLParser.g:3417:5: (lv_size_6_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3417:5: (lv_size_6_0= RULE_INT )
                    // InternalCQLParser.g:3418:6: lv_size_6_0= RULE_INT
                    {
                    lv_size_6_0=(Token)match(input,RULE_INT,FOLLOW_52); 

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

                    // InternalCQLParser.g:3434:4: ( (lv_unit_7_0= ruleTime ) )
                    // InternalCQLParser.g:3435:5: (lv_unit_7_0= ruleTime )
                    {
                    // InternalCQLParser.g:3435:5: (lv_unit_7_0= ruleTime )
                    // InternalCQLParser.g:3436:6: lv_unit_7_0= ruleTime
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
    // InternalCQLParser.g:3458:1: entryRuleCreateDatabaseSink returns [EObject current=null] : iv_ruleCreateDatabaseSink= ruleCreateDatabaseSink EOF ;
    public final EObject entryRuleCreateDatabaseSink() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateDatabaseSink = null;


        try {
            // InternalCQLParser.g:3458:59: (iv_ruleCreateDatabaseSink= ruleCreateDatabaseSink EOF )
            // InternalCQLParser.g:3459:2: iv_ruleCreateDatabaseSink= ruleCreateDatabaseSink EOF
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
    // InternalCQLParser.g:3465:1: ruleCreateDatabaseSink returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= AS otherlv_2= DATABASE ( (lv_database_3_0= RULE_ID ) ) otherlv_4= TABLE ( (lv_table_5_0= RULE_ID ) ) (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )? ) ;
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
            // InternalCQLParser.g:3471:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= AS otherlv_2= DATABASE ( (lv_database_3_0= RULE_ID ) ) otherlv_4= TABLE ( (lv_table_5_0= RULE_ID ) ) (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )? ) )
            // InternalCQLParser.g:3472:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= AS otherlv_2= DATABASE ( (lv_database_3_0= RULE_ID ) ) otherlv_4= TABLE ( (lv_table_5_0= RULE_ID ) ) (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )? )
            {
            // InternalCQLParser.g:3472:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= AS otherlv_2= DATABASE ( (lv_database_3_0= RULE_ID ) ) otherlv_4= TABLE ( (lv_table_5_0= RULE_ID ) ) (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )? )
            // InternalCQLParser.g:3473:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= AS otherlv_2= DATABASE ( (lv_database_3_0= RULE_ID ) ) otherlv_4= TABLE ( (lv_table_5_0= RULE_ID ) ) (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )?
            {
            // InternalCQLParser.g:3473:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:3474:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:3474:4: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:3475:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_28); 

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

            otherlv_1=(Token)match(input,AS,FOLLOW_49); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateDatabaseSinkAccess().getASKeyword_1());
            		
            otherlv_2=(Token)match(input,DATABASE,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateDatabaseSinkAccess().getDATABASEKeyword_2());
            		
            // InternalCQLParser.g:3499:3: ( (lv_database_3_0= RULE_ID ) )
            // InternalCQLParser.g:3500:4: (lv_database_3_0= RULE_ID )
            {
            // InternalCQLParser.g:3500:4: (lv_database_3_0= RULE_ID )
            // InternalCQLParser.g:3501:5: lv_database_3_0= RULE_ID
            {
            lv_database_3_0=(Token)match(input,RULE_ID,FOLLOW_50); 

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
            		
            // InternalCQLParser.g:3521:3: ( (lv_table_5_0= RULE_ID ) )
            // InternalCQLParser.g:3522:4: (lv_table_5_0= RULE_ID )
            {
            // InternalCQLParser.g:3522:4: (lv_table_5_0= RULE_ID )
            // InternalCQLParser.g:3523:5: lv_table_5_0= RULE_ID
            {
            lv_table_5_0=(Token)match(input,RULE_ID,FOLLOW_53); 

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

            // InternalCQLParser.g:3539:3: (otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) ) )?
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==AND) ) {
                alt51=1;
            }
            switch (alt51) {
                case 1 :
                    // InternalCQLParser.g:3540:4: otherlv_6= AND ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) )
                    {
                    otherlv_6=(Token)match(input,AND,FOLLOW_54); 

                    				newLeafNode(otherlv_6, grammarAccess.getCreateDatabaseSinkAccess().getANDKeyword_6_0());
                    			
                    // InternalCQLParser.g:3544:4: ( ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) ) )
                    // InternalCQLParser.g:3545:5: ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) )
                    {
                    // InternalCQLParser.g:3545:5: ( (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE ) )
                    // InternalCQLParser.g:3546:6: (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE )
                    {
                    // InternalCQLParser.g:3546:6: (lv_option_7_1= DROP | lv_option_7_2= TRUNCATE )
                    int alt50=2;
                    int LA50_0 = input.LA(1);

                    if ( (LA50_0==DROP) ) {
                        alt50=1;
                    }
                    else if ( (LA50_0==TRUNCATE) ) {
                        alt50=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 50, 0, input);

                        throw nvae;
                    }
                    switch (alt50) {
                        case 1 :
                            // InternalCQLParser.g:3547:7: lv_option_7_1= DROP
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
                            // InternalCQLParser.g:3558:7: lv_option_7_2= TRUNCATE
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
    // InternalCQLParser.g:3576:1: entryRuleCreateView returns [EObject current=null] : iv_ruleCreateView= ruleCreateView EOF ;
    public final EObject entryRuleCreateView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateView = null;


        try {
            // InternalCQLParser.g:3576:51: (iv_ruleCreateView= ruleCreateView EOF )
            // InternalCQLParser.g:3577:2: iv_ruleCreateView= ruleCreateView EOF
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
    // InternalCQLParser.g:3583:1: ruleCreateView returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= FROM ( (lv_select_2_0= ruleInnerSelect ) ) ) ;
    public final EObject ruleCreateView() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        EObject lv_select_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3589:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= FROM ( (lv_select_2_0= ruleInnerSelect ) ) ) )
            // InternalCQLParser.g:3590:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= FROM ( (lv_select_2_0= ruleInnerSelect ) ) )
            {
            // InternalCQLParser.g:3590:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= FROM ( (lv_select_2_0= ruleInnerSelect ) ) )
            // InternalCQLParser.g:3591:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= FROM ( (lv_select_2_0= ruleInnerSelect ) )
            {
            // InternalCQLParser.g:3591:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:3592:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:3592:4: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:3593:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_11); 

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

            otherlv_1=(Token)match(input,FROM,FOLLOW_14); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateViewAccess().getFROMKeyword_1());
            		
            // InternalCQLParser.g:3613:3: ( (lv_select_2_0= ruleInnerSelect ) )
            // InternalCQLParser.g:3614:4: (lv_select_2_0= ruleInnerSelect )
            {
            // InternalCQLParser.g:3614:4: (lv_select_2_0= ruleInnerSelect )
            // InternalCQLParser.g:3615:5: lv_select_2_0= ruleInnerSelect
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
    // InternalCQLParser.g:3636:1: entryRuleCreateDataBaseJDBCConnection returns [EObject current=null] : iv_ruleCreateDataBaseJDBCConnection= ruleCreateDataBaseJDBCConnection EOF ;
    public final EObject entryRuleCreateDataBaseJDBCConnection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateDataBaseJDBCConnection = null;


        try {
            // InternalCQLParser.g:3636:69: (iv_ruleCreateDataBaseJDBCConnection= ruleCreateDataBaseJDBCConnection EOF )
            // InternalCQLParser.g:3637:2: iv_ruleCreateDataBaseJDBCConnection= ruleCreateDataBaseJDBCConnection EOF
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
    // InternalCQLParser.g:3643:1: ruleCreateDataBaseJDBCConnection returns [EObject current=null] : ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= JDBC ( (lv_server_6_0= RULE_ID ) ) (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )? ) ;
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
            // InternalCQLParser.g:3649:2: ( ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= JDBC ( (lv_server_6_0= RULE_ID ) ) (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )? ) )
            // InternalCQLParser.g:3650:2: ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= JDBC ( (lv_server_6_0= RULE_ID ) ) (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )? )
            {
            // InternalCQLParser.g:3650:2: ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= JDBC ( (lv_server_6_0= RULE_ID ) ) (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )? )
            // InternalCQLParser.g:3651:3: () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= JDBC ( (lv_server_6_0= RULE_ID ) ) (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )?
            {
            // InternalCQLParser.g:3651:3: ()
            // InternalCQLParser.g:3652:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getCreateDataBaseJDBCConnectionAccess().getCreateDataBaseConnectionJDBCAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,CREATE,FOLLOW_49); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getCREATEKeyword_1());
            		
            otherlv_2=(Token)match(input,DATABASE,FOLLOW_55); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getDATABASEKeyword_2());
            		
            otherlv_3=(Token)match(input,CONNECTION,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getCONNECTIONKeyword_3());
            		
            // InternalCQLParser.g:3670:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCQLParser.g:3671:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCQLParser.g:3671:4: (lv_name_4_0= RULE_ID )
            // InternalCQLParser.g:3672:5: lv_name_4_0= RULE_ID
            {
            lv_name_4_0=(Token)match(input,RULE_ID,FOLLOW_56); 

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
            		
            // InternalCQLParser.g:3692:3: ( (lv_server_6_0= RULE_ID ) )
            // InternalCQLParser.g:3693:4: (lv_server_6_0= RULE_ID )
            {
            // InternalCQLParser.g:3693:4: (lv_server_6_0= RULE_ID )
            // InternalCQLParser.g:3694:5: lv_server_6_0= RULE_ID
            {
            lv_server_6_0=(Token)match(input,RULE_ID,FOLLOW_57); 

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

            // InternalCQLParser.g:3710:3: (otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )? )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==WITH) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // InternalCQLParser.g:3711:4: otherlv_7= WITH otherlv_8= USER ( (lv_user_9_0= RULE_ID ) ) otherlv_10= PASSWORD ( (lv_password_11_0= RULE_ID ) ) ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )?
                    {
                    otherlv_7=(Token)match(input,WITH,FOLLOW_58); 

                    				newLeafNode(otherlv_7, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getWITHKeyword_7_0());
                    			
                    otherlv_8=(Token)match(input,USER,FOLLOW_5); 

                    				newLeafNode(otherlv_8, grammarAccess.getCreateDataBaseJDBCConnectionAccess().getUSERKeyword_7_1());
                    			
                    // InternalCQLParser.g:3719:4: ( (lv_user_9_0= RULE_ID ) )
                    // InternalCQLParser.g:3720:5: (lv_user_9_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3720:5: (lv_user_9_0= RULE_ID )
                    // InternalCQLParser.g:3721:6: lv_user_9_0= RULE_ID
                    {
                    lv_user_9_0=(Token)match(input,RULE_ID,FOLLOW_59); 

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
                    			
                    // InternalCQLParser.g:3741:4: ( (lv_password_11_0= RULE_ID ) )
                    // InternalCQLParser.g:3742:5: (lv_password_11_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3742:5: (lv_password_11_0= RULE_ID )
                    // InternalCQLParser.g:3743:6: lv_password_11_0= RULE_ID
                    {
                    lv_password_11_0=(Token)match(input,RULE_ID,FOLLOW_60); 

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

                    // InternalCQLParser.g:3759:4: ( (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK ) )?
                    int alt52=2;
                    int LA52_0 = input.LA(1);

                    if ( (LA52_0==NO_LAZY_CONNECTION_CHECK) ) {
                        alt52=1;
                    }
                    switch (alt52) {
                        case 1 :
                            // InternalCQLParser.g:3760:5: (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK )
                            {
                            // InternalCQLParser.g:3760:5: (lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK )
                            // InternalCQLParser.g:3761:6: lv_lazy_12_0= NO_LAZY_CONNECTION_CHECK
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
    // InternalCQLParser.g:3778:1: entryRuleCreateDataBaseGenericConnection returns [EObject current=null] : iv_ruleCreateDataBaseGenericConnection= ruleCreateDataBaseGenericConnection EOF ;
    public final EObject entryRuleCreateDataBaseGenericConnection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateDataBaseGenericConnection = null;


        try {
            // InternalCQLParser.g:3778:72: (iv_ruleCreateDataBaseGenericConnection= ruleCreateDataBaseGenericConnection EOF )
            // InternalCQLParser.g:3779:2: iv_ruleCreateDataBaseGenericConnection= ruleCreateDataBaseGenericConnection EOF
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
    // InternalCQLParser.g:3785:1: ruleCreateDataBaseGenericConnection returns [EObject current=null] : ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= AS ( (lv_driver_6_0= RULE_ID ) ) otherlv_7= TO ( (lv_source_8_0= RULE_ID ) ) (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )? (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )? ) ;
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
            // InternalCQLParser.g:3791:2: ( ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= AS ( (lv_driver_6_0= RULE_ID ) ) otherlv_7= TO ( (lv_source_8_0= RULE_ID ) ) (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )? (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )? ) )
            // InternalCQLParser.g:3792:2: ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= AS ( (lv_driver_6_0= RULE_ID ) ) otherlv_7= TO ( (lv_source_8_0= RULE_ID ) ) (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )? (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )? )
            {
            // InternalCQLParser.g:3792:2: ( () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= AS ( (lv_driver_6_0= RULE_ID ) ) otherlv_7= TO ( (lv_source_8_0= RULE_ID ) ) (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )? (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )? )
            // InternalCQLParser.g:3793:3: () otherlv_1= CREATE otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) otherlv_5= AS ( (lv_driver_6_0= RULE_ID ) ) otherlv_7= TO ( (lv_source_8_0= RULE_ID ) ) (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )? (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )?
            {
            // InternalCQLParser.g:3793:3: ()
            // InternalCQLParser.g:3794:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getCreateDataBaseGenericConnectionAccess().getCreateDataBaseConnectionGenericAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,CREATE,FOLLOW_49); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateDataBaseGenericConnectionAccess().getCREATEKeyword_1());
            		
            otherlv_2=(Token)match(input,DATABASE,FOLLOW_55); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateDataBaseGenericConnectionAccess().getDATABASEKeyword_2());
            		
            otherlv_3=(Token)match(input,CONNECTION,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateDataBaseGenericConnectionAccess().getCONNECTIONKeyword_3());
            		
            // InternalCQLParser.g:3812:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCQLParser.g:3813:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCQLParser.g:3813:4: (lv_name_4_0= RULE_ID )
            // InternalCQLParser.g:3814:5: lv_name_4_0= RULE_ID
            {
            lv_name_4_0=(Token)match(input,RULE_ID,FOLLOW_28); 

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
            		
            // InternalCQLParser.g:3834:3: ( (lv_driver_6_0= RULE_ID ) )
            // InternalCQLParser.g:3835:4: (lv_driver_6_0= RULE_ID )
            {
            // InternalCQLParser.g:3835:4: (lv_driver_6_0= RULE_ID )
            // InternalCQLParser.g:3836:5: lv_driver_6_0= RULE_ID
            {
            lv_driver_6_0=(Token)match(input,RULE_ID,FOLLOW_61); 

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
            		
            // InternalCQLParser.g:3856:3: ( (lv_source_8_0= RULE_ID ) )
            // InternalCQLParser.g:3857:4: (lv_source_8_0= RULE_ID )
            {
            // InternalCQLParser.g:3857:4: (lv_source_8_0= RULE_ID )
            // InternalCQLParser.g:3858:5: lv_source_8_0= RULE_ID
            {
            lv_source_8_0=(Token)match(input,RULE_ID,FOLLOW_62); 

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

            // InternalCQLParser.g:3874:3: (otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) ) )?
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==AT) ) {
                alt54=1;
            }
            switch (alt54) {
                case 1 :
                    // InternalCQLParser.g:3875:4: otherlv_9= AT ( (lv_host_10_0= RULE_ID ) ) otherlv_11= Colon ( (lv_port_12_0= RULE_INT ) )
                    {
                    otherlv_9=(Token)match(input,AT,FOLLOW_5); 

                    				newLeafNode(otherlv_9, grammarAccess.getCreateDataBaseGenericConnectionAccess().getATKeyword_9_0());
                    			
                    // InternalCQLParser.g:3879:4: ( (lv_host_10_0= RULE_ID ) )
                    // InternalCQLParser.g:3880:5: (lv_host_10_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3880:5: (lv_host_10_0= RULE_ID )
                    // InternalCQLParser.g:3881:6: lv_host_10_0= RULE_ID
                    {
                    lv_host_10_0=(Token)match(input,RULE_ID,FOLLOW_46); 

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

                    otherlv_11=(Token)match(input,Colon,FOLLOW_47); 

                    				newLeafNode(otherlv_11, grammarAccess.getCreateDataBaseGenericConnectionAccess().getColonKeyword_9_2());
                    			
                    // InternalCQLParser.g:3901:4: ( (lv_port_12_0= RULE_INT ) )
                    // InternalCQLParser.g:3902:5: (lv_port_12_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3902:5: (lv_port_12_0= RULE_INT )
                    // InternalCQLParser.g:3903:6: lv_port_12_0= RULE_INT
                    {
                    lv_port_12_0=(Token)match(input,RULE_INT,FOLLOW_57); 

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

            // InternalCQLParser.g:3920:3: (otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )? )?
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==WITH) ) {
                alt56=1;
            }
            switch (alt56) {
                case 1 :
                    // InternalCQLParser.g:3921:4: otherlv_13= WITH otherlv_14= USER ( (lv_user_15_0= RULE_ID ) ) otherlv_16= PASSWORD ( (lv_password_17_0= RULE_ID ) ) ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )?
                    {
                    otherlv_13=(Token)match(input,WITH,FOLLOW_58); 

                    				newLeafNode(otherlv_13, grammarAccess.getCreateDataBaseGenericConnectionAccess().getWITHKeyword_10_0());
                    			
                    otherlv_14=(Token)match(input,USER,FOLLOW_5); 

                    				newLeafNode(otherlv_14, grammarAccess.getCreateDataBaseGenericConnectionAccess().getUSERKeyword_10_1());
                    			
                    // InternalCQLParser.g:3929:4: ( (lv_user_15_0= RULE_ID ) )
                    // InternalCQLParser.g:3930:5: (lv_user_15_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3930:5: (lv_user_15_0= RULE_ID )
                    // InternalCQLParser.g:3931:6: lv_user_15_0= RULE_ID
                    {
                    lv_user_15_0=(Token)match(input,RULE_ID,FOLLOW_59); 

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
                    			
                    // InternalCQLParser.g:3951:4: ( (lv_password_17_0= RULE_ID ) )
                    // InternalCQLParser.g:3952:5: (lv_password_17_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3952:5: (lv_password_17_0= RULE_ID )
                    // InternalCQLParser.g:3953:6: lv_password_17_0= RULE_ID
                    {
                    lv_password_17_0=(Token)match(input,RULE_ID,FOLLOW_60); 

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

                    // InternalCQLParser.g:3969:4: ( (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK ) )?
                    int alt55=2;
                    int LA55_0 = input.LA(1);

                    if ( (LA55_0==NO_LAZY_CONNECTION_CHECK) ) {
                        alt55=1;
                    }
                    switch (alt55) {
                        case 1 :
                            // InternalCQLParser.g:3970:5: (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK )
                            {
                            // InternalCQLParser.g:3970:5: (lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK )
                            // InternalCQLParser.g:3971:6: lv_lazy_18_0= NO_LAZY_CONNECTION_CHECK
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
    // InternalCQLParser.g:3988:1: entryRuleDropDatabaseConnection returns [EObject current=null] : iv_ruleDropDatabaseConnection= ruleDropDatabaseConnection EOF ;
    public final EObject entryRuleDropDatabaseConnection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDropDatabaseConnection = null;


        try {
            // InternalCQLParser.g:3988:63: (iv_ruleDropDatabaseConnection= ruleDropDatabaseConnection EOF )
            // InternalCQLParser.g:3989:2: iv_ruleDropDatabaseConnection= ruleDropDatabaseConnection EOF
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
    // InternalCQLParser.g:3995:1: ruleDropDatabaseConnection returns [EObject current=null] : ( () otherlv_1= DROP otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) ) ;
    public final EObject ruleDropDatabaseConnection() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token lv_name_4_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:4001:2: ( ( () otherlv_1= DROP otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:4002:2: ( () otherlv_1= DROP otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) )
            {
            // InternalCQLParser.g:4002:2: ( () otherlv_1= DROP otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) ) )
            // InternalCQLParser.g:4003:3: () otherlv_1= DROP otherlv_2= DATABASE otherlv_3= CONNECTION ( (lv_name_4_0= RULE_ID ) )
            {
            // InternalCQLParser.g:4003:3: ()
            // InternalCQLParser.g:4004:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getDropDatabaseConnectionAccess().getDropDatabaseConnectionAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,DROP,FOLLOW_49); 

            			newLeafNode(otherlv_1, grammarAccess.getDropDatabaseConnectionAccess().getDROPKeyword_1());
            		
            otherlv_2=(Token)match(input,DATABASE,FOLLOW_55); 

            			newLeafNode(otherlv_2, grammarAccess.getDropDatabaseConnectionAccess().getDATABASEKeyword_2());
            		
            otherlv_3=(Token)match(input,CONNECTION,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getDropDatabaseConnectionAccess().getCONNECTIONKeyword_3());
            		
            // InternalCQLParser.g:4022:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCQLParser.g:4023:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCQLParser.g:4023:4: (lv_name_4_0= RULE_ID )
            // InternalCQLParser.g:4024:5: lv_name_4_0= RULE_ID
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
    // InternalCQLParser.g:4044:1: entryRuleContextStoreType returns [EObject current=null] : iv_ruleContextStoreType= ruleContextStoreType EOF ;
    public final EObject entryRuleContextStoreType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleContextStoreType = null;


        try {
            // InternalCQLParser.g:4044:57: (iv_ruleContextStoreType= ruleContextStoreType EOF )
            // InternalCQLParser.g:4045:2: iv_ruleContextStoreType= ruleContextStoreType EOF
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
    // InternalCQLParser.g:4051:1: ruleContextStoreType returns [EObject current=null] : ( ( (lv_type_0_0= SINGLE ) ) | ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? ) ) ;
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
            // InternalCQLParser.g:4057:2: ( ( ( (lv_type_0_0= SINGLE ) ) | ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? ) ) )
            // InternalCQLParser.g:4058:2: ( ( (lv_type_0_0= SINGLE ) ) | ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? ) )
            {
            // InternalCQLParser.g:4058:2: ( ( (lv_type_0_0= SINGLE ) ) | ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? ) )
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==SINGLE) ) {
                alt58=1;
            }
            else if ( (LA58_0==MULTI) ) {
                alt58=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 58, 0, input);

                throw nvae;
            }
            switch (alt58) {
                case 1 :
                    // InternalCQLParser.g:4059:3: ( (lv_type_0_0= SINGLE ) )
                    {
                    // InternalCQLParser.g:4059:3: ( (lv_type_0_0= SINGLE ) )
                    // InternalCQLParser.g:4060:4: (lv_type_0_0= SINGLE )
                    {
                    // InternalCQLParser.g:4060:4: (lv_type_0_0= SINGLE )
                    // InternalCQLParser.g:4061:5: lv_type_0_0= SINGLE
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
                    // InternalCQLParser.g:4074:3: ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? )
                    {
                    // InternalCQLParser.g:4074:3: ( ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )? )
                    // InternalCQLParser.g:4075:4: ( (lv_type_1_0= MULTI ) ) ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )?
                    {
                    // InternalCQLParser.g:4075:4: ( (lv_type_1_0= MULTI ) )
                    // InternalCQLParser.g:4076:5: (lv_type_1_0= MULTI )
                    {
                    // InternalCQLParser.g:4076:5: (lv_type_1_0= MULTI )
                    // InternalCQLParser.g:4077:6: lv_type_1_0= MULTI
                    {
                    lv_type_1_0=(Token)match(input,MULTI,FOLLOW_47); 

                    						newLeafNode(lv_type_1_0, grammarAccess.getContextStoreTypeAccess().getTypeMULTIKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getContextStoreTypeRule());
                    						}
                    						setWithLastConsumed(current, "type", lv_type_1_0, "MULTI");
                    					

                    }


                    }

                    // InternalCQLParser.g:4089:4: ( (lv_size_2_0= RULE_INT ) )
                    // InternalCQLParser.g:4090:5: (lv_size_2_0= RULE_INT )
                    {
                    // InternalCQLParser.g:4090:5: (lv_size_2_0= RULE_INT )
                    // InternalCQLParser.g:4091:6: lv_size_2_0= RULE_INT
                    {
                    lv_size_2_0=(Token)match(input,RULE_INT,FOLLOW_63); 

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

                    // InternalCQLParser.g:4107:4: (otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) ) )?
                    int alt57=2;
                    int LA57_0 = input.LA(1);

                    if ( (LA57_0==PARTITION) ) {
                        alt57=1;
                    }
                    switch (alt57) {
                        case 1 :
                            // InternalCQLParser.g:4108:5: otherlv_3= PARTITION otherlv_4= BY ( (lv_partition_5_0= RULE_INT ) )
                            {
                            otherlv_3=(Token)match(input,PARTITION,FOLLOW_19); 

                            					newLeafNode(otherlv_3, grammarAccess.getContextStoreTypeAccess().getPARTITIONKeyword_1_2_0());
                            				
                            otherlv_4=(Token)match(input,BY,FOLLOW_47); 

                            					newLeafNode(otherlv_4, grammarAccess.getContextStoreTypeAccess().getBYKeyword_1_2_1());
                            				
                            // InternalCQLParser.g:4116:5: ( (lv_partition_5_0= RULE_INT ) )
                            // InternalCQLParser.g:4117:6: (lv_partition_5_0= RULE_INT )
                            {
                            // InternalCQLParser.g:4117:6: (lv_partition_5_0= RULE_INT )
                            // InternalCQLParser.g:4118:7: lv_partition_5_0= RULE_INT
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
    // InternalCQLParser.g:4140:1: entryRuleCreateContextStore returns [EObject current=null] : iv_ruleCreateContextStore= ruleCreateContextStore EOF ;
    public final EObject entryRuleCreateContextStore() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateContextStore = null;


        try {
            // InternalCQLParser.g:4140:59: (iv_ruleCreateContextStore= ruleCreateContextStore EOF )
            // InternalCQLParser.g:4141:2: iv_ruleCreateContextStore= ruleCreateContextStore EOF
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
    // InternalCQLParser.g:4147:1: ruleCreateContextStore returns [EObject current=null] : ( () otherlv_1= CREATE otherlv_2= CONTEXT otherlv_3= STORE ( (lv_attributes_4_0= ruleSchemaDefinition ) ) otherlv_5= AS ( (lv_contextType_6_0= ruleContextStoreType ) ) ) ;
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
            // InternalCQLParser.g:4153:2: ( ( () otherlv_1= CREATE otherlv_2= CONTEXT otherlv_3= STORE ( (lv_attributes_4_0= ruleSchemaDefinition ) ) otherlv_5= AS ( (lv_contextType_6_0= ruleContextStoreType ) ) ) )
            // InternalCQLParser.g:4154:2: ( () otherlv_1= CREATE otherlv_2= CONTEXT otherlv_3= STORE ( (lv_attributes_4_0= ruleSchemaDefinition ) ) otherlv_5= AS ( (lv_contextType_6_0= ruleContextStoreType ) ) )
            {
            // InternalCQLParser.g:4154:2: ( () otherlv_1= CREATE otherlv_2= CONTEXT otherlv_3= STORE ( (lv_attributes_4_0= ruleSchemaDefinition ) ) otherlv_5= AS ( (lv_contextType_6_0= ruleContextStoreType ) ) )
            // InternalCQLParser.g:4155:3: () otherlv_1= CREATE otherlv_2= CONTEXT otherlv_3= STORE ( (lv_attributes_4_0= ruleSchemaDefinition ) ) otherlv_5= AS ( (lv_contextType_6_0= ruleContextStoreType ) )
            {
            // InternalCQLParser.g:4155:3: ()
            // InternalCQLParser.g:4156:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getCreateContextStoreAccess().getCreateContextStoreAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,CREATE,FOLLOW_64); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateContextStoreAccess().getCREATEKeyword_1());
            		
            otherlv_2=(Token)match(input,CONTEXT,FOLLOW_65); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateContextStoreAccess().getCONTEXTKeyword_2());
            		
            otherlv_3=(Token)match(input,STORE,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateContextStoreAccess().getSTOREKeyword_3());
            		
            // InternalCQLParser.g:4174:3: ( (lv_attributes_4_0= ruleSchemaDefinition ) )
            // InternalCQLParser.g:4175:4: (lv_attributes_4_0= ruleSchemaDefinition )
            {
            // InternalCQLParser.g:4175:4: (lv_attributes_4_0= ruleSchemaDefinition )
            // InternalCQLParser.g:4176:5: lv_attributes_4_0= ruleSchemaDefinition
            {

            					newCompositeNode(grammarAccess.getCreateContextStoreAccess().getAttributesSchemaDefinitionParserRuleCall_4_0());
            				
            pushFollow(FOLLOW_28);
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

            otherlv_5=(Token)match(input,AS,FOLLOW_66); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateContextStoreAccess().getASKeyword_5());
            		
            // InternalCQLParser.g:4197:3: ( (lv_contextType_6_0= ruleContextStoreType ) )
            // InternalCQLParser.g:4198:4: (lv_contextType_6_0= ruleContextStoreType )
            {
            // InternalCQLParser.g:4198:4: (lv_contextType_6_0= ruleContextStoreType )
            // InternalCQLParser.g:4199:5: lv_contextType_6_0= ruleContextStoreType
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
    // InternalCQLParser.g:4220:1: entryRuleDropContextStore returns [EObject current=null] : iv_ruleDropContextStore= ruleDropContextStore EOF ;
    public final EObject entryRuleDropContextStore() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDropContextStore = null;


        try {
            // InternalCQLParser.g:4220:57: (iv_ruleDropContextStore= ruleDropContextStore EOF )
            // InternalCQLParser.g:4221:2: iv_ruleDropContextStore= ruleDropContextStore EOF
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
    // InternalCQLParser.g:4227:1: ruleDropContextStore returns [EObject current=null] : ( () otherlv_1= DROP otherlv_2= CONTEXT otherlv_3= STORE ( (lv_name_4_0= RULE_ID ) ) (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )? ) ;
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
            // InternalCQLParser.g:4233:2: ( ( () otherlv_1= DROP otherlv_2= CONTEXT otherlv_3= STORE ( (lv_name_4_0= RULE_ID ) ) (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )? ) )
            // InternalCQLParser.g:4234:2: ( () otherlv_1= DROP otherlv_2= CONTEXT otherlv_3= STORE ( (lv_name_4_0= RULE_ID ) ) (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )? )
            {
            // InternalCQLParser.g:4234:2: ( () otherlv_1= DROP otherlv_2= CONTEXT otherlv_3= STORE ( (lv_name_4_0= RULE_ID ) ) (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )? )
            // InternalCQLParser.g:4235:3: () otherlv_1= DROP otherlv_2= CONTEXT otherlv_3= STORE ( (lv_name_4_0= RULE_ID ) ) (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )?
            {
            // InternalCQLParser.g:4235:3: ()
            // InternalCQLParser.g:4236:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getDropContextStoreAccess().getDropContextStoreAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,DROP,FOLLOW_64); 

            			newLeafNode(otherlv_1, grammarAccess.getDropContextStoreAccess().getDROPKeyword_1());
            		
            otherlv_2=(Token)match(input,CONTEXT,FOLLOW_65); 

            			newLeafNode(otherlv_2, grammarAccess.getDropContextStoreAccess().getCONTEXTKeyword_2());
            		
            otherlv_3=(Token)match(input,STORE,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getDropContextStoreAccess().getSTOREKeyword_3());
            		
            // InternalCQLParser.g:4254:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCQLParser.g:4255:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCQLParser.g:4255:4: (lv_name_4_0= RULE_ID )
            // InternalCQLParser.g:4256:5: lv_name_4_0= RULE_ID
            {
            lv_name_4_0=(Token)match(input,RULE_ID,FOLLOW_67); 

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

            // InternalCQLParser.g:4272:3: (otherlv_5= IF ( (lv_exists_6_0= EXISTS ) ) )?
            int alt59=2;
            int LA59_0 = input.LA(1);

            if ( (LA59_0==IF) ) {
                alt59=1;
            }
            switch (alt59) {
                case 1 :
                    // InternalCQLParser.g:4273:4: otherlv_5= IF ( (lv_exists_6_0= EXISTS ) )
                    {
                    otherlv_5=(Token)match(input,IF,FOLLOW_68); 

                    				newLeafNode(otherlv_5, grammarAccess.getDropContextStoreAccess().getIFKeyword_5_0());
                    			
                    // InternalCQLParser.g:4277:4: ( (lv_exists_6_0= EXISTS ) )
                    // InternalCQLParser.g:4278:5: (lv_exists_6_0= EXISTS )
                    {
                    // InternalCQLParser.g:4278:5: (lv_exists_6_0= EXISTS )
                    // InternalCQLParser.g:4279:6: lv_exists_6_0= EXISTS
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
    // InternalCQLParser.g:4296:1: entryRuleStreamTo returns [EObject current=null] : iv_ruleStreamTo= ruleStreamTo EOF ;
    public final EObject entryRuleStreamTo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStreamTo = null;


        try {
            // InternalCQLParser.g:4296:49: (iv_ruleStreamTo= ruleStreamTo EOF )
            // InternalCQLParser.g:4297:2: iv_ruleStreamTo= ruleStreamTo EOF
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
    // InternalCQLParser.g:4303:1: ruleStreamTo returns [EObject current=null] : ( () otherlv_1= STREAM otherlv_2= TO ( (lv_name_3_0= RULE_ID ) ) ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) ) ) ;
    public final EObject ruleStreamTo() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token lv_name_3_0=null;
        Token lv_inputname_5_0=null;
        EObject lv_statement_4_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:4309:2: ( ( () otherlv_1= STREAM otherlv_2= TO ( (lv_name_3_0= RULE_ID ) ) ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) ) ) )
            // InternalCQLParser.g:4310:2: ( () otherlv_1= STREAM otherlv_2= TO ( (lv_name_3_0= RULE_ID ) ) ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) ) )
            {
            // InternalCQLParser.g:4310:2: ( () otherlv_1= STREAM otherlv_2= TO ( (lv_name_3_0= RULE_ID ) ) ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:4311:3: () otherlv_1= STREAM otherlv_2= TO ( (lv_name_3_0= RULE_ID ) ) ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) )
            {
            // InternalCQLParser.g:4311:3: ()
            // InternalCQLParser.g:4312:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getStreamToAccess().getStreamToAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,STREAM,FOLLOW_61); 

            			newLeafNode(otherlv_1, grammarAccess.getStreamToAccess().getSTREAMKeyword_1());
            		
            otherlv_2=(Token)match(input,TO,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getStreamToAccess().getTOKeyword_2());
            		
            // InternalCQLParser.g:4326:3: ( (lv_name_3_0= RULE_ID ) )
            // InternalCQLParser.g:4327:4: (lv_name_3_0= RULE_ID )
            {
            // InternalCQLParser.g:4327:4: (lv_name_3_0= RULE_ID )
            // InternalCQLParser.g:4328:5: lv_name_3_0= RULE_ID
            {
            lv_name_3_0=(Token)match(input,RULE_ID,FOLLOW_69); 

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

            // InternalCQLParser.g:4344:3: ( ( (lv_statement_4_0= ruleInnerSelect2 ) ) | ( (lv_inputname_5_0= RULE_ID ) ) )
            int alt60=2;
            int LA60_0 = input.LA(1);

            if ( (LA60_0==SELECT) ) {
                alt60=1;
            }
            else if ( (LA60_0==RULE_ID) ) {
                alt60=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 60, 0, input);

                throw nvae;
            }
            switch (alt60) {
                case 1 :
                    // InternalCQLParser.g:4345:4: ( (lv_statement_4_0= ruleInnerSelect2 ) )
                    {
                    // InternalCQLParser.g:4345:4: ( (lv_statement_4_0= ruleInnerSelect2 ) )
                    // InternalCQLParser.g:4346:5: (lv_statement_4_0= ruleInnerSelect2 )
                    {
                    // InternalCQLParser.g:4346:5: (lv_statement_4_0= ruleInnerSelect2 )
                    // InternalCQLParser.g:4347:6: lv_statement_4_0= ruleInnerSelect2
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
                    // InternalCQLParser.g:4365:4: ( (lv_inputname_5_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:4365:4: ( (lv_inputname_5_0= RULE_ID ) )
                    // InternalCQLParser.g:4366:5: (lv_inputname_5_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4366:5: (lv_inputname_5_0= RULE_ID )
                    // InternalCQLParser.g:4367:6: lv_inputname_5_0= RULE_ID
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
    // InternalCQLParser.g:4388:1: entryRuleDropStream returns [EObject current=null] : iv_ruleDropStream= ruleDropStream EOF ;
    public final EObject entryRuleDropStream() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDropStream = null;


        try {
            // InternalCQLParser.g:4388:51: (iv_ruleDropStream= ruleDropStream EOF )
            // InternalCQLParser.g:4389:2: iv_ruleDropStream= ruleDropStream EOF
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
    // InternalCQLParser.g:4395:1: ruleDropStream returns [EObject current=null] : ( () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )? ) ;
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
            // InternalCQLParser.g:4401:2: ( ( () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )? ) )
            // InternalCQLParser.g:4402:2: ( () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )? )
            {
            // InternalCQLParser.g:4402:2: ( () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )? )
            // InternalCQLParser.g:4403:3: () otherlv_1= DROP ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) ) ( (lv_stream_3_0= RULE_ID ) ) ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )?
            {
            // InternalCQLParser.g:4403:3: ()
            // InternalCQLParser.g:4404:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getDropStreamAccess().getDropStreamAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,DROP,FOLLOW_43); 

            			newLeafNode(otherlv_1, grammarAccess.getDropStreamAccess().getDROPKeyword_1());
            		
            // InternalCQLParser.g:4414:3: ( ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) ) )
            // InternalCQLParser.g:4415:4: ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) )
            {
            // InternalCQLParser.g:4415:4: ( (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW ) )
            // InternalCQLParser.g:4416:5: (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW )
            {
            // InternalCQLParser.g:4416:5: (lv_name_2_1= SINK | lv_name_2_2= STREAM | lv_name_2_3= VIEW )
            int alt61=3;
            switch ( input.LA(1) ) {
            case SINK:
                {
                alt61=1;
                }
                break;
            case STREAM:
                {
                alt61=2;
                }
                break;
            case VIEW:
                {
                alt61=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 61, 0, input);

                throw nvae;
            }

            switch (alt61) {
                case 1 :
                    // InternalCQLParser.g:4417:6: lv_name_2_1= SINK
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
                    // InternalCQLParser.g:4428:6: lv_name_2_2= STREAM
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
                    // InternalCQLParser.g:4439:6: lv_name_2_3= VIEW
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

            // InternalCQLParser.g:4452:3: ( (lv_stream_3_0= RULE_ID ) )
            // InternalCQLParser.g:4453:4: (lv_stream_3_0= RULE_ID )
            {
            // InternalCQLParser.g:4453:4: (lv_stream_3_0= RULE_ID )
            // InternalCQLParser.g:4454:5: lv_stream_3_0= RULE_ID
            {
            lv_stream_3_0=(Token)match(input,RULE_ID,FOLLOW_67); 

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

            // InternalCQLParser.g:4470:3: ( ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS )?
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==IF) ) {
                alt62=1;
            }
            switch (alt62) {
                case 1 :
                    // InternalCQLParser.g:4471:4: ( (lv_exists_4_0= IF ) ) otherlv_5= EXISTS
                    {
                    // InternalCQLParser.g:4471:4: ( (lv_exists_4_0= IF ) )
                    // InternalCQLParser.g:4472:5: (lv_exists_4_0= IF )
                    {
                    // InternalCQLParser.g:4472:5: (lv_exists_4_0= IF )
                    // InternalCQLParser.g:4473:6: lv_exists_4_0= IF
                    {
                    lv_exists_4_0=(Token)match(input,IF,FOLLOW_68); 

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
    // InternalCQLParser.g:4494:1: entryRuleUserManagement returns [EObject current=null] : iv_ruleUserManagement= ruleUserManagement EOF ;
    public final EObject entryRuleUserManagement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleUserManagement = null;


        try {
            // InternalCQLParser.g:4494:55: (iv_ruleUserManagement= ruleUserManagement EOF )
            // InternalCQLParser.g:4495:2: iv_ruleUserManagement= ruleUserManagement EOF
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
    // InternalCQLParser.g:4501:1: ruleUserManagement returns [EObject current=null] : ( () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )? ) ;
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
            // InternalCQLParser.g:4507:2: ( ( () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )? ) )
            // InternalCQLParser.g:4508:2: ( () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )? )
            {
            // InternalCQLParser.g:4508:2: ( () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )? )
            // InternalCQLParser.g:4509:3: () ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) ) ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) ) ( (lv_subjectName_3_0= RULE_ID ) ) (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )?
            {
            // InternalCQLParser.g:4509:3: ()
            // InternalCQLParser.g:4510:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getUserManagementAccess().getUserManagementAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:4516:3: ( ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) ) )
            // InternalCQLParser.g:4517:4: ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) )
            {
            // InternalCQLParser.g:4517:4: ( (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP ) )
            // InternalCQLParser.g:4518:5: (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP )
            {
            // InternalCQLParser.g:4518:5: (lv_name_1_1= CREATE | lv_name_1_2= ALTER | lv_name_1_3= DROP )
            int alt63=3;
            switch ( input.LA(1) ) {
            case CREATE:
                {
                alt63=1;
                }
                break;
            case ALTER:
                {
                alt63=2;
                }
                break;
            case DROP:
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
                    // InternalCQLParser.g:4519:6: lv_name_1_1= CREATE
                    {
                    lv_name_1_1=(Token)match(input,CREATE,FOLLOW_70); 

                    						newLeafNode(lv_name_1_1, grammarAccess.getUserManagementAccess().getNameCREATEKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:4530:6: lv_name_1_2= ALTER
                    {
                    lv_name_1_2=(Token)match(input,ALTER,FOLLOW_70); 

                    						newLeafNode(lv_name_1_2, grammarAccess.getUserManagementAccess().getNameALTERKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getUserManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:4541:6: lv_name_1_3= DROP
                    {
                    lv_name_1_3=(Token)match(input,DROP,FOLLOW_70); 

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

            // InternalCQLParser.g:4554:3: ( ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) ) )
            // InternalCQLParser.g:4555:4: ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) )
            {
            // InternalCQLParser.g:4555:4: ( (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT ) )
            // InternalCQLParser.g:4556:5: (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT )
            {
            // InternalCQLParser.g:4556:5: (lv_subject_2_1= USER | lv_subject_2_2= ROLE | lv_subject_2_3= TENANT )
            int alt64=3;
            switch ( input.LA(1) ) {
            case USER:
                {
                alt64=1;
                }
                break;
            case ROLE:
                {
                alt64=2;
                }
                break;
            case TENANT:
                {
                alt64=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 64, 0, input);

                throw nvae;
            }

            switch (alt64) {
                case 1 :
                    // InternalCQLParser.g:4557:6: lv_subject_2_1= USER
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
                    // InternalCQLParser.g:4568:6: lv_subject_2_2= ROLE
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
                    // InternalCQLParser.g:4579:6: lv_subject_2_3= TENANT
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

            // InternalCQLParser.g:4592:3: ( (lv_subjectName_3_0= RULE_ID ) )
            // InternalCQLParser.g:4593:4: (lv_subjectName_3_0= RULE_ID )
            {
            // InternalCQLParser.g:4593:4: (lv_subjectName_3_0= RULE_ID )
            // InternalCQLParser.g:4594:5: lv_subjectName_3_0= RULE_ID
            {
            lv_subjectName_3_0=(Token)match(input,RULE_ID,FOLLOW_71); 

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

            // InternalCQLParser.g:4610:3: (otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) ) )?
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( (LA65_0==IDENTIFIED) ) {
                alt65=1;
            }
            switch (alt65) {
                case 1 :
                    // InternalCQLParser.g:4611:4: otherlv_4= IDENTIFIED otherlv_5= BY ( (lv_password_6_0= RULE_STRING ) )
                    {
                    otherlv_4=(Token)match(input,IDENTIFIED,FOLLOW_19); 

                    				newLeafNode(otherlv_4, grammarAccess.getUserManagementAccess().getIDENTIFIEDKeyword_4_0());
                    			
                    otherlv_5=(Token)match(input,BY,FOLLOW_35); 

                    				newLeafNode(otherlv_5, grammarAccess.getUserManagementAccess().getBYKeyword_4_1());
                    			
                    // InternalCQLParser.g:4619:4: ( (lv_password_6_0= RULE_STRING ) )
                    // InternalCQLParser.g:4620:5: (lv_password_6_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:4620:5: (lv_password_6_0= RULE_STRING )
                    // InternalCQLParser.g:4621:6: lv_password_6_0= RULE_STRING
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
    // InternalCQLParser.g:4642:1: entryRuleRightsManagement returns [EObject current=null] : iv_ruleRightsManagement= ruleRightsManagement EOF ;
    public final EObject entryRuleRightsManagement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRightsManagement = null;


        try {
            // InternalCQLParser.g:4642:57: (iv_ruleRightsManagement= ruleRightsManagement EOF )
            // InternalCQLParser.g:4643:2: iv_ruleRightsManagement= ruleRightsManagement EOF
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
    // InternalCQLParser.g:4649:1: ruleRightsManagement returns [EObject current=null] : ( ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) ) | ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) ) ) ;
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
            // InternalCQLParser.g:4655:2: ( ( ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) ) | ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) ) ) )
            // InternalCQLParser.g:4656:2: ( ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) ) | ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) ) )
            {
            // InternalCQLParser.g:4656:2: ( ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) ) | ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) ) )
            int alt72=2;
            int LA72_0 = input.LA(1);

            if ( (LA72_0==GRANT) ) {
                alt72=1;
            }
            else if ( (LA72_0==REVOKE) ) {
                alt72=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 72, 0, input);

                throw nvae;
            }
            switch (alt72) {
                case 1 :
                    // InternalCQLParser.g:4657:3: ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) )
                    {
                    // InternalCQLParser.g:4657:3: ( () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) ) )
                    // InternalCQLParser.g:4658:4: () ( (lv_name_1_0= GRANT ) ) ( (lv_operations_2_0= RULE_ID ) ) (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )* (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )? otherlv_9= TO ( (lv_user_10_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:4658:4: ()
                    // InternalCQLParser.g:4659:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getRightsManagementAccess().getRightsManagementAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4665:4: ( (lv_name_1_0= GRANT ) )
                    // InternalCQLParser.g:4666:5: (lv_name_1_0= GRANT )
                    {
                    // InternalCQLParser.g:4666:5: (lv_name_1_0= GRANT )
                    // InternalCQLParser.g:4667:6: lv_name_1_0= GRANT
                    {
                    lv_name_1_0=(Token)match(input,GRANT,FOLLOW_5); 

                    						newLeafNode(lv_name_1_0, grammarAccess.getRightsManagementAccess().getNameGRANTKeyword_0_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_0, "GRANT");
                    					

                    }


                    }

                    // InternalCQLParser.g:4679:4: ( (lv_operations_2_0= RULE_ID ) )
                    // InternalCQLParser.g:4680:5: (lv_operations_2_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4680:5: (lv_operations_2_0= RULE_ID )
                    // InternalCQLParser.g:4681:6: lv_operations_2_0= RULE_ID
                    {
                    lv_operations_2_0=(Token)match(input,RULE_ID,FOLLOW_72); 

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

                    // InternalCQLParser.g:4697:4: (otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) ) )*
                    loop66:
                    do {
                        int alt66=2;
                        int LA66_0 = input.LA(1);

                        if ( (LA66_0==Comma) ) {
                            alt66=1;
                        }


                        switch (alt66) {
                    	case 1 :
                    	    // InternalCQLParser.g:4698:5: otherlv_3= Comma ( (lv_operations_4_0= RULE_ID ) )
                    	    {
                    	    otherlv_3=(Token)match(input,Comma,FOLLOW_5); 

                    	    					newLeafNode(otherlv_3, grammarAccess.getRightsManagementAccess().getCommaKeyword_0_3_0());
                    	    				
                    	    // InternalCQLParser.g:4702:5: ( (lv_operations_4_0= RULE_ID ) )
                    	    // InternalCQLParser.g:4703:6: (lv_operations_4_0= RULE_ID )
                    	    {
                    	    // InternalCQLParser.g:4703:6: (lv_operations_4_0= RULE_ID )
                    	    // InternalCQLParser.g:4704:7: lv_operations_4_0= RULE_ID
                    	    {
                    	    lv_operations_4_0=(Token)match(input,RULE_ID,FOLLOW_72); 

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
                    	    break loop66;
                        }
                    } while (true);

                    // InternalCQLParser.g:4721:4: (otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )* )?
                    int alt68=2;
                    int LA68_0 = input.LA(1);

                    if ( (LA68_0==ON) ) {
                        alt68=1;
                    }
                    switch (alt68) {
                        case 1 :
                            // InternalCQLParser.g:4722:5: otherlv_5= ON ( (lv_operations2_6_0= RULE_ID ) ) (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )*
                            {
                            otherlv_5=(Token)match(input,ON,FOLLOW_5); 

                            					newLeafNode(otherlv_5, grammarAccess.getRightsManagementAccess().getONKeyword_0_4_0());
                            				
                            // InternalCQLParser.g:4726:5: ( (lv_operations2_6_0= RULE_ID ) )
                            // InternalCQLParser.g:4727:6: (lv_operations2_6_0= RULE_ID )
                            {
                            // InternalCQLParser.g:4727:6: (lv_operations2_6_0= RULE_ID )
                            // InternalCQLParser.g:4728:7: lv_operations2_6_0= RULE_ID
                            {
                            lv_operations2_6_0=(Token)match(input,RULE_ID,FOLLOW_73); 

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

                            // InternalCQLParser.g:4744:5: (otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) ) )*
                            loop67:
                            do {
                                int alt67=2;
                                int LA67_0 = input.LA(1);

                                if ( (LA67_0==Comma) ) {
                                    alt67=1;
                                }


                                switch (alt67) {
                            	case 1 :
                            	    // InternalCQLParser.g:4745:6: otherlv_7= Comma ( (lv_operations2_8_0= RULE_ID ) )
                            	    {
                            	    otherlv_7=(Token)match(input,Comma,FOLLOW_5); 

                            	    						newLeafNode(otherlv_7, grammarAccess.getRightsManagementAccess().getCommaKeyword_0_4_2_0());
                            	    					
                            	    // InternalCQLParser.g:4749:6: ( (lv_operations2_8_0= RULE_ID ) )
                            	    // InternalCQLParser.g:4750:7: (lv_operations2_8_0= RULE_ID )
                            	    {
                            	    // InternalCQLParser.g:4750:7: (lv_operations2_8_0= RULE_ID )
                            	    // InternalCQLParser.g:4751:8: lv_operations2_8_0= RULE_ID
                            	    {
                            	    lv_operations2_8_0=(Token)match(input,RULE_ID,FOLLOW_73); 

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
                            	    break loop67;
                                }
                            } while (true);


                            }
                            break;

                    }

                    otherlv_9=(Token)match(input,TO,FOLLOW_5); 

                    				newLeafNode(otherlv_9, grammarAccess.getRightsManagementAccess().getTOKeyword_0_5());
                    			
                    // InternalCQLParser.g:4773:4: ( (lv_user_10_0= RULE_ID ) )
                    // InternalCQLParser.g:4774:5: (lv_user_10_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4774:5: (lv_user_10_0= RULE_ID )
                    // InternalCQLParser.g:4775:6: lv_user_10_0= RULE_ID
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
                    // InternalCQLParser.g:4793:3: ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) )
                    {
                    // InternalCQLParser.g:4793:3: ( () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) ) )
                    // InternalCQLParser.g:4794:4: () ( (lv_name_12_0= REVOKE ) ) ( (lv_operations_13_0= RULE_ID ) ) (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )* (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )? otherlv_20= FROM ( (lv_user_21_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:4794:4: ()
                    // InternalCQLParser.g:4795:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getRightsManagementAccess().getRightsManagementAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4801:4: ( (lv_name_12_0= REVOKE ) )
                    // InternalCQLParser.g:4802:5: (lv_name_12_0= REVOKE )
                    {
                    // InternalCQLParser.g:4802:5: (lv_name_12_0= REVOKE )
                    // InternalCQLParser.g:4803:6: lv_name_12_0= REVOKE
                    {
                    lv_name_12_0=(Token)match(input,REVOKE,FOLLOW_5); 

                    						newLeafNode(lv_name_12_0, grammarAccess.getRightsManagementAccess().getNameREVOKEKeyword_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRightsManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_12_0, "REVOKE");
                    					

                    }


                    }

                    // InternalCQLParser.g:4815:4: ( (lv_operations_13_0= RULE_ID ) )
                    // InternalCQLParser.g:4816:5: (lv_operations_13_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4816:5: (lv_operations_13_0= RULE_ID )
                    // InternalCQLParser.g:4817:6: lv_operations_13_0= RULE_ID
                    {
                    lv_operations_13_0=(Token)match(input,RULE_ID,FOLLOW_74); 

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

                    // InternalCQLParser.g:4833:4: (otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) ) )*
                    loop69:
                    do {
                        int alt69=2;
                        int LA69_0 = input.LA(1);

                        if ( (LA69_0==Comma) ) {
                            alt69=1;
                        }


                        switch (alt69) {
                    	case 1 :
                    	    // InternalCQLParser.g:4834:5: otherlv_14= Comma ( (lv_operations_15_0= RULE_ID ) )
                    	    {
                    	    otherlv_14=(Token)match(input,Comma,FOLLOW_5); 

                    	    					newLeafNode(otherlv_14, grammarAccess.getRightsManagementAccess().getCommaKeyword_1_3_0());
                    	    				
                    	    // InternalCQLParser.g:4838:5: ( (lv_operations_15_0= RULE_ID ) )
                    	    // InternalCQLParser.g:4839:6: (lv_operations_15_0= RULE_ID )
                    	    {
                    	    // InternalCQLParser.g:4839:6: (lv_operations_15_0= RULE_ID )
                    	    // InternalCQLParser.g:4840:7: lv_operations_15_0= RULE_ID
                    	    {
                    	    lv_operations_15_0=(Token)match(input,RULE_ID,FOLLOW_74); 

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
                    	    break loop69;
                        }
                    } while (true);

                    // InternalCQLParser.g:4857:4: (otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )* )?
                    int alt71=2;
                    int LA71_0 = input.LA(1);

                    if ( (LA71_0==ON) ) {
                        alt71=1;
                    }
                    switch (alt71) {
                        case 1 :
                            // InternalCQLParser.g:4858:5: otherlv_16= ON ( (lv_operations2_17_0= RULE_ID ) ) (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )*
                            {
                            otherlv_16=(Token)match(input,ON,FOLLOW_5); 

                            					newLeafNode(otherlv_16, grammarAccess.getRightsManagementAccess().getONKeyword_1_4_0());
                            				
                            // InternalCQLParser.g:4862:5: ( (lv_operations2_17_0= RULE_ID ) )
                            // InternalCQLParser.g:4863:6: (lv_operations2_17_0= RULE_ID )
                            {
                            // InternalCQLParser.g:4863:6: (lv_operations2_17_0= RULE_ID )
                            // InternalCQLParser.g:4864:7: lv_operations2_17_0= RULE_ID
                            {
                            lv_operations2_17_0=(Token)match(input,RULE_ID,FOLLOW_13); 

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

                            // InternalCQLParser.g:4880:5: (otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) ) )*
                            loop70:
                            do {
                                int alt70=2;
                                int LA70_0 = input.LA(1);

                                if ( (LA70_0==Comma) ) {
                                    alt70=1;
                                }


                                switch (alt70) {
                            	case 1 :
                            	    // InternalCQLParser.g:4881:6: otherlv_18= Comma ( (lv_operations2_19_0= RULE_ID ) )
                            	    {
                            	    otherlv_18=(Token)match(input,Comma,FOLLOW_5); 

                            	    						newLeafNode(otherlv_18, grammarAccess.getRightsManagementAccess().getCommaKeyword_1_4_2_0());
                            	    					
                            	    // InternalCQLParser.g:4885:6: ( (lv_operations2_19_0= RULE_ID ) )
                            	    // InternalCQLParser.g:4886:7: (lv_operations2_19_0= RULE_ID )
                            	    {
                            	    // InternalCQLParser.g:4886:7: (lv_operations2_19_0= RULE_ID )
                            	    // InternalCQLParser.g:4887:8: lv_operations2_19_0= RULE_ID
                            	    {
                            	    lv_operations2_19_0=(Token)match(input,RULE_ID,FOLLOW_13); 

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
                            	    break loop70;
                                }
                            } while (true);


                            }
                            break;

                    }

                    otherlv_20=(Token)match(input,FROM,FOLLOW_5); 

                    				newLeafNode(otherlv_20, grammarAccess.getRightsManagementAccess().getFROMKeyword_1_5());
                    			
                    // InternalCQLParser.g:4909:4: ( (lv_user_21_0= RULE_ID ) )
                    // InternalCQLParser.g:4910:5: (lv_user_21_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4910:5: (lv_user_21_0= RULE_ID )
                    // InternalCQLParser.g:4911:6: lv_user_21_0= RULE_ID
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
    // InternalCQLParser.g:4932:1: entryRuleRoleManagement returns [EObject current=null] : iv_ruleRoleManagement= ruleRoleManagement EOF ;
    public final EObject entryRuleRoleManagement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRoleManagement = null;


        try {
            // InternalCQLParser.g:4932:55: (iv_ruleRoleManagement= ruleRoleManagement EOF )
            // InternalCQLParser.g:4933:2: iv_ruleRoleManagement= ruleRoleManagement EOF
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
    // InternalCQLParser.g:4939:1: ruleRoleManagement returns [EObject current=null] : ( ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) ) | ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) ) ) ;
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
            // InternalCQLParser.g:4945:2: ( ( ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) ) | ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) ) ) )
            // InternalCQLParser.g:4946:2: ( ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) ) | ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) ) )
            {
            // InternalCQLParser.g:4946:2: ( ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) ) | ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) ) )
            int alt75=2;
            int LA75_0 = input.LA(1);

            if ( (LA75_0==GRANT) ) {
                alt75=1;
            }
            else if ( (LA75_0==REVOKE) ) {
                alt75=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 75, 0, input);

                throw nvae;
            }
            switch (alt75) {
                case 1 :
                    // InternalCQLParser.g:4947:3: ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) )
                    {
                    // InternalCQLParser.g:4947:3: ( () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) ) )
                    // InternalCQLParser.g:4948:4: () ( (lv_name_1_0= GRANT ) ) otherlv_2= ROLE ( (lv_operations_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )* otherlv_6= TO ( (lv_user_7_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:4948:4: ()
                    // InternalCQLParser.g:4949:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getRoleManagementAccess().getRoleManagementAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4955:4: ( (lv_name_1_0= GRANT ) )
                    // InternalCQLParser.g:4956:5: (lv_name_1_0= GRANT )
                    {
                    // InternalCQLParser.g:4956:5: (lv_name_1_0= GRANT )
                    // InternalCQLParser.g:4957:6: lv_name_1_0= GRANT
                    {
                    lv_name_1_0=(Token)match(input,GRANT,FOLLOW_75); 

                    						newLeafNode(lv_name_1_0, grammarAccess.getRoleManagementAccess().getNameGRANTKeyword_0_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRoleManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_0, "GRANT");
                    					

                    }


                    }

                    otherlv_2=(Token)match(input,ROLE,FOLLOW_5); 

                    				newLeafNode(otherlv_2, grammarAccess.getRoleManagementAccess().getROLEKeyword_0_2());
                    			
                    // InternalCQLParser.g:4973:4: ( (lv_operations_3_0= RULE_ID ) )
                    // InternalCQLParser.g:4974:5: (lv_operations_3_0= RULE_ID )
                    {
                    // InternalCQLParser.g:4974:5: (lv_operations_3_0= RULE_ID )
                    // InternalCQLParser.g:4975:6: lv_operations_3_0= RULE_ID
                    {
                    lv_operations_3_0=(Token)match(input,RULE_ID,FOLLOW_73); 

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

                    // InternalCQLParser.g:4991:4: (otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) ) )*
                    loop73:
                    do {
                        int alt73=2;
                        int LA73_0 = input.LA(1);

                        if ( (LA73_0==Comma) ) {
                            alt73=1;
                        }


                        switch (alt73) {
                    	case 1 :
                    	    // InternalCQLParser.g:4992:5: otherlv_4= Comma ( (lv_operations_5_0= RULE_ID ) )
                    	    {
                    	    otherlv_4=(Token)match(input,Comma,FOLLOW_5); 

                    	    					newLeafNode(otherlv_4, grammarAccess.getRoleManagementAccess().getCommaKeyword_0_4_0());
                    	    				
                    	    // InternalCQLParser.g:4996:5: ( (lv_operations_5_0= RULE_ID ) )
                    	    // InternalCQLParser.g:4997:6: (lv_operations_5_0= RULE_ID )
                    	    {
                    	    // InternalCQLParser.g:4997:6: (lv_operations_5_0= RULE_ID )
                    	    // InternalCQLParser.g:4998:7: lv_operations_5_0= RULE_ID
                    	    {
                    	    lv_operations_5_0=(Token)match(input,RULE_ID,FOLLOW_73); 

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
                    	    break loop73;
                        }
                    } while (true);

                    otherlv_6=(Token)match(input,TO,FOLLOW_5); 

                    				newLeafNode(otherlv_6, grammarAccess.getRoleManagementAccess().getTOKeyword_0_5());
                    			
                    // InternalCQLParser.g:5019:4: ( (lv_user_7_0= RULE_ID ) )
                    // InternalCQLParser.g:5020:5: (lv_user_7_0= RULE_ID )
                    {
                    // InternalCQLParser.g:5020:5: (lv_user_7_0= RULE_ID )
                    // InternalCQLParser.g:5021:6: lv_user_7_0= RULE_ID
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
                    // InternalCQLParser.g:5039:3: ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) )
                    {
                    // InternalCQLParser.g:5039:3: ( () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) ) )
                    // InternalCQLParser.g:5040:4: () ( (lv_name_9_0= REVOKE ) ) otherlv_10= ROLE ( (lv_operations_11_0= RULE_ID ) ) (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )* otherlv_14= FROM ( (lv_user_15_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:5040:4: ()
                    // InternalCQLParser.g:5041:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getRoleManagementAccess().getRoleManagementAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:5047:4: ( (lv_name_9_0= REVOKE ) )
                    // InternalCQLParser.g:5048:5: (lv_name_9_0= REVOKE )
                    {
                    // InternalCQLParser.g:5048:5: (lv_name_9_0= REVOKE )
                    // InternalCQLParser.g:5049:6: lv_name_9_0= REVOKE
                    {
                    lv_name_9_0=(Token)match(input,REVOKE,FOLLOW_75); 

                    						newLeafNode(lv_name_9_0, grammarAccess.getRoleManagementAccess().getNameREVOKEKeyword_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRoleManagementRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_9_0, "REVOKE");
                    					

                    }


                    }

                    otherlv_10=(Token)match(input,ROLE,FOLLOW_5); 

                    				newLeafNode(otherlv_10, grammarAccess.getRoleManagementAccess().getROLEKeyword_1_2());
                    			
                    // InternalCQLParser.g:5065:4: ( (lv_operations_11_0= RULE_ID ) )
                    // InternalCQLParser.g:5066:5: (lv_operations_11_0= RULE_ID )
                    {
                    // InternalCQLParser.g:5066:5: (lv_operations_11_0= RULE_ID )
                    // InternalCQLParser.g:5067:6: lv_operations_11_0= RULE_ID
                    {
                    lv_operations_11_0=(Token)match(input,RULE_ID,FOLLOW_13); 

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

                    // InternalCQLParser.g:5083:4: (otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) ) )*
                    loop74:
                    do {
                        int alt74=2;
                        int LA74_0 = input.LA(1);

                        if ( (LA74_0==Comma) ) {
                            alt74=1;
                        }


                        switch (alt74) {
                    	case 1 :
                    	    // InternalCQLParser.g:5084:5: otherlv_12= Comma ( (lv_operations_13_0= RULE_ID ) )
                    	    {
                    	    otherlv_12=(Token)match(input,Comma,FOLLOW_5); 

                    	    					newLeafNode(otherlv_12, grammarAccess.getRoleManagementAccess().getCommaKeyword_1_4_0());
                    	    				
                    	    // InternalCQLParser.g:5088:5: ( (lv_operations_13_0= RULE_ID ) )
                    	    // InternalCQLParser.g:5089:6: (lv_operations_13_0= RULE_ID )
                    	    {
                    	    // InternalCQLParser.g:5089:6: (lv_operations_13_0= RULE_ID )
                    	    // InternalCQLParser.g:5090:7: lv_operations_13_0= RULE_ID
                    	    {
                    	    lv_operations_13_0=(Token)match(input,RULE_ID,FOLLOW_13); 

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
                    	    break loop74;
                        }
                    } while (true);

                    otherlv_14=(Token)match(input,FROM,FOLLOW_5); 

                    				newLeafNode(otherlv_14, grammarAccess.getRoleManagementAccess().getFROMKeyword_1_5());
                    			
                    // InternalCQLParser.g:5111:4: ( (lv_user_15_0= RULE_ID ) )
                    // InternalCQLParser.g:5112:5: (lv_user_15_0= RULE_ID )
                    {
                    // InternalCQLParser.g:5112:5: (lv_user_15_0= RULE_ID )
                    // InternalCQLParser.g:5113:6: lv_user_15_0= RULE_ID
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
    // InternalCQLParser.g:5134:1: entryRuleWindowOperator returns [EObject current=null] : iv_ruleWindowOperator= ruleWindowOperator EOF ;
    public final EObject entryRuleWindowOperator() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindowOperator = null;


        try {
            // InternalCQLParser.g:5134:55: (iv_ruleWindowOperator= ruleWindowOperator EOF )
            // InternalCQLParser.g:5135:2: iv_ruleWindowOperator= ruleWindowOperator EOF
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
    // InternalCQLParser.g:5141:1: ruleWindowOperator returns [EObject current=null] : (otherlv_0= LeftSquareBracket (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow ) otherlv_4= RightSquareBracket ) ;
    public final EObject ruleWindowOperator() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_4=null;
        EObject this_UnboundedWindow_1 = null;

        EObject this_TimebasedWindow_2 = null;

        EObject this_TuplebasedWindow_3 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5147:2: ( (otherlv_0= LeftSquareBracket (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow ) otherlv_4= RightSquareBracket ) )
            // InternalCQLParser.g:5148:2: (otherlv_0= LeftSquareBracket (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow ) otherlv_4= RightSquareBracket )
            {
            // InternalCQLParser.g:5148:2: (otherlv_0= LeftSquareBracket (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow ) otherlv_4= RightSquareBracket )
            // InternalCQLParser.g:5149:3: otherlv_0= LeftSquareBracket (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow ) otherlv_4= RightSquareBracket
            {
            otherlv_0=(Token)match(input,LeftSquareBracket,FOLLOW_76); 

            			newLeafNode(otherlv_0, grammarAccess.getWindowOperatorAccess().getLeftSquareBracketKeyword_0());
            		
            // InternalCQLParser.g:5153:3: (this_UnboundedWindow_1= ruleUnboundedWindow | this_TimebasedWindow_2= ruleTimebasedWindow | this_TuplebasedWindow_3= ruleTuplebasedWindow )
            int alt76=3;
            int LA76_0 = input.LA(1);

            if ( (LA76_0==UNBOUNDED) ) {
                alt76=1;
            }
            else if ( (LA76_0==SIZE) ) {
                int LA76_2 = input.LA(2);

                if ( (LA76_2==RULE_INT) ) {
                    switch ( input.LA(3) ) {
                    case ADVANCE:
                        {
                        int LA76_4 = input.LA(4);

                        if ( (LA76_4==RULE_INT) ) {
                            int LA76_7 = input.LA(5);

                            if ( (LA76_7==TUPLE) ) {
                                alt76=3;
                            }
                            else if ( (LA76_7==MILLISECONDS||(LA76_7>=MILLISECOND && LA76_7<=NANOSECONDS)||LA76_7==NANOSECOND||LA76_7==MINUTES||LA76_7==SECONDS||LA76_7==MINUTE||LA76_7==SECOND||LA76_7==HOURS||LA76_7==WEEKS||LA76_7==HOUR||LA76_7==NULL||LA76_7==TIME||LA76_7==WEEK) ) {
                                alt76=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 76, 7, input);

                                throw nvae;
                            }
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 76, 4, input);

                            throw nvae;
                        }
                        }
                        break;
                    case TUPLE:
                        {
                        alt76=3;
                        }
                        break;
                    case MILLISECONDS:
                    case MILLISECOND:
                    case NANOSECONDS:
                    case NANOSECOND:
                    case MINUTES:
                    case SECONDS:
                    case MINUTE:
                    case SECOND:
                    case HOURS:
                    case WEEKS:
                    case HOUR:
                    case NULL:
                    case TIME:
                    case WEEK:
                        {
                        alt76=2;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 76, 3, input);

                        throw nvae;
                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 76, 2, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 76, 0, input);

                throw nvae;
            }
            switch (alt76) {
                case 1 :
                    // InternalCQLParser.g:5154:4: this_UnboundedWindow_1= ruleUnboundedWindow
                    {

                    				newCompositeNode(grammarAccess.getWindowOperatorAccess().getUnboundedWindowParserRuleCall_1_0());
                    			
                    pushFollow(FOLLOW_77);
                    this_UnboundedWindow_1=ruleUnboundedWindow();

                    state._fsp--;


                    				current = this_UnboundedWindow_1;
                    				afterParserOrEnumRuleCall();
                    			

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:5163:4: this_TimebasedWindow_2= ruleTimebasedWindow
                    {

                    				newCompositeNode(grammarAccess.getWindowOperatorAccess().getTimebasedWindowParserRuleCall_1_1());
                    			
                    pushFollow(FOLLOW_77);
                    this_TimebasedWindow_2=ruleTimebasedWindow();

                    state._fsp--;


                    				current = this_TimebasedWindow_2;
                    				afterParserOrEnumRuleCall();
                    			

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:5172:4: this_TuplebasedWindow_3= ruleTuplebasedWindow
                    {

                    				newCompositeNode(grammarAccess.getWindowOperatorAccess().getTuplebasedWindowParserRuleCall_1_2());
                    			
                    pushFollow(FOLLOW_77);
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
    // InternalCQLParser.g:5189:1: entryRuleUnboundedWindow returns [EObject current=null] : iv_ruleUnboundedWindow= ruleUnboundedWindow EOF ;
    public final EObject entryRuleUnboundedWindow() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleUnboundedWindow = null;


        try {
            // InternalCQLParser.g:5189:56: (iv_ruleUnboundedWindow= ruleUnboundedWindow EOF )
            // InternalCQLParser.g:5190:2: iv_ruleUnboundedWindow= ruleUnboundedWindow EOF
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
    // InternalCQLParser.g:5196:1: ruleUnboundedWindow returns [EObject current=null] : ( () otherlv_1= UNBOUNDED ) ;
    public final EObject ruleUnboundedWindow() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;


        	enterRule();

        try {
            // InternalCQLParser.g:5202:2: ( ( () otherlv_1= UNBOUNDED ) )
            // InternalCQLParser.g:5203:2: ( () otherlv_1= UNBOUNDED )
            {
            // InternalCQLParser.g:5203:2: ( () otherlv_1= UNBOUNDED )
            // InternalCQLParser.g:5204:3: () otherlv_1= UNBOUNDED
            {
            // InternalCQLParser.g:5204:3: ()
            // InternalCQLParser.g:5205:4: 
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
    // InternalCQLParser.g:5219:1: entryRuleTimebasedWindow returns [EObject current=null] : iv_ruleTimebasedWindow= ruleTimebasedWindow EOF ;
    public final EObject entryRuleTimebasedWindow() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTimebasedWindow = null;


        try {
            // InternalCQLParser.g:5219:56: (iv_ruleTimebasedWindow= ruleTimebasedWindow EOF )
            // InternalCQLParser.g:5220:2: iv_ruleTimebasedWindow= ruleTimebasedWindow EOF
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
    // InternalCQLParser.g:5226:1: ruleTimebasedWindow returns [EObject current=null] : ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) ( (lv_unit_3_0= ruleTime ) )? (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= ruleTime ) )? )? otherlv_7= TIME ) ;
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
            // InternalCQLParser.g:5232:2: ( ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) ( (lv_unit_3_0= ruleTime ) )? (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= ruleTime ) )? )? otherlv_7= TIME ) )
            // InternalCQLParser.g:5233:2: ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) ( (lv_unit_3_0= ruleTime ) )? (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= ruleTime ) )? )? otherlv_7= TIME )
            {
            // InternalCQLParser.g:5233:2: ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) ( (lv_unit_3_0= ruleTime ) )? (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= ruleTime ) )? )? otherlv_7= TIME )
            // InternalCQLParser.g:5234:3: () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) ( (lv_unit_3_0= ruleTime ) )? (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= ruleTime ) )? )? otherlv_7= TIME
            {
            // InternalCQLParser.g:5234:3: ()
            // InternalCQLParser.g:5235:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getTimebasedWindowAccess().getTimebasedWindowAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,SIZE,FOLLOW_47); 

            			newLeafNode(otherlv_1, grammarAccess.getTimebasedWindowAccess().getSIZEKeyword_1());
            		
            // InternalCQLParser.g:5245:3: ( (lv_size_2_0= RULE_INT ) )
            // InternalCQLParser.g:5246:4: (lv_size_2_0= RULE_INT )
            {
            // InternalCQLParser.g:5246:4: (lv_size_2_0= RULE_INT )
            // InternalCQLParser.g:5247:5: lv_size_2_0= RULE_INT
            {
            lv_size_2_0=(Token)match(input,RULE_INT,FOLLOW_78); 

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

            // InternalCQLParser.g:5263:3: ( (lv_unit_3_0= ruleTime ) )?
            int alt77=2;
            int LA77_0 = input.LA(1);

            if ( (LA77_0==MILLISECONDS||(LA77_0>=MILLISECOND && LA77_0<=NANOSECONDS)||LA77_0==NANOSECOND||LA77_0==MINUTES||LA77_0==SECONDS||LA77_0==MINUTE||LA77_0==SECOND||LA77_0==HOURS||LA77_0==WEEKS||LA77_0==HOUR||LA77_0==NULL||LA77_0==WEEK) ) {
                alt77=1;
            }
            switch (alt77) {
                case 1 :
                    // InternalCQLParser.g:5264:4: (lv_unit_3_0= ruleTime )
                    {
                    // InternalCQLParser.g:5264:4: (lv_unit_3_0= ruleTime )
                    // InternalCQLParser.g:5265:5: lv_unit_3_0= ruleTime
                    {

                    					newCompositeNode(grammarAccess.getTimebasedWindowAccess().getUnitTimeEnumRuleCall_3_0());
                    				
                    pushFollow(FOLLOW_79);
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
                    break;

            }

            // InternalCQLParser.g:5282:3: (otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= ruleTime ) )? )?
            int alt79=2;
            int LA79_0 = input.LA(1);

            if ( (LA79_0==ADVANCE) ) {
                alt79=1;
            }
            switch (alt79) {
                case 1 :
                    // InternalCQLParser.g:5283:4: otherlv_4= ADVANCE ( (lv_advance_size_5_0= RULE_INT ) ) ( (lv_advance_unit_6_0= ruleTime ) )?
                    {
                    otherlv_4=(Token)match(input,ADVANCE,FOLLOW_47); 

                    				newLeafNode(otherlv_4, grammarAccess.getTimebasedWindowAccess().getADVANCEKeyword_4_0());
                    			
                    // InternalCQLParser.g:5287:4: ( (lv_advance_size_5_0= RULE_INT ) )
                    // InternalCQLParser.g:5288:5: (lv_advance_size_5_0= RULE_INT )
                    {
                    // InternalCQLParser.g:5288:5: (lv_advance_size_5_0= RULE_INT )
                    // InternalCQLParser.g:5289:6: lv_advance_size_5_0= RULE_INT
                    {
                    lv_advance_size_5_0=(Token)match(input,RULE_INT,FOLLOW_80); 

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

                    // InternalCQLParser.g:5305:4: ( (lv_advance_unit_6_0= ruleTime ) )?
                    int alt78=2;
                    int LA78_0 = input.LA(1);

                    if ( (LA78_0==MILLISECONDS||(LA78_0>=MILLISECOND && LA78_0<=NANOSECONDS)||LA78_0==NANOSECOND||LA78_0==MINUTES||LA78_0==SECONDS||LA78_0==MINUTE||LA78_0==SECOND||LA78_0==HOURS||LA78_0==WEEKS||LA78_0==HOUR||LA78_0==NULL||LA78_0==WEEK) ) {
                        alt78=1;
                    }
                    switch (alt78) {
                        case 1 :
                            // InternalCQLParser.g:5306:5: (lv_advance_unit_6_0= ruleTime )
                            {
                            // InternalCQLParser.g:5306:5: (lv_advance_unit_6_0= ruleTime )
                            // InternalCQLParser.g:5307:6: lv_advance_unit_6_0= ruleTime
                            {

                            						newCompositeNode(grammarAccess.getTimebasedWindowAccess().getAdvance_unitTimeEnumRuleCall_4_2_0());
                            					
                            pushFollow(FOLLOW_81);
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
                            break;

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
    // InternalCQLParser.g:5333:1: entryRuleTuplebasedWindow returns [EObject current=null] : iv_ruleTuplebasedWindow= ruleTuplebasedWindow EOF ;
    public final EObject entryRuleTuplebasedWindow() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTuplebasedWindow = null;


        try {
            // InternalCQLParser.g:5333:57: (iv_ruleTuplebasedWindow= ruleTuplebasedWindow EOF )
            // InternalCQLParser.g:5334:2: iv_ruleTuplebasedWindow= ruleTuplebasedWindow EOF
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
    // InternalCQLParser.g:5340:1: ruleTuplebasedWindow returns [EObject current=null] : ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )? otherlv_5= TUPLE (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )? ) ;
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
            // InternalCQLParser.g:5346:2: ( ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )? otherlv_5= TUPLE (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )? ) )
            // InternalCQLParser.g:5347:2: ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )? otherlv_5= TUPLE (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )? )
            {
            // InternalCQLParser.g:5347:2: ( () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )? otherlv_5= TUPLE (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )? )
            // InternalCQLParser.g:5348:3: () otherlv_1= SIZE ( (lv_size_2_0= RULE_INT ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )? otherlv_5= TUPLE (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )?
            {
            // InternalCQLParser.g:5348:3: ()
            // InternalCQLParser.g:5349:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getTuplebasedWindowAccess().getTuplebasedWindowAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,SIZE,FOLLOW_47); 

            			newLeafNode(otherlv_1, grammarAccess.getTuplebasedWindowAccess().getSIZEKeyword_1());
            		
            // InternalCQLParser.g:5359:3: ( (lv_size_2_0= RULE_INT ) )
            // InternalCQLParser.g:5360:4: (lv_size_2_0= RULE_INT )
            {
            // InternalCQLParser.g:5360:4: (lv_size_2_0= RULE_INT )
            // InternalCQLParser.g:5361:5: lv_size_2_0= RULE_INT
            {
            lv_size_2_0=(Token)match(input,RULE_INT,FOLLOW_82); 

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

            // InternalCQLParser.g:5377:3: (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) )?
            int alt80=2;
            int LA80_0 = input.LA(1);

            if ( (LA80_0==ADVANCE) ) {
                alt80=1;
            }
            switch (alt80) {
                case 1 :
                    // InternalCQLParser.g:5378:4: otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) )
                    {
                    otherlv_3=(Token)match(input,ADVANCE,FOLLOW_47); 

                    				newLeafNode(otherlv_3, grammarAccess.getTuplebasedWindowAccess().getADVANCEKeyword_3_0());
                    			
                    // InternalCQLParser.g:5382:4: ( (lv_advance_size_4_0= RULE_INT ) )
                    // InternalCQLParser.g:5383:5: (lv_advance_size_4_0= RULE_INT )
                    {
                    // InternalCQLParser.g:5383:5: (lv_advance_size_4_0= RULE_INT )
                    // InternalCQLParser.g:5384:6: lv_advance_size_4_0= RULE_INT
                    {
                    lv_advance_size_4_0=(Token)match(input,RULE_INT,FOLLOW_83); 

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

            otherlv_5=(Token)match(input,TUPLE,FOLLOW_63); 

            			newLeafNode(otherlv_5, grammarAccess.getTuplebasedWindowAccess().getTUPLEKeyword_4());
            		
            // InternalCQLParser.g:5405:3: (otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) ) )?
            int alt81=2;
            int LA81_0 = input.LA(1);

            if ( (LA81_0==PARTITION) ) {
                alt81=1;
            }
            switch (alt81) {
                case 1 :
                    // InternalCQLParser.g:5406:4: otherlv_6= PARTITION otherlv_7= BY ( (lv_partition_attribute_8_0= ruleAttribute ) )
                    {
                    otherlv_6=(Token)match(input,PARTITION,FOLLOW_19); 

                    				newLeafNode(otherlv_6, grammarAccess.getTuplebasedWindowAccess().getPARTITIONKeyword_5_0());
                    			
                    otherlv_7=(Token)match(input,BY,FOLLOW_20); 

                    				newLeafNode(otherlv_7, grammarAccess.getTuplebasedWindowAccess().getBYKeyword_5_1());
                    			
                    // InternalCQLParser.g:5414:4: ( (lv_partition_attribute_8_0= ruleAttribute ) )
                    // InternalCQLParser.g:5415:5: (lv_partition_attribute_8_0= ruleAttribute )
                    {
                    // InternalCQLParser.g:5415:5: (lv_partition_attribute_8_0= ruleAttribute )
                    // InternalCQLParser.g:5416:6: lv_partition_attribute_8_0= ruleAttribute
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
    // InternalCQLParser.g:5438:1: entryRuleExpressionsModel returns [EObject current=null] : iv_ruleExpressionsModel= ruleExpressionsModel EOF ;
    public final EObject entryRuleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionsModel = null;


        try {
            // InternalCQLParser.g:5438:57: (iv_ruleExpressionsModel= ruleExpressionsModel EOF )
            // InternalCQLParser.g:5439:2: iv_ruleExpressionsModel= ruleExpressionsModel EOF
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
    // InternalCQLParser.g:5445:1: ruleExpressionsModel returns [EObject current=null] : ( () ( (lv_elements_1_0= ruleExpression ) ) ) ;
    public final EObject ruleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5451:2: ( ( () ( (lv_elements_1_0= ruleExpression ) ) ) )
            // InternalCQLParser.g:5452:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            {
            // InternalCQLParser.g:5452:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            // InternalCQLParser.g:5453:3: () ( (lv_elements_1_0= ruleExpression ) )
            {
            // InternalCQLParser.g:5453:3: ()
            // InternalCQLParser.g:5454:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:5460:3: ( (lv_elements_1_0= ruleExpression ) )
            // InternalCQLParser.g:5461:4: (lv_elements_1_0= ruleExpression )
            {
            // InternalCQLParser.g:5461:4: (lv_elements_1_0= ruleExpression )
            // InternalCQLParser.g:5462:5: lv_elements_1_0= ruleExpression
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
    // InternalCQLParser.g:5483:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCQLParser.g:5483:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCQLParser.g:5484:2: iv_ruleExpression= ruleExpression EOF
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
    // InternalCQLParser.g:5490:1: ruleExpression returns [EObject current=null] : this_OrPredicate_0= ruleOrPredicate ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_OrPredicate_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5496:2: (this_OrPredicate_0= ruleOrPredicate )
            // InternalCQLParser.g:5497:2: this_OrPredicate_0= ruleOrPredicate
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
    // InternalCQLParser.g:5508:1: entryRuleOrPredicate returns [EObject current=null] : iv_ruleOrPredicate= ruleOrPredicate EOF ;
    public final EObject entryRuleOrPredicate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOrPredicate = null;


        try {
            // InternalCQLParser.g:5508:52: (iv_ruleOrPredicate= ruleOrPredicate EOF )
            // InternalCQLParser.g:5509:2: iv_ruleOrPredicate= ruleOrPredicate EOF
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
    // InternalCQLParser.g:5515:1: ruleOrPredicate returns [EObject current=null] : (this_AndPredicate_0= ruleAndPredicate ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )* ) ;
    public final EObject ruleOrPredicate() throws RecognitionException {
        EObject current = null;

        EObject this_AndPredicate_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5521:2: ( (this_AndPredicate_0= ruleAndPredicate ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )* ) )
            // InternalCQLParser.g:5522:2: (this_AndPredicate_0= ruleAndPredicate ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )* )
            {
            // InternalCQLParser.g:5522:2: (this_AndPredicate_0= ruleAndPredicate ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )* )
            // InternalCQLParser.g:5523:3: this_AndPredicate_0= ruleAndPredicate ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrPredicateAccess().getAndPredicateParserRuleCall_0());
            		
            pushFollow(FOLLOW_84);
            this_AndPredicate_0=ruleAndPredicate();

            state._fsp--;


            			current = this_AndPredicate_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:5531:3: ( () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) ) )*
            loop82:
            do {
                int alt82=2;
                int LA82_0 = input.LA(1);

                if ( (LA82_0==OR) ) {
                    alt82=1;
                }


                switch (alt82) {
            	case 1 :
            	    // InternalCQLParser.g:5532:4: () ruleOrOperator ( (lv_right_3_0= ruleAndPredicate ) )
            	    {
            	    // InternalCQLParser.g:5532:4: ()
            	    // InternalCQLParser.g:5533:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrPredicateAccess().getOrPredicateLeftAction_1_0(),
            	    						current);
            	    				

            	    }


            	    				newCompositeNode(grammarAccess.getOrPredicateAccess().getOrOperatorParserRuleCall_1_1());
            	    			
            	    pushFollow(FOLLOW_17);
            	    ruleOrOperator();

            	    state._fsp--;


            	    				afterParserOrEnumRuleCall();
            	    			
            	    // InternalCQLParser.g:5546:4: ( (lv_right_3_0= ruleAndPredicate ) )
            	    // InternalCQLParser.g:5547:5: (lv_right_3_0= ruleAndPredicate )
            	    {
            	    // InternalCQLParser.g:5547:5: (lv_right_3_0= ruleAndPredicate )
            	    // InternalCQLParser.g:5548:6: lv_right_3_0= ruleAndPredicate
            	    {

            	    						newCompositeNode(grammarAccess.getOrPredicateAccess().getRightAndPredicateParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_84);
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
    // $ANTLR end "ruleOrPredicate"


    // $ANTLR start "entryRuleAndPredicate"
    // InternalCQLParser.g:5570:1: entryRuleAndPredicate returns [EObject current=null] : iv_ruleAndPredicate= ruleAndPredicate EOF ;
    public final EObject entryRuleAndPredicate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAndPredicate = null;


        try {
            // InternalCQLParser.g:5570:53: (iv_ruleAndPredicate= ruleAndPredicate EOF )
            // InternalCQLParser.g:5571:2: iv_ruleAndPredicate= ruleAndPredicate EOF
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
    // InternalCQLParser.g:5577:1: ruleAndPredicate returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAndPredicate() throws RecognitionException {
        EObject current = null;

        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5583:2: ( (this_Equalitiy_0= ruleEqualitiy ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCQLParser.g:5584:2: (this_Equalitiy_0= ruleEqualitiy ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCQLParser.g:5584:2: (this_Equalitiy_0= ruleEqualitiy ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCQLParser.g:5585:3: this_Equalitiy_0= ruleEqualitiy ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndPredicateAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_53);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:5593:3: ( () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop83:
            do {
                int alt83=2;
                int LA83_0 = input.LA(1);

                if ( (LA83_0==AND) ) {
                    alt83=1;
                }


                switch (alt83) {
            	case 1 :
            	    // InternalCQLParser.g:5594:4: () ruleAndOperator ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCQLParser.g:5594:4: ()
            	    // InternalCQLParser.g:5595:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndPredicateAccess().getAndPredicateLeftAction_1_0(),
            	    						current);
            	    				

            	    }


            	    				newCompositeNode(grammarAccess.getAndPredicateAccess().getAndOperatorParserRuleCall_1_1());
            	    			
            	    pushFollow(FOLLOW_17);
            	    ruleAndOperator();

            	    state._fsp--;


            	    				afterParserOrEnumRuleCall();
            	    			
            	    // InternalCQLParser.g:5608:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCQLParser.g:5609:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCQLParser.g:5609:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCQLParser.g:5610:6: lv_right_3_0= ruleEqualitiy
            	    {

            	    						newCompositeNode(grammarAccess.getAndPredicateAccess().getRightEqualitiyParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_53);
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
            	    break loop83;
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
    // InternalCQLParser.g:5632:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCQLParser.g:5632:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCQLParser.g:5633:2: iv_ruleEqualitiy= ruleEqualitiy EOF
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
    // InternalCQLParser.g:5639:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject this_Comparison_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5645:2: ( (this_Comparison_0= ruleComparison ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCQLParser.g:5646:2: (this_Comparison_0= ruleComparison ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCQLParser.g:5646:2: (this_Comparison_0= ruleComparison ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCQLParser.g:5647:3: this_Comparison_0= ruleComparison ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_85);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:5655:3: ( () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop84:
            do {
                int alt84=2;
                int LA84_0 = input.LA(1);

                if ( (LA84_0==ExclamationMarkEqualsSign||LA84_0==EqualsSign) ) {
                    alt84=1;
                }


                switch (alt84) {
            	case 1 :
            	    // InternalCQLParser.g:5656:4: () ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCQLParser.g:5656:4: ()
            	    // InternalCQLParser.g:5657:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:5663:4: ( (lv_op_2_0= ruleEQUALITIY_OPERATOR ) )
            	    // InternalCQLParser.g:5664:5: (lv_op_2_0= ruleEQUALITIY_OPERATOR )
            	    {
            	    // InternalCQLParser.g:5664:5: (lv_op_2_0= ruleEQUALITIY_OPERATOR )
            	    // InternalCQLParser.g:5665:6: lv_op_2_0= ruleEQUALITIY_OPERATOR
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getOpEQUALITIY_OPERATORParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_17);
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

            	    // InternalCQLParser.g:5682:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCQLParser.g:5683:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCQLParser.g:5683:5: (lv_right_3_0= ruleComparison )
            	    // InternalCQLParser.g:5684:6: lv_right_3_0= ruleComparison
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getRightComparisonParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_85);
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
    // $ANTLR end "ruleEqualitiy"


    // $ANTLR start "entryRuleComparison"
    // InternalCQLParser.g:5706:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCQLParser.g:5706:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCQLParser.g:5707:2: iv_ruleComparison= ruleComparison EOF
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
    // InternalCQLParser.g:5713:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
    public final EObject ruleComparison() throws RecognitionException {
        EObject current = null;

        EObject this_PlusOrMinus_0 = null;

        AntlrDatatypeRuleToken lv_op_2_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5719:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCQLParser.g:5720:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCQLParser.g:5720:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCQLParser.g:5721:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_86);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:5729:3: ( () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop85:
            do {
                int alt85=2;
                int LA85_0 = input.LA(1);

                if ( ((LA85_0>=LessThanSignEqualsSign && LA85_0<=GreaterThanSignEqualsSign)||LA85_0==LessThanSign||LA85_0==GreaterThanSign) ) {
                    alt85=1;
                }


                switch (alt85) {
            	case 1 :
            	    // InternalCQLParser.g:5730:4: () ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCQLParser.g:5730:4: ()
            	    // InternalCQLParser.g:5731:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:5737:4: ( (lv_op_2_0= ruleCOMPARE_OPERATOR ) )
            	    // InternalCQLParser.g:5738:5: (lv_op_2_0= ruleCOMPARE_OPERATOR )
            	    {
            	    // InternalCQLParser.g:5738:5: (lv_op_2_0= ruleCOMPARE_OPERATOR )
            	    // InternalCQLParser.g:5739:6: lv_op_2_0= ruleCOMPARE_OPERATOR
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getOpCOMPARE_OPERATORParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_17);
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

            	    // InternalCQLParser.g:5756:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCQLParser.g:5757:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCQLParser.g:5757:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCQLParser.g:5758:6: lv_right_3_0= rulePlusOrMinus
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getRightPlusOrMinusParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_86);
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
            	    break loop85;
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
    // InternalCQLParser.g:5780:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCQLParser.g:5780:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCQLParser.g:5781:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
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
    // InternalCQLParser.g:5787:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5793:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCQLParser.g:5794:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCQLParser.g:5794:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCQLParser.g:5795:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_87);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:5803:3: ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop87:
            do {
                int alt87=2;
                int LA87_0 = input.LA(1);

                if ( (LA87_0==PlusSign||LA87_0==HyphenMinus) ) {
                    alt87=1;
                }


                switch (alt87) {
            	case 1 :
            	    // InternalCQLParser.g:5804:4: ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCQLParser.g:5804:4: ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) )
            	    int alt86=2;
            	    int LA86_0 = input.LA(1);

            	    if ( (LA86_0==PlusSign) ) {
            	        alt86=1;
            	    }
            	    else if ( (LA86_0==HyphenMinus) ) {
            	        alt86=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 86, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt86) {
            	        case 1 :
            	            // InternalCQLParser.g:5805:5: ( () otherlv_2= PlusSign )
            	            {
            	            // InternalCQLParser.g:5805:5: ( () otherlv_2= PlusSign )
            	            // InternalCQLParser.g:5806:6: () otherlv_2= PlusSign
            	            {
            	            // InternalCQLParser.g:5806:6: ()
            	            // InternalCQLParser.g:5807:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_2=(Token)match(input,PlusSign,FOLLOW_17); 

            	            						newLeafNode(otherlv_2, grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1());
            	            					

            	            }


            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:5819:5: ( () otherlv_4= HyphenMinus )
            	            {
            	            // InternalCQLParser.g:5819:5: ( () otherlv_4= HyphenMinus )
            	            // InternalCQLParser.g:5820:6: () otherlv_4= HyphenMinus
            	            {
            	            // InternalCQLParser.g:5820:6: ()
            	            // InternalCQLParser.g:5821:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_4=(Token)match(input,HyphenMinus,FOLLOW_17); 

            	            						newLeafNode(otherlv_4, grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1());
            	            					

            	            }


            	            }
            	            break;

            	    }

            	    // InternalCQLParser.g:5833:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCQLParser.g:5834:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCQLParser.g:5834:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCQLParser.g:5835:6: lv_right_5_0= ruleMulOrDiv
            	    {

            	    						newCompositeNode(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_87);
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
            	    break loop87;
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
    // InternalCQLParser.g:5857:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCQLParser.g:5857:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCQLParser.g:5858:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
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
    // InternalCQLParser.g:5864:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:5870:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCQLParser.g:5871:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCQLParser.g:5871:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCQLParser.g:5872:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_88);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:5880:3: ( () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop89:
            do {
                int alt89=2;
                int LA89_0 = input.LA(1);

                if ( (LA89_0==Asterisk||LA89_0==Solidus) ) {
                    alt89=1;
                }


                switch (alt89) {
            	case 1 :
            	    // InternalCQLParser.g:5881:4: () ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCQLParser.g:5881:4: ()
            	    // InternalCQLParser.g:5882:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:5888:4: ( ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) ) )
            	    // InternalCQLParser.g:5889:5: ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) )
            	    {
            	    // InternalCQLParser.g:5889:5: ( (lv_op_2_1= Solidus | lv_op_2_2= Asterisk ) )
            	    // InternalCQLParser.g:5890:6: (lv_op_2_1= Solidus | lv_op_2_2= Asterisk )
            	    {
            	    // InternalCQLParser.g:5890:6: (lv_op_2_1= Solidus | lv_op_2_2= Asterisk )
            	    int alt88=2;
            	    int LA88_0 = input.LA(1);

            	    if ( (LA88_0==Solidus) ) {
            	        alt88=1;
            	    }
            	    else if ( (LA88_0==Asterisk) ) {
            	        alt88=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 88, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt88) {
            	        case 1 :
            	            // InternalCQLParser.g:5891:7: lv_op_2_1= Solidus
            	            {
            	            lv_op_2_1=(Token)match(input,Solidus,FOLLOW_17); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getMulOrDivAccess().getOpSolidusKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getMulOrDivRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:5902:7: lv_op_2_2= Asterisk
            	            {
            	            lv_op_2_2=(Token)match(input,Asterisk,FOLLOW_17); 

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

            	    // InternalCQLParser.g:5915:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCQLParser.g:5916:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCQLParser.g:5916:5: (lv_right_3_0= rulePrimary )
            	    // InternalCQLParser.g:5917:6: lv_right_3_0= rulePrimary
            	    {

            	    						newCompositeNode(grammarAccess.getMulOrDivAccess().getRightPrimaryParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_88);
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
            	    break loop89;
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
    // InternalCQLParser.g:5939:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCQLParser.g:5939:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCQLParser.g:5940:2: iv_rulePrimary= rulePrimary EOF
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
    // InternalCQLParser.g:5946:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
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
            // InternalCQLParser.g:5952:2: ( ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCQLParser.g:5953:2: ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCQLParser.g:5953:2: ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt90=3;
            switch ( input.LA(1) ) {
            case LeftParenthesis:
                {
                alt90=1;
                }
                break;
            case NOT:
                {
                alt90=2;
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
                alt90=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 90, 0, input);

                throw nvae;
            }

            switch (alt90) {
                case 1 :
                    // InternalCQLParser.g:5954:3: ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis )
                    {
                    // InternalCQLParser.g:5954:3: ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis )
                    // InternalCQLParser.g:5955:4: () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis
                    {
                    // InternalCQLParser.g:5955:4: ()
                    // InternalCQLParser.g:5956:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,LeftParenthesis,FOLLOW_17); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCQLParser.g:5966:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCQLParser.g:5967:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCQLParser.g:5967:5: (lv_inner_2_0= ruleExpression )
                    // InternalCQLParser.g:5968:6: lv_inner_2_0= ruleExpression
                    {

                    						newCompositeNode(grammarAccess.getPrimaryAccess().getInnerExpressionParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_25);
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
                    // InternalCQLParser.g:5991:3: ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCQLParser.g:5991:3: ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCQLParser.g:5992:4: () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCQLParser.g:5992:4: ()
                    // InternalCQLParser.g:5993:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,NOT,FOLLOW_17); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCQLParser.g:6003:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCQLParser.g:6004:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCQLParser.g:6004:5: (lv_expression_6_0= rulePrimary )
                    // InternalCQLParser.g:6005:6: lv_expression_6_0= rulePrimary
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
                    // InternalCQLParser.g:6024:3: this_Atomic_7= ruleAtomic
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
    // InternalCQLParser.g:6036:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCQLParser.g:6036:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCQLParser.g:6037:2: iv_ruleAtomic= ruleAtomic EOF
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
    // InternalCQLParser.g:6043:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) ) | ( () ( (lv_value_11_0= ruleComplexPredicate ) ) ) ) ;
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
            // InternalCQLParser.g:6049:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) ) | ( () ( (lv_value_11_0= ruleComplexPredicate ) ) ) ) )
            // InternalCQLParser.g:6050:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) ) | ( () ( (lv_value_11_0= ruleComplexPredicate ) ) ) )
            {
            // InternalCQLParser.g:6050:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) ) | ( () ( (lv_value_11_0= ruleComplexPredicate ) ) ) )
            int alt91=6;
            alt91 = dfa91.predict(input);
            switch (alt91) {
                case 1 :
                    // InternalCQLParser.g:6051:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQLParser.g:6051:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQLParser.g:6052:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQLParser.g:6052:4: ()
                    // InternalCQLParser.g:6053:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6059:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQLParser.g:6060:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQLParser.g:6060:5: (lv_value_1_0= RULE_INT )
                    // InternalCQLParser.g:6061:6: lv_value_1_0= RULE_INT
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
                    // InternalCQLParser.g:6079:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:6079:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQLParser.g:6080:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQLParser.g:6080:4: ()
                    // InternalCQLParser.g:6081:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6087:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQLParser.g:6088:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQLParser.g:6088:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQLParser.g:6089:6: lv_value_3_0= RULE_FLOAT
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
                    // InternalCQLParser.g:6107:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQLParser.g:6107:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQLParser.g:6108:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQLParser.g:6108:4: ()
                    // InternalCQLParser.g:6109:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6115:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQLParser.g:6116:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:6116:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQLParser.g:6117:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQLParser.g:6135:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    {
                    // InternalCQLParser.g:6135:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    // InternalCQLParser.g:6136:4: () ( (lv_value_7_0= ruleBOOLEAN ) )
                    {
                    // InternalCQLParser.g:6136:4: ()
                    // InternalCQLParser.g:6137:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6143:4: ( (lv_value_7_0= ruleBOOLEAN ) )
                    // InternalCQLParser.g:6144:5: (lv_value_7_0= ruleBOOLEAN )
                    {
                    // InternalCQLParser.g:6144:5: (lv_value_7_0= ruleBOOLEAN )
                    // InternalCQLParser.g:6145:6: lv_value_7_0= ruleBOOLEAN
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
                    // InternalCQLParser.g:6164:3: ( () ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) )
                    {
                    // InternalCQLParser.g:6164:3: ( () ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) )
                    // InternalCQLParser.g:6165:4: () ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQLParser.g:6165:4: ()
                    // InternalCQLParser.g:6166:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeRefAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6172:4: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQLParser.g:6173:5: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQLParser.g:6173:5: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQLParser.g:6174:6: lv_value_9_0= ruleAttributeWithoutAliasDefinition
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
                    // InternalCQLParser.g:6193:3: ( () ( (lv_value_11_0= ruleComplexPredicate ) ) )
                    {
                    // InternalCQLParser.g:6193:3: ( () ( (lv_value_11_0= ruleComplexPredicate ) ) )
                    // InternalCQLParser.g:6194:4: () ( (lv_value_11_0= ruleComplexPredicate ) )
                    {
                    // InternalCQLParser.g:6194:4: ()
                    // InternalCQLParser.g:6195:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getComplexPredicateRefAction_5_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6201:4: ( (lv_value_11_0= ruleComplexPredicate ) )
                    // InternalCQLParser.g:6202:5: (lv_value_11_0= ruleComplexPredicate )
                    {
                    // InternalCQLParser.g:6202:5: (lv_value_11_0= ruleComplexPredicate )
                    // InternalCQLParser.g:6203:6: lv_value_11_0= ruleComplexPredicate
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
    // InternalCQLParser.g:6225:1: entryRuleAtomicWithoutAttributeRef returns [EObject current=null] : iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF ;
    public final EObject entryRuleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomicWithoutAttributeRef = null;


        try {
            // InternalCQLParser.g:6225:66: (iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF )
            // InternalCQLParser.g:6226:2: iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF
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
    // InternalCQLParser.g:6232:1: ruleAtomicWithoutAttributeRef returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_MATRIX_FLOAT ) ) ) | ( () ( (lv_value_11_0= RULE_VECTOR_FLOAT ) ) ) ) ;
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
            // InternalCQLParser.g:6238:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_MATRIX_FLOAT ) ) ) | ( () ( (lv_value_11_0= RULE_VECTOR_FLOAT ) ) ) ) )
            // InternalCQLParser.g:6239:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_MATRIX_FLOAT ) ) ) | ( () ( (lv_value_11_0= RULE_VECTOR_FLOAT ) ) ) )
            {
            // InternalCQLParser.g:6239:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= RULE_MATRIX_FLOAT ) ) ) | ( () ( (lv_value_11_0= RULE_VECTOR_FLOAT ) ) ) )
            int alt92=6;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt92=1;
                }
                break;
            case RULE_FLOAT:
                {
                alt92=2;
                }
                break;
            case RULE_STRING:
                {
                alt92=3;
                }
                break;
            case FALSE:
            case TRUE:
                {
                alt92=4;
                }
                break;
            case RULE_MATRIX_FLOAT:
                {
                alt92=5;
                }
                break;
            case RULE_VECTOR_FLOAT:
                {
                alt92=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 92, 0, input);

                throw nvae;
            }

            switch (alt92) {
                case 1 :
                    // InternalCQLParser.g:6240:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQLParser.g:6240:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQLParser.g:6241:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQLParser.g:6241:4: ()
                    // InternalCQLParser.g:6242:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6248:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQLParser.g:6249:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQLParser.g:6249:5: (lv_value_1_0= RULE_INT )
                    // InternalCQLParser.g:6250:6: lv_value_1_0= RULE_INT
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
                    // InternalCQLParser.g:6268:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:6268:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQLParser.g:6269:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQLParser.g:6269:4: ()
                    // InternalCQLParser.g:6270:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6276:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQLParser.g:6277:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQLParser.g:6277:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQLParser.g:6278:6: lv_value_3_0= RULE_FLOAT
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
                    // InternalCQLParser.g:6296:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQLParser.g:6296:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQLParser.g:6297:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQLParser.g:6297:4: ()
                    // InternalCQLParser.g:6298:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6304:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQLParser.g:6305:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:6305:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQLParser.g:6306:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQLParser.g:6324:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    {
                    // InternalCQLParser.g:6324:3: ( () ( (lv_value_7_0= ruleBOOLEAN ) ) )
                    // InternalCQLParser.g:6325:4: () ( (lv_value_7_0= ruleBOOLEAN ) )
                    {
                    // InternalCQLParser.g:6325:4: ()
                    // InternalCQLParser.g:6326:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6332:4: ( (lv_value_7_0= ruleBOOLEAN ) )
                    // InternalCQLParser.g:6333:5: (lv_value_7_0= ruleBOOLEAN )
                    {
                    // InternalCQLParser.g:6333:5: (lv_value_7_0= ruleBOOLEAN )
                    // InternalCQLParser.g:6334:6: lv_value_7_0= ruleBOOLEAN
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
                    // InternalCQLParser.g:6353:3: ( () ( (lv_value_9_0= RULE_MATRIX_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:6353:3: ( () ( (lv_value_9_0= RULE_MATRIX_FLOAT ) ) )
                    // InternalCQLParser.g:6354:4: () ( (lv_value_9_0= RULE_MATRIX_FLOAT ) )
                    {
                    // InternalCQLParser.g:6354:4: ()
                    // InternalCQLParser.g:6355:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getMatrixAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6361:4: ( (lv_value_9_0= RULE_MATRIX_FLOAT ) )
                    // InternalCQLParser.g:6362:5: (lv_value_9_0= RULE_MATRIX_FLOAT )
                    {
                    // InternalCQLParser.g:6362:5: (lv_value_9_0= RULE_MATRIX_FLOAT )
                    // InternalCQLParser.g:6363:6: lv_value_9_0= RULE_MATRIX_FLOAT
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
                    // InternalCQLParser.g:6381:3: ( () ( (lv_value_11_0= RULE_VECTOR_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:6381:3: ( () ( (lv_value_11_0= RULE_VECTOR_FLOAT ) ) )
                    // InternalCQLParser.g:6382:4: () ( (lv_value_11_0= RULE_VECTOR_FLOAT ) )
                    {
                    // InternalCQLParser.g:6382:4: ()
                    // InternalCQLParser.g:6383:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getVectorAction_5_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:6389:4: ( (lv_value_11_0= RULE_VECTOR_FLOAT ) )
                    // InternalCQLParser.g:6390:5: (lv_value_11_0= RULE_VECTOR_FLOAT )
                    {
                    // InternalCQLParser.g:6390:5: (lv_value_11_0= RULE_VECTOR_FLOAT )
                    // InternalCQLParser.g:6391:6: lv_value_11_0= RULE_VECTOR_FLOAT
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
    // InternalCQLParser.g:6412:1: ruleTime returns [Enumerator current=null] : ( (enumLiteral_0= NULL ) | (enumLiteral_1= NANOSECOND ) | (enumLiteral_2= NANOSECONDS ) | (enumLiteral_3= MILLISECOND ) | (enumLiteral_4= SECOND ) | (enumLiteral_5= MINUTE ) | (enumLiteral_6= HOUR ) | (enumLiteral_7= WEEK ) | (enumLiteral_8= MILLISECONDS ) | (enumLiteral_9= SECONDS ) | (enumLiteral_10= MINUTES ) | (enumLiteral_11= HOURS ) | (enumLiteral_12= WEEKS ) ) ;
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
        Token enumLiteral_10=null;
        Token enumLiteral_11=null;
        Token enumLiteral_12=null;


        	enterRule();

        try {
            // InternalCQLParser.g:6418:2: ( ( (enumLiteral_0= NULL ) | (enumLiteral_1= NANOSECOND ) | (enumLiteral_2= NANOSECONDS ) | (enumLiteral_3= MILLISECOND ) | (enumLiteral_4= SECOND ) | (enumLiteral_5= MINUTE ) | (enumLiteral_6= HOUR ) | (enumLiteral_7= WEEK ) | (enumLiteral_8= MILLISECONDS ) | (enumLiteral_9= SECONDS ) | (enumLiteral_10= MINUTES ) | (enumLiteral_11= HOURS ) | (enumLiteral_12= WEEKS ) ) )
            // InternalCQLParser.g:6419:2: ( (enumLiteral_0= NULL ) | (enumLiteral_1= NANOSECOND ) | (enumLiteral_2= NANOSECONDS ) | (enumLiteral_3= MILLISECOND ) | (enumLiteral_4= SECOND ) | (enumLiteral_5= MINUTE ) | (enumLiteral_6= HOUR ) | (enumLiteral_7= WEEK ) | (enumLiteral_8= MILLISECONDS ) | (enumLiteral_9= SECONDS ) | (enumLiteral_10= MINUTES ) | (enumLiteral_11= HOURS ) | (enumLiteral_12= WEEKS ) )
            {
            // InternalCQLParser.g:6419:2: ( (enumLiteral_0= NULL ) | (enumLiteral_1= NANOSECOND ) | (enumLiteral_2= NANOSECONDS ) | (enumLiteral_3= MILLISECOND ) | (enumLiteral_4= SECOND ) | (enumLiteral_5= MINUTE ) | (enumLiteral_6= HOUR ) | (enumLiteral_7= WEEK ) | (enumLiteral_8= MILLISECONDS ) | (enumLiteral_9= SECONDS ) | (enumLiteral_10= MINUTES ) | (enumLiteral_11= HOURS ) | (enumLiteral_12= WEEKS ) )
            int alt93=13;
            switch ( input.LA(1) ) {
            case NULL:
                {
                alt93=1;
                }
                break;
            case NANOSECOND:
                {
                alt93=2;
                }
                break;
            case NANOSECONDS:
                {
                alt93=3;
                }
                break;
            case MILLISECOND:
                {
                alt93=4;
                }
                break;
            case SECOND:
                {
                alt93=5;
                }
                break;
            case MINUTE:
                {
                alt93=6;
                }
                break;
            case HOUR:
                {
                alt93=7;
                }
                break;
            case WEEK:
                {
                alt93=8;
                }
                break;
            case MILLISECONDS:
                {
                alt93=9;
                }
                break;
            case SECONDS:
                {
                alt93=10;
                }
                break;
            case MINUTES:
                {
                alt93=11;
                }
                break;
            case HOURS:
                {
                alt93=12;
                }
                break;
            case WEEKS:
                {
                alt93=13;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 93, 0, input);

                throw nvae;
            }

            switch (alt93) {
                case 1 :
                    // InternalCQLParser.g:6420:3: (enumLiteral_0= NULL )
                    {
                    // InternalCQLParser.g:6420:3: (enumLiteral_0= NULL )
                    // InternalCQLParser.g:6421:4: enumLiteral_0= NULL
                    {
                    enumLiteral_0=(Token)match(input,NULL,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getNULLEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getTimeAccess().getNULLEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:6428:3: (enumLiteral_1= NANOSECOND )
                    {
                    // InternalCQLParser.g:6428:3: (enumLiteral_1= NANOSECOND )
                    // InternalCQLParser.g:6429:4: enumLiteral_1= NANOSECOND
                    {
                    enumLiteral_1=(Token)match(input,NANOSECOND,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getNANOSECONDEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getTimeAccess().getNANOSECONDEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:6436:3: (enumLiteral_2= NANOSECONDS )
                    {
                    // InternalCQLParser.g:6436:3: (enumLiteral_2= NANOSECONDS )
                    // InternalCQLParser.g:6437:4: enumLiteral_2= NANOSECONDS
                    {
                    enumLiteral_2=(Token)match(input,NANOSECONDS,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getNANOSECONDSEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getTimeAccess().getNANOSECONDSEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:6444:3: (enumLiteral_3= MILLISECOND )
                    {
                    // InternalCQLParser.g:6444:3: (enumLiteral_3= MILLISECOND )
                    // InternalCQLParser.g:6445:4: enumLiteral_3= MILLISECOND
                    {
                    enumLiteral_3=(Token)match(input,MILLISECOND,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getMILLISECONDEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_3, grammarAccess.getTimeAccess().getMILLISECONDEnumLiteralDeclaration_3());
                    			

                    }


                    }
                    break;
                case 5 :
                    // InternalCQLParser.g:6452:3: (enumLiteral_4= SECOND )
                    {
                    // InternalCQLParser.g:6452:3: (enumLiteral_4= SECOND )
                    // InternalCQLParser.g:6453:4: enumLiteral_4= SECOND
                    {
                    enumLiteral_4=(Token)match(input,SECOND,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getSECONDEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_4, grammarAccess.getTimeAccess().getSECONDEnumLiteralDeclaration_4());
                    			

                    }


                    }
                    break;
                case 6 :
                    // InternalCQLParser.g:6460:3: (enumLiteral_5= MINUTE )
                    {
                    // InternalCQLParser.g:6460:3: (enumLiteral_5= MINUTE )
                    // InternalCQLParser.g:6461:4: enumLiteral_5= MINUTE
                    {
                    enumLiteral_5=(Token)match(input,MINUTE,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getMINUTEEnumLiteralDeclaration_5().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_5, grammarAccess.getTimeAccess().getMINUTEEnumLiteralDeclaration_5());
                    			

                    }


                    }
                    break;
                case 7 :
                    // InternalCQLParser.g:6468:3: (enumLiteral_6= HOUR )
                    {
                    // InternalCQLParser.g:6468:3: (enumLiteral_6= HOUR )
                    // InternalCQLParser.g:6469:4: enumLiteral_6= HOUR
                    {
                    enumLiteral_6=(Token)match(input,HOUR,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getHOUREnumLiteralDeclaration_6().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_6, grammarAccess.getTimeAccess().getHOUREnumLiteralDeclaration_6());
                    			

                    }


                    }
                    break;
                case 8 :
                    // InternalCQLParser.g:6476:3: (enumLiteral_7= WEEK )
                    {
                    // InternalCQLParser.g:6476:3: (enumLiteral_7= WEEK )
                    // InternalCQLParser.g:6477:4: enumLiteral_7= WEEK
                    {
                    enumLiteral_7=(Token)match(input,WEEK,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getWEEKEnumLiteralDeclaration_7().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_7, grammarAccess.getTimeAccess().getWEEKEnumLiteralDeclaration_7());
                    			

                    }


                    }
                    break;
                case 9 :
                    // InternalCQLParser.g:6484:3: (enumLiteral_8= MILLISECONDS )
                    {
                    // InternalCQLParser.g:6484:3: (enumLiteral_8= MILLISECONDS )
                    // InternalCQLParser.g:6485:4: enumLiteral_8= MILLISECONDS
                    {
                    enumLiteral_8=(Token)match(input,MILLISECONDS,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getMILLISECONDSEnumLiteralDeclaration_8().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_8, grammarAccess.getTimeAccess().getMILLISECONDSEnumLiteralDeclaration_8());
                    			

                    }


                    }
                    break;
                case 10 :
                    // InternalCQLParser.g:6492:3: (enumLiteral_9= SECONDS )
                    {
                    // InternalCQLParser.g:6492:3: (enumLiteral_9= SECONDS )
                    // InternalCQLParser.g:6493:4: enumLiteral_9= SECONDS
                    {
                    enumLiteral_9=(Token)match(input,SECONDS,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getSECONDSEnumLiteralDeclaration_9().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_9, grammarAccess.getTimeAccess().getSECONDSEnumLiteralDeclaration_9());
                    			

                    }


                    }
                    break;
                case 11 :
                    // InternalCQLParser.g:6500:3: (enumLiteral_10= MINUTES )
                    {
                    // InternalCQLParser.g:6500:3: (enumLiteral_10= MINUTES )
                    // InternalCQLParser.g:6501:4: enumLiteral_10= MINUTES
                    {
                    enumLiteral_10=(Token)match(input,MINUTES,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getMINUTESEnumLiteralDeclaration_10().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_10, grammarAccess.getTimeAccess().getMINUTESEnumLiteralDeclaration_10());
                    			

                    }


                    }
                    break;
                case 12 :
                    // InternalCQLParser.g:6508:3: (enumLiteral_11= HOURS )
                    {
                    // InternalCQLParser.g:6508:3: (enumLiteral_11= HOURS )
                    // InternalCQLParser.g:6509:4: enumLiteral_11= HOURS
                    {
                    enumLiteral_11=(Token)match(input,HOURS,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getHOURSEnumLiteralDeclaration_11().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_11, grammarAccess.getTimeAccess().getHOURSEnumLiteralDeclaration_11());
                    			

                    }


                    }
                    break;
                case 13 :
                    // InternalCQLParser.g:6516:3: (enumLiteral_12= WEEKS )
                    {
                    // InternalCQLParser.g:6516:3: (enumLiteral_12= WEEKS )
                    // InternalCQLParser.g:6517:4: enumLiteral_12= WEEKS
                    {
                    enumLiteral_12=(Token)match(input,WEEKS,FOLLOW_2); 

                    				current = grammarAccess.getTimeAccess().getWEEKSEnumLiteralDeclaration_12().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_12, grammarAccess.getTimeAccess().getWEEKSEnumLiteralDeclaration_12());
                    			

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


    protected DFA9 dfa9 = new DFA9(this);
    protected DFA28 dfa28 = new DFA28(this);
    protected DFA48 dfa48 = new DFA48(this);
    protected DFA91 dfa91 = new DFA91(this);
    static final String dfa_1s = "\21\uffff";
    static final String dfa_2s = "\1\36\2\21\1\uffff\2\73\3\uffff\1\12\3\uffff\1\153\1\71\2\uffff";
    static final String dfa_3s = "\1\64\1\102\1\101\1\uffff\2\153\3\uffff\1\12\3\uffff\1\153\1\115\2\uffff";
    static final String dfa_4s = "\3\uffff\1\2\2\uffff\1\11\1\7\1\1\1\uffff\1\10\1\3\1\4\2\uffff\1\6\1\5";
    static final String dfa_5s = "\21\uffff}>";
    static final String[] dfa_6s = {
            "\1\2\3\uffff\1\5\5\uffff\1\3\1\uffff\1\4\11\uffff\1\1",
            "\1\7\6\uffff\1\6\15\uffff\1\10\1\3\23\uffff\1\3\1\10\4\uffff\1\3\1\10",
            "\1\11\6\uffff\1\12\16\uffff\1\3\23\uffff\1\3\5\uffff\1\3",
            "",
            "\1\14\57\uffff\1\13",
            "\1\14\57\uffff\1\13",
            "",
            "",
            "",
            "\1\15",
            "",
            "",
            "",
            "\1\16",
            "\1\17\23\uffff\1\20",
            "",
            ""
    };

    static final short[] dfa_1 = DFA.unpackEncodedString(dfa_1s);
    static final char[] dfa_2 = DFA.unpackEncodedStringToUnsignedChars(dfa_2s);
    static final char[] dfa_3 = DFA.unpackEncodedStringToUnsignedChars(dfa_3s);
    static final short[] dfa_4 = DFA.unpackEncodedString(dfa_4s);
    static final short[] dfa_5 = DFA.unpackEncodedString(dfa_5s);
    static final short[][] dfa_6 = unpackEncodedStringArray(dfa_6s);

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = dfa_1;
            this.eof = dfa_1;
            this.min = dfa_2;
            this.max = dfa_3;
            this.accept = dfa_4;
            this.special = dfa_5;
            this.transition = dfa_6;
        }
        public String getDescription() {
            return "454:4: (lv_type_0_1= ruleDropStream | lv_type_0_2= ruleUserManagement | lv_type_0_3= ruleRightsManagement | lv_type_0_4= ruleRoleManagement | lv_type_0_5= ruleCreateDataBaseGenericConnection | lv_type_0_6= ruleCreateDataBaseJDBCConnection | lv_type_0_7= ruleDropDatabaseConnection | lv_type_0_8= ruleCreateContextStore | lv_type_0_9= ruleDropContextStore )";
        }
    }
    static final String dfa_7s = "\15\uffff";
    static final String dfa_8s = "\1\37\1\112\1\146\2\uffff\1\130\1\uffff\1\113\1\153\2\113\1\147\1\113";
    static final String dfa_9s = "\1\153\1\142\1\146\2\uffff\1\153\1\uffff\1\142\1\153\2\142\1\147\1\142";
    static final String dfa_10s = "\3\uffff\1\2\1\3\1\uffff\1\1\6\uffff";
    static final String dfa_11s = "\15\uffff}>";
    static final String[] dfa_12s = {
            "\1\3\65\uffff\1\2\25\uffff\1\1",
            "\1\7\2\6\4\uffff\1\4\12\uffff\1\5\3\uffff\1\6\1\uffff\1\6",
            "\1\10",
            "",
            "",
            "\1\11\22\uffff\1\12",
            "",
            "\2\6\4\uffff\1\4\16\uffff\1\6\1\uffff\1\6",
            "\1\13",
            "\2\6\4\uffff\1\4\16\uffff\1\6\1\uffff\1\6",
            "\2\6\4\uffff\1\4\16\uffff\1\6\1\uffff\1\6",
            "\1\14",
            "\2\6\4\uffff\1\4\16\uffff\1\6\1\uffff\1\6"
    };

    static final short[] dfa_7 = DFA.unpackEncodedString(dfa_7s);
    static final char[] dfa_8 = DFA.unpackEncodedStringToUnsignedChars(dfa_8s);
    static final char[] dfa_9 = DFA.unpackEncodedStringToUnsignedChars(dfa_9s);
    static final short[] dfa_10 = DFA.unpackEncodedString(dfa_10s);
    static final short[] dfa_11 = DFA.unpackEncodedString(dfa_11s);
    static final short[][] dfa_12 = unpackEncodedStringArray(dfa_12s);

    class DFA28 extends DFA {

        public DFA28(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 28;
            this.eot = dfa_7;
            this.eof = dfa_7;
            this.min = dfa_8;
            this.max = dfa_9;
            this.accept = dfa_10;
            this.special = dfa_11;
            this.transition = dfa_12;
        }
        public String getDescription() {
            return "1444:3: ( ( (lv_quantification_0_0= ruleQuantificationPredicate ) ) | ( (lv_exists_1_0= ruleExistPredicate ) ) | ( (lv_in_2_0= ruleInPredicate ) ) )";
        }
    }
    static final String dfa_13s = "\17\uffff";
    static final String dfa_14s = "\1\153\1\67\1\153\2\uffff\1\153\1\127\1\153\1\21\1\153\4\uffff\1\127";
    static final String dfa_15s = "\1\153\1\126\1\153\2\uffff\1\153\1\132\1\153\1\66\1\153\4\uffff\1\132";
    static final String dfa_16s = "\3\uffff\1\6\1\5\5\uffff\1\4\1\1\1\2\1\3\1\uffff";
    static final String dfa_17s = "\17\uffff}>";
    static final String[] dfa_18s = {
            "\1\1",
            "\1\3\25\uffff\1\4\10\uffff\1\2",
            "\1\5",
            "",
            "",
            "\1\6",
            "\1\10\2\uffff\1\7",
            "\1\11",
            "\1\12\5\uffff\1\14\4\uffff\1\13\31\uffff\1\15",
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

    class DFA48 extends DFA {

        public DFA48(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 48;
            this.eot = dfa_13;
            this.eof = dfa_13;
            this.min = dfa_14;
            this.max = dfa_15;
            this.accept = dfa_16;
            this.special = dfa_17;
            this.transition = dfa_18;
        }
        public String getDescription() {
            return "3009:5: (lv_create_4_1= ruleCreateAccessFramework | lv_create_4_2= ruleCreateChannelFrameworkViaPort | lv_create_4_3= ruleCreateChannelFormatViaFile | lv_create_4_4= ruleCreateDatabaseStream | lv_create_4_5= ruleCreateDatabaseSink | lv_create_4_6= ruleCreateView )";
        }
    }
    static final String dfa_19s = "\24\uffff";
    static final String dfa_20s = "\5\uffff\1\16\3\uffff\1\16\6\uffff\2\16\1\uffff\1\16";
    static final String dfa_21s = "\1\37\4\uffff\1\5\1\146\1\uffff\1\130\1\5\4\37\1\uffff\1\153\2\5\1\147\1\5";
    static final String dfa_22s = "\1\162\4\uffff\1\142\1\146\1\uffff\1\153\1\142\4\162\1\uffff\1\153\2\142\1\147\1\142";
    static final String dfa_23s = "\1\uffff\1\1\1\2\1\3\1\4\2\uffff\1\6\6\uffff\1\5\5\uffff";
    static final String dfa_24s = "\24\uffff}>";
    static final String[] dfa_25s = {
            "\1\7\11\uffff\1\4\26\uffff\1\4\24\uffff\1\6\24\uffff\1\1\1\5\1\2\5\uffff\1\3",
            "",
            "",
            "",
            "",
            "\1\16\5\uffff\1\16\21\uffff\2\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\uffff\2\16\5\uffff\1\16\2\uffff\1\16\21\uffff\1\16\2\uffff\1\16\1\11\1\13\1\12\4\uffff\1\7\1\uffff\1\16\3\uffff\3\16\1\uffff\1\16\1\10\1\16\1\uffff\1\16\1\14\1\16\1\15",
            "\1\17",
            "",
            "\1\21\22\uffff\1\20",
            "\1\16\5\uffff\1\16\21\uffff\2\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\uffff\2\16\5\uffff\1\16\2\uffff\1\16\21\uffff\1\16\2\uffff\1\16\1\uffff\1\13\1\12\4\uffff\1\7\1\uffff\1\16\3\uffff\3\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\14\1\16\1\15",
            "\1\16\11\uffff\1\16\24\uffff\1\7\1\uffff\1\16\4\uffff\1\7\1\uffff\1\7\1\16\14\uffff\2\16\23\uffff\3\16\5\uffff\1\16",
            "\1\16\11\uffff\1\16\24\uffff\1\7\1\uffff\1\16\4\uffff\1\7\1\uffff\1\7\1\16\14\uffff\2\16\23\uffff\3\16\5\uffff\1\16",
            "\1\16\11\uffff\1\16\24\uffff\1\7\1\uffff\1\16\4\uffff\1\7\1\uffff\1\7\1\16\14\uffff\2\16\23\uffff\3\16\5\uffff\1\16",
            "\1\16\11\uffff\1\16\24\uffff\1\7\1\uffff\1\16\4\uffff\1\7\1\uffff\1\7\1\16\14\uffff\2\16\23\uffff\3\16\5\uffff\1\16",
            "",
            "\1\22",
            "\1\16\5\uffff\1\16\21\uffff\2\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\uffff\2\16\5\uffff\1\16\2\uffff\1\16\21\uffff\1\16\2\uffff\1\16\1\uffff\1\13\1\12\4\uffff\1\7\1\uffff\1\16\3\uffff\3\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\14\1\16\1\15",
            "\1\16\5\uffff\1\16\21\uffff\2\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\uffff\2\16\5\uffff\1\16\2\uffff\1\16\21\uffff\1\16\2\uffff\1\16\1\uffff\1\13\1\12\4\uffff\1\7\1\uffff\1\16\3\uffff\3\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\14\1\16\1\15",
            "\1\23",
            "\1\16\5\uffff\1\16\21\uffff\2\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\uffff\2\16\5\uffff\1\16\2\uffff\1\16\21\uffff\1\16\2\uffff\1\16\1\uffff\1\13\1\12\4\uffff\1\7\1\uffff\1\16\3\uffff\3\16\1\uffff\1\16\1\uffff\1\16\1\uffff\1\16\1\14\1\16\1\15"
    };

    static final short[] dfa_19 = DFA.unpackEncodedString(dfa_19s);
    static final short[] dfa_20 = DFA.unpackEncodedString(dfa_20s);
    static final char[] dfa_21 = DFA.unpackEncodedStringToUnsignedChars(dfa_21s);
    static final char[] dfa_22 = DFA.unpackEncodedStringToUnsignedChars(dfa_22s);
    static final short[] dfa_23 = DFA.unpackEncodedString(dfa_23s);
    static final short[] dfa_24 = DFA.unpackEncodedString(dfa_24s);
    static final short[][] dfa_25 = unpackEncodedStringArray(dfa_25s);

    class DFA91 extends DFA {

        public DFA91(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 91;
            this.eot = dfa_19;
            this.eof = dfa_20;
            this.min = dfa_21;
            this.max = dfa_22;
            this.accept = dfa_23;
            this.special = dfa_24;
            this.transition = dfa_25;
        }
        public String getDescription() {
            return "6050:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( (lv_value_7_0= ruleBOOLEAN ) ) ) | ( () ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) ) | ( () ( (lv_value_11_0= ruleComplexPredicate ) ) ) )";
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0010055460000002L,0x0000000080000000L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000020000040000L,0x00059C0001200001L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0080020000040000L,0x00059C0005200001L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0080000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000000000000L,0x0000080000400000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0008080100000002L,0x0000080004400000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0008080100000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000020080000000L,0x00041C0000600101L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000080100000002L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000000000000L,0x0000080000200000L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000100000002L,0x0000080004200000L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000100000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0002000000000822L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000005060000000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000000000002L,0x0000000800002000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000000000002L,0x0000000000002000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000000000000L,0x0000000500001800L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x4000000000000000L,0x00000000000000A0L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000000000002L,0x000000202B002000L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000000000000L,0x000000202B000000L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000000000000000L,0x0006000000000000L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000000000000000L,0x0004000004800000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000000000000000L,0x0000000004800000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x1000004000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0000000000000000L,0x0000040000000000L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0020000000000002L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0504100A0A002340L,0x0000000000000008L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000040L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0010000000200000L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_62 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004010L});
    public static final BitSet FOLLOW_63 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_64 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_65 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_66 = new BitSet(new long[]{0x0000202000000000L});
    public static final BitSet FOLLOW_67 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_68 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_69 = new BitSet(new long[]{0x0000005060000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_70 = new BitSet(new long[]{0x0800008000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_71 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_72 = new BitSet(new long[]{0x0000000000000000L,0x0000000004140000L});
    public static final BitSet FOLLOW_73 = new BitSet(new long[]{0x0000000000000000L,0x0000000004100000L});
    public static final BitSet FOLLOW_74 = new BitSet(new long[]{0x0080000000000000L,0x0000000004040000L});
    public static final BitSet FOLLOW_75 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_76 = new BitSet(new long[]{0x2000000000010000L});
    public static final BitSet FOLLOW_77 = new BitSet(new long[]{0x0000000000000000L,0x0000001000000000L});
    public static final BitSet FOLLOW_78 = new BitSet(new long[]{0x8504100A0A402340L,0x0000000000000008L});
    public static final BitSet FOLLOW_79 = new BitSet(new long[]{0x8000000000400000L});
    public static final BitSet FOLLOW_80 = new BitSet(new long[]{0x8504100A0A002340L,0x0000000000000008L});
    public static final BitSet FOLLOW_81 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_82 = new BitSet(new long[]{0x0001000000400000L});
    public static final BitSet FOLLOW_83 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_84 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_85 = new BitSet(new long[]{0x0000000000000002L,0x0000000200000200L});
    public static final BitSet FOLLOW_86 = new BitSet(new long[]{0x0000000000000002L,0x0000000500001800L});
    public static final BitSet FOLLOW_87 = new BitSet(new long[]{0x0000000000000002L,0x000000000A000000L});
    public static final BitSet FOLLOW_88 = new BitSet(new long[]{0x0000000000000002L,0x0000000021000000L});

}