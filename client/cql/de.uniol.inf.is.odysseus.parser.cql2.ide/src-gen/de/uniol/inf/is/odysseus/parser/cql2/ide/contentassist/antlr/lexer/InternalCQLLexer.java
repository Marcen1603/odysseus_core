package de.uniol.inf.is.odysseus.parser.cql2.ide.contentassist.antlr.lexer;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalCQLLexer extends Lexer {
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

    public InternalCQLLexer() {;} 
    public InternalCQLLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InternalCQLLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "InternalCQLLexer.g"; }

    // $ANTLR start "NO_LAZY_CONNECTION_CHECK"
    public final void mNO_LAZY_CONNECTION_CHECK() throws RecognitionException {
        try {
            int _type = NO_LAZY_CONNECTION_CHECK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:14:26: ( ( 'N' | 'n' ) ( 'O' | 'o' ) '_' ( 'L' | 'l' ) ( 'A' | 'a' ) ( 'Z' | 'z' ) ( 'Y' | 'y' ) '_' ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'N' | 'n' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) '_' ( 'C' | 'c' ) ( 'H' | 'h' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'K' | 'k' ) )
            // InternalCQLLexer.g:14:28: ( 'N' | 'n' ) ( 'O' | 'o' ) '_' ( 'L' | 'l' ) ( 'A' | 'a' ) ( 'Z' | 'z' ) ( 'Y' | 'y' ) '_' ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'N' | 'n' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) '_' ( 'C' | 'c' ) ( 'H' | 'h' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'K' | 'k' )
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

            match('_'); 
            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
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

            if ( input.LA(1)=='Z'||input.LA(1)=='z' ) {
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

            match('_'); 
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

            match('_'); 
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
    // $ANTLR end "NO_LAZY_CONNECTION_CHECK"

    // $ANTLR start "INTERSECTION"
    public final void mINTERSECTION() throws RecognitionException {
        try {
            int _type = INTERSECTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:16:14: ( ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) )
            // InternalCQLLexer.g:16:16: ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' )
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

    // $ANTLR start "MILLISECONDS"
    public final void mMILLISECONDS() throws RecognitionException {
        try {
            int _type = MILLISECONDS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:18:14: ( ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'L' | 'l' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'S' | 's' ) )
            // InternalCQLLexer.g:18:16: ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'L' | 'l' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'S' | 's' )
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
    // $ANTLR end "MILLISECONDS"

    // $ANTLR start "DATAHANDLER"
    public final void mDATAHANDLER() throws RecognitionException {
        try {
            int _type = DATAHANDLER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:20:13: ( ( 'D' | 'd' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'A' | 'a' ) ( 'H' | 'h' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'R' | 'r' ) )
            // InternalCQLLexer.g:20:15: ( 'D' | 'd' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'A' | 'a' ) ( 'H' | 'h' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'R' | 'r' )
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

    // $ANTLR start "MILLISECOND"
    public final void mMILLISECOND() throws RecognitionException {
        try {
            int _type = MILLISECOND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:22:13: ( ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'L' | 'l' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) )
            // InternalCQLLexer.g:22:15: ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'L' | 'l' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' )
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MILLISECOND"

    // $ANTLR start "NANOSECONDS"
    public final void mNANOSECONDS() throws RecognitionException {
        try {
            int _type = NANOSECONDS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:24:13: ( ( 'N' | 'n' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'S' | 's' ) )
            // InternalCQLLexer.g:24:15: ( 'N' | 'n' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'S' | 's' )
            {
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
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

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
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
    // $ANTLR end "NANOSECONDS"

    // $ANTLR start "CONNECTION"
    public final void mCONNECTION() throws RecognitionException {
        try {
            int _type = CONNECTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:26:12: ( ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'N' | 'n' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) )
            // InternalCQLLexer.g:26:14: ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'N' | 'n' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' )
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
    // $ANTLR end "CONNECTION"

    // $ANTLR start "DIFFERENCE"
    public final void mDIFFERENCE() throws RecognitionException {
        try {
            int _type = DIFFERENCE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:28:12: ( ( 'D' | 'd' ) ( 'I' | 'i' ) ( 'F' | 'f' ) ( 'F' | 'f' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'N' | 'n' ) ( 'C' | 'c' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:28:14: ( 'D' | 'd' ) ( 'I' | 'i' ) ( 'F' | 'f' ) ( 'F' | 'f' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'N' | 'n' ) ( 'C' | 'c' ) ( 'E' | 'e' )
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

    // $ANTLR start "IDENTIFIED"
    public final void mIDENTIFIED() throws RecognitionException {
        try {
            int _type = IDENTIFIED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:30:12: ( ( 'I' | 'i' ) ( 'D' | 'd' ) ( 'E' | 'e' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'F' | 'f' ) ( 'I' | 'i' ) ( 'E' | 'e' ) ( 'D' | 'd' ) )
            // InternalCQLLexer.g:30:14: ( 'I' | 'i' ) ( 'D' | 'd' ) ( 'E' | 'e' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'F' | 'f' ) ( 'I' | 'i' ) ( 'E' | 'e' ) ( 'D' | 'd' )
            {
            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
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
    // $ANTLR end "IDENTIFIED"

    // $ANTLR start "NANOSECOND"
    public final void mNANOSECOND() throws RecognitionException {
        try {
            int _type = NANOSECOND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:32:12: ( ( 'N' | 'n' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) )
            // InternalCQLLexer.g:32:14: ( 'N' | 'n' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' )
            {
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
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

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NANOSECOND"

    // $ANTLR start "PARTITION"
    public final void mPARTITION() throws RecognitionException {
        try {
            int _type = PARTITION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:34:11: ( ( 'P' | 'p' ) ( 'A' | 'a' ) ( 'R' | 'r' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) )
            // InternalCQLLexer.g:34:13: ( 'P' | 'p' ) ( 'A' | 'a' ) ( 'R' | 'r' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' )
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
            // InternalCQLLexer.g:36:11: ( ( 'T' | 't' ) ( 'R' | 'r' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'S' | 's' ) ( 'P' | 'p' ) ( 'O' | 'o' ) ( 'R' | 'r' ) ( 'T' | 't' ) )
            // InternalCQLLexer.g:36:13: ( 'T' | 't' ) ( 'R' | 'r' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'S' | 's' ) ( 'P' | 'p' ) ( 'O' | 'o' ) ( 'R' | 'r' ) ( 'T' | 't' )
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
            // InternalCQLLexer.g:38:11: ( ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'B' | 'b' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'E' | 'e' ) ( 'D' | 'd' ) )
            // InternalCQLLexer.g:38:13: ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'B' | 'b' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'E' | 'e' ) ( 'D' | 'd' )
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

    // $ANTLR start "DATABASE"
    public final void mDATABASE() throws RecognitionException {
        try {
            int _type = DATABASE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:40:10: ( ( 'D' | 'd' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'A' | 'a' ) ( 'B' | 'b' ) ( 'A' | 'a' ) ( 'S' | 's' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:40:12: ( 'D' | 'd' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'A' | 'a' ) ( 'B' | 'b' ) ( 'A' | 'a' ) ( 'S' | 's' ) ( 'E' | 'e' )
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

            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
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
    // $ANTLR end "DATABASE"

    // $ANTLR start "DISTINCT"
    public final void mDISTINCT() throws RecognitionException {
        try {
            int _type = DISTINCT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:42:10: ( ( 'D' | 'd' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'C' | 'c' ) ( 'T' | 't' ) )
            // InternalCQLLexer.g:42:12: ( 'D' | 'd' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'C' | 'c' ) ( 'T' | 't' )
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

    // $ANTLR start "PASSWORD"
    public final void mPASSWORD() throws RecognitionException {
        try {
            int _type = PASSWORD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:44:10: ( ( 'P' | 'p' ) ( 'A' | 'a' ) ( 'S' | 's' ) ( 'S' | 's' ) ( 'W' | 'w' ) ( 'O' | 'o' ) ( 'R' | 'r' ) ( 'D' | 'd' ) )
            // InternalCQLLexer.g:44:12: ( 'P' | 'p' ) ( 'A' | 'a' ) ( 'S' | 's' ) ( 'S' | 's' ) ( 'W' | 'w' ) ( 'O' | 'o' ) ( 'R' | 'r' ) ( 'D' | 'd' )
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

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
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

            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
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
    // $ANTLR end "PASSWORD"

    // $ANTLR start "PROTOCOL"
    public final void mPROTOCOL() throws RecognitionException {
        try {
            int _type = PROTOCOL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:46:10: ( ( 'P' | 'p' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'T' | 't' ) ( 'O' | 'o' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'L' | 'l' ) )
            // InternalCQLLexer.g:46:12: ( 'P' | 'p' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'T' | 't' ) ( 'O' | 'o' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'L' | 'l' )
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

    // $ANTLR start "TRUNCATE"
    public final void mTRUNCATE() throws RecognitionException {
        try {
            int _type = TRUNCATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:48:10: ( ( 'T' | 't' ) ( 'R' | 'r' ) ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'C' | 'c' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:48:12: ( 'T' | 't' ) ( 'R' | 'r' ) ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'C' | 'c' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'E' | 'e' )
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
    // $ANTLR end "TRUNCATE"

    // $ANTLR start "ADVANCE"
    public final void mADVANCE() throws RecognitionException {
        try {
            int _type = ADVANCE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:50:9: ( ( 'A' | 'a' ) ( 'D' | 'd' ) ( 'V' | 'v' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'C' | 'c' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:50:11: ( 'A' | 'a' ) ( 'D' | 'd' ) ( 'V' | 'v' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'C' | 'c' ) ( 'E' | 'e' )
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
            // InternalCQLLexer.g:52:9: ( ( 'C' | 'c' ) ( 'H' | 'h' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'N' | 'n' ) ( 'E' | 'e' ) ( 'L' | 'l' ) )
            // InternalCQLLexer.g:52:11: ( 'C' | 'c' ) ( 'H' | 'h' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'N' | 'n' ) ( 'E' | 'e' ) ( 'L' | 'l' )
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

    // $ANTLR start "CONTEXT"
    public final void mCONTEXT() throws RecognitionException {
        try {
            int _type = CONTEXT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:54:9: ( ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'X' | 'x' ) ( 'T' | 't' ) )
            // InternalCQLLexer.g:54:11: ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'X' | 'x' ) ( 'T' | 't' )
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CONTEXT"

    // $ANTLR start "MINUTES"
    public final void mMINUTES() throws RecognitionException {
        try {
            int _type = MINUTES;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:56:9: ( ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'U' | 'u' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'S' | 's' ) )
            // InternalCQLLexer.g:56:11: ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'U' | 'u' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'S' | 's' )
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
    // $ANTLR end "MINUTES"

    // $ANTLR start "OPTIONS"
    public final void mOPTIONS() throws RecognitionException {
        try {
            int _type = OPTIONS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:58:9: ( ( 'O' | 'o' ) ( 'P' | 'p' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'S' | 's' ) )
            // InternalCQLLexer.g:58:11: ( 'O' | 'o' ) ( 'P' | 'p' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'S' | 's' )
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

    // $ANTLR start "SECONDS"
    public final void mSECONDS() throws RecognitionException {
        try {
            int _type = SECONDS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:60:9: ( ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'S' | 's' ) )
            // InternalCQLLexer.g:60:11: ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'S' | 's' )
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
    // $ANTLR end "SECONDS"

    // $ANTLR start "WRAPPER"
    public final void mWRAPPER() throws RecognitionException {
        try {
            int _type = WRAPPER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:62:9: ( ( 'W' | 'w' ) ( 'R' | 'r' ) ( 'A' | 'a' ) ( 'P' | 'p' ) ( 'P' | 'p' ) ( 'E' | 'e' ) ( 'R' | 'r' ) )
            // InternalCQLLexer.g:62:11: ( 'W' | 'w' ) ( 'R' | 'r' ) ( 'A' | 'a' ) ( 'P' | 'p' ) ( 'P' | 'p' ) ( 'E' | 'e' ) ( 'R' | 'r' )
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
            // InternalCQLLexer.g:64:8: ( ( 'A' | 'a' ) ( 'T' | 't' ) ( 'T' | 't' ) ( 'A' | 'a' ) ( 'C' | 'c' ) ( 'H' | 'h' ) )
            // InternalCQLLexer.g:64:10: ( 'A' | 'a' ) ( 'T' | 't' ) ( 'T' | 't' ) ( 'A' | 'a' ) ( 'C' | 'c' ) ( 'H' | 'h' )
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
            // InternalCQLLexer.g:66:8: ( ( 'C' | 'c' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:66:10: ( 'C' | 'c' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'E' | 'e' )
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
            // InternalCQLLexer.g:68:8: ( ( 'E' | 'e' ) ( 'X' | 'x' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'T' | 't' ) ( 'S' | 's' ) )
            // InternalCQLLexer.g:68:10: ( 'E' | 'e' ) ( 'X' | 'x' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'T' | 't' ) ( 'S' | 's' )
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
            // InternalCQLLexer.g:70:8: ( ( 'H' | 'h' ) ( 'A' | 'a' ) ( 'V' | 'v' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'G' | 'g' ) )
            // InternalCQLLexer.g:70:10: ( 'H' | 'h' ) ( 'A' | 'a' ) ( 'V' | 'v' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'G' | 'g' )
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

    // $ANTLR start "MINUTE"
    public final void mMINUTE() throws RecognitionException {
        try {
            int _type = MINUTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:72:8: ( ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'U' | 'u' ) ( 'T' | 't' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:72:10: ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'U' | 'u' ) ( 'T' | 't' ) ( 'E' | 'e' )
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MINUTE"

    // $ANTLR start "REVOKE"
    public final void mREVOKE() throws RecognitionException {
        try {
            int _type = REVOKE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:74:8: ( ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'V' | 'v' ) ( 'O' | 'o' ) ( 'K' | 'k' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:74:10: ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'V' | 'v' ) ( 'O' | 'o' ) ( 'K' | 'k' ) ( 'E' | 'e' )
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

            if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
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

            if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
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
    // $ANTLR end "REVOKE"

    // $ANTLR start "SECOND"
    public final void mSECOND() throws RecognitionException {
        try {
            int _type = SECOND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:76:8: ( ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) )
            // InternalCQLLexer.g:76:10: ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' )
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SECOND"

    // $ANTLR start "SELECT"
    public final void mSELECT() throws RecognitionException {
        try {
            int _type = SELECT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:78:8: ( ( 'S' | 's' ) ( 'E' | 'e' ) ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'T' | 't' ) )
            // InternalCQLLexer.g:78:10: ( 'S' | 's' ) ( 'E' | 'e' ) ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'T' | 't' )
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

    // $ANTLR start "SINGLE"
    public final void mSINGLE() throws RecognitionException {
        try {
            int _type = SINGLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:80:8: ( ( 'S' | 's' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'G' | 'g' ) ( 'L' | 'l' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:80:10: ( 'S' | 's' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'G' | 'g' ) ( 'L' | 'l' ) ( 'E' | 'e' )
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

            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
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
    // $ANTLR end "SINGLE"

    // $ANTLR start "STREAM"
    public final void mSTREAM() throws RecognitionException {
        try {
            int _type = STREAM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:82:8: ( ( 'S' | 's' ) ( 'T' | 't' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'M' | 'm' ) )
            // InternalCQLLexer.g:82:10: ( 'S' | 's' ) ( 'T' | 't' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'M' | 'm' )
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

    // $ANTLR start "TENANT"
    public final void mTENANT() throws RecognitionException {
        try {
            int _type = TENANT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:84:8: ( ( 'T' | 't' ) ( 'E' | 'e' ) ( 'N' | 'n' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'T' | 't' ) )
            // InternalCQLLexer.g:84:10: ( 'T' | 't' ) ( 'E' | 'e' ) ( 'N' | 'n' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'T' | 't' )
            {
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

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
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
    // $ANTLR end "TENANT"

    // $ANTLR start "ALTER"
    public final void mALTER() throws RecognitionException {
        try {
            int _type = ALTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:86:7: ( ( 'A' | 'a' ) ( 'L' | 'l' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' ) )
            // InternalCQLLexer.g:86:9: ( 'A' | 'a' ) ( 'L' | 'l' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' )
            {
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ALTER"

    // $ANTLR start "FALSE"
    public final void mFALSE() throws RecognitionException {
        try {
            int _type = FALSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:88:7: ( ( 'F' | 'f' ) ( 'A' | 'a' ) ( 'L' | 'l' ) ( 'S' | 's' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:88:9: ( 'F' | 'f' ) ( 'A' | 'a' ) ( 'L' | 'l' ) ( 'S' | 's' ) ( 'E' | 'e' )
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

    // $ANTLR start "GRANT"
    public final void mGRANT() throws RecognitionException {
        try {
            int _type = GRANT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:90:7: ( ( 'G' | 'g' ) ( 'R' | 'r' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'T' | 't' ) )
            // InternalCQLLexer.g:90:9: ( 'G' | 'g' ) ( 'R' | 'r' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'T' | 't' )
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
    // $ANTLR end "GRANT"

    // $ANTLR start "GROUP"
    public final void mGROUP() throws RecognitionException {
        try {
            int _type = GROUP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:92:7: ( ( 'G' | 'g' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'P' | 'p' ) )
            // InternalCQLLexer.g:92:9: ( 'G' | 'g' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'P' | 'p' )
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

    // $ANTLR start "HOURS"
    public final void mHOURS() throws RecognitionException {
        try {
            int _type = HOURS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:94:7: ( ( 'H' | 'h' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'R' | 'r' ) ( 'S' | 's' ) )
            // InternalCQLLexer.g:94:9: ( 'H' | 'h' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'R' | 'r' ) ( 'S' | 's' )
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
    // $ANTLR end "HOURS"

    // $ANTLR start "MULTI"
    public final void mMULTI() throws RecognitionException {
        try {
            int _type = MULTI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:96:7: ( ( 'M' | 'm' ) ( 'U' | 'u' ) ( 'L' | 'l' ) ( 'T' | 't' ) ( 'I' | 'i' ) )
            // InternalCQLLexer.g:96:9: ( 'M' | 'm' ) ( 'U' | 'u' ) ( 'L' | 'l' ) ( 'T' | 't' ) ( 'I' | 'i' )
            {
            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
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

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MULTI"

    // $ANTLR start "STORE"
    public final void mSTORE() throws RecognitionException {
        try {
            int _type = STORE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:98:7: ( ( 'S' | 's' ) ( 'T' | 't' ) ( 'O' | 'o' ) ( 'R' | 'r' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:98:9: ( 'S' | 's' ) ( 'T' | 't' ) ( 'O' | 'o' ) ( 'R' | 'r' ) ( 'E' | 'e' )
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
    // $ANTLR end "STORE"

    // $ANTLR start "TABLE"
    public final void mTABLE() throws RecognitionException {
        try {
            int _type = TABLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:100:7: ( ( 'T' | 't' ) ( 'A' | 'a' ) ( 'B' | 'b' ) ( 'L' | 'l' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:100:9: ( 'T' | 't' ) ( 'A' | 'a' ) ( 'B' | 'b' ) ( 'L' | 'l' ) ( 'E' | 'e' )
            {
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

            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
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
    // $ANTLR end "TABLE"

    // $ANTLR start "TUPLE"
    public final void mTUPLE() throws RecognitionException {
        try {
            int _type = TUPLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:102:7: ( ( 'T' | 't' ) ( 'U' | 'u' ) ( 'P' | 'p' ) ( 'L' | 'l' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:102:9: ( 'T' | 't' ) ( 'U' | 'u' ) ( 'P' | 'p' ) ( 'L' | 'l' ) ( 'E' | 'e' )
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
            // InternalCQLLexer.g:104:7: ( ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) )
            // InternalCQLLexer.g:104:9: ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' )
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

    // $ANTLR start "WEEKS"
    public final void mWEEKS() throws RecognitionException {
        try {
            int _type = WEEKS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:106:7: ( ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'K' | 'k' ) ( 'S' | 's' ) )
            // InternalCQLLexer.g:106:9: ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'K' | 'k' ) ( 'S' | 's' )
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
    // $ANTLR end "WEEKS"

    // $ANTLR start "WHERE"
    public final void mWHERE() throws RecognitionException {
        try {
            int _type = WHERE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:108:7: ( ( 'W' | 'w' ) ( 'H' | 'h' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:108:9: ( 'W' | 'w' ) ( 'H' | 'h' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'E' | 'e' )
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
            // InternalCQLLexer.g:110:6: ( ( 'D' | 'd' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'P' | 'p' ) )
            // InternalCQLLexer.g:110:8: ( 'D' | 'd' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'P' | 'p' )
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

    // $ANTLR start "EACH"
    public final void mEACH() throws RecognitionException {
        try {
            int _type = EACH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:112:6: ( ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'C' | 'c' ) ( 'H' | 'h' ) )
            // InternalCQLLexer.g:112:8: ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'C' | 'c' ) ( 'H' | 'h' )
            {
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
    // $ANTLR end "EACH"

    // $ANTLR start "FILE"
    public final void mFILE() throws RecognitionException {
        try {
            int _type = FILE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:114:6: ( ( 'F' | 'f' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:114:8: ( 'F' | 'f' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'E' | 'e' )
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
            // InternalCQLLexer.g:116:6: ( ( 'F' | 'f' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'M' | 'm' ) )
            // InternalCQLLexer.g:116:8: ( 'F' | 'f' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'M' | 'm' )
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

    // $ANTLR start "HOUR"
    public final void mHOUR() throws RecognitionException {
        try {
            int _type = HOUR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:118:6: ( ( 'H' | 'h' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'R' | 'r' ) )
            // InternalCQLLexer.g:118:8: ( 'H' | 'h' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'R' | 'r' )
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HOUR"

    // $ANTLR start "JDBC"
    public final void mJDBC() throws RecognitionException {
        try {
            int _type = JDBC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:120:6: ( ( 'J' | 'j' ) ( 'D' | 'd' ) ( 'B' | 'b' ) ( 'C' | 'c' ) )
            // InternalCQLLexer.g:120:8: ( 'J' | 'j' ) ( 'D' | 'd' ) ( 'B' | 'b' ) ( 'C' | 'c' )
            {
            if ( input.LA(1)=='J'||input.LA(1)=='j' ) {
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

            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "JDBC"

    // $ANTLR start "NULL"
    public final void mNULL() throws RecognitionException {
        try {
            int _type = NULL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:122:6: ( ( 'N' | 'n' ) ( 'U' | 'u' ) ( 'L' | 'l' ) ( 'L' | 'l' ) )
            // InternalCQLLexer.g:122:8: ( 'N' | 'n' ) ( 'U' | 'u' ) ( 'L' | 'l' ) ( 'L' | 'l' )
            {
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NULL"

    // $ANTLR start "ROLE"
    public final void mROLE() throws RecognitionException {
        try {
            int _type = ROLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:124:6: ( ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'L' | 'l' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:124:8: ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'L' | 'l' ) ( 'E' | 'e' )
            {
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
    // $ANTLR end "ROLE"

    // $ANTLR start "SINK"
    public final void mSINK() throws RecognitionException {
        try {
            int _type = SINK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:126:6: ( ( 'S' | 's' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'K' | 'k' ) )
            // InternalCQLLexer.g:126:8: ( 'S' | 's' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'K' | 'k' )
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
            // InternalCQLLexer.g:128:6: ( ( 'S' | 's' ) ( 'I' | 'i' ) ( 'Z' | 'z' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:128:8: ( 'S' | 's' ) ( 'I' | 'i' ) ( 'Z' | 'z' ) ( 'E' | 'e' )
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

    // $ANTLR start "SOME"
    public final void mSOME() throws RecognitionException {
        try {
            int _type = SOME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:130:6: ( ( 'S' | 's' ) ( 'O' | 'o' ) ( 'M' | 'm' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:130:8: ( 'S' | 's' ) ( 'O' | 'o' ) ( 'M' | 'm' ) ( 'E' | 'e' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
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
    // $ANTLR end "SOME"

    // $ANTLR start "TIME"
    public final void mTIME() throws RecognitionException {
        try {
            int _type = TIME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:132:6: ( ( 'T' | 't' ) ( 'I' | 'i' ) ( 'M' | 'm' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:132:8: ( 'T' | 't' ) ( 'I' | 'i' ) ( 'M' | 'm' ) ( 'E' | 'e' )
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
            // InternalCQLLexer.g:134:6: ( ( 'T' | 't' ) ( 'R' | 'r' ) ( 'U' | 'u' ) ( 'E' | 'e' ) )
            // InternalCQLLexer.g:134:8: ( 'T' | 't' ) ( 'R' | 'r' ) ( 'U' | 'u' ) ( 'E' | 'e' )
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

    // $ANTLR start "USER"
    public final void mUSER() throws RecognitionException {
        try {
            int _type = USER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:136:6: ( ( 'U' | 'u' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'R' | 'r' ) )
            // InternalCQLLexer.g:136:8: ( 'U' | 'u' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'R' | 'r' )
            {
            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
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
    // $ANTLR end "USER"

    // $ANTLR start "VIEW"
    public final void mVIEW() throws RecognitionException {
        try {
            int _type = VIEW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:138:6: ( ( 'V' | 'v' ) ( 'I' | 'i' ) ( 'E' | 'e' ) ( 'W' | 'w' ) )
            // InternalCQLLexer.g:138:8: ( 'V' | 'v' ) ( 'I' | 'i' ) ( 'E' | 'e' ) ( 'W' | 'w' )
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

    // $ANTLR start "WEEK"
    public final void mWEEK() throws RecognitionException {
        try {
            int _type = WEEK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:140:6: ( ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'K' | 'k' ) )
            // InternalCQLLexer.g:140:8: ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'K' | 'k' )
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WEEK"

    // $ANTLR start "WITH"
    public final void mWITH() throws RecognitionException {
        try {
            int _type = WITH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:142:6: ( ( 'W' | 'w' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'H' | 'h' ) )
            // InternalCQLLexer.g:142:8: ( 'W' | 'w' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'H' | 'h' )
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WITH"

    // $ANTLR start "ALL"
    public final void mALL() throws RecognitionException {
        try {
            int _type = ALL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:144:5: ( ( 'A' | 'a' ) ( 'L' | 'l' ) ( 'L' | 'l' ) )
            // InternalCQLLexer.g:144:7: ( 'A' | 'a' ) ( 'L' | 'l' ) ( 'L' | 'l' )
            {
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
    // $ANTLR end "ALL"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:146:5: ( ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'D' | 'd' ) )
            // InternalCQLLexer.g:146:7: ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'D' | 'd' )
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

    // $ANTLR start "ANY"
    public final void mANY() throws RecognitionException {
        try {
            int _type = ANY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:148:5: ( ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'Y' | 'y' ) )
            // InternalCQLLexer.g:148:7: ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'Y' | 'y' )
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
    // $ANTLR end "ANY"

    // $ANTLR start "NOT"
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:150:5: ( ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'T' | 't' ) )
            // InternalCQLLexer.g:150:7: ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'T' | 't' )
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
            // InternalCQLLexer.g:152:27: ( '!' '=' )
            // InternalCQLLexer.g:152:29: '!' '='
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

    // $ANTLR start "LeftParenthesisRightParenthesis"
    public final void mLeftParenthesisRightParenthesis() throws RecognitionException {
        try {
            int _type = LeftParenthesisRightParenthesis;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:154:33: ( '(' ')' )
            // InternalCQLLexer.g:154:35: '(' ')'
            {
            match('('); 
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LeftParenthesisRightParenthesis"

    // $ANTLR start "LessThanSignEqualsSign"
    public final void mLessThanSignEqualsSign() throws RecognitionException {
        try {
            int _type = LessThanSignEqualsSign;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:156:24: ( '<' '=' )
            // InternalCQLLexer.g:156:26: '<' '='
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
            // InternalCQLLexer.g:158:27: ( '>' '=' )
            // InternalCQLLexer.g:158:29: '>' '='
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
            // InternalCQLLexer.g:160:4: ( ( 'A' | 'a' ) ( 'S' | 's' ) )
            // InternalCQLLexer.g:160:6: ( 'A' | 'a' ) ( 'S' | 's' )
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

    // $ANTLR start "AT"
    public final void mAT() throws RecognitionException {
        try {
            int _type = AT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:162:4: ( ( 'A' | 'a' ) ( 'T' | 't' ) )
            // InternalCQLLexer.g:162:6: ( 'A' | 'a' ) ( 'T' | 't' )
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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AT"

    // $ANTLR start "BY"
    public final void mBY() throws RecognitionException {
        try {
            int _type = BY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:164:4: ( ( 'B' | 'b' ) ( 'Y' | 'y' ) )
            // InternalCQLLexer.g:164:6: ( 'B' | 'b' ) ( 'Y' | 'y' )
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
            // InternalCQLLexer.g:166:4: ( ( 'I' | 'i' ) ( 'F' | 'f' ) )
            // InternalCQLLexer.g:166:6: ( 'I' | 'i' ) ( 'F' | 'f' )
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
            // InternalCQLLexer.g:168:4: ( ( 'I' | 'i' ) ( 'N' | 'n' ) )
            // InternalCQLLexer.g:168:6: ( 'I' | 'i' ) ( 'N' | 'n' )
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

    // $ANTLR start "ON"
    public final void mON() throws RecognitionException {
        try {
            int _type = ON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:170:4: ( ( 'O' | 'o' ) ( 'N' | 'n' ) )
            // InternalCQLLexer.g:170:6: ( 'O' | 'o' ) ( 'N' | 'n' )
            {
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
    // $ANTLR end "ON"

    // $ANTLR start "OR"
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:172:4: ( ( 'O' | 'o' ) ( 'R' | 'r' ) )
            // InternalCQLLexer.g:172:6: ( 'O' | 'o' ) ( 'R' | 'r' )
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
            // InternalCQLLexer.g:174:4: ( ( 'T' | 't' ) ( 'O' | 'o' ) )
            // InternalCQLLexer.g:174:6: ( 'T' | 't' ) ( 'O' | 'o' )
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

    // $ANTLR start "DollarSign"
    public final void mDollarSign() throws RecognitionException {
        try {
            int _type = DollarSign;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:176:12: ( '$' )
            // InternalCQLLexer.g:176:14: '$'
            {
            match('$'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DollarSign"

    // $ANTLR start "LeftParenthesis"
    public final void mLeftParenthesis() throws RecognitionException {
        try {
            int _type = LeftParenthesis;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:178:17: ( '(' )
            // InternalCQLLexer.g:178:19: '('
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
            // InternalCQLLexer.g:180:18: ( ')' )
            // InternalCQLLexer.g:180:20: ')'
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
            // InternalCQLLexer.g:182:10: ( '*' )
            // InternalCQLLexer.g:182:12: '*'
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
            // InternalCQLLexer.g:184:10: ( '+' )
            // InternalCQLLexer.g:184:12: '+'
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
            // InternalCQLLexer.g:186:7: ( ',' )
            // InternalCQLLexer.g:186:9: ','
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
            // InternalCQLLexer.g:188:13: ( '-' )
            // InternalCQLLexer.g:188:15: '-'
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
            // InternalCQLLexer.g:190:10: ( '.' )
            // InternalCQLLexer.g:190:12: '.'
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
            // InternalCQLLexer.g:192:9: ( '/' )
            // InternalCQLLexer.g:192:11: '/'
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
            // InternalCQLLexer.g:194:7: ( ':' )
            // InternalCQLLexer.g:194:9: ':'
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
            // InternalCQLLexer.g:196:11: ( ';' )
            // InternalCQLLexer.g:196:13: ';'
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
            // InternalCQLLexer.g:198:14: ( '<' )
            // InternalCQLLexer.g:198:16: '<'
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
            // InternalCQLLexer.g:200:12: ( '=' )
            // InternalCQLLexer.g:200:14: '='
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
            // InternalCQLLexer.g:202:17: ( '>' )
            // InternalCQLLexer.g:202:19: '>'
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
            // InternalCQLLexer.g:204:19: ( '[' )
            // InternalCQLLexer.g:204:21: '['
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
            // InternalCQLLexer.g:206:20: ( ']' )
            // InternalCQLLexer.g:206:22: ']'
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

    // $ANTLR start "CircumflexAccent"
    public final void mCircumflexAccent() throws RecognitionException {
        try {
            int _type = CircumflexAccent;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:208:18: ( '^' )
            // InternalCQLLexer.g:208:20: '^'
            {
            match('^'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CircumflexAccent"

    // $ANTLR start "LeftCurlyBracket"
    public final void mLeftCurlyBracket() throws RecognitionException {
        try {
            int _type = LeftCurlyBracket;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:210:18: ( '{' )
            // InternalCQLLexer.g:210:20: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LeftCurlyBracket"

    // $ANTLR start "RightCurlyBracket"
    public final void mRightCurlyBracket() throws RecognitionException {
        try {
            int _type = RightCurlyBracket;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:212:19: ( '}' )
            // InternalCQLLexer.g:212:21: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RightCurlyBracket"

    // $ANTLR start "RULE_ID"
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:214:9: ( ( RULE_LETTER | RULE_SPECIAL_CHARS ) ( RULE_LETTER | RULE_SPECIAL_CHARS | RULE_INT )* )
            // InternalCQLLexer.g:214:11: ( RULE_LETTER | RULE_SPECIAL_CHARS ) ( RULE_LETTER | RULE_SPECIAL_CHARS | RULE_INT )*
            {
            if ( input.LA(1)=='$'||input.LA(1)==':'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='{')||input.LA(1)=='}' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // InternalCQLLexer.g:214:44: ( RULE_LETTER | RULE_SPECIAL_CHARS | RULE_INT )*
            loop1:
            do {
                int alt1=4;
                switch ( input.LA(1) ) {
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                    {
                    alt1=1;
                    }
                    break;
                case '$':
                case ':':
                case '_':
                case '{':
                case '}':
                    {
                    alt1=2;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    {
                    alt1=3;
                    }
                    break;

                }

                switch (alt1) {
            	case 1 :
            	    // InternalCQLLexer.g:214:45: RULE_LETTER
            	    {
            	    mRULE_LETTER(); 

            	    }
            	    break;
            	case 2 :
            	    // InternalCQLLexer.g:214:57: RULE_SPECIAL_CHARS
            	    {
            	    mRULE_SPECIAL_CHARS(); 

            	    }
            	    break;
            	case 3 :
            	    // InternalCQLLexer.g:214:76: RULE_INT
            	    {
            	    mRULE_INT(); 

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

    // $ANTLR start "RULE_LETTER"
    public final void mRULE_LETTER() throws RecognitionException {
        try {
            // InternalCQLLexer.g:216:22: ( ( 'a' .. 'z' | 'A' .. 'Z' ) )
            // InternalCQLLexer.g:216:24: ( 'a' .. 'z' | 'A' .. 'Z' )
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
    // $ANTLR end "RULE_LETTER"

    // $ANTLR start "RULE_SPECIAL_CHARS"
    public final void mRULE_SPECIAL_CHARS() throws RecognitionException {
        try {
            // InternalCQLLexer.g:218:29: ( ( ':' | '_' | '{' | '}' | '$' ) )
            // InternalCQLLexer.g:218:31: ( ':' | '_' | '{' | '}' | '$' )
            {
            if ( input.LA(1)=='$'||input.LA(1)==':'||input.LA(1)=='_'||input.LA(1)=='{'||input.LA(1)=='}' ) {
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
    // $ANTLR end "RULE_SPECIAL_CHARS"

    // $ANTLR start "RULE_FLOAT"
    public final void mRULE_FLOAT() throws RecognitionException {
        try {
            int _type = RULE_FLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:220:12: ( RULE_INT '.' RULE_INT )
            // InternalCQLLexer.g:220:14: RULE_INT '.' RULE_INT
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

    // $ANTLR start "RULE_BIT"
    public final void mRULE_BIT() throws RecognitionException {
        try {
            // InternalCQLLexer.g:222:19: ( ( '0' | '1' ) )
            // InternalCQLLexer.g:222:21: ( '0' | '1' )
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='1') ) {
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
    // $ANTLR end "RULE_BIT"

    // $ANTLR start "RULE_BYTE"
    public final void mRULE_BYTE() throws RecognitionException {
        try {
            int _type = RULE_BYTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:224:11: ( RULE_BIT RULE_BIT RULE_BIT RULE_BIT RULE_BIT RULE_BIT RULE_BIT RULE_BIT )
            // InternalCQLLexer.g:224:13: RULE_BIT RULE_BIT RULE_BIT RULE_BIT RULE_BIT RULE_BIT RULE_BIT RULE_BIT
            {
            mRULE_BIT(); 
            mRULE_BIT(); 
            mRULE_BIT(); 
            mRULE_BIT(); 
            mRULE_BIT(); 
            mRULE_BIT(); 
            mRULE_BIT(); 
            mRULE_BIT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_BYTE"

    // $ANTLR start "RULE_VECTOR_FLOAT"
    public final void mRULE_VECTOR_FLOAT() throws RecognitionException {
        try {
            int _type = RULE_VECTOR_FLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:226:19: ( '[' ( RULE_FLOAT )+ ( ',' RULE_FLOAT )* ']' )
            // InternalCQLLexer.g:226:21: '[' ( RULE_FLOAT )+ ( ',' RULE_FLOAT )* ']'
            {
            match('['); 
            // InternalCQLLexer.g:226:25: ( RULE_FLOAT )+
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
            	    // InternalCQLLexer.g:226:25: RULE_FLOAT
            	    {
            	    mRULE_FLOAT(); 

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

            // InternalCQLLexer.g:226:37: ( ',' RULE_FLOAT )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==',') ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalCQLLexer.g:226:38: ',' RULE_FLOAT
            	    {
            	    match(','); 
            	    mRULE_FLOAT(); 

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_VECTOR_FLOAT"

    // $ANTLR start "RULE_MATRIX_FLOAT"
    public final void mRULE_MATRIX_FLOAT() throws RecognitionException {
        try {
            int _type = RULE_MATRIX_FLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:228:19: ( '[' ( RULE_FLOAT )+ ( ',' RULE_FLOAT )* ( ';' ( RULE_FLOAT )+ ( ',' RULE_FLOAT )* )* ']' )
            // InternalCQLLexer.g:228:21: '[' ( RULE_FLOAT )+ ( ',' RULE_FLOAT )* ( ';' ( RULE_FLOAT )+ ( ',' RULE_FLOAT )* )* ']'
            {
            match('['); 
            // InternalCQLLexer.g:228:25: ( RULE_FLOAT )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalCQLLexer.g:228:25: RULE_FLOAT
            	    {
            	    mRULE_FLOAT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);

            // InternalCQLLexer.g:228:37: ( ',' RULE_FLOAT )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==',') ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // InternalCQLLexer.g:228:38: ',' RULE_FLOAT
            	    {
            	    match(','); 
            	    mRULE_FLOAT(); 

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            // InternalCQLLexer.g:228:55: ( ';' ( RULE_FLOAT )+ ( ',' RULE_FLOAT )* )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==';') ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // InternalCQLLexer.g:228:56: ';' ( RULE_FLOAT )+ ( ',' RULE_FLOAT )*
            	    {
            	    match(';'); 
            	    // InternalCQLLexer.g:228:60: ( RULE_FLOAT )+
            	    int cnt6=0;
            	    loop6:
            	    do {
            	        int alt6=2;
            	        int LA6_0 = input.LA(1);

            	        if ( ((LA6_0>='0' && LA6_0<='9')) ) {
            	            alt6=1;
            	        }


            	        switch (alt6) {
            	    	case 1 :
            	    	    // InternalCQLLexer.g:228:60: RULE_FLOAT
            	    	    {
            	    	    mRULE_FLOAT(); 

            	    	    }
            	    	    break;

            	    	default :
            	    	    if ( cnt6 >= 1 ) break loop6;
            	                EarlyExitException eee =
            	                    new EarlyExitException(6, input);
            	                throw eee;
            	        }
            	        cnt6++;
            	    } while (true);

            	    // InternalCQLLexer.g:228:72: ( ',' RULE_FLOAT )*
            	    loop7:
            	    do {
            	        int alt7=2;
            	        int LA7_0 = input.LA(1);

            	        if ( (LA7_0==',') ) {
            	            alt7=1;
            	        }


            	        switch (alt7) {
            	    	case 1 :
            	    	    // InternalCQLLexer.g:228:73: ',' RULE_FLOAT
            	    	    {
            	    	    match(','); 
            	    	    mRULE_FLOAT(); 

            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop7;
            	        }
            	    } while (true);


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_MATRIX_FLOAT"

    // $ANTLR start "RULE_PATH"
    public final void mRULE_PATH() throws RecognitionException {
        try {
            int _type = RULE_PATH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:230:11: ( ( '\\'' '\\' \\\\ \\'' . ( '\\' \\\\ \\'' . )* '\\'' | '\"' '\\' \\\\ \\'' . ( '\\' \\\\ \\'' . )* '\"' ) )
            // InternalCQLLexer.g:230:13: ( '\\'' '\\' \\\\ \\'' . ( '\\' \\\\ \\'' . )* '\\'' | '\"' '\\' \\\\ \\'' . ( '\\' \\\\ \\'' . )* '\"' )
            {
            // InternalCQLLexer.g:230:13: ( '\\'' '\\' \\\\ \\'' . ( '\\' \\\\ \\'' . )* '\\'' | '\"' '\\' \\\\ \\'' . ( '\\' \\\\ \\'' . )* '\"' )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0=='\'') ) {
                alt11=1;
            }
            else if ( (LA11_0=='\"') ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // InternalCQLLexer.g:230:14: '\\'' '\\' \\\\ \\'' . ( '\\' \\\\ \\'' . )* '\\''
                    {
                    match('\''); 
                    match("' \\ '"); 

                    matchAny(); 
                    // InternalCQLLexer.g:230:32: ( '\\' \\\\ \\'' . )*
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( (LA9_0=='\'') ) {
                            int LA9_1 = input.LA(2);

                            if ( (LA9_1==' ') ) {
                                alt9=1;
                            }


                        }


                        switch (alt9) {
                    	case 1 :
                    	    // InternalCQLLexer.g:230:33: '\\' \\\\ \\'' .
                    	    {
                    	    match("' \\ '"); 

                    	    matchAny(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop9;
                        }
                    } while (true);

                    match('\''); 

                    }
                    break;
                case 2 :
                    // InternalCQLLexer.g:230:53: '\"' '\\' \\\\ \\'' . ( '\\' \\\\ \\'' . )* '\"'
                    {
                    match('\"'); 
                    match("' \\ '"); 

                    matchAny(); 
                    // InternalCQLLexer.g:230:70: ( '\\' \\\\ \\'' . )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( (LA10_0=='\'') ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // InternalCQLLexer.g:230:71: '\\' \\\\ \\'' .
                    	    {
                    	    match("' \\ '"); 

                    	    matchAny(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);

                    match('\"'); 

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
    // $ANTLR end "RULE_PATH"

    // $ANTLR start "RULE_INT"
    public final void mRULE_INT() throws RecognitionException {
        try {
            int _type = RULE_INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQLLexer.g:232:10: ( ( '0' .. '9' )+ )
            // InternalCQLLexer.g:232:12: ( '0' .. '9' )+
            {
            // InternalCQLLexer.g:232:12: ( '0' .. '9' )+
            int cnt12=0;
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>='0' && LA12_0<='9')) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // InternalCQLLexer.g:232:13: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt12 >= 1 ) break loop12;
                        EarlyExitException eee =
                            new EarlyExitException(12, input);
                        throw eee;
                }
                cnt12++;
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
            // InternalCQLLexer.g:234:13: ( ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' ) )
            // InternalCQLLexer.g:234:15: ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            {
            // InternalCQLLexer.g:234:15: ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0=='\"') ) {
                alt15=1;
            }
            else if ( (LA15_0=='\'') ) {
                alt15=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }
            switch (alt15) {
                case 1 :
                    // InternalCQLLexer.g:234:16: '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"'
                    {
                    match('\"'); 
                    // InternalCQLLexer.g:234:20: ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )*
                    loop13:
                    do {
                        int alt13=3;
                        int LA13_0 = input.LA(1);

                        if ( (LA13_0=='\\') ) {
                            alt13=1;
                        }
                        else if ( ((LA13_0>='\u0000' && LA13_0<='!')||(LA13_0>='#' && LA13_0<='[')||(LA13_0>=']' && LA13_0<='\uFFFF')) ) {
                            alt13=2;
                        }


                        switch (alt13) {
                    	case 1 :
                    	    // InternalCQLLexer.g:234:21: '\\\\' .
                    	    {
                    	    match('\\'); 
                    	    matchAny(); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalCQLLexer.g:234:28: ~ ( ( '\\\\' | '\"' ) )
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
                    	    break loop13;
                        }
                    } while (true);

                    match('\"'); 

                    }
                    break;
                case 2 :
                    // InternalCQLLexer.g:234:48: '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\''
                    {
                    match('\''); 
                    // InternalCQLLexer.g:234:53: ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )*
                    loop14:
                    do {
                        int alt14=3;
                        int LA14_0 = input.LA(1);

                        if ( (LA14_0=='\\') ) {
                            alt14=1;
                        }
                        else if ( ((LA14_0>='\u0000' && LA14_0<='&')||(LA14_0>='(' && LA14_0<='[')||(LA14_0>=']' && LA14_0<='\uFFFF')) ) {
                            alt14=2;
                        }


                        switch (alt14) {
                    	case 1 :
                    	    // InternalCQLLexer.g:234:54: '\\\\' .
                    	    {
                    	    match('\\'); 
                    	    matchAny(); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalCQLLexer.g:234:61: ~ ( ( '\\\\' | '\\'' ) )
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
                    	    break loop14;
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
            // InternalCQLLexer.g:236:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // InternalCQLLexer.g:236:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // InternalCQLLexer.g:236:24: ( options {greedy=false; } : . )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0=='*') ) {
                    int LA16_1 = input.LA(2);

                    if ( (LA16_1=='/') ) {
                        alt16=2;
                    }
                    else if ( ((LA16_1>='\u0000' && LA16_1<='.')||(LA16_1>='0' && LA16_1<='\uFFFF')) ) {
                        alt16=1;
                    }


                }
                else if ( ((LA16_0>='\u0000' && LA16_0<=')')||(LA16_0>='+' && LA16_0<='\uFFFF')) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // InternalCQLLexer.g:236:52: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop16;
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
            // InternalCQLLexer.g:238:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // InternalCQLLexer.g:238:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // InternalCQLLexer.g:238:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( ((LA17_0>='\u0000' && LA17_0<='\t')||(LA17_0>='\u000B' && LA17_0<='\f')||(LA17_0>='\u000E' && LA17_0<='\uFFFF')) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // InternalCQLLexer.g:238:24: ~ ( ( '\\n' | '\\r' ) )
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
            	    break loop17;
                }
            } while (true);

            // InternalCQLLexer.g:238:40: ( ( '\\r' )? '\\n' )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0=='\n'||LA19_0=='\r') ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // InternalCQLLexer.g:238:41: ( '\\r' )? '\\n'
                    {
                    // InternalCQLLexer.g:238:41: ( '\\r' )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0=='\r') ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // InternalCQLLexer.g:238:41: '\\r'
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
            // InternalCQLLexer.g:240:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // InternalCQLLexer.g:240:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // InternalCQLLexer.g:240:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt20=0;
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( ((LA20_0>='\t' && LA20_0<='\n')||LA20_0=='\r'||LA20_0==' ') ) {
                    alt20=1;
                }


                switch (alt20) {
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
            	    if ( cnt20 >= 1 ) break loop20;
                        EarlyExitException eee =
                            new EarlyExitException(20, input);
                        throw eee;
                }
                cnt20++;
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
            // InternalCQLLexer.g:242:16: ( . )
            // InternalCQLLexer.g:242:18: .
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
        // InternalCQLLexer.g:1:8: ( NO_LAZY_CONNECTION_CHECK | INTERSECTION | MILLISECONDS | DATAHANDLER | MILLISECOND | NANOSECONDS | CONNECTION | DIFFERENCE | IDENTIFIED | NANOSECOND | PARTITION | TRANSPORT | UNBOUNDED | DATABASE | DISTINCT | PASSWORD | PROTOCOL | TRUNCATE | ADVANCE | CHANNEL | CONTEXT | MINUTES | OPTIONS | SECONDS | WRAPPER | ATTACH | CREATE | EXISTS | HAVING | MINUTE | REVOKE | SECOND | SELECT | SINGLE | STREAM | TENANT | ALTER | FALSE | GRANT | GROUP | HOURS | MULTI | STORE | TABLE | TUPLE | UNION | WEEKS | WHERE | DROP | EACH | FILE | FROM | HOUR | JDBC | NULL | ROLE | SINK | SIZE | SOME | TIME | TRUE | USER | VIEW | WEEK | WITH | ALL | AND | ANY | NOT | ExclamationMarkEqualsSign | LeftParenthesisRightParenthesis | LessThanSignEqualsSign | GreaterThanSignEqualsSign | AS | AT | BY | IF | IN | ON | OR | TO | DollarSign | LeftParenthesis | RightParenthesis | Asterisk | PlusSign | Comma | HyphenMinus | FullStop | Solidus | Colon | Semicolon | LessThanSign | EqualsSign | GreaterThanSign | LeftSquareBracket | RightSquareBracket | CircumflexAccent | LeftCurlyBracket | RightCurlyBracket | RULE_ID | RULE_FLOAT | RULE_BYTE | RULE_VECTOR_FLOAT | RULE_MATRIX_FLOAT | RULE_PATH | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt21=112;
        alt21 = dfa21.predict(input);
        switch (alt21) {
            case 1 :
                // InternalCQLLexer.g:1:10: NO_LAZY_CONNECTION_CHECK
                {
                mNO_LAZY_CONNECTION_CHECK(); 

                }
                break;
            case 2 :
                // InternalCQLLexer.g:1:35: INTERSECTION
                {
                mINTERSECTION(); 

                }
                break;
            case 3 :
                // InternalCQLLexer.g:1:48: MILLISECONDS
                {
                mMILLISECONDS(); 

                }
                break;
            case 4 :
                // InternalCQLLexer.g:1:61: DATAHANDLER
                {
                mDATAHANDLER(); 

                }
                break;
            case 5 :
                // InternalCQLLexer.g:1:73: MILLISECOND
                {
                mMILLISECOND(); 

                }
                break;
            case 6 :
                // InternalCQLLexer.g:1:85: NANOSECONDS
                {
                mNANOSECONDS(); 

                }
                break;
            case 7 :
                // InternalCQLLexer.g:1:97: CONNECTION
                {
                mCONNECTION(); 

                }
                break;
            case 8 :
                // InternalCQLLexer.g:1:108: DIFFERENCE
                {
                mDIFFERENCE(); 

                }
                break;
            case 9 :
                // InternalCQLLexer.g:1:119: IDENTIFIED
                {
                mIDENTIFIED(); 

                }
                break;
            case 10 :
                // InternalCQLLexer.g:1:130: NANOSECOND
                {
                mNANOSECOND(); 

                }
                break;
            case 11 :
                // InternalCQLLexer.g:1:141: PARTITION
                {
                mPARTITION(); 

                }
                break;
            case 12 :
                // InternalCQLLexer.g:1:151: TRANSPORT
                {
                mTRANSPORT(); 

                }
                break;
            case 13 :
                // InternalCQLLexer.g:1:161: UNBOUNDED
                {
                mUNBOUNDED(); 

                }
                break;
            case 14 :
                // InternalCQLLexer.g:1:171: DATABASE
                {
                mDATABASE(); 

                }
                break;
            case 15 :
                // InternalCQLLexer.g:1:180: DISTINCT
                {
                mDISTINCT(); 

                }
                break;
            case 16 :
                // InternalCQLLexer.g:1:189: PASSWORD
                {
                mPASSWORD(); 

                }
                break;
            case 17 :
                // InternalCQLLexer.g:1:198: PROTOCOL
                {
                mPROTOCOL(); 

                }
                break;
            case 18 :
                // InternalCQLLexer.g:1:207: TRUNCATE
                {
                mTRUNCATE(); 

                }
                break;
            case 19 :
                // InternalCQLLexer.g:1:216: ADVANCE
                {
                mADVANCE(); 

                }
                break;
            case 20 :
                // InternalCQLLexer.g:1:224: CHANNEL
                {
                mCHANNEL(); 

                }
                break;
            case 21 :
                // InternalCQLLexer.g:1:232: CONTEXT
                {
                mCONTEXT(); 

                }
                break;
            case 22 :
                // InternalCQLLexer.g:1:240: MINUTES
                {
                mMINUTES(); 

                }
                break;
            case 23 :
                // InternalCQLLexer.g:1:248: OPTIONS
                {
                mOPTIONS(); 

                }
                break;
            case 24 :
                // InternalCQLLexer.g:1:256: SECONDS
                {
                mSECONDS(); 

                }
                break;
            case 25 :
                // InternalCQLLexer.g:1:264: WRAPPER
                {
                mWRAPPER(); 

                }
                break;
            case 26 :
                // InternalCQLLexer.g:1:272: ATTACH
                {
                mATTACH(); 

                }
                break;
            case 27 :
                // InternalCQLLexer.g:1:279: CREATE
                {
                mCREATE(); 

                }
                break;
            case 28 :
                // InternalCQLLexer.g:1:286: EXISTS
                {
                mEXISTS(); 

                }
                break;
            case 29 :
                // InternalCQLLexer.g:1:293: HAVING
                {
                mHAVING(); 

                }
                break;
            case 30 :
                // InternalCQLLexer.g:1:300: MINUTE
                {
                mMINUTE(); 

                }
                break;
            case 31 :
                // InternalCQLLexer.g:1:307: REVOKE
                {
                mREVOKE(); 

                }
                break;
            case 32 :
                // InternalCQLLexer.g:1:314: SECOND
                {
                mSECOND(); 

                }
                break;
            case 33 :
                // InternalCQLLexer.g:1:321: SELECT
                {
                mSELECT(); 

                }
                break;
            case 34 :
                // InternalCQLLexer.g:1:328: SINGLE
                {
                mSINGLE(); 

                }
                break;
            case 35 :
                // InternalCQLLexer.g:1:335: STREAM
                {
                mSTREAM(); 

                }
                break;
            case 36 :
                // InternalCQLLexer.g:1:342: TENANT
                {
                mTENANT(); 

                }
                break;
            case 37 :
                // InternalCQLLexer.g:1:349: ALTER
                {
                mALTER(); 

                }
                break;
            case 38 :
                // InternalCQLLexer.g:1:355: FALSE
                {
                mFALSE(); 

                }
                break;
            case 39 :
                // InternalCQLLexer.g:1:361: GRANT
                {
                mGRANT(); 

                }
                break;
            case 40 :
                // InternalCQLLexer.g:1:367: GROUP
                {
                mGROUP(); 

                }
                break;
            case 41 :
                // InternalCQLLexer.g:1:373: HOURS
                {
                mHOURS(); 

                }
                break;
            case 42 :
                // InternalCQLLexer.g:1:379: MULTI
                {
                mMULTI(); 

                }
                break;
            case 43 :
                // InternalCQLLexer.g:1:385: STORE
                {
                mSTORE(); 

                }
                break;
            case 44 :
                // InternalCQLLexer.g:1:391: TABLE
                {
                mTABLE(); 

                }
                break;
            case 45 :
                // InternalCQLLexer.g:1:397: TUPLE
                {
                mTUPLE(); 

                }
                break;
            case 46 :
                // InternalCQLLexer.g:1:403: UNION
                {
                mUNION(); 

                }
                break;
            case 47 :
                // InternalCQLLexer.g:1:409: WEEKS
                {
                mWEEKS(); 

                }
                break;
            case 48 :
                // InternalCQLLexer.g:1:415: WHERE
                {
                mWHERE(); 

                }
                break;
            case 49 :
                // InternalCQLLexer.g:1:421: DROP
                {
                mDROP(); 

                }
                break;
            case 50 :
                // InternalCQLLexer.g:1:426: EACH
                {
                mEACH(); 

                }
                break;
            case 51 :
                // InternalCQLLexer.g:1:431: FILE
                {
                mFILE(); 

                }
                break;
            case 52 :
                // InternalCQLLexer.g:1:436: FROM
                {
                mFROM(); 

                }
                break;
            case 53 :
                // InternalCQLLexer.g:1:441: HOUR
                {
                mHOUR(); 

                }
                break;
            case 54 :
                // InternalCQLLexer.g:1:446: JDBC
                {
                mJDBC(); 

                }
                break;
            case 55 :
                // InternalCQLLexer.g:1:451: NULL
                {
                mNULL(); 

                }
                break;
            case 56 :
                // InternalCQLLexer.g:1:456: ROLE
                {
                mROLE(); 

                }
                break;
            case 57 :
                // InternalCQLLexer.g:1:461: SINK
                {
                mSINK(); 

                }
                break;
            case 58 :
                // InternalCQLLexer.g:1:466: SIZE
                {
                mSIZE(); 

                }
                break;
            case 59 :
                // InternalCQLLexer.g:1:471: SOME
                {
                mSOME(); 

                }
                break;
            case 60 :
                // InternalCQLLexer.g:1:476: TIME
                {
                mTIME(); 

                }
                break;
            case 61 :
                // InternalCQLLexer.g:1:481: TRUE
                {
                mTRUE(); 

                }
                break;
            case 62 :
                // InternalCQLLexer.g:1:486: USER
                {
                mUSER(); 

                }
                break;
            case 63 :
                // InternalCQLLexer.g:1:491: VIEW
                {
                mVIEW(); 

                }
                break;
            case 64 :
                // InternalCQLLexer.g:1:496: WEEK
                {
                mWEEK(); 

                }
                break;
            case 65 :
                // InternalCQLLexer.g:1:501: WITH
                {
                mWITH(); 

                }
                break;
            case 66 :
                // InternalCQLLexer.g:1:506: ALL
                {
                mALL(); 

                }
                break;
            case 67 :
                // InternalCQLLexer.g:1:510: AND
                {
                mAND(); 

                }
                break;
            case 68 :
                // InternalCQLLexer.g:1:514: ANY
                {
                mANY(); 

                }
                break;
            case 69 :
                // InternalCQLLexer.g:1:518: NOT
                {
                mNOT(); 

                }
                break;
            case 70 :
                // InternalCQLLexer.g:1:522: ExclamationMarkEqualsSign
                {
                mExclamationMarkEqualsSign(); 

                }
                break;
            case 71 :
                // InternalCQLLexer.g:1:548: LeftParenthesisRightParenthesis
                {
                mLeftParenthesisRightParenthesis(); 

                }
                break;
            case 72 :
                // InternalCQLLexer.g:1:580: LessThanSignEqualsSign
                {
                mLessThanSignEqualsSign(); 

                }
                break;
            case 73 :
                // InternalCQLLexer.g:1:603: GreaterThanSignEqualsSign
                {
                mGreaterThanSignEqualsSign(); 

                }
                break;
            case 74 :
                // InternalCQLLexer.g:1:629: AS
                {
                mAS(); 

                }
                break;
            case 75 :
                // InternalCQLLexer.g:1:632: AT
                {
                mAT(); 

                }
                break;
            case 76 :
                // InternalCQLLexer.g:1:635: BY
                {
                mBY(); 

                }
                break;
            case 77 :
                // InternalCQLLexer.g:1:638: IF
                {
                mIF(); 

                }
                break;
            case 78 :
                // InternalCQLLexer.g:1:641: IN
                {
                mIN(); 

                }
                break;
            case 79 :
                // InternalCQLLexer.g:1:644: ON
                {
                mON(); 

                }
                break;
            case 80 :
                // InternalCQLLexer.g:1:647: OR
                {
                mOR(); 

                }
                break;
            case 81 :
                // InternalCQLLexer.g:1:650: TO
                {
                mTO(); 

                }
                break;
            case 82 :
                // InternalCQLLexer.g:1:653: DollarSign
                {
                mDollarSign(); 

                }
                break;
            case 83 :
                // InternalCQLLexer.g:1:664: LeftParenthesis
                {
                mLeftParenthesis(); 

                }
                break;
            case 84 :
                // InternalCQLLexer.g:1:680: RightParenthesis
                {
                mRightParenthesis(); 

                }
                break;
            case 85 :
                // InternalCQLLexer.g:1:697: Asterisk
                {
                mAsterisk(); 

                }
                break;
            case 86 :
                // InternalCQLLexer.g:1:706: PlusSign
                {
                mPlusSign(); 

                }
                break;
            case 87 :
                // InternalCQLLexer.g:1:715: Comma
                {
                mComma(); 

                }
                break;
            case 88 :
                // InternalCQLLexer.g:1:721: HyphenMinus
                {
                mHyphenMinus(); 

                }
                break;
            case 89 :
                // InternalCQLLexer.g:1:733: FullStop
                {
                mFullStop(); 

                }
                break;
            case 90 :
                // InternalCQLLexer.g:1:742: Solidus
                {
                mSolidus(); 

                }
                break;
            case 91 :
                // InternalCQLLexer.g:1:750: Colon
                {
                mColon(); 

                }
                break;
            case 92 :
                // InternalCQLLexer.g:1:756: Semicolon
                {
                mSemicolon(); 

                }
                break;
            case 93 :
                // InternalCQLLexer.g:1:766: LessThanSign
                {
                mLessThanSign(); 

                }
                break;
            case 94 :
                // InternalCQLLexer.g:1:779: EqualsSign
                {
                mEqualsSign(); 

                }
                break;
            case 95 :
                // InternalCQLLexer.g:1:790: GreaterThanSign
                {
                mGreaterThanSign(); 

                }
                break;
            case 96 :
                // InternalCQLLexer.g:1:806: LeftSquareBracket
                {
                mLeftSquareBracket(); 

                }
                break;
            case 97 :
                // InternalCQLLexer.g:1:824: RightSquareBracket
                {
                mRightSquareBracket(); 

                }
                break;
            case 98 :
                // InternalCQLLexer.g:1:843: CircumflexAccent
                {
                mCircumflexAccent(); 

                }
                break;
            case 99 :
                // InternalCQLLexer.g:1:860: LeftCurlyBracket
                {
                mLeftCurlyBracket(); 

                }
                break;
            case 100 :
                // InternalCQLLexer.g:1:877: RightCurlyBracket
                {
                mRightCurlyBracket(); 

                }
                break;
            case 101 :
                // InternalCQLLexer.g:1:895: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 102 :
                // InternalCQLLexer.g:1:903: RULE_FLOAT
                {
                mRULE_FLOAT(); 

                }
                break;
            case 103 :
                // InternalCQLLexer.g:1:914: RULE_BYTE
                {
                mRULE_BYTE(); 

                }
                break;
            case 104 :
                // InternalCQLLexer.g:1:924: RULE_VECTOR_FLOAT
                {
                mRULE_VECTOR_FLOAT(); 

                }
                break;
            case 105 :
                // InternalCQLLexer.g:1:942: RULE_MATRIX_FLOAT
                {
                mRULE_MATRIX_FLOAT(); 

                }
                break;
            case 106 :
                // InternalCQLLexer.g:1:960: RULE_PATH
                {
                mRULE_PATH(); 

                }
                break;
            case 107 :
                // InternalCQLLexer.g:1:970: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 108 :
                // InternalCQLLexer.g:1:979: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 109 :
                // InternalCQLLexer.g:1:991: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 110 :
                // InternalCQLLexer.g:1:1007: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 111 :
                // InternalCQLLexer.g:1:1023: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 112 :
                // InternalCQLLexer.g:1:1031: RULE_ANY_OTHER
                {
                mRULE_ANY_OTHER(); 

                }
                break;

        }

    }


    protected DFA21 dfa21 = new DFA21(this);
    static final String DFA21_eotS =
        "\1\uffff\23\63\1\57\1\147\1\151\1\153\1\63\1\155\6\uffff\1\166\1\167\2\uffff\1\172\2\uffff\1\176\1\177\1\uffff\2\u0080\2\57\2\uffff\3\63\1\uffff\1\u008d\1\63\1\u008f\17\63\1\u00a3\3\63\1\u00a9\2\63\1\u00ae\1\63\1\u00b0\1\u00b1\24\63\7\uffff\1\u00ca\24\uffff\2\u0080\1\uffff\1\u0085\3\uffff\1\63\1\u00d0\3\63\1\uffff\1\63\1\uffff\23\63\1\uffff\5\63\1\uffff\1\63\1\u00f0\1\u00f1\1\u00f2\1\uffff\1\63\2\uffff\30\63\2\uffff\1\u0080\2\uffff\1\63\1\uffff\1\63\1\u0112\10\63\1\u011c\11\63\1\u0126\3\63\1\u012a\2\63\1\u012d\3\63\3\uffff\4\63\1\u0135\1\u0136\2\63\1\u0139\1\63\1\u013c\1\63\1\u013e\1\63\1\u0140\1\63\1\u0143\1\63\1\u0145\1\63\1\u0147\1\u0148\2\63\1\u014b\1\u014c\1\uffff\1\u0080\1\uffff\2\63\1\uffff\4\63\1\u0159\4\63\1\uffff\11\63\1\uffff\1\63\1\u0168\1\u0169\1\uffff\1\63\1\u016b\1\uffff\2\63\1\u016e\4\63\2\uffff\1\63\1\u0174\1\uffff\1\63\1\u0176\1\uffff\1\u0177\1\uffff\1\63\1\uffff\1\63\1\u017a\1\uffff\1\63\1\uffff\1\u017c\2\uffff\1\u017d\1\u017e\6\uffff\1\u0080\1\uffff\5\63\1\u0189\1\uffff\7\63\1\u0191\5\63\1\u0197\2\uffff\1\63\1\uffff\1\63\1\u019a\1\uffff\1\63\1\u019d\1\u019e\1\u019f\1\u01a0\1\uffff\1\63\2\uffff\1\u01a2\1\u01a3\1\uffff\1\u01a4\5\uffff\1\u0080\1\uffff\5\63\1\u01af\1\uffff\5\63\1\u01b5\1\u01b6\1\uffff\5\63\1\uffff\1\63\1\u01bd\1\uffff\1\u01be\1\u01bf\4\uffff\1\u01c0\4\uffff\1\u0080\1\u0085\2\uffff\5\63\1\uffff\1\63\1\u01cd\1\63\1\u01cf\1\63\2\uffff\1\63\1\u01d2\1\u01d3\1\63\1\u01d5\1\63\5\uffff\1\u01d7\1\uffff\1\u00cd\2\uffff\6\63\1\uffff\1\63\1\uffff\1\63\1\u01e1\2\uffff\1\u01e2\1\uffff\1\u01e3\2\uffff\1\63\1\u01e7\1\63\1\u01e9\2\63\1\u01ec\1\u01ed\4\uffff\1\63\1\u01f0\1\uffff\1\63\1\uffff\1\u01f3\1\u01f4\3\uffff\1\63\1\uffff\1\u01f7\1\u01f8\3\uffff\1\63\2\uffff\1\u0085\2\uffff\12\63\1\u0207\1\uffff";
    static final String DFA21_eofS =
        "\u0208\uffff";
    static final String DFA21_minS =
        "\1\0\1\101\1\104\1\111\1\101\1\110\2\101\1\116\1\104\1\116\2\105\2\101\1\105\1\101\1\122\1\104\1\111\1\75\1\51\2\75\1\131\1\44\6\uffff\1\52\1\44\2\uffff\1\60\2\uffff\2\44\1\uffff\2\56\2\0\2\uffff\1\124\1\116\1\114\1\uffff\1\44\1\105\1\44\2\114\1\124\1\106\1\117\1\116\1\101\1\105\1\122\1\117\1\101\1\116\1\102\1\120\1\115\1\44\1\102\1\105\1\126\1\44\1\114\1\104\1\44\1\124\2\44\1\103\1\116\1\117\1\115\1\101\2\105\1\124\1\111\1\103\1\126\1\125\1\126\3\114\1\117\1\101\1\102\1\105\7\uffff\1\44\16\uffff\1\56\5\uffff\2\56\1\uffff\1\40\1\uffff\1\0\1\uffff\1\114\1\44\1\117\1\114\1\105\1\uffff\1\116\1\uffff\1\114\1\125\1\124\1\101\1\106\1\124\1\120\2\116\1\101\1\124\1\123\1\124\1\116\1\105\1\101\2\114\1\105\1\uffff\2\117\1\122\2\101\1\uffff\1\105\3\44\1\uffff\1\111\2\uffff\1\117\1\105\1\107\2\105\1\122\1\105\1\120\1\113\1\122\1\110\1\123\1\110\1\111\1\122\1\117\1\105\1\123\1\105\1\115\1\116\1\125\1\103\1\127\1\uffff\1\60\1\56\1\uffff\1\0\1\101\1\uffff\1\123\1\44\1\122\1\124\1\111\1\124\1\111\1\102\1\105\1\111\1\44\2\105\1\116\1\124\1\111\1\127\1\117\1\123\1\103\1\44\1\116\2\105\1\44\1\125\1\116\1\44\1\116\1\103\1\122\3\uffff\1\117\1\116\1\103\1\114\2\44\1\101\1\105\1\44\1\120\1\44\1\105\1\44\1\124\1\44\1\116\1\44\1\113\1\44\1\105\2\44\1\124\1\120\2\44\1\54\1\56\1\0\1\132\1\105\1\uffff\1\123\1\111\1\123\1\105\1\44\2\101\1\122\1\116\1\uffff\1\103\1\130\2\105\1\124\1\117\1\103\1\120\1\101\1\uffff\1\124\2\44\1\uffff\1\116\1\44\1\uffff\1\103\1\110\1\44\1\116\1\104\1\124\1\105\2\uffff\1\115\1\44\1\uffff\1\105\1\44\1\uffff\1\44\1\uffff\1\123\1\uffff\1\107\1\44\1\uffff\1\105\1\uffff\1\44\2\uffff\2\44\2\uffff\1\60\2\uffff\1\54\1\56\1\0\1\131\1\103\1\105\1\106\1\105\1\44\1\uffff\1\116\1\123\1\105\1\103\2\124\1\114\1\44\1\111\1\122\2\117\1\124\1\44\2\uffff\1\104\1\uffff\1\105\1\44\1\uffff\1\123\4\44\1\uffff\1\122\2\uffff\2\44\1\uffff\1\44\3\uffff\1\56\1\uffff\1\56\1\0\1\137\1\117\1\103\1\111\1\103\1\44\1\uffff\1\104\1\105\1\116\1\124\1\111\2\44\1\uffff\1\117\1\104\1\114\1\122\1\105\1\uffff\1\105\1\44\1\uffff\2\44\4\uffff\1\44\3\uffff\1\60\1\56\1\42\2\0\1\103\1\116\1\124\1\105\1\117\1\uffff\1\114\1\44\1\103\1\44\1\117\2\uffff\1\116\2\44\1\124\1\44\1\104\4\uffff\1\54\1\56\2\0\1\uffff\1\0\1\117\1\104\1\111\1\104\1\116\1\105\1\uffff\1\105\1\uffff\1\116\1\44\2\uffff\1\44\1\uffff\1\44\1\uffff\1\0\1\116\1\44\1\117\1\44\1\104\1\122\2\44\3\uffff\1\0\1\116\1\44\1\uffff\1\116\1\uffff\2\44\2\uffff\1\0\1\105\1\uffff\2\44\2\uffff\1\0\1\103\2\uffff\1\42\2\0\1\124\1\111\1\117\1\116\1\137\1\103\1\110\1\105\1\103\1\113\1\44\1\uffff";
    static final String DFA21_maxS =
        "\1\uffff\1\165\1\156\1\165\3\162\1\165\1\163\1\164\1\162\1\164\1\162\1\170\2\157\2\162\1\144\1\151\1\75\1\51\2\75\1\171\1\175\6\uffff\1\57\1\175\2\uffff\1\71\2\uffff\2\175\1\uffff\2\71\2\uffff\2\uffff\1\164\1\156\1\154\1\uffff\1\175\1\145\1\175\1\156\1\154\1\164\1\163\1\157\1\156\1\141\1\145\1\163\1\157\1\165\1\156\1\142\1\160\1\155\1\175\1\151\1\145\1\166\1\175\1\164\1\171\1\175\1\164\2\175\1\154\1\172\1\162\1\155\1\141\2\145\1\164\1\151\1\143\1\166\1\165\1\166\3\154\2\157\1\142\1\145\7\uffff\1\175\16\uffff\1\71\5\uffff\2\71\1\uffff\1\40\1\uffff\1\uffff\1\uffff\1\154\1\175\1\157\1\154\1\145\1\uffff\1\156\1\uffff\1\154\1\165\1\164\1\141\1\146\1\164\1\160\1\164\1\156\1\141\1\164\1\163\1\164\2\156\1\141\2\154\1\145\1\uffff\2\157\1\162\2\141\1\uffff\1\145\3\175\1\uffff\1\151\2\uffff\1\157\1\145\1\153\2\145\1\162\1\145\1\160\1\153\1\162\1\150\1\163\1\150\1\151\1\162\1\157\1\145\1\163\1\145\1\155\1\156\1\165\1\143\1\167\1\uffff\2\71\1\uffff\1\uffff\1\141\1\uffff\1\163\1\175\1\162\1\164\1\151\1\164\1\151\1\150\1\145\1\151\1\175\2\145\1\156\1\164\1\151\1\167\1\157\1\163\1\143\1\175\1\156\2\145\1\175\1\165\1\156\1\175\1\156\1\143\1\162\3\uffff\1\157\1\156\1\143\1\154\2\175\1\141\1\145\1\175\1\160\1\175\1\145\1\175\1\164\1\175\1\156\1\175\1\153\1\175\1\145\2\175\1\164\1\160\2\175\1\135\1\71\1\uffff\1\172\1\145\1\uffff\1\163\1\151\1\163\1\145\1\175\2\141\1\162\1\156\1\uffff\1\143\1\170\2\145\1\164\1\157\1\143\1\160\1\141\1\uffff\1\164\2\175\1\uffff\1\156\1\175\1\uffff\1\143\1\150\1\175\1\156\1\144\1\164\1\145\2\uffff\1\155\1\175\1\uffff\1\145\1\175\1\uffff\1\175\1\uffff\1\163\1\uffff\1\147\1\175\1\uffff\1\145\1\uffff\1\175\2\uffff\2\175\2\uffff\1\71\2\uffff\1\135\1\71\1\uffff\1\171\1\143\1\145\1\146\1\145\1\175\1\uffff\1\156\1\163\1\145\1\143\2\164\1\154\1\175\1\151\1\162\2\157\1\164\1\175\2\uffff\1\144\1\uffff\1\145\1\175\1\uffff\1\163\4\175\1\uffff\1\162\2\uffff\2\175\1\uffff\1\175\3\uffff\1\71\1\uffff\1\71\1\uffff\1\137\1\157\1\143\1\151\1\143\1\175\1\uffff\1\144\1\145\1\156\1\164\1\151\2\175\1\uffff\1\157\1\144\1\154\1\162\1\145\1\uffff\1\145\1\175\1\uffff\2\175\4\uffff\1\175\3\uffff\2\71\1\47\2\uffff\1\143\1\156\1\164\1\145\1\157\1\uffff\1\154\1\175\1\143\1\175\1\157\2\uffff\1\156\2\175\1\164\1\175\1\144\4\uffff\1\135\1\71\2\uffff\1\uffff\1\uffff\1\157\1\144\1\151\1\144\1\156\1\145\1\uffff\1\145\1\uffff\1\156\1\175\2\uffff\1\175\1\uffff\1\175\1\uffff\1\uffff\1\156\1\175\1\157\1\175\1\144\1\162\2\175\3\uffff\1\uffff\1\156\1\175\1\uffff\1\156\1\uffff\2\175\2\uffff\1\uffff\1\145\1\uffff\2\175\2\uffff\1\uffff\1\143\2\uffff\1\47\2\uffff\1\164\1\151\1\157\1\156\1\137\1\143\1\150\1\145\1\143\1\153\1\175\1\uffff";
    static final String DFA21_acceptS =
        "\32\uffff\1\124\1\125\1\126\1\127\1\130\1\131\2\uffff\1\134\1\136\1\uffff\1\141\1\142\2\uffff\1\145\4\uffff\1\157\1\160\3\uffff\1\145\61\uffff\1\106\1\107\1\123\1\110\1\135\1\111\1\137\1\uffff\1\122\1\124\1\125\1\126\1\127\1\130\1\131\1\155\1\156\1\132\1\133\1\134\1\136\1\140\1\uffff\1\141\1\142\1\143\1\144\1\153\2\uffff\1\146\1\uffff\1\154\1\uffff\1\157\5\uffff\1\116\1\uffff\1\115\23\uffff\1\121\5\uffff\1\113\4\uffff\1\112\1\uffff\1\117\1\120\30\uffff\1\114\2\uffff\1\152\2\uffff\1\105\37\uffff\1\102\1\103\1\104\37\uffff\1\67\11\uffff\1\61\11\uffff\1\75\3\uffff\1\74\2\uffff\1\76\7\uffff\1\71\1\72\2\uffff\1\73\2\uffff\1\100\1\uffff\1\101\1\uffff\1\62\2\uffff\1\65\1\uffff\1\70\1\uffff\1\63\1\64\2\uffff\1\66\1\77\1\uffff\1\151\1\150\11\uffff\1\52\16\uffff\1\54\1\55\1\uffff\1\56\2\uffff\1\45\5\uffff\1\53\1\uffff\1\57\1\60\2\uffff\1\51\1\uffff\1\46\1\47\1\50\1\uffff\1\150\10\uffff\1\36\7\uffff\1\33\5\uffff\1\44\2\uffff\1\32\2\uffff\1\40\1\41\1\42\1\43\1\uffff\1\34\1\35\1\37\12\uffff\1\26\5\uffff\1\25\1\24\6\uffff\1\23\1\27\1\30\1\31\4\uffff\1\152\7\uffff\1\16\1\uffff\1\17\2\uffff\1\20\1\21\1\uffff\1\22\1\uffff\1\147\11\uffff\1\13\1\14\1\15\3\uffff\1\12\1\uffff\1\11\2\uffff\1\10\1\7\2\uffff\1\6\2\uffff\1\5\1\4\2\uffff\1\2\1\3\16\uffff\1\1";
    static final String DFA21_specialS =
        "\1\4\53\uffff\1\3\1\21\130\uffff\1\7\107\uffff\1\10\100\uffff\1\1\102\uffff\1\6\57\uffff\1\12\45\uffff\1\20\1\0\31\uffff\1\11\1\5\1\uffff\1\14\21\uffff\1\15\13\uffff\1\2\11\uffff\1\13\6\uffff\1\16\4\uffff\1\17\1\22\14\uffff}>";
    static final String[] DFA21_transitionS = {
            "\11\57\2\56\2\57\1\56\22\57\1\56\1\24\1\55\1\57\1\31\2\57\1\54\1\25\1\32\1\33\1\34\1\35\1\36\1\37\1\40\2\52\10\53\1\41\1\42\1\26\1\43\1\27\2\57\1\11\1\30\1\5\1\4\1\15\1\20\1\21\1\16\1\2\1\22\2\51\1\3\1\1\1\12\1\6\1\51\1\17\1\13\1\7\1\10\1\23\1\14\3\51\1\44\1\57\1\45\1\46\1\51\1\57\1\11\1\30\1\5\1\4\1\15\1\20\1\21\1\16\1\2\1\22\2\51\1\3\1\1\1\12\1\6\1\51\1\17\1\13\1\7\1\10\1\23\1\14\3\51\1\47\1\57\1\50\uff82\57",
            "\1\61\15\uffff\1\60\5\uffff\1\62\13\uffff\1\61\15\uffff\1\60\5\uffff\1\62",
            "\1\65\1\uffff\1\66\7\uffff\1\64\25\uffff\1\65\1\uffff\1\66\7\uffff\1\64",
            "\1\67\13\uffff\1\70\23\uffff\1\67\13\uffff\1\70",
            "\1\71\7\uffff\1\72\10\uffff\1\73\16\uffff\1\71\7\uffff\1\72\10\uffff\1\73",
            "\1\75\6\uffff\1\74\2\uffff\1\76\25\uffff\1\75\6\uffff\1\74\2\uffff\1\76",
            "\1\77\20\uffff\1\100\16\uffff\1\77\20\uffff\1\100",
            "\1\103\3\uffff\1\102\3\uffff\1\105\5\uffff\1\106\2\uffff\1\101\2\uffff\1\104\13\uffff\1\103\3\uffff\1\102\3\uffff\1\105\5\uffff\1\106\2\uffff\1\101\2\uffff\1\104",
            "\1\107\4\uffff\1\110\32\uffff\1\107\4\uffff\1\110",
            "\1\111\7\uffff\1\113\1\uffff\1\114\4\uffff\1\115\1\112\17\uffff\1\111\7\uffff\1\113\1\uffff\1\114\4\uffff\1\115\1\112",
            "\1\117\1\uffff\1\116\1\uffff\1\120\33\uffff\1\117\1\uffff\1\116\1\uffff\1\120",
            "\1\121\3\uffff\1\122\5\uffff\1\124\4\uffff\1\123\20\uffff\1\121\3\uffff\1\122\5\uffff\1\124\4\uffff\1\123",
            "\1\126\2\uffff\1\127\1\130\10\uffff\1\125\22\uffff\1\126\2\uffff\1\127\1\130\10\uffff\1\125",
            "\1\132\26\uffff\1\131\10\uffff\1\132\26\uffff\1\131",
            "\1\133\15\uffff\1\134\21\uffff\1\133\15\uffff\1\134",
            "\1\135\11\uffff\1\136\25\uffff\1\135\11\uffff\1\136",
            "\1\137\7\uffff\1\140\10\uffff\1\141\16\uffff\1\137\7\uffff\1\140\10\uffff\1\141",
            "\1\142\37\uffff\1\142",
            "\1\143\37\uffff\1\143",
            "\1\144\37\uffff\1\144",
            "\1\145",
            "\1\146",
            "\1\150",
            "\1\152",
            "\1\154\37\uffff\1\154",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\164\4\uffff\1\165",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "",
            "\12\173",
            "",
            "",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\1\u0083\1\uffff\2\u0081\10\u0082",
            "\1\u0083\1\uffff\12\u0082",
            "\47\u0085\1\u0084\uffd8\u0085",
            "\47\u0085\1\u0086\uffd8\u0085",
            "",
            "",
            "\1\u0089\12\uffff\1\u0088\24\uffff\1\u0089",
            "\1\u008a\37\uffff\1\u008a",
            "\1\u008b\37\uffff\1\u008b",
            "",
            "\1\63\13\uffff\13\63\6\uffff\23\63\1\u008c\6\63\4\uffff\1\63\1\uffff\23\63\1\u008c\7\63\1\uffff\1\63",
            "\1\u008e\37\uffff\1\u008e",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u0090\1\uffff\1\u0091\35\uffff\1\u0090\1\uffff\1\u0091",
            "\1\u0092\37\uffff\1\u0092",
            "\1\u0093\37\uffff\1\u0093",
            "\1\u0094\14\uffff\1\u0095\22\uffff\1\u0094\14\uffff\1\u0095",
            "\1\u0096\37\uffff\1\u0096",
            "\1\u0097\37\uffff\1\u0097",
            "\1\u0098\37\uffff\1\u0098",
            "\1\u0099\37\uffff\1\u0099",
            "\1\u009a\1\u009b\36\uffff\1\u009a\1\u009b",
            "\1\u009c\37\uffff\1\u009c",
            "\1\u009d\23\uffff\1\u009e\13\uffff\1\u009d\23\uffff\1\u009e",
            "\1\u009f\37\uffff\1\u009f",
            "\1\u00a0\37\uffff\1\u00a0",
            "\1\u00a1\37\uffff\1\u00a1",
            "\1\u00a2\37\uffff\1\u00a2",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u00a4\6\uffff\1\u00a5\30\uffff\1\u00a4\6\uffff\1\u00a5",
            "\1\u00a6\37\uffff\1\u00a6",
            "\1\u00a7\37\uffff\1\u00a7",
            "\1\63\13\uffff\13\63\6\uffff\23\63\1\u00a8\6\63\4\uffff\1\63\1\uffff\23\63\1\u00a8\7\63\1\uffff\1\63",
            "\1\u00ab\7\uffff\1\u00aa\27\uffff\1\u00ab\7\uffff\1\u00aa",
            "\1\u00ac\24\uffff\1\u00ad\12\uffff\1\u00ac\24\uffff\1\u00ad",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u00af\37\uffff\1\u00af",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u00b2\10\uffff\1\u00b3\26\uffff\1\u00b2\10\uffff\1\u00b3",
            "\1\u00b4\13\uffff\1\u00b5\23\uffff\1\u00b4\13\uffff\1\u00b5",
            "\1\u00b7\2\uffff\1\u00b6\34\uffff\1\u00b7\2\uffff\1\u00b6",
            "\1\u00b8\37\uffff\1\u00b8",
            "\1\u00b9\37\uffff\1\u00b9",
            "\1\u00ba\37\uffff\1\u00ba",
            "\1\u00bb\37\uffff\1\u00bb",
            "\1\u00bc\37\uffff\1\u00bc",
            "\1\u00bd\37\uffff\1\u00bd",
            "\1\u00be\37\uffff\1\u00be",
            "\1\u00bf\37\uffff\1\u00bf",
            "\1\u00c0\37\uffff\1\u00c0",
            "\1\u00c1\37\uffff\1\u00c1",
            "\1\u00c2\37\uffff\1\u00c2",
            "\1\u00c3\37\uffff\1\u00c3",
            "\1\u00c4\37\uffff\1\u00c4",
            "\1\u00c5\37\uffff\1\u00c5",
            "\1\u00c6\15\uffff\1\u00c7\21\uffff\1\u00c6\15\uffff\1\u00c7",
            "\1\u00c8\37\uffff\1\u00c8",
            "\1\u00c9\37\uffff\1\u00c9",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
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
            "\1\u00cb\1\uffff\12\173",
            "",
            "",
            "",
            "",
            "",
            "\1\u0083\1\uffff\2\u00cc\10\u0082",
            "\1\u0083\1\uffff\12\u0082",
            "",
            "\1\u00cd",
            "",
            "\40\u0085\1\u00ce\uffdf\u0085",
            "",
            "\1\u00cf\37\uffff\1\u00cf",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u00d1\37\uffff\1\u00d1",
            "\1\u00d2\37\uffff\1\u00d2",
            "\1\u00d3\37\uffff\1\u00d3",
            "",
            "\1\u00d4\37\uffff\1\u00d4",
            "",
            "\1\u00d5\37\uffff\1\u00d5",
            "\1\u00d6\37\uffff\1\u00d6",
            "\1\u00d7\37\uffff\1\u00d7",
            "\1\u00d8\37\uffff\1\u00d8",
            "\1\u00d9\37\uffff\1\u00d9",
            "\1\u00da\37\uffff\1\u00da",
            "\1\u00db\37\uffff\1\u00db",
            "\1\u00dc\5\uffff\1\u00dd\31\uffff\1\u00dc\5\uffff\1\u00dd",
            "\1\u00de\37\uffff\1\u00de",
            "\1\u00df\37\uffff\1\u00df",
            "\1\u00e0\37\uffff\1\u00e0",
            "\1\u00e1\37\uffff\1\u00e1",
            "\1\u00e2\37\uffff\1\u00e2",
            "\1\u00e3\37\uffff\1\u00e3",
            "\1\u00e5\10\uffff\1\u00e4\26\uffff\1\u00e5\10\uffff\1\u00e4",
            "\1\u00e6\37\uffff\1\u00e6",
            "\1\u00e7\37\uffff\1\u00e7",
            "\1\u00e8\37\uffff\1\u00e8",
            "\1\u00e9\37\uffff\1\u00e9",
            "",
            "\1\u00ea\37\uffff\1\u00ea",
            "\1\u00eb\37\uffff\1\u00eb",
            "\1\u00ec\37\uffff\1\u00ec",
            "\1\u00ed\37\uffff\1\u00ed",
            "\1\u00ee\37\uffff\1\u00ee",
            "",
            "\1\u00ef\37\uffff\1\u00ef",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\1\u00f3\37\uffff\1\u00f3",
            "",
            "",
            "\1\u00f4\37\uffff\1\u00f4",
            "\1\u00f5\37\uffff\1\u00f5",
            "\1\u00f6\3\uffff\1\u00f7\33\uffff\1\u00f6\3\uffff\1\u00f7",
            "\1\u00f8\37\uffff\1\u00f8",
            "\1\u00f9\37\uffff\1\u00f9",
            "\1\u00fa\37\uffff\1\u00fa",
            "\1\u00fb\37\uffff\1\u00fb",
            "\1\u00fc\37\uffff\1\u00fc",
            "\1\u00fd\37\uffff\1\u00fd",
            "\1\u00fe\37\uffff\1\u00fe",
            "\1\u00ff\37\uffff\1\u00ff",
            "\1\u0100\37\uffff\1\u0100",
            "\1\u0101\37\uffff\1\u0101",
            "\1\u0102\37\uffff\1\u0102",
            "\1\u0103\37\uffff\1\u0103",
            "\1\u0104\37\uffff\1\u0104",
            "\1\u0105\37\uffff\1\u0105",
            "\1\u0106\37\uffff\1\u0106",
            "\1\u0107\37\uffff\1\u0107",
            "\1\u0108\37\uffff\1\u0108",
            "\1\u0109\37\uffff\1\u0109",
            "\1\u010a\37\uffff\1\u010a",
            "\1\u010b\37\uffff\1\u010b",
            "\1\u010c\37\uffff\1\u010c",
            "",
            "\12\u010d",
            "\1\u0083\1\uffff\2\u010e\10\u0082",
            "",
            "\134\u0085\1\u010f\uffa3\u0085",
            "\1\u0110\37\uffff\1\u0110",
            "",
            "\1\u0111\37\uffff\1\u0111",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u0113\37\uffff\1\u0113",
            "\1\u0114\37\uffff\1\u0114",
            "\1\u0115\37\uffff\1\u0115",
            "\1\u0116\37\uffff\1\u0116",
            "\1\u0117\37\uffff\1\u0117",
            "\1\u0119\5\uffff\1\u0118\31\uffff\1\u0119\5\uffff\1\u0118",
            "\1\u011a\37\uffff\1\u011a",
            "\1\u011b\37\uffff\1\u011b",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u011d\37\uffff\1\u011d",
            "\1\u011e\37\uffff\1\u011e",
            "\1\u011f\37\uffff\1\u011f",
            "\1\u0120\37\uffff\1\u0120",
            "\1\u0121\37\uffff\1\u0121",
            "\1\u0122\37\uffff\1\u0122",
            "\1\u0123\37\uffff\1\u0123",
            "\1\u0124\37\uffff\1\u0124",
            "\1\u0125\37\uffff\1\u0125",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u0127\37\uffff\1\u0127",
            "\1\u0128\37\uffff\1\u0128",
            "\1\u0129\37\uffff\1\u0129",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u012b\37\uffff\1\u012b",
            "\1\u012c\37\uffff\1\u012c",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u012e\37\uffff\1\u012e",
            "\1\u012f\37\uffff\1\u012f",
            "\1\u0130\37\uffff\1\u0130",
            "",
            "",
            "",
            "\1\u0131\37\uffff\1\u0131",
            "\1\u0132\37\uffff\1\u0132",
            "\1\u0133\37\uffff\1\u0133",
            "\1\u0134\37\uffff\1\u0134",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u0137\37\uffff\1\u0137",
            "\1\u0138\37\uffff\1\u0138",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u013a\37\uffff\1\u013a",
            "\1\63\13\uffff\13\63\6\uffff\22\63\1\u013b\7\63\4\uffff\1\63\1\uffff\22\63\1\u013b\10\63\1\uffff\1\63",
            "\1\u013d\37\uffff\1\u013d",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u013f\37\uffff\1\u013f",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u0141\37\uffff\1\u0141",
            "\1\63\13\uffff\13\63\6\uffff\22\63\1\u0142\7\63\4\uffff\1\63\1\uffff\22\63\1\u0142\10\63\1\uffff\1\63",
            "\1\u0144\37\uffff\1\u0144",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u0146\37\uffff\1\u0146",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u0149\37\uffff\1\u0149",
            "\1\u014a\37\uffff\1\u014a",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u014d\3\uffff\12\u0150\1\uffff\1\u014e\41\uffff\1\u014f",
            "\1\u0083\1\uffff\2\u0151\10\u0082",
            "\40\u0085\1\u0152\uffdf\u0085",
            "\1\u0153\37\uffff\1\u0153",
            "\1\u0154\37\uffff\1\u0154",
            "",
            "\1\u0155\37\uffff\1\u0155",
            "\1\u0156\37\uffff\1\u0156",
            "\1\u0157\37\uffff\1\u0157",
            "\1\u0158\37\uffff\1\u0158",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u015a\37\uffff\1\u015a",
            "\1\u015b\37\uffff\1\u015b",
            "\1\u015c\37\uffff\1\u015c",
            "\1\u015d\37\uffff\1\u015d",
            "",
            "\1\u015e\37\uffff\1\u015e",
            "\1\u015f\37\uffff\1\u015f",
            "\1\u0160\37\uffff\1\u0160",
            "\1\u0161\37\uffff\1\u0161",
            "\1\u0162\37\uffff\1\u0162",
            "\1\u0163\37\uffff\1\u0163",
            "\1\u0164\37\uffff\1\u0164",
            "\1\u0165\37\uffff\1\u0165",
            "\1\u0166\37\uffff\1\u0166",
            "",
            "\1\u0167\37\uffff\1\u0167",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\1\u016a\37\uffff\1\u016a",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\1\u016c\37\uffff\1\u016c",
            "\1\u016d\37\uffff\1\u016d",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u016f\37\uffff\1\u016f",
            "\1\u0170\37\uffff\1\u0170",
            "\1\u0171\37\uffff\1\u0171",
            "\1\u0172\37\uffff\1\u0172",
            "",
            "",
            "\1\u0173\37\uffff\1\u0173",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\1\u0175\37\uffff\1\u0175",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\1\u0178\37\uffff\1\u0178",
            "",
            "\1\u0179\37\uffff\1\u0179",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\1\u017b\37\uffff\1\u017b",
            "",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "",
            "\12\u017f",
            "",
            "",
            "\1\u014d\1\uffff\1\u00cb\1\uffff\12\u0150\1\uffff\1\u014e\41\uffff\1\u014f",
            "\1\u0083\1\uffff\2\u0181\10\u0082",
            "\47\u0085\1\u0182\uffd8\u0085",
            "\1\u0183\37\uffff\1\u0183",
            "\1\u0184\37\uffff\1\u0184",
            "\1\u0185\37\uffff\1\u0185",
            "\1\u0186\37\uffff\1\u0186",
            "\1\u0187\37\uffff\1\u0187",
            "\1\63\13\uffff\13\63\6\uffff\22\63\1\u0188\7\63\4\uffff\1\63\1\uffff\22\63\1\u0188\10\63\1\uffff\1\63",
            "",
            "\1\u018a\37\uffff\1\u018a",
            "\1\u018b\37\uffff\1\u018b",
            "\1\u018c\37\uffff\1\u018c",
            "\1\u018d\37\uffff\1\u018d",
            "\1\u018e\37\uffff\1\u018e",
            "\1\u018f\37\uffff\1\u018f",
            "\1\u0190\37\uffff\1\u0190",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u0192\37\uffff\1\u0192",
            "\1\u0193\37\uffff\1\u0193",
            "\1\u0194\37\uffff\1\u0194",
            "\1\u0195\37\uffff\1\u0195",
            "\1\u0196\37\uffff\1\u0196",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "",
            "\1\u0198\37\uffff\1\u0198",
            "",
            "\1\u0199\37\uffff\1\u0199",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\1\u019b\37\uffff\1\u019b",
            "\1\63\13\uffff\13\63\6\uffff\22\63\1\u019c\7\63\4\uffff\1\63\1\uffff\22\63\1\u019c\10\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\1\u01a1\37\uffff\1\u01a1",
            "",
            "",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "",
            "",
            "\1\u01a5\1\uffff\12\u017f",
            "",
            "\1\u0083\1\uffff\2\u01a6\10\u0082",
            "\42\u01a9\1\u01a7\71\u01a9\1\u01a8\uffa3\u01a9",
            "\1\u01aa",
            "\1\u01ab\37\uffff\1\u01ab",
            "\1\u01ac\37\uffff\1\u01ac",
            "\1\u01ad\37\uffff\1\u01ad",
            "\1\u01ae\37\uffff\1\u01ae",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\1\u01b0\37\uffff\1\u01b0",
            "\1\u01b1\37\uffff\1\u01b1",
            "\1\u01b2\37\uffff\1\u01b2",
            "\1\u01b3\37\uffff\1\u01b3",
            "\1\u01b4\37\uffff\1\u01b4",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\1\u01b7\37\uffff\1\u01b7",
            "\1\u01b8\37\uffff\1\u01b8",
            "\1\u01b9\37\uffff\1\u01b9",
            "\1\u01ba\37\uffff\1\u01ba",
            "\1\u01bb\37\uffff\1\u01bb",
            "",
            "\1\u01bc\37\uffff\1\u01bc",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "",
            "",
            "",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "",
            "",
            "\12\u01c1",
            "\1\u0083\1\uffff\2\u01c2\10\u0082",
            "\1\u00cd\4\uffff\1\u00cd",
            "\42\u0085\1\u01c4\4\u0085\1\u01c3\uffd8\u0085",
            "\42\u0085\1\u01c5\4\u0085\1\u01c6\uffd8\u0085",
            "\1\u01c7\37\uffff\1\u01c7",
            "\1\u01c8\37\uffff\1\u01c8",
            "\1\u01c9\37\uffff\1\u01c9",
            "\1\u01ca\37\uffff\1\u01ca",
            "\1\u01cb\37\uffff\1\u01cb",
            "",
            "\1\u01cc\37\uffff\1\u01cc",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u01ce\37\uffff\1\u01ce",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u01d0\37\uffff\1\u01d0",
            "",
            "",
            "\1\u01d1\37\uffff\1\u01d1",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u01d4\37\uffff\1\u01d4",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u01d6\37\uffff\1\u01d6",
            "",
            "",
            "",
            "",
            "\1\u014d\3\uffff\12\u01c1\1\uffff\1\u014e\41\uffff\1\u014f",
            "\1\u0083\1\uffff\12\u0082",
            "\40\u0085\1\u01d8\uffdf\u0085",
            "\0\u0085",
            "",
            "\40\u0085\1\u01d8\uffdf\u0085",
            "\1\u01d9\37\uffff\1\u01d9",
            "\1\u01da\37\uffff\1\u01da",
            "\1\u01db\37\uffff\1\u01db",
            "\1\u01dc\37\uffff\1\u01dc",
            "\1\u01dd\37\uffff\1\u01dd",
            "\1\u01de\37\uffff\1\u01de",
            "",
            "\1\u01df\37\uffff\1\u01df",
            "",
            "\1\u01e0\37\uffff\1\u01e0",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\134\u0085\1\u01e4\uffa3\u0085",
            "\1\u01e5\37\uffff\1\u01e5",
            "\1\63\13\uffff\13\63\6\uffff\22\63\1\u01e6\7\63\4\uffff\1\63\1\uffff\22\63\1\u01e6\10\63\1\uffff\1\63",
            "\1\u01e8\37\uffff\1\u01e8",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\u01ea\37\uffff\1\u01ea",
            "\1\u01eb\37\uffff\1\u01eb",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "",
            "",
            "\40\u0085\1\u01ee\uffdf\u0085",
            "\1\u01ef\37\uffff\1\u01ef",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "\1\u01f1\37\uffff\1\u01f1",
            "",
            "\1\63\13\uffff\13\63\6\uffff\22\63\1\u01f2\7\63\4\uffff\1\63\1\uffff\22\63\1\u01f2\10\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "",
            "\47\u0085\1\u01f5\uffd8\u0085",
            "\1\u01f6\37\uffff\1\u01f6",
            "",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
            "",
            "",
            "\42\u01fb\1\u01f9\71\u01fb\1\u01fa\uffa3\u01fb",
            "\1\u01fc\37\uffff\1\u01fc",
            "",
            "",
            "\1\u00cd\4\uffff\1\u00cd",
            "\42\u0085\1\u01c4\4\u0085\1\u01c3\uffd8\u0085",
            "\42\u0085\1\u01c5\4\u0085\1\u01c6\uffd8\u0085",
            "\1\u01fd\37\uffff\1\u01fd",
            "\1\u01fe\37\uffff\1\u01fe",
            "\1\u01ff\37\uffff\1\u01ff",
            "\1\u0200\37\uffff\1\u0200",
            "\1\u0201",
            "\1\u0202\37\uffff\1\u0202",
            "\1\u0203\37\uffff\1\u0203",
            "\1\u0204\37\uffff\1\u0204",
            "\1\u0205\37\uffff\1\u0205",
            "\1\u0206\37\uffff\1\u0206",
            "\1\63\13\uffff\13\63\6\uffff\32\63\4\uffff\1\63\1\uffff\33\63\1\uffff\1\63",
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
            return "1:1: Tokens : ( NO_LAZY_CONNECTION_CHECK | INTERSECTION | MILLISECONDS | DATAHANDLER | MILLISECOND | NANOSECONDS | CONNECTION | DIFFERENCE | IDENTIFIED | NANOSECOND | PARTITION | TRANSPORT | UNBOUNDED | DATABASE | DISTINCT | PASSWORD | PROTOCOL | TRUNCATE | ADVANCE | CHANNEL | CONTEXT | MINUTES | OPTIONS | SECONDS | WRAPPER | ATTACH | CREATE | EXISTS | HAVING | MINUTE | REVOKE | SECOND | SELECT | SINGLE | STREAM | TENANT | ALTER | FALSE | GRANT | GROUP | HOURS | MULTI | STORE | TABLE | TUPLE | UNION | WEEKS | WHERE | DROP | EACH | FILE | FROM | HOUR | JDBC | NULL | ROLE | SINK | SIZE | SOME | TIME | TRUE | USER | VIEW | WEEK | WITH | ALL | AND | ANY | NOT | ExclamationMarkEqualsSign | LeftParenthesisRightParenthesis | LessThanSignEqualsSign | GreaterThanSignEqualsSign | AS | AT | BY | IF | IN | ON | OR | TO | DollarSign | LeftParenthesis | RightParenthesis | Asterisk | PlusSign | Comma | HyphenMinus | FullStop | Solidus | Colon | Semicolon | LessThanSign | EqualsSign | GreaterThanSign | LeftSquareBracket | RightSquareBracket | CircumflexAccent | LeftCurlyBracket | RightCurlyBracket | RULE_ID | RULE_FLOAT | RULE_BYTE | RULE_VECTOR_FLOAT | RULE_MATRIX_FLOAT | RULE_PATH | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA21_425 = input.LA(1);

                        s = -1;
                        if ( (LA21_425=='\"') ) {s = 453;}

                        else if ( ((LA21_425>='\u0000' && LA21_425<='!')||(LA21_425>='#' && LA21_425<='&')||(LA21_425>='(' && LA21_425<='\uFFFF')) ) {s = 133;}

                        else if ( (LA21_425=='\'') ) {s = 454;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA21_271 = input.LA(1);

                        s = -1;
                        if ( (LA21_271==' ') ) {s = 338;}

                        else if ( ((LA21_271>='\u0000' && LA21_271<='\u001F')||(LA21_271>='!' && LA21_271<='\uFFFF')) ) {s = 133;}

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA21_484 = input.LA(1);

                        s = -1;
                        if ( (LA21_484==' ') ) {s = 494;}

                        else if ( ((LA21_484>='\u0000' && LA21_484<='\u001F')||(LA21_484>='!' && LA21_484<='\uFFFF')) ) {s = 133;}

                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA21_44 = input.LA(1);

                        s = -1;
                        if ( (LA21_44=='\'') ) {s = 132;}

                        else if ( ((LA21_44>='\u0000' && LA21_44<='&')||(LA21_44>='(' && LA21_44<='\uFFFF')) ) {s = 133;}

                        else s = 47;

                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA21_0 = input.LA(1);

                        s = -1;
                        if ( (LA21_0=='N'||LA21_0=='n') ) {s = 1;}

                        else if ( (LA21_0=='I'||LA21_0=='i') ) {s = 2;}

                        else if ( (LA21_0=='M'||LA21_0=='m') ) {s = 3;}

                        else if ( (LA21_0=='D'||LA21_0=='d') ) {s = 4;}

                        else if ( (LA21_0=='C'||LA21_0=='c') ) {s = 5;}

                        else if ( (LA21_0=='P'||LA21_0=='p') ) {s = 6;}

                        else if ( (LA21_0=='T'||LA21_0=='t') ) {s = 7;}

                        else if ( (LA21_0=='U'||LA21_0=='u') ) {s = 8;}

                        else if ( (LA21_0=='A'||LA21_0=='a') ) {s = 9;}

                        else if ( (LA21_0=='O'||LA21_0=='o') ) {s = 10;}

                        else if ( (LA21_0=='S'||LA21_0=='s') ) {s = 11;}

                        else if ( (LA21_0=='W'||LA21_0=='w') ) {s = 12;}

                        else if ( (LA21_0=='E'||LA21_0=='e') ) {s = 13;}

                        else if ( (LA21_0=='H'||LA21_0=='h') ) {s = 14;}

                        else if ( (LA21_0=='R'||LA21_0=='r') ) {s = 15;}

                        else if ( (LA21_0=='F'||LA21_0=='f') ) {s = 16;}

                        else if ( (LA21_0=='G'||LA21_0=='g') ) {s = 17;}

                        else if ( (LA21_0=='J'||LA21_0=='j') ) {s = 18;}

                        else if ( (LA21_0=='V'||LA21_0=='v') ) {s = 19;}

                        else if ( (LA21_0=='!') ) {s = 20;}

                        else if ( (LA21_0=='(') ) {s = 21;}

                        else if ( (LA21_0=='<') ) {s = 22;}

                        else if ( (LA21_0=='>') ) {s = 23;}

                        else if ( (LA21_0=='B'||LA21_0=='b') ) {s = 24;}

                        else if ( (LA21_0=='$') ) {s = 25;}

                        else if ( (LA21_0==')') ) {s = 26;}

                        else if ( (LA21_0=='*') ) {s = 27;}

                        else if ( (LA21_0=='+') ) {s = 28;}

                        else if ( (LA21_0==',') ) {s = 29;}

                        else if ( (LA21_0=='-') ) {s = 30;}

                        else if ( (LA21_0=='.') ) {s = 31;}

                        else if ( (LA21_0=='/') ) {s = 32;}

                        else if ( (LA21_0==':') ) {s = 33;}

                        else if ( (LA21_0==';') ) {s = 34;}

                        else if ( (LA21_0=='=') ) {s = 35;}

                        else if ( (LA21_0=='[') ) {s = 36;}

                        else if ( (LA21_0==']') ) {s = 37;}

                        else if ( (LA21_0=='^') ) {s = 38;}

                        else if ( (LA21_0=='{') ) {s = 39;}

                        else if ( (LA21_0=='}') ) {s = 40;}

                        else if ( ((LA21_0>='K' && LA21_0<='L')||LA21_0=='Q'||(LA21_0>='X' && LA21_0<='Z')||LA21_0=='_'||(LA21_0>='k' && LA21_0<='l')||LA21_0=='q'||(LA21_0>='x' && LA21_0<='z')) ) {s = 41;}

                        else if ( ((LA21_0>='0' && LA21_0<='1')) ) {s = 42;}

                        else if ( ((LA21_0>='2' && LA21_0<='9')) ) {s = 43;}

                        else if ( (LA21_0=='\'') ) {s = 44;}

                        else if ( (LA21_0=='\"') ) {s = 45;}

                        else if ( ((LA21_0>='\t' && LA21_0<='\n')||LA21_0=='\r'||LA21_0==' ') ) {s = 46;}

                        else if ( ((LA21_0>='\u0000' && LA21_0<='\b')||(LA21_0>='\u000B' && LA21_0<='\f')||(LA21_0>='\u000E' && LA21_0<='\u001F')||LA21_0=='#'||(LA21_0>='%' && LA21_0<='&')||(LA21_0>='?' && LA21_0<='@')||LA21_0=='\\'||LA21_0=='`'||LA21_0=='|'||(LA21_0>='~' && LA21_0<='\uFFFF')) ) {s = 47;}

                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA21_452 = input.LA(1);

                        s = -1;
                        if ( ((LA21_452>='\u0000' && LA21_452<='\uFFFF')) ) {s = 133;}

                        else s = 205;

                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA21_338 = input.LA(1);

                        s = -1;
                        if ( (LA21_338=='\'') ) {s = 386;}

                        else if ( ((LA21_338>='\u0000' && LA21_338<='&')||(LA21_338>='(' && LA21_338<='\uFFFF')) ) {s = 133;}

                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA21_134 = input.LA(1);

                        s = -1;
                        if ( (LA21_134==' ') ) {s = 206;}

                        else if ( ((LA21_134>='\u0000' && LA21_134<='\u001F')||(LA21_134>='!' && LA21_134<='\uFFFF')) ) {s = 133;}

                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA21_206 = input.LA(1);

                        s = -1;
                        if ( (LA21_206=='\\') ) {s = 271;}

                        else if ( ((LA21_206>='\u0000' && LA21_206<='[')||(LA21_206>=']' && LA21_206<='\uFFFF')) ) {s = 133;}

                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA21_451 = input.LA(1);

                        s = -1;
                        if ( (LA21_451==' ') ) {s = 472;}

                        else if ( ((LA21_451>='\u0000' && LA21_451<='\u001F')||(LA21_451>='!' && LA21_451<='\uFFFF')) ) {s = 133;}

                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA21_386 = input.LA(1);

                        s = -1;
                        if ( (LA21_386=='\"') ) {s = 423;}

                        else if ( (LA21_386=='\\') ) {s = 424;}

                        else if ( ((LA21_386>='\u0000' && LA21_386<='!')||(LA21_386>='#' && LA21_386<='[')||(LA21_386>=']' && LA21_386<='\uFFFF')) ) {s = 425;}

                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA21_494 = input.LA(1);

                        s = -1;
                        if ( (LA21_494=='\'') ) {s = 501;}

                        else if ( ((LA21_494>='\u0000' && LA21_494<='&')||(LA21_494>='(' && LA21_494<='\uFFFF')) ) {s = 133;}

                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA21_454 = input.LA(1);

                        s = -1;
                        if ( (LA21_454==' ') ) {s = 472;}

                        else if ( ((LA21_454>='\u0000' && LA21_454<='\u001F')||(LA21_454>='!' && LA21_454<='\uFFFF')) ) {s = 133;}

                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA21_472 = input.LA(1);

                        s = -1;
                        if ( (LA21_472=='\\') ) {s = 484;}

                        else if ( ((LA21_472>='\u0000' && LA21_472<='[')||(LA21_472>=']' && LA21_472<='\uFFFF')) ) {s = 133;}

                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA21_501 = input.LA(1);

                        s = -1;
                        if ( (LA21_501=='\"') ) {s = 505;}

                        else if ( (LA21_501=='\\') ) {s = 506;}

                        else if ( ((LA21_501>='\u0000' && LA21_501<='!')||(LA21_501>='#' && LA21_501<='[')||(LA21_501>=']' && LA21_501<='\uFFFF')) ) {s = 507;}

                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA21_506 = input.LA(1);

                        s = -1;
                        if ( (LA21_506=='\"') ) {s = 452;}

                        else if ( (LA21_506=='\'') ) {s = 451;}

                        else if ( ((LA21_506>='\u0000' && LA21_506<='!')||(LA21_506>='#' && LA21_506<='&')||(LA21_506>='(' && LA21_506<='\uFFFF')) ) {s = 133;}

                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA21_424 = input.LA(1);

                        s = -1;
                        if ( (LA21_424=='\'') ) {s = 451;}

                        else if ( (LA21_424=='\"') ) {s = 452;}

                        else if ( ((LA21_424>='\u0000' && LA21_424<='!')||(LA21_424>='#' && LA21_424<='&')||(LA21_424>='(' && LA21_424<='\uFFFF')) ) {s = 133;}

                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA21_45 = input.LA(1);

                        s = -1;
                        if ( (LA21_45=='\'') ) {s = 134;}

                        else if ( ((LA21_45>='\u0000' && LA21_45<='&')||(LA21_45>='(' && LA21_45<='\uFFFF')) ) {s = 133;}

                        else s = 47;

                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA21_507 = input.LA(1);

                        s = -1;
                        if ( (LA21_507=='\"') ) {s = 453;}

                        else if ( (LA21_507=='\'') ) {s = 454;}

                        else if ( ((LA21_507>='\u0000' && LA21_507<='!')||(LA21_507>='#' && LA21_507<='&')||(LA21_507>='(' && LA21_507<='\uFFFF')) ) {s = 133;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 21, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}