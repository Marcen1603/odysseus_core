package de.uniol.inf.is.odysseus.peer.smarthome.server;

import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.SmartDevice;

public interface ISmartDeviceDictionaryListener {
	void smartDeviceAdded(SmartDeviceDictionaryDiscovery sender, SmartDevice smartDevice);
	void smartDeviceRemoved(SmartDeviceDictionaryDiscovery sender, SmartDevice smartDevice);
	void smartDeviceUpdated(SmartDeviceDictionaryDiscovery sender, SmartDevice smartDevice);
}
