package de.uniol.inf.is.odysseus.query.codegenerator.jre.model;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

public class CAggregateItemModel {
	
	String functionName = "";
	List<SDFAttribute> inAttributes;
	SDFAttribute outAttribute;
	
	String outAttributeSourceName = "";
	String outAttributeAttributeName = "";
	String outAttributeSDFDataTypeName = "";
	
	String outAttributeAttributeNameEscaped = "";
	
	
	public CAggregateItemModel(String functionName, List<SDFAttribute> inAttributes, SDFAttribute outAttribute, String outAttributeSourceName, String outAttributeAttributeName,String outAttributeSDFDataTypeName){
		 this.functionName = functionName;
		 this.inAttributes = inAttributes;
		 this.outAttribute = outAttribute;
		
		 this.outAttributeSourceName = outAttributeSourceName;
		 this.outAttributeAttributeName = outAttributeAttributeName;
		 this.outAttributeSDFDataTypeName = outAttributeSDFDataTypeName;
		 
		 this.outAttributeAttributeNameEscaped = outAttributeAttributeName.replace("(", "_").replace(")", "_").replace(".", "_").replace(":", "");
	}

	

	public String getFunctionName() {
		return functionName;
	}


	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}


	public List<SDFAttribute> getInAttributes() {
		return inAttributes;
	}


	public void setInAttributes(List<SDFAttribute> inAttributes) {
		this.inAttributes = inAttributes;
	}


	public SDFAttribute getOutAttribute() {
		return outAttribute;
	}


	public void setOutAttribute(SDFAttribute outAttribute) {
		this.outAttribute = outAttribute;
	}


	public String getOutAttributeSourceName() {
		return outAttributeSourceName;
	}


	public void setOutAttributeSourceName(String outAttributeSourceName) {
		this.outAttributeSourceName = outAttributeSourceName;
	}


	public String getOutAttributeAttributeName() {
		return outAttributeAttributeName;
	}
	


	public void setOutAttributeAttributeName(String outAttributeAttributeName) {
		this.outAttributeAttributeName = outAttributeAttributeName;
	}


	public String getOutAttributeSDFDataTypeName() {
		return outAttributeSDFDataTypeName;
	}


	public void setOutAttributeSDFDataTypeName(String outAttributeSDFDataTypeName) {
		this.outAttributeSDFDataTypeName = outAttributeSDFDataTypeName;
	}



	public String getOutAttributeAttributeNameEscaped() {
		return outAttributeAttributeNameEscaped;
	}



	public void setOutAttributeAttributeNameEscaped(
			String outAttributeAttributeNameEscaped) {
		this.outAttributeAttributeNameEscaped = outAttributeAttributeNameEscaped;
	}
	



}
