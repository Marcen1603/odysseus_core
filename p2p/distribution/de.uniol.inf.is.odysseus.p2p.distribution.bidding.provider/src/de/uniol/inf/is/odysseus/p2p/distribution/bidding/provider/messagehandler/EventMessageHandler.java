package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.messagehandler;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.communication.AbstractMessageHandler;

public class EventMessageHandler extends AbstractMessageHandler {

	public EventMessageHandler() {
		setInterestedNamespace("ProviderEvents");
	}

	@Override
	public void handleMessage(Object msg, String namespace) {
		String event = MessageTool.getMessageElementAsString(
				namespace, "event", (Message)msg);
		String queryId = MessageTool.getMessageElementAsString(
				namespace, "queryId", (Message)msg);
		Log.addEvent(queryId, event);
	}
	
}
