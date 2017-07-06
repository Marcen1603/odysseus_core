	package de.uniol.inf.is.odysseus.rest.serverresources;




import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest.dto.request.GenericSessionRequestDTO;
import de.uniol.inf.is.odysseus.rest.dto.response.GenericResponseDTO;

public class StopQueryServerResource extends AbstractSessionServerResource  {

	public static final String PATH = "stopQuery";

	@Post
	public GenericResponseDTO<Boolean> stopQuery(GenericSessionRequestDTO<Object> genericSessionRequestDTO) {
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
		
		ExecutorServiceBinding.getExecutor().stopQuery(queryId,session);
		boolean result = ExecutorServiceBinding.getExecutor().getQueryState(queryId, session) == QueryState.INACTIVE;
		return new GenericResponseDTO<Boolean>(result);
	}



}
