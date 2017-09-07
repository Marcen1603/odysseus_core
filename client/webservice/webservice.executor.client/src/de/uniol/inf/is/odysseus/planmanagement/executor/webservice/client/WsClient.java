/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.client.common.ClientSessionStore;
import de.uniol.inf.is.odysseus.core.Activator;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.ClientReceiver;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.NonBlockingTcpClientHandler;
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
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;
import de.uniol.inf.is.odysseus.core.util.BundleClassLoading;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.client.util.WsClientSession;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.client.util.WsClientUser;
import de.uniol.inf.is.odysseus.webservice.client.ConnectionInformation;
import de.uniol.inf.is.odysseus.webservice.client.ConnectionInformationResponse;
import de.uniol.inf.is.odysseus.webservice.client.CreateQueryException_Exception;
import de.uniol.inf.is.odysseus.webservice.client.DetermineOutputSchemaException_Exception;
import de.uniol.inf.is.odysseus.webservice.client.InvalidUserDataException_Exception;
import de.uniol.inf.is.odysseus.webservice.client.LogicalQueryInfo;
import de.uniol.inf.is.odysseus.webservice.client.QueryNotExistsException_Exception;
import de.uniol.inf.is.odysseus.webservice.client.QueryResponse;
import de.uniol.inf.is.odysseus.webservice.client.ResourceInformation;
import de.uniol.inf.is.odysseus.webservice.client.Response;
import de.uniol.inf.is.odysseus.webservice.client.SdfAttributeInformation;
import de.uniol.inf.is.odysseus.webservice.client.SdfDatatypeInformation;
import de.uniol.inf.is.odysseus.webservice.client.SdfSchemaInformation;
import de.uniol.inf.is.odysseus.webservice.client.SinkInformationWS;
import de.uniol.inf.is.odysseus.webservice.client.StringListResponse;
import de.uniol.inf.is.odysseus.webservice.client.StringMapListEntry;
import de.uniol.inf.is.odysseus.webservice.client.StringResponse;
import de.uniol.inf.is.odysseus.webservice.client.ViewInformationWS;
import de.uniol.inf.is.odysseus.webservice.client.WebserviceServer;
import de.uniol.inf.is.odysseus.webservice.client.WebserviceServerService;

/**
 *
 * @author Merlin Wasmann, Marco Grawunder
 *
 */
public class WsClient implements IExecutor, IClientExecutor, IOperatorOwner {

	InfoService INFO = InfoServiceFactory.getInfoService(WsClient.class);

	final static SDFSchema EMPTY_SCHEMA = SDFSchemaFactory.createNewTupleSchema("NOT AVAILABLE", new SDFAttribute("","",SDFDatatype.OBJECT),new SDFAttribute("","",SDFDatatype.OBJECT));

	// TODO: When connecting to multiple servers ... query id is not unique
	// anymore --> need server in gui

