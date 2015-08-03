package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext
import org.eclipse.xtext.common.types.JvmTypeReference
import java.util.List
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils

abstract class AbstractIQLTypeCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, E extends IIQLExpressionCompiler<G>, F extends IIQLTypeFactory, U extends IIQLTypeUtils> implements IIQLTypeCompiler<G>{
	protected H helper;
	
	protected F typeFactory;
	
	protected U typeUtils;
		
	new (H helper, F typeFactory, U typeUtils) {
		this.helper = helper;
		this.typeFactory = typeFactory;
		this.typeUtils = typeUtils;
	}

	
	override compile(JvmTypeReference typeRef, G context, boolean wrapper) {
		var w = wrapper		
		var result =""
		var nodeText = helper.getNodeText(typeRef)		
		if (typeRef == null) {
			return '''void'''
		} 
		if (typeRef instanceof IQLArrayTypeRef) {
			w = true
			if (nodeText != null) {
				nodeText = nodeText.replace("[","").replace("]","").trim;				
			}
			var arrayType = typeRef as IQLArrayTypeRef
			if (arrayType.type.dimensions.size()>0) {
				for (i : 0 ..< arrayType.type.dimensions.size) {
					result += '''«List.simpleName»<'''
				}
			}
		}	
		
		result += compile(typeRef, context, nodeText,w)
		
		if (typeRef instanceof IQLArrayTypeRef) {
			var arrayType = typeRef as IQLArrayTypeRef
			if (arrayType.type.dimensions.size()>0) {
				for (i : 0 ..< arrayType.type.dimensions.size) {
					result += '''>'''
				}
				context.addImport(List.canonicalName)
			}
		}

		return result;
	}
	
	def String compile(JvmTypeReference typeRef, G context, String nodeText, boolean wrapper) {
		var innerType = typeUtils.getInnerType(typeRef, false)
		if (typeFactory.isImportNeeded(innerType, nodeText)) {
			context.addImport(typeFactory.getImportName(innerType));
		} 
		'''«typeFactory.getSimpleName(innerType, nodeText, wrapper, false)»'''		
	}
	
	
}
