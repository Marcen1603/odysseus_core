package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

public interface ISmartDeviceListener {

	public abstract void fieldDeviceConnected(ASmartDevice sender,
			FieldDevice device);

	public abstract void readyStateChanged(ASmartDevice smartDevice,
			boolean state);

}
