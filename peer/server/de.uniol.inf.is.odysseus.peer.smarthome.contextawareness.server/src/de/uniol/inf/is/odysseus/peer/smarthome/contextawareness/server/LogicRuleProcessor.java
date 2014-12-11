package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.FieldDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.AbstractActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.AbstractActor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.AbstractLogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.ILogicRuleListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor.AbstractSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.service.SmartDeviceService;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ISmartDeviceListener;

public class LogicRuleProcessor implements ISmartDeviceDictionaryListener,
		ISmartDeviceListener, IP2PDictionaryListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static LogicRuleProcessor instance;
	private ILogicRuleListener logicRuleListener;
	private HashMap<String, ArrayList<String>> activitySourceMap;

	private LogicRuleProcessor() {
		createLogicRuleListener();
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

		//
		boolean doit = updateLogicRuleWithActivityInterpreters(newSmartDevice);

		if (doit) {
			updateActivitySourceUnionImports();
		}

		// //
		for (AbstractLogicRule rule : SmartDevicePublisher.getInstance()
				.getLocalSmartDevice().getLogicRules()) {
			startLogicRuleAsync(rule);
		}

		// execute logic rules with activity source name:

		/*
		 * //TODO 1b: HashMap<String, String> activityWithSourceNameMap =
		 * newSmartDevice.getActivityViewNameOfMergedActivities();
		 * 
		 * for(FieldDevice device : SmartDevicePublisher.getInstance()
		 * .getLocalSmartDevice().getConnectedFieldDevices()){ if(device
		 * instanceof AbstractActor){ AbstractActor actor =
		 * (AbstractActor)device; for(AbstractLogicRule rule :
		 * actor.getLogicRules()){ for(Entry<String, String> entry :
		 * activityWithSourceNameMap.entrySet()){ String activityName =
		 * entry.getKey(); String activitySourceName = entry.getValue();
		 * 
		 * if(rule.getActivityName().equals(activityName)){
		 * QueryExecutor.getInstance
		 * ().addNeededSourceForImport(activitySourceName,
		 * newSmartDevice.getPeerID());
		 * rule.addActivitySourceName(activitySourceName); } } } } }
		 * 
		 * 
		 * //refreshActivitySourceMergedImport();
		 */

		/*
		 * HashMap<ActivityInterpreter, AbstractLogicRule>
		 * relatedActivityInterpreterWithLogicRule =
		 * getRelatedActivityNamesWithSourceName(newSmartDevice);
		 * 
		 * importRelatedSourcesFromActivityInterpreter(
		 * relatedActivityInterpreterWithLogicRule, newSmartDevice);
		 * 
		 * insertActivitySourceNamesToLogicRules(newSmartDevice);
		 * 
		 * executeRelatedLogicRules(newSmartDevice);
		 */
	}

	private boolean updateLogicRuleWithActivityInterpreters(ASmartDevice newSmartDevice) {
		HashMap<AbstractActivityInterpreter, AbstractLogicRule> relatedActivityInterpreterWithLogicRule = getRelatedActivityNamesWithSourceName(newSmartDevice);
		importRelatedSourcesFromActivityInterpreter(
				relatedActivityInterpreterWithLogicRule, newSmartDevice);

		// //
		boolean doit = false;
		for (AbstractSensor sensor : newSmartDevice.getConnectedSensors()) {
			for (AbstractActivityInterpreter interpreter : sensor
					.getActivityInterpreters()) {
				addActivitySourceToMap(interpreter.getActivityName(),
						interpreter.getActivitySourceName());
				doit = true;
			}
		}
		return doit;
	}

	private void updateActivitySourceUnionImports() {
		for (Entry<String, ArrayList<String>> entry : getActivitySourceMap()
				.entrySet()) {
			String activityName = entry.getKey();
			ArrayList<String> activitySourceNames = entry.getValue();

			LOG.debug("activityName:" + activityName + " size:"
					+ activitySourceNames.size());

			String importedMergedActivitySourceName = SmartDeviceService
					.getInstance().getLocalSmartDevice()
					.getMergedImportedActivitiesSourceName(activityName);
			try {
				QueryExecutor.getInstance().stopQueryAndRemoveViewOrStream(
						importedMergedActivitySourceName);
			} catch (Exception ex) {
				LOG.error(ex.getMessage(), ex);
			}

			runQueriesWhenPossibleAsync(activityName, activitySourceNames);
		}
	}

	private static void startLogicRuleAsync(AbstractLogicRule rule) {
		
		String importedMergedActivitySourceName = SmartDeviceService
				.getInstance()
				.getLocalSmartDevice()
				.getMergedImportedActivitiesSourceName(
						rule.getActivityName());
		if (importedMergedActivitySourceName != null
				&& !importedMergedActivitySourceName.equals("")) {
			LOG.debug("rule:\"" + rule.getReactionDescription()
					+ "\" addActivitySourceName:"
					+ importedMergedActivitySourceName);
			rule.setActivitySourceName(importedMergedActivitySourceName);

			for (Entry<String, String> entry : rule
					.getLogicRulesQueriesWithActivitySourceName()
					.entrySet()) {
				String queryName = entry.getKey();
				String query = entry.getValue();

				LOG.debug("executeQueryAsync queryName:" + queryName
						+ " query:" + query + " waitForSource:"
						+ importedMergedActivitySourceName);

				try {
					QueryExecutor.getInstance().executeQueryAsync(
							queryName, query,
							importedMergedActivitySourceName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void runQueriesWhenPossibleAsync(final String activityName,
			final ArrayList<String> activitySourceNames) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				waitForSources(activitySourceNames);

				runQueriesNow(activityName, activitySourceNames);
			}
		});
		t.setName("runQueriesWhenPossibleAsync Imported union thread.");
		t.setDaemon(true);
		t.start();
	}

	protected void waitForSources(ArrayList<String> activitySourceNames) {
		boolean check = false;
		while (!check) {
			check = true;
			for (String source : activitySourceNames) {
				if (!QueryExecutor.getInstance().isImportedSource(source)) {
					check = false;
					break;
				}
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	private static void runQueriesNow(String activityName,
			ArrayList<String> activitySourceNames) {
		String importedMergedActivitySourceName = SmartDeviceService
				.getInstance().getLocalSmartDevice()
				.getMergedImportedActivitiesSourceName(activityName);

		String mergedActivitySourceNameQuery = "";
		mergedActivitySourceNameQuery += "#PARSER PQL\n";
		mergedActivitySourceNameQuery += "#QNAME "
				+ importedMergedActivitySourceName + "_query\n";
		mergedActivitySourceNameQuery += "#ADDQUERY\n";
		mergedActivitySourceNameQuery += importedMergedActivitySourceName
				+ " := ";

		if (activitySourceNames.size() == 1) {
			mergedActivitySourceNameQuery += activitySourceNames.get(0);
		} else if (activitySourceNames.size() > 1) {
			mergedActivitySourceNameQuery += "";
			int i = 0;
			for (String activitySource : activitySourceNames) {
				if (i == 0) {// first
					mergedActivitySourceNameQuery += "UNION(" + activitySource
							+ ",\n";
				} else if (i > 0 && i < activitySourceNames.size() - 1) {// middle
					mergedActivitySourceNameQuery += "UNION(" + activitySource
							+ ",\n";
				} else if (i < activitySourceNames.size()) {// last
					mergedActivitySourceNameQuery += activitySource + ")\n";
					for (int n = i; n > 1; n--) {
						mergedActivitySourceNameQuery += ")\n";
					}
				}

				i++;
			}
		}

		if (activitySourceNames.size() >= 1) {
			LOG.debug("----Imported MergedActivitySourceName:"
					+ importedMergedActivitySourceName + " Query: \n"
					+ mergedActivitySourceNameQuery);

			try {
				QueryExecutor.getInstance().stopQueryAndRemoveViewOrStream(
						importedMergedActivitySourceName);
			} catch (Exception ex) {
				LOG.error(ex.getMessage(), ex);
			}

			try {
				QueryExecutor.getInstance().executeQueryNow(
						importedMergedActivitySourceName,
						mergedActivitySourceNameQuery);

			} catch (Exception ex) {
				LOG.error(ex.getMessage(), ex);
			}
		}
	}

	private synchronized void addActivitySourceToMap(String activityName,
			String activitySourceName) {
		if (getActivitySourceMap().get(activityName) != null) {// this activity
																// exist!
			ArrayList<String> list = getActivitySourceMap().get(activityName);
			if (list == null) {
				list = new ArrayList<String>();
			}

			if (!list.contains(activitySourceName)) {
				list.add(activitySourceName);
			}

			getActivitySourceMap().put(activityName, list);
		} else { // new activity
			ArrayList<String> list = new ArrayList<String>();
			list.add(activitySourceName);
			getActivitySourceMap().put(activityName, list);
		}
	}

	/**
	 * 
	 * return HashMap<String, ArrayList<String>> (HashMap<ActivityName,
	 * ArrayList<ActivitySourceName>>)
	 */
	private synchronized HashMap<String, ArrayList<String>> getActivitySourceMap() {
		if (activitySourceMap == null) {
			activitySourceMap = new HashMap<String, ArrayList<String>>();
		}
		return activitySourceMap;
	}

	private static HashMap<AbstractActivityInterpreter, AbstractLogicRule> getRelatedActivityNamesWithSourceName(
			ASmartDevice newSmartDevice) {
		HashMap<AbstractActivityInterpreter, AbstractLogicRule> map = new HashMap<>();

		String smartDeviceContextName = "";
		if (SmartDevicePublisher.getInstance().getLocalSmartDevice() == null) {
			return map;
		}
		
		smartDeviceContextName = SmartDevicePublisher.getInstance()
				.getLocalSmartDevice().getContextName();
		LOG.debug("smartDeviceCtx:" + smartDeviceContextName
				+ " newSmartDeviceCtx:" + newSmartDevice.getContextName());
		// TODO: comment out next line
		smartDeviceContextName = "Office";
		

		if (newSmartDevice.getContextName().equals(
						smartDeviceContextName)) {
			for (AbstractSensor remoteSensor : newSmartDevice
					.getConnectedSensors()) {
				for (AbstractActivityInterpreter activityInterpreter : remoteSensor
						.getActivityInterpreters()) {
					for (AbstractActor connectedActor : SmartDevicePublisher
							.getInstance().getLocalSmartDevice()
							.getConnectedActors()) {
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

	@SuppressWarnings("unused")
	private static void importRelatedSourcesFromActivityInterpreter(
			HashMap<AbstractActivityInterpreter, AbstractLogicRule> relatedActivityInterpreterWithLogicRule,
			ASmartDevice newSmartDevice) {

		for (Entry<AbstractActivityInterpreter, AbstractLogicRule> entry : relatedActivityInterpreterWithLogicRule
				.entrySet()) {
			AbstractActivityInterpreter inter = entry.getKey();

			QueryExecutor.getInstance().addNeededSourceForImport(
					inter.getActivitySourceName(),
					inter.getSensor().getSmartDevice().getPeerID());
		}
	}

	@SuppressWarnings("unused")
	private static void insertActivitySourceNamesToLogicRules(
			ASmartDevice newSmartDevice) {
		HashMap<AbstractActivityInterpreter, AbstractLogicRule> relatedSensorsWithActors = getRelatedSensorsWithActorsWithLogic(newSmartDevice);
		for (Entry<AbstractActivityInterpreter, AbstractLogicRule> entry : relatedSensorsWithActors
				.entrySet()) {
			AbstractActivityInterpreter activityInterpreter = entry.getKey();
			AbstractLogicRule logicRule = entry.getValue();

			String activitySourceName = activityInterpreter
					.getActivitySourceName();

			// TODO 1b: String activitySourceName =
			// SmartDevicePublisher.getInstance().getLocalSmartDevice().getMergedImportedActivitiesSourceName(logicRule.getActivityName());

			logicRule.addActivitySourceName(activitySourceName);
		}
	}

	private static HashMap<AbstractActivityInterpreter, AbstractLogicRule> getRelatedSensorsWithActorsWithLogic(
			ASmartDevice newSmartDevice) {
		HashMap<AbstractActivityInterpreter, AbstractLogicRule> map = new HashMap<AbstractActivityInterpreter, AbstractLogicRule>();

		String smartDeviceContextName = "";
		if (SmartDevicePublisher.getInstance().getLocalSmartDevice() == null) {
			return map;
		}
		
		smartDeviceContextName = SmartDevicePublisher.getInstance()
				.getLocalSmartDevice().getContextName();
		LOG.debug("smartDeviceCtx:" + smartDeviceContextName
				+ " newSmartDeviceCtx:" + newSmartDevice.getContextName());
		// TODO: comment out next line
		smartDeviceContextName = "Office";
		

		if (newSmartDevice.getContextName().equals(
						smartDeviceContextName)) {

			fillMapWithRelatedActivityInterpreterAndLogicRules(newSmartDevice,
					map);
		}

		return map;
	}

	private static void fillMapWithRelatedActivityInterpreterAndLogicRules(
			ASmartDevice newSmartDevice,
			HashMap<AbstractActivityInterpreter, AbstractLogicRule> map) {
		for (AbstractSensor remoteSensor : newSmartDevice.getConnectedSensors()) {
			for (AbstractActivityInterpreter activityInterpreter : remoteSensor
					.getActivityInterpreters()) {
				for (AbstractActor localActor : SmartDevicePublisher
						.getInstance().getLocalSmartDevice()
						.getConnectedActors()) {
					for (AbstractLogicRule logicRule : localActor
							.getLogicRules()) {
						if (activityInterpreter.getActivityName().equals(
								logicRule.getActivityName())) {

							LOG.debug("activityInterpreter:"
									+ activityInterpreter.getActivityName()
									+ " logicRuleActivityName:"
									+ logicRule.getActivityName()
									+ " sensorPeerName:"
									+ activityInterpreter.getSensor()
											.getSmartDevice().getPeerName()
									+ " ruleDesc:"
									+ logicRule.getReactionDescription()
									+ " actDesc:"
									+ activityInterpreter
											.getActivityInterpreterDescription());

							// TODO: !?
							// String activitySourceName = activityInterpreter
							// .getActivitySourceName();
							// logicRule.addActivitySourceName(activitySourceName);

							map.put(activityInterpreter, logicRule);
						}
					}
				}
			}
		}

		LOG.debug("map.size():" + map.size());
	}

	private static void removeRelevantLogicRules(ASmartDevice removedSmartDevice) {
		LOG.debug("removeRelevantLogicRules");
		stopRunningLogicRules(removedSmartDevice);
		removeMarkerForSourceNeedToImport(removedSmartDevice);
	}

	private static void stopRunningLogicRules(ASmartDevice removedSmartDevice) {
		LOG.debug("stopRunningLogicRules");
		QueryExecutor.getInstance().removeAllLogicRules(removedSmartDevice);
	}

	private static void removeMarkerForSourceNeedToImport(
			ASmartDevice removedSmartDevice) {
		LOG.debug("removeMarkerForSourceNeedToImport:"
				+ removedSmartDevice.getPeerName());
		for (AbstractSensor remoteSensor : removedSmartDevice
				.getConnectedSensors()) {
			for (AbstractActivityInterpreter activityInterpreter : remoteSensor
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

	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
	private static void executeRelatedLogicRules(ASmartDevice newSmartDevice) {
		LOG.debug("executeRelatedLogicRules:" + newSmartDevice.getPeerName());

		for (AbstractActor localActor : SmartDevicePublisher.getInstance()
				.getLocalSmartDevice().getConnectedActors()) {
			for (AbstractLogicRule localRule : localActor.getLogicRules()) {
				for (String activitySourceNameLogicRule : localRule
						.getActivitySourceNameList()) {
					for (AbstractSensor remoteSensor : newSmartDevice
							.getConnectedSensors()) {
						for (AbstractActivityInterpreter remoteActivityInterpreter : remoteSensor
								.getActivityInterpreters()) {
							if (remoteActivityInterpreter
									.getActivitySourceName().equals(
											activitySourceNameLogicRule)) {
								// match: logicRule with ActivityInterpreter

								// Add activityInterpreter and this adds
								// activitySourceName to LogicRule:
								localRule
										.addActivityInterpreter(remoteActivityInterpreter);

								// LOG.debug("");
								QueryExecutor
										.getInstance()
										.executeQueriesAsync(
												localRule,
												remoteActivityInterpreter,
												localRule
														.getLogicRuleQueries(remoteActivityInterpreter),
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

	protected synchronized void refreshActivitySourceMergedImport() {
		LOG.debug("\n\n ----\n refreshActivitySourceMergedImport");

		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

		for (ASmartDevice device : SmartDeviceDiscovery.getInstance()
				.getFoundSmartDeviceList()) {
			for (AbstractSensor sensor : device.getConnectedSensors()) {
				for (AbstractActivityInterpreter interpreter : sensor
						.getActivityInterpreters()) {
					for (AbstractActor actor : SmartDevicePublisher
							.getInstance().getLocalSmartDevice()
							.getConnectedActors()) {
						for (AbstractLogicRule rule : actor.getLogicRules()) {
							if (rule.getActivityName().equals(
									interpreter.getActivityName())) {
								String importedActivitySourceName = SmartDevicePublisher
										.getInstance()
										.getLocalSmartDevice()
										.getMergedImportedActivitiesSourceName(
												interpreter.getActivityName());

								LOG.debug("device:"
										+ device.getPeerName()
										+ " ActivityName:"
										+ interpreter.getActivityName()
										+ " importedActivitySourceName:"
										+ importedActivitySourceName
										+ " getMergedActivitySourceName:"
										+ device.getMergedActivitySourceName(interpreter
												.getActivityName()));

								ArrayList<String> list;
								if (map.get(importedActivitySourceName) == null
										|| map.get(importedActivitySourceName)
												.size() == 0) {
									list = new ArrayList<>();
									list.add(device
											.getMergedActivitySourceName(interpreter
													.getActivityName()));

									map.put(importedActivitySourceName, list);
								} else {
									list = map.get(importedActivitySourceName);
									list.add(device
											.getMergedActivitySourceName(interpreter
													.getActivityName()));
								}
							}
						}
					}
				}
			}
		}

	}

	private void createLogicRuleListener() {
		logicRuleListener = new ILogicRuleListener() {
			@Override
			public void logicRuleAdded(AbstractLogicRule rule) {
				LOG.debug("logicRuleAdded");

				refreshActivitySourceMergedImport();

				processLogic(rule.getActor().getSmartDevice());

				importSourcesIfKnown(rule);
			}

			@Override
			public void logicRuleRemoved(AbstractLogicRule rule) {
				LOG.debug("logicRuleRemoved");

				try {
					SmartDevicePublisher.getInstance().stopLogicRule(rule);

					System.out.println("LogicProcessor() logicRuleRemoved: "
							+ rule.getActivityName() + " Actor:"
							+ rule.getActor().getName() + "");
				} catch (Exception ex) {
					System.out
							.println("LogicProcessor() logicRuleRemoved: something null...");
				}

				refreshActivitySourceMergedImport();
			}
		};
	}

	protected void importSourcesIfKnown(AbstractLogicRule rule) {
		LOG.debug("importSourcesIfKnown for activity:" + rule.getActivityName());

		ArrayList<ASmartDevice> foundSmartDevices = SmartDeviceDiscovery
				.getInstance().getFoundSmartDeviceList();

		for (ASmartDevice device : foundSmartDevices) {
			LOG.debug("_device:" + device.getPeerName());
			for (AbstractSensor sensor : device.getConnectedSensors()) {
				for (AbstractActivityInterpreter interpreter : sensor
						.getActivityInterpreters()) {
					if (interpreter.getActivityName().equals(
							rule.getActivityName())) {
						LOG.debug("__interpreter.getActivityName().equals(rule.getActivityName():"
								+ interpreter.getActivityName());

						QueryExecutor.getInstance().addNeededSourceForImport(
								interpreter.getActivitySourceName(),
								interpreter.getSensor().getSmartDevice()
										.getPeerID());

						Collection<SourceAdvertisement> publishedSources = SmartHomeServerPlugIn
								.getP2PDictionary().getSources();
						for (SourceAdvertisement publishedSource : publishedSources) {
							LOG.debug("___publishedSource:"
									+ publishedSource.getName());
							if (publishedSource.getName().equals(
									interpreter.getActivitySourceName())) {

								LOG.debug("____importSourcesIfKnown sourceName:"
										+ publishedSource.getName());

								QueryExecutor
										.getInstance()
										.addNeededSourceForImport(
												interpreter
														.getActivitySourceName(),
												publishedSource.getPeerID()
														.intern().toString());
								QueryExecutor.getInstance()
										.importOrRemoveIfNeccessary(
												publishedSource);
							}
						}
					}
				}
			}
		}

	}

	/************************************************
	 * ISmartDeviceListener (Local Smart Device)
	 */

	@Override
	public void fieldDeviceConnected(ASmartDevice sender, FieldDevice device) {
		LOG.debug("fieldDeviceConnected");

		if (device instanceof AbstractActor) {
			AbstractActor actor = (AbstractActor) device;
			actor.addLogicRuleListener(logicRuleListener);
		}
	}

	@Override
	public void fieldDeviceRemoved(ASmartDevice sender, FieldDevice device) {
		LOG.debug("fieldDeviceRemoved");

		if (device instanceof AbstractActor) {
			AbstractActor actor = (AbstractActor) device;

			for(AbstractLogicRule rule : actor.getLogicRules()) {
				stopLogicRuleAsync(rule);
			}

			actor.removeLogicRuleListener(logicRuleListener);
		}
	}

	@Override
	public void smartDeviceReadyStateChanged(ASmartDevice sender, boolean state) {
		LOG.debug("smartDeviceReadyStateChanged");

	}

	@Override
	public void smartDevicesUpdated(ASmartDevice newSmartDevice,
			ASmartDevice oldSmartDevice) {
		LOG.debug("smartDevicesUpdated ");

		if (isLocalPeer(newSmartDevice)) {
			// start new logic rules:
			ArrayList<AbstractLogicRule> newRules = getNewLogicRules(
					newSmartDevice, oldSmartDevice);
			for (AbstractLogicRule rule : newRules) {
				LOG.debug("start new logic rule:" + rule.getActivityName()
						+ " desc:" + rule.getReactionDescription());

				// TODO:
				
				for(ASmartDevice smartDevice : SmartDeviceDiscovery.getInstance().getFoundSmartDeviceList()){
					if(updateLogicRuleWithActivityInterpreters(smartDevice)){
						updateActivitySourceUnionImports();
					}
				}
				
				startLogicRuleAsync(rule);
			}

			// stop removed logic rules:
			ArrayList<AbstractLogicRule> removedRules = getRemovedLogicRules(
					newSmartDevice, oldSmartDevice);
			for (AbstractLogicRule rule : removedRules) {
				LOG.debug("stop removed logic rule:" + rule.getActivityName()
						+ " desc:" + rule.getReactionDescription());

				// TODO:
				stopLogicRuleAsync(rule);
			}
		}
	}

	@SuppressWarnings("unused")
	private static void stopLogicRuleAsync(AbstractLogicRule rule) {
		// TODO Auto-generated method stub
		LOG.debug("stopLogicRuleAsync() -> currently not implemented!");
		
		
	}

	private static boolean isLocalPeer(ASmartDevice newSmartDevice) {
		return SmartDeviceService.getInstance().getLocalSmartDevice()
				.equals(newSmartDevice);
	}

	private static ArrayList<AbstractLogicRule> getRemovedLogicRules(
			ASmartDevice newSmartDevice, ASmartDevice oldSmartDevice) {
		ArrayList<AbstractLogicRule> list = new ArrayList<>();

		getLeftIntersection(oldSmartDevice, newSmartDevice, list);

		return list;
	}

	private static ArrayList<AbstractLogicRule> getNewLogicRules(
			ASmartDevice newSmartDevice, ASmartDevice oldSmartDevice) {
		ArrayList<AbstractLogicRule> list = new ArrayList<>();

		getLeftIntersection(newSmartDevice, oldSmartDevice, list);

		return list;
	}

	private static void getLeftIntersection(ASmartDevice leftSmartDevice,
			ASmartDevice rightSmartDevice, ArrayList<AbstractLogicRule> list) {
		for (AbstractActor actor : leftSmartDevice.getConnectedActors()) {
			for (AbstractActor rightActor : rightSmartDevice
					.getConnectedActors()) {
				for (AbstractLogicRule leftRule : actor.getLogicRules()) {

					boolean ruleExist = false;
					for (AbstractLogicRule rightRule : rightActor
							.getLogicRules()) {
						if (leftRule.equals(rightRule)) {
							ruleExist = true;
							break;
						}
					}

					if (!ruleExist && !list.contains(leftRule)) {
						list.add(leftRule);
					}
				}
			}
		}
	}

	/******
	 * IP2PDictionaryListener:
	 */
	@Override
	public void sourceAdded(IP2PDictionary sender,
			SourceAdvertisement advertisement) {
		LOG.debug("sourceAdded");

	}

	@Override
	public void sourceRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement) {
		LOG.debug("sourceRemoved");
	}

	@Override
	public void sourceImported(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceImported");

		// TODO: start relevant Queries
		LOG.debug("start relevant queries. currently not implemented!");
		
		
	}

	@Override
	public void sourceImportRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceImportRemoved");
	}

	@Override
	public void sourceExported(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceExported");
	}

	@Override
	public void sourceExportRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceExportRemoved");
	}
}
