package de.uniol.inf.is.odysseus.peer.rest.dto.request;

import de.uniol.inf.is.odysseus.rest.dto.request.AbstractLoginRequestDTO;

public class GetLocalQueriesRequestDTO extends AbstractLoginRequestDTO{
	private String sharedQueryId;
	
	public GetLocalQueriesRequestDTO() {
		
	}

	
	public String getSharedQueryId() {
		return sharedQueryId;
	}

	public void setSharedQueryId(String sharedQueryId) {
		this.sharedQueryId = sharedQueryId;
	}
	
	
}
