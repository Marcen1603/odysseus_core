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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.ExecutorInitializeException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.WebserviceServer;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.WebserviceServerService;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

/**
 * 
 * @author Merlin Wasmann 
 *
 */

public class WsClient implements IExecutor {
	
	// init
	public static void startClient() {
		WsClient client = new WsClient();
		client.service = new WebserviceServerService();
		client.server = client.service.getWebserviceServerPort();
		// TODO: check if this works
		client.securitytoken = client.server.login("System", "manager").getResponseValue();
	}
	
	// manages the connection to the WebserviceServer
	WebserviceServerService service;
	// create handle for WebserviceServer
	WebserviceServer server;
	// SecurityToken
	String securitytoken;
	
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
	public IPlan getPlan() throws PlanManagementException {
		// TODO: not implemented this way by server
		return null;
	}

	@Override
	public void addPlanModificationListener(IPlanModificationListener listener) {
		// TODO: not implemented by server
	}

	@Override
	public void removePlanModificationListener(
			IPlanModificationListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public IExecutionPlan getExecutionPlan() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startExecution() throws PlanManagementException {
		if(getWebserviceServer() != null) {
			getWebserviceServer().startExecution(getSecurityToken());
		}
	}

	@Override
	public void stopExecution() throws PlanManagementException {
		if(getWebserviceServer() != null) {
			getWebserviceServer().stopExecution(getSecurityToken());
		}
	}

	@Override
	public boolean isRunning() throws PlanManagementException {
		if(getWebserviceServer() != null) {
			// TODO: maybe not the right method on BooleanResponse
			return getWebserviceServer().isRunning(getSecurityToken()).isSuccessful();
		}
		return false;
	}

	@Override
	public void addPlanExecutionListener(IPlanExecutionListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePlanExecutionListener(IPlanExecutionListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getInfos() {
		if(getWebserviceServer() != null) {
			return getWebserviceServer().getInfos(getSecurityToken()).getResponseValue();
		}
		return null;
	}

	@Override
	public void addErrorEventListener(IErrorEventListener errorEventListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeErrorEventListener(IErrorEventListener errorEventListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireErrorEvent(ErrorEvent eventArgs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void errorEventOccured(ErrorEvent eventArgs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() throws ExecutorInitializeException {
		// TODO Auto-generated method stub

	}

	@Override
	public ExecutionConfiguration getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getQueryBuildConfigurationNames() {
		if(getWebserviceServer() != null) {
			return getWebserviceServer().getQueryBuildConfigurationNames(getSecurityToken()).getResponseValue();
		}
		return null;
	}

	@Override
	public IQueryBuildConfiguration getQueryBuildConfiguration(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, IQueryBuildConfiguration> getQueryBuildConfigurations() {
		// TODO Auto-generated method stub
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
	public Collection<IQuery> addQuery(String query, String parserID,
			ISession user, String queryBuildConfigurationName)
			throws PlanManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IQuery addQuery(ILogicalOperator logicalPlan, ISession user,
			String queryBuildConfigurationName) throws PlanManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IQuery addQuery(List<IPhysicalOperator> physicalPlan, ISession user,
			String queryBuildConfigurationName) throws PlanManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IQuery> startAllClosedQueries(ISession user) {
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
				stratSet.add(sched);
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

	@Override
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void logout(ISession caller) {
		// TODO: not implemented in server
	}

	@Override
	public List<IQuery> translateQuery(String query, String parserID,
			ISession user) throws QueryParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void transform(IQuery query,
			TransformationConfiguration transformationConfiguration,
			ISession caller) throws TransformationException {
		// TODO Auto-generated method stub

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

}
