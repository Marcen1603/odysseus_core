package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInstanceOfExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionBoolean;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionDouble;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionNull;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionRange;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionString;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression;
import de.uniol.inf.is.odysseus.iql.basic.types.IQLUtils
import de.uniol.inf.is.odysseus.iql.basic.types.Range
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList
import org.eclipse.xtext.common.types.JvmTypeReference
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionInt
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.emf.common.util.EList
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils
import org.eclipse.xtext.common.types.JvmField
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLParenthesisExpression
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSuperExpression
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLThisExpression
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJvmElementCallExpression
import org.eclipse.xtext.common.types.JvmOperation
import org.eclipse.xtext.common.types.JvmDeclaredType
import java.util.ArrayList
import java.util.List
import de.uniol.inf.is.odysseus.iql.basic.typing.^extension.IQLOperatorOverloadingUtils
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.IIQLExpressionEvaluator
import de.uniol.inf.is.odysseus.iql.basic.typing.^extension.IIQLTypeExtensionsDictionary
import de.uniol.inf.is.odysseus.iql.basic.types.^extension.ListExtensions
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionType

abstract class AbstractIQLExpressionCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>, E extends IIQLExpressionEvaluator, U extends IIQLTypeUtils, L extends IIQLLookUp, O extends IIQLTypeExtensionsDictionary, D extends IIQLTypeDictionary> implements IIQLExpressionCompiler<G>{
	
	protected H helper;
	
	protected T typeCompiler;
	
	protected E exprEvaluator;
	
	protected U typeUtils;
	
	protected L lookUp;
	
	protected O typeExtensionsDictionary;

	protected D typeDictionary;
	
	new (H helper, T typeCompiler, E exprEvaluator, U typeUtils, L lookUp, O typeExtensionsDictionary, D typeDictionary) {
		this.helper = helper;
		this.typeCompiler = typeCompiler;
		this.exprEvaluator = exprEvaluator;
		this.typeUtils = typeUtils;
		this.lookUp = lookUp;
		this.typeExtensionsDictionary = typeExtensionsDictionary;
		this.typeDictionary = typeDictionary;
	}
	
	override compile(IQLExpression expr, G context) {
		if (expr instanceof IQLAssignmentExpression) {
			return compile(expr as IQLAssignmentExpression, context);
		} else if (expr instanceof IQLLogicalOrExpression) {
			return compile(expr as IQLLogicalOrExpression, context);
		} else if (expr instanceof IQLLogicalAndExpression) {
			return compile(expr as IQLLogicalAndExpression, context);
		} else if (expr instanceof IQLEqualityExpression) {
			return compile(expr as IQLEqualityExpression, context);
		} else if (expr instanceof IQLRelationalExpression) {
			return compile(expr as IQLRelationalExpression, context);
		} else if (expr instanceof IQLInstanceOfExpression) {
			return compile(expr as IQLInstanceOfExpression, context);
		} else if (expr instanceof IQLAdditiveExpression) {
			return compile(expr as IQLAdditiveExpression, context);
		} else if (expr instanceof IQLMultiplicativeExpression) {
			return compile(expr as IQLMultiplicativeExpression, context);
		} else if (expr instanceof IQLPlusMinusExpression) {
			return compile(expr as IQLPlusMinusExpression, context);
		} else if (expr instanceof IQLBooleanNotExpression) {
			return compile(expr as IQLBooleanNotExpression, context);
		} else if (expr instanceof IQLPrefixExpression) {
			return compile(expr as IQLPrefixExpression, context);
		} else if (expr instanceof IQLTypeCastExpression) {
			return compile(expr as IQLTypeCastExpression, context);
		} else if (expr instanceof IQLPostfixExpression) {
			return compile(expr as IQLPostfixExpression, context);
		} else if (expr instanceof IQLArrayExpression) {
			return compile(expr as IQLArrayExpression, context);
		} else if (expr instanceof IQLMemberSelectionExpression) {
			return compile(expr as IQLMemberSelectionExpression, context);
		} else if (expr instanceof IQLJvmElementCallExpression) {
			return compile(expr as IQLJvmElementCallExpression, context);
		} else if (expr instanceof IQLThisExpression) {
			return compile(expr as IQLThisExpression, context);
		} else if (expr instanceof IQLSuperExpression) {
			return compile(expr as IQLSuperExpression, context);
		} else if (expr instanceof IQLParenthesisExpression) {
			return compile(expr as IQLParenthesisExpression, context);
		} else if (expr instanceof IQLNewExpression) {
			return compile(expr as IQLNewExpression, context);
		} else if (expr instanceof IQLLiteralExpressionInt) {
			return compile(expr as IQLLiteralExpressionInt, context);
		} else if (expr instanceof IQLLiteralExpressionDouble) {
			return compile(expr as IQLLiteralExpressionDouble, context);
		} else if (expr instanceof IQLLiteralExpressionString) {
			return compile(expr as IQLLiteralExpressionString, context);
		} else if (expr instanceof IQLLiteralExpressionBoolean) {
			return compile(expr as IQLLiteralExpressionBoolean, context);
		} else if (expr instanceof IQLLiteralExpressionRange) {
			return compile(expr as IQLLiteralExpressionRange, context);
		} else if (expr instanceof IQLLiteralExpressionNull) {
			return compile(expr as IQLLiteralExpressionNull, context);
		} else if (expr instanceof IQLLiteralExpressionList) {
			return compile(expr as IQLLiteralExpressionList, context);
		} else if (expr instanceof IQLLiteralExpressionMap) {
			return compile(expr as IQLLiteralExpressionMap, context);
		} else if (expr instanceof IQLLiteralExpressionType) {
			return compile(expr as IQLLiteralExpressionType, context);
		}
		return null;
	}
	
	def String compile(IQLAssignmentExpression e, G c) {
		if (e.leftOperand instanceof IQLMemberSelectionExpression) {
			compileAssignmentExpr(e, e.leftOperand as IQLMemberSelectionExpression, c);			
		} else if (e.leftOperand instanceof IQLJvmElementCallExpression) {
			compileAssignmentExpr(e, e.leftOperand as IQLJvmElementCallExpression, c);			
		} else if (e.leftOperand instanceof IQLArrayExpression) {
			compileAssignmentExpr(e, e.leftOperand as IQLArrayExpression, c);			
		} else {
			compileAssignmentExpr(e, c);
		}

	}
	
	
	
	def String compileAssignmentExpr(IQLAssignmentExpression e, IQLJvmElementCallExpression elementCallExpr, G c) {
		if (e.op.equals("=") && elementCallExpr.element instanceof JvmOperation) {			
			var leftType = (elementCallExpr.element as JvmOperation).parameters.get(0).parameterType
			var rightType = exprEvaluator.eval(e.rightOperand)
			c.expectedTypeRef = leftType

			var result = "";
			var op = elementCallExpr.element as JvmOperation
			c.addExceptions(op.exceptions)
			if (helper.isJvmArray(leftType) && !rightType.^null && !helper.isJvmArray(rightType.ref)){
				c.addImport(IQLUtils.canonicalName)
				if (helper.isPrimitiveArray(leftType)) {
					result = '''«op.simpleName»(«IQLUtils.simpleName».«helper.getArrayMethodName(leftType)»(«compile(e.rightOperand, c)»))'''
				} else {
					var clazz = typeCompiler.compile(typeUtils.createTypeRef(typeUtils.getInnerType(leftType, false)), c, true);
					result = '''«op.simpleName»(«IQLUtils.simpleName».«helper.getArrayMethodName(leftType)»(«clazz».class, «compile(e.rightOperand, c)»))'''
				}
			} else if (rightType.^null || lookUp.isAssignable(leftType, rightType.ref)){
				result = '''«op.simpleName»(«compile(e.rightOperand, c)»)'''
			} else if (rightType.^null || lookUp.isCastable(leftType, rightType.ref)){
				var target = typeCompiler.compile(leftType, c, false)
				result = '''«op.simpleName»((«target»)«compile(e.rightOperand, c)»)'''
			} else {
				result = '''«op.simpleName»(«compile(e.rightOperand, c)»)'''
			}
			c.expectedTypeRef = null
			return result;
		} else {
			compileAssignmentExpr(e,c);
		}
	}
	
