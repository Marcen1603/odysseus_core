package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.FieldDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.ActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.IActivityInterpreterListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.Actor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.ILogicRuleListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.LogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor.Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ISmartDeviceListener;

public class ActivityInterpreterProcessor implements ISmartDeviceListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static ActivityInterpreterProcessor instance = new ActivityInterpreterProcessor();
	private IActivityInterpreterListener activityInterpreterListener;
	private ILogicRuleListener logicRuleListener;

	private ActivityInterpreterProcessor() {
		createActivityInterpreterListener();
		createLogicRuleListener();
	}

	private void createActivityInterpreterListener() {
		activityInterpreterListener = new IActivityInterpreterListener() {
			@Override
			public void activityInterpreterRemoved(
					ActivityInterpreter activityInterpreter) {
				LOG.debug("activityInterpreterRemoved:"
						+ activityInterpreter.getActivityName());

				if (SmartDevicePublisher.isLocalPeer(activityInterpreter
						.getSensor().getSmartDevice().getPeerID())) {
					QueryExecutor.getInstance().removeSourceIfNeccessary(
							activityInterpreter.getActivitySourceName());
				} else {
					// activityInterpreterRemoved at remote smart device:

				}
			}

			@Override
			public void activityInterpreterAdded(
					ActivityInterpreter activityInterpreter) {
				LOG.debug("SmartDeviceServerFieldDeviceListener activityInterpreterAdded:"
						+ activityInterpreter.getActivityName());

				String activityName = activityInterpreter.getActivityName();
				String activitySourceName = activityInterpreter
						.getActivitySourceName();

				for (Entry<String, String> entry : activityInterpreter
						.getActivityInterpreterQueries(activityName).entrySet()) {
					String viewName = entry.getKey();
					String query = entry.getValue();

					try {
						QueryExecutor.getInstance().executeQueryNow(viewName,
								query);

					} catch (Exception e) {
						LOG.error(e.getMessage(), e);
					}
				}

				// TODO:
				// Merge activities with same name to one activity-output-stream
				// and then export the merged stream
				// (UNION)
				try {
					QueryExecutor.getInstance().exportWhenPossibleAsync(
							activitySourceName);
				} catch (Exception ex) {
					LOG.error(ex.getMessage(), ex);
				}

				if (SmartDevicePublisher.isLocalPeer(activityInterpreter
						.getSensor().getSmartDevice().getPeerID())) {
					QueryExecutor.getInstance().exportWhenPossibleAsync(
							activityInterpreter.getActivitySourceName());
				} else {
					// activityInterpreterAdded at remote smart device:

				}
			}
		};
	}

	private void createLogicRuleListener() {
		logicRuleListener = new ILogicRuleListener() {
			@Override
			public void logicRuleRemoved(LogicRule rule) {
				SmartDevicePublisher.getInstance().stopLogicRule(rule);

				try {
					System.out.println("LogicProcessor() logicRuleRemoved: "
							+ rule.getActivityName() + " Actor:"
							+ rule.getActor().getName() + "");
				} catch (Exception ex) {
					System.out
							.println("LogicProcessor() logicRuleRemoved: something null...");
				}
			}
		};
	}

	public synchronized static ActivityInterpreterProcessor getInstance() {
		return instance;
	}

	/***
	 * ISmartDeviceListener
	 */
	@Override
	public void fieldDeviceConnected(ASmartDevice sender, FieldDevice device) {
		LOG.debug("fieldDeviceConnected: " + device.getName());

		if (device instanceof Actor) {
			Actor actor = (Actor) device;
			actor.addLogicRuleListener(logicRuleListener);
		} else if (device instanceof Sensor) {
			Sensor sensor = (Sensor) device;

			for (Entry<String, String> queryForRawValue : sensor
					.getQueriesForRawValues().entrySet()) {
				String viewName = queryForRawValue.getKey();
				String query = queryForRawValue.getValue();

				try {
					QueryExecutor.getInstance()
							.executeQueryNow(viewName, query);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}

			sensor.addActivityInterpreterListener(activityInterpreterListener);
		}
	}

	@Override
	public void fieldDeviceRemoved(ASmartDevice sender, FieldDevice device) {
		LOG.debug("fieldDeviceRemoved: " + device.getName());

		if (device instanceof Actor) {
			Actor actor = (Actor) device;

			actor.removeLogicRuleListener(logicRuleListener);
		} else if (device instanceof Sensor) {
			Sensor sensor = (Sensor) device;
			
			//TODO: Stop activity interpreters
			for(@SuppressWarnings("unused") ActivityInterpreter interpreter : sensor.getActivityInterpreters()){
				//interpreter.
				
			}
			
			sensor.removeActivityInterpreterListener(activityInterpreterListener);
		}
	}

	@Override
	public void readyStateChanged(ASmartDevice sender, boolean state) {
		LOG.debug("readyStateChanged: " + state);

	}

	@Override
	public void SmartDevicesUpdated(ASmartDevice smartDevice) {
		LOG.debug("SmartDevicesUpdated: " + smartDevice.getPeerName());

		// TODO:
		
	}
}
