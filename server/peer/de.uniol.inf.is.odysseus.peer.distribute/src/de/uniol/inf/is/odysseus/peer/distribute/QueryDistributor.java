package de.uniol.inf.is.odysseus.peer.distribute;

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
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.IQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.RepeatingMessageSend;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.distribute.adv.QueryPartController;
import de.uniol.inf.is.odysseus.peer.distribute.adv.QueryPartManager;
import de.uniol.inf.is.odysseus.peer.distribute.message.AddQueryPartMessage;
import de.uniol.inf.is.odysseus.peer.distribute.message.QueryPartAddAckMessage;
import de.uniol.inf.is.odysseus.peer.distribute.util.IOperatorGenerator;
import de.uniol.inf.is.odysseus.peer.distribute.util.InterfaceParametersPair;
import de.uniol.inf.is.odysseus.peer.distribute.util.LoggingHelper;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.distribute.util.ParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.util.QueryDistributorHelper;

public class QueryDistributor implements IQueryDistributor, IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(QueryDistributor.class);

	private static IPQLGenerator pqlGenerator;
	private static IJxtaServicesProvider jxtaServicesProvider;
	private static IP2PDictionary p2pDictionary;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;

	private static int queryPartIDCounter = 0;
	private static Map<Integer, RepeatingMessageSend> senderMap = Maps.newConcurrentMap();
	private static Collection<Integer> processQueryPartIDs = Lists.newLinkedList();

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
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;
	}

	// called by OSGi-DS
	public static void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		if (jxtaServicesProvider == serv) {
			jxtaServicesProvider = null;
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
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
		LOG.debug("Bound p2p network manager {}", serv);
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}

	// called by OSGi-DS
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;

		peerCommunicator.registerMessageType(AddQueryPartMessage.class);
		peerCommunicator.registerMessageType(QueryPartAddAckMessage.class);
		peerCommunicator.addListener(this, AddQueryPartMessage.class);
		peerCommunicator.addListener(this, QueryPartAddAckMessage.class);
	}

	// called by OSGi-DS
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this, AddQueryPartMessage.class);
			peerCommunicator.removeListener(this, QueryPartAddAckMessage.class);
			peerCommunicator.unregisterMessageType(AddQueryPartMessage.class);
			peerCommunicator.unregisterMessageType(QueryPartAddAckMessage.class);

			peerCommunicator = null;
		}
	}

	@Override
	public void distribute(final IServerExecutor serverExecutor, final ISession caller, final Collection<ILogicalQuery> queriesToDistribute, final QueryBuildConfiguration config) {
		QueryDistributorThread thread = new QueryDistributorThread(serverExecutor, caller, queriesToDistribute, config);
		thread.start(); // calls distributeImpl (async)
	}

	static void distributeImpl(IServerExecutor serverExecutor, ISession caller, Collection<ILogicalQuery> queriesToDistribute, QueryBuildConfiguration config) throws QueryDistributionException {
		Preconditions.checkNotNull(serverExecutor, "Server executor for distributing queries must not be null!");
		Preconditions.checkNotNull(caller, "Caller must not be null!");
		Preconditions.checkNotNull(queriesToDistribute, "Collection of queries to distribute must not be null!");
		Preconditions.checkNotNull(config, "QueryBuildConfiguration for distribution must not be null!");

		if (queriesToDistribute.isEmpty()) {
			LOG.warn("Collection of queries to distribute is empty!");
			return;
		}

		LOG.debug("Begin with distributing queries");
		Collection<ILogicalQuery> queries = copyAllQueries(queriesToDistribute);

		LOG.debug("{} queries will be distributed if possible.", queries.size());

		List<InterfaceParametersPair<IQueryDistributionPreProcessor>> preProcessors = ParameterHelper.determineQueryDistributionPreProcessors(config);
		List<InterfaceParametersPair<IQueryPartitioner>> partitioners = ParameterHelper.determineQueryPartitioners(config);
		List<InterfaceParametersPair<IQueryPartModificator>> modificators = ParameterHelper.determineQueryPartModificators(config);
		List<InterfaceParametersPair<IQueryPartAllocator>> allocators = ParameterHelper.determineQueryPartAllocators(config);
		List<InterfaceParametersPair<IQueryDistributionPostProcessor>> postProcessors = ParameterHelper.determineQueryDistributionPostProcessors(config);

		LoggingHelper.printUsedInterfaces(preProcessors, partitioners, modificators, allocators, postProcessors);

		for (ILogicalQuery query : queries) {
			LOG.debug("Start distribution of query {}", query);

			QueryDistributorHelper.tryPreProcess(serverExecutor, caller, config, preProcessors, query);

			Collection<ILogicalOperator> operators = QueryDistributorHelper.collectRelevantOperators(query);
			LOG.debug("Following operators are condidered during distribution: {}", operators);

			QueryDistributorHelper.tryCheckDistribution(config, query, operators);
			Collection<ILogicalQueryPart> queryParts = QueryDistributorHelper.tryPartitionQuery(config, partitioners, operators, query);
			Collection<ILogicalQueryPart> modifiedQueryParts = QueryDistributorHelper.tryModifyQueryParts(config, modificators, queryParts, query);
			Map<ILogicalQueryPart, PeerID> allocationMap = QueryDistributorHelper.tryAllocate(config, allocators, modifiedQueryParts, query);

			QueryDistributorHelper.tryPostProcess(serverExecutor, caller, allocationMap, config, postProcessors, query);

			ID sharedQueryID = IDFactory.newContentID(p2pNetworkManager.getLocalPeerGroupID(), false, String.valueOf(System.currentTimeMillis()).getBytes());
			insertJxtaOperators(allocationMap);
			replaceAccessAOsOfExportedViews(allocationMap.keySet());
			Collection<ILogicalQueryPart> localQueryParts = distributeToRemotePeers(sharedQueryID, allocationMap, config);

			if (!localQueryParts.isEmpty()) {
				LOG.debug("Building local logical query out of {} local query parts", localQueryParts.size());

				ILogicalQuery localQuery = buildLocalQuery(localQueryParts);
				localQuery.setName(query.getName());
				localQuery.setUser(caller);

				callExecutorToAddLocalQueries(localQuery, sharedQueryID, serverExecutor, caller, config, determineSlavePeers(allocationMap));
			} else {
				LOG.debug("No local query part of query {} remains.", query);
			}
		}

	}

	private static Collection<PeerID> determineSlavePeers(Map<ILogicalQueryPart, PeerID> allocationMap) {
		Collection<PeerID> slavePeers = Lists.newArrayList();
		for( ILogicalQueryPart queryPart : allocationMap.keySet() ) {
			PeerID pid = allocationMap.get(queryPart);
			if( !slavePeers.contains(pid) && !p2pNetworkManager.getLocalPeerID().equals(pid)) {
				slavePeers.add(pid);
			}
		}
		return slavePeers;
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
						receiverOperator.setOutputSchema(advertisement.getOutputSchema());
						receiverOperator.setSchema(advertisement.getOutputSchema().getAttributes());
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

	private static Collection<ILogicalQuery> copyAllQueries(Collection<ILogicalQuery> queriesToDistribute) {
		Collection<ILogicalQuery> copiedQueries = Lists.newArrayList();
		for (ILogicalQuery queryToDistribute : queriesToDistribute) {
			copiedQueries.add(LogicalQueryHelper.copyLogicalQuery(queryToDistribute));
		}

		return copiedQueries;
	}

	private static void callExecutorToAddLocalQueries(ILogicalQuery query, ID sharedQueryID, IServerExecutor serverExecutor, ISession caller, QueryBuildConfiguration config, Collection<PeerID> slavePeers)
			throws QueryDistributionException {
		try {
			int queryID = serverExecutor.addQuery(query.getLogicalPlan(), caller, config.getName());

			QueryPartController.getInstance().registerAsMaster(query, queryID, sharedQueryID, slavePeers);
		} catch (Throwable ex) {
			throw new QueryDistributionException("Could not add local query to server executor", ex);
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
				access.setPipeID(pipeID.toString());
				access.setSchema(sourceOp.getOutputSchema().getAttributes());
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

	private static Collection<ILogicalQueryPart> distributeToRemotePeers(ID sharedQueryID, Map<ILogicalQueryPart, PeerID> correctedAllocationMap, QueryBuildConfiguration parameters) {
		List<ILogicalQueryPart> localParts = Lists.newArrayList();

		for (ILogicalQueryPart part : correctedAllocationMap.keySet()) {
			PeerID peerID = correctedAllocationMap.get(part);

			if (peerID.equals(p2pNetworkManager.getLocalPeerID())) {
				localParts.add(part);
				LOG.debug("Query part {} stays local", part);

			} else {
				AddQueryPartMessage msg = new AddQueryPartMessage(sharedQueryID, LogicalQueryHelper.generatePQLStatementFromQueryPart(part), parameters.getName(), queryPartIDCounter++);

				RepeatingMessageSend msgSender = new RepeatingMessageSend(peerCommunicator, msg, peerID);
				senderMap.put(msg.getQueryPartID(), msgSender);
				msgSender.start();

				LOG.debug("Sent query part {} to peerID {}", part, peerID);
				LOG.debug("PQL-Query of query part {} is\n{}", part, msg.getPqlStatement());
			}
		}
		return localParts;
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if( message instanceof AddQueryPartMessage ) {
			LOG.debug("Received AddQueryPartMessage");
	
			AddQueryPartMessage addQueryPartMessage = (AddQueryPartMessage) message;
			
			if( !processQueryPartIDs.contains(addQueryPartMessage.getQueryPartID())) {
				processQueryPartIDs.add(addQueryPartMessage.getQueryPartID());
				
				QueryPartAddAckMessage ackMessage = new QueryPartAddAckMessage(addQueryPartMessage);
				try {
					communicator.send(senderPeer, ackMessage);
				} catch (PeerCommunicationException e) {
					LOG.debug("Send query part add ack message failed", e);
				}
		
				QueryPartManager.getInstance().addQueryPart(addQueryPartMessage);
			}
		} else if( message instanceof QueryPartAddAckMessage ) {
			QueryPartAddAckMessage ackMessage = (QueryPartAddAckMessage)message;
			
			RepeatingMessageSend sender = senderMap.get(ackMessage.getQueryPartID());
			if( sender != null ) {
				sender.stopRunning();
				senderMap.remove(ackMessage.getQueryPartID());
			}
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
}
