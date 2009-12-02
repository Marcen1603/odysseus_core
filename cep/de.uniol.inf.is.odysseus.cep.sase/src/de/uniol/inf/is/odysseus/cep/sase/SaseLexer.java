// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g 2009-12-02 11:39:30
 package de.uniol.inf.is.odysseus.cep.sase; 

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SaseLexer extends Lexer {
    public static final int WHERE=7;
    public static final int POINT=25;
    public static final int LETTER=37;
    public static final int FLOAT=34;
    public static final int NOT=29;
    public static final int AND=12;
    public static final int EOF=-1;
    public static final int SPACE=40;
    public static final int PATTERN=6;
    public static final int LBRACKET=31;
    public static final int NAME=19;
    public static final int STRING_LITERAL=39;
    public static final int LEFTCURLY=10;
    public static final int SEQ=9;
    public static final int COMMA=30;
    public static final int PREVIOUS=17;
    public static final int RIGHTCURLY=11;
    public static final int PLUS=23;
    public static final int DIGIT=36;
    public static final int LAST=20;
    public static final int RBRACKET=32;
    public static final int BBRACKETRIGHT=14;
    public static final int INTEGER=33;
    public static final int ALLTOPREVIOUS=18;
    public static final int TIMEUNIT=21;
    public static final int SKIP_METHOD=22;
    public static final int NUMBER=35;
    public static final int WHITESPACE=44;
    public static final int HAVING=5;
    public static final int MINUS=24;
    public static final int MULT=26;
    public static final int AGGREGATEOP=28;
    public static final int CURRENT=16;
    public static final int NEWLINE=43;
    public static final int NONCONTROL_CHAR=38;
    public static final int BBRACKETLEFT=13;
    public static final int WITHIN=8;
    public static final int LOWER=41;
    public static final int FROM=4;
    public static final int COMPAREOP=27;
    public static final int UPPER=42;
    public static final int FIRST=15;

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

    // $ANTLR start "FROM"
    public final void mFROM() throws RecognitionException {
        try {
            int _type = FROM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:5:6: ( 'FROM' | 'from' )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='F') ) {
                alt1=1;
            }
            else if ( (LA1_0=='f') ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:5:8: 'FROM'
                    {
                    match("FROM"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:5:17: 'from'
                    {
                    match("from"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FROM"

    // $ANTLR start "HAVING"
    public final void mHAVING() throws RecognitionException {
        try {
            int _type = HAVING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:6:8: ( 'HAVING' | 'having' )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='H') ) {
                alt2=1;
            }
            else if ( (LA2_0=='h') ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:6:10: 'HAVING'
                    {
                    match("HAVING"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:6:21: 'having'
                    {
                    match("having"); 


                    }
                    break;

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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:7:9: ( 'PATTERN' | 'pattern' )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='P') ) {
                alt3=1;
            }
            else if ( (LA3_0=='p') ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:7:11: 'PATTERN'
                    {
                    match("PATTERN"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:7:23: 'pattern'
                    {
                    match("pattern"); 


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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:8:7: ( 'WHERE' | 'where' )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='W') ) {
                alt4=1;
            }
            else if ( (LA4_0=='w') ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:8:9: 'WHERE'
                    {
                    match("WHERE"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:8:19: 'where'
                    {
                    match("where"); 


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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:9:8: ( 'WITHIN' | 'within' )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='W') ) {
                alt5=1;
            }
            else if ( (LA5_0=='w') ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:9:10: 'WITHIN'
                    {
                    match("WITHIN"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:9:21: 'within'
                    {
                    match("within"); 


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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:10:5: ( 'SEQ' | 'seq' )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='S') ) {
                alt6=1;
            }
            else if ( (LA6_0=='s') ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:10:7: 'SEQ'
                    {
                    match("SEQ"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:10:15: 'seq'
                    {
                    match("seq"); 


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

    // $ANTLR start "LEFTCURLY"
    public final void mLEFTCURLY() throws RecognitionException {
        try {
            int _type = LEFTCURLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:11:11: ( '{' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:11:13: '{'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:12:12: ( '}' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:12:14: '}'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:13:5: ( 'AND' | 'and' )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='A') ) {
                alt7=1;
            }
            else if ( (LA7_0=='a') ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:13:6: 'AND'
                    {
                    match("AND"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:13:12: 'and'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:14:7: ( BBRACKETLEFT '1' BBRACKETRIGHT )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:14:9: BBRACKETLEFT '1' BBRACKETRIGHT
            {
            mBBRACKETLEFT(); 
            match('1'); 
            mBBRACKETRIGHT(); 

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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:15:9: ( BBRACKETLEFT 'i' BBRACKETRIGHT )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:15:11: BBRACKETLEFT 'i' BBRACKETRIGHT
            {
            mBBRACKETLEFT(); 
            match('i'); 
            mBBRACKETRIGHT(); 

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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:16:10: ( BBRACKETLEFT 'i-1' BBRACKETRIGHT )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:16:12: BBRACKETLEFT 'i-1' BBRACKETRIGHT
            {
            mBBRACKETLEFT(); 
            match("i-1"); 

            mBBRACKETRIGHT(); 

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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:17:15: ( BBRACKETLEFT '..i-1' BBRACKETRIGHT )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:17:17: BBRACKETLEFT '..i-1' BBRACKETRIGHT
            {
            mBBRACKETLEFT(); 
            match("..i-1"); 

            mBBRACKETRIGHT(); 

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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:18:6: ( BBRACKETLEFT NAME '.LEN' BBRACKETRIGHT )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:18:8: BBRACKETLEFT NAME '.LEN' BBRACKETRIGHT
            {
            mBBRACKETLEFT(); 
            mNAME(); 
            match(".LEN"); 

            mBBRACKETRIGHT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LAST"

    // $ANTLR start "TIMEUNIT"
    public final void mTIMEUNIT() throws RecognitionException {
        try {
            int _type = TIMEUNIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:20:9: ( 'hours' | 'minutes' | 'seconds' | 'days' | 'milliseconds' )
            int alt8=5;
            switch ( input.LA(1) ) {
            case 'h':
                {
                alt8=1;
                }
                break;
            case 'm':
                {
                int LA8_2 = input.LA(2);

                if ( (LA8_2=='i') ) {
                    int LA8_5 = input.LA(3);

                    if ( (LA8_5=='n') ) {
                        alt8=2;
                    }
                    else if ( (LA8_5=='l') ) {
                        alt8=5;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 5, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 2, input);

                    throw nvae;
                }
                }
                break;
            case 's':
                {
                alt8=3;
                }
                break;
            case 'd':
                {
                alt8=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:20:11: 'hours'
                    {
                    match("hours"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:20:21: 'minutes'
                    {
                    match("minutes"); 


                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:20:33: 'seconds'
                    {
                    match("seconds"); 


                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:20:45: 'days'
                    {
                    match("days"); 


                    }
                    break;
                case 5 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:20:54: 'milliseconds'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:23:2: ( 'skip_till_next_match' | 'skip_till_any_match' )
            int alt9=2;
            alt9 = dfa9.predict(input);
            switch (alt9) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:23:4: 'skip_till_next_match'
                    {
                    match("skip_till_next_match"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:23:27: 'skip_till_any_match'
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

    // $ANTLR start "BBRACKETLEFT"
    public final void mBBRACKETLEFT() throws RecognitionException {
        try {
            int _type = BBRACKETLEFT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:25:14: ( '[' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:25:17: '['
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:26:15: ( ']' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:26:18: ']'
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

    // $ANTLR start "PLUS"
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:28:6: ( '+' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:28:8: '+'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:29:7: ( '-' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:29:9: '-'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:30:7: ( '.' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:30:9: '.'
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

    // $ANTLR start "MULT"
    public final void mMULT() throws RecognitionException {
        try {
            int _type = MULT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:31:6: ( '*' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:31:8: '*'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:11: ( '=' | '<=' | '>=' | '!=' | '<' | '>' )
            int alt10=6;
            switch ( input.LA(1) ) {
            case '=':
                {
                alt10=1;
                }
                break;
            case '<':
                {
                int LA10_2 = input.LA(2);

                if ( (LA10_2=='=') ) {
                    alt10=2;
                }
                else {
                    alt10=5;}
                }
                break;
            case '>':
                {
                int LA10_3 = input.LA(2);

                if ( (LA10_3=='=') ) {
                    alt10=3;
                }
                else {
                    alt10=6;}
                }
                break;
            case '!':
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
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:13: '='
                    {
                    match('='); 

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:17: '<='
                    {
                    match("<="); 


                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:22: '>='
                    {
                    match(">="); 


                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:27: '!='
                    {
                    match("!="); 


                    }
                    break;
                case 5 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:32: '<'
                    {
                    match('<'); 

                    }
                    break;
                case 6 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:36: '>'
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

    // $ANTLR start "AGGREGATEOP"
    public final void mAGGREGATEOP() throws RecognitionException {
        try {
            int _type = AGGREGATEOP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:33:13: ( 'AVG' | 'MIN' | 'MAX' | 'SUM' | 'COUNT' )
            int alt11=5;
            switch ( input.LA(1) ) {
            case 'A':
                {
                alt11=1;
                }
                break;
            case 'M':
                {
                int LA11_2 = input.LA(2);

                if ( (LA11_2=='I') ) {
                    alt11=2;
                }
                else if ( (LA11_2=='A') ) {
                    alt11=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 11, 2, input);

                    throw nvae;
                }
                }
                break;
            case 'S':
                {
                alt11=4;
                }
                break;
            case 'C':
                {
                alt11=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }

            switch (alt11) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:33:15: 'AVG'
                    {
                    match("AVG"); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:33:21: 'MIN'
                    {
                    match("MIN"); 


                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:33:27: 'MAX'
                    {
                    match("MAX"); 


                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:33:33: 'SUM'
                    {
                    match("SUM"); 


                    }
                    break;
                case 5 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:33:39: 'COUNT'
                    {
                    match("COUNT"); 


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

    // $ANTLR start "NOT"
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:35:5: ( '~' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:35:7: '~'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:37:7: ( ',' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:37:9: ','
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:40:2: ( '(' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:40:4: '('
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:42:2: ( ')' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:42:4: ')'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:45:8: ( INTEGER | FLOAT )
            int alt12=2;
            alt12 = dfa12.predict(input);
            switch (alt12) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:45:11: INTEGER
                    {
                    mINTEGER(); 

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:45:21: FLOAT
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:46:16: ( INTEGER '.' ( DIGIT )+ )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:46:18: INTEGER '.' ( DIGIT )+
            {
            mINTEGER(); 
            match('.'); 
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:46:30: ( DIGIT )+
            int cnt13=0;
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>='0' && LA13_0<='9')) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:46:30: DIGIT
            	    {
            	    mDIGIT(); 

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

        }
        finally {
        }
    }
    // $ANTLR end "FLOAT"

    // $ANTLR start "INTEGER"
    public final void mINTEGER() throws RecognitionException {
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:18: ( '0' | '1' .. '9' ( '0' .. '9' )* )
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0=='0') ) {
                alt15=1;
            }
            else if ( ((LA15_0>='1' && LA15_0<='9')) ) {
                alt15=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }
            switch (alt15) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:20: '0'
                    {
                    match('0'); 

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:26: '1' .. '9' ( '0' .. '9' )*
                    {
                    matchRange('1','9'); 
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:35: ( '0' .. '9' )*
                    loop14:
                    do {
                        int alt14=2;
                        int LA14_0 = input.LA(1);

                        if ( ((LA14_0>='0' && LA14_0<='9')) ) {
                            alt14=1;
                        }


                        switch (alt14) {
                    	case 1 :
                    	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:36: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop14;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:49:6: ( LETTER ( LETTER | DIGIT | '_' | ':' )* )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:49:8: LETTER ( LETTER | DIGIT | '_' | ':' )*
            {
            mLETTER(); 
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:49:15: ( LETTER | DIGIT | '_' | ':' )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( ((LA16_0>='0' && LA16_0<=':')||(LA16_0>='A' && LA16_0<='Z')||LA16_0=='_'||(LA16_0>='a' && LA16_0<='z')) ) {
                    alt16=1;
                }


                switch (alt16) {
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
            	    break loop16;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:50:16: ( '\"' ( NONCONTROL_CHAR )* '\"' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:50:18: '\"' ( NONCONTROL_CHAR )* '\"'
            {
            match('\"'); 
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:50:22: ( NONCONTROL_CHAR )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0=='\t'||LA17_0==' '||(LA17_0>='0' && LA17_0<='9')||(LA17_0>='A' && LA17_0<='Z')||(LA17_0>='a' && LA17_0<='z')) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:50:22: NONCONTROL_CHAR
            	    {
            	    mNONCONTROL_CHAR(); 

            	    }
            	    break;

            	default :
            	    break loop17;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:52:26: ( LETTER | DIGIT | SPACE )
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:53:17: ( LOWER | UPPER )
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:54:17: ( 'a' .. 'z' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:54:19: 'a' .. 'z'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:55:17: ( 'A' .. 'Z' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:55:19: 'A' .. 'Z'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:56:17: ( '0' .. '9' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:56:19: '0' .. '9'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:57:17: ( ' ' | '\\t' )
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:59:9: ( ( ( '\\r' )? '\\n' )+ )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:59:11: ( ( '\\r' )? '\\n' )+
            {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:59:11: ( ( '\\r' )? '\\n' )+
            int cnt19=0;
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0=='\n'||LA19_0=='\r') ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:59:12: ( '\\r' )? '\\n'
            	    {
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:59:12: ( '\\r' )?
            	    int alt18=2;
            	    int LA18_0 = input.LA(1);

            	    if ( (LA18_0=='\r') ) {
            	        alt18=1;
            	    }
            	    switch (alt18) {
            	        case 1 :
            	            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:59:12: '\\r'
            	            {
            	            match('\r'); 

            	            }
            	            break;

            	    }

            	    match('\n'); 

            	    }
            	    break;

            	default :
            	    if ( cnt19 >= 1 ) break loop19;
                        EarlyExitException eee =
                            new EarlyExitException(19, input);
                        throw eee;
                }
                cnt19++;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:61:12: ( ( SPACE )+ )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:61:14: ( SPACE )+
            {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:61:14: ( SPACE )+
            int cnt20=0;
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( (LA20_0=='\t'||LA20_0==' ') ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:61:14: SPACE
            	    {
            	    mSPACE(); 

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
        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:8: ( FROM | HAVING | PATTERN | WHERE | WITHIN | SEQ | LEFTCURLY | RIGHTCURLY | AND | FIRST | CURRENT | PREVIOUS | ALLTOPREVIOUS | LAST | TIMEUNIT | SKIP_METHOD | BBRACKETLEFT | BBRACKETRIGHT | PLUS | MINUS | POINT | MULT | COMPAREOP | AGGREGATEOP | NOT | COMMA | LBRACKET | RBRACKET | NUMBER | NAME | STRING_LITERAL | NEWLINE | WHITESPACE )
        int alt21=33;
        alt21 = dfa21.predict(input);
        switch (alt21) {
            case 1 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:10: FROM
                {
                mFROM(); 

                }
                break;
            case 2 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:15: HAVING
                {
                mHAVING(); 

                }
                break;
            case 3 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:22: PATTERN
                {
                mPATTERN(); 

                }
                break;
            case 4 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:30: WHERE
                {
                mWHERE(); 

                }
                break;
            case 5 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:36: WITHIN
                {
                mWITHIN(); 

                }
                break;
            case 6 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:43: SEQ
                {
                mSEQ(); 

                }
                break;
            case 7 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:47: LEFTCURLY
                {
                mLEFTCURLY(); 

                }
                break;
            case 8 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:57: RIGHTCURLY
                {
                mRIGHTCURLY(); 

                }
                break;
            case 9 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:68: AND
                {
                mAND(); 

                }
                break;
            case 10 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:72: FIRST
                {
                mFIRST(); 

                }
                break;
            case 11 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:78: CURRENT
                {
                mCURRENT(); 

                }
                break;
            case 12 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:86: PREVIOUS
                {
                mPREVIOUS(); 

                }
                break;
            case 13 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:95: ALLTOPREVIOUS
                {
                mALLTOPREVIOUS(); 

                }
                break;
            case 14 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:109: LAST
                {
                mLAST(); 

                }
                break;
            case 15 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:114: TIMEUNIT
                {
                mTIMEUNIT(); 

                }
                break;
            case 16 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:123: SKIP_METHOD
                {
                mSKIP_METHOD(); 

                }
                break;
            case 17 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:135: BBRACKETLEFT
                {
                mBBRACKETLEFT(); 

                }
                break;
            case 18 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:148: BBRACKETRIGHT
                {
                mBBRACKETRIGHT(); 

                }
                break;
            case 19 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:162: PLUS
                {
                mPLUS(); 

                }
                break;
            case 20 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:167: MINUS
                {
                mMINUS(); 

                }
                break;
            case 21 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:173: POINT
                {
                mPOINT(); 

                }
                break;
            case 22 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:179: MULT
                {
                mMULT(); 

                }
                break;
            case 23 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:184: COMPAREOP
                {
                mCOMPAREOP(); 

                }
                break;
            case 24 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:194: AGGREGATEOP
                {
                mAGGREGATEOP(); 

                }
                break;
            case 25 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:206: NOT
                {
                mNOT(); 

                }
                break;
            case 26 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:210: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 27 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:216: LBRACKET
                {
                mLBRACKET(); 

                }
                break;
            case 28 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:225: RBRACKET
                {
                mRBRACKET(); 

                }
                break;
            case 29 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:234: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 30 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:241: NAME
                {
                mNAME(); 

                }
                break;
            case 31 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:246: STRING_LITERAL
                {
                mSTRING_LITERAL(); 

                }
                break;
            case 32 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:261: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 33 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:269: WHITESPACE
                {
                mWHITESPACE(); 

                }
                break;

        }

    }


    protected DFA9 dfa9 = new DFA9(this);
    protected DFA12 dfa12 = new DFA12(this);
    protected DFA21 dfa21 = new DFA21(this);
    static final String DFA9_eotS =
        "\15\uffff";
    static final String DFA9_eofS =
        "\15\uffff";
    static final String DFA9_minS =
        "\1\163\1\153\1\151\1\160\1\137\1\164\1\151\2\154\1\137\1\141\2"+
        "\uffff";
    static final String DFA9_maxS =
        "\1\163\1\153\1\151\1\160\1\137\1\164\1\151\2\154\1\137\1\156\2"+
        "\uffff";
    static final String DFA9_acceptS =
        "\13\uffff\1\1\1\2";
    static final String DFA9_specialS =
        "\15\uffff}>";
    static final String[] DFA9_transitionS = {
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

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "22:1: SKIP_METHOD : ( 'skip_till_next_match' | 'skip_till_any_match' );";
        }
    }
    static final String DFA12_eotS =
        "\1\uffff\2\3\2\uffff\1\3";
    static final String DFA12_eofS =
        "\6\uffff";
    static final String DFA12_minS =
        "\1\60\2\56\2\uffff\1\56";
    static final String DFA12_maxS =
        "\1\71\1\56\1\71\2\uffff\1\71";
    static final String DFA12_acceptS =
        "\3\uffff\1\1\1\2\1\uffff";
    static final String DFA12_specialS =
        "\6\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\1\11\2",
            "\1\4",
            "\1\4\1\uffff\12\5",
            "",
            "",
            "\1\4\1\uffff\12\5"
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "45:1: NUMBER : ( INTEGER | FLOAT );";
        }
    }
    static final String DFA21_eotS =
        "\1\uffff\12\37\2\uffff\2\37\1\65\2\37\6\uffff\2\37\11\uffff\22"+
        "\37\5\uffff\20\37\1\145\1\146\1\145\2\37\1\151\1\146\1\151\2\uffff"+
        "\3\37\2\146\1\37\2\156\11\37\2\uffff\2\37\1\uffff\2\37\1\174\1\37"+
        "\1\uffff\2\37\1\174\2\37\1\u0082\1\37\1\u0082\5\37\1\uffff\1\146"+
        "\2\u0089\2\37\1\uffff\2\u008c\4\37\1\uffff\2\u0091\1\uffff\1\174"+
        "\1\37\1\174\1\37\1\uffff\13\37\1\174\15\37\2\u00ad\1\uffff";
    static final String DFA21_eofS =
        "\u00ae\uffff";
    static final String DFA21_minS =
        "\1\11\1\122\1\162\1\101\1\141\1\101\1\141\1\110\1\150\1\105\1\145"+
        "\2\uffff\1\116\1\156\1\56\1\151\1\141\6\uffff\1\101\1\117\11\uffff"+
        "\1\117\1\157\1\126\1\166\1\165\1\124\1\164\1\105\1\124\1\145\1\164"+
        "\1\121\1\115\1\143\1\151\1\104\1\107\1\144\3\uffff\1\55\1\uffff"+
        "\1\154\1\171\1\116\1\130\1\125\1\115\1\155\1\111\1\151\1\162\1\124"+
        "\1\164\1\122\1\110\1\162\1\150\3\60\1\157\1\160\3\60\2\uffff\1\165"+
        "\1\154\1\163\2\60\1\116\2\60\1\116\1\156\1\163\1\105\1\145\1\105"+
        "\1\111\1\145\1\151\2\uffff\1\156\1\137\1\uffff\1\164\1\151\1\60"+
        "\1\124\1\uffff\1\107\1\147\1\60\1\122\1\162\1\60\1\116\1\60\1\156"+
        "\1\144\1\164\1\145\1\163\1\uffff\3\60\1\116\1\156\1\uffff\2\60\1"+
        "\163\1\151\1\163\1\145\1\uffff\2\60\1\uffff\1\60\1\154\1\60\1\143"+
        "\1\uffff\1\154\1\157\1\137\1\156\1\141\1\144\1\145\1\156\1\163\1"+
        "\170\1\171\1\60\1\164\2\137\2\155\2\141\2\164\2\143\2\150\2\60\1"+
        "\uffff";
    static final String DFA21_maxS =
        "\1\176\1\122\1\162\1\101\1\157\1\101\1\141\1\111\1\151\1\125\1"+
        "\153\2\uffff\1\126\1\156\1\172\1\151\1\141\6\uffff\1\111\1\117\11"+
        "\uffff\1\117\1\157\1\126\1\166\1\165\1\124\1\164\1\105\1\124\1\145"+
        "\1\164\1\121\1\115\1\161\1\151\1\104\1\107\1\144\3\uffff\1\172\1"+
        "\uffff\1\156\1\171\1\116\1\130\1\125\1\115\1\155\1\111\1\151\1\162"+
        "\1\124\1\164\1\122\1\110\1\162\1\150\3\172\1\157\1\160\3\172\2\uffff"+
        "\1\165\1\154\1\163\2\172\1\116\2\172\1\116\1\156\1\163\1\105\1\145"+
        "\1\105\1\111\1\145\1\151\2\uffff\1\156\1\137\1\uffff\1\164\1\151"+
        "\1\172\1\124\1\uffff\1\107\1\147\1\172\1\122\1\162\1\172\1\116\1"+
        "\172\1\156\1\144\1\164\1\145\1\163\1\uffff\3\172\1\116\1\156\1\uffff"+
        "\2\172\1\163\1\151\1\163\1\145\1\uffff\2\172\1\uffff\1\172\1\154"+
        "\1\172\1\143\1\uffff\1\154\1\157\1\137\2\156\1\144\1\145\1\156\1"+
        "\163\1\170\1\171\1\172\1\164\2\137\2\155\2\141\2\164\2\143\2\150"+
        "\2\172\1\uffff";
    static final String DFA21_acceptS =
        "\13\uffff\1\7\1\10\5\uffff\1\22\1\23\1\24\1\25\1\26\1\27\2\uffff"+
        "\1\31\1\32\1\33\1\34\1\35\1\36\1\37\1\40\1\41\22\uffff\1\21\1\12"+
        "\1\15\1\uffff\1\16\30\uffff\1\14\1\13\21\uffff\1\6\1\30\2\uffff"+
        "\1\11\4\uffff\1\1\15\uffff\1\17\5\uffff\1\4\6\uffff\1\2\2\uffff"+
        "\1\5\4\uffff\1\3\33\uffff\1\20";
    static final String DFA21_specialS =
        "\u00ae\uffff}>";
    static final String[] DFA21_transitionS = {
            "\1\42\1\41\2\uffff\1\41\22\uffff\1\42\1\27\1\40\5\uffff\1\34"+
            "\1\35\1\26\1\23\1\33\1\24\1\25\1\uffff\12\36\2\uffff\3\27\2"+
            "\uffff\1\15\1\37\1\31\2\37\1\1\1\37\1\3\4\37\1\30\2\37\1\5\2"+
            "\37\1\11\3\37\1\7\3\37\1\17\1\uffff\1\22\3\uffff\1\16\2\37\1"+
            "\21\1\37\1\2\1\37\1\4\4\37\1\20\2\37\1\6\2\37\1\12\3\37\1\10"+
            "\3\37\1\13\1\uffff\1\14\1\32",
            "\1\43",
            "\1\44",
            "\1\45",
            "\1\46\15\uffff\1\47",
            "\1\50",
            "\1\51",
            "\1\52\1\53",
            "\1\54\1\55",
            "\1\56\17\uffff\1\57",
            "\1\60\5\uffff\1\61",
            "",
            "",
            "\1\62\7\uffff\1\63",
            "\1\64",
            "\1\67\2\uffff\1\66\17\uffff\32\71\6\uffff\10\71\1\70\21\71",
            "\1\72",
            "\1\73",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\75\7\uffff\1\74",
            "\1\76",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
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
            "\1\112",
            "\1\113",
            "\1\115\15\uffff\1\114",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\121",
            "",
            "",
            "",
            "\1\122\1\71\1\uffff\13\71\6\uffff\32\71\2\uffff\1\123\1\uffff"+
            "\1\71\1\uffff\32\71",
            "",
            "\1\125\1\uffff\1\124",
            "\1\126",
            "\1\127",
            "\1\130",
            "\1\131",
            "\1\132",
            "\1\133",
            "\1\134",
            "\1\135",
            "\1\136",
            "\1\137",
            "\1\140",
            "\1\141",
            "\1\142",
            "\1\143",
            "\1\144",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\1\147",
            "\1\150",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "",
            "",
            "\1\152",
            "\1\153",
            "\1\154",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\1\155",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\1\157",
            "\1\160",
            "\1\161",
            "\1\162",
            "\1\163",
            "\1\164",
            "\1\165",
            "\1\166",
            "\1\167",
            "",
            "",
            "\1\170",
            "\1\171",
            "",
            "\1\172",
            "\1\173",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\1\175",
            "",
            "\1\176",
            "\1\177",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\1\u0080",
            "\1\u0081",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\1\u0083",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\1\u0084",
            "\1\u0085",
            "\1\u0086",
            "\1\u0087",
            "\1\u0088",
            "",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\1\u008a",
            "\1\u008b",
            "",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\1\u008d",
            "\1\u008e",
            "\1\u008f",
            "\1\u0090",
            "",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\1\u0092",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\1\u0093",
            "",
            "\1\u0094",
            "\1\u0095",
            "\1\u0096",
            "\1\u0097",
            "\1\u0099\14\uffff\1\u0098",
            "\1\u009a",
            "\1\u009b",
            "\1\u009c",
            "\1\u009d",
            "\1\u009e",
            "\1\u009f",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
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
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\13\37\6\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            ""
    };

    static final short[] DFA21_eot = DFA.unpackEncodedString(DFA21_eotS);
    static final short[] DFA21_eof = DFA.unpackEncodedString(DFA21_eofS);
    static final char[] DFA21_min = DFA.unpackEncodedStringToUnsignedChars(DFA21_minS);
    static final char[] DFA21_max = DFA.unpackEncodedStringToUnsignedChars(DFA21_maxS);
    static final short[] DFA21_accept = DFA.unpackEncodedString(DFA21_acceptS);
    static final short[] DFA21_special = DFA.unpackEncodedString(DFA21_specialS);
    static final short[][] DFA21_transition;

    static {
        int numStates = DFA21_transitionS.length;
        DFA21_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA21_transition[i] = DFA.unpackEncodedString(DFA21_transitionS[i]);
        }
    }

    class DFA21 extends DFA {

        public DFA21(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 21;
            this.eot = DFA21_eot;
            this.eof = DFA21_eof;
            this.min = DFA21_min;
            this.max = DFA21_max;
            this.accept = DFA21_accept;
            this.special = DFA21_special;
            this.transition = DFA21_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( FROM | HAVING | PATTERN | WHERE | WITHIN | SEQ | LEFTCURLY | RIGHTCURLY | AND | FIRST | CURRENT | PREVIOUS | ALLTOPREVIOUS | LAST | TIMEUNIT | SKIP_METHOD | BBRACKETLEFT | BBRACKETRIGHT | PLUS | MINUS | POINT | MULT | COMPAREOP | AGGREGATEOP | NOT | COMMA | LBRACKET | RBRACKET | NUMBER | NAME | STRING_LITERAL | NEWLINE | WHITESPACE );";
        }
    }
 

}