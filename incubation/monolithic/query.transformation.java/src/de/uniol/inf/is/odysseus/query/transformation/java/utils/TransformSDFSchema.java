package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;

public class TransformSDFSchema {
	
	public static CodeFragmentInfo getCodeForSDFSchema(SDFSchema schema, String operatorVariable){
		CodeFragmentInfo sdfSchema = new CodeFragmentInfo();
		
		String className = schema.getType().getSimpleName()+".class";
		
		StringBuilder code = new StringBuilder();
		
	
		StringTemplate sdfSchemaTemplate = new StringTemplate("java","sdfSchema");
		sdfSchemaTemplate.getSt().add("operatorVariable", operatorVariable);
		sdfSchemaTemplate.getSt().add("schemaURI", schema.getURI());
		sdfSchemaTemplate.getSt().add("className", className);
		sdfSchemaTemplate.getSt().add("sdfAttributes", schema.getAttributes());
		
		code.append(sdfSchemaTemplate.getSt().render());
		
	
		//add needed dataHandler
		for(SDFAttribute attribute : schema.getAttributes()){
			TransformationInformation.getInstance().addDataHandler(attribute.getDatatype().toString());
		}

		
		//Add imports 
		sdfSchema.addImport(schema.getType().getName());
		sdfSchema.addImport(SDFSchema.class.getName());
		sdfSchema.addImport(SDFSchemaFactory.class.getName());
		
		
		sdfSchema.addImport(SDFAttribute.class.getName());
		sdfSchema.addImport(List.class.getName());
		sdfSchema.addImport(ArrayList.class.getName());
		sdfSchema.addImport(SDFDatatype.class.getName());
		
		
		
		sdfSchema.addCode(code.toString());

		return sdfSchema;
	}
	
	
	/*
    //AccessPO schema erstellen
    List<SDFAttribute> sourceSchemaList = new ArrayList(); 
    SDFAttribute sid = new SDFAttribute(null, "a", SDFDatatype.INTEGER, null, null,null);
    SDFAttribute ts = new SDFAttribute(null, "ts", SDFDatatype.STRING, null, null,null);
    sourceSchemaList.add(sid);
    sourceSchemaList.add(ts);
    
	SDFSchema sourceSchema = SDFSchemaFactory.createNewSchema("soccergame", Tuple.class, sourceSchemaList);
*/
}
