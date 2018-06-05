package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleDouble
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleString
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleBoolean
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleNull
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueList
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMap
import java.util.concurrent.atomic.AtomicInteger
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleTypeRef
import java.util.List
import java.util.ArrayList
import java.util.Map
import java.util.HashMap
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleInt
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils
import javax.inject.Inject

abstract class AbstractIQLMetadataMethodCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>> implements IIQLMetadataMethodCompiler<G>{
	
	public static final String ADD_METADATA_METHOD_NAME = "addMetadata"
	public static final String METADATA_VALUE_VAR_NAME = "metadataValue"
		
	
	protected H helper;
	
	protected T typeCompiler;
	
	@Inject
	protected IIQLTypeUtils typeUtils;
	
	new (H helper, T typeCompiler) {
		this.helper = helper;
		this.typeCompiler = typeCompiler;
	}
	

	override String compile(IQLMetadataList o, G context) {
		var counter = new AtomicInteger(0)		
		'''
		public void «CREATE_METADATA_METHOD_NAME»() {
			«FOR m : o.elements»
			«var varName = METADATA_VALUE_VAR_NAME+counter.incrementAndGet()»
			«IF m.value !== null» 
			«compile(m.getValue(), varName, counter, context)»
			«ADD_METADATA_METHOD_NAME»("«m.name»",«varName»);
			«ELSE»
			«ADD_METADATA_METHOD_NAME»("«m.name»",null);
			«ENDIF»
			«ENDFOR»
			
		}
		'''		
	}	
	
	def String compile(IQLMetadataValue o, String varName, AtomicInteger counter, G context) {
		if(o instanceof IQLMetadataValueSingleInt) {
			return compile(o as IQLMetadataValueSingleInt, varName, context);
		} else if(o instanceof IQLMetadataValueSingleDouble) {
			return compile(o as IQLMetadataValueSingleDouble, varName, context);
		} else if(o instanceof IQLMetadataValueSingleString) {
			return compile(o as IQLMetadataValueSingleString, varName, context);
		} else if(o instanceof IQLMetadataValueSingleBoolean) {
			return compile(o as IQLMetadataValueSingleBoolean, varName, context);
		} else if(o instanceof IQLMetadataValueSingleNull) {
			return compile(o as IQLMetadataValueSingleNull, varName, context);
		} else if(o instanceof IQLMetadataValueSingleTypeRef) {
			return compile(o as IQLMetadataValueSingleTypeRef, varName, context);
		} else if(o instanceof IQLMetadataValueList) {
			return compile(o as IQLMetadataValueList, varName, counter, context);
		} else if(o instanceof IQLMetadataValueMap) {
			return compile(o as IQLMetadataValueMap, varName, counter, context);
		} 
		return "";
	}
	
	def String compile(IQLMetadataValueSingleInt o, String varName, G context) {
		'''int «varName» = «o.value»;'''
	}
	
	def String compile(IQLMetadataValueSingleDouble o, String varName, G context) {
		'''double «varName» = «o.value»;'''
	}

	
	def String compile(IQLMetadataValueSingleString o, String varName, G context) {
		if (context.expectedTypeRef !== null) {
			if (typeUtils.isCharacter(context.expectedTypeRef)) {
				return "String "+varName+" = '"+o.value+"';"				
			} else {
				'''String «varName» = "«o.value»";'''
			}
		} else {
			'''String «varName» = "«o.value»";'''
		}
	}
	
	def String compile(IQLMetadataValueSingleBoolean o, String varName, G context) {
		'''boolean «varName» = «o.value»;'''
	}


	def String compile(IQLMetadataValueSingleNull o, String varName, G context) {
		'''«Object.simpleName» «varName» = null;'''
	}
	
	def String compile(IQLMetadataValueSingleTypeRef o, String varName, G context) {
		'''Class<?> «varName» = «typeCompiler.compile(o.value, context, true)».class; '''
	}

	def String compile(IQLMetadataValueList o, String varName, AtomicInteger counter, G context) {
		context.addImport(List.canonicalName)
		context.addImport(ArrayList.canonicalName)
		
		'''
		«List.simpleName»<«Object.simpleName»> «varName» = new «ArrayList.simpleName»<>(); 
		«FOR e : o.elements»
			«var name = METADATA_VALUE_VAR_NAME+counter.incrementAndGet()»
			«compile(e, name, counter, context)»
			«varName».add(«name»);
		«ENDFOR»
		'''
	}
	
	def String compile(IQLMetadataValueMap o, String varName, AtomicInteger counter, G context) {
		context.addImport(Map.canonicalName)
		context.addImport(HashMap.canonicalName)
	
		'''
		«Map.simpleName»<«Object.simpleName»,«Object.simpleName»> «varName» = new «HashMap.simpleName»<>(); 
		«FOR e : o.elements»
			«var keyVar = METADATA_VALUE_VAR_NAME+counter.incrementAndGet()»
			«var valueVar = METADATA_VALUE_VAR_NAME+counter.incrementAndGet()»
			«compile(e.key, keyVar, counter, context)»
			«compile(e.value, valueVar, counter, context)»
			«varName».put(«keyVar», «valueVar»);
		«ENDFOR»
		'''
	}
}
