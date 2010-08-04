// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g 2010-08-04 16:12:40
 package de.uniol.inf.is.odysseus.cep.sase;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SaseLexer extends Lexer {
    public static final int WEEK=18;
    public static final int WHERE=8;
    public static final int MILLISECOND=23;
    public static final int POINT=35;
    public static final int LETTER=49;
    public static final int MAX=30;
    public static final int FLOAT=46;
    public static final int DAY=19;
    public static final int COUNT=32;
    public static final int EQUALS=40;
    public static final int AND=14;
    public static final int SUM=31;
    public static final int SPACE=53;
    public static final int EOF=-1;
    public static final int PATTERN=7;
    public static final int LBRACKET=43;
    public static final int SINGLEEQUALS=39;
    public static final int NAME=50;
    public static final int CREATE=4;
    public static final int STRING_LITERAL=52;
    public static final int NOTSIGN=41;
    public static final int LEFTCURLY=12;
    public static final int SEQ=11;
    public static final int COMMA=42;
    public static final int AVG=28;
    public static final int RETURN=10;
    public static final int RIGHTCURLY=13;
    public static final int DIVISION=36;
    public static final int PLUS=33;
    public static final int DIGIT=48;
    public static final int RBRACKET=44;
    public static final int STREAM=5;
    public static final int BBRACKETRIGHT=17;
    public static final int INTEGER=45;
    public static final int SECOND=22;
    public static final int VIEW=6;
    public static final int NUMBER=47;
    public static final int WHITESPACE=57;
    public static final int MIN=29;
    public static final int MINUS=34;
    public static final int MULT=37;
    public static final int HOUR=20;
    public static final int SKIP_TILL_NEXT_MATCH=24;
    public static final int NEWLINE=56;
    public static final int NONCONTROL_CHAR=51;
    public static final int BBRACKETLEFT=16;
    public static final int PARTITION_CONTIGUITY=27;
    public static final int WITHIN=9;
    public static final int SKIP_TILL_ANY_MATCH=25;
    public static final int LEN=15;
    public static final int LOWER=54;
    public static final int MINUTE=21;
    public static final int STRICT_CONTIGUITY=26;
    public static final int COMPAREOP=38;
    public static final int UPPER=55;

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

    // $ANTLR start "CREATE"
    public final void mCREATE() throws RecognitionException {
        try {
            int _type = CREATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:5:9: ( ( 'C' | 'c' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'E' | 'e' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:5:11: ( 'C' | 'c' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'E' | 'e' )
            {
            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CREATE"

    // $ANTLR start "STREAM"
    public final void mSTREAM() throws RecognitionException {
        try {
            int _type = STREAM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:6:9: ( ( 'S' | 's' ) ( 'T' | 't' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'M' | 'm' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:6:11: ( 'S' | 's' ) ( 'T' | 't' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'M' | 'm' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STREAM"

    // $ANTLR start "VIEW"
    public final void mVIEW() throws RecognitionException {
        try {
            int _type = VIEW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:7:5: ( ( 'V' | 'v' ) ( 'I' | 'i' ) ( 'E' | 'e' ) ( 'W' | 'w' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:7:7: ( 'V' | 'v' ) ( 'I' | 'i' ) ( 'E' | 'e' ) ( 'W' | 'w' )
            {
            if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VIEW"

    // $ANTLR start "PATTERN"
    public final void mPATTERN() throws RecognitionException {
        try {
            int _type = PATTERN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:8:9: ( ( 'P' | 'p' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'N' | 'n' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:8:11: ( 'P' | 'p' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:9:7: ( ( 'W' | 'w' ) ( 'H' | 'h' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'E' | 'e' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:9:9: ( 'W' | 'w' ) ( 'H' | 'h' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'E' | 'e' )
            {
            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:10:8: ( ( 'W' | 'w' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'H' | 'h' ) ( 'I' | 'i' ) ( 'N' | 'n' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:10:9: ( 'W' | 'w' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'H' | 'h' ) ( 'I' | 'i' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WITHIN"

    // $ANTLR start "RETURN"
    public final void mRETURN() throws RecognitionException {
        try {
            int _type = RETURN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:11:9: ( ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'T' | 't' ) ( 'U' | 'u' ) ( 'R' | 'r' ) ( 'N' | 'n' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:11:11: ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'T' | 't' ) ( 'U' | 'u' ) ( 'R' | 'r' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RETURN"

    // $ANTLR start "SEQ"
    public final void mSEQ() throws RecognitionException {
        try {
            int _type = SEQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:12:5: ( ( 'S' | 's' ) ( 'E' | 'e' ) ( 'Q' | 'q' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:12:7: ( 'S' | 's' ) ( 'E' | 'e' ) ( 'Q' | 'q' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='Q'||input.LA(1)=='q' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:13:11: ( '{' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:13:13: '{'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:14:12: ( '}' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:14:14: '}'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:15:5: ( ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'D' | 'd' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:15:7: ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'D' | 'd' )
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AND"

    // $ANTLR start "LEN"
    public final void mLEN() throws RecognitionException {
        try {
            int _type = LEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:16:6: ( ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'N' | 'n' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:16:8: ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LEN"

    // $ANTLR start "BBRACKETLEFT"
    public final void mBBRACKETLEFT() throws RecognitionException {
        try {
            int _type = BBRACKETLEFT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:18:14: ( '[' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:18:17: '['
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:19:15: ( ']' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:19:18: ']'
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

    // $ANTLR start "WEEK"
    public final void mWEEK() throws RecognitionException {
        try {
            int _type = WEEK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:21:5: ( ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'K' | 'k' ) ( 'S' | 's' )? )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:21:7: ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'K' | 'k' ) ( 'S' | 's' )?
            {
            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:21:43: ( 'S' | 's' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='S'||LA1_0=='s') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
                    {
                    if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


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
    // $ANTLR end "WEEK"

    // $ANTLR start "DAY"
    public final void mDAY() throws RecognitionException {
        try {
            int _type = DAY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:22:4: ( ( 'D' | 'd' ) ( 'A' | 'a' ) ( 'Y' | 'y' ) ( 'S' | 's' )? )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:22:6: ( 'D' | 'd' ) ( 'A' | 'a' ) ( 'Y' | 'y' ) ( 'S' | 's' )?
            {
            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:22:33: ( 'S' | 's' )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='S'||LA2_0=='s') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
                    {
                    if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


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
    // $ANTLR end "DAY"

    // $ANTLR start "HOUR"
    public final void mHOUR() throws RecognitionException {
        try {
            int _type = HOUR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:23:5: ( ( 'H' | 'h' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'R' | 'r' ) ( 'S' | 's' )? )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:23:7: ( 'H' | 'h' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'R' | 'r' ) ( 'S' | 's' )?
            {
            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:23:43: ( 'S' | 's' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='S'||LA3_0=='s') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
                    {
                    if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


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
    // $ANTLR end "HOUR"

    // $ANTLR start "MINUTE"
    public final void mMINUTE() throws RecognitionException {
        try {
            int _type = MINUTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:24:7: ( ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'U' | 'u' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'S' | 's' )? )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:24:9: ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'U' | 'u' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'S' | 's' )?
            {
            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:24:63: ( 'S' | 's' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='S'||LA4_0=='s') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
                    {
                    if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


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
    // $ANTLR end "MINUTE"

    // $ANTLR start "SECOND"
    public final void mSECOND() throws RecognitionException {
        try {
            int _type = SECOND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:25:7: ( ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'S' | 's' )? )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:25:9: ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'S' | 's' )?
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:25:63: ( 'S' | 's' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='S'||LA5_0=='s') ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
                    {
                    if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


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
    // $ANTLR end "SECOND"

    // $ANTLR start "MILLISECOND"
    public final void mMILLISECOND() throws RecognitionException {
        try {
            int _type = MILLISECOND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:26:12: ( ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'L' | 'l' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'S' | 's' )? )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:26:13: ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'L' | 'l' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'S' | 's' )?
            {
            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:26:112: ( 'S' | 's' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='S'||LA6_0=='s') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
                    {
                    if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


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
    // $ANTLR end "MILLISECOND"

    // $ANTLR start "SKIP_TILL_NEXT_MATCH"
    public final void mSKIP_TILL_NEXT_MATCH() throws RecognitionException {
        try {
            int _type = SKIP_TILL_NEXT_MATCH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:29:21: ( ( 'S' | 's' ) ( 'K' | 'k' ) ( 'I' | 'i' ) ( 'P' | 'p' ) ( '_' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'L' | 'l' ) ( '_' ) ( 'N' | 'n' ) ( 'E' | 'e' ) ( 'X' | 'x' ) ( 'T' | 't' ) ( '_' ) ( 'M' | 'm' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'C' | 'c' ) ( 'H' | 'h' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:29:23: ( 'S' | 's' ) ( 'K' | 'k' ) ( 'I' | 'i' ) ( 'P' | 'p' ) ( '_' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'L' | 'l' ) ( '_' ) ( 'N' | 'n' ) ( 'E' | 'e' ) ( 'X' | 'x' ) ( 'T' | 't' ) ( '_' ) ( 'M' | 'm' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'C' | 'c' ) ( 'H' | 'h' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:29:59: ( '_' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:29:60: '_'
            {
            match('_'); 

            }

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:29:100: ( '_' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:29:101: '_'
            {
            match('_'); 

            }

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:29:141: ( '_' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:29:142: '_'
            {
            match('_'); 

            }

            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SKIP_TILL_NEXT_MATCH"

    // $ANTLR start "SKIP_TILL_ANY_MATCH"
    public final void mSKIP_TILL_ANY_MATCH() throws RecognitionException {
        try {
            int _type = SKIP_TILL_ANY_MATCH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:30:20: ( ( 'S' | 's' ) ( 'K' | 'k' ) ( 'I' | 'i' ) ( 'P' | 'p' ) ( '_' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'L' | 'l' ) ( '_' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'Y' | 'y' ) ( '_' ) ( 'M' | 'm' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'C' | 'c' ) ( 'H' | 'h' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:30:24: ( 'S' | 's' ) ( 'K' | 'k' ) ( 'I' | 'i' ) ( 'P' | 'p' ) ( '_' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'L' | 'l' ) ( '_' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'Y' | 'y' ) ( '_' ) ( 'M' | 'm' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'C' | 'c' ) ( 'H' | 'h' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:30:60: ( '_' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:30:61: '_'
            {
            match('_'); 

            }

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:30:101: ( '_' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:30:102: '_'
            {
            match('_'); 

            }

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:30:133: ( '_' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:30:134: '_'
            {
            match('_'); 

            }

            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SKIP_TILL_ANY_MATCH"

    // $ANTLR start "STRICT_CONTIGUITY"
    public final void mSTRICT_CONTIGUITY() throws RecognitionException {
        try {
            int _type = STRICT_CONTIGUITY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:31:18: ( ( 'S' | 's' ) ( 'T' | 't' ) ( 'R' | 'r' ) ( 'I' | 'i' ) ( 'C' | 'c' ) ( 'T' | 't' ) ( '_' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'G' | 'g' ) ( 'U' | 'u' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'Y' | 'y' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:31:22: ( 'S' | 's' ) ( 'T' | 't' ) ( 'R' | 'r' ) ( 'I' | 'i' ) ( 'C' | 'c' ) ( 'T' | 't' ) ( '_' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'G' | 'g' ) ( 'U' | 'u' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'Y' | 'y' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:31:76: ( '_' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:31:77: '_'
            {
            match('_'); 

            }

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRICT_CONTIGUITY"

    // $ANTLR start "PARTITION_CONTIGUITY"
    public final void mPARTITION_CONTIGUITY() throws RecognitionException {
        try {
            int _type = PARTITION_CONTIGUITY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:21: ( ( 'P' | 'p' ) ( 'A' | 'a' ) ( 'R' | 'r' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( '_' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'G' | 'g' ) ( 'U' | 'u' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'Y' | 'y' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:25: ( 'P' | 'p' ) ( 'A' | 'a' ) ( 'R' | 'r' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( '_' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'G' | 'g' ) ( 'U' | 'u' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'Y' | 'y' )
            {
            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:106: ( '_' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:107: '_'
            {
            match('_'); 

            }

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PARTITION_CONTIGUITY"

    // $ANTLR start "AVG"
    public final void mAVG() throws RecognitionException {
        try {
            int _type = AVG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:34:5: ( ( 'A' | 'a' ) ( 'V' | 'v' ) ( 'G' | 'g' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:34:7: ( 'A' | 'a' ) ( 'V' | 'v' ) ( 'G' | 'g' )
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AVG"

    // $ANTLR start "MIN"
    public final void mMIN() throws RecognitionException {
        try {
            int _type = MIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:35:5: ( ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'N' | 'n' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:35:7: ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MIN"

    // $ANTLR start "MAX"
    public final void mMAX() throws RecognitionException {
        try {
            int _type = MAX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:36:5: ( ( 'M' | 'm' ) ( 'A' | 'a' ) ( 'X' | 'x' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:36:7: ( 'M' | 'm' ) ( 'A' | 'a' ) ( 'X' | 'x' )
            {
            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MAX"

    // $ANTLR start "SUM"
    public final void mSUM() throws RecognitionException {
        try {
            int _type = SUM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:37:5: ( ( 'S' | 's' ) ( 'U' | 'u' ) ( 'M' | 'm' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:37:7: ( 'S' | 's' ) ( 'U' | 'u' ) ( 'M' | 'm' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SUM"

    // $ANTLR start "COUNT"
    public final void mCOUNT() throws RecognitionException {
        try {
            int _type = COUNT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:38:7: ( ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'T' | 't' ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:38:9: ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'T' | 't' )
            {
            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COUNT"

    // $ANTLR start "PLUS"
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:41:6: ( '+' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:41:8: '+'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:42:7: ( '-' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:42:9: '-'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:43:7: ( '.' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:43:9: '.'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:44:9: ( '/' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:44:11: '/'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:45:6: ( '*' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:45:8: '*'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:11: ( '<=' | '>=' | '!=' | '<' | '>' )
            int alt7=5;
            switch ( input.LA(1) ) {
            case '<':
                {
                int LA7_1 = input.LA(2);

                if ( (LA7_1=='=') ) {
                    alt7=1;
                }
                else {
                    alt7=4;}
                }
                break;
            case '>':
                {
                int LA7_2 = input.LA(2);

                if ( (LA7_2=='=') ) {
                    alt7=2;
                }
                else {
                    alt7=5;}
                }
                break;
            case '!':
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
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:13: '<='
                    {
                    match("<="); 


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:18: '>='
                    {
                    match(">="); 


                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:23: '!='
                    {
                    match("!="); 


                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:28: '<'
                    {
                    match('<'); 

                    }
                    break;
                case 5 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:32: '>'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:48:14: ( '=' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:48:16: '='
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:49:9: ( '==' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:49:11: '=='
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

    // $ANTLR start "NOTSIGN"
    public final void mNOTSIGN() throws RecognitionException {
        try {
            int _type = NOTSIGN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:51:9: ( '~' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:51:11: '~'
            {
            match('~'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOTSIGN"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:53:7: ( ',' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:53:9: ','
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:56:2: ( '(' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:56:4: '('
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:58:2: ( ')' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:58:4: ')'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:61:8: ( INTEGER | FLOAT )
            int alt8=2;
            alt8 = dfa8.predict(input);
            switch (alt8) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:61:11: INTEGER
                    {
                    mINTEGER(); 

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:61:21: FLOAT
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:62:16: ( INTEGER '.' ( DIGIT )+ )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:62:18: INTEGER '.' ( DIGIT )+
            {
            mINTEGER(); 
            match('.'); 
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:62:30: ( DIGIT )+
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
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:62:30: DIGIT
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:63:18: ( '0' | '1' .. '9' ( '0' .. '9' )* )
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
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:63:20: '0'
                    {
                    match('0'); 

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:63:26: '1' .. '9' ( '0' .. '9' )*
                    {
                    matchRange('1','9'); 
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:63:35: ( '0' .. '9' )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( ((LA10_0>='0' && LA10_0<='9')) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:63:36: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

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

    // $ANTLR start "NAME"
    public final void mNAME() throws RecognitionException {
        try {
            int _type = NAME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:65:6: ( LETTER ( LETTER | DIGIT | '_' | ':' )* )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:65:8: LETTER ( LETTER | DIGIT | '_' | ':' )*
            {
            mLETTER(); 
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:65:15: ( LETTER | DIGIT | '_' | ':' )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>='0' && LA12_0<=':')||(LA12_0>='A' && LA12_0<='Z')||LA12_0=='_'||(LA12_0>='a' && LA12_0<='z')) ) {
                    alt12=1;
                }


                switch (alt12) {
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
    // $ANTLR end "NAME"

    // $ANTLR start "STRING_LITERAL"
    public final void mSTRING_LITERAL() throws RecognitionException {
        try {
            int _type = STRING_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:66:16: ( '\"' ( NONCONTROL_CHAR )* '\"' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:66:18: '\"' ( NONCONTROL_CHAR )* '\"'
            {
            match('\"'); 
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:66:22: ( NONCONTROL_CHAR )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0=='\t'||LA13_0==' '||(LA13_0>='0' && LA13_0<='9')||(LA13_0>='A' && LA13_0<='Z')||(LA13_0>='a' && LA13_0<='z')) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:66:22: NONCONTROL_CHAR
            	    {
            	    mNONCONTROL_CHAR(); 

            	    }
            	    break;

            	default :
            	    break loop13;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:68:26: ( LETTER | DIGIT | SPACE )
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:69:17: ( LOWER | UPPER )
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:70:17: ( 'a' .. 'z' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:70:19: 'a' .. 'z'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:71:17: ( 'A' .. 'Z' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:71:19: 'A' .. 'Z'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:72:17: ( '0' .. '9' )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:72:19: '0' .. '9'
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:73:17: ( ' ' | '\\t' )
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:75:9: ( ( ( '\\r' )? '\\n' )+ )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:75:11: ( ( '\\r' )? '\\n' )+
            {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:75:11: ( ( '\\r' )? '\\n' )+
            int cnt15=0;
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0=='\n'||LA15_0=='\r') ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:75:12: ( '\\r' )? '\\n'
            	    {
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:75:12: ( '\\r' )?
            	    int alt14=2;
            	    int LA14_0 = input.LA(1);

            	    if ( (LA14_0=='\r') ) {
            	        alt14=1;
            	    }
            	    switch (alt14) {
            	        case 1 :
            	            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:75:12: '\\r'
            	            {
            	            match('\r'); 

            	            }
            	            break;

            	    }

            	    match('\n'); 

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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:77:12: ( ( SPACE )+ )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:77:14: ( SPACE )+
            {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:77:14: ( SPACE )+
            int cnt16=0;
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0=='\t'||LA16_0==' ') ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:77:14: SPACE
            	    {
            	    mSPACE(); 

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
    // $ANTLR end "WHITESPACE"

    public void mTokens() throws RecognitionException {
        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:8: ( CREATE | STREAM | VIEW | PATTERN | WHERE | WITHIN | RETURN | SEQ | LEFTCURLY | RIGHTCURLY | AND | LEN | BBRACKETLEFT | BBRACKETRIGHT | WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND | SKIP_TILL_NEXT_MATCH | SKIP_TILL_ANY_MATCH | STRICT_CONTIGUITY | PARTITION_CONTIGUITY | AVG | MIN | MAX | SUM | COUNT | PLUS | MINUS | POINT | DIVISION | MULT | COMPAREOP | SINGLEEQUALS | EQUALS | NOTSIGN | COMMA | LBRACKET | RBRACKET | NUMBER | NAME | STRING_LITERAL | NEWLINE | WHITESPACE )
        int alt17=46;
        alt17 = dfa17.predict(input);
        switch (alt17) {
            case 1 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:10: CREATE
                {
                mCREATE(); 

                }
                break;
            case 2 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:17: STREAM
                {
                mSTREAM(); 

                }
                break;
            case 3 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:24: VIEW
                {
                mVIEW(); 

                }
                break;
            case 4 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:29: PATTERN
                {
                mPATTERN(); 

                }
                break;
            case 5 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:37: WHERE
                {
                mWHERE(); 

                }
                break;
            case 6 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:43: WITHIN
                {
                mWITHIN(); 

                }
                break;
            case 7 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:50: RETURN
                {
                mRETURN(); 

                }
                break;
            case 8 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:57: SEQ
                {
                mSEQ(); 

                }
                break;
            case 9 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:61: LEFTCURLY
                {
                mLEFTCURLY(); 

                }
                break;
            case 10 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:71: RIGHTCURLY
                {
                mRIGHTCURLY(); 

                }
                break;
            case 11 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:82: AND
                {
                mAND(); 

                }
                break;
            case 12 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:86: LEN
                {
                mLEN(); 

                }
                break;
            case 13 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:90: BBRACKETLEFT
                {
                mBBRACKETLEFT(); 

                }
                break;
            case 14 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:103: BBRACKETRIGHT
                {
                mBBRACKETRIGHT(); 

                }
                break;
            case 15 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:117: WEEK
                {
                mWEEK(); 

                }
                break;
            case 16 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:122: DAY
                {
                mDAY(); 

                }
                break;
            case 17 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:126: HOUR
                {
                mHOUR(); 

                }
                break;
            case 18 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:131: MINUTE
                {
                mMINUTE(); 

                }
                break;
            case 19 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:138: SECOND
                {
                mSECOND(); 

                }
                break;
            case 20 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:145: MILLISECOND
                {
                mMILLISECOND(); 

                }
                break;
            case 21 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:157: SKIP_TILL_NEXT_MATCH
                {
                mSKIP_TILL_NEXT_MATCH(); 

                }
                break;
            case 22 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:178: SKIP_TILL_ANY_MATCH
                {
                mSKIP_TILL_ANY_MATCH(); 

                }
                break;
            case 23 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:198: STRICT_CONTIGUITY
                {
                mSTRICT_CONTIGUITY(); 

                }
                break;
            case 24 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:216: PARTITION_CONTIGUITY
                {
                mPARTITION_CONTIGUITY(); 

                }
                break;
            case 25 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:237: AVG
                {
                mAVG(); 

                }
                break;
            case 26 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:241: MIN
                {
                mMIN(); 

                }
                break;
            case 27 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:245: MAX
                {
                mMAX(); 

                }
                break;
            case 28 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:249: SUM
                {
                mSUM(); 

                }
                break;
            case 29 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:253: COUNT
                {
                mCOUNT(); 

                }
                break;
            case 30 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:259: PLUS
                {
                mPLUS(); 

                }
                break;
            case 31 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:264: MINUS
                {
                mMINUS(); 

                }
                break;
            case 32 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:270: POINT
                {
                mPOINT(); 

                }
                break;
            case 33 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:276: DIVISION
                {
                mDIVISION(); 

                }
                break;
            case 34 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:285: MULT
                {
                mMULT(); 

                }
                break;
            case 35 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:290: COMPAREOP
                {
                mCOMPAREOP(); 

                }
                break;
            case 36 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:300: SINGLEEQUALS
                {
                mSINGLEEQUALS(); 

                }
                break;
            case 37 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:313: EQUALS
                {
                mEQUALS(); 

                }
                break;
            case 38 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:320: NOTSIGN
                {
                mNOTSIGN(); 

                }
                break;
            case 39 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:328: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 40 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:334: LBRACKET
                {
                mLBRACKET(); 

                }
                break;
            case 41 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:343: RBRACKET
                {
                mRBRACKET(); 

                }
                break;
            case 42 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:352: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 43 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:359: NAME
                {
                mNAME(); 

                }
                break;
            case 44 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:364: STRING_LITERAL
                {
                mSTRING_LITERAL(); 

                }
                break;
            case 45 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:379: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 46 :
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:387: WHITESPACE
                {
                mWHITESPACE(); 

                }
                break;

        }

    }


    protected DFA8 dfa8 = new DFA8(this);
    protected DFA17 dfa17 = new DFA17(this);
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
            return "61:1: NUMBER : ( INTEGER | FLOAT );";
        }
    }
    static final String DFA17_eotS =
        "\1\uffff\6\34\2\uffff\2\34\2\uffff\3\34\6\uffff\1\64\11\uffff\23"+
        "\34\2\uffff\3\34\1\117\2\34\1\122\7\34\1\132\1\133\1\134\1\136\1"+
        "\34\1\141\1\34\1\143\4\34\1\uffff\2\34\1\uffff\1\152\4\34\1\160"+
        "\1\34\3\uffff\1\136\1\uffff\1\163\1\34\1\uffff\1\34\1\uffff\1\34"+
        "\1\167\4\34\1\uffff\2\34\1\176\1\34\1\160\1\uffff\1\34\1\163\1\uffff"+
        "\2\34\1\u0083\1\uffff\1\u0084\1\34\1\u0087\3\34\1\uffff\1\u008b"+
        "\1\u008c\1\u008d\1\34\2\uffff\1\34\1\u0087\1\uffff\1\34\1\u0092"+
        "\1\34\3\uffff\1\u008d\3\34\1\uffff\16\34\1\u00a7\4\34\1\u00a7\1"+
        "\uffff\20\34\1\u00bc\3\34\1\uffff\4\34\1\u00c4\1\34\1\u00c6\1\uffff"+
        "\1\u00c7\2\uffff";
    static final String DFA17_eofS =
        "\u00c8\uffff";
    static final String DFA17_minS =
        "\1\11\1\117\1\105\1\111\1\101\2\105\2\uffff\1\116\1\105\2\uffff"+
        "\1\101\1\117\1\101\6\uffff\1\75\11\uffff\1\105\1\125\1\122\1\103"+
        "\1\111\1\115\1\105\1\122\1\105\1\124\1\105\1\124\1\104\1\107\1\116"+
        "\1\131\1\125\1\114\1\130\2\uffff\1\101\1\116\1\105\1\60\1\117\1"+
        "\120\1\60\1\127\2\124\1\122\1\110\1\113\1\125\4\60\1\122\1\60\1"+
        "\114\1\60\2\124\1\101\1\103\1\uffff\1\116\1\137\1\uffff\1\60\1\105"+
        "\1\111\1\105\1\111\1\60\1\122\3\uffff\1\60\1\uffff\1\60\1\124\1"+
        "\uffff\1\111\1\uffff\1\105\1\60\1\115\1\124\1\104\1\124\1\uffff"+
        "\1\122\1\124\1\60\1\116\1\60\1\uffff\1\116\1\60\1\uffff\1\105\1"+
        "\123\1\60\1\uffff\1\60\1\137\1\60\1\111\1\116\1\111\1\uffff\3\60"+
        "\1\105\2\uffff\1\103\1\60\1\uffff\1\114\1\60\1\117\3\uffff\1\60"+
        "\1\103\1\117\1\114\1\uffff\1\116\1\117\1\116\2\137\1\116\1\124\1"+
        "\101\1\103\1\104\1\111\1\105\1\116\1\117\1\60\1\107\1\130\1\131"+
        "\1\116\1\60\1\uffff\1\125\1\124\1\137\1\124\1\111\1\137\1\115\1"+
        "\111\1\124\1\115\1\101\1\107\1\131\1\101\1\124\1\125\1\60\1\124"+
        "\1\103\1\111\1\uffff\1\103\1\110\1\124\1\110\1\60\1\131\1\60\1\uffff"+
        "\1\60\2\uffff";
    static final String DFA17_maxS =
        "\1\176\1\162\1\165\1\151\1\141\1\151\1\145\2\uffff\1\166\1\145"+
        "\2\uffff\1\141\1\157\1\151\6\uffff\1\75\11\uffff\1\145\1\165\1\162"+
        "\1\161\1\151\1\155\1\145\1\164\1\145\1\164\1\145\1\164\1\144\1\147"+
        "\1\156\1\171\1\165\1\156\1\170\2\uffff\1\141\1\156\1\151\1\172\1"+
        "\157\1\160\1\172\1\167\2\164\1\162\1\150\1\153\1\165\4\172\1\162"+
        "\1\172\1\154\1\172\2\164\1\141\1\143\1\uffff\1\156\1\137\1\uffff"+
        "\1\172\1\145\1\151\1\145\1\151\1\172\1\162\3\uffff\1\172\1\uffff"+
        "\1\172\1\164\1\uffff\1\151\1\uffff\1\145\1\172\1\155\1\164\1\144"+
        "\1\164\1\uffff\1\162\1\164\1\172\1\156\1\172\1\uffff\1\156\1\172"+
        "\1\uffff\1\145\1\163\1\172\1\uffff\1\172\1\137\1\172\1\151\1\156"+
        "\1\151\1\uffff\3\172\1\145\2\uffff\1\143\1\172\1\uffff\1\154\1\172"+
        "\1\157\3\uffff\1\172\1\143\1\157\1\154\1\uffff\1\156\1\157\1\156"+
        "\2\137\1\156\1\164\1\156\1\143\1\144\1\151\1\145\1\156\1\157\1\172"+
        "\1\147\1\170\1\171\1\156\1\172\1\uffff\1\165\1\164\1\137\1\164\1"+
        "\151\1\137\1\155\1\151\1\164\1\155\1\141\1\147\1\171\1\141\1\164"+
        "\1\165\1\172\1\164\1\143\1\151\1\uffff\1\143\1\150\1\164\1\150\1"+
        "\172\1\171\1\172\1\uffff\1\172\2\uffff";
    static final String DFA17_acceptS =
        "\7\uffff\1\11\1\12\2\uffff\1\15\1\16\3\uffff\1\36\1\37\1\40\1\41"+
        "\1\42\1\43\1\uffff\1\46\1\47\1\50\1\51\1\52\1\53\1\54\1\55\1\56"+
        "\23\uffff\1\45\1\44\32\uffff\1\10\2\uffff\1\34\7\uffff\1\13\1\31"+
        "\1\14\1\uffff\1\20\2\uffff\1\32\1\uffff\1\33\6\uffff\1\3\5\uffff"+
        "\1\17\2\uffff\1\21\3\uffff\1\35\6\uffff\1\5\4\uffff\1\1\1\2\2\uffff"+
        "\1\23\3\uffff\1\6\1\7\1\22\4\uffff\1\4\24\uffff\1\24\24\uffff\1"+
        "\27\7\uffff\1\26\1\uffff\1\25\1\30";
    static final String DFA17_specialS =
        "\u00c8\uffff}>";
    static final String[] DFA17_transitionS = {
            "\1\37\1\36\2\uffff\1\36\22\uffff\1\37\1\25\1\35\5\uffff\1\31"+
            "\1\32\1\24\1\20\1\30\1\21\1\22\1\23\12\33\2\uffff\1\25\1\26"+
            "\1\25\2\uffff\1\11\1\34\1\1\1\15\3\34\1\16\3\34\1\12\1\17\2"+
            "\34\1\4\1\34\1\6\1\2\2\34\1\3\1\5\3\34\1\13\1\uffff\1\14\3\uffff"+
            "\1\11\1\34\1\1\1\15\3\34\1\16\3\34\1\12\1\17\2\34\1\4\1\34\1"+
            "\6\1\2\2\34\1\3\1\5\3\34\1\7\1\uffff\1\10\1\27",
            "\1\41\2\uffff\1\40\34\uffff\1\41\2\uffff\1\40",
            "\1\43\5\uffff\1\44\10\uffff\1\42\1\45\17\uffff\1\43\5\uffff"+
            "\1\44\10\uffff\1\42\1\45",
            "\1\46\37\uffff\1\46",
            "\1\47\37\uffff\1\47",
            "\1\52\2\uffff\1\50\1\51\33\uffff\1\52\2\uffff\1\50\1\51",
            "\1\53\37\uffff\1\53",
            "",
            "",
            "\1\54\7\uffff\1\55\27\uffff\1\54\7\uffff\1\55",
            "\1\56\37\uffff\1\56",
            "",
            "",
            "\1\57\37\uffff\1\57",
            "\1\60\37\uffff\1\60",
            "\1\62\7\uffff\1\61\27\uffff\1\62\7\uffff\1\61",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\63",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\65\37\uffff\1\65",
            "\1\66\37\uffff\1\66",
            "\1\67\37\uffff\1\67",
            "\1\71\15\uffff\1\70\21\uffff\1\71\15\uffff\1\70",
            "\1\72\37\uffff\1\72",
            "\1\73\37\uffff\1\73",
            "\1\74\37\uffff\1\74",
            "\1\76\1\uffff\1\75\35\uffff\1\76\1\uffff\1\75",
            "\1\77\37\uffff\1\77",
            "\1\100\37\uffff\1\100",
            "\1\101\37\uffff\1\101",
            "\1\102\37\uffff\1\102",
            "\1\103\37\uffff\1\103",
            "\1\104\37\uffff\1\104",
            "\1\105\37\uffff\1\105",
            "\1\106\37\uffff\1\106",
            "\1\107\37\uffff\1\107",
            "\1\111\1\uffff\1\110\35\uffff\1\111\1\uffff\1\110",
            "\1\112\37\uffff\1\112",
            "",
            "",
            "\1\113\37\uffff\1\113",
            "\1\114\37\uffff\1\114",
            "\1\115\3\uffff\1\116\33\uffff\1\115\3\uffff\1\116",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "\1\120\37\uffff\1\120",
            "\1\121\37\uffff\1\121",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "\1\123\37\uffff\1\123",
            "\1\124\37\uffff\1\124",
            "\1\125\37\uffff\1\125",
            "\1\126\37\uffff\1\126",
            "\1\127\37\uffff\1\127",
            "\1\130\37\uffff\1\130",
            "\1\131\37\uffff\1\131",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "\13\34\6\uffff\22\34\1\135\7\34\4\uffff\1\34\1\uffff\22\34"+
            "\1\135\7\34",
            "\1\137\37\uffff\1\137",
            "\13\34\6\uffff\24\34\1\140\5\34\4\uffff\1\34\1\uffff\24\34"+
            "\1\140\5\34",
            "\1\142\37\uffff\1\142",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "\1\144\37\uffff\1\144",
            "\1\145\37\uffff\1\145",
            "\1\146\37\uffff\1\146",
            "\1\147\37\uffff\1\147",
            "",
            "\1\150\37\uffff\1\150",
            "\1\151",
            "",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "\1\153\37\uffff\1\153",
            "\1\154\37\uffff\1\154",
            "\1\155\37\uffff\1\155",
            "\1\156\37\uffff\1\156",
            "\13\34\6\uffff\22\34\1\157\7\34\4\uffff\1\34\1\uffff\22\34"+
            "\1\157\7\34",
            "\1\161\37\uffff\1\161",
            "",
            "",
            "",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "",
            "\13\34\6\uffff\22\34\1\162\7\34\4\uffff\1\34\1\uffff\22\34"+
            "\1\162\7\34",
            "\1\164\37\uffff\1\164",
            "",
            "\1\165\37\uffff\1\165",
            "",
            "\1\166\37\uffff\1\166",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "\1\170\37\uffff\1\170",
            "\1\171\37\uffff\1\171",
            "\1\172\37\uffff\1\172",
            "\1\173\37\uffff\1\173",
            "",
            "\1\174\37\uffff\1\174",
            "\1\175\37\uffff\1\175",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "\1\177\37\uffff\1\177",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "",
            "\1\u0080\37\uffff\1\u0080",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "",
            "\1\u0081\37\uffff\1\u0081",
            "\1\u0082\37\uffff\1\u0082",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "\1\u0085",
            "\13\34\6\uffff\22\34\1\u0086\7\34\4\uffff\1\34\1\uffff\22"+
            "\34\1\u0086\7\34",
            "\1\u0088\37\uffff\1\u0088",
            "\1\u0089\37\uffff\1\u0089",
            "\1\u008a\37\uffff\1\u008a",
            "",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "\13\34\6\uffff\22\34\1\u008e\7\34\4\uffff\1\34\1\uffff\22"+
            "\34\1\u008e\7\34",
            "\1\u008f\37\uffff\1\u008f",
            "",
            "",
            "\1\u0090\37\uffff\1\u0090",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "",
            "\1\u0091\37\uffff\1\u0091",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "\1\u0093\37\uffff\1\u0093",
            "",
            "",
            "",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "\1\u0094\37\uffff\1\u0094",
            "\1\u0095\37\uffff\1\u0095",
            "\1\u0096\37\uffff\1\u0096",
            "",
            "\1\u0097\37\uffff\1\u0097",
            "\1\u0098\37\uffff\1\u0098",
            "\1\u0099\37\uffff\1\u0099",
            "\1\u009a",
            "\1\u009b",
            "\1\u009c\37\uffff\1\u009c",
            "\1\u009d\37\uffff\1\u009d",
            "\1\u009f\14\uffff\1\u009e\22\uffff\1\u009f\14\uffff\1\u009e",
            "\1\u00a0\37\uffff\1\u00a0",
            "\1\u00a1\37\uffff\1\u00a1",
            "\1\u00a2\37\uffff\1\u00a2",
            "\1\u00a3\37\uffff\1\u00a3",
            "\1\u00a4\37\uffff\1\u00a4",
            "\1\u00a5\37\uffff\1\u00a5",
            "\13\34\6\uffff\22\34\1\u00a6\7\34\4\uffff\1\34\1\uffff\22"+
            "\34\1\u00a6\7\34",
            "\1\u00a8\37\uffff\1\u00a8",
            "\1\u00a9\37\uffff\1\u00a9",
            "\1\u00aa\37\uffff\1\u00aa",
            "\1\u00ab\37\uffff\1\u00ab",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "",
            "\1\u00ac\37\uffff\1\u00ac",
            "\1\u00ad\37\uffff\1\u00ad",
            "\1\u00ae",
            "\1\u00af\37\uffff\1\u00af",
            "\1\u00b0\37\uffff\1\u00b0",
            "\1\u00b1",
            "\1\u00b2\37\uffff\1\u00b2",
            "\1\u00b3\37\uffff\1\u00b3",
            "\1\u00b4\37\uffff\1\u00b4",
            "\1\u00b5\37\uffff\1\u00b5",
            "\1\u00b6\37\uffff\1\u00b6",
            "\1\u00b7\37\uffff\1\u00b7",
            "\1\u00b8\37\uffff\1\u00b8",
            "\1\u00b9\37\uffff\1\u00b9",
            "\1\u00ba\37\uffff\1\u00ba",
            "\1\u00bb\37\uffff\1\u00bb",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "\1\u00bd\37\uffff\1\u00bd",
            "\1\u00be\37\uffff\1\u00be",
            "\1\u00bf\37\uffff\1\u00bf",
            "",
            "\1\u00c0\37\uffff\1\u00c0",
            "\1\u00c1\37\uffff\1\u00c1",
            "\1\u00c2\37\uffff\1\u00c2",
            "\1\u00c3\37\uffff\1\u00c3",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "\1\u00c5\37\uffff\1\u00c5",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "",
            "\13\34\6\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
            "",
            ""
    };

    static final short[] DFA17_eot = DFA.unpackEncodedString(DFA17_eotS);
    static final short[] DFA17_eof = DFA.unpackEncodedString(DFA17_eofS);
    static final char[] DFA17_min = DFA.unpackEncodedStringToUnsignedChars(DFA17_minS);
    static final char[] DFA17_max = DFA.unpackEncodedStringToUnsignedChars(DFA17_maxS);
    static final short[] DFA17_accept = DFA.unpackEncodedString(DFA17_acceptS);
    static final short[] DFA17_special = DFA.unpackEncodedString(DFA17_specialS);
    static final short[][] DFA17_transition;

    static {
        int numStates = DFA17_transitionS.length;
        DFA17_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA17_transition[i] = DFA.unpackEncodedString(DFA17_transitionS[i]);
        }
    }

    class DFA17 extends DFA {

        public DFA17(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 17;
            this.eot = DFA17_eot;
            this.eof = DFA17_eof;
            this.min = DFA17_min;
            this.max = DFA17_max;
            this.accept = DFA17_accept;
            this.special = DFA17_special;
            this.transition = DFA17_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( CREATE | STREAM | VIEW | PATTERN | WHERE | WITHIN | RETURN | SEQ | LEFTCURLY | RIGHTCURLY | AND | LEN | BBRACKETLEFT | BBRACKETRIGHT | WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND | SKIP_TILL_NEXT_MATCH | SKIP_TILL_ANY_MATCH | STRICT_CONTIGUITY | PARTITION_CONTIGUITY | AVG | MIN | MAX | SUM | COUNT | PLUS | MINUS | POINT | DIVISION | MULT | COMPAREOP | SINGLEEQUALS | EQUALS | NOTSIGN | COMMA | LBRACKET | RBRACKET | NUMBER | NAME | STRING_LITERAL | NEWLINE | WHITESPACE );";
        }
    }
 

}