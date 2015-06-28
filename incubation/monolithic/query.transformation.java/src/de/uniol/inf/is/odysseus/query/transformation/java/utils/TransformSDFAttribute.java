package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

public class TransformSDFAttribute {
	
	
	/*
	 *  SDFAttribute ts = new SDFAttribute(null, "ts", SDFDatatype.STRING, null, null,null);
	 * 
	 */
	
	public static String getCodeForSDFAttribute(List<SDFAttribute> list, String uri){
		
		StringBuilder sdfAttributeCode = new StringBuilder();
		
		sdfAttributeCode.append("List<SDFAttribute> "+uri+"SDFAttributeList = new ArrayList();");
		sdfAttributeCode.append("\n");
		
		for(SDFAttribute attribute : list){
			sdfAttributeCode.append("SDFAttribute "+attribute.getQualName()+" = new SDFAttribute(null,\""+attribute.getQualName()+"\", SDFDatatype."+attribute.getDatatype().toString().toUpperCase()+", null, null,null);");
			sdfAttributeCode.append("\n");
			sdfAttributeCode.append(uri+"SDFAttributeList.add("+attribute.getQualName()+");");
			sdfAttributeCode.append("\n");
		
		}
		
		return sdfAttributeCode.toString();
		
	}

}
