package de.uniol.inf.is.odysseus.iql.odl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLTypeCompiler
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory
import org.eclipse.xtext.common.types.JvmTypeReference
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter
import org.eclipse.xtext.EcoreUtil2

class ODLTypeCompiler extends AbstractIQLTypeCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLExpressionCompiler, ODLTypeFactory>{
	
	@Inject
	new(ODLCompilerHelper helper, ODLTypeFactory typeFactory) {
		super(helper,typeFactory)
	}
	
	override String compile(JvmTypeReference typeRef, ODLGeneratorContext context, String nodeText, boolean wrapper) {
		var result = super.compile(typeRef, context, nodeText, wrapper)
		
		var parameter = EcoreUtil2.getContainerOfType(typeRef, ODLParameter)
		
		if (parameter != null && !typeFactory.isArray(typeRef) && typeFactory.isList(typeRef)) {
			var listElement = typeFactory.getListElementType(parameter)
			
			result+="<"+compile(listElement,context, true)+">"
		} else if (parameter != null && !typeFactory.isArray(typeRef) && typeFactory.isMap(typeRef)) {
			var key = typeFactory.getMapKeyType(parameter)
			var value = typeFactory.getMapValueType(parameter)
			
			result+="<"+compile(key,context, true)+","+compile(value,context, true)+">"
		}	
		return result;
	}
	
	
}
