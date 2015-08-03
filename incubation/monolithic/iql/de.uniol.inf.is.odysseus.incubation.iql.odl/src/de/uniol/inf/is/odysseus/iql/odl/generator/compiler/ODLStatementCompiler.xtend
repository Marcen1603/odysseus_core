package de.uniol.inf.is.odysseus.iql.odl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLStatementCompiler
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLExpressionParser
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils

class ODLStatementCompiler extends AbstractIQLStatementCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLTypeCompiler, ODLExpressionCompiler, ODLTypeUtils, ODLExpressionParser, ODLLookUp>{
	
	@Inject
	new(ODLCompilerHelper helper, ODLExpressionCompiler exprCompiler, ODLTypeCompiler typeCompiler, ODLTypeUtils typeUtils,ODLExpressionParser exprParser, ODLLookUp lookUp) {
		super(helper, exprCompiler, typeCompiler, typeUtils, exprParser, lookUp)
	}

	
	
}
