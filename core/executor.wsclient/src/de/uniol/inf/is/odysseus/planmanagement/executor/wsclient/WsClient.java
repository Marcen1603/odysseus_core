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

package de.uniol.inf.is.odysseus.planmanagement.executor.wsclient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.namespace.QName;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IClientExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.WebserviceServer;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.WebserviceServerService;

/**
 * 
 * @author Merlin Wasmann 
 *
 */

public class WsClient implements IExecutor, IClientExecutor{

	// manages the connection to the WebserviceServer
	WebserviceServerService service;
	// create handle for WebserviceServer
	WebserviceServer server;
	// SecurityToken
	String securitytoken;
	
	@Override
	public boolean connect(String connectString) {
		// connectString should look like this:
		// wsdlLocation#service
		String[] subConnect = connectString.split("#");
		if(subConnect.length > 1 && subConnect.length < 3) {
			try {
				startClient(new URL(subConnect[0]), new QName(subConnect[1]));
				return true;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	// init
	public void startClient(URL wsdlLocation, QName service) {
		WsClient client = new WsClient();
		client.service = new WebserviceServerService(wsdlLocation, service);
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
	public void removeQuery(int queryID, ISession caller)
			throws PlanManagementException {
		// TODO: maybe not needed
		if(getWebserviceServer() != null) {
			getWebserviceServer().removeQuery(caller.getToken(), queryID);
		}
	}

	@Override
	public void startQuery(int queryID, ISession caller)
			throws PlanManagementException {
		if(getWebserviceServer() != null) {
			getWebserviceServer().startQuery(caller.getToken(), queryID);
		}
	}

	@Override
	public void stopQuery(int queryID, ISession caller)
			throws PlanManagementException {
		if(getWebserviceServer() != null) {
			getWebserviceServer().stopQuery(caller.getToken(), queryID);
		}
	}

	@Override
	public Collection<String> getQueryBuildConfigurationNames() {
		if(getWebserviceServer() != null) {
			return getWebserviceServer().getQueryBuildConfigurationNames(getSecurityToken()).getResponseValue();
		}
		return null;
	}

	@Override
	public Set<String> getSupportedQueryParsers()
			throws PlanManagementException {
		if(getWebserviceServer() != null) {
			List<String> parsers = getWebserviceServer().getSupportedQueryParsers(getSecurityToken()).getResponseValue();
			// TODO: really a specific type?
			Set<String> parserSet = new HashSet<String>();
			for(String parser : parsers) {
				parserSet.add(parser);
			}
			return parserSet;
		}
		return null;
	}

	@Override
	public List<Integer> startAllClosedQueries(ISession user) {
		if(getWebserviceServer() != null) {
			// TODO: this is always null
			getWebserviceServer().startAllClosedQueries(user.getToken());
		}
		return null;
	}

	@Override
	public Set<String> getRegisteredBufferPlacementStrategiesIDs() {
		if(getWebserviceServer() != null) {
			List<String> ids = getWebserviceServer().getRegisteredBufferPlacementStrategiesIDs(getSecurityToken()).getResponseValue();
			// TODO: really specific type?
			Set<String> idSet = new HashSet<String>();
			for(String id : ids) {
				idSet.add(id);
			}
			return idSet;
		}
		return null;
	}

	@Override
	public Set<String> getRegisteredSchedulingStrategies() {
		if(getWebserviceServer() != null) {
			List<String> strats = getWebserviceServer().getRegisteredSchedulingStrategies(getSecurityToken()).getResponseValue();
			// TODO: really specific type?
			Set<String> stratSet = new HashSet<String>();
			for(String strat : strats) {
				stratSet.add(strat);
			}
			return stratSet;
		}
		return null;
	}

	@Override
	public Set<String> getRegisteredSchedulers() {
		if(getWebserviceServer() != null) {
			List<String> scheds = getWebserviceServer().getRegisteredSchedulingStrategies(getSecurityToken()).getResponseValue();
			// TODO: really specific type?
			Set<String> schedSet = new HashSet<String>();
			for(String sched : scheds) {
				schedSet.add(sched);
			}
			return schedSet;
		}
		return null;
	}

	@Override
	public void setScheduler(String scheduler, String schedulerStrategy) {
		if(getWebserviceServer() != null) {
			getWebserviceServer().setScheduler(getSecurityToken(), scheduler, schedulerStrategy);
		}

	}

	@Override
	public String getCurrentSchedulerID() {
		if(getWebserviceServer() != null) {
			return getWebserviceServer().getCurrentSchedulerID(getSecurityToken()).getResponseValue();
		}
		return null;
	}

	@Override
	public String getCurrentSchedulingStrategyID() {
		if(getWebserviceServer() != null) {
			return getWebserviceServer().getCurrentSchedulingStrategyID(getSecurityToken()).getResponseValue();
		}
		return null;
	}

	public void updateExecutionPlan() throws NoOptimizerLoadedException,
			QueryOptimizationException {
		if(getWebserviceServer() != null) {
			getWebserviceServer().updateExecutionPlan(getSecurityToken());
		}
	}

	@Override
	public String getName() {
		if(getWebserviceServer() != null) {
			return getWebserviceServer().getName(getSecurityToken()).getResponseValue();
		}
		return null;
	}

	@Override
	public ISession login(String username, byte[] password) {
		this.securitytoken = getWebserviceServer().login(username, new String(password)).getResponseValue();
		return null;
	}

	@Override
	public void logout(ISession caller) {
	}

	@Override
	public ILogicalOperator removeSink(String name, ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Entry<String, ILogicalOperator>> getStreamsAndViews(
			ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Entry<String, ILogicalOperator>> getSinks(ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reloadStoredQueries(ISession caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<Integer> addQuery(String query, String parserID,
			ISession user, String queryBuildConfigurationName)
			throws PlanManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer addQuery(ILogicalOperator logicalPlan, ISession user,
			String queryBuildConfigurationName) throws PlanManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer addQuery(List<IPhysicalOperator> physicalPlan,
			ISession user, String queryBuildConfigurationName)
			throws PlanManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeViewOrStream(String name, ISession caller) {
		// TODO Auto-generated method stub
		
	}



}
