package de.uniol.inf.is.odysseus.rest.serverresources;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest.dto.request.GenericSessionRequestDTO;
import de.uniol.inf.is.odysseus.rest.dto.response.GenericResponseDTO;
import de.uniol.inf.is.odysseus.rest.dto.response.QueryInformation;

/**
 * Collects information about a specific query and returns it.
 * 
 * @author Tobias Brandt
 *
 */
public class GetQueryInformationServerResource extends AbstractSessionServerResource {

	public static final String PATH = "getQueryInfo";

	@Post
	public GenericResponseDTO<QueryInformation> getQueryInfo(
			GenericSessionRequestDTO<Object> genericSessionRequestDTO) {
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

		String name = ExecutorServiceBinding.getExecutor().getLogicalQueryById(queryId, session).getName()
				.getResourceName();
		String parser = ExecutorServiceBinding.getExecutor().getLogicalQueryById(queryId, session).getParserId();
		QueryInformation queryInformation = new QueryInformation(name, parser);
		return new GenericResponseDTO<QueryInformation>(queryInformation);
	}
}
