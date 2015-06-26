package de.uniol.inf.is.odysseus.rest.serverresources;

import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.dto.request.CreateSocketRequestDTO;
import de.uniol.inf.is.odysseus.rest.socket.SocketInfo;
import de.uniol.inf.is.odysseus.rest.socket.SocketService;

public class CreateMultipleSocketServerResource extends AbstractSessionServerResource {

	public static final String PATH = "createMultiSocket";

	@Post
	public Map<Integer, SocketInfo> addQuery(CreateSocketRequestDTO createSocketRequestDTO) {
		ISession session = this.loginWithToken(createSocketRequestDTO.getToken());
		Map<Integer, SocketInfo> infos = new HashMap<>();
		boolean withMetaData = false;

		if (createSocketRequestDTO.isUseQueryName()) {
			// We want to use a query name to identify the correct query
			if (createSocketRequestDTO.isUseOutputOperatorPort()) {
				// We know exactly which output we need (operator and
				// output-port from the operator)
				SocketInfo socketInfo = SocketService.getInstance().getConnectionInformation(session,
						createSocketRequestDTO.getQueryName(), createSocketRequestDTO.getOperatorName(),
						createSocketRequestDTO.getOutputOperatorPort(), withMetaData);
				infos.put(createSocketRequestDTO.getOutputOperatorPort(), socketInfo);
			} else if (createSocketRequestDTO.getOperatorName() != null
					&& !createSocketRequestDTO.getOperatorName().isEmpty()) {
				// We know the operator but not the output port
				infos = SocketService.getInstance().getMultipleConnectionInformation(session,
						createSocketRequestDTO.getQueryName(), createSocketRequestDTO.getOperatorName(), withMetaData);
			} else {
				// We don't know the operator
				infos = SocketService.getInstance().getMultipleConnectionInformation(session,
						createSocketRequestDTO.getQueryName(), withMetaData);
			}
		} else {
			// We want to use a query name to identify the correct query
			if (createSocketRequestDTO.isUseOutputOperatorPort()) {
				// We know exactly which output we need (operator and
				// output-port from the operator)
				SocketInfo socketInfo = SocketService.getInstance().getConnectionInformation(session,
						createSocketRequestDTO.getQueryId(), createSocketRequestDTO.getOperatorName(),
						createSocketRequestDTO.getOutputOperatorPort(), withMetaData);
				infos.put(createSocketRequestDTO.getOutputOperatorPort(), socketInfo);
			} else if (createSocketRequestDTO.getOperatorName() != null
					&& !createSocketRequestDTO.getOperatorName().isEmpty()) {
				// We know the operator but not the output port
				infos = SocketService.getInstance().getMultipleConnectionInformation(session,
						createSocketRequestDTO.getQueryId(), createSocketRequestDTO.getOperatorName(), withMetaData);
			} else {
				// We don't know the operator
				infos = SocketService.getInstance().getMultipleConnectionInformation(session,
						createSocketRequestDTO.getQueryId(), withMetaData);
			}
		}

		return infos;
	}

}
