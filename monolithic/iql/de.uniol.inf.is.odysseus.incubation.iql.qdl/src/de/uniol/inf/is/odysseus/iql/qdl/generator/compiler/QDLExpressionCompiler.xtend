package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLExpressionCompiler
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.IQDLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression
import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression
import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression
import de.uniol.inf.is.odysseus.iql.qdl.exprevaluator.IQDLExpressionEvaluator
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.IQDLGeneratorContext
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils
import de.uniol.inf.is.odysseus.iql.qdl.typing.typeextension.IQDLTypeExtensionsDictionary
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.QDLSubscribableWithPort
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary

class QDLExpressionCompiler extends AbstractIQLExpressionCompiler<IQDLCompilerHelper, IQDLGeneratorContext, IQDLTypeCompiler, IQDLExpressionEvaluator, IQDLTypeUtils, IQDLLookUp, IQDLTypeExtensionsDictionary, IQDLTypeDictionary> implements IQDLExpressionCompiler{
		
	@Inject
	new(IQDLCompilerHelper helper, IQDLTypeCompiler typeCompiler, IQDLExpressionEvaluator exprEvaluator, IQDLTypeUtils typeUtils, IQDLLookUp lookUp, IQDLTypeExtensionsDictionary typeExtensionsDictionary, IQDLTypeDictionary typeDictionary) {
		super(helper, typeCompiler, exprEvaluator, typeUtils, lookUp, typeExtensionsDictionary, typeDictionary)
	}
	
