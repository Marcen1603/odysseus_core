package de.uniol.inf.is.odysseus.p2p.peer.communication;


/**
 * Base Interface for Message Handling
 * @author Christian Zillmann, Mart Koehler, Marco Grawunder
 *
 */

public interface IMessageHandler {
	void handleMessage(Object msg, String namespace);
	String getInterestedNamespace();
//	void setInterestedNamespace(String namespace);
}
