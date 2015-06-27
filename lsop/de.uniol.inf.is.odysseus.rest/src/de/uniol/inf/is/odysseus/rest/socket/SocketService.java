package de.uniol.inf.is.odysseus.rest.socket;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.NullAwareTupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.NioByteBufferSinkStreamHandlerBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SocketSinkPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;

/**
 * This service creates sockets for outputs. The sockets (SocketSinkPO) get
 * stored to use them multiple times if necessary.
 * 
 * @author Thore, Tobias Brandt
 *
 */
public class SocketService extends Observable {

	// QueryId, OutputOperator, OperatorOutputPort
	private Map<Integer, Map<IPhysicalOperator, Map<Integer, SocketSinkPO>>> socketSinkMap = new HashMap<>();

	private InetAddress address;

	private static final int SINK_MIN_PORT = 10000;
	private static final int SINK_MAX_PORT = 20000;

	private static SocketService instance;

	private SocketService() {
	}

	public static SocketService getInstance() {
		if (SocketService.instance == null) {
			SocketService.instance = new SocketService();
		}
		return SocketService.instance;
	}

	/**
	 * Legacy support for old versions. Returns a SocketInformation of the first
	 * output port from the given operator.
	 * 
	 * @param session
	 *            Session to create a socket
	 * @param queryId
	 *            Id of the query
	 * @param outputOperator
	 *            The operator you want to send the output via a socket
	 * @return SocketInformation about the socket
	 */
	public SocketInfo getConnectionInformation(ISession session, int queryId, int outputOperator) {
		int minPort = Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort", SINK_MIN_PORT));
		int maxPort = Integer.valueOf(OdysseusConfiguration.getInt("maxSinkPort", SINK_MAX_PORT));
		IPhysicalOperator rootOperator = getRootOperator(queryId, outputOperator);
		SocketInfo info = getConnectionInformationWithPorts(session, queryId, minPort, maxPort, rootOperator, 0, false);
		return info;
	}

	/**
	 * Legacy support for old versions. Returns a SocketInformation of the first
	 * output port from the given operator.
	 * 
	 * @param session
	 *            Session to create a socket
	 * @param queryId
	 *            Id of the query
	 * @param operator
	 *            The operator you want to send the output via a socket
	 * @return SocketInformation about the socket
	 */
	public SocketInfo getConnectionInformation(ISession session, int queryId, IPhysicalOperator operator) {
		int minPort = Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort", SINK_MIN_PORT));
		int maxPort = Integer.valueOf(OdysseusConfiguration.getInt("maxSinkPort", SINK_MAX_PORT));
		SocketInfo info = getConnectionInformationWithPorts(session, queryId, minPort, maxPort, operator, 0, false);
		return info;
	}

	/**
	 * Creates sockets for all outputs of the given query and returns the
	 * information needed to connect to this socket
	 * 
	 * @param session
	 *            The session (for the user/right management)
	 * @param queryName
	 *            The name of the query you want to get the socket connection to
	 * @param withMetaData
	 *            If you want to send the metadata via the socket. Not working
	 *            now.
	 * @return The outer map has the name of the operator as key, the inner map
	 *         the output port of the operator
	 */
	public Map<String, Map<Integer, SocketInfo>> getMultipleConnectionInformation(ISession session, String queryName,
			boolean withMetaData) {
		List<IPhysicalOperator> rootOperators = getRootOperators(queryName);
		int minPort = Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort", SINK_MIN_PORT));
		int maxPort = Integer.valueOf(OdysseusConfiguration.getInt("maxSinkPort", SINK_MAX_PORT));
		Map<String, Map<Integer, SocketInfo>> allInfos = new HashMap<>();
		for (IPhysicalOperator root : rootOperators) {
			Map<Integer, SocketInfo> partInfos = getConnectionInformationWithPorts(session, queryName, minPort, maxPort,
					root, withMetaData);
			allInfos.put(root.getName(), partInfos);
		}
		return allInfos;
	}

