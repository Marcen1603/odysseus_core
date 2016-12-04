package de.uniol.inf.is.odysseus.parser.novel.cql.ide.contentassist.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalCQLLexer extends Lexer {
    public static final int T__50=50;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__59=59;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__55=55;
    public static final int T__12=12;
    public static final int T__56=56;
    public static final int T__13=13;
    public static final int T__57=57;
    public static final int T__14=14;
    public static final int T__58=58;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__60=60;
    public static final int T__61=61;
    public static final int RULE_ID=7;
    public static final int RULE_FLOAT_NUMBER=5;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=4;
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
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__42=42;
    public static final int T__43=43;

    // delegates
    // delegators

    public InternalCQLLexer() {;} 
    public InternalCQLLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InternalCQLLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "InternalCQL.g"; }

    // $ANTLR start "T__12"
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:11:7: ( 'UNBOUNDED' )
            // InternalCQL.g:11:9: 'UNBOUNDED'
            {
            match("UNBOUNDED"); 


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
            // InternalCQL.g:12:7: ( 'TRUE' )
            // InternalCQL.g:12:9: 'TRUE'
            {
            match("TRUE"); 


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
            // InternalCQL.g:13:7: ( 'FALSE' )
            // InternalCQL.g:13:9: 'FALSE'
            {
            match("FALSE"); 


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
            // InternalCQL.g:14:7: ( 'INTEGER' )
            // InternalCQL.g:14:9: 'INTEGER'
            {
            match("INTEGER"); 


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
            // InternalCQL.g:15:7: ( 'DOUBLE' )
            // InternalCQL.g:15:9: 'DOUBLE'
            {
            match("DOUBLE"); 


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
            // InternalCQL.g:16:7: ( 'FLOAT' )
            // InternalCQL.g:16:9: 'FLOAT'
            {
            match("FLOAT"); 


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
            // InternalCQL.g:17:7: ( 'STRING' )
            // InternalCQL.g:17:9: 'STRING'
            {
            match("STRING"); 


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
            // InternalCQL.g:18:7: ( 'BOOLEAN' )
            // InternalCQL.g:18:9: 'BOOLEAN'
            {
            match("BOOLEAN"); 


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
            // InternalCQL.g:19:7: ( 'STARTTIMESTAMP' )
            // InternalCQL.g:19:9: 'STARTTIMESTAMP'
            {
            match("STARTTIMESTAMP"); 


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
            // InternalCQL.g:20:7: ( 'ENDTIMESTAMP' )
            // InternalCQL.g:20:9: 'ENDTIMESTAMP'
            {
            match("ENDTIMESTAMP"); 


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
            // InternalCQL.g:21:7: ( '==' )
            // InternalCQL.g:21:9: '=='
            {
            match("=="); 


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
            // InternalCQL.g:22:7: ( '!=' )
            // InternalCQL.g:22:9: '!='
            {
            match("!="); 


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
            // InternalCQL.g:23:7: ( '>=' )
            // InternalCQL.g:23:9: '>='
            {
            match(">="); 


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
            // InternalCQL.g:24:7: ( '<=' )
            // InternalCQL.g:24:9: '<='
            {
            match("<="); 


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
            // InternalCQL.g:25:7: ( '<' )
            // InternalCQL.g:25:9: '<'
            {
            match('<'); 

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
            // InternalCQL.g:26:7: ( '>' )
            // InternalCQL.g:26:9: '>'
            {
            match('>'); 

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
            // InternalCQL.g:27:7: ( '*' )
            // InternalCQL.g:27:9: '*'
            {
            match('*'); 

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
            // InternalCQL.g:28:7: ( '/' )
            // InternalCQL.g:28:9: '/'
            {
            match('/'); 

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
            // InternalCQL.g:29:7: ( 'STREAM' )
            // InternalCQL.g:29:9: 'STREAM'
            {
            match("STREAM"); 


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
            // InternalCQL.g:30:7: ( 'VIEW' )
            // InternalCQL.g:30:9: 'VIEW'
            {
            match("VIEW"); 


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
            // InternalCQL.g:31:7: ( 'SINK' )
            // InternalCQL.g:31:9: 'SINK'
            {
            match("SINK"); 


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
            // InternalCQL.g:32:7: ( ';' )
            // InternalCQL.g:32:9: ';'
            {
            match(';'); 

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
            // InternalCQL.g:33:7: ( '[' )
            // InternalCQL.g:33:9: '['
            {
            match('['); 

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
            // InternalCQL.g:34:7: ( ']' )
            // InternalCQL.g:34:9: ']'
            {
            match(']'); 

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
            // InternalCQL.g:35:7: ( 'OR' )
            // InternalCQL.g:35:9: 'OR'
            {
            match("OR"); 


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
            // InternalCQL.g:36:7: ( 'AND' )
            // InternalCQL.g:36:9: 'AND'
            {
            match("AND"); 


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
            // InternalCQL.g:37:7: ( '+' )
            // InternalCQL.g:37:9: '+'
            {
            match('+'); 

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
            // InternalCQL.g:38:7: ( '-' )
            // InternalCQL.g:38:9: '-'
            {
            match('-'); 

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
            // InternalCQL.g:39:7: ( '(' )
            // InternalCQL.g:39:9: '('
            {
            match('('); 

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
            // InternalCQL.g:40:7: ( ')' )
            // InternalCQL.g:40:9: ')'
            {
            match(')'); 

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
            // InternalCQL.g:41:7: ( 'NOT' )
            // InternalCQL.g:41:9: 'NOT'
            {
            match("NOT"); 


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
            // InternalCQL.g:42:7: ( 'DISTINCT' )
            // InternalCQL.g:42:9: 'DISTINCT'
            {
            match("DISTINCT"); 


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
            // InternalCQL.g:43:7: ( 'FROM' )
            // InternalCQL.g:43:9: 'FROM'
            {
            match("FROM"); 


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
            // InternalCQL.g:44:7: ( ',' )
            // InternalCQL.g:44:9: ','
            {
            match(','); 

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
            // InternalCQL.g:45:7: ( 'WHERE' )
            // InternalCQL.g:45:9: 'WHERE'
            {
            match("WHERE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__46"

    // $ANTLR start "T__47"
    public final void mT__47() throws RecognitionException {
        try {
            int _type = T__47;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:46:7: ( 'SIZE' )
            // InternalCQL.g:46:9: 'SIZE'
            {
            match("SIZE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__47"

    // $ANTLR start "T__48"
    public final void mT__48() throws RecognitionException {
        try {
            int _type = T__48;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:47:7: ( 'TIME' )
            // InternalCQL.g:47:9: 'TIME'
            {
            match("TIME"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__48"

    // $ANTLR start "T__49"
    public final void mT__49() throws RecognitionException {
        try {
            int _type = T__49;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:48:7: ( 'ADVANCE' )
            // InternalCQL.g:48:9: 'ADVANCE'
            {
            match("ADVANCE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__49"

    // $ANTLR start "T__50"
    public final void mT__50() throws RecognitionException {
        try {
            int _type = T__50;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:49:7: ( 'TUPLE' )
            // InternalCQL.g:49:9: 'TUPLE'
            {
            match("TUPLE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__50"

    // $ANTLR start "T__51"
    public final void mT__51() throws RecognitionException {
        try {
            int _type = T__51;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:50:7: ( 'PARTITION' )
            // InternalCQL.g:50:9: 'PARTITION'
            {
            match("PARTITION"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__51"

    // $ANTLR start "T__52"
    public final void mT__52() throws RecognitionException {
        try {
            int _type = T__52;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:51:7: ( 'BY' )
            // InternalCQL.g:51:9: 'BY'
            {
            match("BY"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__52"

    // $ANTLR start "T__53"
    public final void mT__53() throws RecognitionException {
        try {
            int _type = T__53;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:52:7: ( 'CREATE' )
            // InternalCQL.g:52:9: 'CREATE'
            {
            match("CREATE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__53"

    // $ANTLR start "T__54"
    public final void mT__54() throws RecognitionException {
        try {
            int _type = T__54;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:53:7: ( 'WRAPPER' )
            // InternalCQL.g:53:9: 'WRAPPER'
            {
            match("WRAPPER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__54"

    // $ANTLR start "T__55"
    public final void mT__55() throws RecognitionException {
        try {
            int _type = T__55;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:54:7: ( 'PROTOCOL' )
            // InternalCQL.g:54:9: 'PROTOCOL'
            {
            match("PROTOCOL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__55"

    // $ANTLR start "T__56"
    public final void mT__56() throws RecognitionException {
        try {
            int _type = T__56;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:55:7: ( 'TRANSPORT' )
            // InternalCQL.g:55:9: 'TRANSPORT'
            {
            match("TRANSPORT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__56"

    // $ANTLR start "T__57"
    public final void mT__57() throws RecognitionException {
        try {
            int _type = T__57;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:56:7: ( 'DATAHANDLER' )
            // InternalCQL.g:56:9: 'DATAHANDLER'
            {
            match("DATAHANDLER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__57"

    // $ANTLR start "T__58"
    public final void mT__58() throws RecognitionException {
        try {
            int _type = T__58;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:57:7: ( 'OPTIONS' )
            // InternalCQL.g:57:9: 'OPTIONS'
            {
            match("OPTIONS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__58"

    // $ANTLR start "T__59"
    public final void mT__59() throws RecognitionException {
        try {
            int _type = T__59;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:58:7: ( 'CHANNEL' )
            // InternalCQL.g:58:9: 'CHANNEL'
            {
            match("CHANNEL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__59"

    // $ANTLR start "T__60"
    public final void mT__60() throws RecognitionException {
        try {
            int _type = T__60;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:59:7: ( ':' )
            // InternalCQL.g:59:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__60"

    // $ANTLR start "T__61"
    public final void mT__61() throws RecognitionException {
        try {
            int _type = T__61;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:60:7: ( 'SELECT' )
            // InternalCQL.g:60:9: 'SELECT'
            {
            match("SELECT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__61"

    // $ANTLR start "RULE_FLOAT_NUMBER"
    public final void mRULE_FLOAT_NUMBER() throws RecognitionException {
        try {
            int _type = RULE_FLOAT_NUMBER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:5663:19: ( RULE_INT '.' RULE_INT )
            // InternalCQL.g:5663:21: RULE_INT '.' RULE_INT
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
    // $ANTLR end "RULE_FLOAT_NUMBER"

    // $ANTLR start "RULE_ID"
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:5665:9: ( ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // InternalCQL.g:5665:11: ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            // InternalCQL.g:5665:11: ( '^' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='^') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // InternalCQL.g:5665:11: '^'
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

            // InternalCQL.g:5665:40: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalCQL.g:
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
            	    break loop2;
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
            // InternalCQL.g:5667:10: ( ( '0' .. '9' )+ )
            // InternalCQL.g:5667:12: ( '0' .. '9' )+
            {
            // InternalCQL.g:5667:12: ( '0' .. '9' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalCQL.g:5667:13: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
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
            // InternalCQL.g:5669:13: ( ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' ) )
            // InternalCQL.g:5669:15: ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            {
            // InternalCQL.g:5669:15: ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='\"') ) {
                alt6=1;
            }
            else if ( (LA6_0=='\'') ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // InternalCQL.g:5669:16: '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"'
                    {
                    match('\"'); 
                    // InternalCQL.g:5669:20: ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )*
                    loop4:
                    do {
                        int alt4=3;
                        int LA4_0 = input.LA(1);

                        if ( (LA4_0=='\\') ) {
                            alt4=1;
                        }
                        else if ( ((LA4_0>='\u0000' && LA4_0<='!')||(LA4_0>='#' && LA4_0<='[')||(LA4_0>=']' && LA4_0<='\uFFFF')) ) {
                            alt4=2;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // InternalCQL.g:5669:21: '\\\\' .
                    	    {
                    	    match('\\'); 
                    	    matchAny(); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalCQL.g:5669:28: ~ ( ( '\\\\' | '\"' ) )
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
                    	    break loop4;
                        }
                    } while (true);

                    match('\"'); 

                    }
                    break;
                case 2 :
                    // InternalCQL.g:5669:48: '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\''
                    {
                    match('\''); 
                    // InternalCQL.g:5669:53: ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )*
                    loop5:
                    do {
                        int alt5=3;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0=='\\') ) {
                            alt5=1;
                        }
                        else if ( ((LA5_0>='\u0000' && LA5_0<='&')||(LA5_0>='(' && LA5_0<='[')||(LA5_0>=']' && LA5_0<='\uFFFF')) ) {
                            alt5=2;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // InternalCQL.g:5669:54: '\\\\' .
                    	    {
                    	    match('\\'); 
                    	    matchAny(); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalCQL.g:5669:61: ~ ( ( '\\\\' | '\\'' ) )
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
                    	    break loop5;
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
            // InternalCQL.g:5671:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // InternalCQL.g:5671:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // InternalCQL.g:5671:24: ( options {greedy=false; } : . )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='*') ) {
                    int LA7_1 = input.LA(2);

                    if ( (LA7_1=='/') ) {
                        alt7=2;
                    }
                    else if ( ((LA7_1>='\u0000' && LA7_1<='.')||(LA7_1>='0' && LA7_1<='\uFFFF')) ) {
                        alt7=1;
                    }


                }
                else if ( ((LA7_0>='\u0000' && LA7_0<=')')||(LA7_0>='+' && LA7_0<='\uFFFF')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // InternalCQL.g:5671:52: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop7;
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
            // InternalCQL.g:5673:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // InternalCQL.g:5673:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // InternalCQL.g:5673:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='\u0000' && LA8_0<='\t')||(LA8_0>='\u000B' && LA8_0<='\f')||(LA8_0>='\u000E' && LA8_0<='\uFFFF')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // InternalCQL.g:5673:24: ~ ( ( '\\n' | '\\r' ) )
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
            	    break loop8;
                }
            } while (true);

            // InternalCQL.g:5673:40: ( ( '\\r' )? '\\n' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='\n'||LA10_0=='\r') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // InternalCQL.g:5673:41: ( '\\r' )? '\\n'
                    {
                    // InternalCQL.g:5673:41: ( '\\r' )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0=='\r') ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // InternalCQL.g:5673:41: '\\r'
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
            // InternalCQL.g:5675:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // InternalCQL.g:5675:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // InternalCQL.g:5675:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>='\t' && LA11_0<='\n')||LA11_0=='\r'||LA11_0==' ') ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // InternalCQL.g:
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
            	    if ( cnt11 >= 1 ) break loop11;
                        EarlyExitException eee =
                            new EarlyExitException(11, input);
                        throw eee;
                }
                cnt11++;
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
            // InternalCQL.g:5677:16: ( . )
            // InternalCQL.g:5677:18: .
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
        // InternalCQL.g:1:8: ( T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | RULE_FLOAT_NUMBER | RULE_ID | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt12=58;
        alt12 = dfa12.predict(input);
        switch (alt12) {
            case 1 :
                // InternalCQL.g:1:10: T__12
                {
                mT__12(); 

                }
                break;
            case 2 :
                // InternalCQL.g:1:16: T__13
                {
                mT__13(); 

                }
                break;
            case 3 :
                // InternalCQL.g:1:22: T__14
                {
                mT__14(); 

                }
                break;
            case 4 :
                // InternalCQL.g:1:28: T__15
                {
                mT__15(); 

                }
                break;
            case 5 :
                // InternalCQL.g:1:34: T__16
                {
                mT__16(); 

                }
                break;
            case 6 :
                // InternalCQL.g:1:40: T__17
                {
                mT__17(); 

                }
                break;
            case 7 :
                // InternalCQL.g:1:46: T__18
                {
                mT__18(); 

                }
                break;
            case 8 :
                // InternalCQL.g:1:52: T__19
                {
                mT__19(); 

                }
                break;
            case 9 :
                // InternalCQL.g:1:58: T__20
                {
                mT__20(); 

                }
                break;
            case 10 :
                // InternalCQL.g:1:64: T__21
                {
                mT__21(); 

                }
                break;
            case 11 :
                // InternalCQL.g:1:70: T__22
                {
                mT__22(); 

                }
                break;
            case 12 :
                // InternalCQL.g:1:76: T__23
                {
                mT__23(); 

                }
                break;
            case 13 :
                // InternalCQL.g:1:82: T__24
                {
                mT__24(); 

                }
                break;
            case 14 :
                // InternalCQL.g:1:88: T__25
                {
                mT__25(); 

                }
                break;
            case 15 :
                // InternalCQL.g:1:94: T__26
                {
                mT__26(); 

                }
                break;
            case 16 :
                // InternalCQL.g:1:100: T__27
                {
                mT__27(); 

                }
                break;
            case 17 :
                // InternalCQL.g:1:106: T__28
                {
                mT__28(); 

                }
                break;
            case 18 :
                // InternalCQL.g:1:112: T__29
                {
                mT__29(); 

                }
                break;
            case 19 :
                // InternalCQL.g:1:118: T__30
                {
                mT__30(); 

                }
                break;
            case 20 :
                // InternalCQL.g:1:124: T__31
                {
                mT__31(); 

                }
                break;
            case 21 :
                // InternalCQL.g:1:130: T__32
                {
                mT__32(); 

                }
                break;
            case 22 :
                // InternalCQL.g:1:136: T__33
                {
                mT__33(); 

                }
                break;
            case 23 :
                // InternalCQL.g:1:142: T__34
                {
                mT__34(); 

                }
                break;
            case 24 :
                // InternalCQL.g:1:148: T__35
                {
                mT__35(); 

                }
                break;
            case 25 :
                // InternalCQL.g:1:154: T__36
                {
                mT__36(); 

                }
                break;
            case 26 :
                // InternalCQL.g:1:160: T__37
                {
                mT__37(); 

                }
                break;
            case 27 :
                // InternalCQL.g:1:166: T__38
                {
                mT__38(); 

                }
                break;
            case 28 :
                // InternalCQL.g:1:172: T__39
                {
                mT__39(); 

                }
                break;
            case 29 :
                // InternalCQL.g:1:178: T__40
                {
                mT__40(); 

                }
                break;
            case 30 :
                // InternalCQL.g:1:184: T__41
                {
                mT__41(); 

                }
                break;
            case 31 :
                // InternalCQL.g:1:190: T__42
                {
                mT__42(); 

                }
                break;
            case 32 :
                // InternalCQL.g:1:196: T__43
                {
                mT__43(); 

                }
                break;
            case 33 :
                // InternalCQL.g:1:202: T__44
                {
                mT__44(); 

                }
                break;
            case 34 :
                // InternalCQL.g:1:208: T__45
                {
                mT__45(); 

                }
                break;
            case 35 :
                // InternalCQL.g:1:214: T__46
                {
                mT__46(); 

                }
                break;
            case 36 :
                // InternalCQL.g:1:220: T__47
                {
                mT__47(); 

                }
                break;
            case 37 :
                // InternalCQL.g:1:226: T__48
                {
                mT__48(); 

                }
                break;
            case 38 :
                // InternalCQL.g:1:232: T__49
                {
                mT__49(); 

                }
                break;
            case 39 :
                // InternalCQL.g:1:238: T__50
                {
                mT__50(); 

                }
                break;
            case 40 :
                // InternalCQL.g:1:244: T__51
                {
                mT__51(); 

                }
                break;
            case 41 :
                // InternalCQL.g:1:250: T__52
                {
                mT__52(); 

                }
                break;
            case 42 :
                // InternalCQL.g:1:256: T__53
                {
                mT__53(); 

                }
                break;
            case 43 :
                // InternalCQL.g:1:262: T__54
                {
                mT__54(); 

                }
                break;
            case 44 :
                // InternalCQL.g:1:268: T__55
                {
                mT__55(); 

                }
                break;
            case 45 :
                // InternalCQL.g:1:274: T__56
                {
                mT__56(); 

                }
                break;
            case 46 :
                // InternalCQL.g:1:280: T__57
                {
                mT__57(); 

                }
                break;
            case 47 :
                // InternalCQL.g:1:286: T__58
                {
                mT__58(); 

                }
                break;
            case 48 :
                // InternalCQL.g:1:292: T__59
                {
                mT__59(); 

                }
                break;
            case 49 :
                // InternalCQL.g:1:298: T__60
                {
                mT__60(); 

                }
                break;
            case 50 :
                // InternalCQL.g:1:304: T__61
                {
                mT__61(); 

                }
                break;
            case 51 :
                // InternalCQL.g:1:310: RULE_FLOAT_NUMBER
                {
                mRULE_FLOAT_NUMBER(); 

                }
                break;
            case 52 :
                // InternalCQL.g:1:328: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 53 :
                // InternalCQL.g:1:336: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 54 :
                // InternalCQL.g:1:345: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 55 :
                // InternalCQL.g:1:357: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 56 :
                // InternalCQL.g:1:373: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 57 :
                // InternalCQL.g:1:389: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 58 :
                // InternalCQL.g:1:397: RULE_ANY_OTHER
                {
                mRULE_ANY_OTHER(); 

                }
                break;

        }

    }


    protected DFA12 dfa12 = new DFA12(this);
    static final String DFA12_eotS =
        "\1\uffff\10\47\2\45\1\73\1\75\1\uffff\1\101\1\47\3\uffff\2\47\4\uffff\1\47\1\uffff\3\47\1\uffff\1\127\1\45\1\uffff\2\45\2\uffff\1\47\1\uffff\16\47\1\156\1\47\12\uffff\1\47\3\uffff\1\161\3\47\4\uffff\1\47\1\uffff\6\47\2\uffff\1\127\3\uffff\22\47\1\uffff\2\47\1\uffff\1\47\1\u0092\1\47\1\u0094\7\47\1\u009c\1\47\1\u009e\3\47\1\u00a2\7\47\1\u00aa\1\u00ab\3\47\1\u00af\1\47\1\uffff\1\47\1\uffff\7\47\1\uffff\1\47\1\uffff\1\u00ba\1\u00bb\1\u00bc\1\uffff\7\47\2\uffff\3\47\1\uffff\2\47\1\u00c9\7\47\3\uffff\1\47\1\u00d2\2\47\1\u00d5\1\u00d6\1\47\1\u00d8\4\47\1\uffff\3\47\1\u00e0\3\47\1\u00e4\1\uffff\2\47\2\uffff\1\47\1\uffff\1\u00e8\1\47\1\u00ea\1\u00eb\1\u00ec\2\47\1\uffff\1\u00ef\2\47\1\uffff\1\u00f2\2\47\1\uffff\1\47\3\uffff\1\47\1\u00f7\1\uffff\1\u00f8\1\u00f9\1\uffff\3\47\1\u00fd\3\uffff\3\47\1\uffff\1\u0101\2\47\1\uffff\1\47\1\u0105\1\47\1\uffff\1\u0107\1\uffff";
    static final String DFA12_eofS =
        "\u0108\uffff";
    static final String DFA12_minS =
        "\1\0\1\116\1\111\1\101\1\116\1\101\1\105\1\117\1\116\4\75\1\uffff\1\52\1\111\3\uffff\1\120\1\104\4\uffff\1\117\1\uffff\1\110\1\101\1\110\1\uffff\1\56\1\101\1\uffff\2\0\2\uffff\1\102\1\uffff\1\101\1\115\1\120\1\114\2\117\1\124\1\125\1\123\1\124\1\101\1\116\1\114\1\117\1\60\1\104\12\uffff\1\105\3\uffff\1\60\1\124\1\104\1\126\4\uffff\1\124\1\uffff\1\105\1\101\1\122\1\117\1\105\1\101\2\uffff\1\56\3\uffff\1\117\1\105\1\116\1\105\1\114\1\123\1\101\1\115\1\105\1\102\1\124\1\101\1\105\1\122\1\113\2\105\1\114\1\uffff\1\124\1\127\1\uffff\1\111\1\60\1\101\1\60\1\122\1\120\2\124\1\101\1\116\1\125\1\60\1\123\1\60\2\105\1\124\1\60\1\107\1\114\1\111\1\110\1\116\1\101\1\124\2\60\1\103\1\105\1\111\1\60\1\117\1\uffff\1\116\1\uffff\1\105\1\120\1\111\1\117\1\124\2\116\1\uffff\1\120\1\uffff\3\60\1\uffff\2\105\1\116\1\101\1\107\1\115\1\124\2\uffff\1\124\1\101\1\115\1\uffff\1\116\1\103\1\60\1\105\1\124\1\103\2\105\1\104\1\117\3\uffff\1\122\1\60\1\103\1\116\2\60\1\111\1\60\1\116\1\105\1\123\1\105\1\uffff\1\122\1\111\1\117\1\60\1\114\1\105\1\122\1\60\1\uffff\1\124\1\104\2\uffff\1\115\1\uffff\1\60\1\123\3\60\1\117\1\114\1\uffff\1\60\1\104\1\124\1\uffff\1\60\1\114\1\105\1\uffff\1\124\3\uffff\1\116\1\60\1\uffff\2\60\1\uffff\1\105\1\123\1\101\1\60\3\uffff\1\122\1\124\1\115\1\uffff\1\60\1\101\1\120\1\uffff\1\115\1\60\1\120\1\uffff\1\60\1\uffff";
    static final String DFA12_maxS =
        "\1\uffff\1\116\1\125\1\122\1\116\1\117\1\124\1\131\1\116\4\75\1\uffff\1\57\1\111\3\uffff\1\122\1\116\4\uffff\1\117\1\uffff\3\122\1\uffff\1\71\1\172\1\uffff\2\uffff\2\uffff\1\102\1\uffff\1\125\1\115\1\120\1\114\2\117\1\124\1\125\1\123\1\124\1\122\1\132\1\114\1\117\1\172\1\104\12\uffff\1\105\3\uffff\1\172\1\124\1\104\1\126\4\uffff\1\124\1\uffff\1\105\1\101\1\122\1\117\1\105\1\101\2\uffff\1\71\3\uffff\1\117\1\105\1\116\1\105\1\114\1\123\1\101\1\115\1\105\1\102\1\124\1\101\1\111\1\122\1\113\2\105\1\114\1\uffff\1\124\1\127\1\uffff\1\111\1\172\1\101\1\172\1\122\1\120\2\124\1\101\1\116\1\125\1\172\1\123\1\172\2\105\1\124\1\172\1\107\1\114\1\111\1\110\1\116\1\101\1\124\2\172\1\103\1\105\1\111\1\172\1\117\1\uffff\1\116\1\uffff\1\105\1\120\1\111\1\117\1\124\2\116\1\uffff\1\120\1\uffff\3\172\1\uffff\2\105\1\116\1\101\1\107\1\115\1\124\2\uffff\1\124\1\101\1\115\1\uffff\1\116\1\103\1\172\1\105\1\124\1\103\2\105\1\104\1\117\3\uffff\1\122\1\172\1\103\1\116\2\172\1\111\1\172\1\116\1\105\1\123\1\105\1\uffff\1\122\1\111\1\117\1\172\1\114\1\105\1\122\1\172\1\uffff\1\124\1\104\2\uffff\1\115\1\uffff\1\172\1\123\3\172\1\117\1\114\1\uffff\1\172\1\104\1\124\1\uffff\1\172\1\114\1\105\1\uffff\1\124\3\uffff\1\116\1\172\1\uffff\2\172\1\uffff\1\105\1\123\1\101\1\172\3\uffff\1\122\1\124\1\115\1\uffff\1\172\1\101\1\120\1\uffff\1\115\1\172\1\120\1\uffff\1\172\1\uffff";
    static final String DFA12_acceptS =
        "\15\uffff\1\21\2\uffff\1\26\1\27\1\30\2\uffff\1\33\1\34\1\35\1\36\1\uffff\1\42\3\uffff\1\61\2\uffff\1\64\2\uffff\1\71\1\72\1\uffff\1\64\20\uffff\1\13\1\14\1\15\1\20\1\16\1\17\1\21\1\67\1\70\1\22\1\uffff\1\26\1\27\1\30\4\uffff\1\33\1\34\1\35\1\36\1\uffff\1\42\6\uffff\1\61\1\65\1\uffff\1\63\1\66\1\71\22\uffff\1\51\2\uffff\1\31\40\uffff\1\32\1\uffff\1\37\7\uffff\1\2\1\uffff\1\45\3\uffff\1\41\7\uffff\1\25\1\44\3\uffff\1\24\12\uffff\1\47\1\3\1\6\14\uffff\1\43\10\uffff\1\5\2\uffff\1\7\1\23\1\uffff\1\62\7\uffff\1\52\3\uffff\1\4\3\uffff\1\10\1\uffff\1\57\1\46\1\53\2\uffff\1\60\2\uffff\1\40\4\uffff\1\54\1\1\1\55\3\uffff\1\50\3\uffff\1\56\3\uffff\1\12\1\uffff\1\11";
    static final String DFA12_specialS =
        "\1\0\41\uffff\1\1\1\2\u00e4\uffff}>";
    static final String[] DFA12_transitionS = {
            "\11\45\2\44\2\45\1\44\22\45\1\44\1\12\1\42\4\45\1\43\1\27\1\30\1\15\1\25\1\32\1\26\1\45\1\16\12\37\1\36\1\20\1\14\1\11\1\13\2\45\1\24\1\7\1\35\1\5\1\10\1\3\2\41\1\4\4\41\1\31\1\23\1\34\2\41\1\6\1\2\1\1\1\17\1\33\3\41\1\21\1\45\1\22\1\40\1\41\1\45\32\41\uff85\45",
            "\1\46",
            "\1\51\10\uffff\1\50\2\uffff\1\52",
            "\1\53\12\uffff\1\54\5\uffff\1\55",
            "\1\56",
            "\1\61\7\uffff\1\60\5\uffff\1\57",
            "\1\64\3\uffff\1\63\12\uffff\1\62",
            "\1\65\11\uffff\1\66",
            "\1\67",
            "\1\70",
            "\1\71",
            "\1\72",
            "\1\74",
            "",
            "\1\77\4\uffff\1\100",
            "\1\102",
            "",
            "",
            "",
            "\1\107\1\uffff\1\106",
            "\1\111\11\uffff\1\110",
            "",
            "",
            "",
            "",
            "\1\116",
            "",
            "\1\120\11\uffff\1\121",
            "\1\122\20\uffff\1\123",
            "\1\125\11\uffff\1\124",
            "",
            "\1\131\1\uffff\12\130",
            "\32\47\4\uffff\1\47\1\uffff\32\47",
            "",
            "\0\132",
            "\0\132",
            "",
            "",
            "\1\134",
            "",
            "\1\136\23\uffff\1\135",
            "\1\137",
            "\1\140",
            "\1\141",
            "\1\142",
            "\1\143",
            "\1\144",
            "\1\145",
            "\1\146",
            "\1\147",
            "\1\151\20\uffff\1\150",
            "\1\152\13\uffff\1\153",
            "\1\154",
            "\1\155",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\157",
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
            "\1\160",
            "",
            "",
            "",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\162",
            "\1\163",
            "\1\164",
            "",
            "",
            "",
            "",
            "\1\165",
            "",
            "\1\166",
            "\1\167",
            "\1\170",
            "\1\171",
            "\1\172",
            "\1\173",
            "",
            "",
            "\1\131\1\uffff\12\130",
            "",
            "",
            "",
            "\1\174",
            "\1\175",
            "\1\176",
            "\1\177",
            "\1\u0080",
            "\1\u0081",
            "\1\u0082",
            "\1\u0083",
            "\1\u0084",
            "\1\u0085",
            "\1\u0086",
            "\1\u0087",
            "\1\u0089\3\uffff\1\u0088",
            "\1\u008a",
            "\1\u008b",
            "\1\u008c",
            "\1\u008d",
            "\1\u008e",
            "",
            "\1\u008f",
            "\1\u0090",
            "",
            "\1\u0091",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u0093",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u0095",
            "\1\u0096",
            "\1\u0097",
            "\1\u0098",
            "\1\u0099",
            "\1\u009a",
            "\1\u009b",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u009d",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u009f",
            "\1\u00a0",
            "\1\u00a1",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u00a3",
            "\1\u00a4",
            "\1\u00a5",
            "\1\u00a6",
            "\1\u00a7",
            "\1\u00a8",
            "\1\u00a9",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u00ac",
            "\1\u00ad",
            "\1\u00ae",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u00b0",
            "",
            "\1\u00b1",
            "",
            "\1\u00b2",
            "\1\u00b3",
            "\1\u00b4",
            "\1\u00b5",
            "\1\u00b6",
            "\1\u00b7",
            "\1\u00b8",
            "",
            "\1\u00b9",
            "",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "",
            "\1\u00bd",
            "\1\u00be",
            "\1\u00bf",
            "\1\u00c0",
            "\1\u00c1",
            "\1\u00c2",
            "\1\u00c3",
            "",
            "",
            "\1\u00c4",
            "\1\u00c5",
            "\1\u00c6",
            "",
            "\1\u00c7",
            "\1\u00c8",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u00ca",
            "\1\u00cb",
            "\1\u00cc",
            "\1\u00cd",
            "\1\u00ce",
            "\1\u00cf",
            "\1\u00d0",
            "",
            "",
            "",
            "\1\u00d1",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u00d3",
            "\1\u00d4",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u00d7",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u00d9",
            "\1\u00da",
            "\1\u00db",
            "\1\u00dc",
            "",
            "\1\u00dd",
            "\1\u00de",
            "\1\u00df",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u00e1",
            "\1\u00e2",
            "\1\u00e3",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "",
            "\1\u00e5",
            "\1\u00e6",
            "",
            "",
            "\1\u00e7",
            "",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u00e9",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u00ed",
            "\1\u00ee",
            "",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u00f0",
            "\1\u00f1",
            "",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u00f3",
            "\1\u00f4",
            "",
            "\1\u00f5",
            "",
            "",
            "",
            "\1\u00f6",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "",
            "\1\u00fa",
            "\1\u00fb",
            "\1\u00fc",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "",
            "",
            "",
            "\1\u00fe",
            "\1\u00ff",
            "\1\u0100",
            "",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u0102",
            "\1\u0103",
            "",
            "\1\u0104",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            "\1\u0106",
            "",
            "\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
            ""
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
            return "1:1: Tokens : ( T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | RULE_FLOAT_NUMBER | RULE_ID | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA12_0 = input.LA(1);

                        s = -1;
                        if ( (LA12_0=='U') ) {s = 1;}

                        else if ( (LA12_0=='T') ) {s = 2;}

                        else if ( (LA12_0=='F') ) {s = 3;}

                        else if ( (LA12_0=='I') ) {s = 4;}

                        else if ( (LA12_0=='D') ) {s = 5;}

                        else if ( (LA12_0=='S') ) {s = 6;}

                        else if ( (LA12_0=='B') ) {s = 7;}

                        else if ( (LA12_0=='E') ) {s = 8;}

                        else if ( (LA12_0=='=') ) {s = 9;}

                        else if ( (LA12_0=='!') ) {s = 10;}

                        else if ( (LA12_0=='>') ) {s = 11;}

                        else if ( (LA12_0=='<') ) {s = 12;}

                        else if ( (LA12_0=='*') ) {s = 13;}

                        else if ( (LA12_0=='/') ) {s = 14;}

                        else if ( (LA12_0=='V') ) {s = 15;}

                        else if ( (LA12_0==';') ) {s = 16;}

                        else if ( (LA12_0=='[') ) {s = 17;}

                        else if ( (LA12_0==']') ) {s = 18;}

                        else if ( (LA12_0=='O') ) {s = 19;}

                        else if ( (LA12_0=='A') ) {s = 20;}

                        else if ( (LA12_0=='+') ) {s = 21;}

                        else if ( (LA12_0=='-') ) {s = 22;}

                        else if ( (LA12_0=='(') ) {s = 23;}

                        else if ( (LA12_0==')') ) {s = 24;}

                        else if ( (LA12_0=='N') ) {s = 25;}

                        else if ( (LA12_0==',') ) {s = 26;}

                        else if ( (LA12_0=='W') ) {s = 27;}

                        else if ( (LA12_0=='P') ) {s = 28;}

                        else if ( (LA12_0=='C') ) {s = 29;}

                        else if ( (LA12_0==':') ) {s = 30;}

                        else if ( ((LA12_0>='0' && LA12_0<='9')) ) {s = 31;}

                        else if ( (LA12_0=='^') ) {s = 32;}

                        else if ( ((LA12_0>='G' && LA12_0<='H')||(LA12_0>='J' && LA12_0<='M')||(LA12_0>='Q' && LA12_0<='R')||(LA12_0>='X' && LA12_0<='Z')||LA12_0=='_'||(LA12_0>='a' && LA12_0<='z')) ) {s = 33;}

                        else if ( (LA12_0=='\"') ) {s = 34;}

                        else if ( (LA12_0=='\'') ) {s = 35;}

                        else if ( ((LA12_0>='\t' && LA12_0<='\n')||LA12_0=='\r'||LA12_0==' ') ) {s = 36;}

                        else if ( ((LA12_0>='\u0000' && LA12_0<='\b')||(LA12_0>='\u000B' && LA12_0<='\f')||(LA12_0>='\u000E' && LA12_0<='\u001F')||(LA12_0>='#' && LA12_0<='&')||LA12_0=='.'||(LA12_0>='?' && LA12_0<='@')||LA12_0=='\\'||LA12_0=='`'||(LA12_0>='{' && LA12_0<='\uFFFF')) ) {s = 37;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA12_34 = input.LA(1);

                        s = -1;
                        if ( ((LA12_34>='\u0000' && LA12_34<='\uFFFF')) ) {s = 90;}

                        else s = 37;

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA12_35 = input.LA(1);

                        s = -1;
                        if ( ((LA12_35>='\u0000' && LA12_35<='\uFFFF')) ) {s = 90;}

                        else s = 37;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 12, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}