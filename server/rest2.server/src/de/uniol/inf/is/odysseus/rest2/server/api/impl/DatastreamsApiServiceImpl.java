package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.uniol.inf.is.odysseus.core.planmanagement.AbstractResourceInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.Resource;
import de.uniol.inf.is.odysseus.rest2.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest2.server.api.DatastreamsApi;
import de.uniol.inf.is.odysseus.rest2.server.api.DatastreamsApiService;

/**
 * This class provides the implementation for the REST service
 * {@link DatastreamsApi} that manages datastreams (views, sources).
 *
 * @author Cornelius A. Ludmann
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T16:12:00.919Z[GMT]")
public class DatastreamsApiServiceImpl extends DatastreamsApiService {

	@Override
	public Response datastreamsGet(Optional<ISession> session) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		List<ViewInformation> datastreams = ExecutorServiceBinding.getExecutor()
				.getStreamsAndViewsInformation(session.get());

		List<Resource> result = datastreams.stream().map(DatastreamsApiServiceImpl::transform)
				.collect(Collectors.toList());

		return Response.ok().entity(result).build();
	}

	@Override
	public Response datastreamsNameDelete(Optional<ISession> session, String name) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		// TODO: Should we check if the stream exists to response with a 404?

		// TODO: Do we need to ask for the owner to avoid a name collision?
		ExecutorServiceBinding.getExecutor().removeViewOrStream(name, session.get());

		// TODO: Do we need to check if the stream was successfully removed?

		return Response.ok().build();
	}

	@Override
	public Response datastreamsNameGet(Optional<ISession> session, String name) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		List<ViewInformation> datastreams = ExecutorServiceBinding.getExecutor()
				.getStreamsAndViewsInformation(session.get());

		// TODO: Do we need to check if there is more than one resource with the given
		// name or do we need ask for the user (owner)?
		Optional<ViewInformation> result = datastreams.stream()
				.filter(datastream -> datastream.getName().getResourceName().equals(name)).findAny();

		if (result.isPresent()) {
			return Response.ok().entity(transform(result.get())).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	/**
	 * Transforms a {@link AbstractResourceInformation} (e.g.
	 * {@link ViewInformation} object to the DTO {@link Resource}.
	 * 
	 * @param resource The source {@link AbstractResourceInformation} object.
	 * @return The resulting {@link Resource} object.
	 */
	static Resource transform(AbstractResourceInformation resource) {
		Objects.requireNonNull(resource);
		final Resource result = new Resource();
		result.setName(resource.getName().getResourceName());
		result.setOwner(resource.getName().getUser());
		result.setSchema(DatatypesApiServiceImpl.transform(resource.getOutputSchema()));
		result.setType(resource.getType());
		// TODO: Do we need resource.getName().isMarked() ?
		return result;
	}
}
