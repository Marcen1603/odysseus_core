package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.uniol.inf.is.odysseus.core.planmanagement.SinkInformation;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.Resource;
import de.uniol.inf.is.odysseus.rest2.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest2.server.api.SinksApiService;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T16:12:00.919Z[GMT]")
public class SinksApiServiceImpl extends SinksApiService {

	@Override
	public Response sinksGet(Optional<ISession> session) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		List<SinkInformation> sinks = ExecutorServiceBinding.getExecutor().getSinks(session.get());

		List<Resource> result = sinks.stream().map(DatastreamsApiServiceImpl::transform).collect(Collectors.toList());

		return Response.ok().entity(result).build();
	}

	@Override
	public Response sinksNameDelete(Optional<ISession> session, String name) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		// TODO: Should we check if the sink exists to response with a 404?

		// TODO: Do we need to ask for the owner to avoid a name collision?
		ExecutorServiceBinding.getExecutor().removeSink(name, session.get());

		// TODO: Do we need to check if the sink was successfully removed?

		return Response.ok().build();
	}

	@Override
	public Response sinksNameGet(Optional<ISession> session, String name) {
		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		List<SinkInformation> sinks = ExecutorServiceBinding.getExecutor().getSinks(session.get());

		// TODO: Do we need to check if there is more than one resource with the given
		// name or do we need ask for the user (owner)?
		Optional<SinkInformation> result = sinks.stream()
				.filter(datastream -> datastream.getName().getResourceName().equals(name)).findAny();

		if (result.isPresent()) {
			return Response.ok().entity(DatastreamsApiServiceImpl.transform(result.get())).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}

	}
}
