package de.uniol.inf.is.odysseus.peer.smarthome.server;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Actor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.SmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.server.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.peer.smarthome.server.service.SessionManagementService;

public class LogicExecutor implements
ISmartDeviceDictionaryListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static LogicExecutor instance;

	@Override
	public void smartDeviceAdded(SmartDeviceDictionaryDiscovery sender,
			SmartDevice newSmartDevice) {
		LOG.debug("smartDeviceAdded: " + newSmartDevice.getPeerID()
				+ " processLogic(...");

		processLogic(newSmartDevice);
	}

	@Override
	public void smartDeviceRemoved(SmartDeviceDictionaryDiscovery sender,
			SmartDevice remoteSmartDevice) {

		LOG.debug("SmartHomeServerPlugIn smartDeviceRemoved: "
				+ SmartHomeServerPlugIn.getLocalSmartDevice().getPeerID());

		if (isRunningLogicRule(remoteSmartDevice)) {
			removeAllLogicRules(remoteSmartDevice);
		}
	}

	@Override
	public void smartDeviceUpdated(SmartDeviceDictionaryDiscovery sender,
			SmartDevice smartDevice) {
		// LOG.debug("smartDeviceUpdated: " +
		// smartDevice.getPeerIDString());

		// TODO: Nachschauen was sich am SmartDevice geändert hat.
		// Falls Sensoren hinzugefügt oder entfernt wurden, dann müssen die
		// Logik-Regeln überprüft und anschließend ausgeführt oder entfernt
		// werden!

	}
	
	void processLogic(SmartDevice newSmartDevice) {
		/*******************************
		 * TODO: Logic processing: 1. find matches
		 *******************************/

		String smartDeviceContextName = "";
		if (SmartHomeServerPlugIn.getLocalSmartDevice() == null) {
			return;
		} else {
			smartDeviceContextName = SmartHomeServerPlugIn.getLocalSmartDevice().getContextName();
			LOG.debug("smartDeviceCtx:" + smartDeviceContextName
					+ " newSmartDeviceCtx:" + newSmartDevice.getContextName());
			// TODO: comment out next line
			smartDeviceContextName = "Office";
		}

		if (smartDeviceContextName != null
				&& newSmartDevice.getContextName().equals(
						smartDeviceContextName)) {
			for (Sensor remoteSensor : newSmartDevice.getConnectedSensors()) {
				for (String possibleActivityName : remoteSensor
						.getPossibleActivityNames()) {
					for (Actor localActor : SmartHomeServerPlugIn.getLocalSmartDevice()
							.getConnectedActors()) {
						if (localActor != null
								&& localActor.getPossibleActivityNames()
										.contains(possibleActivityName)) {

							SmartHomeServerPlugIn.addNeededSourceForImport(
									remoteSensor
											.getActivitySourceName(possibleActivityName),
									newSmartDevice.getPeerID());

							executeLogicRuleWhenPossibleAsync(
									possibleActivityName,
									(Sensor) remoteSensor, (Actor) localActor,
									newSmartDevice);
						}
					}
				}
			}
		} else {
			// The context does not matter:

		}

	}

	boolean isLogicExecutionPossible(
			String possibleActivityName, Sensor sensor, Actor actor,
			SmartDevice smartDeviceWithSensor) {
		return SmartHomeServerPlugIn.getP2PDictionary() != null
				&& SmartHomeServerPlugIn.getP2PDictionary().isImported(sensor.getRawSourceName())
				&& SmartHomeServerPlugIn.getP2PDictionary().isImported(sensor
						.getActivitySourceName(possibleActivityName))
				&& !logicRuleIsCurrentlyRunning(possibleActivityName, sensor,
						actor, smartDeviceWithSensor);
	}

	private static boolean logicRuleIsCurrentlyRunning(
			String possibleActivityName, Sensor sensor, Actor actor,
			SmartDevice smartDeviceWithSensor) {

		return LogicExecutionContainer.getInstance().isLogicRuleRunning(
				possibleActivityName, sensor, actor, smartDeviceWithSensor);
	}
	
	public static boolean isSourceNameNeddedByLogicRule(String sourceName) {
		// TODO: check correctness

		for (Actor actor : SmartHomeServerPlugIn.getLocalSmartDevice().getConnectedActors()) {
			for (String activity : actor.getPossibleActivityNames()) {
				try {
					for (Entry<String, String> map : actor
							.getLogicRuleOfActivity(activity).entrySet()) {
						String queryName = map.getKey();
						// String query = map.getValue();

						if (sourceName.equals(queryName)) {
							return true;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return false;
	}

	public boolean isSourceNameUsingByLogicRule(String sourceName) {
		return SmartHomeServerPlugIn.getP2PDictionary().isSourceNameAlreadyInUse(sourceName);
	}

	public boolean isRunningLogicRule(SmartDevice remoteSmartDevice) {
		// TODO: check correctness
		for (Sensor sensor : remoteSmartDevice.getConnectedSensors()) {
			for (Entry<String, String> entry : sensor
					.getViewForActivityInterpreterQueries().entrySet()) {
				String queryName = entry.getKey();
				if (SmartHomeServerPlugIn.getP2PDictionary().isSourceNameAlreadyInUse(queryName)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static void executeLogicRuleWhenPossibleAsync(
			final String possibleActivityName, final Sensor sensor,
			final Actor actor, final SmartDevice smartDeviceWithSensor) {
		Thread t = new Thread(new Runnable() {
			boolean ruleExecuted = false;

			@Override
			public void run() {
				String message = "executeLogicRuleAsync activity:"
						+ possibleActivityName + " sensor:" + sensor.getName()
						+ " actor:" + actor.getName() + " smartDevice:"
						+ smartDeviceWithSensor.getPeerName();
				LOG.debug(message);

				while (!ruleExecuted) {
					try {
						Thread.sleep(2000);
					} catch (Exception ex) {
					}

					try {

						if (LogicExecutor.getInstance().isLogicExecutionPossible(possibleActivityName,
								sensor, actor, smartDeviceWithSensor)) {

							executeLogicRuleNow(possibleActivityName, sensor,
									actor, smartDeviceWithSensor);
							ruleExecuted = true;
						} else {
							// LOG.debug("!isLogicExecutionPossible: " +
							// message);
							// TODO: import neccessary source:

						}
					} catch (Exception ex) {
						LOG.error(ex.getMessage(), ex);
					}
				}
			}
		});
		t.setName("Logic execution thread. smartDevice:"
				+ smartDeviceWithSensor + " activityName:"
				+ possibleActivityName + " sensor:" + sensor + " actor:"
				+ actor);
		t.setDaemon(true);
		t.start();
	}

	private static void executeLogicRuleNow(String possibleActivityName,
			Sensor sensor, Actor actor, SmartDevice smartDeviceWithSensor) {
		LOG.debug("executeLogicRule(" + possibleActivityName + "," + sensor
				+ "," + actor + "," + smartDeviceWithSensor.getPeerName() + ")");

		if (LogicExecutor.getInstance().isLogicExecutionPossible(possibleActivityName, sensor, actor,
				smartDeviceWithSensor)) {

			LOG.debug("sourceIsImported run the actor logic script now:");

			try {
				actor.setActivitySourceName(sensor
						.getActivitySourceName(possibleActivityName));

				LinkedHashMap<String, String> logicRuleQueries = actor
						.getLogicRuleOfActivity(possibleActivityName);
				for (Entry<String, String> entry : logicRuleQueries.entrySet()) {
					String viewName = entry.getKey();
					String ruleQuery = entry.getValue();

					LOG.debug("executeLogicRule(" + possibleActivityName + ","
							+ sensor + "," + actor + ") viewName:" + viewName);
					LogicExecutionContainer.getInstance().addRunningLogicRule(
							possibleActivityName, sensor, actor,
							smartDeviceWithSensor);

					executeQueryNow(viewName, ruleQuery);

				}
			} catch (Exception ex) {
				LOG.debug(ex.getMessage(), ex);
			}
		} else {
			String sName;
			if (sensor != null
					&& sensor.getActivitySourceName(possibleActivityName) != null) {
				sName = sensor.getActivitySourceName(possibleActivityName);
			} else {
				sName = "null";
			}

			LOG.debug("source is not imported or the peerID was not found, the actor logic script is not running! sourceName:"
					+ sName);
		}
	}
	
	static void executeQueryNow(String viewName, String query)
			throws Exception {
		// LOG.error("viewName:" + viewName + " query:" + query);

		Collection<Integer> queryIDs = ServerExecutorService
				.getServerExecutor().addQuery(query, "OdysseusScript",
						SessionManagementService.getActiveSession(),
						"Standard", Context.empty());
		Integer queryId;

		if (queryIDs == null) {
			LOG.debug("queryIDs==null @viewName:" + viewName);
			return;
		}

		if (!queryIDs.iterator().hasNext()) {
			LOG.debug("!queryIDs.iterator().hasNext() @viewName:" + viewName);
			return;
		}

		queryId = queryIDs.iterator().next();
		IPhysicalQuery physicalQuery = ServerExecutorService
				.getServerExecutor().getExecutionPlan().getQueryById(queryId);
		ILogicalQuery logicalQuery = physicalQuery.getLogicalQuery();
		logicalQuery.setName(viewName);
		logicalQuery.setParserId("P2P");
		logicalQuery.setUser(SessionManagementService.getActiveSession());
		logicalQuery.setQueryText("Exporting " + viewName);

		LOG.debug("QueryId:" + queryId + " viewName:" + viewName);

		/*
		 * exportSource: try { p2pDictionary.exportSource(viewName); } catch
		 * (PeerException ex) { LOG.error(ex.getMessage(), ex); }
		 */
	}
	
	public void removeAllLogicRules(SmartDevice remoteSmartDevice) {
		for (Sensor sensor : remoteSmartDevice.getConnectedSensors()) {
			/*
			// iterate backward:
			ListIterator<String> iterator = new ArrayList<String>(sensor
					.getViewForActivityInterpreterQueries().keySet())
					.listIterator(sensor.getViewForActivityInterpreterQueries()
							.size());
			while (iterator.hasPrevious()) {
				String queryName = iterator.previous();

				stopQuery(queryName);
			}

			// iterate backward:
			ListIterator<String> iteratorRawValues = new ArrayList<String>(
					sensor.getQueriesForRawValues().keySet())
					.listIterator(sensor.getQueriesForRawValues().size());
			while (iteratorRawValues.hasPrevious()) {
				String queryName = iteratorRawValues.previous();

				stopQuery(queryName);
			}*/
			
			for(String possibleActivity : sensor.getPossibleActivityNames()){
				stopQuery(sensor.getActivitySourceName(possibleActivity));
				stopQuery(sensor.getActivitySourceName(possibleActivity)+"_query");
			}
		}
		LogicExecutionContainer.getInstance().removeLogicRules(
				remoteSmartDevice);
	}

	void stopQuery(String queryName) {
		LOG.debug("stop and removeQueryName:" + queryName);

		// TODO: check correctness
		ServerExecutorService.getServerExecutor().removeQuery(
				queryName + "_query",
				SessionManagementService.getActiveSession());

		// ServerExecutorService.getServerExecutor().
	}
	
	public static LogicExecutor getInstance() {
		if(instance==null){
			instance = new LogicExecutor();
		}
		return instance;
	}
}
