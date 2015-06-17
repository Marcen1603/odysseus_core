package de.uniol.inf.is.odysseus.rest.serverresources;

import java.util.List;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.dto.request.CreateSocketRequestDTO;
import de.uniol.inf.is.odysseus.rest.dto.response.GenericResponseDTO;
import de.uniol.inf.is.odysseus.rest.socket.SocketInfo;
import de.uniol.inf.is.odysseus.rest.socket.SocketService;

public class CreateMultipleSocketServerResource extends AbstractSessionServerResource {

	public static final String PATH = "createMultiSocket";

	@Post
	public GenericResponseDTO<List<SocketInfo>> addQuery(CreateSocketRequestDTO ceateSocketRequestDTO) {
		ISession session = this.loginWithToken(ceateSocketRequestDTO.getToken());
		int sourceOutputPort = 0;
		List<SocketInfo> infos = SocketService.getInstance().getMultipleConnectionInformation(session,
				ceateSocketRequestDTO.getQueryId(), sourceOutputPort);
		return new GenericResponseDTO<List<SocketInfo>>(infos);
	}

}
