package de.uniol.inf.is.odysseus.rest.serverresources;




import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest.dto.request.GenericSessionRequestDTO;
import de.uniol.inf.is.odysseus.rest.dto.response.GenericResponseDTO;

public class RemoveQueryServerResource extends AbstractSessionServerResource  {

	public static final String PATH = "removeQuery";

	@Post
	public GenericResponseDTO<Boolean> removeQuery(GenericSessionRequestDTO<Integer> genericSessionRequestDTO) {
		ISession session = this.loginWithToken(genericSessionRequestDTO.getToken());
		int queryId = genericSessionRequestDTO.getValue();
		ExecutorServiceBinding.getExecutor().removeQuery(queryId,session);
		boolean result = ExecutorServiceBinding.getExecutor().getQueryState(queryId) == QueryState.UNDEF;
		return new GenericResponseDTO<Boolean>(result);
	}

	

}
