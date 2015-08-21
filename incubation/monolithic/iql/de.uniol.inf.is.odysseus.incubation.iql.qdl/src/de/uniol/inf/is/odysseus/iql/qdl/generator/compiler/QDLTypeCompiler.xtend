package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLTypeCompiler
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.IQDLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.IQDLGeneratorContext
import de.uniol.inf.is.odysseus.iql.qdl.typing.factory.IQDLTypeFactory
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils

class QDLTypeCompiler extends AbstractIQLTypeCompiler<IQDLCompilerHelper, IQDLGeneratorContext, IQDLExpressionCompiler, IQDLTypeFactory, IQDLTypeUtils> implements IQDLTypeCompiler{
	@Inject	
	new(IQDLCompilerHelper helper, IQDLTypeFactory typeFactory, IQDLTypeUtils typeUtils) {
		super(helper, typeFactory, typeUtils)
	}
	

	
}
