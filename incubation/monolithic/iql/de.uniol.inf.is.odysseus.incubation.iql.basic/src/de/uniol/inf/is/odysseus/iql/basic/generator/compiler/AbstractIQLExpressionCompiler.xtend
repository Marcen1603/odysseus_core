package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttributeSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInstanceOfExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionBoolean;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionChar;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionDouble;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionNull;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionRange;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionString;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionNew;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionParenthesis;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionSuper;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionThis;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionVariable;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression;
import de.uniol.inf.is.odysseus.iql.basic.types.IQLUtils
import de.uniol.inf.is.odysseus.iql.basic.types.Range
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.IIQLExpressionParser
import org.eclipse.xtext.common.types.JvmTypeReference
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionInt
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.emf.common.util.EList
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionMethod
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils
import de.uniol.inf.is.odysseus.iql.basic.typing.^extension.IIQLTypeExtensionsFactory
import org.eclipse.xtext.common.types.JvmField
import org.eclipse.xtext.common.types.JvmGenericType

abstract class AbstractIQLExpressionCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>, E extends IIQLExpressionParser, U extends IIQLTypeUtils, L extends IIQLLookUp, O extends IIQLTypeExtensionsFactory> implements IIQLExpressionCompiler<G>{
	
	protected H helper;
	
	protected T typeCompiler;
	
	protected E exprParser;
	
	protected U typeUtils;
	
	protected L lookUp;
	
