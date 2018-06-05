package de.uniol.inf.is.odysseus.iql.odl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataMethodCompiler
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.IODLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.odl.generator.context.IODLGeneratorContext

class ODLMetadataMethodCompiler extends AbstractIQLMetadataMethodCompiler<IODLCompilerHelper, IODLGeneratorContext, IODLTypeCompiler> implements IODLMetadataMethodCompiler{
	
	@Inject
	new(IODLCompilerHelper helper, IODLTypeCompiler typeCompiler) {
		super(helper, typeCompiler)
	}
	
	
}
