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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.ws.Endpoint;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.ByteBufferSinkStreamHandlerBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.ISinkStreamHandlerBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SocketSinkPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.OperatorBuilderFactoryServiceBinding;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.exception.WebserviceException;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.BooleanResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.ConnectionInformation;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.ConnectionInformationResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.GraphNode;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.IntegerCollectionResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.OperatorBuilderInformation;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.OperatorBuilderListResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.ParameterInfo;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.QueryResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.Response;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.SDFAttributeInformation;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.SDFDatatypeInformation;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.SDFSchemaInformation;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.SDFSchemaResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.SimpleGraph;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.SourceInformation;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.SourceListResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.StringListResponse;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response.StringResponse;

/**
 * 
 * @author Dennis Geesen Created at: 09.08.2011
 */

@WebService
@SOAPBinding(style = Style.DOCUMENT)
@XmlSeeAlso({ SimpleGraph.class, String[].class, GraphNode.class,
		LogicalQuery.class, ConnectionInformation.class })
public class WebserviceServer {

	private static final int SINK_MIN_PORT = 10000;
	private static final int SINK_MAX_PORT = 20000;

	// Session management needed for logout.
	// identified by securitytoken
	@XmlTransient
	private Map<String, ISession> sessions = new HashMap<String, ISession>();

	@XmlTransient
	private Map<Integer, Integer> socketPortMap = new HashMap<Integer, Integer>();

	/**
	 * Socket Management
	 */
	// Map<queryID, Port>
	protected Map<Integer, Integer> socketMap;
	// Map<queryID, SocketSinkPO>
	protected Map<Integer, ILogicalOperator> socketSinkMap;

	public static void startServer() {
		WebserviceServer server = new WebserviceServer();
		Endpoint endpoint = Endpoint.publish("http://127.0.0.1:9669/odysseus",
				server);
		if (endpoint.isPublished()) {
			Logger.getAnonymousLogger()
					.log(Level.FINE, "Webservice published!");
		}
	}

	protected IExecutor getExecutor() {
		return ExecutorServiceBinding.getExecutor();
	}

	protected IOperatorBuilderFactory getOperatorBuilderFactory() {
		return OperatorBuilderFactoryServiceBinding.getOperatorBuilderFactory();
	}

	@WebResult(name = "securitytoken")
	public StringResponse login(@WebParam(name = "username") String username,
			@WebParam(name = "password") String password) {
		ISession user = UserManagement.getSessionmanagement().login(username,
				password.getBytes());
		if (user != null) {
			String token = user.getToken();
			StringResponse response = new StringResponse(token, true);
			// session-management...
			sessions.put(token, user);
			return response;
		}
		return new StringResponse(null, false);
	}

	public Response logout(
			@WebParam(name = "securitytoken") String securityToken) {
		ISession user = sessions.get(securityToken);
		if (user != null) {
			UserManagement.getSessionmanagement().logout(user);
			sessions.remove(securityToken);
			return new Response(true);
		}
		return new Response(false);
	}

