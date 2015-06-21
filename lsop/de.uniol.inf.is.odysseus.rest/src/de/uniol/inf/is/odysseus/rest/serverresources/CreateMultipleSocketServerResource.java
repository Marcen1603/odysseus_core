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
	public GenericResponseDTO<List<SocketInfo>> addQuery(CreateSocketRequestDTO createSocketRequestDTO) {
		ISession session = this.loginWithToken(createSocketRequestDTO.getToken());
		List<SocketInfo> infos = null;
		boolean withMetaData = true;
		if (createSocketRequestDTO.isUseName()) {
			infos = SocketService.getInstance().getMultipleConnectionInformation(session,
					createSocketRequestDTO.getQueryName(), withMetaData);
		} else {
			infos = SocketService.getInstance().getMultipleConnectionInformation(session,
					createSocketRequestDTO.getQueryId(), withMetaData);
		}

		return new GenericResponseDTO<List<SocketInfo>>(infos);
	}

}
