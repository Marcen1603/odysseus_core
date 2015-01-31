package de.uniol.inf.is.odysseus.peer.rest.serverresources;



import java.util.Collection;


import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.rest.dto.LocalQueryInfo;
import de.uniol.inf.is.odysseus.peer.rest.dto.request.GetLocalQueriesRequestDTO;
import de.uniol.inf.is.odysseus.peer.rest.dto.response.GetLocalQueriesResponseDTO;
import de.uniol.inf.is.odysseus.peer.rest.sharedquerygraph.SharedQueryGraphHelper;
import de.uniol.inf.is.odysseus.rest.serverresources.AbstractLoginServerResource;

public class GetLocalQueriesServerResource extends AbstractLoginServerResource {

	
	public static final String PATH = "getLocalQueries";


	@Post
	public GetLocalQueriesResponseDTO getLocalQueries(GetLocalQueriesRequestDTO req) {		
		ISession session = login(req.getTenant(), req.getUsername(), req.getPassword());
		Collection<LocalQueryInfo> result = SharedQueryGraphHelper.getLocalQueries(session, req.getSharedQueryId());
		return new GetLocalQueriesResponseDTO(result);
	}	
	
}
