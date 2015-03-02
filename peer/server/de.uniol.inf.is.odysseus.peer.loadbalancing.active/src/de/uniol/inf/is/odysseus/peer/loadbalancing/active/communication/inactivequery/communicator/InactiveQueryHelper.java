package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.communicator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.DownstreamConnection;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.IMessageDeliveryFailedListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.UpstreamConnection;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.status.InactiveQueryMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.status.InactiveQuerySlaveStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.status.JxtaOperatorInformation;

/***
 * Helper class for MovingState Strategy
 * 
 * @author Carsten Cordes
 *
 */
public class InactiveQueryHelper {

	/***
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(InactiveQueryHelper.class);



	@SuppressWarnings("rawtypes")
	public static void addChangeInformation(String pipeID, InactiveQuerySlaveStatus status, boolean isSender) {
		IPhysicalOperator operator = LoadBalancingHelper
				.getPhysicalJxtaOperator(isSender, pipeID);
		if(operator!=null) {
			if(isSender) {
				JxtaSenderPO sender = (JxtaSenderPO)operator;
				JxtaOperatorInformation info = new JxtaOperatorInformation();
				info.setOldPeer(sender.getPeerIDString());
				info.setPipeID(sender.getPipeIDString());
				info.setOperator(sender);
				info.setSender(true);
				status.addChangedPipe(pipeID, info);
			}
			else {
				JxtaReceiverPO receiver = (JxtaReceiverPO)operator;
				JxtaOperatorInformation info = new JxtaOperatorInformation();
				info.setOldPeer(receiver.getPeerIDString());
				info.setPipeID(receiver.getPipeIDString());
				info.setOperator(receiver);
				info.setSender(false);
				status.addChangedPipe(pipeID, info);
			}
		}
	}
	
	/**
	 * Sends abort Message to all Peers involved in LoadBalancing Process
	 * 
	 * @param status
	 *            Master Status
	 */
	public static void notifyInvolvedPeers(InactiveQueryMasterStatus status) {

		IMessageDeliveryFailedListener deliveryFailedListener = InactiveQueryCommunicatorImpl
				.getInstance();

		status.setPhase(InactiveQueryMasterStatus.LB_PHASES.FAILURE);
		// Get Distinct List of involved Peers:
		ArrayList<PeerID> involvedPeers = new ArrayList<PeerID>(
				new HashSet<PeerID>(status.getPeersForPipe().values()));
		involvedPeers.add(status.getVolunteeringPeer());
		for (PeerID peer : involvedPeers) {
			LOG.debug("Asking Peer " + peer, " to abort.");
			status.getMessageDispatcher().sendAbortInstruction(peer,
					deliveryFailedListener);
		}
	}

	/***
	 * Sets Sender or Receiver to new PeerID.
	 * 
	 * @param pipeID
	 *            PipeID of JxtaOperator
	 * @param peerID
	 *            new Peer ID.
	 * @param isSender
	 *            is Operator sender?
	 * @throws LoadBalancingException
	 */
	public static void setNewPeerId(String pipeID, String peerID,
			boolean isSender) throws LoadBalancingException {
		IPhysicalOperator operator = LoadBalancingHelper
				.getPhysicalJxtaOperator(isSender, pipeID);
		ILogicalOperator logicalOp = LoadBalancingHelper.getLogicalJxtaOperator(isSender, pipeID);
		
		if (operator == null) {
			throw new LoadBalancingException("No Sender with pipeID " + pipeID
					+ " found.");
		}
		if (operator instanceof JxtaSenderPO) {
			///LoadBalancingHelper.setNewPeerID((JxtaSenderPO) operator, peerID);
			if(logicalOp!=null) {
				JxtaSenderAO senderAO = (JxtaSenderAO) logicalOp;
				senderAO.setPeerID(peerID);
			}
			else {
				LOG.warn("No according logical sender to existing pyhsical Operator with pipe ID " + pipeID);
			}
		}
		if (operator instanceof JxtaReceiverPO) {
			///LoadBalancingHelper.setNewPeerID((JxtaReceiverPO) operator, peerID);
			if(logicalOp!=null) {
				JxtaReceiverAO receiverAO = (JxtaReceiverAO) logicalOp;
				receiverAO.setPeerID(peerID);
			}
			else {
				LOG.warn("No according logical receiver to existing pyhsical Operator with pipe ID " + pipeID);
			}
		}
		
	}
	

