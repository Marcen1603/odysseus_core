package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class TransformSDFSchema {
	
	public static String getCodeForSDFSchema(SDFSchema schema, String operatorVariable){
		
		String className = schema.getType().getSimpleName()+".class";
	
	
		StringBuilder sdfSchemaCode = new StringBuilder();
		
		sdfSchemaCode.append("\n");
		sdfSchemaCode.append("\n");
		sdfSchemaCode.append("//Create SDFSchema for "+operatorVariable);
		sdfSchemaCode.append("\n");
		sdfSchemaCode.append("\n");
		sdfSchemaCode.append(TransformSDFAttribute.getCodeForSDFAttribute(schema.getAttributes(), operatorVariable));
		sdfSchemaCode.append("SDFSchema "+operatorVariable+"SDFSchema = SDFSchemaFactory.createNewSchema(\""+schema.getURI()+"\", "+className+", "+operatorVariable+"SDFAttributeList);");
		sdfSchemaCode.append("\n");
		sdfSchemaCode.append("\n");
		

		return sdfSchemaCode.toString();
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
