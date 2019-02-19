package de.uniol.inf.is.odysseus.rest2.server.events;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.server.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.queryadded.IQueryAddedListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.QueryAddedEvent;
import de.uniol.inf.is.odysseus.rest2.server.ExecutorServiceBinding;

/**
 * Websocket to receive messages when a server event occurs. This could, for
 * example, be a new query.
 * 
 * @author Tobias Brandt
 *
 */
@ServerEndpoint(value = "/server/updateevents/{type}/{securityToken}")
public class ServerEventsWebsocketEndpoint implements WebSocketEndpoint, IUpdateEventListener, IQueryAddedListener,
		IErrorEventListener, IPlanModificationListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerEventsWebsocketEndpoint.class);
	private final Gson gson = new Gson();

	// type to sessions
	private Map<ServerEventType, List<Session>> typeListeners = new HashMap<>();

	@OnOpen
	public void onOpen(@PathParam("type") String type, @PathParam("securityToken") String securityToken,
			Session session) {
		try {
			ISession odysseusSession = SessionManagement.instance.login(securityToken);
			if (odysseusSession != null) {
				// Add this class as an EventListener to the type
				subscribeToEvents(type, session, odysseusSession);
			} else {
				// TODO: Handle "Connection refused"
				CloseReason reason = new CloseReason(CloseCodes.CANNOT_ACCEPT, "Login failed");
				closeConnection(session, reason);
			}
		} catch (Exception e) {
			LOGGER.error("Error in onOpen", e);
		}
	}

	private void subscribeToEvents(String type, Session session, ISession odysseusSession) {
		IServerExecutor executor = ExecutorServiceBinding.getExecutor();
		ServerEventType enumType = ServerEventType.valueOf(type);
		switch (enumType) {
		case QUERY_ADDED:
			executor.addQueryAddedListener(this);
			break;
		case ERROR_EVENT:
			executor.addErrorEventListener(this);
			break;
		case PLAN_MODIFICATION:
			executor.addPlanModificationListener(this);
			break;
		case SESSION:
		case DATADICTIONARY:
		case USER:
		case QUERY:
		case SCHEDULING:
			executor.addUpdateEventListener(this, type, odysseusSession);
			break;
		default:
			break;
		}

		this.addToListeners(enumType, session);
	}

	/**
	 * This session is interested in this type of updates
	 */
	private void addToListeners(ServerEventType type, Session session) {
		if (this.typeListeners.get(type) == null) {
			List<Session> sessions = new ArrayList<Session>();
			this.typeListeners.put(type, sessions);
		}

		if (!this.typeListeners.get(type).contains(session)) {
			this.typeListeners.get(type).add(session);
		}
	}

	private void closeConnection(Session session, CloseReason reason) {
		try {
			session.close(reason);
			onClose(reason, session);
		} catch (IOException e) {
			LOGGER.error("Error closing connection", e);
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

	/**
	 * Sends the text string to the receivers
	 * 
	 * @param sessions Sessions that should receive the text
	 * @param toSend   The text (JSON) to send to the receivers
	 */
	private void sendText(List<Session> sessions, String toSend) {
		// Prevent a ConcurrentModificationException when onClose modifies the
		// session-list
		List<Session> sessionsCopy = new ArrayList<>(sessions);
		sessionsCopy.forEach(session -> {
			try {
				session.getBasicRemote().sendText(toSend);
			} catch (IOException e) {
				LOGGER.error("Problems sending value " + toSend, e);
				if (e instanceof ClosedChannelException) {
					LOGGER.debug("Channel closed", e);
				} else {
					LOGGER.error("Problems sending value " + toSend, e);
				}
				onClose(new CloseReason(CloseCodes.GOING_AWAY, ""), session);
			}
		});
	}

	public void onClose(CloseReason closeReason, Session session) {
		// Remove all subscriptions from this session
		List<ServerEventType> keysToRemove = new ArrayList<>();
		for (ServerEventType key : this.typeListeners.keySet()) {
			List<Session> sessions = this.typeListeners.get(key);
			if (sessions.contains(session)) {
				sessions.remove(session);
			}
			if (sessions.isEmpty()) {
				keysToRemove.add(key);
			}
		}
		for (ServerEventType type : keysToRemove) {
			unsubscribeFromType(type);
			typeListeners.remove(type);
		}
	}

	private void unsubscribeFromType(ServerEventType type) {
		/*
		 * We cannot unsubscribe without a session, but we cannot force the client to
		 * give us the securityToken again when closing the connection. Use the
		 * superuser instead.
		 */
		ISession odysseusSession = SessionManagement.instance.loginSuperUser(null);
		IServerExecutor executor = ExecutorServiceBinding.getExecutor();

		switch (type) {
		case QUERY_ADDED:
			executor.removeQueryAddedListener(this);
			break;
		case ERROR_EVENT:
			executor.removeErrorEventListener(this);
			break;
		case PLAN_MODIFICATION:
			executor.removePlanModificationListener(this);
			break;
		case SESSION:
		case DATADICTIONARY:
		case USER:
		case QUERY:
		case SCHEDULING:
			executor.removeUpdateEventListener(this, type.name(), odysseusSession);
			break;
		default:
			break;
		}
	}

	@Override
	public void eventOccured(String type) {
		List<Session> list = this.typeListeners.get(ServerEventType.valueOf(type));
		if (list == null) {
			// No one is interested in this event (should not occur)
			return;
		}
		sendText(list, type);
	}

	@Override
	public void queryAddedEvent(String query, List<Integer> queryIds, QueryBuildConfiguration buildConfig,
			String parserID, ISession user, Context context) {
		QueryAddedEvent event = new QueryAddedEvent(query, queryIds, parserID);
		String asJson = gson.toJson(event);
		List<Session> sessions = this.typeListeners.get(ServerEventType.QUERY_ADDED);
		sendText(sessions, asJson);
	}

	@Override
	public void errorEventOccured(ErrorEvent eventArgs) {
		String asJson = gson.toJson(eventArgs);
		List<Session> sessions = this.typeListeners.get(ServerEventType.ERROR_EVENT);
		sendText(sessions, asJson);
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		String asJson = gson.toJson(eventArgs);
		List<Session> sessions = this.typeListeners.get(ServerEventType.PLAN_MODIFICATION);
		sendText(sessions, asJson);
	}

}
