package de.uniol.inf.is.odysseus.iql.odl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLExpressionCompiler
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLExpressionParser
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeOperatorsFactory
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils

class ODLExpressionCompiler extends AbstractIQLExpressionCompiler<ODLCompilerHelper,ODLGeneratorContext, ODLTypeCompiler, ODLExpressionParser, ODLTypeUtils, ODLLookUp, ODLTypeOperatorsFactory>{
	
	@Inject
	new(ODLCompilerHelper helper, ODLTypeCompiler typeCompiler, ODLExpressionParser parser,ODLTypeUtils typeUtils, ODLLookUp lookUp, ODLTypeOperatorsFactory typeOperatorsFactory) {
		super(helper, typeCompiler, parser, typeUtils, lookUp, typeOperatorsFactory)
	}
	
	
}