	/**
	 * Creates sockets for all output ports of the given operator in the given
	 * query and returns the information needed to connect to this socket
	 * 
	 * @param session
	 *            The session (for the user/right management)
	 * @param queryName
	 *            The name of the query you want to get the socket connection to
	 * @param operatorName
	 *            The name of the operator you want the output from. Make sure
	 *            that the name is set in the operator.
	 * @param withMetaData
	 *            If you want to send the metadata via the socket. Not working
	 *            now.
	 * @return The outer map has the name of the operator as key, the inner map
	 *         the output port of the operator
	 */
	public Map<String, Map<Integer, SocketInfo>> getMultipleConnectionInformation(ISession session, String queryName,
			String operatorName, boolean withMetaData) {
		IPhysicalOperator rootOperator = getRootOperator(queryName, operatorName);
		if (rootOperator == null) {
			return new HashMap<String, Map<Integer, SocketInfo>>();
		}
		int minPort = Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort", SINK_MIN_PORT));
		int maxPort = Integer.valueOf(OdysseusConfiguration.getInt("maxSinkPort", SINK_MAX_PORT));

		Map<Integer, SocketInfo> infos = getConnectionInformationWithPorts(session, queryName, minPort, maxPort,
				rootOperator, withMetaData);
		Map<String, Map<Integer, SocketInfo>> output = new HashMap<>();
		output.put(rootOperator.getName(), infos);
		return output;
	}

	/**
	 * Creates a socket for the given output port of the given operator in the
	 * given query and returns the information needed to connect to this socket
	 * 
	 * @param session
	 *            The session (for the user/right management)
	 * @param queryName
	 *            The name of the query you want to get the socket connection to
	 * @param operatorName
	 *            The name of the operator you want the output from. Make sure
	 *            that the name is set in the operator.
	 * @param operatorOutputPort
	 *            The output port of the operator you want to get the socket for
	 * @param withMetaData
	 *            If you want to send the metadata via the socket. Not working
	 *            now.
	 * @return The Socket Information
	 */
	public SocketInfo getConnectionInformation(ISession session, String queryName, String operatorName,
			int operatorOutputPort, boolean withMetaData) {
		IPhysicalOperator rootOperator = getRootOperator(queryName, operatorName);
		int minPort = Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort", SINK_MIN_PORT));
		int maxPort = Integer.valueOf(OdysseusConfiguration.getInt("maxSinkPort", SINK_MAX_PORT));
		SocketInfo info = getConnectionInformationWithPorts(session, queryName, minPort, maxPort, rootOperator,
				operatorOutputPort, withMetaData);
		return info;
	}

	/**
	 * Creates sockets for all outputs of the given query and returns the
	 * information needed to connect to this socket
	 * 
	 * @param session
	 *            The session (for the user/right management)
	 * @param queryId
	 *            The id of the query you want to get the socket connection to
	 * @param withMetaData
	 *            If you want to send the metadata via the socket. Not working
	 *            now.
	 * @return The outer map has the name of the operator as key, the inner map
	 *         the output port of the operator
	 */
	public Map<String, Map<Integer, SocketInfo>> getMultipleConnectionInformation(ISession session, int queryId,
			boolean withMetaData) {
		List<IPhysicalOperator> rootOperators = getRootOperators(queryId);
		int minPort = Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort", SINK_MIN_PORT));
		int maxPort = Integer.valueOf(OdysseusConfiguration.getInt("maxSinkPort", SINK_MAX_PORT));
		Map<String, Map<Integer, SocketInfo>> allInfos = new HashMap<>();
		for (IPhysicalOperator root : rootOperators) {
			Map<Integer, SocketInfo> partInfos = getConnectionInformationWithPorts(session, queryId, minPort, maxPort,
					root, withMetaData);
			allInfos.put(root.getName(), partInfos);
		}
		return allInfos;
	}

	/**
	 * Creates sockets for all output ports of the given operator in the given
	 * query and returns the information needed to connect to this socket
	 * 
	 * @param session
	 *            The session (for the user/right management)
	 * @param queryId
	 *            The id of the query you want to get the socket connection to
	 * @param operatorName
	 *            The name of the operator you want the output from. Make sure
	 *            that the name is set in the operator.
	 * @param withMetaData
	 *            If you want to send the metadata via the socket. Not working
	 *            now.
	 * @return The outer map has the name of the operator as key, the inner map
	 *         the output port of the operator
	 */
	public Map<String, Map<Integer, SocketInfo>> getMultipleConnectionInformation(ISession session, int queryId, String operatorName,
			boolean withMetaData) {
		IPhysicalOperator rootOperator = getRootOperator(queryId, operatorName);
		int minPort = Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort", SINK_MIN_PORT));
		int maxPort = Integer.valueOf(OdysseusConfiguration.getInt("maxSinkPort", SINK_MAX_PORT));
		Map<Integer, SocketInfo> socketInfos = getConnectionInformationWithPorts(session, queryId, minPort, maxPort,
				rootOperator, withMetaData);
		Map<String, Map<Integer, SocketInfo>> output = new HashMap<>();
		output.put(rootOperator.getName(), socketInfos);
		return output;
	}

