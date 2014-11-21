package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.Actor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.FieldDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.IActivityInterpreterListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ILogicRuleListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.IFieldDeviceListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.LogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.SmartDevice;

public class SmartDevicePublisherFieldDeviceListener implements IFieldDeviceListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private IActivityInterpreterListener activityInterpreterListener;
	
	SmartDevicePublisherFieldDeviceListener(){
		activityInterpreterListener = new IActivityInterpreterListener() {
			
			@Override
			public void activityInterpreterRemoved(
					ActivityInterpreter activityInterpreter) {
				LOG.debug("activityInterpreterRemoved:" + activityInterpreter.getActivityName());
				
				if(SmartDevicePublisher.isLocalPeer(activityInterpreter.getSensor().getSmartDevice().getPeerID())){
					QueryExecutor.getInstance().removeSourceIfNeccessary(activityInterpreter.getActivitySourceName());
				}else{
					//activityInterpreterRemoved at remote smart device:
					
					
				}
			}
			
			@Override
			public void activityInterpreterAdded(ActivityInterpreter activityInterpreter) {
				LOG.debug("SmartDeviceServerFieldDeviceListener activityInterpreterAdded:" + activityInterpreter.getActivityName());
				
				if(SmartDevicePublisher.isLocalPeer(activityInterpreter.getSensor().getSmartDevice().getPeerID())){
					QueryExecutor.getInstance().exportWhenPossibleAsync(
							activityInterpreter.getActivitySourceName());
				}else{
					//activityInterpreterAdded at remote smart device:
					
					
				}
			}
		};
	}

	@Override
	public void fieldDeviceConnected(ASmartDevice sender, FieldDevice device) {
		LOG.debug("SmartDeviceServerFieldDeviceListener fieldDeviceConnected device:" + device.getName());
		
		if (device instanceof Actor) {
			((Actor) device).addLogicRuleListener(new ILogicRuleListener() {
				@Override
				public void logicRuleRemoved(LogicRule rule) {
					SmartDevicePublisher.getInstance().stopLogicRule(rule);

					try {
						System.out
								.println("LogicProcessor() logicRuleRemoved: "
										+ rule.getActivityName() + " Actor:"
										+ rule.getActor().getName() + "");
					} catch (Exception ex) {
						System.out
								.println("LogicProcessor() logicRuleRemoved: something null...");
					}
				}
			});
		}else if(device instanceof Sensor){
			Sensor sensor = (Sensor)device;
			
			sensor.addActivityInterpreterListener(activityInterpreterListener);
			
			for(ActivityInterpreter interpreter : sensor.getActivityInterpreters()){
				QueryExecutor.getInstance().exportWhenPossibleAsync(
						interpreter.getActivitySourceName());
			}
		}
	}

	@Override
	public void fieldDeviceRemoved(ASmartDevice smartDevice, FieldDevice device) {
		LOG.debug("SmartDeviceServerFieldDeviceListener fieldDeviceRemoved device:" + device.getName());
		
		if (device instanceof Actor) {
			
		}else if(device instanceof Sensor){
			Sensor sensor = (Sensor)device;
			
			sensor.removeActivityInterpreterListener(activityInterpreterListener);
		}
	}

	@Override
	public void readyStateChanged(ASmartDevice smartDevice, boolean state) {
		LOG.debug("SmartDeviceServerFieldDeviceListener smartDevice: " + smartDevice.getPeerName() + " readyState:"
				+ state);
		
	}

	@Override
	public void fieldDevicesUpdated(SmartDevice smartDevice) {
		LOG.debug("SmartDeviceServerFieldDeviceListener fieldDevicesUpdated smart device:"
				+ smartDevice.getPeerName());
		
		
	}
}
