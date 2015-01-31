package de.uniol.inf.is.odysseus.peer.rest.serverresources;



import java.util.Collection;











import org.restlet.resource.Post;


import de.uniol.inf.is.odysseus.peer.rest.dto.SharedQueryInfo;
import de.uniol.inf.is.odysseus.peer.rest.dto.response.GetSharedQueryIdsResponseDTO;
import de.uniol.inf.is.odysseus.peer.rest.sharedquerygraph.SharedQueryGraphHelper;
import de.uniol.inf.is.odysseus.rest.dto.request.SecurityTokenRequestDTO;
import de.uniol.inf.is.odysseus.rest.serverresources.AbstractSessionServerResource;

public class GetSharedQueryIdsServerResource extends AbstractSessionServerResource {

	
	public static final String PATH = "getSharedQueryIds";


	@Post
	public GetSharedQueryIdsResponseDTO getSharedQueryIds(SecurityTokenRequestDTO securityTokenRequestDTO) {
		loginWithToken(securityTokenRequestDTO.getToken());
		Collection<SharedQueryInfo> result = SharedQueryGraphHelper.getSharedQueryIds();
		return new GetSharedQueryIdsResponseDTO(result);
	}	
	
	
}
