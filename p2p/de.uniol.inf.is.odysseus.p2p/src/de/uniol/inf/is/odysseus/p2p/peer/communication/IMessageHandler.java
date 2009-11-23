package de.uniol.inf.is.odysseus.p2p.peer.communication;

import java.util.Map;

public interface IMessageHandler {
	void handleMessage(Object msg, String namespace);
	String getInterestedNamespace();
}
