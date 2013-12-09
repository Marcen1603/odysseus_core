package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import java.util.List;
import java.util.Map;

public class StringMapStringListResponse extends Response {
	
	private Map<String, List<String>> responseValue;
	
	public StringMapStringListResponse() {
		super();
	}
	
	public StringMapStringListResponse(Map<String, List<String>> responseValue, boolean success) {
		super(success);
		this.responseValue = responseValue;
	}
	
	public Map<String, List<String>> getResponseValue() {
		return this.responseValue;
	}
	
	public void setResponseValue(Map<String, List<String>> value) {
		this.responseValue = value;
	}

}
