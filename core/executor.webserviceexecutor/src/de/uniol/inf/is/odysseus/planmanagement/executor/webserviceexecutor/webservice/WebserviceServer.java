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

package de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice;

import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Endpoint;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.exception.WebserviceException;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.response.BooleanResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.response.GraphNode;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.response.Response;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.response.SimpleGraph;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.response.StringListResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.response.StringResponse;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.usermanagement.ISession;
import de.uniol.inf.is.odysseus.usermanagement.ISession;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;

/**
 * 
 * @author Dennis Geesen Created at: 09.08.2011
 */

@WebService
@SOAPBinding(style = Style.DOCUMENT)
@XmlSeeAlso({ SimpleGraph.class, String[].class, GraphNode.class })
public class WebserviceServer {

	public static void startServer() {
		WebserviceServer server = new WebserviceServer();		
		Endpoint endpoint = Endpoint.publish("http://0.0.0.0:9669/odysseus", server);
		if (endpoint.isPublished()) {
			Logger.getAnonymousLogger().log(Level.FINE, "Webservice published!");
		}
	}

	protected IExecutor getExecutor(){
		return ExecutorServiceBinding.getExecutor();
	}
	
	@WebResult(name = "securitytoken")
	public StringResponse login(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
		ISession user = UserManagement.getSessionmanagement().login(username, password.getBytes());
		if (user != null) {
			String token = SessionManagement.getInstance().createNewSession(user);
			StringResponse response = new StringResponse(token, true);
			return response;
		} else {
			return new StringResponse(null, false);
		}
	}

	public Response addQuery(@WebParam(name = "securitytoken") String securityToken, @WebParam(name = "parser") String parser, @WebParam(name = "query") String query,
			@WebParam(name = "transformationconfig") String transCfg) {
		try {
			ISession user = loginWithSecurityToken(securityToken);			
			IDataDictionary dd = GlobalState.getActiveDatadictionary();
			ExecutorServiceBinding.getExecutor().addQuery(query, parser, user, dd,transCfg);
			return new Response(true);
		} catch (WebserviceException e) {
			e.printStackTrace();
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
		return new Response(false);
	}

	public StringListResponse getInstalledSources(@WebParam(name = "securitytoken") String securityToken) {
		StringListResponse response = new StringListResponse(true);
		try {
			ISession user = loginWithSecurityToken(securityToken);
			IDataDictionary dd = GlobalState.getActiveDatadictionary();
			for (Entry<String, ILogicalOperator> e : dd.getStreams(user)) {
				response.addResponseValue(e.getKey());
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return new StringListResponse(false);
		}

	}
	
	public StringListResponse getInstalledQueries(@WebParam(name="securitytoken") String securityToken){
		StringListResponse response = new StringListResponse(true);
		try{
			loginWithSecurityToken(securityToken);			
			for(IQuery q : ExecutorServiceBinding.getExecutor().getPlan().getQueries()){
				response.addResponseValue(q.getQueryText());
			}
			return response;
		}catch (Exception e) {
			e.printStackTrace();
			return new StringListResponse(false);
		}
	}

	protected ISession loginWithSecurityToken(String securityToken) throws WebserviceException {
		if (SessionManagement.getInstance().isValidSession(securityToken)) {
			// TODO: ---> Session initalisieren, kein User mehr
			//ISession user = SessionManagement.getInstance().getUser(securityToken);
			//return user;
			return null;
		} else {
			throw new WebserviceException("Security token unknown! You have to login first to obtain a security token!");
		}
	}

	public Response removeQuery(@WebParam(name = "securitytoken") String securityToken, @WebParam(name = "queryID") int queryID) {
		try {
			ISession user = loginWithSecurityToken(securityToken);
			ExecutorServiceBinding.getExecutor().removeQuery(queryID, user);
			return new Response(true);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(false);
		}
	}

	public Response startQuery(@WebParam(name = "securitytoken") String securityToken, @WebParam(name = "queryID") int queryID) {
		try {
			ISession user = loginWithSecurityToken(securityToken);
			ExecutorServiceBinding.getExecutor().startQuery(queryID, user);
			return new Response(true);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(false);
		}

	}

	public Response stopQuery(@WebParam(name = "securitytoken") String securityToken, @WebParam(name = "queryID") int queryID) {
		try {
			ISession user = loginWithSecurityToken(securityToken);
			ExecutorServiceBinding.getExecutor().stopQuery(queryID, user);
			return new Response(true);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(false);
		}

	}

	public Response startExecution(@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			ExecutorServiceBinding.getExecutor().startExecution();
			return new Response(true);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(false);
		}

	}

	public Response stopExecution(@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			ExecutorServiceBinding.getExecutor().stopExecution();
			return new Response(true);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(false);
		}
	}

