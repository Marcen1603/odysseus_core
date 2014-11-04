package de.uniol.inf.is.odysseus.peer.smarthome.server;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Actor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.SmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.server.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.peer.smarthome.server.service.SessionManagementService;

public class QueryExecutor implements IP2PDictionaryListener, ISmartDeviceDictionaryListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static QueryExecutor instance;
	private static IP2PDictionary p2pDictionary;
	private static LinkedHashMap<String, String> sourcesNeededForImport;
	
	
	/************************************************
	 * IP2PDictionaryListener
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
		LOG.debug("sourceImported sourceName:" + sourceName);

		if (advertisement.isLocal()) {
			LOG.debug("advertisement.isLocal()");
			return;
		}

	}

	@Override
	public void sourceImportRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceImportRemoved sourceName:" + sourceName
				+ " and stopQuery");

		QueryExecutor.getInstance().removeSourceNeededForImport(sourceName);
		QueryExecutor.getInstance().stopQuery(sourceName);
		QueryExecutor.getInstance().stopQuery(
				sourceName + "_query");

		if (QueryExecutor.getInstance()
				.isSourceNameUsingByLogicRule(sourceName)) {
			// TODO: Anfragen wieder entfernen:

		}
	}

	@Override
	public void sourceExported(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceExported: " + sourceName);
	}

	@Override
	public void sourceExportRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceExportRemoved: " + sourceName);
	}
	
	void executeActivityInterpreterQueries(SmartDevice smartDevice) {
		for (Sensor sensor : smartDevice.getConnectedSensors()) {
			for (Entry<String, String> queryForRawValue : sensor
					.getQueriesForRawValues().entrySet()) {
				String viewName = queryForRawValue.getKey();
				String query = queryForRawValue.getValue();

				try {
					executeQueryNow(viewName, query);

				} catch (NoSuchElementException ex) {
					LOG.error(ex.getMessage(), ex);
				} catch (Exception ex) {
					LOG.error("EXCEPTION - viewName:" + viewName + " query:"
							+ query);
					LOG.error(ex.getMessage(), ex);
				}
			}

			// stream with participating activities of the sensor
			for (Entry<String, String> activityInterpreterQuery : sensor
					.getViewForActivityInterpreterQueries().entrySet()) {
				String viewName = activityInterpreterQuery.getKey();
				String query = activityInterpreterQuery.getValue();

				try {
					executeQueryNow(viewName, query);

				} catch (NoSuchElementException ex) {
					LOG.error(ex.getMessage(), ex);
				} catch (Exception ex) {
					LOG.error("EXCEPTION - viewName:" + viewName + " query:"
							+ query);
					LOG.error(ex.getMessage(), ex);
				}
			}

			// Export ActivitySources:
			for (String possibleActivity : sensor.getPossibleActivityNames()) {
				try {
					getP2PDictionary().exportSource(
							sensor.getActivitySourceName(possibleActivity));
				} catch (PeerException ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}
		}
	}
	
	

	void executeQueryNow(String viewName, String query) throws Exception {
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

	public void executeActorLogicRuleWhenPossibleAsync(
			final String possibleActivityName, final Sensor sensor,
			final Actor actor, final SmartDevice smartDeviceWithSensor) {
		Thread t = new Thread(new Runnable() {
			boolean ruleExecuted = false;

			@Override
			public void run() {
				String message = "activity:" + possibleActivityName
						+ " sensor:" + sensor.getName() + " actor:"
						+ actor.getName() + " smartDevice:"
						+ smartDeviceWithSensor.getPeerName();
				LOG.debug("executeLogicRuleAsync: " + message);

				while (!ruleExecuted) {
					try {
						Thread.sleep(2000);
					} catch (Exception ex) {
					}

					try {
						if (isLogicExecutionPossible(possibleActivityName,
								sensor, actor, smartDeviceWithSensor)) {

							executeActorLogicRuleNow(possibleActivityName,
									sensor, actor, smartDeviceWithSensor);
							ruleExecuted = true;
						} else {
							// LOG.debug("!isLogicExecutionPossible: " +
							// message);
							// TODO: import neccessary source:
							if (logicRuleIsCurrentlyRunning(
									possibleActivityName, sensor, actor,
									smartDeviceWithSensor)) {
								LOG.debug("___logicRuleIsCurrentlyRunning!!!!");
								ruleExecuted = true;
							}
							if (!getP2PDictionary()
									.isImported(
											sensor.getActivitySourceName(possibleActivityName))) {
								//LOG.debug("___source is not imported! addNeededSourceForImport:"
								//		+ sensor.getActivitySourceName(possibleActivityName));
								addNeededSourceForImport(
										sensor.getActivitySourceName(possibleActivityName),
										smartDeviceWithSensor.getPeerID());
							}

						}
					} catch (Exception ex) {
						LOG.error(ex.getMessage(), ex);
					}
				}

				LOG.debug("_END_executeLogicRuleAsync: " + message);
			}
		});
		t.setName("Logic execution thread. smartDevice:"
				+ smartDeviceWithSensor + " activityName:"
				+ possibleActivityName + " sensor:" + sensor + " actor:"
				+ actor);
		t.setDaemon(true);
		t.start();
	}
	
	public void addNeededSourceForImport(String neddedSourceName,
			String peerIDString) {
		if (!getP2PDictionary().isImported(neddedSourceName)) {

			getSourcesNeededForImport().put(neddedSourceName, peerIDString);

			//LOG.debug("addNeededSourceForImport(" + neddedSourceName + ", "
			//		+ peerIDString + ")");
		}
	}
	public void removeSourceNeededForImport(String srcName) {
		getSourcesNeededForImport().remove(srcName);
	}

	 void importIfNeccessary(SourceAdvertisement srcAdv) {
		// TODO: import if neccessary

		// LOG.debug("???importIfNeccessary? srcAdv.getName(): " +
		// srcAdv.getName());

		if (!getP2PDictionary().isImported(srcAdv.getName())
				&& getSourcesNeededForImport().containsKey(srcAdv.getName())) {
			try {
				LOG.debug("!!!import is neccessary importSourceNow:"
						+ srcAdv.getName());

				getP2PDictionary().importSource(srcAdv, srcAdv.getName());
				removeSourceNeededForImport(srcAdv.getName());
			} catch (PeerException e) {
				e.printStackTrace();
			} catch (InvalidP2PSource e) {
				e.printStackTrace();
			}
		} else if (getP2PDictionary().isImported(srcAdv.getName())
				&& getSourcesNeededForImport().containsKey(srcAdv.getName())) {
			removeSourceNeededForImport(srcAdv.getName());
		} else {

		}
	}

	static LinkedHashMap<String, String> getSourcesNeededForImport() {
		if (sourcesNeededForImport == null) {
			sourcesNeededForImport = new LinkedHashMap<String, String>();
		}
		return sourcesNeededForImport;
	}
	boolean isLogicExecutionPossible(String possibleActivityName,
			Sensor sensor, Actor actor, SmartDevice smartDeviceWithSensor) {
		return getP2PDictionary() != null
				// && getP2PDictionary().isImported(sensor.getRawSourceName())
				&& getP2PDictionary().isImported(
						sensor.getActivitySourceName(possibleActivityName))
				&& !logicRuleIsCurrentlyRunning(possibleActivityName, sensor,
						actor, smartDeviceWithSensor);
	}

	private static boolean logicRuleIsCurrentlyRunning(
			String possibleActivityName, Sensor sensor, Actor actor,
			SmartDevice smartDeviceWithSensor) {

		return LogicExecutionContainer.getInstance().isLogicRuleRunning(
				possibleActivityName, sensor, actor, smartDeviceWithSensor);
	}
	
	private void executeActorLogicRuleNow(String possibleActivityName,
			Sensor sensor, Actor actor, SmartDevice smartDeviceWithSensor) {
		LOG.debug("executeLogicRule(" + possibleActivityName + "," + sensor
				+ "," + actor + "," + smartDeviceWithSensor.getPeerName() + ")");

		if (isLogicExecutionPossible(possibleActivityName, sensor, actor,
				smartDeviceWithSensor)) {

			LOG.debug("sourceIsImported run the actor logic script now:");

			try {
				actor.setActivitySourceName(sensor
						.getActivitySourceName(possibleActivityName));
				
				LinkedHashMap<String, String> logicRuleQueries = actor
						.getLogicRuleOfActivity(possibleActivityName);
				for (Entry<String, String> entry : logicRuleQueries.entrySet()) {
					String viewName = entry.getKey()+"_"+possibleActivityName;
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
	public static QueryExecutor getInstance() {
		if(instance==null){
			instance = new QueryExecutor();
		}
		return instance;
	}

	private static IP2PDictionary getP2PDictionary() {
		return p2pDictionary;
	}
	public void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
		p2pDictionary.addListener(this);
	}

	public void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary.removeListener(this);
			p2pDictionary = null;
		}
	}
	
	void stopQuery(String queryName) {
		LOG.debug("stop and removeQueryName:" + queryName);

		// TODO: check correctness
		ServerExecutorService.getServerExecutor().removeQuery(queryName,
				SessionManagementService.getActiveSession());

		ServerExecutorService.getServerExecutor().removeQuery(
				queryName + "_query",
				SessionManagementService.getActiveSession());

		// ServerExecutorService.getServerExecutor().
	}
	
	public void removeAllLogicRules(SmartDevice remoteSmartDevice) {
		for (Sensor sensor : remoteSmartDevice.getConnectedSensors()) {
			/*
			 * // iterate backward: ListIterator<String> iterator = new
			 * ArrayList<String>(sensor
			 * .getViewForActivityInterpreterQueries().keySet())
			 * .listIterator(sensor.getViewForActivityInterpreterQueries()
			 * .size()); while (iterator.hasPrevious()) { String queryName =
			 * iterator.previous();
			 * 
			 * stopQuery(queryName); }
			 * 
			 * // iterate backward: ListIterator<String> iteratorRawValues = new
			 * ArrayList<String>( sensor.getQueriesForRawValues().keySet())
			 * .listIterator(sensor.getQueriesForRawValues().size()); while
			 * (iteratorRawValues.hasPrevious()) { String queryName =
			 * iteratorRawValues.previous();
			 * 
			 * stopQuery(queryName); }
			 */

			for (String possibleActivity : sensor.getPossibleActivityNames()) {
				stopQuery(sensor.getActivitySourceName(possibleActivity));
				stopQuery(sensor.getActivitySourceName(possibleActivity)
						+ "_query");
			}
		}
		LogicExecutionContainer.getInstance().removeLogicRules(
				remoteSmartDevice);
	}
	public boolean isSourceNameUsingByLogicRule(String sourceName) {
		return getP2PDictionary()
				.isSourceNameAlreadyInUse(sourceName);
	}
	
	public boolean isRunningLogicRule(SmartDevice remoteSmartDevice) {
		// TODO: check correctness
		for (Sensor sensor : remoteSmartDevice.getConnectedSensors()) {
			for (Entry<String, String> entry : sensor
					.getViewForActivityInterpreterQueries().entrySet()) {
				String queryName = entry.getKey();
				if (SmartHomeServerPlugIn.getP2PDictionary()
						.isSourceNameAlreadyInUse(queryName)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void smartDeviceAdded(SmartDeviceServerDictionaryDiscovery sender,
			SmartDevice smartDevice) {
	}

	@Override
	public void smartDeviceRemoved(SmartDeviceServerDictionaryDiscovery sender,
			SmartDevice smartDevice) {
		LOG.debug("QueryExecutor smartDeviceRemoved: "
				+ SmartDeviceServer.getInstance().getLocalSmartDevice()
						.getPeerID());

		if (QueryExecutor.getInstance().isRunningLogicRule(smartDevice)) {
			QueryExecutor.getInstance().removeAllLogicRules(smartDevice);
		}
	}

	@Override
	public void smartDeviceUpdated(SmartDeviceServerDictionaryDiscovery sender,
			SmartDevice smartDevice) {
		// LOG.debug("smartDeviceUpdated: " +
		// smartDevice.getPeerIDString());
		//
		// TODO: Nachschauen was sich am SmartDevice geändert hat.
				// Falls Sensoren hinzugefügt oder entfernt wurden, dann müssen die
				// Logik-Regeln überprüft und anschließend ausgeführt oder entfernt
				// werden!

	}
}