	/**
	 * Creates a socket for the given output port of the given operator in the
	 * given query and returns the information needed to connect to this socket
	 * 
	 * @param session
	 *            The session (for the user/right management)
	 * @param queryId
	 *            The id of the query you want to get the socket connection to
	 * @param operatorOutputPort
	 *            The output port of the operator you want to get the socket for
	 * @param withMetaData
	 *            If you want to send the metadata via the socket. Not working
	 *            now.
	 * @return A list with information about the created sockets
	 */
	public SocketInfo getConnectionInformation(ISession session, int queryId, String operatorName,
			int operatorOutputPort, boolean withMetaData) {
		IPhysicalOperator rootOperator = getRootOperator(queryId, operatorName);
		int minPort = Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort", SINK_MIN_PORT));
		int maxPort = Integer.valueOf(OdysseusConfiguration.getInt("maxSinkPort", SINK_MAX_PORT));
		SocketInfo socketInfo = getConnectionInformationWithPorts(session, queryId, minPort, maxPort, rootOperator,
				operatorOutputPort, withMetaData);
		return socketInfo;
	}

	/**
	 * Creates a socket for the given output port of the given operator in the
	 * given query and returns the information needed to connect to this socket
	 * 
	 * @param session
	 *            The session (for the user/rights management)
	 * @param queryId
	 *            The id of the query you want to get the socket connection to
	 * @param operator
	 *            The operator you want to get the sockets for
	 * @param withMetaData
	 *            If you want to send the metadata via the socket. Not working
	 *            now.
	 * @return A map with information about the created sockets
	 */
	public Map<Integer, SocketInfo> getMultipleConnectionInformation(ISession session, int queryId,
			IPhysicalOperator operator, boolean withMetaData) {
		int minPort = Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort", SINK_MIN_PORT));
		int maxPort = Integer.valueOf(OdysseusConfiguration.getInt("maxSinkPort", SINK_MAX_PORT));
		return getConnectionInformationWithPorts(session, queryId, minPort, maxPort, operator, withMetaData);
	}

	/**
	 * Inform listeners about a new client socket.
	 * 
	 * @param queryId
	 *            The id of the query
	 * @param clientIp
	 *            The IP address of the client
	 */
	public void informAboutNewClient(int queryId, String clientIp, String hostIP, int hostPort) {
		ClientIpInfo info = new ClientIpInfo();
		info.queryId = queryId;
		info.clientIP = clientIp;
		info.hostIP = hostIP;
		info.hostPort = hostPort;
		this.setChanged();
		this.notifyObservers(info);
	}

	/**
	 * Searches for the connection information for the given operator. If there
	 * is no socket for this operator, the socket will be created.
	 * 
	 * @param session
	 *            The session for user / right management
	 * @param queryName
	 *            The name of the query where this operator is in
	 * @param minPort
	 *            The minimum possible port for a maybe necessary new socket
	 * @param maxPort
	 *            The maximum possible port for a maybe necessary new socket
	 * @param operator
	 *            The operator where you want to have the socket connection
	 *            information for
	 * @return A list with all socket information available (one operator can
	 *         have multiple outputs and hence multiple sockets)
	 */
	private SocketInfo getConnectionInformationWithPorts(ISession session, String queryName, int minPort, int maxPort,
			IPhysicalOperator operator, int operatorOutputPort, boolean withMetaData) {
		IExecutionPlan plan = ExecutorServiceBinding.getExecutor().getExecutionPlan();
		int queryId = plan.getQueryByName(queryName).getID();
		return getConnectionInformationWithPorts(session, queryId, minPort, maxPort, operator, operatorOutputPort,
				withMetaData);
	}

