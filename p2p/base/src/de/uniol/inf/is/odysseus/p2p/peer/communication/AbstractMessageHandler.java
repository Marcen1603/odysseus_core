package de.uniol.inf.is.odysseus.p2p.peer.communication;


abstract public class AbstractMessageHandler implements IMessageHandler {

	String interestedNamespace = null; 

	public AbstractMessageHandler(String interestedNamespace) {
		this.interestedNamespace = interestedNamespace;
	}
	
	@Override
	final public String getInterestedNamespace() {
		return interestedNamespace;
	}
	
}
