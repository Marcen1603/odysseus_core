package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice;

public interface ISmartDeviceListener {

	public abstract void fieldDeviceConnected(ASmartDevice sender,
			FieldDevice device);

	public abstract void readyStateChanged(ASmartDevice sender,
			boolean state);

	public abstract void fieldDeviceRemoved(ASmartDevice sender,
			FieldDevice device);

	public abstract void fieldDevicesUpdated(SmartDevice smartDevice);

}
