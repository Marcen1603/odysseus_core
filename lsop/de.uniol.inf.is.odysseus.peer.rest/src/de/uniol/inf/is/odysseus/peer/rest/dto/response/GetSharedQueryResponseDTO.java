package de.uniol.inf.is.odysseus.peer.rest.dto.response;

import java.util.Collection;
import java.util.Map;

import de.uniol.inf.is.odysseus.peer.rest.dto.LocalQueryInfo;
import de.uniol.inf.is.odysseus.rest.dto.response.AbstractResponseDTO;

public class GetSharedQueryResponseDTO extends AbstractResponseDTO {
	private Map<String, Collection<LocalQueryInfo>>localQueries;
	
	public GetSharedQueryResponseDTO() {
		
	}
	
	public GetSharedQueryResponseDTO(Map<String, Collection<LocalQueryInfo>> localQueries) {
		this.localQueries = localQueries;
	}

	public Map<String, Collection<LocalQueryInfo>> getLocalQueries() {
		return localQueries;
	}

	public void setLocalQueries(Map<String, Collection<LocalQueryInfo>> localQueries) {
		this.localQueries = localQueries;
	}
	
	
}
