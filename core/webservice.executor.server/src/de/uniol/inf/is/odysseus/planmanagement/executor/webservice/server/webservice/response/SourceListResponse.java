package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 
 * @author merlin
 *
 */
public class SourceListResponse extends Response {

	private List<SourceInformation> responseValue = new ArrayList<SourceInformation>();

	public SourceListResponse() {
		super();
	}

	public SourceListResponse(boolean success) {
		super(success);
	}

	public SourceListResponse(List<SourceInformation> response, boolean success) {
		super(success);
		this.responseValue = response;
	}

	public SourceListResponse(Collection<SourceInformation> response,
			boolean success) {
		super(success);
		this.responseValue.clear();
		this.responseValue.addAll(response);
	}

	public List<SourceInformation> getResponseValue() {
		return this.responseValue;
	}

	public void setResponseValue(List<SourceInformation> responseValue) {
		this.responseValue = responseValue;
	}
}