	protected O typeExtensionsFactory;
	
	
	new (H helper, T typeCompiler, E exprParser, U typeUtils, L lookUp, O typeExtensionsFactory) {
		this.helper = helper;
		this.typeCompiler = typeCompiler;
		this.exprParser = exprParser;
		this.typeUtils = typeUtils;
		this.lookUp = lookUp;
		this.typeExtensionsFactory = typeExtensionsFactory;
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
		} else if (expr instanceof IQLTerminalExpressionVariable) {
			return compile(expr as IQLTerminalExpressionVariable, context);
		} else if (expr instanceof IQLTerminalExpressionMethod) {
			return compile(expr as IQLTerminalExpressionMethod, context);
		} else if (expr instanceof IQLTerminalExpressionThis) {
			return compile(expr as IQLTerminalExpressionThis, context);
		} else if (expr instanceof IQLTerminalExpressionSuper) {
			return compile(expr as IQLTerminalExpressionSuper, context);
		} else if (expr instanceof IQLTerminalExpressionParenthesis) {
			return compile(expr as IQLTerminalExpressionParenthesis, context);
		} else if (expr instanceof IQLTerminalExpressionNew) {
			return compile(expr as IQLTerminalExpressionNew, context);
		} else if (expr instanceof IQLLiteralExpressionInt) {
			return compile(expr as IQLLiteralExpressionInt, context);
		} else if (expr instanceof IQLLiteralExpressionDouble) {
			return compile(expr as IQLLiteralExpressionDouble, context);
		} else if (expr instanceof IQLLiteralExpressionString) {
			return compile(expr as IQLLiteralExpressionString, context);
		} else if (expr instanceof IQLLiteralExpressionBoolean) {
			return compile(expr as IQLLiteralExpressionBoolean, context);
		} else if (expr instanceof IQLLiteralExpressionChar) {
			return compile(expr as IQLLiteralExpressionChar, context);
		} else if (expr instanceof IQLLiteralExpressionRange) {
			return compile(expr as IQLLiteralExpressionRange, context);
		} else if (expr instanceof IQLLiteralExpressionNull) {
			return compile(expr as IQLLiteralExpressionNull, context);
		} else if (expr instanceof IQLLiteralExpressionList) {
			return compile(expr as IQLLiteralExpressionList, context);
		} else if (expr instanceof IQLLiteralExpressionMap) {
			return compile(expr as IQLLiteralExpressionMap, context);
		}
		return null;
	}
	
	def String compile(IQLAssignmentExpression e, G c) {
		if (e.leftOperand instanceof IQLMemberSelectionExpression) {
			compileAssignmentExpr(e, e.leftOperand as IQLMemberSelectionExpression, c);			
		} else if (e.leftOperand instanceof IQLArrayExpression) {
			compileAssignmentExpr(e, e.leftOperand as IQLArrayExpression, c);			
		} else {
			compileAssignmentExpr(e,c);
		}
	}
	
	def String compileAssignmentExpr(IQLAssignmentExpression e, IQLMemberSelectionExpression selExpr,  G c) {
		compileAssignmentExpr(e, c);		
	}	
	
	def String compileAssignmentExpr(IQLAssignmentExpression e, IQLArrayExpression arrayExpr,  G c) {
		var leftType = exprParser.getType(e.leftOperand)
		if (!leftType.^null) {
			c.expectedTypeRef = leftType.ref
		}
		var arrayType = exprParser.getType(arrayExpr.leftOperand)
		if (!arrayType.^null && typeExtensionsFactory.hasTypeExtensions(arrayType.ref,"set",3)){
			var typeOps = typeExtensionsFactory.getTypeExtensions(arrayType.ref,"set",3);
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».set(«compile(arrayExpr.leftOperand, c)», «compile(e.rightOperand, c)», «arrayExpr.expressions.map[ el | compile(el, c)].join(", ")»)'''
		} else {
			compileAssignmentExpr(e, c);
		}
	}
	
	def String compileAssignmentExpr(IQLAssignmentExpression e, G c) {
		var left = exprParser.getType(e.leftOperand)
		var right = exprParser.getType(e.rightOperand)
		if (!left.^null){
			c.expectedTypeRef = left.ref
		}
		var result = "";
		if (left.^null || right.^null || lookUp.isAssignable(left.ref, right.ref)){
			result = '''«compile(e.leftOperand, c)» «e.op» «compile(e.rightOperand, c)»'''
		} else {
			var target = typeCompiler.compile(left.ref, c, false)
			result = '''«compile(e.leftOperand, c)» «e.op» ((«target») «compile(e.rightOperand, c)»)'''	
		}
		c.expectedTypeRef = null
		return result;
	}
	
	
	def String compile(IQLLogicalOrExpression e, G c) {
		'''«compile(e.leftOperand, c)» «e.op» «compile(e.rightOperand, c)»'''
	}
	
	def String compile(IQLLogicalAndExpression e, G c) {
		'''«compile(e.leftOperand, c)» «e.op» «compile(e.rightOperand, c)»'''
	}
	
	def String compile(IQLEqualityExpression e, G c) {
		'''«compile(e.leftOperand, c)» «e.op» «compile(e.rightOperand, c)»'''
	}
	
	def String compile(IQLRelationalExpression e, G c) {
		'''«compile(e.leftOperand, c)» «e.op» «compile(e.rightOperand, c)»'''
	}
	
	def String compile(IQLInstanceOfExpression e, G c) {
		'''«compile(e.leftOperand, c)» instanceof «typeCompiler.compile(e.targetRef, c, true)»'''
	}
	
	def String compile(IQLAdditiveExpression e, G c) {
		var left = exprParser.getType(e.leftOperand)
		var result = "";
		if (!left.^null){
			c.expectedTypeRef = left.ref			
		} 	
		if (!left.^null && e.op.equals("+") && typeExtensionsFactory.hasTypeExtensions(left.ref,"plus",2)){
			var typeOps = typeExtensionsFactory.getTypeExtensions(left.ref,"plus",2);
			c.addImport(typeOps.class.canonicalName)
			result = '''«typeOps.class.simpleName».plus(«compile(e.leftOperand, c)», «compile(e.rightOperand, c)»)'''
		} else if (!left.^null &&  e.op.equals("-") && typeExtensionsFactory.hasTypeExtensions(left.ref,"minus",2)){
			var typeOps = typeExtensionsFactory.getTypeExtensions(left.ref,"minus",2);
			c.addImport(typeOps.class.canonicalName)
			result = '''«typeOps.class.simpleName».minus(«compile(e.leftOperand, c)», «compile(e.rightOperand, c)»)'''
		} else {
			result = '''«compile(e.leftOperand, c)» «e.op» «compile(e.rightOperand, c)»'''	
		}
			
		c.expectedTypeRef = null
		return result		
	}
	
	def String compile(IQLMultiplicativeExpression e, G c) {
		var left = exprParser.getType(e.leftOperand)
		var result = "";
		
		if (!left.^null){
			c.expectedTypeRef = left.ref
		}
		if (!left.^null &&  e.op.equals("*") && typeExtensionsFactory.hasTypeExtensions(left.ref,"multiply",2)){
			var typeOps = typeExtensionsFactory.getTypeExtensions(left.ref,"multiply",2);
			c.addImport(typeOps.class.canonicalName)
			result = '''«typeOps.class.simpleName».multiply(«compile(e.leftOperand, c)», «compile(e.rightOperand, c)»)'''
		} else if (!left.^null &&  e.op.equals("/") && typeExtensionsFactory.hasTypeExtensions(left.ref,"divide",2)){
			var typeOps = typeExtensionsFactory.getTypeExtensions(left.ref,"divide",2);
			c.addImport(typeOps.class.canonicalName)
			result = '''«typeOps.class.simpleName».divide(«compile(e.leftOperand, c)», «compile(e.rightOperand, c)»)'''
		} else if (!left.^null &&  e.op.equals("%") && typeExtensionsFactory.hasTypeExtensions(left.ref,"modulo",2)){
			var typeOps = typeExtensionsFactory.getTypeExtensions(left.ref,"modulo",2);
			c.addImport(typeOps.class.canonicalName)
			result = '''«typeOps.class.simpleName».modulo(«compile(e.leftOperand, c)», «compile(e.rightOperand, c)»)'''
		} else {
			result = '''«compile(e.leftOperand, c)» «e.op» «compile(e.rightOperand, c)»'''	
		}
		c.expectedTypeRef = null
		return result;		
	}
	
	def String compile(IQLPlusMinusExpression e, G c) {
		'''«e.op»«compile(e.operand, c)»'''
	}
	
	def String compile(IQLBooleanNotExpression e, G c) {
		'''«e.op»«compile(e.operand, c)»'''
	}
	
	def String compile(IQLPrefixExpression e, G c) {
		var left = exprParser.getType(e.operand)
		if (!left.^null &&  e.op.equals("++") && typeExtensionsFactory.hasTypeExtensions(left.ref,"plusPrefix",1)){
			var typeOps = typeExtensionsFactory.getTypeExtensions(left.ref,"plusPrefix",1);
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».plusPrefix(«compile(e.operand, c)»)'''
		} else if (!left.^null &&  e.op.equals("--") && typeExtensionsFactory.hasTypeExtensions(left.ref,"minusPrefix",1)){
			var typeOps = typeExtensionsFactory.getTypeExtensions(left.ref,"minusPrefix",1);
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».minusPrefix(«compile(e.operand, c)»)'''
		} else {
			'''«e.op»«compile(e.operand, c)»'''
		}
	}
	
	def String compile(IQLTypeCastExpression e, G c) {
		'''(«typeCompiler.compile(e.targetRef, c, false)»)(«compile(e.operand, c)»)'''
	}	
	
	def String compile(IQLPostfixExpression e, G c) {
		var right = exprParser.getType(e.operand)
		if (!right.^null && e.op.equals("++") && typeExtensionsFactory.hasTypeExtensions(right.ref,"plusPostfix",1)){
			var typeOps = typeExtensionsFactory.getTypeExtensions(right.ref,"plusPostfix",1);
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».plusPostfix(«compile(e.operand, c)»)'''
		} else if (!right.^null && e.op.equals("--") && typeExtensionsFactory.hasTypeExtensions(right.ref,"minusPostfix",1)){
			var typeOps = typeExtensionsFactory.getTypeExtensions(right.ref,"minusPostfix",1);
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».minusPostfix(«compile(e.operand, c)»)'''
		} else {
			'''«compile(e.operand, c)»«e.op»'''
		}
	}
	
	def String compile(IQLArrayExpression e, G c) {
		var left = exprParser.getType(e.leftOperand)
		if (!left.^null && typeExtensionsFactory.hasTypeExtensions(left.ref,"get",2)){
			var typeOps = typeExtensionsFactory.getTypeExtensions(left.ref,"get",2);
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».get(«compile(e.leftOperand, c)», «e.expressions.map[ el | compile(el, c)].join(", ")»)'''
		} else {
			'''«compile(e.leftOperand, c)»[«compile(e.expressions.get(0), c)»]'''
		}
	}
	
	
	def String compile(IQLMemberSelectionExpression e, G c) {
		var left = exprParser.getType(e.leftOperand).ref;
		
		if (e.rightOperand instanceof IQLAttributeSelection) {
			var a = e.rightOperand as IQLAttributeSelection
			'''«compile(a,left, e, c)»'''
		} else if (e.rightOperand instanceof IQLMethodSelection) {
			var m = e.rightOperand as IQLMethodSelection
			'''«compile(m, left, e, c)»'''
		}
	}
	
	def String compile(IQLAttributeSelection a, JvmTypeReference left, IQLMemberSelectionExpression expr, G c) {	
		if (typeExtensionsFactory.hasTypeExtensions(left,a.^var.simpleName)){
			var typeOps = typeExtensionsFactory.getTypeExtensions(left,a.^var.simpleName);
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».«a.^var.simpleName»'''
		} else {
			'''«compile(expr.leftOperand, c)».«a.^var.simpleName»'''
		}
	}

	
	def String compile(IQLMethodSelection m,JvmTypeReference left, IQLMemberSelectionExpression expr, G c) {
		if (typeExtensionsFactory.hasTypeExtensions(left, m.method.simpleName, m.args.elements.size)){
			var typeOps = typeExtensionsFactory.getTypeExtensions(left, m.method.simpleName, m.args.elements.size);
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».«m.method.simpleName»(«compile(expr.leftOperand, c)»«IF m.args.elements.size > 0», «ENDIF»«compile(m.args, m.method.parameters, c)»)'''
		} else {
			'''«compile(expr.leftOperand, c)».«m.method.simpleName»(«compile(m.args, m.method.parameters, c)»)'''
		}		
	}
	
	override compile(IQLArgumentsList args, EList<JvmFormalParameter> parameters,  G c) {
		var result = "";
		for (var i = 0; i <parameters.size; i++) {
			if (i > 0) {
				result = result + ","
			}
			c.expectedTypeRef = parameters.get(i).parameterType
			result = result + compile(args.elements.get(i), c)
			c.expectedTypeRef = null
			
		}
		return result;
	}
	
	override compile(IQLArgumentsList list, G context) {
		'''«list.elements.map[e | compile(e, context)].join(", ")»'''
	}
	
	override compile(IQLArgumentsMap map, JvmTypeReference typeRef,  G c) {
		var result = "";		
		for (var i = 0; i< map.elements.size; i++) {
			if (i > 0) {
				result = result+", "
			}
			var el = map.elements.get(i);
			var type = helper.getPropertyType(el.key, typeRef)
			if (type != null) {
				c.expectedTypeRef = type
			} 
			result = result+ compile(el.value, c)
			c.expectedTypeRef = null			
		}			
		return result;
	}
	
	def String compile(IQLTerminalExpressionVariable e, G c) {
		if (e.^var instanceof JvmField) {
			var a = e.^var as JvmField			
			if (a.isStatic) {
				var containerType = a.eContainer as JvmGenericType
				var javaType = typeUtils.getJavaType(typeUtils.getLongName(containerType, false))
				c.addImport(javaType.canonicalName)
				'''«javaType.simpleName».«a.simpleName»'''
			} else {
				'''«a.simpleName»'''
			}
		} else {
			if (e.^var instanceof IQLVariableDeclaration) {
				var v = e.^var as IQLVariableDeclaration		
				'''«v.name»'''
			} else if (e.^var instanceof JvmFormalParameter) {
				var p = e.^var as JvmFormalParameter
				'''«p.name»'''
			} else {
				''''''
			}
		}
	}
	
	def String compile(IQLTerminalExpressionMethod m, G c) {
		var method = m.method;		
		var typeDef = exprParser.getThisType(m)
		if (!typeDef.isNull() && typeExtensionsFactory.hasTypeExtensions(typeDef.ref, m.method.simpleName, m.args.elements.size)){
			var typeOps = typeExtensionsFactory.getTypeExtensions(typeDef.ref, m.method.simpleName, m.args.elements.size);
			c.addImport(typeOps.class.canonicalName)
			'''«typeOps.class.simpleName».«m.method.simpleName»(this«IF m.args.elements.size > 0», «ENDIF»«compile(m.args, m.method.parameters, c)»)'''
		} else if (method.isStatic) {
			var containerType = method.eContainer as JvmGenericType
			var javaType = typeUtils.getJavaType(typeUtils.getLongName(containerType, false))
			c.addImport(javaType.canonicalName)
			'''«javaType.simpleName».«m.method.simpleName»(«compile(m.args,method.parameters, c)»)'''			
		} else {
			'''«m.method.simpleName»(«compile(m.args, c)»)'''
		}
	}

	def String compile(IQLTerminalExpressionThis e, G c) {
		'''this'''
	}
	
	def String compile(IQLTerminalExpressionSuper e, G c) {
		'''super'''
	}
	
	def String compile(IQLTerminalExpressionParenthesis e, G c) {
		'''(«compile(e.expr, c)»)'''
	}
	
	def String compile(IQLTerminalExpressionNew e, G c) {
				
		if (e.argsMap != null && e.argsMap.elements.size > 0) {		
			var constructor = lookUp.findConstructor(e.ref, e.argsList.elements.size)
			if (constructor != null) {
				'''get«typeUtils.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, c, true)»(«compile(e.argsList, constructor.parameters, c)»), «compile(e.argsMap, e.ref, c)»)'''
			} else {
				'''get«typeUtils.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, c, true)»(«compile(e.argsList, c)»), «compile(e.argsMap, e.ref, c)»)'''		
			}
		} else if (e.argsList != null) {
			var constructor = lookUp.findConstructor(e.ref, e.argsList.elements.size)
			if (constructor != null) {
				'''new «typeCompiler.compile(e.ref, c, true)»(«compile(e.argsList, constructor.parameters, c)»)'''			
			} else {
				'''new «typeCompiler.compile(e.ref, c, true)»(«compile(e.argsList, c)»)'''			
			}
		} else if (e.ref instanceof IQLArrayTypeRef) {
			'''new «typeCompiler.compile(e.ref, c, false)»'''			
		} else {
			'''new «typeCompiler.compile(e.ref, c, true)»()'''			
		}
	}	
	

	def String compile(IQLLiteralExpressionDouble e, G c) {
		if (c.expectedTypeRef != null) {
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
		if (c.expectedTypeRef != null) {
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
		'''"«e.value»"'''
	}	
	
	def String compile(IQLLiteralExpressionBoolean e, G c) {
		'''«e.value»'''
	}		
	
	def String compile(IQLLiteralExpressionChar e, G c) {
		''''«e.value»' '''
	}

	def String compile(IQLLiteralExpressionRange e, G c) {
		c.addImport(Range.canonicalName)
		var from = Integer.parseInt(e.value.substring(0, e.value.indexOf('.')))
		var to = Integer.parseInt(e.value.substring(e.value.lastIndexOf('.')+1, e.value.length))
		'''new «Range.simpleName»(«from» , «to»)'''
	}
	
	def String compile(IQLLiteralExpressionNull e, G c) {
		'''null'''
	}
	
	def String compile(IQLLiteralExpressionList e, G c) {
		c.addImport(IQLUtils.canonicalName)		
		'''«IQLUtils.simpleName».createList(«e.elements.map[el | compile(el, c)].join(", ")»)'''
	}
	
	def String compile(IQLLiteralExpressionMap e, G c) {
		c.addImport(IQLUtils.canonicalName)
		'''	«IQLUtils.simpleName».createMap(«e.elements.map[el | compile(el.key, c) +", " +compile(el.value, c)].join(", ")»)'''
	}

	
}
