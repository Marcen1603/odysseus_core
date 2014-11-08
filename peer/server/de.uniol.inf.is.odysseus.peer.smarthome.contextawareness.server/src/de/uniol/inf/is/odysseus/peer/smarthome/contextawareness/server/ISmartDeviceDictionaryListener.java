package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ASmartDevice;


public interface ISmartDeviceDictionaryListener {
	void smartDeviceAdded(SmartDeviceServer sender, ASmartDevice smartDevice);
	void smartDeviceRemoved(SmartDeviceServer sender, ASmartDevice smartDevice);
	void smartDeviceUpdated(SmartDeviceServer sender, ASmartDevice smartDevice);
}
