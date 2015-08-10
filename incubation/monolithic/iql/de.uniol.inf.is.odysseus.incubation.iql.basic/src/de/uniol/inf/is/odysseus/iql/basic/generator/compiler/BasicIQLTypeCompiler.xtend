package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils
import javax.inject.Inject

class BasicIQLTypeCompiler extends AbstractIQLTypeCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLExpressionCompiler, BasicIQLTypeFactory, BasicIQLTypeUtils>{
	
	@Inject
	new(BasicIQLCompilerHelper helper, BasicIQLTypeFactory typeFactory, BasicIQLTypeUtils typeUtils) {
		super(helper, typeFactory, typeUtils)
	}
	

}
