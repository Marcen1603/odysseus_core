package de.uniol.inf.is.odysseus.rest2.server.query;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.websocket.CloseReason;
import javax.websocket.OnError;
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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.server.ExecutorServiceBinding;

@ServerEndpoint(value = "/queries/{id}/{operator}/{port}/{protocol}/{securityToken}")
public class QueryResultWebsocketEndpoint extends AbstractSink<IStreamObject<IMetaAttribute>>
		implements WebSocketEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryResultWebsocketEndpoint.class);
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
					LOGGER.error("Problems sending value "+object,e);
				}
			});
		}else {
			// TODO: Add converter
			ByteBuffer toSend = ByteBuffer.allocate(1);
			resultReceiver.sessions.forEach(session -> {
				try {
					session.getBasicRemote().sendBinary(toSend);
				} catch (IOException e) {
					LOGGER.error("Problems sending value "+object,e);
				}
			});
		}
	}

	@OnOpen
	public void onOpen(@PathParam("id") String id, @PathParam("operator") String operatorName,
			@PathParam("port") String port, @PathParam("protocol") String protocol,
			@PathParam("securityToken") String securityToken, Session session) {
		// check login
		ISession odysseusSession = SessionManagement.instance.login(securityToken);

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
					// throw new QueryNotFoundException("No query with name or id " + id + "
					// found.");
					return;
				}
				// TODO handle operator param
				IPhysicalOperator operator = query.getRoots().get(0);
				Integer connectionPort = Integer.parseInt(port);

				// Check, if operator with this protocol and port is already bound
				if (connectedOperators.get(protocol) == null || connectedOperators.get(protocol).get(operator) == null
						|| connectedOperators.get(protocol).get(operator).get(connectionPort) == null) {
					addNewConnection(protocol, connectionPort, operator);
				}
				addQueryResultReceiver(connectedOperators.get(protocol).get(operator).get(connectionPort));

			}
		} else {
			// TODO: Handle "Connection refused"
		}
	}

	private void addQueryResultReceiver(Integer integer) {
		// TODO Auto-generated method stub

	}

	private void addNewConnection(String protocol, Integer connectionPort, IPhysicalOperator operator) {
		// TODO Auto-generated method stub

	}

	public void onClose(@PathParam("name") String name, CloseReason closeReason, Session session) {
	}

	@OnError
	public void onError(Throwable throwable, Session session) {
		LOGGER.error("Error found in method : " + throwable.toString());
	}
}

class QueryResultReceiver {
	public boolean useSendText = true;
	public List<Session> sessions = new LinkedList<>();
}
