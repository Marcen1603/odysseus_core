package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import java.util.HashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.Actor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.LogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.Sensor;

public class LogicProcessor implements ISmartDeviceDictionaryListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static LogicProcessor instance;

	private LogicProcessor() {

	}

	/************************************************
	 * ISmartDeviceDictionaryListener
	 */
	@Override
	public void smartDeviceAdded(SmartDeviceServer sender,
			ASmartDevice newSmartDevice) {
		LOG.debug("smartDeviceAdded: " + newSmartDevice.getPeerID()
				+ " processLogic(" + newSmartDevice.getPeerName());

		processLogic(newSmartDevice);
	}

	@Override
	public void smartDeviceRemoved(SmartDeviceServer sender,
			ASmartDevice removedSmartDevice) {
		LOG.debug("smartDeviceRemoved: " + removedSmartDevice.getPeerName());
		// TODO: beide methoden vergleichen!!!
		removeRelevantLogicRules(removedSmartDevice);
		QueryExecutor.getInstance().removeAllLogicRules(removedSmartDevice);
	}

	@Override
	public void smartDeviceUpdated(SmartDeviceServer sender,
			ASmartDevice updatedSmartDevice) {
		updateLogicRulesIfNeeded(updatedSmartDevice);
	}

	/*******************************
	 * Logic processing:
	 *******************************/
	void processLogic(ASmartDevice newSmartDevice) {
		LOG.debug("processLogic for newSmartDevice:"
				+ newSmartDevice.getPeerName());

		HashMap<ActivityInterpreter, LogicRule> relatedActivityInterpreterWithLogicRule = getRelatedActivityNamesWithSourceName(newSmartDevice);

		importRelatedSourcesFromActivityInterpreter(
				relatedActivityInterpreterWithLogicRule, newSmartDevice);

		insertActivitySourceNamesToLogicRules(newSmartDevice);

		executeRelatedLogicRules(newSmartDevice);
	}

	private HashMap<ActivityInterpreter, LogicRule> getRelatedActivityNamesWithSourceName(
			ASmartDevice newSmartDevice) {
		HashMap<ActivityInterpreter, LogicRule> map = new HashMap<>();

		String smartDeviceContextName = "";
		if (SmartDeviceServer.getInstance().getLocalSmartDevice() == null) {
			return map;
		} else {
			smartDeviceContextName = SmartDeviceServer.getInstance()
					.getLocalSmartDevice().getContextName();
			LOG.debug("smartDeviceCtx:" + smartDeviceContextName
					+ " newSmartDeviceCtx:" + newSmartDevice.getContextName());
			// TODO: comment out next line
			smartDeviceContextName = "Office";
		}

		if (smartDeviceContextName != null
				&& newSmartDevice.getContextName().equals(
						smartDeviceContextName)) {
			for (Sensor remoteSensor : newSmartDevice.getConnectedSensors()) {
				for (ActivityInterpreter activityInterpreter : remoteSensor
						.getActivityInterpreters()) {
					for (Actor connectedActor : SmartDeviceServer.getInstance()
							.getLocalSmartDevice().getConnectedActors()) {
						for (LogicRule logicRule : connectedActor
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
			HashMap<ActivityInterpreter, LogicRule> relatedActivityInterpreterWithLogicRule,
			ASmartDevice newSmartDevice) {

		for (Entry<ActivityInterpreter, LogicRule> entry : relatedActivityInterpreterWithLogicRule
				.entrySet()) {
			ActivityInterpreter inter = entry.getKey();

			QueryExecutor.getInstance().addNeededSourceForImport(
					inter.getActivitySourceName(),
					inter.getSensor().getSmartDevice().getPeerID());
		}
	}

	private void insertActivitySourceNamesToLogicRules(
			ASmartDevice newSmartDevice) {
		HashMap<ActivityInterpreter, LogicRule> relatedSensorsWithActors = getRelatedSensorsWithActorsWithLogic(newSmartDevice);
		for (Entry<ActivityInterpreter, LogicRule> entry : relatedSensorsWithActors
				.entrySet()) {
			ActivityInterpreter activityInterpreter = entry.getKey();
			LogicRule logicRule = entry.getValue();

			String activitySourceName = activityInterpreter
					.getActivitySourceName();
			logicRule.addActivitySourceName(activitySourceName);
		}
	}

	private HashMap<ActivityInterpreter, LogicRule> getRelatedSensorsWithActorsWithLogic(
			ASmartDevice newSmartDevice) {
		HashMap<ActivityInterpreter, LogicRule> map = new HashMap<ActivityInterpreter, LogicRule>();

		String smartDeviceContextName = "";
		if (SmartDeviceServer.getInstance().getLocalSmartDevice() == null) {
			return map;
		} else {
			smartDeviceContextName = SmartDeviceServer.getInstance()
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
			HashMap<ActivityInterpreter, LogicRule> map) {
		for (Sensor remoteSensor : newSmartDevice.getConnectedSensors()) {
			for (ActivityInterpreter activityInterpreter : remoteSensor
					.getActivityInterpreters()) {
				for (Actor localActor : SmartDeviceServer.getInstance()
						.getLocalSmartDevice().getConnectedActors()) {
					for (LogicRule logicRule : localActor.getLogicRules()) {
						if (activityInterpreter.getActivityName().equals(
								logicRule.getActivityName())) {
							LOG.debug("activityInterpreter,logicRule activityName:"
									+ logicRule.getActivityName());
							map.put(activityInterpreter, logicRule);
						}
					}
				}
			}
		}
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
		for (Sensor remoteSensor : removedSmartDevice.getConnectedSensors()) {
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
		for (Actor localActor : SmartDeviceServer.getInstance()
				.getLocalSmartDevice().getConnectedActors()) {
			for (LogicRule rule : localActor.getLogicRules()) {
				for (String activitySourceNameLogicRule : rule
						.getActivitySourceNameList()) {
					for (Sensor sensor : newSmartDevice.getConnectedSensors()) {
						for (ActivityInterpreter activityInterpreter : sensor
								.getActivityInterpreters()) {
							if (activityInterpreter.getActivitySourceName()
									.equals(activitySourceNameLogicRule)) {
								// match: logicRule with ActivityInterpreter

								// Add activityInterpreter and this adds
								// activitySourceName to LogicRule:
								rule.addActivityInterpreter(activityInterpreter);

								QueryExecutor
										.getInstance()
										.executeQueriesAsync(
												rule,
												activityInterpreter,
												rule.getLogicRuleQueries(activityInterpreter),
												activityInterpreter
														.getActivitySourceName());

							}
						}
					}
				}
			}
		}
	}

	public static LogicProcessor getInstance() {
		if (instance == null) {
			instance = new LogicProcessor();
		}
		return instance;
	}

	/*
	 * public void initForLocalSmartDevice() { localSmartDeviceListener = new
	 * SmartDeviceListener();
	 * SmartDeviceServer.getInstance().getLocalSmartDevice
	 * ().addSmartDeviceListener(localSmartDeviceListener); }
	 */

	void executeActivityInterpreterQueries(ASmartDevice smartDevice) {
		for (Sensor sensor : smartDevice.getConnectedSensors()) {
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

			for (ActivityInterpreter activityInterpreter : sensor
					.getActivityInterpreters()) {
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

				try {
					QueryExecutor.getInstance().exportWhenPossibleAsync(
							activitySourceName);
				} catch (Exception ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}
		}
	}
}
