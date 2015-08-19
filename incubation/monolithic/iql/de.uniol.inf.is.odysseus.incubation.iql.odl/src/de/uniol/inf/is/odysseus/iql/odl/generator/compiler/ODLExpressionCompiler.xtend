package de.uniol.inf.is.odysseus.iql.odl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLExpressionCompiler
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeExtensionsFactory
import de.uniol.inf.is.odysseus.iql.odl.exprevaluator.ODLExpressionEvaluator

class ODLExpressionCompiler extends AbstractIQLExpressionCompiler<ODLCompilerHelper,ODLGeneratorContext, ODLTypeCompiler, ODLExpressionEvaluator, ODLTypeUtils, ODLLookUp, ODLTypeExtensionsFactory>{
	
	@Inject
	new(ODLCompilerHelper helper, ODLTypeCompiler typeCompiler, ODLExpressionEvaluator exprEvaluator,ODLTypeUtils typeUtils, ODLLookUp lookUp, ODLTypeExtensionsFactory typeOperatorsFactory) {
		super(helper, typeCompiler, exprEvaluator, typeUtils, lookUp, typeOperatorsFactory)
	}
	
	
}
