package de.uniol.inf.is.odysseus.iql.odl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLExpressionCompiler
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLExpressionParser
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp

class ODLExpressionCompiler extends AbstractIQLExpressionCompiler<ODLCompilerHelper,ODLGeneratorContext, ODLTypeCompiler, ODLExpressionParser, ODLTypeFactory, ODLLookUp>{
	
	@Inject
	new(ODLCompilerHelper helper, ODLTypeCompiler typeCompiler, ODLExpressionParser parser,ODLTypeFactory factory, ODLLookUp lookUp) {
		super(helper, typeCompiler, parser, factory, lookUp)
	}
	
	
}
