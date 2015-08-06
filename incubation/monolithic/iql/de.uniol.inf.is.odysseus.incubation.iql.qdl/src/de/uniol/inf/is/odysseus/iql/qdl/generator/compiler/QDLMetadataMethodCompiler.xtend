package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataMethodCompiler
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLMetadataValueSingleID
import de.uniol.inf.is.odysseus.iql.basic.types.ID
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue
import java.util.concurrent.atomic.AtomicInteger

class QDLMetadataMethodCompiler extends AbstractIQLMetadataMethodCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler>{
	
	@Inject
	new(QDLCompilerHelper helper, QDLTypeCompiler typeCompiler) {
		super(helper, typeCompiler)
	}
	
	override String compile(IQLMetadataValue o, String varName, AtomicInteger counter, QDLGeneratorContext context) {
		if(o instanceof QDLMetadataValueSingleID) {
			return compile(o as QDLMetadataValueSingleID, varName, context);
		} else{
			return super.compile(o, varName, counter, context);
		}
	}
	
	def String compile(QDLMetadataValueSingleID o, String varName, QDLGeneratorContext context) {
		context.addImport(ID.canonicalName);
		'''ID «varName» = new ID("«o.value»");'''
	}
	
	
}
