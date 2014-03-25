package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

public class ViewInformationWS {

	ResourceInformation name;
	SDFSchemaInformation schema;
	
	public ViewInformationWS() {
	}
	
	public void setName(ResourceInformation name) {
		this.name = name;
	}
	
	public ResourceInformation getName() {
		return name;
	}
	
	public void setSchema(SDFSchemaInformation schema) {
		this.schema = schema;
	}
	
	public SDFSchemaInformation getSchema() {
		return schema;
	}

}
