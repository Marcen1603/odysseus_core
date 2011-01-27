package de.uniol.inf.is.odysseus.p2p.jxta.peer.communication;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.peer.communication.AbstractMessageHandler;

abstract public class AbstractJxtaMessageHandler extends AbstractMessageHandler {

	protected ILogListener log;

	public AbstractJxtaMessageHandler(ILogListener log, String interestedNamespace){
		super(interestedNamespace);
		this.log = log;
	}
	
	protected String meas(String namespace, String element, Message msg) {
		return MessageTool.getMessageElementAsString(namespace, element, msg);
	}
	
}
