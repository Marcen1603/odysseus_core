package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.BasicIQLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext

class BasicIQLMetadataMethodCompiler extends AbstractIQLMetadataMethodCompiler<BasicIQLCompilerHelper, BasicIQLGeneratorContext, BasicIQLTypeCompiler>{
	
	@Inject
	new(BasicIQLCompilerHelper helper, BasicIQLTypeCompiler typeCompiler) {
		super(helper, typeCompiler)
	}
	
	
}
