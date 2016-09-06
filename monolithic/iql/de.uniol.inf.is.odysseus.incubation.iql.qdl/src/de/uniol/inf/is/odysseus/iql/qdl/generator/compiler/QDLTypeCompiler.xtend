package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLTypeCompiler
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.IQDLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.IQDLGeneratorContext
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary

class QDLTypeCompiler extends AbstractIQLTypeCompiler<IQDLCompilerHelper, IQDLGeneratorContext, IQDLExpressionCompiler, IQDLTypeDictionary, IQDLTypeUtils> implements IQDLTypeCompiler{
	@Inject	
	new(IQDLCompilerHelper helper, IQDLTypeDictionary typeDictionary, IQDLTypeUtils typeUtils) {
		super(helper, typeDictionary, typeUtils)
	}
	

	
}
