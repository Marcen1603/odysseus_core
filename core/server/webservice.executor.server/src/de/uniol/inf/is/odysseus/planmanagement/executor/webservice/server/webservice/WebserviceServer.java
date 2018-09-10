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

package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.planmanagement.SinkInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.sdf.SDFDatatypeInformation;
import de.uniol.inf.is.odysseus.core.sdf.SDFSchemaInformation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.MapParameter;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.ByteBufferSinkStreamHandlerBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.NioByteBufferSinkStreamHandlerBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SocketSinkPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.OperatorBuilderFactoryServiceBinding;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.exception.CreateQueryException;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.exception.DetermineOutputSchemaException;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.exception.InvalidUserDataException;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.exception.QueryNotExistsException;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.BooleanResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.ConnectionInformation;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.ConnectionInformationResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.GraphNode;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.IntegerCollectionResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.LogicalOperatorInformationListResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.LogicalOperatorInformationResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.LogicalOperatorResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.OperatorBuilderInformation;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.OperatorBuilderListResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.ParameterInfo;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.QueryResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.ResourceInformation;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.Response;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.SDFDatatypeListResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.SDFSchemaResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.SimpleGraph;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.SinkInformationWS;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.SourceInformation;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.SourceListResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.StoredProcedureListResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.StoredProcedureResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.StringListResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.StringMapListEntry;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.StringMapStringListResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.StringResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.ViewInformationWS;
//import de.uniol.inf.is.odysseus.relational_interval.replacement.ReplacementRegistry;
import de.uniol.inf.is.odysseus.security.ssl.SSLServerSocketProvider;

/**
 *
 * @author Dennis Geesen, Thore Stratmann, Marco Grawunder
 */

@WebService
@SOAPBinding(style = Style.DOCUMENT)
@XmlSeeAlso({ SimpleGraph.class, String[].class, GraphNode.class, LogicalQuery.class, ConnectionInformation.class,
		Context.class })
public class WebserviceServer {

	static final private Logger logger = LoggerFactory.getLogger(WebserviceServer.class);

	private static final int SINK_MIN_PORT = OdysseusConfiguration.instance.getInt("webservice.queryconnect.sink.minport",
			10000);
	private static final int SINK_MAX_PORT = OdysseusConfiguration.instance.getInt("webservice.queryconnect.sink.maxport",
			20000);;

	/**
	 * Socket Management
	 */

	@XmlTransient
	private Map<Integer, Map<Integer, Integer>> socketPortMap = new HashMap<>();
	private Map<Integer, Map<Integer, SocketSinkPO>> socketSinkMap = new HashMap<>();
	private List<String> address;

	protected IExecutor getExecutor() {
		return ExecutorServiceBinding.getExecutor();
	}

	protected IOperatorBuilderFactory getOperatorBuilderFactory() {
		return OperatorBuilderFactoryServiceBinding.getOperatorBuilderFactory();
	}

	@WebResult(name = "securitytoken")
	public StringResponse login(@WebParam(name = "username") String username,
			@WebParam(name = "password") String password, @WebParam(name = "tenantname") String tenantname) {
		ITenant tenant = UserManagementProvider.instance.getTenant(tenantname);
		ISession user = SessionManagement.instance.login(username, password.getBytes(), tenant);
		if (user != null) {
			String token = user.getToken();
			StringResponse response = new StringResponse(token, true);
			logger.debug("New user " + username + "@" + tenantname + " connected.");
			return response;
		}
		return new StringResponse(null, false);
	}

	@WebResult(name = "securitytoken")
	public StringResponse login2(@WebParam(name = "username") String username,
			@WebParam(name = "password") String password) {
		ITenant tenant = UserManagementProvider.instance.getDefaultTenant();
		ISession user = SessionManagement.instance.login(username, password.getBytes(), tenant);
		if (user != null) {
			String token = user.getToken();
			StringResponse response = new StringResponse(token, true);
			logger.debug("New user " + username + " connected.");
			return response;
		}
		return new StringResponse(null, false);
	}

	public Response logout(@WebParam(name = "securitytoken") String securityToken) {
		ISession session = SessionManagement.instance.login(securityToken);
		if (session != null) {
			SessionManagement.instance.logout(session);
			for (Map<Integer, SocketSinkPO> entry : socketSinkMap.values()) {
				for (SocketSinkPO po : entry.values()) {
					po.removeAllowedSessionId(securityToken);
				}
			}
			return new Response(true);
		}
		return new Response(false);
	}

	public Response isValidSession(@WebParam(name = "securitytoken") String securityToken) {
		ISession session = SessionManagement.instance.login(securityToken);
		if (session != null) {
			return new Response(getExecutor().isValid(session));
		}
		return new Response(false);
	}

