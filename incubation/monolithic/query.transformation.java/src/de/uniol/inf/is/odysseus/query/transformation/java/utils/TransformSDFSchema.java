package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;

public class TransformSDFSchema {
	
	public static CodeFragmentInfo getCodeForSDFSchema(SDFSchema schema, String operatorVariable){
		CodeFragmentInfo sdfSchema = new CodeFragmentInfo();
		
		String className = schema.getType().getSimpleName()+".class";

		
		StringBuilder code = new StringBuilder();
		
	
		
		sdfSchema.addCodeFragmentInfo(TransformSDFAttribute.getCodeForSDFAttribute(schema.getAttributes(), operatorVariable));

		code.append("\n");
		code.append("\n");
		code.append("//Create SDFSchema for "+operatorVariable);
		code.append("\n");
		code.append("SDFSchema "+operatorVariable+"SDFSchema = SDFSchemaFactory.createNewSchema(\""+schema.getURI()+"\", "+className+", "+operatorVariable+"SDFAttributeList);");
		code.append("\n");
		code.append("\n");
		
		
		sdfSchema.addImport(schema.getType().getName());
		sdfSchema.addImport(SDFSchema.class.getName());
		sdfSchema.addImport(SDFSchemaFactory.class.getName());
		
		
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
