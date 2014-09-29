package de.uniol.inf.is.odysseus.peer.smarthome;

// implements IMessage
// extends SmartDeviceConfigurationMessage
public class SmartDeviceConfigurationResponseMessage extends SmartDeviceConfigurationMessage {
	private SmartDeviceConfig smartDeviceConfig;
	
	private String text;
	
	public SmartDeviceConfigurationResponseMessage() {
	}
	
	public SmartDeviceConfigurationResponseMessage(String text) {
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

	public void setSmartDeviceConfig(SmartDeviceConfig _smartDeviceConfig) {
		smartDeviceConfig = _smartDeviceConfig;
	}
	
	public SmartDeviceConfig getSmartDeviceConfig(){
		return smartDeviceConfig;
	}

}
