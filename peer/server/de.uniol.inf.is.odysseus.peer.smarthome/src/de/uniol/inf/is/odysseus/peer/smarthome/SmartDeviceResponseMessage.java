package de.uniol.inf.is.odysseus.peer.smarthome;

import java.io.IOException;

import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.SmartDevice;



public class SmartDeviceResponseMessage extends SmartDeviceMessage {
	private SmartDevice smartDevice;
	
	public byte[] toBytes() {
		try {
			return serialize(smartDevice);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void fromBytes(byte[] data) {
		try {
			this.smartDevice = (SmartDevice) deserialize(data);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	public SmartDevice getSmartDevice() {
		return smartDevice;
	}

	public void setSmartDevice(SmartDevice smartDevice) {
		this.smartDevice = smartDevice;
	}

}