	public BooleanResponse isRunning(@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			boolean running = ExecutorServiceBinding.getExecutor().isRunning();
			return new BooleanResponse(running, true);
		} catch (Exception e) {
			e.printStackTrace();
			return new BooleanResponse(false, false);
		}
	}

	public StringResponse getInfos(@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new StringResponse(ExecutorServiceBinding.getExecutor().getInfos(), true);
		} catch (Exception e) {
			e.printStackTrace();
			return new StringResponse("", false);
		}
	}

	public Response initialize(@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			ExecutorServiceBinding.getExecutor().initialize();
			return new Response(true);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(false);
		}
	}

	public StringListResponse getQueryBuildConfigurationNames(@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new StringListResponse(ExecutorServiceBinding.getExecutor().getQueryBuildConfigurationNames(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new StringListResponse(false);
	}

	public StringListResponse getSupportedQueryParsers(@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			Set<String> parsers = ExecutorServiceBinding.getExecutor().getSupportedQueryParsers();
			return new StringListResponse(parsers, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new StringListResponse(false);
	}

	public Response startAllClosedQueries(@WebParam(name = "securitytoken") String securityToken) {
		try {
			ISession user = loginWithSecurityToken(securityToken);
			ExecutorServiceBinding.getExecutor().startAllClosedQueries(user);
			return new Response(true);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(false);
		}
	}

	public StringListResponse getRegisteredBufferPlacementStrategiesIDs(@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new StringListResponse(ExecutorServiceBinding.getExecutor().getRegisteredBufferPlacementStrategiesIDs(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new StringListResponse(false);
	}

	public StringListResponse getRegisteredSchedulingStrategies(@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new StringListResponse(ExecutorServiceBinding.getExecutor().getRegisteredSchedulingStrategies(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new StringListResponse(false);
	}

	public StringListResponse getRegisteredSchedulers(@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new StringListResponse(ExecutorServiceBinding.getExecutor().getRegisteredSchedulers(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new StringListResponse(false);
	}

	public Response setScheduler(@WebParam(name = "securitytoken") String securityToken, @WebParam(name = "scheduler") String scheduler,
			@WebParam(name = "scheduler_strategy") String schedulerStrategy) {
		try {
			loginWithSecurityToken(securityToken);
			ExecutorServiceBinding.getExecutor().setScheduler(scheduler, schedulerStrategy);
			return new Response(true);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(false);
		}
	}

	public StringResponse getCurrentSchedulerID(@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new StringResponse(ExecutorServiceBinding.getExecutor().getCurrentSchedulerID(), true);
		} catch (Exception e) {
			e.printStackTrace();
			return new StringResponse("", false);
		}
	}

	public StringResponse getCurrentSchedulingStrategyID(@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new StringResponse(ExecutorServiceBinding.getExecutor().getCurrentSchedulingStrategyID(), true);
		} catch (Exception e) {
			e.printStackTrace();
			return new StringResponse("", false);
		}
	}

	public Response updateExecutionPlan(@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			ExecutorServiceBinding.getExecutor().updateExecutionPlan();
			return new Response(true);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(false);
		}

	}

	public StringResponse getName(@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new StringResponse(ExecutorServiceBinding.getExecutor().getName(), true);
		} catch (Exception e) {
			e.printStackTrace();
			return new StringResponse("", false);
		}
	}

	public SimpleGraph getPlan(@WebParam(name ="securitytoken") String securityToken){
		try{
			SimpleGraph graph = new SimpleGraph();
			//loginWithSecurityToken(securityToken);
			IPlan plan = ExecutorServiceBinding.getExecutor().getPlan();		
			int idCounter = 0;
			for(IPhysicalOperator op : plan.getRoots()){
				GraphNodeVisitor<IPhysicalOperator> visitor = new GraphNodeVisitor<IPhysicalOperator>();		
				visitor.setIdCounter(idCounter);
				@SuppressWarnings("rawtypes")
				AbstractGraphWalker walker = new AbstractGraphWalker();
				walker.prefixWalkPhysical(op, visitor);			
				graph.addRootNode(visitor.getResult());	
				idCounter = visitor.getIdCounter();
			}
			return graph;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
