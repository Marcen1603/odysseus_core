package de.uniol.inf.is.odysseus.rest.serverresources;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.dto.request.CreateSocketRequestDTO;
import de.uniol.inf.is.odysseus.rest.dto.response.GenericResponseDTO;
import de.uniol.inf.is.odysseus.rest.socket.SocketInfo;
import de.uniol.inf.is.odysseus.rest.socket.SocketService;

public class CreateSocketServerResource extends AbstractSessionServerResource {

	public static final String PATH = "createSocket";

	@Post
	public GenericResponseDTO<SocketInfo> addQuery(CreateSocketRequestDTO ceateSocketRequestDTO) {
		ISession session = this.loginWithToken(ceateSocketRequestDTO.getToken());
		SocketInfo info = SocketService.getInstance().getConnectionInformation(session,
				ceateSocketRequestDTO.getQueryId(), ceateSocketRequestDTO.getRootPort());
		return new GenericResponseDTO<SocketInfo>(info);
	}

}
