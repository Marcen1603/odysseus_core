// Generated from IDL.g4 by ANTLR 4.4
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class IDLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		INTEGER_LITERAL=1, OCTAL_LITERAL=2, HEX_LITERAL=3, FLOATING_PT_LITERAL=4, 
		FIXED_PT_LITERAL=5, WIDE_CHARACTER_LITERAL=6, CHARACTER_LITERAL=7, WIDE_STRING_LITERAL=8, 
		STRING_LITERAL=9, BOOLEAN_LITERAL=10, SEMICOLON=11, COLON=12, COMA=13, 
		LEFT_BRACE=14, RIGHT_BRACE=15, LEFT_BRACKET=16, RIGHT_BRACKET=17, LEFT_SQUARE_BRACKET=18, 
		RIGHT_SQUARE_BRACKET=19, TILDE=20, SLASH=21, LEFT_ANG_BRACKET=22, RIGHT_ANG_BRACKET=23, 
		STAR=24, PLUS=25, MINUS=26, CARET=27, AMPERSAND=28, PIPE=29, EQUAL=30, 
		PERCENT=31, DOUBLE_COLON=32, RIGHT_SHIFT=33, LEFT_SHIFT=34, KW_SETRAISES=35, 
		KW_OUT=36, KW_EMITS=37, KW_STRING=38, KW_SWITCH=39, KW_PUBLISHES=40, KW_TYPEDEF=41, 
		KW_USES=42, KW_PRIMARYKEY=43, KW_CUSTOM=44, KW_OCTET=45, KW_SEQUENCE=46, 
		KW_IMPORT=47, KW_STRUCT=48, KW_NATIVE=49, KW_READONLY=50, KW_FINDER=51, 
		KW_RAISES=52, KW_VOID=53, KW_PRIVATE=54, KW_EVENTTYPE=55, KW_WCHAR=56, 
		KW_IN=57, KW_DEFAULT=58, KW_PUBLIC=59, KW_SHORT=60, KW_LONG=61, KW_ENUM=62, 
		KW_WSTRING=63, KW_CONTEXT=64, KW_HOME=65, KW_FACTORY=66, KW_EXCEPTION=67, 
		KW_GETRAISES=68, KW_CONST=69, KW_VALUEBASE=70, KW_VALUETYPE=71, KW_SUPPORTS=72, 
		KW_MODULE=73, KW_OBJECT=74, KW_TRUNCATABLE=75, KW_UNSIGNED=76, KW_FIXED=77, 
		KW_UNION=78, KW_ONEWAY=79, KW_ANY=80, KW_CHAR=81, KW_CASE=82, KW_FLOAT=83, 
		KW_BOOLEAN=84, KW_MULTIPLE=85, KW_ABSTRACT=86, KW_INOUT=87, KW_PROVIDES=88, 
		KW_CONSUMES=89, KW_DOUBLE=90, KW_TYPEPREFIX=91, KW_TYPEID=92, KW_ATTRIBUTE=93, 
		KW_LOCAL=94, KW_MANAGES=95, KW_INTERFACE=96, KW_COMPONENT=97, ID=98, WS=99, 
		COMMENT=100, LINE_COMMENT=101;
	public static final String[] tokenNames = {
		"<INVALID>", "INTEGER_LITERAL", "OCTAL_LITERAL", "HEX_LITERAL", "FLOATING_PT_LITERAL", 
		"FIXED_PT_LITERAL", "WIDE_CHARACTER_LITERAL", "CHARACTER_LITERAL", "WIDE_STRING_LITERAL", 
		"STRING_LITERAL", "BOOLEAN_LITERAL", "';'", "':'", "','", "'{'", "'}'", 
		"'('", "')'", "'['", "']'", "'~'", "'/'", "'<'", "'>'", "'*'", "'+'", 
		"'-'", "'^'", "'&'", "'|'", "'='", "'%'", "'::'", "'>>'", "'<<'", "'setraises'", 
		"'out'", "'emits'", "'string'", "'switch'", "'publishes'", "'typedef'", 
		"'uses'", "'primarykey'", "'custom'", "'octet'", "'sequence'", "'import'", 
		"'struct'", "'native'", "'readonly'", "'finder'", "'raises'", "'void'", 
		"'private'", "'eventtype'", "'wchar'", "'in'", "'default'", "'public'", 
		"'short'", "'long'", "'enum'", "'wstring'", "'context'", "'home'", "'factory'", 
		"'exception'", "'getraises'", "'const'", "'ValueBase'", "'valuetype'", 
		"'supports'", "'module'", "'Object'", "'truncatable'", "'unsigned'", "'fixed'", 
		"'union'", "'oneway'", "'any'", "'char'", "'case'", "'float'", "'boolean'", 
		"'multiple'", "'abstract'", "'inout'", "'provides'", "'consumes'", "'double'", 
		"'typeprefix'", "'typeid'", "'attribute'", "'local'", "'manages'", "'interface'", 
		"'component'", "ID", "WS", "COMMENT", "LINE_COMMENT"
	};
	public static final int
		RULE_specification = 0, RULE_definition = 1, RULE_module = 2, RULE_interface_or_forward_decl = 3, 
		RULE_interface_decl = 4, RULE_forward_decl = 5, RULE_interface_header = 6, 
		RULE_interface_body = 7, RULE_export = 8, RULE_interface_inheritance_spec = 9, 
		RULE_interface_name = 10, RULE_scoped_name = 11, RULE_value = 12, RULE_value_forward_decl = 13, 
		RULE_value_box_decl = 14, RULE_value_abs_decl = 15, RULE_value_decl = 16, 
		RULE_value_header = 17, RULE_value_inheritance_spec = 18, RULE_value_name = 19, 
		RULE_value_element = 20, RULE_state_member = 21, RULE_init_decl = 22, 
		RULE_init_param_decls = 23, RULE_init_param_decl = 24, RULE_init_param_attribute = 25, 
		RULE_const_decl = 26, RULE_const_type = 27, RULE_const_exp = 28, RULE_or_expr = 29, 
		RULE_xor_expr = 30, RULE_and_expr = 31, RULE_shift_expr = 32, RULE_add_expr = 33, 
		RULE_mult_expr = 34, RULE_unary_expr = 35, RULE_unary_operator = 36, RULE_primary_expr = 37, 
		RULE_literal = 38, RULE_positive_int_const = 39, RULE_type_decl = 40, 
		RULE_type_declarator = 41, RULE_type_spec = 42, RULE_simple_type_spec = 43, 
		RULE_base_type_spec = 44, RULE_template_type_spec = 45, RULE_constr_type_spec = 46, 
		RULE_declarators = 47, RULE_declarator = 48, RULE_simple_declarator = 49, 
		RULE_complex_declarator = 50, RULE_floating_pt_type = 51, RULE_integer_type = 52, 
		RULE_signed_int = 53, RULE_signed_short_int = 54, RULE_signed_long_int = 55, 
		RULE_signed_longlong_int = 56, RULE_unsigned_int = 57, RULE_unsigned_short_int = 58, 
		RULE_unsigned_long_int = 59, RULE_unsigned_longlong_int = 60, RULE_char_type = 61, 
		RULE_wide_char_type = 62, RULE_boolean_type = 63, RULE_octet_type = 64, 
		RULE_any_type = 65, RULE_object_type = 66, RULE_struct_type = 67, RULE_member_list = 68, 
		RULE_member = 69, RULE_keymarker = 70, RULE_union_type = 71, RULE_switch_type_spec = 72, 
		RULE_switch_body = 73, RULE_case_stmt = 74, RULE_case_label = 75, RULE_element_spec = 76, 
		RULE_enum_type = 77, RULE_enumerator = 78, RULE_sequence_type = 79, RULE_string_type = 80, 
		RULE_wide_string_type = 81, RULE_array_declarator = 82, RULE_fixed_array_size = 83, 
		RULE_attr_decl = 84, RULE_except_decl = 85, RULE_op_decl = 86, RULE_op_attribute = 87, 
		RULE_op_type_spec = 88, RULE_parameter_decls = 89, RULE_param_decl = 90, 
		RULE_param_attribute = 91, RULE_raises_expr = 92, RULE_context_expr = 93, 
		RULE_param_type_spec = 94, RULE_fixed_pt_type = 95, RULE_fixed_pt_const_type = 96, 
		RULE_value_base_type = 97, RULE_constr_forward_decl = 98, RULE_import_decl = 99, 
		RULE_imported_scope = 100, RULE_type_id_decl = 101, RULE_type_prefix_decl = 102, 
		RULE_readonly_attr_spec = 103, RULE_readonly_attr_declarator = 104, RULE_attr_spec = 105, 
		RULE_attr_declarator = 106, RULE_attr_raises_expr = 107, RULE_get_excep_expr = 108, 
		RULE_set_excep_expr = 109, RULE_exception_list = 110, RULE_component = 111, 
		RULE_component_forward_decl = 112, RULE_component_decl = 113, RULE_component_header = 114, 
		RULE_supported_interface_spec = 115, RULE_component_inheritance_spec = 116, 
		RULE_component_body = 117, RULE_component_export = 118, RULE_provides_decl = 119, 
		RULE_interface_type = 120, RULE_uses_decl = 121, RULE_emits_decl = 122, 
		RULE_publishes_decl = 123, RULE_consumes_decl = 124, RULE_home_decl = 125, 
		RULE_home_header = 126, RULE_home_inheritance_spec = 127, RULE_primary_key_spec = 128, 
		RULE_home_body = 129, RULE_home_export = 130, RULE_factory_decl = 131, 
		RULE_finder_decl = 132, RULE_event = 133, RULE_event_forward_decl = 134, 
		RULE_event_abs_decl = 135, RULE_event_decl = 136, RULE_event_header = 137;
	public static final String[] ruleNames = {
		"specification", "definition", "module", "interface_or_forward_decl", 
		"interface_decl", "forward_decl", "interface_header", "interface_body", 
		"export", "interface_inheritance_spec", "interface_name", "scoped_name", 
		"value", "value_forward_decl", "value_box_decl", "value_abs_decl", "value_decl", 
		"value_header", "value_inheritance_spec", "value_name", "value_element", 
		"state_member", "init_decl", "init_param_decls", "init_param_decl", "init_param_attribute", 
		"const_decl", "const_type", "const_exp", "or_expr", "xor_expr", "and_expr", 
		"shift_expr", "add_expr", "mult_expr", "unary_expr", "unary_operator", 
		"primary_expr", "literal", "positive_int_const", "type_decl", "type_declarator", 
		"type_spec", "simple_type_spec", "base_type_spec", "template_type_spec", 
		"constr_type_spec", "declarators", "declarator", "simple_declarator", 
		"complex_declarator", "floating_pt_type", "integer_type", "signed_int", 
		"signed_short_int", "signed_long_int", "signed_longlong_int", "unsigned_int", 
		"unsigned_short_int", "unsigned_long_int", "unsigned_longlong_int", "char_type", 
		"wide_char_type", "boolean_type", "octet_type", "any_type", "object_type", 
		"struct_type", "member_list", "member", "keymarker", "union_type", "switch_type_spec", 
		"switch_body", "case_stmt", "case_label", "element_spec", "enum_type", 
		"enumerator", "sequence_type", "string_type", "wide_string_type", "array_declarator", 
		"fixed_array_size", "attr_decl", "except_decl", "op_decl", "op_attribute", 
		"op_type_spec", "parameter_decls", "param_decl", "param_attribute", "raises_expr", 
		"context_expr", "param_type_spec", "fixed_pt_type", "fixed_pt_const_type", 
		"value_base_type", "constr_forward_decl", "import_decl", "imported_scope", 
		"type_id_decl", "type_prefix_decl", "readonly_attr_spec", "readonly_attr_declarator", 
		"attr_spec", "attr_declarator", "attr_raises_expr", "get_excep_expr", 
		"set_excep_expr", "exception_list", "component", "component_forward_decl", 
		"component_decl", "component_header", "supported_interface_spec", "component_inheritance_spec", 
		"component_body", "component_export", "provides_decl", "interface_type", 
		"uses_decl", "emits_decl", "publishes_decl", "consumes_decl", "home_decl", 
		"home_header", "home_inheritance_spec", "primary_key_spec", "home_body", 
		"home_export", "factory_decl", "finder_decl", "event", "event_forward_decl", 
		"event_abs_decl", "event_decl", "event_header"
	};

	@Override
	public String getGrammarFileName() { return "IDL.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public IDLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class SpecificationContext extends ParserRuleContext {
		public List<Import_declContext> import_decl() {
			return getRuleContexts(Import_declContext.class);
		}
		public Import_declContext import_decl(int i) {
			return getRuleContext(Import_declContext.class,i);
		}
		public DefinitionContext definition(int i) {
			return getRuleContext(DefinitionContext.class,i);
		}
		public List<DefinitionContext> definition() {
			return getRuleContexts(DefinitionContext.class);
		}
		public SpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_specification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitSpecification(this);
		}
	}

	public final SpecificationContext specification() throws RecognitionException {
		SpecificationContext _localctx = new SpecificationContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_specification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==KW_IMPORT) {
				{
				{
				setState(276); import_decl();
				}
				}
				setState(281);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(283); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(282); definition();
				}
				}
				setState(285); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 41)) & ~0x3f) == 0 && ((1L << (_la - 41)) & ((1L << (KW_TYPEDEF - 41)) | (1L << (KW_CUSTOM - 41)) | (1L << (KW_STRUCT - 41)) | (1L << (KW_NATIVE - 41)) | (1L << (KW_EVENTTYPE - 41)) | (1L << (KW_ENUM - 41)) | (1L << (KW_HOME - 41)) | (1L << (KW_EXCEPTION - 41)) | (1L << (KW_CONST - 41)) | (1L << (KW_VALUETYPE - 41)) | (1L << (KW_MODULE - 41)) | (1L << (KW_UNION - 41)) | (1L << (KW_ABSTRACT - 41)) | (1L << (KW_TYPEPREFIX - 41)) | (1L << (KW_TYPEID - 41)) | (1L << (KW_LOCAL - 41)) | (1L << (KW_INTERFACE - 41)) | (1L << (KW_COMPONENT - 41)))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DefinitionContext extends ParserRuleContext {
		public TerminalNode SEMICOLON() { return getToken(IDLParser.SEMICOLON, 0); }
		public Interface_or_forward_declContext interface_or_forward_decl() {
			return getRuleContext(Interface_or_forward_declContext.class,0);
		}
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public Home_declContext home_decl() {
			return getRuleContext(Home_declContext.class,0);
		}
		public Type_prefix_declContext type_prefix_decl() {
			return getRuleContext(Type_prefix_declContext.class,0);
		}
		public Except_declContext except_decl() {
			return getRuleContext(Except_declContext.class,0);
		}
		public Type_declContext type_decl() {
			return getRuleContext(Type_declContext.class,0);
		}
		public Type_id_declContext type_id_decl() {
			return getRuleContext(Type_id_declContext.class,0);
		}
		public ModuleContext module() {
			return getRuleContext(ModuleContext.class,0);
		}
		public Const_declContext const_decl() {
			return getRuleContext(Const_declContext.class,0);
		}
		public EventContext event() {
			return getRuleContext(EventContext.class,0);
		}
		public ComponentContext component() {
			return getRuleContext(ComponentContext.class,0);
		}
		public DefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitDefinition(this);
		}
	}

	public final DefinitionContext definition() throws RecognitionException {
		DefinitionContext _localctx = new DefinitionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_definition);
		try {
			setState(320);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(287); type_decl();
				setState(288); match(SEMICOLON);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(290); const_decl();
				setState(291); match(SEMICOLON);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(293); except_decl();
				setState(294); match(SEMICOLON);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(296); interface_or_forward_decl();
				setState(297); match(SEMICOLON);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(299); module();
				setState(300); match(SEMICOLON);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(302); value();
				setState(303); match(SEMICOLON);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(305); type_id_decl();
				setState(306); match(SEMICOLON);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(308); type_prefix_decl();
				setState(309); match(SEMICOLON);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(311); event();
				setState(312); match(SEMICOLON);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(314); component();
				setState(315); match(SEMICOLON);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(317); home_decl();
				setState(318); match(SEMICOLON);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModuleContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public TerminalNode KW_MODULE() { return getToken(IDLParser.KW_MODULE, 0); }
		public TerminalNode LEFT_BRACE() { return getToken(IDLParser.LEFT_BRACE, 0); }
		public DefinitionContext definition(int i) {
			return getRuleContext(DefinitionContext.class,i);
		}
		public TerminalNode RIGHT_BRACE() { return getToken(IDLParser.RIGHT_BRACE, 0); }
		public List<DefinitionContext> definition() {
			return getRuleContexts(DefinitionContext.class);
		}
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitModule(this);
		}
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_module);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(322); match(KW_MODULE);
			setState(323); match(ID);
			setState(324); match(LEFT_BRACE);
			setState(326); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(325); definition();
				}
				}
				setState(328); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 41)) & ~0x3f) == 0 && ((1L << (_la - 41)) & ((1L << (KW_TYPEDEF - 41)) | (1L << (KW_CUSTOM - 41)) | (1L << (KW_STRUCT - 41)) | (1L << (KW_NATIVE - 41)) | (1L << (KW_EVENTTYPE - 41)) | (1L << (KW_ENUM - 41)) | (1L << (KW_HOME - 41)) | (1L << (KW_EXCEPTION - 41)) | (1L << (KW_CONST - 41)) | (1L << (KW_VALUETYPE - 41)) | (1L << (KW_MODULE - 41)) | (1L << (KW_UNION - 41)) | (1L << (KW_ABSTRACT - 41)) | (1L << (KW_TYPEPREFIX - 41)) | (1L << (KW_TYPEID - 41)) | (1L << (KW_LOCAL - 41)) | (1L << (KW_INTERFACE - 41)) | (1L << (KW_COMPONENT - 41)))) != 0) );
			setState(330); match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Interface_or_forward_declContext extends ParserRuleContext {
		public Interface_declContext interface_decl() {
			return getRuleContext(Interface_declContext.class,0);
		}
		public Forward_declContext forward_decl() {
			return getRuleContext(Forward_declContext.class,0);
		}
		public Interface_or_forward_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interface_or_forward_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterInterface_or_forward_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitInterface_or_forward_decl(this);
		}
	}

	public final Interface_or_forward_declContext interface_or_forward_decl() throws RecognitionException {
		Interface_or_forward_declContext _localctx = new Interface_or_forward_declContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_interface_or_forward_decl);
		try {
			setState(334);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(332); interface_decl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(333); forward_decl();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Interface_declContext extends ParserRuleContext {
		public Interface_headerContext interface_header() {
			return getRuleContext(Interface_headerContext.class,0);
		}
		public Interface_bodyContext interface_body() {
			return getRuleContext(Interface_bodyContext.class,0);
		}
		public TerminalNode LEFT_BRACE() { return getToken(IDLParser.LEFT_BRACE, 0); }
		public TerminalNode RIGHT_BRACE() { return getToken(IDLParser.RIGHT_BRACE, 0); }
		public Interface_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interface_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterInterface_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitInterface_decl(this);
		}
	}

	public final Interface_declContext interface_decl() throws RecognitionException {
		Interface_declContext _localctx = new Interface_declContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_interface_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(336); interface_header();
			setState(337); match(LEFT_BRACE);
			setState(338); interface_body();
			setState(339); match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Forward_declContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public TerminalNode KW_LOCAL() { return getToken(IDLParser.KW_LOCAL, 0); }
		public TerminalNode KW_INTERFACE() { return getToken(IDLParser.KW_INTERFACE, 0); }
		public TerminalNode KW_ABSTRACT() { return getToken(IDLParser.KW_ABSTRACT, 0); }
		public Forward_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forward_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterForward_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitForward_decl(this);
		}
	}

	public final Forward_declContext forward_decl() throws RecognitionException {
		Forward_declContext _localctx = new Forward_declContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_forward_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(342);
			_la = _input.LA(1);
			if (_la==KW_ABSTRACT || _la==KW_LOCAL) {
				{
				setState(341);
				_la = _input.LA(1);
				if ( !(_la==KW_ABSTRACT || _la==KW_LOCAL) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
			}

			setState(344); match(KW_INTERFACE);
			setState(345); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Interface_headerContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public Interface_inheritance_specContext interface_inheritance_spec() {
			return getRuleContext(Interface_inheritance_specContext.class,0);
		}
		public TerminalNode KW_LOCAL() { return getToken(IDLParser.KW_LOCAL, 0); }
		public TerminalNode KW_INTERFACE() { return getToken(IDLParser.KW_INTERFACE, 0); }
		public TerminalNode KW_ABSTRACT() { return getToken(IDLParser.KW_ABSTRACT, 0); }
		public Interface_headerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interface_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterInterface_header(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitInterface_header(this);
		}
	}

	public final Interface_headerContext interface_header() throws RecognitionException {
		Interface_headerContext _localctx = new Interface_headerContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_interface_header);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(348);
			_la = _input.LA(1);
			if (_la==KW_ABSTRACT || _la==KW_LOCAL) {
				{
				setState(347);
				_la = _input.LA(1);
				if ( !(_la==KW_ABSTRACT || _la==KW_LOCAL) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
			}

			setState(350); match(KW_INTERFACE);
			setState(351); match(ID);
			setState(353);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(352); interface_inheritance_spec();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Interface_bodyContext extends ParserRuleContext {
		public List<ExportContext> export() {
			return getRuleContexts(ExportContext.class);
		}
		public ExportContext export(int i) {
			return getRuleContext(ExportContext.class,i);
		}
		public Interface_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interface_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterInterface_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitInterface_body(this);
		}
	}

	public final Interface_bodyContext interface_body() throws RecognitionException {
		Interface_bodyContext _localctx = new Interface_bodyContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_interface_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(358);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE_COLON) | (1L << KW_STRING) | (1L << KW_TYPEDEF) | (1L << KW_OCTET) | (1L << KW_STRUCT) | (1L << KW_NATIVE) | (1L << KW_READONLY) | (1L << KW_VOID) | (1L << KW_WCHAR) | (1L << KW_SHORT) | (1L << KW_LONG) | (1L << KW_ENUM) | (1L << KW_WSTRING))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (KW_EXCEPTION - 67)) | (1L << (KW_CONST - 67)) | (1L << (KW_VALUEBASE - 67)) | (1L << (KW_OBJECT - 67)) | (1L << (KW_UNSIGNED - 67)) | (1L << (KW_UNION - 67)) | (1L << (KW_ONEWAY - 67)) | (1L << (KW_ANY - 67)) | (1L << (KW_CHAR - 67)) | (1L << (KW_FLOAT - 67)) | (1L << (KW_BOOLEAN - 67)) | (1L << (KW_DOUBLE - 67)) | (1L << (KW_TYPEPREFIX - 67)) | (1L << (KW_TYPEID - 67)) | (1L << (KW_ATTRIBUTE - 67)) | (1L << (ID - 67)))) != 0)) {
				{
				{
				setState(355); export();
				}
				}
				setState(360);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExportContext extends ParserRuleContext {
		public TerminalNode SEMICOLON() { return getToken(IDLParser.SEMICOLON, 0); }
		public Attr_declContext attr_decl() {
			return getRuleContext(Attr_declContext.class,0);
		}
		public Type_prefix_declContext type_prefix_decl() {
			return getRuleContext(Type_prefix_declContext.class,0);
		}
		public Except_declContext except_decl() {
			return getRuleContext(Except_declContext.class,0);
		}
		public Type_declContext type_decl() {
			return getRuleContext(Type_declContext.class,0);
		}
		public Type_id_declContext type_id_decl() {
			return getRuleContext(Type_id_declContext.class,0);
		}
		public Const_declContext const_decl() {
			return getRuleContext(Const_declContext.class,0);
		}
		public Op_declContext op_decl() {
			return getRuleContext(Op_declContext.class,0);
		}
		public ExportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_export; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterExport(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitExport(this);
		}
	}

	public final ExportContext export() throws RecognitionException {
		ExportContext _localctx = new ExportContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_export);
		try {
			setState(382);
			switch (_input.LA(1)) {
			case KW_TYPEDEF:
			case KW_STRUCT:
			case KW_NATIVE:
			case KW_ENUM:
			case KW_UNION:
				enterOuterAlt(_localctx, 1);
				{
				setState(361); type_decl();
				setState(362); match(SEMICOLON);
				}
				break;
			case KW_CONST:
				enterOuterAlt(_localctx, 2);
				{
				setState(364); const_decl();
				setState(365); match(SEMICOLON);
				}
				break;
			case KW_EXCEPTION:
				enterOuterAlt(_localctx, 3);
				{
				setState(367); except_decl();
				setState(368); match(SEMICOLON);
				}
				break;
			case KW_READONLY:
			case KW_ATTRIBUTE:
				enterOuterAlt(_localctx, 4);
				{
				setState(370); attr_decl();
				setState(371); match(SEMICOLON);
				}
				break;
			case DOUBLE_COLON:
			case KW_STRING:
			case KW_OCTET:
			case KW_VOID:
			case KW_WCHAR:
			case KW_SHORT:
			case KW_LONG:
			case KW_WSTRING:
			case KW_VALUEBASE:
			case KW_OBJECT:
			case KW_UNSIGNED:
			case KW_ONEWAY:
			case KW_ANY:
			case KW_CHAR:
			case KW_FLOAT:
			case KW_BOOLEAN:
			case KW_DOUBLE:
			case ID:
				enterOuterAlt(_localctx, 5);
				{
				setState(373); op_decl();
				setState(374); match(SEMICOLON);
				}
				break;
			case KW_TYPEID:
				enterOuterAlt(_localctx, 6);
				{
				setState(376); type_id_decl();
				setState(377); match(SEMICOLON);
				}
				break;
			case KW_TYPEPREFIX:
				enterOuterAlt(_localctx, 7);
				{
				setState(379); type_prefix_decl();
				setState(380); match(SEMICOLON);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Interface_inheritance_specContext extends ParserRuleContext {
		public Interface_nameContext interface_name(int i) {
			return getRuleContext(Interface_nameContext.class,i);
		}
		public List<Interface_nameContext> interface_name() {
			return getRuleContexts(Interface_nameContext.class);
		}
		public TerminalNode COLON() { return getToken(IDLParser.COLON, 0); }
		public TerminalNode COMA(int i) {
			return getToken(IDLParser.COMA, i);
		}
		public List<TerminalNode> COMA() { return getTokens(IDLParser.COMA); }
		public Interface_inheritance_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interface_inheritance_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterInterface_inheritance_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitInterface_inheritance_spec(this);
		}
	}

	public final Interface_inheritance_specContext interface_inheritance_spec() throws RecognitionException {
		Interface_inheritance_specContext _localctx = new Interface_inheritance_specContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_interface_inheritance_spec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(384); match(COLON);
			setState(385); interface_name();
			setState(390);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(386); match(COMA);
				setState(387); interface_name();
				}
				}
				setState(392);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Interface_nameContext extends ParserRuleContext {
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public Interface_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interface_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterInterface_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitInterface_name(this);
		}
	}

	public final Interface_nameContext interface_name() throws RecognitionException {
		Interface_nameContext _localctx = new Interface_nameContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_interface_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(393); scoped_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Scoped_nameContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(IDLParser.ID); }
		public TerminalNode DOUBLE_COLON(int i) {
			return getToken(IDLParser.DOUBLE_COLON, i);
		}
		public TerminalNode ID(int i) {
			return getToken(IDLParser.ID, i);
		}
		public List<TerminalNode> DOUBLE_COLON() { return getTokens(IDLParser.DOUBLE_COLON); }
		public Scoped_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scoped_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterScoped_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitScoped_name(this);
		}
	}

	public final Scoped_nameContext scoped_name() throws RecognitionException {
		Scoped_nameContext _localctx = new Scoped_nameContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_scoped_name);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(396);
			_la = _input.LA(1);
			if (_la==DOUBLE_COLON) {
				{
				setState(395); match(DOUBLE_COLON);
				}
			}

			setState(398); match(ID);
			setState(403);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOUBLE_COLON) {
				{
				{
				setState(399); match(DOUBLE_COLON);
				setState(400); match(ID);
				}
				}
				setState(405);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public Value_declContext value_decl() {
			return getRuleContext(Value_declContext.class,0);
		}
		public Value_forward_declContext value_forward_decl() {
			return getRuleContext(Value_forward_declContext.class,0);
		}
		public Value_abs_declContext value_abs_decl() {
			return getRuleContext(Value_abs_declContext.class,0);
		}
		public Value_box_declContext value_box_decl() {
			return getRuleContext(Value_box_declContext.class,0);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitValue(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(410);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				setState(406); value_decl();
				}
				break;
			case 2:
				{
				setState(407); value_abs_decl();
				}
				break;
			case 3:
				{
				setState(408); value_box_decl();
				}
				break;
			case 4:
				{
				setState(409); value_forward_decl();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Value_forward_declContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public TerminalNode KW_VALUETYPE() { return getToken(IDLParser.KW_VALUETYPE, 0); }
		public TerminalNode KW_ABSTRACT() { return getToken(IDLParser.KW_ABSTRACT, 0); }
		public Value_forward_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value_forward_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterValue_forward_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitValue_forward_decl(this);
		}
	}

	public final Value_forward_declContext value_forward_decl() throws RecognitionException {
		Value_forward_declContext _localctx = new Value_forward_declContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_value_forward_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(413);
			_la = _input.LA(1);
			if (_la==KW_ABSTRACT) {
				{
				setState(412); match(KW_ABSTRACT);
				}
			}

			setState(415); match(KW_VALUETYPE);
			setState(416); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Value_box_declContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public Type_specContext type_spec() {
			return getRuleContext(Type_specContext.class,0);
		}
		public TerminalNode KW_VALUETYPE() { return getToken(IDLParser.KW_VALUETYPE, 0); }
		public Value_box_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value_box_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterValue_box_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitValue_box_decl(this);
		}
	}

	public final Value_box_declContext value_box_decl() throws RecognitionException {
		Value_box_declContext _localctx = new Value_box_declContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_value_box_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(418); match(KW_VALUETYPE);
			setState(419); match(ID);
			setState(420); type_spec();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Value_abs_declContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public List<ExportContext> export() {
			return getRuleContexts(ExportContext.class);
		}
		public ExportContext export(int i) {
			return getRuleContext(ExportContext.class,i);
		}
		public TerminalNode LEFT_BRACE() { return getToken(IDLParser.LEFT_BRACE, 0); }
		public TerminalNode KW_ABSTRACT() { return getToken(IDLParser.KW_ABSTRACT, 0); }
		public TerminalNode KW_VALUETYPE() { return getToken(IDLParser.KW_VALUETYPE, 0); }
		public Value_inheritance_specContext value_inheritance_spec() {
			return getRuleContext(Value_inheritance_specContext.class,0);
		}
		public TerminalNode RIGHT_BRACE() { return getToken(IDLParser.RIGHT_BRACE, 0); }
		public Value_abs_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value_abs_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterValue_abs_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitValue_abs_decl(this);
		}
	}

	public final Value_abs_declContext value_abs_decl() throws RecognitionException {
		Value_abs_declContext _localctx = new Value_abs_declContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_value_abs_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(422); match(KW_ABSTRACT);
			setState(423); match(KW_VALUETYPE);
			setState(424); match(ID);
			setState(425); value_inheritance_spec();
			setState(426); match(LEFT_BRACE);
			setState(430);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE_COLON) | (1L << KW_STRING) | (1L << KW_TYPEDEF) | (1L << KW_OCTET) | (1L << KW_STRUCT) | (1L << KW_NATIVE) | (1L << KW_READONLY) | (1L << KW_VOID) | (1L << KW_WCHAR) | (1L << KW_SHORT) | (1L << KW_LONG) | (1L << KW_ENUM) | (1L << KW_WSTRING))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (KW_EXCEPTION - 67)) | (1L << (KW_CONST - 67)) | (1L << (KW_VALUEBASE - 67)) | (1L << (KW_OBJECT - 67)) | (1L << (KW_UNSIGNED - 67)) | (1L << (KW_UNION - 67)) | (1L << (KW_ONEWAY - 67)) | (1L << (KW_ANY - 67)) | (1L << (KW_CHAR - 67)) | (1L << (KW_FLOAT - 67)) | (1L << (KW_BOOLEAN - 67)) | (1L << (KW_DOUBLE - 67)) | (1L << (KW_TYPEPREFIX - 67)) | (1L << (KW_TYPEID - 67)) | (1L << (KW_ATTRIBUTE - 67)) | (1L << (ID - 67)))) != 0)) {
				{
				{
				setState(427); export();
				}
				}
				setState(432);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(433); match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Value_declContext extends ParserRuleContext {
		public Value_headerContext value_header() {
			return getRuleContext(Value_headerContext.class,0);
		}
		public TerminalNode LEFT_BRACE() { return getToken(IDLParser.LEFT_BRACE, 0); }
		public Value_elementContext value_element(int i) {
			return getRuleContext(Value_elementContext.class,i);
		}
		public List<Value_elementContext> value_element() {
			return getRuleContexts(Value_elementContext.class);
		}
		public TerminalNode RIGHT_BRACE() { return getToken(IDLParser.RIGHT_BRACE, 0); }
		public Value_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterValue_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitValue_decl(this);
		}
	}

	public final Value_declContext value_decl() throws RecognitionException {
		Value_declContext _localctx = new Value_declContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_value_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(435); value_header();
			setState(436); match(LEFT_BRACE);
			setState(440);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE_COLON) | (1L << KW_STRING) | (1L << KW_TYPEDEF) | (1L << KW_OCTET) | (1L << KW_STRUCT) | (1L << KW_NATIVE) | (1L << KW_READONLY) | (1L << KW_VOID) | (1L << KW_PRIVATE) | (1L << KW_WCHAR) | (1L << KW_PUBLIC) | (1L << KW_SHORT) | (1L << KW_LONG) | (1L << KW_ENUM) | (1L << KW_WSTRING))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (KW_FACTORY - 66)) | (1L << (KW_EXCEPTION - 66)) | (1L << (KW_CONST - 66)) | (1L << (KW_VALUEBASE - 66)) | (1L << (KW_OBJECT - 66)) | (1L << (KW_UNSIGNED - 66)) | (1L << (KW_UNION - 66)) | (1L << (KW_ONEWAY - 66)) | (1L << (KW_ANY - 66)) | (1L << (KW_CHAR - 66)) | (1L << (KW_FLOAT - 66)) | (1L << (KW_BOOLEAN - 66)) | (1L << (KW_DOUBLE - 66)) | (1L << (KW_TYPEPREFIX - 66)) | (1L << (KW_TYPEID - 66)) | (1L << (KW_ATTRIBUTE - 66)) | (1L << (ID - 66)))) != 0)) {
				{
				{
				setState(437); value_element();
				}
				}
				setState(442);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(443); match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Value_headerContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public TerminalNode KW_VALUETYPE() { return getToken(IDLParser.KW_VALUETYPE, 0); }
		public TerminalNode KW_CUSTOM() { return getToken(IDLParser.KW_CUSTOM, 0); }
		public Value_inheritance_specContext value_inheritance_spec() {
			return getRuleContext(Value_inheritance_specContext.class,0);
		}
		public Value_headerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterValue_header(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitValue_header(this);
		}
	}

	public final Value_headerContext value_header() throws RecognitionException {
		Value_headerContext _localctx = new Value_headerContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_value_header);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(446);
			_la = _input.LA(1);
			if (_la==KW_CUSTOM) {
				{
				setState(445); match(KW_CUSTOM);
				}
			}

			setState(448); match(KW_VALUETYPE);
			setState(449); match(ID);
			setState(450); value_inheritance_spec();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Value_inheritance_specContext extends ParserRuleContext {
		public Interface_nameContext interface_name(int i) {
			return getRuleContext(Interface_nameContext.class,i);
		}
		public List<Interface_nameContext> interface_name() {
			return getRuleContexts(Interface_nameContext.class);
		}
		public TerminalNode COLON() { return getToken(IDLParser.COLON, 0); }
		public Value_nameContext value_name(int i) {
			return getRuleContext(Value_nameContext.class,i);
		}
		public TerminalNode COMA(int i) {
			return getToken(IDLParser.COMA, i);
		}
		public TerminalNode KW_TRUNCATABLE() { return getToken(IDLParser.KW_TRUNCATABLE, 0); }
		public List<Value_nameContext> value_name() {
			return getRuleContexts(Value_nameContext.class);
		}
		public TerminalNode KW_SUPPORTS() { return getToken(IDLParser.KW_SUPPORTS, 0); }
		public List<TerminalNode> COMA() { return getTokens(IDLParser.COMA); }
		public Value_inheritance_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value_inheritance_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterValue_inheritance_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitValue_inheritance_spec(this);
		}
	}

	public final Value_inheritance_specContext value_inheritance_spec() throws RecognitionException {
		Value_inheritance_specContext _localctx = new Value_inheritance_specContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_value_inheritance_spec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(464);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(452); match(COLON);
				setState(454);
				_la = _input.LA(1);
				if (_la==KW_TRUNCATABLE) {
					{
					setState(453); match(KW_TRUNCATABLE);
					}
				}

				setState(456); value_name();
				setState(461);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMA) {
					{
					{
					setState(457); match(COMA);
					setState(458); value_name();
					}
					}
					setState(463);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(475);
			_la = _input.LA(1);
			if (_la==KW_SUPPORTS) {
				{
				setState(466); match(KW_SUPPORTS);
				setState(467); interface_name();
				setState(472);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMA) {
					{
					{
					setState(468); match(COMA);
					setState(469); interface_name();
					}
					}
					setState(474);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Value_nameContext extends ParserRuleContext {
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public Value_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterValue_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitValue_name(this);
		}
	}

	public final Value_nameContext value_name() throws RecognitionException {
		Value_nameContext _localctx = new Value_nameContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_value_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(477); scoped_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Value_elementContext extends ParserRuleContext {
		public ExportContext export() {
			return getRuleContext(ExportContext.class,0);
		}
		public State_memberContext state_member() {
			return getRuleContext(State_memberContext.class,0);
		}
		public Init_declContext init_decl() {
			return getRuleContext(Init_declContext.class,0);
		}
		public Value_elementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value_element; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterValue_element(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitValue_element(this);
		}
	}

	public final Value_elementContext value_element() throws RecognitionException {
		Value_elementContext _localctx = new Value_elementContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_value_element);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(482);
			switch (_input.LA(1)) {
			case DOUBLE_COLON:
			case KW_STRING:
			case KW_TYPEDEF:
			case KW_OCTET:
			case KW_STRUCT:
			case KW_NATIVE:
			case KW_READONLY:
			case KW_VOID:
			case KW_WCHAR:
			case KW_SHORT:
			case KW_LONG:
			case KW_ENUM:
			case KW_WSTRING:
			case KW_EXCEPTION:
			case KW_CONST:
			case KW_VALUEBASE:
			case KW_OBJECT:
			case KW_UNSIGNED:
			case KW_UNION:
			case KW_ONEWAY:
			case KW_ANY:
			case KW_CHAR:
			case KW_FLOAT:
			case KW_BOOLEAN:
			case KW_DOUBLE:
			case KW_TYPEPREFIX:
			case KW_TYPEID:
			case KW_ATTRIBUTE:
			case ID:
				{
				setState(479); export();
				}
				break;
			case KW_PRIVATE:
			case KW_PUBLIC:
				{
				setState(480); state_member();
				}
				break;
			case KW_FACTORY:
				{
				setState(481); init_decl();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class State_memberContext extends ParserRuleContext {
		public TerminalNode KW_PUBLIC() { return getToken(IDLParser.KW_PUBLIC, 0); }
		public TerminalNode SEMICOLON() { return getToken(IDLParser.SEMICOLON, 0); }
		public TerminalNode KW_PRIVATE() { return getToken(IDLParser.KW_PRIVATE, 0); }
		public Type_specContext type_spec() {
			return getRuleContext(Type_specContext.class,0);
		}
		public DeclaratorsContext declarators() {
			return getRuleContext(DeclaratorsContext.class,0);
		}
		public State_memberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_state_member; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterState_member(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitState_member(this);
		}
	}

	public final State_memberContext state_member() throws RecognitionException {
		State_memberContext _localctx = new State_memberContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_state_member);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(484);
			_la = _input.LA(1);
			if ( !(_la==KW_PRIVATE || _la==KW_PUBLIC) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(485); type_spec();
			setState(486); declarators();
			setState(487); match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Init_declContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public TerminalNode SEMICOLON() { return getToken(IDLParser.SEMICOLON, 0); }
		public Init_param_declsContext init_param_decls() {
			return getRuleContext(Init_param_declsContext.class,0);
		}
		public TerminalNode KW_FACTORY() { return getToken(IDLParser.KW_FACTORY, 0); }
		public TerminalNode LEFT_BRACKET() { return getToken(IDLParser.LEFT_BRACKET, 0); }
		public TerminalNode RIGHT_BRACKET() { return getToken(IDLParser.RIGHT_BRACKET, 0); }
		public Raises_exprContext raises_expr() {
			return getRuleContext(Raises_exprContext.class,0);
		}
		public Init_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_init_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterInit_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitInit_decl(this);
		}
	}

	public final Init_declContext init_decl() throws RecognitionException {
		Init_declContext _localctx = new Init_declContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_init_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(489); match(KW_FACTORY);
			setState(490); match(ID);
			setState(491); match(LEFT_BRACKET);
			setState(493);
			_la = _input.LA(1);
			if (_la==KW_IN) {
				{
				setState(492); init_param_decls();
				}
			}

			setState(495); match(RIGHT_BRACKET);
			setState(497);
			_la = _input.LA(1);
			if (_la==KW_RAISES) {
				{
				setState(496); raises_expr();
				}
			}

			setState(499); match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Init_param_declsContext extends ParserRuleContext {
		public Init_param_declContext init_param_decl(int i) {
			return getRuleContext(Init_param_declContext.class,i);
		}
		public List<Init_param_declContext> init_param_decl() {
			return getRuleContexts(Init_param_declContext.class);
		}
		public TerminalNode COMA(int i) {
			return getToken(IDLParser.COMA, i);
		}
		public List<TerminalNode> COMA() { return getTokens(IDLParser.COMA); }
		public Init_param_declsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_init_param_decls; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterInit_param_decls(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitInit_param_decls(this);
		}
	}

	public final Init_param_declsContext init_param_decls() throws RecognitionException {
		Init_param_declsContext _localctx = new Init_param_declsContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_init_param_decls);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(501); init_param_decl();
			setState(506);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(502); match(COMA);
				setState(503); init_param_decl();
				}
				}
				setState(508);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Init_param_declContext extends ParserRuleContext {
		public Param_type_specContext param_type_spec() {
			return getRuleContext(Param_type_specContext.class,0);
		}
		public Simple_declaratorContext simple_declarator() {
			return getRuleContext(Simple_declaratorContext.class,0);
		}
		public Init_param_attributeContext init_param_attribute() {
			return getRuleContext(Init_param_attributeContext.class,0);
		}
		public Init_param_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_init_param_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterInit_param_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitInit_param_decl(this);
		}
	}

	public final Init_param_declContext init_param_decl() throws RecognitionException {
		Init_param_declContext _localctx = new Init_param_declContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_init_param_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(509); init_param_attribute();
			setState(510); param_type_spec();
			setState(511); simple_declarator();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Init_param_attributeContext extends ParserRuleContext {
		public TerminalNode KW_IN() { return getToken(IDLParser.KW_IN, 0); }
		public Init_param_attributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_init_param_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterInit_param_attribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitInit_param_attribute(this);
		}
	}

	public final Init_param_attributeContext init_param_attribute() throws RecognitionException {
		Init_param_attributeContext _localctx = new Init_param_attributeContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_init_param_attribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(513); match(KW_IN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Const_declContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public TerminalNode EQUAL() { return getToken(IDLParser.EQUAL, 0); }
		public TerminalNode KW_CONST() { return getToken(IDLParser.KW_CONST, 0); }
		public Const_typeContext const_type() {
			return getRuleContext(Const_typeContext.class,0);
		}
		public Const_expContext const_exp() {
			return getRuleContext(Const_expContext.class,0);
		}
		public Const_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_const_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterConst_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitConst_decl(this);
		}
	}

	public final Const_declContext const_decl() throws RecognitionException {
		Const_declContext _localctx = new Const_declContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_const_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(515); match(KW_CONST);
			setState(516); const_type();
			setState(517); match(ID);
			setState(518); match(EQUAL);
			setState(519); const_exp();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Const_typeContext extends ParserRuleContext {
		public String_typeContext string_type() {
			return getRuleContext(String_typeContext.class,0);
		}
		public Octet_typeContext octet_type() {
			return getRuleContext(Octet_typeContext.class,0);
		}
		public Boolean_typeContext boolean_type() {
			return getRuleContext(Boolean_typeContext.class,0);
		}
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public Fixed_pt_const_typeContext fixed_pt_const_type() {
			return getRuleContext(Fixed_pt_const_typeContext.class,0);
		}
		public Wide_char_typeContext wide_char_type() {
			return getRuleContext(Wide_char_typeContext.class,0);
		}
		public Integer_typeContext integer_type() {
			return getRuleContext(Integer_typeContext.class,0);
		}
		public Char_typeContext char_type() {
			return getRuleContext(Char_typeContext.class,0);
		}
		public Wide_string_typeContext wide_string_type() {
			return getRuleContext(Wide_string_typeContext.class,0);
		}
		public Floating_pt_typeContext floating_pt_type() {
			return getRuleContext(Floating_pt_typeContext.class,0);
		}
		public Const_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_const_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterConst_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitConst_type(this);
		}
	}

	public final Const_typeContext const_type() throws RecognitionException {
		Const_typeContext _localctx = new Const_typeContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_const_type);
		try {
			setState(531);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(521); integer_type();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(522); char_type();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(523); wide_char_type();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(524); boolean_type();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(525); floating_pt_type();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(526); string_type();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(527); wide_string_type();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(528); fixed_pt_const_type();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(529); scoped_name();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(530); octet_type();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Const_expContext extends ParserRuleContext {
		public Or_exprContext or_expr() {
			return getRuleContext(Or_exprContext.class,0);
		}
		public Const_expContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_const_exp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterConst_exp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitConst_exp(this);
		}
	}

	public final Const_expContext const_exp() throws RecognitionException {
		Const_expContext _localctx = new Const_expContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_const_exp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(533); or_expr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Or_exprContext extends ParserRuleContext {
		public List<Xor_exprContext> xor_expr() {
			return getRuleContexts(Xor_exprContext.class);
		}
		public List<TerminalNode> PIPE() { return getTokens(IDLParser.PIPE); }
		public Xor_exprContext xor_expr(int i) {
			return getRuleContext(Xor_exprContext.class,i);
		}
		public TerminalNode PIPE(int i) {
			return getToken(IDLParser.PIPE, i);
		}
		public Or_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_or_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterOr_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitOr_expr(this);
		}
	}

	public final Or_exprContext or_expr() throws RecognitionException {
		Or_exprContext _localctx = new Or_exprContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_or_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(535); xor_expr();
			setState(540);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PIPE) {
				{
				{
				setState(536); match(PIPE);
				setState(537); xor_expr();
				}
				}
				setState(542);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Xor_exprContext extends ParserRuleContext {
		public List<And_exprContext> and_expr() {
			return getRuleContexts(And_exprContext.class);
		}
		public TerminalNode CARET(int i) {
			return getToken(IDLParser.CARET, i);
		}
		public List<TerminalNode> CARET() { return getTokens(IDLParser.CARET); }
		public And_exprContext and_expr(int i) {
			return getRuleContext(And_exprContext.class,i);
		}
		public Xor_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xor_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterXor_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitXor_expr(this);
		}
	}

	public final Xor_exprContext xor_expr() throws RecognitionException {
		Xor_exprContext _localctx = new Xor_exprContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_xor_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(543); and_expr();
			setState(548);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CARET) {
				{
				{
				setState(544); match(CARET);
				setState(545); and_expr();
				}
				}
				setState(550);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class And_exprContext extends ParserRuleContext {
		public Shift_exprContext shift_expr(int i) {
			return getRuleContext(Shift_exprContext.class,i);
		}
		public List<Shift_exprContext> shift_expr() {
			return getRuleContexts(Shift_exprContext.class);
		}
		public List<TerminalNode> AMPERSAND() { return getTokens(IDLParser.AMPERSAND); }
		public TerminalNode AMPERSAND(int i) {
			return getToken(IDLParser.AMPERSAND, i);
		}
		public And_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_and_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterAnd_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitAnd_expr(this);
		}
	}

	public final And_exprContext and_expr() throws RecognitionException {
		And_exprContext _localctx = new And_exprContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_and_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(551); shift_expr();
			setState(556);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AMPERSAND) {
				{
				{
				setState(552); match(AMPERSAND);
				setState(553); shift_expr();
				}
				}
				setState(558);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Shift_exprContext extends ParserRuleContext {
		public List<TerminalNode> RIGHT_SHIFT() { return getTokens(IDLParser.RIGHT_SHIFT); }
		public List<Add_exprContext> add_expr() {
			return getRuleContexts(Add_exprContext.class);
		}
		public TerminalNode RIGHT_SHIFT(int i) {
			return getToken(IDLParser.RIGHT_SHIFT, i);
		}
		public Add_exprContext add_expr(int i) {
			return getRuleContext(Add_exprContext.class,i);
		}
		public List<TerminalNode> LEFT_SHIFT() { return getTokens(IDLParser.LEFT_SHIFT); }
		public TerminalNode LEFT_SHIFT(int i) {
			return getToken(IDLParser.LEFT_SHIFT, i);
		}
		public Shift_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_shift_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterShift_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitShift_expr(this);
		}
	}

	public final Shift_exprContext shift_expr() throws RecognitionException {
		Shift_exprContext _localctx = new Shift_exprContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_shift_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(559); add_expr();
			setState(564);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==RIGHT_SHIFT || _la==LEFT_SHIFT) {
				{
				{
				setState(560);
				_la = _input.LA(1);
				if ( !(_la==RIGHT_SHIFT || _la==LEFT_SHIFT) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(561); add_expr();
				}
				}
				setState(566);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Add_exprContext extends ParserRuleContext {
		public TerminalNode MINUS(int i) {
			return getToken(IDLParser.MINUS, i);
		}
		public List<Mult_exprContext> mult_expr() {
			return getRuleContexts(Mult_exprContext.class);
		}
		public Mult_exprContext mult_expr(int i) {
			return getRuleContext(Mult_exprContext.class,i);
		}
		public List<TerminalNode> PLUS() { return getTokens(IDLParser.PLUS); }
		public List<TerminalNode> MINUS() { return getTokens(IDLParser.MINUS); }
		public TerminalNode PLUS(int i) {
			return getToken(IDLParser.PLUS, i);
		}
		public Add_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_add_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterAdd_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitAdd_expr(this);
		}
	}

	public final Add_exprContext add_expr() throws RecognitionException {
		Add_exprContext _localctx = new Add_exprContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_add_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(567); mult_expr();
			setState(572);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PLUS || _la==MINUS) {
				{
				{
				setState(568);
				_la = _input.LA(1);
				if ( !(_la==PLUS || _la==MINUS) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(569); mult_expr();
				}
				}
				setState(574);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Mult_exprContext extends ParserRuleContext {
		public List<Unary_exprContext> unary_expr() {
			return getRuleContexts(Unary_exprContext.class);
		}
		public Unary_exprContext unary_expr(int i) {
			return getRuleContext(Unary_exprContext.class,i);
		}
		public List<TerminalNode> SLASH() { return getTokens(IDLParser.SLASH); }
		public TerminalNode PERCENT(int i) {
			return getToken(IDLParser.PERCENT, i);
		}
		public TerminalNode SLASH(int i) {
			return getToken(IDLParser.SLASH, i);
		}
		public List<TerminalNode> PERCENT() { return getTokens(IDLParser.PERCENT); }
		public Mult_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mult_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterMult_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitMult_expr(this);
		}
	}

	public final Mult_exprContext mult_expr() throws RecognitionException {
		Mult_exprContext _localctx = new Mult_exprContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_mult_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(575); unary_expr();
			setState(580);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SLASH) | (1L << STAR) | (1L << PERCENT))) != 0)) {
				{
				{
				setState(576);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SLASH) | (1L << STAR) | (1L << PERCENT))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(577); unary_expr();
				}
				}
				setState(582);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Unary_exprContext extends ParserRuleContext {
		public Primary_exprContext primary_expr() {
			return getRuleContext(Primary_exprContext.class,0);
		}
		public Unary_operatorContext unary_operator() {
			return getRuleContext(Unary_operatorContext.class,0);
		}
		public Unary_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterUnary_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitUnary_expr(this);
		}
	}

	public final Unary_exprContext unary_expr() throws RecognitionException {
		Unary_exprContext _localctx = new Unary_exprContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_unary_expr);
		try {
			setState(587);
			switch (_input.LA(1)) {
			case TILDE:
			case PLUS:
			case MINUS:
				enterOuterAlt(_localctx, 1);
				{
				setState(583); unary_operator();
				setState(584); primary_expr();
				}
				break;
			case INTEGER_LITERAL:
			case HEX_LITERAL:
			case FLOATING_PT_LITERAL:
			case FIXED_PT_LITERAL:
			case WIDE_CHARACTER_LITERAL:
			case CHARACTER_LITERAL:
			case WIDE_STRING_LITERAL:
			case STRING_LITERAL:
			case BOOLEAN_LITERAL:
			case LEFT_BRACKET:
			case DOUBLE_COLON:
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(586); primary_expr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Unary_operatorContext extends ParserRuleContext {
		public TerminalNode MINUS() { return getToken(IDLParser.MINUS, 0); }
		public TerminalNode PLUS() { return getToken(IDLParser.PLUS, 0); }
		public TerminalNode TILDE() { return getToken(IDLParser.TILDE, 0); }
		public Unary_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterUnary_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitUnary_operator(this);
		}
	}

	public final Unary_operatorContext unary_operator() throws RecognitionException {
		Unary_operatorContext _localctx = new Unary_operatorContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_unary_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(589);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TILDE) | (1L << PLUS) | (1L << MINUS))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Primary_exprContext extends ParserRuleContext {
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public TerminalNode LEFT_BRACKET() { return getToken(IDLParser.LEFT_BRACKET, 0); }
		public TerminalNode RIGHT_BRACKET() { return getToken(IDLParser.RIGHT_BRACKET, 0); }
		public Const_expContext const_exp() {
			return getRuleContext(Const_expContext.class,0);
		}
		public Primary_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterPrimary_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitPrimary_expr(this);
		}
	}

	public final Primary_exprContext primary_expr() throws RecognitionException {
		Primary_exprContext _localctx = new Primary_exprContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_primary_expr);
		try {
			setState(597);
			switch (_input.LA(1)) {
			case DOUBLE_COLON:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(591); scoped_name();
				}
				break;
			case INTEGER_LITERAL:
			case HEX_LITERAL:
			case FLOATING_PT_LITERAL:
			case FIXED_PT_LITERAL:
			case WIDE_CHARACTER_LITERAL:
			case CHARACTER_LITERAL:
			case WIDE_STRING_LITERAL:
			case STRING_LITERAL:
			case BOOLEAN_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(592); literal();
				}
				break;
			case LEFT_BRACKET:
				enterOuterAlt(_localctx, 3);
				{
				setState(593); match(LEFT_BRACKET);
				setState(594); const_exp();
				setState(595); match(RIGHT_BRACKET);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode FLOATING_PT_LITERAL() { return getToken(IDLParser.FLOATING_PT_LITERAL, 0); }
		public TerminalNode INTEGER_LITERAL() { return getToken(IDLParser.INTEGER_LITERAL, 0); }
		public TerminalNode WIDE_CHARACTER_LITERAL() { return getToken(IDLParser.WIDE_CHARACTER_LITERAL, 0); }
		public TerminalNode FIXED_PT_LITERAL() { return getToken(IDLParser.FIXED_PT_LITERAL, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(IDLParser.STRING_LITERAL, 0); }
		public TerminalNode HEX_LITERAL() { return getToken(IDLParser.HEX_LITERAL, 0); }
		public TerminalNode CHARACTER_LITERAL() { return getToken(IDLParser.CHARACTER_LITERAL, 0); }
		public TerminalNode BOOLEAN_LITERAL() { return getToken(IDLParser.BOOLEAN_LITERAL, 0); }
		public TerminalNode WIDE_STRING_LITERAL() { return getToken(IDLParser.WIDE_STRING_LITERAL, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitLiteral(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(599);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INTEGER_LITERAL) | (1L << HEX_LITERAL) | (1L << FLOATING_PT_LITERAL) | (1L << FIXED_PT_LITERAL) | (1L << WIDE_CHARACTER_LITERAL) | (1L << CHARACTER_LITERAL) | (1L << WIDE_STRING_LITERAL) | (1L << STRING_LITERAL) | (1L << BOOLEAN_LITERAL))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Positive_int_constContext extends ParserRuleContext {
		public Const_expContext const_exp() {
			return getRuleContext(Const_expContext.class,0);
		}
		public Positive_int_constContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_positive_int_const; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterPositive_int_const(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitPositive_int_const(this);
		}
	}

	public final Positive_int_constContext positive_int_const() throws RecognitionException {
		Positive_int_constContext _localctx = new Positive_int_constContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_positive_int_const);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(601); const_exp();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Type_declContext extends ParserRuleContext {
		public TerminalNode KW_NATIVE() { return getToken(IDLParser.KW_NATIVE, 0); }
		public Union_typeContext union_type() {
			return getRuleContext(Union_typeContext.class,0);
		}
		public Type_declaratorContext type_declarator() {
			return getRuleContext(Type_declaratorContext.class,0);
		}
		public Enum_typeContext enum_type() {
			return getRuleContext(Enum_typeContext.class,0);
		}
		public Simple_declaratorContext simple_declarator() {
			return getRuleContext(Simple_declaratorContext.class,0);
		}
		public Struct_typeContext struct_type() {
			return getRuleContext(Struct_typeContext.class,0);
		}
		public Constr_forward_declContext constr_forward_decl() {
			return getRuleContext(Constr_forward_declContext.class,0);
		}
		public TerminalNode KW_TYPEDEF() { return getToken(IDLParser.KW_TYPEDEF, 0); }
		public Type_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterType_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitType_decl(this);
		}
	}

	public final Type_declContext type_decl() throws RecognitionException {
		Type_declContext _localctx = new Type_declContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_type_decl);
		try {
			setState(611);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(603); match(KW_TYPEDEF);
				setState(604); type_declarator();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(605); struct_type();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(606); union_type();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(607); enum_type();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(608); match(KW_NATIVE);
				setState(609); simple_declarator();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(610); constr_forward_decl();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Type_declaratorContext extends ParserRuleContext {
		public Type_specContext type_spec() {
			return getRuleContext(Type_specContext.class,0);
		}
		public DeclaratorsContext declarators() {
			return getRuleContext(DeclaratorsContext.class,0);
		}
		public Type_declaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_declarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterType_declarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitType_declarator(this);
		}
	}

	public final Type_declaratorContext type_declarator() throws RecognitionException {
		Type_declaratorContext _localctx = new Type_declaratorContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_type_declarator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(613); type_spec();
			setState(614); declarators();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Type_specContext extends ParserRuleContext {
		public Simple_type_specContext simple_type_spec() {
			return getRuleContext(Simple_type_specContext.class,0);
		}
		public Constr_type_specContext constr_type_spec() {
			return getRuleContext(Constr_type_specContext.class,0);
		}
		public Type_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterType_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitType_spec(this);
		}
	}

	public final Type_specContext type_spec() throws RecognitionException {
		Type_specContext _localctx = new Type_specContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_type_spec);
		try {
			setState(618);
			switch (_input.LA(1)) {
			case DOUBLE_COLON:
			case KW_STRING:
			case KW_OCTET:
			case KW_SEQUENCE:
			case KW_WCHAR:
			case KW_SHORT:
			case KW_LONG:
			case KW_WSTRING:
			case KW_VALUEBASE:
			case KW_OBJECT:
			case KW_UNSIGNED:
			case KW_FIXED:
			case KW_ANY:
			case KW_CHAR:
			case KW_FLOAT:
			case KW_BOOLEAN:
			case KW_DOUBLE:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(616); simple_type_spec();
				}
				break;
			case KW_STRUCT:
			case KW_ENUM:
			case KW_UNION:
				enterOuterAlt(_localctx, 2);
				{
				setState(617); constr_type_spec();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Simple_type_specContext extends ParserRuleContext {
		public Base_type_specContext base_type_spec() {
			return getRuleContext(Base_type_specContext.class,0);
		}
		public Template_type_specContext template_type_spec() {
			return getRuleContext(Template_type_specContext.class,0);
		}
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public Simple_type_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_type_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterSimple_type_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitSimple_type_spec(this);
		}
	}

	public final Simple_type_specContext simple_type_spec() throws RecognitionException {
		Simple_type_specContext _localctx = new Simple_type_specContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_simple_type_spec);
		try {
			setState(623);
			switch (_input.LA(1)) {
			case KW_OCTET:
			case KW_WCHAR:
			case KW_SHORT:
			case KW_LONG:
			case KW_VALUEBASE:
			case KW_OBJECT:
			case KW_UNSIGNED:
			case KW_ANY:
			case KW_CHAR:
			case KW_FLOAT:
			case KW_BOOLEAN:
			case KW_DOUBLE:
				enterOuterAlt(_localctx, 1);
				{
				setState(620); base_type_spec();
				}
				break;
			case KW_STRING:
			case KW_SEQUENCE:
			case KW_WSTRING:
			case KW_FIXED:
				enterOuterAlt(_localctx, 2);
				{
				setState(621); template_type_spec();
				}
				break;
			case DOUBLE_COLON:
			case ID:
				enterOuterAlt(_localctx, 3);
				{
				setState(622); scoped_name();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Base_type_specContext extends ParserRuleContext {
		public Octet_typeContext octet_type() {
			return getRuleContext(Octet_typeContext.class,0);
		}
		public Value_base_typeContext value_base_type() {
			return getRuleContext(Value_base_typeContext.class,0);
		}
		public Boolean_typeContext boolean_type() {
			return getRuleContext(Boolean_typeContext.class,0);
		}
		public Wide_char_typeContext wide_char_type() {
			return getRuleContext(Wide_char_typeContext.class,0);
		}
		public Any_typeContext any_type() {
			return getRuleContext(Any_typeContext.class,0);
		}
		public Integer_typeContext integer_type() {
			return getRuleContext(Integer_typeContext.class,0);
		}
		public Char_typeContext char_type() {
			return getRuleContext(Char_typeContext.class,0);
		}
		public Floating_pt_typeContext floating_pt_type() {
			return getRuleContext(Floating_pt_typeContext.class,0);
		}
		public Object_typeContext object_type() {
			return getRuleContext(Object_typeContext.class,0);
		}
		public Base_type_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_base_type_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterBase_type_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitBase_type_spec(this);
		}
	}

	public final Base_type_specContext base_type_spec() throws RecognitionException {
		Base_type_specContext _localctx = new Base_type_specContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_base_type_spec);
		try {
			setState(634);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(625); floating_pt_type();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(626); integer_type();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(627); char_type();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(628); wide_char_type();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(629); boolean_type();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(630); octet_type();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(631); any_type();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(632); object_type();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(633); value_base_type();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Template_type_specContext extends ParserRuleContext {
		public Sequence_typeContext sequence_type() {
			return getRuleContext(Sequence_typeContext.class,0);
		}
		public String_typeContext string_type() {
			return getRuleContext(String_typeContext.class,0);
		}
		public Fixed_pt_typeContext fixed_pt_type() {
			return getRuleContext(Fixed_pt_typeContext.class,0);
		}
		public Wide_string_typeContext wide_string_type() {
			return getRuleContext(Wide_string_typeContext.class,0);
		}
		public Template_type_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_template_type_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterTemplate_type_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitTemplate_type_spec(this);
		}
	}

	public final Template_type_specContext template_type_spec() throws RecognitionException {
		Template_type_specContext _localctx = new Template_type_specContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_template_type_spec);
		try {
			setState(640);
			switch (_input.LA(1)) {
			case KW_SEQUENCE:
				enterOuterAlt(_localctx, 1);
				{
				setState(636); sequence_type();
				}
				break;
			case KW_STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(637); string_type();
				}
				break;
			case KW_WSTRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(638); wide_string_type();
				}
				break;
			case KW_FIXED:
				enterOuterAlt(_localctx, 4);
				{
				setState(639); fixed_pt_type();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Constr_type_specContext extends ParserRuleContext {
		public Union_typeContext union_type() {
			return getRuleContext(Union_typeContext.class,0);
		}
		public Enum_typeContext enum_type() {
			return getRuleContext(Enum_typeContext.class,0);
		}
		public Struct_typeContext struct_type() {
			return getRuleContext(Struct_typeContext.class,0);
		}
		public Constr_type_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constr_type_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterConstr_type_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitConstr_type_spec(this);
		}
	}

	public final Constr_type_specContext constr_type_spec() throws RecognitionException {
		Constr_type_specContext _localctx = new Constr_type_specContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_constr_type_spec);
		try {
			setState(645);
			switch (_input.LA(1)) {
			case KW_STRUCT:
				enterOuterAlt(_localctx, 1);
				{
				setState(642); struct_type();
				}
				break;
			case KW_UNION:
				enterOuterAlt(_localctx, 2);
				{
				setState(643); union_type();
				}
				break;
			case KW_ENUM:
				enterOuterAlt(_localctx, 3);
				{
				setState(644); enum_type();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclaratorsContext extends ParserRuleContext {
		public List<DeclaratorContext> declarator() {
			return getRuleContexts(DeclaratorContext.class);
		}
		public DeclaratorContext declarator(int i) {
			return getRuleContext(DeclaratorContext.class,i);
		}
		public TerminalNode COMA(int i) {
			return getToken(IDLParser.COMA, i);
		}
		public List<TerminalNode> COMA() { return getTokens(IDLParser.COMA); }
		public DeclaratorsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarators; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterDeclarators(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitDeclarators(this);
		}
	}

	public final DeclaratorsContext declarators() throws RecognitionException {
		DeclaratorsContext _localctx = new DeclaratorsContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_declarators);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(647); declarator();
			setState(652);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(648); match(COMA);
				setState(649); declarator();
				}
				}
				setState(654);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclaratorContext extends ParserRuleContext {
		public Complex_declaratorContext complex_declarator() {
			return getRuleContext(Complex_declaratorContext.class,0);
		}
		public Simple_declaratorContext simple_declarator() {
			return getRuleContext(Simple_declaratorContext.class,0);
		}
		public DeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitDeclarator(this);
		}
	}

	public final DeclaratorContext declarator() throws RecognitionException {
		DeclaratorContext _localctx = new DeclaratorContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_declarator);
		try {
			setState(657);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(655); simple_declarator();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(656); complex_declarator();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Simple_declaratorContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public Simple_declaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_declarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterSimple_declarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitSimple_declarator(this);
		}
	}

	public final Simple_declaratorContext simple_declarator() throws RecognitionException {
		Simple_declaratorContext _localctx = new Simple_declaratorContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_simple_declarator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(659); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Complex_declaratorContext extends ParserRuleContext {
		public Array_declaratorContext array_declarator() {
			return getRuleContext(Array_declaratorContext.class,0);
		}
		public Complex_declaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_complex_declarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterComplex_declarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitComplex_declarator(this);
		}
	}

	public final Complex_declaratorContext complex_declarator() throws RecognitionException {
		Complex_declaratorContext _localctx = new Complex_declaratorContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_complex_declarator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(661); array_declarator();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Floating_pt_typeContext extends ParserRuleContext {
		public TerminalNode KW_LONG() { return getToken(IDLParser.KW_LONG, 0); }
		public TerminalNode KW_DOUBLE() { return getToken(IDLParser.KW_DOUBLE, 0); }
		public TerminalNode KW_FLOAT() { return getToken(IDLParser.KW_FLOAT, 0); }
		public Floating_pt_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_floating_pt_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterFloating_pt_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitFloating_pt_type(this);
		}
	}

	public final Floating_pt_typeContext floating_pt_type() throws RecognitionException {
		Floating_pt_typeContext _localctx = new Floating_pt_typeContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_floating_pt_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(667);
			switch (_input.LA(1)) {
			case KW_FLOAT:
				{
				setState(663); match(KW_FLOAT);
				}
				break;
			case KW_DOUBLE:
				{
				setState(664); match(KW_DOUBLE);
				}
				break;
			case KW_LONG:
				{
				setState(665); match(KW_LONG);
				setState(666); match(KW_DOUBLE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Integer_typeContext extends ParserRuleContext {
		public Unsigned_intContext unsigned_int() {
			return getRuleContext(Unsigned_intContext.class,0);
		}
		public Signed_intContext signed_int() {
			return getRuleContext(Signed_intContext.class,0);
		}
		public Integer_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integer_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterInteger_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitInteger_type(this);
		}
	}

	public final Integer_typeContext integer_type() throws RecognitionException {
		Integer_typeContext _localctx = new Integer_typeContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_integer_type);
		try {
			setState(671);
			switch (_input.LA(1)) {
			case KW_SHORT:
			case KW_LONG:
				enterOuterAlt(_localctx, 1);
				{
				setState(669); signed_int();
				}
				break;
			case KW_UNSIGNED:
				enterOuterAlt(_localctx, 2);
				{
				setState(670); unsigned_int();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Signed_intContext extends ParserRuleContext {
		public Signed_long_intContext signed_long_int() {
			return getRuleContext(Signed_long_intContext.class,0);
		}
		public Signed_longlong_intContext signed_longlong_int() {
			return getRuleContext(Signed_longlong_intContext.class,0);
		}
		public Signed_short_intContext signed_short_int() {
			return getRuleContext(Signed_short_intContext.class,0);
		}
		public Signed_intContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_signed_int; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterSigned_int(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitSigned_int(this);
		}
	}

	public final Signed_intContext signed_int() throws RecognitionException {
		Signed_intContext _localctx = new Signed_intContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_signed_int);
		try {
			setState(676);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(673); signed_short_int();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(674); signed_long_int();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(675); signed_longlong_int();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Signed_short_intContext extends ParserRuleContext {
		public TerminalNode KW_SHORT() { return getToken(IDLParser.KW_SHORT, 0); }
		public Signed_short_intContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_signed_short_int; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterSigned_short_int(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitSigned_short_int(this);
		}
	}

	public final Signed_short_intContext signed_short_int() throws RecognitionException {
		Signed_short_intContext _localctx = new Signed_short_intContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_signed_short_int);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(678); match(KW_SHORT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Signed_long_intContext extends ParserRuleContext {
		public TerminalNode KW_LONG() { return getToken(IDLParser.KW_LONG, 0); }
		public Signed_long_intContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_signed_long_int; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterSigned_long_int(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitSigned_long_int(this);
		}
	}

	public final Signed_long_intContext signed_long_int() throws RecognitionException {
		Signed_long_intContext _localctx = new Signed_long_intContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_signed_long_int);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(680); match(KW_LONG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Signed_longlong_intContext extends ParserRuleContext {
		public List<TerminalNode> KW_LONG() { return getTokens(IDLParser.KW_LONG); }
		public TerminalNode KW_LONG(int i) {
			return getToken(IDLParser.KW_LONG, i);
		}
		public Signed_longlong_intContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_signed_longlong_int; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterSigned_longlong_int(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitSigned_longlong_int(this);
		}
	}

	public final Signed_longlong_intContext signed_longlong_int() throws RecognitionException {
		Signed_longlong_intContext _localctx = new Signed_longlong_intContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_signed_longlong_int);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(682); match(KW_LONG);
			setState(683); match(KW_LONG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Unsigned_intContext extends ParserRuleContext {
		public Unsigned_longlong_intContext unsigned_longlong_int() {
			return getRuleContext(Unsigned_longlong_intContext.class,0);
		}
		public Unsigned_long_intContext unsigned_long_int() {
			return getRuleContext(Unsigned_long_intContext.class,0);
		}
		public Unsigned_short_intContext unsigned_short_int() {
			return getRuleContext(Unsigned_short_intContext.class,0);
		}
		public Unsigned_intContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unsigned_int; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterUnsigned_int(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitUnsigned_int(this);
		}
	}

	public final Unsigned_intContext unsigned_int() throws RecognitionException {
		Unsigned_intContext _localctx = new Unsigned_intContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_unsigned_int);
		try {
			setState(688);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(685); unsigned_short_int();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(686); unsigned_long_int();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(687); unsigned_longlong_int();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Unsigned_short_intContext extends ParserRuleContext {
		public TerminalNode KW_SHORT() { return getToken(IDLParser.KW_SHORT, 0); }
		public TerminalNode KW_UNSIGNED() { return getToken(IDLParser.KW_UNSIGNED, 0); }
		public Unsigned_short_intContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unsigned_short_int; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterUnsigned_short_int(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitUnsigned_short_int(this);
		}
	}

	public final Unsigned_short_intContext unsigned_short_int() throws RecognitionException {
		Unsigned_short_intContext _localctx = new Unsigned_short_intContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_unsigned_short_int);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(690); match(KW_UNSIGNED);
			setState(691); match(KW_SHORT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Unsigned_long_intContext extends ParserRuleContext {
		public TerminalNode KW_LONG() { return getToken(IDLParser.KW_LONG, 0); }
		public TerminalNode KW_UNSIGNED() { return getToken(IDLParser.KW_UNSIGNED, 0); }
		public Unsigned_long_intContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unsigned_long_int; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterUnsigned_long_int(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitUnsigned_long_int(this);
		}
	}

	public final Unsigned_long_intContext unsigned_long_int() throws RecognitionException {
		Unsigned_long_intContext _localctx = new Unsigned_long_intContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_unsigned_long_int);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(693); match(KW_UNSIGNED);
			setState(694); match(KW_LONG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Unsigned_longlong_intContext extends ParserRuleContext {
		public List<TerminalNode> KW_LONG() { return getTokens(IDLParser.KW_LONG); }
		public TerminalNode KW_LONG(int i) {
			return getToken(IDLParser.KW_LONG, i);
		}
		public TerminalNode KW_UNSIGNED() { return getToken(IDLParser.KW_UNSIGNED, 0); }
		public Unsigned_longlong_intContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unsigned_longlong_int; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterUnsigned_longlong_int(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitUnsigned_longlong_int(this);
		}
	}

	public final Unsigned_longlong_intContext unsigned_longlong_int() throws RecognitionException {
		Unsigned_longlong_intContext _localctx = new Unsigned_longlong_intContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_unsigned_longlong_int);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(696); match(KW_UNSIGNED);
			setState(697); match(KW_LONG);
			setState(698); match(KW_LONG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Char_typeContext extends ParserRuleContext {
		public TerminalNode KW_CHAR() { return getToken(IDLParser.KW_CHAR, 0); }
		public Char_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_char_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterChar_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitChar_type(this);
		}
	}

	public final Char_typeContext char_type() throws RecognitionException {
		Char_typeContext _localctx = new Char_typeContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_char_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(700); match(KW_CHAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Wide_char_typeContext extends ParserRuleContext {
		public TerminalNode KW_WCHAR() { return getToken(IDLParser.KW_WCHAR, 0); }
		public Wide_char_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wide_char_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterWide_char_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitWide_char_type(this);
		}
	}

	public final Wide_char_typeContext wide_char_type() throws RecognitionException {
		Wide_char_typeContext _localctx = new Wide_char_typeContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_wide_char_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(702); match(KW_WCHAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Boolean_typeContext extends ParserRuleContext {
		public TerminalNode KW_BOOLEAN() { return getToken(IDLParser.KW_BOOLEAN, 0); }
		public Boolean_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boolean_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterBoolean_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitBoolean_type(this);
		}
	}

	public final Boolean_typeContext boolean_type() throws RecognitionException {
		Boolean_typeContext _localctx = new Boolean_typeContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_boolean_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(704); match(KW_BOOLEAN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Octet_typeContext extends ParserRuleContext {
		public TerminalNode KW_OCTET() { return getToken(IDLParser.KW_OCTET, 0); }
		public Octet_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_octet_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterOctet_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitOctet_type(this);
		}
	}

	public final Octet_typeContext octet_type() throws RecognitionException {
		Octet_typeContext _localctx = new Octet_typeContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_octet_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(706); match(KW_OCTET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Any_typeContext extends ParserRuleContext {
		public TerminalNode KW_ANY() { return getToken(IDLParser.KW_ANY, 0); }
		public Any_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_any_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterAny_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitAny_type(this);
		}
	}

	public final Any_typeContext any_type() throws RecognitionException {
		Any_typeContext _localctx = new Any_typeContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_any_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(708); match(KW_ANY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Object_typeContext extends ParserRuleContext {
		public TerminalNode KW_OBJECT() { return getToken(IDLParser.KW_OBJECT, 0); }
		public Object_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_object_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterObject_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitObject_type(this);
		}
	}

	public final Object_typeContext object_type() throws RecognitionException {
		Object_typeContext _localctx = new Object_typeContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_object_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(710); match(KW_OBJECT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Struct_typeContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public Member_listContext member_list() {
			return getRuleContext(Member_listContext.class,0);
		}
		public TerminalNode KW_STRUCT() { return getToken(IDLParser.KW_STRUCT, 0); }
		public TerminalNode LEFT_BRACE() { return getToken(IDLParser.LEFT_BRACE, 0); }
		public TerminalNode RIGHT_BRACE() { return getToken(IDLParser.RIGHT_BRACE, 0); }
		public Struct_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_struct_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterStruct_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitStruct_type(this);
		}
	}

	public final Struct_typeContext struct_type() throws RecognitionException {
		Struct_typeContext _localctx = new Struct_typeContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_struct_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(712); match(KW_STRUCT);
			setState(713); match(ID);
			setState(714); match(LEFT_BRACE);
			setState(715); member_list();
			setState(716); match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Member_listContext extends ParserRuleContext {
		public MemberContext member(int i) {
			return getRuleContext(MemberContext.class,i);
		}
		public List<MemberContext> member() {
			return getRuleContexts(MemberContext.class);
		}
		public Member_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_member_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterMember_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitMember_list(this);
		}
	}

	public final Member_listContext member_list() throws RecognitionException {
		Member_listContext _localctx = new Member_listContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_member_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(719); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(718); member();
				}
				}
				setState(721); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE_COLON) | (1L << KW_STRING) | (1L << KW_OCTET) | (1L << KW_SEQUENCE) | (1L << KW_STRUCT) | (1L << KW_WCHAR) | (1L << KW_SHORT) | (1L << KW_LONG) | (1L << KW_ENUM) | (1L << KW_WSTRING))) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & ((1L << (KW_VALUEBASE - 70)) | (1L << (KW_OBJECT - 70)) | (1L << (KW_UNSIGNED - 70)) | (1L << (KW_FIXED - 70)) | (1L << (KW_UNION - 70)) | (1L << (KW_ANY - 70)) | (1L << (KW_CHAR - 70)) | (1L << (KW_FLOAT - 70)) | (1L << (KW_BOOLEAN - 70)) | (1L << (KW_DOUBLE - 70)) | (1L << (ID - 70)))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MemberContext extends ParserRuleContext {
		public TerminalNode SEMICOLON() { return getToken(IDLParser.SEMICOLON, 0); }
		public Type_specContext type_spec() {
			return getRuleContext(Type_specContext.class,0);
		}
		public DeclaratorsContext declarators() {
			return getRuleContext(DeclaratorsContext.class,0);
		}
		public MemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_member; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitMember(this);
		}
	}

	public final MemberContext member() throws RecognitionException {
		MemberContext _localctx = new MemberContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_member);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(723); type_spec();
			setState(724); declarators();
			setState(725); match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeymarkerContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public List<TerminalNode> SLASH() { return getTokens(IDLParser.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(IDLParser.SLASH, i);
		}
		public KeymarkerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keymarker; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterKeymarker(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitKeymarker(this);
		}
	}

	public final KeymarkerContext keymarker() throws RecognitionException {
		KeymarkerContext _localctx = new KeymarkerContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_keymarker);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(727); match(SLASH);
			setState(728); match(SLASH);
			setState(729); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Union_typeContext extends ParserRuleContext {
		public TerminalNode KW_UNION() { return getToken(IDLParser.KW_UNION, 0); }
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public Switch_bodyContext switch_body() {
			return getRuleContext(Switch_bodyContext.class,0);
		}
		public TerminalNode LEFT_BRACKET() { return getToken(IDLParser.LEFT_BRACKET, 0); }
		public TerminalNode RIGHT_BRACKET() { return getToken(IDLParser.RIGHT_BRACKET, 0); }
		public TerminalNode LEFT_BRACE() { return getToken(IDLParser.LEFT_BRACE, 0); }
		public TerminalNode KW_SWITCH() { return getToken(IDLParser.KW_SWITCH, 0); }
		public TerminalNode RIGHT_BRACE() { return getToken(IDLParser.RIGHT_BRACE, 0); }
		public Switch_type_specContext switch_type_spec() {
			return getRuleContext(Switch_type_specContext.class,0);
		}
		public Union_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_union_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterUnion_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitUnion_type(this);
		}
	}

	public final Union_typeContext union_type() throws RecognitionException {
		Union_typeContext _localctx = new Union_typeContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_union_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(731); match(KW_UNION);
			setState(732); match(ID);
			setState(733); match(KW_SWITCH);
			setState(734); match(LEFT_BRACKET);
			setState(735); switch_type_spec();
			setState(736); match(RIGHT_BRACKET);
			setState(737); match(LEFT_BRACE);
			setState(738); switch_body();
			setState(739); match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Switch_type_specContext extends ParserRuleContext {
		public Boolean_typeContext boolean_type() {
			return getRuleContext(Boolean_typeContext.class,0);
		}
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public Enum_typeContext enum_type() {
			return getRuleContext(Enum_typeContext.class,0);
		}
		public Integer_typeContext integer_type() {
			return getRuleContext(Integer_typeContext.class,0);
		}
		public Char_typeContext char_type() {
			return getRuleContext(Char_typeContext.class,0);
		}
		public Switch_type_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switch_type_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterSwitch_type_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitSwitch_type_spec(this);
		}
	}

	public final Switch_type_specContext switch_type_spec() throws RecognitionException {
		Switch_type_specContext _localctx = new Switch_type_specContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_switch_type_spec);
		try {
			setState(746);
			switch (_input.LA(1)) {
			case KW_SHORT:
			case KW_LONG:
			case KW_UNSIGNED:
				enterOuterAlt(_localctx, 1);
				{
				setState(741); integer_type();
				}
				break;
			case KW_CHAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(742); char_type();
				}
				break;
			case KW_BOOLEAN:
				enterOuterAlt(_localctx, 3);
				{
				setState(743); boolean_type();
				}
				break;
			case KW_ENUM:
				enterOuterAlt(_localctx, 4);
				{
				setState(744); enum_type();
				}
				break;
			case DOUBLE_COLON:
			case ID:
				enterOuterAlt(_localctx, 5);
				{
				setState(745); scoped_name();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Switch_bodyContext extends ParserRuleContext {
		public Case_stmtContext case_stmt(int i) {
			return getRuleContext(Case_stmtContext.class,i);
		}
		public List<Case_stmtContext> case_stmt() {
			return getRuleContexts(Case_stmtContext.class);
		}
		public Switch_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switch_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterSwitch_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitSwitch_body(this);
		}
	}

	public final Switch_bodyContext switch_body() throws RecognitionException {
		Switch_bodyContext _localctx = new Switch_bodyContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_switch_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(749); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(748); case_stmt();
				}
				}
				setState(751); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==KW_DEFAULT || _la==KW_CASE );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Case_stmtContext extends ParserRuleContext {
		public Element_specContext element_spec() {
			return getRuleContext(Element_specContext.class,0);
		}
		public Case_labelContext case_label(int i) {
			return getRuleContext(Case_labelContext.class,i);
		}
		public TerminalNode SEMICOLON() { return getToken(IDLParser.SEMICOLON, 0); }
		public List<Case_labelContext> case_label() {
			return getRuleContexts(Case_labelContext.class);
		}
		public Case_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_case_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterCase_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitCase_stmt(this);
		}
	}

	public final Case_stmtContext case_stmt() throws RecognitionException {
		Case_stmtContext _localctx = new Case_stmtContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_case_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(754); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(753); case_label();
				}
				}
				setState(756); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==KW_DEFAULT || _la==KW_CASE );
			setState(758); element_spec();
			setState(759); match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Case_labelContext extends ParserRuleContext {
		public TerminalNode KW_CASE() { return getToken(IDLParser.KW_CASE, 0); }
		public TerminalNode COLON() { return getToken(IDLParser.COLON, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(IDLParser.KW_DEFAULT, 0); }
		public Const_expContext const_exp() {
			return getRuleContext(Const_expContext.class,0);
		}
		public Case_labelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_case_label; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterCase_label(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitCase_label(this);
		}
	}

	public final Case_labelContext case_label() throws RecognitionException {
		Case_labelContext _localctx = new Case_labelContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_case_label);
		try {
			setState(767);
			switch (_input.LA(1)) {
			case KW_CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(761); match(KW_CASE);
				setState(762); const_exp();
				setState(763); match(COLON);
				}
				break;
			case KW_DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(765); match(KW_DEFAULT);
				setState(766); match(COLON);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Element_specContext extends ParserRuleContext {
		public DeclaratorContext declarator() {
			return getRuleContext(DeclaratorContext.class,0);
		}
		public Type_specContext type_spec() {
			return getRuleContext(Type_specContext.class,0);
		}
		public Element_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_element_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterElement_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitElement_spec(this);
		}
	}

	public final Element_specContext element_spec() throws RecognitionException {
		Element_specContext _localctx = new Element_specContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_element_spec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(769); type_spec();
			setState(770); declarator();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Enum_typeContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public List<EnumeratorContext> enumerator() {
			return getRuleContexts(EnumeratorContext.class);
		}
		public TerminalNode COMA(int i) {
			return getToken(IDLParser.COMA, i);
		}
		public TerminalNode LEFT_BRACE() { return getToken(IDLParser.LEFT_BRACE, 0); }
		public TerminalNode KW_ENUM() { return getToken(IDLParser.KW_ENUM, 0); }
		public TerminalNode RIGHT_BRACE() { return getToken(IDLParser.RIGHT_BRACE, 0); }
		public List<TerminalNode> COMA() { return getTokens(IDLParser.COMA); }
		public EnumeratorContext enumerator(int i) {
			return getRuleContext(EnumeratorContext.class,i);
		}
		public Enum_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enum_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterEnum_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitEnum_type(this);
		}
	}

	public final Enum_typeContext enum_type() throws RecognitionException {
		Enum_typeContext _localctx = new Enum_typeContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_enum_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(772); match(KW_ENUM);
			setState(773); match(ID);
			setState(774); match(LEFT_BRACE);
			setState(775); enumerator();
			setState(780);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(776); match(COMA);
				setState(777); enumerator();
				}
				}
				setState(782);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(783); match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumeratorContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public EnumeratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumerator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterEnumerator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitEnumerator(this);
		}
	}

	public final EnumeratorContext enumerator() throws RecognitionException {
		EnumeratorContext _localctx = new EnumeratorContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_enumerator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(785); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Sequence_typeContext extends ParserRuleContext {
		public Positive_int_constContext positive_int_const() {
			return getRuleContext(Positive_int_constContext.class,0);
		}
		public Simple_type_specContext simple_type_spec() {
			return getRuleContext(Simple_type_specContext.class,0);
		}
		public TerminalNode RIGHT_ANG_BRACKET() { return getToken(IDLParser.RIGHT_ANG_BRACKET, 0); }
		public TerminalNode LEFT_ANG_BRACKET() { return getToken(IDLParser.LEFT_ANG_BRACKET, 0); }
		public TerminalNode COMA() { return getToken(IDLParser.COMA, 0); }
		public TerminalNode KW_SEQUENCE() { return getToken(IDLParser.KW_SEQUENCE, 0); }
		public Sequence_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sequence_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterSequence_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitSequence_type(this);
		}
	}

	public final Sequence_typeContext sequence_type() throws RecognitionException {
		Sequence_typeContext _localctx = new Sequence_typeContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_sequence_type);
		try {
			setState(799);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(787); match(KW_SEQUENCE);
				setState(788); match(LEFT_ANG_BRACKET);
				setState(789); simple_type_spec();
				setState(790); match(COMA);
				setState(791); positive_int_const();
				setState(792); match(RIGHT_ANG_BRACKET);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(794); match(KW_SEQUENCE);
				setState(795); match(LEFT_ANG_BRACKET);
				setState(796); simple_type_spec();
				setState(797); match(RIGHT_ANG_BRACKET);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class String_typeContext extends ParserRuleContext {
		public Positive_int_constContext positive_int_const() {
			return getRuleContext(Positive_int_constContext.class,0);
		}
		public TerminalNode KW_STRING() { return getToken(IDLParser.KW_STRING, 0); }
		public TerminalNode RIGHT_ANG_BRACKET() { return getToken(IDLParser.RIGHT_ANG_BRACKET, 0); }
		public TerminalNode LEFT_ANG_BRACKET() { return getToken(IDLParser.LEFT_ANG_BRACKET, 0); }
		public String_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterString_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitString_type(this);
		}
	}

	public final String_typeContext string_type() throws RecognitionException {
		String_typeContext _localctx = new String_typeContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_string_type);
		try {
			setState(807);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(801); match(KW_STRING);
				setState(802); match(LEFT_ANG_BRACKET);
				setState(803); positive_int_const();
				setState(804); match(RIGHT_ANG_BRACKET);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(806); match(KW_STRING);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Wide_string_typeContext extends ParserRuleContext {
		public Positive_int_constContext positive_int_const() {
			return getRuleContext(Positive_int_constContext.class,0);
		}
		public TerminalNode KW_WSTRING() { return getToken(IDLParser.KW_WSTRING, 0); }
		public TerminalNode RIGHT_ANG_BRACKET() { return getToken(IDLParser.RIGHT_ANG_BRACKET, 0); }
		public TerminalNode LEFT_ANG_BRACKET() { return getToken(IDLParser.LEFT_ANG_BRACKET, 0); }
		public Wide_string_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wide_string_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterWide_string_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitWide_string_type(this);
		}
	}

	public final Wide_string_typeContext wide_string_type() throws RecognitionException {
		Wide_string_typeContext _localctx = new Wide_string_typeContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_wide_string_type);
		try {
			setState(815);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(809); match(KW_WSTRING);
				setState(810); match(LEFT_ANG_BRACKET);
				setState(811); positive_int_const();
				setState(812); match(RIGHT_ANG_BRACKET);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(814); match(KW_WSTRING);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Array_declaratorContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public Fixed_array_sizeContext fixed_array_size(int i) {
			return getRuleContext(Fixed_array_sizeContext.class,i);
		}
		public List<Fixed_array_sizeContext> fixed_array_size() {
			return getRuleContexts(Fixed_array_sizeContext.class);
		}
		public Array_declaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_declarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterArray_declarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitArray_declarator(this);
		}
	}

	public final Array_declaratorContext array_declarator() throws RecognitionException {
		Array_declaratorContext _localctx = new Array_declaratorContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_array_declarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(817); match(ID);
			setState(819); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(818); fixed_array_size();
				}
				}
				setState(821); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==LEFT_SQUARE_BRACKET );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Fixed_array_sizeContext extends ParserRuleContext {
		public Positive_int_constContext positive_int_const() {
			return getRuleContext(Positive_int_constContext.class,0);
		}
		public TerminalNode RIGHT_SQUARE_BRACKET() { return getToken(IDLParser.RIGHT_SQUARE_BRACKET, 0); }
		public TerminalNode LEFT_SQUARE_BRACKET() { return getToken(IDLParser.LEFT_SQUARE_BRACKET, 0); }
		public Fixed_array_sizeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fixed_array_size; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterFixed_array_size(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitFixed_array_size(this);
		}
	}

	public final Fixed_array_sizeContext fixed_array_size() throws RecognitionException {
		Fixed_array_sizeContext _localctx = new Fixed_array_sizeContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_fixed_array_size);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(823); match(LEFT_SQUARE_BRACKET);
			setState(824); positive_int_const();
			setState(825); match(RIGHT_SQUARE_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Attr_declContext extends ParserRuleContext {
		public Readonly_attr_specContext readonly_attr_spec() {
			return getRuleContext(Readonly_attr_specContext.class,0);
		}
		public Attr_specContext attr_spec() {
			return getRuleContext(Attr_specContext.class,0);
		}
		public Attr_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attr_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterAttr_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitAttr_decl(this);
		}
	}

	public final Attr_declContext attr_decl() throws RecognitionException {
		Attr_declContext _localctx = new Attr_declContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_attr_decl);
		try {
			setState(829);
			switch (_input.LA(1)) {
			case KW_READONLY:
				enterOuterAlt(_localctx, 1);
				{
				setState(827); readonly_attr_spec();
				}
				break;
			case KW_ATTRIBUTE:
				enterOuterAlt(_localctx, 2);
				{
				setState(828); attr_spec();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Except_declContext extends ParserRuleContext {
		public TerminalNode KW_EXCEPTION() { return getToken(IDLParser.KW_EXCEPTION, 0); }
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public MemberContext member(int i) {
			return getRuleContext(MemberContext.class,i);
		}
		public TerminalNode LEFT_BRACE() { return getToken(IDLParser.LEFT_BRACE, 0); }
		public List<MemberContext> member() {
			return getRuleContexts(MemberContext.class);
		}
		public TerminalNode RIGHT_BRACE() { return getToken(IDLParser.RIGHT_BRACE, 0); }
		public Except_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_except_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterExcept_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitExcept_decl(this);
		}
	}

	public final Except_declContext except_decl() throws RecognitionException {
		Except_declContext _localctx = new Except_declContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_except_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(831); match(KW_EXCEPTION);
			setState(832); match(ID);
			setState(833); match(LEFT_BRACE);
			setState(837);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE_COLON) | (1L << KW_STRING) | (1L << KW_OCTET) | (1L << KW_SEQUENCE) | (1L << KW_STRUCT) | (1L << KW_WCHAR) | (1L << KW_SHORT) | (1L << KW_LONG) | (1L << KW_ENUM) | (1L << KW_WSTRING))) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & ((1L << (KW_VALUEBASE - 70)) | (1L << (KW_OBJECT - 70)) | (1L << (KW_UNSIGNED - 70)) | (1L << (KW_FIXED - 70)) | (1L << (KW_UNION - 70)) | (1L << (KW_ANY - 70)) | (1L << (KW_CHAR - 70)) | (1L << (KW_FLOAT - 70)) | (1L << (KW_BOOLEAN - 70)) | (1L << (KW_DOUBLE - 70)) | (1L << (ID - 70)))) != 0)) {
				{
				{
				setState(834); member();
				}
				}
				setState(839);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(840); match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Op_declContext extends ParserRuleContext {
		public Op_type_specContext op_type_spec() {
			return getRuleContext(Op_type_specContext.class,0);
		}
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public Context_exprContext context_expr() {
			return getRuleContext(Context_exprContext.class,0);
		}
		public Raises_exprContext raises_expr() {
			return getRuleContext(Raises_exprContext.class,0);
		}
		public Parameter_declsContext parameter_decls() {
			return getRuleContext(Parameter_declsContext.class,0);
		}
		public Op_attributeContext op_attribute() {
			return getRuleContext(Op_attributeContext.class,0);
		}
		public Op_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterOp_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitOp_decl(this);
		}
	}

	public final Op_declContext op_decl() throws RecognitionException {
		Op_declContext _localctx = new Op_declContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_op_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(843);
			_la = _input.LA(1);
			if (_la==KW_ONEWAY) {
				{
				setState(842); op_attribute();
				}
			}

			setState(845); op_type_spec();
			setState(846); match(ID);
			setState(847); parameter_decls();
			setState(849);
			_la = _input.LA(1);
			if (_la==KW_RAISES) {
				{
				setState(848); raises_expr();
				}
			}

			setState(852);
			_la = _input.LA(1);
			if (_la==KW_CONTEXT) {
				{
				setState(851); context_expr();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Op_attributeContext extends ParserRuleContext {
		public TerminalNode KW_ONEWAY() { return getToken(IDLParser.KW_ONEWAY, 0); }
		public Op_attributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterOp_attribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitOp_attribute(this);
		}
	}

	public final Op_attributeContext op_attribute() throws RecognitionException {
		Op_attributeContext _localctx = new Op_attributeContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_op_attribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(854); match(KW_ONEWAY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Op_type_specContext extends ParserRuleContext {
		public Param_type_specContext param_type_spec() {
			return getRuleContext(Param_type_specContext.class,0);
		}
		public TerminalNode KW_VOID() { return getToken(IDLParser.KW_VOID, 0); }
		public Op_type_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op_type_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterOp_type_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitOp_type_spec(this);
		}
	}

	public final Op_type_specContext op_type_spec() throws RecognitionException {
		Op_type_specContext _localctx = new Op_type_specContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_op_type_spec);
		try {
			setState(858);
			switch (_input.LA(1)) {
			case DOUBLE_COLON:
			case KW_STRING:
			case KW_OCTET:
			case KW_WCHAR:
			case KW_SHORT:
			case KW_LONG:
			case KW_WSTRING:
			case KW_VALUEBASE:
			case KW_OBJECT:
			case KW_UNSIGNED:
			case KW_ANY:
			case KW_CHAR:
			case KW_FLOAT:
			case KW_BOOLEAN:
			case KW_DOUBLE:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(856); param_type_spec();
				}
				break;
			case KW_VOID:
				enterOuterAlt(_localctx, 2);
				{
				setState(857); match(KW_VOID);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Parameter_declsContext extends ParserRuleContext {
		public Param_declContext param_decl(int i) {
			return getRuleContext(Param_declContext.class,i);
		}
		public TerminalNode COMA(int i) {
			return getToken(IDLParser.COMA, i);
		}
		public TerminalNode LEFT_BRACKET() { return getToken(IDLParser.LEFT_BRACKET, 0); }
		public List<Param_declContext> param_decl() {
			return getRuleContexts(Param_declContext.class);
		}
		public TerminalNode RIGHT_BRACKET() { return getToken(IDLParser.RIGHT_BRACKET, 0); }
		public List<TerminalNode> COMA() { return getTokens(IDLParser.COMA); }
		public Parameter_declsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter_decls; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterParameter_decls(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitParameter_decls(this);
		}
	}

	public final Parameter_declsContext parameter_decls() throws RecognitionException {
		Parameter_declsContext _localctx = new Parameter_declsContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_parameter_decls);
		int _la;
		try {
			setState(873);
			switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(860); match(LEFT_BRACKET);
				setState(861); param_decl();
				setState(866);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMA) {
					{
					{
					setState(862); match(COMA);
					setState(863); param_decl();
					}
					}
					setState(868);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(869); match(RIGHT_BRACKET);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(871); match(LEFT_BRACKET);
				setState(872); match(RIGHT_BRACKET);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Param_declContext extends ParserRuleContext {
		public Param_type_specContext param_type_spec() {
			return getRuleContext(Param_type_specContext.class,0);
		}
		public Simple_declaratorContext simple_declarator() {
			return getRuleContext(Simple_declaratorContext.class,0);
		}
		public Param_attributeContext param_attribute() {
			return getRuleContext(Param_attributeContext.class,0);
		}
		public Param_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterParam_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitParam_decl(this);
		}
	}

	public final Param_declContext param_decl() throws RecognitionException {
		Param_declContext _localctx = new Param_declContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_param_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(875); param_attribute();
			setState(876); param_type_spec();
			setState(877); simple_declarator();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Param_attributeContext extends ParserRuleContext {
		public TerminalNode KW_INOUT() { return getToken(IDLParser.KW_INOUT, 0); }
		public TerminalNode KW_IN() { return getToken(IDLParser.KW_IN, 0); }
		public TerminalNode KW_OUT() { return getToken(IDLParser.KW_OUT, 0); }
		public Param_attributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterParam_attribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitParam_attribute(this);
		}
	}

	public final Param_attributeContext param_attribute() throws RecognitionException {
		Param_attributeContext _localctx = new Param_attributeContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_param_attribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(879);
			_la = _input.LA(1);
			if ( !(((((_la - 36)) & ~0x3f) == 0 && ((1L << (_la - 36)) & ((1L << (KW_OUT - 36)) | (1L << (KW_IN - 36)) | (1L << (KW_INOUT - 36)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Raises_exprContext extends ParserRuleContext {
		public TerminalNode KW_RAISES() { return getToken(IDLParser.KW_RAISES, 0); }
		public List<Scoped_nameContext> scoped_name() {
			return getRuleContexts(Scoped_nameContext.class);
		}
		public Scoped_nameContext scoped_name(int i) {
			return getRuleContext(Scoped_nameContext.class,i);
		}
		public TerminalNode COMA(int i) {
			return getToken(IDLParser.COMA, i);
		}
		public TerminalNode LEFT_BRACKET() { return getToken(IDLParser.LEFT_BRACKET, 0); }
		public TerminalNode RIGHT_BRACKET() { return getToken(IDLParser.RIGHT_BRACKET, 0); }
		public List<TerminalNode> COMA() { return getTokens(IDLParser.COMA); }
		public Raises_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_raises_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterRaises_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitRaises_expr(this);
		}
	}

	public final Raises_exprContext raises_expr() throws RecognitionException {
		Raises_exprContext _localctx = new Raises_exprContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_raises_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(881); match(KW_RAISES);
			setState(882); match(LEFT_BRACKET);
			setState(883); scoped_name();
			setState(888);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(884); match(COMA);
				setState(885); scoped_name();
				}
				}
				setState(890);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(891); match(RIGHT_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Context_exprContext extends ParserRuleContext {
		public List<TerminalNode> STRING_LITERAL() { return getTokens(IDLParser.STRING_LITERAL); }
		public TerminalNode COMA(int i) {
			return getToken(IDLParser.COMA, i);
		}
		public TerminalNode LEFT_BRACKET() { return getToken(IDLParser.LEFT_BRACKET, 0); }
		public TerminalNode RIGHT_BRACKET() { return getToken(IDLParser.RIGHT_BRACKET, 0); }
		public TerminalNode KW_CONTEXT() { return getToken(IDLParser.KW_CONTEXT, 0); }
		public TerminalNode STRING_LITERAL(int i) {
			return getToken(IDLParser.STRING_LITERAL, i);
		}
		public List<TerminalNode> COMA() { return getTokens(IDLParser.COMA); }
		public Context_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_context_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterContext_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitContext_expr(this);
		}
	}

	public final Context_exprContext context_expr() throws RecognitionException {
		Context_exprContext _localctx = new Context_exprContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_context_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(893); match(KW_CONTEXT);
			setState(894); match(LEFT_BRACKET);
			setState(895); match(STRING_LITERAL);
			setState(900);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(896); match(COMA);
				setState(897); match(STRING_LITERAL);
				}
				}
				setState(902);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(903); match(RIGHT_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Param_type_specContext extends ParserRuleContext {
		public String_typeContext string_type() {
			return getRuleContext(String_typeContext.class,0);
		}
		public Base_type_specContext base_type_spec() {
			return getRuleContext(Base_type_specContext.class,0);
		}
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public Wide_string_typeContext wide_string_type() {
			return getRuleContext(Wide_string_typeContext.class,0);
		}
		public Param_type_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param_type_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterParam_type_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitParam_type_spec(this);
		}
	}

	public final Param_type_specContext param_type_spec() throws RecognitionException {
		Param_type_specContext _localctx = new Param_type_specContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_param_type_spec);
		try {
			setState(909);
			switch (_input.LA(1)) {
			case KW_OCTET:
			case KW_WCHAR:
			case KW_SHORT:
			case KW_LONG:
			case KW_VALUEBASE:
			case KW_OBJECT:
			case KW_UNSIGNED:
			case KW_ANY:
			case KW_CHAR:
			case KW_FLOAT:
			case KW_BOOLEAN:
			case KW_DOUBLE:
				enterOuterAlt(_localctx, 1);
				{
				setState(905); base_type_spec();
				}
				break;
			case KW_STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(906); string_type();
				}
				break;
			case KW_WSTRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(907); wide_string_type();
				}
				break;
			case DOUBLE_COLON:
			case ID:
				enterOuterAlt(_localctx, 4);
				{
				setState(908); scoped_name();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Fixed_pt_typeContext extends ParserRuleContext {
		public List<Positive_int_constContext> positive_int_const() {
			return getRuleContexts(Positive_int_constContext.class);
		}
		public TerminalNode RIGHT_ANG_BRACKET() { return getToken(IDLParser.RIGHT_ANG_BRACKET, 0); }
		public TerminalNode LEFT_ANG_BRACKET() { return getToken(IDLParser.LEFT_ANG_BRACKET, 0); }
		public Positive_int_constContext positive_int_const(int i) {
			return getRuleContext(Positive_int_constContext.class,i);
		}
		public TerminalNode KW_FIXED() { return getToken(IDLParser.KW_FIXED, 0); }
		public TerminalNode COMA() { return getToken(IDLParser.COMA, 0); }
		public Fixed_pt_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fixed_pt_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterFixed_pt_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitFixed_pt_type(this);
		}
	}

	public final Fixed_pt_typeContext fixed_pt_type() throws RecognitionException {
		Fixed_pt_typeContext _localctx = new Fixed_pt_typeContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_fixed_pt_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(911); match(KW_FIXED);
			setState(912); match(LEFT_ANG_BRACKET);
			setState(913); positive_int_const();
			setState(914); match(COMA);
			setState(915); positive_int_const();
			setState(916); match(RIGHT_ANG_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Fixed_pt_const_typeContext extends ParserRuleContext {
		public TerminalNode KW_FIXED() { return getToken(IDLParser.KW_FIXED, 0); }
		public Fixed_pt_const_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fixed_pt_const_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterFixed_pt_const_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitFixed_pt_const_type(this);
		}
	}

	public final Fixed_pt_const_typeContext fixed_pt_const_type() throws RecognitionException {
		Fixed_pt_const_typeContext _localctx = new Fixed_pt_const_typeContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_fixed_pt_const_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(918); match(KW_FIXED);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Value_base_typeContext extends ParserRuleContext {
		public TerminalNode KW_VALUEBASE() { return getToken(IDLParser.KW_VALUEBASE, 0); }
		public Value_base_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value_base_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterValue_base_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitValue_base_type(this);
		}
	}

	public final Value_base_typeContext value_base_type() throws RecognitionException {
		Value_base_typeContext _localctx = new Value_base_typeContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_value_base_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(920); match(KW_VALUEBASE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Constr_forward_declContext extends ParserRuleContext {
		public TerminalNode KW_UNION() { return getToken(IDLParser.KW_UNION, 0); }
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public TerminalNode KW_STRUCT() { return getToken(IDLParser.KW_STRUCT, 0); }
		public Constr_forward_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constr_forward_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterConstr_forward_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitConstr_forward_decl(this);
		}
	}

	public final Constr_forward_declContext constr_forward_decl() throws RecognitionException {
		Constr_forward_declContext _localctx = new Constr_forward_declContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_constr_forward_decl);
		try {
			setState(926);
			switch (_input.LA(1)) {
			case KW_STRUCT:
				enterOuterAlt(_localctx, 1);
				{
				setState(922); match(KW_STRUCT);
				setState(923); match(ID);
				}
				break;
			case KW_UNION:
				enterOuterAlt(_localctx, 2);
				{
				setState(924); match(KW_UNION);
				setState(925); match(ID);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Import_declContext extends ParserRuleContext {
		public TerminalNode SEMICOLON() { return getToken(IDLParser.SEMICOLON, 0); }
		public TerminalNode KW_IMPORT() { return getToken(IDLParser.KW_IMPORT, 0); }
		public Imported_scopeContext imported_scope() {
			return getRuleContext(Imported_scopeContext.class,0);
		}
		public Import_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_import_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterImport_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitImport_decl(this);
		}
	}

	public final Import_declContext import_decl() throws RecognitionException {
		Import_declContext _localctx = new Import_declContext(_ctx, getState());
		enterRule(_localctx, 198, RULE_import_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(928); match(KW_IMPORT);
			setState(929); imported_scope();
			setState(930); match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Imported_scopeContext extends ParserRuleContext {
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public TerminalNode STRING_LITERAL() { return getToken(IDLParser.STRING_LITERAL, 0); }
		public Imported_scopeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_imported_scope; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterImported_scope(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitImported_scope(this);
		}
	}

	public final Imported_scopeContext imported_scope() throws RecognitionException {
		Imported_scopeContext _localctx = new Imported_scopeContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_imported_scope);
		try {
			setState(934);
			switch (_input.LA(1)) {
			case DOUBLE_COLON:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(932); scoped_name();
				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(933); match(STRING_LITERAL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Type_id_declContext extends ParserRuleContext {
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public TerminalNode STRING_LITERAL() { return getToken(IDLParser.STRING_LITERAL, 0); }
		public TerminalNode KW_TYPEID() { return getToken(IDLParser.KW_TYPEID, 0); }
		public Type_id_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_id_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterType_id_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitType_id_decl(this);
		}
	}

	public final Type_id_declContext type_id_decl() throws RecognitionException {
		Type_id_declContext _localctx = new Type_id_declContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_type_id_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(936); match(KW_TYPEID);
			setState(937); scoped_name();
			setState(938); match(STRING_LITERAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Type_prefix_declContext extends ParserRuleContext {
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public TerminalNode KW_TYPEPREFIX() { return getToken(IDLParser.KW_TYPEPREFIX, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(IDLParser.STRING_LITERAL, 0); }
		public Type_prefix_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_prefix_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterType_prefix_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitType_prefix_decl(this);
		}
	}

	public final Type_prefix_declContext type_prefix_decl() throws RecognitionException {
		Type_prefix_declContext _localctx = new Type_prefix_declContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_type_prefix_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(940); match(KW_TYPEPREFIX);
			setState(941); scoped_name();
			setState(942); match(STRING_LITERAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Readonly_attr_specContext extends ParserRuleContext {
		public TerminalNode KW_READONLY() { return getToken(IDLParser.KW_READONLY, 0); }
		public Param_type_specContext param_type_spec() {
			return getRuleContext(Param_type_specContext.class,0);
		}
		public TerminalNode KW_ATTRIBUTE() { return getToken(IDLParser.KW_ATTRIBUTE, 0); }
		public Readonly_attr_declaratorContext readonly_attr_declarator() {
			return getRuleContext(Readonly_attr_declaratorContext.class,0);
		}
		public Readonly_attr_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_readonly_attr_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterReadonly_attr_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitReadonly_attr_spec(this);
		}
	}

	public final Readonly_attr_specContext readonly_attr_spec() throws RecognitionException {
		Readonly_attr_specContext _localctx = new Readonly_attr_specContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_readonly_attr_spec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(944); match(KW_READONLY);
			setState(945); match(KW_ATTRIBUTE);
			setState(946); param_type_spec();
			setState(947); readonly_attr_declarator();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Readonly_attr_declaratorContext extends ParserRuleContext {
		public List<Simple_declaratorContext> simple_declarator() {
			return getRuleContexts(Simple_declaratorContext.class);
		}
		public TerminalNode COMA(int i) {
			return getToken(IDLParser.COMA, i);
		}
		public Simple_declaratorContext simple_declarator(int i) {
			return getRuleContext(Simple_declaratorContext.class,i);
		}
		public Raises_exprContext raises_expr() {
			return getRuleContext(Raises_exprContext.class,0);
		}
		public List<TerminalNode> COMA() { return getTokens(IDLParser.COMA); }
		public Readonly_attr_declaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_readonly_attr_declarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterReadonly_attr_declarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitReadonly_attr_declarator(this);
		}
	}

	public final Readonly_attr_declaratorContext readonly_attr_declarator() throws RecognitionException {
		Readonly_attr_declaratorContext _localctx = new Readonly_attr_declaratorContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_readonly_attr_declarator);
		int _la;
		try {
			setState(960);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(949); simple_declarator();
				setState(950); raises_expr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(952); simple_declarator();
				setState(957);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMA) {
					{
					{
					setState(953); match(COMA);
					setState(954); simple_declarator();
					}
					}
					setState(959);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Attr_specContext extends ParserRuleContext {
		public Param_type_specContext param_type_spec() {
			return getRuleContext(Param_type_specContext.class,0);
		}
		public Attr_declaratorContext attr_declarator() {
			return getRuleContext(Attr_declaratorContext.class,0);
		}
		public TerminalNode KW_ATTRIBUTE() { return getToken(IDLParser.KW_ATTRIBUTE, 0); }
		public Attr_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attr_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterAttr_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitAttr_spec(this);
		}
	}

	public final Attr_specContext attr_spec() throws RecognitionException {
		Attr_specContext _localctx = new Attr_specContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_attr_spec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(962); match(KW_ATTRIBUTE);
			setState(963); param_type_spec();
			setState(964); attr_declarator();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Attr_declaratorContext extends ParserRuleContext {
		public List<Simple_declaratorContext> simple_declarator() {
			return getRuleContexts(Simple_declaratorContext.class);
		}
		public TerminalNode COMA(int i) {
			return getToken(IDLParser.COMA, i);
		}
		public Simple_declaratorContext simple_declarator(int i) {
			return getRuleContext(Simple_declaratorContext.class,i);
		}
		public Attr_raises_exprContext attr_raises_expr() {
			return getRuleContext(Attr_raises_exprContext.class,0);
		}
		public List<TerminalNode> COMA() { return getTokens(IDLParser.COMA); }
		public Attr_declaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attr_declarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterAttr_declarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitAttr_declarator(this);
		}
	}

	public final Attr_declaratorContext attr_declarator() throws RecognitionException {
		Attr_declaratorContext _localctx = new Attr_declaratorContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_attr_declarator);
		int _la;
		try {
			setState(977);
			switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(966); simple_declarator();
				setState(967); attr_raises_expr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(969); simple_declarator();
				setState(974);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMA) {
					{
					{
					setState(970); match(COMA);
					setState(971); simple_declarator();
					}
					}
					setState(976);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Attr_raises_exprContext extends ParserRuleContext {
		public Set_excep_exprContext set_excep_expr() {
			return getRuleContext(Set_excep_exprContext.class,0);
		}
		public Get_excep_exprContext get_excep_expr() {
			return getRuleContext(Get_excep_exprContext.class,0);
		}
		public Attr_raises_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attr_raises_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterAttr_raises_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitAttr_raises_expr(this);
		}
	}

	public final Attr_raises_exprContext attr_raises_expr() throws RecognitionException {
		Attr_raises_exprContext _localctx = new Attr_raises_exprContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_attr_raises_expr);
		int _la;
		try {
			setState(984);
			switch (_input.LA(1)) {
			case KW_GETRAISES:
				enterOuterAlt(_localctx, 1);
				{
				setState(979); get_excep_expr();
				setState(981);
				_la = _input.LA(1);
				if (_la==KW_SETRAISES) {
					{
					setState(980); set_excep_expr();
					}
				}

				}
				break;
			case KW_SETRAISES:
				enterOuterAlt(_localctx, 2);
				{
				setState(983); set_excep_expr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Get_excep_exprContext extends ParserRuleContext {
		public TerminalNode KW_GETRAISES() { return getToken(IDLParser.KW_GETRAISES, 0); }
		public Exception_listContext exception_list() {
			return getRuleContext(Exception_listContext.class,0);
		}
		public Get_excep_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_get_excep_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterGet_excep_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitGet_excep_expr(this);
		}
	}

	public final Get_excep_exprContext get_excep_expr() throws RecognitionException {
		Get_excep_exprContext _localctx = new Get_excep_exprContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_get_excep_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(986); match(KW_GETRAISES);
			setState(987); exception_list();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Set_excep_exprContext extends ParserRuleContext {
		public Exception_listContext exception_list() {
			return getRuleContext(Exception_listContext.class,0);
		}
		public TerminalNode KW_SETRAISES() { return getToken(IDLParser.KW_SETRAISES, 0); }
		public Set_excep_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_set_excep_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterSet_excep_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitSet_excep_expr(this);
		}
	}

	public final Set_excep_exprContext set_excep_expr() throws RecognitionException {
		Set_excep_exprContext _localctx = new Set_excep_exprContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_set_excep_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(989); match(KW_SETRAISES);
			setState(990); exception_list();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Exception_listContext extends ParserRuleContext {
		public List<Scoped_nameContext> scoped_name() {
			return getRuleContexts(Scoped_nameContext.class);
		}
		public Scoped_nameContext scoped_name(int i) {
			return getRuleContext(Scoped_nameContext.class,i);
		}
		public TerminalNode COMA(int i) {
			return getToken(IDLParser.COMA, i);
		}
		public TerminalNode LEFT_BRACKET() { return getToken(IDLParser.LEFT_BRACKET, 0); }
		public TerminalNode RIGHT_BRACKET() { return getToken(IDLParser.RIGHT_BRACKET, 0); }
		public List<TerminalNode> COMA() { return getTokens(IDLParser.COMA); }
		public Exception_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exception_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterException_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitException_list(this);
		}
	}

	public final Exception_listContext exception_list() throws RecognitionException {
		Exception_listContext _localctx = new Exception_listContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_exception_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(992); match(LEFT_BRACKET);
			setState(993); scoped_name();
			setState(998);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(994); match(COMA);
				setState(995); scoped_name();
				}
				}
				setState(1000);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1001); match(RIGHT_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComponentContext extends ParserRuleContext {
		public Component_forward_declContext component_forward_decl() {
			return getRuleContext(Component_forward_declContext.class,0);
		}
		public Component_declContext component_decl() {
			return getRuleContext(Component_declContext.class,0);
		}
		public ComponentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_component; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterComponent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitComponent(this);
		}
	}

	public final ComponentContext component() throws RecognitionException {
		ComponentContext _localctx = new ComponentContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_component);
		try {
			setState(1005);
			switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1003); component_decl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1004); component_forward_decl();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Component_forward_declContext extends ParserRuleContext {
		public TerminalNode KW_COMPONENT() { return getToken(IDLParser.KW_COMPONENT, 0); }
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public Component_forward_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_component_forward_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterComponent_forward_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitComponent_forward_decl(this);
		}
	}

	public final Component_forward_declContext component_forward_decl() throws RecognitionException {
		Component_forward_declContext _localctx = new Component_forward_declContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_component_forward_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1007); match(KW_COMPONENT);
			setState(1008); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Component_declContext extends ParserRuleContext {
		public Component_bodyContext component_body() {
			return getRuleContext(Component_bodyContext.class,0);
		}
		public Component_headerContext component_header() {
			return getRuleContext(Component_headerContext.class,0);
		}
		public TerminalNode LEFT_BRACE() { return getToken(IDLParser.LEFT_BRACE, 0); }
		public TerminalNode RIGHT_BRACE() { return getToken(IDLParser.RIGHT_BRACE, 0); }
		public Component_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_component_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterComponent_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitComponent_decl(this);
		}
	}

	public final Component_declContext component_decl() throws RecognitionException {
		Component_declContext _localctx = new Component_declContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_component_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1010); component_header();
			setState(1011); match(LEFT_BRACE);
			setState(1012); component_body();
			setState(1013); match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Component_headerContext extends ParserRuleContext {
		public TerminalNode KW_COMPONENT() { return getToken(IDLParser.KW_COMPONENT, 0); }
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public Supported_interface_specContext supported_interface_spec() {
			return getRuleContext(Supported_interface_specContext.class,0);
		}
		public Component_inheritance_specContext component_inheritance_spec() {
			return getRuleContext(Component_inheritance_specContext.class,0);
		}
		public Component_headerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_component_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterComponent_header(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitComponent_header(this);
		}
	}

	public final Component_headerContext component_header() throws RecognitionException {
		Component_headerContext _localctx = new Component_headerContext(_ctx, getState());
		enterRule(_localctx, 228, RULE_component_header);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1015); match(KW_COMPONENT);
			setState(1016); match(ID);
			setState(1018);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(1017); component_inheritance_spec();
				}
			}

			setState(1021);
			_la = _input.LA(1);
			if (_la==KW_SUPPORTS) {
				{
				setState(1020); supported_interface_spec();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Supported_interface_specContext extends ParserRuleContext {
		public List<Scoped_nameContext> scoped_name() {
			return getRuleContexts(Scoped_nameContext.class);
		}
		public Scoped_nameContext scoped_name(int i) {
			return getRuleContext(Scoped_nameContext.class,i);
		}
		public TerminalNode COMA(int i) {
			return getToken(IDLParser.COMA, i);
		}
		public TerminalNode KW_SUPPORTS() { return getToken(IDLParser.KW_SUPPORTS, 0); }
		public List<TerminalNode> COMA() { return getTokens(IDLParser.COMA); }
		public Supported_interface_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_supported_interface_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterSupported_interface_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitSupported_interface_spec(this);
		}
	}

	public final Supported_interface_specContext supported_interface_spec() throws RecognitionException {
		Supported_interface_specContext _localctx = new Supported_interface_specContext(_ctx, getState());
		enterRule(_localctx, 230, RULE_supported_interface_spec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1023); match(KW_SUPPORTS);
			setState(1024); scoped_name();
			setState(1029);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(1025); match(COMA);
				setState(1026); scoped_name();
				}
				}
				setState(1031);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Component_inheritance_specContext extends ParserRuleContext {
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public TerminalNode COLON() { return getToken(IDLParser.COLON, 0); }
		public Component_inheritance_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_component_inheritance_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterComponent_inheritance_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitComponent_inheritance_spec(this);
		}
	}

	public final Component_inheritance_specContext component_inheritance_spec() throws RecognitionException {
		Component_inheritance_specContext _localctx = new Component_inheritance_specContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_component_inheritance_spec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1032); match(COLON);
			setState(1033); scoped_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Component_bodyContext extends ParserRuleContext {
		public Component_exportContext component_export(int i) {
			return getRuleContext(Component_exportContext.class,i);
		}
		public List<Component_exportContext> component_export() {
			return getRuleContexts(Component_exportContext.class);
		}
		public Component_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_component_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterComponent_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitComponent_body(this);
		}
	}

	public final Component_bodyContext component_body() throws RecognitionException {
		Component_bodyContext _localctx = new Component_bodyContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_component_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1038);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 37)) & ~0x3f) == 0 && ((1L << (_la - 37)) & ((1L << (KW_EMITS - 37)) | (1L << (KW_PUBLISHES - 37)) | (1L << (KW_USES - 37)) | (1L << (KW_READONLY - 37)) | (1L << (KW_PROVIDES - 37)) | (1L << (KW_CONSUMES - 37)) | (1L << (KW_ATTRIBUTE - 37)))) != 0)) {
				{
				{
				setState(1035); component_export();
				}
				}
				setState(1040);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Component_exportContext extends ParserRuleContext {
		public TerminalNode SEMICOLON() { return getToken(IDLParser.SEMICOLON, 0); }
		public Attr_declContext attr_decl() {
			return getRuleContext(Attr_declContext.class,0);
		}
		public Provides_declContext provides_decl() {
			return getRuleContext(Provides_declContext.class,0);
		}
		public Emits_declContext emits_decl() {
			return getRuleContext(Emits_declContext.class,0);
		}
		public Consumes_declContext consumes_decl() {
			return getRuleContext(Consumes_declContext.class,0);
		}
		public Publishes_declContext publishes_decl() {
			return getRuleContext(Publishes_declContext.class,0);
		}
		public Uses_declContext uses_decl() {
			return getRuleContext(Uses_declContext.class,0);
		}
		public Component_exportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_component_export; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterComponent_export(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitComponent_export(this);
		}
	}

	public final Component_exportContext component_export() throws RecognitionException {
		Component_exportContext _localctx = new Component_exportContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_component_export);
		try {
			setState(1059);
			switch (_input.LA(1)) {
			case KW_PROVIDES:
				enterOuterAlt(_localctx, 1);
				{
				setState(1041); provides_decl();
				setState(1042); match(SEMICOLON);
				}
				break;
			case KW_USES:
				enterOuterAlt(_localctx, 2);
				{
				setState(1044); uses_decl();
				setState(1045); match(SEMICOLON);
				}
				break;
			case KW_EMITS:
				enterOuterAlt(_localctx, 3);
				{
				setState(1047); emits_decl();
				setState(1048); match(SEMICOLON);
				}
				break;
			case KW_PUBLISHES:
				enterOuterAlt(_localctx, 4);
				{
				setState(1050); publishes_decl();
				setState(1051); match(SEMICOLON);
				}
				break;
			case KW_CONSUMES:
				enterOuterAlt(_localctx, 5);
				{
				setState(1053); consumes_decl();
				setState(1054); match(SEMICOLON);
				}
				break;
			case KW_READONLY:
			case KW_ATTRIBUTE:
				enterOuterAlt(_localctx, 6);
				{
				setState(1056); attr_decl();
				setState(1057); match(SEMICOLON);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Provides_declContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public TerminalNode KW_PROVIDES() { return getToken(IDLParser.KW_PROVIDES, 0); }
		public Interface_typeContext interface_type() {
			return getRuleContext(Interface_typeContext.class,0);
		}
		public Provides_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_provides_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterProvides_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitProvides_decl(this);
		}
	}

	public final Provides_declContext provides_decl() throws RecognitionException {
		Provides_declContext _localctx = new Provides_declContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_provides_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1061); match(KW_PROVIDES);
			setState(1062); interface_type();
			setState(1063); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Interface_typeContext extends ParserRuleContext {
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public TerminalNode KW_OBJECT() { return getToken(IDLParser.KW_OBJECT, 0); }
		public Interface_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interface_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterInterface_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitInterface_type(this);
		}
	}

	public final Interface_typeContext interface_type() throws RecognitionException {
		Interface_typeContext _localctx = new Interface_typeContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_interface_type);
		try {
			setState(1067);
			switch (_input.LA(1)) {
			case DOUBLE_COLON:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(1065); scoped_name();
				}
				break;
			case KW_OBJECT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1066); match(KW_OBJECT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Uses_declContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public TerminalNode KW_USES() { return getToken(IDLParser.KW_USES, 0); }
		public TerminalNode KW_MULTIPLE() { return getToken(IDLParser.KW_MULTIPLE, 0); }
		public Interface_typeContext interface_type() {
			return getRuleContext(Interface_typeContext.class,0);
		}
		public Uses_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_uses_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterUses_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitUses_decl(this);
		}
	}

	public final Uses_declContext uses_decl() throws RecognitionException {
		Uses_declContext _localctx = new Uses_declContext(_ctx, getState());
		enterRule(_localctx, 242, RULE_uses_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1069); match(KW_USES);
			setState(1071);
			_la = _input.LA(1);
			if (_la==KW_MULTIPLE) {
				{
				setState(1070); match(KW_MULTIPLE);
				}
			}

			setState(1073); interface_type();
			setState(1074); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Emits_declContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public TerminalNode KW_EMITS() { return getToken(IDLParser.KW_EMITS, 0); }
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public Emits_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emits_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterEmits_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitEmits_decl(this);
		}
	}

	public final Emits_declContext emits_decl() throws RecognitionException {
		Emits_declContext _localctx = new Emits_declContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_emits_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1076); match(KW_EMITS);
			setState(1077); scoped_name();
			setState(1078); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Publishes_declContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public TerminalNode KW_PUBLISHES() { return getToken(IDLParser.KW_PUBLISHES, 0); }
		public Publishes_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_publishes_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterPublishes_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitPublishes_decl(this);
		}
	}

	public final Publishes_declContext publishes_decl() throws RecognitionException {
		Publishes_declContext _localctx = new Publishes_declContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_publishes_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1080); match(KW_PUBLISHES);
			setState(1081); scoped_name();
			setState(1082); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Consumes_declContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public TerminalNode KW_CONSUMES() { return getToken(IDLParser.KW_CONSUMES, 0); }
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public Consumes_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_consumes_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterConsumes_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitConsumes_decl(this);
		}
	}

	public final Consumes_declContext consumes_decl() throws RecognitionException {
		Consumes_declContext _localctx = new Consumes_declContext(_ctx, getState());
		enterRule(_localctx, 248, RULE_consumes_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1084); match(KW_CONSUMES);
			setState(1085); scoped_name();
			setState(1086); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Home_declContext extends ParserRuleContext {
		public Home_headerContext home_header() {
			return getRuleContext(Home_headerContext.class,0);
		}
		public Home_bodyContext home_body() {
			return getRuleContext(Home_bodyContext.class,0);
		}
		public Home_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_home_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterHome_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitHome_decl(this);
		}
	}

	public final Home_declContext home_decl() throws RecognitionException {
		Home_declContext _localctx = new Home_declContext(_ctx, getState());
		enterRule(_localctx, 250, RULE_home_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1088); home_header();
			setState(1089); home_body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Home_headerContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public TerminalNode KW_HOME() { return getToken(IDLParser.KW_HOME, 0); }
		public Home_inheritance_specContext home_inheritance_spec() {
			return getRuleContext(Home_inheritance_specContext.class,0);
		}
		public TerminalNode KW_MANAGES() { return getToken(IDLParser.KW_MANAGES, 0); }
		public Supported_interface_specContext supported_interface_spec() {
			return getRuleContext(Supported_interface_specContext.class,0);
		}
		public Primary_key_specContext primary_key_spec() {
			return getRuleContext(Primary_key_specContext.class,0);
		}
		public Home_headerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_home_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterHome_header(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitHome_header(this);
		}
	}

	public final Home_headerContext home_header() throws RecognitionException {
		Home_headerContext _localctx = new Home_headerContext(_ctx, getState());
		enterRule(_localctx, 252, RULE_home_header);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1091); match(KW_HOME);
			setState(1092); match(ID);
			setState(1094);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(1093); home_inheritance_spec();
				}
			}

			setState(1097);
			_la = _input.LA(1);
			if (_la==KW_SUPPORTS) {
				{
				setState(1096); supported_interface_spec();
				}
			}

			setState(1099); match(KW_MANAGES);
			setState(1100); scoped_name();
			setState(1102);
			_la = _input.LA(1);
			if (_la==KW_PRIMARYKEY) {
				{
				setState(1101); primary_key_spec();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Home_inheritance_specContext extends ParserRuleContext {
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public TerminalNode COLON() { return getToken(IDLParser.COLON, 0); }
		public Home_inheritance_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_home_inheritance_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterHome_inheritance_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitHome_inheritance_spec(this);
		}
	}

	public final Home_inheritance_specContext home_inheritance_spec() throws RecognitionException {
		Home_inheritance_specContext _localctx = new Home_inheritance_specContext(_ctx, getState());
		enterRule(_localctx, 254, RULE_home_inheritance_spec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1104); match(COLON);
			setState(1105); scoped_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Primary_key_specContext extends ParserRuleContext {
		public Scoped_nameContext scoped_name() {
			return getRuleContext(Scoped_nameContext.class,0);
		}
		public TerminalNode KW_PRIMARYKEY() { return getToken(IDLParser.KW_PRIMARYKEY, 0); }
		public Primary_key_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary_key_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterPrimary_key_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitPrimary_key_spec(this);
		}
	}

	public final Primary_key_specContext primary_key_spec() throws RecognitionException {
		Primary_key_specContext _localctx = new Primary_key_specContext(_ctx, getState());
		enterRule(_localctx, 256, RULE_primary_key_spec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1107); match(KW_PRIMARYKEY);
			setState(1108); scoped_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Home_bodyContext extends ParserRuleContext {
		public Home_exportContext home_export(int i) {
			return getRuleContext(Home_exportContext.class,i);
		}
		public TerminalNode LEFT_BRACE() { return getToken(IDLParser.LEFT_BRACE, 0); }
		public List<Home_exportContext> home_export() {
			return getRuleContexts(Home_exportContext.class);
		}
		public TerminalNode RIGHT_BRACE() { return getToken(IDLParser.RIGHT_BRACE, 0); }
		public Home_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_home_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterHome_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitHome_body(this);
		}
	}

	public final Home_bodyContext home_body() throws RecognitionException {
		Home_bodyContext _localctx = new Home_bodyContext(_ctx, getState());
		enterRule(_localctx, 258, RULE_home_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1110); match(LEFT_BRACE);
			setState(1114);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE_COLON) | (1L << KW_STRING) | (1L << KW_TYPEDEF) | (1L << KW_OCTET) | (1L << KW_STRUCT) | (1L << KW_NATIVE) | (1L << KW_READONLY) | (1L << KW_FINDER) | (1L << KW_VOID) | (1L << KW_WCHAR) | (1L << KW_SHORT) | (1L << KW_LONG) | (1L << KW_ENUM) | (1L << KW_WSTRING))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (KW_FACTORY - 66)) | (1L << (KW_EXCEPTION - 66)) | (1L << (KW_CONST - 66)) | (1L << (KW_VALUEBASE - 66)) | (1L << (KW_OBJECT - 66)) | (1L << (KW_UNSIGNED - 66)) | (1L << (KW_UNION - 66)) | (1L << (KW_ONEWAY - 66)) | (1L << (KW_ANY - 66)) | (1L << (KW_CHAR - 66)) | (1L << (KW_FLOAT - 66)) | (1L << (KW_BOOLEAN - 66)) | (1L << (KW_DOUBLE - 66)) | (1L << (KW_TYPEPREFIX - 66)) | (1L << (KW_TYPEID - 66)) | (1L << (KW_ATTRIBUTE - 66)) | (1L << (ID - 66)))) != 0)) {
				{
				{
				setState(1111); home_export();
				}
				}
				setState(1116);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1117); match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Home_exportContext extends ParserRuleContext {
		public TerminalNode SEMICOLON() { return getToken(IDLParser.SEMICOLON, 0); }
		public ExportContext export() {
			return getRuleContext(ExportContext.class,0);
		}
		public Factory_declContext factory_decl() {
			return getRuleContext(Factory_declContext.class,0);
		}
		public Finder_declContext finder_decl() {
			return getRuleContext(Finder_declContext.class,0);
		}
		public Home_exportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_home_export; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterHome_export(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitHome_export(this);
		}
	}

	public final Home_exportContext home_export() throws RecognitionException {
		Home_exportContext _localctx = new Home_exportContext(_ctx, getState());
		enterRule(_localctx, 260, RULE_home_export);
		try {
			setState(1126);
			switch (_input.LA(1)) {
			case DOUBLE_COLON:
			case KW_STRING:
			case KW_TYPEDEF:
			case KW_OCTET:
			case KW_STRUCT:
			case KW_NATIVE:
			case KW_READONLY:
			case KW_VOID:
			case KW_WCHAR:
			case KW_SHORT:
			case KW_LONG:
			case KW_ENUM:
			case KW_WSTRING:
			case KW_EXCEPTION:
			case KW_CONST:
			case KW_VALUEBASE:
			case KW_OBJECT:
			case KW_UNSIGNED:
			case KW_UNION:
			case KW_ONEWAY:
			case KW_ANY:
			case KW_CHAR:
			case KW_FLOAT:
			case KW_BOOLEAN:
			case KW_DOUBLE:
			case KW_TYPEPREFIX:
			case KW_TYPEID:
			case KW_ATTRIBUTE:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(1119); export();
				}
				break;
			case KW_FACTORY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1120); factory_decl();
				setState(1121); match(SEMICOLON);
				}
				break;
			case KW_FINDER:
				enterOuterAlt(_localctx, 3);
				{
				setState(1123); finder_decl();
				setState(1124); match(SEMICOLON);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Factory_declContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public Init_param_declsContext init_param_decls() {
			return getRuleContext(Init_param_declsContext.class,0);
		}
		public TerminalNode KW_FACTORY() { return getToken(IDLParser.KW_FACTORY, 0); }
		public TerminalNode LEFT_BRACKET() { return getToken(IDLParser.LEFT_BRACKET, 0); }
		public TerminalNode RIGHT_BRACKET() { return getToken(IDLParser.RIGHT_BRACKET, 0); }
		public Raises_exprContext raises_expr() {
			return getRuleContext(Raises_exprContext.class,0);
		}
		public Factory_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factory_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterFactory_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitFactory_decl(this);
		}
	}

	public final Factory_declContext factory_decl() throws RecognitionException {
		Factory_declContext _localctx = new Factory_declContext(_ctx, getState());
		enterRule(_localctx, 262, RULE_factory_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1128); match(KW_FACTORY);
			setState(1129); match(ID);
			setState(1130); match(LEFT_BRACKET);
			setState(1132);
			_la = _input.LA(1);
			if (_la==KW_IN) {
				{
				setState(1131); init_param_decls();
				}
			}

			setState(1134); match(RIGHT_BRACKET);
			setState(1136);
			_la = _input.LA(1);
			if (_la==KW_RAISES) {
				{
				setState(1135); raises_expr();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Finder_declContext extends ParserRuleContext {
		public TerminalNode KW_FINDER() { return getToken(IDLParser.KW_FINDER, 0); }
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public Init_param_declsContext init_param_decls() {
			return getRuleContext(Init_param_declsContext.class,0);
		}
		public TerminalNode LEFT_BRACKET() { return getToken(IDLParser.LEFT_BRACKET, 0); }
		public TerminalNode RIGHT_BRACKET() { return getToken(IDLParser.RIGHT_BRACKET, 0); }
		public Raises_exprContext raises_expr() {
			return getRuleContext(Raises_exprContext.class,0);
		}
		public Finder_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_finder_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterFinder_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitFinder_decl(this);
		}
	}

	public final Finder_declContext finder_decl() throws RecognitionException {
		Finder_declContext _localctx = new Finder_declContext(_ctx, getState());
		enterRule(_localctx, 264, RULE_finder_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1138); match(KW_FINDER);
			setState(1139); match(ID);
			setState(1140); match(LEFT_BRACKET);
			setState(1142);
			_la = _input.LA(1);
			if (_la==KW_IN) {
				{
				setState(1141); init_param_decls();
				}
			}

			setState(1144); match(RIGHT_BRACKET);
			setState(1146);
			_la = _input.LA(1);
			if (_la==KW_RAISES) {
				{
				setState(1145); raises_expr();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EventContext extends ParserRuleContext {
		public Event_abs_declContext event_abs_decl() {
			return getRuleContext(Event_abs_declContext.class,0);
		}
		public Event_declContext event_decl() {
			return getRuleContext(Event_declContext.class,0);
		}
		public Event_forward_declContext event_forward_decl() {
			return getRuleContext(Event_forward_declContext.class,0);
		}
		public EventContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_event; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitEvent(this);
		}
	}

	public final EventContext event() throws RecognitionException {
		EventContext _localctx = new EventContext(_ctx, getState());
		enterRule(_localctx, 266, RULE_event);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1151);
			switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
			case 1:
				{
				setState(1148); event_decl();
				}
				break;
			case 2:
				{
				setState(1149); event_abs_decl();
				}
				break;
			case 3:
				{
				setState(1150); event_forward_decl();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Event_forward_declContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public TerminalNode KW_EVENTTYPE() { return getToken(IDLParser.KW_EVENTTYPE, 0); }
		public TerminalNode KW_ABSTRACT() { return getToken(IDLParser.KW_ABSTRACT, 0); }
		public Event_forward_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_event_forward_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterEvent_forward_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitEvent_forward_decl(this);
		}
	}

	public final Event_forward_declContext event_forward_decl() throws RecognitionException {
		Event_forward_declContext _localctx = new Event_forward_declContext(_ctx, getState());
		enterRule(_localctx, 268, RULE_event_forward_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1154);
			_la = _input.LA(1);
			if (_la==KW_ABSTRACT) {
				{
				setState(1153); match(KW_ABSTRACT);
				}
			}

			setState(1156); match(KW_EVENTTYPE);
			setState(1157); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Event_abs_declContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public List<ExportContext> export() {
			return getRuleContexts(ExportContext.class);
		}
		public ExportContext export(int i) {
			return getRuleContext(ExportContext.class,i);
		}
		public TerminalNode KW_EVENTTYPE() { return getToken(IDLParser.KW_EVENTTYPE, 0); }
		public TerminalNode LEFT_BRACE() { return getToken(IDLParser.LEFT_BRACE, 0); }
		public TerminalNode KW_ABSTRACT() { return getToken(IDLParser.KW_ABSTRACT, 0); }
		public Value_inheritance_specContext value_inheritance_spec() {
			return getRuleContext(Value_inheritance_specContext.class,0);
		}
		public TerminalNode RIGHT_BRACE() { return getToken(IDLParser.RIGHT_BRACE, 0); }
		public Event_abs_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_event_abs_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterEvent_abs_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitEvent_abs_decl(this);
		}
	}

	public final Event_abs_declContext event_abs_decl() throws RecognitionException {
		Event_abs_declContext _localctx = new Event_abs_declContext(_ctx, getState());
		enterRule(_localctx, 270, RULE_event_abs_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1159); match(KW_ABSTRACT);
			setState(1160); match(KW_EVENTTYPE);
			setState(1161); match(ID);
			setState(1162); value_inheritance_spec();
			setState(1163); match(LEFT_BRACE);
			setState(1167);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE_COLON) | (1L << KW_STRING) | (1L << KW_TYPEDEF) | (1L << KW_OCTET) | (1L << KW_STRUCT) | (1L << KW_NATIVE) | (1L << KW_READONLY) | (1L << KW_VOID) | (1L << KW_WCHAR) | (1L << KW_SHORT) | (1L << KW_LONG) | (1L << KW_ENUM) | (1L << KW_WSTRING))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (KW_EXCEPTION - 67)) | (1L << (KW_CONST - 67)) | (1L << (KW_VALUEBASE - 67)) | (1L << (KW_OBJECT - 67)) | (1L << (KW_UNSIGNED - 67)) | (1L << (KW_UNION - 67)) | (1L << (KW_ONEWAY - 67)) | (1L << (KW_ANY - 67)) | (1L << (KW_CHAR - 67)) | (1L << (KW_FLOAT - 67)) | (1L << (KW_BOOLEAN - 67)) | (1L << (KW_DOUBLE - 67)) | (1L << (KW_TYPEPREFIX - 67)) | (1L << (KW_TYPEID - 67)) | (1L << (KW_ATTRIBUTE - 67)) | (1L << (ID - 67)))) != 0)) {
				{
				{
				setState(1164); export();
				}
				}
				setState(1169);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1170); match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Event_declContext extends ParserRuleContext {
		public TerminalNode LEFT_BRACE() { return getToken(IDLParser.LEFT_BRACE, 0); }
		public Value_elementContext value_element(int i) {
			return getRuleContext(Value_elementContext.class,i);
		}
		public List<Value_elementContext> value_element() {
			return getRuleContexts(Value_elementContext.class);
		}
		public TerminalNode RIGHT_BRACE() { return getToken(IDLParser.RIGHT_BRACE, 0); }
		public Event_headerContext event_header() {
			return getRuleContext(Event_headerContext.class,0);
		}
		public Event_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_event_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterEvent_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitEvent_decl(this);
		}
	}

	public final Event_declContext event_decl() throws RecognitionException {
		Event_declContext _localctx = new Event_declContext(_ctx, getState());
		enterRule(_localctx, 272, RULE_event_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1172); event_header();
			setState(1173); match(LEFT_BRACE);
			setState(1177);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE_COLON) | (1L << KW_STRING) | (1L << KW_TYPEDEF) | (1L << KW_OCTET) | (1L << KW_STRUCT) | (1L << KW_NATIVE) | (1L << KW_READONLY) | (1L << KW_VOID) | (1L << KW_PRIVATE) | (1L << KW_WCHAR) | (1L << KW_PUBLIC) | (1L << KW_SHORT) | (1L << KW_LONG) | (1L << KW_ENUM) | (1L << KW_WSTRING))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (KW_FACTORY - 66)) | (1L << (KW_EXCEPTION - 66)) | (1L << (KW_CONST - 66)) | (1L << (KW_VALUEBASE - 66)) | (1L << (KW_OBJECT - 66)) | (1L << (KW_UNSIGNED - 66)) | (1L << (KW_UNION - 66)) | (1L << (KW_ONEWAY - 66)) | (1L << (KW_ANY - 66)) | (1L << (KW_CHAR - 66)) | (1L << (KW_FLOAT - 66)) | (1L << (KW_BOOLEAN - 66)) | (1L << (KW_DOUBLE - 66)) | (1L << (KW_TYPEPREFIX - 66)) | (1L << (KW_TYPEID - 66)) | (1L << (KW_ATTRIBUTE - 66)) | (1L << (ID - 66)))) != 0)) {
				{
				{
				setState(1174); value_element();
				}
				}
				setState(1179);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1180); match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Event_headerContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IDLParser.ID, 0); }
		public TerminalNode KW_EVENTTYPE() { return getToken(IDLParser.KW_EVENTTYPE, 0); }
		public TerminalNode KW_CUSTOM() { return getToken(IDLParser.KW_CUSTOM, 0); }
		public Value_inheritance_specContext value_inheritance_spec() {
			return getRuleContext(Value_inheritance_specContext.class,0);
		}
		public Event_headerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_event_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).enterEvent_header(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IDLListener ) ((IDLListener)listener).exitEvent_header(this);
		}
	}

	public final Event_headerContext event_header() throws RecognitionException {
		Event_headerContext _localctx = new Event_headerContext(_ctx, getState());
		enterRule(_localctx, 274, RULE_event_header);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1183);
			_la = _input.LA(1);
			if (_la==KW_CUSTOM) {
				{
				setState(1182); match(KW_CUSTOM);
				}
			}

			setState(1185); match(KW_EVENTTYPE);
			setState(1186); match(ID);
			setState(1187); value_inheritance_spec();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3g\u04a8\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"+
		"`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\4k\t"+
		"k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\4u\tu\4v\tv\4"+
		"w\tw\4x\tx\4y\ty\4z\tz\4{\t{\4|\t|\4}\t}\4~\t~\4\177\t\177\4\u0080\t\u0080"+
		"\4\u0081\t\u0081\4\u0082\t\u0082\4\u0083\t\u0083\4\u0084\t\u0084\4\u0085"+
		"\t\u0085\4\u0086\t\u0086\4\u0087\t\u0087\4\u0088\t\u0088\4\u0089\t\u0089"+
		"\4\u008a\t\u008a\4\u008b\t\u008b\3\2\7\2\u0118\n\2\f\2\16\2\u011b\13\2"+
		"\3\2\6\2\u011e\n\2\r\2\16\2\u011f\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0143\n\3\3\4\3\4\3\4\3\4\6\4\u0149\n\4"+
		"\r\4\16\4\u014a\3\4\3\4\3\5\3\5\5\5\u0151\n\5\3\6\3\6\3\6\3\6\3\6\3\7"+
		"\5\7\u0159\n\7\3\7\3\7\3\7\3\b\5\b\u015f\n\b\3\b\3\b\3\b\5\b\u0164\n\b"+
		"\3\t\7\t\u0167\n\t\f\t\16\t\u016a\13\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u0181\n\n\3\13"+
		"\3\13\3\13\3\13\7\13\u0187\n\13\f\13\16\13\u018a\13\13\3\f\3\f\3\r\5\r"+
		"\u018f\n\r\3\r\3\r\3\r\7\r\u0194\n\r\f\r\16\r\u0197\13\r\3\16\3\16\3\16"+
		"\3\16\5\16\u019d\n\16\3\17\5\17\u01a0\n\17\3\17\3\17\3\17\3\20\3\20\3"+
		"\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\7\21\u01af\n\21\f\21\16\21\u01b2"+
		"\13\21\3\21\3\21\3\22\3\22\3\22\7\22\u01b9\n\22\f\22\16\22\u01bc\13\22"+
		"\3\22\3\22\3\23\5\23\u01c1\n\23\3\23\3\23\3\23\3\23\3\24\3\24\5\24\u01c9"+
		"\n\24\3\24\3\24\3\24\7\24\u01ce\n\24\f\24\16\24\u01d1\13\24\5\24\u01d3"+
		"\n\24\3\24\3\24\3\24\3\24\7\24\u01d9\n\24\f\24\16\24\u01dc\13\24\5\24"+
		"\u01de\n\24\3\25\3\25\3\26\3\26\3\26\5\26\u01e5\n\26\3\27\3\27\3\27\3"+
		"\27\3\27\3\30\3\30\3\30\3\30\5\30\u01f0\n\30\3\30\3\30\5\30\u01f4\n\30"+
		"\3\30\3\30\3\31\3\31\3\31\7\31\u01fb\n\31\f\31\16\31\u01fe\13\31\3\32"+
		"\3\32\3\32\3\32\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\35\3\35\5\35\u0216\n\35\3\36\3\36\3\37\3\37"+
		"\3\37\7\37\u021d\n\37\f\37\16\37\u0220\13\37\3 \3 \3 \7 \u0225\n \f \16"+
		" \u0228\13 \3!\3!\3!\7!\u022d\n!\f!\16!\u0230\13!\3\"\3\"\3\"\7\"\u0235"+
		"\n\"\f\"\16\"\u0238\13\"\3#\3#\3#\7#\u023d\n#\f#\16#\u0240\13#\3$\3$\3"+
		"$\7$\u0245\n$\f$\16$\u0248\13$\3%\3%\3%\3%\5%\u024e\n%\3&\3&\3\'\3\'\3"+
		"\'\3\'\3\'\3\'\5\'\u0258\n\'\3(\3(\3)\3)\3*\3*\3*\3*\3*\3*\3*\3*\5*\u0266"+
		"\n*\3+\3+\3+\3,\3,\5,\u026d\n,\3-\3-\3-\5-\u0272\n-\3.\3.\3.\3.\3.\3."+
		"\3.\3.\3.\5.\u027d\n.\3/\3/\3/\3/\5/\u0283\n/\3\60\3\60\3\60\5\60\u0288"+
		"\n\60\3\61\3\61\3\61\7\61\u028d\n\61\f\61\16\61\u0290\13\61\3\62\3\62"+
		"\5\62\u0294\n\62\3\63\3\63\3\64\3\64\3\65\3\65\3\65\3\65\5\65\u029e\n"+
		"\65\3\66\3\66\5\66\u02a2\n\66\3\67\3\67\3\67\5\67\u02a7\n\67\38\38\39"+
		"\39\3:\3:\3:\3;\3;\3;\5;\u02b3\n;\3<\3<\3<\3=\3=\3=\3>\3>\3>\3>\3?\3?"+
		"\3@\3@\3A\3A\3B\3B\3C\3C\3D\3D\3E\3E\3E\3E\3E\3E\3F\6F\u02d2\nF\rF\16"+
		"F\u02d3\3G\3G\3G\3G\3H\3H\3H\3H\3I\3I\3I\3I\3I\3I\3I\3I\3I\3I\3J\3J\3"+
		"J\3J\3J\5J\u02ed\nJ\3K\6K\u02f0\nK\rK\16K\u02f1\3L\6L\u02f5\nL\rL\16L"+
		"\u02f6\3L\3L\3L\3M\3M\3M\3M\3M\3M\5M\u0302\nM\3N\3N\3N\3O\3O\3O\3O\3O"+
		"\3O\7O\u030d\nO\fO\16O\u0310\13O\3O\3O\3P\3P\3Q\3Q\3Q\3Q\3Q\3Q\3Q\3Q\3"+
		"Q\3Q\3Q\3Q\5Q\u0322\nQ\3R\3R\3R\3R\3R\3R\5R\u032a\nR\3S\3S\3S\3S\3S\3"+
		"S\5S\u0332\nS\3T\3T\6T\u0336\nT\rT\16T\u0337\3U\3U\3U\3U\3V\3V\5V\u0340"+
		"\nV\3W\3W\3W\3W\7W\u0346\nW\fW\16W\u0349\13W\3W\3W\3X\5X\u034e\nX\3X\3"+
		"X\3X\3X\5X\u0354\nX\3X\5X\u0357\nX\3Y\3Y\3Z\3Z\5Z\u035d\nZ\3[\3[\3[\3"+
		"[\7[\u0363\n[\f[\16[\u0366\13[\3[\3[\3[\3[\5[\u036c\n[\3\\\3\\\3\\\3\\"+
		"\3]\3]\3^\3^\3^\3^\3^\7^\u0379\n^\f^\16^\u037c\13^\3^\3^\3_\3_\3_\3_\3"+
		"_\7_\u0385\n_\f_\16_\u0388\13_\3_\3_\3`\3`\3`\3`\5`\u0390\n`\3a\3a\3a"+
		"\3a\3a\3a\3a\3b\3b\3c\3c\3d\3d\3d\3d\5d\u03a1\nd\3e\3e\3e\3e\3f\3f\5f"+
		"\u03a9\nf\3g\3g\3g\3g\3h\3h\3h\3h\3i\3i\3i\3i\3i\3j\3j\3j\3j\3j\3j\7j"+
		"\u03be\nj\fj\16j\u03c1\13j\5j\u03c3\nj\3k\3k\3k\3k\3l\3l\3l\3l\3l\3l\7"+
		"l\u03cf\nl\fl\16l\u03d2\13l\5l\u03d4\nl\3m\3m\5m\u03d8\nm\3m\5m\u03db"+
		"\nm\3n\3n\3n\3o\3o\3o\3p\3p\3p\3p\7p\u03e7\np\fp\16p\u03ea\13p\3p\3p\3"+
		"q\3q\5q\u03f0\nq\3r\3r\3r\3s\3s\3s\3s\3s\3t\3t\3t\5t\u03fd\nt\3t\5t\u0400"+
		"\nt\3u\3u\3u\3u\7u\u0406\nu\fu\16u\u0409\13u\3v\3v\3v\3w\7w\u040f\nw\f"+
		"w\16w\u0412\13w\3x\3x\3x\3x\3x\3x\3x\3x\3x\3x\3x\3x\3x\3x\3x\3x\3x\3x"+
		"\5x\u0426\nx\3y\3y\3y\3y\3z\3z\5z\u042e\nz\3{\3{\5{\u0432\n{\3{\3{\3{"+
		"\3|\3|\3|\3|\3}\3}\3}\3}\3~\3~\3~\3~\3\177\3\177\3\177\3\u0080\3\u0080"+
		"\3\u0080\5\u0080\u0449\n\u0080\3\u0080\5\u0080\u044c\n\u0080\3\u0080\3"+
		"\u0080\3\u0080\5\u0080\u0451\n\u0080\3\u0081\3\u0081\3\u0081\3\u0082\3"+
		"\u0082\3\u0082\3\u0083\3\u0083\7\u0083\u045b\n\u0083\f\u0083\16\u0083"+
		"\u045e\13\u0083\3\u0083\3\u0083\3\u0084\3\u0084\3\u0084\3\u0084\3\u0084"+
		"\3\u0084\3\u0084\5\u0084\u0469\n\u0084\3\u0085\3\u0085\3\u0085\3\u0085"+
		"\5\u0085\u046f\n\u0085\3\u0085\3\u0085\5\u0085\u0473\n\u0085\3\u0086\3"+
		"\u0086\3\u0086\3\u0086\5\u0086\u0479\n\u0086\3\u0086\3\u0086\5\u0086\u047d"+
		"\n\u0086\3\u0087\3\u0087\3\u0087\5\u0087\u0482\n\u0087\3\u0088\5\u0088"+
		"\u0485\n\u0088\3\u0088\3\u0088\3\u0088\3\u0089\3\u0089\3\u0089\3\u0089"+
		"\3\u0089\3\u0089\7\u0089\u0490\n\u0089\f\u0089\16\u0089\u0493\13\u0089"+
		"\3\u0089\3\u0089\3\u008a\3\u008a\3\u008a\7\u008a\u049a\n\u008a\f\u008a"+
		"\16\u008a\u049d\13\u008a\3\u008a\3\u008a\3\u008b\5\u008b\u04a2\n\u008b"+
		"\3\u008b\3\u008b\3\u008b\3\u008b\3\u008b\2\2\u008c\2\4\6\b\n\f\16\20\22"+
		"\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnp"+
		"rtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094"+
		"\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac"+
		"\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4"+
		"\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da\u00dc"+
		"\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0\u00f2\u00f4"+
		"\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106\u0108\u010a\u010c"+
		"\u010e\u0110\u0112\u0114\2\n\4\2XX``\4\288==\3\2#$\3\2\33\34\5\2\27\27"+
		"\32\32!!\4\2\26\26\33\34\4\2\3\3\5\f\5\2&&;;YY\u04b8\2\u0119\3\2\2\2\4"+
		"\u0142\3\2\2\2\6\u0144\3\2\2\2\b\u0150\3\2\2\2\n\u0152\3\2\2\2\f\u0158"+
		"\3\2\2\2\16\u015e\3\2\2\2\20\u0168\3\2\2\2\22\u0180\3\2\2\2\24\u0182\3"+
		"\2\2\2\26\u018b\3\2\2\2\30\u018e\3\2\2\2\32\u019c\3\2\2\2\34\u019f\3\2"+
		"\2\2\36\u01a4\3\2\2\2 \u01a8\3\2\2\2\"\u01b5\3\2\2\2$\u01c0\3\2\2\2&\u01d2"+
		"\3\2\2\2(\u01df\3\2\2\2*\u01e4\3\2\2\2,\u01e6\3\2\2\2.\u01eb\3\2\2\2\60"+
		"\u01f7\3\2\2\2\62\u01ff\3\2\2\2\64\u0203\3\2\2\2\66\u0205\3\2\2\28\u0215"+
		"\3\2\2\2:\u0217\3\2\2\2<\u0219\3\2\2\2>\u0221\3\2\2\2@\u0229\3\2\2\2B"+
		"\u0231\3\2\2\2D\u0239\3\2\2\2F\u0241\3\2\2\2H\u024d\3\2\2\2J\u024f\3\2"+
		"\2\2L\u0257\3\2\2\2N\u0259\3\2\2\2P\u025b\3\2\2\2R\u0265\3\2\2\2T\u0267"+
		"\3\2\2\2V\u026c\3\2\2\2X\u0271\3\2\2\2Z\u027c\3\2\2\2\\\u0282\3\2\2\2"+
		"^\u0287\3\2\2\2`\u0289\3\2\2\2b\u0293\3\2\2\2d\u0295\3\2\2\2f\u0297\3"+
		"\2\2\2h\u029d\3\2\2\2j\u02a1\3\2\2\2l\u02a6\3\2\2\2n\u02a8\3\2\2\2p\u02aa"+
		"\3\2\2\2r\u02ac\3\2\2\2t\u02b2\3\2\2\2v\u02b4\3\2\2\2x\u02b7\3\2\2\2z"+
		"\u02ba\3\2\2\2|\u02be\3\2\2\2~\u02c0\3\2\2\2\u0080\u02c2\3\2\2\2\u0082"+
		"\u02c4\3\2\2\2\u0084\u02c6\3\2\2\2\u0086\u02c8\3\2\2\2\u0088\u02ca\3\2"+
		"\2\2\u008a\u02d1\3\2\2\2\u008c\u02d5\3\2\2\2\u008e\u02d9\3\2\2\2\u0090"+
		"\u02dd\3\2\2\2\u0092\u02ec\3\2\2\2\u0094\u02ef\3\2\2\2\u0096\u02f4\3\2"+
		"\2\2\u0098\u0301\3\2\2\2\u009a\u0303\3\2\2\2\u009c\u0306\3\2\2\2\u009e"+
		"\u0313\3\2\2\2\u00a0\u0321\3\2\2\2\u00a2\u0329\3\2\2\2\u00a4\u0331\3\2"+
		"\2\2\u00a6\u0333\3\2\2\2\u00a8\u0339\3\2\2\2\u00aa\u033f\3\2\2\2\u00ac"+
		"\u0341\3\2\2\2\u00ae\u034d\3\2\2\2\u00b0\u0358\3\2\2\2\u00b2\u035c\3\2"+
		"\2\2\u00b4\u036b\3\2\2\2\u00b6\u036d\3\2\2\2\u00b8\u0371\3\2\2\2\u00ba"+
		"\u0373\3\2\2\2\u00bc\u037f\3\2\2\2\u00be\u038f\3\2\2\2\u00c0\u0391\3\2"+
		"\2\2\u00c2\u0398\3\2\2\2\u00c4\u039a\3\2\2\2\u00c6\u03a0\3\2\2\2\u00c8"+
		"\u03a2\3\2\2\2\u00ca\u03a8\3\2\2\2\u00cc\u03aa\3\2\2\2\u00ce\u03ae\3\2"+
		"\2\2\u00d0\u03b2\3\2\2\2\u00d2\u03c2\3\2\2\2\u00d4\u03c4\3\2\2\2\u00d6"+
		"\u03d3\3\2\2\2\u00d8\u03da\3\2\2\2\u00da\u03dc\3\2\2\2\u00dc\u03df\3\2"+
		"\2\2\u00de\u03e2\3\2\2\2\u00e0\u03ef\3\2\2\2\u00e2\u03f1\3\2\2\2\u00e4"+
		"\u03f4\3\2\2\2\u00e6\u03f9\3\2\2\2\u00e8\u0401\3\2\2\2\u00ea\u040a\3\2"+
		"\2\2\u00ec\u0410\3\2\2\2\u00ee\u0425\3\2\2\2\u00f0\u0427\3\2\2\2\u00f2"+
		"\u042d\3\2\2\2\u00f4\u042f\3\2\2\2\u00f6\u0436\3\2\2\2\u00f8\u043a\3\2"+
		"\2\2\u00fa\u043e\3\2\2\2\u00fc\u0442\3\2\2\2\u00fe\u0445\3\2\2\2\u0100"+
		"\u0452\3\2\2\2\u0102\u0455\3\2\2\2\u0104\u0458\3\2\2\2\u0106\u0468\3\2"+
		"\2\2\u0108\u046a\3\2\2\2\u010a\u0474\3\2\2\2\u010c\u0481\3\2\2\2\u010e"+
		"\u0484\3\2\2\2\u0110\u0489\3\2\2\2\u0112\u0496\3\2\2\2\u0114\u04a1\3\2"+
		"\2\2\u0116\u0118\5\u00c8e\2\u0117\u0116\3\2\2\2\u0118\u011b\3\2\2\2\u0119"+
		"\u0117\3\2\2\2\u0119\u011a\3\2\2\2\u011a\u011d\3\2\2\2\u011b\u0119\3\2"+
		"\2\2\u011c\u011e\5\4\3\2\u011d\u011c\3\2\2\2\u011e\u011f\3\2\2\2\u011f"+
		"\u011d\3\2\2\2\u011f\u0120\3\2\2\2\u0120\3\3\2\2\2\u0121\u0122\5R*\2\u0122"+
		"\u0123\7\r\2\2\u0123\u0143\3\2\2\2\u0124\u0125\5\66\34\2\u0125\u0126\7"+
		"\r\2\2\u0126\u0143\3\2\2\2\u0127\u0128\5\u00acW\2\u0128\u0129\7\r\2\2"+
		"\u0129\u0143\3\2\2\2\u012a\u012b\5\b\5\2\u012b\u012c\7\r\2\2\u012c\u0143"+
		"\3\2\2\2\u012d\u012e\5\6\4\2\u012e\u012f\7\r\2\2\u012f\u0143\3\2\2\2\u0130"+
		"\u0131\5\32\16\2\u0131\u0132\7\r\2\2\u0132\u0143\3\2\2\2\u0133\u0134\5"+
		"\u00ccg\2\u0134\u0135\7\r\2\2\u0135\u0143\3\2\2\2\u0136\u0137\5\u00ce"+
		"h\2\u0137\u0138\7\r\2\2\u0138\u0143\3\2\2\2\u0139\u013a\5\u010c\u0087"+
		"\2\u013a\u013b\7\r\2\2\u013b\u0143\3\2\2\2\u013c\u013d\5\u00e0q\2\u013d"+
		"\u013e\7\r\2\2\u013e\u0143\3\2\2\2\u013f\u0140\5\u00fc\177\2\u0140\u0141"+
		"\7\r\2\2\u0141\u0143\3\2\2\2\u0142\u0121\3\2\2\2\u0142\u0124\3\2\2\2\u0142"+
		"\u0127\3\2\2\2\u0142\u012a\3\2\2\2\u0142\u012d\3\2\2\2\u0142\u0130\3\2"+
		"\2\2\u0142\u0133\3\2\2\2\u0142\u0136\3\2\2\2\u0142\u0139\3\2\2\2\u0142"+
		"\u013c\3\2\2\2\u0142\u013f\3\2\2\2\u0143\5\3\2\2\2\u0144\u0145\7K\2\2"+
		"\u0145\u0146\7d\2\2\u0146\u0148\7\20\2\2\u0147\u0149\5\4\3\2\u0148\u0147"+
		"\3\2\2\2\u0149\u014a\3\2\2\2\u014a\u0148\3\2\2\2\u014a\u014b\3\2\2\2\u014b"+
		"\u014c\3\2\2\2\u014c\u014d\7\21\2\2\u014d\7\3\2\2\2\u014e\u0151\5\n\6"+
		"\2\u014f\u0151\5\f\7\2\u0150\u014e\3\2\2\2\u0150\u014f\3\2\2\2\u0151\t"+
		"\3\2\2\2\u0152\u0153\5\16\b\2\u0153\u0154\7\20\2\2\u0154\u0155\5\20\t"+
		"\2\u0155\u0156\7\21\2\2\u0156\13\3\2\2\2\u0157\u0159\t\2\2\2\u0158\u0157"+
		"\3\2\2\2\u0158\u0159\3\2\2\2\u0159\u015a\3\2\2\2\u015a\u015b\7b\2\2\u015b"+
		"\u015c\7d\2\2\u015c\r\3\2\2\2\u015d\u015f\t\2\2\2\u015e\u015d\3\2\2\2"+
		"\u015e\u015f\3\2\2\2\u015f\u0160\3\2\2\2\u0160\u0161\7b\2\2\u0161\u0163"+
		"\7d\2\2\u0162\u0164\5\24\13\2\u0163\u0162\3\2\2\2\u0163\u0164\3\2\2\2"+
		"\u0164\17\3\2\2\2\u0165\u0167\5\22\n\2\u0166\u0165\3\2\2\2\u0167\u016a"+
		"\3\2\2\2\u0168\u0166\3\2\2\2\u0168\u0169\3\2\2\2\u0169\21\3\2\2\2\u016a"+
		"\u0168\3\2\2\2\u016b\u016c\5R*\2\u016c\u016d\7\r\2\2\u016d\u0181\3\2\2"+
		"\2\u016e\u016f\5\66\34\2\u016f\u0170\7\r\2\2\u0170\u0181\3\2\2\2\u0171"+
		"\u0172\5\u00acW\2\u0172\u0173\7\r\2\2\u0173\u0181\3\2\2\2\u0174\u0175"+
		"\5\u00aaV\2\u0175\u0176\7\r\2\2\u0176\u0181\3\2\2\2\u0177\u0178\5\u00ae"+
		"X\2\u0178\u0179\7\r\2\2\u0179\u0181\3\2\2\2\u017a\u017b\5\u00ccg\2\u017b"+
		"\u017c\7\r\2\2\u017c\u0181\3\2\2\2\u017d\u017e\5\u00ceh\2\u017e\u017f"+
		"\7\r\2\2\u017f\u0181\3\2\2\2\u0180\u016b\3\2\2\2\u0180\u016e\3\2\2\2\u0180"+
		"\u0171\3\2\2\2\u0180\u0174\3\2\2\2\u0180\u0177\3\2\2\2\u0180\u017a\3\2"+
		"\2\2\u0180\u017d\3\2\2\2\u0181\23\3\2\2\2\u0182\u0183\7\16\2\2\u0183\u0188"+
		"\5\26\f\2\u0184\u0185\7\17\2\2\u0185\u0187\5\26\f\2\u0186\u0184\3\2\2"+
		"\2\u0187\u018a\3\2\2\2\u0188\u0186\3\2\2\2\u0188\u0189\3\2\2\2\u0189\25"+
		"\3\2\2\2\u018a\u0188\3\2\2\2\u018b\u018c\5\30\r\2\u018c\27\3\2\2\2\u018d"+
		"\u018f\7\"\2\2\u018e\u018d\3\2\2\2\u018e\u018f\3\2\2\2\u018f\u0190\3\2"+
		"\2\2\u0190\u0195\7d\2\2\u0191\u0192\7\"\2\2\u0192\u0194\7d\2\2\u0193\u0191"+
		"\3\2\2\2\u0194\u0197\3\2\2\2\u0195\u0193\3\2\2\2\u0195\u0196\3\2\2\2\u0196"+
		"\31\3\2\2\2\u0197\u0195\3\2\2\2\u0198\u019d\5\"\22\2\u0199\u019d\5 \21"+
		"\2\u019a\u019d\5\36\20\2\u019b\u019d\5\34\17\2\u019c\u0198\3\2\2\2\u019c"+
		"\u0199\3\2\2\2\u019c\u019a\3\2\2\2\u019c\u019b\3\2\2\2\u019d\33\3\2\2"+
		"\2\u019e\u01a0\7X\2\2\u019f\u019e\3\2\2\2\u019f\u01a0\3\2\2\2\u01a0\u01a1"+
		"\3\2\2\2\u01a1\u01a2\7I\2\2\u01a2\u01a3\7d\2\2\u01a3\35\3\2\2\2\u01a4"+
		"\u01a5\7I\2\2\u01a5\u01a6\7d\2\2\u01a6\u01a7\5V,\2\u01a7\37\3\2\2\2\u01a8"+
		"\u01a9\7X\2\2\u01a9\u01aa\7I\2\2\u01aa\u01ab\7d\2\2\u01ab\u01ac\5&\24"+
		"\2\u01ac\u01b0\7\20\2\2\u01ad\u01af\5\22\n\2\u01ae\u01ad\3\2\2\2\u01af"+
		"\u01b2\3\2\2\2\u01b0\u01ae\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1\u01b3\3\2"+
		"\2\2\u01b2\u01b0\3\2\2\2\u01b3\u01b4\7\21\2\2\u01b4!\3\2\2\2\u01b5\u01b6"+
		"\5$\23\2\u01b6\u01ba\7\20\2\2\u01b7\u01b9\5*\26\2\u01b8\u01b7\3\2\2\2"+
		"\u01b9\u01bc\3\2\2\2\u01ba\u01b8\3\2\2\2\u01ba\u01bb\3\2\2\2\u01bb\u01bd"+
		"\3\2\2\2\u01bc\u01ba\3\2\2\2\u01bd\u01be\7\21\2\2\u01be#\3\2\2\2\u01bf"+
		"\u01c1\7.\2\2\u01c0\u01bf\3\2\2\2\u01c0\u01c1\3\2\2\2\u01c1\u01c2\3\2"+
		"\2\2\u01c2\u01c3\7I\2\2\u01c3\u01c4\7d\2\2\u01c4\u01c5\5&\24\2\u01c5%"+
		"\3\2\2\2\u01c6\u01c8\7\16\2\2\u01c7\u01c9\7M\2\2\u01c8\u01c7\3\2\2\2\u01c8"+
		"\u01c9\3\2\2\2\u01c9\u01ca\3\2\2\2\u01ca\u01cf\5(\25\2\u01cb\u01cc\7\17"+
		"\2\2\u01cc\u01ce\5(\25\2\u01cd\u01cb\3\2\2\2\u01ce\u01d1\3\2\2\2\u01cf"+
		"\u01cd\3\2\2\2\u01cf\u01d0\3\2\2\2\u01d0\u01d3\3\2\2\2\u01d1\u01cf\3\2"+
		"\2\2\u01d2\u01c6\3\2\2\2\u01d2\u01d3\3\2\2\2\u01d3\u01dd\3\2\2\2\u01d4"+
		"\u01d5\7J\2\2\u01d5\u01da\5\26\f\2\u01d6\u01d7\7\17\2\2\u01d7\u01d9\5"+
		"\26\f\2\u01d8\u01d6\3\2\2\2\u01d9\u01dc\3\2\2\2\u01da\u01d8\3\2\2\2\u01da"+
		"\u01db\3\2\2\2\u01db\u01de\3\2\2\2\u01dc\u01da\3\2\2\2\u01dd\u01d4\3\2"+
		"\2\2\u01dd\u01de\3\2\2\2\u01de\'\3\2\2\2\u01df\u01e0\5\30\r\2\u01e0)\3"+
		"\2\2\2\u01e1\u01e5\5\22\n\2\u01e2\u01e5\5,\27\2\u01e3\u01e5\5.\30\2\u01e4"+
		"\u01e1\3\2\2\2\u01e4\u01e2\3\2\2\2\u01e4\u01e3\3\2\2\2\u01e5+\3\2\2\2"+
		"\u01e6\u01e7\t\3\2\2\u01e7\u01e8\5V,\2\u01e8\u01e9\5`\61\2\u01e9\u01ea"+
		"\7\r\2\2\u01ea-\3\2\2\2\u01eb\u01ec\7D\2\2\u01ec\u01ed\7d\2\2\u01ed\u01ef"+
		"\7\22\2\2\u01ee\u01f0\5\60\31\2\u01ef\u01ee\3\2\2\2\u01ef\u01f0\3\2\2"+
		"\2\u01f0\u01f1\3\2\2\2\u01f1\u01f3\7\23\2\2\u01f2\u01f4\5\u00ba^\2\u01f3"+
		"\u01f2\3\2\2\2\u01f3\u01f4\3\2\2\2\u01f4\u01f5\3\2\2\2\u01f5\u01f6\7\r"+
		"\2\2\u01f6/\3\2\2\2\u01f7\u01fc\5\62\32\2\u01f8\u01f9\7\17\2\2\u01f9\u01fb"+
		"\5\62\32\2\u01fa\u01f8\3\2\2\2\u01fb\u01fe\3\2\2\2\u01fc\u01fa\3\2\2\2"+
		"\u01fc\u01fd\3\2\2\2\u01fd\61\3\2\2\2\u01fe\u01fc\3\2\2\2\u01ff\u0200"+
		"\5\64\33\2\u0200\u0201\5\u00be`\2\u0201\u0202\5d\63\2\u0202\63\3\2\2\2"+
		"\u0203\u0204\7;\2\2\u0204\65\3\2\2\2\u0205\u0206\7G\2\2\u0206\u0207\5"+
		"8\35\2\u0207\u0208\7d\2\2\u0208\u0209\7 \2\2\u0209\u020a\5:\36\2\u020a"+
		"\67\3\2\2\2\u020b\u0216\5j\66\2\u020c\u0216\5|?\2\u020d\u0216\5~@\2\u020e"+
		"\u0216\5\u0080A\2\u020f\u0216\5h\65\2\u0210\u0216\5\u00a2R\2\u0211\u0216"+
		"\5\u00a4S\2\u0212\u0216\5\u00c2b\2\u0213\u0216\5\30\r\2\u0214\u0216\5"+
		"\u0082B\2\u0215\u020b\3\2\2\2\u0215\u020c\3\2\2\2\u0215\u020d\3\2\2\2"+
		"\u0215\u020e\3\2\2\2\u0215\u020f\3\2\2\2\u0215\u0210\3\2\2\2\u0215\u0211"+
		"\3\2\2\2\u0215\u0212\3\2\2\2\u0215\u0213\3\2\2\2\u0215\u0214\3\2\2\2\u0216"+
		"9\3\2\2\2\u0217\u0218\5<\37\2\u0218;\3\2\2\2\u0219\u021e\5> \2\u021a\u021b"+
		"\7\37\2\2\u021b\u021d\5> \2\u021c\u021a\3\2\2\2\u021d\u0220\3\2\2\2\u021e"+
		"\u021c\3\2\2\2\u021e\u021f\3\2\2\2\u021f=\3\2\2\2\u0220\u021e\3\2\2\2"+
		"\u0221\u0226\5@!\2\u0222\u0223\7\35\2\2\u0223\u0225\5@!\2\u0224\u0222"+
		"\3\2\2\2\u0225\u0228\3\2\2\2\u0226\u0224\3\2\2\2\u0226\u0227\3\2\2\2\u0227"+
		"?\3\2\2\2\u0228\u0226\3\2\2\2\u0229\u022e\5B\"\2\u022a\u022b\7\36\2\2"+
		"\u022b\u022d\5B\"\2\u022c\u022a\3\2\2\2\u022d\u0230\3\2\2\2\u022e\u022c"+
		"\3\2\2\2\u022e\u022f\3\2\2\2\u022fA\3\2\2\2\u0230\u022e\3\2\2\2\u0231"+
		"\u0236\5D#\2\u0232\u0233\t\4\2\2\u0233\u0235\5D#\2\u0234\u0232\3\2\2\2"+
		"\u0235\u0238\3\2\2\2\u0236\u0234\3\2\2\2\u0236\u0237\3\2\2\2\u0237C\3"+
		"\2\2\2\u0238\u0236\3\2\2\2\u0239\u023e\5F$\2\u023a\u023b\t\5\2\2\u023b"+
		"\u023d\5F$\2\u023c\u023a\3\2\2\2\u023d\u0240\3\2\2\2\u023e\u023c\3\2\2"+
		"\2\u023e\u023f\3\2\2\2\u023fE\3\2\2\2\u0240\u023e\3\2\2\2\u0241\u0246"+
		"\5H%\2\u0242\u0243\t\6\2\2\u0243\u0245\5H%\2\u0244\u0242\3\2\2\2\u0245"+
		"\u0248\3\2\2\2\u0246\u0244\3\2\2\2\u0246\u0247\3\2\2\2\u0247G\3\2\2\2"+
		"\u0248\u0246\3\2\2\2\u0249\u024a\5J&\2\u024a\u024b\5L\'\2\u024b\u024e"+
		"\3\2\2\2\u024c\u024e\5L\'\2\u024d\u0249\3\2\2\2\u024d\u024c\3\2\2\2\u024e"+
		"I\3\2\2\2\u024f\u0250\t\7\2\2\u0250K\3\2\2\2\u0251\u0258\5\30\r\2\u0252"+
		"\u0258\5N(\2\u0253\u0254\7\22\2\2\u0254\u0255\5:\36\2\u0255\u0256\7\23"+
		"\2\2\u0256\u0258\3\2\2\2\u0257\u0251\3\2\2\2\u0257\u0252\3\2\2\2\u0257"+
		"\u0253\3\2\2\2\u0258M\3\2\2\2\u0259\u025a\t\b\2\2\u025aO\3\2\2\2\u025b"+
		"\u025c\5:\36\2\u025cQ\3\2\2\2\u025d\u025e\7+\2\2\u025e\u0266\5T+\2\u025f"+
		"\u0266\5\u0088E\2\u0260\u0266\5\u0090I\2\u0261\u0266\5\u009cO\2\u0262"+
		"\u0263\7\63\2\2\u0263\u0266\5d\63\2\u0264\u0266\5\u00c6d\2\u0265\u025d"+
		"\3\2\2\2\u0265\u025f\3\2\2\2\u0265\u0260\3\2\2\2\u0265\u0261\3\2\2\2\u0265"+
		"\u0262\3\2\2\2\u0265\u0264\3\2\2\2\u0266S\3\2\2\2\u0267\u0268\5V,\2\u0268"+
		"\u0269\5`\61\2\u0269U\3\2\2\2\u026a\u026d\5X-\2\u026b\u026d\5^\60\2\u026c"+
		"\u026a\3\2\2\2\u026c\u026b\3\2\2\2\u026dW\3\2\2\2\u026e\u0272\5Z.\2\u026f"+
		"\u0272\5\\/\2\u0270\u0272\5\30\r\2\u0271\u026e\3\2\2\2\u0271\u026f\3\2"+
		"\2\2\u0271\u0270\3\2\2\2\u0272Y\3\2\2\2\u0273\u027d\5h\65\2\u0274\u027d"+
		"\5j\66\2\u0275\u027d\5|?\2\u0276\u027d\5~@\2\u0277\u027d\5\u0080A\2\u0278"+
		"\u027d\5\u0082B\2\u0279\u027d\5\u0084C\2\u027a\u027d\5\u0086D\2\u027b"+
		"\u027d\5\u00c4c\2\u027c\u0273\3\2\2\2\u027c\u0274\3\2\2\2\u027c\u0275"+
		"\3\2\2\2\u027c\u0276\3\2\2\2\u027c\u0277\3\2\2\2\u027c\u0278\3\2\2\2\u027c"+
		"\u0279\3\2\2\2\u027c\u027a\3\2\2\2\u027c\u027b\3\2\2\2\u027d[\3\2\2\2"+
		"\u027e\u0283\5\u00a0Q\2\u027f\u0283\5\u00a2R\2\u0280\u0283\5\u00a4S\2"+
		"\u0281\u0283\5\u00c0a\2\u0282\u027e\3\2\2\2\u0282\u027f\3\2\2\2\u0282"+
		"\u0280\3\2\2\2\u0282\u0281\3\2\2\2\u0283]\3\2\2\2\u0284\u0288\5\u0088"+
		"E\2\u0285\u0288\5\u0090I\2\u0286\u0288\5\u009cO\2\u0287\u0284\3\2\2\2"+
		"\u0287\u0285\3\2\2\2\u0287\u0286\3\2\2\2\u0288_\3\2\2\2\u0289\u028e\5"+
		"b\62\2\u028a\u028b\7\17\2\2\u028b\u028d\5b\62\2\u028c\u028a\3\2\2\2\u028d"+
		"\u0290\3\2\2\2\u028e\u028c\3\2\2\2\u028e\u028f\3\2\2\2\u028fa\3\2\2\2"+
		"\u0290\u028e\3\2\2\2\u0291\u0294\5d\63\2\u0292\u0294\5f\64\2\u0293\u0291"+
		"\3\2\2\2\u0293\u0292\3\2\2\2\u0294c\3\2\2\2\u0295\u0296\7d\2\2\u0296e"+
		"\3\2\2\2\u0297\u0298\5\u00a6T\2\u0298g\3\2\2\2\u0299\u029e\7U\2\2\u029a"+
		"\u029e\7\\\2\2\u029b\u029c\7?\2\2\u029c\u029e\7\\\2\2\u029d\u0299\3\2"+
		"\2\2\u029d\u029a\3\2\2\2\u029d\u029b\3\2\2\2\u029ei\3\2\2\2\u029f\u02a2"+
		"\5l\67\2\u02a0\u02a2\5t;\2\u02a1\u029f\3\2\2\2\u02a1\u02a0\3\2\2\2\u02a2"+
		"k\3\2\2\2\u02a3\u02a7\5n8\2\u02a4\u02a7\5p9\2\u02a5\u02a7\5r:\2\u02a6"+
		"\u02a3\3\2\2\2\u02a6\u02a4\3\2\2\2\u02a6\u02a5\3\2\2\2\u02a7m\3\2\2\2"+
		"\u02a8\u02a9\7>\2\2\u02a9o\3\2\2\2\u02aa\u02ab\7?\2\2\u02abq\3\2\2\2\u02ac"+
		"\u02ad\7?\2\2\u02ad\u02ae\7?\2\2\u02aes\3\2\2\2\u02af\u02b3\5v<\2\u02b0"+
		"\u02b3\5x=\2\u02b1\u02b3\5z>\2\u02b2\u02af\3\2\2\2\u02b2\u02b0\3\2\2\2"+
		"\u02b2\u02b1\3\2\2\2\u02b3u\3\2\2\2\u02b4\u02b5\7N\2\2\u02b5\u02b6\7>"+
		"\2\2\u02b6w\3\2\2\2\u02b7\u02b8\7N\2\2\u02b8\u02b9\7?\2\2\u02b9y\3\2\2"+
		"\2\u02ba\u02bb\7N\2\2\u02bb\u02bc\7?\2\2\u02bc\u02bd\7?\2\2\u02bd{\3\2"+
		"\2\2\u02be\u02bf\7S\2\2\u02bf}\3\2\2\2\u02c0\u02c1\7:\2\2\u02c1\177\3"+
		"\2\2\2\u02c2\u02c3\7V\2\2\u02c3\u0081\3\2\2\2\u02c4\u02c5\7/\2\2\u02c5"+
		"\u0083\3\2\2\2\u02c6\u02c7\7R\2\2\u02c7\u0085\3\2\2\2\u02c8\u02c9\7L\2"+
		"\2\u02c9\u0087\3\2\2\2\u02ca\u02cb\7\62\2\2\u02cb\u02cc\7d\2\2\u02cc\u02cd"+
		"\7\20\2\2\u02cd\u02ce\5\u008aF\2\u02ce\u02cf\7\21\2\2\u02cf\u0089\3\2"+
		"\2\2\u02d0\u02d2\5\u008cG\2\u02d1\u02d0\3\2\2\2\u02d2\u02d3\3\2\2\2\u02d3"+
		"\u02d1\3\2\2\2\u02d3\u02d4\3\2\2\2\u02d4\u008b\3\2\2\2\u02d5\u02d6\5V"+
		",\2\u02d6\u02d7\5`\61\2\u02d7\u02d8\7\r\2\2\u02d8\u008d\3\2\2\2\u02d9"+
		"\u02da\7\27\2\2\u02da\u02db\7\27\2\2\u02db\u02dc\7d\2\2\u02dc\u008f\3"+
		"\2\2\2\u02dd\u02de\7P\2\2\u02de\u02df\7d\2\2\u02df\u02e0\7)\2\2\u02e0"+
		"\u02e1\7\22\2\2\u02e1\u02e2\5\u0092J\2\u02e2\u02e3\7\23\2\2\u02e3\u02e4"+
		"\7\20\2\2\u02e4\u02e5\5\u0094K\2\u02e5\u02e6\7\21\2\2\u02e6\u0091\3\2"+
		"\2\2\u02e7\u02ed\5j\66\2\u02e8\u02ed\5|?\2\u02e9\u02ed\5\u0080A\2\u02ea"+
		"\u02ed\5\u009cO\2\u02eb\u02ed\5\30\r\2\u02ec\u02e7\3\2\2\2\u02ec\u02e8"+
		"\3\2\2\2\u02ec\u02e9\3\2\2\2\u02ec\u02ea\3\2\2\2\u02ec\u02eb\3\2\2\2\u02ed"+
		"\u0093\3\2\2\2\u02ee\u02f0\5\u0096L\2\u02ef\u02ee\3\2\2\2\u02f0\u02f1"+
		"\3\2\2\2\u02f1\u02ef\3\2\2\2\u02f1\u02f2\3\2\2\2\u02f2\u0095\3\2\2\2\u02f3"+
		"\u02f5\5\u0098M\2\u02f4\u02f3\3\2\2\2\u02f5\u02f6\3\2\2\2\u02f6\u02f4"+
		"\3\2\2\2\u02f6\u02f7\3\2\2\2\u02f7\u02f8\3\2\2\2\u02f8\u02f9\5\u009aN"+
		"\2\u02f9\u02fa\7\r\2\2\u02fa\u0097\3\2\2\2\u02fb\u02fc\7T\2\2\u02fc\u02fd"+
		"\5:\36\2\u02fd\u02fe\7\16\2\2\u02fe\u0302\3\2\2\2\u02ff\u0300\7<\2\2\u0300"+
		"\u0302\7\16\2\2\u0301\u02fb\3\2\2\2\u0301\u02ff\3\2\2\2\u0302\u0099\3"+
		"\2\2\2\u0303\u0304\5V,\2\u0304\u0305\5b\62\2\u0305\u009b\3\2\2\2\u0306"+
		"\u0307\7@\2\2\u0307\u0308\7d\2\2\u0308\u0309\7\20\2\2\u0309\u030e\5\u009e"+
		"P\2\u030a\u030b\7\17\2\2\u030b\u030d\5\u009eP\2\u030c\u030a\3\2\2\2\u030d"+
		"\u0310\3\2\2\2\u030e\u030c\3\2\2\2\u030e\u030f\3\2\2\2\u030f\u0311\3\2"+
		"\2\2\u0310\u030e\3\2\2\2\u0311\u0312\7\21\2\2\u0312\u009d\3\2\2\2\u0313"+
		"\u0314\7d\2\2\u0314\u009f\3\2\2\2\u0315\u0316\7\60\2\2\u0316\u0317\7\30"+
		"\2\2\u0317\u0318\5X-\2\u0318\u0319\7\17\2\2\u0319\u031a\5P)\2\u031a\u031b"+
		"\7\31\2\2\u031b\u0322\3\2\2\2\u031c\u031d\7\60\2\2\u031d\u031e\7\30\2"+
		"\2\u031e\u031f\5X-\2\u031f\u0320\7\31\2\2\u0320\u0322\3\2\2\2\u0321\u0315"+
		"\3\2\2\2\u0321\u031c\3\2\2\2\u0322\u00a1\3\2\2\2\u0323\u0324\7(\2\2\u0324"+
		"\u0325\7\30\2\2\u0325\u0326\5P)\2\u0326\u0327\7\31\2\2\u0327\u032a\3\2"+
		"\2\2\u0328\u032a\7(\2\2\u0329\u0323\3\2\2\2\u0329\u0328\3\2\2\2\u032a"+
		"\u00a3\3\2\2\2\u032b\u032c\7A\2\2\u032c\u032d\7\30\2\2\u032d\u032e\5P"+
		")\2\u032e\u032f\7\31\2\2\u032f\u0332\3\2\2\2\u0330\u0332\7A\2\2\u0331"+
		"\u032b\3\2\2\2\u0331\u0330\3\2\2\2\u0332\u00a5\3\2\2\2\u0333\u0335\7d"+
		"\2\2\u0334\u0336\5\u00a8U\2\u0335\u0334\3\2\2\2\u0336\u0337\3\2\2\2\u0337"+
		"\u0335\3\2\2\2\u0337\u0338\3\2\2\2\u0338\u00a7\3\2\2\2\u0339\u033a\7\24"+
		"\2\2\u033a\u033b\5P)\2\u033b\u033c\7\25\2\2\u033c\u00a9\3\2\2\2\u033d"+
		"\u0340\5\u00d0i\2\u033e\u0340\5\u00d4k\2\u033f\u033d\3\2\2\2\u033f\u033e"+
		"\3\2\2\2\u0340\u00ab\3\2\2\2\u0341\u0342\7E\2\2\u0342\u0343\7d\2\2\u0343"+
		"\u0347\7\20\2\2\u0344\u0346\5\u008cG\2\u0345\u0344\3\2\2\2\u0346\u0349"+
		"\3\2\2\2\u0347\u0345\3\2\2\2\u0347\u0348\3\2\2\2\u0348\u034a\3\2\2\2\u0349"+
		"\u0347\3\2\2\2\u034a\u034b\7\21\2\2\u034b\u00ad\3\2\2\2\u034c\u034e\5"+
		"\u00b0Y\2\u034d\u034c\3\2\2\2\u034d\u034e\3\2\2\2\u034e\u034f\3\2\2\2"+
		"\u034f\u0350\5\u00b2Z\2\u0350\u0351\7d\2\2\u0351\u0353\5\u00b4[\2\u0352"+
		"\u0354\5\u00ba^\2\u0353\u0352\3\2\2\2\u0353\u0354\3\2\2\2\u0354\u0356"+
		"\3\2\2\2\u0355\u0357\5\u00bc_\2\u0356\u0355\3\2\2\2\u0356\u0357\3\2\2"+
		"\2\u0357\u00af\3\2\2\2\u0358\u0359\7Q\2\2\u0359\u00b1\3\2\2\2\u035a\u035d"+
		"\5\u00be`\2\u035b\u035d\7\67\2\2\u035c\u035a\3\2\2\2\u035c\u035b\3\2\2"+
		"\2\u035d\u00b3\3\2\2\2\u035e\u035f\7\22\2\2\u035f\u0364\5\u00b6\\\2\u0360"+
		"\u0361\7\17\2\2\u0361\u0363\5\u00b6\\\2\u0362\u0360\3\2\2\2\u0363\u0366"+
		"\3\2\2\2\u0364\u0362\3\2\2\2\u0364\u0365\3\2\2\2\u0365\u0367\3\2\2\2\u0366"+
		"\u0364\3\2\2\2\u0367\u0368\7\23\2\2\u0368\u036c\3\2\2\2\u0369\u036a\7"+
		"\22\2\2\u036a\u036c\7\23\2\2\u036b\u035e\3\2\2\2\u036b\u0369\3\2\2\2\u036c"+
		"\u00b5\3\2\2\2\u036d\u036e\5\u00b8]\2\u036e\u036f\5\u00be`\2\u036f\u0370"+
		"\5d\63\2\u0370\u00b7\3\2\2\2\u0371\u0372\t\t\2\2\u0372\u00b9\3\2\2\2\u0373"+
		"\u0374\7\66\2\2\u0374\u0375\7\22\2\2\u0375\u037a\5\30\r\2\u0376\u0377"+
		"\7\17\2\2\u0377\u0379\5\30\r\2\u0378\u0376\3\2\2\2\u0379\u037c\3\2\2\2"+
		"\u037a\u0378\3\2\2\2\u037a\u037b\3\2\2\2\u037b\u037d\3\2\2\2\u037c\u037a"+
		"\3\2\2\2\u037d\u037e\7\23\2\2\u037e\u00bb\3\2\2\2\u037f\u0380\7B\2\2\u0380"+
		"\u0381\7\22\2\2\u0381\u0386\7\13\2\2\u0382\u0383\7\17\2\2\u0383\u0385"+
		"\7\13\2\2\u0384\u0382\3\2\2\2\u0385\u0388\3\2\2\2\u0386\u0384\3\2\2\2"+
		"\u0386\u0387\3\2\2\2\u0387\u0389\3\2\2\2\u0388\u0386\3\2\2\2\u0389\u038a"+
		"\7\23\2\2\u038a\u00bd\3\2\2\2\u038b\u0390\5Z.\2\u038c\u0390\5\u00a2R\2"+
		"\u038d\u0390\5\u00a4S\2\u038e\u0390\5\30\r\2\u038f\u038b\3\2\2\2\u038f"+
		"\u038c\3\2\2\2\u038f\u038d\3\2\2\2\u038f\u038e\3\2\2\2\u0390\u00bf\3\2"+
		"\2\2\u0391\u0392\7O\2\2\u0392\u0393\7\30\2\2\u0393\u0394\5P)\2\u0394\u0395"+
		"\7\17\2\2\u0395\u0396\5P)\2\u0396\u0397\7\31\2\2\u0397\u00c1\3\2\2\2\u0398"+
		"\u0399\7O\2\2\u0399\u00c3\3\2\2\2\u039a\u039b\7H\2\2\u039b\u00c5\3\2\2"+
		"\2\u039c\u039d\7\62\2\2\u039d\u03a1\7d\2\2\u039e\u039f\7P\2\2\u039f\u03a1"+
		"\7d\2\2\u03a0\u039c\3\2\2\2\u03a0\u039e\3\2\2\2\u03a1\u00c7\3\2\2\2\u03a2"+
		"\u03a3\7\61\2\2\u03a3\u03a4\5\u00caf\2\u03a4\u03a5\7\r\2\2\u03a5\u00c9"+
		"\3\2\2\2\u03a6\u03a9\5\30\r\2\u03a7\u03a9\7\13\2\2\u03a8\u03a6\3\2\2\2"+
		"\u03a8\u03a7\3\2\2\2\u03a9\u00cb\3\2\2\2\u03aa\u03ab\7^\2\2\u03ab\u03ac"+
		"\5\30\r\2\u03ac\u03ad\7\13\2\2\u03ad\u00cd\3\2\2\2\u03ae\u03af\7]\2\2"+
		"\u03af\u03b0\5\30\r\2\u03b0\u03b1\7\13\2\2\u03b1\u00cf\3\2\2\2\u03b2\u03b3"+
		"\7\64\2\2\u03b3\u03b4\7_\2\2\u03b4\u03b5\5\u00be`\2\u03b5\u03b6\5\u00d2"+
		"j\2\u03b6\u00d1\3\2\2\2\u03b7\u03b8\5d\63\2\u03b8\u03b9\5\u00ba^\2\u03b9"+
		"\u03c3\3\2\2\2\u03ba\u03bf\5d\63\2\u03bb\u03bc\7\17\2\2\u03bc\u03be\5"+
		"d\63\2\u03bd\u03bb\3\2\2\2\u03be\u03c1\3\2\2\2\u03bf\u03bd\3\2\2\2\u03bf"+
		"\u03c0\3\2\2\2\u03c0\u03c3\3\2\2\2\u03c1\u03bf\3\2\2\2\u03c2\u03b7\3\2"+
		"\2\2\u03c2\u03ba\3\2\2\2\u03c3\u00d3\3\2\2\2\u03c4\u03c5\7_\2\2\u03c5"+
		"\u03c6\5\u00be`\2\u03c6\u03c7\5\u00d6l\2\u03c7\u00d5\3\2\2\2\u03c8\u03c9"+
		"\5d\63\2\u03c9\u03ca\5\u00d8m\2\u03ca\u03d4\3\2\2\2\u03cb\u03d0\5d\63"+
		"\2\u03cc\u03cd\7\17\2\2\u03cd\u03cf\5d\63\2\u03ce\u03cc\3\2\2\2\u03cf"+
		"\u03d2\3\2\2\2\u03d0\u03ce\3\2\2\2\u03d0\u03d1\3\2\2\2\u03d1\u03d4\3\2"+
		"\2\2\u03d2\u03d0\3\2\2\2\u03d3\u03c8\3\2\2\2\u03d3\u03cb\3\2\2\2\u03d4"+
		"\u00d7\3\2\2\2\u03d5\u03d7\5\u00dan\2\u03d6\u03d8\5\u00dco\2\u03d7\u03d6"+
		"\3\2\2\2\u03d7\u03d8\3\2\2\2\u03d8\u03db\3\2\2\2\u03d9\u03db\5\u00dco"+
		"\2\u03da\u03d5\3\2\2\2\u03da\u03d9\3\2\2\2\u03db\u00d9\3\2\2\2\u03dc\u03dd"+
		"\7F\2\2\u03dd\u03de\5\u00dep\2\u03de\u00db\3\2\2\2\u03df\u03e0\7%\2\2"+
		"\u03e0\u03e1\5\u00dep\2\u03e1\u00dd\3\2\2\2\u03e2\u03e3\7\22\2\2\u03e3"+
		"\u03e8\5\30\r\2\u03e4\u03e5\7\17\2\2\u03e5\u03e7\5\30\r\2\u03e6\u03e4"+
		"\3\2\2\2\u03e7\u03ea\3\2\2\2\u03e8\u03e6\3\2\2\2\u03e8\u03e9\3\2\2\2\u03e9"+
		"\u03eb\3\2\2\2\u03ea\u03e8\3\2\2\2\u03eb\u03ec\7\23\2\2\u03ec\u00df\3"+
		"\2\2\2\u03ed\u03f0\5\u00e4s\2\u03ee\u03f0\5\u00e2r\2\u03ef\u03ed\3\2\2"+
		"\2\u03ef\u03ee\3\2\2\2\u03f0\u00e1\3\2\2\2\u03f1\u03f2\7c\2\2\u03f2\u03f3"+
		"\7d\2\2\u03f3\u00e3\3\2\2\2\u03f4\u03f5\5\u00e6t\2\u03f5\u03f6\7\20\2"+
		"\2\u03f6\u03f7\5\u00ecw\2\u03f7\u03f8\7\21\2\2\u03f8\u00e5\3\2\2\2\u03f9"+
		"\u03fa\7c\2\2\u03fa\u03fc\7d\2\2\u03fb\u03fd\5\u00eav\2\u03fc\u03fb\3"+
		"\2\2\2\u03fc\u03fd\3\2\2\2\u03fd\u03ff\3\2\2\2\u03fe\u0400\5\u00e8u\2"+
		"\u03ff\u03fe\3\2\2\2\u03ff\u0400\3\2\2\2\u0400\u00e7\3\2\2\2\u0401\u0402"+
		"\7J\2\2\u0402\u0407\5\30\r\2\u0403\u0404\7\17\2\2\u0404\u0406\5\30\r\2"+
		"\u0405\u0403\3\2\2\2\u0406\u0409\3\2\2\2\u0407\u0405\3\2\2\2\u0407\u0408"+
		"\3\2\2\2\u0408\u00e9\3\2\2\2\u0409\u0407\3\2\2\2\u040a\u040b\7\16\2\2"+
		"\u040b\u040c\5\30\r\2\u040c\u00eb\3\2\2\2\u040d\u040f\5\u00eex\2\u040e"+
		"\u040d\3\2\2\2\u040f\u0412\3\2\2\2\u0410\u040e\3\2\2\2\u0410\u0411\3\2"+
		"\2\2\u0411\u00ed\3\2\2\2\u0412\u0410\3\2\2\2\u0413\u0414\5\u00f0y\2\u0414"+
		"\u0415\7\r\2\2\u0415\u0426\3\2\2\2\u0416\u0417\5\u00f4{\2\u0417\u0418"+
		"\7\r\2\2\u0418\u0426\3\2\2\2\u0419\u041a\5\u00f6|\2\u041a\u041b\7\r\2"+
		"\2\u041b\u0426\3\2\2\2\u041c\u041d\5\u00f8}\2\u041d\u041e\7\r\2\2\u041e"+
		"\u0426\3\2\2\2\u041f\u0420\5\u00fa~\2\u0420\u0421\7\r\2\2\u0421\u0426"+
		"\3\2\2\2\u0422\u0423\5\u00aaV\2\u0423\u0424\7\r\2\2\u0424\u0426\3\2\2"+
		"\2\u0425\u0413\3\2\2\2\u0425\u0416\3\2\2\2\u0425\u0419\3\2\2\2\u0425\u041c"+
		"\3\2\2\2\u0425\u041f\3\2\2\2\u0425\u0422\3\2\2\2\u0426\u00ef\3\2\2\2\u0427"+
		"\u0428\7Z\2\2\u0428\u0429\5\u00f2z\2\u0429\u042a\7d\2\2\u042a\u00f1\3"+
		"\2\2\2\u042b\u042e\5\30\r\2\u042c\u042e\7L\2\2\u042d\u042b\3\2\2\2\u042d"+
		"\u042c\3\2\2\2\u042e\u00f3\3\2\2\2\u042f\u0431\7,\2\2\u0430\u0432\7W\2"+
		"\2\u0431\u0430\3\2\2\2\u0431\u0432\3\2\2\2\u0432\u0433\3\2\2\2\u0433\u0434"+
		"\5\u00f2z\2\u0434\u0435\7d\2\2\u0435\u00f5\3\2\2\2\u0436\u0437\7\'\2\2"+
		"\u0437\u0438\5\30\r\2\u0438\u0439\7d\2\2\u0439\u00f7\3\2\2\2\u043a\u043b"+
		"\7*\2\2\u043b\u043c\5\30\r\2\u043c\u043d\7d\2\2\u043d\u00f9\3\2\2\2\u043e"+
		"\u043f\7[\2\2\u043f\u0440\5\30\r\2\u0440\u0441\7d\2\2\u0441\u00fb\3\2"+
		"\2\2\u0442\u0443\5\u00fe\u0080\2\u0443\u0444\5\u0104\u0083\2\u0444\u00fd"+
		"\3\2\2\2\u0445\u0446\7C\2\2\u0446\u0448\7d\2\2\u0447\u0449\5\u0100\u0081"+
		"\2\u0448\u0447\3\2\2\2\u0448\u0449\3\2\2\2\u0449\u044b\3\2\2\2\u044a\u044c"+
		"\5\u00e8u\2\u044b\u044a\3\2\2\2\u044b\u044c\3\2\2\2\u044c\u044d\3\2\2"+
		"\2\u044d\u044e\7a\2\2\u044e\u0450\5\30\r\2\u044f\u0451\5\u0102\u0082\2"+
		"\u0450\u044f\3\2\2\2\u0450\u0451\3\2\2\2\u0451\u00ff\3\2\2\2\u0452\u0453"+
		"\7\16\2\2\u0453\u0454\5\30\r\2\u0454\u0101\3\2\2\2\u0455\u0456\7-\2\2"+
		"\u0456\u0457\5\30\r\2\u0457\u0103\3\2\2\2\u0458\u045c\7\20\2\2\u0459\u045b"+
		"\5\u0106\u0084\2\u045a\u0459\3\2\2\2\u045b\u045e\3\2\2\2\u045c\u045a\3"+
		"\2\2\2\u045c\u045d\3\2\2\2\u045d\u045f\3\2\2\2\u045e\u045c\3\2\2\2\u045f"+
		"\u0460\7\21\2\2\u0460\u0105\3\2\2\2\u0461\u0469\5\22\n\2\u0462\u0463\5"+
		"\u0108\u0085\2\u0463\u0464\7\r\2\2\u0464\u0469\3\2\2\2\u0465\u0466\5\u010a"+
		"\u0086\2\u0466\u0467\7\r\2\2\u0467\u0469\3\2\2\2\u0468\u0461\3\2\2\2\u0468"+
		"\u0462\3\2\2\2\u0468\u0465\3\2\2\2\u0469\u0107\3\2\2\2\u046a\u046b\7D"+
		"\2\2\u046b\u046c\7d\2\2\u046c\u046e\7\22\2\2\u046d\u046f\5\60\31\2\u046e"+
		"\u046d\3\2\2\2\u046e\u046f\3\2\2\2\u046f\u0470\3\2\2\2\u0470\u0472\7\23"+
		"\2\2\u0471\u0473\5\u00ba^\2\u0472\u0471\3\2\2\2\u0472\u0473\3\2\2\2\u0473"+
		"\u0109\3\2\2\2\u0474\u0475\7\65\2\2\u0475\u0476\7d\2\2\u0476\u0478\7\22"+
		"\2\2\u0477\u0479\5\60\31\2\u0478\u0477\3\2\2\2\u0478\u0479\3\2\2\2\u0479"+
		"\u047a\3\2\2\2\u047a\u047c\7\23\2\2\u047b\u047d\5\u00ba^\2\u047c\u047b"+
		"\3\2\2\2\u047c\u047d\3\2\2\2\u047d\u010b\3\2\2\2\u047e\u0482\5\u0112\u008a"+
		"\2\u047f\u0482\5\u0110\u0089\2\u0480\u0482\5\u010e\u0088\2\u0481\u047e"+
		"\3\2\2\2\u0481\u047f\3\2\2\2\u0481\u0480\3\2\2\2\u0482\u010d\3\2\2\2\u0483"+
		"\u0485\7X\2\2\u0484\u0483\3\2\2\2\u0484\u0485\3\2\2\2\u0485\u0486\3\2"+
		"\2\2\u0486\u0487\79\2\2\u0487\u0488\7d\2\2\u0488\u010f\3\2\2\2\u0489\u048a"+
		"\7X\2\2\u048a\u048b\79\2\2\u048b\u048c\7d\2\2\u048c\u048d\5&\24\2\u048d"+
		"\u0491\7\20\2\2\u048e\u0490\5\22\n\2\u048f\u048e\3\2\2\2\u0490\u0493\3"+
		"\2\2\2\u0491\u048f\3\2\2\2\u0491\u0492\3\2\2\2\u0492\u0494\3\2\2\2\u0493"+
		"\u0491\3\2\2\2\u0494\u0495\7\21\2\2\u0495\u0111\3\2\2\2\u0496\u0497\5"+
		"\u0114\u008b\2\u0497\u049b\7\20\2\2\u0498\u049a\5*\26\2\u0499\u0498\3"+
		"\2\2\2\u049a\u049d\3\2\2\2\u049b\u0499\3\2\2\2\u049b\u049c\3\2\2\2\u049c"+
		"\u049e\3\2\2\2\u049d\u049b\3\2\2\2\u049e\u049f\7\21\2\2\u049f\u0113\3"+
		"\2\2\2\u04a0\u04a2\7.\2\2\u04a1\u04a0\3\2\2\2\u04a1\u04a2\3\2\2\2\u04a2"+
		"\u04a3\3\2\2\2\u04a3\u04a4\79\2\2\u04a4\u04a5\7d\2\2\u04a5\u04a6\5&\24"+
		"\2\u04a6\u0115\3\2\2\2f\u0119\u011f\u0142\u014a\u0150\u0158\u015e\u0163"+
		"\u0168\u0180\u0188\u018e\u0195\u019c\u019f\u01b0\u01ba\u01c0\u01c8\u01cf"+
		"\u01d2\u01da\u01dd\u01e4\u01ef\u01f3\u01fc\u0215\u021e\u0226\u022e\u0236"+
		"\u023e\u0246\u024d\u0257\u0265\u026c\u0271\u027c\u0282\u0287\u028e\u0293"+
		"\u029d\u02a1\u02a6\u02b2\u02d3\u02ec\u02f1\u02f6\u0301\u030e\u0321\u0329"+
		"\u0331\u0337\u033f\u0347\u034d\u0353\u0356\u035c\u0364\u036b\u037a\u0386"+
		"\u038f\u03a0\u03a8\u03bf\u03c2\u03d0\u03d3\u03d7\u03da\u03e8\u03ef\u03fc"+
		"\u03ff\u0407\u0410\u0425\u042d\u0431\u0448\u044b\u0450\u045c\u0468\u046e"+
		"\u0472\u0478\u047c\u0481\u0484\u0491\u049b\u04a1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}