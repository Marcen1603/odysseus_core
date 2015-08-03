package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataAnnotationCompiler
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils

class QDLMetadataAnnotationCompiler extends AbstractIQLMetadataAnnotationCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler, QDLTypeUtils>{
	
	@Inject
	new(QDLCompilerHelper helper, QDLTypeCompiler typeCompiler,QDLTypeUtils typeUtils) {
		super(helper, typeCompiler, typeUtils)
	}
	
	
}
