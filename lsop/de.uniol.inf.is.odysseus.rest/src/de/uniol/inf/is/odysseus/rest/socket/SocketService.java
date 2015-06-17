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

public class SocketService extends Observable {

	// QueryId or name, OutputOperator, OutputPort
	private Map<Object, Map<IPhysicalOperator, Map<Integer, SocketSinkPO>>> socketSinkMap = new HashMap<>();

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

	// Legacy support
	public SocketInfo getConnectionInformation(ISession session, int queryId, int outputOperator) {
		int minPort = Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort", SINK_MIN_PORT));
		int maxPort = Integer.valueOf(OdysseusConfiguration.getInt("maxSinkPort", SINK_MAX_PORT));
		IPhysicalOperator rootOperator = getRootOperator(queryId, outputOperator);
		List<SocketInfo> infos = getConnectionInformationWithPorts(session, queryId, minPort, maxPort, rootOperator);
		if (!infos.isEmpty()) {
			return infos.get(0);
		}
		return null;
	}

	// Legacy support
	public SocketInfo getConnectionInformation(ISession session, int queryId, IPhysicalOperator operator) {
		int minPort = Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort", SINK_MIN_PORT));
		int maxPort = Integer.valueOf(OdysseusConfiguration.getInt("maxSinkPort", SINK_MAX_PORT));
		List<SocketInfo> infos = getConnectionInformationWithPorts(session, queryId, minPort, maxPort, operator);
		if (!infos.isEmpty()) {
			return infos.get(0);
		}
		return null;
	}

	/**
	 * Creates sockets for all outputs of the given query and returns the
	 * information needed to connect to this socket
	 * 
	 * @param session
	 *            The session (for the user/right management)
	 * @param queryName
	 *            The name of the query you want to get the socket connection to
	 * @return A list with information about the created sockets
	 */
	public List<SocketInfo> getMultipleConnectionInformation(ISession session, String queryName) {
		List<IPhysicalOperator> rootOperators = getRootOperators(queryName);
		int minPort = Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort", SINK_MIN_PORT));
		int maxPort = Integer.valueOf(OdysseusConfiguration.getInt("maxSinkPort", SINK_MAX_PORT));
		List<SocketInfo> allInfos = new ArrayList<SocketInfo>();
		for (IPhysicalOperator root : rootOperators) {
			List<SocketInfo> partInfos = getConnectionInformationWithPorts(session, queryName, minPort, maxPort, root);
			allInfos.addAll(partInfos);
		}
		return allInfos;
	}

	/**
	 * Creates sockets for all outputs of the given query and returns the
	 * information needed to connect to this socket
	 * 
	 * @param session
	 *            The session (for the user/right management)
	 * @param queryId
	 *            The id of the query you want to get the socket connection to
	 * @return A list with information about the created sockets
	 */
	public List<SocketInfo> getMultipleConnectionInformation(ISession session, int queryId) {
		List<IPhysicalOperator> rootOperators = getRootOperators(queryId);
		int minPort = Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort", SINK_MIN_PORT));
		int maxPort = Integer.valueOf(OdysseusConfiguration.getInt("maxSinkPort", SINK_MAX_PORT));
		List<SocketInfo> allInfos = new ArrayList<SocketInfo>();
		for (IPhysicalOperator root : rootOperators) {
			List<SocketInfo> partInfos = getConnectionInformationWithPorts(session, queryId, minPort, maxPort, root);
			allInfos.addAll(partInfos);
		}
		return allInfos;
	}

