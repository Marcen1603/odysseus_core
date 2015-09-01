package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.BasicIQLTypeDictionary

class BasicIQLTypeCompiler extends AbstractIQLTypeCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLExpressionCompiler, BasicIQLTypeDictionary, BasicIQLTypeUtils>{
	
	@Inject
	new(BasicIQLCompilerHelper helper, BasicIQLTypeDictionary typeDictionary, BasicIQLTypeUtils typeUtils) {
		super(helper, typeDictionary, typeUtils)
	}
	

}
