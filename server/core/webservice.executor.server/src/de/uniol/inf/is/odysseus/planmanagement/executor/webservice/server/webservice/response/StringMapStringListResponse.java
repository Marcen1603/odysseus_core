package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import java.util.List;

public class StringMapStringListResponse extends Response {
	
	private List<StringMapListEntry> responseValue;
	
	public StringMapStringListResponse() {
		super();
	}
	
	public StringMapStringListResponse(List<StringMapListEntry> responseValue, boolean success) {
		super(success);
		this.responseValue = responseValue;
	}
	
	public List<StringMapListEntry> getResponseValue() {
		return this.responseValue;
	}
	
	public void setResponseValue(List<StringMapListEntry> value) {
		this.responseValue = value;
	}

}
