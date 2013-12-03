package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;


public class SourceInformation {
	
	private SDFSchemaInformation schema;
	private String name;
	private String ownerId;

	public SourceInformation() {
		
	}
	
	public SourceInformation(SDFSchemaInformation schema, String name, String ownerId) {
		this.schema = schema;
		this.name = name;
		this.ownerId = ownerId;
	}

	public SDFSchemaInformation getSchema() {
		return schema;
	}

	public void setSchema(SDFSchemaInformation schema) {
		this.schema = schema;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
}
