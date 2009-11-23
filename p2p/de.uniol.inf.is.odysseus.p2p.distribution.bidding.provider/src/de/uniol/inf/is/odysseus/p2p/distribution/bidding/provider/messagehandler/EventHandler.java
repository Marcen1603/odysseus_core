package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.messagehandler;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;

public class EventHandler implements IMessageHandler {

	@Override
	public String getInterestedNamespace() {
		return "ProviderEvents";
	}

	@Override
	public void handleMessage(Object msg, String namespace) {
		String event = MessageTool.getMessageElementAsString(
				namespace, "event", (Message)msg);
		String queryId = MessageTool.getMessageElementAsString(
				namespace, "geryId", (Message)msg);
		Log.addEvent(queryId, event);
	}

}
