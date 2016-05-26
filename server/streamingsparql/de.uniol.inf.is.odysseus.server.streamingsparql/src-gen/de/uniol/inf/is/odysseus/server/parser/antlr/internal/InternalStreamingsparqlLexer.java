package de.uniol.inf.is.odysseus.server.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalStreamingsparqlLexer extends Lexer {
    public static final int T__50=50;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int RULE_IRI_TERMINAL=5;
    public static final int RULE_ID=4;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=9;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int RULE_ML_COMMENT=11;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__20=20;
    public static final int T__21=21;
    public static final int RULE_WINDOWTYPE=8;
    public static final int RULE_STRING=7;
    public static final int RULE_SL_COMMENT=12;
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
    public static final int RULE_WS=13;
    public static final int RULE_ANY_OTHER=14;
    public static final int RULE_UNITTYPE=10;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__46=46;
    public static final int RULE_AGG_FUNCTION=6;
    public static final int T__47=47;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__42=42;
    public static final int T__43=43;

    // delegates
    // delegators

    public InternalStreamingsparqlLexer() {;} 
    public InternalStreamingsparqlLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InternalStreamingsparqlLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g"; }

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:11:7: ( 'PREFIX' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:11:9: 'PREFIX'
            {
            match("PREFIX"); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:12:7: ( ':' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:12:9: ':'
            {
            match(':'); 

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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:13:7: ( 'BASE' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:13:9: 'BASE'
            {
            match("BASE"); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:14:7: ( '#ADDQUERY' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:14:9: '#ADDQUERY'
            {
            match("#ADDQUERY"); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:15:7: ( '#RUNQUERY' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:15:9: '#RUNQUERY'
            {
            match("#RUNQUERY"); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:16:7: ( 'SELECT' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:16:9: 'SELECT'
            {
            match("SELECT"); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:17:7: ( 'DISTINCT' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:17:9: 'DISTINCT'
            {
            match("DISTINCT"); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:18:7: ( 'REDUCED' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:18:9: 'REDUCED'
            {
            match("REDUCED"); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:19:7: ( 'AGGREGATE(' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:19:9: 'AGGREGATE('
            {
            match("AGGREGATE("); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:20:7: ( 'aggregations = [' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:20:9: 'aggregations = ['
            {
            match("aggregations = ["); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:21:7: ( ']' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:21:9: ']'
            {
            match(']'); 

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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:22:7: ( ',' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:22:9: ','
            {
            match(','); 

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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:23:7: ( ')' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:23:9: ')'
            {
            match(')'); 

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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:24:7: ( 'group_by=[' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:24:9: 'group_by=['
            {
            match("group_by=["); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:25:7: ( '[' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:25:9: '['
            {
            match('['); 

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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:26:7: ( 'CSVFILESINK(' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:26:9: 'CSVFILESINK('
            {
            match("CSVFILESINK("); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:27:7: ( 'FILTER(' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:27:9: 'FILTER('
            {
            match("FILTER("); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:28:7: ( 'FROM' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:28:9: 'FROM'
            {
            match("FROM"); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:29:7: ( 'AS' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:29:9: 'AS'
            {
            match("AS"); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:30:7: ( '[TYPE ' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:30:9: '[TYPE '
            {
            match("[TYPE "); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:31:7: ( 'SIZE ' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:31:9: 'SIZE '
            {
            match("SIZE "); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:32:7: ( 'ADVANCE' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:32:9: 'ADVANCE'
            {
            match("ADVANCE"); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:33:7: ( 'UNIT ' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:33:9: 'UNIT '
            {
            match("UNIT "); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:34:7: ( 'WHERE' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:34:9: 'WHERE'
            {
            match("WHERE"); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:35:7: ( '{' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:35:9: '{'
            {
            match('{'); 

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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:36:7: ( '}' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:36:9: '}'
            {
            match('}'); 

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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:37:7: ( '.' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:37:9: '.'
            {
            match('.'); 

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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:38:7: ( ';' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:38:9: ';'
            {
            match(';'); 

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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:39:7: ( '?' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:39:9: '?'
            {
            match('?'); 

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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:40:7: ( '<' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:40:9: '<'
            {
            match('<'); 

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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:41:7: ( '>' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:41:9: '>'
            {
            match('>'); 

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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:42:7: ( '<=' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:42:9: '<='
            {
            match("<="); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:43:7: ( '>=' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:43:9: '>='
            {
            match(">="); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:44:7: ( '=' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:44:9: '='
            {
            match('='); 

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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:45:7: ( '!=' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:45:9: '!='
            {
            match("!="); 


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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:46:7: ( '+' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:46:9: '+'
            {
            match('+'); 

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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:47:7: ( '/' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:47:9: '/'
            {
            match('/'); 

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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:48:7: ( '-' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:48:9: '-'
            {
            match('-'); 

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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:49:7: ( '*' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:49:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__53"

    // $ANTLR start "RULE_AGG_FUNCTION"
    public final void mRULE_AGG_FUNCTION() throws RecognitionException {
        try {
            int _type = RULE_AGG_FUNCTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1714:19: ( ( 'COUNT' | 'MAX' | 'MIN' | 'AVG' | 'SUM' | 'MEDIAN' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1714:21: ( 'COUNT' | 'MAX' | 'MIN' | 'AVG' | 'SUM' | 'MEDIAN' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1714:21: ( 'COUNT' | 'MAX' | 'MIN' | 'AVG' | 'SUM' | 'MEDIAN' )
            int alt1=6;
            switch ( input.LA(1) ) {
            case 'C':
                {
                alt1=1;
                }
                break;
            case 'M':
                {
                switch ( input.LA(2) ) {
                case 'A':
                    {
                    alt1=2;
                    }
                    break;
                case 'I':
                    {
                    alt1=3;
                    }
                    break;
                case 'E':
                    {
                    alt1=6;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 2, input);

                    throw nvae;
                }

                }
                break;
            case 'A':
                {
                alt1=4;
                }
                break;
            case 'S':
                {
                alt1=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }

            switch (alt1) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1714:22: 'COUNT'
                    {
                    match("COUNT"); 


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1714:30: 'MAX'
                    {
                    match("MAX"); 


                    }
                    break;
                case 3 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1714:36: 'MIN'
                    {
                    match("MIN"); 


                    }
                    break;
                case 4 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1714:42: 'AVG'
                    {
                    match("AVG"); 


                    }
                    break;
                case 5 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1714:48: 'SUM'
                    {
                    match("SUM"); 


                    }
                    break;
                case 6 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1714:54: 'MEDIAN'
                    {
                    match("MEDIAN"); 


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
    // $ANTLR end "RULE_AGG_FUNCTION"

    // $ANTLR start "RULE_IRI_TERMINAL"
    public final void mRULE_IRI_TERMINAL() throws RecognitionException {
        try {
            int _type = RULE_IRI_TERMINAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1716:19: ( '<' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' | '/' | ':' | '.' | '#' | '?' | '@' | '$' | '&' | '=' )* '>' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1716:21: '<' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' | '/' | ':' | '.' | '#' | '?' | '@' | '$' | '&' | '=' )* '>'
            {
            match('<'); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1716:25: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' | '/' | ':' | '.' | '#' | '?' | '@' | '$' | '&' | '=' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='#' && LA2_0<='$')||LA2_0=='&'||(LA2_0>='-' && LA2_0<=':')||LA2_0=='='||(LA2_0>='?' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:
            	    {
            	    if ( (input.LA(1)>='#' && input.LA(1)<='$')||input.LA(1)=='&'||(input.LA(1)>='-' && input.LA(1)<=':')||input.LA(1)=='='||(input.LA(1)>='?' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
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

            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_IRI_TERMINAL"

    // $ANTLR start "RULE_STRING"
    public final void mRULE_STRING() throws RecognitionException {
        try {
            int _type = RULE_STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1718:13: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1718:15: '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"'
            {
            match('\"'); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1718:19: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )*
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
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1718:20: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' )
            	    {
            	    match('\\'); 
            	    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;
            	case 2 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1718:61: ~ ( ( '\\\\' | '\"' ) )
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

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_STRING"

    // $ANTLR start "RULE_WINDOWTYPE"
    public final void mRULE_WINDOWTYPE() throws RecognitionException {
        try {
            int _type = RULE_WINDOWTYPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1720:17: ( ( 'ELEMENT' | 'TIME' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1720:19: ( 'ELEMENT' | 'TIME' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1720:19: ( 'ELEMENT' | 'TIME' )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='E') ) {
                alt4=1;
            }
            else if ( (LA4_0=='T') ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1720:20: 'ELEMENT'
                    {
                    match("ELEMENT"); 


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1720:30: 'TIME'
                    {
                    match("TIME"); 


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
    // $ANTLR end "RULE_WINDOWTYPE"

    // $ANTLR start "RULE_UNITTYPE"
    public final void mRULE_UNITTYPE() throws RecognitionException {
        try {
            int _type = RULE_UNITTYPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1722:15: ( ( 'NANOSECONDS' | 'MICROSECONDS' | 'MILLISECONDS' | 'SECONDS' | 'MINUTES' | 'HOURS' | 'DAYS' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1722:17: ( 'NANOSECONDS' | 'MICROSECONDS' | 'MILLISECONDS' | 'SECONDS' | 'MINUTES' | 'HOURS' | 'DAYS' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1722:17: ( 'NANOSECONDS' | 'MICROSECONDS' | 'MILLISECONDS' | 'SECONDS' | 'MINUTES' | 'HOURS' | 'DAYS' )
            int alt5=7;
            alt5 = dfa5.predict(input);
            switch (alt5) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1722:18: 'NANOSECONDS'
                    {
                    match("NANOSECONDS"); 


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1722:32: 'MICROSECONDS'
                    {
                    match("MICROSECONDS"); 


                    }
                    break;
                case 3 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1722:47: 'MILLISECONDS'
                    {
                    match("MILLISECONDS"); 


                    }
                    break;
                case 4 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1722:62: 'SECONDS'
                    {
                    match("SECONDS"); 


                    }
                    break;
                case 5 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1722:72: 'MINUTES'
                    {
                    match("MINUTES"); 


                    }
                    break;
                case 6 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1722:82: 'HOURS'
                    {
                    match("HOURS"); 


                    }
                    break;
                case 7 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1722:90: 'DAYS'
                    {
                    match("DAYS"); 


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
    // $ANTLR end "RULE_UNITTYPE"

    // $ANTLR start "RULE_ID"
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1724:9: ( ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1724:11: ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1724:11: ( '^' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='^') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1724:11: '^'
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

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1724:40: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='0' && LA7_0<='9')||(LA7_0>='A' && LA7_0<='Z')||LA7_0=='_'||(LA7_0>='a' && LA7_0<='z')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:
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
            	    break loop7;
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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1726:10: ( ( '0' .. '9' )+ )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1726:12: ( '0' .. '9' )+
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1726:12: ( '0' .. '9' )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='0' && LA8_0<='9')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1726:13: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_INT"

    // $ANTLR start "RULE_ML_COMMENT"
    public final void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1728:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1728:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1728:24: ( options {greedy=false; } : . )*
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
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1728:52: .
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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1730:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1730:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1730:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( ((LA10_0>='\u0000' && LA10_0<='\t')||(LA10_0>='\u000B' && LA10_0<='\f')||(LA10_0>='\u000E' && LA10_0<='\uFFFF')) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1730:24: ~ ( ( '\\n' | '\\r' ) )
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

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1730:40: ( ( '\\r' )? '\\n' )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0=='\n'||LA12_0=='\r') ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1730:41: ( '\\r' )? '\\n'
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1730:41: ( '\\r' )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0=='\r') ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1730:41: '\\r'
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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1732:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1732:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1732:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
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
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:
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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1734:16: ( . )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1734:18: .
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
        // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:8: ( T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | RULE_AGG_FUNCTION | RULE_IRI_TERMINAL | RULE_STRING | RULE_WINDOWTYPE | RULE_UNITTYPE | RULE_ID | RULE_INT | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt14=50;
        alt14 = dfa14.predict(input);
        switch (alt14) {
            case 1 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:10: T__15
                {
                mT__15(); 

                }
                break;
            case 2 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:16: T__16
                {
                mT__16(); 

                }
                break;
            case 3 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:22: T__17
                {
                mT__17(); 

                }
                break;
            case 4 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:28: T__18
                {
                mT__18(); 

                }
                break;
            case 5 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:34: T__19
                {
                mT__19(); 

                }
                break;
            case 6 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:40: T__20
                {
                mT__20(); 

                }
                break;
            case 7 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:46: T__21
                {
                mT__21(); 

                }
                break;
            case 8 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:52: T__22
                {
                mT__22(); 

                }
                break;
            case 9 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:58: T__23
                {
                mT__23(); 

                }
                break;
            case 10 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:64: T__24
                {
                mT__24(); 

                }
                break;
            case 11 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:70: T__25
                {
                mT__25(); 

                }
                break;
            case 12 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:76: T__26
                {
                mT__26(); 

                }
                break;
            case 13 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:82: T__27
                {
                mT__27(); 

                }
                break;
            case 14 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:88: T__28
                {
                mT__28(); 

                }
                break;
            case 15 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:94: T__29
                {
                mT__29(); 

                }
                break;
            case 16 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:100: T__30
                {
                mT__30(); 

                }
                break;
            case 17 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:106: T__31
                {
                mT__31(); 

                }
                break;
            case 18 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:112: T__32
                {
                mT__32(); 

                }
                break;
            case 19 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:118: T__33
                {
                mT__33(); 

                }
                break;
            case 20 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:124: T__34
                {
                mT__34(); 

                }
                break;
            case 21 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:130: T__35
                {
                mT__35(); 

                }
                break;
            case 22 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:136: T__36
                {
                mT__36(); 

                }
                break;
            case 23 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:142: T__37
                {
                mT__37(); 

                }
                break;
            case 24 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:148: T__38
                {
                mT__38(); 

                }
                break;
            case 25 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:154: T__39
                {
                mT__39(); 

                }
                break;
            case 26 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:160: T__40
                {
                mT__40(); 

                }
                break;
            case 27 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:166: T__41
                {
                mT__41(); 

                }
                break;
            case 28 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:172: T__42
                {
                mT__42(); 

                }
                break;
            case 29 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:178: T__43
                {
                mT__43(); 

                }
                break;
            case 30 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:184: T__44
                {
                mT__44(); 

                }
                break;
            case 31 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:190: T__45
                {
                mT__45(); 

                }
                break;
            case 32 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:196: T__46
                {
                mT__46(); 

                }
                break;
            case 33 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:202: T__47
                {
                mT__47(); 

                }
                break;
            case 34 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:208: T__48
                {
                mT__48(); 

                }
                break;
            case 35 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:214: T__49
                {
                mT__49(); 

                }
                break;
            case 36 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:220: T__50
                {
                mT__50(); 

                }
                break;
            case 37 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:226: T__51
                {
                mT__51(); 

                }
                break;
            case 38 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:232: T__52
                {
                mT__52(); 

                }
                break;
            case 39 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:238: T__53
                {
                mT__53(); 

                }
                break;
            case 40 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:244: RULE_AGG_FUNCTION
                {
                mRULE_AGG_FUNCTION(); 

                }
                break;
            case 41 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:262: RULE_IRI_TERMINAL
                {
                mRULE_IRI_TERMINAL(); 

                }
                break;
            case 42 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:280: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 43 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:292: RULE_WINDOWTYPE
                {
                mRULE_WINDOWTYPE(); 

                }
                break;
            case 44 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:308: RULE_UNITTYPE
                {
                mRULE_UNITTYPE(); 

                }
                break;
            case 45 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:322: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 46 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:330: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 47 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:339: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 48 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:355: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 49 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:371: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 50 :
                // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1:379: RULE_ANY_OTHER
                {
                mRULE_ANY_OTHER(); 

                }
                break;

        }

    }


    protected DFA5 dfa5 = new DFA5(this);
    protected DFA14 dfa14 = new DFA14(this);
    static final String DFA5_eotS =
        "\12\uffff";
    static final String DFA5_eofS =
        "\12\uffff";
    static final String DFA5_minS =
        "\1\104\1\uffff\1\111\3\uffff\1\103\3\uffff";
    static final String DFA5_maxS =
        "\1\123\1\uffff\1\111\3\uffff\1\116\3\uffff";
    static final String DFA5_acceptS =
        "\1\uffff\1\1\1\uffff\1\4\1\6\1\7\1\uffff\1\2\1\3\1\5";
    static final String DFA5_specialS =
        "\12\uffff}>";
    static final String[] DFA5_transitionS = {
            "\1\5\3\uffff\1\4\4\uffff\1\2\1\1\4\uffff\1\3",
            "",
            "\1\6",
            "",
            "",
            "",
            "\1\7\10\uffff\1\10\1\uffff\1\11",
            "",
            "",
            ""
    };

    static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
    static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
    static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
    static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
    static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
    static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
    static final short[][] DFA5_transition;

    static {
        int numStates = DFA5_transitionS.length;
        DFA5_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
        }
    }

    class DFA5 extends DFA {

        public DFA5(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 5;
            this.eot = DFA5_eot;
            this.eof = DFA5_eof;
            this.min = DFA5_min;
            this.max = DFA5_max;
            this.accept = DFA5_accept;
            this.special = DFA5_special;
            this.transition = DFA5_transition;
        }
        public String getDescription() {
            return "1722:17: ( 'NANOSECONDS' | 'MICROSECONDS' | 'MILLISECONDS' | 'SECONDS' | 'MINUTES' | 'HOURS' | 'DAYS' )";
        }
    }
    static final String DFA14_eotS =
        "\1\uffff\1\54\1\uffff\1\54\1\52\5\54\3\uffff\1\54\1\101\4\54\5"+
        "\uffff\1\116\1\121\1\uffff\1\52\1\uffff\1\127\2\uffff\1\54\1\52"+
        "\4\54\1\52\4\uffff\1\54\2\uffff\1\54\2\uffff\7\54\1\156\3\54\3\uffff"+
        "\1\54\2\uffff\6\54\5\uffff\1\171\14\uffff\3\54\1\uffff\4\54\2\uffff"+
        "\5\54\1\u0088\4\54\1\uffff\1\54\1\u0088\10\54\1\uffff\2\u0088\10"+
        "\54\1\u009f\3\54\1\uffff\1\54\1\u00a4\10\54\1\u00ad\7\54\1\u00b5"+
        "\3\54\1\uffff\2\54\1\uffff\1\54\1\uffff\6\54\1\u0088\1\54\2\uffff"+
        "\1\u00c3\5\54\1\uffff\1\54\1\u00a4\1\u00ca\1\u00cb\11\54\1\uffff"+
        "\3\54\1\u0088\2\54\2\uffff\1\u00a4\1\54\1\u00db\1\54\1\u00dd\3\54"+
        "\1\uffff\1\u00a4\2\54\1\u00b5\1\54\1\u00e4\1\uffff\1\54\1\uffff"+
        "\6\54\1\uffff\2\54\1\uffff\4\54\1\uffff\11\54\1\u00a4\1\54\1\uffff"+
        "\2\u00a4\1\uffff";
    static final String DFA14_eofS =
        "\u00fc\uffff";
    static final String DFA14_minS =
        "\1\0\1\122\1\uffff\2\101\1\105\1\101\1\105\1\104\1\147\3\uffff"+
        "\1\162\1\124\1\117\1\111\1\116\1\110\5\uffff\1\43\1\75\1\uffff\1"+
        "\75\1\uffff\1\52\2\uffff\1\101\1\0\1\114\1\111\1\101\1\117\1\101"+
        "\4\uffff\1\105\2\uffff\1\123\2\uffff\1\103\1\132\1\115\1\123\1\131"+
        "\1\104\1\107\1\60\1\126\1\107\1\147\3\uffff\1\157\2\uffff\1\126"+
        "\1\125\1\114\1\117\1\111\1\105\5\uffff\1\43\14\uffff\1\130\1\103"+
        "\1\104\1\uffff\1\105\1\115\1\116\1\125\2\uffff\1\106\2\105\1\117"+
        "\1\105\1\60\1\124\1\123\1\125\1\122\1\uffff\1\101\1\60\1\162\1\165"+
        "\1\106\1\116\1\124\1\115\1\124\1\122\1\uffff\2\60\1\122\1\114\1"+
        "\111\1\115\1\105\1\117\1\122\1\111\1\60\1\103\1\116\1\40\1\uffff"+
        "\1\111\1\60\1\103\1\105\1\116\1\145\1\160\1\111\1\124\1\105\1\60"+
        "\1\40\1\105\1\124\1\117\1\111\1\101\1\105\1\60\2\123\1\130\1\uffff"+
        "\1\124\1\104\1\uffff\1\116\1\uffff\1\105\1\107\1\103\1\147\1\137"+
        "\1\114\1\60\1\122\2\uffff\1\60\1\105\2\123\2\116\1\uffff\1\105\3"+
        "\60\1\123\1\103\1\104\1\101\1\105\1\141\1\142\1\105\1\50\1\uffff"+
        "\1\123\2\105\1\60\1\124\1\103\2\uffff\1\60\1\124\1\60\1\124\1\60"+
        "\1\164\1\171\1\123\1\uffff\1\60\2\103\1\60\1\117\1\60\1\uffff\1"+
        "\105\1\uffff\1\151\1\75\1\111\2\117\1\116\1\uffff\1\50\1\157\1\uffff"+
        "\3\116\1\104\1\uffff\1\156\1\113\2\104\1\123\1\163\1\50\2\123\1"+
        "\60\1\40\1\uffff\2\60\1\uffff";
    static final String DFA14_maxS =
        "\1\uffff\1\122\1\uffff\1\101\1\122\1\125\1\111\1\105\1\126\1\147"+
        "\3\uffff\1\162\1\124\1\123\1\122\1\116\1\110\5\uffff\1\172\1\75"+
        "\1\uffff\1\75\1\uffff\1\57\2\uffff\1\111\1\uffff\1\114\1\111\1\101"+
        "\1\117\1\172\4\uffff\1\105\2\uffff\1\123\2\uffff\1\114\1\132\1\115"+
        "\1\123\1\131\1\104\1\107\1\172\1\126\1\107\1\147\3\uffff\1\157\2"+
        "\uffff\1\126\1\125\1\114\1\117\1\111\1\105\5\uffff\1\172\14\uffff"+
        "\1\130\1\116\1\104\1\uffff\1\105\1\115\1\116\1\125\2\uffff\1\106"+
        "\2\105\1\117\1\105\1\172\1\124\1\123\1\125\1\122\1\uffff\1\101\1"+
        "\172\1\162\1\165\1\106\1\116\1\124\1\115\1\124\1\122\1\uffff\2\172"+
        "\1\122\1\114\1\111\1\115\1\105\1\117\1\122\1\111\1\172\1\103\1\116"+
        "\1\40\1\uffff\1\111\1\172\1\103\1\105\1\116\1\145\1\160\1\111\1"+
        "\124\1\105\1\172\1\40\1\105\1\124\1\117\1\111\1\101\1\105\1\172"+
        "\2\123\1\130\1\uffff\1\124\1\104\1\uffff\1\116\1\uffff\1\105\1\107"+
        "\1\103\1\147\1\137\1\114\1\172\1\122\2\uffff\1\172\1\105\2\123\2"+
        "\116\1\uffff\1\105\3\172\1\123\1\103\1\104\1\101\1\105\1\141\1\142"+
        "\1\105\1\50\1\uffff\1\123\2\105\1\172\1\124\1\103\2\uffff\1\172"+
        "\1\124\1\172\1\124\1\172\1\164\1\171\1\123\1\uffff\1\172\2\103\1"+
        "\172\1\117\1\172\1\uffff\1\105\1\uffff\1\151\1\75\1\111\2\117\1"+
        "\116\1\uffff\1\50\1\157\1\uffff\3\116\1\104\1\uffff\1\156\1\113"+
        "\2\104\1\123\1\163\1\50\2\123\1\172\1\40\1\uffff\2\172\1\uffff";
    static final String DFA14_acceptS =
        "\2\uffff\1\2\7\uffff\1\13\1\14\1\15\6\uffff\1\31\1\32\1\33\1\34"+
        "\1\35\2\uffff\1\42\1\uffff\1\44\1\uffff\1\46\1\47\7\uffff\1\55\1"+
        "\56\1\61\1\62\1\uffff\1\55\1\2\1\uffff\1\4\1\5\13\uffff\1\13\1\14"+
        "\1\15\1\uffff\1\24\1\17\6\uffff\1\31\1\32\1\33\1\34\1\35\1\uffff"+
        "\1\36\1\51\1\41\1\37\1\42\1\43\1\44\1\57\1\60\1\45\1\46\1\47\3\uffff"+
        "\1\52\4\uffff\1\56\1\61\12\uffff\1\23\12\uffff\1\40\16\uffff\1\50"+
        "\26\uffff\1\3\2\uffff\1\25\1\uffff\1\54\10\uffff\1\22\1\27\6\uffff"+
        "\1\53\15\uffff\1\30\6\uffff\1\1\1\6\10\uffff\1\21\6\uffff\1\10\1"+
        "\uffff\1\26\6\uffff\1\7\2\uffff\1\16\4\uffff\1\11\13\uffff\1\20"+
        "\2\uffff\1\12";
    static final String DFA14_specialS =
        "\1\0\40\uffff\1\1\u00da\uffff}>";
    static final String[] DFA14_transitionS = {
            "\11\52\2\51\2\52\1\51\22\52\1\51\1\33\1\41\1\4\5\52\1\14\1"+
            "\37\1\34\1\13\1\36\1\25\1\35\12\50\1\2\1\26\1\30\1\32\1\31\1"+
            "\27\1\52\1\10\1\3\1\17\1\6\1\42\1\20\1\47\1\45\4\47\1\40\1\44"+
            "\1\47\1\1\1\47\1\7\1\5\1\43\1\21\1\47\1\22\3\47\1\16\1\52\1"+
            "\12\1\46\1\47\1\52\1\11\5\47\1\15\23\47\1\23\1\52\1\24\uff82"+
            "\52",
            "\1\53",
            "",
            "\1\56",
            "\1\57\20\uffff\1\60",
            "\1\61\3\uffff\1\62\13\uffff\1\63",
            "\1\65\7\uffff\1\64",
            "\1\66",
            "\1\71\2\uffff\1\67\13\uffff\1\70\2\uffff\1\72",
            "\1\73",
            "",
            "",
            "",
            "\1\77",
            "\1\100",
            "\1\103\3\uffff\1\102",
            "\1\104\10\uffff\1\105",
            "\1\106",
            "\1\107",
            "",
            "",
            "",
            "",
            "",
            "\2\117\1\uffff\1\117\6\uffff\16\117\2\uffff\1\115\35\117\4"+
            "\uffff\1\117\1\uffff\32\117",
            "\1\120",
            "",
            "\1\123",
            "",
            "\1\125\4\uffff\1\126",
            "",
            "",
            "\1\132\3\uffff\1\134\3\uffff\1\133",
            "\0\135",
            "\1\136",
            "\1\137",
            "\1\140",
            "\1\141",
            "\32\54\4\uffff\1\54\1\uffff\32\54",
            "",
            "",
            "",
            "",
            "\1\144",
            "",
            "",
            "\1\145",
            "",
            "",
            "\1\147\10\uffff\1\146",
            "\1\150",
            "\1\151",
            "\1\152",
            "\1\153",
            "\1\154",
            "\1\155",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\157",
            "\1\160",
            "\1\161",
            "",
            "",
            "",
            "\1\162",
            "",
            "",
            "\1\163",
            "\1\164",
            "\1\165",
            "\1\166",
            "\1\167",
            "\1\170",
            "",
            "",
            "",
            "",
            "",
            "\2\117\1\uffff\1\117\6\uffff\16\117\2\uffff\36\117\4\uffff"+
            "\1\117\1\uffff\32\117",
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
            "\1\172",
            "\1\174\10\uffff\1\175\1\uffff\1\173",
            "\1\176",
            "",
            "\1\177",
            "\1\u0080",
            "\1\u0081",
            "\1\u0082",
            "",
            "",
            "\1\u0083",
            "\1\u0084",
            "\1\u0085",
            "\1\u0086",
            "\1\u0087",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\u0089",
            "\1\u008a",
            "\1\u008b",
            "\1\u008c",
            "",
            "\1\u008d",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\u008e",
            "\1\u008f",
            "\1\u0090",
            "\1\u0091",
            "\1\u0092",
            "\1\u0093",
            "\1\u0094",
            "\1\u0095",
            "",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\12\54\7\uffff\24\54\1\u0096\5\54\4\uffff\1\54\1\uffff\32"+
            "\54",
            "\1\u0097",
            "\1\u0098",
            "\1\u0099",
            "\1\u009a",
            "\1\u009b",
            "\1\u009c",
            "\1\u009d",
            "\1\u009e",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\u00a0",
            "\1\u00a1",
            "\1\u00a2",
            "",
            "\1\u00a3",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\u00a5",
            "\1\u00a6",
            "\1\u00a7",
            "\1\u00a8",
            "\1\u00a9",
            "\1\u00aa",
            "\1\u00ab",
            "\1\u00ac",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\u00ae",
            "\1\u00af",
            "\1\u00b0",
            "\1\u00b1",
            "\1\u00b2",
            "\1\u00b3",
            "\1\u00b4",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\u00b6",
            "\1\u00b7",
            "\1\u00b8",
            "",
            "\1\u00b9",
            "\1\u00ba",
            "",
            "\1\u00bb",
            "",
            "\1\u00bc",
            "\1\u00bd",
            "\1\u00be",
            "\1\u00bf",
            "\1\u00c0",
            "\1\u00c1",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\u00c2",
            "",
            "",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\u00c4",
            "\1\u00c5",
            "\1\u00c6",
            "\1\u00c7",
            "\1\u00c8",
            "",
            "\1\u00c9",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\u00cc",
            "\1\u00cd",
            "\1\u00ce",
            "\1\u00cf",
            "\1\u00d0",
            "\1\u00d1",
            "\1\u00d2",
            "\1\u00d3",
            "\1\u00d4",
            "",
            "\1\u00d5",
            "\1\u00d6",
            "\1\u00d7",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\u00d8",
            "\1\u00d9",
            "",
            "",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\u00da",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\u00dc",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\u00de",
            "\1\u00df",
            "\1\u00e0",
            "",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\u00e1",
            "\1\u00e2",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\u00e3",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "",
            "\1\u00e5",
            "",
            "\1\u00e6",
            "\1\u00e7",
            "\1\u00e8",
            "\1\u00e9",
            "\1\u00ea",
            "\1\u00eb",
            "",
            "\1\u00ec",
            "\1\u00ed",
            "",
            "\1\u00ee",
            "\1\u00ef",
            "\1\u00f0",
            "\1\u00f1",
            "",
            "\1\u00f2",
            "\1\u00f3",
            "\1\u00f4",
            "\1\u00f5",
            "\1\u00f6",
            "\1\u00f7",
            "\1\u00f8",
            "\1\u00f9",
            "\1\u00fa",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\1\u00fb",
            "",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
            "\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54",
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
            return "1:1: Tokens : ( T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | RULE_AGG_FUNCTION | RULE_IRI_TERMINAL | RULE_STRING | RULE_WINDOWTYPE | RULE_UNITTYPE | RULE_ID | RULE_INT | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA14_0 = input.LA(1);

                        s = -1;
                        if ( (LA14_0=='P') ) {s = 1;}

                        else if ( (LA14_0==':') ) {s = 2;}

                        else if ( (LA14_0=='B') ) {s = 3;}

                        else if ( (LA14_0=='#') ) {s = 4;}

                        else if ( (LA14_0=='S') ) {s = 5;}

                        else if ( (LA14_0=='D') ) {s = 6;}

                        else if ( (LA14_0=='R') ) {s = 7;}

                        else if ( (LA14_0=='A') ) {s = 8;}

                        else if ( (LA14_0=='a') ) {s = 9;}

                        else if ( (LA14_0==']') ) {s = 10;}

                        else if ( (LA14_0==',') ) {s = 11;}

                        else if ( (LA14_0==')') ) {s = 12;}

                        else if ( (LA14_0=='g') ) {s = 13;}

                        else if ( (LA14_0=='[') ) {s = 14;}

                        else if ( (LA14_0=='C') ) {s = 15;}

                        else if ( (LA14_0=='F') ) {s = 16;}

                        else if ( (LA14_0=='U') ) {s = 17;}

                        else if ( (LA14_0=='W') ) {s = 18;}

                        else if ( (LA14_0=='{') ) {s = 19;}

                        else if ( (LA14_0=='}') ) {s = 20;}

                        else if ( (LA14_0=='.') ) {s = 21;}

                        else if ( (LA14_0==';') ) {s = 22;}

                        else if ( (LA14_0=='?') ) {s = 23;}

                        else if ( (LA14_0=='<') ) {s = 24;}

                        else if ( (LA14_0=='>') ) {s = 25;}

                        else if ( (LA14_0=='=') ) {s = 26;}

                        else if ( (LA14_0=='!') ) {s = 27;}

                        else if ( (LA14_0=='+') ) {s = 28;}

                        else if ( (LA14_0=='/') ) {s = 29;}

                        else if ( (LA14_0=='-') ) {s = 30;}

                        else if ( (LA14_0=='*') ) {s = 31;}

                        else if ( (LA14_0=='M') ) {s = 32;}

                        else if ( (LA14_0=='\"') ) {s = 33;}

                        else if ( (LA14_0=='E') ) {s = 34;}

                        else if ( (LA14_0=='T') ) {s = 35;}

                        else if ( (LA14_0=='N') ) {s = 36;}

                        else if ( (LA14_0=='H') ) {s = 37;}

                        else if ( (LA14_0=='^') ) {s = 38;}

                        else if ( (LA14_0=='G'||(LA14_0>='I' && LA14_0<='L')||LA14_0=='O'||LA14_0=='Q'||LA14_0=='V'||(LA14_0>='X' && LA14_0<='Z')||LA14_0=='_'||(LA14_0>='b' && LA14_0<='f')||(LA14_0>='h' && LA14_0<='z')) ) {s = 39;}

                        else if ( ((LA14_0>='0' && LA14_0<='9')) ) {s = 40;}

                        else if ( ((LA14_0>='\t' && LA14_0<='\n')||LA14_0=='\r'||LA14_0==' ') ) {s = 41;}

                        else if ( ((LA14_0>='\u0000' && LA14_0<='\b')||(LA14_0>='\u000B' && LA14_0<='\f')||(LA14_0>='\u000E' && LA14_0<='\u001F')||(LA14_0>='$' && LA14_0<='(')||LA14_0=='@'||LA14_0=='\\'||LA14_0=='`'||LA14_0=='|'||(LA14_0>='~' && LA14_0<='\uFFFF')) ) {s = 42;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA14_33 = input.LA(1);

                        s = -1;
                        if ( ((LA14_33>='\u0000' && LA14_33<='\uFFFF')) ) {s = 93;}

                        else s = 42;

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