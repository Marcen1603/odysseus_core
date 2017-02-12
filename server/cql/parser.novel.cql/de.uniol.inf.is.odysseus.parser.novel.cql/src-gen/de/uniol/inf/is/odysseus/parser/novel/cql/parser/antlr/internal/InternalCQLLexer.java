package de.uniol.inf.is.odysseus.parser.novel.cql.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;


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
    public static final int RULE_ID=4;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=6;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int T__66=66;
    public static final int RULE_ML_COMMENT=8;
    public static final int T__23=23;
    public static final int T__67=67;
    public static final int T__24=24;
    public static final int T__68=68;
    public static final int T__25=25;
    public static final int T__69=69;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T__20=20;
    public static final int T__64=64;
    public static final int T__21=21;
    public static final int T__65=65;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int RULE_STRING=5;
    public static final int RULE_SL_COMMENT=9;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int T__33=33;
    public static final int T__77=77;
    public static final int T__34=34;
    public static final int T__78=78;
    public static final int T__35=35;
    public static final int T__79=79;
    public static final int T__36=36;
    public static final int T__73=73;
    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__74=74;
    public static final int T__31=31;
    public static final int T__75=75;
    public static final int T__32=32;
    public static final int T__76=76;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int RULE_WS=10;
    public static final int RULE_ANY_OTHER=11;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int RULE_FLOAT=7;
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
            // InternalCQL.g:11:7: ( ';' )
            // InternalCQL.g:11:9: ';'
            {
            match(';'); 

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
            // InternalCQL.g:12:7: ( 'SELECT' )
            // InternalCQL.g:12:9: 'SELECT'
            {
            match("SELECT"); 


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
            // InternalCQL.g:13:7: ( 'DISTINCT' )
            // InternalCQL.g:13:9: 'DISTINCT'
            {
            match("DISTINCT"); 


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
            // InternalCQL.g:14:7: ( '*' )
            // InternalCQL.g:14:9: '*'
            {
            match('*'); 

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
            // InternalCQL.g:15:7: ( ',' )
            // InternalCQL.g:15:9: ','
            {
            match(','); 

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
            // InternalCQL.g:16:7: ( 'FROM' )
            // InternalCQL.g:16:9: 'FROM'
            {
            match("FROM"); 


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
            // InternalCQL.g:17:7: ( 'WHERE' )
            // InternalCQL.g:17:9: 'WHERE'
            {
            match("WHERE"); 


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
            // InternalCQL.g:18:7: ( 'GROUP' )
            // InternalCQL.g:18:9: 'GROUP'
            {
            match("GROUP"); 


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
            // InternalCQL.g:19:7: ( 'BY' )
            // InternalCQL.g:19:9: 'BY'
            {
            match("BY"); 


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
            // InternalCQL.g:20:7: ( 'HAVING' )
            // InternalCQL.g:20:9: 'HAVING'
            {
            match("HAVING"); 


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
            // InternalCQL.g:21:7: ( '(' )
            // InternalCQL.g:21:9: '('
            {
            match('('); 

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
            // InternalCQL.g:22:7: ( ')' )
            // InternalCQL.g:22:9: ')'
            {
            match(')'); 

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
            // InternalCQL.g:23:7: ( '[' )
            // InternalCQL.g:23:9: '['
            {
            match('['); 

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
            // InternalCQL.g:24:7: ( ']' )
            // InternalCQL.g:24:9: ']'
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
            // InternalCQL.g:25:7: ( 'AS' )
            // InternalCQL.g:25:9: 'AS'
            {
            match("AS"); 


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
            // InternalCQL.g:26:7: ( '.' )
            // InternalCQL.g:26:9: '.'
            {
            match('.'); 

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
            // InternalCQL.g:27:7: ( 'IN' )
            // InternalCQL.g:27:9: 'IN'
            {
            match("IN"); 


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
            // InternalCQL.g:28:7: ( 'AVG' )
            // InternalCQL.g:28:9: 'AVG'
            {
            match("AVG"); 


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
            // InternalCQL.g:29:7: ( 'MIN' )
            // InternalCQL.g:29:9: 'MIN'
            {
            match("MIN"); 


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
            // InternalCQL.g:30:7: ( 'MAX' )
            // InternalCQL.g:30:9: 'MAX'
            {
            match("MAX"); 


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
            // InternalCQL.g:31:7: ( 'COUNT' )
            // InternalCQL.g:31:9: 'COUNT'
            {
            match("COUNT"); 


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
            // InternalCQL.g:32:7: ( 'SUM' )
            // InternalCQL.g:32:9: 'SUM'
            {
            match("SUM"); 


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
            // InternalCQL.g:33:7: ( 'MEDIAN' )
            // InternalCQL.g:33:9: 'MEDIAN'
            {
            match("MEDIAN"); 


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
            // InternalCQL.g:34:7: ( 'FIRST' )
            // InternalCQL.g:34:9: 'FIRST'
            {
            match("FIRST"); 


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
            // InternalCQL.g:35:7: ( 'LAST' )
            // InternalCQL.g:35:9: 'LAST'
            {
            match("LAST"); 


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
            // InternalCQL.g:36:7: ( 'DolToEur' )
            // InternalCQL.g:36:9: 'DolToEur'
            {
            match("DolToEur"); 


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
            // InternalCQL.g:39:7: ( '/' )
            // InternalCQL.g:39:9: '/'
            {
            match('/'); 

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
            // InternalCQL.g:40:7: ( 'WRAPPER' )
            // InternalCQL.g:40:9: 'WRAPPER'
            {
            match("WRAPPER"); 


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
            // InternalCQL.g:41:7: ( 'PROTOCOL' )
            // InternalCQL.g:41:9: 'PROTOCOL'
            {
            match("PROTOCOL"); 


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
            // InternalCQL.g:42:7: ( 'TRANSPORT' )
            // InternalCQL.g:42:9: 'TRANSPORT'
            {
            match("TRANSPORT"); 


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
            // InternalCQL.g:43:7: ( 'DATAHANDLER' )
            // InternalCQL.g:43:9: 'DATAHANDLER'
            {
            match("DATAHANDLER"); 


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
            // InternalCQL.g:44:7: ( 'OPTIONS' )
            // InternalCQL.g:44:9: 'OPTIONS'
            {
            match("OPTIONS"); 


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
            // InternalCQL.g:45:7: ( 'STREAM' )
            // InternalCQL.g:45:9: 'STREAM'
            {
            match("STREAM"); 


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
            // InternalCQL.g:46:7: ( 'SINK' )
            // InternalCQL.g:46:9: 'SINK'
            {
            match("SINK"); 


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
            // InternalCQL.g:47:7: ( 'CHANNEL' )
            // InternalCQL.g:47:9: 'CHANNEL'
            {
            match("CHANNEL"); 


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
            // InternalCQL.g:48:7: ( ':' )
            // InternalCQL.g:48:9: ':'
            {
            match(':'); 

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
            // InternalCQL.g:49:7: ( 'FILE' )
            // InternalCQL.g:49:9: 'FILE'
            {
            match("FILE"); 


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
            // InternalCQL.g:50:7: ( 'VIEW' )
            // InternalCQL.g:50:9: 'VIEW'
            {
            match("VIEW"); 


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
            // InternalCQL.g:51:7: ( 'TO' )
            // InternalCQL.g:51:9: 'TO'
            {
            match("TO"); 


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
            // InternalCQL.g:52:7: ( 'DROP' )
            // InternalCQL.g:52:9: 'DROP'
            {
            match("DROP"); 


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
            // InternalCQL.g:53:7: ( 'IF EXISTS' )
            // InternalCQL.g:53:9: 'IF EXISTS'
            {
            match("IF EXISTS"); 


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
            // InternalCQL.g:54:7: ( 'UNBOUNDED' )
            // InternalCQL.g:54:9: 'UNBOUNDED'
            {
            match("UNBOUNDED"); 


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
            // InternalCQL.g:55:7: ( 'SIZE' )
            // InternalCQL.g:55:9: 'SIZE'
            {
            match("SIZE"); 


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
            // InternalCQL.g:56:7: ( 'ADVANCE' )
            // InternalCQL.g:56:9: 'ADVANCE'
            {
            match("ADVANCE"); 


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
            // InternalCQL.g:57:7: ( 'TIME' )
            // InternalCQL.g:57:9: 'TIME'
            {
            match("TIME"); 


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
            // InternalCQL.g:58:7: ( 'TUPLE' )
            // InternalCQL.g:58:9: 'TUPLE'
            {
            match("TUPLE"); 


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
            // InternalCQL.g:59:7: ( 'PARTITION' )
            // InternalCQL.g:59:9: 'PARTITION'
            {
            match("PARTITION"); 


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
            // InternalCQL.g:60:7: ( 'OR' )
            // InternalCQL.g:60:9: 'OR'
            {
            match("OR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__61"

    // $ANTLR start "T__62"
    public final void mT__62() throws RecognitionException {
        try {
            int _type = T__62;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:61:7: ( 'AND' )
            // InternalCQL.g:61:9: 'AND'
            {
            match("AND"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__62"

    // $ANTLR start "T__63"
    public final void mT__63() throws RecognitionException {
        try {
            int _type = T__63;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:62:7: ( '=' )
            // InternalCQL.g:62:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__63"

    // $ANTLR start "T__64"
    public final void mT__64() throws RecognitionException {
        try {
            int _type = T__64;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:63:7: ( '!=' )
            // InternalCQL.g:63:9: '!='
            {
            match("!="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__64"

    // $ANTLR start "T__65"
    public final void mT__65() throws RecognitionException {
        try {
            int _type = T__65;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:64:7: ( '>=' )
            // InternalCQL.g:64:9: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__65"

    // $ANTLR start "T__66"
    public final void mT__66() throws RecognitionException {
        try {
            int _type = T__66;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:65:7: ( '<=' )
            // InternalCQL.g:65:9: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__66"

    // $ANTLR start "T__67"
    public final void mT__67() throws RecognitionException {
        try {
            int _type = T__67;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:66:7: ( '<' )
            // InternalCQL.g:66:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__67"

    // $ANTLR start "T__68"
    public final void mT__68() throws RecognitionException {
        try {
            int _type = T__68;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:67:7: ( '>' )
            // InternalCQL.g:67:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__68"

    // $ANTLR start "T__69"
    public final void mT__69() throws RecognitionException {
        try {
            int _type = T__69;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:68:7: ( 'NOT' )
            // InternalCQL.g:68:9: 'NOT'
            {
            match("NOT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__69"

    // $ANTLR start "T__70"
    public final void mT__70() throws RecognitionException {
        try {
            int _type = T__70;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:69:7: ( 'TRUE' )
            // InternalCQL.g:69:9: 'TRUE'
            {
            match("TRUE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__70"

    // $ANTLR start "T__71"
    public final void mT__71() throws RecognitionException {
        try {
            int _type = T__71;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:70:7: ( 'FALSE' )
            // InternalCQL.g:70:9: 'FALSE'
            {
            match("FALSE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__71"

    // $ANTLR start "T__72"
    public final void mT__72() throws RecognitionException {
        try {
            int _type = T__72;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:71:7: ( 'INTEGER' )
            // InternalCQL.g:71:9: 'INTEGER'
            {
            match("INTEGER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__72"

    // $ANTLR start "T__73"
    public final void mT__73() throws RecognitionException {
        try {
            int _type = T__73;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:72:7: ( 'DOUBLE' )
            // InternalCQL.g:72:9: 'DOUBLE'
            {
            match("DOUBLE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__73"

    // $ANTLR start "T__74"
    public final void mT__74() throws RecognitionException {
        try {
            int _type = T__74;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:73:7: ( 'LONG' )
            // InternalCQL.g:73:9: 'LONG'
            {
            match("LONG"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__74"

    // $ANTLR start "T__75"
    public final void mT__75() throws RecognitionException {
        try {
            int _type = T__75;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:74:7: ( 'FLOAT' )
            // InternalCQL.g:74:9: 'FLOAT'
            {
            match("FLOAT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__75"

    // $ANTLR start "T__76"
    public final void mT__76() throws RecognitionException {
        try {
            int _type = T__76;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:75:7: ( 'STRING' )
            // InternalCQL.g:75:9: 'STRING'
            {
            match("STRING"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__76"

    // $ANTLR start "T__77"
    public final void mT__77() throws RecognitionException {
        try {
            int _type = T__77;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:76:7: ( 'BOOLEAN' )
            // InternalCQL.g:76:9: 'BOOLEAN'
            {
            match("BOOLEAN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__77"

    // $ANTLR start "T__78"
    public final void mT__78() throws RecognitionException {
        try {
            int _type = T__78;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:77:7: ( 'STARTTIMESTAMP' )
            // InternalCQL.g:77:9: 'STARTTIMESTAMP'
            {
            match("STARTTIMESTAMP"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__78"

    // $ANTLR start "T__79"
    public final void mT__79() throws RecognitionException {
        try {
            int _type = T__79;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:78:7: ( 'ENDTIMESTAMP' )
            // InternalCQL.g:78:9: 'ENDTIMESTAMP'
            {
            match("ENDTIMESTAMP"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__79"

    // $ANTLR start "T__80"
    public final void mT__80() throws RecognitionException {
        try {
            int _type = T__80;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:79:7: ( 'CREATE' )
            // InternalCQL.g:79:9: 'CREATE'
            {
            match("CREATE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__80"

    // $ANTLR start "T__81"
    public final void mT__81() throws RecognitionException {
        try {
            int _type = T__81;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:80:7: ( 'ATTACH' )
            // InternalCQL.g:80:9: 'ATTACH'
            {
            match("ATTACH"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__81"

    // $ANTLR start "RULE_ID"
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCQL.g:4682:9: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' | ':' | '$' | '{' | '}' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | ':' | '$' | '{' | '}' | '0' .. '9' )* )
            // InternalCQL.g:4682:11: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | ':' | '$' | '{' | '}' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | ':' | '$' | '{' | '}' | '0' .. '9' )*
            {
            if ( input.LA(1)=='$'||input.LA(1)==':'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='{')||input.LA(1)=='}' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // InternalCQL.g:4682:51: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | ':' | '$' | '{' | '}' | '0' .. '9' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='$'||(LA1_0>='0' && LA1_0<=':')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='{')||LA1_0=='}') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalCQL.g:
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
            // InternalCQL.g:4684:12: ( RULE_INT '.' RULE_INT )
            // InternalCQL.g:4684:14: RULE_INT '.' RULE_INT
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
            // InternalCQL.g:4686:10: ( ( '0' .. '9' )+ )
            // InternalCQL.g:4686:12: ( '0' .. '9' )+
            {
            // InternalCQL.g:4686:12: ( '0' .. '9' )+
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
            	    // InternalCQL.g:4686:13: '0' .. '9'
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
            // InternalCQL.g:4688:13: ( ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' ) )
            // InternalCQL.g:4688:15: ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            {
            // InternalCQL.g:4688:15: ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
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
                    // InternalCQL.g:4688:16: '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"'
                    {
                    match('\"'); 
                    // InternalCQL.g:4688:20: ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )*
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
                    	    // InternalCQL.g:4688:21: '\\\\' .
                    	    {
                    	    match('\\'); 
                    	    matchAny(); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalCQL.g:4688:28: ~ ( ( '\\\\' | '\"' ) )
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
                    // InternalCQL.g:4688:48: '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\''
                    {
                    match('\''); 
                    // InternalCQL.g:4688:53: ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )*
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
                    	    // InternalCQL.g:4688:54: '\\\\' .
                    	    {
                    	    match('\\'); 
                    	    matchAny(); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalCQL.g:4688:61: ~ ( ( '\\\\' | '\\'' ) )
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
            // InternalCQL.g:4690:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // InternalCQL.g:4690:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // InternalCQL.g:4690:24: ( options {greedy=false; } : . )*
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
            	    // InternalCQL.g:4690:52: .
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
            // InternalCQL.g:4692:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // InternalCQL.g:4692:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // InternalCQL.g:4692:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='\u0000' && LA7_0<='\t')||(LA7_0>='\u000B' && LA7_0<='\f')||(LA7_0>='\u000E' && LA7_0<='\uFFFF')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // InternalCQL.g:4692:24: ~ ( ( '\\n' | '\\r' ) )
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

            // InternalCQL.g:4692:40: ( ( '\\r' )? '\\n' )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='\n'||LA9_0=='\r') ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // InternalCQL.g:4692:41: ( '\\r' )? '\\n'
                    {
                    // InternalCQL.g:4692:41: ( '\\r' )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0=='\r') ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // InternalCQL.g:4692:41: '\\r'
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
            // InternalCQL.g:4694:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // InternalCQL.g:4694:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // InternalCQL.g:4694:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
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
            // InternalCQL.g:4696:16: ( . )
            // InternalCQL.g:4696:18: .
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
        // InternalCQL.g:1:8: ( T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | RULE_ID | RULE_FLOAT | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt11=78;
        alt11 = dfa11.predict(input);
        switch (alt11) {
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
                // InternalCQL.g:1:310: T__62
                {
                mT__62(); 

                }
                break;
            case 52 :
                // InternalCQL.g:1:316: T__63
                {
                mT__63(); 

                }
                break;
            case 53 :
                // InternalCQL.g:1:322: T__64
                {
                mT__64(); 

                }
                break;
            case 54 :
                // InternalCQL.g:1:328: T__65
                {
                mT__65(); 

                }
                break;
            case 55 :
                // InternalCQL.g:1:334: T__66
                {
                mT__66(); 

                }
                break;
            case 56 :
                // InternalCQL.g:1:340: T__67
                {
                mT__67(); 

                }
                break;
            case 57 :
                // InternalCQL.g:1:346: T__68
                {
                mT__68(); 

                }
                break;
            case 58 :
                // InternalCQL.g:1:352: T__69
                {
                mT__69(); 

                }
                break;
            case 59 :
                // InternalCQL.g:1:358: T__70
                {
                mT__70(); 

                }
                break;
            case 60 :
                // InternalCQL.g:1:364: T__71
                {
                mT__71(); 

                }
                break;
            case 61 :
                // InternalCQL.g:1:370: T__72
                {
                mT__72(); 

                }
                break;
            case 62 :
                // InternalCQL.g:1:376: T__73
                {
                mT__73(); 

                }
                break;
            case 63 :
                // InternalCQL.g:1:382: T__74
                {
                mT__74(); 

                }
                break;
            case 64 :
                // InternalCQL.g:1:388: T__75
                {
                mT__75(); 

                }
                break;
            case 65 :
                // InternalCQL.g:1:394: T__76
                {
                mT__76(); 

                }
                break;
            case 66 :
                // InternalCQL.g:1:400: T__77
                {
                mT__77(); 

                }
                break;
            case 67 :
                // InternalCQL.g:1:406: T__78
                {
                mT__78(); 

                }
                break;
            case 68 :
                // InternalCQL.g:1:412: T__79
                {
                mT__79(); 

                }
                break;
            case 69 :
                // InternalCQL.g:1:418: T__80
                {
                mT__80(); 

                }
                break;
            case 70 :
                // InternalCQL.g:1:424: T__81
                {
                mT__81(); 

                }
                break;
            case 71 :
                // InternalCQL.g:1:430: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 72 :
                // InternalCQL.g:1:438: RULE_FLOAT
                {
                mRULE_FLOAT(); 

                }
                break;
            case 73 :
                // InternalCQL.g:1:449: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 74 :
                // InternalCQL.g:1:458: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 75 :
                // InternalCQL.g:1:470: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 76 :
                // InternalCQL.g:1:486: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 77 :
                // InternalCQL.g:1:502: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 78 :
                // InternalCQL.g:1:510: RULE_ANY_OTHER
                {
                mRULE_ANY_OTHER(); 

                }
                break;

        }

    }


    protected DFA11 dfa11 = new DFA11(this);
    static final String DFA11_eotS =
        "\2\uffff\2\57\2\uffff\5\57\4\uffff\1\57\1\uffff\4\57\2\uffff\1\131\3\57\1\142\2\57\1\uffff\1\51\1\150\1\152\2\57\1\uffff\1\155\2\51\3\uffff\4\57\1\uffff\5\57\2\uffff\7\57\1\u0085\2\57\4\uffff\1\u0088\4\57\1\uffff\1\u008e\11\57\5\uffff\3\57\1\u009c\3\57\1\u00a0\1\uffff\2\57\6\uffff\2\57\2\uffff\1\155\2\uffff\1\57\1\u00a6\21\57\1\uffff\2\57\1\uffff\1\u00bb\1\57\1\u00bd\2\57\2\uffff\1\u00c0\1\u00c1\12\57\1\uffff\3\57\1\uffff\2\57\1\u00d1\2\57\1\uffff\3\57\1\u00d7\1\u00d8\3\57\1\u00dc\1\57\1\u00de\1\57\1\u00e0\7\57\1\uffff\1\57\1\uffff\2\57\2\uffff\4\57\1\u00ef\1\u00f0\3\57\1\u00f4\1\u00f5\2\57\1\u00f8\1\57\1\uffff\5\57\2\uffff\3\57\1\uffff\1\57\1\uffff\1\u0103\1\uffff\1\u0104\1\u0105\1\u0106\1\57\1\u0108\6\57\1\u010f\2\57\2\uffff\3\57\2\uffff\1\u0115\1\57\1\uffff\2\57\1\u0119\1\u011a\1\u011b\4\57\1\u0120\4\uffff\1\57\1\uffff\1\57\1\u0123\1\57\1\u0125\1\57\1\u0127\1\uffff\1\57\1\u0129\3\57\1\uffff\3\57\3\uffff\4\57\1\uffff\1\u0134\1\u0135\1\uffff\1\u0136\1\uffff\1\u0137\1\uffff\1\u0138\1\uffff\3\57\1\u013c\3\57\1\u0140\1\u0141\1\57\5\uffff\1\u0143\2\57\1\uffff\3\57\2\uffff\1\57\1\uffff\1\u014a\1\u014b\1\u014c\3\57\3\uffff\2\57\1\u0152\2\57\1\uffff\1\u0155\1\57\1\uffff\1\u0157\1\uffff";
    static final String DFA11_eofS =
        "\u0158\uffff";
    static final String DFA11_minS =
        "\1\0\1\uffff\1\105\1\101\2\uffff\1\101\1\110\1\122\1\117\1\101\4\uffff\1\104\1\uffff\1\106\1\101\1\110\1\101\2\uffff\1\52\1\101\1\111\1\120\1\44\1\111\1\116\1\uffff\3\75\1\117\1\116\1\uffff\1\56\2\0\3\uffff\1\114\1\115\1\101\1\116\1\uffff\1\123\1\154\1\124\1\117\1\125\2\uffff\1\117\2\114\1\117\1\105\1\101\1\117\1\44\1\117\1\126\4\uffff\1\44\1\107\1\126\1\104\1\124\1\uffff\1\44\1\40\1\116\1\130\1\104\1\125\1\101\1\105\1\123\1\116\5\uffff\1\117\1\122\1\101\1\44\1\115\1\120\1\124\1\44\1\uffff\1\105\1\102\6\uffff\1\124\1\104\2\uffff\1\56\2\uffff\1\105\1\44\1\105\1\122\1\113\1\105\2\124\1\101\1\120\1\102\1\115\1\123\1\105\1\123\1\101\1\122\1\120\1\125\1\uffff\1\114\1\111\1\uffff\1\44\1\101\1\44\1\101\1\105\2\uffff\2\44\1\111\2\116\1\101\1\124\1\107\2\124\1\116\1\105\1\uffff\1\105\1\114\1\111\1\uffff\1\127\1\117\1\44\1\124\1\103\1\uffff\1\101\1\116\1\124\2\44\1\111\1\157\1\110\1\44\1\114\1\44\1\124\1\44\1\105\1\124\1\105\2\120\1\105\1\116\1\uffff\1\116\1\uffff\1\103\1\107\2\uffff\1\101\1\124\1\116\1\124\2\44\1\117\1\111\1\123\2\44\1\105\1\117\1\44\1\125\1\uffff\1\111\1\124\1\115\1\107\1\124\2\uffff\1\116\1\105\1\101\1\uffff\1\105\1\uffff\1\44\1\uffff\3\44\1\105\1\44\1\101\1\107\1\103\1\110\1\105\1\116\1\44\2\105\2\uffff\1\103\1\124\1\120\2\uffff\1\44\1\116\1\uffff\1\116\1\115\3\44\1\111\1\103\1\165\1\116\1\44\4\uffff\1\122\1\uffff\1\116\1\44\1\105\1\44\1\122\1\44\1\uffff\1\114\1\44\1\117\1\111\1\117\1\uffff\1\123\1\104\1\105\3\uffff\1\115\1\124\1\162\1\104\1\uffff\2\44\1\uffff\1\44\1\uffff\1\44\1\uffff\1\44\1\uffff\1\114\1\117\1\122\1\44\1\105\1\123\1\105\2\44\1\114\5\uffff\1\44\1\116\1\124\1\uffff\1\104\1\124\1\123\2\uffff\1\105\1\uffff\3\44\1\101\1\124\1\122\3\uffff\1\115\1\101\1\44\1\120\1\115\1\uffff\1\44\1\120\1\uffff\1\44\1\uffff";
    static final String DFA11_maxS =
        "\1\uffff\1\uffff\1\125\1\157\2\uffff\3\122\1\131\1\101\4\uffff\1\126\1\uffff\1\116\1\111\1\122\1\117\2\uffff\1\57\1\122\1\125\1\122\1\175\1\111\1\116\1\uffff\3\75\1\117\1\116\1\uffff\1\71\2\uffff\3\uffff\1\114\1\115\1\122\1\132\1\uffff\1\123\1\154\1\124\1\117\1\125\2\uffff\1\117\1\122\1\114\1\117\1\105\1\101\1\117\1\175\1\117\1\126\4\uffff\1\175\1\107\1\126\1\104\1\124\1\uffff\1\175\1\40\1\116\1\130\1\104\1\125\1\101\1\105\1\123\1\116\5\uffff\1\117\1\122\1\125\1\175\1\115\1\120\1\124\1\175\1\uffff\1\105\1\102\6\uffff\1\124\1\104\2\uffff\1\71\2\uffff\1\105\1\175\1\111\1\122\1\113\1\105\2\124\1\101\1\120\1\102\1\115\1\123\1\105\1\123\1\101\1\122\1\120\1\125\1\uffff\1\114\1\111\1\uffff\1\175\1\101\1\175\1\101\1\105\2\uffff\2\175\1\111\2\116\1\101\1\124\1\107\2\124\1\116\1\105\1\uffff\1\105\1\114\1\111\1\uffff\1\127\1\117\1\175\1\124\1\103\1\uffff\1\101\1\116\1\124\2\175\1\111\1\157\1\110\1\175\1\114\1\175\1\124\1\175\1\105\1\124\1\105\2\120\1\105\1\116\1\uffff\1\116\1\uffff\1\103\1\107\2\uffff\1\101\1\124\1\116\1\124\2\175\1\117\1\111\1\123\2\175\1\105\1\117\1\175\1\125\1\uffff\1\111\1\124\1\115\1\107\1\124\2\uffff\1\116\1\105\1\101\1\uffff\1\105\1\uffff\1\175\1\uffff\3\175\1\105\1\175\1\101\1\107\1\103\1\110\1\105\1\116\1\175\2\105\2\uffff\1\103\1\124\1\120\2\uffff\1\175\1\116\1\uffff\1\116\1\115\3\175\1\111\1\103\1\165\1\116\1\175\4\uffff\1\122\1\uffff\1\116\1\175\1\105\1\175\1\122\1\175\1\uffff\1\114\1\175\1\117\1\111\1\117\1\uffff\1\123\1\104\1\105\3\uffff\1\115\1\124\1\162\1\104\1\uffff\2\175\1\uffff\1\175\1\uffff\1\175\1\uffff\1\175\1\uffff\1\114\1\117\1\122\1\175\1\105\1\123\1\105\2\175\1\114\5\uffff\1\175\1\116\1\124\1\uffff\1\104\1\124\1\123\2\uffff\1\105\1\uffff\3\175\1\101\1\124\1\122\3\uffff\1\115\1\101\1\175\1\120\1\115\1\uffff\1\175\1\120\1\uffff\1\175\1\uffff";
    static final String DFA11_acceptS =
        "\1\uffff\1\1\2\uffff\1\4\1\5\5\uffff\1\13\1\14\1\15\1\16\1\uffff\1\20\4\uffff\1\33\1\34\7\uffff\1\64\5\uffff\1\107\3\uffff\1\115\1\116\1\1\4\uffff\1\107\5\uffff\1\4\1\5\12\uffff\1\13\1\14\1\15\1\16\5\uffff\1\20\12\uffff\1\33\1\34\1\113\1\114\1\35\10\uffff\1\46\2\uffff\1\64\1\65\1\66\1\71\1\67\1\70\2\uffff\1\111\1\110\1\uffff\1\112\1\115\23\uffff\1\11\2\uffff\1\17\5\uffff\1\21\1\53\14\uffff\1\51\3\uffff\1\62\5\uffff\1\26\24\uffff\1\22\1\uffff\1\63\2\uffff\1\23\1\24\17\uffff\1\72\5\uffff\1\44\1\55\3\uffff\1\52\1\uffff\1\6\1\uffff\1\47\16\uffff\1\31\1\77\3\uffff\1\73\1\57\2\uffff\1\50\12\uffff\1\30\1\74\1\100\1\7\1\uffff\1\10\6\uffff\1\25\5\uffff\1\60\3\uffff\1\2\1\43\1\101\4\uffff\1\76\2\uffff\1\12\1\uffff\1\106\1\uffff\1\27\1\uffff\1\105\12\uffff\1\36\1\102\1\56\1\75\1\45\3\uffff\1\42\3\uffff\1\3\1\32\1\uffff\1\37\6\uffff\1\61\1\40\1\54\5\uffff\1\41\2\uffff\1\104\1\uffff\1\103";
    static final String DFA11_specialS =
        "\1\2\45\uffff\1\0\1\1\u0130\uffff}>";
    static final String[] DFA11_transitionS = {
            "\11\51\2\50\2\51\1\50\22\51\1\50\1\37\1\46\1\51\1\44\2\51\1\47\1\13\1\14\1\4\1\25\1\5\1\26\1\20\1\27\12\45\1\33\1\1\1\41\1\36\1\40\2\51\1\17\1\11\1\23\1\3\1\43\1\6\1\10\1\12\1\21\2\44\1\24\1\22\1\42\1\32\1\30\2\44\1\2\1\31\1\35\1\34\1\7\3\44\1\15\1\51\1\16\1\51\1\44\1\51\33\44\1\51\1\44\uff82\51",
            "",
            "\1\53\3\uffff\1\56\12\uffff\1\55\1\54",
            "\1\62\7\uffff\1\60\5\uffff\1\64\2\uffff\1\63\34\uffff\1\61",
            "",
            "",
            "\1\71\7\uffff\1\70\2\uffff\1\72\5\uffff\1\67",
            "\1\73\11\uffff\1\74",
            "\1\75",
            "\1\77\11\uffff\1\76",
            "\1\100",
            "",
            "",
            "",
            "",
            "\1\107\11\uffff\1\110\4\uffff\1\105\1\111\1\uffff\1\106",
            "",
            "\1\114\7\uffff\1\113",
            "\1\116\3\uffff\1\117\3\uffff\1\115",
            "\1\121\6\uffff\1\120\2\uffff\1\122",
            "\1\123\15\uffff\1\124",
            "",
            "",
            "\1\127\4\uffff\1\130",
            "\1\133\20\uffff\1\132",
            "\1\136\5\uffff\1\135\2\uffff\1\134\2\uffff\1\137",
            "\1\140\1\uffff\1\141",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\143",
            "\1\144",
            "",
            "\1\146",
            "\1\147",
            "\1\151",
            "\1\153",
            "\1\154",
            "",
            "\1\156\1\uffff\12\157",
            "\0\160",
            "\0\160",
            "",
            "",
            "",
            "\1\162",
            "\1\163",
            "\1\165\20\uffff\1\164",
            "\1\166\13\uffff\1\167",
            "",
            "\1\170",
            "\1\171",
            "\1\172",
            "\1\173",
            "\1\174",
            "",
            "",
            "\1\175",
            "\1\177\5\uffff\1\176",
            "\1\u0080",
            "\1\u0081",
            "\1\u0082",
            "\1\u0083",
            "\1\u0084",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u0086",
            "\1\u0087",
            "",
            "",
            "",
            "",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u0089",
            "\1\u008a",
            "\1\u008b",
            "\1\u008c",
            "",
            "\1\57\13\uffff\13\57\6\uffff\23\57\1\u008d\6\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u008f",
            "\1\u0090",
            "\1\u0091",
            "\1\u0092",
            "\1\u0093",
            "\1\u0094",
            "\1\u0095",
            "\1\u0096",
            "\1\u0097",
            "",
            "",
            "",
            "",
            "",
            "\1\u0098",
            "\1\u0099",
            "\1\u009a\23\uffff\1\u009b",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u009d",
            "\1\u009e",
            "\1\u009f",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "",
            "\1\u00a1",
            "\1\u00a2",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u00a3",
            "\1\u00a4",
            "",
            "",
            "\1\156\1\uffff\12\157",
            "",
            "",
            "\1\u00a5",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u00a7\3\uffff\1\u00a8",
            "\1\u00a9",
            "\1\u00aa",
            "\1\u00ab",
            "\1\u00ac",
            "\1\u00ad",
            "\1\u00ae",
            "\1\u00af",
            "\1\u00b0",
            "\1\u00b1",
            "\1\u00b2",
            "\1\u00b3",
            "\1\u00b4",
            "\1\u00b5",
            "\1\u00b6",
            "\1\u00b7",
            "\1\u00b8",
            "",
            "\1\u00b9",
            "\1\u00ba",
            "",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u00bc",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u00be",
            "\1\u00bf",
            "",
            "",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u00c2",
            "\1\u00c3",
            "\1\u00c4",
            "\1\u00c5",
            "\1\u00c6",
            "\1\u00c7",
            "\1\u00c8",
            "\1\u00c9",
            "\1\u00ca",
            "\1\u00cb",
            "",
            "\1\u00cc",
            "\1\u00cd",
            "\1\u00ce",
            "",
            "\1\u00cf",
            "\1\u00d0",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u00d2",
            "\1\u00d3",
            "",
            "\1\u00d4",
            "\1\u00d5",
            "\1\u00d6",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u00d9",
            "\1\u00da",
            "\1\u00db",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u00dd",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u00df",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u00e1",
            "\1\u00e2",
            "\1\u00e3",
            "\1\u00e4",
            "\1\u00e5",
            "\1\u00e6",
            "\1\u00e7",
            "",
            "\1\u00e8",
            "",
            "\1\u00e9",
            "\1\u00ea",
            "",
            "",
            "\1\u00eb",
            "\1\u00ec",
            "\1\u00ed",
            "\1\u00ee",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u00f1",
            "\1\u00f2",
            "\1\u00f3",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u00f6",
            "\1\u00f7",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u00f9",
            "",
            "\1\u00fa",
            "\1\u00fb",
            "\1\u00fc",
            "\1\u00fd",
            "\1\u00fe",
            "",
            "",
            "\1\u00ff",
            "\1\u0100",
            "\1\u0101",
            "",
            "\1\u0102",
            "",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u0107",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u0109",
            "\1\u010a",
            "\1\u010b",
            "\1\u010c",
            "\1\u010d",
            "\1\u010e",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u0110",
            "\1\u0111",
            "",
            "",
            "\1\u0112",
            "\1\u0113",
            "\1\u0114",
            "",
            "",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u0116",
            "",
            "\1\u0117",
            "\1\u0118",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u011c",
            "\1\u011d",
            "\1\u011e",
            "\1\u011f",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "",
            "",
            "",
            "",
            "\1\u0121",
            "",
            "\1\u0122",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u0124",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u0126",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "",
            "\1\u0128",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u012a",
            "\1\u012b",
            "\1\u012c",
            "",
            "\1\u012d",
            "\1\u012e",
            "\1\u012f",
            "",
            "",
            "",
            "\1\u0130",
            "\1\u0131",
            "\1\u0132",
            "\1\u0133",
            "",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "",
            "\1\u0139",
            "\1\u013a",
            "\1\u013b",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u013d",
            "\1\u013e",
            "\1\u013f",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u0142",
            "",
            "",
            "",
            "",
            "",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u0144",
            "\1\u0145",
            "",
            "\1\u0146",
            "\1\u0147",
            "\1\u0148",
            "",
            "",
            "\1\u0149",
            "",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u014d",
            "\1\u014e",
            "\1\u014f",
            "",
            "",
            "",
            "\1\u0150",
            "\1\u0151",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u0153",
            "\1\u0154",
            "",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
            "\1\u0156",
            "",
            "\1\57\13\uffff\13\57\6\uffff\32\57\4\uffff\1\57\1\uffff\33\57\1\uffff\1\57",
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
            return "1:1: Tokens : ( T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | RULE_ID | RULE_FLOAT | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA11_38 = input.LA(1);

                        s = -1;
                        if ( ((LA11_38>='\u0000' && LA11_38<='\uFFFF')) ) {s = 112;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA11_39 = input.LA(1);

                        s = -1;
                        if ( ((LA11_39>='\u0000' && LA11_39<='\uFFFF')) ) {s = 112;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA11_0 = input.LA(1);

                        s = -1;
                        if ( (LA11_0==';') ) {s = 1;}

                        else if ( (LA11_0=='S') ) {s = 2;}

                        else if ( (LA11_0=='D') ) {s = 3;}

                        else if ( (LA11_0=='*') ) {s = 4;}

                        else if ( (LA11_0==',') ) {s = 5;}

                        else if ( (LA11_0=='F') ) {s = 6;}

                        else if ( (LA11_0=='W') ) {s = 7;}

                        else if ( (LA11_0=='G') ) {s = 8;}

                        else if ( (LA11_0=='B') ) {s = 9;}

                        else if ( (LA11_0=='H') ) {s = 10;}

                        else if ( (LA11_0=='(') ) {s = 11;}

                        else if ( (LA11_0==')') ) {s = 12;}

                        else if ( (LA11_0=='[') ) {s = 13;}

                        else if ( (LA11_0==']') ) {s = 14;}

                        else if ( (LA11_0=='A') ) {s = 15;}

                        else if ( (LA11_0=='.') ) {s = 16;}

                        else if ( (LA11_0=='I') ) {s = 17;}

                        else if ( (LA11_0=='M') ) {s = 18;}

                        else if ( (LA11_0=='C') ) {s = 19;}

                        else if ( (LA11_0=='L') ) {s = 20;}

                        else if ( (LA11_0=='+') ) {s = 21;}

                        else if ( (LA11_0=='-') ) {s = 22;}

                        else if ( (LA11_0=='/') ) {s = 23;}

                        else if ( (LA11_0=='P') ) {s = 24;}

                        else if ( (LA11_0=='T') ) {s = 25;}

                        else if ( (LA11_0=='O') ) {s = 26;}

                        else if ( (LA11_0==':') ) {s = 27;}

                        else if ( (LA11_0=='V') ) {s = 28;}

                        else if ( (LA11_0=='U') ) {s = 29;}

                        else if ( (LA11_0=='=') ) {s = 30;}

                        else if ( (LA11_0=='!') ) {s = 31;}

                        else if ( (LA11_0=='>') ) {s = 32;}

                        else if ( (LA11_0=='<') ) {s = 33;}

                        else if ( (LA11_0=='N') ) {s = 34;}

                        else if ( (LA11_0=='E') ) {s = 35;}

                        else if ( (LA11_0=='$'||(LA11_0>='J' && LA11_0<='K')||(LA11_0>='Q' && LA11_0<='R')||(LA11_0>='X' && LA11_0<='Z')||LA11_0=='_'||(LA11_0>='a' && LA11_0<='{')||LA11_0=='}') ) {s = 36;}

                        else if ( ((LA11_0>='0' && LA11_0<='9')) ) {s = 37;}

                        else if ( (LA11_0=='\"') ) {s = 38;}

                        else if ( (LA11_0=='\'') ) {s = 39;}

                        else if ( ((LA11_0>='\t' && LA11_0<='\n')||LA11_0=='\r'||LA11_0==' ') ) {s = 40;}

                        else if ( ((LA11_0>='\u0000' && LA11_0<='\b')||(LA11_0>='\u000B' && LA11_0<='\f')||(LA11_0>='\u000E' && LA11_0<='\u001F')||LA11_0=='#'||(LA11_0>='%' && LA11_0<='&')||(LA11_0>='?' && LA11_0<='@')||LA11_0=='\\'||LA11_0=='^'||LA11_0=='`'||LA11_0=='|'||(LA11_0>='~' && LA11_0<='\uFFFF')) ) {s = 41;}

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