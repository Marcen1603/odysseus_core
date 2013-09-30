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
 ******************************************************************************/
/** Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.connection.NioConnectionHandler;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.SizeByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ClientReceiver;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IClientExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IQueryListener;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.client.util.WsClientSession;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.client.util.WsClientUser;
import de.uniol.inf.is.odysseus.webservice.client.ConnectionInformation;
import de.uniol.inf.is.odysseus.webservice.client.LogicalQueryInfo;
import de.uniol.inf.is.odysseus.webservice.client.SdfAttributeInformation;
import de.uniol.inf.is.odysseus.webservice.client.SdfDatatypeInformation;
import de.uniol.inf.is.odysseus.webservice.client.SdfSchemaInformation;
import de.uniol.inf.is.odysseus.webservice.client.WebserviceServer;
import de.uniol.inf.is.odysseus.webservice.client.WebserviceServerService;

/**
 * 
 * @author Merlin Wasmann
 * 
 */
@SuppressWarnings("deprecation")
public class WsClient implements IExecutor, IClientExecutor {

	protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(WsClient.class);
		}
		return _logger;
	}

	private List<IQueryListener> queryListener = new ArrayList<IQueryListener>();

	// Map of all added queries accessible by queryId
	private Map<Integer, ILogicalQuery> queries = new HashMap<Integer, ILogicalQuery>();

	@SuppressWarnings("rawtypes")
	private Map<Integer, ClientReceiver> receivers = new HashMap<Integer, ClientReceiver>();

	// manages the connection to the WebserviceServer
	WebserviceServerService service;
	// create handle for WebserviceServer
	WebserviceServer server;
	// SecurityToken
	String securitytoken;
	// User
	ISession user;

	/**
	 * connect
	 * 
	 * @param connectString
	 *            String: expected format is
	 *            wsdlLocation;serviceNamespace;service
	 */
	@Override
	public boolean connect(String connectString) {
		// connectString should look like this:
		// wsdlLocation;serviceNamespace;service
		String[] subConnect = connectString.split(";");
		if (subConnect.length > 1 && subConnect.length < 4) {
			try {
				startClient(new URL(subConnect[0]), new QName(subConnect[1], subConnect[2]));
				return true;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	@Override
	public void addQueryListener(IQueryListener listener) {
		queryListener.add(listener);
	}

	@Override
	public void removeQueryListener(IQueryListener listener) {
		queryListener.remove(listener);
	}

	/**
	 * Setup the client with given wsdlLocation and the service
	 * 
	 * @param wsdlLocation
	 * @param service
	 */
	public void startClient(URL wsdlLocation, QName service) {
		this.service = new WebserviceServerService(wsdlLocation, service);
		this.server = this.service.getWebserviceServerPort();
	}

	public WebserviceServerService getWebserviceServerService() {
		return this.service;
	}

	public WebserviceServer getWebserviceServer() {
		return this.server;
	}

	private String getSecurityToken() {
		return this.securitytoken;
	}

	@Override
	public ISession login(String username, byte[] password, String tenant) {
		this.securitytoken = getWebserviceServer().login(username, new String(password)).getResponseValue();
		IUser user = new WsClientUser(username, password, true);
		WsClientSession session = new WsClientSession(user, tenant);
		session.setToken(this.securitytoken);
		this.user = session;
		return session;
	}

	@Override
	public void removeQuery(int queryID, ISession caller) throws PlanManagementException {
		if (getWebserviceServer() != null) {
			getWebserviceServer().removeQuery(caller.getToken(), queryID);
			queries.remove(queryID);
		}
	}

	@Override
	public void startQuery(int queryID, ISession caller) throws PlanManagementException {
		if (getWebserviceServer() != null) {
			getWebserviceServer().startQuery(caller.getToken(), queryID);
		}
	}

	@Override
	public void stopQuery(int queryID, ISession caller) throws PlanManagementException {
		if (getWebserviceServer() != null) {
			getWebserviceServer().stopQuery(caller.getToken(), queryID);
		}
	}

	@Override
	public Collection<String> getQueryBuildConfigurationNames() {
		if (getWebserviceServer() != null) {
			return getWebserviceServer().getQueryBuildConfigurationNames(getSecurityToken()).getResponseValue();
		}
		return null;
	}

	@Override
	public Set<String> getSupportedQueryParsers() throws PlanManagementException {
		if (getWebserviceServer() != null) {
			List<String> parsers = getWebserviceServer().getSupportedQueryParsers(getSecurityToken()).getResponseValue();
			Set<String> parserSet = new HashSet<String>();
			for (String parser : parsers) {
				parserSet.add(parser);
			}
			return parserSet;
		}
		return null;
	}

	@Override
	public List<Integer> startAllClosedQueries(ISession user) {
		if (getWebserviceServer() != null) {
			// this is always null
			getWebserviceServer().startAllClosedQueries(user.getToken());
		}
		return null;
	}

	@Override
	public Set<String> getRegisteredBufferPlacementStrategiesIDs() {
		if (getWebserviceServer() != null) {
			List<String> ids = getWebserviceServer().getRegisteredBufferPlacementStrategiesIDs(getSecurityToken()).getResponseValue();
			Set<String> idSet = new HashSet<String>();
			for (String id : ids) {
				idSet.add(id);
			}
			return idSet;
		}
		return null;
	}

	@Override
	public Set<String> getRegisteredSchedulingStrategies() {
		if (getWebserviceServer() != null) {
			List<String> strats = getWebserviceServer().getRegisteredSchedulingStrategies(getSecurityToken()).getResponseValue();
			Set<String> stratSet = new HashSet<String>();
			for (String strat : strats) {
				stratSet.add(strat);
			}
			return stratSet;
		}
		return null;
	}

	@Override
	public Set<String> getRegisteredSchedulers() {
		if (getWebserviceServer() != null) {
			List<String> scheds = getWebserviceServer().getRegisteredSchedulingStrategies(getSecurityToken()).getResponseValue();
			Set<String> schedSet = new HashSet<String>();
			for (String sched : scheds) {
				schedSet.add(sched);
			}
			return schedSet;
		}
		return null;
	}

	@Override
	public void setScheduler(String scheduler, String schedulerStrategy) {
		if (getWebserviceServer() != null) {
			getWebserviceServer().setScheduler(getSecurityToken(), scheduler, schedulerStrategy);
		}

	}

	@Override
	public String getCurrentSchedulerID() {
		if (getWebserviceServer() != null) {
			return getWebserviceServer().getCurrentSchedulerID(getSecurityToken()).getResponseValue();
		}
		return null;
	}

	@Override
	public String getCurrentSchedulingStrategyID() {
		if (getWebserviceServer() != null) {
			return getWebserviceServer().getCurrentSchedulingStrategyID(getSecurityToken()).getResponseValue();
		}
		return null;
	}

	@Override
	public String getName() {
		if (getWebserviceServer() != null) {
			return getWebserviceServer().getName(getSecurityToken()).getResponseValue();
		}
		return null;
	}

	@Override
	public Collection<Integer> addQuery(String query, String parserID, ISession user, String queryBuildConfigurationName) throws PlanManagementException {
		Collection<Integer> response = getWebserviceServer().addQuery(user.getToken(), parserID, query, queryBuildConfigurationName).getResponseValue();
		for (Integer val : response) {
			this.queries.put(val, getLogicalQueryById(val));
		}
		return response;
	}

	@Override
	public ILogicalQuery getLogicalQueryById(int id) {
		// FIXME: Call getLogicalQueryById
		return createLogicalQueryFromInfo(getWebserviceServer().getLogicalQuery(getSecurityToken(), "" + id).getResponseValue());
	}

	@Override
	public ILogicalQuery getLogicalQueryByName(String name) {
		// FIXME: Call getLogicalQueryByName
		return createLogicalQueryFromInfo(getWebserviceServer().getLogicalQuery(getSecurityToken(), name).getResponseValue());
	}

	/**
	 * method to create an instance of LogicalQuery from an instance of
	 * LogicalQueryInfo
	 * 
	 * @param info
	 * @return LogicalQuery
	 */
	public LogicalQuery createLogicalQueryFromInfo(LogicalQueryInfo info) {
		LogicalQuery query = new LogicalQuery(info.getId(), info.getParserID(), null, info.getPriority());
		query.setContainsCycles(info.isContainsCycles());
		query.setQueryText(info.getQueryText());
		query.setUser(this.user);
		return query;
	}

	@Override
	public Collection<ILogicalQuery> getLogicalQueryInfos() {
		return this.queries.values();
	}

	public void setQueries(Collection<ILogicalQuery> qs) {
		this.queries.clear();
		for (ILogicalQuery q : qs) {
			this.queries.put(q.getID(), q);
		}
	}

	@Override
	public void logout(ISession caller) {
		getWebserviceServer().logout(caller.getToken());
	}

	@Override
	public Collection<Integer> getLogicalQueryIds() {
		if (getWebserviceServer() != null) {
			return getWebserviceServer().getLogicalQueryIds(getSecurityToken()).getResponseValue();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<IPhysicalOperator> getPhysicalRoots(int queryID) {
		List<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>();
		Optional<ClientReceiver> receiver = createClientReceiver(this, queryID);
		if (receiver.isPresent()) {
			roots.add(receiver.get());
		}
		return roots;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Optional<ClientReceiver> createClientReceiver(IExecutor exec, int queryId) {
		SDFSchema outputSchema = exec.getOutputSchema(queryId);
		IDataHandler tdh = DataHandlerRegistry.getDataHandler("Tuple", outputSchema);
		InetSocketAddress adr = (InetSocketAddress) ((IClientExecutor) exec).getSocketConnectionInformation(queryId);
		// TODO username and password get from anywhere
		try {
			if (receivers.containsKey(queryId)) {
				return Optional.of(receivers.get(queryId));
			} 
			ClientReceiver receiver = new ClientReceiver(new ByteBufferHandler(tdh), new SizeByteBufferHandler(), new NioConnectionHandler(adr.getHostName(), adr.getPort(),
					false, null, null));
			receiver.open(null, 0, 0, null, null);
			receivers.put(queryId, receiver);
			return Optional.of(receiver);
			
		} catch (IOException e) {
			e.printStackTrace();
			return Optional.absent();
		}
	}

	/**
	 * Returns a SocketAddress object
	 * 
	 * @param queryId
	 * @return SocketAddress
	 */
	@Override
	public SocketAddress getSocketConnectionInformation(int queryId) {
		if (getWebserviceServer() != null) {
			ConnectionInformation info = getWebserviceServer().getConnectionInformation(getSecurityToken(), queryId).getResponseValue();
			try {
				return new InetSocketAddress(InetAddress.getByName(info.getAddress()), info.getPort());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public ISession getUser() {
		return this.user;
	}

	@Override
	public SDFSchema getOutputSchema(int queryId) {
		if (getWebserviceServer() != null) {
			return createSchemaFromInformation(getWebserviceServer().getOutputSchema(getSecurityToken(), queryId).getResponseValue());
		}
		return null;
	}

	private SDFSchema createSchemaFromInformation(SdfSchemaInformation info) {
		String uri = info.getUri();
		Collection<SdfAttributeInformation> attributeInfos = info.getAttributes();
		Collection<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		for (SdfAttributeInformation attrInfo : attributeInfos) {
			attributes.add(createAttributeFromInformation(attrInfo));
		}
		// FIXME: Is this always Tuple?
		return new SDFSchema(uri, Tuple.class, attributes);
	}

	private SDFAttribute createAttributeFromInformation(SdfAttributeInformation info) {
		return new SDFAttribute(info.getSourcename(), info.getAttributename(), createDatatypeFromInformation(info.getDatatype()));
	}

	private SDFDatatype createDatatypeFromInformation(SdfDatatypeInformation info) {
		return new SDFDatatype(info.getUri());
	}

	/********************************************************************
	 * Methods not implemented by server *
	 ********************************************************************/

	@Override
	public Integer addQuery(ILogicalOperator logicalPlan, ISession user, String queryBuildConfigurationName) throws PlanManagementException {
		// TODO not implemented by server yet
		return null;
	}

	@Override
	public Integer addQuery(List<IPhysicalOperator> physicalPlan, ISession user, String queryBuildConfigurationName) throws PlanManagementException {
		// TODO not implemented by server yet
		return null;
	}

	@Override
	public ILogicalOperator removeSink(String name, ISession caller) {
		// TODO not implemented by server yet
		return null;
	}
	
	@Override
	public ILogicalOperator removeSink(Resource name, ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeViewOrStream(String name, ISession caller) {
		// TODO not implemented by server yet
	}
	
	@Override
	public void removeViewOrStream(Resource name, ISession caller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Entry<Resource, ILogicalOperator>> getStreamsAndViews(ISession caller) {
		// TODO not implemented by server yet
		return null;
	}

	@Override
	public Set<Entry<Resource, ILogicalOperator>> getSinks(ISession caller) {
		// TODO not implemented by server yet
		return null;
	}

	@Override
	public void reloadStoredQueries(ISession caller) {
		// TODO not implemented by server yet

	}

	@Override
	public void addStoredProcedure(String name, StoredProcedure sp, ISession caller) {		
	}

	@Override
	public StoredProcedure getStoredProcedure(String name, ISession caller) {
		return null;
	}

	@Override
	public void removeStoredProcedure(String name, ISession caller) {		
	}

	@Override
	public List<StoredProcedure> getStoredProcedures(ISession caller) {
		return null;
	}

	@Override
	public boolean containsStoredProcedures(String name, ISession caller) {
		return false;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#getOperatorNames(de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public List<String> getOperatorNames(ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#getOperatorInformations(de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public List<LogicalOperatorInformation> getOperatorInformations(ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#getOperatorInformation(java.lang.String, de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public LogicalOperatorInformation getOperatorInformation(String name, ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public SDFSchema determinedOutputSchema(String query, String parserID,
			ISession user, int port) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#getRegisteredDatatypes(de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public Set<SDFDatatype> getRegisteredDatatypes(ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#getRegisteredAggregateFunctions(java.lang.String, de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public Set<String> getRegisteredAggregateFunctions(Class<? extends IStreamObject> datamodel, ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

}
