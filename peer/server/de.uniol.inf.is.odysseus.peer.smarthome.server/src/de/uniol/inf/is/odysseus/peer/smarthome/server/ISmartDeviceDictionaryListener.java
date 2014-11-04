package de.uniol.inf.is.odysseus.peer.smarthome.server;

import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.SmartDevice;

public interface ISmartDeviceDictionaryListener {
	void smartDeviceAdded(SmartDeviceServerDictionaryDiscovery sender, SmartDevice smartDevice);
	void smartDeviceRemoved(SmartDeviceServerDictionaryDiscovery sender, SmartDevice smartDevice);
	void smartDeviceUpdated(SmartDeviceServerDictionaryDiscovery sender, SmartDevice smartDevice);
}
