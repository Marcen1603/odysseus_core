package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.FieldDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.ActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.AbstractActor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.AbstractLogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.ILogicRuleListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor.AbstractSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ISmartDeviceListener;

public class LogicRuleProcessor implements ISmartDeviceDictionaryListener, ISmartDeviceListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static LogicRuleProcessor instance;
	private ILogicRuleListener logicRuleListener;
	
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

		/*
		//TODO 1b:
		HashMap<String, String> activityWithSourceNameMap = newSmartDevice.getActivityViewNameOfMergedActivities();
		
		for(FieldDevice device : SmartDevicePublisher.getInstance()
				.getLocalSmartDevice().getConnectedFieldDevices()){
			if(device instanceof AbstractActor){
				AbstractActor actor = (AbstractActor)device;
				for(AbstractLogicRule rule : actor.getLogicRules()){
					for(Entry<String, String> entry : activityWithSourceNameMap.entrySet()){
						String activityName = entry.getKey();
						String activitySourceName = entry.getValue();
						
						if(rule.getActivityName().equals(activityName)){
							QueryExecutor.getInstance().addNeededSourceForImport(activitySourceName, newSmartDevice.getPeerID());
							rule.addActivitySourceName(activitySourceName);
						}
					}
				}
			}
		}
		
		
		//refreshActivitySourceMergedImport();
		*/
		
		
		HashMap<ActivityInterpreter, AbstractLogicRule> relatedActivityInterpreterWithLogicRule = getRelatedActivityNamesWithSourceName(newSmartDevice);

		importRelatedSourcesFromActivityInterpreter(relatedActivityInterpreterWithLogicRule, newSmartDevice);
		
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
			
			//TODO 1b: String activitySourceName = SmartDevicePublisher.getInstance().getLocalSmartDevice().getMergedImportedActivitiesSourceName(logicRule.getActivityName());
			
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

	protected synchronized void refreshActivitySourceMergedImport() {
		LOG.debug("\n\n ----\n refreshActivitySourceMergedImport");
		
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		
		for(ASmartDevice device : SmartDeviceDictionaryDiscovery.getInstance().getFoundSmartDeviceList()){
			for(AbstractSensor sensor : device.getConnectedSensors()){
				for(ActivityInterpreter interpreter : sensor.getActivityInterpreters()){
					for(AbstractActor actor : SmartDevicePublisher.getInstance().getLocalSmartDevice().getConnectedActors()){
						for(AbstractLogicRule rule : actor.getLogicRules()){
							if(rule.getActivityName().equals(interpreter.getActivityName())){
								String importedActivitySourceName = SmartDevicePublisher.getInstance().getLocalSmartDevice().getMergedImportedActivitiesSourceName(interpreter.getActivityName());
								
								LOG.debug("device:"+device.getPeerName()+" ActivityName:"+interpreter.getActivityName()+" importedActivitySourceName:"+importedActivitySourceName+" getMergedActivitySourceName:"+device.getMergedActivitySourceName(interpreter.getActivityName()));
								
								ArrayList<String> list;
								if(map.get(importedActivitySourceName)==null || map.get(importedActivitySourceName).size()==0){
									list = new ArrayList<>();
									list.add(device.getMergedActivitySourceName(interpreter.getActivityName()));
									
									map.put(importedActivitySourceName, list);
								}else{
									list = map.get(importedActivitySourceName);
									list.add(device.getMergedActivitySourceName(interpreter.getActivityName()));
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
	
	

	/************************************************
	 * ISmartDeviceListener  (Local Smart Device)
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
			
			//TODO: stop and remove logic rules!
			for(@SuppressWarnings("unused") AbstractLogicRule rule : actor.getLogicRules()){
				//rule.getActivityInterpretersWithRunningRules()
				
			}

			actor.removeLogicRuleListener(logicRuleListener);
		}
	}

	@Override
	public void smartDeviceReadyStateChanged(ASmartDevice sender, boolean state) {
		LOG.debug("smartDeviceReadyStateChanged");
		
	}

	@Override
	public void smartDevicesUpdated(ASmartDevice smartDevice) {
		LOG.debug("smartDevicesUpdated");
		
	}
}