	override String compile(IQLExpression e, IQDLGeneratorContext context) {
		if (e instanceof IQLSubscribeExpression) {
			return compile(e as IQLSubscribeExpression, context)
		} else if (e instanceof IQLPortExpression) {
			return compile(e as IQLPortExpression, context)
		} else {
			super.compile(e, context)
		}	
	}
	

	
	def String compile(IQLSubscribeExpression e, IQDLGeneratorContext context) {	
		if (e.op.equals("->")) {
			'''subscribeSink(«compile(e.leftOperand, context)», «compile(e.rightOperand, context)»)'''
		} else {
			'''subscribeToSource(«compile(e.leftOperand, context)», «compile(e.rightOperand, context)»)'''
		}
	}	
	
	
	def String compile(IQLPortExpression e, IQDLGeneratorContext context) {	
		context.addImport(QDLSubscribableWithPort.canonicalName)
		'''new «QDLSubscribableWithPort.simpleName»(«compile(e.leftOperand, context)», «compile(e.rightOperand, context)»)'''
	}

	
	override String compile(IQLNewExpression e, IQDLGeneratorContext context) {		
		if (helper.isOperator(e.ref)) {
			if (e.argsList !== null && e.argsMap !== null && e.argsMap.elements.size > 0) {	
				var constructor = lookUp.findPublicConstructor(e.ref, e.argsList.elements)
				var args = e.argsList.elements.size > 0
				if (constructor !== null) {
					'''getOperator«typeUtils.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, context, false)»("«typeUtils.getShortName(e.ref, false)»"«IF args», «ENDIF»«compile(e.argsList,constructor.parameters, context)»), operators,  «compile(e.argsMap,e.ref, context)»)'''
				} else {
					'''getOperator«typeUtils.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, context, false)»("«typeUtils.getShortName(e.ref, false)»"«IF args», «ENDIF»«compile(e.argsList, context)»), operators,  «compile(e.argsMap,e.ref, context)»)'''
				}		
			} else if (e.argsMap !== null && e.argsMap.elements.size > 0) {	
				'''getOperator«typeUtils.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, context, false)»("«typeUtils.getShortName(e.ref, false)»"), operators,  «compile(e.argsMap,e.ref, context)»)'''		
			} else if (e.argsList !== null) {
				var constructor = lookUp.findPublicConstructor(e.ref, e.argsList.elements)
				var args = e.argsList.elements.size > 0
				if (constructor !== null) {
					'''getOperator«typeUtils.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, context, false)»("«typeUtils.getShortName(e.ref, false)»"«IF args», «ENDIF»«compile(e.argsList,constructor.parameters, context)»), operators)'''
				} else {
					'''getOperator«typeUtils.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, context, false)»("«typeUtils.getShortName(e.ref, false)»"«IF args», «ENDIF»«compile(e.argsList, context)»)>, operators)'''
				}	
			} else {
				'''getOperator«typeUtils.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, context, false)»("«typeUtils.getShortName(e.ref, false)»"), operators)'''
			}
		} else if (helper.isSource(e.ref)) {
				'''new «typeCompiler.compile(e.ref, context, false)»("«typeUtils.getShortName(e.ref, false)»")'''
		} else {
			super.compile(e, context);
		}
	}
	
	
//	override String compile(IQLMemberSelectionExpression e, JvmTypeReference left, JvmField field, IQDLGeneratorContext c) {
//		if (helper.isSource(left)){
//			var name = field.simpleName;
//			if (helper.isSourceAttribute(left, name)) {
//				'''"«name»"'''
//			} else {
//				super.compile(e, left, field, c);
//			}
//		} else {
//			super.compile(e, left, field, c);
//		}
//	}
	
//	override String compile(IQLMemberSelectionExpression e, JvmTypeReference left, JvmOperation method, IQDLGeneratorContext c) {	
//		if (helper.isOperator(left)){
//			if (helper.isParameterGetter(method, left) && helper.isJvmArray(method.returnType) && (c.expectedTypeRef == null || !helper.isJvmArray(c.expectedTypeRef))){
//				var name = helper.getParameterOfGetter(method)
//				c.addImport(IQLUtils.canonicalName)	
//				'''«IQLUtils.simpleName».toList(«compile(e.leftOperand, c)».getParameter("«name»"))'''
//			} else if (helper.isParameterGetter(method, left)) {
//				var name = helper.getParameterOfGetter(method)
//				'''«compile(e.leftOperand, c)».getParameter("«name»")'''
//			} else if (helper.isParameterSetter(method , left)) {
//				var name = helper.getParameterOfSetter(method)
//				'''«compile(e.leftOperand, c)».setParameter("«name»",«compile(e.sel.args, method.parameters, c)»)'''
//			} else {
//				super.compile(e, left, method, c);
//			}
//		} else {
//			super.compile(e, left, method, c);
//		}
//	}
	
//	override String compileAssignmentExpr(IQLAssignmentExpression e, IQLMemberSelectionExpression selExpr, IQDLGeneratorContext c) {
//		if (selExpr.sel.member instanceof JvmOperation) {
//			var op = selExpr.sel.member as JvmOperation
//			var leftType = exprEvaluator.eval(selExpr.leftOperand);
//			var rightType = exprEvaluator.eval(e.rightOperand);
//			if (!leftType.isNull && helper.isOperator(leftType.ref) && helper.isParameterSetter(op, leftType.ref) && helper.isJvmArray(op.parameters.get(0).parameterType) && !rightType.isNull && !helper.isJvmArray(rightType.ref)){
//				c.addImport(IQLUtils.canonicalName)
//				var dim = typeUtils.getArrayDim(op.parameters.get(0).parameterType);
//				var pName = helper.getParameterOfSetter(op);
//				'''«compile(selExpr.leftOperand, c)».setParameter("«pName»",«IQLUtils.simpleName».toArray«dim»(«compile(e.rightOperand, c)»))'''		
//			} else if (!leftType.isNull && helper.isOperator(leftType.ref) && helper.isParameterSetter(op, leftType.ref)) {
//				var pName = helper.getParameterOfSetter(op);
//				'''«compile(selExpr.leftOperand, c)».setParameter("«pName»",«compile(e.rightOperand, c)»)'''		
//			}else {
//				super.compileAssignmentExpr(e, selExpr, c);
//			}
//		} else {
//			super.compileAssignmentExpr(e, selExpr, c);
//		}
//	}

	
}
