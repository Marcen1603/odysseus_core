package de.uniol.inf.is.odysseus.peer.smarthome;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class SmartDeviceMessage implements IMessage {

	private String text;
	
	public SmartDeviceMessage() {
	}
	
	public SmartDeviceMessage(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public byte[] toBytes() {
		byte[] textBytes = text.getBytes();
		byte[] data = new byte[1 + textBytes.length];
		System.arraycopy(textBytes, 0, data, 1, textBytes.length);
		return data;
	}

	@Override
	public void fromBytes(byte[] data) {
		byte[] textBytes = new byte[data.length - 1];
		System.arraycopy(data, 1, textBytes, 0, textBytes.length);
		
		text = new String(textBytes);
	}

}
