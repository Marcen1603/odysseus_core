package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfig;

public class SmartDevice implements Serializable {
	private static final long serialVersionUID = 1L;
	private String smartDevicePeerID;
	private String contextName;
	private List<FieldDevice> connectedFieldDevices = new ArrayList<FieldDevice>();
	private boolean state = false;
	private String peerName;

	public List<FieldDevice> getConnectedFieldDevices() {
		return connectedFieldDevices;
	}
	
	public ArrayList<Sensor> getConnectedSensors(){
		ArrayList<Sensor> sensors = new ArrayList<Sensor>();
		for(FieldDevice fieldDevice : getConnectedFieldDevices()){
			if(fieldDevice instanceof Sensor){
				sensors.add((Sensor)fieldDevice);
			}
		}
		return sensors;
	}
	
	public ArrayList<Actor> getConnectedActors(){
		ArrayList<Actor> actors = new ArrayList<Actor>();
		for(FieldDevice fieldDevice : getConnectedFieldDevices()){
			if(fieldDevice instanceof Actor){
				actors.add((Actor)fieldDevice);
			}
		}
		return actors;
	}

	public void setConnectedFieldDevices(List<FieldDevice> connectedFieldDevices) {
		this.connectedFieldDevices = connectedFieldDevices;
	}

	public void addConnectedFieldDevice(FieldDevice device) {
		this.connectedFieldDevices.add(device);
	}

	public String getContextName() {
		return contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public void setSmartDevice(SmartDeviceConfig config) {
		if (config.getContextname() != null) {
			setContextName(config.getContextname());
		}
		// ...

	}

	public void setPeerID(String peerID) {
		this.smartDevicePeerID = peerID;
	}

	public String getPeerID() {
		return this.smartDevicePeerID;
	}

	@Override
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

	public void overwriteWith(SmartDevice smartDevice) {
		if (smartDevice != null) {
			this.setContextName(smartDevice.getContextName());
		}

		if (smartDevice.getConnectedFieldDevices() != null) {
			this.setConnectedFieldDevices(smartDevice
					.getConnectedFieldDevices());
		}
	}

	public boolean isReady() {
		return this.state;
	}

	public void setReady(boolean state) {
		this.state = state;
	}

	public String getPeerName() {
		return this.peerName;
	}

	public void setPeerName(String peerName) {
		this.peerName = peerName;
	}
}
