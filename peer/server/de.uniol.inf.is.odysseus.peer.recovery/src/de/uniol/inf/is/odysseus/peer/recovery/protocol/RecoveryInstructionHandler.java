package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.net.URI;
import java.util.Collection;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryInstructionMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

/**
 * This class handles incoming RecoveryInstructionMessages, e.g., to install a
 * new query for recovery.
 * 
 * @author Tobias Brandt
 *
 */
public class RecoveryInstructionHandler {

	private static IRecoveryCommunicator recoveryCommunicator;
	private static IP2PNetworkManager p2pNetworkManager;

	// called by OSGi-DS
	public static void bindRecoveryCommunicator(
			IRecoveryCommunicator communicator) {
		recoveryCommunicator = communicator;
	}

	// called by OSGi-DS
	public static void unbindRecoveryCommunicator(
			IRecoveryCommunicator communicator) {
		if (recoveryCommunicator == communicator)
			recoveryCommunicator = null;
	}

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

	/**
	 * Handles an incoming instruction-message
	 * 
	 * @param instructionMessage
	 *            The incoming message
	 */
	public static void handleInstruction(PeerID sender,
			RecoveryInstructionMessage instructionMessage) {
		switch (instructionMessage.getMessageType()) {
		case RecoveryInstructionMessage.HOLD_ON:
			holdOn(instructionMessage.getSharedQueryId());
			break;
		case RecoveryInstructionMessage.ADD_QUERY:
			addQuery(instructionMessage.getPqlQuery());
			break;
		case RecoveryInstructionMessage.UPDATE_RECEIVER:
			updateReceiver(instructionMessage.getNewSender(), instructionMessage.getPipeId());
			break;
		case RecoveryInstructionMessage.BE_BUDDY:
			beBuddy(sender, instructionMessage.getSharedQueryId());
			break;
		}
	}

	private static void holdOn(ID queryId) {
		// Here we want to store the tuples
		// RecoveryTupleStorePO<IStreamObject<? extends ITimeInterval>> store =
		// new RecoveryTupleStorePO<IStreamObject<? extends ITimeInterval>>();

	}

	@SuppressWarnings("rawtypes")
	private static void addQuery(String pql) {
		Collection<Integer> installedQueries = RecoveryHelper
				.installAndRunQueryPartFromPql(pql);

		// TODO Call "receiveFromNewPeer" on the subsequent receiver
		IServerExecutor executor = RecoveryCommunicator.getExecutor();

		for (IPhysicalQuery query : executor.getExecutionPlan().getQueries()) {
			if (installedQueries.contains(query.getID())) {
				for (IPhysicalOperator operator : query.getAllOperators()) {
					if (operator instanceof JxtaSenderPO) {
						JxtaSenderPO sender = (JxtaSenderPO) operator;
						// For this sender we want to get the peer to which it
						// sends

						try {
							String peerIdString = sender.getPeerIDString();
							URI peerUri = new URI(peerIdString);
							PeerID peer = PeerID.create(peerUri);
							// To this peer we have to send an "UPDATE_RECEIVER"
							// message
							String pipeIdString = sender.getPipeIDString();
							URI pipeUri = new URI(pipeIdString);
							PipeID pipe = PipeID.create(pipeUri);

							PeerID ownPeerId = p2pNetworkManager
									.getLocalPeerID();

							recoveryCommunicator.sendUpdateReceiverMessage(
									peer, ownPeerId, pipe);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private static void updateReceiver(PeerID newSender, PipeID pipeId) {
		// 1. Get the receiver, which we have to update
		Collection<IPhysicalQuery> queries = RecoveryCommunicator.getExecutor()
				.getExecutionPlan().getQueries();
		for (IPhysicalQuery query : queries) {
			for (IPhysicalOperator op : query.getAllOperators()) {
				if (op instanceof JxtaReceiverPO) {
					JxtaReceiverPO receiver = (JxtaReceiverPO) op;
					if (receiver.getPipeIDString().equals(pipeId.toString())) {
						// This should be the receiver we have to update
						try {
							receiver.receiveFromNewPeer(newSender.toString());
						} catch (DataTransmissionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private static void beBuddy(PeerID sender, ID sharedQueryId) {
		LocalBackupInformationAccess.addBuddy(sender, sharedQueryId);
	}

}
