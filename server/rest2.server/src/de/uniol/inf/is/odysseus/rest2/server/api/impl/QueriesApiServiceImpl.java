package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.Query;
import de.uniol.inf.is.odysseus.rest2.common.model.QueryPorts;
import de.uniol.inf.is.odysseus.rest2.common.model.QueryRootOperators;
import de.uniol.inf.is.odysseus.rest2.common.model.QueryWebsockets;
import de.uniol.inf.is.odysseus.rest2.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest2.server.api.QueriesApiService;
import de.uniol.inf.is.odysseus.rest2.server.query.QueryResultWebsocketEndpoint;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParser;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T16:12:00.919Z[GMT]")
public class QueriesApiServiceImpl extends QueriesApiService {
	
	private static final Logger LOG = LoggerFactory.getLogger(QueriesApiServiceImpl.class);

	static Optional<Query> getQuery(ISession session, int queryid, boolean includeAllOps) {
		if (includeAllOps) {
			LOG.warn("Currently, only root operators are supported!");
			includeAllOps = false;
		}

		
		IServerExecutor executor = ExecutorServiceBinding.getExecutor();
		if (executor != null) {
			ILogicalQuery logicalQuery = executor.getLogicalQueryById(queryid, session);
			if (logicalQuery == null) {
				return Optional.empty();
			}
			Query query = new Query();
			query.setId(queryid);
			if (logicalQuery.getName() != null) {
				query.setName(String.valueOf(logicalQuery.getName()));
			}
			query.setParser(logicalQuery.getParserId());
			query.setQueryText(logicalQuery.getQueryText());
			QueryState state = executor.getQueryState(queryid, session);
			query.setState(state.name());

			Set<String> protocols = QueryResultWebsocketEndpoint.protocols();
			Collection<IPhysicalOperator> physicalRoots;
			if (includeAllOps) {
				physicalRoots = executor.getPhysicalQueryByString(queryid+"", session).getAllOperators();
			}else {
				physicalRoots = executor.getPhysicalRoots(queryid, session);
			}
		
			physicalRoots.forEach(physicalRoot -> {

				QueryRootOperators queryRootOperator = new QueryRootOperators();
				String operatorId = String.valueOf(physicalRoot.getUUID());
				queryRootOperator.setOperatorName(operatorId);
				
				queryRootOperator.setOperatorDisplayName(physicalRoot.getName());
				
				if (physicalRoot.getLogicalOperator() != null) {
					LogicalOperator annotation = physicalRoot.getLogicalOperator().getClass()
							.getAnnotation(LogicalOperator.class);
					if (annotation != null) {
						queryRootOperator.setOperatorType(annotation.name());
					}
				}
				
				queryRootOperator.setOperatorImplementation(physicalRoot.getClass().getSimpleName());

				physicalRoot.getOutputSchemas().keySet().forEach(port -> {

					QueryPorts queryPort = new QueryPorts();
					queryPort.setPort(port);
					queryPort.setSchema(DatatypesApiServiceImpl.transform(physicalRoot.getOutputSchema(port)));

					protocols.forEach(protocol -> {
						QueryWebsockets queryWebsocket = new QueryWebsockets();
						queryWebsocket.setProtocol(protocol);
						queryWebsocket.setUri(QueryResultWebsocketEndpoint.toWebsocketUrl(session, queryid, operatorId,
								port, protocol));
						queryPort.addWebsocketsItem(queryWebsocket);
					});

					queryRootOperator.addPortsItem(queryPort);
				});

				query.addRootOperatorsItem(queryRootOperator);
			});

			return Optional.of(query);
		}

		return Optional.empty();
	}

