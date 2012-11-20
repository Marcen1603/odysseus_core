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

package de.uniol.inf.is.odysseus.planmanagement.executor.gwtclient;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import de.uniol.inf.is.odysseus.planmanagement.executor.gwtclient.util.GwtClientSession;
import de.uniol.inf.is.odysseus.planmanagement.executor.gwtclient.util.GwtClientUser;
import de.uniol.inf.is.odysseus.webservice.client.ConnectionInformation;
import de.uniol.inf.is.odysseus.webservice.client.LogicalQueryInfo;
import de.uniol.inf.is.odysseus.webservice.client.OperatorBuilderInformation;
import de.uniol.inf.is.odysseus.webservice.client.WebserviceServer;
import de.uniol.inf.is.odysseus.webservice.client.WebserviceServerService;

/**
 * @author Merlin Wasmann
 * 
 */
public class GwtClient {

	// Map of all added queries accessible by queryId
	private Map<Integer, LogicalQueryInfo> queries = new HashMap<Integer, LogicalQueryInfo>();
	// manages the connection to the WebserviceServer
	private WebserviceServerService service;
	// create handle for WebserviceServer
	private WebserviceServer server;
	// SecurityToken
	private String securitytoken;
	// User
	private GwtClientSession user;

	/**
	 * connect
	 * 
	 * @param connectString
	 *            String: expected format is
	 *            wsdlLocation;serviceNamespace;service
	 */
	public boolean connect(String connectString) {
		// connectString should look like this:
		// wsdlLocation;serviceNamespace;service
		String[] subConnect = connectString.split(";");
		if (subConnect.length > 1 && subConnect.length < 4) {
			try {
				startClient(new URL(subConnect[0]), new QName(subConnect[1],
						subConnect[2]));
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

	public GwtClientSession login(String username, byte[] password) {
		this.securitytoken = getWebserviceServer().login(username,
				new String(password)).getResponseValue();
		GwtClientUser user = new GwtClientUser(username, password, true);
		GwtClientSession session = new GwtClientSession(user);
		session.setToken(this.securitytoken);
		this.user = session;
		return session;
	}

	public void logout(GwtClientSession caller) {
		getWebserviceServer().logout(caller.getToken());
	}

	private String getSecurityToken() {
		return this.securitytoken;
	}

	public SocketAddress getSocketConnectionInformation(int queryId) {
		if (getWebserviceServer() != null) {
			ConnectionInformation info = getWebserviceServer()
					.getConnectionInformation(getSecurityToken(), queryId)
					.getResponseValue();
			try {
				return new InetSocketAddress(InetAddress.getByName(info
						.getAddress()), info.getPort());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public GwtClientSession getUser() {
		return this.user;
	}

	public Collection<Integer> addQuery(String query, String parserID,
			GwtClientSession user, String queryBuildConfigurationName) {
		if(user == null) {
			System.out.println("[GWT] User is null");
		}
		if(query.isEmpty()) {
			System.out.println("[GWT] Query is empty");
		}
		if(parserID.isEmpty()) {
			System.out.println("[GWT] ParserID is empty");
		}
		if(queryBuildConfigurationName.isEmpty()) {
			System.out.println("[GWT] QueryBuildConfigurationName is empty");
		}
		Collection<Integer> response = getWebserviceServer().addQuery(
				user.getToken(), parserID, query, queryBuildConfigurationName)
				.getResponseValue();
		for (Integer val : response) {
			this.queries.put(val, getLogicalQuery(val));
		}
		return response;
	}

	private LogicalQueryInfo getLogicalQuery(int id) {
		return getWebserviceServer().getLogicalQuery(getSecurityToken(),
				"" + id).getResponseValue();
	}

	public List<OperatorBuilderInformation> getOperatorList() {
		if(getWebserviceServer() != null) {
			return getWebserviceServer().getOperatorBuilderList(getSecurityToken()).getResponseValue();
		}
		return null;
	}
	
	// FIXME LogicalQuery muss nachgebaut werden
//	public ILogicalQuery getLogicalQuery(int id) {
//		return null;
//	}

	public Collection<Integer> getLogicalQueryIds() {
		if (getWebserviceServer() != null) {
			return getWebserviceServer().getLogicalQueryIds(getSecurityToken())
					.getResponseValue();
		}
		return null;
	}

	public String getName() {
		if (getWebserviceServer() != null) {
			return getWebserviceServer().getName(getSecurityToken())
					.getResponseValue();
		}
		return null;
	}

	public Collection<Integer> startAllClosedQueries(GwtClientSession user) {
		if (getWebserviceServer() != null) {
			// this is always null
			getWebserviceServer().startAllClosedQueries(user.getToken());
		}
		return null;
	}

	public void removeQuery(int queryID, GwtClientSession caller) {
		if (getWebserviceServer() != null) {
			getWebserviceServer().removeQuery(caller.getToken(), queryID);
			queries.remove(queryID);
		}
	}

	public void startQuery(int queryID, GwtClientSession caller) {
		if (getWebserviceServer() != null) {
			getWebserviceServer().startQuery(caller.getToken(), queryID);
		}
	}

	public void stopQuery(int queryID, GwtClientSession caller) {
		if (getWebserviceServer() != null) {
			getWebserviceServer().stopQuery(caller.getToken(), queryID);
		}
	}
	
	public LogicalQueryInfo getLogicalQuery(int queryID, GwtClientSession caller) {
		if(getWebserviceServer() != null) {
			return getWebserviceServer().getLogicalQuery(caller.getToken(), "" + queryID).getResponseValue();
		}
		return null;
	}

	/***
	 * not needed in here ***
	 * 
	 * public Set<Entry<String, ILogicalOperator>> getStreamsAndViews(
	 * GwtClientSession caller) { return null; }
	 * 
	 * public Set<Entry<String, ILogicalOperator>> getSinks(GwtClientSession
	 * caller) { return null; }
	 * 
	 * public ILogicalOperator removeSink(String name, GwtClientSession caller)
	 * { return null; }
	 * 
	 * public ILogicalOperator removeSink(String name, GwtClientSession caller)
	 * { return null; }
	 * 
	 * public Integer addQuery(ILogicalOperator logicalPlan, GwtClientSession
	 * user, String queryBuildConfigurationName) throws PlanManagementException
	 * { return null; }
	 * 
	 * 
	 * public Integer addQuery(List<IPhysicalOperator> physicalPlan,
	 * GwtClientSession user, String queryBuildConfigurationName) throws
	 * PlanManagementException { return null; }
	 * 
	 * public void removeViewOrStream(String name, GwtClientSession caller) {
	 * 
	 * }
	 * 
	 * public void addQueryListener(IQueryListener listener) {
	 * 
	 * }
	 * 
	 * public void removeQueryListener(IQueryListener listener) {
	 * 
	 * }
	 * 
	 * public SDFSchema getOutputSchema(int queryId) { return null; }
	 * 
	 * public List<IPhysicalOperator> getPhysicalRoots(int queryID) { return
	 * null; }
	 * 
	 * public Collection<String> getQueryBuildConfigurationNames() { return
	 * null; }
	 * 
	 * public Set<String> getSupportedQueryParsers() { return null; }
	 * 
	 * public void reloadStoredQueries(GwtClientSession caller) {
	 * 
	 * } public Set<String> getRegisteredBufferPlacementStrategiesIDs() { return
	 * null; }
	 * 
	 * public Set<String> getRegisteredSchedulingStrategies() { return null; }
	 * 
	 * public Set<String> getRegisteredSchedulers() { return null; }
	 * 
	 * public void setScheduler(String scheduler, String schedulerStrategy) {
	 * 
	 * }
	 * 
	 * public String getCurrentSchedulerID() { return null; }
	 * 
	 * public String getCurrentSchedulingStrategyID() { return null; }
	 **/
}
