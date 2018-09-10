package de.uniol.inf.is.odysseus.rest.impl;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.NonBlockingTcpServerHandler_Netty;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.QuerySourceAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PhysicalOperatorParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest.socket.SocketService;
import de.uniol.inf.is.odysseus.server.keyvalue.logicaloperator.ToKeyValueAO;

/**
 * This class manages the websocket queries. It can create new websocket queries
 * or reuse existing ones. Necessary for the REST interface.
 * 
 * @author Tobias Brandt
 *
 */
public class WebsocketManager {

	private static WebsocketManager instance;

	// query id -> operator name -> port -> websocket URL
	private Map<Integer, Map<String, Map<Integer, String>>> websockets;
	// websocket URL -> query id of the websocket query
	private Map<String, Integer> queryIds;
	private int lastUsedPort = 8001;

	private WebsocketManager() {
		this.websockets = new HashMap<>();
		this.queryIds = new HashMap<>();
	}

	public static WebsocketManager getInstance() {
		if (instance == null) {
			instance = new WebsocketManager();
		}
		return instance;
	}

	/**
	 * Returns the information about the websocket query / queries for the given
	 * query. If there were created websocket queries before for this query and they
	 * are still available, they are reused. If not, new websocket queries are
	 * created.
	 * 
	 * @param queryId
	 *            The query for which you want to have the websockets
	 * @param session
	 *            The current session
	 * @return The URLs to the websockets. As every query can have multiple sinks,
	 *         the URLs are organized as follows: operator name -> port -> websocket
	 *         URL
	 * @throws UnknownHostException
	 *             Thrown if the local IP of this Odysseus instance cannot be
	 *             determined
	 */
	public Map<String, Map<Integer, String>> getWebsockets(int queryId, ISession session) throws UnknownHostException {
		if (!websockets.containsKey(queryId)) {
			// We don't have websockets for this query yet -> create
			websockets.put(queryId, createWebsockets(queryId, session));
		} else if (!allWebsocketQueriesArePresent(queryId, session)) {
			// Some of the previously existing websocket queries are no longer present ->
			// create new and overwrite old
			websockets.put(queryId, createWebsockets(queryId, session));
		}
		return websockets.get(queryId);
	}

	/**
	 * Checks, if all websocket queries for this query are still present if there
	 * were once websocket queries for the given query id installed.
	 * 
	 * @param queryId
	 *            The query for which websocket queries where once installed
	 * @return true, of they are still ALL present, false if one or more are missing
	 */
	private boolean allWebsocketQueriesArePresent(int queryId, ISession session) {
		// Go through operators
		for (String operatorName : websockets.get(queryId).keySet()) {
			// Go through output ports
			for (int outputPort : websockets.get(queryId).get(operatorName).keySet()) {
				String websocketURL = websockets.get(queryId).get(operatorName).get(outputPort);
				int websocketQueryId = queryIds.get(websocketURL);
				// Check, if this websocket query is still running
				if (!ExecutorServiceBinding.getExecutor().getLogicalQueryIds(session).contains(websocketQueryId)) {
					// This websocket query is no longer present - we have to install new queries
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Creates the websockets for the given query
	 * 
	 * @param queryId
	 *            The query where the websocket(s) shall be created for
	 * @param session
	 *            The current session
	 * @return The URLs to the websockets. As every query can have multiple sinks,
	 *         the URLs are organized as follows: operator name -> port -> websocket
	 *         URL
	 * @throws UnknownHostException
	 */
	private Map<String, Map<Integer, String>> createWebsockets(int queryId, ISession session)
			throws UnknownHostException {
		Map<String, Map<Integer, String>> resultMap = new HashMap<String, Map<Integer, String>>();

		// Create a websocket sink query for every root operator

		// Get the roots from the given query
		List<IPhysicalOperator> rootOperators = SocketService.getInstance().getRootOperators(queryId, session);

		for (IPhysicalOperator root : rootOperators) {

			Map<Integer, String> portMap = new HashMap<>();
			resultMap.put(root.getName(), portMap);

			// Get the hashcode
			int hashCode = root.hashCode();

			// Create a websocket for each output port of the operator
			int numberOfOutputPorts = root.getOutputSchemas().size();
			for (int outputPort = 0; outputPort < numberOfOutputPorts; outputPort++) {

				// Find a free port
				while (!portAvailable(lastUsedPort)) {
					lastUsedPort++;
				}

				// Create a URL
				String protocol = "ws";
				String ip = Inet4Address.getLocalHost().getHostAddress();
				String path = "/websocket/" + queryId + "/" + root.getName() + "/" + outputPort;
				String url = protocol + "://" + ip + ":" + lastUsedPort + path;

				// 3. Connect to the stream from the operator
				QuerySourceAO querySource = new QuerySourceAO();
				ILogicalOperator sourceForSender = querySource;

				PhysicalOperatorParameter physicalParam = new PhysicalOperatorParameter();
				List<String> inputParameters = new ArrayList<>(2);
				inputParameters.add(String.valueOf(queryId));
				inputParameters.add(String.valueOf(hashCode));
				physicalParam.setInputValue(inputParameters);
				physicalParam.setServerExecutor(ExecutorServiceBinding.getExecutor());
				physicalParam.setCaller(session);
				querySource.setQueryName(physicalParam.getValue());
				querySource.setPort(0);

				// Convert to KeyValue
				// if already KeyValue, don't do this
				if (!root.getOutputSchema().isEmpty()) {
					ToKeyValueAO toKeyValueAO = new ToKeyValueAO();
					toKeyValueAO.subscribeToSource(querySource, 0, 0, querySource.getOutputSchema());
					sourceForSender = toKeyValueAO;
				}

				// Create sender with WebSocket
				SenderAO sender = new SenderAO();
				sender.subscribeToSource(sourceForSender, 0, 0, querySource.getOutputSchema());
				sender.setProtocolHandler("JSON");
				sender.setTransportHandler("TCPServer");
				sender.setDataHandler("KeyValueObject");
				Resource resource = new Resource(session.getUser(), "webSink_" + hashCode);
				sender.setSink(resource);
				sender.setWrapper("GenericPush");
				Option portOption = new Option(NonBlockingTcpServerHandler_Netty.PORT, lastUsedPort);
				Option websocketOption = new Option(NonBlockingTcpServerHandler_Netty.WEBSOCKET, true);
				Option websocketPathOption = new Option(NonBlockingTcpServerHandler_Netty.PATH_OPTION, path);
				List<Option> senderOptions = new ArrayList<>(3);
				senderOptions.add(portOption);
				senderOptions.add(websocketOption);
				senderOptions.add(websocketPathOption);
				sender.setOptions(senderOptions);

				// Install query
				int websocketQueryId = ExecutorServiceBinding.getExecutor().addQuery(sender, session, "Standard");

				// Start query
				ExecutorServiceBinding.getExecutor().startQuery(websocketQueryId, session);

				// Add websocket to the returned information
				portMap.put(outputPort, url);

				// Store, which URL maps to which query
				this.queryIds.put(url, websocketQueryId);
			}
		}

		return resultMap;
	}

	/**
	 * Checks if a port is available / free. Source:
	 * https://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java
	 * 
	 * @param port
	 *            port to test
	 * @return true, if the port is available
	 */
	private static boolean portAvailable(int port) {
		try (Socket ignored = new Socket("localhost", port)) {
			return false;
		} catch (IOException ignored) {
			return true;
		}
	}

}
