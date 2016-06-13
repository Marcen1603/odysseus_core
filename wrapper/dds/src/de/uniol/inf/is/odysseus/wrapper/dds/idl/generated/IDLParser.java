// Generated from IDL.g4 by ANTLR 4.4

package de.uniol.inf.is.odysseus.wrapper.dds.idl.generated;

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
		RULE_member = 69, RULE_union_type = 70, RULE_switch_type_spec = 71, RULE_switch_body = 72, 
		RULE_case_stmt = 73, RULE_case_label = 74, RULE_element_spec = 75, RULE_enum_type = 76, 
		RULE_enumerator = 77, RULE_sequence_type = 78, RULE_string_type = 79, 
		RULE_wide_string_type = 80, RULE_array_declarator = 81, RULE_fixed_array_size = 82, 
		RULE_attr_decl = 83, RULE_except_decl = 84, RULE_op_decl = 85, RULE_op_attribute = 86, 
		RULE_op_type_spec = 87, RULE_parameter_decls = 88, RULE_param_decl = 89, 
		RULE_param_attribute = 90, RULE_raises_expr = 91, RULE_context_expr = 92, 
		RULE_param_type_spec = 93, RULE_fixed_pt_type = 94, RULE_fixed_pt_const_type = 95, 
		RULE_value_base_type = 96, RULE_constr_forward_decl = 97, RULE_import_decl = 98, 
		RULE_imported_scope = 99, RULE_type_id_decl = 100, RULE_type_prefix_decl = 101, 
		RULE_readonly_attr_spec = 102, RULE_readonly_attr_declarator = 103, RULE_attr_spec = 104, 
		RULE_attr_declarator = 105, RULE_attr_raises_expr = 106, RULE_get_excep_expr = 107, 
		RULE_set_excep_expr = 108, RULE_exception_list = 109, RULE_component = 110, 
		RULE_component_forward_decl = 111, RULE_component_decl = 112, RULE_component_header = 113, 
		RULE_supported_interface_spec = 114, RULE_component_inheritance_spec = 115, 
		RULE_component_body = 116, RULE_component_export = 117, RULE_provides_decl = 118, 
		RULE_interface_type = 119, RULE_uses_decl = 120, RULE_emits_decl = 121, 
		RULE_publishes_decl = 122, RULE_consumes_decl = 123, RULE_home_decl = 124, 
		RULE_home_header = 125, RULE_home_inheritance_spec = 126, RULE_primary_key_spec = 127, 
		RULE_home_body = 128, RULE_home_export = 129, RULE_factory_decl = 130, 
		RULE_finder_decl = 131, RULE_event = 132, RULE_event_forward_decl = 133, 
		RULE_event_abs_decl = 134, RULE_event_decl = 135, RULE_event_header = 136;
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
		"struct_type", "member_list", "member", "union_type", "switch_type_spec", 
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
			setState(277);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==KW_IMPORT) {
				{
				{
				setState(274); import_decl();
				}
				}
				setState(279);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(281); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(280); definition();
				}
				}
				setState(283); 
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
			setState(318);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(285); type_decl();
				setState(286); match(SEMICOLON);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(288); const_decl();
				setState(289); match(SEMICOLON);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(291); except_decl();
				setState(292); match(SEMICOLON);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(294); interface_or_forward_decl();
				setState(295); match(SEMICOLON);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(297); module();
				setState(298); match(SEMICOLON);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(300); value();
				setState(301); match(SEMICOLON);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(303); type_id_decl();
				setState(304); match(SEMICOLON);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(306); type_prefix_decl();
				setState(307); match(SEMICOLON);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(309); event();
				setState(310); match(SEMICOLON);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(312); component();
				setState(313); match(SEMICOLON);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(315); home_decl();
				setState(316); match(SEMICOLON);
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
			setState(320); match(KW_MODULE);
			setState(321); match(ID);
			setState(322); match(LEFT_BRACE);
			setState(324); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(323); definition();
				}
				}
				setState(326); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 41)) & ~0x3f) == 0 && ((1L << (_la - 41)) & ((1L << (KW_TYPEDEF - 41)) | (1L << (KW_CUSTOM - 41)) | (1L << (KW_STRUCT - 41)) | (1L << (KW_NATIVE - 41)) | (1L << (KW_EVENTTYPE - 41)) | (1L << (KW_ENUM - 41)) | (1L << (KW_HOME - 41)) | (1L << (KW_EXCEPTION - 41)) | (1L << (KW_CONST - 41)) | (1L << (KW_VALUETYPE - 41)) | (1L << (KW_MODULE - 41)) | (1L << (KW_UNION - 41)) | (1L << (KW_ABSTRACT - 41)) | (1L << (KW_TYPEPREFIX - 41)) | (1L << (KW_TYPEID - 41)) | (1L << (KW_LOCAL - 41)) | (1L << (KW_INTERFACE - 41)) | (1L << (KW_COMPONENT - 41)))) != 0) );
			setState(328); match(RIGHT_BRACE);
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
			setState(332);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(330); interface_decl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(331); forward_decl();
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
			setState(334); interface_header();
			setState(335); match(LEFT_BRACE);
			setState(336); interface_body();
			setState(337); match(RIGHT_BRACE);
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
			setState(340);
			_la = _input.LA(1);
			if (_la==KW_ABSTRACT || _la==KW_LOCAL) {
				{
				setState(339);
				_la = _input.LA(1);
				if ( !(_la==KW_ABSTRACT || _la==KW_LOCAL) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
			}

			setState(342); match(KW_INTERFACE);
			setState(343); match(ID);
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
			setState(346);
			_la = _input.LA(1);
			if (_la==KW_ABSTRACT || _la==KW_LOCAL) {
				{
				setState(345);
				_la = _input.LA(1);
				if ( !(_la==KW_ABSTRACT || _la==KW_LOCAL) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
			}

			setState(348); match(KW_INTERFACE);
			setState(349); match(ID);
			setState(351);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(350); interface_inheritance_spec();
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
			setState(356);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE_COLON) | (1L << KW_STRING) | (1L << KW_TYPEDEF) | (1L << KW_OCTET) | (1L << KW_STRUCT) | (1L << KW_NATIVE) | (1L << KW_READONLY) | (1L << KW_VOID) | (1L << KW_WCHAR) | (1L << KW_SHORT) | (1L << KW_LONG) | (1L << KW_ENUM) | (1L << KW_WSTRING))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (KW_EXCEPTION - 67)) | (1L << (KW_CONST - 67)) | (1L << (KW_VALUEBASE - 67)) | (1L << (KW_OBJECT - 67)) | (1L << (KW_UNSIGNED - 67)) | (1L << (KW_UNION - 67)) | (1L << (KW_ONEWAY - 67)) | (1L << (KW_ANY - 67)) | (1L << (KW_CHAR - 67)) | (1L << (KW_FLOAT - 67)) | (1L << (KW_BOOLEAN - 67)) | (1L << (KW_DOUBLE - 67)) | (1L << (KW_TYPEPREFIX - 67)) | (1L << (KW_TYPEID - 67)) | (1L << (KW_ATTRIBUTE - 67)) | (1L << (ID - 67)))) != 0)) {
				{
				{
				setState(353); export();
				}
				}
				setState(358);
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
			setState(380);
			switch (_input.LA(1)) {
			case KW_TYPEDEF:
			case KW_STRUCT:
			case KW_NATIVE:
			case KW_ENUM:
			case KW_UNION:
				enterOuterAlt(_localctx, 1);
				{
				setState(359); type_decl();
				setState(360); match(SEMICOLON);
				}
				break;
			case KW_CONST:
				enterOuterAlt(_localctx, 2);
				{
				setState(362); const_decl();
				setState(363); match(SEMICOLON);
				}
				break;
			case KW_EXCEPTION:
				enterOuterAlt(_localctx, 3);
				{
				setState(365); except_decl();
				setState(366); match(SEMICOLON);
				}
				break;
			case KW_READONLY:
			case KW_ATTRIBUTE:
				enterOuterAlt(_localctx, 4);
				{
				setState(368); attr_decl();
				setState(369); match(SEMICOLON);
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
				setState(371); op_decl();
				setState(372); match(SEMICOLON);
				}
				break;
			case KW_TYPEID:
				enterOuterAlt(_localctx, 6);
				{
				setState(374); type_id_decl();
				setState(375); match(SEMICOLON);
				}
				break;
			case KW_TYPEPREFIX:
				enterOuterAlt(_localctx, 7);
				{
				setState(377); type_prefix_decl();
				setState(378); match(SEMICOLON);
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
			setState(382); match(COLON);
			setState(383); interface_name();
			setState(388);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(384); match(COMA);
				setState(385); interface_name();
				}
				}
				setState(390);
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
			setState(391); scoped_name();
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
			setState(394);
			_la = _input.LA(1);
			if (_la==DOUBLE_COLON) {
				{
				setState(393); match(DOUBLE_COLON);
				}
			}

			setState(396); match(ID);
			setState(401);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOUBLE_COLON) {
				{
				{
				setState(397); match(DOUBLE_COLON);
				setState(398); match(ID);
				}
				}
				setState(403);
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
			setState(408);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				setState(404); value_decl();
				}
				break;
			case 2:
				{
				setState(405); value_abs_decl();
				}
				break;
			case 3:
				{
				setState(406); value_box_decl();
				}
				break;
			case 4:
				{
				setState(407); value_forward_decl();
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
			setState(411);
			_la = _input.LA(1);
			if (_la==KW_ABSTRACT) {
				{
				setState(410); match(KW_ABSTRACT);
				}
			}

			setState(413); match(KW_VALUETYPE);
			setState(414); match(ID);
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
			setState(416); match(KW_VALUETYPE);
			setState(417); match(ID);
			setState(418); type_spec();
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
			setState(420); match(KW_ABSTRACT);
			setState(421); match(KW_VALUETYPE);
			setState(422); match(ID);
			setState(423); value_inheritance_spec();
			setState(424); match(LEFT_BRACE);
			setState(428);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE_COLON) | (1L << KW_STRING) | (1L << KW_TYPEDEF) | (1L << KW_OCTET) | (1L << KW_STRUCT) | (1L << KW_NATIVE) | (1L << KW_READONLY) | (1L << KW_VOID) | (1L << KW_WCHAR) | (1L << KW_SHORT) | (1L << KW_LONG) | (1L << KW_ENUM) | (1L << KW_WSTRING))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (KW_EXCEPTION - 67)) | (1L << (KW_CONST - 67)) | (1L << (KW_VALUEBASE - 67)) | (1L << (KW_OBJECT - 67)) | (1L << (KW_UNSIGNED - 67)) | (1L << (KW_UNION - 67)) | (1L << (KW_ONEWAY - 67)) | (1L << (KW_ANY - 67)) | (1L << (KW_CHAR - 67)) | (1L << (KW_FLOAT - 67)) | (1L << (KW_BOOLEAN - 67)) | (1L << (KW_DOUBLE - 67)) | (1L << (KW_TYPEPREFIX - 67)) | (1L << (KW_TYPEID - 67)) | (1L << (KW_ATTRIBUTE - 67)) | (1L << (ID - 67)))) != 0)) {
				{
				{
				setState(425); export();
				}
				}
				setState(430);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(431); match(RIGHT_BRACE);
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
			setState(433); value_header();
			setState(434); match(LEFT_BRACE);
			setState(438);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE_COLON) | (1L << KW_STRING) | (1L << KW_TYPEDEF) | (1L << KW_OCTET) | (1L << KW_STRUCT) | (1L << KW_NATIVE) | (1L << KW_READONLY) | (1L << KW_VOID) | (1L << KW_PRIVATE) | (1L << KW_WCHAR) | (1L << KW_PUBLIC) | (1L << KW_SHORT) | (1L << KW_LONG) | (1L << KW_ENUM) | (1L << KW_WSTRING))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (KW_FACTORY - 66)) | (1L << (KW_EXCEPTION - 66)) | (1L << (KW_CONST - 66)) | (1L << (KW_VALUEBASE - 66)) | (1L << (KW_OBJECT - 66)) | (1L << (KW_UNSIGNED - 66)) | (1L << (KW_UNION - 66)) | (1L << (KW_ONEWAY - 66)) | (1L << (KW_ANY - 66)) | (1L << (KW_CHAR - 66)) | (1L << (KW_FLOAT - 66)) | (1L << (KW_BOOLEAN - 66)) | (1L << (KW_DOUBLE - 66)) | (1L << (KW_TYPEPREFIX - 66)) | (1L << (KW_TYPEID - 66)) | (1L << (KW_ATTRIBUTE - 66)) | (1L << (ID - 66)))) != 0)) {
				{
				{
				setState(435); value_element();
				}
				}
				setState(440);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(441); match(RIGHT_BRACE);
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
			setState(444);
			_la = _input.LA(1);
			if (_la==KW_CUSTOM) {
				{
				setState(443); match(KW_CUSTOM);
				}
			}

			setState(446); match(KW_VALUETYPE);
			setState(447); match(ID);
			setState(448); value_inheritance_spec();
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
			setState(462);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(450); match(COLON);
				setState(452);
				_la = _input.LA(1);
				if (_la==KW_TRUNCATABLE) {
					{
					setState(451); match(KW_TRUNCATABLE);
					}
				}

				setState(454); value_name();
				setState(459);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMA) {
					{
					{
					setState(455); match(COMA);
					setState(456); value_name();
					}
					}
					setState(461);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(473);
			_la = _input.LA(1);
			if (_la==KW_SUPPORTS) {
				{
				setState(464); match(KW_SUPPORTS);
				setState(465); interface_name();
				setState(470);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMA) {
					{
					{
					setState(466); match(COMA);
					setState(467); interface_name();
					}
					}
					setState(472);
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
			setState(475); scoped_name();
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
			setState(480);
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
				setState(477); export();
				}
				break;
			case KW_PRIVATE:
			case KW_PUBLIC:
				{
				setState(478); state_member();
				}
				break;
			case KW_FACTORY:
				{
				setState(479); init_decl();
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
			setState(482);
			_la = _input.LA(1);
			if ( !(_la==KW_PRIVATE || _la==KW_PUBLIC) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(483); type_spec();
			setState(484); declarators();
			setState(485); match(SEMICOLON);
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
			setState(487); match(KW_FACTORY);
			setState(488); match(ID);
			setState(489); match(LEFT_BRACKET);
			setState(491);
			_la = _input.LA(1);
			if (_la==KW_IN) {
				{
				setState(490); init_param_decls();
				}
			}

			setState(493); match(RIGHT_BRACKET);
			setState(495);
			_la = _input.LA(1);
			if (_la==KW_RAISES) {
				{
				setState(494); raises_expr();
				}
			}

			setState(497); match(SEMICOLON);
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
			setState(499); init_param_decl();
			setState(504);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(500); match(COMA);
				setState(501); init_param_decl();
				}
				}
				setState(506);
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
			setState(507); init_param_attribute();
			setState(508); param_type_spec();
			setState(509); simple_declarator();
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
			setState(511); match(KW_IN);
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
			setState(513); match(KW_CONST);
			setState(514); const_type();
			setState(515); match(ID);
			setState(516); match(EQUAL);
			setState(517); const_exp();
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
			setState(529);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(519); integer_type();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(520); char_type();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(521); wide_char_type();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(522); boolean_type();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(523); floating_pt_type();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(524); string_type();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(525); wide_string_type();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(526); fixed_pt_const_type();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(527); scoped_name();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(528); octet_type();
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
			setState(531); or_expr();
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
			setState(533); xor_expr();
			setState(538);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PIPE) {
				{
				{
				setState(534); match(PIPE);
				setState(535); xor_expr();
				}
				}
				setState(540);
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
			setState(541); and_expr();
			setState(546);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CARET) {
				{
				{
				setState(542); match(CARET);
				setState(543); and_expr();
				}
				}
				setState(548);
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
			setState(549); shift_expr();
			setState(554);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AMPERSAND) {
				{
				{
				setState(550); match(AMPERSAND);
				setState(551); shift_expr();
				}
				}
				setState(556);
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
			setState(557); add_expr();
			setState(562);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==RIGHT_SHIFT || _la==LEFT_SHIFT) {
				{
				{
				setState(558);
				_la = _input.LA(1);
				if ( !(_la==RIGHT_SHIFT || _la==LEFT_SHIFT) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(559); add_expr();
				}
				}
				setState(564);
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
			setState(565); mult_expr();
			setState(570);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PLUS || _la==MINUS) {
				{
				{
				setState(566);
				_la = _input.LA(1);
				if ( !(_la==PLUS || _la==MINUS) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(567); mult_expr();
				}
				}
				setState(572);
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
			setState(573); unary_expr();
			setState(578);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SLASH) | (1L << STAR) | (1L << PERCENT))) != 0)) {
				{
				{
				setState(574);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SLASH) | (1L << STAR) | (1L << PERCENT))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(575); unary_expr();
				}
				}
				setState(580);
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
			setState(585);
			switch (_input.LA(1)) {
			case TILDE:
			case PLUS:
			case MINUS:
				enterOuterAlt(_localctx, 1);
				{
				setState(581); unary_operator();
				setState(582); primary_expr();
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
				setState(584); primary_expr();
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
			setState(587);
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
			setState(595);
			switch (_input.LA(1)) {
			case DOUBLE_COLON:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(589); scoped_name();
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
				setState(590); literal();
				}
				break;
			case LEFT_BRACKET:
				enterOuterAlt(_localctx, 3);
				{
				setState(591); match(LEFT_BRACKET);
				setState(592); const_exp();
				setState(593); match(RIGHT_BRACKET);
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
			setState(597);
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
			setState(599); const_exp();
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
			setState(609);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(601); match(KW_TYPEDEF);
				setState(602); type_declarator();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(603); struct_type();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(604); union_type();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(605); enum_type();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(606); match(KW_NATIVE);
				setState(607); simple_declarator();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(608); constr_forward_decl();
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
			setState(611); type_spec();
			setState(612); declarators();
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
			setState(616);
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
				setState(614); simple_type_spec();
				}
				break;
			case KW_STRUCT:
			case KW_ENUM:
			case KW_UNION:
				enterOuterAlt(_localctx, 2);
				{
				setState(615); constr_type_spec();
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
			setState(621);
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
				setState(618); base_type_spec();
				}
				break;
			case KW_STRING:
			case KW_SEQUENCE:
			case KW_WSTRING:
			case KW_FIXED:
				enterOuterAlt(_localctx, 2);
				{
				setState(619); template_type_spec();
				}
				break;
			case DOUBLE_COLON:
			case ID:
				enterOuterAlt(_localctx, 3);
				{
				setState(620); scoped_name();
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
			setState(632);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(623); floating_pt_type();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(624); integer_type();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(625); char_type();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(626); wide_char_type();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(627); boolean_type();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(628); octet_type();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(629); any_type();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(630); object_type();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(631); value_base_type();
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
			setState(638);
			switch (_input.LA(1)) {
			case KW_SEQUENCE:
				enterOuterAlt(_localctx, 1);
				{
				setState(634); sequence_type();
				}
				break;
			case KW_STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(635); string_type();
				}
				break;
			case KW_WSTRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(636); wide_string_type();
				}
				break;
			case KW_FIXED:
				enterOuterAlt(_localctx, 4);
				{
				setState(637); fixed_pt_type();
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
			setState(643);
			switch (_input.LA(1)) {
			case KW_STRUCT:
				enterOuterAlt(_localctx, 1);
				{
				setState(640); struct_type();
				}
				break;
			case KW_UNION:
				enterOuterAlt(_localctx, 2);
				{
				setState(641); union_type();
				}
				break;
			case KW_ENUM:
				enterOuterAlt(_localctx, 3);
				{
				setState(642); enum_type();
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
			setState(645); declarator();
			setState(650);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(646); match(COMA);
				setState(647); declarator();
				}
				}
				setState(652);
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
			setState(655);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(653); simple_declarator();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(654); complex_declarator();
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
			setState(657); match(ID);
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
			setState(659); array_declarator();
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
			setState(665);
			switch (_input.LA(1)) {
			case KW_FLOAT:
				{
				setState(661); match(KW_FLOAT);
				}
				break;
			case KW_DOUBLE:
				{
				setState(662); match(KW_DOUBLE);
				}
				break;
			case KW_LONG:
				{
				setState(663); match(KW_LONG);
				setState(664); match(KW_DOUBLE);
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
			setState(669);
			switch (_input.LA(1)) {
			case KW_SHORT:
			case KW_LONG:
				enterOuterAlt(_localctx, 1);
				{
				setState(667); signed_int();
				}
				break;
			case KW_UNSIGNED:
				enterOuterAlt(_localctx, 2);
				{
				setState(668); unsigned_int();
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
			setState(674);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(671); signed_short_int();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(672); signed_long_int();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(673); signed_longlong_int();
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
			setState(676); match(KW_SHORT);
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
			setState(678); match(KW_LONG);
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
			setState(680); match(KW_LONG);
			setState(681); match(KW_LONG);
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
			setState(686);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(683); unsigned_short_int();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(684); unsigned_long_int();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(685); unsigned_longlong_int();
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
			setState(688); match(KW_UNSIGNED);
			setState(689); match(KW_SHORT);
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
			setState(691); match(KW_UNSIGNED);
			setState(692); match(KW_LONG);
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
			setState(694); match(KW_UNSIGNED);
			setState(695); match(KW_LONG);
			setState(696); match(KW_LONG);
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
			setState(698); match(KW_CHAR);
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
			setState(700); match(KW_WCHAR);
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
			setState(702); match(KW_BOOLEAN);
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
			setState(704); match(KW_OCTET);
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
			setState(706); match(KW_ANY);
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
			setState(708); match(KW_OBJECT);
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
			setState(710); match(KW_STRUCT);
			setState(711); match(ID);
			setState(712); match(LEFT_BRACE);
			setState(713); member_list();
			setState(714); match(RIGHT_BRACE);
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
			setState(717); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(716); member();
				}
				}
				setState(719); 
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
			setState(721); type_spec();
			setState(722); declarators();
			setState(723); match(SEMICOLON);
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
		enterRule(_localctx, 140, RULE_union_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(725); match(KW_UNION);
			setState(726); match(ID);
			setState(727); match(KW_SWITCH);
			setState(728); match(LEFT_BRACKET);
			setState(729); switch_type_spec();
			setState(730); match(RIGHT_BRACKET);
			setState(731); match(LEFT_BRACE);
			setState(732); switch_body();
			setState(733); match(RIGHT_BRACE);
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
		enterRule(_localctx, 142, RULE_switch_type_spec);
		try {
			setState(740);
			switch (_input.LA(1)) {
			case KW_SHORT:
			case KW_LONG:
			case KW_UNSIGNED:
				enterOuterAlt(_localctx, 1);
				{
				setState(735); integer_type();
				}
				break;
			case KW_CHAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(736); char_type();
				}
				break;
			case KW_BOOLEAN:
				enterOuterAlt(_localctx, 3);
				{
				setState(737); boolean_type();
				}
				break;
			case KW_ENUM:
				enterOuterAlt(_localctx, 4);
				{
				setState(738); enum_type();
				}
				break;
			case DOUBLE_COLON:
			case ID:
				enterOuterAlt(_localctx, 5);
				{
				setState(739); scoped_name();
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
		enterRule(_localctx, 144, RULE_switch_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(743); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(742); case_stmt();
				}
				}
				setState(745); 
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
		enterRule(_localctx, 146, RULE_case_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(748); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(747); case_label();
				}
				}
				setState(750); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==KW_DEFAULT || _la==KW_CASE );
			setState(752); element_spec();
			setState(753); match(SEMICOLON);
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
		enterRule(_localctx, 148, RULE_case_label);
		try {
			setState(761);
			switch (_input.LA(1)) {
			case KW_CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(755); match(KW_CASE);
				setState(756); const_exp();
				setState(757); match(COLON);
				}
				break;
			case KW_DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(759); match(KW_DEFAULT);
				setState(760); match(COLON);
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
		enterRule(_localctx, 150, RULE_element_spec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(763); type_spec();
			setState(764); declarator();
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
		enterRule(_localctx, 152, RULE_enum_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(766); match(KW_ENUM);
			setState(767); match(ID);
			setState(768); match(LEFT_BRACE);
			setState(769); enumerator();
			setState(774);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(770); match(COMA);
				setState(771); enumerator();
				}
				}
				setState(776);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(777); match(RIGHT_BRACE);
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
		enterRule(_localctx, 154, RULE_enumerator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(779); match(ID);
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
		enterRule(_localctx, 156, RULE_sequence_type);
		try {
			setState(793);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(781); match(KW_SEQUENCE);
				setState(782); match(LEFT_ANG_BRACKET);
				setState(783); simple_type_spec();
				setState(784); match(COMA);
				setState(785); positive_int_const();
				setState(786); match(RIGHT_ANG_BRACKET);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(788); match(KW_SEQUENCE);
				setState(789); match(LEFT_ANG_BRACKET);
				setState(790); simple_type_spec();
				setState(791); match(RIGHT_ANG_BRACKET);
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
		enterRule(_localctx, 158, RULE_string_type);
		try {
			setState(801);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(795); match(KW_STRING);
				setState(796); match(LEFT_ANG_BRACKET);
				setState(797); positive_int_const();
				setState(798); match(RIGHT_ANG_BRACKET);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(800); match(KW_STRING);
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
		enterRule(_localctx, 160, RULE_wide_string_type);
		try {
			setState(809);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(803); match(KW_WSTRING);
				setState(804); match(LEFT_ANG_BRACKET);
				setState(805); positive_int_const();
				setState(806); match(RIGHT_ANG_BRACKET);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(808); match(KW_WSTRING);
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
		enterRule(_localctx, 162, RULE_array_declarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(811); match(ID);
			setState(813); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(812); fixed_array_size();
				}
				}
				setState(815); 
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
		enterRule(_localctx, 164, RULE_fixed_array_size);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(817); match(LEFT_SQUARE_BRACKET);
			setState(818); positive_int_const();
			setState(819); match(RIGHT_SQUARE_BRACKET);
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
		enterRule(_localctx, 166, RULE_attr_decl);
		try {
			setState(823);
			switch (_input.LA(1)) {
			case KW_READONLY:
				enterOuterAlt(_localctx, 1);
				{
				setState(821); readonly_attr_spec();
				}
				break;
			case KW_ATTRIBUTE:
				enterOuterAlt(_localctx, 2);
				{
				setState(822); attr_spec();
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
		enterRule(_localctx, 168, RULE_except_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(825); match(KW_EXCEPTION);
			setState(826); match(ID);
			setState(827); match(LEFT_BRACE);
			setState(831);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE_COLON) | (1L << KW_STRING) | (1L << KW_OCTET) | (1L << KW_SEQUENCE) | (1L << KW_STRUCT) | (1L << KW_WCHAR) | (1L << KW_SHORT) | (1L << KW_LONG) | (1L << KW_ENUM) | (1L << KW_WSTRING))) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & ((1L << (KW_VALUEBASE - 70)) | (1L << (KW_OBJECT - 70)) | (1L << (KW_UNSIGNED - 70)) | (1L << (KW_FIXED - 70)) | (1L << (KW_UNION - 70)) | (1L << (KW_ANY - 70)) | (1L << (KW_CHAR - 70)) | (1L << (KW_FLOAT - 70)) | (1L << (KW_BOOLEAN - 70)) | (1L << (KW_DOUBLE - 70)) | (1L << (ID - 70)))) != 0)) {
				{
				{
				setState(828); member();
				}
				}
				setState(833);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(834); match(RIGHT_BRACE);
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
		enterRule(_localctx, 170, RULE_op_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(837);
			_la = _input.LA(1);
			if (_la==KW_ONEWAY) {
				{
				setState(836); op_attribute();
				}
			}

			setState(839); op_type_spec();
			setState(840); match(ID);
			setState(841); parameter_decls();
			setState(843);
			_la = _input.LA(1);
			if (_la==KW_RAISES) {
				{
				setState(842); raises_expr();
				}
			}

			setState(846);
			_la = _input.LA(1);
			if (_la==KW_CONTEXT) {
				{
				setState(845); context_expr();
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
		enterRule(_localctx, 172, RULE_op_attribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(848); match(KW_ONEWAY);
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
		enterRule(_localctx, 174, RULE_op_type_spec);
		try {
			setState(852);
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
				setState(850); param_type_spec();
				}
				break;
			case KW_VOID:
				enterOuterAlt(_localctx, 2);
				{
				setState(851); match(KW_VOID);
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
		enterRule(_localctx, 176, RULE_parameter_decls);
		int _la;
		try {
			setState(867);
			switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(854); match(LEFT_BRACKET);
				setState(855); param_decl();
				setState(860);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMA) {
					{
					{
					setState(856); match(COMA);
					setState(857); param_decl();
					}
					}
					setState(862);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(863); match(RIGHT_BRACKET);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(865); match(LEFT_BRACKET);
				setState(866); match(RIGHT_BRACKET);
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
		enterRule(_localctx, 178, RULE_param_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(869); param_attribute();
			setState(870); param_type_spec();
			setState(871); simple_declarator();
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
		enterRule(_localctx, 180, RULE_param_attribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(873);
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
		enterRule(_localctx, 182, RULE_raises_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(875); match(KW_RAISES);
			setState(876); match(LEFT_BRACKET);
			setState(877); scoped_name();
			setState(882);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(878); match(COMA);
				setState(879); scoped_name();
				}
				}
				setState(884);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(885); match(RIGHT_BRACKET);
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
		enterRule(_localctx, 184, RULE_context_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(887); match(KW_CONTEXT);
			setState(888); match(LEFT_BRACKET);
			setState(889); match(STRING_LITERAL);
			setState(894);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(890); match(COMA);
				setState(891); match(STRING_LITERAL);
				}
				}
				setState(896);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(897); match(RIGHT_BRACKET);
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
		enterRule(_localctx, 186, RULE_param_type_spec);
		try {
			setState(903);
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
				setState(899); base_type_spec();
				}
				break;
			case KW_STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(900); string_type();
				}
				break;
			case KW_WSTRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(901); wide_string_type();
				}
				break;
			case DOUBLE_COLON:
			case ID:
				enterOuterAlt(_localctx, 4);
				{
				setState(902); scoped_name();
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
		enterRule(_localctx, 188, RULE_fixed_pt_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(905); match(KW_FIXED);
			setState(906); match(LEFT_ANG_BRACKET);
			setState(907); positive_int_const();
			setState(908); match(COMA);
			setState(909); positive_int_const();
			setState(910); match(RIGHT_ANG_BRACKET);
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
		enterRule(_localctx, 190, RULE_fixed_pt_const_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(912); match(KW_FIXED);
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
		enterRule(_localctx, 192, RULE_value_base_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(914); match(KW_VALUEBASE);
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
		enterRule(_localctx, 194, RULE_constr_forward_decl);
		try {
			setState(920);
			switch (_input.LA(1)) {
			case KW_STRUCT:
				enterOuterAlt(_localctx, 1);
				{
				setState(916); match(KW_STRUCT);
				setState(917); match(ID);
				}
				break;
			case KW_UNION:
				enterOuterAlt(_localctx, 2);
				{
				setState(918); match(KW_UNION);
				setState(919); match(ID);
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
		enterRule(_localctx, 196, RULE_import_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(922); match(KW_IMPORT);
			setState(923); imported_scope();
			setState(924); match(SEMICOLON);
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
		enterRule(_localctx, 198, RULE_imported_scope);
		try {
			setState(928);
			switch (_input.LA(1)) {
			case DOUBLE_COLON:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(926); scoped_name();
				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(927); match(STRING_LITERAL);
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
		enterRule(_localctx, 200, RULE_type_id_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(930); match(KW_TYPEID);
			setState(931); scoped_name();
			setState(932); match(STRING_LITERAL);
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
		enterRule(_localctx, 202, RULE_type_prefix_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(934); match(KW_TYPEPREFIX);
			setState(935); scoped_name();
			setState(936); match(STRING_LITERAL);
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
		enterRule(_localctx, 204, RULE_readonly_attr_spec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(938); match(KW_READONLY);
			setState(939); match(KW_ATTRIBUTE);
			setState(940); param_type_spec();
			setState(941); readonly_attr_declarator();
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
		enterRule(_localctx, 206, RULE_readonly_attr_declarator);
		int _la;
		try {
			setState(954);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(943); simple_declarator();
				setState(944); raises_expr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(946); simple_declarator();
				setState(951);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMA) {
					{
					{
					setState(947); match(COMA);
					setState(948); simple_declarator();
					}
					}
					setState(953);
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
		enterRule(_localctx, 208, RULE_attr_spec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(956); match(KW_ATTRIBUTE);
			setState(957); param_type_spec();
			setState(958); attr_declarator();
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
		enterRule(_localctx, 210, RULE_attr_declarator);
		int _la;
		try {
			setState(971);
			switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(960); simple_declarator();
				setState(961); attr_raises_expr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(963); simple_declarator();
				setState(968);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMA) {
					{
					{
					setState(964); match(COMA);
					setState(965); simple_declarator();
					}
					}
					setState(970);
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
		enterRule(_localctx, 212, RULE_attr_raises_expr);
		int _la;
		try {
			setState(978);
			switch (_input.LA(1)) {
			case KW_GETRAISES:
				enterOuterAlt(_localctx, 1);
				{
				setState(973); get_excep_expr();
				setState(975);
				_la = _input.LA(1);
				if (_la==KW_SETRAISES) {
					{
					setState(974); set_excep_expr();
					}
				}

				}
				break;
			case KW_SETRAISES:
				enterOuterAlt(_localctx, 2);
				{
				setState(977); set_excep_expr();
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
		enterRule(_localctx, 214, RULE_get_excep_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(980); match(KW_GETRAISES);
			setState(981); exception_list();
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
		enterRule(_localctx, 216, RULE_set_excep_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(983); match(KW_SETRAISES);
			setState(984); exception_list();
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
		enterRule(_localctx, 218, RULE_exception_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(986); match(LEFT_BRACKET);
			setState(987); scoped_name();
			setState(992);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(988); match(COMA);
				setState(989); scoped_name();
				}
				}
				setState(994);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(995); match(RIGHT_BRACKET);
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
		enterRule(_localctx, 220, RULE_component);
		try {
			setState(999);
			switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(997); component_decl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(998); component_forward_decl();
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
		enterRule(_localctx, 222, RULE_component_forward_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1001); match(KW_COMPONENT);
			setState(1002); match(ID);
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
		enterRule(_localctx, 224, RULE_component_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1004); component_header();
			setState(1005); match(LEFT_BRACE);
			setState(1006); component_body();
			setState(1007); match(RIGHT_BRACE);
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
		enterRule(_localctx, 226, RULE_component_header);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1009); match(KW_COMPONENT);
			setState(1010); match(ID);
			setState(1012);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(1011); component_inheritance_spec();
				}
			}

			setState(1015);
			_la = _input.LA(1);
			if (_la==KW_SUPPORTS) {
				{
				setState(1014); supported_interface_spec();
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
		enterRule(_localctx, 228, RULE_supported_interface_spec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1017); match(KW_SUPPORTS);
			setState(1018); scoped_name();
			setState(1023);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(1019); match(COMA);
				setState(1020); scoped_name();
				}
				}
				setState(1025);
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
		enterRule(_localctx, 230, RULE_component_inheritance_spec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1026); match(COLON);
			setState(1027); scoped_name();
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
		enterRule(_localctx, 232, RULE_component_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1032);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 37)) & ~0x3f) == 0 && ((1L << (_la - 37)) & ((1L << (KW_EMITS - 37)) | (1L << (KW_PUBLISHES - 37)) | (1L << (KW_USES - 37)) | (1L << (KW_READONLY - 37)) | (1L << (KW_PROVIDES - 37)) | (1L << (KW_CONSUMES - 37)) | (1L << (KW_ATTRIBUTE - 37)))) != 0)) {
				{
				{
				setState(1029); component_export();
				}
				}
				setState(1034);
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
		enterRule(_localctx, 234, RULE_component_export);
		try {
			setState(1053);
			switch (_input.LA(1)) {
			case KW_PROVIDES:
				enterOuterAlt(_localctx, 1);
				{
				setState(1035); provides_decl();
				setState(1036); match(SEMICOLON);
				}
				break;
			case KW_USES:
				enterOuterAlt(_localctx, 2);
				{
				setState(1038); uses_decl();
				setState(1039); match(SEMICOLON);
				}
				break;
			case KW_EMITS:
				enterOuterAlt(_localctx, 3);
				{
				setState(1041); emits_decl();
				setState(1042); match(SEMICOLON);
				}
				break;
			case KW_PUBLISHES:
				enterOuterAlt(_localctx, 4);
				{
				setState(1044); publishes_decl();
				setState(1045); match(SEMICOLON);
				}
				break;
			case KW_CONSUMES:
				enterOuterAlt(_localctx, 5);
				{
				setState(1047); consumes_decl();
				setState(1048); match(SEMICOLON);
				}
				break;
			case KW_READONLY:
			case KW_ATTRIBUTE:
				enterOuterAlt(_localctx, 6);
				{
				setState(1050); attr_decl();
				setState(1051); match(SEMICOLON);
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
		enterRule(_localctx, 236, RULE_provides_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1055); match(KW_PROVIDES);
			setState(1056); interface_type();
			setState(1057); match(ID);
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
		enterRule(_localctx, 238, RULE_interface_type);
		try {
			setState(1061);
			switch (_input.LA(1)) {
			case DOUBLE_COLON:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(1059); scoped_name();
				}
				break;
			case KW_OBJECT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1060); match(KW_OBJECT);
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
		enterRule(_localctx, 240, RULE_uses_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1063); match(KW_USES);
			setState(1065);
			_la = _input.LA(1);
			if (_la==KW_MULTIPLE) {
				{
				setState(1064); match(KW_MULTIPLE);
				}
			}

			setState(1067); interface_type();
			setState(1068); match(ID);
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
		enterRule(_localctx, 242, RULE_emits_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1070); match(KW_EMITS);
			setState(1071); scoped_name();
			setState(1072); match(ID);
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
		enterRule(_localctx, 244, RULE_publishes_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1074); match(KW_PUBLISHES);
			setState(1075); scoped_name();
			setState(1076); match(ID);
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
		enterRule(_localctx, 246, RULE_consumes_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1078); match(KW_CONSUMES);
			setState(1079); scoped_name();
			setState(1080); match(ID);
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
		enterRule(_localctx, 248, RULE_home_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1082); home_header();
			setState(1083); home_body();
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
		enterRule(_localctx, 250, RULE_home_header);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1085); match(KW_HOME);
			setState(1086); match(ID);
			setState(1088);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(1087); home_inheritance_spec();
				}
			}

			setState(1091);
			_la = _input.LA(1);
			if (_la==KW_SUPPORTS) {
				{
				setState(1090); supported_interface_spec();
				}
			}

			setState(1093); match(KW_MANAGES);
			setState(1094); scoped_name();
			setState(1096);
			_la = _input.LA(1);
			if (_la==KW_PRIMARYKEY) {
				{
				setState(1095); primary_key_spec();
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
		enterRule(_localctx, 252, RULE_home_inheritance_spec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1098); match(COLON);
			setState(1099); scoped_name();
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
		enterRule(_localctx, 254, RULE_primary_key_spec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1101); match(KW_PRIMARYKEY);
			setState(1102); scoped_name();
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
		enterRule(_localctx, 256, RULE_home_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1104); match(LEFT_BRACE);
			setState(1108);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE_COLON) | (1L << KW_STRING) | (1L << KW_TYPEDEF) | (1L << KW_OCTET) | (1L << KW_STRUCT) | (1L << KW_NATIVE) | (1L << KW_READONLY) | (1L << KW_FINDER) | (1L << KW_VOID) | (1L << KW_WCHAR) | (1L << KW_SHORT) | (1L << KW_LONG) | (1L << KW_ENUM) | (1L << KW_WSTRING))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (KW_FACTORY - 66)) | (1L << (KW_EXCEPTION - 66)) | (1L << (KW_CONST - 66)) | (1L << (KW_VALUEBASE - 66)) | (1L << (KW_OBJECT - 66)) | (1L << (KW_UNSIGNED - 66)) | (1L << (KW_UNION - 66)) | (1L << (KW_ONEWAY - 66)) | (1L << (KW_ANY - 66)) | (1L << (KW_CHAR - 66)) | (1L << (KW_FLOAT - 66)) | (1L << (KW_BOOLEAN - 66)) | (1L << (KW_DOUBLE - 66)) | (1L << (KW_TYPEPREFIX - 66)) | (1L << (KW_TYPEID - 66)) | (1L << (KW_ATTRIBUTE - 66)) | (1L << (ID - 66)))) != 0)) {
				{
				{
				setState(1105); home_export();
				}
				}
				setState(1110);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1111); match(RIGHT_BRACE);
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
		enterRule(_localctx, 258, RULE_home_export);
		try {
			setState(1120);
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
				setState(1113); export();
				}
				break;
			case KW_FACTORY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1114); factory_decl();
				setState(1115); match(SEMICOLON);
				}
				break;
			case KW_FINDER:
				enterOuterAlt(_localctx, 3);
				{
				setState(1117); finder_decl();
				setState(1118); match(SEMICOLON);
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
		enterRule(_localctx, 260, RULE_factory_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1122); match(KW_FACTORY);
			setState(1123); match(ID);
			setState(1124); match(LEFT_BRACKET);
			setState(1126);
			_la = _input.LA(1);
			if (_la==KW_IN) {
				{
				setState(1125); init_param_decls();
				}
			}

			setState(1128); match(RIGHT_BRACKET);
			setState(1130);
			_la = _input.LA(1);
			if (_la==KW_RAISES) {
				{
				setState(1129); raises_expr();
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
		enterRule(_localctx, 262, RULE_finder_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1132); match(KW_FINDER);
			setState(1133); match(ID);
			setState(1134); match(LEFT_BRACKET);
			setState(1136);
			_la = _input.LA(1);
			if (_la==KW_IN) {
				{
				setState(1135); init_param_decls();
				}
			}

			setState(1138); match(RIGHT_BRACKET);
			setState(1140);
			_la = _input.LA(1);
			if (_la==KW_RAISES) {
				{
				setState(1139); raises_expr();
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
		enterRule(_localctx, 264, RULE_event);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1145);
			switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
			case 1:
				{
				setState(1142); event_decl();
				}
				break;
			case 2:
				{
				setState(1143); event_abs_decl();
				}
				break;
			case 3:
				{
				setState(1144); event_forward_decl();
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
		enterRule(_localctx, 266, RULE_event_forward_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1148);
			_la = _input.LA(1);
			if (_la==KW_ABSTRACT) {
				{
				setState(1147); match(KW_ABSTRACT);
				}
			}

			setState(1150); match(KW_EVENTTYPE);
			setState(1151); match(ID);
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
		enterRule(_localctx, 268, RULE_event_abs_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1153); match(KW_ABSTRACT);
			setState(1154); match(KW_EVENTTYPE);
			setState(1155); match(ID);
			setState(1156); value_inheritance_spec();
			setState(1157); match(LEFT_BRACE);
			setState(1161);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE_COLON) | (1L << KW_STRING) | (1L << KW_TYPEDEF) | (1L << KW_OCTET) | (1L << KW_STRUCT) | (1L << KW_NATIVE) | (1L << KW_READONLY) | (1L << KW_VOID) | (1L << KW_WCHAR) | (1L << KW_SHORT) | (1L << KW_LONG) | (1L << KW_ENUM) | (1L << KW_WSTRING))) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (KW_EXCEPTION - 67)) | (1L << (KW_CONST - 67)) | (1L << (KW_VALUEBASE - 67)) | (1L << (KW_OBJECT - 67)) | (1L << (KW_UNSIGNED - 67)) | (1L << (KW_UNION - 67)) | (1L << (KW_ONEWAY - 67)) | (1L << (KW_ANY - 67)) | (1L << (KW_CHAR - 67)) | (1L << (KW_FLOAT - 67)) | (1L << (KW_BOOLEAN - 67)) | (1L << (KW_DOUBLE - 67)) | (1L << (KW_TYPEPREFIX - 67)) | (1L << (KW_TYPEID - 67)) | (1L << (KW_ATTRIBUTE - 67)) | (1L << (ID - 67)))) != 0)) {
				{
				{
				setState(1158); export();
				}
				}
				setState(1163);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1164); match(RIGHT_BRACE);
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
		enterRule(_localctx, 270, RULE_event_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1166); event_header();
			setState(1167); match(LEFT_BRACE);
			setState(1171);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE_COLON) | (1L << KW_STRING) | (1L << KW_TYPEDEF) | (1L << KW_OCTET) | (1L << KW_STRUCT) | (1L << KW_NATIVE) | (1L << KW_READONLY) | (1L << KW_VOID) | (1L << KW_PRIVATE) | (1L << KW_WCHAR) | (1L << KW_PUBLIC) | (1L << KW_SHORT) | (1L << KW_LONG) | (1L << KW_ENUM) | (1L << KW_WSTRING))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (KW_FACTORY - 66)) | (1L << (KW_EXCEPTION - 66)) | (1L << (KW_CONST - 66)) | (1L << (KW_VALUEBASE - 66)) | (1L << (KW_OBJECT - 66)) | (1L << (KW_UNSIGNED - 66)) | (1L << (KW_UNION - 66)) | (1L << (KW_ONEWAY - 66)) | (1L << (KW_ANY - 66)) | (1L << (KW_CHAR - 66)) | (1L << (KW_FLOAT - 66)) | (1L << (KW_BOOLEAN - 66)) | (1L << (KW_DOUBLE - 66)) | (1L << (KW_TYPEPREFIX - 66)) | (1L << (KW_TYPEID - 66)) | (1L << (KW_ATTRIBUTE - 66)) | (1L << (ID - 66)))) != 0)) {
				{
				{
				setState(1168); value_element();
				}
				}
				setState(1173);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1174); match(RIGHT_BRACE);
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
		enterRule(_localctx, 272, RULE_event_header);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1177);
			_la = _input.LA(1);
			if (_la==KW_CUSTOM) {
				{
				setState(1176); match(KW_CUSTOM);
				}
			}

			setState(1179); match(KW_EVENTTYPE);
			setState(1180); match(ID);
			setState(1181); value_inheritance_spec();
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3g\u04a2\4\2\t\2\4"+
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
		"\4\u008a\t\u008a\3\2\7\2\u0116\n\2\f\2\16\2\u0119\13\2\3\2\6\2\u011c\n"+
		"\2\r\2\16\2\u011d\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\5\3\u0141\n\3\3\4\3\4\3\4\3\4\6\4\u0147\n\4\r\4\16\4\u0148"+
		"\3\4\3\4\3\5\3\5\5\5\u014f\n\5\3\6\3\6\3\6\3\6\3\6\3\7\5\7\u0157\n\7\3"+
		"\7\3\7\3\7\3\b\5\b\u015d\n\b\3\b\3\b\3\b\5\b\u0162\n\b\3\t\7\t\u0165\n"+
		"\t\f\t\16\t\u0168\13\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u017f\n\n\3\13\3\13\3\13\3\13"+
		"\7\13\u0185\n\13\f\13\16\13\u0188\13\13\3\f\3\f\3\r\5\r\u018d\n\r\3\r"+
		"\3\r\3\r\7\r\u0192\n\r\f\r\16\r\u0195\13\r\3\16\3\16\3\16\3\16\5\16\u019b"+
		"\n\16\3\17\5\17\u019e\n\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\7\21\u01ad\n\21\f\21\16\21\u01b0\13\21\3\21\3\21"+
		"\3\22\3\22\3\22\7\22\u01b7\n\22\f\22\16\22\u01ba\13\22\3\22\3\22\3\23"+
		"\5\23\u01bf\n\23\3\23\3\23\3\23\3\23\3\24\3\24\5\24\u01c7\n\24\3\24\3"+
		"\24\3\24\7\24\u01cc\n\24\f\24\16\24\u01cf\13\24\5\24\u01d1\n\24\3\24\3"+
		"\24\3\24\3\24\7\24\u01d7\n\24\f\24\16\24\u01da\13\24\5\24\u01dc\n\24\3"+
		"\25\3\25\3\26\3\26\3\26\5\26\u01e3\n\26\3\27\3\27\3\27\3\27\3\27\3\30"+
		"\3\30\3\30\3\30\5\30\u01ee\n\30\3\30\3\30\5\30\u01f2\n\30\3\30\3\30\3"+
		"\31\3\31\3\31\7\31\u01f9\n\31\f\31\16\31\u01fc\13\31\3\32\3\32\3\32\3"+
		"\32\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3"+
		"\35\3\35\3\35\3\35\3\35\5\35\u0214\n\35\3\36\3\36\3\37\3\37\3\37\7\37"+
		"\u021b\n\37\f\37\16\37\u021e\13\37\3 \3 \3 \7 \u0223\n \f \16 \u0226\13"+
		" \3!\3!\3!\7!\u022b\n!\f!\16!\u022e\13!\3\"\3\"\3\"\7\"\u0233\n\"\f\""+
		"\16\"\u0236\13\"\3#\3#\3#\7#\u023b\n#\f#\16#\u023e\13#\3$\3$\3$\7$\u0243"+
		"\n$\f$\16$\u0246\13$\3%\3%\3%\3%\5%\u024c\n%\3&\3&\3\'\3\'\3\'\3\'\3\'"+
		"\3\'\5\'\u0256\n\'\3(\3(\3)\3)\3*\3*\3*\3*\3*\3*\3*\3*\5*\u0264\n*\3+"+
		"\3+\3+\3,\3,\5,\u026b\n,\3-\3-\3-\5-\u0270\n-\3.\3.\3.\3.\3.\3.\3.\3."+
		"\3.\5.\u027b\n.\3/\3/\3/\3/\5/\u0281\n/\3\60\3\60\3\60\5\60\u0286\n\60"+
		"\3\61\3\61\3\61\7\61\u028b\n\61\f\61\16\61\u028e\13\61\3\62\3\62\5\62"+
		"\u0292\n\62\3\63\3\63\3\64\3\64\3\65\3\65\3\65\3\65\5\65\u029c\n\65\3"+
		"\66\3\66\5\66\u02a0\n\66\3\67\3\67\3\67\5\67\u02a5\n\67\38\38\39\39\3"+
		":\3:\3:\3;\3;\3;\5;\u02b1\n;\3<\3<\3<\3=\3=\3=\3>\3>\3>\3>\3?\3?\3@\3"+
		"@\3A\3A\3B\3B\3C\3C\3D\3D\3E\3E\3E\3E\3E\3E\3F\6F\u02d0\nF\rF\16F\u02d1"+
		"\3G\3G\3G\3G\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3I\3I\3I\3I\3I\5I\u02e7\nI"+
		"\3J\6J\u02ea\nJ\rJ\16J\u02eb\3K\6K\u02ef\nK\rK\16K\u02f0\3K\3K\3K\3L\3"+
		"L\3L\3L\3L\3L\5L\u02fc\nL\3M\3M\3M\3N\3N\3N\3N\3N\3N\7N\u0307\nN\fN\16"+
		"N\u030a\13N\3N\3N\3O\3O\3P\3P\3P\3P\3P\3P\3P\3P\3P\3P\3P\3P\5P\u031c\n"+
		"P\3Q\3Q\3Q\3Q\3Q\3Q\5Q\u0324\nQ\3R\3R\3R\3R\3R\3R\5R\u032c\nR\3S\3S\6"+
		"S\u0330\nS\rS\16S\u0331\3T\3T\3T\3T\3U\3U\5U\u033a\nU\3V\3V\3V\3V\7V\u0340"+
		"\nV\fV\16V\u0343\13V\3V\3V\3W\5W\u0348\nW\3W\3W\3W\3W\5W\u034e\nW\3W\5"+
		"W\u0351\nW\3X\3X\3Y\3Y\5Y\u0357\nY\3Z\3Z\3Z\3Z\7Z\u035d\nZ\fZ\16Z\u0360"+
		"\13Z\3Z\3Z\3Z\3Z\5Z\u0366\nZ\3[\3[\3[\3[\3\\\3\\\3]\3]\3]\3]\3]\7]\u0373"+
		"\n]\f]\16]\u0376\13]\3]\3]\3^\3^\3^\3^\3^\7^\u037f\n^\f^\16^\u0382\13"+
		"^\3^\3^\3_\3_\3_\3_\5_\u038a\n_\3`\3`\3`\3`\3`\3`\3`\3a\3a\3b\3b\3c\3"+
		"c\3c\3c\5c\u039b\nc\3d\3d\3d\3d\3e\3e\5e\u03a3\ne\3f\3f\3f\3f\3g\3g\3"+
		"g\3g\3h\3h\3h\3h\3h\3i\3i\3i\3i\3i\3i\7i\u03b8\ni\fi\16i\u03bb\13i\5i"+
		"\u03bd\ni\3j\3j\3j\3j\3k\3k\3k\3k\3k\3k\7k\u03c9\nk\fk\16k\u03cc\13k\5"+
		"k\u03ce\nk\3l\3l\5l\u03d2\nl\3l\5l\u03d5\nl\3m\3m\3m\3n\3n\3n\3o\3o\3"+
		"o\3o\7o\u03e1\no\fo\16o\u03e4\13o\3o\3o\3p\3p\5p\u03ea\np\3q\3q\3q\3r"+
		"\3r\3r\3r\3r\3s\3s\3s\5s\u03f7\ns\3s\5s\u03fa\ns\3t\3t\3t\3t\7t\u0400"+
		"\nt\ft\16t\u0403\13t\3u\3u\3u\3v\7v\u0409\nv\fv\16v\u040c\13v\3w\3w\3"+
		"w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\5w\u0420\nw\3x\3x\3x\3"+
		"x\3y\3y\5y\u0428\ny\3z\3z\5z\u042c\nz\3z\3z\3z\3{\3{\3{\3{\3|\3|\3|\3"+
		"|\3}\3}\3}\3}\3~\3~\3~\3\177\3\177\3\177\5\177\u0443\n\177\3\177\5\177"+
		"\u0446\n\177\3\177\3\177\3\177\5\177\u044b\n\177\3\u0080\3\u0080\3\u0080"+
		"\3\u0081\3\u0081\3\u0081\3\u0082\3\u0082\7\u0082\u0455\n\u0082\f\u0082"+
		"\16\u0082\u0458\13\u0082\3\u0082\3\u0082\3\u0083\3\u0083\3\u0083\3\u0083"+
		"\3\u0083\3\u0083\3\u0083\5\u0083\u0463\n\u0083\3\u0084\3\u0084\3\u0084"+
		"\3\u0084\5\u0084\u0469\n\u0084\3\u0084\3\u0084\5\u0084\u046d\n\u0084\3"+
		"\u0085\3\u0085\3\u0085\3\u0085\5\u0085\u0473\n\u0085\3\u0085\3\u0085\5"+
		"\u0085\u0477\n\u0085\3\u0086\3\u0086\3\u0086\5\u0086\u047c\n\u0086\3\u0087"+
		"\5\u0087\u047f\n\u0087\3\u0087\3\u0087\3\u0087\3\u0088\3\u0088\3\u0088"+
		"\3\u0088\3\u0088\3\u0088\7\u0088\u048a\n\u0088\f\u0088\16\u0088\u048d"+
		"\13\u0088\3\u0088\3\u0088\3\u0089\3\u0089\3\u0089\7\u0089\u0494\n\u0089"+
		"\f\u0089\16\u0089\u0497\13\u0089\3\u0089\3\u0089\3\u008a\5\u008a\u049c"+
		"\n\u008a\3\u008a\3\u008a\3\u008a\3\u008a\3\u008a\2\2\u008b\2\4\6\b\n\f"+
		"\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^"+
		"`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090"+
		"\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8"+
		"\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0"+
		"\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8"+
		"\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0"+
		"\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106\u0108"+
		"\u010a\u010c\u010e\u0110\u0112\2\n\4\2XX``\4\288==\3\2#$\3\2\33\34\5\2"+
		"\27\27\32\32!!\4\2\26\26\33\34\4\2\3\3\5\f\5\2&&;;YY\u04b3\2\u0117\3\2"+
		"\2\2\4\u0140\3\2\2\2\6\u0142\3\2\2\2\b\u014e\3\2\2\2\n\u0150\3\2\2\2\f"+
		"\u0156\3\2\2\2\16\u015c\3\2\2\2\20\u0166\3\2\2\2\22\u017e\3\2\2\2\24\u0180"+
		"\3\2\2\2\26\u0189\3\2\2\2\30\u018c\3\2\2\2\32\u019a\3\2\2\2\34\u019d\3"+
		"\2\2\2\36\u01a2\3\2\2\2 \u01a6\3\2\2\2\"\u01b3\3\2\2\2$\u01be\3\2\2\2"+
		"&\u01d0\3\2\2\2(\u01dd\3\2\2\2*\u01e2\3\2\2\2,\u01e4\3\2\2\2.\u01e9\3"+
		"\2\2\2\60\u01f5\3\2\2\2\62\u01fd\3\2\2\2\64\u0201\3\2\2\2\66\u0203\3\2"+
		"\2\28\u0213\3\2\2\2:\u0215\3\2\2\2<\u0217\3\2\2\2>\u021f\3\2\2\2@\u0227"+
		"\3\2\2\2B\u022f\3\2\2\2D\u0237\3\2\2\2F\u023f\3\2\2\2H\u024b\3\2\2\2J"+
		"\u024d\3\2\2\2L\u0255\3\2\2\2N\u0257\3\2\2\2P\u0259\3\2\2\2R\u0263\3\2"+
		"\2\2T\u0265\3\2\2\2V\u026a\3\2\2\2X\u026f\3\2\2\2Z\u027a\3\2\2\2\\\u0280"+
		"\3\2\2\2^\u0285\3\2\2\2`\u0287\3\2\2\2b\u0291\3\2\2\2d\u0293\3\2\2\2f"+
		"\u0295\3\2\2\2h\u029b\3\2\2\2j\u029f\3\2\2\2l\u02a4\3\2\2\2n\u02a6\3\2"+
		"\2\2p\u02a8\3\2\2\2r\u02aa\3\2\2\2t\u02b0\3\2\2\2v\u02b2\3\2\2\2x\u02b5"+
		"\3\2\2\2z\u02b8\3\2\2\2|\u02bc\3\2\2\2~\u02be\3\2\2\2\u0080\u02c0\3\2"+
		"\2\2\u0082\u02c2\3\2\2\2\u0084\u02c4\3\2\2\2\u0086\u02c6\3\2\2\2\u0088"+
		"\u02c8\3\2\2\2\u008a\u02cf\3\2\2\2\u008c\u02d3\3\2\2\2\u008e\u02d7\3\2"+
		"\2\2\u0090\u02e6\3\2\2\2\u0092\u02e9\3\2\2\2\u0094\u02ee\3\2\2\2\u0096"+
		"\u02fb\3\2\2\2\u0098\u02fd\3\2\2\2\u009a\u0300\3\2\2\2\u009c\u030d\3\2"+
		"\2\2\u009e\u031b\3\2\2\2\u00a0\u0323\3\2\2\2\u00a2\u032b\3\2\2\2\u00a4"+
		"\u032d\3\2\2\2\u00a6\u0333\3\2\2\2\u00a8\u0339\3\2\2\2\u00aa\u033b\3\2"+
		"\2\2\u00ac\u0347\3\2\2\2\u00ae\u0352\3\2\2\2\u00b0\u0356\3\2\2\2\u00b2"+
		"\u0365\3\2\2\2\u00b4\u0367\3\2\2\2\u00b6\u036b\3\2\2\2\u00b8\u036d\3\2"+
		"\2\2\u00ba\u0379\3\2\2\2\u00bc\u0389\3\2\2\2\u00be\u038b\3\2\2\2\u00c0"+
		"\u0392\3\2\2\2\u00c2\u0394\3\2\2\2\u00c4\u039a\3\2\2\2\u00c6\u039c\3\2"+
		"\2\2\u00c8\u03a2\3\2\2\2\u00ca\u03a4\3\2\2\2\u00cc\u03a8\3\2\2\2\u00ce"+
		"\u03ac\3\2\2\2\u00d0\u03bc\3\2\2\2\u00d2\u03be\3\2\2\2\u00d4\u03cd\3\2"+
		"\2\2\u00d6\u03d4\3\2\2\2\u00d8\u03d6\3\2\2\2\u00da\u03d9\3\2\2\2\u00dc"+
		"\u03dc\3\2\2\2\u00de\u03e9\3\2\2\2\u00e0\u03eb\3\2\2\2\u00e2\u03ee\3\2"+
		"\2\2\u00e4\u03f3\3\2\2\2\u00e6\u03fb\3\2\2\2\u00e8\u0404\3\2\2\2\u00ea"+
		"\u040a\3\2\2\2\u00ec\u041f\3\2\2\2\u00ee\u0421\3\2\2\2\u00f0\u0427\3\2"+
		"\2\2\u00f2\u0429\3\2\2\2\u00f4\u0430\3\2\2\2\u00f6\u0434\3\2\2\2\u00f8"+
		"\u0438\3\2\2\2\u00fa\u043c\3\2\2\2\u00fc\u043f\3\2\2\2\u00fe\u044c\3\2"+
		"\2\2\u0100\u044f\3\2\2\2\u0102\u0452\3\2\2\2\u0104\u0462\3\2\2\2\u0106"+
		"\u0464\3\2\2\2\u0108\u046e\3\2\2\2\u010a\u047b\3\2\2\2\u010c\u047e\3\2"+
		"\2\2\u010e\u0483\3\2\2\2\u0110\u0490\3\2\2\2\u0112\u049b\3\2\2\2\u0114"+
		"\u0116\5\u00c6d\2\u0115\u0114\3\2\2\2\u0116\u0119\3\2\2\2\u0117\u0115"+
		"\3\2\2\2\u0117\u0118\3\2\2\2\u0118\u011b\3\2\2\2\u0119\u0117\3\2\2\2\u011a"+
		"\u011c\5\4\3\2\u011b\u011a\3\2\2\2\u011c\u011d\3\2\2\2\u011d\u011b\3\2"+
		"\2\2\u011d\u011e\3\2\2\2\u011e\3\3\2\2\2\u011f\u0120\5R*\2\u0120\u0121"+
		"\7\r\2\2\u0121\u0141\3\2\2\2\u0122\u0123\5\66\34\2\u0123\u0124\7\r\2\2"+
		"\u0124\u0141\3\2\2\2\u0125\u0126\5\u00aaV\2\u0126\u0127\7\r\2\2\u0127"+
		"\u0141\3\2\2\2\u0128\u0129\5\b\5\2\u0129\u012a\7\r\2\2\u012a\u0141\3\2"+
		"\2\2\u012b\u012c\5\6\4\2\u012c\u012d\7\r\2\2\u012d\u0141\3\2\2\2\u012e"+
		"\u012f\5\32\16\2\u012f\u0130\7\r\2\2\u0130\u0141\3\2\2\2\u0131\u0132\5"+
		"\u00caf\2\u0132\u0133\7\r\2\2\u0133\u0141\3\2\2\2\u0134\u0135\5\u00cc"+
		"g\2\u0135\u0136\7\r\2\2\u0136\u0141\3\2\2\2\u0137\u0138\5\u010a\u0086"+
		"\2\u0138\u0139\7\r\2\2\u0139\u0141\3\2\2\2\u013a\u013b\5\u00dep\2\u013b"+
		"\u013c\7\r\2\2\u013c\u0141\3\2\2\2\u013d\u013e\5\u00fa~\2\u013e\u013f"+
		"\7\r\2\2\u013f\u0141\3\2\2\2\u0140\u011f\3\2\2\2\u0140\u0122\3\2\2\2\u0140"+
		"\u0125\3\2\2\2\u0140\u0128\3\2\2\2\u0140\u012b\3\2\2\2\u0140\u012e\3\2"+
		"\2\2\u0140\u0131\3\2\2\2\u0140\u0134\3\2\2\2\u0140\u0137\3\2\2\2\u0140"+
		"\u013a\3\2\2\2\u0140\u013d\3\2\2\2\u0141\5\3\2\2\2\u0142\u0143\7K\2\2"+
		"\u0143\u0144\7d\2\2\u0144\u0146\7\20\2\2\u0145\u0147\5\4\3\2\u0146\u0145"+
		"\3\2\2\2\u0147\u0148\3\2\2\2\u0148\u0146\3\2\2\2\u0148\u0149\3\2\2\2\u0149"+
		"\u014a\3\2\2\2\u014a\u014b\7\21\2\2\u014b\7\3\2\2\2\u014c\u014f\5\n\6"+
		"\2\u014d\u014f\5\f\7\2\u014e\u014c\3\2\2\2\u014e\u014d\3\2\2\2\u014f\t"+
		"\3\2\2\2\u0150\u0151\5\16\b\2\u0151\u0152\7\20\2\2\u0152\u0153\5\20\t"+
		"\2\u0153\u0154\7\21\2\2\u0154\13\3\2\2\2\u0155\u0157\t\2\2\2\u0156\u0155"+
		"\3\2\2\2\u0156\u0157\3\2\2\2\u0157\u0158\3\2\2\2\u0158\u0159\7b\2\2\u0159"+
		"\u015a\7d\2\2\u015a\r\3\2\2\2\u015b\u015d\t\2\2\2\u015c\u015b\3\2\2\2"+
		"\u015c\u015d\3\2\2\2\u015d\u015e\3\2\2\2\u015e\u015f\7b\2\2\u015f\u0161"+
		"\7d\2\2\u0160\u0162\5\24\13\2\u0161\u0160\3\2\2\2\u0161\u0162\3\2\2\2"+
		"\u0162\17\3\2\2\2\u0163\u0165\5\22\n\2\u0164\u0163\3\2\2\2\u0165\u0168"+
		"\3\2\2\2\u0166\u0164\3\2\2\2\u0166\u0167\3\2\2\2\u0167\21\3\2\2\2\u0168"+
		"\u0166\3\2\2\2\u0169\u016a\5R*\2\u016a\u016b\7\r\2\2\u016b\u017f\3\2\2"+
		"\2\u016c\u016d\5\66\34\2\u016d\u016e\7\r\2\2\u016e\u017f\3\2\2\2\u016f"+
		"\u0170\5\u00aaV\2\u0170\u0171\7\r\2\2\u0171\u017f\3\2\2\2\u0172\u0173"+
		"\5\u00a8U\2\u0173\u0174\7\r\2\2\u0174\u017f\3\2\2\2\u0175\u0176\5\u00ac"+
		"W\2\u0176\u0177\7\r\2\2\u0177\u017f\3\2\2\2\u0178\u0179\5\u00caf\2\u0179"+
		"\u017a\7\r\2\2\u017a\u017f\3\2\2\2\u017b\u017c\5\u00ccg\2\u017c\u017d"+
		"\7\r\2\2\u017d\u017f\3\2\2\2\u017e\u0169\3\2\2\2\u017e\u016c\3\2\2\2\u017e"+
		"\u016f\3\2\2\2\u017e\u0172\3\2\2\2\u017e\u0175\3\2\2\2\u017e\u0178\3\2"+
		"\2\2\u017e\u017b\3\2\2\2\u017f\23\3\2\2\2\u0180\u0181\7\16\2\2\u0181\u0186"+
		"\5\26\f\2\u0182\u0183\7\17\2\2\u0183\u0185\5\26\f\2\u0184\u0182\3\2\2"+
		"\2\u0185\u0188\3\2\2\2\u0186\u0184\3\2\2\2\u0186\u0187\3\2\2\2\u0187\25"+
		"\3\2\2\2\u0188\u0186\3\2\2\2\u0189\u018a\5\30\r\2\u018a\27\3\2\2\2\u018b"+
		"\u018d\7\"\2\2\u018c\u018b\3\2\2\2\u018c\u018d\3\2\2\2\u018d\u018e\3\2"+
		"\2\2\u018e\u0193\7d\2\2\u018f\u0190\7\"\2\2\u0190\u0192\7d\2\2\u0191\u018f"+
		"\3\2\2\2\u0192\u0195\3\2\2\2\u0193\u0191\3\2\2\2\u0193\u0194\3\2\2\2\u0194"+
		"\31\3\2\2\2\u0195\u0193\3\2\2\2\u0196\u019b\5\"\22\2\u0197\u019b\5 \21"+
		"\2\u0198\u019b\5\36\20\2\u0199\u019b\5\34\17\2\u019a\u0196\3\2\2\2\u019a"+
		"\u0197\3\2\2\2\u019a\u0198\3\2\2\2\u019a\u0199\3\2\2\2\u019b\33\3\2\2"+
		"\2\u019c\u019e\7X\2\2\u019d\u019c\3\2\2\2\u019d\u019e\3\2\2\2\u019e\u019f"+
		"\3\2\2\2\u019f\u01a0\7I\2\2\u01a0\u01a1\7d\2\2\u01a1\35\3\2\2\2\u01a2"+
		"\u01a3\7I\2\2\u01a3\u01a4\7d\2\2\u01a4\u01a5\5V,\2\u01a5\37\3\2\2\2\u01a6"+
		"\u01a7\7X\2\2\u01a7\u01a8\7I\2\2\u01a8\u01a9\7d\2\2\u01a9\u01aa\5&\24"+
		"\2\u01aa\u01ae\7\20\2\2\u01ab\u01ad\5\22\n\2\u01ac\u01ab\3\2\2\2\u01ad"+
		"\u01b0\3\2\2\2\u01ae\u01ac\3\2\2\2\u01ae\u01af\3\2\2\2\u01af\u01b1\3\2"+
		"\2\2\u01b0\u01ae\3\2\2\2\u01b1\u01b2\7\21\2\2\u01b2!\3\2\2\2\u01b3\u01b4"+
		"\5$\23\2\u01b4\u01b8\7\20\2\2\u01b5\u01b7\5*\26\2\u01b6\u01b5\3\2\2\2"+
		"\u01b7\u01ba\3\2\2\2\u01b8\u01b6\3\2\2\2\u01b8\u01b9\3\2\2\2\u01b9\u01bb"+
		"\3\2\2\2\u01ba\u01b8\3\2\2\2\u01bb\u01bc\7\21\2\2\u01bc#\3\2\2\2\u01bd"+
		"\u01bf\7.\2\2\u01be\u01bd\3\2\2\2\u01be\u01bf\3\2\2\2\u01bf\u01c0\3\2"+
		"\2\2\u01c0\u01c1\7I\2\2\u01c1\u01c2\7d\2\2\u01c2\u01c3\5&\24\2\u01c3%"+
		"\3\2\2\2\u01c4\u01c6\7\16\2\2\u01c5\u01c7\7M\2\2\u01c6\u01c5\3\2\2\2\u01c6"+
		"\u01c7\3\2\2\2\u01c7\u01c8\3\2\2\2\u01c8\u01cd\5(\25\2\u01c9\u01ca\7\17"+
		"\2\2\u01ca\u01cc\5(\25\2\u01cb\u01c9\3\2\2\2\u01cc\u01cf\3\2\2\2\u01cd"+
		"\u01cb\3\2\2\2\u01cd\u01ce\3\2\2\2\u01ce\u01d1\3\2\2\2\u01cf\u01cd\3\2"+
		"\2\2\u01d0\u01c4\3\2\2\2\u01d0\u01d1\3\2\2\2\u01d1\u01db\3\2\2\2\u01d2"+
		"\u01d3\7J\2\2\u01d3\u01d8\5\26\f\2\u01d4\u01d5\7\17\2\2\u01d5\u01d7\5"+
		"\26\f\2\u01d6\u01d4\3\2\2\2\u01d7\u01da\3\2\2\2\u01d8\u01d6\3\2\2\2\u01d8"+
		"\u01d9\3\2\2\2\u01d9\u01dc\3\2\2\2\u01da\u01d8\3\2\2\2\u01db\u01d2\3\2"+
		"\2\2\u01db\u01dc\3\2\2\2\u01dc\'\3\2\2\2\u01dd\u01de\5\30\r\2\u01de)\3"+
		"\2\2\2\u01df\u01e3\5\22\n\2\u01e0\u01e3\5,\27\2\u01e1\u01e3\5.\30\2\u01e2"+
		"\u01df\3\2\2\2\u01e2\u01e0\3\2\2\2\u01e2\u01e1\3\2\2\2\u01e3+\3\2\2\2"+
		"\u01e4\u01e5\t\3\2\2\u01e5\u01e6\5V,\2\u01e6\u01e7\5`\61\2\u01e7\u01e8"+
		"\7\r\2\2\u01e8-\3\2\2\2\u01e9\u01ea\7D\2\2\u01ea\u01eb\7d\2\2\u01eb\u01ed"+
		"\7\22\2\2\u01ec\u01ee\5\60\31\2\u01ed\u01ec\3\2\2\2\u01ed\u01ee\3\2\2"+
		"\2\u01ee\u01ef\3\2\2\2\u01ef\u01f1\7\23\2\2\u01f0\u01f2\5\u00b8]\2\u01f1"+
		"\u01f0\3\2\2\2\u01f1\u01f2\3\2\2\2\u01f2\u01f3\3\2\2\2\u01f3\u01f4\7\r"+
		"\2\2\u01f4/\3\2\2\2\u01f5\u01fa\5\62\32\2\u01f6\u01f7\7\17\2\2\u01f7\u01f9"+
		"\5\62\32\2\u01f8\u01f6\3\2\2\2\u01f9\u01fc\3\2\2\2\u01fa\u01f8\3\2\2\2"+
		"\u01fa\u01fb\3\2\2\2\u01fb\61\3\2\2\2\u01fc\u01fa\3\2\2\2\u01fd\u01fe"+
		"\5\64\33\2\u01fe\u01ff\5\u00bc_\2\u01ff\u0200\5d\63\2\u0200\63\3\2\2\2"+
		"\u0201\u0202\7;\2\2\u0202\65\3\2\2\2\u0203\u0204\7G\2\2\u0204\u0205\5"+
		"8\35\2\u0205\u0206\7d\2\2\u0206\u0207\7 \2\2\u0207\u0208\5:\36\2\u0208"+
		"\67\3\2\2\2\u0209\u0214\5j\66\2\u020a\u0214\5|?\2\u020b\u0214\5~@\2\u020c"+
		"\u0214\5\u0080A\2\u020d\u0214\5h\65\2\u020e\u0214\5\u00a0Q\2\u020f\u0214"+
		"\5\u00a2R\2\u0210\u0214\5\u00c0a\2\u0211\u0214\5\30\r\2\u0212\u0214\5"+
		"\u0082B\2\u0213\u0209\3\2\2\2\u0213\u020a\3\2\2\2\u0213\u020b\3\2\2\2"+
		"\u0213\u020c\3\2\2\2\u0213\u020d\3\2\2\2\u0213\u020e\3\2\2\2\u0213\u020f"+
		"\3\2\2\2\u0213\u0210\3\2\2\2\u0213\u0211\3\2\2\2\u0213\u0212\3\2\2\2\u0214"+
		"9\3\2\2\2\u0215\u0216\5<\37\2\u0216;\3\2\2\2\u0217\u021c\5> \2\u0218\u0219"+
		"\7\37\2\2\u0219\u021b\5> \2\u021a\u0218\3\2\2\2\u021b\u021e\3\2\2\2\u021c"+
		"\u021a\3\2\2\2\u021c\u021d\3\2\2\2\u021d=\3\2\2\2\u021e\u021c\3\2\2\2"+
		"\u021f\u0224\5@!\2\u0220\u0221\7\35\2\2\u0221\u0223\5@!\2\u0222\u0220"+
		"\3\2\2\2\u0223\u0226\3\2\2\2\u0224\u0222\3\2\2\2\u0224\u0225\3\2\2\2\u0225"+
		"?\3\2\2\2\u0226\u0224\3\2\2\2\u0227\u022c\5B\"\2\u0228\u0229\7\36\2\2"+
		"\u0229\u022b\5B\"\2\u022a\u0228\3\2\2\2\u022b\u022e\3\2\2\2\u022c\u022a"+
		"\3\2\2\2\u022c\u022d\3\2\2\2\u022dA\3\2\2\2\u022e\u022c\3\2\2\2\u022f"+
		"\u0234\5D#\2\u0230\u0231\t\4\2\2\u0231\u0233\5D#\2\u0232\u0230\3\2\2\2"+
		"\u0233\u0236\3\2\2\2\u0234\u0232\3\2\2\2\u0234\u0235\3\2\2\2\u0235C\3"+
		"\2\2\2\u0236\u0234\3\2\2\2\u0237\u023c\5F$\2\u0238\u0239\t\5\2\2\u0239"+
		"\u023b\5F$\2\u023a\u0238\3\2\2\2\u023b\u023e\3\2\2\2\u023c\u023a\3\2\2"+
		"\2\u023c\u023d\3\2\2\2\u023dE\3\2\2\2\u023e\u023c\3\2\2\2\u023f\u0244"+
		"\5H%\2\u0240\u0241\t\6\2\2\u0241\u0243\5H%\2\u0242\u0240\3\2\2\2\u0243"+
		"\u0246\3\2\2\2\u0244\u0242\3\2\2\2\u0244\u0245\3\2\2\2\u0245G\3\2\2\2"+
		"\u0246\u0244\3\2\2\2\u0247\u0248\5J&\2\u0248\u0249\5L\'\2\u0249\u024c"+
		"\3\2\2\2\u024a\u024c\5L\'\2\u024b\u0247\3\2\2\2\u024b\u024a\3\2\2\2\u024c"+
		"I\3\2\2\2\u024d\u024e\t\7\2\2\u024eK\3\2\2\2\u024f\u0256\5\30\r\2\u0250"+
		"\u0256\5N(\2\u0251\u0252\7\22\2\2\u0252\u0253\5:\36\2\u0253\u0254\7\23"+
		"\2\2\u0254\u0256\3\2\2\2\u0255\u024f\3\2\2\2\u0255\u0250\3\2\2\2\u0255"+
		"\u0251\3\2\2\2\u0256M\3\2\2\2\u0257\u0258\t\b\2\2\u0258O\3\2\2\2\u0259"+
		"\u025a\5:\36\2\u025aQ\3\2\2\2\u025b\u025c\7+\2\2\u025c\u0264\5T+\2\u025d"+
		"\u0264\5\u0088E\2\u025e\u0264\5\u008eH\2\u025f\u0264\5\u009aN\2\u0260"+
		"\u0261\7\63\2\2\u0261\u0264\5d\63\2\u0262\u0264\5\u00c4c\2\u0263\u025b"+
		"\3\2\2\2\u0263\u025d\3\2\2\2\u0263\u025e\3\2\2\2\u0263\u025f\3\2\2\2\u0263"+
		"\u0260\3\2\2\2\u0263\u0262\3\2\2\2\u0264S\3\2\2\2\u0265\u0266\5V,\2\u0266"+
		"\u0267\5`\61\2\u0267U\3\2\2\2\u0268\u026b\5X-\2\u0269\u026b\5^\60\2\u026a"+
		"\u0268\3\2\2\2\u026a\u0269\3\2\2\2\u026bW\3\2\2\2\u026c\u0270\5Z.\2\u026d"+
		"\u0270\5\\/\2\u026e\u0270\5\30\r\2\u026f\u026c\3\2\2\2\u026f\u026d\3\2"+
		"\2\2\u026f\u026e\3\2\2\2\u0270Y\3\2\2\2\u0271\u027b\5h\65\2\u0272\u027b"+
		"\5j\66\2\u0273\u027b\5|?\2\u0274\u027b\5~@\2\u0275\u027b\5\u0080A\2\u0276"+
		"\u027b\5\u0082B\2\u0277\u027b\5\u0084C\2\u0278\u027b\5\u0086D\2\u0279"+
		"\u027b\5\u00c2b\2\u027a\u0271\3\2\2\2\u027a\u0272\3\2\2\2\u027a\u0273"+
		"\3\2\2\2\u027a\u0274\3\2\2\2\u027a\u0275\3\2\2\2\u027a\u0276\3\2\2\2\u027a"+
		"\u0277\3\2\2\2\u027a\u0278\3\2\2\2\u027a\u0279\3\2\2\2\u027b[\3\2\2\2"+
		"\u027c\u0281\5\u009eP\2\u027d\u0281\5\u00a0Q\2\u027e\u0281\5\u00a2R\2"+
		"\u027f\u0281\5\u00be`\2\u0280\u027c\3\2\2\2\u0280\u027d\3\2\2\2\u0280"+
		"\u027e\3\2\2\2\u0280\u027f\3\2\2\2\u0281]\3\2\2\2\u0282\u0286\5\u0088"+
		"E\2\u0283\u0286\5\u008eH\2\u0284\u0286\5\u009aN\2\u0285\u0282\3\2\2\2"+
		"\u0285\u0283\3\2\2\2\u0285\u0284\3\2\2\2\u0286_\3\2\2\2\u0287\u028c\5"+
		"b\62\2\u0288\u0289\7\17\2\2\u0289\u028b\5b\62\2\u028a\u0288\3\2\2\2\u028b"+
		"\u028e\3\2\2\2\u028c\u028a\3\2\2\2\u028c\u028d\3\2\2\2\u028da\3\2\2\2"+
		"\u028e\u028c\3\2\2\2\u028f\u0292\5d\63\2\u0290\u0292\5f\64\2\u0291\u028f"+
		"\3\2\2\2\u0291\u0290\3\2\2\2\u0292c\3\2\2\2\u0293\u0294\7d\2\2\u0294e"+
		"\3\2\2\2\u0295\u0296\5\u00a4S\2\u0296g\3\2\2\2\u0297\u029c\7U\2\2\u0298"+
		"\u029c\7\\\2\2\u0299\u029a\7?\2\2\u029a\u029c\7\\\2\2\u029b\u0297\3\2"+
		"\2\2\u029b\u0298\3\2\2\2\u029b\u0299\3\2\2\2\u029ci\3\2\2\2\u029d\u02a0"+
		"\5l\67\2\u029e\u02a0\5t;\2\u029f\u029d\3\2\2\2\u029f\u029e\3\2\2\2\u02a0"+
		"k\3\2\2\2\u02a1\u02a5\5n8\2\u02a2\u02a5\5p9\2\u02a3\u02a5\5r:\2\u02a4"+
		"\u02a1\3\2\2\2\u02a4\u02a2\3\2\2\2\u02a4\u02a3\3\2\2\2\u02a5m\3\2\2\2"+
		"\u02a6\u02a7\7>\2\2\u02a7o\3\2\2\2\u02a8\u02a9\7?\2\2\u02a9q\3\2\2\2\u02aa"+
		"\u02ab\7?\2\2\u02ab\u02ac\7?\2\2\u02acs\3\2\2\2\u02ad\u02b1\5v<\2\u02ae"+
		"\u02b1\5x=\2\u02af\u02b1\5z>\2\u02b0\u02ad\3\2\2\2\u02b0\u02ae\3\2\2\2"+
		"\u02b0\u02af\3\2\2\2\u02b1u\3\2\2\2\u02b2\u02b3\7N\2\2\u02b3\u02b4\7>"+
		"\2\2\u02b4w\3\2\2\2\u02b5\u02b6\7N\2\2\u02b6\u02b7\7?\2\2\u02b7y\3\2\2"+
		"\2\u02b8\u02b9\7N\2\2\u02b9\u02ba\7?\2\2\u02ba\u02bb\7?\2\2\u02bb{\3\2"+
		"\2\2\u02bc\u02bd\7S\2\2\u02bd}\3\2\2\2\u02be\u02bf\7:\2\2\u02bf\177\3"+
		"\2\2\2\u02c0\u02c1\7V\2\2\u02c1\u0081\3\2\2\2\u02c2\u02c3\7/\2\2\u02c3"+
		"\u0083\3\2\2\2\u02c4\u02c5\7R\2\2\u02c5\u0085\3\2\2\2\u02c6\u02c7\7L\2"+
		"\2\u02c7\u0087\3\2\2\2\u02c8\u02c9\7\62\2\2\u02c9\u02ca\7d\2\2\u02ca\u02cb"+
		"\7\20\2\2\u02cb\u02cc\5\u008aF\2\u02cc\u02cd\7\21\2\2\u02cd\u0089\3\2"+
		"\2\2\u02ce\u02d0\5\u008cG\2\u02cf\u02ce\3\2\2\2\u02d0\u02d1\3\2\2\2\u02d1"+
		"\u02cf\3\2\2\2\u02d1\u02d2\3\2\2\2\u02d2\u008b\3\2\2\2\u02d3\u02d4\5V"+
		",\2\u02d4\u02d5\5`\61\2\u02d5\u02d6\7\r\2\2\u02d6\u008d\3\2\2\2\u02d7"+
		"\u02d8\7P\2\2\u02d8\u02d9\7d\2\2\u02d9\u02da\7)\2\2\u02da\u02db\7\22\2"+
		"\2\u02db\u02dc\5\u0090I\2\u02dc\u02dd\7\23\2\2\u02dd\u02de\7\20\2\2\u02de"+
		"\u02df\5\u0092J\2\u02df\u02e0\7\21\2\2\u02e0\u008f\3\2\2\2\u02e1\u02e7"+
		"\5j\66\2\u02e2\u02e7\5|?\2\u02e3\u02e7\5\u0080A\2\u02e4\u02e7\5\u009a"+
		"N\2\u02e5\u02e7\5\30\r\2\u02e6\u02e1\3\2\2\2\u02e6\u02e2\3\2\2\2\u02e6"+
		"\u02e3\3\2\2\2\u02e6\u02e4\3\2\2\2\u02e6\u02e5\3\2\2\2\u02e7\u0091\3\2"+
		"\2\2\u02e8\u02ea\5\u0094K\2\u02e9\u02e8\3\2\2\2\u02ea\u02eb\3\2\2\2\u02eb"+
		"\u02e9\3\2\2\2\u02eb\u02ec\3\2\2\2\u02ec\u0093\3\2\2\2\u02ed\u02ef\5\u0096"+
		"L\2\u02ee\u02ed\3\2\2\2\u02ef\u02f0\3\2\2\2\u02f0\u02ee\3\2\2\2\u02f0"+
		"\u02f1\3\2\2\2\u02f1\u02f2\3\2\2\2\u02f2\u02f3\5\u0098M\2\u02f3\u02f4"+
		"\7\r\2\2\u02f4\u0095\3\2\2\2\u02f5\u02f6\7T\2\2\u02f6\u02f7\5:\36\2\u02f7"+
		"\u02f8\7\16\2\2\u02f8\u02fc\3\2\2\2\u02f9\u02fa\7<\2\2\u02fa\u02fc\7\16"+
		"\2\2\u02fb\u02f5\3\2\2\2\u02fb\u02f9\3\2\2\2\u02fc\u0097\3\2\2\2\u02fd"+
		"\u02fe\5V,\2\u02fe\u02ff\5b\62\2\u02ff\u0099\3\2\2\2\u0300\u0301\7@\2"+
		"\2\u0301\u0302\7d\2\2\u0302\u0303\7\20\2\2\u0303\u0308\5\u009cO\2\u0304"+
		"\u0305\7\17\2\2\u0305\u0307\5\u009cO\2\u0306\u0304\3\2\2\2\u0307\u030a"+
		"\3\2\2\2\u0308\u0306\3\2\2\2\u0308\u0309\3\2\2\2\u0309\u030b\3\2\2\2\u030a"+
		"\u0308\3\2\2\2\u030b\u030c\7\21\2\2\u030c\u009b\3\2\2\2\u030d\u030e\7"+
		"d\2\2\u030e\u009d\3\2\2\2\u030f\u0310\7\60\2\2\u0310\u0311\7\30\2\2\u0311"+
		"\u0312\5X-\2\u0312\u0313\7\17\2\2\u0313\u0314\5P)\2\u0314\u0315\7\31\2"+
		"\2\u0315\u031c\3\2\2\2\u0316\u0317\7\60\2\2\u0317\u0318\7\30\2\2\u0318"+
		"\u0319\5X-\2\u0319\u031a\7\31\2\2\u031a\u031c\3\2\2\2\u031b\u030f\3\2"+
		"\2\2\u031b\u0316\3\2\2\2\u031c\u009f\3\2\2\2\u031d\u031e\7(\2\2\u031e"+
		"\u031f\7\30\2\2\u031f\u0320\5P)\2\u0320\u0321\7\31\2\2\u0321\u0324\3\2"+
		"\2\2\u0322\u0324\7(\2\2\u0323\u031d\3\2\2\2\u0323\u0322\3\2\2\2\u0324"+
		"\u00a1\3\2\2\2\u0325\u0326\7A\2\2\u0326\u0327\7\30\2\2\u0327\u0328\5P"+
		")\2\u0328\u0329\7\31\2\2\u0329\u032c\3\2\2\2\u032a\u032c\7A\2\2\u032b"+
		"\u0325\3\2\2\2\u032b\u032a\3\2\2\2\u032c\u00a3\3\2\2\2\u032d\u032f\7d"+
		"\2\2\u032e\u0330\5\u00a6T\2\u032f\u032e\3\2\2\2\u0330\u0331\3\2\2\2\u0331"+
		"\u032f\3\2\2\2\u0331\u0332\3\2\2\2\u0332\u00a5\3\2\2\2\u0333\u0334\7\24"+
		"\2\2\u0334\u0335\5P)\2\u0335\u0336\7\25\2\2\u0336\u00a7\3\2\2\2\u0337"+
		"\u033a\5\u00ceh\2\u0338\u033a\5\u00d2j\2\u0339\u0337\3\2\2\2\u0339\u0338"+
		"\3\2\2\2\u033a\u00a9\3\2\2\2\u033b\u033c\7E\2\2\u033c\u033d\7d\2\2\u033d"+
		"\u0341\7\20\2\2\u033e\u0340\5\u008cG\2\u033f\u033e\3\2\2\2\u0340\u0343"+
		"\3\2\2\2\u0341\u033f\3\2\2\2\u0341\u0342\3\2\2\2\u0342\u0344\3\2\2\2\u0343"+
		"\u0341\3\2\2\2\u0344\u0345\7\21\2\2\u0345\u00ab\3\2\2\2\u0346\u0348\5"+
		"\u00aeX\2\u0347\u0346\3\2\2\2\u0347\u0348\3\2\2\2\u0348\u0349\3\2\2\2"+
		"\u0349\u034a\5\u00b0Y\2\u034a\u034b\7d\2\2\u034b\u034d\5\u00b2Z\2\u034c"+
		"\u034e\5\u00b8]\2\u034d\u034c\3\2\2\2\u034d\u034e\3\2\2\2\u034e\u0350"+
		"\3\2\2\2\u034f\u0351\5\u00ba^\2\u0350\u034f\3\2\2\2\u0350\u0351\3\2\2"+
		"\2\u0351\u00ad\3\2\2\2\u0352\u0353\7Q\2\2\u0353\u00af\3\2\2\2\u0354\u0357"+
		"\5\u00bc_\2\u0355\u0357\7\67\2\2\u0356\u0354\3\2\2\2\u0356\u0355\3\2\2"+
		"\2\u0357\u00b1\3\2\2\2\u0358\u0359\7\22\2\2\u0359\u035e\5\u00b4[\2\u035a"+
		"\u035b\7\17\2\2\u035b\u035d\5\u00b4[\2\u035c\u035a\3\2\2\2\u035d\u0360"+
		"\3\2\2\2\u035e\u035c\3\2\2\2\u035e\u035f\3\2\2\2\u035f\u0361\3\2\2\2\u0360"+
		"\u035e\3\2\2\2\u0361\u0362\7\23\2\2\u0362\u0366\3\2\2\2\u0363\u0364\7"+
		"\22\2\2\u0364\u0366\7\23\2\2\u0365\u0358\3\2\2\2\u0365\u0363\3\2\2\2\u0366"+
		"\u00b3\3\2\2\2\u0367\u0368\5\u00b6\\\2\u0368\u0369\5\u00bc_\2\u0369\u036a"+
		"\5d\63\2\u036a\u00b5\3\2\2\2\u036b\u036c\t\t\2\2\u036c\u00b7\3\2\2\2\u036d"+
		"\u036e\7\66\2\2\u036e\u036f\7\22\2\2\u036f\u0374\5\30\r\2\u0370\u0371"+
		"\7\17\2\2\u0371\u0373\5\30\r\2\u0372\u0370\3\2\2\2\u0373\u0376\3\2\2\2"+
		"\u0374\u0372\3\2\2\2\u0374\u0375\3\2\2\2\u0375\u0377\3\2\2\2\u0376\u0374"+
		"\3\2\2\2\u0377\u0378\7\23\2\2\u0378\u00b9\3\2\2\2\u0379\u037a\7B\2\2\u037a"+
		"\u037b\7\22\2\2\u037b\u0380\7\13\2\2\u037c\u037d\7\17\2\2\u037d\u037f"+
		"\7\13\2\2\u037e\u037c\3\2\2\2\u037f\u0382\3\2\2\2\u0380\u037e\3\2\2\2"+
		"\u0380\u0381\3\2\2\2\u0381\u0383\3\2\2\2\u0382\u0380\3\2\2\2\u0383\u0384"+
		"\7\23\2\2\u0384\u00bb\3\2\2\2\u0385\u038a\5Z.\2\u0386\u038a\5\u00a0Q\2"+
		"\u0387\u038a\5\u00a2R\2\u0388\u038a\5\30\r\2\u0389\u0385\3\2\2\2\u0389"+
		"\u0386\3\2\2\2\u0389\u0387\3\2\2\2\u0389\u0388\3\2\2\2\u038a\u00bd\3\2"+
		"\2\2\u038b\u038c\7O\2\2\u038c\u038d\7\30\2\2\u038d\u038e\5P)\2\u038e\u038f"+
		"\7\17\2\2\u038f\u0390\5P)\2\u0390\u0391\7\31\2\2\u0391\u00bf\3\2\2\2\u0392"+
		"\u0393\7O\2\2\u0393\u00c1\3\2\2\2\u0394\u0395\7H\2\2\u0395\u00c3\3\2\2"+
		"\2\u0396\u0397\7\62\2\2\u0397\u039b\7d\2\2\u0398\u0399\7P\2\2\u0399\u039b"+
		"\7d\2\2\u039a\u0396\3\2\2\2\u039a\u0398\3\2\2\2\u039b\u00c5\3\2\2\2\u039c"+
		"\u039d\7\61\2\2\u039d\u039e\5\u00c8e\2\u039e\u039f\7\r\2\2\u039f\u00c7"+
		"\3\2\2\2\u03a0\u03a3\5\30\r\2\u03a1\u03a3\7\13\2\2\u03a2\u03a0\3\2\2\2"+
		"\u03a2\u03a1\3\2\2\2\u03a3\u00c9\3\2\2\2\u03a4\u03a5\7^\2\2\u03a5\u03a6"+
		"\5\30\r\2\u03a6\u03a7\7\13\2\2\u03a7\u00cb\3\2\2\2\u03a8\u03a9\7]\2\2"+
		"\u03a9\u03aa\5\30\r\2\u03aa\u03ab\7\13\2\2\u03ab\u00cd\3\2\2\2\u03ac\u03ad"+
		"\7\64\2\2\u03ad\u03ae\7_\2\2\u03ae\u03af\5\u00bc_\2\u03af\u03b0\5\u00d0"+
		"i\2\u03b0\u00cf\3\2\2\2\u03b1\u03b2\5d\63\2\u03b2\u03b3\5\u00b8]\2\u03b3"+
		"\u03bd\3\2\2\2\u03b4\u03b9\5d\63\2\u03b5\u03b6\7\17\2\2\u03b6\u03b8\5"+
		"d\63\2\u03b7\u03b5\3\2\2\2\u03b8\u03bb\3\2\2\2\u03b9\u03b7\3\2\2\2\u03b9"+
		"\u03ba\3\2\2\2\u03ba\u03bd\3\2\2\2\u03bb\u03b9\3\2\2\2\u03bc\u03b1\3\2"+
		"\2\2\u03bc\u03b4\3\2\2\2\u03bd\u00d1\3\2\2\2\u03be\u03bf\7_\2\2\u03bf"+
		"\u03c0\5\u00bc_\2\u03c0\u03c1\5\u00d4k\2\u03c1\u00d3\3\2\2\2\u03c2\u03c3"+
		"\5d\63\2\u03c3\u03c4\5\u00d6l\2\u03c4\u03ce\3\2\2\2\u03c5\u03ca\5d\63"+
		"\2\u03c6\u03c7\7\17\2\2\u03c7\u03c9\5d\63\2\u03c8\u03c6\3\2\2\2\u03c9"+
		"\u03cc\3\2\2\2\u03ca\u03c8\3\2\2\2\u03ca\u03cb\3\2\2\2\u03cb\u03ce\3\2"+
		"\2\2\u03cc\u03ca\3\2\2\2\u03cd\u03c2\3\2\2\2\u03cd\u03c5\3\2\2\2\u03ce"+
		"\u00d5\3\2\2\2\u03cf\u03d1\5\u00d8m\2\u03d0\u03d2\5\u00dan\2\u03d1\u03d0"+
		"\3\2\2\2\u03d1\u03d2\3\2\2\2\u03d2\u03d5\3\2\2\2\u03d3\u03d5\5\u00dan"+
		"\2\u03d4\u03cf\3\2\2\2\u03d4\u03d3\3\2\2\2\u03d5\u00d7\3\2\2\2\u03d6\u03d7"+
		"\7F\2\2\u03d7\u03d8\5\u00dco\2\u03d8\u00d9\3\2\2\2\u03d9\u03da\7%\2\2"+
		"\u03da\u03db\5\u00dco\2\u03db\u00db\3\2\2\2\u03dc\u03dd\7\22\2\2\u03dd"+
		"\u03e2\5\30\r\2\u03de\u03df\7\17\2\2\u03df\u03e1\5\30\r\2\u03e0\u03de"+
		"\3\2\2\2\u03e1\u03e4\3\2\2\2\u03e2\u03e0\3\2\2\2\u03e2\u03e3\3\2\2\2\u03e3"+
		"\u03e5\3\2\2\2\u03e4\u03e2\3\2\2\2\u03e5\u03e6\7\23\2\2\u03e6\u00dd\3"+
		"\2\2\2\u03e7\u03ea\5\u00e2r\2\u03e8\u03ea\5\u00e0q\2\u03e9\u03e7\3\2\2"+
		"\2\u03e9\u03e8\3\2\2\2\u03ea\u00df\3\2\2\2\u03eb\u03ec\7c\2\2\u03ec\u03ed"+
		"\7d\2\2\u03ed\u00e1\3\2\2\2\u03ee\u03ef\5\u00e4s\2\u03ef\u03f0\7\20\2"+
		"\2\u03f0\u03f1\5\u00eav\2\u03f1\u03f2\7\21\2\2\u03f2\u00e3\3\2\2\2\u03f3"+
		"\u03f4\7c\2\2\u03f4\u03f6\7d\2\2\u03f5\u03f7\5\u00e8u\2\u03f6\u03f5\3"+
		"\2\2\2\u03f6\u03f7\3\2\2\2\u03f7\u03f9\3\2\2\2\u03f8\u03fa\5\u00e6t\2"+
		"\u03f9\u03f8\3\2\2\2\u03f9\u03fa\3\2\2\2\u03fa\u00e5\3\2\2\2\u03fb\u03fc"+
		"\7J\2\2\u03fc\u0401\5\30\r\2\u03fd\u03fe\7\17\2\2\u03fe\u0400\5\30\r\2"+
		"\u03ff\u03fd\3\2\2\2\u0400\u0403\3\2\2\2\u0401\u03ff\3\2\2\2\u0401\u0402"+
		"\3\2\2\2\u0402\u00e7\3\2\2\2\u0403\u0401\3\2\2\2\u0404\u0405\7\16\2\2"+
		"\u0405\u0406\5\30\r\2\u0406\u00e9\3\2\2\2\u0407\u0409\5\u00ecw\2\u0408"+
		"\u0407\3\2\2\2\u0409\u040c\3\2\2\2\u040a\u0408\3\2\2\2\u040a\u040b\3\2"+
		"\2\2\u040b\u00eb\3\2\2\2\u040c\u040a\3\2\2\2\u040d\u040e\5\u00eex\2\u040e"+
		"\u040f\7\r\2\2\u040f\u0420\3\2\2\2\u0410\u0411\5\u00f2z\2\u0411\u0412"+
		"\7\r\2\2\u0412\u0420\3\2\2\2\u0413\u0414\5\u00f4{\2\u0414\u0415\7\r\2"+
		"\2\u0415\u0420\3\2\2\2\u0416\u0417\5\u00f6|\2\u0417\u0418\7\r\2\2\u0418"+
		"\u0420\3\2\2\2\u0419\u041a\5\u00f8}\2\u041a\u041b\7\r\2\2\u041b\u0420"+
		"\3\2\2\2\u041c\u041d\5\u00a8U\2\u041d\u041e\7\r\2\2\u041e\u0420\3\2\2"+
		"\2\u041f\u040d\3\2\2\2\u041f\u0410\3\2\2\2\u041f\u0413\3\2\2\2\u041f\u0416"+
		"\3\2\2\2\u041f\u0419\3\2\2\2\u041f\u041c\3\2\2\2\u0420\u00ed\3\2\2\2\u0421"+
		"\u0422\7Z\2\2\u0422\u0423\5\u00f0y\2\u0423\u0424\7d\2\2\u0424\u00ef\3"+
		"\2\2\2\u0425\u0428\5\30\r\2\u0426\u0428\7L\2\2\u0427\u0425\3\2\2\2\u0427"+
		"\u0426\3\2\2\2\u0428\u00f1\3\2\2\2\u0429\u042b\7,\2\2\u042a\u042c\7W\2"+
		"\2\u042b\u042a\3\2\2\2\u042b\u042c\3\2\2\2\u042c\u042d\3\2\2\2\u042d\u042e"+
		"\5\u00f0y\2\u042e\u042f\7d\2\2\u042f\u00f3\3\2\2\2\u0430\u0431\7\'\2\2"+
		"\u0431\u0432\5\30\r\2\u0432\u0433\7d\2\2\u0433\u00f5\3\2\2\2\u0434\u0435"+
		"\7*\2\2\u0435\u0436\5\30\r\2\u0436\u0437\7d\2\2\u0437\u00f7\3\2\2\2\u0438"+
		"\u0439\7[\2\2\u0439\u043a\5\30\r\2\u043a\u043b\7d\2\2\u043b\u00f9\3\2"+
		"\2\2\u043c\u043d\5\u00fc\177\2\u043d\u043e\5\u0102\u0082\2\u043e\u00fb"+
		"\3\2\2\2\u043f\u0440\7C\2\2\u0440\u0442\7d\2\2\u0441\u0443\5\u00fe\u0080"+
		"\2\u0442\u0441\3\2\2\2\u0442\u0443\3\2\2\2\u0443\u0445\3\2\2\2\u0444\u0446"+
		"\5\u00e6t\2\u0445\u0444\3\2\2\2\u0445\u0446\3\2\2\2\u0446\u0447\3\2\2"+
		"\2\u0447\u0448\7a\2\2\u0448\u044a\5\30\r\2\u0449\u044b\5\u0100\u0081\2"+
		"\u044a\u0449\3\2\2\2\u044a\u044b\3\2\2\2\u044b\u00fd\3\2\2\2\u044c\u044d"+
		"\7\16\2\2\u044d\u044e\5\30\r\2\u044e\u00ff\3\2\2\2\u044f\u0450\7-\2\2"+
		"\u0450\u0451\5\30\r\2\u0451\u0101\3\2\2\2\u0452\u0456\7\20\2\2\u0453\u0455"+
		"\5\u0104\u0083\2\u0454\u0453\3\2\2\2\u0455\u0458\3\2\2\2\u0456\u0454\3"+
		"\2\2\2\u0456\u0457\3\2\2\2\u0457\u0459\3\2\2\2\u0458\u0456\3\2\2\2\u0459"+
		"\u045a\7\21\2\2\u045a\u0103\3\2\2\2\u045b\u0463\5\22\n\2\u045c\u045d\5"+
		"\u0106\u0084\2\u045d\u045e\7\r\2\2\u045e\u0463\3\2\2\2\u045f\u0460\5\u0108"+
		"\u0085\2\u0460\u0461\7\r\2\2\u0461\u0463\3\2\2\2\u0462\u045b\3\2\2\2\u0462"+
		"\u045c\3\2\2\2\u0462\u045f\3\2\2\2\u0463\u0105\3\2\2\2\u0464\u0465\7D"+
		"\2\2\u0465\u0466\7d\2\2\u0466\u0468\7\22\2\2\u0467\u0469\5\60\31\2\u0468"+
		"\u0467\3\2\2\2\u0468\u0469\3\2\2\2\u0469\u046a\3\2\2\2\u046a\u046c\7\23"+
		"\2\2\u046b\u046d\5\u00b8]\2\u046c\u046b\3\2\2\2\u046c\u046d\3\2\2\2\u046d"+
		"\u0107\3\2\2\2\u046e\u046f\7\65\2\2\u046f\u0470\7d\2\2\u0470\u0472\7\22"+
		"\2\2\u0471\u0473\5\60\31\2\u0472\u0471\3\2\2\2\u0472\u0473\3\2\2\2\u0473"+
		"\u0474\3\2\2\2\u0474\u0476\7\23\2\2\u0475\u0477\5\u00b8]\2\u0476\u0475"+
		"\3\2\2\2\u0476\u0477\3\2\2\2\u0477\u0109\3\2\2\2\u0478\u047c\5\u0110\u0089"+
		"\2\u0479\u047c\5\u010e\u0088\2\u047a\u047c\5\u010c\u0087\2\u047b\u0478"+
		"\3\2\2\2\u047b\u0479\3\2\2\2\u047b\u047a\3\2\2\2\u047c\u010b\3\2\2\2\u047d"+
		"\u047f\7X\2\2\u047e\u047d\3\2\2\2\u047e\u047f\3\2\2\2\u047f\u0480\3\2"+
		"\2\2\u0480\u0481\79\2\2\u0481\u0482\7d\2\2\u0482\u010d\3\2\2\2\u0483\u0484"+
		"\7X\2\2\u0484\u0485\79\2\2\u0485\u0486\7d\2\2\u0486\u0487\5&\24\2\u0487"+
		"\u048b\7\20\2\2\u0488\u048a\5\22\n\2\u0489\u0488\3\2\2\2\u048a\u048d\3"+
		"\2\2\2\u048b\u0489\3\2\2\2\u048b\u048c\3\2\2\2\u048c\u048e\3\2\2\2\u048d"+
		"\u048b\3\2\2\2\u048e\u048f\7\21\2\2\u048f\u010f\3\2\2\2\u0490\u0491\5"+
		"\u0112\u008a\2\u0491\u0495\7\20\2\2\u0492\u0494\5*\26\2\u0493\u0492\3"+
		"\2\2\2\u0494\u0497\3\2\2\2\u0495\u0493\3\2\2\2\u0495\u0496\3\2\2\2\u0496"+
		"\u0498\3\2\2\2\u0497\u0495\3\2\2\2\u0498\u0499\7\21\2\2\u0499\u0111\3"+
		"\2\2\2\u049a\u049c\7.\2\2\u049b\u049a\3\2\2\2\u049b\u049c\3\2\2\2\u049c"+
		"\u049d\3\2\2\2\u049d\u049e\79\2\2\u049e\u049f\7d\2\2\u049f\u04a0\5&\24"+
		"\2\u04a0\u0113\3\2\2\2f\u0117\u011d\u0140\u0148\u014e\u0156\u015c\u0161"+
		"\u0166\u017e\u0186\u018c\u0193\u019a\u019d\u01ae\u01b8\u01be\u01c6\u01cd"+
		"\u01d0\u01d8\u01db\u01e2\u01ed\u01f1\u01fa\u0213\u021c\u0224\u022c\u0234"+
		"\u023c\u0244\u024b\u0255\u0263\u026a\u026f\u027a\u0280\u0285\u028c\u0291"+
		"\u029b\u029f\u02a4\u02b0\u02d1\u02e6\u02eb\u02f0\u02fb\u0308\u031b\u0323"+
		"\u032b\u0331\u0339\u0341\u0347\u034d\u0350\u0356\u035e\u0365\u0374\u0380"+
		"\u0389\u039a\u03a2\u03b9\u03bc\u03ca\u03cd\u03d1\u03d4\u03e2\u03e9\u03f6"+
		"\u03f9\u0401\u040a\u041f\u0427\u042b\u0442\u0445\u044a\u0456\u0462\u0468"+
		"\u046c\u0472\u0476\u047b\u047e\u048b\u0495\u049b";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}