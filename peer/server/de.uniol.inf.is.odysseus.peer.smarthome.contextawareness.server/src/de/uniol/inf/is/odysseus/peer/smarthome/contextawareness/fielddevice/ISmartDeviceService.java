package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice;

public interface ISmartDeviceService {
	public abstract ASmartDevice getLocalSmartDevice();
	public abstract void addSmartDeviceListener(ISmartDeviceListener listener);
	public abstract void removeSmartDeviceListener(ISmartDeviceListener listener);
}
