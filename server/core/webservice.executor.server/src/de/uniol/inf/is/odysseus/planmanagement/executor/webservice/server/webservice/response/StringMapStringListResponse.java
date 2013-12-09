package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import java.util.ArrayList;
import java.util.Map;

public class StringMapStringListResponse extends Response {
	
	private Map<String, ArrayList<String>> responseValue;
	
	public StringMapStringListResponse() {
		super();
	}
	
	public StringMapStringListResponse(Map<String, ArrayList<String>> responseValue, boolean success) {
		super(success);
		this.responseValue = responseValue;
	}
	
	public Map<String, ArrayList<String>> getResponseValue() {
		return this.responseValue;
	}
	
	public void setResponseValue(Map<String, ArrayList<String>> value) {
		this.responseValue = value;
	}

}
