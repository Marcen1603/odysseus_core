package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.Actor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.FieldDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ILogicRuleListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.IFieldDeviceListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.LogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.SmartDevice;

public class FieldDeviceListener implements IFieldDeviceListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);

	@Override
	public void fieldDeviceConnected(ASmartDevice sender, FieldDevice device) {

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
			//TODO:
			
		}
	}

	@Override
	public void fieldDeviceRemoved(ASmartDevice smartDevice, FieldDevice device) {
		LOG.debug("fieldDeviceRemoved device:" + device.getName());
	}

	@Override
	public void readyStateChanged(ASmartDevice smartDevice, boolean state) {
		LOG.debug("smartDevice: " + smartDevice.getPeerName() + " readyState:"
				+ state);
	}

	@Override
	public void fieldDevicesUpdated(SmartDevice smartDevice) {
		LOG.debug("fieldDevicesUpdated smart device:"
				+ smartDevice.getPeerName());

	}
}
