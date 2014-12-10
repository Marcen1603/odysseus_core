package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.message;

import java.io.IOException;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ASmartDevice;

public class SmartDeviceResponseMessage extends SmartDeviceMessage {
	//private static transient final Logger LOG = LoggerFactory
	//.getLogger(SmartHomeServerPlugIn.class);
	
	private ASmartDevice smartDevice;
	
	@Override
	public synchronized byte[] toBytes() {
		try {
			return serialize(smartDevice);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public synchronized void fromBytes(byte[] data) {
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
