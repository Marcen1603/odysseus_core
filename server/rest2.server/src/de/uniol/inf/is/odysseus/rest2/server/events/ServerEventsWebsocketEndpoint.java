package de.uniol.inf.is.odysseus.rest2.server.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.msf4j.websocket.WebSocketEndpoint;
import org.wso2.transport.http.netty.contract.websocket.WebSocketConnection;

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.server.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompilerListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.executorcommand.IExecutorCommandListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.event.AbstractPlanExecutionEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.queryadded.IQueryAddedListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.events.CompilerEvent;
import de.uniol.inf.is.odysseus.rest2.common.model.events.ExecutorCommandEvent;
import de.uniol.inf.is.odysseus.rest2.common.model.events.QueryAddedEvent;
import de.uniol.inf.is.odysseus.rest2.common.model.events.ServerEvent;
import de.uniol.inf.is.odysseus.rest2.server.ExecutorServiceBinding;

/**
 * Websocket to receive messages when a server event occurs. This could, for
 * example, be a new query.
 * 
 * @author Tobias Brandt
 *
 */
@ServerEndpoint(value = "/services/events/{type}/{securityToken}")
public class ServerEventsWebsocketEndpoint
		implements WebSocketEndpoint, IUpdateEventListener, IQueryAddedListener, IErrorEventListener,
		IPlanModificationListener, IPlanExecutionListener, IExecutorCommandListener, ICompilerListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerEventsWebsocketEndpoint.class);
	private final Gson gson = new Gson();
	public static final String SERVER_ENDPOINT_URI = "/services/events/{type}/{securityToken}";

	// type to sessions
	private Map<ServerEventType, List<WebSocketConnection>> typeListeners = new HashMap<>();

	@OnOpen
	public void onOpen(@PathParam("type") String type, @PathParam("securityToken") String securityToken,
			WebSocketConnection session) {
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

	private void subscribeToEvents(String type, WebSocketConnection session, ISession odysseusSession) {
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
		case SCHEDULER_MANAGER:
			executor.addPlanExecutionListener(this);
			break;
		case EXECUTOR_COMMAND:
			executor.addExecutorCommandListener(this);
			break;
		case COMPILER:
			executor.addCompilerListener(this, odysseusSession);
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
	private void addToListeners(ServerEventType type, WebSocketConnection session) {
		if (this.typeListeners.get(type) == null) {
			List<WebSocketConnection> sessions = new ArrayList<>();
			this.typeListeners.put(type, sessions);
		}

		if (!this.typeListeners.get(type).contains(session)) {
			this.typeListeners.get(type).add(session);
		}
	}

	private void closeConnection(WebSocketConnection session, CloseReason reason) {
		session.terminateConnection();
	}

	@OnMessage
	public void onTextMessage(String text, WebSocketConnection session) throws IOException {
		LOGGER.info("Received Text : " + text + " from  " + session.getChannelId());
	}

	@OnError
	public void onError(Throwable throwable, WebSocketConnection session) {
		LOGGER.error("Error found in method : " + throwable.toString());
	}

	/**
	 * Sends the text string to the receivers
	 * 
	 * @param sessions Sessions that should receive the text
	 * @param toSend   The text (JSON) to send to the receivers
	 */
	private void sendText(List<WebSocketConnection> sessions, String toSend) {
		// Prevent a ConcurrentModificationException when onClose modifies the
		// session-list
		List<WebSocketConnection> sessionsCopy = new ArrayList<>(sessions);
		sessionsCopy.forEach(session -> session.pushText(toSend));
	}

	public void onClose(CloseReason closeReason, WebSocketConnection session) {
		// Remove all subscriptions from this session
		List<ServerEventType> keysToRemove = new ArrayList<>();
		for (ServerEventType key : this.typeListeners.keySet()) {
			List<WebSocketConnection> sessions = this.typeListeners.get(key);
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
		case SCHEDULER_MANAGER:
			executor.removePlanExecutionListener(this);
			break;
		case EXECUTOR_COMMAND:
			executor.removeExecutorCommandListener(this);
			break;
		case COMPILER:
			// Currently, it is not possible to remove me as a listener
			break;
		case SESSION:
		case DATADICTIONARY:
		case USER:
		case QUERY:
		case SCHEDULING:
			/*
			 * We cannot unsubscribe without a session, but we cannot force the client to
			 * give us the securityToken again when closing the connection. Use the
			 * superuser instead.
			 */
			ISession odysseusSession = SessionManagement.instance.loginSuperUser(null);
			executor.removeUpdateEventListener(this, type.name(), odysseusSession);
			break;
		default:
			break;
		}
	}

	@Override
	public void eventOccured(String type) {
		List<WebSocketConnection> sessions = this.typeListeners.get(ServerEventType.valueOf(type));
		if (sessions == null) {
			// No one is interested in this event (should not occur)
			return;
		}
		ServerEvent event = new ServerEvent(type, "", "");
		String asJson = gson.toJson(event);
		CompletableFuture.runAsync(() -> sendText(sessions, asJson));
	}

	@Override
	public void queryAddedEvent(String query, List<Integer> queryIds, QueryBuildConfiguration buildConfig,
			String parserID, ISession user, Context context) {
		QueryAddedEvent event = new QueryAddedEvent(query, queryIds, parserID);
		String asJson = gson.toJson(event);
		List<WebSocketConnection> sessions = this.typeListeners.get(ServerEventType.QUERY_ADDED);
		CompletableFuture.runAsync(() -> sendText(sessions, asJson));
	}

	@Override
	public void errorEventOccured(ErrorEvent eventArgs) {
		ServerEvent event = new ServerEvent(eventArgs.getEventType().toString(), eventArgs.getValue().toString(),
				eventArgs.getMessage());
		String asJson = gson.toJson(event);
		List<WebSocketConnection> sessions = this.typeListeners.get(ServerEventType.ERROR_EVENT);
		CompletableFuture.runAsync(() -> sendText(sessions, asJson));
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		ServerEvent event = new ServerEvent(eventArgs.getEventType().toString(), eventArgs.getValue().toString(), "");
		String asJson = gson.toJson(event);
		List<WebSocketConnection> sessions = this.typeListeners.get(ServerEventType.PLAN_MODIFICATION);
		CompletableFuture.runAsync(() -> sendText(sessions, asJson));
	}

	@Override
	public void planExecutionEvent(AbstractPlanExecutionEvent<?> eventArgs) {
		ServerEvent event = new ServerEvent(eventArgs.getEventType().toString(), eventArgs.getValue().toString(), "");
		String asJson = gson.toJson(event);
		List<WebSocketConnection> sessions = this.typeListeners.get(ServerEventType.SCHEDULER_MANAGER);
		CompletableFuture.runAsync(() -> sendText(sessions, asJson));
	}

	@Override
	public void executorCommandEvent(IExecutorCommand command) {
		Collection<Integer> createdQueryIds = command.getCreatedQueryIds();
		ExecutorCommandEvent event = new ExecutorCommandEvent(ServerEventType.EXECUTOR_COMMAND.toString(),
				createdQueryIds);
		String asJson = gson.toJson(event);
		List<WebSocketConnection> sessions = this.typeListeners.get(ServerEventType.EXECUTOR_COMMAND);
		CompletableFuture.runAsync(() -> sendText(sessions, asJson));
	}

	@Override
	public void parserBound(String parserID) {
		CompilerEvent event = new CompilerEvent(ServerEventType.COMPILER.toString(), "parserBound", parserID);
		String asJson = gson.toJson(event);
		List<WebSocketConnection> sessions = this.typeListeners.get(ServerEventType.COMPILER);
		CompletableFuture.runAsync(() -> sendText(sessions, asJson));
	}

	@Override
	public void rewriteBound() {
		CompilerEvent event = new CompilerEvent(ServerEventType.COMPILER.toString(), "rewriteBound", "");
		String asJson = gson.toJson(event);
		List<WebSocketConnection> sessions = this.typeListeners.get(ServerEventType.COMPILER);
		CompletableFuture.runAsync(() -> sendText(sessions, asJson));
	}

	@Override
	public void transformationBound() {
		CompilerEvent event = new CompilerEvent(ServerEventType.COMPILER.toString(), "transformationBound", "");
		String asJson = gson.toJson(event);
		List<WebSocketConnection> sessions = this.typeListeners.get(ServerEventType.COMPILER);
		CompletableFuture.runAsync(() -> sendText(sessions, asJson));
	}

	@Override
	public void planGeneratorBound() {
		CompilerEvent event = new CompilerEvent(ServerEventType.COMPILER.toString(), "planGeneratorBound", "");
		String asJson = gson.toJson(event);
		List<WebSocketConnection> sessions = this.typeListeners.get(ServerEventType.COMPILER);
		CompletableFuture.runAsync(() -> sendText(sessions, asJson));
	}
}
