package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.BasicIQLExpressionParser
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory
import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp

class BasicIQLExpressionCompiler extends AbstractIQLExpressionCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLTypeCompiler, BasicIQLExpressionParser, BasicIQLTypeFactory, BasicIQLLookUp>{
	
	@Inject
	new(BasicIQLCompilerHelper helper, BasicIQLTypeCompiler typeCompiler, BasicIQLExpressionParser parser, BasicIQLTypeFactory factory, BasicIQLLookUp lookUp) {
		super(helper, typeCompiler, parser, factory, lookUp)
	}
	
	
}
