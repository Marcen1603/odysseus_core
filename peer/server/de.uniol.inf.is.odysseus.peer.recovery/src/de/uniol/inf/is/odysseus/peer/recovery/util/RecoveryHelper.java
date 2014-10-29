package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.physicaloperator.RecoveryBufferPO;

public class RecoveryHelper {

	private static final Logger LOG = LoggerFactory.getLogger(RecoveryHelper.class);

	/**
	 * Installs and executes a query from PQL.
	 * 
	 * @param pql
	 *            PQL to execute.
	 */
	public static Collection<Integer> installAndRunQueryPartFromPql(String pql) {

		IServerExecutor executor = RecoveryCommunicator.getExecutor();
		ISession session = RecoveryCommunicator.getActiveSession();

		Collection<Integer> installedQueries = executor.addQuery(pql, "PQL", session, "Standard", Context.empty());

		// TODO send success message

		return installedQueries;
	}

	/**
	 * If you need the PeerId for a given PeerName Copied from PeerConsole.
	 * 
	 * @param peerName
	 *            Name of the peer you want to have the ID from
	 * @return PeerID
	 */
	public static Optional<PeerID> determinePeerID(String peerName) {
		for (PeerID pid : RecoveryCommunicator.getP2pDictionary().getRemotePeerIDs()) {
			if (RecoveryCommunicator.getP2pDictionary().getRemotePeerName(pid).equals(peerName)) {
				return Optional.of(pid);
			}
		}
		return Optional.absent();
	}

	/**
	 * If you have the peerId and want to have the name of the peer
	 * 
	 * @param peerId
	 *            Peer you want to have the name from
	 * @return Name of the peer
	 */
	public static String determinePeerName(PeerID peerId) {
		return RecoveryCommunicator.getP2pDictionary().getRemotePeerName(peerId);
	}

