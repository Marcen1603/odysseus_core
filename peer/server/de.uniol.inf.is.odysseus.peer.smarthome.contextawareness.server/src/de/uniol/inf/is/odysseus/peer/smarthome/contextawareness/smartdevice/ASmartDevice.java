package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.FieldDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.AbstractActor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.AbstractLogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor.AbstractSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.config.SmartDeviceConfig;
import net.jxta.peer.PeerID;

public abstract class ASmartDevice implements Serializable, Cloneable { // ISmartDevice,
	private static final long serialVersionUID = 1L;
	
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

	public abstract List<FieldDevice> getConnectedFieldDevices();

	public abstract ArrayList<AbstractSensor> getConnectedSensors();

	public abstract ArrayList<AbstractActor> getConnectedActors();

	public abstract void addConnectedFieldDevice(FieldDevice device);

	public abstract void removeConnectedFieldDevice(FieldDevice device);
	
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

	public abstract Collection<? extends AbstractLogicRule> getLogicRules();

	public abstract boolean save() throws PeerCommunicationException;

	public AbstractSensor getSensor(String sensorName) {
		for(AbstractSensor sensor : getConnectedSensors()){
			return sensor;
		}
		return null;
	}

	public abstract String getMergedActivitySourceName(String activityName);

	public abstract HashMap<String, ArrayList<String>> getActivitySourceMap();

	public abstract void updateActivitiesSources();

	public abstract HashMap<String, String> getActivityViewNameOfMergedActivities();

	public abstract String getMergedImportedActivitiesSourceName(String activityName);

	
}
