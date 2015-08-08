package de.uniol.inf.is.odysseus.peer.distribute.part;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.DistributedQueryRepresentationAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.peer.communication.RepeatingMessageSend;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.PeerDistributePlugIn;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartTransmissionException;
import de.uniol.inf.is.odysseus.peer.distribute.message.AbortQueryPartAddAckMessage;
import de.uniol.inf.is.odysseus.peer.distribute.message.AbortQueryPartAddMessage;
import de.uniol.inf.is.odysseus.peer.distribute.message.AddQueryPartMessage;
import de.uniol.inf.is.odysseus.peer.distribute.message.QueryPartAddAckMessage;
import de.uniol.inf.is.odysseus.peer.distribute.message.QueryPartAddFailMessage;
import de.uniol.inf.is.odysseus.peer.distribute.util.IOperatorGenerator;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

public class QueryPartSender implements IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartSender.class);

	private static IP2PNetworkManager p2pNetworkManager;
	private static IP2PDictionary p2pDictionary;
	private static IPeerDictionary peerDictionary;
	private static IPQLGenerator pqlGenerator;
	private static IPeerCommunicator peerCommunicator;

	private static QueryPartSender instance;

	private static int queryPartIDCounter = 0;

	private final Map<Integer, RepeatingMessageSend> senderMap = Maps.newConcurrentMap();
	private final Map<Integer, PeerID> sendDestinationMap = Maps.newConcurrentMap();
	private final Map<Integer, String> sendResultMap = Maps.newConcurrentMap();

	private final Map<PeerIDSharedQueryIDPair, RepeatingMessageSend> abortSenderMap = Maps.newConcurrentMap();

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}

	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}

	// called by OSGi-DS
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if (peerDictionary == serv) {
			peerDictionary = null;
		}
	}

	// called by OSGi-DS
	public static void bindPQLGenerator(IPQLGenerator serv) {
		pqlGenerator = serv;
	}

	// called by OSGi-DS
	public static void unbindPQLGenerator(IPQLGenerator serv) {
		if (pqlGenerator == serv) {
			pqlGenerator = null;
		}
	}

	// called by OSGi-DS
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;

		peerCommunicator.registerMessageType(QueryPartAddAckMessage.class);
		peerCommunicator.registerMessageType(QueryPartAddFailMessage.class);
		peerCommunicator.registerMessageType(AbortQueryPartAddMessage.class);
		peerCommunicator.registerMessageType(AbortQueryPartAddAckMessage.class);

		peerCommunicator.addListener(this, QueryPartAddAckMessage.class);
		peerCommunicator.addListener(this, QueryPartAddFailMessage.class);
		peerCommunicator.addListener(this, AbortQueryPartAddAckMessage.class);
	}

	// called by OSGi-DS
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this, QueryPartAddAckMessage.class);
			peerCommunicator.removeListener(this, QueryPartAddFailMessage.class);
			peerCommunicator.removeListener(this, AbortQueryPartAddAckMessage.class);

			peerCommunicator.unregisterMessageType(QueryPartAddAckMessage.class);
			peerCommunicator.unregisterMessageType(QueryPartAddFailMessage.class);
			peerCommunicator.unregisterMessageType(AbortQueryPartAddMessage.class);
			peerCommunicator.unregisterMessageType(AbortQueryPartAddAckMessage.class);

			peerCommunicator = null;
		}
	}

	// called by OSGi-DS
	public void activate() {
		instance = this;
	}

	// called by OSGi-DS
	public void deactivate() {
		instance = null;
	}

	public static QueryPartSender getInstance() {
		return instance;
	}

	public static boolean isActivated() {
		return instance != null;
	}

	public static void waitFor() {
		while (!isActivated()) {
			waitSomeTime();
		}
	}

	public ID transmit(Map<ILogicalQueryPart, PeerID> allocationMap, IServerExecutor serverExecutor, ISession caller, String queryName, QueryBuildConfiguration config) throws QueryPartTransmissionException, QueryDistributionException {
		LOG.debug("Beginning transmission...");
		ID sharedQueryID = IDFactory.newContentID(p2pNetworkManager.getLocalPeerGroupID(), false, String.valueOf(System.currentTimeMillis()).getBytes());

		appendPeerIDToStreamAO(allocationMap);
		insertJxtaOperators(allocationMap);
		replaceAccessAOsOfExportedViews(allocationMap.keySet());

		distributeToRemotePeers(sharedQueryID, serverExecutor, caller, allocationMap, config, queryName);

		Collection<ILogicalQueryPart> localQueryParts = determineLocalQueryParts(allocationMap);

		if (localQueryParts.isEmpty()) {
			LOG.debug("There is no local query. Building a representation operator.");

			LogicalQueryPart part = new LogicalQueryPart(new DistributedQueryRepresentationAO(sharedQueryID));
			localQueryParts.add(part);
		}

		LOG.debug("Building local logical query out of {} local query parts", localQueryParts.size());

		ILogicalQuery localQuery = buildLocalQuery(localQueryParts);
		localQuery.setName(queryName);
		localQuery.setUser(caller);

		try {
			callExecutorToAddLocalQueries(localQuery, sharedQueryID, serverExecutor, caller, config, determineSlavePeers(allocationMap));
		} catch (Throwable t) {
			LOG.error("Exception during placing local query part. Query is {}", localQuery.getQueryText());
			LOG.error("Exception is ", t);
			removeDistributedQueryParts(sharedQueryID);
		}
		
		LOG.debug("Transmission finished");
		return sharedQueryID;
	}

	private static void appendPeerIDToStreamAO(Map<ILogicalQueryPart, PeerID> allocationMap) throws QueryDistributionException {
		for (ILogicalQueryPart queryPart : allocationMap.keySet()) {

			if (!allocationMap.get(queryPart).equals(p2pNetworkManager.getLocalPeerID())) {
				// QueryPart wird verteilt
				for (ILogicalOperator operator : queryPart.getOperators()) {
					if (operator instanceof StreamAO) {
						StreamAO streamAO = (StreamAO) operator;

						String sourceName = streamAO.getStreamname().getResourceName();
						if (p2pDictionary.isExported(sourceName)) {
							SourceAdvertisement srcAdv = p2pDictionary.getExportedSource(sourceName).get();
							streamAO.setNode(srcAdv.getPeerID().toString());
						} else if (p2pDictionary.isImported(sourceName)) {
							SourceAdvertisement srcAdv = p2pDictionary.getImportedSource(sourceName).get();
							streamAO.setNode(srcAdv.getPeerID().toString());
						} else {
							throw new QueryDistributionException("Could not distribute since the StreamAO uses a non-exported source '" + sourceName + "'");
						}
					}
				}
			}
		}
	}

	private static Collection<ILogicalQueryPart> determineLocalQueryParts(Map<ILogicalQueryPart, PeerID> allocationMap) {
		List<ILogicalQueryPart> localParts = Lists.newArrayList();

		for (ILogicalQueryPart part : allocationMap.keySet()) {
			PeerID peerID = allocationMap.get(part);

			if (peerID.equals(p2pNetworkManager.getLocalPeerID())) {
				localParts.add(part);
				LOG.debug("Query part {} stays local", part);
			}
		}

		return localParts;
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if (message instanceof QueryPartAddAckMessage) {
			QueryPartAddAckMessage ackMessage = (QueryPartAddAckMessage) message;

			synchronized (senderMap) {
				RepeatingMessageSend sender = senderMap.get(ackMessage.getQueryPartID());
				if (sender != null) {
					sender.stopRunning();
					senderMap.remove(ackMessage.getQueryPartID());
				}
			}

			sendResultMap.put(ackMessage.getQueryPartID(), "OK");

		} else if (message instanceof QueryPartAddFailMessage) {
			QueryPartAddFailMessage failMessage = (QueryPartAddFailMessage) message;

			synchronized (senderMap) {
				RepeatingMessageSend sender = senderMap.get(failMessage.getQueryPartID());
				if (sender != null) {
					sender.stopRunning();
					senderMap.remove(failMessage.getQueryPartID());
				}
			}

			sendResultMap.put(failMessage.getQueryPartID(), failMessage.getMessage());

		} else if (message instanceof AbortQueryPartAddAckMessage) {
			AbortQueryPartAddAckMessage abortAckMessage = (AbortQueryPartAddAckMessage) message;

			PeerIDSharedQueryIDPair pair = new PeerIDSharedQueryIDPair(senderPeer, abortAckMessage.getSharedQueryID());
			RepeatingMessageSend abortSender = abortSenderMap.get(pair);
			if (abortSender != null) {
				abortSender.stopRunning();
				abortSenderMap.remove(pair);
			}
		}

	}

	private static void insertJxtaOperators(final Map<ILogicalQueryPart, PeerID> allocationMap) {
		Set<ILogicalQueryPart> queryParts = allocationMap.keySet();
		LogicalQueryHelper.disconnectQueryParts(queryParts, new IOperatorGenerator() {

			private PipeID pipeID;
			private ILogicalOperator sourceOp;

			private ILogicalQueryPart sourceQueryPart;
			private ILogicalQueryPart sinkQueryPart;

			@Override
			public void beginDisconnect(ILogicalQueryPart sourceQueryPart, ILogicalOperator sourceOperator, ILogicalQueryPart sinkQueryPart, ILogicalOperator sinkOperator) {
				LOG.debug("Create JXTA-Connection between {} and {}", new Object[] { sourceOperator, sinkOperator });

				pipeID = IDFactory.newPipeID(p2pNetworkManager.getLocalPeerGroupID());
				sourceOp = sourceOperator;

				this.sourceQueryPart = sourceQueryPart;
				this.sinkQueryPart = sinkQueryPart;
			}

			@Override
			public ILogicalOperator createSourceofSink(ILogicalQueryPart sinkQueryPart, ILogicalOperator sink) {
				JxtaReceiverAO access = new JxtaReceiverAO();
				
				Optional<String> optString = LogicalQueryHelper.getBaseTimeunitString(sink.getOutputSchema());
				if( optString.isPresent() ) {
					access.setBaseTimeunit(optString.get());
				}
				access.setPipeID(pipeID.toString());
				access.setSchema(sourceOp.getOutputSchema().getAttributes());
				access.setSchemaName(sourceOp.getOutputSchema().getURI());
				access.setMetaschemata(sourceOp.getOutputSchema().getMetaschema());
				access.setPeerID(allocationMap.get(sourceQueryPart).toString());
				return access;
			}

			@Override
			public ILogicalOperator createSinkOfSource(ILogicalQueryPart sourceQueryPart, ILogicalOperator source) {
				JxtaSenderAO sender = new JxtaSenderAO();
				sender.setPipeID(pipeID.toString());
				sender.setPeerID(allocationMap.get(sinkQueryPart).toString());
				return sender;
			}

			@Override
			public void endDisconnect() {
				pipeID = null;
				sourceOp = null;
			}
		});
	}

	private static void replaceAccessAOsOfExportedViews(Set<ILogicalQueryPart> queryParts) {
		for (ILogicalQueryPart queryPart : queryParts) {
			for (ILogicalOperator operator : queryPart.getOperators().toArray(new ILogicalOperator[0])) {
				if (operator instanceof AbstractAccessAO) {
					AbstractAccessAO accessAO = (AbstractAccessAO) operator;
					Optional<SourceAdvertisement> optSourceAdv = p2pDictionary.getExportedSource(accessAO.getAccessAOName().getResourceName());
					if (optSourceAdv.isPresent() && optSourceAdv.get().isView()) {
						SourceAdvertisement advertisement = optSourceAdv.get();
						LOG.debug("Found accessAO for exported view which has to be send to peer: " + accessAO);

						final JxtaReceiverAO receiverOperator = new JxtaReceiverAO();
						receiverOperator.setPipeID(advertisement.getPipeID().toString());
						SDFSchema outputSchema = advertisement.getOutputSchema();
						outputSchema.setMetaSchema(advertisement.getMetaSchemata());
						receiverOperator.setOutputSchema(outputSchema);
						receiverOperator.setSchema(advertisement.getOutputSchema().getAttributes());
						receiverOperator.setBaseTimeunit(advertisement.getBaseTimeunit());
						receiverOperator.setSchemaName(advertisement.getOutputSchema().getURI());
						receiverOperator.setName(accessAO.getAccessAOName().getResourceName() + "_Receive");
						receiverOperator.setImportedSourceAdvertisement(advertisement);
						receiverOperator.setPeerID(advertisement.getPeerID().toString());

						Collection<LogicalSubscription> subscriptions = accessAO.getSubscriptions();
						for (LogicalSubscription subscription : subscriptions.toArray(new LogicalSubscription[0])) {
							accessAO.unsubscribeSink(subscription);
							receiverOperator.subscribeSink(subscription.getTarget(), subscription.getSinkInPort(), 0, subscription.getSchema());
						}

						queryPart.addOperator(receiverOperator);
						queryPart.removeOperator(accessAO);
					}
				}
			}
		}
	}

	private void distributeToRemotePeers(ID sharedQueryID, IServerExecutor serverExecutor, ISession caller, Map<ILogicalQueryPart, PeerID> correctedAllocationMap, QueryBuildConfiguration parameters, String queryName) throws QueryPartTransmissionException {
		senderMap.clear();
		sendDestinationMap.clear();
		sendResultMap.clear();

		int remoteSendCount = 0;
		int partNumber = 0;
		for (ILogicalQueryPart part : correctedAllocationMap.keySet()) {
			PeerID peerID = correctedAllocationMap.get(part);

			if (!peerID.equals(p2pNetworkManager.getLocalPeerID())) {
				ParameterTransformationConfiguration paramConfiguration = parameters.get(ParameterTransformationConfiguration.class);
				Collection<String> metaTypes = paramConfiguration.getValue().getDefaultMetaTypeSet();
				AddQueryPartMessage msg = new AddQueryPartMessage(sharedQueryID, LogicalQueryHelper.generatePQLStatementFromQueryPart(part), parameters.getName(), queryPartIDCounter++, metaTypes, queryName + "_" + partNumber);

				RepeatingMessageSend msgSender = new RepeatingMessageSend(peerCommunicator, msg, peerID);
				senderMap.put(msg.getQueryPartID(), msgSender);
				sendDestinationMap.put(msg.getQueryPartID(), peerID);
				remoteSendCount++;
				msgSender.start();

				LOG.debug("Sent query part {} to peerID {} (queryPartID = {})", new Object[] { part, peerID, msg.getQueryPartID() });
				LOG.trace("PQL-Query of query part {} is\n{}", part, msg.getPqlStatement());
			}
			
			partNumber++;
		}

		waitForAcksAndFails();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Response results: ");
			for (Integer qpid : sendResultMap.keySet()) {
				LOG.debug("\tQueryPartID {} (Peer {}) : {}", new Object[] { qpid, peerDictionary.getRemotePeerName(sendDestinationMap.get(qpid)), sendResultMap.get(qpid) });
			}
		}

		if (sendResultMap.size() != remoteSendCount) {
			removeDistributedQueryParts(sharedQueryID);

			List<PeerID> missingPeers = Lists.newArrayList();
			for (Integer qpid : sendDestinationMap.keySet()) {
				if (!sendResultMap.containsKey(qpid)) {
					missingPeers.add(sendDestinationMap.get(qpid));
				}
			}

			if (LOG.isErrorEnabled()) {
				LOG.error("Could not distribute the query parts since peers are not reachable");
				for (PeerID missingPeer : missingPeers) {
					LOG.error("\t{} : {}", peerDictionary.getRemotePeerName(missingPeer), missingPeer);
				}
			}

			throw new QueryPartTransmissionException(missingPeers);
		}

		List<PeerID> faultyPeers = Lists.newArrayList();
		List<String> faultMessages = Lists.newArrayList();
		for (Integer qpid : sendResultMap.keySet()) {
			String msg = sendResultMap.get(qpid);
			PeerID pid = sendDestinationMap.get(qpid);
			if (msg == null || !msg.equalsIgnoreCase("OK")) {
				faultyPeers.add(pid);
				faultMessages.add(msg);
			}
		}

		if (!faultyPeers.isEmpty()) {
			removeDistributedQueryParts(sharedQueryID);

			if (LOG.isErrorEnabled()) {
				LOG.error("Could not distribute the query since peers could not execute/add its query part:");
				for (int index = 0; index < faultyPeers.size(); index++) {
					LOG.error("\t{}: {}", peerDictionary.getRemotePeerName(faultyPeers.get(index)), faultMessages.get(index));
				}
			}

			throw new QueryPartTransmissionException(faultyPeers);
		}

		LOG.debug("Remote distribution complete.");
	}

	private void removeDistributedQueryParts(ID sharedQueryID) {
		LOG.debug("Removing already distributed query parts remotely");
		Collection<PeerID> sendPeerIDs = Lists.newLinkedList();
		for (Integer qpid : sendDestinationMap.keySet()) {
			PeerID destination = sendDestinationMap.get(qpid);

			if (!sendPeerIDs.contains(destination)) {
				sendPeerIDs.add(destination);

				LOG.debug("Send abort to peer '{}'", peerDictionary.getRemotePeerName(destination));

				RepeatingMessageSend abortSender = new RepeatingMessageSend(peerCommunicator, new AbortQueryPartAddMessage(sharedQueryID), destination);
				abortSenderMap.put(new PeerIDSharedQueryIDPair(destination, sharedQueryID), abortSender);
				abortSender.start();
			}
		}

		sendResultMap.clear();
		sendDestinationMap.clear();
		senderMap.clear();
	}

	private void waitForAcksAndFails() {
		LOG.debug("Waiting for responses from peers...");
		while (oneSenderIsActive()) {
			waitSomeTime();
		}
	}

	private static void waitSomeTime() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
		}
	}

	private boolean oneSenderIsActive() {
		synchronized (senderMap) {
			if (senderMap.isEmpty()) {
				return false;
			}

			for (int queryPartID : senderMap.keySet()) {
				RepeatingMessageSend sender = senderMap.get(queryPartID);
				if (sender.isRunning()) {
					return true;
				}
			}
			return false;
		}
	}

	private static ILogicalQuery buildLocalQuery(Collection<ILogicalQueryPart> localQueryParts) {
		Collection<ILogicalOperator> sinks = Lists.newArrayList();
		for (ILogicalQueryPart localPart : localQueryParts) {
			Collection<ILogicalOperator> partOperators = localPart.getOperators();

			Collection<ILogicalOperator> allOperators = Lists.newArrayList();
			for (ILogicalOperator operator : partOperators) {
				for (ILogicalOperator op : LogicalQueryHelper.getAllOperators(operator)) {
					if (!allOperators.contains(op)) {
						allOperators.add(op);
					}
				}
			}
			sinks.addAll(LogicalQueryHelper.getSinks(allOperators));
		}
		LOG.debug("Determined {} logical sinks", sinks.size());

		TopAO topAO = new TopAO();
		int inputPort = 0;
		for (ILogicalOperator sink : sinks) {
			topAO.subscribeToSource(sink, inputPort++, 0, sink.getOutputSchema());
		}
		LOG.debug("Created TopAO-Operator with {} sinks", sinks.size());

		ILogicalQuery logicalQuery = new LogicalQuery();
		logicalQuery.setLogicalPlan(topAO, true);
		logicalQuery.setParserId("PQL");
		logicalQuery.setPriority(0);
		logicalQuery.setQueryText(pqlGenerator.generatePQLStatement(topAO));
		LOG.debug("Created logical query with queryText = {}", logicalQuery.getQueryText());

		return logicalQuery;
	}

	private static Collection<PeerID> determineSlavePeers(Map<ILogicalQueryPart, PeerID> allocationMap) {
		Collection<PeerID> slavePeers = Lists.newArrayList();
		for (ILogicalQueryPart queryPart : allocationMap.keySet()) {
			PeerID pid = allocationMap.get(queryPart);
			if (!slavePeers.contains(pid) && !p2pNetworkManager.getLocalPeerID().equals(pid)) {
				slavePeers.add(pid);
			}
		}
		return slavePeers;
	}

	private static void callExecutorToAddLocalQueries(ILogicalQuery query, ID sharedQueryID, IServerExecutor serverExecutor, ISession caller, QueryBuildConfiguration config, Collection<PeerID> slavePeers) throws QueryDistributionException {
		try {
			LOG.debug("Adding local query to serverExecutor now.");

			StringBuilder sb = new StringBuilder();
			sb.append("#PARSER PQL\n");
			sb.append("#QNAME " + query.getName() + "\n");
			sb.append("#ADDQUERY\n");
			sb.append(query.getQueryText());
			sb.append("\n");
			String scriptText = sb.toString();

			List<IQueryBuildSetting<?>> configuration = determineQueryBuildSettings(serverExecutor, "Standard");
			Optional<ParameterTransformationConfiguration> params = getParameterTransformationConfiguration(configuration);
			if (params.isPresent()) {
				ParameterTransformationConfiguration paramConfiguration = config.get(ParameterTransformationConfiguration.class);
				Collection<String> metaTypes = paramConfiguration.getValue().getDefaultMetaTypeSet();

				params.get().getValue().addTypes(Sets.newHashSet(metaTypes));
			}
			Collection<Integer> queryIDs = serverExecutor.addQuery(scriptText, "OdysseusScript", PeerDistributePlugIn.getActiveSession(), "Standard", Context.empty(), configuration);

			QueryPartController.getInstance().registerAsMaster(query, queryIDs.iterator().next(), sharedQueryID, slavePeers);
			LOG.debug("Local query added");
		} catch (Throwable ex) {
			throw new QueryDistributionException("Could not add local query to server executor", ex);
		}
	}

	private static List<IQueryBuildSetting<?>> determineQueryBuildSettings(IServerExecutor executor, String cfgName) {
		final IQueryBuildConfigurationTemplate qbc = executor.getQueryBuildConfiguration(cfgName);
		final List<IQueryBuildSetting<?>> configuration = qbc.getConfiguration();

		final List<IQueryBuildSetting<?>> settings = Lists.newArrayList();
		settings.addAll(configuration);
		settings.add(ParameterDoRewrite.FALSE);
		return settings;
	}

	private static Optional<ParameterTransformationConfiguration> getParameterTransformationConfiguration(List<IQueryBuildSetting<?>> settings) {
		for (IQueryBuildSetting<?> s : settings) {
			if (s instanceof ParameterTransformationConfiguration) {
				return Optional.of((ParameterTransformationConfiguration) s);
			}
		}

		return Optional.absent();
	}
}
