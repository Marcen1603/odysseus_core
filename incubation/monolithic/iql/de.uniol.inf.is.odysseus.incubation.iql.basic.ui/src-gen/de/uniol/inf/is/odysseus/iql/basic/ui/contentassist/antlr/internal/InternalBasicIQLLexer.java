package de.uniol.inf.is.odysseus.iql.basic.ui.contentassist.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalBasicIQLLexer extends Lexer {
    public static final int RULE_ID=4;
    public static final int T__29=29;
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int T__23=23;
    public static final int T__22=22;
    public static final int RULE_ANY_OTHER=10;
    public static final int T__21=21;
    public static final int T__20=20;
    public static final int EOF=-1;
    public static final int T__93=93;
    public static final int T__19=19;
    public static final int T__94=94;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int T__16=16;
    public static final int T__90=90;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int RULE_CHAR=8;
    public static final int T__99=99;
    public static final int T__98=98;
    public static final int T__97=97;
    public static final int T__96=96;
    public static final int T__95=95;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int T__85=85;
    public static final int T__84=84;
    public static final int T__87=87;
    public static final int T__86=86;
    public static final int T__89=89;
    public static final int T__88=88;
    public static final int RULE_ML_COMMENT=12;
    public static final int RULE_RANGE=11;
    public static final int RULE_STRING=7;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int T__70=70;
    public static final int T__76=76;
    public static final int T__75=75;
    public static final int T__74=74;
    public static final int T__73=73;
    public static final int T__79=79;
    public static final int T__78=78;
    public static final int T__77=77;
    public static final int T__68=68;
    public static final int RULE_BOOLEAN=5;
    public static final int T__69=69;
    public static final int T__66=66;
    public static final int T__67=67;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T__61=61;
    public static final int T__60=60;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__107=107;
    public static final int T__108=108;
    public static final int T__109=109;
    public static final int T__103=103;
    public static final int T__59=59;
    public static final int T__104=104;
    public static final int T__105=105;
    public static final int T__106=106;
    public static final int RULE_INT=9;
    public static final int T__50=50;
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__102=102;
    public static final int T__101=101;
    public static final int T__100=100;
    public static final int RULE_SL_COMMENT=13;
    public static final int RULE_DOUBLE=6;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int RULE_WS=14;

    // delegates
    // delegators

    public InternalBasicIQLLexer() {;} 
    public InternalBasicIQLLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InternalBasicIQLLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g"; }

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:11:7: ( '||' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:11:9: '||'
            {
            match("||"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:12:7: ( '&&' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:12:9: '&&'
            {
            match("&&"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:13:7: ( '!' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:13:9: '!'
            {
            match('!'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:14:7: ( 'super' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:14:9: 'super'
            {
            match("super"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:15:7: ( 'this' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:15:9: 'this'
            {
            match("this"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:16:7: ( '=' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:16:9: '='
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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:17:7: ( '+=' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:17:9: '+='
            {
            match("+="); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18:7: ( '-=' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18:9: '-='
            {
            match("-="); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:19:7: ( '*=' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:19:9: '*='
            {
            match("*="); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:20:7: ( '/=' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:20:9: '/='
            {
            match("/="); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:21:7: ( '%=' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:21:9: '%='
            {
            match("%="); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:22:7: ( '==' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:22:9: '=='
            {
            match("=="); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:23:7: ( '!=' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:23:9: '!='
            {
            match("!="); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:24:7: ( '>' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:24:9: '>'
            {
            match('>'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:25:7: ( '>=' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:25:9: '>='
            {
            match(">="); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:26:7: ( '<' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:26:9: '<'
            {
            match('<'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:27:7: ( '<=' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:27:9: '<='
            {
            match("<="); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:28:7: ( '+' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:28:9: '+'
            {
            match('+'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:29:7: ( '-' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:29:9: '-'
            {
            match('-'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:30:7: ( '*' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:30:9: '*'
            {
            match('*'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:31:7: ( '/' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:31:9: '/'
            {
            match('/'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:32:7: ( '%' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:32:9: '%'
            {
            match('%'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:33:7: ( '++' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:33:9: '++'
            {
            match("++"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:34:7: ( '--' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:34:9: '--'
            {
            match("--"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:35:7: ( '~' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:35:9: '~'
            {
            match('~'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:36:7: ( '?:' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:36:9: '?:'
            {
            match("?:"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:37:7: ( '|' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:37:9: '|'
            {
            match('|'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:38:7: ( '|=' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:38:9: '|='
            {
            match("|="); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:39:7: ( '^' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:39:9: '^'
            {
            match('^'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:40:7: ( '^=' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:40:9: '^='
            {
            match("^="); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:41:7: ( '&' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:41:9: '&'
            {
            match('&'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:42:7: ( '&=' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:42:9: '&='
            {
            match("&="); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:43:7: ( '>>' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:43:9: '>>'
            {
            match(">>"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:44:7: ( '>>=' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:44:9: '>>='
            {
            match(">>="); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:45:7: ( '<<' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:45:9: '<<'
            {
            match("<<"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:46:7: ( '<<=' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:46:9: '<<='
            {
            match("<<="); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:47:7: ( '>>>' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:47:9: '>>>'
            {
            match(">>>"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:48:7: ( '>>>=' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:48:9: '>>>='
            {
            match(">>>="); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:49:7: ( '[' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:49:9: '['
            {
            match('['); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:50:7: ( ']' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:50:9: ']'
            {
            match(']'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:51:7: ( '{' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:51:9: '{'
            {
            match('{'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:52:7: ( '}' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:52:9: '}'
            {
            match('}'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:53:7: ( '(' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:53:9: '('
            {
            match('('); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:54:7: ( ')' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:54:9: ')'
            {
            match(')'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:55:7: ( '.' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:55:9: '.'
            {
            match('.'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:56:7: ( ':' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:56:9: ':'
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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:57:7: ( ';' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:57:9: ';'
            {
            match(';'); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:58:7: ( ',' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:58:9: ','
            {
            match(','); 

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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:59:7: ( 'null' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:59:9: 'null'
            {
            match("null"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:60:7: ( 'break' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:60:9: 'break'
            {
            match("break"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:61:7: ( 'case' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:61:9: 'case'
            {
            match("case"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:62:7: ( 'class' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:62:9: 'class'
            {
            match("class"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:63:7: ( 'continue' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:63:9: 'continue'
            {
            match("continue"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:64:7: ( 'default' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:64:9: 'default'
            {
            match("default"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:65:7: ( 'do' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:65:9: 'do'
            {
            match("do"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:66:7: ( 'else' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:66:9: 'else'
            {
            match("else"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:67:7: ( 'extends' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:67:9: 'extends'
            {
            match("extends"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:68:7: ( 'for' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:68:9: 'for'
            {
            match("for"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:69:7: ( 'if' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:69:9: 'if'
            {
            match("if"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:70:7: ( 'implements' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:70:9: 'implements'
            {
            match("implements"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:71:7: ( 'instanceof' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:71:9: 'instanceof'
            {
            match("instanceof"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:72:7: ( 'interface' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:72:9: 'interface'
            {
            match("interface"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:73:7: ( 'new' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:73:9: 'new'
            {
            match("new"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:74:7: ( 'package' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:74:9: 'package'
            {
            match("package"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:75:7: ( 'return' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:75:9: 'return'
            {
            match("return"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:76:7: ( 'switch' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:76:9: 'switch'
            {
            match("switch"); 


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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:77:7: ( 'while' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:77:9: 'while'
            {
            match("while"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__81"

    // $ANTLR start "T__82"
    public final void mT__82() throws RecognitionException {
        try {
            int _type = T__82;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:78:7: ( 'abstract' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:78:9: 'abstract'
            {
            match("abstract"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__82"

    // $ANTLR start "T__83"
    public final void mT__83() throws RecognitionException {
        try {
            int _type = T__83;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:79:7: ( 'assert' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:79:9: 'assert'
            {
            match("assert"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__83"

    // $ANTLR start "T__84"
    public final void mT__84() throws RecognitionException {
        try {
            int _type = T__84;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:80:7: ( 'catch' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:80:9: 'catch'
            {
            match("catch"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__84"

    // $ANTLR start "T__85"
    public final void mT__85() throws RecognitionException {
        try {
            int _type = T__85;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:81:7: ( 'const' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:81:9: 'const'
            {
            match("const"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__85"

    // $ANTLR start "T__86"
    public final void mT__86() throws RecognitionException {
        try {
            int _type = T__86;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:82:7: ( 'enum' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:82:9: 'enum'
            {
            match("enum"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__86"

    // $ANTLR start "T__87"
    public final void mT__87() throws RecognitionException {
        try {
            int _type = T__87;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:83:7: ( 'final' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:83:9: 'final'
            {
            match("final"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__87"

    // $ANTLR start "T__88"
    public final void mT__88() throws RecognitionException {
        try {
            int _type = T__88;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:84:7: ( 'finally' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:84:9: 'finally'
            {
            match("finally"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__88"

    // $ANTLR start "T__89"
    public final void mT__89() throws RecognitionException {
        try {
            int _type = T__89;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:85:7: ( 'goto' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:85:9: 'goto'
            {
            match("goto"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__89"

    // $ANTLR start "T__90"
    public final void mT__90() throws RecognitionException {
        try {
            int _type = T__90;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:86:7: ( 'import' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:86:9: 'import'
            {
            match("import"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__90"

    // $ANTLR start "T__91"
    public final void mT__91() throws RecognitionException {
        try {
            int _type = T__91;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:87:7: ( 'native' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:87:9: 'native'
            {
            match("native"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__91"

    // $ANTLR start "T__92"
    public final void mT__92() throws RecognitionException {
        try {
            int _type = T__92;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:88:7: ( 'private' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:88:9: 'private'
            {
            match("private"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__92"

    // $ANTLR start "T__93"
    public final void mT__93() throws RecognitionException {
        try {
            int _type = T__93;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:89:7: ( 'protected' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:89:9: 'protected'
            {
            match("protected"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__93"

    // $ANTLR start "T__94"
    public final void mT__94() throws RecognitionException {
        try {
            int _type = T__94;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:90:7: ( 'public' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:90:9: 'public'
            {
            match("public"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__94"

    // $ANTLR start "T__95"
    public final void mT__95() throws RecognitionException {
        try {
            int _type = T__95;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:91:7: ( 'static' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:91:9: 'static'
            {
            match("static"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__95"

    // $ANTLR start "T__96"
    public final void mT__96() throws RecognitionException {
        try {
            int _type = T__96;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:92:7: ( 'synchronized' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:92:9: 'synchronized'
            {
            match("synchronized"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__96"

    // $ANTLR start "T__97"
    public final void mT__97() throws RecognitionException {
        try {
            int _type = T__97;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:93:7: ( 'throw' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:93:9: 'throw'
            {
            match("throw"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__97"

    // $ANTLR start "T__98"
    public final void mT__98() throws RecognitionException {
        try {
            int _type = T__98;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:94:7: ( 'throws' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:94:9: 'throws'
            {
            match("throws"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__98"

    // $ANTLR start "T__99"
    public final void mT__99() throws RecognitionException {
        try {
            int _type = T__99;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:95:7: ( 'transient' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:95:9: 'transient'
            {
            match("transient"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__99"

    // $ANTLR start "T__100"
    public final void mT__100() throws RecognitionException {
        try {
            int _type = T__100;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:96:8: ( 'try' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:96:10: 'try'
            {
            match("try"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__100"

    // $ANTLR start "T__101"
    public final void mT__101() throws RecognitionException {
        try {
            int _type = T__101;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:97:8: ( 'volatile' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:97:10: 'volatile'
            {
            match("volatile"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__101"

    // $ANTLR start "T__102"
    public final void mT__102() throws RecognitionException {
        try {
            int _type = T__102;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:98:8: ( 'strictfp' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:98:10: 'strictfp'
            {
            match("strictfp"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__102"

    // $ANTLR start "T__103"
    public final void mT__103() throws RecognitionException {
        try {
            int _type = T__103;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:99:8: ( 'namespace' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:99:10: 'namespace'
            {
            match("namespace"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__103"

    // $ANTLR start "T__104"
    public final void mT__104() throws RecognitionException {
        try {
            int _type = T__104;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:100:8: ( 'use' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:100:10: 'use'
            {
            match("use"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__104"

    // $ANTLR start "T__105"
    public final void mT__105() throws RecognitionException {
        try {
            int _type = T__105;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:101:8: ( '$*' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:101:10: '$*'
            {
            match("$*"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__105"

    // $ANTLR start "T__106"
    public final void mT__106() throws RecognitionException {
        try {
            int _type = T__106;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:102:8: ( '*$' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:102:10: '*$'
            {
            match("*$"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__106"

    // $ANTLR start "T__107"
    public final void mT__107() throws RecognitionException {
        try {
            int _type = T__107;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:103:8: ( '::*' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:103:10: '::*'
            {
            match("::*"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__107"

    // $ANTLR start "T__108"
    public final void mT__108() throws RecognitionException {
        try {
            int _type = T__108;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:104:8: ( '::' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:104:10: '::'
            {
            match("::"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__108"

    // $ANTLR start "T__109"
    public final void mT__109() throws RecognitionException {
        try {
            int _type = T__109;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:105:8: ( 'override' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:105:10: 'override'
            {
            match("override"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__109"

    // $ANTLR start "RULE_RANGE"
    public final void mRULE_RANGE() throws RecognitionException {
        try {
            int _type = RULE_RANGE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18247:12: ( RULE_INT '..' RULE_INT )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18247:14: RULE_INT '..' RULE_INT
            {
            mRULE_INT(); 
            match(".."); 

            mRULE_INT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_RANGE"

    // $ANTLR start "RULE_BOOLEAN"
    public final void mRULE_BOOLEAN() throws RecognitionException {
        try {
            int _type = RULE_BOOLEAN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18249:14: ( ( 'true' | 'false' ) )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18249:16: ( 'true' | 'false' )
            {
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18249:16: ( 'true' | 'false' )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='t') ) {
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
                    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18249:17: 'true'
                    {
                    match("true"); 


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18249:24: 'false'
                    {
                    match("false"); 


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
    // $ANTLR end "RULE_BOOLEAN"

    // $ANTLR start "RULE_INT"
    public final void mRULE_INT() throws RecognitionException {
        try {
            int _type = RULE_INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18251:10: ( ( '0' .. '9' )+ )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18251:12: ( '0' .. '9' )+
            {
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18251:12: ( '0' .. '9' )+
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
            	    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18251:13: '0' .. '9'
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

    // $ANTLR start "RULE_DOUBLE"
    public final void mRULE_DOUBLE() throws RecognitionException {
        try {
            int _type = RULE_DOUBLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18253:13: ( ( '0' .. '9' )* '.' ( '0' .. '9' )+ )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18253:15: ( '0' .. '9' )* '.' ( '0' .. '9' )+
            {
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18253:15: ( '0' .. '9' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18253:16: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            match('.'); 
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18253:31: ( '0' .. '9' )+
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
            	    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18253:32: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_DOUBLE"

    // $ANTLR start "RULE_CHAR"
    public final void mRULE_CHAR() throws RecognitionException {
        try {
            int _type = RULE_CHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18255:11: ( '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) ) '\\'' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18255:13: '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) ) '\\''
            {
            match('\''); 
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18255:18: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='\\') ) {
                alt5=1;
            }
            else if ( ((LA5_0>='\u0000' && LA5_0<='&')||(LA5_0>='(' && LA5_0<='[')||(LA5_0>=']' && LA5_0<='\uFFFF')) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18255:19: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\\'' | '\\\\' )
                    {
                    match('\\'); 
                    if ( input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||(input.LA(1)>='t' && input.LA(1)<='u') ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18255:60: ~ ( ( '\\\\' | '\\'' ) )
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

            }

            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_CHAR"

    // $ANTLR start "RULE_ID"
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18257:9: ( ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18257:11: ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18257:11: ( '^' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='^') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18257:11: '^'
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

            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18257:40: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='0' && LA7_0<='9')||(LA7_0>='A' && LA7_0<='Z')||LA7_0=='_'||(LA7_0>='a' && LA7_0<='z')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:
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

    // $ANTLR start "RULE_STRING"
    public final void mRULE_STRING() throws RecognitionException {
        try {
            int _type = RULE_STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18259:13: ( ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' ) )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18259:15: ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            {
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18259:15: ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='\"') ) {
                alt10=1;
            }
            else if ( (LA10_0=='\'') ) {
                alt10=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18259:16: '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"'
                    {
                    match('\"'); 
                    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18259:20: ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )*
                    loop8:
                    do {
                        int alt8=3;
                        int LA8_0 = input.LA(1);

                        if ( (LA8_0=='\\') ) {
                            alt8=1;
                        }
                        else if ( ((LA8_0>='\u0000' && LA8_0<='!')||(LA8_0>='#' && LA8_0<='[')||(LA8_0>=']' && LA8_0<='\uFFFF')) ) {
                            alt8=2;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18259:21: '\\\\' .
                    	    {
                    	    match('\\'); 
                    	    matchAny(); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18259:28: ~ ( ( '\\\\' | '\"' ) )
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
                    	    break loop8;
                        }
                    } while (true);

                    match('\"'); 

                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18259:48: '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\''
                    {
                    match('\''); 
                    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18259:53: ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )*
                    loop9:
                    do {
                        int alt9=3;
                        int LA9_0 = input.LA(1);

                        if ( (LA9_0=='\\') ) {
                            alt9=1;
                        }
                        else if ( ((LA9_0>='\u0000' && LA9_0<='&')||(LA9_0>='(' && LA9_0<='[')||(LA9_0>=']' && LA9_0<='\uFFFF')) ) {
                            alt9=2;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18259:54: '\\\\' .
                    	    {
                    	    match('\\'); 
                    	    matchAny(); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18259:61: ~ ( ( '\\\\' | '\\'' ) )
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
                    	    break loop9;
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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18261:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18261:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18261:24: ( options {greedy=false; } : . )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0=='*') ) {
                    int LA11_1 = input.LA(2);

                    if ( (LA11_1=='/') ) {
                        alt11=2;
                    }
                    else if ( ((LA11_1>='\u0000' && LA11_1<='.')||(LA11_1>='0' && LA11_1<='\uFFFF')) ) {
                        alt11=1;
                    }


                }
                else if ( ((LA11_0>='\u0000' && LA11_0<=')')||(LA11_0>='+' && LA11_0<='\uFFFF')) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18261:52: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop11;
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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18263:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18263:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18263:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>='\u0000' && LA12_0<='\t')||(LA12_0>='\u000B' && LA12_0<='\f')||(LA12_0>='\u000E' && LA12_0<='\uFFFF')) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18263:24: ~ ( ( '\\n' | '\\r' ) )
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
            	    break loop12;
                }
            } while (true);

            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18263:40: ( ( '\\r' )? '\\n' )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0=='\n'||LA14_0=='\r') ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18263:41: ( '\\r' )? '\\n'
                    {
                    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18263:41: ( '\\r' )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0=='\r') ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18263:41: '\\r'
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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18265:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18265:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18265:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt15=0;
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( ((LA15_0>='\t' && LA15_0<='\n')||LA15_0=='\r'||LA15_0==' ') ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:
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
            	    if ( cnt15 >= 1 ) break loop15;
                        EarlyExitException eee =
                            new EarlyExitException(15, input);
                        throw eee;
                }
                cnt15++;
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
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18267:16: ( . )
            // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:18267:18: .
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
        // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:8: ( T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | T__89 | T__90 | T__91 | T__92 | T__93 | T__94 | T__95 | T__96 | T__97 | T__98 | T__99 | T__100 | T__101 | T__102 | T__103 | T__104 | T__105 | T__106 | T__107 | T__108 | T__109 | RULE_RANGE | RULE_BOOLEAN | RULE_INT | RULE_DOUBLE | RULE_CHAR | RULE_ID | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt16=106;
        alt16 = dfa16.predict(input);
        switch (alt16) {
            case 1 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:10: T__15
                {
                mT__15(); 

                }
                break;
            case 2 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:16: T__16
                {
                mT__16(); 

                }
                break;
            case 3 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:22: T__17
                {
                mT__17(); 

                }
                break;
            case 4 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:28: T__18
                {
                mT__18(); 

                }
                break;
            case 5 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:34: T__19
                {
                mT__19(); 

                }
                break;
            case 6 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:40: T__20
                {
                mT__20(); 

                }
                break;
            case 7 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:46: T__21
                {
                mT__21(); 

                }
                break;
            case 8 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:52: T__22
                {
                mT__22(); 

                }
                break;
            case 9 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:58: T__23
                {
                mT__23(); 

                }
                break;
            case 10 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:64: T__24
                {
                mT__24(); 

                }
                break;
            case 11 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:70: T__25
                {
                mT__25(); 

                }
                break;
            case 12 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:76: T__26
                {
                mT__26(); 

                }
                break;
            case 13 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:82: T__27
                {
                mT__27(); 

                }
                break;
            case 14 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:88: T__28
                {
                mT__28(); 

                }
                break;
            case 15 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:94: T__29
                {
                mT__29(); 

                }
                break;
            case 16 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:100: T__30
                {
                mT__30(); 

                }
                break;
            case 17 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:106: T__31
                {
                mT__31(); 

                }
                break;
            case 18 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:112: T__32
                {
                mT__32(); 

                }
                break;
            case 19 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:118: T__33
                {
                mT__33(); 

                }
                break;
            case 20 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:124: T__34
                {
                mT__34(); 

                }
                break;
            case 21 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:130: T__35
                {
                mT__35(); 

                }
                break;
            case 22 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:136: T__36
                {
                mT__36(); 

                }
                break;
            case 23 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:142: T__37
                {
                mT__37(); 

                }
                break;
            case 24 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:148: T__38
                {
                mT__38(); 

                }
                break;
            case 25 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:154: T__39
                {
                mT__39(); 

                }
                break;
            case 26 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:160: T__40
                {
                mT__40(); 

                }
                break;
            case 27 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:166: T__41
                {
                mT__41(); 

                }
                break;
            case 28 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:172: T__42
                {
                mT__42(); 

                }
                break;
            case 29 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:178: T__43
                {
                mT__43(); 

                }
                break;
            case 30 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:184: T__44
                {
                mT__44(); 

                }
                break;
            case 31 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:190: T__45
                {
                mT__45(); 

                }
                break;
            case 32 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:196: T__46
                {
                mT__46(); 

                }
                break;
            case 33 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:202: T__47
                {
                mT__47(); 

                }
                break;
            case 34 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:208: T__48
                {
                mT__48(); 

                }
                break;
            case 35 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:214: T__49
                {
                mT__49(); 

                }
                break;
            case 36 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:220: T__50
                {
                mT__50(); 

                }
                break;
            case 37 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:226: T__51
                {
                mT__51(); 

                }
                break;
            case 38 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:232: T__52
                {
                mT__52(); 

                }
                break;
            case 39 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:238: T__53
                {
                mT__53(); 

                }
                break;
            case 40 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:244: T__54
                {
                mT__54(); 

                }
                break;
            case 41 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:250: T__55
                {
                mT__55(); 

                }
                break;
            case 42 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:256: T__56
                {
                mT__56(); 

                }
                break;
            case 43 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:262: T__57
                {
                mT__57(); 

                }
                break;
            case 44 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:268: T__58
                {
                mT__58(); 

                }
                break;
            case 45 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:274: T__59
                {
                mT__59(); 

                }
                break;
            case 46 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:280: T__60
                {
                mT__60(); 

                }
                break;
            case 47 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:286: T__61
                {
                mT__61(); 

                }
                break;
            case 48 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:292: T__62
                {
                mT__62(); 

                }
                break;
            case 49 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:298: T__63
                {
                mT__63(); 

                }
                break;
            case 50 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:304: T__64
                {
                mT__64(); 

                }
                break;
            case 51 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:310: T__65
                {
                mT__65(); 

                }
                break;
            case 52 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:316: T__66
                {
                mT__66(); 

                }
                break;
            case 53 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:322: T__67
                {
                mT__67(); 

                }
                break;
            case 54 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:328: T__68
                {
                mT__68(); 

                }
                break;
            case 55 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:334: T__69
                {
                mT__69(); 

                }
                break;
            case 56 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:340: T__70
                {
                mT__70(); 

                }
                break;
            case 57 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:346: T__71
                {
                mT__71(); 

                }
                break;
            case 58 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:352: T__72
                {
                mT__72(); 

                }
                break;
            case 59 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:358: T__73
                {
                mT__73(); 

                }
                break;
            case 60 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:364: T__74
                {
                mT__74(); 

                }
                break;
            case 61 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:370: T__75
                {
                mT__75(); 

                }
                break;
            case 62 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:376: T__76
                {
                mT__76(); 

                }
                break;
            case 63 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:382: T__77
                {
                mT__77(); 

                }
                break;
            case 64 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:388: T__78
                {
                mT__78(); 

                }
                break;
            case 65 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:394: T__79
                {
                mT__79(); 

                }
                break;
            case 66 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:400: T__80
                {
                mT__80(); 

                }
                break;
            case 67 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:406: T__81
                {
                mT__81(); 

                }
                break;
            case 68 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:412: T__82
                {
                mT__82(); 

                }
                break;
            case 69 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:418: T__83
                {
                mT__83(); 

                }
                break;
            case 70 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:424: T__84
                {
                mT__84(); 

                }
                break;
            case 71 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:430: T__85
                {
                mT__85(); 

                }
                break;
            case 72 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:436: T__86
                {
                mT__86(); 

                }
                break;
            case 73 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:442: T__87
                {
                mT__87(); 

                }
                break;
            case 74 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:448: T__88
                {
                mT__88(); 

                }
                break;
            case 75 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:454: T__89
                {
                mT__89(); 

                }
                break;
            case 76 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:460: T__90
                {
                mT__90(); 

                }
                break;
            case 77 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:466: T__91
                {
                mT__91(); 

                }
                break;
            case 78 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:472: T__92
                {
                mT__92(); 

                }
                break;
            case 79 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:478: T__93
                {
                mT__93(); 

                }
                break;
            case 80 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:484: T__94
                {
                mT__94(); 

                }
                break;
            case 81 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:490: T__95
                {
                mT__95(); 

                }
                break;
            case 82 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:496: T__96
                {
                mT__96(); 

                }
                break;
            case 83 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:502: T__97
                {
                mT__97(); 

                }
                break;
            case 84 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:508: T__98
                {
                mT__98(); 

                }
                break;
            case 85 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:514: T__99
                {
                mT__99(); 

                }
                break;
            case 86 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:520: T__100
                {
                mT__100(); 

                }
                break;
            case 87 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:527: T__101
                {
                mT__101(); 

                }
                break;
            case 88 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:534: T__102
                {
                mT__102(); 

                }
                break;
            case 89 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:541: T__103
                {
                mT__103(); 

                }
                break;
            case 90 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:548: T__104
                {
                mT__104(); 

                }
                break;
            case 91 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:555: T__105
                {
                mT__105(); 

                }
                break;
            case 92 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:562: T__106
                {
                mT__106(); 

                }
                break;
            case 93 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:569: T__107
                {
                mT__107(); 

                }
                break;
            case 94 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:576: T__108
                {
                mT__108(); 

                }
                break;
            case 95 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:583: T__109
                {
                mT__109(); 

                }
                break;
            case 96 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:590: RULE_RANGE
                {
                mRULE_RANGE(); 

                }
                break;
            case 97 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:601: RULE_BOOLEAN
                {
                mRULE_BOOLEAN(); 

                }
                break;
            case 98 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:614: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 99 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:623: RULE_DOUBLE
                {
                mRULE_DOUBLE(); 

                }
                break;
            case 100 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:635: RULE_CHAR
                {
                mRULE_CHAR(); 

                }
                break;
            case 101 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:645: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 102 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:653: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 103 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:665: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 104 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:681: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 105 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:697: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 106 :
                // ../de.uniol.inf.is.odysseus.incubation.iql.basic.ui/src-gen/de/uniol/inf/is/odysseus/iql/basic/ui/contentassist/antlr/internal/InternalBasicIQL.g:1:705: RULE_ANY_OTHER
                {
                mRULE_ANY_OTHER(); 

                }
                break;

        }

    }


    protected DFA16 dfa16 = new DFA16(this);
    static final String DFA16_eotS =
        "\1\uffff\1\63\1\66\1\70\2\75\1\101\1\104\1\107\1\112\1\116\1\120"+
        "\1\123\1\126\1\uffff\1\60\1\132\6\uffff\1\141\1\144\2\uffff\16\75"+
        "\1\60\1\75\1\u0085\1\60\1\uffff\1\60\12\uffff\4\75\1\uffff\2\75"+
        "\22\uffff\1\u0098\2\uffff\1\u009a\15\uffff\1\u009c\3\uffff\10\75"+
        "\1\u00a7\6\75\1\u00ae\14\75\1\uffff\1\75\2\uffff\1\u0085\4\uffff"+
        "\10\75\1\u00c9\1\75\1\uffff\1\u00cc\5\uffff\1\75\1\u00ce\10\75\1"+
        "\uffff\3\75\1\u00db\2\75\1\uffff\15\75\1\u00ec\1\75\3\uffff\5\75"+
        "\1\u00f4\2\75\1\uffff\1\u00f7\2\uffff\1\u00f8\1\uffff\3\75\1\u00fc"+
        "\5\75\1\u0102\1\75\1\u0104\1\uffff\16\75\1\u0113\1\75\1\uffff\1"+
        "\75\1\uffff\1\u0116\4\75\1\uffff\1\u011c\1\75\2\uffff\2\75\1\u0120"+
        "\1\uffff\1\u0121\1\u0122\1\75\1\u0124\1\75\1\uffff\1\75\1\uffff"+
        "\1\u0128\1\u00f7\11\75\1\u0132\2\75\1\uffff\2\75\1\uffff\1\u0137"+
        "\1\u0138\2\75\1\u013b\1\uffff\1\75\1\u013d\1\75\3\uffff\1\75\1\uffff"+
        "\3\75\1\uffff\1\75\1\u0144\5\75\1\u014a\1\u014b\1\uffff\1\75\1\u014d"+
        "\2\75\2\uffff\2\75\1\uffff\1\75\1\uffff\2\75\1\u0155\1\u0156\1\u0157"+
        "\1\75\1\uffff\2\75\1\u015b\1\u015c\1\75\2\uffff\1\75\1\uffff\2\75"+
        "\1\u0161\3\75\1\u0165\3\uffff\3\75\2\uffff\1\75\1\u016a\1\u016b"+
        "\1\u016c\1\uffff\1\75\1\u016e\1\u016f\1\uffff\2\75\1\u0172\1\u0173"+
        "\3\uffff\1\75\2\uffff\1\u0175\1\u0176\2\uffff\1\75\2\uffff\1\u0178"+
        "\1\uffff";
    static final String DFA16_eofS =
        "\u0179\uffff";
    static final String DFA16_minS =
        "\1\0\1\75\1\46\1\75\1\164\1\150\1\75\1\53\1\55\1\44\1\52\2\75\1"+
        "\74\1\uffff\1\72\1\75\6\uffff\1\60\1\72\2\uffff\1\141\1\162\1\141"+
        "\1\145\1\154\1\141\1\146\1\141\1\145\1\150\1\142\2\157\1\163\1\52"+
        "\1\166\1\56\1\0\1\uffff\1\0\12\uffff\1\160\1\151\1\141\1\156\1\uffff"+
        "\1\151\1\141\22\uffff\1\75\2\uffff\1\75\15\uffff\1\52\3\uffff\1"+
        "\154\1\167\1\155\1\145\1\163\1\141\1\156\1\146\1\60\1\163\1\164"+
        "\1\165\1\162\1\156\1\154\1\60\1\160\1\163\1\143\1\151\1\142\1\164"+
        "\1\151\2\163\1\164\1\154\1\145\1\uffff\1\145\1\uffff\2\56\2\0\2"+
        "\uffff\1\145\2\164\1\151\1\143\1\163\1\157\1\156\1\60\1\145\1\uffff"+
        "\1\75\5\uffff\1\154\1\60\1\151\1\145\1\141\1\145\1\143\2\163\1\141"+
        "\1\uffff\2\145\1\155\1\60\1\141\1\163\1\uffff\1\154\1\164\1\145"+
        "\1\153\1\166\1\164\1\154\1\165\1\154\1\164\1\145\1\157\1\141\1\60"+
        "\1\162\1\uffff\1\0\1\uffff\1\162\1\143\1\151\1\143\1\150\1\60\1"+
        "\167\1\163\1\uffff\1\60\2\uffff\1\60\1\uffff\1\166\1\163\1\153\1"+
        "\60\1\150\1\163\1\151\1\164\1\165\1\60\1\156\1\60\1\uffff\1\154"+
        "\2\145\1\162\1\141\1\162\2\141\1\145\1\151\1\162\1\145\2\162\1\60"+
        "\1\164\1\uffff\1\162\1\uffff\1\60\1\150\1\143\1\164\1\162\1\uffff"+
        "\1\60\1\151\2\uffff\1\145\1\160\1\60\1\uffff\2\60\1\156\1\60\1\154"+
        "\1\uffff\1\144\1\uffff\2\60\1\155\1\164\1\156\1\146\1\147\1\164"+
        "\2\143\1\156\1\60\1\141\1\164\1\uffff\2\151\1\uffff\2\60\1\146\1"+
        "\157\1\60\1\uffff\1\145\1\60\1\141\3\uffff\1\165\1\uffff\1\164\1"+
        "\163\1\171\1\uffff\1\145\1\60\1\143\1\141\2\145\1\164\2\60\1\uffff"+
        "\1\143\1\60\1\154\1\144\2\uffff\1\160\1\156\1\uffff\1\156\1\uffff"+
        "\1\143\1\145\3\60\1\156\1\uffff\1\145\1\143\2\60\1\145\2\uffff\1"+
        "\164\1\uffff\2\145\1\60\1\151\1\164\1\145\1\60\3\uffff\1\164\1\157"+
        "\1\145\2\uffff\1\144\3\60\1\uffff\1\172\2\60\1\uffff\1\163\1\146"+
        "\2\60\3\uffff\1\145\2\uffff\2\60\2\uffff\1\144\2\uffff\1\60\1\uffff";
    static final String DFA16_maxS =
        "\1\uffff\1\174\2\75\1\171\1\162\6\75\1\76\1\75\1\uffff\1\72\1\172"+
        "\6\uffff\1\71\1\72\2\uffff\1\165\1\162\2\157\1\170\1\157\1\156\1"+
        "\165\1\145\1\150\1\163\2\157\1\163\1\52\1\166\1\71\1\uffff\1\uffff"+
        "\1\uffff\12\uffff\1\160\1\151\1\162\1\156\1\uffff\1\162\1\171\22"+
        "\uffff\1\76\2\uffff\1\75\15\uffff\1\52\3\uffff\1\154\1\167\1\164"+
        "\1\145\1\164\1\141\1\156\1\146\1\172\1\163\1\164\1\165\1\162\1\156"+
        "\1\154\1\172\1\160\1\164\1\143\1\157\1\142\1\164\1\151\2\163\1\164"+
        "\1\154\1\145\1\uffff\1\145\1\uffff\2\71\2\uffff\2\uffff\1\145\2"+
        "\164\1\151\1\143\1\163\1\157\1\156\1\172\1\145\1\uffff\1\75\5\uffff"+
        "\1\154\1\172\1\151\1\145\1\141\1\145\1\143\1\163\1\164\1\141\1\uffff"+
        "\2\145\1\155\1\172\1\141\1\163\1\uffff\1\157\1\164\1\145\1\153\1"+
        "\166\1\164\1\154\1\165\1\154\1\164\1\145\1\157\1\141\1\172\1\162"+
        "\1\uffff\1\uffff\1\uffff\1\162\1\143\1\151\1\143\1\150\1\172\1\167"+
        "\1\163\1\uffff\1\172\2\uffff\1\172\1\uffff\1\166\1\163\1\153\1\172"+
        "\1\150\1\163\1\151\1\164\1\165\1\172\1\156\1\172\1\uffff\1\154\2"+
        "\145\1\162\1\141\1\162\2\141\1\145\1\151\1\162\1\145\2\162\1\172"+
        "\1\164\1\uffff\1\162\1\uffff\1\172\1\150\1\143\1\164\1\162\1\uffff"+
        "\1\172\1\151\2\uffff\1\145\1\160\1\172\1\uffff\2\172\1\156\1\172"+
        "\1\154\1\uffff\1\144\1\uffff\2\172\1\155\1\164\1\156\1\146\1\147"+
        "\1\164\2\143\1\156\1\172\1\141\1\164\1\uffff\2\151\1\uffff\2\172"+
        "\1\146\1\157\1\172\1\uffff\1\145\1\172\1\141\3\uffff\1\165\1\uffff"+
        "\1\164\1\163\1\171\1\uffff\1\145\1\172\1\143\1\141\2\145\1\164\2"+
        "\172\1\uffff\1\143\1\172\1\154\1\144\2\uffff\1\160\1\156\1\uffff"+
        "\1\156\1\uffff\1\143\1\145\3\172\1\156\1\uffff\1\145\1\143\2\172"+
        "\1\145\2\uffff\1\164\1\uffff\2\145\1\172\1\151\1\164\1\145\1\172"+
        "\3\uffff\1\164\1\157\1\145\2\uffff\1\144\3\172\1\uffff\3\172\1\uffff"+
        "\1\163\1\146\2\172\3\uffff\1\145\2\uffff\2\172\2\uffff\1\144\2\uffff"+
        "\1\172\1\uffff";
    static final String DFA16_acceptS =
        "\16\uffff\1\31\2\uffff\1\47\1\50\1\51\1\52\1\53\1\54\2\uffff\1"+
        "\57\1\60\22\uffff\1\145\1\uffff\1\151\1\152\1\1\1\34\1\33\1\2\1"+
        "\40\1\37\1\15\1\3\4\uffff\1\145\2\uffff\1\14\1\6\1\7\1\27\1\22\1"+
        "\10\1\30\1\23\1\11\1\134\1\24\1\12\1\147\1\150\1\25\1\13\1\26\1"+
        "\17\1\uffff\1\16\1\21\1\uffff\1\20\1\31\1\32\1\36\1\35\1\47\1\50"+
        "\1\51\1\52\1\53\1\54\1\55\1\143\1\uffff\1\56\1\57\1\60\34\uffff"+
        "\1\133\1\uffff\1\142\4\uffff\1\146\1\151\12\uffff\1\42\1\uffff\1"+
        "\41\1\44\1\43\1\135\1\136\12\uffff\1\67\6\uffff\1\73\17\uffff\1"+
        "\140\1\uffff\1\144\10\uffff\1\126\1\uffff\1\46\1\45\1\uffff\1\77"+
        "\14\uffff\1\72\20\uffff\1\132\1\uffff\1\144\5\uffff\1\5\2\uffff"+
        "\1\141\1\61\3\uffff\1\63\5\uffff\1\70\1\uffff\1\110\16\uffff\1\113"+
        "\2\uffff\1\4\5\uffff\1\123\3\uffff\1\62\1\106\1\64\1\uffff\1\107"+
        "\3\uffff\1\111\11\uffff\1\103\4\uffff\1\102\1\121\2\uffff\1\124"+
        "\1\uffff\1\115\6\uffff\1\114\5\uffff\1\120\1\101\1\uffff\1\105\7"+
        "\uffff\1\66\1\71\1\112\3\uffff\1\100\1\116\4\uffff\1\130\3\uffff"+
        "\1\65\4\uffff\1\104\1\127\1\137\1\uffff\1\125\1\131\2\uffff\1\76"+
        "\1\117\1\uffff\1\74\1\75\1\uffff\1\122";
    static final String DFA16_specialS =
        "\1\3\53\uffff\1\0\1\uffff\1\2\131\uffff\1\1\1\4\65\uffff\1\5\u00b9"+
        "\uffff}>";
    static final String[] DFA16_transitionS = {
            "\11\60\2\57\2\60\1\57\22\60\1\57\1\3\1\56\1\60\1\51\1\13\1"+
            "\2\1\54\1\25\1\26\1\11\1\7\1\32\1\10\1\27\1\12\12\53\1\30\1"+
            "\31\1\15\1\6\1\14\1\17\1\60\32\55\1\21\1\60\1\22\1\20\1\55\1"+
            "\60\1\45\1\34\1\35\1\36\1\37\1\40\1\46\1\55\1\41\4\55\1\33\1"+
            "\52\1\42\1\55\1\43\1\4\1\5\1\50\1\47\1\44\3\55\1\23\1\1\1\24"+
            "\1\16\uff81\60",
            "\1\62\76\uffff\1\61",
            "\1\64\26\uffff\1\65",
            "\1\67",
            "\1\73\1\71\1\uffff\1\72\1\uffff\1\74",
            "\1\76\11\uffff\1\77",
            "\1\100",
            "\1\103\21\uffff\1\102",
            "\1\106\17\uffff\1\105",
            "\1\111\30\uffff\1\110",
            "\1\114\4\uffff\1\115\15\uffff\1\113",
            "\1\117",
            "\1\121\1\122",
            "\1\125\1\124",
            "",
            "\1\130",
            "\1\131\3\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "",
            "",
            "",
            "",
            "",
            "",
            "\12\142",
            "\1\143",
            "",
            "",
            "\1\151\3\uffff\1\150\17\uffff\1\147",
            "\1\152",
            "\1\153\12\uffff\1\154\2\uffff\1\155",
            "\1\156\11\uffff\1\157",
            "\1\160\1\uffff\1\162\11\uffff\1\161",
            "\1\165\7\uffff\1\164\5\uffff\1\163",
            "\1\166\6\uffff\1\167\1\170",
            "\1\171\20\uffff\1\172\2\uffff\1\173",
            "\1\174",
            "\1\175",
            "\1\176\20\uffff\1\177",
            "\1\u0080",
            "\1\u0081",
            "\1\u0082",
            "\1\u0083",
            "\1\u0084",
            "\1\u0086\1\uffff\12\u0087",
            "\47\u0089\1\u008a\64\u0089\1\u0088\uffa3\u0089",
            "",
            "\0\u008a",
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
            "\1\u008c",
            "\1\u008d",
            "\1\u008e\20\uffff\1\u008f",
            "\1\u0090",
            "",
            "\1\u0091\10\uffff\1\u0092",
            "\1\u0093\23\uffff\1\u0095\3\uffff\1\u0094",
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
            "",
            "\1\u0096\1\u0097",
            "",
            "",
            "\1\u0099",
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
            "\1\u009b",
            "",
            "",
            "",
            "\1\u009d",
            "\1\u009e",
            "\1\u00a0\6\uffff\1\u009f",
            "\1\u00a1",
            "\1\u00a2\1\u00a3",
            "\1\u00a4",
            "\1\u00a5",
            "\1\u00a6",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u00a8",
            "\1\u00a9",
            "\1\u00aa",
            "\1\u00ab",
            "\1\u00ac",
            "\1\u00ad",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u00af",
            "\1\u00b0\1\u00b1",
            "\1\u00b2",
            "\1\u00b3\5\uffff\1\u00b4",
            "\1\u00b5",
            "\1\u00b6",
            "\1\u00b7",
            "\1\u00b8",
            "\1\u00b9",
            "\1\u00ba",
            "\1\u00bb",
            "\1\u00bc",
            "",
            "\1\u00bd",
            "",
            "\1\u00be\1\uffff\12\142",
            "\1\u0086\1\uffff\12\u0087",
            "\47\u008a\1\u00bf\64\u008a\1\u00bf\5\u008a\1\u00bf\3\u008a"+
            "\1\u00bf\7\u008a\1\u00bf\3\u008a\1\u00bf\1\u008a\2\u00bf\uff8a"+
            "\u008a",
            "\47\u008a\1\u00c0\uffd8\u008a",
            "",
            "",
            "\1\u00c1",
            "\1\u00c2",
            "\1\u00c3",
            "\1\u00c4",
            "\1\u00c5",
            "\1\u00c6",
            "\1\u00c7",
            "\1\u00c8",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u00ca",
            "",
            "\1\u00cb",
            "",
            "",
            "",
            "",
            "",
            "\1\u00cd",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u00cf",
            "\1\u00d0",
            "\1\u00d1",
            "\1\u00d2",
            "\1\u00d3",
            "\1\u00d4",
            "\1\u00d6\1\u00d5",
            "\1\u00d7",
            "",
            "\1\u00d8",
            "\1\u00d9",
            "\1\u00da",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u00dc",
            "\1\u00dd",
            "",
            "\1\u00de\2\uffff\1\u00df",
            "\1\u00e0",
            "\1\u00e1",
            "\1\u00e2",
            "\1\u00e3",
            "\1\u00e4",
            "\1\u00e5",
            "\1\u00e6",
            "\1\u00e7",
            "\1\u00e8",
            "\1\u00e9",
            "\1\u00ea",
            "\1\u00eb",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u00ed",
            "",
            "\47\u008a\1\u00c0\uffd8\u008a",
            "",
            "\1\u00ef",
            "\1\u00f0",
            "\1\u00f1",
            "\1\u00f2",
            "\1\u00f3",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u00f5",
            "\1\u00f6",
            "",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "",
            "",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "",
            "\1\u00f9",
            "\1\u00fa",
            "\1\u00fb",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u00fd",
            "\1\u00fe",
            "\1\u00ff",
            "\1\u0100",
            "\1\u0101",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u0103",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "",
            "\1\u0105",
            "\1\u0106",
            "\1\u0107",
            "\1\u0108",
            "\1\u0109",
            "\1\u010a",
            "\1\u010b",
            "\1\u010c",
            "\1\u010d",
            "\1\u010e",
            "\1\u010f",
            "\1\u0110",
            "\1\u0111",
            "\1\u0112",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u0114",
            "",
            "\1\u0115",
            "",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u0117",
            "\1\u0118",
            "\1\u0119",
            "\1\u011a",
            "",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\22\75\1\u011b\7"+
            "\75",
            "\1\u011d",
            "",
            "",
            "\1\u011e",
            "\1\u011f",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u0123",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u0125",
            "",
            "\1\u0126",
            "",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\13\75\1\u0127\16"+
            "\75",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u0129",
            "\1\u012a",
            "\1\u012b",
            "\1\u012c",
            "\1\u012d",
            "\1\u012e",
            "\1\u012f",
            "\1\u0130",
            "\1\u0131",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u0133",
            "\1\u0134",
            "",
            "\1\u0135",
            "\1\u0136",
            "",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u0139",
            "\1\u013a",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "",
            "\1\u013c",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u013e",
            "",
            "",
            "",
            "\1\u013f",
            "",
            "\1\u0140",
            "\1\u0141",
            "\1\u0142",
            "",
            "\1\u0143",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u0145",
            "\1\u0146",
            "\1\u0147",
            "\1\u0148",
            "\1\u0149",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "",
            "\1\u014c",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u014e",
            "\1\u014f",
            "",
            "",
            "\1\u0150",
            "\1\u0151",
            "",
            "\1\u0152",
            "",
            "\1\u0153",
            "\1\u0154",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u0158",
            "",
            "\1\u0159",
            "\1\u015a",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u015d",
            "",
            "",
            "\1\u015e",
            "",
            "\1\u015f",
            "\1\u0160",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\1\u0162",
            "\1\u0163",
            "\1\u0164",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "",
            "",
            "",
            "\1\u0166",
            "\1\u0167",
            "\1\u0168",
            "",
            "",
            "\1\u0169",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "",
            "\1\u016d",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "",
            "\1\u0170",
            "\1\u0171",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "",
            "",
            "",
            "\1\u0174",
            "",
            "",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            "",
            "",
            "\1\u0177",
            "",
            "",
            "\12\75\7\uffff\32\75\4\uffff\1\75\1\uffff\32\75",
            ""
    };

    static final short[] DFA16_eot = DFA.unpackEncodedString(DFA16_eotS);
    static final short[] DFA16_eof = DFA.unpackEncodedString(DFA16_eofS);
    static final char[] DFA16_min = DFA.unpackEncodedStringToUnsignedChars(DFA16_minS);
    static final char[] DFA16_max = DFA.unpackEncodedStringToUnsignedChars(DFA16_maxS);
    static final short[] DFA16_accept = DFA.unpackEncodedString(DFA16_acceptS);
    static final short[] DFA16_special = DFA.unpackEncodedString(DFA16_specialS);
    static final short[][] DFA16_transition;

    static {
        int numStates = DFA16_transitionS.length;
        DFA16_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA16_transition[i] = DFA.unpackEncodedString(DFA16_transitionS[i]);
        }
    }

    class DFA16 extends DFA {

        public DFA16(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 16;
            this.eot = DFA16_eot;
            this.eof = DFA16_eof;
            this.min = DFA16_min;
            this.max = DFA16_max;
            this.accept = DFA16_accept;
            this.special = DFA16_special;
            this.transition = DFA16_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | T__89 | T__90 | T__91 | T__92 | T__93 | T__94 | T__95 | T__96 | T__97 | T__98 | T__99 | T__100 | T__101 | T__102 | T__103 | T__104 | T__105 | T__106 | T__107 | T__108 | T__109 | RULE_RANGE | RULE_BOOLEAN | RULE_INT | RULE_DOUBLE | RULE_CHAR | RULE_ID | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA16_44 = input.LA(1);

                        s = -1;
                        if ( (LA16_44=='\\') ) {s = 136;}

                        else if ( ((LA16_44>='\u0000' && LA16_44<='&')||(LA16_44>='(' && LA16_44<='[')||(LA16_44>=']' && LA16_44<='\uFFFF')) ) {s = 137;}

                        else if ( (LA16_44=='\'') ) {s = 138;}

                        else s = 48;

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA16_136 = input.LA(1);

                        s = -1;
                        if ( (LA16_136=='\''||LA16_136=='\\'||LA16_136=='b'||LA16_136=='f'||LA16_136=='n'||LA16_136=='r'||(LA16_136>='t' && LA16_136<='u')) ) {s = 191;}

                        else if ( ((LA16_136>='\u0000' && LA16_136<='&')||(LA16_136>='(' && LA16_136<='[')||(LA16_136>=']' && LA16_136<='a')||(LA16_136>='c' && LA16_136<='e')||(LA16_136>='g' && LA16_136<='m')||(LA16_136>='o' && LA16_136<='q')||LA16_136=='s'||(LA16_136>='v' && LA16_136<='\uFFFF')) ) {s = 138;}

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA16_46 = input.LA(1);

                        s = -1;
                        if ( ((LA16_46>='\u0000' && LA16_46<='\uFFFF')) ) {s = 138;}

                        else s = 48;

                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA16_0 = input.LA(1);

                        s = -1;
                        if ( (LA16_0=='|') ) {s = 1;}

                        else if ( (LA16_0=='&') ) {s = 2;}

                        else if ( (LA16_0=='!') ) {s = 3;}

                        else if ( (LA16_0=='s') ) {s = 4;}

                        else if ( (LA16_0=='t') ) {s = 5;}

                        else if ( (LA16_0=='=') ) {s = 6;}

                        else if ( (LA16_0=='+') ) {s = 7;}

                        else if ( (LA16_0=='-') ) {s = 8;}

                        else if ( (LA16_0=='*') ) {s = 9;}

                        else if ( (LA16_0=='/') ) {s = 10;}

                        else if ( (LA16_0=='%') ) {s = 11;}

                        else if ( (LA16_0=='>') ) {s = 12;}

                        else if ( (LA16_0=='<') ) {s = 13;}

                        else if ( (LA16_0=='~') ) {s = 14;}

                        else if ( (LA16_0=='?') ) {s = 15;}

                        else if ( (LA16_0=='^') ) {s = 16;}

                        else if ( (LA16_0=='[') ) {s = 17;}

                        else if ( (LA16_0==']') ) {s = 18;}

                        else if ( (LA16_0=='{') ) {s = 19;}

                        else if ( (LA16_0=='}') ) {s = 20;}

                        else if ( (LA16_0=='(') ) {s = 21;}

                        else if ( (LA16_0==')') ) {s = 22;}

                        else if ( (LA16_0=='.') ) {s = 23;}

                        else if ( (LA16_0==':') ) {s = 24;}

                        else if ( (LA16_0==';') ) {s = 25;}

                        else if ( (LA16_0==',') ) {s = 26;}

                        else if ( (LA16_0=='n') ) {s = 27;}

                        else if ( (LA16_0=='b') ) {s = 28;}

                        else if ( (LA16_0=='c') ) {s = 29;}

                        else if ( (LA16_0=='d') ) {s = 30;}

                        else if ( (LA16_0=='e') ) {s = 31;}

                        else if ( (LA16_0=='f') ) {s = 32;}

                        else if ( (LA16_0=='i') ) {s = 33;}

                        else if ( (LA16_0=='p') ) {s = 34;}

                        else if ( (LA16_0=='r') ) {s = 35;}

                        else if ( (LA16_0=='w') ) {s = 36;}

                        else if ( (LA16_0=='a') ) {s = 37;}

                        else if ( (LA16_0=='g') ) {s = 38;}

                        else if ( (LA16_0=='v') ) {s = 39;}

                        else if ( (LA16_0=='u') ) {s = 40;}

                        else if ( (LA16_0=='$') ) {s = 41;}

                        else if ( (LA16_0=='o') ) {s = 42;}

                        else if ( ((LA16_0>='0' && LA16_0<='9')) ) {s = 43;}

                        else if ( (LA16_0=='\'') ) {s = 44;}

                        else if ( ((LA16_0>='A' && LA16_0<='Z')||LA16_0=='_'||LA16_0=='h'||(LA16_0>='j' && LA16_0<='m')||LA16_0=='q'||(LA16_0>='x' && LA16_0<='z')) ) {s = 45;}

                        else if ( (LA16_0=='\"') ) {s = 46;}

                        else if ( ((LA16_0>='\t' && LA16_0<='\n')||LA16_0=='\r'||LA16_0==' ') ) {s = 47;}

                        else if ( ((LA16_0>='\u0000' && LA16_0<='\b')||(LA16_0>='\u000B' && LA16_0<='\f')||(LA16_0>='\u000E' && LA16_0<='\u001F')||LA16_0=='#'||LA16_0=='@'||LA16_0=='\\'||LA16_0=='`'||(LA16_0>='\u007F' && LA16_0<='\uFFFF')) ) {s = 48;}

                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA16_137 = input.LA(1);

                        s = -1;
                        if ( (LA16_137=='\'') ) {s = 192;}

                        else if ( ((LA16_137>='\u0000' && LA16_137<='&')||(LA16_137>='(' && LA16_137<='\uFFFF')) ) {s = 138;}

                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA16_191 = input.LA(1);

                        s = -1;
                        if ( (LA16_191=='\'') ) {s = 192;}

                        else if ( ((LA16_191>='\u0000' && LA16_191<='&')||(LA16_191>='(' && LA16_191<='\uFFFF')) ) {s = 138;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 16, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}