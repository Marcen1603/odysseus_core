package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.service;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.IFieldDeviceListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDevicePublisher;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDeviceServerDictionaryDiscovery;

public interface ISmartDeviceService {
	public abstract ASmartDevice getLocalSmartDevice();
	public abstract void addSmartDeviceListener(IFieldDeviceListener listener);
	public abstract void removeSmartDeviceListener(IFieldDeviceListener listener);
	public abstract SmartDevicePublisher getSmartDeviceServer();
	public abstract SmartDeviceServerDictionaryDiscovery getSmartDeviceServerDictionaryDiscovery();
}
