package de.uniol.inf.is.odysseus.planmanagement.executor.rest.client;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.client.common.ClientSession;
import de.uniol.inf.is.odysseus.client.common.ClientSessionStore;
import de.uniol.inf.is.odysseus.client.common.ClientUser;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.mep.IFunctionSignatur;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.ClientReceiver;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.SinkInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IClientExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype.KindOfDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;
import de.uniol.inf.is.odysseus.rest2.client.ApiException;
import de.uniol.inf.is.odysseus.rest2.client.RestService;
import de.uniol.inf.is.odysseus.rest2.client.api.DefaultApi;
import de.uniol.inf.is.odysseus.rest2.common.model.Attribute;
import de.uniol.inf.is.odysseus.rest2.common.model.Datatype;
import de.uniol.inf.is.odysseus.rest2.common.model.Metaschema;
import de.uniol.inf.is.odysseus.rest2.common.model.Query;
import de.uniol.inf.is.odysseus.rest2.common.model.QueryWebsockets;
import de.uniol.inf.is.odysseus.rest2.common.model.Schema;
import de.uniol.inf.is.odysseus.rest2.common.model.Token;
import de.uniol.inf.is.odysseus.rest2.common.model.User;
import de.uniol.inf.is.odysseus.wrapper.websocket.WebSocketClientTransportHandler;

/**
 * Client that uses the new REST2 interface to communicate with the Odysseus
 * Server
 * 
 * @author Marco Grawunder
 *
 */

public class RESTClient implements IClientExecutor, IExecutor, IOperatorOwner {

	Logger LOG = LoggerFactory.getLogger(RESTClient.class);

	@SuppressWarnings("rawtypes")
	private Map<String, Map<Integer, ClientReceiver>> receivers = new HashMap<>();
	@SuppressWarnings("rawtypes")
	private Map<ClientReceiver, Integer> opReceivers = new HashMap<>();

	final Map<String, List<IUpdateEventListener>> updateEventListener = new HashMap<>();
	final long UPDATEINTERVAL = 60000;

	// Fire update events --> TODO: Get Events from Server and fire
	private class Runner extends Thread {

		@Override
		public void run() {
			while (true) {
				try {
					synchronized (this) {
						wait(UPDATEINTERVAL);
					}
					fireAllUpdateEvents();
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}
			}
		}
	};

	private void fireAllUpdateEvents() {
		RESTClient.this.fireUpdateEvent(IUpdateEventListener.DATADICTIONARY);
		RESTClient.this.fireUpdateEvent(IUpdateEventListener.QUERY);
		RESTClient.this.fireUpdateEvent(IUpdateEventListener.SCHEDULING);
		RESTClient.this.fireUpdateEvent(IUpdateEventListener.SESSION);
		RESTClient.this.fireUpdateEvent(IUpdateEventListener.USER);
	}

	private Runner generateEvents = new Runner();

	@Override
	public synchronized void addUpdateEventListener(IUpdateEventListener listener, String type, ISession session) {
		List<IUpdateEventListener> l = updateEventListener.get(type);
		if (l == null) {
			l = new CopyOnWriteArrayList<>();
			this.updateEventListener.put(type, l);
		}
		l.add(listener);
	}

	@Override
	public void removeUpdateEventListener(IUpdateEventListener listener, String type, ISession session) {
		if (session != null) {
			List<IUpdateEventListener> l = updateEventListener.get(type);
			if (l != null) {
				l.remove(listener);
				if (l.isEmpty()) {
					updateEventListener.get(session.getConnectionName()).remove(listener);
				}
			}
		}
	}

	private void fireUpdateEvent(String type) {
		List<IUpdateEventListener> list = updateEventListener.get(type);
		if (list != null) {
			for (IUpdateEventListener l : list) {
				l.eventOccured(type);
			}
		}
	}

