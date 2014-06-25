package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

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

		ConnectionToOperator(ILogicalOperator operator,
				String remotePeerID, String oldPipeID, int port) {
			this.localOperator = operator;
			this.remotePeerID = remotePeerID;
			this.oldPipeID = oldPipeID;
			this.port = port;
		}

		private String remotePeerID;
		private ILogicalOperator localOperator;
		@SuppressWarnings("unused")
		private String oldPipeID;
		private int port;
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
		if(p2pNetworkManager == serv) {
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

		peerCommunicator.addListener(this,
				LoadBalancingInitiateCopyMessage.class);
		peerCommunicator.addListener(this, LoadBalancingInitiateMessage.class);
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

	private void relinkQueryPart(ILogicalQueryPart part, String newPeerID) {
		removeTopAOs(part);
		Map<ILogicalOperator, Collection<ConnectionToOperator>> incomingConnections = stripJxtaReceivers(part);
		Map<ILogicalOperator, Collection<ConnectionToOperator>> outgoingConnections = stripJxtaSenders(part);
		Collection<ILogicalOperator> relativeSources = LogicalQueryHelper
				.getRelativeSinksOfLogicalQueryPart(part);
		Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper
				.getRelativeSourcesOfLogicalQueryPart(part);

		for (ILogicalOperator relativeSource : relativeSources) {
			if(incomingConnections.containsKey(relativeSource)) {
				Collection<ConnectionToOperator> connections = incomingConnections.get(relativeSource);
				for (ConnectionToOperator connection : connections) {
				
					String newPipeID = IDFactory.newPipeID(p2pNetworkManager.getLocalPeerGroupID()).toString();
					JxtaReceiverAO receiver = new JxtaReceiverAO();
					
					receiver.setPipeID(newPipeID);
					receiver.setPeerID(connection.remotePeerID);
					receiver.connectSink(relativeSource, connection.port, 0, relativeSource.getInputSchema(0));
					///SendLoadBalancingCopySenderMessage(connection.remotePeerID,newPeerID,connection.oldPipeID,newPipeID);
				}
			}
		}
		
		
		for(ILogicalOperator relativeSink : relativeSinks) {
			if(outgoingConnections.containsKey(relativeSink)) {
				Collection<ConnectionToOperator> connections = outgoingConnections.get(relativeSink);
				for (ConnectionToOperator connection : connections) {
					String newPipeID = IDFactory.newPipeID(p2pNetworkManager.getLocalPeerGroupID()).toString();
					
					JxtaSenderAO sender = new JxtaSenderAO();
					sender.setPeerID(connection.remotePeerID);
					sender.setPipeID(newPipeID);
					connection.localOperator.connectSink(sender, 0, connection.port, connection.localOperator.getOutputSchema());
					///SendLoadBalancingCopyReceiverMessage(connection.remotePeerID,newPeerID,connection.oldPipeID,newPipeID);
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
			relinkQueryPart(copy,senderPeer.toString());
			String pqlFromQueryPart = LogicalQueryHelper.generatePQLStatementFromQueryPart(copy);
			LOG.debug(pqlFromQueryPart);
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
			if(operator instanceof TopAO) {
				toRemove.add(operator);
			}
		}
		
		for (ILogicalOperator topAO : toRemove) {
			topAO.unsubscribeFromAllSinks();
			topAO.unsubscribeFromAllSources();
			part.removeOperator(topAO);
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
					result.get(targetOperator)
							.add(new ConnectionToOperator(targetOperator,
									receiver.getPeerID(), receiver.getPipeID(),subscription.getSinkInPort()));

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

	private Map<ILogicalOperator, Collection<ConnectionToOperator>> stripJxtaSenders(ILogicalQueryPart part) {
		HashMap<ILogicalOperator, Collection<ConnectionToOperator>> result = new HashMap<ILogicalOperator, Collection<ConnectionToOperator>>();
		ArrayList<ILogicalOperator> toRemove = new ArrayList<ILogicalOperator>();
		for (ILogicalOperator operator : part.getOperators()) {
			if (operator instanceof JxtaSenderAO) {
				JxtaSenderAO sender = (JxtaSenderAO) operator;
				for (LogicalSubscription subscription : sender
						.getSubscribedToSource()) {

					ILogicalOperator incomingOperator = subscription.getTarget();
							
					if (!result.containsKey(incomingOperator)) {
						result.put(incomingOperator,
								new ArrayList<ConnectionToOperator>());
					}
					result.get(incomingOperator)
							.add(new ConnectionToOperator(incomingOperator,
									sender.getPeerID(), sender.getPipeID(),subscription.getSourceOutPort()));

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

	@SuppressWarnings("unused")
	private JxtaReceiverAO getLocalReceiverByPipeID(String pipeID) {
		for(ILogicalQueryPart part : getRunningQueryParts()) {
			for(ILogicalOperator operator : part.getOperators()) {
				if(operator instanceof JxtaReceiverAO) {
					JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
					if(receiver.getPipeID().equals(pipeID)) {
						return receiver;
					}
					
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	private JxtaSenderAO getLocalSenderByPipeID(String pipeID) {
		for(ILogicalQueryPart part : getRunningQueryParts()) {
			for(ILogicalOperator operator : part.getOperators()) {
				if(operator instanceof JxtaSenderAO) {
					JxtaSenderAO sender = (JxtaSenderAO) operator;
					if(sender.getPipeID().equals(pipeID)) {
						return sender;
					}
					
				}
			}
		}
		return null;
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

}