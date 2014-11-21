package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.FieldDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.ActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.IActivityInterpreterListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.AbstractActor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.ILogicRuleListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.AbstractLogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor.AbstractSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.service.SmartDeviceService;
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
				
				refreshActivitySourceMergedExport();
			}

			@Override
			public void activityInterpreterAdded(
					ActivityInterpreter activityInterpreter) {
				LOG.debug(" activityInterpreterAdded:"
						+ activityInterpreter.getActivityName());

				String activityName = activityInterpreter.getActivityName();
				//String activitySourceName = activityInterpreter
				//		.getActivitySourceName();

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
				
				refreshActivitySourceMergedExport();
			}
		};
	}
	
	private synchronized void refreshActivitySourceMergedExport() {
		LOG.debug("refreshActivitySourceMergedExport(...) map.size:");
		
		HashMap<String, ArrayList<String>> map = SmartDevicePublisher.getInstance().getLocalSmartDevice().getActivitySourceMap();
		
		for(Entry<String, ArrayList<String>> entry : map.entrySet()){
			String activityName = entry.getKey();
			ArrayList<String> activitySourceNames = entry.getValue();
			
			LOG.debug("activityName:"+activityName+" size:"+activitySourceNames.size());
			
			
			String mergedActivitySourceName = SmartDeviceService.getInstance().getLocalSmartDevice().getMergedActivitySourceName(activityName);
			String mergedActivitySourceNameQuery = "";
			mergedActivitySourceNameQuery += "#PARSER PQL\n";
			mergedActivitySourceNameQuery += "#QNAME "+mergedActivitySourceName+"_query\n";
			mergedActivitySourceNameQuery += "#ADDQUERY\n";
			mergedActivitySourceNameQuery += mergedActivitySourceName+" := ";
			
			if(activitySourceNames.size()==1){
				mergedActivitySourceNameQuery += activitySourceNames.get(0);
			}else if(activitySourceNames.size()>1){
				mergedActivitySourceNameQuery += "";
				int i = 0;
				for(String activitySource : activitySourceNames){
					if(i==0){//first
						mergedActivitySourceNameQuery += "UNION("+activitySource+",\n";
					}else if(i>0 && i < activitySourceNames.size()-1){//middle
						mergedActivitySourceNameQuery += "UNION("+activitySource+",\n";
					}else if(i<activitySourceNames.size()){//last
						mergedActivitySourceNameQuery += activitySource+")\n";
						for(int n=i;n>1;n--){
							mergedActivitySourceNameQuery += ")\n";
						}
					}
					
					i++;
				}
				
			}
			
			if(activitySourceNames.size()>=1){
				LOG.debug("----mergedActivitySourceName:"+mergedActivitySourceName+" Query: \n"+mergedActivitySourceNameQuery);
				
				try{
					QueryExecutor.getInstance().stopQueryAndRemoveViewOrStream(mergedActivitySourceName);
				}catch(Exception ex){
					LOG.error(ex.getMessage(), ex);
				}
				
				try {
					
					QueryExecutor.getInstance().executeQueryNow(mergedActivitySourceName, mergedActivitySourceNameQuery);
					QueryExecutor.getInstance().exportWhenPossibleAsync(mergedActivitySourceName);
				} catch (Exception ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}
		}
	}

	private void createLogicRuleListener() {
		logicRuleListener = new ILogicRuleListener() {
			@Override
			public void logicRuleAdded(AbstractLogicRule rule) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void logicRuleRemoved(AbstractLogicRule rule) {
				try {
					SmartDevicePublisher.getInstance().stopLogicRule(rule);
					
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

		if (device instanceof AbstractActor) {
			AbstractActor actor = (AbstractActor) device;
			actor.addLogicRuleListener(logicRuleListener);
		} else if (device instanceof AbstractSensor) {
			AbstractSensor sensor = (AbstractSensor) device;

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

		if (device instanceof AbstractActor) {
			AbstractActor actor = (AbstractActor) device;
			
			//TODO: stop and remove logic rules!
			for(@SuppressWarnings("unused") AbstractLogicRule rule : actor.getLogicRules()){
				//rule.getActivityInterpretersWithRunningRules()
				
			}

			actor.removeLogicRuleListener(logicRuleListener);
		} else if (device instanceof AbstractSensor) {
			AbstractSensor sensor = (AbstractSensor) device;
			
			//TODO: Stop activity interpreters
			for(@SuppressWarnings("unused") ActivityInterpreter interpreter : sensor.getActivityInterpreters()){
				//interpreter.getActivityInterpreterQueries(activityName)
				
			}
			
			sensor.removeActivityInterpreterListener(activityInterpreterListener);
		}
	}

	@Override
	public void smartDeviceReadyStateChanged(ASmartDevice sender, boolean state) {
		LOG.debug("smartDeviceReadyStateChanged: " + state);

	}

	@Override
	public void smartDevicesUpdated(ASmartDevice smartDevice) {
		LOG.debug("SmartDevicesUpdated: " + smartDevice.getPeerName());

		// TODO:
		
	}
}
