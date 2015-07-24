package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLStatementCompiler
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization
import org.eclipse.xtext.common.types.JvmTypeReference
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLExpressionParser

class QDLStatementCompiler extends AbstractIQLStatementCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler, QDLExpressionCompiler, QDLTypeFactory, QDLExpressionParser, QDLLookUp>{
	
	@Inject
	new(QDLCompilerHelper helper, QDLExpressionCompiler exprCompiler, QDLTypeCompiler typeCompiler, QDLTypeFactory factory, QDLExpressionParser exprParser,  QDLLookUp lookUp) {
		super(helper, exprCompiler, typeCompiler, factory, exprParser, lookUp)
	}
	
	override String compile(IQLVariableInitialization init, JvmTypeReference typeRef, QDLGeneratorContext context) {
		if (helper.isOperator(typeRef)) {
			if (init.argsMap != null && init.argsMap.elements.size > 0) {
				var constructor = lookUp.findConstructor(typeRef, init.argsList)
				if (constructor != null) {
					'''getOperator«factory.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, false)»(«exprCompiler.compile(init.argsList,constructor.parameters, context)»), operators, «exprCompiler.compile(init.argsMap, typeRef, context)»)'''
				} else {
					'''getOperator«factory.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, false)»(«exprCompiler.compile(init.argsList, context)»), operators, «exprCompiler.compile(init.argsMap, typeRef, context)»)'''
				}
			} else if (init.argsList != null) {
				var constructor = lookUp.findConstructor(typeRef, init.argsList)
				if (constructor != null) {
					'''getOperator«factory.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, false)»(«exprCompiler.compile(init.argsList,constructor.parameters, context)»), operators)'''
				} else {
					'''getOperator«factory.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, false)»(«exprCompiler.compile(init.argsList, context)»), operators)'''
				}
			} else {
			super.compile(init, typeRef, context);
			}
		} else if (helper.isSource(typeRef)) {
			'''getSource("«factory.getShortName(typeRef, false)»")'''
		} else {
			super.compile(init, typeRef, context);
		}
	}
	
}
