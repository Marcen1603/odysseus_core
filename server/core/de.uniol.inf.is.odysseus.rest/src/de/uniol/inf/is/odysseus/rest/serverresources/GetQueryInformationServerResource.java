package de.uniol.inf.is.odysseus.rest.serverresources;

import org.restlet.resource.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
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
	
	static Logger logger = LoggerFactory.getLogger(GetQueryInformationServerResource.class);

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
		} else {
			// It's in an unknown format
			logger.error("Query-id of the request is not in a known format. Should be integer or string.");
		}

		String name = "";
		String parser = "";
		String state = "";
		String queryText = "";
		
		// Check, if query exists
		IPhysicalQuery query = ExecutorServiceBinding.getExecutor().getExecutionPlan(session).getQueryById(queryId, session);
		if (query == null) {
			// Query does not exist
			logger.error("Query with id " + queryId + " does not exist.");
			name = "";
			parser = "";
			state = "Not existing";
		} else {
			// Query exists
			Resource nameResource = ExecutorServiceBinding.getExecutor().getLogicalQueryById(queryId, session).getName();
			if (nameResource == null ) {
				name = "";
			} else {
				name = nameResource.getResourceName();
			}
			parser = ExecutorServiceBinding.getExecutor().getLogicalQueryById(queryId, session).getParserId();
			state = query.getState().toString();
			queryText = query.getQueryText();
		}
		
		QueryInformation queryInformation = new QueryInformation(name, parser, state, queryText);
		return new GenericResponseDTO<QueryInformation>(queryInformation);
	}
}