	def String compileAssignmentExpr(IQLAssignmentExpression e, IQLMemberSelectionExpression selExpr, G c) {
		if (e.op.equals("=") && selExpr.sel.member instanceof JvmOperation) {
			var leftType = (selExpr.sel.member as JvmOperation).parameters.get(0).parameterType
			var rightType = exprEvaluator.eval(e.rightOperand)
			c.expectedTypeRef = leftType

			var result = "";
			var op = selExpr.sel.member as JvmOperation
			c.addExceptions(op.exceptions)
			if (helper.isJvmArray(leftType) && !rightType.^null && !helper.isJvmArray(rightType.ref)){
				c.addImport(IQLUtils.canonicalName)
				if (helper.hasSystemTypeCompiler(op) &&  helper.getSystemTypeCompiler(op).compileMethodSelectionManually) {
					var systemTypeCompiler = helper.getSystemTypeCompiler(op);
					if (helper.isPrimitiveArray(leftType)) {
						result = '''«compile(selExpr.leftOperand, c)».«systemTypeCompiler.compileMethodSelection(op, helper.toList(IQLUtils.simpleName+"."+helper.getArrayMethodName(leftType)+"("+compile(e.rightOperand, c)+")"))»'''				
					} else {
						var clazz = typeCompiler.compile(typeUtils.createTypeRef(typeUtils.getInnerType(leftType, false)), c, true);
						result = '''«compile(selExpr.leftOperand, c)».«systemTypeCompiler.compileMethodSelection(op, helper.toList(IQLUtils.simpleName+"."+helper.getArrayMethodName(leftType)+"("+clazz+".class, "+compile(e.rightOperand, c)+")"))»'''				
					}
				} else {
					if (helper.isPrimitiveArray(leftType)) {
						result = '''«compile(selExpr.leftOperand, c)».«op.simpleName»(«IQLUtils.simpleName».«helper.getArrayMethodName(leftType)»(«compile(e.rightOperand, c)»))'''			
					} else {
						var clazz = typeCompiler.compile(typeUtils.createTypeRef(typeUtils.getInnerType(leftType, false)), c, true);
						result = '''«compile(selExpr.leftOperand, c)».«op.simpleName»(«IQLUtils.simpleName».«helper.getArrayMethodName(leftType)»(«clazz».class, «compile(e.rightOperand, c)»))'''				
					}									
				}
			} else if (rightType.^null || lookUp.isAssignable(leftType, rightType.ref)){
				if (helper.hasSystemTypeCompiler(op) &&  helper.getSystemTypeCompiler(op).compileMethodSelectionManually) {
					var systemTypeCompiler = helper.getSystemTypeCompiler(op);
					result = '''«compile(selExpr.leftOperand, c)».«systemTypeCompiler.compileMethodSelection(op, helper.toList(compile(e.rightOperand, c)))»'''
				} else {
					result = '''«compile(selExpr.leftOperand, c)».«op.simpleName»(«compile(e.rightOperand, c)»)'''
				}
			} else if (rightType.^null || lookUp.isCastable(leftType, rightType.ref)){
				var target = typeCompiler.compile(leftType, c, false)
				if (helper.hasSystemTypeCompiler(op) &&  helper.getSystemTypeCompiler(op).compileMethodSelectionManually) {
					var systemTypeCompiler = helper.getSystemTypeCompiler(op);
					result = '''«compile(selExpr.leftOperand, c)».«systemTypeCompiler.compileMethodSelection(op, helper.toList("("+target+")"+compile(e.rightOperand, c)))»'''
				} else {
					result = '''«compile(selExpr.leftOperand, c)».«op.simpleName»((«target»)«compile(e.rightOperand, c)»)'''
				}
			} else {
				if (helper.hasSystemTypeCompiler(op) &&  helper.getSystemTypeCompiler(op).compileMethodSelectionManually) {
					var systemTypeCompiler = helper.getSystemTypeCompiler(op);
					result = '''«compile(selExpr.leftOperand, c)».«systemTypeCompiler.compileMethodSelection(op, helper.toList(compile(e.rightOperand, c)))»'''
				} else {
					result = '''«compile(selExpr.leftOperand, c)».«op.simpleName»(«compile(e.rightOperand, c)»)'''
				}
			}
			c.expectedTypeRef = null
			return result;
		} else {
			compileAssignmentExpr(e, c);
		}
	}	
	
