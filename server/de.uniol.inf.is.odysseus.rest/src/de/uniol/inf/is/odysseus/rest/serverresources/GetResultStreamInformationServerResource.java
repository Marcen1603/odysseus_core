package de.uniol.inf.is.odysseus.rest.serverresources;

import java.net.UnknownHostException;
import java.util.Map;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.dto.request.GenericSessionRequestDTO;
import de.uniol.inf.is.odysseus.rest.impl.WebsocketManager;

/**
 * REST resource to create WebSocket-Sink queries and get the information about
 * a sink
 * 
 * @author Tobias Brandt
 *
 */
public class GetResultStreamInformationServerResource extends AbstractSessionServerResource {

	public static final String PATH = "getResultStreamInformation";

	@Post
	public Map<String, Map<Integer, String>> getResultStreamInformation(
			GenericSessionRequestDTO<Object> genericSessionRequestDTO) throws UnknownHostException {
		ISession session = this.loginWithToken(genericSessionRequestDTO.getToken());
		int queryId = -1;

		/*
		 * When using the REST interface with JavaScript / JSON, integers are maybe send
		 * as string
		 */
		if (genericSessionRequestDTO.getValue() instanceof String) {
			queryId = Integer.parseInt((String) genericSessionRequestDTO.getValue());			
		} else if (genericSessionRequestDTO.getValue() instanceof Integer) {
			queryId = (Integer) genericSessionRequestDTO.getValue();
		}

		return WebsocketManager.getInstance().getWebsockets(queryId, session);
	}

}
