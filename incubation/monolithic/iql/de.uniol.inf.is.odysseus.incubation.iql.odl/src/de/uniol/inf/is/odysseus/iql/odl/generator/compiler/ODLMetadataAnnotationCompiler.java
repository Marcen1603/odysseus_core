package de.uniol.inf.is.odysseus.iql.odl.generator.compiler;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;




















import javax.inject.Inject;

import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLMetadataAnnotationCompiler;
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;

public class ODLMetadataAnnotationCompiler extends AbstractIQLMetadataAnnotationCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLTypeCompiler>{
	

	private static String AO_DEFAULT_DOC = "IQL user operator";
	private static String AO_DEFAULT_Category = LogicalOperatorCategory.ADVANCED;
	
	@Inject
	private ODLTypeFactory typeFactory;
	
	@Inject
	public ODLMetadataAnnotationCompiler(ODLCompilerHelper helper,ODLTypeCompiler typeCompiler) {
		super(helper, typeCompiler);
	}
	
	
	public String getAOAnnotationElements(ODLOperator operator, ODLGeneratorContext context) {
		Map<String, String> elements = new HashMap<>();
		IQLMetadataList metadataList = operator.getMetadataList();
		if (metadataList != null) {
			for (IQLMetadata metadata : metadataList.getElements()) {
				if (isAOAnnotationElement(metadata)) {
					elements.put(metadata.getName(), compile(metadata.getValue(), context));
				}
			}
		}
		if (!elements.containsKey("name")) {
			elements.put("name", "\""+operator.getSimpleName()+"\"");
		}
		if (!elements.containsKey("minInputPorts")) {
			elements.put("minInputPorts", "1");
		}
		if (!elements.containsKey("maxInputPorts")) {
			elements.put("maxInputPorts", "1");	
		}
		if (!elements.containsKey("doc")) {
			elements.put("doc", "\""+AO_DEFAULT_DOC+"\"");	
		}
		if (!elements.containsKey("category")) {
			elements.put("category", "{"+LogicalOperatorCategory.class.getSimpleName()+"."+AO_DEFAULT_Category+"}");	
		}
		
		StringBuilder b = new StringBuilder();
		int i = 0;
		for (Entry<String, String> e : elements.entrySet()) {
			if (i > 0 ) {
				b.append(", ");
			}
			i++;
			b.append(e.getKey()+" = "+e.getValue());
		}
		return b.toString();
	}
	
	
	public String getParameterAnnotationElements(ODLParameter parameter, ODLGeneratorContext context) {
		Map<String, String> elements = new HashMap<>();
		IQLMetadataList metadataList = parameter.getMetadataList();
		if (metadataList != null) {
			for (IQLMetadata metadata : metadataList.getElements()) {
				if (isAOParameterAnnotationElement(metadata)) {
					elements.put(metadata.getName(), compile(metadata.getValue(), context));
				}
			}
		}
		if (!elements.containsKey("type")) {
			JvmTypeReference parameterType = typeFactory.getParameterType(parameter.getType());
			if (parameterType != null) {
				elements.put("type", typeCompiler.compile(parameterType, context,  true)+".class");
			}
		}
		if (!elements.containsKey("optional")) {
			elements.put("optional", parameter.isOptional()+"");
		}
		if (typeFactory.isList(parameter.getType()) && !elements.containsKey("isList")) {
			elements.put("isList", "true");	
		}
		if (typeFactory.isMap(parameter.getType()) && !elements.containsKey("isMap")) {
			elements.put("isMap", "true");				
		}
		if (elements.containsKey(ODLTypeFactory.KEY_TYPE)) {
			elements.put("keytype", elements.get(ODLTypeFactory.KEY_TYPE));				
		}
		
		StringBuilder b = new StringBuilder();
		int i = 0;
		for (Entry<String, String> e : elements.entrySet()) {
			if (i > 0 ) {
				b.append(", ");
			}
			i++;
			b.append(e.getKey()+" = "+e.getValue());
		}
		return b.toString();
	}
		
	private boolean isAOParameterAnnotationElement(IQLMetadata metadata) {
		for (Method element : Parameter.class.getMethods()) {
			if (element.getName().equals(metadata.getName())) {
				return true;
			}
		}
		return false;
	}

	
	private boolean isAOAnnotationElement(IQLMetadata metadata) {
		for (Method element : LogicalOperator.class.getMethods()) {
			if (element.getName().equals(metadata.getName())) {
				return true;
			}
		}
		return false;
	}

}