	@Override
	public Response queriesGet(Optional<ISession> session) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		IServerExecutor executor = ExecutorServiceBinding.getExecutor();
		Collection<Integer> queryIds = executor.getLogicalQueryIds(session.get());
		List<Query> queries = queryIds.stream().map(x -> getQuery(session.get(), x, false)).filter(Optional::isPresent)
				.map(Optional::get).collect(Collectors.toList());
		return Response.ok().entity(queries).build();
	}

	@Override
	public Response queriesIdDelete(Optional<ISession> session, Integer id) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		IServerExecutor executor = ExecutorServiceBinding.getExecutor();

		if (executor.getLogicalQueryById(id, session.get()) == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		executor.removeQuery(id, session.get());

		if (executor.getLogicalQueryById(id, session.get()) == null) {
			return Response.ok().build();
		} else {
			// query still exists, should never happen
			return Response.serverError().entity("Query still exists after attempt to remove. This is unexpected.")
					.type(MediaType.TEXT_PLAIN).build();
		}
	}

	@Override
	public Response queriesIdGet(Optional<ISession> session, Integer id) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		Optional<Query> query = getQuery(session.get(), id, true);
		if (query.isPresent()) {
			return Response.ok().entity(query.get()).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@Override
	public Response queriesIdPut(Optional<ISession> session, Integer id, Query query) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		if (query.getId() != null && !query.getId().equals(id)) {
			return Response.status(Status.BAD_REQUEST)
					.entity(String.format(
							"Query ID (%s) needs to be null or the same as the path parameter value (%s).",
							String.valueOf(query.getId()), String.valueOf(id)))
					.type(MediaType.TEXT_PLAIN).build();
		}

		IServerExecutor executor = ExecutorServiceBinding.getExecutor();
		ILogicalQuery logicalQuery = executor.getLogicalQueryById(id, session.get());

		if (logicalQuery == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		if (query.getName() != null && logicalQuery.getName() == null) {
			return Response.status(Status.BAD_REQUEST).entity("Setting a query name is not allowed.")
					.type(MediaType.TEXT_PLAIN).build();
		}

		if (query.getName() != null && !query.getName().equals(logicalQuery.getName().getResourceName())) {
			return Response.status(Status.BAD_REQUEST).entity("Changing the query name is not allowed.")
					.type(MediaType.TEXT_PLAIN).build();
		}

		if (query.getParser() != null && !query.getParser().equals(logicalQuery.getParserId())) {
			return Response.status(Status.BAD_REQUEST).entity("Changing the parser is not allowed.")
					.type(MediaType.TEXT_PLAIN).build();
		}

		if (query.getQueryText() != null && !query.getQueryText().equals(logicalQuery.getQueryText())) {
			return Response.status(Status.BAD_REQUEST).entity("Changing the query text is not allowed.")
					.type(MediaType.TEXT_PLAIN).build();
		}

		if (query.getRootOperators() != null && !query.getRootOperators().isEmpty()) {
			return Response.status(Status.BAD_REQUEST)
					.entity("Changing root operators is not allowed. Don't set these values at all.")
					.type(MediaType.TEXT_PLAIN).build();
		}

		QueryState currentState = executor.getQueryState(id, session.get());

		if (query.getState() != null && !query.getState().equals(currentState.name())) {
			QueryState desiredState;
			try {
				desiredState = QueryState.valueOf(query.getState());
			} catch (IllegalArgumentException e) {
				String values = Arrays.asList(QueryState.values()).stream().map(QueryState::name)
						.collect(Collectors.joining(", "));
				return Response.status(Status.BAD_REQUEST).entity("State needs to be one of these: " + values)
						.type(MediaType.TEXT_PLAIN).build();
			}
			switch (desiredState) {
			case INACTIVE:
				executor.stopQuery(id, session.get());
				break;
			case PARTIAL:
				// TODO implement state change to PARTIAL (find a way to pass option
				// sheddingFactor via REST)
//				int sheddingFactor;
//				executor.partial(id, sheddingFactor, session.get());
				return Response.status(Status.BAD_REQUEST)
						.entity("Changing state to " + QueryState.PARTIAL.name() + " is not supported yet.")
						.type(MediaType.TEXT_PLAIN).build();
			case PARTIAL_SUSPENDED:
				// TODO implement state change to PARTIAL_SUSPENDED (find a way to pass option
				// sheddingFactor via REST)
//				int sheddingFactor;
//				executor.partialQuery(id, sheddingFactor, session.get());
				return Response.status(Status.BAD_REQUEST)
						.entity("Changing state to " + QueryState.PARTIAL_SUSPENDED.name() + " is not supported yet.")
						.type(MediaType.TEXT_PLAIN).build();
			case RUNNING:
				executor.startQuery(id, session.get());
				break;
			case SUSPENDED:
				executor.suspendQuery(id, session.get());
				break;
			case UNDEF:
				return Response.status(Status.BAD_REQUEST)
						.entity("Changing state to " + QueryState.UNDEF.name() + " is not supported.")
						.type(MediaType.TEXT_PLAIN).build();
			}

			QueryState newState = executor.getQueryState(id, session.get());
			if (!newState.equals(desiredState)) {
				return Response.serverError()
						.entity(String.format(
								"Query state is now '%s' after the attempt to change it to '%s'. This is unexpected.",
								newState.name(), desiredState.name()))
						.type(MediaType.TEXT_PLAIN).build();
			}
		}

		return Response.ok().entity(getQuery(session.get(), id, true)).build();
	}

	/**
	 * This helper method calls the function given as {@code delegate} with the
	 * session object and the query id that is given as string or that belongs to
	 * the query with the given name.
	 * 
	 * @param session  The session object.
	 * @param idOrName The numerical ID as string or the name of the query.
	 * @param delegate The function which should be called with the appropriate
	 *                 query ID.
	 * @return The response given by the delegate function or FORBIDDEN, when the
	 *         session is not present or NOT_FOUND if no query with the given name
	 *         exists.
	 */
	private static Response callWithQueryId(Optional<ISession> session, String idOrName,
			BiFunction<Optional<ISession>, Integer, Response> delegate) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		IServerExecutor executor = ExecutorServiceBinding.getExecutor();

		try {
			int id = Integer.valueOf(idOrName);
			if (executor.getLogicalQueryById(id, session.get()) != null) {
				return delegate.apply(session, id);
			}
		} catch (NumberFormatException e) {
			// do nothing
		}

		// We reach this point if the query name is not a number or there is no query
		// with the ID. In that case we try to use the value as query name.

		ILogicalQuery query = executor
				.getLogicalQueryByName(new Resource(session.get().getUser().getName() + "." + idOrName), session.get());
		if (query == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return delegate.apply(session, query.getID());
	}

	@Override
	public Response queriesNameDelete(Optional<ISession> session, String name) {
		return callWithQueryId(session, name, this::queriesIdDelete);
	}

	@Override
	public Response queriesNameGet(Optional<ISession> session, String name) {
		return callWithQueryId(session, name, this::queriesIdGet);
	}

	@Override
	public Response queriesNamePut(Optional<ISession> session, String name, Query query) {
		return callWithQueryId(session, name, (t, u) -> queriesIdPut(t, u, query));
	}

	@Override
	public Response queriesPost(Optional<ISession> session, Query query) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		final IServerExecutor executor = ExecutorServiceBinding.getExecutor();
		final Set<String> supportedParsers = executor.getSupportedQueryParsers(session.get());

		if (query == null) {
			return Response.status(Status.BAD_REQUEST).entity("Query needs to be not null.").type(MediaType.TEXT_PLAIN)
					.build();
		}
		if (query.getParser() == null || !supportedParsers.contains(query.getParser())) {
			return Response.status(Status.BAD_REQUEST)
					.entity("Parser needs to be one of these: " + String.join(", ", supportedParsers))
					.type(MediaType.TEXT_PLAIN).build();
		}
		if (query.getQueryText() == null) {
			return Response.status(Status.BAD_REQUEST).entity("Query text needs to be not null.")
					.type(MediaType.TEXT_PLAIN).build();
		}
		if (query.getId() != null) {
			return Response.status(Status.BAD_REQUEST).entity("Setting an ID is not allowed.")
					.type(MediaType.TEXT_PLAIN).build();
		}
		if (query.getRootOperators() != null && !query.getRootOperators().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity("Setting root operators is not allowed.")
					.type(MediaType.TEXT_PLAIN).build();
		}

		QueryState queryState;
		if (query.getState() == null) {
			queryState = QueryState.INACTIVE;
		} else {
			try {
				queryState = QueryState.valueOf(query.getState());
			} catch (IllegalArgumentException e) {
				String values = Arrays.asList(QueryState.values()).stream().map(QueryState::name)
						.collect(Collectors.joining(", "));
				return Response.status(Status.BAD_REQUEST).entity("State needs to be one of these: " + values)
						.type(MediaType.TEXT_PLAIN).build();
			}
		}

		String parser = query.getParser();
		String queryText = query.getQueryText();

		if (!parser.equals(OdysseusScriptParser.PARSER_NAME)) {
			StringBuilder queryTextBuilder = new StringBuilder();
			queryTextBuilder.append("#PARSER ").append(parser).append(System.lineSeparator());
			if (query.getName() != null) {
				queryTextBuilder.append("#QNAME ").append(query.getName()).append(System.lineSeparator());
			}
			if (queryState.equals(QueryState.RUNNING)) {
				queryTextBuilder.append("#RUNQUERY").append(System.lineSeparator());
			} else if (queryState.equals(QueryState.INACTIVE)) {
				queryTextBuilder.append("#ADDQUERY").append(System.lineSeparator());
			} else {
				// TODO: Add support for other states???
				return Response.status(Status.BAD_REQUEST)
						.entity("Adding queries with state other than " + QueryState.RUNNING.name() + " or "
								+ QueryState.INACTIVE.name() + " is currently not supported.")
						.build();
			}
			queryTextBuilder.append(queryText);
			queryText = queryTextBuilder.toString();
			parser = OdysseusScriptParser.PARSER_NAME;
		}

		// TODO: Is there a way to catch errors caused by deficient scripts? In this
		// case returning a 4xx status code would be more appropriate.

		Collection<Integer> queryIds = executor.addQuery(queryText, parser, session.get(), new Context());
		List<Query> queries = queryIds.stream().map(x -> getQuery(session.get(), x, false)).filter(Optional::isPresent)
				.map(Optional::get).collect(Collectors.toList());

		return Response.ok().entity(queries).build();
	}
}
