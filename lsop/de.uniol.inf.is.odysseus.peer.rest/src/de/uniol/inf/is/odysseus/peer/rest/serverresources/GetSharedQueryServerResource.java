package de.uniol.inf.is.odysseus.peer.rest.serverresources;



import java.util.Collection;
import java.util.Map;



import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.rest.dto.LocalQueryInfo;
import de.uniol.inf.is.odysseus.peer.rest.dto.request.GetSharedQueryRequestDTO;
import de.uniol.inf.is.odysseus.peer.rest.dto.response.GetSharedQueryResponseDTO;
import de.uniol.inf.is.odysseus.peer.rest.sharedquerygraph.SharedQueryGraphHelper;
import de.uniol.inf.is.odysseus.rest.serverresources.AbstractSessionServerResource;


public class GetSharedQueryServerResource extends AbstractSessionServerResource {

	
	public static final String PATH = "getSharedQuery";


	@Post
	public GetSharedQueryResponseDTO getSharedQueryInfo(GetSharedQueryRequestDTO req) {
		ISession session = loginWithToken(req.getToken());
		Map<String, Collection<LocalQueryInfo>> result = SharedQueryGraphHelper.getSharedQueryInfo(session, req.getSharedQueryId(), req.getTenant(), req.getUsername(),req.getPassword());
		return new GetSharedQueryResponseDTO(result);
	}	
	
}
