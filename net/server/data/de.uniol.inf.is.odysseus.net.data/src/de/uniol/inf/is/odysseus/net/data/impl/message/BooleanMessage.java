package de.uniol.inf.is.odysseus.net.data.impl.message;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class BooleanMessage implements IMessage {

	private boolean value;
	
	public BooleanMessage() {
		
	}
	
	public BooleanMessage( boolean value ) {
		this.value = value;
	}
	
	@Override
	public byte[] toBytes() {
		byte[] bytes = new byte[1];
		bytes[0] = (value ? (byte)1 : (byte)0);
		return bytes;
	}

	@Override
	public void fromBytes(byte[] data) {
		value = (data[0] == 1);
	}
	
	public boolean getValue() {
		return value;
	}
}