	/**
	 * Send Message to all incoming Peers to add a duplicate connection
	 * 
	 * @param status
	 *            Status
	 */
	public static void notifyDownstreamPeers(InactiveQueryMasterStatus status) {

		IMessageDeliveryFailedListener deliveryFailedListener = InactiveQueryCommunicatorImpl
				.getInstance();

		String volunteeringPeer = status.getVolunteeringPeer().toString();
		ILogicalQueryPart modifiedQueryPart = status.getModifiedPart();
		InactiveQueryMessageDispatcher dispatcher = status.getMessageDispatcher();

		HashMap<String, PeerID> peersForPipe = new HashMap<String, PeerID>();

		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if (operator instanceof JxtaSenderAO) {
				JxtaSenderAO sender = (JxtaSenderAO) operator;

				String pipe = sender.getPipeID();
				PeerID destinationPeer = LoadBalancingHelper.toPeerID(sender
						.getPeerID());
				peersForPipe.put(pipe, destinationPeer);

				dispatcher.sendReplaceReceiverMessage(destinationPeer,
						volunteeringPeer, pipe, deliveryFailedListener);
			}
		}
		status.setPeersForPipe(peersForPipe);

	}

	/**
	 * Send Message to all upstream Peers to add a duplicate connection
	 * 
	 * @param status
	 *            Status
	 */
	public static void notifyUpstreamPeers(InactiveQueryMasterStatus status) {

		IMessageDeliveryFailedListener deliveryFailedListener = InactiveQueryCommunicatorImpl
				.getInstance();

		String volunteeringPeer = status.getVolunteeringPeer().toString();
		ILogicalQueryPart modifiedQueryPart = status.getModifiedPart();
		ArrayList<PeerID> upstreamPeers = new ArrayList<PeerID>();

		InactiveQueryMessageDispatcher dispatcher = status.getMessageDispatcher();

		HashMap<String, PeerID> peersForPipe = status.getPeersForPipe();
		if (peersForPipe == null) {
			peersForPipe = new HashMap<String, PeerID>();
		}

		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if (operator instanceof JxtaReceiverAO) {
				JxtaReceiverAO receiver = (JxtaReceiverAO) operator;

				String pipe = receiver.getPipeID();
				PeerID destinationPeer = LoadBalancingHelper.toPeerID(receiver
						.getPeerID());
				if (!upstreamPeers.contains(destinationPeer))
					upstreamPeers.add(destinationPeer);
				peersForPipe.put(pipe, destinationPeer);

				dispatcher.sendInstallBufferAndReplaceSenderMessage(
						destinationPeer, volunteeringPeer, pipe,
						deliveryFailedListener);
			}
		}
		status.setPeersForPipe(peersForPipe);
		status.setUpstreamPeers(upstreamPeers);

	}

	/**
	 * Relinks a logical Query Part to a new peer.
	 * 
	 * @param modifiedPart
	 *            Query part where receivers/senders should be added
	 * @param status
	 *            current master Status.
	 * @return
	 */
	public static void relinkQueryPart(ILogicalQueryPart modifiedPart,
			InactiveQueryMasterStatus status) {

		LoadBalancingHelper.removeTopAOs(modifiedPart);

		Map<ILogicalOperator, Collection<UpstreamConnection>> incomingConnections = LoadBalancingHelper
				.stripJxtaReceivers(modifiedPart);
		Map<ILogicalOperator, Collection<DownstreamConnection>> outgoingConnections = LoadBalancingHelper
				.stripJxtaSenders(modifiedPart);

		Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper
				.getRelativeSinksOfLogicalQueryPart(modifiedPart);
		Collection<ILogicalOperator> relativeSources = LogicalQueryHelper
				.getRelativeSourcesOfLogicalQueryPart(modifiedPart);

		ArrayList<String> senderPipes = new ArrayList<String>();

		for (ILogicalOperator relativeSource : relativeSources) {
			if (incomingConnections.containsKey(relativeSource)) {
				Collection<UpstreamConnection> connections = incomingConnections
						.get(relativeSource);
				for (UpstreamConnection connection : connections) {
					JxtaReceiverAO receiver = LoadBalancingHelper
							.createReceiverAO(connection, connection.oldPipeID);
					modifiedPart.addOperator(receiver);

				}
			}
		}

		for (ILogicalOperator relativeSink : relativeSinks) {
			if (outgoingConnections.containsKey(relativeSink)) {
				Collection<DownstreamConnection> connections = outgoingConnections
						.get(relativeSink);
				for (DownstreamConnection connection : connections) {
					JxtaSenderAO sender = LoadBalancingHelper.createSenderAO(
							connection, connection.oldPipeID);

					modifiedPart.addOperator(sender);
					senderPipes.add(connection.oldPipeID);
				}
			}
		}
		status.setPipesToSync(senderPipes);
		LOG.debug("Pipes to Sync (later):");
		for (String pipe : senderPipes) {
			LOG.debug(pipe);
		}
		status.setModifiedPart(modifiedPart);
	}

}