	public List<SocketInfo> getMultipleConnectionInformation(ISession session, int queryId, IPhysicalOperator operator) {
		int minPort = Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort", SINK_MIN_PORT));
		int maxPort = Integer.valueOf(OdysseusConfiguration.getInt("maxSinkPort", SINK_MAX_PORT));
		return getConnectionInformationWithPorts(session, queryId, minPort, maxPort, operator);
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
	private List<SocketInfo> getConnectionInformationWithPorts(ISession session, String queryName, int minPort,
			int maxPort, IPhysicalOperator operator) {
		// Check, if we have a map for this operator
		Map<Integer, SocketSinkPO> mapForPort = null;
		Map<IPhysicalOperator, Map<Integer, SocketSinkPO>> mapForOp = socketSinkMap.get(queryName);
		if (mapForOp != null) {
			mapForPort = mapForOp.get(operator);
		} else {
			mapForOp = new HashMap<>();
		}

		// Check, if we have a map for this output port for this operator
		if (mapForPort == null) {
			mapForPort = addSocketSink(queryName, minPort, maxPort, operator, session.getToken());
			mapForOp.put(operator, mapForPort);
			socketSinkMap.put(queryName, mapForOp);
		}

		return getConnectionInformationWithPorts(mapForPort, operator);
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
	 * @return A list with all socket information available (one operator can
	 *         have multiple outputs and hence multiple sockets)
	 */
	private List<SocketInfo> getConnectionInformationWithPorts(ISession session, int queryId, int minPort, int maxPort,
			IPhysicalOperator operator) {

		// Check, if we have a map for this operator
		Map<Integer, SocketSinkPO> mapForPort = null;
		Map<IPhysicalOperator, Map<Integer, SocketSinkPO>> mapForOp = socketSinkMap.get(queryId);
		if (mapForOp != null) {
			mapForPort = mapForOp.get(operator);
		} else {
			mapForOp = new HashMap<>();
		}

		// Check, if we have a map for this output port for this operator
		if (mapForPort == null) {
			mapForPort = addSocketSink(queryId, minPort, maxPort, operator, session.getToken());
			mapForOp.put(operator, mapForPort);
			socketSinkMap.put(queryId, mapForOp);
		}
		return getConnectionInformationWithPorts(mapForPort, operator);
	}

	private List<SocketInfo> getConnectionInformationWithPorts(Map<Integer, SocketSinkPO> mapForPort,
			IPhysicalOperator operator) {
		try {
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

			List<SocketInfo> socketInfos = new ArrayList<SocketInfo>(mapForPort.keySet().size());
			for (int serverPort : mapForPort.keySet()) {
				SocketInfo socketInfo = new SocketInfo(InetAddress.getLocalHost().getHostAddress(), serverPort,
						this.getAttributeInformationList(operator));
				socketInfos.add(socketInfo);
			}

			return socketInfos;

		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<AttributeInformation> getAttributeInformationList(IPhysicalOperator operator) {
		SDFSchema outputSchema = operator.getOutputSchema();

		List<SDFAttribute> attibuteList = outputSchema.getAttributes();

		ArrayList<AttributeInformation> attributeInformationList = new ArrayList<AttributeInformation>();
		for (SDFAttribute sdfAttribute : attibuteList) {
			attributeInformationList.add(new AttributeInformation(sdfAttribute.getAttributeName(), sdfAttribute
					.getDatatype().getQualName()));
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

	private List<IPhysicalOperator> getRootOperators(String queryName) {
		IExecutionPlan plan = ExecutorServiceBinding.getExecutor().getExecutionPlan();
		IPhysicalQuery query = plan.getQueryByName(queryName);
		return query.getRoots();
	}

	private Map<Integer, SocketSinkPO> addSocketSink(int queryId, int minPort, int maxPort, IPhysicalOperator root,
			String token) {
		IExecutionPlan plan = ExecutorServiceBinding.getExecutor().getExecutionPlan();
		IPhysicalQuery query = plan.getQueryById(queryId);
		return addSocketSink(query, minPort, maxPort, root, token);
	}

	private Map<Integer, SocketSinkPO> addSocketSink(String queryName, int minPort, int maxPort,
			IPhysicalOperator root, String token) {
		IExecutionPlan plan = ExecutorServiceBinding.getExecutor().getExecutionPlan();
		IPhysicalQuery query = plan.getQueryByName(queryName);
		return addSocketSink(query, minPort, maxPort, root, token);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<Integer, SocketSinkPO> addSocketSink(IPhysicalQuery query, int minPort, int maxPort,
			IPhysicalOperator root, String token) {
		if (!(root instanceof ISource)) {
			// E.g. a MongoDBSinkPO is no ISource and can't be processed here
			return new HashMap<Integer, SocketSinkPO>();
		}
		final ISource<?> rootAsSource = (ISource<?>) root;
		Map<Integer, SocketSinkPO> socketSinks = new HashMap<Integer, SocketSinkPO>(root.getOutputSchemas().size());
		for (int outputPort : root.getOutputSchemas().keySet()) {
			NullAwareTupleDataHandler handler = new NullAwareTupleDataHandler(root.getOutputSchema(outputPort));

			ByteBufferHandler<Tuple<ITimeInterval>> objectHandler = new ByteBufferHandler<Tuple<ITimeInterval>>(handler);
			int serverPort = getNextFreePort(minPort, maxPort);
			SocketSinkPO sink = new SocketSinkPO(serverPort, "", new NioByteBufferSinkStreamHandlerBuilder(), true,
					false, false, objectHandler, false, false);

			rootAsSource.subscribeSink((ISink) sink, 0, outputPort, root.getOutputSchema(outputPort), true, 0);
			sink.startListening();
			sink.addOwner(query);
			sink.addAllowedSessionId(token);
			socketSinks.put(serverPort, sink);
		}
		return socketSinks;
	}

	public class ClientIpInfo {
		public int queryId;
		public String clientIP;
		public String hostIP;
		public int hostPort;
	}

}
