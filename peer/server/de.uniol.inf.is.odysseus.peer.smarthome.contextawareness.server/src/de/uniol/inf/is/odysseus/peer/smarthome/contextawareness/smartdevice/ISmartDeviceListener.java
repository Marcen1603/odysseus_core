package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.FieldDevice;

public interface ISmartDeviceListener {

	public abstract void fieldDeviceConnected(ASmartDevice sender,
			FieldDevice device);

	public abstract void fieldDeviceRemoved(ASmartDevice sender,
			FieldDevice device);
	
	public abstract void readyStateChanged(ASmartDevice sender,
			boolean state);

	public abstract void SmartDevicesUpdated(ASmartDevice smartDevice);

}
