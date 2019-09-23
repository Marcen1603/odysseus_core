package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import java.util.Optional;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest2.server.api.AggregateFunctionsApi;
import de.uniol.inf.is.odysseus.rest2.server.api.AggregateFunctionsApiService;

/**
 * This class provides the implementation for the REST service
 * {@link AggregateFunctionsApi} that returns the available aggregate functions.
 * 
 * @author Cornelius A. Ludmann
 */
public class AggregateFunctionsApiServiceImpl extends AggregateFunctionsApiService {

	@Override
	public Response aggregateFunctionsGet(Optional<ISession> session, String datamodel) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		if (datamodel == null || datamodel.trim().equals("")) {
			return Response.status(Status.BAD_REQUEST).entity("Datamodel not given.").type(MediaType.TEXT_PLAIN)
					.build();
		}

		Set<String> aggregateFunctions = ExecutorServiceBinding.getExecutor().getRegisteredAggregateFunctions(datamodel,
				session.get());

		return Response.ok().entity(aggregateFunctions).build();
	}
}
