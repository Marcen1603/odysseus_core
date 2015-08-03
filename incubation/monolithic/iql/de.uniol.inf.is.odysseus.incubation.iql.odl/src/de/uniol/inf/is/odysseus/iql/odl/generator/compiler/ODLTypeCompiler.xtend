package de.uniol.inf.is.odysseus.iql.odl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLTypeCompiler
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory

class ODLTypeCompiler extends AbstractIQLTypeCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLExpressionCompiler, ODLTypeFactory,  ODLTypeUtils>{
	
	@Inject
	new(ODLCompilerHelper helper,ODLTypeFactory typeFactory,  ODLTypeUtils typeUtils) {
		super(helper, typeFactory, typeUtils)
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
