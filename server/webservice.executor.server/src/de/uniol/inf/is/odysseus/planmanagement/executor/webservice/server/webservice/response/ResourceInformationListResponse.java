package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResourceInformationListResponse extends Response {
	
	private List<ResourceInformationEntry> responseValue = new ArrayList<ResourceInformationEntry>();

	public ResourceInformationListResponse() {
		super();
	}

	public ResourceInformationListResponse(boolean success) {
		super(success);
	}

	public ResourceInformationListResponse(List<ResourceInformationEntry> response, boolean success) {
		super(success);
		this.responseValue = response;
	}

	public ResourceInformationListResponse(Collection<ResourceInformationEntry> response,
			boolean success) {
		super(success);
		this.responseValue.clear();
		this.responseValue.addAll(response);
	}

	public List<ResourceInformationEntry> getResponseValue() {
		return this.responseValue;
	}

	public void setResponseValue(List<ResourceInformationEntry> responseValue) {
		this.responseValue = responseValue;
	}

	public void addResponseValue(ResourceInformationEntry resourceInformationEntry) {
		this.responseValue.add(resourceInformationEntry);
		
	}
	
}
