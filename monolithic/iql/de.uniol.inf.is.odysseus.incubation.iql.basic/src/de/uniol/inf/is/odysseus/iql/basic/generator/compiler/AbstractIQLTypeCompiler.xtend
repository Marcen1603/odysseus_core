package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext
import org.eclipse.xtext.common.types.JvmTypeReference
import java.util.List
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary

abstract class AbstractIQLTypeCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, E extends IIQLExpressionCompiler<G>, F extends IIQLTypeDictionary, U extends IIQLTypeUtils> implements IIQLTypeCompiler<G>{
	protected H helper;
	
	protected F typeDictionary;
	
	protected U typeUtils;
		
	new (H helper, F typeDictionary, U typeUtils) {
		this.helper = helper;
		this.typeDictionary = typeDictionary;
		this.typeUtils = typeUtils;
	}

	
	override compile(JvmTypeReference typeRef, G context, boolean wrapper) {
		var w = wrapper		
		var result =""
		var nodeText = helper.getNodeText(typeRef)		
		if (typeRef === null) {
			return '''void'''
		} 
		if (typeRef instanceof IQLArrayTypeRef) {
			w = true
			if (nodeText !== null) {
				nodeText = nodeText.replace("[","").replace("]","").trim;				
			}
			var arrayTypeRef = typeRef as IQLArrayTypeRef
			var arrayType = arrayTypeRef.type as IQLArrayType
			
			if (arrayType.dimensions.size()>0) {
				for (i : 0 ..< arrayType.dimensions.size) {
					result += '''«List.simpleName»<'''
				}
			}
		}	
		
		result += compile(typeRef, context, nodeText,w)
		
		if (typeRef instanceof IQLArrayTypeRef) {
			var arrayTypeRef = typeRef as IQLArrayTypeRef
			var arrayType = arrayTypeRef.type as IQLArrayType
			if (arrayType.dimensions.size()>0) {
				for (i : 0 ..< arrayType.dimensions.size) {
					result += '''>'''
				}
				context.addImport(List.canonicalName)
			}
		}

		return result;
	}
	
	def String compile(JvmTypeReference typeRef, G context, String nodeText, boolean wrapper) {
		var innerType = typeUtils.getInnerType(typeRef, false)
		if (typeDictionary.isImportNeeded(innerType, nodeText)) {
			context.addImport(typeDictionary.getImportName(innerType));
		} 
		'''«typeDictionary.getSimpleName(innerType, nodeText, wrapper, false)»'''		
	}
	
	
}
