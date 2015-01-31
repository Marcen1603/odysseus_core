package de.uniol.inf.is.odysseus.peer.rest.dto.response;

import java.util.Collection;

import de.uniol.inf.is.odysseus.peer.rest.dto.SharedQueryInfo;
import de.uniol.inf.is.odysseus.rest.dto.response.AbstractResponseDTO;

public class GetSharedQueryIdsResponseDTO extends AbstractResponseDTO {
	private Collection<SharedQueryInfo> sharedQueries;
	
	public GetSharedQueryIdsResponseDTO() {
		
	}
	
	public GetSharedQueryIdsResponseDTO(Collection<SharedQueryInfo> sharedQueries) {
		this.sharedQueries = sharedQueries;
	}

	public Collection<SharedQueryInfo> getSharedQueries() {
		return sharedQueries;
	}

	public void setSharedQueries(Collection<SharedQueryInfo> sharedQueries) {
		this.sharedQueries = sharedQueries;
	}

	
	
	
}
