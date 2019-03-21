package de.uniol.inf.is.odysseus.planmanagement.executor.rest.client;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.client.common.ClientSession;
import de.uniol.inf.is.odysseus.client.common.ClientSessionStore;
import de.uniol.inf.is.odysseus.client.common.ClientUser;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.mep.IFunctionSignatur;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;
import de.uniol.inf.is.odysseus.rest2.client.ApiException;
import de.uniol.inf.is.odysseus.rest2.client.RestService;
import de.uniol.inf.is.odysseus.rest2.client.api.DefaultApi;
import de.uniol.inf.is.odysseus.rest2.client.model.Query;
import de.uniol.inf.is.odysseus.rest2.client.model.Token;
import de.uniol.inf.is.odysseus.rest2.client.model.User;

/**
 * Client that uses the REST interface to communicate with the Odysseus Server
 * 
 * @author Marco Grawunder
 *
 */

public class RESTClient implements IClientExecutor, IExecutor, IOperatorOwner {

	Logger LOG = LoggerFactory.getLogger(RESTClient.class);

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
					updateEventListener.get(session.getConnectionName()).remove(l);
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

		// Store rest service ...

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeQuery(int queryID, ISession caller) throws PlanManagementException {
		DefaultApi api = getAPI(caller);
		try {
			api.queriesIdDelete(queryID);
		} catch (ApiException e) {
			throw new PlanManagementException(e);
		}
	}

	@Override
	public void removeQuery(Resource queryName, ISession caller) throws PlanManagementException {
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
	public void startQuery(int queryID, ISession caller) throws PlanManagementException {
		changeQueryState(queryID, getAPI(caller), String.valueOf(QueryState.RUNNING));
	}

	@Override
	public void stopQuery(int queryID, ISession caller) throws PlanManagementException {
		changeQueryState(queryID, getAPI(caller), String.valueOf(QueryState.INACTIVE));
	}

	@Override
	public void suspendQuery(int queryID, ISession caller) throws PlanManagementException {
		changeQueryState(queryID, getAPI(caller), String.valueOf(QueryState.SUSPENDED));
	}

	@Override
	public void resumeQuery(int queryID, ISession caller) throws PlanManagementException {
		changeQueryState(queryID, getAPI(caller), String.valueOf(QueryState.RUNNING));
	}

	@Override
	public void partialQuery(int queryID, int sheddingFactor, ISession caller) throws PlanManagementException {
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
	public void startQuery(Resource queryName, ISession caller) throws PlanManagementException {
		changeQueryState(queryName, getAPI(caller), String.valueOf(QueryState.RUNNING));
	}

	@Override
	public void stopQuery(Resource queryName, ISession caller) throws PlanManagementException {
		changeQueryState(queryName, getAPI(caller), String.valueOf(QueryState.INACTIVE));
	}

	@Override
	public void suspendQuery(Resource queryName, ISession caller) throws PlanManagementException {
		changeQueryState(queryName, getAPI(caller), String.valueOf(QueryState.SUSPENDED));
	}

	@Override
	public void resumeQuery(Resource queryName, ISession caller) throws PlanManagementException {
		changeQueryState(queryName, getAPI(caller), String.valueOf(QueryState.RUNNING));
	}

	@Override
	public void partialQuery(Resource queryName, int sheddingFactor, ISession caller) throws PlanManagementException {
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

	@Override
	public int compareTo(IOperatorOwner arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ISession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void done(IOwnedOperator op) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<String> getQueryBuildConfigurationNames(ISession session) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public Set<String> getSupportedQueryParsers(ISession session) throws PlanManagementException {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public Set<String> getMetadataNames(ISession session) {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public Map<String, List<String>> getQueryParserTokens(String queryParser, ISession user) {
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
	public Collection<Integer> addQuery(String query, String parserID, ISession user, Context context)
			throws PlanManagementException {
		Collection<Integer> createdQueries = new ArrayList<>();
		DefaultApi api = getAPI(user);
		Query queryModel = new Query();
		queryModel.setQueryText(query);
		queryModel.setParser(parserID);

		try {
			List<Query> queries = api.queriesPost(queryModel);
			for(Query q:queries) {
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
		// TODO Auto-generated method stub

	}

	@Override
	public SDFSchema determineOutputSchema(String query, String parserID, ISession user, int port, Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILogicalQuery getLogicalQueryById(int id, ISession session) {
		Query q = getQuery(id, session);
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
		// TODO Auto-generated method stub
		return null;
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
		for (int i=0;i<ids.size();i++) {
			Query q = getQuery(ids.get(i), session.get(i));
			states.add(QueryState.valueOf(q.getState()));
		}
		return states;
	}

	@Override
	public List<IPhysicalOperator> getPhysicalRoots(int queryID, ISession session) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
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
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public Set<String> getRegisteredWrapperNames(ISession caller) {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public Set<String> getRegisteredAggregateFunctions(Class<? extends IStreamObject> datamodel, ISession caller) {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public Set<String> getRegisteredAggregateFunctions(String datamodel, ISession caller) {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISession login(String username, byte[] password, String tenantname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISession login(String username, byte[] password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void logout(ISession caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid(ISession session) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ILogicalPlan removeSink(String name, ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILogicalPlan removeSink(Resource name, ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeViewOrStream(String name, ISession caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeViewOrStream(Resource name, ISession caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ViewInformation> getStreamsAndViewsInformation(ISession caller) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public List<SinkInformation> getSinks(ISession caller) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public boolean containsViewOrStream(Resource name, ISession caller) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsViewOrStream(String name, ISession caller) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reloadStoredQueries(ISession caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public SDFSchema getOutputSchema(int queryId, ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addStoredProcedure(String name, StoredProcedure proc, ISession caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeStoredProcedure(String name, ISession caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public StoredProcedure getStoredProcedure(String name, ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StoredProcedure> getStoredProcedures(ISession caller) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public boolean containsStoredProcedures(String name, ISession caller) {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public Collection<String> getUdfs() {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public Set<IFunctionSignatur> getMepFunctions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILogicalQuery getLogicalQueryByString(String idOrName, ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsSink(String name, ISession caller) {
		// TODO Auto-generated method stub
		return false;
	}

}
