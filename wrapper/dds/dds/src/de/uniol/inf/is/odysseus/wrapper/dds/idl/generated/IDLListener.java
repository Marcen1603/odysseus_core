// Generated from IDL.g4 by ANTLR 4.4

package de.uniol.inf.is.odysseus.wrapper.dds.idl.generated;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link IDLParser}.
 */
public interface IDLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link IDLParser#component_body}.
	 * @param ctx the parse tree
	 */
	void enterComponent_body(@NotNull IDLParser.Component_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#component_body}.
	 * @param ctx the parse tree
	 */
	void exitComponent_body(@NotNull IDLParser.Component_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#attr_decl}.
	 * @param ctx the parse tree
	 */
	void enterAttr_decl(@NotNull IDLParser.Attr_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#attr_decl}.
	 * @param ctx the parse tree
	 */
	void exitAttr_decl(@NotNull IDLParser.Attr_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#attr_raises_expr}.
	 * @param ctx the parse tree
	 */
	void enterAttr_raises_expr(@NotNull IDLParser.Attr_raises_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#attr_raises_expr}.
	 * @param ctx the parse tree
	 */
	void exitAttr_raises_expr(@NotNull IDLParser.Attr_raises_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#context_expr}.
	 * @param ctx the parse tree
	 */
	void enterContext_expr(@NotNull IDLParser.Context_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#context_expr}.
	 * @param ctx the parse tree
	 */
	void exitContext_expr(@NotNull IDLParser.Context_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#xor_expr}.
	 * @param ctx the parse tree
	 */
	void enterXor_expr(@NotNull IDLParser.Xor_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#xor_expr}.
	 * @param ctx the parse tree
	 */
	void exitXor_expr(@NotNull IDLParser.Xor_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#component_decl}.
	 * @param ctx the parse tree
	 */
	void enterComponent_decl(@NotNull IDLParser.Component_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#component_decl}.
	 * @param ctx the parse tree
	 */
	void exitComponent_decl(@NotNull IDLParser.Component_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#attr_spec}.
	 * @param ctx the parse tree
	 */
	void enterAttr_spec(@NotNull IDLParser.Attr_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#attr_spec}.
	 * @param ctx the parse tree
	 */
	void exitAttr_spec(@NotNull IDLParser.Attr_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#unsigned_longlong_int}.
	 * @param ctx the parse tree
	 */
	void enterUnsigned_longlong_int(@NotNull IDLParser.Unsigned_longlong_intContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#unsigned_longlong_int}.
	 * @param ctx the parse tree
	 */
	void exitUnsigned_longlong_int(@NotNull IDLParser.Unsigned_longlong_intContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#fixed_array_size}.
	 * @param ctx the parse tree
	 */
	void enterFixed_array_size(@NotNull IDLParser.Fixed_array_sizeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#fixed_array_size}.
	 * @param ctx the parse tree
	 */
	void exitFixed_array_size(@NotNull IDLParser.Fixed_array_sizeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#and_expr}.
	 * @param ctx the parse tree
	 */
	void enterAnd_expr(@NotNull IDLParser.And_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#and_expr}.
	 * @param ctx the parse tree
	 */
	void exitAnd_expr(@NotNull IDLParser.And_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#op_decl}.
	 * @param ctx the parse tree
	 */
	void enterOp_decl(@NotNull IDLParser.Op_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#op_decl}.
	 * @param ctx the parse tree
	 */
	void exitOp_decl(@NotNull IDLParser.Op_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#or_expr}.
	 * @param ctx the parse tree
	 */
	void enterOr_expr(@NotNull IDLParser.Or_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#or_expr}.
	 * @param ctx the parse tree
	 */
	void exitOr_expr(@NotNull IDLParser.Or_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#value_forward_decl}.
	 * @param ctx the parse tree
	 */
	void enterValue_forward_decl(@NotNull IDLParser.Value_forward_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#value_forward_decl}.
	 * @param ctx the parse tree
	 */
	void exitValue_forward_decl(@NotNull IDLParser.Value_forward_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#except_decl}.
	 * @param ctx the parse tree
	 */
	void enterExcept_decl(@NotNull IDLParser.Except_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#except_decl}.
	 * @param ctx the parse tree
	 */
	void exitExcept_decl(@NotNull IDLParser.Except_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#fixed_pt_const_type}.
	 * @param ctx the parse tree
	 */
	void enterFixed_pt_const_type(@NotNull IDLParser.Fixed_pt_const_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#fixed_pt_const_type}.
	 * @param ctx the parse tree
	 */
	void exitFixed_pt_const_type(@NotNull IDLParser.Fixed_pt_const_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#value_element}.
	 * @param ctx the parse tree
	 */
	void enterValue_element(@NotNull IDLParser.Value_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#value_element}.
	 * @param ctx the parse tree
	 */
	void exitValue_element(@NotNull IDLParser.Value_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#interface_header}.
	 * @param ctx the parse tree
	 */
	void enterInterface_header(@NotNull IDLParser.Interface_headerContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#interface_header}.
	 * @param ctx the parse tree
	 */
	void exitInterface_header(@NotNull IDLParser.Interface_headerContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#unsigned_long_int}.
	 * @param ctx the parse tree
	 */
	void enterUnsigned_long_int(@NotNull IDLParser.Unsigned_long_intContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#unsigned_long_int}.
	 * @param ctx the parse tree
	 */
	void exitUnsigned_long_int(@NotNull IDLParser.Unsigned_long_intContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#provides_decl}.
	 * @param ctx the parse tree
	 */
	void enterProvides_decl(@NotNull IDLParser.Provides_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#provides_decl}.
	 * @param ctx the parse tree
	 */
	void exitProvides_decl(@NotNull IDLParser.Provides_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(@NotNull IDLParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(@NotNull IDLParser.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#param_type_spec}.
	 * @param ctx the parse tree
	 */
	void enterParam_type_spec(@NotNull IDLParser.Param_type_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#param_type_spec}.
	 * @param ctx the parse tree
	 */
	void exitParam_type_spec(@NotNull IDLParser.Param_type_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#signed_long_int}.
	 * @param ctx the parse tree
	 */
	void enterSigned_long_int(@NotNull IDLParser.Signed_long_intContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#signed_long_int}.
	 * @param ctx the parse tree
	 */
	void exitSigned_long_int(@NotNull IDLParser.Signed_long_intContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#mult_expr}.
	 * @param ctx the parse tree
	 */
	void enterMult_expr(@NotNull IDLParser.Mult_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#mult_expr}.
	 * @param ctx the parse tree
	 */
	void exitMult_expr(@NotNull IDLParser.Mult_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#switch_body}.
	 * @param ctx the parse tree
	 */
	void enterSwitch_body(@NotNull IDLParser.Switch_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#switch_body}.
	 * @param ctx the parse tree
	 */
	void exitSwitch_body(@NotNull IDLParser.Switch_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#type_declarator}.
	 * @param ctx the parse tree
	 */
	void enterType_declarator(@NotNull IDLParser.Type_declaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#type_declarator}.
	 * @param ctx the parse tree
	 */
	void exitType_declarator(@NotNull IDLParser.Type_declaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#const_type}.
	 * @param ctx the parse tree
	 */
	void enterConst_type(@NotNull IDLParser.Const_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#const_type}.
	 * @param ctx the parse tree
	 */
	void exitConst_type(@NotNull IDLParser.Const_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#home_decl}.
	 * @param ctx the parse tree
	 */
	void enterHome_decl(@NotNull IDLParser.Home_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#home_decl}.
	 * @param ctx the parse tree
	 */
	void exitHome_decl(@NotNull IDLParser.Home_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#type_prefix_decl}.
	 * @param ctx the parse tree
	 */
	void enterType_prefix_decl(@NotNull IDLParser.Type_prefix_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#type_prefix_decl}.
	 * @param ctx the parse tree
	 */
	void exitType_prefix_decl(@NotNull IDLParser.Type_prefix_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#octet_type}.
	 * @param ctx the parse tree
	 */
	void enterOctet_type(@NotNull IDLParser.Octet_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#octet_type}.
	 * @param ctx the parse tree
	 */
	void exitOctet_type(@NotNull IDLParser.Octet_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#wide_string_type}.
	 * @param ctx the parse tree
	 */
	void enterWide_string_type(@NotNull IDLParser.Wide_string_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#wide_string_type}.
	 * @param ctx the parse tree
	 */
	void exitWide_string_type(@NotNull IDLParser.Wide_string_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#home_body}.
	 * @param ctx the parse tree
	 */
	void enterHome_body(@NotNull IDLParser.Home_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#home_body}.
	 * @param ctx the parse tree
	 */
	void exitHome_body(@NotNull IDLParser.Home_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#value_box_decl}.
	 * @param ctx the parse tree
	 */
	void enterValue_box_decl(@NotNull IDLParser.Value_box_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#value_box_decl}.
	 * @param ctx the parse tree
	 */
	void exitValue_box_decl(@NotNull IDLParser.Value_box_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#finder_decl}.
	 * @param ctx the parse tree
	 */
	void enterFinder_decl(@NotNull IDLParser.Finder_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#finder_decl}.
	 * @param ctx the parse tree
	 */
	void exitFinder_decl(@NotNull IDLParser.Finder_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#event_forward_decl}.
	 * @param ctx the parse tree
	 */
	void enterEvent_forward_decl(@NotNull IDLParser.Event_forward_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#event_forward_decl}.
	 * @param ctx the parse tree
	 */
	void exitEvent_forward_decl(@NotNull IDLParser.Event_forward_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#value_base_type}.
	 * @param ctx the parse tree
	 */
	void enterValue_base_type(@NotNull IDLParser.Value_base_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#value_base_type}.
	 * @param ctx the parse tree
	 */
	void exitValue_base_type(@NotNull IDLParser.Value_base_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#state_member}.
	 * @param ctx the parse tree
	 */
	void enterState_member(@NotNull IDLParser.State_memberContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#state_member}.
	 * @param ctx the parse tree
	 */
	void exitState_member(@NotNull IDLParser.State_memberContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#template_type_spec}.
	 * @param ctx the parse tree
	 */
	void enterTemplate_type_spec(@NotNull IDLParser.Template_type_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#template_type_spec}.
	 * @param ctx the parse tree
	 */
	void exitTemplate_type_spec(@NotNull IDLParser.Template_type_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#interface_inheritance_spec}.
	 * @param ctx the parse tree
	 */
	void enterInterface_inheritance_spec(@NotNull IDLParser.Interface_inheritance_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#interface_inheritance_spec}.
	 * @param ctx the parse tree
	 */
	void exitInterface_inheritance_spec(@NotNull IDLParser.Interface_inheritance_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#home_export}.
	 * @param ctx the parse tree
	 */
	void enterHome_export(@NotNull IDLParser.Home_exportContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#home_export}.
	 * @param ctx the parse tree
	 */
	void exitHome_export(@NotNull IDLParser.Home_exportContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#get_excep_expr}.
	 * @param ctx the parse tree
	 */
	void enterGet_excep_expr(@NotNull IDLParser.Get_excep_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#get_excep_expr}.
	 * @param ctx the parse tree
	 */
	void exitGet_excep_expr(@NotNull IDLParser.Get_excep_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#integer_type}.
	 * @param ctx the parse tree
	 */
	void enterInteger_type(@NotNull IDLParser.Integer_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#integer_type}.
	 * @param ctx the parse tree
	 */
	void exitInteger_type(@NotNull IDLParser.Integer_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#raises_expr}.
	 * @param ctx the parse tree
	 */
	void enterRaises_expr(@NotNull IDLParser.Raises_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#raises_expr}.
	 * @param ctx the parse tree
	 */
	void exitRaises_expr(@NotNull IDLParser.Raises_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(@NotNull IDLParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(@NotNull IDLParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#primary_key_spec}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_key_spec(@NotNull IDLParser.Primary_key_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#primary_key_spec}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_key_spec(@NotNull IDLParser.Primary_key_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#complex_declarator}.
	 * @param ctx the parse tree
	 */
	void enterComplex_declarator(@NotNull IDLParser.Complex_declaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#complex_declarator}.
	 * @param ctx the parse tree
	 */
	void exitComplex_declarator(@NotNull IDLParser.Complex_declaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#string_type}.
	 * @param ctx the parse tree
	 */
	void enterString_type(@NotNull IDLParser.String_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#string_type}.
	 * @param ctx the parse tree
	 */
	void exitString_type(@NotNull IDLParser.String_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#type_spec}.
	 * @param ctx the parse tree
	 */
	void enterType_spec(@NotNull IDLParser.Type_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#type_spec}.
	 * @param ctx the parse tree
	 */
	void exitType_spec(@NotNull IDLParser.Type_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#event_abs_decl}.
	 * @param ctx the parse tree
	 */
	void enterEvent_abs_decl(@NotNull IDLParser.Event_abs_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#event_abs_decl}.
	 * @param ctx the parse tree
	 */
	void exitEvent_abs_decl(@NotNull IDLParser.Event_abs_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#member_list}.
	 * @param ctx the parse tree
	 */
	void enterMember_list(@NotNull IDLParser.Member_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#member_list}.
	 * @param ctx the parse tree
	 */
	void exitMember_list(@NotNull IDLParser.Member_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#member}.
	 * @param ctx the parse tree
	 */
	void enterMember(@NotNull IDLParser.MemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#member}.
	 * @param ctx the parse tree
	 */
	void exitMember(@NotNull IDLParser.MemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#factory_decl}.
	 * @param ctx the parse tree
	 */
	void enterFactory_decl(@NotNull IDLParser.Factory_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#factory_decl}.
	 * @param ctx the parse tree
	 */
	void exitFactory_decl(@NotNull IDLParser.Factory_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#value_header}.
	 * @param ctx the parse tree
	 */
	void enterValue_header(@NotNull IDLParser.Value_headerContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#value_header}.
	 * @param ctx the parse tree
	 */
	void exitValue_header(@NotNull IDLParser.Value_headerContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#event}.
	 * @param ctx the parse tree
	 */
	void enterEvent(@NotNull IDLParser.EventContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#event}.
	 * @param ctx the parse tree
	 */
	void exitEvent(@NotNull IDLParser.EventContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#event_header}.
	 * @param ctx the parse tree
	 */
	void enterEvent_header(@NotNull IDLParser.Event_headerContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#event_header}.
	 * @param ctx the parse tree
	 */
	void exitEvent_header(@NotNull IDLParser.Event_headerContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#value_name}.
	 * @param ctx the parse tree
	 */
	void enterValue_name(@NotNull IDLParser.Value_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#value_name}.
	 * @param ctx the parse tree
	 */
	void exitValue_name(@NotNull IDLParser.Value_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#constr_forward_decl}.
	 * @param ctx the parse tree
	 */
	void enterConstr_forward_decl(@NotNull IDLParser.Constr_forward_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#constr_forward_decl}.
	 * @param ctx the parse tree
	 */
	void exitConstr_forward_decl(@NotNull IDLParser.Constr_forward_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#type_decl}.
	 * @param ctx the parse tree
	 */
	void enterType_decl(@NotNull IDLParser.Type_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#type_decl}.
	 * @param ctx the parse tree
	 */
	void exitType_decl(@NotNull IDLParser.Type_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#unsigned_short_int}.
	 * @param ctx the parse tree
	 */
	void enterUnsigned_short_int(@NotNull IDLParser.Unsigned_short_intContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#unsigned_short_int}.
	 * @param ctx the parse tree
	 */
	void exitUnsigned_short_int(@NotNull IDLParser.Unsigned_short_intContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#consumes_decl}.
	 * @param ctx the parse tree
	 */
	void enterConsumes_decl(@NotNull IDLParser.Consumes_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#consumes_decl}.
	 * @param ctx the parse tree
	 */
	void exitConsumes_decl(@NotNull IDLParser.Consumes_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#simple_declarator}.
	 * @param ctx the parse tree
	 */
	void enterSimple_declarator(@NotNull IDLParser.Simple_declaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#simple_declarator}.
	 * @param ctx the parse tree
	 */
	void exitSimple_declarator(@NotNull IDLParser.Simple_declaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#fixed_pt_type}.
	 * @param ctx the parse tree
	 */
	void enterFixed_pt_type(@NotNull IDLParser.Fixed_pt_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#fixed_pt_type}.
	 * @param ctx the parse tree
	 */
	void exitFixed_pt_type(@NotNull IDLParser.Fixed_pt_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#component}.
	 * @param ctx the parse tree
	 */
	void enterComponent(@NotNull IDLParser.ComponentContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#component}.
	 * @param ctx the parse tree
	 */
	void exitComponent(@NotNull IDLParser.ComponentContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#param_attribute}.
	 * @param ctx the parse tree
	 */
	void enterParam_attribute(@NotNull IDLParser.Param_attributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#param_attribute}.
	 * @param ctx the parse tree
	 */
	void exitParam_attribute(@NotNull IDLParser.Param_attributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#declarators}.
	 * @param ctx the parse tree
	 */
	void enterDeclarators(@NotNull IDLParser.DeclaratorsContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#declarators}.
	 * @param ctx the parse tree
	 */
	void exitDeclarators(@NotNull IDLParser.DeclaratorsContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#readonly_attr_spec}.
	 * @param ctx the parse tree
	 */
	void enterReadonly_attr_spec(@NotNull IDLParser.Readonly_attr_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#readonly_attr_spec}.
	 * @param ctx the parse tree
	 */
	void exitReadonly_attr_spec(@NotNull IDLParser.Readonly_attr_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#base_type_spec}.
	 * @param ctx the parse tree
	 */
	void enterBase_type_spec(@NotNull IDLParser.Base_type_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#base_type_spec}.
	 * @param ctx the parse tree
	 */
	void exitBase_type_spec(@NotNull IDLParser.Base_type_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#value_decl}.
	 * @param ctx the parse tree
	 */
	void enterValue_decl(@NotNull IDLParser.Value_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#value_decl}.
	 * @param ctx the parse tree
	 */
	void exitValue_decl(@NotNull IDLParser.Value_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#home_inheritance_spec}.
	 * @param ctx the parse tree
	 */
	void enterHome_inheritance_spec(@NotNull IDLParser.Home_inheritance_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#home_inheritance_spec}.
	 * @param ctx the parse tree
	 */
	void exitHome_inheritance_spec(@NotNull IDLParser.Home_inheritance_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#add_expr}.
	 * @param ctx the parse tree
	 */
	void enterAdd_expr(@NotNull IDLParser.Add_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#add_expr}.
	 * @param ctx the parse tree
	 */
	void exitAdd_expr(@NotNull IDLParser.Add_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#import_decl}.
	 * @param ctx the parse tree
	 */
	void enterImport_decl(@NotNull IDLParser.Import_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#import_decl}.
	 * @param ctx the parse tree
	 */
	void exitImport_decl(@NotNull IDLParser.Import_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#any_type}.
	 * @param ctx the parse tree
	 */
	void enterAny_type(@NotNull IDLParser.Any_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#any_type}.
	 * @param ctx the parse tree
	 */
	void exitAny_type(@NotNull IDLParser.Any_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#case_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCase_stmt(@NotNull IDLParser.Case_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#case_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCase_stmt(@NotNull IDLParser.Case_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#op_type_spec}.
	 * @param ctx the parse tree
	 */
	void enterOp_type_spec(@NotNull IDLParser.Op_type_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#op_type_spec}.
	 * @param ctx the parse tree
	 */
	void exitOp_type_spec(@NotNull IDLParser.Op_type_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#event_decl}.
	 * @param ctx the parse tree
	 */
	void enterEvent_decl(@NotNull IDLParser.Event_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#event_decl}.
	 * @param ctx the parse tree
	 */
	void exitEvent_decl(@NotNull IDLParser.Event_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#type_id_decl}.
	 * @param ctx the parse tree
	 */
	void enterType_id_decl(@NotNull IDLParser.Type_id_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#type_id_decl}.
	 * @param ctx the parse tree
	 */
	void exitType_id_decl(@NotNull IDLParser.Type_id_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#constr_type_spec}.
	 * @param ctx the parse tree
	 */
	void enterConstr_type_spec(@NotNull IDLParser.Constr_type_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#constr_type_spec}.
	 * @param ctx the parse tree
	 */
	void exitConstr_type_spec(@NotNull IDLParser.Constr_type_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#const_exp}.
	 * @param ctx the parse tree
	 */
	void enterConst_exp(@NotNull IDLParser.Const_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#const_exp}.
	 * @param ctx the parse tree
	 */
	void exitConst_exp(@NotNull IDLParser.Const_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#publishes_decl}.
	 * @param ctx the parse tree
	 */
	void enterPublishes_decl(@NotNull IDLParser.Publishes_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#publishes_decl}.
	 * @param ctx the parse tree
	 */
	void exitPublishes_decl(@NotNull IDLParser.Publishes_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#signed_short_int}.
	 * @param ctx the parse tree
	 */
	void enterSigned_short_int(@NotNull IDLParser.Signed_short_intContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#signed_short_int}.
	 * @param ctx the parse tree
	 */
	void exitSigned_short_int(@NotNull IDLParser.Signed_short_intContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#const_decl}.
	 * @param ctx the parse tree
	 */
	void enterConst_decl(@NotNull IDLParser.Const_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#const_decl}.
	 * @param ctx the parse tree
	 */
	void exitConst_decl(@NotNull IDLParser.Const_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#interface_type}.
	 * @param ctx the parse tree
	 */
	void enterInterface_type(@NotNull IDLParser.Interface_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#interface_type}.
	 * @param ctx the parse tree
	 */
	void exitInterface_type(@NotNull IDLParser.Interface_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#export}.
	 * @param ctx the parse tree
	 */
	void enterExport(@NotNull IDLParser.ExportContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#export}.
	 * @param ctx the parse tree
	 */
	void exitExport(@NotNull IDLParser.ExportContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#primary_expr}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_expr(@NotNull IDLParser.Primary_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#primary_expr}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_expr(@NotNull IDLParser.Primary_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#readonly_attr_declarator}.
	 * @param ctx the parse tree
	 */
	void enterReadonly_attr_declarator(@NotNull IDLParser.Readonly_attr_declaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#readonly_attr_declarator}.
	 * @param ctx the parse tree
	 */
	void exitReadonly_attr_declarator(@NotNull IDLParser.Readonly_attr_declaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#signed_longlong_int}.
	 * @param ctx the parse tree
	 */
	void enterSigned_longlong_int(@NotNull IDLParser.Signed_longlong_intContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#signed_longlong_int}.
	 * @param ctx the parse tree
	 */
	void exitSigned_longlong_int(@NotNull IDLParser.Signed_longlong_intContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#unary_operator}.
	 * @param ctx the parse tree
	 */
	void enterUnary_operator(@NotNull IDLParser.Unary_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#unary_operator}.
	 * @param ctx the parse tree
	 */
	void exitUnary_operator(@NotNull IDLParser.Unary_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#op_attribute}.
	 * @param ctx the parse tree
	 */
	void enterOp_attribute(@NotNull IDLParser.Op_attributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#op_attribute}.
	 * @param ctx the parse tree
	 */
	void exitOp_attribute(@NotNull IDLParser.Op_attributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#init_param_attribute}.
	 * @param ctx the parse tree
	 */
	void enterInit_param_attribute(@NotNull IDLParser.Init_param_attributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#init_param_attribute}.
	 * @param ctx the parse tree
	 */
	void exitInit_param_attribute(@NotNull IDLParser.Init_param_attributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#char_type}.
	 * @param ctx the parse tree
	 */
	void enterChar_type(@NotNull IDLParser.Char_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#char_type}.
	 * @param ctx the parse tree
	 */
	void exitChar_type(@NotNull IDLParser.Char_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#case_label}.
	 * @param ctx the parse tree
	 */
	void enterCase_label(@NotNull IDLParser.Case_labelContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#case_label}.
	 * @param ctx the parse tree
	 */
	void exitCase_label(@NotNull IDLParser.Case_labelContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#component_header}.
	 * @param ctx the parse tree
	 */
	void enterComponent_header(@NotNull IDLParser.Component_headerContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#component_header}.
	 * @param ctx the parse tree
	 */
	void exitComponent_header(@NotNull IDLParser.Component_headerContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#shift_expr}.
	 * @param ctx the parse tree
	 */
	void enterShift_expr(@NotNull IDLParser.Shift_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#shift_expr}.
	 * @param ctx the parse tree
	 */
	void exitShift_expr(@NotNull IDLParser.Shift_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#component_export}.
	 * @param ctx the parse tree
	 */
	void enterComponent_export(@NotNull IDLParser.Component_exportContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#component_export}.
	 * @param ctx the parse tree
	 */
	void exitComponent_export(@NotNull IDLParser.Component_exportContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#unary_expr}.
	 * @param ctx the parse tree
	 */
	void enterUnary_expr(@NotNull IDLParser.Unary_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#unary_expr}.
	 * @param ctx the parse tree
	 */
	void exitUnary_expr(@NotNull IDLParser.Unary_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#home_header}.
	 * @param ctx the parse tree
	 */
	void enterHome_header(@NotNull IDLParser.Home_headerContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#home_header}.
	 * @param ctx the parse tree
	 */
	void exitHome_header(@NotNull IDLParser.Home_headerContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#struct_type}.
	 * @param ctx the parse tree
	 */
	void enterStruct_type(@NotNull IDLParser.Struct_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#struct_type}.
	 * @param ctx the parse tree
	 */
	void exitStruct_type(@NotNull IDLParser.Struct_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#value_abs_decl}.
	 * @param ctx the parse tree
	 */
	void enterValue_abs_decl(@NotNull IDLParser.Value_abs_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#value_abs_decl}.
	 * @param ctx the parse tree
	 */
	void exitValue_abs_decl(@NotNull IDLParser.Value_abs_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#unsigned_int}.
	 * @param ctx the parse tree
	 */
	void enterUnsigned_int(@NotNull IDLParser.Unsigned_intContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#unsigned_int}.
	 * @param ctx the parse tree
	 */
	void exitUnsigned_int(@NotNull IDLParser.Unsigned_intContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#forward_decl}.
	 * @param ctx the parse tree
	 */
	void enterForward_decl(@NotNull IDLParser.Forward_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#forward_decl}.
	 * @param ctx the parse tree
	 */
	void exitForward_decl(@NotNull IDLParser.Forward_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#exception_list}.
	 * @param ctx the parse tree
	 */
	void enterException_list(@NotNull IDLParser.Exception_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#exception_list}.
	 * @param ctx the parse tree
	 */
	void exitException_list(@NotNull IDLParser.Exception_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#parameter_decls}.
	 * @param ctx the parse tree
	 */
	void enterParameter_decls(@NotNull IDLParser.Parameter_declsContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#parameter_decls}.
	 * @param ctx the parse tree
	 */
	void exitParameter_decls(@NotNull IDLParser.Parameter_declsContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#set_excep_expr}.
	 * @param ctx the parse tree
	 */
	void enterSet_excep_expr(@NotNull IDLParser.Set_excep_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#set_excep_expr}.
	 * @param ctx the parse tree
	 */
	void exitSet_excep_expr(@NotNull IDLParser.Set_excep_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#interface_name}.
	 * @param ctx the parse tree
	 */
	void enterInterface_name(@NotNull IDLParser.Interface_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#interface_name}.
	 * @param ctx the parse tree
	 */
	void exitInterface_name(@NotNull IDLParser.Interface_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#init_param_decls}.
	 * @param ctx the parse tree
	 */
	void enterInit_param_decls(@NotNull IDLParser.Init_param_declsContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#init_param_decls}.
	 * @param ctx the parse tree
	 */
	void exitInit_param_decls(@NotNull IDLParser.Init_param_declsContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#init_param_decl}.
	 * @param ctx the parse tree
	 */
	void enterInit_param_decl(@NotNull IDLParser.Init_param_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#init_param_decl}.
	 * @param ctx the parse tree
	 */
	void exitInit_param_decl(@NotNull IDLParser.Init_param_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#wide_char_type}.
	 * @param ctx the parse tree
	 */
	void enterWide_char_type(@NotNull IDLParser.Wide_char_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#wide_char_type}.
	 * @param ctx the parse tree
	 */
	void exitWide_char_type(@NotNull IDLParser.Wide_char_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#supported_interface_spec}.
	 * @param ctx the parse tree
	 */
	void enterSupported_interface_spec(@NotNull IDLParser.Supported_interface_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#supported_interface_spec}.
	 * @param ctx the parse tree
	 */
	void exitSupported_interface_spec(@NotNull IDLParser.Supported_interface_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#element_spec}.
	 * @param ctx the parse tree
	 */
	void enterElement_spec(@NotNull IDLParser.Element_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#element_spec}.
	 * @param ctx the parse tree
	 */
	void exitElement_spec(@NotNull IDLParser.Element_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#positive_int_const}.
	 * @param ctx the parse tree
	 */
	void enterPositive_int_const(@NotNull IDLParser.Positive_int_constContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#positive_int_const}.
	 * @param ctx the parse tree
	 */
	void exitPositive_int_const(@NotNull IDLParser.Positive_int_constContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#component_inheritance_spec}.
	 * @param ctx the parse tree
	 */
	void enterComponent_inheritance_spec(@NotNull IDLParser.Component_inheritance_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#component_inheritance_spec}.
	 * @param ctx the parse tree
	 */
	void exitComponent_inheritance_spec(@NotNull IDLParser.Component_inheritance_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#array_declarator}.
	 * @param ctx the parse tree
	 */
	void enterArray_declarator(@NotNull IDLParser.Array_declaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#array_declarator}.
	 * @param ctx the parse tree
	 */
	void exitArray_declarator(@NotNull IDLParser.Array_declaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#signed_int}.
	 * @param ctx the parse tree
	 */
	void enterSigned_int(@NotNull IDLParser.Signed_intContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#signed_int}.
	 * @param ctx the parse tree
	 */
	void exitSigned_int(@NotNull IDLParser.Signed_intContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#floating_pt_type}.
	 * @param ctx the parse tree
	 */
	void enterFloating_pt_type(@NotNull IDLParser.Floating_pt_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#floating_pt_type}.
	 * @param ctx the parse tree
	 */
	void exitFloating_pt_type(@NotNull IDLParser.Floating_pt_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#enum_type}.
	 * @param ctx the parse tree
	 */
	void enterEnum_type(@NotNull IDLParser.Enum_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#enum_type}.
	 * @param ctx the parse tree
	 */
	void exitEnum_type(@NotNull IDLParser.Enum_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#uses_decl}.
	 * @param ctx the parse tree
	 */
	void enterUses_decl(@NotNull IDLParser.Uses_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#uses_decl}.
	 * @param ctx the parse tree
	 */
	void exitUses_decl(@NotNull IDLParser.Uses_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterDefinition(@NotNull IDLParser.DefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitDefinition(@NotNull IDLParser.DefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#enumerator}.
	 * @param ctx the parse tree
	 */
	void enterEnumerator(@NotNull IDLParser.EnumeratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#enumerator}.
	 * @param ctx the parse tree
	 */
	void exitEnumerator(@NotNull IDLParser.EnumeratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(@NotNull IDLParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(@NotNull IDLParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#scoped_name}.
	 * @param ctx the parse tree
	 */
	void enterScoped_name(@NotNull IDLParser.Scoped_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#scoped_name}.
	 * @param ctx the parse tree
	 */
	void exitScoped_name(@NotNull IDLParser.Scoped_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#interface_decl}.
	 * @param ctx the parse tree
	 */
	void enterInterface_decl(@NotNull IDLParser.Interface_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#interface_decl}.
	 * @param ctx the parse tree
	 */
	void exitInterface_decl(@NotNull IDLParser.Interface_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#attr_declarator}.
	 * @param ctx the parse tree
	 */
	void enterAttr_declarator(@NotNull IDLParser.Attr_declaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#attr_declarator}.
	 * @param ctx the parse tree
	 */
	void exitAttr_declarator(@NotNull IDLParser.Attr_declaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#init_decl}.
	 * @param ctx the parse tree
	 */
	void enterInit_decl(@NotNull IDLParser.Init_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#init_decl}.
	 * @param ctx the parse tree
	 */
	void exitInit_decl(@NotNull IDLParser.Init_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#object_type}.
	 * @param ctx the parse tree
	 */
	void enterObject_type(@NotNull IDLParser.Object_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#object_type}.
	 * @param ctx the parse tree
	 */
	void exitObject_type(@NotNull IDLParser.Object_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#imported_scope}.
	 * @param ctx the parse tree
	 */
	void enterImported_scope(@NotNull IDLParser.Imported_scopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#imported_scope}.
	 * @param ctx the parse tree
	 */
	void exitImported_scope(@NotNull IDLParser.Imported_scopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#union_type}.
	 * @param ctx the parse tree
	 */
	void enterUnion_type(@NotNull IDLParser.Union_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#union_type}.
	 * @param ctx the parse tree
	 */
	void exitUnion_type(@NotNull IDLParser.Union_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#param_decl}.
	 * @param ctx the parse tree
	 */
	void enterParam_decl(@NotNull IDLParser.Param_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#param_decl}.
	 * @param ctx the parse tree
	 */
	void exitParam_decl(@NotNull IDLParser.Param_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#specification}.
	 * @param ctx the parse tree
	 */
	void enterSpecification(@NotNull IDLParser.SpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#specification}.
	 * @param ctx the parse tree
	 */
	void exitSpecification(@NotNull IDLParser.SpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#boolean_type}.
	 * @param ctx the parse tree
	 */
	void enterBoolean_type(@NotNull IDLParser.Boolean_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#boolean_type}.
	 * @param ctx the parse tree
	 */
	void exitBoolean_type(@NotNull IDLParser.Boolean_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#interface_or_forward_decl}.
	 * @param ctx the parse tree
	 */
	void enterInterface_or_forward_decl(@NotNull IDLParser.Interface_or_forward_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#interface_or_forward_decl}.
	 * @param ctx the parse tree
	 */
	void exitInterface_or_forward_decl(@NotNull IDLParser.Interface_or_forward_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#component_forward_decl}.
	 * @param ctx the parse tree
	 */
	void enterComponent_forward_decl(@NotNull IDLParser.Component_forward_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#component_forward_decl}.
	 * @param ctx the parse tree
	 */
	void exitComponent_forward_decl(@NotNull IDLParser.Component_forward_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#declarator}.
	 * @param ctx the parse tree
	 */
	void enterDeclarator(@NotNull IDLParser.DeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#declarator}.
	 * @param ctx the parse tree
	 */
	void exitDeclarator(@NotNull IDLParser.DeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#value_inheritance_spec}.
	 * @param ctx the parse tree
	 */
	void enterValue_inheritance_spec(@NotNull IDLParser.Value_inheritance_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#value_inheritance_spec}.
	 * @param ctx the parse tree
	 */
	void exitValue_inheritance_spec(@NotNull IDLParser.Value_inheritance_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#sequence_type}.
	 * @param ctx the parse tree
	 */
	void enterSequence_type(@NotNull IDLParser.Sequence_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#sequence_type}.
	 * @param ctx the parse tree
	 */
	void exitSequence_type(@NotNull IDLParser.Sequence_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#switch_type_spec}.
	 * @param ctx the parse tree
	 */
	void enterSwitch_type_spec(@NotNull IDLParser.Switch_type_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#switch_type_spec}.
	 * @param ctx the parse tree
	 */
	void exitSwitch_type_spec(@NotNull IDLParser.Switch_type_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#interface_body}.
	 * @param ctx the parse tree
	 */
	void enterInterface_body(@NotNull IDLParser.Interface_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#interface_body}.
	 * @param ctx the parse tree
	 */
	void exitInterface_body(@NotNull IDLParser.Interface_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#simple_type_spec}.
	 * @param ctx the parse tree
	 */
	void enterSimple_type_spec(@NotNull IDLParser.Simple_type_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#simple_type_spec}.
	 * @param ctx the parse tree
	 */
	void exitSimple_type_spec(@NotNull IDLParser.Simple_type_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link IDLParser#emits_decl}.
	 * @param ctx the parse tree
	 */
	void enterEmits_decl(@NotNull IDLParser.Emits_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link IDLParser#emits_decl}.
	 * @param ctx the parse tree
	 */
	void exitEmits_decl(@NotNull IDLParser.Emits_declContext ctx);
}