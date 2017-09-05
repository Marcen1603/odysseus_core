package de.uniol.inf.is.odysseus.server.ide.contentassist.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.Lexer;


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
    public String getGrammarFileName() { return "InternalStreamingsparql.g"; }

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalStreamingsparql.g:11:7: ( '#RUNQUERY' )
            // InternalStreamingsparql.g:11:9: '#RUNQUERY'
            {
            match("#RUNQUERY"); 


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
            // InternalStreamingsparql.g:12:7: ( '<' )
            // InternalStreamingsparql.g:12:9: '<'
            {
            match('<'); 

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
            // InternalStreamingsparql.g:13:7: ( '>' )
            // InternalStreamingsparql.g:13:9: '>'
            {
            match('>'); 

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
            // InternalStreamingsparql.g:14:7: ( '<=' )
            // InternalStreamingsparql.g:14:9: '<='
            {
            match("<="); 


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
            // InternalStreamingsparql.g:15:7: ( '>=' )
            // InternalStreamingsparql.g:15:9: '>='
            {
            match(">="); 


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
            // InternalStreamingsparql.g:16:7: ( '=' )
            // InternalStreamingsparql.g:16:9: '='
            {
            match('='); 

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
            // InternalStreamingsparql.g:17:7: ( '!=' )
            // InternalStreamingsparql.g:17:9: '!='
            {
            match("!="); 


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
            // InternalStreamingsparql.g:18:7: ( '+' )
            // InternalStreamingsparql.g:18:9: '+'
            {
            match('+'); 

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
            // InternalStreamingsparql.g:19:7: ( '/' )
            // InternalStreamingsparql.g:19:9: '/'
            {
            match('/'); 

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
            // InternalStreamingsparql.g:20:7: ( '-' )
            // InternalStreamingsparql.g:20:9: '-'
            {
            match('-'); 

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
            // InternalStreamingsparql.g:21:7: ( '*' )
            // InternalStreamingsparql.g:21:9: '*'
            {
            match('*'); 

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
            // InternalStreamingsparql.g:22:7: ( 'PREFIX' )
            // InternalStreamingsparql.g:22:9: 'PREFIX'
            {
            match("PREFIX"); 


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
            // InternalStreamingsparql.g:23:7: ( ':' )
            // InternalStreamingsparql.g:23:9: ':'
            {
            match(':'); 

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
            // InternalStreamingsparql.g:24:7: ( 'BASE' )
            // InternalStreamingsparql.g:24:9: 'BASE'
            {
            match("BASE"); 


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
            // InternalStreamingsparql.g:25:7: ( 'SELECT' )
            // InternalStreamingsparql.g:25:9: 'SELECT'
            {
            match("SELECT"); 


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
            // InternalStreamingsparql.g:26:7: ( 'AGGREGATE(' )
            // InternalStreamingsparql.g:26:9: 'AGGREGATE('
            {
            match("AGGREGATE("); 


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
            // InternalStreamingsparql.g:27:7: ( ')' )
            // InternalStreamingsparql.g:27:9: ')'
            {
            match(')'); 

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
            // InternalStreamingsparql.g:28:7: ( 'aggregations' )
            // InternalStreamingsparql.g:28:9: 'aggregations'
            {
            match("aggregations"); 


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
            // InternalStreamingsparql.g:29:7: ( '[' )
            // InternalStreamingsparql.g:29:9: '['
            {
            match('['); 

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
            // InternalStreamingsparql.g:30:7: ( ']' )
            // InternalStreamingsparql.g:30:9: ']'
            {
            match(']'); 

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
            // InternalStreamingsparql.g:31:7: ( ',' )
            // InternalStreamingsparql.g:31:9: ','
            {
            match(','); 

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
            // InternalStreamingsparql.g:32:7: ( 'group_by=[' )
            // InternalStreamingsparql.g:32:9: 'group_by=['
            {
            match("group_by=["); 


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
            // InternalStreamingsparql.g:33:7: ( 'CSVFILESINK(' )
            // InternalStreamingsparql.g:33:9: 'CSVFILESINK('
            {
            match("CSVFILESINK("); 


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
            // InternalStreamingsparql.g:34:7: ( 'FILTER(' )
            // InternalStreamingsparql.g:34:9: 'FILTER('
            {
            match("FILTER("); 


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
            // InternalStreamingsparql.g:35:7: ( 'FROM' )
            // InternalStreamingsparql.g:35:9: 'FROM'
            {
            match("FROM"); 


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
            // InternalStreamingsparql.g:36:7: ( 'AS' )
            // InternalStreamingsparql.g:36:9: 'AS'
            {
            match("AS"); 


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
            // InternalStreamingsparql.g:37:7: ( 'TYPE' )
            // InternalStreamingsparql.g:37:9: 'TYPE'
            {
            match("TYPE"); 


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
            // InternalStreamingsparql.g:38:7: ( 'SIZE' )
            // InternalStreamingsparql.g:38:9: 'SIZE'
            {
            match("SIZE"); 


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
            // InternalStreamingsparql.g:39:7: ( 'ADVANCE' )
            // InternalStreamingsparql.g:39:9: 'ADVANCE'
            {
            match("ADVANCE"); 


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
            // InternalStreamingsparql.g:40:7: ( 'UNIT' )
            // InternalStreamingsparql.g:40:9: 'UNIT'
            {
            match("UNIT"); 


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
            // InternalStreamingsparql.g:41:7: ( 'WHERE' )
            // InternalStreamingsparql.g:41:9: 'WHERE'
            {
            match("WHERE"); 


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
            // InternalStreamingsparql.g:42:7: ( '{' )
            // InternalStreamingsparql.g:42:9: '{'
            {
            match('{'); 

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
            // InternalStreamingsparql.g:43:7: ( '}' )
            // InternalStreamingsparql.g:43:9: '}'
            {
            match('}'); 

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
            // InternalStreamingsparql.g:44:7: ( '.' )
            // InternalStreamingsparql.g:44:9: '.'
            {
            match('.'); 

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
            // InternalStreamingsparql.g:45:7: ( ';' )
            // InternalStreamingsparql.g:45:9: ';'
            {
            match(';'); 

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
            // InternalStreamingsparql.g:46:7: ( '?' )
            // InternalStreamingsparql.g:46:9: '?'
            {
            match('?'); 

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
            // InternalStreamingsparql.g:47:7: ( '#ADDQUERY' )
            // InternalStreamingsparql.g:47:9: '#ADDQUERY'
            {
            match("#ADDQUERY"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__51"

    // $ANTLR start "RULE_AGG_FUNCTION"
    public final void mRULE_AGG_FUNCTION() throws RecognitionException {
        try {
            int _type = RULE_AGG_FUNCTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalStreamingsparql.g:4263:19: ( ( 'COUNT' | 'MAX' | 'MIN' | 'AVG' | 'SUM' | 'MEDIAN' ) )
            // InternalStreamingsparql.g:4263:21: ( 'COUNT' | 'MAX' | 'MIN' | 'AVG' | 'SUM' | 'MEDIAN' )
            {
            // InternalStreamingsparql.g:4263:21: ( 'COUNT' | 'MAX' | 'MIN' | 'AVG' | 'SUM' | 'MEDIAN' )
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
                    // InternalStreamingsparql.g:4263:22: 'COUNT'
                    {
                    match("COUNT"); 


                    }
                    break;
                case 2 :
                    // InternalStreamingsparql.g:4263:30: 'MAX'
                    {
                    match("MAX"); 


                    }
                    break;
                case 3 :
                    // InternalStreamingsparql.g:4263:36: 'MIN'
                    {
                    match("MIN"); 


                    }
                    break;
                case 4 :
                    // InternalStreamingsparql.g:4263:42: 'AVG'
                    {
                    match("AVG"); 


                    }
                    break;
                case 5 :
                    // InternalStreamingsparql.g:4263:48: 'SUM'
                    {
                    match("SUM"); 


                    }
                    break;
                case 6 :
                    // InternalStreamingsparql.g:4263:54: 'MEDIAN'
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
            // InternalStreamingsparql.g:4265:19: ( '<' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' | '/' | ':' | '.' | '#' | '?' | '@' | '$' | '&' | '=' )* '>' )
            // InternalStreamingsparql.g:4265:21: '<' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' | '/' | ':' | '.' | '#' | '?' | '@' | '$' | '&' | '=' )* '>'
            {
            match('<'); 
            // InternalStreamingsparql.g:4265:25: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' | '/' | ':' | '.' | '#' | '?' | '@' | '$' | '&' | '=' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='#' && LA2_0<='$')||LA2_0=='&'||(LA2_0>='-' && LA2_0<=':')||LA2_0=='='||(LA2_0>='?' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalStreamingsparql.g:
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
            // InternalStreamingsparql.g:4267:13: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' )
            // InternalStreamingsparql.g:4267:15: '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"'
            {
            match('\"'); 
            // InternalStreamingsparql.g:4267:19: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )*
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
            	    // InternalStreamingsparql.g:4267:20: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' )
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
            	    // InternalStreamingsparql.g:4267:61: ~ ( ( '\\\\' | '\"' ) )
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
            // InternalStreamingsparql.g:4269:17: ( ( 'ELEMENT' | 'TIME' ) )
            // InternalStreamingsparql.g:4269:19: ( 'ELEMENT' | 'TIME' )
            {
            // InternalStreamingsparql.g:4269:19: ( 'ELEMENT' | 'TIME' )
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
                    // InternalStreamingsparql.g:4269:20: 'ELEMENT'
                    {
                    match("ELEMENT"); 


                    }
                    break;
                case 2 :
                    // InternalStreamingsparql.g:4269:30: 'TIME'
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
            // InternalStreamingsparql.g:4271:15: ( ( 'NANOSECONDS' | 'MICROSECONDS' | 'MILLISECONDS' | 'SECONDS' | 'MINUTES' | 'HOURS' | 'DAYS' ) )
            // InternalStreamingsparql.g:4271:17: ( 'NANOSECONDS' | 'MICROSECONDS' | 'MILLISECONDS' | 'SECONDS' | 'MINUTES' | 'HOURS' | 'DAYS' )
            {
            // InternalStreamingsparql.g:4271:17: ( 'NANOSECONDS' | 'MICROSECONDS' | 'MILLISECONDS' | 'SECONDS' | 'MINUTES' | 'HOURS' | 'DAYS' )
            int alt5=7;
            alt5 = dfa5.predict(input);
            switch (alt5) {
                case 1 :
                    // InternalStreamingsparql.g:4271:18: 'NANOSECONDS'
                    {
                    match("NANOSECONDS"); 


                    }
                    break;
                case 2 :
                    // InternalStreamingsparql.g:4271:32: 'MICROSECONDS'
                    {
                    match("MICROSECONDS"); 


                    }
                    break;
                case 3 :
                    // InternalStreamingsparql.g:4271:47: 'MILLISECONDS'
                    {
                    match("MILLISECONDS"); 


                    }
                    break;
                case 4 :
                    // InternalStreamingsparql.g:4271:62: 'SECONDS'
                    {
                    match("SECONDS"); 


                    }
                    break;
                case 5 :
                    // InternalStreamingsparql.g:4271:72: 'MINUTES'
                    {
                    match("MINUTES"); 


                    }
                    break;
                case 6 :
                    // InternalStreamingsparql.g:4271:82: 'HOURS'
                    {
                    match("HOURS"); 


                    }
                    break;
                case 7 :
                    // InternalStreamingsparql.g:4271:90: 'DAYS'
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
            // InternalStreamingsparql.g:4273:9: ( ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // InternalStreamingsparql.g:4273:11: ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            // InternalStreamingsparql.g:4273:11: ( '^' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='^') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // InternalStreamingsparql.g:4273:11: '^'
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

            // InternalStreamingsparql.g:4273:40: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='0' && LA7_0<='9')||(LA7_0>='A' && LA7_0<='Z')||LA7_0=='_'||(LA7_0>='a' && LA7_0<='z')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // InternalStreamingsparql.g:
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
            // InternalStreamingsparql.g:4275:10: ( ( '0' .. '9' )+ )
            // InternalStreamingsparql.g:4275:12: ( '0' .. '9' )+
            {
            // InternalStreamingsparql.g:4275:12: ( '0' .. '9' )+
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
            	    // InternalStreamingsparql.g:4275:13: '0' .. '9'
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
            // InternalStreamingsparql.g:4277:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // InternalStreamingsparql.g:4277:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // InternalStreamingsparql.g:4277:24: ( options {greedy=false; } : . )*
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
            	    // InternalStreamingsparql.g:4277:52: .
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
            // InternalStreamingsparql.g:4279:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // InternalStreamingsparql.g:4279:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // InternalStreamingsparql.g:4279:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( ((LA10_0>='\u0000' && LA10_0<='\t')||(LA10_0>='\u000B' && LA10_0<='\f')||(LA10_0>='\u000E' && LA10_0<='\uFFFF')) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // InternalStreamingsparql.g:4279:24: ~ ( ( '\\n' | '\\r' ) )
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

            // InternalStreamingsparql.g:4279:40: ( ( '\\r' )? '\\n' )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0=='\n'||LA12_0=='\r') ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // InternalStreamingsparql.g:4279:41: ( '\\r' )? '\\n'
                    {
                    // InternalStreamingsparql.g:4279:41: ( '\\r' )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0=='\r') ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // InternalStreamingsparql.g:4279:41: '\\r'
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
            // InternalStreamingsparql.g:4281:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // InternalStreamingsparql.g:4281:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // InternalStreamingsparql.g:4281:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
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
            	    // InternalStreamingsparql.g:
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
            // InternalStreamingsparql.g:4283:16: ( . )
            // InternalStreamingsparql.g:4283:18: .
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
        // InternalStreamingsparql.g:1:8: ( T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | RULE_AGG_FUNCTION | RULE_IRI_TERMINAL | RULE_STRING | RULE_WINDOWTYPE | RULE_UNITTYPE | RULE_ID | RULE_INT | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt14=48;
        alt14 = dfa14.predict(input);
        switch (alt14) {
            case 1 :
                // InternalStreamingsparql.g:1:10: T__15
                {
                mT__15(); 

                }
                break;
            case 2 :
                // InternalStreamingsparql.g:1:16: T__16
                {
                mT__16(); 

                }
                break;
            case 3 :
                // InternalStreamingsparql.g:1:22: T__17
                {
                mT__17(); 

                }
                break;
            case 4 :
                // InternalStreamingsparql.g:1:28: T__18
                {
                mT__18(); 

                }
                break;
            case 5 :
                // InternalStreamingsparql.g:1:34: T__19
                {
                mT__19(); 

                }
                break;
            case 6 :
                // InternalStreamingsparql.g:1:40: T__20
                {
                mT__20(); 

                }
                break;
            case 7 :
                // InternalStreamingsparql.g:1:46: T__21
                {
                mT__21(); 

                }
                break;
            case 8 :
                // InternalStreamingsparql.g:1:52: T__22
                {
                mT__22(); 

                }
                break;
            case 9 :
                // InternalStreamingsparql.g:1:58: T__23
                {
                mT__23(); 

                }
                break;
            case 10 :
                // InternalStreamingsparql.g:1:64: T__24
                {
                mT__24(); 

                }
                break;
            case 11 :
                // InternalStreamingsparql.g:1:70: T__25
                {
                mT__25(); 

                }
                break;
            case 12 :
                // InternalStreamingsparql.g:1:76: T__26
                {
                mT__26(); 

                }
                break;
            case 13 :
                // InternalStreamingsparql.g:1:82: T__27
                {
                mT__27(); 

                }
                break;
            case 14 :
                // InternalStreamingsparql.g:1:88: T__28
                {
                mT__28(); 

                }
                break;
            case 15 :
                // InternalStreamingsparql.g:1:94: T__29
                {
                mT__29(); 

                }
                break;
            case 16 :
                // InternalStreamingsparql.g:1:100: T__30
                {
                mT__30(); 

                }
                break;
            case 17 :
                // InternalStreamingsparql.g:1:106: T__31
                {
                mT__31(); 

                }
                break;
            case 18 :
                // InternalStreamingsparql.g:1:112: T__32
                {
                mT__32(); 

                }
                break;
            case 19 :
                // InternalStreamingsparql.g:1:118: T__33
                {
                mT__33(); 

                }
                break;
            case 20 :
                // InternalStreamingsparql.g:1:124: T__34
                {
                mT__34(); 

                }
                break;
            case 21 :
                // InternalStreamingsparql.g:1:130: T__35
                {
                mT__35(); 

                }
                break;
            case 22 :
                // InternalStreamingsparql.g:1:136: T__36
                {
                mT__36(); 

                }
                break;
            case 23 :
                // InternalStreamingsparql.g:1:142: T__37
                {
                mT__37(); 

                }
                break;
            case 24 :
                // InternalStreamingsparql.g:1:148: T__38
                {
                mT__38(); 

                }
                break;
            case 25 :
                // InternalStreamingsparql.g:1:154: T__39
                {
                mT__39(); 

                }
                break;
            case 26 :
                // InternalStreamingsparql.g:1:160: T__40
                {
                mT__40(); 

                }
                break;
            case 27 :
                // InternalStreamingsparql.g:1:166: T__41
                {
                mT__41(); 

                }
                break;
            case 28 :
                // InternalStreamingsparql.g:1:172: T__42
                {
                mT__42(); 

                }
                break;
            case 29 :
                // InternalStreamingsparql.g:1:178: T__43
                {
                mT__43(); 

                }
                break;
            case 30 :
                // InternalStreamingsparql.g:1:184: T__44
                {
                mT__44(); 

                }
                break;
            case 31 :
                // InternalStreamingsparql.g:1:190: T__45
                {
                mT__45(); 

                }
                break;
            case 32 :
                // InternalStreamingsparql.g:1:196: T__46
                {
                mT__46(); 

                }
                break;
            case 33 :
                // InternalStreamingsparql.g:1:202: T__47
                {
                mT__47(); 

                }
                break;
            case 34 :
                // InternalStreamingsparql.g:1:208: T__48
                {
                mT__48(); 

                }
                break;
            case 35 :
                // InternalStreamingsparql.g:1:214: T__49
                {
                mT__49(); 

                }
                break;
            case 36 :
                // InternalStreamingsparql.g:1:220: T__50
                {
                mT__50(); 

                }
                break;
            case 37 :
                // InternalStreamingsparql.g:1:226: T__51
                {
                mT__51(); 

                }
                break;
            case 38 :
                // InternalStreamingsparql.g:1:232: RULE_AGG_FUNCTION
                {
                mRULE_AGG_FUNCTION(); 

                }
                break;
            case 39 :
                // InternalStreamingsparql.g:1:250: RULE_IRI_TERMINAL
                {
                mRULE_IRI_TERMINAL(); 

                }
                break;
            case 40 :
                // InternalStreamingsparql.g:1:268: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 41 :
                // InternalStreamingsparql.g:1:280: RULE_WINDOWTYPE
                {
                mRULE_WINDOWTYPE(); 

                }
                break;
            case 42 :
                // InternalStreamingsparql.g:1:296: RULE_UNITTYPE
                {
                mRULE_UNITTYPE(); 

                }
                break;
            case 43 :
                // InternalStreamingsparql.g:1:310: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 44 :
                // InternalStreamingsparql.g:1:318: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 45 :
                // InternalStreamingsparql.g:1:327: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 46 :
                // InternalStreamingsparql.g:1:343: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 47 :
                // InternalStreamingsparql.g:1:359: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 48 :
                // InternalStreamingsparql.g:1:367: RULE_ANY_OTHER
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
            return "4271:17: ( 'NANOSECONDS' | 'MICROSECONDS' | 'MILLISECONDS' | 'SECONDS' | 'MINUTES' | 'HOURS' | 'DAYS' )";
        }
    }
    static final String DFA14_eotS =
        "\1\uffff\1\51\1\56\1\60\1\uffff\1\51\1\uffff\1\66\2\uffff\1\72\1\uffff\3\72\1\uffff\1\72\3\uffff\6\72\5\uffff\1\72\1\51\4\72\1\51\6\uffff\1\141\14\uffff\1\72\2\uffff\5\72\1\151\2\72\1\uffff\1\72\3\uffff\11\72\5\uffff\3\72\1\uffff\4\72\3\uffff\5\72\1\u0084\1\72\1\uffff\1\72\1\u0084\12\72\2\u0084\10\72\1\u009a\2\72\1\u009d\1\uffff\7\72\1\u00a5\1\u00a6\1\u00a7\1\u00a8\10\72\1\u00b1\1\72\1\uffff\2\72\1\uffff\5\72\1\u0084\1\72\4\uffff\1\u00bb\6\72\1\u00b1\1\uffff\1\u00c2\1\u00c3\7\72\1\uffff\3\72\1\u0084\2\72\2\uffff\1\u00b1\1\72\1\u00d1\3\72\1\uffff\1\u00b1\2\72\1\u00a7\2\72\1\uffff\10\72\1\uffff\4\72\1\uffff\11\72\1\u00b1\1\u00ee\1\uffff\2\u00b1\1\uffff";
    static final String DFA14_eofS =
        "\u00ef\uffff";
    static final String DFA14_minS =
        "\1\0\1\101\1\43\1\75\1\uffff\1\75\1\uffff\1\52\2\uffff\1\122\1\uffff\1\101\1\105\1\104\1\uffff\1\147\3\uffff\1\162\1\117\2\111\1\116\1\110\5\uffff\1\101\1\0\1\114\1\101\1\117\2\101\6\uffff\1\43\14\uffff\1\105\2\uffff\1\123\1\103\1\132\1\115\1\107\1\60\1\126\1\107\1\uffff\1\147\3\uffff\1\157\1\126\1\125\1\114\1\117\1\120\1\115\1\111\1\105\5\uffff\1\130\1\103\1\104\1\uffff\1\105\1\116\1\125\1\131\3\uffff\1\106\2\105\1\117\1\105\1\60\1\122\1\uffff\1\101\1\60\1\162\1\165\1\106\1\116\1\124\1\115\2\105\1\124\1\122\2\60\1\122\1\114\1\111\1\115\1\117\1\122\1\123\1\111\1\60\1\103\1\116\1\60\1\uffff\1\105\1\116\1\145\1\160\1\111\1\124\1\105\4\60\1\105\1\124\1\117\1\111\1\101\1\105\2\123\1\60\1\130\1\uffff\1\124\1\104\1\uffff\1\107\1\103\1\147\1\137\1\114\1\60\1\122\4\uffff\1\60\1\105\2\123\2\116\1\105\1\60\1\uffff\2\60\1\123\1\101\1\105\1\141\1\142\1\105\1\50\1\uffff\1\123\2\105\1\60\1\124\1\103\2\uffff\1\60\1\124\1\60\1\164\1\171\1\123\1\uffff\1\60\2\103\1\60\1\117\1\105\1\uffff\1\151\1\75\1\111\2\117\1\116\1\50\1\157\1\uffff\3\116\1\104\1\uffff\1\156\1\113\2\104\1\123\1\163\1\50\2\123\2\60\1\uffff\2\60\1\uffff";
    static final String DFA14_maxS =
        "\1\uffff\1\122\1\172\1\75\1\uffff\1\75\1\uffff\1\57\2\uffff\1\122\1\uffff\1\101\1\125\1\126\1\uffff\1\147\3\uffff\1\162\1\123\1\122\1\131\1\116\1\110\5\uffff\1\111\1\uffff\1\114\1\101\1\117\1\101\1\172\6\uffff\1\172\14\uffff\1\105\2\uffff\1\123\1\114\1\132\1\115\1\107\1\172\1\126\1\107\1\uffff\1\147\3\uffff\1\157\1\126\1\125\1\114\1\117\1\120\1\115\1\111\1\105\5\uffff\1\130\1\116\1\104\1\uffff\1\105\1\116\1\125\1\131\3\uffff\1\106\2\105\1\117\1\105\1\172\1\122\1\uffff\1\101\1\172\1\162\1\165\1\106\1\116\1\124\1\115\2\105\1\124\1\122\2\172\1\122\1\114\1\111\1\115\1\117\1\122\1\123\1\111\1\172\1\103\1\116\1\172\1\uffff\1\105\1\116\1\145\1\160\1\111\1\124\1\105\4\172\1\105\1\124\1\117\1\111\1\101\1\105\2\123\1\172\1\130\1\uffff\1\124\1\104\1\uffff\1\107\1\103\1\147\1\137\1\114\1\172\1\122\4\uffff\1\172\1\105\2\123\2\116\1\105\1\172\1\uffff\2\172\1\123\1\101\1\105\1\141\1\142\1\105\1\50\1\uffff\1\123\2\105\1\172\1\124\1\103\2\uffff\1\172\1\124\1\172\1\164\1\171\1\123\1\uffff\1\172\2\103\1\172\1\117\1\105\1\uffff\1\151\1\75\1\111\2\117\1\116\1\50\1\157\1\uffff\3\116\1\104\1\uffff\1\156\1\113\2\104\1\123\1\163\1\50\2\123\2\172\1\uffff\2\172\1\uffff";
    static final String DFA14_acceptS =
        "\4\uffff\1\6\1\uffff\1\10\1\uffff\1\12\1\13\1\uffff\1\15\3\uffff\1\21\1\uffff\1\23\1\24\1\25\6\uffff\1\40\1\41\1\42\1\43\1\44\7\uffff\1\53\1\54\1\57\1\60\1\1\1\45\1\uffff\1\47\1\2\1\5\1\3\1\6\1\7\1\10\1\55\1\56\1\11\1\12\1\13\1\uffff\1\53\1\15\10\uffff\1\21\1\uffff\1\23\1\24\1\25\11\uffff\1\40\1\41\1\42\1\43\1\44\3\uffff\1\50\4\uffff\1\54\1\57\1\4\7\uffff\1\32\32\uffff\1\46\25\uffff\1\16\2\uffff\1\34\7\uffff\1\31\1\33\1\51\1\36\10\uffff\1\52\11\uffff\1\37\6\uffff\1\14\1\17\6\uffff\1\30\6\uffff\1\35\10\uffff\1\26\4\uffff\1\20\13\uffff\1\27\2\uffff\1\22";
    static final String DFA14_specialS =
        "\1\0\37\uffff\1\1\u00ce\uffff}>";
    static final String[] DFA14_transitionS = {
            "\11\51\2\50\2\51\1\50\22\51\1\50\1\5\1\40\1\1\5\51\1\17\1\11\1\6\1\23\1\10\1\34\1\7\12\47\1\13\1\35\1\2\1\4\1\3\1\36\1\51\1\16\1\14\1\25\1\44\1\41\1\26\1\46\1\43\4\46\1\37\1\42\1\46\1\12\2\46\1\15\1\27\1\30\1\46\1\31\3\46\1\21\1\51\1\22\1\45\1\46\1\51\1\20\5\46\1\24\23\46\1\32\1\51\1\33\uff82\51",
            "\1\53\20\uffff\1\52",
            "\2\55\1\uffff\1\55\6\uffff\16\55\2\uffff\1\54\35\55\4\uffff\1\55\1\uffff\32\55",
            "\1\57",
            "",
            "\1\62",
            "",
            "\1\64\4\uffff\1\65",
            "",
            "",
            "\1\71",
            "",
            "\1\74",
            "\1\75\3\uffff\1\76\13\uffff\1\77",
            "\1\102\2\uffff\1\100\13\uffff\1\101\2\uffff\1\103",
            "",
            "\1\105",
            "",
            "",
            "",
            "\1\111",
            "\1\113\3\uffff\1\112",
            "\1\114\10\uffff\1\115",
            "\1\117\17\uffff\1\116",
            "\1\120",
            "\1\121",
            "",
            "",
            "",
            "",
            "",
            "\1\127\3\uffff\1\131\3\uffff\1\130",
            "\0\132",
            "\1\133",
            "\1\134",
            "\1\135",
            "\1\136",
            "\32\72\4\uffff\1\72\1\uffff\32\72",
            "",
            "",
            "",
            "",
            "",
            "",
            "\2\55\1\uffff\1\55\6\uffff\16\55\2\uffff\36\55\4\uffff\1\55\1\uffff\32\55",
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
            "\1\142",
            "",
            "",
            "\1\143",
            "\1\145\10\uffff\1\144",
            "\1\146",
            "\1\147",
            "\1\150",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\1\152",
            "\1\153",
            "",
            "\1\154",
            "",
            "",
            "",
            "\1\155",
            "\1\156",
            "\1\157",
            "\1\160",
            "\1\161",
            "\1\162",
            "\1\163",
            "\1\164",
            "\1\165",
            "",
            "",
            "",
            "",
            "",
            "\1\166",
            "\1\170\10\uffff\1\171\1\uffff\1\167",
            "\1\172",
            "",
            "\1\173",
            "\1\174",
            "\1\175",
            "\1\176",
            "",
            "",
            "",
            "\1\177",
            "\1\u0080",
            "\1\u0081",
            "\1\u0082",
            "\1\u0083",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\1\u0085",
            "",
            "\1\u0086",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\1\u0087",
            "\1\u0088",
            "\1\u0089",
            "\1\u008a",
            "\1\u008b",
            "\1\u008c",
            "\1\u008d",
            "\1\u008e",
            "\1\u008f",
            "\1\u0090",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\12\72\7\uffff\24\72\1\u0091\5\72\4\uffff\1\72\1\uffff\32\72",
            "\1\u0092",
            "\1\u0093",
            "\1\u0094",
            "\1\u0095",
            "\1\u0096",
            "\1\u0097",
            "\1\u0098",
            "\1\u0099",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\1\u009b",
            "\1\u009c",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "",
            "\1\u009e",
            "\1\u009f",
            "\1\u00a0",
            "\1\u00a1",
            "\1\u00a2",
            "\1\u00a3",
            "\1\u00a4",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\1\u00a9",
            "\1\u00aa",
            "\1\u00ab",
            "\1\u00ac",
            "\1\u00ad",
            "\1\u00ae",
            "\1\u00af",
            "\1\u00b0",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\1\u00b2",
            "",
            "\1\u00b3",
            "\1\u00b4",
            "",
            "\1\u00b5",
            "\1\u00b6",
            "\1\u00b7",
            "\1\u00b8",
            "\1\u00b9",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\1\u00ba",
            "",
            "",
            "",
            "",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\1\u00bc",
            "\1\u00bd",
            "\1\u00be",
            "\1\u00bf",
            "\1\u00c0",
            "\1\u00c1",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\1\u00c4",
            "\1\u00c5",
            "\1\u00c6",
            "\1\u00c7",
            "\1\u00c8",
            "\1\u00c9",
            "\1\u00ca",
            "",
            "\1\u00cb",
            "\1\u00cc",
            "\1\u00cd",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\1\u00ce",
            "\1\u00cf",
            "",
            "",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\1\u00d0",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\1\u00d2",
            "\1\u00d3",
            "\1\u00d4",
            "",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\1\u00d5",
            "\1\u00d6",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\1\u00d7",
            "\1\u00d8",
            "",
            "\1\u00d9",
            "\1\u00da",
            "\1\u00db",
            "\1\u00dc",
            "\1\u00dd",
            "\1\u00de",
            "\1\u00df",
            "\1\u00e0",
            "",
            "\1\u00e1",
            "\1\u00e2",
            "\1\u00e3",
            "\1\u00e4",
            "",
            "\1\u00e5",
            "\1\u00e6",
            "\1\u00e7",
            "\1\u00e8",
            "\1\u00e9",
            "\1\u00ea",
            "\1\u00eb",
            "\1\u00ec",
            "\1\u00ed",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
            "\12\72\7\uffff\32\72\4\uffff\1\72\1\uffff\32\72",
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
            return "1:1: Tokens : ( T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | RULE_AGG_FUNCTION | RULE_IRI_TERMINAL | RULE_STRING | RULE_WINDOWTYPE | RULE_UNITTYPE | RULE_ID | RULE_INT | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA14_0 = input.LA(1);

                        s = -1;
                        if ( (LA14_0=='#') ) {s = 1;}

                        else if ( (LA14_0=='<') ) {s = 2;}

                        else if ( (LA14_0=='>') ) {s = 3;}

                        else if ( (LA14_0=='=') ) {s = 4;}

                        else if ( (LA14_0=='!') ) {s = 5;}

                        else if ( (LA14_0=='+') ) {s = 6;}

                        else if ( (LA14_0=='/') ) {s = 7;}

                        else if ( (LA14_0=='-') ) {s = 8;}

                        else if ( (LA14_0=='*') ) {s = 9;}

                        else if ( (LA14_0=='P') ) {s = 10;}

                        else if ( (LA14_0==':') ) {s = 11;}

                        else if ( (LA14_0=='B') ) {s = 12;}

                        else if ( (LA14_0=='S') ) {s = 13;}

                        else if ( (LA14_0=='A') ) {s = 14;}

                        else if ( (LA14_0==')') ) {s = 15;}

                        else if ( (LA14_0=='a') ) {s = 16;}

                        else if ( (LA14_0=='[') ) {s = 17;}

                        else if ( (LA14_0==']') ) {s = 18;}

                        else if ( (LA14_0==',') ) {s = 19;}

                        else if ( (LA14_0=='g') ) {s = 20;}

                        else if ( (LA14_0=='C') ) {s = 21;}

                        else if ( (LA14_0=='F') ) {s = 22;}

                        else if ( (LA14_0=='T') ) {s = 23;}

                        else if ( (LA14_0=='U') ) {s = 24;}

                        else if ( (LA14_0=='W') ) {s = 25;}

                        else if ( (LA14_0=='{') ) {s = 26;}

                        else if ( (LA14_0=='}') ) {s = 27;}

                        else if ( (LA14_0=='.') ) {s = 28;}

                        else if ( (LA14_0==';') ) {s = 29;}

                        else if ( (LA14_0=='?') ) {s = 30;}

                        else if ( (LA14_0=='M') ) {s = 31;}

                        else if ( (LA14_0=='\"') ) {s = 32;}

                        else if ( (LA14_0=='E') ) {s = 33;}

                        else if ( (LA14_0=='N') ) {s = 34;}

                        else if ( (LA14_0=='H') ) {s = 35;}

                        else if ( (LA14_0=='D') ) {s = 36;}

                        else if ( (LA14_0=='^') ) {s = 37;}

                        else if ( (LA14_0=='G'||(LA14_0>='I' && LA14_0<='L')||LA14_0=='O'||(LA14_0>='Q' && LA14_0<='R')||LA14_0=='V'||(LA14_0>='X' && LA14_0<='Z')||LA14_0=='_'||(LA14_0>='b' && LA14_0<='f')||(LA14_0>='h' && LA14_0<='z')) ) {s = 38;}

                        else if ( ((LA14_0>='0' && LA14_0<='9')) ) {s = 39;}

                        else if ( ((LA14_0>='\t' && LA14_0<='\n')||LA14_0=='\r'||LA14_0==' ') ) {s = 40;}

                        else if ( ((LA14_0>='\u0000' && LA14_0<='\b')||(LA14_0>='\u000B' && LA14_0<='\f')||(LA14_0>='\u000E' && LA14_0<='\u001F')||(LA14_0>='$' && LA14_0<='(')||LA14_0=='@'||LA14_0=='\\'||LA14_0=='`'||LA14_0=='|'||(LA14_0>='~' && LA14_0<='\uFFFF')) ) {s = 41;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA14_32 = input.LA(1);

                        s = -1;
                        if ( ((LA14_32>='\u0000' && LA14_32<='\uFFFF')) ) {s = 90;}

                        else s = 41;

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