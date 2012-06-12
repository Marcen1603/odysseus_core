package de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.response;


/**
 * @author merlin
 *
 */
public class SDFSchemaResponse extends Response {
	private SDFSchemaInformation responseValue;
	
	public SDFSchemaResponse() {
		super();
	}
	
	public SDFSchemaResponse(SDFSchemaInformation response, boolean success) {
		super(success);
		this.responseValue = response;
	}
	
	public SDFSchemaInformation getResponseValue() {
		return this.responseValue;
	}
	
	public void setResponseValue(SDFSchemaInformation value) {
		this.responseValue = value;
	}
}
