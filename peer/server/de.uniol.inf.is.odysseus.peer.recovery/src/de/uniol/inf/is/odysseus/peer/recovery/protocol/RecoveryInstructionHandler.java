package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.Collection;
import java.util.List;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryInstructionAckMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryInstructionFailMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryInstructionMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.BuddyHelper;
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

	private static final Logger LOG = LoggerFactory
			.getLogger(RecoveryInstructionHandler.class);

	private static IRecoveryCommunicator recoveryCommunicator;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPQLGenerator pqlGenerator;

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

	// called by OSGi-DS
	public static void bindPQLGenerator(IPQLGenerator generator) {
		pqlGenerator = generator;
	}

	// called by OSGi-DS
	public static void unbindPQLGenerator(IPQLGenerator generator) {
		if (pqlGenerator == generator)
			pqlGenerator = null;
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
			holdOn(instructionMessage.getPipeId());
			break;
		case RecoveryInstructionMessage.GO_ON:
			goOn(instructionMessage.getPipeId());
			break;
		case RecoveryInstructionMessage.ADD_QUERY:
			addQuery(sender, instructionMessage);
			break;
		case RecoveryInstructionMessage.UPDATE_RECEIVER:
			updateReceiver(instructionMessage.getNewSender(),
					instructionMessage.getPipeId(),
					instructionMessage.getSharedQueryId());
			break;
		case RecoveryInstructionMessage.BE_BUDDY:
			beBuddy(sender, instructionMessage.getSharedQueryId(),
					instructionMessage.getPql());
			break;
		}
	}

	public static void handleAckInstruction(PeerID senderPeer,
			RecoveryInstructionAckMessage instruction) {
		
	}

	public static void handleFailedInstruction(PeerID senderPeer,
			RecoveryInstructionFailMessage instruction) {

	}

	private static void holdOn(PipeID pipeId) {
		// Here we want to store the tuples
		RecoveryHelper.startBuffering(pipeId.toString());
	}

	private static void goOn(PipeID pipeId) {
		// Here we want to empty the buffer and go on sending the tuples to the
		// next peer
		RecoveryHelper.resumeFromBuffering(pipeId.toString());
	}

	/**
	 * Adds a query (installs and runs it) and saves the new backup-information.
	 * If neccecary, searches for a buddy
	 * 
	 * @param pql
	 *            The PQL String to install
	 * @param sharedQueryId
	 *            The id of the shared query where this PQL belongs to
	 */
	@SuppressWarnings("rawtypes")
	private static void addQuery(PeerID senderPeer, RecoveryInstructionMessage instructionMessage) {
		if (!RecoveryCommunicator.getExecutor().isPresent()) {
			LOG.error("No executor bound!");
			return;
		}
		
		Collection<Integer> installedQueries;  
		try {
			installedQueries = RecoveryHelper
					.installAndRunQueryPartFromPql(instructionMessage.getPqlQuery());
			if (installedQueries == null || installedQueries.size() == 0){
				LOG.error("Installing QueryPart on Peer failed. Searching for other peers.");
				recoveryCommunicator.sendAddQueryFail(senderPeer, instructionMessage);
				return;
			}
		} catch (Exception e) {
			LOG.error("Installing QueryPart on Peer failed. Searching for other peers.");
			recoveryCommunicator.sendAddQueryFail(senderPeer, instructionMessage);
			return;
		}
		// installing query was successful, send ACK
		recoveryCommunicator.sendAddQueryAck(senderPeer, instructionMessage);

		// Call "receiveFromNewPeer" on the subsequent receiver so that that
		// peer creates a socket-connection to us
		IServerExecutor executor = RecoveryCommunicator.getExecutor().get();
		for (IPhysicalQuery query : executor.getExecutionPlan().getQueries()) {
			if (installedQueries.contains(query.getID())) {
				for (IPhysicalOperator operator : query.getAllOperators()) {
					if (operator instanceof JxtaSenderPO) {
						JxtaSenderPO sender = (JxtaSenderPO) operator;

						// For this sender we want to get the peer to which
						// it sends to update the receiver on the other peer

						PeerID peer = RecoveryHelper.convertToPeerId(sender
								.getPeerIDString());
						// To this peer we have to send an "UPDATE_RECEIVER"
						// message
						PipeID pipe = RecoveryHelper.convertToPipeId(sender
								.getPipeIDString());
						PeerID ownPeerId = p2pNetworkManager.getLocalPeerID();

						if (peer != null && pipe != null)
							recoveryCommunicator.sendUpdateReceiverMessage(
									peer, ownPeerId, pipe, instructionMessage.getSharedQueryId());

					} else if (operator instanceof JxtaReceiverPO) {
						// GO ON
						// -----
						JxtaReceiverPO receiver = (JxtaReceiverPO) operator;

						// For this receiver, we want to tell the sender that he
						// can go on
						PeerID peer = RecoveryHelper.convertToPeerId(receiver
								.getPeerIDString());
						PipeID pipe = RecoveryHelper.convertToPipeId(receiver
								.getPipeIDString());

						if (peer != null && pipe != null)
							recoveryCommunicator.sendGoOnMessage(peer, pipe);
					}
				}
			}
		}

		// Add this info to the local-backup-info
		IRecoveryBackupInformation backupInfo = new BackupInformation();
		backupInfo.setSharedQuery(instructionMessage.getSharedQueryId());
		backupInfo.setLocalPQL(instructionMessage.getPqlQuery());
		backupInfo.setLocationPeer(p2pNetworkManager.getLocalPeerID());
		backupInfo.setPQL("");
		LocalBackupInformationAccess.getStore().add(backupInfo);

		if (BuddyHelper.needBuddy(instructionMessage.getPqlQuery())) {
			// We don't have a receiver, thus we need a buddy
			recoveryCommunicator.chooseBuddyForQuery(instructionMessage.getSharedQueryId());
		}
	}

	@SuppressWarnings("rawtypes")
	private static void updateReceiver(PeerID newSender, PipeID pipeId,
			ID sharedQueryId) {

		if (!RecoveryCommunicator.getExecutor().isPresent()) {

			LOG.error("No executor bound!");
			return;

		}

		// 1. Get the receiver, which we have to update
		Collection<IPhysicalQuery> queries = RecoveryCommunicator.getExecutor()
				.get().getExecutionPlan().getQueries();
		for (IPhysicalQuery query : queries) {
			for (IPhysicalOperator op : query.getAllOperators()) {
				if (op instanceof JxtaReceiverPO) {
					JxtaReceiverPO receiver = (JxtaReceiverPO) op;
					if (receiver.getPipeIDString().equals(pipeId.toString())) {
						// This should be the receiver we have to update
						try {
							receiver.receiveFromNewPeer(newSender.toString());
							break;
						} catch (DataTransmissionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * Saves, that this peer is a buddy for the given peer and saves the
	 * corresponding backup-information
	 * 
	 * @param sender
	 *            The sender of the message - this is the peer we will be the
	 *            buddy for
	 * @param sharedQueryId
	 *            The id of the shared query we ware the buddy for
	 * @param pqls
	 *            The PQL-Strings with the information about the peer we are the
	 *            buddy for
	 */
	private static void beBuddy(PeerID sender, ID sharedQueryId,
			List<String> pqls) {
		LocalBackupInformationAccess.addBuddy(sender, sharedQueryId);
		IRecoveryBackupInformation info = new BackupInformation();
		info.setAboutPeer(sender);
		info.setSharedQuery(sharedQueryId);
		String totalPQL = "";
		for (String pql : pqls) {
			totalPQL += " " + pql;
		}
		info.setPQL(totalPQL);
		// This info is meant to be used in this peer
		info.setLocationPeer(RecoveryCommunicator.getP2PNetworkManager().get()
				.getLocalPeerID());
		LocalBackupInformationAccess.getStore().add(info);
		LOG.debug("I am now the buddy for {}", RecoveryCommunicator
				.getPeerDictionary().get().getRemotePeerName(sender));
	}

}