	def String compileAssignmentExpr(IQLAssignmentExpression e, IQLArrayExpression arrayExpr,  G c) {
		var arrayType = exprEvaluator.eval(arrayExpr.leftOperand)
		var methodName = IQLOperatorOverloadingUtils.SET;			
		
		if (e.op.equals("=") && !arrayType.^null && typeExtensionsDictionary.hasTypeExtensions(arrayType.ref,methodName,helper.createSetterArguments(e.rightOperand, arrayExpr.expressions))){
			var leftType = exprEvaluator.eval(arrayExpr)
			var rightType = exprEvaluator.eval(e.rightOperand)
			if (!leftType.^null) {
				c.expectedTypeRef = leftType.ref
			}
			var result = "";		
			var typeOps = typeExtensionsDictionary.getTypeExtensions(arrayType.ref,methodName,helper.createSetterArguments(e.rightOperand, arrayExpr.expressions));
			c.addImport(typeOps.class.canonicalName)
			if (!leftType.^null && helper.isJvmArray(leftType.ref) && !rightType.^null && !helper.isJvmArray(rightType.ref)){
				c.addImport(IQLUtils.canonicalName)
				if (helper.isPrimitiveArray(leftType.ref)) {
					result = '''«typeOps.class.simpleName».«methodName»(«compile(arrayExpr.leftOperand, c)», «IQLUtils.simpleName».«helper.getArrayMethodName(leftType.ref)»(«compile(e.rightOperand, c)»), «arrayExpr.expressions.map[ el | compile(el, c)].join(", ")»)'''
				} else {
					var clazz = typeCompiler.compile(typeUtils.createTypeRef(typeUtils.getInnerType(leftType.ref, false)), c, true);
					result = '''«typeOps.class.simpleName».«methodName»(«compile(arrayExpr.leftOperand, c)», «IQLUtils.simpleName».«helper.getArrayMethodName(leftType.ref)»(«clazz».class, «compile(e.rightOperand, c)»), «arrayExpr.expressions.map[ el | compile(el, c)].join(", ")»)'''
				}								
			} else if (leftType.^null || rightType.^null || lookUp.isAssignable(leftType.ref, rightType.ref)){
				if (arrayExpr.expressions.size == 1) {
					result = '''«typeOps.class.simpleName».«methodName»(«compile(arrayExpr.leftOperand, c)», «compile(e.rightOperand, c)», «compile(arrayExpr.expressions.get(0), c)»)'''
				} else {
					result = '''«typeOps.class.simpleName».«methodName»(«compile(arrayExpr.leftOperand, c)», «compile(e.rightOperand, c)», «IQLUtils.simpleName».createList(«arrayExpr.expressions.map[el | compile(el, c)].join(", ")»))'''
				}
			} else if (leftType.^null || rightType.^null || lookUp.isCastable(leftType.ref, rightType.ref)){
				var target = typeCompiler.compile(leftType.ref, c, false)
				if (arrayExpr.expressions.size == 1) {
					result = '''«typeOps.class.simpleName».«methodName»(«compile(arrayExpr.leftOperand, c)», ((«target»)«compile(e.rightOperand, c)»), «compile(arrayExpr.expressions.get(0), c)»)'''
				} else {
					result = '''«typeOps.class.simpleName».«methodName»(«compile(arrayExpr.leftOperand, c)», ((«target»)«compile(e.rightOperand, c)»), «IQLUtils.simpleName».createList(«arrayExpr.expressions.map[el | compile(el, c)].join(", ")»))'''
				}
			} else {
				if (arrayExpr.expressions.size == 1) {
					result = '''«typeOps.class.simpleName».«methodName»(«compile(arrayExpr.leftOperand, c)», «compile(e.rightOperand, c)», «compile(arrayExpr.expressions.get(0), c)»)'''
				} else {
					result = '''«typeOps.class.simpleName».«methodName»(«compile(arrayExpr.leftOperand, c)», «compile(e.rightOperand, c)», «IQLUtils.simpleName».createList(«arrayExpr.expressions.map[el | compile(el, c)].join(", ")»))'''
				}
			}
			c.expectedTypeRef = null
			return result;
		} else if (e.op.equals("=") &&!arrayType.^null && typeUtils.isArray(arrayType.ref)){
			var leftType = exprEvaluator.eval(arrayExpr)
			var rightType = exprEvaluator.eval(e.rightOperand)
			if (!leftType.^null) {
				c.expectedTypeRef = leftType.ref
			}
			var result = "";		
			if (!leftType.^null && helper.isJvmArray(leftType.ref) && !rightType.^null && !helper.isJvmArray(rightType.ref)){
				c.addImport(IQLUtils.canonicalName)
				if (helper.isPrimitiveArray(leftType.ref)) {
					result = '''«ListExtensions.simpleName».«methodName»(«compile(arrayExpr.leftOperand, c)», «IQLUtils.simpleName».«helper.getArrayMethodName(leftType.ref)»(«compile(e.rightOperand, c)»), «arrayExpr.expressions.map[ el | compile(el, c)].join(", ")»)'''
				} else {
					var clazz = typeCompiler.compile(typeUtils.createTypeRef(typeUtils.getInnerType(leftType.ref, false)), c, true);
					result = '''«ListExtensions.simpleName».«methodName»(«compile(arrayExpr.leftOperand, c)», «IQLUtils.simpleName».«helper.getArrayMethodName(leftType.ref)»(«clazz».class, «compile(e.rightOperand, c)»), «arrayExpr.expressions.map[ el | compile(el, c)].join(", ")»)'''
				}				
			} else if (leftType.^null || rightType.^null || lookUp.isAssignable(leftType.ref, rightType.ref)){
				c.addImport(ListExtensions.canonicalName)			
				result = '''«ListExtensions.simpleName».«methodName»(«compile(arrayExpr.leftOperand, c)», «compile(e.rightOperand, c)», «arrayExpr.expressions.map[ el | compile(el, c)].join(", ")»)'''
			} else if (leftType.^null || rightType.^null || lookUp.isCastable(leftType.ref, rightType.ref)){
				c.addImport(ListExtensions.canonicalName)
				var target = typeCompiler.compile(leftType.ref, c, false)
				result = '''«ListExtensions.simpleName».«methodName»(«compile(arrayExpr.leftOperand, c)», ((«target»)«compile(e.rightOperand, c)»), «arrayExpr.expressions.map[ el | compile(el, c)].join(", ")»)'''
			} else {
				c.addImport(ListExtensions.canonicalName)			
				result = '''«ListExtensions.simpleName».«methodName»(«compile(arrayExpr.leftOperand, c)», «compile(e.rightOperand, c)», «arrayExpr.expressions.map[ el | compile(el, c)].join(", ")»)'''
			}
			c.expectedTypeRef = null
			return result;
		} else {
			compileAssignmentExpr(e, c);
		}
	}
	
