package de.uniol.inf.is.odysseus.net.resource.impl;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class AskUsageMessage implements IMessage {

	private boolean force;
	
	private static final byte[] NO_FORCE_BYTES = new byte[] {0};
	private static final byte[] FORCE_BYTES = new byte[] {1};
	
	public AskUsageMessage() {
		
	}
	
	public AskUsageMessage(boolean force) {
		this.force = force;
	}
	
	@Override
	public byte[] toBytes() {
		return force ? FORCE_BYTES : NO_FORCE_BYTES;
	}

	@Override
	public void fromBytes(byte[] data) {
		if( data.length != 1 ) {
			force = false;
		} else {
			force = (data[0] == 1);
		}
	}
	
	public boolean isForce() {
		return force;
	}

}
