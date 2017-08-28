package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleDouble
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleString
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleBoolean
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleNull
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleTypeRef
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueList
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMap
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMapElement
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleInt
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils

abstract class AbstractIQLMetadataAnnotationCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>, U extends IIQLTypeUtils> implements IIQLMetadataAnnotationCompiler<G>{
	protected H helper;
	protected T typeCompiler;
	protected U typeUtils;
	
	new (H helper, T typeCompiler, U typeUtils) {
		this.helper = helper;
		this.typeCompiler = typeCompiler;	
		this.typeUtils = typeUtils;			
	}
	

	def String compile(IQLMetadataList o, G c) {
		'''
		«o.elements.map[e | compile(e, c)].join(", ")»
		'''		
	}
	
	def String compile(IQLMetadata o, G c) {
		'''
		«o.name» = «compile(o.value, c)»
		'''		
	}
	
	def String compile(IQLMetadataValue o, G c) {
		if(o instanceof IQLMetadataValueSingleInt) {
			return compile(o as IQLMetadataValueSingleInt, c);
		} else if(o instanceof IQLMetadataValueSingleDouble) {
			return compile(o as IQLMetadataValueSingleDouble, c);
		} else if(o instanceof IQLMetadataValueSingleString) {
			return compile(o as IQLMetadataValueSingleString, c);
		} else if(o instanceof IQLMetadataValueSingleBoolean) {
			return compile(o as IQLMetadataValueSingleBoolean, c);
		} else if(o instanceof IQLMetadataValueSingleNull) {
			return compile(o as IQLMetadataValueSingleNull, c);
		} else if(o instanceof IQLMetadataValueSingleTypeRef) {
			return compile(o as IQLMetadataValueSingleTypeRef, c);
		}else if(o instanceof IQLMetadataValueList) {
			return compile(o as IQLMetadataValueList, c);
		} else if(o instanceof IQLMetadataValueMap) {
			return compile(o as IQLMetadataValueMap, c);
		} 
		return "";	
	}

	def String compile(IQLMetadataValueSingleInt o, G c) {
		'''«o.value»'''		
	}
	
	
	def String compile(IQLMetadataValueSingleDouble o, G c) {
		'''«o.value»'''		
	}
	
	def String compile(IQLMetadataValueSingleString o, G c) {
		if (c.expectedTypeRef !== null) {
			if (typeUtils.isCharacter(c.expectedTypeRef)) {
				return "'"+o.value+"'"				
			} else {
				'''"«o.value»"'''
			}
		} else {
			'''"«o.value»"'''
		}
	}
	
	def String compile(IQLMetadataValueSingleBoolean o, G c) {
		'''«o.value»'''		
	}
	


	def String compile(IQLMetadataValueSingleNull o, G c) {
		'''null'''		
	}
	
	def String compile(IQLMetadataValueSingleTypeRef o, G c) {
		'''«typeCompiler.compile(o.value, c, true)».class'''		
	}
	

	def String compile(IQLMetadataValueList o, G c) {
		'''{«o.elements.map[e | compile(e, c)].join(", ")»}'''		
	}
	
	def String compile(IQLMetadataValueMap o, G c) {
		'''(«o.elements.map[e | compile(e, c)].join(", ")»)'''		
	}
	
	def String compile(IQLMetadataValueMapElement o, G c) {
		'''«compile(o.key, c)» = «compile(o.value, c)»'''		
	}
	
}
