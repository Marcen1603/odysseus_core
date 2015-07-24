package de.uniol.inf.is.odysseus.iql.odl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataMethodCompiler
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext

class ODLMetadataMethodCompiler extends AbstractIQLMetadataMethodCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLTypeCompiler>{
	
	@Inject
	new(ODLCompilerHelper helper, ODLTypeCompiler typeCompiler) {
		super(helper, typeCompiler)
	}
	
	
}
