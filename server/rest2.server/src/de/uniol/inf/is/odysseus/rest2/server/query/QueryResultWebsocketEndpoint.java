package de.uniol.inf.is.odysseus.rest2.server.query;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.msf4j.websocket.WebSocketEndpoint;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractObjectHandlerByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.OdysseusProtocolHandler;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.ThreadedBufferPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest2.server.exception.QueryNotFoundException;
import de.uniol.inf.is.odysseus.server.keyvalue.physicaloperator.TupleToKeyValuePO;

@ServerEndpoint(value = "/queries/{id}/{operator}/{port}/{protocol}/{securityToken}")
public class QueryResultWebsocketEndpoint extends AbstractSink<IStreamObject<IMetaAttribute>>
		implements WebSocketEndpoint, IOperatorOwner {

	public static final String PROTOCOL_JSON = "JSON";
	public static final String PROTOCOL_CSV = "CSV";
	public static final String PROTOCOL_BINARY = "Binary";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryResultWebsocketEndpoint.class);
	// TODO: Config!
	private static final int BUFFER_LIMIT = 1000;
	private Map<Integer, QueryResultReceiver> receiver = new HashMap<>();
	private Map<Session, Integer> sessionToInputPortMapping = new HashMap<>();
	// protocol, port, outputport, inputport
	private Map<String, Map<IPhysicalOperator, Map<Integer, Integer>>> connectedOperators = new HashMap<>();

	
	public static Set<String> protocols() {
		// use this after migration to Java > 8
		// return Set.of(PROTOCOL_JSON, PROTOCOL_CSV, PROTOCOL_BINARY);
		Set<String> result = new HashSet<>();
		result.add(PROTOCOL_JSON);
		result.add(PROTOCOL_CSV);
		result.add(PROTOCOL_BINARY);
		return result;
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO: synchronization necessary?
		QueryResultReceiver resultReceiver = receiver.get(port);
		if (resultReceiver != null) {
			if (resultReceiver.useSendText) {
				String toSend = punctuation.toString();
				sendText(resultReceiver.sessions, toSend);
			} else {
				ByteBuffer toSend0 = AbstractObjectHandlerByteBufferHandler.convertPunctuation(punctuation);
				ByteBuffer toSend = ByteBuffer
						.wrap(OdysseusProtocolHandler.addTypeInfo(toSend0, OdysseusProtocolHandler.PUNCT));
				sendBinary(resultReceiver.sessions, toSend);
			}
		}
	}

	@Override
	protected void process_next(IStreamObject<IMetaAttribute> object, int port) {
		// TODO: synchronization necessary?
		QueryResultReceiver resultReceiver = receiver.get(port);
		if (resultReceiver != null) {
			if (resultReceiver.useSendText) {
				if (resultReceiver.convertToCSV) {
					StringBuilder out = new StringBuilder();
					resultReceiver.dataHandler.writeCSVData(out, object, WriteOptions.defaultOptions);
					sendText(resultReceiver.sessions, out.toString());
				} else {
					sendText(resultReceiver.sessions,object.toString());
				}
			} else {
				ByteBuffer toSend0 = AbstractObjectHandlerByteBufferHandler.convertObject(object,
						resultReceiver.dataHandler);
				ByteBuffer toSend = ByteBuffer
						.wrap(OdysseusProtocolHandler.addTypeInfo(toSend0, OdysseusProtocolHandler.OBJECT));
				sendBinary(resultReceiver.sessions, toSend);
			}
		}
	}

	private void sendText(List<Session> sessions, String toSend) {
		// Sessions are now CopyOnWriteArrayLists
		sessions.forEach(session -> {
			try {
				session.getBasicRemote().sendText(toSend);
			} catch (IOException e) {
				if (e instanceof ClosedChannelException) {
					LOGGER.debug("Channel closed", e);
				} else {
					LOGGER.error("Problems sending value " + toSend, e);
				}
				onClose(new CloseReason(CloseCodes.GOING_AWAY, ""), session);
			}
		});
	}

	private void sendBinary(List<Session> sessions, ByteBuffer toSend) {
		// Sessions are now CopyOnWriteArrayLists
		sessions.forEach(session -> {
			try {
				session.getBasicRemote().sendBinary(toSend);
			} catch (IOException e) {
				if (e instanceof ClosedChannelException) {
					LOGGER.debug("Channel closed", e);
				} else {
					LOGGER.error("Problems sending value", e);
				}
				onClose(new CloseReason(CloseCodes.GOING_AWAY, ""), session);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@OnOpen
	public void onOpen(@PathParam("id") String id, @PathParam("operator") String operatorName,
			@PathParam("port") String port, @PathParam("protocol") String protocol,
			@PathParam("securityToken") String securityToken, Session session) {
		try {
			ISession odysseusSession = SessionManagement.instance.login(securityToken);
			//ISession odysseusSession = SessionManagement.instance.loginSuperUser("");

			if (odysseusSession != null) {
				synchronized (this) {
					IExecutionPlan currentPlan = ExecutorServiceBinding.getExecutor().getExecutionPlan(odysseusSession);
					final IPhysicalQuery query = getQuery(id, odysseusSession, currentPlan);

					IPhysicalOperator operatorP = query.getOperator(UUID.fromString(operatorName));
					ISource<IStreamObject<?>> operator = null;
					if (operatorP instanceof ISource) {
						operator = (ISource<IStreamObject<?>>) operatorP;
					} else {
						// exception or use inputs?
						throw new RuntimeException("Operator to connect is not available or no source");
					}

					Integer connectionPort = Integer.parseInt(port);

					// Check, if operator with this protocol and port is already bound
					if (connectedOperators.get(protocol) == null
							|| connectedOperators.get(protocol).get(operator) == null
							|| connectedOperators.get(protocol).get(operator).get(connectionPort) == null) {
						addNewConnection(protocol, connectionPort, operator);
					}
					addQueryResultReceiver(protocol, operator, connectionPort, session, odysseusSession);

				}
			} else {
				// TODO: Handle "Connection refused"
				CloseReason reason = new CloseReason(CloseCodes.CANNOT_ACCEPT, "Login failed");
				closeConnection(session, reason);
			}
		} catch (Exception e) {
			LOGGER.error("Error in onOpen", e);
		}
	}

	private IPhysicalQuery getQuery(String id, ISession odysseusSession, IExecutionPlan currentPlan) {
		Integer queryID;
		IPhysicalQuery query = null;
		try {
			queryID = Integer.parseInt(id);
			query = currentPlan.getQueryById(queryID.intValue(), odysseusSession);
		} catch (NumberFormatException e) {
		}
		if (query == null) {
			query = currentPlan.getQueryByName(new Resource(id), odysseusSession);
		}
		if (query == null) {
			// TODO:
			throw new QueryNotFoundException("No query with name or id " + id + " found.");
		}
		return query;
	}

	private void closeConnection(Session session, CloseReason reason) {
		try {
			session.close(reason);
		} catch (IOException e) {
			LOGGER.error("Error closing connection", e);
		}
	}

	private void addQueryResultReceiver(String protocol, IPhysicalOperator operator, Integer connectionPort,
			Session session, ISession odysseusSession) {
		Integer inputPort = connectedOperators.get(protocol).get(operator).get(connectionPort);
		QueryResultReceiver qrr = receiver.get(inputPort);
		if (qrr == null) {
			Class<?> type = operator.getOutputSchema().getType();
			IStreamObjectDataHandler<?> dh = ExecutorServiceBinding.getExecutor().getDataDictionary(odysseusSession)
					.getDataHandlerRegistry(odysseusSession)
					.getStreamObjectDataHandler(type.getSimpleName(), operator.getOutputSchema(connectionPort));

			qrr = new QueryResultReceiver(protocol, dh, (ISource<?>) operator, connectionPort);
			receiver.put(inputPort, qrr);
		}
		qrr.addSession(session);
		sessionToInputPortMapping.put(session, inputPort);

	}

	private synchronized void removeQueryResultReceiver(Integer inputPort, Session session) {
		QueryResultReceiver qrr = receiver.get(inputPort);
		if (qrr != null) {
			qrr.removeSession(session);
			if (qrr.noMoreReceivers()) {
				receiver.remove(inputPort);
				removeConnection(qrr);
			}
		}
		sessionToInputPortMapping.remove(session);

	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addNewConnection(String protocol, Integer connectionPort,
			ISource<IStreamObject<? extends IMetaAttribute>> operator) {

		int nextFreeInputPort = getNextFreeSinkInPort();

		// protocol, port, outputport, inputport
		// private Map<String, Map<IPhysicalOperator, Map<Integer, Integer>>>
		// connectedOperators = new HashMap<>();

		Map<IPhysicalOperator, Map<Integer, Integer>> protolSpecificMap = connectedOperators.get(protocol);
		if (protolSpecificMap == null) {
			protolSpecificMap = new HashMap<>();
			connectedOperators.put(protocol, protolSpecificMap);
		}

		Map<Integer, Integer> operatorSpecificMap = protolSpecificMap.get(operator);
		if (operatorSpecificMap == null) {
			operatorSpecificMap = new HashMap<>();
			protolSpecificMap.put(operator, operatorSpecificMap);
		}

		Integer inputPort = operatorSpecificMap.get(connectionPort);
		if (inputPort != null) {
			throw new RuntimeException("connection already established!");
		}
		operatorSpecificMap.put(connectionPort, nextFreeInputPort);

		// create new Buffer
		ThreadedBufferPO<IStreamObject<? extends IMetaAttribute>> bufferPO = new ThreadedBufferPO<>(BUFFER_LIMIT);
		bufferPO.addOwner(this);
		operator.connectSink(bufferPO, 0, connectionPort, operator.getOutputSchema(connectionPort));

		AbstractPipe<IStreamObject<?>, IStreamObject<?>> converter;

		// Default: need to convert
		converter = bufferPO;

		if (PROTOCOL_JSON.equalsIgnoreCase(protocol)) {
			if (operator.getOutputSchema(connectionPort).getType() == Tuple.class) {
				converter = new TupleToKeyValuePO();
				bufferPO.subscribeSink((ISink) converter, 0, 0, operator.getOutputSchema());
			}

			// TODO: how to handle xml cases?
		}

		// use output schema from operator because bufferPO does not have any
		converter.subscribeSink((ISink) this, nextFreeInputPort, 0, converter.getOutputSchema());
		converter.addOwner(this);
		this.addOwner(this);
		this.open(this);
	}

	private void removeConnection(QueryResultReceiver qrr) {
		// Remove only subscription to buffer
		qrr.source.disconnectSink(qrr.buffer, 0, qrr.connectionPort, qrr.source.getOutputSchema(qrr.connectionPort));			
	}

	public void onClose(CloseReason closeReason, Session session) {
		Integer inputPort = sessionToInputPortMapping.get(session);
		if (inputPort != null) {
			removeQueryResultReceiver(inputPort, session);
		}
	}

	@OnMessage
	public void onTextMessage(String text, Session session) throws IOException {
		LOGGER.info("Received Text : {} from  {}", text, session.getId()+"");
	}

	@OnError
	public void onError(Throwable throwable, Session session) {
		LOGGER.error("Error found in method : {}", throwable);
	}

	@Override
	public int compareTo(IOperatorOwner arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ISession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void done(IOwnedOperator op) {
		// TODO Auto-generated method stub

	}
	
	public static String toWebsocketUrl(ISession session, int queryid, String operator, int port, String protocol) {
		return String.format("/queries/%s/%s/%s/%s/%s",
					String.valueOf(queryid),
					operator,
					String.valueOf(port),
					protocol,
					session.getToken()
				);
	}
}

class QueryResultReceiver {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryResultReceiver.class);
	
	ThreadedBufferPO<IStreamObject<? extends IMetaAttribute>> buffer;
	final boolean useSendText;
	final boolean convertToCSV;
	final String protocol;
	final List<Session> sessions = new CopyOnWriteArrayList<>();
	final IStreamObjectDataHandler<?> dataHandler;
	final ISource<?> source;
	final int connectionPort; 

	QueryResultReceiver(String protocol, IStreamObjectDataHandler<?> dataHandler, ISource<?> op, Integer connectionPort) {
		this.useSendText = getTypeForProtocol(protocol);
		this.protocol = protocol;
		this.dataHandler = dataHandler;
		this.source = op;
		this.connectionPort = connectionPort;
		this.convertToCSV = protocol.equalsIgnoreCase("csv");
	}

	public boolean noMoreReceivers() {
		return sessions.isEmpty();
	}

	public void removeSession(Session session) {
		this.sessions.remove(session);
		LOGGER.debug("Session {} removed",session);
	}

	void addSession(Session session) {
		this.sessions.add(session);
		LOGGER.debug("Session {} added",session);
	}

	List<Session> getSessions() {
		return sessions;
	}
	
	private boolean getTypeForProtocol(String protocol) {
		// TODO: more generic
		if (QueryResultWebsocketEndpoint.PROTOCOL_JSON.equalsIgnoreCase(protocol)) {
			return true;
		}
		if (QueryResultWebsocketEndpoint.PROTOCOL_CSV.equalsIgnoreCase(protocol)) {
			return true;
		}
		return false;
	}


}
