package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.FieldDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.IFieldDeviceListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ISmartDeviceListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.LogicRule;

public class SmartDeviceListener implements ISmartDeviceListener{
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	
	@Override
	public void fieldDeviceConnected(ASmartDevice sender, FieldDevice device) {
		device.addFieldDeviceListener(new IFieldDeviceListener() {
			@Override
			public void logicRuleRemoved(LogicRule rule) {
				SmartDeviceServer.getInstance().stopLogicRule(rule);
				
				try{
					System.out.println("LogicProcessor() logicRuleRemoved: "+rule.getActivityName()+" Actor:"+rule.getActor().getName()+"");
				}catch(Exception ex){
					System.out.println("LogicProcessor() logicRuleRemoved: something null...");
				}
			}
		});
	}
	
	@Override
	public void fieldDeviceRemoved(ASmartDevice smartDevice,
			FieldDevice device) {
		LOG.debug("fieldDeviceRemoved device:"+device.getName());
	}
	
	@Override
	public void readyStateChanged(ASmartDevice smartDevice, boolean state) {
		LOG.debug("smartDevice: "+smartDevice.getPeerName()+" readyState:"+state);
	}
}
