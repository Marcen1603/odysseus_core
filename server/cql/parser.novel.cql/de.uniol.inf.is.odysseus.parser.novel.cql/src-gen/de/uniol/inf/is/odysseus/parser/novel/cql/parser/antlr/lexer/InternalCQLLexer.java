package de.uniol.inf.is.odysseus.parser.novel.cql.parser.antlr.lexer;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalCQLLexer extends Lexer {
    public static final int CREATE=17;
    public static final int FROM=29;
    public static final int VIEW=34;
    public static final int LessThanSign=56;
    public static final int LeftParenthesis=46;
    public static final int UNBOUNDED=9;
    public static final int WRAPPER=15;
    public static final int PARTITION=7;
    public static final int IF=42;
    public static final int RightSquareBracket=60;
    public static final int GreaterThanSign=58;
    public static final int RULE_ID=61;
    public static final int IN=43;
    public static final int DISTINCT=10;
    public static final int SIZE=31;
    public static final int PROTOCOL=11;
    public static final int RightParenthesis=47;
    public static final int TRUE=33;
    public static final int OPTIONS=14;
    public static final int WHERE=26;
    public static final int GreaterThanSignEqualsSign=39;
    public static final int NOT=36;
    public static final int AS=40;
    public static final int INTERSECTION=4;
    public static final int CHANNEL=13;
    public static final int SINK=30;
    public static final int PlusSign=49;
    public static final int RULE_INT=62;
    public static final int AND=35;
    public static final int RULE_ML_COMMENT=65;
    public static final int LeftSquareBracket=59;
    public static final int ADVANCE=12;
    public static final int HAVING=19;
    public static final int RULE_STRING=64;
    public static final int DROP=27;
    public static final int RULE_SL_COMMENT=66;
    public static final int GROUP=23;
    public static final int Comma=50;
    public static final int EqualsSign=57;
    public static final int TRANSPORT=8;
    public static final int HyphenMinus=51;
    public static final int DIFFERENCE=6;
    public static final int BY=41;
    public static final int LessThanSignEqualsSign=38;
    public static final int Solidus=53;
    public static final int Colon=54;
    public static final int DATAHANDLER=5;
    public static final int FILE=28;
    public static final int EOF=-1;
    public static final int Asterisk=48;
    public static final int FullStop=52;
    public static final int OR=44;
    public static final int EXISTS=18;
    public static final int RULE_WS=67;
    public static final int STREAM=21;
    public static final int TIME=32;
    public static final int RULE_ANY_OTHER=68;
    public static final int SELECT=20;
    public static final int TUPLE=24;
    public static final int Semicolon=55;
    public static final int ATTACH=16;
    public static final int RULE_FLOAT=63;
    public static final int FALSE=22;
    public static final int ExclamationMarkEqualsSign=37;
    public static final int TO=45;
    public static final int UNION=25;

    // delegates
    // delegators

    public InternalCQLLexer() {;} 
    public InternalCQLLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InternalCQLLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "InternalCQLLexer.g"; }

    // $ANTLR start "INTERSECTION"
    public final void mINTERSECTION() throws RecognitionException {
        try {
            int _type = INTERSECTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:14:14: ( ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) )
            // InternalCQLLexer.g:14:16: ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' )
            {
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INTERSECTION"

    // $ANTLR start "DATAHANDLER"
    public final void mDATAHANDLER() throws RecognitionException {
        try {
            int _type = DATAHANDLER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:16:13: ( ( 'D' | 'd' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'A' | 'a' ) ( 'H' | 'h' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'R' | 'r' ) )
            // InternalCQLLexer.g:16:15: ( 'D' | 'd' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'A' | 'a' ) ( 'H' | 'h' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'R' | 'r' )
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

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
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

            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
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

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
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
    // $ANTLR end "DATAHANDLER"

    // $ANTLR start "DIFFERENCE"
    public final void mDIFFERENCE() throws RecognitionException {
        try {
            int _type = DIFFERENCE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:18:12: ( ( 'D' | 'd' ) ( 'I' | 'i' ) ( 'F' | 'f' ) ( 'F' | 'f' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'N' | 'n' ) ( 'C' | 'c' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:18:14: ( 'D' | 'd' ) ( 'I' | 'i' ) ( 'F' | 'f' ) ( 'F' | 'f' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'N' | 'n' ) ( 'C' | 'c' ) ( 'E' | 'e' )
            {
            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
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

            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
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

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
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
    // $ANTLR end "DIFFERENCE"

    // $ANTLR start "PARTITION"
    public final void mPARTITION() throws RecognitionException {
        try {
            int _type = PARTITION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:20:11: ( ( 'P' | 'p' ) ( 'A' | 'a' ) ( 'R' | 'r' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) )
            // InternalCQLLexer.g:20:13: ( 'P' | 'p' ) ( 'A' | 'a' ) ( 'R' | 'r' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' )
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PARTITION"

    // $ANTLR start "TRANSPORT"
    public final void mTRANSPORT() throws RecognitionException {
        try {
            int _type = TRANSPORT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:22:11: ( ( 'T' | 't' ) ( 'R' | 'r' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'S' | 's' ) ( 'P' | 'p' ) ( 'O' | 'o' ) ( 'R' | 'r' ) ( 'T' | 't' ) )
            // InternalCQLLexer.g:22:13: ( 'T' | 't' ) ( 'R' | 'r' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'S' | 's' ) ( 'P' | 'p' ) ( 'O' | 'o' ) ( 'R' | 'r' ) ( 'T' | 't' )
            {
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

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
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

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TRANSPORT"

    // $ANTLR start "UNBOUNDED"
    public final void mUNBOUNDED() throws RecognitionException {
        try {
            int _type = UNBOUNDED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:24:11: ( ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'B' | 'b' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'E' | 'e' ) ( 'D' | 'd' ) )
            // InternalCQLLexer.g:24:13: ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'B' | 'b' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'E' | 'e' ) ( 'D' | 'd' )
            {
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

            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
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

            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
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
    // $ANTLR end "UNBOUNDED"

    // $ANTLR start "DISTINCT"
    public final void mDISTINCT() throws RecognitionException {
        try {
            int _type = DISTINCT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:26:10: ( ( 'D' | 'd' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'C' | 'c' ) ( 'T' | 't' ) )
            // InternalCQLLexer.g:26:12: ( 'D' | 'd' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'C' | 'c' ) ( 'T' | 't' )
            {
            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
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

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DISTINCT"

    // $ANTLR start "PROTOCOL"
    public final void mPROTOCOL() throws RecognitionException {
        try {
            int _type = PROTOCOL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:28:10: ( ( 'P' | 'p' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'T' | 't' ) ( 'O' | 'o' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'L' | 'l' ) )
            // InternalCQLLexer.g:28:12: ( 'P' | 'p' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'T' | 't' ) ( 'O' | 'o' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'L' | 'l' )
            {
            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
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

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
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

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
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

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
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
    // $ANTLR end "PROTOCOL"

    // $ANTLR start "ADVANCE"
    public final void mADVANCE() throws RecognitionException {
        try {
            int _type = ADVANCE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:30:9: ( ( 'A' | 'a' ) ( 'D' | 'd' ) ( 'V' | 'v' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'C' | 'c' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:30:11: ( 'A' | 'a' ) ( 'D' | 'd' ) ( 'V' | 'v' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'C' | 'c' ) ( 'E' | 'e' )
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
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

            if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
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

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
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
    // $ANTLR end "ADVANCE"

    // $ANTLR start "CHANNEL"
    public final void mCHANNEL() throws RecognitionException {
        try {
            int _type = CHANNEL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:32:9: ( ( 'C' | 'c' ) ( 'H' | 'h' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'N' | 'n' ) ( 'E' | 'e' ) ( 'L' | 'l' ) )
            // InternalCQLLexer.g:32:11: ( 'C' | 'c' ) ( 'H' | 'h' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'N' | 'n' ) ( 'E' | 'e' ) ( 'L' | 'l' )
            {
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

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
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
    // $ANTLR end "CHANNEL"

    // $ANTLR start "OPTIONS"
    public final void mOPTIONS() throws RecognitionException {
        try {
            int _type = OPTIONS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:34:9: ( ( 'O' | 'o' ) ( 'P' | 'p' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'S' | 's' ) )
            // InternalCQLLexer.g:34:11: ( 'O' | 'o' ) ( 'P' | 'p' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'S' | 's' )
            {
            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
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

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
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
    // $ANTLR end "OPTIONS"

    // $ANTLR start "WRAPPER"
    public final void mWRAPPER() throws RecognitionException {
        try {
            int _type = WRAPPER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:36:9: ( ( 'W' | 'w' ) ( 'R' | 'r' ) ( 'A' | 'a' ) ( 'P' | 'p' ) ( 'P' | 'p' ) ( 'E' | 'e' ) ( 'R' | 'r' ) )
            // InternalCQLLexer.g:36:11: ( 'W' | 'w' ) ( 'R' | 'r' ) ( 'A' | 'a' ) ( 'P' | 'p' ) ( 'P' | 'p' ) ( 'E' | 'e' ) ( 'R' | 'r' )
            {
            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
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

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
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

            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WRAPPER"

    // $ANTLR start "ATTACH"
    public final void mATTACH() throws RecognitionException {
        try {
            int _type = ATTACH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:38:8: ( ( 'A' | 'a' ) ( 'T' | 't' ) ( 'T' | 't' ) ( 'A' | 'a' ) ( 'C' | 'c' ) ( 'H' | 'h' ) )
            // InternalCQLLexer.g:38:10: ( 'A' | 'a' ) ( 'T' | 't' ) ( 'T' | 't' ) ( 'A' | 'a' ) ( 'C' | 'c' ) ( 'H' | 'h' )
            {
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

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
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
    // $ANTLR end "ATTACH"

    // $ANTLR start "CREATE"
    public final void mCREATE() throws RecognitionException {
        try {
            int _type = CREATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:40:8: ( ( 'C' | 'c' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:40:10: ( 'C' | 'c' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'E' | 'e' )
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

    // $ANTLR start "EXISTS"
    public final void mEXISTS() throws RecognitionException {
        try {
            int _type = EXISTS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:42:8: ( ( 'E' | 'e' ) ( 'X' | 'x' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'T' | 't' ) ( 'S' | 's' ) )
            // InternalCQLLexer.g:42:10: ( 'E' | 'e' ) ( 'X' | 'x' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'T' | 't' ) ( 'S' | 's' )
            {
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

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EXISTS"

    // $ANTLR start "HAVING"
    public final void mHAVING() throws RecognitionException {
        try {
            int _type = HAVING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:44:8: ( ( 'H' | 'h' ) ( 'A' | 'a' ) ( 'V' | 'v' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'G' | 'g' ) )
            // InternalCQLLexer.g:44:10: ( 'H' | 'h' ) ( 'A' | 'a' ) ( 'V' | 'v' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'G' | 'g' )
            {
            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
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

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
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
    // $ANTLR end "HAVING"

    // $ANTLR start "SELECT"
    public final void mSELECT() throws RecognitionException {
        try {
            int _type = SELECT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:46:8: ( ( 'S' | 's' ) ( 'E' | 'e' ) ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'T' | 't' ) )
            // InternalCQLLexer.g:46:10: ( 'S' | 's' ) ( 'E' | 'e' ) ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'T' | 't' )
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SELECT"

    // $ANTLR start "STREAM"
    public final void mSTREAM() throws RecognitionException {
        try {
            int _type = STREAM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:48:8: ( ( 'S' | 's' ) ( 'T' | 't' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'M' | 'm' ) )
            // InternalCQLLexer.g:48:10: ( 'S' | 's' ) ( 'T' | 't' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'M' | 'm' )
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

    // $ANTLR start "FALSE"
    public final void mFALSE() throws RecognitionException {
        try {
            int _type = FALSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:50:7: ( ( 'F' | 'f' ) ( 'A' | 'a' ) ( 'L' | 'l' ) ( 'S' | 's' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:50:9: ( 'F' | 'f' ) ( 'A' | 'a' ) ( 'L' | 'l' ) ( 'S' | 's' ) ( 'E' | 'e' )
            {
            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
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

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FALSE"

    // $ANTLR start "GROUP"
    public final void mGROUP() throws RecognitionException {
        try {
            int _type = GROUP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:52:7: ( ( 'G' | 'g' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'P' | 'p' ) )
            // InternalCQLLexer.g:52:9: ( 'G' | 'g' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'P' | 'p' )
            {
            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
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

            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
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
    // $ANTLR end "GROUP"

    // $ANTLR start "TUPLE"
    public final void mTUPLE() throws RecognitionException {
        try {
            int _type = TUPLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:54:7: ( ( 'T' | 't' ) ( 'U' | 'u' ) ( 'P' | 'p' ) ( 'L' | 'l' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:54:9: ( 'T' | 't' ) ( 'U' | 'u' ) ( 'P' | 'p' ) ( 'L' | 'l' ) ( 'E' | 'e' )
            {
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

            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
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
    // $ANTLR end "TUPLE"

    // $ANTLR start "UNION"
    public final void mUNION() throws RecognitionException {
        try {
            int _type = UNION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:56:7: ( ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) )
            // InternalCQLLexer.g:56:9: ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' )
            {
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UNION"

    // $ANTLR start "WHERE"
    public final void mWHERE() throws RecognitionException {
        try {
            int _type = WHERE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:58:7: ( ( 'W' | 'w' ) ( 'H' | 'h' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:58:9: ( 'W' | 'w' ) ( 'H' | 'h' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'E' | 'e' )
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

    // $ANTLR start "DROP"
    public final void mDROP() throws RecognitionException {
        try {
            int _type = DROP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:60:6: ( ( 'D' | 'd' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'P' | 'p' ) )
            // InternalCQLLexer.g:60:8: ( 'D' | 'd' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'P' | 'p' )
            {
            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
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

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DROP"

    // $ANTLR start "FILE"
    public final void mFILE() throws RecognitionException {
        try {
            int _type = FILE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:62:6: ( ( 'F' | 'f' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:62:8: ( 'F' | 'f' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'E' | 'e' )
            {
            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
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
    // $ANTLR end "FILE"

    // $ANTLR start "FROM"
    public final void mFROM() throws RecognitionException {
        try {
            int _type = FROM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:64:6: ( ( 'F' | 'f' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'M' | 'm' ) )
            // InternalCQLLexer.g:64:8: ( 'F' | 'f' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'M' | 'm' )
            {
            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
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

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
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
    // $ANTLR end "FROM"

    // $ANTLR start "SINK"
    public final void mSINK() throws RecognitionException {
        try {
            int _type = SINK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:66:6: ( ( 'S' | 's' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'K' | 'k' ) )
            // InternalCQLLexer.g:66:8: ( 'S' | 's' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'K' | 'k' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
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

            if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
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
    // $ANTLR end "SINK"

    // $ANTLR start "SIZE"
    public final void mSIZE() throws RecognitionException {
        try {
            int _type = SIZE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:68:6: ( ( 'S' | 's' ) ( 'I' | 'i' ) ( 'Z' | 'z' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:68:8: ( 'S' | 's' ) ( 'I' | 'i' ) ( 'Z' | 'z' ) ( 'E' | 'e' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
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

            if ( input.LA(1)=='Z'||input.LA(1)=='z' ) {
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
    // $ANTLR end "SIZE"

    // $ANTLR start "TIME"
    public final void mTIME() throws RecognitionException {
        try {
            int _type = TIME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:70:6: ( ( 'T' | 't' ) ( 'I' | 'i' ) ( 'M' | 'm' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:70:8: ( 'T' | 't' ) ( 'I' | 'i' ) ( 'M' | 'm' ) ( 'E' | 'e' )
            {
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

            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
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
    // $ANTLR end "TIME"

    // $ANTLR start "TRUE"
    public final void mTRUE() throws RecognitionException {
        try {
            int _type = TRUE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:72:6: ( ( 'T' | 't' ) ( 'R' | 'r' ) ( 'U' | 'u' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:72:8: ( 'T' | 't' ) ( 'R' | 'r' ) ( 'U' | 'u' ) ( 'E' | 'e' )
            {
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

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
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
    // $ANTLR end "TRUE"

    // $ANTLR start "VIEW"
    public final void mVIEW() throws RecognitionException {
        try {
            int _type = VIEW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:74:6: ( ( 'V' | 'v' ) ( 'I' | 'i' ) ( 'E' | 'e' ) ( 'W' | 'w' ) )
            // InternalCQLLexer.g:74:8: ( 'V' | 'v' ) ( 'I' | 'i' ) ( 'E' | 'e' ) ( 'W' | 'w' )
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

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:76:5: ( ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'D' | 'd' ) )
            // InternalCQLLexer.g:76:7: ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'D' | 'd' )
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

    // $ANTLR start "NOT"
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:78:5: ( ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'T' | 't' ) )
            // InternalCQLLexer.g:78:7: ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'T' | 't' )
            {
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
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
    // $ANTLR end "NOT"

    // $ANTLR start "ExclamationMarkEqualsSign"
    public final void mExclamationMarkEqualsSign() throws RecognitionException {
        try {
            int _type = ExclamationMarkEqualsSign;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:80:27: ( '!' '=' )
            // InternalCQLLexer.g:80:29: '!' '='
            {
            match('!'); 
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ExclamationMarkEqualsSign"

    // $ANTLR start "LessThanSignEqualsSign"
    public final void mLessThanSignEqualsSign() throws RecognitionException {
        try {
            int _type = LessThanSignEqualsSign;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:82:24: ( '<' '=' )
            // InternalCQLLexer.g:82:26: '<' '='
            {
            match('<'); 
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LessThanSignEqualsSign"

    // $ANTLR start "GreaterThanSignEqualsSign"
    public final void mGreaterThanSignEqualsSign() throws RecognitionException {
        try {
            int _type = GreaterThanSignEqualsSign;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:84:27: ( '>' '=' )
            // InternalCQLLexer.g:84:29: '>' '='
            {
            match('>'); 
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GreaterThanSignEqualsSign"

    // $ANTLR start "AS"
    public final void mAS() throws RecognitionException {
        try {
            int _type = AS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:86:4: ( ( 'A' | 'a' ) ( 'S' | 's' ) )
            // InternalCQLLexer.g:86:6: ( 'A' | 'a' ) ( 'S' | 's' )
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AS"

    // $ANTLR start "BY"
    public final void mBY() throws RecognitionException {
        try {
            int _type = BY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:88:4: ( ( 'B' | 'b' ) ( 'Y' | 'y' ) )
            // InternalCQLLexer.g:88:6: ( 'B' | 'b' ) ( 'Y' | 'y' )
            {
            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
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
    // $ANTLR end "BY"

    // $ANTLR start "IF"
    public final void mIF() throws RecognitionException {
        try {
            int _type = IF;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:90:4: ( ( 'I' | 'i' ) ( 'F' | 'f' ) )
            // InternalCQLLexer.g:90:6: ( 'I' | 'i' ) ( 'F' | 'f' )
            {
            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
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
    // $ANTLR end "IF"

    // $ANTLR start "IN"
    public final void mIN() throws RecognitionException {
        try {
            int _type = IN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:92:4: ( ( 'I' | 'i' ) ( 'N' | 'n' ) )
            // InternalCQLLexer.g:92:6: ( 'I' | 'i' ) ( 'N' | 'n' )
            {
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
    // $ANTLR end "IN"

    // $ANTLR start "OR"
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:94:4: ( ( 'O' | 'o' ) ( 'R' | 'r' ) )
            // InternalCQLLexer.g:94:6: ( 'O' | 'o' ) ( 'R' | 'r' )
            {
            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OR"

    // $ANTLR start "TO"
    public final void mTO() throws RecognitionException {
        try {
            int _type = TO;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:96:4: ( ( 'T' | 't' ) ( 'O' | 'o' ) )
            // InternalCQLLexer.g:96:6: ( 'T' | 't' ) ( 'O' | 'o' )
            {
            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TO"

    // $ANTLR start "LeftParenthesis"
    public final void mLeftParenthesis() throws RecognitionException {
        try {
            int _type = LeftParenthesis;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:98:17: ( '(' )
            // InternalCQLLexer.g:98:19: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LeftParenthesis"

    // $ANTLR start "RightParenthesis"
    public final void mRightParenthesis() throws RecognitionException {
        try {
            int _type = RightParenthesis;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:100:18: ( ')' )
            // InternalCQLLexer.g:100:20: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RightParenthesis"

    // $ANTLR start "Asterisk"
    public final void mAsterisk() throws RecognitionException {
        try {
            int _type = Asterisk;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:102:10: ( '*' )
            // InternalCQLLexer.g:102:12: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Asterisk"

    // $ANTLR start "PlusSign"
    public final void mPlusSign() throws RecognitionException {
        try {
            int _type = PlusSign;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:104:10: ( '+' )
            // InternalCQLLexer.g:104:12: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PlusSign"

    // $ANTLR start "Comma"
    public final void mComma() throws RecognitionException {
        try {
            int _type = Comma;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:106:7: ( ',' )
            // InternalCQLLexer.g:106:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Comma"

    // $ANTLR start "HyphenMinus"
    public final void mHyphenMinus() throws RecognitionException {
        try {
            int _type = HyphenMinus;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:108:13: ( '-' )
            // InternalCQLLexer.g:108:15: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HyphenMinus"

    // $ANTLR start "FullStop"
    public final void mFullStop() throws RecognitionException {
        try {
            int _type = FullStop;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:110:10: ( '.' )
            // InternalCQLLexer.g:110:12: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FullStop"

    // $ANTLR start "Solidus"
    public final void mSolidus() throws RecognitionException {
        try {
            int _type = Solidus;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:112:9: ( '/' )
            // InternalCQLLexer.g:112:11: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Solidus"

    // $ANTLR start "Colon"
    public final void mColon() throws RecognitionException {
        try {
            int _type = Colon;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:114:7: ( ':' )
            // InternalCQLLexer.g:114:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Colon"

    // $ANTLR start "Semicolon"
    public final void mSemicolon() throws RecognitionException {
        try {
            int _type = Semicolon;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:116:11: ( ';' )
            // InternalCQLLexer.g:116:13: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Semicolon"

    // $ANTLR start "LessThanSign"
    public final void mLessThanSign() throws RecognitionException {
        try {
            int _type = LessThanSign;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:118:14: ( '<' )
            // InternalCQLLexer.g:118:16: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LessThanSign"

    // $ANTLR start "EqualsSign"
    public final void mEqualsSign() throws RecognitionException {
        try {
            int _type = EqualsSign;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:120:12: ( '=' )
            // InternalCQLLexer.g:120:14: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EqualsSign"

    // $ANTLR start "GreaterThanSign"
    public final void mGreaterThanSign() throws RecognitionException {
        try {
            int _type = GreaterThanSign;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:122:17: ( '>' )
            // InternalCQLLexer.g:122:19: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GreaterThanSign"

    // $ANTLR start "LeftSquareBracket"
    public final void mLeftSquareBracket() throws RecognitionException {
        try {
            int _type = LeftSquareBracket;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:124:19: ( '[' )
            // InternalCQLLexer.g:124:21: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LeftSquareBracket"

    // $ANTLR start "RightSquareBracket"
    public final void mRightSquareBracket() throws RecognitionException {
        try {
            int _type = RightSquareBracket;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:126:20: ( ']' )
            // InternalCQLLexer.g:126:22: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RightSquareBracket"

    // $ANTLR start "RULE_ID"
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:130:9: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' | ':' | '$' | '{' | '}' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | ':' | '$' | '{' | '}' | '0' .. '9' )* )
            // InternalCQLLexer.g:130:11: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | ':' | '$' | '{' | '}' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | ':' | '$' | '{' | '}' | '0' .. '9' )*
            {
            if ( input.LA(1)=='$'||input.LA(1)==':'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='{')||input.LA(1)=='}' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // InternalCQLLexer.g:130:51: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | ':' | '$' | '{' | '}' | '0' .. '9' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='$'||(LA1_0>='0' && LA1_0<=':')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='{')||LA1_0=='}') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalCQLLexer.g:
            	    {
            	    if ( input.LA(1)=='$'||(input.LA(1)>='0' && input.LA(1)<=':')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='{')||input.LA(1)=='}' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
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

    // $ANTLR start "RULE_FLOAT"
    public final void mRULE_FLOAT() throws RecognitionException {
        try {
            int _type = RULE_FLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:132:12: ( RULE_INT '.' RULE_INT )
            // InternalCQLLexer.g:132:14: RULE_INT '.' RULE_INT
            {
            mRULE_INT(); 
            match('.'); 
            mRULE_INT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_FLOAT"

    // $ANTLR start "RULE_INT"
    public final void mRULE_INT() throws RecognitionException {
        try {
            int _type = RULE_INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:134:10: ( ( '0' .. '9' )+ )
            // InternalCQLLexer.g:134:12: ( '0' .. '9' )+
            {
            // InternalCQLLexer.g:134:12: ( '0' .. '9' )+
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
            	    // InternalCQLLexer.g:134:13: '0' .. '9'
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
            // InternalCQLLexer.g:136:13: ( ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' ) )
            // InternalCQLLexer.g:136:15: ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            {
            // InternalCQLLexer.g:136:15: ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='\"') ) {
                alt5=1;
            }
            else if ( (LA5_0=='\'') ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // InternalCQLLexer.g:136:16: '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"'
                    {
                    match('\"'); 
                    // InternalCQLLexer.g:136:20: ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )*
                    loop3:
                    do {
                        int alt3=3;
                        int LA3_0 = input.LA(1);

                        if ( (LA3_0=='\\') ) {
                            alt3=1;
                        }
                        else if ( ((LA3_0>='\u0000' && LA3_0<='!')||(LA3_0>='#' && LA3_0<='[')||(LA3_0>=']' && LA3_0<='\uFFFF')) ) {
                            alt3=2;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // InternalCQLLexer.g:136:21: '\\\\' .
                    	    {
                    	    match('\\'); 
                    	    matchAny(); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalCQLLexer.g:136:28: ~ ( ( '\\\\' | '\"' ) )
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
                    	    break loop3;
                        }
                    } while (true);

                    match('\"'); 

                    }
                    break;
                case 2 :
                    // InternalCQLLexer.g:136:48: '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\''
                    {
                    match('\''); 
                    // InternalCQLLexer.g:136:53: ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )*
                    loop4:
                    do {
                        int alt4=3;
                        int LA4_0 = input.LA(1);

                        if ( (LA4_0=='\\') ) {
                            alt4=1;
                        }
                        else if ( ((LA4_0>='\u0000' && LA4_0<='&')||(LA4_0>='(' && LA4_0<='[')||(LA4_0>=']' && LA4_0<='\uFFFF')) ) {
                            alt4=2;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // InternalCQLLexer.g:136:54: '\\\\' .
                    	    {
                    	    match('\\'); 
                    	    matchAny(); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalCQLLexer.g:136:61: ~ ( ( '\\\\' | '\\'' ) )
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
                    	    break loop4;
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
            // InternalCQLLexer.g:138:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // InternalCQLLexer.g:138:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // InternalCQLLexer.g:138:24: ( options {greedy=false; } : . )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='*') ) {
                    int LA6_1 = input.LA(2);

                    if ( (LA6_1=='/') ) {
                        alt6=2;
                    }
                    else if ( ((LA6_1>='\u0000' && LA6_1<='.')||(LA6_1>='0' && LA6_1<='\uFFFF')) ) {
                        alt6=1;
                    }


                }
                else if ( ((LA6_0>='\u0000' && LA6_0<=')')||(LA6_0>='+' && LA6_0<='\uFFFF')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // InternalCQLLexer.g:138:52: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop6;
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
            // InternalCQLLexer.g:140:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // InternalCQLLexer.g:140:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // InternalCQLLexer.g:140:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='\u0000' && LA7_0<='\t')||(LA7_0>='\u000B' && LA7_0<='\f')||(LA7_0>='\u000E' && LA7_0<='\uFFFF')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // InternalCQLLexer.g:140:24: ~ ( ( '\\n' | '\\r' ) )
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
            	    break loop7;
                }
            } while (true);

            // InternalCQLLexer.g:140:40: ( ( '\\r' )? '\\n' )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='\n'||LA9_0=='\r') ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // InternalCQLLexer.g:140:41: ( '\\r' )? '\\n'
                    {
                    // InternalCQLLexer.g:140:41: ( '\\r' )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0=='\r') ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // InternalCQLLexer.g:140:41: '\\r'
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
            // InternalCQLLexer.g:142:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // InternalCQLLexer.g:142:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // InternalCQLLexer.g:142:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt10=0;
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( ((LA10_0>='\t' && LA10_0<='\n')||LA10_0=='\r'||LA10_0==' ') ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // InternalCQLLexer.g:
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
            	    if ( cnt10 >= 1 ) break loop10;
                        EarlyExitException eee =
                            new EarlyExitException(10, input);
                        throw eee;
                }
                cnt10++;
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
            // InternalCQLLexer.g:144:16: ( . )
            // InternalCQLLexer.g:144:18: .
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
        // InternalCQLLexer.g:1:8: ( INTERSECTION | DATAHANDLER | DIFFERENCE | PARTITION | TRANSPORT | UNBOUNDED | DISTINCT | PROTOCOL | ADVANCE | CHANNEL | OPTIONS | WRAPPER | ATTACH | CREATE | EXISTS | HAVING | SELECT | STREAM | FALSE | GROUP | TUPLE | UNION | WHERE | DROP | FILE | FROM | SINK | SIZE | TIME | TRUE | VIEW | AND | NOT | ExclamationMarkEqualsSign | LessThanSignEqualsSign | GreaterThanSignEqualsSign | AS | BY | IF | IN | OR | TO | LeftParenthesis | RightParenthesis | Asterisk | PlusSign | Comma | HyphenMinus | FullStop | Solidus | Colon | Semicolon | LessThanSign | EqualsSign | GreaterThanSign | LeftSquareBracket | RightSquareBracket | RULE_ID | RULE_FLOAT | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt11=65;
        alt11 = dfa11.predict(input);
        switch (alt11) {
            case 1 :
                // InternalCQLLexer.g:1:10: INTERSECTION
                {
                mINTERSECTION(); 

                }
                break;
            case 2 :
                // InternalCQLLexer.g:1:23: DATAHANDLER
                {
                mDATAHANDLER(); 

                }
                break;
            case 3 :
                // InternalCQLLexer.g:1:35: DIFFERENCE
                {
                mDIFFERENCE(); 

                }
                break;
            case 4 :
                // InternalCQLLexer.g:1:46: PARTITION
                {
                mPARTITION(); 

                }
                break;
            case 5 :
                // InternalCQLLexer.g:1:56: TRANSPORT
                {
                mTRANSPORT(); 

                }
                break;
            case 6 :
                // InternalCQLLexer.g:1:66: UNBOUNDED
                {
                mUNBOUNDED(); 

                }
                break;
            case 7 :
                // InternalCQLLexer.g:1:76: DISTINCT
                {
                mDISTINCT(); 

                }
                break;
            case 8 :
                // InternalCQLLexer.g:1:85: PROTOCOL
                {
                mPROTOCOL(); 

                }
                break;
            case 9 :
                // InternalCQLLexer.g:1:94: ADVANCE
                {
                mADVANCE(); 

                }
                break;
            case 10 :
                // InternalCQLLexer.g:1:102: CHANNEL
                {
                mCHANNEL(); 

                }
                break;
            case 11 :
                // InternalCQLLexer.g:1:110: OPTIONS
                {
                mOPTIONS(); 

                }
                break;
            case 12 :
                // InternalCQLLexer.g:1:118: WRAPPER
                {
                mWRAPPER(); 

                }
                break;
            case 13 :
                // InternalCQLLexer.g:1:126: ATTACH
                {
                mATTACH(); 

                }
                break;
            case 14 :
                // InternalCQLLexer.g:1:133: CREATE
                {
                mCREATE(); 

                }
                break;
            case 15 :
                // InternalCQLLexer.g:1:140: EXISTS
                {
                mEXISTS(); 

                }
                break;
            case 16 :
                // InternalCQLLexer.g:1:147: HAVING
                {
                mHAVING(); 

                }
                break;
            case 17 :
                // InternalCQLLexer.g:1:154: SELECT
                {
                mSELECT(); 

                }
                break;
            case 18 :
                // InternalCQLLexer.g:1:161: STREAM
                {
                mSTREAM(); 

                }
                break;
            case 19 :
                // InternalCQLLexer.g:1:168: FALSE
                {
                mFALSE(); 

                }
                break;
            case 20 :
                // InternalCQLLexer.g:1:174: GROUP
                {
                mGROUP(); 

                }
                break;
            case 21 :
                // InternalCQLLexer.g:1:180: TUPLE
                {
                mTUPLE(); 

                }
                break;
            case 22 :
                // InternalCQLLexer.g:1:186: UNION
                {
                mUNION(); 

                }
                break;
            case 23 :
                // InternalCQLLexer.g:1:192: WHERE
                {
                mWHERE(); 

                }
                break;
            case 24 :
                // InternalCQLLexer.g:1:198: DROP
                {
                mDROP(); 

                }
                break;
            case 25 :
                // InternalCQLLexer.g:1:203: FILE
                {
                mFILE(); 

                }
                break;
            case 26 :
                // InternalCQLLexer.g:1:208: FROM
                {
                mFROM(); 

                }
                break;
            case 27 :
                // InternalCQLLexer.g:1:213: SINK
                {
                mSINK(); 

                }
                break;
            case 28 :
                // InternalCQLLexer.g:1:218: SIZE
                {
                mSIZE(); 

                }
                break;
            case 29 :
                // InternalCQLLexer.g:1:223: TIME
                {
                mTIME(); 

                }
                break;
            case 30 :
                // InternalCQLLexer.g:1:228: TRUE
                {
                mTRUE(); 

                }
                break;
            case 31 :
                // InternalCQLLexer.g:1:233: VIEW
                {
                mVIEW(); 

                }
                break;
            case 32 :
                // InternalCQLLexer.g:1:238: AND
                {
                mAND(); 

                }
                break;
            case 33 :
                // InternalCQLLexer.g:1:242: NOT
                {
                mNOT(); 

                }
                break;
            case 34 :
                // InternalCQLLexer.g:1:246: ExclamationMarkEqualsSign
                {
                mExclamationMarkEqualsSign(); 

                }
                break;
            case 35 :
                // InternalCQLLexer.g:1:272: LessThanSignEqualsSign
                {
                mLessThanSignEqualsSign(); 

                }
                break;
            case 36 :
                // InternalCQLLexer.g:1:295: GreaterThanSignEqualsSign
                {
                mGreaterThanSignEqualsSign(); 

                }
                break;
            case 37 :
                // InternalCQLLexer.g:1:321: AS
                {
                mAS(); 

                }
                break;
            case 38 :
                // InternalCQLLexer.g:1:324: BY
                {
                mBY(); 

                }
                break;
            case 39 :
                // InternalCQLLexer.g:1:327: IF
                {
                mIF(); 

                }
                break;
            case 40 :
                // InternalCQLLexer.g:1:330: IN
                {
                mIN(); 

                }
                break;
            case 41 :
                // InternalCQLLexer.g:1:333: OR
                {
                mOR(); 

                }
                break;
            case 42 :
                // InternalCQLLexer.g:1:336: TO
                {
                mTO(); 

                }
                break;
            case 43 :
                // InternalCQLLexer.g:1:339: LeftParenthesis
                {
                mLeftParenthesis(); 

                }
                break;
            case 44 :
                // InternalCQLLexer.g:1:355: RightParenthesis
                {
                mRightParenthesis(); 

                }
                break;
            case 45 :
                // InternalCQLLexer.g:1:372: Asterisk
                {
                mAsterisk(); 

                }
                break;
            case 46 :
                // InternalCQLLexer.g:1:381: PlusSign
                {
                mPlusSign(); 

                }
                break;
            case 47 :
                // InternalCQLLexer.g:1:390: Comma
                {
                mComma(); 

                }
                break;
            case 48 :
                // InternalCQLLexer.g:1:396: HyphenMinus
                {
                mHyphenMinus(); 

                }
                break;
            case 49 :
                // InternalCQLLexer.g:1:408: FullStop
                {
                mFullStop(); 

                }
                break;
            case 50 :
                // InternalCQLLexer.g:1:417: Solidus
                {
                mSolidus(); 

                }
                break;
            case 51 :
                // InternalCQLLexer.g:1:425: Colon
                {
                mColon(); 

                }
                break;
            case 52 :
                // InternalCQLLexer.g:1:431: Semicolon
                {
                mSemicolon(); 

                }
                break;
            case 53 :
                // InternalCQLLexer.g:1:441: LessThanSign
                {
                mLessThanSign(); 

                }
                break;
            case 54 :
                // InternalCQLLexer.g:1:454: EqualsSign
                {
                mEqualsSign(); 

                }
                break;
            case 55 :
                // InternalCQLLexer.g:1:465: GreaterThanSign
                {
                mGreaterThanSign(); 

                }
                break;
            case 56 :
                // InternalCQLLexer.g:1:481: LeftSquareBracket
                {
                mLeftSquareBracket(); 

                }
                break;
            case 57 :
                // InternalCQLLexer.g:1:499: RightSquareBracket
                {
                mRightSquareBracket(); 

                }
                break;
            case 58 :
                // InternalCQLLexer.g:1:518: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 59 :
                // InternalCQLLexer.g:1:526: RULE_FLOAT
                {
                mRULE_FLOAT(); 

                }
                break;
            case 60 :
                // InternalCQLLexer.g:1:537: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 61 :
                // InternalCQLLexer.g:1:546: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 62 :
                // InternalCQLLexer.g:1:558: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 63 :
                // InternalCQLLexer.g:1:574: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 64 :
                // InternalCQLLexer.g:1:590: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 65 :
                // InternalCQLLexer.g:1:598: RULE_ANY_OTHER
                {
                mRULE_ANY_OTHER(); 

                }
                break;

        }

    }


    protected DFA11 dfa11 = new DFA11(this);
    static final String DFA11_eotS =
        "\1\uffff\20\52\1\47\1\114\1\116\1\52\7\uffff\1\131\1\132\5\uffff\1\137\2\47\2\uffff\1\145\1\146\1\uffff\10\52\1\161\4\52\1\167\3\52\1\173\15\52\5\uffff\1\u008a\21\uffff\1\137\2\uffff\1\52\2\uffff\12\52\1\uffff\4\52\1\u009a\1\uffff\3\52\1\uffff\15\52\1\u00ab\1\uffff\4\52\1\u00b0\3\52\1\u00b4\1\52\1\u00b6\4\52\1\uffff\11\52\1\u00c4\1\u00c5\1\52\1\u00c7\1\u00c8\1\52\1\u00ca\1\uffff\4\52\1\uffff\3\52\1\uffff\1\u00d2\1\uffff\1\52\1\u00d4\6\52\1\u00db\4\52\2\uffff\1\u00e0\2\uffff\1\u00e1\1\uffff\7\52\1\uffff\1\52\1\uffff\1\52\1\u00eb\1\52\1\u00ed\2\52\1\uffff\1\u00f0\1\u00f1\1\u00f2\1\u00f3\2\uffff\10\52\1\u00fc\1\uffff\1\u00fd\1\uffff\1\u00fe\1\u00ff\4\uffff\3\52\1\u0103\1\52\1\u0105\2\52\4\uffff\3\52\1\uffff\1\u010b\1\uffff\1\u010c\1\u010d\2\52\1\u0110\3\uffff\1\52\1\u0112\1\uffff\1\u0113\2\uffff";
    static final String DFA11_eofS =
        "\u0114\uffff";
    static final String DFA11_minS =
        "\1\0\1\106\2\101\1\111\1\116\1\104\1\110\1\120\1\110\1\130\1\101\1\105\1\101\1\122\1\111\1\117\3\75\1\131\7\uffff\1\52\1\44\5\uffff\1\56\2\0\2\uffff\2\44\1\uffff\1\124\1\106\1\117\1\122\1\117\1\101\1\120\1\115\1\44\1\102\1\126\1\124\1\104\1\44\1\101\1\105\1\124\1\44\1\101\1\105\1\111\1\126\1\114\1\122\1\116\2\114\2\117\1\105\1\124\5\uffff\1\44\21\uffff\1\56\2\uffff\1\105\2\uffff\1\101\1\106\1\124\1\120\2\124\1\116\1\105\1\114\1\105\1\uffff\2\117\2\101\1\44\1\uffff\1\116\1\101\1\111\1\uffff\1\120\1\122\1\123\1\111\2\105\1\113\1\105\1\123\1\105\1\115\1\125\1\127\1\44\1\uffff\1\122\1\110\1\105\1\111\1\44\1\111\1\117\1\123\1\44\1\105\1\44\1\125\2\116\1\103\1\uffff\1\116\1\124\1\117\1\120\1\105\1\124\1\116\1\103\1\101\2\44\1\105\2\44\1\120\1\44\1\uffff\1\123\1\101\1\122\1\116\1\uffff\1\124\1\103\1\120\1\uffff\1\44\1\uffff\1\116\1\44\1\103\1\110\2\105\1\116\1\105\1\44\1\123\1\107\1\124\1\115\2\uffff\1\44\2\uffff\1\44\1\uffff\1\105\1\116\1\105\1\103\1\111\2\117\1\uffff\1\104\1\uffff\1\105\1\44\1\114\1\44\1\123\1\122\1\uffff\4\44\2\uffff\1\103\1\104\1\116\1\124\1\117\1\114\1\122\1\105\1\44\1\uffff\1\44\1\uffff\2\44\4\uffff\1\124\1\114\1\103\1\44\1\116\1\44\1\124\1\104\4\uffff\1\111\2\105\1\uffff\1\44\1\uffff\2\44\1\117\1\122\1\44\3\uffff\1\116\1\44\1\uffff\1\44\2\uffff";
    static final String DFA11_maxS =
        "\1\uffff\1\156\2\162\1\165\1\156\1\164\3\162\1\170\1\141\1\164\2\162\1\151\1\157\3\75\1\171\7\uffff\1\57\1\175\5\uffff\1\71\2\uffff\2\uffff\2\175\1\uffff\1\164\1\163\1\157\1\162\1\157\1\165\1\160\1\155\1\175\1\151\1\166\1\164\1\144\1\175\1\141\1\145\1\164\1\175\1\141\1\145\1\151\1\166\1\154\1\162\1\172\2\154\2\157\1\145\1\164\5\uffff\1\175\21\uffff\1\71\2\uffff\1\145\2\uffff\1\141\1\146\1\164\1\160\2\164\1\156\1\145\1\154\1\145\1\uffff\2\157\2\141\1\175\1\uffff\1\156\1\141\1\151\1\uffff\1\160\1\162\1\163\1\151\2\145\1\153\1\145\1\163\1\145\1\155\1\165\1\167\1\175\1\uffff\1\162\1\150\1\145\1\151\1\175\1\151\1\157\1\163\1\175\1\145\1\175\1\165\2\156\1\143\1\uffff\1\156\1\164\1\157\1\160\1\145\1\164\1\156\1\143\1\141\2\175\1\145\2\175\1\160\1\175\1\uffff\1\163\1\141\1\162\1\156\1\uffff\1\164\1\143\1\160\1\uffff\1\175\1\uffff\1\156\1\175\1\143\1\150\2\145\1\156\1\145\1\175\1\163\1\147\1\164\1\155\2\uffff\1\175\2\uffff\1\175\1\uffff\1\145\1\156\1\145\1\143\1\151\2\157\1\uffff\1\144\1\uffff\1\145\1\175\1\154\1\175\1\163\1\162\1\uffff\4\175\2\uffff\1\143\1\144\1\156\1\164\1\157\1\154\1\162\1\145\1\175\1\uffff\1\175\1\uffff\2\175\4\uffff\1\164\1\154\1\143\1\175\1\156\1\175\1\164\1\144\4\uffff\1\151\2\145\1\uffff\1\175\1\uffff\2\175\1\157\1\162\1\175\3\uffff\1\156\1\175\1\uffff\1\175\2\uffff";
    static final String DFA11_acceptS =
        "\25\uffff\1\53\1\54\1\55\1\56\1\57\1\60\1\61\2\uffff\1\64\1\66\1\70\1\71\1\72\3\uffff\1\100\1\101\2\uffff\1\72\37\uffff\1\42\1\43\1\65\1\44\1\67\1\uffff\1\53\1\54\1\55\1\56\1\57\1\60\1\61\1\76\1\77\1\62\1\63\1\64\1\66\1\70\1\71\1\74\1\73\1\uffff\1\75\1\100\1\uffff\1\50\1\47\12\uffff\1\52\5\uffff\1\45\3\uffff\1\51\16\uffff\1\46\17\uffff\1\40\20\uffff\1\41\4\uffff\1\30\3\uffff\1\36\1\uffff\1\35\15\uffff\1\33\1\34\1\uffff\1\31\1\32\1\uffff\1\37\7\uffff\1\25\1\uffff\1\26\6\uffff\1\27\4\uffff\1\23\1\24\11\uffff\1\15\1\uffff\1\16\2\uffff\1\17\1\20\1\21\1\22\10\uffff\1\11\1\12\1\13\1\14\3\uffff\1\7\1\uffff\1\10\5\uffff\1\4\1\5\1\6\2\uffff\1\3\1\uffff\1\2\1\1";
    static final String DFA11_specialS =
        "\1\0\43\uffff\1\2\1\1\u00ee\uffff}>";
    static final String[] DFA11_transitionS = {
            "\11\47\2\46\2\47\1\46\22\47\1\46\1\21\1\44\1\47\1\42\2\47\1\45\1\25\1\26\1\27\1\30\1\31\1\32\1\33\1\34\12\43\1\35\1\36\1\22\1\37\1\23\2\47\1\6\1\24\1\7\1\2\1\12\1\15\1\16\1\13\1\1\4\42\1\20\1\10\1\3\2\42\1\14\1\4\1\5\1\17\1\11\3\42\1\40\1\47\1\41\1\47\1\42\1\47\1\6\1\24\1\7\1\2\1\12\1\15\1\16\1\13\1\1\4\42\1\20\1\10\1\3\2\42\1\14\1\4\1\5\1\17\1\11\4\42\1\47\1\42\uff82\47",
            "\1\51\7\uffff\1\50\27\uffff\1\51\7\uffff\1\50",
            "\1\53\7\uffff\1\54\10\uffff\1\55\16\uffff\1\53\7\uffff\1\54\10\uffff\1\55",
            "\1\56\20\uffff\1\57\16\uffff\1\56\20\uffff\1\57",
            "\1\62\5\uffff\1\63\2\uffff\1\60\2\uffff\1\61\23\uffff\1\62\5\uffff\1\63\2\uffff\1\60\2\uffff\1\61",
            "\1\64\37\uffff\1\64",
            "\1\65\11\uffff\1\67\4\uffff\1\70\1\66\17\uffff\1\65\11\uffff\1\67\4\uffff\1\70\1\66",
            "\1\71\11\uffff\1\72\25\uffff\1\71\11\uffff\1\72",
            "\1\73\1\uffff\1\74\35\uffff\1\73\1\uffff\1\74",
            "\1\76\11\uffff\1\75\25\uffff\1\76\11\uffff\1\75",
            "\1\77\37\uffff\1\77",
            "\1\100\37\uffff\1\100",
            "\1\101\3\uffff\1\103\12\uffff\1\102\20\uffff\1\101\3\uffff\1\103\12\uffff\1\102",
            "\1\104\7\uffff\1\105\10\uffff\1\106\16\uffff\1\104\7\uffff\1\105\10\uffff\1\106",
            "\1\107\37\uffff\1\107",
            "\1\110\37\uffff\1\110",
            "\1\111\37\uffff\1\111",
            "\1\112",
            "\1\113",
            "\1\115",
            "\1\117\37\uffff\1\117",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\127\4\uffff\1\130",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "",
            "",
            "",
            "",
            "",
            "\1\140\1\uffff\12\141",
            "\0\142",
            "\0\142",
            "",
            "",
            "\1\52\13\uffff\13\52\6\uffff\23\52\1\144\6\52\4\uffff\1\52\1\uffff\23\52\1\144\7\52\1\uffff\1\52",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "",
            "\1\147\37\uffff\1\147",
            "\1\150\14\uffff\1\151\22\uffff\1\150\14\uffff\1\151",
            "\1\152\37\uffff\1\152",
            "\1\153\37\uffff\1\153",
            "\1\154\37\uffff\1\154",
            "\1\155\23\uffff\1\156\13\uffff\1\155\23\uffff\1\156",
            "\1\157\37\uffff\1\157",
            "\1\160\37\uffff\1\160",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\162\6\uffff\1\163\30\uffff\1\162\6\uffff\1\163",
            "\1\164\37\uffff\1\164",
            "\1\165\37\uffff\1\165",
            "\1\166\37\uffff\1\166",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\170\37\uffff\1\170",
            "\1\171\37\uffff\1\171",
            "\1\172\37\uffff\1\172",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\174\37\uffff\1\174",
            "\1\175\37\uffff\1\175",
            "\1\176\37\uffff\1\176",
            "\1\177\37\uffff\1\177",
            "\1\u0080\37\uffff\1\u0080",
            "\1\u0081\37\uffff\1\u0081",
            "\1\u0082\13\uffff\1\u0083\23\uffff\1\u0082\13\uffff\1\u0083",
            "\1\u0084\37\uffff\1\u0084",
            "\1\u0085\37\uffff\1\u0085",
            "\1\u0086\37\uffff\1\u0086",
            "\1\u0087\37\uffff\1\u0087",
            "\1\u0088\37\uffff\1\u0088",
            "\1\u0089\37\uffff\1\u0089",
            "",
            "",
            "",
            "",
            "",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
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
            "",
            "",
            "",
            "",
            "",
            "\1\140\1\uffff\12\141",
            "",
            "",
            "\1\u008b\37\uffff\1\u008b",
            "",
            "",
            "\1\u008c\37\uffff\1\u008c",
            "\1\u008d\37\uffff\1\u008d",
            "\1\u008e\37\uffff\1\u008e",
            "\1\u008f\37\uffff\1\u008f",
            "\1\u0090\37\uffff\1\u0090",
            "\1\u0091\37\uffff\1\u0091",
            "\1\u0092\37\uffff\1\u0092",
            "\1\u0093\37\uffff\1\u0093",
            "\1\u0094\37\uffff\1\u0094",
            "\1\u0095\37\uffff\1\u0095",
            "",
            "\1\u0096\37\uffff\1\u0096",
            "\1\u0097\37\uffff\1\u0097",
            "\1\u0098\37\uffff\1\u0098",
            "\1\u0099\37\uffff\1\u0099",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "",
            "\1\u009b\37\uffff\1\u009b",
            "\1\u009c\37\uffff\1\u009c",
            "\1\u009d\37\uffff\1\u009d",
            "",
            "\1\u009e\37\uffff\1\u009e",
            "\1\u009f\37\uffff\1\u009f",
            "\1\u00a0\37\uffff\1\u00a0",
            "\1\u00a1\37\uffff\1\u00a1",
            "\1\u00a2\37\uffff\1\u00a2",
            "\1\u00a3\37\uffff\1\u00a3",
            "\1\u00a4\37\uffff\1\u00a4",
            "\1\u00a5\37\uffff\1\u00a5",
            "\1\u00a6\37\uffff\1\u00a6",
            "\1\u00a7\37\uffff\1\u00a7",
            "\1\u00a8\37\uffff\1\u00a8",
            "\1\u00a9\37\uffff\1\u00a9",
            "\1\u00aa\37\uffff\1\u00aa",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "",
            "\1\u00ac\37\uffff\1\u00ac",
            "\1\u00ad\37\uffff\1\u00ad",
            "\1\u00ae\37\uffff\1\u00ae",
            "\1\u00af\37\uffff\1\u00af",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\u00b1\37\uffff\1\u00b1",
            "\1\u00b2\37\uffff\1\u00b2",
            "\1\u00b3\37\uffff\1\u00b3",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\u00b5\37\uffff\1\u00b5",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\u00b7\37\uffff\1\u00b7",
            "\1\u00b8\37\uffff\1\u00b8",
            "\1\u00b9\37\uffff\1\u00b9",
            "\1\u00ba\37\uffff\1\u00ba",
            "",
            "\1\u00bb\37\uffff\1\u00bb",
            "\1\u00bc\37\uffff\1\u00bc",
            "\1\u00bd\37\uffff\1\u00bd",
            "\1\u00be\37\uffff\1\u00be",
            "\1\u00bf\37\uffff\1\u00bf",
            "\1\u00c0\37\uffff\1\u00c0",
            "\1\u00c1\37\uffff\1\u00c1",
            "\1\u00c2\37\uffff\1\u00c2",
            "\1\u00c3\37\uffff\1\u00c3",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\u00c6\37\uffff\1\u00c6",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\u00c9\37\uffff\1\u00c9",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "",
            "\1\u00cb\37\uffff\1\u00cb",
            "\1\u00cc\37\uffff\1\u00cc",
            "\1\u00cd\37\uffff\1\u00cd",
            "\1\u00ce\37\uffff\1\u00ce",
            "",
            "\1\u00cf\37\uffff\1\u00cf",
            "\1\u00d0\37\uffff\1\u00d0",
            "\1\u00d1\37\uffff\1\u00d1",
            "",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "",
            "\1\u00d3\37\uffff\1\u00d3",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\u00d5\37\uffff\1\u00d5",
            "\1\u00d6\37\uffff\1\u00d6",
            "\1\u00d7\37\uffff\1\u00d7",
            "\1\u00d8\37\uffff\1\u00d8",
            "\1\u00d9\37\uffff\1\u00d9",
            "\1\u00da\37\uffff\1\u00da",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\u00dc\37\uffff\1\u00dc",
            "\1\u00dd\37\uffff\1\u00dd",
            "\1\u00de\37\uffff\1\u00de",
            "\1\u00df\37\uffff\1\u00df",
            "",
            "",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "",
            "",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "",
            "\1\u00e2\37\uffff\1\u00e2",
            "\1\u00e3\37\uffff\1\u00e3",
            "\1\u00e4\37\uffff\1\u00e4",
            "\1\u00e5\37\uffff\1\u00e5",
            "\1\u00e6\37\uffff\1\u00e6",
            "\1\u00e7\37\uffff\1\u00e7",
            "\1\u00e8\37\uffff\1\u00e8",
            "",
            "\1\u00e9\37\uffff\1\u00e9",
            "",
            "\1\u00ea\37\uffff\1\u00ea",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\u00ec\37\uffff\1\u00ec",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\u00ee\37\uffff\1\u00ee",
            "\1\u00ef\37\uffff\1\u00ef",
            "",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "",
            "",
            "\1\u00f4\37\uffff\1\u00f4",
            "\1\u00f5\37\uffff\1\u00f5",
            "\1\u00f6\37\uffff\1\u00f6",
            "\1\u00f7\37\uffff\1\u00f7",
            "\1\u00f8\37\uffff\1\u00f8",
            "\1\u00f9\37\uffff\1\u00f9",
            "\1\u00fa\37\uffff\1\u00fa",
            "\1\u00fb\37\uffff\1\u00fb",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "",
            "",
            "",
            "",
            "\1\u0100\37\uffff\1\u0100",
            "\1\u0101\37\uffff\1\u0101",
            "\1\u0102\37\uffff\1\u0102",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\u0104\37\uffff\1\u0104",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\u0106\37\uffff\1\u0106",
            "\1\u0107\37\uffff\1\u0107",
            "",
            "",
            "",
            "",
            "\1\u0108\37\uffff\1\u0108",
            "\1\u0109\37\uffff\1\u0109",
            "\1\u010a\37\uffff\1\u010a",
            "",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "\1\u010e\37\uffff\1\u010e",
            "\1\u010f\37\uffff\1\u010f",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "",
            "",
            "",
            "\1\u0111\37\uffff\1\u0111",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "",
            "\1\52\13\uffff\13\52\6\uffff\32\52\4\uffff\1\52\1\uffff\33\52\1\uffff\1\52",
            "",
            ""
    };

    static final short[] DFA11_eot = DFA.unpackEncodedString(DFA11_eotS);
    static final short[] DFA11_eof = DFA.unpackEncodedString(DFA11_eofS);
    static final char[] DFA11_min = DFA.unpackEncodedStringToUnsignedChars(DFA11_minS);
    static final char[] DFA11_max = DFA.unpackEncodedStringToUnsignedChars(DFA11_maxS);
    static final short[] DFA11_accept = DFA.unpackEncodedString(DFA11_acceptS);
    static final short[] DFA11_special = DFA.unpackEncodedString(DFA11_specialS);
    static final short[][] DFA11_transition;

    static {
        int numStates = DFA11_transitionS.length;
        DFA11_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA11_transition[i] = DFA.unpackEncodedString(DFA11_transitionS[i]);
        }
    }

    class DFA11 extends DFA {

        public DFA11(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 11;
            this.eot = DFA11_eot;
            this.eof = DFA11_eof;
            this.min = DFA11_min;
            this.max = DFA11_max;
            this.accept = DFA11_accept;
            this.special = DFA11_special;
            this.transition = DFA11_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( INTERSECTION | DATAHANDLER | DIFFERENCE | PARTITION | TRANSPORT | UNBOUNDED | DISTINCT | PROTOCOL | ADVANCE | CHANNEL | OPTIONS | WRAPPER | ATTACH | CREATE | EXISTS | HAVING | SELECT | STREAM | FALSE | GROUP | TUPLE | UNION | WHERE | DROP | FILE | FROM | SINK | SIZE | TIME | TRUE | VIEW | AND | NOT | ExclamationMarkEqualsSign | LessThanSignEqualsSign | GreaterThanSignEqualsSign | AS | BY | IF | IN | OR | TO | LeftParenthesis | RightParenthesis | Asterisk | PlusSign | Comma | HyphenMinus | FullStop | Solidus | Colon | Semicolon | LessThanSign | EqualsSign | GreaterThanSign | LeftSquareBracket | RightSquareBracket | RULE_ID | RULE_FLOAT | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA11_0 = input.LA(1);

                        s = -1;
                        if ( (LA11_0=='I'||LA11_0=='i') ) {s = 1;}

                        else if ( (LA11_0=='D'||LA11_0=='d') ) {s = 2;}

                        else if ( (LA11_0=='P'||LA11_0=='p') ) {s = 3;}

                        else if ( (LA11_0=='T'||LA11_0=='t') ) {s = 4;}

                        else if ( (LA11_0=='U'||LA11_0=='u') ) {s = 5;}

                        else if ( (LA11_0=='A'||LA11_0=='a') ) {s = 6;}

                        else if ( (LA11_0=='C'||LA11_0=='c') ) {s = 7;}

                        else if ( (LA11_0=='O'||LA11_0=='o') ) {s = 8;}

                        else if ( (LA11_0=='W'||LA11_0=='w') ) {s = 9;}

                        else if ( (LA11_0=='E'||LA11_0=='e') ) {s = 10;}

                        else if ( (LA11_0=='H'||LA11_0=='h') ) {s = 11;}

                        else if ( (LA11_0=='S'||LA11_0=='s') ) {s = 12;}

                        else if ( (LA11_0=='F'||LA11_0=='f') ) {s = 13;}

                        else if ( (LA11_0=='G'||LA11_0=='g') ) {s = 14;}

                        else if ( (LA11_0=='V'||LA11_0=='v') ) {s = 15;}

                        else if ( (LA11_0=='N'||LA11_0=='n') ) {s = 16;}

                        else if ( (LA11_0=='!') ) {s = 17;}

                        else if ( (LA11_0=='<') ) {s = 18;}

                        else if ( (LA11_0=='>') ) {s = 19;}

                        else if ( (LA11_0=='B'||LA11_0=='b') ) {s = 20;}

                        else if ( (LA11_0=='(') ) {s = 21;}

                        else if ( (LA11_0==')') ) {s = 22;}

                        else if ( (LA11_0=='*') ) {s = 23;}

                        else if ( (LA11_0=='+') ) {s = 24;}

                        else if ( (LA11_0==',') ) {s = 25;}

                        else if ( (LA11_0=='-') ) {s = 26;}

                        else if ( (LA11_0=='.') ) {s = 27;}

                        else if ( (LA11_0=='/') ) {s = 28;}

                        else if ( (LA11_0==':') ) {s = 29;}

                        else if ( (LA11_0==';') ) {s = 30;}

                        else if ( (LA11_0=='=') ) {s = 31;}

                        else if ( (LA11_0=='[') ) {s = 32;}

                        else if ( (LA11_0==']') ) {s = 33;}

                        else if ( (LA11_0=='$'||(LA11_0>='J' && LA11_0<='M')||(LA11_0>='Q' && LA11_0<='R')||(LA11_0>='X' && LA11_0<='Z')||LA11_0=='_'||(LA11_0>='j' && LA11_0<='m')||(LA11_0>='q' && LA11_0<='r')||(LA11_0>='x' && LA11_0<='{')||LA11_0=='}') ) {s = 34;}

                        else if ( ((LA11_0>='0' && LA11_0<='9')) ) {s = 35;}

                        else if ( (LA11_0=='\"') ) {s = 36;}

                        else if ( (LA11_0=='\'') ) {s = 37;}

                        else if ( ((LA11_0>='\t' && LA11_0<='\n')||LA11_0=='\r'||LA11_0==' ') ) {s = 38;}

                        else if ( ((LA11_0>='\u0000' && LA11_0<='\b')||(LA11_0>='\u000B' && LA11_0<='\f')||(LA11_0>='\u000E' && LA11_0<='\u001F')||LA11_0=='#'||(LA11_0>='%' && LA11_0<='&')||(LA11_0>='?' && LA11_0<='@')||LA11_0=='\\'||LA11_0=='^'||LA11_0=='`'||LA11_0=='|'||(LA11_0>='~' && LA11_0<='\uFFFF')) ) {s = 39;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA11_37 = input.LA(1);

                        s = -1;
                        if ( ((LA11_37>='\u0000' && LA11_37<='\uFFFF')) ) {s = 98;}

                        else s = 39;

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA11_36 = input.LA(1);

                        s = -1;
                        if ( ((LA11_36>='\u0000' && LA11_36<='\uFFFF')) ) {s = 98;}

                        else s = 39;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 11, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}