package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator.LoadBalancingSynchronizerPO;

public class LoadBalancingCommunicationListener implements
		IPeerCommunicatorListener {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(LoadBalancingCommunicationListener.class);

	private static IServerExecutor executor;
	private static IPeerCommunicator peerCommunicator;
	private static IP2PNetworkManager p2pNetworkManager;
	private static LoadBalancingCommunicationListener instance;
	private static ISession activeSession;

	private class ConnectionToOperator {

		ConnectionToOperator(ILogicalOperator operator, String remotePeerID,
				String oldPipeID, int port, SDFSchema schema) {
			this.localOperator = operator;
			this.remotePeerID = remotePeerID;
			this.oldPipeID = oldPipeID;
			this.port = port;
			this.schema = schema;
		}

		private String remotePeerID;
		private ILogicalOperator localOperator;
		private String oldPipeID;
		private int port;
		private SDFSchema schema;
	};

	@SuppressWarnings("unused")
	private ArrayList<Integer> activeLoadBalancingProcesses;

	public static LoadBalancingCommunicationListener getInstance() {
		return instance;
	}

	public void activate() {
		this.activeLoadBalancingProcesses = new ArrayList<Integer>();
		instance = this;
		LOG.debug("Instantiated");
	}

	public void deactivate() {
		this.activeLoadBalancingProcesses = null;
		LOG.debug("Deinitialized");
		instance = null;
	}

	// called by OSGi-DS
	public static void bindExecutor(IExecutor exe) {
		LOG.debug("Bound Executor.");
		executor = (IServerExecutor) exe;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor exe) {
		LOG.debug("Unbound Executor.");
		if (executor == exe) {
			executor = null;
		}
	}

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		LOG.debug("Bound network Manager");
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		LOG.debug("Unbound NetworkMananger");
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}

	// called by OSGi-DS
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		LOG.debug("Bound Peer Communicator.");
		peerCommunicator = serv;
		peerCommunicator
				.registerMessageType(LoadBalancingInitiateCopyMessage.class);
		peerCommunicator
				.registerMessageType(LoadBalancingInitiateMessage.class);
		peerCommunicator
				.registerMessageType(LoadBalancingCopyConnectionMessage.class);
		peerCommunicator
				.registerMessageType(LoadBalancingAddQueryMessage.class);

		peerCommunicator.addListener(this,
				LoadBalancingInitiateCopyMessage.class);
		peerCommunicator.addListener(this, LoadBalancingInitiateMessage.class);
		peerCommunicator.addListener(this,
				LoadBalancingCopyConnectionMessage.class);
		peerCommunicator.addListener(this, LoadBalancingAddQueryMessage.class);
	}

	// called by OSGi-DS
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		LOG.debug("Unbound Peer Communicator.");
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this,
					LoadBalancingInitiateCopyMessage.class);
			peerCommunicator.removeListener(this,
					LoadBalancingInitiateMessage.class);
			peerCommunicator
					.unregisterMessageType(LoadBalancingInitiateCopyMessage.class);
			peerCommunicator
					.unregisterMessageType(LoadBalancingInitiateMessage.class);
			peerCommunicator = null;
		}
	}

	private void relinkQueryPart(int lbProcessId, ILogicalQueryPart part,
			String newPeerID) {
		removeTopAOs(part);
		Map<ILogicalOperator, Collection<ConnectionToOperator>> incomingConnections = stripJxtaReceivers(part);
		Map<ILogicalOperator, Collection<ConnectionToOperator>> outgoingConnections = stripJxtaSenders(part);
		Collection<ILogicalOperator> relativeSources = LogicalQueryHelper
				.getRelativeSinksOfLogicalQueryPart(part);
		Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper
				.getRelativeSourcesOfLogicalQueryPart(part);

		for (ILogicalOperator relativeSource : relativeSources) {
			if (incomingConnections.containsKey(relativeSource)) {
				Collection<ConnectionToOperator> connections = incomingConnections
						.get(relativeSource);
				for (ConnectionToOperator connection : connections) {

					String newPipeID = IDFactory.newPipeID(
							p2pNetworkManager.getLocalPeerGroupID()).toString();
					JxtaReceiverAO receiver = new JxtaReceiverAO();

					receiver.setPipeID(newPipeID);
					receiver.setPeerID(connection.remotePeerID);
					receiver.setSchema(connection.schema.getAttributes());
					receiver.connectSink(relativeSource, connection.port, 0,
							relativeSource.getInputSchema(0));
					sendCopyConnectionMessage(true, lbProcessId,
							toPeerID(connection.remotePeerID),
							connection.oldPipeID.toString(), newPipeID,
							newPeerID);
				}
			}
		}

		for (ILogicalOperator relativeSink : relativeSinks) {
			if (outgoingConnections.containsKey(relativeSink)) {
				Collection<ConnectionToOperator> connections = outgoingConnections
						.get(relativeSink);
				for (ConnectionToOperator connection : connections) {
					String newPipeID = IDFactory.newPipeID(
							p2pNetworkManager.getLocalPeerGroupID()).toString();

					JxtaSenderAO sender = new JxtaSenderAO();
					sender.setPeerID(connection.remotePeerID);
					sender.setPipeID(newPipeID);
					sender.setOutputSchema(connection.schema);
					connection.localOperator.connectSink(sender, 0,
							connection.port,
							connection.localOperator.getOutputSchema());
					sendCopyConnectionMessage(false, lbProcessId,
							toPeerID(connection.remotePeerID),
							connection.oldPipeID.toString(), newPipeID,
							newPeerID);
				}
			}
		}

	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {

		if (message instanceof LoadBalancingInitiateMessage) {
			// TODO Check if loadBalancing ok.
			// Answer
			sendCopyMessage(senderPeer,
					((LoadBalancingInitiateMessage) message)
							.getLoadBalancingProcessId());
		}

		if (message instanceof LoadBalancingInitiateCopyMessage) {
			int loadBalancingProcessId = ((LoadBalancingInitiateCopyMessage) message)
					.getLoadBalancingProcessId();
			ILogicalQueryPart part = LoadBalancingQueryCache.getInstance()
					.getQueryPart(loadBalancingProcessId);
			ILogicalQueryPart copy = getCopyOfQueryPart(part);
			relinkQueryPart(loadBalancingProcessId, copy, senderPeer.toString());
			String pqlFromQueryPart = LogicalQueryHelper
					.generatePQLStatementFromQueryPart(copy);
			LOG.debug("Created PQL from Query Part: " + pqlFromQueryPart);
			sendAddQueryPartMessage(loadBalancingProcessId, senderPeer,
					pqlFromQueryPart);
			// TODO Order of Messages
		}

		if (message instanceof LoadBalancingAddQueryMessage) {
			LoadBalancingAddQueryMessage queryMessage = (LoadBalancingAddQueryMessage) message;
			installQueryPartFromPql(queryMessage.getPqlQuery());
			// TODO Ack.
		}

		if (message instanceof LoadBalancingCopyConnectionMessage) {
			LoadBalancingCopyConnectionMessage copyMessage = (LoadBalancingCopyConnectionMessage) message;
			findAndCopyLocalJxtaOperator(copyMessage.isSender(),
					copyMessage.getNewPeerId(), copyMessage.getOldPipeId(),
					copyMessage.getNewPipeId());
		}

	}

	/**
	 * Sends a copy Message to initiating Peer, this message initiates a copy
	 * process for given LoadBalancing Process. This should be an answer to the
	 * initiating peer, after being accepted as loadbalancing partner.
	 * 
	 * @param destinationPeerId
	 *            initiating Peer
	 */
	public void sendCopyMessage(PeerID destinationPeerId,
			int loadBalancingProcessId) {
		try {
			if (peerCommunicator != null) {
				peerCommunicator.send(destinationPeerId,
						new LoadBalancingInitiateCopyMessage(
								loadBalancingProcessId));
			}
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send Message");
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void findAndCopyLocalJxtaOperator(boolean isSender,
			String newPeerId, String oldPipeId, String newPipeId) {
		ILogicalOperator operator = getLogicalJxtaOperator(isSender, oldPipeId);
		if (operator != null) {
			if (isSender) {
				JxtaSenderAO logicalSender = (JxtaSenderAO) operator;
				JxtaSenderAO copy = (JxtaSenderAO) logicalSender.clone();
				copy.setPipeID(newPipeId);
				copy.setPeerID(newPeerId);
				try {
					JxtaSenderPO physicalCopy = new JxtaSenderPO(copy);
					JxtaSenderPO physicalOriginal = (JxtaSenderPO) getPhysicalJxtaOperator(
							isSender, oldPipeId);
					physicalCopy.setOutputSchema(physicalOriginal
							.getOutputSchema());

					PhysicalSubscription subscription = physicalOriginal
							.getSubscribedToSource(0);

					if (subscription.getTarget() instanceof AbstractPipe) {
						((AbstractPipe) subscription.getTarget())
								.subscribeSink(physicalCopy, 0,
										subscription.getSourceOutPort(),
										subscription.getSchema(), true,
										subscription.getOpenCalls());
					} else if (subscription.getTarget() instanceof AbstractSource) {
						((AbstractSource) subscription.getTarget())
								.subscribeSink(physicalCopy, 0,
										subscription.getSourceOutPort(),
										subscription.getSchema(), true,
										subscription.getOpenCalls());
					}
				} catch (DataTransmissionException e) {
					LOG.debug("Did not go as expected.");
				}

			} else {
				JxtaReceiverAO logicalReceiver = (JxtaReceiverAO) operator;
				JxtaReceiverAO copy = (JxtaReceiverAO) logicalReceiver.clone();
				copy.setPipeID(newPipeId);
				copy.setPeerID(newPeerId);
				copy.setSchema(logicalReceiver.getSchema());
				try {
					JxtaReceiverPO physicalCopy = new JxtaReceiverPO(copy);
					JxtaReceiverPO physicalOriginal = (JxtaReceiverPO) getPhysicalJxtaOperator(
							isSender, oldPipeId);
					physicalCopy.setOutputSchema(physicalOriginal
							.getOutputSchema());

					LoadBalancingSynchronizerPO<IStreamObject<ITimeInterval>> synchronizer = new LoadBalancingSynchronizerPO<IStreamObject<ITimeInterval>>(
							new TITransferArea<IStreamObject<ITimeInterval>, IStreamObject<ITimeInterval>>());

					physicalOriginal.block();

					List<PhysicalSubscription<ISink<? super IStreamObject>>> subscriptionList = physicalOriginal
							.getSubscriptions();

					for (PhysicalSubscription<ISink<? super IStreamObject>> subscription : subscriptionList) {
						physicalOriginal.unsubscribeSink(subscription);
						synchronizer.subscribeSink(subscription.getTarget(),
								subscription.getSinkInPort(),
								subscription.getSourceOutPort(),
								subscription.getSchema(), true,
								subscription.getOpenCalls());
					}

					physicalOriginal.subscribeSink(synchronizer, 0, 0,
							physicalOriginal.getOutputSchema());
					physicalCopy.subscribeSink(synchronizer, 1, 0,
							physicalOriginal.getOutputSchema());

					physicalOriginal.unblock();
				} catch (DataTransmissionException e) {
					LOG.debug("Did not go as expected.");
				}
			}
		}

	}

	private void installQueryPartFromPql(String pql) {
		// TODO Check if buildConfiguration is ok...
		Collection<Integer> installedQueries = executor.addQuery(pql, "PQL",
				getActiveSession(), "Standard", Context.empty());
		for (int query : installedQueries) {
			executor.startQuery(query, getActiveSession());
		}
	}

	private ILogicalQueryPart getCopyOfQueryPart(ILogicalQueryPart part) {
		ILogicalQueryPart result = null;
		ArrayList<ILogicalQueryPart> partsList = new ArrayList<ILogicalQueryPart>();
		partsList.add(part);
		Map<ILogicalQueryPart, ILogicalQueryPart> copies = getCopyOfQueryParts(partsList);
		for (Map.Entry<ILogicalQueryPart, ILogicalQueryPart> entry : copies
				.entrySet()) {
			result = entry.getKey();
		}
		return result;
	}

	/**
	 * Returns copies for each QueryPart in List
	 * 
	 * @param parts
	 *            QueryParts to copy
	 * @return Cut copies of Query parts.
	 */
	private Map<ILogicalQueryPart, ILogicalQueryPart> getCopyOfQueryParts(
			Collection<ILogicalQueryPart> parts) {
		Map<ILogicalQueryPart, ILogicalQueryPart> copies = LogicalQueryHelper
				.copyQueryPartsDeep(parts);
		return copies;
	}

	private Collection<ILogicalQueryPart> getRunningQueryParts() {
		ArrayList<ILogicalQueryPart> parts = new ArrayList<ILogicalQueryPart>();
		for (int queryId : executor.getLogicalQueryIds(getActiveSession())) {
			ILogicalQuery query = executor.getLogicalQueryById(queryId,
					getActiveSession());

			ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
			RestructHelper.collectOperators(query.getLogicalPlan(), operators);
			parts.add(new LogicalQueryPart(operators));
		}
		return parts;
	}

	public void testCopy(PeerID otherPeer, int queryPartID) {
		ILogicalQueryPart partToCopy = null;
		for (ILogicalQueryPart part : getRunningQueryParts()) {
			partToCopy = part;
		}
		int lbProcessIdentifier = LoadBalancingQueryCache.getInstance()
				.addQueryPartCopy(partToCopy);
		sendInitiateLoadBalancingMessage(otherPeer, lbProcessIdentifier);
	}

	private void removeTopAOs(ILogicalQueryPart part) {
		ArrayList<ILogicalOperator> toRemove = new ArrayList<>();
		for (ILogicalOperator operator : part.getOperators()) {
			if (operator instanceof TopAO) {
				toRemove.add(operator);
			}
		}

		for (ILogicalOperator topAO : toRemove) {
			topAO.unsubscribeFromAllSinks();
			topAO.unsubscribeFromAllSources();
			part.removeOperator(topAO);
		}
	}

	private void sendAddQueryPartMessage(int loadBalancingProcessID,
			PeerID destinationPeer, String queryPartPql) {
		LoadBalancingAddQueryMessage message = new LoadBalancingAddQueryMessage(
				loadBalancingProcessID, queryPartPql);
		try {
			peerCommunicator.send(destinationPeer, message);
		} catch (PeerCommunicationException e) {
			LOG.debug("Communication excepting while sending query Part");
			e.printStackTrace();
		}
	}

	private void sendCopyConnectionMessage(boolean isSender,
			int loadBalancingProcessID, PeerID destinationPeer,
			String oldPipeId, String newPipeId, String newPeerId) {
		LoadBalancingCopyConnectionMessage message = new LoadBalancingCopyConnectionMessage(
				loadBalancingProcessID, isSender, oldPipeId, newPipeId,
				newPeerId);
		try {
			peerCommunicator.send(destinationPeer, message);
		} catch (PeerCommunicationException e) {
			LOG.debug("Communication excepting while sending query Part");
			e.printStackTrace();
		}
	}

	private Map<ILogicalOperator, Collection<ConnectionToOperator>> stripJxtaReceivers(
			ILogicalQueryPart part) {

		HashMap<ILogicalOperator, Collection<ConnectionToOperator>> result = new HashMap<ILogicalOperator, Collection<ConnectionToOperator>>();
		ArrayList<ILogicalOperator> toRemove = new ArrayList<ILogicalOperator>();
		for (ILogicalOperator operator : part.getOperators()) {
			if (operator instanceof JxtaReceiverAO) {
				JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
				for (LogicalSubscription subscription : receiver
						.getSubscriptions()) {

					ILogicalOperator targetOperator = subscription.getTarget();
					if (!result.containsKey(targetOperator)) {
						result.put(targetOperator,
								new ArrayList<ConnectionToOperator>());
					}
					result.get(targetOperator).add(
							new ConnectionToOperator(targetOperator, receiver
									.getPeerID(), receiver.getPipeID(),
									subscription.getSinkInPort(), subscription
											.getSchema()));

				}
				toRemove.add(receiver);
			}
		}

		for (ILogicalOperator operator : toRemove) {
			operator.unsubscribeFromAllSinks();
			part.removeOperator(operator);
		}
		return result;
	}

	private Map<ILogicalOperator, Collection<ConnectionToOperator>> stripJxtaSenders(
			ILogicalQueryPart part) {
		HashMap<ILogicalOperator, Collection<ConnectionToOperator>> result = new HashMap<ILogicalOperator, Collection<ConnectionToOperator>>();
		ArrayList<ILogicalOperator> toRemove = new ArrayList<ILogicalOperator>();
		for (ILogicalOperator operator : part.getOperators()) {
			if (operator instanceof JxtaSenderAO) {
				JxtaSenderAO sender = (JxtaSenderAO) operator;
				for (LogicalSubscription subscription : sender
						.getSubscribedToSource()) {

					ILogicalOperator incomingOperator = subscription
							.getTarget();

					if (!result.containsKey(incomingOperator)) {
						result.put(incomingOperator,
								new ArrayList<ConnectionToOperator>());
					}
					result.get(incomingOperator).add(
							new ConnectionToOperator(incomingOperator, sender
									.getPeerID(), sender.getPipeID(),
									subscription.getSourceOutPort(),
									subscription.getSchema()));

				}
				toRemove.add(sender);
			}
		}

		for (ILogicalOperator operator : toRemove) {
			operator.unsubscribeFromAllSources();
			part.removeOperator(operator);
		}
		return result;
	}

	private void sendInitiateLoadBalancingMessage(PeerID volunteeringPeer,
			int loadBalancingProcessId) {
		try {
			if (peerCommunicator != null) {
				peerCommunicator
						.send(volunteeringPeer,
								new LoadBalancingInitiateMessage(
										loadBalancingProcessId));
			}
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send Message");
		}
	}

	private ILogicalOperator getLogicalJxtaOperator(boolean lookForSender,
			String pipeID) {
		for (ILogicalQueryPart part : getRunningQueryParts()) {
			for (ILogicalOperator operator : part.getOperators()) {
				if (lookForSender) {
					if (operator instanceof JxtaSenderAO) {
						JxtaSenderAO sender = (JxtaSenderAO) operator;
						if (sender.getPipeID().equals(pipeID)) {
							return sender;
						}

					}
				} else {
					if (operator instanceof JxtaReceiverAO) {
						JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
						if (receiver.getPipeID().equals(pipeID)) {
							return receiver;
						}

					}
				}

			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private IPhysicalOperator getPhysicalJxtaOperator(boolean lookForSender,
			String pipeID) {
		for (IPhysicalQuery query : executor.getExecutionPlan().getQueries()) {
			for (IPhysicalOperator operator : query.getAllOperators()) {
				if (lookForSender) {
					if (operator instanceof JxtaSenderPO) {
						JxtaSenderPO sender = (JxtaSenderPO) operator;
						if (sender.getPipeIDString().equals(pipeID)) {
							return sender;
						}
					}
				} else {
					if (operator instanceof JxtaReceiverPO) {
						JxtaReceiverPO receiver = (JxtaReceiverPO) operator;
						if (receiver.getPipeIDString().equals(pipeID)) {
							return receiver;
						}
					}
				}
			}
		}
		return null;
	}


	@SuppressWarnings("unused")
	private int getQueryWithOperator(ILogicalOperator operator) {
		for (int queryId : executor.getLogicalQueryIds(getActiveSession())) {
			ILogicalQuery query = executor.getLogicalQueryById(queryId,
					getActiveSession());
			ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
			RestructHelper.collectOperators(query.getLogicalPlan(), operators);
			if (operators.contains(operator)) {
				return queryId;
			}
		}
		return -1;
	}

	public static ISession getActiveSession() {
		if (activeSession == null || !activeSession.isValid()) {
			activeSession = UserManagementProvider
					.getSessionmanagement()
					.loginSuperUser(null,
							UserManagementProvider.getDefaultTenant().getName());
		}
		return activeSession;
	}

	protected static PeerID toPeerID(String peerIDString) {
		try {
			final URI id = new URI(peerIDString);
			return (PeerID) IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not get id from peerIDString {}", peerIDString, ex);
			return null;
		}
	}

}