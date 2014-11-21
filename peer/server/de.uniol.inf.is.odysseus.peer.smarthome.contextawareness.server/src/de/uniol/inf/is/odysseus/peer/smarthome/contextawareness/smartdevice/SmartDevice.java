package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.FieldDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.ActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.IActivityInterpreterListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.AbstractActor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.ILogicRuleListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.AbstractLogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor.AbstractSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDevicePublisher;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDeviceDictionaryDiscovery;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.config.SmartDeviceConfig;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.message.SmartDeviceResponseMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;

public class SmartDevice extends ASmartDevice implements Serializable {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static final long serialVersionUID = 1L;
	private String smartDevicePeerID;
	private String contextName;
	private List<FieldDevice> connectedFieldDevices = new ArrayList<FieldDevice>();
	private boolean state = false;
	private String peerName = "local";

	private transient ArrayList<ISmartDeviceListener> smartDeviceListener;
	private transient LogicRuleListener logicRuleListener;
	private transient ActivityInterpreterListener activityInterpreterListener;
	private transient HashMap<String, ArrayList<String>> activitySourceMap = new HashMap<String, ArrayList<String>>();
	
	private static SmartDevice instance;

	public List<FieldDevice> getConnectedFieldDevices() {
		return connectedFieldDevices;
	}

	public ArrayList<AbstractSensor> getConnectedSensors() {
		ArrayList<AbstractSensor> sensors = new ArrayList<AbstractSensor>();
		for (FieldDevice fieldDevice : getConnectedFieldDevices()) {
			if (fieldDevice instanceof AbstractSensor) {
				sensors.add((AbstractSensor) fieldDevice);
			}
		}
		return sensors;
	}

	public ArrayList<AbstractActor> getConnectedActors() {
		ArrayList<AbstractActor> actors = new ArrayList<AbstractActor>();
		for (FieldDevice fieldDevice : getConnectedFieldDevices()) {
			if (fieldDevice instanceof AbstractActor) {
				actors.add((AbstractActor) fieldDevice);
			}
		}
		return actors;
	}

	private void setConnectedFieldDevices(
			List<FieldDevice> connectedFieldDevices) {
		this.connectedFieldDevices = connectedFieldDevices;
	}

	@Override
	public synchronized void addConnectedFieldDevice(FieldDevice device) {
		LOG.debug("addConnectedFieldDevice");
		
		if (!this.connectedFieldDevices.contains(device) && this.connectedFieldDevices.add(device)) {
			device.setSmartDevice(this);
			if(device instanceof AbstractSensor){
				((AbstractSensor) device).addActivityInterpreterListener(getActivityInterpreterListener());
			}else if(device instanceof AbstractActor){
				((AbstractActor) device).addLogicRuleListener(getLogicRuleListener());
			}
			
			updateActivitiesSources();
			fireFieldDeviceConnected(device);
		}
	}

	@Override
	public synchronized void removeConnectedFieldDevice(FieldDevice device) {
		if (this.connectedFieldDevices.remove(device)) {
			
			if(device instanceof AbstractSensor){
				((AbstractSensor) device).removeActivityInterpreterListener(getActivityInterpreterListener());
			}else if(device instanceof AbstractActor){
				((AbstractActor) device).removeLogicRuleListener(getLogicRuleListener());
			}
			
			updateActivitiesSources();
			fireFieldDeviceRemoved(device);
		}
	}

	private ILogicRuleListener getLogicRuleListener() {
		if (logicRuleListener == null) {
			logicRuleListener = new LogicRuleListener();
		}
		return logicRuleListener;
	}
	
	private IActivityInterpreterListener getActivityInterpreterListener(){
		if (activityInterpreterListener == null) {
			activityInterpreterListener = new ActivityInterpreterListener();
		}
		return activityInterpreterListener;
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
			fireSmartDevicesUpdated();
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

	public synchronized static ASmartDevice getInstance() {
		if (instance == null) {
			instance = new SmartDevice();
		}
		return instance;
	}

	@Override
	public synchronized void addSmartDeviceListener(ISmartDeviceListener listener) {
		LOG.error("addSmartDeviceListener!!!!!");
		
		if(!getSmartDeviceListener().contains(listener)){
			getSmartDeviceListener().add(listener);
		}
	}

	@Override
	public synchronized void removeSmartDeviceListener(ISmartDeviceListener listener) {
		getSmartDeviceListener().remove(listener);
	}

	private ArrayList<ISmartDeviceListener> getSmartDeviceListener() {
		if (smartDeviceListener == null) {
			smartDeviceListener = new ArrayList<ISmartDeviceListener>();
		}
		return smartDeviceListener;
	}

	private void fireFieldDeviceConnected(FieldDevice device) {
		LOG.error("fireFieldDeviceConnected:"+device.getName());
		
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
			listener.smartDeviceReadyStateChanged(this, state);
		}
	}

