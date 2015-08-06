package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataAnnotationCompiler
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLMetadataValueSingleID
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue

class QDLMetadataAnnotationCompiler extends AbstractIQLMetadataAnnotationCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler, QDLTypeUtils>{
	
	@Inject
	new(QDLCompilerHelper helper, QDLTypeCompiler typeCompiler,QDLTypeUtils typeUtils) {
		super(helper, typeCompiler, typeUtils)
	}
	
	override String compile(IQLMetadataValue o, QDLGeneratorContext c) {
		if(o instanceof QDLMetadataValueSingleID) {
			return compile(o as QDLMetadataValueSingleID, c);
		} else {
			super.compile(o,c)
		}
	}
	
	def String compile(QDLMetadataValueSingleID o, QDLGeneratorContext c) {
		'''"«o.value»"'''		
	}
	
}
