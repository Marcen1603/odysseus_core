package de.uniol.inf.is.odysseus.iql.odl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLStatementCompiler
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLExpressionParser
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp

class ODLStatementCompiler extends AbstractIQLStatementCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLTypeCompiler, ODLExpressionCompiler, ODLTypeFactory, ODLExpressionParser, ODLLookUp>{
	
	@Inject
	new(ODLCompilerHelper helper, ODLExpressionCompiler exprCompiler, ODLTypeCompiler typeCompiler, ODLTypeFactory factory,ODLExpressionParser exprParser, ODLLookUp lookUp) {
		super(helper, exprCompiler, typeCompiler, factory, exprParser, lookUp)
	}

	
	
}
