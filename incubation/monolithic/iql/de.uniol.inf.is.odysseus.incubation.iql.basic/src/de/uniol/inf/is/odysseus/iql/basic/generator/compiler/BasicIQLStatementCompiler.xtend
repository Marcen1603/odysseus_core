package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.BasicIQLExpressionParser
import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp

class BasicIQLStatementCompiler extends AbstractIQLStatementCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext,BasicIQLTypeCompiler, BasicIQLExpressionCompiler, BasicIQLTypeFactory, BasicIQLExpressionParser, BasicIQLLookUp>{
		
	@Inject
	new(BasicIQLCompilerHelper helper, BasicIQLExpressionCompiler exprCompiler, BasicIQLTypeCompiler typeCompiler, BasicIQLTypeFactory factory, BasicIQLExpressionParser exprParser, BasicIQLLookUp lookUp) {
		super(helper, exprCompiler, typeCompiler, factory, exprParser, lookUp)
	}


	
}
