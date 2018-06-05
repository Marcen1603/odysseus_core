package de.uniol.inf.is.odysseus.iql.odl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLTypeCompiler
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.IODLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.odl.generator.context.IODLGeneratorContext
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary

class ODLTypeCompiler extends AbstractIQLTypeCompiler<IODLCompilerHelper, IODLGeneratorContext, IODLExpressionCompiler, IODLTypeDictionary,  IODLTypeUtils> implements IODLTypeCompiler{
	
	@Inject
	new(IODLCompilerHelper helper,IODLTypeDictionary typeDictionary,  IODLTypeUtils typeUtils) {
		super(helper, typeDictionary, typeUtils)
	}
	
//	override String compile(JvmTypeReference typeRef, ODLGeneratorContext context, String nodeText, boolean wrapper) {
//		var result = super.compile(typeRef, context, nodeText, wrapper)
//		
//		var parameter = EcoreUtil2.getContainerOfType(typeRef, ODLParameter)
//		
//		if (parameter != null && !typeUtils.isArray(typeRef) && typeUtils.isList(typeRef)) {
//			var listElement = typeFactory.getListElementType(parameter)
//			
//			result+="<"+compile(listElement,context, true)+">"
//		} else if (parameter != null && !typeUtils.isArray(typeRef) && typeUtils.isMap(typeRef)) {
//			var key = typeFactory.getMapKeyType(parameter)
//			var value = typeFactory.getMapValueType(parameter)
//			
//			result+="<"+compile(key,context, true)+","+compile(value,context, true)+">"
//		}	
//		return result;
//	}
	
	
}
