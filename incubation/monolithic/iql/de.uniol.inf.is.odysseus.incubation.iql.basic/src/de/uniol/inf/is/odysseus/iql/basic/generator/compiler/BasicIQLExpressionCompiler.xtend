package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils
import de.uniol.inf.is.odysseus.iql.basic.typing.^extension.BasicIQLTypeExtensionsFactory
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.BasicIQLExpressionEvaluator

class BasicIQLExpressionCompiler extends AbstractIQLExpressionCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLTypeCompiler, BasicIQLExpressionEvaluator, BasicIQLTypeUtils, BasicIQLLookUp, BasicIQLTypeExtensionsFactory>{
	
	@Inject
	new(BasicIQLCompilerHelper helper, BasicIQLTypeCompiler typeCompiler, BasicIQLExpressionEvaluator exprEvaluator, BasicIQLTypeUtils typeUtils, BasicIQLLookUp lookUp, BasicIQLTypeExtensionsFactory typeExtensionsFactory) {
		super(helper, typeCompiler, exprEvaluator, typeUtils, lookUp, typeExtensionsFactory)
	}
	
	
}
