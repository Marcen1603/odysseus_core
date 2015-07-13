package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;

public class TransformSDFAttribute {
	
	
	/*
	 *  SDFAttribute ts = new SDFAttribute(null, "ts", SDFDatatype.STRING, null, null,null);
	 * 
	 */
	
	public static CodeFragmentInfo getCodeForSDFAttribute(List<SDFAttribute> list, String operatorVariable){
		CodeFragmentInfo sdfAttributeList = new CodeFragmentInfo();
		
	
		StringBuilder code = new StringBuilder();
		
		
		
		code.append("\n");
		code.append("\n");
		code.append("//Create SDFAttribute for "+operatorVariable);
		code.append("\n");
		code.append("List<SDFAttribute> "+operatorVariable+"SDFAttributeList = new ArrayList();");
		code.append("\n");
		
		for(SDFAttribute attribute : list){
			code.append("SDFAttribute "+operatorVariable+attribute.getQualName()+" = new SDFAttribute(null,\""+attribute.getQualName()+"\", SDFDatatype.getType(\""+attribute.getDatatype().toString()+"\"), null, null,null);");
			code.append("\n");
			code.append(operatorVariable+"SDFAttributeList.add("+operatorVariable+attribute.getQualName()+");");
			code.append("\n");
	
			TransformationInformation.getInstance().addDataHandler(attribute.getDatatype().toString());
		}

		
		sdfAttributeList.addImport(SDFAttribute.class.getName());
		sdfAttributeList.addImport(List.class.getName());
		sdfAttributeList.addImport(ArrayList.class.getName());
		sdfAttributeList.addImport(SDFDatatype.class.getName());
		
		
		sdfAttributeList.addCode(code.toString());
		
		
		return sdfAttributeList;
		
	}

}
