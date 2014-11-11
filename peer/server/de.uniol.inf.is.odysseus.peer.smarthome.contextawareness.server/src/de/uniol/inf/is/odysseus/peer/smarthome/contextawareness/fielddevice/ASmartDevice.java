package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.utils.SmartDeviceConfig;
import net.jxta.peer.PeerID;

public abstract class ASmartDevice implements Serializable { // ISmartDevice,
	private static final long serialVersionUID = 1L;

	/*
	public static ASmartDevice getInstance() {
		return null;
	}
	*/

	// public abstract boolean equals(Object obj);
	public boolean equals(Object obj) {
		if (obj instanceof PeerID) {
			String peerID = this.getPeerID().intern().toString();
			PeerID objPeerID = ((PeerID) obj);
			String objPeerIDString = objPeerID.intern().toString();

			if (peerID != null && objPeerIDString != null) {
				return peerID.equals(objPeerIDString);
			} else {
				return false;
			}
		} else {
			return super.equals(obj);
		}
	}

	// /ISmartDevice
	public abstract List<FieldDevice> getConnectedFieldDevices();

	public abstract ArrayList<Sensor> getConnectedSensors();

	public abstract ArrayList<Actor> getConnectedActors();

	public abstract void addConnectedFieldDevice(FieldDevice device);

	public abstract String getContextName();

	public abstract void setContextName(String contextName);

	public abstract void setSmartDeviceConfig(SmartDeviceConfig config);

	public abstract void setPeerID(String peerID);

	public abstract String getPeerID();

	public abstract void overwriteWith(ASmartDevice smartDevice);

	public abstract boolean isReady();

	public abstract void setReady(boolean state);

	public abstract String getPeerName();

	public abstract void setPeerName(String peerName);

	public abstract void addSmartDeviceListener(ISmartDeviceListener listener);

	public abstract void removeSmartDeviceListener(ISmartDeviceListener listener);

	public abstract Collection<? extends LogicRule> getLogicRules();

	public abstract boolean save() throws PeerCommunicationException;

}
