package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.BasicIQLExpressionEvaluator

class BasicIQLStatementCompiler extends AbstractIQLStatementCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext,BasicIQLTypeCompiler, BasicIQLExpressionCompiler, BasicIQLTypeUtils, BasicIQLExpressionEvaluator, BasicIQLLookUp>{
		
	@Inject
	new(BasicIQLCompilerHelper helper, BasicIQLExpressionCompiler exprCompiler, BasicIQLTypeCompiler typeCompiler, BasicIQLTypeUtils typeUtils, BasicIQLExpressionEvaluator exprEvaluator, BasicIQLLookUp lookUp) {
		super(helper, exprCompiler, typeCompiler, typeUtils, exprEvaluator, lookUp)
	}


	
}
