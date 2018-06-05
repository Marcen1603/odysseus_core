package de.uniol.inf.is.odysseus.iql.odl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLStatementCompiler
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.IODLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.odl.generator.context.IODLGeneratorContext
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils
import de.uniol.inf.is.odysseus.iql.odl.lookup.IODLLookUp
import de.uniol.inf.is.odysseus.iql.odl.exprevaluator.IODLExpressionEvaluator

class ODLStatementCompiler extends AbstractIQLStatementCompiler<IODLCompilerHelper, IODLGeneratorContext, IODLTypeCompiler, IODLExpressionCompiler, IODLTypeUtils, IODLExpressionEvaluator, IODLLookUp> implements IODLStatementCompiler{
	
	@Inject
	new(IODLCompilerHelper helper, IODLExpressionCompiler exprCompiler, IODLTypeCompiler typeCompiler, IODLTypeUtils typeUtils,IODLExpressionEvaluator exprEvaluator, IODLLookUp lookUp) {
		super(helper, exprCompiler, typeCompiler, typeUtils, exprEvaluator, lookUp)
	}

	
	
}
