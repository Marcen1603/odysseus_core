package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLStatementCompiler
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization
import org.eclipse.xtext.common.types.JvmTypeReference
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLExpressionParser
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils

class QDLStatementCompiler extends AbstractIQLStatementCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler, QDLExpressionCompiler, QDLTypeUtils, QDLExpressionParser, QDLLookUp>{
	
	@Inject
	new(QDLCompilerHelper helper, QDLExpressionCompiler exprCompiler, QDLTypeCompiler typeCompiler, QDLTypeUtils typeUtils, QDLExpressionParser exprParser,  QDLLookUp lookUp) {
		super(helper, exprCompiler, typeCompiler, typeUtils, exprParser, lookUp)
	}
	
	override String compile(IQLVariableInitialization init, JvmTypeReference typeRef, QDLGeneratorContext context) {
		if (helper.isOperator(typeRef)) {
			var opName = helper.getLogicalOperatorName(typeRef)
			if (init.argsMap != null && init.argsMap.elements.size > 0) {
				var constructor = lookUp.findConstructor(typeRef, init.argsList.elements.size)
				var args = init.argsList.elements.size > 0
				if (constructor != null) {
					'''getOperator«typeUtils.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, false)»<«opName»>(new «opName»()«IF args», «ENDIF»«exprCompiler.compile(init.argsList,constructor.parameters, context)»), operators, «exprCompiler.compile(init.argsMap, typeRef, context)»)'''
				} else {
					'''getOperator«typeUtils.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, false)»<«opName»>(new «opName»()«IF args», «ENDIF»«exprCompiler.compile(init.argsList, context)»), operators, «exprCompiler.compile(init.argsMap, typeRef, context)»)'''
				}
			} else if (init.argsList != null) {
				var constructor = lookUp.findConstructor(typeRef, init.argsList.elements.size)
				var args = init.argsList.elements.size > 0				
				if (constructor != null) {
					'''getOperator«typeUtils.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, false)»<«opName»>(new «opName»()«IF args», «ENDIF»«exprCompiler.compile(init.argsList,constructor.parameters, context)»), operators)'''
				} else {
					'''getOperator«typeUtils.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, false)»<«opName»>(new «opName»()«IF args», «ENDIF»«exprCompiler.compile(init.argsList, context)»), operators)'''
				}
			} else {
				super.compile(init, typeRef, context);
			}
		} else if (helper.isSource(typeRef)) {
			'''getSource("«typeUtils.getShortName(typeRef, false)»")'''
		} else {
			super.compile(init, typeRef, context);
		}
	}
	
}
