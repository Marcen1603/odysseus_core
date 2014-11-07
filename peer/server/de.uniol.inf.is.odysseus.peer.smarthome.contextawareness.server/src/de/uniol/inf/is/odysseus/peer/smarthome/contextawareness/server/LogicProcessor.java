package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import java.util.HashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.Actor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.FieldDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.IFieldDeviceListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ISmartDeviceListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.LogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.Sensor;

public class LogicProcessor implements ISmartDeviceDictionaryListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static LogicProcessor instance;
	private SmartDeviceListener localSmartDeviceListener;
	
	private LogicProcessor() {
		
	}

	/************************************************
	 * ISmartDeviceDictionaryListener
	 */
	@Override
	public void smartDeviceAdded(SmartDeviceServerDictionaryDiscovery sender,
			ASmartDevice newSmartDevice) {
		LOG.debug("smartDeviceAdded: " + newSmartDevice.getPeerID()
				+ " processLogic("+newSmartDevice.getPeerName());

		processLogic(newSmartDevice);
	}

	@Override
	public void smartDeviceRemoved(SmartDeviceServerDictionaryDiscovery sender,
			ASmartDevice removedSmartDevice) {
		LOG.debug("smartDeviceRemoved: " + removedSmartDevice.getPeerName());
		removeRelevantLogicRules(removedSmartDevice);
	}

	@Override
	public void smartDeviceUpdated(SmartDeviceServerDictionaryDiscovery sender,
			ASmartDevice updatedSmartDevice) {
		updateLogicRulesIfNeeded(updatedSmartDevice);
	}
	
	/*******************************
	 * Logic processing:
	 *******************************/
	void processLogic(ASmartDevice newSmartDevice) {
		LOG.debug("processLogic for newSmartDevice:"+newSmartDevice.getPeerName());

		HashMap<ActivityInterpreter, LogicRule> relatedActivityInterpreterWithLogicRule = getRelatedActivityNamesWithSourceName(newSmartDevice);

		importRelatedSourcesFromActivityInterpreter(relatedActivityInterpreterWithLogicRule, newSmartDevice);
		
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
					for(Actor connectedActor : SmartDeviceServer.getInstance()
							  .getLocalSmartDevice().getConnectedActors()){
						for(LogicRule logicRule : connectedActor.getLogicRules()){
							if(activityInterpreter.getActivityName().equals(logicRule.getActivityName())){
								LOG.debug("Logic map.put: activityInterpreter:"+activityInterpreter.getActivityName()+", logicRule:"+logicRule.getActivityName());
								
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
		
		for(Entry<ActivityInterpreter, LogicRule> entry : relatedActivityInterpreterWithLogicRule.entrySet()){
			ActivityInterpreter inter = entry.getKey();
			
			QueryExecutor.getInstance().addNeededSourceForImport(inter.getActivitySourceName(), inter.getSensor().getSmartDevice().getPeerID());
		}
	}

	private void insertActivitySourceNamesToLogicRules(ASmartDevice newSmartDevice) {
		HashMap<ActivityInterpreter, LogicRule> relatedSensorsWithActors = getRelatedSensorsWithActorsWithLogic(newSmartDevice);
		for (Entry<ActivityInterpreter, LogicRule> entry : relatedSensorsWithActors.entrySet()) {
			ActivityInterpreter activityInterpreter = entry.getKey();
			LogicRule logicRule = entry.getValue();

			String activitySourceName = activityInterpreter.getActivitySourceName();
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
			
			fillMapWithRelatedActivityInterpreterAndLogicRules(newSmartDevice, map);
		}

		return map;
	}

	private void fillMapWithRelatedActivityInterpreterAndLogicRules(ASmartDevice newSmartDevice,
			HashMap<ActivityInterpreter, LogicRule> map) {
		for (Sensor remoteSensor : newSmartDevice.getConnectedSensors()) {
			for(ActivityInterpreter activityInterpreter : remoteSensor.getActivityInterpreters()){
				for(Actor localActor : SmartDeviceServer.getInstance()
						.getLocalSmartDevice().getConnectedActors()){
					for(LogicRule logicRule : localActor.getLogicRules()){
						if(activityInterpreter.getActivityName().equals(logicRule.getActivityName())){
							LOG.debug("activityInterpreter,logicRule activityName:"+logicRule.getActivityName());
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

	private void removeMarkerForSourceNeedToImport(ASmartDevice removedSmartDevice) {
		LOG.debug("removeMarkerForSourceNeedToImport:"+removedSmartDevice.getPeerName());
		for (Sensor remoteSensor : removedSmartDevice.getConnectedSensors()) {
			for (ActivityInterpreter activityInterpreter : remoteSensor
					.getActivityInterpreters()) {
				String srcName = activityInterpreter.getActivitySourceName();
				try {
					QueryExecutor.getInstance().removeSourceIfNeccessary(
							srcName);
					
					//QueryExecutor.getInstance().stopQuery(srcName);
					//QueryExecutor.getInstance().stopQuery(srcName + "_query");
				} catch (Exception ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}
		}
	}
	
	private void updateLogicRulesIfNeeded(ASmartDevice updatedSmartDevice) {
		// TODO:
		
	}
	
	private void executeRelatedLogicRules(ASmartDevice newSmartDevice) {
		LOG.debug("executeRelatedLogicRules:"+newSmartDevice.getPeerName());
		for(Actor localActor : SmartDeviceServer.getInstance().getLocalSmartDevice().getConnectedActors()){
			for(LogicRule rule : localActor.getLogicRules()){
				for(String activitySourceNameLogicRule : rule.getActivitySourceNameList()){
					for(Sensor sensor : newSmartDevice.getConnectedSensors()){
						for(ActivityInterpreter activityInterpreter : sensor.getActivityInterpreters()){
							if(activityInterpreter.getActivitySourceName().equals(activitySourceNameLogicRule)){
								//match: logicRule with ActivityInterpreter
								
								//Add activityInterpreter and this adds activitySourceName to LogicRule:
								rule.addActivityInterpreter(activityInterpreter);
								
								QueryExecutor.getInstance().executeQueriesAsync(rule, activityInterpreter, rule.getLogicRuleQueries(activityInterpreter), activityInterpreter.getActivitySourceName());
								
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
	
	public class SmartDeviceListener implements ISmartDeviceListener{
		@Override
		public void fieldDeviceConnected(ASmartDevice sender, FieldDevice device) {
			device.addFieldDeviceListener(new IFieldDeviceListener() {
				@Override
				public void logicRuleRemoved(LogicRule rule) {
					QueryExecutor.getInstance().stopLogicRuleAsync(rule);
					
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

	public void initForLocalSmartDevice() {
		localSmartDeviceListener = new SmartDeviceListener();
		SmartDeviceServer.getInstance().getLocalSmartDevice().addSmartDeviceListener(localSmartDeviceListener);
	}
}
