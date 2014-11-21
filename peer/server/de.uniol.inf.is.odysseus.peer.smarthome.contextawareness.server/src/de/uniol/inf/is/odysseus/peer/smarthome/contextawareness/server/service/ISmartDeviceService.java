package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.service;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDevicePublisher;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDeviceServerDictionaryDiscovery;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ISmartDeviceListener;

public interface ISmartDeviceService {
	public abstract ASmartDevice getLocalSmartDevice();
	public abstract void addSmartDeviceListener(ISmartDeviceListener listener);
	public abstract void removeSmartDeviceListener(ISmartDeviceListener listener);
	public abstract SmartDevicePublisher getSmartDeviceServer();
	public abstract SmartDeviceServerDictionaryDiscovery getSmartDeviceServerDictionaryDiscovery();
}
