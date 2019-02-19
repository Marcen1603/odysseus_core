package de.uniol.inf.is.odysseus.rest2.server.events;

import java.io.IOException;
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

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.ServerEvent;
import de.uniol.inf.is.odysseus.rest2.server.ExecutorServiceBinding;

/**
 * Websocket to receive messages when a server event occurs. This could, for
 * example, be a new query.
 * 
 * @author Tobias Brandt
 *
 */
@ServerEndpoint(value = "/server/updateevents/{type}/{securityToken}")
public class ServerEventsWebsocketEndpoint implements WebSocketEndpoint, IUpdateEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerEventsWebsocketEndpoint.class);
	private final Gson gson = new Gson();

	// type to sessions
	private Map<String, List<Session>> typeListeners = new HashMap<String, List<Session>>();

	@OnOpen
	public void onOpen(@PathParam("type") String type, @PathParam("securityToken") String securityToken,
			Session session) {
		try {
			ISession odysseusSession = SessionManagement.instance.login(securityToken);
			if (odysseusSession != null) {
				IServerExecutor executor = ExecutorServiceBinding.getExecutor();

				// Add this class as an EventListener to the type
				executor.addUpdateEventListener(this, type, odysseusSession);
				this.addToListeners(type, session);
			} else {
				// TODO: Handle "Connection refused"
				CloseReason reason = new CloseReason(CloseCodes.CANNOT_ACCEPT, "Login failed");
				closeConnection(session, reason);
			}
		} catch (Exception e) {
			LOGGER.error("Error in onOpen", e);
		}
	}

	/**
	 * This session is interested in this type of updates
	 */
	private void addToListeners(String type, Session session) {
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

	@Override
	public void eventOccured(String type) {
		List<Session> list = this.typeListeners.get(type);
		if (list == null) {
			// No one is interested in this event (should not occur)
			return;
		}
		sendText(list, type);
	}

	private void sendText(List<Session> sessions, String toSend) {
		ServerEvent event = new ServerEvent(toSend);
		String asJson = gson.toJson(event);
		sessions.forEach(session -> {
			try {
				session.getBasicRemote().sendText(asJson);
			} catch (IOException e) {
				LOGGER.error("Problems sending value " + toSend, e);
				onClose(new CloseReason(CloseCodes.GOING_AWAY, ""), session);
			}
		});
	}

	public void onClose(CloseReason closeReason, Session session) {
		// Remove all subscriptions from this session
		List<String> keysToRemove = new ArrayList<String>();
		for (String key : this.typeListeners.keySet()) {
			List<Session> sessions = this.typeListeners.get(key);
			if (sessions.contains(session)) {
				sessions.remove(session);
			}
			if (sessions.isEmpty()) {
				keysToRemove.add(key);
			}
		}
		for (String type : keysToRemove) {
			unsubscribeFromType(type);
			typeListeners.remove(type);
		}
	}

	private void unsubscribeFromType(String type) {
		/*
		 * We cannot unsubscribe without a session, but we cannot force the client to
		 * give us the securityToken again when closing the connection. Use the
		 * superuser instead.
		 */
		ISession odysseusSession = SessionManagement.instance.loginSuperUser(null);
		IServerExecutor executor = ExecutorServiceBinding.getExecutor();
		executor.removeUpdateEventListener(this, type, odysseusSession);
	}

}
