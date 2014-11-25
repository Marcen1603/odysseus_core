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
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.ActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.AbstractActor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.AbstractLogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor.AbstractSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.service.SessionManagementService;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.SmartDevice;

public class QueryExecutor implements IP2PDictionaryListener,
		IAdvertisementDiscovererListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static QueryExecutor instance;
	private static IP2PDictionary p2pDictionary;
	private static LinkedHashMap<String, String> sourcesNeededForImport;
	private IP2PNetworkManager p2pNetworkManager;

	private QueryExecutor(){
	}
	
	private void waitForServerExecutorService() {
		while(ServerExecutorService.getServerExecutor()==null || !ServerExecutorService.getServerExecutor().isRunning()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public synchronized static QueryExecutor getInstance() {
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
		for (AbstractActor actor : remoteSmartDevice.getConnectedActors()) {
			for (AbstractLogicRule rule : actor.getLogicRules()) {
				for (AbstractSensor remoteSensor : remoteSmartDevice
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
		for (AbstractActor localActor : SmartDevicePublisher.getInstance()
				.getLocalSmartDevice().getConnectedActors()) {
			for (AbstractLogicRule rule : localActor.getLogicRules()) {
				for (AbstractSensor remoteSensor : remoteSmartDevice
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
				LOG.debug("exportWhenPossibleAsync() ");
				
				waitForServerExecutorService();
				waitForP2PDictionary();
				//waitForSource(sourceName);
				
				LOG.debug("exportWhenPossibleAsync() waitForP2PDictionary done.");

				try {
					getP2PDictionary().exportSource(sourceName);
					
					if(getP2PDictionary().getSources(sourceName)!=null && getP2PDictionary().getSources(sourceName).size()>0){
						LOG.debug("exportWhenPossibleAsync exportSource!!!!!!!");
					}else{
						LOG.debug("exportWhenPossibleAsync FALSE exportSource!!!!!!!");
					}
				} catch (PeerException e) {
					LOG.debug("___!!!___export source Exception:" + sourceName);
					LOG.error(e.getMessage(), e);
				}
			}
		});
		t.setName(getClass() + " export source thread. source:" + sourceName);
		t.setDaemon(true);
		t.start();
	}

	protected void waitForSource(String sourceName) {
		while(getP2PDictionary().getSources(sourceName)!=null && getP2PDictionary().getSources(sourceName).size() <= 0 ){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void advertisementDiscovered(Advertisement advertisement) {
		if (advertisement instanceof SourceAdvertisement) {
			SourceAdvertisement srcAdv = (SourceAdvertisement) advertisement;

			importOrRemoveIfNeccessary(srcAdv);
		} else if (advertisement instanceof MultipleSourceAdvertisement) {
			MultipleSourceAdvertisement multiSourceAdv = (MultipleSourceAdvertisement) advertisement;

			for (SourceAdvertisement sAdv : multiSourceAdv
					.getSourceAdvertisements()) {

				importOrRemoveIfNeccessary(sAdv);
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
	public void executeQueriesAsync(final AbstractLogicRule rule,
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
					//TODO: loop to wait for import source: activityInterpreter.getActivitySourceName()
					//QueryState queryState = ServerExecutorService.getServerExecutor()
					//		.getQueryState(queryName);
					//queryState.equals(QueryState.RUNNING);
					
					if(getP2PDictionary().isImported(activityInterpreter.getActivitySourceName())){
						executeQueryNow(logicRuleQueries);
						rule.addRunningWith(activityInterpreter);
					}else{
						LOG.error("can't execute logicrules, because source is not imported. source name: "+activityInterpreter.getActivitySourceName());
					}
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
		LOG.debug("stopLogicRuleQueries for sourceName:" + sourceName);
		for (AbstractActor actor : SmartDevicePublisher.getInstance()
				.getLocalSmartDevice().getConnectedActors()) {
			for (AbstractLogicRule rule : actor.getLogicRules()) {
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
		
		QueryState queryState = ServerExecutorService.getServerExecutor()
				.getQueryState(viewName+"_query");
		//queryState.equals(QueryState.UNDEF);
		
		LOG.error("executeQueryNow() viewName:'"+viewName+"_query' queryState:'"+queryState+"' query:\n"+query+"\n");
		
		
		//&& queryState.equals(QueryState.UNDEF)
		
		//if (!queryIsRunning(viewName + "_query") ) {
			Collection<Integer> queryIDs = ServerExecutorService
					.getServerExecutor().addQuery(query, "OdysseusScript",
							SessionManagementService.getActiveSession(),
							"Standard", Context.empty());
			LOG.debug("Query added!");
			
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
		//}
		//return null;
	}

	//TODO: 
	@SuppressWarnings("unused")
	private boolean queryIsRunning(String queryName) {
		QueryState queryState = ServerExecutorService.getServerExecutor()
				.getQueryState(queryName);
		return queryState.equals(QueryState.RUNNING);
	}

	private void importOrRemoveIfNeccessary(SourceAdvertisement srcAdv) {
		if (!getP2PDictionary().isImported(srcAdv.getName())
				&& getSourcesNeededForImport().containsKey(srcAdv.getName())) {
			try {
				LOG.debug("!!!import is neccessary importSourceNow:"
						+ srcAdv.getName());

				getP2PDictionary().importSource(srcAdv, srcAdv.getName());
			} catch (PeerException e) {
				e.printStackTrace();
			} catch (InvalidP2PSource e) {
				e.printStackTrace();
			}
		} else if (getP2PDictionary().isImported(srcAdv.getName())
				&& !getSourcesNeededForImport().containsKey(srcAdv.getName())) {
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

	void stopQueryAndRemoveViewOrStream(String queryName) {
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

		importOrRemoveIfNeccessary(advertisement);
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

		//removeSourceIfNeccessary(sourceName);
	}

	@Override
	public void sourceImportRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceImportRemoved sourceName:" + sourceName
				+ " and stopQuery");

		// QueryExecutor.getInstance().removeSourceIfNeccessary(sourceName);

		//stopLogicRuleQueries(sourceName);
		
		
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

	public void stopLogicRuleAsync(final AbstractLogicRule rule) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					stopLogicRuleNow(rule);
				} catch (Exception ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}
		});
		t.setName(QueryExecutor.class + " stopLogicRuleAsync thread.");
		t.setDaemon(true);
		t.start();
	}

	private void stopLogicRuleNow(AbstractLogicRule rule) {
		LOG.debug("stopLogicRuleNow");
		for(ActivityInterpreter activityInterpreter : rule.getActivityInterpretersWithRunningRules()){
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

	public void removeExport(String mergedActivitySourceName) {
		getP2PDictionary().removeSourceExport(mergedActivitySourceName);
	}

	public void exportNow(String mergedActivitySourceName) throws PeerException {
		getP2PDictionary().exportSource(mergedActivitySourceName);
	}

	public boolean isImportedSource(String source) {
		return getP2PDictionary().isImported(source);
	}

	public void executeQueryAsync(final String queryName, final String query, final String sourceNameToWaitFor) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				//TODO:
				//waitForSource(sourceNameToWaitFor);
				while(!p2pDictionary.isSourceNameAlreadyInUse(sourceNameToWaitFor)){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
				
				LOG.debug("source is available:"+sourceNameToWaitFor);
				
				
				try {
					executeQueryNow(queryName, query);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}				
			}
		});
		t.setName("execute query async thread to wait for source");
		t.setDaemon(true);
		t.start();
	}
}
