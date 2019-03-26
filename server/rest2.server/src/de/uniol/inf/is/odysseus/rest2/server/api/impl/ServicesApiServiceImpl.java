package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.rest2.common.model.BundleInfo;
import de.uniol.inf.is.odysseus.rest2.common.model.EventWebSocket;
import de.uniol.inf.is.odysseus.rest2.common.model.Query;
import de.uniol.inf.is.odysseus.rest2.common.model.Schema;
import de.uniol.inf.is.odysseus.rest2.common.model.Token;
import de.uniol.inf.is.odysseus.rest2.common.model.User;
import de.uniol.inf.is.odysseus.rest2.server.Application;
import de.uniol.inf.is.odysseus.rest2.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest2.server.api.ServicesApi;
import de.uniol.inf.is.odysseus.rest2.server.api.ServicesApiService;
import de.uniol.inf.is.odysseus.rest2.server.events.ServerEventType;
import de.uniol.inf.is.odysseus.rest2.server.events.ServerEventsWebsocketEndpoint;

/**
 * This class provides the implementation for the REST service
 * {@link ServicesApi} that provides some services that do not belong to other
 * REST resources.
 * 
 * @author Cornelius A. Ludmann
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T16:12:00.919Z[GMT]")
public class ServicesApiServiceImpl extends ServicesApiService {

	@Override
	public Response servicesLoginPost(Optional<ISession> ignore, User user) {
		ITenant tenant = UserManagementProvider.instance.getTenant(user.getTenant() == null ? "" : user.getTenant());
		ISession session = SessionManagement.instance.login(user.getUsername(), user.getPassword().getBytes(), tenant);
		if (session != null) {
			final Token token = new Token();
			token.setToken(session.getToken());
			return Response.ok().entity(token).build();
		} else {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	@Override
	public Response servicesValidateSession(Optional<ISession> session) {
		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		final Token token = new Token();
		token.setToken(session.get().getToken());
		return Response.ok().entity(token).build();

	}

	@Override
	public Response servicesOutputschemaPost(Optional<ISession> session, Query query, Integer port) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		IServerExecutor executor = ExecutorServiceBinding.getExecutor();
		final Set<String> supportedParsers = executor.getSupportedQueryParsers(session.get());

		if (query == null) {
			return Response.status(Status.BAD_REQUEST).entity("Query needs to be not null.").type(MediaType.TEXT_PLAIN)
					.build();
		}
		
		SDFSchema schema;
	
		if (query.getId() != null) {
			schema = executor.getOutputSchema(query.getId(), session.get());
		} else {

			if (query.getParser() == null || !supportedParsers.contains(query.getParser())) {
				return Response.status(Status.BAD_REQUEST)
						.entity("Parser needs to be one of these: " + String.join(", ", supportedParsers))
						.type(MediaType.TEXT_PLAIN).build();
			}
			if (query.getQueryText() == null) {
				return Response.status(Status.BAD_REQUEST).entity("Query text needs to be not null.")
						.type(MediaType.TEXT_PLAIN).build();
			}
			if (query.getRootOperators() != null && !query.getRootOperators().isEmpty()) {
				return Response.status(Status.BAD_REQUEST).entity("Setting root operators is not allowed.")
						.type(MediaType.TEXT_PLAIN).build();
			}
			if (query.getState() != null) {
				return Response.status(Status.BAD_REQUEST).entity("Setting a state is not allowed.")
						.type(MediaType.TEXT_PLAIN).build();
			}

			schema = executor.determineOutputSchema(query.getQueryText(), query.getParser(), session.get(),
					port, Context.empty());
			
		}
		Schema result = DatatypesApiServiceImpl.transform(schema);
		return Response.ok().entity(result).build();
	}

	@Override
	public Response servicesBundlesGet(Optional<ISession> session, String filter) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		final BundleContext context = Application.getContext();
		final Bundle[] bundles = context.getBundles();
		Stream<Bundle> stream = Arrays.stream(bundles);
		if (filter != null && !filter.trim().equals("")) {
			stream = stream.filter(bundle -> bundle.getSymbolicName().contains(filter));
		}
		final List<BundleInfo> bundleInfos = stream.map(ServicesApiServiceImpl::transform).collect(Collectors.toList());
		return Response.ok().entity(bundleInfos).build();
	}

	private static BundleInfo transform(Bundle bundle) {
		final BundleInfo result = new BundleInfo();
		result.setBundleId(bundle.getBundleId());
		result.setLastModified(bundle.getLastModified());
		switch (bundle.getState()) {
		case Bundle.ACTIVE:
			result.setState(BundleInfo.StateEnum.ACTIVE);
			break;
		case Bundle.INSTALLED:
			result.setState(BundleInfo.StateEnum.INSTALLED);
			break;
		case Bundle.RESOLVED:
			result.setState(BundleInfo.StateEnum.RESOLVED);
			break;
		case Bundle.UNINSTALLED:
			result.setState(BundleInfo.StateEnum.UNINSTALLED);
			break;
		case Bundle.STARTING:
			result.setState(BundleInfo.StateEnum.STARTING);
			break;
		case Bundle.STOPPING:
			result.setState(BundleInfo.StateEnum.STOPPING);
			break;
		default:
			break;
		}
		result.setSymbolicName(bundle.getSymbolicName());
		if (bundle.getVersion() != null) {
			result.setVersion(bundle.getVersion().toString());
		}
		return result;
	}

	@Override
	public Response servicesEventsGet(Optional<ISession> session) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		final List<EventWebSocket> result = new ArrayList<>();

		/*
		 * Add all ServerEventTypes and their descriptions from the enum and create the
		 * URI where the WebSocket for the respective event can be accessed.
		 */
		for (ServerEventType type : ServerEventType.values()) {
			final EventWebSocket eventWebSocket = new EventWebSocket();
			eventWebSocket.setType(type.toString());
			eventWebSocket.setDescription(type.description());
			String webSocketUri = ServerEventsWebsocketEndpoint.SERVER_ENDPOINT_URI.replace("{type}", type.toString())
					.replace("{securityToken}", session.get().getToken());
			eventWebSocket.setWebsocketUri(webSocketUri);
			result.add(eventWebSocket);
		}

		return Response.ok().entity(result).build();
	}
}
