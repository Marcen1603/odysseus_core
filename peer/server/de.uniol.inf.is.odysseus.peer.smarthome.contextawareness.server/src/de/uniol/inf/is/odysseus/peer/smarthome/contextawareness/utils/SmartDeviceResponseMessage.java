package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.utils;

import java.io.IOException;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ASmartDevice;

public class SmartDeviceResponseMessage extends SmartDeviceMessage {
	private ASmartDevice smartDevice;
	
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
			this.smartDevice = (ASmartDevice) deserialize(data);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	public ASmartDevice getSmartDevice() {
		return smartDevice;
	}

	public void setSmartDevice(ASmartDevice smartDevice) {
		this.smartDevice = smartDevice;
	}

}
