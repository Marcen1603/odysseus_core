package de.uniol.inf.is.odysseus.peer.rest.dto.response;

import java.util.Collection;

import de.uniol.inf.is.odysseus.peer.rest.dto.LocalQueryInfo;
import de.uniol.inf.is.odysseus.rest.dto.response.AbstractResponseDTO;

public class GetLocalQueriesResponseDTO extends AbstractResponseDTO {
	private Collection<LocalQueryInfo> localQueries;
	
	public GetLocalQueriesResponseDTO() {
		
	}
	
	public GetLocalQueriesResponseDTO(Collection<LocalQueryInfo> localQueries) {
		this.localQueries = localQueries;
	}

	public Collection<LocalQueryInfo> getLocalQueries() {
		return localQueries;
	}

	public void setLocalQueries(Collection<LocalQueryInfo> localQueries) {
		this.localQueries = localQueries;
	}
	
	
}
