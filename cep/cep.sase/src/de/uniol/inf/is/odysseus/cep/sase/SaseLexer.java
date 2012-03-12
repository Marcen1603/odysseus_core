// $ANTLR 3.4 E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g 2012-03-12 14:39:29
 
/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.cep.sase;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class SaseLexer extends Lexer {
    public static final int EOF=-1;
    public static final int ALT=4;
    public static final int AND=5;
    public static final int AS=6;
    public static final int ASSIGN=7;
    public static final int AVG=8;
    public static final int BBRACKETLEFT=9;
    public static final int BBRACKETRIGHT=10;
    public static final int COMMA=11;
    public static final int COMPAREOP=12;
    public static final int COUNT=13;
    public static final int CREATE=14;
    public static final int DAY=15;
    public static final int DIGIT=16;
    public static final int DIVISION=17;
    public static final int EQUALS=18;
    public static final int FLOAT=19;
    public static final int HOUR=20;
    public static final int INTEGER=21;
    public static final int LBRACKET=22;
    public static final int LEFTCURLY=23;
    public static final int LEN=24;
    public static final int LETTER=25;
    public static final int LOWER=26;
    public static final int MAX=27;
    public static final int MILLISECOND=28;
    public static final int MIN=29;
    public static final int MINUS=30;
    public static final int MINUTE=31;
    public static final int MULT=32;
    public static final int NAME=33;
    public static final int NEWLINE=34;
    public static final int NONCONTROL_CHAR=35;
    public static final int NOTSIGN=36;
    public static final int NUMBER=37;
    public static final int PARTITION_CONTIGUITY=38;
    public static final int PATTERN=39;
    public static final int PLUS=40;
    public static final int POINT=41;
    public static final int RBRACKET=42;
    public static final int RETURN=43;
    public static final int RIGHTCURLY=44;
    public static final int SECOND=45;
    public static final int SEQ=46;
    public static final int SINGLEEQUALS=47;
    public static final int SKIP_TILL_ANY_MATCH=48;
    public static final int SKIP_TILL_NEXT_MATCH=49;
    public static final int SPACE=50;
    public static final int STREAM=51;
    public static final int STRICT_CONTIGUITY=52;
    public static final int STRING_LITERAL=53;
    public static final int SUM=54;
    public static final int UPPER=55;
    public static final int VIEW=56;
    public static final int WEEK=57;
    public static final int WHERE=58;
    public static final int WHITESPACE=59;
    public static final int WITHIN=60;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public SaseLexer() {} 
    public SaseLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public SaseLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g"; }

    // $ANTLR start "CREATE"
    public final void mCREATE() throws RecognitionException {
        try {
            int _type = CREATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:21:9: ( ( 'C' | 'c' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'E' | 'e' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:21:11: ( 'C' | 'c' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'E' | 'e' )
            {
            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CREATE"

    // $ANTLR start "STREAM"
    public final void mSTREAM() throws RecognitionException {
        try {
            int _type = STREAM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:22:9: ( ( 'S' | 's' ) ( 'T' | 't' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'M' | 'm' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:22:11: ( 'S' | 's' ) ( 'T' | 't' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'M' | 'm' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STREAM"

    // $ANTLR start "VIEW"
    public final void mVIEW() throws RecognitionException {
        try {
            int _type = VIEW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:23:5: ( ( 'V' | 'v' ) ( 'I' | 'i' ) ( 'E' | 'e' ) ( 'W' | 'w' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:23:7: ( 'V' | 'v' ) ( 'I' | 'i' ) ( 'E' | 'e' ) ( 'W' | 'w' )
            {
            if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "VIEW"

    // $ANTLR start "PATTERN"
    public final void mPATTERN() throws RecognitionException {
        try {
            int _type = PATTERN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:24:9: ( ( 'P' | 'p' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'N' | 'n' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:24:11: ( 'P' | 'p' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PATTERN"

    // $ANTLR start "WHERE"
    public final void mWHERE() throws RecognitionException {
        try {
            int _type = WHERE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:25:7: ( ( 'W' | 'w' ) ( 'H' | 'h' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'E' | 'e' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:25:9: ( 'W' | 'w' ) ( 'H' | 'h' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'E' | 'e' )
            {
            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WHERE"

    // $ANTLR start "WITHIN"
    public final void mWITHIN() throws RecognitionException {
        try {
            int _type = WITHIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:26:8: ( ( 'W' | 'w' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'H' | 'h' ) ( 'I' | 'i' ) ( 'N' | 'n' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:26:9: ( 'W' | 'w' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'H' | 'h' ) ( 'I' | 'i' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WITHIN"

    // $ANTLR start "RETURN"
    public final void mRETURN() throws RecognitionException {
        try {
            int _type = RETURN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:27:9: ( ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'T' | 't' ) ( 'U' | 'u' ) ( 'R' | 'r' ) ( 'N' | 'n' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:27:11: ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'T' | 't' ) ( 'U' | 'u' ) ( 'R' | 'r' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RETURN"

    // $ANTLR start "SEQ"
    public final void mSEQ() throws RecognitionException {
        try {
            int _type = SEQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:28:5: ( ( 'S' | 's' ) ( 'E' | 'e' ) ( 'Q' | 'q' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:28:7: ( 'S' | 's' ) ( 'E' | 'e' ) ( 'Q' | 'q' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='Q'||input.LA(1)=='q' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SEQ"

    // $ANTLR start "ALT"
    public final void mALT() throws RecognitionException {
        try {
            int _type = ALT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:29:5: ( ( 'A' | 'a' ) ( 'L' | 'l' ) ( 'T' | 't' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:29:6: ( 'A' | 'a' ) ( 'L' | 'l' ) ( 'T' | 't' )
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ALT"

    // $ANTLR start "LEFTCURLY"
    public final void mLEFTCURLY() throws RecognitionException {
        try {
            int _type = LEFTCURLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:30:11: ( '{' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:30:13: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LEFTCURLY"

    // $ANTLR start "RIGHTCURLY"
    public final void mRIGHTCURLY() throws RecognitionException {
        try {
            int _type = RIGHTCURLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:31:12: ( '}' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:31:14: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RIGHTCURLY"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:5: ( ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'D' | 'd' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:32:7: ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'D' | 'd' )
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "AND"

    // $ANTLR start "AS"
    public final void mAS() throws RecognitionException {
        try {
            int _type = AS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:33:4: ( ( 'A' | 'a' ) ( 'S' | 's' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:33:6: ( 'A' | 'a' ) ( 'S' | 's' )
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "AS"

    // $ANTLR start "LEN"
    public final void mLEN() throws RecognitionException {
        try {
            int _type = LEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:34:6: ( ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'N' | 'n' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:34:8: ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LEN"

    // $ANTLR start "BBRACKETLEFT"
    public final void mBBRACKETLEFT() throws RecognitionException {
        try {
            int _type = BBRACKETLEFT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:36:14: ( '[' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:36:17: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "BBRACKETLEFT"

    // $ANTLR start "BBRACKETRIGHT"
    public final void mBBRACKETRIGHT() throws RecognitionException {
        try {
            int _type = BBRACKETRIGHT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:37:15: ( ']' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:37:18: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "BBRACKETRIGHT"

    // $ANTLR start "WEEK"
    public final void mWEEK() throws RecognitionException {
        try {
            int _type = WEEK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:39:5: ( ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'K' | 'k' ) ( 'S' | 's' )? )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:39:7: ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'K' | 'k' ) ( 'S' | 's' )?
            {
            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:39:43: ( 'S' | 's' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='S'||LA1_0=='s') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
                    {
                    if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WEEK"

    // $ANTLR start "DAY"
    public final void mDAY() throws RecognitionException {
        try {
            int _type = DAY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:40:4: ( ( 'D' | 'd' ) ( 'A' | 'a' ) ( 'Y' | 'y' ) ( 'S' | 's' )? )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:40:6: ( 'D' | 'd' ) ( 'A' | 'a' ) ( 'Y' | 'y' ) ( 'S' | 's' )?
            {
            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:40:33: ( 'S' | 's' )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='S'||LA2_0=='s') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
                    {
                    if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DAY"

    // $ANTLR start "HOUR"
    public final void mHOUR() throws RecognitionException {
        try {
            int _type = HOUR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:41:5: ( ( 'H' | 'h' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'R' | 'r' ) ( 'S' | 's' )? )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:41:7: ( 'H' | 'h' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'R' | 'r' ) ( 'S' | 's' )?
            {
            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:41:43: ( 'S' | 's' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='S'||LA3_0=='s') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
                    {
                    if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "HOUR"

    // $ANTLR start "MINUTE"
    public final void mMINUTE() throws RecognitionException {
        try {
            int _type = MINUTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:42:7: ( ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'U' | 'u' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'S' | 's' )? )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:42:9: ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'U' | 'u' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'S' | 's' )?
            {
            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:42:63: ( 'S' | 's' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='S'||LA4_0=='s') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
                    {
                    if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "MINUTE"

    // $ANTLR start "SECOND"
    public final void mSECOND() throws RecognitionException {
        try {
            int _type = SECOND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:43:7: ( ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'S' | 's' )? )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:43:9: ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'S' | 's' )?
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:43:63: ( 'S' | 's' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='S'||LA5_0=='s') ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
                    {
                    if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SECOND"

    // $ANTLR start "MILLISECOND"
    public final void mMILLISECOND() throws RecognitionException {
        try {
            int _type = MILLISECOND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:44:12: ( ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'L' | 'l' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'S' | 's' )? )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:44:13: ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'L' | 'l' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'D' | 'd' ) ( 'S' | 's' )?
            {
            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:44:112: ( 'S' | 's' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='S'||LA6_0=='s') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
                    {
                    if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "MILLISECOND"

    // $ANTLR start "SKIP_TILL_NEXT_MATCH"
    public final void mSKIP_TILL_NEXT_MATCH() throws RecognitionException {
        try {
            int _type = SKIP_TILL_NEXT_MATCH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:21: ( ( 'S' | 's' ) ( 'K' | 'k' ) ( 'I' | 'i' ) ( 'P' | 'p' ) ( '_' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'L' | 'l' ) ( '_' ) ( 'N' | 'n' ) ( 'E' | 'e' ) ( 'X' | 'x' ) ( 'T' | 't' ) ( '_' ) ( 'M' | 'm' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'C' | 'c' ) ( 'H' | 'h' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:23: ( 'S' | 's' ) ( 'K' | 'k' ) ( 'I' | 'i' ) ( 'P' | 'p' ) ( '_' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'L' | 'l' ) ( '_' ) ( 'N' | 'n' ) ( 'E' | 'e' ) ( 'X' | 'x' ) ( 'T' | 't' ) ( '_' ) ( 'M' | 'm' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'C' | 'c' ) ( 'H' | 'h' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:59: ( '_' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:60: '_'
            {
            match('_'); 

            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:100: ( '_' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:101: '_'
            {
            match('_'); 

            }


            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:141: ( '_' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:47:142: '_'
            {
            match('_'); 

            }


            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SKIP_TILL_NEXT_MATCH"

    // $ANTLR start "SKIP_TILL_ANY_MATCH"
    public final void mSKIP_TILL_ANY_MATCH() throws RecognitionException {
        try {
            int _type = SKIP_TILL_ANY_MATCH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:48:20: ( ( 'S' | 's' ) ( 'K' | 'k' ) ( 'I' | 'i' ) ( 'P' | 'p' ) ( '_' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'L' | 'l' ) ( '_' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'Y' | 'y' ) ( '_' ) ( 'M' | 'm' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'C' | 'c' ) ( 'H' | 'h' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:48:24: ( 'S' | 's' ) ( 'K' | 'k' ) ( 'I' | 'i' ) ( 'P' | 'p' ) ( '_' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'L' | 'l' ) ( 'L' | 'l' ) ( '_' ) ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'Y' | 'y' ) ( '_' ) ( 'M' | 'm' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'C' | 'c' ) ( 'H' | 'h' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:48:60: ( '_' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:48:61: '_'
            {
            match('_'); 

            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:48:101: ( '_' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:48:102: '_'
            {
            match('_'); 

            }


            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:48:133: ( '_' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:48:134: '_'
            {
            match('_'); 

            }


            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SKIP_TILL_ANY_MATCH"

    // $ANTLR start "STRICT_CONTIGUITY"
    public final void mSTRICT_CONTIGUITY() throws RecognitionException {
        try {
            int _type = STRICT_CONTIGUITY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:49:18: ( ( 'S' | 's' ) ( 'T' | 't' ) ( 'R' | 'r' ) ( 'I' | 'i' ) ( 'C' | 'c' ) ( 'T' | 't' ) ( '_' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'G' | 'g' ) ( 'U' | 'u' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'Y' | 'y' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:49:22: ( 'S' | 's' ) ( 'T' | 't' ) ( 'R' | 'r' ) ( 'I' | 'i' ) ( 'C' | 'c' ) ( 'T' | 't' ) ( '_' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'G' | 'g' ) ( 'U' | 'u' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'Y' | 'y' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:49:76: ( '_' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:49:77: '_'
            {
            match('_'); 

            }


            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STRICT_CONTIGUITY"

    // $ANTLR start "PARTITION_CONTIGUITY"
    public final void mPARTITION_CONTIGUITY() throws RecognitionException {
        try {
            int _type = PARTITION_CONTIGUITY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:50:21: ( ( 'P' | 'p' ) ( 'A' | 'a' ) ( 'R' | 'r' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( '_' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'G' | 'g' ) ( 'U' | 'u' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'Y' | 'y' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:50:25: ( 'P' | 'p' ) ( 'A' | 'a' ) ( 'R' | 'r' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( '_' ) ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'G' | 'g' ) ( 'U' | 'u' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'Y' | 'y' )
            {
            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:50:106: ( '_' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:50:107: '_'
            {
            match('_'); 

            }


            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PARTITION_CONTIGUITY"

    // $ANTLR start "AVG"
    public final void mAVG() throws RecognitionException {
        try {
            int _type = AVG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:52:5: ( ( 'A' | 'a' ) ( 'V' | 'v' ) ( 'G' | 'g' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:52:7: ( 'A' | 'a' ) ( 'V' | 'v' ) ( 'G' | 'g' )
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "AVG"

    // $ANTLR start "MIN"
    public final void mMIN() throws RecognitionException {
        try {
            int _type = MIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:53:5: ( ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'N' | 'n' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:53:7: ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "MIN"

    // $ANTLR start "MAX"
    public final void mMAX() throws RecognitionException {
        try {
            int _type = MAX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:54:5: ( ( 'M' | 'm' ) ( 'A' | 'a' ) ( 'X' | 'x' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:54:7: ( 'M' | 'm' ) ( 'A' | 'a' ) ( 'X' | 'x' )
            {
            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "MAX"

    // $ANTLR start "SUM"
    public final void mSUM() throws RecognitionException {
        try {
            int _type = SUM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:55:5: ( ( 'S' | 's' ) ( 'U' | 'u' ) ( 'M' | 'm' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:55:7: ( 'S' | 's' ) ( 'U' | 'u' ) ( 'M' | 'm' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SUM"

    // $ANTLR start "COUNT"
    public final void mCOUNT() throws RecognitionException {
        try {
            int _type = COUNT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:56:7: ( ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'T' | 't' ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:56:9: ( 'C' | 'c' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'T' | 't' )
            {
            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COUNT"

    // $ANTLR start "PLUS"
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:59:6: ( '+' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:59:8: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PLUS"

    // $ANTLR start "MINUS"
    public final void mMINUS() throws RecognitionException {
        try {
            int _type = MINUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:60:7: ( '-' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:60:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "MINUS"

    // $ANTLR start "POINT"
    public final void mPOINT() throws RecognitionException {
        try {
            int _type = POINT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:61:7: ( '.' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:61:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "POINT"

    // $ANTLR start "DIVISION"
    public final void mDIVISION() throws RecognitionException {
        try {
            int _type = DIVISION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:62:9: ( '/' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:62:11: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DIVISION"

    // $ANTLR start "MULT"
    public final void mMULT() throws RecognitionException {
        try {
            int _type = MULT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:63:6: ( '*' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:63:8: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "MULT"

    // $ANTLR start "COMPAREOP"
    public final void mCOMPAREOP() throws RecognitionException {
        try {
            int _type = COMPAREOP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:65:11: ( '<=' | '>=' | '!=' | '<' | '>' )
            int alt7=5;
            switch ( input.LA(1) ) {
            case '<':
                {
                int LA7_1 = input.LA(2);

                if ( (LA7_1=='=') ) {
                    alt7=1;
                }
                else {
                    alt7=4;
                }
                }
                break;
            case '>':
                {
                int LA7_2 = input.LA(2);

                if ( (LA7_2=='=') ) {
                    alt7=2;
                }
                else {
                    alt7=5;
                }
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:65:13: '<='
                    {
                    match("<="); 



                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:65:18: '>='
                    {
                    match(">="); 



                    }
                    break;
                case 3 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:65:23: '!='
                    {
                    match("!="); 



                    }
                    break;
                case 4 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:65:28: '<'
                    {
                    match('<'); 

                    }
                    break;
                case 5 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:65:32: '>'
                    {
                    match('>'); 

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COMPAREOP"

    // $ANTLR start "SINGLEEQUALS"
    public final void mSINGLEEQUALS() throws RecognitionException {
        try {
            int _type = SINGLEEQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:66:14: ( '=' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:66:16: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SINGLEEQUALS"

    // $ANTLR start "EQUALS"
    public final void mEQUALS() throws RecognitionException {
        try {
            int _type = EQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:67:9: ( '==' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:67:11: '=='
            {
            match("=="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "EQUALS"

    // $ANTLR start "ASSIGN"
    public final void mASSIGN() throws RecognitionException {
        try {
            int _type = ASSIGN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:68:9: ( ':=' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:68:11: ':='
            {
            match(":="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ASSIGN"

    // $ANTLR start "NOTSIGN"
    public final void mNOTSIGN() throws RecognitionException {
        try {
            int _type = NOTSIGN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:70:9: ( '~' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:70:11: '~'
            {
            match('~'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NOTSIGN"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:72:7: ( ',' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:72:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "LBRACKET"
    public final void mLBRACKET() throws RecognitionException {
        try {
            int _type = LBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:75:2: ( '(' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:75:4: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LBRACKET"

    // $ANTLR start "RBRACKET"
    public final void mRBRACKET() throws RecognitionException {
        try {
            int _type = RBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:77:2: ( ')' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:77:4: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RBRACKET"

    // $ANTLR start "NUMBER"
    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:80:8: ( INTEGER | FLOAT )
            int alt8=2;
            alt8 = dfa8.predict(input);
            switch (alt8) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:80:11: INTEGER
                    {
                    mINTEGER(); 


                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:80:21: FLOAT
                    {
                    mFLOAT(); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NUMBER"

    // $ANTLR start "FLOAT"
    public final void mFLOAT() throws RecognitionException {
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:81:16: ( INTEGER '.' ( DIGIT )+ )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:81:18: INTEGER '.' ( DIGIT )+
            {
            mINTEGER(); 


            match('.'); 

            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:81:30: ( DIGIT )+
            int cnt9=0;
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0 >= '0' && LA9_0 <= '9')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


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
        	// do for sure before leaving
        }
    }
    // $ANTLR end "FLOAT"

    // $ANTLR start "INTEGER"
    public final void mINTEGER() throws RecognitionException {
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:82:18: ( '0' | '1' .. '9' ( '0' .. '9' )* )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0=='0') ) {
                alt11=1;
            }
            else if ( ((LA11_0 >= '1' && LA11_0 <= '9')) ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }
            switch (alt11) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:82:20: '0'
                    {
                    match('0'); 

                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:82:26: '1' .. '9' ( '0' .. '9' )*
                    {
                    matchRange('1','9'); 

                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:82:35: ( '0' .. '9' )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( ((LA10_0 >= '0' && LA10_0 <= '9')) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
                    	    {
                    	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


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
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INTEGER"

    // $ANTLR start "NAME"
    public final void mNAME() throws RecognitionException {
        try {
            int _type = NAME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:84:6: ( LETTER ( LETTER | DIGIT | '_' | ':' )* )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:84:8: LETTER ( LETTER | DIGIT | '_' | ':' )*
            {
            mLETTER(); 


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:84:15: ( LETTER | DIGIT | '_' | ':' )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0 >= '0' && LA12_0 <= ':')||(LA12_0 >= 'A' && LA12_0 <= 'Z')||LA12_0=='_'||(LA12_0 >= 'a' && LA12_0 <= 'z')) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= ':')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


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
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NAME"

    // $ANTLR start "STRING_LITERAL"
    public final void mSTRING_LITERAL() throws RecognitionException {
        try {
            int _type = STRING_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:85:16: ( '\"' ( NONCONTROL_CHAR )* '\"' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:85:18: '\"' ( NONCONTROL_CHAR )* '\"'
            {
            match('\"'); 

            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:85:22: ( NONCONTROL_CHAR )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0=='\t'||LA13_0==' '||(LA13_0 >= '0' && LA13_0 <= '9')||(LA13_0 >= 'A' && LA13_0 <= 'Z')||(LA13_0 >= 'a' && LA13_0 <= 'z')) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' '||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


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
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STRING_LITERAL"

    // $ANTLR start "NONCONTROL_CHAR"
    public final void mNONCONTROL_CHAR() throws RecognitionException {
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:87:26: ( LETTER | DIGIT | SPACE )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
            {
            if ( input.LA(1)=='\t'||input.LA(1)==' '||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NONCONTROL_CHAR"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:88:17: ( LOWER | UPPER )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LETTER"

    // $ANTLR start "LOWER"
    public final void mLOWER() throws RecognitionException {
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:89:17: ( 'a' .. 'z' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
            {
            if ( (input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LOWER"

    // $ANTLR start "UPPER"
    public final void mUPPER() throws RecognitionException {
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:90:17: ( 'A' .. 'Z' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "UPPER"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:91:17: ( '0' .. '9' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "SPACE"
    public final void mSPACE() throws RecognitionException {
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:92:17: ( ' ' | '\\t' )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
            {
            if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SPACE"

    // $ANTLR start "NEWLINE"
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:94:9: ( ( ( '\\r' )? '\\n' )+ )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:94:11: ( ( '\\r' )? '\\n' )+
            {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:94:11: ( ( '\\r' )? '\\n' )+
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
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:94:12: ( '\\r' )? '\\n'
            	    {
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:94:12: ( '\\r' )?
            	    int alt14=2;
            	    int LA14_0 = input.LA(1);

            	    if ( (LA14_0=='\r') ) {
            	        alt14=1;
            	    }
            	    switch (alt14) {
            	        case 1 :
            	            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:94:12: '\\r'
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
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NEWLINE"

    // $ANTLR start "WHITESPACE"
    public final void mWHITESPACE() throws RecognitionException {
        try {
            int _type = WHITESPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:96:12: ( ( SPACE )+ )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:96:14: ( SPACE )+
            {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:96:14: ( SPACE )+
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
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
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


            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WHITESPACE"

    public void mTokens() throws RecognitionException {
        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:8: ( CREATE | STREAM | VIEW | PATTERN | WHERE | WITHIN | RETURN | SEQ | ALT | LEFTCURLY | RIGHTCURLY | AND | AS | LEN | BBRACKETLEFT | BBRACKETRIGHT | WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND | SKIP_TILL_NEXT_MATCH | SKIP_TILL_ANY_MATCH | STRICT_CONTIGUITY | PARTITION_CONTIGUITY | AVG | MIN | MAX | SUM | COUNT | PLUS | MINUS | POINT | DIVISION | MULT | COMPAREOP | SINGLEEQUALS | EQUALS | ASSIGN | NOTSIGN | COMMA | LBRACKET | RBRACKET | NUMBER | NAME | STRING_LITERAL | NEWLINE | WHITESPACE )
        int alt17=49;
        alt17 = dfa17.predict(input);
        switch (alt17) {
            case 1 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:10: CREATE
                {
                mCREATE(); 


                }
                break;
            case 2 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:17: STREAM
                {
                mSTREAM(); 


                }
                break;
            case 3 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:24: VIEW
                {
                mVIEW(); 


                }
                break;
            case 4 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:29: PATTERN
                {
                mPATTERN(); 


                }
                break;
            case 5 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:37: WHERE
                {
                mWHERE(); 


                }
                break;
            case 6 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:43: WITHIN
                {
                mWITHIN(); 


                }
                break;
            case 7 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:50: RETURN
                {
                mRETURN(); 


                }
                break;
            case 8 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:57: SEQ
                {
                mSEQ(); 


                }
                break;
            case 9 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:61: ALT
                {
                mALT(); 


                }
                break;
            case 10 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:65: LEFTCURLY
                {
                mLEFTCURLY(); 


                }
                break;
            case 11 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:75: RIGHTCURLY
                {
                mRIGHTCURLY(); 


                }
                break;
            case 12 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:86: AND
                {
                mAND(); 


                }
                break;
            case 13 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:90: AS
                {
                mAS(); 


                }
                break;
            case 14 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:93: LEN
                {
                mLEN(); 


                }
                break;
            case 15 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:97: BBRACKETLEFT
                {
                mBBRACKETLEFT(); 


                }
                break;
            case 16 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:110: BBRACKETRIGHT
                {
                mBBRACKETRIGHT(); 


                }
                break;
            case 17 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:124: WEEK
                {
                mWEEK(); 


                }
                break;
            case 18 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:129: DAY
                {
                mDAY(); 


                }
                break;
            case 19 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:133: HOUR
                {
                mHOUR(); 


                }
                break;
            case 20 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:138: MINUTE
                {
                mMINUTE(); 


                }
                break;
            case 21 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:145: SECOND
                {
                mSECOND(); 


                }
                break;
            case 22 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:152: MILLISECOND
                {
                mMILLISECOND(); 


                }
                break;
            case 23 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:164: SKIP_TILL_NEXT_MATCH
                {
                mSKIP_TILL_NEXT_MATCH(); 


                }
                break;
            case 24 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:185: SKIP_TILL_ANY_MATCH
                {
                mSKIP_TILL_ANY_MATCH(); 


                }
                break;
            case 25 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:205: STRICT_CONTIGUITY
                {
                mSTRICT_CONTIGUITY(); 


                }
                break;
            case 26 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:223: PARTITION_CONTIGUITY
                {
                mPARTITION_CONTIGUITY(); 


                }
                break;
            case 27 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:244: AVG
                {
                mAVG(); 


                }
                break;
            case 28 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:248: MIN
                {
                mMIN(); 


                }
                break;
            case 29 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:252: MAX
                {
                mMAX(); 


                }
                break;
            case 30 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:256: SUM
                {
                mSUM(); 


                }
                break;
            case 31 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:260: COUNT
                {
                mCOUNT(); 


                }
                break;
            case 32 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:266: PLUS
                {
                mPLUS(); 


                }
                break;
            case 33 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:271: MINUS
                {
                mMINUS(); 


                }
                break;
            case 34 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:277: POINT
                {
                mPOINT(); 


                }
                break;
            case 35 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:283: DIVISION
                {
                mDIVISION(); 


                }
                break;
            case 36 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:292: MULT
                {
                mMULT(); 


                }
                break;
            case 37 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:297: COMPAREOP
                {
                mCOMPAREOP(); 


                }
                break;
            case 38 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:307: SINGLEEQUALS
                {
                mSINGLEEQUALS(); 


                }
                break;
            case 39 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:320: EQUALS
                {
                mEQUALS(); 


                }
                break;
            case 40 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:327: ASSIGN
                {
                mASSIGN(); 


                }
                break;
            case 41 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:334: NOTSIGN
                {
                mNOTSIGN(); 


                }
                break;
            case 42 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:342: COMMA
                {
                mCOMMA(); 


                }
                break;
            case 43 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:348: LBRACKET
                {
                mLBRACKET(); 


                }
                break;
            case 44 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:357: RBRACKET
                {
                mRBRACKET(); 


                }
                break;
            case 45 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:366: NUMBER
                {
                mNUMBER(); 


                }
                break;
            case 46 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:373: NAME
                {
                mNAME(); 


                }
                break;
            case 47 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:378: STRING_LITERAL
                {
                mSTRING_LITERAL(); 


                }
                break;
            case 48 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:393: NEWLINE
                {
                mNEWLINE(); 


                }
                break;
            case 49 :
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseLexer.g:1:401: WHITESPACE
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
            return "80:1: NUMBER : ( INTEGER | FLOAT );";
        }
    }
    static final String DFA17_eotS =
        "\1\uffff\7\35\2\uffff\1\35\2\uffff\3\35\6\uffff\1\67\12\uffff\16"+
        "\35\1\110\6\35\2\uffff\3\35\1\124\2\35\1\127\7\35\1\137\1\140\1"+
        "\uffff\1\141\1\142\1\144\1\35\1\147\1\35\1\151\4\35\1\uffff\2\35"+
        "\1\uffff\1\160\4\35\1\166\1\35\4\uffff\1\144\1\uffff\1\171\1\35"+
        "\1\uffff\1\35\1\uffff\1\35\1\175\4\35\1\uffff\2\35\1\u0084\1\35"+
        "\1\166\1\uffff\1\35\1\171\1\uffff\2\35\1\u0089\1\uffff\1\u008a\1"+
        "\35\1\u008d\3\35\1\uffff\1\u0091\1\u0092\1\u0094\1\35\2\uffff\1"+
        "\35\1\u008d\1\uffff\1\35\1\u0098\1\35\2\uffff\1\u0094\1\uffff\3"+
        "\35\1\uffff\16\35\1\u00ad\4\35\1\u00ad\1\uffff\20\35\1\u00c2\3\35"+
        "\1\uffff\4\35\1\u00ca\1\35\1\u00cc\1\uffff\1\u00cd\2\uffff";
    static final String DFA17_eofS =
        "\u00ce\uffff";
    static final String DFA17_minS =
        "\1\11\1\117\1\105\1\111\1\101\2\105\1\114\2\uffff\1\105\2\uffff"+
        "\1\101\1\117\1\101\6\uffff\1\75\12\uffff\1\105\1\125\1\122\1\103"+
        "\1\111\1\115\1\105\1\122\1\105\1\124\1\105\2\124\1\104\1\60\1\107"+
        "\1\116\1\131\1\125\1\114\1\130\2\uffff\1\101\1\116\1\105\1\60\1"+
        "\117\1\120\1\60\1\127\2\124\1\122\1\110\1\113\1\125\2\60\1\uffff"+
        "\3\60\1\122\1\60\1\114\1\60\2\124\1\101\1\103\1\uffff\1\116\1\137"+
        "\1\uffff\1\60\1\105\1\111\1\105\1\111\1\60\1\122\4\uffff\1\60\1"+
        "\uffff\1\60\1\124\1\uffff\1\111\1\uffff\1\105\1\60\1\115\1\124\1"+
        "\104\1\124\1\uffff\1\122\1\124\1\60\1\116\1\60\1\uffff\1\116\1\60"+
        "\1\uffff\1\105\1\123\1\60\1\uffff\1\60\1\137\1\60\1\111\1\116\1"+
        "\111\1\uffff\3\60\1\105\2\uffff\1\103\1\60\1\uffff\1\114\1\60\1"+
        "\117\2\uffff\1\60\1\uffff\1\103\1\117\1\114\1\uffff\1\116\1\117"+
        "\1\116\2\137\1\116\1\124\1\101\1\103\1\104\1\111\1\105\1\116\1\117"+
        "\1\60\1\107\1\130\1\131\1\116\1\60\1\uffff\1\125\1\124\1\137\1\124"+
        "\1\111\1\137\1\115\1\111\1\124\1\115\1\101\1\107\1\131\1\101\1\124"+
        "\1\125\1\60\1\124\1\103\1\111\1\uffff\1\103\1\110\1\124\1\110\1"+
        "\60\1\131\1\60\1\uffff\1\60\2\uffff";
    static final String DFA17_maxS =
        "\1\176\1\162\1\165\1\151\1\141\1\151\1\145\1\166\2\uffff\1\145\2"+
        "\uffff\1\141\1\157\1\151\6\uffff\1\75\12\uffff\1\145\1\165\1\162"+
        "\1\161\1\151\1\155\1\145\1\164\1\145\1\164\1\145\2\164\1\144\1\172"+
        "\1\147\1\156\1\171\1\165\1\156\1\170\2\uffff\1\141\1\156\1\151\1"+
        "\172\1\157\1\160\1\172\1\167\2\164\1\162\1\150\1\153\1\165\2\172"+
        "\1\uffff\3\172\1\162\1\172\1\154\1\172\2\164\1\141\1\143\1\uffff"+
        "\1\156\1\137\1\uffff\1\172\1\145\1\151\1\145\1\151\1\172\1\162\4"+
        "\uffff\1\172\1\uffff\1\172\1\164\1\uffff\1\151\1\uffff\1\145\1\172"+
        "\1\155\1\164\1\144\1\164\1\uffff\1\162\1\164\1\172\1\156\1\172\1"+
        "\uffff\1\156\1\172\1\uffff\1\145\1\163\1\172\1\uffff\1\172\1\137"+
        "\1\172\1\151\1\156\1\151\1\uffff\3\172\1\145\2\uffff\1\143\1\172"+
        "\1\uffff\1\154\1\172\1\157\2\uffff\1\172\1\uffff\1\143\1\157\1\154"+
        "\1\uffff\1\156\1\157\1\156\2\137\1\156\1\164\1\156\1\143\1\144\1"+
        "\151\1\145\1\156\1\157\1\172\1\147\1\170\1\171\1\156\1\172\1\uffff"+
        "\1\165\1\164\1\137\1\164\1\151\1\137\1\155\1\151\1\164\1\155\1\141"+
        "\1\147\1\171\1\141\1\164\1\165\1\172\1\164\1\143\1\151\1\uffff\1"+
        "\143\1\150\1\164\1\150\1\172\1\171\1\172\1\uffff\1\172\2\uffff";
    static final String DFA17_acceptS =
        "\10\uffff\1\12\1\13\1\uffff\1\17\1\20\3\uffff\1\40\1\41\1\42\1\43"+
        "\1\44\1\45\1\uffff\1\50\1\51\1\52\1\53\1\54\1\55\1\56\1\57\1\60"+
        "\1\61\25\uffff\1\47\1\46\20\uffff\1\15\13\uffff\1\10\2\uffff\1\36"+
        "\7\uffff\1\11\1\14\1\33\1\16\1\uffff\1\22\2\uffff\1\34\1\uffff\1"+
        "\35\6\uffff\1\3\5\uffff\1\21\2\uffff\1\23\3\uffff\1\37\6\uffff\1"+
        "\5\4\uffff\1\1\1\2\2\uffff\1\25\3\uffff\1\6\1\7\1\uffff\1\24\3\uffff"+
        "\1\4\24\uffff\1\26\24\uffff\1\31\7\uffff\1\30\1\uffff\1\27\1\32";
    static final String DFA17_specialS =
        "\u00ce\uffff}>";
    static final String[] DFA17_transitionS = {
            "\1\40\1\37\2\uffff\1\37\22\uffff\1\40\1\25\1\36\5\uffff\1\32"+
            "\1\33\1\24\1\20\1\31\1\21\1\22\1\23\12\34\1\27\1\uffff\1\25"+
            "\1\26\1\25\2\uffff\1\7\1\35\1\1\1\15\3\35\1\16\3\35\1\12\1\17"+
            "\2\35\1\4\1\35\1\6\1\2\2\35\1\3\1\5\3\35\1\13\1\uffff\1\14\3"+
            "\uffff\1\7\1\35\1\1\1\15\3\35\1\16\3\35\1\12\1\17\2\35\1\4\1"+
            "\35\1\6\1\2\2\35\1\3\1\5\3\35\1\10\1\uffff\1\11\1\30",
            "\1\42\2\uffff\1\41\34\uffff\1\42\2\uffff\1\41",
            "\1\44\5\uffff\1\45\10\uffff\1\43\1\46\17\uffff\1\44\5\uffff"+
            "\1\45\10\uffff\1\43\1\46",
            "\1\47\37\uffff\1\47",
            "\1\50\37\uffff\1\50",
            "\1\53\2\uffff\1\51\1\52\33\uffff\1\53\2\uffff\1\51\1\52",
            "\1\54\37\uffff\1\54",
            "\1\55\1\uffff\1\56\4\uffff\1\57\2\uffff\1\60\25\uffff\1\55"+
            "\1\uffff\1\56\4\uffff\1\57\2\uffff\1\60",
            "",
            "",
            "\1\61\37\uffff\1\61",
            "",
            "",
            "\1\62\37\uffff\1\62",
            "\1\63\37\uffff\1\63",
            "\1\65\7\uffff\1\64\27\uffff\1\65\7\uffff\1\64",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\66",
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
            "\1\70\37\uffff\1\70",
            "\1\71\37\uffff\1\71",
            "\1\72\37\uffff\1\72",
            "\1\74\15\uffff\1\73\21\uffff\1\74\15\uffff\1\73",
            "\1\75\37\uffff\1\75",
            "\1\76\37\uffff\1\76",
            "\1\77\37\uffff\1\77",
            "\1\101\1\uffff\1\100\35\uffff\1\101\1\uffff\1\100",
            "\1\102\37\uffff\1\102",
            "\1\103\37\uffff\1\103",
            "\1\104\37\uffff\1\104",
            "\1\105\37\uffff\1\105",
            "\1\106\37\uffff\1\106",
            "\1\107\37\uffff\1\107",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\111\37\uffff\1\111",
            "\1\112\37\uffff\1\112",
            "\1\113\37\uffff\1\113",
            "\1\114\37\uffff\1\114",
            "\1\116\1\uffff\1\115\35\uffff\1\116\1\uffff\1\115",
            "\1\117\37\uffff\1\117",
            "",
            "",
            "\1\120\37\uffff\1\120",
            "\1\121\37\uffff\1\121",
            "\1\122\3\uffff\1\123\33\uffff\1\122\3\uffff\1\123",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\125\37\uffff\1\125",
            "\1\126\37\uffff\1\126",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\130\37\uffff\1\130",
            "\1\131\37\uffff\1\131",
            "\1\132\37\uffff\1\132",
            "\1\133\37\uffff\1\133",
            "\1\134\37\uffff\1\134",
            "\1\135\37\uffff\1\135",
            "\1\136\37\uffff\1\136",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\13\35\6\uffff\22\35\1\143\7\35\4\uffff\1\35\1\uffff\22\35"+
            "\1\143\7\35",
            "\1\145\37\uffff\1\145",
            "\13\35\6\uffff\24\35\1\146\5\35\4\uffff\1\35\1\uffff\24\35"+
            "\1\146\5\35",
            "\1\150\37\uffff\1\150",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\152\37\uffff\1\152",
            "\1\153\37\uffff\1\153",
            "\1\154\37\uffff\1\154",
            "\1\155\37\uffff\1\155",
            "",
            "\1\156\37\uffff\1\156",
            "\1\157",
            "",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\161\37\uffff\1\161",
            "\1\162\37\uffff\1\162",
            "\1\163\37\uffff\1\163",
            "\1\164\37\uffff\1\164",
            "\13\35\6\uffff\22\35\1\165\7\35\4\uffff\1\35\1\uffff\22\35"+
            "\1\165\7\35",
            "\1\167\37\uffff\1\167",
            "",
            "",
            "",
            "",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "",
            "\13\35\6\uffff\22\35\1\170\7\35\4\uffff\1\35\1\uffff\22\35"+
            "\1\170\7\35",
            "\1\172\37\uffff\1\172",
            "",
            "\1\173\37\uffff\1\173",
            "",
            "\1\174\37\uffff\1\174",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\176\37\uffff\1\176",
            "\1\177\37\uffff\1\177",
            "\1\u0080\37\uffff\1\u0080",
            "\1\u0081\37\uffff\1\u0081",
            "",
            "\1\u0082\37\uffff\1\u0082",
            "\1\u0083\37\uffff\1\u0083",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\u0085\37\uffff\1\u0085",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "",
            "\1\u0086\37\uffff\1\u0086",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "",
            "\1\u0087\37\uffff\1\u0087",
            "\1\u0088\37\uffff\1\u0088",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\u008b",
            "\13\35\6\uffff\22\35\1\u008c\7\35\4\uffff\1\35\1\uffff\22\35"+
            "\1\u008c\7\35",
            "\1\u008e\37\uffff\1\u008e",
            "\1\u008f\37\uffff\1\u008f",
            "\1\u0090\37\uffff\1\u0090",
            "",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\13\35\6\uffff\22\35\1\u0093\7\35\4\uffff\1\35\1\uffff\22\35"+
            "\1\u0093\7\35",
            "\1\u0095\37\uffff\1\u0095",
            "",
            "",
            "\1\u0096\37\uffff\1\u0096",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "",
            "\1\u0097\37\uffff\1\u0097",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\u0099\37\uffff\1\u0099",
            "",
            "",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "",
            "\1\u009a\37\uffff\1\u009a",
            "\1\u009b\37\uffff\1\u009b",
            "\1\u009c\37\uffff\1\u009c",
            "",
            "\1\u009d\37\uffff\1\u009d",
            "\1\u009e\37\uffff\1\u009e",
            "\1\u009f\37\uffff\1\u009f",
            "\1\u00a0",
            "\1\u00a1",
            "\1\u00a2\37\uffff\1\u00a2",
            "\1\u00a3\37\uffff\1\u00a3",
            "\1\u00a5\14\uffff\1\u00a4\22\uffff\1\u00a5\14\uffff\1\u00a4",
            "\1\u00a6\37\uffff\1\u00a6",
            "\1\u00a7\37\uffff\1\u00a7",
            "\1\u00a8\37\uffff\1\u00a8",
            "\1\u00a9\37\uffff\1\u00a9",
            "\1\u00aa\37\uffff\1\u00aa",
            "\1\u00ab\37\uffff\1\u00ab",
            "\13\35\6\uffff\22\35\1\u00ac\7\35\4\uffff\1\35\1\uffff\22\35"+
            "\1\u00ac\7\35",
            "\1\u00ae\37\uffff\1\u00ae",
            "\1\u00af\37\uffff\1\u00af",
            "\1\u00b0\37\uffff\1\u00b0",
            "\1\u00b1\37\uffff\1\u00b1",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "",
            "\1\u00b2\37\uffff\1\u00b2",
            "\1\u00b3\37\uffff\1\u00b3",
            "\1\u00b4",
            "\1\u00b5\37\uffff\1\u00b5",
            "\1\u00b6\37\uffff\1\u00b6",
            "\1\u00b7",
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
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\u00c3\37\uffff\1\u00c3",
            "\1\u00c4\37\uffff\1\u00c4",
            "\1\u00c5\37\uffff\1\u00c5",
            "",
            "\1\u00c6\37\uffff\1\u00c6",
            "\1\u00c7\37\uffff\1\u00c7",
            "\1\u00c8\37\uffff\1\u00c8",
            "\1\u00c9\37\uffff\1\u00c9",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\u00cb\37\uffff\1\u00cb",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "",
            "\13\35\6\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
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
            return "1:1: Tokens : ( CREATE | STREAM | VIEW | PATTERN | WHERE | WITHIN | RETURN | SEQ | ALT | LEFTCURLY | RIGHTCURLY | AND | AS | LEN | BBRACKETLEFT | BBRACKETRIGHT | WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND | SKIP_TILL_NEXT_MATCH | SKIP_TILL_ANY_MATCH | STRICT_CONTIGUITY | PARTITION_CONTIGUITY | AVG | MIN | MAX | SUM | COUNT | PLUS | MINUS | POINT | DIVISION | MULT | COMPAREOP | SINGLEEQUALS | EQUALS | ASSIGN | NOTSIGN | COMMA | LBRACKET | RBRACKET | NUMBER | NAME | STRING_LITERAL | NEWLINE | WHITESPACE );";
        }
    }
 

}