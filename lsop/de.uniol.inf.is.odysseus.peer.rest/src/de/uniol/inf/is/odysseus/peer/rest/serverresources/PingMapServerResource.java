package de.uniol.inf.is.odysseus.peer.rest.serverresources;



import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.peer.rest.pingmap.PingMapHelper;
import de.uniol.inf.is.odysseus.rest.dto.request.SecurityTokenRequestDTO;
import de.uniol.inf.is.odysseus.rest.dto.response.GenericResponseDTO;
import de.uniol.inf.is.odysseus.rest.serverresources.AbstractSessionServerResource;

public class PingMapServerResource extends AbstractSessionServerResource {

	
	public static final String PATH = "pingMap";


	@Post
	public GenericResponseDTO<Integer> getPingMapConnection(SecurityTokenRequestDTO securityTokenRequestDTO) {
		loginWithToken(securityTokenRequestDTO.getToken());
		int port = PingMapHelper.getInstance().createServerSocket();
		return new GenericResponseDTO<Integer>(port);
	}	
	
}
