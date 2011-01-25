package de.uniol.inf.is.odysseus.p2p.peer.communication;


abstract public class AbstractMessageHandler implements IMessageHandler {

	String interestedNamespace = null; 

	@Override
	final public String getInterestedNamespace() {
		return interestedNamespace;
	}

	final protected void setInterestedNamespace(String namespace) {
		this.interestedNamespace = namespace;
	}
	
}
