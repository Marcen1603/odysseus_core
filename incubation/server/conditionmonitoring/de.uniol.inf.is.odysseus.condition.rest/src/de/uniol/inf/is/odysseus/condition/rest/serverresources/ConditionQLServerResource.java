package de.uniol.inf.is.odysseus.condition.rest.serverresources;

import java.util.Collection;

import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

import de.uniol.inf.is.odysseus.condition.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.condition.rest.dto.request.ExecuteConditionQLRequestDTO;
import de.uniol.inf.is.odysseus.condition.rest.dto.response.ExecuteConditionQLResponseDTO;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.serverresources.AbstractSessionServerResource;
import de.uniol.inf.is.odysseus.rest.service.RestService;
import de.uniol.inf.is.odysseus.rest.socket.SocketInfo;
import de.uniol.inf.is.odysseus.rest.socket.SocketService;

public class ConditionQLServerResource extends AbstractSessionServerResource {
	
	public static final String PATH = "conditionQL";
	
	@Post
	public ExecuteConditionQLResponseDTO addQuery(
			ExecuteConditionQLRequestDTO executeConditionQLRequestDTO) {
		
		ISession session = loginWithToken(executeConditionQLRequestDTO.getToken());
		String address = getRequest().getClientInfo().getAddress();

		try {
			StringBuilder conditionQL = new StringBuilder();
			conditionQL.append("#PARSER ConditionQL\n");
			conditionQL.append("#DOREWRITE false\n");
			conditionQL.append("#ADDQUERY \n");
			conditionQL.append(executeConditionQLRequestDTO.getConditionQL());

			Collection<Integer> queryIDs = ExecutorServiceBinding.getExecutor().addQuery(conditionQL.toString(), "OdysseusScript", session, Context.empty());
			int queryId = queryIDs.iterator().next();
			ExecutorServiceBinding.getExecutor().startQuery(queryId, session);
			SocketInfo peerSocket = SocketService.getInstance().getConnectionInformation(session, queryId, 0);

			// Save connection information in backup-information for recovery
			SocketService.getInstance().informAboutNewClient(queryId, address, peerSocket.getIp(), peerSocket.getPort());

			return new ExecuteConditionQLResponseDTO(session.getToken(), peerSocket, queryIDs.iterator().next(), RestService.getPort());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while executing query+ " + "", e);
		}

	}

}
