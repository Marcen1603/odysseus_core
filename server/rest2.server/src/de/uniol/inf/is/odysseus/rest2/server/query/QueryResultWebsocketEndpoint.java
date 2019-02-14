package de.uniol.inf.is.odysseus.rest2.server.query;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryResultWebsocketEndpoint.class);
	// TODO: Config!
	private static final int BUFFER_LIMIT = 1000;
	private Map<Integer, QueryResultReceiver> receiver = new HashMap<>();
	private Map<Session, Integer> sessionToInputPortMapping = new HashMap<>();
	// protocol, port, outputport, inputport
	private Map<String, Map<IPhysicalOperator, Map<Integer, Integer>>> connectedOperators = new HashMap<>();

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO: synchronization necessary?
		QueryResultReceiver resultReceiver = receiver.get(port);
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

	@Override
	protected void process_next(IStreamObject<IMetaAttribute> object, int port) {
		// TODO: synchronization necessary?
		QueryResultReceiver resultReceiver = receiver.get(port);
		if (resultReceiver != null) {
			if (resultReceiver.useSendText) {
				String toSend = object.toString();
				sendText(resultReceiver.sessions, toSend);
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
		sessions.forEach(session -> {
			try {
				session.getBasicRemote().sendText(toSend);
			} catch (IOException e) {
				LOGGER.error("Problems sending value " + toSend, e);
				onClose(new CloseReason(CloseCodes.GOING_AWAY, ""), session);
			}
		});
	}

	private void sendBinary(List<Session> sessions, ByteBuffer toSend) {

		sessions.forEach(session -> {
			try {
				session.getBasicRemote().sendBinary(toSend);
			} catch (IOException e) {
				LOGGER.error("Problems sending value ", e);
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
					Integer queryID = null;
					IPhysicalQuery query = null;
					query = getQuery(id, odysseusSession, currentPlan);

					// TODO handle operator param
					IPhysicalOperator operatorP = query.getRoots().get(0);
					ISource<IStreamObject<?>> operator = null;
					if (operatorP instanceof ISource) {
						operator = (ISource<IStreamObject<?>>) operatorP;
					} else {
						// exception or use inputs?
						throw new RuntimeException("Operator to connect is no source");
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
		IPhysicalQuery query;
		try {
			queryID = Integer.parseInt(id);
			query = currentPlan.getQueryById(queryID.intValue(), odysseusSession);
		} catch (NumberFormatException e) {
			LOGGER.debug("No query id. Try to retrieve query by name");
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

			qrr = new QueryResultReceiver(getTypeForProtocol(protocol), protocol, dh, (ISource<?>) operator);
			receiver.put(inputPort, qrr);
		}
		qrr.addSession(session);
		sessionToInputPortMapping.put(session, inputPort);

	}

	private void removeQueryResultReceiver(Integer inputPort, Session session) {
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

	private boolean getTypeForProtocol(String protocol) {
		// TODO: more generic
		if ("JSON".equalsIgnoreCase(protocol)) {
			return true;
		}
		return false;
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

		if ("JSON".equalsIgnoreCase(protocol)) {
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
		Collection<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> sinks = qrr.source.getConnectedSinks();

		sinks.forEach(sinksubscription -> {
			sinksubscription.getSink().done(0);
			qrr.source.disconnectSink(sinksubscription.getSink(), sinksubscription.getSinkInPort(),
					sinksubscription.getSourceOutPort(), sinksubscription.getSchema());
		});

	}

	public void onClose(CloseReason closeReason, Session session) {
		Integer inputPort = sessionToInputPortMapping.get(session);
		if (inputPort != null) {
			removeQueryResultReceiver(inputPort, session);
		}
	}

	@OnMessage
	public void onTextMessage(String text, Session session) throws IOException {
		LOGGER.info("Received Text : " + text + " from  " + session.getId());
	}

	@OnError
	public void onError(Throwable throwable, Session session) {
		LOGGER.error("Error found in method : " + throwable.toString());
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
}

class QueryResultReceiver {
	public ThreadedBufferPO<IStreamObject<? extends IMetaAttribute>> buffer;
	final boolean useSendText;
	final String protocol;
	final List<Session> sessions = new LinkedList<>();
	final IStreamObjectDataHandler<?> dataHandler;
	final ISource<?> source;

	QueryResultReceiver(boolean useSendText, String protocol, IStreamObjectDataHandler<?> dataHandler, ISource<?> op) {
		this.useSendText = useSendText;
		this.protocol = protocol;
		this.dataHandler = dataHandler;
		this.source = op;
	}

	public boolean noMoreReceivers() {
		return sessions.isEmpty();
	}

	public void removeSession(Session session) {
		this.sessions.remove(session);
	}

	void addSession(Session session) {
		this.sessions.add(session);
	}

	List<Session> getSessions() {
		return sessions;
	}

}
