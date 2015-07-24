package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory

class BasicIQLCompiler extends AbstractIQLCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLTypeCompiler, BasicIQLStatementCompiler, BasicIQLTypeFactory>{
	
	@Inject
	new(BasicIQLCompilerHelper helper, BasicIQLTypeCompiler typeCompiler, BasicIQLStatementCompiler stmtCompiler,BasicIQLTypeFactory factory) {
		super(helper, typeCompiler, stmtCompiler, factory)
	}
	
	
}
