package de.uniol.inf.is.odysseus.rest2.server.query;

import java.io.IOException;
import java.nio.ByteBuffer;
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
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
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
		// TODO Auto-generated method stub

	}

	@Override
	protected void process_next(IStreamObject<IMetaAttribute> object, int port) {
		// TODO: synchronization necessary?
		QueryResultReceiver resultReceiver = receiver.get(port);
		if (resultReceiver.useSendText) {
			String toSend = object.toString();
			resultReceiver.sessions.forEach(session -> {
				try {
					session.getBasicRemote().sendText(toSend);
				} catch (IOException e) {
					LOGGER.error("Problems sending value " + object, e);
				}
			});
			
		} else {
			// TODO: Add converter
			ByteBuffer toSend = ByteBuffer.allocate(1);
			resultReceiver.sessions.forEach(session -> {
				try {
					session.getBasicRemote().sendBinary(toSend);
				} catch (IOException e) {
					LOGGER.error("Problems sending value " + object, e);
				}
			});
		}
	}

	@SuppressWarnings("unchecked")
	@OnOpen
	public void onOpen(@PathParam("id") String id, @PathParam("operator") String operatorName,
			@PathParam("port") String port, @PathParam("protocol") String protocol,
			@PathParam("securityToken") String securityToken, Session session) {
		// check login
		try {
			ISession odysseusSession = SessionManagement.instance.login(securityToken);
			//ISession odysseusSession = SessionManagement.instance.loginSuperUser("");
			
			if (odysseusSession != null) {
				synchronized (this) {
					IExecutionPlan currentPlan = ExecutorServiceBinding.getExecutor().getExecutionPlan(odysseusSession);
					Integer queryID = null;
					IPhysicalQuery query = null;
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
					// TODO handle operator param
					IPhysicalOperator operatorP = query.getRoots().get(0);
					ISource<IStreamObject<?>> operator=null;
					if (operatorP instanceof ISource) {
						operator = (ISource<IStreamObject<?>>) operatorP;
					}else {
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
					addQueryResultReceiver(protocol, operator, connectionPort, session);

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

	private void closeConnection(Session session, CloseReason reason) {
		try {
			session.close(reason);
		} catch (IOException e) {
			LOGGER.error("Error closing connection", e);
		}
	}

	private void addQueryResultReceiver(String protocol, IPhysicalOperator operator, Integer connectionPort,
			Session session) {
		Integer inputPort = connectedOperators.get(protocol).get(operator).get(connectionPort);
		QueryResultReceiver qrr = receiver.get(inputPort);
		if (qrr == null) {
			qrr = new QueryResultReceiver(getTypeForProtocol(protocol));
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

	private void addNewConnection(String protocol, Integer connectionPort, ISource<IStreamObject<? extends IMetaAttribute>> operator) {

		int nextFreeInputPort = getNextFreeSinkInPort();
		
		// protocol, port, outputport, inputport
		// private Map<String, Map<IPhysicalOperator, Map<Integer, Integer>>> connectedOperators = new HashMap<>();

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
		operator.connectSink(bufferPO, 0, connectionPort, operator.getOutputSchema());

		AbstractPipe<IStreamObject<?>, IStreamObject<?>> converter;
		
		if ("JSON".equalsIgnoreCase(protocol)) {
			converter = new TupleToKeyValuePO();
		}else {
			throw new RuntimeException("Only JSON supported at the moment");
		}
		
		// use output schema from operator because bufferPO does not have any 
		bufferPO.subscribeSink((ISink)converter, 0,0,operator.getOutputSchema());
		
		converter.subscribeSink((ISink)this, nextFreeInputPort, 0, converter.getOutputSchema());
		converter.addOwner(this);
		this.addOwner(this);
		this.open(this);
	}

	private void removeConnection(QueryResultReceiver qrr) {
		// TODO Auto-generated method stub

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
	final boolean useSendText;
	final List<Session> sessions = new LinkedList<>();

	QueryResultReceiver(boolean useSendText) {
		this.useSendText = useSendText;
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
