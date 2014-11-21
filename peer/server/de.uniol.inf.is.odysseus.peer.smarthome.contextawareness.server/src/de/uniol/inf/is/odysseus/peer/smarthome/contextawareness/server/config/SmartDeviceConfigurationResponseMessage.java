package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.config;

import java.io.IOException;

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
	
	public byte[] toBytes() {
		try {
			return serialize(smartDeviceConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void fromBytes(byte[] data) {
		try {
			this.smartDeviceConfig = (SmartDeviceConfig) deserialize(data);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setSmartDeviceConfig(SmartDeviceConfig _smartDeviceConfig) {
		smartDeviceConfig = _smartDeviceConfig;
	}
	
	public SmartDeviceConfig getSmartDeviceConfig(){
		return smartDeviceConfig;
	}
}
