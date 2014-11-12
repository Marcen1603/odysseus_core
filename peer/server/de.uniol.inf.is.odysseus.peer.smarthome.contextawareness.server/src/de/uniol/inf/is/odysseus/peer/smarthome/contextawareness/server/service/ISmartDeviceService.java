package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.service;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ISmartDeviceListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDeviceServer;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDeviceServerDictionaryDiscovery;

public interface ISmartDeviceService {
	public abstract ASmartDevice getLocalSmartDevice();
	public abstract void addSmartDeviceListener(ISmartDeviceListener listener);
	public abstract void removeSmartDeviceListener(ISmartDeviceListener listener);
	public abstract SmartDeviceServer getSmartDeviceServer();
	public abstract SmartDeviceServerDictionaryDiscovery getSmartDeviceServerDictionaryDiscovery();
}
