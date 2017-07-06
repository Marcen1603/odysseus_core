package de.uniol.inf.is.odysseus.rest.serverresources;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest.dto.request.GenericSessionRequestDTO;
import de.uniol.inf.is.odysseus.rest.dto.response.GenericResponseDTO;

public class StartQueryServerResource extends AbstractSessionServerResource {

	public static final String PATH = "startQuery";

	@Post
	public GenericResponseDTO<Boolean> startQuery(GenericSessionRequestDTO<Object> genericSessionRequestDTO) {
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

		ExecutorServiceBinding.getExecutor().startQuery(queryId, session);
		boolean result = ExecutorServiceBinding.getExecutor().getQueryState(queryId, session) == QueryState.RUNNING;
		return new GenericResponseDTO<Boolean>(result);
	}

}
