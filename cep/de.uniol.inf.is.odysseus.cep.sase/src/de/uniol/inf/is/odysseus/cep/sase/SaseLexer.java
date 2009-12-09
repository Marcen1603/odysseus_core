// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g 2009-12-09 11:45:16
 package de.uniol.inf.is.odysseus.cep.sase; 

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SaseLexer extends Lexer {
    public static final int WHERE=6;
    public static final int POINT=25;
    public static final int LETTER=39;
    public static final int EQUALS=30;
    public static final int FLOAT=36;
    public static final int NOT=31;
    public static final int AND=11;
    public static final int EOF=-1;
    public static final int SPACE=42;
    public static final int PATTERN=5;
    public static final int LBRACKET=33;
    public static final int SINGLEEQUALS=29;
    public static final int NAME=16;
    public static final int STRING_LITERAL=41;
    public static final int LEFTCURLY=9;
    public static final int SEQ=8;
    public static final int COMMA=32;
    public static final int PREVIOUS=14;
    public static final int RIGHTCURLY=10;
    public static final int DIVISION=26;
    public static final int PLUS=23;
    public static final int DIGIT=38;
    public static final int LAST=17;
    public static final int RBRACKET=34;
    public static final int BBRACKETRIGHT=19;
    public static final int INTEGER=35;
    public static final int ALLTOPREVIOUS=15;
    public static final int TIMEUNIT=20;
    public static final int SKIP_METHOD=21;
    public static final int NUMBER=37;
    public static final int WHITESPACE=46;
    public static final int HAVING=4;
    public static final int MINUS=24;
    public static final int MULT=27;
    public static final int AGGREGATEOP=22;
    public static final int CURRENT=13;
    public static final int NEWLINE=45;
    public static final int NONCONTROL_CHAR=40;
    public static final int BBRACKETLEFT=18;
    public static final int WITHIN=7;
    public static final int LOWER=43;
    public static final int COMPAREOP=28;
    public static final int UPPER=44;
    public static final int FIRST=12;

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

    // $ANTLR start "HAVING"
    public final void mHAVING() throws RecognitionException {
        try {
            int _type = HAVING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:5:8: ( 'HAVING' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:5:10: 'HAVING'
            {
            match("HAVING"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HAVING"

    // $ANTLR start "PATTERN"
    public final void mPATTERN() throws RecognitionException {
        try {
            int _type = PATTERN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:6:9: ( 'PATTERN' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:6:11: 'PATTERN'
            {
            match("PATTERN"); 


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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:7:7: ( 'WHERE' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:7:9: 'WHERE'
            {
            match("WHERE"); 


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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:8:8: ( 'WITHIN' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:8:10: 'WITHIN'
            {
            match("WITHIN"); 


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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:9:5: ( 'SEQ' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:9:7: 'SEQ'
            {
            match("SEQ"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEQ"

    // $ANTLR start "LEFTCURLY"
    public final void mLEFTCURLY() throws RecognitionException {
        try {
            int _type = LEFTCURLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:10:11: ( '{' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:10:13: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LEFTCURLY"

    // $ANTLR start "RIGHTCURLY"
    public final void mRIGHTCURLY() throws RecognitionException {
        try {
            int _type = RIGHTCURLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:11:12: ( '}' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:11:14: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RIGHTCURLY"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:12:5: ( 'AND' | 'and' )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='A') ) {
                alt1=1;
            }
            else if ( (LA1_0=='a') ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:12:6: 'AND'
                    {
                    match("AND"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:12:12: 'and'
                    {
                    match("and"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AND"

    // $ANTLR start "FIRST"
    public final void mFIRST() throws RecognitionException {
        try {
            int _type = FIRST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:13:7: ( '[1]' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:13:8: '[1]'
            {
            match("[1]"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FIRST"

    // $ANTLR start "CURRENT"
    public final void mCURRENT() throws RecognitionException {
        try {
            int _type = CURRENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:14:9: ( '[i]' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:14:10: '[i]'
            {
            match("[i]"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CURRENT"

    // $ANTLR start "PREVIOUS"
    public final void mPREVIOUS() throws RecognitionException {
        try {
            int _type = PREVIOUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:15:10: ( '[i-1]' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:15:13: '[i-1]'
            {
            match("[i-1]"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PREVIOUS"

    // $ANTLR start "ALLTOPREVIOUS"
    public final void mALLTOPREVIOUS() throws RecognitionException {
        try {
            int _type = ALLTOPREVIOUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:16:15: ( '[..i-1]' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:16:18: '[..i-1]'
            {
            match("[..i-1]"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ALLTOPREVIOUS"

    // $ANTLR start "LAST"
    public final void mLAST() throws RecognitionException {
        try {
            int _type = LAST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:17:7: ( '[' NAME '.LEN]' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:17:10: '[' NAME '.LEN]'
            {
            match('['); 
            mNAME(); 
            match(".LEN]"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LAST"

    // $ANTLR start "BBRACKETLEFT"
    public final void mBBRACKETLEFT() throws RecognitionException {
        try {
            int _type = BBRACKETLEFT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:19:14: ( '[' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:19:17: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BBRACKETLEFT"

    // $ANTLR start "BBRACKETRIGHT"
    public final void mBBRACKETRIGHT() throws RecognitionException {
        try {
            int _type = BBRACKETRIGHT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:20:15: ( ']' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:20:18: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BBRACKETRIGHT"

    // $ANTLR start "TIMEUNIT"
    public final void mTIMEUNIT() throws RecognitionException {
        try {
            int _type = TIMEUNIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:22:9: ( 'hour' | 'minute' | 'second' | 'day' | 'millisecond' | 'hours' | 'minutes' | 'seconds' | 'days' | 'milliseconds' )
            int alt2=10;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:22:11: 'hour'
                    {
                    match("hour"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:22:19: 'minute'
                    {
                    match("minute"); 


                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:22:30: 'second'
                    {
                    match("second"); 


                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:22:41: 'day'
                    {
                    match("day"); 


                    }
                    break;
                case 5 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:22:49: 'millisecond'
                    {
                    match("millisecond"); 


                    }
                    break;
                case 6 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:22:65: 'hours'
                    {
                    match("hours"); 


                    }
                    break;
                case 7 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:22:75: 'minutes'
                    {
                    match("minutes"); 


                    }
                    break;
                case 8 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:22:87: 'seconds'
                    {
                    match("seconds"); 


                    }
                    break;
                case 9 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:22:99: 'days'
                    {
                    match("days"); 


                    }
                    break;
                case 10 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:22:108: 'milliseconds'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:25:2: ( 'skip_till_next_match' | 'skip_till_any_match' | 'strict_contiguity' | 'partition_contiguity' )
            int alt3=4;
            alt3 = dfa3.predict(input);
            switch (alt3) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:25:4: 'skip_till_next_match'
                    {
                    match("skip_till_next_match"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:25:27: 'skip_till_any_match'
                    {
                    match("skip_till_any_match"); 


                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:25:49: 'strict_contiguity'
                    {
                    match("strict_contiguity"); 


                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:25:69: 'partition_contiguity'
                    {
                    match("partition_contiguity"); 


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

    // $ANTLR start "AGGREGATEOP"
    public final void mAGGREGATEOP() throws RecognitionException {
        try {
            int _type = AGGREGATEOP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:27:13: ( 'Avg' | 'Min' | 'Max' | 'Sum' | 'Count' )
            int alt4=5;
            switch ( input.LA(1) ) {
            case 'A':
                {
                alt4=1;
                }
                break;
            case 'M':
                {
                int LA4_2 = input.LA(2);

                if ( (LA4_2=='i') ) {
                    alt4=2;
                }
                else if ( (LA4_2=='a') ) {
                    alt4=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 2, input);

                    throw nvae;
                }
                }
                break;
            case 'S':
                {
                alt4=4;
                }
                break;
            case 'C':
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
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:27:15: 'Avg'
                    {
                    match("Avg"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:27:23: 'Min'
                    {
                    match("Min"); 


                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:27:31: 'Max'
                    {
                    match("Max"); 


                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:27:39: 'Sum'
                    {
                    match("Sum"); 


                    }
                    break;
                case 5 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:27:47: 'Count'
                    {
                    match("Count"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AGGREGATEOP"

    // $ANTLR start "PLUS"
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:30:6: ( '+' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:30:8: '+'
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

    // $ANTLR start "MINUS"
    public final void mMINUS() throws RecognitionException {
        try {
            int _type = MINUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:31:7: ( '-' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:31:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MINUS"

    // $ANTLR start "POINT"
    public final void mPOINT() throws RecognitionException {
        try {
            int _type = POINT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:7: ( '.' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "POINT"

    // $ANTLR start "DIVISION"
    public final void mDIVISION() throws RecognitionException {
        try {
            int _type = DIVISION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:33:9: ( '/' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:33:11: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DIVISION"

    // $ANTLR start "MULT"
    public final void mMULT() throws RecognitionException {
        try {
            int _type = MULT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:34:6: ( '*' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:34:8: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MULT"

    // $ANTLR start "COMPAREOP"
    public final void mCOMPAREOP() throws RecognitionException {
        try {
            int _type = COMPAREOP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:36:11: ( '<=' | '>=' | '!=' | '<' | '>' )
            int alt5=5;
            switch ( input.LA(1) ) {
            case '<':
                {
                int LA5_1 = input.LA(2);

                if ( (LA5_1=='=') ) {
                    alt5=1;
                }
                else {
                    alt5=4;}
                }
                break;
            case '>':
                {
                int LA5_2 = input.LA(2);

                if ( (LA5_2=='=') ) {
                    alt5=2;
                }
                else {
                    alt5=5;}
                }
                break;
            case '!':
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
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:36:13: '<='
                    {
                    match("<="); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:36:18: '>='
                    {
                    match(">="); 


                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:36:23: '!='
                    {
                    match("!="); 


                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:36:28: '<'
                    {
                    match('<'); 

                    }
                    break;
                case 5 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:36:32: '>'
                    {
                    match('>'); 

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMPAREOP"

    // $ANTLR start "SINGLEEQUALS"
    public final void mSINGLEEQUALS() throws RecognitionException {
        try {
            int _type = SINGLEEQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:37:14: ( '=' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:37:16: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SINGLEEQUALS"

    // $ANTLR start "EQUALS"
    public final void mEQUALS() throws RecognitionException {
        try {
            int _type = EQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:38:9: ( '==' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:38:11: '=='
            {
            match("=="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQUALS"

    // $ANTLR start "NOT"
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:40:5: ( '~' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:40:7: '~'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:42:7: ( ',' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:42:9: ','
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:45:2: ( '(' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:45:4: '('
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:2: ( ')' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:4: ')'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:50:8: ( INTEGER | FLOAT )
            int alt6=2;
            alt6 = dfa6.predict(input);
            switch (alt6) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:50:11: INTEGER
                    {
                    mINTEGER(); 

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:50:21: FLOAT
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:51:16: ( INTEGER '.' ( DIGIT )+ )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:51:18: INTEGER '.' ( DIGIT )+
            {
            mINTEGER(); 
            match('.'); 
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:51:30: ( DIGIT )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='0' && LA7_0<='9')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:51:30: DIGIT
            	    {
            	    mDIGIT(); 

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
        finally {
        }
    }
    // $ANTLR end "FLOAT"

    // $ANTLR start "INTEGER"
    public final void mINTEGER() throws RecognitionException {
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:52:18: ( '0' | '1' .. '9' ( '0' .. '9' )* )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='0') ) {
                alt9=1;
            }
            else if ( ((LA9_0>='1' && LA9_0<='9')) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:52:20: '0'
                    {
                    match('0'); 

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:52:26: '1' .. '9' ( '0' .. '9' )*
                    {
                    matchRange('1','9'); 
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:52:35: ( '0' .. '9' )*
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( ((LA8_0>='0' && LA8_0<='9')) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:52:36: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop8;
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

    // $ANTLR start "NAME"
    public final void mNAME() throws RecognitionException {
        try {
            int _type = NAME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:54:6: ( LETTER ( LETTER | DIGIT | '_' | ':' )* )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:54:8: LETTER ( LETTER | DIGIT | '_' | ':' )*
            {
            mLETTER(); 
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:54:15: ( LETTER | DIGIT | '_' | ':' )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( ((LA10_0>='0' && LA10_0<=':')||(LA10_0>='A' && LA10_0<='Z')||LA10_0=='_'||(LA10_0>='a' && LA10_0<='z')) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<=':')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:55:16: ( '\"' ( NONCONTROL_CHAR )* '\"' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:55:18: '\"' ( NONCONTROL_CHAR )* '\"'
            {
            match('\"'); 
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:55:22: ( NONCONTROL_CHAR )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0=='\t'||LA11_0==' '||(LA11_0>='0' && LA11_0<='9')||(LA11_0>='A' && LA11_0<='Z')||(LA11_0>='a' && LA11_0<='z')) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:55:22: NONCONTROL_CHAR
            	    {
            	    mNONCONTROL_CHAR(); 

            	    }
            	    break;

            	default :
            	    break loop11;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:57:26: ( LETTER | DIGIT | SPACE )
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:58:17: ( LOWER | UPPER )
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:59:17: ( 'a' .. 'z' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:59:19: 'a' .. 'z'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:60:17: ( 'A' .. 'Z' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:60:19: 'A' .. 'Z'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:61:17: ( '0' .. '9' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:61:19: '0' .. '9'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:62:17: ( ' ' | '\\t' )
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:64:9: ( ( ( '\\r' )? '\\n' )+ )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:64:11: ( ( '\\r' )? '\\n' )+
            {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:64:11: ( ( '\\r' )? '\\n' )+
            int cnt13=0;
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0=='\n'||LA13_0=='\r') ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:64:12: ( '\\r' )? '\\n'
            	    {
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:64:12: ( '\\r' )?
            	    int alt12=2;
            	    int LA12_0 = input.LA(1);

            	    if ( (LA12_0=='\r') ) {
            	        alt12=1;
            	    }
            	    switch (alt12) {
            	        case 1 :
            	            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:64:12: '\\r'
            	            {
            	            match('\r'); 

            	            }
            	            break;

            	    }

            	    match('\n'); 

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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:66:12: ( ( SPACE )+ )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:66:14: ( SPACE )+
            {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:66:14: ( SPACE )+
            int cnt14=0;
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0=='\t'||LA14_0==' ') ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:66:14: SPACE
            	    {
            	    mSPACE(); 

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
        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:8: ( HAVING | PATTERN | WHERE | WITHIN | SEQ | LEFTCURLY | RIGHTCURLY | AND | FIRST | CURRENT | PREVIOUS | ALLTOPREVIOUS | LAST | BBRACKETLEFT | BBRACKETRIGHT | TIMEUNIT | SKIP_METHOD | AGGREGATEOP | PLUS | MINUS | POINT | DIVISION | MULT | COMPAREOP | SINGLEEQUALS | EQUALS | NOT | COMMA | LBRACKET | RBRACKET | NUMBER | NAME | STRING_LITERAL | NEWLINE | WHITESPACE )
        int alt15=35;
        alt15 = dfa15.predict(input);
        switch (alt15) {
            case 1 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:10: HAVING
                {
                mHAVING(); 

                }
                break;
            case 2 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:17: PATTERN
                {
                mPATTERN(); 

                }
                break;
            case 3 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:25: WHERE
                {
                mWHERE(); 

                }
                break;
            case 4 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:31: WITHIN
                {
                mWITHIN(); 

                }
                break;
            case 5 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:38: SEQ
                {
                mSEQ(); 

                }
                break;
            case 6 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:42: LEFTCURLY
                {
                mLEFTCURLY(); 

                }
                break;
            case 7 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:52: RIGHTCURLY
                {
                mRIGHTCURLY(); 

                }
                break;
            case 8 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:63: AND
                {
                mAND(); 

                }
                break;
            case 9 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:67: FIRST
                {
                mFIRST(); 

                }
                break;
            case 10 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:73: CURRENT
                {
                mCURRENT(); 

                }
                break;
            case 11 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:81: PREVIOUS
                {
                mPREVIOUS(); 

                }
                break;
            case 12 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:90: ALLTOPREVIOUS
                {
                mALLTOPREVIOUS(); 

                }
                break;
            case 13 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:104: LAST
                {
                mLAST(); 

                }
                break;
            case 14 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:109: BBRACKETLEFT
                {
                mBBRACKETLEFT(); 

                }
                break;
            case 15 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:122: BBRACKETRIGHT
                {
                mBBRACKETRIGHT(); 

                }
                break;
            case 16 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:136: TIMEUNIT
                {
                mTIMEUNIT(); 

                }
                break;
            case 17 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:145: SKIP_METHOD
                {
                mSKIP_METHOD(); 

                }
                break;
            case 18 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:157: AGGREGATEOP
                {
                mAGGREGATEOP(); 

                }
                break;
            case 19 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:169: PLUS
                {
                mPLUS(); 

                }
                break;
            case 20 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:174: MINUS
                {
                mMINUS(); 

                }
                break;
            case 21 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:180: POINT
                {
                mPOINT(); 

                }
                break;
            case 22 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:186: DIVISION
                {
                mDIVISION(); 

                }
                break;
            case 23 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:195: MULT
                {
                mMULT(); 

                }
                break;
            case 24 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:200: COMPAREOP
                {
                mCOMPAREOP(); 

                }
                break;
            case 25 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:210: SINGLEEQUALS
                {
                mSINGLEEQUALS(); 

                }
                break;
            case 26 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:223: EQUALS
                {
                mEQUALS(); 

                }
                break;
            case 27 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:230: NOT
                {
                mNOT(); 

                }
                break;
            case 28 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:234: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 29 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:240: LBRACKET
                {
                mLBRACKET(); 

                }
                break;
            case 30 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:249: RBRACKET
                {
                mRBRACKET(); 

                }
                break;
            case 31 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:258: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 32 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:265: NAME
                {
                mNAME(); 

                }
                break;
            case 33 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:270: STRING_LITERAL
                {
                mSTRING_LITERAL(); 

                }
                break;
            case 34 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:285: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 35 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:293: WHITESPACE
                {
                mWHITESPACE(); 

                }
                break;

        }

    }


    protected DFA2 dfa2 = new DFA2(this);
    protected DFA3 dfa3 = new DFA3(this);
    protected DFA6 dfa6 = new DFA6(this);
    protected DFA15 dfa15 = new DFA15(this);
    static final String DFA2_eotS =
        "\15\uffff\1\23\1\25\12\uffff\1\35\1\uffff\1\40\10\uffff\1\46\2"+
        "\uffff";
    static final String DFA2_eofS =
        "\47\uffff";
    static final String DFA2_minS =
        "\1\144\1\157\1\151\1\145\1\141\1\165\1\154\1\143\1\171\1\162\1"+
        "\165\1\154\1\157\2\163\1\164\1\151\1\156\4\uffff\1\145\1\163\1\144"+
        "\1\163\1\145\1\163\2\uffff\1\143\2\uffff\1\157\1\156\1\144\1\163"+
        "\2\uffff";
    static final String DFA2_maxS =
        "\1\163\1\157\1\151\1\145\1\141\1\165\1\156\1\143\1\171\1\162\1"+
        "\165\1\154\1\157\2\163\1\164\1\151\1\156\4\uffff\1\145\1\163\1\144"+
        "\1\163\1\145\1\163\2\uffff\1\143\2\uffff\1\157\1\156\1\144\1\163"+
        "\2\uffff";
    static final String DFA2_acceptS =
        "\22\uffff\1\11\1\4\1\6\1\1\6\uffff\1\7\1\2\1\uffff\1\10\1\3\4\uffff"+
        "\1\12\1\5";
    static final String DFA2_specialS =
        "\47\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\4\3\uffff\1\1\4\uffff\1\2\5\uffff\1\3",
            "\1\5",
            "\1\6",
            "\1\7",
            "\1\10",
            "\1\11",
            "\1\13\1\uffff\1\12",
            "\1\14",
            "\1\15",
            "\1\16",
            "\1\17",
            "\1\20",
            "\1\21",
            "\1\22",
            "\1\24",
            "\1\26",
            "\1\27",
            "\1\30",
            "",
            "",
            "",
            "",
            "\1\31",
            "\1\32",
            "\1\33",
            "\1\34",
            "\1\36",
            "\1\37",
            "",
            "",
            "\1\41",
            "",
            "",
            "\1\42",
            "\1\43",
            "\1\44",
            "\1\45",
            "",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "22:1: TIMEUNIT : ( 'hour' | 'minute' | 'second' | 'day' | 'millisecond' | 'hours' | 'minutes' | 'seconds' | 'days' | 'milliseconds' );";
        }
    }
    static final String DFA3_eotS =
        "\17\uffff";
    static final String DFA3_eofS =
        "\17\uffff";
    static final String DFA3_minS =
        "\1\160\1\153\1\uffff\1\151\1\uffff\1\160\1\137\1\164\1\151\2\154"+
        "\1\137\1\141\2\uffff";
    static final String DFA3_maxS =
        "\1\163\1\164\1\uffff\1\151\1\uffff\1\160\1\137\1\164\1\151\2\154"+
        "\1\137\1\156\2\uffff";
    static final String DFA3_acceptS =
        "\2\uffff\1\4\1\uffff\1\3\10\uffff\1\1\1\2";
    static final String DFA3_specialS =
        "\17\uffff}>";
    static final String[] DFA3_transitionS = {
            "\1\2\2\uffff\1\1",
            "\1\3\10\uffff\1\4",
            "",
            "\1\5",
            "",
            "\1\6",
            "\1\7",
            "\1\10",
            "\1\11",
            "\1\12",
            "\1\13",
            "\1\14",
            "\1\16\14\uffff\1\15",
            "",
            ""
    };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        public String getDescription() {
            return "24:1: SKIP_METHOD : ( 'skip_till_next_match' | 'skip_till_any_match' | 'strict_contiguity' | 'partition_contiguity' );";
        }
    }
    static final String DFA6_eotS =
        "\1\uffff\2\3\2\uffff\1\3";
    static final String DFA6_eofS =
        "\6\uffff";
    static final String DFA6_minS =
        "\1\60\2\56\2\uffff\1\56";
    static final String DFA6_maxS =
        "\1\71\1\56\1\71\2\uffff\1\71";
    static final String DFA6_acceptS =
        "\3\uffff\1\1\1\2\1\uffff";
    static final String DFA6_specialS =
        "\6\uffff}>";
    static final String[] DFA6_transitionS = {
            "\1\1\11\2",
            "\1\4",
            "\1\4\1\uffff\12\5",
            "",
            "",
            "\1\4\1\uffff\12\5"
    };

    static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
    static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
    static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
    static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
    static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
    static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
    static final short[][] DFA6_transition;

    static {
        int numStates = DFA6_transitionS.length;
        DFA6_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
        }
    }

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = DFA6_eot;
            this.eof = DFA6_eof;
            this.min = DFA6_min;
            this.max = DFA6_max;
            this.accept = DFA6_accept;
            this.special = DFA6_special;
            this.transition = DFA6_transition;
        }
        public String getDescription() {
            return "50:1: NUMBER : ( INTEGER | FLOAT );";
        }
    }
    static final String DFA15_eotS =
        "\1\uffff\4\36\2\uffff\2\36\1\56\1\uffff\7\36\6\uffff\1\73\11\uffff"+
        "\11\36\5\uffff\12\36\2\uffff\4\36\1\126\1\127\1\130\1\127\1\130"+
        "\2\uffff\6\36\1\140\1\36\2\127\5\36\3\uffff\1\140\5\36\1\140\1\uffff"+
        "\4\36\1\161\1\36\1\140\6\36\1\127\1\171\1\36\1\uffff\1\173\1\140"+
        "\1\36\1\140\3\36\1\uffff\1\u0082\1\uffff\1\140\1\36\1\140\3\36\1"+
        "\uffff\14\36\1\140\4\36\1\140\26\36\1\u00af\3\36\1\uffff\2\36\1"+
        "\u00af\1\36\2\u00af";
    static final String DFA15_eofS =
        "\u00b6\uffff";
    static final String DFA15_minS =
        "\1\11\2\101\1\110\1\105\2\uffff\1\116\1\156\1\56\1\uffff\1\157"+
        "\1\151\1\145\3\141\1\157\6\uffff\1\75\11\uffff\1\126\1\124\1\105"+
        "\1\124\1\121\1\155\1\104\1\147\1\144\1\uffff\1\55\3\uffff\1\165"+
        "\1\154\1\143\1\151\1\162\1\171\1\162\1\156\1\170\1\165\2\uffff\1"+
        "\111\1\124\1\122\1\110\5\60\2\uffff\1\162\1\165\1\154\1\157\1\160"+
        "\1\151\1\60\1\164\2\60\1\156\1\116\2\105\1\111\3\uffff\1\60\1\164"+
        "\1\151\1\156\1\137\1\143\1\60\1\uffff\1\151\1\164\1\107\1\122\1"+
        "\60\1\116\1\60\1\145\1\163\1\144\3\164\2\60\1\116\1\uffff\2\60\1"+
        "\145\1\60\1\151\1\137\1\151\1\uffff\1\60\1\uffff\1\60\1\143\1\60"+
        "\1\154\1\143\1\157\1\uffff\1\157\1\154\1\157\2\156\1\137\1\156\1"+
        "\137\1\144\1\141\1\164\1\143\1\60\1\145\1\156\1\151\1\157\1\60\1"+
        "\170\1\171\1\147\1\156\1\164\1\137\1\165\1\164\1\137\1\155\2\151"+
        "\1\155\1\141\1\164\1\147\1\141\1\164\1\171\1\165\1\164\1\143\1\60"+
        "\1\151\1\143\1\150\1\uffff\1\164\1\150\1\60\1\171\2\60";
    static final String DFA15_maxS =
        "\1\176\2\101\1\111\1\165\2\uffff\1\166\1\156\1\172\1\uffff\1\157"+
        "\1\151\1\164\2\141\1\151\1\157\6\uffff\1\75\11\uffff\1\126\1\124"+
        "\1\105\1\124\1\121\1\155\1\104\1\147\1\144\1\uffff\1\172\3\uffff"+
        "\1\165\1\156\1\143\1\151\1\162\1\171\1\162\1\156\1\170\1\165\2\uffff"+
        "\1\111\1\124\1\122\1\110\5\172\2\uffff\1\162\1\165\1\154\1\157\1"+
        "\160\1\151\1\172\1\164\2\172\1\156\1\116\2\105\1\111\3\uffff\1\172"+
        "\1\164\1\151\1\156\1\137\1\143\1\172\1\uffff\1\151\1\164\1\107\1"+
        "\122\1\172\1\116\1\172\1\145\1\163\1\144\3\164\2\172\1\116\1\uffff"+
        "\2\172\1\145\1\172\1\151\1\137\1\151\1\uffff\1\172\1\uffff\1\172"+
        "\1\143\1\172\1\154\1\143\1\157\1\uffff\1\157\1\154\1\157\2\156\1"+
        "\137\1\156\1\137\1\144\1\156\1\164\1\143\1\172\1\145\1\156\1\151"+
        "\1\157\1\172\1\170\1\171\1\147\1\156\1\164\1\137\1\165\1\164\1\137"+
        "\1\155\2\151\1\155\1\141\1\164\1\147\1\141\1\164\1\171\1\165\1\164"+
        "\1\143\1\172\1\151\1\143\1\150\1\uffff\1\164\1\150\1\172\1\171\2"+
        "\172";
    static final String DFA15_acceptS =
        "\5\uffff\1\6\1\7\3\uffff\1\17\7\uffff\1\23\1\24\1\25\1\26\1\27"+
        "\1\30\1\uffff\1\33\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43\11\uffff"+
        "\1\11\1\uffff\1\14\1\16\1\15\12\uffff\1\32\1\31\11\uffff\1\12\1"+
        "\13\17\uffff\1\5\1\22\1\10\7\uffff\1\20\20\uffff\1\3\7\uffff\1\1"+
        "\1\uffff\1\4\6\uffff\1\2\54\uffff\1\21\6\uffff";
    static final String DFA15_specialS =
        "\u00b6\uffff}>";
    static final String[] DFA15_transitionS = {
            "\1\41\1\40\2\uffff\1\40\22\uffff\1\41\1\27\1\37\5\uffff\1\33"+
            "\1\34\1\26\1\22\1\32\1\23\1\24\1\25\12\35\2\uffff\1\27\1\30"+
            "\1\27\2\uffff\1\7\1\36\1\21\4\36\1\1\4\36\1\20\2\36\1\2\2\36"+
            "\1\4\3\36\1\3\3\36\1\11\1\uffff\1\12\3\uffff\1\10\2\36\1\16"+
            "\3\36\1\13\4\36\1\14\2\36\1\17\2\36\1\15\7\36\1\5\1\uffff\1"+
            "\6\1\31",
            "\1\42",
            "\1\43",
            "\1\44\1\45",
            "\1\46\57\uffff\1\47",
            "",
            "",
            "\1\50\47\uffff\1\51",
            "\1\52",
            "\1\55\2\uffff\1\53\17\uffff\32\57\6\uffff\10\57\1\54\21\57",
            "",
            "\1\60",
            "\1\61",
            "\1\62\5\uffff\1\63\10\uffff\1\64",
            "\1\65",
            "\1\66",
            "\1\70\7\uffff\1\67",
            "\1\71",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\72",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\74",
            "\1\75",
            "\1\76",
            "\1\77",
            "\1\100",
            "\1\101",
            "\1\102",
            "\1\103",
            "\1\104",
            "",
            "\1\106\1\57\1\uffff\13\57\6\uffff\32\57\2\uffff\1\105\1\uffff"+
            "\1\57\1\uffff\32\57",
            "",
            "",
            "",
            "\1\107",
            "\1\111\1\uffff\1\110",
            "\1\112",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\121",
            "",
            "",
            "\1\122",
            "\1\123",
            "\1\124",
            "\1\125",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "",
            "",
            "\1\131",
            "\1\132",
            "\1\133",
            "\1\134",
            "\1\135",
            "\1\136",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\22\36\1\137\7\36",
            "\1\141",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\142",
            "\1\143",
            "\1\144",
            "\1\145",
            "\1\146",
            "",
            "",
            "",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\22\36\1\147\7\36",
            "\1\150",
            "\1\151",
            "\1\152",
            "\1\153",
            "\1\154",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "",
            "\1\155",
            "\1\156",
            "\1\157",
            "\1\160",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\162",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\163",
            "\1\164",
            "\1\165",
            "\1\166",
            "\1\167",
            "\1\170",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\172",
            "",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\22\36\1\174\7\36",
            "\1\175",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\22\36\1\176\7\36",
            "\1\177",
            "\1\u0080",
            "\1\u0081",
            "",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\u0083",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
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
            "\1\u008e",
            "\1\u008f",
            "\1\u0091\14\uffff\1\u0090",
            "\1\u0092",
            "\1\u0093",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\22\36\1\u0094\7"+
            "\36",
            "\1\u0095",
            "\1\u0096",
            "\1\u0097",
            "\1\u0098",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\u0099",
            "\1\u009a",
            "\1\u009b",
            "\1\u009c",
            "\1\u009d",
            "\1\u009e",
            "\1\u009f",
            "\1\u00a0",
            "\1\u00a1",
            "\1\u00a2",
            "\1\u00a3",
            "\1\u00a4",
            "\1\u00a5",
            "\1\u00a6",
            "\1\u00a7",
            "\1\u00a8",
            "\1\u00a9",
            "\1\u00aa",
            "\1\u00ab",
            "\1\u00ac",
            "\1\u00ad",
            "\1\u00ae",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\u00b0",
            "\1\u00b1",
            "\1\u00b2",
            "",
            "\1\u00b3",
            "\1\u00b4",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\u00b5",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\13\36\6\uffff\32\36\4\uffff\1\36\1\uffff\32\36"
    };

    static final short[] DFA15_eot = DFA.unpackEncodedString(DFA15_eotS);
    static final short[] DFA15_eof = DFA.unpackEncodedString(DFA15_eofS);
    static final char[] DFA15_min = DFA.unpackEncodedStringToUnsignedChars(DFA15_minS);
    static final char[] DFA15_max = DFA.unpackEncodedStringToUnsignedChars(DFA15_maxS);
    static final short[] DFA15_accept = DFA.unpackEncodedString(DFA15_acceptS);
    static final short[] DFA15_special = DFA.unpackEncodedString(DFA15_specialS);
    static final short[][] DFA15_transition;

    static {
        int numStates = DFA15_transitionS.length;
        DFA15_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA15_transition[i] = DFA.unpackEncodedString(DFA15_transitionS[i]);
        }
    }

    class DFA15 extends DFA {

        public DFA15(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 15;
            this.eot = DFA15_eot;
            this.eof = DFA15_eof;
            this.min = DFA15_min;
            this.max = DFA15_max;
            this.accept = DFA15_accept;
            this.special = DFA15_special;
            this.transition = DFA15_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( HAVING | PATTERN | WHERE | WITHIN | SEQ | LEFTCURLY | RIGHTCURLY | AND | FIRST | CURRENT | PREVIOUS | ALLTOPREVIOUS | LAST | BBRACKETLEFT | BBRACKETRIGHT | TIMEUNIT | SKIP_METHOD | AGGREGATEOP | PLUS | MINUS | POINT | DIVISION | MULT | COMPAREOP | SINGLEEQUALS | EQUALS | NOT | COMMA | LBRACKET | RBRACKET | NUMBER | NAME | STRING_LITERAL | NEWLINE | WHITESPACE );";
        }
    }
 

}