	@SuppressWarnings("rawtypes")
	/**
	 * Get Physical JxtaOperator (Sender or Receiver) by PipeID.
	 * Copied from LoadBalancingHelper
	 * @param lookForSender true if we look for a sender, false if we look for receiver.
	 * @param pipeID Pipe id of Sender we look for.
	 * @return Operator (if found) or null.
	 */
	public static IPhysicalOperator getPhysicalJxtaOperator(boolean lookForSender, String pipeID) {

		IServerExecutor executor = RecoveryCommunicator.getExecutor();

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

	/**
	 * Get Logical JxtaOperator (Sender or Receiver) by PipeID. Copied from
	 * LoadBalancingHelper
	 * 
	 * @param lookForSender
	 *            true if we look for a sender, false if we look for receiver.
	 * @param pipeID
	 *            Pipe id of Sender we look for.
	 * @return Operator (if found) or null.
	 */
	public static ILogicalOperator getLogicalJxtaOperator(boolean lookForSender, String pipeID) {

		for (ILogicalQueryPart part : getInstalledQueryParts()) {
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

	/**
	 * Get all currently Installed Queries as Query Parts. Copied from
	 * LoadBalancingHelper
	 * 
	 * @return List of installed LogicalQueryParts.
	 */
	public static Collection<ILogicalQueryPart> getInstalledQueryParts() {

		IServerExecutor executor = RecoveryCommunicator.getExecutor();
		ISession session = RecoveryCommunicator.getActiveSession();

		ArrayList<ILogicalQueryPart> parts = new ArrayList<ILogicalQueryPart>();
		for (int queryId : executor.getLogicalQueryIds(session)) {
			ILogicalQuery query = executor.getLogicalQueryById(queryId, session);

			ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
			RestructHelper.collectOperators(query.getLogicalPlan(), operators);
			parts.add(new LogicalQueryPart(operators));
		}
		return parts;
	}

	/**
	 * 
	 * @return All JxtaSenderPOs which can be found in the execution plan on
	 *         this peer
	 */
	@SuppressWarnings("rawtypes")
	public static List<JxtaSenderPO> getJxtaSenders() {
		List<JxtaSenderPO> senders = new ArrayList<JxtaSenderPO>();

		Iterator<IPhysicalQuery> queryIterator = RecoveryCommunicator.getExecutor().getExecutionPlan().getQueries()
				.iterator();
		// Iterate through all queries we have installed
		while (queryIterator.hasNext()) {
			IPhysicalQuery query = queryIterator.next();
			List<IPhysicalOperator> roots = query.getRoots();

			// Get the roots for each installed query (there we can find the
			// JxtaSenders)
			for (IPhysicalOperator root : roots) {
				if (root instanceof JxtaSenderPO) {
					JxtaSenderPO sender = (JxtaSenderPO) root;
					senders.add(sender);
				}
			}
		}

		return senders;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addNewSender(JxtaSenderPO originalSender, PeerID newPeer) {
		// TEST Update sender
		// Goal: install a new sender which officially sends to the new
		// receiver so that if we stop a query, it will stop on the new
		// peer, too

		// New JxtaSender which should send to the new peer
		JxtaSenderAO originalSenderAO = (JxtaSenderAO) RecoveryHelper.getLogicalJxtaOperator(true,
				originalSender.getPipeIDString());
		JxtaSenderAO jxtaSenderAO = (JxtaSenderAO) originalSenderAO.clone();
		jxtaSenderAO.setPeerID(newPeer.toString());

		JxtaSenderPO jxtaSender = null;
		try {
			jxtaSender = new JxtaSenderPO(jxtaSenderAO);
		} catch (DataTransmissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Now do the subscriptions
		jxtaSender.setOutputSchema(originalSender.getOutputSchema());
		AbstractPhysicalSubscription subscription = originalSender.getSubscribedToSource(0);

		if (subscription.getTarget() instanceof AbstractPipe) {
			((AbstractPipe) subscription.getTarget()).subscribeSink(jxtaSender, 0, subscription.getSourceOutPort(),
					subscription.getSchema(), true, subscription.getOpenCalls());

			jxtaSender.subscribeToSource(subscription.getTarget(), subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
		} else if (subscription.getTarget() instanceof AbstractSource) {
			((AbstractSource) subscription.getTarget()).subscribeSink(jxtaSender, 0, subscription.getSourceOutPort(),
					subscription.getSchema(), true, subscription.getOpenCalls());

			jxtaSender.subscribeToSource(subscription.getTarget(), subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
		}

		List<IPhysicalOperator> plan = new ArrayList<IPhysicalOperator>();
		plan.add(jxtaSender);
		RecoveryCommunicator.getExecutor().addQuery(plan, RecoveryCommunicator.getActiveSession(), "Standard");

	}

	/**
	 * Inserts an operator between a sink and its sources on a specific port.
	 * Copied from LoadBalancingHelper.
	 * 
	 * @param sink
	 *            Operator after operator to insert
	 * @param operatorToInsert
	 *            Operator to insert before sink
	 * @param port
	 *            input port of sink
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void insertOperatorBefore(ISink sink, AbstractPipe operatorToInsert, int port) {

		AbstractPhysicalSubscription subscription = (AbstractPhysicalSubscription) sink.getSubscribedToSource(port);

		sink.unsubscribeFromSource(subscription);
		operatorToInsert.subscribeToSource(subscription.getTarget(), subscription.getSinkInPort(),
				subscription.getSourceOutPort(), subscription.getSchema());
		operatorToInsert.subscribeSink(sink, subscription.getSinkInPort(), subscription.getSinkInPort(),
				subscription.getSchema(), true, subscription.getOpenCalls());
		operatorToInsert.addOwner(sink.getOwner());

	}

	/***
	 * Adds a buffer for each incoming port of a jxta Sender. Copied from
	 * MovingStateHelper from Loadbalancing
	 * 
	 * @param pipeID
	 *            PipeID of JxtaSender
	 * @return List of added Buffers.
	 */
	@SuppressWarnings("rawtypes")
	public static List<RecoveryBufferPO> insertBuffer(String pipeID) {
		IPhysicalOperator operator = getPhysicalJxtaOperator(true, pipeID);
		if (operator == null) {
			LOG.error("No Sender with PipeID " + pipeID + " found.");
			// TODO Error
			return null;
		}
		JxtaSenderPO sender = (JxtaSenderPO) operator;

		ArrayList<RecoveryBufferPO> addedBuffers = new ArrayList<RecoveryBufferPO>();

		for (int i = 0; i < sender.getInputPortCount(); i++) {
			RecoveryBufferPO<IStreamObject<ITimeInterval>> buffer = new RecoveryBufferPO<IStreamObject<ITimeInterval>>();
			insertOperatorBefore(sender, buffer, i);
			addedBuffers.add(buffer);
		}

		return addedBuffers;

	}
}
