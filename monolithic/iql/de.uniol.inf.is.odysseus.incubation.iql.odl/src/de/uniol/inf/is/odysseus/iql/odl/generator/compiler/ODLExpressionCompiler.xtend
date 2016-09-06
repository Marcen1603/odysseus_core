package de.uniol.inf.is.odysseus.iql.odl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLExpressionCompiler
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.IODLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.odl.generator.context.IODLGeneratorContext
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils
import de.uniol.inf.is.odysseus.iql.odl.lookup.IODLLookUp
import de.uniol.inf.is.odysseus.iql.odl.exprevaluator.IODLExpressionEvaluator
import de.uniol.inf.is.odysseus.iql.odl.typing.typeextension.IODLTypeExtensionsDictionary
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary

class ODLExpressionCompiler extends AbstractIQLExpressionCompiler<IODLCompilerHelper,IODLGeneratorContext, IODLTypeCompiler, IODLExpressionEvaluator, IODLTypeUtils, IODLLookUp, IODLTypeExtensionsDictionary, IODLTypeDictionary> implements IODLExpressionCompiler{
	
	@Inject
	new(IODLCompilerHelper helper, IODLTypeCompiler typeCompiler, IODLExpressionEvaluator exprEvaluator,IODLTypeUtils typeUtils, IODLLookUp lookUp, IODLTypeExtensionsDictionary typeExtensionsDictionary, IODLTypeDictionary typeDictionary) {
		super(helper, typeCompiler, exprEvaluator, typeUtils, lookUp, typeExtensionsDictionary, typeDictionary)
	}
	
	
}
