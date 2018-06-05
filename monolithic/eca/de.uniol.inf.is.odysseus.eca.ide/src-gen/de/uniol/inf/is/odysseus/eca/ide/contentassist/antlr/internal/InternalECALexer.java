package de.uniol.inf.is.odysseus.eca.ide.contentassist.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalECALexer extends Lexer {
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__12=12;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int RULE_ID=4;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=5;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int RULE_ML_COMMENT=8;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__20=20;
    public static final int T__21=21;
    public static final int RULE_STRING=6;
    public static final int RULE_SL_COMMENT=9;
    public static final int T__37=37;
    public static final int RULE_DOUBLE=7;
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
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__46=46;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__42=42;
    public static final int T__43=43;

    // delegates
    // delegators

    public InternalECALexer() {;} 
    public InternalECALexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InternalECALexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "InternalECA.g"; }

    // $ANTLR start "T__12"
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:11:7: ( 'MIN' )
            // InternalECA.g:11:9: 'MIN'
            {
            match("MIN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__12"

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:12:7: ( 'MAX' )
            // InternalECA.g:12:9: 'MAX'
            {
            match("MAX"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:13:7: ( 'TimerEvent' )
            // InternalECA.g:13:9: 'TimerEvent'
            {
            match("TimerEvent"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:14:7: ( 'QueryEvent' )
            // InternalECA.g:14:9: 'QueryEvent'
            {
            match("QueryEvent"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:15:7: ( 'curCPULoad' )
            // InternalECA.g:15:9: 'curCPULoad'
            {
            match("curCPULoad"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:16:7: ( 'curMEMLoad' )
            // InternalECA.g:16:9: 'curMEMLoad'
            {
            match("curMEMLoad"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:17:7: ( 'curNETLoad' )
            // InternalECA.g:17:9: 'curNETLoad'
            {
            match("curNETLoad"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:18:7: ( '<' )
            // InternalECA.g:18:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:19:7: ( '>' )
            // InternalECA.g:19:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:20:7: ( '=' )
            // InternalECA.g:20:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:21:7: ( '<=' )
            // InternalECA.g:21:9: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:22:7: ( '>=' )
            // InternalECA.g:22:9: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:23:7: ( 'DEFINE' )
            // InternalECA.g:23:9: 'DEFINE'
            {
            match("DEFINE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:24:7: ( 'CONSTANT' )
            // InternalECA.g:24:9: 'CONSTANT'
            {
            match("CONSTANT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:25:7: ( ':' )
            // InternalECA.g:25:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:26:7: ( ';' )
            // InternalECA.g:26:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:27:7: ( 'WINDOWSIZE' )
            // InternalECA.g:27:9: 'WINDOWSIZE'
            {
            match("WINDOWSIZE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:28:7: ( 'TIMEINTERVALL' )
            // InternalECA.g:28:9: 'TIMEINTERVALL'
            {
            match("TIMEINTERVALL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:29:7: ( 'EVENT' )
            // InternalECA.g:29:9: 'EVENT'
            {
            match("EVENT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:30:7: ( 'WITH' )
            // InternalECA.g:30:9: 'WITH'
            {
            match("WITH"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:31:7: ( 'ON' )
            // InternalECA.g:31:9: 'ON'
            {
            match("ON"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:32:7: ( 'IF' )
            // InternalECA.g:32:9: 'IF'
            {
            match("IF"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "T__34"
    public final void mT__34() throws RecognitionException {
        try {
            int _type = T__34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:33:7: ( 'THEN' )
            // InternalECA.g:33:9: 'THEN'
            {
            match("THEN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__34"

    // $ANTLR start "T__35"
    public final void mT__35() throws RecognitionException {
        try {
            int _type = T__35;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:34:7: ( 'AND' )
            // InternalECA.g:34:9: 'AND'
            {
            match("AND"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__35"

    // $ANTLR start "T__36"
    public final void mT__36() throws RecognitionException {
        try {
            int _type = T__36;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:35:7: ( '${' )
            // InternalECA.g:35:9: '${'
            {
            match("${"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__36"

    // $ANTLR start "T__37"
    public final void mT__37() throws RecognitionException {
        try {
            int _type = T__37;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:36:7: ( '}' )
            // InternalECA.g:36:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__37"

    // $ANTLR start "T__38"
    public final void mT__38() throws RecognitionException {
        try {
            int _type = T__38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:37:7: ( 'queryExists' )
            // InternalECA.g:37:9: 'queryExists'
            {
            match("queryExists"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__38"

    // $ANTLR start "T__39"
    public final void mT__39() throws RecognitionException {
        try {
            int _type = T__39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:38:7: ( '(' )
            // InternalECA.g:38:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__39"

    // $ANTLR start "T__40"
    public final void mT__40() throws RecognitionException {
        try {
            int _type = T__40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:39:7: ( ')' )
            // InternalECA.g:39:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__40"

    // $ANTLR start "T__41"
    public final void mT__41() throws RecognitionException {
        try {
            int _type = T__41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:40:7: ( 'SYSTEM.' )
            // InternalECA.g:40:9: 'SYSTEM.'
            {
            match("SYSTEM."); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__41"

    // $ANTLR start "T__42"
    public final void mT__42() throws RecognitionException {
        try {
            int _type = T__42;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:41:7: ( 'GET' )
            // InternalECA.g:41:9: 'GET'
            {
            match("GET"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__42"

    // $ANTLR start "T__43"
    public final void mT__43() throws RecognitionException {
        try {
            int _type = T__43;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:42:7: ( 'prio' )
            // InternalECA.g:42:9: 'prio'
            {
            match("prio"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__43"

    // $ANTLR start "T__44"
    public final void mT__44() throws RecognitionException {
        try {
            int _type = T__44;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:43:7: ( ',' )
            // InternalECA.g:43:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__44"

    // $ANTLR start "T__45"
    public final void mT__45() throws RecognitionException {
        try {
            int _type = T__45;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:44:7: ( 'state' )
            // InternalECA.g:44:9: 'state'
            {
            match("state"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__45"

    // $ANTLR start "T__46"
    public final void mT__46() throws RecognitionException {
        try {
            int _type = T__46;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:45:7: ( '!' )
            // InternalECA.g:45:9: '!'
            {
            match('!'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__46"

    // $ANTLR start "RULE_DOUBLE"
    public final void mRULE_DOUBLE() throws RecognitionException {
        try {
            int _type = RULE_DOUBLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:4237:13: ( ( '-' )? ( '0' .. '9' )+ '.' '0' .. '9' )
            // InternalECA.g:4237:15: ( '-' )? ( '0' .. '9' )+ '.' '0' .. '9'
            {
            // InternalECA.g:4237:15: ( '-' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='-') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // InternalECA.g:4237:15: '-'
                    {
                    match('-'); 

                    }
                    break;

            }

            // InternalECA.g:4237:20: ( '0' .. '9' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalECA.g:4237:21: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);

            match('.'); 
            matchRange('0','9'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_DOUBLE"

    // $ANTLR start "RULE_ID"
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:4239:9: ( ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // InternalECA.g:4239:11: ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            // InternalECA.g:4239:11: ( '^' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='^') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalECA.g:4239:11: '^'
                    {
                    match('^'); 

                    }
                    break;

            }

            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // InternalECA.g:4239:40: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')||(LA4_0>='A' && LA4_0<='Z')||LA4_0=='_'||(LA4_0>='a' && LA4_0<='z')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalECA.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ID"

    // $ANTLR start "RULE_INT"
    public final void mRULE_INT() throws RecognitionException {
        try {
            int _type = RULE_INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:4241:10: ( ( '0' .. '9' )+ )
            // InternalECA.g:4241:12: ( '0' .. '9' )+
            {
            // InternalECA.g:4241:12: ( '0' .. '9' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // InternalECA.g:4241:13: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_INT"

    // $ANTLR start "RULE_STRING"
    public final void mRULE_STRING() throws RecognitionException {
        try {
            int _type = RULE_STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:4243:13: ( ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' ) )
            // InternalECA.g:4243:15: ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            {
            // InternalECA.g:4243:15: ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='\"') ) {
                alt8=1;
            }
            else if ( (LA8_0=='\'') ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // InternalECA.g:4243:16: '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"'
                    {
                    match('\"'); 
                    // InternalECA.g:4243:20: ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )*
                    loop6:
                    do {
                        int alt6=3;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0=='\\') ) {
                            alt6=1;
                        }
                        else if ( ((LA6_0>='\u0000' && LA6_0<='!')||(LA6_0>='#' && LA6_0<='[')||(LA6_0>=']' && LA6_0<='\uFFFF')) ) {
                            alt6=2;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // InternalECA.g:4243:21: '\\\\' .
                    	    {
                    	    match('\\'); 
                    	    matchAny(); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalECA.g:4243:28: ~ ( ( '\\\\' | '\"' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);

                    match('\"'); 

                    }
                    break;
                case 2 :
                    // InternalECA.g:4243:48: '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\''
                    {
                    match('\''); 
                    // InternalECA.g:4243:53: ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )*
                    loop7:
                    do {
                        int alt7=3;
                        int LA7_0 = input.LA(1);

                        if ( (LA7_0=='\\') ) {
                            alt7=1;
                        }
                        else if ( ((LA7_0>='\u0000' && LA7_0<='&')||(LA7_0>='(' && LA7_0<='[')||(LA7_0>=']' && LA7_0<='\uFFFF')) ) {
                            alt7=2;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // InternalECA.g:4243:54: '\\\\' .
                    	    {
                    	    match('\\'); 
                    	    matchAny(); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalECA.g:4243:61: ~ ( ( '\\\\' | '\\'' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop7;
                        }
                    } while (true);

                    match('\''); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_STRING"

    // $ANTLR start "RULE_ML_COMMENT"
    public final void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:4245:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // InternalECA.g:4245:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // InternalECA.g:4245:24: ( options {greedy=false; } : . )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0=='*') ) {
                    int LA9_1 = input.LA(2);

                    if ( (LA9_1=='/') ) {
                        alt9=2;
                    }
                    else if ( ((LA9_1>='\u0000' && LA9_1<='.')||(LA9_1>='0' && LA9_1<='\uFFFF')) ) {
                        alt9=1;
                    }


                }
                else if ( ((LA9_0>='\u0000' && LA9_0<=')')||(LA9_0>='+' && LA9_0<='\uFFFF')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // InternalECA.g:4245:52: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            match("*/"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ML_COMMENT"

    // $ANTLR start "RULE_SL_COMMENT"
    public final void mRULE_SL_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_SL_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:4247:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // InternalECA.g:4247:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // InternalECA.g:4247:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( ((LA10_0>='\u0000' && LA10_0<='\t')||(LA10_0>='\u000B' && LA10_0<='\f')||(LA10_0>='\u000E' && LA10_0<='\uFFFF')) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // InternalECA.g:4247:24: ~ ( ( '\\n' | '\\r' ) )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            // InternalECA.g:4247:40: ( ( '\\r' )? '\\n' )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0=='\n'||LA12_0=='\r') ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // InternalECA.g:4247:41: ( '\\r' )? '\\n'
                    {
                    // InternalECA.g:4247:41: ( '\\r' )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0=='\r') ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // InternalECA.g:4247:41: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }

                    match('\n'); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_SL_COMMENT"

    // $ANTLR start "RULE_WS"
    public final void mRULE_WS() throws RecognitionException {
        try {
            int _type = RULE_WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:4249:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // InternalECA.g:4249:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // InternalECA.g:4249:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt13=0;
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>='\t' && LA13_0<='\n')||LA13_0=='\r'||LA13_0==' ') ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // InternalECA.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_WS"

    // $ANTLR start "RULE_ANY_OTHER"
    public final void mRULE_ANY_OTHER() throws RecognitionException {
        try {
            int _type = RULE_ANY_OTHER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalECA.g:4251:16: ( . )
            // InternalECA.g:4251:18: .
            {
            matchAny(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ANY_OTHER"

    public void mTokens() throws RecognitionException {
        // InternalECA.g:1:8: ( T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | RULE_DOUBLE | RULE_ID | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt14=43;
        alt14 = dfa14.predict(input);
        switch (alt14) {
            case 1 :
                // InternalECA.g:1:10: T__12
                {
                mT__12(); 

                }
                break;
            case 2 :
                // InternalECA.g:1:16: T__13
                {
                mT__13(); 

                }
                break;
            case 3 :
                // InternalECA.g:1:22: T__14
                {
                mT__14(); 

                }
                break;
            case 4 :
                // InternalECA.g:1:28: T__15
                {
                mT__15(); 

                }
                break;
            case 5 :
                // InternalECA.g:1:34: T__16
                {
                mT__16(); 

                }
                break;
            case 6 :
                // InternalECA.g:1:40: T__17
                {
                mT__17(); 

                }
                break;
            case 7 :
                // InternalECA.g:1:46: T__18
                {
                mT__18(); 

                }
                break;
            case 8 :
                // InternalECA.g:1:52: T__19
                {
                mT__19(); 

                }
                break;
            case 9 :
                // InternalECA.g:1:58: T__20
                {
                mT__20(); 

                }
                break;
            case 10 :
                // InternalECA.g:1:64: T__21
                {
                mT__21(); 

                }
                break;
            case 11 :
                // InternalECA.g:1:70: T__22
                {
                mT__22(); 

                }
                break;
            case 12 :
                // InternalECA.g:1:76: T__23
                {
                mT__23(); 

                }
                break;
            case 13 :
                // InternalECA.g:1:82: T__24
                {
                mT__24(); 

                }
                break;
            case 14 :
                // InternalECA.g:1:88: T__25
                {
                mT__25(); 

                }
                break;
            case 15 :
                // InternalECA.g:1:94: T__26
                {
                mT__26(); 

                }
                break;
            case 16 :
                // InternalECA.g:1:100: T__27
                {
                mT__27(); 

                }
                break;
            case 17 :
                // InternalECA.g:1:106: T__28
                {
                mT__28(); 

                }
                break;
            case 18 :
                // InternalECA.g:1:112: T__29
                {
                mT__29(); 

                }
                break;
            case 19 :
                // InternalECA.g:1:118: T__30
                {
                mT__30(); 

                }
                break;
            case 20 :
                // InternalECA.g:1:124: T__31
                {
                mT__31(); 

                }
                break;
            case 21 :
                // InternalECA.g:1:130: T__32
                {
                mT__32(); 

                }
                break;
            case 22 :
                // InternalECA.g:1:136: T__33
                {
                mT__33(); 

                }
                break;
            case 23 :
                // InternalECA.g:1:142: T__34
                {
                mT__34(); 

                }
                break;
            case 24 :
                // InternalECA.g:1:148: T__35
                {
                mT__35(); 

                }
                break;
            case 25 :
                // InternalECA.g:1:154: T__36
                {
                mT__36(); 

                }
                break;
            case 26 :
                // InternalECA.g:1:160: T__37
                {
                mT__37(); 

                }
                break;
            case 27 :
                // InternalECA.g:1:166: T__38
                {
                mT__38(); 

                }
                break;
            case 28 :
                // InternalECA.g:1:172: T__39
                {
                mT__39(); 

                }
                break;
            case 29 :
                // InternalECA.g:1:178: T__40
                {
                mT__40(); 

                }
                break;
            case 30 :
                // InternalECA.g:1:184: T__41
                {
                mT__41(); 

                }
                break;
            case 31 :
                // InternalECA.g:1:190: T__42
                {
                mT__42(); 

                }
                break;
            case 32 :
                // InternalECA.g:1:196: T__43
                {
                mT__43(); 

                }
                break;
            case 33 :
                // InternalECA.g:1:202: T__44
                {
                mT__44(); 

                }
                break;
            case 34 :
                // InternalECA.g:1:208: T__45
                {
                mT__45(); 

                }
                break;
            case 35 :
                // InternalECA.g:1:214: T__46
                {
                mT__46(); 

                }
                break;
            case 36 :
                // InternalECA.g:1:220: RULE_DOUBLE
                {
                mRULE_DOUBLE(); 

                }
                break;
            case 37 :
                // InternalECA.g:1:232: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 38 :
                // InternalECA.g:1:240: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 39 :
                // InternalECA.g:1:249: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 40 :
                // InternalECA.g:1:261: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 41 :
                // InternalECA.g:1:277: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 42 :
                // InternalECA.g:1:293: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 43 :
                // InternalECA.g:1:301: RULE_ANY_OTHER
                {
                mRULE_ANY_OTHER(); 

                }
                break;

        }

    }


    protected DFA14 dfa14 = new DFA14(this);
    static final String DFA14_eotS =
        "\1\uffff\4\47\1\56\1\60\1\uffff\2\47\2\uffff\5\47\1\44\1\uffff\1\47\2\uffff\3\47\1\uffff\1\47\1\uffff\1\44\1\107\1\44\1\uffff\3\44\2\uffff\2\47\1\uffff\5\47\5\uffff\2\47\2\uffff\2\47\1\131\1\132\1\47\2\uffff\1\47\2\uffff\3\47\1\uffff\1\47\3\uffff\1\107\4\uffff\1\141\1\142\12\47\2\uffff\1\157\2\47\1\162\2\47\2\uffff\2\47\1\167\7\47\1\177\1\47\1\uffff\2\47\1\uffff\1\u0083\3\47\1\uffff\7\47\1\uffff\1\u008e\2\47\1\uffff\1\u0091\6\47\1\u0098\2\47\1\uffff\2\47\1\uffff\6\47\1\uffff\3\47\1\uffff\6\47\1\u00ac\10\47\1\uffff\2\47\1\u00b7\1\47\1\u00b9\1\u00ba\1\u00bb\1\u00bc\1\u00bd\1\47\1\uffff\1\47\5\uffff\1\u00c0\1\47\1\uffff\1\u00c2\1\uffff";
    static final String DFA14_eofS =
        "\u00c3\uffff";
    static final String DFA14_minS =
        "\1\0\1\101\1\110\2\165\2\75\1\uffff\1\105\1\117\2\uffff\1\111\1\126\1\116\1\106\1\116\1\173\1\uffff\1\165\2\uffff\1\131\1\105\1\162\1\uffff\1\164\1\uffff\1\60\1\56\1\101\1\uffff\2\0\1\52\2\uffff\1\116\1\130\1\uffff\1\155\1\115\1\105\1\145\1\162\5\uffff\1\106\1\116\2\uffff\1\116\1\105\2\60\1\104\2\uffff\1\145\2\uffff\1\123\1\124\1\151\1\uffff\1\141\3\uffff\1\56\4\uffff\2\60\1\145\1\105\1\116\1\162\1\103\1\111\1\123\1\104\1\110\1\116\2\uffff\1\60\1\162\1\124\1\60\1\157\1\164\2\uffff\1\162\1\111\1\60\1\171\1\120\2\105\1\116\1\124\1\117\1\60\1\124\1\uffff\1\171\1\105\1\uffff\1\60\1\145\1\105\1\116\1\uffff\1\105\1\125\1\115\1\124\1\105\1\101\1\127\1\uffff\1\60\1\105\1\115\1\uffff\1\60\1\166\1\124\1\166\3\114\1\60\1\116\1\123\1\uffff\1\170\1\56\1\uffff\1\145\1\105\1\145\3\157\1\uffff\1\124\1\111\1\151\1\uffff\1\156\1\122\1\156\3\141\1\60\1\132\1\163\1\164\1\126\1\164\3\144\1\uffff\1\105\1\164\1\60\1\101\5\60\1\163\1\uffff\1\114\5\uffff\1\60\1\114\1\uffff\1\60\1\uffff";
    static final String DFA14_maxS =
        "\1\uffff\1\111\1\151\2\165\2\75\1\uffff\1\105\1\117\2\uffff\1\111\1\126\1\116\1\106\1\116\1\173\1\uffff\1\165\2\uffff\1\131\1\105\1\162\1\uffff\1\164\1\uffff\2\71\1\172\1\uffff\2\uffff\1\57\2\uffff\1\116\1\130\1\uffff\1\155\1\115\1\105\1\145\1\162\5\uffff\1\106\1\116\2\uffff\1\124\1\105\2\172\1\104\2\uffff\1\145\2\uffff\1\123\1\124\1\151\1\uffff\1\141\3\uffff\1\71\4\uffff\2\172\1\145\1\105\1\116\1\162\1\116\1\111\1\123\1\104\1\110\1\116\2\uffff\1\172\1\162\1\124\1\172\1\157\1\164\2\uffff\1\162\1\111\1\172\1\171\1\120\2\105\1\116\1\124\1\117\1\172\1\124\1\uffff\1\171\1\105\1\uffff\1\172\1\145\1\105\1\116\1\uffff\1\105\1\125\1\115\1\124\1\105\1\101\1\127\1\uffff\1\172\1\105\1\115\1\uffff\1\172\1\166\1\124\1\166\3\114\1\172\1\116\1\123\1\uffff\1\170\1\56\1\uffff\1\145\1\105\1\145\3\157\1\uffff\1\124\1\111\1\151\1\uffff\1\156\1\122\1\156\3\141\1\172\1\132\1\163\1\164\1\126\1\164\3\144\1\uffff\1\105\1\164\1\172\1\101\5\172\1\163\1\uffff\1\114\5\uffff\1\172\1\114\1\uffff\1\172\1\uffff";
    static final String DFA14_acceptS =
        "\7\uffff\1\12\2\uffff\1\17\1\20\6\uffff\1\32\1\uffff\1\34\1\35\3\uffff\1\41\1\uffff\1\43\3\uffff\1\45\3\uffff\1\52\1\53\2\uffff\1\45\5\uffff\1\13\1\10\1\14\1\11\1\12\2\uffff\1\17\1\20\5\uffff\1\31\1\32\1\uffff\1\34\1\35\3\uffff\1\41\1\uffff\1\43\1\44\1\46\1\uffff\1\47\1\50\1\51\1\52\14\uffff\1\25\1\26\6\uffff\1\1\1\2\14\uffff\1\30\2\uffff\1\37\4\uffff\1\27\7\uffff\1\24\3\uffff\1\40\12\uffff\1\23\2\uffff\1\42\6\uffff\1\15\3\uffff\1\36\17\uffff\1\16\12\uffff\1\3\1\uffff\1\4\1\5\1\6\1\7\1\21\2\uffff\1\33\1\uffff\1\22";
    static final String DFA14_specialS =
        "\1\0\37\uffff\1\2\1\1\u00a1\uffff}>";
    static final String[] DFA14_transitionS = {
            "\11\44\2\43\2\44\1\43\22\44\1\43\1\33\1\40\1\44\1\21\2\44\1\41\1\24\1\25\2\44\1\31\1\34\1\44\1\42\12\35\1\12\1\13\1\5\1\7\1\6\2\44\1\20\1\37\1\11\1\10\1\15\1\37\1\27\1\37\1\17\3\37\1\1\1\37\1\16\1\37\1\3\1\37\1\26\1\2\2\37\1\14\3\37\3\44\1\36\1\37\1\44\2\37\1\4\14\37\1\30\1\23\1\37\1\32\7\37\2\44\1\22\uff82\44",
            "\1\46\7\uffff\1\45",
            "\1\52\1\51\37\uffff\1\50",
            "\1\53",
            "\1\54",
            "\1\55",
            "\1\57",
            "",
            "\1\62",
            "\1\63",
            "",
            "",
            "\1\66",
            "\1\67",
            "\1\70",
            "\1\71",
            "\1\72",
            "\1\73",
            "",
            "\1\75",
            "",
            "",
            "\1\100",
            "\1\101",
            "\1\102",
            "",
            "\1\104",
            "",
            "\12\106",
            "\1\106\1\uffff\12\110",
            "\32\47\4\uffff\1\47\1\uffff\32\47",
            "",
            "\0\111",
            "\0\111",
            "\1\112\4\uffff\1\113",
            "",
            "",
            "\1\115",
            "\1\116",
            "",
            "\1\117",
            "\1\120",
            "\1\121",
            "\1\122",
            "\1\123",
            "",
            "",
            "",
            "",
            "",
            "\1\124",
            "\1\125",
            "",
            "",
            "\1\126\5\uffff\1\127",
            "\1\130",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\133",
            "",
            "",
            "\1\134",
            "",
            "",
            "\1\135",
            "\1\136",
            "\1\137",
            "",
            "\1\140",
            "",
            "",
            "",
            "\1\106\1\uffff\12\110",
            "",
            "",
            "",
            "",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\143",
            "\1\144",
            "\1\145",
            "\1\146",
            "\1\147\11\uffff\1\150\1\151",
            "\1\152",
            "\1\153",
            "\1\154",
            "\1\155",
            "\1\156",
            "",
            "",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\160",
            "\1\161",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\163",
            "\1\164",
            "",
            "",
            "\1\165",
            "\1\166",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\170",
            "\1\171",
            "\1\172",
            "\1\173",
            "\1\174",
            "\1\175",
            "\1\176",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u0080",
            "",
            "\1\u0081",
            "\1\u0082",
            "",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u0084",
            "\1\u0085",
            "\1\u0086",
            "",
            "\1\u0087",
            "\1\u0088",
            "\1\u0089",
            "\1\u008a",
            "\1\u008b",
            "\1\u008c",
            "\1\u008d",
            "",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u008f",
            "\1\u0090",
            "",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u0092",
            "\1\u0093",
            "\1\u0094",
            "\1\u0095",
            "\1\u0096",
            "\1\u0097",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u0099",
            "\1\u009a",
            "",
            "\1\u009b",
            "\1\u009c",
            "",
            "\1\u009d",
            "\1\u009e",
            "\1\u009f",
            "\1\u00a0",
            "\1\u00a1",
            "\1\u00a2",
            "",
            "\1\u00a3",
            "\1\u00a4",
            "\1\u00a5",
            "",
            "\1\u00a6",
            "\1\u00a7",
            "\1\u00a8",
            "\1\u00a9",
            "\1\u00aa",
            "\1\u00ab",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u00ad",
            "\1\u00ae",
            "\1\u00af",
            "\1\u00b0",
            "\1\u00b1",
            "\1\u00b2",
            "\1\u00b3",
            "\1\u00b4",
            "",
            "\1\u00b5",
            "\1\u00b6",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u00b8",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u00be",
            "",
            "\1\u00bf",
            "",
            "",
            "",
            "",
            "",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u00c1",
            "",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            ""
    };

    static final short[] DFA14_eot = DFA.unpackEncodedString(DFA14_eotS);
    static final short[] DFA14_eof = DFA.unpackEncodedString(DFA14_eofS);
    static final char[] DFA14_min = DFA.unpackEncodedStringToUnsignedChars(DFA14_minS);
    static final char[] DFA14_max = DFA.unpackEncodedStringToUnsignedChars(DFA14_maxS);
    static final short[] DFA14_accept = DFA.unpackEncodedString(DFA14_acceptS);
    static final short[] DFA14_special = DFA.unpackEncodedString(DFA14_specialS);
    static final short[][] DFA14_transition;

    static {
        int numStates = DFA14_transitionS.length;
        DFA14_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA14_transition[i] = DFA.unpackEncodedString(DFA14_transitionS[i]);
        }
    }

    class DFA14 extends DFA {

        public DFA14(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 14;
            this.eot = DFA14_eot;
            this.eof = DFA14_eof;
            this.min = DFA14_min;
            this.max = DFA14_max;
            this.accept = DFA14_accept;
            this.special = DFA14_special;
            this.transition = DFA14_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | RULE_DOUBLE | RULE_ID | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA14_0 = input.LA(1);

                        s = -1;
                        if ( (LA14_0=='M') ) {s = 1;}

                        else if ( (LA14_0=='T') ) {s = 2;}

                        else if ( (LA14_0=='Q') ) {s = 3;}

                        else if ( (LA14_0=='c') ) {s = 4;}

                        else if ( (LA14_0=='<') ) {s = 5;}

                        else if ( (LA14_0=='>') ) {s = 6;}

                        else if ( (LA14_0=='=') ) {s = 7;}

                        else if ( (LA14_0=='D') ) {s = 8;}

                        else if ( (LA14_0=='C') ) {s = 9;}

                        else if ( (LA14_0==':') ) {s = 10;}

                        else if ( (LA14_0==';') ) {s = 11;}

                        else if ( (LA14_0=='W') ) {s = 12;}

                        else if ( (LA14_0=='E') ) {s = 13;}

                        else if ( (LA14_0=='O') ) {s = 14;}

                        else if ( (LA14_0=='I') ) {s = 15;}

                        else if ( (LA14_0=='A') ) {s = 16;}

                        else if ( (LA14_0=='$') ) {s = 17;}

                        else if ( (LA14_0=='}') ) {s = 18;}

                        else if ( (LA14_0=='q') ) {s = 19;}

                        else if ( (LA14_0=='(') ) {s = 20;}

                        else if ( (LA14_0==')') ) {s = 21;}

                        else if ( (LA14_0=='S') ) {s = 22;}

                        else if ( (LA14_0=='G') ) {s = 23;}

                        else if ( (LA14_0=='p') ) {s = 24;}

                        else if ( (LA14_0==',') ) {s = 25;}

                        else if ( (LA14_0=='s') ) {s = 26;}

                        else if ( (LA14_0=='!') ) {s = 27;}

                        else if ( (LA14_0=='-') ) {s = 28;}

                        else if ( ((LA14_0>='0' && LA14_0<='9')) ) {s = 29;}

                        else if ( (LA14_0=='^') ) {s = 30;}

                        else if ( (LA14_0=='B'||LA14_0=='F'||LA14_0=='H'||(LA14_0>='J' && LA14_0<='L')||LA14_0=='N'||LA14_0=='P'||LA14_0=='R'||(LA14_0>='U' && LA14_0<='V')||(LA14_0>='X' && LA14_0<='Z')||LA14_0=='_'||(LA14_0>='a' && LA14_0<='b')||(LA14_0>='d' && LA14_0<='o')||LA14_0=='r'||(LA14_0>='t' && LA14_0<='z')) ) {s = 31;}

                        else if ( (LA14_0=='\"') ) {s = 32;}

                        else if ( (LA14_0=='\'') ) {s = 33;}

                        else if ( (LA14_0=='/') ) {s = 34;}

                        else if ( ((LA14_0>='\t' && LA14_0<='\n')||LA14_0=='\r'||LA14_0==' ') ) {s = 35;}

                        else if ( ((LA14_0>='\u0000' && LA14_0<='\b')||(LA14_0>='\u000B' && LA14_0<='\f')||(LA14_0>='\u000E' && LA14_0<='\u001F')||LA14_0=='#'||(LA14_0>='%' && LA14_0<='&')||(LA14_0>='*' && LA14_0<='+')||LA14_0=='.'||(LA14_0>='?' && LA14_0<='@')||(LA14_0>='[' && LA14_0<=']')||LA14_0=='`'||(LA14_0>='{' && LA14_0<='|')||(LA14_0>='~' && LA14_0<='\uFFFF')) ) {s = 36;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA14_33 = input.LA(1);

                        s = -1;
                        if ( ((LA14_33>='\u0000' && LA14_33<='\uFFFF')) ) {s = 73;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA14_32 = input.LA(1);

                        s = -1;
                        if ( ((LA14_32>='\u0000' && LA14_32<='\uFFFF')) ) {s = 73;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 14, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}