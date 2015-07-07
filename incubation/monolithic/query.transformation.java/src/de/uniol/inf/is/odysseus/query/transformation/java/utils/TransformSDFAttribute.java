package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.NeededDataHandler;

public class TransformSDFAttribute {
	
	
	/*
	 *  SDFAttribute ts = new SDFAttribute(null, "ts", SDFDatatype.STRING, null, null,null);
	 * 
	 */
	
	public static String getCodeForSDFAttribute(List<SDFAttribute> list, String operatorVariable){
		
		StringBuilder sdfAttributeCode = new StringBuilder();
		
		sdfAttributeCode.append("List<SDFAttribute> "+operatorVariable+"SDFAttributeList = new ArrayList();");
		sdfAttributeCode.append("\n");
		
		for(SDFAttribute attribute : list){
			sdfAttributeCode.append("SDFAttribute "+operatorVariable+attribute.getQualName()+" = new SDFAttribute(null,\""+attribute.getQualName()+"\", SDFDatatype."+attribute.getDatatype().toString().toUpperCase()+", null, null,null);");
			sdfAttributeCode.append("\n");
			sdfAttributeCode.append(operatorVariable+"SDFAttributeList.add("+operatorVariable+attribute.getQualName()+");");
			sdfAttributeCode.append("\n");
	
			NeededDataHandler.addDataHandler(DataHandlerRegistry.getIDataHandlerClass(attribute.getDatatype().toString()).getClass().getName(),DataHandlerRegistry.getIDataHandlerClass(attribute.getDatatype().toString()).getClass().getSimpleName());
		}
		
		return sdfAttributeCode.toString();
		
	}

}