	final Map<String, List<IUpdateEventListener>> updateEventListener = new HashMap<String, List<IUpdateEventListener>>();
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
		WsClient.this.fireUpdateEvent(IUpdateEventListener.DATADICTIONARY);
		WsClient.this.fireUpdateEvent(IUpdateEventListener.QUERY);
		WsClient.this.fireUpdateEvent(IUpdateEventListener.SCHEDULING);
		WsClient.this.fireUpdateEvent(IUpdateEventListener.SESSION);
		WsClient.this.fireUpdateEvent(IUpdateEventListener.USER);
	}

	private Runner generateEvents = new Runner();

	protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(WsClient.class);
		}
		return _logger;
	}

	@SuppressWarnings("rawtypes")
	private Map<String, Map<Integer, ClientReceiver>> receivers = new HashMap<>();
	@SuppressWarnings("rawtypes")
	private Map<ClientReceiver, Integer> opReceivers = new HashMap<>();

	private Map<String, WebserviceServer> server = new HashMap<String, WebserviceServer>();

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
		List<IUpdateEventListener> l = updateEventListener.get(type);
		if (l != null) {
			l.remove(listener);
			if (l.isEmpty()) {
				updateEventListener.remove(l);
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

	/**
	 * connect
	 *
	 * @param connectString
	 *            String: expected format is
	 *            wsdlLocation;serviceNamespace;service
	 */
	@Override
	public boolean connect(String connectString) {
		String[] subConnect = connectString.split(";");
		if (subConnect.length > 1 && subConnect.length < 4) {
			try {
				startClient(new URL(subConnect[0]), new QName(subConnect[1], subConnect[2]), connectString);
			} catch (MalformedURLException e) {
				throw new RuntimeException("Could not connect to " + connectString, e);
			}
			return true;
		}
		return false;
	}

	/**
	 * Setup the client with given wsdlLocation and the service
	 *
	 * @param wsdlLocation
	 * @param service
	 */
	private void startClient(URL wsdlLocation, QName serviceName, String connectString) {
		try {
			WebserviceServerService service = new WebserviceServerService(wsdlLocation, serviceName);
			// TODO keep realname;
			this.server.put(connectString, service.getWebserviceServerPort());
			if (!generateEvents.isAlive()) {
				generateEvents.start();
			}
		} catch (Exception e) {
			INFO.error("Cannot connect to server", e);
			throw e;
		}
	}

	public WebserviceServer getWebserviceServer(String name) {
		return this.server.get(name);
	}

	@Override
	public ISession login(String username, byte[] password, String tenantname) {
		// / Simply ignore
		return null;
	}

	@Override
	public ISession login(String username, byte[] password, String tenant, String connectString) {
		String securitytoken = getSecurityToken(username, password, tenant, connectString);
		if (securitytoken == null) {
			return null;
		}
		IUser user = new WsClientUser(username, password, true);
		WsClientSession session = new WsClientSession(user, tenant, connectString);
		session.setToken(securitytoken);
		ClientSessionStore.addSession(connectString, session);
		fireUpdateEvent(IUpdateEventListener.SESSION);
		return session;
	}

	private String getSecurityToken(String username, byte[] password, String tenant, String connectString) {
		WebserviceServer wsServer = getWebserviceServer(connectString);
		if (wsServer == null) {
			if (!connect(connectString)) {
				throw new RuntimeException("Could not connect to " + connectString);
			}
			wsServer = getWebserviceServer(connectString);
		}

		String securitytoken = wsServer.login(username, new String(password), tenant).getResponseValue();
		return securitytoken;
	}

	@Override
	public ISession login(String username, byte[] password) {
		throw new RuntimeException("Login with username and password cannot be used in web service. Must use login(String username, byte[] password, String tenant, String connectString) ");
	}

	@Override
	public boolean isValid(ISession session) {
		Response r = getWebserviceServer(session.getConnectionName()).isValidSession(session.getToken());
		return r.isSuccessful();
	}

	public void assureLogin(ISession session) {
		if (session != null) {
			Response r = null;
			try {
				r = getWebserviceServer(session.getConnectionName()).isValidSession(session.getToken());
			} catch (Exception e) {
				// IGNORE
				INFO.error("Cannot connect to server " + session.getConnectionName(), e);
			}
			// try a relogin, can be necessary in case of server restart
			if (r != null && !r.isSuccessful()) {
				if (session instanceof WsClientSession) {
					WsClientUser user = (WsClientUser) ((WsClientSession) session).getUser();

					String token = getSecurityToken(user.getName(), user.getPassword(),
							((WsClientSession) session).getTenantName(), session.getConnectionName());
					// In case of new login the security token changes -->
					// Update old session object
					((WsClientSession) session).setToken(token);
				}
			}
		}
	}

	@Override
	public void removeQuery(int queryID, ISession caller) throws PlanManagementException {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				getWebserviceServer(caller.getConnectionName()).removeQuery(caller.getToken(), queryID);
			} catch (QueryNotExistsException_Exception | InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		fireUpdateEvent(IUpdateEventListener.QUERY);
	}

	@Override
	public void removeQuery(Resource queryName, ISession caller) throws PlanManagementException {
		throw new PlanManagementException("Currently not implemented");
	}

	@Override
	public void startQuery(int queryID, ISession caller) throws PlanManagementException {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				getWebserviceServer(caller.getConnectionName()).startQuery(caller.getToken(), queryID);
			} catch (QueryNotExistsException_Exception | InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		fireUpdateEvent(IUpdateEventListener.QUERY);
	}

	@Override
	public void startQuery(Resource queryName, ISession caller) throws PlanManagementException {
		throw new PlanManagementException("Currently not implemented");
	}

	@Override
	public void stopQuery(int queryID, ISession caller) throws PlanManagementException {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				getWebserviceServer(caller.getConnectionName()).stopQuery(caller.getToken(), queryID);
			} catch (QueryNotExistsException_Exception | InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		fireUpdateEvent(IUpdateEventListener.QUERY);
	}

	@Override
	public void stopQuery(Resource queryName, ISession caller) throws PlanManagementException {
		throw new PlanManagementException("Currently not implemented");
	}

	@Override
	public void suspendQuery(int queryID, ISession caller) throws PlanManagementException {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				getWebserviceServer(caller.getConnectionName()).suspendQuery(caller.getToken(), queryID);
			} catch (QueryNotExistsException_Exception | InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		fireUpdateEvent(IUpdateEventListener.QUERY);
	}

	@Override
	public void suspendQuery(Resource queryName, ISession caller) throws PlanManagementException {
		throw new PlanManagementException("Currently not implemented");
	}

	@Override
	public void resumeQuery(int queryID, ISession caller) throws PlanManagementException {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				getWebserviceServer(caller.getConnectionName()).resumeQuery(caller.getToken(), queryID);
			} catch (QueryNotExistsException_Exception | InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		fireUpdateEvent(IUpdateEventListener.QUERY);
	}

	@Override
	public void resumeQuery(Resource queryName, ISession caller) throws PlanManagementException {
		throw new PlanManagementException("Currently not implemented");
	}

	@Override
	public void partialQuery(int queryID, int sheddingFactor, ISession caller) throws PlanManagementException {
		throw new PlanManagementException("Currently not implemented");
	}

	@Override
	public void partialQuery(Resource queryName, int sheddingFactor, ISession caller) throws PlanManagementException {
		throw new PlanManagementException("Currently not implemented");
	}

	@Override
	public QueryState getQueryState(int queryID, ISession session) {
		assureLogin(session);
		return getWebserviceServer(session.getConnectionName()).getQueryState(queryID, session.getToken());
	}

	@Override
	public QueryState getQueryState(String queryName, ISession session) {
		throw new PlanManagementException("Currently not implemented");
	}

	@Override
	public List<QueryState> getQueryStates(List<Integer> queryIDs, List<ISession> sessions) {
		List<QueryState> states = new ArrayList<QueryState>();

		List<String> tokens = new ArrayList<>();
		sessions.forEach((ISession s) -> tokens.add(s.getToken()));

		for (ISession s : sessions) {
			states.addAll(getWebserviceServer(s.getConnectionName()).getQueryStates(queryIDs, tokens));
		}

		return states;
	}

	@Override
	public Collection<String> getQueryBuildConfigurationNames(ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				return getWebserviceServer(caller.getConnectionName())
						.getQueryBuildConfigurationNames(caller.getToken()).getResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public Set<String> getSupportedQueryParsers(ISession caller) throws PlanManagementException {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				List<String> parsers = getWebserviceServer(caller.getConnectionName())
						.getSupportedQueryParsers(caller.getToken()).getResponseValue();
				Set<String> parserSet = new HashSet<String>();
				for (String parser : parsers) {
					parserSet.add(parser);
				}
				return parserSet;
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public List<Integer> startAllClosedQueries(ISession user) {
		assureLogin(user);
		if (getWebserviceServer(user.getConnectionName()) != null) {
			// this is always null
			try {
				getWebserviceServer(user.getConnectionName()).startAllClosedQueries(user.getToken());
			} catch (InvalidUserDataException_Exception e) {
				e.printStackTrace();
			}
		}
		fireUpdateEvent(IUpdateEventListener.QUERY);
		return null;
	}

	@Override
	public Set<String> getRegisteredBufferPlacementStrategiesIDs(ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				List<String> ids = getWebserviceServer(caller.getConnectionName())
						.getRegisteredBufferPlacementStrategiesIDs(caller.getToken()).getResponseValue();
				Set<String> idSet = new HashSet<String>();
				for (String id : ids) {
					idSet.add(id);
				}
				return idSet;
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public Set<String> getRegisteredSchedulingStrategies(ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				List<String> strats = getWebserviceServer(caller.getConnectionName())
						.getRegisteredSchedulingStrategies(caller.getToken()).getResponseValue();
				Set<String> stratSet = new HashSet<String>();
				for (String strat : strats) {
					stratSet.add(strat);
				}
				return stratSet;
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public Set<String> getRegisteredSchedulers(ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				List<String> scheds = getWebserviceServer(caller.getConnectionName())
						.getRegisteredSchedulingStrategies(caller.getToken()).getResponseValue();
				Set<String> schedSet = new HashSet<String>();
				for (String sched : scheds) {
					schedSet.add(sched);
				}
				return schedSet;
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public void setScheduler(String scheduler, String schedulerStrategy, ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				getWebserviceServer(caller.getConnectionName()).setScheduler(caller.getToken(), scheduler,
						schedulerStrategy);
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}

	}

	@Override
	public String getCurrentSchedulerID(ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				StringResponse response = getWebserviceServer(caller.getConnectionName())
						.getCurrentSchedulerID(caller.getToken());
				if (response != null) {
					return response.getResponseValue();
				}
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public String getCurrentSchedulingStrategyID(ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				return getWebserviceServer(caller.getConnectionName()).getCurrentSchedulingStrategyID(caller.getToken())
						.getResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return "WebServiceExecutor";
	}

	@Override
	public Collection<Integer> addQuery(String query, String parserID, ISession user, Context context)
			throws PlanManagementException {
		assureLogin(user);
		try {
			Collection<Integer> response = getWebserviceServer(user.getConnectionName())
					.addQuery2(user.getToken(), parserID, query, context).getResponseValue();
			return response;
		} catch (InvalidUserDataException_Exception | CreateQueryException_Exception e) {
			throw new PlanManagementException(e);
		}finally {
			fireUpdateEvent(IUpdateEventListener.QUERY);
			// Query could create schema information ... fire events
			fireUpdateEvent(IUpdateEventListener.DATADICTIONARY);			
		}
	}

	@Override
	public void runCommand(String commandExpression, ISession caller) {
		assureLogin(caller);
		try{
			getWebserviceServer(caller.getConnectionName()).runCommand(caller.getToken(), commandExpression);
			fireAllUpdateEvents();
		}catch (InvalidUserDataException_Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ILogicalQuery getLogicalQueryById(int id, ISession caller) {
		assureLogin(caller);
		try {
			QueryResponse resp = getWebserviceServer(caller.getConnectionName()).getLogicalQueryById(caller.getToken(),
					Integer.toString(id));
			return createLogicalQueryFromInfo(resp.getResponseValue(), resp.getQueryState(), caller);
		} catch (InvalidUserDataException_Exception | QueryNotExistsException_Exception e) {
			throw new PlanManagementException(e);
		}
	}

	@Override
	public ILogicalQuery getLogicalQueryByName(Resource name, ISession caller) {
		assureLogin(caller);
		try {
			// Avoid sending Resource over the web-Service
			QueryResponse resp = getWebserviceServer(caller.getConnectionName())
					.getLogicalQueryByName(caller.getToken(), name.toString());
			return createLogicalQueryFromInfo(resp.getResponseValue(), resp.getQueryState(), caller);
		} catch (InvalidUserDataException_Exception e) {
			throw new PlanManagementException(e);
		}
	}

	/**
	 * method to create an instance of LogicalQuery from an instance of
	 * LogicalQueryInfo
	 *
	 * @param info
	 * @param queryState
	 * @return LogicalQuery
	 */
	public LogicalQuery createLogicalQueryFromInfo(LogicalQueryInfo info, QueryState queryState, ISession caller) {
		assureLogin(caller);
		LogicalQuery query = new LogicalQuery(info.getId(), info.getParserID(), null, info.getPriority());
		query.setParameters(info.getParameters());
		query.setParameter("STATE", queryState);
		query.setContainsCycles(info.isContainsCycles());
		query.setQueryText(info.getQueryText());
		query.setUser(caller);
		if (info.getName() != null){
			query.setName(Resource.specialCreateResource(info.getName().toString(),caller.getUser()));
		}
		return query;
	}

	@Override
	public void logout(ISession caller) {
		assureLogin(caller);
		getWebserviceServer(caller.getConnectionName()).logout(caller.getToken());
		ClientSessionStore.removeSession(caller.getConnectionName());
		fireUpdateEvent(IUpdateEventListener.SESSION);
	}

	@Override
	public Collection<Integer> getLogicalQueryIds(ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				return getWebserviceServer(caller.getConnectionName()).getLogicalQueryIds(caller.getToken())
						.getResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<IPhysicalOperator> getPhysicalRoots(int queryID, ISession caller) {
		assureLogin(caller);
		List<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>();
		Optional<ClientReceiver> receiver = createClientReceiver(this, queryID, caller);
		if (receiver.isPresent()) {
			roots.add(receiver.get());
		}
		return roots;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Optional<ClientReceiver> createClientReceiver(IExecutor exec, int queryId, ISession caller) {
		assureLogin(caller);

		if (receivers.containsKey(queryId)) {
			return Optional.of(receivers.get(caller.getConnectionName()).get(queryId));
		}

		SDFSchema outputSchema = exec.getOutputSchema(queryId, caller);
		String type = outputSchema.getType().getSimpleName();
		IStreamObjectDataHandler<?> dataHandler = DataHandlerRegistry.getStreamObjectDataHandler(type, outputSchema);

		if (dataHandler == null){
			throw new RuntimeException("Cannot find data handler for type "+type);
		}

		// TODO: Handle generic metadata ...
		dataHandler.setMetaAttribute(new TimeInterval());
		List<SocketAddress> adr = ((IClientExecutor) exec).getSocketConnectionInformation(queryId, caller);
		OptionMap options = new OptionMap();
		String hosts = "";
		String ports = "";
		for (SocketAddress sa : adr) {
			hosts += ((InetSocketAddress) sa).getHostString() + ",";
			ports += ((InetSocketAddress) sa).getPort() + ",";
		}
		options.setOption("ports", ports);
		options.setOption("hosts", hosts);
		// TODO: Send logininfo to server
		// options.setOption("logininfo", caller.getToken() + "\n");
		options.setOption("init", caller.getToken() + "\n");

		// TODO username and password get from anywhere
		IProtocolHandler h = ProtocolHandlerRegistry.getInstance("SizeByteBuffer", ITransportDirection.IN,
				IAccessPattern.PUSH, options, dataHandler);
		// Must be done to add the transport to the protocoll ... seems not
		// really intuitive ...
		ITransportHandler th = TransportHandlerRegistry.getInstance(NonBlockingTcpClientHandler.NAME, h, options);
		h.setTransportHandler(th);
		ClientReceiver receiver = new ClientReceiver(h);
		receiver.setOutputSchema(outputSchema);
		receiver.open(null, 0, 0, null, null);
		receiver.addOwner(this);
		Map<Integer, ClientReceiver> r = receivers.get(caller.getConnectionName());
		if (r == null) {
			r = new HashMap<Integer, ClientReceiver>();
			receivers.put(caller.getConnectionName(), r);
		}
		r.put(queryId, receiver);
		// TODO: FIXME for multiple server version
		opReceivers.put(receiver, queryId);

		return Optional.of(receiver);
	}

	@Override
	public void done(IOwnedOperator op) {
		Integer queryID = opReceivers.get(op);
		if (queryID != null) {
			opReceivers.remove(op);
			receivers.remove(queryID);
		}
	}

	/**
	 * Returns a SocketAddress object
	 *
	 * @param queryId
	 * @return SocketAddress
	 */
	@Override
	public List<SocketAddress> getSocketConnectionInformation(int queryId, ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				ConnectionInformationResponse infoResponse = getWebserviceServer(caller.getConnectionName())
						.getConnectionInformation(caller.getToken(), queryId, false);

				if (infoResponse != null) {
					ConnectionInformation info = infoResponse.getResponseValue();
					List<SocketAddress> addresses = new LinkedList<SocketAddress>();
					List<String> adressStrings = info.getAddress();
					for (String a : adressStrings) {
						try {
							addresses.add(new InetSocketAddress(InetAddress.getByName(a), info.getPort()));
						} catch (Exception e) {
							// Ignore

						}
					}
					return addresses;
				}
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public SDFSchema getOutputSchema(int queryId, ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				return createSchemaFromInformation(getWebserviceServer(caller.getConnectionName())
						.getOutputSchemaByQueryId(caller.getToken(), queryId).getResponseValue());
			} catch (InvalidUserDataException_Exception | QueryNotExistsException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	private SDFSchema createSchemaFromInformation(SdfSchemaInformation info) {
		if (info == null) {
			return null;
		}
		String uri = info.getUri();
		Collection<SdfAttributeInformation> attributeInfos = info.getAttributes();
		Collection<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		for (SdfAttributeInformation attrInfo : attributeInfos) {
			attributes.add(createAttributeFromInformation(attrInfo));
		}

		try{
			// TODO: This will not work in OSGi if type class is in different bundle!
			//Class<?> typeClass = Class.forName(info.getTypeClass());
			Class<?> typeClass = BundleClassLoading.findClass(info.getTypeClass(), Activator.getBundleContext().getBundle());
			return SDFSchemaFactory.createNewSchema(uri, typeClass, attributes);
		}catch(Exception e){
			e.printStackTrace();
		}
		return SDFSchemaFactory.createNewTupleSchema(uri, attributes);
	}

	private SDFAttribute createAttributeFromInformation(SdfAttributeInformation info) {
		// TODO Extend SdfAttributeInformation
		SDFDatatype dt = createDatatypeFromInformation(info.getDatatype());

		return new SDFAttribute(info.getSourcename(), info.getAttributename(), SDFDatatype.createTypeWithSubSchema(
				dt, dt.getSubType(), createSchemaFromInformation(info.getSubschema())));
	}

	private SDFDatatype createDatatypeFromInformation(SdfDatatypeInformation info) {
		SDFDatatype subtype = null;
		SDFSchema subschema = null;
		if (info.getSubtype() != null) {
			subtype = createDatatypeFromInformation(info.getSubtype());
		}
		if (info.getSubSchema() != null) {
			subschema = createSchemaFromInformation(info.getSubSchema());
		}
		return new SDFDatatype(info.getUri(), info.getType(), subtype, subschema);
	}

	@Override
	public ILogicalPlan removeSink(String name, ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				ILogicalOperator result = (ILogicalOperator) getWebserviceServer(caller.getConnectionName())
						.removeSinkByName(name, caller.getToken()).getResponseValue();
				fireUpdateEvent(IUpdateEventListener.DATADICTIONARY);
				return new LogicalPlan(result);
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public ILogicalPlan removeSink(Resource name, ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				ResourceInformation ri = toResourceInformation(name);
				ILogicalOperator result = (ILogicalOperator) getWebserviceServer(caller.getConnectionName())
						.removeSinkByResource(ri, caller.getToken()).getResponseValue();
				fireUpdateEvent(IUpdateEventListener.DATADICTIONARY);
				return new LogicalPlan(result);
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public void removeViewOrStream(String name, ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				getWebserviceServer(caller.getConnectionName()).removeViewOrStreamByName(name, caller.getToken());
				fireUpdateEvent(IUpdateEventListener.DATADICTIONARY);
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
	}

	@Override
	public void removeViewOrStream(Resource name, ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				ResourceInformation ri = toResourceInformation(name);
				getWebserviceServer(caller.getConnectionName()).removeViewOrStreamByResource(ri, caller.getToken());
				fireUpdateEvent(IUpdateEventListener.DATADICTIONARY);
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}

	}

	@Override
	public List<ViewInformation> getStreamsAndViewsInformation(ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				List<ViewInformation> result = new ArrayList<>();
				List<ViewInformationWS> l = getWebserviceServer(caller.getConnectionName())
						.getStreamsAndViews(caller.getToken());
				for (ViewInformationWS viws : l) {
					ViewInformation vi = new ViewInformation();
					vi.setName(toResource(viws.getName()));
					try{
						vi.setOutputSchema(toSDFSchema(viws.getSchema()));
					}catch(ClassNotFoundException e){
						// TODO: Should there be an output??
						vi.setOutputSchema(EMPTY_SCHEMA);
					}
					result.add(vi);
				}
				return result;
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public List<SinkInformation> getSinks(ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				List<SinkInformationWS> l = getWebserviceServer(caller.getConnectionName()).getSinks(caller.getToken());
				List<SinkInformation> result = new ArrayList<>();
				for (SinkInformationWS viws : l) {
					SinkInformation vi = new SinkInformation();
					vi.setName(toResource(viws.getName()));
					vi.setOutputSchema(toSDFSchema(viws.getSchema()));
					result.add(vi);
				}
				return result;
			} catch (InvalidUserDataException_Exception | ClassNotFoundException e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public void reloadStoredQueries(ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				getWebserviceServer(caller.getConnectionName()).reloadStoredQueries(caller.getToken());
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}

	}

	@Override
	public boolean containsViewOrStream(Resource name, ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				ResourceInformation ri = toResourceInformation(name);
				return getWebserviceServer(caller.getConnectionName())
						.containsViewOrStreamByResource(ri, caller.getToken()).isResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return false;
	}

	public ResourceInformation toResourceInformation(Resource name) {
		ResourceInformation ri = new ResourceInformation();
		ri.setResourceName(name.getResourceName());
		ri.setUser(name.getUser());
		return ri;
	}

	public Resource toResource(ResourceInformation name) {
		Resource ri = new Resource(name.getUser(), name.getResourceName());
		return ri;
	}

	@Override
	public boolean containsViewOrStream(String name, ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				return getWebserviceServer(caller.getConnectionName())
						.containsViewOrStreamByName(name, caller.getToken()).isResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return false;
	}

	@Override
	public void addStoredProcedure(String name, StoredProcedure sp, ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				getWebserviceServer(caller.getConnectionName()).addStoredProcedure(name, sp, caller.getToken());
				fireUpdateEvent(IUpdateEventListener.DATADICTIONARY);
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
	}

	@Override
	public StoredProcedure getStoredProcedure(String name, ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				StoredProcedure result = getWebserviceServer(caller.getConnectionName())
						.getStoredProcedure(name, caller.getToken()).getResponseValue();
				fireUpdateEvent(IUpdateEventListener.DATADICTIONARY);
				return result;
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public void removeStoredProcedure(String name, ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				getWebserviceServer(caller.getConnectionName()).removeStoredProcedure(name, caller.getToken());
				fireUpdateEvent(IUpdateEventListener.DATADICTIONARY);
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
	}

	@Override
	public List<StoredProcedure> getStoredProcedures(ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {

				return getWebserviceServer(caller.getConnectionName()).getStoredProcedures(caller.getToken())
						.getResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return new ArrayList<>();
	}

	@Override
	public boolean containsStoredProcedures(String name, ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				return getWebserviceServer(caller.getConnectionName()).containsStoredProcedures(name, caller.getToken())
						.isResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#
	 * getOperatorNames(de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public List<String> getOperatorNames(ISession caller) {
		assureLogin(caller);
		if (caller != null) {
			if (getWebserviceServer(caller.getConnectionName()) != null) {
				try {
					return getWebserviceServer(caller.getConnectionName()).getOperatorNames(caller.getToken())
							.getResponseValue();
				} catch (InvalidUserDataException_Exception e) {
					throw new PlanManagementException(e);
				}
			}
		}
		return new ArrayList<>();
	}

	private Map<String, List<LogicalOperatorInformation>> logicalOperatorInformationCache = new HashMap<>();

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#
	 * getOperatorInformations
	 * (de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public List<LogicalOperatorInformation> getOperatorInformations(ISession caller) {
		if (caller == null) {
			return null;
		}
		List<LogicalOperatorInformation> ret = logicalOperatorInformationCache.get(caller.getConnectionName());

		if (ret == null) {
			assureLogin(caller);
			if (getWebserviceServer(caller.getConnectionName()) != null) {
				try {
					ret = getWebserviceServer(caller.getConnectionName()).getOperatorInformations(caller.getToken())
							.getResponseValue();
				} catch (InvalidUserDataException_Exception e) {
					throw new PlanManagementException(e);
				}
			}
			logicalOperatorInformationCache.put(caller.getConnectionName(), ret);
		}

		return ret != null ? ret : new ArrayList<>();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#
	 * getOperatorInformation(java.lang.String,
	 * de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public LogicalOperatorInformation getOperatorInformation(String name, ISession caller) {
		if (caller == null) {
			return null;
		}
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				return getWebserviceServer(caller.getConnectionName()).getOperatorInformation(name, caller.getToken())
						.getResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public SDFSchema determineOutputSchema(String query, String parserID, ISession user, int port, Context context) {
		assureLogin(user);
		if (getWebserviceServer(user.getConnectionName()) != null) {
			try {
				SdfSchemaInformation si = getWebserviceServer(user.getConnectionName())
						.determineOutputSchema(query, parserID, user.getToken(), port, context).getResponseValue();
				SDFSchema schema = toSDFSchema(si);
				return schema;
			} catch (InvalidUserDataException_Exception | ClassNotFoundException
					| DetermineOutputSchemaException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public SDFSchema toSDFSchema(SdfSchemaInformation si) throws ClassNotFoundException {
		List<SDFAttribute> attributes = new ArrayList<>();
		// TODO: extends SdfAttributeInformation
		for (SdfAttributeInformation sda : si.getAttributes()) {
			SDFDatatype dt = new SDFDatatype(sda.getDatatype().getUri());
			attributes.add(new SDFAttribute(sda.getSourcename(), sda.getAttributename(), dt, null, null, null));
		}

		// Will not work in OSGi environments!
		//Class<? extends IStreamObject> type = (Class<? extends IStreamObject>) Class.forName(si.getTypeClass());
		@SuppressWarnings({ "rawtypes" })
		Class<? extends IStreamObject> type = (Class<? extends IStreamObject>) BundleClassLoading.findClass(si.getTypeClass(), Activator.getBundleContext().getBundle());

		SDFSchema schema = SDFSchemaFactory.createNewSchema(si.getUri(), type,
				attributes);
		return schema;
	}

	private Map<String, Set<SDFDatatype>> registeredDatatypeCache = new HashMap<>();

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#
	 * getRegisteredDatatypes
	 * (de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public Set<SDFDatatype> getRegisteredDatatypes(ISession caller) {
		if (caller == null) {
			return null;
		}
		Set<SDFDatatype> ret = registeredDatatypeCache.get(caller.getConnectionName());
		if (ret == null) {
			assureLogin(caller);
			if (getWebserviceServer(caller.getConnectionName()) != null) {
				try {
					ret = new HashSet<>();
					List<SdfDatatypeInformation> dts = getWebserviceServer(caller.getConnectionName())
							.getRegisteredDatatypes(caller.getToken()).getResponseValue();
					for (SdfDatatypeInformation dt : dts) {
						ret.add(new SDFDatatype(dt.getUri()));
					}
				} catch (InvalidUserDataException_Exception e) {
					throw new PlanManagementException(e);
				}
				registeredDatatypeCache.put(caller.getConnectionName(), ret);
			}
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#
	 * getRegisteredAggregateFunctions(java.lang.String,
	 * de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public Set<String> getRegisteredAggregateFunctions(String datamodel, ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				HashSet<String> set = new HashSet<>();
				set.addAll(getWebserviceServer(caller.getConnectionName())
						.getRegisteredAggregateFunctions(datamodel, caller.getToken()).getResponseValue());
				return set;
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return new HashSet<>();
	}

	@Override
	public Set<String> getRegisteredAggregateFunctions(@SuppressWarnings("rawtypes") Class<? extends IStreamObject> datamodel, ISession caller) {
		return getRegisteredAggregateFunctions(datamodel.getName(), caller);
	}
	
	@Override
	public Set<String> getRegisteredWrapperNames(ISession caller) {
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				HashSet<String> set = new HashSet<>();
				set.addAll(getWebserviceServer(caller.getConnectionName()).getRegisteredWrapperNames(caller.getToken())
						.getResponseValue());
				return set;
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return new HashSet<>();
	}

	Map<String, Map<String, Map<String, List<String>>>> queryParserTokensCache = new HashMap<>();

	@Override
	public Map<String, List<String>> getQueryParserTokens(String queryParser, ISession user) {
		if (user == null) {
			return null;
		}
		Map<String, Map<String, List<String>>> connCache = queryParserTokensCache.get(user.getConnectionName());
		if (connCache == null) {
			connCache = new HashMap<>();
			queryParserTokensCache.put(user.getConnectionName(), connCache);
		}
		Map<String, List<String>> result = connCache.get(queryParser);
		if (result == null) {
			assureLogin(user);
			result = new HashMap<>();
			if (getWebserviceServer(user.getConnectionName()) != null) {
				try {
					List<StringMapListEntry> entries = getWebserviceServer(user.getConnectionName())
							.getQueryParserTokens(queryParser, user.getToken()).getResponseValue();
					for (StringMapListEntry e : entries) {
						result.put(e.getKey(), e.getValue());
					}
				} catch (InvalidUserDataException_Exception e) {
					throw new PlanManagementException(e);
				}
			}
			connCache.put(queryParser, result);
		}
		return result;
	}

	@Override
	public List<String> getQueryParserSuggestions(String queryParser, String hint, ISession caller) {
		if (caller == null) {
			return null;
		}
		assureLogin(caller);
		if (getWebserviceServer(caller.getConnectionName()) != null) {
			try {
				return getWebserviceServer(caller.getConnectionName())
						.getQueryParserSuggestions(queryParser, hint, caller.getToken()).getResponseValue();

			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public List<IUser> getUsers(ISession session) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<String> getUdfs() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int compareTo(IOperatorOwner o) {
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
	public Set<String> getMetadataNames(ISession session) {
		if( session == null ) {
			return null;
		}

		assureLogin(session);
		WebserviceServer webserviceServer = getWebserviceServer(session.getConnectionName());
		if( webserviceServer != null ) {
			try {
				StringListResponse names = webserviceServer.getMetadataNames(session.getToken());
				return new HashSet<String>(names.getResponseValue());

			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}

		return null;
	}

}