	public IntegerCollectionResponse addQuery2(@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "parser") String parser, @WebParam(name = "query") String query,
			@WebParam(name = "context") Context context) throws CreateQueryException, InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		try {
			IntegerCollectionResponse response = new IntegerCollectionResponse(
					ExecutorServiceBinding.getExecutor().addQuery(query, parser, user, context), true);
			return response;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new CreateQueryException(e.toString());
		}
	}

	public Response runCommand(@WebParam(name = "securitytoken") String securityToken, @WebParam(name = "command") String command) throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		try{
			ExecutorServiceBinding.getExecutor().runCommand(command, user);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		return new Response(true);

	}



	public StringListResponse getInstalledSources(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		StringListResponse response = new StringListResponse(true);
		ISession user = loginWithSecurityToken(securityToken);
		for (ViewInformation e : getExecutor().getStreamsAndViewsInformation(user)) {
			response.addResponseValue(e.getName().toString());
		}
		return response;

	}

	public StringListResponse getInstalledQueries(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		StringListResponse response = new StringListResponse(true);

		ISession session = loginWithSecurityToken(securityToken);
		for (IPhysicalQuery q : ExecutorServiceBinding.getExecutor().getExecutionPlan(session).getQueries(session)) {
			if (q.getLogicalQuery() != null) {
				response.addResponseValue(q.getLogicalQuery().getQueryText());
			}
		}
		return response;

	}

	public QueryState getQueryState(int queryID, String securityToken) {
		ISession session = SessionManagement.instance.login(securityToken);
		return ExecutorServiceBinding.getExecutor().getQueryState(queryID, session);
	}

	public ArrayList<QueryState> getQueryStates(ArrayList<Integer> queryIDs, List<String> securityTokens) {
		List<ISession> sessions = new ArrayList<>();
		for (String securityToken:securityTokens){
			sessions.add(SessionManagement.instance.login(securityToken));
		}
		return new ArrayList<>(ExecutorServiceBinding.getExecutor().getQueryStates(queryIDs, sessions));
	}

	protected ISession loginWithSecurityToken(String securityToken) throws InvalidUserDataException {
		ISession session = SessionManagement.instance.login(securityToken);
		if (session != null) {
			return session;
		}
		throw new InvalidUserDataException(
				"Security token unknown! You have to login first to obtain a security token!");
	}

	public synchronized Response removeQuery(@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "queryID") int queryID) throws InvalidUserDataException, QueryNotExistsException {
		ISession user = loginWithSecurityToken(securityToken);
		try {
			ExecutorServiceBinding.getExecutor().removeQuery(queryID, user);
			socketPortMap.remove(queryID);
			socketSinkMap.remove(queryID);
		} catch (Exception e) {
			throw new QueryNotExistsException();
		}
		return new Response(true);

	}

	public Response startQuery(@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "queryID") int queryID) throws InvalidUserDataException, QueryNotExistsException {
		ISession user = loginWithSecurityToken(securityToken);
		try {
			ExecutorServiceBinding.getExecutor().startQuery(queryID, user);
		} catch (Exception e) {
			throw new QueryNotExistsException();
		}
		return new Response(true);

	}

	public Response stopQuery(@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "queryID") int queryID) throws InvalidUserDataException, QueryNotExistsException {
		ISession user = loginWithSecurityToken(securityToken);
		try {
			ExecutorServiceBinding.getExecutor().stopQuery(queryID, user);
		} catch (Exception e) {
			throw new QueryNotExistsException();
		}
		return new Response(true);

	}

	public Response suspendQuery(@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "queryID") int queryID) throws InvalidUserDataException, QueryNotExistsException {
		ISession user = loginWithSecurityToken(securityToken);
		try {
			ExecutorServiceBinding.getExecutor().suspendQuery(queryID, user);
		} catch (Exception e) {
			throw new QueryNotExistsException();
		}
		return new Response(true);

	}

	public Response resumeQuery(@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "queryID") int queryID) throws InvalidUserDataException, QueryNotExistsException {
		ISession user = loginWithSecurityToken(securityToken);
		try {
			ExecutorServiceBinding.getExecutor().resumeQuery(queryID, user);
		} catch (Exception e) {
			throw new QueryNotExistsException();
		}
		return new Response(true);

	}

	public Response startExecution(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession session = loginWithSecurityToken(securityToken);
		ExecutorServiceBinding.getExecutor().startExecution(session);
		return new Response(true);

	}

	public Response stopExecution(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession session = loginWithSecurityToken(securityToken);
		ExecutorServiceBinding.getExecutor().stopExecution(session);
		return new Response(true);

	}

	public BooleanResponse isRunning(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		loginWithSecurityToken(securityToken);
		boolean running = ExecutorServiceBinding.getExecutor().isRunning();
		return new BooleanResponse(running, true);

	}

	public StringResponse getInfos(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		loginWithSecurityToken(securityToken);
		return new StringResponse(ExecutorServiceBinding.getExecutor().getInfos(), true);

	}

	public StringListResponse getQueryBuildConfigurationNames(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		return new StringListResponse(ExecutorServiceBinding.getExecutor().getQueryBuildConfigurationNames(user), true);

	}

	public StringListResponse getSupportedQueryParsers(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		Set<String> parsers = ExecutorServiceBinding.getExecutor().getSupportedQueryParsers(user);
		return new StringListResponse(parsers, true);

	}

	public Response startAllClosedQueries(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		ExecutorServiceBinding.getExecutor().startAllClosedQueries(user);
		return new Response(true);

	}

	public StringListResponse getRegisteredBufferPlacementStrategiesIDs(
			@WebParam(name = "securitytoken") String securityToken) throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		return new StringListResponse(
				ExecutorServiceBinding.getExecutor().getRegisteredBufferPlacementStrategiesIDs(user), true);
	}

	public StringListResponse getRegisteredSchedulingStrategies(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		return new StringListResponse(ExecutorServiceBinding.getExecutor().getRegisteredSchedulingStrategies(user),
				true);

	}

	public StringListResponse getRegisteredSchedulers(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		return new StringListResponse(ExecutorServiceBinding.getExecutor().getRegisteredSchedulers(user), true);
	}

	public Response setScheduler(@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "scheduler") String scheduler,
			@WebParam(name = "scheduler_strategy") String schedulerStrategy) throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		ExecutorServiceBinding.getExecutor().setScheduler(scheduler, schedulerStrategy, user);
		return new Response(true);

	}

	public StringResponse getCurrentSchedulerID(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		return new StringResponse(ExecutorServiceBinding.getExecutor().getCurrentSchedulerID(user), true);

	}

	public StringResponse getCurrentSchedulingStrategyID(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		return new StringResponse(ExecutorServiceBinding.getExecutor().getCurrentSchedulingStrategyID(user), true);

	}

	public StringResponse getName() {
		return new StringResponse(ExecutorServiceBinding.getExecutor().getName(), true);

	}

	public SimpleGraph getLogicalQueryPlan(@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "queryID") Integer queryID) throws InvalidUserDataException, QueryNotExistsException {
		ISession session = loginWithSecurityToken(securityToken);
		IServerExecutor executer = ExecutorServiceBinding.getExecutor();
		ILogicalQuery logicalQuery = executer.getLogicalQueryById(queryID, session);
		if (logicalQuery == null) {
			throw new QueryNotExistsException("Could not find query with id "+queryID);
		}
		ILogicalOperator operator = logicalQuery.getLogicalPlan().getRoot();
		Map<Integer, GraphNode> visitedOperators = new HashMap<Integer, GraphNode>();
		SimpleGraph graph = new SimpleGraph();
		for (LogicalSubscription subscription : operator.getSubscribedToSource()) {
			graph.addRootNode(this.createGraphNode(subscription, visitedOperators));
		}
		return graph;
	}

	private GraphNode createGraphNode(LogicalSubscription subscription, Map<Integer, GraphNode> visitedOperators) {
		ILogicalOperator operator = subscription.getSource();
		GraphNode newNode = new GraphNode();
		newNode.setName(operator.getName());
		newNode.setParameterInfos(operator.getParameterInfos());
		newNode.setOutputSchema(this.createSDFSchemaInformation(operator.getOutputSchema()).getResponseValue());
		newNode.setClassName(operator.getClass().getSimpleName());
		newNode.setHash(operator.hashCode());
		newNode.setSource(operator.isSourceOperator());
		newNode.setPipe(operator.isPipeOperator());
		newNode.setSink(operator.isSinkOperator());

		if (operator instanceof StreamAO) {
			newNode.setSource(true);
			newNode.setPipe(false);
			newNode.setSink(false);
		}

		visitedOperators.put(operator.hashCode(), newNode);
		for (LogicalSubscription subs : operator.getSubscribedToSource()) {
			ILogicalOperator op = subs.getSource();
			GraphNode node = visitedOperators.get(op.hashCode());
			if (node == null) {
				newNode.addChild(this.createGraphNode(subs, visitedOperators), subs.getSourceOutPort());
			} else {
				newNode.addChild(node, subs.getSinkInPort());
			}
		}
		return newNode;
	}

	@SuppressWarnings("unchecked")
	public SimpleGraph getPlan(@WebParam(name = "securitytoken") String securityToken) throws InvalidUserDataException {
		SimpleGraph graph = new SimpleGraph();
		ISession session = loginWithSecurityToken(securityToken);
		IExecutionPlan plan = ExecutorServiceBinding.getExecutor().getExecutionPlan(session);
		int idCounter = 0;
		for (IPhysicalOperator op : plan.getRoots(session)) {
			GraphNodeVisitor<IPhysicalOperator> visitor = new GraphNodeVisitor<IPhysicalOperator>();
			visitor.setIdCounter(idCounter);
			@SuppressWarnings("rawtypes")
			GenericGraphWalker walker = new GenericGraphWalker();
			walker.prefixWalkPhysical(op, visitor);
			graph.addRootNode(visitor.getResult());
			idCounter = visitor.getIdCounter();
		}
		return graph;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SimpleGraph getPlanByQueryID(@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "queryID") Integer queryID) throws InvalidUserDataException, QueryNotExistsException {
		ISession session = loginWithSecurityToken(securityToken);
		SimpleGraph graph = new SimpleGraph();
		IServerExecutor executer = ExecutorServiceBinding.getExecutor();
		int idCounter = 0;
		List<IPhysicalOperator> roots = null;
		try {
			roots = executer.getPhysicalRoots(queryID, session);
		} catch (Exception e) {
			e.printStackTrace();
			throw new QueryNotExistsException();
		}
		for (IPhysicalOperator op : roots) {
			GraphNodeVisitor<IPhysicalOperator> visitor = new GraphNodeVisitor<IPhysicalOperator>();
			visitor.setIdCounter(idCounter);
			GenericGraphWalker walker = new GenericGraphWalker();
			walker.prefixWalkPhysical(op, visitor);
			graph.addRootNode(visitor.getResult());
			idCounter = visitor.getIdCounter();
		}
		return graph;
	}

	public QueryResponse getLogicalQuery(@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "id") String id) throws InvalidUserDataException, QueryNotExistsException {
		return getLogicalQueryById(securityToken, id);
	}

	public QueryResponse getLogicalQueryById(@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "id") String id) throws InvalidUserDataException, QueryNotExistsException {
		ISession user = loginWithSecurityToken(securityToken);

		IPhysicalQuery queryById = ExecutorServiceBinding.getExecutor().getExecutionPlan(user)
				.getQueryById(Integer.valueOf(id), user);
		LogicalQuery logicalQuery = (LogicalQuery) ExecutorServiceBinding.getExecutor()
				.getLogicalQueryById(Integer.valueOf(id), user);
		if (queryById == null || logicalQuery == null) {
			throw new QueryNotExistsException("Could not find query with id "+id);
		}
		List<String> roots = new ArrayList<String>();
		for (IPhysicalOperator operator : queryById.getRoots()) {
			roots.add(operator.getName());
		}
		return new QueryResponse(logicalQuery, queryById.getSession().getUser().getName(), queryById.getState(), roots,
				true);
	}

	public QueryResponse getLogicalQueryByName(@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "name") String name) throws InvalidUserDataException {
		ISession session = loginWithSecurityToken(securityToken);
		IPhysicalQuery queryById = ExecutorServiceBinding.getExecutor().getExecutionPlan(session).getQueryByName(Resource.specialCreateResource(name, session.getUser()), session);
		List<String> roots = new ArrayList<String>();
		for (IPhysicalOperator operator : queryById.getRoots()) {
			roots.add(operator.getName());
		}
		return new QueryResponse(
				(LogicalQuery) ExecutorServiceBinding.getExecutor().getLogicalQueryByName(Resource.specialCreateResource(name, session.getUser()), session),
				queryById.getSession().getUser().getName(), queryById.getState(), roots, true);

	}

	public IntegerCollectionResponse getLogicalQueryIds(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession session = loginWithSecurityToken(securityToken);
		return new IntegerCollectionResponse(ExecutorServiceBinding.getExecutor().getLogicalQueryIds(session), true);
	}

	public ConnectionInformationResponse getConnectionInformationWithSSL(
			@WebParam(name = "securitytoken") String securityToken, @WebParam(name = "queryId") int queryId,
			@WebParam(name = "sslClientAuthentication") boolean sslClientAuthentication,
			@WebParam(name = "nullValues") boolean nullValues) throws InvalidUserDataException {

		return getConnectionInformationWithPorts(securityToken, queryId, 0,
				Integer.valueOf(OdysseusConfiguration.instance.getInt("minSinkPort", SINK_MIN_PORT)),
				Integer.valueOf(OdysseusConfiguration.instance.getInt("maxSinkPort", SINK_MAX_PORT)), nullValues, true,
				sslClientAuthentication, true);
	}

	public ConnectionInformationResponse getConnectionInformation(
			@WebParam(name = "securitytoken") String securityToken, @WebParam(name = "queryId") int queryId,
			@WebParam(name = "nullValues") boolean nullValues) throws InvalidUserDataException {

		return getConnectionInformationWithPorts(securityToken, queryId, 0,
				Integer.valueOf(OdysseusConfiguration.instance.getInt("minSinkPort", SINK_MIN_PORT)),
				Integer.valueOf(OdysseusConfiguration.instance.getInt("maxSinkPort", SINK_MAX_PORT)), nullValues);
	}

	public ConnectionInformationResponse getConnectionInformationWithMetadata(
			@WebParam(name = "securitytoken") String securityToken, @WebParam(name = "queryId") int queryId,
			@WebParam(name = "nullValues") boolean nullValues, @WebParam(name = "withMetadata") boolean withMetadata)
					throws InvalidUserDataException {

		return getConnectionInformationWithPorts(securityToken, queryId, 0,
				Integer.valueOf(OdysseusConfiguration.instance.getInt("minSinkPort", SINK_MIN_PORT)),
				Integer.valueOf(OdysseusConfiguration.instance.getInt("maxSinkPort", SINK_MAX_PORT)), nullValues, false, false,
				withMetadata);
	}

	public ConnectionInformationResponse getConnectionInformationWithPorts(
			@WebParam(name = "securitytoken") String securityToken, @WebParam(name = "queryId") int queryId,
			@WebParam(name = "rootPort") int rootPort, @WebParam(name = "minPort") int minPort,
			@WebParam(name = "maxPort") int maxPort, @WebParam(name = "nullValues") boolean nullValues)
					throws InvalidUserDataException {
		return this.getConnectionInformationWithPorts(securityToken, queryId, rootPort, minPort, maxPort, nullValues,
				false, false, true);
	}

	private synchronized ConnectionInformationResponse getConnectionInformationWithPorts(String securityToken,
			int queryId, int rootPort, int minPort, int maxPort, boolean nullValues, boolean ssl,
			boolean sslClientAuthentication, boolean withMetadata) throws InvalidUserDataException {
		try {
			ISession session = loginWithSecurityToken(securityToken);
			int port = 0;
			SocketSinkPO po;
			Map<Integer, Integer> socketPortMapEntry = socketPortMap.get(queryId);
			Map<Integer, SocketSinkPO> socketSinkMapEntry = socketSinkMap.get(queryId);

			if (socketSinkMapEntry != null) {
				if (socketSinkMapEntry.get(rootPort) == null) {
					port = getNextFreePort(minPort, maxPort);
					po = addSocketSink(queryId, rootPort, port, nullValues, ssl, sslClientAuthentication, withMetadata, session);
					socketSinkMapEntry.put(rootPort, po);
					socketPortMapEntry.put(rootPort, port);
				} else {
					port = socketPortMapEntry.get(rootPort);
					po = socketSinkMapEntry.get(rootPort);
				}
			} else {
				socketSinkMapEntry = new HashMap<>();
				socketPortMapEntry = new HashMap<>();
				socketPortMap.put(queryId, socketPortMapEntry);
				socketSinkMap.put(queryId, socketSinkMapEntry);
				port = getNextFreePort(minPort, maxPort);
				po = addSocketSink(queryId, rootPort, port, nullValues, ssl, sslClientAuthentication, withMetadata, session);
				socketSinkMapEntry.put(rootPort, po);
				socketPortMapEntry.put(rootPort, port);
			}
			po.addAllowedSessionId(securityToken);
			if (this.address == null) {
				List<InetAddress> addresses = new LinkedList<InetAddress>();
				addresses.add(InetAddress.getLocalHost());
				Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
				while (interfaces.hasMoreElements()) {
					NetworkInterface ni = interfaces.nextElement();
					if (ni.isVirtual()) {
						continue;
					}
					if (ni.isLoopback()) {
						continue;
					}
					Enumeration<InetAddress> iter = ni.getInetAddresses();
					while (iter.hasMoreElements()) {
						addresses.add(iter.nextElement());
					}
				}
				address = new LinkedList<String>();
				for (InetAddress i : addresses) {
					if (!address.contains(i.getHostAddress())) {
						address.add(i.getHostAddress());
					}
				}
			}
			ConnectionInformation connectInfo = new ConnectionInformation(port, address);
			return new ConnectionInformationResponse(connectInfo, true);
		} catch (SocketException e) {
			e.printStackTrace();
			return new ConnectionInformationResponse(null, false);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return new ConnectionInformationResponse(null, false);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private SocketSinkPO addSocketSink(int queryId, int rootPort, int port, boolean nullValues, boolean ssl,
			boolean sslClientAuthentication, boolean withMetadata, ISession session) {
		IExecutionPlan plan = ExecutorServiceBinding.getExecutor().getExecutionPlan(session);
		IPhysicalQuery query = plan.getQueryById(queryId, session);
		List<IPhysicalOperator> roots = query.getRoots();
		final IPhysicalOperator root = roots.get(rootPort);
		final ISource<?> rootAsSource;

		if (root.isSource()) {
			rootAsSource = (ISource<?>) root;
		} else {
			rootAsSource = ((ISink<?>) root).getSubscribedToSource(0).getSource();
		}

		IStreamObjectDataHandler<?> handler = null;

		// TODO: Find a way to handle null values again
		if (nullValues){
			logger.warn("Do not use param nullValues. Use correct datahandler (e.g. NullAwareTuple) instead.");
		}
		Class<? extends IStreamObject> typeClass = root.getOutputSchema().getType();

		handler = DataHandlerRegistry.instance.getStreamObjectDataHandler(typeClass.getSimpleName(), root.getOutputSchema());


//		if (nullValues) {
//			handler = new NullAwareTupleDataHandler(root.getOutputSchema());
//		} else {
//			handler = (IStreamObjectDataHandler<?>) new TupleDataHandler().createInstance(root.getOutputSchema());
//		}

		IMetaAttribute metaAttribute = MetadataRegistry.getMetadataType(root.getOutputSchema().getMetaAttributeNames());
		handler.setMetaAttribute(metaAttribute);

		ByteBufferHandler<Tuple<ITimeInterval>> objectHandler = new ByteBufferHandler<Tuple<ITimeInterval>>(handler);

		SocketSinkPO sink = null;

		if (ssl) {
			sink = new SocketSinkPO(new SSLServerSocketProvider(port, sslClientAuthentication),
					new ByteBufferSinkStreamHandlerBuilder(), false, false, objectHandler, withMetadata);
		} else {
			sink = new SocketSinkPO(port, "", new NioByteBufferSinkStreamHandlerBuilder(), true, false, false,
					objectHandler, false, withMetadata);
		}

		rootAsSource.subscribeSink((ISink) sink, 0, 0, root.getOutputSchema(), true, 0);
		sink.addOwner(query);
		// rootAsSource.connectSink((ISink) sink, 0, 0, root.getOutputSchema());
		// sink.setInputPortCount(1);
		sink.startListening();
		return sink;
	}

	private int getNextFreePort(int min, int max) {
		int port = min;
		while (!available(port)) {
			// port = min + (int) (Math.random() * ((max - min) + 1));
			port++;
		}
		return port;
	}

	/**
	 * Checks to see if a specific port is available.
	 *
	 * @param port
	 *            the port to check for availability
	 */
	public static boolean available(int port) {

		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		} catch (IOException e) {
		} finally {
			if (ds != null) {
				ds.close();
			}

			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					/* should not be thrown */
				}
			}
		}

		return false;
	}

	private SDFSchemaResponse createSDFSchemaInformation(SDFSchema schema) {
		SDFSchemaInformation info = SDFSchemaInformation.createSchemaInformation(schema);
		return new SDFSchemaResponse(info, true);
	}

	public SDFSchemaResponse getOutputSchemaByQueryId(@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "queryId") int queryId) throws InvalidUserDataException, QueryNotExistsException {
		ISession session = loginWithSecurityToken(securityToken);
		SDFSchema schema;
		try {
			schema = ExecutorServiceBinding.getExecutor().getOutputSchema(queryId, session);
		} catch (Exception e) {
			throw new QueryNotExistsException("Could not find query with id "+queryId, e);
		}
		return createSDFSchemaInformation(schema);

	}

	public SDFSchemaResponse getOutputSchemaByQueryIdAndPort(@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "queryId") int queryId, @WebParam(name = "port") int port)
					throws InvalidUserDataException, QueryNotExistsException {
		ISession session = loginWithSecurityToken(securityToken);
		SDFSchema schema;
		try {
			IPhysicalOperator operator = ExecutorServiceBinding.getExecutor().getExecutionPlan(session).getQueryById(queryId, session)
					.getRoots().get(port);
			schema = operator.getOutputSchema();
		} catch (Exception e) {
			throw new QueryNotExistsException("Could not find query with id "+queryId, e);
		}
		return createSDFSchemaInformation(schema);

	}

	public SDFSchemaResponse getOutputSchemaBySource(@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "sourcename") String sourcename) throws InvalidUserDataException {

		ISession session = loginWithSecurityToken(securityToken);
		SDFSchema schema = ExecutorServiceBinding.getExecutor().getDataDictionary(session).getSources().get(sourcename)
				.getOutputSchema();
		return createSDFSchemaInformation(schema);

	}

	public SourceListResponse getSources(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		Set<Entry<Resource, ILogicalPlan>> sources = ExecutorServiceBinding.getExecutor().getDataDictionary(user)
				.getStreamsAndViews(user);
		List<SourceInformation> sourceInfos = new ArrayList<SourceInformation>();
		for (Entry<Resource, ILogicalPlan> source : sources) {
			SDFSchema schema = source.getValue().getOutputSchema();
			SDFSchemaInformation schemaInfo = SDFSchemaInformation.createSchemaInformation(schema);
			// FIXME: Use Resource
			sourceInfos.add(
					new SourceInformation(schemaInfo, source.getKey().toString(), source.getValue().getRoot().getOwnerIDs()));
		}
		return new SourceListResponse(sourceInfos, true);
	}

	public OperatorBuilderListResponse getOperatorBuilderList(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		loginWithSecurityToken(securityToken);
		List<IOperatorBuilder> opBuilders = getOperatorBuilderFactory().getOperatorBuilder();
		List<OperatorBuilderInformation> opInfo = extractOperatorBuilderInformation(opBuilders);
		return new OperatorBuilderListResponse(opInfo, true);

	}

	private List<OperatorBuilderInformation> extractOperatorBuilderInformation(List<IOperatorBuilder> builders) {
		List<OperatorBuilderInformation> infos = new ArrayList<OperatorBuilderInformation>();
		for (IOperatorBuilder builder : builders) {
			OperatorBuilderInformation info = new OperatorBuilderInformation();
			info.setName(builder.getName());
			info.setMinInputOperatorCount(builder.getMinInputOperatorCount());
			info.setMaxInputOperatorCount(builder.getMaxInputOperatorCount());
			info.setDoc(builder.getDoc());
			info.setParameters(extractParameterInformation(builder.getParameters()));
			info.setCategories(builder.getCategories());
			infos.add(info);
		}
		return infos;
	}

	private Set<ParameterInfo> extractParameterInformation(Set<IParameter<?>> parameters) {
		Set<ParameterInfo> infos = new HashSet<ParameterInfo>();
		for (IParameter<?> parameter : parameters) {
			infos.add(setParameterInfo(parameter));
		}
		return infos;
	}

	private ParameterInfo setParameterInfo(IParameter<?> parameter) {
		ParameterInfo info = new ParameterInfo();
		info.setName(parameter.getName());
		info.setRequirement(ParameterInfo.REQUIREMENT.valueOf(parameter.getRequirement().name()));
		info.setDeprecated(parameter.isDeprecated());
		info.setMandatory(parameter.isMandatory());
		info.setPossibleValueMethod(parameter.getPossibleValueMethod());
		info.setDoc(parameter.getDoc());
		String dataType = parameter.getClass().getSimpleName();
		info.setDataType(dataType);
		if (parameter instanceof ListParameter<?>) {
			ListParameter<?> listParameter = (ListParameter<?>) parameter;
			info.setListDataType(setParameterInfo(listParameter.getSingleParameter()));
		} else if (parameter instanceof MapParameter<?, ?>) {
			MapParameter<?, ?> mapParameter = (MapParameter<?, ?>) parameter;
			info.setMapKeyDataType(setParameterInfo(mapParameter.getKeyParameter()));
			info.setMapValueDataType(setParameterInfo(mapParameter.getValueParameter()));
		}
		return info;
	}

	public StringMapStringListResponse getQueryParserTokens(@WebParam(name = "queryParser") String queryParser,
			@WebParam(name = "securitytoken") String securityToken) throws InvalidUserDataException {
		ISession caller = loginWithSecurityToken(securityToken);
		Map<String, List<String>> result = getExecutor().getQueryParserTokens(queryParser, caller);
		List<StringMapListEntry> res = new ArrayList<>();
		for (Entry<String, List<String>> e : result.entrySet()) {
			res.add(new StringMapListEntry(e.getKey(), e.getValue()));
		}
		return new StringMapStringListResponse(res, true);
	}

	public StringListResponse getQueryParserSuggestions(@WebParam(name = "queryParser") String queryParser,
			@WebParam(name = "hint") String hint, @WebParam(name = "securitytoken") String securityToken)
					throws InvalidUserDataException {
		ISession caller = loginWithSecurityToken(securityToken);
		List<String> resp = getExecutor().getQueryParserSuggestions(queryParser, hint, caller);
		return new StringListResponse(resp, true);
	}

	public SDFSchemaResponse determineOutputSchema(@WebParam(name = "query") String query,
			@WebParam(name = "parserID") String parserID, @WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "port") int port, @WebParam(name = "context") Context context)
					throws InvalidUserDataException, DetermineOutputSchemaException {
		ISession caller = loginWithSecurityToken(securityToken);
		try {
			SDFSchema schema = getExecutor().determineOutputSchema(query, parserID, caller, port, context);
			return createSDFSchemaInformation(schema);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new DetermineOutputSchemaException(e.toString());
		}
	}

	public SDFDatatypeListResponse getRegisteredDatatypes(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		Set<SDFDatatype> dts = getExecutor().getRegisteredDatatypes(user);
		SDFDatatypeListResponse resp = new SDFDatatypeListResponse(true);
		for (SDFDatatype dt : dts) {
			resp.addResonseValue(SDFDatatypeInformation.createDatatypeInformation(dt));
		}
		return resp;
	}

	public StringListResponse getRegisteredWrapperNames(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		Set<String> names = getExecutor().getRegisteredWrapperNames(user);
		return new StringListResponse(names, true);
	}

	public StringListResponse getRegisteredAggregateFunctions(
			@WebParam(name = "datamodel") @SuppressWarnings("rawtypes") Class<? extends IStreamObject> datamodel,
			@WebParam(name = "securitytoken") String securityToken) throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		Set<String> names = getExecutor().getRegisteredAggregateFunctions(datamodel.getName(), user);
		return new StringListResponse(names, true);
	}

	public LogicalOperatorResponse removeSinkByName(@WebParam(name = "name") String name,
			@WebParam(name = "securitytoken") String securityToken) throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		ILogicalOperator logicalOperator = null;
		try {
			logicalOperator = getExecutor().removeSink(name, user).getRoot();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new LogicalOperatorResponse(logicalOperator, true);
	}

	public LogicalOperatorResponse removeSinkByResource(@WebParam(name = "ri") ResourceInformation ri,
			@WebParam(name = "securitytoken") String securityToken) throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		ILogicalOperator logicalOperator = null;
		try {
			logicalOperator = getExecutor().removeSink(new Resource(ri.getUser(), ri.getResourceName()), user).getRoot();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new LogicalOperatorResponse(logicalOperator, true);
	}

	public Response removeViewOrStreamByName(@WebParam(name = "name") String name,
			@WebParam(name = "securitytoken") String securityToken) throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		getExecutor().removeViewOrStream(name, user);
		return new Response(true);
	}

	public Response removeViewOrStreamByResource(@WebParam(name = "ri") ResourceInformation ri,
			@WebParam(name = "securitytoken") String securityToken) throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		getExecutor().removeViewOrStream(new Resource(ri.getUser(), ri.getResourceName()), user);
		return new Response(true);

	}

	public ArrayList<ViewInformationWS> getStreamsAndViews(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		List<ViewInformation> result = getExecutor().getStreamsAndViewsInformation(user);
		ArrayList<ViewInformationWS> resp = new ArrayList<>();
		for (ViewInformation entry : result) {
			ViewInformationWS vi = new ViewInformationWS();
			vi.setName(new ResourceInformation(entry.getName()));
			vi.setSchema(SDFSchemaInformation.createSchemaInformation(entry.getOutputSchema()));
			resp.add(vi);
		}
		return resp;
	}

	public ArrayList<SinkInformationWS> getSinks(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		List<SinkInformation> result = getExecutor().getSinks(user);
		ArrayList<SinkInformationWS> resp = new ArrayList<>();
		for (SinkInformation entry : result) {
			SinkInformationWS vi = new SinkInformationWS();
			vi.setName(new ResourceInformation(entry.getName()));
			vi.setSchema(SDFSchemaInformation.createSchemaInformation(entry.getOutputSchema()));
			resp.add(vi);
		}
		return resp;
	}

	public BooleanResponse containsViewOrStreamByResource(@WebParam(name = "ri") ResourceInformation ri,
			@WebParam(name = "securitytoken") String securityToken) throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		boolean result = getExecutor().containsViewOrStream(new Resource(ri.getUser(), ri.getResourceName()), user);
		return new BooleanResponse(result, true);
	}

	public BooleanResponse containsViewOrStreamByName(@WebParam(name = "name") String name,
			@WebParam(name = "securitytoken") String securityToken) throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		boolean result = getExecutor().containsViewOrStream(name, user);
		return new BooleanResponse(result, true);
	}

	public Response reloadStoredQueries(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		getExecutor().reloadStoredQueries(user);
		return new Response(true);
	}

	public Response addStoredProcedure(@WebParam(name = "name") String name,
			@WebParam(name = "storedprocedure") StoredProcedure proc,
			@WebParam(name = "securitytoken") String securityToken) throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		getExecutor().addStoredProcedure(name, proc, user);
		return new Response(true);

	}

	public Response removeStoredProcedure(@WebParam(name = "name") String name,
			@WebParam(name = "securitytoken") String securityToken) throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		getExecutor().removeStoredProcedure(name, user);
		return new Response(true);

	}

	public StoredProcedureResponse getStoredProcedure(@WebParam(name = "name") String name,
			@WebParam(name = "securitytoken") String securityToken) throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		StoredProcedure result = getExecutor().getStoredProcedure(name, user);
		return new StoredProcedureResponse(result, true);
	}

	public StoredProcedureListResponse getStoredProcedures(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		List<StoredProcedure> result = getExecutor().getStoredProcedures(user);
		return new StoredProcedureListResponse(result, true);
	}

	public BooleanResponse containsStoredProcedures(@WebParam(name = "name") String name,
			@WebParam(name = "securitytoken") String securityToken) throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		boolean result = getExecutor().containsStoredProcedures(name, user);
		return new BooleanResponse(result, true);
	}

	public StringListResponse getOperatorNames(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		List<String> result = getExecutor().getOperatorNames(user);
		return new StringListResponse(result, true);
	}

	public LogicalOperatorInformationListResponse getOperatorInformations(
			@WebParam(name = "securitytoken") String securityToken) throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		List<LogicalOperatorInformation> result = getExecutor().getOperatorInformations(user);
		return new LogicalOperatorInformationListResponse(result, true);
	}

	public LogicalOperatorInformationResponse getOperatorInformation(@WebParam(name = "name") String name,
			@WebParam(name = "securitytoken") String securityToken) throws InvalidUserDataException {
		ISession user = loginWithSecurityToken(securityToken);
		LogicalOperatorInformation result = getExecutor().getOperatorInformation(name, user);
		return new LogicalOperatorInformationResponse(result, true);
	}

	public StringListResponse getProtocolValues(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		loginWithSecurityToken(securityToken);
		ImmutableList<String> names = ProtocolHandlerRegistry.instance.getHandlerNames();
		return new StringListResponse(names, true);
	}

	public StringListResponse getDataHandlerValues(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		loginWithSecurityToken(securityToken);
		ImmutableList<String> names = DataHandlerRegistry.instance.getHandlerNames();
		return new StringListResponse(names, true);
	}

	public StringListResponse getTransportValues(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		loginWithSecurityToken(securityToken);
		List<String> names = TransportHandlerRegistry.instance.getHandlerNames();
		return new StringListResponse(names, true);
	}

	public StringListResponse getWindowTypes(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		loginWithSecurityToken(securityToken);
		List<String> names = WindowType.getValues();
		return new StringListResponse(names, true);
	}

	public StringListResponse getMetadataNames(@WebParam(name = "securitytoken") String securityToken)
			throws InvalidUserDataException {
		loginWithSecurityToken(securityToken);
		Set<String> names = MetadataRegistry.getNames();
		return new StringListResponse(names, true);
	}


}