	def String compileAssignmentExpr(IQLAssignmentExpression e, G c) {
		var leftType = exprEvaluator.eval(e.leftOperand)		
		if (e.op.equals("=")) {
			var rightType = exprEvaluator.eval(e.rightOperand)
			if (!leftType.^null){
				c.expectedTypeRef = leftType.ref
			}
			var result = "";
			if (!leftType.^null && helper.isJvmArray(leftType.ref) && !rightType.^null && !helper.isJvmArray(rightType.ref)){
				c.addImport(IQLUtils.canonicalName)
				if (helper.isPrimitiveArray(leftType.ref)) {
					result = '''«compile(e.leftOperand, c)» «e.op» «IQLUtils.simpleName».«helper.getArrayMethodName(leftType.ref)»(«compile(e.rightOperand, c)»))'''
				} else {
					var clazz = typeCompiler.compile(typeUtils.createTypeRef(typeUtils.getInnerType(leftType.ref, false)), c, true);
					result = '''«compile(e.leftOperand, c)» «e.op» «IQLUtils.simpleName».«helper.getArrayMethodName(leftType.ref)»(«clazz».class, «compile(e.rightOperand, c)»))'''
				}				
			} else if (leftType.^null || rightType.^null || lookUp.isAssignable(leftType.ref, rightType.ref)){
				result = '''«compile(e.leftOperand, c)» «e.op» «compile(e.rightOperand, c)»'''
			} else if (leftType.^null || rightType.^null || lookUp.isCastable(leftType.ref, rightType.ref)){
				var target = typeCompiler.compile(leftType.ref, c, false)
				result = '''«compile(e.leftOperand, c)» «e.op» ((«target») «compile(e.rightOperand, c)»)'''	
			} else {
				result = '''«compile(e.leftOperand, c)» «e.op» «compile(e.rightOperand, c)»'''
			}
			c.expectedTypeRef = null
			return result
		} else if (!leftType.^null && e.op.equals("+=")) {
			return compileOperatorOverloading("+", IQLOperatorOverloadingUtils.PLUS, leftType.ref, e.leftOperand, e.rightOperand, c);
		} else if (!leftType.^null && e.op.equals("-=")) {
			return compileOperatorOverloading("-", IQLOperatorOverloadingUtils.MINUS, leftType.ref, e.leftOperand, e.rightOperand, c);
		} else if (!leftType.^null && e.op.equals("*=")) {
			return compileOperatorOverloading("*", IQLOperatorOverloadingUtils.MULTIPLY, leftType.ref, e.leftOperand, e.rightOperand, c);
		} else if (!leftType.^null && e.op.equals("/=")) {
			return compileOperatorOverloading("/", IQLOperatorOverloadingUtils.DIVIDE, leftType.ref, e.leftOperand, e.rightOperand, c);
		} else if (!leftType.^null && e.op.equals("%=")) {
			return compileOperatorOverloading("%", IQLOperatorOverloadingUtils.MODULO, leftType.ref, e.leftOperand, e.rightOperand, c);
		}else {
			return "";
		}
	}
	
	
	def String compile(IQLLogicalOrExpression e, G c) {
		var left = exprEvaluator.eval(e.leftOperand)		
		var result = "";	
		if (!left.^null && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.LOGICAL_OR,e.rightOperand)){
			result = compileOperatorOverloading(e.op, IQLOperatorOverloadingUtils.LOGICAL_OR, left.ref, e.leftOperand, e.rightOperand, c);
		} else {
			result = '''«compile(e.leftOperand, c)» «e.op» «compile(e.rightOperand, c)»'''
		}			
		return result	
	}
	
	def String compile(IQLLogicalAndExpression e, G c) {
		var left = exprEvaluator.eval(e.leftOperand)		
		var result = "";	
		if (!left.^null && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.LOGICAL_AND,e.rightOperand)){
			result = compileOperatorOverloading(e.op, IQLOperatorOverloadingUtils.LOGICAL_AND, left.ref, e.leftOperand, e.rightOperand, c);
		} else {
			result = '''«compile(e.leftOperand, c)» «e.op» «compile(e.rightOperand, c)»'''
		}			
		return result
	}
	
	def String compile(IQLEqualityExpression e, G c) {
		var left = exprEvaluator.eval(e.leftOperand)		
		var result = "";	
		if (!left.^null && e.op.equals("==") && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.EQUALS,e.rightOperand)){
			result = compileOperatorOverloading("==", IQLOperatorOverloadingUtils.EQUALS, left.ref, e.leftOperand, e.rightOperand, c);
		} else if (!left.^null && e.op.equals("!==") && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.EQUALS_NOT,e.rightOperand)){
			result = compileOperatorOverloading("!==", IQLOperatorOverloadingUtils.EQUALS_NOT, left.ref, e.leftOperand, e.rightOperand, c);
		}else {
			if (!left.^null){
				c.expectedTypeRef = left.ref			
			} 
			result = '''«compile(e.leftOperand, c)» «e.op» «compile(e.rightOperand, c)»'''	
		}			
		c.expectedTypeRef = null
		return result	
	}
	
	def String compile(IQLRelationalExpression e, G c) {
		var left = exprEvaluator.eval(e.leftOperand)		
		var result = "";	
		if (!left.^null && e.op.equals(">") && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.GREATER_THAN,e.rightOperand)){
			result = compileOperatorOverloading(">", IQLOperatorOverloadingUtils.GREATER_THAN, left.ref, e.leftOperand, e.rightOperand, c);
		} else if (!left.^null &&  e.op.equals("<") && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.LESS_THAN,e.rightOperand)){
			result = compileOperatorOverloading("<", IQLOperatorOverloadingUtils.LESS_THAN, left.ref, e.leftOperand, e.rightOperand, c);
		} else if (!left.^null &&  e.op.equals(">=") && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.GREATER_EQUALS_THAN,e.rightOperand)){
			result = compileOperatorOverloading(">=", IQLOperatorOverloadingUtils.GREATER_EQUALS_THAN, left.ref, e.leftOperand, e.rightOperand, c);
		} else if (!left.^null &&  e.op.equals("<=") && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.LESS_EQUALS_THAN,e.rightOperand)){
			result = compileOperatorOverloading("<=", IQLOperatorOverloadingUtils.LESS_EQUALS_THAN, left.ref, e.leftOperand, e.rightOperand, c);
		}else {
			if (!left.^null){
				c.expectedTypeRef = left.ref			
			} 
			result = '''«compile(e.leftOperand, c)» «e.op» «compile(e.rightOperand, c)»'''	
		}			
		c.expectedTypeRef = null
		return result	
	}
	
	def String compile(IQLInstanceOfExpression e, G c) {
		'''«compile(e.leftOperand, c)» instanceof «typeCompiler.compile(e.targetRef, c, true)»'''
	}
	
	def String compileOperatorOverloading(String operator, String operatorName, JvmTypeReference left,IQLExpression leftOperand, IQLExpression rightOperand, G c) {
		var rightType = exprEvaluator.eval(rightOperand)
		var typeOps = typeExtensionsDictionary.getTypeExtensions(left,operatorName,rightOperand);
		c.addImport(typeOps.class.canonicalName)
		var targetType = typeExtensionsDictionary.getMethod(left,operatorName,rightOperand).parameters.get(0).parameterType;
		c.expectedTypeRef = targetType
		var result = "";
		if (targetType !== null && helper.isJvmArray(targetType) && !rightType.^null && !helper.isJvmArray(rightType.ref)){
			c.addImport(IQLUtils.canonicalName)
			if (helper.isPrimitiveArray(targetType)) {
				result = '''«typeOps.class.simpleName».«operatorName»(«compile(leftOperand, c)», «IQLUtils.simpleName».«helper.getArrayMethodName(targetType)»(«compile(rightOperand, c)»))'''
			} else {
				var clazz = typeCompiler.compile(typeUtils.createTypeRef(typeUtils.getInnerType(targetType, false)), c, true);
				result = '''«typeOps.class.simpleName».«operatorName»(«compile(leftOperand, c)», «IQLUtils.simpleName».«helper.getArrayMethodName(targetType)»(«clazz».class, «compile(rightOperand, c)»))'''
			}								
		} else if (!rightType.isNull && lookUp.isAssignable(targetType, rightType.ref)){
			result = '''«typeOps.class.simpleName».«operatorName»(«compile(leftOperand, c)», «compile(rightOperand, c)»)'''
		} else if (rightType.^null || lookUp.isCastable(targetType, rightType.ref)){
			var target = typeCompiler.compile(targetType, c, false)
			result = '''«typeOps.class.simpleName».«operatorName»(«compile(leftOperand, c)», ((«target»)«compile(rightOperand, c)»))'''
		} else {
			result = '''«typeOps.class.simpleName».«operatorName»(«compile(leftOperand, c)», «compile(rightOperand, c)»)'''
		}	
		c.expectedTypeRef = null
		return result;	
		
	}
	
	def String compile(IQLAdditiveExpression e, G c) {
		var left = exprEvaluator.eval(e.leftOperand)		
		var result = "";	
		if (!left.^null && e.op.equals("+") && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.PLUS,e.rightOperand)){
			result = compileOperatorOverloading("+", IQLOperatorOverloadingUtils.PLUS, left.ref, e.leftOperand, e.rightOperand, c);
		} else if (!left.^null &&  e.op.equals("-") && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.MINUS,e.rightOperand)){
			result = compileOperatorOverloading("-", IQLOperatorOverloadingUtils.MINUS, left.ref, e.leftOperand, e.rightOperand, c);
		} else {
			if (!left.^null){
				c.expectedTypeRef = left.ref			
			} 
			result = '''«compile(e.leftOperand, c)» «e.op» «compile(e.rightOperand, c)»'''	
		}			
		c.expectedTypeRef = null
		return result		
	}
	
	def String compile(IQLMultiplicativeExpression e, G c) {
		var left = exprEvaluator.eval(e.leftOperand)
		var result = "";
		
		if (!left.^null &&  e.op.equals("*") && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.MULTIPLY,e.rightOperand)){
			result = compileOperatorOverloading("*", IQLOperatorOverloadingUtils.MULTIPLY, left.ref, e.leftOperand, e.rightOperand, c);
		} else if (!left.^null &&  e.op.equals("/") && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.DIVIDE,e.rightOperand)){
			result = compileOperatorOverloading("/", IQLOperatorOverloadingUtils.DIVIDE, left.ref, e.leftOperand, e.rightOperand, c);
		} else if (!left.^null &&  e.op.equals("%") && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.MODULO,e.rightOperand)){
			result = compileOperatorOverloading("%", IQLOperatorOverloadingUtils.MODULO, left.ref, e.leftOperand, e.rightOperand, c);
		} else {
			if (!left.^null){
				c.expectedTypeRef = left.ref			
			}
			result = '''«compile(e.leftOperand, c)» «e.op» «compile(e.rightOperand, c)»'''	
		}
		c.expectedTypeRef = null
		return result;		
	}
	
	def String compile(IQLPlusMinusExpression e, G c) {
		var left = exprEvaluator.eval(e.operand)
		if (!left.^null &&  e.op.equals("+") && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.SIMPLE_PLUS_PREFIX, new ArrayList<IQLExpression>())){
			var methodName = IQLOperatorOverloadingUtils.SIMPLE_PLUS_PREFIX;
			var typeOps = typeExtensionsDictionary.getTypeExtensions(left.ref,methodName, new ArrayList<IQLExpression>());
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».«methodName»(«compile(e.operand, c)»)'''
		} else if (!left.^null &&  e.op.equals("-") && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.SIMPLE_MINUS_PREFIX, new ArrayList<IQLExpression>())){
			var methodName = IQLOperatorOverloadingUtils.SIMPLE_MINUS_PREFIX;
			var typeOps = typeExtensionsDictionary.getTypeExtensions(left.ref,methodName, new ArrayList<IQLExpression>());
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».«methodName»(«compile(e.operand, c)»)'''
		} else {
			'''«e.op»«compile(e.operand, c)»'''
		}			
	}
	
	def String compile(IQLBooleanNotExpression e, G c) {
		var left = exprEvaluator.eval(e.operand)	
		var methodName = IQLOperatorOverloadingUtils.BOOLEAN_NOT_PREFIX;			
		if (!left.^null && typeExtensionsDictionary.hasTypeExtensions(left.ref,methodName, new ArrayList<IQLExpression>())){
			var typeOps = typeExtensionsDictionary.getTypeExtensions(left.ref,methodName, new ArrayList<IQLExpression>());
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».«methodName»(«compile(e.operand, c)»)'''
		} else {
			'''«e.op»«compile(e.operand, c)»'''
		}	
	}
	
	def String compile(IQLPrefixExpression e, G c) {
		var left = exprEvaluator.eval(e.operand)
		if (!left.^null &&  e.op.equals("++") && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.PLUS_PREFIX, new ArrayList<IQLExpression>())){
			var methodName = IQLOperatorOverloadingUtils.PLUS_PREFIX;
			var typeOps = typeExtensionsDictionary.getTypeExtensions(left.ref,methodName, new ArrayList<IQLExpression>());
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».«methodName»(«compile(e.operand, c)»)'''
		} else if (!left.^null &&  e.op.equals("--") && typeExtensionsDictionary.hasTypeExtensions(left.ref,IQLOperatorOverloadingUtils.MINUS_PREFIX, new ArrayList<IQLExpression>())){
			var methodName = IQLOperatorOverloadingUtils.MINUS_PREFIX;
			var typeOps = typeExtensionsDictionary.getTypeExtensions(left.ref,methodName, new ArrayList<IQLExpression>());
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».«methodName»(«compile(e.operand, c)»)'''
		} else {
			'''«e.op»«compile(e.operand, c)»'''
		}
	}
	
	def String compile(IQLTypeCastExpression e, G c) {
		c.expectedTypeRef = e.targetRef		
		var result = '''(«typeCompiler.compile(e.targetRef, c, false)»)(«compile(e.operand, c)»)'''
		c.expectedTypeRef = null
		return result
	}	
	
	def String compile(IQLPostfixExpression e, G c) {
		var right = exprEvaluator.eval(e.operand)
		if (!right.^null && e.op.equals("++") && typeExtensionsDictionary.hasTypeExtensions(right.ref,IQLOperatorOverloadingUtils.PLUS_POSTFIX, new ArrayList<IQLExpression>())){
			var methodName = IQLOperatorOverloadingUtils.PLUS_POSTFIX;
			var typeOps = typeExtensionsDictionary.getTypeExtensions(right.ref,methodName, new ArrayList<IQLExpression>());
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».«methodName»(«compile(e.operand, c)»)'''
		} else if (!right.^null && e.op.equals("--") && typeExtensionsDictionary.hasTypeExtensions(right.ref,IQLOperatorOverloadingUtils.MINUS_POSTFIX, new ArrayList<IQLExpression>())){
			var methodName = IQLOperatorOverloadingUtils.MINUS_POSTFIX;
			var typeOps = typeExtensionsDictionary.getTypeExtensions(right.ref,methodName, new ArrayList<IQLExpression>());
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».«methodName»(«compile(e.operand, c)»)'''
		} else {
			'''«compile(e.operand, c)»«e.op»'''
		}
	}
	
	def String compile(IQLArrayExpression e, G c) {
		var left = exprEvaluator.eval(e.leftOperand)
		var methodName = IQLOperatorOverloadingUtils.GET;
		if (!left.^null && typeExtensionsDictionary.hasTypeExtensions(left.ref,methodName,helper.createGetterArguments(e.expressions))){
			var typeOps = typeExtensionsDictionary.getTypeExtensions(left.ref,methodName,helper.createGetterArguments(e.expressions));
			c.addImport(typeOps.class.canonicalName)
			if (e.expressions.size == 1) {	
				c.expectedTypeRef = null;			
				'''«typeOps.class.simpleName».«methodName»(«compile(e.leftOperand, c)», «compile(e.expressions.get(0), c)»)'''
			} else {
				c.addImport(IQLUtils.canonicalName)
				c.expectedTypeRef = null;
				'''«typeOps.class.simpleName».«methodName»(«compile(e.leftOperand, c)», «IQLUtils.simpleName».createList(«e.expressions.map[el | compile(el, c)].join(", ")»))'''
			}
		} else if (!left.^null && typeUtils.isArray(left.ref)){
			c.addImport(ListExtensions.canonicalName)	
			if (e.expressions.size == 1) {				
				'''«ListExtensions.simpleName».«methodName»(«compile(e.leftOperand, c)», «compile(e.expressions.get(0), c)»)'''
			} else {
				c.addImport(IQLUtils.canonicalName)
				'''«ListExtensions.simpleName».«methodName»(«compile(e.leftOperand, c)», «IQLUtils.simpleName».createList(«e.expressions.map[el | compile(el, c)].join(", ")»))'''
			}		
		} else {
			'''«compile(e.leftOperand, c)»[«compile(e.expressions.get(0), c)»]'''
		}
	}
	
	
	def String compile(IQLMemberSelectionExpression e, G c) {
		var left = exprEvaluator.eval(e.leftOperand).ref;		
		if (e.sel.member instanceof JvmField) {
			'''«compile(e, left, e.sel.member as JvmField, c)»'''
		} else if (e.sel.member instanceof JvmOperation) {
			'''«compile(e, left, e.sel.member as JvmOperation, c)»'''
		}
	}
	
	def String compile(IQLMemberSelectionExpression e, JvmTypeReference left, JvmField field, G c) {	
		if (typeExtensionsDictionary.hasTypeExtensions(left,field.simpleName)){
			var typeOps = typeExtensionsDictionary.getTypeExtensions(left,field.simpleName);
			c.addImport(typeOps.class.canonicalName)
			if (helper.isJvmArray(field.type) && (c.expectedTypeRef === null || !helper.isJvmArray(c.expectedTypeRef))) {
				c.addImport(IQLUtils.canonicalName)
				'''«IQLUtils.simpleName».toList(«typeOps.class.simpleName».«field.simpleName»)'''
			} else {
				'''«typeOps.class.simpleName».«field.simpleName»'''				
			}
		} else if (helper.hasSystemTypeCompiler(field) &&  helper.getSystemTypeCompiler(field).compileFieldSelectionManually){
			var systemTypeCompiler = helper.getSystemTypeCompiler(field);
			if (helper.isJvmArray(field.type) && (c.expectedTypeRef === null || !helper.isJvmArray(c.expectedTypeRef))){
				c.addImport(IQLUtils.canonicalName)
				'''«IQLUtils.simpleName».toList(«compile(e.leftOperand, c)».«systemTypeCompiler.compileFieldSelection(field)»)'''
			} else {
				'''«compile(e.leftOperand, c)».«systemTypeCompiler.compileFieldSelection(field)»'''				
			}
		} else {
			if (helper.isJvmArray(field.type) && (c.expectedTypeRef === null || !helper.isJvmArray(c.expectedTypeRef))){
				c.addImport(IQLUtils.canonicalName)
				'''«IQLUtils.simpleName».toList(«compile(e.leftOperand, c)».«field.simpleName»)'''
			} else {
				'''«compile(e.leftOperand, c)».«field.simpleName»'''				
			}
		} 
	}

	
	def String compile(IQLMemberSelectionExpression e, JvmTypeReference left, JvmOperation method, G c) {
		c.addExceptions(method.exceptions)
		var List<IQLExpression> list = null;
		if (e.sel.args !== null) {
			list = e.sel.args.elements;
		} else {
			list = new ArrayList();
		}
		if (typeExtensionsDictionary.hasTypeExtensions(left, method.simpleName,list)){
			var typeOps = typeExtensionsDictionary.getTypeExtensions(left, method.simpleName, list);
			c.addImport(typeOps.class.canonicalName)
			if (method.returnType !== null &&helper.isJvmArray(method.returnType) && (c.expectedTypeRef === null || !helper.isJvmArray(c.expectedTypeRef))) {
				c.addImport(IQLUtils.canonicalName)
				if (typeExtensionsDictionary.ignoreFirstParameter(method)) {
					'''«IQLUtils.simpleName».toList(«typeOps.class.simpleName».«method.simpleName»(«compile(e.leftOperand, c)»«IF method.parameters.size > 0», «ENDIF»«compile(e.sel.args, method.parameters, c)»))'''
				} else {
					'''«IQLUtils.simpleName».toList(«typeOps.class.simpleName».«method.simpleName»(«compile(e.sel.args, method.parameters, c)»))'''
				}
			} else {
				if (typeExtensionsDictionary.ignoreFirstParameter(method)) {
					'''«typeOps.class.simpleName».«method.simpleName»(«compile(e.leftOperand, c)»«IF method.parameters.size > 0», «ENDIF»«compile(e.sel.args, method.parameters, c)»)'''				
				} else {
					'''«typeOps.class.simpleName».«method.simpleName»(«compile(e.sel.args, method.parameters, c)»)'''
				}
			}
		} else if (helper.hasSystemTypeCompiler(method) &&  helper.getSystemTypeCompiler(method).compileMethodSelectionManually){
			var systemTypeCompiler = helper.getSystemTypeCompiler(method);
			if (method.returnType !== null && helper.isJvmArray(method.returnType) && (c.expectedTypeRef === null || !helper.isJvmArray(c.expectedTypeRef))) {
				c.addImport(IQLUtils.canonicalName)
				'''«IQLUtils.simpleName».toList(«compile(e.leftOperand, c)».«systemTypeCompiler.compileMethodSelection(method, compileArguments(e.sel.args, method.parameters, c))»)'''	
			} else {
				'''«compile(e.leftOperand, c)».«systemTypeCompiler.compileMethodSelection(method, compileArguments(e.sel.args, method.parameters, c))»'''
			}
		} else {
			if (method.returnType !== null && helper.isJvmArray(method.returnType) && (c.expectedTypeRef === null || !helper.isJvmArray(c.expectedTypeRef))) {
				c.addImport(IQLUtils.canonicalName)
				'''«IQLUtils.simpleName».toList(«compile(e.leftOperand, c)».«method.simpleName»(«compile(e.sel.args, method.parameters, c)»))'''	
			} else {
				'''«compile(e.leftOperand, c)».«method.simpleName»(«compile(e.sel.args, method.parameters, c)»)'''
			}
		}		
	}
	
	override compile(IQLArgumentsList args, EList<JvmFormalParameter> parameters,  G c) {
		var arguments = compileArguments(args, parameters, c);
		var result = "";
		var i = 0;
		for (String argument : arguments) {
			if (i > 0) {
				result = result+", "
			}
			i++;
			result = result + argument
		}		
		return result;
	}
	
	def List<String> compileArguments(IQLArgumentsList args, EList<JvmFormalParameter> parameters,  G c) {
		var result = new ArrayList<String>;
		if (args === null) {
			return result;
		}
		for (var i = 0; i <parameters.size; i++) {			
			var expectedTypeRef = parameters.get(i).parameterType
			if (expectedTypeRef !== null) {
				c.expectedTypeRef = expectedTypeRef
			} 
			var type = exprEvaluator.eval(args.elements.get(i));
			if (expectedTypeRef !== null && helper.isJvmArray(expectedTypeRef) && !type.^null && !helper.isJvmArray(type.ref)){
				c.addImport(IQLUtils.canonicalName)
				if (helper.isPrimitiveArray(expectedTypeRef)) {
					result.add('''«IQLUtils.simpleName».«helper.getArrayMethodName(expectedTypeRef)»(«compile(args.elements.get(i), c)»)''')
				} else {
					var clazz = typeCompiler.compile(typeUtils.createTypeRef(typeUtils.getInnerType(expectedTypeRef, false)), c, true);
					result.add('''«IQLUtils.simpleName».«helper.getArrayMethodName(expectedTypeRef)»(«clazz».class, «compile(args.elements.get(i), c)»)''')
				}								
			} else if (!type.isNull && lookUp.isAssignable(parameters.get(i).parameterType, type.ref)){
				result.add(compile(args.elements.get(i), c))
			} else if (!type.isNull && lookUp.isCastable(expectedTypeRef, type.ref)){
				var target = typeCompiler.compile(expectedTypeRef, c, false)
				result.add('''((«target»)«compile(args.elements.get(i), c)»)''')	
			} else {
				result.add(compile(args.elements.get(i), c))
			}
			c.expectedTypeRef = null			
		}
		return result;
	}
	
	override compile(IQLArgumentsList list, G context) {
		if (list === null) {
			return "";
		}
		'''«list.elements.map[e | compile(e, context)].join(", ")»'''
	}
	
	override compile(IQLArgumentsMap map, JvmTypeReference typeRef,  G c) {
		var result = "";		
		for (var i = 0; i< map.elements.size; i++) {
			if (i > 0) {
				result = result+", "
			}
			var el = map.elements.get(i);
			var type = exprEvaluator.eval(el.value);
			var expectedTypeRef = helper.getPropertyType(el.key, typeRef)
			if (expectedTypeRef !== null) {
				c.expectedTypeRef = expectedTypeRef
			} 
			if (expectedTypeRef !== null && helper.isJvmArray(expectedTypeRef) && !type.^null && !helper.isJvmArray(type.ref)){
				c.addImport(IQLUtils.canonicalName)
				if (helper.isPrimitiveArray(expectedTypeRef)) {
					result = '''«IQLUtils.simpleName».«helper.getArrayMethodName(expectedTypeRef)»(«compile(el.value, c)»)'''
				} else {
					var clazz = typeCompiler.compile(typeUtils.createTypeRef(typeUtils.getInnerType(expectedTypeRef, false)), c, true);
					result = '''«IQLUtils.simpleName».«helper.getArrayMethodName(expectedTypeRef)»(«clazz».class, «compile(el.value, c)»)'''
				}
			} else if (!type.isNull && lookUp.isAssignable(expectedTypeRef, type.ref)){
				result = result + compile(el.value, c)
			} else if (!type.isNull && lookUp.isCastable(expectedTypeRef, type.ref)){
				var target = typeCompiler.compile(expectedTypeRef, c, false)
				result = result + '''((«target»)«compile(el.value, c)»)'''	
			} else {
				result = result + compile(el.value, c)
			}
			c.expectedTypeRef = null			
		}			
		return result;
	}
	
	def String compile(IQLJvmElementCallExpression e, G c) {
		if (e.element instanceof JvmOperation) {
			compile(e, e.element as JvmOperation, c)
		} else if (e.element instanceof JvmField) {
			compile(e, e.element as JvmField, c)
		} else if (e.element instanceof IQLVariableDeclaration) {
			compile(e, e.element as IQLVariableDeclaration, c)
		} else if (e.element instanceof JvmFormalParameter) {
			compile(e, e.element as JvmFormalParameter, c)
		}
	}
	
	def String compile(IQLJvmElementCallExpression e, JvmField field,  G c) {
		var thisType = lookUp.getThisType(e);
		var containerType = field.eContainer as JvmDeclaredType
		var typeRef = typeUtils.createTypeRef(containerType)
		if (thisType !== null && typeExtensionsDictionary.hasTypeExtensions(thisType, field.simpleName)){
			var typeOps = typeExtensionsDictionary.getTypeExtensions(thisType, field.simpleName);
			c.addImport(typeOps.class.canonicalName)
			if (helper.isJvmArray(field.type) && (c.expectedTypeRef === null || !helper.isJvmArray(c.expectedTypeRef))) {
				c.addImport(IQLUtils.canonicalName)
				'''«IQLUtils.simpleName».toList(«typeOps.class.simpleName».«field.simpleName»)'''
			} else {
				'''«typeOps.class.simpleName».«field.simpleName»'''
			}
		} else if (helper.hasSystemTypeCompiler(field) &&  helper.getSystemTypeCompiler(field).compileFieldSelectionManually){
			var systemTypeCompiler = helper.getSystemTypeCompiler(field);
			if (helper.isJvmArray(field.type) && (c.expectedTypeRef === null || !helper.isJvmArray(c.expectedTypeRef))){
				c.addImport(IQLUtils.canonicalName)
				if (field.isStatic) {
					'''«IQLUtils.simpleName».toList(«typeCompiler.compile(typeRef, c, true)».«systemTypeCompiler.compileFieldSelection(field)»)'''
				} else {
					'''«typeCompiler.compile(typeRef, c, true)».«systemTypeCompiler.compileFieldSelection(field)»'''				
				}
			} else {
				if (field.isStatic) {
					'''«IQLUtils.simpleName».toList(«systemTypeCompiler.compileFieldSelection(field)»)'''
				} else {
					'''«systemTypeCompiler.compileFieldSelection(field)»'''
				}
			}
		} else if (field.isStatic) {
			if (helper.isJvmArray(field.type) && (c.expectedTypeRef === null || !helper.isJvmArray(c.expectedTypeRef))) {
				c.addImport(IQLUtils.canonicalName)
				'''«IQLUtils.simpleName».toList(«typeCompiler.compile(typeRef, c, true)».«field.simpleName»)'''
			} else {
				'''«typeCompiler.compile(typeRef, c, true)».«field.simpleName»'''				
			}
		}  else {
			if (helper.isJvmArray(field.type) && (c.expectedTypeRef === null || !helper.isJvmArray(c.expectedTypeRef))) {
				c.addImport(IQLUtils.canonicalName)
				'''«IQLUtils.simpleName».toList(«field.simpleName»)'''
			} else {
				'''«field.simpleName»'''				
			}
		}
	}
	
	def String compile(IQLJvmElementCallExpression e, IQLVariableDeclaration varDecl, G c) {
		if (helper.isJvmArray(varDecl.ref) && (c.expectedTypeRef === null || !helper.isJvmArray(c.expectedTypeRef))) {
			c.addImport(IQLUtils.canonicalName)
			'''«IQLUtils.simpleName».toList(«varDecl.name»)'''
		} else {
			'''«varDecl.name»'''
		}
	}
	
	def String compile(IQLJvmElementCallExpression e, JvmFormalParameter parameter, G c) {
		if (helper.isJvmArray(parameter.parameterType) && (c.expectedTypeRef === null || !helper.isJvmArray(c.expectedTypeRef))) {
			c.addImport(IQLUtils.canonicalName)
			'''«IQLUtils.simpleName».toList(«parameter.name»)'''
		} else {
			'''«parameter.name»'''
		}
	}
	
	def String compile(IQLJvmElementCallExpression m, JvmOperation method, G c) {
		c.addExceptions(method.exceptions)
		var List<IQLExpression> list = null;
		if (m.args !== null) {
			list = m.args.elements;
		} else {
			list = new ArrayList();
		}
		var thisType = lookUp.getThisType(m);
		var containerType = method.eContainer as JvmDeclaredType
		var typeRef = typeUtils.createTypeRef(containerType)
		if (thisType !== null && typeExtensionsDictionary.hasTypeExtensions(thisType, method.simpleName, list)){
			var typeOps = typeExtensionsDictionary.getTypeExtensions(thisType, method.simpleName, list);
			c.addImport(typeOps.class.canonicalName)
			if (method.returnType !== null && helper.isJvmArray(method.returnType) && (c.expectedTypeRef === null || !helper.isJvmArray(c.expectedTypeRef))) {
				c.addImport(IQLUtils.canonicalName)
				'''«IQLUtils.simpleName».toList(«typeOps.class.simpleName».«method.simpleName»(this«IF method.parameters.size > 0», «ENDIF»«compile(m.args, method.parameters, c)»))'''
			} else {
				if (typeExtensionsDictionary.ignoreFirstParameter(method)) {
					'''«typeOps.class.simpleName».«method.simpleName»(this«IF method.parameters.size > 0», «ENDIF»«compile(m.args, method.parameters, c)»)'''
				} else {
					'''«typeOps.class.simpleName».«method.simpleName»(«compile(m.args, method.parameters, c)»)'''
				}
			}
		} else if (helper.hasSystemTypeCompiler(method) &&  helper.getSystemTypeCompiler(method).compileMethodSelectionManually){
			var systemTypeCompiler = helper.getSystemTypeCompiler(method);
			if (method.returnType !== null && helper.isJvmArray(method.returnType) && (c.expectedTypeRef === null || !helper.isJvmArray(c.expectedTypeRef))) {
				c.addImport(IQLUtils.canonicalName)
				if (method.isStatic) {
					'''«IQLUtils.simpleName».toList(«typeCompiler.compile(typeRef, c, true)».«systemTypeCompiler.compileMethodSelection(method, compileArguments(m.args, method.parameters, c))»)'''
				} else {
					'''«IQLUtils.simpleName».toList(«systemTypeCompiler.compileMethodSelection(method, compileArguments(m.args, method.parameters, c))»)'''
				}
			} else {
				if (method.isStatic) {
					'''«typeCompiler.compile(typeRef, c, true)».«systemTypeCompiler.compileMethodSelection(method,compileArguments(m.args, method.parameters, c))»'''
				} else {
					'''«systemTypeCompiler.compileMethodSelection(method, compileArguments(m.args, method.parameters, c))»'''
				}
			}
		} else if (method.isStatic) {
			if (method.returnType !== null && helper.isJvmArray(method.returnType) && (c.expectedTypeRef === null || !helper.isJvmArray(c.expectedTypeRef))) {
				c.addImport(IQLUtils.canonicalName)
				'''«IQLUtils.simpleName».toList(«typeCompiler.compile(typeRef, c, true)».«method.simpleName»(«compile(m.args,method.parameters, c)»))'''
			} else {
				'''«typeCompiler.compile(typeRef, c, true)».«method.simpleName»(«compile(m.args,method.parameters, c)»)'''			
			}
		} else {
			if (method.returnType !== null && helper.isJvmArray(method.returnType) && (c.expectedTypeRef === null || !helper.isJvmArray(c.expectedTypeRef))) {
				c.addImport(IQLUtils.canonicalName)
				'''«IQLUtils.simpleName».toList(«method.simpleName»(«IF m.args !== null»«compile(m.args,method.parameters, c)»«ENDIF»))'''
			} else {
				'''«method.simpleName»(«IF m.args !== null»«compile(m.args,method.parameters, c)»«ENDIF»)'''
			}
		}
	}

	def String compile(IQLThisExpression e, G c) {
		'''this'''
	}
	
	def String compile(IQLSuperExpression e, G c) {
		'''super'''
	}
	
	def String compile(IQLParenthesisExpression e, G c) {
		'''(«compile(e.expr, c)»)'''
	}
	
	def String compile(IQLNewExpression e, G c) {
				
		if (e.argsList !== null && e.argsMap !== null && e.argsMap.elements.size > 0) {		
			var constructor = lookUp.findPublicConstructor(e.ref, e.argsList.elements)
			c.addExceptions(constructor.exceptions)
			if (constructor !== null) {
				'''get«typeUtils.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, c, true)»(«compile(e.argsList, constructor.parameters, c)»), «compile(e.argsMap, e.ref, c)»)'''
			} else {
				'''get«typeUtils.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, c, true)»(«compile(e.argsList, c)»), «compile(e.argsMap, e.ref, c)»)'''		
			}
		} else if (e.argsMap !== null && e.argsMap.elements.size > 0) {		
			var constructor = lookUp.findPublicConstructor(e.ref, new ArrayList())
			if (constructor !== null) {
				c.addExceptions(constructor.exceptions)
			} 
			'''get«typeUtils.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, c, true)»(), «compile(e.argsMap, e.ref, c)»)'''
		} else if (e.argsList !== null) {
			var constructor = lookUp.findPublicConstructor(e.ref, e.argsList.elements)
			if (constructor !== null) {
				c.addExceptions(constructor.exceptions)
				'''new «typeCompiler.compile(e.ref, c, true)»(«compile(e.argsList, constructor.parameters, c)»)'''			
			} else {
				'''new «typeCompiler.compile(e.ref, c, true)»(«compile(e.argsList, c)»)'''			
			}
		} else if (e.ref instanceof IQLArrayTypeRef) {
			c.addImport(ArrayList.canonicalName)
			'''new «ArrayList.simpleName»()'''			
		} else {
			var constructor = lookUp.findPublicConstructor(e.ref, new ArrayList<IQLExpression>())
			if (constructor !== null) {
				c.addExceptions(constructor.exceptions)
			}
			'''new «typeCompiler.compile(e.ref, c, true)»()'''			
		}
	}	
	

	def String compile(IQLLiteralExpressionDouble e, G c) {
		if (c.expectedTypeRef !== null && typeExtensionsDictionary.hasTypeExtensions(c.expectedTypeRef,"doubleToType", e)){
			var typeOps = typeExtensionsDictionary.getTypeExtensions(c.expectedTypeRef,"doubleToType", e);
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».doubleToType(«e.value»)'''
		} else if (c.expectedTypeRef !== null) {
			if (typeUtils.isFloat(c.expectedTypeRef)) {
				'''«e.value»F'''				
			} else if (typeUtils.isDouble(c.expectedTypeRef, true)) {
				'''«e.value»D'''				
			} else {
				'''«e.value»'''
			}	
		} else {
			'''«e.value»'''
		}		
	}

	def String compile(IQLLiteralExpressionInt e, G c) {
		if (c.expectedTypeRef !== null && typeExtensionsDictionary.hasTypeExtensions(c.expectedTypeRef,"intToType", e)){
			var typeOps = typeExtensionsDictionary.getTypeExtensions(c.expectedTypeRef,"intToType", e);
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».intToType(«e.value»)'''
		} else if (c.expectedTypeRef !== null) {
			if (typeUtils.isFloat(c.expectedTypeRef)) {
				'''«e.value»F'''				
			} else if (typeUtils.isDouble(c.expectedTypeRef, true)) {
				'''«e.value»D'''				
			} else if (typeUtils.isLong(c.expectedTypeRef, true)) {
				'''«e.value»L'''				
			} else {
				'''«e.value»'''
			}
		} else {
			'''«e.value»'''
		}
	}
		
	def String compile(IQLLiteralExpressionString e, G c) {
		if (c.expectedTypeRef !== null && typeExtensionsDictionary.hasTypeExtensions(c.expectedTypeRef,"stringToType", e)){
			var typeOps = typeExtensionsDictionary.getTypeExtensions(c.expectedTypeRef,"stringToType", e);
			c.addImport(typeOps.class.canonicalName)			
			'''«typeOps.class.simpleName».stringToType("«e.value»")'''
		} else if (c.expectedTypeRef !== null) {
			if (typeUtils.isCharacter(c.expectedTypeRef)) {
				return "'"+e.value+"'"				
			} else {
				'''"«e.value»"'''
			}
		} else {
			'''"«e.value»"'''
		}
	}	
	
	def String compile(IQLLiteralExpressionBoolean e, G c) {
		if (c.expectedTypeRef !== null && typeExtensionsDictionary.hasTypeExtensions(c.expectedTypeRef,"booleanToType", e)){
			var typeOps = typeExtensionsDictionary.getTypeExtensions(c.expectedTypeRef,"booleanToType", e);
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».booleanToType(«e.value»)'''
		} else {
			'''«e.value»'''
		}
	}
	
	def String compile(IQLLiteralExpressionType e, G c) {
		'''((Class)«typeCompiler.compile(e.value, c, true)».class)'''
	}

	def String compile(IQLLiteralExpressionRange e, G c) {
		var from = Integer.parseInt(e.value.substring(0, e.value.indexOf('.')))
		var to = Integer.parseInt(e.value.substring(e.value.lastIndexOf('.')+1, e.value.length))
		if (c.expectedTypeRef !== null && typeExtensionsDictionary.hasTypeExtensions(c.expectedTypeRef,"rangeToType", e)){
			var typeOps = typeExtensionsDictionary.getTypeExtensions(c.expectedTypeRef,"rangeToType",e);
			c.addImport(typeOps.class.canonicalName)
			c.addImport(Range.canonicalName)
			'''«typeOps.class.simpleName».rangeToType(new «Range.simpleName»(«from» , «to»))'''
		} else {
			c.addImport(Range.canonicalName)
			'''new «Range.simpleName»(«from» , «to»)'''
		}
	}
	
	def String compile(IQLLiteralExpressionNull e, G c) {
		'''null'''	
	}
	
	def String compile(IQLLiteralExpressionList e, G c) {				
		if (c.expectedTypeRef !== null && typeExtensionsDictionary.hasTypeExtensions(c.expectedTypeRef,"listToType", e)){
			var typeOps = typeExtensionsDictionary.getTypeExtensions(c.expectedTypeRef,"listToType", e);
			c.addImport(typeOps.class.canonicalName)
			c.expectedTypeRef = null;			
			'''«typeOps.class.simpleName».listToType(«IQLUtils.simpleName».createList(«e.elements.map[el | compile(el, c)].join(", ")»))'''
		} else {
			c.addImport(IQLUtils.canonicalName)		
			'''«IQLUtils.simpleName».createList(«e.elements.map[el | compile(el, c)].join(", ")»)'''
		}
	}
	
	def String compile(IQLLiteralExpressionMap e, G c) {
		if (c.expectedTypeRef !== null && typeExtensionsDictionary.hasTypeExtensions(c.expectedTypeRef,"mapToType", e)){
			var typeOps = typeExtensionsDictionary.getTypeExtensions(c.expectedTypeRef,"mapToType", e);
			c.addImport(typeOps.class.canonicalName)
			c.expectedTypeRef = null;
			'''«typeOps.class.simpleName».mapToType(«IQLUtils.simpleName».createMap(«e.elements.map[el | compile(el.key, c) +", " +compile(el.value, c)].join(", ")»))'''
		} else {
			c.addImport(IQLUtils.canonicalName)
			'''	«IQLUtils.simpleName».createMap(«e.elements.map[el | compile(el.key, c) +", " +compile(el.value, c)].join(", ")»)'''
		}
	}	
	
}

