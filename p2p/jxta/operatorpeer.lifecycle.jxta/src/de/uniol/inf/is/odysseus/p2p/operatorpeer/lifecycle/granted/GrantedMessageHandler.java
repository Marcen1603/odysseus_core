/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.granted;

import java.util.HashMap;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.AbstractJxtaMessageHandler;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageSender;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public class GrantedMessageHandler extends AbstractJxtaMessageHandler {

	static Logger logger = LoggerFactory
			.getLogger(GrantedExecutionHandler.class);

	private P2PQuery query = null;
	private IExecutionListenerCallback callback;
	@SuppressWarnings("rawtypes")
	private IMessageSender messageSender;
	private boolean granted = false;

	public GrantedMessageHandler(IExecutionListenerCallback callback,
			String namespace, IMessageSender<?, ?, ?> sender, ILogListener log) {
		super(log, namespace);
		this.setQuery(callback.getQuery());
		this.callback = callback;
		this.messageSender = sender;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Object msg, String namespace) {
		this.granted = true;
		String query = meas(namespace, "queryId", (Message) msg);
		String subplanId = meas(namespace, "subplanId", (Message) msg);
		PipeAdvertisement pipeAdv = MessageTool.createResponsePipeFromMessage(namespace,
				(Message) msg, 0);
		logger.debug("Erhalte im MessageHandler querid " + query
				+ " subplanid " + subplanId);
		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		// Sende Anfrage an die bestaetigten Peers
		messageElements.put("queryId", query);
		messageElements.put("subplanId", subplanId);
		logger.debug("Sende Granted Antwort zu Teilplan " + subplanId);
		this.messageSender.sendMessage(PeerGroupTool.getPeerGroup(),
				MessageTool.createSimpleMessage("Granted" + query,
						messageElements), pipeAdv, 10);
	}

	public IExecutionListenerCallback getCallback() {
		return callback;
	}

	public void setQuery(P2PQuery query) {
		this.query = query;
	}

	public P2PQuery getQuery() {
		return query;
	}

	public boolean isGranted() {
		return granted;
	}

}
