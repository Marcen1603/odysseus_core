package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.utils;


public class SmartDeviceConfigurationRequestMessage extends SmartDeviceConfigurationMessage {

	private String text;
	
	public SmartDeviceConfigurationRequestMessage() {
	}
	
	public SmartDeviceConfigurationRequestMessage(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public byte[] toBytes() {
		byte[] textBytes = text.getBytes();
		byte[] data = new byte[1 + textBytes.length];
		System.arraycopy(textBytes, 0, data, 1, textBytes.length);
		return data;
	}

	public void fromBytes(byte[] data) {
		byte[] textBytes = new byte[data.length - 1];
		System.arraycopy(data, 1, textBytes, 0, textBytes.length);
		
		text = new String(textBytes);
	}
}
