package de.uniol.inf.is.odysseus.peer.smarthome;

public class SmartDeviceConfigurationResponseMessage extends
		SmartDeviceConfigurationMessage {
	private SmartDeviceConfig smartDeviceConfig;
	
	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fromBytes(byte[] data) {
		// TODO Auto-generated method stub
		
	}

	public void setSmartDeviceConfig(SmartDeviceConfig _smartDeviceConfig) {
		smartDeviceConfig = _smartDeviceConfig;
	}
	
	public SmartDeviceConfig getSmartDeviceConfig(){
		return smartDeviceConfig;
	}

}
