package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDeviceServer;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDeviceServerDictionaryDiscovery;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.utils.SmartDeviceConfig;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.utils.SmartDeviceResponseMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.jxta.peer.PeerID;

public class SmartDevice extends ASmartDevice implements Serializable {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	//private static final Logger LOG = LoggerFactory
	//		.getLogger(SmartHomeServerPlugIn.class);
	private static final long serialVersionUID = 1L;
	private String smartDevicePeerID;
	private String contextName;
	private List<FieldDevice> connectedFieldDevices = new ArrayList<FieldDevice>();
	private boolean state = false;
	private String peerName = "local";

	private transient ArrayList<ISmartDeviceListener> smartDeviceListener;
	private transient ConnectedFieldDeviceListener fieldDeviceListener;

	private static SmartDevice instance;

	public List<FieldDevice> getConnectedFieldDevices() {
		return connectedFieldDevices;
	}

	public ArrayList<Sensor> getConnectedSensors() {
		ArrayList<Sensor> sensors = new ArrayList<Sensor>();
		for (FieldDevice fieldDevice : getConnectedFieldDevices()) {
			if (fieldDevice instanceof Sensor) {
				sensors.add((Sensor) fieldDevice);
			}
		}
		return sensors;
	}

	public ArrayList<Actor> getConnectedActors() {
		ArrayList<Actor> actors = new ArrayList<Actor>();
		for (FieldDevice fieldDevice : getConnectedFieldDevices()) {
			if (fieldDevice instanceof Actor) {
				actors.add((Actor) fieldDevice);
			}
		}
		return actors;
	}

	private void setConnectedFieldDevices(
			List<FieldDevice> connectedFieldDevices) {
		this.connectedFieldDevices = connectedFieldDevices;
	}

	public void addConnectedFieldDevice(FieldDevice device) {
		if (this.connectedFieldDevices.add(device)) {
			device.setSmartDevice(this);
			device.addFieldDeviceListener(getConnectedFieldDeviceListener());
			fireFieldDeviceConnected(device);
		}
	}

	public void removeConnectedFieldDevice(FieldDevice device) {
		if (this.connectedFieldDevices.remove(device)) {
			device.removeFieldDeviceListener(getConnectedFieldDeviceListener());
			fireFieldDeviceRemoved(device);
		}
	}

	private IFieldDeviceListener getConnectedFieldDeviceListener() {
		if (fieldDeviceListener == null) {
			fieldDeviceListener = new ConnectedFieldDeviceListener();
		}
		return fieldDeviceListener;
	}

	public String getContextName() {
		return contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public void setSmartDeviceConfig(SmartDeviceConfig config) {
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

	public void overwriteWith(ASmartDevice smartDevice) {
		if (smartDevice != null) {
			this.setContextName(smartDevice.getContextName());
		}

		if (smartDevice.getConnectedFieldDevices() != null) {
			this.setConnectedFieldDevices(smartDevice
					.getConnectedFieldDevices());
			fireFieldDevicesUpdated();
		}
	}

	

	public boolean isReady() {
		return this.state;
	}

	public void setReady(boolean state) {
		this.state = state;
		fireReadyStateChanged(state);
	}

	public String getPeerName() {
		return this.peerName;
	}

	public void setPeerName(String peerName) {
		this.peerName = peerName;
	}

	public static boolean isActivated() {
		return instance != null;
	}

	public static ASmartDevice getInstance() {
		if (instance == null) {
			instance = new SmartDevice();
		}
		return instance;
	}

	@Override
	public void addSmartDeviceListener(ISmartDeviceListener listener) {
		getSmartDeviceListener().add(listener);
	}

	@Override
	public void removeSmartDeviceListener(ISmartDeviceListener listener) {
		getSmartDeviceListener().remove(listener);
	}

	private ArrayList<ISmartDeviceListener> getSmartDeviceListener() {
		if (smartDeviceListener == null) {
			smartDeviceListener = new ArrayList<ISmartDeviceListener>();
		}
		return smartDeviceListener;
	}

	private void fireFieldDeviceConnected(FieldDevice device) {
		for (ISmartDeviceListener listener : getSmartDeviceListener()) {
			listener.fieldDeviceConnected(this, device);
		}
	}

	private void fireFieldDeviceRemoved(FieldDevice device) {
		for (ISmartDeviceListener listener : getSmartDeviceListener()) {
			listener.fieldDeviceRemoved(this, device);
		}
	}

	private void fireReadyStateChanged(boolean state) {
		for (ISmartDeviceListener listener : getSmartDeviceListener()) {
			listener.readyStateChanged(this, state);
		}
	}

	@Override
	public Collection<? extends LogicRule> getLogicRules() {
		Collection<LogicRule> rules = Lists.newLinkedList();

		for (Actor actor : getConnectedActors()) {
			rules.addAll(actor.getLogicRules());
		}

		return rules;
	}

	private class ConnectedFieldDeviceListener implements IFieldDeviceListener {
		@Override
		public void logicRuleRemoved(LogicRule rule) {
			// TODO Auto-generated method stub

		}
	}

	@Override
	public boolean save() throws PeerCommunicationException {
		if(!SmartDeviceServer.isLocalPeer(this.getPeerID())){
			SmartDeviceResponseMessage smartDeviceResponse = new SmartDeviceResponseMessage();
			smartDeviceResponse.setSmartDevice(this);
			
			PeerID peer = SmartDeviceServerDictionaryDiscovery.getPeerIDOfString(getPeerID());
			
			LOG.debug("save() send to peer: "+this.getPeerName());
			
				SmartDeviceServer.getInstance().getPeerCommunicator()
						.send(peer, smartDeviceResponse);
				return true;
		}else{
			//SmartDeviceServer.getInstance().getLocalSmartDevice().overwriteWith(this);
			
			//TODO: save local
			
			return false;
		}		
	}
	
	private void fireFieldDevicesUpdated() {
		for (ISmartDeviceListener listener : getSmartDeviceListener()) {
			listener.fieldDevicesUpdated(this);
		}
	}
}
