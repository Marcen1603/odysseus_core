/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package de.uniol.inf.is.odysseus.rest2.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.msf4j.websocket.WebSocketEndpoint;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * This is a Sample class for WebSocket. This provides a chat with multiple
 * users.
 */

@ServerEndpoint(value = "/chat/{name}/{sessionid}")
public class ChatAppEndpoint implements WebSocketEndpoint {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChatAppEndpoint.class);
	private List<Session> sessions = new LinkedList<>();
	private Map<Session, ISession> sessionMap = new HashMap<>();

	@OnOpen
	public void onOpen(@PathParam("name") String name, @PathParam("sessionid") String securityToken, Session session) {
		// check login
		ISession odysseusSession = SessionManagement.instance.login(securityToken);
		if (odysseusSession != null) {
			sessions.add(session);
			sessionMap.put(session, odysseusSession);
			String msg = name + " connected to chat";
			LOGGER.info(msg);
			sendMessageToAll(msg);
		} else {
			sendMessage(session, "Connection refused");
		}
	}

	private void sendMessage(Session session, String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			LOGGER.error("Error", e);
		}
	}

	@OnMessage
	public void onTextMessage(@PathParam("name") String name, String text, Session session) throws IOException {
		if (sessionMap.get(session) != null) {
			String msg = name + " : " + text;
			LOGGER.info("Received Text : " + text + " from  " + name + session.getId());
			sendMessageToAll(msg);
		}else {
			sendMessage(session, "Connection refused");			
		}
	}

	@OnMessage
	public void onBinaryMessage(byte[] bytes, Session session) {
		LOGGER.info("Reading binary Message");
		LOGGER.info(bytes.toString());
	}

	@OnClose
	public void onClose(@PathParam("name") String name, CloseReason closeReason, Session session) {
		LOGGER.info("Connection is closed with status code : " + closeReason.getCloseCode().getCode() + " On reason "
				+ closeReason.getReasonPhrase());
		sessions.remove(session);
		sessionMap.remove(session);
		String msg = name + " left the chat";
		sendMessageToAll(msg);
	}

	@OnError
	public void onError(Throwable throwable, Session session) {
		LOGGER.error("Error found in method : " + throwable.toString());
	}

	private void sendMessageToAll(String message) {
		sessions.forEach(session -> {
			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				LOGGER.error(e.toString());
			}
		});
	}

}