	private Map<Integer, SocketInfo> getConnectionInformationWithPorts(ISession session, String queryName, int minPort,
			int maxPort, IPhysicalOperator operator, boolean withMetaData) {
		IExecutionPlan plan = ExecutorServiceBinding.getExecutor().getExecutionPlan();
		int queryId = plan.getQueryByName(queryName).getID();
		return getConnectionInformationWithPorts(session, queryId, minPort, maxPort, operator, withMetaData);
	}

	/**
	 * Searches for the connection information for the given operator. If there
	 * is no socket for this operator, the socket will be created.
	 * 
	 * @param session
	 *            The session for user / right management
	 * @param queryId
	 *            The id of the query where this operator is in
	 * @param minPort
	 *            The minimum possible port for a maybe necessary new socket
	 * @param maxPort
	 *            The maximum possible port for a maybe necessary new socket
	 * @param operator
	 *            The operator where you want to have the socket connection
	 *            information for
	 * @return A map with all socket information available (one operator can
	 *         have multiple outputs and hence multiple sockets)
	 */
	private Map<Integer, SocketInfo> getConnectionInformationWithPorts(ISession session, int queryId, int minPort,
			int maxPort, IPhysicalOperator operator, boolean withMetaData) {

		Map<Integer, SDFSchema> outputMap = operator.getOutputSchemas();
		Map<Integer, SocketInfo> socketInfos = new HashMap<Integer, SocketInfo>();
		for (int operatorOutputPort : outputMap.keySet()) {
			SocketInfo socketInfo = getConnectionInformationWithPorts(session, queryId, minPort, maxPort, operator,
					operatorOutputPort, withMetaData);
			socketInfos.put(operatorOutputPort, socketInfo);
		}
		return socketInfos;
	}

	private SocketInfo getConnectionInformationWithPorts(ISession session, int queryId, int minPort, int maxPort,
			IPhysicalOperator operator, int operatorOutputPort, boolean withMetaData) {
		// Check, if we have a map for this operator
		Map<Integer, SocketSinkPO> mapForOperatorPort = null;
		Map<IPhysicalOperator, Map<Integer, SocketSinkPO>> mapForOp = socketSinkMap.get(queryId);
		if (mapForOp != null) {
			mapForOperatorPort = mapForOp.get(operator);
		} else {
			mapForOp = new HashMap<>();
			socketSinkMap.put(queryId, mapForOp);
		}

		// Check, if we have a map for this output port for this operator
		if (mapForOperatorPort == null) {
			mapForOperatorPort = new HashMap<>();
			mapForOp.put(operator, mapForOperatorPort);
		}

		SocketSinkPO socketSink = mapForOperatorPort.get(operatorOutputPort);
		if (socketSink == null) {
			socketSink = addSocketSink(queryId, minPort, maxPort, operator, operatorOutputPort, session.getToken(),
					withMetaData);
			mapForOperatorPort.put(operatorOutputPort, socketSink);
		}

		return getSocketInfo(socketSink, operator, withMetaData);
	}