	@Override
	public ISession login(String username, byte[] password, String tenantname, String host, int port, String instance) {
		String connectString = host + ":" + port;

		RestService restService = new RestService();
		restService.setBasePath(connectString);
		restService.setUsername(username);
		restService.setPassword(new String(password));
		DefaultApi api = new DefaultApi(restService);

		if (!generateEvents.isAlive()) {
			generateEvents.start();
		}

		User user = new User();
		user.setUsername(username);
		user.setPassword(new String(password));
		user.setTenant(tenantname);

		Token token = null;

		try {
			token = api.servicesLoginPost(user);
		} catch (Exception e) {
			LOG.error("Login error", e);
		}

		if (token == null) {
			return null;
		}
		IUser clientUser = new ClientUser(username, password, true);
		ClientSession session = new ClientSession(clientUser, tenantname, connectString);
		session.setToken(token.getToken());
		ClientSessionStore.addSession(connectString, session);
		ClientSessionStore.putSessionContext(session, api);
		fireUpdateEvent(IUpdateEventListener.SESSION);
		return session;
	}

	public DefaultApi getAPI(ISession session) {
		try {
			if (session instanceof ClientSession) {
				ClientUser clientUser = (ClientUser) ((ClientSession) session).getUser();

				User user = new User();
				user.setUsername(clientUser.getName());
				user.setPassword(new String(clientUser.getPassword()));
				user.setTenant(((ClientSession) session).getTenantName());

				DefaultApi api = (DefaultApi) ClientSessionStore.getSessionContext(session);

				Token token = api.servicesLoginPost(user);
				// In case of new login the security token changes -->
				// Update old session object
				((ClientSession) session).setToken(token.getToken());

				return api;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		throw new IllegalArgumentException("Session is ClientSession");
	}

	@Override
	public List<SocketAddress> getSocketConnectionInformation(int queryId, ISession caller) {
		throw new RuntimeException("This method cannot be used in REST Scenario");
	}

	@Override
	public void removeQuery(int queryID, ISession caller) {
		DefaultApi api = getAPI(caller);
		try {
			api.queriesIdDelete(queryID);
		} catch (ApiException e) {
			throw new PlanManagementException(e);
		}
	}

	@Override
	public void removeQuery(Resource queryName, ISession caller) {
		DefaultApi api = getAPI(caller);
		try {
			api.queriesNameDelete(String.valueOf(queryName));
		} catch (ApiException e) {
			throw new PlanManagementException(e);
		}

	}

	// FIXME: Calls depends on current state (so maybe simple to set new state is
	// not not right decision)
	// QueryState.PARTIAL_SUSPENDED is currently not reachable

	@Override
	public void startQuery(int queryID, ISession caller) {
		changeQueryState(queryID, getAPI(caller), String.valueOf(QueryState.RUNNING));
	}

	@Override
	public void stopQuery(int queryID, ISession caller) {
		changeQueryState(queryID, getAPI(caller), String.valueOf(QueryState.INACTIVE));
	}

	@Override
	public void suspendQuery(int queryID, ISession caller) {
		changeQueryState(queryID, getAPI(caller), String.valueOf(QueryState.SUSPENDED));
	}

	@Override
	public void resumeQuery(int queryID, ISession caller) {
		changeQueryState(queryID, getAPI(caller), String.valueOf(QueryState.RUNNING));
	}

	@Override
	public void partialQuery(int queryID, int sheddingFactor, ISession caller) {
		changeQueryState(queryID, getAPI(caller), String.valueOf(QueryState.PARTIAL));
	}

	private void changeQueryState(int queryID, DefaultApi api, String newState) {
		try {
			Query query = new Query().id(queryID).state(newState);
			api.queriesIdPut(queryID, query);
			RESTClient.this.fireUpdateEvent(IUpdateEventListener.QUERY);
		} catch (ApiException e) {
			throw new PlanManagementException(e);
		}
	}

	@Override
	public void startQuery(Resource queryName, ISession caller) {
		changeQueryState(queryName, getAPI(caller), String.valueOf(QueryState.RUNNING));
	}

	@Override
	public void stopQuery(Resource queryName, ISession caller) {
		changeQueryState(queryName, getAPI(caller), String.valueOf(QueryState.INACTIVE));
	}

	@Override
	public void suspendQuery(Resource queryName, ISession caller) {
		changeQueryState(queryName, getAPI(caller), String.valueOf(QueryState.SUSPENDED));
	}

	@Override
	public void resumeQuery(Resource queryName, ISession caller) {
		changeQueryState(queryName, getAPI(caller), String.valueOf(QueryState.RUNNING));
	}

	@Override
	public void partialQuery(Resource queryName, int sheddingFactor, ISession caller) {
		changeQueryState(queryName, getAPI(caller), String.valueOf(QueryState.PARTIAL));
	}

	private void changeQueryState(Resource queryName, DefaultApi api, String newState) {
		try {
			Query query = new Query().name(String.valueOf(queryName)).state(newState);
			api.queriesNamePut(String.valueOf(queryName), query);
			RESTClient.this.fireUpdateEvent(IUpdateEventListener.QUERY);
		} catch (ApiException e) {
			throw new PlanManagementException(e);
		}
	}

	// ------------------------------------------
	// Most Operator owner methods can be ignored
	// ------------------------------------------

	@Override
	public int compareTo(IOperatorOwner arg0) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int getID() {
		return 0;
	}

	@Override
	public ISession getSession() {
		return null;
	}

	@Override
	public void done(IOwnedOperator op) {
		// Ignored
	}

	@Override
	public Collection<String> getQueryBuildConfigurationNames(ISession session) {
		return Collections.emptyList();
	}

	@Override
	public Set<String> getSupportedQueryParsers(ISession session) {
		Set<String> parsers = new TreeSet<>();
		DefaultApi api = getAPI(session);
		try {
			parsers.addAll(api.parsersGet());
		} catch (ApiException e) {
			LOG.warn("Error reading parsers", e);
		}
		return parsers;
	}

	@Override
	public Set<String> getMetadataNames(ISession session) {
		DefaultApi api = getAPI(session);
		// TODO: Create on Server side
		return Collections.emptySet();
	}

	@Override
	public Map<String, List<String>> getQueryParserTokens(String queryParser, ISession user) {
		DefaultApi api = getAPI(user);
		// TODO: get from server!
		Map<String, List<String>> tokens = new HashMap<>();
		tokens.put("OdysseusScript", Collections.emptyList());
		tokens.put("PQL", Collections.emptyList());

		return tokens;
	}

	@Override
	public List<String> getQueryParserSuggestions(String queryParser, String hint, ISession user) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public Collection<Integer> addQuery(String query, String parserID, ISession user, Context context) {
		Collection<Integer> createdQueries = new ArrayList<>();
		DefaultApi api = getAPI(user);
		Query queryModel = new Query();
		queryModel.setQueryText(query);
		queryModel.setParser(parserID);

		try {
			List<Query> queries = api.queriesPost(queryModel);
			for (Query q : queries) {
				createdQueries.add(q.getId());
			}
		} catch (ApiException e) {
			throw new PlanManagementException(e);
		}

		fireAllUpdateEvents();

		return createdQueries;
	}

	@Override
	public void runCommand(String commandExpression, ISession caller) {
		throw new RuntimeException("Not implemented for REST");
	}

	@Override
	public SDFSchema determineOutputSchema(String query, String parserID, ISession user, int port, Context context) {
		DefaultApi api = getAPI(user);
		Query q = new Query();
		q.setQueryText(query);
		q.setParser(parserID);

		try {
			Schema schema = api.servicesOutputschemaPost(q, port);
			return toSDFSchema(schema);
		} catch (ApiException e) {
			LOG.warn("Error resolving output schema for query ", e);
		}

		return null;
	}

	@Override
	public ILogicalQuery getLogicalQueryById(int id, ISession session) {
		Query q = getQuery(id, session);
		return toLogicalQuery(id, session, q);
	}

	private ILogicalQuery toLogicalQuery(int id, ISession session, Query q) {
		ILogicalQuery query = new LogicalQuery(id);
		if (q.getName() != null) {
			query.setName(new Resource(q.getName()));
		}
		query.setParserId(q.getParser());
		query.setQueryText(q.getQueryText());
		query.setParameter("STATE", QueryState.valueOf(q.getState()));
		// FIXME: Query needs to have an own user
		query.setUser(session);
		return query;
	}

	@Override
	public ILogicalQuery getLogicalQueryByName(Resource name, ISession session) {
		Query q = getQuery(name, session);
		return toLogicalQuery(q.getId(), session, q);
	}

	@Override
	public ILogicalQuery getLogicalQueryByString(String idOrName, ISession session) {
		Query q = getQuery(idOrName, session);
		return toLogicalQuery(q.getId(), session, q);
	}

	@Override
	public Collection<Integer> getLogicalQueryIds(ISession session) {
		DefaultApi api = getAPI(session);
		Collection<Integer> queryIDs = new ArrayList<>();
		try {
			List<Query> queries = api.queriesGet();
			for (Query q : queries) {
				queryIDs.add(q.getId());
			}

		} catch (ApiException e) {
			throw new PlanManagementException(e);
		}
		return queryIDs;
	}

	@Override
	public QueryState getQueryState(int queryID, ISession session) {
		Query query = getQuery(queryID, session);
		if (query != null) {
			return QueryState.valueOf(query.getState());
		}
		return null;
	}

	private Query getQuery(int queryID, ISession session) {
		DefaultApi api = getAPI(session);
		Query query;
		try {
			query = api.queriesIdGet(queryID);
		} catch (ApiException e) {
			throw new PlanManagementException(e);
		}
		return query;
	}

	private Query getQuery(Resource name, ISession session) {
		return getQuery(String.valueOf(name), session);
	}

	private Query getQuery(String name, ISession session) {
		DefaultApi api = getAPI(session);
		Query query;
		try {
			query = api.queriesNameGet(name);
		} catch (ApiException e) {
			throw new PlanManagementException(e);
		}
		return query;
	}

	@Override
	public QueryState getQueryState(String queryName, ISession session) {
		Query query = getQuery(queryName, session);
		if (query != null) {
			return QueryState.valueOf(query.getState());
		}
		return null;
	}

	@Override
	public List<QueryState> getQueryStates(List<Integer> ids, List<ISession> session) {
		List<QueryState> states = new ArrayList<>();
		// TODO: why list of sessions?
		for (int i = 0; i < ids.size(); i++) {
			Query q = getQuery(ids.get(i), session.get(i));
			states.add(QueryState.valueOf(q.getState()));
		}
		return states;
	}

	@Override
	public List<IPhysicalOperator> getPhysicalRoots(int queryID, ISession session) {
		List<IPhysicalOperator> roots = new ArrayList<>();
		Optional<ClientReceiver> receiver = createClientReceiver(queryID, session);
		if (receiver.isPresent()) {
			roots.add(receiver.get());
		}
		return roots;
	}

	private Optional<ClientReceiver> createClientReceiver(int queryId, ISession caller) {
		DefaultApi api = getAPI(caller);

		if (receivers.get(caller.getConnectionName()) != null
				&& receivers.get(caller.getConnectionName()).containsKey(queryId)) {
			return Optional.of(receivers.get(caller.getConnectionName()).get(queryId));
		}

		SDFSchema outputSchema = getOutputSchema(queryId, caller);
		String type = outputSchema.getType().getSimpleName();
		IStreamObjectDataHandler<?> dataHandler = DataHandlerRegistry.instance.getStreamObjectDataHandler(type,
				outputSchema);

		if (dataHandler == null) {
			throw new RuntimeException("Cannot find data handler for type " + type);
		}

		// TODO: Find generic way for meta data ... problem: currently, meta data handling is part of server only
		if (!outputSchema.getMetaschema().isEmpty()) {
	
			if (outputSchema.getMetaschema().get(0).getMetaAttribute() != ITimeInterval.class) {
				throw new RuntimeException("Sorry. Currently only metadata with time interval can be used for client/server communication");
			}
		}
		// set fix time interval meta attribute
		dataHandler.setMetaAttribute(new TimeInterval());
			
		// TODO: Switch to binary

		// TODO: Handle cases with multiple roots
		Query query = getQuery(queryId, caller);
		OptionMap options = new OptionMap();
		List<QueryWebsockets> wsDefs = query.getRootOperators().get(0).getPorts().get(0).getWebsockets();
		for (QueryWebsockets wsDef : wsDefs) {
			if ("tuple".equalsIgnoreCase(type)) {
				if ("csv".equalsIgnoreCase(wsDef.getProtocol())) {
					options.setOption("uri", caller.getConnectionName().replace("http", "ws") + wsDef.getUri());
				}
			} else if ("keyvalue".equalsIgnoreCase(type)) {
				if ("json".equalsIgnoreCase(wsDef.getProtocol()) && "keyvalue".equalsIgnoreCase(type)) {
					options.setOption("uri", caller.getConnectionName().replace("http", "ws") + wsDef.getUri());
				}
			} else {
				if ("binary".equalsIgnoreCase(wsDef.getProtocol())) {
					options.setOption("uri", caller.getConnectionName().replace("http", "ws") + wsDef.getUri());
				}
			}
		}

		final IProtocolHandler h;

		if ("tuple".equalsIgnoreCase(type)) {
			h = ProtocolHandlerRegistry.instance.getInstance("CSV", ITransportDirection.IN, IAccessPattern.PUSH,
					options, dataHandler);
		} else if ("keyvalue".equalsIgnoreCase(type)) {
			h = ProtocolHandlerRegistry.instance.getInstance("JSON", ITransportDirection.IN, IAccessPattern.PUSH,
					options, dataHandler);
		} else {
			h = ProtocolHandlerRegistry.instance.getInstance("Odysseus", ITransportDirection.IN, IAccessPattern.PUSH,
					options, dataHandler);
		}
		// Must be done to add the transport to the protocoll ... seems not
		// really intuitive ...
		ITransportHandler th = TransportHandlerRegistry.instance.getInstance(WebSocketClientTransportHandler.NAME, h,
				options);
		if (th == null) {
			throw new RuntimeException("Fatal! WebSocketClientTransportHandler not found!");
		}
		h.setTransportHandler(th);
		ClientReceiver receiver = new ClientReceiver(h);
		receiver.setOutputSchema(outputSchema);
		receiver.open(null, 0, 0, null, null);
		receiver.addOwner(this);
		Map<Integer, ClientReceiver> r = receivers.get(caller.getConnectionName());
		if (r == null) {
			r = new HashMap<>();
			receivers.put(caller.getConnectionName(), r);
		}
		r.put(queryId, receiver);
		// TODO: FIXME for multiple server version
		opReceivers.put(receiver, queryId);

		return Optional.of(receiver);

	}

	@Override
	public Collection<Integer> startAllClosedQueries(ISession user) {

		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public Set<String> getRegisteredBufferPlacementStrategiesIDs(ISession session) {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public Set<String> getRegisteredSchedulingStrategies(ISession session) {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public Set<String> getRegisteredSchedulers(ISession session) {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public void setScheduler(String scheduler, String schedulerStrategy, ISession session) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCurrentSchedulerID(ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCurrentSchedulingStrategyID(ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<SDFDatatype> getRegisteredDatatypes(ISession caller) {
		DefaultApi api = getAPI(caller);
		Set<SDFDatatype> ret = new TreeSet<>();
		try {
			List<Datatype> dts = api.datatypesGet();
			for (Datatype dt : dts) {
				ret.add(toSDFDatatype(dt));
			}
		} catch (ApiException e) {
			throw new RuntimeException("Error retrieving data types from server", e);
		}

		return ret;
	}

	private SDFDatatype toSDFDatatype(Datatype dt) {
		SDFDatatype subtype = null;
		if (dt.getSubtype() != null) {
			subtype = toSDFDatatype(dt.getSubtype());
		}
		SDFSchema subSchema = null;
		if (dt.getSubschema() != null) {
			subSchema = toSDFSchema(dt.getSubschema());
		}
		KindOfDatatype kind = SDFDatatype.KindOfDatatype.valueOf(dt.getType().name());
		return new SDFDatatype(dt.getUri(), kind, subtype, subSchema);
	}

	@Override
	public Set<String> getRegisteredWrapperNames(ISession caller) {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public Set<String> getRegisteredAggregateFunctions(Class<? extends IStreamObject> datamodel, ISession caller) {
		return getRegisteredAggregateFunctions(datamodel.getSimpleName(), caller);
	}

	@Override
	public Set<String> getRegisteredAggregateFunctions(String datamodel, ISession caller) {
		DefaultApi api = getAPI(caller);
		try {
			List<List<Object>> fktn = api.aggregateFunctionsGet(datamodel);

		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Collections.emptySet();
	}

	@Override
	public String getName() {
		return "REST2Executor";
	}

	@Override
	public ISession login(String username, byte[] password, String tenantname) {
		throw new UnsupportedOperationException("Not implemented in REST");
	}

	@Override
	public ISession login(String username, byte[] password) {
		return login(username, password, "");
	}

	@Override
	public void logout(ISession caller) {
		ClientSessionStore.removeSession(caller.getConnectionName());
	}

	@Override
	public boolean isValid(ISession session) {
		return session.isValid();
	}

	@Override
	public ILogicalPlan removeSink(String name, ISession caller) {
		DefaultApi api = getAPI(caller);
		try {
			api.sinksNameDelete(name);
		} catch (ApiException e) {
			throw new PlanManagementException(e);
		}
		return null;
	}

	@Override
	public ILogicalPlan removeSink(Resource name, ISession caller) {
		return removeSink(String.valueOf(name), caller);
	}

	@Override
	public void removeViewOrStream(String name, ISession caller) {
		DefaultApi api = getAPI(caller);
		try {
			api.datastreamsNameDelete(name);
		} catch (ApiException e) {
			throw new PlanManagementException(e);
		}
	}

	@Override
	public void removeViewOrStream(Resource name, ISession caller) {
		removeViewOrStream(String.valueOf(name), caller);
	}

	@Override
	public List<ViewInformation> getStreamsAndViewsInformation(ISession caller) {
		DefaultApi api = getAPI(caller);
		List<ViewInformation> vis = new ArrayList<>();

		try {
			List<de.uniol.inf.is.odysseus.rest2.common.model.Resource> streams = api.datastreamsGet();
			for (de.uniol.inf.is.odysseus.rest2.common.model.Resource r : streams) {
				ViewInformation vi = new ViewInformation();
				vi.setName(new Resource(r.getOwner(), r.getName()));
				vi.setType(r.getType());
				vi.setOutputSchema(toSDFSchema(r.getSchema()));
				vis.add(vi);
			}
		} catch (ApiException e) {
			throw new PlanManagementException(e);
		}

		return vis;
	}

	private SDFSchema toSDFSchema(Schema schema) {
		List<SDFAttribute> sdfAttributes = convertAttributes(schema.getAttributes());
		List<SDFMetaSchema> metaSchema = new ArrayList<>();
		try {
			if (schema.getMetaschema() != null) {
				for (Metaschema s : schema.getMetaschema()) {
					Collection<SDFAttribute> metaAttributes = convertAttributes(s.getAttributes());
					metaSchema.add(SDFSchemaFactory.createNewMetaSchema(s.getUri(), Class.forName(s.getTypeClass()),
							metaAttributes,
							(Class<? extends IMetaAttribute>) Class.forName(s.getMetaattributeClass())));
				}
			}
			SDFSchema schemaWithoutMeta = SDFSchemaFactory.createNewSchema(schema.getUri(), Class.forName(schema.getTypeClass()),
					sdfAttributes);
			if (!metaSchema.isEmpty()) {
				return SDFSchemaFactory.createNewWithMetaSchema(schemaWithoutMeta, metaSchema);
			}else {
				return schemaWithoutMeta;
			}
		} catch (ClassNotFoundException e) {
			LOG.warn("Error converting schema ", e);
		}
		return null;
	}

	private List<SDFAttribute> convertAttributes(List<Attribute> attributes) {
		List<SDFAttribute> sdfAttributes = new ArrayList<>();
		for (Attribute a : attributes) {
			sdfAttributes.add(new SDFAttribute(a.getSourcename(), a.getAttributename(),
					SDFDatatype.getType(a.getDatatype().getUri())));
		}
		return sdfAttributes;
	}
	
	@Override
	public List<SinkInformation> getSinks(ISession caller) {
		DefaultApi api = getAPI(caller);
		List<SinkInformation> sinks = new ArrayList<>();

		try {
			List<de.uniol.inf.is.odysseus.rest2.common.model.Resource> sinkDefs = api.sinksGet();
			for (de.uniol.inf.is.odysseus.rest2.common.model.Resource r : sinkDefs) {
				SinkInformation si = new SinkInformation();
				si.setName(new Resource(r.getOwner(), r.getName()));
				si.setType(r.getType());
				si.setOutputSchema(toSDFSchema(r.getSchema()));
				sinks.add(si);
			}
		} catch (ApiException e) {
			throw new PlanManagementException(e);
		}

		return sinks;
	}

	@Override
	public boolean containsViewOrStream(Resource name, ISession caller) {
		throw new UnsupportedOperationException("Not available for REST");
	}

	@Override
	public boolean containsViewOrStream(String name, ISession caller) {
		throw new UnsupportedOperationException("Not available for REST");
	}

	@Override
	public void reloadStoredQueries(ISession caller) {
		throw new UnsupportedOperationException("Not available for REST");
	}

	@Override
	public SDFSchema getOutputSchema(int queryId, ISession session) {
		Query q = getQuery(queryId, session);

		Schema schema = q.getRootOperators().get(0).getPorts().get(0).getSchema();
		if (schema != null) {
			return toSDFSchema(schema);
		}
		return null;
	}

	@Override
	public void addStoredProcedure(String name, StoredProcedure proc, ISession caller) {
		throw new UnsupportedOperationException("Not available for REST");
	}

	@Override
	public void removeStoredProcedure(String name, ISession caller) {
		throw new UnsupportedOperationException("Not available for REST");
	}

	@Override
	public StoredProcedure getStoredProcedure(String name, ISession caller) {
		throw new UnsupportedOperationException("Not available for REST");
	}

	@Override
	public List<StoredProcedure> getStoredProcedures(ISession caller) {
		throw new UnsupportedOperationException("Not available for REST");
	}

	@Override
	public boolean containsStoredProcedures(String name, ISession caller) {
		throw new UnsupportedOperationException("Not available for REST");
	}

	@Override
	public List<String> getOperatorNames(ISession caller) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public List<LogicalOperatorInformation> getOperatorInformations(ISession caller) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public LogicalOperatorInformation getOperatorInformation(String name, ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IUser> getUsers(ISession caller) {
		List<IUser> users = new ArrayList<>();
		DefaultApi api = getAPI(caller);
		try {
			List<User> user = api.usersGet();
			for (User u : user) {
				users.add(convertToUser(u));
			}
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
		return users;
	}

	private IUser convertToUser(User u) {
		return new ClientUser(u.getUsername(), u.getPassword().getBytes(), true);
	}

	@Override
	public Collection<String> getUdfs() {
		return Collections.emptySet();
	}

	@Override
	public Set<IFunctionSignatur> getMepFunctions() {
		return Collections.emptySet();
	}

	@Override
	public boolean containsSink(String name, ISession caller) {
		return false;
	}

}
