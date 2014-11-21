package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.FieldDevice;

public interface ISmartDeviceListener {
	public abstract void fieldDeviceConnected(ASmartDevice sender, FieldDevice device);
	public abstract void fieldDeviceRemoved(ASmartDevice sender, FieldDevice device);
	public abstract void smartDeviceReadyStateChanged(ASmartDevice sender, boolean state);
	public abstract void smartDevicesUpdated(ASmartDevice smartDevice);
}
