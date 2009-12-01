// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g 2009-12-01 16:52:45
 package de.uniol.inf.is.odysseus.cep.sase; 

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SaseLexer extends Lexer {
    public static final int INTEGER=17;
    public static final int WHERE=5;
    public static final int EGAL=8;
    public static final int LETTER=22;
    public static final int TIMEUNIT=9;
    public static final int SKIP_METHOD=10;
    public static final int NUMBER=19;
    public static final int WHITESPACE=30;
    public static final int FLOAT=18;
    public static final int NOT=13;
    public static final int SPACE=28;
    public static final int EOF=-1;
    public static final int TYPE=23;
    public static final int PATTERN=4;
    public static final int LBRACKET=15;
    public static final int KLEENEBRACKET=11;
    public static final int NAME=25;
    public static final int STRING_LITERAL=27;
    public static final int NEWLINE=29;
    public static final int NONCONTROL_CHAR=26;
    public static final int SEQ=7;
    public static final int COMMA=14;
    public static final int WITHIN=6;
    public static final int PLUS=12;
    public static final int LOWER=24;
    public static final int DIGIT=20;
    public static final int RBRACKET=16;
    public static final int UPPER=21;

    // delegates
    // delegators

    public SaseLexer() {;} 
    public SaseLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public SaseLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g"; }

    // $ANTLR start "PATTERN"
    public final void mPATTERN() throws RecognitionException {
        try {
            int _type = PATTERN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:5:9: ( 'PATTERN' | 'Pattern' )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='P') ) {
                int LA1_1 = input.LA(2);

                if ( (LA1_1=='A') ) {
                    alt1=1;
                }
                else if ( (LA1_1=='a') ) {
                    alt1=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:5:11: 'PATTERN'
                    {
                    match("PATTERN"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:5:23: 'Pattern'
                    {
                    match("Pattern"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PATTERN"

    // $ANTLR start "WHERE"
    public final void mWHERE() throws RecognitionException {
        try {
            int _type = WHERE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:6:7: ( 'WHERE' | 'Where' )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='W') ) {
                int LA2_1 = input.LA(2);

                if ( (LA2_1=='H') ) {
                    alt2=1;
                }
                else if ( (LA2_1=='h') ) {
                    alt2=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:6:9: 'WHERE'
                    {
                    match("WHERE"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:6:19: 'Where'
                    {
                    match("Where"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WHERE"

    // $ANTLR start "WITHIN"
    public final void mWITHIN() throws RecognitionException {
        try {
            int _type = WITHIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:7:8: ( 'WITHIN' | 'Within' )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='W') ) {
                int LA3_1 = input.LA(2);

                if ( (LA3_1=='I') ) {
                    alt3=1;
                }
                else if ( (LA3_1=='i') ) {
                    alt3=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 3, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:7:10: 'WITHIN'
                    {
                    match("WITHIN"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:7:21: 'Within'
                    {
                    match("Within"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WITHIN"

    // $ANTLR start "SEQ"
    public final void mSEQ() throws RecognitionException {
        try {
            int _type = SEQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:8:5: ( 'SEQ' | 'Seq' )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='S') ) {
                int LA4_1 = input.LA(2);

                if ( (LA4_1=='E') ) {
                    alt4=1;
                }
                else if ( (LA4_1=='e') ) {
                    alt4=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:8:7: 'SEQ'
                    {
                    match("SEQ"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:8:15: 'Seq'
                    {
                    match("Seq"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEQ"

    // $ANTLR start "EGAL"
    public final void mEGAL() throws RecognitionException {
        try {
            int _type = EGAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:9:6: ( 'EGAL' | 'egal' )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='E') ) {
                alt5=1;
            }
            else if ( (LA5_0=='e') ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:9:8: 'EGAL'
                    {
                    match("EGAL"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:9:17: 'egal'
                    {
                    match("egal"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EGAL"

    // $ANTLR start "TIMEUNIT"
    public final void mTIMEUNIT() throws RecognitionException {
        try {
            int _type = TIMEUNIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:11:9: ( 'hours' | 'minutes' | 'seconds' | 'days' | 'milliseconds' )
            int alt6=5;
            switch ( input.LA(1) ) {
            case 'h':
                {
                alt6=1;
                }
                break;
            case 'm':
                {
                int LA6_2 = input.LA(2);

                if ( (LA6_2=='i') ) {
                    int LA6_5 = input.LA(3);

                    if ( (LA6_5=='n') ) {
                        alt6=2;
                    }
                    else if ( (LA6_5=='l') ) {
                        alt6=5;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 5, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 2, input);

                    throw nvae;
                }
                }
                break;
            case 's':
                {
                alt6=3;
                }
                break;
            case 'd':
                {
                alt6=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:11:11: 'hours'
                    {
                    match("hours"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:11:21: 'minutes'
                    {
                    match("minutes"); 


                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:11:33: 'seconds'
                    {
                    match("seconds"); 


                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:11:45: 'days'
                    {
                    match("days"); 


                    }
                    break;
                case 5 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:11:54: 'milliseconds'
                    {
                    match("milliseconds"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TIMEUNIT"

    // $ANTLR start "SKIP_METHOD"
    public final void mSKIP_METHOD() throws RecognitionException {
        try {
            int _type = SKIP_METHOD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:14:2: ( 'skip_till_next_match' | 'skip_till_any_match' )
            int alt7=2;
            alt7 = dfa7.predict(input);
            switch (alt7) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:14:4: 'skip_till_next_match'
                    {
                    match("skip_till_next_match"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:14:27: 'skip_till_any_match'
                    {
                    match("skip_till_any_match"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SKIP_METHOD"

    // $ANTLR start "KLEENEBRACKET"
    public final void mKLEENEBRACKET() throws RecognitionException {
        try {
            int _type = KLEENEBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:17:2: ( '[]' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:17:5: '[]'
            {
            match("[]"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KLEENEBRACKET"

    // $ANTLR start "PLUS"
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:19:6: ( '+' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:19:8: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PLUS"

    // $ANTLR start "NOT"
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:21:5: ( '~' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:21:7: '~'
            {
            match('~'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOT"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:23:7: ( ',' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:23:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "LBRACKET"
    public final void mLBRACKET() throws RecognitionException {
        try {
            int _type = LBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:26:2: ( '(' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:26:4: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LBRACKET"

    // $ANTLR start "RBRACKET"
    public final void mRBRACKET() throws RecognitionException {
        try {
            int _type = RBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:28:2: ( ')' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:28:4: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RBRACKET"

    // $ANTLR start "NUMBER"
    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:31:8: ( INTEGER | FLOAT )
            int alt8=2;
            alt8 = dfa8.predict(input);
            switch (alt8) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:31:11: INTEGER
                    {
                    mINTEGER(); 

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:31:21: FLOAT
                    {
                    mFLOAT(); 

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUMBER"

    // $ANTLR start "FLOAT"
    public final void mFLOAT() throws RecognitionException {
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:16: ( INTEGER '.' ( DIGIT )+ )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:18: INTEGER '.' ( DIGIT )+
            {
            mINTEGER(); 
            match('.'); 
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:30: ( DIGIT )+
            int cnt9=0;
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>='0' && LA9_0<='9')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:30: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt9 >= 1 ) break loop9;
                        EarlyExitException eee =
                            new EarlyExitException(9, input);
                        throw eee;
                }
                cnt9++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "FLOAT"

    // $ANTLR start "INTEGER"
    public final void mINTEGER() throws RecognitionException {
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:33:18: ( '0' | '1' .. '9' ( DIGIT )* )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0=='0') ) {
                alt11=1;
            }
            else if ( ((LA11_0>='1' && LA11_0<='9')) ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:33:20: '0'
                    {
                    match('0'); 

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:33:26: '1' .. '9' ( DIGIT )*
                    {
                    matchRange('1','9'); 
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:33:35: ( DIGIT )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( ((LA10_0>='0' && LA10_0<='9')) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:33:35: DIGIT
                    	    {
                    	    mDIGIT(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);


                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end "INTEGER"

    // $ANTLR start "TYPE"
    public final void mTYPE() throws RecognitionException {
        try {
            int _type = TYPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:35:6: ( UPPER ( LETTER | DIGIT | '_' )* )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:35:8: UPPER ( LETTER | DIGIT | '_' )*
            {
            mUPPER(); 
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:35:14: ( LETTER | DIGIT | '_' )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>='0' && LA12_0<='9')||(LA12_0>='A' && LA12_0<='Z')||LA12_0=='_'||(LA12_0>='a' && LA12_0<='z')) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
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
            	    break loop12;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TYPE"

    // $ANTLR start "NAME"
    public final void mNAME() throws RecognitionException {
        try {
            int _type = NAME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:36:6: ( LOWER ( LETTER | DIGIT | '_' )* )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:36:8: LOWER ( LETTER | DIGIT | '_' )*
            {
            mLOWER(); 
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:36:14: ( LETTER | DIGIT | '_' )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>='0' && LA13_0<='9')||(LA13_0>='A' && LA13_0<='Z')||LA13_0=='_'||(LA13_0>='a' && LA13_0<='z')) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
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
            	    break loop13;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NAME"

    // $ANTLR start "STRING_LITERAL"
    public final void mSTRING_LITERAL() throws RecognitionException {
        try {
            int _type = STRING_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:37:16: ( '\"' ( NONCONTROL_CHAR )* '\"' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:37:18: '\"' ( NONCONTROL_CHAR )* '\"'
            {
            match('\"'); 
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:37:22: ( NONCONTROL_CHAR )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0=='\t'||LA14_0==' '||(LA14_0>='0' && LA14_0<='9')||(LA14_0>='A' && LA14_0<='Z')||(LA14_0>='a' && LA14_0<='z')) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:37:22: NONCONTROL_CHAR
            	    {
            	    mNONCONTROL_CHAR(); 

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING_LITERAL"

    // $ANTLR start "NONCONTROL_CHAR"
    public final void mNONCONTROL_CHAR() throws RecognitionException {
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:39:26: ( LETTER | DIGIT | SPACE )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
            {
            if ( input.LA(1)=='\t'||input.LA(1)==' '||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "NONCONTROL_CHAR"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:40:17: ( LOWER | UPPER )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "LETTER"

    // $ANTLR start "LOWER"
    public final void mLOWER() throws RecognitionException {
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:41:17: ( 'a' .. 'z' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:41:19: 'a' .. 'z'
            {
            matchRange('a','z'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "LOWER"

    // $ANTLR start "UPPER"
    public final void mUPPER() throws RecognitionException {
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:42:17: ( 'A' .. 'Z' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:42:19: 'A' .. 'Z'
            {
            matchRange('A','Z'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "UPPER"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:43:17: ( '0' .. '9' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:43:19: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "SPACE"
    public final void mSPACE() throws RecognitionException {
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:44:17: ( ' ' | '\\t' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
            {
            if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "SPACE"

    // $ANTLR start "NEWLINE"
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:46:9: ( ( ( '\\r' )? '\\n' )+ )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:46:11: ( ( '\\r' )? '\\n' )+
            {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:46:11: ( ( '\\r' )? '\\n' )+
            int cnt16=0;
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0=='\n'||LA16_0=='\r') ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:46:12: ( '\\r' )? '\\n'
            	    {
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:46:12: ( '\\r' )?
            	    int alt15=2;
            	    int LA15_0 = input.LA(1);

            	    if ( (LA15_0=='\r') ) {
            	        alt15=1;
            	    }
            	    switch (alt15) {
            	        case 1 :
            	            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:46:12: '\\r'
            	            {
            	            match('\r'); 

            	            }
            	            break;

            	    }

            	    match('\n'); 

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

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEWLINE"

    // $ANTLR start "WHITESPACE"
    public final void mWHITESPACE() throws RecognitionException {
        try {
            int _type = WHITESPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:48:12: ( ( SPACE )+ )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:48:14: ( SPACE )+
            {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:48:14: ( SPACE )+
            int cnt17=0;
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0=='\t'||LA17_0==' ') ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:48:14: SPACE
            	    {
            	    mSPACE(); 

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

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WHITESPACE"

    public void mTokens() throws RecognitionException {
        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:8: ( PATTERN | WHERE | WITHIN | SEQ | EGAL | TIMEUNIT | SKIP_METHOD | KLEENEBRACKET | PLUS | NOT | COMMA | LBRACKET | RBRACKET | NUMBER | TYPE | NAME | STRING_LITERAL | NEWLINE | WHITESPACE )
        int alt18=19;
        alt18 = dfa18.predict(input);
        switch (alt18) {
            case 1 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:10: PATTERN
                {
                mPATTERN(); 

                }
                break;
            case 2 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:18: WHERE
                {
                mWHERE(); 

                }
                break;
            case 3 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:24: WITHIN
                {
                mWITHIN(); 

                }
                break;
            case 4 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:31: SEQ
                {
                mSEQ(); 

                }
                break;
            case 5 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:35: EGAL
                {
                mEGAL(); 

                }
                break;
            case 6 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:40: TIMEUNIT
                {
                mTIMEUNIT(); 

                }
                break;
            case 7 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:49: SKIP_METHOD
                {
                mSKIP_METHOD(); 

                }
                break;
            case 8 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:61: KLEENEBRACKET
                {
                mKLEENEBRACKET(); 

                }
                break;
            case 9 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:75: PLUS
                {
                mPLUS(); 

                }
                break;
            case 10 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:80: NOT
                {
                mNOT(); 

                }
                break;
            case 11 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:84: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 12 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:90: LBRACKET
                {
                mLBRACKET(); 

                }
                break;
            case 13 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:99: RBRACKET
                {
                mRBRACKET(); 

                }
                break;
            case 14 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:108: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 15 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:115: TYPE
                {
                mTYPE(); 

                }
                break;
            case 16 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:120: NAME
                {
                mNAME(); 

                }
                break;
            case 17 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:125: STRING_LITERAL
                {
                mSTRING_LITERAL(); 

                }
                break;
            case 18 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:140: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 19 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:148: WHITESPACE
                {
                mWHITESPACE(); 

                }
                break;

        }

    }


    protected DFA7 dfa7 = new DFA7(this);
    protected DFA8 dfa8 = new DFA8(this);
    protected DFA18 dfa18 = new DFA18(this);
    static final String DFA7_eotS =
        "\15\uffff";
    static final String DFA7_eofS =
        "\15\uffff";
    static final String DFA7_minS =
        "\1\163\1\153\1\151\1\160\1\137\1\164\1\151\2\154\1\137\1\141\2"+
        "\uffff";
    static final String DFA7_maxS =
        "\1\163\1\153\1\151\1\160\1\137\1\164\1\151\2\154\1\137\1\156\2"+
        "\uffff";
    static final String DFA7_acceptS =
        "\13\uffff\1\1\1\2";
    static final String DFA7_specialS =
        "\15\uffff}>";
    static final String[] DFA7_transitionS = {
            "\1\1",
            "\1\2",
            "\1\3",
            "\1\4",
            "\1\5",
            "\1\6",
            "\1\7",
            "\1\10",
            "\1\11",
            "\1\12",
            "\1\14\14\uffff\1\13",
            "",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "13:1: SKIP_METHOD : ( 'skip_till_next_match' | 'skip_till_any_match' );";
        }
    }
    static final String DFA8_eotS =
        "\1\uffff\2\3\2\uffff\1\3";
    static final String DFA8_eofS =
        "\6\uffff";
    static final String DFA8_minS =
        "\1\60\2\56\2\uffff\1\56";
    static final String DFA8_maxS =
        "\1\71\1\56\1\71\2\uffff\1\71";
    static final String DFA8_acceptS =
        "\3\uffff\1\1\1\2\1\uffff";
    static final String DFA8_specialS =
        "\6\uffff}>";
    static final String[] DFA8_transitionS = {
            "\1\1\11\2",
            "\1\4",
            "\1\4\1\uffff\12\5",
            "",
            "",
            "\1\4\1\uffff\12\5"
    };

    static final short[] DFA8_eot = DFA.unpackEncodedString(DFA8_eotS);
    static final short[] DFA8_eof = DFA.unpackEncodedString(DFA8_eofS);
    static final char[] DFA8_min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);
    static final char[] DFA8_max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);
    static final short[] DFA8_accept = DFA.unpackEncodedString(DFA8_acceptS);
    static final short[] DFA8_special = DFA.unpackEncodedString(DFA8_specialS);
    static final short[][] DFA8_transition;

    static {
        int numStates = DFA8_transitionS.length;
        DFA8_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA8_transition[i] = DFA.unpackEncodedString(DFA8_transitionS[i]);
        }
    }

    class DFA8 extends DFA {

        public DFA8(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 8;
            this.eot = DFA8_eot;
            this.eof = DFA8_eof;
            this.min = DFA8_min;
            this.max = DFA8_max;
            this.accept = DFA8_accept;
            this.special = DFA8_special;
            this.transition = DFA8_transition;
        }
        public String getDescription() {
            return "31:1: NUMBER : ( INTEGER | FLOAT );";
        }
    }
    static final String DFA18_eotS =
        "\1\uffff\4\21\5\22\14\uffff\11\21\6\22\6\21\2\73\1\21\7\22\6\21"+
        "\1\uffff\2\112\5\22\1\120\2\21\2\123\2\21\1\uffff\1\120\4\22\1\uffff"+
        "\2\21\1\uffff\2\134\4\22\2\141\1\uffff\1\120\1\22\1\120\1\22\1\uffff"+
        "\11\22\1\120\17\22\2\175\1\uffff";
    static final String DFA18_eofS =
        "\176\uffff";
    static final String DFA18_minS =
        "\1\11\1\101\1\110\1\105\1\107\1\147\1\157\1\151\1\145\1\141\14"+
        "\uffff\1\124\1\164\1\105\1\145\1\124\1\164\1\121\1\161\1\101\1\141"+
        "\1\165\1\154\1\143\1\151\1\171\1\124\1\164\1\122\1\162\1\110\1\150"+
        "\2\60\1\114\1\154\1\162\1\165\1\154\1\157\1\160\1\163\1\105\1\145"+
        "\1\105\1\145\1\111\1\151\1\uffff\2\60\1\163\1\164\1\151\1\156\1"+
        "\137\1\60\1\122\1\162\2\60\1\116\1\156\1\uffff\1\60\1\145\1\163"+
        "\1\144\1\164\1\uffff\1\116\1\156\1\uffff\2\60\1\163\1\145\1\163"+
        "\1\151\2\60\1\uffff\1\60\1\143\1\60\1\154\1\uffff\1\157\1\154\1"+
        "\156\1\137\1\144\1\141\1\163\1\145\1\156\1\60\1\170\1\171\1\164"+
        "\2\137\2\155\2\141\2\164\2\143\2\150\2\60\1\uffff";
    static final String DFA18_maxS =
        "\1\176\1\141\1\151\1\145\1\107\1\147\1\157\1\151\1\153\1\141\14"+
        "\uffff\1\124\1\164\1\105\1\145\1\124\1\164\1\121\1\161\1\101\1\141"+
        "\1\165\1\156\1\143\1\151\1\171\1\124\1\164\1\122\1\162\1\110\1\150"+
        "\2\172\1\114\1\154\1\162\1\165\1\154\1\157\1\160\1\163\1\105\1\145"+
        "\1\105\1\145\1\111\1\151\1\uffff\2\172\1\163\1\164\1\151\1\156\1"+
        "\137\1\172\1\122\1\162\2\172\1\116\1\156\1\uffff\1\172\1\145\1\163"+
        "\1\144\1\164\1\uffff\1\116\1\156\1\uffff\2\172\1\163\1\145\1\163"+
        "\1\151\2\172\1\uffff\1\172\1\143\1\172\1\154\1\uffff\1\157\1\154"+
        "\1\156\1\137\1\144\1\156\1\163\1\145\1\156\1\172\1\170\1\171\1\164"+
        "\2\137\2\155\2\141\2\164\2\143\2\150\2\172\1\uffff";
    static final String DFA18_acceptS =
        "\12\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16\1\17\1\20\1\21\1\22"+
        "\1\23\45\uffff\1\4\16\uffff\1\5\5\uffff\1\6\2\uffff\1\2\10\uffff"+
        "\1\3\4\uffff\1\1\33\uffff\1\7";
    static final String DFA18_specialS =
        "\176\uffff}>";
    static final String[] DFA18_transitionS = {
            "\1\25\1\24\2\uffff\1\24\22\uffff\1\25\1\uffff\1\23\5\uffff"+
            "\1\16\1\17\1\uffff\1\13\1\15\3\uffff\12\20\7\uffff\4\21\1\4"+
            "\12\21\1\1\2\21\1\3\3\21\1\2\3\21\1\12\5\uffff\3\22\1\11\1\5"+
            "\2\22\1\6\4\22\1\7\5\22\1\10\7\22\3\uffff\1\14",
            "\1\26\37\uffff\1\27",
            "\1\30\1\32\36\uffff\1\31\1\33",
            "\1\34\37\uffff\1\35",
            "\1\36",
            "\1\37",
            "\1\40",
            "\1\41",
            "\1\42\5\uffff\1\43",
            "\1\44",
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
            "\1\45",
            "\1\46",
            "\1\47",
            "\1\50",
            "\1\51",
            "\1\52",
            "\1\53",
            "\1\54",
            "\1\55",
            "\1\56",
            "\1\57",
            "\1\61\1\uffff\1\60",
            "\1\62",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\66",
            "\1\67",
            "\1\70",
            "\1\71",
            "\1\72",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\74",
            "\1\75",
            "\1\76",
            "\1\77",
            "\1\100",
            "\1\101",
            "\1\102",
            "\1\103",
            "\1\104",
            "\1\105",
            "\1\106",
            "\1\107",
            "\1\110",
            "\1\111",
            "",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\116",
            "\1\117",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\121",
            "\1\122",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\124",
            "\1\125",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\126",
            "\1\127",
            "\1\130",
            "\1\131",
            "",
            "\1\132",
            "\1\133",
            "",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\135",
            "\1\136",
            "\1\137",
            "\1\140",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\142",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\143",
            "",
            "\1\144",
            "\1\145",
            "\1\146",
            "\1\147",
            "\1\150",
            "\1\152\14\uffff\1\151",
            "\1\153",
            "\1\154",
            "\1\155",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\156",
            "\1\157",
            "\1\160",
            "\1\161",
            "\1\162",
            "\1\163",
            "\1\164",
            "\1\165",
            "\1\166",
            "\1\167",
            "\1\170",
            "\1\171",
            "\1\172",
            "\1\173",
            "\1\174",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            ""
    };

    static final short[] DFA18_eot = DFA.unpackEncodedString(DFA18_eotS);
    static final short[] DFA18_eof = DFA.unpackEncodedString(DFA18_eofS);
    static final char[] DFA18_min = DFA.unpackEncodedStringToUnsignedChars(DFA18_minS);
    static final char[] DFA18_max = DFA.unpackEncodedStringToUnsignedChars(DFA18_maxS);
    static final short[] DFA18_accept = DFA.unpackEncodedString(DFA18_acceptS);
    static final short[] DFA18_special = DFA.unpackEncodedString(DFA18_specialS);
    static final short[][] DFA18_transition;

    static {
        int numStates = DFA18_transitionS.length;
        DFA18_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA18_transition[i] = DFA.unpackEncodedString(DFA18_transitionS[i]);
        }
    }

    class DFA18 extends DFA {

        public DFA18(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 18;
            this.eot = DFA18_eot;
            this.eof = DFA18_eof;
            this.min = DFA18_min;
            this.max = DFA18_max;
            this.accept = DFA18_accept;
            this.special = DFA18_special;
            this.transition = DFA18_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( PATTERN | WHERE | WITHIN | SEQ | EGAL | TIMEUNIT | SKIP_METHOD | KLEENEBRACKET | PLUS | NOT | COMMA | LBRACKET | RBRACKET | NUMBER | TYPE | NAME | STRING_LITERAL | NEWLINE | WHITESPACE );";
        }
    }
 

}