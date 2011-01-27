package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.messagehandler;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.AbstractJxtaMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;

public class EventMessageHandler extends AbstractJxtaMessageHandler {

	public EventMessageHandler(ILogListener log) {
		super(log,"ProviderEvents");
	}

	@Override
	public void handleMessage(Object msg, String namespace) {
		String event = meas(namespace, "event", (Message) msg);
		String queryId = meas(namespace, "queryId", (Message) msg);
		log.addEvent(queryId, event);
	}

}
