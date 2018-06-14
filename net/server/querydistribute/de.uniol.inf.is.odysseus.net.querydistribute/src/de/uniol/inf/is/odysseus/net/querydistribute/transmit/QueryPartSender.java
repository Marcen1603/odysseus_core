package de.uniol.inf.is.odysseus.net.querydistribute.transmit;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartTransmissionException;
import de.uniol.inf.is.odysseus.net.querydistribute.activator.QueryDistributionPlugIn;
import de.uniol.inf.is.odysseus.net.querydistribute.impl.QueryDistributionComponent;
import de.uniol.inf.is.odysseus.net.querydistribute.logicaloperator.DistributedQueryRepresentationAO;
import de.uniol.inf.is.odysseus.net.querydistribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public class QueryPartSender implements IOdysseusNodeCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartSender.class);

	private static IPQLGenerator pqlGenerator;
	private static IOdysseusNodeCommunicator nodeCommunicator;

	private static QueryPartSender instance;

	private static int queryPartIDCounter = 0;

	private final Map<Integer, IOdysseusNode> sendDestinationMap = Maps.newConcurrentMap();
	private final Map<Integer, String> sendResultMap = Maps.newConcurrentMap();

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
	public void bindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		nodeCommunicator = serv;

		nodeCommunicator.registerMessageType(QueryPartAddAckMessage.class);
		nodeCommunicator.registerMessageType(QueryPartAddFailMessage.class);
		nodeCommunicator.registerMessageType(AbortQueryPartAddMessage.class);

		nodeCommunicator.addListener(this, QueryPartAddAckMessage.class);
		nodeCommunicator.addListener(this, QueryPartAddFailMessage.class);
	}

	// called by OSGi-DS
	public void unbindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		if (nodeCommunicator == serv) {
			nodeCommunicator.removeListener(this, QueryPartAddAckMessage.class);
			nodeCommunicator.removeListener(this, QueryPartAddFailMessage.class);

			nodeCommunicator.unregisterMessageType(QueryPartAddAckMessage.class);
			nodeCommunicator.unregisterMessageType(QueryPartAddFailMessage.class);
			nodeCommunicator.unregisterMessageType(AbortQueryPartAddMessage.class);

			nodeCommunicator = null;
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

	public UUID transmit(Map<ILogicalQueryPart, IOdysseusNode> allocationMap, IServerExecutor serverExecutor,
			ISession caller, QueryBuildConfiguration config)
			throws QueryPartTransmissionException, QueryDistributionException {
		LOG.debug("Beginning transmission...");

		IOdysseusNode localNode = null;
		try {
			localNode = QueryDistributionComponent.getLocalNode();
		} catch (OdysseusNetException e) {
			throw new QueryDistributionException("Local node not available since OdysseusNet is not started");
		}
		UUID sharedQueryID = UUID.randomUUID();

		insertSenderAndReceiverOperators(allocationMap);
		distributeToRemotePeers(sharedQueryID, serverExecutor, caller, allocationMap, localNode, config);
		Collection<ILogicalQueryPart> localQueryParts = determineLocalQueryParts(allocationMap, localNode);

		if (localQueryParts.isEmpty()) {
			LOG.debug("There is no local query. Building a representation operator.");

			LogicalQueryPart part = new LogicalQueryPart(new DistributedQueryRepresentationAO(sharedQueryID));
			localQueryParts.add(part);
		}

		LOG.debug("Building local logical query out of {} local query parts", localQueryParts.size());

		ILogicalQuery localQuery = buildLocalQuery(localQueryParts);
		localQuery.setUser(caller);

		try {
			callExecutorToAddLocalQueries(localQuery, sharedQueryID, serverExecutor, caller, config,
					determineSlavePeers(allocationMap, localNode));
		} catch (Throwable t) {
			LOG.error("Exception during placing local query part. Query is {}", localQuery.getQueryText());
			LOG.error("Exception is ", t);
			removeDistributedQueryParts(sharedQueryID);
		}

		LOG.debug("Transmission finished");
		return sharedQueryID;
	}

	private static Collection<ILogicalQueryPart> determineLocalQueryParts(
			Map<ILogicalQueryPart, IOdysseusNode> allocationMap, IOdysseusNode localNode) {
		List<ILogicalQueryPart> localParts = Lists.newArrayList();

		for (ILogicalQueryPart part : allocationMap.keySet()) {
			IOdysseusNode node = allocationMap.get(part);

			if (node.equals(localNode)) {
				localParts.add(part);
				LOG.debug("Query part {} stays local", part);
			}
		}

		return localParts;
	}

	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		if (message instanceof QueryPartAddAckMessage) {
			QueryPartAddAckMessage ackMessage = (QueryPartAddAckMessage) message;
			sendResultMap.put(ackMessage.getQueryPartID(), "OK");

		} else if (message instanceof QueryPartAddFailMessage) {
			QueryPartAddFailMessage failMessage = (QueryPartAddFailMessage) message;
			sendResultMap.put(failMessage.getQueryPartID(), failMessage.getMessage());

		}
	}

	private static void insertSenderAndReceiverOperators(final Map<ILogicalQueryPart, IOdysseusNode> allocationMap)
			throws QueryPartTransmissionException {
		LogicalQueryHelper.disconnectQueryParts(allocationMap,
				new NetworkConnectionOperatorGenerator(nodeCommunicator));

		// LogicalQueryHelper.disconnectQueryParts(allocationMap, new
		// IOperatorGenerator() {
		//
		// private UUID connectionID;
		// private ILogicalOperator sourceOp;
		//
		// private ILogicalQueryPart sourceQueryPart;
		// private ILogicalQueryPart sinkQueryPart;
		//
		// @Override
		// public void beginDisconnect(ILogicalQueryPart sourceQueryPart,
		// ILogicalOperator sourceOperator, IOdysseusNode sourceNode,
		// ILogicalQueryPart sinkQueryPart, ILogicalOperator sinkOperator,
		// IOdysseusNode sinkNode) {
		// LOG.debug("Create JXTA-Connection between {} and {}", new Object[] {
		// sourceOperator, sinkOperator });
		//
		// connectionID = UUID.randomUUID();
		// sourceOp = sourceOperator;
		//
		// this.sourceQueryPart = sourceQueryPart;
		// this.sinkQueryPart = sinkQueryPart;
		// }
		//
		// @Override
		// public ILogicalOperator createSourceofSink(ILogicalQueryPart
		// sinkQueryPart, ILogicalOperator sink) {
		// SharedQueryReceiverAO receiverAO = new SharedQueryReceiverAO();
		// Optional<String> optString =
		// LogicalQueryHelper.getBaseTimeunitString(sink.getOutputSchema());
		// if (optString.isPresent()) {
		// receiverAO.setBaseTimeunit(optString.get());
		// }
		// receiverAO.setConnectionID(connectionID.toString());
		// receiverAO.setAttributes(sourceOp.getOutputSchema().getAttributes());
		// receiverAO.setLocalMetaAttribute(MetadataRegistry.getMetadataType(sourceOp.getOutputSchema().getMetaAttributeNames()));
		// receiverAO.setSchemaName(sourceOp.getOutputSchema().getURI());
		// receiverAO.setOdysseusNodeID(allocationMap.get(sourceQueryPart).getID().toString());
		//
		// return receiverAO;
		// }
		//
		// @Override
		// public ILogicalOperator createSinkOfSource(ILogicalQueryPart
		// sourceQueryPart, ILogicalOperator source) {
		// SharedQuerySenderAO senderAO = new SharedQuerySenderAO();
		// senderAO.setConnectionID(connectionID.toString());
		// senderAO.setOdysseusNodeID(allocationMap.get(sinkQueryPart).getID().toString());
		// return senderAO;
		// }
		//
		// @Override
		// public void endDisconnect() {
		// connectionID = null;
		// sourceOp = null;
		// }
		// });
	}

	private void distributeToRemotePeers(UUID sharedQueryID, IServerExecutor serverExecutor, ISession caller,
			Map<ILogicalQueryPart, IOdysseusNode> allocationMap, IOdysseusNode localNode,
			QueryBuildConfiguration parameters) throws QueryPartTransmissionException {
		sendDestinationMap.clear();
		sendResultMap.clear();

		for (ILogicalQueryPart part : allocationMap.keySet()) {
			IOdysseusNode allocatedNode = allocationMap.get(part);

			if (!allocatedNode.equals(localNode)) {
				String queryText = generateOdysseusScript(part, parameters);
				AddQueryPartMessage msg = new AddQueryPartMessage(sharedQueryID, queryText, "OdysseusScript", parameters.getName(),
						queryPartIDCounter++);

				try {
					nodeCommunicator.send(allocatedNode, msg);

				} catch (OdysseusNodeCommunicationException e) {
					removeDistributedQueryParts(sharedQueryID);
					throw new QueryPartTransmissionException(Lists.newArrayList(allocatedNode),
							"Could not send add query part message to " + allocatedNode, e);
				}

				sendDestinationMap.put(msg.getQueryPartID(), allocatedNode);

				LOG.debug("Sent query part {} to node {} (queryPartID = {})",
						new Object[] { part, allocatedNode, msg.getQueryPartID() });
				LOG.trace("PQL-Query of query part {} is\n{}", part, msg.getQueryText());
			}
		}

		waitForNodeAnswers();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Response results: ");
			for (Integer qpid : sendResultMap.keySet()) {
				LOG.debug("\tQueryPartID {} (Peer {}) : {}",
						new Object[] { qpid, sendDestinationMap.get(qpid), sendResultMap.get(qpid) });
			}
		}

		if (sendResultMap.size() != sendDestinationMap.size()) {
			// not every node had answered... aborting

			List<IOdysseusNode> missingNodes = Lists.newArrayList();
			for (Integer qpid : sendDestinationMap.keySet()) {
				if (!sendResultMap.containsKey(qpid)) {
					missingNodes.add(sendDestinationMap.get(qpid));
				}
			}

			if (LOG.isErrorEnabled()) {
				LOG.error("Could not distribute the query parts since nodes are not reachable");
				for (IOdysseusNode missingNode : missingNodes) {
					LOG.error("\t{}", missingNode);
				}
			}

			removeDistributedQueryParts(sharedQueryID);

			throw new QueryPartTransmissionException(missingNodes);
		}

		List<IOdysseusNode> faultyNodes = Lists.newArrayList();
		List<String> faultMessages = Lists.newArrayList();
		for (Integer qpid : sendResultMap.keySet()) {
			String msg = sendResultMap.get(qpid);
			IOdysseusNode node = sendDestinationMap.get(qpid);
			if (msg == null || !msg.equalsIgnoreCase("OK")) {
				faultyNodes.add(node);
				faultMessages.add(msg);
			}
		}

		if (!faultyNodes.isEmpty()) {
			if (LOG.isErrorEnabled()) {
				LOG.error("Could not distribute the query since nodes could not execute/add its query part:");
				for (int index = 0; index < faultyNodes.size(); index++) {
					LOG.error("\t{}: {}", faultyNodes.get(index), faultMessages.get(index));
				}
			}

			removeDistributedQueryParts(sharedQueryID);

			throw new QueryPartTransmissionException(faultyNodes);
		}

		LOG.debug("Remote distribution complete.");
	}

	private static String generateOdysseusScript(ILogicalQueryPart part, QueryBuildConfiguration parameters) {
		StringBuilder builder = new StringBuilder();
		builder.append("#PARSER PQL").append("\n");
		for (IQueryBuildSetting<?> parameter : parameters.values()) {
			String paramText = null;
			try {
				paramText = parameter.toOdysseusScript();
			} catch (UnsupportedOperationException e) {
				LOG.warn(
						"Can not send query build setting {} with query part, because it does not implement the method 'toOdysseusScript'.",
						parameter.getClass().getSimpleName());
				continue;
			}
			builder.append(paramText).append("\n");
		}
		builder.append("#ADDQUERY").append("\n");
		builder.append(LogicalQueryHelper.generatePQLStatementFromQueryPart(part)).append("\n");
		return builder.toString();
	}

	private void waitForNodeAnswers() {
		long waitTime = 0;

		// if they are equal, every node we contacted has answered
		while (sendResultMap.size() != sendDestinationMap.size()) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
			}

			// we do not want to wait too long
			waitTime += 300;
			if (waitTime > 15 * 1000) {
				break;
			}
		}
	}

	private void removeDistributedQueryParts(UUID sharedQueryID) {
		LOG.debug("Removing already distributed query parts remotely");
		Collection<IOdysseusNode> sendNodes = Lists.newLinkedList();
		for (Integer qpid : sendDestinationMap.keySet()) {
			IOdysseusNode destination = sendDestinationMap.get(qpid);

			if (!sendNodes.contains(destination)) {
				sendNodes.add(destination);

				LOG.debug("Send abort to node {}", destination);
				try {
					nodeCommunicator.send(destination, new AbortQueryPartAddMessage(sharedQueryID));
				} catch (OdysseusNodeCommunicationException e) {
					LOG.error("Could not send abort query part add message to node {}", destination, e);
				}
			}
		}

		sendResultMap.clear();
		sendDestinationMap.clear();
	}

	private static ILogicalQuery buildLocalQuery(Collection<ILogicalQueryPart> localQueryParts) {
		Collection<ILogicalOperator> sinks = Lists.newArrayList();
		for (ILogicalQueryPart localPart : localQueryParts) {
			Collection<ILogicalOperator> partOperators = localPart.getOperators();

			Collection<ILogicalOperator> allOperators = Lists.newArrayList();
			for (ILogicalOperator operator : partOperators) {
				for (ILogicalOperator op : LogicalPlan.getAllOperators(operator)) {
					if (!allOperators.contains(op)) {
						allOperators.add(op);
					}
				}
			}
			sinks.addAll(LogicalPlan.getSinks(allOperators));
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

	private static Collection<IOdysseusNode> determineSlavePeers(Map<ILogicalQueryPart, IOdysseusNode> allocationMap,
			IOdysseusNode localNode) {
		Collection<IOdysseusNode> slaveNodes = Lists.newArrayList();
		for (ILogicalQueryPart queryPart : allocationMap.keySet()) {
			IOdysseusNode node = allocationMap.get(queryPart);
			if (!slaveNodes.contains(node) && !localNode.equals(node)) {
				slaveNodes.add(node);
			}
		}
		return slaveNodes;
	}

	private static void callExecutorToAddLocalQueries(ILogicalQuery query, UUID sharedQueryID,
			IServerExecutor serverExecutor, ISession caller, QueryBuildConfiguration config,
			Collection<IOdysseusNode> slaveNodes) throws QueryDistributionException {
		try {
			LOG.debug("Adding local query to serverExecutor now.");

			StringBuilder sb = new StringBuilder();
			sb.append("#PARSER PQL\n");
			sb.append("#ADDQUERY\n");
			sb.append(query.getQueryText());
			sb.append("\n");
			String scriptText = sb.toString();

			List<IQueryBuildSetting<?>> configuration = determineQueryBuildSettings(serverExecutor, "Standard");
			Optional<ParameterTransformationConfiguration> params = getParameterTransformationConfiguration(
					configuration);
			if (params.isPresent()) {
				ParameterTransformationConfiguration paramConfiguration = config
						.get(ParameterTransformationConfiguration.class);
				Collection<String> metaTypes = paramConfiguration.getValue().getDefaultMetaTypeSet();

				params.get().getValue().addTypes(Sets.newHashSet(metaTypes));
			}
			Collection<Integer> queryIDs = serverExecutor.addQuery(scriptText, "OdysseusScript",
					QueryDistributionPlugIn.getActiveSession(), "Standard", Context.empty(), configuration);

			QueryPartController.getInstance().registerAsMaster(query, queryIDs.iterator().next(), sharedQueryID,
					slaveNodes);
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

	private static Optional<ParameterTransformationConfiguration> getParameterTransformationConfiguration(
			List<IQueryBuildSetting<?>> settings) {
		for (IQueryBuildSetting<?> s : settings) {
			if (s instanceof ParameterTransformationConfiguration) {
				return Optional.of((ParameterTransformationConfiguration) s);
			}
		}

		return Optional.absent();
	}
}
