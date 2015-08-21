package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataAnnotationCompiler
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLMetadataValueSingleID
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.IQDLGeneratorContext
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.IQDLCompilerHelper
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils

class QDLMetadataAnnotationCompiler extends AbstractIQLMetadataAnnotationCompiler<IQDLCompilerHelper, IQDLGeneratorContext, IQDLTypeCompiler, IQDLTypeUtils> implements IQDLMetadataAnnotationCompiler{
	
	@Inject
	new(IQDLCompilerHelper helper, IQDLTypeCompiler typeCompiler,IQDLTypeUtils typeUtils) {
		super(helper, typeCompiler, typeUtils)
	}
	
	override String compile(IQLMetadataValue o, IQDLGeneratorContext c) {
		if(o instanceof QDLMetadataValueSingleID) {
			return compile(o as QDLMetadataValueSingleID, c);
		} else {
			super.compile(o,c)
		}
	}
	
	def String compile(QDLMetadataValueSingleID o, IQDLGeneratorContext c) {
		'''"«o.value»"'''		
	}
	
}
