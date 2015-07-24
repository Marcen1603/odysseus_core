package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataAnnotationCompiler
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext

class QDLMetadataAnnotationCompiler extends AbstractIQLMetadataAnnotationCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler>{
	
	@Inject
	new(QDLCompilerHelper helper, QDLTypeCompiler typeCompiler) {
		super(helper, typeCompiler)
	}
	
	
}
