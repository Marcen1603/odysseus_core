package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.communication;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class RequestContractMessage implements IMessage {

	
	public RequestContractMessage() {
		//no implementation needed.
	}
	
	
	@Override
	public byte[] toBytes() {
		//no implementation needed.
		return new byte[0];
	}

	@Override
	public void fromBytes(byte[] data) {
		//no implementation needed.
		
	}

}
