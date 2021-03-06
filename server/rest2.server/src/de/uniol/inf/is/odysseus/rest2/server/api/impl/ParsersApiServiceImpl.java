package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.Query;
import de.uniol.inf.is.odysseus.rest2.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest2.server.api.ParsersApiService;

public class ParsersApiServiceImpl extends ParsersApiService {

	@Override
	public Response parsersGet(Optional<ISession> session) {
		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}
		final IServerExecutor executor = ExecutorServiceBinding.getExecutor();
		return Response.ok().entity(executor.getSupportedQueryParsers(session.get())).build();
	}

	@Override
	public Response parsersNamePost(Optional<ISession> session, String parser, String scriptText) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		final IServerExecutor executor = ExecutorServiceBinding.getExecutor();

		final Set<String> supportedParsers = executor.getSupportedQueryParsers(session.get());
		if (!supportedParsers.contains(parser)) {
			return Response.status(Status.BAD_REQUEST)
					.entity("Parser needs to be one of these: " + String.join(", ", supportedParsers))
					.type(MediaType.TEXT_PLAIN).build();
		}

		if (scriptText == null) {
			return Response.status(Status.BAD_REQUEST).entity("Query text missing.").type(MediaType.TEXT_PLAIN).build();
		}

		// TODO: Is addQuery() the appropriate method to execute the given script?

		// TODO: Is there a way to catch errors caused by deficient scripts? In this
		// case returning a 4xx status code would be more appropriate.

		Collection<Integer> queryIds=null;
		
		try {
			queryIds = executor.addQuery(scriptText, parser, session.get(), new Context());
		}catch(Exception e) {
			throw new PlanManagementException(e.getMessage());
		}

		if (queryIds == null || queryIds.isEmpty()) {
			return Response.noContent().build();
		} else {
			List<Query> queries = queryIds.stream().map(x -> QueriesApiServiceImpl.getQuery(session.get(), x, false))
					.filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

			return Response.ok().entity(queries).build();
		}
	}
}
