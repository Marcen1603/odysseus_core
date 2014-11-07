package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import net.jxta.document.Advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.MultipleSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.Actor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.LogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.SmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.service.SessionManagementService;

public class QueryExecutor implements IP2PDictionaryListener,
		ISmartDeviceDictionaryListener, IAdvertisementDiscovererListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static QueryExecutor instance;
	private static IP2PDictionary p2pDictionary;
	private static LinkedHashMap<String, String> sourcesNeededForImport;
	private IP2PNetworkManager p2pNetworkManager;

	public static QueryExecutor getInstance() {
		if (instance == null) {
			instance = new QueryExecutor();
		}
		return instance;
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

	public void addNeededSourceForImport(String neddedSourceName,
			String peerIDString) {
		getSourcesNeededForImport().put(neddedSourceName, peerIDString);
	}

	public void removeSourceIfNeccessary(String srcName) {
		getSourcesNeededForImport().remove(srcName);
	}

	public void removeAllLogicRules(ASmartDevice remoteSmartDevice) {
		LOG.debug("removeAllLogicRules:" + remoteSmartDevice.getPeerName());
		for (Actor actor : remoteSmartDevice.getConnectedActors()) {
			for (LogicRule rule : actor.getLogicRules()) {
				for (Sensor remoteSensor : remoteSmartDevice
						.getConnectedSensors()) {
					for (ActivityInterpreter activityInterpreter : remoteSensor
							.getActivityInterpreters()) {
						if (rule.isRunningWith(activityInterpreter)) {

							@SuppressWarnings({ "rawtypes", "unchecked" })
							ListIterator<Map.Entry<String, String>> iter = new ArrayList(
									rule.getLogicRuleQueries(
											activityInterpreter).entrySet())
									.listIterator(rule.getLogicRuleQueries(
											activityInterpreter).size());

							while (iter.hasPrevious()) {
								Map.Entry<String, String> entry = iter
										.previous();

								String viewName = entry.getKey();

								stopQueryAndRemoveViewOrStream(viewName);
							}
						}
					}
				}
			}
		}
	}

	public boolean isSourceNameUsingByLogicRule(String sourceName) {
		return getP2PDictionary().isSourceNameAlreadyInUse(sourceName);
	}

	public boolean isRunningLogicRule(SmartDevice remoteSmartDevice) {
		for (Actor localActor : SmartDeviceServer.getInstance()
				.getLocalSmartDevice().getConnectedActors()) {
			for (LogicRule rule : localActor.getLogicRules()) {
				for (Sensor remoteSensor : remoteSmartDevice
						.getConnectedSensors()) {
					for (ActivityInterpreter activityInterpreter : remoteSensor
							.getActivityInterpreters()) {
						if (rule.isRunningWith(activityInterpreter)) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public void exportWhenPossibleAsync(final String sourceName) {
		if (sourceName == null) {
			throw new NullPointerException("sourceName is null!");
		}

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				waitForP2PDictionary();

				try {
					getP2PDictionary().exportSource(sourceName);
				} catch (PeerException e) {
					LOG.debug("export source:" + sourceName);
					LOG.error(e.getMessage(), e);
				}
			}
		});
		t.setName(getClass() + " export source thread. source:" + sourceName);
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void advertisementDiscovered(Advertisement advertisement) {
		if (advertisement instanceof SourceAdvertisement) {
			SourceAdvertisement srcAdv = (SourceAdvertisement) advertisement;

			importIfNeccessary(srcAdv);
		} else if (advertisement instanceof MultipleSourceAdvertisement) {
			MultipleSourceAdvertisement multiSourceAdv = (MultipleSourceAdvertisement) advertisement;

			for (SourceAdvertisement sAdv : multiSourceAdv
					.getSourceAdvertisements()) {

				importIfNeccessary(sAdv);
			}
		}
	}

	@Override
	public void updateAdvertisements() {
	}

	public void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
		p2pNetworkManager.addAdvertisementListener(this);
	}

	public void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			getP2PNetworkManager().removeAdvertisementListener(this);
			p2pNetworkManager = null;
		}
	}

	/**
	 * 
	 * @param activityInterpreter
	 * @param rule
	 * @param logicRuleQueries
	 *            as LinkedHashMap<String,String>(<ViewName,Query>)
	 * @param sourcNameToWaitFor
	 */
	public void executeQueriesAsync(final LogicRule rule,
			final ActivityInterpreter activityInterpreter,
			final LinkedHashMap<String, String> logicRuleQueries,
			final String sourcNameToWaitFor) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				LOG.debug("executeQueriesAsync run:" + rule.getActivityName()
						+ " sourcNameToWaitFor:" + sourcNameToWaitFor);
				waitForImport(sourcNameToWaitFor);

				try {
					executeQueryNow(logicRuleQueries);
					rule.setRunningWith(activityInterpreter);
				} catch (Exception ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}
		});
		t.setName(getClass() + " executeQueriesAsync");
		t.setDaemon(true);
		t.start();
	}

	private void stopLogicRuleQueries(String sourceName) {
		LOG.debug("stopLogicRuleQueries:" + sourceName);
		for (Actor actor : SmartDeviceServer.getInstance()
				.getLocalSmartDevice().getConnectedActors()) {
			for (LogicRule rule : actor.getLogicRules()) {
				for (ActivityInterpreter activityInterpreter : rule
						.getActivityInterpretersWithRunningRules()) {
					if (rule.isRunningWith(activityInterpreter)) {
						@SuppressWarnings({ "rawtypes", "unchecked" })
						ListIterator<Map.Entry<String, String>> iter = new ArrayList(
								rule.getLogicRuleQueries(activityInterpreter)
										.entrySet()).listIterator(rule
								.getLogicRuleQueries(activityInterpreter)
								.size());

						while (iter.hasPrevious()) {
							Map.Entry<String, String> entry = iter.previous();

							String viewName = entry.getKey();

							stopQueryAndRemoveViewOrStream(viewName);
						}
					}
				}

			}
		}
	}

	public Integer executeQueryNow(String viewName, String query)
			throws Exception {
		// LOG.error("viewName:" + viewName + " query:" + query);
		if (!queryIsRunning(viewName + "_query")) {
			Collection<Integer> queryIDs = ServerExecutorService
					.getServerExecutor().addQuery(query, "OdysseusScript",
							SessionManagementService.getActiveSession(),
							"Standard", Context.empty());
			Integer queryId;

			if (queryIDs == null) {
				LOG.debug("queryIDs==null @viewName:" + viewName);
				return null;
			}

			if (!queryIDs.iterator().hasNext()) {
				LOG.debug("!queryIDs.iterator().hasNext() @viewName:"
						+ viewName);
				return null;
			}

			queryId = queryIDs.iterator().next();
			IPhysicalQuery physicalQuery = ServerExecutorService
					.getServerExecutor().getExecutionPlan()
					.getQueryById(queryId);
			ILogicalQuery logicalQuery = physicalQuery.getLogicalQuery();
			logicalQuery.setName(viewName);
			logicalQuery.setParserId("P2P");
			logicalQuery.setUser(SessionManagementService.getActiveSession());
			logicalQuery.setQueryText("Query " + viewName);

			LOG.debug("QueryId:" + queryId + " viewName:" + viewName);

			return queryId;
		}
		return null;
	}

	private boolean queryIsRunning(String queryName) {
		QueryState queryState = ServerExecutorService.getServerExecutor()
				.getQueryState(queryName);
		return queryState.equals(QueryState.RUNNING);
	}

	private void importIfNeccessary(SourceAdvertisement srcAdv) {
		if (!getP2PDictionary().isImported(srcAdv.getName())
				&& getSourcesNeededForImport().containsKey(srcAdv.getName())) {
			try {
				LOG.debug("!!!import is neccessary importSourceNow:"
						+ srcAdv.getName());

				getP2PDictionary().importSource(srcAdv, srcAdv.getName());
				removeSourceIfNeccessary(srcAdv.getName());
			} catch (PeerException e) {
				e.printStackTrace();
			} catch (InvalidP2PSource e) {
				e.printStackTrace();
			}
		} else if (getP2PDictionary().isImported(srcAdv.getName())
				&& getSourcesNeededForImport().containsKey(srcAdv.getName())) {
			removeSourceIfNeccessary(srcAdv.getName());
		} else {

		}
	}

	private LinkedHashMap<String, String> getSourcesNeededForImport() {
		if (sourcesNeededForImport == null) {
			sourcesNeededForImport = new LinkedHashMap<String, String>();
		}
		return sourcesNeededForImport;
	}

	private static IP2PDictionary getP2PDictionary() {
		return p2pDictionary;
	}

	private void stopQueryAndRemoveViewOrStream(String queryName) {
		LOG.debug("stopQuery:" + queryName);

		try {
			stopQueryImmediately(queryName);
			stopQueryImmediately(queryName + "_query");
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
		}

		try {
			removeViewImmediately(queryName);
			removeViewImmediately(queryName + "_query");
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
		}
	}

	private void stopQueryImmediately(String queryName) {
		QueryState queryState = ServerExecutorService.getServerExecutor()
				.getQueryState(queryName);
		if (queryState.equals(QueryState.RUNNING)) {

			ServerExecutorService.getServerExecutor().removeQuery(queryName,
					SessionManagementService.getActiveSession());

		} else if (!queryState.equals(QueryState.UNDEF)) {
			LOG.debug("can't stopQueryName:" + queryName + " queryState:"
					+ queryState);
		}

		ServerExecutorService.getServerExecutor().removeViewOrStream(queryName,
				SessionManagementService.getActiveSession());
	}

	private void removeViewImmediately(String queryName) {
		ServerExecutorService.getServerExecutor().removeViewOrStream(queryName,
				SessionManagementService.getActiveSession());
	}

	private IP2PNetworkManager getP2PNetworkManager() {
		return p2pNetworkManager;
	}

	protected void waitForImportSource(String activitySourceName) {
		if (!getP2PDictionary().isImported(activitySourceName)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	protected void waitForImport(String viewName) {
		while (getP2PDictionary() == null
				|| !getP2PDictionary().isImported(viewName)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	protected void waitForImports(LinkedHashMap<String, String> queries) {
		int numImported = 0;
		while (numImported != queries.size()) {
			numImported = 0;
			for (Entry<String, String> entry : queries.entrySet()) {
				String viewName = entry.getKey();
				if (getP2PDictionary().isImported(viewName)) {
					numImported++;
				} else {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}

	protected void executeQueryNow(LinkedHashMap<String, String> queries)
			throws Exception {
		for (Entry<String, String> entry : queries.entrySet()) {
			executeQueryNow(entry.getKey(), entry.getValue());
		}
	}

	protected void waitForP2PDictionary() {
		while (getP2PDictionary() == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	/*********************************************************
	 * IP2PDictionaryListener
	 */
	@Override
	public void sourceAdded(IP2PDictionary sender,
			SourceAdvertisement advertisement) {
		LOG.debug("sourceAdded");

		importIfNeccessary(advertisement);
	}

	@Override
	public void sourceRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement) {
		LOG.debug("sourceRemoved");

		stopLogicRuleQueries(advertisement.getName());
	}

	@Override
	public void sourceImported(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceImported sourceName:" + sourceName);

		if (advertisement.isLocal()) {
			LOG.debug("advertisement.isLocal()");
			return;
		}

		removeSourceIfNeccessary(sourceName);
	}

	@Override
	public void sourceImportRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceImportRemoved sourceName:" + sourceName
				+ " and stopQuery");

		// QueryExecutor.getInstance().removeSourceIfNeccessary(sourceName);

		stopLogicRuleQueries(sourceName);
	}

	@Override
	public void sourceExported(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		// LOG.debug("sourceExported: " + sourceName);
	}

	@Override
	public void sourceExportRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		// LOG.debug("sourceExportRemoved: " + sourceName);
	}

	/*********************************************************
	 * ISmartDeviceDictionaryListener
	 */
	@Override
	public void smartDeviceAdded(SmartDeviceServerDictionaryDiscovery sender,
			ASmartDevice smartDevice) {
	}

	@Override
	public void smartDeviceRemoved(SmartDeviceServerDictionaryDiscovery sender,
			ASmartDevice smartDevice) {

		LOG.debug("QueryExecutor smartDeviceRemoved: "
				+ SmartDeviceServer.getInstance().getLocalSmartDevice()
						.getPeerID());

		// if (isRunningLogicRule(smartDevice)) {
		removeAllLogicRules(smartDevice);
		// }
	}

	@Override
	public void smartDeviceUpdated(SmartDeviceServerDictionaryDiscovery sender,
			ASmartDevice smartDevice) {
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
}
