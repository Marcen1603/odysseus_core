package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ASmartDevice;


public interface ISmartDeviceDictionaryListener {
	void smartDeviceAdded(SmartDevicePublisher sender, ASmartDevice smartDevice);
	void smartDeviceRemoved(SmartDevicePublisher sender, ASmartDevice smartDevice);
	void smartDeviceUpdated(SmartDevicePublisher sender, ASmartDevice smartDevice);
}
