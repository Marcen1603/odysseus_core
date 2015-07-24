package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataMethodCompiler
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext

class QDLMetadataMethodCompiler extends AbstractIQLMetadataMethodCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler>{
	
	@Inject
	new(QDLCompilerHelper helper, QDLTypeCompiler typeCompiler) {
		super(helper, typeCompiler)
	}
	
	
}
