package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import java.util.HashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.FieldDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.ActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.AbstractActor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.AbstractLogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor.AbstractSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ISmartDeviceListener;

public class LogicRuleProcessor implements ISmartDeviceDictionaryListener, ISmartDeviceListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static LogicRuleProcessor instance;

	private LogicRuleProcessor() {

	}

	/************************************************
	 * ISmartDeviceDictionaryListener
	 */
	@Override
	public void smartDeviceAdded(SmartDevicePublisher sender,
			ASmartDevice newSmartDevice) {
		LOG.debug("smartDeviceAdded: " + newSmartDevice.getPeerID()
				+ " processLogic(" + newSmartDevice.getPeerName());

		processLogic(newSmartDevice);
	}

	@Override
	public void smartDeviceRemoved(SmartDevicePublisher sender,
			ASmartDevice removedSmartDevice) {
		LOG.debug("smartDeviceRemoved: " + removedSmartDevice.getPeerName());
		// TODO: beide methoden vergleichen!!!
		removeRelevantLogicRules(removedSmartDevice);
		QueryExecutor.getInstance().removeAllLogicRules(removedSmartDevice);
	}

	@Override
	public void smartDeviceUpdated(SmartDevicePublisher sender,
			ASmartDevice updatedSmartDevice) {
		updateLogicRulesIfNeeded(updatedSmartDevice);
	}

	/*******************************
	 * Logic processing:
	 *******************************/
	void processLogic(ASmartDevice newSmartDevice) {
		LOG.debug("processLogic for newSmartDevice:"
				+ newSmartDevice.getPeerName());

		HashMap<ActivityInterpreter, AbstractLogicRule> relatedActivityInterpreterWithLogicRule = getRelatedActivityNamesWithSourceName(newSmartDevice);

		importRelatedSourcesFromActivityInterpreter(
				relatedActivityInterpreterWithLogicRule, newSmartDevice);

		insertActivitySourceNamesToLogicRules(newSmartDevice);

		executeRelatedLogicRules(newSmartDevice);
	}

	private HashMap<ActivityInterpreter, AbstractLogicRule> getRelatedActivityNamesWithSourceName(
			ASmartDevice newSmartDevice) {
		HashMap<ActivityInterpreter, AbstractLogicRule> map = new HashMap<>();

		String smartDeviceContextName = "";
		if (SmartDevicePublisher.getInstance().getLocalSmartDevice() == null) {
			return map;
		} else {
			smartDeviceContextName = SmartDevicePublisher.getInstance()
					.getLocalSmartDevice().getContextName();
			LOG.debug("smartDeviceCtx:" + smartDeviceContextName
					+ " newSmartDeviceCtx:" + newSmartDevice.getContextName());
			// TODO: comment out next line
			smartDeviceContextName = "Office";
		}

		if (smartDeviceContextName != null
				&& newSmartDevice.getContextName().equals(
						smartDeviceContextName)) {
			for (AbstractSensor remoteSensor : newSmartDevice.getConnectedSensors()) {
				for (ActivityInterpreter activityInterpreter : remoteSensor
						.getActivityInterpreters()) {
					for (AbstractActor connectedActor : SmartDevicePublisher.getInstance()
							.getLocalSmartDevice().getConnectedActors()) {
						for (AbstractLogicRule logicRule : connectedActor
								.getLogicRules()) {
							if (activityInterpreter.getActivityName().equals(
									logicRule.getActivityName())) {
								LOG.debug("Logic map.put: activityInterpreter:"
										+ activityInterpreter.getActivityName()
										+ ", logicRule:"
										+ logicRule.getActivityName());

								map.put(activityInterpreter, logicRule);
							}
						}
					}
				}
			}
		}

		return map;
	}

	private void importRelatedSourcesFromActivityInterpreter(
			HashMap<ActivityInterpreter, AbstractLogicRule> relatedActivityInterpreterWithLogicRule,
			ASmartDevice newSmartDevice) {

		for (Entry<ActivityInterpreter, AbstractLogicRule> entry : relatedActivityInterpreterWithLogicRule
				.entrySet()) {
			ActivityInterpreter inter = entry.getKey();

			QueryExecutor.getInstance().addNeededSourceForImport(
					inter.getActivitySourceName(),
					inter.getSensor().getSmartDevice().getPeerID());
		}
	}

	private void insertActivitySourceNamesToLogicRules(
			ASmartDevice newSmartDevice) {
		HashMap<ActivityInterpreter, AbstractLogicRule> relatedSensorsWithActors = getRelatedSensorsWithActorsWithLogic(newSmartDevice);
		for (Entry<ActivityInterpreter, AbstractLogicRule> entry : relatedSensorsWithActors
				.entrySet()) {
			ActivityInterpreter activityInterpreter = entry.getKey();
			AbstractLogicRule logicRule = entry.getValue();

			String activitySourceName = activityInterpreter
					.getActivitySourceName();
			logicRule.addActivitySourceName(activitySourceName);
		}
	}

	private HashMap<ActivityInterpreter, AbstractLogicRule> getRelatedSensorsWithActorsWithLogic(
			ASmartDevice newSmartDevice) {
		HashMap<ActivityInterpreter, AbstractLogicRule> map = new HashMap<ActivityInterpreter, AbstractLogicRule>();

		String smartDeviceContextName = "";
		if (SmartDevicePublisher.getInstance().getLocalSmartDevice() == null) {
			return map;
		} else {
			smartDeviceContextName = SmartDevicePublisher.getInstance()
					.getLocalSmartDevice().getContextName();
			LOG.debug("smartDeviceCtx:" + smartDeviceContextName
					+ " newSmartDeviceCtx:" + newSmartDevice.getContextName());
			// TODO: comment out next line
			smartDeviceContextName = "Office";
		}

		if (smartDeviceContextName != null
				&& newSmartDevice.getContextName().equals(
						smartDeviceContextName)) {

			fillMapWithRelatedActivityInterpreterAndLogicRules(newSmartDevice,
					map);
		}

		return map;
	}

	private void fillMapWithRelatedActivityInterpreterAndLogicRules(
			ASmartDevice newSmartDevice,
			HashMap<ActivityInterpreter, AbstractLogicRule> map) {
		for (AbstractSensor remoteSensor : newSmartDevice.getConnectedSensors()) {
			for (ActivityInterpreter activityInterpreter : remoteSensor
					.getActivityInterpreters()) {
				for (AbstractActor localActor : SmartDevicePublisher.getInstance()
						.getLocalSmartDevice().getConnectedActors()) {
					for (AbstractLogicRule logicRule : localActor.getLogicRules()) {
						if (activityInterpreter.getActivityName().equals(
								logicRule.getActivityName())) {
							
							LOG.debug("activityInterpreter:"+activityInterpreter.getActivityName()+" logicRuleActivityName:"
									+ logicRule.getActivityName()+" sensorPeerName:"+activityInterpreter.getSensor().getSmartDevice().getPeerName()
									+ " ruleDesc:"+logicRule.getReactionDescription()+" actDesc:"+activityInterpreter.getActivityInterpreterDescription());
							
							//TODO: !?
							//String activitySourceName = activityInterpreter
							//		.getActivitySourceName();
							//logicRule.addActivitySourceName(activitySourceName);
							
							map.put(activityInterpreter, logicRule);
						}
					}
				}
			}
		}
		
		LOG.debug("map.size():"+map.size());
	}

	private void removeRelevantLogicRules(ASmartDevice removedSmartDevice) {
		LOG.debug("removeRelevantLogicRules");
		stopRunningLogicRules(removedSmartDevice);
		removeMarkerForSourceNeedToImport(removedSmartDevice);
	}

	private void stopRunningLogicRules(ASmartDevice removedSmartDevice) {
		LOG.debug("stopRunningLogicRules");
		QueryExecutor.getInstance().removeAllLogicRules(removedSmartDevice);
	}

	private void removeMarkerForSourceNeedToImport(
			ASmartDevice removedSmartDevice) {
		LOG.debug("removeMarkerForSourceNeedToImport:"
				+ removedSmartDevice.getPeerName());
		for (AbstractSensor remoteSensor : removedSmartDevice.getConnectedSensors()) {
			for (ActivityInterpreter activityInterpreter : remoteSensor
					.getActivityInterpreters()) {
				String srcName = activityInterpreter.getActivitySourceName();
				try {
					QueryExecutor.getInstance().removeSourceIfNeccessary(
							srcName);

					// QueryExecutor.getInstance().stopQuery(srcName);
					// QueryExecutor.getInstance().stopQuery(srcName +
					// "_query");
				} catch (Exception ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}
		}
	}

	private void updateLogicRulesIfNeeded(ASmartDevice updatedSmartDevice) {
		// TODO:
		// LOG.debug("smartDeviceUpdated: " +
		// smartDevice.getPeerIDString());
		//
		// TODO: Nachschauen was sich am SmartDevice geändert hat.
		// Falls Sensoren hinzugefügt oder entfernt wurden, dann müssen die
		// Logik-Regeln überprüft und anschließend ausgeführt oder entfernt
		// werden!
		//
		// vergleich von altem mit neuem SmartDevice?

	}

	private void executeRelatedLogicRules(ASmartDevice newSmartDevice) {
		LOG.debug("executeRelatedLogicRules:" + newSmartDevice.getPeerName());
		
		for (AbstractActor localActor : SmartDevicePublisher.getInstance()
				.getLocalSmartDevice().getConnectedActors()) {
			for (AbstractLogicRule localRule : localActor.getLogicRules()) {
				for (String activitySourceNameLogicRule : localRule
						.getActivitySourceNameList()) {
					for (AbstractSensor remoteSensor : newSmartDevice.getConnectedSensors()) {
						for (ActivityInterpreter remoteActivityInterpreter : remoteSensor
								.getActivityInterpreters()) {
							if (remoteActivityInterpreter.getActivitySourceName()
									.equals(activitySourceNameLogicRule)) {
								// match: logicRule with ActivityInterpreter

								// Add activityInterpreter and this adds
								// activitySourceName to LogicRule:
								localRule.addActivityInterpreter(remoteActivityInterpreter);

								
								//LOG.debug("");
								QueryExecutor
										.getInstance()
										.executeQueriesAsync(
												localRule,
												remoteActivityInterpreter,
												localRule.getLogicRuleQueries(remoteActivityInterpreter),
												remoteActivityInterpreter
														.getActivitySourceName());

							}
						}
					}
				}
			}
		}
	}

	public synchronized static LogicRuleProcessor getInstance() {
		if (instance == null) {
			instance = new LogicRuleProcessor();
		}
		return instance;
	}

	/************************************************
	 * ISmartDeviceListener  (Local Smart Device)
	 */
	
	@Override
	public void fieldDeviceConnected(ASmartDevice sender, FieldDevice device) {
		LOG.debug("fieldDeviceConnected");
		
	}

	@Override
	public void fieldDeviceRemoved(ASmartDevice sender, FieldDevice device) {
		LOG.debug("fieldDeviceRemoved");
		
	}

	@Override
	public void smartDeviceReadyStateChanged(ASmartDevice sender, boolean state) {
		LOG.debug("smartDeviceReadyStateChanged");
		
	}

	@Override
	public void smartDevicesUpdated(ASmartDevice smartDevice) {
		LOG.debug("smartDevicesUpdated");
		
	}

	/*
	 * public void initForLocalSmartDevice() { localSmartDeviceListener = new
	 * SmartDeviceListener();
	 * SmartDeviceServer.getInstance().getLocalSmartDevice
	 * ().addSmartDeviceListener(localSmartDeviceListener); }
	 */

	
}
