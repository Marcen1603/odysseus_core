package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.BasicIQLExpressionEvaluator
import de.uniol.inf.is.odysseus.iql.basic.typing.^extension.BasicIQLTypeExtensionsDictionary
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.BasicIQLTypeDictionary

class BasicIQLExpressionCompiler extends AbstractIQLExpressionCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLTypeCompiler, BasicIQLExpressionEvaluator, BasicIQLTypeUtils, BasicIQLLookUp, BasicIQLTypeExtensionsDictionary, BasicIQLTypeDictionary>{
	
	@Inject
	new(BasicIQLCompilerHelper helper, BasicIQLTypeCompiler typeCompiler, BasicIQLExpressionEvaluator exprEvaluator, BasicIQLTypeUtils typeUtils, BasicIQLLookUp lookUp, BasicIQLTypeExtensionsDictionary typeExtensionsDictionary, BasicIQLTypeDictionary typeDictionary) {
		super(helper, typeCompiler, exprEvaluator, typeUtils, lookUp, typeExtensionsDictionary, typeDictionary)
	}
	
	
}
