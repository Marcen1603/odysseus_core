package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.Collection;

import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAddQueryMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

/**
 * This class handles incoming RecoveryInstructionMessages, e.g., to install a new query for recovery.
 * 
 * @author Tobias Brandt
 * 
 */
public class RecoveryInstructionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RecoveryInstructionHandler.class);

	private static IRecoveryCommunicator recoveryCommunicator;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPQLGenerator pqlGenerator;

	// called by OSGi-DS
	public static void bindRecoveryCommunicator(IRecoveryCommunicator communicator) {
		recoveryCommunicator = communicator;
	}

	// called by OSGi-DS
	public static void unbindRecoveryCommunicator(IRecoveryCommunicator communicator) {
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

	public static void handleAddQueryInstruction(RecoveryAddQueryMessage message, PeerID sender) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(sender);

		addQuery(sender, message);
	}

	/**
	 * Adds a query (installs and runs it) and saves the new backup-information. If necessary, searches for a buddy
	 * 
	 * @param pql
	 *            The PQL String to install
	 * @param sharedQueryId
	 *            The id of the shared query where this PQL belongs to
	 */
	@SuppressWarnings("rawtypes")
	private static void addQuery(PeerID senderPeer, RecoveryAddQueryMessage instructionMessage) {
		Preconditions.checkNotNull(recoveryCommunicator);
		if (!RecoveryCommunicator.getExecutor().isPresent()) {
			LOG.error("No executor bound!");
			return;
		}

		Collection<Integer> installedQueries;
		try {
			installedQueries = RecoveryHelper.installAndRunQueryPartFromPql(instructionMessage.getPQLCode());
			if (installedQueries == null || installedQueries.size() == 0) {
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

		LOG.debug("Installed query for recovery.");

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

						PeerID peer = RecoveryHelper.convertToPeerId(sender.getPeerIDString());
						// To this peer we have to send an "UPDATE_RECEIVER"
						// message
						PipeID pipe = RecoveryHelper.convertToPipeId(sender.getPipeIDString());
						PeerID ownPeerId = p2pNetworkManager.getLocalPeerID();

						if (peer != null && pipe != null)
							recoveryCommunicator.sendUpdateReceiverMessage(peer, ownPeerId, pipe,
									instructionMessage.getSharedQueryId());

					} else if (operator instanceof JxtaReceiverPO) {
						// GO ON
						// -----
						JxtaReceiverPO receiver = (JxtaReceiverPO) operator;

						// For this receiver, we want to tell the sender that he
						// can go on
						PeerID peer = RecoveryHelper.convertToPeerId(receiver.getPeerIDString());
						PipeID pipe = RecoveryHelper.convertToPipeId(receiver.getPipeIDString());

						if (peer != null && pipe != null)
							recoveryCommunicator.sendGoOnMessage(peer, pipe);
					}
				}
			}
		}

		// Add this info to the local-backup-info
		IRecoveryBackupInformation backupInfo = new BackupInformation();
		backupInfo.setSharedQuery(instructionMessage.getSharedQueryId());
		backupInfo.setLocalPQL(instructionMessage.getPQLCode());
		backupInfo.setLocationPeer(p2pNetworkManager.getLocalPeerID());
		backupInfo.setPQL("");
		LocalBackupInformationAccess.getStore().add(backupInfo);

		// TODO Test this. For now, we always need a buddy, cause the previous peers won't have updated
		// backup-information.

		// if (BuddyHelper.needBuddy(instructionMessage.getPQLCode())) {
		// // We don't have a receiver, thus we need a buddy
		// recoveryCommunicator.chooseBuddyForQuery(instructionMessage
		// .getSharedQueryId());
		// }

		recoveryCommunicator.chooseBuddyForQuery(instructionMessage.getSharedQueryId());
	}

}