	@Override
	public Collection<? extends AbstractLogicRule> getLogicRules() {
		Collection<AbstractLogicRule> rules = Lists.newLinkedList();

		for (AbstractActor actor : getConnectedActors()) {
			rules.addAll(actor.getLogicRules());
		}

		return rules;
	}

	private class LogicRuleListener implements ILogicRuleListener {
		@Override
		public void logicRuleAdded(AbstractLogicRule rule) {
			LOG.debug("SmartDevice logicRuleAdded:"+rule.getActivityName());
			
		}
		
		@Override
		public void logicRuleRemoved(AbstractLogicRule rule) {
			LOG.debug("SmartDevice logicRuleRemoved:"+rule.getActivityName());
			
		}
	}
	
	private class ActivityInterpreterListener implements IActivityInterpreterListener {
		@Override
		public void activityInterpreterAdded(
				ActivityInterpreter activityInterpreter) {
			System.out.println("SmartDevice activityInterpreterAdded:"+activityInterpreter.getActivityName());
			
		}
		
		@Override
		public void activityInterpreterRemoved(
				ActivityInterpreter activityInterpreter) {
			System.out.println("SmartDevice activityInterpreterRemoved:"+activityInterpreter.getActivityName());
			
		}
	}
	
	@Override
	public synchronized HashMap<String, ArrayList<String>> getActivitySourceMap() {
		if(activitySourceMap==null){
			activitySourceMap = new HashMap<String, ArrayList<String>>();
		}
		return activitySourceMap;
	}

	@Override
	public boolean save() throws PeerCommunicationException {
		if(!SmartDevicePublisher.isLocalPeer(this.getPeerID())){
			SmartDeviceResponseMessage smartDeviceResponse = new SmartDeviceResponseMessage();
			smartDeviceResponse.setSmartDevice(this);
			
			PeerID peer = SmartDeviceDictionaryDiscovery.getPeerIDOfString(getPeerID());
			
			LOG.debug("save() send to peer: "+this.getPeerName());
			
				SmartDevicePublisher.getInstance().getPeerCommunicator()
						.send(peer, smartDeviceResponse);
				return true;
		}else{
			//SmartDeviceServer.getInstance().getLocalSmartDevice().overwriteWith(this);
			
			//TODO: save local
			
			return false;
		}		
	}
	
	private void fireSmartDevicesUpdated() {
		for (ISmartDeviceListener listener : getSmartDeviceListener()) {
			listener.smartDevicesUpdated(this);
		}
	}

	public String getMergedActivitySourceName(String activitySourceName) {
		return getPeerName()+"_Activities_"+activitySourceName+"_source";
	}

	public synchronized void updateActivitiesSources() {
		LOG.debug("-----updateActivitiesSources------");
		
		getActivitySourceMap().clear();
		
		for(AbstractSensor sensor : getConnectedSensors()){
			
			for(ActivityInterpreter activityInterpreter : sensor.getActivityInterpreters()){
				String activityName = activityInterpreter.getActivityName();
				String activitySourceName = activityInterpreter.getActivitySourceName();
				
				LOG.debug("Sensor:"+sensor.getName()+" activityName:"+activityName+" activitySourceName:"+activitySourceName);
				
				
				addActivitySourceToMap(activityName, activitySourceName);
			}
		}
	}

	private synchronized void addActivitySourceToMap(String activityName, String activitySourceName) {
		if(getActivitySourceMap().get(activityName)!=null){//this activity exist!
			ArrayList<String> list = getActivitySourceMap().get(activityName);
			if(list == null){
				list = new ArrayList<String>();
			}
			
			if(!list.contains(activitySourceName)){
				list.add(activitySourceName);
			}
			
			getActivitySourceMap().put(activityName, list);
		}else{
			ArrayList<String> list = new ArrayList<String>();
			list.add(activitySourceName);
			getActivitySourceMap().put(activityName, list);
		}
	}
}