	public IntegerCollectionResponse addQuery(
			@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "parser") String parser,
			@WebParam(name = "query") String query,
			@WebParam(name = "transformationconfig") String transCfg) {
		try {
			ISession user = loginWithSecurityToken(securityToken);
			IntegerCollectionResponse response = new IntegerCollectionResponse(
					ExecutorServiceBinding.getExecutor().addQuery(query,
							parser, user, transCfg), true);
			return response;
		} catch (WebserviceException e) {
			e.printStackTrace();
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
		return new IntegerCollectionResponse(null, false);
	}

	public StringListResponse getInstalledSources(
			@WebParam(name = "securitytoken") String securityToken) {
		StringListResponse response = new StringListResponse(true);
		try {
			ISession user = loginWithSecurityToken(securityToken);
			for (Entry<String, ILogicalOperator> e : getExecutor()
					.getStreamsAndViews(user)) {
				response.addResponseValue(e.getKey());
			}
			return response;
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new StringListResponse(false);
		}

	}

	public StringListResponse getInstalledQueries(
			@WebParam(name = "securitytoken") String securityToken) {
		StringListResponse response = new StringListResponse(true);
		try {
			loginWithSecurityToken(securityToken);
			for (IPhysicalQuery q : ExecutorServiceBinding.getExecutor()
					.getExecutionPlan().getQueries()) {
				if (q.getLogicalQuery() != null) {
					response.addResponseValue(q.getLogicalQuery()
							.getQueryText());
				}
			}
			return response;
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new StringListResponse(false);
		}
	}

	protected ISession loginWithSecurityToken(String securityToken)
			throws WebserviceException {
		ISession session = UserManagement.getSessionmanagement().login(
				securityToken);
		if (session != null) {
			return session;
		}
		throw new WebserviceException(
				"Security token unknown! You have to login first to obtain a security token!");
	}

	public Response removeQuery(
			@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "queryID") int queryID) {
		try {
			ISession user = loginWithSecurityToken(securityToken);
			ExecutorServiceBinding.getExecutor().removeQuery(queryID, user);
			return new Response(true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new Response(false);
		}
	}

	public Response startQuery(
			@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "queryID") int queryID) {
		try {
			ISession user = loginWithSecurityToken(securityToken);
			ExecutorServiceBinding.getExecutor().startQuery(queryID, user);
			return new Response(true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new Response(false);
		}

	}

	public Response stopQuery(
			@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "queryID") int queryID) {
		try {
			ISession user = loginWithSecurityToken(securityToken);
			ExecutorServiceBinding.getExecutor().stopQuery(queryID, user);
			return new Response(true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new Response(false);
		}

	}

	public Response startExecution(
			@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			ExecutorServiceBinding.getExecutor().startExecution();
			return new Response(true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new Response(false);
		}

	}

	public Response stopExecution(
			@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			ExecutorServiceBinding.getExecutor().stopExecution();
			return new Response(true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new Response(false);
		}
	}

	public BooleanResponse isRunning(
			@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			boolean running = ExecutorServiceBinding.getExecutor().isRunning();
			return new BooleanResponse(running, true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new BooleanResponse(false, false);
		}
	}

	public StringResponse getInfos(
			@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new StringResponse(ExecutorServiceBinding.getExecutor()
					.getInfos(), true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new StringResponse("", false);
		}
	}

	public StringListResponse getQueryBuildConfigurationNames(
			@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new StringListResponse(ExecutorServiceBinding.getExecutor()
					.getQueryBuildConfigurationNames(), true);
		} catch (WebserviceException e) {
			e.printStackTrace();
		}
		return new StringListResponse(false);
	}

	public StringListResponse getSupportedQueryParsers(
			@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			Set<String> parsers = ExecutorServiceBinding.getExecutor()
					.getSupportedQueryParsers();
			return new StringListResponse(parsers, true);
		} catch (WebserviceException e) {
			e.printStackTrace();
		}
		return new StringListResponse(false);
	}

	public Response startAllClosedQueries(
			@WebParam(name = "securitytoken") String securityToken) {
		try {
			ISession user = loginWithSecurityToken(securityToken);
			ExecutorServiceBinding.getExecutor().startAllClosedQueries(user);
			return new Response(true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new Response(false);
		}
	}

	public StringListResponse getRegisteredBufferPlacementStrategiesIDs(
			@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new StringListResponse(ExecutorServiceBinding.getExecutor()
					.getRegisteredBufferPlacementStrategiesIDs(), true);
		} catch (WebserviceException e) {
			e.printStackTrace();
		}
		return new StringListResponse(false);
	}

	public StringListResponse getRegisteredSchedulingStrategies(
			@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new StringListResponse(ExecutorServiceBinding.getExecutor()
					.getRegisteredSchedulingStrategies(), true);
		} catch (WebserviceException e) {
			e.printStackTrace();
		}
		return new StringListResponse(false);
	}

	public StringListResponse getRegisteredSchedulers(
			@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new StringListResponse(ExecutorServiceBinding.getExecutor()
					.getRegisteredSchedulers(), true);
		} catch (WebserviceException e) {
			e.printStackTrace();
		}
		return new StringListResponse(false);
	}

	public Response setScheduler(
			@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "scheduler") String scheduler,
			@WebParam(name = "scheduler_strategy") String schedulerStrategy) {
		try {
			loginWithSecurityToken(securityToken);
			ExecutorServiceBinding.getExecutor().setScheduler(scheduler,
					schedulerStrategy);
			return new Response(true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new Response(false);
		}
	}

	public StringResponse getCurrentSchedulerID(
			@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new StringResponse(ExecutorServiceBinding.getExecutor()
					.getCurrentSchedulerID(), true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new StringResponse("", false);
		}
	}

	public StringResponse getCurrentSchedulingStrategyID(
			@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new StringResponse(ExecutorServiceBinding.getExecutor()
					.getCurrentSchedulingStrategyID(), true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new StringResponse("", false);
		}
	}

	public StringResponse getName(
			@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new StringResponse(ExecutorServiceBinding.getExecutor()
					.getName(), true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new StringResponse("", false);
		}
	}

	@SuppressWarnings("unchecked")
	public SimpleGraph getPlan(@WebParam(name = "securitytoken") String securityToken) {
		try {
			SimpleGraph graph = new SimpleGraph();
			// loginWithSecurityToken(securityToken);
			IExecutionPlan plan = ExecutorServiceBinding.getExecutor().getExecutionPlan();
			int idCounter = 0;
			for (IPhysicalOperator op : plan.getRoots()) {
				GraphNodeVisitor<IPhysicalOperator> visitor = new GraphNodeVisitor<IPhysicalOperator>();
				visitor.setIdCounter(idCounter);
				@SuppressWarnings("rawtypes")
				GenericGraphWalker walker = new GenericGraphWalker();
				walker.prefixWalkPhysical(op, visitor);
				graph.addRootNode(visitor.getResult());
				idCounter = visitor.getIdCounter();
			}
			return graph;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return graph;
	}

	public QueryResponse getLogicalQuery(
			@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "id") String id) {
		return getLogicalQueryById(securityToken, id);
	}

	public QueryResponse getLogicalQueryById(
			@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "id") String id) {
		try {
			loginWithSecurityToken(securityToken);
			return new QueryResponse((LogicalQuery) ExecutorServiceBinding
					.getExecutor().getLogicalQueryById(Integer.valueOf(id)),
					true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new QueryResponse(null, false);
		}
	}

	public QueryResponse getLogicalQueryByName(
			@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "name") String name) {
		try {
			loginWithSecurityToken(securityToken);
			return new QueryResponse((LogicalQuery) ExecutorServiceBinding
					.getExecutor().getLogicalQueryByName(name), true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new QueryResponse(null, false);
		}
	}

	public IntegerCollectionResponse getLogicalQueryIds(
			@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			return new IntegerCollectionResponse(ExecutorServiceBinding
					.getExecutor().getLogicalQueryIds(), true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new IntegerCollectionResponse(null, false);
		}
	}

	public ConnectionInformationResponse getConnectionInformation(
			@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "queryId") int queryId) {
		try {
			loginWithSecurityToken(securityToken);
			int port = 0;
			if (!socketPortMap.containsKey(queryId)) {
				// no socketsink available so create one
				int minPort = Integer.valueOf(OdysseusConfiguration.getInt(
						"minSinkPort", SINK_MIN_PORT));
				int maxPort = Integer.valueOf(OdysseusConfiguration.getInt(
						"maxSinkPort", SINK_MAX_PORT));
				port = getNextFreePort(minPort, maxPort);
				addSocketSink(queryId, port);
			} else {
				// there is already a socketsink so we can use the port
				port = socketPortMap.get(queryId);
			}
			ConnectionInformation connectInfo = new ConnectionInformation(port,
					InetAddress.getLocalHost().getHostAddress());
			return new ConnectionInformationResponse(connectInfo, true);
		} catch (WebserviceException | UnknownHostException e) {
			e.printStackTrace();
			return new ConnectionInformationResponse(null, false);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addSocketSink(int queryId, int port) {
		IExecutionPlan plan = ExecutorServiceBinding.getExecutor()
				.getExecutionPlan();
		IPhysicalQuery query = plan.getQueryById(queryId);
		List<IPhysicalOperator> roots = query.getRoots();
		if (roots.size() == 1) {
			ISinkStreamHandlerBuilder sinkStreamHandlerBuilder = new ByteBufferSinkStreamHandlerBuilder();
			boolean useNio = true;
			boolean loginNeeded = false;
			int sourceInPort = 0;
			int sourceOutPort = 0;
			// copied from TRelationSocketSinkAORule
			IDataHandler<?> handler = new TupleDataHandler().getInstance(roots
					.get(0).getOutputSchema());
			ByteBufferHandler<Tuple<ITimeInterval>> objectHandler = new ByteBufferHandler<Tuple<ITimeInterval>>(
					handler);
			ISink sink = new SocketSinkPO(port, "", sinkStreamHandlerBuilder,
					useNio, loginNeeded, objectHandler, false);
			sink.subscribeToSource(roots.get(0), sourceInPort, sourceOutPort,
					roots.get(0).getOutputSchema());
			sink.open();
		} else {
			// TODO solution for a plan with more roots
		}
	}

	private int getNextFreePort(int min, int max) {
		int port = 0;
		do {
			port = min + (int) (Math.random() * ((max - min) + 1));
		} while (socketPortMap.containsKey(port));
		return port;
	}

	public SDFSchemaResponse getOutputSchema(
			@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "queryId") int queryId) {
		try {
			loginWithSecurityToken(securityToken);
			SDFSchema schema = ExecutorServiceBinding.getExecutor()
					.getOutputSchema(queryId);
			Collection<SDFAttribute> attributes = schema.getAttributes();
			Collection<SDFAttributeInformation> attrInfo = new ArrayList<SDFAttributeInformation>();
			for (SDFAttribute attr : attributes) {
				attrInfo.add(new SDFAttributeInformation(attr.getSourceName(),
						attr.getAttributeName(), new SDFDatatypeInformation(
								attr.getDatatype().getURI())));
			}
			SDFSchemaInformation info = new SDFSchemaInformation(
					schema.getURI(), attrInfo);
			return new SDFSchemaResponse(info, true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new SDFSchemaResponse(null, false);
		}
	}

	public SDFSchemaResponse getOutputSchemaByQueryId(
			@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "queryId") int queryId) {
		try {
			loginWithSecurityToken(securityToken);
			SDFSchema schema = ExecutorServiceBinding.getExecutor()
					.getOutputSchema(queryId);
			Collection<SDFAttribute> attributes = schema.getAttributes();
			Collection<SDFAttributeInformation> attrInfo = new ArrayList<SDFAttributeInformation>();
			for (SDFAttribute attr : attributes) {
				attrInfo.add(new SDFAttributeInformation(attr.getSourceName(),
						attr.getAttributeName(), new SDFDatatypeInformation(
								attr.getDatatype().getURI())));
			}
			SDFSchemaInformation info = new SDFSchemaInformation(
					schema.getURI(), attrInfo);
			return new SDFSchemaResponse(info, true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new SDFSchemaResponse(null, false);
		}
	}

	public SDFSchemaResponse getOutputSchemaBySource(
			@WebParam(name = "securitytoken") String securityToken,
			@WebParam(name = "sourcename") String sourcename) {
		try {
			loginWithSecurityToken(securityToken);
			SDFSchema schema = ExecutorServiceBinding.getExecutor()
					.getDataDictionary().getSources().get(sourcename)
					.getOutputSchema();
			Collection<SDFAttribute> attributes = schema.getAttributes();
			Collection<SDFAttributeInformation> attributeInfos = new ArrayList<SDFAttributeInformation>();
			for (SDFAttribute attribute : attributes) {
				attributeInfos.add(new SDFAttributeInformation(attribute
						.getSourceName(), attribute.getAttributeName(),
						new SDFDatatypeInformation(attribute.getDatatype()
								.getURI())));
			}
			SDFSchemaInformation info = new SDFSchemaInformation(
					schema.getURI(), attributeInfos);
			return new SDFSchemaResponse(info, true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new SDFSchemaResponse(null, false);
		}
	}

	public SourceListResponse getSources(
			@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			Map<String, ISource<?>> sources = ExecutorServiceBinding
					.getExecutor().getDataDictionary().getSources();
			List<SourceInformation> sourceInfos = new ArrayList<SourceInformation>();

			for (ISource<?> source : sources.values()) {
				SDFSchema schema = source.getOutputSchema();
				Collection<SDFAttribute> attributes = schema.getAttributes();
				Collection<SDFAttributeInformation> attributeInfos = new ArrayList<SDFAttributeInformation>();
				for (SDFAttribute attribute : attributes) {
					attributeInfos.add(new SDFAttributeInformation(attribute
							.getSourceName(), attribute.getAttributeName(),
							new SDFDatatypeInformation(attribute.getDatatype()
									.getURI())));
				}
				SDFSchemaInformation schemaInfo = new SDFSchemaInformation(
						schema.getURI(), attributeInfos);
				sourceInfos.add(new SourceInformation(schemaInfo, source
						.getName(), source.getOwnerIDs()));
			}
			return new SourceListResponse(sourceInfos, true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new SourceListResponse(null, false);
		}
	}

	public OperatorBuilderListResponse getOperatorBuilderList(
			@WebParam(name = "securitytoken") String securityToken) {
		try {
			loginWithSecurityToken(securityToken);
			List<IOperatorBuilder> opBuilders = getOperatorBuilderFactory()
					.getOperatorBuilder();
			List<OperatorBuilderInformation> opInfo = extractOperatorBuilderInformation(opBuilders);
			return new OperatorBuilderListResponse(opInfo, true);
		} catch (WebserviceException e) {
			e.printStackTrace();
			return new OperatorBuilderListResponse(null, false);
		}

	}

	private List<OperatorBuilderInformation> extractOperatorBuilderInformation(
			List<IOperatorBuilder> builders) {
		List<OperatorBuilderInformation> infos = new ArrayList<OperatorBuilderInformation>();
		for (IOperatorBuilder builder : builders) {
			OperatorBuilderInformation info = new OperatorBuilderInformation();
			info.setParameters(extractParameterInformation(builder
					.getParameters()));
		}
		return infos;
	}

	@SuppressWarnings("rawtypes")
	private Set<ParameterInfo> extractParameterInformation(
			Set<IParameter<?>> parameters) {
		Set<ParameterInfo> infos = new HashSet<ParameterInfo>();
		for (IParameter parameter : parameters) {
			ParameterInfo info = new ParameterInfo();
			info.setName(parameter.getName());
			info.setRequirement(ParameterInfo.REQUIREMENT.valueOf(parameter
					.getRequirement().name()));
			info.setValue((String) parameter.getValue());
			infos.add(info);
		}
		return infos;
	}
}
