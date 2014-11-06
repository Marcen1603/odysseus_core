package de.uniol.inf.is.odysseus.peer.smarthome.server;

import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.ASmartDevice;


public interface ISmartDeviceDictionaryListener {
	void smartDeviceAdded(SmartDeviceServerDictionaryDiscovery sender, ASmartDevice smartDevice);
	void smartDeviceRemoved(SmartDeviceServerDictionaryDiscovery sender, ASmartDevice smartDevice);
	void smartDeviceUpdated(SmartDeviceServerDictionaryDiscovery sender, ASmartDevice smartDevice);
}
