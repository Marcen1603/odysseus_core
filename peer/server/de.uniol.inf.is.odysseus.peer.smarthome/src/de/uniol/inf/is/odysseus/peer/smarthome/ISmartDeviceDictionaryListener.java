package de.uniol.inf.is.odysseus.peer.smarthome;

import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.SmartDevice;

public interface ISmartDeviceDictionaryListener {
	void smartDeviceAdded(SmartDeviceDictionary sender, SmartDevice smartDevice);
	void smartDeviceRemoved(SmartDeviceDictionary sender, SmartDevice smartDevice);
	void smartDeviceUpdated(SmartDeviceDictionary sender, SmartDevice smartDevice);
}