	private SocketInfo getSocketInfo(SocketSinkPO socketSink, IPhysicalOperator operator, boolean withMetaData) {
		try {

			// Get our address
			if (this.address == null) {

				Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
				while (interfaces.hasMoreElements()) {
					NetworkInterface ni = interfaces.nextElement();
					if (ni.isVirtual()) {
						continue;
					}
					if (ni.isLoopback()) {
						continue;
					}
					if (ni.getInetAddresses().hasMoreElements()) {
						this.address = ni.getInetAddresses().nextElement();
					}
				}
			}

			SocketInfo socketInfo = new SocketInfo(InetAddress.getLocalHost().getHostAddress(),
					socketSink.getServerPort(), this.getAttributeInformationList(operator, withMetaData));
			return socketInfo;

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<AttributeInformation> getAttributeInformationList(IPhysicalOperator operator, boolean withMetaData) {
		// Data output
		SDFSchema outputSchema = operator.getOutputSchema();
		List<SDFAttribute> attibuteList = outputSchema.getAttributes();
		// List<SDFMetaSchema> metaschema = outputSchema.getMetaschema();

		ArrayList<AttributeInformation> attributeInformationList = new ArrayList<AttributeInformation>();
		for (SDFAttribute sdfAttribute : attibuteList) {
			attributeInformationList.add(new AttributeInformation(sdfAttribute.getAttributeName(),
					sdfAttribute.getDatatype().getQualName()));
		}

		return attributeInformationList;
	}

	private int getNextFreePort(int min, int max) {
		List<Integer> usedPorts = new ArrayList<Integer>();

		// Collect all used ports
		for (Object queryIndicator : this.socketSinkMap.keySet()) {
			Map<IPhysicalOperator, Map<Integer, SocketSinkPO>> mapForOps = this.socketSinkMap.get(queryIndicator);
			for (IPhysicalOperator op : mapForOps.keySet()) {
				Map<Integer, SocketSinkPO> mapForPorts = mapForOps.get(op);
				for (int serverPort : mapForPorts.keySet()) {
					usedPorts.add(serverPort);
				}
			}
		}

		int port = 0;
		do {
			port = min + (int) (Math.random() * ((max - min) + 1));
		} while (usedPorts.contains(port));
		return port;
	}

	private IPhysicalOperator getRootOperator(int queryId, int rootPort) {
		List<IPhysicalOperator> roots = getRootOperators(queryId);
		return roots.get(rootPort);
	}

	private List<IPhysicalOperator> getRootOperators(int queryId) {
		IExecutionPlan plan = ExecutorServiceBinding.getExecutor().getExecutionPlan();
		IPhysicalQuery query = plan.getQueryById(queryId);
		return query.getRoots();
	}

	/**
	 * 
	 * @param queryId
	 *            The id of the query you want to search in
	 * @param operatorName
	 *            The name of the operator you want to get
	 * @return The operator with the given name in the query or null, if such an
	 *         operator is not found
	 */
	private IPhysicalOperator getRootOperator(int queryId, String operatorName) {
		List<IPhysicalOperator> allOps = getRootOperators(queryId);
		for (IPhysicalOperator op : allOps) {
			if (op.getName().equals(operatorName)) {
				return op;
			}
		}
		return null;
	}

	private List<IPhysicalOperator> getRootOperators(String queryName) {
		IExecutionPlan plan = ExecutorServiceBinding.getExecutor().getExecutionPlan();
		IPhysicalQuery query = plan.getQueryByName(queryName);
		return query.getRoots();
	}

	private IPhysicalOperator getRootOperator(String queryName, String operatorName) {
		List<IPhysicalOperator> allOps = getRootOperators(queryName);
		for (IPhysicalOperator op : allOps) {
			if (op.getName().equals(operatorName)) {
				return op;
			}
			if (op.getUniqueIds().containsValue(operatorName)) {
				return op;
			}

		}

		return null;
	}

	private SocketSinkPO addSocketSink(int queryId, int minPort, int maxPort, IPhysicalOperator root, int rootPort,
			String token, boolean withMetaData) {
		IExecutionPlan plan = ExecutorServiceBinding.getExecutor().getExecutionPlan();
		IPhysicalQuery query = plan.getQueryById(queryId);
		return addSocketSink(query, minPort, maxPort, root, rootPort, token, withMetaData);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private SocketSinkPO addSocketSink(IPhysicalQuery query, int minPort, int maxPort, IPhysicalOperator root,
			int rootOutputPort, String token, boolean withMetaData) {
		if (!(root instanceof ISource)) {
			// E.g. a MongoDBSinkPO is no ISource and can't be processed here
			return null;
		}
		final ISource<?> rootAsSource = (ISource<?>) root;
		if (root.getOutputSchema(rootOutputPort) != null) {
			NullAwareTupleDataHandler handler = new NullAwareTupleDataHandler(root.getOutputSchema(rootOutputPort));

			ByteBufferHandler<Tuple<ITimeInterval>> objectHandler = new ByteBufferHandler<Tuple<ITimeInterval>>(
					handler);
			int serverPort = getNextFreePort(minPort, maxPort);
			SocketSinkPO sink = new SocketSinkPO(serverPort, "", new NioByteBufferSinkStreamHandlerBuilder(), true,
					false, false, objectHandler, false, withMetaData);

			rootAsSource.subscribeSink((ISink) sink, 0, rootOutputPort, root.getOutputSchema(rootOutputPort), true, 0);
			sink.startListening();
			sink.addOwner(query);
			sink.addAllowedSessionId(token);
			return sink;
		}
		return null;
	}

	public class ClientIpInfo {
		public int queryId;
		public String clientIP;
		public String hostIP;
		public int hostPort;
	}

}
