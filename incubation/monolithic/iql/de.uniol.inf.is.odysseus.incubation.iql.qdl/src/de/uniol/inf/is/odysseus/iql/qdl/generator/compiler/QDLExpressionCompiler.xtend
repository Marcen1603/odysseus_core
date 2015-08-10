package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLExpressionCompiler
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression
import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression
import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression
import de.uniol.inf.is.odysseus.iql.qdl.types.query.QDLSubscribableWithPort
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLExpressionParser
import org.eclipse.xtext.common.types.JvmTypeReference
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression
import org.eclipse.xtext.common.types.JvmField
import org.eclipse.xtext.common.types.JvmOperation
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeExtensionsFactory

class QDLExpressionCompiler extends AbstractIQLExpressionCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler, QDLExpressionParser, QDLTypeUtils, QDLLookUp, QDLTypeExtensionsFactory>{
		
	@Inject
	new(QDLCompilerHelper helper, QDLTypeCompiler typeCompiler, QDLExpressionParser exprParser, QDLTypeUtils typeUtils, QDLLookUp lookUp, QDLTypeExtensionsFactory typeOperatorsFactory) {
		super(helper, typeCompiler, exprParser, typeUtils, lookUp, typeOperatorsFactory)
	}
	
	override String compile(IQLExpression e, QDLGeneratorContext context) {
		if (e instanceof IQLSubscribeExpression) {
			return compile(e as IQLSubscribeExpression, context)
		} else if (e instanceof IQLPortExpression) {
			return compile(e as IQLPortExpression, context)
		} else {
			super.compile(e, context)
		}	
	}
	

	
	def String compile(IQLSubscribeExpression e, QDLGeneratorContext context) {	
		if (e.op.equals("->")) {
			'''subscribeSink(«compile(e.leftOperand, context)», «compile(e.rightOperand, context)»)'''
		} else {
			'''subscribeToSource(«compile(e.leftOperand, context)», «compile(e.rightOperand, context)»)'''
		}
	}	
	
	
	def String compile(IQLPortExpression e, QDLGeneratorContext context) {	
		context.addImport(QDLSubscribableWithPort.canonicalName)
		'''new «QDLSubscribableWithPort.simpleName»(«compile(e.leftOperand, context)», «compile(e.rightOperand, context)»)'''
	}
	
	override String compile(IQLNewExpression e, QDLGeneratorContext context) {		
		if (helper.isOperator(e.ref)) {
			var opName = helper.getLogicalOperatorName(e.ref)
			if (e.argsMap != null && e.argsMap.elements.size > 0) {	
				var constructor = lookUp.findConstructor(e.ref, e.argsList.elements)
				var args = e.argsList.elements.size > 0
				if (constructor != null) {
					'''getOperator«typeUtils.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, context, false)»<«opName»>(new «opName»()«IF args», «ENDIF»«compile(e.argsList,constructor.parameters, context)»), operators,  «compile(e.argsMap,e.ref, context)»)'''
				} else {
					'''getOperator«typeUtils.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, context, false)»<«opName»>(new «opName»()«IF args», «ENDIF»«compile(e.argsList, context)»), operators,  «compile(e.argsMap,e.ref, context)»)'''
				}		
			} else if (e.argsList != null) {
				var constructor = lookUp.findConstructor(e.ref, e.argsList.elements)
				var args = e.argsList.elements.size > 0
				if (constructor != null) {
					'''getOperator«typeUtils.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, context, false)»<«opName»>(new «opName»()«IF args», «ENDIF»«compile(e.argsList,constructor.parameters, context)»), operators)'''
				} else {
					'''getOperator«typeUtils.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, context, false)»<«opName»>(new «opName»()«IF args», «ENDIF»«compile(e.argsList, context)»)>, operators)'''
				}	
			} else {
				'''getOperator«typeUtils.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, context, false)»<«opName»>(new «opName»()), operators)'''
			}
		} else if (helper.isSource(e.ref)) {
				'''getSource("«typeUtils.getShortName(e.ref, false)»")'''
		} else {
			super.compile(e, context);
		}
	}
	
	
	override String compile(IQLMemberSelectionExpression e, JvmTypeReference left, JvmField field, QDLGeneratorContext c) {
		if (helper.isSource(left)){
			var name = field.simpleName;
			if (helper.isSourceAttribute(left, name)) {
				'''"«name»"'''
			} else {
				super.compile(e, left, field, c);
			}
		} else if (helper.isOperator(left)){
			var pName = field.simpleName;
			if (helper.isParameter(pName, left)) {
				'''«compile(e.leftOperand, c)».getParameter("«pName»")'''
			} else {
				super.compile(e, left, field, c);
			}
		} else {
			super.compile(e, left, field, c);
		}
	}
	
	override String compile(IQLMemberSelectionExpression e, JvmTypeReference left, JvmOperation method, QDLGeneratorContext c) {	
		if (helper.isOperator(left)){
			if (helper.isParameterGetter(method, left)) {
				var name = helper.getParameterOfGetter(method)
				'''«compile(e.leftOperand, c)».getParameter("«name»")'''
			} else if (helper.isParameterSetter(method , left)) {
				var name = helper.getParameterOfSetter(method)
				'''«compile(e.leftOperand, c)».setParameter("«name»",«compile(e.sel.args, method.parameters, c)»)'''
			} else {
				super.compile(e, left, method, c);
			}
		} else {
			super.compile(e, left, method, c);
		}
	}
	
	override String compileAssignmentExpr(IQLAssignmentExpression e, IQLMemberSelectionExpression selExpr, QDLGeneratorContext c) {
		if (selExpr.sel.member instanceof JvmField) {
			var field = selExpr.sel.member as JvmField
			var left = exprParser.getType(selExpr.leftOperand);
			if (!left.isNull && helper.isOperator(left.ref) && helper.isParameter(field.simpleName, left.ref)) {
				'''«compile(selExpr.leftOperand, c)».setParameter("«field.simpleName»",«compile(e.rightOperand, c)»)'''		
			} else {
				super.compileAssignmentExpr(e, selExpr, c);
			}
		} else {
			super.compileAssignmentExpr(e, selExpr, c);
		}
	}

	
}
