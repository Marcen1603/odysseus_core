package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLExpressionCompiler
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression
import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression
import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression
import de.uniol.inf.is.odysseus.iql.qdl.types.query.QDLSubscribableWithPort
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionNew
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLExpressionParser
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttributeSelection
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodSelection
import org.eclipse.xtext.common.types.JvmTypeReference
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeOperatorsFactory

class QDLExpressionCompiler extends AbstractIQLExpressionCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler, QDLExpressionParser, QDLTypeFactory, QDLLookUp, QDLTypeOperatorsFactory>{
		
	@Inject
	new(QDLCompilerHelper helper, QDLTypeCompiler typeCompiler, QDLExpressionParser exprParser, QDLTypeFactory factory, QDLLookUp lookUp, QDLTypeOperatorsFactory typeOperatorsFactory) {
		super(helper, typeCompiler, exprParser, factory, lookUp, typeOperatorsFactory)
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
	
	override String compile(IQLTerminalExpressionNew e, QDLGeneratorContext context) {		
		if (helper.isOperator(e.ref)) {
			var opName = helper.getLogicalOperatorName(e.ref)
			if (e.argsMap != null && e.argsMap.elements.size > 0) {	
				var constructor = lookUp.findConstructor(e.ref, e.argsList.elements.size)
				var args = e.argsList.elements.size > 0
				if (constructor != null) {
					'''getOperator«factory.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, context, false)»<«opName»>(new «opName»()«IF args», «ENDIF»«compile(e.argsList,constructor.parameters, context)»), operators,  «compile(e.argsMap,e.ref, context)»)'''
				} else {
					'''getOperator«factory.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, context, false)»<«opName»>(new «opName»()«IF args», «ENDIF»«compile(e.argsList, context)»), operators,  «compile(e.argsMap,e.ref, context)»)'''
				}		
			} else if (e.argsList != null) {
				var constructor = lookUp.findConstructor(e.ref, e.argsList.elements.size)
				var args = e.argsList.elements.size > 0
				if (constructor != null) {
					'''getOperator«factory.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, context, false)»<«opName»>(new «opName»()«IF args», «ENDIF»«compile(e.argsList,constructor.parameters, context)»), operators)'''
				} else {
					'''getOperator«factory.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, context, false)»<«opName»>(new «opName»()«IF args», «ENDIF»«compile(e.argsList, context)»)>, operators)'''
				}	
			} else {
				'''getOperator«factory.getShortName(e.ref, false)»«e.ref.hashCode»(new «typeCompiler.compile(e.ref, context, false)»<«opName»>(new «opName»()), operators)'''
			}
		} else if (helper.isSource(e.ref)) {
				'''getSource("«factory.getShortName(e.ref, false)»")'''
		} else {
			super.compile(e, context);
		}
	}
	
	
	override String compile(IQLAttributeSelection a, JvmTypeReference left, IQLMemberSelectionExpression expr, QDLGeneratorContext context) {	
		if (helper.isSource(left)){
			var name = a.^var.simpleName;
			if (helper.isSourceAttribute(left, name)) {
				'''"«name»"'''
			} else {
				super.compile(a, left, expr, context);
			}
		} else if (helper.isOperator(left)){
			var pName = a.^var.simpleName;
			if (helper.isParameter(pName, left)) {
				if (expr.eContainer instanceof IQLAssignmentExpression) {
					'''«compile(expr.leftOperand, context)».setParameter("«pName»",'''
				} else {
					'''«compile(expr.leftOperand, context)».getParameter("«pName»")'''
				}
			} else {
				super.compile(a, left, expr, context);
			}
		} else {
			super.compile(a, left, expr, context);
		}
	}
	
	override String compile(IQLMethodSelection m,JvmTypeReference left, IQLMemberSelectionExpression expr, QDLGeneratorContext context) {	
		if (helper.isOperator(left)){
			if (helper.isParameterGetter(m, left)) {
				var name = helper.getParameterOfGetter(m)
				'''«compile(expr.leftOperand, context)».getParameter("«name»")'''
			} else if (helper.isParameterSetter(m , left)) {
				var name = helper.getParameterOfSetter(m)
				'''«compile(expr.leftOperand, context)».setParameter("«name»",«compile(m.args, m.method.parameters, context)»)'''
			} else {
				super.compile(m, left, expr, context);
			}
		} else {
			super.compile(m, left, expr, context);
		}
	}
	
	override String compile(IQLAssignmentExpression expr, QDLGeneratorContext context) {	
		if (expr.leftOperand instanceof IQLMemberSelectionExpression) {
			var leftType = exprParser.getType(expr.leftOperand)
			if (!leftType.^null){
				context.expectedTypeRef = leftType.ref
				exprParser.getType(expr.rightOperand, leftType.ref)
			}
			var selExpr = expr.leftOperand as IQLMemberSelectionExpression;
			if (selExpr.rightOperand instanceof IQLAttributeSelection) {
				var attr = selExpr.rightOperand as IQLAttributeSelection
				var left = exprParser.getType(selExpr.leftOperand);
				if (!left.isNull && helper.isOperator(left.ref) && helper.isParameter(attr.^var.simpleName, left.ref)) {
					'''«compile(expr.leftOperand, context)»«compile(expr.rightOperand, context)»)'''
				} else {
					super.compile(expr, context);
				}
			} else {
				super.compile(expr, context);				
			}		
		} else {
			super.compile(expr, context);				
		}		
	}	
	
}
