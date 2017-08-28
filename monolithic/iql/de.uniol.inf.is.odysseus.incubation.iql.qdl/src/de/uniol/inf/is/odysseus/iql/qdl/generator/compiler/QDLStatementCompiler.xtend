package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLStatementCompiler
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.IQDLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization
import org.eclipse.xtext.common.types.JvmTypeReference
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp
import de.uniol.inf.is.odysseus.iql.qdl.exprevaluator.IQDLExpressionEvaluator
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.IQDLGeneratorContext
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils

class QDLStatementCompiler extends AbstractIQLStatementCompiler<IQDLCompilerHelper, IQDLGeneratorContext, IQDLTypeCompiler, IQDLExpressionCompiler, IQDLTypeUtils, IQDLExpressionEvaluator, IQDLLookUp> implements IQDLStatementCompiler{
	
	@Inject
	new(IQDLCompilerHelper helper, IQDLExpressionCompiler exprCompiler, IQDLTypeCompiler typeCompiler, IQDLTypeUtils typeUtils, IQDLExpressionEvaluator exprEvaluator,  IQDLLookUp lookUp) {
		super(helper, exprCompiler, typeCompiler, typeUtils, exprEvaluator, lookUp)
	}
	
	override String compile(IQLVariableInitialization init, JvmTypeReference typeRef, IQDLGeneratorContext context) {
		if (helper.isOperator(typeRef)) {
			if (init.argsList !== null && init.argsMap !== null && init.argsMap.elements.size > 0) {
				var constructor = lookUp.findPublicConstructor(typeRef, init.argsList.elements)
				var args = init.argsList.elements.size > 0
				if (constructor !== null) {					
					'''getOperator«typeUtils.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, false)»("«typeUtils.getShortName(typeRef, false)»"«IF args», «ENDIF»«exprCompiler.compile(init.argsList,constructor.parameters, context)»), operators, «exprCompiler.compile(init.argsMap, typeRef, context)»)'''
				} else {
					'''getOperator«typeUtils.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, false)»("«typeUtils.getShortName(typeRef, false)»"«IF args», «ENDIF»«exprCompiler.compile(init.argsList, context)»), operators, «exprCompiler.compile(init.argsMap, typeRef, context)»)'''
				}
			} else if (init.argsMap !== null && init.argsMap.elements.size > 0) {
				'''getOperator«typeUtils.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, false)»("«typeUtils.getShortName(typeRef, false)»"), operators, «exprCompiler.compile(init.argsMap, typeRef, context)»)'''
			} else if (init.argsList !== null) {
				var constructor = lookUp.findPublicConstructor(typeRef, init.argsList.elements)
				var args = init.argsList.elements.size > 0				
				if (constructor !== null) {
					'''getOperator«typeUtils.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, false)»("«typeUtils.getShortName(typeRef, false)»"«IF args», «ENDIF»«exprCompiler.compile(init.argsList,constructor.parameters, context)»), operators)'''
				} else {
					'''getOperator«typeUtils.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, false)»("«typeUtils.getShortName(typeRef, false)»"«IF args», «ENDIF»«exprCompiler.compile(init.argsList, context)»), operators)'''
				}
			} else {
				super.compile(init, typeRef, context);
			}
		} else if (helper.isSource(typeRef)) {
			'''new «typeCompiler.compile(typeRef, context, false)»("«typeUtils.getShortName(typeRef, false)»")'''
		} else {
			super.compile(init, typeRef, context);
		}
	}
	
}
