package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLTypeCompiler
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils

class QDLTypeCompiler extends AbstractIQLTypeCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLExpressionCompiler, QDLTypeFactory, QDLTypeUtils>{
	@Inject	
	new(QDLCompilerHelper helper, QDLTypeFactory typeFactory, QDLTypeUtils typeUtils) {
		super(helper, typeFactory, typeUtils)
	}
	

	
}
