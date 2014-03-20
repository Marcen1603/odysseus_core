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
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.ClientReceiver;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IClientExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
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
import de.uniol.inf.is.odysseus.webservice.client.CreateQueryException_Exception;
import de.uniol.inf.is.odysseus.webservice.client.InvalidUserDataException_Exception;
import de.uniol.inf.is.odysseus.webservice.client.LogicalQueryInfo;
import de.uniol.inf.is.odysseus.webservice.client.QueryNotExistsException_Exception;
import de.uniol.inf.is.odysseus.webservice.client.ResourceInformation;
import de.uniol.inf.is.odysseus.webservice.client.ResourceInformationEntry;
import de.uniol.inf.is.odysseus.webservice.client.SdfAttributeInformation;
import de.uniol.inf.is.odysseus.webservice.client.SdfDatatypeInformation;
import de.uniol.inf.is.odysseus.webservice.client.SdfSchemaInformation;
import de.uniol.inf.is.odysseus.webservice.client.StringMapListEntry;
import de.uniol.inf.is.odysseus.webservice.client.WebserviceServer;
import de.uniol.inf.is.odysseus.webservice.client.WebserviceServerService;

/**
 * 
 * @author Merlin Wasmann, Marco Grawunder
 * 
 */
public class WsClient implements IExecutor, IClientExecutor {

	final List<IUpdateEventListener> updateEventListener = new CopyOnWriteArrayList<>();
	
	protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(WsClient.class);
		}
		return _logger;
	}

	@SuppressWarnings("rawtypes")
	private Map<Integer, ClientReceiver> receivers = new HashMap<Integer, ClientReceiver>();

	// manages the connection to the WebserviceServer
	WebserviceServerService service;
	// create handle for WebserviceServer
	WebserviceServer server;

	@Override
	public void addUpdateEventListener(IUpdateEventListener listener) {
		this.updateEventListener.add(listener);
	}
	
	@Override
	public void removeUpdateEventListener(IUpdateEventListener listener) {
		this.updateEventListener.remove(listener);
	}
	
	private void fireUpdateEvent(){
		for (IUpdateEventListener l:updateEventListener){
			l.doUpdate();
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

	@Override
	public ISession login(String username, byte[] password, String tenant) {
		String securitytoken = getWebserviceServer().login(username, new String(password), tenant).getResponseValue();
		IUser user = new WsClientUser(username, password, true);
		WsClientSession session = new WsClientSession(user, tenant);
		session.setToken(securitytoken);
		fireUpdateEvent();
		return session;
	}

	@Override
	public void removeQuery(int queryID, ISession caller) throws PlanManagementException {
		if (getWebserviceServer() != null) {
			try {
				getWebserviceServer().removeQuery(caller.getToken(), queryID);
			} catch (QueryNotExistsException_Exception | InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		fireUpdateEvent();
	}

	@Override
	public void startQuery(int queryID, ISession caller) throws PlanManagementException {
		if (getWebserviceServer() != null) {
			try {
				getWebserviceServer().startQuery(caller.getToken(), queryID);
			} catch (QueryNotExistsException_Exception | InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		fireUpdateEvent();
	}

	@Override
	public void stopQuery(int queryID, ISession caller) throws PlanManagementException {
		if (getWebserviceServer() != null) {
			try {
				getWebserviceServer().stopQuery(caller.getToken(), queryID);
			} catch (QueryNotExistsException_Exception | InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		fireUpdateEvent();
	}

	@Override
	public Collection<String> getQueryBuildConfigurationNames(ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				return getWebserviceServer().getQueryBuildConfigurationNames(caller.getToken()).getResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public Set<String> getSupportedQueryParsers(ISession caller) throws PlanManagementException {
		if (getWebserviceServer() != null) {
			try {
				List<String> parsers = getWebserviceServer().getSupportedQueryParsers(caller.getToken()).getResponseValue();
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
		if (getWebserviceServer() != null) {
			// this is always null
			try {
				getWebserviceServer().startAllClosedQueries(user.getToken());
			} catch (InvalidUserDataException_Exception e) {
				e.printStackTrace();
			}
		}
		fireUpdateEvent();
		return null;
	}

	@Override
	public Set<String> getRegisteredBufferPlacementStrategiesIDs(ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				List<String> ids = getWebserviceServer().getRegisteredBufferPlacementStrategiesIDs(caller.getToken()).getResponseValue();
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
		if (getWebserviceServer() != null) {
			try {
				List<String> strats = getWebserviceServer().getRegisteredSchedulingStrategies(caller.getToken()).getResponseValue();
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
		if (getWebserviceServer() != null) {
			try {
				List<String> scheds = getWebserviceServer().getRegisteredSchedulingStrategies(caller.getToken()).getResponseValue();
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
		if (getWebserviceServer() != null) {
			try {
				getWebserviceServer().setScheduler(caller.getToken(), scheduler, schedulerStrategy);
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}

	}

	@Override
	public String getCurrentSchedulerID(ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				return getWebserviceServer().getCurrentSchedulerID(caller.getToken()).getResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public String getCurrentSchedulingStrategyID(ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				return getWebserviceServer().getCurrentSchedulingStrategyID(caller.getToken()).getResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public String getName() {
		if (getWebserviceServer() != null) {
			return getWebserviceServer().getName().getResponseValue();
		}
		return null;
	}

	@Override
	public Collection<Integer> addQuery(String query, String parserID, ISession user, String queryBuildConfigurationName, Context context) throws PlanManagementException { //
		try {
			Collection<Integer> response = getWebserviceServer().addQuery(user.getToken(), parserID, query, queryBuildConfigurationName, context).getResponseValue();
			fireUpdateEvent();
			return response;
		} catch (InvalidUserDataException_Exception | CreateQueryException_Exception e) {
			throw new PlanManagementException(e);
		}
		
	}

	@Override
	public ILogicalQuery getLogicalQueryById(int id, ISession caller) {
		try {
			return createLogicalQueryFromInfo(getWebserviceServer().getLogicalQueryById(caller.getToken(), Integer.toString(id)).getResponseValue(), caller);
		} catch (InvalidUserDataException_Exception | QueryNotExistsException_Exception e) {
			throw new PlanManagementException(e);
		}
	}

	@Override
	public ILogicalQuery getLogicalQueryByName(String name, ISession caller) {
		try {
			return createLogicalQueryFromInfo(getWebserviceServer().getLogicalQueryByName(caller.getToken(), name).getResponseValue(), caller);
		} catch (InvalidUserDataException_Exception e) {
			throw new PlanManagementException(e);
		}
	}

	/**
	 * method to create an instance of LogicalQuery from an instance of
	 * LogicalQueryInfo
	 * 
	 * @param info
	 * @return LogicalQuery
	 */
	public LogicalQuery createLogicalQueryFromInfo(LogicalQueryInfo info, ISession caller) {
		LogicalQuery query = new LogicalQuery(info.getId(), info.getParserID(), null, info.getPriority());
		query.setContainsCycles(info.isContainsCycles());
		query.setQueryText(info.getQueryText());
		query.setUser(caller);
		return query;
	}

	@Override
	public void logout(ISession caller) {
		getWebserviceServer().logout(caller.getToken());
		fireUpdateEvent();
	}

	@Override
	public Collection<Integer> getLogicalQueryIds(ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				return getWebserviceServer().getLogicalQueryIds(caller.getToken()).getResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<IPhysicalOperator> getPhysicalRoots(int queryID, ISession caller) {
		List<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>();
		Optional<ClientReceiver> receiver = createClientReceiver(this, queryID, caller);
		if (receiver.isPresent()) {
			roots.add(receiver.get());
		}
		return roots;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Optional<ClientReceiver> createClientReceiver(IExecutor exec, int queryId, ISession caller) {
		SDFSchema outputSchema = exec.getOutputSchema(queryId, caller);
		IDataHandler dataHandler = DataHandlerRegistry.getDataHandler("Tuple", outputSchema);
		InetSocketAddress adr = (InetSocketAddress) ((IClientExecutor) exec).getSocketConnectionInformation(queryId, caller);
		Map<String, String> options = new HashMap<>();
		options.put("port", "" + adr.getPort());
		options.put("host", adr.getHostName());
		// TODO username and password get from anywhere
		if (receivers.containsKey(queryId)) {
			return Optional.of(receivers.get(queryId));
		}
		IProtocolHandler h = ProtocolHandlerRegistry.getInstance("SizeByteBuffer", ITransportDirection.IN, IAccessPattern.PUSH, options, dataHandler);
		// Must be done to add the transport to the protocoll ... seems not
		// really intuitive ...
		ITransportHandler th = TransportHandlerRegistry.getInstance("TCPClient", h, options);
		h.setTransportHandler(th);
		ClientReceiver receiver = new ClientReceiver(h);
		receiver.setOutputSchema(outputSchema);
		receiver.open(null, 0, 0, null, null);
		receivers.put(queryId, receiver);
		return Optional.of(receiver);

	}

	/**
	 * Returns a SocketAddress object
	 * 
	 * @param queryId
	 * @return SocketAddress
	 */
	@Override
	public SocketAddress getSocketConnectionInformation(int queryId, ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				ConnectionInformation info = getWebserviceServer().getConnectionInformation(caller.getToken(), queryId).getResponseValue();
				return new InetSocketAddress(InetAddress.getByName(info.getAddress()), info.getPort());
			} catch (UnknownHostException | InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public SDFSchema getOutputSchema(int queryId, ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				return createSchemaFromInformation(getWebserviceServer().getOutputSchemaByQueryId(caller.getToken(), queryId).getResponseValue());
			} catch (InvalidUserDataException_Exception | QueryNotExistsException_Exception e) {
				throw new PlanManagementException(e);
			}
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
		// TODO Extend SdfAttributeInformation 
		return new SDFAttribute(info.getSourcename(), info.getAttributename(), createDatatypeFromInformation(info.getDatatype()), null, null, null);
	}

	private SDFDatatype createDatatypeFromInformation(SdfDatatypeInformation info) {
		return new SDFDatatype(info.getUri());
	}

	@Override
	public ILogicalOperator removeSink(String name, ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				ILogicalOperator result = (ILogicalOperator) getWebserviceServer().removeSinkByName(name, caller.getToken()).getResponseValue();
				fireUpdateEvent();
				return result;
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public ILogicalOperator removeSink(Resource name, ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				ResourceInformation ri = new ResourceInformation();
				ri.setResourceName(name.getResourceName());
				ri.setUser(name.getUser());
				ILogicalOperator result = (ILogicalOperator) getWebserviceServer().removeSinkByResource(ri, caller.getToken()).getResponseValue();
				fireUpdateEvent();
				return result;
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public void removeViewOrStream(String name, ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				getWebserviceServer().removeViewOrStreamByName(name, caller.getToken());
				fireUpdateEvent();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
	}

	@Override
	public void removeViewOrStream(Resource name, ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				ResourceInformation ri = new ResourceInformation();
				ri.setResourceName(name.getResourceName());
				ri.setUser(name.getUser());
				getWebserviceServer().removeViewOrStreamByResource(ri, caller.getToken());
				fireUpdateEvent();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}

	}

	@Override
	public Set<Entry<Resource, ILogicalOperator>> getStreamsAndViews(ISession caller) {
		HashMap<Resource, ILogicalOperator> set = new HashMap<>();
		if (getWebserviceServer() != null) {
			try {
				List<ResourceInformationEntry> result = getWebserviceServer().getStreamsAndViews(caller.getToken()).getResponseValue();

				for (ResourceInformationEntry e : result) {
					Resource r = new Resource(e.getResource().getUser(), e.getResource().getResourceName());
					ILogicalOperator op = (ILogicalOperator) e.getOperator();
					set.put(r, op);
				}
				return set.entrySet();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return set.entrySet();
	}

	@Override
	public Set<Entry<Resource, ILogicalOperator>> getSinks(ISession caller) {
		HashMap<Resource, ILogicalOperator> set = new HashMap<>();
		if (getWebserviceServer() != null) {
			try {
				List<ResourceInformationEntry> result = getWebserviceServer().getSinks(caller.getToken()).getResponseValue();

				for (ResourceInformationEntry e : result) {
					Resource r = new Resource(e.getResource().getUser(), e.getResource().getResourceName());
					ILogicalOperator op = (ILogicalOperator) e.getOperator();
					set.put(r, op);
				}
				return set.entrySet();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return set.entrySet();
	}

	@Override
	public void reloadStoredQueries(ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				getWebserviceServer().reloadStoredQueries(caller.getToken());
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}

	}

	@Override
	public boolean containsViewOrStream(Resource name, ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				ResourceInformation ri = new ResourceInformation();
				ri.setResourceName(name.getResourceName());
				ri.setUser(name.getUser());
				return getWebserviceServer().containsViewOrStreamByResource(ri, caller.getToken()).isResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return false;
	}

	@Override
	public boolean containsViewOrStream(String name, ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				return getWebserviceServer().containsViewOrStreamByName(name, caller.getToken()).isResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return false;
	}

	@Override
	public void addStoredProcedure(String name, StoredProcedure sp, ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				getWebserviceServer().addStoredProcedure(name, sp, caller.getToken());
				fireUpdateEvent();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
	}

	@Override
	public StoredProcedure getStoredProcedure(String name, ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				StoredProcedure result = getWebserviceServer().getStoredProcedure(name, caller.getToken()).getResponseValue();
				fireUpdateEvent();
				return result;
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public void removeStoredProcedure(String name, ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				getWebserviceServer().removeStoredProcedure(name, caller.getToken());
				fireUpdateEvent();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
	}

	@Override
	public List<StoredProcedure> getStoredProcedures(ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				
				return getWebserviceServer().getStoredProcedures(caller.getToken()).getResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return new ArrayList<>();
	}

	@Override
	public boolean containsStoredProcedures(String name, ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				return getWebserviceServer().containsStoredProcedures(name, caller.getToken()).isResponseValue();
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
		if (getWebserviceServer() != null) {
			try {
				return getWebserviceServer().getOperatorNames(caller.getToken()).getResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return new ArrayList<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#
	 * getOperatorInformations
	 * (de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public List<LogicalOperatorInformation> getOperatorInformations(ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				return getWebserviceServer().getOperatorInformations(caller.getToken()).getResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return new ArrayList<>();
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
		if (getWebserviceServer() != null) {
			try {
				return getWebserviceServer().getOperatorInformation(name, caller.getToken()).getResponseValue();
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	@Override
	public SDFSchema determineOutputSchema(String query, String parserID, ISession user, int port, Context context) {
		if (getWebserviceServer() != null) {
			try {
				SdfSchemaInformation si = getWebserviceServer().determineOutputSchema(query, parserID, user.getToken(), port, context).getResponseValue();
				List<SDFAttribute> attributes = new ArrayList<>();
				// TODO: extends SdfAttributeInformation
				for (SdfAttributeInformation sda : si.getAttributes()) {
					SDFDatatype dt = new SDFDatatype(sda.getDatatype().getUri());
					attributes.add(new SDFAttribute(sda.getSourcename(), sda.getAttributename(), dt, null, null, null));
				}
				@SuppressWarnings({ "rawtypes", "unchecked" })
				Class<? extends IStreamObject> type = (Class<? extends IStreamObject>) Class.forName(si.getTypeClass());
				SDFSchema schema = new SDFSchema(si.getUri(), type, attributes);
				return schema;
			} catch (InvalidUserDataException_Exception | ClassNotFoundException e) {
				throw new PlanManagementException(e);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#
	 * getRegisteredDatatypes
	 * (de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public Set<SDFDatatype> getRegisteredDatatypes(ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				HashSet<SDFDatatype> set = new HashSet<>();
				List<SdfDatatypeInformation> dts = getWebserviceServer().getRegisteredDatatypes(caller.getToken()).getResponseValue();
				for (SdfDatatypeInformation dt : dts) {
					set.add(new SDFDatatype(dt.getUri()));
				}
				return set;
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return new HashSet<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#
	 * getRegisteredAggregateFunctions(java.lang.String,
	 * de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public Set<String> getRegisteredAggregateFunctions(@SuppressWarnings("rawtypes") Class<? extends IStreamObject> datamodel, ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				HashSet<String> set = new HashSet<>();
				set.addAll(getWebserviceServer().getRegisteredAggregateFunctions(datamodel.getName(), caller.getToken()).getResponseValue());
				return set;
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return new HashSet<>();
	}

	@Override
	public Set<String> getRegisteredWrapperNames(ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				HashSet<String> set = new HashSet<>();
				set.addAll(getWebserviceServer().getRegisteredWrapperNames(caller.getToken()).getResponseValue());
				return set;
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return new HashSet<>();
	}

	@Override
	public Map<String, List<String>> getQueryParserTokens(String queryParser, ISession user) {
		HashMap<String, List<String>> result = new HashMap<>();
		if (getWebserviceServer() != null) {
			try {
				List<StringMapListEntry> entries = getWebserviceServer().getQueryParserTokens(queryParser, user.getToken()).getResponseValue();
				for (StringMapListEntry e : entries) {
					result.put(e.getKey(), e.getValue());
				}
			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return result;
	}

	@Override
	public List<String> getQueryParserSuggestions(String queryParser, String hint, ISession caller) {
		if (getWebserviceServer() != null) {
			try {
				return getWebserviceServer().getQueryParserSuggestions(queryParser, hint, caller.getToken()).getResponseValue();

			} catch (InvalidUserDataException_Exception e) {
				throw new PlanManagementException(e);
			}
		}
		return new ArrayList<>();
	}

}
