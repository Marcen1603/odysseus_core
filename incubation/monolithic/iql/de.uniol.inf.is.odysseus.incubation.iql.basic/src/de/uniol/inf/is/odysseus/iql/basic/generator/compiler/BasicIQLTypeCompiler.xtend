package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory

class BasicIQLTypeCompiler extends AbstractIQLTypeCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLExpressionCompiler, BasicIQLTypeFactory>{
	
	new(BasicIQLCompilerHelper helper, BasicIQLTypeFactory typeFactory) {
		super(helper, typeFactory)
	}
	

}
